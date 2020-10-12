package com.hzys.ssoserver.config.sso;//package com.hzys.dd.config.sso;

import com.yh.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Permission;
import java.util.ArrayList;
import java.util.List;

//自定义userdetailservice
@Service("userDetailsService")
public class UserDetailServiceImpl implements UserDetailsService {

//    @Autowired
//    SysUserRepository sysUserRepository;
//    @Autowired
//    PermissionService permissionService;
//    @Autowired
//    PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // SysUser sysUser = sysUserRepository.getUserByName(username);
        System.err.println("username===============" + username);
        String sysUser = username; //"admin";
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        if (sysUser != null) {
            System.err.println("sysUser===============" + sysUser);
            //获取用户的授权
            List<Permission> permissions = new ArrayList<>(); // permissionService.findByAdminUserId(sysUser.getId());
            //声明授权文件
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_"+"admin");
            grantedAuthorities.add(grantedAuthority);
//            for (Permission permission : permissions) {
//                if (permission != null && permission.getName() != null) {
//                    GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_"+permission.getName());
//                    grantedAuthorities.add(grantedAuthority);
//                }
//            }
        }
        System.err.println("grantedAuthorities===============" + grantedAuthorities);
        return new User(sysUser, new BCryptPasswordEncoder().encode("123456"), grantedAuthorities);

    }
}
