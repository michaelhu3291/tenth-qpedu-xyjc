package data.academic.schoolQualityAnalysis.controller;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.alibaba.fastjson.JSONObject;
import data.academic.dataManage.service.SmallTitleService;
import data.academic.examNumberManagement.service.ExamNumberManageService;
import data.academic.schoolManage.service.PoliticalInstructorService;
import data.academic.util.ExportUtil;
import data.framework.support.AbstractBaseController;
import data.framework.support.ConfigContext;
import data.platform.authority.security.SecurityContext;

@Controller
@RequestMapping("schoolQualityAnalysis/personScoreSummary")
public class SchoolPersonScoreSummaryController extends AbstractBaseController {

	@Override
	protected void init(ModelMap model, HttpServletRequest request) {
		// TODO Auto-generated method stub

	}

	@Autowired
	private SmallTitleService smallTitleService;
	@Autowired
	private ExamNumberManageService examNumberManageService;
	@Autowired
	private PoliticalInstructorService politicalInstructorService;

	/**
	 * 
	 * @Title: analyzePersonalSamllScore
	 * @Description: 个人单科成绩汇总;显示学校简称
	 * @author chenteng;zhaohuanhuan
	 * @date 2017年8月3日;2017-11-27
	 * @return void
	 * @param data
	 * @param out
	 */
	@RequestMapping(params = "command=analyzePersonalSamllScore")
	@ResponseBody
	public JSONObject analyzePersonalSamllScore(@RequestParam("data") String data, HttpServletResponse response) {
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		// 先查这门考试 有没有 有的话看题目有几题
		List<HashMap<String, String>> examQuestion = smallTitleService.getExamQuestion(requestMap);
		List<String> questionList = new ArrayList<>();
		if (examQuestion.size() > 0) {
			for (HashMap<String, String> temp : examQuestion) {
				questionList.add(temp.get("Question_Number"));
			}
		}
		String schoolCode = "";
		String loginName = SecurityContext.getPrincipal().getUsername();
		if ("qpAdmin".equals(SecurityContext.getPrincipal().getUsername())) {
			schoolCode = ConfigContext.getValue("framework.tmis.roleCode['qpOrgCode']");
		} else {
			schoolCode = examNumberManageService.getSchoolCodeByLoginName(loginName);
		}
		requestMap.put("targetschool", schoolCode);
		List<Map<String, Object>> scoreList = smallTitleService.getScoreDetailList(requestMap);
		// 操作状态0查询1导出 by zj
		String state = formatString(requestMap.get("state"));
		// 导出操作
		if ("1".equals(state)) {
			String fileName = "成绩汇总表.xls";
			String sheetName = "成绩汇总";
			// 学校，班级，学籍号，考号，姓名+题目（不定）+总分
			List<String> title = new ArrayList<String>();
			List<String> key = new ArrayList<String>();
			title.add("序号");
			key.add("xh");
			title.add("学校");
			key.add("School_Name");
			title.add("班级");
			key.add("Class_Name");
			title.add("学籍号");
			key.add("XJH");
			title.add("考号");
			key.add("Exam_Number");
			title.add("姓名");
			key.add("Name");
			title.addAll(questionList);
			key.addAll(questionList);
			title.add("总分");
			key.add("TotalScore");
			// Score_List,School_Name,XJH,Name,TotalScore,Exam_Number,Class_Name

			for (Map<String, Object> map : scoreList) {
				String[] score = formatString(map.get("Score_List")).split(",");
				for (int i = 0; i < score.length; i++) {
					map.put(questionList.get(i), score[i]);
				}
			}
			// list转数组
			String[] arr1 = (String[]) title.toArray(new String[title.size()]);
			String[] arr2 = (String[]) key.toArray(new String[key.size()]);
			ExportUtil.ExportExcel(response, arr1, fileName, sheetName, scoreList, arr2);

		}
		List<Map<String, Object>> newScoreList = new ArrayList<>();
		for (Map<String, Object> map : scoreList) {
			String schoolShortName = politicalInstructorService
					.getSchoolShortNameBySchoolCode(map.get("School_Code").toString());
			map.put("School_Name", schoolShortName);
			newScoreList.add(map);
		}
		JSONObject jo = new JSONObject();
		jo.put("questionList", questionList);
		jo.put("scoreList", scoreList);
		return jo;
	}
}
