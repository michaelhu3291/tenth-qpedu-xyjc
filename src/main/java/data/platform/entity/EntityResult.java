package data.platform.entity;

import java.util.List;

/*
 * 返回参数
 */
public class EntityResult {

	private String tokenCode; //token
	private List<String> list;//实体集合
	private String userId;
	
	public String getTokenCode() {
		return tokenCode;
	}
	public void setTokenCode(String tokenCode) {
		this.tokenCode = tokenCode;
	}
	
	public List<String> getList() {
		return list;
	}
	public void setList(List<String> list) {
		this.list = list;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	
	
	
	
}
