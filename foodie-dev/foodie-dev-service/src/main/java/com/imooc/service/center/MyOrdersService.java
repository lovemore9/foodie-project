package com.imooc.service.center;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.imooc.bo.center.CenterUserBO;
import com.imooc.pojo.Orders;
import com.imooc.pojo.Users;
import com.imooc.vo.MyOrdersVO;

/**
 * 类简要说明
 *
 * @author wangyujin
 * @version 1.0
 * </pre>
 * Created on 2020-03-16 22:00
 * </pre>
 */

public interface MyOrdersService {

    /**
     * @Description: 查询我的订单列表
     * @author wangyujin
     * @Param: userId:
     * @Param: orderStatis:
     * @Param: pageNum:
     * @Param: pageSize:
     *  Created on :2020/3/17 21:38
     */
    PageInfo<MyOrdersVO> queryMyOrder(String userId, Integer orderStatus, Integer pageNum, Integer pageSize);

    /**
     * @Description: 查询我的订单
     * @author wangyujin
     * @Param: orderId:
     * @Param: userId:
     * @return: com.imooc.pojo.Orders
     *  Created on :2020/3/17 22:38
     */
    Orders queryMyOrder(String orderId, String userId);
}
