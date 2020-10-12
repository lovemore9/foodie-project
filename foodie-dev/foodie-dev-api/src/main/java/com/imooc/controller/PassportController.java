package com.imooc.controller;

import cn.hutool.core.util.ObjectUtil;
import com.imooc.bo.ShopCartBO;
import com.imooc.bo.UserBO;
import com.imooc.pojo.Carousel;
import com.imooc.pojo.Users;
import com.imooc.service.UserService;
import com.imooc.utils.*;
import com.imooc.vo.CategoryVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * 类简要说明
 *
 * @author wangyujin
 * @version 1.0
 * </pre>
 * Created on 2020-02-24 20:58
 * </pre>
 */
@Api(value = "注册登录", tags = "用于注册登录相关接口")
@RestController
//@ApiIgnore //忽略当前文档进入swagger2
@RequestMapping("/passport")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PassportController extends BaseController{

    private final UserService userService;

    private final RedisOperator redisOperator;

    @ApiOperation(value = "用户名是否存在", notes = "用户名是否存在", httpMethod = "GET")
    @GetMapping("/usernameIsExist")
    public IMOOCJSONResult usernameIsExist(@RequestParam("username") String username) {
        if (StringUtils.isBlank(username)) {
            return IMOOCJSONResult.errorMsg("username是空的");
        }
        //用户名不可用
        if (this.userService.queryUserNameIsExist(username)) {
            return IMOOCJSONResult.errorMsg("username已经存在");

        }
        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "注册用户", notes = "注册用户", httpMethod = "POST")
    @PostMapping("/regist")
    public IMOOCJSONResult regist(@RequestBody @Validated UserBO userBO,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {
        if (this.userService.queryUserNameIsExist(userBO.getUsername())) {
            return IMOOCJSONResult.errorMsg("username已经存在");
        }
        if (userBO.getPassword().length()<6) {
            return IMOOCJSONResult.errorMsg("密码长度不能少于6");
        }
        if (!userBO.getPassword().equals(userBO.getConfirmPassword())) {
            return IMOOCJSONResult.errorMsg("密码不一致");
        }
        Users user = userService.createUser(userBO);

        user = this.setNull(user);

        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(user), true);

        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "用户登录", notes = "用户登录", httpMethod = "POST")
    @PostMapping("/login")
    public IMOOCJSONResult login(@RequestBody @Validated UserBO userBO,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
        Users user = null;
        try {
            user = userService.queryUserForLogin(userBO.getUsername(), MD5Utils.getMD5Str(userBO.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (user == null) {
            return IMOOCJSONResult.errorMsg("用户或密码不正确");
        }

        //TODO 分布式会话，将生成的token存入redis
        //TODO 同步购物车数据
        synchShopcartData(user.getId(), request ,response);

        user = this.setNull(user);

        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(user), true);

        return IMOOCJSONResult.ok(user);
    }

    @ApiOperation(value = "用户退出", notes = "用户退出", httpMethod = "POST")
    @PostMapping("/logout")
    public IMOOCJSONResult logout(@RequestParam String userId,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {

        //清除用户相关cookie
        CookieUtils.deleteCookie(request, response, "user");

        //TODO 分布式会话清除用户数据
        //清除购物车相关cookie
        CookieUtils.deleteCookie(request, response, SHOPCART);


        return IMOOCJSONResult.ok();
    }

    /**
     * @Description: 将属性设置为null
     * @author wangyujin
     * @Param: user:
     *  Created on :2020/3/2 23:29
     */
    public Users setNull(Users user) {
        user.setPassword(null);
        user.setMobile(null);
        user.setEmail(null);
        return user;
    }

    /**
     * @Description 同步购物车数据(redis,cookie)
     *  Created on :2020/3/30 15:10
     */
    public void synchShopcartData(String userId, HttpServletRequest request, HttpServletResponse response) {
        /**
         * 1.redis中无数据，如果cookie中也无数据，不需要同步
         * redis中无数据，如果cookie中有数据，需要同步
         * 2.redis中有数据，如果cookie中无数据，需要同步
         * redis中有数据，如果cookie中也有数据，需要同步，以cookie为主，覆盖或删除redis中的商品
         * 3.同步redis后，在同步本地redis
         */
        //redis中数据
        String str_redisShopCart = redisOperator.get(SHOPCART + ":" + userId);
        //cookie中数据
        String str_cookieShopcart = CookieUtils.getCookieValue(request, SHOPCART, true);
        if (StringUtils.isBlank(str_redisShopCart)) {
            //redis为null cookie不为null,直接cookie覆盖redis
            if (StringUtils.isNotBlank(str_cookieShopcart)) {
                redisOperator.set(SHOPCART + ":" + userId, str_cookieShopcart);
            }
        } else {
            //redis不为null cookie不为null,合并redis以及cookie,同一商品以cookie为主
            if (StringUtils.isNotBlank(str_cookieShopcart)) {
                List<ShopCartBO> redis_shopCartBOS = JsonUtils.jsonToList(str_redisShopCart, ShopCartBO.class);
                List<ShopCartBO> cookie_shopCartBOS = JsonUtils.jsonToList(str_cookieShopcart, ShopCartBO.class);
                //定义要给待删除list
                List<ShopCartBO> pendingDelList = new ArrayList<>();
                
                //已经存在的，吧cookie中对应的数量，覆盖redis
                for (ShopCartBO redis_shopCartBO : redis_shopCartBOS) {
                    //待删除的商品list
                    String redis_specId = redis_shopCartBO.getSpecId();
                    for (ShopCartBO cookie_shopCartBO : cookie_shopCartBOS) {
                        String cookie_specId = cookie_shopCartBO.getSpecId();
                        if (ObjectUtil.equal(redis_specId, cookie_specId)) {
                            //覆盖购买数量不累加，参考京东
                            redis_shopCartBO.setBuyCounts(cookie_shopCartBO.getBuyCounts());
                            //把cookieshopcartlist中的数据放入待删除，用于合并
                            pendingDelList.add(cookie_shopCartBO);
                        }
                    }
                }
                //从现有购物车中删除对应覆盖过的商品数据，
                cookie_shopCartBOS.removeAll(pendingDelList);
                //合并两个list
                redis_shopCartBOS.addAll(cookie_shopCartBOS);
                redisOperator.set(SHOPCART + ":" + userId, JsonUtils.objectToJson(redis_shopCartBOS));
                CookieUtils.setCookie(request, response, SHOPCART, JsonUtils.objectToJson(redis_shopCartBOS), true);
            //redis不为null cookie为null,redis覆盖cookie
            } else {
                CookieUtils.setCookie(request, response, SHOPCART, str_redisShopCart, true);
            }
        }
    }
}
