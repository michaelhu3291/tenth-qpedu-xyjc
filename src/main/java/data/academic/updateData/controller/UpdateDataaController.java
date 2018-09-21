package data.academic.updateData.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import data.academic.updateData.service.UpdateDataService;
import data.academic.util.HttpClientUtil;
import data.framework.httpClient.RestResponse;
import data.framework.support.AbstractBaseController;
import data.framework.support.ConfigContext;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @Title: UpdateDataController
 * @Description: 对更新接口获取的的数据进行增删改控制层
 * @author zhaohuanhuan
 * @date 2016年8月4日
 */
@Controller
@RequestMapping("/updateData")
public class UpdateDataaController extends AbstractBaseController {

	HttpClientUtil httpClientUtil = new HttpClientUtil();
	String url = ConfigContext.getStringSection("framework.web.academic.https.path");
	String charset = "utf-8";

	@Override
	protected void init(ModelMap model, HttpServletRequest request) {
		// TODO Auto-generated method stub

	}

	/**
	 * @Title: updateTeacherData
	 * @Description: 获得更新教师接口的数据并进行增删改操作
	 * @author zhaohuanhuan
	 * @date 2016年8月4日
	 * @param message
	 * @return
	 * @return RestResponse
	 */
	@RequestMapping(value = "/updateTeacherData", method = RequestMethod.POST)
	public @ResponseBody RestResponse updateTeacherData(@RequestBody String message) {
		RestResponse rsp = new RestResponse();
		String updateDataUrl = url + "getTeacherDataUpdate";
		Map<String, Object> updataTeacherDataMap = new HashMap<String, Object>();
		updataTeacherDataMap.put("access_token", httpClientUtil.getAccessToken());
		updataTeacherDataMap.put("number", "500");
		String httpOrgCreateTestRtn = httpClientUtil.doPost(updateDataUrl, updataTeacherDataMap, charset);
		JSONObject json = JSONObject.fromObject(httpOrgCreateTestRtn);
		JSONArray array = JSONArray.fromObject(json.getString("data"));
		if (array.size() > 0) {
			JSONObject object = json.getJSONObject("page");
			// 总页数
			Integer pages = Integer.parseInt(object.getString("pages"));
			for (int currentPage = 1; currentPage <= pages; currentPage++) {
				// 每页显示多少条
				updataTeacherDataMap.put("currentPage", String.valueOf(currentPage));
				/**
				 * 得到更新接口返回的教师的数据
				 */
				String getTeacherData = httpClientUtil.doPost(updateDataUrl, updataTeacherDataMap, charset);
				// String==>json
				JSONObject jsons = JSONObject.fromObject(getTeacherData);
				// 获得data数组
				JSONArray updateTeaArray = JSONArray.fromObject(jsons.getString("data"));
				// json数组==>list
				List<Map<String, Object>> updateTeaList = (List<Map<String, Object>>) JSONArray
						.toCollection(updateTeaArray, Map.class);
				for (int i = 0; i < updateTeaArray.size(); i++) {
					JSONObject ob = (JSONObject) updateTeaArray.get(i);
					String updateType = ob.getString("updateType");
					if (updateType .equals("add")) {
						updateDataService.batchInsertTeacher(ob);
					}
					if (updateType.equals("update") ) {
						updateDataService.batchUpdateTeacher(ob);
					}
					if (updateType.equals("delete")) {
						updateDataService.remove(ob.getString("Pk"));
					}
				}
			}
		}
		return rsp;
	}

	@Autowired
	private UpdateDataService updateDataService;

}
