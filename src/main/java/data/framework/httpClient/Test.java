package data.framework.httpClient;


import java.io.UnsupportedEncodingException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Test {

	public static void main(String[] args) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		JSONObject param = new JSONObject();
		param.put("userName", "admin");
		param.put("pwd", "a");
		param.put("sysCode", "academic");
		param.put("sysPasswd", "123456");
		
		String jo=HttpUtil.doPost(CommonInterFace.sysLogin,param);
		
		JSONObject json=JSONObject.fromObject(jo);  
		JSONArray array=JSONArray.fromObject(json.getString("data"));
		JSONObject obj=array.getJSONObject(0);
		String token=obj.getString("token");
		String entity=obj.getString("entity");
		System.out.println("=="+token);
		System.out.println("ddd"+entity);
		System.out.println("=="+json);
		
		
		/*RestResponse rr=JacksonUtil.readValue(jo, RestResponse.class);
		System.out.println("================="+rr);
		*/
		
		
		
		
		
		//System.out.println(HttpUtil.login(param));
		System.out.println("==="+jo);
	}

}
