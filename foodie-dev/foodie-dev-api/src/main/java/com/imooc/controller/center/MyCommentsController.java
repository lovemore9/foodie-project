package com.imooc.controller.center;

import cn.hutool.core.util.ObjectUtil;
import com.imooc.bo.center.OrderItemsCommentBO;
import com.imooc.pojo.Orders;
import com.imooc.service.OrderService;
import com.imooc.service.center.MyCommentsService;
import com.imooc.service.center.MyOrdersService;
import com.imooc.utils.IMOOCJSONResult;
import enums.OrderStatusEnum;
import enums.YesOrNo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
@Api(value = "用户中心评价", tags = "用户中心评价我的订单相关接口")
@RestController
@RequestMapping("/mycomments")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MyCommentsController {

    private final MyCommentsService myCommentsService;

    private final MyOrdersService myOrdersService;

    @ApiOperation(value = "获取我的评价信息", notes = "获取我的评价信息", httpMethod = "POST")
    @PostMapping("/pending")
    public IMOOCJSONResult pending(
            @ApiParam(name = "userId", value = "用户ID", required = true)
            @RequestParam String userId,
            @ApiParam(name = "orderId", value = "orderId", required = true)
            @RequestParam String orderId) {

        Orders orders = myOrdersService.queryMyOrder(orderId, userId);

        if (ObjectUtil.isNull(orders)) {
            return IMOOCJSONResult.errorMsg("订单权限不足，无法操作。");
        }
        if (ObjectUtil.equal(orders.getIsComment(), (long)YesOrNo.YES.type)) {
            return IMOOCJSONResult.errorMsg("订单已经评价，请勿重复操作。");
        }

        return IMOOCJSONResult.ok(myCommentsService.queryPendingComment(orderId));
    }

    @ApiOperation(value = "对订单进行评论", notes = "对订单进行评论", httpMethod = "POST")
    @PostMapping("/saveList")
    public IMOOCJSONResult saveList(
            @ApiParam(name = "userId", value = "用户ID", required = true)
            @RequestParam String userId,
            @ApiParam(name = "orderId", value = "orderId", required = true)
            @RequestParam String orderId,
            @ApiParam(name = "commentList", value = "订单项集合", required = true)
            @RequestBody List<OrderItemsCommentBO> commentList) {

        Orders orders = myOrdersService.queryMyOrder(orderId, userId);

        if (ObjectUtil.isNull(orders)) {
            return IMOOCJSONResult.errorMsg("订单权限不足，无法操作。");
        }

        if (ObjectUtil.isNull(commentList) || commentList.isEmpty()) {
            return IMOOCJSONResult.errorMsg("评论不能为空。");
        }

        myCommentsService.saveComments(orderId, userId, commentList);

        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "查询评论列表", notes = "查询评论列表", httpMethod = "POST")
    @PostMapping("/query")
    public IMOOCJSONResult query(
            @ApiParam(name = "userId", value = "用户ID", required = true)
            @RequestParam String userId,
            @ApiParam(name = "page", value = "第几页", required = true)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "每页多少", required = true)
            @RequestParam Integer pageSize) {

        return IMOOCJSONResult.ok(myCommentsService.queryMyCommentVO(userId, page, pageSize));
    }

}
