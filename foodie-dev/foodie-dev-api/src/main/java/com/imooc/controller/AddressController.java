package com.imooc.controller;

import cn.hutool.core.util.ObjectUtil;
import com.imooc.bo.UserAddressBO;
import com.imooc.service.UserAddressService;
import com.imooc.utils.IMOOCJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 购物车相关
 *
 * @author wangyujin
 * @version 1.0
 * </pre>
 * Created on 2020-02-24 20:58
 * </pre>
 */
@Slf4j
@Api(value = "收货地址", tags = "收货地址接口相关操作")
@RestController
@RequestMapping("/address")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AddressController {

    private final UserAddressService userAddressService;

    @ApiOperation(value = "获取当前用户的收货地址列表", notes = "获取当前用户的收货地址列表", httpMethod = "GET")
    @PostMapping("/list")
    public IMOOCJSONResult add(
            @ApiParam(name = "userId", value = "用户ID", required = true)
            @RequestParam String userId) {

        if (ObjectUtil.isEmpty(userId)) {
            return IMOOCJSONResult.errorMsg("用户ID不能为null");
        }

        return IMOOCJSONResult.ok(userAddressService.queryAddressByUserId(userId));
    }

    @ApiOperation(value = "修改用户默认地址", notes = "修改用户默认地址", httpMethod = "GET")
    @PostMapping("/setDefalut")
    public IMOOCJSONResult setDefalut(
            @ApiParam(name = "userId", value = "用户ID", required = true)
            @RequestParam String userId,
            @ApiParam(name = "addressId", value = "地址Id", required = true)
            @RequestParam String addressId) {

        if (ObjectUtil.isEmpty(userId) || ObjectUtil.isEmpty(addressId)) {
            return IMOOCJSONResult.errorMsg("参数不能为空");
        }

        return IMOOCJSONResult.ok(userAddressService.setDefaultAddress(userId, addressId));
    }

    @ApiOperation(value = "增加用户收货地址", notes = "增加用户收货地址", httpMethod = "POST")
    @PostMapping("/add")
    public IMOOCJSONResult add(
            @ApiParam(name = "新增用户地址BO", value = "userAddressBO", required = true)
            @RequestBody UserAddressBO userAddressBO) {
        if (ObjectUtil.isEmpty(userAddressBO)) {
            return IMOOCJSONResult.errorMsg("参数不能为空");
        }
        if (ObjectUtil.isEmpty(userAddressBO.getUserId())) {
            return IMOOCJSONResult.errorMsg("参数不能为空");
        }

        return IMOOCJSONResult.ok(userAddressService.addUserAddress(userAddressBO));
    }

    @ApiOperation(value = "删除用户地址", notes = "删除用户地址", httpMethod = "POST")
    @PostMapping("/delete")
    public IMOOCJSONResult delete(
            @ApiParam(name = "userId", value = "用户ID", required = true)
            @RequestParam String userId,
            @ApiParam(name = "addressId", value = "地址Id", required = true)
            @RequestParam String addressId) {
        if (ObjectUtil.isEmpty(userId) || ObjectUtil.isEmpty(addressId)) {
            return IMOOCJSONResult.errorMsg("参数不能为空");
        }

        return IMOOCJSONResult.ok(userAddressService.removeAddress(userId, addressId));
    }

    @ApiOperation(value = "修改收件地址信息", notes = "修改收件地址信息", httpMethod = "POST")
    @PostMapping("/update")
    public IMOOCJSONResult update(
            @ApiParam(name = "修改收件地址BO", value = "修改收件地址BO", required = true)
            @RequestBody UserAddressBO userAddressBO) {
        if (ObjectUtil.isEmpty(userAddressBO)) {
            return IMOOCJSONResult.errorMsg("参数不能为空");
        }
        if (ObjectUtil.isEmpty(userAddressBO.getUserId()) || ObjectUtil.isEmpty(userAddressBO.getAddressId())) {
            return IMOOCJSONResult.errorMsg("参数不能为空");
        }

        return IMOOCJSONResult.ok(userAddressService.editAddress(userAddressBO));
    }

}
