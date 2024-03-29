package com.hzys.ssoserver.config.sso.hander;


import com.hzys.ssoserver.restResult.RestResult;
import com.hzys.ssoserver.utils.ResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component("userLoginSuccessHandler")
public class UserLoginSuccessHandler implements AuthenticationSuccessHandler {
    private Logger logger = LoggerFactory.getLogger(getClass());
//    @Resource
//    private MenuRightMapper menuRightMapper;
//    @Autowired
//    private IApplicationConfig applicationConfig;
//    @Resource
//    private SysLogService sysLogService;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        //获得授权后可得到用户信息   可使用SecurityUserService进行数据库操作
//        LoginUserDTO userDetails = (LoginUserDTO) authentication.getPrincipal();
//        Long userId = userDetails.getId();
//        List<MenuRight> menus = menuRightMapper.getUserMenus(userId);
//        LoginOutpDTO dto = new LoginOutpDTO();
//        BeanUtils.copyProperties(userDetails, dto);
//        dto.setMenus(menus);
        //输出登录提示信息
        logger.info("用户 " + authentication.getName() + " 登录");
        logger.info("用户 " + authentication.getAuthorities() + "角色权限");
//        log.info("IP :"+ IPUtils.getIpAddr(request));
        // 记录登录成功的日志
        this.saveLog(request, authentication);
        // JSON 格式的返回
        ResponseUtils.renderSuccessJson(request, response, new RestResult(200, "登0录成功", null),  new String[]{});
    }


    /**
     * 记录登录成功的日志
     * @param request
     */
    private void saveLog(HttpServletRequest request, Authentication authentication) {
//        LoginUserDTO user = (LoginUserDTO) authentication.getPrincipal();
//        SysLog sysLog = SecurityUtils.buildLog(request, authentication);
//        sysLog.setOperation(5);
//        sysLog.setLogDesc(user.getUsername() + " 登录了系统 ");
//        sysLog.setLogType(2);
//        sysLogService.saveLog(sysLog);
    }
}

