
package data.academic.statisticsAnalysis.controller;

import java.io.UnsupportedEncodingException;
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
import data.academic.statisticsAnalysis.service.SubTeaScoreSearchService;
import data.academic.teacherManagement.service.TeacherManagementService;
import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractBaseController;
import data.platform.authority.security.SecurityContext;
import data.platform.service.PlatformUserService;

/**
 * @Title: SubTeaScoreSearchController
 * @Description: 科任老师查询成绩控制层
 * @author zhaohuanhuan
 * @date 2016年9月6日 上午11:21:07
 */
@RestController
@RequestMapping("statisticsAnalysis/subTeaScoreSearch")
public class SubTeaScoreSearchController extends AbstractBaseController{

	@Override
	protected void init(ModelMap model, HttpServletRequest request) {
		
		
	}
	
	
	/**
	 * @Title: searchPaging
	 * @Description: 分页显示科任老师查询的成绩
	 * @author zhaohuanhuan
	 * @date 2016年9月8日 
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=searchScore")
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
		if ("".equals(requestMap.get("term"))) {
			requestMap.put("term", "xxq");
		}
		if ("".equals(requestMap.get("examType"))) {
			requestMap.put("examType", "qm");
		}
	    //加载当前页面是显示当前班级记录
		if(null==requestMap.get("classs")){
			 List<Map<String, Object>> list = subTeaScoreSearchService.getGradeAndClassByLoginName(SecurityContext.getPrincipal().getUsername());
			 List<String> classIdList=new ArrayList<>();
			 for (Map<String, Object> map : list) {
				String classId= map.get("Class_Name").toString();
				classIdList.add(classId);
			   }
			requestMap.put("classs",classIdList);
		}
		
		int currentPage = parseInteger(requestMap.get("page"));
		int pageSize = parseInteger(requestMap.get("rows"));
		String sortField = trimString(requestMap.get("sidx"));
		if (sortField.trim().length() == 0) {
			sortField = "Exam_Number";
		}
		String sort = trimString(requestMap.get("sord"));
		PagingResult<Map<String, Object>> pagingResult = subTeaScoreSearchService.ScoreSearch(requestMap, sortField, sort,
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
			
			// 动态得到学科类型，得到学科总分
						String courseStr = trimString(requestMap.get("course"));
						String courseVal = courseStr.substring(1, courseStr.length() - 1);
						String[] courseType = courseVal.split(", ");
						Integer yw = 0;
						for (int i = 0; i < courseType.length; i++) {
							if (map.get(courseType[i]) != null) {
								yw = yw + parseInteger(map.get(courseType[i]));
							} else {
								map.put("total", yw);
							}
							map.put("total", yw);
						}
						
						
			
			paramList.add(map);
		}
		PagingResult<Map<String, Object>> newPagingResult = new PagingResult<Map<String, Object>>(paramList,
				pagingResult.getPage(), pagingResult.getPageSize(), pagingResult.getRecords());
		
		out.print(getSerializer().formatObject(newPagingResult));
	}
	
	
	 /**
	 * @Title: getGradeAndClassByLoginName
	 * @Description: 根据登录名的到该用户的年级和班级
	 * @author zhaohuanhuan
	 * @date 2016年9月8日 
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping( params = "command=getGradeAndClassByLoginName" )
	    public void getGradeAndClassByLoginName( @RequestParam( "data" ) String data, java.io.PrintWriter out)
	    {
	        List<Map<String, Object>> list = subTeaScoreSearchService.getGradeAndClassByLoginName(SecurityContext.getPrincipal().getUsername());
	        out.print( getSerializer().formatList( list ));
	    }
	 
	 
	 /**
	 * @Title: getCourseByLoginName
	 * @Description: 根据登录名得到该用户所教的科目
	 * @author zhaohuanhuan
	 * @date 2016年9月8日 
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping( params = "command=getCourseByLoginName" )
	  public void getCourseByLoginName( @RequestParam( "data" ) String data, java.io.PrintWriter out)
	    {
	        List<Map<String, Object>> list = subTeaScoreSearchService.getCourseByLoginName(SecurityContext.getPrincipal().getUsername());
	       
	        out.print( getSerializer().formatList( list ));
	    }
	 
	 
	 
	 
	 /**
	 * @Title: getClassByGrade
	 * @Description: 根据年级得到班级集合
	 * @author zhaohuanhuan
	 * @date 2016年9月8日 
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping( params = "command=getClassByGrade" )
	 public void getClassByGrade(@RequestParam("data") String data, java.io.PrintWriter out) {
			Map<String, Object> requestMap = getSerializer().parseMap(data);
			List<Map<String, Object>> list = subTeaScoreSearchService.getClassByGrade(requestMap.get("gradeId").toString());
			out.print(getSerializer().formatList(list));
		}
	 
	
	@RequestMapping( params = "command=getSchoolTypeAndSchoolNameByLoginName" )
	 public void getSchoolTypeAndSchoolNameByLoginName( @RequestParam( "data" ) String data, java.io.PrintWriter out)
	    {
	    	
	        String username = SecurityContext.getPrincipal().getUsername();
	        //根据登录用户显示其所属的学校类型和学校名称
	        //String schoolCode=examNumberManageService.getSchoolCodeByLoginName(username);
	        List<Map<String, Object>> list = teaManagementService.getSchoolTypeAndName(username);
	        List<Map<String, Object>> paramList = new ArrayList<Map<String,Object>>();
	        for(Map<String, Object> map : list){
	        	if("8".equals(map.get("School_Type_Sequence").toString())){
	        		map.put("School_Type_Sequence", "yey");
	        		map.put("School_Type", "幼儿园");
	        	}
	        	else if("0".equals(map.get("School_Type_Sequence").toString())){
	        		map.put("School_Type_Sequence", "xx");
	        		map.put("School_Type", "小学");
	        	}
	        	else if("1".equals(map.get("School_Type_Sequence").toString())){
	        		map.put("School_Type_Sequence", "cz");
	        		map.put("School_Type", "初中");
	        	}
	        	else if("4".equals(map.get("School_Type_Sequence").toString())
	        			   || "3".equals(map.get("School_Type_Sequence").toString()) || "2".equals(map.get("School_Type_Sequence").toString())){
	        		map.put("School_Type_Sequence", "gz");
	        		map.put("School_Type", "高中");
	        	}
	        	paramList.add(map);
	        }
	        out.print( getSerializer().formatList( paramList ));
	    }
	
	/**
	 * @Title: getIntervalstuCount
	 * @Description: 查询任课老师分数段人数
	 * @author xiahuajun
	 * @date 2016年9月12日 
	 * @param out
	 * @return void
	 */
	 @RequestMapping(params = "command=getIntervalstuCount")
	    public void getIntervalstuCount( java.io.PrintWriter out )
	    {
	    	//String username = SecurityContext.getPrincipal().getUsername();
	    	//根据登录名查询teacher_pk
			String teacher_pk = userService.selectTeacherPkByLoginName(SecurityContext.getPrincipal().getUsername());
			//查询当前用户的学校code(根据登录名)
			String schoolCode = userService.selectSchoolCodeByLoginName(SecurityContext.getPrincipal().getUsername());
			//查询科任老师的年级班级科目stuCount=[{Count=4, Total_Score=60以下 , Class_Id=01}, {Count=11, Total_Score=61-70 , Class_Id=01}, {Count=10, Total_Score=71-80 , Class_Id=01}
			//[{Grade_Name=初一, Course_Name=语文, Teacher_Name=朱苏苹, Teacher_Pk=9BD6C39D-20F4-48EF-85F5-6314EAE4B2FD, Course_Code=yw, Class_Name=01, Grade_Id=17, Course_Id=0B2D3060-C01B-4CEF-B427-B00CA6226826, Class_Id=02B7322F-1FC1-47A1-98A4-3EB508E10B21}]
			List<Map<String, Object>> list = userService.selectGradeClassCourseByTeacherPk(teacher_pk);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			//Integer startNum = 50;
			for(Map<String, Object> map : list){
				map.put("schoolCode", schoolCode);
				map.put("examType", "qm");
				map.put("term", "xxq");
				List<Map<String, Object>> stuCount = userService.selectIntervalStuCount(map);
				paramMap.put("stuCount",stuCount);
			}
				//map.put("schoolCode", schoolCode);
				//for(int i=0;i<5;i++){
//					map.put("startNum", startNum);
//					Map<String, Object> paramMap = new HashMap<String, Object>();
//					Map<String, Object> stuCount = userService.selectIntervalStuCount(map);
//					for(Entry<String,Object> map1 : stuCount.entrySet()){
//						if(null != map1.getKey()){
//							paramMap.put("interval", map1.getKey());
//						} else {
//							paramMap.put("interval", null);
//						}
//						
//						if(null != map1.getValue()){
//							paramMap.put("renCounts", map1.getValue());
//						} else {
//							paramMap.put("renCounts", 0);
//						}
//						
//						break;
//					}
//					paramList.add(paramMap);
//					startNum += 10;
//					if(startNum == 100){
//						break;
//					}
//				}
//			}
			//list.get(0).put("schoolCode", schoolCode);
			//查询某个年级各个班级的平均分
			//List<Map<String, Object>> classAvgList = userService.selectClassAvg(list.get(0));
			//查询某个分数段的人数[{E=4, Class_Id=01}, {D=11, Class_Id=01}, {C=13, Class_Id=01}, {B=5, Class_Id=01}, null]
			//map.put("paramList", paramList);
	    	out.print(getSerializer().formatMap(paramMap));
	    }
	
	 /**
		 * @Title: getIntervalstuCount
		 * @Description: 查询班主任分数段人数
		 * @author xiahuajun
		 * @date 2016年9月12日 
		 * @param out
		 * @return void
		 */
		 @RequestMapping(params = "command=getIntervalstuCountforClassRoomTeacher")
		    public void getIntervalstuCountforClassRoomTeacher( java.io.PrintWriter out )
		    {
		    	//String username = SecurityContext.getPrincipal().getUsername();
		    	//根据登录名查询teacher_pk
				String teacher_pk = userService.selectTeacherPkByLoginName(SecurityContext.getPrincipal().getUsername());
				//查询当前用户的学校code(根据登录名)
				String schoolCode = userService.selectSchoolCodeByLoginName(SecurityContext.getPrincipal().getUsername());
				//查询科任老师的年级班级科目stuCount=[{Count=4, Total_Score=60以下 , Class_Id=01}, {Count=11, Total_Score=61-70 , Class_Id=01}, {Count=10, Total_Score=71-80 , Class_Id=01}
				//[{Grade_Name=初一, Course_Name=语文, Teacher_Name=朱苏苹, Teacher_Pk=9BD6C39D-20F4-48EF-85F5-6314EAE4B2FD, Course_Code=yw, Class_Name=01, Grade_Id=17, Course_Id=0B2D3060-C01B-4CEF-B427-B00CA6226826, Class_Id=02B7322F-1FC1-47A1-98A4-3EB508E10B21}]
				List<Map<String, Object>> list = userService.selectGradeClassCourseByTeacherPk(teacher_pk);
				Map<String, Object> paramMap = new HashMap<String, Object>();
				//Integer startNum = 50;
				for(Map<String, Object> map : list){
					map.put("schoolCode", schoolCode);
					map.put("examType", "qm");
					map.put("term", "xxq");
					List<Map<String, Object>> stuCount = userService.selectIntervalStuCountForClassRoomTeacher(map);
					paramMap.put("stuCount",stuCount);
				}
					
		    	out.print(getSerializer().formatMap(paramMap));
		    }
		 
	/**
	 * @Title: getAvg
	 * @Description: 得到平均分
	 * @author zhaohuanhuan
	 * @date 2016年9月13日 
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=getAvg")
	public void getClassAvgByClassId(java.io.PrintWriter out) {
		String loginName=SecurityContext.getPrincipal().getUsername();
		List<Map<String, Object>> courseList =subTeaScoreSearchService.getCourseByLoginName(loginName);
		List<Map<String, Object>> gradeList = subTeaScoreSearchService.getGradeAndClassByLoginName(loginName);
		String schoolCode=subTeaScoreSearchService.selectSchoolCodeByLoginName(loginName);
		List<Map<String, Object>> list=new ArrayList<>();
		List<Map<String, Object>> gradesList=new ArrayList<>();
		Map<String, Object> gMap=new HashMap<>();
		
		for (Map<String, Object> gradeMap : gradeList) {
			String gradeId=gradeMap.get("Grade_Id").toString();
			if(gradesList.size()==0){
				gMap.put("Grade_Id",gradeId);
				gMap.put("Grade_Name", gradeMap.get("Grade_Name"));
				gradesList.add(gMap);
			}else{
				for (Map<String, Object> grMap : gradesList) {
					if(!grMap.get("Grade_Id").equals(gradeId)){
						gMap.put("Grade_Id",grMap.get("Grade_Id"));
						gMap.put("Grade_Name", gradeMap.get("Grade_Name"));
						gradesList.add(grMap);
					}
				}
			}
		}
		
		for(Map<String, Object> gradesMap : gradesList){
			
			String gradeId=gradesMap.get("Grade_Id").toString();
			
			for (Map<String, Object> map : courseList) {
				
				String courseCode=map.get("Course_Code").toString();
				List<Map<String, Object>> classList = subTeaScoreSearchService.getClassAvgByClassId(courseCode);
				for (Map<String, Object> classMap : classList) {
					String classId=formatString(classMap.get("avgName"));
					if(StringUtils.isNotBlank(classId) ){
						//处理班级
						String classIdOneStr=classId.substring(0, 1);
						if(classIdOneStr.equals("0")){
							classId=classId.substring(classId.length()-1);
							classId=gradesMap.get("Grade_Name").toString()+"("+classId+")班";
						}
						classMap.put("avgName", classId);
					}
				 list.add(classMap);
				}
				List<Map<String, Object>> avgList=subTeaScoreSearchService.getAvgByCourseAndGradeId(courseCode, gradeId);
				List<Map<String, Object>> gradeNameList=subTeaScoreSearchService.getGradeNameByGradeId(gradeId);
				for (Map<String, Object> avgQxMap : avgList) {
					for (Map<String, Object> gradeNameMap : gradeNameList) {
						avgQxMap.put("avgName", gradeNameMap.get("Grade_Name"));
						
						list.add(avgQxMap);
					}
				}
				List<Map<String, Object>> avgQqList=subTeaScoreSearchService.getAvgByCourseAndGradeIdAndSchoolCode(courseCode, gradeId, schoolCode);
			    for (Map<String, Object> avgQqMap : avgQqList) {
			    	list.add(avgQqMap);
			    }
			    
			}
			
		}
		
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("data", list);
		out.print(getSerializer().formatMap(resultMap));
	}
	
	
	@RequestMapping(params = "command=getSiLv")
	public void getSiLv(java.io.PrintWriter out){
		
		String loginName=SecurityContext.getPrincipal().getUsername();
		List<Map<String, Object>> courseList =subTeaScoreSearchService.getCourseByLoginName(loginName);
		List<Map<String, Object>> gradeList = subTeaScoreSearchService.getGradeAndClassByLoginName(loginName);
		List<Map<String, Object>> newGradeList=new ArrayList<>();
		List<Map<String, Object>> newClassList=new ArrayList<>();
		Map<String, Object> gMap=new HashMap<>();
		for (Map<String, Object> gradeMap : gradeList) {
			String gradeId=gradeMap.get("Grade_Id").toString();
			newClassList.add(gradeMap);
			if(newGradeList.size()==0){
				gMap.put("Grade_Id",gradeId);
				gMap.put("Grade_Name", gradeMap.get("Grade_Name"));
				newGradeList.add(gMap);
			}else{
				for (Map<String, Object> grMap : newGradeList) {
					if(!grMap.get("Grade_Id").equals(gradeId)){
						gMap.put("Grade_Id",grMap.get("Grade_Id"));
						gMap.put("Grade_Name", gradeMap.get("Grade_Name"));
						newGradeList.add(grMap);
					}
				}
			}
		}
		List<Map<String, Object>> list=new ArrayList<>();
		for (Map<String, Object> newGradeMap : newGradeList) {
			//得到年级id
			String gradeId=newGradeMap.get("Grade_Id").toString();
			String schoolCode=subTeaScoreSearchService.selectSchoolCodeByLoginName(loginName);
		    for(Map<String, Object> classMap : newClassList){
		       String classId=classMap.get("Class_Name").toString();
              for(Map<String, Object>  courseMap : courseList){
            	  String course=courseMap.get("Course_Code").toString();
            	  List<Map<String, Object>> siLvList=subTeaScoreSearchService.getSiLv(course, schoolCode, classId, gradeId);
            	  for (Map<String, Object> map : siLvList) {
            		    map.put("Yll", Math.round(parseDouble(map.get("Yll"))*100));
						map.put("Yl",Math.round(parseDouble(map.get("Yl"))*100));
						map.put("Ll",Math.round( parseDouble(map.get("Ll"))*100));
						map.put("Jgl", Math.round(parseDouble(map.get("Jgl"))*100));
						map.put("course", courseMap.get("Course_Name"));
						if(StringUtils.isNotBlank(classId) ){
							//处理班级
							String classIdOneStr=classId.substring(0, 1);
							if(classIdOneStr.equals("0")){
								classId=classId.substring(classId.length()-1);
								classId=newGradeMap.get("Grade_Name").toString()+"("+classId+")班";
							}
						}
						map.put("Class_Id", classId);
                	 list.add(map);
				}
              }

		    }
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("data", list);
		out.print(getSerializer().formatMap(resultMap));
	}
	
	/**
	 * @Title: getSiLv
	 * @Description: 四率为班主任
	 * @author xiahuajun
	 * @date 2016年9月12日 
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=getSiLvForClassRoomTeacher")
	public void getSiLvForClassRoomTeacher(java.io.PrintWriter out){
		//String username = SecurityContext.getPrincipal().getUsername();
    	//根据登录名查询teacher_pk
		String teacher_pk = userService.selectTeacherPkByLoginName(SecurityContext.getPrincipal().getUsername());
		//查询当前用户的学校code(根据登录名)
		String schoolCode = userService.selectSchoolCodeByLoginName(SecurityContext.getPrincipal().getUsername());
		//查询科任老师的年级班级科目stuCount=[{Count=4, Total_Score=60以下 , Class_Id=01}, {Count=11, Total_Score=61-70 , Class_Id=01}, {Count=10, Total_Score=71-80 , Class_Id=01}
		//[{Grade_Name=初一, Course_Name=语文, Teacher_Name=朱苏苹, Teacher_Pk=9BD6C39D-20F4-48EF-85F5-6314EAE4B2FD, Course_Code=yw, Class_Name=01, Grade_Id=17, Course_Id=0B2D3060-C01B-4CEF-B427-B00CA6226826, Class_Id=02B7322F-1FC1-47A1-98A4-3EB508E10B21}]
		List<Map<String, Object>> list = userService.selectGradeClassCourseByTeacherPk(teacher_pk);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		//Integer startNum = 50;
		for(Map<String, Object> map : list){
			map.put("schoolCode", schoolCode);
			map.put("examType", "qm");
			map.put("term", "xxq");
			map.put("schoolYear", "2015-2016");
			List<Map<String, Object>> stuCount = userService.selectsilvForClassRoomTeacher(map);
			paramMap.put("stuCount",stuCount);
		}
			
    	out.print(getSerializer().formatMap(paramMap));
		
	}
	
	/**
	 * @Title: getSiLvForClassRoomTeacher
	 * @Description:学校普通管理员查询四率 
	 * @author xiahuajun
	 * @date 2016年9月14日 
	 * @param out
	 * @return void
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(params = "command=getSiLvForSchoolPlainAdmin")
	public void getSiLvForSchoolPlainAdmin(java.io.PrintWriter out) throws UnsupportedEncodingException{
//		String id = SecurityContext.getPrincipal().getId();
//		//调用bua接口查询当前用户的学校code(根据用户id){meta={success=true, message=ok}, data=[{ORG_ID=3071}]}
//		String str = userService.selectSchoolCodeByUserId(id);
//		Map<String, Object> requestMap = getSerializer().parseMap(str);
//		List<Map<String,Object>> list= (List<Map<String, Object>>) requestMap.get("data");
//		String schoolCode = list.get(0).get("ORG_ID").toString();
		//查询科任老师的年级班级科目stuCount=[{Count=4, Total_Score=60以下 , Class_Id=01}, {Count=11, Total_Score=61-70 , Class_Id=01}, {Count=10, Total_Score=71-80 , Class_Id=01}
		//[{Grade_Name=初一, Course_Name=语文, Teacher_Name=朱苏苹, Teacher_Pk=9BD6C39D-20F4-48EF-85F5-6314EAE4B2FD, Course_Code=yw, Class_Name=01, Grade_Id=17, Course_Id=0B2D3060-C01B-4CEF-B427-B00CA6226826, Class_Id=02B7322F-1FC1-47A1-98A4-3EB508E10B21}]
		//List<Map<String, Object>> list = userService.selectGradeClassCourseByTeacherPk(teacher_pk);
		//根据登录名查询schoolCode
		String loginName=SecurityContext.getPrincipal().getUsername();
		String schoolCode = examNumberManageService.getSchoolCodeByLoginName(loginName);

		Map<String, Object> paramMap = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
			map.put("schoolCode", schoolCode);
			map.put("examType", "qm");
			map.put("term", "xxq");
			map.put("schoolYear", "2015-2016");
			List<Map<String, Object>> stuCount = userService.selectsilvForSchoolPlainAdmin(map);
			paramMap.put("stuCount",stuCount);
    	out.print(getSerializer().formatMap(paramMap));
		
	}
	 @Autowired
     private SubTeaScoreSearchService subTeaScoreSearchService;
	 @Autowired
     private TeacherManagementService teaManagementService;
	 @Autowired
	 private PlatformUserService userService;
	 @Autowired
	 private ExamNumberManageService examNumberManageService;
}
