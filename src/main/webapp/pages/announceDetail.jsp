<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"
	import="java.util.*,data.platform.authority.security.SecurityContext"%>
	
<% 
String path = request.getContextPath(); 
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/"; 
String id = request.getParameter("id");//用request得到 
%> 

<!DOCTYPE html>
<html>
<head>
<link rel="shortcut icon" href="theme/default/icon/favicon.ico" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="theme/default/page.common.css" rel="stylesheet" />
<link href="theme/default/ui.custom.css" rel="stylesheet" />
<link href="theme/default/ui.jqgrid.css" rel="stylesheet" />
<link href="theme/default/font.awesome.css" rel="stylesheet" />
<link href="theme/default/ui.pagedmenu.css" rel="stylesheet" />
<link href="theme/default/ui.dropdown.css" rel="stylesheet" />
<link href="theme/default/ui.accordionmenu.css" rel="stylesheet" />
<link href="theme/default/ui.common.css" rel="stylesheet" />
<link href="theme/default/page.index.css" rel="stylesheet" />
<link href="theme/default/ui.pick.css" rel="stylesheet" />
<link href="theme/default/master.css" rel="stylesheet">
<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="js/ui.custom.js"></script>
<script type="text/javascript" src="js/ui.common.js"></script>
<script type="text/javascript" src="js/ui.chosen.js"></script>
<script type="text/javascript" src="js/ui.dropdown.js"></script>
<script type="text/javascript" src="js/ui.nodeline.js"></script>
<script type="text/javascript" src="js/page.common.js"></script>
<script type="text/javascript" src="js/ui.pagedmenu.js"></script>
<script type="text/javascript" src="js/jquery.hashchange.min.js"></script>
<script type="text/javascript" src="js/jquery.easytabs.js"></script>
<script type="text/javascript" src="js/util.js"></script>
<title>学业质量监测</title>
<!--[if IE 7]>
        <link href="theme/default/font.awesome.ie7.css" rel="stylesheet" />
        <style type="text/css">
            .fa-lg {
              line-height: 68px !important;
            }
        </style>
        <![endif]-->
</head>

<style>
.ui-pagedmenu-menu-item {
    padding: 0 0 1px 25px;
    background:url("theme/images/l_h01.png") no-repeat left center;
    height:38px; 
    line-height:38px; 
    color:#fff; 
    font-size:18px;
    cursor: pointer;
}
.ui-li-itemClickDown{
   background:#94c476;
   border-bottom:1px solid #ccdad4; 
   height:54px; 
   line-height:54px; 
   color:#fff; 
   font-size:14px; 
   padding:0 0 0 25px;

}

.nx-left .textSpan {
	padding-left: -15px;
	margin-left: 6px;
}
/* 左边菜单第一层鼠标悬停样式 */
.ui-pagedmenu-menu-item:hover {
	background: #67891B;
	color: #FFF;
}
.yq_popbox01{
	display:none;
}
.yqlist01 ul li span:hover{
	text-decoration:underline;
}
                 
   .zoom-box {
	width: 100%;
	height: 30px;
	position:relative;
	bottom: 0px;
}    
  .zoom-leftPane {
	position:relative;
}   
.l_dou_view
{
    position: absolute;
    left: 178px;
    display: none;
    background: #94c476;
    width: 50px;
    text-align: center;
} 
.l_dou
{
  position: relative;
  cursor: pointer;
}

body{
    overflow: auto;}
.ui-pagedmenu-twolevel {
	min-height: 25px;
	font-size: 14px;
	color: #293038;
	line-height: 25px;
	word-break: break-all;
	cursor: pointer;
	float: left;
	margin-top: 10px;
	padding: 0 15px;
}

.ui-pagedmenu-threelevel {
	min-height: 30px;
	font-size: 12px;
	color: #333;
	line-height: 30px;
	margin-left: auto;
	margin-right: auto;
	margin-bottom: 5px;
	word-break: break-all;
	cursor: pointer;
	border-left: 1px solid #333;
	padding: 5px;
}

.ui-pagedmenu-twolevel:hover {
	color: red;
}

.ui-pagedmenu-menuBox {
	position: absolute;
	max-height: 500px;
	min-height: 40px;
	border: 1px solid #293038;
	border-left: 0;
	left: 189px;
	background: #FFF;
	cursor: pointer;
	z-index: 4;
}

.ui-pagedmenu-ul {
	margin-left: 24px;
}

.ui-pagedmenu-ul li {
	float: left;
	padding: 0 15px;
	height: 12px;
	line-height: 12px;
	max-width: 70px;
	border-left: 1px solid #666;
	margin: 4px 0;
	text-overflow: ellipsis;
	white-space: nowrap;
	list-style: none;
}

.ui-pagedmenu-ul li:hover {
	color: red;
}

.nx-left li {
	list-style: none;
}

.ui-pagedmenu-menuBox li {
	list-style: none;
}

.ui-pagedmenu-row {
	border-bottom: 1px dotted #ccc;
	overflow: hidden;
	min-width: 140px;
	max-width: 400px;
}

.ui-ul-itemClickDown {
	margin: 0;
	padding: 0;
	display: none;
}

.ui-li-itemClickDown {
	list-style: none;
	cursor: pointer;
}

.menu-icon-layout {
	float: left;
	margin-top: 12px;
	margin-left: 15px;
	width: 20px;
}

.menu-icon-hover {
	width: 200px;
	height: 40px;
	color: #FFF;
	position: absolute;
	z-index: 4;
	left: 200px;
	top: 200px;
}

.menu-icon-hover .triangle-left {
	width: 0;
	height: 0;
	border-top: 5px solid transparent;
	border-right: 10px solid #94c476;
	border-bottom: 5px solid transparent;
	float: left;
	margin-top: 15px;
}

.menu-icon-hover .content {
	width: auto;
	height: 40px;
	background: #94c476;
	float: left;
	line-height: 40px;
	text-align: center;
	padding-left: 10px;
	padding-right: 10px;
	border-radius: 2px;
}

.quickMenu {
	float: left;
	width: 100%;
	height: 26px;
	background: #3B5617;
}

.ui-faster-1 {
	height: 0;
	width: 50px;
	border-bottom: 26px solid #67891B;
	border-left: 0px solid transparent;
	border-right: 30px solid transparent;
	line-height: 26px;
	text-align: center;
	color: #FFF;
	font-size: 15px;
	font-weight: bold;
	font-family: "楷体";
	cursor: pointer;
	float: left;
}

.ui-faster-2, .ui-faster-o2 {
	width: 100px;
	line-height: 26px;
	text-align: center;
	color: #FFF;
	font-size: 15px;
	font-weight: bold;
	font-family: "楷体";
	cursor: pointer;
	float: left;
	background: #3B5617;
	color: #fff;
}

.ui-faster-o2::before, .ui-faster-2::before {
	content: "";
	height: 0;
	width: 0px;
	border-top: 26px solid #3B5617;
	border-left: 30px solid transparent;
	border-right: 0px solid transparent;
	position: absolute;
	left: 50px;
}

.ui-faster-o2::before {
	border-top: 26px solid #67891B;
}

.ui-faster-o2 {
	background: #67891B;
}

.ui-faster-3 {
	width: 100px;
	height: 26px;
	float: left;
	background: #3B5617;
	line-height: 26px;
	text-align: center;
	color: #fff;
	font-weight: bold;
	font-family: "楷体";
	font-size: 15px;
}

.ui-faster {
	cursor: pointer;
	font-family: "楷体";
	font-size: 15px;
	font-weight: bold;
}

.checked {
	background: #67891B;
}

.tilt{

  width:80%;
  height:80%;
  border: #cccccc solid 1px;
  margin:50px auto 0 auto;
}

.daohang{ 
 margin-left:30px;  
}



.cnt_tit {
    width: 100%;
    height: 34px;
    border-bottom: 1px solid #156A69;
    font-size: 12px;
    color: #333;
    line-height: 34px;
    background: 24px 8px no-repeat;
    text-indent: 45px;
}

.title1 {
    font-size: 18px;
    color: #1a1a1a;
    font-family: "微软雅黑";
    width: 980px;
    margin: auto;
    line-height: 40px;
    text-align: center;
}

.suoyin {
    margin: 20px 0;
    border-collapse: collapse;
    padding: 0 30px;
}

.neirong {

    width: 90%;
    margin: auto auto;
    overflow:auto;
}

.filesList{
    margin: auto auto;
    overflow:auto;
    
}

.time {
    width: 960px;
    height: 30px;
    line-height: 24px;
    text-align: center;
    margin: auto;
    font-size: 12px;
    color: #888888;
    background:  bottom left repeat-x;
    border-bottom: 1px dotted;
}


.foote {
    width:100%;
    height: 60px;
    line-height: 60px;
    background: #3B5617;
    color: #aedd99;
    position:fixed;
    bottom:0;
    font-size: 12px;
    padding: 0 10px;
    overflow: hidden;
}



</style>
<body style="background: #FFF;">
    <input type="text" id="clientId" style="display: none;">
    <input type="text" id="personId" style="display: none;">
    <input type="text" id="loginAccount" value="${user.loginAccount}" style="display: none;" />
    <div class="menu-icon-hover" style="display:none;">
        <div class="triangle-left"></div>
        <div class="content"></div>
    </div>
    <div class="topnav">
        <div class="nx-header">
            <div class="menubar">
                <span class="l_admin"> 
				    <span class="l_a_name"> 
				      <span id="nxbbbb"> 
						 <label></label>
					  </span>
                </span>
                <label class="l_date" id="logoDate"></label>
                </span>
                <span class="l_sider"> 
				    <img src="theme/images/sider.png">&nbsp;&nbsp;青浦教育&nbsp;>&nbsp;学业质量监测
				</span>
            </div>
        </div>
    </div>
    <div class="header">
        <img src="theme/images/logo.png" style="padding: 13px 37px 0px 0px;cursor: pointer;float: left;"><span style="float: left;font-size: 30px;">学业质量监测</span>
    </div>
    <table id="ct" hidden="hidden" width="90%" height="90%" border="0" cellspacing="0" cellpadding="0" style="border:1px solid #cccccc;margin-top:20px;margin-bottom:80px;" align="center">
        <tbody>
            <tr height="5%">
                <td valign="top">
                    <div class="cnt_tit" id="daohang"></div>
                </td>
            </tr>
            <tr height="5%">
                <td valign="top">
                    <div style="height:10px;"></div>
                    <div class="title1" id="title"></div>
                </td>
            </tr>
            <tr height="5%">
                <td valign="top" width="100%" style="padding 0 30px;">
                    <div class="time" style="line-height:39px;" id="publishDate">
                        <span>来源：学业监测中心管理员</span>
                        <span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
                    </div>
                    
                </td>
            </tr>
            <tr>
                <td valign="top">
                    <div class="neirong" id="context"></div>
                    <div style="height:10px;"></div>
                    <div class="filesList" id="filesList"></div>
                </td>
            </tr>
            <tr>
                <td valign="top">
                    
                </td>
            </tr>
            <tr>
                <td valign="top">
                    <div style="height:15px;"></div>
                </td>
            </tr>
            <tr>
                <td valign="top">
                    <div style="height:15px;"></div>
                </td>
            </tr>
        </tbody>
    </table>
    <div class="footer" hidden="hidden" style="margin-top: 10px;"><span class="s_logo"><img src="theme/images/s_logo.png"><a href="http://bszs.conac.cn/sitename?method=show&amp;id=1ABDF6EB45DA3EDBE053022819AC2885" target="_blank"><img src="theme/images/qpjyj_59.png" width="24" height="30"></a><img src="theme/images/qpjyj_61.png" width="" height="30"></span>版权所有：上海市青浦区教育局 　 技术支持：青浦教育信息中心 　 地址：上海市青浦区公园路301号 　 联系我们</div>
</body>
<script type="text/javascript">
    function getNowFormatDate() {
        var today = new Date();
        var seperator1 = ".";
        var seperator2 = ":";
        var month = today.getMonth() + 1;
        var day = today.getDate();
        var h = today.getHours();
        var m = today.getMinutes();
        var s = checkTime(today.getSeconds());
        month = checkTime(month);
        day = checkTime(day);
        h = checkTime(h);
        m = checkTime(m);
        var currentdate = today.getFullYear() + seperator1 + month + seperator1 + day + "　" + h + seperator2 + m + seperator2 + s;
        t = setTimeout('getNowFormatDate()', 500);
        $("#logoDate").text(currentdate);
    }

    function checkTime(i) {
        if (i < 10) {
            i = "0" + i
        }
        return i
    }

    function backHome() {
        window.location.href = "<%=request.getContextPath()%>/homePage_.do";
    }

    $(function () {

        getNowFormatDate();
        var detailUrl = "announceDetail.do?command=getDetailbyId";
        var id = getUrlParam("id");
        $.ajax({
            url: detailUrl,
            type: "POST",
            data: {
                id: id
            },
            dataType: "JSON",
            success: function (data) {
                var title = data.title; //标题
                var publishDate = data.publishDate; //发布日期
                var context = data.context; //内容
                var type = data.announcementType; //类型
                var d = new Date(publishDate);
                var year = d.getFullYear();
                var month = d.getMonth() + 1;
                if (month >= 10) {
                    month = month;
                } else {
                    month = "0" + month;
                }
                var day = d.getDate();
                if (day >= 10) {
                    day = day;
                } else {
                    day = "0" + day;
                }
                publishDate = year + "-" + month + "-" + day;
                $("#daohang").append("<div style='height:30px;margin-top:10px;margin-right:30px;'>您当前的位置：    <a href='<%=request.getContextPath()%>/homePage_.do' style='margin-right:4px;'>首页</a>" + "&nbsp;>&nbsp;" + type + "</div>");
                $("#title").append("<div style='height:50px;display:block;font-size:large;'>" + title + "</div>");
                $("#publishDate").append("<span style='margin:auto;'>发布日期：" + publishDate + "</span>");
                $("#context").append("<div style='margin-top:40px;'>" + context + "</div>");

                var fileStr = "";
                for (var i = 0; i < data.announcementBasicInfoEntity.files.length; i++) {
                    var fileObj = data.announcementBasicInfoEntity.files[i];
                    fileStr += "<a style='color: blue;margin-left:80px;' class='file-name' data-fid=\"" + fileObj.id + "\" href='<%=request.getContextPath()%>/platform/accessory_.do?command=download&id=" + fileObj.id + " '>附件：" + fileObj.fileName + "</a>";
                }
                $("#filesList").append(fileStr);
                $("#ct").removeAttr("hidden");
                $(".footer").removeAttr("hidden");
            }
        });
    });
</script>
</html>