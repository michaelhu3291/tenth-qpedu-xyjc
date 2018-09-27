<%@ page language="java"  
import="java.util.*,
com.sun.org.apache.xerces.internal.impl.dv.util.Base64,
data.framework.utility.EncryptHelper" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="shortcut icon" href="theme/default/icon/favicon.ico" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="theme/default/master.css" rel="stylesheet" type="text/css" >

<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="js/ui.common.js"></script>
<title>欢迎登录系统</title>
<script type="text/javascript" src="js/page.login.js"></script>
<script type="text/javascript">
	
	function enterHomePage() {
		if (event.keyCode == 13) {
			$("#login_form").trigger("submit");
		}
	};
</script>
</head>
<body  class="l_bg"  onkeydown="enterHomePage();">
<%
String password = null;
String loginAccount = null;
String autoLogin = null;
Cookie c[]=request.getCookies();
if(c!=null){
	for(int x=0;x<c.length;x++){
		if(c[x].getName().equals("zz_oa")){
			String cookieValue = c[x].getValue();
			String cookieValueAfterDecode = new String(Base64.decode(cookieValue),"utf-8");
			String cookieValues[] = cookieValueAfterDecode.split(":");
			loginAccount = cookieValues[0];
			password = cookieValues[1];
			autoLogin = cookieValues[2];
			request.setAttribute("loginAccount", loginAccount);
			request.setAttribute("password", password);
			request.setAttribute("autoLogin", autoLogin);
		}
	} 
}
%>
    <input type="hidden" name="loginAccount" id="loginAccount" value="${loginAccount}"/>
    <input type="hidden" name="password" id="password" value="${password}"/>
    <input type="hidden" name="autoLogin" id="autoLogin" value="${autoLogin}"/>
	<div class="login_box">
	<span class="login_name">学业质量监测</span>
		<form action="#" id="login_form" method="post" autocomplete="off">
			<div class="delu">
				<table border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td class="r01">
						<span class="l_ipt01"><input type="text" value="请输入用户名" class="textinp" placeholder="用户名" id="j_username" name="j_username"></span>
						</td>
					</tr>
					<tr>
						<td class="r01">
						 <span class="l_ipt02"><input type="password"  value="请输入密码" class="textinp" placeholder="口令" id="j_password" name="j_password"></span>
						</td>
					</tr>
				</table>
			</div>
			 <span class="l_btn"><a  onclick='$("#login_form").trigger("submit");'>登录</a></span>
		</form>
	</div>
	<span class="address">上海市青浦区教育局</span>
	
</body>
</html>