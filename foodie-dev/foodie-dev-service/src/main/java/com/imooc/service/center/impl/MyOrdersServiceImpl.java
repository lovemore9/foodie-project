package com.imooc.service.center.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.bo.center.CenterUserBO;
import com.imooc.mapper.OrdersMapper;
import com.imooc.mapper.UsersMapper;
import com.imooc.mapper.ustomize.OrdersCustomizeMapper;
import com.imooc.pojo.Orders;
import com.imooc.pojo.Users;
import com.imooc.service.center.CenterUserService;
import com.imooc.service.center.MyOrdersService;
import com.imooc.vo.MyOrdersVO;
import com.imooc.vo.SearchItemVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 类简要说明
 *
 * @author wangyujin
 * @version 1.0
 * </pre>
 * Created on 2020-03-16 22:01
 * </pre>
 */
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class MyOrdersServiceImpl implements MyOrdersService {

    private final OrdersCustomizeMapper ordersCustomizeMapper;

    private final OrdersMapper ordersMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public PageInfo<MyOrdersVO> queryMyOrder(String userId, Integer orderStatus, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<MyOrdersVO> myOrdersVOS = ordersCustomizeMapper.selectOrderByStatus(userId, orderStatus);
        PageInfo<MyOrdersVO> myOrdersVOPageInfo = new PageInfo<>(myOrdersVOS);
        return myOrdersVOPageInfo;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Orders queryMyOrder(String orderId, String userId) {
        Orders orders = ordersMapper.selectOne(
                new QueryWrapper<Orders>()
                        .eq("user_id", userId)
                        .eq("id", orderId));
        return orders;
    }
}
