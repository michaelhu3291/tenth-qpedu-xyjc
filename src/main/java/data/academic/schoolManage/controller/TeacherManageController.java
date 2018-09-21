package data.academic.schoolManage.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import data.academic.examNumberManagement.service.ExamNumberManageService;
import data.academic.schoolManage.service.TeacherManageService;
import data.academic.teacherManagement.service.TeacherManagementService;
import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractBaseController1;
import data.framework.support.ConfigContext;
import data.framework.utility.EncryptHelper;
import data.platform.authority.security.SecurityContext;

@RestController
@RequestMapping("schoolManage/teacherManage")
public class TeacherManageController extends AbstractBaseController1{
	
	@Autowired
	private TeacherManageService teacherManageService;
	@Autowired
	private ExamNumberManageService examNumberManageService;
	@Autowired
	private TeacherManagementService teacherManagementService;
	@RequestMapping
	protected ModelAndView initialize(ModelMap model, HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException{
		String loginName = SecurityContext.getPrincipal().getUsername();
		//根据登录名查询角色Code
		String roleCode = teacherManagementService.selectRoleCodeByLoginName(loginName);
		if(roleCode.equals(ConfigContext.getValue("framework.tmis.roleCode['qpRoleAdminCode']")) || roleCode.equals(ConfigContext.getValue("framework.tmis.roleCode['qpRoleQuAdminCode']"))) {
			request.getRequestDispatcher("/transfer/teacherTransfer.do").forward(request,response);
		} 
		return new ModelAndView();
	}
	
	/**
	 * @Title: searchPaging
	 * @Description: 分页查询教师，显示教师所在学校，所带班级
	 * @author xiahuajun
	 * @date 2016年9月1日 
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=searchPaging")
	public void searchPaging(@RequestParam("data") String data,java.io.PrintWriter out){
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		//schoolType=1, school=3063
		boolean isFast = parseBoolean( requestMap.get( "isFast" ) ) ;
	    String loginName = "" ;
	    String teacherName="";
	    String teacherCourse="";
	    String teacherGrade="";
	    String loginName1 = SecurityContext.getPrincipal().getUsername();
	    if( isFast ){
	    	loginName = trimString( requestMap.get( "q" ) ) ;
	    	teacherName = trimString( requestMap.get( "q" ) ) ;
	    }else{
	    	teacherCourse=formatString(requestMap.get( "teacherCourse" ) );
	    	teacherGrade=formatString(requestMap.get( "teacherGrade" ) );
	    }
	    //List<Map<String, Object>> schoolCodeList=teacherManageService.getSchoolCodeByLoginName(SecurityContext.getPrincipal().getUsername());//根据登录名得到学校code
	    String schoolCode = examNumberManageService.getSchoolCodeByLoginName(loginName1);
	    List<String> schoolCodeList = new ArrayList<>();
	    schoolCodeList.add(schoolCode);
	    requestMap.put("loginName", loginName);   
	    requestMap.put("teacherName", teacherName);  
	    requestMap.put("schoolCodeList", schoolCodeList);
	    requestMap.put("teacherCourse", teacherCourse);
	    requestMap.put("teacherGrade", teacherGrade);
		int currentPage = parseInteger(requestMap.get("page"));
		int pageSize = parseInteger(requestMap.get("rows"));
		String sortField = trimString(requestMap.get("sidx"));
		String sort = trimString(requestMap.get("sord"));
		//当前年
        String time = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		String currentYear = time.substring(0, time.indexOf("-"));
		requestMap.put("currentYear", currentYear);
		//得到所有的教师集合
		PagingResult<Map<String, Object>> pagingResult = teacherManageService.searchPaging(requestMap, sortField, sort, currentPage, pageSize);
		List<Map<String,Object>> newList = new ArrayList<Map<String,Object>>() ;
		String teacherIds, schoolName,teaName;
		List<Map<String,String>> schoolList=new ArrayList<>();
		List<Map<String, String>> classList=new ArrayList<>();
		List<Map<String, String>> courseList=new ArrayList<>();
		List<Map<String, Object>>  list=pagingResult.getRows();
		for (Map<String, Object> teacherIdmap : list) {
			int schoolNum=1;
//			int classNum=1;
//			int courseNum=1;
			StringBuffer schools=new StringBuffer();
//			StringBuffer classs=new StringBuffer();
//			StringBuffer courses=new StringBuffer();
			Map<String,Object> map = new HashMap<String,Object>() ;
			teacherIds=(String) teacherIdmap.get("Teacher_Pk");
			tea_state = teacherIdmap.get("state").toString();
			teaName = teacherIdmap.get("Teacher_Name").toString();
			boolean empty = tea_state.isEmpty();
			if (!empty) {
				if ("1".equals(tea_state)) {
					teaName = teaName+"(社招)";
				}else if ("2".equals(tea_state)) {
					teaName = teaName+"(已调动)";
				}else if ("3".equals(tea_state)) {
					teaName = teaName+"(待调动)";
				}else if ("4".equals(tea_state)) {
					teaName = teaName+"(外校调入)";
				}
			}
			
			String teaLoginName=(String) teacherIdmap.get("Login_Name");
			String teaType=(String) teacherIdmap.get("Teacher_Position");
			
			map.put("id", teacherIds);
			map.put("state", teacherIdmap.get("state").toString());
			map.put("teaName", teaName);
			map.put("loginName", teaLoginName);
			map.put("teaType", teaType);
			//根据当前老师的id得到所拥有的学校集合
			schoolList=teacherManageService.getSchoolByTeaId(teacherIds);
			//根据当前老师的id得到所拥有的班级集合
			classList=teacherManageService.getClassByTeaId(teacherIds,currentYear);
			//根据当前老师的id得到所拥有的科目集合
			courseList=teacherManageService.getCourseByTeaId(teacherIds,currentYear);
			if(schoolList.size()>0 ){
				for (Map<String, String> schoolMap : schoolList) {
					if(schoolMap!=null){
						schoolName=schoolMap.get("School_Short_Name");
						if(StringUtils.isNotBlank(schoolName)){
							if(1<schoolNum  && schoolNum<=schoolList.size()){
								schools.append(",");
							}
							schools.append(schoolName);
							schoolNum++;
						}
						
					}else{
						schoolName="";
					}
				}
			}
			/*if(classList.size()>0){
				for (Map<String, String> classMap : classList) {
					className=classMap.get("Class_Id");
					String classNames="";
					String classIdOneStr=className.substring(0, 1);
					if(classIdOneStr.equals("0")){
						classNames=className.substring(className.length()-1);
						classNames=classMap.get("Grade_Id")+"("+classNames+")班";
					}else{
						classNames=classMap.get("Grade_Id")+"("+classNames+")班";
					}
					if(1<classNum  && classNum<=classList.size()){
						classs.append(",");
					}
					classs.append(classNames);
					classNum++;
				}
			}*/
			/*if(courseList.size()>0){
				for (Map<String, String> courseMap : courseList) {
					courseName=courseMap.get("Course_Name");
					if(1<courseNum  && courseNum<=courseList.size()){
						courses.append(",");
					}
					courses.append(courseName);
					courseNum++;
				}
			}*/
				
			
			map.put("schoolName", schools.toString());
			//map.put("className", classs.toString());
			map.put("className", classList);
			//map.put("courseName", courses.toString());
			map.put("courseName", courseList);
			newList.add( map ) ;
		}
		PagingResult<Map<String,Object>> newPagingResult = 
				new PagingResult<Map<String,Object>>( newList, pagingResult.getPage(), pagingResult.getPageSize(), pagingResult.getRecords() ) ;
		out.print(getSerializer().formatObject(newPagingResult));
		}
	
	/**
	 * @Title: deleteExamPaper
	 * @Description: 添加教师
	 * @author xiahuajun
	 * @date 2016年9月4日 
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=addTeacher")
	public void addTeacher(@RequestParam("data") String data,java.io.PrintWriter out) {
		Map<String, Object> paramMap = getSerializer().parseMap(data);
		// 创建人
		String create_person = SecurityContext.getPrincipal().getChineseName();
		// 创建事间
		String create_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		String passwordMd5 = EncryptHelper.encryptEncode(paramMap.get("password").toString());
		String teacherPk = UUID.randomUUID().toString();
		paramMap.put("create_person", create_person);
		paramMap.put("create_time", create_time);
		paramMap.put("teacher_pk", teacherPk);
		paramMap.put("passwordMd5", passwordMd5);
		teacherManageService.addTeacher(paramMap);
		//根据登录名查询teacher_pk
		String teacher_pk = teacherManageService.selectTeacherPkByLoginName(paramMap.get("username").toString());
		//查询当前用户的学校code(根据登录名)
		String loginName=SecurityContext.getPrincipal().getUsername();
		String schoolCode = examNumberManageService.getSchoolCodeByLoginName(loginName);
		paramMap.put("teacher_pk", teacher_pk);
		paramMap.put("state", "1");
		paramMap.put("schoolCode", schoolCode);
		//添加教师学校关系表
		teacherManageService.addRefTeacherSchool(paramMap);
		
		//BUA_USER表中的User_Id
		paramMap.put("loginName", loginName);
		paramMap.put("userId", teacherPk);
		//chinesename=潘长江, username=panchangjiang, password=, create_person=沈巷中学教导员, create_time=2016-10-26 13:47:02, teacher_pk=39f963e3-d108-4da2-8e50-a4a3d1d6bb23, passwordMd5=, id=094EEE4B-3B8E-4782-B045-A4D44169C5EB, state=1, schoolCode=3071, userId=2051c41b-c98d-43b8-9677-2986a650d849
		//添加BUA_USER表中添加数据
		teacherManageService.addBuaUserInfo(paramMap);
		//通过role_code查询role_id
		String roleId = teacherManageService.selectRoleIdByRoleCode("teacher");
		paramMap.put("roleId", roleId);
		//添加Ref_Role_User表中添加数据
		teacherManageService.addRefBuaUserInfo(paramMap);
		
		paramMap.put("mess", "success");
		out.print(getSerializer().formatMap(paramMap));
	}
	
	/**
	 * @Title: addTeacher
	 * @Description: 根据姓名查询外校老师信息
	 * @author xiahuajun
	 * @date 2016年9月5日 
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=selectTeacherByName")
	public void selectTeacherByName(@RequestParam("data") String data,java.io.PrintWriter out) {
		Map<String, Object> paramMap = getSerializer().parseMap(data);
		paramMap.put("realname", StringUtils.trim(paramMap.get("realname").toString()));
		
		List<Map<String,Object>> list = teacherManageService.selectTeacherByChineseName(paramMap);
		if(list.size() == 0){
			paramMap.put("mess", "notFound");
			out.print(getSerializer().formatMap(paramMap));
			return;
		}
		out.print(getSerializer().formatList(list));
	}
	
	/**
	 * @Title: selectTeacherByName
	 * @Description: 添加教师信息
	 * @author xiahuajun
	 * @date 2016年9月5日 
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=addTeacherInfo")
	public void addTeacherInfo(@RequestParam("data") String data,java.io.PrintWriter out) {
		Map<String, Object> paramMap = getSerializer().parseMap(data);
		//查询当前用户的学校code(根据登录名)
		String loginName=SecurityContext.getPrincipal().getUsername();
		String schoolCode = examNumberManageService.getSchoolCodeByLoginName(loginName);
		paramMap.put("state", "2");
		paramMap.put("schoolCode", schoolCode);
		//添加前查询被添加的老师是否已经添加过
		List<Map<String, Object>> list = teacherManageService.selectTeacherIsExist(paramMap);
		if(null != list && list.size() > 0){
			paramMap.put("mess", "isExist");
			out.print(getSerializer().formatMap(paramMap));
			return;
		}
		//添加教师学校关系表
		teacherManageService.addRefTeacherSchool(paramMap);
		paramMap.put("mess", "success");
		out.print(getSerializer().formatMap(paramMap));
	}
	
	/**
	 * @Title: deleteExamPaper
	 * @Description: 删除老师
	 * @author xiahuajun
	 * @date 2016年9月5日 
	 * @param data
	 * @param out
	 * @return void
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(params = "command=deleteTeacher")
	public void deleteTeacher(@RequestParam("data") String data,java.io.PrintWriter out) {
		Map<String, Object> paramMap = getSerializer().parseMap(data);
		//int a = 3/0;
		//删除前先查询此老师有没有被调走[{state=0}, {state=3}]
		List<Map<String,Object>> paramList = teaManagementService.findIsAlreadyTransfer(paramMap);
		for(Map<String,Object> map : paramList){
			if("2".equals(map.get("state").toString())){
				paramMap.put("mess", "notSubmit");
				out.print(getSerializer().formatMap(paramMap));
				return;
			}
		}
		List<String> list = (List<String>) paramMap.get("selArr");
		//查询当前用户的学校code(根据登录名)
		String loginName=SecurityContext.getPrincipal().getUsername();
		String schoolCode = examNumberManageService.getSchoolCodeByLoginName(loginName);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("selArr", list);
		map.put("schoolCode", schoolCode);
		int result = teacherManageService.remove(map);
		if(result == 1){
			paramMap.put("mess", "notDelete");
			out.print(getSerializer().formatMap(paramMap));
			return;
		}
		//删除ref_role_user关系表
		teacherManageService.removeRefRoleUser(map);
		//删除bua_user表
		teacherManageService.removeBuaUser(map);
		paramMap.put("mess", "success");
		out.print(getSerializer().formatMap(paramMap));
	}
	
	/**
	 * @Title: selectCoursesByLoginName
	 * @Description: 根据登录用户判断学校类型，显示科目
	 * @author xiahuajun
	 * @date 2016年9月6日 
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=selectCoursesByLoginName")
	public void selectCoursesByLoginName(java.io.PrintWriter out) {
		//String username = SecurityContext.getPrincipal().getUsername();
		//查询当前用户的学校code(根据登录名)
		String loginName=SecurityContext.getPrincipal().getUsername();
		String schoolCode = examNumberManageService.getSchoolCodeByLoginName(loginName);
		String schoolSequence = teacherManageService.selectSchoolTypeByLoginName(schoolCode);
		String schoolType = "";
		if("0".equals(schoolSequence)){
			schoolType = "xx";
		}
		else if("1".equals(schoolSequence)){
			schoolType = "cz";
		}
		else if("4".equals(schoolSequence) || "3".equals(schoolSequence) || "2".equals(schoolSequence)){
			schoolType = "gz";
		}
		List<Map<String,Object>> list = teacherManageService.selectCoursesByScholType(schoolType);
		out.print(getSerializer().formatList(list));
	}
	
	/**
	 * @Title: submitApplyTeacher
	 * @Description: 本校管理员提交调度老师
	 * @author xiahuajun
	 * @date 2016年9月19日 
	 * @param data
	 * @param out
	 * @param val 
	 * @return void
	 */
	@RequestMapping(params = "command=submitApplyTeacher")
	public void submitApplyTeacher(@RequestParam("data") String data,java.io.PrintWriter out) {
		Map<String, Object> paramMap = getSerializer().parseMap(data);
		//获取多条记录的id
		//List<String> list = (List<String>) paramMap.get("selArr");
		//查询当前用户的学校code(根据登录名)
		String loginName=SecurityContext.getPrincipal().getUsername();
		String schoolCode = examNumberManageService.getSchoolCodeByLoginName(loginName);
		paramMap.put("schoolCode", schoolCode);
		//新去向 
		//paramMap.put("newadr", val);
		// 创建人	
		
		String create_person = SecurityContext.getPrincipal().getChineseName();
		// 创建事间
		String create_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		//防止重复申请
		List<Map<String,Object>> repeatList = teaManagementService.findIsAlreadyApply(paramMap);
		for(Map<String,Object> map : repeatList){
			if("3".equals(map.get("state").toString())){
				paramMap.put("mess", "repeatApply");
				out.print(getSerializer().formatMap(paramMap));
				return;
			}
		}
		//调动前先查询此老师有没有被调走[{state=0}, {state=3}]
		List<Map<String,Object>> list = teaManagementService.findIsAlreadyTransfer(paramMap);
		for(Map<String,Object> map : list){
			if("2".equals(map.get("state").toString())){
				paramMap.put("mess", "notSubmit");
				out.print(getSerializer().formatMap(paramMap));
				return;
			}
		}
		paramMap.put("create_person", create_person);
		paramMap.put("create_time", create_time);
		teaManagementService.submitApply(paramMap);
		//添加申请人和申请时间
		teaManagementService.addCreatePerson(paramMap);
		paramMap.put("mess", "success");
		out.print(getSerializer().formatMap(paramMap));
	}
	
	/**
	 * @Title: cancelsubmitApplyTeacher
	 * @Description: 本校管理员撤销调度老师
	 * @author xiahuajun
	 * @date 2016年9月19日 
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=cancelsubmitApplyTeacher")
	public void cancelsubmitApplyTeacher(@RequestParam("data") String data,java.io.PrintWriter out) {
		Map<String, Object> paramMap = getSerializer().parseMap(data);
		// 创建人
//		String create_person = SecurityContext.getPrincipal().getChineseName();
		// 创建事间
//		String create_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		//查询当前用户的学校code(根据登录名)
		String loginName=SecurityContext.getPrincipal().getUsername();
		String schoolCode = examNumberManageService.getSchoolCodeByLoginName(loginName);
		paramMap.put("schoolCode", schoolCode);
		//撤销前先查询有没有未提交的调度[{state=0}, {state=3}]
		List<Map<String,Object>> list = teaManagementService.findIsSubmitApply(paramMap);
		for(Map<String,Object> map : list){
			if(! "3".equals(map.get("state").toString())){
				paramMap.put("mess", "notSubmit");
				out.print(getSerializer().formatMap(paramMap));
				return;
			}
		}
		//撤销调度申请
		teaManagementService.cancelSubmitApply(paramMap);
		paramMap.put("mess", "success");
		out.print(getSerializer().formatMap(paramMap));
	}
	
	@Autowired
	private TeacherManagementService teaManagementService;
	private String tea_state;
}

