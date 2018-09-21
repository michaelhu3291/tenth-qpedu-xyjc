<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"  import="java.util.*,data.platform.authority.security.SecurityContext"
%>

<!DOCTYPE html>
<html>
    <head>
       <%-- <link rel="shortcut icon" href="theme/default/icon/" />--%>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <!-- <link href="theme/default/ripple-style.css" rel="stylesheet"/> -->
        <link href="theme/default/page.common.css" rel="stylesheet" />
        <link href="theme/default/ui.custom.css" rel="stylesheet" />
        <link href="theme/default/ui.jqgrid.css" rel="stylesheet" />
        <link href="theme/default/font.awesome.css" rel="stylesheet" />
       
        <link href="theme/default/ui.dropdown.css" rel="stylesheet" />
        <link href="theme/default/ui.accordionmenu.css" rel="stylesheet" />
        <link href="theme/default/ui.common.css" rel="stylesheet" />
        <link href="theme/default/page.index.css" rel="stylesheet" />
        <link href="theme/default/ui.pick.css" rel="stylesheet" />
        <link href="theme/default/mCustomScrollbar.css" rel="stylesheet"/>
        <link href="theme/default/calendar-style.css" rel="stylesheet" />
          
        <script type="text/javascript" src="js/jquery.js"></script>
        <script type="text/javascript" src="js/ui.custom.js"></script>
        <script type="text/javascript" src="js/ui.common.js"></script>
        <script type="text/javascript" src="js/ui.chosen.js"></script>
        <script type="text/javascript" src="js/ui.dropdown.js"></script>
        <script type="text/javascript" src="js/ui.nodeline.js"></script>
        <script type="text/javascript" src="js/page.common.js"></script>
		<script type="text/javascript" src="js/lanren.js"></script>
		<script type="text/javascript" src="js/util.js"></script>
        <title>学业质量监测</title>
        
    </head>
    
 <style>
.nx-header{
    height: 50px;
    background-color: transparent; 
    top: 0px;
    left: 0px;
    width: 1000px;
    right: 0px;
    z-index: 5;
    box-shadow:0px 0px 0px #000;
    margin: auto;
     background: url("theme/image/title-bg.png");
     position:static;
}

.next-menus{
	float: left;
    width: 100%;
    background: #fff;
}

.next-menus ul{
	list-style: none;
	float: left;
	margin-left: 10px;
	margin-right: 10px;
	margin-top:5px;
}

.next-menus li{
	height: 20px;
    width: 100px;
    text-align: center;
    padding: 5px;
    list-style: none;
	float: left;
	cursor:pointer;
	font-family: "楷体";
	font-size: 14px;
	font-weight:700;
}
.next-navigation{
    width: 100%;
    float: left;
    background: #F5F5F5;
    color: #1B85BA;
    height:20px;
    padding-top: 8px;
}
.next-navigation li{
	list-style: none;
	float: left;
	font-size: 13px;
	cursor:pointer;
}

.next-navigation ul{
	margin: 0;
    padding: 0;
    margin-left: 12px;
}

.finger-li{
	content:"";
	width:0;   
	height:0;   
	border-left:10px solid transparent;   
	border-right:10px solid transparent;   
	border-top:10px solid #e6b453;
	position: absolute;
    margin-top: 8px;
    margin-left: 40px;
}

.menu-item{
	float: left;
	width:130px;
	height:91px;
	margin:3px;
	color:#fff;
	text-align: center;
	cursor:pointer;
	background-image: url("theme/image/menu-bg.png");
}


.aboluo-td-type{
	border-radius: 15px;
	font-size: 9px;
	width: 14px;
	position: absolute;
	z-index: 10;
	color: white;
	margin-top: -8px;
	margin-left: -7%;
	background: #FF0000;
}

.aboluo-rilidiv .aboluo-rilitable tr td .aboluo-aclick{
	border:0px;
}

.aboluo-rilitable td{
	border: 1px solid #ccc;
}

.aboluo-rightdiv {
    background: #22696D;
    border: 2px solid #22696D;
    width:106px;
}
.aboluo-leftdiv {
    border: 2px solid #22696D;
    width: 70%;
    height: 257px;
}

.aboluo-rilitable{
	height: 90%;
	border-collapse:collapse;
}

.aboluo-rilidiv .aboluo-rilitable tr td a{
	     font-size: 16px;
}

 .aboluo-rilidiv{
 	margin-bottom: 5px;
 	height:85%;
 }
 
 .aboluo-rightdiv .aboluo-currday{
	font-size:30px;
}

.schedule-content ul{
	padding: 0;
	margin: 0;
}

.schedule-content li{
	height:18px;
	font-size: 12px;
	color:#FFF;
	margin: 0;
}

.schedule-content li:hover{
	cursor: pointer;
	color:#FFBB00;
}


.aboluo-xssj span{
	font-size: 14px;
}
    
        
 .aboluo-calendar-month {
    margin-left: 0px; 
    width: 40%;
}

.aboluo-calendar-month select {
    width: 55%;
}

.aboluo-xssj{
	text-align: center;
}

.aboluo-rightdiv .aboluo-xssj span{
	margin-left: 0;
}

.aboluo-tools .aboluo-toToday {
    width: 40px;
    border: 1px solid #AAAAAA;
    margin-left: 15px;
    margin-top: 0px;
    color: #AAAAAA;
    background: #ffffff;
}

.friend-link-cotent a{
	color: #fff;
	font-weight:700;
}

.friend-link-cotent a:hover{
	color: #F5961B;
}

.mes-card{
	float:left;
	color:#fff;
	margin-left: 30px;
	margin-top:20px;
    font-size: 13px;
    width:170px;
}

.mes-card li{
	margin-top: 10px;
	width:155px;
	list-style-type: square;
}

.mes-card ul{
	padding-left:15px;
}

.next-menus ul{
	padding-left:0px;
	margin:0;
}

.friend-link-cotent .aSty::after{
	content:"";
	border-right: 1px solid #fff;
	margin-left: 8px;
}

.menu-item.c{
	width:217px;
}

.menu-item.d{
	width:106px;
}

.menu-item:hover{
	background: #80B8AC;
}

body{
 	background: url('theme/image/backImg.jpg');
}

.next-menus-cls{
	background: #e6b453;
	color:#fff;
}

.nx-header .menubar{
	position:static;
}

.fa.fa-reply:hover{
	    text-shadow: 5px 5px 10px #000;
	    font-size:31px;
}
  </style>
<script type="text/javascript">
//定义弹出窗体（可以覆盖到最大）
window.frameDialog = function(url, title, opts) {
    var $this = undefined;
    if (!url) {
        return;
    }

    var ev_resize = function(event) {
        getWH();
        $this.dialog("option", {
            width : w,
            height : h
        });
    };
    var opt = undefined;
    opt = $.extend({
        mode : 'default',
        resizable : opts.mode !== "full",
        draggable : opts.mode !== "full",
        modal : opts.mode !== "full"
    }, opts), oldopts = $.extend({}, opts), w = 0, h = 0;
    var getWH = function() {
        if (opt.mode === "full") {
            w = $doc.width();
            h = $doc.height();
        } else {
            w = oldopts.width ? oldopts.width : $doc.innerWidth() - 24;
            h = oldopts.height ? oldopts.height : $doc.innerHeight() - 24;
        }
        $.extend(opt, {
            width : w,
            height : h
        });
    };
    getWH();
    $.extend(opt, {
        dialogClass : opt.mode === "full" ? "full-choose-dialog"+(opt.topbar?" top-bar":"") : "choose-dialog"
    });
    if (opt.mode === "full") {
        $(window).resize(ev_resize);
    }
    $this = $(["<div title='", title ? title : "请选择", "'><iframe src='", url, "'></iframe></div>"].join('')).dialog(opt);
    var tf=$this.find("iframe")[0].contentWindow,
        data=opts.data;
    tf.onload=function(){
        if(tf.setValue){
            tf.setValue(data);
        }
        if(tf.initHTML){tf.initHTML();}
    };
    $this.on( "dialogbeforeclose", function( event, ui ) {
        if (opt.mode === "full") {
            $(window).unbind("resize", ev_resize);
        }
        tf.blur();
        tf.document.write("");
        $this.empty();
    } );
    $this.on("dialogclose",function(event,ui){
        $(this).dialog("destroy");
    });
    return $this;
};
//定义弹出框
window.dialog = function(html, opts) {
	var opt = $.extend({
		close : function() {
			$(this).dialog("destroy");
		}
	}, opts);
        return	$(html).dialog(opt);
};
    
    window.Msg={};
//定义消息框
window.Msg.message = function(opts) {
	var set = $.extend({
		text : "",
		//icon : "info",
		width : 355,
		title : "提示"/*,
		 buttonLabel: "确认"*/
	}, opts), opt = $.extend({
		close : function() {
			$(this).dialog("destroy");
		},
		modal : true
	}, set);
	var html = ["<div title='", set.title, "' >", set.icon ? '<span class="ui-icon ui-icon-' + set.icon + '" style="float:left;margin-right:5px;"></span>' : "", set.text, "</div>"];

        return $(html.join("")).dialog(opt);
};
    window.Msg.error=function(text,title,icon){
        window.Msg.message({title:title||'发生错误',text:text||"",icon:icon||"alert"});
    };
    window.Msg.alert=function(text,title,icon,autoclose){
        var dig= window.Msg.message({title:title||'提醒',text:text||"",icon:icon||"info",buttons:[
               {text:"确认",click:function(ev){
                       $(this).dialog("close");
               }}
        ]});
        if(autoclose){
        	//var digObj=dig.data("ui-dialog"),
        	   var $btn=dig.closest(".ui-dialog").find(".ui-dialog-buttonpane button:eq(0)"),
        	       $btnText=$btn.find(".ui-button-text"),
        	       oldText=$btnText.text(),
        	       time=$.isNumeric(autoclose)?autoclose:2,
        	    		   doClose=function(){
        		   if(time<0){
        			   $btn.trigger("click");
        		       clearInterval(ds);
        		   }
        		   $btnText.html(oldText+" (<label style='color:#e0e0e0;'>"+time+"</label>)");
        		   time--;
        	   };
        	   var ds=setInterval(doClose,1000);
        	   doClose();
        }
        return dig;
    };
//定义遮罩
window.frameMask = function(title) {
	$("body").mask(title);
};
//卸载遮罩
window.frameUnMask = function() {
	$("body").unMask();
};




 var updatePasswd=function(){
     $("#editorOther").dialog("option","title","修改口令").dialog("open");
 };
 
 var editUserInfo=function(){
	    POST("main.do?command=getUserSomeInfo",null, function(data, xhr) {
	    for(var item in data){
	        $("#editorForm").find("input[name="+item+"]").val(data[item]);
	      }
	     $("#editor").dialog("option","title","编辑资料").dialog("open");
	    });
	 
	 };
	
	 
		//切换用户
		var switchUser = function(roleId) {
			var url = "main.do?command=switchRole"+(roleId?"&roleId="+roleId:"");
			$("body").mask("正在读取用户信息...");
			$.getJSON(url).done(function(data) {
				var n = data.displayName, icon = data.userIcon, ous = data.otherUsers, $nx = $("#nxbbbb"), $icon = $nx.find(".user-icon"), $otherUsers = $nx.find("ul");
				$icon.attr("src", icon).next().text(n);
				$otherUsers.find("li a[actionType=switchUser]").closest("li").remove();
				for (var i = 0, len = ous.length; i < len; i++) {
					var item = ous[i];
					$(['<li><a actionType="switchUser" uid="', item.userId, '" tabindex="-1" href="javascript:;" class="main-link">', item.displayName, '</a></li>'].join("")).appendTo($otherUsers);
				}
				$(".door-content").pagedmenu("option", {
	    			sourceUrl : "main.do?command=initMenu"
	    		});
				$("body").unMask();
				//
			}).fail(function() {
				$("body").unMask();
				//
				message({
					text : "切换用户失败!",
					title : "提醒"
				});
			});
		};
	        
	        
 (function ($) {
		$.widget("ui.pagedmenu", {
			version: "2.0",
		    contentElement: null,
		    options: {
		        cls: ".menu-item",
		        change: null, //Events
		        sourceUrl: null,
		        selectedItem: undefined,
		        leftPaneWidth:"190px"
		    },
		    _create: function () {
		 	   this.contentElement =$(".menu-item");
		 	   var _this = this;
		 	   
		 	   if (this.options.sourceUrl) {
		            this.loadMenusForUrl(this.options.sourceUrl);
		        }
		    },
		    _initContentConfig: function () {
		    },
		    loadMenusForUrl: function (url) {
		        var _this = this;
		        if (url && url!="") {
		        	_this.options.sourceUrl = url;
		        }
		        _this._clearItems();
		        $.getJSON(this.options.sourceUrl).done(function (data) {
		        	_this._addItems(data.response);
		        }).fail(function () {
		        	
		        });
		    }, 
		    _clearItems: function () {
		        this.contentElement.children().remove();
		    }, 
		    _addItems: function (items) {//添加一级菜单
		        var $cel = this.contentElement, item = undefined,_this=this;
		        for (var i = 0, len = items.length; i < len; i++) {
		            item = items[i];
		            if(item.title.length>10)
			    	{
		            	item.title = item.title.substr(0,10)+"...";
			    	}//一级菜单
		            
			    	var $div = $("<div>").addClass("menu-title").data({"menus":item.menus}).text(item.title);
			    	if(!this.contentElement[i])return alert("门户模块菜单不够！");
			    	
			    	$(this.contentElement[i]).append($("<img>").attr("src","theme/image/"+item.cls).css({"width":"40px","height":"40px","margin-top":"15px"})).append($div).click(function(){
			    		_this._itemClick1($(this));//点击一级菜单
			    	});     
		        }
		        
		    },
		    _setOptions: function (options) {//OVERVIEW SETOPTIONS
		        var that = this,
				  reload = false;
			      $.each(options, function (key, value) {
			          that._setOption(key, value);
			          if (key === "sourceUrl") {
			              reload = true;
			          }
			      });
			      if (reload) {
			          this.loadMenusForUrl(this.options.sourceUrl);
			      }
		  },
		   _itemClick1: function (le) {//点击一级菜单
		   		var $ul = $(".next-menus ul").html(""),
		   		menus = $(le).find(".menu-title").data().menus,
		   		$lis =[],
		   		_this=this;
		   		
		   		$(".next-menu-title h2").html($(le).text());
		   		
		   		for(var k in menus)
		   		{
		   			var obj = menus[k];
		   			var $li = $("<li>").text(obj.title).data(obj).click(function(){
		   				_this._itemClick2($(this));
		   			});
		   			$lis.push($li);
		   		}
		   	
		   		
		   		if($lis[0] && $lis[0].data().url)
		   		{
		   			$("#contextIframe")[0].onload=function(){
			   				$(".friend-link-cotent").hide();
			   				$(".next-pane").slideDown(300,function(){
				   				$(".door-content").hide();
				   			});
			   		};
			   		_this._itemClick2($lis[0]);
		   		}
		   		$ul.append($lis);
		  	},
		  	_itemClick2: function(le){//点击二级菜单
		  		    $(".finger-li").remove();
		  			$(".next-menus-cls").removeClass();
		  			var cre = $("<div>").addClass("finger-li");
		  			
				  	$("#contextIframe").attr("src",$(le).addClass("next-menus-cls").append(cre).data().url);
		    	  	return this;
			  }
		});
		
})(jQuery);
 
 
 function panePosition()
 {
	 var l = $(document.body).width()/2-$(".mainPane").width()/2;
	 var $main = $(".mainPane").css("left",l+"px");
	 
 	  if($(document.body).height()<700)
	  {
		  $main.css("height","100%");
	  }else{
		  $main.css("height","670px");
		  $main.css("top",($(document.body).height()-670)/2);
		  
	  } 
	  
 }
 
 window.onresize=function(){  
	 panePosition();
}  
 
 function quit()//退出按钮事件
 {
	$("#logoutbtn").click(function() {
			$("<div><br/><span>将会销毁当前用户会话,并会放弃所有未保存的操作。</span></div>").dialog({
				show : 'blind',
				hide : 'explode',
				modal : true,
				draggable : false,
				title : "确认要退出吗？",
				buttons : {
					"确认" : function() {
						$(this).dialog("close");
						$.ajax({
		                    url: "j_spring_security_logout",
		                    type: "POST",
		                    dataType: "JSON",
		                    success: function (data, xhr) {
		                    	window.location.replace("login.jsp");
		                    }
		                });   
					},
					"取消" : function() {
						$(this).dialog("close");
					}
				}
			});
		});
 }

 
 
	//供给后台推送的时候 调用     
	function show(msg){  
		$("#clientId").val(msg.cid);
		$("#personId").val(msg.personId);
	    var account = $("#loginAccount").val();
	    if(account==msg.account)
	    {
			$("#contextIframe").attr("src","pages/newStudent/newStuReport.jsp");
			
			$("#contextIframe")[0].onload=function(){
					$(".friend-link-cotent").hide();
					$(".next-pane").slideDown(300,function(){
	   				$(".door-content").hide();
	   			});
			};
	    }
	} 
	
	function gotoMeterial(msg){  
		$("#personId").val(msg.personId);
	    var account = $("#loginAccount").val();
	    if(account==msg.account)
	    {
			$("#contextIframe").attr("src","newStudent/newStuAudit.do");
			
			$("#contextIframe")[0].onload=function(){
					$(".friend-link-cotent").hide();
					$(".next-pane").slideDown(300,function(){
	   				$(".door-content").hide();
	   			});
			};
	    }
	} 
	
	
	function hidePane()
	{
		 $(".door-content").show();
		 $('.next-pane').slideUp(300,function(){
			 $(".friend-link-cotent").show();
		 });
	}
	
 $(function(){
	    panePosition();
	    $(".door-content").pagedmenu({
			change : function(a, it){}
		});
		switchUser();//切换用户
		quit();//退出按钮事件注册
		
		//初始DropDown控件并绑定事件
		$(".menubar .nx-dropdown").dropdown({
		        width:231,
			menuclick : function(ev, sender) {
				var $sender = $(sender);
				switch ($sender.attr("actionType")) {
					case "switchUser":
						var uid = $sender.attr("uid");
						switchUser(uid);
						break;
					case "message":
						break;
					case "editInfo":
						break;
					case "setting":
						break;
					case "changePassword":
						break;
				}
			}
		});
		loadAllAnnouncements();
		loadPendingItems();
		loadSystemMessage();
});

 /*查看当前公告详细信息*/
 var viewAnnouncementItem = function(id)
 {
	  var url="announcementView.do?id="+id;
	   frameDialog(url, "通知公告", {mode:"middle",width:1000,height:580,buttons:[
			        { text:"返回", icons:{ primary:"ui-icon-cancel" }, click:function( ev )
			        {
			            var $this = window.top.$( this ) ;
			            $this.dialog( "close" ) ;
			        }}
			     ]});  
 };
//加载待办任务
 var loadPendingItems=function(){
	 var url="main.do?command=loadPendingItems";
     $.post(url,null, function(data){
    	    var htm="";
			for(var i in data)
			{
				var obj=data[i];
				htm+= "<li><a onclick=viewPendingItems(\""+obj.id+"\");>"+obj.TITLE+"</a></li>";
			}
			$("#woderengwu").empty();
			$("#woderengwu").append(htm);
     },"json");
 };
  
//加载系统消息
 var loadSystemMessage=function(){
	 var url="main.do?command=loadSystemMessage";
     $.post(url,null, function(data){
    	    var htm="";
			for(var i in data)
			{
				var obj=data[i];
				htm+= "<li><a onclick=viewSystemMessage();>"+obj.TEXT+"</a></li>";
			}
			$("#xitonxiaoxi").empty();
			$("#xitonxiaoxi").append(htm);
     },"json");
 };

 
//加载已发布的通知公告
 var loadAllAnnouncements=function(){
	 var loadPublishedAnnouncementsUrl="main.do?command=loadAllPublishedAnnouncements";
   POST(loadPublishedAnnouncementsUrl,null, function(data){
        var $ulObj=$("#tzgg");
        var $announcementObj=$("#tzgg");
        $("#announcementNums").text(data.rows.length);
        for(var i=0;i<data.rows.length;i++){
          if(i>4){
            break;
          }
          
          if(data.rows[i].status!=2 || data.rows[i].status!="2"){
        	  break;
          }
          var obj=data.rows[i];
          var liStr= "<li><a onclick=viewAnnouncementItem(\""+obj.id+"\");>"+obj.title+"</a></li>";
          $ulObj.append(liStr);
        }
    });
 };

</script>
<body style="background-size:100% 100%;overflow-y:auto; " onload="">
	<input type="text" id="loginAccount" value="${user.loginAccount}" style="display:none;"/>
    <input type="text" id="clientId" style="display:none;">
     <input type="text" id="personId" style="display:none;">
	<input id="usesrId" value="${user.id }" type="hidden"/>
	<input id="usesrName" value="${user.chineseName }" type="hidden"/>
    <div class="mainPane" style="width:1000px;position:fixed;min-height:670px;height:100%;overflow-y: hidden;"><!-- background: url('theme/image/xx.jpg');background-size: 100% 100%;box-shadow: 3px 0px 10px #0B5690 inset,-3px 0 10px #0B5690 inset,3px 0 10px #0b5690,-3px 0 10px #0b5690; -->
	    <div class="menu-icon-hover" style="display:none;">
		    <div class="triangle-left"></div>
		    <div class="content"></div>
	    </div>
    
        
           
  <div style="position: absolute;width:1000px;margin:auto;top:0px;z-index:1;bottom:0px;margin-left:0px;">
        
         <div class="next-pane" style="height:100%;z-index:2;top:52px;bottom: 60px;border: 1px;width:100%;display:none;background: #fff;">
	         <div class="next-menu-title" style="height:50px;background:#80B8AC">
	         		<table style="width:100%;">
	         			<tr>
	         					<td style="text-align: left;"><h2 style="margin:0px;margin-top: 10px;margin-left:20px;color:#fff;">发送公务</h2></td>
	         					<td style="text-align: right;"><i class="fa fa-reply" style="margin-right: 20px;cursor:pointer;color:#fff;font-size:29px;margin-top:6px;" onclick="hidePane();"></i></td>
	         			</tr>
	         		</table>
	         </div>
	         
	          <div class="next-menus">
	              <ul>
	             
	              </ul>
	          </div>
	          
	          <!-- <div class="next-navigation">
	              <ul>
	                  <li><i class="fa fa-home"></i><a onclick="hidePane();">主页</a></li>
	                  <li><i class="fa fa-angle-double-right" style="margin-top:3px;"></i></li>
	                  <li><a onclick="currPage();"></a></li>
	              </ul>
	              
	              
	          </div> -->
	          
	         <iframe id="contextIframe" style="width:100%;height:90%;border:0;"></iframe>
         </div>
            
        
	         <div class="door-content" style="height:100%;">
	         	<div class="nx-header">
		           <div class="logo" style="font-size:30px;color:#fff;width: 350px;height:47px;margin-top:1px;"></div>
		            <div class="ui-pagedmenu-panel">
		                <div class="ui-pagedmenu-content"></div>
		            </div>
		            <div class="menubar" style="top:0px;color: #fff;">
		                <div id="nxbbbb" class="nx-menubutton nx-dropdown" style="min-width:136px;margin-left: 411px;">
		                    <img class="user-icon" src="" style="" alt="" />
		                    <label></label>
		                    <!-- <div class="message-mas">0</div> -->
		                    <div class="nx-dropdown-content">
				                    	<ul>
						                            <li id="messageContentUl"><h4>您有 <span id="messageCountSpan">0</span> 条未读消息</h4></li>
						                            <li onclick="showMessage();"><a actiontype="message" tabindex="-1" href="javascript:;" class="main-link"><i class="fa fa-th-list"></i>查看全部</a></li>
						                            <li><h4>基本资料</h4></li>
						                            <li><a onclick="editUserInfo();" class="main-link">
						                               <i class="fa fa-edit"></i>编辑资料</a>
						                            </li>
						                            <li><a onclick="updatePasswd();" class="main-link"><i
						                                        class="fa fa-edit"></i>修改口令
						                                  </a>
						                            </li>
						                           
						                           <li><a onclick="editFasterMenu();" class="main-link">
						                           				<i class="fa fa-edit"></i>编辑快捷键
						                           		 </a>
						                           	</li>
						                           	 <li><a onclick="editFasterMenu();" class="main-link">
						                           				<i class="fa fa-edit"></i>编辑网页
						                           		 </a>
						                           	</li>
				                        </ul>
		                       </div>
		                </div>
		                <div class="nx-menubutton" id="logoutbtn" title="退出"
		                     style="line-height: 2.7em; color: white; font-size: 20px; vertical-align: middle;">
		                    <i class="fa fa-power-off fa-large" style="font-size:1.33em;"></i>
		                </div>
		            </div>
		        </div>
	           	<div style="background:url('theme/image/heiban.jpg');background-size: 100% 100%;border:0px solid #6D2E71;width: 610px;height:360px;float:left;">
           				<div style="height:230px;" class="mes-box">
           						<div class="mes-card">
           							<span>我的任务</span>
           							<ul id="woderengwu">
           								<!-- <li>李华奖学金申请未通过</li>
           								<li>李华晚归处分告知 </li>
           								<li>李华图书未归还</li>
           								<li>【约翰·霍普金斯大学段荣宁校长一行专程来访】</li> -->
           							</ul>
           						</div>
           						
           						<div class="mes-card">
           							<span id="announcements">通知公告</span>
           							<ul id="tzgg">
           								
           							</ul>
           						</div>
           						
           						<div class="mes-card">
           							<span>系统消息</span>
           							<ul id="xitonxiaoxi">
           								<!-- <li>李华获得全国大学生英语演讲比赛一等奖</li>
           								<li>【文产学院分团委创新形式进行大学生诚信教育】</li> -->
           							</ul>
           						</div>
           					
           					
           					
           					
           				</div>
			</div>
           
           <div style="background: #fff;width: 380px;height:260px;float:right;">
				<div style="height:100%;">
					<div class="aboluo-leftdiv">
						<div class="aboluo-tools">
							<div class="aboluo-calendar-select-year"></div>
							<div class="aboluo-calendar-month">
								<a class="aboluo-month-a-perv" href="javascript:;">&lt; </a>
								<a class="aboluo-month-a-next" href="javascript:;"> &gt;</a>
							</div>
							<input type="button" class="aboluo-toToday" value="今天" />
						</div>
						<div class="aboluo-rilidiv">
								<table class="aboluo-rilitable" cellspacing="0" cellpadding="0" >
									<thead class="aboluo-rilithead">
										<tr>
											<th>一</th>
											<th>二</th>
											<th>三</th>
											<th>四</th>
											<th>五</th>
											<th style="color:red;">六</th>
											<th style="color:red;">日</th>
										</tr>
									</thead>
								</table>
						</div>
					</div>
					<div class="aboluo-rightdiv">
						<p class="aboluo-xssj"></p>
						<p class="aboluo-currday"></p>
						<div class="schedule-content">
							<ul>
								<li>校会</li>
								<li>接待访客</li>
								<li>同学会</li>
							</ul>
						</div>
					</div>
			</div>
		   </div>
			<div style="width: 376px;height:90px;float: right;border: 2px solid #22696D;margin-top: 6px;">
				<img src="theme/image/show.jpg" style="width:100%;height:100%;"></img>
			</div>
			
			<div style="width:100%;height:270px;border: 0px solid red;float: left;margin-top: 3px;">
				   <div class="menu-item c" style="margin-left:0px;"></div>
		           <div class="menu-item d" ></div>
		           <div class="menu-item d" ></div>
		           <div class="menu-item c" ></div>
		           <div class="menu-item d"  ></div>
		           <div class="menu-item d"  ></div>
		           <div class="menu-item d"  style="margin-right:0px;"></div>
		           
		           <div class="menu-item d" style="margin-left:0px;"></div>
		           <div class="menu-item d" ></div>
		           <div class="menu-item c" ></div>
		           <div class="menu-item d"  ></div>
		           <div class="menu-item d"  ></div>
		           <div class="menu-item c" ></div>
		           <div class="menu-item d" style="margin-right:0px;"></div>
			</div>
			
			<div class="friend-link-cotent" style="position: absolute;bottom: 0px;clear:both;width:100%;height:60px;background: url('theme/image/menu-bg.png')">
				<p style="margin-top: 20px;font-size: 13px;width: 650px;margin-left: auto;margin-right: auto;">
					<a class="aSty">SIVA官网</a>
					<a class="aSty">SIVA图书馆</a>
					<a class="aSty">教学管理系统</a>
					<a class="aSty">中华英才网</a>
					<a class="aSty">人才市场报</a>
					<a class="aSty">上海市高校就业指导中心</a>
					<a>研究生人才网</a>
				</p> 
			</div> 
	
        </div>
        
   </div>
</div>
	
    </body>
</html>