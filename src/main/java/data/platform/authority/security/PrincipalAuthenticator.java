package data.platform.authority.security ;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired ;
import org.springframework.beans.factory.annotation.Qualifier ;
import org.springframework.security.authentication.AuthenticationProvider ;
import org.springframework.security.authentication.AuthenticationServiceException ;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken ;
import org.springframework.security.core.Authentication ;
import org.springframework.security.core.AuthenticationException ;
import org.springframework.security.core.userdetails.UserDetails ;
import org.springframework.stereotype.Service ;

import data.academic.util.HttpClientUtil;
import data.framework.support.ConfigContext;
import data.framework.utility.EncryptHelper;


/**
 * 平台－基于  Spring Security 的用户验证器。
 * @author wanggq
 */
@Service
public class PrincipalAuthenticator implements AuthenticationProvider
{
    @SuppressWarnings("unused")
	@Override
    public Authentication authenticate( Authentication authentication ) throws AuthenticationException
    {
        String username = authentication.getName() ;
        
        String userPassword = authentication.getCredentials().toString();
        Map<String, String> userMap=new HashMap<String, String>();
        userMap.put("userName", username);
        userMap.put("pwd", userPassword);
        userMap.put("appId", ConfigContext.getValue("framework.academic.default.setup['appId']"));
        userMap.put("sysCode", ConfigContext.getValue("framework.academic.default.setup['sysCode']"));
        userMap.put("sysPasswd",ConfigContext.getValue("framework.academic.default.setup['sysPasswd']"));
        UserDetails user = this.getUserDetailsService().loadUserByUserbua(userMap) ;
        
        if( user == null )
        {
            throw new AuthenticationServiceException( "帐号不存在！" ) ;
        }

        if( !user.isEnabled() )
        {
            throw new AuthenticationServiceException( "帐号已经被管理员禁用！" ) ;
        }
        if( !user.isAccountNonExpired() )
        {
            throw new AuthenticationServiceException( "帐号已经过期！" ) ;
        }
        if( !user.isAccountNonLocked() )
        {
            throw new AuthenticationServiceException( "帐号已经被管理员锁定！" ) ;
        }
        if(!user.isCredentialsNonExpired())
        {
            throw new AuthenticationServiceException( "密码已经过期！" ) ;
        }
        
        if (user.getAuthorities() == null) {
			throw new AuthenticationServiceException("账号暂无权限！");
		}
        
        String password = EncryptHelper.encryptEncode( authentication.getCredentials().toString() ) ;
        String pwd=EncryptHelper.encryptEncode(user.getPassword());
        if( !pwd.equals( password ) )
        {
            throw new AuthenticationServiceException( "密码错误！" ) ;
       }

        UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(
                user,
                authentication.getCredentials(),
                user.getAuthorities());

        result.setDetails(authentication.getDetails());
        
        //HttpClientUtil httpClientUtil=new HttpClientUtil();
        //httpClientUtil.getUpdateData();
        return result ;
    }

    @Override
    public boolean supports( Class<? extends Object> authentication )
    {
        return (Authentication.class.isAssignableFrom(authentication));
    }

    /**
     * 设置用户身份标识提供类，通过 spring 容器来完成注入。
     * @param userDetailsService 用户身份标识提供类
     */
    @Autowired
    @Qualifier( "databaseProvider" )
    public void setUserDetailsService( PrincipalProvider userDetailsService )
    {
        this.userDetailsService = userDetailsService;
    }

    /**
     * 获取用户身份标识提供类。
     * @return 用户身份标识提供类
     */
    protected PrincipalProvider getUserDetailsService()
    {
        return userDetailsService;
    }
    
    private PrincipalProvider userDetailsService;
}