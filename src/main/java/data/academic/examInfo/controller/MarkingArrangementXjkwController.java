package data.academic.examInfo.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import data.academic.examInfo.service.ExamInfoSchoolService;
import data.academic.examInfo.service.ExamInfoService;
import data.academic.examInfo.service.MarkingArrangementService;
import data.academic.examNumberManagement.service.AdminExamNumberService;
import data.academic.examNumberManagement.service.ExamNumberManageService;
import data.academic.statisticsAnalysis.service.SchoolPlainAdminScoreSearchService;
import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractBaseController;
import data.framework.support.ConfigContext;
import data.platform.authority.security.SecurityContext;

/**
 * @Title: MarkingArrangementXjkw
 * @Description: TODO
 * @author zhaohuanhuan
 * @date 2016年10月24日 下午3:20:00
 */
@RestController
@RequestMapping("examInfo/markingArrangement_xjkw")
public class MarkingArrangementXjkwController  extends AbstractBaseController{
 
	@Override
	protected void init(ModelMap model, HttpServletRequest request) {
		// TODO Auto-generated method stub
	}
	Map<String, Object> paramMaps=new HashMap<>();
	Map<String, Object> paramExamAndGradeMaps=new HashMap<>();
	
	/**
	 * @Title: serachCoursePaging
	 * @Description: 得到该考试下的所有科目用于分配阅卷人
	 * @author zhaohuanhuan
	 * @date 2016年10月24日 
	 * @param data
	 * @param out
	 * @param request
	 * @return void
	 */
	@RequestMapping(params = "command=serachCoursePaging")
	public void serachCoursePaging(@RequestParam("data") String data,
			java.io.PrintWriter out,HttpServletRequest request) {
		
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		int currentPage = parseInteger(requestMap.get("page"));
		int pageSize = parseInteger(requestMap.get("rows"));
		String sortField = trimString(requestMap.get("sidx"));
		String sort = trimString("asc");
      String examCode=trimString(requestMap.get("o"));
		requestMap.put("examCode", examCode);
		PagingResult<Map<String, Object>> pagingResult = markingArrangementService
				.serachCoursePaging(requestMap, sortField, sort, currentPage,
						pageSize);
		List<Map<String, Object>> list = pagingResult.getRows();
		List<Map<String, Object>> paramList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> teacherList=new ArrayList<Map<String, Object>>();
	   String teaName="";
		for (Map<String, Object> map : list) {
			
			map.put("examCode",examCode );
			map.put("course",map.get("Course"));
			map.put("Course_Exam_Time",formatDate(((Date)map.get("Course_Exam_Time")),"yyyy-MM-dd"));
			map.put("Marking_Time",formatDate(((Date)map.get("Marking_Time")),"yyyy-MM-dd"));
			map.put("Marking_Place",formatString(map.get("Marking_Place")));
			map.put("Exam_Length",formatString(map.get("Exam_Length")));
    		map.put("Marking_End_Date",formatDate(parseDate(map.get("Marking_End_Date")), "yyyy-MM-dd"));
    		map.put("Marking_End_Time",formatString(map.get("Marking_End_Time")));
    		map.put("Marking_Start_Time",formatString(map.get("Marking_Start_Time")));
    		map.put("Course_Exam_Type",formatString(map.get("Course_Exam_Type")));
			int teacherNum=1;
			StringBuffer teachers=new StringBuffer();
			teacherList=markingArrangementService.getSelectedTeacher(map);
			if(teacherList.size()>0){
				for (Map<String, Object> teacherMap : teacherList) {
					teaName=parseString(teacherMap.get("Teacher_Name"));
					if(1<teacherNum  && teacherNum<=teacherList.size()){
						teachers.append(",");
					}
					teachers.append(teaName);
					teacherNum++;
				}
			}
			
		   map.put("Teacher_Name", teachers);
			paramList.add(map);
		}
		PagingResult<Map<String, Object>> newPagingResult = new PagingResult<Map<String, Object>>(
				paramList, pagingResult.getPage(), pagingResult.getPageSize(),
				pagingResult.getRecords());
		        out.print(getSerializer().formatObject(newPagingResult));
	}
	
	
	
	/**
	 * @Title: queryParams
	 * @Description: 获得年级和科目考试编号
	 * @author zhaohuanhuan
	 * @date 2016年10月25日 
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=queryParams")
	public void queryParams(@RequestParam("data") String data, java.io.PrintWriter out) {
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		paramMaps = new HashMap<String, Object>();
		//根据登录名得到学校code
		 paramMaps.put("gradeId", requestMap.get("gradeId"));
		
		 if( requestMap.containsKey("course")){
			 paramMaps.put("course", requestMap.get("course"));
		 }
		 paramMaps.put("examCode", requestMap.get("examCode"));
		out.print(getSerializer().formatMap(paramMaps));
	}
	
	
	/**
	 * @Title: queryExamCodeAndGradeCodeUrl
	 * @Description: 得到年级和考号
	 * @author zhaohuanhuan
	 * @date 2016年10月27日 
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=queryExamCodeAndGradeCode")
	public void queryExamCodeAndGradeCode(@RequestParam("data") String data, java.io.PrintWriter out) {
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		 paramExamAndGradeMaps = new HashMap<String, Object>();
		 paramExamAndGradeMaps.put("gradeId", requestMap.get("gradeId"));
		 paramExamAndGradeMaps.put("examCode", requestMap.get("examCode"));
		out.print(getSerializer().formatMap(paramExamAndGradeMaps));
	}
	
	
	
	/**
	 * @Title: getTeacherByCourseAndGrade
	 * @Description: 根据科目和年级得到老师
	 * @author zhaohuanhuan
	 * @date 2016年10月25日 
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=getTeacherByCourseAndGrade")
	public void getTeacherByCourseAndGrade(@RequestParam("data") String data, java.io.PrintWriter out) {
		Map<String, Object> parmaMap = getSerializer().parseMap(data);
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> resultByExamCodeMap = new HashMap<>();
		//根据登录名得到学校code
		 String schoolCode = examNumberManageService.getSchoolCodeByLoginName(SecurityContext.getPrincipal().getUsername());
		 parmaMap.put("gradeId",  paramMaps.get("gradeId"));
		 resultByExamCodeMap.put("gradeId",  paramMaps.get("gradeId"));
		
 		 parmaMap.put("schoolCode", schoolCode);
 		parmaMap.put("examCode", paramMaps.get("examCode"));
 		parmaMap.put("course",  paramMaps.get("course"));
		 //根据科目和年级得到老师集合 
		  List<Map<String, Object>> teacherByExamCodeList=markingArrangementService.getSelectedTeacher(parmaMap);
		 List<String> teacherPkList=new ArrayList<>();
		  for (Map<String, Object> map : teacherByExamCodeList) {
			  teacherPkList.add(map.get("Teacher_Pk").toString());
		}
		  parmaMap.put("teacherPkList", teacherPkList);
		  if(parmaMap.containsKey("courses")){
				 parmaMap.put("course",  parmaMap.get("courses"));
			 }else{
				 parmaMap.put("course",  paramMaps.get("course"));
				
			 }
		  List<Map<String, Object>> teacherList=markingArrangementService.getTeacherByCourseAndGrade(parmaMap);
		  resultMap.put("teacherList", teacherList);
			
		  resultMap.put("teacherByExamCodeList", teacherByExamCodeList);
		  out.print(getSerializer().formatMap(resultMap));
	}

	/**
	 * @Title: getTeacherByCourseAndExamCode
	 * @Description: 根据考号和科目的到教师
	 * @author zhaohuanhuan
	 * @date 2016年10月25日 
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=getTeacherByCourseAndExamCode")
	public void getTeacherByCourseAndExamCode(@RequestParam("data") String data, java.io.PrintWriter out) {
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		 requestMap.put("course",  paramMaps.get("course"));
		 requestMap.put("examCode",  paramMaps.get("examCode"));
		 //根据科目和年级得到老师集合 
		 /*List<Map<String, Object>> teacherList=markingArrangementService.getSelectedTeacher(requestMap);
		 out.print(getSerializer().formatList(teacherList));*/
		 out.print(getSerializer().formatMap(requestMap));
	}
	
	/**
	 * @Title: addTeacherRefExam
	 * @Description: 添加阅卷人
	 * @author zhaohuanhuan
	 * @date 2016年10月25日 
	 * @param data
	 * @param out
	 * @throws Exception
	 * @return void
	 */
	@RequestMapping(params = "command=addTeacherRefExam")
	public void addTeacherRefExam(@RequestParam("data") String data,
			java.io.PrintWriter out) throws Exception {
		String loginName=SecurityContext.getPrincipal().getUsername();
		String schoolCode = examNumberManageService.getSchoolCodeByLoginName(loginName);
		Map<String, Object> paramMap = getSerializer().parseMap(data);
		
		Map<String, Object> resultMap = getSerializer().parseMap(data);
		
		  //得到选择的老师集合
		List<Map<String,Object>> teacherIdList = (List<Map<String,Object>>)paramMap.get( "id" ) ;
		paramMap.put("schoolCode", schoolCode);
		 markingArrangementService.deleteTeacherRefExam(paramMap);
	    if( teacherIdList != null && !teacherIdList.isEmpty() )
	        {
	            for( Map<String,Object> teacherIdMap : teacherIdList )
	            {
	            	 teacherIdMap.put("examCode", parseString( paramMap.get( "examCode" ) ) );
	            	 teacherIdMap.put("teacherId", parseString( teacherIdMap.get( "id" ) ));
	            	 teacherIdMap.put("course", parseString( paramMap.get( "course" ) ) );
	            	 teacherIdMap.put("schoolCode",schoolCode);
	            	 markingArrangementService.addTeacherRefExam(teacherIdMap);
	            }
	            resultMap.put("success", "true");
	        }else{
	        	 resultMap.put("success", "false");
	        }
		
		out.print(getSerializer().formatMap(paramMap));
		
	}
	
	
	/**
	 * @Title: serachCourseAdminPaging
	 * @Description:分页显示区级发布考试所对应的科目
	 * @author zhaohuanhuan
	 * @date 2016年10月26日 
	 * @param data
	 * @param out
	 * @param request
	 * @return void
	 */
	@RequestMapping(params = "command=serachCourseAdminPaging")
	public void serachCourseAdminPaging(@RequestParam("data") String data,
			java.io.PrintWriter out,HttpServletRequest request) {
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		int currentPage = parseInteger(requestMap.get("page"));
		int pageSize = parseInteger(requestMap.get("rows"));
		String sortField = trimString(requestMap.get("sidx"));
		String sort = trimString("asc");
		String examCode="";
		if("".equals(trimString(paramExamAndGradeMaps.get("examCode"))) || null==trimString(paramExamAndGradeMaps.get("examCode"))){
			examCode =trimString(requestMap.get("examCode"));
		}else{
			examCode=trimString(paramExamAndGradeMaps.get("examCode"));
		}
		requestMap.put("examCode", examCode);
		PagingResult<Map<String, Object>> pagingResult = markingArrangementService
				.serachCoursePaging(requestMap, sortField, sort, currentPage,
						pageSize);
		List<Map<String, Object>> list = pagingResult.getRows();
	 
		List<Map<String, Object>> paramList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> teacherList=new ArrayList<Map<String, Object>>();
	   String teaName="";
		String userId=SecurityContext.getPrincipal().getId();
		//根据登录人的id得到其角色code
	    String roleCode=examInfoService.getRoleByUserId(userId);
		List<Map<String, Object>> SchoolList=new ArrayList<>();
	    if(ConfigContext.getValue("framework.tmis.roleCode['qpRoleAdminCode']").equals(roleCode)){
	    	for (Map<String, Object> map : list) {
	    		map.put("Course_Exam_Time",formatDate(parseDate(map.get("Course_Exam_Time")), "yyyy-MM-dd"));
	    		map.put("Marking_Time",formatDate(parseDate(map.get("Marking_Time")), "yyyy-MM-dd"));
	    		map.put("Marking_Place",formatString(map.get("Marking_Place")));
	    		map.put("Exam_Length",formatString(map.get("Exam_Length")));
	    		map.put("Marking_End_Date",formatDate(parseDate(map.get("Marking_End_Date")), "yyyy-MM-dd"));
	    		map.put("Marking_End_Time",formatString(map.get("Marking_End_Time")));
	    		map.put("Marking_Start_Time",formatString(map.get("Marking_Start_Time")));
	    		map.put("Course_Exam_Type",formatString(map.get("Course_Exam_Type")));
				map.put("examCode",examCode);
				map.put("course",map.get("Course"));
				//得到存在教师的学校
				SchoolList=markingArrangementService.getSchoolShortName(map);
				StringBuffer teacherNames=new StringBuffer();
				for (Map<String, Object> schoolMap : SchoolList) {
					int teacherNum=1;
					StringBuffer teachers=new StringBuffer();
					map.put("schoolCode", schoolMap.get("School_Code"));
					String schoolShortName=trimString(schoolMap.get("School_Short_Name"));
					//得到选中的教师集合
					teacherList=markingArrangementService.getSelectedTeacher(map);
					if(teacherList.size()>0){
						for (Map<String, Object> teacherMap : teacherList) {
							teaName=parseString(teacherMap.get("Teacher_Name"));
							if(1<teacherNum  && teacherNum<=teacherList.size()){
								teachers.append(",");
							}
							teachers.append(teaName);
							teacherNum++;
						}
						teacherNames.append(schoolShortName)
						.append("(")
						.append(teachers.toString())
						.append(")");
					}
					
				   map.put("Teacher_Name", teacherNames);
				}
				
				paramList.add(map);
			}
	    }else{
	    	  String loginName=SecurityContext.getPrincipal().getUsername();
	    	//根据登录名得到学code
			String schoolCode = examNumberManageService.getSchoolCodeByLoginName(loginName);
	    	for (Map<String, Object> map : list) {
	    		map.put("Course_Exam_Time",
						formatDate(parseDate(map.get("Course_Exam_Time")), "yyyy-MM-dd"));
	    		map.put("Marking_Time",
						formatDate(parseDate(map.get("Marking_Time")), "yyyy-MM-dd"));
	    		map.put("Marking_Place",formatString(map.get("Marking_Place")));
	    		map.put("Exam_Length",formatString(map.get("Exam_Length")));
	    		map.put("Marking_End_Date",formatDate(parseDate(map.get("Marking_End_Date")), "yyyy-MM-dd"));
	    		map.put("Marking_End_Time",formatString(map.get("Marking_End_Time")));
	    		map.put("Marking_Start_Time",formatString(map.get("Marking_Start_Time")));
	    		map.put("Course_Exam_Type",formatString(map.get("Course_Exam_Type")));
				map.put("examCode",examCode);
				map.put("course",map.get("Course"));
				map.put("schoolCode",schoolCode);
					//得到选中的教师集合
					teacherList=markingArrangementService.getSelectedTeacher(map);
					int teacherNum=1;
					StringBuffer teachers=new StringBuffer();
					//StringBuffer teacherNames=new StringBuffer();
					if(teacherList.size()>0){
						for (Map<String, Object> teacherMap : teacherList) {
							teaName=parseString(teacherMap.get("Teacher_Name"));
							if(1<teacherNum  && teacherNum<=teacherList.size()){
								teachers.append(",");
							}
							teachers.append(teaName);
							teacherNum++;
						}
					}
					
				   map.put("Teacher_Name", teachers);
				paramList.add(map);
			}
	    }
	
		
		PagingResult<Map<String, Object>> newPagingResult = new PagingResult<Map<String, Object>>(
				paramList, pagingResult.getPage(), pagingResult.getPageSize(),
				pagingResult.getRecords());
		        out.print(getSerializer().formatObject(newPagingResult));
	}
	
	
	/**
	 * @Title: getCoursesBySchoolType
	 * @Description: 根据学校类型得到科目
	 * @author zhaohuanhuan
	 * @date 2016年10月25日 
	 * @param data
	 * @param out
	 * @return void
	 */

	@RequestMapping(params = "command=getCourseBySchoolType")
	public void getCourseBySchoolType(@RequestParam("data") String data,
			java.io.PrintWriter out,HttpServletRequest request) {
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		int currentPage = parseInteger(requestMap.get("page"));
		int pageSize = parseInteger(requestMap.get("rows"));
		String sortField = trimString(requestMap.get("sidx"));
		String sort = trimString("asc");
		String examCode=trimString(requestMap.get("o"));
		requestMap.put("examCode", examCode);
		  //根据登录名查询schoolCode
				String loginName=SecurityContext.getPrincipal().getUsername();
				String schoolCode = examNumberManageService.getSchoolCodeByLoginName(loginName);
				String schoolSequence = schoolPlainAdminScoreSearchService.findSchoolTypeBySchoolCode(schoolCode);
				if("0".equals(schoolSequence)){
					requestMap.put("schoolType","xx");
				}
				else if("1".equals(schoolSequence)){
					requestMap.put("schoolType","cz");
				}
				else if("3".equals(schoolSequence)){
					requestMap.put("schoolType","gz");
				}
				else if("2".equals(schoolSequence) || "3".equals(schoolSequence) || "4".equals(schoolSequence)  ){
					if("3062".equals(schoolCode) ){
						requestMap.put("schoolType","cz");
					}
					requestMap.put("schoolType","gz");
				}
		
		PagingResult<Map<String, Object>> pagingResult = markingArrangementService
				.selectCourseBySchoolCode(requestMap, sortField, sort, currentPage,
						pageSize);
		List<Map<String, Object>> list = pagingResult.getRows();
		List<Map<String, Object>> paramList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> teacherList=new ArrayList<Map<String, Object>>();
	   String teaName="";
		for (Map<String, Object> map : list) {
			
			map.put("examCode",examCode);
			map.put("course",map.get("DictionaryCode"));
			map.put("Course", map.get("DictionaryCode"));
			map.put("schoolCode", schoolCode);
			int teacherNum=1;
			StringBuffer teachers=new StringBuffer();
			teacherList=markingArrangementService.getSelectedTeacher(map);
			if(teacherList.size()>0){
				for (Map<String, Object> teacherMap : teacherList) {
					teaName=parseString(teacherMap.get("Teacher_Name"));
					if(1<teacherNum  && teacherNum<=teacherList.size()){
						teachers.append(",");
					}
					teachers.append(teaName);
					teacherNum++;
				}
			}
			
		   map.put("Teacher_Name", teachers);
			paramList.add(map);
		}
		PagingResult<Map<String, Object>> newPagingResult = new PagingResult<Map<String, Object>>(
				list, pagingResult.getPage(), pagingResult.getPageSize(),
				pagingResult.getRecords());
		        out.print(getSerializer().formatObject(newPagingResult));
	}
	
	
	/**
	 * @Title: showSelectArrangementByExam
	 * @Description: 得到选中的科目的信息
	 * @author zhaohuanhuan
	 * @date 2016年11月1日 
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=showSelectArrangementByExam")
	public void showSelectArrangementByExam(@RequestParam("data") String data, java.io.PrintWriter out) {
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		List<Map<String, Object>> paramList=new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> courseList =new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> teacherList=new ArrayList<Map<String, Object>>();
	    String teaName="";
		String examCode=trimString(requestMap.get("examCode"));
		String userId=SecurityContext.getPrincipal().getId();
		//根据登录人的id得到其角色code
	    String roleCode=examInfoService.getRoleByUserId(userId);
		List<Map<String, Object>> SchoolList=new ArrayList<>();
	    if(ConfigContext.getValue("framework.tmis.roleCode['qpRoleCode']").contains(roleCode)){
	    	 courseList = examInfoSchoolService.selectCoursesByExamCode(requestMap);
	    	for (Map<String, Object> map : courseList) {
				map.put("examCode",examCode);
				map.put("course",map.get("Course"));
				map.put("Course_Exam_Time",formatDate(((Date)map.get("Course_Exam_Time")),"yyyy-MM-dd"));
				map.put("Marking_Time",formatDate(((Date)map.get("Marking_Time")),"yyyy-MM-dd"));
				map.put("Marking_End_Date",formatDate(((Date)map.get("Marking_End_Date")),"yyyy-MM-dd"));
				map.put("Marking_Start_Time",formatString(map.get("Marking_Start_Time")));
				map.put("Marking_End_Time",formatString(map.get("Marking_End_Time")));
				map.put("Course_Exam_Type",formatString(map.get("Course_Exam_Type")));
		        //得到该考试下面的所有学校总数
		        Integer schoolSum=adminExamNumber.countSchoolByExamCode(requestMap);
				
				map.put("teacherId","teacherId");
				Integer teacherCount=markingArrangementService.countArrangementNum(map);
				SchoolList=markingArrangementService.getSchoolShortName(map);
				Integer schoolNum=SchoolList.size();
				Integer schoolNotNum=schoolSum-schoolNum;
				List<String> schoolCodeList=new ArrayList<>();
				for (Map<String, Object> schoolMap : SchoolList) {
					schoolCodeList.add(trimString(schoolMap.get("School_Code")));
				}
				map.put("schoolCodeList", schoolCodeList);
				map.put("teacherCount", teacherCount);
				map.put("schoolSum", schoolSum);
				map.put("schoolNum", schoolNum);
				map.put("schoolNotNum", schoolNotNum);
				paramList.add(map);
			}
	    }else{
	    	  String loginName=SecurityContext.getPrincipal().getUsername();
	    	//根据登录名得到学code
			String schoolCode = examNumberManageService.getSchoolCodeByLoginName(loginName);
			Map<String, Object> paramsMap=new HashMap<>();

			paramsMap.put("examCode", examCode);
			courseList=new ArrayList<>();
			courseList =  examInfoSchoolService.selectCoursesByExamCode(requestMap);
			for (Map<String, Object> map : courseList) {
				map.put("examCode",examCode);
				map.put("course",map.get("Course"));
			    map.put("schoolCode",schoolCode);
			    map.put("Course_Exam_Time",formatDate(((Date)map.get("Course_Exam_Time")),"yyyy-MM-dd"));
			    map.put("Marking_Time",formatDate(((Date)map.get("Marking_Time")),"yyyy-MM-dd"));
				map.put("Marking_Place",formatString(map.get("Marking_Place")));
				map.put("Marking_End_Date",formatDate(((Date)map.get("Marking_End_Date")),"yyyy-MM-dd"));
				map.put("Marking_Start_Time",formatString(map.get("Marking_Start_Time")));
				map.put("Marking_End_Time",formatString(map.get("Marking_End_Time")));
				map.put("Course_Exam_Type",formatString(map.get("Course_Exam_Type")));
				Integer teacherCount=markingArrangementService.countArrangementNum(map);
			    //得到选中的教师集合
				teacherList=markingArrangementService.getSelectedTeacher(map);
				StringBuffer teachers=new StringBuffer();
				int teacherNum=1;
				if(teacherList.size()>0){
					for (Map<String, Object> teacherMap : teacherList) {
						teaName=parseString(teacherMap.get("Teacher_Name"));
						if(1<teacherNum  && teacherNum<=teacherList.size()){
							teachers.append(",");
						}
				teachers.append(teaName);
							teacherNum++;
						}
					
					}
				   map.put("Teacher_Name", teachers);
				   map.put("teacherCount", teacherCount);
				paramList.add(map);
			}
	    }
		        out.print(getSerializer().formatList(paramList));
	}
	
	
	/**
	 * @Title: showArrangementByExam
	 * @Description:根据考试编号显示各科阅卷人
	 * @author zhaohuanhuan
	 * @date 2016年10月26日 
	 * @param data
	 * @param out
	 * @param request
	 * @return void
	 */
	@RequestMapping(params = "command=showArrangementByExam")
	public void showArrangementByExam(@RequestParam("data") String data, java.io.PrintWriter out) {
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		//根据年级得到其父字典项的id
		List<Map<String, Object>> parIdMapList=examInfoService.getParentDictionaryByDicCode(trimString(requestMap.get("gradeId")));
       List<String> parIdList=new ArrayList<String>();
		for (Map<String, Object> map : parIdMapList) {
			parIdList.add(trimString(map.get("ParentDictionary")));
		}
		requestMap.put("parentIdList",parIdList);
		List<Map<String, Object>> courseList =new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> paramList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> teacherList=new ArrayList<Map<String, Object>>();
	    String teaName="";
		String examCode=trimString(requestMap.get("examCode"));
		String userId=SecurityContext.getPrincipal().getId();
		//根据登录人的id得到其角色code
	    String roleCode=examInfoService.getRoleByUserId(userId);
		List<Map<String, Object>> SchoolList=new ArrayList<>();
	    if(ConfigContext.getValue("framework.tmis.roleCode['qpRoleAdminCode']").equals(roleCode)){
	    	 courseList = markingArrangementService.showArrangementByExam(requestMap);
	    	for (Map<String, Object> map : courseList) {
				map.put("examCode",examCode);
				map.put("course",map.get("DictionaryCode"));
				SchoolList=markingArrangementService.getSchoolShortName(map);
			    StringBuffer teacherNames=new StringBuffer();
				for (Map<String, Object> schoolMap : SchoolList) {
					int teacherNum=1;
					StringBuffer teachers=new StringBuffer();
					map.put("schoolCode", schoolMap.get("School_Code"));
					String schoolShortName=trimString(schoolMap.get("School_Short_Name"));
					//得到选中的教师集合
					teacherList=markingArrangementService.getSelectedTeacher(map);
					if(teacherList.size()>0){
						for (Map<String, Object> teacherMap : teacherList) {
							teaName=parseString(teacherMap.get("Teacher_Name"));
							if(1<teacherNum  && teacherNum<=teacherList.size()){
								teachers.append(",");
							}
							teachers.append(teaName);
							teacherNum++;
						}
						teacherNames.append(schoolShortName)
						.append("(")
						.append(teachers.toString())
						.append(")");
					}
				}
				map.put("Teacher_Name", teacherNames);
				paramList.add(map);
			}
	    }else{
	    	  String loginName=SecurityContext.getPrincipal().getUsername();
	    	//根据登录名得到学code
			String schoolCode = examNumberManageService.getSchoolCodeByLoginName(loginName);
			Map<String, Object> paramsMap=new HashMap<>();
			List<String> classIdList=new ArrayList<>();
			paramsMap.put("classIdList", classIdList);
			paramsMap.put("examCode", examCode);
			courseList=new ArrayList<>();
			 courseList=examInfoSchoolService.getCourserByExamCodeAndClass(paramsMap);
			for (Map<String, Object> map : courseList) {
				map.put("examCode",examCode);
				map.put("course",map.get("Course"));
			    map.put("schoolCode",schoolCode);
					//得到选中的教师集合
					teacherList=markingArrangementService.getSelectedTeacher(map);
					StringBuffer teachers=new StringBuffer();
					int teacherNum=1;
					if(teacherList.size()>0){
						for (Map<String, Object> teacherMap : teacherList) {
							teaName=parseString(teacherMap.get("Teacher_Name"));
							if(1<teacherNum  && teacherNum<=teacherList.size()){
								teachers.append(",");
							}
							teachers.append(teaName);
							teacherNum++;
						}
					
					}
				   map.put("Teacher_Name", teachers);
				paramList.add(map);
			}
	    }
		        out.print(getSerializer().formatList(paramList));
	}
	
	
	
	
	@Autowired
	private MarkingArrangementService markingArrangementService;
	
	@Autowired
	private ExamNumberManageService examNumberManageService;
	
	@Autowired
	private SchoolPlainAdminScoreSearchService schoolPlainAdminScoreSearchService;
	
	@Autowired
	private ExamInfoService examInfoService;
	
	@Autowired
	private ExamInfoSchoolService examInfoSchoolService;
	
	@Autowired
	private AdminExamNumberService adminExamNumber;
}
