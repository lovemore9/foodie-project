/**
 * smarthomesystem Copyright 2017 IKSONYA Co.ltd .
 * All rights reserved.
 * Package:  com.iksonya.shs.common.core.Interceptor
 * FileName: BodyFilter.java
 * @version 1.0
 * @author lizhaofeng
 * @created on 2018年3月19日
 */
package com.imooc.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.imooc.interceptor.CommonHttpServletRequestWrapper;
import org.apache.commons.lang3.StringUtils;


/**
 *  RequstBody过滤器，使用CommonHttpServletRequestWrapper替换HttpServletRequest
 *  @author lizhaofeng
 *  @version 1.0
 *  </pre>
 *  Created on :2018年3月19日 下午6:14:45
 *  </pre>
 */
public class RequestBodyFilter implements Filter{
	
	public void init(FilterConfig filterConfig) throws ServletException {
		// Nothig to do
	}
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		//使用 CommonHttpServletRequestWrapper备份HttpServletRequest
		String contentType = request.getContentType();
		if(StringUtils.indexOf(contentType,"multipart/form-data") > -1) {//文件上传不重新包装request
			chain.doFilter(request, response);
		}else {
			chain.doFilter(new CommonHttpServletRequestWrapper((HttpServletRequest) request), response);
		}
		
	}

	@Override
	public void destroy() {
		// Nothig to do

	}


}
