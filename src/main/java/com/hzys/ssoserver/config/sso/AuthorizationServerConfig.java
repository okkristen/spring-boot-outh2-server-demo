package com.hzys.ssoserver.config.sso;

import com.hzys.ssoserver.config.sso.redirectResolver.MyRedirectResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.endpoint.RedirectResolver;
import org.springframework.security.oauth2.provider.endpoint.TokenKeyEndpoint;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.*;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.sql.DataSource;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableAuthorizationServer // 这个注解告诉 Spring 这个应用是 OAuth2 的授权服务器//
// 提供/oauth/authorize,/oauth/token,/oauth/check_token,/oauth/confirm_access,/oauth/error
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisConnectionFactory connectionFactory;

    @Autowired
    @Qualifier("userDetailsService")
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DataSource dataSource;

    private final static   String client_id = "client";

    private final static   String client_secret = "admin";

    @Bean
    public TokenStore tokenStore() {
        RedisTokenStore redis = new RedisTokenStore(connectionFactory);
        return redis;
    }


//    @Bean
//    public DefaultAccessTokenConverter defaultAccessTokenConverter() {
//        DefaultAccessTokenConverter defaultAccessTokenConverter = new DefaultAccessTokenConverter();
//        return defaultAccessTokenConverter;
//    }


    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//        clients.jdbc(dataSource);
        InMemoryClientDetailsServiceBuilder inMemoryClientDetailsServiceBuilder = clients.inMemory();
        List<String> list = new ArrayList<>(Arrays.asList("", "1"));
        for (String s: list  ) {
            inMemoryClientDetailsServiceBuilder
                    .withClient( (client_id + s).trim() )
                    .secret(new BCryptPasswordEncoder().encode((client_secret + s).trim()))
                    .authorizedGrantTypes("password", "refresh_token")//允许授权范围
                    .authorizedGrantTypes("authorization_code", "implicit")
                    .authorities("ROLE_ADMIN","ROLE_USER")//客户端可以使用的权限
                    .scopes( "read", "write")
                    //  必须要在该范围内才能转发
//                 .redirectUris("")
                    // 是否跳过授权部分
                    .autoApprove(true)
                    .accessTokenValiditySeconds(60 * 2  * 100)
                    .refreshTokenValiditySeconds(60 * 2 * 100);
        }
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        String urlPrefix = "";
        endpoints.tokenStore(tokenStore())
                .authenticationManager(authenticationManager)
                .accessTokenConverter(jwtAccessTokenConverter())
                // 自动化重定向策略 换成用户传入的路径
                .redirectResolver(new MyRedirectResolver())
//                将目标的路径 换成自己想要的请求
                .pathMapping("/oauth/authorize", urlPrefix+"/oauth/authorize")
                .pathMapping("/oauth/token", urlPrefix+"/oauth/token")
                .pathMapping("/oauth/confirm_access", urlPrefix+"/oauth/confirm_access")
                .pathMapping("/oauth/error", urlPrefix+"/oauth/error")
                .pathMapping("/oauth/check_token", urlPrefix+"/oauth/check_token")
                .pathMapping("/oauth/token_key", urlPrefix+"/oauth/token_key")
                .userDetailsService(userDetailsService);//必须设置 UserDetailsService 否则刷新token 时会报错
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
//        security
////                .tokenKeyAccess("permitAll()")
////                .checkTokenAccess("isAuthenticated()")
////                .checkTokenAccess("permitAll()")
//                .allowFormAuthenticationForClients();//允许表单登录
//        security.passwordEncoder(passwordEncoder);
        // 对于CheckEndpoint控制器[框架自带的校验]的/oau而th/check端点允许所有客户端发送器请求不会被Spring-security拦截
        // 开启/oauth/token_key验证端口无权限访问
        // 开启/oauth/check_token验证端口认证权限访问
        security
                // 开启/oauth/token_key验证端口无权限访问 已授权用户 获取 token 接口。
                .tokenKeyAccess("isAuthenticated()")
                // 开启/oauth/check_token验证端口认证权限访问 已授权用户访问 checkToken 接口
                .checkTokenAccess("isAuthenticated()")
                .allowFormAuthenticationForClients();
//        security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()").allowFormAuthenticationForClients();
        security.passwordEncoder(passwordEncoder);
//        security.realm("oauth2");
    }
//

    //    @Bean
//    public JwtTokenStore tokenStore() {
//        JwtTokenStore jwtTokenStore =  new JwtTokenStore(jwtAccessTokenConverter());
//        return  jwtTokenStore;
//    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setSigningKey("hzys");   //  Sets the JWT signing key
        return jwtAccessTokenConverter;
    }

//    @Bean
//    public TokenKeyEndpoint tokenKeyEndpoint() {
//        return new TokenKeyEndpoint(jwtAccessTokenConverter());
//    }

//    private JwtAccessTokenConverter jwtAccessTokenConverter() {
//        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
//        jwtAccessTokenConverter.setKeyPair(keyPair());
//        return jwtAccessTokenConverter;
//    }
//
//    private KeyPair keyPair() {
//        return new KeyStoreKeyFactory(new ClassPathResource("xxx.jks"), "123456".toCharArray()).getKeyPair("xxx", "123456".toCharArray());
//    }



}
