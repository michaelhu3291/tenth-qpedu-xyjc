
package data.academic.examInfo.controller;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import data.academic.examInfo.service.MarkingArrangementService;
import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractBaseController;
 

/**
 * @Title: MarkingArrangementInfoController
 * @Description: TODO
 * @author zhaohuanhuan
 * @date 2016年10月26日 上午10:22:31
 */
@RestController
@RequestMapping("examInfo/markingArrangementInfo")
public class MarkingArrangementInfoController extends AbstractBaseController{
	String examCode,course,examName,courseName,
	          isExistArrangement,existArrangement,notExistArrangement="";
	List<String> schoolCodeList=new ArrayList<>();
	@Override
	protected void init(ModelMap model, HttpServletRequest request) {
		examCode=request.getParameter("examCode");
		course= request.getParameter("course");
		examName= request.getParameter("examName");
		isExistArrangement= request.getParameter("isExistArrangement");
		existArrangement= request.getParameter("existArrangement");
		notExistArrangement= request.getParameter("notExistArrangement");
		courseName= request.getParameter("courseName");
	}

	
	/**
	 * @Title: schoolArrangementPading
	 * @Description:  根据考试编号和科目分页显示学校相关阅卷人信息
	 * @author zhaohuanhuan
	 * @date 2016年11月17日 
	 * @param data
	 * @param out
	 * @param request
	 * @return void
	 */
	@RequestMapping(params = "command=schoolArrangementPading")
	public void schoolArrangementPading(@RequestParam("data") String data,
			java.io.PrintWriter out,HttpServletRequest request) {
		
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		int currentPage = parseInteger(requestMap.get("page"));
		int pageSize = parseInteger(requestMap.get("rows"));
		String sortField = trimString(requestMap.get("sidx"));
		String sort = trimString("asc");
		requestMap.put("examCode",examCode);
		requestMap.put("course",course);	
		PagingResult<Map<String, Object>> pagingResult =null;
		 if("allExistArrangement".equals(isExistArrangement) || 
			"allNotExistArrangement".equals(isExistArrangement) || "allArrangement".equals(isExistArrangement) ){
			  pagingResult = markingArrangementService
						                .getSchoolArrangementByExamCodeAndCourse(requestMap, sortField, sort, currentPage,
								         pageSize);
				
	    	}
	    	//未安排阅卷人的所有信息
	        if("notExistArrangement".equals(notExistArrangement)){
	        	 pagingResult = markingArrangementService
			                .getSchoolNotExistArrangementByExamCodePading(requestMap, sortField, sort, currentPage,
					         pageSize);
	    	}
	        if("existArrangement".equals(existArrangement)){
	        	pagingResult=markingArrangementService.getExistArrangementPading(requestMap, sortField, sort, currentPage,
				         pageSize);
	        }
		List<Map<String, Object>> list = pagingResult.getRows();
		List<Map<String, Object>> schoolList=new ArrayList<>();
		List<Map<String, Object>> teacherList=new ArrayList<>();
		List<Map<String, Object>> newList=new ArrayList<>();
		for (Map<String, Object> map : list) {
			schoolList=markingArrangementService.getSchoolShortNameAndBrevityCode(map);
			String teaName="";
			 StringBuffer teacherNames=new StringBuffer();
			if(schoolList.size()>0){
				for (Map<String, Object> schoolMap : schoolList) {
					int teacherNum=1;
					StringBuffer teachers=new StringBuffer();
					map.put("School_Short_Name", schoolMap.get("School_Short_Name"));
					map.put("Brevity_Code", schoolMap.get("Brevity_Code"));
					map.put("schoolCode",schoolMap.get("School_Code"));
					map.put("examCode",examCode);
					map.put("course",course);	
					//得到选中的教师集合
					teacherList=markingArrangementService.getSelectedTeacher(map);
					if(teacherList.size()>0){
						for (Map<String, Object> teacherMap : teacherList) {
							teaName=parseString(teacherMap.get("Teacher_Name"));
							if(1<teacherNum  && teacherNum<=teacherList.size()){
								teachers.append(",");
							}
							teachers.append(teaName);
							teacherNum++;
						}
						teacherNames.append(teachers.toString());
				}
					map.put("Teacher_Name", teacherNames);
					newList.add(map);
			}
				
			}
		}
		PagingResult<Map<String, Object>> newPagingResult = new PagingResult<Map<String, Object>>(
				newList, pagingResult.getPage(), pagingResult.getPageSize(),
				pagingResult.getRecords());
		        out.print(getSerializer().formatObject(newPagingResult));
	}
	
	
	
 
	/**
	 * @Title: exportArrangementExcel
	 * @Description: 导出阅卷人
	 * @author zhaohuanhuan
	 * @date 2016年11月24日 
	 * @param request
	 * @param response
	 * @throws UnsupportedEncodingException
	 * @return void
	 */
	@RequestMapping(params="command=exportArrangementExcel")
	public void exportArrangementExcel(HttpServletRequest request,HttpServletResponse response ) throws UnsupportedEncodingException{
	    String datas=request.getParameter("data");
    	Map<String,Object> dataMap = this.getSerializer().parseMap(datas) ;
    	 dataMap.put("examCode",examCode);
    	 dataMap.put("course",course);
         //得到某次考试下面的所有学校
    	List<Map<String,Object>> list =new ArrayList<Map<String,Object>>();
    	List<Map<String,Object>> notExistArrangementList=new ArrayList<Map<String,Object>>();
    	List<Map<String,Object>> existArrangementList=new ArrayList<Map<String,Object>>();
    	
        if("allExistArrangement".equals(isExistArrangement) || "allNotExistArrangement".equals(isExistArrangement) ){
        	list = markingArrangementService.getSchoolArrangementByExamCode(dataMap);
    	}
    	//未安排阅卷人的所有信息
        if("notExistArrangement".equals(notExistArrangement)){
        	notExistArrangementList = markingArrangementService.getSchoolNotExistArrangementByExamCode(dataMap);
    	}
        if("existArrangement".equals(existArrangement)){
        	existArrangementList=markingArrangementService.getSchoolShortName(dataMap);
        }
        if("allArrangement".equals(isExistArrangement)){
        	notExistArrangementList = markingArrangementService.getSchoolNotExistArrangementByExamCode(dataMap);
        	existArrangementList=markingArrangementService.getSchoolShortName(dataMap);
        }
    	List<Map<String, Object>> schoolList=new ArrayList<>();
		List<Map<String, Object>> teacherList=new ArrayList<>();
    	List<Map<String, Object>> newList=new ArrayList<>();
    	List<Map<String, Object>> existList=new ArrayList<>();
    	List<Map<String, Object>> notExistList=new ArrayList<>();
    	//得到所有考试相关学校教师集合
    	if(list.size()>0){
        	for (Map<String, Object> map : list) {
        		//得到某学校的简称和简码
    			schoolList=markingArrangementService.getSchoolShortNameAndBrevityCode(map);
    			String teaName="";
    			 StringBuffer teacherNames=new StringBuffer();
    			if(schoolList.size()>0){
    				for (Map<String, Object> schoolMap : schoolList) {
    					int teacherNum=1;
    					StringBuffer teachers=new StringBuffer();
    					map.put("School_Short_Name", schoolMap.get("School_Short_Name"));
    					map.put("Brevity_Code", schoolMap.get("Brevity_Code"));
    					map.put("schoolCode",schoolMap.get("School_Code"));
    					map.put("examCode",examCode);
    					map.put("course",course);	
    					//得到选中的教师集合
    					teacherList=markingArrangementService.getSelectedTeacher(map);
    					Integer teacherSum=teacherList.size();
    					if(teacherList.size()>0){
    						for (Map<String, Object> teacherMap : teacherList) {
    							teaName=parseString(teacherMap.get("Teacher_Name"));
    							if(1<teacherNum  && teacherNum<=teacherList.size()){
    								teachers.append(",");
    							}
    							teachers.append(teaName);
    							teacherNum++;
    						}
    						teacherNames.append(teachers.toString());
    				}
    					map.put("Teacher_Name", teacherNames);
    					map.put("teacherSum", teacherSum);
    					newList.add(map);
    			}
    		 }		
        	}
    	}
    	
    	//得到所有存在阅卷人的学校信息
    	if(existArrangementList.size()>0){
			for (Map<String, Object> map : existArrangementList) {
				//得到某学校的简称和简码
    			schoolList=markingArrangementService.getSchoolShortNameAndBrevityCode(map);
				String teaName="";
				StringBuffer teacherNames=new StringBuffer();
				 if(schoolList.size()>0){
	    				for (Map<String, Object> schoolMap : schoolList) {
	    					int teacherNum=1;
	    					StringBuffer teachers=new StringBuffer();
	    					map.put("School_Short_Name", schoolMap.get("School_Short_Name"));
	    					map.put("Brevity_Code", schoolMap.get("Brevity_Code"));
	    					map.put("schoolCode",schoolMap.get("School_Code"));
	    					map.put("examCode",examCode);
	    					map.put("course",course);	
	    					//得到选中的教师集合
	    					teacherList=markingArrangementService.getSelectedTeacher(map);
	    					Integer teacherSum=teacherList.size();
	    					if(teacherList.size()>0){
	    						for (Map<String, Object> teacherMap : teacherList) {
	    							teaName=parseString(teacherMap.get("Teacher_Name"));
	    							if(1<teacherNum  && teacherNum<=teacherList.size()){
	    								teachers.append(",");
	    							}
	    							teachers.append(teaName);
	    							teacherNum++;
	    						}
	    						teacherNames.append(teachers.toString());
	    				}
	    					map.put("Teacher_Name", teacherNames);
	    					map.put("teacherSum", teacherSum);
	    					existList.add(map);
	    			}
	    		 }	
	      }	
    	}
    	//得到未安排阅卷人的学校信息
    	if(notExistArrangementList.size()>0){
			for (Map<String, Object> map : notExistArrangementList) {
				//得到某学校的简称和简码
	    			schoolList=markingArrangementService.getSchoolShortNameAndBrevityCode(map);
					String teaName="";
					StringBuffer teacherNames=new StringBuffer();
					 if(schoolList.size()>0){
		    				for (Map<String, Object> schoolMap : schoolList) {
		    					int teacherNum=1;
		    					StringBuffer teachers=new StringBuffer();
		    					map.put("School_Short_Name", schoolMap.get("School_Short_Name"));
		    					map.put("Brevity_Code", schoolMap.get("Brevity_Code"));
		    					map.put("schoolCode",schoolMap.get("School_Code"));
		    					map.put("examCode",examCode);
		    					map.put("course",course);	
		    					//得到选中的教师集合
		    					teacherList=markingArrangementService.getSelectedTeacher(map);
		    					if(teacherList.size()>0){
		    						for (Map<String, Object> teacherMap : teacherList) {
		    							teaName=parseString(teacherMap.get("Teacher_Name"));
		    							if(1<teacherNum  && teacherNum<=teacherList.size()){
		    								teachers.append(",");
		    							}
		    							teachers.append(teaName);
		    							teacherNum++;
		    						}
		    						teacherNames.append(teachers.toString());
		    				}
		    					map.put("Teacher_Name", teacherNames);
		    					notExistList.add(map);
		    			}
		    		 }	
	        }	
    	}
    	StringBuffer sb=new StringBuffer();
    	sb.append(examName)
    	.append(courseName)
    	.append("阅卷人信息.xls");
    	String fileDisplay=sb.toString();
    	try{
			response.setContentType("application/x-download");
			fileDisplay = URLEncoder.encode(fileDisplay, "UTF-8");
	        response.addHeader("Content-Disposition", "attachment;filename="+ fileDisplay);
	     //创建一个webbook，对应一个Excel文件  
	        HSSFWorkbook workbook = new HSSFWorkbook();
	        //在webbook中添加一个sheet,对应Excel文件中的sheet  
	        HSSFSheet sheet = workbook.createSheet("1");
	        
	        
	        sheet.setColumnWidth((short)0,(short)2000);  
	        sheet.setColumnWidth((short)1,(short)6000);  
	        sheet.setColumnWidth((short)2,(short)6000);  
	        sheet.setColumnWidth((short)3,(short)3000);
	        
	      //，创建单元格，并设置值表头 设置表头居中  
	        HSSFCellStyle titleStyle = workbook.createCellStyle();  
	        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式  
	        
	        HSSFFont
	        font = workbook.createFont();    
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
	        HSSFRow row = sheet.createRow((short) 0);
	        row.setHeight((short)350);
	       
	        //表头
	        String[] titleAry=new String[]{};
	        if("allExistArrangement".equals(isExistArrangement)){
	        	
	        	//在sheet中添加表头第0行
		        workbook.setSheetName(0,"已安排阅卷人学校详情");
	        	titleAry=new String[]{"序号","学校","阅卷人","人数"};
	        	  //设置title
		        for(int i=0;i<titleAry.length;i++)
		        {
		        	HSSFCell cell=row.createCell((short)i);
		        	cell.setCellStyle(titleStyle);
		        	cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		            cell.setCellValue(titleAry[i]);  
		        }
		      //写入实体数据
		        Integer sum=0;
		        for(int i=0;i<newList.size();i++)
		        {
		        	Map<String,Object> paramMap =newList.get(i);
		        	row=sheet.createRow((short) i + 1);
		        	HSSFCell cell=row.createCell((short)0);
		        	cell.setCellStyle(style);
		        	cell.setCellValue((i+1)); 
		          	HSSFCell cellOne=row.createCell((short)1);
		        	cellOne.setCellStyle(style);
		        	cellOne.setCellValue(parseString(paramMap.get("School_Short_Name"))); 
		        	
		        	HSSFCell cellTow=row.createCell((short)2);
		        	cellTow.setCellStyle(style);
		        	cellTow.setCellValue(parseString(paramMap.get("Teacher_Name"))); 
		        	
		        	HSSFCell cellThree=row.createCell((short)3);
		        	cellThree.setCellStyle(style);
		        	cellThree.setCellValue(parseInteger(paramMap.get("teacherSum"))); 
		        	sum=sum+parseInteger(paramMap.get("teacherSum"));
		        }
		        row=sheet.createRow((short)(sheet.getLastRowNum()+1)); //在现有行号后追加数据   
		        HSSFCellStyle rowStyle = workbook.createCellStyle();  
		        rowStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT); 
		        rowStyle.setFont(font);
		     	HSSFCell rowCellOne=row.createCell((short)2);
		     	rowCellOne.setCellStyle(rowStyle);
		     	rowCellOne.setCellValue("总人数"); //设置第一个（从0开始）单元格的数据  
		    	rowStyle = workbook.createCellStyle();  
		        rowStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); 
		    	HSSFCell rowCellTwo=row.createCell((short)3);
		    	rowCellTwo.setCellStyle(rowStyle);
		    	rowCellTwo.setCellValue(sum); //设置第二个（从0开始）单元格的数据
	        }
	    	if("allNotExistArrangement".equals(isExistArrangement)){
	    		titleAry=new String[]{"序号","学校"};
	    		  for(int i=0;i<titleAry.length;i++)
	  	        {
	  	        	HSSFCell cell=row.createCell((short)i);
	  	        	cell.setCellStyle(titleStyle);
	  	        	cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	  	            cell.setCellValue(titleAry[i]);  
	  	        }
	  	        //写入实体数据
	  		        for(int i=0;i<newList.size();i++)
	  		        {
	  		        	Map<String,Object> paramMap =newList.get(i);
	  		        	row=sheet.createRow((short) i + 1);
	  		        	HSSFCell cell=row.createCell((short)0);
	  		        	cell.setCellStyle(style);
	  		        	cell.setCellValue((i+1)); 
	  		          	HSSFCell cellOne=row.createCell((short)1);
	  		        	cellOne.setCellStyle(style);
	  		        	cellOne.setCellValue(parseString(paramMap.get("School_Short_Name"))); 
	  		        }
	    	}
	    	//某次考试下某一科目的所有阅卷人
	    	if("allArrangement".equals(isExistArrangement)){
	    		workbook.setSheetName(0,"已安排阅卷人学校详情");
	    		titleAry=new String[]{"序号","学校","阅卷人","人数"};
	        	  //设置title
		        for(int i=0;i<titleAry.length;i++)
		        {
		        	HSSFCell cell=row.createCell((short)i);
		        	cell.setCellStyle(titleStyle);
		        	cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		            cell.setCellValue(titleAry[i]);  
		        }
		      //写入实体数据
		        Integer sum=0;
		        for(int i=0;i<existList.size();i++)
		        {
		        	Map<String,Object> paramMap =existList.get(i);
		        	row=sheet.createRow((short) i + 1);
		        	HSSFCell cell=row.createCell((short)0);
		        	cell.setCellStyle(style);
		        	cell.setCellValue((i+1)); 
		          	HSSFCell cellOne=row.createCell((short)1);
		        	cellOne.setCellStyle(style);
		        	cellOne.setCellValue(parseString(paramMap.get("School_Short_Name"))); 
		        	
		        	HSSFCell cellTow=row.createCell((short)2);
		        	cellTow.setCellStyle(style);
		        	cellTow.setCellValue(parseString(paramMap.get("Teacher_Name"))); 
		        	
		        	HSSFCell cellThree=row.createCell((short)3);
		        	cellThree.setCellStyle(style);
		        	cellThree.setCellValue(parseInteger(paramMap.get("teacherSum"))); 
		        	
		        	sum=sum+parseInteger(paramMap.get("teacherSum"));
		        }
		        row=sheet.createRow((short)(sheet.getLastRowNum()+1)); //在现有行号后追加数据   
		        HSSFCellStyle rowStyle = workbook.createCellStyle();  
		        rowStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT); 
		        rowStyle.setFont(font);
		     	HSSFCell rowCellOne=row.createCell((short)2);
		     	rowCellOne.setCellStyle(rowStyle);
		     	rowCellOne.setCellValue("总人数"); //设置第一个（从0开始）单元格的数据  
		    	rowStyle = workbook.createCellStyle();  
		        rowStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); 
		    	HSSFCell rowCellTwo=row.createCell((short)3);
		    	rowCellTwo.setCellStyle(rowStyle);
		    	rowCellTwo.setCellValue(sum); //设置第二个（从0开始）单元格的数据
		       
		        
		        HSSFSheet sheet2 = workbook.createSheet("2");
		        HSSFRow row2 = sheet2.createRow((short) 0);
		        row2.setHeight((short)350);
		        String[] titleAry2=new String[]{};
		        sheet2.setColumnWidth((short)0,(short)2000);  
		        sheet2.setColumnWidth((short)1,(short)2000);  
		        workbook.setSheetName(1,"未安排阅卷人学校详情");
		        titleAry2=new String[]{"序号","学校"};
	    		  for(int i=0;i<titleAry2.length;i++)
	  	        {
	  	        	HSSFCell cell=row2.createCell((short)i);
	  	        	cell.setCellStyle(titleStyle);
	  	        	cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	  	            cell.setCellValue(titleAry2[i]);  
	  	        }
	  	        //写入实体数据
	  		        for(int i=0;i<notExistList.size();i++)
	  		        {
	  		        	Map<String,Object> paramMap =notExistList.get(i);
	  		        	row2=sheet2.createRow((short) i + 1);
	  		        	HSSFCell cell=row2.createCell((short)0);
	  		        	cell.setCellStyle(style);
	  		        	cell.setCellValue((i+1)); 
	  		          	HSSFCell cellOne=row2.createCell((short)1);
	  		        	cellOne.setCellStyle(style);
	  		        	cellOne.setCellValue(parseString(paramMap.get("School_Short_Name"))); 
	  		        }
	    	
	    	}
	    	//所有存在阅卷人的学校
	    	if("existArrangement".equals(existArrangement)){
	    		titleAry=new String[]{"序号","学校","阅卷人","人数"};
	    		 workbook.setSheetName(0,"已安排阅卷人学校详情");
	        	  //设置title
		        for(int i=0;i<titleAry.length;i++)
		        {
		        	HSSFCell cell=row.createCell((short)i);
		        	cell.setCellStyle(titleStyle);
		        	cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		            cell.setCellValue(titleAry[i]);  
		        }
		      //写入实体数据
		        Integer sum=0;
		        for(int i=0;i<existList.size();i++)
		        {
		        	Map<String,Object> paramMap =existList.get(i);
		        	row=sheet.createRow((short) i + 1);
		        	HSSFCell cell=row.createCell((short)0);
		        	cell.setCellStyle(style);
		        	cell.setCellValue((i+1)); 
		          	HSSFCell cellOne=row.createCell((short)1);
		        	cellOne.setCellStyle(style);
		        	cellOne.setCellValue(parseString(paramMap.get("School_Short_Name"))); 
		        	
		        	HSSFCell cellTow=row.createCell((short)2);
		        	cellTow.setCellStyle(style);
		        	cellTow.setCellValue(parseString(paramMap.get("Teacher_Name"))); 
		        	
		        	HSSFCell cellThree=row.createCell((short)3);
		        	cellThree.setCellStyle(style);
		        	cellThree.setCellValue(parseInteger(paramMap.get("teacherSum"))); 
		        	sum=sum+parseInteger(paramMap.get("teacherSum"));
		        }
		        row=sheet.createRow((short)(sheet.getLastRowNum()+1)); //在现有行号后追加数据   
		        HSSFCellStyle rowStyle = workbook.createCellStyle();  
		        rowStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT); 
		        rowStyle.setFont(font);
		     	HSSFCell rowCellOne=row.createCell((short)2);
		     	rowCellOne.setCellStyle(rowStyle);
		     	rowCellOne.setCellValue("总人数"); //设置第一个（从0开始）单元格的数据  
		    	rowStyle = workbook.createCellStyle();  
		        rowStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); 
		    	HSSFCell rowCellTwo=row.createCell((short)3);
		    	rowCellTwo.setCellStyle(rowStyle);
		    	rowCellTwo.setCellValue(sum); //设置第二个（从0开始）单元格的数据
		       
	    	}
	    	//所有未安排阅卷人的学校
	    	if("notExistArrangement".equals(notExistArrangement)){
	    		titleAry=new String[]{"序号","学校"};
	    		  workbook.setSheetName(0,"未安排阅卷人学校详情");
	    		  for(int i=0;i<titleAry.length;i++)
	  	        {
	  	        	HSSFCell cell=row.createCell((short)i);
	  	        	cell.setCellStyle(titleStyle);
	  	        	cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	  	            cell.setCellValue(titleAry[i]);  
	  	        }
	  	        //写入实体数据
	  		        for(int i=0;i<notExistList.size();i++)
	  		        {
	  		        	Map<String,Object> paramMap =notExistList.get(i);
	  		        	row=sheet.createRow((short) i + 1);
	  		        	HSSFCell cell=row.createCell((short)0);
	  		        	cell.setCellStyle(style);
	  		        	cell.setCellValue((i+1)); 
	  		          	HSSFCell cellOne=row.createCell((short)1);
	  		        	cellOne.setCellStyle(style);
	  		        	cellOne.setCellValue(parseString(paramMap.get("School_Short_Name"))); 
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
	private MarkingArrangementService markingArrangementService;
}
