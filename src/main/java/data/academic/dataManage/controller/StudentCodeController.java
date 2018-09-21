package data.academic.dataManage.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import data.academic.dataManage.service.StudentCodeService;
import data.framework.support.AbstractBaseController;
import data.framework.utility.ExcelReadUtils;
import data.framework.utility.UploadHelper;
import data.platform.authority.security.SecurityContext;
@RestController
@RequestMapping("dataManage/studentCode")
public class StudentCodeController extends AbstractBaseController{

	@Override
	protected void init(ModelMap model, HttpServletRequest request) {
		// TODO Auto-generated method stub
		
	}
	@Autowired
	private StudentCodeService studentCodeService;
	private Map<String,String> map = new HashMap<String,String>();
	
	
	/**
	 * @Title: importStuCodeExamCode
	 * @Description: 导入学籍号考号
	 * @author xiahuajun
	 * @date 2016年9月1日 
	 * @param xlsFile
	 * @param out
	 * @param request
	 * @return void
	 */
	@RequestMapping(params=("command=importStuCodeExamCode"))
    public void importStuCodeExamCode(@RequestParam("xlsFile")CommonsMultipartFile xlsFile,java.io.PrintWriter out,HttpServletRequest request){
		 //Map<String, Object> requestMap = getSerializer().parseMap(data);
		 File fullFile = new File(uploadPath+File.separator+xlsFile.getFileItem().getName());
		 try {
			 if(!fullFile.exists())
			 {
				 fullFile.createNewFile();
			 }
			xlsFile.transferTo(fullFile);
			List<List<Object>> list =ExcelReadUtils.readExcel(fullFile);
			//System.out.println(list.size());
			
			//List<Object> titleO = list.get(0);
			List<Map<String,Object>> paramList = new ArrayList<Map<String,Object>>();
			String create_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			String create_person = SecurityContext.getPrincipal().getChineseName();
			for (int i = 1; i < list.size(); i++) {
				List<Object> rowData = list.get(i);
				if(rowData.size() == 0){
					break;
				}
				//for(int k = 0; k < rowData.size(); k++){。
					Map<String,Object> param = new HashMap<String,Object>();
					//param.put("exam_number", rowData.get(0));
					//param.put("name", rowData.get(1));
					//param.put("tihao", titleO.get(k));
					//param.put("score", rowData.get(k));
					
					param.put("studentCode", rowData.get(0));
					param.put("examNumber", rowData.get(1));
					param.put("create_time", create_time);
					param.put("create_person", create_person);
					
					//map.put("schoolYear",requestMap.get("schoolYear").toString());
					//map.put("schoolType",requestMap.get("schoolType").toString());
					//map.put("term",requestMap.get("term").toString());
					//map.put("examType",requestMap.get("examType").toString());
					//map.put("course",requestMap.get("course").toString());
					param.put("schoolYear", map.get("schoolYear"));
					param.put("schoolType", map.get("schoolType"));
					param.put("term", map.get("term"));
					param.put("examType", map.get("examType"));
					param.put("randomUUid", UUID.randomUUID().toString());
					//param.put("course", map.get("course"));
					paramList.add(param);
				//}
//				if(map.containsKey("exam_number")){
//					map.remove("exam_number");
//				}
				//map.put("studentCode", rowData.get(0).toString());
				//防止重复导入
				//
				studentCodeService.deleteStuCode(param);
				
			}
			//导入考号学籍号	
			studentCodeService.insertStuCodeAndExamCode(paramList);
			Map<String,Object> param = new HashMap<String,Object>();
			param.put("mess", "success");
			out.print(getSerializer().formatMap(param));
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();

		}
		
	}	 
	
	/**
	 * @Title: getImportParams
	 * @Description: 得到下拉选项的值
	 * @author xiahuajun
	 * @date 2016年9月1日 
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=getImportParams")
	public void getImportParams(@RequestParam("data") String data,java.io.PrintWriter out){
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		//{schoolYear=wl, schoolType=cz, term=sxq, examType=qz, course=yy}
		map.put("schoolYear",requestMap.get("schoolYear").toString());
		map.put("schoolType",requestMap.get("schoolType").toString());
		map.put("term",requestMap.get("term").toString());
		map.put("examType",requestMap.get("examType").toString());
		out.print(getSerializer().formatObject(""));
	}
    private static String uploadPath = UploadHelper.getRealUploadPath() ;
}
