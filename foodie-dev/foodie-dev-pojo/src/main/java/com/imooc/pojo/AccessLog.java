package com.imooc.pojo;

import com.alibaba.fastjson.JSONObject;
import lombok.Builder;
import lombok.Data;

/**
 * 日志文件
 *
 * @author wangyujin
 * @version 1.0
 * </pre>
 * Created on 2020-09-23 14:38
 * </pre>
 */
@Builder
@Data
public class AccessLog {

    private String loginId;

    private String userId;

    private String requestContentType;

    private String method;

    private String url;

    private JSONObject paramsData;

    private String clientIp;

    private Long beginTime;

    private Long endTime;

    private Long consumingTime;

    private Integer returnStatusCode;

    private JSONObject returnData;

    private String exceptionMsg;

    private Short Status;

    private String remark;

    private Long createTime;

}
