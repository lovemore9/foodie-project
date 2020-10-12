package com.imooc.mapper.ustomize;

import com.imooc.vo.CommentsVO;
import com.imooc.vo.MyOrdersVO;
import com.imooc.vo.MySubOrderItemVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 类简要说明
 *
 * @author wangyujin
 * @version 1.0
 * </pre>
 * Created on 2020-03-17 21:22
 * </pre>
 */
public interface OrdersCustomizeMapper {

    /**
     * @Description 方法描述
     * @param userId
     * @param status
     * @author wangyujin
     *  Created on :2020/3/12 10:09
     */
    @SelectProvider(type= OrdersSqlProvider.class, method="selectByCriteria")
    @Results(value = {
            @Result(property = "orderId", column = "order_id"),
            @Result(property = "subOrderItemList", column = "order_id", one = @One(select = "com.imooc.mapper.ustomize.OrdersCustomizeMapper.selectByOrderItemByOrderId")),
    })
    List<MyOrdersVO> selectOrderByStatus(@Param("userId") String userId, @Param("status") Integer status);

    @Select("SELECT oi.item_id, oi.item_name, oi.item_img, oi.item_spec_id, oi.item_spec_name, oi.buy_counts, oi.price FROM order_items oi WHERE order_id = #{orderId}")
    List<MySubOrderItemVO> selectByOrderItemByOrderId(@Param("orderId") String OrderId);
}
