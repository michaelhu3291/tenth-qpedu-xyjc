package data.academic.studentScoreManage.controller;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSONObject;

import data.academic.examNumberManagement.service.ExamNumberManageService;
import data.academic.schoolQualityAnalysis.service.SchoolScoreService;
import data.academic.studentScoreManage.service.StudentPersonalScoreService;
import data.academic.util.ExportUtil;
import data.framework.support.AbstractBaseController;
import data.framework.support.ConfigContext;
import data.framework.utility.ArithmeticUtil;
import data.platform.authority.security.SecurityContext;
@RestController
@RequestMapping("studentScoreManage/studentPersonalScore")
public class StudentPersonalScoreController extends AbstractBaseController{
	@Override
	protected void init(ModelMap model, HttpServletRequest request) {
	}
	
	@Autowired
	private SchoolScoreService schoolScoreService;
	@Autowired
	private ExamNumberManageService examNumberManageService;
	@Autowired
	private StudentPersonalScoreService studentPersonalScoreService;
	
	@RequestMapping(params = "command=searchScore")
	@ResponseBody
	public JSONObject getClassByGrade(@RequestParam("data") String data,HttpServletResponse response){
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		HashMap<String,String> staticMap = new HashMap();
		//TODO 改成enumMap工具类
		staticMap.put("yw","语文");staticMap.put("sx","数学");staticMap.put("yy","外语");staticMap.put("wl","物理");staticMap.put("hx","化学");staticMap.put("ty","体育");staticMap.put("sxzz","思想政治");
		staticMap.put("ls","历史");staticMap.put("dl","地理");staticMap.put("kx","科学");staticMap.put("ms","美术");staticMap.put("tzxkc","拓展型课程");staticMap.put("xxkj","信息科技");
		staticMap.put("yjxkc","研究型课程");staticMap.put("yyue","音乐");staticMap.put("njyy","牛津英语");staticMap.put("sw","生物");staticMap.put("xsjyy","高中新世纪英语");staticMap.put("zr","自然");
		staticMap.put("qm","期末");staticMap.put("qz","期中");staticMap.put("sxq","上学期");staticMap.put("xxq","下学期");
		//根据登录账号（身份证号）在tbl_ref_stucode_ExamNumber查考号和考试编号  学年学期考试类型可有可无
		String loginId = SecurityContext.getPrincipal().getUsername();
		requestMap.put("loginId", loginId);
		List<HashMap<String,String>> examNumberAndCode  = studentPersonalScoreService.searchNumberAndCode(requestMap);
		HashMap<String,String> numCodeMap = new HashMap<>();
		List<String> examNumberList = new ArrayList<>();//考号集合
		List<String> examCodeList = new ArrayList<>();//考试集合 用来查总分优良及格
		List<Map<String,Object>> newList = new ArrayList<>();
		JSONObject jo =new JSONObject();
		if(examNumberAndCode.size()>0){
			for(HashMap<String,String> temp:examNumberAndCode){
				numCodeMap.put(temp.get("examNumber"), temp.get("examCode"));
				examNumberList.add(temp.get("examNumber"));
				examCodeList.add(temp.get("examCode"));
			}
			requestMap.put("examNumberList",examNumberList);
			requestMap.put("examCodeList",examCodeList);
			List<HashMap<String,String>> examInfoList = studentPersonalScoreService.searchExamInfoList(requestMap);
			HashMap<String,HashMap> courseScoreMap = new HashMap<>();
			for(HashMap<String,String> temp:examInfoList){
				courseScoreMap.put(temp.get("course"), temp);
			}
			List<HashMap<String,Object>> scoreList = studentPersonalScoreService.searchScoreList(requestMap);
			
			Map<Integer,String> gradeMap  =new HashMap();
			gradeMap.put(1, "E");gradeMap.put(2, "D");gradeMap.put(3, "C");gradeMap.put(4, "B");gradeMap.put(5, "A");gradeMap.put(6, "A");
			for(HashMap<String,Object> temp : scoreList){
				if(courseScoreMap.get(temp.get("course"))==null){
					continue;
				}
				Integer zf = parseInteger(courseScoreMap.get(temp.get("course")).get("zf"));
				Double score = parseDouble(temp.get("totalScore"));
				double diff = score/(zf/5)+1;
				temp.put("totalScore", gradeMap.get((int)Math.floor(diff)));
				temp.put("examType", staticMap.get(temp.get("examType")));
				temp.put("term", staticMap.get(temp.get("term")));
				newList.add(temp);
				temp.put("course", staticMap.get(temp.get("course")));
			}
			
		}
		
		jo.put("newList", newList);
		String state=formatString(requestMap.get("state"));
		if("1".equals(state)){
			String fileName="成绩列表.xls";
			String sheetName="成绩列表详情";
			String[] title={"序号","学年","学期","测试类型","科目","等第"};
			String[] key={"xh","schoolYear","term","examType","course","totalScore"};
			
			ExportUtil.ExportExcel(response, title, fileName, sheetName, newList, key);
		}
 		return jo;
	}
	
	@RequestMapping(params = "command=searchScoretrend")
	@ResponseBody
	public JSONObject searchScoretrend(@RequestParam("data") String data){
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		ArrayList<String> courseList = (ArrayList<String>) requestMap.get("course");
		HashMap<String,String> staticMap = new HashMap();
		//TODO 改成enumMap工具类
		staticMap.put("yw","语文");staticMap.put("sx","数学");staticMap.put("yy","外语");staticMap.put("wl","物理");staticMap.put("hx","化学");staticMap.put("ty","体育");staticMap.put("sxzz","思想政治");
		staticMap.put("ls","历史");staticMap.put("dl","地理");staticMap.put("kx","科学");staticMap.put("ms","美术");staticMap.put("tzxkc","拓展型课程");staticMap.put("xxkj","信息科技");
		staticMap.put("yjxkc","研究型课程");staticMap.put("yyue","音乐");staticMap.put("njyy","牛津英语");staticMap.put("sw","生物");staticMap.put("xsjyy","高中新世纪英语");staticMap.put("zr","自然");
		staticMap.put("qm","期末");staticMap.put("qz","期中");staticMap.put("sxq","上学期");staticMap.put("xxq","下学期");
		//根据登录账号（身份证号）在tbl_ref_stucode_ExamNumber查考号和考试编号  学年学期考试类型可有可无
		String loginId = SecurityContext.getPrincipal().getUsername();
		requestMap.put("loginId", loginId);
		List<HashMap<String,String>> examNumberAndCode  = studentPersonalScoreService.searchNumberAndCode(requestMap);
		HashMap<String,String> numCodeMap = new HashMap<>();
		List<String> examNumberList = new ArrayList<>();//考号集合
		List<String> examCodeList = new ArrayList<>();//考试集合 用来查总分优良及格
		List<String> examNameList = new ArrayList<>();//考试名称集合 用来查总分优良及格
		List<HashMap<String,Object>> newList = new ArrayList<>();
		if(examNumberAndCode.size()>0){
			for(HashMap<String,String> temp:examNumberAndCode){
				numCodeMap.put(temp.get("examNumber"), temp.get("examCode"));
				examNumberList.add(temp.get("examNumber"));
				examCodeList.add(temp.get("examCode"));
				examNameList.add(temp.get("examName"));
			}
			/*requestMap.put("examNumberList",examNumberList);
			requestMap.put("examCodeList",examCodeList);*/
			HashMap<String,Object> queryMap = new HashMap<>();
			queryMap.put("examCodeList", examCodeList);
			
			HashMap<String,Object> courseAndExam = new HashMap<>();
			courseAndExam.put("examNumberList", examNumberList);
			for(String temp:courseList){
				List<String> tempList = new ArrayList<>();
				tempList.add(temp);
				queryMap.put("course", tempList);
				List<HashMap<String,String>> examInfoList = studentPersonalScoreService.searchExamInfoList(queryMap);
				if(examInfoList.size()<1){
					continue;
				}
				HashMap<String,Object> courseScoreMap = new HashMap<>();
				courseScoreMap.put("course",staticMap.get(temp));
				courseScoreMap.put("zf", examInfoList.get(0).get("zf"));
				courseAndExam.put("course", temp);
				List<Double> scoreList  = studentPersonalScoreService.searchCourseScoreList(courseAndExam);
				courseScoreMap.put("scoreList", scoreList);
				newList.add(courseScoreMap);
			}
		}
		JSONObject jo =new JSONObject();
		jo.put("newList", newList);
		jo.put("examNameList", examNameList);
 		return jo;
	}
	
	
}
