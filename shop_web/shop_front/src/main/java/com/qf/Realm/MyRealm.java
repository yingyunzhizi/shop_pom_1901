package com.qf.Realm;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.IUserService;
import com.qf.entity.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

/**
 * @version 1.0
 * @data 5/30/2019 10:26
 * @user yingyunzhizi
 */
public class MyRealm extends AuthorizingRealm {
    @Reference
    private IUserService userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("token是用户输入的");
        String username = (String) token.getPrincipal();
        User user = userService.getUserByUsername(username);

        //封装
        if (user==null){
            return null;
        }
        // 进行验证，将正确数据讲给shiro处理
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                user,
                user.getPassword(),
                ByteSource.Util.bytes(username), // 加盐后的密码
                getName() // 指定当前 Realm 的类名
        );
        // 返回给安全管理器，由 securityManager 比对密码的正确性
        return authenticationInfo;
    }

    /**
     * 清空用户的缓存
     */
    public void clearCache(){
        PrincipalCollection principal = SecurityUtils.getSubject().getPreviousPrincipals();
        super.clearCache(principal);
    }
}
