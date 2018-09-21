package data.framework.httpClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpUtil {

	private static String userName;// 用户标识

	private static String token;// 用户凭证

	private static String sysCode;// 系统标识
	
	public static String appId;//系统Id
	
	

	/**
	 * 初始化工具类
	 * 
	 * @param param
	 * @return 用户验证结果
	 */
	public static String initUtil(JSONObject param) {

		HttpUtil.userName = param.getString("userName");
		HttpUtil.sysCode = param.getString("sysCode");
		//HttpUtil.appId = param.getString("appId");

		String jsonToken = getToken(param);

		JSONObject resultJson = JSONObject.fromObject(jsonToken);

		String mark = resultJson.getJSONObject("meta").getString("success");

		// String pwdMsg=resultJson.getJSONObject("meta").getString("message");

		if (mark.equals("false")) {
			return null;
		}
		JSONArray data = resultJson.getJSONArray("data");
		HttpUtil.token = data.getJSONObject(0).getString("token");
		return data.getJSONObject(0).getString("entity");
	}

	/**
	 * 获取用户可用token
	 * 
	 * @param map
	 * @return（用户密码正确返回唯一token,反之返回null）
	 */
	public static String getToken(JSONObject param) {
		String url = CommonInterFace.sysLogin;
		String jsonToken = null;
		try {
			jsonToken = HttpUtil.doPost(url, param);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jsonToken;
	}

	/**
	 * 发送 GET 请求（HTTP），不带输入数据
	 * 
	 * @param url
	 * @return
	 */
	public static String doGet(String url) {
		return doGet(url, new HashMap<String, Object>());
	}

	/**
	 * http://localhost:8080/bua/load/1212 发送 GET 请求（HTTP），不带输入数据
	 * 
	 * @param apiUrl
	 * @param String
	 * @return
	 */
	public static String doGet(String url, String id) {

		String apiUrl = url;
		StringBuffer param = new StringBuffer();
		param.append("/");
		param.append(id);
		apiUrl += param;
		//System.out.println(apiUrl);
		HttpGet httpGet = new HttpGet(apiUrl);

		return sendRequest(httpGet);
	}

	/**
	 * 发送 GET 请求（HTTP），K-V形式
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public static String doGet(String url, Map<String, Object> params) {
		params.put("sysCode", sysCode);// 系统标识
		String apiUrl = url;
		StringBuffer param = new StringBuffer();
		int i = 0;
		for (String key : params.keySet()) {
			if (i == 0)
				param.append("?");
			else
				param.append("&");
			param.append(key).append("=").append(params.get(key));
			i++;
		}
		apiUrl += param;
		//System.out.println(apiUrl);
		HttpGet httpGet = new HttpGet(apiUrl);
		return sendRequest(httpGet);
	}

	/**
	 * 发送 POST 请求（HTTP），不带输入数据
	 * 
	 * @param apiUrl
	 * @return
	 */
	public static String doPost(String apiUrl) {
		return doPostByMap(apiUrl, new HashMap<String, Object>());
	}

	/**
	 * 发送 POST 请求（HTTP），K-V形式
	 * 
	 * @param apiUrl
	 *            API接口URL
	 * @param params
	 *            参数map
	 * @return
	 */
	public static String doPostByMap(String apiUrl, Map<String, Object> params) {
		params.put("sysCode", sysCode);// 系统标识
		HttpPost httpPost = new HttpPost(apiUrl);
		List<NameValuePair> pairList = new ArrayList<>(params.size());
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue().toString());
			pairList.add(pair);
		}
		httpPost.setEntity(new UrlEncodedFormEntity(pairList, Charset.forName("UTF-8")));
		return sendRequest(httpPost);
	}

	/**
	 * http://localhost:8080/bua/post/1212 发送 POST 请求（HTTP），
	 * 
	 * @param apiUrl
	 * @param String
	 * @return
	 */
	public static String doPost(String url, String id) {
		String apiUrl = url;
		StringBuffer param = new StringBuffer();
		param.append("/");
		param.append(id);
		apiUrl += param;
		HttpPost httpPost = new HttpPost(apiUrl);
		return sendRequest(httpPost);
	}

	/**
	 * 发送 POST 请求（HTTP），JSON形式
	 * 
	 * @param apiUrl
	 * @param json
	 *            json对象
	 * @return
	 */
	public static String doPost(String apiUrl, JSONObject json) throws UnsupportedEncodingException{
		// json.put("sysCode", sysCode);//系统标识
		HttpPost httpPost = new HttpPost(apiUrl);
		StringEntity stringEntity = null;
		stringEntity = new StringEntity(json.toString(), "UTF-8");
		stringEntity.setContentEncoding("UTF-8");
		stringEntity.setContentType("application/json");
		httpPost.setEntity(stringEntity);
		return sendRequest(httpPost);
	}

	/**
	 * http://localhost:8080/bua/app/delete 发送 DELETE 请求（HTTP），JSON形式
	 * 
	 * @param apiUrl
	 * @param json
	 * @return
	 */
	public static String doDelete(String apiUrl, JSONObject json) {
		json.put("sysCode", sysCode);// 系统标识

		HttpDeleteWithBody httpDelete = new HttpDeleteWithBody(apiUrl);

		try {
			StringEntity stringEntity = new StringEntity(json.toString(), "UTF-8");
			stringEntity.setContentEncoding("UTF-8");
			stringEntity.setContentType("application/json");
			httpDelete.setEntity(stringEntity);
		} catch (Exception e) {
			e.printStackTrace();
			return CommonErr.castErr;
		}
		return sendRequest(httpDelete);
	}

	/**
	 * http://localhost:8080/bua/app/delete/1212 发送 DELETE 请求（HTTP），
	 * 
	 * @param apiUrl
	 * @param String
	 * @return
	 */
	public static String doDelete(String url, String id) {

		String apiUrl = url;
		StringBuffer param = new StringBuffer();
		param.append("/");
		param.append(id);
		apiUrl += param;
		HttpDelete httpDelete = new HttpDelete(apiUrl);

		return sendRequest(httpDelete);
	}

	/**
	 * @description 发送Http请求
	 * @param request
	 * @return
	 */
	private static String sendRequest(HttpUriRequest request) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse response = null;
		String result = CommonErr.sysErr;

		request.addHeader("X-Token", token); // 认证token
		request.addHeader("USERNAME", userName); // 认证用户名

		try {
			response = httpClient.execute(request);
			int statusCode = response.getStatusLine().getStatusCode();
			//System.out.println("执行状态码 : " + statusCode);
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				result = EntityUtils.toString(entity, "UTF-8");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (response != null) {
				try {
					EntityUtils.consume(response.getEntity());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

}
