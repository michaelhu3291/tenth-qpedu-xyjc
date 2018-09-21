package data.academic.transfer.controller;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import data.academic.transfer.service.TeacherTransferService;
import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractBaseController;
import data.framework.support.ConfigContext;
import data.platform.authority.security.SecurityContext;
@RestController
@RequestMapping("transfer/teacherTransfer")
public class TeacherTransferController extends AbstractBaseController{
	
	@Autowired
	private TeacherTransferService teacherTransferService;
	@Override
	protected void init(ModelMap model, HttpServletRequest request) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * @Title: searchPaging
	 * @Description: 分页查询申请调动老师
	 * @author xiahuajun
	 * @date 2016年9月20日 
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=searchPaging")
	public void searchPaging(@RequestParam("data") String data,
			java.io.PrintWriter out) {
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		// Map<String, Object> paramMap = new HashMap<String, Object>();
		boolean isFast = parseBoolean(requestMap.get("isFast"));
		String examPaperName = "";
		if (isFast) {
			examPaperName = trimString(requestMap.get("q"));
		}
		int currentPage = parseInteger(requestMap.get("page"));
		int pageSize = parseInteger(requestMap.get("rows"));
		String sortField = trimString(requestMap.get("sidx"));

		if (sortField.trim().length() == 0) {
			sortField = "Create_Time";
		}
		String sort = trimString("desc");
		PagingResult<Map<String, Object>> pagingResult = teacherTransferService.serachPaging(requestMap, sortField, sort, currentPage,pageSize);
		List<Map<String, Object>> list = pagingResult.getRows();
		//[{School_Name=上海市青浦区沈巷中学, id=193BE99D-36D4-406A-9BF1-50D09B917096, rownum=1, Teacher_Name=陈晓虎, Teacher_Pk=482D3F92-22CC-4289-A1D3-35531036F211, Create_Time=2016-09-20 00:25:28.0, Login_Name=chenxiaohu, Detached=1, Create_Person=蔡娟芳}, {School_Name=上海市青浦区沈巷中学, id=2AC95CB9-A338-43B3-B803-0B493607F13A, rownum=2, Teacher_Name=陈笑盈, Teacher_Pk=A931D25C-1FFE-43C0-852B-6383A1DE21B1, Create_Time=2016-09-20 00:25:28.0, Login_Name=chenxiaoy, Detached=1, Create_Person=蔡娟芳}]
		List<Map<String, Object>> paramList = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : list) {
			//Map<String, Object> paramMap = new HashMap<String, Object>();
			//加入新去向 
			map.put("School_New_Adr", map.get("School_New_Adr"));
			map.put("Create_Time",
					formatDate(parseDate(map.get("Create_Time")), "yyyy-MM-dd"));
			map.put("Id", map.get("Teacher_Pk").toString());
			paramList.add(map);
		}
		PagingResult<Map<String, Object>> newPagingResult = new PagingResult<Map<String, Object>>(
				paramList, pagingResult.getPage(), pagingResult.getPageSize(),
				pagingResult.getRecords());
		out.print(getSerializer().formatObject(newPagingResult));
	}
	
	/**
	 * @Title: searchPaging
	 * @Description: 查询所有的学校
	 * @author xiahuajun
	 * @date 2016年9月20日 
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=selectAllSchools")
	public void selectAllSchools(@RequestParam("data") String data,
			java.io.PrintWriter out) {
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		String school_sequence = "";
		String school_sequence1="";
		String schoolCode = "";
		if("xx".equals(requestMap.get("schoolType").toString())){
			school_sequence = "0";
		}
		else if("cz".equals(requestMap.get("schoolType").toString())){
			school_sequence = "1";
		}
		else if("ygz".equals(requestMap.get("schoolType").toString())){
			school_sequence = "5";
		}
		else if("wz".equals(requestMap.get("schoolType").toString())){
			school_sequence = "4";
			schoolCode = "3004";
		}
		else if("gz".equals(requestMap.get("schoolType").toString())){
			school_sequence1 = "gz";
			schoolCode = "3004";
		}
		
		requestMap.put("school_sequence", school_sequence);
		requestMap.put("school_sequence1", school_sequence1);
		requestMap.put("schoolCode", schoolCode);
		List<Map<String,Object>> list = teacherTransferService.selectAllSchools(requestMap);
		out.print(getSerializer().formatList(list));
	}
	
	/**
	 * @Title: selectAllSchools
	 * @Description:提交调动 老师
	 * @author xiahuajun
	 * @date 2016年9月20日 
	 * @param data
	 * @param out
	 * @return void
	 * @throws ParseException 
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(params = "command=submitTransfer")
	public void submitTransfer(@RequestParam("data") String data,
			java.io.PrintWriter out) throws ParseException, UnsupportedEncodingException {
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		Map<String, Object> paramMap =new HashMap<>();
		String name = null;
		String proposer = null;
		if(requestMap.containsKey("roleCode")){
			name = new String((trimString(requestMap.get("name"))).getBytes("iso-8859-1"),"utf-8");
			proposer = new String((trimString(requestMap.get("proposer"))).getBytes("iso-8859-1"),"utf-8");
		}else{
			name = trimString(requestMap.get("teacherName"));
			proposer = trimString(requestMap.get("proposer"));
		}
		//查询code校验教师是不是调回原校籍
		paramMap.put("teacherPk", requestMap.get("teacherPk"));
		paramMap.put("state", "2");
		String schoolCode = teacherTransferService.selectTeacherSchoolCode(requestMap.get("teacherPk").toString());
		String schoolCodes=teacherTransferService.getSchoolByState(paramMap);
		paramMap.put("state","");
		paramMap.put("notState","3");
		String school=teacherTransferService.getSchoolByState(paramMap);
		//当前年
        String time = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		String currentYear = time.substring(0, time.indexOf("-"));
		requestMap.put("schoolCode", schoolCode);
		if(schoolCode.equals(requestMap.get("school").toString())){
			if(!"".equals(schoolCodes) && schoolCodes!=null){
				if(schoolCodes.equals(requestMap.get("school").toString())){
					 //删除该老师和其它学校的关系表数据
					teacherTransferService.deleterefTeacherSchool(requestMap.get("teacherPk").toString());
					//添加一条状态为0的新数据
					 teacherTransferService.insertRefTeacherSchool(requestMap);
					 requestMap.put("mess","success");
				}else{
					requestMap.put("mess","false");
				}
			}else{
				requestMap.put("mess","false");
			}
		}else {
			//添加被调动老师与新学校的关联关系teacher_id School_code state=4(调入)
			teacherTransferService.submitTransferInfo(requestMap);
			//修改被调动老师在原学校的状态 state=2(调出)
			teacherTransferService.updateTeacherState(requestMap);
			requestMap.put("mess","success");
		}
		if("success".equals(requestMap.get("mess").toString())){
			//调动成功后清除老师关联的科目
			requestMap.put("currentYear", currentYear);
			teacherTransferService.deleteRefTeaCourse(requestMap);
			//调动成功后清除老师关联的班级
			teacherTransferService.deleteRefTeaClass(requestMap);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date d = sdf.parse(trimString(requestMap.get("applyTime")));
				String loginName=SecurityContext.getPrincipal().getUsername();
				Map<String, Object> historyMap = new HashMap<String, Object>();
				if(!"".equals(school) && school!=null){
					historyMap.put("schoolCode", school);
				}else{
					historyMap.put("schoolCode", schoolCode);
				}
				
				historyMap.put("name",name);
				historyMap.put("direction", requestMap.get("school"));
				historyMap.put("updateTime", new Date());
				historyMap.put("updatePerson", loginName);
				historyMap.put("createTime", new Date());
				historyMap.put("createPerson", loginName);
				historyMap.put("applyTime", d);
				historyMap.put("proposer", proposer);
				historyMap.put("roleState", ConfigContext.getValue("framework.tmis.transfer['teacherHistory']"));
				teacherTransferService.insertHistory(historyMap);
		}
			
		
		
		out.print(getSerializer().formatMap(requestMap));
	}
	
}
