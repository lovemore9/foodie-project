package com.imooc.controller;

import cn.hutool.core.util.ObjectUtil;
import com.imooc.bo.ShopCartBO;
import com.imooc.bo.UserAddressBO;
import com.imooc.pojo.OrderStatus;
import com.imooc.pojo.Orders;
import com.imooc.service.OrderService;
import com.imooc.service.UserAddressService;
import com.imooc.utils.CookieUtils;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.RedisOperator;
import com.imooc.vo.MerchantOrdersVO;
import com.imooc.vo.OrderVO;
import com.imooc.vo.SubmitOrderVO;
import enums.OrderStatusEnum;
import enums.PayMethod;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 购物车相关
 *
 * @author wangyujin
 * @version 1.0
 * </pre>
 * Created on 2020-02-24 20:58
 * </pre>
 */
@Slf4j
@Api(value = "订单相关", tags = "订单相关接口相关操作")
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrdersController extends BaseController{

    private final OrderService orderService;

    private final RestTemplate restTemplate;

    private final RedisOperator redisOperator;


    @ApiOperation(value = "用户提交订单创建相关订单信息", notes = "用户提交订单创建相关订单信息", httpMethod = "GET")
    @PostMapping("/create")
    public IMOOCJSONResult create(
            @ApiParam(name = "submitOrderVO", value = "用户提交订单VO", required = true)
            @RequestBody SubmitOrderVO submitOrderVO,
            HttpServletRequest request,
            HttpServletResponse response) {

        if (ObjectUtil.isEmpty(submitOrderVO) || ObjectUtil.isEmpty(submitOrderVO.getUserId())) {
            return IMOOCJSONResult.errorMsg("参数不能为null");
        }

        if (ObjectUtil.equal(submitOrderVO.getPayMethod(), PayMethod.WEIXIN.type)
                && ObjectUtil.equal(submitOrderVO.getPayMethod(), PayMethod.ALIPAY.type)) {
            return IMOOCJSONResult.errorMsg("支付方式不支持！");
        }

        log.info("submitOrderVO对象信息：{}", submitOrderVO);

        String str_shopCart = redisOperator.get("shopcart" + ":" + submitOrderVO.getUserId());
        List<ShopCartBO> shopCartList = null;
        if (StringUtils.isBlank(str_shopCart)) {
            return IMOOCJSONResult.errorMsg("购物车数据不正确！");
        }

        shopCartList = JsonUtils.jsonToList(str_shopCart, ShopCartBO.class);

        //1.创建订单
        OrderVO order = orderService.createOrder(submitOrderVO, shopCartList);

        //2.创建订单后移除购物车中已经结算的商品

        //整合redis后完善购物车中的商品清除
        List<ShopCartBO> toBeRemoveList = order.getToBeRemoveList();
        shopCartList.removeAll(toBeRemoveList);

        //覆盖现有的购物车数据
        redisOperator.set(SHOPCART + ":" + submitOrderVO.getUserId(), JsonUtils.objectToJson(shopCartList));

        //清空前端购物车
        CookieUtils.setCookie(request, response, FOODIE_SHOPCART, JsonUtils.objectToJson(shopCartList), true);

        //3.向支付中心发送当前订单用于保存支付中心的订单数据
        MerchantOrdersVO merchantOrdersVO = order.getMerchantOrdersVO();
        merchantOrdersVO.setReturnUrl(PAY_RETURN_URL);
        merchantOrdersVO.setAmount(1);

        HttpHeaders headers = new HttpHeaders();
        headers.setConnection(MediaType.APPLICATION_JSON_UTF8_VALUE);
        headers.add("imoocUserId", "imooc");
        headers.add("password", "imooc");

        HttpEntity<MerchantOrdersVO> entity = new HttpEntity<>(merchantOrdersVO, headers);
        ResponseEntity<IMOOCJSONResult> imoocjsonResultResponseEntity = restTemplate.postForEntity(PAY_MENT_URL, entity, IMOOCJSONResult.class);
        if (ObjectUtil.equal(imoocjsonResultResponseEntity.getStatusCode(), HttpStatus.OK.value())) {
            log.error("支付中心创建订单失败");
        }
        IMOOCJSONResult result = imoocjsonResultResponseEntity.getBody();
        log.info("支付中心返回值：{}", result);


        return IMOOCJSONResult.ok(order.getOrderId());
    }

    @ApiOperation(value = "查询支付结果", notes = "查询支付结果", httpMethod = "GET")
    @PostMapping("/getPaidOrderInfo")
    public IMOOCJSONResult getPaidOrderInfo(
            @ApiParam(name = "订单Id", value = "订单Id", required = true)
            @RequestParam String orderId) {

        return IMOOCJSONResult.ok(orderService.queryOrdersIsPayByOrderId(orderId));
    }

    @ApiOperation(value = "用于支付中心回调本地订单修改订单状态", notes = "用于支付中心回调本地订单修改订单状态", httpMethod = "GET")
    @PostMapping("/notifyMerchantOrderPaid")
    public HttpStatus notifyMerchantOrderPaid(
            @ApiParam(name = "订单Id", value = "订单Id", required = true)
            @RequestParam String merchantOrderId) {
        orderService.updateOrderStatus(merchantOrderId, OrderStatusEnum.WAIT_DELIVER.type);
        return HttpStatus.OK;
    }

    @ApiOperation(value = "用户商家发货后改变订单状态", notes = "用户商家发货后改变订单状态", httpMethod = "GET")
    @PostMapping("/updateDeliverOrderStatus")
    public HttpStatus updateDeliverOrderStatus (
            @ApiParam(name = "订单Id", value = "订单Id", required = true)
            @RequestParam String merchantOrderId) {
        orderService.updateOrderStatus(merchantOrderId, OrderStatusEnum.WAIT_RECEIVE.type);
        return HttpStatus.OK;
    }

}
