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
@RequestMapping("schoolQualityAnalysis/scoreBetweenClass")
public class ScoreBetweenClassController extends AbstractBaseController{

	@Override
	protected void init(ModelMap model, HttpServletRequest request) {
	}
	@Autowired
	private ScoreBetweenClassScoreService scoreBetweenClassScoreService;
	@Autowired
	private SchoolScoreService schoolScoreService;
	@Autowired
	private ExamNumberManageService examNumberManageService;
	
	@RequestMapping(params = "command=analyzeScoreBetweenClass")
	@ResponseBody
	public JSONObject analyzePersonalSamllScore(@RequestParam("data") String data,HttpServletResponse response){
		HashMap<String,String> staticMap = new HashMap();
		//TODO 改成enumMap工具类
		staticMap.put("yw","语文");staticMap.put("sx","数学");staticMap.put("yy","外语");staticMap.put("wl","物理");staticMap.put("hx","化学");staticMap.put("ty","体育");staticMap.put("sxzz","思想政治");
		staticMap.put("ls","历史");staticMap.put("dl","地理");staticMap.put("kx","科学");staticMap.put("ms","美术");staticMap.put("tzxkc","拓展型课程");staticMap.put("xxkj","信息科技");
		staticMap.put("yjxkc","研究型课程");staticMap.put("yyue","音乐");staticMap.put("njyy","牛津英语");staticMap.put("sw","生物");staticMap.put("xsjyy","高中新世纪英语");staticMap.put("zr","自然");
		staticMap.put("qm","期末");staticMap.put("qz","期中");staticMap.put("sxq","上学期");staticMap.put("xxq","下学期");
		DecimalFormat df = new DecimalFormat("######0.00");
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		JSONObject jo =new JSONObject();
		String loginName=SecurityContext.getPrincipal().getUsername();
		List<String> course = new ArrayList<>();
		course.add(scoreBetweenClassScoreService.getCourseByTeacher(loginName));
		requestMap.put("course", course);
		List<Map<String,Object>> classesScoreList = scoreBetweenClassScoreService.getClassesScoreList(requestMap);
		List<HashMap> classList = new ArrayList<>();
		Map<Integer,String> gradeMap  =new HashMap();
		gradeMap.put(1, "E");gradeMap.put(2, "D");gradeMap.put(3, "C");gradeMap.put(4, "B");gradeMap.put(5, "A");gradeMap.put(6, "A");
		if(classesScoreList.size()>0){
			List<HashMap<String,String>> courseExaminfoList =schoolScoreService.getCourseExaminfoList(requestMap);
			int zf = parseInteger(courseExaminfoList.get(0).get("Exam_Zf"));
			int A= zf/5*4;
			int B= zf/5*3;
			int C= zf/5*2;
			int D= zf/5*1;
			int E= 0;
			ArrayList<String> classCode  = (ArrayList<String>) requestMap.get("classCode");
			ArrayList<String> className  = (ArrayList<String>) requestMap.get("className");
			for(int i=0;i<classCode.size();i++){
				HashMap<String,Object> classMap = new HashMap<>();
				ArrayList<Map> classScoreList = new ArrayList<>();
				for(Map<String,Object> studentMap :classesScoreList){
					if(classCode.get(i).equals(studentMap.get("classId"))){
						classScoreList.add(studentMap);
					}
				}
				if(classScoreList.size()>0){
					int aSize=0;int bSize=0;int cSize=0;int dSize=0;int eSize=0;
					for(Map<String,String> temp :classScoreList){
						if(parseInteger(temp.get("totalScore"))>=A){
							aSize++;
						}else if(parseInteger(temp.get("totalScore"))<A&&parseInteger(temp.get("totalScore"))>=B){
							bSize++;
						}else if(parseInteger(temp.get("totalScore"))<B&&parseInteger(temp.get("totalScore"))>=C){
							cSize++;
						}else if(parseInteger(temp.get("totalScore"))<C&&parseInteger(temp.get("totalScore"))>=D){
							dSize++;
						}else{
							eSize++;
						}
						
						Double score = parseDouble(temp.get("totalScore"));
						double diff = score/(zf/5)+1;
						temp.put("totalScore", gradeMap.get((int)Math.floor(diff)));
					}
					classMap.put("className", className.get(i));
					classMap.put("zrs", classScoreList.size());//总人数
					classMap.put("A", aSize);
					classMap.put("B", bSize);
					classMap.put("C", cSize);
					classMap.put("D", dSize);
					classMap.put("E", eSize);
					classMap.put("Abfb", df.format(parseDouble(aSize)*100/classScoreList.size()));
					classMap.put("Bbfb", df.format(parseDouble(bSize)*100/classScoreList.size()));
					classMap.put("Cbfb", df.format(parseDouble(cSize)*100/classScoreList.size()));
					classMap.put("Dbfb", df.format(parseDouble(dSize)*100/classScoreList.size()));
					classMap.put("Ebfb", df.format(parseDouble(eSize)*100/classScoreList.size()));
					classList.add(classMap);
				}
			}

		}
		jo.put("classesScoreList",classesScoreList);
		jo.put("classList",classList);
		String state=formatString(requestMap.get("state"));
		if("1".equals(state)){//导出
			String fileName="带班成绩对比.xls";
			String sheetName="带班成绩列表";
			String[] title={"序号","班级","姓名","等第"};
			String[] key={"xh","className","name","totalScore"};
			ExportUtil.ExportExcel(response, title, fileName, sheetName, classesScoreList, key);
		}
		return jo;
	}
	@RequestMapping(params = "command=analyzeClassTrend")
	@ResponseBody
	public JSONObject analyzeClassTrend(@RequestParam("data") String data){
		HashMap<String,String> staticMap = new HashMap();
		//TODO 改成enumMap工具类
		
		DecimalFormat df = new DecimalFormat("######0.00");
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		JSONObject jo =new JSONObject();
		String loginName=SecurityContext.getPrincipal().getUsername();
		List<String> course = new ArrayList<>();
		course.add(scoreBetweenClassScoreService.getCourseByTeacher(loginName));
		requestMap.put("course", course);
		String schoolCode = "";
		if("qpAdmin".equals(SecurityContext.getPrincipal().getUsername())){
			schoolCode = ConfigContext.getValue("framework.tmis.roleCode['qpOrgCode']");
		} else {
			schoolCode = examNumberManageService.getSchoolCodeByLoginName(loginName);
		}
		List<HashMap<String,String>> examCodeAndNameList = scoreBetweenClassScoreService.getExamCodeListBySchool(parseString(requestMap.get("grade")));
		ArrayList<String> examCodeList = new ArrayList<>();
		ArrayList<String> classCodeList = (ArrayList<String>) requestMap.get("classCode");
		ArrayList<String> classNameList = (ArrayList<String>) requestMap.get("className");
		List<String> availableClass = new LinkedList<>(); 
		HashMap<String,List> bigMap = new HashMap<>();
		String CHARINDEX ="'";
		List<String> category = new LinkedList<>(); 
		for(HashMap<String,String> examCode:examCodeAndNameList){
			CHARINDEX+=examCode.get("Exam_Number")+",";
			category.add(examCode.get("Exam_Name"));
			examCodeList.add(examCode.get("Exam_Number"));
		}
		requestMap.put("examCodeList", examCodeList);
		CHARINDEX= CHARINDEX.substring(0,CHARINDEX.length()-1)+"'";
		requestMap.put("CHARINDEX", CHARINDEX);
		for(int i=0;i<classCodeList.size();i++){
			requestMap.put("classCode", classCodeList.get(i));
			List<String> oneClassScore = scoreBetweenClassScoreService.getOneClassScoreList(requestMap);
			if(oneClassScore.size()>0){
				bigMap.put(classNameList.get(i), oneClassScore);
				availableClass.add(classNameList.get(i));
			}
		}
		String zf = scoreBetweenClassScoreService.getExamZf(course.get(0),examCodeList.get(0));
		jo.put("zf", zf);
		jo.put("bigMap", bigMap);
		jo.put("category", category);
		jo.put("availableClass", availableClass);
		return jo;
	}
}
