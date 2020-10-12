/**
 * smarthomesystem Copyright 2017 IKSONYA Co.ltd .
 * All rights reserved.
 * Package:  com.iksonya.shs.common.core.Interceptor
 * FileName: MyResettableServletRequest.java
 * @version 1.0
 * @author lizhaofeng
 * @created on 2018年3月19日
 */
package com.imooc.interceptor;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 *  HttpServletRequest包装类
 *  @author lizhaofeng
 *  @version 1.0
 *  </pre>
 *  Created on :2018年3月19日 下午6:12:02
 *  </pre>
 */
public class CommonHttpServletRequestWrapper extends HttpServletRequestWrapper {
	private String requestBody = null;

	public CommonHttpServletRequestWrapper(HttpServletRequest request) {
		super(request);
		if (requestBody == null) {
			requestBody = readBody(request);
		}
	}

	@Override
	public BufferedReader getReader() throws IOException {
		return new BufferedReader(new InputStreamReader(getInputStream()));
	}

	/**
	 *  覆盖读取方法
	 *  @return
	 *  @throws IOException
	 *  @author lizhaofeng
	 *  Created on :2018年3月20日 下午3:18:58
	 */
	@Override
	public ServletInputStream getInputStream() throws IOException {
		return new CommonServletInputStream(requestBody);
	}

	/**
	 *  读取requestbody
	 *  @param request
	 *  @return
	 *  @author lizhaofeng
	 *  Created on :2018年3月20日 下午1:41:02
	 */
	private static String readBody(ServletRequest request) {
		StringBuilder sb = new StringBuilder();
		String inputLine;
		BufferedReader br = null;
		try {
			br = request.getReader();
			while ((inputLine = br.readLine()) != null) {
				sb.append(inputLine);
			}
		} catch (IOException e) {
			throw new RuntimeException("Failed to read body.", e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
				}
			}
		}
		return sb.toString();
	}

	/**
	 *  缓存requestbody
	 *  @author lizhaofeng
	 *  @version 1.0
	 *  </pre>
	 *  Created on :2018年3月20日 下午3:19:12
	 *  </pre>
	 */
	private class CommonServletInputStream extends ServletInputStream {
		private ByteArrayInputStream buffer;

		public CommonServletInputStream(String body) {
			body = body == null ? "" : body;
			this.buffer = new ByteArrayInputStream(body.getBytes());
		}

		@Override
		public int read() throws IOException {
			return buffer.read();
		}

		@Override
		public boolean isFinished() {
			return buffer.available() == 0;
		}

		@Override
		public boolean isReady() {
			return true;
		}

		@Override
		public void setReadListener(ReadListener listener) {
			throw new RuntimeException("Not implemented");
		}
	}

}
