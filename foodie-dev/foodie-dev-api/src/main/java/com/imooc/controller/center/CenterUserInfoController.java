package com.imooc.controller.center;

import com.imooc.bo.center.CenterUserBO;
import com.imooc.controller.BaseController;
import com.imooc.pojo.Users;
import com.imooc.resource.FileUpload;
import com.imooc.service.center.CenterUserService;
import com.imooc.utils.CookieUtils;
import com.imooc.utils.DateUtil;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
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
@RequestMapping("/userInfo")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CenterUserInfoController extends BaseController {

    private final CenterUserService centerUserService;

    private final FileUpload fileUpload;

    @ApiOperation(value = "根据用户Id查询用户信息", notes = "根据用户Id查询用户信息", httpMethod = "POST")
    @PostMapping("/update")
    public IMOOCJSONResult update(
            @ApiParam(name = "userId", value = "用户ID", required = true)
            @RequestParam String userId,
            @ApiParam(name = "CenterUserBO", value = "用户提交修改信息VO", required = true)
            @Valid @RequestBody CenterUserBO centerUserBO,
            BindingResult result,
            HttpServletRequest request,
            HttpServletResponse response) {

        if (result.hasErrors()) {
            return IMOOCJSONResult.errorMap(getErrors(result));
        }

        Users users = centerUserService.updateUserInfo(userId, centerUserBO);
        Users newUsers = this.setNull(users);

        //TODO 后续整合redis分布式token

        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(newUsers), true);


        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "用户头像修改", notes = "用户头像修改", httpMethod = "POST")
    @PostMapping("/uploadFace")
    public IMOOCJSONResult uploadFace(
            @ApiParam(name = "userId", value = "用户ID", required = true)
            @RequestParam String userId,
            @ApiParam(name = "file", value = "用户头像", required = true)
            MultipartFile file,
            HttpServletRequest request,
            HttpServletResponse response) {

        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;

        //定义头像的特定位置
//        String fileSpace = IMAGE_USER_FACE_LOCATION;
        String fileSpace = fileUpload.getImageUserFaceLocation();
        //在路径上为没有给用户增加一个userId,用于区分不同的上传
        String uploadPathPrefix = File.separator + userId;
        //开始文件上传
        if (file != null) {
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
                //文件名称重组face-{userId}.jpg 当前方法时覆盖式的
                //增量式额外拼接时间戳即可
                String newFileName = "face-" + userId + "." + suffix;
                String finalFacePath = fileSpace + uploadPathPrefix + File.separator + newFileName;

                //提供给web服务地址
                uploadPathPrefix +=  "/" + newFileName;

                File outFile = new File(finalFacePath);
                if (outFile.getParentFile() != null) {
                    //创建文件夹
                    outFile.getParentFile().mkdirs();
                }
                //文件输出保存目录
                try {
                    fileOutputStream = new FileOutputStream(outFile);
                    inputStream = file.getInputStream();
                    IOUtils.copy(inputStream, fileOutputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.flush();
                            fileOutputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }

        } else {
            return IMOOCJSONResult.errorMsg("文件不能为空！");
        }

        //更新用户头像到数据库
        String imageServerUrl = fileUpload.getImageServerUrl();
        Users newUsers = centerUserService.updateUserFace(userId,
                 imageServerUrl +
                        uploadPathPrefix +
                        "?t=" +
                        DateUtil.getCurrentDateString(DateUtil.DATE_PATTERN));
        //TODO 后续整合redis分布式token

        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(newUsers), true);

        return IMOOCJSONResult.ok();
    }

    /**
     * @Description: 将属性设置为null
     * @author wangyujin
     * @Param: user:
     *  Created on :2020/3/2 23:29
     */
    private Users setNull(Users user) {
        user.setPassword(null);
        user.setMobile(null);
        user.setEmail(null);
        return user;
    }

    /**
     * @Description: 解析错误格式
     * @author wangyujin
     * @Param: result:
     * @return: java.util.Map<java.lang.String,java.lang.String>
     *  Created on :2020/3/16 22:46
     */
    private Map<String, String> getErrors(BindingResult result) {
        List<FieldError> fieldErrors = result.getFieldErrors();

        Map<String, String> resultMap = fieldErrors.stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));

        return resultMap;

    }
}
