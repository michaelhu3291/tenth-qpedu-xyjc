package data.platform.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractBaseController;
import data.platform.authority.security.PrincipalDetails;
import data.platform.authority.security.SecurityContext;
import data.platform.common.MenuTree;
import data.platform.entity.EntityPlatformRole;
import data.platform.entity.EntityPlatformUser;
import data.platform.service.PlatformUserService;

/**
 * 平台-框架首页控制器类。
 * @author LIUGUO
 *
 */
@Controller
@RequestMapping( "main" )
public class PlatformMainController extends AbstractBaseController
{

    @Override
    protected void init( ModelMap model, HttpServletRequest request )
    {
    	
    	EntityPlatformUser userInfo=userService.load(SecurityContext.getPrincipal().getId());
		model.addAttribute("user", userInfo);
    }

    /**
     * 初始化登录用户的菜单信息。
     * @param out 响应输出对象
     */
    @RequestMapping(params = "command=initMenu")
    public void initMenu( java.io.PrintWriter out )
    {
        if( SecurityContext.isAuthenticated() )
        {
        	//List<MenuTree> menuTree = userService.getMenusByUserAndRole( SecurityContext.getPrincipal().getId(), SecurityContext.getPrincipal().getRoleId() ) ;
        	//List<MenuTree> menuTree = userService.getMenusByUser( SecurityContext.getPrincipal().getUsername() ) ;
        	List<MenuTree> menuTree = userService.getMenusByUser( SecurityContext.getPrincipal().getId() ) ;
            Map<String,Object> map = new HashMap<String,Object>() ;
            map.put( "success", true );
            map.put( "response", menuTree );
           
            out.print( getSerializer().formatMap( map ) ) ;
        }
    }
    
    /**
     * 获取显示登录用户信息和与当前用户的其他角色信息（不含当前角色）。
     * @param out 响应输出对象
     */
    @RequestMapping(params = "command=getUserInfo")
    public void getUserInfo( java.io.PrintWriter out )
    {
        if( SecurityContext.isAuthenticated() )
        {
            PrincipalDetails userDetails = SecurityContext.getPrincipal() ;
            Map<String,Object> map = new HashMap<String,Object>() ;
            map.put( "success", true ) ;
            map.put( "userId", userDetails.getId() ) ;
            map.put( "displayName", userDetails.getChineseName() ) ;
            map.put( "userName", userDetails.getUsername() ) ;
            map.put( "userIcon", "theme/default/images/u_w.png" ) ;
            //修改
            List<EntityPlatformUser> otherUsers = userService.getOtherUsersByADAccount( userDetails.getId(), userDetails.getUsername()) ;
            List<Map<String,String>> newOtherUsers = new ArrayList<Map<String,String>>() ;
            if( otherUsers != null && !otherUsers.isEmpty() )
            {
                for( EntityPlatformUser entity : otherUsers )
                {
                    Map<String,String> otherMap = new HashMap<String,String>() ;
                    otherMap.put( "userId", entity.getId() ) ;
                    otherMap.put( "displayName", entity.getChineseName() ) ;
                    otherMap.put( "userName", entity.getLoginAccount() ) ;
                    otherMap.put( "userIcon", "theme/default/images/u_w.png" ) ;
                    newOtherUsers.add( otherMap ) ;
                }
            }


            List<EntityPlatformRole> roleList = new ArrayList<EntityPlatformRole>();
            
            roleList = userService.getUserRoleInfo(userDetails.getId());
            for (EntityPlatformRole entity : roleList) {
				String roleName = entity.getRoleName();
				
				if (!map.containsKey("userRole")){
					map.put("userRole", roleName);
				}
			}
            
            map.put( "otherUsers", newOtherUsers ) ;
            out.print( getSerializer().formatMap( map ) ) ;

        }
    }
    
    /**
     * 初始化登录用户的菜单信息。
     * @param out 响应输出对象
     */
    @RequestMapping(params = "command=getUserSomeInfo")
    public void getUserSomeInfo( java.io.PrintWriter out )
    {
    	EntityPlatformUser userInfo=userService.load(SecurityContext.getPrincipal().getId());
    	out.print( getSerializer().formatObject( userInfo ) ) ;
    }
    
    @RequestMapping( params = "command=switchRole" )
    public void switchRole( HttpServletRequest request, java.io.PrintWriter out ) throws Exception
    {
        String roleId = parseString( request.getParameter( "roleId" ) ) ;
        if( StringUtils.isNotBlank( roleId ) )
        {
            if( SecurityContext.isAuthenticated() )
            {
                PrincipalDetails userDetails = SecurityContext.getPrincipal() ;
                UserDetails newUserDetails = new PrincipalDetails(
                        userDetails.getId(),
                        //userDetails.getOrgId(),
                        userDetails.getUsername(),
                        userDetails.getPassword(),
                        userDetails.getChineseName(),
                        userDetails.getEnglishName(),
                        userDetails.getAdAccount(),
                        //roleId,
                        userDetails.isAccountNonExpired(),
                        userDetails.isAccountNonLocked(),
                        userDetails.isCredentialsNonExpired(),
                        userDetails.isEnabled(),
                        //userService.getFunctionAuthoritiesByUser( userDetails.getUsername(), roleId )
                        userService.getFunctionAuthoritiesByUser( userDetails.getId() )
                    ) ;
                //根据userDetails构建新的Authentication
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken( newUserDetails, "", newUserDetails.getAuthorities() ) ;
                // 设置authentication中details
                authentication.setDetails( new WebAuthenticationDetails( request ) ) ;
                SecurityContextHolder.getContext().setAuthentication( authentication ) ;
                HttpSession session = request.getSession( true ) ;
                // 在session中存放security context,方便同一个session中控制用户的其他操作
                session.setAttribute( HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext() ) ;
            }
        }
        getUserInfo( out ) ;
    }
    
    /**
  	 * 获取当前用可以查看或者回复的公告信息
  	 * 
  	 * @param out 响应输出对象
  	 */
  	@RequestMapping(params = "command=loadAllPublishedAnnouncements")
  	public void loadAllPublishedAnnouncements(java.io.PrintWriter out) 
  	{	
  		PagingResult<Map<String,Object>> pagingResult = userService.searchAnnouncementInfo(SecurityContext.getPrincipal().getId(),2, formatDate(new Date(), "yyyy-MM-dd"),null,null,null,null,null,null,1,16);
  		List<Map<String,Object>> list = pagingResult.getRows();
  		List<Map<String, String>> newList = new ArrayList<Map<String, String>>();
  		for (Map<String,Object> resultMap : list) {
  			Map<String, String> map = new HashMap<String, String>();
  			map.put("id", formatString(resultMap.get("id")));
  			map.put("title", formatString(resultMap.get("Title")));
  			map.put("publishDate", formatDate(parseDate(resultMap.get("PublishDate")),"yyyy-MM-dd HH:mm:ss"));
  			map.put("operator", formatString(resultMap.get("Operator")));
  			map.put("seqNums", formatString(resultMap.get("SeqNums")));
  			map.put("status", formatString(resultMap.get("Status")));
  			map.put("edit", formatString(resultMap.get("PublishPersonId")).equals(SecurityContext.getPrincipal().getId())?"ok":"no");
  			newList.add(map);
  		}
  		PagingResult<Map<String,String>> newPagingResult = new PagingResult<Map<String,String>>( newList, pagingResult.getPage(), pagingResult.getPageSize(), pagingResult.getRecords() ) ;
  	    out.print( getSerializer().formatObject( newPagingResult ) ) ;
  	}
  	
    @Autowired
    private PlatformUserService userService ;
    
}
