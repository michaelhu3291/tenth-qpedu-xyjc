/**
 * 2016年10月18日
 */
package data.academic.examInfo.controller;

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

import data.academic.examInfo.entity.EntityExamInfo;
import data.academic.examInfo.service.ExamInfoSchoolService;
import data.academic.examInfo.service.ExamInfoService;
import data.academic.examNumberManagement.service.ExamNumberManageService;
import data.academic.teacherManagement.service.TeacherManagementService;
import data.framework.support.AbstractBaseController;
import data.framework.support.ConfigContext;
import data.platform.authority.security.SecurityContext;
import data.platform.service.PlatformAccessoryService;

/**
 * @Title: ExamInfoSchoolController
 * @Description: 学校考试管理控制层
 * @author zhaohuanhuan
 * @date 2016年10月18日 下午1:45:11
 */
@RestController
@RequestMapping("examInfo/examManageSchool")
public class ExamInfoSchoolController  extends AbstractBaseController{

	@Override
	protected void init(ModelMap model, HttpServletRequest request) {
	}
	@RequestMapping(params = "command=addSchoolExam")
	public void addSchoolExam(@RequestParam("data") String data,
			java.io.PrintWriter out) throws Exception {
		Map<String, Object> paramMap = getSerializer().parseMap(data);
		String schoolYear = paramMap.get("schoolYear").toString();
		String gradeTxt = paramMap.get("gradeTxt").toString();
		String grade = paramMap.get("grade").toString();
		String schoolCode = "";
		String loginName=SecurityContext.getPrincipal().getUsername();
		Map<String, Object> schoolMap=new HashMap<>();
		if("qpAdmin".equals(SecurityContext.getPrincipal().getUsername())){
			schoolCode = ConfigContext.getValue("framework.tmis.roleCode['qpOrgCode']");
		} else {
			schoolCode = examNumberManageService.getSchoolCodeByLoginName(loginName);
		}
		 schoolMap.put("schoolCode", schoolCode);
		 schoolMap.put("schoolName", "");
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
		sb.append(examTimeStr)
		.append(termId)
		.append(examTypeId)
		.append(grade);
		Integer numStr;
		String examNumberMax="";
		paramMap.put("exam_name", exam_name);
		paramMap.put("schoolCode", schoolCode);
		paramMap.put("closingTime", paramMap.get("closingTime"));//考号截止时间
		paramMap.put("introducedTime",  paramMap.get("introducedTime"));//考试发布时间
		paramMap.put("introducedState",  paramMap.get("introducedState"));//考试发布状态
		//List<Map<String,Object>> courseList =new ArrayList<>();
		List<Map<String,Object>> classList =new ArrayList<>();
		Map<String, Object> paramMaps=new HashMap<>();
		if (paramMap.get("id") == null || "".equals(paramMap.get("id"))) {
				if(!ConfigContext.getValue("framework.tmis.roleCode['qpOrgCode']").equals(schoolCode)){
					numStr=2001;
					examNumberMax=examInfoSchoolService.getExamNumberByNotSchoolCode(ConfigContext.getValue("framework.tmis.roleCode['qpOrgCode']"));
					if(null!=examNumberMax){
							if(parseInteger(examNumberMax)>8999){
								paramMap.put("mess", "examNumberMax");
								out.print(getSerializer().formatMap(paramMap));
								return;
							}else{
								numStr=parseInteger(examNumberMax)+1;
							}
						numStr=parseInteger(examNumberMax)+1;
					}
					examNumberMax=parseString(numStr);
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
				 schoolMap.put("examCode", examNumber);
		        	//添加关系表，学校code和考号
					 examNumberManageService.insertschoolCodeExamNumber(schoolMap);
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
					    
					    
					    List<Map<String, Object>> courseExamParamList=(List<Map<String, Object>>) paramMap.get("params");
					    classList = (List<Map<String,Object>>)paramMap.get( "classId" ) ;
					    paramMaps=new HashMap<>();
						if( courseExamParamList != null ){
							if(classList.size()>0){
								   for( Map<String,Object> courseMap : courseExamParamList )
						            {
									   List<Integer> scoreSegement = examInfoSchoolService.searchScoreSegement(parseString(courseMap.get("zf")));
									    courseMap.put("zf", scoreSegement.get(3));
									    courseMap.put("yx", scoreSegement.get(2));
									    courseMap.put("lh", scoreSegement.get(1));
									    courseMap.put("jg", scoreSegement.get(0));
						            	for (Map<String, Object> classMap : classList) {
						            		courseMap.put("course", courseMap.get("course"));
						            		courseMap.put("classId", classMap.get("classId"));
						            		courseMap.put("examCode", examNumber);
						            		courseMap.put("createPerson", create_person);
						            		courseMap.put("createTime", create_time);
						            		Object markingTime=courseMap.get("markingTime");
						    				if("".equals(markingTime) || null==markingTime){
						    					markingTime=null;
						    				}
						    				courseMap.put("markingTime", markingTime);
							            	//添加相关班级和科目
							            	 examInfoSchoolService.addExamCalssCourse(courseMap);
										}
						            }
							}
							
						}
					Object introducedTime= paramMap.get("introducedTime");	
						if("".equals(introducedTime) || null==introducedTime){
							introducedTime=null;
						}
						paramMap.put("introducedTime", introducedTime);
				//添加考试信息
				examInfoService.addExam(paramMap);
				paramMap.put("mess", "addSuccess");
			} else {
				//修改考试前判断考号有无生成
				Map<String,Object> updateMap = new HashMap<String,Object>();
				List<String> paramList = new ArrayList<>();
				String examCode="";
				if(paramMap.containsKey("oldExamCode")){
					examCode=paramMap.get("oldExamCode").toString();
					examCode=examCode.substring(examCode.length()-4, examCode.length());
				}
				sb.append(examCode);
				examNumber =  sb.toString();
				paramMap.put("examNumber", examNumber);
				paramList.add(paramMap.get("id").toString());
				updateMap.put("selArr", paramList);
				//判断考号是否存在
				List<Map<String,Object>> list = examInfoService.selectExamNumberIsExist(updateMap);
				if(list != null && list.size() > 0){
					paramMap.put("mess", "notUpdate");
					out.print(getSerializer().formatMap(paramMap));
					return;
				}
				//得到修改之前的考号，用于修改考号
				if(paramMap.containsKey("oldExamCode")){
					 List<String> examCodeList=new ArrayList<>() ;
					if(!"".equals(paramMap.get("oldExamCode").toString()) || null!=paramMap.get("oldExamCode").toString()){
						//String oldExamCode=examCodeList.get(0);
						examCodeList.add(paramMap.get("oldExamCode").toString());
						//删除与考试相关的学校
						examInfoService.deleteExamRefSchool(examCodeList);
						//删除相关的班级和科目
					   examInfoSchoolService.deleteCourseClassByExamCode(examCodeList);
						Map<String, Object> filesMap=new HashMap<>();
						filesMap.put("examNumber", examNumber);
						filesMap.put("oldExamCode", paramMap.get("oldExamCode").toString());
						//修改考试相关的附件信息
						examInfoService.updateExamRefAccessory(filesMap);
					}
				}
				
				// 修改人
				String update_person = SecurityContext.getPrincipal().getId();
				// 修改时间
				String update_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
						.format(new Date());
				paramMap.put("update_person", update_person);
				paramMap.put("update_time", update_time);
				   schoolMap.put("examCode", examNumber);
				   examNumberManageService.insertschoolCodeExamNumber(schoolMap);
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
				    //得到相关的班级和科目
				    List<Map<String, Object>> courseExamParamList=(List<Map<String, Object>>) paramMap.get("params");
				    classList = (List<Map<String,Object>>)paramMap.get( "classId" ) ;
				    paramMaps=new HashMap<>();
					if( courseExamParamList != null ){
						if(classList.size()>0){
							   for( Map<String,Object> courseMap : courseExamParamList )
					            {
								    List<Integer> scoreSegement = examInfoSchoolService.searchScoreSegement(parseString(courseMap.get("zf")));
								    courseMap.put("zf", scoreSegement.get(3));
								    courseMap.put("yx", scoreSegement.get(2));
								    courseMap.put("lh", scoreSegement.get(1));
								    courseMap.put("jg", scoreSegement.get(0));
					            	for (Map<String, Object> classMap : classList) {
					            		courseMap.put("course", courseMap.get("course"));
					            		courseMap.put("classId", classMap.get("classId"));
					            		courseMap.put("examCode", examNumber);
					            		courseMap.put("createPerson", update_person);
					            		courseMap.put("createTime", update_time);
					            		Object markingTime=courseMap.get("markingTime");
					    				if("".equals(markingTime) || null==markingTime){
					    					markingTime=null;
					    				}
					    				courseMap.put("markingTime", markingTime);
						            	//添加相关班级和科目
					    				
						            	 examInfoSchoolService.addExamCalssCourse(courseMap);
									}
					            }
						}
					}
					Object introducedTime= paramMap.get("introducedTime");	
					if("".equals(introducedTime) || null==introducedTime){
						introducedTime=null;
					}
					paramMap.put("introducedTime", introducedTime);
				//根据考试信息
			   examInfoService.updateExamById(paramMap);
			
				paramMap.put("mess", "updateSuccess");
			}
		
		
		out.print(getSerializer().formatMap(paramMap));
	}
	
	
	/**
	 * @Title: getClassBySchoolAndGrade
	 * @Description: 根据学校code和年级得到班级
	 * @author zhaohuanhuan
	 * @date 2016年10月18日 
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=getClassBySchoolAndGrade")
	public void getClassBySchoolAndGrade(@RequestParam("data") String data, java.io.PrintWriter out) {
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		String loginName=SecurityContext.getPrincipal().getUsername();
		String schoolCode = examNumberManageService.getSchoolCodeByLoginName(loginName);
		requestMap.put("schoolCode", schoolCode);
		int minClassNo=0;
		int maxClassNo=0;
		String syzxSchoolCode=ConfigContext.getStringSection("syzxSchoolCode");
		String yczxSchoolCode=ConfigContext.getStringSection("yczxSchoolCode");
		if(yczxSchoolCode.equals(schoolCode)){
			schoolCode=syzxSchoolCode;
			minClassNo = 21;
			maxClassNo = 27;
			requestMap.put("schoolCode", schoolCode);
		}else if(syzxSchoolCode.equals(schoolCode)){
			minClassNo = 1;
			maxClassNo = 12;
		}
		requestMap.put("minClassNo", minClassNo);
		requestMap.put("maxClassNo", maxClassNo);
		List<Map<String, Object> > list=examInfoSchoolService.getClassBySchoolAndGrade(requestMap);
		out.print(getSerializer().formatList(list));
	}
	
	
	/**
	 * @Title: getClassIdAndCourse
	 * @Description: 得到与考试编号相关的班级和科目
	 * @author zhaohuanhuan
	 * @date 2016年10月19日 
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=getClassIdAndCourse")
	public void getClassIdAndCourse(@RequestParam("data") String data,
			java.io.PrintWriter out) {
		Map<String, Object> paramMap = getSerializer().parseMap(data);
		//根据考试编号得到与之相关的班级
		List<Map<String, Object>> list=examInfoSchoolService.getClassIdByExamCode(paramMap.get("examCode").toString());
		Map<String, Object> paramsMap =  new HashMap<>();
		paramsMap.put("examCode", paramMap.get("examCode").toString());
		List<String> classIdList=new ArrayList<>();
		for (Map<String, Object> map : list) {
			String classId=map.get("Class_Id").toString();
			classIdList.add(classId);
		}
		paramsMap.put("classIdList", classIdList);
		//根据考试编号和班级得到与之相关的科目
		List<Map<String, Object>> courselist=examInfoSchoolService.getCourserByExamCodeAndClass(paramsMap);
		paramMap=new HashMap<>();
		 paramMap.put("list", list);
		 paramMap.put("courselist", courselist);
		out.print(getSerializer().formatMap(paramMap));
	}
	
	
	
	
	@RequestMapping(params = "command=updateExamRefCourseByExamCodeAndCourse")
	public void updateExamRefCourseByExamCodeAndCourse(@RequestParam("data") String data,
			java.io.PrintWriter out) {
		Map<String, Object> paramMap = getSerializer().parseMap(data);
		//根据考试编号得到与之相关的班级
		 examInfoSchoolService.updateExamRefCourseByExamCodeAndCourse(paramMap);	
		 paramMap.put("message", true);
		 out.print(getSerializer().formatMap(paramMap));
	}
	
	
	@Autowired
	private ExamInfoSchoolService examInfoSchoolService ;
	
	@Autowired
	private ExamNumberManageService examNumberManageService;
	
	@Autowired
	private ExamInfoService examInfoService;
	
	@Autowired
	private PlatformAccessoryService accessoryService;
}
