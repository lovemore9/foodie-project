package com.imooc.service;

import com.imooc.bo.UserAddressBO;
import com.imooc.pojo.UserAddress;

import java.util.List;

/**
 * 收货地址
 *
 * @author wangyujin
 * @version 1.0
 * </pre>
 * Created on 2020-02-24 20:43
 * </pre>
 */

public interface UserAddressService {

    /**
     * @Description: 根据当前登录用户获取其收货地址信息
     * @author wangyujin
     * @Param: userId:
     *  Created on :2020/3/12 22:56
     */
    List<UserAddress> queryAddressByUserId(String userId);

    /**
     * @Description 设置用户默认收货地址
     * @param userId
     * @param addressId
     * @author wangyujin
     *  Created on :2020/3/13 11:15
     */
    boolean setDefaultAddress(String userId, String addressId);

    /**
     * @Description 用户新增收货地址
     * @param userAddressBO
     * @author wangyujin
     *  Created on :2020/3/13 13:03
     */
    boolean addUserAddress(UserAddressBO userAddressBO);

    /**
     * @Description 删除用户地址
     * @param userId
     * @param addressId
     * @author wangyujin
     *  Created on :2020/3/13 14:11
     */
    boolean removeAddress(String userId, String addressId);

    /**
     * @Description 用户编辑维护收货地址
     * @param userAddressBO
     * @author wangyujin
     *  Created on :2020/3/13 13:03
     */
    boolean editAddress(UserAddressBO userAddressBO);
    
    /**
     * @Description: 根据传入的userId以及addressId获取用户地址信息
     * @author wangyujin
     * @Param: userId:
     * @Param: addressId:
     * @version 1.0
     *  Created on :2020/3/15 9:32
     */
    UserAddress queryUserAddress(String userId, String addressId);
}
