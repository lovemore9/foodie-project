package com.imooc.mapper.ustomize;

import cn.hutool.core.util.ObjectUtil;
import org.apache.ibatis.jdbc.SQL;

import java.util.Map;

/**
 * 类简要说明
 *
 * @author wangyujin
 * @version 1.0
 * </pre>
 * Created on 2020-03-11 18:49
 * </pre>
 */
public class CommentsSqlProvider {

    public String selectByCriteria(Map<String, Object> params) {
        Object itemId = params.get("itemId");
        Object level = params.get("level");

        SQL sql = new SQL();
        sql.SELECT(" u.nickname, ic.created_time, ic.content, ic.sepc_name, u.face AS user_face  ");
        sql.FROM("items_comments ic ");
        sql.LEFT_OUTER_JOIN("users u ON ic.user_id = u.id");
        if (ObjectUtil.isNotNull(itemId)) {
            sql.WHERE("item_id=#{itemId}");
        }
        if (ObjectUtil.isNotEmpty(level)) {
            sql.WHERE("comment_level=#{level}");
        }
        return sql.toString();
    }

    public String selectByUserId(Map<String, Object> params) {
        Object itemId = params.get("itemId");
        Object userId = params.get("userId");

        SQL sql = new SQL();
        sql.SELECT(" ic.id AS comment_id, ic.content, ic.created_time, ic.item_id, ic.sepc_name, ii.url AS item_img, ic.item_name  ");
        sql.FROM("items_comments ic ");
        sql.LEFT_OUTER_JOIN("items_img ii ON ic.item_id = ii.item_id");
        if (ObjectUtil.isNotNull(userId)) {
            sql.WHERE("ic.user_id=#{userId}");
        }
        if (ObjectUtil.isNotEmpty(itemId)) {
            sql.WHERE("ic.item_id=#{itemId}");
        }
        sql.WHERE("ii.is_main = 1");
        sql.ORDER_BY("created_time desc");
        return sql.toString();
    }

    public String selectMyOrderStatusCounts(Map<String, Object> params) {
        Object userId = params.get("userId");
        Object orderStatus = params.get("orderStatus");
        Object isComment = params.get("isComment");

        SQL sql = new SQL();
        sql.SELECT(" count(1) ");
        sql.FROM("orders o ");
        sql.LEFT_OUTER_JOIN("order_status os ON o.id = os.order_id ");
        if (ObjectUtil.isNotNull(userId)) {
            sql.WHERE("user_id=#{userId}");
        }
        if (ObjectUtil.isNotEmpty(isComment)) {
            sql.WHERE("o.is_comment =#{isComment}");
        }
        if (ObjectUtil.isNotEmpty(orderStatus)) {
            sql.WHERE("os.order_status=#{orderStatus}");
        }

        return sql.toString();
    }

    public String selectMyOrderStatus(Map<String, Object> params) {
        Object userId = params.get("userId");

        SQL sql = new SQL();
        sql.SELECT(" os.order_id, os.order_status, os.created_time, os.pay_time, os.deliver_time, os.success_time, os.close_time, os.comment_time  ");
        sql.FROM("orders o  ");
        sql.LEFT_OUTER_JOIN("order_status os ON o.id = os.order_id ");
        if (ObjectUtil.isNotNull(userId)) {
            sql.WHERE("user_id=#{userId}");
        }
        sql.WHERE("o.is_delete = 0");
        sql.WHERE("os.order_status IN (20,30,40)");
        sql.ORDER_BY("os.order_id DESC");
        return sql.toString();
    }
}
