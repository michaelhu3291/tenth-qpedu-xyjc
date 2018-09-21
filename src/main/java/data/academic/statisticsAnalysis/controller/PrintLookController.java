package data.academic.statisticsAnalysis.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import data.academic.examNumberManagement.service.ExamNumberManageService;
import data.academic.statisticsAnalysis.service.DistrictSubjectInstructorService;
import data.academic.statisticsAnalysis.service.ScoreSearchService;
import data.academic.teacherManagement.service.TeacherManagementService;
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
@RequestMapping("statisticsAnalysis/printLook")
public class PrintLookController extends AbstractBaseController1 {
	@RequestMapping
	protected ModelAndView initialize(ModelMap model, HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException{
		String loginName = SecurityContext.getPrincipal().getUsername();
		//根据登录名查询角色Code
		String roleCode = teacherManagementService.selectRoleCodeByLoginName(loginName);
		if("schoolPlainAdmin".equals(roleCode) || "schoolAdmin".equals(roleCode)) {
			request.getRequestDispatcher("/statisticsAnalysis/printLook.do").forward(request,response);
		}
		else if(roleCode.equals(ConfigContext.getValue("framework.tmis.roleCode['qpRoleAdminCode']")) || roleCode.equals(ConfigContext.getValue("framework.tmis.roleCode['qpRoleQuAdminCode']"))) {
			request.getRequestDispatcher("/statisticsAnalysis/printLook.do").forward(request,response);
			
		}
		else if("instructor".equals(roleCode)) {
			request.getRequestDispatcher("/statisticsAnalysis/printLook.do").forward(request,response);
			
		}
		
		return new ModelAndView();
		
	}
	/**
	 * @Title: searchScoreForqpAdmin
	 * @Description: 青浦超级管理员打印预览
	 * @author huchuanhu
	 * @date 2016年10月27日 
	 * @param request
	 * @param response
	 * @return ModelAndView
	 * @throws IOException 
	 * @throws ServletException 
	 */
	@RequestMapping(params = "command=searchScoreForqpAdmin")
	public ModelAndView searchScoreForqpAdmin(HttpServletRequest request, HttpServletResponse response,java.io.PrintWriter out) throws ServletException, IOException {
		String schoolYear = request.getParameter("schoolYear"); 
		String school = request.getParameter("school");
		//String school1=school;
		//System.out.println("==="+school1);
		String grade = request.getParameter("grade"); 
		String term = request.getParameter("term"); 
		String idx=request.getParameter("idx");
		String so=request.getParameter("so");
		String examNumber=request.getParameter("examNum");
		String examType = request.getParameter("examType"); 
		String examNumberOrStuCode = request.getParameter("examNumberOrStuCode");
		String courseVal = request.getParameter("courseVal");
		String scoreHtml=request.getParameter("scoreHtml");
		//System.out.println("====courseVal===="+courseVal);
		String courseTxt = request.getParameter("courseTxt");
		//System.out.println("====courseTxt===="+courseTxt);
		String gradeTxt=request.getParameter("gradeTxt");
		List<String> courseName=new ArrayList<>();
		List<String> courseValue=new ArrayList<>();
		String[] sourceStrArray = courseTxt.split(",");
		String[] sourceStrArrayVal = courseVal.split(",");
		for(int i=0;i<sourceStrArray.length;i++){
			courseName.add(sourceStrArray[i]);
			courseValue.add(sourceStrArrayVal[i]);
		}
		String loginName=SecurityContext.getPrincipal().getUsername();
		String schoolCode = null;
		String roleType = null; //登录角色类型
		String examCode1 = null;//考试编号
		String roleCode = teacherManagementService.selectRoleCodeByLoginName(loginName);
		if("schoolPlainAdmin".equals(roleCode) || "schoolAdmin".equals(roleCode)) {
			schoolCode = examNumberManageService.getSchoolCodeByLoginName(loginName);
			roleType = "school";
			examCode1 = examNumber;
		}else{
			roleType = "district";
		}
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
		Map<String, Object> map=new HashMap<>();
		map.put("schoolYear", schoolYear);
		map.put("grade", grade);
		map.put("term", term);
		map.put("stuCode", examNumberOrStuCode);
		map.put("examNumber", examNumberOrStuCode);
		//学校类型
		map.put("schoolType", schoolType);
		/*if(schoolCode!=null){
			map.put("schoolCode", schoolCode);
		}*/
		ArrayList<String> schoolList=new ArrayList<>();
		if(!"null".equals(school)&&school!=null){
			String[] schoolArray = school.split(",");
			for(int i=0;i<schoolArray.length;i++){
				schoolList.add(schoolArray[i]);
			}
			map.put("school", schoolList);
		}
		
		map.put("schoolCode", schoolCode);
		map.put("examType", examType);
		map.put("courseVal", courseVal);
		map.put("idx", idx);
		map.put("so", so);
		if(examNumber!=null){
			map.put("examCode", examNumber);
		}
		//System.out.println("===map==="+map);
		List<Map<String, Object>> pagingResult = scoreSearchService.searchScoreForqpAdminPage(map);
		
		

	//	List<Map<String, Object>> list = pagingResult.getRows();
		//System.out.println("===pagingResult==="+pagingResult);
		ModelAndView mvAndView ;
		/*if(schoolType.equals("xx")){
			mvAndView = new ModelAndView("/statisticsAnalysis/printLookcz");
		}else if(schoolType.equals("cz")){
			mvAndView = new ModelAndView("/statisticsAnalysis/printLookcz");
		}else {
			mvAndView = new ModelAndView("/statisticsAnalysis/printLookcz");
		}*/
		mvAndView = new ModelAndView("/statisticsAnalysis/printLookcz");
		if("schoolPlainAdmin".equals(roleCode) || "schoolAdmin".equals(roleCode)) {
			mvAndView = new ModelAndView("/statisticsAnalysis/printLookczSchoolAdmin");
		}else{
			mvAndView = new ModelAndView("/statisticsAnalysis/printLookcz");
		}
		if(pagingResult.size()>0){
			for(int i=0;i<pagingResult.size();i++){
				switch ((String)pagingResult.get(i).get("Class_Id")) {
					case "01":
						pagingResult.get(i).put("Class_Id", "(1)班");
					break;
					case "02":
						pagingResult.get(i).put("Class_Id", "(2)班");
					break;
					case "03":
						pagingResult.get(i).put("Class_Id", "(3)班");
					break;
					case "04":
						pagingResult.get(i).put("Class_Id", "(4)班");
					break;
					case "05":
						pagingResult.get(i).put("Class_Id", "(5)班");
					break;
					case "06":
						pagingResult.get(i).put("Class_Id", "(6)班");
					break;
					case "07":
						pagingResult.get(i).put("Class_Id", "(7)班");
					break;
					case "08":
						pagingResult.get(i).put("Class_Id", "(8)班");
					break;
					case "09":
						pagingResult.get(i).put("Class_Id", "(9)班");
					break;
					case "10":
						pagingResult.get(i).put("Class_Id", "(10)班");
					break;
					default:
				}
			}
		}
		mvAndView.addObject("pagingResult",pagingResult);
		mvAndView.addObject("courseName",courseName);
		mvAndView.addObject("courseVal",courseValue);
		
		/*for(Map<String, Object> signResult:pagingResult){
			for(String course:courseValue){
				signResult.put(course, signResult.get(course));
			}
		}*/
		request.setAttribute("courseName", courseName);
		request.setAttribute("courseVal", courseValue);
		request.setAttribute("pagingResult", pagingResult);
		request.setAttribute("scoreHtml", scoreHtml);
		request.setAttribute("gradeTxt", gradeTxt);
		return mvAndView;
		//request.getRequestDispatcher("/statisticsAnalysis/printLook.do").forward(request,response);
		 //out.print(getSerializer().formatList(pagingResult));
	}
	
	/**
	 * @Title: searchScoreForTeacher
	 * @Description: 教师打印预览
	 * @author huchuanhu
	 * @date 2016年11月22日 
	 * @param request
	 * @param response
	 * @return ModelAndView
	 * @throws IOException 
	 * @throws ServletException 
	 */
	@RequestMapping(params = "command=searchScoreForTeacher")
	public ModelAndView searchScoreForTeacher(HttpServletRequest request, HttpServletResponse response,java.io.PrintWriter out) throws ServletException, IOException {
		String datas=request.getParameter("data");
    	Map<String,Object> requestMap = this.getSerializer().parseMap(datas) ;
    	//System.out.println("====requestMap===="+requestMap);
    	String grade=(String) requestMap.get("grade");
		List<String> classList = (List<String>) requestMap.get("classArr");
		List<Map<String,Object>> list1 = new ArrayList<>();
		for(String str : classList) {
			Map<String,Object> map = new HashMap<String,Object>();
			String[] arr = str.split(",");
			map.put("grade", arr[0]);
			map.put("class", arr[1]);
			list1.add(map);
		}
		String loginName = SecurityContext.getPrincipal().getUsername();
		String schoolCode = examNumberManageService.getSchoolCodeByLoginName(loginName);
		String stuCode = trimString(requestMap.get("examNumberOrStuCode").toString());
		String examNumber = trimString(requestMap.get("examNumberOrStuCode").toString());
		String gradeTxt= trimString(requestMap.get("gradeTxt").toString());
		String index=trimString(requestMap.get("idx").toString());
    	String sortOrder=trimString(requestMap.get("so").toString());
    	String scoreHtml=trimString(requestMap.get("scoreHtml").toString());
    	List<String> courseName=new ArrayList<>();
		List<String> courseValue=new ArrayList<>();
		String sourceStrArray = trimString(requestMap.get("courseTxt").toString());
		String sourceStrArrayVal = trimString(requestMap.get("course").toString());
		courseName.add(sourceStrArray);
		courseValue.add(sourceStrArrayVal);
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
		requestMap.put("schoolCode", schoolCode);
		requestMap.put("schoolType", schoolType);
		requestMap.put("stuCode", stuCode);
		requestMap.put("examNumber", examNumber);
		requestMap.put("list1", list1);
		requestMap.put("index", index);
		requestMap.put("sortOrder", sortOrder);
		List<Map<String, Object>> pagingResult = scoreSearchService.searchScoreForTeacherPage(requestMap);
		//System.out.println("===pagingResult==="+pagingResult);

		
		ModelAndView mvAndView ;
		mvAndView = new ModelAndView("/statisticsAnalysis/printLookczTeacher");
		if(pagingResult.size()>0){
			for(int i=0;i<pagingResult.size();i++){
				switch ((String)pagingResult.get(i).get("Class_Id")) {
					case "01":
						pagingResult.get(i).put("Class_Id", "(1)班");
					break;
					case "02":
						pagingResult.get(i).put("Class_Id", "(2)班");
					break;
					case "03":
						pagingResult.get(i).put("Class_Id", "(3)班");
					break;
					case "04":
						pagingResult.get(i).put("Class_Id", "(4)班");
					break;
					case "05":
						pagingResult.get(i).put("Class_Id", "(5)班");
					break;
					case "06":
						pagingResult.get(i).put("Class_Id", "(6)班");
					break;
					case "07":
						pagingResult.get(i).put("Class_Id", "(7)班");
					break;
					case "08":
						pagingResult.get(i).put("Class_Id", "(8)班");
					break;
					case "09":
						pagingResult.get(i).put("Class_Id", "(9)班");
					break;
					case "10":
						pagingResult.get(i).put("Class_Id", "(10)班");
					break;
					default:
				}
			}
		}
		mvAndView.addObject("pagingResult",pagingResult);
		mvAndView.addObject("courseName",courseName);
		mvAndView.addObject("courseVal",courseValue);
		request.setAttribute("courseName", courseName);
		request.setAttribute("courseVal", courseValue);
		request.setAttribute("pagingResult", pagingResult);
		request.setAttribute("scoreHtml", scoreHtml);
		request.setAttribute("gradeTxt", gradeTxt);
		return mvAndView;
	}
	
	@Autowired
	private ScoreSearchService scoreSearchService;
	@Autowired
	private TeacherManagementService teacherManagementService;
	@Autowired
	private ExamNumberManageService examNumberManageService;

}