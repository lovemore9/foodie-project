package com.imooc.controller;

import com.imooc.pojo.Users;
import com.imooc.service.FdfsService;
import com.imooc.service.center.CenterUserService;
import com.imooc.utils.CookieUtils;
import com.imooc.utils.DateUtil;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
@Api(value = "用户信息", tags = "用户信息展示相关接口")
@RestController
@RequestMapping("/fdfs")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CenterUserInfoController {

    private final CenterUserService centerUserService;

    private final FdfsService fdfsService;

    @Value("${file.host}")
    private String fileHost;

    @PostMapping("/uploadFace")
    public IMOOCJSONResult uploadFace(
            @ApiParam(name = "userId", value = "用户ID", required = true)
            @RequestParam String userId,
            @ApiParam(name = "file", value = "用户头像", required = true)
            MultipartFile file,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String path = "";

        //开始文件上传
        if (file != null) {
            //文件上床名称
            String fileName = file.getOriginalFilename();
            if (StringUtils.isNotBlank(fileName)) {
                //获取文件后缀名
                String[] fileNameArr = fileName.split("\\.");
                String suffix = fileNameArr[fileNameArr.length - 1];
                //防止上传可执行文件
                if (!suffix.equalsIgnoreCase("png") &&
                        !suffix.equalsIgnoreCase("jpg") &&
                        !suffix.equalsIgnoreCase("jepg")) {
                    return IMOOCJSONResult.errorMsg("图片格式不正确");
                }
                path = fdfsService.upload(file, suffix);
                log.info("文件上传成功，userId：{}，path：{}", userId, path);
            }
        } else {
            return IMOOCJSONResult.errorMsg("文件不能为空！");
        }

        if (StringUtils.isBlank(path)) {
            return IMOOCJSONResult.errorMsg("上传头像失败！");
        } else {
            //更新用户头像到数据库
            Users newUsers = centerUserService.updateUserFace(userId, fileHost+ path);
            //TODO 后续整合redis分布式token
            CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(newUsers), true);

            return IMOOCJSONResult.ok();
        }


    }


}
