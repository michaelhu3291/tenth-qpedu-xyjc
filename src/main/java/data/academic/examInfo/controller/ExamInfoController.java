package data.academic.examInfo.controller;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import data.academic.examInfo.entity.EntityExamInfo;
import data.academic.examInfo.service.ExamInfoSchoolService;
import data.academic.examInfo.service.ExamInfoService;
import data.academic.examInfo.service.MarkingArrangementService;
import data.academic.examNumberManagement.service.ExamNumberManageService;
import data.academic.statisticsAnalysis.service.SchoolPlainAdminScoreSearchService;
import data.academic.util.ExportUtil;
import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractBaseController;
import data.framework.support.ConfigContext;
import data.platform.authority.security.SecurityContext;
import data.platform.service.PlatformAccessoryService;
import data.platform.service.PlatformDataDictionaryService;

/**
 * @Title: ExamInfoController
 * @Description: 考试管理控制层
 * @author zhaohuanhuan
 * @date 2016年10月12日 下午1:55:38
 */
@RestController
@RequestMapping("examInfo/examManage")
public class ExamInfoController extends AbstractBaseController {
	@Autowired
	private ExamInfoService examInfoService;
	@Autowired
	private ExamNumberManageService examNumberManageService;
	@Autowired
	private PlatformAccessoryService accessoryService;
	@Autowired
	private ExamInfoSchoolService examInfoSchoolService;
	@Autowired
	private SchoolPlainAdminScoreSearchService schoolPlainAdminScoreSearchService;
	@Autowired
	 private PlatformDataDictionaryService dictionaryService;

	@Override
	protected void init(ModelMap model, HttpServletRequest request) {
		// TODO Auto-generated method stub
	}

	/**
	 * 添加考试 2016年7月28日 xiahuajun
	 * @throws Exception 
	 */
	@RequestMapping(params = "command=addExam")
	public void addExam(@RequestParam("data") String data,
			java.io.PrintWriter out) throws Exception {
		Map<String, Object> paramMap = getSerializer().parseMap(data);
		String schoolYear = paramMap.get("schoolYear").toString();
		String gradeTxt = paramMap.get("gradeTxt").toString();
		String grade = paramMap.get("grade").toString();
		String schoolCode = "";
		String userId=SecurityContext.getPrincipal().getId();
		//根据登录人的id得到其角色code
	    String roleCode=examInfoService.getRoleByUserId(userId);
	    String loginName=SecurityContext.getPrincipal().getUsername();
	
		if(ConfigContext.getValue("framework.tmis.roleCode['qpRoleAdminCode']").equals(roleCode)){
			schoolCode = ConfigContext.getValue("framework.tmis.roleCode['qpOrgCode']");
		} else {
			//根据登录名得到学ode
			schoolCode = examNumberManageService.getSchoolCodeByLoginName(loginName);
		}
		String exam_name = "";
		String term = "";
		String termId="";
		//考试编号
		String examNumber = "";
		String examType = "";
		String examTypeId="";
		if(paramMap.get("term").toString().equals("sxq")){
			term = "第一学期";
			termId="0";
		}
		else if(paramMap.get("term").toString().equals("xxq")){
			term = "第二学期";
			termId="1";
		}
		if(paramMap.get("examType").toString().equals("qz")){
			examType = "期中";
			examTypeId="01";
		}
		else if(paramMap.get("examType").toString().equals("qm")){
			examType = "期末";
			examTypeId="02";
		}
		exam_name = schoolYear + "学年" + gradeTxt + term + examType +"测试";
		//生成考试编号
	  String examTimeStr=paramMap.get("exam_time").toString().replace("-", "");
	  examTimeStr=examTimeStr.substring(examTimeStr.length()-4, examTimeStr.length());
		StringBuffer sb = new StringBuffer();
		sb.append(examTimeStr);
		sb.append(termId);
		sb.append(examTypeId);
		sb.append(grade);
		Integer numStr;
		String examNumberMax="";
		paramMap.put("exam_name", exam_name);
		paramMap.put("schoolCode", schoolCode);
		paramMap.put("closingTime", paramMap.get("closingTime"));//考号截止时间
		paramMap.put("introducedTime",  paramMap.get("introducedTime"));//考试发布时间
		paramMap.put("introducedState",  paramMap.get("introducedState"));//考试发布状态
		//TODO 
		if(paramMap.containsKey("cousre")){
			paramMap.put("cousre","");
		}else{
			paramMap.put("cousre",  paramMap.get("cousre"));
		}
			if (paramMap.get("id") == null || "".equals(paramMap.get("id"))) {
				if(ConfigContext.getValue("framework.tmis.roleCode['qpOrgCode']").equals(schoolCode)){
					numStr=1001;
					//得到登录人code得到存在考号中最大的数据
					examNumberMax=examInfoService.getExamNumberBySchoolCode(schoolCode);
					if(null!=examNumberMax){
						if(parseInteger(examNumberMax)>1998){
							if(parseInteger(examNumberMax)>9000){
								numStr=parseInteger(examNumberMax)+1;
							}else{
								numStr=9000;
								numStr=numStr+1;
							}
						}
						numStr=parseInteger(examNumberMax)+1;
					}
					examNumberMax=parseString(numStr);
				}
				if(parseInteger(examNumberMax)>9998){
					paramMap.put("mess", "examNumberMax");
					out.print(getSerializer().formatMap(paramMap));
					return;
				}
				sb.append(examNumberMax);
				examNumber =  sb.toString();	
				paramMap.put("examNumber", examNumber);
				// 创建人
				String create_person = SecurityContext.getPrincipal().getId();
				// 创建事间
				String create_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
						.format(new Date());
				paramMap.put("create_person", create_person);
				paramMap.put("create_time", create_time);
				  //关联附件
				List<Map<String,Object>> fileList = (List<Map<String,Object>>)paramMap.get( "files" ) ;
			    List<String> fileIds = new ArrayList<String>() ;
			    if( fileList != null && !fileList.isEmpty() )
			        {
			            for( Map<String,Object> fileMap : fileList )
			            {
			                fileIds.add( parseString( fileMap.get( "id" ) ) ) ;
			            }
			            //关联附件
			         accessoryService.associated(examNumber, fileIds ) ;
			        }
				Map<String, Object>paramMaps=new HashMap<>();
			//得到选择的学校
				List<Map<String,Object>> schoolList = (List<Map<String,Object>>)paramMap.get( "school" ) ;
				 if( schoolList != null && !schoolList.isEmpty() )
			        {
			            for( Map<String,Object> schoolMap : schoolList )
			            {
			            	paramMaps.put("schoolCode", schoolMap.get("schoolVal"));
			            	paramMaps.put("schoolName", schoolMap.get("schoolText"));
			            	if(schoolMap.containsKey("brevityCode") && !"".equals(schoolMap.get("brevityCode"))){
			            		paramMaps.put("brevityCode",parseInteger(schoolMap.get("brevityCode")) );
			            	}else{
			            		paramMaps.put("brevityCode",null );
			            	}
			            	
			            	paramMaps.put("examCode", examNumber);
			            	//添加关系表，学校code和考号
							 examNumberManageService.insertschoolCodeExamNumber(paramMaps);
			            }
			        }
				Object introducedTime=paramMap.get("introducedTime");
				if("".equals(introducedTime) || null==introducedTime){
					introducedTime=null;
				}
		
				paramMap.put("introducedTime", introducedTime);
				//添加考试信息
				examInfoService.addExam(paramMap);
				List<Map<String, Object>> courseExamParamList=(List<Map<String, Object>>) paramMap.get("params");
			    for (Map<String, Object> courseExamParamMap : courseExamParamList) {
			    	courseExamParamMap.put("examCode", examNumber);
			    	courseExamParamMap.put("createPerson", create_person);
			    	courseExamParamMap.put("createTime", create_time);
			    	courseExamParamMap.put("classId", null);
					Object markingTime=courseExamParamMap.get("markingTime");
					if("".equals(markingTime) || null==markingTime){
						markingTime=null;
					}
					courseExamParamMap.put("markingTime", markingTime);
			    	//添加考试科目关系表
					//TODO 查数据字典分数
					List<Integer> scoreSegement = examInfoSchoolService.searchScoreSegement(parseString(courseExamParamMap.get("zf")));
					courseExamParamMap.put("zf", scoreSegement.get(3));
					courseExamParamMap.put("yx", scoreSegement.get(2));
					courseExamParamMap.put("lh", scoreSegement.get(1));
					courseExamParamMap.put("jg", scoreSegement.get(0));
			    	examInfoSchoolService.addExamCalssCourse(courseExamParamMap);
				}
				paramMap.put("mess", "addSuccess");
			} else {
				Map<String,Object> updateMap = new HashMap<String,Object>();
				List<String> paramList = new ArrayList<>();
				String examCode="";
				//获取到修改之前的考试编号后四位，用于修改考试编号
				if(paramMap.containsKey("oldExamCode")){
					examCode=paramMap.get("oldExamCode").toString();
					examCode=examCode.substring(examCode.length()-4, examCode.length());
				}
				sb.append(examCode);
				examNumber =  sb.toString();
				paramMap.put("examNumber", examNumber);
				paramList.add(paramMap.get("id").toString());
				updateMap.put("selArr", paramList);
				//修改考试前判断考号有无生成
				List<Map<String,Object>> list = examInfoService.selectExamNumberIsExist(updateMap);
				if(list != null && list.size() > 0){
					paramMap.put("mess", "notUpdate");
					out.print(getSerializer().formatMap(paramMap));
					return;
				}
				if(paramMap.containsKey("oldExamCode")){
					List<String> examCodeList=new ArrayList<>();
					if("".equals(paramMap.get("oldExamCode").toString()) || null!=paramMap.get("oldExamCode").toString()){
						examCodeList.add(paramMap.get("oldExamCode").toString());
						//删除与考试相关的学校
						examInfoService.deleteExamRefSchool(examCodeList);
						Map<String, Object> filesMap=new HashMap<>();
						filesMap.put("examNumber", examNumber);
						filesMap.put("oldExamCode", paramMap.get("oldExamCode"));
						//更新与 相关的附件的考试 编号 
						examInfoService.updateExamRefAccessory(filesMap);
						examInfoSchoolService.deleteCourseClassByExamCode(examCodeList);
					}
				}
				
				// 修改人
				String update_person = SecurityContext.getPrincipal().getId();
				// 修改时间
				String update_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
						.format(new Date());
				paramMap.put("update_person", update_person);
				paramMap.put("update_time", update_time);
				//关联附件
				List<Map<String,Object>> fileList = (List<Map<String,Object>>)paramMap.get( "files" ) ;
			    List<String> fileIds = new ArrayList<String>() ;
			    if( fileList != null && !fileList.isEmpty() )
			        {
			            for( Map<String,Object> fileMap : fileList )
			            {
			                fileIds.add( parseString( fileMap.get( "id" ) ) ) ;
			            }
			         accessoryService.associated(examNumber, fileIds ) ;
			        }
				Map<String, Object>paramMaps=new HashMap<>();
				//得到重新选中的学校
				List<Map<String,Object>> schoolList = (List<Map<String,Object>>)paramMap.get( "school" ) ;
				 if( schoolList != null && !schoolList.isEmpty() )
			        {
			            for( Map<String,Object> schoolMap : schoolList )
			            {
			            	paramMaps.put("schoolCode", schoolMap.get("schoolVal"));
			            	paramMaps.put("schoolName", schoolMap.get("schoolText"));
			            	if(schoolMap.containsKey("brevityCode") && !"".equals(schoolMap.get("brevityCode"))){
			            		paramMaps.put("brevityCode",parseInteger(schoolMap.get("brevityCode")) );
			            	}else{
			            		paramMaps.put("brevityCode",null );
			            	}
			            	
			            	paramMaps.put("examCode", examNumber);
			            	//添加相关的学校
							 examNumberManageService.insertschoolCodeExamNumber(paramMaps);
			            }
			        }
					Object introducedTime= paramMap.get("introducedTime");	
					if("".equals(introducedTime) || null==introducedTime){
						introducedTime=null;
					}
					paramMap.put("introducedTime", introducedTime);
				//修改考试信息
			   examInfoService.updateExamById(paramMap);
			   //添加考试科目关系数据
				List<Map<String, Object>> courseExamParamList=(List<Map<String, Object>>) paramMap.get("params");
			    for (Map<String, Object> courseExamParamMap : courseExamParamList) {
			    	courseExamParamMap.put("examCode", examNumber);
			    	courseExamParamMap.put("createPerson", update_person);
			    	courseExamParamMap.put("createTime", update_time);
			    	courseExamParamMap.put("classId", null);
			    	Object markingTime=courseExamParamMap.get("markingTime");
					if("".equals(markingTime) || null==markingTime){
						markingTime=null;
					}
					courseExamParamMap.put("markingTime", markingTime);
			    	//添加考试科目关系表
					List<Integer> scoreSegement = examInfoSchoolService.searchScoreSegement(parseString(courseExamParamMap.get("zf")));
					courseExamParamMap.put("zf", scoreSegement.get(3));
					courseExamParamMap.put("yx", scoreSegement.get(2));
					courseExamParamMap.put("lh", scoreSegement.get(1));
					courseExamParamMap.put("jg", scoreSegement.get(0));
			    	 examInfoSchoolService.addExamCalssCourse(courseExamParamMap);
				}
				paramMap.put("mess", "updateSuccess");
			}
		
		
		out.print(getSerializer().formatMap(paramMap));
	}

	/**
	 * @Title: searchPaging
	 * @Description: 区和学校的考试管理分页显示
	 * @author zhaohuanhuan
	 * @date 2016年10月18日 
	 * @param data
	 * @param out
	 * @return void
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(params = "command=searchPaging")
	public void searchPaging(@RequestParam("data") String data,
			java.io.PrintWriter out, HttpServletRequest request) throws UnsupportedEncodingException {
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		boolean isFast = parseBoolean(requestMap.get("isFast"));
		String examName = "";
		if (isFast) {
			examName = trimString(requestMap.get("q"));
		}
		requestMap.put("examName", examName);
		int currentPage = parseInteger(requestMap.get("page"));
		int pageSize = parseInteger(requestMap.get("rows"));
		String sortField = trimString(requestMap.get("sidx"));
		if (sortField.trim().length() == 0) {
			sortField = "Exam_Time";
		}
		String sort = trimString("desc");
		String schoolCode = "";
	    String userId=SecurityContext.getPrincipal().getId();
	    //根据用户id得到用户的角色
	    String roleCode=examInfoService.getRoleByUserId(userId);
		List<String> gradeIdList=new ArrayList<>();
		if(ConfigContext.getValue("framework.tmis.roleCode['qpRoleAdminCode']").equals(roleCode)){
			schoolCode = ConfigContext.getValue("framework.tmis.roleCode['qpOrgCode']");
		} else{
			if("".equals(parseString(requestMap.get("schoolCode")))){
				//根据登录名得到学校code
				schoolCode = examNumberManageService.getSchoolCodeByLoginName(SecurityContext.getPrincipal().getUsername());
			}else{
				schoolCode=parseString(requestMap.get("schoolCode"));
			}
			//根据当前登录的用户名得到学校code用于得到年级
			String schoolCodes=examNumberManageService.getSchoolCodeByLoginName(SecurityContext.getPrincipal().getUsername());
			//根据用户名得到学校类型
			String schoolSequence = schoolPlainAdminScoreSearchService.findSchoolTypeBySchoolCode(schoolCodes);
		    String schoolTypeCode="";
			if("0".equals(schoolSequence)){
				schoolTypeCode="xx";
			}
			else if("1".equals(schoolSequence)){
				schoolTypeCode="cz";
			}
			else if("2".equals(schoolSequence) || "3".equals(schoolSequence) || "4".equals(schoolSequence) ){
				schoolTypeCode="gz";
				if("3062".equals(schoolCodes) ){
					schoolTypeCode="cz";
				}
				if("3004".equals(schoolCodes) ){
					schoolTypeCode="wz";
				}
			}
			else if("5".equals(schoolSequence)){
				schoolTypeCode="ygz";
			}
			//根据学校类型查询年级
			List<Map<String, Object>> gradeList = dictionaryService.getGradesBySchoolType(schoolTypeCode);
		    for (Map<String, Object> map : gradeList) {
		    	String gradeId=map.get("DictionaryCode").toString();
				gradeIdList.add(gradeId);
			}
		}
		
		requestMap.put("gradeIdList",gradeIdList);
		requestMap.put("schoolCode", schoolCode);
		
		  Date date=new Date();
		  DateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		  String time=format.format(date);
		 Map<String, Object> timeMap=new HashMap<>();
		 timeMap.put("getTime", time);
		 //根据发布时间更新发布状态 
		 examNumberManageService.updateIntroducedStateByIntroducedTime(timeMap);
		 if(!requestMap.containsKey("introducedState")){
			 requestMap.put("introducedState", "");
		 }
		PagingResult<Map<String, Object>> pagingResult = examInfoService
				.serachExamPaging(requestMap, sortField, sort, currentPage,
						pageSize);
		List<Map<String, Object>> list = pagingResult.getRows();
		List<Map<String, Object>> paramList = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : list) {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("Exam_Time",
					formatDate(parseDate(map.get("Exam_Time")), "yyyy-MM-dd"));
			paramMap.put("Exam_Name", map.get("Exam_Name").toString());
			paramMap.put("Id", map.get("Id").toString());
			paramMap.put("Grade_Code", map.get("Grade_Code"));
			paramMap.put("Introduced_State", map.get("Introduced_State"));
			if(! map.containsKey("Exam_Number")){
				paramMap.put("examNumber", null);
			} else {
				paramMap.put("examNumber", map.get("Exam_Number").toString());
			}
			Map<String, Object> examMap = new HashMap<String, Object>();
			examMap.put("examCode", map.get("Exam_Number"));
			//得到某次考试下面的科目考试的开始时间和结束时间
			 List<Map<String, Object>> courseTime=examInfoSchoolService.getCourseStartTimeAndEndTime(examMap);
			if(courseTime.size()>0){
				for (Map<String, Object> map2 : courseTime) {
					 String startStrTime=parseString(map2.get("startTime"));
					 String endStrTime=parseString(map2.get("endTime"));
					 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						try {
							Date startTime=sdf.parse(startStrTime);
							Date endTime=sdf.parse(endStrTime);
							Date date1=sdf.parse(time);
							if(date1.before(startTime)){
								paramMap.put("examState", "0");
							}
							if(startTime.before(date1) && date.before(date1) || date1.equals(endTime)) {
								paramMap.put("examState", "1");
							}
							if(date1.after(endTime)){
								paramMap.put("examState", "2");
							}
						} catch (ParseException e) {
							e.printStackTrace();
						}
				}
			}
			 
			paramList.add(paramMap);
		}
		PagingResult<Map<String, Object>> newPagingResult = new PagingResult<Map<String, Object>>(
				paramList, pagingResult.getPage(), pagingResult.getPageSize(),
				pagingResult.getRecords());
		out.print(getSerializer().formatObject(newPagingResult));
	}
	
	/**
	 * 
	 * @Title: importExcel
	 * @Description: 导出
	 * @author jay zhong
	 * @date 2017年11月21日 下午1:33:40 
	 * @return void
	 *
	 * @param data
	 * @param response
	 */
	@RequestMapping(params = "command=importExcel")
	public void importExcel(@RequestParam( "data" ) String data,HttpServletResponse response) 
	{	
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		String examName = formatString(requestMap.get("q"));
		requestMap.put("examName", examName);
		String schoolCode = "";
	    String userId=SecurityContext.getPrincipal().getId();
	    //根据用户id得到用户的角色
	    String roleCode=examInfoService.getRoleByUserId(userId);
		List<String> gradeIdList=new ArrayList<>();
		if(ConfigContext.getValue("framework.tmis.roleCode['qpRoleAdminCode']").equals(roleCode)){
			schoolCode = ConfigContext.getValue("framework.tmis.roleCode['qpOrgCode']");
		} else{
			if("".equals(parseString(requestMap.get("schoolCode")))){
				//根据登录名得到学校code
				schoolCode = examNumberManageService.getSchoolCodeByLoginName(SecurityContext.getPrincipal().getUsername());
			}else{
				schoolCode=parseString(requestMap.get("schoolCode"));
			}
			//根据当前登录的用户名得到学校code用于得到年级
			String schoolCodes=examNumberManageService.getSchoolCodeByLoginName(SecurityContext.getPrincipal().getUsername());
			//根据用户名得到学校类型
			String schoolSequence = schoolPlainAdminScoreSearchService.findSchoolTypeBySchoolCode(schoolCodes);
		    String schoolTypeCode="";
			if("0".equals(schoolSequence)){
				schoolTypeCode="xx";
			}
			else if("1".equals(schoolSequence)){
				schoolTypeCode="cz";
			}
			else if("2".equals(schoolSequence) || "3".equals(schoolSequence) || "4".equals(schoolSequence) ){
				schoolTypeCode="gz";
				if("3062".equals(schoolCodes) ){
					schoolTypeCode="cz";
				}
				if("3004".equals(schoolCodes) ){
					schoolTypeCode="wz";
				}
			}
			else if("5".equals(schoolSequence)){
				schoolTypeCode="ygz";
			}
			//根据学校类型查询年级
			List<Map<String, Object>> gradeList = dictionaryService.getGradesBySchoolType(schoolTypeCode);
		    for (Map<String, Object> map : gradeList) {
		    	String gradeId=map.get("DictionaryCode").toString();
				gradeIdList.add(gradeId);
			}
		}
		
		requestMap.put("gradeIdList",gradeIdList);
		requestMap.put("schoolCode", schoolCode);
		
		  Date date=new Date();
		  DateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		  String time=format.format(date);
		 Map<String, Object> timeMap=new HashMap<>();
		 timeMap.put("getTime", time);
		 //根据发布时间更新发布状态 
		 examNumberManageService.updateIntroducedStateByIntroducedTime(timeMap);
		 if(!requestMap.containsKey("introducedState")){
			 requestMap.put("introducedState", "");
		 }
		 List<Map<String, Object>> list = examInfoService
				.serachImportExamPaging(requestMap);
		List<Map<String, Object>> paramList = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : list) {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("Exam_Time",
					formatDate(parseDate(map.get("Exam_Time")), "yyyy-MM-dd"));
			paramMap.put("Exam_Name", map.get("Exam_Name").toString());
			paramMap.put("Id", map.get("Id").toString());
			paramMap.put("Grade_Code", map.get("Grade_Code"));
			paramMap.put("Introduced_State", map.get("Introduced_State"));
			if(! map.containsKey("Exam_Number")){
				paramMap.put("examNumber", null);
			} else {
				paramMap.put("examNumber", map.get("Exam_Number").toString());
			}
			Map<String, Object> examMap = new HashMap<String, Object>();
			examMap.put("examCode", map.get("Exam_Number"));
			//得到某次考试下面的科目考试的开始时间和结束时间
			 List<Map<String, Object>> courseTime=examInfoSchoolService.getCourseStartTimeAndEndTime(examMap);
			if(courseTime.size()>0){
				for (Map<String, Object> map2 : courseTime) {
					 String startStrTime=parseString(map2.get("startTime"));
					 String endStrTime=parseString(map2.get("endTime"));
					 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						try {
							Date startTime=sdf.parse(startStrTime);
							Date endTime=sdf.parse(endStrTime);
							Date date1=sdf.parse(time);
							if(date1.before(startTime)){
								paramMap.put("examState", "0");
							}
							if(startTime.before(date1) && date.before(date1) || date1.equals(endTime)) {
								paramMap.put("examState", "1");
							}
							if(date1.after(endTime)){
								paramMap.put("examState", "2");
							}
						} catch (ParseException e) {
							e.printStackTrace();
						}
				}
			}
			 
			paramList.add(paramMap);
		}
		String fileName="测试列表.xls";
		String sheetName="测试列表详情";
		String[] title={"序号","测试日期","测试编号","测试名称"};
		String[] key={"xh","Exam_Time","examNumber","Exam_Name"};
		ExportUtil.ExportExcel(response, title, fileName, sheetName, paramList, key);
				
		
	}

	/**
	 * 删除考试 2016年7月29日 xiahuajun
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(params = "command=delete")
	public void delete(@RequestParam("data") String data,
			java.io.PrintWriter out) {
		Map<String, Object> paramMap = getSerializer().parseMap(data);
		//删除考试前判断考号有无生成
		List<Map<String,Object>> list = examInfoService.selectExamNumberIsExist(paramMap);
		if(list != null && list.size() > 0){
			paramMap.put("mess", "notDelete");
			out.print(getSerializer().formatMap(paramMap));
			return;
		}
		List<String> examNumberList=(List<String>) paramMap.get("examNumberList");
		if(examNumberList.size()>0){
			examInfoService.deleteAccessory(examNumberList);
			//删除与选中考试相关的学校
			examInfoService.deleteExamRefSchool(examNumberList);
			//删除与选中考试相关的班级科目
			examInfoSchoolService.deleteCourseClassByExamCode(examNumberList);
		}
		examInfoService.remove((List<String>) paramMap.get("selArr"));
		paramMap.put("mess", "success");
		out.print(getSerializer().formatMap(paramMap));
	}
	
	/**
	 * 通过id把数据查出来展现给用户以便修改 2016年7月29日 xiahuajun
	 */
	@RequestMapping(params = "command=selectExamById")
	public void selectExamById(@RequestParam("data") String data,
			java.io.PrintWriter out) {
		Map<String, Object> paramMap = getSerializer().parseMap(data);
		EntityExamInfo entityExamInfo = examInfoService
				.selectExamById(paramMap);
		out.print(getSerializer().formatObject(entityExamInfo));
	}
	/**
	 * @Title: getUserRole
	 * @Description: 得到用户角色
	 * @author zhaohuanhuan
	 * @date 2016年10月17日 
	 * @param examCode
	 * @return
	 * @return List<Map<String,Object>>
	 */
	@RequestMapping(params = "command=getUserRole")
	public void getUserRole(@RequestParam("data") String data,
			java.io.PrintWriter out) {
		String userId=SecurityContext.getPrincipal().getId();
	    String roleCode=examInfoService.getRoleByUserId(userId);
		out.print(getSerializer().formatObject(roleCode));
	}
	
	
	/**
	 * @Title: getParIdByDicCode
	 * @Description: 得到学校code和学校名称（根据年级code）
	 * @author zhaohuanhuan
	 * @date 2016年10月18日 
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=getParIdByDicCode")
	public void getParIdByDicCode(@RequestParam("data") String data, java.io.PrintWriter out) {
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		
		List<Map<String, Object>> ParIdList = examInfoService.getParentDictionaryByDicCode(requestMap.get("grade").toString());
		List<String> idList=new ArrayList<>();
		for (Map<String, Object> map : ParIdList) {
			idList.add(map.get("ParentDictionary").toString());
		}
		Map<String, Object> paramsMap=new HashMap<>();
		paramsMap.put("idList", idList);
		List<Map<String, Object>> schoolTypeCodeList=examInfoService.getParIdByDicCode(paramsMap);
		List<String> schoolTypeList=new ArrayList<>();
		Map<String, Object> paramMap=new HashMap<>();
		for (Map<String, Object> map : schoolTypeCodeList) {
			String schoolType=map.get("DictionaryCode").toString();
			if("xx".equals(schoolType)){
				schoolTypeList.add("0");
			}
			if("cz".equals(schoolType)){
				schoolTypeList.add("1");
			}
			if("gz".equals(schoolType)){
				schoolTypeList.add("2");
				schoolTypeList.add("3");
				schoolTypeList.add("4");
			}
			if("ygz".equals(schoolType)){
				schoolTypeList.add("0");
				schoolTypeList.add("1");
				schoolTypeList.add("5");
			}
			if("wx".equals(schoolType)){
				schoolTypeList.add("1");
				schoolTypeList.add("2");
				schoolTypeList.add("3");
				schoolTypeList.add("4");
			}
			
		}
		paramMap.put("schoolTypeList", schoolTypeList);
		List<Map<String, Object> > list=examInfoService.getSchoolCodeAndShoolName(paramMap);
		out.print(getSerializer().formatList(list));
	}
	
	
	/**
	 * @Title: selectSchoolByExamCode
	 * @Description: 根据考试编号得到学校
	 * @author zhaohuanhuan
	 * @date 2016年10月31日 
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=selectSchoolByExamCode")
	public void selectSchoolByExamCode(@RequestParam("data") String data,
			java.io.PrintWriter out) {
		Map<String, Object> paramMap = getSerializer().parseMap(data);
		List<Map<String, Object>> list=examInfoService.selectSchoolByExamCode(paramMap.get("examCode").toString());
		out.print(getSerializer().formatList(list));
	}
	
	
	
	/**
	 * @Title: getCourseByGrade
	 * @Description: 根据年级得到科目
	 * @author zhaohuanhuan
	 * @date 2016年10月31日 
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=getCourseByGrade")
	public void getCourseByGrade(@RequestParam("data") String data, java.io.PrintWriter out){
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		//根据年级得到其父字典项的id
		List<Map<String, Object>> parIdMapList=examInfoService.getParentDictionaryByDicCode(trimString(requestMap.get("gradeId")));
	       List<String> parIdList=new ArrayList<String>();
			for (Map<String, Object> map : parIdMapList) {
				parIdList.add(trimString(map.get("ParentDictionary")));
			}
			requestMap.put("parentIdList",parIdList);
			List<Map<String, Object>> courseList = markingArrangementService.showArrangementByExam(requestMap);
			 out.print(getSerializer().formatList(courseList));
	}
	
	
	@Autowired
	private MarkingArrangementService markingArrangementService;
	
}
