<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title>打印预览</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
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
function on_prient(){
    $("#jqprient").jqprint({operaSupport: true});
}
</script>
</head>
<body style="overflow: auto;">
		<span class="toolbar" style="display: none;">
			<button title="打印预览" style="margin-left: 0px;" onclick="on_prient();">
			<i class="fa fa-search"></i>打印预览
			</button>
		</span>
		<%int a=1; %>
	<!--startprint1-->
    <div id="jqprient">
    	 <div id="div_signView" class="box">
            <%--<p>签字意见</p>--%>
            <div id="signView" class="box-body table-responsive no-padding">
                <table class="table table-hover table-bordered">
                    <colgroup>
                    	<col width="3%">
                        <col width="13%">
                        <col width="11%">
                        <col width="7%">
                        <col width="9%">
                        <col width="3%">
                        <col width="3%">
                        <col width="3%">
                        <col width="3%">
                        <col width="3%">
                        <col width="3%">
                        <col width="3%">
                        <col width="3%">
                        <col width="3%">
                        <col width="3%">
                        <col width="3%">
                        <col width="3%">
                        <col width="3%">
                        <col width="3%">
                        <col width="3%">
                        <col width="3%">
                        <col width="3%">
                        <col width="3%">
                        <col width="3%">
                    </colgroup>
                    <tbody>
                        <tr>
                        	<td>序号</td>
                            <td>考号</td>
                            <td>学籍号</td>
                            <td>姓名</td>
                            <td>学校</td>
                            <td>年级</td>
                            <td>班级</td>
                            <td>区级名次</td>
                            <td>班级名次</td>                           
                            <td>语文 </td> 
                            <td>数学</td>
                            <td>物理</td>
                            <td>化学</td>
                            <td>思想政治</td>
                            <td>历史</td>
                            <td>地理</td>
                            <td>信息科技</td>
                            <td>美术</td>
                            <td>生物</td>
                            <td>体育</td>
                            <td>音乐</td>
                            <td>牛津英语</td>
                            <td>研究型课程</td>
                            <td>拓展型课程</td>
                            <td>新世纪英语</td>
                            <td>总分</td>
                        </tr>
                     <c:forEach items="${pagingResult}" var="user">
		<tr>
		<td><%=a%></td>
        <td>${user.XJFH}</td>
        <td>${user.Exam_Number}</td>
        <td>${user.Name}</td>
        <td>${user.School_Name}</td>
        <td>${user.Grade_Id}</td>
        <td>${user.Class_Id}</td>
        <td>${user.orderDistrict}</td>
        <td>${user.orderClass}</td>
        <td>${user.yw}</td>
        <td>${user.sx}</td>
        <td>${user.wl}</td>
        <td>${user.hx}</td>
        <td>${user.sxzz}</td>
        <td>${user.ls}</td>
        <td>${user.dl}</td>
        <td>${user.xxkj}</td>
        <td>${user.ms}</td>
        <td>${user.sw}</td>
        <td>${user.ty}</td>
        <td>${user.yyue}</td>
        <td>${user.njyy}</td>
        <td>${user.yjxkc}</td>
        <td>${user.tzxkc}</td>
        <td>${user.xsjyy}</td>
        <td>${user.Total_Score}</td>
        </tr>
         <%a++; %>
</c:forEach>
                    </tbody>
                </table>

            </div>
        </div>
    <!--startprint2-->
    </div>
</body>
</html>