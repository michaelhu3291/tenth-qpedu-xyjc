package data.academic.scoreManage.controller;

/**
 * @Title
 * @Description 成绩管理--位次查询
 * @author wangchaofa
 * @CreateDate Nov 30,2016 
 */

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import data.academic.examNumberManagement.service.ExamNumberManageService;
import data.academic.statisticsAnalysis.service.SchoolPlainAdminScoreSearchService;
import data.academic.statisticsAnalysis.service.ScoreSearchService;
import data.academic.teacherManagement.service.TeacherManagementService;
import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractBaseController1;
import data.framework.support.ConfigContext;
import data.platform.authority.security.SecurityContext;

@RestController
@RequestMapping("scoreManage/orderSearch")
public class OrderSearchController extends AbstractBaseController1{

	
	@Autowired
	private ScoreSearchService scoreSearchService;
	@Autowired
	private TeacherManagementService teacherManagementService;
	@Autowired
	private ExamNumberManageService examNumberManageService;
	@Autowired
	private SchoolPlainAdminScoreSearchService schoolPlainAdminScoreSearchService;
	
	
	@RequestMapping
	protected ModelAndView init(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		// 得到所有学校的集合
		List<Map<String, Object>> schoolList = scoreSearchService.getSchoolCode();
		model.put("schoolList", schoolList);
		// 得到科目名称
		List<Map<String, Object>> courseTypeList = scoreSearchService.getCourseName();
		model.put("courseTypeList", courseTypeList);
				
		String loginName = SecurityContext.getPrincipal().getUsername();
		//根据登录名查询角色Code
		String roleCode = teacherManagementService.selectRoleCodeByLoginName(loginName);
		if("schoolPlainAdmin".equals(roleCode) || "schoolAdmin".equals(roleCode)) {
			request.getRequestDispatcher("/scoreManage/orderSearchForSchoolAdmin.do").forward(request,response);
		}
		else if(roleCode.equals(ConfigContext.getValue("framework.tmis.roleCode['qpRoleAdminCode']")) || roleCode.equals(ConfigContext.getValue("framework.tmis.roleCode['qpRoleQuAdminCode']"))) {
			request.getRequestDispatcher("/scoreManage/orderSearchForAdmin.do").forward(request,response);
					
		}
		else if("instructor".equals(roleCode)) {
			request.getRequestDispatcher("/scoreManage/orderSearchForInstructor.do").forward(request,response);
					
		}
		return new ModelAndView();
		
	}


	
	/**
	 * @Title: overallSearchForAdmin
	 * @Description: 青浦超级管理员，分页位次查询
	 * @author wangchaofa
	 * @date 2016年11月30日 
	 * @param data
	 * @param out
	 * @return void
	 */
	@SuppressWarnings("unused")
	@RequestMapping(params = "command=orderSearchForAdmin")
	public void orderSearchForAdmin(@RequestParam("data") String data, java.io.PrintWriter out) {
        long startTime = System.currentTimeMillis();
        Map<String, Object> requestMap = getSerializer().parseMap(data);
		String grade = requestMap.get("grade").toString();
		String loginName=SecurityContext.getPrincipal().getUsername();
		String schoolCode = null;//学校编号
		String roleType = null; //登录角色类型
		String examCode1 = null;//考试编号
		//根据登录名查询角色Code
		String roleCode = teacherManagementService.selectRoleCodeByLoginName(loginName);
		if("schoolPlainAdmin".equals(roleCode) || "schoolAdmin".equals(roleCode)) {//校级
			schoolCode = examNumberManageService.getSchoolCodeByLoginName(loginName);
			roleType = "school";
			examCode1 = trimString(requestMap.get("examNumber").toString());
		}else{//区级
			roleType = "district";
		}
		String schoolType = null; //学校类型
		
		if("11".equals(grade) || "12".equals(grade) || "13".equals(grade) || "14".equals(grade) || "15".equals(grade)){
			schoolType = "xx";
		}
		else if("16".equals(grade) || "17".equals(grade) || "18".equals(grade) || "19".equals(grade)){
			schoolType = "cz";
		}
		else if("31".equals(grade) || "32".equals(grade) || "33".equals(grade)){
			schoolType = "gz";
		}

//        Map<String,String> schoolMap = (Map<String, String>) requestMap.get("school");
        //schoolYear=2016-2017, term=xxq, examType=qm, grade=16, school=3071,gradeHidden
		//String courseStr = requestMap.get("gradeHidden").toString();
		//String[] courseArr = courseStr.split(",");
		//List<String> courseList = new ArrayList<>();
		/*for(String str : courseArr){
			courseList.add(str);
		}*/
		//科目拼接串,ISNULL(yw,0)+ISNULL(sx,0)+ISNULL(yy,0)+ISNULL(wl,0)+ISNULL(hx,0)+ISNULL(sxzz,0)+ISNULL(ls,0)+ISNULL(kx,0)+ISNULL(xxkj,0)+ISNULL(ms,0)+ISNULL(ty,0)+ISNULL(yyue,0)+ISNULL(yjxkc,0)+ISNULL(tzxkc,0) AS Total_Score
		//StringBuffer sb = new StringBuffer();
		//sb.append(",");
		//int i;
		/*for(i = 0;i < courseList.size();i++){
			//ISNULL(sx, 0) + ISNULL(yy, 0) + ISNULL(yw, 0) AS Total_Score
			if(i == courseList.size() - 1){
				sb.append("ISNULL("+courseList.get(i)+",0)");
			} else {
				sb.append("ISNULL("+courseList.get(i)+",0)" + "+");
			}
		}*/
		//sb.append(" AS Total_Score");
		int currentPage = parseInteger(requestMap.get("page"));
		int pageSize = parseInteger(requestMap.get("rows"));
		String sortField = trimString(requestMap.get("sidx"));
		if (sortField.trim().length() == 0) {
			sortField = "School_Code,Grade_Id,Class_Id";
		}
		String stuCode = trimString(requestMap.get("examNumberOrStuCode").toString());
		String examNumber = trimString(requestMap.get("examNumberOrStuCode").toString());
        String examType = parseString(requestMap.get("examType"));
        requestMap.put("stuCode", stuCode);
		requestMap.put("examType", examType);
		requestMap.put("examNumber", examNumber);
		requestMap.put("examCode", examCode1);
		//学校类型
		requestMap.put("schoolType", schoolType);
		requestMap.put("schoolCode", schoolCode);
		//科目拼接串
		//requestMap.put("courseStr", sb.toString());
		//requestMap.put(""+ ""+ "", courseArr);
						
		
		String sort = trimString(requestMap.get("sord"));
		PagingResult<Map<String, Object>> pagingResult = scoreSearchService.searchScoreForqpAdmin2(requestMap, sortField, sort,
                currentPage, pageSize);
        System.out.println("执行scoreSearch.searchScoreForqpAdmin,已耗时:"+((System.currentTimeMillis()-startTime)/1000)+"秒!");
        List<Map<String, Object>> list = pagingResult.getRows();
		List<Map<String,Object>> newList = new ArrayList<>();
		String totalDistrict = scoreSearchService.getTotalDistrict(requestMap);  //区级总人数
        System.out.println("执行scoreSearch.getTotalDistrict,已耗时:"+((System.currentTimeMillis()-startTime)/1000)+"秒!");
		String order = null;
		String order1 = null;
		String order2 = null;
		String orderDistrict = null;//区级排名
		String orderGrade = null;//年级排名
		String orderClass = null;//班级排名
		String examCode = null;//考号
		String orderCourse = null;  //科目排名

        //排名处理
        //取区级名次及各科区级名次(key=学籍号,val=(key=科目,val=名次))
        Map<String,Map<String,Object>> qjRanks=new HashMap<>();
        List<Map<String, Object>> maps = scoreSearchService.searchRankQj(requestMap);
        for (Map<String, Object> map : maps) {
            qjRanks.put(parseString(map.get("XJFH")),map);
        }
        //取年级名次(key=学籍号,val=名次)
        Map<String,Map<String,Object>> njRanks=new HashMap<>();
        List<Map<String, Object>> maps2 = scoreSearchService.searchRankNj(requestMap);
        for (Map<String, Object> map : maps2) {
            njRanks.put(parseString(map.get("XJFH")),map);
        }


        for (Map<String, Object> m : list) {
            String xjfh =parseString( m.get("XJFH"));
            Map<String, Object> qjRank = qjRanks.get(xjfh);
            Object school_name = m.get("School_Name");
            String orderClass1 =parseString( qjRank.get("rank_total"));
            String totalGrade = parseString(m.get("totalSchool"));//年级总人数
            orderGrade = njRanks.get(xjfh).get("rank_nj") + " / " + totalGrade;
            m.put("School_Short_Name",school_name);
            orderDistrict = orderClass1 + " / " + totalDistrict;  //区级排名  例： 1/200
            m.put("orderDistrict", orderDistrict);
            m.put("orderGrade", orderGrade);


            if (m.containsKey("yw")) { String yw = parseString(qjRank.get("rank_yw")); String yw1 = !"".equals(yw)?(yw + " / " + totalDistrict):""; m.put("yw", yw1); }
            if (m.containsKey("sx")) { String sx = parseString(qjRank.get("rank_sx")); String sx1 = !"".equals(sx)?(sx + " / " + totalDistrict):""; m.put("sx", sx1); }
            if (m.containsKey("yy")) { String sx = parseString(qjRank.get("rank_yy")); String sx1 = !"".equals(sx)?(sx + " / " + totalDistrict):""; m.put("yy", sx1); }
            if (m.containsKey("zr")) { String sx = parseString(qjRank.get("rank_zr")); String sx1 = !"".equals(sx)?(sx + " / " + totalDistrict):""; m.put("zr", sx1); }
            if (m.containsKey("wl")) { String sx = parseString(qjRank.get("rank_wl")); String sx1 = !"".equals(sx)?(sx + " / " + totalDistrict):""; m.put("wl", sx1); }
            if (m.containsKey("hx")) { String sx = parseString(qjRank.get("rank_hx")); String sx1 = !"".equals(sx)?(sx + " / " + totalDistrict):""; m.put("hx", sx1); }
            if (m.containsKey("sxzz")) { String sx = parseString(qjRank.get("rank_sxzz")); String sx1 = !"".equals(sx)?(sx + " / " + totalDistrict):""; m.put("sxzz", sx1); }
            if (m.containsKey("ls")) { String sx = parseString(qjRank.get("rank_ls")); String sx1 = !"".equals(sx)?(sx + " / " + totalDistrict):""; m.put("ls", sx1); }
            if (m.containsKey("kx")) { String sx = parseString(qjRank.get("rank_kx")); String sx1 = !"".equals(sx)?(sx + " / " + totalDistrict):""; m.put("kx", sx1); }
            if (m.containsKey("dl")) { String sx = parseString(qjRank.get("rank_dl")); String sx1 = !"".equals(sx)?(sx + " / " + totalDistrict):""; m.put("dl", sx1); }
            if (m.containsKey("xxkj")) { String sx = parseString(qjRank.get("rank_xxkj")); String sx1 = !"".equals(sx)?(sx + " / " + totalDistrict):""; m.put("xxkj", sx1); }
            if (m.containsKey("ms")) { String sx = parseString(qjRank.get("rank_ms")); String sx1 = !"".equals(sx)?(sx + " / " + totalDistrict):""; m.put("ms", sx1); }
            if (m.containsKey("sw")) { String sx = parseString(qjRank.get("rank_sw")); String sx1 = !"".equals(sx)?(sx + " / " + totalDistrict):""; m.put("sw", sx1); }
            if (m.containsKey("ty")) { String sx = parseString(qjRank.get("rank_ty")); String sx1 = !"".equals(sx)?(sx + " / " + totalDistrict):""; m.put("ty", sx1); }
            if (m.containsKey("yyue")) { String sx = parseString(qjRank.get("rank_yyue")); String sx1 = !"".equals(sx)?(sx + " / " + totalDistrict):""; m.put("yyue", sx1); }
            if (m.containsKey("njyy")) { String sx = parseString(qjRank.get("rank_njyy")); String sx1 = !"".equals(sx)?(sx + " / " + totalDistrict):""; m.put("njyy", sx1); }
            if (m.containsKey("yjxkc")) { String sx = parseString(qjRank.get("rank_yjxkc")); String sx1 = !"".equals(sx)?(sx + " / " + totalDistrict):""; m.put("yjxkc", sx1); }
            if (m.containsKey("tzxkc")) { String sx = parseString(qjRank.get("rank_tzxkc")); String sx1 = !"".equals(sx)?(sx + " / " + totalDistrict):""; m.put("tzxkc", sx1); }
            if (m.containsKey("xsjyy")) { String sx = parseString(qjRank.get("rank_xsjyy")); String sx1 = !"".equals(sx)?(sx + " / " + totalDistrict):""; m.put("xsjyy", sx1); }


        }


		if(false && "district".equals(roleType)){  //区级 （区级名次、年级名次）
            for(Map<String,Object> map : list){
				 examCode = trimString(map.get("Exam_Number"));				 
				 requestMap.put("examNumber", examCode);
				 
				 order = scoreSearchService.getOrderDistrict(requestMap);//区级名次
				 orderDistrict = order + " / " + totalDistrict;  //区级排名  例： 1/200
				 
				 requestMap.put("schoolCode", trimString(map.get("School_Code")));
				 String totalGrade = scoreSearchService.getTotalSchool(requestMap);//年级总人数
				 order1 = scoreSearchService.getOrderGrade(requestMap);//年级名次
				 orderGrade = order1 + " / " + totalGrade;

				 Set<Map.Entry<String,Object>> sets = map.entrySet();
				 for(Map.Entry<String,Object> entry:sets){

					 Map<String,Object> newMap = new HashMap<String,Object>();
					 String course1 = trimString(entry.getKey());
					 switch(course1){
					 
					 case "yw": //语文
						 requestMap.put("course", "yw");
						 String totalYw = scoreSearchService.getTotalCourse(requestMap);
						 examCode = trimString(map.get("Exam_Number"));
						 requestMap.put("examNumber", examCode);
						 order2 = scoreSearchService.getOrderCourseYw(requestMap);
						 orderCourse = order2 + " / " + totalYw;
						 map.put("yw", orderCourse);
						 break;
					 case "sx": //数学
						 requestMap.put("course", "sx");
						 String totalSx = scoreSearchService.getTotalCourse(requestMap);
						 examCode = trimString(map.get("Exam_Number"));
						 requestMap.put("examNumber", examCode);
						 order2 = scoreSearchService.getOrderCourseSx(requestMap);
						 orderCourse = order2 + " / " + totalSx;
						 map.put("sx", orderCourse);
						 break;
					 case "yy": //外语
						 requestMap.put("course", "yy");
						 String totalYy = scoreSearchService.getTotalCourse(requestMap);
						 examCode = trimString(map.get("Exam_Number"));
						 requestMap.put("examNumber", examCode);
						 order2 = scoreSearchService.getOrderCourseYy(requestMap);
						 orderCourse = order2 + " / " + totalYy;
						 map.put("yy", orderCourse);
						 break;
					 case "wl": //物理
						 requestMap.put("course", "wl");
						 String totalWl = scoreSearchService.getTotalCourse(requestMap);
						 examCode = trimString(map.get("Exam_Number"));
						 requestMap.put("examNumber", examCode);
						 order2 = scoreSearchService.getOrderCourseWl(requestMap);
						 orderCourse = order2 + " / " + totalWl;
						 map.put("wl", orderCourse);
						 break;
					 case "hx": //化学
						 requestMap.put("course", "hx");
						 String totalHx = scoreSearchService.getTotalCourse(requestMap);
						 examCode = trimString(map.get("Exam_Number"));
						 requestMap.put("examNumber", examCode);
						 order2 = scoreSearchService.getOrderCourseHx(requestMap);
						 orderCourse = order2 + " / " + totalHx;
						 map.put("hx", orderCourse);
						 break;
					 case "sxzz": //思想政治
						 requestMap.put("course", "sxzz");
						 String totalSxzz = scoreSearchService.getTotalCourse(requestMap);
						 examCode = trimString(map.get("Exam_Number"));
						 requestMap.put("examNumber", examCode);
						 order2 = scoreSearchService.getOrderCourseSxzz(requestMap);
						 orderCourse = order2 + " / " + totalSxzz;
						 map.put("sxzz", orderCourse);
						 break;
					 case "ls": //历史
						 requestMap.put("course", "ls");
						 String totalLs = scoreSearchService.getTotalCourse(requestMap);
						 examCode = trimString(map.get("Exam_Number"));
						 requestMap.put("examNumber", examCode);
						 order2 = scoreSearchService.getOrderCourseLs(requestMap);
						 orderCourse = order2 + " / " + totalLs;
						 map.put("ls", orderCourse);
						 break;
					 case "kx": //科学
						 requestMap.put("course", "kx");
						 String totalKx = scoreSearchService.getTotalCourse(requestMap);
						 examCode = trimString(map.get("Exam_Number"));
						 requestMap.put("examNumber", examCode);
						 order2 = scoreSearchService.getOrderCourseKx(requestMap);
						 orderCourse = order2 + " / " + totalKx;
						 map.put("kx", orderCourse);
						 break;
					 case "xxkj": //信息科技
						 requestMap.put("course", "xxkj");
						 String totalXxkj = scoreSearchService.getTotalCourse(requestMap);
						 examCode = trimString(map.get("Exam_Number"));
						 requestMap.put("examNumber", examCode);
						 order2 = scoreSearchService.getOrderCourseXxkj(requestMap);
						 orderCourse = order2 + " / " + totalXxkj;
						 map.put("xxkj", orderCourse);
						 break;
					 case "ms": //美术
						 requestMap.put("course", "ms");
						 String totalMs = scoreSearchService.getTotalCourse(requestMap);
						 examCode = trimString(map.get("Exam_Number"));
						 requestMap.put("examNumber", examCode);
						 order2 = scoreSearchService.getOrderCourseMs(requestMap);
						 orderCourse = order2 + " / " + totalMs;
						 map.put("ms", orderCourse);
						 break;
					 case "ty": //体育
						 requestMap.put("course", "ty");
						 String totalTy = scoreSearchService.getTotalCourse(requestMap);
						 examCode = trimString(map.get("Exam_Number"));
						 requestMap.put("examNumber", examCode);
						 order2 = scoreSearchService.getOrderCourseTy(requestMap);
						 orderCourse = order2 + " / " + totalTy;
						 map.put("ty", orderCourse);
						 break;
					 case "yyue": //音乐
						 requestMap.put("course", "yyue");
						 String totalYyue = scoreSearchService.getTotalCourse(requestMap);
						 examCode = trimString(map.get("Exam_Number"));
						 requestMap.put("examNumber", examCode);
						 order2 = scoreSearchService.getOrderCourseYy(requestMap);
						 orderCourse = order2 + " / " + totalYyue;
						 map.put("yyue", orderCourse);
						 break;
					
					 case "sw": //生物
						 requestMap.put("course", "sw");
						 String totalSw = scoreSearchService.getTotalCourse(requestMap);
						 examCode = trimString(map.get("Exam_Number"));
						 requestMap.put("examNumber", examCode);
						 order2 = scoreSearchService.getOrderCourseSw(requestMap);
						 orderCourse = order2 + " / " + totalSw;
						 map.put("sw", orderCourse);
						 break;
					 case "zr": // 自然
						 requestMap.put("course", "zr");
						 String totalZr = scoreSearchService.getTotalCourse(requestMap);
						 examCode = trimString(map.get("Exam_Number"));
						 requestMap.put("examNumber", examCode);
						 order2 = scoreSearchService.getOrderCourseZr(requestMap);
						 orderCourse = order2 + " / " + totalZr;
						 map.put("zr", orderCourse);
						 break;
					 case "yjxkc": //研究型课程
						 requestMap.put("course", "yjxkc");	
						 String totalYjxkc = scoreSearchService.getTotalCourse(requestMap);
						 requestMap.put("examNumber", requestMap.get("examNumber"));
						 order2 = scoreSearchService.getOrderCourseYjxkc(requestMap);
						 orderCourse = order2 + " / " + totalYjxkc;
						 map.put("yjxkc", orderCourse);
						 break;
					 case "tzxkc": //拓展型课程
						 requestMap.put("course", "tzxkc");
						 String totalTzxkc = scoreSearchService.getTotalCourse(requestMap);
						 requestMap.put("examNumber", requestMap.get("examNumber"));
						 order2 = scoreSearchService.getOrderCourseTzxkc(requestMap);
						 orderCourse = order2 + " / " + totalTzxkc;
						 map.put("tzxkc", orderCourse);
						 break;
					 case "njyy": //牛津英语
						 requestMap.put("course", "njyy");
						 String totalNjyy = scoreSearchService.getTotalCourse(requestMap);
						 examCode = trimString(map.get("Exam_Number"));
						 requestMap.put("examNumber", examCode);
						 order2 = scoreSearchService.getOrderCourseNjyy(requestMap);
						 orderCourse = order2 + " / " + totalNjyy;
						 map.put("njyy", orderCourse);
						 break;
					 case "xsjyy": //高中新世纪英语
						 requestMap.put("course", "xsjyy");
						 String totalXsjyy = scoreSearchService.getTotalCourse(requestMap);
						 examCode = trimString(map.get("Exam_Number"));
						 requestMap.put("examNumber", examCode);
						 order2 = scoreSearchService.getOrderCourseXsjyy(requestMap);
						 orderCourse = order2 + " / " + totalXsjyy;
						 map.put("xsjyy", orderCourse);
						 break;
					 }
				 }
				 map.put("orderDistrict", orderDistrict);
				 map.put("orderGrade", orderGrade);
				 newList.add(map);
			}
		}
		
		/*if("school".equals(roleType)){  //校级 （年级名次、班级名次）
			for(Map<String,Object> map : list){  
				 String totalGrade = scoreSearchService.getTotalSchool(requestMap); //年级总人数
				 order = trimString(map.get("orderDistrict"));//年级名次
				 orderGrade = order + " / " + totalGrade;//年级排名 例：2/200
				 String classId = trimString(map.get("Class_Id"));
				 requestMap.put("classId", classId);
				 String totalClass = scoreSearchService.getTotalClass(requestMap);//班级总人数
				 examCode = trimString(map.get("Exam_Number"));
				 requestMap.put("examNumber", examCode);
				 order1 = scoreSearchService.getOrderClass(requestMap);//班级名次				 
				 orderClass = order1 + " / " + totalClass;//班级排名 例: 4/70
				 
				 map.put("orderGrade", orderGrade);
				 map.put("orderClass", orderClass);
				 newList.add(map);
			}
		}*/
		
		
		PagingResult<Map<String, Object>> newPagingResult = new PagingResult<Map<String, Object>>(list,
				pagingResult.getPage(), pagingResult.getPageSize(), pagingResult.getRecords());
		out.print(getSerializer().formatObject(newPagingResult));
        System.out.println("耗时==:"+( (System.currentTimeMillis()-startTime)/1000)+"秒!");
	}
	
	/**
	 * @Title: exportExcelForAdmin
	 * @Description: 青浦管理员，位次查询，导出excel数据
	 * @author wangchaofa
	 * @date 2016年12月12日 
	 * @param request
	 * @param response
	 * @throws UnsupportedEncodingException
	 * @return void
	 */
	@SuppressWarnings("unused")
	@RequestMapping(params="command=exportExcelForAdmin")
	public void exportExcelForAdmin(HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException{
		request.setCharacterEncoding("UTF-8");
	    String datas=request.getParameter("data");
    	Map<String,Object> requestMap = this.getSerializer().parseMap(datas) ;
    	@SuppressWarnings("unchecked")
		List<String> courseTxtList = (List<String>) requestMap.get("courseTxt");
    	@SuppressWarnings("unchecked")
		List<String> courseValList = (List<String>) requestMap.get("courseVal");
    	String schoolCode = null;
    	String roleType = null; //登录角色类型
    	String loginName=SecurityContext.getPrincipal().getUsername();
		//根据登录名查询角色Code
		String roleCode = teacherManagementService.selectRoleCodeByLoginName(loginName);
		if("schoolPlainAdmin".equals(roleCode) || "schoolAdmin".equals(roleCode)) {
			schoolCode = examNumberManageService.getSchoolCodeByLoginName(loginName);
			roleType = "school";
		}else{
			roleType = "district";
		}
    	String grade = requestMap.get("grade").toString();
		String schoolType = null;
		if("11".equals(grade) || "12".equals(grade) || "13".equals(grade) || "14".equals(grade) || "15".equals(grade)){
			schoolType = "xx";
		}
		else if("16".equals(grade) || "17".equals(grade) || "18".equals(grade) || "19".equals(grade)){
			schoolType = "cz";
		}
		else if("31".equals(grade) || "32".equals(grade) || "33".equals(grade)){
			schoolType = "gz";
		}
    	String index=trimString(requestMap.get("idx").toString());
    	String columnIndex=trimString(requestMap.get("ci").toString());
    	String sortOrder=trimString(requestMap.get("so").toString());

		String stuCode = trimString(requestMap.get("examNumberOrStuCode").toString());
		String examNumber = trimString(requestMap.get("examNumberOrStuCode").toString());
		String time = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		int year = parseInteger(time.substring(0, time.indexOf("-")));
		int month = parseInteger(time.substring(time.indexOf("-") + 1, time.lastIndexOf("-")));
		String schoolYear = "";
		if (month < 9) {
			schoolYear = (year - 1) + "-" + year;
		} else {
			schoolYear = year + "-" + (year + 1);
		}
		if (null == requestMap.get("schoolYear")) {
			requestMap.put("schoolYear", schoolYear);
		}
		
		requestMap.put("schoolCode", schoolCode);
		requestMap.put("schoolType", schoolType);
		requestMap.put("stuCode", stuCode);
		requestMap.put("examNumber", examNumber);
		requestMap.put("index", index);
		requestMap.put("sortOrder", sortOrder);
		//教导员查询成绩导出excel
		List<Map<String, Object>>  paramList = schoolPlainAdminScoreSearchService.selectExportScoreDataForSchoolAdmin(requestMap);
		String totalDistrict = scoreSearchService.getTotalDistrict(requestMap);  //区级总人数
		List<Map<String,Object>> newList = new ArrayList<>();
		String order = null;
		String order1 = null;
		String order2 = null;
		String orderDistrict = null;//区级排名
		String orderGrade = null;//年级排名
		String orderClass = null;//班级排名
		String examCode = null;//考号
		String orderCourse = null;  //科目排名
		
		  //排名处理
        //取区级名次及各科区级名次(key=学籍号,val=(key=科目,val=名次))
        Map<String,Map<String,Object>> qjRanks=new HashMap<>();
        List<Map<String, Object>> maps = scoreSearchService.searchRankQj(requestMap);
        for (Map<String, Object> map : maps) {
            qjRanks.put(parseString(map.get("XJFH")),map);
        }
        //取年级名次(key=学籍号,val=名次)
        Map<String,Map<String,Object>> njRanks=new HashMap<>();
        List<Map<String, Object>> maps2 = scoreSearchService.searchRankNj(requestMap);
        for (Map<String, Object> map : maps2) {
            njRanks.put(parseString(map.get("XJFH")),map);
        }
		
        for (Map<String, Object> m : paramList) {
            String xjfh =parseString( m.get("XJFH"));
            Map<String, Object> qjRank = qjRanks.get(xjfh);
            Object school_name = m.get("School_Name");
            if(qjRank!=null && qjRank.size()>0){
            	String orderClass1 =parseString( qjRank.get("rank_total"));
                String totalGrade = parseString(m.get("totalSchool"));//年级总人数
                orderGrade = njRanks.get(xjfh).get("rank_nj") + " / " + totalGrade;
                m.put("School_Short_Name",school_name);
                orderDistrict = orderClass1 + " / " + totalDistrict;  //区级排名  例： 1/200
                m.put("orderDistrict", orderDistrict);
                m.put("orderGrade", orderGrade);
                if (m.containsKey("yw")) { String yw = parseString(qjRank.get("rank_yw")); String yw1 = !"".equals(yw)?(yw + " / " + totalDistrict):""; m.put("yw", yw1); }
                if (m.containsKey("sx")) { String sx = parseString(qjRank.get("rank_sx")); String sx1 = !"".equals(sx)?(sx + " / " + totalDistrict):""; m.put("sx", sx1); }
                if (m.containsKey("yy")) { String sx = parseString(qjRank.get("rank_yy")); String sx1 = !"".equals(sx)?(sx + " / " + totalDistrict):""; m.put("yy", sx1); }
                if (m.containsKey("zr")) { String sx = parseString(qjRank.get("rank_zr")); String sx1 = !"".equals(sx)?(sx + " / " + totalDistrict):""; m.put("zr", sx1); }
                if (m.containsKey("wl")) { String sx = parseString(qjRank.get("rank_wl")); String sx1 = !"".equals(sx)?(sx + " / " + totalDistrict):""; m.put("wl", sx1); }
                if (m.containsKey("hx")) { String sx = parseString(qjRank.get("rank_hx")); String sx1 = !"".equals(sx)?(sx + " / " + totalDistrict):""; m.put("hx", sx1); }
                if (m.containsKey("sxzz")) { String sx = parseString(qjRank.get("rank_sxzz")); String sx1 = !"".equals(sx)?(sx + " / " + totalDistrict):""; m.put("sxzz", sx1); }
                if (m.containsKey("ls")) { String sx = parseString(qjRank.get("rank_ls")); String sx1 = !"".equals(sx)?(sx + " / " + totalDistrict):""; m.put("ls", sx1); }
                if (m.containsKey("kx")) { String sx = parseString(qjRank.get("rank_kx")); String sx1 = !"".equals(sx)?(sx + " / " + totalDistrict):""; m.put("kx", sx1); }
                if (m.containsKey("dl")) { String sx = parseString(qjRank.get("rank_dl")); String sx1 = !"".equals(sx)?(sx + " / " + totalDistrict):""; m.put("dl", sx1); }
                if (m.containsKey("xxkj")) { String sx = parseString(qjRank.get("rank_xxkj")); String sx1 = !"".equals(sx)?(sx + " / " + totalDistrict):""; m.put("xxkj", sx1); }
                if (m.containsKey("ms")) { String sx = parseString(qjRank.get("rank_ms")); String sx1 = !"".equals(sx)?(sx + " / " + totalDistrict):""; m.put("ms", sx1); }
                if (m.containsKey("sw")) { String sx = parseString(qjRank.get("rank_sw")); String sx1 = !"".equals(sx)?(sx + " / " + totalDistrict):""; m.put("sw", sx1); }
                if (m.containsKey("ty")) { String sx = parseString(qjRank.get("rank_ty")); String sx1 = !"".equals(sx)?(sx + " / " + totalDistrict):""; m.put("ty", sx1); }
                if (m.containsKey("yyue")) { String sx = parseString(qjRank.get("rank_yyue")); String sx1 = !"".equals(sx)?(sx + " / " + totalDistrict):""; m.put("yyue", sx1); }
                if (m.containsKey("njyy")) { String sx = parseString(qjRank.get("rank_njyy")); String sx1 = !"".equals(sx)?(sx + " / " + totalDistrict):""; m.put("njyy", sx1); }
                if (m.containsKey("yjxkc")) { String sx = parseString(qjRank.get("rank_yjxkc")); String sx1 = !"".equals(sx)?(sx + " / " + totalDistrict):""; m.put("yjxkc", sx1); }
                if (m.containsKey("tzxkc")) { String sx = parseString(qjRank.get("rank_tzxkc")); String sx1 = !"".equals(sx)?(sx + " / " + totalDistrict):""; m.put("tzxkc", sx1); }
                if (m.containsKey("xsjyy")) { String sx = parseString(qjRank.get("rank_xsjyy")); String sx1 = !"".equals(sx)?(sx + " / " + totalDistrict):""; m.put("xsjyy", sx1); }
            }
        }
		
		if(false && "district".equals(roleType)){  //区级 （区级名次、年级名次）
			for(Map<String,Object> map : paramList){
				 examCode = trimString(map.get("Exam_Number"));				 
				 requestMap.put("examNumber", examCode);
				 
				 order = scoreSearchService.getOrderDistrict(requestMap);//区级名次
				 orderDistrict = order + " / " + totalDistrict;  //区级排名  例： 1/200
				 
				 requestMap.put("schoolCode", trimString(map.get("School_Code")));
				 String totalGrade = scoreSearchService.getTotalSchool(requestMap);//年级总人数
				 order1 = scoreSearchService.getOrderGrade(requestMap);//年级名次
				 orderGrade = order1 + " / " + totalGrade;

				 Set<Map.Entry<String,Object>> sets = map.entrySet();
				 for(Map.Entry<String,Object> entry:sets){

					 Map<String,Object> newMap = new HashMap<String,Object>();
					 String course1 = trimString(entry.getKey());
					 switch(course1){
					 
					 case "yw": //语文
						 requestMap.put("course", "yw");
						 String totalYw = scoreSearchService.getTotalCourse(requestMap);
						 examCode = trimString(map.get("Exam_Number"));
						 requestMap.put("examNumber", examCode);
						 order2 = scoreSearchService.getOrderCourseYw(requestMap);
						 orderCourse = order2 + " / " + totalYw;
						 map.put("yw", orderCourse);
						 break;
					 case "sx": //数学
						 requestMap.put("course", "sx");
						 String totalSx = scoreSearchService.getTotalCourse(requestMap);
						 examCode = trimString(map.get("Exam_Number"));
						 requestMap.put("examNumber", examCode);
						 order2 = scoreSearchService.getOrderCourseSx(requestMap);
						 orderCourse = order2 + " / " + totalSx;
						 map.put("sx", orderCourse);
						 break;
					 case "yy": //外语
						 requestMap.put("course", "yy");
						 String totalYy = scoreSearchService.getTotalCourse(requestMap);
						 examCode = trimString(map.get("Exam_Number"));
						 requestMap.put("examNumber", examCode);
						 order2 = scoreSearchService.getOrderCourseYy(requestMap);
						 orderCourse = order2 + " / " + totalYy;
						 map.put("yy", orderCourse);
						 break;
					 case "wl": //物理
						 requestMap.put("course", "wl");
						 String totalWl = scoreSearchService.getTotalCourse(requestMap);
						 examCode = trimString(map.get("Exam_Number"));
						 requestMap.put("examNumber", examCode);
						 order2 = scoreSearchService.getOrderCourseWl(requestMap);
						 orderCourse = order2 + " / " + totalWl;
						 map.put("wl", orderCourse);
						 break;
					 case "hx": //化学
						 requestMap.put("course", "hx");
						 String totalHx = scoreSearchService.getTotalCourse(requestMap);
						 examCode = trimString(map.get("Exam_Number"));
						 requestMap.put("examNumber", examCode);
						 order2 = scoreSearchService.getOrderCourseHx(requestMap);
						 orderCourse = order2 + " / " + totalHx;
						 map.put("hx", orderCourse);
						 break;
					 case "sxzz": //思想政治
						 requestMap.put("course", "sxzz");
						 String totalSxzz = scoreSearchService.getTotalCourse(requestMap);
						 examCode = trimString(map.get("Exam_Number"));
						 requestMap.put("examNumber", examCode);
						 order2 = scoreSearchService.getOrderCourseSxzz(requestMap);
						 orderCourse = order2 + " / " + totalSxzz;
						 map.put("sxzz", orderCourse);
						 break;
					 case "ls": //历史
						 requestMap.put("course", "ls");
						 String totalLs = scoreSearchService.getTotalCourse(requestMap);
						 examCode = trimString(map.get("Exam_Number"));
						 requestMap.put("examNumber", examCode);
						 order2 = scoreSearchService.getOrderCourseLs(requestMap);
						 orderCourse = order2 + " / " + totalLs;
						 map.put("ls", orderCourse);
						 break;
					 case "kx": //科学
						 requestMap.put("course", "kx");
						 String totalKx = scoreSearchService.getTotalCourse(requestMap);
						 examCode = trimString(map.get("Exam_Number"));
						 requestMap.put("examNumber", examCode);
						 order2 = scoreSearchService.getOrderCourseKx(requestMap);
						 orderCourse = order2 + " / " + totalKx;
						 map.put("kx", orderCourse);
						 break;
					 case "xxkj": //信息科技
						 requestMap.put("course", "xxkj");
						 String totalXxkj = scoreSearchService.getTotalCourse(requestMap);
						 examCode = trimString(map.get("Exam_Number"));
						 requestMap.put("examNumber", examCode);
						 order2 = scoreSearchService.getOrderCourseXxkj(requestMap);
						 orderCourse = order2 + " / " + totalXxkj;
						 map.put("xxkj", orderCourse);
						 break;
					 case "ms": //美术
						 requestMap.put("course", "ms");
						 String totalMs = scoreSearchService.getTotalCourse(requestMap);
						 examCode = trimString(map.get("Exam_Number"));
						 requestMap.put("examNumber", examCode);
						 order2 = scoreSearchService.getOrderCourseMs(requestMap);
						 orderCourse = order2 + " / " + totalMs;
						 map.put("ms", orderCourse);
						 break;
					 case "ty": //体育
						 requestMap.put("course", "ty");
						 String totalTy = scoreSearchService.getTotalCourse(requestMap);
						 examCode = trimString(map.get("Exam_Number"));
						 requestMap.put("examNumber", examCode);
						 order2 = scoreSearchService.getOrderCourseTy(requestMap);
						 orderCourse = order2 + " / " + totalTy;
						 map.put("ty", orderCourse);
						 break;
					 case "yyue": //音乐
						 requestMap.put("course", "yyue");
						 String totalYyue = scoreSearchService.getTotalCourse(requestMap);
						 examCode = trimString(map.get("Exam_Number"));
						 requestMap.put("examNumber", examCode);
						 order2 = scoreSearchService.getOrderCourseYy(requestMap);
						 orderCourse = order2 + " / " + totalYyue;
						 map.put("yyue", orderCourse);
						 break;
					
					 case "sw": //生物
						 requestMap.put("course", "sw");
						 String totalSw = scoreSearchService.getTotalCourse(requestMap);
						 examCode = trimString(map.get("Exam_Number"));
						 requestMap.put("examNumber", examCode);
						 order2 = scoreSearchService.getOrderCourseSw(requestMap);
						 orderCourse = order2 + " / " + totalSw;
						 map.put("sw", orderCourse);
						 break;
					 case "zr": // 自然
						 requestMap.put("course", "zr");
						 String totalZr = scoreSearchService.getTotalCourse(requestMap);
						 examCode = trimString(map.get("Exam_Number"));
						 requestMap.put("examNumber", examCode);
						 order2 = scoreSearchService.getOrderCourseZr(requestMap);
						 orderCourse = order2 + " / " + totalZr;
						 map.put("zr", orderCourse);
						 break;
					 case "yjxkc": //研究型课程
						 requestMap.put("course", "yjxkc");	
						 String totalYjxkc = scoreSearchService.getTotalCourse(requestMap);
						 requestMap.put("examNumber", requestMap.get("examNumber"));
						 order2 = scoreSearchService.getOrderCourseYjxkc(requestMap);
						 orderCourse = order2 + " / " + totalYjxkc;
						 map.put("yjxkc", orderCourse);
						 break;
					 case "tzxkc": //拓展型课程
						 requestMap.put("course", "tzxkc");
						 String totalTzxkc = scoreSearchService.getTotalCourse(requestMap);
						 requestMap.put("examNumber", requestMap.get("examNumber"));
						 order2 = scoreSearchService.getOrderCourseTzxkc(requestMap);
						 orderCourse = order2 + " / " + totalTzxkc;
						 map.put("tzxkc", orderCourse);
						 break;
					 case "njyy": //牛津英语
						 requestMap.put("course", "njyy");
						 String totalNjyy = scoreSearchService.getTotalCourse(requestMap);
						 examCode = trimString(map.get("Exam_Number"));
						 requestMap.put("examNumber", examCode);
						 order2 = scoreSearchService.getOrderCourseNjyy(requestMap);
						 orderCourse = order2 + " / " + totalNjyy;
						 map.put("njyy", orderCourse);
						 break;
					 case "xsjyy": //高中新世纪英语
						 requestMap.put("course", "xsjyy");
						 String totalXsjyy = scoreSearchService.getTotalCourse(requestMap);
						 examCode = trimString(map.get("Exam_Number"));
						 requestMap.put("examNumber", examCode);
						 order2 = scoreSearchService.getOrderCourseXsjyy(requestMap);
						 orderCourse = order2 + " / " + totalXsjyy;
						 map.put("xsjyy", orderCourse);
						 break;
					 }
				 }
				 map.put("orderDistrict", orderDistrict);
				 map.put("orderGrade", orderGrade);
				 newList.add(map);
			}
			}
			String fileDisplay=requestMap.get("scoreHtml").toString()+".xls";

	    	try{
				response.setContentType("application/x-download");
				fileDisplay = URLEncoder.encode(fileDisplay, "utf-8");
		        response.addHeader("Content-Disposition", "attachment;filename="+ fileDisplay);
		        //创建一个webbook，对应一个Excel文件  
		        HSSFWorkbook workbook = new HSSFWorkbook();
		        //在webbook中添加一个sheet,对应Excel文件中的sheet  
		        HSSFSheet sheet = workbook.createSheet();
		        //在sheet中添加表头第0行
		        workbook.setSheetName(0,"导出成绩数据");
		        
		        sheet.setColumnWidth((short)0,(short)2000);  
		        sheet.setColumnWidth((short)1,(short)6000);  
		        sheet.setColumnWidth((short)2,(short)6000);  
		        sheet.setColumnWidth((short)3,(short)3000);
		        sheet.setColumnWidth((short)4,(short)3000);
		        sheet.setColumnWidth((short)5,(short)3000);
		        sheet.setColumnWidth((short)6,(short)3000);
		        sheet.setColumnWidth((short)7,(short)3000);
		      //，创建单元格，并设置值表头 设置表头居中  
		        HSSFCellStyle titleStyle = workbook.createCellStyle();  
		        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式  
		        
		        HSSFFont font = workbook.createFont();
		        font.setFontName("宋体");  
		        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		        font.setFontHeightInPoints((short) 11);//设置字体大小  
		        titleStyle.setFont(font);
		        
		        //总汇字体
		        HSSFCellStyle countStyle = workbook.createCellStyle();  
		        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式  
		        HSSFFont countFont = workbook.createFont();    
		        countFont.setFontName("宋体");  
		        countFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		        countFont.setFontHeightInPoints((short)14);//设置字体大小  
		        countFont.setColor(HSSFColor.RED.index);//设置字体颜色
		        countStyle.setFont(countFont);
		        
		        
		        //学生信息字体
		        HSSFCellStyle style = workbook.createCellStyle();  
		        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); 
		        //表头
		        List<String> titleList = new ArrayList<>();
		        titleList.add("序号");
		        titleList.add("考号");
		        titleList.add("学籍号");
		        titleList.add("姓名");
		        if("schoolPlainAdmin".equals(roleCode) || "schoolAdmin".equals(roleCode)) {
				
		        }else{
					titleList.add("学校");
				}
		        titleList.add("年级");
		        titleList.add("班级");

		        //动态追加科目名
		        for(String str : courseTxtList){
		        	titleList.add(str);
		        }
		        if("schoolPlainAdmin".equals(roleCode) || "schoolAdmin".equals(roleCode)) {
		        	titleList.add("年级名次");
		        	titleList.add("班级名次");
				}else{
					titleList.add("区级名次");
					titleList.add("年级名次");
				}
		     
		        HSSFRow row = sheet.createRow((short) 0);
		        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, (titleList.size()-1)));    
		        HSSFCell cellTiltle = row.createCell(0);  
		        row.setHeight((short)350);
		        cellTiltle.setCellStyle(countStyle);
		        String tit=requestMap.get("scoreHtml").toString();
		        cellTiltle.setCellValue(tit);
		        
		        row = sheet.createRow((short) 1);
		        
		        //设置title
		        for(int i=0;i<titleList.size();i++)
		        {
		        	HSSFCell cell=row.createCell((short)i);
		        	cell.setCellStyle(titleStyle);
		        	cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		            cell.setCellValue(titleList.get(i));  
		        }
		        
		        //写入实体数据
		       int j=1;
		        for(int i=0;i<paramList.size();i++)
		        {
		        	Map<String,Object> paramMap =paramList.get(i);
		        	row=sheet.createRow((short) j+ 1);
		        	j++;
		        	HSSFCell cell=row.createCell((short)0);
		        	cell.setCellStyle(style);
		        	cell.setCellValue((i+1)); 
		        	
		          	HSSFCell cellOne=row.createCell((short)1);
		        	cellOne.setCellStyle(style);
		        	cellOne.setCellValue(parseString(paramMap.get("Exam_Number"))); 
		        	
		        	
		        	HSSFCell cellTwo=row.createCell((short)2);
		        	cellTwo.setCellStyle(style);
		        	cellTwo.setCellValue(parseString(paramMap.get("XJFH")));
		        	
		        	HSSFCell cellThree=row.createCell((short)3);
		        	cellThree.setCellStyle(style);
		        	cellThree.setCellValue(parseString(paramMap.get("Name")));
		        	String Grade_Id = "";
		        	if("11".equals(paramMap.get("Grade_Id").toString())){
						Grade_Id = "一年级";
					}
					else if("12".equals(paramMap.get("Grade_Id").toString())){
						Grade_Id = "二年级";
					}
					else if("13".equals(paramMap.get("Grade_Id").toString())){
						Grade_Id = "三年级";
					}
					else if("14".equals(paramMap.get("Grade_Id").toString())){
						Grade_Id = "四年级";
					}
					else if("15".equals(paramMap.get("Grade_Id").toString())){
						Grade_Id = "五年级";
					}
					else if("16".equals(paramMap.get("Grade_Id").toString())){
						Grade_Id = "六年级";
					}
					else if("17".equals(paramMap.get("Grade_Id").toString())){
						Grade_Id = "七年级";
					}
					else if("18".equals(paramMap.get("Grade_Id").toString())){
						Grade_Id = "八年级";
					}
					else if("19".equals(paramMap.get("Grade_Id").toString())){
						Grade_Id = "九年级";
					}
					else if("31".equals(paramMap.get("Grade_Id").toString())){
						Grade_Id = "高一年级";
					}
					else if("32".equals(paramMap.get("Grade_Id").toString())){
						Grade_Id = "高二年级";
					}
					else if("33".equals(paramMap.get("Grade_Id").toString())){
						Grade_Id = "高三年级";
					}
		        	
		        	String Class_Id = "";
		        	if("01".equals(paramMap.get("Class_Id").toString())){
		        		Class_Id = "（1）班";
					}
					else if("02".equals(paramMap.get("Class_Id").toString())){
						Class_Id = "（2）班";
					}
					else if("03".equals(paramMap.get("Class_Id").toString())){
						Class_Id = "（3）班";
					}
					else if("04".equals(paramMap.get("Class_Id").toString())){
						Class_Id = "（4）班";
					}
					else if("05".equals(paramMap.get("Class_Id").toString())){
						Class_Id = "（5）班";
					}
					else if("06".equals(paramMap.get("Class_Id").toString())){
						Class_Id = "（6）班";
					}
					else if("07".equals(paramMap.get("Class_Id").toString())){
						Class_Id = "（7）班";
					}
					else if("08".equals(paramMap.get("Class_Id").toString())){
						Class_Id = "（8）班";
					}
					else if("09".equals(paramMap.get("Class_Id").toString())){
						Class_Id = "（9）班";
					}
					else if("10".equals(paramMap.get("Class_Id").toString())){
						Class_Id = "（10）班";
					}
					else if("11".equals(paramMap.get("Grade_Id").toString())){
						Class_Id = "（11）班";
					}
					else if("12".equals(paramMap.get("Class_Id").toString())){
						Class_Id = "（12）班";
					}
					else if("13".equals(paramMap.get("Class_Id").toString())){
						Class_Id = "（13）班";
					}
					else if("14".equals(paramMap.get("Class_Id").toString())){
						Class_Id = "（14）班";
					}
					else if("15".equals(paramMap.get("Grade_Id").toString())){
						Class_Id = "（15）班";
					}
		        	if("schoolPlainAdmin".equals(roleCode) || "schoolAdmin".equals(roleCode)) {
			        	 
			        	HSSFCell cellFour=row.createCell((short)4);
			        	cellFour.setCellStyle(style);
			        	cellFour.setCellValue(Grade_Id); 
			        	
			        	HSSFCell cellFive=row.createCell((short)5);
			        	cellFive.setCellStyle(style); 	
			        	cellFive.setCellValue(Class_Id); 
				        		        	
			        	for(int k = 0;k < courseValList.size();k++){
			        		HSSFCell cellAvalible = row.createCell((short)(6+k));
			        		sheet.setColumnWidth(6+k, 3000);
			        		cellAvalible.setCellStyle(style);
			        		cellAvalible.setCellValue(parseString(paramMap.get(courseValList.get(k))));
			        	}
			        	
			        	HSSFCell cellTotalScore=row.createCell((short)(6+courseValList.size()));
			        	cellTotalScore.setCellStyle(style);
			        	cellTotalScore.setCellValue(parseString(paramMap.get("Total_Score")));
			        	
			        	HSSFCell cellEight = row.createCell((short)7+courseValList.size());
			        	cellEight.setCellStyle(style);
			        	cellEight.setCellValue(parseString(paramMap.get("orderGrade")));
			        	
			        	HSSFCell cellNine = row.createCell((short)8+courseValList.size());
			        	cellNine.setCellStyle(style);
			        	cellNine.setCellValue(parseString(paramMap.get("orderClass")));
					}else{
						HSSFCell cellFour=row.createCell((short)4);
			        	sheet.setColumnWidth(4, 7000);
			        	cellFour.setCellStyle(style);
			        	cellFour.setCellValue(parseString(paramMap.get("School_Short_Name"))); 
			        	
			        	HSSFCell cellFive=row.createCell((short)5);
			        	cellFive.setCellStyle(style);		        	
			        	cellFive.setCellValue(Grade_Id); 
			        	
			        	HSSFCell cellSix=row.createCell((short)6);
			        	cellSix.setCellStyle(style);		        	
			        	cellSix.setCellValue(Class_Id); 
			        		        	
			        	for(int k = 0;k < courseValList.size();k++){
			        		HSSFCell cellAvalible = row.createCell((short)(7+k));
			        		sheet.setColumnWidth(7+k, 3000);
			        		cellAvalible.setCellStyle(style);
			        		cellAvalible.setCellValue(parseString(paramMap.get(courseValList.get(k))));
			        	}			        
			        	
			        	HSSFCell cellEight = row.createCell((short)7+courseValList.size());
			        	cellEight.setCellStyle(style);
			        	cellEight.setCellValue(parseString(paramMap.get("orderDistrict")));
			        	
			        	HSSFCell cellNine = row.createCell((short)8+courseValList.size());
			        	cellNine.setCellStyle(style);
			        	cellNine.setCellValue(parseString(paramMap.get("orderGrade")));
					}
		        }
		        OutputStream out=response.getOutputStream();
				workbook.write(out);
		        out.flush();
		        out.close();
		        
		        
			}catch(Exception e){
				System.out.println(e);
			}
	 
	 }
	 
	
	
	/**
	 * @Title: printLook
	 * @Description: 老师，名次查询，打印预览
	 * @param request
	 * @param response
	 * @return ModelAndView
	 * @throws IOException 
	 * @throws ServletException 
	 */
	@RequestMapping(params = "command=printLook")
	public ModelAndView printLook(HttpServletRequest request, HttpServletResponse response,java.io.PrintWriter out) throws ServletException, IOException {
		String datas=request.getParameter("data");
    	Map<String,Object> requestMap = this.getSerializer().parseMap(datas) ;
    	//System.out.println("====requestMap===="+requestMap);
    	String grade=(String) requestMap.get("grade");
		List<String> classList = (List<String>) requestMap.get("classArr");
		List<Map<String,Object>> list1 = new ArrayList<>();
		for(String str : classList) {
			Map<String,Object> map = new HashMap<String,Object>();
			String[] arr = str.split(",");
			map.put("grade", arr[0]);
			map.put("class", arr[1]);
			list1.add(map);
		}
		String loginName = SecurityContext.getPrincipal().getUsername();
		String schoolCode = examNumberManageService.getSchoolCodeByLoginName(loginName);
		String stuCode = trimString(requestMap.get("examNumberOrStuCode").toString());
		String examNumber = trimString(requestMap.get("examNumberOrStuCode").toString());
		String gradeTxt= trimString(requestMap.get("gradeTxt").toString());
		String index=trimString(requestMap.get("idx").toString());
    	String sortOrder=trimString(requestMap.get("so").toString());
    	String scoreHtml=trimString(requestMap.get("scoreHtml").toString());
    	List<String> courseName=new ArrayList<>();
		List<String> courseValue=new ArrayList<>();
		String sourceStrArray = trimString(requestMap.get("courseTxt").toString());
		String sourceStrArrayVal = trimString(requestMap.get("course").toString());
		courseName.add(sourceStrArray);
		courseValue.add(sourceStrArrayVal);
		String schoolType = null;
		if("11".equals(grade) || "12".equals(grade) || "13".equals(grade) || "14".equals(grade) || "15".equals(grade)){
			schoolType = "xx";
		}
		else if("16".equals(grade) || "17".equals(grade) || "18".equals(grade) || "19".equals(grade)){
			schoolType = "cz";
		}
		else if("31".equals(grade) || "32".equals(grade) || "33".equals(grade)){
			schoolType = "gz";
		}
		requestMap.put("schoolCode", schoolCode);
		requestMap.put("schoolType", schoolType);
		requestMap.put("stuCode", stuCode);
		requestMap.put("examNumber", examNumber);
		requestMap.put("list1", list1);
		requestMap.put("index", index);
		requestMap.put("sortOrder", sortOrder);
		List<Map<String, Object>> pagingResult = scoreSearchService.searchScoreForTeacherPage(requestMap);
		//System.out.println("===pagingResult==="+pagingResult);
		List<Map<String,Object>> newList = new ArrayList();
		String totalDistrict = scoreSearchService.getTotalDistrict(requestMap);  //区级总人数
		String order = null;
		String order1 = null;
		String orderDistrict = null;//区级排名
		String orderSchool = null;//年级排名
		String orderClass = null;//班级排名
		String examCode = null;//考号
		for(Map<String,Object> map : pagingResult){  
			 String totalSchool = scoreSearchService.getTotalSchool(requestMap); //年级总人数
			 examCode = trimString(map.get("Exam_Number"));
			 requestMap.put("examNumber", examCode);
			 order = scoreSearchService.getOrderGradeTec(requestMap);//年级名次
			 orderSchool = order + " / " + totalSchool;//年级排名 例：2/200
			 
			 String classId = trimString(map.get("Class_Id"));
			 requestMap.put("classId", classId);
			 String totalClass = scoreSearchService.getTotalClass(requestMap);//班级总人数
			 order1 = trimString(map.get("orderDistrict"));//班级名次				 
			 orderClass = order1 + " / " + totalClass;//班级排名 例: 4/70
			 
			 map.put("orderDistrict", orderSchool);
			 map.put("orderClass", orderClass);
			 newList.add(map);
		}
		
		ModelAndView mvAndView ;
		mvAndView = new ModelAndView("/scoreManage/printOrderTeacher");
		if(pagingResult.size()>0){
			for(int i=0;i<pagingResult.size();i++){
				switch ((String)pagingResult.get(i).get("Class_Id")) {
					case "01":
						pagingResult.get(i).put("Class_Id", "(1)班");
					break;
					case "02":
						pagingResult.get(i).put("Class_Id", "(2)班");
					break;
					case "03":
						pagingResult.get(i).put("Class_Id", "(3)班");
					break;
					case "04":
						pagingResult.get(i).put("Class_Id", "(4)班");
					break;
					case "05":
						pagingResult.get(i).put("Class_Id", "(5)班");
					break;
					case "06":
						pagingResult.get(i).put("Class_Id", "(6)班");
					break;
					case "07":
						pagingResult.get(i).put("Class_Id", "(7)班");
					break;
					case "08":
						pagingResult.get(i).put("Class_Id", "(8)班");
					break;
					case "09":
						pagingResult.get(i).put("Class_Id", "(9)班");
					break;
					case "10":
						pagingResult.get(i).put("Class_Id", "(10)班");
					break;
					default:
				}
			}
		}
		mvAndView.addObject("pagingResult",pagingResult);
		mvAndView.addObject("courseName",courseName);
		mvAndView.addObject("courseVal",courseValue);
		request.setAttribute("courseName", courseName);
		request.setAttribute("courseVal", courseValue);
		request.setAttribute("pagingResult", pagingResult);
		request.setAttribute("scoreHtml", scoreHtml);
		request.setAttribute("gradeTxt", gradeTxt);
		return mvAndView;
		//request.getRequestDispatcher("/statisticsAnalysis/printLook.do").forward(request,response);
		 //out.print(getSerializer().formatList(pagingResult));
	}
	
	
	
	/**
	 * @Title: printLookForAdmin
	 * @Description: 青浦超级管理员,名次查询，打印预览
	 * @author wangchaofa
	 * @date 2016年11月30日 
	 * @param request
	 * @param response
	 * @return ModelAndView
	 * @throws IOException 
	 * @throws ServletException 
	 */
	@SuppressWarnings("unused")
	@RequestMapping(params = "command=printLookForAdmin")
	public ModelAndView printLookForAdmin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String schoolYear = request.getParameter("schoolYear"); 
		String school = request.getParameter("school");
		String school1=school;
		String grade = request.getParameter("grade"); 
		String term = request.getParameter("term"); 
		String idx=request.getParameter("idx");
		String so=request.getParameter("so");
		String examNumber=request.getParameter("examNum");
		String examType = request.getParameter("examType"); 
		String examNumberOrStuCode = request.getParameter("examNumberOrStuCode");
		String courseVal = request.getParameter("courseVal");
		String scoreHtml=request.getParameter("scoreHtml");
		String courseTxt = request.getParameter("courseTxt");
		String gradeTxt=request.getParameter("gradeTxt");
		List<String> courseName=new ArrayList<>();
		List<String> courseValue=new ArrayList<>();
		String[] sourceStrArray = courseTxt.split(",");
		String[] sourceStrArrayVal = courseVal.split(",");
		for(int i=0;i<sourceStrArray.length;i++){
			courseName.add(sourceStrArray[i]);
			courseValue.add(sourceStrArrayVal[i]);
		}
		String loginName=SecurityContext.getPrincipal().getUsername();
		String schoolCode = null;
		String roleType = null; //登录角色类型
		String examCode1 = null;//考试编号
		String roleCode = teacherManagementService.selectRoleCodeByLoginName(loginName);
		if("schoolPlainAdmin".equals(roleCode) || "schoolAdmin".equals(roleCode)) {
			schoolCode = examNumberManageService.getSchoolCodeByLoginName(loginName);
			roleType = "school";
			examCode1 = examNumber;
		}else{
			roleType = "district";
		}
		String schoolType = null;
		if("11".equals(grade) || "12".equals(grade) || "13".equals(grade) || "14".equals(grade) || "15".equals(grade)){
			schoolType = "xx";
		}
		else if("16".equals(grade) || "17".equals(grade) || "18".equals(grade) || "19".equals(grade)){
			schoolType = "cz";
		}
		else if("31".equals(grade) || "32".equals(grade) || "33".equals(grade)){
			schoolType = "gz";
		}
		Map<String, Object> requestMap=new HashMap<>();
		requestMap.put("schoolYear", schoolYear);
		requestMap.put("grade", grade);
		requestMap.put("term", term);
		requestMap.put("stuCode", examNumberOrStuCode);
		requestMap.put("examNumber", examNumberOrStuCode);
		//学校类型
		requestMap.put("schoolType", schoolType);
		ArrayList<String> schoolList=new ArrayList<>();
		if(!"null".equals(school)&&school!=null){
			String[] schoolArray = school.split(",");
			for(int i=0;i<schoolArray.length;i++){
				schoolList.add(schoolArray[i]);
			}
			requestMap.put("school", schoolList);
		}
		
		requestMap.put("schoolCode", schoolCode);
		requestMap.put("examType", examType);
		requestMap.put("courseVal", courseVal);
		requestMap.put("idx", idx);
		requestMap.put("so", so);
		if(examNumber!=null){
			requestMap.put("examCode", examNumber);
		}
		List<Map<String, Object>> pagingResult = scoreSearchService.searchScoreForqpAdminPage2(requestMap);
		List<Map<String,Object>> newList = new ArrayList<>();
		String totalDistrict = scoreSearchService.getTotalDistrict(requestMap);  //区级总人数
		String order = null;
		String order1 = null;
		String order2 = null;
		String orderDistrict = null;//区级排名
		String orderGrade = null;//年级排名
		String orderClass = null;//班级排名
		String examCode = null;//考号
		String orderCourse = null;  //科目排名
		//排名处理
        //取区级名次及各科区级名次(key=学籍号,val=(key=科目,val=名次))
        Map<String,Map<String,Object>> qjRanks=new HashMap<>();
        List<Map<String, Object>> maps = scoreSearchService.searchRankQj(requestMap);
        for (Map<String, Object> map : maps) {
            qjRanks.put(parseString(map.get("XJFH")),map);
        }
        //取年级名次(key=学籍号,val=名次)
        Map<String,Map<String,Object>> njRanks=new HashMap<>();
        List<Map<String, Object>> maps2 = scoreSearchService.searchRankNj(requestMap);
        for (Map<String, Object> map : maps2) {
            njRanks.put(parseString(map.get("XJFH")),map);
        }


        for (Map<String, Object> m : pagingResult) {
            String xjfh =parseString( m.get("XJFH"));
            Map<String, Object> qjRank = qjRanks.get(xjfh);
            if(qjRank!=null && qjRank.size()>0){
            	 Object school_name = m.get("School_Name");
                 String orderClass1 =parseString( qjRank.get("rank_total"));
                 String totalGrade = parseString(m.get("totalSchool"));//年级总人数
                 orderGrade = njRanks.get(xjfh).get("rank_nj") + " / " + totalGrade;
                 m.put("School_Short_Name",school_name);
                 orderDistrict = orderClass1 + " / " + totalDistrict;  //区级排名  例： 1/200
                 m.put("orderDistrict", orderDistrict);
                 m.put("orderGrade", orderGrade);


                 if (m.containsKey("yw")) { String yw = parseString(qjRank.get("rank_yw")); String yw1 = !"".equals(yw)?(yw + " / " + totalDistrict):""; m.put("yw", yw1); }
                 if (m.containsKey("sx")) { String sx = parseString(qjRank.get("rank_sx")); String sx1 = !"".equals(sx)?(sx + " / " + totalDistrict):""; m.put("sx", sx1); }
                 if (m.containsKey("yy")) { String sx = parseString(qjRank.get("rank_yy")); String sx1 = !"".equals(sx)?(sx + " / " + totalDistrict):""; m.put("yy", sx1); }
                 if (m.containsKey("zr")) { String sx = parseString(qjRank.get("rank_zr")); String sx1 = !"".equals(sx)?(sx + " / " + totalDistrict):""; m.put("zr", sx1); }
                 if (m.containsKey("wl")) { String sx = parseString(qjRank.get("rank_wl")); String sx1 = !"".equals(sx)?(sx + " / " + totalDistrict):""; m.put("wl", sx1); }
                 if (m.containsKey("hx")) { String sx = parseString(qjRank.get("rank_hx")); String sx1 = !"".equals(sx)?(sx + " / " + totalDistrict):""; m.put("hx", sx1); }
                 if (m.containsKey("sxzz")) { String sx = parseString(qjRank.get("rank_sxzz")); String sx1 = !"".equals(sx)?(sx + " / " + totalDistrict):""; m.put("sxzz", sx1); }
                 if (m.containsKey("ls")) { String sx = parseString(qjRank.get("rank_ls")); String sx1 = !"".equals(sx)?(sx + " / " + totalDistrict):""; m.put("ls", sx1); }
                 if (m.containsKey("kx")) { String sx = parseString(qjRank.get("rank_kx")); String sx1 = !"".equals(sx)?(sx + " / " + totalDistrict):""; m.put("kx", sx1); }
                 if (m.containsKey("dl")) { String sx = parseString(qjRank.get("rank_dl")); String sx1 = !"".equals(sx)?(sx + " / " + totalDistrict):""; m.put("dl", sx1); }
                 if (m.containsKey("xxkj")) { String sx = parseString(qjRank.get("rank_xxkj")); String sx1 = !"".equals(sx)?(sx + " / " + totalDistrict):""; m.put("xxkj", sx1); }
                 if (m.containsKey("ms")) { String sx = parseString(qjRank.get("rank_ms")); String sx1 = !"".equals(sx)?(sx + " / " + totalDistrict):""; m.put("ms", sx1); }
                 if (m.containsKey("sw")) { String sx = parseString(qjRank.get("rank_sw")); String sx1 = !"".equals(sx)?(sx + " / " + totalDistrict):""; m.put("sw", sx1); }
                 if (m.containsKey("ty")) { String sx = parseString(qjRank.get("rank_ty")); String sx1 = !"".equals(sx)?(sx + " / " + totalDistrict):""; m.put("ty", sx1); }
                 if (m.containsKey("yyue")) { String sx = parseString(qjRank.get("rank_yyue")); String sx1 = !"".equals(sx)?(sx + " / " + totalDistrict):""; m.put("yyue", sx1); }
                 if (m.containsKey("njyy")) { String sx = parseString(qjRank.get("rank_njyy")); String sx1 = !"".equals(sx)?(sx + " / " + totalDistrict):""; m.put("njyy", sx1); }
                 if (m.containsKey("yjxkc")) { String sx = parseString(qjRank.get("rank_yjxkc")); String sx1 = !"".equals(sx)?(sx + " / " + totalDistrict):""; m.put("yjxkc", sx1); }
                 if (m.containsKey("tzxkc")) { String sx = parseString(qjRank.get("rank_tzxkc")); String sx1 = !"".equals(sx)?(sx + " / " + totalDistrict):""; m.put("tzxkc", sx1); }
                 if (m.containsKey("xsjyy")) { String sx = parseString(qjRank.get("rank_xsjyy")); String sx1 = !"".equals(sx)?(sx + " / " + totalDistrict):""; m.put("xsjyy", sx1); }
             }
            }
           

		ModelAndView mvAndView ;
		if("schoolPlainAdmin".equals(roleCode) || "schoolAdmin".equals(roleCode)) {
			mvAndView = new ModelAndView("/scoreManage/printOrderSchoolAdmin");
		}else{
			mvAndView = new ModelAndView("/scoreManage/printOrderAdmin");
		}
		if(pagingResult.size()>0){
			for(int i=0;i<pagingResult.size();i++){
				switch ((String)pagingResult.get(i).get("Class_Id")) {
					case "01":
						pagingResult.get(i).put("Class_Id", "(1)班");
					break;
					case "02":
						pagingResult.get(i).put("Class_Id", "(2)班");
					break;
					case "03":
						pagingResult.get(i).put("Class_Id", "(3)班");
					break;
					case "04":
						pagingResult.get(i).put("Class_Id", "(4)班");
					break;
					case "05":
						pagingResult.get(i).put("Class_Id", "(5)班");
					break;
					case "06":
						pagingResult.get(i).put("Class_Id", "(6)班");
					break;
					case "07":
						pagingResult.get(i).put("Class_Id", "(7)班");
					break;
					case "08":
						pagingResult.get(i).put("Class_Id", "(8)班");
					break;
					case "09":
						pagingResult.get(i).put("Class_Id", "(9)班");
					break;
					case "10":
						pagingResult.get(i).put("Class_Id", "(10)班");
					break;
					default:
				}
			}
		}
		mvAndView.addObject("pagingResult",pagingResult);
		mvAndView.addObject("courseName",courseName);
		mvAndView.addObject("courseVal",courseValue);
		request.setAttribute("courseName", courseName);
		request.setAttribute("courseVal", courseValue);
		request.setAttribute("pagingResult", pagingResult);
		request.setAttribute("scoreHtml", scoreHtml);
		request.setAttribute("gradeTxt", gradeTxt);
		return mvAndView;
	}
	
	
	
}
