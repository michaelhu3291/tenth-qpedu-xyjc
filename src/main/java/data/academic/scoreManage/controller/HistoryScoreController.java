package data.academic.scoreManage.controller;

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
import data.academic.scoreManage.service.HistoryScoreService;
import data.academic.teacherManagement.service.TeacherManagementService;
import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractBaseController1;
import data.framework.support.ConfigContext;
import data.platform.authority.security.SecurityContext;

@RestController
@RequestMapping("scoreManage/historyScore")
public class HistoryScoreController extends AbstractBaseController1{

	@Autowired
	private HistoryScoreService historyScoreService;
	@Autowired
	private ExamNumberManageService examNumberManageService;
	@Autowired
	private TeacherManagementService teacherManagementService;
	
	@RequestMapping
	protected ModelAndView initialize(ModelMap model, HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException{
		String loginName = SecurityContext.getPrincipal().getUsername();
		//根据登录名查询角色Code
		String roleCode = teacherManagementService.selectRoleCodeByLoginName(loginName);
		if("schoolPlainAdmin".equals(roleCode) || "teacher".equals(roleCode) || "schoolAdmin".equals(roleCode)) {
			request.getRequestDispatcher("/scoreManage/teacherAndShoolPlainAdminHistoryScore.do").forward(request,response);
		}
		return new ModelAndView();
		
	}
	
	/**
	 * @Title: searchPaging
	 * @Description: 历史成绩分页查询
	 * @author xiahuajun
	 * @date 2016年10月28日 
	 * @param data
	 * @param out
	 * @throws Exception
	 * @return void
	 */
	@RequestMapping(params = "command=searchScorePagging")
	public void searchPaging(@RequestParam("data") String data, java.io.PrintWriter out) throws Exception {
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		
		//根据登录名查询schoolCode
		String loginName = SecurityContext.getPrincipal().getUsername();
		String schoolCode = examNumberManageService.getSchoolCodeByLoginName(loginName);
		String stuCode = trimString(requestMap.get("examNumberOrStuCode").toString());
		String examNumber = trimString(requestMap.get("examNumberOrStuCode").toString());
		//根据登录名查询角色Code
		String roleCode = teacherManagementService.selectRoleCodeByLoginName(loginName);
		//判断是不是教研员角色(教研员分配了年级，可以查询历史成绩数据)
		if("instructor".equals(roleCode)){
			schoolCode = null;
		}
		//判断是不是qpAdmin角色(该角色可以查询所有学校历史成绩数据)
		if(roleCode.equals(ConfigContext.getValue("framework.tmis.roleCode['qpRoleAdminCode']")) || roleCode.equals(ConfigContext.getValue("framework.tmis.roleCode['qpRoleQuAdminCode']"))){
			schoolCode = null;
		}
		requestMap.put("schoolCode", schoolCode);
		requestMap.put("stuCode", stuCode);
		requestMap.put("examNumber", examNumber);
		int currentPage = parseInteger(requestMap.get("page"));
		int pageSize = parseInteger(requestMap.get("rows"));
		String sortField = trimString(requestMap.get("sidx"));

		if (sortField.trim().length() == 0) {
			sortField = "School_Code,Grade_Id,Class_Id";
		}
		String sort = trimString("ASC");
		PagingResult<Map<String, Object>> pagingResult = historyScoreService.serachAllStudentsScorePaging(requestMap, sortField, sort, currentPage,pageSize);
		List<Map<String, Object>> list = pagingResult.getRows();
		
		
		PagingResult<Map<String, Object>> newPagingResult = new PagingResult<Map<String, Object>>(
				list, pagingResult.getPage(), pagingResult.getPageSize(),
		pagingResult.getRecords());
		out.print(getSerializer().formatObject(newPagingResult));
	}
	
	/**
	 * @Title: getclassesByGradeCode
	 * @Description: 选择年级查询班级
	 * @author xiahuajun
	 * @date 2016年10月29日 
	 * @param data
	 * @param out
	 * @throws Exception
	 * @return void
	 */
	@RequestMapping(params = "command=getclassesByGradeCode")
	public void getclassesByGradeCode(@RequestParam("data") String data, java.io.PrintWriter out) throws Exception {
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		//根据登录名查询schoolCode
		String loginName=SecurityContext.getPrincipal().getUsername();
		String schoolCode = examNumberManageService.getSchoolCodeByLoginName(loginName);
		requestMap.put("schoolCode", schoolCode);
		//选择年级查询班级
		List<Map<String,Object>> classList = historyScoreService.getclassesByGradeCode(requestMap);
		if(! classList.isEmpty()) {
			out.print(getSerializer().formatList(classList));
		} else {
			out.print("");
		}
		
	}
	
	/**
	 * @Title: searchScoreForTeacherAndSchoolPlainAdmin
	 * @Description: 老师和教导员历史成绩查询
	 * @author xiahuajun
	 * @date 2016年10月31日 
	 * @param data
	 * @param out
	 * @throws Exception
	 * @return void
	 */
	@RequestMapping(params = "command=searchScoreForTeacherAndSchoolPlainAdmin")
	public void searchScoreForTeacherAndSchoolPlainAdmin(@RequestParam("data") String data, java.io.PrintWriter out) throws Exception {
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		@SuppressWarnings("unchecked")
		List<String> list1 = (List<String>) requestMap.get("class");
		List<Map<String,String>> classList = new ArrayList<>();
		if(list1 != null && list1.size() > 0) {
			for(String str : list1) {
				Map<String,String> map = new HashMap<String,String>();
				String[] strArr = str.split(",");
				map.put("grade", strArr[0]);
				map.put("class", strArr[1]);
				classList.add(map);
			}
		}
    	//System.out.println("====index===="+index);
    	//System.out.println("====columnIndex===="+columnIndex);
    	//System.out.println("====sortOrder===="+sortOrder);
		//根据登录名查询schoolCode
		String loginName=SecurityContext.getPrincipal().getUsername();
		String schoolCode = examNumberManageService.getSchoolCodeByLoginName(loginName);
		String stuCode = trimString(requestMap.get("examNumberOrStuCode").toString());
		String examNumber = trimString(requestMap.get("examNumberOrStuCode").toString());
		requestMap.put("stuCode", stuCode);
		requestMap.put("examNumber", examNumber);
		requestMap.put("schoolCode", schoolCode);
		if(list1 != null && list1.size() > 0) {
			requestMap.put("classList", classList);
		}
		int currentPage = parseInteger(requestMap.get("page"));
		int pageSize = parseInteger(requestMap.get("rows"));
		String sortField = trimString(requestMap.get("sidx"));

		if (sortField.trim().length() == 0) {
			sortField = "School_Code,Grade_Id,Class_Id";
		}
		String sort = trimString("ASC");
		PagingResult<Map<String, Object>> pagingResult = historyScoreService.serachAllStudentsScorePaging(requestMap, sortField, sort, currentPage,pageSize);
		List<Map<String, Object>> list = pagingResult.getRows();
		PagingResult<Map<String, Object>> newPagingResult = new PagingResult<Map<String, Object>>(
				list, pagingResult.getPage(), pagingResult.getPageSize(),
		pagingResult.getRecords());
		out.print(getSerializer().formatObject(newPagingResult));
	}
	
	/**
	 * @Title: exportExcel
	 * @Description: 老师和教导员导出历史成绩excel
	 * @author xiahuajun
	 * @date 2016年11月6日 
	 * @param request
	 * @param response
	 * @throws UnsupportedEncodingException
	 * @return void
	 */
	@RequestMapping(params="command=exportExcel")
	public void exportExcel(HttpServletRequest request,HttpServletResponse response ) throws UnsupportedEncodingException{
	    String datas=request.getParameter("data");
    	Map<String,Object> requestMap = this.getSerializer().parseMap(datas) ;
    	
    	//根据登录名查询角色Code
        //String roleCode = teacherManagementService.selectRoleCodeByLoginName(loginName);
    	
    	//根据登录名查询schoolCode
			String loginName = SecurityContext.getPrincipal().getUsername();
			String schoolCode = examNumberManageService.getSchoolCodeByLoginName(loginName);
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
			
			@SuppressWarnings("unchecked")
			List<String> classList = (List<String>) requestMap.get("classArr");
			List<Map<String,Object>> list1 = new ArrayList<>();
			for(String str : classList) {
				Map<String,Object> map = new HashMap<String,Object>();
				String[] arr = str.split(",");
				map.put("grade", arr[0]);
				map.put("class", arr[1]);
				list1.add(map);
			}
			String index=trimString(requestMap.get("idx").toString());
	    	//String columnIndex=trimString(requestMap.get("ci").toString());
	    	String sortOrder=trimString(requestMap.get("so").toString());
			requestMap.put("schoolCode", schoolCode);
			requestMap.put("stuCode", stuCode);
			requestMap.put("examNumber", examNumber);
			requestMap.put("index", index);
			requestMap.put("sortOrder", sortOrder);
			requestMap.put("list1", list1);
			
			//老师和学校管理员查询历史成绩导出excel
			List<Map<String, Object>>  paramList = historyScoreService.selectExportHistoryScoreForTeacherAndAdmin(requestMap);
			
			
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
		        String[] titleAry=new String[]{"序号","考号","学籍号","学校","姓名","年级","班级","科目","成绩"};
		        HSSFRow row = sheet.createRow((short) 0);
		        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, (titleAry.length-1)));    
		        HSSFCell cellTiltle = row.createCell(0);  
		        //总汇信息
//    	            cellTiltle.setCellValue("学校总人数："+isTestCaseGeneration+"人,已分配考号"+
//    		                                              testCaseGeneration+"人,未分配考号"+
//    	            		                              notTestCaseGeneration+"人"+",沪籍学生0人,"); 
		        row.setHeight((short)350);
		        cellTiltle.setCellStyle(countStyle);
		        
		        cellTiltle.setCellValue(requestMap.get("scoreHtml").toString());
		        
		        row = sheet.createRow((short) 1);
		        
		        //设置title
		        for(int i=0;i<titleAry.length;i++)
		        {
		        	HSSFCell cell=row.createCell((short)i);
		        	cell.setCellStyle(titleStyle);
		        	cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		            cell.setCellValue(titleAry[i]);  
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
		        	sheet.setColumnWidth(3, 8000);
		        	cellThree.setCellStyle(style);
		        	cellThree.setCellValue(parseString(paramMap.get("School_Name"))); 
		        	
		        	HSSFCell cellFour=row.createCell((short)4);
		        	cellFour.setCellStyle(style);
		        	cellFour.setCellValue(parseString(paramMap.get("Name"))); 
		        	
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
		        	HSSFCell cellFive=row.createCell((short)5);
		        	cellFive.setCellStyle(style);

		        	cellFive.setCellValue(Grade_Id); 
		        	
		        	String Class_Id = "";
		        	if("01".equals(paramMap.get("Class_Id").toString())){
		        		Class_Id = "一班";
					}
					else if("02".equals(paramMap.get("Class_Id").toString())){
						Class_Id = "二班";
					}
					else if("03".equals(paramMap.get("Class_Id").toString())){
						Class_Id = "三班";
					}
					else if("04".equals(paramMap.get("Class_Id").toString())){
						Class_Id = "四班";
					}
					else if("05".equals(paramMap.get("Class_Id").toString())){
						Class_Id = "五班";
					}
					else if("06".equals(paramMap.get("Class_Id").toString())){
						Class_Id = "六班";
					}
					else if("07".equals(paramMap.get("Class_Id").toString())){
						Class_Id = "七班";
					}
					else if("08".equals(paramMap.get("Class_Id").toString())){
						Class_Id = "八班";
					}
					else if("09".equals(paramMap.get("Class_Id").toString())){
						Class_Id = "九班";
					}
					else if("10".equals(paramMap.get("Class_Id").toString())){
						Class_Id = "十班";
					}
					else if("11".equals(paramMap.get("Class_Id").toString())){
						Class_Id = "十一班";
					}
					else if("12".equals(paramMap.get("Class_Id").toString())){
						Class_Id = "十二班";
					}
					else if("13".equals(paramMap.get("Class_Id").toString())){
						Class_Id = "十三班";
					}
					else if("14".equals(paramMap.get("Class_Id").toString())){
						Class_Id = "十四班";
					}
					else if("15".equals(paramMap.get("Class_Id").toString())){
						Class_Id = "十五班";
					}
		        
		        	
		        	HSSFCell cellSix=row.createCell((short)6);
		        	cellSix.setCellStyle(style);
		        	cellSix.setCellValue(Class_Id);
		        	
		        	String course = "";
		        	if("yw".equals(paramMap.get("Course").toString())){
		        		course = "语文";
					}
					else if("sx".equals(paramMap.get("Course").toString())){
						course = "数学";
					}
					else if("zr".equals(paramMap.get("Course").toString())){
						course = "自然";
					}
					else if("yy".equals(paramMap.get("Course").toString())){
						course = "外语";
					}
					else if("dl".equals(paramMap.get("Course").toString())){
						course = "地理";
					}
					else if("hx".equals(paramMap.get("Course").toString())){
						course = "化学";
					}
					
					else if("ls".equals(paramMap.get("Course").toString())){
						course = "历史";
					}
					else if("ms".equals(paramMap.get("Course").toString())){
						course = "美术";
					}
					else if("njyy".equals(paramMap.get("Course").toString())){
						course = "牛津英语";
					}
					else if("sw".equals(paramMap.get("Course").toString())){
						course = "生物";
					}
					else if("sxzz".equals(paramMap.get("Course").toString())){
						course = "思想政治";
					}
					else if("ty".equals(paramMap.get("Course").toString())){
						course = "体育";
					}
					else if("tzxkc".equals(paramMap.get("Course").toString())){
						course = "拓展型课程";
					}
					else if("wl".equals(paramMap.get("Course").toString())){
						course = "物理";
					}
					else if("xsjyy".equals(paramMap.get("Course").toString())){
						course = "高中新世纪英语";
					}
					else if("xxkj".equals(paramMap.get("Course").toString())){
						course = "信息科技";
					}
					else if("yjxkc".equals(paramMap.get("Course").toString())){
						course = "研究型课程";
					}
					else if("yyue".equals(paramMap.get("Course").toString())){
						course = "音乐";
					}
		        	
		        	HSSFCell cellSeven=row.createCell((short)7);
		        	cellSeven.setCellStyle(style);
		        	cellSeven.setCellValue(course); 
		        	
		        	HSSFCell cellEight=row.createCell((short)8);
		        	cellEight.setCellStyle(style);
		        	cellEight.setCellValue(parseString(paramMap.get("Total_Score"))); 

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
	 * @Title: exportExcel
	 * @Description: 青浦超级管理员和教研员导出历史成绩excel
	 * @author xiahuajun
	 * @date 2016年11月7日 
	 * @param request
	 * @param response
	 * @throws UnsupportedEncodingException
	 * @return void
	 */
	@RequestMapping(params="command=exportExcelForqpAdminAndInstructor")
	public void exportExcelForqpAdminAndInstructor(HttpServletRequest request,HttpServletResponse response ) throws UnsupportedEncodingException{
	    String datas=request.getParameter("data");
    	Map<String,Object> requestMap = this.getSerializer().parseMap(datas) ;
    	
    	//根据登录名查询角色Code
        //String roleCode = teacherManagementService.selectRoleCodeByLoginName(loginName);
    	
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
			
			requestMap.put("stuCode", stuCode);
			requestMap.put("examNumber", examNumber);
			
			//教研员和青浦超级管理员查询历史成绩导出excel
			List<Map<String, Object>>  paramList = historyScoreService.selectExportHistoryScoreForqpAdminAndInstrutor(requestMap);
			
			
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
		        String[] titleAry=new String[]{"序号","考号","学籍号","学校","姓名","年级","班级","科目","成绩"};
		        HSSFRow row = sheet.createRow((short) 0);
		        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, (titleAry.length-1)));    
		        HSSFCell cellTiltle = row.createCell(0);  
		        //总汇信息
//    	            cellTiltle.setCellValue("学校总人数："+isTestCaseGeneration+"人,已分配考号"+
//    		                                              testCaseGeneration+"人,未分配考号"+
//    	            		                              notTestCaseGeneration+"人"+",沪籍学生0人,"); 
		        row.setHeight((short)350);
		        cellTiltle.setCellStyle(countStyle);
		        
		        cellTiltle.setCellValue(requestMap.get("scoreHtml").toString());
		        
		        row = sheet.createRow((short) 1);
		        
		        //设置title
		        for(int i=0;i<titleAry.length;i++)
		        {
		        	HSSFCell cell=row.createCell((short)i);
		        	cell.setCellStyle(titleStyle);
		        	cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		            cell.setCellValue(titleAry[i]);  
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
		        	sheet.setColumnWidth(3, 8000);
		        	cellThree.setCellStyle(style);
		        	cellThree.setCellValue(parseString(paramMap.get("School_Name"))); 
		        	
		        	HSSFCell cellFour=row.createCell((short)4);
		        	cellFour.setCellStyle(style);
		        	cellFour.setCellValue(parseString(paramMap.get("Name"))); 
		        	
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
		        	HSSFCell cellFive=row.createCell((short)5);
		        	cellFive.setCellStyle(style);

		        	cellFive.setCellValue(Grade_Id); 
		        	
		        	String Class_Id = "";
		        	if("01".equals(paramMap.get("Class_Id").toString())){
		        		Class_Id = "一班";
					}
					else if("02".equals(paramMap.get("Class_Id").toString())){
						Class_Id = "二班";
					}
					else if("03".equals(paramMap.get("Class_Id").toString())){
						Class_Id = "三班";
					}
					else if("04".equals(paramMap.get("Class_Id").toString())){
						Class_Id = "四班";
					}
					else if("05".equals(paramMap.get("Class_Id").toString())){
						Class_Id = "五班";
					}
					else if("06".equals(paramMap.get("Class_Id").toString())){
						Class_Id = "六班";
					}
					else if("07".equals(paramMap.get("Class_Id").toString())){
						Class_Id = "七班";
					}
					else if("08".equals(paramMap.get("Class_Id").toString())){
						Class_Id = "八班";
					}
					else if("09".equals(paramMap.get("Class_Id").toString())){
						Class_Id = "九班";
					}
					else if("10".equals(paramMap.get("Class_Id").toString())){
						Class_Id = "十班";
					}
					else if("11".equals(paramMap.get("Class_Id").toString())){
						Class_Id = "十一班";
					}
					else if("12".equals(paramMap.get("Class_Id").toString())){
						Class_Id = "十二班";
					}
					else if("13".equals(paramMap.get("Class_Id").toString())){
						Class_Id = "十三班";
					}
					else if("14".equals(paramMap.get("Class_Id").toString())){
						Class_Id = "十四班";
					}
					else if("15".equals(paramMap.get("Class_Id").toString())){
						Class_Id = "十五班";
					}
		        
		        	
		        	HSSFCell cellSix=row.createCell((short)6);
		        	cellSix.setCellStyle(style);
		        	cellSix.setCellValue(Class_Id);
		        	
		        	String course = "";
		        	if("yw".equals(paramMap.get("Course").toString())){
		        		course = "语文";
					}
					else if("sx".equals(paramMap.get("Course").toString())){
						course = "数学";
					}
					else if("zr".equals(paramMap.get("Course").toString())){
						course = "自然";
					}
					else if("yy".equals(paramMap.get("Course").toString())){
						course = "外语";
					}
					else if("dl".equals(paramMap.get("Course").toString())){
						course = "地理";
					}
					else if("hx".equals(paramMap.get("Course").toString())){
						course = "化学";
					}
					
					else if("ls".equals(paramMap.get("Course").toString())){
						course = "历史";
					}
					else if("ms".equals(paramMap.get("Course").toString())){
						course = "美术";
					}
					else if("njyy".equals(paramMap.get("Course").toString())){
						course = "牛津英语";
					}
					else if("sw".equals(paramMap.get("Course").toString())){
						course = "生物";
					}
					else if("sxzz".equals(paramMap.get("Course").toString())){
						course = "思想政治";
					}
					else if("ty".equals(paramMap.get("Course").toString())){
						course = "体育";
					}
					else if("tzxkc".equals(paramMap.get("Course").toString())){
						course = "拓展型课程";
					}
					else if("wl".equals(paramMap.get("Course").toString())){
						course = "物理";
					}
					else if("xsjyy".equals(paramMap.get("Course").toString())){
						course = "高中新世纪英语";
					}
					else if("xxkj".equals(paramMap.get("Course").toString())){
						course = "信息科技";
					}
					else if("yjxkc".equals(paramMap.get("Course").toString())){
						course = "研究型课程";
					}
					else if("yyue".equals(paramMap.get("Course").toString())){
						course = "音乐";
					}
		        	
		        	HSSFCell cellSeven=row.createCell((short)7);
		        	cellSeven.setCellStyle(style);
		        	cellSeven.setCellValue(course); 
		        	
		        	HSSFCell cellEight=row.createCell((short)8);
		        	cellEight.setCellStyle(style);
		        	cellEight.setCellValue(parseString(paramMap.get("Total_Score"))); 

		        }
		        OutputStream out=response.getOutputStream();
				workbook.write(out);
		        out.flush();
		        out.close();
			}catch(Exception e){
				System.out.println(e);
			}
	}

	
	
	
}
