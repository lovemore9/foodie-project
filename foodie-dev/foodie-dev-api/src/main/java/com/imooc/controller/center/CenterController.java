package com.imooc.controller.center;

import com.imooc.service.center.CenterUserService;
import com.imooc.utils.IMOOCJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 类简要说明
 *
 * @author wangyujin
 * @version 1.0
 * </pre>
 * Created on 2020-03-16 21:59
 * </pre>
 */
@Slf4j
@Api(value = "center - 用户中心", tags = "用户中心展示相关接口")
@RestController
@RequestMapping("/center")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CenterController {

    private final CenterUserService centerUserService;

    @ApiOperation(value = "根据用户Id查询用户信息", notes = "根据用户Id查询用户信息", httpMethod = "GET")
    @GetMapping("/userInfo")
    public IMOOCJSONResult userInfo(
            @ApiParam(name = "userId", value = "用户ID", required = true)
            @RequestParam String userId) {

        return IMOOCJSONResult.ok(centerUserService.queryUserInfo(userId));
    }
}
