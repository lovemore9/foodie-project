package com.imooc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.imooc.bo.ShopCartBO;
import com.imooc.mapper.OrderItemsMapper;
import com.imooc.mapper.OrderStatusMapper;
import com.imooc.mapper.OrdersMapper;
import com.imooc.pojo.*;
import com.imooc.service.ItemService;
import com.imooc.service.OrderService;
import com.imooc.service.UserAddressService;
import com.imooc.utils.DateUtil;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.RedisOperator;
import com.imooc.vo.MerchantOrdersVO;
import com.imooc.vo.OrderVO;
import com.imooc.vo.SubmitOrderVO;
import enums.OrderStatusEnum;
import enums.YesOrNo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 类简要说明
 *
 * @author wangyujin
 * @version 1.0
 * </pre>
 * Created on 2020-02-24 22:40
 * </pre>
 */

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class OrderServiceImpl implements OrderService {

    private final OrdersMapper ordersMapper;

    private final OrderItemsMapper orderItemsMapper;

    private final OrderStatusMapper orderStatusMapper;

    private final UserAddressService userAddressService;

    private final ItemService itemService;

    private final Sid sid;

    private final RedisOperator redisOperator;

    @Override
    @Transactional(propagation = Propagation.REQUIRED) //创建用REQUIRED
    public OrderVO createOrder(SubmitOrderVO submitOrderVO, List<ShopCartBO> shopCartList) {

        String userId = submitOrderVO.getUserId();
        String addressId  = submitOrderVO.getAddressId();
        String ItemSpecIds = submitOrderVO.getItemSpecIds();
        Integer payMethod = submitOrderVO.getPayMethod();
        String leftMsg = submitOrderVO.getLeftMsg();
        //包邮啦 邮费设置0了
        Integer postAmount  = 0;

        String orderId = sid.nextShort();

        //1.新订单数据保存
        UserAddress userAddress = userAddressService.queryUserAddress(userId, addressId);
        if (BeanUtil.isEmpty(userAddress)) {
        //return IMOOCJSONResult.errorMsg("用户地址信息不正确");
        }
        String[] itemSpecIdArr = ItemSpecIds.split(",");
        //商品原价价格
        Integer totalAmount = 0;
        //折扣价格
        Integer realPayAmount = 0;
        Date now = new Date();
        Orders orders = Orders.builder()
                .id(orderId)
                .userId(userId)
                .receiverName(userAddress.getReceiver())
                .receiverAddress(userAddress.getProvince() + "" +
                        userAddress.getCity() + "" +
                        userAddress.getProvince() + ""
                        +userAddress.getDetail())
                .receiverMobile(userAddress.getMobile())
                .totalAmount((long)totalAmount)
                .realPayAmount((long)realPayAmount)
                .postAmount((long)postAmount)
                .payMethod((long)payMethod)
                .leftMsg(leftMsg)
                .isComment((long)YesOrNo.NO.type)
                .isDelete((long)YesOrNo.NO.type)
                .createdTime(now)
                .updatedTime(now).build();

        List<ShopCartBO> toBoRemoveList = new ArrayList<>();
        //2.ItemSpecIds循环商品信息
        for (String specId : itemSpecIdArr) {
            ItemsSpec itemsSpec = itemService.queryItemSpecById(specId);
            //商品价格 X 购物车商品数量 在redis中获取
            //直接找到specId的商品
            List<ShopCartBO> current_shopCartBO = shopCartList.stream().filter((ShopCartBO bo) -> bo.getSpecId().equals(specId)).limit(1).
                    collect(Collectors.toList());
            Integer buyCount = null;
            if (ObjectUtil.isNotNull(current_shopCartBO) && !current_shopCartBO.isEmpty()) {
                buyCount = current_shopCartBO.get(0).getBuyCounts();
                toBoRemoveList.addAll(current_shopCartBO);
            } else {
                return null;
            }
            totalAmount += (int)itemsSpec.getPriceNormal() * buyCount;
            realPayAmount += (int)itemsSpec.getPriceDiscount() * buyCount;
            //2.1根据商品id获取商品信息及商品图片
            String itemId = itemsSpec.getItemId();
            Items items = itemService.queryItemById(itemId);
            String imgUrl = itemService.queryItemMainImgById(itemId);

            //2.2循环保存订单数据进数据库
            OrderItems orderItem = OrderItems.builder()
                    .id(sid.nextShort())
                    .orderId(orderId)
                    .itemId(itemId)
                    .itemName(items.getItemName())
                    .itemImg(imgUrl)
                    .buyCounts(buyCount)
                    .itemSpecId(itemsSpec.getItemId())
                    .itemSpecName(itemsSpec.getName())
                    .price(itemsSpec.getPriceDiscount()).build();
            //2.3插入订单项
            orderItemsMapper.insert(orderItem);

            //2.4在用户提交订单以后规格表扣掉库存
            itemService.decreaseItemSpecStock(specId, buyCount);

        }
        orders.setTotalAmount((long)totalAmount);
        orders.setRealPayAmount((long)realPayAmount);

        //插入订单
        ordersMapper.insert(orders);

        //3.保存订单状态
        OrderStatus waitPayOrderStatus = OrderStatus.builder()
                .orderId(orderId)
                .createdTime(now)
                .orderStatus((long)OrderStatusEnum.WAIT_PAY.type).build();

        orderStatusMapper.insert(waitPayOrderStatus);

        //4.构建商户订单，用户传给支付中心
        MerchantOrdersVO mechantOrderVo = MerchantOrdersVO.builder()
                .merchantOrderId(orderId)
                .merchantUserId(userId)
                .amount(realPayAmount + postAmount)
                .payMethod(payMethod).build();

        //5.构建自定义订单VO
        OrderVO orderVO = OrderVO.builder()
                .orderId(orderId)
                .merchantOrdersVO(mechantOrderVo)
                .toBeRemoveList(toBoRemoveList)
                .build();

        //返回生成的订单Id
        return orderVO;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED) //创建用REQUIRED
    public int updateOrderStatus(String orderId, Integer status) {
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderStatus((long)status);
        if (ObjectUtil.equal((int)status, OrderStatusEnum.WAIT_DELIVER.type)) {
            orderStatus.setPayTime(new Date());
        }
        if (ObjectUtil.equal((int)status, OrderStatusEnum.WAIT_RECEIVE.type)) {
            orderStatus.setDeliverTime(new Date());
        }
        if (ObjectUtil.equal((int)status, OrderStatusEnum.SUCCESS.type)) {
            orderStatus.setSuccessTime(new Date());
        }
        orderStatus.setOrderId(orderId);
        return orderStatusMapper.updateById(orderStatus);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS) //查询用supperots
    public OrderStatus queryOrdersIsPayByOrderId(String orderId) {
        OrderStatus orderStatus = orderStatusMapper.selectById(orderId);
        return orderStatus;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED) //创建用REQUIRED
    public void clouseOrder() {
        //查询所有未付款订单，判断时间是否超时，超时则关闭
        List<OrderStatus> order_status = orderStatusMapper.selectList(
                new QueryWrapper<OrderStatus>()
                        .eq("order_status", OrderStatusEnum.WAIT_PAY.type));

        List<String> orderIds = order_status.stream()
                .filter(orderStatus -> DateUtil.daysBetween(orderStatus.getCreatedTime(), new Date()) >= 1)
                .map(orderStatus -> orderStatus.getOrderId())
                .collect(Collectors.toList());

        if (!orderIds.isEmpty()) {
            OrderStatus closeOrders = new OrderStatus();
            closeOrders.setOrderStatus((long)OrderStatusEnum.CLOSE.type);
            closeOrders.setCloseTime(new Date());
            orderStatusMapper.update(
                    closeOrders,
                    new QueryWrapper<OrderStatus>()
                            .in("order_id", orderIds));
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED) //创建用REQUIRED
    public void removeOrder(String userId, String orderId) {
        Orders orders = new Orders();
        orders.setIsDelete((long)YesOrNo.YES.type);
        ordersMapper.update(orders,
                new QueryWrapper<Orders>()
                    .eq("user_id", userId)
                    .eq("id", orderId));
    }

}
