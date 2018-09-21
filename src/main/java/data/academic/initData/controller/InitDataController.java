package data.academic.initData.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import data.academic.initData.service.InitDataService;
import data.academic.util.HttpClientUtil;
import data.framework.support.AbstractBaseController;
import data.framework.support.ConfigContext;
import data.platform.authority.security.SecurityContext;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @Title: InitDataController
 * @Description: 初始化数据控制层
 * @author zhaohuanhuan
 * @date 2016年8月2日
 */

@Controller
@RequestMapping("dataSynchronization/dataInit")
public class InitDataController extends AbstractBaseController {

	@Override
	protected void init(ModelMap model, HttpServletRequest request) {

	}

	HttpClientUtil httpClientUtil = new HttpClientUtil();
	String url = ConfigContext.getStringSection("framework.web.academic.https.path");
	String charset = "utf-8";
	boolean success = false;

	/**
	 * @Title: addTeachers
	 * @Description: 根据获取教师接口获取教师信息并导入教师信息
	 * @author zhaohuanhuan
	 * @date 2016年8月2日
	 * @param data
	 * @param out
	 * @throws Exception
	 * @return void
	 */
	@RequestMapping(params = "command=addTeachers")
	public void addTeachers(java.io.PrintWriter out, HttpSession session) {
		String httpUrl = url + "getTeacherData";
		initDataService.removeTeacher(); // 每次初始化时清空老师数据
		initDataService.removeTeacherRefSchool();// 每次初始化时清空老师学校关系数据
		initDataService.removeUsersForBua("1");// 初始化时清空bua里所有相关用户
		Map<String, Object> resultMap;
		try {
			resultMap = initData(httpUrl, "teacher","500");
			String successStr=resultMap.get("success").toString();
			if (successStr.equals("true")) {
				resultMap.put("message", "教师数据同步成功");
			} else {
				resultMap.put("message", "教师数据同步失败");
			}
			out.print(getSerializer().formatObject(resultMap));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @Title: addSchools
	 * @Description: 根据获取教育单位接口获取教育单位信息并导入教育单位信息
	 * @author zhaohuanhuan
	 * @date 2016年8月3日
	 * @param out
	 * @throws Exception
	 * @return void
	 */
	@RequestMapping(params = "command=addSchools")
	public void addSchools(java.io.PrintWriter out) throws Exception {
			initDataService.removeSchool();
			initDataService.removeUsersAndOrgs();// 初始化时清空bua里所有相关用户和机构
			// 获取数据的地址
			String httpUrl = url + "getUnitData";
			Map<String, Object> resultMap =initData(httpUrl, "unit","500");
			String successStr=resultMap.get("success").toString();
			if (successStr.equals("true")) {
				resultMap.put("message", "教育单位数据同步成功");
			} else {
				resultMap.put("message", "教育单位同步失败");
			}
			out.print(getSerializer().formatObject(resultMap));
	}

	/**
	 * @Title: addStudents
	 * @Description: 根据获取学生接口获取学生信息并导入学生信息
	 * @author zhaohuanhuan
	 * @date 2016年8月5日
	 * @param out
	 * @throws Exception
	 * @return void
	 */
	@RequestMapping(params = "command=addStudents")
	public void addStudents(java.io.PrintWriter out) throws Exception {
		String httpUrl = url + "getStudentData";
		initDataService.removeStudent();// 删除学生数据
		initDataService.removeStudentRefSchool();// 删除学生学校表数据
		initDataService.removeStudentRefClass();// 删除学生班级表数据
		initDataService.removeUsersForBua("3");//删除bua中学生相关数据
		Map<String, Object> resultMap =initData(httpUrl, "student","500");
		String successStr=resultMap.get("success").toString();
		if (successStr.equals("true")) {
			resultMap.put("message", "学生数据同步成功");
		} else {
			resultMap.put("message", "学生数据同步失败");
		}
		out.print(getSerializer().formatObject(resultMap));
	}

	/**
	 * @Title: addCourse
	 * @Description: 根据获取科目接口获取科目信息并导入科目信息
	 * @author zhaohuanhuan
	 * @date 2016年8月23日
	 * @param out
	 * @throws Exception
	 * @return void
	 */
	@RequestMapping(params = "command=addCourse")
	public void addCourse(java.io.PrintWriter out) throws Exception {
		// 删除科目
		initDataService.removeCourse();
		String httpUrl = url + "getSubjectData";
		Map<String, Object> resultMap =initData(httpUrl, "course","500");
		String successStr=resultMap.get("success").toString();
		if (successStr.equals("true")) {
			resultMap.put("message", "科目数据同步成功");
		} else {
			resultMap.put("message", "科目数据同步失败");
		}
		out.print(getSerializer().formatObject(resultMap));
	}

	/**
	 * @Title: addClass
	 * @Description: 根据获取班级接口获取班级信息并导入班级信息
	 * @author zhaohuanhuan
	 * @date 2016年8月10日
	 * @param out
	 * @throws Exception
	 * @return void
	 */
	@RequestMapping(params = "command=addClass")
	public void addClass(java.io.PrintWriter out) throws Exception {
		// 删除班级
		initDataService.removeClass();
		String httpUrl = url + "getClassData";
		Map<String, Object> resultMap =initData(httpUrl, "class","200");
		String successStr=resultMap.get("success").toString();
		if (successStr.equals("true")) {
			resultMap.put("message", "班级数据同步成功");
		} else {
			resultMap.put("message", "班级数据同步失败");
		}
		out.print(getSerializer().formatObject(resultMap));
	}

	/**
	 * @Title: initKnowledgeData
	 * @Description: 初始话知识库数据
	 * @author zhaohuanhuan
	 * @date 2017年9月4日 
	 * @return void
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(params = "command=initKnowledgeData")
	public void initKnowledgeData(java.io.PrintWriter out) throws Exception{
		//删除知识点
		initDataService.removeKnowledge();
		String httpUrl=url + "getKnowledgeData";
		Map<String, Object> resultMap=initData(httpUrl,"knowledge","200");
		String successStr=resultMap.get("success").toString();
		if (successStr.equals("true")) {
			resultMap.put("message", "知识点数据同步成功");
		} else {
			resultMap.put("message", "知识点数据同步失败");
		}
		out.print(getSerializer().formatObject(resultMap));
	}
	/**
	 * @Title: initData
	 * @Description: 初始化数据
	 * @author zhaohuanhuan
	 * @date 2017年8月29日
	 * @return Map<String,Object>
	 * @param httpUrl
	 * @param success
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> initData(String httpUrl, String type,String number)  throws Exception{
		Map<String, Object> map = new HashMap<>();
		boolean success = false;
		try {
		Map<String, Object> getInitParamMap = new HashMap<>();
		getInitParamMap.put("access_token", httpClientUtil.getAccessToken());// 访问令牌
		getInitParamMap.put("number", number);// 每页显示多少条
		String getInitData = httpClientUtil.doPost(httpUrl, getInitParamMap, charset);// 得到接口返回的教育单位的数据的页数
		JSONObject initDataJson = JSONObject.fromObject(getInitData);// String==>json
		JSONObject page = initDataJson.getJSONObject("page");// 获得page json对象
		Integer pages = Integer.parseInt(page.getString("pages"));	// 获取数据的总页数
		for (int currentPage = 1; currentPage <= pages; currentPage++) {
			getInitParamMap.put("currentPage", String.valueOf(currentPage));// 每页显示多少条
			String getUnitData = httpClientUtil.doPost(httpUrl, getInitParamMap, charset);// 得到接口返回的教育单位的数据
			JSONObject jsons = JSONObject.fromObject(getUnitData);// String==>json
			JSONArray initDataArray = JSONArray.fromObject(jsons.getString("data"));// 获得data数组
			for (int j = 0; j < initDataArray.size(); j++) {
				JSONObject ob = (JSONObject) initDataArray.get(j);
				if (type.equals("unit")) {
					if (!ob.containsKey("DWBBM")) {
						ob.put("DWBBM", "");
					}
					if (!ob.containsKey("DWJC")) {
						ob.put("DWJC", "");
					}
					if (!ob.containsKey("DWDM")) {
						ob.put("DWDM", "");
						ob.put("sequence", "");
					} else {
						Integer sequence;
						String str = ob.get("DWDM").toString();
						sequence = parseInteger(str.charAt(6));
						ob.put("sequence", sequence.toString());
					}
					if (!ob.containsKey("DWH")) {
						ob.put("DWH", "");
					}
					if (!ob.containsKey("DWLBM")) {
						ob.put("DWLBM", "");
					}
					ob.put("identifier", 1);
				} else if (type.equals("teacher")) {
					if (!ob.containsKey("loginName")) {
						ob.put("loginName", "");
					}
					ob.put("identifier", 1);
					ob.put("roleId", ConfigContext.getStringSection("academic.teacherRoleId"));
				} else if (type.equals("student")) {
					if (!ob.containsKey("SFZJH")) {
						ob.put("SFZJH", "SFZJH1");
					}
					if (!ob.containsKey("XXPK")) {
						ob.put("XXPK", "XXPK");
					}
					if(!ob.containsKey("XM")){
						ob.put("XM", "XM");
					}
					if (!ob.containsKey("STATE")) {
						ob.put("STATE", "STATE");
						ob.put("STATE_CODE", "qt");
					} else {
						String state = ob.get("STATE").toString();
						if (state.equals("在读")) {
							ob.put("STATE_CODE", "zd");
						}
						if (state.equals("转出到外区（地）")) {
							ob.put("STATE_CODE", "zc");
						}
						if (state.equals("休学")) {
							ob.put("STATE_CODE", "xiux");
						}
						if (state.equals("退学")) {
							ob.put("STATE_CODE", "tx");
						}
						if (state.equals("伤亡")) {
							ob.put("STATE_CODE", "sw");
						}
					}
					if(!ob.containsKey("BJID")){
						ob.put("BJID", "BJID");
					}
					ob.put("createPerson",SecurityContext.getPrincipal().getChineseName());
					ob.put("identifier", 3);
					ob.put("roleId", ConfigContext.getStringSection("academic.studentRoleId"));
				}else if(type.equals("class")){
					if (!ob.containsKey("NJDM")) {
						ob.put("NJDM", "");
					}
					if (!ob.containsKey("BJMC")) {
						ob.put("BJMC", "");
					}
					if (!ob.containsKey("XXID")) {
						ob.put("XXID", "");
					}
					if (!ob.containsKey("NJID")) {
						ob.put("NJID", "");
					}
					if (!ob.containsKey("CLASSNO")) {
						ob.put("CLASSNO", "");
					}
					if (!ob.containsKey("NJMC")) {
						ob.put("NJMC", "");
					}
					if (!ob.containsKey("BJID")) {
						ob.put("BJID", "");
					}
				}else if("knowledge".equals(type)){
					if(!ob.containsKey("PARENT_ID")){
						ob.put("PARENT_ID", "");
					}
				}
			}
			// json数组==>list
			List<Map<String, Object>> initDataList = (List<Map<String, Object>>) JSONArray.toCollection(initDataArray,
					Map.class);
			if ("unit".equals(type)) {//教育单位
				initDataService.insertSchool(initDataList);// 同步教育单位数据
				initDataService.insertUnitForBua(initDataList);// 同步教育单位数据到bua
			} else if ("teacher".equals(type)) {//教师
				initDataService.insertTeacher(initDataList);// 同步教师数据
				initDataService.insertTeaRefSchool(initDataList);// 教师学校关系表同步
				initDataService.insertTeacherForBua(initDataList);// 同步教师数据到bua
				initDataService.insertUserRoleForBua(initDataList);// 赋予教师角色权限
			} else if ("student".equals(type)) {//学生
				initDataService.insertStudent(initDataList);// 同步学生数据
				initDataService.insertStudentRefSchool(initDataList);// 同步学生学校数据
				initDataService.insertStudentRefClass(initDataList);// 同步学生班级数据
				initDataService.insertStuentForBua(initDataList);//同步到学生数据到bua
				initDataService.insertUserRoleForBua(initDataList);//赋予学生角色
			}else if("course".equals(type)){//科目
				initDataService.insertCourse(initDataList); //同步科目数据
			}else if("class".equals(type)){//班级
				initDataService.insertClass(initDataList);//同步班级数据
			}else if("knowledge".equals(type)){
				initDataService.insertKnowledgeData(initDataList);
			}
			if (currentPage == pages) {
				success = true;
			}
		}
		}catch (Exception e) {
			System.out.println(e);
			map.put("success", success);
		} finally {
			map.put("success", success);
		}
		return map;
	}
	@Autowired
	private InitDataService initDataService;

}
