package com.imooc.service;

import com.imooc.pojo.Carousel;

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

public interface CarouselService {

    /**
     * @Description: 查询所有轮播图图别表
     *
     * @author wangyujin
     * @Param: isShow:
     * @return: java.util.List<com.imooc.pojo.Carousel>
     * @version 1.0
     * </pre>
     *  Created on :2020/3/3 21:30
     * </pre>
     */
    List<Carousel> queryAll(Integer isShow);


}
