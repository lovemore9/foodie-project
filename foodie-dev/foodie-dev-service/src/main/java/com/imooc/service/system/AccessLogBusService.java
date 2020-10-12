package com.imooc.service.system;

import com.imooc.pojo.AccessLog;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 系统日志业务处理
 *
 * @author wangyujin
 * @version 1.0
 * </pre>
 * Created on 2020-09-23 15:01
 * </pre>
 */

public interface AccessLogBusService {

    /**
     * @Description: 生成日志实体对象
     *
     * @author wangyujin
     * @Param: request:
     * @Param: response:
     * @Param: exception:
     * @Param: token:
     * @return: com.imooc.pojo.AccessLog
     * @version 1.0
     * </pre>
     *  Created on :2020/9/23 15:14
     * </pre>
     */
    AccessLog generateAccessLogPojo(HttpServletRequest request, HttpServletResponse response, Exception exception, String token);
}
