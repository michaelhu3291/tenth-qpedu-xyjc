package data.academic.qualityAnalysis.controller;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import data.academic.dataManage.service.SmallTitleService;
import data.academic.util.ExportUtil;
import data.framework.support.AbstractBaseController;
import data.framework.utility.ArithmeticUtil;
@RestController
@RequestMapping("qualityAnalysis/totalScoreSegment")
public class TotalScoreSegmentController extends AbstractBaseController{

	@Override
	protected void init(ModelMap model, HttpServletRequest request) {
		// TODO Auto-generated method stub
		
	}
	@Autowired
	private SmallTitleService smallTitleService;
	/**
	 * 
	 * @Title: analyzePersonalSamllScore
	 * @Description: 个人单科成绩汇总,获取单科百分点成绩集合索引修改
	 * @author chenteng,zhaohuanhuan
	 * @date 2017年8月3日 ,2017.11.24
	 * @return void
	 * @param data
	 * @param out
	 */
	@RequestMapping(params = "command=analyzeSegment")
	@ResponseBody
	public JSONObject analyzePersonalSamllScore(@RequestParam("data") String data){
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		ArrayList<String> courseList = new ArrayList<>();
		HashMap<String,String> staticMap = new HashMap();
		//TODO 改成enumMap工具类
		staticMap.put("yw","语文");staticMap.put("sx","数学");staticMap.put("yy","外语");staticMap.put("wl","物理");staticMap.put("hx","化学");staticMap.put("ty","体育");staticMap.put("sxzz","思想政治");
		staticMap.put("ls","历史");staticMap.put("dl","地理");staticMap.put("kx","科学");staticMap.put("ms","美术");staticMap.put("tzxkc","拓展型课程");staticMap.put("xxkj","信息科技");
		staticMap.put("yjxkc","研究型课程");staticMap.put("yyue","音乐");staticMap.put("njyy","牛津英语");staticMap.put("sw","生物");staticMap.put("xsjyy","高中新世纪英语");staticMap.put("zr","自然");
		staticMap.put("qm","期末");staticMap.put("qz","期中");staticMap.put("sxq","上学期");staticMap.put("xxq","下学期");
		Integer percentage = parseInteger(requestMap.get("percentage"));
		JSONObject jo =new JSONObject();
		DecimalFormat format = new DecimalFormat("0.00%");
		DecimalFormat df = new DecimalFormat("######0.0000");  
		LinkedHashMap<String,List> courseMap =new LinkedHashMap<>();
		
		int zf=100;//
		int stage = zf/percentage;//100/5=20
		int gap=10;//分数段间隔 10分 页面好像没得选 先写死
		List<Double> stageList1 = new ArrayList<>();
		List<String> percentageList = new ArrayList<>();
		requestMap.put("isDistrict",true);
		//————————————————————————————————————————————————————————————————————————————
		int allZf=0;
		HashMap<String,HashMap> courseScoreMap = new HashMap<>();
		List<HashMap<String,String>> courseExaminfoList =smallTitleService.getCourseExaminfoList(requestMap);
		if(courseExaminfoList.size()<1){
			jo.put("message", "faile");
			return jo;
		}
		for(HashMap<String,String> temp:courseExaminfoList){
			allZf+=parseInteger(temp.get("Exam_Zf"));
			courseScoreMap.put(temp.get("Course"), temp);
			courseList.add(temp.get("Course"));
		}
		//————————————————————————————————————————————————————————————————————————————
		List<Double> totalScoreList = smallTitleService.getScoreNum(requestMap);
		if(totalScoreList.size()<1){
			jo.put("message", "faile");
			return jo;
		}
		int scoreNum=totalScoreList.size();//总分个数  前百分之多少指的是占去重后全部分数的百分比  不能对SUM进行COUNT 所以先查下有多少个不重的总分
		for(int i=0;i<stage-1;i++){
			int index=(int)(Math.floor(parseDouble(scoreNum)/100*((i+1)*percentage)));
			if(index>0){
				index=index-1;
			}
			Double rate = totalScoreList.get(index);
			stageList1.add(rate);
			percentageList.add("前"+(i+1)*percentage+"%分数点");
		}
		
		courseMap.put("百分点/科目", percentageList);
		requestMap.remove("isDistrict");
		HashMap<String,List> allCourseMap = new HashMap<>();
		//————————————————————————————————————————————————————————————————————————————
		//
		//————————————————————————————————————————————————————————————————————————————
		for(int h=0;h<courseList.size();h++){
			requestMap.put("targetCourse", courseList.get(h));
			List<Object> oneCourseList =new ArrayList<>();
			List<Double> stageList = new ArrayList<>();
			List<Double> oneCourseScoreList = smallTitleService.getScoreNum(requestMap);
			int courseScoreNum=oneCourseScoreList.size();
			for(int i=0;i<stage-1;i++){
				int index = (int)(Math.floor(parseDouble(courseScoreNum)/100*((i+1)*percentage)));
				if(index>0){
					index=index-1;
				}
				Double rate = oneCourseScoreList.get(index);
				stageList.add(rate);
			}
			courseMap.put(staticMap.get(courseList.get(h)), stageList);
			
			
			List<Double> scoreList = smallTitleService.getScoreList(requestMap);//待筛选的分数集合 其中储存了这一门考试所有考生的成绩
			int allScore=parseInteger(courseScoreMap.get(courseList.get(h)).get("Exam_Zf"));
			int max=0;
			int min=0;
			
			for(int j=allScore;j>0;j-=gap){
				max=j;
				if(j==allScore){
					oneCourseList.add(j+"~"+(j-10));
					min=j-10;
					j--;
				}else{
					oneCourseList.add(j+"~"+(j-9));
					min=j-9;
				}
				int totalNumber=0;
				for(Double temp:scoreList){
					if(temp>=min && temp<=max){
						totalNumber++;
					}
				}
				oneCourseList.add(totalNumber);
				oneCourseList.add(df.format(parseDouble(totalNumber)/scoreList.size()));
				allCourseMap.put(staticMap.get(courseList.get(h)), oneCourseList);
			}
		};
		courseMap.put("总分", stageList1);//还要传个百分点 科目
		//————————————————————————————————————————————————————————————————————————————
		//总分分数段 各段人数 百分比 累计人数 累计百分比
		//————————————————————————————————————————————————————————————————————————————
		int allScore=allZf;//应该用查找所有考试的总分 然后加起来
		List<Object> allList =new ArrayList<>();
		int ljrs=0;//累计人数
		Double ljbfb=0.0;//累计百分比
		requestMap.put("isDistrict",true);
		List<Double> scoreList = smallTitleService.getScoreList(requestMap);//待筛选的分数集合 其中储存了这一门考试所有考生的成绩
		int max=0;
		int min=0;
		for(int j=allScore;j>0;j-=gap){
			max=j;
			if(j==allScore){
				allList.add(j+"~"+(j-10));
				min=j-10;
				j--;
			}else{
				allList.add(j+"~"+(j-9));
				min=j-9;
			}
			int totalNumber=0;
			for(Double temp:scoreList){
				if(temp>=min && temp<=max){
					totalNumber++;
				}
			}
			allList.add(totalNumber);
			ljrs+=totalNumber;
			allList.add(format.format(parseDouble(totalNumber)/scoreList.size()));
			ljbfb=ArithmeticUtil.add(ljbfb,parseDouble(totalNumber)/scoreList.size());
			allList.add(ljrs);
			allList.add(format.format(ljbfb));
		}
		
		jo.put("courseMap", courseMap);
		jo.put("allCourseMap", allCourseMap);
		jo.put("allList", allList);
		return jo;
	}

	/**
	 * 
	 * @Title: analyzePersonalSamllScoreImport
	 * @Description: 导出
	 * @author jay zhong
	 * @date 2017年11月27日 下午4:50:25 
	 * @return void
	 *
	 * @param data
	 * @param response
	 * @throws UnsupportedEncodingException
	 */

	@RequestMapping(params = "command=importExcel")
	@ResponseBody
	public void analyzePersonalSamllScoreImport(@RequestParam("data") String data,HttpServletResponse response) throws UnsupportedEncodingException{
		data = new String (data.getBytes("iso8859-1"),"UTF-8");
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		ArrayList<String> courseList = new ArrayList<>();
		HashMap<String,String> staticMap = new HashMap();
		//TODO 改成enumMap工具类
		staticMap.put("yw","语文");staticMap.put("sx","数学");staticMap.put("yy","外语");staticMap.put("wl","物理");staticMap.put("hx","化学");staticMap.put("ty","体育");staticMap.put("sxzz","思想政治");
		staticMap.put("ls","历史");staticMap.put("dl","地理");staticMap.put("kx","科学");staticMap.put("ms","美术");staticMap.put("tzxkc","拓展型课程");staticMap.put("xxkj","信息科技");
		staticMap.put("yjxkc","研究型课程");staticMap.put("yyue","音乐");staticMap.put("njyy","牛津英语");staticMap.put("sw","生物");staticMap.put("xsjyy","高中新世纪英语");staticMap.put("zr","自然");
		staticMap.put("qm","期末");staticMap.put("qz","期中");staticMap.put("sxq","上学期");staticMap.put("xxq","下学期");
		Integer percentage = parseInteger(requestMap.get("percentage"));
		JSONObject jo =new JSONObject();
		DecimalFormat format = new DecimalFormat("0.00%");
		DecimalFormat df = new DecimalFormat("######0.0000");  
		LinkedHashMap<String,List> courseMap =new LinkedHashMap<>(); 
		
		int zf=100;//
		int stage = zf/percentage;//100/5=20
		int gap=10;//分数段间隔 10分 页面好像没得选 先写死
		List<Double> stageList1 = new ArrayList<>();
		List<String> percentageList = new ArrayList<>();
		requestMap.put("isDistrict",true);
		//————————————————————————————————————————————————————————————————————————————
		int allZf=0;
		HashMap<String,HashMap> courseScoreMap = new HashMap<>();
		List<HashMap<String,String>> courseExaminfoList =smallTitleService.getCourseExaminfoList(requestMap);
		if(courseExaminfoList.size()<1){
			jo.put("message", "faile");
			//return jo;
		}
		for(HashMap<String,String> temp:courseExaminfoList){
			allZf+=parseInteger(temp.get("Exam_Zf"));
			courseScoreMap.put(temp.get("Course"), temp);
			courseList.add(temp.get("Course"));
		}
		//————————————————————————————————————————————————————————————————————————————
		List<Double> totalScoreList = smallTitleService.getScoreNum(requestMap);
		if(totalScoreList.size()<1){
			jo.put("message", "faile");
			//return jo;
		}
		int scoreNum=totalScoreList.size();//总分个数  前百分之多少指的是占去重后全部分数的百分比  不能对SUM进行COUNT 所以先查下有多少个不重的总分
		for(int i=0;i<stage-1;i++){
			Double rate = totalScoreList.get((int)(Math.floor(parseDouble(scoreNum)/100*((i+1)*percentage))));
			stageList1.add(rate);
			percentageList.add("前"+(i+1)*percentage+"%分数点");
		}
		
		courseMap.put("百分点/科目", percentageList);
		requestMap.remove("isDistrict");
		Map<String,List> allCourseMap = new HashMap<>();
		//————————————————————————————————————————————————————————————————————————————
		//
		//————————————————————————————————————————————————————————————————————————————
		for(int h=0;h<courseList.size();h++){
			requestMap.put("targetCourse", courseList.get(h));
			List<Object> oneCourseList =new ArrayList<>();
			List<Double> stageList = new ArrayList<>();
			List<Double> oneCourseScoreList = smallTitleService.getScoreNum(requestMap);
			int courseScoreNum=oneCourseScoreList.size();
			for(int i=0;i<stage-1;i++){
				int index = (int)(Math.floor(parseDouble(courseScoreNum)/100*((i+1)*percentage)));
				Double rate = oneCourseScoreList.get(index);
				stageList.add(rate);
			}
			courseMap.put(staticMap.get(courseList.get(h)), stageList);
			
			
			List<Double> scoreList = smallTitleService.getScoreList(requestMap);//待筛选的分数集合 其中储存了这一门考试所有考生的成绩
			int allScore=parseInteger(courseScoreMap.get(courseList.get(h)).get("Exam_Zf"));
			int max=0;
			int min=0;
			
			for(int j=allScore;j>0;j-=gap){
				max=j;
				if(j==allScore){
					oneCourseList.add(j+"~"+(j-10));
					min=j-10;
					j--;
				}else{
					oneCourseList.add(j+"~"+(j-9));
					min=j-9;
				}
				int totalNumber=0;
				for(Double temp:scoreList){
					if(temp>=min && temp<=max){
						totalNumber++;
					}
				}
				oneCourseList.add(totalNumber);
				oneCourseList.add(df.format(parseDouble(totalNumber)/scoreList.size()));
				allCourseMap.put(staticMap.get(courseList.get(h)), oneCourseList);
			}
		};
		courseMap.put("总分", stageList1);//还要传个百分点 科目
		//————————————————————————————————————————————————————————————————————————————
		//总分分数段 各段人数 百分比 累计人数 累计百分比
		//————————————————————————————————————————————————————————————————————————————
		int allScore=allZf;//应该用查找所有考试的总分 然后加起来
		List<Object> allList =new ArrayList<>();
		int ljrs=0;//累计人数
		Double ljbfb=0.0;//累计百分比
		requestMap.put("isDistrict",true);
		List<Double> scoreList = smallTitleService.getScoreList(requestMap);//待筛选的分数集合 其中储存了这一门考试所有考生的成绩
		int max=0;
		int min=0;
		for(int j=allScore;j>0;j-=gap){
			max=j;
			if(j==allScore){
				allList.add(j+"~"+(j-10));
				min=j-10;
				j--;
			}else{
				allList.add(j+"~"+(j-9));
				min=j-9;
			}
			int totalNumber=0;
			for(Double temp:scoreList){
				if(temp>=min && temp<=max){
					totalNumber++;
				}
			}
			allList.add(totalNumber);
			ljrs+=totalNumber;
			allList.add(format.format(parseDouble(totalNumber)/scoreList.size()));
			ljbfb=ArithmeticUtil.add(ljbfb,parseDouble(totalNumber)/scoreList.size());
			allList.add(ljrs);
			allList.add(format.format(ljbfb));
		}
		
		jo.put("courseMap", courseMap);
		jo.put("allCourseMap", allCourseMap);
		jo.put("allList", allList);
		System.out.println("通过Map.entrySet遍历key和value");
		 int j=0;
		 int valuesSize=0;
		   List<Map<String, Object>> mapsList=new ArrayList<>();
		   for (Entry<String, List> entry :courseMap.entrySet()) {
		   		valuesSize=entry.getValue().size();
		   		break;
		   }
		  
		   for(int i=0;i<valuesSize;i++){
			   Map<String, Object> map=new HashMap<>();
			   for (Entry<String, List> entrys :courseMap.entrySet()) {
				   int k=0;
				   List<String> values=entrys.getValue();
				   for(int q=0;q<values.size();q++){//循环遍历map的value值[前5%分数点, 前10%分数点, 前15%分数点, 前20%分数点, 前25%分数点, 前30%分数点, 前35%分数点, 前40%分数点, 前45%分数点, 前50%分数点, 前55%分数点, 前60%分数点, 前65%分数点, 前70%分数点, 前75%分数点, 前80%分数点, 前85%分数点, 前90%分数点, 前95%分数点]
					   if(k==0){
						   Object value=values.get(i);
						   map.put(entrys.getKey(), value);
					   }
					   k++;
					   break;
				   }
				   j++;
			   }
			   mapsList.add(map);
		   }  
		
		String fileName="百分点分段.xls";
		String sheetName="百分点分段列表详情";
		List<String> title1=new ArrayList<>();
		List<String> title2=new ArrayList<>();
		List<String> title3=new ArrayList<>();
		List<String> key1=new ArrayList<>();
		List<String> key2=new ArrayList<>();
		List<String> key3=new ArrayList<>();
		key1.add("序号");
		key1.add("百分点/科目");
		Set<String> keys1=courseMap.keySet();
		Set<String> keys2=staticMap.keySet();
		int total=0;
		for(String s:courseList){
			key1.add(staticMap.get(s));
			title2.add(staticMap.get(s));
			title2.add(staticMap.get(s));
			title2.add(staticMap.get(s));
			title3.add("分数段");
			title3.add("人数");
			title3.add("百分比");
			List list=allCourseMap.get(staticMap.get(s));
			total=list.size();
		}
		List<List> dataList2=new ArrayList<>();
		for(int i=0;i<10;i++){
			List dataMap=new ArrayList<>();
			
				
				for(int m=0;m<courseList.size();m++){
					List list=allCourseMap.get(staticMap.get(courseList.get(m)));
					int c1=i*3;
					int c2=i*3+1;
					int c3=i*3+2;
					dataMap.add(list.get(i*3));
					dataMap.add(list.get(i*3+1));
					dataMap.add(format.format(parseDouble(list.get(i*3+2))));
					
					
				}
			
			dataList2.add(dataMap);
		}
		
		 List<List> dataList3=new ArrayList<>();
 		for(int i=0;i<allList.size()/5;i++){
 			List dataMap=new ArrayList<>();
			dataMap.add(allList.get(i*5));
			dataMap.add(allList.get(i*5+1));
			dataMap.add(allList.get(i*5+2));
			dataMap.add(allList.get(i*5+3));
			dataMap.add(allList.get(i*5+4));
 			dataList3.add(dataMap);
 		}
		key1.add("总分");
		String[] arr1 = (String[])key1.toArray(new String[key1.size()]);
		//ExportUtil.ExportExcel(response, arr1, fileName, sheetName, mapsList, arr1);
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

            HSSFRow row = sheet.createRow((short) 0);
            row.setHeight((short) 350);
            
            //设置页名
            workbook.setSheetName(0,sheetName);
            for(int j1=0;j1<arr1.length;j1++){
            	//设置列长
 	            sheet.setColumnWidth((short) j1, (short) 5000);
 	            //设置title
 	           HSSFCell cell = row.createCell((short) j1);
               cell.setCellStyle(titleStyle);
               cell.setCellType(HSSFCell.CELL_TYPE_STRING);
               cell.setCellValue(arr1[j1]);
            }
            // 写入实体数据,title和keys相对应，顺序一致，用于赋值
            for (int i = 0; i < mapsList.size(); i++) {
                Map<String, Object> paramMap = mapsList.get(i);
                //序号，默认都有,从第二行开始
                row = sheet.createRow((short) i + 1);
	            HSSFCell cell = row.createCell((short) 0);
	            cell.setCellStyle(style);
	            cell.setCellValue((i + 1));
	            
	            
	            //把值写入表格
	            for(int k=1;k<arr1.length;k++){
	            	HSSFCell keyCell = row.createCell((short) k);
	            	keyCell.setCellStyle(style);
	            	keyCell.setCellValue(parseString(paramMap.get(arr1[k])));
	            }
            }
            String sheetName2="分数段";
            HSSFSheet sheet2 = workbook.createSheet();
            HSSFRow row2 = sheet2.createRow((short) 0);
            HSSFRow row3 = sheet2.createRow((short) 1);
            row2.setHeight((short) 350);
            workbook.setSheetName(1,sheetName2);
            
           
            //第三页
            String sheetName3="各分段详情";
            HSSFSheet sheet3 = workbook.createSheet();
            HSSFRow row4 = sheet3.createRow((short) 0);
            row4.setHeight((short) 350);
            workbook.setSheetName(2,sheetName3);
            String[] key4={"分数段","人数","百分比","累计人数","累计百分比"};
            
            List<String> head2=new ArrayList<>();
            int a=0;
            int b=2;
            for(int t=0;t<courseList.size();t++){
            	head2.add("0,0,"+a+","+b);
            	a+=3;
            	b=a+2;
            }
            
           
            
            //第二页
            for(int j1=0;j1<title2.size();j1++){
            	//设置列长
 	            sheet2.setColumnWidth((short) j1, (short) 5000);
 	            //设置title
 	           HSSFCell cell = row2.createCell((short) j1);
               cell.setCellStyle(titleStyle);
               cell.setCellType(HSSFCell.CELL_TYPE_STRING);
               cell.setCellValue(title2.get(j1));
            }
            for(int j1=0;j1<title3.size();j1++){
            	//设置列长
 	            sheet2.setColumnWidth((short) j1, (short) 5000);
 	            //设置title
 	           HSSFCell cell = row3.createCell((short) j1);
               cell.setCellStyle(titleStyle);
               cell.setCellType(HSSFCell.CELL_TYPE_STRING);
               cell.setCellValue(title3.get(j1));
            }
            // 写入实体数据,title和keys相对应，顺序一致，用于赋值
            for (int i = 0; i < dataList2.size(); i++) {
                List paramMap = dataList2.get(i);
                row = sheet2.createRow((short) i + 2);
	            
	            
	            //把值写入表格
	            for(int k=0;k<courseList.size()*3;k++){
	            	HSSFCell keyCell = row.createCell((short) k);
	            	keyCell.setCellStyle(style);
	            	keyCell.setCellValue(parseString(paramMap.get(k)));
	            }
            }
            //动态合并单元格
            for (int i = 0; i < head2.size(); i++) {
                String[] temp = head2.get(i).split(",");
                Integer startrow = Integer.parseInt(temp[0]);
                Integer overrow = Integer.parseInt(temp[1]);
                Integer startcol = Integer.parseInt(temp[2]);
                Integer overcol = Integer.parseInt(temp[3]);
                sheet2.addMergedRegion(new CellRangeAddress(startrow, overrow,
                        startcol, overcol));
            }
            
            //第三页
           
            for(int j1=0;j1<key4.length;j1++){
            	//设置列长
 	            sheet3.setColumnWidth((short) j1, (short) 5000);
 	            //设置title
 	           HSSFCell cell = row4.createCell((short) j1);
               cell.setCellStyle(titleStyle);
               cell.setCellType(HSSFCell.CELL_TYPE_STRING);
               cell.setCellValue(key4[j1]);
            }
           
            // 写入实体数据,title和keys相对应，顺序一致，用于赋值
            for (int i = 0; i < dataList3.size(); i++) {
                List paramMap = dataList3.get(i);
                row = sheet3.createRow((short) i + 1);
	            
	            
	            //把值写入表格
	            for(int k=0;k<5;k++){
	            	HSSFCell keyCell = row.createCell((short) k);
	            	keyCell.setCellStyle(style);
	            	keyCell.setCellValue(parseString(paramMap.get(k)));
	            }
            }
            
            
            OutputStream out = response.getOutputStream();
            workbook.write(out);
            out.flush();
            out.close();
        } catch (Exception e) {
            System.out.println(e);
        }
		
		//return jo;
	}
}
