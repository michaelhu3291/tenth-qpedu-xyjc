package data.academic.statisticsAnalysis.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import data.academic.examNumberManagement.service.ExamNumberManageService;
import data.academic.statisticsAnalysis.service.DistrictSubjectInstructorService;
import data.academic.teacherManagement.service.TeacherManagementService;
import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractBaseController;
import data.platform.authority.security.SecurityContext;

/**
 * @Title: DistrictSubjectInstructorController
 * @Description: 学科教研员控制层
 * @author zhaohuanhuan
 * @date 2016年9月14日 上午9:33:46
 */

@RestController
@RequestMapping("statisticsAnalysis/districtSubjectInstructor")
public class DistrictSubjectInstructorController extends AbstractBaseController{

	@Override
	protected void init(ModelMap model, HttpServletRequest request) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * @Title: searchPaging
	 * @Description: 科目教研员成绩分析显示
	 * @author zhaohuanhuan
	 * @date 2016年9月18日 
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=courseScoreSearch")
	public void searchPaging(@RequestParam("data") String data, java.io.PrintWriter out) {
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		// 加载页面时显示当前学年记录
		String time = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		int year = parseInteger(time.substring(0, time.indexOf("-")));
		int month = parseInteger(time.substring(time.indexOf("-") + 1, time.lastIndexOf("-")));
		String schoolYear = "";
		if (month < 9) {
			schoolYear = (year - 1) + "-" + year;
		} else {
			schoolYear = year + "-" + (year + 1);
		}
		if (null == requestMap.get("schoolYear")) {
			requestMap.put("schoolYear", schoolYear);
		}
		if ("".equals(requestMap.get("term") )|| null== requestMap.get("term") ) {
			requestMap.put("term", "xxq");
		}
		if ("".equals( requestMap.get("examType") ) || null== requestMap.get("examType")) {
			requestMap.put("examType", "qm");
		}
		int currentPage = parseInteger(requestMap.get("page"));
		int pageSize = parseInteger(requestMap.get("rows"));
		String sortField = trimString(requestMap.get("sidx"));
		if (sortField.trim().length() == 0) {
			sortField = "School_Code,Class_Id";
		}
		String sort = trimString(requestMap.get("sord"));
		PagingResult<Map<String, Object>> pagingResult = districtSubjectInstructorService.courseScoreSearch(requestMap, sortField, sort,
				currentPage, pageSize);
		List<Map<String, Object>> list = pagingResult.getRows();

		List<Map<String, Object>> paramList = new ArrayList<Map<String, Object>>();

		for (Map<String, Object> map : list) {
			String className="";
			String classId=formatString(map.get("Class_Id"));
			if(StringUtils.isNotBlank(classId) ){
				//处理班级
				className=classId.substring(classId.length()-2);
				String classIdOneStr=className.substring(0, 1);
				if(classIdOneStr.equals("0")){
					className=classId.substring(classId.length()-1);
				}
				map.put("Class_Id", className);
			}
			paramList.add(map);
		}
	PagingResult<Map<String, Object>> newPagingResult = new PagingResult<Map<String, Object>>(paramList,
				pagingResult.getPage(), pagingResult.getPageSize(), pagingResult.getRecords());
		out.print(getSerializer().formatObject(newPagingResult));
	}

	/**
	 * @Title: getSubjectInstrutorsAvg
	 * @Description: 学科平均分（教研员和学科教研员）
	 * @author zhaohuanhuan
	 * @date 2016年10月8日 
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=getSubjectInstrutorsAvg")
	public void getSubjectInstrutorsAvg(@RequestParam("data") String data,java.io.PrintWriter out) {
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		
		String course="yw";
		List<Map<String, Object>> cousreList=districtSubjectInstructorService.getCourseByLoginName(SecurityContext.getPrincipal().getUsername());
			for (Map<String, Object> map : cousreList) {
				if(map!=null){
					if(map.get("COUSRE")!=null){
						 course=map.get("COURSE").toString(); 
					}else{
						course=course;
						}
				
				}
				  
		}
	
		List<Map<String, Object>> list=new ArrayList<>();
		
		   Map<String, Object> paramMap = new HashMap<String, Object>();
		
				   if(requestMap.containsKey("schoolYear")){
					   paramMap.put("schoolYear",requestMap.get("schoolYear"));
					}else{
						String time = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		        		int year = parseInteger(time.substring(0, time.indexOf("-")));
		        		int month = parseInteger(time.substring(time.indexOf("-") + 1, time.lastIndexOf("-")));
		        		String schoolYear = "";
		        		if (month < 9) {
		        			schoolYear = (year - 1) + "-" + year;
		        		} else {
		        			schoolYear = year + "-" + (year + 1);
		        		}
		        	   paramMap.put("schoolYear", schoolYear);
					}
				   if(requestMap.containsKey("term")){
					   paramMap.put("term",requestMap.get("term"));
				   }else{
						paramMap.put("term", "xxq");
				   }
				   if(requestMap.containsKey("schoolType")){
					   paramMap.put("schoolType", requestMap.get("schoolType"));
				   }
				if(requestMap.containsKey("course")){
					course=requestMap.get("course").toString();
					  paramMap.put("course", course);
				}else{
					paramMap.put("course", course);
				}
					List<Map<String, Object>> avgList=districtSubjectInstructorService.getSubjectInstrutorsAvg(paramMap);
		            if(avgList.size()>0){
		            	for (Map<String, Object> map : avgList) {
			            	 map.put("School_Name", map.get("School_Name"));
			            	 map.put("qmAvg", map.get("qmAvg"));
			            	 map.put("qzAvg", map.get("qzAvg"));
			            	 list.add(map);
						}
		            
			}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("course", course);
		resultMap.put("data", list);
		out.print(getSerializer().formatMap(resultMap));
	}
	
	
	/**
	 * @Title: getCourseByLoginName
	 * @Description: 根据登录名得到相应学科
	 * @author zhaohuanhuan
	 * @date 2016年9月18日 
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=getCourseByLoginName")
	public void getCourseByLoginName(java.io.PrintWriter out) {
		List<Map<String, Object>> cousreList=districtSubjectInstructorService.getCourseByLoginName(SecurityContext.getPrincipal().getUsername());
		out.print(getSerializer().formatList(cousreList));
	}
	
	
	/**
	 * @Title: getSubjectInstrutorsSiLv
	 * @Description: 四率（教研员和学科教研员）
	 * @author zhaohuanhuan
	 * @date 2016年10月8日 
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=getSubjectInstrutorsSiLv")
	public void getSubjectInstrutorsSiLv(@RequestParam("data") String data,java.io.PrintWriter out) {
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		//登录名
		String loginName=SecurityContext.getPrincipal().getUsername();
		//根据登录名查询当前教研员所关联的年级
		List<Map<String, Object>> gradeList = districtSubjectInstructorService.selectGradesByLoginName(loginName);
		String state = null;
		if(requestMap.containsKey("state") && !requestMap.get("state").toString().equals("qb")){
			 state = requestMap.get("state").toString();
		} else {
			state = "qb";
		}
		//学籍状态集合
		List<String> stateCodeList = new ArrayList<>();
		String course="";
		//得到学科
		List<Map<String, Object>> cousreList=districtSubjectInstructorService.getCourseByLoginName(SecurityContext.getPrincipal().getUsername());
		course="yw";
		for (Map<String, Object> map : cousreList) {
			if(map!=null){
				if( map.get("COUSRE")!=null){
					 course=map.get("COURSE").toString(); 
				}else{
					course=course;					
					}
			}
		  
		}
		
		List<Map<String, Object>> list=new ArrayList<>();
		   Map<String, Object> paramMap = new HashMap<String, Object>();
		   //判断requestmap里面是否存在这些键
		           if(requestMap.containsKey("course") && !"".equals(requestMap.get("course").toString())){
		        	   course=requestMap.get("course").toString();
		        	   paramMap.put("course", course);
		           }else{
		        	   paramMap.put("course", course);
		           }
		           if(requestMap.containsKey("schoolYear")){
		        	   paramMap.put("schoolYear", requestMap.get("schoolYear"));
		           }else{
		        	   
		        		String time = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		        		int year = parseInteger(time.substring(0, time.indexOf("-")));
		        		int month = parseInteger(time.substring(time.indexOf("-") + 1, time.lastIndexOf("-")));
		        		String schoolYear = "";
		        		if (month < 9) {
		        			schoolYear = (year - 1) + "-" + year;
		        		} else {
		        			schoolYear = year + "-" + (year + 1);
		        		}
		        	   paramMap.put("schoolYear", schoolYear);
		           }
		           if(requestMap.containsKey("term")){
		        	   paramMap.put("term", requestMap.get("term").toString());
		           }else{
		        	   paramMap.put("term", "xxq");
		           }
		           if(requestMap.containsKey("examType")){
		        	   paramMap.put("examType", requestMap.get("examType").toString());
		           }else{
		        	   paramMap.put("examType", "qm");
		           }
		           
		           if(requestMap.containsKey("grade") && !"".equals(requestMap.get("grade").toString())){
		        	   paramMap.put("grade", requestMap.get("grade").toString());
		           }else{
		        	   if(gradeList != null && gradeList.size() > 0){
		        		   paramMap.put("grade", gradeList.get(0).get("Grade").toString());
		        	   }
		        	   
		           }
		           
		           if(requestMap.containsKey("state") && !requestMap.get("state").toString().equals("qb")){
		        	   if(state.equals("qb")){
		        		   stateCodeList.add("1");
		        		   stateCodeList.add("xjb");
		        		   stateCodeList.add("zd");
		        		   stateCodeList.add("4");
		        	   }
		        	   else if(state.equals("bx")){
		        		   stateCodeList.add("1");
		        		   stateCodeList.add("xjb");
		        		   stateCodeList.add("zd");
		        	   }
		        	   else if(state.equals("yx")){
		        		   stateCodeList.add("4");
		        	   }
		        	   else if(state.equals("sbjd")){
		        		   stateCodeList.add("1");
		        	   }
		        	   else if(state.equals("xjb")){
		        		   stateCodeList.add("xjb");
		        	   }
		           }else{
		        	   stateCodeList.add("1");
	        		   stateCodeList.add("xjb");
	        		   stateCodeList.add("zd");
	        		   stateCodeList.add("4");
		           }
		           StringBuffer sb = new StringBuffer();
					int i;
					for(i = 0;i < stateCodeList.size();i++){
						if(i == stateCodeList.size() - 1){
							sb.append(stateCodeList.get(i));
						} else {
							sb.append(stateCodeList.get(i)+",");
						}
					}
					//1,xjb,zd,4
					paramMap.put("state",sb.toString());
					paramMap.put("c",",");
					//paramMap.put("schoolType", requestMap.get("schoolType"));
					List<Map<String, Object>> siLvList=districtSubjectInstructorService.getSubjectInstrutorsSiLv(paramMap);
		            if(siLvList.size()>0){
		            	for (Map<String, Object> map : siLvList) {
			            	 map.put("School_Name", map.get("School_Name"));
			            	 map.put("Shcool_Code",map.get("School_Code"));
			            	 map.put("Yll", map.get(course+"_yll"));
						     map.put("Yl",map.get(course+"_yl"));
							 map.put("Ll",map.get(course+"_ll"));
							 map.put("Jgl", map.get(course+"_jgl"));
			            	 list.add(map);
						}
			}
				  
				
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("data", list);
			resultMap.put("course", course);
			resultMap.put("gradeList", gradeList);
			resultMap.put("grade", paramMap.get("grade").toString());
			resultMap.put("state", state);
			resultMap.put("schoolYear", paramMap.get("schoolYear").toString());
			resultMap.put("examType", paramMap.get("examType").toString());
			resultMap.put("term", paramMap.get("term").toString());
			out.print(getSerializer().formatMap(resultMap));
	}
	
	/**
	 * @Title: getSiLvForqpAdmin
	 * @Description: 青浦超级管理员四率查询
	 * @author xiahuajun
	 * @date 2016年10月28日 
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=getSiLvForqpAdmin")
	public void getSiLvForqpAdmin(@RequestParam("data") String data,java.io.PrintWriter out) {

		Map<String, Object> requestMap = getSerializer().parseMap(data);
		//登录名
		String state = null;
		if(requestMap.containsKey("state") && !requestMap.get("state").toString().equals("qb")){
			 state = requestMap.get("state").toString();
		} else {
			state = "qb";
		}
		//学籍状态集合
		List<String> stateCodeList = new ArrayList<>();
		String course="yw";
		List<Map<String, Object>> list=new ArrayList<>();
		   Map<String, Object> paramMap = new HashMap<String, Object>();
		   //判断requestmap里面是否存在这些键
		           if(requestMap.containsKey("course") && !"".equals(requestMap.get("course").toString())){
		        	   course=requestMap.get("course").toString();
		        	   paramMap.put("course", course);
		           }else{
		        	   paramMap.put("course", course);
		           }
		           if(requestMap.containsKey("schoolYear")){
		        	   paramMap.put("schoolYear", requestMap.get("schoolYear"));
		           }else{
		        	   
		        		String time = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		        		int year = parseInteger(time.substring(0, time.indexOf("-")));
		        		int month = parseInteger(time.substring(time.indexOf("-") + 1, time.lastIndexOf("-")));
		        		String schoolYear = "";
		        		if (month < 9) {
		        			schoolYear = (year - 1) + "-" + year;
		        		} else {
		        			schoolYear = year + "-" + (year + 1);
		        		}
		        	   paramMap.put("schoolYear", schoolYear);
		           }
		           if(requestMap.containsKey("term")){
		        	   paramMap.put("term", requestMap.get("term").toString());
		           }else{
		        	   paramMap.put("term", "xxq");
		           }
		           if(requestMap.containsKey("examType")){
		        	   paramMap.put("examType", requestMap.get("examType").toString());
		           }else{
		        	   paramMap.put("examType", "qm");
		           }
		           
		           if(requestMap.containsKey("grade") && !"".equals(requestMap.get("grade").toString())){
		        	   paramMap.put("grade", requestMap.get("grade").toString());
		           }else{
		        	   paramMap.put("grade", "16");
		           }
		           
		           if(requestMap.containsKey("state") && !requestMap.get("state").toString().equals("qb")){
		        	   if(state.equals("qb")){
		        		   stateCodeList.add("1");
		        		   stateCodeList.add("xjb");
		        		   stateCodeList.add("zd");
		        		   stateCodeList.add("4");
		        	   }
		        	   else if(state.equals("bx")){
		        		   stateCodeList.add("1");
		        		   stateCodeList.add("xjb");
		        		   stateCodeList.add("zd");
		        	   }
		        	   else if(state.equals("yx")){
		        		   stateCodeList.add("4");
		        	   }
		        	   else if(state.equals("sbjd")){
		        		   stateCodeList.add("1");
		        	   }
		        	   else if(state.equals("xjb")){
		        		   stateCodeList.add("xjb");
		        	   }
		           }else{
		        	   stateCodeList.add("1");
	        		   stateCodeList.add("xjb");
	        		   stateCodeList.add("zd");
	        		   stateCodeList.add("4");
		           }
		           StringBuffer sb = new StringBuffer();
					int i;
					for(i = 0;i < stateCodeList.size();i++){
						if(i == stateCodeList.size() - 1){
							sb.append(stateCodeList.get(i));
						} else {
							sb.append(stateCodeList.get(i)+",");
						}
					}
					//1,xjb,zd,4
					paramMap.put("state",sb.toString());
					paramMap.put("c",",");
					//paramMap.put("schoolType", requestMap.get("schoolType"));
					List<Map<String, Object>> siLvList=districtSubjectInstructorService.getSubjectInstrutorsSiLv(paramMap);
		            if(siLvList.size()>0){
		            	for (Map<String, Object> map : siLvList) {
			            	 map.put("School_Name", map.get("School_Name"));
			            	 map.put("Shcool_Code",map.get("School_Code"));
			            	 map.put("Yll", map.get(course+"_yll"));
						     map.put("Yl",map.get(course+"_yl"));
							 map.put("Ll",map.get(course+"_ll"));
							 map.put("Jgl", map.get(course+"_jgl"));
			            	 list.add(map);
						}
			}
				  
				
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("data", list);
			resultMap.put("course", course);
			resultMap.put("grade", paramMap.get("grade").toString());
			resultMap.put("state", state);
			resultMap.put("schoolYear", paramMap.get("schoolYear").toString());
			resultMap.put("examType", paramMap.get("examType").toString());
			resultMap.put("term", paramMap.get("term").toString());
			out.print(getSerializer().formatMap(resultMap));
	}
	@Autowired
	private DistrictSubjectInstructorService districtSubjectInstructorService;
	@Autowired
	private ExamNumberManageService examNumberManageService;
	@Autowired
	private TeacherManagementService teacherManagementService;
}
