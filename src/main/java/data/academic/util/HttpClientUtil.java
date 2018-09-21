package data.academic.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jdom.input.SAXBuilder;
import org.springframework.stereotype.Component;

import data.academic.updateData.controller.UpdateDataController;
import data.framework.support.ConfigContext;
import net.sf.json.JSONObject;

/**
 * @Title: HttpClientUtil
 * @Description: 利用HttpClient进行post请求的工具类
 * @author huxian
 * @date 2016年7月27日 下午3:39:04
 */
@Component
public class HttpClientUtil {
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String doPost(String url, Map<String, Object> paramMap, String charset) {
		HttpClient httpClient = null;
		HttpPost httpPost = null;
		String result = null;
		try {
			httpClient = new SSLClient();
			httpPost = new HttpPost(url);
			// 设置参数
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			Iterator iterator = paramMap.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, String> elem = (Entry<String, String>) iterator.next();
				list.add(new BasicNameValuePair(elem.getKey(), elem.getValue()));
			}
			if (list.size() > 0) {
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, charset);
				httpPost.setEntity(entity);
			}
			HttpResponse response = httpClient.execute(httpPost);
			if (response != null) {
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					result = EntityUtils.toString(resEntity, charset);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	/**
	 * @Title: getAccessToken
	 * @Description: 获取访问令牌
	 * @author zhaohuanhuan
	 * @date 2016年8月4日
	 * @return
	 * @return String
	 */
	public String getAccessToken() {
		SAXBuilder builder = new SAXBuilder();
		String charset = "utf-8";
		String url = ConfigContext.getStringSection("framework.web.academic.https.path");
		String httpUrl = url + ConfigContext.getStringSection("academic.accessToken");
		Map<String, Object> getAccessTockenMap = new HashMap<String, Object>();
		// 应用唯一标识
		getAccessTockenMap.put("appid", ConfigContext.getStringSection("academic.appid"));
		// 应用密钥
		getAccessTockenMap.put("secret", ConfigContext.getStringSection("academic.secret"));
		// 证书类型
		getAccessTockenMap.put("grant_type", ConfigContext.getStringSection("academic.grant_type"));
		builder.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
		String getAccessTokenHttp = doPost(httpUrl, getAccessTockenMap, charset);
		JSONObject json = JSONObject.fromObject(getAccessTokenHttp);
		String accessToken = null;
		try {
			if (!json.isEmpty()) {
				// 得到data数组
				JSONObject data = json.getJSONObject("data");
				accessToken = data.getString("access_token");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return accessToken;
	}

	/**
	 * @Title: getUpdateData
	 * @Description: 获取更新的数据并对其进行操作
	 * @author zhaohuanhuan
	 * @date 2016年8月10日
	 * @return void
	 */
	public void getUpdateData() {
		UpdateDataController updateData = new UpdateDataController();
		String charset = "utf-8";
		String url = ConfigContext.getStringSection("framework.web.academic.https.path");
		String httpUrl = url + ConfigContext.getStringSection("academic.accessToken");
		String accessToken=this.getAccessToken();
		Map<String, Object> getAccessTockenMap = new HashMap<String, Object>();
		// 应用唯一标识
		getAccessTockenMap.put("appid", ConfigContext.getStringSection("academic.appid"));
		// 应用密钥
		getAccessTockenMap.put("secret", ConfigContext.getStringSection("academic.secret"));
		// 证书类型
		getAccessTockenMap.put("grant_type", ConfigContext.getStringSection("academic.grant_type"));
		SAXBuilder builder = new SAXBuilder();
		builder.setFeature(ConfigContext.getStringSection("builderPath"), false);
		String getAccessTokenHttp = doPost(httpUrl, getAccessTockenMap, charset);
		JSONObject json = JSONObject.fromObject(getAccessTokenHttp);
		if (!json.isNullObject()) {
			// 得到data数组
			JSONObject data = json.getJSONObject("data");
			if (data.containsKey("update_data")) {
				List<String> updateDataStrList = new ArrayList<String>();
				// 获取更新数据的类型
				String updateDataStr = data.getString("update_data");
				// 根据逗号分割更新数据类型
				String[] updateDateArray = updateDataStr.split(",");
				for (int i = 0; i < updateDateArray.length; i++) {
					// 将分割的字符串添加给list集合
					updateDataStrList.add(updateDateArray[i]);
				}
				// 判断某字符串是否存在在updateDataStrList集合中
				if (updateDataStrList.contains(ConfigContext.getStringSection("academic.student"))) {
					updateData.getStudentDataUpdate(accessToken);
				}
				if (updateDataStrList.contains(ConfigContext.getStringSection("academic.teacher"))) {
					updateData.getTeacherDataUpdate(accessToken);
				}
				if (updateDataStrList.contains(ConfigContext.getStringSection("academic.unit"))) {
					updateData.getUnitDataUpdate(accessToken);
				}
			}
		}
	}
	
}