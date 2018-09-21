package data.platform.controller;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import data.framework.support.AbstractBaseController;
import data.platform.authority.security.SecurityContext;
import data.platform.entity.EntityPlatformUser;
import data.platform.service.PlatformUserService;

@Controller
@RequestMapping("workFlow/FileManager/CommonForm")
public class CommonFormController extends AbstractBaseController {
    
    @Autowired
    private PlatformUserService userService;
    
    /**
     * 这个方法是得到当前用的用户处室和用户名e
     * @param request request对象
     * @param out 流出对象
     */
    @RequestMapping(params = "command=getUserNameAndDept")
    public void getUserNameAndDept(HttpServletRequest request, PrintWriter out){
        String userId = SecurityContext.getPrincipal().getId();
        EntityPlatformUser userInfo = userService.load(userId);
        String userName=userInfo.getChineseName();
        String dept=userInfo.getOrganization().getOrganizationName();
        String deptId = userInfo.getOrganizationId();
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("name",userName);
        map.put("dept",dept);
        map.put("personId",userId);
        map.put("deptId", deptId);
        out.print(this.getSerializer().formatMap(map));
    }

	@Override
	protected void init(ModelMap model, HttpServletRequest request) {
		// TODO Auto-generated method stub
		
	}
    
   
}
