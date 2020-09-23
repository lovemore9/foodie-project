package com.imooc.service.center.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.bo.center.OrderItemsCommentBO;
import com.imooc.mapper.ItemsCommentsMapper;
import com.imooc.mapper.OrderItemsMapper;
import com.imooc.mapper.OrderStatusMapper;
import com.imooc.mapper.OrdersMapper;
import com.imooc.mapper.ustomize.CommentsCustomizeMapper;
import com.imooc.pojo.ItemsComments;
import com.imooc.pojo.OrderItems;
import com.imooc.pojo.OrderStatus;
import com.imooc.pojo.Orders;
import com.imooc.service.center.MyCommentsService;
import com.imooc.vo.MyCommentVO;
import com.imooc.vo.OrderStatusCountsVO;
import enums.OrderStatusEnum;
import enums.YesOrNo;
import lombok.RequiredArgsConstructor;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 类简要说明
 *
 * @author wangyujin
 * @version 1.0
 * </pre>
 * Created on 2020-03-16 22:01
 * </pre>
 */
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class MyCommentsServiceImpl implements MyCommentsService {

    private final OrderItemsMapper orderItemsMapper;

    private final OrdersMapper ordersMapper;

    private final Sid sid;

    private final ItemsCommentsMapper itemsCommentsMapper;

    private final OrderStatusMapper oderStatusMapper;

    private final CommentsCustomizeMapper commentsCustomizeMapper;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<OrderItems> queryPendingComment(String orderId) {
        List<OrderItems> orderitemsList = orderItemsMapper.selectList(
                new QueryWrapper<OrderItems>()
                        .eq("order_id", orderId));
        return orderitemsList;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveComments(String orderId, String userId, List<OrderItemsCommentBO> orderItemsCommentBOList) {

        orderItemsCommentBOList.forEach(item -> {
            ItemsComments itemsComments = new ItemsComments();
            BeanUtils.copyProperties(item, itemsComments);
            itemsComments.setId(sid.nextShort());
            itemsComments.setUserId(userId);
            itemsComments.setSepcName(item.getItemSpecName());
            itemsComments.setCommentLevel(item.getCommentLevel());

            itemsComments.setCreatedTime(new Date());
            itemsComments.setUpdatedTime(new Date());
            itemsCommentsMapper.insert(itemsComments);
        });

        //订单表标记已经评论
        Orders orders = new Orders();
        orders.setId(orderId);
        orders.setIsComment((long)YesOrNo.YES.type);
        orders.setUpdatedTime(new Date());
        ordersMapper.updateById(orders);

        //修改订单表状态的留言时间
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(orderId);
        orderStatus.setCommentTime(new Date());
        oderStatusMapper.updateById(orderStatus);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public PageInfo<MyCommentVO> queryMyCommentVO(String userId, Integer pageNum, Integer pageSize) {

        PageHelper.startPage(pageNum, pageSize);
        List<MyCommentVO> myCommentVOS = commentsCustomizeMapper.selectByUserId(userId, null);
        PageInfo<MyCommentVO> myCommentVOPageInfo = new PageInfo<>(myCommentVOS);
        return myCommentVOPageInfo;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public OrderStatusCountsVO getMyOrderStatusCount(String userId) {
        int waitPayCounts = commentsCustomizeMapper.queryMyOrderStatusCounts(userId, OrderStatusEnum.WAIT_PAY.type, null);
        int waitDeliverCounts = commentsCustomizeMapper.queryMyOrderStatusCounts(userId, OrderStatusEnum.WAIT_DELIVER.type, null);
        int waitReceiveCounts = commentsCustomizeMapper.queryMyOrderStatusCounts(userId, OrderStatusEnum.WAIT_RECEIVE.type, null);
        int waitCommentCounts = commentsCustomizeMapper.queryMyOrderStatusCounts(userId, OrderStatusEnum.SUCCESS.type, YesOrNo.NO.type);

        OrderStatusCountsVO countsVO = OrderStatusCountsVO.builder()
                .waitCommentCounts(waitCommentCounts)
                .waitDeliverCounts(waitDeliverCounts)
                .waitPayCounts(waitPayCounts)
                .waitReceiveCounts(waitReceiveCounts).build();

        return countsVO;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public PageInfo<OrderStatus> getOrdersTrend(String userId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<OrderStatus> orderStatuses = commentsCustomizeMapper.queryMyOrderStatus(userId);
        PageInfo<OrderStatus> orderStatusPageInfo = new PageInfo<>(orderStatuses);
        return orderStatusPageInfo;
    }
}
