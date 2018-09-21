package data.academic.transfer.controller;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import data.academic.examNumberManagement.service.ExamNumberManageService;
import data.academic.studentManagement.service.StuManagementService;
import data.academic.transfer.service.StudentTransferService;
import data.academic.transfer.service.TeacherTransferService;
import data.academic.util.ExportUtil;
import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractBaseController;
import data.framework.support.ConfigContext;
import data.platform.authority.security.SecurityContext;
@RestController
@RequestMapping("transfer/studentTransfer")
public class StudentTransferController extends AbstractBaseController{
		
	 @Autowired
	 private StudentTransferService studentTransferService;
	 @Autowired
	 private ExamNumberManageService examNumberManageService;
	 @Autowired
	 private TeacherTransferService teacherTransferService;
	 @Autowired
	 private StuManagementService stuManagementService;
	@Override
	protected void init(ModelMap model, HttpServletRequest request) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * @Title: searchPaging
	 * @Description: 分页查询待调动的学生
	 * @author xiahuajun
	 * @date 2016年9月21日 
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=searchPaging")
	public void searchPaging(@RequestParam("data") String data,
			java.io.PrintWriter out) {
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		// Map<String, Object> paramMap = new HashMap<String, Object>();
		//boolean isFast = parseBoolean(requestMap.get("isFast"));
		//String examPaperName = "";
//		if (isFast) {
//			examPaperName = trimString(requestMap.get("q"));
//		}
		int currentPage = parseInteger(requestMap.get("page"));
		int pageSize = parseInteger(requestMap.get("rows"));
		String sortField = trimString(requestMap.get("sidx"));

		if (sortField.trim().length() == 0) {
			sortField = "Create_Time";
		}
		String sort = trimString("desc");
		PagingResult<Map<String, Object>> pagingResult = studentTransferService.serachPaging(requestMap, sortField, sort, currentPage,pageSize);
		List<Map<String, Object>> list = pagingResult.getRows();
		//[{School_Name=上海市青浦区沈巷中学, id=193BE99D-36D4-406A-9BF1-50D09B917096, rownum=1, Teacher_Name=陈晓虎, Teacher_Pk=482D3F92-22CC-4289-A1D3-35531036F211, Create_Time=2016-09-20 00:25:28.0, Login_Name=chenxiaohu, Detached=1, Create_Person=蔡娟芳}, {School_Name=上海市青浦区沈巷中学, id=2AC95CB9-A338-43B3-B803-0B493607F13A, rownum=2, Teacher_Name=陈笑盈, Teacher_Pk=A931D25C-1FFE-43C0-852B-6383A1DE21B1, Create_Time=2016-09-20 00:25:28.0, Login_Name=chenxiaoy, Detached=1, Create_Person=蔡娟芳}]
		List<Map<String, Object>> paramList = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : list) {
			//Map<String, Object> paramMap = new HashMap<String, Object>();
			//加入新去向 
			map.put("School_New_Adr", map.get("School_New_Adr"));
			map.put("Create_Time",
					formatDate(parseDate(map.get("Create_Time")), "yyyy-MM-dd"));
			map.put("Id", map.get("Stu_Pk").toString());
			paramList.add(map);
		}
		PagingResult<Map<String, Object>> newPagingResult = new PagingResult<Map<String, Object>>(
				paramList, pagingResult.getPage(), pagingResult.getPageSize(),
				pagingResult.getRecords());
		out.print(getSerializer().formatObject(newPagingResult));
	}
	
	@RequestMapping(params = "command=importExcel")
	public void searchImportPaging(@RequestParam("data") String data,
			HttpServletResponse response) {
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		String state=formatString(requestMap.get("state"));
		List<Map<String, Object>> list = studentTransferService.serachImportPaging(requestMap,state);
		//[{School_Name=上海市青浦区沈巷中学, id=193BE99D-36D4-406A-9BF1-50D09B917096, rownum=1, Teacher_Name=陈晓虎, Teacher_Pk=482D3F92-22CC-4289-A1D3-35531036F211, Create_Time=2016-09-20 00:25:28.0, Login_Name=chenxiaohu, Detached=1, Create_Person=蔡娟芳}, {School_Name=上海市青浦区沈巷中学, id=2AC95CB9-A338-43B3-B803-0B493607F13A, rownum=2, Teacher_Name=陈笑盈, Teacher_Pk=A931D25C-1FFE-43C0-852B-6383A1DE21B1, Create_Time=2016-09-20 00:25:28.0, Login_Name=chenxiaoy, Detached=1, Create_Person=蔡娟芳}]
		List<Map<String, Object>> paramList = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : list) {
			//Map<String, Object> paramMap = new HashMap<String, Object>();
			//加入新去向 
			map.put("School_New_Adr", map.get("School_New_Adr"));
			map.put("Create_Time",
					formatDate(parseDate(map.get("Create_Time")), "yyyy-MM-dd"));
			paramList.add(map);
		}
		String fileName="学生待调动记录.xls";
		String sheetName="学生待调动记录列表";
		String[] title={"序号","姓名","学校","申请人","去向","申请时间"};
		String[] key=new String[6];
		if("1".equals(state)){
			key[0]="xh";
			key[1]="Stu_Name";
			key[2]="School_Short_Name";
			key[3]="Create_Person";
			key[4]="School_New_Adr";
			key[5]="Create_Time";
			
		}else{
			key[0]="xh";
			key[1]="Teacher_Name";
			key[2]="School_Short_Name";
			key[3]="Create_Person";
			key[4]="School_New_Adr";
			key[5]="Create_Time";
			
		}
		
		ExportUtil.ExportExcel(response, title, fileName, sheetName, paramList, key);
		
	}
	
	/**
	 * @Title: submitApplyTeacher
	 * @Description: 本校管理员提交调度学生
	 * @author xiahuajun
	 * @date 2016年9月20日 
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=submitApplyStudent")
	public void submitApplyStudent(@RequestParam("data") String data,java.io.PrintWriter out) {
		Map<String, Object> paramMap = getSerializer().parseMap(data);
		//获取多条记录的id
		//List<String> list = (List<String>) paramMap.get("selArr");
		//查询当前用户的学校code(根据登录名)
		String loginName=SecurityContext.getPrincipal().getUsername();
		String schoolCode = examNumberManageService.getSchoolCodeByLoginName(loginName);
		paramMap.put("schoolCode", schoolCode);
		// 创建人
		String create_person = SecurityContext.getPrincipal().getChineseName();
		// 创建事间
		String create_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		//防止重复申请
		List<Map<String,Object>> repeatList = studentTransferService.findIsAlreadyApply(paramMap);
		for(Map<String,Object> map : repeatList){
			if("3".equals(map.get("State").toString())){
				paramMap.put("mess", "repeatApply");
				out.print(getSerializer().formatMap(paramMap));
				return;
			}
		}
		//调动前先查询此老师有没有被调走[{state=0}, {state=3}]
		List<Map<String,Object>> list = studentTransferService.findIsAlreadyTransfer(paramMap);
		for(Map<String,Object> map : list){
			if("2".equals(map.get("State").toString())){
				paramMap.put("mess", "notSubmit");
				out.print(getSerializer().formatMap(paramMap));
				return;
			}
		}
		paramMap.put("create_person", create_person);
		paramMap.put("create_time", create_time);
		studentTransferService.submitApply(paramMap);
		//添加申请人和申请时间
		studentTransferService.addCreatePerson(paramMap);
		paramMap.put("mess", "success");
		out.print(getSerializer().formatMap(paramMap));
	}
	
	/**
	 * @Title: cancelsubmitApplyTeacher
	 * @Description: 本校管理员撤销调动学生
	 * @author xiahuajun
	 * @date 2016年9月20日 
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=cancelsubmitApplyStudent")
	public void cancelsubmitApplyTeacher(@RequestParam("data") String data,java.io.PrintWriter out) {
		Map<String, Object> paramMap = getSerializer().parseMap(data);
		// 创建人
	//	String create_person = SecurityContext.getPrincipal().getChineseName();
		// 创建事间
	//	String create_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		//查询当前用户的学校code(根据登录名)
		//根据登录名查询schoolCode
		String loginName=SecurityContext.getPrincipal().getUsername();
		String schoolCode = examNumberManageService.getSchoolCodeByLoginName(loginName);
		paramMap.put("schoolCode", schoolCode);
		//撤销前先查询有没有未提交的调度[{state=0}, {state=3}]
		List<Map<String,Object>> list = studentTransferService.findIsSubmitApply(paramMap);
		for(Map<String,Object> map : list){
			if(! "3".equals(map.get("State").toString())){
				paramMap.put("mess", "notSubmit");
				out.print(getSerializer().formatMap(paramMap));
				return;
			}
		}
		//撤销调度申请
		studentTransferService.cancelSubmitApply(paramMap);
		paramMap.put("mess", "success");
		out.print(getSerializer().formatMap(paramMap));
	}
	
	/**
	 * @Title: submitTransfer
	 * @Description: 提交调动学生
	 * @author xiahuajun
	 * @date 2016年10月5日 
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
		Map<String, Object> paramMap=new HashMap<>();
		String loginName=SecurityContext.getPrincipal().getUsername();
		String id = null;
		String name = null;
		String proposer = null;
		List<String> list = new ArrayList<>();
		List<String> sfzjPkList = new ArrayList<>();
		if(requestMap.containsKey("roleCode")){
			id = trimString(requestMap.get("studentPk"));
			list.add(id);
			name = new String((trimString(requestMap.get("name"))).getBytes("iso-8859-1"),"utf-8");
			proposer = new String((trimString(requestMap.get("proposer"))).getBytes("iso-8859-1"),"utf-8");
		}else{
			list = (List<String>) requestMap.get("studentPk");
			name = trimString(requestMap.get("name"));
			proposer = trimString(requestMap.get("proposer"));
		}
		 
		String schoolCode = null,school=null,
				    schoolCodes = null;
		for(String str : list){
			schoolCode = studentTransferService.selectTeacherSchoolCode(str);
			//查询code校验教师是不是调回原校籍
		    paramMap.put("studentPk",str);
		    List<Map<String,Object>> stuInfoList=studentTransferService.getIdCardByStuPk(paramMap);
			paramMap.put("state", "2");
		    schoolCodes=teacherTransferService.getStuSchoolByState(paramMap);
		    paramMap.put("state","");
			paramMap.put("notState","3");
			school=teacherTransferService.getStuSchoolByState(paramMap);
			paramMap = new HashMap<String, Object>();
			paramMap.put("studentPk", str);
			paramMap.put("schoolCode", schoolCode);
			
			if(schoolCode.equals(requestMap.get("school").toString())){
				if(!"".equals(schoolCodes) && schoolCodes!=null){
					if(schoolCodes.equals(requestMap.get("school").toString())){
						//删除该学生和其它学校的关系表数据
						studentTransferService.deleterefStudentSchool(str);
						//添加一条状态为0的新数据
						studentTransferService.insertRefStudentSchool(paramMap);
						for (Map<String, Object> map : stuInfoList) {
							String idCard=map.get("SFZJH").toString();
							sfzjPkList.add(idCard);
						}
						paramMap.put("studentIds", sfzjPkList);
						paramMap.put("classId", "");
						paramMap.put("updatePerson", loginName);
						 stuManagementService.updateClassByStudentId(paramMap);
						requestMap.put("mess","success");
					}else{
						requestMap.put("mess","false");
					}
				}else{
					requestMap.put("mess","false");
				}
			}else {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("studentPk", str);
				map.put("uuid", UUID.randomUUID().toString());
				map.put("school",requestMap.get("school").toString());
				for (Map<String, Object> stuMap : stuInfoList) {
					String idCard=stuMap.get("SFZJH").toString();
					sfzjPkList.add(idCard);
				}
				paramMap.put("studentIds", sfzjPkList);
				paramMap.put("classId", "");
				paramMap.put("updatePerson", loginName);
				 stuManagementService.updateClassByStudentId(paramMap);
				//添加被调动老师与新学校的关联关系teacher_id School_code state=4(调入)
				studentTransferService.submitTransferInfo(map);
				//修改被调动老师在原学校的状态 state=2(调出)
				studentTransferService.updateStudentState(str);
				requestMap.put("mess","success");
			}
		}
		if("success".equals(requestMap.get("mess").toString())){
			Map<String, Object> historyMap = new HashMap<String, Object>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date d = sdf.parse(trimString(requestMap.get("applyTime")));
			 if(!"".equals(school) && school!=null){
					historyMap.put("schoolCode", school);
				}else{
					historyMap.put("schoolCode", schoolCode);
				}
			historyMap.put("name", name);
			historyMap.put("direction", requestMap.get("school"));
			historyMap.put("updateTime", new Date());
			historyMap.put("updatePerson", loginName);
			historyMap.put("createTime", new Date());
			historyMap.put("createPerson", loginName);
			historyMap.put("applyTime", d);
			historyMap.put("proposer", proposer);
			historyMap.put("roleState", ConfigContext.getValue("framework.tmis.transfer['studentHistory']"));
			teacherTransferService.insertHistory(historyMap);
		}
		out.print(getSerializer().formatMap(requestMap));
	}
	
	
	
	/**
	 * @Title: getStuAndTeacherTrans
	 * @Description:  得到待调动的学生和老师 
	 * @author zhaohuanhuan
	 * @date 2016年12月6日 
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=getStuAndTeacherTrans")
	public void getStuAndTeacherTrans(@RequestParam("data") String data,java.io.PrintWriter out) {
		 List<Map<String, Object>> transList=studentTransferService.getStuAndTeacherTrans();
		out.print(getSerializer().formatList(transList));
	}
	
}
