<%@page import="java.util.Map"%>
<%@page import="com.sun.tools.xjc.reader.xmlschema.bindinfo.BIConversion.User"%>
<%@ page import="java.util.ArrayList" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
    ArrayList<String> listName = (ArrayList<String>)request.getAttribute("courseName");
	ArrayList<String> listVal = (ArrayList<String>)request.getAttribute("courseVal");
	ArrayList<Map<String, Object>> listScore = (ArrayList<Map<String, Object>>)request.getAttribute("pagingResult");
%>
<!DOCTYPE html>
<html>
<head>
<title>初中-管理员-名次查询--打印预览</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="shortcut icon" href="../theme/default/icon/favicon.ico" />
<link href="../theme/default/ui.custom.css" rel="stylesheet" />
<link href="../theme/default/ui.jqgrid.css" rel="stylesheet" />
<link href="../theme/default/font.awesome.css" rel="stylesheet" />
<link href="../theme/default/ui.chosen.css" rel="stylesheet" />
<link href="../theme/default/ui.uploadfile.css" rel="stylesheet" />
<link href="../theme/default/ui.pick.css" rel="stylesheet" />
<!-- <link href="../theme/default/page.common.css" rel="stylesheet" /> -->
<link href="../theme/default/jquery.multiselect.css" rel="stylesheet" />
<link href="../theme/default/jquery.multiselect.filter.css"	rel="stylesheet" />
<script type="text/javascript" src="../js/jquery.js"></script>
<script type="text/javascript" src="../js/ui.custom.js"></script>
<script type="text/javascript" src="../js/ui.jqgrid.js"></script>
<script type="text/javascript" src="../js/ui.autosearch.js"></script>
<script type="text/javascript" src="../js/ui.chosen.js"></script>
<script type="text/javascript" src="../js/ui.uploadfile.js"></script>
<script type="text/javascript" src="../js/ui.pick.js"></script>
<script type="text/javascript" src="../js/ui.common.js"></script>
<script type="text/javascript" src="../js/jquery.validate.js"></script>
<script type="text/javascript" src="../js/page.common.js"></script>
<script type="text/javascript" src="../js/util.js"></script>
<script type="text/javascript" src="../js/jquery.multiselect.js"></script>
<script type="text/javascript" src="../js/jquery.multiselect.filter.js"></script>
<script type="text/javascript" src="../js/jquery.jqprint-0.3.js"></script>
<script type="text/javascript" src="../js/jquery-migrate-1.4.1.js"></script>
<style type="text/css" media=print>
.noprint{display : none }
</style>
<style>
.ui-multiselect-checkboxes {
	border: 1px solid #aaa;
	background: #fff;
	color: #555;
}
.ui-button-text {
	font-weight: bold;
}

.ui-multiselect, span {
	font-weight: normal;
}

.ui-multiselect {
	border: 1px solid #aaa;
}

.ui-multiselect.ui-state-hover {
	border: 1px solid #3B5615;
	background: #ffffff;
	font-weight: bold;
	color: #3B5615;
}

.form-control {
	color: #3B5615;
}

.customStyle {
	background: #FFFFFF;
	border: 1px solid #3babe3;
	color: #3babe3;
	cursor: pointer;
	font-weight: bold;
	padding: .4em 1em;
}

.customStyle:hover {
	color: #FFFFFF;
	background: #3babe3;
}

#tblInfo input {
	background-color: #F6F6F6;
}

#stu_look input {
	width: 100%;
	height: 100%;
	border: 1px;
	text-align: center;
	background-color: white;
}

#stu_look td {
	height: 30px;
}

#tabs-2 {
	height: 847px;
}

.ui-widget-header a {
	color: white;
}

.ui-jqgrid .ui-jqgrid-titlebar {
	font-size: 16px;
}
td{
	text-align: center;
}
</style>
<script type="text/javascript">
function preview(oper) {
    if (oper < 10) {
        bdhtml = window.document.body.innerHTML; //获取当前页的html代码 
        sprnstr = "<!--startprint" + oper + "-->"; //设置打印开始区域 
        eprnstr = "<!--endprint" + oper + "-->"; //设置打印结束区域 
        prnhtml = bdhtml.substring(bdhtml.indexOf(sprnstr) + 18); //从开始代码向后取html 

        prnhtml = prnhtml.substring(0, prnhtml.indexOf(eprnstr)); //从结束代码向前取html 
        window.document.body.innerHTML = prnhtml;
        window.print();
        window.document.body.innerHTML = bdhtml;
    } else {
        window.print();
    }
};
function on_prient(){
    $("#jqprient").jqprint({operaSupport: true,importCSS: true});
}
</script>
</head>
<body style="overflow: auto;">
<div align="right">
	<span class="toolbar" style="display: none;">
			<button title="打印预览" style="margin-right: 30px;" onclick="on_prient();">
			<i class="fa fa-print"></i>打印
			</button>
		</span>
</div>
		<%int a=1; %>
		<%int b=0; %>
<!--startprint1-->
    <div id="jqprient">
    	 <div id="div_signView" class="box">
            <%--<p>签字意见</p>--%>
            <div id="signView" class="box-body table-responsive no-padding">
			<%for(int j=0;j<100;j++){%>
                    <table class="table table-hover table-bordered" border="1" cellspacing="0" style="page-break-after: always; width: 100%;">
                    <caption align="left"><%=request.getAttribute("scoreHtml") %></caption>
                    <tbody>
                        <tr>
                        	<td style="text-align: center;width: 3%;">序号</td>
                            <td style="text-align: center;width: 5%;">考号</td>
                            <td style="text-align: center;width: 10%;">学籍号</td>
                            <td style="text-align: center;width: 6%;">姓名</td>
                            <td style="text-align: center;width: 5%;">学校</td>
                            <td style="text-align: center;width: 6%;">年级</td>
                            <td style="text-align: center;width: 4%;">班级</td>
                             
                            <%
                            for(int i = 0;i<listName.size();i++){
                        	%>
                        	<td style="text-align: center;width: 8%;"><%=listName.get(i)%></td>
                        	<%
                            }                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     
                       		%>
                            <td style="text-align: center;width: 8%;">区级名次</td>
                            <td style="text-align: center;width: 8%;">年级名次</td>
                        </tr>
					<%for(int i=b;i<listScore.size();){%>
						<tr>
						<td  style="text-align: center;"><%=a%></td>
						<td style="text-align: center;"><%=listScore.get(b).get("Exam_Number")%></td>
						<td style="text-align: center;"><%=listScore.get(b).get("XJFH")%></td>
						<td style="text-align: center;"><%=listScore.get(b).get("Name")%></td>
						<td style="text-align: center;"><%=listScore.get(b).get("School_Short_Name")%></td>
						<td style="text-align: center;"><%=request.getAttribute("gradeTxt") %></td>
						<td style="text-align: center;"><%=listScore.get(b).get("Class_Id")%></td>
						
						<%for(int k=0;k<listVal.size();k++){
        	if(listScore.get(b).get(listVal.get(k))!=null){
        		%>	
        		<td style="text-align: center;"><%=listScore.get(b).get(listVal.get(k)) %></td>
        		
        		<% }else{ %>
        	
        <td></td>
        <% } %> 
        
        	
        <% } %> 
                <td style="text-align: center;"><%=listScore.get(b).get("orderDistrict")%></td>
				<td style="text-align: center;"><%=listScore.get(b).get("orderGrade")%></td>
						</tr>
						<%a++;
						  i++;
						  b=i;
						if(a%30-1==0){
							break;
						}
						%>
					<%}%>
                    </tbody>
                </table>
                <%if(b>=listScore.size()){
                	break;
                } %> 
                <%}%>
            </div>
        </div>
    
    </div>
    <!--endprint1-->
</body>
</html>