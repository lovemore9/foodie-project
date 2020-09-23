package com.imooc.mapper.ustomize;

import cn.hutool.core.util.ObjectUtil;
import com.imooc.utils.SqlUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
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
public class ItemsSqlProvider {

    public String selectByCriteria(Map<String, Object> params) {

        Object keywords = params.get("keywords");
        Object sort = params.get("sort");
        Object catId = params.get("catId");

        SQL sql = new SQL();
        sql.SELECT(" i.item_name, i.id AS item_id, i.sell_counts, ii.url AS img_url, s.price_discount AS price, i.cat_id ");
        sql.FROM("items i ");
        sql.LEFT_OUTER_JOIN("items_img ii ON i.id = ii.item_id ");
        sql.LEFT_OUTER_JOIN("(SELECT item_id, MIN(price_discount) AS price_discount FROM items_spec GROUP BY item_id) s ON i.id = s.item_id ");
        sql.WHERE("ii.is_main = 1");
        if (ObjectUtil.isNotNull(keywords)) {
            sql.WHERE("i.item_name like concat('%',#{keywords},'%')");
        }
        if (ObjectUtil.isNotEmpty(catId)) {
            sql.WHERE("i.cat_id = #{catId}");
        }
        if (ObjectUtil.isNotEmpty(sort)) {
            if (ObjectUtil.equal("k", sort)) {
                sql.ORDER_BY(" i.item_name ASC");
            }
            if (ObjectUtil.equal("c", sort)) {
                sql.ORDER_BY(" sell_counts DESC");
            }
            if (ObjectUtil.equal("p", sort)) {
                sql.ORDER_BY(" price ASC");
            }
        }
        return sql.toString();
    }

    public String selectBySpecIds(Map<String, Object> params) {
        List<String> specIds = (List<String>) params.get("specIds");
        SQL sql = new SQL();
        sql.SELECT("its.item_id, i.item_name, iti.url AS item_img_url, its.id AS spec_id, its.name AS spec_name, its.price_discount, its.price_normal");
        sql.FROM("items_spec its ");
        sql.LEFT_OUTER_JOIN(" items i ON its.item_id = i.id ");
        sql.LEFT_OUTER_JOIN(" items_img iti ON iti.item_id = its.item_id  ");
        sql.WHERE("iti.is_main = 1 AND its.id IN " + SqlUtils.getInSqlStr(specIds));
        return sql.toString();
    }
}
