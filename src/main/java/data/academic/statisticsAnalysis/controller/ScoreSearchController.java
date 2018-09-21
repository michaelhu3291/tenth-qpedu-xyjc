package data.academic.statisticsAnalysis.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import data.academic.examNumberManagement.service.ExamNumberManageService;
import data.academic.noticeManage.service.NoticeBasicInfoService;
import data.academic.schoolManage.service.PoliticalInstructorService;
import data.academic.schoolManage.service.TeacherManageService;
import data.academic.statisticsAnalysis.service.DistrictSubjectInstructorService;
import data.academic.statisticsAnalysis.service.ScoreSearchService;
import data.academic.teacherManagement.service.TeacherManagementService;
import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractBaseController1;
import data.framework.support.ConfigContext;
import data.platform.authority.security.SecurityContext;

/**
 * @Title: ScoreSearchController
 * @Description: 成绩查询控制层
 * @author zhaohuanhuan
 * @date 2016年8月16日
 */
@RestController
@RequestMapping("statisticsAnalysis/scoreSearch")
public class ScoreSearchController extends AbstractBaseController1 {

	//@Override
	/*protected void init(ModelMap model, HttpServletRequest request) {
		// 得到所有学校的集合
		List<Map<String, Object>> schoolList = scoreSearchService.getSchoolCode();
		model.put("schoolList", schoolList);
		// 得到科目名称
		List<Map<String, Object>> courseTypeList = scoreSearchService.getCourseName();
		model.put("courseTypeList", courseTypeList);
	}*/
	
	@RequestMapping
	protected ModelAndView initialize(ModelMap model, HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException{
		// 得到所有学校的集合
		List<Map<String, Object>> schoolList = scoreSearchService.getSchoolCode();
		model.put("schoolList", schoolList);
		// 得到科目名称
		List<Map<String, Object>> courseTypeList = scoreSearchService.getCourseName();
		model.put("courseTypeList", courseTypeList);
		
		String loginName = SecurityContext.getPrincipal().getUsername();
		//根据登录名查询角色Code
		String roleCode = teacherManagementService.selectRoleCodeByLoginName(loginName);
		if("schoolPlainAdmin".equals(roleCode) || "schoolAdmin".equals(roleCode)) {
			request.getRequestDispatcher("/statisticsAnalysis/schoolPlainAdminScoreSearch.do").forward(request,response);
		}
		else if(roleCode.equals(ConfigContext.getValue("framework.tmis.roleCode['qpRoleAdminCode']")) || roleCode.equals(ConfigContext.getValue("framework.tmis.roleCode['qpRoleQuAdminCode']"))) {
			request.getRequestDispatcher("/statisticsAnalysis/subTeaScoreSearch.do").forward(request,response);
			
		}
		else if("instructor".equals(roleCode)) {
			request.getRequestDispatcher("/statisticsAnalysis/instructorScoreSearch.do").forward(request,response);
			
		}
		
		return new ModelAndView();
		
	}

	/**
	 * @Title: getCoursesByCode
	 * @Description: 通过科目id关联查询科目
	 * @author zhaohuanhuan
	 * @date 2016年9月5日 
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=getCoursesByCourseId")
	public void getCoursesByCode(@RequestParam("data") String data, java.io.PrintWriter out) {
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		List<Map<String, Object>> list = scoreSearchService.getCoursesByCourseId(requestMap.get("courseId").toString());
		out.print(getSerializer().formatList(list));
	}

	/**
	 * @Title: searchPaging
	 * @Description: 老师分页查询成绩
	 * @author zhaohuanhuan
	 * @date 2016年8月16日
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=searchScore")
	public void searchPaging(@RequestParam("data") String data, java.io.PrintWriter out) {
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		// 加载页面时显示当前学年记录
		//schoolYear=2016-2017, course=sx, term=xxq, examType=qm, grade=[16, 17], class=[16,01, 16,02, 17,01]
		//根据登录名查询schoolCode
		//{_search=false, nd=1479797725279, rows=15, page=1, sidx=, sord=asc, schoolYear=2016-2017, grade=[16],
		//class=[16,01], course=yw, term=sxq, examType=qz, examNumberOrStuCode=, isFast=false, q=, o=}
		String loginName = SecurityContext.getPrincipal().getUsername();
		String schoolCode = examNumberManageService.getSchoolCodeByLoginName(loginName);
		String stuCode = trimString(requestMap.get("examNumberOrStuCode").toString());
		String examNumber = trimString(requestMap.get("examNumberOrStuCode").toString());
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
		String grade = null;
		@SuppressWarnings("unchecked")
		List<String> classList = (List<String>) requestMap.get("class");
		List<Map<String,Object>> list1 = new ArrayList<>();
		for(String str : classList) {
			Map<String,Object> map = new HashMap<String,Object>();
			String[] arr = str.split(",");
			map.put("grade", arr[0]);
			grade = arr[0];
			map.put("class", arr[1]);
			list1.add(map);
		}
		
		requestMap.put("schoolCode", schoolCode);
		requestMap.put("stuCode", stuCode);
		requestMap.put("examNumber", examNumber);
		requestMap.put("list1", list1);
		
		int currentPage = parseInteger(requestMap.get("page"));
		int pageSize = parseInteger(requestMap.get("rows"));
		String sortField = trimString(requestMap.get("sidx"));
		if (sortField.trim().length() == 0) {
			sortField = "Grade_Id,Class_Id";
		}
		String sort = trimString(requestMap.get("sord"));
		PagingResult<Map<String, Object>> pagingResult = scoreSearchService.ScoreSearch(requestMap, sortField, sort,
				currentPage, pageSize);
		List<Map<String, Object>> list = pagingResult.getRows();
		
		PagingResult<Map<String, Object>> newPagingResult = new PagingResult<Map<String, Object>>(list,
				pagingResult.getPage(), pagingResult.getPageSize(), pagingResult.getRecords());
		out.print(getSerializer().formatObject(newPagingResult));
	}
	
	/**
	 * @Title: selectGradeForTeacher
	 * @Description: 查询老师所带的年级
	 * @author xiahuajun
	 * @date 2016年10月21日 
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=selectGradeForTeacher")
	public void selectGradeForTeacher(@RequestParam("data") String data, java.io.PrintWriter out) {
		//Map<String, Object> requestMap = getSerializer().parseMap(data);
		String loginName = SecurityContext.getPrincipal().getUsername();
		//当前年
        String time = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		String currentYear = time.substring(0, time.indexOf("-"));
		//根据登录名查询teacher_pk
		String teacher_pk = teacherManageService.selectTeacherPkByLoginName(loginName);
		//查询老师所带的科目
		List<Map<String,String>> courseList = teacherManagementService.getCourseByTeaIdAndYear(teacher_pk, currentYear);
		
		//查询老师所带的年级
		List<Map<String,String>> list = teacherManagementService.getClassByTeaPk(teacher_pk,currentYear);
		List<String> gradeList = new ArrayList<String>();
		if(list != null && list.size() > 0) {
			String firstGradeVal = list.get(0).get("Grade_Id");
			gradeList.add(firstGradeVal);
			for(int i = 1;i < list.size();i++) {
				if(! list.get(i).get("Grade_Id").equals(firstGradeVal)) {
					gradeList.add(list.get(i).get("Grade_Id"));
				}
			}
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("gradeList", gradeList);
		map.put("courseList", courseList);
		out.print(getSerializer().formatMap(map));
	}
	
	/**
	 * @Title: selectClassByGrade
	 * @Description: 选择年级关联班级
	 * @author xiahuajun
	 * @date 2016年10月22日 
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=selectClassByGrade")
	public void selectClassByGrade(@RequestParam("data") String data, java.io.PrintWriter out) {
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		String loginName = SecurityContext.getPrincipal().getUsername();
		//根据登录名查询teacher_pk
		String teacher_pk = teacherManageService.selectTeacherPkByLoginName(loginName);
		//当前年
        String time = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		String currentYear = time.substring(0, time.indexOf("-"));
		requestMap.put("teacherPk", teacher_pk);
		requestMap.put("currentYear", currentYear);
		//查询老师所带的班级
		List<Map<String,String>> classList = teacherManagementService.selectClassByGrade(requestMap);
		out.print(getSerializer().formatList(classList));
	}
	
	/**
	 * @Title: getSubjectInstrutorsSiLv
	 * @Description: 老师查询四率
	 * @author xiahuajun
	 * @date 2016年10月24日 
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=getSiLvForTeacher")
	public void getSubjectInstrutorsSiLv(@RequestParam("data") String data,java.io.PrintWriter out) {
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		String state = null;
		if(requestMap.containsKey("state")){
			 state = requestMap.get("state").toString();
		} else {
			state = "qb";
		}
		
		String loginName = SecurityContext.getPrincipal().getUsername();
		//当前年
        String time = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		String currentYear = time.substring(0, time.indexOf("-"));
		//根据登录名查询teacher_pk
		String teacher_pk = teacherManageService.selectTeacherPkByLoginName(loginName);
		//查询老师所带的科目
		List<Map<String,String>> courseList = teacherManagementService.getCourseByTeaIdAndYear(teacher_pk, currentYear);
		String course = null;
		 if(requestMap.containsKey("course")){
			 course = requestMap.get("course").toString();
         }else{
        	 if (!courseList.isEmpty()) {
        		 course = courseList.get(0).get("Course_Id");
			}
         }
		//查询老师所带的年级
		List<Map<String,String>> list1 = teacherManagementService.getClassByTeaPk(teacher_pk,currentYear);
		List<String> gradeList = new ArrayList<String>();
		//过滤掉重复的年级
		if(list1 != null && list1.size() > 0) {
			String firstGradeVal = list1.get(0).get("Grade_Id");
			gradeList.add(firstGradeVal);
			for(int i = 1;i < list1.size();i++) {
				if(! list1.get(i).get("Grade_Id").equals(firstGradeVal)) {
					gradeList.add(list1.get(i).get("Grade_Id"));
				}
			}
		}
		//学籍状态集合
		List<String> stateCodeList = new ArrayList<>();
		   Map<String, Object> paramMap = new HashMap<String, Object>();
		   //判断requestmap里面是否存在这些键
				   if(requestMap.containsKey("state")){
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
		           if(requestMap.containsKey("schoolYear")){
		        	   paramMap.put("schoolYear", requestMap.get("schoolYear").toString());
		           }else{
		        	   
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
		        	   paramMap.put("term", requestMap.get("term"));
		           }else{
		        	   paramMap.put("term", "xxq");
		           }
		           if(requestMap.containsKey("examType")){
		        	   paramMap.put("examType", requestMap.get("examType"));
		           }else{
		        	   paramMap.put("examType", "qm");
		           }
		            //根据登录名查询schoolCode
					String schoolCode = examNumberManageService.getSchoolCodeByLoginName(loginName);
					if (requestMap.containsKey("gradeId")) {
						paramMap.put("gradeId", formatString(requestMap.get("gradeId")));
					} else {
						if (!gradeList.isEmpty()) {
							paramMap.put("gradeId", gradeList.get(0));
						}
					}
					paramMap.put("course", course);
					paramMap.put("school", schoolCode);
					
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
					paramMap.put("str",sb.toString());
					paramMap.put("c",",");
					StringBuffer schoolSb=new StringBuffer();
					int minClassNo=0;
					int maxClassNo=0;
					if(ConfigContext.getStringSection("yczxSchoolCode").equals(schoolCode)){
						schoolSb.append(schoolCode).append(",").append(ConfigContext.getStringSection("syzxSchoolCode"));
						minClassNo=21;
						maxClassNo=27;
						paramMap.put("school", schoolSb.toString());
					}
					if(ConfigContext.getStringSection("syzxSchoolCode").equals(schoolCode)){
						minClassNo=1;
						maxClassNo=12;
					}
					paramMap.put("minClassNo",minClassNo);
					paramMap.put("maxClassNo",maxClassNo);
					List<Map<String, Object>> siLvList = districtSubjectInstructorService.getSubjectPoliticalInstrutorsSiLv(paramMap);
					List<Map<String, Object>> list = new ArrayList<>();
					if(siLvList.size()>0){
		            	for (Map<String, Object> map : siLvList) {
		            		Map<String,Object> map1 = new HashMap<String,Object>();
		            			 map1.put("classId", map.get("Class_Id"));
				            	 map1.put("Yll", map.get(course+"_yll"));
							     map1.put("Yl",map.get(course+"_yl"));
								 map1.put("Ll",map.get(course+"_ll"));
								 map1.put("Jgl", map.get(course+"_jkl"));
				            	 list.add(map1);
		            			
		            		}
			            	 
						}
			
				
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("data", list);
			resultMap.put("course", formatString(course));
			resultMap.put("gradeId",formatString(paramMap.get("gradeId")));
			resultMap.put("schoolYear",formatString(paramMap.get("schoolYear")));
			resultMap.put("term",formatString(paramMap.get("term")));
			resultMap.put("examType",formatString(paramMap.get("examType")));
			resultMap.put("state",state);
			out.print(getSerializer().formatMap(resultMap));
	}
	
	/**
	 * @Title: selectGradesForInstructor
	 * @Description: 根据登录名查询当前教研员所关联的年级
	 * @author xiahuajun
	 * @date 2016年10月27日 
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=selectGradesForInstructor")
	public void selectGradesForInstructor(@RequestParam("data") String data, java.io.PrintWriter out) {
//		Map<String, Object> requestMap = getSerializer().parseMap(data);
		String loginName = SecurityContext.getPrincipal().getUsername();
		//根据登录名查询当前教研员所关联的年级
		List<Map<String, Object>> gradeList = districtSubjectInstructorService.selectGradesByLoginName(loginName);  
		out.print(getSerializer().formatList(gradeList));
	}
	
	/**
	 * @Title: selectGradesForInstructor
	 * @Description: 选择年级关联学校
	 * @author xiahuajun
	 * @date 2016年10月27日 
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=getSchoolsByGrade")
	public void getSchoolsByGrade(@RequestParam("data") String data, java.io.PrintWriter out) {
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		String grade = requestMap.get("grade").toString();
		int bCode=0;
		//String schoolType = null;
		List<String> schoolTypeList = new ArrayList<>();
		Map<String,Object> map = new HashMap<String,Object>();
		boolean flag = false;
		if("11".equals(grade) || "12".equals(grade) || "13".equals(grade) || "14".equals(grade) || "15".equals(grade)){
			schoolTypeList.add("0");
			schoolTypeList.add("5");
		}
		else if("16".equals(grade) || "17".equals(grade) || "18".equals(grade) || "19".equals(grade)){
			schoolTypeList.add("1");
			schoolTypeList.add("5");
			bCode=30;
			flag = true;
		}
		else if("31".equals(grade) || "32".equals(grade) || "33".equals(grade)){
			schoolTypeList.add("2");
			schoolTypeList.add("3");
			schoolTypeList.add("4");
		}
		map.put("schoolTypeList", schoolTypeList);
		//String loginName = SecurityContext.getPrincipal().getUsername();
		//查询关联年级所属的学校
		List<Map<String, Object>> schoolList = null;
		if(flag){
			schoolList = scoreSearchService.getSchoolsByGrade(map);
			Map<String,Object> paramMap = new HashMap<String,Object>();
				schoolList.remove("3004");
				paramMap.put("Brevity_Code", bCode);
				paramMap.put("School_Code", "3004");
				paramMap.put("School_Name", "上海市青浦区第一中学");
				paramMap.put("School_Short_Name", "青浦一中");
				paramMap.put("Brevity_Code", bCode);
		    	schoolList.add(paramMap);
		} else {
			schoolList = scoreSearchService.getSchoolsByGrade(map);
		}
		out.print(getSerializer().formatList(schoolList));
	}
	
	/**
	 * @Title: searchPaging
	 * @Description: 教研员分页查询成绩
	 * @author xiahuajun
	 * @date 2016年10月27日
	 * @param data
	 * @param out
	 * @return void
	 */
	//@RequestMapping(params = "command=searchScoreForInstructor")
	/*public void searchScoreForInstructor(@RequestParam("data") String data, java.io.PrintWriter out) {
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		//schoolYear=2016-2017, term=xxq, examType=qm, grade=16, school=3071
		int currentPage = parseInteger(requestMap.get("page"));
		int pageSize = parseInteger(requestMap.get("rows"));
		String stuCode = trimString(requestMap.get("examNumberOrStuCode").toString());
		String examNumber = trimString(requestMap.get("examNumberOrStuCode").toString());
		requestMap.put("stuCode", stuCode);
		requestMap.put("examNumber", examNumber);
		String sortField = trimString(requestMap.get("sidx"));
		if (sortField.trim().length() == 0) {
			sortField = "School_Code,Grade_Id,Class_Id";
		}
		String sort = trimString(requestMap.get("sord"));
		PagingResult<Map<String, Object>> pagingResult = scoreSearchService.searchScoreForInstructor(requestMap, sortField, sort,
				currentPage, pageSize);
		List<Map<String, Object>> list = pagingResult.getRows();

		
		PagingResult<Map<String, Object>> newPagingResult = new PagingResult<Map<String, Object>>(list,
				pagingResult.getPage(), pagingResult.getPageSize(), pagingResult.getRecords());
		out.print(getSerializer().formatObject(newPagingResult));
	}*/
	
	/**
	 * @Title: searchScoreForInstructor
	 * @Description: 青浦教育局加载所有学校
	 * @author xiahuajun
	 * @date 2016年10月27日 
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=getAllSchoolsByForqpAdmin")
	public void getAllSchoolsByForqpAdmin(@RequestParam("data") String data, java.io.PrintWriter out) {
		//Map<String, Object> requestMap = getSerializer().parseMap(data);
		List<Map<String, Object>> schoolList = scoreSearchService.getAllSchoolsByForqpAdmin();
		out.print(getSerializer().formatList(schoolList));
	}
	
	
	
	/**
	 * @Title: searchScoreForInstructor
	 * @Description: 青浦超级管理员和教研员分页成绩查询
	 * @author xiahuajun
	 * @date 2016年10月27日 
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=searchScoreForqpAdmin")
	public void searchScoreForqpAdmin(@RequestParam("data") String data, java.io.PrintWriter out) {
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		String grade = requestMap.get("grade").toString();
		String loginName=SecurityContext.getPrincipal().getUsername();
		String schoolCode = null;//学校编号
		String roleType = null; //登录角色类型
		String examCode1 = null;//考试编号
		//根据登录名查询角色Code
		String roleCode = teacherManagementService.selectRoleCodeByLoginName(loginName);
		if("schoolPlainAdmin".equals(roleCode) || "schoolAdmin".equals(roleCode)) {//校级
			schoolCode = examNumberManageService.getSchoolCodeByLoginName(loginName);
			roleType = "school";
			examCode1 = trimString(requestMap.get("examNumber").toString());
		}else{//区级
			roleType = "district";
		}
		String schoolType = null; //学校类型
		
		if("11".equals(grade) || "12".equals(grade) || "13".equals(grade) || "14".equals(grade) || "15".equals(grade)){
			schoolType = "xx";
		}
		else if("16".equals(grade) || "17".equals(grade) || "18".equals(grade) || "19".equals(grade)){
			schoolType = "cz";
		}
		else if("31".equals(grade) || "32".equals(grade) || "33".equals(grade)){
			schoolType = "gz";
		}
		int currentPage = parseInteger(requestMap.get("page"));
		int pageSize = parseInteger(requestMap.get("rows"));
		String sortField = trimString(requestMap.get("sidx"));
		if (sortField.trim().length() == 0) {
			sortField = "School_Code,Grade_Id,Class_Id";
		}
		String stuCode = trimString(requestMap.get("examNumberOrStuCode").toString());
		String examNumber = trimString(requestMap.get("examNumberOrStuCode").toString());
		
		requestMap.put("stuCode", stuCode);
		requestMap.put("examNumber", examNumber);
		requestMap.put("examCode", examCode1);
		//学校类型
		requestMap.put("schoolType", schoolType);
		requestMap.put("schoolCode", schoolCode);
		List<String> schoolList=new ArrayList<>();
		schoolList.add(schoolCode);
		int minClassNo=0;
		int maxClassNo=0;
		if(ConfigContext.getStringSection("yczxSchoolCode").equals(schoolCode)){
			schoolList.add(ConfigContext.getStringSection("syzxSchoolCode"));
			minClassNo=21;
			maxClassNo=27;
		}else if(ConfigContext.getStringSection("syzxSchoolCode").equals(schoolCode)){
			minClassNo=1;
			maxClassNo=12;
		}
		requestMap.put("schoolList", schoolList);
		requestMap.put("minClassNo", minClassNo);
		requestMap.put("maxClassNo", maxClassNo);
		//科目拼接串
		//requestMap.put("courseStr", sb.toString());
		//requestMap.put(""+ ""+ "", courseArr);
						
		
		String sort = trimString(requestMap.get("sord"));
		PagingResult<Map<String, Object>> pagingResult = scoreSearchService.searchScoreForqpAdmin(requestMap, sortField, sort,
				currentPage, pageSize);
		List<Map<String, Object>> list = pagingResult.getRows();

				
		PagingResult<Map<String, Object>> newPagingResult = new PagingResult<Map<String, Object>>(list,
				pagingResult.getPage(), pagingResult.getPageSize(), pagingResult.getRecords());
		out.print(getSerializer().formatObject(newPagingResult));
	}
	
	/**
	 * @Title: exportExcel
	 * @Description: 老师导出成绩查询数据
	 * @author xiahuajun
	 * @date 2016年11月4日 
	 * @param request
	 * @param response
	 * @throws UnsupportedEncodingException
	 * @return void
	 */
	@RequestMapping(params="command=exportExcel")
	public void exportExcel(HttpServletRequest request,HttpServletResponse response ) throws UnsupportedEncodingException{
	    String datas=request.getParameter("data");
    	Map<String,Object> requestMap = this.getSerializer().parseMap(datas) ;
    	
    	//根据登录名查询角色Code
        //String roleCode = teacherManagementService.selectRoleCodeByLoginName(loginName);
    	
    	//根据登录名查询schoolCode
    			String loginName = SecurityContext.getPrincipal().getUsername();
    			String schoolCode = examNumberManageService.getSchoolCodeByLoginName(loginName);
    			String stuCode = trimString(requestMap.get("examNumberOrStuCode").toString());
    			String examNumber = trimString(requestMap.get("examNumberOrStuCode").toString());
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
    			String grade = null;
    			
    			@SuppressWarnings("unchecked")
    			List<String> classList = (List<String>) requestMap.get("classArr");
    			List<Map<String,Object>> list1 = new ArrayList<>();
    			for(String str : classList) {
    				Map<String,Object> map = new HashMap<String,Object>();
    				String[] arr = str.split(",");
    				map.put("grade", arr[0]);
    				grade = arr[0];
    				map.put("class", arr[1]);
    				list1.add(map);
    			}
    			String index=trimString(requestMap.get("idx").toString());
    	    	String columnIndex=trimString(requestMap.get("ci").toString());
    	    	String sortOrder=trimString(requestMap.get("so").toString());
    	    	//System.out.println("====index===="+index);
    	    	//System.out.println("====columnIndex===="+columnIndex);
    	    	//System.out.println("====sortOrder===="+sortOrder);
    			requestMap.put("schoolCode", schoolCode);
    			requestMap.put("stuCode", stuCode);
    			requestMap.put("examNumber", examNumber);
    			requestMap.put("list1", list1);
    			requestMap.put("examNumber", examNumber);
    			requestMap.put("index", index);
    			requestMap.put("sortOrder", sortOrder);
    			//System.out.println("====requestMap===="+requestMap);
    			/*int currentPage = parseInteger(requestMap.get("page"));
    			int pageSize = parseInteger(requestMap.get("rows"));
    			String sortField = trimString(requestMap.get("sidx"));
    			if (sortField.trim().length() == 0) {
    				sortField = "Grade_Id,Class_Id";
    			}
    			String sort = trimString(requestMap.get("sord"));*/
    			//老师查询成绩导出excel
    			List<Map<String, Object>>  paramList = scoreSearchService.selectExportScoreDataForTeacher(requestMap);
    			

    			String fileDisplay=requestMap.get("scoreHtml").toString()+".xls";
    			//System.out.println("====fileDisplay===="+fileDisplay);
    	    	try{
    				response.setContentType("application/x-download");
    				fileDisplay = URLEncoder.encode(fileDisplay, "utf-8");
    		        response.addHeader("Content-Disposition", "attachment;filename="+ fileDisplay);
    		     //创建一个webbook，对应一个Excel文件  
    		        HSSFWorkbook workbook = new HSSFWorkbook();
    		        //在webbook中添加一个sheet,对应Excel文件中的sheet  
    		        HSSFSheet sheet = workbook.createSheet();
    		        //在sheet中添加表头第0行
    		        workbook.setSheetName(0,"导出成绩数据");
    		        
    		        sheet.setColumnWidth((short)0,(short)2000);  
    		        sheet.setColumnWidth((short)1,(short)6000);  
    		        sheet.setColumnWidth((short)2,(short)6000);  
    		        sheet.setColumnWidth((short)3,(short)3000);
    		        sheet.setColumnWidth((short)4,(short)3000);
    		        sheet.setColumnWidth((short)5,(short)3000);
    		        sheet.setColumnWidth((short)6,(short)3000);
    		        sheet.setColumnWidth((short)7,(short)3000);
    		      //，创建单元格，并设置值表头 设置表头居中  
    		        HSSFCellStyle titleStyle = workbook.createCellStyle();  
    		        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式  
    		        
    		        HSSFFont
    		        font = workbook.createFont();    
    		        font.setFontName("宋体");  
    		        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
    		        font.setFontHeightInPoints((short) 11);//设置字体大小  
    		        titleStyle.setFont(font);
    		        
    		        //总汇字体
    		        HSSFCellStyle countStyle = workbook.createCellStyle();  
    		        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式  
    		        HSSFFont countFont = workbook.createFont();    
    		        countFont.setFontName("宋体");  
    		        countFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
    		        countFont.setFontHeightInPoints((short)14);//设置字体大小  
    		        countFont.setColor(HSSFColor.RED.index);//设置字体颜色
    		        countStyle.setFont(countFont);
    		        
    		        
    		        //学生信息字体
    		        HSSFCellStyle style = workbook.createCellStyle();  
    		        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); 
    		        //表头
    		        String[] titleAry=new String[]{"序号","考号","学籍号","姓名","年级","班级","总分"};
    		        HSSFRow row = sheet.createRow((short) 0);
    		        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, (titleAry.length-1)));    
    		        HSSFCell cellTiltle = row.createCell(0);  
    		        //总汇信息
//    	            cellTiltle.setCellValue("学校总人数："+isTestCaseGeneration+"人,已分配考号"+
//    		                                              testCaseGeneration+"人,未分配考号"+
//    	            		                              notTestCaseGeneration+"人"+",沪籍学生0人,"); 
    		        row.setHeight((short)350);
    		        cellTiltle.setCellStyle(countStyle);
    		        String tit=requestMap.get("scoreHtml").toString();
    		        cellTiltle.setCellValue(tit);
    		        
    		        row = sheet.createRow((short) 1);
    		        
    		        //设置title
    		        for(int i=0;i<titleAry.length;i++)
    		        {
    		        	HSSFCell cell=row.createCell((short)i);
    		        	cell.setCellStyle(titleStyle);
    		        	cell.setCellType(HSSFCell.CELL_TYPE_STRING);
    		            cell.setCellValue(titleAry[i]);  
    		        }
    		        
    		        //写入实体数据
    		       int j=1;
    		        for(int i=0;i<paramList.size();i++)
    		        {
    		        	Map<String,Object> paramMap =paramList.get(i);
    		        	row=sheet.createRow((short) j+ 1);
    		        	j++;
    		        	HSSFCell cell=row.createCell((short)0);
    		        	cell.setCellStyle(style);
    		        	cell.setCellValue((i+1)); 
    		        	
    		          	HSSFCell cellOne=row.createCell((short)1);
    		        	cellOne.setCellStyle(style);
    		        	cellOne.setCellValue(parseString(paramMap.get("Exam_Number"))); 
    		        	
    		        	
    		        	HSSFCell cellTwo=row.createCell((short)2);
    		        	cellTwo.setCellStyle(style);
    		        	cellTwo.setCellValue(parseString(paramMap.get("XJFH"))); 
    		        	
    		        	HSSFCell cellThree=row.createCell((short)3);
    		        	cellThree.setCellStyle(style);
    		        	cellThree.setCellValue(parseString(paramMap.get("Name"))); 
    		        	
    		        	HSSFCell cellFour=row.createCell((short)4);
    		        	cellFour.setCellStyle(style);
    		        	String Grade_Id = "";
    		        	if("11".equals(paramMap.get("Grade_Id").toString())){
    						Grade_Id = "一年级";
    					}
    					else if("12".equals(paramMap.get("Grade_Id").toString())){
    						Grade_Id = "二年级";
    					}
    					else if("13".equals(paramMap.get("Grade_Id").toString())){
    						Grade_Id = "三年级";
    					}
    					else if("14".equals(paramMap.get("Grade_Id").toString())){
    						Grade_Id = "四年级";
    					}
    					else if("15".equals(paramMap.get("Grade_Id").toString())){
    						Grade_Id = "五年级";
    					}
    					else if("16".equals(paramMap.get("Grade_Id").toString())){
    						Grade_Id = "六年级";
    					}
    					else if("17".equals(paramMap.get("Grade_Id").toString())){
    						Grade_Id = "七年级";
    					}
    					else if("18".equals(paramMap.get("Grade_Id").toString())){
    						Grade_Id = "八年级";
    					}
    					else if("19".equals(paramMap.get("Grade_Id").toString())){
    						Grade_Id = "九年级";
    					}
    					else if("31".equals(paramMap.get("Grade_Id").toString())){
    						Grade_Id = "高一年级";
    					}
    					else if("32".equals(paramMap.get("Grade_Id").toString())){
    						Grade_Id = "高二年级";
    					}
    					else if("33".equals(paramMap.get("Grade_Id").toString())){
    						Grade_Id = "高三年级";
    					}
    		        	
    		        	cellFour.setCellValue(Grade_Id); 
    		        	HSSFCell cellFive=row.createCell((short)5);
    		        	cellFive.setCellStyle(style);
    		        	String Class_Id = "";
    		        	if("01".equals(paramMap.get("Class_Id").toString())){
    		        		Class_Id = "（1）班";
    					}
    					else if("02".equals(paramMap.get("Class_Id").toString())){
    						Class_Id = "（2）班";
    					}
    					else if("03".equals(paramMap.get("Class_Id").toString())){
    						Class_Id = "（3）班";
    					}
    					else if("04".equals(paramMap.get("Class_Id").toString())){
    						Class_Id = "（4）班";
    					}
    					else if("05".equals(paramMap.get("Class_Id").toString())){
    						Class_Id = "（5）班";
    					}
    					else if("06".equals(paramMap.get("Class_Id").toString())){
    						Class_Id = "（6）班";
    					}
    					else if("07".equals(paramMap.get("Class_Id").toString())){
    						Class_Id = "（7）班";
    					}
    					else if("08".equals(paramMap.get("Class_Id").toString())){
    						Class_Id = "（8）班";
    					}
    					else if("09".equals(paramMap.get("Class_Id").toString())){
    						Class_Id = "（9）班";
    					}
    					else if("10".equals(paramMap.get("Class_Id").toString())){
    						Class_Id = "（10）班";
    					}
    					else if("11".equals(paramMap.get("Grade_Id").toString())){
    						Class_Id = "（11）班";
    					}
    					else if("12".equals(paramMap.get("Class_Id").toString())){
    						Class_Id = "（12）班";
    					}
    					else if("13".equals(paramMap.get("Class_Id").toString())){
    						Class_Id = "（13）班";
    					}
    					else if("14".equals(paramMap.get("Class_Id").toString())){
    						Class_Id = "（14）班";
    					}
    					else if("15".equals(paramMap.get("Grade_Id").toString())){
    						Class_Id = "（15）班";
    					}
    		        	
    		        	cellFive.setCellValue(Class_Id); 
    		            		        	
    		        	HSSFCell cellSix=row.createCell((short)6);
    		        	cellSix.setCellStyle(style);
    		        	cellSix.setCellValue(parseString(paramMap.get("Total_Score"))); 
    		        	
    		        	
    		        
//    		        	HSSFCell cellSeven=row.createCell((short)7);
//    		        	cellSeven.setCellStyle(style);
//    		        	String isSbjd="";
//    		        	if("1".equals(parseString(paramMap.get("STATE_CODE")))){
//    		        		isSbjd="是";
//    		        	}else{
//    		        		isSbjd="否";
//    		        	}
//    		        	cellSeven.setCellValue(isSbjd); 
    		        }
    		        OutputStream out=response.getOutputStream();
    				workbook.write(out);
    		        out.flush();
    		        out.close();
    			}catch(Exception e){
    				System.out.println(e);
    			}
	}
	
	/**
	 * @Title: getCoursesByCode
	 * @Description: 通过学校类型code关联科目
	 * @author xiahuajun
	 * @date 2s016年8月8日 
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=getCoursesByCode")
	public void getCoursesByGrade(@RequestParam("data") String data,java.io.PrintWriter out){
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		//Map<String, Object> paramMap = new HashMap<String, Object>();
		//System.out.println("====schoolYear===="+requestMap.get("schoolYear"));
		//System.out.println("====grade===="+requestMap.get("grade"));
		//System.out.println("====term===="+requestMap.get("term"));
		//System.out.println("====examType===="+requestMap.get("examType"));
		String loginName = SecurityContext.getPrincipal().getUsername();
		String orgId = politicalInstructorService.selectOrgIdByLoginName(loginName);    //根据USER_UID获取组织id
		Map<String,Object> param = new HashMap();
		param.put("orgId", orgId);
		String orgCode = noticeBasicInfoService1.getOrgCode(param);    //组织code 
		requestMap.put("schoolCode", orgCode);
		//System.out.println("====schoolCode===="+orgCode);
		List<Map<String,Object>> list = scoreSearchService.getCoursesByGrade(requestMap);
		//System.out.println("=====list===="+list);
		out.print(getSerializer().formatList(list));
	}
	/**
	 * @Title: getExamNameByCode
	 * @Description: 通过学校类型code关联科目
	 * @author huchuanhu
	 * @date 2016年11月17日 
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=getExamNameByCode")
	public void getExamNameByCode(@RequestParam("data") String data,java.io.PrintWriter out){
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		//Map<String, Object> paramMap = new HashMap<String, Object>();
		//System.out.println("====schoolYear===="+requestMap.get("schoolYear"));
		//System.out.println("====grade===="+requestMap.get("grade"));
		//System.out.println("====term===="+requestMap.get("term"));
		//System.out.println("====examType===="+requestMap.get("examType"));
		String loginName = SecurityContext.getPrincipal().getUsername();
		String orgId = politicalInstructorService.selectOrgIdByLoginName(loginName);    //根据USER_UID获取组织id
		Map<String,Object> param = new HashMap();
		param.put("orgId", orgId);
		String orgCode = noticeBasicInfoService1.getOrgCode(param);    //组织code 
		requestMap.put("schoolCode", orgCode);
		//System.out.println("====schoolCode===="+orgCode);
		List<Map<String,Object>> list = scoreSearchService.getExamNameByCode(requestMap);
		//System.out.println("=====list===="+list);
		out.print(getSerializer().formatList(list));
	}
	
	/**
	 * @Title: getCourseByExamNumber
	 * @Description: 通过考试编号关联科目
	 * @author huchuanhu
	 * @date 2016年11月17日 
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=getCourseByExamNumber")
	public void getCourseByExamNumber(@RequestParam("data") String data,java.io.PrintWriter out){
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		List<Map<String,Object>> list = scoreSearchService.getCourseByExamNumber(requestMap);
		//System.out.println("=====list===="+list);
		out.print(getSerializer().formatList(list));
	}
	/**
	 * @Title: exportExcel
	 * @Description: 教研员导出成绩查询excel数据
	 * @author xiahuajun
	 * @date 2016年11月5日 
	 * @param request
	 * @param response
	 * @throws UnsupportedEncodingException
	 * @return void
	 */
	@RequestMapping(params="command=exportExcelForInstructor")
	public void exportExcelForInstructor(HttpServletRequest request,HttpServletResponse response ) throws UnsupportedEncodingException{
	    String datas=request.getParameter("data");
    	Map<String,Object> requestMap = this.getSerializer().parseMap(datas) ;
    	String grade = requestMap.get("grade").toString();
		String schoolType = null;
		if("11".equals(grade) || "12".equals(grade) || "13".equals(grade) || "14".equals(grade) || "15".equals(grade)){
			schoolType = "xx";
		}
		else if("16".equals(grade) || "17".equals(grade) || "18".equals(grade) || "19".equals(grade)){
			schoolType = "cz";
		}
		else if("31".equals(grade) || "32".equals(grade) || "33".equals(grade)){
			schoolType = "gz";
		}
		String stuCode = trimString(requestMap.get("examNumberOrStuCode").toString());
		String examNumber = trimString(requestMap.get("examNumberOrStuCode").toString());
		String time = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		//int year = parseInteger(time.substring(0, time.indexOf("-")));
		//int month = parseInteger(time.substring(time.indexOf("-") + 1, time.lastIndexOf("-")));
		//requestMap.put("schoolCode", schoolCode);
		requestMap.put("stuCode", stuCode);
		requestMap.put("examNumber", examNumber);
		//教研员查询成绩导出excel
		List<Map<String, Object>>  paramList = scoreSearchService.selectExportScoreDataForInstructor(requestMap);
		String fileDisplay=requestMap.get("scoreHtml").toString()+".xls";
		//System.out.println("====fileDisplay===="+fileDisplay);
    	try{
			response.setContentType("application/x-download");
			fileDisplay = URLEncoder.encode(fileDisplay, "utf-8");
	        response.addHeader("Content-Disposition", "attachment;filename="+ fileDisplay);
	     //创建一个webbook，对应一个Excel文件  
	        HSSFWorkbook workbook = new HSSFWorkbook();
	        //在webbook中添加一个sheet,对应Excel文件中的sheet  
	        HSSFSheet sheet = workbook.createSheet();
	        //在sheet中添加表头第0行
	        workbook.setSheetName(0,"导出成绩数据");
	        
	        sheet.setColumnWidth((short)0,(short)2000);  
	        sheet.setColumnWidth((short)1,(short)6000);  
	        sheet.setColumnWidth((short)2,(short)6000);  
	        sheet.setColumnWidth((short)3,(short)3000);
	        sheet.setColumnWidth((short)4,(short)3000);
	        sheet.setColumnWidth((short)5,(short)3000);
	        sheet.setColumnWidth((short)6,(short)3000);
	        sheet.setColumnWidth((short)7,(short)3000);
	      //，创建单元格，并设置值表头 设置表头居中  
	        HSSFCellStyle titleStyle = workbook.createCellStyle();  
	        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式  
	        
	        HSSFFont font = workbook.createFont();
	        font.setFontName("宋体");  
	        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	        font.setFontHeightInPoints((short) 11);//设置字体大小  
	        titleStyle.setFont(font);
	        
	        //总汇字体
	        HSSFCellStyle countStyle = workbook.createCellStyle();  
	        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式  
	        HSSFFont countFont = workbook.createFont();    
	        countFont.setFontName("宋体");  
	        countFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	        countFont.setFontHeightInPoints((short)14);//设置字体大小  
	        countFont.setColor(HSSFColor.RED.index);//设置字体颜色
	        countStyle.setFont(countFont);
	        
	        
	        //学生信息字体
	        HSSFCellStyle style = workbook.createCellStyle();  
	        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); 
	        //表头
	        String[] titleAry=new String[]{"序号","考号","学籍号","姓名","学校","年级","班级","语文","数学","外语","物理","化学","总分"};
	        HSSFRow row = sheet.createRow((short) 0);
	        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, (titleAry.length-1)));    
	        HSSFCell cellTiltle = row.createCell(0);  
	        row.setHeight((short)350);
	        cellTiltle.setCellStyle(countStyle);
	        
	        cellTiltle.setCellValue(requestMap.get("scoreHtml").toString());
	        
	        row = sheet.createRow((short) 1);
	        
	        //设置title
	        for(int i=0;i<titleAry.length;i++)
	        {
	        	HSSFCell cell=row.createCell((short)i);
	        	cell.setCellStyle(titleStyle);
	        	cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	            cell.setCellValue(titleAry[i]);  
	        }
	        
	        //写入实体数据
	       int j=1;
	        for(int i=0;i<paramList.size();i++)
	        {
	        	Map<String,Object> paramMap =paramList.get(i);
	        	row=sheet.createRow((short) j+ 1);
	        	j++;
	        	HSSFCell cell=row.createCell((short)0);
	        	cell.setCellStyle(style);
	        	cell.setCellValue((i+1)); 
	        	
	          	HSSFCell cellOne=row.createCell((short)1);
	        	cellOne.setCellStyle(style);
	        	cellOne.setCellValue(parseString(paramMap.get("Exam_Number"))); 
	        	
	        	
	        	HSSFCell cellTwo=row.createCell((short)2);
	        	cellTwo.setCellStyle(style);
	        	cellTwo.setCellValue(parseString(paramMap.get("XJFH"))); 
	        	
	        	HSSFCell cellThree=row.createCell((short)3);
	        	cellThree.setCellStyle(style);
	        	cellThree.setCellValue(parseString(paramMap.get("Name"))); 
	        	
	        	HSSFCell cellSchool=row.createCell((short)4);
	        	sheet.setColumnWidth(4, 8000);
	        	cellSchool.setCellStyle(style);
	        	cellSchool.setCellValue(parseString(paramMap.get("School_Name"))); 
	        	
	        	HSSFCell cellFour=row.createCell((short)5);
	        	cellFour.setCellStyle(style);
	        	
	        	String Grade_Id = "";
	        	if("11".equals(paramMap.get("Grade_Id").toString())){
					Grade_Id = "一年级";
				}
				else if("12".equals(paramMap.get("Grade_Id").toString())){
					Grade_Id = "二年级";
				}
				else if("13".equals(paramMap.get("Grade_Id").toString())){
					Grade_Id = "三年级";
				}
				else if("14".equals(paramMap.get("Grade_Id").toString())){
					Grade_Id = "四年级";
				}
				else if("15".equals(paramMap.get("Grade_Id").toString())){
					Grade_Id = "五年级";
				}
				else if("16".equals(paramMap.get("Grade_Id").toString())){
					Grade_Id = "六年级";
				}
				else if("17".equals(paramMap.get("Grade_Id").toString())){
					Grade_Id = "七年级";
				}
				else if("18".equals(paramMap.get("Grade_Id").toString())){
					Grade_Id = "八年级";
				}
				else if("19".equals(paramMap.get("Grade_Id").toString())){
					Grade_Id = "九年级";
				}
				else if("31".equals(paramMap.get("Grade_Id").toString())){
					Grade_Id = "高一年级";
				}
				else if("32".equals(paramMap.get("Grade_Id").toString())){
					Grade_Id = "高二年级";
				}
				else if("33".equals(paramMap.get("Grade_Id").toString())){
					Grade_Id = "高三年级";
				}
	        	cellFour.setCellValue(Grade_Id); 
	        	
	        	HSSFCell cellFive=row.createCell((short)6);
	        	cellFive.setCellStyle(style);
	        	
	        	String Class_Id = "";
	        	if("01".equals(paramMap.get("Class_Id").toString())){
	        		Class_Id = "一班";
				}
				else if("02".equals(paramMap.get("Class_Id").toString())){
					Class_Id = "二班";
				}
				else if("03".equals(paramMap.get("Class_Id").toString())){
					Class_Id = "三班";
				}
				else if("04".equals(paramMap.get("Class_Id").toString())){
					Class_Id = "四班";
				}
				else if("05".equals(paramMap.get("Class_Id").toString())){
					Class_Id = "五班";
				}
				else if("06".equals(paramMap.get("Class_Id").toString())){
					Class_Id = "六班";
				}
				else if("07".equals(paramMap.get("Class_Id").toString())){
					Class_Id = "七班";
				}
				else if("08".equals(paramMap.get("Class_Id").toString())){
					Class_Id = "八班";
				}
				else if("09".equals(paramMap.get("Class_Id").toString())){
					Class_Id = "九班";
				}
				else if("10".equals(paramMap.get("Class_Id").toString())){
					Class_Id = "十班";
				}
				else if("11".equals(paramMap.get("Class_Id").toString())){
					Class_Id = "十一班";
				}
				else if("12".equals(paramMap.get("Class_Id").toString())){
					Class_Id = "十二班";
				}
				else if("13".equals(paramMap.get("Class_Id").toString())){
					Class_Id = "十三班";
				}
				else if("14".equals(paramMap.get("Class_Id").toString())){
					Class_Id = "十四班";
				}
				else if("15".equals(paramMap.get("Class_Id").toString())){
					Class_Id = "十五班";
				}
	        	cellFive.setCellValue(Class_Id); 
	        
	        	
	        	HSSFCell cellSix=row.createCell((short)7);
	        	cellSix.setCellStyle(style);
	        	cellSix.setCellValue(parseString(paramMap.get("yw"))); 
	        	
	        	HSSFCell cellSeven=row.createCell((short)8);
	        	cellSeven.setCellStyle(style);
	        	cellSeven.setCellValue(parseString(paramMap.get("sx"))); 
	        	
	        	HSSFCell cellEight=row.createCell((short)9);
	        	cellEight.setCellStyle(style);
	        	cellEight.setCellValue(parseString(paramMap.get("yy"))); 
	        	
	        	HSSFCell cellNine=row.createCell((short)10);
	        	cellNine.setCellStyle(style);
	        	cellNine.setCellValue(parseString(paramMap.get("wl"))); 
	        	
	        	HSSFCell cellTen=row.createCell((short)11);
	        	cellTen.setCellStyle(style);
	        	cellTen.setCellValue(parseString(paramMap.get("hx"))); 
	        	
	        	HSSFCell cellEleven=row.createCell((short)12);
	        	cellEleven.setCellStyle(style);
	        	cellEleven.setCellValue(parseString(paramMap.get("Total_Score"))); 
	        
	        }
	        OutputStream out=response.getOutputStream();
			workbook.write(out);
	        out.flush();
	        out.close();
		}catch(Exception e){
			System.out.println(e);
		}
	}
	
	@Autowired
	private ScoreSearchService scoreSearchService;
	@Autowired
	private TeacherManageService teacherManageService;
	@Autowired
	private TeacherManagementService teacherManagementService;
	@Autowired
	private ExamNumberManageService examNumberManageService;
	@Autowired
	private DistrictSubjectInstructorService districtSubjectInstructorService;
	@Autowired
	private PoliticalInstructorService politicalInstructorService;
	@Autowired
	private NoticeBasicInfoService noticeBasicInfoService1;
}