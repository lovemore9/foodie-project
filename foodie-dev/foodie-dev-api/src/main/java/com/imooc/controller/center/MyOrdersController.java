package com.imooc.controller.center;

import cn.hutool.core.util.ObjectUtil;
import com.imooc.pojo.Orders;
import com.imooc.service.OrderService;
import com.imooc.service.center.CenterUserService;
import com.imooc.service.center.MyCommentsService;
import com.imooc.service.center.MyOrdersService;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.vo.OrderStatusCountsVO;
import enums.OrderStatusEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 类简要说明
 *
 * @author wangyujin
 * @version 1.0
 * </pre>
 * Created on 2020-03-16 21:59
 * </pre>
 */
@Slf4j
@Api(value = "用户中心我的订单", tags = "用户中心用户中心我的订单相关接口")
@RestController
@RequestMapping("/myorders")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MyOrdersController {

    private final MyOrdersService myOrdersService;

    private final OrderService orderService;
    
    private final MyCommentsService myCommentsService;

    @ApiOperation(value = "查询订单列表", notes = "根据用户Id查询用户信息", httpMethod = "POST")
    @PostMapping("/query")
    public IMOOCJSONResult query(
            @ApiParam(name = "userId", value = "用户ID", required = true)
            @RequestParam String userId,
            @ApiParam(name = "orderStatus", value = "订单状态", required = false)
            @RequestParam(required = false) Integer orderStatus,
            @ApiParam(name = "page", value = "第几页", required = true)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "每页多少", required = true)
            @RequestParam Integer pageSize) {

        return IMOOCJSONResult.ok(myOrdersService.queryMyOrder(userId, orderStatus, page, pageSize));
    }

    @ApiOperation(value = "确认收货", notes = "确认收货", httpMethod = "POST")
    @PostMapping("/confirmReceive")
    public IMOOCJSONResult confirmReceive(
            @ApiParam(name = "userId", value = "用户ID", required = true)
            @RequestParam String userId,
            @ApiParam(name = "orderId", value = "订单ID", required = true)
            @RequestParam String orderId) {

        Orders orders = myOrdersService.queryMyOrder(orderId, userId);

        if (ObjectUtil.isNull(orders)) {
            return IMOOCJSONResult.errorMsg("订单权限不足，无法操作。");
        }
        orderService.updateOrderStatus(orderId, OrderStatusEnum.SUCCESS.type);
        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "删除订单", notes = "删除订单", httpMethod = "POST")
    @PostMapping("/delete")
    public IMOOCJSONResult delete(
            @ApiParam(name = "userId", value = "用户ID", required = true)
            @RequestParam String userId,
            @ApiParam(name = "orderId", value = "订单ID", required = true)
            @RequestParam String orderId) {

        Orders orders = myOrdersService.queryMyOrder(orderId, userId);

        if (ObjectUtil.isNull(orders)) {
            return IMOOCJSONResult.errorMsg("订单权限不足，无法操作。");
        }
        orderService.removeOrder(userId, orderId);
        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "查询订单状态数量", notes = "查询订单状态数量", httpMethod = "POST")
    @PostMapping("/statusCounts")
    public IMOOCJSONResult delete(
            @ApiParam(name = "userId", value = "用户ID", required = true)
            @RequestParam String userId) {

        return IMOOCJSONResult.ok(myCommentsService.getMyOrderStatusCount(userId));
    }

    @ApiOperation(value = "订单动向", notes = "订单动向", httpMethod = "POST")
    @PostMapping("/trend")
    public IMOOCJSONResult trend(
            @ApiParam(name = "userId", value = "用户ID", required = true)
            @RequestParam String userId,
            @ApiParam(name = "page", value = "第几页", required = true)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "每页多少", required = true)
            @RequestParam Integer pageSize) {

        return IMOOCJSONResult.ok(myCommentsService.getOrdersTrend(userId, page, pageSize));
    }
}
