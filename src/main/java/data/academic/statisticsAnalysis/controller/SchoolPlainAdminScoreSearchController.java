
package data.academic.statisticsAnalysis.controller;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import data.academic.examNumberManagement.service.ExamNumberManageService;
import data.academic.statisticsAnalysis.service.DistrictSubjectInstructorService;
import data.academic.statisticsAnalysis.service.SchoolPlainAdminScoreSearchService;
import data.academic.statisticsAnalysis.service.ScoreSearchService;
import data.academic.teacherManagement.service.TeacherManagementService;
import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractBaseController;
import data.framework.support.ConfigContext;
import data.platform.authority.security.SecurityContext;
import data.platform.service.PlatformDataDictionaryService;
import data.platform.service.PlatformUserService;

/**
 * @Title: SubTeaScoreSearchController
 * @Description: 科任老师查询成绩控制层
 * @author zhaohuanhuan
 * @date 2016年9月6日 上午11:21:07
 */
@RestController
@RequestMapping("statisticsAnalysis/schoolPlainAdminScoreSearch")
public class SchoolPlainAdminScoreSearchController extends AbstractBaseController{

	@Override
	protected void init(ModelMap model, HttpServletRequest request) {
		
		
	}
	
	
	/**
	 * @Title: searchPaging
	 * @Description: 分页显示教导员查询的成绩
	 * @author zhaohuanhuan
	 * @date 2016年9月8日 
	 * @param data
	 * @param out
	 * @return void
	 * @throws Exception 
	 */
	/*@RequestMapping(params = "command=searchScorePagging")
	public void searchPaging(@RequestParam("data") String data, java.io.PrintWriter out) {
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		//schoolYear=2016-2017, schoolType=cz, term=xxq, examType=qm, course=yw, school=3071, isFast=true
//		boolean isFast = parseBoolean(requestMap.get("isFast"));
//		String examPaperName = "";
//		if (!isFast) {
//			examPaperName = trimString(requestMap.get("q"));
//		}
//		requestMap.put("examPaperName", examPaperName);
		int currentPage = parseInteger(requestMap.get("page"));
		int pageSize = parseInteger(requestMap.get("rows"));
		String sortField = trimString(requestMap.get("sidx"));

		if (sortField.trim().length() == 0) {
			sortField = "Total_Score";
		}
		String sort = trimString("desc");
		PagingResult<Map<String, Object>> pagingResult = schoolPlainAdminScoreSearchService.serachSlAvgPaging(requestMap, sortField, sort, currentPage,pageSize);
		List<Map<String, Object>> list = pagingResult.getRows();
		List<Map<String, Object>> paramList = new ArrayList<Map<String, Object>>();
		for(Map<String, Object> map : list){
			map.put("course",requestMap.get("course").toString());
			map.put("Avg_Score", String.format("%10.2f", parseFloat(map.get("Avg_Score").toString())));
			
			map.put("yl", String.format("%10.2f", parseFloat(map.get("yl").toString())*100) + "%");
			map.put("ll", String.format("%10.2f", parseFloat(map.get("ll").toString())*100) + "%");
			map.put("yll", String.format("%10.2f", parseFloat(map.get("yll").toString())*100) + "%");
			map.put("jgl", String.format("%10.2f", parseFloat(map.get("jgl").toString())*100) + "%");
			paramList.add(map);
		}
		
		
		PagingResult<Map<String, Object>> newPagingResult = new PagingResult<Map<String, Object>>(
				paramList, pagingResult.getPage(), pagingResult.getPageSize(),
		pagingResult.getRecords());
		out.print(getSerializer().formatObject(newPagingResult));
	}*/
	
	@RequestMapping(params = "command=searchScorePagging")
	public void searchPaging(@RequestParam("data") String data, java.io.PrintWriter out) throws Exception {
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		//根据登录名查询schoolCode
		String stuCode = trimString(requestMap.get("examNumberOrStuCode").toString());
		String examNumber = trimString(requestMap.get("examNumberOrStuCode").toString());
		String loginName=SecurityContext.getPrincipal().getUsername();
		String schoolCode = examNumberManageService.getSchoolCodeByLoginName(loginName);
		requestMap.put("schoolCode", schoolCode);
		requestMap.put("stuCode", stuCode);
		requestMap.put("examNumber", examNumber);
		int currentPage = parseInteger(requestMap.get("page"));
		int pageSize = parseInteger(requestMap.get("rows"));
		String sortField = trimString(requestMap.get("sidx"));

		if (sortField.trim().length() == 0) {
			sortField = "Class_Id,Total_Score";
		}
		String sort = trimString("desc");
		PagingResult<Map<String, Object>> pagingResult = schoolPlainAdminScoreSearchService.serachAllStudentsScorePaging(requestMap, sortField, sort, currentPage,pageSize);
		List<Map<String, Object>> list = pagingResult.getRows();
		
		PagingResult<Map<String, Object>> newPagingResult = new PagingResult<Map<String, Object>>(
				list, pagingResult.getPage(), pagingResult.getPageSize(),
		pagingResult.getRecords());
		out.print(getSerializer().formatObject(newPagingResult));
	}
	
	
	 /**
	 * @Title: getGradeAndClassByLoginName
	 * @Description: 根据登录名的到该用户的年级和班级
	 * @author zhaohuanhuan
	 * @date 2016年9月8日 
	 * @param data
	 * @param out
	 * @return void
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping( params = "command=getSchoolCodeAndSchoolName" )
	    public void getSchoolCodeAndSchoolName( @RequestParam( "data" ) String data, java.io.PrintWriter out) throws UnsupportedEncodingException
	    {
			List<Map<String,Object>> paramList = null;
			List<Map<String,Object>> gradeList = null;
			Map<String,Object> mapTotal = new HashMap<String,Object>();
			//根据登录名查询schoolCode
			String loginName = SecurityContext.getPrincipal().getUsername();
			String schoolCode = examNumberManageService.getSchoolCodeByLoginName(loginName);
			//根据登录名查询角色Code
			String roleCode = teacherManagementService.selectRoleCodeByLoginName(loginName);
			//判断是不是教研员角色(教研员分配了年级，可以查询历史成绩数据)
			if("instructor".equals(roleCode)){
				//根据登录名查询当前教研员所关联的年级
				gradeList = districtSubjectInstructorService.selectGradesByLoginName(loginName);
				mapTotal.put("gradeList", gradeList);
				out.print( getSerializer().formatMap(mapTotal));
				return;
			}
			//查询学校code和学校名称
			Map<String,Object> map = schoolPlainAdminScoreSearchService.findSchoolCodeAndASchoolName(schoolCode);
			Map<String,Object> paramMap = new HashMap<String,Object>();
			//超级管理员查询所有年级，普通管理员根据schoolSequence查询年级
				if(roleCode.equals(ConfigContext.getValue("framework.tmis.roleCode['qpRoleAdminCode']")) || roleCode.equals(ConfigContext.getValue("framework.tmis.roleCode['qpRoleQuAdminCode']"))){
					
				paramList = teacherManagementService.selectSchoolTypeList("nj");
			} else {
				//查询学校类型
				String schoolSequence = schoolPlainAdminScoreSearchService.findSchoolTypeBySchoolCode(schoolCode);
				if("0".equals(schoolSequence)){
					paramMap.put("schoolTypeCode","xx");
					paramMap.put("schoolTypeName","小学");
				}else if("1".equals(schoolSequence)){
					paramMap.put("schoolTypeCode","cz");
					paramMap.put("schoolTypeName","初中");
				}else if("2".equals(schoolSequence) || "3".equals(schoolSequence) || "4".equals(schoolSequence)  ){
					paramMap.put("schoolTypeCode","gz");
					paramMap.put("schoolTypeName","高中");
					if("3062".equals(schoolCode) ){
						paramMap.put("schoolTypeCode","cz");
						paramMap.put("schoolTypeName","初中");
					}
					if("3004".equals(schoolCode) ){
						paramMap.put("schoolTypeCode","wz");
						paramMap.put("schoolTypeName","完中");
					}
					
			   }else if("5".equals(schoolSequence)){
				    paramMap.put("schoolTypeCode","ygz");
					paramMap.put("schoolTypeName","一贯制");
			  }
				//根据学校类型查询年级
				paramList = dictionaryService.getGradesBySchoolType(paramMap.get("schoolTypeCode").toString());
			}
			
			mapTotal.put("map", map);
			mapTotal.put("paramList", paramList);
	        out.print( getSerializer().formatMap(mapTotal));
	    }
	
	/**
	 * @Title: getSchoolCodeAndSchoolName
	 * @Description: 教导处查询平均分
	 * @author xiahuajun
	 * @date 2016年9月18日 
	 * @param data
	 * @param out
	 * @return void
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping( params = "command=getAvgScore" )
    public void getAvgScore( @RequestParam( "data" ) String data, java.io.PrintWriter out) throws UnsupportedEncodingException
    {
		//根据登录名查询schoolCode
		String loginName=SecurityContext.getPrincipal().getUsername();
		String schoolCode = examNumberManageService.getSchoolCodeByLoginName(loginName);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("schoolCode", schoolCode);
		map.put("schoolYear", "2015-2016");
		map.put("term", "xxq");
		map.put("examType", "qm");
		//教导处查询平均分
		//Map<String,Object> map = schoolPlainAdminScoreSearchService.findSchoolCodeAndASchoolName(schoolCode);
		List<Map<String,Object>> paramList = schoolPlainAdminScoreSearchService.selectAvgScore(map);
		List<Map<String,Object>> paramList1 = new ArrayList<>();
		for(Map<String,Object> paramMap : paramList){
			if("11".equals(paramMap.get("Grade_Id").toString())){
				paramMap.put("Grade_Id", "一年级");
			}
			else if("12".equals(paramMap.get("Grade_Id").toString())){
				paramMap.put("Grade_Id", "二年级");
			}
			else if("13".equals(paramMap.get("Grade_Id").toString())){
				paramMap.put("Grade_Id", "三年级");
			}
			else if("14".equals(paramMap.get("Grade_Id").toString())){
				paramMap.put("Grade_Id", "四年级");
			}
			else if("15".equals(paramMap.get("Grade_Id").toString())){
				paramMap.put("Grade_Id", "五年级");
			}
			else if("16".equals(paramMap.get("Grade_Id").toString())){
				paramMap.put("Grade_Id", "六年级");
			}
			else if("17".equals(paramMap.get("Grade_Id").toString())){
				paramMap.put("Grade_Id", "七年级");
			}
			else if("18".equals(paramMap.get("Grade_Id").toString())){
				paramMap.put("Grade_Id", "八年级");
			}
			else if("19".equals(paramMap.get("Grade_Id").toString())){
				paramMap.put("Grade_Id", "九年级");
			}
			else if("31".equals(paramMap.get("Grade_Id").toString())){
				paramMap.put("Grade_Id", "十年级");
			}
			else if("32".equals(paramMap.get("Grade_Id").toString())){
				paramMap.put("Grade_Id", "十一年级");
			}
			else if("33".equals(paramMap.get("Grade_Id").toString())){
				paramMap.put("Grade_Id", "十二年级");
			}
			paramList1.add(paramMap);
		}
        out.print( getSerializer().formatList(paramList));
    }
	
	/**
	 * @Title: getIntervalstuCountforSchoolPlainAdmin
	 * @Description: 教导处查询分数段学生人数
	 * @author xiahuajun
	 * @date 2016年9月18日 
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=getIntervalstuCountforSchoolPlainAdmin")
    public void getIntervalstuCountforSchoolPlainAdmin( java.io.PrintWriter out )
    {
    	//String username = SecurityContext.getPrincipal().getUsername();
    	//根据登录名查询teacher_pk
		String teacher_pk = userService.selectTeacherPkByLoginName(SecurityContext.getPrincipal().getUsername());
		//查询当前用户的学校code(根据登录名)
		String schoolCode = userService.selectSchoolCodeByLoginName(SecurityContext.getPrincipal().getUsername());
		//查询科任老师的年级班级科目stuCount=[{Count=4, Total_Score=60以下 , Class_Id=01}, {Count=11, Total_Score=61-70 , Class_Id=01}, {Count=10, Total_Score=71-80 , Class_Id=01}
		//[{Grade_Name=初一, Course_Name=语文, Teacher_Name=朱苏苹, Teacher_Pk=9BD6C39D-20F4-48EF-85F5-6314EAE4B2FD, Course_Code=yw, Class_Name=01, Grade_Id=17, Course_Id=0B2D3060-C01B-4CEF-B427-B00CA6226826, Class_Id=02B7322F-1FC1-47A1-98A4-3EB508E10B21}]
		List<Map<String, Object>> list = userService.selectGradeClassCourseByTeacherPk(teacher_pk);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		//Integer startNum = 50;
		for(Map<String, Object> map : list){
			map.put("schoolCode", schoolCode);
			map.put("examType", "qm");
			map.put("term", "xxq");
			map.put("schoolYear", "2015-2016");
			List<Map<String, Object>> stuCount = schoolPlainAdminScoreSearchService.selectIntervalStuCountForSchoolPlainAdmin(map);
			paramMap.put("stuCount",stuCount);
		}
			
    	out.print(getSerializer().formatMap(paramMap));
    }
	
	/**
	 * @Title: exportExcel
	 * @Description: 教导员导出成绩查询excel数据
	 * @author xiahuajun
	 * @date 2016年11月5日 
	 * @param request
	 * @param response
	 * @throws UnsupportedEncodingException
	 * @return void
	 */
	@RequestMapping(params="command=exportExcel")
	public void exportExcel(HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException{
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
    	//根据登录名查询角色Code
        //String roleCode = teacherManagementService.selectRoleCodeByLoginName(loginName);
    	String index=trimString(requestMap.get("idx").toString());
    	//String columnIndex=trimString(requestMap.get("ci").toString());
    	String sortOrder=trimString(requestMap.get("so").toString());
    	//根据登录名查询schoolCode
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
		/*int currentPage = parseInteger(requestMap.get("page"));
		int pageSize = parseInteger(requestMap.get("rows"));
		String sortField = trimString(requestMap.get("sidx"));
		if (sortField.trim().length() == 0) {
			sortField = "Grade_Id,Class_Id";
		}
		String sort = trimString(requestMap.get("sord"));*/
		//教导员查询成绩导出excel
		List<Map<String, Object>>  paramList = schoolPlainAdminScoreSearchService.selectExportScoreDataForSchoolAdmin(requestMap);
				
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
	        titleList.add("总分");
	        //String[] titleAry=new String[]{"序号","考号","学籍号","姓名","学校","年级","班级","语文","数学","外语","物理","化学","总分"};
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
					Grade_Id = "十年级";
				}
				else if("32".equals(paramMap.get("Grade_Id").toString())){
					Grade_Id = "十一年级";
				}
				else if("33".equals(paramMap.get("Grade_Id").toString())){
					Grade_Id = "十二年级";
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
		        	
		        	HSSFCell cellTotalScore=row.createCell((short)(7+courseValList.size()));
		        	cellTotalScore.setCellStyle(style);
		        	cellTotalScore.setCellValue(parseString(paramMap.get("Total_Score")));
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
	 
	 @Autowired
	 private PlatformUserService userService;
	 @Autowired
	 private SchoolPlainAdminScoreSearchService schoolPlainAdminScoreSearchService;
	 @Autowired
	 private PlatformDataDictionaryService dictionaryService;
	 @Autowired
	 private TeacherManagementService teacherManagementService;
	 @Autowired
	 private ExamNumberManageService examNumberManageService;
	 @Autowired
	 private DistrictSubjectInstructorService districtSubjectInstructorService;
}
