package com.imooc.service;

import com.imooc.bo.UserBO;
import com.imooc.pojo.Users;

/**
 * 类简要说明
 *
 * @author wangyujin
 * @version 1.0
 * </pre>
 * Created on 2020-02-24 20:43
 * </pre>
 */

public interface UserService {

    /**
     * @Description: 用户名是否存在
     *
     * @author wangyujin
     * @Param: userName:
     * @return: boolean
     * @version 1.0
     * </pre>
     *  Created on :2020/2/24 20:45
     * </pre>
     */
    boolean queryUserNameIsExist(String userName);

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
    Users createUser(UserBO userBo);

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
    Users queryUserForLogin(String username, String password);
}
