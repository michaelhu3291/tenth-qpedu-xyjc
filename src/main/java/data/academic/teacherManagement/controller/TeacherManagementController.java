package data.academic.teacherManagement.controller;

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

import data.academic.examInfo.service.ExamInfoSchoolService;
import data.academic.examNumberManagement.service.ExamNumberManageService;
import data.academic.statisticsAnalysis.service.SchoolPlainAdminScoreSearchService;
import data.academic.studentManagement.service.StuManagementService;
import data.academic.teacherManagement.service.TeacherManagementService;
import data.academic.transfer.service.StudentTransferService;
import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractBaseController;
import data.framework.support.ConfigContext;
import data.platform.authority.security.SecurityContext;

/**
 * @Title: TeacherManagementController
 * @Description: 教师学校控制层
 * @author zhaohuanhuan
 * @date 2016年7月29日
 */
@RestController
@RequestMapping("teaManagement/teacherInfo")
public class TeacherManagementController extends AbstractBaseController {
	@Autowired
	private TeacherManagementService teaManagementService;
	@Autowired
	private ExamNumberManageService examNumberManageService;
	@Autowired
	private SchoolPlainAdminScoreSearchService schoolPlainAdminScoreSearchService;
	@Autowired
	private StuManagementService stuManagementService;
	@Autowired
	private ExamInfoSchoolService examInfoSchoolService;
	@Autowired
	private StudentTransferService studentTransferService;

	@Override
	protected void init(ModelMap model, HttpServletRequest request) {

	}

	/**
	 * @Title: searchPaging
	 * @Description: 分页查询教师，显示教师所在学校，所带班级，所教科目
	 * @author zhaohuanhuan
	 * @date 2016年7月29日
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=searchPaging")
	public void searchPaging(@RequestParam("data") String data, java.io.PrintWriter out) {
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		// schoolType=1, school=3063
		boolean isFast = parseBoolean(requestMap.get("isFast"));
		String loginName = "";
		String teacherName = "";
		if (!isFast) {
			loginName = trimString(requestMap.get("q"));
			teacherName = trimString(requestMap.get("q"));
		}
		List<Map<String, Object>> schoolCodeList = teaManagementService
				.getSchoolCodeByLoginName(SecurityContext.getPrincipal().getUsername());// 根据登录名得到学校code
		requestMap.put("loginName", loginName);
		requestMap.put("teacherName", teacherName);
		requestMap.put("schoolCodeList", schoolCodeList);
		int currentPage = parseInteger(requestMap.get("page"));
		int pageSize = parseInteger(requestMap.get("rows"));
		String sortField = trimString(requestMap.get("sidx"));
		String sort = trimString(requestMap.get("sord"));
		// 得到所有的教师集合
		PagingResult<Map<String, Object>> pagingResult = teaManagementService.searchPaging(requestMap, sortField, sort,
				currentPage, pageSize);
		List<Map<String, String>> newList = new ArrayList<Map<String, String>>();
		String teacherIds, schoolName, className, courseName = "";
		List<Map<String, String>> schoolList = new ArrayList<>();
		List<Map<String, String>> classList = new ArrayList<>();
		List<Map<String, String>> courseList = new ArrayList<>();
		List<Map<String, Object>> list = pagingResult.getRows();
		for (Map<String, Object> teacherIdmap : list) {
			int schoolNum = 1;
			int classNum = 1;
			int courseNum = 1;
			StringBuffer schools = new StringBuffer();
			StringBuffer classs = new StringBuffer();
			StringBuffer courses = new StringBuffer();
			Map<String, String> map = new HashMap<String, String>();
			teacherIds = (String) teacherIdmap.get("Teacher_Pk");
			String teaName = (String) teacherIdmap.get("Teacher_Name");
			String teaLoginName = (String) teacherIdmap.get("Login_Name");
			String teaType = (String) teacherIdmap.get("Teacher_Position");
			map.put("id", teacherIds);
			map.put("teaName", teaName);
			map.put("loginName", teaLoginName);
			map.put("teaType", teaType);
			// 根据当前老师的id得到所拥有的学校集合
			schoolList = teaManagementService.getSchoolByTeaId(teacherIds);
			// 根据当前老师的id得到所拥有的班级集合
			classList = teaManagementService.getClassByTeaId(teacherIds);
			// 根据当前老师的id得到所拥有的科目集合
			courseList = teaManagementService.getCourseByTeaId(teacherIds);

			if (schoolList.size() > 0) {
				for (Map<String, String> schoolMap : schoolList) {
					if (schoolMap != null) {
						schoolName = schoolMap.get("School_Name");
						if (StringUtils.isNotBlank(schoolName)) {
							if (1 < schoolNum && schoolNum <= schoolList.size()) {
								schools.append(",");
							}
							schools.append(schoolName);
							schoolNum++;
						}

					} else {
						schoolName = "";
					}
				}
			}
			if (classList.size() > 0) {
				for (Map<String, String> classMap : classList) {
					className = classMap.get("Class_Name");
					if (1 < classNum && classNum <= classList.size()) {
						classs.append(",");
					}
					classs.append(className);
					classNum++;
				}
			}
			if (courseList.size() > 0) {
				for (Map<String, String> courseMap : courseList) {
					courseName = courseMap.get("Course_Name");
					if (1 < courseNum && courseNum <= courseList.size()) {
						courses.append(",");
					}
					courses.append(courseName);
					courseNum++;
				}
			}

			map.put("schoolName", schools.toString());
			map.put("className", classs.toString());
			map.put("courseName", courses.toString());
			newList.add(map);
		}
		PagingResult<Map<String, String>> newPagingResult = new PagingResult<Map<String, String>>(newList,
				pagingResult.getPage(), pagingResult.getPageSize(), pagingResult.getRecords());
		out.print(getSerializer().formatObject(newPagingResult));
	}

	/**
	 * @Title: getSchoolByTeaId
	 * @Description: 得到已选择学校和待选择学校
	 * @author zhaohuanhuan
	 * @date 2016年7月29日
	 * @param request
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=getSchoolById")
	public void getSchoolByTeaId(HttpServletRequest request, java.io.PrintWriter out) {
		String id = request.getParameter("id");// 教师id
		// 得到当前老师所在学校
		List<Map<String, String>> schoolByIdList = teaManagementService.getSchoolByTeaId(id);

		List<String> teaSchool = new ArrayList<>();
		for (Map<String, String> school1 : schoolByIdList) {
			if (school1 != null) {
				teaSchool.add(school1.get("School_Code"));
			}
			/*
			 * if(StringUtils.isNotBlank(school1.get("School_Id"))){
			 * 
			 * }
			 */
		}
		// 过滤当前教师所在的学校
		List<Map<String, String>> schoolList = teaManagementService.getSchool(teaSchool);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("schoolByIdList", schoolByIdList);// 已选择学校的集合
		resultMap.put("schoolList", schoolList);// 可选择的学校集合
		out.print(getSerializer().formatMap(resultMap));
	}

	/**
	 * @Title: saveSchool
	 * @Description: 给当前老师添加学校
	 * @author zhaohuanhuan
	 * @date 2016年7月29日
	 * @param data
	 * @param out
	 * @return void
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(params = "command=saveSchool")
	public void saveSchool(@RequestParam("data") String data, java.io.PrintWriter out) {
		Map<String, Object> paramMap = getSerializer().parseMap(data);
		// 教师id
		String teacherId = parseString(paramMap.get("teacherId"));
		// 得到当前选中的学校id集合
		List<String> schoolIds = (List<String>) paramMap.get("schoolIds");
		// 删除老师开始拥有的学校
		teaManagementService.removeSchool(teacherId);
		if (schoolIds != null) {
			for (String schools : schoolIds) {
				// 添加现在选中的学校
				teaManagementService.teaReSchool(teacherId, schools);
			}
		}

		Map<String, String> map = new HashMap<String, String>();
		map.put("message", "保存成功");
		out.print(getSerializer().formatObject(map));
	}

	/**
	 * @Title: getClassById
	 * @Description: 得到已选择班级和待选择班级
	 * @author zhaohuanhuan
	 * @date 2016年7月29日
	 * @param request
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=getClassById")
	public void getClassById(HttpServletRequest request, java.io.PrintWriter out) {
		// 根据登录名查询schoolCode
		String loginName = SecurityContext.getPrincipal().getUsername();
		String schoolCode = examNumberManageService.getSchoolCodeByLoginName(loginName);
		int minClassNo = 0;
		int maxClassNo = 0;
		if (ConfigContext.getStringSection("syzxSchoolCode").equals(schoolCode)) {
			minClassNo = 1;
			maxClassNo = 12;
		} else if (ConfigContext.getStringSection("yczxSchoolCode").equals(schoolCode)) {
			schoolCode = ConfigContext.getStringSection("syzxSchoolCode");
			minClassNo = 21;
			maxClassNo = 27;
		}
		Map<String, Object> paraMap=new HashMap<>();
		paraMap.put("schoolCode", schoolCode);
		paraMap.put("minClassNo", minClassNo);
		paraMap.put("maxClassNo", maxClassNo);
		// 当前年
		// 通过school_code查询该学校所有班级和年级
		List<Map<String, String>> allClassList = teaManagementService.selectAllClassBySchoolCode(paraMap);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("classList", allClassList);// 可选择的班级集合
		out.print(getSerializer().formatMap(resultMap));

	}

	/**
	 * @Title: saveClass
	 * @Description: 给当前老师添加班级
	 * @author zhaohuanhuan
	 * @date 2016年7月29日
	 * @param data
	 * @param out
	 * @return void
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(params = "command=saveClass")
	public void saveClass(@RequestParam("data") String data, java.io.PrintWriter out) {
		Map<String, Object> paramMap = getSerializer().parseMap(data);
		// 教师id
		// String teacherId=parseString(paramMap.get("teacherId"));
		// 当前年
		String time = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		String currentYear = time.substring(0, time.indexOf("-"));
		List<String> paramIdList = new ArrayList<>();
		paramMap.put("currentYear", currentYear);
		Map<String, Object> map = new HashMap<String, Object>();
		List<String> schoolCodeList = new ArrayList<>();
		List<String> classNoList = new ArrayList<>();
		List<List<String>> erArr = new ArrayList<>();
		List<Map<String, Object>> examInfoMapList = new ArrayList<>();
		String update_person = SecurityContext.getPrincipal().getId();
		String loginName = SecurityContext.getPrincipal().getUsername();
		String schoolCode = examNumberManageService.getSchoolCodeByLoginName(loginName);
		String roleCode = parseString(paramMap.get("roleCode"));
		String classId = parseString(paramMap.get("classId"));
		String nowSchoolCode = examNumberManageService
				.getSchoolCodeByLoginName(SecurityContext.getPrincipal().getUsername());
		if (ConfigContext.getValue("framework.tmis.transfer['studentHistory']").equals(roleCode)) {

			schoolCodeList.add(schoolCode);
			schoolCodeList.add(ConfigContext.getValue("framework.tmis.roleCode['qpOrgCode']"));
			map = new HashMap<String, Object>();
			map.put("schoolCodeList", schoolCodeList);
			map.put("gradeNo", paramMap.get("gradeNo"));
			String ClassNo = paramMap.get("classNo").toString();
			// 匹配该学生所在年级，班级是否已经生成考号
			// 根据年级和学校code得到考试信息
			examInfoMapList = examNumberManageService.getExamCodeByStuInfo(map);
			List<Map<String, Object>> stuIdList = (List<Map<String, Object>>) paramMap.get("studentIds");
			for (Map<String, Object> stuInfoMap : stuIdList) {
				String createSchooCode = studentTransferService
						.selectTeacherSchoolCode(stuInfoMap.get("stuPk").toString());
				map.clear();
				map.put("schoolCode", nowSchoolCode);
				map.put("studentId", stuInfoMap.get("stuPk"));
				String state = stuManagementService.getCallOutStudentById(map);
				// 处理学生考号
				for (Map<String, Object> examInfoMap : examInfoMapList) {
					examInfoMap.put("createSchoolCode", createSchooCode);
					examInfoMap.put("Create_Person", loginName);
					// 根据考试编号和创建人得到该学生所在学校年级班级是否生成考号
					int isExistExamNumber = examNumberManageService.getExamNumberByStuInfo(examInfoMap);
					examInfoMap.put("sfzjh", stuInfoMap.get("sfzjh"));
					examInfoMap.put("xjfh", stuInfoMap.get("xjfh"));
					examInfoMap.put("schoolCode", schoolCode);
					examInfoMap.put("createSchoolCode", createSchooCode);
					if (isExistExamNumber > 0) {

						// 区级发布的考试
						if (ConfigContext.getValue("framework.tmis.roleCode['qpOrgCode']")
								.equals(examInfoMap.get("School_Code"))) {
							if ("4".equals(state)) {

								examNumberManageService.updateExamNumberInfo(examInfoMap);
							}
							// 添加该学生到学生考号关系表中
							examNumberManageService.insertExamNumberForNewStuInfo(examInfoMap);
						} else {// 校级发布的考试
								// 根据考试编号得到某次考试下面的所有班级
							List<Map<String, Object>> classMapList = examInfoSchoolService
									.getClassIdByExamCode(examInfoMap.get("Exam_Number").toString());
							for (Map<String, Object> map2 : classMapList) {
								classNoList.add(map2.get("Class_Id").toString());
							}
							if (classNoList.size() > 0) {
								// 判断当前这个学生所在班级是否在学校发布的考试中
								if (classNoList.contains(ClassNo)) {
									if ("4".equals(state)) {
										examNumberManageService.updateExamNumberInfo(examInfoMap);
									}
									// 添加该学生到学生考号关系表中
									examNumberManageService.insertExamNumberForNewStuInfo(examInfoMap);
								}
							}
						}
					}
				}

				if ("4".equals(state)) {// 转入
					paramMap.put("studentId", stuInfoMap.get("sfzjh"));
					paramMap.put("classId", classId);
					paramMap.put("updatePerson", update_person);
					stuManagementService.updateClassByStudentId(paramMap);
				} else {// 转入
					Map<String, Object> classInfoMap = examNumberManageService.getClassInfoByClassId(classId);
					classInfoMap.put("studentId", stuInfoMap.get("stuPk"));
					classInfoMap.put("updatePerson", loginName);
					stuManagementService.updateStudentClass(classInfoMap);
					classInfoMap.put("classId", classInfoMap.get("Class_Pk"));
					classInfoMap.put("studentId", stuInfoMap.get("sfzjh"));
					stuManagementService.updateClassByStudentId(classInfoMap);
				}
			}

		}
		if (ConfigContext.getValue("framework.tmis.transfer['teacherHistory']").equals(roleCode)) {
			paramIdList = (List<String>) paramMap.get("teacherIds");
			erArr = (List<List<String>>) paramMap.get("classIds");
			paramMap.put("teacherIds", paramIdList);
			// 删除该教师开始拥有的班级
			teaManagementService.removeClass(paramMap);
			if (paramIdList.size() > 0) {
				for (String teacherId : paramIdList) {
					map.put("teacherId", teacherId);
					for (List<String> yiArr : erArr) {
						map.put("class", yiArr.get(0));
						map.put("grade", yiArr.get(1));
						map.put("currentYear", currentYear);
						// 给老师添加班级
						teaManagementService.addClassForTeacher(map);
					}
				}
			}
		}

		Map<String, String> maps = new HashMap<String, String>();
		map.put("message", "保存成功");
		out.print(getSerializer().formatObject(maps));
	}

	/**
	 * @Title: getCourseById
	 * @Description: 得到已选择科目和待选择科目
	 * @author zhaohuanhuan
	 * @date 2016年7月29日
	 * @param request
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=getCourseById")
	public void getCourseById(HttpServletRequest request, java.io.PrintWriter out) {
		// 根据登录名查询schoolCode
		String loginName = SecurityContext.getPrincipal().getUsername();
		String schoolCode = examNumberManageService.getSchoolCodeByLoginName(loginName);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 通过学校code查询学校类型
		String schoolSequence = schoolPlainAdminScoreSearchService.findSchoolTypeBySchoolCode(schoolCode);
		// 得到学校类型
		if ("0".equals(schoolSequence)) {
			paramMap.put("schoolType", "xx");
		} else if ("1".equals(schoolSequence)) {
			paramMap.put("schoolType", "cz");
		} else if ("2".equals(schoolSequence)) {
			paramMap.put("schoolType", "gz");
		} else if ("3".equals(schoolSequence)) {
			paramMap.put("schoolType", "gz");
		} else if ("4".equals(schoolSequence)) {
			paramMap.put("schoolType", "wz");
		} else if ("5".equals(schoolSequence)) {
			paramMap.put("schoolType", "ygz");
		} else if ("2".equals(schoolSequence) || "3".equals(schoolSequence) || "4".equals(schoolSequence)) {
			if ("3062".equals(schoolCode)) {
				paramMap.put("schoolType", "cz");
			}

			paramMap.put("schoolType", "gz");
		}

		// 通过学校类型查询科目(过滤掉老师已选的科目)
		List<Map<String, String>> courseList = teaManagementService.selectCoursesBySchoolType(paramMap);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("courseList", courseList);// 可选择的科目集合
		out.print(getSerializer().formatMap(resultMap));

	}

	/**
	 * @Title: saveCourse
	 * @Description: 给当前老师添加科目
	 * @author zhaohuanhuan
	 * @date 2016年7月29日
	 * @param data
	 * @param out
	 * @return void
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(params = "command=saveCourse")
	public void saveCourse(@RequestParam("data") String data, java.io.PrintWriter out) {
		Map<String, Object> paramMap = getSerializer().parseMap(data);
		Map<String, Object> resultMap = new HashMap<>();
		// 当前年
		String time = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		String currentYear = time.substring(0, time.indexOf("-"));
		// 得到科目id集合，用于添加学校
		List<String> teacherIds = (List<String>) paramMap.get("teacherIds");
		List<String> courseIds = (List<String>) paramMap.get("courseIds");
		String teacherType = parseString(paramMap.get("teacherType"));
		paramMap.put("teacherIds", teacherIds);
		paramMap.put("currentYear", currentYear);
		// 删除当前老师开始所在的科目
		teaManagementService.removeCourse(paramMap);
		if (teacherIds != null) {
			for (String teacherId : teacherIds) {
				resultMap.put("teacherId", teacherId);
				// 重新给老师添加学校
				if (courseIds != null) {
					for (String courseId : courseIds) {
						resultMap.put("courseId", courseId);
						resultMap.put("teacherType", teacherType);
						resultMap.put("currentYear", currentYear);
						teaManagementService.teaReCourse(resultMap);
					}
				}
			}
		}

		Map<String, String> map = new HashMap<String, String>();
		map.put("message", "保存成功");
		out.print(getSerializer().formatObject(map));
	}

	/**
	 * @Title: saveSchool
	 * @Description: 根据登录用户显示其所属的学校类型和学校名称
	 * @author xiahuajun
	 * @date 2016年8月30日
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=getSchoolTypeAndSchoolName")
	public void getSchoolTypeAndSchoolName(@RequestParam("data") String data, java.io.PrintWriter out) {

		String username = SecurityContext.getPrincipal().getUsername();
		// 权限判断是超级管理员还是学校管理员
		// [{DictionaryName=小学, DictionaryCode=xx}, {DictionaryName=初中,
		// DictionaryCode=cz}, {DictionaryName=高中, DictionaryCode=gz}]
		if ("admin".equals(username)) {
			// 查询学校类型(小学，初中，高中)
			List<Map<String, Object>> schoolTypeList = teaManagementService.selectSchoolTypeList("xxlx");
			schoolTypeList.get(0).put("mess", "admin");

			List<Map<String, Object>> paramList = new ArrayList<Map<String, Object>>();
			for (Map<String, Object> map : schoolTypeList) {
				if ("xx".equals(map.get("DictionaryCode").toString())) {
					map.put("DictionaryCode", "2");
				} else if ("cz".equals(map.get("DictionaryCode").toString())) {
					map.put("DictionaryCode", "3");
				} else if ("gz".equals(map.get("DictionaryCode").toString())) {
					map.put("DictionaryCode", "4");
				}
				paramList.add(map);
			}
			out.print(getSerializer().formatList(schoolTypeList));
		} else {
			List<Map<String, Object>> list = teaManagementService.getSchoolTypeAndName(username);
			// School_Name=上海市青浦豫苗幼儿园, School_Type_Sequence=1
			List<Map<String, Object>> paramList = new ArrayList<Map<String, Object>>();
			for (Map<String, Object> map : list) {
				if ("1".equals(map.get("School_Type_Sequence").toString())) {
					map.put("School_Type", "幼儿园");
				} else if ("2".equals(map.get("School_Type_Sequence").toString())) {
					map.put("School_Type", "小学");
				} else if ("3".equals(map.get("School_Type_Sequence").toString())) {
					map.put("School_Type", "初中");
				} else if ("4".equals(map.get("School_Type_Sequence").toString())) {
					map.put("School_Type", "高中");
				}
				paramList.add(map);
			}
			out.print(getSerializer().formatList(paramList));
		}

	}
}
