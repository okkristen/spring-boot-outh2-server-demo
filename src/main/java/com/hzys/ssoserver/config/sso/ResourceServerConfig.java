package com.hzys.ssoserver.config.sso;//package com.hzys.dd.config.sso;
//
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

@Configuration
@EnableResourceServer //这个类表明了此应用是OAuth2 的资源服务器，此处主要指定了受资源服务器保护的资源链接
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        // 如果关闭 stateless，则 accessToken 使用时的 session id 会被记录，后续请求不携带 accessToken 也可以正常响应
        // 如果 stateless 为 true 打开状态，则 每次请求都必须携带 accessToken 请求才行，否则将无法访问
        resources.resourceId("oauth2").stateless(true);
    }

    /**
     * 路径设置
     * @param http
     * @throws Exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable()//禁用了 csrf 功能
//                .authorizeRequests()//限定签名成功的请求
//                .antMatchers("/decision/**","/govern/**").hasAnyRole("USER","ADMIN")
//                .antMatchers("/admin/**").hasRole("ADMIN")
//                .antMatchers("/test/**").authenticated()//必须认证过后才可以访问
//                .anyRequest().permitAll()//其他没有限定的请求，允许随意访问
        // 获取登录用户的 session
//                .and().anonymous();//对于没有配置权限的其他请求允许匿名访问
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                // 资源服务器拦截的路径 注意此路径不要和主过滤器冲突
                .requestMatchers().antMatchers("/user/oauth/**");
        http
                .authorizeRequests()
                // 配置资源服务器已拦截的路径才有效
                .antMatchers("/user/oauth/**").authenticated();//其他没有限定的请求，允许随意访问;
//        http.authorizeRequests().antMatchers("/oauth/token").permitAll();


    }

}
