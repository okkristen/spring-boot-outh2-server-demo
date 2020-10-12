package com.hzys.ssoserver.config.sso.hander;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户访问没有权限资源的处理
 * @author lirong
 * @date
 */

@Component("securityAccessDeniedHandler")
public class SecurityAccessDeniedHandler implements AccessDeniedHandler {

	private Logger logger = LoggerFactory.getLogger(getClass());
//	@Autowired
//	private IApplicationConfig applicationConfig;
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) {
		logger.info(request.getRequestURL()+"没有权限");

//		ResponseUtils.renderJson(request, response, ResultCode.LIMITED_AUTHORITY, applicationConfig.getOrigins());
	}
}
