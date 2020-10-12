package com.imooc.mapper.ustomize;

import cn.hutool.core.util.ObjectUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.Map;

/**
 * 类简要说明
 *
 * @author wangyujin
 * @version 1.0
 * </pre>
 * Created on 2020-03-17 21:26
 * </pre>
 */

public class OrdersSqlProvider {

    public String selectByCriteria(Map<String, Object> params) {
        Object userId = params.get("userId");
        Object status = params.get("status");

        SQL sql = new SQL();
        sql.SELECT(" od.id as order_id, od.is_comment, od.created_time, od.pay_method, od.real_pay_amount, od.post_amount, os.order_status");
        sql.FROM("orders od ");
        sql.LEFT_OUTER_JOIN("order_status os on od.id = os.order_id ");
        sql.WHERE(" od.user_id = #{userId}");
        sql.WHERE(" od.is_delete = 0");
        if (ObjectUtil.isNotNull(status)) {
            sql.WHERE(" os.order_status = #{status}");
        }
        sql.ORDER_BY("od.updated_time ASC");
        return sql.toString();
    }
}
