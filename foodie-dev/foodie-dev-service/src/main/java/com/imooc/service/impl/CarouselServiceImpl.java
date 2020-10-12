package com.imooc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.imooc.mapper.CarouselMapper;
import com.imooc.pojo.Carousel;
import com.imooc.service.CarouselService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
public class CarouselServiceImpl implements CarouselService {

    private final CarouselMapper carouselMapper;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS) //查询用supperots
    public List<Carousel> queryAll(Integer isShow) {
        List<Carousel> carousels = carouselMapper.selectList(
                new QueryWrapper<Carousel>()
                        .eq("is_show", isShow)
                        .orderByDesc("sort"));
        return carousels;
    }


}
