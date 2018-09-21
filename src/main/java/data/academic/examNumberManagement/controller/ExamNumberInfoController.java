/**
 * 2016年9月21日
 */
package data.academic.examNumberManagement.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import data.academic.examInfo.service.ExamInfoSchoolService;
import data.academic.examInfo.service.ExamInfoService;
import data.academic.examInfo.service.MarkingArrangementService;
import data.academic.examNumberManagement.service.AdminExamNumberService;
import data.academic.examNumberManagement.service.ExamNumberInfoService;
import data.academic.examNumberManagement.service.ExamNumberManageService;
import data.academic.studentManagement.service.StuManagementService;
import data.academic.transfer.service.StudentTransferService;
import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractBaseController;
import data.framework.support.ConfigContext;
import data.platform.authority.security.SecurityContext;

/**
 * @Title: ExamNumberInfoController
 * @Description: 学生生成考号控制层
 * @author zhaohuanhuan
 * @date 2016年9月21日 下午9:48:05
 */
@Controller
@RequestMapping("examNumberManagement/examNumberInfo")
public class ExamNumberInfoController extends AbstractBaseController {

	@Override
	protected void init(ModelMap model, HttpServletRequest request) {
		// TODO Auto-generated method stub

	}

	Map<String, String> paramMaps = new HashMap<String, String>();

	/**
	 * @Title: searchPaging
	 * @Description: 分页显示学生与考号的信息
	 * @author zhaohuanhuan
	 * @date 2016年9月27日
	 * @param data
	 * @param out
	 * @param request
	 * @return void
	 */
	@RequestMapping(params = "command=searchPaging")
	public void searchPaging(@RequestParam("data") String data, java.io.PrintWriter out, HttpServletRequest request) {
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		boolean isFast = parseBoolean(requestMap.get("isFast"));
		requestMap.put("nameOrXjfh", paramMaps.get("nameOrXjfh"));
		requestMap.put("gradeId", paramMaps.get("gradeId"));
		String stuName = "";
		String Xjfh = "";
		String schoolName="";
		if (isFast) {
			stuName = trimString(requestMap.get("nameOrXjfh"));
			Xjfh = trimString(requestMap.get("nameOrXjfh"));
		}
		String examNumberState = trimString(paramMaps.get("examNumberState"));
		String examNumber = paramMaps.get("examNumber");
		requestMap.put("examNumberState", examNumberState);
		requestMap.put("stuName", stuName);
		requestMap.put("Xjfh", Xjfh);
		requestMap.put("examNumber", examNumber);
		requestMap.put("chooseCourse", formatString(paramMaps.get("chooseCourse")));
		requestMap.put("testCaseGeneration", paramMaps.get("testCaseGeneration"));
		requestMap.put("notTestCaseGeneration", paramMaps.get("notTestCaseGeneration"));
		int currentPage = parseInteger(requestMap.get("page"));
		int pageSize = parseInteger(requestMap.get("rows"));
		String sortField = trimString(requestMap.get("sidx"));
		String sort = trimString("asc");
		String loginName = SecurityContext.getPrincipal().getUsername();
		String userId = SecurityContext.getPrincipal().getId();
		// 根据登录人的id得到其角色code
		String roleCode = examInfoService.getRoleByUserId(userId);
		List<String> schoolCodeList = new ArrayList<String>();
		Map<String, Object> getClassParamMap = new HashMap<>();
		String schoolCode = "";
		String loginUserSchoolCode = "";
		// 用于区分实验中学和豫才中学[实验中学西和东]
		int minClassNo = 0;
		int maxClassNo = 0;
		String yczxSchoolCode=""; //用于豫才中学查询数据
		if (ConfigContext.getStringSection("syzxAdmin").equals(loginName)) {
			minClassNo = 1;
			maxClassNo = 12;
		} else if (ConfigContext.getStringSection("yczxAdmin").equals(loginName)) {
			minClassNo = 21;
			maxClassNo = 27;
			schoolName=ConfigContext.getStringSection("yczxSchoolName");
			yczxSchoolCode=ConfigContext.getStringSection("syzxSchoolCode");
		}
		requestMap.put("minClassNo", minClassNo);
		requestMap.put("maxClassNo", maxClassNo);
		if ("".equals(parseString(paramMaps.get("schoolCode"))) || null == parseString(paramMaps.get("schoolCode"))) {
			if (ConfigContext.getValue("framework.tmis.roleCode['qpRoleAdminCode']").equals(roleCode)) {
				List<Map<String, Object>> schoolList = adminExamNumverService.getSchoolByExamCode(examNumber);
				requestMap.put("stuState", "2");
				requestMap.put("stuRefExam", "0");
				for (Map<String, Object> schoolMap : schoolList) {
					if(ConfigContext.getStringSection("yczxSchoolCode").equals(trimString(schoolMap.get("School_Code")))){
						schoolCodeList.add(ConfigContext.getStringSection("syzxSchoolCode"));
					}else{
						schoolCodeList.add(trimString(schoolMap.get("School_Code")));
					}
					
				}
			} else if(!"".equals(yczxSchoolCode)){
				schoolCode=yczxSchoolCode;
				schoolCodeList.add(schoolCode);
			}else{
				// 根据当前登录的用户名得到学校code
				schoolCode = examNumberManageService.getSchoolCodeByLoginName(loginName);
				loginUserSchoolCode = schoolCode;
				schoolCodeList.add(schoolCode);
			}
		} else {
			schoolCode = parseString(paramMaps.get("schoolCode"));
			requestMap.put("createSchoolCode", schoolCode);
			loginUserSchoolCode = schoolCode;
			if(ConfigContext.getStringSection("yczxSchoolCode").equals(schoolCode)){
				schoolCode=ConfigContext.getStringSection("syzxSchoolCode");
				schoolName=ConfigContext.getStringSection("yczxSchoolName");
			}
			schoolCodeList.add(schoolCode);

		}
		requestMap.put("schoolCodeList", schoolCodeList);
		// 得到此次考试的相关班级
		List<Map<String, Object>> classList = examInfoSchoolService.getClassIdByExamCode(examNumber);
		List<String> classIdList = new ArrayList<>();
		for (Map<String, Object> classMap : classList) {
			if (classMap != null) {
				classIdList.add(formatString(classMap.get("Class_Id")));
			}
		}
		requestMap.put("classIdList", classIdList);
		PagingResult<Map<String, Object>> pagingResult = examNumberInfoService.searchPaging(requestMap, sortField, sort,
				currentPage, pageSize);
		List<Map<String, Object>> list = pagingResult.getRows();
		List<Map<String, Object>> paramList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> schoolInfoList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> calssMapList = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : list) {
			String stuSchoolCode = studentTransferService.selectTeacherSchoolCode(map.get("Stu_Pk").toString());

			if (loginUserSchoolCode != null && !"".equals(loginUserSchoolCode)
					&& loginUserSchoolCode.equals(map.get("school"))) {
				if (!map.get("School_Code").equals(map.get("school"))) {
					map.put("School_Code", map.get("school"));
					schoolInfoList = markingArrangementService.getSchoolShortNameAndBrevityCode(map);
				} else {
					schoolInfoList = markingArrangementService.getSchoolShortNameAndBrevityCode(map);
				}
			} else {
				// 得到该学生原本的学校
				if (!map.get("School_Code").equals(map.get("school"))) {
					map.put("School_Code", map.get("school"));
					schoolInfoList = markingArrangementService.getSchoolShortNameAndBrevityCode(map);
				} else {
					schoolInfoList = markingArrangementService.getSchoolShortNameAndBrevityCode(map);
				}
			}
			if (schoolInfoList.size() > 0) {
				for (Map<String, Object> map2 : schoolInfoList) {
					map.put("School_Name", map2.get("School_Short_Name"));
					map.put("Brevity_Code", map2.get("Brevity_Code"));
				}
			}

			if (map.containsKey("Exam_Number_Isnull") == false) {
				map.put("Exam_Number_Isnull", "0");
			}
			if (map.containsKey("Exam_Code") == false) {
				map.put("Exam_Code", examNumber);
			}
			if ("2".equals(map.get("stuState"))) {
				if (loginUserSchoolCode.equals(stuSchoolCode)
						|| loginUserSchoolCode.equals(parseString(paramMaps.get("schoolCode")))) {
					getClassParamMap.put("sfzjh", map.get("SFZJH"));
					calssMapList = stuManagementService.getOldClassByStuSfzjh(getClassParamMap);
				}
			}
			/*
			 * 根据班级id得到学生所在班级
			 */
			if (calssMapList.size() > 0) {
				for (Map<String, Object> calssMap : calssMapList) {
					String classId = formatString(calssMap.get("Class_No"));
					String classIdOneStr = classId.substring(0, 1);
					if (classIdOneStr.equals("0")) {
						classId = classId.substring(classId.length() - 1);
					}
					map.put("Class_Id", classId);
					map.put("ClassName", formatString(calssMap.get("Class_No")));
					map.put("GradeName", calssMap.get("Grade_No"));
				}
			} else {
				String classId = formatString(map.get("Class_No"));
				String classIdOneStr = classId.substring(0, 1);
				if (classIdOneStr.equals("0")) {
					classId = classId.substring(classId.length() - 1);
				}
				map.put("Class_Id", classId);
				map.put("ClassName", formatString(map.get("Class_No")));
				map.put("GradeName", map.get("Grade_No"));
			}
			map.put("Sex", trimString(map.get("Sex")));
			map.put("stuState", trimString(map.get("stuState")));
			map.put("Is_Xjb", trimString(map.get("Is_Xjb")));
			if(ConfigContext.getStringSection("syzxSchoolCode").equals(map.get("School_Code"))&&
					parseInteger(map.get("Class_No"))>20){
				map.put("School_Name", ConfigContext.getStringSection("yczxSchoolName"));
			}
			if(!"".equals(schoolName)){
				map.put("School_Name", schoolName);
			}
			paramList.add(map);

		}
		PagingResult<Map<String, Object>> newPagingResult = new PagingResult<Map<String, Object>>(paramList,
				pagingResult.getPage(), pagingResult.getPageSize(), pagingResult.getRecords());
		out.print(getSerializer().formatObject(newPagingResult));
	}

	/**
	 * @Title: getStuExamNumber
	 * @Description: 得到此次考试学生考号的前6位
	 * @author zhaohuanhuan
	 * @date 2016年10月20日
	 * @param data
	 * @param out
	 * @return void
	 */

	@RequestMapping(params = "command=getStuExamNumber")
	public void getStuExamNumber(@RequestParam("data") String data, java.io.PrintWriter out) {
		Map<String, Object> paramMap = getSerializer().parseMap(data);
		String loginName = SecurityContext.getPrincipal().getUsername();
		String gradeId = parseString(paramMap.get("gradeId"));
		String schoolCode = "";
		String userId = SecurityContext.getPrincipal().getId();
		// 根据登录人的id得到其角色code
		String roleCode = examInfoService.getRoleByUserId(userId);
		if (ConfigContext.getValue("framework.tmis.roleCode['qpRoleAdminCode']").equals(roleCode)) {
			schoolCode = parseString(paramMap.get("schoolCode"));
		} else {
			schoolCode = examNumberManageService.getSchoolCodeByLoginName(loginName);
		}
		String examCode = parseString(paramMap.get("examNumber"));// 考试编号
		// 得到考试编号的后四位
		if (!"".equals(examCode) || null != examCode) {
			examCode = examCode.substring(examCode.length() - 4, examCode.length());
		}
		String brevityCode = "";
		if ("3004".equals(schoolCode)) {
			if ("16".equals(gradeId) || "17".equals(gradeId) || "18".equals(gradeId) || "19".equals(gradeId)) {
				brevityCode = "30";
			} else {
				brevityCode = "85";
			}
		} else {
			brevityCode = examNumberManageService.getBrevityCodeBySchoolCode(schoolCode);
			if (brevityCode.length() == 1) {
				brevityCode = "0" + brevityCode;
			}
		}
		StringBuffer sb = new StringBuffer();
		sb.append(examCode).append(brevityCode);
		String examNumber = sb.toString();
		out.print(getSerializer().formatObject(examNumber));
	}

	/**
	 * @Title: updateExamNumberById
	 * @Description: 根据id修改学生考号
	 * @author zhaohuanhuan
	 * @date 2016年9月27日
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=updateExamNumberById")
	public void updateExamNumberById(@RequestParam("data") String data, java.io.PrintWriter out) {
		Map<String, Object> paramMap = getSerializer().parseMap(data);
		if ("".equals(paramMap.get("examNumber").toString()) || null == paramMap.get("examNumber").toString()) {
			paramMap.put("examNumberIsNull", "0");// 表示未生成考号
		} else {
			paramMap.put("examNumberIsNull", "1");// 表示已生成考号
		}
		examNumberManageService.updateExamNumberById(paramMap);
		paramMap.put("mess", "success");
		out.print(getSerializer().formatMap(paramMap));
	}

	/**
	 * @Title: loadExamNumberById
	 * @Description: 根据id加载学生考号
	 * @author zhaohuanhuan
	 * @date 2016年9月27日
	 * @param request
	 * @param out
	 * @throws Exception
	 * @return void
	 */

	@RequestMapping(params = "command=loadExamNumberById")
	public void loadExamNumberById(HttpServletRequest request, java.io.PrintWriter out) throws Exception {
		String id = request.getParameter("id");
		List<Map<String, Object>> list = examNumberManageService.getExamNumberById(id);
		out.print(getSerializer().formatList(list));
	}

	/**
	 * @Title: getImportParams
	 * @Description: 点击查询的时候得到参数
	 * @author zhaohuanhuan
	 * @date 2016年9月27日
	 * @param data
	 * @param out
	 * @return void
	 */

	@RequestMapping(params = "command=getImportParams")
	public void getImportParams(@RequestParam("data") String data, java.io.PrintWriter out) {
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		paramMaps = new HashMap<String, String>();
		Map<String, Object> subExamMap = new HashMap<String, Object>();
		String schoolCode = "";
		String examNumberState = "";
		String stuNameOrXjfh = "";
		List<String> schoolCodeList = new ArrayList<>();
		String gradeId = "";
		String loginName = SecurityContext.getPrincipal().getUsername();
		int minClassNo = 0;
		int maxClassNo = 0;
		if (requestMap.containsKey("schoolCode")) {
			if ("".equals(requestMap.get("schoolCode")) || null == requestMap.get("schoolCode")) {
				schoolCodeList = adminExamNumverService
						.getSchoolCodeAndExistNumber(requestMap.get("examNumber").toString());
				if(ConfigContext.getStringSection("yczxSchoolCode").equals(schoolCode)){
					schoolCodeList.add(ConfigContext.getStringSection("syzxSchoolCode"));
				}
				subExamMap.put("stuState", "2");
				subExamMap.put("stuRefExam", "0");
			} else {
				if (ConfigContext.getValue("framework.tmis.roleCode['qpOrgCode']")
						.equals(requestMap.get("schoolCode"))) {
					// 根据当前登录的用户名得到学校code
					schoolCode = examNumberManageService.getSchoolCodeByLoginName(loginName);
					subExamMap.put("createSchoolCode", schoolCode);
				} else {
					schoolCode = requestMap.get("schoolCode").toString();
					subExamMap.put("createSchoolCode", schoolCode);
				}
				List<String> existExamNumberSchoolCodeList = adminExamNumverService
						.getSchoolCodeAndExistNumber(requestMap.get("examNumber").toString());
				if (existExamNumberSchoolCodeList.contains(schoolCode)) {
					if(ConfigContext.getStringSection("yczxSchoolCode").equals(schoolCode)){
						schoolCodeList.add(schoolCode);
						schoolCodeList.add(ConfigContext.getStringSection("syzxSchoolCode"));
						minClassNo = 21;
						maxClassNo = 27;
					}else if(ConfigContext.getStringSection("syzxSchoolCode").equals(schoolCode)){
						schoolCodeList.add(schoolCode);
						minClassNo = 1;
						maxClassNo = 12;
					}else{
						schoolCodeList.add(schoolCode);
					}
				}
			}
		} else {
			schoolCode = examNumberManageService.getSchoolCodeByLoginName(loginName);
			subExamMap.put("createSchoolCode", schoolCode);
			if(ConfigContext.getStringSection("yczxSchoolCode").equals(schoolCode)){
				schoolCodeList.add(ConfigContext.getStringSection("yczxSchoolCode"));
				minClassNo = 21;
				maxClassNo = 27;
			}else if(ConfigContext.getStringSection("syzxSchoolCode").equals(schoolCode)){
				minClassNo = 1;
				maxClassNo = 12;
				schoolCodeList.add(schoolCode);
			}else{
				schoolCodeList.add(schoolCode);
			}
			List<Map<String, Object>> classList = examInfoSchoolService
					.getClassIdByExamCode(parseString(requestMap.get("examNumber")));
			List<String> classIdList = new ArrayList<>();
			for (Map<String, Object> classMap : classList) {
				if (classMap != null) {
					classIdList.add(formatString(classMap.get("Class_Id")));
				}
			}
			subExamMap.put("classIdList", classIdList);
		}
		
		if (ConfigContext.getStringSection("syzxAdmin").equals(loginName)) {
			minClassNo = 1;
			maxClassNo = 12;
		} else if (ConfigContext.getStringSection("yczxAdmin").equals(loginName)) {
			minClassNo = 21;
			maxClassNo = 27;
		}
		subExamMap.put("minClassNo", minClassNo);
		subExamMap.put("maxClassNo", maxClassNo);
		paramMaps.put("schoolCode", schoolCode);
		stuNameOrXjfh = formatString(requestMap.get("nameOrXjfh"));
		paramMaps.put("nameOrXjfh", formatString(requestMap.get("nameOrXjfh")));
		examNumberState = formatString(requestMap.get("examNumberState"));
		paramMaps.put("examNumberState", examNumberState);
		gradeId = formatString(requestMap.get("gradeCode"));
		paramMaps.put("gradeId", gradeId);
		subExamMap.put("chooseCourse", formatString(requestMap.get("chooseCourse")));
		paramMaps.put("examNumber", formatString(requestMap.get("examNumber")));
		if (schoolCodeList.size() > 0) {
			subExamMap.put("schoolCodeList", schoolCodeList);
			subExamMap.put("gradeId", gradeId);
			subExamMap.put("examCode", formatString(paramMaps.get("examNumber")));
			subExamMap.put("examNumberIsnull", examNumberState);
			subExamMap.put("stuNameOrXjfh", stuNameOrXjfh);
			// 得到某学校年级班级下所有学生总数
			Integer isTestCaseGeneration = examNumberManageService.countAllStuBySchoolCodeAndGrade(subExamMap);
			// 得到某学校年级班级下生成考号的学生总数
			subExamMap.put("examNumberIsExist", 1);
			Integer testCaseGeneration = examNumberManageService.countAllStuBySchoolCodeAndGrade(subExamMap);
			// 得到某学校年级班级下未生成考号的学生总数
			Integer notTestCaseGeneration = isTestCaseGeneration - testCaseGeneration;
			requestMap.put("isTestCaseGeneration", isTestCaseGeneration);
			requestMap.put("testCaseGeneration", testCaseGeneration);
			requestMap.put("notTestCaseGeneration", notTestCaseGeneration);
		} else {
			requestMap.put("message", "false");
		}
		paramMaps.put("chooseCourse", formatString(requestMap.get("chooseCourse")));
		paramMaps.put("testCaseGeneration", formatString(requestMap.get("testCaseGeneration")));
		paramMaps.put("notTestCaseGeneration", formatString(requestMap.get("notTestCaseGeneration")));
		paramMaps.put("isTestCaseGeneration", formatString(requestMap.get("isTestCaseGeneration")));
		out.print(getSerializer().formatMap(requestMap));
	}

	/**
	 * @Title: selectIdByAssociatedExamNumber
	 * @Description: 根据考试编号得到其附件
	 * @author zhaohuanhuan
	 * @date 2016年10月17日
	 * @param data
	 * @param out
	 * @return void
	 */

	@RequestMapping(params = "command=selectIdByAssociatedExamNumber")
	public void selectIdByAssociatedExamNumber(@RequestParam("data") String data, java.io.PrintWriter out) {
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("examNumber", requestMap.get("examCode"));
		List<Map<String, Object>> requestList = examNumberManageService.selectIdByAssociatedExamNumber(paramMap);
		out.print(getSerializer().formatList(requestList));
	}

	@Autowired
	private ExamNumberInfoService examNumberInfoService;

	@Autowired
	private ExamNumberManageService examNumberManageService;

	@Autowired
	private ExamInfoService examInfoService;
	@Autowired
	private AdminExamNumberService adminExamNumverService;
	@Autowired
	private ExamInfoSchoolService examInfoSchoolService;
	@Autowired
	private MarkingArrangementService markingArrangementService;
	@Autowired
	private StuManagementService stuManagementService;
	@Autowired
	private StudentTransferService studentTransferService;
}
