package data.academic.schoolQualityAnalysis.controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.solr.common.util.Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.xml.bind.v2.TODO;

import data.academic.dataManage.service.SmallTitleService;
import data.academic.examNumberManagement.service.ExamNumberManageService;
import data.academic.schoolQualityAnalysis.service.SchoolScoreService;
import data.academic.studentManagement.service.StuManagementService;
import data.academic.teacherManagement.service.TeacherManagementService;
import data.framework.httpClient.RestResponse;
import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractBaseController;
import data.framework.support.ConfigContext;
import data.framework.utility.ArithmeticUtil;
import data.framework.utility.ExcelReadUtils;
import data.framework.utility.UploadHelper;
import data.platform.authority.security.SecurityContext;
@Controller
@RequestMapping("schoolQualityAnalysis/schoolSegment")
public class SchoolSegmentController extends AbstractBaseController{

	@Override
	protected void init(ModelMap model, HttpServletRequest request) {
	}
	@Autowired
	private SchoolScoreService schoolScoreService;
	/**
	 * 
	 * @Title: analyzePersonalSamllScore
	 * @Description: 个人单科成绩汇总
	 * @author chenteng
	 * @date 2017年8月3日 
	 * @return void
	 * @param data
	 * @param out
	 */
	@RequestMapping(params = "command=analyzeSegment")
	@ResponseBody
	public JSONObject analyzePersonalSamllScore(@RequestParam("data") String data){
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		ArrayList<String> courseList = new ArrayList<>();
		ArrayList<String> classCodeList = (ArrayList<String>) requestMap.get("classCode");
		ArrayList<String> classNameList = (ArrayList<String>) requestMap.get("className");
		LinkedHashSet<String> titleList = new LinkedHashSet<>();
		
		Integer chooseFunction = parseInteger(requestMap.get("chooseFunction"));
		JSONObject jo =new JSONObject();
		DecimalFormat format = new DecimalFormat("0.00%");
		DecimalFormat df = new DecimalFormat("######0.00");
		//————————————————————————————————————————————————————————————————————————————
		int allZf=0;
		HashMap<String,HashMap> courseScoreMap = new HashMap<>();
		List<HashMap<String,String>> courseExaminfoList =schoolScoreService.getCourseExaminfoList(requestMap);
		if(courseExaminfoList.size()<1||classCodeList.size()<1){
			jo.put("message", "faile");
			return jo;
		}
		for(HashMap<String,String> temp:courseExaminfoList){
			allZf+=parseInteger(temp.get("Exam_Zf"));
			courseScoreMap.put(temp.get("Course"), temp);
			courseList.add(temp.get("Course"));
		}
		requestMap.put("course",courseList);
		//————————————————————————————————————————————————————————————————————————————
		
		
		int gap=10;//分数段间隔 10分 页面好像没得选 先写死
		ArrayList<HashMap> inforamtionList = new ArrayList<>();
		requestMap.put("isSchoolSegment",true);
		if(chooseFunction==1){//单科
			requestMap.put("targetCourse", courseList.get(0));
		}else{//多科
			requestMap.put("isDistrict",true);
			
		}
		for(int i=0;i<classCodeList.size();i++){
			HashMap<String,Object> schoolMap = new HashMap<>();
			List<Map> tempList=new ArrayList<>();
			requestMap.put("targetClass", classCodeList.get(i));
			List<Double> scoreList = schoolScoreService.getScoreList(requestMap);
			if(scoreList.size()<1){
				continue;
			}
			schoolMap.put("classCode", classCodeList.get(i));//实考人数
			schoolMap.put("className", classNameList.get(i));//实考人数
			schoolMap.put("ksrs", scoreList.size());//实考人数
			int allScore=allZf;//查出这门考试有多少分
			int max=0;
			int min=0;
			int ljrs=0;//累计人数
			Double ljbfb=0.0;//累计百分比
			for(int j=allScore;j>0;j-=gap){
				max=j;
				if(j==allScore){
					min=j-10;
					j--;
				}else{
					min=j-9;
				}
				
				titleList.add(max+"-"+min);
				int totalNumber=0;
				for(Double temp:scoreList){
					if(temp>=min && temp<=max){
						totalNumber++;
					}
				}
				Map<String,String> scoreMap = new HashMap<>();
				scoreMap.put("rs", totalNumber+"");//人数
				scoreMap.put("bfb", format.format(parseDouble(totalNumber)/scoreList.size()));//百分比
				scoreMap.put("bfbNoChar", df.format(parseDouble(totalNumber)*100/scoreList.size()));//百分比
				ljrs+=totalNumber;
				scoreMap.put("ljrs", ljrs+"");//累计人数
				ljbfb=ArithmeticUtil.add(ljbfb,parseDouble(totalNumber)/scoreList.size());
				scoreMap.put("ljbfb", format.format(ljbfb));//累计百分比
				scoreMap.put("range",max+"-"+min);
				tempList.add(scoreMap);
			}
			schoolMap.put("tempList", tempList);
			inforamtionList.add(schoolMap);
		}
		//小计开始
		HashMap<String,Object> countMap = new HashMap<>();
		
		List<Map> tempList=new ArrayList<>();
		requestMap.put("isDistrict",true);
		requestMap.remove("isSchoolSegment");
		List<Double> scoreList = schoolScoreService.getScoreList(requestMap);
		countMap.put("ksrs", scoreList.size());//实考人数
		int allScore=allZf;//查出这门考试有多少分  先写死 100分
		int max=0;
		int min=0;
		int ljrs=0;//累计人数
		Double ljbfb=0.0;//累计百分比
		for(int j=allScore;j>0;j-=gap){
			max=j;
			if(j==allScore){
				min=j-10;
				j--;
			}else{
				min=j-9;
			}
			int totalNumber=0;
			for(Double temp:scoreList){
				if(temp>=min && temp<=max){
					totalNumber++;
				}
			}
			Map<String,String> scoreMap = new HashMap<>();
			scoreMap.put("rs", totalNumber+"");//人数
			scoreMap.put("bfb", format.format(parseDouble(totalNumber)/scoreList.size()));//百分比
			ljrs+=totalNumber;
			scoreMap.put("ljrs", ljrs+"");//累计人数
			ljbfb=ArithmeticUtil.add(ljbfb,parseDouble(totalNumber)/scoreList.size());
			scoreMap.put("ljbfb", format.format(ljbfb));//累计百分比
			tempList.add(scoreMap);
		}
		countMap.put("tempList", tempList);
		//小计结束
		jo.put("inforamtionList", inforamtionList);
		jo.put("titleList", titleList);
		jo.put("countMap", countMap);
		return jo;
	}
	@RequestMapping(params = "command=importExcel")
	@ResponseBody
	public void analyzePersonalSamllScoreImport(@RequestParam("data") String data,HttpServletResponse response) throws UnsupportedEncodingException{
		data = new String (data.getBytes("iso8859-1"),"UTF-8");
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		ArrayList<String> courseList = new ArrayList<>();
		ArrayList<String> classCodeList = (ArrayList<String>) requestMap.get("classCode");
		ArrayList<String> classNameList = (ArrayList<String>) requestMap.get("className");
		List<String> titleList = new ArrayList<>();
		
		Integer chooseFunction = parseInteger(requestMap.get("chooseFunction"));
		JSONObject jo =new JSONObject();
		DecimalFormat format = new DecimalFormat("0.00%");
		DecimalFormat df = new DecimalFormat("######0.00");
		//————————————————————————————————————————————————————————————————————————————
		int allZf=0;
		HashMap<String,HashMap> courseScoreMap = new HashMap<>();
		List<HashMap<String,String>> courseExaminfoList =schoolScoreService.getCourseExaminfoList(requestMap);
		if(courseExaminfoList.size()<1||classCodeList.size()<1){
			jo.put("message", "faile");
			//return jo;
		}
		for(HashMap<String,String> temp:courseExaminfoList){
			allZf+=parseInteger(temp.get("Exam_Zf"));
			courseScoreMap.put(temp.get("Course"), temp);
			courseList.add(temp.get("Course"));
		}
		requestMap.put("course",courseList);
		//————————————————————————————————————————————————————————————————————————————
		
		
		int gap=10;//分数段间隔 10分 页面好像没得选 先写死
		ArrayList<HashMap> inforamtionList = new ArrayList<>();
		requestMap.put("isSchoolSegment",true);
		if(chooseFunction==1){//单科
			requestMap.put("targetCourse", courseList.get(0));
		}else{//多科
			requestMap.put("isDistrict",true);
			
		}
		for(int i=0;i<classCodeList.size();i++){
			HashMap<String,Object> schoolMap = new HashMap<>();
			List<Map> tempList=new ArrayList<>();
			requestMap.put("targetClass", classCodeList.get(i));
			List<Double> scoreList = schoolScoreService.getScoreList(requestMap);
			if(scoreList.size()<1){
				continue;
			}
			schoolMap.put("classCode", classCodeList.get(i));//实考人数
			schoolMap.put("className", classNameList.get(i));//实考人数
			schoolMap.put("ksrs", scoreList.size());//实考人数
			int allScore=allZf;//查出这门考试有多少分
			int max=0;
			int min=0;
			int ljrs=0;//累计人数
			Double ljbfb=0.0;//累计百分比
			for(int j=allScore;j>0;j-=gap){
				max=j;
				if(j==allScore){
					min=j-10;
					j--;
				}else{
					min=j-9;
				}
				
				titleList.add(max+"-"+min);
				int totalNumber=0;
				for(Double temp:scoreList){
					if(temp>=min && temp<=max){
						totalNumber++;
					}
				}
				Map<String,String> scoreMap = new HashMap<>();
				scoreMap.put("rs", totalNumber+"");//人数
				scoreMap.put("bfb", format.format(parseDouble(totalNumber)/scoreList.size()));//百分比
				scoreMap.put("bfbNoChar", df.format(parseDouble(totalNumber)*100/scoreList.size()));//百分比
				ljrs+=totalNumber;
				scoreMap.put("ljrs", ljrs+"");//累计人数
				ljbfb=ArithmeticUtil.add(ljbfb,parseDouble(totalNumber)/scoreList.size());
				scoreMap.put("ljbfb", format.format(ljbfb));//累计百分比
				scoreMap.put("range",max+"-"+min);
				tempList.add(scoreMap);
			}
			schoolMap.put("tempList", tempList);
			inforamtionList.add(schoolMap);
		}
		//小计开始
		HashMap<String,Object> countMap = new HashMap<>();
		
		List<Map> tempList=new ArrayList<>();
		requestMap.put("isDistrict",true);
		requestMap.remove("isSchoolSegment");
		List<Double> scoreList = schoolScoreService.getScoreList(requestMap);
		countMap.put("ksrs", scoreList.size());//实考人数
		int allScore=allZf;//查出这门考试有多少分  先写死 100分
		int max=0;
		int min=0;
		int ljrs=0;//累计人数
		Double ljbfb=0.0;//累计百分比
		for(int j=allScore;j>0;j-=gap){
			max=j;
			if(j==allScore){
				min=j-10;
				j--;
			}else{
				min=j-9;
			}
			int totalNumber=0;
			for(Double temp:scoreList){
				if(temp>=min && temp<=max){
					totalNumber++;
				}
			}
			Map<String,String> scoreMap = new HashMap<>();
			scoreMap.put("rs", totalNumber+"");//人数
			scoreMap.put("bfb", format.format(parseDouble(totalNumber)/scoreList.size()));//百分比
			ljrs+=totalNumber;
			scoreMap.put("ljrs", ljrs+"");//累计人数
			ljbfb=ArithmeticUtil.add(ljbfb,parseDouble(totalNumber)/scoreList.size());
			scoreMap.put("ljbfb", format.format(ljbfb));//累计百分比
			tempList.add(scoreMap);
		}
		countMap.put("tempList", tempList);
		//小计结束
		jo.put("inforamtionList", inforamtionList);
		jo.put("titleList", titleList);
		jo.put("countMap", countMap);
		List<String> titleList2=new ArrayList<>();
		List<Map<String,Object>> list2=(List<Map<String, Object>>) inforamtionList.get(0).get("tempList");
		for(int m=0;m<list2.size();m++){
			titleList2.add(formatString(list2.get(m).get("range")));
		}
		List<String> title1=new ArrayList<>();
		List<String> title2=new ArrayList<>();
		List<String> head1=new ArrayList<>();
		List<String> head2=new ArrayList<>();
		title1.add("班级");
		title1.add("实考人数");
		title2.add("班级");
		title2.add("实考人数");
		for(int i=0;i<titleList2.size();i++){
			title1.add(titleList2.get(i));
			title1.add(titleList2.get(i));
			title1.add(titleList2.get(i));
			title1.add(titleList2.get(i));
			title2.add("人数");
			title2.add("百分比");
			title2.add("累计人数");
			title2.add("累计百分比");
		}
		jo.put("countMap", countMap);
		countMap.put("className","小计");
		inforamtionList.add(countMap);
		List<List<String>> dataList=new ArrayList<>();
		
		for(int i=0;i<inforamtionList.size();i++){
			List<String> dataMap=new ArrayList<>();
			String schoolName=formatString(inforamtionList.get(i).get("className"));
			String skrs=formatString(inforamtionList.get(i).get("ksrs"));
			dataMap.add(schoolName);
			dataMap.add(skrs);
			List<Map<String,Object>> list=(List<Map<String, Object>>) inforamtionList.get(i).get("tempList");
			for(int j=0;j<list.size();j++){
				Map<String,Object> ran=list.get(j);
				String rs=formatString(ran.get("rs"));
				String bfb=formatString(ran.get("bfb"));
				String ljrs1=formatString(ran.get("ljrs"));
				String ljbfb1=formatString(ran.get("ljbfb"));
				dataMap.add(rs);
				dataMap.add(bfb);
				dataMap.add(ljrs1);
				dataMap.add(ljbfb1);
			}
			
			dataList.add(dataMap);
		}
		String fileName="分数段分段.xls";
		String sheetName="分数段分段列表";
		//return jo;
		try {
            response.setContentType("application/x-download");
            //fileDisplay = URLEncoder.encode(fileDisplay, "UTF-8");
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("GBK"), "ISO-8859-1"));
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet();
           
            HSSFCellStyle titleStyle = workbook.createCellStyle();
            titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

            HSSFFont font = workbook.createFont();
            font.setFontName("宋体");
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            font.setFontHeightInPoints((short) 11);// 设置字体大小
            titleStyle.setFont(font);

            HSSFCellStyle style = workbook.createCellStyle();
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

            HSSFRow row1 = sheet.createRow((short) 0);
            row1.setHeight((short) 350);
            
            HSSFRow row2 = sheet.createRow((short) 1);
            row2.setHeight((short) 350);
            
            //设置页名
            workbook.setSheetName(0,sheetName);

            
            int a=2;
            int b=5;
            head1.add("0,1,0,0");
            head1.add("0,1,1,1");
            for(int t=0;t<titleList2.size();t++){
            	head1.add("0,0,"+a+","+b);
            	a+=4;
            	b=a+3;
            }
            
           
            
            
            for(int j1=0;j1<title1.size();j1++){
            	//设置列长
 	            sheet.setColumnWidth((short) j1, (short) 5000);
 	            //设置title
 	           HSSFCell cell = row1.createCell((short) j1);
               cell.setCellStyle(titleStyle);
               cell.setCellType(HSSFCell.CELL_TYPE_STRING);
               cell.setCellValue(title1.get(j1));
            }
            for(int j1=0;j1<title2.size();j1++){
            	//设置列长
 	            sheet.setColumnWidth((short) j1, (short) 5000);
 	            //设置title
 	           HSSFCell cell = row2.createCell((short) j1);
               cell.setCellStyle(titleStyle);
               cell.setCellType(HSSFCell.CELL_TYPE_STRING);
               cell.setCellValue(title2.get(j1));
            }
            // 写入实体数据,title和keys相对应，顺序一致，用于赋值
            for (int i = 0; i < dataList.size(); i++) {
                List paramMap = dataList.get(i);
                row1 = sheet.createRow((short) i + 2);
	            
	            
	            //把值写入表格
	            for(int k=0;k<title1.size();k++){
	            	HSSFCell keyCell = row1.createCell((short) k);
	            	keyCell.setCellStyle(style);
	            	keyCell.setCellValue(parseString(paramMap.get(k)));
	            }
            }
            //动态合并单元格
            for (int i = 0; i < head1.size(); i++) {
                String[] temp =head1.get(i).split(",");
                Integer startrow = Integer.parseInt(temp[0]);
                Integer overrow = Integer.parseInt(temp[1]);
                Integer startcol = Integer.parseInt(temp[2]);
                Integer overcol = Integer.parseInt(temp[3]);
                sheet.addMergedRegion(new CellRangeAddress(startrow, overrow,
                        startcol, overcol));
            }
            
           
            
            OutputStream out = response.getOutputStream();
            workbook.write(out);
            out.flush();
            out.close();
        } catch (Exception e) {
            System.out.println(e);
        }
	}
}