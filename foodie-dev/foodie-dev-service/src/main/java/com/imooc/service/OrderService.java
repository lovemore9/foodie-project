package com.imooc.service;

import com.imooc.bo.ShopCartBO;
import com.imooc.pojo.Carousel;
import com.imooc.pojo.OrderStatus;
import com.imooc.pojo.Orders;
import com.imooc.vo.OrderVO;
import com.imooc.vo.SubmitOrderVO;

import java.util.List;

/**
 * 订单Service
 *
 * @author wangyujin
 * @version 1.0
 * </pre>
 * Created on 2020-02-24 22:39
 * </pre>
 */

public interface OrderService {

    /**
     * @Description: 创建订单相关信息
     * @author wangyujin
     * @Param: submitOrderVO:
     *  Created on :2020/3/15 9:21
     */
    OrderVO createOrder(SubmitOrderVO submitOrderVO, List<ShopCartBO> shopCartList);

    /**
     * @Description: 修改订单状态
     * @author wangyujin
     * @Param: orderId:
     * @Param: orderStatus:
     *  Created on :2020/3/15 22:04
     */
    int updateOrderStatus(String orderId, Integer orderStatus);

    /**
     * @Description: 查询订单状态是否为已经支付
     * @author wangyujin
     * @Param: orderId:
     *  Created on :2020/3/15 23:20
     */
    OrderStatus queryOrdersIsPayByOrderId(String orderId);

    /**
     * @Description: 关闭超时未支付订单
     * @author wangyujin
     *  Created on :2020/3/16 21:16
     */
    void clouseOrder();

    /**
     * @Description: 删除订单
     * @author wangyujin
     * @Param: orderId:
     * @Param: orderId1:
     *  Created on :2020/3/17 23:06
     */
    void removeOrder(String userId, String orderId);
}
