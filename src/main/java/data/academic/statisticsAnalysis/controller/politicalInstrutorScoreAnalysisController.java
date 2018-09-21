/**
 * 2016年9月12日
 */
package data.academic.statisticsAnalysis.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import data.academic.examNumberManagement.service.ExamNumberManageService;
import data.academic.statisticsAnalysis.service.DistrictSubjectInstructorService;
import data.academic.statisticsAnalysis.service.SchoolPlainAdminScoreSearchService;
import data.academic.teacherManagement.service.TeacherManagementService;
import data.framework.support.AbstractBaseController1;
import data.framework.support.ConfigContext;
import data.platform.authority.security.SecurityContext;
import data.platform.service.PlatformDataDictionaryService;

/**
 * @Title: politicalInstrutorScoreAnalysisController
 * @Description: 教导员成绩查询控制层
 * @author xiahuajun
 * @date 2016年10月8日
 */
@RestController
@RequestMapping("statisticsAnalysis/politicalInstructor")
public class politicalInstrutorScoreAnalysisController extends AbstractBaseController1 {

	
	@RequestMapping
	protected ModelAndView initialize(ModelMap model, HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException{
		String loginName = SecurityContext.getPrincipal().getUsername();
		//根据登录名查询角色Code
		String roleCode = teacherManagementService.selectRoleCodeByLoginName(loginName);
		if("instructor".equals(roleCode)) {
			request.getRequestDispatcher("/statisticsAnalysis/instructorScoreAnalysisBySiLv.do").forward(request,response);
		}
		else if(roleCode.equals(ConfigContext.getValue("framework.tmis.roleCode['qpRoleAdminCode']")) || roleCode.equals(ConfigContext.getValue("framework.tmis.roleCode['qpRoleQuAdminCode']"))) {
			request.getRequestDispatcher("/statisticsAnalysis/subjectInstructorScoreAnalysis.do").forward(request,response);
			
		}
		else if("teacher".equals(roleCode)) {
			request.getRequestDispatcher("/statisticsAnalysis/teacherSiLv.do").forward(request,response);
			
		}
		else if("schoolAdmin".equals(roleCode)) {
			request.getRequestDispatcher("/statisticsAnalysis/schoolAdmin.do").forward(request,response);
			
		}
		return new ModelAndView();
	}
	/**
	 * @Title: getSubjectInstrutorsAvg
	 * @Description: 教导员查询平均分
	 * @author xiahuajun
	 * @date 2016年10月8日 
	 * @param data
	 * @param out
	 * @return void
	 */
	//@RequestMapping(params = "command=getSubjectInstrutorsAvg")
	/*public void getSubjectInstrutorsAvg(@RequestParam("data") String data,java.io.PrintWriter out) {
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		//schoolYear=2015-2016, term=xxq, gradeId=16, school=3071}
		String course="yw";
		List<Map<String, Object>> cousreList=districtSubjectInstructorService.getCourseByLoginName(SecurityContext.getPrincipal().getUsername());
			for (Map<String, Object> map : cousreList) {
				if(map!=null){
					if(map.get("COUSRE")!=null){
						 course=map.get("COURSE").toString(); 
					}else{
						course=course;					}
				
				}
				  
		}
	
		List<Map<String, Object>> list=new ArrayList<>();
		
		   Map<String, Object> paramMap = new HashMap<String, Object>();
		
				   if(requestMap.containsKey("schoolYear")){
					   paramMap.put("schoolYear",requestMap.get("schoolYear"));
					}else{
						paramMap.put("schoolYear", "2016-2017");
					}
				   if(requestMap.containsKey("term")){
					   paramMap.put("term",requestMap.get("term"));
				   }else{
						paramMap.put("term", "xxq");
				   }
				   
				 //根据登录名查询schoolCode
					String loginName=SecurityContext.getPrincipal().getUsername();
					String schoolCode = examNumberManageService.getSchoolCodeByLoginName(loginName);
					String schoolSequence = schoolPlainAdminScoreSearchService.findSchoolTypeBySchoolCode(schoolCode);
					if("0".equals(schoolSequence)){
						paramMap.put("schoolTypeCode","xx");
						paramMap.put("schoolTypeName","小学");
					}
					else if("1".equals(schoolSequence)){
						paramMap.put("schoolTypeCode","cz");
						paramMap.put("schoolTypeName","初中");
					}
					else if("2".equals(schoolSequence)){
						paramMap.put("schoolTypeCode","gz");
						paramMap.put("schoolTypeName","高中");
					}
					else if("3".equals(schoolSequence)){
						paramMap.put("schoolTypeCode","gz");
						paramMap.put("schoolTypeName","高中");
					}
					else if("2".equals(schoolSequence) || "3".equals(schoolSequence) || "4".equals(schoolSequence)  ){
						if("3062".equals(schoolCode) ){
							paramMap.put("schoolTypeCode","cz");
							paramMap.put("schoolTypeName","初中");
						}
					
						paramMap.put("schoolTypeCode","gz");
						paramMap.put("schoolTypeName","高中");
					}
					//根据学校类型查询年级
					List<Map<String,Object>> paramList = dictionaryService.getGradesBySchoolType(paramMap.get("schoolTypeCode").toString());
					if(requestMap.containsKey("gradeId")){
			        	   paramMap.put("gradeId", requestMap.get("gradeId").toString());
			           }else{
			        	   paramMap.put("gradeId", paramList.get(0).get("DictionaryCode").toString());
			           }
					
					paramMap.put("school", schoolCode);
				   //if(requestMap.containsKey("schoolType")){
					//   paramMap.put("schoolType", requestMap.get("schoolType"));
				  // }
				if(requestMap.containsKey("course")){
					course=requestMap.get("course").toString();
					  paramMap.put("course", course);
				}else{
					paramMap.put("course", course);
				}
					List<Map<String, Object>> avgList=districtSubjectInstructorService.getSubjectPoliticalInstrutorsAvg(paramMap);
		            if(avgList.size()>0){
		            	for (Map<String, Object> map : avgList) {
			            	 map.put("classId", map.get("Class_Id"));
			            	 map.put("qmAvg", map.get("qmAvg"));
			            	 map.put("qzAvg", map.get("qzAvg"));
			            	 list.add(map);
						}
		            
			}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("course", course);
		resultMap.put("gradeId", paramList.get(0).get("DictionaryCode").toString());
		resultMap.put("data", list);
		out.print(getSerializer().formatMap(resultMap));
	}*/
	
	/**
	 * @Title: getSubjectInstrutorsSiLv
	 * @Description: 教导员查询四率
	 * @author xiahuajun
	 * @date 2016年10月8日 
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=getSubjectInstrutorsSiLv")
	public void getSubjectInstrutorsSiLv(@RequestParam("data") String data,java.io.PrintWriter out) {
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		String state = null;
		if(requestMap.containsKey("state")){
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
				if(map.get("COUSRE")!=null){
					 course=map.get("COURSE").toString(); 
				}else{
					course=course;					
					}
			}
		  
		}
		
		
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
		   		   
		   
		           if(requestMap.containsKey("course")){
		        	   course=requestMap.get("course").toString();
		        	   paramMap.put("course", course);
		           }else{
		        	   paramMap.put("course", course);
		           }
		           
		           if(requestMap.containsKey("schoolYear")){
		        	   paramMap.put("schoolYear", requestMap.get("schoolYear").toString());
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
					String loginName=SecurityContext.getPrincipal().getUsername();
					String schoolCode = examNumberManageService.getSchoolCodeByLoginName(loginName);
					String schoolSequence = schoolPlainAdminScoreSearchService.findSchoolTypeBySchoolCode(schoolCode);
					if("0".equals(schoolSequence)){
						paramMap.put("schoolTypeCode","xx");
						paramMap.put("schoolTypeName","小学");
					}
					else if("1".equals(schoolSequence)){
						paramMap.put("schoolTypeCode","cz");
						paramMap.put("schoolTypeName","初中");
					}
					else if("2".equals(schoolSequence) || "3".equals(schoolSequence) || "4".equals(schoolSequence)  ){
						if("3062".equals(schoolCode) ){
							paramMap.put("schoolTypeCode","cz");
							paramMap.put("schoolTypeName","初中");
						}
						if("3004".equals(schoolCode) ){
							paramMap.put("schoolTypeCode","wz");
							paramMap.put("schoolTypeName","完中");
						}
					}else if("5".equals(schoolSequence)){
						paramMap.put("schoolTypeCode","ygz");
						paramMap.put("schoolTypeName","一贯制");
				 }
				
					//根据学校类型查询年级
					List<Map<String,Object>> paramList = dictionaryService.getGradesBySchoolType(paramMap.get("schoolTypeCode").toString());
					if(requestMap.containsKey("gradeId")){
			        	   paramMap.put("gradeId", requestMap.get("gradeId").toString());
			           }else{
			        	   paramMap.put("gradeId",paramList.get(0).get("DictionaryCode").toString());
			           }
					
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
					List<Map<String, Object>> siLvList=districtSubjectInstructorService.getSubjectPoliticalInstrutorsSiLv(paramMap);
					List<Map<String, Object>> list=new ArrayList<>();
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
			//course=yw, schoolYear=2016-2017, term=xxq, gradeId=16, examType=qm, state=sbjd
			resultMap.put("data", list);
			resultMap.put("course", course);
			resultMap.put("gradeId",paramMap.get("gradeId").toString());
			resultMap.put("schoolYear",paramMap.get("schoolYear").toString());
			resultMap.put("term",paramMap.get("term").toString());
			resultMap.put("examType",paramMap.get("examType").toString());
			resultMap.put("state",state);
			out.print(getSerializer().formatMap(resultMap));
	}
	
	/**
	 * @Title: getCoursesBySchoolType
	 * @Description: 根据学校类型得到科目
	 * @author xiahuajun
	 * @date 2016年10月11日 
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=getCoursesBySchoolType")
	public void getCoursesBySchoolType(@RequestParam("data") String data,java.io.PrintWriter out) {
		//Map<String, Object> requestMap = getSerializer().parseMap(data);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		   
        //根据登录名查询schoolCode
		String loginName=SecurityContext.getPrincipal().getUsername();
		String schoolCode = examNumberManageService.getSchoolCodeByLoginName(loginName);
		String schoolSequence = schoolPlainAdminScoreSearchService.findSchoolTypeBySchoolCode(schoolCode);
		if("0".equals(schoolSequence)){
			paramMap.put("schoolTypeCode","xx");
			paramMap.put("schoolTypeName","小学");
		}
		else if("1".equals(schoolSequence)){
			paramMap.put("schoolTypeCode","cz");
			paramMap.put("schoolTypeName","初中");
		}
		else if("2".equals(schoolSequence) || "3".equals(schoolSequence) || "4".equals(schoolSequence)  ){
			if("3062".equals(schoolCode) ){
				paramMap.put("schoolTypeCode","cz");
				paramMap.put("schoolTypeName","初中");
				
			}else if("3004".equals(schoolCode)){
				paramMap.put("schoolTypeCode","wz");
				paramMap.put("schoolTypeName","完中");
			}else{
				paramMap.put("schoolTypeCode","gz");
				paramMap.put("schoolTypeName","高中");
			}
			
		}else if("5".equals(schoolSequence)){
			paramMap.put("schoolTypeCode","ygz");
			paramMap.put("schoolTypeName","一贯制");
		}
		String schoolTypeCode=formatString(paramMap.get("schoolTypeCode"));
		//根据学校类型查询科目
		List<Map<String,Object>> paramList = dictionaryService.getCoursesBySchoolType(schoolTypeCode);
		//paramMap.put("paramList", paramList);			
		out.print(getSerializer().formatList(paramList));
	}
	@Autowired
	private DistrictSubjectInstructorService districtSubjectInstructorService;
	@Autowired
	private ExamNumberManageService examNumberManageService;
	@Autowired
	private SchoolPlainAdminScoreSearchService schoolPlainAdminScoreSearchService;
	@Autowired
	private PlatformDataDictionaryService dictionaryService;
	@Autowired
	private TeacherManagementService teacherManagementService;
}


