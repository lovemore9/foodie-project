package com.imooc.controller;

import cn.hutool.core.util.ObjectUtil;
import com.imooc.bo.ShopCartBO;
import com.imooc.pojo.Carousel;
import com.imooc.pojo.Category;
import com.imooc.service.CarouselService;
import com.imooc.service.CategoryService;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.RedisOperator;
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
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spring.web.json.Json;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 购物车相关
 *
 * @author wangyujin
 * @version 1.0
 * </pre>
 * Created on 2020-02-24 20:58
 * </pre>
 */
@Slf4j
@Api(value = "购物车接口", tags = "购物车接口相关操作")
@RestController
@RequestMapping("/shopcart")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShopCartController extends BaseController{

    private final RedisOperator redisOperator;

    @ApiOperation(value = "同步购物车到后端", notes = "同步购物车到后端", httpMethod = "POST")
    @PostMapping("/add")
    public IMOOCJSONResult add(
            @ApiParam(name = "userId", value = "用户ID", required = true)
            @RequestParam String userId,
            @ApiParam(name = "shopCartBO", value = "购物车前端传入接收对象", required = true)
            @RequestBody ShopCartBO shopCartBO,
            HttpServletRequest request,
            HttpServletResponse response) {

        log.info("购物车相关内容：{}", shopCartBO.toString());

        //TODO 此处简单判断，后续使用分布式进行处理
        if (ObjectUtil.isEmpty(userId)) {
            return IMOOCJSONResult.errorMsg("用户Id不能为空");
        }

        List<ShopCartBO> shopCartList = null;

        //前端用户在登录情况下添加商品进购物车，会在后端同步进redis
        String str_shopCart = redisOperator.get(SHOPCART + ":" + userId);
        if (StringUtils.isNotBlank(str_shopCart)) {
            shopCartList = JsonUtils.jsonToList(str_shopCart, ShopCartBO.class);
            //添加已经存在的商品，进行累加
            boolean isHaving = false;
            for (ShopCartBO sc : shopCartList) {
                String currentSpecId = sc.getSpecId();
                if (ObjectUtil.equal(currentSpecId, shopCartBO.getSpecId())) {
                    sc.setBuyCounts(sc.getBuyCounts() + shopCartBO.getBuyCounts());
                    isHaving = true;
                }
            }
            if (!isHaving) {
                shopCartList.add(shopCartBO);
            }
        } else {
            shopCartList = new ArrayList<>();
            shopCartList.add(shopCartBO);
        }
        //覆盖现有的购物车数据
        redisOperator.set(SHOPCART + ":" + userId, JsonUtils.objectToJson(shopCartList));

        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "删除购物车内物品", notes = "删除购物车内物品", httpMethod = "POST")
    @PostMapping("/del")
    public IMOOCJSONResult del(
            @ApiParam(name = "userId", value = "用户ID", required = true)
            @RequestParam String userId,
            @ApiParam(name = "itemSpecId", value = "购物车内被删除的商品Id", required = true)
            @RequestParam String itemSpecId,
            HttpServletRequest request,
            HttpServletResponse response) {

        //TODO 此处简单判断，后续使用分布式进行处理
        if (ObjectUtil.isEmpty(userId) || ObjectUtil.isEmpty(itemSpecId)) {
            return IMOOCJSONResult.errorMsg("参数不能为空");
        }
        //前端用户在登录情况下删除商品，会在后端同步进redis
        List<ShopCartBO> shopCartList = null;
        String str_shopCart = redisOperator.get(SHOPCART + ":" + userId);
        if (StringUtils.isNotBlank(str_shopCart)) {
            shopCartList = JsonUtils.jsonToList(str_shopCart, ShopCartBO.class);
            for (ShopCartBO sc : shopCartList) {
                String currentSpecId = sc.getSpecId();
                if (ObjectUtil.equal(currentSpecId, itemSpecId)) {
                    shopCartList.remove(sc);
                    break;
                }
            }
        }
        //覆盖现有的购物车数据
        redisOperator.set(SHOPCART + ":" + userId, JsonUtils.objectToJson(shopCartList));
        return IMOOCJSONResult.ok();
    }

}
