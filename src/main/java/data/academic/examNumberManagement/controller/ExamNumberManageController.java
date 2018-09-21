/**
 * 2016年9月21日
 */
package data.academic.examNumberManagement.controller;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import data.academic.examInfo.service.ExamInfoSchoolService;
import data.academic.examInfo.service.MarkingArrangementService;
import data.academic.examNumberManagement.service.AdminExamNumberService;
import data.academic.examNumberManagement.service.ExamNumberManageService;
import data.academic.schoolManage.service.TeacherManageService;
import data.academic.statisticsAnalysis.service.SchoolPlainAdminScoreSearchService;
import data.academic.studentManagement.service.StuManagementService;
import data.academic.teacherManagement.service.TeacherManagementService;
import data.academic.util.ExportUtil;
import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractBaseController;
import data.framework.support.ConfigContext;
import data.platform.authority.security.SecurityContext;
import data.platform.service.PlatformDataDictionaryService;

/**
 * @Title: ExamNumberManageController
 * @Description: TODO
 * @author zhaohuanhuan
 * @date 2016年9月21日 下午9:01:04
 */
@Controller
@RequestMapping("examNumberManagement/examNumberManage")
public class ExamNumberManageController extends AbstractBaseController {
	@Override
	protected void init(ModelMap model, HttpServletRequest request) {

	}

	/**
	 * @Title: searchPaging
	 * @Description: 分页查询考试信息
	 * @author zhaohuanhuan
	 * @date 2016年9月26日
	 * @param data
	 * @param out
	 * @return void
	 */
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
		if(ConfigContext.getValue("framework.tmis.roleCode['qpOrgId']").equals(schoolCode)){
			schoolCode=ConfigContext.getValue("framework.tmis.roleCode['qpOrgCode']");
		}
		// 根据用户名得到学校类型
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
			if ("3004".equals(schoolCode)) {
				schoolTypeCode = "wz";
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
		List<String> schoolCodeList = new ArrayList<>();
		List<String> schoolCodeLists = new ArrayList<>();
		// schoolCodeList.add(schoolCode);
		// 根据登录名查询角色Code
		schoolCodeLists.add(ConfigContext.getValue("framework.tmis.roleCode['qpOrgCode']"));
		requestMap.put("schoolCodeList", schoolCodeLists);
		requestMap.put("gradeIdList", gradeIdList);
		Date date = new Date();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String time = format.format(date);
		Map<String, Object> timeMap = new HashMap<>();
		timeMap.put("getTime", time);
		int minClassNo = 0;
		int maxClassNo = 0;
		String syzxSchoolCode="";
		if (ConfigContext.getStringSection("syzxAdmin").equals(loginName)) {
			minClassNo = 1;
			maxClassNo = 12;
		} else if (ConfigContext.getStringSection("yczxAdmin").equals(loginName)) {
			minClassNo = 21;
			maxClassNo = 27;
			syzxSchoolCode=ConfigContext.getStringSection("yczxSchoolCode");
		}
		requestMap.put("minClassNo", minClassNo);
		requestMap.put("maxClassNo", maxClassNo);
		requestMap.put("schoolCode", schoolCode);
		// 根据发布时间更新发布状态
		examNumberManageService.updateIntroducedStateByIntroducedTime(timeMap);
		PagingResult<Map<String, Object>> pagingResult = examNumberManageService.searchPaging(requestMap, sortField,
				sort, currentPage, pageSize);
		List<Map<String, Object>> list = pagingResult.getRows();
		List<Map<String, Object>> paramList = new ArrayList<Map<String, Object>>();
		Map<String, Object> subExamMap = new HashMap<String, Object>();
		for (Map<String, Object> map : list) {
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
			paramMap.put("Closing_Time", formatDate(parseDate(map.get("Closing_Time")), "yyyy-MM-dd"));
			subExamMap.put("schoolCode", schoolCode);
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
			List<Map<String, Object>> filesList = examNumberManageService.selectIdByAssociatedExamNumber(filesMap);
			String filesIsExist = "0";
			if (filesList.size() > 0) {
				filesIsExist = "1";
			}
			paramMap.put("filesIsExist", filesIsExist);
			subExamMap = new HashMap<String, Object>();
			if(!"".equals(syzxSchoolCode)){
				schoolCodeList.add(syzxSchoolCode);
				schoolCodeList.add(ConfigContext.getStringSection("syzxSchoolCode"));
			}else{
				schoolCodeList.add(schoolCode);
			}
			
			subExamMap.put("schoolCodeList", schoolCodeList);
			subExamMap.put("gradeId", map.get("Grade_Code"));
			subExamMap.put("examCode", map.get("Exam_Number"));
			if(ConfigContext.getStringSection("yczxSchoolCode").equals(schoolCode)
					&& !ConfigContext.getStringSection("yczxAdmin").equals(loginName)){
				schoolCode=ConfigContext.getStringSection("syzxSchoolCode");
			}
			subExamMap.put("createSchoolCode", schoolCode);
			subExamMap.put("minClassNo", minClassNo);
			subExamMap.put("maxClassNo", maxClassNo);
			// 得到某学校年级班级下所有学生总数
			Integer isTestCaseGeneration = examNumberManageService.countAllStuBySchoolCodeAndGrade(subExamMap);
			subExamMap.put("examNumberIsExist", 1);
			// 得到某学校年级班级下所有生成考号学生总数
			Integer testCaseGeneration = examNumberManageService.countAllStuBySchoolCodeAndGrade(subExamMap);
			// 得到某学校年级班级下未生成考号的学生总数
			Integer notTestCaseGeneration = isTestCaseGeneration - testCaseGeneration;
			paramMap.put("testCaseGeneration", testCaseGeneration);
			paramMap.put("notTestCaseGeneration", notTestCaseGeneration);
			paramList.add(paramMap);

		}
		PagingResult<Map<String, Object>> newPagingResult = new PagingResult<Map<String, Object>>(paramList,
				pagingResult.getPage(), pagingResult.getPageSize(), pagingResult.getRecords());
		out.print(getSerializer().formatObject(newPagingResult));
	}
	
	@RequestMapping(params = "command=importExcel")
	public void searchPagingImport(@RequestParam("data") String data,HttpServletResponse response) throws ParseException {

		Map<String, Object> requestMap = getSerializer().parseMap(data);
		String examName =trimString(requestMap.get("fastQueryText"));
		requestMap.put("examName", examName);
		String loginName = SecurityContext.getPrincipal().getUsername();
		// 根据当前登录的用户名得到学校code
		String schoolCode = examNumberManageService.getSchoolCodeByLoginName(loginName);
		if(ConfigContext.getValue("framework.tmis.roleCode['qpOrgId']").equals(schoolCode)){
			schoolCode=ConfigContext.getValue("framework.tmis.roleCode['qpOrgCode']");
		}
		// 根据用户名得到学校类型
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
			if ("3004".equals(schoolCode)) {
				schoolTypeCode = "wz";
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
		List<String> schoolCodeList = new ArrayList<>();
		List<String> schoolCodeLists = new ArrayList<>();
		// schoolCodeList.add(schoolCode);
		// 根据登录名查询角色Code
		schoolCodeLists.add(ConfigContext.getValue("framework.tmis.roleCode['qpOrgCode']"));
		requestMap.put("schoolCodeList", schoolCodeLists);
		requestMap.put("gradeIdList", gradeIdList);
		Date date = new Date();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String time = format.format(date);
		Map<String, Object> timeMap = new HashMap<>();
		timeMap.put("getTime", time);
		int minClassNo = 0;
		int maxClassNo = 0;
		String syzxSchoolCode="";
		if (ConfigContext.getStringSection("syzxAdmin").equals(loginName)) {
			minClassNo = 1;
			maxClassNo = 12;
		} else if (ConfigContext.getStringSection("yczxAdmin").equals(loginName)) {
			minClassNo = 21;
			maxClassNo = 27;
			syzxSchoolCode=ConfigContext.getStringSection("yczxSchoolCode");
		}
		requestMap.put("minClassNo", minClassNo);
		requestMap.put("maxClassNo", maxClassNo);
		requestMap.put("schoolCode", schoolCode);
		// 根据发布时间更新发布状态
		examNumberManageService.updateIntroducedStateByIntroducedTime(timeMap);
		List<Map<String, Object>> list = examNumberManageService.searchPagingImport(requestMap);
		List<Map<String, Object>> paramList = new ArrayList<Map<String, Object>>();
		Map<String, Object> subExamMap = new HashMap<String, Object>();
		for (Map<String, Object> map : list) {
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
			paramMap.put("Closing_Time", formatDate(parseDate(map.get("Closing_Time")), "yyyy-MM-dd"));
			subExamMap.put("schoolCode", schoolCode);
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
			List<Map<String, Object>> filesList = examNumberManageService.selectIdByAssociatedExamNumber(filesMap);
			String filesIsExist = "0";
			if (filesList.size() > 0) {
				filesIsExist = "1";
			}
			paramMap.put("filesIsExist", filesIsExist);
			subExamMap = new HashMap<String, Object>();
			if(!"".equals(syzxSchoolCode)){
				schoolCodeList.add(syzxSchoolCode);
				schoolCodeList.add(ConfigContext.getStringSection("syzxSchoolCode"));
			}else{
				schoolCodeList.add(schoolCode);
			}
			
			subExamMap.put("schoolCodeList", schoolCodeList);
			subExamMap.put("gradeId", map.get("Grade_Code"));
			subExamMap.put("examCode", map.get("Exam_Number"));
			if(ConfigContext.getStringSection("yczxSchoolCode").equals(schoolCode)
					&& !ConfigContext.getStringSection("yczxAdmin").equals(loginName)){
				schoolCode=ConfigContext.getStringSection("syzxSchoolCode");
			}
			subExamMap.put("createSchoolCode", schoolCode);
			subExamMap.put("minClassNo", minClassNo);
			subExamMap.put("maxClassNo", maxClassNo);
			// 得到某学校年级班级下所有学生总数
			Integer isTestCaseGeneration = examNumberManageService.countAllStuBySchoolCodeAndGrade(subExamMap);
			subExamMap.put("examNumberIsExist", 1);
			// 得到某学校年级班级下所有生成考号学生总数
			Integer testCaseGeneration = examNumberManageService.countAllStuBySchoolCodeAndGrade(subExamMap);
			// 得到某学校年级班级下未生成考号的学生总数
			Integer notTestCaseGeneration = isTestCaseGeneration - testCaseGeneration;
			paramMap.put("testCaseGeneration", testCaseGeneration);
			paramMap.put("notTestCaseGeneration", notTestCaseGeneration);
			paramList.add(paramMap);

		}
		String fileName="考号生成情况.xls";
		String sheetName="考号生成情况列表";
		String[] title={"序号","测试日期","测试编号","测试名称","考号生成截止日期","考号生成情况"};
		String[] key={"xh","Exam_Time","Exam_Number","Exam_Name","Closing_Time","testCaseGeneration"};
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
		for(Map<String,Object> map:paramList){
			String Number_State=formatString(map.get("Number_State"));
			String Closing_Time=formatString(map.get("Closing_Time"));
			if(!"1".equals(Number_State)){
				map.put("testCaseGeneration","未生成考号,请生成考号");
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

	/**
	 * @Title: queryDetailList
	 * @Description: 生成九位数的考号
	 * @author zhaohuanhuan
	 * @date 2016年9月26日
	 * @return void
	 * @param data
	 * @param out
	 */
	@RequestMapping(params = "command=queryDetailList")
	public void queryDetailList(@RequestParam("data") String data, java.io.PrintWriter out) {
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		List<Map<String, Object>> paramList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> isNotStatearamList = new ArrayList<Map<String, Object>>();
		Map<String, Object> paramMap = new HashMap<>();
		Map<String, Object> stateMap = new HashMap<>();
		List<Map<String, Object>> stuInfoList = new ArrayList<>();
		List<Map<String, Object>> stuIfnoIsNotStateList = new ArrayList<>();
		List<Map<String, Object>> xjbStuInfoList = new ArrayList<>();
		List<Map<String, Object>> xjbStuInfoNotList = new ArrayList<>();
		String loginName = SecurityContext.getPrincipal().getUsername();
		String examCode = requestMap.get("examNumber").toString();// 考试编号
		// 得到学校code
		String schoolCode = examNumberManageService.getSchoolCodeByLoginName(loginName);
		String state = requestMap.get("stateCode").toString();
		List<Object> stateCodeList = new ArrayList<>();
		List<Object> codes = new ArrayList<>();
		String stateVal = state.substring(1, state.length() - 1);
		String[] stateArrar = stateVal.split(", ");
		Map<String, Object> candidateNumberTypeMap = new HashMap<>();
		for (int i = 0; i < stateArrar.length; i++) {
			candidateNumberTypeMap.put("examCode", examCode);
			candidateNumberTypeMap.put("schoolCode", schoolCode);
			candidateNumberTypeMap.put("candidateNumberType", stateArrar[i]);
			codes.add(stateArrar[i]);
			// 学校和生成考号类型关联关系
			examNumberManageService.inserttSchoolCandidateNumberType(candidateNumberTypeMap);
		}
		candidateNumberTypeMap = new HashMap<>();
		candidateNumberTypeMap.put("codes", codes);
		List<Map<String, Object>> stateCode = dictionaryService.loadChiadNode(candidateNumberTypeMap);
		for (Map<String, Object> map : stateCode) {
			stateCodeList.add(map.get("DictionaryCode"));
		}
		String examNumber = "";
		String createSchoolCode=schoolCode;
		if(ConfigContext.getStringSection("yczxSchoolCode").equals(schoolCode)){
			schoolCode=ConfigContext.getStringSection("syzxSchoolCode");
		}
		paramMap.put("schoolCode", schoolCode);
		paramMap.put("stateCodeList", stateCodeList);
		paramMap.put("gradeId", requestMap.get("gradeCode"));
		stateMap.put("schoolCode", schoolCode);
		stateMap.put("gradeId", requestMap.get("gradeCode"));
		// 根据考试编号得到与之相关的班级
		List<Map<String, Object>> list = examInfoSchoolService.getClassIdByExamCode(examCode);
		List<String> classIdList = new ArrayList<>();
		if (list.size() > 0) {
			for (Map<String, Object> map : list) {
				if (map != null) {
					classIdList.add(map.get("Class_Id").toString());
				}
			}
		}
		paramMap.put("classIdList", classIdList);
		List<String> xjbClassIdList = examNumberManageService.getXjbClass(stateMap);
		stateMap.clear();
		int minClassNo = 0;
		int maxClassNo = 0;
		String schoolJm = "";
		if (ConfigContext.getStringSection("syzxAdmin").equals(loginName)) {
			minClassNo = 1;
			maxClassNo = 12;
			schoolJm = "06";
		} else if (ConfigContext.getStringSection("yczxAdmin").equals(loginName)) {
			minClassNo = 21;
			maxClassNo = 27;
			schoolJm = "09";
		}
		paramMap.put("minClassNo", minClassNo);
		paramMap.put("maxClassNo", maxClassNo);
		stateMap.put("minClassNo", minClassNo);
		stateMap.put("maxClassNo", maxClassNo);
		if (!stateCodeList.contains("0") && stateCodeList.contains("5")) {
			// 根据学校code和选中学籍状态得到学生 除了本校调出待调动的学生
			stuInfoList = examNumberManageService.getStuInfoBySchoolCodeAndStateCode(paramMap); // 除了调出和待调动的所有学生
			stuIfnoIsNotStateList = examNumberManageService.getStuInfoBySchoolCodeAndIsNotStateCode(paramMap);
			if (xjbClassIdList.size() > 0) {
				stateMap.put("xjbClassIdList", xjbClassIdList);
				stateMap.put("schoolCode", schoolCode);
				stateMap.put("classIdList", classIdList);
				stateMap.put("gradeId", requestMap.get("gradeCode"));
				xjbStuInfoList = examNumberManageService.getStuInfoBySchoolCodeAndStateCode(stateMap);// 新疆班学生信息
				xjbStuInfoNotList = examNumberManageService.getStuInfoBySchoolCodeAndStateCode(stateMap);// 剔除新疆班的学生
				stuInfoList.addAll(xjbStuInfoList);
				stuIfnoIsNotStateList.removeAll(xjbStuInfoNotList);
			}
		} else if (!stateCodeList.contains("5") && stateCodeList.contains("0")) {
			stuInfoList = examNumberManageService.getStuInfoBySchoolCodeAndStateCode(paramMap); // 除了调出和待调动的所有学生
			stuIfnoIsNotStateList = examNumberManageService.getStuInfoBySchoolCodeAndIsNotStateCode(paramMap);
			if (xjbClassIdList.size() > 0) {
				stateMap.put("classIdList", classIdList);
				stateMap.put("xjbClassIdList", xjbClassIdList);
				stateMap.put("schoolCode", schoolCode);
				stateMap.put("gradeId", requestMap.get("gradeCode"));
				xjbStuInfoList = examNumberManageService.getStuInfoBySchoolCodeAndStateCode(stateMap);// 新疆班学生信息
				xjbStuInfoNotList = examNumberManageService.getStuInfoBySchoolCodeAndStateCode(stateMap);// 剔除新疆班的学生
				stuInfoList.removeAll(xjbStuInfoList);
				stuIfnoIsNotStateList.addAll(xjbStuInfoNotList);
			}
		} else if (stateCodeList.size() == 1 && "5".equals(stateCodeList.get(0))) {
			stuInfoList = examNumberManageService.getStuInfoBySchoolCodeAndStateCode(paramMap); // 除了调出和待调动的所有学生
			stuIfnoIsNotStateList = examNumberManageService.getStuInfoBySchoolCodeAndIsNotStateCode(paramMap);
			if (xjbClassIdList.size() > 0) {
				stateMap.put("xjbClassIdList", xjbClassIdList);
				stateMap.put("schoolCode", schoolCode);
				xjbStuInfoList = examNumberManageService.getStuInfoBySchoolCodeAndStateCode(stateMap);// 新疆班学生信息
				xjbStuInfoNotList = examNumberManageService.getStuInfoBySchoolCodeAndStateCode(stateMap);// 剔除新疆班的学生
				stuInfoList.addAll(xjbStuInfoList);
				stuIfnoIsNotStateList.removeAll(xjbStuInfoNotList);
			}
		} else {
			stuInfoList = examNumberManageService.getStuInfoBySchoolCodeAndStateCode(paramMap); // 除了调出和待调动的所有学生
			stuIfnoIsNotStateList = examNumberManageService.getStuInfoBySchoolCodeAndIsNotStateCode(paramMap);
		}
		String schoolSequence = teacherManageService.selectSchoolTypeByLoginName(schoolCode);
		String createPerson = SecurityContext.getPrincipal().getUsername();
		String schoolType = "";
		if ("0".equals(schoolSequence)) {
			schoolType = "xx";
		} else if ("1".equals(schoolSequence)) {
			schoolType = "cz";
		} else if ("4".equals(schoolSequence) || "3".equals(schoolSequence) || "2".equals(schoolSequence)) {
			schoolType = "gz";
		}
		String examTime = requestMap.get("examTime").toString();
		examTime = examTime.replace("-", "");
		Integer i = 0;
		for (Map<String, Object> map : stuIfnoIsNotStateList) {
			map.put("examTime", examTime);
			String stuCode = map.get("SFZJH").toString();
			map.put("examNumber", "");
			map.put("schoolYear", requestMap.get("schoolYear"));
			map.put("term", requestMap.get("term"));
			map.put("examType", requestMap.get("examType"));
			map.put("schoolType", schoolType);
			map.put("stuCode", stuCode);
			map.put("createPerson", createPerson);
			map.put("examCode", requestMap.get("examNumber"));
			map.put("xjfh", map.get("XJFH"));
			map.put("examNumberIsnull", "0");
			map.put("createSchoolCode", schoolCode);
			if ("2".equals(map.get("stuState"))) {
				map.put("stuState", "0"); // 表示转出
			} else {
				map.put("stuState", "1");
			}
			isNotStatearamList.add(map);
		}
		String gradeId = "";
		for (Map<String, Object> map : stuInfoList) {
			String classId = formatString(map.get("Class_Id"));
			if (!"".equals(classId)) {
				Map<String, Object> classMap = examNumberManageService.getClassInfoByClassId(classId);
				gradeId = formatString(classMap.get("Grade_No"));
			}
			map.put("examTime", examTime);
			// 根据学校code得到学校简码
			String brevityCode = "";
			if ("3004".equals(schoolCode)) {
				if ("16".equals(gradeId) || "17".equals(gradeId) || "18".equals(gradeId) || "19".equals(gradeId)) {
					brevityCode = "30";
				} else {
					brevityCode = "85";
				}
			} else if (ConfigContext.getStringSection("syzxSchoolCode").equals(schoolCode) || ConfigContext.getStringSection("yczxSchoolCode").equals(schoolCode)) {
				brevityCode = schoolJm;
			} else {
				brevityCode = examNumberManageService.getBrevityCodeBySchoolCode(schoolCode);
				if ("".equals(brevityCode) || null == brevityCode) {
					brevityCode = "99";
				}
				if (brevityCode.length() == 1) {
					brevityCode = "0" + brevityCode;
				}
			}
			// 得到考试编号的后四位
			if (!"".equals(examCode) || null != examCode) {
				examCode = examCode.substring(examCode.length() - 4, examCode.length());
			}
			String number = i.toString();
			i++;
			if (i < 10) {
				number = "00" + i;
			}
			if (i > 9 && i <= 99) {
				number = "0" + i;
			}
			if (i > 99 && i < 999) {
				number = i.toString();
			}
			if (i > 999) {
				i = 0;
			}

			StringBuffer sb = new StringBuffer();
			sb.append(examCode).append(brevityCode).append(number);
			// 学生考号
			examNumber = sb.toString();

			String stuCode = map.get("SFZJH").toString();
			map.put("examNumber", examNumber);
			map.put("schoolYear", requestMap.get("schoolYear"));
			map.put("term", requestMap.get("term"));
			map.put("examType", requestMap.get("examType"));
			map.put("schoolType", schoolType);
			map.put("stuCode", stuCode);
			map.put("createPerson", createPerson);
			map.put("examCode", requestMap.get("examNumber"));
			map.put("createSchoolCode", createSchoolCode);
			if (map.containsKey("XJFH")) {
				map.put("xjfh", map.get("XJFH"));
			} else {
				map.put("xjfh", "");
			}
			map.put("examNumberIsnull", "1");
			map.put("stuState", "1");
			if ("2".equals(map.get("stuState"))) {
				map.put("stuState", "0"); // 表示转出
			} else {
				map.put("stuState", "1");
			}
			paramList.add(map);
		}
		// 添加状态在选中的考号
		examNumberManageService.insertStuCodeExamNumber(paramList);
		// 添加状态不在在选中的考号
		examNumberManageService.insertStuCodeExamNumber(isNotStatearamList);
		Map<String, Object> subInstructorExamMap = new HashMap<String, Object>();
		subInstructorExamMap.put("schoolCode", createSchoolCode);
		subInstructorExamMap.put("examCode", requestMap.get("examNumber"));
		// 更改该校是否生成考号
		examNumberManageService.updateExamNumberStateById(subInstructorExamMap);
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> subExamMap = new HashMap<String, Object>();
		List<String> schoolCodeList = new ArrayList<>();
		schoolCodeList.add(schoolCode);
		subExamMap.put("schoolCodeList", schoolCodeList);
		subExamMap.put("gradeId", gradeId);
		subExamMap.put("examCode", requestMap.get("examNumber"));// 考试编号
		// 得到某学校年级班级下所有学生总数
		Integer isTestCaseGeneration = stuInfoList.size() + stuIfnoIsNotStateList.size();
		// 得到某学校年级班级下所有生成考号学生总数
		subExamMap.put("examNumberIsExist", 1);
		Integer testCaseGeneration = stuInfoList.size();
		// 得到某学校年级班级下所有未生成考号学生总数
		Integer notTestCaseGeneration = stuIfnoIsNotStateList.size();
		map.put("isTestCaseGeneration", isTestCaseGeneration);
		map.put("testCaseGeneration", testCaseGeneration);
		map.put("notTestCaseGeneration", notTestCaseGeneration);
		map.put("message", "success");
		out.print(getSerializer().formatObject(map));
	}

	/**
	 * @Title: examNumberIsExist
	 * @Description: 判断考号是否存在
	 * @author zhaohuanhuan
	 * @date 2016年9月25日
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=examNumberIsExist")
	public void examNumberIsExist(@RequestParam("data") String data, java.io.PrintWriter out) {
		Map<String, Object> map = getSerializer().parseMap(data);
		String examNumber = (String) map.get("examNumber");
		String id = (String) map.get("id");
		boolean flag = true;
		if ("".equals(examNumber) || null == examNumber) {
			flag = true;
		} else {
			List<Map<String, Object>> list = examNumberManageService.getExamNumberById(id);
			String examNumber2 = "";
			for (Map<String, Object> map2 : list) {
				examNumber2 = map2.get("Exam_Number").toString();
			}

			if (examNumber.equals(examNumber2)) {
				flag = true;
			} else {
				List<Map<String, Object>> paramMapList = examNumberManageService.examNumberIsExist(examNumber);
				if (paramMapList.size() > 0) {
					flag = false;
				}
			}
		}

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("flag", flag);
		out.print(getSerializer().formatMap(resultMap));
	}

	/**
	 * @Title: exportExcel
	 * @Description: 导出excel表
	 * @author zhaohuanhuan
	 * @date 2016年9月26日
	 * @param request
	 * @param response
	 * @throws UnsupportedEncodingException
	 * @return void
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(params = "command=exportExcel")
	public void exportExcel(HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException {
		String datas = request.getParameter("data");
		Map<String, Object> dataMap = this.getSerializer().parseMap(datas);
		String loginName = SecurityContext.getPrincipal().getUsername();
		String schoolCode = "";
		// 根据登录名查询角色Code
		String roleCode = teacherManagementService.selectRoleCodeByLoginName(loginName);
		List<String> schoolCodeList = new ArrayList<>();
		Map<String, Object> subExamMap = new HashMap<String, Object>();
		int type = 0;
		int minClassNo = 0;
		int maxClassNo = 0;
		// 区级管理员
		if ("".equals(parseString(dataMap.get("schoolCode"))) || null == parseString(dataMap.get("schoolCode"))) {
			if (ConfigContext.getValue("framework.tmis.roleCode['qpRoleAdminCode']").equals(roleCode)) {
				schoolCodeList = adminExamNumverService
						.getSchoolCodeAndExistNumber(parseString(dataMap.get("examCode")));
				dataMap.put("stuState", "2");
				dataMap.put("stuRefExam", "0");
				subExamMap.put("stuState", "2");
				subExamMap.put("stuRefExam", "0");
				type = 1;
			} else {// 校级管理员
				// 根据当前登录的用户名得到学校code
				schoolCode = examNumberManageService.getSchoolCodeByLoginName(loginName);
				dataMap.put("createSchoolCode", schoolCode);
				subExamMap.put("createSchoolCode", schoolCode);
				if(ConfigContext.getStringSection("yczxSchoolCode").equals(schoolCode)){
					schoolCodeList.add(ConfigContext.getStringSection("syzxSchoolCode"));
					minClassNo=21;
					maxClassNo=27;
				}else{
					if(ConfigContext.getStringSection("syzxSchoolCode").equals(schoolCode)){
						minClassNo=1;
						maxClassNo=12;
					}
					schoolCodeList.add(schoolCode);
				}
				type = 2;
			}
			// 选中学校
		} else {
			dataMap.put("createSchoolCode", schoolCode);
			subExamMap.put("createSchoolCode", schoolCode);
			schoolCode = parseString(dataMap.get("schoolCode"));
			if(ConfigContext.getStringSection("yczxSchoolCode").equals(schoolCode)){
				minClassNo=21;
				maxClassNo=27;
			}else{
				if(ConfigContext.getStringSection("syzxSchoolCode").equals(schoolCode)){
					minClassNo=1;
					maxClassNo=12;
				}
				schoolCodeList.add(schoolCode);
			}
			type = 3;
		}
		dataMap.put("schoolCode", schoolCode);
		// 得到此次考试的相关班级
		List<Map<String, Object>> classList = examInfoSchoolService
				.getClassIdByExamCode(parseString(dataMap.get("examCode")));
		List<String> classIdList = new ArrayList<>();
		for (Map<String, Object> classMap : classList) {
			if (classMap != null) {
				classIdList.add(formatString(classMap.get("Class_Id")));
			}
		}
		dataMap.put("classIdList", classIdList);
		subExamMap.put("schoolCodeList", schoolCodeList);
		subExamMap.put("gradeId", dataMap.get("gradeId"));
		subExamMap.put("examCode", dataMap.get("examCode"));
		subExamMap.put("classIdList", classIdList);
		subExamMap.put("stuNameOrXjfh", dataMap.get("stuNameOrXjfh"));
		subExamMap.put("examNumberIsnull", dataMap.get("examNumberState"));
		subExamMap.put("chooseCourse", dataMap.get("chooseCourse"));
		if (ConfigContext.getStringSection("syzxAdmin").equals(loginName)) {
			minClassNo = 1;
			maxClassNo = 12;
		} else if (ConfigContext.getStringSection("yczxAdmin").equals(loginName)) {
			minClassNo = 21;
			maxClassNo = 27;
		}
		subExamMap.put("minClassNo", minClassNo);
		subExamMap.put("maxClassNo", maxClassNo);
		dataMap.put("minClassNo", minClassNo);
		dataMap.put("maxClassNo", maxClassNo);
		// 得到某学校年级班级下所有学生总数
		Integer isTestCaseGeneration = examNumberManageService.countAllStuBySchoolCodeAndGrade(subExamMap);
		subExamMap.put("examNumberIsExist", 1);
		// 得到某学校年级班级下所有生成考号学生总数
		Integer testCaseGeneration = examNumberManageService.countAllStuBySchoolCodeAndGrade(subExamMap);
		// 得到某学校年级班级下所有未生成考号学生总数
		Integer notTestCaseGeneration = isTestCaseGeneration - testCaseGeneration;
		dataMap.put("schoolCodeList", schoolCodeList);
		// 得到存在考号的学生信息
		List<Map<String, Object>> list = examNumberManageService.getStuInfoByParams(dataMap);
		String fileDisplay = dataMap.get("gradeName").toString() + "学生信息.xls";
		try {
			response.setContentType("application/x-download");
			fileDisplay = URLEncoder.encode(fileDisplay, "UTF-8");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileDisplay);
			// 创建一个webbook，对应一个Excel文件
			HSSFWorkbook workbook = new HSSFWorkbook();
			// 在webbook中添加一个sheet,对应Excel文件中的sheet
			HSSFSheet sheet = workbook.createSheet();
			// 在sheet中添加表头第0行
			workbook.setSheetName(0, "学生信息");

			sheet.setColumnWidth((short) 0, (short) 2000);
			sheet.setColumnWidth((short) 1, (short) 4000);
			sheet.setColumnWidth((short) 2, (short) 3000);
			sheet.setColumnWidth((short) 3, (short) 3000);
			sheet.setColumnWidth((short) 4, (short) 3000);
			sheet.setColumnWidth((short) 5, (short) 3000);
			sheet.setColumnWidth((short) 6, (short) 6000);
			sheet.setColumnWidth((short) 7, (short) 4000);
			sheet.setColumnWidth((short) 8, (short) 3000);
			sheet.setColumnWidth((short) 9, (short) 3000);
			sheet.setColumnWidth((short) 10, (short) 3000);
			sheet.setColumnWidth((short) 11, (short) 4000);
			if ("31".equals(dataMap.get("gradeId")) || "32".equals(dataMap.get("gradeId"))
					|| "33".equals(dataMap.get("gradeId"))) {
				sheet.setColumnWidth((short) 12, (short) 6000);
			}
			// ，创建单元格，并设置值表头 设置表头居中
			HSSFCellStyle titleStyle = workbook.createCellStyle();
			titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

			HSSFFont font = workbook.createFont();
			font.setFontName("宋体");
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			font.setFontHeightInPoints((short) 11);// 设置字体大小
			titleStyle.setFont(font);

			// 总汇字体
			HSSFCellStyle countStyle = workbook.createCellStyle();
			titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
			HSSFFont countFont = workbook.createFont();
			countFont.setFontName("宋体");
			countFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			countFont.setFontHeightInPoints((short) 14);// 设置字体大小
			countFont.setColor(HSSFColor.RED.index);// 设置字体颜色
			countStyle.setFont(countFont);

			// 学生信息字体
			HSSFCellStyle style = workbook.createCellStyle();
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			// 表头
			String[] titleAry = new String[] { "序号", "学校简称", "学校简码", "年级", "班级", "性别", "学籍号", "考号", "姓名", "户籍", "随班就读",
					"借读来源", "是否新疆班" };
			if ("31".equals(dataMap.get("gradeId")) || "32".equals(dataMap.get("gradeId"))
					|| "33".equals(dataMap.get("gradeId"))) {
				titleAry = new String[] { "序号", "学校简称", "学校简码", "年级", "班级", "性别", "学籍号", "考号", "姓名", "户籍", "随班就读",
						"借读来源", "是否新疆班", "学科" };
			}
			HSSFRow row = sheet.createRow((short) 0);
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, (titleAry.length - 1)));
			HSSFCell cellTiltle = row.createCell(0);

			if ("0".equals(dataMap.get("examNumberState").toString())) {
				// 总汇信息
				cellTiltle.setCellValue("未分配考号" + notTestCaseGeneration + "人");
			} else if ("1".equals(dataMap.get("examNumberState").toString())) {
				// 总汇信息
				cellTiltle.setCellValue("已分配考号" + testCaseGeneration + "人");
			} else {
				// 总汇信息
				cellTiltle.setCellValue("总人数：" + isTestCaseGeneration + "人,已分配考号" + testCaseGeneration + "人,未分配考号"
						+ notTestCaseGeneration + "人");
			}
			row.setHeight((short) 350);
			cellTiltle.setCellStyle(countStyle);
			row = sheet.createRow((short) 1);

			// 设置title
			for (int i = 0; i < titleAry.length; i++) {
				HSSFCell cell = row.createCell((short) i);
				cell.setCellStyle(titleStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(titleAry[i]);
			}

			// 写入实体数据
			int j = 1;
			String schoolShortName = "";
			String brevityCode = "";
			String classNo = "";
			subExamMap = new HashMap<String, Object>();
			List<Map<String, Object>> calssMapList = new ArrayList<>();
			Map<String, Object> getClassParamMap = new HashMap<String, Object>();
			List<Map<String, Object>> schoolInfoList = new ArrayList<>();
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> paramMap = list.get(i);
				row = sheet.createRow((short) j + 1);
				j++;
				HSSFCell cell = row.createCell((short) 0);
				cell.setCellStyle(style);
				cell.setCellValue((i + 1));
				if (type == 1 && !paramMap.get("School_Code").equals(paramMap.get("school"))) {
					subExamMap.put("School_Code", paramMap.get("school"));
					schoolInfoList = markingArrangementService.getSchoolShortNameAndBrevityCode(subExamMap);
				} else {
					if ("4".equals(paramMap.get("stuState"))) {
						subExamMap.put("School_Code", paramMap.get("school"));
					} else {
						subExamMap.put("School_Code", paramMap.get("School_Code"));
					}

					schoolInfoList = markingArrangementService.getSchoolShortNameAndBrevityCode(subExamMap);
				}
				for (Map<String, Object> map : schoolInfoList) {
					schoolShortName = parseString(map.get("School_Short_Name"));
					brevityCode = parseString(map.get("Brevity_Code"));
				}
				if(ConfigContext.getStringSection("syzxSchoolCode").equals(subExamMap.get("School_Code"))){
					if(parseInteger(paramMap.get("Class_No"))>20){
						schoolShortName = ConfigContext.getStringSection("yczxSchoolName");
						brevityCode = "9";
					}
					
				}
				String stateCode = parseString(paramMap.get("STATE_CODE"));
				HSSFCell cellOne = row.createCell((short) 1);
				cellOne.setCellStyle(style);
				cellOne.setCellValue(schoolShortName);

				HSSFCell cellBrevityCode = row.createCell((short) 2);
				cellBrevityCode.setCellStyle(style);
				cellBrevityCode.setCellValue(brevityCode);

				HSSFCell cellGrade = row.createCell((short) 3);
				cellGrade.setCellStyle(style);
				cellGrade.setCellValue(parseString(dataMap.get("gradeId")));

				if ("2".equals(paramMap.get("stuState"))) {
					getClassParamMap.put("sfzjh", paramMap.get("SFZJH"));
					calssMapList = stuManagementService.getOldClassByStuSfzjh(getClassParamMap);
				}
				if (calssMapList.size() > 0) {
					for (Map<String, Object> map : calssMapList) {
						classNo = map.get("Class_No").toString();
					}
				} else {
					classNo = parseString(paramMap.get("Class_No"));
				}
				HSSFCell cellTwo = row.createCell((short) 4);
				cellTwo.setCellStyle(style);
				// cellTwo.setCellValue(parseString(paramMap.get("Class_No")));
				cellTwo.setCellValue(classNo);

				HSSFCell cellThree = row.createCell((short) 5);
				cellThree.setCellStyle(style);
				String sex = "";
				if ("1".equals(trimString(paramMap.get("Sex")))) {
					sex = "男";
				} else if ("2".equals(trimString(paramMap.get("Sex")))) {
					sex = "女";
				} else {
					sex = "";
				}
				cellThree.setCellValue(sex);

				HSSFCell celClass = row.createCell((short) 6);
				celClass.setCellStyle(style);
				celClass.setCellValue(parseString(paramMap.get("XJFH")));

				HSSFCell cellFour = row.createCell((short) 7);
				cellFour.setCellStyle(style);
				String examNumber = "";
				if ("".equals(parseString(paramMap.get("Exam_Number")))
						|| null == parseString(paramMap.get("Exam_Number"))) {
					examNumber = "未分配";
				} else {
					examNumber = parseString(paramMap.get("Exam_Number"));
				}
				cellFour.setCellValue(examNumber);

				HSSFCell cellFive = row.createCell((short) 8);
				cellFive.setCellStyle(style);
				cellFive.setCellValue(parseString(paramMap.get("Stu_Name")));

				HSSFCell cellSix = row.createCell((short) 9);
				cellSix.setCellStyle(style);
				cellSix.setCellValue(parseString(paramMap.get("HJ")));

				HSSFCell cellSeven = row.createCell((short) 10);
				cellSeven.setCellStyle(style);
				String isSbjd = "";
				if ("1".equals(stateCode)) {
					isSbjd = "是";
				} else {
					isSbjd = "否";
				}
				cellSeven.setCellValue(isSbjd);

				HSSFCell cellJd = row.createCell((short) 11);
				cellJd.setCellStyle(style);
				String jd = "";
				cellJd.setCellValue(jd);

				HSSFCell cellXjb = row.createCell((short) 12);
				cellXjb.setCellStyle(style);
				String isXjb = "";
				if ("xjb".equals(stateCode)) {
					isXjb = "是";
				} else {
					isXjb = "否";
				}
				cellXjb.setCellValue(isXjb);

				if ("31".equals(dataMap.get("gradeId")) || "32".equals(dataMap.get("gradeId"))
						|| "33".equals(dataMap.get("gradeId"))) {
					HSSFCell cellEight = row.createCell((short) 13);
					cellEight.setCellStyle(style);
					StringBuffer sb = new StringBuffer();
					int courseNum = 1;
					String courseName = "";
					List<Map<String, Object>> courseList = stuManagementService.getCousresByXjfh(paramMap);
					for (Map<String, Object> map : courseList) {
						courseName = parseString(map.get("Course_Name"));
						if (1 < courseNum && courseNum <= courseList.size()) {
							sb.append(",");
						}
						sb.append(courseName);
						courseNum++;
					}
					cellEight.setCellValue(sb.toString());
				}
			}
			OutputStream out = response.getOutputStream();
			workbook.write(out);
			out.flush();
			out.close();
		} catch (Exception e) {
			System.out.println(e);
		}

	}

	@Autowired
	private ExamNumberManageService examNumberManageService;
	@Autowired
	private TeacherManageService teacherManageService;
	@Autowired
	private SchoolPlainAdminScoreSearchService schoolPlainAdminScoreSearchService;
	@Autowired
	private PlatformDataDictionaryService dictionaryService;
	@Autowired
	private ExamInfoSchoolService examInfoSchoolService;
	@Autowired
	private TeacherManagementService teacherManagementService;
	@Autowired
	private AdminExamNumberService adminExamNumverService;
	@Autowired
	private StuManagementService stuManagementService;
	@Autowired
	private MarkingArrangementService markingArrangementService;
}
