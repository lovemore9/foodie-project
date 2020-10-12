package com.imooc.service.center;

import com.imooc.bo.center.CenterUserBO;
import com.imooc.pojo.Users;

/**
 * 类简要说明
 *
 * @author wangyujin
 * @version 1.0
 * </pre>
 * Created on 2020-03-16 22:00
 * </pre>
 */

public interface CenterUserService {

    /**
     * @Description: 查询用户信息
     * @author wangyujin
     * @Param: userId:
     *  Created on :2020/3/16 22:02
     */
    Users queryUserInfo(String userId);

    /**
     * @Description: 修改用户信息
     * @author wangyujin
     * @Param: userId:
     * @Param: centerUserBO:
     *  Created on :2020/3/16 22:15
     */
    Users updateUserInfo(String userId, CenterUserBO centerUserBO);

    /**
     * @Description: 修改用户头像
     * @author wangyujin
     * @Param: userId:
     * @Param: faceUrl:
     * </pre>
     */
    Users updateUserFace(String userId, String faceUrl);
}
