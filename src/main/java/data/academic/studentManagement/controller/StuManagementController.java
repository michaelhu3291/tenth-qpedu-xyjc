package data.academic.studentManagement.controller;

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
import data.academic.studentManagement.service.StuManagementService;
import data.academic.teacherManagement.service.TeacherManagementService;
import data.academic.transfer.service.StudentTransferService;
import data.academic.util.ExportUtil;
import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractBaseController1;
import data.framework.support.ConfigContext;
import data.platform.authority.security.SecurityContext;
/**
 * @Title: StuManagementController
 * @Description:  学生 管理控制层
 * @author zhaohuanhuan
 * @date 2016年7月29日 
 */
@RestController
@RequestMapping("stuManagement/stuInfo")
public class StuManagementController extends AbstractBaseController1{
	@Autowired
	private StuManagementService stuManagementService;
	@Autowired
	private ExamNumberManageService examNumberManageService;
	@Autowired
	private TeacherManagementService teacherManagementService;
	@Autowired
	private StudentTransferService studentTransferService;
	@RequestMapping
	protected ModelAndView initialize(ModelMap model, HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException{
		String loginName = SecurityContext.getPrincipal().getUsername();
		//根据登录名查询角色Code
		String roleCode = teacherManagementService.selectRoleCodeByLoginName(loginName);
		if(roleCode.equals(ConfigContext.getValue("framework.tmis.roleCode['qpRoleAdminCode']")) || roleCode.equals(ConfigContext.getValue("framework.tmis.roleCode['qpRoleQuAdminCode']"))) {
			request.getRequestDispatcher("/transfer/studentTransfer.do").forward(request,response);
		} 
		return new ModelAndView();
		
	}
	
	/**
	 * 分页查询学生，学生所选的科目
	 */
	@RequestMapping(params = "command=searchPaging")
	public void searchPaging(@RequestParam("data") String data,java.io.PrintWriter out){
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		//schoolType=1, school=3063
		boolean isFast = parseBoolean( requestMap.get( "isFast" ) ) ;
	    String xjfh = "" ;
	    String studentName="";
	    String studentState="";
	    String studentCourse="";
	    String studentGrade="";
	    String loginName1 = SecurityContext.getPrincipal().getUsername();
	    if( isFast ){
	    	xjfh = trimString( requestMap.get( "q" ) ) ;
	    	studentName = trimString( requestMap.get( "q" ) ) ;
	    }else{
	    	studentState=formatString(requestMap.get("studentState"));
	    	studentCourse=formatString(requestMap.get("studentCourse"));
	    	studentGrade=formatString(requestMap.get("studentGrade"));
	    }
	    String schoolCode = examNumberManageService.getSchoolCodeByLoginName(loginName1);
	    List<String> schoolCodeList = new ArrayList<>();
	    int minClassNo=0;
	    int maxClassNo=0;
	    String ycschoolName="";
	    if(ConfigContext.getStringSection("syzxSchoolCode").equals(schoolCode)){
	    	minClassNo = 1;
			maxClassNo = 12;
	    }else if(ConfigContext.getStringSection("yczxSchoolCode").equals(schoolCode)){
 	    	Map<String, Object> map=new HashMap<>();
	    	map.put("Direction", schoolCode);
			ycschoolName = studentTransferService.getSchoolName(map); 
	    	schoolCode=ConfigContext.getStringSection("syzxSchoolCode");
	    	minClassNo = 21;
			maxClassNo = 27;
	    }
	    requestMap.put("minClassNo", minClassNo);
	    requestMap.put("maxClassNo", maxClassNo);
	    schoolCodeList.add(schoolCode);
	    requestMap.put("xjfh", xjfh);   
	    requestMap.put("studentName", studentName);  
	    requestMap.put("schoolCodeList", schoolCodeList);
	    requestMap.put("studentState", studentState);
	    requestMap.put("studentCourse", studentCourse);
	    requestMap.put("studentGrade", studentGrade);
		int currentPage = parseInteger(requestMap.get("page"));
		int pageSize = parseInteger(requestMap.get("rows"));
		String sortField = trimString(requestMap.get("sidx"));
		String sort = trimString(requestMap.get("sord"));
		//得到所有的教师集合
		PagingResult<Map<String, Object>> pagingResult = stuManagementService.searchPaging(requestMap, sortField, sort, currentPage, pageSize);
		List<Map<String,Object>> newList = new ArrayList<Map<String,Object>>() ;
		String schoolName= "";
		List<Map<String,String>> schoolList=new ArrayList<>();
		List<Map<String, Object>> classList=new ArrayList<>();
		List<Map<String, String>> courseList=new ArrayList<>();
		List<Map<String, Object>>  list=pagingResult.getRows();
		String stu_state,stuName;
		Map<String,Object>  xjfhMap=new HashMap<>();
		for (Map<String, Object> studentIdmap : list) {
			String sfzjh=(String) studentIdmap.get("SFZJH");
			int schoolNum=1;
			StringBuffer schools=new StringBuffer();
			Map<String,Object> map = new HashMap<String,Object>() ;
	        String studentIds=(String) studentIdmap.get("Stu_Pk");
	        stu_state = studentIdmap.get("State").toString();
			stuName = studentIdmap.get("Stu_Name").toString();	       
	        if("4".equals(stu_state)){
	        	classList=stuManagementService.getClassByStuSfzjh(sfzjh);
	        	for (Map<String, Object> classMap : classList) {
	        		map.put("Grade_No", classMap.get("Grade_No"));
		 			String classId="";
		 			if(classMap.containsKey("Class_No")){
		 				 classId=classMap.get("Class_No").toString();
		 				 String classIdOneStr=classId.substring(0, 1);
		 					if(classIdOneStr.equals("0")){
		 						classId=classId.substring(classId.length()-1);
		 					}
		 					map.put("Class_No", classId);
		 			}else {
		 					map.put("classId", "");
		 			}
		 			 map.put("Class_Name", classMap.get("Class_Name"));
				}
	        }else{
	        	map.put("Grade_No", studentIdmap.get("Grade_No"));
	 			String classId="";
	 			if(studentIdmap.containsKey("Class_No")){
	 				 classId=studentIdmap.get("Class_No").toString();
	 				 String classIdOneStr=classId.substring(0, 1);
	 					if(classIdOneStr.equals("0")){
	 						classId=classId.substring(classId.length()-1);
	 					}
	 					map.put("Class_No", classId);
	 			}else {
	 					map.put("classId", "");
	 			}
	 			 map.put("Class_Name", studentIdmap.get("Class_Name"));
	        }
			
			String teaLoginName=(String) studentIdmap.get("Login_Name");
			String teaType=(String) studentIdmap.get("Teacher_Position");
			map.put("stuPk", studentIds);
			map.put("state", studentIdmap.get("State").toString());
			map.put("stuName", stuName);
			map.put("stuCode", formatString(studentIdmap.get("XJFH")));
			map.put("schoolName", formatString(studentIdmap.get("School_Short_Name")));
			if(!"".equals(ycschoolName)){
				map.put("schoolName", ycschoolName);
			}
			map.put("loginName", teaLoginName);
			map.put("teaType", teaType);
			map.put("state", stu_state);
			map.put("gradeId", formatString(studentIdmap.get("Grade_Id")));
			//根据当前学生的id得到所拥有的学校集合
			schoolList=stuManagementService.getSchoolByStuId(studentIds);
			//根据当前学生的id得到所拥有的班级集合
		//	classList=stuManagementService.getClassByStuId(studentIds);
			//根据当前学生的id得到所拥有的科目集合
			xjfhMap.put("xfzjh",sfzjh);
			courseList=stuManagementService.getCourseByStuId(xjfhMap);
			if(schoolList.size()>0 ){
				for (Map<String, String> schoolMap : schoolList) {
					if(schoolMap!=null){
						if(schoolNum==1 && !"".equals(ycschoolName)){
							schoolName=ycschoolName;
						}else{
							schoolName=schoolMap.get("School_Short_Name");
						}
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
			map.put("schoolName", schools.toString());
			map.put("courseList", courseList);
			map.put("courseList", courseList);
			map.put("SFZJH", sfzjh);
			map.put("Is_Xjb", studentIdmap.get("Is_Xjb"));
			newList.add( map ) ;
		}
		PagingResult<Map<String,Object>> newPagingResult = 
				new PagingResult<Map<String,Object>>( newList, pagingResult.getPage(), pagingResult.getPageSize(), pagingResult.getRecords() ) ;
		out.print(getSerializer().formatObject(newPagingResult));
		
}
	
	/**
	 * 
	 * @Title: searchPagingImport
	 * @Description: 导出
	 * @author jay zhong
	 * @date 2017年11月23日 下午7:24:44 
	 * @return void
	 *
	 * @param data
	 * @param response
	 */
	@RequestMapping(params = "command=importExcel")
	public void searchPagingImport(@RequestParam("data") String data,HttpServletResponse response){
		Map<String, Object> requestMap = getSerializer().parseMap(data);

	    String xjfh = "" ;
	    String studentName="";
	    String studentState="";
	    String studentCourse="";
	    String studentGrade="";
	    String loginName1 = SecurityContext.getPrincipal().getUsername();
	    xjfh = trimString( requestMap.get( "q" ) ) ;
	    studentName = trimString( requestMap.get( "q" ) ) ;
	    studentState=formatString(requestMap.get("studentState"));
	    studentCourse=formatString(requestMap.get("studentCourse"));
	    studentGrade=formatString(requestMap.get("studentGrade"));
	    
	    String schoolCode = examNumberManageService.getSchoolCodeByLoginName(loginName1);
	    List<String> schoolCodeList = new ArrayList<>();
	    int minClassNo=0;
	    int maxClassNo=0;
	    String ycschoolName="";
	    if(ConfigContext.getStringSection("syzxSchoolCode").equals(schoolCode)){
	    	minClassNo = 1;
			maxClassNo = 12;
	    }else if(ConfigContext.getStringSection("yczxSchoolCode").equals(schoolCode)){
 	    	Map<String, Object> map=new HashMap<>();
	    	map.put("Direction", schoolCode);
			ycschoolName = studentTransferService.getSchoolName(map); 
	    	schoolCode=ConfigContext.getStringSection("syzxSchoolCode");
	    	minClassNo = 21;
			maxClassNo = 27;
	    }
	    requestMap.put("minClassNo", minClassNo);
	    requestMap.put("maxClassNo", maxClassNo);
	    schoolCodeList.add(schoolCode);
	    requestMap.put("xjfh", xjfh);   
	    requestMap.put("studentName", studentName);  
	    requestMap.put("schoolCodeList", schoolCodeList);
	    requestMap.put("studentState", studentState);
	    requestMap.put("studentCourse", studentCourse);
	    requestMap.put("studentGrade", studentGrade);
		
		//得到所有的教师集合
		List<Map<String, Object>> list = stuManagementService.searchPagingImport(requestMap);
		List<Map<String,Object>> newList = new ArrayList<Map<String,Object>>() ;
		String schoolName= "";
		List<Map<String,String>> schoolList=new ArrayList<>();
		List<Map<String, Object>> classList=new ArrayList<>();
		List<Map<String, String>> courseList=new ArrayList<>();
		
		String stu_state,stuName;
		Map<String,Object>  xjfhMap=new HashMap<>();
		for (Map<String, Object> studentIdmap : list) {
			String sfzjh=(String) studentIdmap.get("SFZJH");
			int schoolNum=1;
			StringBuffer schools=new StringBuffer();
			Map<String,Object> map = new HashMap<String,Object>() ;
	        String studentIds=(String) studentIdmap.get("Stu_Pk");
	        stu_state = studentIdmap.get("State").toString();
			stuName = studentIdmap.get("Stu_Name").toString();	       
	        if("4".equals(stu_state)){
	        	classList=stuManagementService.getClassByStuSfzjh(sfzjh);
	        	for (Map<String, Object> classMap : classList) {
	        		map.put("Grade_No", classMap.get("Grade_No"));
		 			String classId="";
		 			if(classMap.containsKey("Class_No")){
		 				 classId=classMap.get("Class_No").toString();
		 				 String classIdOneStr=classId.substring(0, 1);
		 					if(classIdOneStr.equals("0")){
		 						classId=classId.substring(classId.length()-1);
		 					}
		 					map.put("Class_No", classId);
		 			}else {
		 					map.put("classId", "");
		 			}
		 			 map.put("Class_Name", classMap.get("Class_Name"));
				}
	        }else{
	        	map.put("Grade_No", studentIdmap.get("Grade_No"));
	 			String classId="";
	 			if(studentIdmap.containsKey("Class_No")){
	 				 classId=studentIdmap.get("Class_No").toString();
	 				 String classIdOneStr=classId.substring(0, 1);
	 					if(classIdOneStr.equals("0")){
	 						classId=classId.substring(classId.length()-1);
	 					}
	 					map.put("Class_No", classId);
	 			}else {
	 					map.put("classId", "");
	 			}
	 			 map.put("Class_Name", studentIdmap.get("Class_Name"));
	        }
			
			String teaLoginName=(String) studentIdmap.get("Login_Name");
			String teaType=(String) studentIdmap.get("Teacher_Position");
			map.put("stuPk", studentIds);
			map.put("state", studentIdmap.get("State").toString());
			map.put("stuName", stuName);
			map.put("stuCode", formatString(studentIdmap.get("XJFH")));
			map.put("schoolName", formatString(studentIdmap.get("School_Short_Name")));
			if(!"".equals(ycschoolName)){
				map.put("schoolName", ycschoolName);
			}
			map.put("loginName", teaLoginName);
			map.put("teaType", teaType);
			map.put("state", stu_state);
			map.put("gradeId", formatString(studentIdmap.get("Grade_Id")));
			//根据当前学生的id得到所拥有的学校集合
			schoolList=stuManagementService.getSchoolByStuId(studentIds);
			//根据当前学生的id得到所拥有的班级集合
		//	classList=stuManagementService.getClassByStuId(studentIds);
			//根据当前学生的id得到所拥有的科目集合
			xjfhMap.put("xfzjh",sfzjh);
			courseList=stuManagementService.getCourseByStuId(xjfhMap);
			if(schoolList.size()>0 ){
				for (Map<String, String> schoolMap : schoolList) {
					if(schoolMap!=null){
						if(schoolNum==1 && !"".equals(ycschoolName)){
							schoolName=ycschoolName;
						}else{
							schoolName=schoolMap.get("School_Short_Name");
						}
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
			map.put("schoolName", schools.toString());
			for(Map map1:courseList){
				formatString(map1.get("Course"));
				
			}
			map.put("courseList", courseList);
			map.put("SFZJH", sfzjh);
			map.put("Is_Xjb", studentIdmap.get("Is_Xjb"));
			newList.add( map ) ;
		}
		String fileName="学生列表.xls";
		String sheetName="学生列表详情";
		String[] title={"序号","姓名","学籍号","学校","班级","科目"};
		String[] key={"xh","stuName","stuCode","schoolName","Class_Name","courseList"};
		for(Map<String,Object> map:newList){
			map.put("stuName",formatString(map.get("stuName"))+"("+formatString(ExportUtil.XJ_STATE_MAP.get(formatString(map.get("state"))))+")");
			String Is_Xjb=formatString(map.get("Is_Xjb"));
			String str="";
			if("1".equals(Is_Xjb)){
				str="(新疆班)";
			}
			map.put("Grade_No",formatString(ExportUtil.CLASS_TYPE_MAP.get(formatString(map.get("Grade_No"))))+"("+formatString(map.get("Class_No"))+")班"+str);
			List<Map<String,Object>> list2=(List<Map<String, Object>>) map.get("courseList");
			String km="";
			for(int i=0;i<list2.size();i++){
				String km2=formatString(list2.get(i).get("Course"));
				if(!"".equals(km2)){
					
						km+=formatString(ExportUtil.CLASS_TYPE_MAP.get(km2))+" ";
					
				}
				
			}
			map.put("courseList", km);
			
		}
		ExportUtil.ExportExcel(response, title, fileName, sheetName, newList, key);
		
		
		
}
			 
    
    /**
	 * 得到已选择科目和待选择科目
	 */
	@RequestMapping( params = "command=getCourseById" )
    public void getCourseById(HttpServletRequest request,java.io.PrintWriter out)
    {   
		//得到选中的学生拥有科目id
		List<String> stuCourse=new ArrayList<>(); 
		//过滤掉学生当前拥有的科目
        List<Map<String, String>> courseList=stuManagementService.getCourseList(stuCourse);   
        Map<String,Object> resultMap = new HashMap<String,Object>() ;
		resultMap.put("courseList", courseList);//可选择的科目集合
		out.print( getSerializer().formatMap(resultMap)) ;
		
    }
	/**
	 * 给当前学生添加科目
	 */
    @RequestMapping( params = "command=saveCourse" )
    public void saveCourse( @RequestParam( "data" ) String data, java.io.PrintWriter out )
    {
        Map<String,Object> paramMap = getSerializer().parseMap( data ) ;
    	//得到学生id
        //String xjfh=parseString(paramMap.get("xjfh"));
        List<String> sfzjhArr=(List<String>) paramMap.get("sfzjhArr");
    	//得到科目id集合，用于添加学校
    	List<String> courseIds=(List<String>) paramMap.get("courseIds");
    	//删除当前学生开始所在的学校
        stuManagementService.removeCourseByXjfh(sfzjhArr);
    	//重新给学生添加学校
    	if(courseIds!=null){
    		for (String course : courseIds) {
    			for(String sfzjh: sfzjhArr){
    				stuManagementService.stuReCourse(sfzjh,course);
    			}
    			
    		}  
    	}
    	Map<String, String> map = new HashMap<String, String>();
        map.put("message", "保存成功");
        out.print( getSerializer().formatObject( map ) ) ;
    }
    
     /**
      * @Title: addTeacher
      * @Description: 添加随班就读学生
      * @author xiahuajun
      * @date 2016年9月28日 
      * @param data
      * @param out
      * @return void
      */
    @RequestMapping(params = "command=addStudent")
	public void addTeacher(@RequestParam("data") String data,java.io.PrintWriter out) {
		Map<String, Object> paramMap = getSerializer().parseMap(data);
		// 创建人
		String create_person = SecurityContext.getPrincipal().getChineseName();
		// 创建事间
		String create_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		//根据登录名查询schoolCode
		String loginName=SecurityContext.getPrincipal().getUsername();
		String schoolCode = examNumberManageService.getSchoolCodeByLoginName(loginName);
		//根据schoolCode查询学校名
		String schoolName = stuManagementService.selectSchoolNameBySchoolCode(schoolCode);
		paramMap.put("create_person", create_person);
		paramMap.put("schoolCode", schoolCode);
		paramMap.put("create_time", create_time);
		paramMap.put("schoolName", schoolName);
		paramMap.put("uuid", UUID.randomUUID().toString());
		paramMap.put("state", "随班就读");
		paramMap.put("stateCode", "1");
		//添加随班就读学生(学生表)
		stuManagementService.addStudent(paramMap);
		//添加随班就读学生(学生学校关系表)
		stuManagementService.addRefStudentSchool(paramMap);
		//添加随班就读学生（学生班级关系表）
		stuManagementService.addRefStudentClass(paramMap);
		paramMap.put("mess", "success");
		out.print(getSerializer().formatMap(paramMap));
	}



	@RequestMapping( params = "command=getSequenceBySchoolCode" )
	 public void getSequenceBySchoolCode( @RequestParam( "data" ) String data, java.io.PrintWriter out)
	    {
	    	
	        String username = SecurityContext.getPrincipal().getUsername();
	        //根据登录用户显示其所属的学校类型和学校名称
	        String schoolCode=examNumberManageService.getSchoolCodeByLoginName(username);
	       String sequence=stuManagementService.getSequenceBySchoolCode(schoolCode);
	        List<Map<String, Object>> paramList = new ArrayList<Map<String,Object>>();
	        Map<String, Object> map=new HashMap<>();	
	        map.put("sequence", sequence);
	        map.put("schoolCode", schoolCode);
	        paramList.add(map);
	        out.print( getSerializer().formatList( paramList ));
	    }
	

	/** 
	* @Title: updateStudentState 
	* @Description: 修改学生学籍状态
	* @param @param data
	* @param @param out 
	* @author zhaohuanhuan
	* @return void   
	* @throws 
	*/
	@RequestMapping( params = "command=updateStudentState" )
    public void updateStudentState( @RequestParam( "data" ) String data, java.io.PrintWriter out )
    {
        Map<String,Object> paramMap = getSerializer().parseMap( data ) ;
        String username = SecurityContext.getPrincipal().getUsername();
        //根据登录用户显示其所属的学校类型和学校名称
        String schoolCode=examNumberManageService.getSchoolCodeByLoginName(username);
        if(ConfigContext.getStringSection("yczxSchoolCode").equals(schoolCode)){
	    	schoolCode=ConfigContext.getStringSection("syzxSchoolCode");
        }
    	//得到学生id集合
        List< Map<String,Object>> studnetPkList=(List< Map<String,Object>>) paramMap.get("studentIds");
    	 for (Map<String, Object> map : studnetPkList) {
    		 paramMap.put("studentPk", map.get("studentPk"));
			if("4".equals(map.get("state"))){
				paramMap.put("state", "1");
				paramMap.put("schoolCode", schoolCode);
				stuManagementService.updateStuStateForStudentRefSchool(paramMap);
			}else{
				paramMap.put("state", "1");
				paramMap.put("stateName", "随班就读");
				paramMap.put("stateCode", "sbjd");
				paramMap.put("schoolCode", schoolCode);
				stuManagementService.updateStuStateForStudent(paramMap);
				stuManagementService.updateStuStateForStudentRefSchool(paramMap);
			}
		}
    	Map<String, String> map = new HashMap<String, String>();
        map.put("message", "true");
        out.print( getSerializer().formatObject( map ) ) ;
    }
	
	
	
	@RequestMapping( params = "command=cencelSbjd" )
    public void cencelSbjd( @RequestParam( "data" ) String data, java.io.PrintWriter out )
    {
        Map<String,Object> paramMap = getSerializer().parseMap( data ) ;
        String username = SecurityContext.getPrincipal().getUsername();
        //根据登录用户显示其所属的学校类型和学校名称
        String schoolCode=examNumberManageService.getSchoolCodeByLoginName(username);
        if(ConfigContext.getStringSection("yczxSchoolCode").equals(schoolCode)){
	    	schoolCode=ConfigContext.getStringSection("syzxSchoolCode");
        }
    	//得到学生id集合
        List< Map<String,Object>> studnetPkList=(List< Map<String,Object>>) paramMap.get("studentIds");
    	 for (Map<String, Object> map : studnetPkList) {
    		 paramMap.put("studentPk", map.get("studentPk"));
    		 String oldStudentSchool=studentTransferService.selectTeacherSchoolCode(map.get("studentPk").toString());
			 if(schoolCode.equals(oldStudentSchool)){
				 paramMap.put("state", "0"); 
				 paramMap.put("stateName", "在读");
				 paramMap.put("stateCode", "zd");
				 paramMap.put("schoolCode", schoolCode);
				 stuManagementService.updateStuStateForStudent(paramMap);
			 }else{
				 paramMap.put("schoolCode", schoolCode);
				 paramMap.put("state", "4"); 
			 }
			 stuManagementService.updateStuStateForStudentRefSchool(paramMap);
		}
    	Map<String, String> map = new HashMap<String, String>();
        map.put("message", "true");
        out.print( getSerializer().formatObject( map ) ) ;
    }
}
