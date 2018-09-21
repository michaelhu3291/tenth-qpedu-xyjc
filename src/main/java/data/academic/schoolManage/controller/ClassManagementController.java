package data.academic.schoolManage.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import data.academic.examNumberManagement.service.ExamNumberManageService;
import data.academic.schoolManage.service.ClassManagementService;
import data.academic.studentManagement.service.StuManagementService;
import data.academic.util.ExportUtil;
import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractBaseController;
import data.framework.support.ConfigContext;
import data.platform.authority.security.SecurityContext;

/** 
* @ClassName: ClassManagementController 
* @Description: 班级管理控制层
* @author zhaohuanhuan
* @date 2017年1月12日
*  
*/
@RestController
@RequestMapping("/schoolManage/classManagement")
public class ClassManagementController extends AbstractBaseController{

	@Override
	protected void init(ModelMap model, HttpServletRequest request) {
		// TODO Auto-generated method stub
	}

	@RequestMapping(params="command=searchPaging")
	public void searchPaging(@RequestParam("data") String data,java.io.PrintWriter out){
		Map<String, Object> requestMap = getSerializer().parseMap(data);
	    String loginName1 = SecurityContext.getPrincipal().getUsername();
	    String schoolCode = examNumberManageService.getSchoolCodeByLoginName(loginName1);
	    int minClassNo=0;
	    int maxClassNo=0;
	    if(ConfigContext.getStringSection("syzxSchoolCode").equals(schoolCode)){
	    	minClassNo = 1;
			maxClassNo = 12;
	    }else if(ConfigContext.getStringSection("yczxSchoolCode").equals(schoolCode)){
	    	schoolCode=ConfigContext.getStringSection("syzxSchoolCode");
	    	minClassNo = 21;
			maxClassNo = 27;
	    }
	    requestMap.put("schoolCode",schoolCode);
	    requestMap.put("minClassNo", minClassNo);
	    requestMap.put("maxClassNo", maxClassNo);
		int currentPage = parseInteger(requestMap.get("page"));
		int pageSize = parseInteger(requestMap.get("rows"));
		String sortField = trimString(requestMap.get("sidx"));
		String sort = trimString(requestMap.get("sord"));
		//得到所有的教师集合
		PagingResult<Map<String, Object>> pagingResult = classManagementService.searchAllClassPaging(requestMap, sortField, sort, currentPage, pageSize);
		out.print(getSerializer().formatObject(pagingResult));
		}
	/**
	 * 
	 * @Title: searchPagingImport
	 * @Description: 导出
	 * @author jay zhong
	 * @date 2017年11月23日 下午1:46:16 
	 * @return void
	 *
	 * @param data
	 * @param response
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(params="command=importExcel")
	public void searchPagingImport(@RequestParam("data") String data,HttpServletResponse response){
		Map<String, Object> requestMap = getSerializer().parseMap(data);
	    String loginName1 = SecurityContext.getPrincipal().getUsername();
	    String schoolCode = examNumberManageService.getSchoolCodeByLoginName(loginName1);
	    int minClassNo=0;
	    int maxClassNo=0;
	    if(ConfigContext.getStringSection("syzxSchoolCode").equals(schoolCode)){
	    	minClassNo = 1;
			maxClassNo = 12;
	    }else if(ConfigContext.getStringSection("yczxSchoolCode").equals(schoolCode)){
	    	schoolCode=ConfigContext.getStringSection("syzxSchoolCode");
	    	minClassNo = 21;
			maxClassNo = 27;
	    }
	    requestMap.put("schoolCode",schoolCode);
	    requestMap.put("minClassNo", minClassNo);
	    requestMap.put("maxClassNo", maxClassNo);
		//得到所有的教师集合
		List<Map<String, Object>> list = classManagementService.searchAllClassPagingImport(requestMap);
		String fileName="班级列表.xls";
		String sheetName="班级列表详情";
		String[] title={"序号","班级名称","是否新疆班"};
		String[] key={"xh","Class_No","Is_Xjb"};
		for(Map<String,Object> map:list){
			map.put("Class_No",formatString(ExportUtil.CLASS_TYPE_MAP.get(map.get("Grade_No")))+formatString(ExportUtil.CLASS_NO_MAP.get(map.get("Class_No"))));
		}
		ExportUtil.ExportExcel(response, title, fileName, sheetName, list, key);
		
		}
	
	    /** 
	    * @Title: updateClassTypeByClassId 
	    * @Description: 修改班级是否为新疆班
	    * @param @param data
	    * @param @param out 
	    * @author zhaohuanhuan
	    * @return void   
	    * @throws 
	    */
		@RequestMapping(params="command=setAtXjb")
	    public void updateClassTypeByClassId(@RequestParam("data") String data,java.io.PrintWriter out){
	    	Map<String, Object> requestMap = getSerializer().parseMap(data);
	    	List<String> classIdList=(List<String>) requestMap.get("classIdList");
	    	Map<String, Object> paramMap=new HashMap<>();
	    	Map<String, Object> resultMap =new HashMap<>();
	    	paramMap.put("classIdPk", classIdList);
	    	paramMap.put("isXjb", "1");
	    	classManagementService.updateClassTypeByClassId(paramMap);
	    	resultMap.put("success", "true");
	    	out.print(getSerializer().formatObject(resultMap));
	    }
		
		
		
		/**
		 * @Title: cancelXjbClassTypeByClassId
		 * @Description: 取消新疆班
		 * @author zhaohuanhuan
		 * @return void
		 * @param data
		 * @param out
		 */
		@RequestMapping(params="command=cancelXjb")
	    public void cancelXjbClassTypeByClassId(@RequestParam("data") String data,java.io.PrintWriter out){
	    	Map<String, Object> requestMap = getSerializer().parseMap(data);
	    	List<String> classIdList=(List<String>) requestMap.get("classIdList");
	    	Map<String, Object> paramMap=new HashMap<>();
	    	Map<String, Object> resultMap =new HashMap<>();
	    	paramMap.put("classIdPk", classIdList);
	    	paramMap.put("isXjb", "0");
	    	classManagementService.updateClassTypeByClassId(paramMap);
	    	resultMap.put("success", "true");
	    	out.print(getSerializer().formatObject(resultMap));
	    }
	@Autowired
	private ExamNumberManageService examNumberManageService;
	@Autowired
	private ClassManagementService classManagementService;
}
