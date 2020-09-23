package com.imooc.service.center;

import com.github.pagehelper.PageInfo;
import com.imooc.bo.center.OrderItemsCommentBO;
import com.imooc.pojo.OrderItems;
import com.imooc.pojo.OrderStatus;
import com.imooc.pojo.Orders;
import com.imooc.vo.MyCommentVO;
import com.imooc.vo.MyOrdersVO;
import com.imooc.vo.OrderStatusCountsVO;

import java.util.List;

/**
 * 类简要说明
 *
 * @author wangyujin
 * @version 1.0
 * </pre>
 * Created on 2020-03-16 22:00
 * </pre>
 */

public interface MyCommentsService {

    /**
     * @Description: 查询订单项
     * @author wangyujin
     * @Param: orderId:
     *  Created on :2020/3/17 23:26
     */
    List<OrderItems> queryPendingComment(String orderId);

    /**
     * @Description: 评论商品
     * @author wangyujin
     * @Param: oderId:
     * @Param: userId:
     * @Param: orderItemsCommentBOList:
     *  Created on :2020/3/18 22:22
     */
    void saveComments(String orderId, String userId, List<OrderItemsCommentBO> orderItemsCommentBOList);

    /**
     * @Description: 查询我的评价查询
     * @author wangyujin
     * @Param: userId:
     * @Param: pageNum:
     * @Param: pageSize:
     *  Created on :2020/3/18 23:01
     */
    PageInfo<MyCommentVO> queryMyCommentVO(String userId, Integer pageNum, Integer pageSize);

    /**
     * @Description 查询订单数量
     * @param userId
     * @author wangyujin
     *  Created on :2020/3/19 9:35
     */
    OrderStatusCountsVO getMyOrderStatusCount(String userId);

    /**
     * @Description 分页的订单动向
     * @param userId
     * @param pageNum
     * @param pageSize
     * @author wangyujin
     *  Created on :2020/3/19 10:14
     */
    PageInfo<OrderStatus> getOrdersTrend(String userId, Integer pageNum, Integer pageSize);
}
