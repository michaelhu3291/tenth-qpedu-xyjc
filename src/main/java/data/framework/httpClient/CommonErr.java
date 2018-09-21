package data.framework.httpClient;


/**
 * bua常用异常信息
 * @author qcxiao
 * @createTime 2016年3月30日-下午4:51:37
 *
 */
public abstract class CommonErr {
    
	public static String sysErr=info("系统访问异常!");//系统访问异常信息
	
	public static String castErr=info("数据转换异常!");//数据转换异常信息
	
	private static String info(String errInfo) {
		RestResponse errResp=new RestResponse();
		errResp.failure(errInfo);
        return JacksonUtil.toJSon(errResp);
	}
	
}
