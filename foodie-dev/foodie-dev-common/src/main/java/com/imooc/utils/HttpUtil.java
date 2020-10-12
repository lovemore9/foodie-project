package com.imooc.utils;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;

/**
 * 类简要说明
 *
 * @author wangyujin
 * @version 1.0
 * </pre>
 * Created on 2020-09-23 13:20
 * </pre>
 */

public class HttpUtil {

    public static String getRequestParams(HttpServletRequest request) {

        JSONObject jsonObject = new JSONObject();
        String nowUrlParams = "";
        String contentType = request.getContentType();
        if (StringUtils.indexOf(contentType, "multipart/form-data") == -1) {
            InputStream is = null;
            try {
                is = request.getInputStream();
                nowUrlParams = IOUtils.toString(is, "utf-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
            jsonObject = JSONObject.parseObject(nowUrlParams) == null ? new JSONObject() : JSONObject.parseObject(nowUrlParams);
            Map<String, String[]> parameterMap = request.getParameterMap();
            if (parameterMap != null && !parameterMap.isEmpty()) {
                for (String key : parameterMap.keySet()) {
                    jsonObject.put(key, Arrays.asList(parameterMap.get(key)));
                }
            }
        }
        return JSON.toJSONString(jsonObject);
    }
}
