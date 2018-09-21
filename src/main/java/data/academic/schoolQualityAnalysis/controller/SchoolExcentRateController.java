package data.academic.schoolQualityAnalysis.controller;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSONObject;

import data.academic.dataManage.service.SmallTitleService;
import data.academic.examNumberManagement.service.ExamNumberManageService;
import data.academic.schoolQualityAnalysis.service.SchoolScoreService;
import data.academic.studentManagement.service.StuManagementService;
import data.academic.util.ExportUtil;
import data.framework.support.AbstractBaseController;
import data.framework.support.ConfigContext;
import data.framework.utility.ArithmeticUtil;
import data.platform.authority.security.SecurityContext;
@Controller
@RequestMapping("schoolQualityAnalysis/excellentRate")
public class SchoolExcentRateController extends AbstractBaseController{

	@Override
	protected void init(ModelMap model, HttpServletRequest request) {
	}
	/*@Autowired
	private SmallTitleService smallTitleService;*/
	@Autowired
	private SchoolScoreService schoolScoreService;
	@Autowired
	private ExamNumberManageService examNumberManageService;
	@Autowired
	private StuManagementService stuManagementService;
	/**
	 * 
	 * @Title: analyzePersonalSamllScore
	 * @Description: 个人单科成绩汇总;添加区级成绩汇总信息
	 * @author chenteng;zhoahuanhuan
	 * @date 2017年8月3日 ;2017.11.24
	 * @return void
	 * @param data
	 * @param out
	 */
	@RequestMapping(params = "command=analyzeExcellentRate")
	@ResponseBody
	public JSONObject analyzePersonalSamllScore(@RequestParam("data") String data){
		HashMap<String,String> staticMap = new HashMap();
		//TODO 改成enumMap工具类
		staticMap.put("yw","语文");staticMap.put("sx","数学");staticMap.put("yy","外语");staticMap.put("wl","物理");staticMap.put("hx","化学");staticMap.put("ty","体育");staticMap.put("sxzz","思想政治");
		staticMap.put("ls","历史");staticMap.put("dl","地理");staticMap.put("kx","科学");staticMap.put("ms","美术");staticMap.put("tzxkc","拓展型课程");staticMap.put("xxkj","信息科技");
		staticMap.put("yjxkc","研究型课程");staticMap.put("yyue","音乐");staticMap.put("njyy","牛津英语");staticMap.put("sw","生物");staticMap.put("xsjyy","高中新世纪英语");staticMap.put("zr","自然");
		staticMap.put("qm","期末");staticMap.put("qz","期中");staticMap.put("sxq","上学期");staticMap.put("xxq","下学期");
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		Map<String, Object> mapForDistrict = getSerializer().parseMap(data);
		String schoolCode = "";
		String loginName=SecurityContext.getPrincipal().getUsername();
		if("qpAdmin".equals(SecurityContext.getPrincipal().getUsername())){
			schoolCode = ConfigContext.getValue("framework.tmis.roleCode['qpOrgCode']");
		} else {
			schoolCode = examNumberManageService.getSchoolCodeByLoginName(loginName);
		}
		requestMap.put("schoolCode", schoolCode);
		mapForDistrict.put("schoolCode", schoolCode); 
		ArrayList<String> courseList = new ArrayList<>();
		//ArrayList<String> courseNameList = (ArrayList<String>) requestMap.get("courseName");
		ArrayList<String> classCodeList = (ArrayList<String>) requestMap.get("classCode");
		ArrayList<String> classNameList = (ArrayList<String>) requestMap.get("className");
		ArrayList<String> classCodeList2 = new ArrayList<>();
		ArrayList<String> classNameList2 = new ArrayList<>();
		mapForDistrict.put("isDistrict", true);
		mapForDistrict.put("classCodeList", classCodeList);
		JSONObject jo =new JSONObject();
		//总分 及格率 优良率 平均分 平均分排名
		DecimalFormat format = new DecimalFormat("0.00%");
		DecimalFormat df = new DecimalFormat("######0.00");  
		ArrayList<HashMap<String, Object>> districtList = new ArrayList<>();
		//获得选中的科目 根据他们获得每门考试的总分 优分 良分 及格分
		HashMap<String,HashMap> courseScoreMap = new HashMap<>();
		int allZf=0;
		int allYx=0;
		int allLh=0;
		int allJg=0;
		//查有没有选中的考试
		List<HashMap<String,String>> courseExaminfoList =schoolScoreService.getCourseExaminfoList(requestMap);
		if(courseExaminfoList.size()<1){
			jo.put("message", "faile");
			return jo;
		}
		//算出选中考试的总分 优良及格总分
		for(HashMap<String,String> temp:courseExaminfoList){
			allZf+=parseInteger(temp.get("Exam_Zf"));
			allYx+=parseInteger(temp.get("Exam_Yx"));
			allLh+=parseInteger(temp.get("Exam_Lh"));
			allJg+=parseInteger(temp.get("Exam_Jg"));
			courseScoreMap.put(temp.get("Course"), temp);
			temp.put("courseName", staticMap.get(temp.get("Course")));
			courseList.add(temp.get("Course"));
		}
		//提交班级名称中数字 --zhaohuanhuan
		String regEx="[^0-9]";   
		Pattern p = Pattern.compile(regEx);
		for(int i=0;i<classCodeList.size();i++){
			mapForDistrict.put("classCode", classCodeList.get(i));
			mapForDistrict.put("allZf", allZf);
			mapForDistrict.put("allYx", allYx);
			mapForDistrict.put("allLh", allLh);
			mapForDistrict.put("allJg", allJg);
			HashMap<String, Object> districtRateMap = schoolScoreService.getDistrictRateMap(mapForDistrict);
			if(parseInteger(districtRateMap.get("zrs"))==0){
				continue;
			}
			classCodeList2.add(classCodeList.get(i));
			classNameList2.add(classNameList.get(i));
			String className=classNameList2.get(i);
			Matcher m = p.matcher(className);  
			String classNo=m.replaceAll("").trim();
			districtRateMap.put("classNo", classNo);
			districtRateMap.put("classCode", classCodeList.get(i));
			districtRateMap.put("pjf", df.format(parseDouble(districtRateMap.get("zf"))/parseDouble(districtRateMap.get("zrs"))));
			districtRateMap.put("jgl", format.format(parseDouble(districtRateMap.get("jgrs"))/parseDouble(districtRateMap.get("zrs"))));
			districtRateMap.put("yll", format.format(parseDouble(districtRateMap.get("ylrs"))/parseDouble(districtRateMap.get("zrs"))));
			districtList.add(districtRateMap);
		}
		Collections.sort(districtList, new Comparator<Map<String, Object>>() {
	        public int compare(Map<String, Object> o1, Map<String, Object> o2) {
	            return parseDouble(o1.get("pjf")).compareTo(parseDouble(o2.get("pjf")));
	        }
	    });
		Collections.reverse(districtList);
		for(int k=0;k<districtList.size();k++){
			districtList.get(k).put("pjfpm", k+1);
		}
		
		Collections.sort(districtList, new Comparator<Map<String, Object>>() {
	        public int compare(Map<String, Object> o1, Map<String, Object> o2) {
	            return parseDouble(o1.get("classNo")).compareTo(parseDouble(o2.get("classNo")));
	        }
	    });

		ArrayList<HashMap> rateList = new ArrayList<>();
		for(int h=0;h<courseList.size();h++){
			HashMap<String,Object> rateMap= new HashMap<>();
			requestMap.put("targetCourse", courseList.get(h));
			mapForDistrict.put("targetCourse", courseList.get(h));
			//全校
			mapForDistrict.put("Zf", courseScoreMap.get(courseList.get(h)).get("Exam_Zf"));
			mapForDistrict.put("Yx", courseScoreMap.get(courseList.get(h)).get("Exam_Yx"));
			mapForDistrict.put("Lh", courseScoreMap.get(courseList.get(h)).get("Exam_Lh"));
			mapForDistrict.put("Jg", courseScoreMap.get(courseList.get(h)).get("Exam_Jg"));
			requestMap.put("Zf", courseScoreMap.get(courseList.get(h)).get("Exam_Zf"));
			requestMap.put("Yx", courseScoreMap.get(courseList.get(h)).get("Exam_Yx"));
			requestMap.put("Lh", courseScoreMap.get(courseList.get(h)).get("Exam_Lh"));
			requestMap.put("Jg", courseScoreMap.get(courseList.get(h)).get("Exam_Jg"));
			HashMap<String, Object> districtRateMap = schoolScoreService.getOneCourseRateList(mapForDistrict);
			if(parseInteger(districtRateMap.get("skrs"))<1){
				continue;
			}
			districtRateMap.put("bzc", df.format(districtRateMap.get("bzc")));
			districtRateMap.put("pjf", df.format(districtRateMap.get("pjf")));
			districtRateMap.put("yxl", format.format(parseDouble(districtRateMap.get("yxrs"))/parseDouble(districtRateMap.get("skrs"))));
			districtRateMap.put("lhl", format.format(parseDouble(districtRateMap.get("lhrs"))/parseDouble(districtRateMap.get("skrs"))));
			districtRateMap.put("yll", format.format(parseDouble(districtRateMap.get("ylrs"))/parseDouble(districtRateMap.get("skrs"))));
			districtRateMap.put("hgl", format.format(parseDouble(districtRateMap.get("hgrs"))/parseDouble(districtRateMap.get("skrs"))));
			districtRateMap.put("jgl", format.format(parseDouble(districtRateMap.get("jgrs"))/parseDouble(districtRateMap.get("skrs"))));
			districtRateMap.put("jcl", format.format(parseDouble(districtRateMap.get("jcrs"))/parseDouble(districtRateMap.get("skrs"))));
			districtRateMap.put("cyxs", df.format(parseDouble(districtRateMap.get("bzc"))/parseDouble(districtRateMap.get("pjf"))));
			districtRateMap.put("course", courseList.get(h));
			districtRateMap.put("courseName", staticMap.get(courseList.get(h)));//还要多表查询该门考试总分多少
			rateMap.put("district", districtRateMap);
			
			
			//全区每科详细情况
			mapForDistrict.put("schoolCode", "");
			List<String> schoolCodeList=new ArrayList<>();
			mapForDistrict.put("schoolCodeList",schoolCodeList);
			HashMap<String, Object> allSchoolMap = smallTitleService.getOneCourseRateList(mapForDistrict);
			if(parseInteger(allSchoolMap.get("skrs"))<1){
				continue;
			}
			if(allSchoolMap.size()>0 && allSchoolMap!=null){
				allSchoolMap.put("bzc", df.format(allSchoolMap.get("bzc")));//标准差
				allSchoolMap.put("pjf", df.format(allSchoolMap.get("pjf")));//平均分
				allSchoolMap.put("yxl", format.format(parseDouble(allSchoolMap.get("yxrs"))/parseDouble(allSchoolMap.get("skrs"))));//优秀率 yxrs——优秀人数 skrs——实考人数
				allSchoolMap.put("lhl", format.format(parseDouble(allSchoolMap.get("lhrs"))/parseDouble(allSchoolMap.get("skrs"))));//良好率
				allSchoolMap.put("yll", format.format(parseDouble(allSchoolMap.get("ylrs"))/parseDouble(allSchoolMap.get("skrs"))));//优良率
				allSchoolMap.put("hgl", format.format(parseDouble(allSchoolMap.get("hgrs"))/parseDouble(allSchoolMap.get("skrs"))));//合格人数
				allSchoolMap.put("jgl", format.format(parseDouble(allSchoolMap.get("jgrs"))/parseDouble(allSchoolMap.get("skrs"))));//及格人数
				allSchoolMap.put("jcl", format.format(parseDouble(allSchoolMap.get("jcrs"))/parseDouble(allSchoolMap.get("skrs"))));//极差人数
				allSchoolMap.put("cyxs", df.format(parseDouble(allSchoolMap.get("bzc"))/parseDouble(allSchoolMap.get("pjf"))));//bzc——标准差 cyxs——差异系数
				allSchoolMap.put("course", courseList.get(h));
				allSchoolMap.put("courseName", staticMap.get(courseList.get(h)));
			}
			
			rateMap.put("allSchool", allSchoolMap);
			//各个班级每门
			List<HashMap<String, Object>> oneCourseSchoolList = new ArrayList<>();
			for(int i=0;i<classCodeList2.size();i++){
				requestMap.put("classCode", classCodeList2.get(i));
				//传入全区单科平均分
				requestMap.put("districtPjf", districtRateMap.get("pjf"));
				HashMap<String, Object> oneCourseRateMap = schoolScoreService.getOneCourseRateList(requestMap);
				if(parseInteger(oneCourseRateMap.get("skrs"))<1){
					continue;
				}
				if(parseInteger(oneCourseRateMap.get("skrs"))>0){
					oneCourseRateMap.put("schoolName", classNameList2.get(i));
					String className=classNameList2.get(i);
					Matcher m = p.matcher(className);  
					String classNo=m.replaceAll("").trim();
					oneCourseRateMap.put("classNo", classNo);
					oneCourseRateMap.put("schoolCode", classCodeList2.get(i));
					//优秀率 >90
					oneCourseRateMap.put("yxl", format.format(parseDouble(oneCourseRateMap.get("yxrs"))/parseDouble(oneCourseRateMap.get("skrs"))));
					//良好率 80~90
					oneCourseRateMap.put("lhl", format.format(parseDouble(oneCourseRateMap.get("lhrs"))/parseDouble(oneCourseRateMap.get("skrs"))));
					//优良率 >80
					oneCourseRateMap.put("yll", format.format(parseDouble(oneCourseRateMap.get("ylrs"))/parseDouble(oneCourseRateMap.get("skrs"))));
					oneCourseRateMap.put("ylll", ArithmeticUtil.div(parseDouble(oneCourseRateMap.get("ylrs")), parseDouble(oneCourseRateMap.get("skrs"))));
					//合格率 60~80
					oneCourseRateMap.put("hgl", format.format(parseDouble(oneCourseRateMap.get("hgrs"))/parseDouble(oneCourseRateMap.get("skrs"))));
					//及格率 >60
					oneCourseRateMap.put("jgl", format.format(parseDouble(oneCourseRateMap.get("jgrs"))/parseDouble(oneCourseRateMap.get("skrs"))));
					oneCourseRateMap.put("bjgrs", parseDouble(oneCourseRateMap.get("skrs"))-parseDouble(oneCourseRateMap.get("jgrs")));
					//极差率 <20
					oneCourseRateMap.put("jcl", format.format(parseDouble(oneCourseRateMap.get("jcrs"))/parseDouble(oneCourseRateMap.get("skrs"))));
					oneCourseRateMap.put("bzc", df.format(oneCourseRateMap.get("bzc")));
					oneCourseRateMap.put("pjf", df.format(oneCourseRateMap.get("pjf")));
					//标准差，两位小数
					
					//标准分
					String bzf = df.format((parseDouble(oneCourseRateMap.get("pjf"))-parseDouble(districtRateMap.get("pjf")))/parseDouble(districtRateMap.get("bzc")));
					oneCourseRateMap.put("bzf",bzf);
					//T值
					oneCourseRateMap.put("T", df.format(parseDouble(bzf)*10+50));
					//差异系数
					oneCourseRateMap.put("cyxs", df.format(parseDouble(oneCourseRateMap.get("bzc"))/parseDouble(oneCourseRateMap.get("pjf"))));
					//偏差率
					oneCourseRateMap.put("pcl", df.format((parseDouble(oneCourseRateMap.get("pjf"))-parseDouble(districtRateMap.get("pjf")))/parseDouble(districtRateMap.get("pjf"))*100));
					//超均率
					oneCourseRateMap.put("cjl", format.format(parseDouble(oneCourseRateMap.get("cjrs"))/parseDouble(oneCourseRateMap.get("skrs"))));
					for(HashMap temp2:districtList){
						if(temp2.get("classCode").equals(classCodeList2.get(i))){
							oneCourseRateMap.put("zfpjfpm",temp2.get("pjfpm"));
						}
					}
					oneCourseSchoolList.add(oneCourseRateMap);
				}
				//[{ylrs=7, lhrs=7, skrs=168, zgf=83, yxrs=0, zdf=55, pjf=69.238095, hgrs=155}]
				//拿到了 优秀人数 优良人数 良好人数 实考人数 最高分 最低分 平均分 合格人数
			}
			//先排平均分 再排偏差率
			Collections.sort(oneCourseSchoolList, new Comparator<Map<String, Object>>() {
		        public int compare(Map<String, Object> o1, Map<String, Object> o2) {
		            return parseDouble(o1.get("pjf")).compareTo(parseDouble(o2.get("pjf")));
		        }
		    });
			Collections.reverse(oneCourseSchoolList);
			for(int k=0;k<oneCourseSchoolList.size();k++){
				oneCourseSchoolList.get(k).put("pjfpm", k+1);
			}
			//偏差率排名
			Collections.sort(oneCourseSchoolList, new Comparator<Map<String, Object>>() {
		        public int compare(Map<String, Object> o1, Map<String, Object> o2) {
		            return parseDouble(o1.get("pcl")).compareTo(parseDouble(o2.get("pcl")));
		        }
		    });
			Collections.reverse(oneCourseSchoolList);
			for(int k=0;k<oneCourseSchoolList.size();k++){
				oneCourseSchoolList.get(k).put("pclpm", k+1);
			}
			//优良率排名
			Collections.sort(oneCourseSchoolList, new Comparator<Map<String, Object>>() {
		        public int compare(Map<String, Object> o1, Map<String, Object> o2) {
		            return parseDouble(o1.get("ylll")).compareTo(parseDouble(o2.get("ylll")));
		        }
		    });
			Collections.reverse(oneCourseSchoolList);
			for(int k=0;k<oneCourseSchoolList.size();k++){
				oneCourseSchoolList.get(k).put("yllpm", k+1);
			}
			
			
			Collections.sort(oneCourseSchoolList, new Comparator<Map<String, Object>>() {
		        public int compare(Map<String, Object> o1, Map<String, Object> o2) {
		           // return parseDouble(o1.get("zfpjfpm")).compareTo(parseDouble(o2.get("zfpjfpm")));
		        	 return parseDouble(o1.get("classNo")).compareTo(parseDouble(o2.get("classNo")));
		        }
		    });
			
			rateMap.put("schools", oneCourseSchoolList);
			rateList.add(rateMap);
		};
		if(mapForDistrict.get("chooseFunction")!=null && mapForDistrict.get("chooseFunction").equals("1")){
			requestMap.put("targetCourse", courseList.get(0));
			List<HashMap> minAndMax=new ArrayList<>();
			for(int i=0;i<classCodeList2.size();i++){
				mapForDistrict.put("targetClassCode", classCodeList2.get(i));
				HashMap<String,String> schoolMap =new HashMap<>();
				schoolMap.put("className", classNameList2.get(i));
				List<String> minNameList = schoolScoreService.getMinNameList(mapForDistrict);
				if(minNameList.size()<1){
					continue;
				}
				String minList="";
				for(String temp :minNameList){
					minList+=temp;
					minList+=",";
				}
				schoolMap.put("minList", minList.substring(0, minList.length()-1));
				
				List<String> maxNameList = schoolScoreService.getMaxNameList(mapForDistrict);
				
				String maxList="";
				for(String temp :maxNameList){
					maxList+=temp;
					maxList+=",";
				}
				schoolMap.put("maxList", maxList.substring(0, maxList.length()-1));
				minAndMax.add(schoolMap);
			}
			jo.put("minAndMax", minAndMax);
		}
		//全区每科详细情况
		mapForDistrict.put("schoolCode", "");
		List<String> schoolCodeList=new ArrayList<>();
		mapForDistrict.put("schoolCodeList",schoolCodeList);
		HashMap<String, Object> allSchoolMap = smallTitleService.getOneCourseRateList(mapForDistrict);
		if(allSchoolMap.size()>0 && allSchoolMap!=null){
			allSchoolMap.put("bzc", df.format(allSchoolMap.get("bzc")));//标准差
			allSchoolMap.put("pjf", df.format(allSchoolMap.get("pjf")));//平均分
			allSchoolMap.put("yxl", format.format(parseDouble(allSchoolMap.get("yxrs"))/parseDouble(allSchoolMap.get("skrs"))));//优秀率 yxrs——优秀人数 skrs——实考人数
			allSchoolMap.put("lhl", format.format(parseDouble(allSchoolMap.get("lhrs"))/parseDouble(allSchoolMap.get("skrs"))));//良好率
			allSchoolMap.put("yll", format.format(parseDouble(allSchoolMap.get("ylrs"))/parseDouble(allSchoolMap.get("skrs"))));//优良率
			allSchoolMap.put("hgl", format.format(parseDouble(allSchoolMap.get("hgrs"))/parseDouble(allSchoolMap.get("skrs"))));//合格人数
			allSchoolMap.put("jgl", format.format(parseDouble(allSchoolMap.get("jgrs"))/parseDouble(allSchoolMap.get("skrs"))));//及格人数
			allSchoolMap.put("jcl", format.format(parseDouble(allSchoolMap.get("jcrs"))/parseDouble(allSchoolMap.get("skrs"))));//极差人数
			allSchoolMap.put("cyxs", df.format(parseDouble(allSchoolMap.get("bzc"))/parseDouble(allSchoolMap.get("pjf"))));//bzc——标准差 cyxs——差异系数
		}
		HashMap<String, Object> allSchool=new HashMap<>();
		allSchool.put("zjallSchool", allSchoolMap);
		rateList.add(allSchool);
		//全区优良率 平均分 排名
		//TODO 要改 随着选中门数变化
		mapForDistrict.remove("classCode");
		Map<String,Object> lastMap = schoolScoreService.getDistrictCountMap(mapForDistrict);
		lastMap.put("pjf", df.format(parseDouble(lastMap.get("zf"))/parseDouble(lastMap.get("zrs"))));
		lastMap.put("jgl", format.format(parseDouble(lastMap.get("jgrs"))/parseDouble(lastMap.get("zrs"))));
		lastMap.put("yll", format.format(parseDouble(lastMap.get("ylrs"))/parseDouble(lastMap.get("zrs"))));
		//全校总分情况
		mapForDistrict.put("schoolCode", schoolCode);
		Map<String, Object> xjLastMap=schoolScoreService.getDistrictCountMap(mapForDistrict);
		xjLastMap.put("pjf", df.format(parseDouble(xjLastMap.get("zf"))/parseDouble(xjLastMap.get("zrs"))));
		xjLastMap.put("jgl", format.format(parseDouble(xjLastMap.get("jgrs"))/parseDouble(xjLastMap.get("zrs"))));
		xjLastMap.put("yll", format.format(parseDouble(xjLastMap.get("ylrs"))/parseDouble(xjLastMap.get("zrs"))));
		jo.put("lastMap", lastMap);
		jo.put("xjLastMap", xjLastMap);
		jo.put("rateList", rateList);
		jo.put("districtList", districtList);
		jo.put("message", "success");
		jo.put("courseList", courseExaminfoList);
		return jo;
	}
	
	@RequestMapping(params = "command=analyzeExcellentRateImport")
	@ResponseBody
	public void analyzePersonalSamllScoreImport(@RequestParam("data") String data,HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException{
		data = new String (data.getBytes("iso8859-1"),"UTF-8");
		HashMap<String,String> staticMap = new HashMap();
		//TODO 改成enumMap工具类
		staticMap.put("yw","语文");staticMap.put("sx","数学");staticMap.put("yy","外语");staticMap.put("wl","物理");staticMap.put("hx","化学");staticMap.put("ty","体育");staticMap.put("sxzz","思想政治");
		staticMap.put("ls","历史");staticMap.put("dl","地理");staticMap.put("kx","科学");staticMap.put("ms","美术");staticMap.put("tzxkc","拓展型课程");staticMap.put("xxkj","信息科技");
		staticMap.put("yjxkc","研究型课程");staticMap.put("yyue","音乐");staticMap.put("njyy","牛津英语");staticMap.put("sw","生物");staticMap.put("xsjyy","高中新世纪英语");staticMap.put("zr","自然");
		staticMap.put("qm","期末");staticMap.put("qz","期中");staticMap.put("sxq","上学期");staticMap.put("xxq","下学期");
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		Map<String, Object> mapForDistrict = getSerializer().parseMap(data);
		String schoolCode = "";
		String loginName=SecurityContext.getPrincipal().getUsername();
		if("qpAdmin".equals(SecurityContext.getPrincipal().getUsername())){
			schoolCode = ConfigContext.getValue("framework.tmis.roleCode['qpOrgCode']");
		} else {
			schoolCode = examNumberManageService.getSchoolCodeByLoginName(loginName);
		}
		requestMap.put("schoolCode", schoolCode);
		mapForDistrict.put("schoolCode", schoolCode); 
		ArrayList<String> courseList = new ArrayList<>();
		//ArrayList<String> courseNameList = (ArrayList<String>) requestMap.get("courseName");
		ArrayList<String> classCodeList = (ArrayList<String>) requestMap.get("classCode");
		ArrayList<String> classNameList = (ArrayList<String>) requestMap.get("className");
		ArrayList<String> classCodeList2 = new ArrayList<>();
		ArrayList<String> classNameList2 = new ArrayList<>();
		mapForDistrict.put("isDistrict", true);
		mapForDistrict.put("classCodeList", classCodeList);
		JSONObject jo =new JSONObject();
		//总分 及格率 优良率 平均分 平均分排名
		DecimalFormat format = new DecimalFormat("0.00%");
		DecimalFormat df = new DecimalFormat("######0.00");  
		ArrayList<HashMap<String, Object>> districtList = new ArrayList<>();
		//获得选中的科目 根据他们获得每门考试的总分 优分 良分 及格分
		HashMap<String,HashMap> courseScoreMap = new HashMap<>();
		int allZf=0;
		int allYx=0;
		int allLh=0;
		int allJg=0;
		//查有没有选中的考试
		List<HashMap<String,String>> courseExaminfoList =schoolScoreService.getCourseExaminfoList(requestMap);
		if(courseExaminfoList.size()<1){
			jo.put("message", "faile");
			//return jo;
		}
		//算出选中考试的总分 优良及格总分
		for(HashMap<String,String> temp:courseExaminfoList){
			allZf+=parseInteger(temp.get("Exam_Zf"));
			allYx+=parseInteger(temp.get("Exam_Yx"));
			allLh+=parseInteger(temp.get("Exam_Lh"));
			allJg+=parseInteger(temp.get("Exam_Jg"));
			courseScoreMap.put(temp.get("Course"), temp);
			temp.put("courseName", staticMap.get(temp.get("Course")));
			courseList.add(temp.get("Course"));
		}
		
		for(int i=0;i<classCodeList.size();i++){
			mapForDistrict.put("classCode", classCodeList.get(i));
			mapForDistrict.put("allZf", allZf);
			mapForDistrict.put("allYx", allYx);
			mapForDistrict.put("allLh", allLh);
			mapForDistrict.put("allJg", allJg);
			HashMap<String, Object> districtRateMap = schoolScoreService.getDistrictRateMap(mapForDistrict);
			if(parseInteger(districtRateMap.get("zrs"))==0){
				continue;
			}
			classCodeList2.add(classCodeList.get(i));
			classNameList2.add(classNameList.get(i));
			districtRateMap.put("classCode", classCodeList.get(i));
			districtRateMap.put("pjf", df.format(parseDouble(districtRateMap.get("zf"))/parseDouble(districtRateMap.get("zrs"))));
			districtRateMap.put("jgl", format.format(parseDouble(districtRateMap.get("jgrs"))/parseDouble(districtRateMap.get("zrs"))));
			districtRateMap.put("yll", format.format(parseDouble(districtRateMap.get("ylrs"))/parseDouble(districtRateMap.get("zrs"))));
			districtList.add(districtRateMap);
		}
		Collections.sort(districtList, new Comparator<Map<String, Object>>() {
	        public int compare(Map<String, Object> o1, Map<String, Object> o2) {
	            return parseDouble(o1.get("pjf")).compareTo(parseDouble(o2.get("pjf")));
	        }
	    });
		Collections.reverse(districtList);
		for(int k=0;k<districtList.size();k++){
			districtList.get(k).put("pjfpm", k+1);
		}

		ArrayList<HashMap> rateList = new ArrayList<>();
		for(int h=0;h<courseList.size();h++){
			HashMap<String,Object> rateMap= new HashMap<>();
			requestMap.put("targetCourse", courseList.get(h));
			mapForDistrict.put("targetCourse", courseList.get(h));
			//全区每门
			mapForDistrict.put("Zf", courseScoreMap.get(courseList.get(h)).get("Exam_Zf"));
			mapForDistrict.put("Yx", courseScoreMap.get(courseList.get(h)).get("Exam_Yx"));
			mapForDistrict.put("Lh", courseScoreMap.get(courseList.get(h)).get("Exam_Lh"));
			mapForDistrict.put("Jg", courseScoreMap.get(courseList.get(h)).get("Exam_Jg"));
			requestMap.put("Zf", courseScoreMap.get(courseList.get(h)).get("Exam_Zf"));
			requestMap.put("Yx", courseScoreMap.get(courseList.get(h)).get("Exam_Yx"));
			requestMap.put("Lh", courseScoreMap.get(courseList.get(h)).get("Exam_Lh"));
			requestMap.put("Jg", courseScoreMap.get(courseList.get(h)).get("Exam_Jg"));
			HashMap<String, Object> districtRateMap = schoolScoreService.getOneCourseRateList(mapForDistrict);
			if(parseInteger(districtRateMap.get("skrs"))<1){
				continue;
			}
			districtRateMap.put("bzc", df.format(districtRateMap.get("bzc")));
			districtRateMap.put("pjf", df.format(districtRateMap.get("pjf")));
			districtRateMap.put("yxl", format.format(parseDouble(districtRateMap.get("yxrs"))/parseDouble(districtRateMap.get("skrs"))));
			districtRateMap.put("lhl", format.format(parseDouble(districtRateMap.get("lhrs"))/parseDouble(districtRateMap.get("skrs"))));
			districtRateMap.put("yll", format.format(parseDouble(districtRateMap.get("ylrs"))/parseDouble(districtRateMap.get("skrs"))));
			districtRateMap.put("hgl", format.format(parseDouble(districtRateMap.get("hgrs"))/parseDouble(districtRateMap.get("skrs"))));
			districtRateMap.put("jgl", format.format(parseDouble(districtRateMap.get("jgrs"))/parseDouble(districtRateMap.get("skrs"))));
			districtRateMap.put("jcl", format.format(parseDouble(districtRateMap.get("jcrs"))/parseDouble(districtRateMap.get("skrs"))));
			districtRateMap.put("cyxs", df.format(parseDouble(districtRateMap.get("bzc"))/parseDouble(districtRateMap.get("pjf"))));
			districtRateMap.put("course", courseList.get(h));
			districtRateMap.put("courseName", staticMap.get(courseList.get(h)));//还要多表查询该门考试总分多少
			rateMap.put("district", districtRateMap);
			//各个班级每门
			List<HashMap<String, Object>> oneCourseSchoolList = new ArrayList<>();
			for(int i=0;i<classCodeList2.size();i++){
				requestMap.put("classCode", classCodeList2.get(i));
				//传入全区单科平均分
				requestMap.put("districtPjf", districtRateMap.get("pjf"));
				HashMap<String, Object> oneCourseRateMap = schoolScoreService.getOneCourseRateList(requestMap);
				if(parseInteger(oneCourseRateMap.get("skrs"))<1){
					continue;
				}
				if(parseInteger(oneCourseRateMap.get("skrs"))>0){
					oneCourseRateMap.put("schoolName", classNameList2.get(i));
					oneCourseRateMap.put("schoolCode", classCodeList2.get(i));
					//优秀率 >90
					oneCourseRateMap.put("yxl", format.format(parseDouble(oneCourseRateMap.get("yxrs"))/parseDouble(oneCourseRateMap.get("skrs"))));
					//良好率 80~90
					oneCourseRateMap.put("lhl", format.format(parseDouble(oneCourseRateMap.get("lhrs"))/parseDouble(oneCourseRateMap.get("skrs"))));
					//优良率 >80
					oneCourseRateMap.put("yll", format.format(parseDouble(oneCourseRateMap.get("ylrs"))/parseDouble(oneCourseRateMap.get("skrs"))));
					oneCourseRateMap.put("ylll", ArithmeticUtil.div(parseDouble(oneCourseRateMap.get("ylrs")), parseDouble(oneCourseRateMap.get("skrs"))));
					//合格率 60~80
					oneCourseRateMap.put("hgl", format.format(parseDouble(oneCourseRateMap.get("hgrs"))/parseDouble(oneCourseRateMap.get("skrs"))));
					//及格率 >60
					oneCourseRateMap.put("jgl", format.format(parseDouble(oneCourseRateMap.get("jgrs"))/parseDouble(oneCourseRateMap.get("skrs"))));
					oneCourseRateMap.put("bjgrs", parseDouble(oneCourseRateMap.get("skrs"))-parseDouble(oneCourseRateMap.get("jgrs")));
					//极差率 <20
					oneCourseRateMap.put("jcl", format.format(parseDouble(oneCourseRateMap.get("jcrs"))/parseDouble(oneCourseRateMap.get("skrs"))));
					oneCourseRateMap.put("bzc", df.format(oneCourseRateMap.get("bzc")));
					oneCourseRateMap.put("pjf", df.format(oneCourseRateMap.get("pjf")));
					//标准差，两位小数
					
					//标准分
					String bzf = df.format((parseDouble(oneCourseRateMap.get("pjf"))-parseDouble(districtRateMap.get("pjf")))/parseDouble(districtRateMap.get("bzc")));
					oneCourseRateMap.put("bzf",bzf);
					//T值
					oneCourseRateMap.put("T", df.format(parseDouble(bzf)*10+50));
					//差异系数
					oneCourseRateMap.put("cyxs", df.format(parseDouble(oneCourseRateMap.get("bzc"))/parseDouble(oneCourseRateMap.get("pjf"))));
					//偏差率
					oneCourseRateMap.put("pcl", df.format((parseDouble(oneCourseRateMap.get("pjf"))-parseDouble(districtRateMap.get("pjf")))/parseDouble(districtRateMap.get("pjf"))*100));
					//超均率
					oneCourseRateMap.put("cjl", format.format(parseDouble(oneCourseRateMap.get("cjrs"))/parseDouble(oneCourseRateMap.get("skrs"))));
					for(HashMap temp2:districtList){
						if(temp2.get("classCode").equals(classCodeList2.get(i))){
							oneCourseRateMap.put("zfpjfpm",temp2.get("pjfpm"));
						}
					}
					oneCourseSchoolList.add(oneCourseRateMap);
				}
				//[{ylrs=7, lhrs=7, skrs=168, zgf=83, yxrs=0, zdf=55, pjf=69.238095, hgrs=155}]
				//拿到了 优秀人数 优良人数 良好人数 实考人数 最高分 最低分 平均分 合格人数
			}
			//先排平均分 再排偏差率
			Collections.sort(oneCourseSchoolList, new Comparator<Map<String, Object>>() {
		        public int compare(Map<String, Object> o1, Map<String, Object> o2) {
		            return parseDouble(o1.get("pjf")).compareTo(parseDouble(o2.get("pjf")));
		        }
		    });
			Collections.reverse(oneCourseSchoolList);
			for(int k=0;k<oneCourseSchoolList.size();k++){
				oneCourseSchoolList.get(k).put("pjfpm", k+1);
			}
			//偏差率排名
			Collections.sort(oneCourseSchoolList, new Comparator<Map<String, Object>>() {
		        public int compare(Map<String, Object> o1, Map<String, Object> o2) {
		            return parseDouble(o1.get("pcl")).compareTo(parseDouble(o2.get("pcl")));
		        }
		    });
			Collections.reverse(oneCourseSchoolList);
			for(int k=0;k<oneCourseSchoolList.size();k++){
				oneCourseSchoolList.get(k).put("pclpm", k+1);
			}
			//优良率排名
			Collections.sort(oneCourseSchoolList, new Comparator<Map<String, Object>>() {
		        public int compare(Map<String, Object> o1, Map<String, Object> o2) {
		            return parseDouble(o1.get("ylll")).compareTo(parseDouble(o2.get("ylll")));
		        }
		    });
			Collections.reverse(oneCourseSchoolList);
			for(int k=0;k<oneCourseSchoolList.size();k++){
				oneCourseSchoolList.get(k).put("yllpm", k+1);
			}
			
			
			Collections.sort(oneCourseSchoolList, new Comparator<Map<String, Object>>() {
		        public int compare(Map<String, Object> o1, Map<String, Object> o2) {
		            return parseDouble(o1.get("zfpjfpm")).compareTo(parseDouble(o2.get("zfpjfpm")));
		        }
		    });
			
			rateMap.put("schools", oneCourseSchoolList);
			rateList.add(rateMap);
		};
		List<Map<String,Object>> minAndMax=new ArrayList<>();
		if(mapForDistrict.get("chooseFunction")!=null && mapForDistrict.get("chooseFunction").equals("1")){
			requestMap.put("targetCourse", courseList.get(0));
			
			for(int i=0;i<classCodeList2.size();i++){
				mapForDistrict.put("targetClassCode", classCodeList2.get(i));
				HashMap<String,Object> schoolMap =new HashMap<>();
				schoolMap.put("className", classNameList2.get(i));
				List<String> minNameList = schoolScoreService.getMinNameList(mapForDistrict);
				if(minNameList.size()<1){
					continue;
				}
				String minList="";
				for(String temp :minNameList){
					minList+=temp;
					minList+=",";
				}
				schoolMap.put("minList", minList.substring(0, minList.length()-1));
				
				List<String> maxNameList = schoolScoreService.getMaxNameList(mapForDistrict);
				
				String maxList="";
				for(String temp :maxNameList){
					maxList+=temp;
					maxList+=",";
				}
				schoolMap.put("maxList", maxList.substring(0, maxList.length()-1));
				minAndMax.add(schoolMap);
			}
			jo.put("minAndMax", minAndMax);
		}
		//全区优良率 平均分 排名
		//TODO 要改 随着选中门数变化
		mapForDistrict.remove("classCode");
		Map<String,Object> lastMap = schoolScoreService.getDistrictCountMap(mapForDistrict);
		lastMap.put("pjf", df.format(parseDouble(lastMap.get("zf"))/parseDouble(lastMap.get("zrs"))));
		lastMap.put("jgl", format.format(parseDouble(lastMap.get("jgrs"))/parseDouble(lastMap.get("zrs"))));
		lastMap.put("yll", format.format(parseDouble(lastMap.get("ylrs"))/parseDouble(lastMap.get("zrs"))));
		jo.put("lastMap", lastMap);
		jo.put("rateList", rateList);
		jo.put("districtList", districtList);
		jo.put("message", "success");
		jo.put("courseList", courseExaminfoList);
		if("1".equals(mapForDistrict.get("chooseFunction"))){
			List<Map<String,Object>> danke=(List<Map<String, Object>>) rateList.get(0).get("schools");
			Map<String,Object> xj=(Map<String, Object>) rateList.get(0).get("district");
			xj.put("schoolName","小计");
			danke.add(xj);
			String fileName="优良率分析.xls";
			String sheetName1="优良率分析列表";
			String[] title1={"序号","班级名称","实考人数","最高分","最低分","平均分","平均分排名","优率","良率",
					"优良率","优良率排名","合格率","及格率","极差率","标准差","标准分","T值","差异系数","超均率",
					"偏差率","偏差率排名"};
			String[] key1={"xh","schoolName","skrs","zgf","zdf","pjf","pjfpm","yxl","lhl",
					"yll","yllpm","hgl","jgl","jcl","bzc","bzf","T","cyxs","cjl",
					"pcl","pclpm"};
			//ExportUtil.ExportExcel(response, title1, fileName, sheetName, danke, key1);
			//String fileName2="最高分最低分名单.xls";
			String sheetName2="最高分最低分列表";
			String[] title2={"序号","班级名称","最高分名单","最低分名单"};
			String[] key2={"xh","className","maxList","minList"};
			//ExportUtil.ExportExcel(response, title2, fileName2, sheetName2, minAndMax, key2);
			ExportUtil.Export2SheetExcel(response, fileName, title1, key1, sheetName1, danke, sheetName2, title2, key2, minAndMax);
			
			
		}else if("2".equals(mapForDistrict.get("chooseFunction"))){
			
			List<String> kmList=new ArrayList<>();
			for(Map<String,Object> map:rateList){
				Map<String,Object> kmMap=(Map) map.get("district");
				for(int i=0;i<4;i++){
					kmList.add(formatString(kmMap.get("courseName")));
				}
				
			}
			List<Map<String,Object>> danke=(List<Map<String, Object>>) rateList.get(0).get("schools");
			Map<String,Object> xj=(Map<String, Object>) rateList.get(0).get("district");
			xj.put("schoolName","小计");
			danke.add(xj);
			String fileName="优良率分析.xls";
			String sheetName="优良率分析列表";
			List<String> title1=new ArrayList<>();
			List<String> key=new ArrayList<>();
			//第一行标题
			title1.add("序号");
			title1.add("班级");
			title1.add("实考人数");
			key.add("xh");
			key.add("schoolName");
			key.add("skrs");
			title1.addAll(kmList);
			title1.add("总分");
			List<String> title2=new ArrayList<>();
			//合并单元格，对应excel中的行和列，下表从0开始{"开始行,结束行,开始列,结束列"}
			List<String> head1=new ArrayList<>();
			head1.add("0,1,0,0");
			head1.add("0,1,1,1");
			head1.add("0,1,2,2");
			title2.add("序号");
			title2.add("班级");
			title2.add("实考人数");
			List<String> head2=new ArrayList<>();
			//第二行标题
			int a=3;//开始
			int b=6;//结束
			int k=0;//记录总数
			for(int i=0;i<kmList.size()/4+1;i++){
				title2.add("及格率");
				title2.add("优良率");
				title2.add("平均分");
				title2.add("平均分排名");
				key.add("jgl");
				key.add("yll");
				key.add("pjf");
				key.add("pjfpm");
				head1.add("0,0,"+a+","+b);
				
				a+=4;
				b+=4;
				k++;
				
			}
			int c=2;
			
			for(int i=0;i<k;i++){
				head2.add("1,1,"+c+","+c);
				c++;
			}
			
			 List<List<String>> newData=new ArrayList<>();
	            
				List<String> totalList=new ArrayList<>();
				totalList.add("小计");
				totalList.add(formatString(((Map<String, Object>)rateList.get(0).get("district")).get("skrs")));
	            for(int i=0;i<classNameList2.size();i++){
	            	List<String> paramList=new ArrayList<>();
	            	paramList.add(stuManagementService.selectClassNameByClassCode(formatString(districtList.get(i).get("classCode"))));
	            	paramList.add(formatString(districtList.get(i).get("zrs")));
	            	for(int j=0;j<rateList.size();j++){
	            		 Map<String,Object> map1=rateList.get(j);
	            		 List<Map<String,Object>> list=(List<Map<String, Object>>) map1.get("schools");
	            		 Map<String,Object> totalMap=(Map<String, Object>) map1.get("district");
	            		 if(i==classNameList2.size()-1){
	            			 
	            			 totalList.add(formatString(totalMap.get("jgl")));
		            		 totalList.add(formatString(totalMap.get("yll")));
		            		 totalList.add(formatString(totalMap.get("pjf")));
		            		 totalList.add(formatString(totalMap.get("pjfpm")));
	            		 }
            			 Map<String,Object> map2=list.get(i);
            			 paramList.add(formatString(map2.get("jgl")));
            			 paramList.add(formatString(map2.get("yll")));
            			 paramList.add(formatString(map2.get("pjf")));
            			 paramList.add(formatString(map2.get("pjfpm")));
	            		 
	            	}
	            	paramList.add(formatString(districtList.get(i).get("jgl")));
	            	paramList.add(formatString(districtList.get(i).get("yll")));
	            	paramList.add(formatString(districtList.get(i).get("pjf")));
	            	paramList.add(formatString(districtList.get(i).get("pjfpm")));
	            	newData.add(paramList);
	            }
	            totalList.add(formatString(lastMap.get("jgl")));
	            totalList.add(formatString(lastMap.get("yll")));
	            totalList.add(formatString(lastMap.get("pjf")));
	            totalList.add(formatString(lastMap.get("pjfpm")));
	            newData.add(totalList);
			/*head1.add("0,0,2,5");
			head1.add("0,1,0,0");
			head1.add("0,1,0,0");
			String[] title1={"序号","学校","实考人数","最高分","最低分","平均分","平均分排名","优率","良率",
					"优良率","优良率排名","合格率","及格率","极差率","标准差","标准分","T值","差异系数","超均率",
					"偏差率","偏差率排名"};
			String[] title2={"序号","学校","实考人数","","最低分","平均分","平均分排名","优率","良率",
					"优良率","优良率排名","合格率","及格率","极差率","标准差","标准分","T值","差异系数","超均率",
					"偏差率","偏差率排名"};
			String[] title3={"及格率","优良率","平均分","平均分排名"};
			String[] key1={"xh","schoolName","skrs","zgf","zdf","pjf","pjfpm","yxl","lhl",
					"yll","yllpm","hgl","jgl","jcl","bzc","bzf","T","cyxs","cjl",
					"pcl","pclpm"};*/
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
	            HSSFRow row1 = sheet.createRow((short) 1);
	            row.setHeight((short) 350);
	            
	            //设置页名
	            workbook.setSheetName(0,sheetName);
	            for(int j=0;j<title1.size();j++){
	            	//设置列长
	 	            sheet.setColumnWidth((short) j, (short) 5000);
	 	            //设置title
	 	           HSSFCell cell = row.createCell((short) j);
	               cell.setCellStyle(titleStyle);
	               cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	               cell.setCellValue(title1.get(j));
	            }
	            for(int j=0;j<title2.size();j++){
	            	//设置列长
	 	            sheet.setColumnWidth((short) j, (short) 5000);
	 	            //设置title
	 	           HSSFCell cell = row1.createCell((short) j);
	               cell.setCellStyle(titleStyle);
	               cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	               cell.setCellValue(title2.get(j));
	            }
	            //动态合并单元格
	            for (int i = 0; i < head1.size(); i++) {
	                String[] temp = head1.get(i).split(",");
	                Integer startrow = Integer.parseInt(temp[0]);
	                Integer overrow = Integer.parseInt(temp[1]);
	                Integer startcol = Integer.parseInt(temp[2]);
	                Integer overcol = Integer.parseInt(temp[3]);
	                sheet.addMergedRegion(new CellRangeAddress(startrow, overrow,
	                        startcol, overcol));
	            }
	          //动态合并单元格
	            for (int i = 0; i < head2.size(); i++) {
	                String[] temp = head2.get(i).split(",");
	                Integer startrow = Integer.parseInt(temp[0]);
	                Integer overrow = Integer.parseInt(temp[1]);
	                Integer startcol = Integer.parseInt(temp[2]);
	                Integer overcol = Integer.parseInt(temp[3]);
	                sheet.addMergedRegion(new CellRangeAddress(startrow, overrow,
	                        startcol, overcol));
	            }
	         // 写入实体数据,title和keys相对应，顺序一致，用于赋值
	            for (int i = 0; i < newData.size(); i++) {
	                List<String> paramMap = newData.get(i);
	                //序号，默认都有,从第二行开始
	                row = sheet.createRow((short) i + 2);
		            HSSFCell cell = row.createCell((short) 0);
		            cell.setCellStyle(style);
		            cell.setCellValue((i + 1));
		            
		            //把值写入表格
		            for(int m=1;m<title2.size();m++){
		            	HSSFCell keyCell = row.createCell((short) m);
		            	keyCell.setCellStyle(style);
		            	keyCell.setCellValue(parseString(paramMap.get(m-1)));
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
	}
	
	/**
	 * 
	 * @Title: getClassByGrade
	 * @Description: update 实验中学（东）的schoolCode取实验中学（东）的
	 * @author jay zhong
	 * @date 2017年11月6日 下午5:15:19 
	 * @return JSONObject
	 */
	@RequestMapping(params = "command=getClassByGrade")
	@ResponseBody
	public JSONObject getClassByGrade(@RequestParam("data") String data){
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		String schoolCode = "";
		String schoolCode2 = "";
		String loginName=SecurityContext.getPrincipal().getUsername();
		if("qpAdmin".equals(SecurityContext.getPrincipal().getUsername())){
			schoolCode = ConfigContext.getValue("framework.tmis.roleCode['qpOrgCode']");
		}else {
			schoolCode = examNumberManageService.getSchoolCodeByLoginName(loginName);
			schoolCode2=schoolCode;
			//如果code是3062（实验中学东），就替换为3008（实验中学西）
			if(ConfigContext.getStringSection("yczxSchoolCode").equals(schoolCode)){
				schoolCode=ConfigContext.getStringSection("syzxSchoolCode");
			}
		}
		List<Map<String,String>> classList = schoolScoreService.getClassBySchoolCodeAndGrade(schoolCode,parseString(requestMap.get("grade")),schoolCode2);
		JSONObject jo =new JSONObject();
		jo.put("classList", classList);
 		return jo;
	}
	
	
	@RequestMapping(params = "command=getClassByTeacher")
	@ResponseBody
	public JSONObject getClassByTeacher(@RequestParam("data") String data){
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		String loginName=SecurityContext.getPrincipal().getUsername();
		List<Map<String,String>> classList = schoolScoreService.getClassByTeacher(loginName,parseString(requestMap.get("grade")));
		JSONObject jo =new JSONObject();
		jo.put("classList", classList);
 		return jo;
	}
	@Autowired
	private SmallTitleService smallTitleService;
}
