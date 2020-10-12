package com.imooc.controller;

import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageInfo;
import com.imooc.pojo.*;
import com.imooc.service.ItemService;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@Api(value = "商品接口", tags = "商品信息展示的相关接口")
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ItemsController extends BaseController{

    private final ItemService itemService;


    @ApiOperation(value = "查询商品详情", notes = "查询商品详情", httpMethod = "GET")
    @GetMapping("/info/{itemId}")
    public IMOOCJSONResult infoItemId(
            @ApiParam(name = "itemId", value = "商品Id", required = true)
            @PathVariable String itemId) {
        if (StringUtils.isBlank(itemId)) {
            return IMOOCJSONResult.errorMsg("商品Id不存在");
        }
        Items items = itemService.queryItemById(itemId);
        if (items == null) {
            return IMOOCJSONResult.errorMsg("商品Id不存在");
        }
        List<ItemsImg> itemsImgs = itemService.queryItemImgListById(itemId);
        List<ItemsSpec> itemsSpecs = itemService.queryItemSpecListById(itemId);
        ItemsParam itemsParam = itemService.queryItemParamListById(itemId);

        ItemInfoVO build = ItemInfoVO.builder()
                .item(items)
                .itemImgList(itemsImgs)
                .itemSpecList(itemsSpecs)
                .itemParams(itemsParam)
                .build();
        return IMOOCJSONResult.ok(build);
    }


    @ApiOperation(value = "获取商品评价数量", notes = "获取商品评价数量", httpMethod = "GET")
    @GetMapping("/commentLevel")
    public IMOOCJSONResult commentLevel(
            @ApiParam(name = "itemId", value = "商品Id", required = true)
            @RequestParam String itemId) {
        if (StringUtils.isBlank(itemId)) {
            return IMOOCJSONResult.errorMsg("商品Id不存在");
        }
        Items items = itemService.queryItemById(itemId);
        if (items == null) {
            return IMOOCJSONResult.errorMsg("商品Id不存在");
        }
        CommentsLevelCountsVO commentsLevelCountsVO = itemService.queryCommentCount(itemId);
        return IMOOCJSONResult.ok(commentsLevelCountsVO);
    }

    @ApiOperation(value = "获取商品评价详情", notes = "获取商品评价详情", httpMethod = "GET")
    @GetMapping("/comments")
    public IMOOCJSONResult comments(
            @ApiParam(name = "itemId", value = "商品Id", required = true)
            @RequestParam String itemId,
            @ApiParam(name = "level", value = "评价等级", required = false)
            @RequestParam(required = false) Integer level,
            @ApiParam(name = "page", value = "第几页", required = true)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "每页多少", required = true)
            @RequestParam Integer pageSize) {
        if (StringUtils.isBlank(itemId)) {
            return IMOOCJSONResult.errorMsg("商品Id不存在");
        }
        if (ObjectUtil.isNull(page) || ObjectUtil.isNull(pageSize)) {
            page = 1;
            pageSize = this.COMMENT_PAGE_SIZE;
        }
        Items items = itemService.queryItemById(itemId);
        if (ObjectUtil.isNull(items)) {
            return IMOOCJSONResult.errorMsg("商品Id不存在");
        }
        PageInfo<CommentsVO> itemsCommentsPageInfo = itemService.queryComments(itemId, level, page, pageSize);
        return IMOOCJSONResult.ok(itemsCommentsPageInfo);
    }

    @ApiOperation(value = "搜索关键词获取商品", notes = "搜索关键词获取商品", httpMethod = "GET")
    @GetMapping("/catItems")
    public IMOOCJSONResult searchCatItems(
            @ApiParam(name = "catId", value = "商品类被", required = true)
            @RequestParam String catId,
            @ApiParam(name = "sort", value = "排序规则", required = true)
            @RequestParam String sort,
            @ApiParam(name = "page", value = "第几页", required = true)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "每页多少", required = true)
            @RequestParam Integer pageSize) {
        if (StringUtils.isBlank(catId)) {
            return IMOOCJSONResult.errorMsg("类别不存在");
        }
        if (StringUtils.isBlank(sort)) {
            return IMOOCJSONResult.errorMsg("排序规则不存在");
        }
        if (ObjectUtil.isNull(page) || ObjectUtil.isNull(pageSize)) {
            page = 1;
            pageSize = this.COMMENT_PAGE_SIZE;
        }
        PageInfo<SearchItemVO> searchItemVOPageInfo = itemService.searchItemsByCatId(catId, sort, page, pageSize);
        return IMOOCJSONResult.ok(searchItemVOPageInfo);
    }

    @ApiOperation(value = "搜索关键词获取商品", notes = "搜索关键词获取商品", httpMethod = "GET")
    @GetMapping("/search")
    public IMOOCJSONResult searchItems(
            @ApiParam(name = "keywords", value = "关键词", required = true)
            @RequestParam String keywords,
            @ApiParam(name = "sort", value = "排序规则", required = true)
            @RequestParam String sort,
            @ApiParam(name = "page", value = "第几页", required = true)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "每页多少", required = true)
            @RequestParam Integer pageSize) {
        if (StringUtils.isBlank(keywords)) {
            return IMOOCJSONResult.errorMsg("关键词不存在");
        }
        if (StringUtils.isBlank(sort)) {
            return IMOOCJSONResult.errorMsg("排序规则不存在");
        }
        if (ObjectUtil.isNull(page) || ObjectUtil.isNull(pageSize)) {
            page = 1;
            pageSize = this.COMMENT_PAGE_SIZE;
        }
        PageInfo<SearchItemVO> searchItemVOPageInfo = itemService.searchItemsByKeyWords(keywords, sort, page, pageSize);
        return IMOOCJSONResult.ok(searchItemVOPageInfo);
    }


    @ApiOperation(value = "购物车界面刷新购物车相关信息", notes = "购物车界面刷新购物车相关信息", httpMethod = "GET")
    @GetMapping("/refresh")
    public IMOOCJSONResult refresh(
            @ApiParam(name = "itemSpecIds", value = "规格Id拼接后的字符串", required = true, example = "1001,1002,1003")
            @RequestParam String itemSpecIds) {
        if (ObjectUtil.isEmpty(itemSpecIds)) {
            return IMOOCJSONResult.errorMsg("规格Id不存在");

        }
        List<ShopCartVO> shopCartVOS = itemService.queryItemsBySpecIds(itemSpecIds);
        return IMOOCJSONResult.ok(shopCartVOS);
    }

}
