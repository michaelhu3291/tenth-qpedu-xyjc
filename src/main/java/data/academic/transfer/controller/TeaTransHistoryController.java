package data.academic.transfer.controller;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import data.academic.examInfo.service.ExamInfoService;
import data.academic.examNumberManagement.service.ExamNumberManageService;
import data.academic.transfer.service.StudentTransferService;
import data.academic.transfer.service.TeacherTransferService;
import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractBaseController;
import data.framework.support.ConfigContext;
import data.platform.authority.security.SecurityContext;

@RestController
@RequestMapping("transfer/teaTransHistory")
public class TeaTransHistoryController extends AbstractBaseController{

	@Autowired
	private StudentTransferService studentTransferService;
	@Autowired
	private TeacherTransferService teacherTransferService;
	@Autowired
	private ExamInfoService examInfoService;
	@Autowired
	private ExamNumberManageService examNumberManageService;
	@Override
	protected void init(ModelMap model, HttpServletRequest request) {
		
	}

	
	@RequestMapping(params = "command=teaHistoryPage")
	public void searchTeaHistoryPaging(@RequestParam("data") String data, PrintWriter out){
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		
		String name = trimString(requestMap.get("q"));
		requestMap.put("name", name);
		requestMap.put("state", ConfigContext.getValue("framework.tmis.transfer['teacherHistory']"));
		int currentPage = parseInteger(requestMap.get("page"));
		int pageSize = parseInteger(requestMap.get("rows"));
		String sortField = trimString(requestMap.get("sidx"));

		if (sortField.trim().length() == 0) {
			sortField = "Apply_Time";
		}
		String sort = trimString("desc");
		String userId=SecurityContext.getPrincipal().getId();
	    String roleCode=examInfoService.getRoleByUserId(userId);
	    String schoolCode="";
	    if(!ConfigContext.getValue("framework.tmis.roleCode['qpRoleAdminCode']").equals(roleCode)){
	    	String loginName=SecurityContext.getPrincipal().getUsername();
	       schoolCode=examNumberManageService.getSchoolCodeByLoginName(loginName);
	    }
	    requestMap.put("schoolCode", schoolCode);
		PagingResult<Map<String, Object>> pagingResult = teacherTransferService.searchTeaHistoryPaging(requestMap, sortField, sort, currentPage, pageSize);
		List<Map<String, Object>> list = pagingResult.getRows();
		List<Map<String, Object>> paramList = new ArrayList<Map<String, Object>>();
		for(Map<String,Object> map : list){
			map.put("schoolName", map.get("School_Short_Name"));
			map.put("applyTime", formatDate(parseDate(map.get("Apply_Time")), "yyyy-MM-dd") );
			String direction = studentTransferService.getSchoolName(map);
			map.put("direction", direction);
			paramList.add(map);
		}
		
		PagingResult<Map<String, Object>> newPagingResult = new PagingResult<Map<String, Object>>(
				paramList, pagingResult.getPage(), pagingResult.getPageSize(),pagingResult.getRecords());
		out.print(getSerializer().formatObject(newPagingResult));
		
	}
}
