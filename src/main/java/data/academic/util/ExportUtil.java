package data.academic.util;

import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import data.framework.utility.FormatConvertor;

/**
 * 导出工具类
 * @author jay zhong
 *
 */

public class ExportUtil {
	public final static Map<String, Object> CLASS_TYPE_MAP = new HashMap<String, Object>() {
	    {
	        put("01", "小班");
	        put("02", "中班");
	        put("03", "大班");
	        put("11", "一年级");
	        put("12", "二年级");
	        put("13", "三年级");
	        put("14", "四年级");
	        put("15", "五年级");
	        put("16", "六年级");
	        put("17", "七年级");
	        put("18", "八年级");
	        put("19", "九年级");
	        put("31", "高一");
	        put("32", "高二");
	        put("33", "高三");
	        put("34","高预(民族班)");
	        put("yw","语文");
	        put("sx","数学");
	        put("yy","外语");
	        put("wl","物理");
	        put("hx","化学");
	        put("ty","体育");
	        put("sxzz","思想政治");
	        put("ls","历史");
	        put("dl","地理");
	        put("sw","生物");
	        put("ms","美术");
	        put("zr","自然");
	        put("yyue","音乐");
	        put("xxkj","信息科技");
	        put("tzxkc","拓展型课程");
	        put("yjxkc","研究型课程");
	        put("kx","科学");
	        put("njyy","牛津英语");
	        put("xsjyy","高中新世纪英语");
	        put("sxq","第一学期");
	        put("xxq","第二学期");
	        put("qz","期中");
	        put("qm","期末");
	        put("aj","A卷");
	        put("bj","B卷");
	        put("lk","理科");
	        put("wk","文科");
	        put("lhhj","理化合卷");
	        put("0","小学");
	        put("1","初中");
	        put("2","高中");
	        put("0, 1","一贯制");
	        put("1, 2","完校");
	        put("0, 1, 2","其它");
	        put("qb","全部");
	        put("sbjd","随班就读");
	        put("bx","本校");
	        put("yx","原校");
	        put("xjb","新疆班");
	       
	    }
	};
	public final static Map<String, Object> CLASS_NO_MAP = new HashMap<String, Object>() {
	    {
	        put("01","(1)班");
	        put("02","(2)班");
	        put("03","(3)班");
	        put("04","(4)班");
	        put("05","(5)班");
	        put("06","(6)班");
	        put("07","(7)班");
	        put("08","(8)班");
	        put("09","(9)班");
	        put("10","(10)班");
	        put("11","(11)班");
	        put("12","(12)班");
	        put("13","(13)班");
	        put("14","(14)班");
	        put("15","(15)班");
	        put("16","(16)班");
	        put("17","(17)班");
	        put("18","(18)班");
	        put("19","(19)班");
	        put("20","(20)班");
	        put("21","(21)班");
	        put("22","(22)班");
	        put("23","(23)班");
	        put("24","(24)班");
	        put("25","(25)班");
	        put("26","(26)班");
	        put("27","(27)班");
	        put("28","(28)班");
	        put("29","(29)班");
	       
	    }
	};
	public final static Map<String, Object> XJ_STATE_MAP = new HashMap<String, Object>() {
	    {
	        put("0","在读");
	        put("1","随班就读");
	        put("2","调出");
	        put("3","待调动");
	        put("4","调入");
	        put("5","新疆班");
	       
	    }
	};
	
	/**
	 * 
	 * @Title: ExportExcel
	 * @Description: 导出excel工具,以行为单位进行赋值
	 * @author jay zhong
	 * @date 2017年11月20日 上午11:46:06 
	 * @return void
	 *
	 * @param response
	 * @param title 标题
	 * @param fileName 文件名字：XXX.xls
	 * @param sheetName 页名
	 * @param dataList 数据集合
	 * @param key map的key值，用于取值
	 */
	@SuppressWarnings("deprecation")
	public static void ExportExcel(HttpServletResponse response,String[] title,String fileName,String sheetName,
													List<Map<String,Object>> dataList,String[] key){
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
            for(int j=0;j<title.length;j++){
            	//设置列长
 	            sheet.setColumnWidth((short) j, (short) 5000);
 	            //设置title
 	           HSSFCell cell = row.createCell((short) j);
               cell.setCellStyle(titleStyle);
               cell.setCellType(HSSFCell.CELL_TYPE_STRING);
               cell.setCellValue(title[j]);
            }
            // 写入实体数据,title和keys相对应，顺序一致，用于赋值
            for (int i = 0; i < dataList.size(); i++) {
                Map<String, Object> paramMap = dataList.get(i);
                //序号，默认都有,从第二行开始
                row = sheet.createRow((short) i + 1);
	            HSSFCell cell = row.createCell((short) 0);
	            cell.setCellStyle(style);
	            cell.setCellValue((i + 1));
	            
	            //把值写入表格
	            for(int k=1;k<key.length;k++){
	            	HSSFCell keyCell = row.createCell((short) k);
	            	keyCell.setCellStyle(style);
	            	keyCell.setCellValue(parseString(paramMap.get(key[k])));
	            }
            }
            OutputStream out = response.getOutputStream();
            workbook.write(out);
            out.flush();
            out.close();
        } catch (Exception e) {
            System.out.println(e);
        }
		
	}
	/**
	 * 
	 * @Title: Export2SheetExcel
	 * @Description: 导出两页的excel表格
	 * @author jay zhong
	 * @date 2017年11月24日 下午1:27:31 
	 * @return void
	 *
	 * @param response
	 * @param fileName 文件名
	 * @param title1 标题1
	 * @param key1 key1
	 * @param sheetName1 页名1
	 * @param dataList1 数据1
	 * @param sheetName2 页名2
	 * @param title2 标题2
	 * @param key2 key2
	 * @param dataList2 数据2
	 */
	@SuppressWarnings("deprecation")
	public static void Export2SheetExcel(HttpServletResponse response,String fileName,String[] title1,String[] key1,String sheetName1,
			List<Map<String,Object>> dataList1,String sheetName2,String[] title2,String[] key2,
			List<Map<String,Object>> dataList2){
		try {
			 response.setContentType("application/x-download");
	            //fileDisplay = URLEncoder.encode(fileDisplay, "UTF-8");
	            response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("GBK"), "ISO-8859-1"));
	            HSSFWorkbook workbook = new HSSFWorkbook();
	     
	            HSSFSheet sheet = workbook.createSheet();
	            HSSFSheet sheet2 = workbook.createSheet();
	           
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
	            HSSFRow row2 = sheet2.createRow((short) 0);
	            row.setHeight((short) 350);
	            row2.setHeight((short) 350);
	            
	            //设置页名
	            workbook.setSheetName(0,sheetName1);
	            workbook.setSheetName(1,sheetName2);
	            for(int j=0;j<title1.length;j++){
	            	//设置列长
	 	            sheet.setColumnWidth((short) j, (short) 5000);
	 	            //设置title
	 	           HSSFCell cell = row.createCell((short) j);
	               cell.setCellStyle(titleStyle);
	               cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	               cell.setCellValue(title1[j]);
	            }
	            for(int j=0;j<title2.length;j++){
	            	//设置列长
	 	            sheet2.setColumnWidth((short) j, (short) 5000);
	 	            //设置title
	 	           HSSFCell cell = row2.createCell((short) j);
	               cell.setCellStyle(titleStyle);
	               cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	               cell.setCellValue(title2[j]);
	            }
	            // 写入实体数据,title和keys相对应，顺序一致，用于赋值
	            for (int i = 0; i < dataList1.size(); i++) {
	                Map<String, Object> paramMap = dataList1.get(i);
	                //序号，默认都有,从第二行开始
	                row = sheet.createRow((short) i + 1);
		            HSSFCell cell = row.createCell((short) 0);
		            cell.setCellStyle(style);
		            cell.setCellValue((i + 1));
		            
		            //把值写入表格
		            for(int k=1;k<key1.length;k++){
		            	HSSFCell keyCell = row.createCell((short) k);
		            	keyCell.setCellStyle(style);
		            	keyCell.setCellValue(parseString(paramMap.get(key1[k])));
		            }
	            }
	            // 写入实体数据,title和keys相对应，顺序一致，用于赋值
	            for (int i = 0; i < dataList2.size(); i++) {
	                Map<String, Object> paramMap = dataList2.get(i);
	                //序号，默认都有,从第二行开始
	                row2 = sheet2.createRow((short) i + 1);
		            HSSFCell cell = row2.createCell((short) 0);
		            cell.setCellStyle(style);
		            cell.setCellValue((i + 1));
		            
		            //把值写入表格
		            for(int k=1;k<key2.length;k++){
		            	HSSFCell keyCell = row2.createCell((short) k);
		            	keyCell.setCellStyle(style);
		            	keyCell.setCellValue(parseString(paramMap.get(key2[k])));
		            }
	            }
	            OutputStream out = response.getOutputStream();
	            workbook.write(out);
	            out.flush();
	            out.close();
		} catch (Exception e) {
            System.out.println(e);
        }
		
		
	}
	 /**
     * 将 Object 转换成字符串。
     * @param value 输入值
     * @return 输出值，对于 null 将返回空串
     */
	 private static String parseString(Object object) {
		 return FormatConvertor.parseString( object ) ;
	}
	 /**  
	     * 计算两个日期之间相差的天数  
	     * @author jay zhong
	     * @date 2017-11-23
	     * @param smdate 较小的时间 
	     * @param bdate  较大的时间 
	     * @return 相差天数 
	     * @throws ParseException  
	     */    
	    public static int daysBetween(Date smdate,Date bdate) throws ParseException
	    {    
	        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
	        smdate=sdf.parse(sdf.format(smdate));  
	        bdate=sdf.parse(sdf.format(bdate));  
	        Calendar cal = Calendar.getInstance();    
	        cal.setTime(smdate);    
	        long time1 = cal.getTimeInMillis();                 
	        cal.setTime(bdate);    
	        long time2 = cal.getTimeInMillis();         
	        long between_days=(time2-time1)/(1000*3600*24);  
	            
	       return Integer.parseInt(String.valueOf(between_days));           
	    }    
	

}
