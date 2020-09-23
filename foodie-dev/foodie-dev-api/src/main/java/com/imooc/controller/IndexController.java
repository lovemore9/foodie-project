package com.imooc.controller;

import com.imooc.bo.UserBO;
import com.imooc.pojo.Carousel;
import com.imooc.pojo.Category;
import com.imooc.pojo.Users;
import com.imooc.service.CarouselService;
import com.imooc.service.CategoryService;
import com.imooc.service.UserService;
import com.imooc.utils.*;
import com.imooc.vo.CategoryVO;
import com.imooc.vo.NewItemsVO;
import enums.YesOrNo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 类简要说明
 *
 * @author wangyujin
 * @version 1.0
 * </pre>
 * Created on 2020-02-24 20:58
 * </pre>
 */
@Slf4j
@Api(value = "首页", tags = "首页展示的相关接口")
@RestController
@RequestMapping("/index")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IndexController {

    private final CarouselService carouselService;

    private final CategoryService categoryService;

    private final RedisOperator redisOperator;


    @ApiOperation(value = "获取首页轮播图片", notes = "获取首页轮播图片", httpMethod = "GET")
    @GetMapping("/carousel")
    public IMOOCJSONResult carousel() {
        //先去缓存中查找
        String str_carousel = redisOperator.get("carousel");
        List<Carousel> carousels = new ArrayList<>();

        if (StringUtils.isBlank(str_carousel)) {
            carousels = carouselService.queryAll(YesOrNo.YES.type);
            redisOperator.set("carousel", JsonUtils.objectToJson(carousels));
        } else {
            carousels = JsonUtils.jsonToList(str_carousel, Carousel.class);
        }
        //后台运营系统 1.广告位发生更改，删除缓存 2.每天定时重置缓存 3.每个广告可能都会有个过期时间

        return IMOOCJSONResult.ok(carousels);
    }

    @ApiOperation(value = "获取商品一级分类", notes = "获取商品一级分类", httpMethod = "GET")
    @GetMapping("/cats")
    public IMOOCJSONResult cats() {
        String str_cats = redisOperator.get("cats");
        List<Category> categories = new ArrayList<>();
        if (StringUtils.isBlank(str_cats)) {
            categories = categoryService.queryAllRootLevelCat();
            redisOperator.set("cats", JsonUtils.objectToJson(categories));
        } else {
            categories = JsonUtils.jsonToList(str_cats, Category.class);
        }
        return IMOOCJSONResult.ok(categories);
    }

    @ApiOperation(value = "获取商品子分类", notes = "获取商品子分类", httpMethod = "GET")
    @GetMapping("/subCat/{rootCatId}")
    public IMOOCJSONResult cats(
            @ApiParam(name = "rootCatId", value = "以及分类Id", required = true)
            @PathVariable Integer rootCatId) {
        if (rootCatId == null) {
            return IMOOCJSONResult.errorMsg("分类不存在");
        }
        List<CategoryVO> subCatList = new ArrayList<>();
        String str_subCat_rootCatId = redisOperator.get("subCat_rootCatId:" + rootCatId);
        if (StringUtils.isBlank(str_subCat_rootCatId)) {
            subCatList = categoryService.getSubCatList(rootCatId);
            redisOperator.set("subCat_rootCatId:" + rootCatId, JsonUtils.objectToJson(subCatList));
        } else {
            subCatList = JsonUtils.jsonToList(str_subCat_rootCatId, CategoryVO.class);
        }

        return IMOOCJSONResult.ok(subCatList);
    }

    @ApiOperation(value = "查询每个路由分类下的6个最新商品", notes = "查询每个路由分类下的6个最新商品", httpMethod = "GET")
    @GetMapping("/sixNewItems/{rootCatId}")
    public IMOOCJSONResult sixNewItems(
            @ApiParam(name = "rootCatId", value = "以及分类Id", required = true)
            @PathVariable Integer rootCatId) {
        if (rootCatId == null) {
            return IMOOCJSONResult.errorMsg("分类不存在");
        }
        List<NewItemsVO> sixNewItemsLazy = categoryService.getSixNewItemsLazy(rootCatId);
        return IMOOCJSONResult.ok(sixNewItemsLazy);
    }


}
