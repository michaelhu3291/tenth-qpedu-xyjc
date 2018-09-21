package data.academic.transfer.controller;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import data.academic.examInfo.service.ExamInfoService;
import data.academic.examNumberManagement.service.ExamNumberManageService;
import data.academic.transfer.service.StudentTransferService;
import data.academic.util.ExportUtil;
import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractBaseController;
import data.framework.support.ConfigContext;
import data.platform.authority.security.SecurityContext;

@RestController
@RequestMapping("transfer/stuTransHistory")
public class StuTransHistoryController extends AbstractBaseController{

	
	@Autowired
	private StudentTransferService studentTransferService;
	@Autowired
	private ExamInfoService examInfoService;
	@Autowired
	private ExamNumberManageService examNumberManageService;
	@Override
	protected void init(ModelMap model, HttpServletRequest request) {
				
	}

	@RequestMapping(params = "command=stuHistoryPage")
	public void searchStuHistoryPaging(@RequestParam("data") String data, PrintWriter out){
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		
		String name = trimString(requestMap.get("q"));
		requestMap.put("name", name);
		requestMap.put("state", ConfigContext.getValue("framework.tmis.transfer['studentHistory']"));
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
		PagingResult<Map<String, Object>> pagingResult = studentTransferService.searchStuHistoryPaging(requestMap, sortField, sort, currentPage, pageSize);
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
	
	/**
	 * 
	 * @Title: importStuHistoryPaging
	 * @Description: excel导出
	 * @author jay zhong
	 * @date 2017年11月21日 上午10:28:23 
	 * @return void
	 *
	 * @param data
	 * @param response
	 */
	@RequestMapping(params = "command=importExcel")
	public void importStuHistoryPaging(@RequestParam("data") String data, HttpServletResponse response){
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		
		String name = trimString(requestMap.get("q"));
		requestMap.put("name", name);
		String state=formatString(requestMap.get("state"));//1学生2老师
		if("1".equals(state)){
			requestMap.put("state", ConfigContext.getValue("framework.tmis.transfer['studentHistory']"));
		}else{
			requestMap.put("state", ConfigContext.getValue("framework.tmis.transfer['teacherHistory']"));
		}
		String userId=SecurityContext.getPrincipal().getId();
	    String roleCode=examInfoService.getRoleByUserId(userId);
	    String schoolCode="";
	    if(!ConfigContext.getValue("framework.tmis.roleCode['qpRoleAdminCode']").equals(roleCode)){
	    	String loginName=SecurityContext.getPrincipal().getUsername();
	       schoolCode=examNumberManageService.getSchoolCodeByLoginName(loginName);
	    }
	    requestMap.put("schoolCode", schoolCode);
		List<Map<String, Object>> list = studentTransferService.importStuHistoryPaging(requestMap);
		
		List<Map<String, Object>> paramList = new ArrayList<Map<String, Object>>();
		for(Map<String,Object> map : list){
			map.put("schoolName", map.get("School_Short_Name"));
			map.put("applyTime", formatDate(parseDate(map.get("Apply_Time")), "yyyy-MM-dd") );
			String direction = studentTransferService.getSchoolName(map);
			map.put("direction", direction);
			paramList.add(map);
		}
		String fileName="学生调动记录.xls";
		String sheetName="学生调动记录列表";
		String[] title={"序号","姓名","学校","申请人","去向","申请时间"};
		String[] key={"xh","Name","School_Short_Name","Proposer","direction","applyTime"};
		ExportUtil.ExportExcel(response, title, fileName, sheetName, paramList, key);
		
		
		
	}
}
