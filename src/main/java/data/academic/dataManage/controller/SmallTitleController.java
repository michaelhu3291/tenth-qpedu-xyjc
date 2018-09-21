package data.academic.dataManage.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import data.academic.dataManage.service.SmallTitleService;
import data.academic.examNumberManagement.service.ExamNumberManageService;
import data.academic.qualityAnalysis.entity.StuTempInfo;
import data.academic.studentManagement.service.StuManagementService;
import data.academic.teacherManagement.service.TeacherManagementService;
import data.academic.util.ExportUtil;
import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractBaseController;
import data.framework.support.ConfigContext;
import data.framework.utility.ArithmeticUtil;
import data.framework.utility.ExcelReadUtils;
import data.framework.utility.UploadHelper;
import data.platform.authority.security.SecurityContext;
@RestController
@RequestMapping("dataManage/smallTitle")
public class SmallTitleController extends AbstractBaseController{

	@Override
	protected void init(ModelMap model, HttpServletRequest request) {
		// TODO Auto-generated method stub
		
	}
	@Autowired
	private SmallTitleService smallTitleService;
	@Autowired
	private TeacherManagementService teacherManagementService;
	@Autowired
	private ExamNumberManageService examNumberManageService;
	@Autowired
	private StuManagementService stuManagementService;
	private  HashMap<String,Object> map = new HashMap<String,Object>();
	
	/**
	 * 
	 * @Title: importNewStu
	 * @Description: 导入小题分
	 * @author xiahuajun
	 * @date 2016年8月2日 
	 * @param xlsFile
	 * @param out
	 * @param request
	 * @return void
	 */
	/**
	 * 
	 * @Title: importNewStu
	 * @Description: TODO 	批量删除
	 * @author chenteng
	 * @date 2017年8月3日 
	 * @return void
	 * @param xlsFile
	 * @param out
	 * @param request
	 */
	@RequestMapping(params=("command=importSmallTitle"))
    public void importNewStu(@RequestParam("xlsFile")CommonsMultipartFile xlsFile,java.io.PrintWriter out,HttpServletRequest request){
		Date start =new Date();
		
		 File fullFile = new File(xlsFile.getFileItem().getName());
		 String fileName = xlsFile.getFileItem().getName();
		 Map<String,Object> paramMap = new HashMap<String,Object>();
		 String loginName = SecurityContext.getPrincipal().getUsername();
		//根据登录名查询角色Code
		 String roleCode = teacherManagementService.selectRoleCodeByLoginName(loginName);
		 String schoolCode = null;
		 if("schoolPlainAdmin".equals(roleCode) || "schoolAdmin".equals(roleCode)) {
			 //学校管理员角色查询组织code
			 schoolCode = examNumberManageService.getSchoolCodeByLoginName(loginName); 
		 }
		 else if(roleCode.equals(ConfigContext.getValue("framework.tmis.roleCode['qpRoleAdminCode']")) || roleCode.equals(ConfigContext.getValue("framework.tmis.roleCode['qpRoleQuAdminCode']"))){
			 //青浦超级管理员角色查询组织code
			 schoolCode = smallTitleService.selectOrgCodeByLoginName(loginName);
		 }
		 if(!"xls".equals(fileName.substring(fileName.indexOf(".")+1)) && !"xlsx".equals(fileName.substring(fileName.indexOf(".")+1))){
			 paramMap.put("mess", "fileFormatError");
			 out.print(getSerializer().formatMap(paramMap));
			 return;
		 }
		 String course = "";
		 if(map.get("course").equals("sx")){course = "数学";}
		 else if(map.get("course").equals("yw")){course = "语文";}
		 else if(map.get("course").equals("yy")){course = "外语";}
		 else if(map.get("course").equals("wl")){course = "物理";}
		 else if(map.get("course").equals("hx")){course = "化学";}
		 else if(map.get("course").equals("ty")){course = "体育";}
		 else if(map.get("course").toString().equals("sxzz")){course = "思想政治";}
		 else if(map.get("course").toString().equals("ls")){course = "历史";}
		 else if(map.get("course").toString().equals("dl")){course = "地理";}
		 else if(map.get("course").toString().equals("kx")){course = "科学";}
		 else if(map.get("course").toString().equals("ms")){course = "美术";}
		 else if(map.get("course").toString().equals("tzxkc")){course = "拓展型课程";}
		 else if(map.get("course").toString().equals("xxkj")){course = "信息科技";}
		 else if(map.get("course").toString().equals("yjxkc")){course = "研究型课程";}
		 else if(map.get("course").toString().equals("yyue")){course = "音乐";}
		 else if(map.get("course").toString().equals("njyy")){course = "牛津英语";}
		 else if(map.get("course").toString().equals("sw")){course = "生物";}
		 else if(map.get("course").toString().equals("xsjyy")){course = "高中新世纪英语";}
		 else if(map.get("course").toString().equals("zr")){course = "自然";}
		 if(xlsFile.getFileItem().getName().indexOf(course) == -1){
			 Map<String,Object> mapValid = new HashMap<String,Object>();
			 mapValid.put("message", "error");
			 out.print(getSerializer().formatMap(mapValid));
			 return;
		 }
		 try {
			 if(!fullFile.exists())
			 {
				 fullFile.createNewFile();
			 }
			xlsFile.transferTo(fullFile);
			List<List<Object>> list =ExcelReadUtils.readExcel(fullFile);
			if(list.size() == 0){
				paramMap.put("mess", "noData");
				out.print(getSerializer().formatMap(paramMap));
				return;
			}
			List<Object> titleO = list.get(0);
			List<HashMap<String,Object>> paramList = new ArrayList<HashMap<String,Object>>();
			List<HashMap<String,Object>> totalScoreList = new ArrayList<HashMap<String,Object>>();
			HashMap<String,Object> deleteMap = new HashMap<>();
			String create_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			String create_person = SecurityContext.getPrincipal().getChineseName();
			//--------------------------------------------------------------------------
			List<String> examNumList = new ArrayList<>();
			for (int i = 1; i < list.size(); i++) {
				List<Object> rowData = list.get(i);
				if(rowData.size() == 0){
					continue;
				}
				examNumList.add(parseString(rowData.get(0)));
			}
			List<StuTempInfo> examInfoList = new ArrayList<>();//
			//--------------
			int batchNum = 0;
			int paramListSize=0;
			ArrayList<String> tempExamNumList =new ArrayList<>();
			for(int i=0;i<examNumList.size();i++){
			    if(batchNum<2000){
			    	tempExamNumList.add(examNumList.get(i));
			        batchNum++;
			    }else{
			    	examInfoList.addAll(stuManagementService.findInfoList(tempExamNumList));
					tempExamNumList = new ArrayList<String>();
					tempExamNumList.add(examNumList.get(i));
				     batchNum=1;
			    }
			    paramListSize++;
			}
			
			for(int i=paramListSize;i<examNumList.size();i++){
				tempExamNumList.add(examNumList.get(i));
	        }
			examInfoList.addAll(stuManagementService.findInfoList(tempExamNumList));
			//--------------
			HashMap<String,HashMap> infoMap = new HashMap<>();
 			for(StuTempInfo temp:examInfoList)
			{
 				HashMap<String,String> map2 =new HashMap<>();
 				map2.put("schoolName",temp.getSchoolName());
 		        map2.put("classId",temp.getClassId());
 		        map2.put("grade",temp.getGrade());
 		        map2.put("classType",temp.getClassType());
 		        map2.put("className",temp.getClassName());
 		        map2.put("schoolCode",temp.getSchoolCode());
 		        map2.put("xjh",temp.getXjh());
 		        map2.put("examCode",temp.getExamCode());
 				infoMap.put(temp.getExamNumber(), map2);
			}
			//--------------------------------------------------------------------------解决了N+1
			for (int i = 1; i < list.size(); i++) {//一张Excel里所有的行,第一行是表头
				List<Object> rowData = list.get(i);
				if(rowData.size() == 0){
					continue;
				}
				double personScore =0;
				StringBuffer sb = new StringBuffer();
				//HashMap<String,String> classAndTypeMap = stuManagementService.findClassDetail(parseString(rowData.get(0)));
				for(int k = 2; k < rowData.size(); k++){//一行列表里所有的数据 从第三列开始是分数
					HashMap<String,Object> param = new HashMap<String,Object>();
					if(rowData.get(0) == null || "".equals(rowData.get(0).toString())){
						continue;
					}
					param.put("exam_number", rowData.get(0));//考号
					param.put("name", rowData.get(1));//学生姓名
					param.put("tihao", titleO.get(k));//题号
					param.put("score", rowData.get(k));//分数
					//TODO 正则检验数字 为空、异常字符、空字符串放0；
					Pattern pattern = Pattern.compile("[0-9]*"); 
					Matcher isNum = pattern.matcher((String)rowData.get(k));
					
					try {
						if( isNum.matches() ){
							personScore=ArithmeticUtil.add(personScore, parseDouble(rowData.get(k)));//算出总分
							sb.append((String)rowData.get(k));
						}else{
							personScore=ArithmeticUtil.add(personScore, parseDouble(0));//算出总分
							sb.append("0");
						}
					} catch (Exception e) {
						personScore=ArithmeticUtil.add(personScore, parseDouble(0));//算出总分
						sb.append("0");
					}
					sb.append(",");
					param.put("create_time", create_time);
					param.put("grade",  infoMap.get(rowData.get(0)).get("grade"));//增加年级字段
					param.put("create_person", create_person);
					param.put("schoolYear", map.get("schoolYear"));
					param.put("schoolType", map.get("schoolType"));
					param.put("term", map.get("term"));
					param.put("examType", map.get("examType"));
					param.put("course", map.get("course"));
					param.put("schoolCode", schoolCode);
					param.put("schoolName", infoMap.get(rowData.get(0)).get("schoolName"));
					param.put("className", infoMap.get(rowData.get(0)).get("className"));
					param.put("xjh", infoMap.get(rowData.get(0)).get("xjh"));
					
					param.put("smallTitleSort", k);
					paramList.add(param);
				}
				
				HashMap<String,Object> personTotalScore = new HashMap<String,Object>();
				
				personTotalScore.put("exam_number", rowData.get(0));//考号
				//由考号查出学生对应的班级和是否是新疆班  还要加上学校编号 上面的schoolcode是错的 只是上传人所在单位的编号
				personTotalScore.put("schoolName", infoMap.get(rowData.get(0)).get("schoolName"));
				personTotalScore.put("xjh", infoMap.get(rowData.get(0)).get("xjh"));
				personTotalScore.put("className", infoMap.get(rowData.get(0)).get("className"));
				personTotalScore.put("classType", infoMap.get(rowData.get(0)).get("classType"));
				personTotalScore.put("schoolCode", infoMap.get(rowData.get(0)).get("schoolCode"));
				personTotalScore.put("classId", infoMap.get(rowData.get(0)).get("classId"));
				personTotalScore.put("grade", infoMap.get(rowData.get(0)).get("grade"));
				personTotalScore.put("name", rowData.get(1));//学生姓名
				personTotalScore.put("totalScore", personScore);//学生个人总分
				personTotalScore.put("create_time", create_time);
				personTotalScore.put("create_person", create_person);
				personTotalScore.put("schoolYear", map.get("schoolYear"));
				personTotalScore.put("schoolType", map.get("schoolType"));
				personTotalScore.put("term", map.get("term"));
				personTotalScore.put("examType", map.get("examType"));
				personTotalScore.put("course", map.get("course"));
				personTotalScore.put("examCode", infoMap.get(rowData.get(0)).get("examCode"));
				personTotalScore.put("smallScoreList",(sb.toString()).substring(0,sb.toString().length()-1));
				totalScoreList.add(personTotalScore);

				//deleteMap.put("exam_number", rowData.get(0).toString());
				deleteMap.put("course", map.get("course"));
				deleteMap.put("examNumList", examNumList);
				
			}
			
			//导入小题分	    
			smallTitleService.batchOperate(deleteMap,paramList,totalScoreList);
			Map<String,Object> param = new HashMap<String,Object>();
			param.put("mess", "success");
			System.out.println("花费"+(new Date().getTime()-start.getTime()));
			out.print(getSerializer().formatMap(param));
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();

		}
	}	 
	
	/**
	 * @Title: searchPaging
	 * @Description: 分页查询小题分
	 * @author xiahuajun
	 * @date 2016年8月3日 
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=searchSmallTitlePaging")
	public void searchPaging(@RequestParam("data") String data,java.io.PrintWriter out,HttpServletRequest request){
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		String time = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		int year = parseInteger(time.substring(0, time.indexOf("-")));
		int month = parseInteger(time.substring(time.indexOf("-")+1,time.lastIndexOf("-")));
		String schoolYear = "";
		if(month < 9){
			schoolYear = (year-1) + "-" + year;
		} else {
			schoolYear = year + "-" + (year+1);
		}
		if(null == requestMap.get("schoolYear")){
			requestMap.
			put("schoolYear", schoolYear);
		}
		boolean isFast = parseBoolean(requestMap.get( "isFast" )) ;
	    String exam_number = "" ;
	    String name = "";
	    if( !isFast ){
	    	exam_number = trimString( requestMap.get( "q" ) ) ;
	    	name = trimString( requestMap.get( "q" ) ) ;
	    }
	    String loginName = SecurityContext.getPrincipal().getUsername();
		//根据登录名查询角色Code
		 String roleCode = teacherManagementService.selectRoleCodeByLoginName(loginName);
		 String schoolCode = null;
		 if("schoolPlainAdmin".equals(roleCode) || "schoolAdmin".equals(roleCode)) {
			 //学校管理员角色查询组织code
			 schoolCode = examNumberManageService.getSchoolCodeByLoginName(loginName); 
		 }
		 else if(roleCode.equals(ConfigContext.getValue("framework.tmis.roleCode['qpRoleAdminCode']")) || roleCode.equals(ConfigContext.getValue("framework.tmis.roleCode['qpRoleQuAdminCode']"))){
			 //青浦超级管理员角色查询组织code
			 schoolCode = smallTitleService.selectOrgCodeByLoginName(loginName);
		 }
	    requestMap.put("exam_number", exam_number);
	    requestMap.put("name", name);
	    requestMap.put("schoolCode", schoolCode);
		int currentPage = parseInteger(requestMap.get("page"));
		int pageSize = parseInteger(requestMap.get("rows"));
		String sortField = trimString(requestMap.get("sidx"));
		if (sortField.trim().length() == 0)
		{
			//sortField = "Exam_Number";
			sortField = "Total_Score";
		}
		String sort = trimString(requestMap.get("sord"));
		
		
		PagingResult<Map<String, Object>> pagingResult = smallTitleService.serachSmallTitleScore(requestMap, sortField, sort, currentPage, pageSize);
		List<Map<String, Object>> list = pagingResult.getRows();
		PagingResult<Map<String, Object>> newPagingResult = new PagingResult<Map<String, Object>>(list,
				pagingResult.getPage(), pagingResult.getPageSize(), pagingResult.getRecords());
		out.print(getSerializer().formatObject(newPagingResult));
		}
	/**
	 * 
	 * @Title: importExcel
	 * @Description: 导出
	 * @author jay zhong
	 * @date 2017年11月21日 下午2:33:40 
	 * @return void
	 *
	 * @param data
	 * @param response
	 */
	@RequestMapping(params = "command=importExcel")
	public void importExcel(@RequestParam( "data" ) String data,HttpServletResponse response) 
	{	
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		String time = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		int year = parseInteger(time.substring(0, time.indexOf("-")));
		int month = parseInteger(time.substring(time.indexOf("-")+1,time.lastIndexOf("-")));
		String schoolYear = "";
		if(month < 9){
			schoolYear = (year-1) + "-" + year;
		} else {
			schoolYear = year + "-" + (year+1);
		}
		if(null == requestMap.get("schoolYear")){
			requestMap.
			put("schoolYear", schoolYear);
		}
	    String exam_number = "" ;
	    String name = "";
	    exam_number = trimString( requestMap.get( "q" ) ) ;
	    name = trimString( requestMap.get( "q" ) ) ;
	    String loginName = SecurityContext.getPrincipal().getUsername();
		//根据登录名查询角色Code
		 String roleCode = teacherManagementService.selectRoleCodeByLoginName(loginName);
		 String schoolCode = null;
		 if("schoolPlainAdmin".equals(roleCode) || "schoolAdmin".equals(roleCode)) {
			 //学校管理员角色查询组织code
			 schoolCode = examNumberManageService.getSchoolCodeByLoginName(loginName); 
		 }
		 else if(roleCode.equals(ConfigContext.getValue("framework.tmis.roleCode['qpRoleAdminCode']")) || roleCode.equals(ConfigContext.getValue("framework.tmis.roleCode['qpRoleQuAdminCode']"))){
			 //青浦超级管理员角色查询组织code
			 schoolCode = smallTitleService.selectOrgCodeByLoginName(loginName);
		 }
	    requestMap.put("exam_number", exam_number);
	    requestMap.put("name", name);
	    requestMap.put("schoolCode", schoolCode);
		
		List<Map<String, Object>> list = smallTitleService.serachImportSmallTitleScore(requestMap);
		String fileName="考分列表.xls";
		String sheetName="考分列表详情";
		String[] title={"序号","考号","姓名","学年","测试类型","学期","科目","总分"};
		String[] key={"xh","Exam_Number","Name","School_Year","Exam_Type","Term","Course","Total_Score"};
		ExportUtil.ExportExcel(response, title, fileName, sheetName, list, key);
				
		
	}
	
	/**
	 * @Title: getImportParams
	 * @Description: 按条件插入
	 * @author xiahuajun
	 * @date 2016年8月8日 
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
		map.put("course",requestMap.get("course").toString());
		out.print(getSerializer().formatObject(""));
	}
	
	
	/**
	 * @Title: queryDetailList
	 * @Description: 查看详情
	 * @author xiahuajun
	 * @date 2016年8月11日 
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=queryDetailList")
	public void queryDetailList(@RequestParam("data") String data,java.io.PrintWriter out){
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		if(requestMap.get("course").equals("数学")){
			requestMap.put("course", "sx");
		}
		else if(requestMap.get("course").equals("语文")){
			requestMap.put("course", "yw");
		}
		else if(requestMap.get("course").equals("英语")){
			requestMap.put("course", "yy");
		}
		else if(requestMap.get("course").equals("物理")){
			requestMap.put("course", "wl");
		}
		else if(requestMap.get("course").equals("化学")){
			requestMap.put("Course", "hx");
		}
		else if(requestMap.get("course").equals("体育")){
			requestMap.put("Course", "ty");
		}
		else if(requestMap.get("course").equals("政治")){
			requestMap.put("Course", "zz");
		}
		else if(requestMap.get("course").equals("历史")){
			requestMap.put("Course", "ls");
		}
		else if(requestMap.get("course").equals("地理")){
			requestMap.put("Course", "dl");
		}
		List<Map<String, Object>> list = smallTitleService.getDetailList(requestMap);
		List<Map<String, Object>> paramList = new ArrayList<Map<String, Object>>();
		//[{Question_Number=1, School_Year=2015-2016, Create_Time=2016-08-11 10:03:07.0, School_Type=xx, Exam_Type=qz, Create_Person=238F62E4-AFBE-4976-AF3B-B1F1417CD13D, Term=sxq, Name=吴琰, Course=sx, Id=EE346EFF-196E-418F-A950-6D09300A38A6, Exam_Number=270601008, Score=2, Stu_Code=1001}
		for(Map<String, Object> map:list){
			if(map.get("Term").toString().equals("sxq")){
				map.put("Term", "第一期");
			} else if(map.get("Term").toString().equals("xxq")){
				map.put("Term", "第二学期");
			}
			if(map.get("Exam_Type").toString().equals("qz")){
				map.put("Exam_Type", "期中");
			} else if(map.get("Exam_Type").toString().equals("qm")){
				map.put("Exam_Type", "期末");
			}
			if(map.get("Course").toString().equals("sx")){
				map.put("Course", "数学");
			}
			else if(map.get("Course").toString().equals("yw")){
				map.put("Course", "语文");
			}
			else if(map.get("Course").toString().equals("yy")){
				map.put("Course", "英语");
			}
			else if(map.get("Course").toString().equals("wl")){
				map.put("Course", "物理");
			}
			else if(map.get("Course").toString().equals("hx")){
				map.put("Course", "化学");
			}
			else if(map.get("Course").toString().equals("ty")){
				map.put("Course", "体育");
			}
			else if(map.get("Course").toString().equals("zz")){
				map.put("Course", "政治");
			}
			else if(map.get("Course").toString().equals("ls")){
				map.put("Course", "历史");
			}
			else if(map.get("Course").toString().equals("dl")){
				map.put("Course", "地理");
			}
			paramList.add(map);
		}
		out.print(getSerializer().formatList(paramList));
		}
	
	/**
	 * @Title: getCurrentYear
	 * @Description: 显示学年为当前学年
	 * @author xiahuajun
	 * @date 2016年8月18日 
	 * @param data
	 * @param out
	 * @return void
	 */
	@RequestMapping(params = "command=getCurrentYear")
	public void getCurrentYear(@RequestParam("data") String data,java.io.PrintWriter out){
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		//{schoolYear=wl, schoolType=cz, term=sxq, examType=qz, course=yy}
		Map<String, Object> map= smallTitleService.getCurrentYearByParam(requestMap);
		out.print(getSerializer().formatMap(map));
	}
    private static String uploadPath = UploadHelper.getRealUploadPath() ;
}
