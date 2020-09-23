package com.imooc.service;

import com.imooc.pojo.Carousel;
import com.imooc.pojo.Category;
import com.imooc.vo.CategoryVO;
import com.imooc.vo.NewItemsVO;

import java.util.List;

/**
 * 轮播图service
 *
 * @author wangyujin
 * @version 1.0
 * </pre>
 * Created on 2020-02-24 22:39
 * </pre>
 */

public interface CategoryService {

    /**
     * @Description: 查询所有一级分类
     *
     * @author wangyujin
     * @Param: isShow:
     * @return: java.util.List<com.imooc.pojo.Carousel>
     * @version 1.0
     * </pre>
     *  Created on :2020/3/3 21:30
     * </pre>
     */
    List<Category> queryAllRootLevelCat();

    /**
     * @Description: 根据父分类查新子分类
     *
     * @author wangyujin
     * @Param: rootCatId:
     * @return: java.util.List<com.imooc.vo.CategoryVO>
     * @version 1.0
     * </pre>
     *  Created on :2020/3/4 20:55
     * </pre>
     */
    List<CategoryVO> getSubCatList(Integer rootCatId);

    /**
     * @Description: 获取最新6个商品信息
     *
     * @author wangyujin
     * @Param: rootCatId:
     * @return: java.util.List<com.imooc.vo.NewItemsVO>
     * @version 1.0
     * </pre>
     *  Created on :2020/3/4 22:47
     * </pre>
     */
    List<NewItemsVO> getSixNewItemsLazy(Integer rootCatId);
}
