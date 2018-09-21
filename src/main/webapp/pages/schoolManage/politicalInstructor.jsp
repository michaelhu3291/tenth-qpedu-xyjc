<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="../theme/default/ui.custom.css" rel="stylesheet" />
<link href="../theme/default/ui.jqgrid.css" rel="stylesheet" />
<link href="../theme/default/font.awesome.css" rel="stylesheet" />
<link href="../theme/default/ui.chosen.css" rel="stylesheet" />
<link href="../theme/default/ui.uploadfile.css" rel="stylesheet" />
<link href="../theme/default/ui.pick.css" rel="stylesheet" />
<link href="../theme/default/page.common.css" rel="stylesheet" />
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
<script type="text/javascript" src="../js/lab2.js"></script>

<!--[if IE 7]>
         <link href="../theme/default/font.awesome.ie7.css" rel="stylesheet" />
         <link href="../theme/default/page.common.ie7.css" rel="stylesheet" />
    <![endif]-->
<style type="text/css">

.s1 {
	width: 120px;
    background-color: #e7fcd9;
    border: 0px;
    color: #3b5617;
    cursor: pointer;
}
.s1:HOVER {
    background-color: #3b5617;
    color: #ffffff;
}

#d1 {
	width: 810px;
	height: 400px;
	background-color: #94c476;
	margin: 0 auto;
	margin-top: 60px;
}

#d2 {
	height: 30px;
	font-size: 24px;
	background-color: #3B5617;
	color: white;
	text-align: center;
}

#d3 {
	padding-left: 120px;
}

#tabs ul li a{
	cursor:pointer;
}

#frame1 ul li{
	list-style:none;
	margin-top:20px;
}

#addTeacherInfo{
	margin-top:20px;
}

#addTeacherInfo thead td{
	text-align:center;
	font-weight:bold;
}

#addTeacherInfo tbody td{
	background:#E7FCD9;
	width:150px;
	text-align:center;
}

#addTeacherInfo tbody a:hover{
	background:#3B5617;
	color:#ffffff;
}
</style>
<script type="text/javascript">
	var listId = "#list2", 
	editorFormId = "#editorForm",
	pagerId = '#pager2', 
	loginNames,
	deleteUrl="../schoolManage/politicalInstructor.do?command=delete",
	loadUrl="../schoolManage/politicalInstructor.do?command=loadPoliticalInstructor",
    addUrl="../schoolManage/politicalInstructor.do?command=addPoliticalInstructor",
	listUrl = "../schoolManage/politicalInstructor.do?command=searchPaging",
	verifyCodeUrl="../dataManage/districtSubjectInstructorInfo.do?command=selectUserUidByUserId";
	
	$(function() {
		
		
	     $("#tabs").tabs({
	    	event:"mouseover",
			active:0,
	     });
	     $("#tabs ul li:first").css("background","#E7FCD9");
	     $("#tabs ul li a").mouseover(function(){
	    	 $(this).parent().next().css("background","#9bc609");
	    	 $(this).parent().prev().css("background","#9bc609");
	    	 $(this).parent().css("background","#E7FCD9");
	     });
		_initButtons({
			cancelBTN : function() {
				$("#loginName").val("");
				hideSlidePanel(".page-editor-panel");
			},
			updatePoliticalInstructor : function(ev) {
				
                 $("#caseEditorForm").resetHasXTypeForm();
                 $("#tbl").text("");
                 $("#tbls").text("");
                 $('#name').blur(function () { 
                	 $("#tbls").text("");
                	    var n=$("#name").val();
                	    if(n==""){
                		 $("#tbls").text("姓名不能为空");
                		 return;
                	    }
                 });
                 $('#loginName').blur(function () { 
    		    	 /* 登录名 */
    		    	 $("#tbl").text("");
    		    	  var userId=$("#id").val();
    		    	  var loginName=$("#loginName").val();
    		    	  var data={userId:userId,loginName:loginName}
    		    	  var returnVal=true;
    		 			$.ajax({
    		 	            url: verifyCodeUrl,
    		 	            type: "POST",
    		 	            data:data,
    		 	 	        dataType: "json",
    		 	 	        async:false,
    		 	 	        success : function (data) {
    		 	            	returnVal=data.flag;
    		 	            	if($("#loginName").val()=="" || $("#loginName").val()==null){
    		 	            	$("#tbl").text("用户名不能为空");
    		 	            	}else{
    		 	            		if(returnVal==false){
    		 	            			$("#tbl").text("用户名已存在");
    		 	 	            	}else{
    		 	 	            		$("#tbl").text("");
    		 	 	            	}
    		 	            	}
    		 		        }
    		 	        });
    		 } );
 				var $i = $(ev.currentTarget).find("i"),
 				idAry = $(listId).jqGrid("getGridParam", "selarrrow");
 		        if (idAry.length === 0) {
 		            window.message({
 		                text: "请选择要修改的记录!",
 		                title: "提示"
 		            });
 		            return;
 		        }
 		        if (idAry.length > 1) {
 		            window.message({
 		                text: "每次只能修改单条记录!",
 		                title: "提示"
 		            });
 		            return;
 		        }
 		      GET(loadUrl, {id: idAry[0]}, function (data) {
 		        	var $piel =showSlidePanel(".page-editor-panel");
 		        	for(var i=0;i<data.length;i++){
 		        		   $("#id").val(data[i].USER_ID);
 		        		   $("#loginName").val(data[i].USER_UID);
 		        		  $("#name").val(data[i].USER_NAME);
 		        	showSlidePanel(".page-editor-panel");
 		        	}
 		        	  loginNames="";
		 				loginNames=$("#loginName").val();
 		        }); 
 			},
 			 detelePoliticalInstructor: function () {
 				 var idAry = $(listId).jqGrid("getGridParam", "selarrrow");
 		            if (idAry.length === 0) {
 		                window.message({
 		                    text: "请选择要删除的记录!",
 		                    title: "提示"
 		                });
 		                return;
 		            }
 	            window.message({
 	                text: "确认要删除所选择的记录吗?",
 	                title: "提醒",
 	                buttons: {
 	                    "确认": function () {
 	                        window.top.$(this).dialog("close");
 	                        POST(deleteUrl, {id: idAry}, function (data) {
 	                            $(listId).trigger("reloadGrid");
 	                            if (window._delete) {
 	                                window._delete();
 	                            }
 	                        });
 	                    },
 	                    "取消": function () {
 	                        window.top.$(this).dialog("close");
 	                    }
 	                }
 	            });

 	        },
		});//from page.common.js
		_initFormControls();//from page.common.js
		_initValidateForXTypeForm(editorFormId);
		var _colModel = [ {
			name : 'USER_ID',
			key : true,
			hidden : true,
			search : false
			},{
				name : 'USER_NAME',
				sortable : false,
				autoWidth : true,
				align : "center",
		      },{
			name : 'USER_UID',
			sortable : false,
			autoWidth : true,
			align : "center"
	      },{
				name : 'POSITION_NO',
				sortable : false,
				autoWidth : true,
				align : "center",
		      }], 
		_colNames = [ '', '姓名', '账号','职业'];
		$(listId).jqGrid($.extend(defaultGridOpts, {
			url : listUrl,
			colNames : _colNames,
			colModel : _colModel,
			pager : pagerId
		}));
		resizeFun();

		$('#loginName').focus(function(){
	    	 $("#tbl").text("");
	     });
		
		
	     //添加操作
		  $("#saveBTN").click(function(ev){
			  $("#tbl").text("");
			  var returnVal=true;
			  var userId=$("#id").val();
			  var loginName = $("#loginName").val();
			  var name=$("#name").val();
			  var datas = {userId:userId,loginName:loginName,name:name};
			  
				$.ajax({
	 	            url: verifyCodeUrl,
	 	            type: "POST",
	 	            data:datas,
	 	 	        dataType: "json",
	 	 	        async:false,
	 	 	        success : function (data) {
	 	            	returnVal=data.flag;
	 	            	if($("#loginName").val()=="" || $("#loginName").val()==null){
	 	            	$("#tbl").text("用户名不能为空");
	 	            	}else{
	 	            		if(returnVal==false){
	 	            			$("#tbl").text("用户名已存在");
	 	 	            	}else{
	 	 	            		$("#tbl").text("");
	 	 	            		$("#tbls").text("");
	 	                	    var n=$("#name").val();
	 	                	    if(n==""){
	 	                		 $("#tbls").text("姓名不能为空");
	 	                		 return;
	 	                	    }else{
	 	                	    	if(loginNames!=""){
		 	 	            			 window.message({
		 		 	 	    	 			title:'提醒',
		 		 	 	    	 			text:'确定修改此教导员吗?',
		 		 	 	    	 			buttons:{
		 		 	 	    	 				'确定':function(){
		 		 	 	    	 					window.top.$(this).dialog("close");
		 		 	 	    	 					 $.ajax({
		 		 	 	    	 				   	       	url:addUrl,
		 		 	 	    	 				   	        type:"POST",
		 		 	 	    	 				   	        data:datas,
		 		 	 	    	 				   	        dataType:"JSON",
		 		 	 	    	 				   	        success: function(data, xhr) {
		 		 	 	    	 				   	        	if(data.mess == 'addSuccess'){
		 		 	 	    	 				   	        		window.Msg.alert("添加成功!");
		 		 	 	    	 				   	        	}
		 		 	 	    	 				   	        	if(data.mess=='updateSuccess'){
		 		 	 	    	 				   	        	window.Msg.alert("修改成功!");
		 		 	 	    	 				   	        	}
		 		 	 	    	 				   	        	$(listId).trigger("reloadGrid");
		 		 	 	    	 				   	       		hideSlidePanel(".page-editor-panel");
		 		 	 	    	 				   	       }
		 		 	 	    	 				   	    });  
		 		 	 	    	 				},
		 		 	 	    	 				'取消':function(){
		 		 	 	    	 					window.top.$(this).dialog("close");
		 		 	 	    	 				}
		 		 	 	    	 			}		
		 		 	 	    	 		});
		 	 	            		}else{
		 	 	            			window.message({
		 		 	 	    	 			title:'提醒',
		 		 	 	    	 			text:'确定添加此教导员吗?',
		 		 	 	    	 			buttons:{
		 		 	 	    	 				'确定':function(){
		 		 	 	    	 					window.top.$(this).dialog("close");
		 		 	 	    	 					 $.ajax({
		 		 	 	    	 				   	       	url:addUrl,
		 		 	 	    	 				   	        type:"POST",
		 		 	 	    	 				   	        data:datas,
		 		 	 	    	 				   	        dataType:"JSON",
		 		 	 	    	 				   	        success: function(data, xhr) {
		 		 	 	    	 				   	        	if(data.mess == 'addSuccess'){
		 		 	 	    	 				   	        		window.Msg.alert("添加成功!");
		 		 	 	    	 				   	        	}
		 		 	 	    	 				   	        	if(data.mess=='updateSuccess'){
		 		 	 	    	 				   	        	window.Msg.alert("修改成功!");
		 		 	 	    	 				   	        	}
		 		 	 	    	 				   	        	$(listId).trigger("reloadGrid");
		 		 	 	    	 				   	       		hideSlidePanel(".page-editor-panel");
		 		 	 	    	 				   	       }
		 		 	 	    	 				   	    });  
		 		 	 	    	 				},
		 		 	 	    	 				'取消':function(){
		 		 	 	    	 					window.top.$(this).dialog("close");
		 		 	 	    	 				}
		 		 	 	    	 			}		
		 		 	 	    	 		});
		 	 	            		}
	 	                	    }
	 	 	            		
	 	 	            		
	 	 	            		
	 	 	            		
	 	 	            	 
	 	 	            	}
	 	            	}
	 		        }
	 	        });
			
		 });
	     
	     
	     
	     
	     
	     
	     
	    //添加页面下拉显示
		  $("#addPoliticalInstructor").click(function(ev){
			  loginNames="";
			  loginNames=$("#loginName").val();
			  $("#caseEditorForm").resetHasXTypeForm();
			    $('#name').blur(function () { 
               	 $("#tbls").text("");
               	    var n=$("#name").val();
               	    if(n==""){
               		 $("#tbls").text("姓名不能为空");
               		 return;
               	    }
                });
			  $('#loginName').blur(function () { 
		    	  var loginName=$("#loginName").val();
		    	  var returnVal=true;
		 		    var verifyCodeUrl="../dataManage/districtSubjectInstructorInfo.do?command=getInfoByLoginName";
		 			$.ajax({
		 	            url: verifyCodeUrl,
		 	            type: "POST",
		 	            data: {loginName:$("#loginName").val()},
		 	 	        dataType: "json",
		 	 	        async:false,
		 	 	        success : function (data) {
		 	            	returnVal=data.flag;
		 	            	returnVal=data.flag;
		 	            	if($("#loginName").val()=="" || $("#loginName").val()==null){
		 	            	$("#tbl").text("用户名不能为空");
		 	            	}else{
		 	            		if(returnVal==false){
		 	            			$("#tbl").text("用户名已存在");
		 	 	            	}else{
		 	 	            		$("#tbl").text("");
		 	 	            	}
		 	            	}
		 		        }
		 	        }); 
		 } );
				var $i = $(ev.currentTarget).find("i"),
	            $piel = $(".page-editor-panel").show({
	               effect: "slide",
	               direction: "up",
	               easing: 'easeInOutExpo',
	               duration: 900
	           });
		 });
	});	 
</script>
</head>
<body>
	<div class="page-list-panel">
		<div class="head-panel">
			<div class="toolbar">
				<table style="height: 100%;" cellpadding="0" cellspacing="0"
					border="0">
					<tr>
						<td style="padding-left: 12px; padding-right: 24px;"><i
							class="fa fa-list-ul micon"></i></td>
						<td class="buttons">
							<button id="addPoliticalInstructor">
								<i class="fa fa-plus"></i>添加
							</button>
							<button id="updatePoliticalInstructor">
								<i class="fa fa-pencil"></i>修改
							</button>
							<button id="detelePoliticalInstructor">
								<i class="fa fa-trash-o"></i>删除
							</button>
						</td>
						<td style="padding-left: 24px; padding-right: 5px;"><input
							id="fastQueryText" type="text" placeholder="输入账号"
							style="line-height: 1em; font-size: 1em; cursor: text;" /></td>
						<td>
							<button id="fastSearch" title="查询" style="margin-left: 0px;">
								<i class="fa fa-search"></i>查询
							</button>
						</td>
					</tr>
				</table>
			</div>
		</div>
		<table id="list2"></table>
		<div id="pager2"></div>
	</div>



<!-- 添加教导员 -->
	<div class="page-editor-panel full-drop-panel">
		<div class="title-bar">
			<h4>
				<i class="fa fa-plus"></i>
			</h4>
			<div class="btn-area">
				<div style="margin-top: 4px;">
					<button id="saveBTN">
						<i class="fa fa-check"></i>保存
					</button>
					<button id="cancelBTN" >
						<i class="fa fa-times"></i>取消
					</button>
				</div>
			</div>
		</div>
		<div class="page-content">
			<div class="page-inner-content" style="margin: auto;">
			  	<form id="caseEditorForm">
			  	<input type="hidden" name="id" id="id" />
				    <table cellspacing="0" border="0" style="width:94.7%;" class="tableTemplet">
		                <!-- <thead>
							<tr>
								<th colspan="4"  style="color: black;">
									<i class="fa fa-file-text"></i>
									<span>添加教导员</span>
								</th>
							</tr>
						</thead> -->
						<tbody>
						<tr>
								<td class="label" style="width:15%"><span style="color:red;">*</span>姓名：</td>
								<td style="width:25%">
								<input data-xtype="text" name="name" id="name" />
								<span style="color:red;" id="tbls"></span>
								</td>
							</tr>
							<tr>
								<td class="label" style="width:15%"><span style="color:red;">*</span>登录名：</td>
								<td style="width:25%">
								<input data-xtype="text" name="loginName" id="loginName" />
								<span style="color:red;" id="tbl"></span>
								</td>
							</tr>
		      	 </table>
			</form>
			</div>
		</div>
	</div>


	
	
</body>
</html>