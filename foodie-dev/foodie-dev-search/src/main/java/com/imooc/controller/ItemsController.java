package com.imooc.controller;


import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageInfo;
import com.imooc.es.pojo.Items;
import com.imooc.service.ItemESService;
import com.imooc.utils.IMOOCJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

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
@Api(value = "ES搜索", tags = "ES搜索的相关接口")
@RestController
@RequestMapping("items")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ItemsController {

    @Autowired
    private ItemESService itemESService;

    @ApiOperation(value = "测试", notes = "测试", httpMethod = "GET")
    @GetMapping("/hello")
    public String carousel() {
        return "hello ES!";
    }

    @ApiOperation(value = "搜索关键词获取商品", notes = "搜索关键词获取商品", httpMethod = "GET")
    @GetMapping("/es/search")
    public IMOOCJSONResult searchItems(
            @ApiParam(name = "keywords", value = "关键词", required = true)
            String keywords,
            @ApiParam(name = "sort", value = "排序规则", required = true)
            String sort,
            @ApiParam(name = "page", value = "第几页", required = true)
            Integer page,
            @ApiParam(name = "pageSize", value = "每页多少", required = true)
            Integer pageSize) {
        if (StringUtils.isBlank(keywords)) {
            return IMOOCJSONResult.errorMsg("关键词不存在");
        }
//        if (StringUtils.isBlank(sort)) {
//            return IMOOCJSONResult.errorMsg("排序规则不存在");
//        }
        if (ObjectUtil.isNull(page)) {
            page = 1;
        }
        if (ObjectUtil.isNull(pageSize)) {
            pageSize = 20;
        }

        page --;
        PageInfo<Items> itemsPageInfo = itemESService.searchItems(keywords, sort, page, pageSize);
        return IMOOCJSONResult.ok(itemsPageInfo);
    }


}
