package com.imooc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.imooc.mapper.CategoryMapper;
import com.imooc.mapper.ustomize.CategoryCustomizeMapper;
import com.imooc.pojo.Category;
import com.imooc.service.CategoryService;
import com.imooc.vo.CategoryVO;
import com.imooc.vo.NewItemsVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类简要说明
 *
 * @author wangyujin
 * @version 1.0
 * </pre>
 * Created on 2020-02-24 22:40
 * </pre>
 */

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;

    private final CategoryCustomizeMapper categoryCustomizeMapper;


    @Override
    @Transactional(propagation = Propagation.SUPPORTS) //查询用supperots
    public List<Category> queryAllRootLevelCat() {
        List<Category> categories = categoryMapper.selectList(
                new QueryWrapper<Category>().
                eq("type", 1));
        return categories;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS) //查询用supperots
    public List<CategoryVO> getSubCatList(Integer rootCatId) {
        return categoryCustomizeMapper.getCatList(rootCatId);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS) //查询用supperots
    public List<NewItemsVO> getSixNewItemsLazy(Integer rootCatId) {
        Map<String, Object> params = new HashMap<>();
        params.put("rootCatId", rootCatId);
        return categoryCustomizeMapper.getSixNewItemsLazy(params);
    }
}
