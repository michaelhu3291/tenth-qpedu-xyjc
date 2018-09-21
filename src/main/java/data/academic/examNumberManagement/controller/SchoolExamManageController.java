/**
 * 2016年10月20日
 */
package data.academic.examNumberManagement.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

import data.academic.examInfo.service.ExamInfoSchoolService;
import data.academic.examNumberManagement.service.ExamNumberManageService;
import data.academic.statisticsAnalysis.service.SchoolPlainAdminScoreSearchService;
import data.academic.util.ExportUtil;
import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractBaseController;
import data.framework.support.ConfigContext;
import data.platform.authority.security.SecurityContext;
import data.platform.service.PlatformDataDictionaryService;

/**
 * @Title: SchoolExamManageController
 * @Description: 校级考号管理
 * @author zhaohuanhuan
 * @date 2016年10月20日 下午3:40:36
 */
@Controller
@RequestMapping("examNumberManagement/schoolExamNumberManage")
public class SchoolExamManageController extends AbstractBaseController {

	@Override
	protected void init(ModelMap model, HttpServletRequest request) {

	}

	@RequestMapping(params = "command=searchPaging")
	public void searchPaging(@RequestParam("data") String data, java.io.PrintWriter out) {

		Map<String, Object> requestMap = getSerializer().parseMap(data);
		boolean isFast = parseBoolean(requestMap.get("isFast"));
		String examName = "";
		if (isFast) {
			examName = trimString(requestMap.get("q"));
		}
		requestMap.put("examName", examName);
		int currentPage = parseInteger(requestMap.get("page"));
		int pageSize = parseInteger(requestMap.get("rows"));
		String sortField = trimString(requestMap.get("sidx"));

		if (sortField.trim().length() == 0) {
			sortField = "Exam_Time";
		}
		String sort = trimString("desc");
		String loginName = SecurityContext.getPrincipal().getUsername();
		// 根据当前登录的用户名得到学校code
		String schoolCode = examNumberManageService.getSchoolCodeByLoginName(loginName);
		String schoolSequence = schoolPlainAdminScoreSearchService.findSchoolTypeBySchoolCode(schoolCode);
		String schoolTypeCode = "";
		if ("0".equals(schoolSequence)) {
			schoolTypeCode = "xx";
		} else if ("1".equals(schoolSequence)) {
			schoolTypeCode = "cz";
		} else if ("2".equals(schoolSequence) || "3".equals(schoolSequence) || "4".equals(schoolSequence)) {
			schoolTypeCode = "gz";
			if ("3062".equals(schoolCode)) {
				schoolTypeCode = "cz";
			}
		} else if ("5".equals(schoolSequence)) {
			schoolTypeCode = "ygz";
		}
		// 根据学校类型查询年级
		List<Map<String, Object>> gradeList = dictionaryService.getGradesBySchoolType(schoolTypeCode);

		List<String> gradeIdList = new ArrayList<>();
		for (Map<String, Object> map : gradeList) {
			String gradeId = map.get("DictionaryCode").toString();
			gradeIdList.add(gradeId);
		}
		List<String> schoolCodeLists = new ArrayList<>();
		schoolCodeLists.add(schoolCode);
		requestMap.put("schoolCodeList", schoolCodeLists);
		requestMap.put("gradeIdList", gradeIdList);
		Date date = new Date();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String time = format.format(date);
		Map<String, Object> timeMap = new HashMap<>();
		timeMap.put("getTime", time);
		examNumberManageService.updateIntroducedStateByIntroducedTime(timeMap);
		requestMap.put("schoolCode", schoolCode);
		PagingResult<Map<String, Object>> pagingResult = examNumberManageService.searchPaging(requestMap, sortField,
				sort, currentPage, pageSize);
		List<Map<String, Object>> list = pagingResult.getRows();
		List<Map<String, Object>> paramList = new ArrayList<Map<String, Object>>();
		Map<String, Object> subExamMap = new HashMap<String, Object>();
		for (Map<String, Object> map : list) {
			List<String> schoolCodeList = new ArrayList<>();
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("Exam_Time", formatDate(parseDate(map.get("Exam_Time")), "yyyy-MM-dd"));
			paramMap.put("Exam_Name", map.get("Exam_Name").toString());
			paramMap.put("Exam_Number", map.get("Exam_Number").toString());
			paramMap.put("School_Year", map.get("School_Year").toString());
			paramMap.put("Term", map.get("Term").toString());
			paramMap.put("School_Code", map.get("School_Code").toString());
			paramMap.put("Exam_Type", map.get("Exam_Type").toString());
			paramMap.put("Grade_Code", map.get("Grade_Code").toString());
			paramMap.put("Id", map.get("Id").toString());
			subExamMap.put("schoolCode", schoolCode);
			paramMap.put("Closing_Time", formatDate(parseDate(map.get("Closing_Time")), "yyyy-MM-dd"));
			subExamMap.put("examCode", map.get("Exam_Number").toString());
			// 得到某次考试下面的科目考试的开始时间和结束时间
			List<Map<String, Object>> courseTime = examInfoSchoolService.getCourseStartTimeAndEndTime(subExamMap);

			if (courseTime.size() > 0) {
				for (Map<String, Object> map2 : courseTime) {
					String startStrTime = parseString(map2.get("startTime"));
					String endStrTime = parseString(map2.get("endTime"));
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					try {
						Date startTime = sdf.parse(startStrTime);
						Date endTime = sdf.parse(endStrTime);
						Date date1 = sdf.parse(time);
						if (date1.before(startTime)) {
							paramMap.put("examState", "0");
						}
						if (startTime.before(date1) && date.before(date1) || date1.equals(endTime)) {
							paramMap.put("examState", "1");
						}
						if (date1.after(endTime)) {
							paramMap.put("examState", "2");
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
			}
			// 根据学校code和考试编号判断该学校是否已经生成考号
			String examCode = examNumberManageService.getIsExistExamNumberBySchoolCodeAndExamCode(subExamMap);
			if ("0".equals(examCode) || null == examCode) {
				paramMap.put("Number_State", "0");
			} else {
				paramMap.put("Number_State", "1");
			}
			Map<String, Object> filesMap = new HashMap<>();
			filesMap.put("examNumber", map.get("Exam_Number"));
			// 查询该次考试相关的附件
			List<Map<String, Object>> filesList = examNumberManageService.selectIdByAssociatedExamNumber(filesMap);
			String filesIsExist = "0";
			if (filesList.size() > 0) {
				filesIsExist = "1";
			}
			// 得到此次考试的相关班级
			List<Map<String, Object>> classList = examInfoSchoolService
					.getClassIdByExamCode(map.get("Exam_Number").toString());
			List<String> classIdList = new ArrayList<>();
			for (Map<String, Object> classMap : classList) {
				String classId = classMap.get("Class_Id").toString();
				classIdList.add(classId);
			}
			paramMap.put("classIdList", classIdList);
			Map<String, Object> courseMap = new HashMap<>();
			courseMap.put("examCode", map.get("Exam_Number").toString());
			courseMap.put("classIdList", classIdList);
			List<Map<String, Object>> courselist = examInfoSchoolService.getCourserByExamCodeAndClass(courseMap);
			List<String> courseIdlist = new ArrayList<>();
			for (Map<String, Object> courseMaps : courselist) {
				courseIdlist.add(trimString(courseMaps.get("Course")));
			}
			paramMap.put("courseIdlist", courseIdlist);
			paramMap.put("filesIsExist", filesIsExist);
			subExamMap = new HashMap<String, Object>();
			schoolCodeList.add(schoolCode);
			subExamMap.put("schoolCodeList", schoolCodeList);
			subExamMap.put("gradeId", map.get("Grade_Code"));
			subExamMap.put("examCode", map.get("Exam_Number"));
			subExamMap.put("classIdList", classIdList);
			int minClassNo = 0;
			int maxClassNo = 0;
			if (ConfigContext.getStringSection("syzxAdmin").equals(loginName)) {
				minClassNo = 1;
				maxClassNo = 12;
			} else if (ConfigContext.getStringSection("yczxAdmin").equals(loginName)) {
				minClassNo = 21;
				maxClassNo = 27;
			}
			subExamMap.put("minClassNo", minClassNo);
			subExamMap.put("maxClassNo", maxClassNo);
			// 得到此次考试总人数
			Integer isTestCaseGeneration = examNumberManageService.countAllStuBySchoolCodeAndGrade(subExamMap);
			// 得到此次考试已生成考号人数
			Integer testCaseGeneration = examNumberManageService
					.countExistExamNumberStuBySchoolCodeAndGrade(subExamMap);
			// 得到此次考试未生成考号人数
			Integer notTestCaseGeneration = isTestCaseGeneration - testCaseGeneration;
			paramMap.put("testCaseGeneration", testCaseGeneration);
			paramMap.put("notTestCaseGeneration", notTestCaseGeneration);
			paramList.add(paramMap);

		}
		PagingResult<Map<String, Object>> newPagingResult = new PagingResult<Map<String, Object>>(paramList,
				pagingResult.getPage(), pagingResult.getPageSize(), pagingResult.getRecords());
		out.print(getSerializer().formatObject(newPagingResult));
	}
	
	/**
	 * 
	 * @Title: searchPagingImport
	 * @Description: 导出
	 * @author jay zhong
	 * @date 2017年11月23日 下午4:27:58 
	 * @return void
	 *
	 * @param data
	 * @param response
	 * @throws ParseException 
	 */
	@RequestMapping(params = "command=importExcel")
	public void searchPagingImport(@RequestParam("data") String data, HttpServletResponse response) throws ParseException {

		Map<String, Object> requestMap = getSerializer().parseMap(data);
		String examName = trimString(requestMap.get("fastQueryText"));
		requestMap.put("examName", examName);
		/*int currentPage = parseInteger(requestMap.get("page"));
		int pageSize = parseInteger(requestMap.get("rows"));
		String sortField = trimString(requestMap.get("sidx"));

		if (sortField.trim().length() == 0) {
			sortField = "Exam_Time";
		}
		String sort = trimString("desc");*/
		String loginName = SecurityContext.getPrincipal().getUsername();
		// 根据当前登录的用户名得到学校code
		String schoolCode = examNumberManageService.getSchoolCodeByLoginName(loginName);
		String schoolSequence = schoolPlainAdminScoreSearchService.findSchoolTypeBySchoolCode(schoolCode);
		String schoolTypeCode = "";
		if ("0".equals(schoolSequence)) {
			schoolTypeCode = "xx";
		} else if ("1".equals(schoolSequence)) {
			schoolTypeCode = "cz";
		} else if ("2".equals(schoolSequence) || "3".equals(schoolSequence) || "4".equals(schoolSequence)) {
			schoolTypeCode = "gz";
			if ("3062".equals(schoolCode)) {
				schoolTypeCode = "cz";
			}
		} else if ("5".equals(schoolSequence)) {
			schoolTypeCode = "ygz";
		}
		// 根据学校类型查询年级
		List<Map<String, Object>> gradeList = dictionaryService.getGradesBySchoolType(schoolTypeCode);

		List<String> gradeIdList = new ArrayList<>();
		for (Map<String, Object> map : gradeList) {
			String gradeId = map.get("DictionaryCode").toString();
			gradeIdList.add(gradeId);
		}
		List<String> schoolCodeLists = new ArrayList<>();
		schoolCodeLists.add(schoolCode);
		requestMap.put("schoolCodeList", schoolCodeLists);
		requestMap.put("gradeIdList", gradeIdList);
		Date date = new Date();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String time = format.format(date);
		Map<String, Object> timeMap = new HashMap<>();
		timeMap.put("getTime", time);
		examNumberManageService.updateIntroducedStateByIntroducedTime(timeMap);
		requestMap.put("schoolCode", schoolCode);
		List<Map<String, Object>> list = examNumberManageService.searchPagingImport(requestMap);
		List<Map<String, Object>> paramList = new ArrayList<Map<String, Object>>();
		Map<String, Object> subExamMap = new HashMap<String, Object>();
		for (Map<String, Object> map : list) {
			List<String> schoolCodeList = new ArrayList<>();
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("Exam_Time", formatDate(parseDate(map.get("Exam_Time")), "yyyy-MM-dd"));
			paramMap.put("Exam_Name", map.get("Exam_Name").toString());
			paramMap.put("Exam_Number", map.get("Exam_Number").toString());
			paramMap.put("School_Year", map.get("School_Year").toString());
			paramMap.put("Term", map.get("Term").toString());
			paramMap.put("School_Code", map.get("School_Code").toString());
			paramMap.put("Exam_Type", map.get("Exam_Type").toString());
			paramMap.put("Grade_Code", map.get("Grade_Code").toString());
			paramMap.put("Id", map.get("Id").toString());
			subExamMap.put("schoolCode", schoolCode);
			paramMap.put("Closing_Time", formatDate(parseDate(map.get("Closing_Time")), "yyyy-MM-dd"));
			subExamMap.put("examCode", map.get("Exam_Number").toString());
			// 得到某次考试下面的科目考试的开始时间和结束时间
			List<Map<String, Object>> courseTime = examInfoSchoolService.getCourseStartTimeAndEndTime(subExamMap);

			if (courseTime.size() > 0) {
				for (Map<String, Object> map2 : courseTime) {
					String startStrTime = parseString(map2.get("startTime"));
					String endStrTime = parseString(map2.get("endTime"));
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					try {
						Date startTime = sdf.parse(startStrTime);
						Date endTime = sdf.parse(endStrTime);
						Date date1 = sdf.parse(time);
						if (date1.before(startTime)) {
							paramMap.put("examState", "0");
						}
						if (startTime.before(date1) && date.before(date1) || date1.equals(endTime)) {
							paramMap.put("examState", "1");
						}
						if (date1.after(endTime)) {
							paramMap.put("examState", "2");
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
			}
			// 根据学校code和考试编号判断该学校是否已经生成考号
			String examCode = examNumberManageService.getIsExistExamNumberBySchoolCodeAndExamCode(subExamMap);
			if ("0".equals(examCode) || null == examCode) {
				paramMap.put("Number_State", "0");
			} else {
				paramMap.put("Number_State", "1");
			}
			Map<String, Object> filesMap = new HashMap<>();
			filesMap.put("examNumber", map.get("Exam_Number"));
			// 查询该次考试相关的附件
			List<Map<String, Object>> filesList = examNumberManageService.selectIdByAssociatedExamNumber(filesMap);
			String filesIsExist = "0";
			if (filesList.size() > 0) {
				filesIsExist = "1";
			}
			// 得到此次考试的相关班级
			List<Map<String, Object>> classList = examInfoSchoolService
					.getClassIdByExamCode(map.get("Exam_Number").toString());
			List<String> classIdList = new ArrayList<>();
			for (Map<String, Object> classMap : classList) {
				String classId = classMap.get("Class_Id").toString();
				classIdList.add(classId);
			}
			paramMap.put("classIdList", classIdList);
			Map<String, Object> courseMap = new HashMap<>();
			courseMap.put("examCode", map.get("Exam_Number").toString());
			courseMap.put("classIdList", classIdList);
			List<Map<String, Object>> courselist = examInfoSchoolService.getCourserByExamCodeAndClass(courseMap);
			List<String> classIdList2 = new ArrayList<>();
			for (Map<String, Object> classMap : classList) {
				String classId = classMap.get("Class_Id").toString();
				classIdList2.add(formatString(ExportUtil.CLASS_NO_MAP.get(classId)));
			}
			List<String> courseIdlist = new ArrayList<>();
			for (Map<String, Object> courseMaps : courselist) {
				courseIdlist.add(formatString(ExportUtil.CLASS_TYPE_MAP.get(trimString(courseMaps.get("Course")))));
			}
			paramMap.put("courseIdlist", courseIdlist);
			paramMap.put("filesIsExist", filesIsExist);
			subExamMap = new HashMap<String, Object>();
			schoolCodeList.add(schoolCode);
			subExamMap.put("schoolCodeList", schoolCodeList);
			subExamMap.put("gradeId", map.get("Grade_Code"));
			subExamMap.put("examCode", map.get("Exam_Number"));
			subExamMap.put("classIdList", classIdList);
			int minClassNo = 0;
			int maxClassNo = 0;
			if (ConfigContext.getStringSection("syzxAdmin").equals(loginName)) {
				minClassNo = 1;
				maxClassNo = 12;
			} else if (ConfigContext.getStringSection("yczxAdmin").equals(loginName)) {
				minClassNo = 21;
				maxClassNo = 27;
			}
			subExamMap.put("minClassNo", minClassNo);
			subExamMap.put("maxClassNo", maxClassNo);
			// 得到此次考试总人数
			Integer isTestCaseGeneration = examNumberManageService.countAllStuBySchoolCodeAndGrade(subExamMap);
			// 得到此次考试已生成考号人数
			Integer testCaseGeneration = examNumberManageService
					.countExistExamNumberStuBySchoolCodeAndGrade(subExamMap);
			// 得到此次考试未生成考号人数
			Integer notTestCaseGeneration = isTestCaseGeneration - testCaseGeneration;
			paramMap.put("testCaseGeneration", testCaseGeneration);
			paramMap.put("notTestCaseGeneration", notTestCaseGeneration);
			paramList.add(paramMap);

		}
		String fileName="考号生成情况.xls";
		String sheetName="考号生成情况列表";
		String[] title={"序号","测试日期","测试编号","测试名称","测试班级","测试科目","考号生成截止日期","考号生成情况"};
		String[] key={"xh","Exam_Time","Exam_Number","Exam_Name","classIdList","courseIdlist","Closing_Time","testCaseGeneration"};
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
		for(Map<String,Object> map:paramList){
			String Number_State=formatString(map.get("Number_State"));
			String Closing_Time=formatString(map.get("Closing_Time"));
			if(!"1".equals(Number_State)){
				map.put("testCaseGeneration","暂未生成考号");
				Date close=sdf.parse(Closing_Time);
				int days=ExportUtil.daysBetween(new Date(),close);
				if(days==0){
					map.put("Closing_Time",Closing_Time+"今天截止");
				}else if(days>0){
					map.put("Closing_Time",Closing_Time +"距离截止"+days+"天");
				}else{
					
				}
			}else{
				map.put("testCaseGeneration","已生成考号"+formatString(map.get("testCaseGeneration"))+"人未生成考号"+formatString(map.get("notTestCaseGeneration"))+"人");
			}
		}
		ExportUtil.ExportExcel(response, title, fileName, sheetName,paramList, key);
		
		
	}

	@Autowired
	private ExamNumberManageService examNumberManageService;

	@Autowired
	private SchoolPlainAdminScoreSearchService schoolPlainAdminScoreSearchService;

	@Autowired
	private ExamInfoSchoolService examInfoSchoolService;

	@Autowired
	private PlatformDataDictionaryService dictionaryService;
}
