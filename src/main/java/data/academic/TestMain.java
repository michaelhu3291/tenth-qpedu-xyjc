package data.academic;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.ModelMap;

import data.academic.util.HttpClientUtil;
import data.framework.support.AbstractBaseController;
import data.framework.support.ConfigContext;

//对接口进行测试  
public class TestMain extends AbstractBaseController{
	// "https://trade.tongbanjie.com/insurance/callback/hk/redeem.htm/httpOrg/create";
	private String url = ConfigContext.getStringSection("framework.web.academic.https.path");
	private String charset = "utf-8";
	private HttpClientUtil httpClientUtil = null;

	public TestMain() {
		httpClientUtil = new HttpClientUtil();
	}

	public void test(String operaterType) {
		String httpOrgCreateTest = url + operaterType;
		Map<String, Object> createMap = new HashMap<String, Object>();
//		createMap.put("appid", ConfigContext.getStringSection("academic.appid"));
//		createMap.put("secret", ConfigContext.getStringSection("academic.secret"));
//		createMap.put("grant_type", ConfigContext.getStringSection("academic.grant_type"));
		
		createMap.put("access_token", "d1774779c59d403fb3b60fb660ba635c");
		createMap.put("number", "5");
		String httpOrgCreateTestRtn = httpClientUtil.doPost(httpOrgCreateTest,
				createMap, charset);
		//System.out.println("result:" + httpOrgCreateTestRtn);
//		getSerializer().parseMap("123:123");
//		Map<String, Object> requestMap = getSerializer().parseMap("");
//		System.out.println(requestMap);
		
	}

//	public static void main(String[] args) {
//		TestMain main = new TestMain();
//		//获取accessToken
////		main.test(ConfigContext.getStringSection("academic.accessToken"));
//		main.test("getTeacherData");//获取老师的数据
//	}
	
	public String  httpResult(String url ,String operaterType){
		String httpOrgCreateTest = url + operaterType;
		Map<String, Object> createMap = new HashMap<String, Object>();
		createMap.put("access_token", "d1774779c59d403fb3b60fb660ba635c");
		createMap.put("number", "5");
		return httpClientUtil.doPost(httpOrgCreateTest,
				createMap, charset);
	}

	@Override
	protected void init(ModelMap model, HttpServletRequest request) {
		// TODO Auto-generated method stub
		
	}
}