package com.hzys.ssoserver.config.sso.hander;

import com.hzys.ssoserver.restResult.RestResult;
import com.hzys.ssoserver.utils.ResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Component("securityLogoutSuccessHandler")
public class SecurityLogoutSuccessHandler implements LogoutSuccessHandler {

	private Logger logger = LoggerFactory.getLogger(getClass());
//	@Autowired
//	private IApplicationConfig applicationConfig;
//	@Resource
//	private SysLogService sysLogService;
	@Override
	public void onLogoutSuccess(HttpServletRequest request , HttpServletResponse response , Authentication authentication) {

		// 清除登录的session
		request.getSession().invalidate();
		// 记录登出日志
		if (null != authentication) {
			this.saveLog(request, authentication);
		}
		System.out.println("name:" + authentication.getName());
		logger.info("退出成功");
		// JSON 格式的返回
		 ResponseUtils.renderSuccessJson(request, response, new RestResult(200, "退出成功"), new String[]{});
	}

	/**
	 * 记录登出日志
	 * @param request
	 */
	private void saveLog(HttpServletRequest request, Authentication authentication) {
//		LoginUserDTO user = (LoginUserDTO) authentication.getPrincipal();
//		SysLog sysLog = SecurityUtils.buildLog(request, authentication);
//		sysLog.setOperation(6);
//		sysLog.setLogDesc(user.getUsername() + " 退出了系统 ");
//		sysLog.setLogType(2);
//		sysLogService.saveLog(sysLog);
	}

}
