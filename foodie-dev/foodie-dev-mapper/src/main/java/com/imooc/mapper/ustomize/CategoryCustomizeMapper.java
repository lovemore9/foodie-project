package com.imooc.mapper.ustomize;

import com.imooc.vo.CategoryVO;
import com.imooc.vo.NewItemsVO;
import com.imooc.vo.SimpleItemVO;
import com.imooc.vo.SubCategoryVO;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * 类简要说明
 *
 * @author wangyujin
 * @version 1.0
 * </pre>
 * Created on 2020-03-03 22:33
 * </pre>
 */

public interface CategoryCustomizeMapper {

    @Select(
            {"SELECT",
                "f.id as id,",
                "f.name as name,",
                "f.type as type,",
                "f.father_id as fatherId",
             "FROM",
                "category f",
             "WHERE",
                "f.father_id = #{rootCatId}"})
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "type", column = "type"),
            @Result(property = "fatherId", column = "fatherId"),
            @Result(property = "subCatList", column = "id", one = @One(select = "com.imooc.mapper.ustomize.CategoryCustomizeMapper.getSubCatList")),
    })
    List<CategoryVO> getCatList(int rootCatId);


    @Select("SELECT * FROM category WHERE father_id = #{subFatherId}")
    @Results(value = {
            @Result(property = "subId", column = "id"),
            @Result(property = "subName", column = "name"),
            @Result(property = "subType", column = "type"),
            @Result(property = "subFatherId", column = "father_id"),
    })
    List<SubCategoryVO> getSubCatList(int subFatherId);

    @Select("SELECT f.id as rootCatId, f.name as rootCatName, f.slogan as slogan, f.cat_image as catImage, f.bg_color as bgColor FROM category f  WHERE f.type = 1 AND f.id = #{params.rootCatId}")
    @Results(
            @Result(property = "simpleItemList", column = "rootCatId", one = @One(select = "com.imooc.mapper.ustomize.CategoryCustomizeMapper.getSimpleItemVO"))
    )
    List<NewItemsVO> getSixNewItemsLazy(@Param("params") Map<String, Object> params);

    @Select("SELECT i.id as itemId, i.item_name as itemName, ii.url as itemUrl, i.created_time as createdTime FROM items i LEFT JOIN items_img ii ON i.id = ii.item_id WHERE i.root_cat_id = #{rootCatId} AND ii.is_main = 1 ORDER BY i.created_time DESC LIMIT 6")
    List<SimpleItemVO> getSimpleItemVO(Integer rootCatId);

}
