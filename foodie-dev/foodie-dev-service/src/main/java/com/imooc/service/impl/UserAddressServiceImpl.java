package com.imooc.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.imooc.bo.UserAddressBO;
import com.imooc.mapper.UserAddressMapper;
import com.imooc.pojo.UserAddress;
import com.imooc.service.UserAddressService;
import enums.YesOrNo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.n3r.idworker.Sid;
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
 * Created on 2020-02-24 20:44
 * </pre>
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserAddressServiceImpl implements UserAddressService {

    private final UserAddressMapper userAddressMapper;

    private final Sid sid;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS) //查询用supperots
    public List<UserAddress> queryAddressByUserId(String userId) {
        List<UserAddress> userAddressList = userAddressMapper.selectList(
                new QueryWrapper<UserAddress>()
                        .eq("user_id", userId));
        return userAddressList;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED) //创建用REQUIRED
    public boolean setDefaultAddress(String userId, String addressId) {
        UserAddress userAddress = new UserAddress();
        //当前用户所有地址is_default设置为0
        userAddress.setIsDefault(YesOrNo.NO.type);
        int up_AlldefaultToNo = userAddressMapper.update(
                userAddress,
                new UpdateWrapper<UserAddress>()
                        .eq("user_id", userId));
        userAddress.setIsDefault(YesOrNo.YES.type);
        //当前用户指定地址is_default设置为1
        int up_defaultToYes = userAddressMapper.update(
                //修改的某些属性
                userAddress,
                //修改的条件
                new UpdateWrapper<UserAddress>()
                        .eq("id", addressId)
                        .eq("user_id", userId));

        return up_defaultToYes > 0 ? true : false && up_AlldefaultToNo > 0 ? true : false;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED) //创建用REQUIRED
    public boolean addUserAddress(UserAddressBO userAddressBO) {
        UserAddress userAddress = new UserAddress();
        BeanUtils.copyProperties(userAddressBO, userAddress);

        //如果该用户之前没有地址
        if (userAddressMapper.selectCount(
                new QueryWrapper<UserAddress>()
                        .eq("user_id",userAddressBO.getUserId())) == 0) {
            userAddress.setIsDefault(YesOrNo.YES.type);
        } else {
            userAddress.setIsDefault(YesOrNo.NO.type);
        }

        userAddress.setId(sid.nextShort());
        Date now = new Date();
        userAddress.setCreatedTime(now);
        userAddress.setUpdatedTime(now);
        return userAddressMapper.insert(userAddress) > 0 ? true : false;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED) //创建用REQUIRED
    public boolean removeAddress(String userId, String addressId) {

        UserAddress userAddress = userAddressMapper.selectOne(
                new QueryWrapper<UserAddress>()
                        .eq("user_id", userId)
                        .eq("id", addressId));

        if (ObjectUtil.isNotNull(userAddress)) {
            int delete = userAddressMapper.delete(
                    new QueryWrapper<UserAddress>()
                            .eq("user_id", userId)
                            .eq("id", addressId));
            //如果这个地址是默认地址
            if (ObjectUtil.equal((int)userAddress.getIsDefault(), YesOrNo.YES.type)) {
                userAddress = userAddressMapper.selectOne(
                        new QueryWrapper<UserAddress>()
                                .eq("user_id", userId)
                                .last("LIMIT 1"));
                //说明还有其他地址
                if (ObjectUtil.isNotNull(userAddress)) {
                    userAddress.setIsDefault(YesOrNo.YES.type);
                    //修改其为默认地址
                    userAddressMapper.update(
                            userAddress,
                            new UpdateWrapper<UserAddress>()
                                .eq("id", userAddress.getId()));
                }
            }
            return true;
        }
        return false;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED) //创建用REQUIRED
    public boolean editAddress(UserAddressBO userAddressBO) {
        UserAddress userAddress = new UserAddress();
        BeanUtils.copyProperties(userAddressBO, userAddress);
        UserAddress oldAddress = userAddressMapper.selectById(userAddressBO.getAddressId());
        userAddress.setIsDefault(oldAddress.getIsDefault());
        userAddress.setUpdatedTime(new Date());
        int update = userAddressMapper.update(
                userAddress,
                new QueryWrapper<UserAddress>()
                        .eq("user_id", userAddressBO.getUserId())
                        .eq("id", userAddressBO.getAddressId())
        );
        return update > 0 ? true : false;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED) //创建用REQUIRED
    public UserAddress queryUserAddress(String userId, String addressId) {
        UserAddress userAddress = userAddressMapper.selectOne(
                new QueryWrapper<UserAddress>()
                        .eq("user_id", userId)
                        .eq("id", addressId));
        return userAddress;
    }

}
