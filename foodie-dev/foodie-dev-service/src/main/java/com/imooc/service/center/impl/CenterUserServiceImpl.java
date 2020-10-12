package com.imooc.service.center.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.imooc.bo.center.CenterUserBO;
import com.imooc.mapper.UsersMapper;
import com.imooc.pojo.Users;
import com.imooc.service.center.CenterUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

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
public class CenterUserServiceImpl implements CenterUserService {

    private final UsersMapper usersMapper;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS) //查询用supperots
    public Users queryUserInfo(String userId) {
        Users users = usersMapper.selectById(userId);
        users.setPassword(null);
        return users;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Users updateUserInfo(String userId, CenterUserBO centerUserBO) {
        Users updateUser = new Users();
        BeanUtils.copyProperties(centerUserBO, updateUser);
        updateUser.setUpdatedTime(new Date());
        updateUser.setId(userId);

        usersMapper.updateById(updateUser);

        return this.queryUserInfo(userId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Users updateUserFace(String userId, String faceUrl) {
        Users updateUser = new Users();
        updateUser.setId(userId);
        updateUser.setFace(faceUrl);
        updateUser.setUpdatedTime(new Date());
        usersMapper.updateById(updateUser);
        return updateUser;
    }


}
