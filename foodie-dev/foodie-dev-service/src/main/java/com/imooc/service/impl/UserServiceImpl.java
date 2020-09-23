package com.imooc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.imooc.bo.UserBO;
import com.imooc.mapper.UsersMapper;
import com.imooc.pojo.Users;
import com.imooc.service.UserService;
import com.imooc.utils.DateUtil;
import com.imooc.utils.MD5Utils;
import enums.Sex;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
public class UserServiceImpl implements UserService {

    private final UsersMapper usersMapper;

    private final Sid sid;

    private static final String USER_FACE = "http://122.152.205.72:88/group1/M00/00/05/CpoxxFw_8_qAIlFXAAAcIhVPdSg994.png";

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean queryUserNameIsExist(String userName) {
        QueryWrapper<Users> queryWrapperUser = new QueryWrapper<>();
        queryWrapperUser.lambda().eq(Users::getUsername, userName);
        Users users = usersMapper.selectOne(queryWrapperUser);
        if (users != null) {
            return true;
        }
        return false;
    }

    /**
     * @Description: 创建用户
     *
     * @author wangyujin
     * @Param: userBo:
     * @return: com.imooc.pojo.Users
     * @version 1.0
     * </pre>
     *  Created on :2020/2/25 21:06
     * </pre>
     */
    @Transactional(propagation = Propagation.REQUIRED) //创建用REQUIRED
    @Override
    public Users createUser(UserBO userBO) {
        Users user = new Users();
        user.setUsername(userBO.getUsername());
        try {
            user.setPassword(MD5Utils.getMD5Str(userBO.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //默认用户昵称同用户名
        user.setNickname(userBO.getUsername());
        user.setFace(USER_FACE);
        user.setBirthday(DateUtil.stringToDate("1900-01-01"));
        user.setSex(Sex.secret.type);
        user.setId(sid.nextShort());
        user.setCreatedTime(new Date());
        user.setUpdatedTime(new Date());
        usersMapper.insert(user);
        return user;
    }

    /**
     * @Description: 检索用户名与密码
     *
     * @author wangyujin
     * @Param: username:
     * @Param: password:
     * @return: com.imooc.pojo.Users
     * @version 1.0
     * </pre>
     *  Created on :2020/2/26 22:37
     * </pre>
     */
    @Transactional(propagation = Propagation.SUPPORTS) //查询用supperots
    @Override
    public Users queryUserForLogin(String username, String password) {
        QueryWrapper<Users> queryWrapperUser = new QueryWrapper<>();
        queryWrapperUser.lambda().eq(Users::getUsername, username).and(wq-> wq.eq(Users::getPassword,password));
        Users users = usersMapper.selectOne(queryWrapperUser);
        return users;
    }
}
