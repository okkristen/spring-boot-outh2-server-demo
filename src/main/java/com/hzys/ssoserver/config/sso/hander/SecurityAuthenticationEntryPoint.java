package com.hzys.ssoserver.config.sso.hander;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用户未登录时的处理
 * @author lirong
 * @date 2019-8-8 17:37:27
 */
@Component("securityAuthenticationEntryPoint")
public class SecurityAuthenticationEntryPoint implements AuthenticationEntryPoint {
	private Logger logger = LoggerFactory.getLogger(getClass());
//	@Autowired
//	private IApplicationConfig applicationConfig;
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
		if(request.getMethod() != HttpMethod.OPTIONS.toString()) {
			logger.info("尚未登录:" + authException.getMessage());
		}
		// 1. 返回json让前端自己处理
		// ResponseUtils.renderJson(request, response, ResultCode.UNLOGIN, applicationConfig.getOrigins());
		// 2. 由后端直接跳转到登录页面
		try {
			response.sendRedirect(request.getContextPath() + "/static/login.html");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
