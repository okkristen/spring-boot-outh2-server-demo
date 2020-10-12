package com.hzys.ssoserver.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.List;

/**
 * 测试
 */
@Controller
@RequestMapping("/user")
public class TestController {
//    未登录时  跳转的  登录页面
//    @RequestMapping("/login")
////    @ResponseBody
//    public String login(HttpServletRequest request, HttpServletResponse response) {
//        System.out.println("1232131233");
//        return "login";
//    }

//    登录页面 请求的 接口
//    @RequestMapping("/form")
////    @ResponseBody
//    public String form(HttpServletRequest request, HttpServletResponse response) {
//        System.out.println("1232131233");
//        String username = request.getParameter("username");
//        String password = request.getParameter("password");
//        System.out.println("username: " + username);
//        System.out.println("password: " + password);
//        return "login";
//    }

    /**
     * *******************开放给SSO客户端使用的接口 *******************
     */
    @PostMapping("/oauth/sso")
    @ResponseBody
//    @ApiOperation(value = "获取登录用户", notes = "SSO客户端使用接口", produces = "application/json")
    public String getSsoUserId(Principal user) {
        OAuth2Authentication u = (OAuth2Authentication) user;
        String username = u.getPrincipal().toString();
        System.out.println(username);
        return username;
    }

    @PostMapping("/oauth/all")
    @ResponseBody
//    @ApiOperation(value = "下拉框用户列表", notes = "SSO客户端使用接口", produces = "application/json")
    public String getSsoUserList() {
//        List<User> list = userService.listAll();
        return "list";
    }

//    @ApiOperation(value = "用户所有的权限", notes = "SSO客户端使用接口", produces = "application/json")
    @PostMapping("/oauth/menu")
    @ResponseBody
    public String getSsoUserMenus() {
//        Long userId = SecurityUtils.getLoginUserId();
        return "权限";
    }
}
