package data.academic.schoolQualityAnalysis.controller;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;

import data.academic.examNumberManagement.service.ExamNumberManageService;
import data.academic.schoolQualityAnalysis.service.SchoolScoreService;
import data.academic.schoolQualityAnalysis.service.ScoreBetweenClassScoreService;
import data.academic.util.ExportUtil;
import data.framework.support.AbstractBaseController;
import data.framework.support.ConfigContext;
import data.framework.utility.ArithmeticUtil;
import data.platform.authority.security.SecurityContext;
@Controller
@RequestMapping("schoolQualityAnalysis/scoreBetweenTeacher")
public class ScoreBetweenTeacherController extends AbstractBaseController{

	@Override
	protected void init(ModelMap model, HttpServletRequest request) {
	}
	@Autowired
	private ScoreBetweenClassScoreService scoreBetweenClassScoreService;
	@Autowired
	private SchoolScoreService schoolScoreService;
	@Autowired
	private ExamNumberManageService examNumberManageService;
	
	@RequestMapping(params = "command=analyzeScoreBetweenTeacher")
	@ResponseBody
	public JSONObject analyzeScoreBetweenTeacher(@RequestParam("data") String data){
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		JSONObject jo =new JSONObject();
		String loginName=SecurityContext.getPrincipal().getUsername();
		List<String> course = new ArrayList<>();
		course.add(scoreBetweenClassScoreService.getCourseByTeacher(loginName));
		requestMap.put("course", course);
		DecimalFormat df = new DecimalFormat("######0.00");
		String schoolCode = "";
		if("qpAdmin".equals(SecurityContext.getPrincipal().getUsername())){
			schoolCode = ConfigContext.getValue("framework.tmis.roleCode['qpOrgCode']");
		} else {
			schoolCode = examNumberManageService.getSchoolCodeByLoginName(loginName);
		}
		requestMap.put("schoolCode", schoolCode);
		//根据老师和选定的年纪得到该年级其他所有班级，和在该年级同门课带课的老师
		List<HashMap<String,String>> classNameAndTeacherName = scoreBetweenClassScoreService.getClassNameAndTeacherName(requestMap);
		Map<Integer,String> gradeMap  =new HashMap();
		gradeMap.put(1, "E");gradeMap.put(2, "D");gradeMap.put(3, "C");gradeMap.put(4, "B");gradeMap.put(5, "A");gradeMap.put(6, "A");
		List<Map> mapList = new ArrayList<>();
		
		if(classNameAndTeacherName.size()>0){
			List<HashMap<String,String>> courseExaminfoList =schoolScoreService.getCourseExaminfoList(requestMap);
			if(courseExaminfoList.size()<1){
				jo.put("message", "faile");
				return jo;
			}
			int zf = parseInteger(courseExaminfoList.get(0).get("Exam_Zf"));
			int A= zf/5*4;
			int B= zf/5*3;
			int C= zf/5*2;
			int D= zf/5*1;
			int E= 0;
			for(HashMap<String,String> temp:classNameAndTeacherName){
				Map<String,Object> testMap = new HashMap<>();
				List<String> classCode = new ArrayList<>();
				classCode.add(temp.get("Class_Pk"));
				requestMap.put("classCode", classCode);
				List<Map<String,Object>> classesScoreList = scoreBetweenClassScoreService.getClassesScoreList(requestMap);
				if(classesScoreList.size()<1){
					continue;
				}
				testMap.put("teacherName", temp.get("Teacher_Name"));
				testMap.put("classSize", classesScoreList.size());
				testMap.put("className", temp.get("Class_Name"));
				int aSize=0;int bSize=0;int cSize=0;int dSize=0;int eSize=0;
				for(Map<String,Object> scoreMap: classesScoreList){
					if(parseInteger(scoreMap.get("totalScore"))>=A){
						aSize++;
					}else if(parseInteger(scoreMap.get("totalScore"))<A&&parseInteger(scoreMap.get("totalScore"))>=B){
						bSize++;
					}else if(parseInteger(scoreMap.get("totalScore"))<B&&parseInteger(scoreMap.get("totalScore"))>=C){
						cSize++;
					}else if(parseInteger(scoreMap.get("totalScore"))<C&&parseInteger(scoreMap.get("totalScore"))>=D){
						dSize++;
					}else{
						eSize++;
					}
				}
				testMap.put("A", aSize);
				testMap.put("B", bSize);
				testMap.put("C", cSize);
				testMap.put("D", dSize);
				testMap.put("E", eSize);
				testMap.put("Abfb", df.format(parseDouble(aSize)*100/classesScoreList.size()));
				testMap.put("Bbfb", df.format(parseDouble(bSize)*100/classesScoreList.size()));
				testMap.put("Cbfb", df.format(parseDouble(cSize)*100/classesScoreList.size()));
				testMap.put("Dbfb", df.format(parseDouble(dSize)*100/classesScoreList.size()));
				testMap.put("Ebfb", df.format(parseDouble(eSize)*100/classesScoreList.size()));
				mapList.add(testMap);
			}
		}
		jo.put("mapList", mapList);
		return jo;
	}
	
	@RequestMapping(params = "command=analyzeScoreBetweenTeacherImport")
	@ResponseBody
	public void analyzeScoreBetweenTeacherImport(@RequestParam("data") String data,HttpServletResponse response){
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		JSONObject jo =new JSONObject();
		String loginName=SecurityContext.getPrincipal().getUsername();
		List<String> course = new ArrayList<>();
		course.add(scoreBetweenClassScoreService.getCourseByTeacher(loginName));
		requestMap.put("course", course);
		DecimalFormat df = new DecimalFormat("######0.00");
		String schoolCode = "";
		if("qpAdmin".equals(SecurityContext.getPrincipal().getUsername())){
			schoolCode = ConfigContext.getValue("framework.tmis.roleCode['qpOrgCode']");
		} else {
			schoolCode = examNumberManageService.getSchoolCodeByLoginName(loginName);
		}
		requestMap.put("schoolCode", schoolCode);
		//根据老师和选定的年纪得到该年级其他所有班级，和在该年级同门课带课的老师
		List<HashMap<String,String>> classNameAndTeacherName = scoreBetweenClassScoreService.getClassNameAndTeacherName(requestMap);
		Map<Integer,String> gradeMap  =new HashMap();
		gradeMap.put(1, "E");gradeMap.put(2, "D");gradeMap.put(3, "C");gradeMap.put(4, "B");gradeMap.put(5, "A");gradeMap.put(6, "A");
		List<Map<String,Object>> mapList = new ArrayList<>();
		
		if(classNameAndTeacherName.size()>0){
			List<HashMap<String,String>> courseExaminfoList =schoolScoreService.getCourseExaminfoList(requestMap);
			if(courseExaminfoList.size()<1){
				jo.put("message", "faile");
				//return jo;
			}
			int zf = parseInteger(courseExaminfoList.get(0).get("Exam_Zf"));
			int A= zf/5*4;
			int B= zf/5*3;
			int C= zf/5*2;
			int D= zf/5*1;
			int E= 0;
			for(HashMap<String,String> temp:classNameAndTeacherName){
				Map<String,Object> testMap = new HashMap<>();
				List<String> classCode = new ArrayList<>();
				classCode.add(temp.get("Class_Pk"));
				requestMap.put("classCode", classCode);
				List<Map<String,Object>> classesScoreList = scoreBetweenClassScoreService.getClassesScoreList(requestMap);
				if(classesScoreList.size()<1){
					continue;
				}
				testMap.put("teacherName", temp.get("Teacher_Name"));
				testMap.put("classSize", classesScoreList.size());
				testMap.put("className", temp.get("Class_Name"));
				int aSize=0;int bSize=0;int cSize=0;int dSize=0;int eSize=0;
				for(Map<String,Object> scoreMap: classesScoreList){
					if(parseInteger(scoreMap.get("totalScore"))>=A){
						aSize++;
					}else if(parseInteger(scoreMap.get("totalScore"))<A&&parseInteger(scoreMap.get("totalScore"))>=B){
						bSize++;
					}else if(parseInteger(scoreMap.get("totalScore"))<B&&parseInteger(scoreMap.get("totalScore"))>=C){
						cSize++;
					}else if(parseInteger(scoreMap.get("totalScore"))<C&&parseInteger(scoreMap.get("totalScore"))>=D){
						dSize++;
					}else{
						eSize++;
					}
				}
				testMap.put("A", aSize);
				testMap.put("B", bSize);
				testMap.put("C", cSize);
				testMap.put("D", dSize);
				testMap.put("E", eSize);
				testMap.put("Abfb", df.format(parseDouble(aSize)*100/classesScoreList.size()));
				testMap.put("Bbfb", df.format(parseDouble(bSize)*100/classesScoreList.size()));
				testMap.put("Cbfb", df.format(parseDouble(cSize)*100/classesScoreList.size()));
				testMap.put("Dbfb", df.format(parseDouble(dSize)*100/classesScoreList.size()));
				testMap.put("Ebfb", df.format(parseDouble(eSize)*100/classesScoreList.size()));
				mapList.add(testMap);
			}
		}
		jo.put("mapList", mapList);
		for(Map<String,Object> map:mapList){
			map.put("A",formatString(map.get("Abfb"))+"%("+formatString(map.get("A"))+"人)");
			map.put("B",formatString(map.get("Bbfb"))+"%("+formatString(map.get("B"))+"人)");
			map.put("C",formatString(map.get("Cbfb"))+"%("+formatString(map.get("C"))+"人)");
			map.put("D",formatString(map.get("Dbfb"))+"%("+formatString(map.get("D"))+"人)");
			map.put("E",formatString(map.get("Ebfb"))+"%("+formatString(map.get("E"))+"人)");
		}
		//return jo;
		String fileName="班间成绩对比.xls";
		String sheetName="班间成绩列表";
		String[] title={"序号","班级","教师","人数","A","B","C","D","E"};
		String[] key={"xh","className","teacherName","classSize","A","B","C","D","E"};
		ExportUtil.ExportExcel(response, title, fileName, sheetName, mapList, key);
	}
	

}
