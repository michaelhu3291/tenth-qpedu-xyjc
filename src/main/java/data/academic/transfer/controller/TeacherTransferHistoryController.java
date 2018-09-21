package data.academic.transfer.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import data.academic.teacherManagement.service.TeacherManagementService;
import data.framework.support.AbstractBaseController1;
import data.framework.support.ConfigContext;
import data.platform.authority.security.SecurityContext;

@RestController
@RequestMapping("transfer/teacherTransferHistory")
public class TeacherTransferHistoryController extends AbstractBaseController1{

	@Autowired
	private TeacherManagementService teacherManagementService;
	
	@RequestMapping
	protected ModelAndView init(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String loginName = SecurityContext.getPrincipal().getUsername();
		//根据登录名查询角色Code
		String roleCode = teacherManagementService.selectRoleCodeByLoginName(loginName);
		if(roleCode.equals(ConfigContext.getValue("framework.tmis.roleCode['qpRoleAdminCode']")) || roleCode.equals(ConfigContext.getValue("framework.tmis.roleCode['qpRoleQuAdminCode']"))) {
			request.getRequestDispatcher("/transfer/transferTeacher.do").forward(request,response);
		}
		if("schoolAdmin".equals(roleCode) || "schoolPlainAdmin".equals(roleCode)) {
			request.getRequestDispatcher("/schoolManage/teacherManage.do").forward(request,response);
		}
		return new ModelAndView();
	}
}
