package data.framework.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
/**
 * 读取excel内容
 * @author Administrator
 *
 */
public class ExcelReadUtils {
	/**
	 * 读取2003Excel
	 * 
	 * @param file
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static List<List<Object>> read2003Excel(File file,InputStream inputStream) throws IOException {
		List<List<Object>> dataList = new ArrayList<List<Object>>();
		HSSFWorkbook wb = null;
		if (file != null) {
			wb = new HSSFWorkbook(new FileInputStream(file));
		} else {
			wb = new HSSFWorkbook(new POIFSFileSystem(inputStream));
		}
		HSSFSheet sheet = wb.getSheetAt(0);
		HSSFRow row = null;
		HSSFCell cell = null;
		Object val = null;
		DecimalFormat df = new DecimalFormat("0");// 格式化数字
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 格式化日期字符串
		for (int i = sheet.getFirstRowNum(); i < sheet
				.getPhysicalNumberOfRows(); i++) {
			row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			List<Object> objList = new ArrayList<Object>();
			for (int j = 0; j < row.getLastCellNum(); j++) {
				cell = row.getCell((short) j);
				if (cell == null) {
					val = null;
					objList.add(val);
					continue;
				}
				switch (cell.getCellType()) {
				case HSSFCell.CELL_TYPE_STRING:
					val = cell.getStringCellValue();
					break;
				case HSSFCell.CELL_TYPE_NUMERIC:
					if ("@".equals(cell.getCellStyle().getDataFormatString())) {
						val = df.format(cell.getNumericCellValue());
					} else {
						val = df.format(cell.getNumericCellValue());
					}
					break;
				case HSSFCell.CELL_TYPE_BOOLEAN:
					val = cell.getBooleanCellValue();
					break;
				case HSSFCell.CELL_TYPE_BLANK:
					val = "";
					break;
				default:
					val = cell.toString();
					break;
				}
				objList.add(val);
			}
			dataList.add(objList);
		}
		return dataList;
	}

	/**
	 * 读取Excel表头
	 * @param file
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("deprecation")
	public String[] readExcelHead(File file, InputStream inputStream) throws IOException {
		HSSFWorkbook wb = null;
		if (file != null) {
			wb = new HSSFWorkbook(new FileInputStream(file));
		} else {
			wb = new HSSFWorkbook(new POIFSFileSystem(inputStream));
		}
		HSSFSheet sheet = wb.getSheetAt(0);
		HSSFRow row = null;
		HSSFCell cell = null;
		row = sheet.getRow(0);
		String[] buff = new String[row.getLastCellNum()];
		for (int i = 0; i < row.getLastCellNum(); i++) {
			cell = row.getCell((short) i);
			buff[i] = cell.getStringCellValue();
		}
		return buff;
	}

	/**
	 * 读取2007excel
	 * @param file
	 * @param inputStream
	 *            两个参数任选其一
	 * @return
	 */
	public static List<List<Object>> read2007Excel(File file,InputStream inputStream) throws IOException {
		List<List<Object>> dataList = new ArrayList<List<Object>>();
		XSSFWorkbook xwb = null;
		if (file != null) {
			xwb = new XSSFWorkbook(new FileInputStream(file));
		} else {
			xwb = new XSSFWorkbook(inputStream);
		}

		XSSFSheet sheet = xwb.getSheetAt(0);
		XSSFRow row = null;
		XSSFCell cell = null;
		Object val = null;
		DecimalFormat df = new DecimalFormat("0");// 格式化数字
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 格式化日期字符串
		for (int i = sheet.getFirstRowNum(); i < sheet
				.getPhysicalNumberOfRows(); i++) {
			row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			List<Object> objList = new ArrayList<Object>();
			for (int j = 0; j < row.getLastCellNum(); j++) {
				cell = row.getCell(j);
				if (cell == null) {
					val = null;
					objList.add(val);
					continue;
				}
				switch (cell.getCellType()) {
				case XSSFCell.CELL_TYPE_STRING:// XSSFCell.CELL_TYPE_STRING
					val = cell.getStringCellValue();
					break;
				case XSSFCell.CELL_TYPE_NUMERIC:// XSSFCell.CELL_TYPE_NUMERIC
					if ("@".equals(cell.getCellStyle().getDataFormatString())) {
						val = df.format(cell.getNumericCellValue());
					} else{
						val = df.format(cell.getNumericCellValue());
					}
					break;
				case XSSFCell.CELL_TYPE_BOOLEAN:// XSSFCell.CELL_TYPE_BOOLEAN
					val = cell.getBooleanCellValue();
					break;
				case XSSFCell.CELL_TYPE_BLANK:// XSSFCell.CELL_TYPE_BLANK
					val = "";
					break;
				default:
					val = cell.toString();
					break;
				}
				objList.add(val);
			}
			dataList.add(objList);
		}
		return dataList;
	}

	// 读取excel文件的接口
	public static List<List<Object>> readExcel(File file) throws IOException {
		String fName = file.getName();
		String extension = fName.lastIndexOf(".") == -1 ? "" : fName
				.substring(fName.lastIndexOf(".") + 1);
		if ("xls".equals(extension)) {// 2003
			//System.err.println("读取excel2003文件内容");
			return read2003Excel(file,null);
		} else if ("xlsx".equals(extension)) {// 2007
			//System.err.println("读取excel2007文件内容");
			return read2007Excel(file,null);
		} else {
			throw new IOException("不支持的文件类型:" + extension);
		}
	}
	public static void main(String[] args) {
		try {
			List<List<Object>>  list =readExcel(new File("C://八年级数学_小题分 - 副本.xls"));
			for (int i=1;i<list.size();i++) {//除了第一行表头
				List<Object> list2=list.get(i);
				for (Object object : list2) {
					System.out.println(object);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
