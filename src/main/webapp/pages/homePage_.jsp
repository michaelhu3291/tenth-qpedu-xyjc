<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"
	import="java.util.*,data.platform.authority.security.SecurityContext"%>
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
<link href="theme/default/mCustomScrollbar.css" rel="stylesheet" />
<link href="theme/default/homePage.css" rel="stylesheet" />
<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="js/ui.custom.js"></script>
<script type="text/javascript" src="js/ui.jqgrid.js"></script>
<script type="text/javascript" src="js/ui.common.js"></script>
<script type="text/javascript" src="js/ui.chosen.js"></script>
<script type="text/javascript" src="js/ui.dropdown.js"></script>
<script type="text/javascript" src="js/ui.nodeline.js"></script>
<script type="text/javascript" src="js/page.common.js"></script>
<script type="text/javascript" src="js/mousewheel.js"></script>
<script type="text/javascript" src="js/mCustomScrollbar.js"></script>
<script type="text/javascript" src="js/ui.pagedmenu.js"></script>
<script type="text/javascript" src="js/page.index.js"></script>
<script type="text/javascript" src="js/jquery-ui.js"></script>
<script type="text/javascript" src="js/jquery.hashchange.min.js"></script>
<!-- <script type="text/javascript" src="js/jquery.easytabs.js"></script> -->
<!-- 加载特别链接 -->
<!-- <script type="text/javascript" src="js/setTBLJ.js"></script> -->
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

<script type="text/javascript">
    var editUserInfo = function () {
        POST("homePage_.do?command=getUserSomeInfo", null, function (data, xhr) {
            for (var item in data) {
                $("#editorForm").find("input[name=" + item + "]").val(data[item]);
            }
            $("#editor").dialog("option", "title", "编辑资料").dialog("open");
        });

    };
	$("#nxbbbb").mouseenter(function(){
		
	});
    var updatePasswd=function(){
   	 var url = "homePage_/updatePasswd.do";
   	  frameDialog(url, "修改口令", {mode:"middle",resizable:false,width:400,height:350,buttons:[
   	    { text:"保存 ", icons:{ primary:"ui-icon-check" },click : function( ev ){
   	    	  var $this   = window.top.$( this ),
   	    	  		dial = $this.find("iframe")[0].contentWindow;
   	    	  var param = dial.getData();
   	    	  if(param)
   	    	 {
   	    		  POST("homePage_/updatePasswd.do?command=updatePasswd", param, function(data, xhr) {
   	    			  $this.dialog( "close" ) ;
   		          });
   	    	 }
   	    }},
   	    { text:"返回", icons:{ primary:"ui-icon-cancel" }, click:function( ev )
         	 	{
   	            var $this = window.top.$( this ) ;
   	            $this.dialog( "close" ) ;
          	}
         	}
   	  ]});                                                                                
   	  
        //$("#editorOther").dialog("option","title","修改口令").dialog("open");
    };
    function getNowFormatDate() {
        var today = new Date();
        var seperator1 = "-";
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
        var currentdate = today.getFullYear() + seperator1 + month + seperator1 + day + "　" + h + seperator2 + m+seperator2+s;
        t = setTimeout('getNowFormatDate()', 1000);
        $("#logoDate").text(currentdate);
    }

    function checkTime(i) {
        if (i < 10) {
            i = "0" + i
        }
        return i
    }

   
   
    $(function () {
    
     
        //控制返回顶部显示与隐藏
        $("#back-top").hide();
            $(window).scroll(function(){   
		          var s = $(this).scrollTop();
		           if (s > 40) {    
		               $("#back-top").fadeIn(500);  
		           }else{  
		               $("#back-top").fadeOut(500);  
		              }   
		   });
         //点击 返回顶部
         $("#back-top").click(function() {  
              $("body,html").animate({scrollTop: 0 }, 300); //点击go to top按钮时，以400的速度回到顶部
              return false;  
           });   
    
    	getNowFormatDate();
    	roleCode="";
    	//根据登录用户角色跳转到不同的页面
    	 $.ajax({
        url: "homePage_.do?command=getUserRoleCode",
        type: "POST",
        data: {},
        dataType: "JSON",
        success: function (data) {
        	var personName="<%=SecurityContext.getPrincipal().getUsername()%>";
        	if (personName=="admin") {
        		$("#contentFrame").attr("src", "pages/dashboard.jsp");
        		return;
			}
        	if(data.data[0]!=null&&data.data[0].roleCode){
        		roleCode = data.data[0].roleCode;
        	}else{
        		$("#contentFrame").attr("src", "403.html");
        		return;
        	}
        	//alert(roleCode);
        	if(roleCode == "teacher"){//学校老师角色页面
        		 $(".ui-faster:not('.ui-faster-1')").remove();
                 $("#contentFrame").attr("src", "pages/statistical_teacher.jsp");
        	} 
        	else if(roleCode == "classRoomTeacher"){
        		$(".ui-faster:not('.ui-faster-1')").remove();
                $("#contentFrame").attr("src", "pages/statistical_classroomTeacher.jsp");
        	}
        	else if(roleCode=="subjectInstructor"){//学科教研员
        		$(".ui-faster:not('.ui-faster-1')").remove();
        		 $("#contentFrame").attr("src", "pages/statistical_districtSubjectInstructor.jsp");
        	}
        	else if(roleCode=="instructor"){// 教研员
        		$(".ui-faster:not('.ui-faster-1')").remove();
        		 $("#contentFrame").attr("src", "pages/statistical_districtInstructor.jsp");
        	}
        	else if(roleCode=="qpAdmin" ||  roleCode=="quAdmin"){// 青浦超级管理员
        		$(".ui-faster:not('.ui-faster-1')").remove();
        		 $("#contentFrame").attr("src", "pages/statistical_qpAdmin.jsp");
        	}
        	else if(roleCode == "schoolPlainAdmin" || roleCode == "schoolAdmin"){//学校教导处
        		$(".ui-faster:not('.ui-faster-1')").remove();
                $("#contentFrame").attr("src", "pages/statistical_teachingOffice.jsp");
        	}
        	else {
        		 $("#contentFrame").attr("src", "pages/dashboard.jsp");
        	}
        }
    });
    	
        /*if (personName == "lida") {
            $(".ui-faster:not('.ui-faster-1')").remove();
            $("#contentFrame").attr("src", "pages/stuDashboard.jsp");
        } else {
            $("#contentFrame").attr("src", "pages/statistical.jsp");
        }*/
        $(".l_title022").mCustomScrollbar();


        var $n = $(".ui-faster-1");
        $(".ui-faster").click(function () {
            $n = replaceBak($(this));
        });

        $(".ui-faster").hover(function () {
            replaceBak($(this));
        }, function () {
            replaceBak($n);
        });

        //允许使用推送技术  
        /* dwr.engine.setActiveReverseAjax(true); */

        //$(".link_button").css("padding-left","630px").css("margin-top","30px");

//         $("#tab-container").easytabs();
    });


    function replaceBak(_this) {
        $(".ui-faster-1").css("border-bottom", "26px solid #3B5617");
        $(".ui-faster").not(".ui-faster-1,.ui-faster-o2,.ui-faster-2").css(
            "background", "#3B5617");
        $(".ui-faster-o2").removeClass("ui-faster-o2").addClass("ui-faster-2");

        if ($(_this).hasClass("ui-faster-2")) {
            $(_this).removeClass("ui-faster-2");
            $(_this).addClass("ui-faster-o2");
        } else if ($(_this).hasClass("ui-faster-1")) {
            $(_this).css("border-bottom", "26px solid #67891B");
        } else {
            $(_this).css("background", "#67891B");
        }
        return _this;
    }

    function gotoMeterial(msg) {
        $("#personId").val(msg.personId);
        //var addUrl="pages/common/push.jsp";
        var account = $("#loginAccount").val();
        if (account == msg.account) {
            $("#contentFrame").attr("src", "pages/newStudent/newStuAudit.jsp");
        }
    }
    var cid = "",
        stuNum = ""; //APP client id
    //供给后台推送的时候 调用     
    function show(msg) {
        $("#clientId").val(msg.cid);
        $("#personId").val(msg.personId);
        //var addUrl="pages/common/push.jsp";
        var account = $("#loginAccount").val();
        if (account == msg.account) {
            $("#contentFrame").attr("src", "pages/newStudent/newStuReport.jsp");
        }
    }

    function getCrossDomainData() {
        var url = "homePage_.do?command=getCrossDomainData";
        $.post(url, {}, function () {});
    }

    /* function backHome() {
		$(".checked").removeClass("checked");
	} */
    //iframe 自适应子页面的高度和宽度  
    function iFrameHeight() {
        var ifm = document.getElementById("contentFrame");
        var subWeb = document.frames ? document.frames["contentFrame"].document : ifm.contentDocument; //document.frames  浏览器兼容
        if (ifm != null && subWeb != null) {
            ifm.height = subWeb.body.scrollHeight;
            ifm.width = subWeb.body.scrollWidth;
        }
    }

    function show() {
        $(".l_dou_view").show();
    }

    function hide() {
        $(".l_dou_view").hide();
    }
    
   	/*选择学校类型关联科目 */
    function schoolTypeChange(){
		var objS = document.getElementById("schoolType1");
		var grade = objS.options[objS.selectedIndex].value;
		$("#course1 option[value != '']").remove();
	    var schoolType1 = $("#schoolType1").val();
	    var url = "platform/dictionary.do?command=getCoursesByCode";
	    var data = {
	        schoolType: schoolType1
	    };
	    if(schoolType1 != ""){
	    	 $.ajax({
	             url: url,
	             type: "POST",
	             data: data,
	             dataType: "JSON",
	             success: function (data) {
	                 for (var i = 0; i < data.length; i++) {
	                     $("#course1").append("<option value='" + data[i].DictionaryCode + "'>" + data[i].DictionaryName + "</option>");
	                 }
	             }
	         });
	    }
	}
</script>
<body >
	<input type="text" id="clientId" style="display: none;">
	<input type="text" id="personId" style="display: none;">
	<input type="text" id="loginAccount" value="${user.loginAccount}"
		style="display: none;" />
	<div class="menu-icon-hover" style="display: none;">
		<div class="triangle-left"></div>
		<div class="content"></div>
	</div>

	<div class="topnav" style="width: 99%;">
		<div class="nx-header">
			<div class="menubar">
				<span class="l_admin"> <span class="l_a_name"> <span
						id="nxbbbb" class="nx-menubutton nx-dropdown nx-menuInfo" > <label></label>
							<div class="nx-dropdown-content" style="top: 30px;left: 800px;z-index: 1000">
								<ul>
									<!-- <li style="display: none id="messageContentUl"><h4>
											您有 <span id="messageCountSpan">0</span> 条未读消息
										</h4></li>
									<li style="display: none onclick="showMessage();"><a
										actiontype="message" tabindex="-1" href="javascript:;"
										class="main-link"><i class="fa fa-th-list"></i>查看全部</a></li>
									<li style="display: none"><h4>基本资料</h4></li>
									<li style="display: none"><a onclick="editUserInfo();" class="main-link"> <i
											class="fa fa-edit"></i>编辑资料
									</a></li> -->
									<li><a onclick="updatePasswd();" class="main-link"><i
											class="fa fa-edit"></i>修改口令 </a></li>

									<!-- <li style="display: none"><a onclick="editFasterMenu();" class="main-link">
											<i class="fa fa-edit"></i>编辑快捷键
									</a></li>
									<li style="display: none"><a onclick="editFasterMenu();" class="main-link">
											<i class="fa fa-edit"></i>编辑网页
									</a></li> -->
								</ul>
							</div>
					</span>
				</span>
					<div class="quit" id="logoutbtn">安全退出</div> <label class="l_date"
					id="logoDate"></label>
				</span> <span class="l_sider"> <a href="#"
					style="display: block; float: left; width: 70px; z-index: 99999;">
						<img src="theme/images/sider.png"> <!--悬浮框--> <!-- <div class="l_pop">
							<ul>
								<li class="l_p_title"><span>青浦教育管理</span><span>青浦教育平台</span><span>青浦教育业务</span><span
									class="last">青浦教育课程</span></li>
								<li class="l_p_list"><span>教育课程</span><span>教育课程</span><span>教育课程</span><span
									class="last">教育课程</span></li>
								<li class="l_p_list"><span>教师博客</span><span>教师博客</span><span>教师博客</span><span
									class="last">教师博客</span></li>
								<li class="l_p_list"><span>教育图片</span><span>教育图片</span><span>教育图片</span><span
									class="last">教育图片</span></li>
								<li class="l_p_list"><span>教育资源</span><span>教育资源</span><span>教育资源</span><span
									class="last">教育资源</span></li>
							</ul>
						 </div>  --> 青浦教育
				</a>>学业质量监测
				</span>
			</div>
		</div>
	</div>
	<div class="header" style="margin-top: 28px; width: 100%">
		<img onclick="backHome();" src="theme/images/logo.png"
			style="padding: 13px 37px 0px 0px; cursor: pointer; float: left;"><span
			style="float: left; font-size: 30px;">学业质量监测</span>
	</div>
	<div class="l_cont" style="overflow: hidden; margin-top: 112px;">
		<div class="leftPane commHeight">
			<div class="l_title022">
				<ul class="nx-left" id="leftMenu" data-mcs-theme="minimal-dark"></ul>
			</div>
			<div class="zoom-box">
				<div class="zoom-leftPane">
					<span class="l_dou" onmouseover="show();" onmouseout="hide();">
						<!--  <span class="l_dou"  onmouseover="show();" onmouseout="hide();"> -->
						<img src="theme/images/l_dou.png"> <!--  <img src="theme/images/l_dou_left.png"> -->
						<span class="l_dou_view">收缩</span>
					</span>
				</div>
			</div>

		</div>
		<div class="l_right commHeight">
			<iframe id="contentFrame" name="contentFrame"
				style="width: 100%; height: 100%; border: none; outline: hidden;"
				frameborder="0" marginwidth="0" marginheight="0"
				src="pages/temp.jsp" onload="iFrameHeight()"></iframe>
		</div>
	</div>
	<div class="scrolling" id="back-top" style="display:block"><a title="返回顶部" href=""></a></div>
	<div class="yqlink" hidden="hidden">
		<span class="l_title03">特别链接</span>
		<div id="tab-container" class='tab-container'>
			<div class="yqlist01">
				<ul class='etabs'>
					<li class='tab'><a href="#bsxj">本市县级教育网站</a></li>
					<li class='tab'><a href="#bsjy">本市教育网站</a></li>
					<li class='tab'><a href="#gov">政府相关网站</a></li>
					<li class='tab'><a href="#cyjy">常用教育资源类网站</a></li>
					<li class='tab'><a href="#cywz">常用网站</a></li>
				</ul>
			</div>
			<div class="panel-container">
				<div id="bsxj" style="display: none;">
					<ul></ul>
				</div>
				<div id="bsjy" style="display: none;">
					<ul></ul>
				</div>
				<div id="gov" style="display: none;">
					<ul></ul>
				</div>
				<div id="cyjy" style="display: none;">
					<ul></ul>
				</div>
				<div id="cywz" style="display: none;">
					<ul></ul>
				</div>
			</div>
		</div>
	</div>
	<div class="footer" style="margin-top: 10px;">
		<span class="s_logo"><img src="theme/images/s_logo.png"><a
			href="http://bszs.conac.cn/sitename?method=show&amp;id=1ABDF6EB45DA3EDBE053022819AC2885"
			target="_blank"><img src="theme/images/qpjyj_59.png" width="24"
				height="30"></a><img src="theme/images/qpjyj_61.png" width=""
			height="30"></span>版权所有：上海市青浦区教育局 技术支持：青浦教育信息中心 地址：上海市青浦区公园路301号 联系我们
	</div>
</body>
</html>