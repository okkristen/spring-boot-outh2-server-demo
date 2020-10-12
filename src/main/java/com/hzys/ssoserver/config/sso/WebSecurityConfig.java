package com.hzys.ssoserver.config.sso;//package com.hzys.dd.config.sso;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    @Qualifier("securityAuthenticationProvider")
    private AuthenticationProvider securityAuthenticationProvider;

    @Autowired
    @Qualifier("userLoginSuccessHandler")
    private AuthenticationSuccessHandler userLoginSuccessHandler;

    @Autowired
    @Qualifier("securityAuthenticationFailureHandler")
    private AuthenticationFailureHandler securityAuthenticationFailureHandler;

    @Autowired
    @Qualifier("securityLogoutSuccessHandler")
    private LogoutSuccessHandler securityLogoutSuccessHandler;

    @Autowired
    @Qualifier("securityAccessDeniedHandler")
    private AccessDeniedHandler securityAccessDeniedHandler;


    @Autowired
    @Qualifier("securityAuthenticationEntryPoint")
    private AuthenticationEntryPoint securityAuthenticationEntryPoint;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean() ;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        super.configure(http);
//        http.csrf().disable()//禁用了 csrf 功能
//                .authorizeRequests()//限定签名成功的请求
////                .antMatchers("/oauth/**", "/login/**", "/logout/**")
//                .antMatchers("/decision/**","/govern/**").hasAnyRole("USER","ADMIN")//对decision和govern 下的接口 需要 USER 或者 ADMIN 权限
//                .antMatchers("/admin/login").permitAll()///admin/login 不限定
//                .antMatchers("/admin/**").hasRole("ADMIN")//对admin下的接口 需要ADMIN权限
//                .antMatchers("/oauth/**").permitAll()//不拦截 oauth 开放的资源
//                .anyRequest().permitAll()//其他没有限定的请求，允许访问
//                .and().anonymous()//对于没有配置权限的其他请求允许匿名访问
//                .and().formLogin()//使用 spring security 默认登录页面
//                .and().httpBasic();//启用http 基础验证

        //不拦截 oauth 开放的资源
//        http.csrf().disable();
        http.requestMatchers()
                    //使HttpSecurity接收以"/login/","/oauth/"开头请求。
                .antMatchers("/oauth/**", "/login/**", "/logout/**")
                .and()
                .authorizeRequests()
                .antMatchers("/oauth/**").authenticated()
                .and();
                http.formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .permitAll()
                .failureHandler(securityAuthenticationFailureHandler)
                .successHandler(userLoginSuccessHandler)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(securityAuthenticationEntryPoint)
                .accessDeniedHandler(securityAccessDeniedHandler)
                .and()
                .logout()
                .deleteCookies("JSESSIONID")
                .logoutUrl("/logout")
                .logoutSuccessHandler(securityLogoutSuccessHandler)
                .permitAll()
                .and()
                .csrf()
                .disable();

        ;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
        super.configure(auth);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }
}


