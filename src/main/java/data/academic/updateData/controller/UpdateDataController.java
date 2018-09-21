package data.academic.updateData.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import data.academic.updateData.service.UpdateDataService;
import data.academic.util.HttpClientUtil;
import data.framework.support.AbstractBaseController;
import data.framework.support.ConfigContext;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @Title: UpdataData
 * @Description: 获取更新数据的方法
 * @author zhaohuanhuan
 * @date 2016年8月9日
 */
@Controller
@RequestMapping("updateData/updateData")
public class UpdateDataController extends AbstractBaseController {

	@Override
	protected void init(ModelMap model, HttpServletRequest request) {
		// TODO Auto-generated method stub
	}

	@Autowired
	private UpdateDataService updateDataService;

	HttpClientUtil httpClientUtil = new HttpClientUtil();
	String url = ConfigContext.getStringSection("framework.web.academic.https.path");
	String charset = "utf-8";

	/**
	 * @Title: getTeacherDataUpdate
	 * @Description: 根据教师更新接口获取教师更新的数据并进行操作
	 * @author zhaohuanhuan
	 * @date 2016年8月9日
	 * @return void
	 */
	@SuppressWarnings("unchecked")
	public void getTeacherDataUpdate(String accessToken) {
		String teacherUpdateDataUrl = url + "getTeacherDataUpdate";
		Map<String, Object> teacherDataUpdateMap = new HashMap<String, Object>();
		teacherDataUpdateMap.put("access_token", accessToken);
		teacherDataUpdateMap.put("number", "500");
		String httpOrgCreateTestRtn = httpClientUtil.doPost(teacherUpdateDataUrl, teacherDataUpdateMap, charset);
		JSONObject json = JSONObject.fromObject(httpOrgCreateTestRtn);
		JSONArray TeacherArray = JSONArray.fromObject(json.getString("data"));
		if (TeacherArray.size() > 0) {
			JSONObject object = json.getJSONObject("page");
			// 总页数
			Integer pages = Integer.parseInt(object.getString("pages"));
			for (int currentPage = 1; currentPage <= pages; currentPage++) {
				List<String> updatePkList = new ArrayList<>();
				// 每页显示多少条
				teacherDataUpdateMap.put("currentPage", currentPage + "");
				// 得到更新接口返回的教师的数据
				String getTeacherUpdateData = httpClientUtil.doPost(teacherUpdateDataUrl, teacherDataUpdateMap,
						charset);
				// String==>json
				JSONObject jsons = JSONObject.fromObject(getTeacherUpdateData);
				// 获得data数组
				JSONArray updateTeaArray = JSONArray.fromObject(jsons.getString("data"));
				for (int i = 0; i < updateTeaArray.size(); i++) {
					JSONObject ob = (JSONObject) updateTeaArray.get(i);
					if (!ob.containsKey("loginName")) {
						ob.put("loginName", "");
					}
					if (ob.containsKey("updateType")) {
						String updateType = ob.getString("updateType");
						if (updateType.equals("add")) {
							updateDataService.batchInsertTeacher(ob);
							ob.put("schoolCode", ob.get("pkSchool"));
							ob.put("identifier", 1);
							updateDataService.batchInsertUserForBua(ob);
						}
						if (updateType.equals("update")) {
							updateDataService.batchUpdateTeacher(ob);
							ob.put("schoolCode", ob.get("pkSchool"));
							updateDataService.batchUpdateUserByBua(ob);
						}
						if (updateType.equals("delete")) {
							updateDataService.remove(ob.getString("PK"));
							updateDataService.batchDeleteByBua(ob.getString("PK"));
						}
						updatePkList.add(ob.getString("updatePk"));
					}
				}
				this.updateCallback(accessToken, updatePkList);
				// json数组==>list
				List<Map<String, Object>> updateTeaList = (List<Map<String, Object>>) JSONArray
						.toCollection(updateTeaArray, Map.class);
				// 同步更新接口数据
				updateDataService.insertUpdateTeacher(updateTeaList);
			}
		}
	}

	/**
	 * @Title: getStudentDataUpdate
	 * @Description: 根据学生更新接口获取教师更新的数据并进行操作
	 * @author zhaohuanhuan
	 * @date 2016年8月9日
	 * @return void
	 */
	@SuppressWarnings("unchecked")
	public void getStudentDataUpdate(String accessToken) {
		String studentUpdateDataUrl = url + "getStudentDataUpdate";
		Map<String, Object> studentDataUpdatMap = new HashMap<String, Object>();
		studentDataUpdatMap.put("access_token", accessToken);
		studentDataUpdatMap.put("number", "500");
		String httpOrgCreateTestRtn = httpClientUtil.doPost(studentUpdateDataUrl, studentDataUpdatMap, charset);
		JSONObject json = JSONObject.fromObject(httpOrgCreateTestRtn);
		JSONArray studentArray = JSONArray.fromObject(json.getString("data"));
		if (studentArray.size() > 0) {
			JSONObject object = json.getJSONObject("page");
			// 总页数
			Integer pages = Integer.parseInt(object.getString("pages"));
			for (int currentPage = 1; currentPage <= pages; currentPage++) {
				List<String> updatePkList = new ArrayList<>();
				// 每页显示多少条
				studentDataUpdatMap.put("currentPage", currentPage + "");
				// 得到更新接口返回的学生的数据
				String getStudentUpdateData = httpClientUtil.doPost(studentUpdateDataUrl, studentDataUpdatMap, charset);
				// String==>json
				JSONObject jsons = JSONObject.fromObject(getStudentUpdateData);
				// 获得data数组
				JSONArray studentUpdateArray = JSONArray.fromObject(jsons.getString("data"));
				for (int i = 0; i < studentUpdateArray.size(); i++) {
					JSONObject ob = (JSONObject) studentUpdateArray.get(i);
					if (!ob.containsKey("BJID")) {
						ob.put("BJID", "");
					}
					if (!ob.containsKey("NJID")) {
						ob.put("NJID", "");
					}
					if (!ob.containsKey("STATE")) {
						ob.put("STATE", "");
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
					if (!ob.containsKey("SFZJH")) {
						ob.put("SFZJH", "");
					}
					if (!ob.containsKey("XJFH")) {
						ob.put("XJFH", "");
					}
					String updateType = ob.getString("updateType");
					if (updateType.equals("add")) {
						updateDataService.batchInsertStudent(ob);
						ob.put("schoolCode", ob.get("XXPK"));
						ob.put("loginName", ob.get("SFZJH"));
						ob.put("identifier", 3);
						updateDataService.batchInsertUserForBua(ob);
					}
					if (updateType.equals("update")) {
						updateDataService.batchUpdateStudent(ob);
						ob.put("schoolCode", ob.get("XXPK"));
						ob.put("loginName", ob.get("SFZJH"));
						updateDataService.batchUpdateUserByBua(ob);
					}
					if (updateType.equals("delete")) {
						updateDataService.removeStudent(ob.getString("Pk"));
						updateDataService.batchDeleteByBua(ob.getString("PK"));
					}
					updatePkList.add(ob.getString("updatePk"));
				}
				this.updateCallback(accessToken, updatePkList);
				// json数组==>list
				List<Map<String, Object>> updateStudentList = (List<Map<String, Object>>) JSONArray
						.toCollection(studentUpdateArray, Map.class);
				//同步学生更新接口数据
				updateDataService.insertUpdateStudent(updateStudentList);
			}
		}
	}

	/**
	 * @Title: getUnitDataUpdate
	 * @Description: 根据教育单位更新接口获取教师更新的数据并进行操作
	 * @author zhaohuanhuan
	 * @date 2016年8月9日
	 * @return void
	 */
	@SuppressWarnings("unchecked")
	public void getUnitDataUpdate(String accessToken) {
		String unitUpdateDataUrl = url + "getUnitDataUpdate";
		Map<String, Object> studentDataUpdatMap = new HashMap<String, Object>();
		studentDataUpdatMap.put("access_token", accessToken);
		studentDataUpdatMap.put("number", "500");
		String httpOrgCreateTestRtn = httpClientUtil.doPost(unitUpdateDataUrl, studentDataUpdatMap, charset);
		JSONObject json = JSONObject.fromObject(httpOrgCreateTestRtn);
		JSONArray unitArray = JSONArray.fromObject(json.getString("data"));
		if (unitArray.size() > 0) {
			JSONObject object = json.getJSONObject("page");
			// 总页数
			Integer pages = Integer.parseInt(object.getString("pages"));
			List<String> updatePkList = new ArrayList<>();
			for (int currentPage = 1; currentPage <= pages; currentPage++) {
				// 每页显示多少条
				studentDataUpdatMap.put("currentPage", currentPage + "");
				//得到更新接口返回的教育单位的数据
				String getUnitUpdateData = httpClientUtil.doPost(unitUpdateDataUrl, studentDataUpdatMap, charset);
				// String==>json
				JSONObject jsons = JSONObject.fromObject(getUnitUpdateData);
				// 获得data数组
				JSONArray updateUnitArray = JSONArray.fromObject(jsons.getString("data"));
				// json数组==>list
				for (int i = 0; i < updateUnitArray.size(); i++) {
					JSONObject ob = (JSONObject) updateUnitArray.get(i);
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
					if (ob.containsKey("updateType")) {
						String updateType = ob.getString("updateType");
						if (updateType.equals("add")) {
							updateDataService.batchInsertUnit(ob);
							updateDataService.batchInsertSchoolByBua(ob);
						}
						if (updateType.equals("update")) {
							updateDataService.batchUpdateUnit(ob);
							updateDataService.batchUpdateUnitForBua(ob);
						}
						if (updateType.equals("delete")) {
							updateDataService.removeUnit(ob.getString("DWH"));
							updateDataService.batchDeleteUnitForBua(ob.getString("DWH"));
						}
						updatePkList.add(ob.getString("updatePk"));
					}
					ob.put("identifier", 1);
				}
				this.updateCallback(accessToken, updatePkList);
				// json数组==>list
				List<Map<String, Object>> updateUnitList = (List<Map<String, Object>>) JSONArray
						.toCollection(updateUnitArray, Map.class);
				//同步教育单位更新接口数据
				updateDataService.insertUpdateUnit(updateUnitList);
			}
		}
	}

	/**
	 * @Title: updateCallback
	 * @Description: 获取数据更新结果
	 * @author zhaohuanhuan
	 * @date 2017年8月28日
	 * @return void
	 */
	@SuppressWarnings("unchecked")
	public void updateCallback(String accessToken, List<String> updatePkList) {
		String unitUpdateDataUrl = url + "updateCallback";
		JSONObject json = new JSONObject();
		StringBuilder sb = new StringBuilder();
		String pk_list = "";
		if (updatePkList.size() > 0) {
			for (int i = 0; i < updatePkList.size(); i++) {
				sb.append(updatePkList.get(i)).append(",");
			}
			pk_list = sb.toString().substring(0, sb.toString().length() - 1);
			json.put("access_token", accessToken);
			json.put("pk_list", pk_list);
			httpClientUtil.doPost(unitUpdateDataUrl, json, charset);
			System.out.println("更新" + updatePkList.size() + "条数据");
		}
	}
}
