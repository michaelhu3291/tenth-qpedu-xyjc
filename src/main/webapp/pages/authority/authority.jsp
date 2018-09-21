<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="<%=request.getContextPath() %>/theme/default/ui.custom.css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/theme/default/ui.jqgrid.css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/theme/default/font.awesome.css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/theme/default/ui.chosen.css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/theme/default/ui.uploadfile.css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/theme/default/ui.pick.css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/theme/default/page.common.css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/theme/default/jquery.multiselect.css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/theme/default/jquery.multiselect.filter.css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/ui.custom.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/ui.jqgrid.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/ui.autosearch.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/ui.chosen.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/ui.uploadfile.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/ui.pick.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/ui.common.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery.validate.js"></script>
<!--<script type="text/javascript" src="../js/jquery.metadata.js"></script>-->
<script type="text/javascript" src="<%=request.getContextPath() %>/js/page.common.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/util.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery.multiselect.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery.multiselect.filter.js"></script>
<!--[if IE 7]>
         <link href="../theme/default/font.awesome.ie7.css" rel="stylesheet" />
         <link href="../theme/default/page.common.ie7.css" rel="stylesheet" />
    <![endif]-->


<style>
.page-editor-panel-look input, .page-editor-panel-look textarea {
	background: #CAF1BD;
}

#more {
	position: absolute;
	width: 140px;
	height: auto;
	z-index: 100;
	background: rgb(230, 240, 246);
	border: 1px solid #D8E5EF;
	display: none;
}

.select td {
	text-align: right;
	width: 100px;
}

#more li {
	list-style-type: none;
	text-align: center;
	height: 30px;
	line-height: 30px;
}
#more ul {
	margin: 0px;
	padding: 0px;
}
#more li:hover {
	background: #0072bc;
	color: #FFF;
	cursor: pointer;
}
</style>
<script type="text/javascript">
	var listId = "#list2", editorFormId = "#editorForm", pagerId = "#pager2", loadUrl = "../authority/authority.do?command=load", deleteUrl = "../authority/authority.do?command=delete", saveUrl = "../authority/authority.do?command=submit", listUrl = "../authority/authority.do?command=search";
	var flag = false;
	var list4Id="#list4";
	var editorFormId1= "#editorForm1";
	var editorFormId2= "#editorForm2";
	var curUserUrl="../authority/authority.do?command=getRoleUser";//用户集合
	var deleteRoles=0;
	loadRoleUser.curUsers=null;//当前用户的ID集合(实时变化)
	function loadRoleUser(param)
	{
		loadRoleUser.curUsers=[];
		
		$.post(curUserUrl,param,function(data){
			for(var i in data)
			{
				loadRoleUser.curUsers.push(data[i].USER_ID);
			}
		});
	}

	$(document).mouseup(function(e) {
		var _con1 = $('#ui-loadMore-role');
		var _con2 = $('#more');
		if (_con1.is(e.target) || _con1.has(e.target).length !== 0){
			return;
		}
			
		if (_con2.is(e.target) || _con2.has(e.target).length !== 0){
			return;
		}
			$("#more").slideUp();
			flag = false;
	});
	
	var cancelBTN = function() {
		hideSlidePanel(".page-editors-panel");
		$(listId).trigger("reloadGrid");
	}

	var editorSave = function() {
		if ($(editorFormId + " [data-validate]").valid()) {
			POST(saveUrl, $(editorFormId).getFormData(), function(data) {
				$(listId).trigger("reloadGrid");
				hideSlidePanel(".page-editor-panel");
				hideSlidePanel(".page-editors-panel");
			});
		}
	}
	
	var insertUser = function() {
		var user=$("#select_user").val();
		if (user == '' || user == null) {
			$(".exam_time_validate").html("必须选择用户!");
			return;
		}
		var url="../authority/authority.do?command=setUser";
			var dataParam=$(editorFormId1).getFormData();
			var roleUserId=[];
			var val = $("#select_user").find("option:selected")
			.each(function() {
				var params={};
				roleUserId.push($(this).val());
			});
			dataParam.roleUserIds=roleUserId;
			dataParam.roleId=roleId;
			POST(url, dataParam, function(data) {
				$(".ui-effects-wrapper").css("display","none");
				hideSlidePanel(".page-editor-insert-panel");
				hideSlidePanel(".page-editor-insert-panel-org");
				$("#list4").trigger("reloadGrid");
			});
	}
	
	var insertOrg = function() {
		var org=$("#select_org").val();
		if (org == '' || org == null) {
			$(".yanzheng").html("必须选择组织!");
			return;
		}
		var url_setOrg="../authority/authority.do?command=setOrg";
			var dataParam=$(editorFormId1).getFormData();
			var roleOrgId=[];
			var val = $("#select_org").find("option:selected")
			.each(function() {
				var params={};
				roleOrgId.push($(this).val());
			});
			dataParam.roleOrgIds=roleOrgId;
			dataParam.roleId=roleId;
			POST(url_setOrg, dataParam, function(data) {
				$(listId).trigger("reloadGrid");
				$(".ui-effects-wrapper").css("display","none");
				hideSlidePanel(".page-editor-insert-panel");
				hideSlidePanel(".page-editor-insert-panel-org");
				$("#list4").trigger("reloadGrid");
			});
		//}
	}
	
	var cancel=function(){
		hideSlidePanel(".page-editor-panel");
		flag=false;
		deleteRoles=0;
	}
	var cancleInsert=function(){
		$(".exam_time_validate").html("");
		$(listId).trigger("reloadGrid");
		$(".ui-effects-wrapper").css("display","none");
		hideSlidePanel(".page-editor-insert-panel");
		hideSlidePanel(".page-editor-insert-panel-org");
		$("#list4").trigger("reloadGrid");
	}
	
	//用户新增
	var insertTree=function(){
		if(deleteRoles==1){
			$("#school option[value != '']").remove();
			$("#select_user option[value != '']").remove();
			$(editorFormId1)[0].reset();
			var $piel = showSlidePanel(".page-editor-insert-panel");
		}else if(deleteRoles==3){
			$("#select_org option[value != '']").remove();
			$(editorFormId2)[0].reset();
			var $piel = showSlidePanel(".page-editor-insert-panel-org");
		}
    };
    
    
  //删除角色用户
	var deleteRoleUser=function(){ 
		
		var idArys = $("#list4").jqGrid("getGridParam", "selarrrow");
	    if (idArys.length === 0) {
	        window.message({
	            text: "请选择要删除的记录!",
	            title: "提示"
	        });
	        return;
	    }
	    if(deleteRoles==1){
	    	deleteRolesUrl="../authority/authority.do?command=deleteRoleUser";
	    }else if(deleteRoles==2){
	    	deleteRolesUrl="../authority/authority.do?command=deleteRolePost";
	    }else if(deleteRoles==3){
	    	deleteRolesUrl="../authority/authority.do?command=deleteRoleOrg";
	    }
	    window.message({
			text : "确认要删除所选择的记录吗?",
			title : "提醒",
			buttons : {
				"确认" : function() {
					window.top.$(this).dialog("close");
					POST(deleteRolesUrl, {
						roleId :roleId,
						userIds : idArys
					}, function(data) {
						$("#list4").trigger("reloadGrid");
						if (window._delete) {
							window._delete();
						}
					});
				},
				"取消" : function() {
					window.top.$(this).dialog("close");
				}
			}
		});
	
	   
	};
	
	
	var setRoleUser = function() {
		var idAry = $(listId).jqGrid("getGridParam", "selarrrow");
		if (idAry.length === 0) {
			window.message({
				text : "请选择要查看的记录!",
				title : "提示"
			});
			return;
		}
		if (idAry.length > 1) {
			window.message({
				text : "每次只能查看单条记录!",
				title : "提示"
			});
			return;
		}
		$("#more").slideUp();
		deleteRoles=1;
		var $piel = $("#myPanel").show({
			effect : "slide",
			direction : "up",
			easing : 'easeInOutExpo',
			duration : 900
		});
		roleId = idAry[0];
		var loadUser = "../authority/authority.do?command=selectUsersByRoleId&roleId="
				+ roleId + "";
		var _colModel, _colNames;

		_colModel = [ {
			name : 'USER_ID',
			key : true,
			width : 60,
			hidden : true
		}, {
			name : 'USER_UID',
			width : 160,
			align : "center"
		}, {
			name : 'USER_NAME',
			width : 300,
			align : "center"
		}, {
			name : 'ORG_NAME',
			autoWidth : true,
			align : "center"
		}, {
			name : 'mainUnit',
			autoWidth : true,
			align : "center",
			sortable : false
		} ], _colNames = [ '选择', '登录名', '用户名称', '主组织机构名称', '主单位' ];
		$("#list4").GridUnload();
		$("#list4").jqGrid($.extend(defaultGridOpts, {
			url : loadUser,
			mtype : "POST",
			colNames : _colNames,
			colModel : _colModel,
			pager : "#pager4"
		}));
		resizeFunFour('654');
	}
	
	
	var setRolePost = function() {
		var idAry = $(listId).jqGrid("getGridParam", "selarrrow");
		if (idAry.length === 0) {
			window.message({
				text : "请选择要查看的记录!",
				title : "提示"
			});
			return;
		}
		if (idAry.length > 1) {
			window.message({
				text : "每次只能查看单条记录!",
				title : "提示"
			});
			return;
		}
		$("#more").slideUp();
		deleteRoles=2;
		var $piel = $("#myPanel").show({
			effect : "slide",
			direction : "up",
			easing : 'easeInOutExpo',
			duration : 900
		});
		roleId = idAry[0];
		var loadPost = "../authority/authority.do?command=selectPostsByRoleId&roleId="
				+ roleId + "";
		var _colModel, _colNames;

		_colModel = [
		 		    {name : 'POST_ID',key : true,width : 60,hidden : true},
		 			{name : 'POST_NAME',width : 160,align:"left"}, 
		 			{name : 'POST_CODE',width : 300,align :"center"},
		 			{name : 'ORG_NAME', autowidth :true,align : "center",sortable:false},
		 			{name : 'mainUnit',autoWidth : true,align:"center",sortable:false}
		 			], 
		 			_colNames = ['选择', '岗位名称', '岗位标识','组织机构名称', '主单位'];
		
		$("#list4").GridUnload();
		$("#list4").jqGrid($.extend(defaultGridOpts, {
			url : loadPost,
			mtype : "POST",
			colNames : _colNames,
			colModel : _colModel,
			pager : "#pager4"
		}));
		resizeFunFour('654');
	}
	
	
	var setRoleOrg = function() {

		var idAry = $(listId).jqGrid("getGridParam", "selarrrow");
		if (idAry.length === 0) {
			window.message({
				text : "请选择要查看的记录!",
				title : "提示"
			});
			return;
		}
		if (idAry.length > 1) {
			window.message({
				text : "每次只能查看单条记录!",
				title : "提示"
			});
			return;
		}
		$("#more").slideUp();
		deleteRoles=3;
		var $piel = $("#myPanel").show({
			effect : "slide",
			direction : "up",
			easing : 'easeInOutExpo',
			duration : 900
		});
		roleId = idAry[0];
		var loadOrg = "../authority/authority.do?command=selectOrgsByRoleId&roleId="
				+ roleId + "";
		var _colModel, _colNames;

		_colModel = [
		 		    {name : 'ORG_ID',key : true,width : 60,hidden : true},
		 			{name : 'ORG_NAME',width : 160,align:"left"}, 
		 			{name : 'ORG_CODE',width : 300, align : "center"},
		 			{name : 'orgParentName', autowidth :true,align : "center", sortable:false},
		 			{name : 'mainUnit',autoWidth : true,align:"center", sortable:false}
		 			], 
		 			_colNames = ['选择', '组织机构名称', '组织机构标识', '父组织机构名称','主单位'];
		
		$("#list4").GridUnload();
		$("#list4").jqGrid($.extend(defaultGridOpts, {
			url : loadOrg,
			mtype : "POST",
			colNames : _colNames,
			colModel : _colModel,
			height:"100%",
			pager : "#pager4"
		}));
		resizeFunFour('654');
		
		
	}

	var returnTab = function() {//返回公告主页
		hideSlidePanel("#lookAnnouncement");
	};

	

	function showPanel(url) {
		var _colModel, _colNames;
		_colModel = [ {
			name : 'USER_ID',
			key : true,
			width : 60,
			hidden : true
		}, {
			name : 'USER_UID',
			autoWidth : true,
			align : "center"
		}, {
			name : 'USER_NAME',
			width : 240,
			align : "center"
		}, {
			name : 'ORG_NAME',
			width : 180,
			align : "center",
			sortable : false
		}, {
			name : 'mainUnit',
			autoWidth : true,
			align : "center",
			sortable : false
		} ], _colNames = [ '选择', '登录名', '用户名称', '主组织机构名称', '主单位' ];
		$(listId).jqGrid($.extend(defaultGridOpts, {
			url : curUserlist,
			mtype : "POST",
			colNames : _colNames,
			colModel : _colModel,
			pager : pagerId
		}));
		resizeFun();

	}

	var more1 = function() {
		flag = !flag;
		if (flag == true) {
			loadMore();
		} else {
			$("#more").slideUp();
		}

		loadMore.show = false;

	};

	function loadMore()//加载更多
	{
		var offset = $("#ui-loadMore-role").offset();
		var x = offset.left - $("#ui-loadMore-role").width() / 2 + "px";
		var y = offset.top + $("#ui-loadMore-role").height() + 5 + "px";
		$("#more").slideDown().css({
			"left" : x,
			"top" : y
		});

	}

	$(function() {
		loadDictionary();
		loadTimeDictionary();
		$("#select_user").multiselect({
			checkAllText : "全选",
			uncheckAllText : "全不选",
			noneSelectedText : "选择用户",
			selectedText : '#' + " 个用户",
			selectedList : 2
		}).multiselectfilter({
			label : "用户名称",
			placeholder : "请输入用户名"
		});
		
		 $("#select_org").multiselect({
			checkAllText : "全选",
			uncheckAllText : "全不选",
			noneSelectedText : "选择用户",
			selectedText : '#' + " 个用户",
			selectedList : 2
		}).multiselectfilter({
			label : "用户名称",
			placeholder : "请输入用户名"
		}); 
		 //加载学校
		$("#schoolType").change(function(){
			$("#school option[value != '']").remove();
			var schoolType = $("#schoolType").val();
			if(schoolType != ""){
				$.ajax({
		   	       	url:"../transfer/teacherTransfer.do?command=selectAllSchools",
		   	        type: "POST",
		   	        data: {schoolType:schoolType},
		   	        dataType: "JSON",
		   	        success: function(data, xhr) {
		   	        	$.each(data,function(index,item){
		   	        		$("#school").append("<option value='"+item.School_Code+"' >"+item.School_Short_Name+"</option>");
		   	        	});
		   	       }
		   	    });  
			}
			
			
		});
		 
		 
		
		$("#school").change(function(){
			$("#select_user option[value != '']").remove();
			var schoolCode = $("#school").val();
			if(schoolCode != ""){
				$.ajax({
		   	       	url:"../authority/authority.do?command=selectAllUsersBySchoolCode",
		   	        type: "POST",
		   	        data: {schoolCode:schoolCode,roleId:roleId},
		   	        dataType: "JSON",
		   	     success : function(data) {
						for (var i = 0; i < data.length; i++) {
							$("#select_user").append("<option value='" + data[i].USER_ID + "'>"
													+ data[i].USER_NAME
													+ "</option>");
						}
						if ($("#select_user").find("option").length > 0) {
							$("#select_user").multiselect('refresh');

						} else {
							$("#select_user").multiselect('refresh');
						}
					}
		   	    });  
			}
			
			
		});
		$("#select_user").change(function(){
			$(".exam_time_validate").html("");
			
		});
		
		$("#select_org").change(function(){
			$(".yanzheng").html("");
			
		});
		 $("#schoolTypeOrg").change(function(){
			$("#select_org option[value != '']").remove();
			var schoolTypeOrg = $("#schoolTypeOrg").val();
			if(schoolTypeOrg != ""){
				$.ajax({
		   	       	url:"../authority/authority.do?command=selectAllOrgsRole",
		   	        type: "POST",
		   	        data: {schoolType:schoolTypeOrg,roleId:roleId},
		   	        dataType: "JSON",
		   	     success : function(data) {
						for (var i = 0; i < data.length; i++) {
								$("#select_org").append("<option value='" + data[i].School_Code + "'>"
										+ data[i].School_Name
										+ "</option>");
						}
						if ($("#select_org").find("option").length > 0) {
							$("#select_org").multiselect('refresh');

						} else {
							$("#select_org").multiselect('refresh');
						}
					}
		   	    });  
			}
			
			
		}); 
		
		_initButtons({
			insertBTN : function(ev) {
				$("#myPanel").slideUp();
				var $i = $(ev.currentTarget).find("i"), $piel = $(
						".page-editors-panel").show({
					effect : "slide",
					direction : "up",
					easing : 'easeInOutExpo',
					duration : 900
				}).find("h4 i").removeClass();
				if ($i.length) {
					$piel.addClass($i.attr("class"));
				}
				window._EDITDATA = undefined;
				var $grid = $(listId), idAry = $grid.jqGrid("getGridParam",
						"selarrrow");
				if (idAry.length === 0) {
					$(editorFormId).resetHasXTypeForm();
				} else {
					var data = $grid.jqGrid("getRowData", idAry[0]);

					$(editorFormId).resetHasXTypeForm({
						"parentDictionary" : [ {
							text : data.roleName,
							value : data.roleId
						} ]
					});
				}
			},
			edit_BTN : function(ev) {
				$("#myPanel").slideUp();
				var $i = $(ev.currentTarget).find("i"), idAry = $(listId)
						.jqGrid("getGridParam", "selarrrow");
				if (idAry.length === 0) {
					window.message({
						text : "请选择要修改的记录!",
						title : "提示"
					});
					return;
				}
				if (idAry.length > 1) {
					window.message({
						text : "每次只能修改单条记录!",
						title : "提示"
					});
					return;
				}

				GET(loadUrl, {
					id : idAry[0],
					dc : (new Date()).getTime()
				}, function(data) {
					data.roleId = idAry[0];
					$("#roleId").val(idAry[0]);
					var $piel = showSlidePanel(".page-editors-panel").find(
							"h4 i").removeClass();
					if ($i.length) {
						$piel.addClass($i.attr("class"));
					}
					if (data.fieldConfig) {
						$(editorFormId).configFormField(data.fieldConfig);
						window._FIELDCONFIG = data.fieldConfig;
						$(editorFormId).resetHasXTypeForm(data.entity);
						window._EDITDATA = data.entity;
					} else {
						$(editorFormId).resetHasXTypeForm(data);
						window._EDITDATA = data;
					}
					if (window._edit) {
						window._edit();
					}
				});
			},

			viewBTN : function(ev) {
				$("#myPanel").slideUp();
				var $i = $(ev.currentTarget).find("i"), idAry = $(listId)
						.jqGrid("getGridParam", "selarrrow");
				if (idAry.length === 0) {
					window.message({
						text : "请选择要查看的记录!",
						title : "提示"
					});
					return;
				}
				if (idAry.length > 1) {
					window.message({
						text : "每次只能查看单条记录!",
						title : "提示"
					});
					return;
				}
				GET(loadUrl, {
					id : idAry[0],
					dc : (new Date()).getTime()
				}, function(data) {
					$("span#role_name").html(data.roleName);
					$("span#role_code").html(data.roleCode);
					if (data.roleType == "B") {
						$("span#role_type").html("业务角色");
					} else if (data.roleType == "S") {
						$("span#role_type").html("系统角色");
					} else if (data.roleType == "P") {
						$("span#role_type").html("私有角色");
					} else {
						return " ";
					}
					$("span#role_description").html(data.roleDescription);
					$("span#create_name").html(data.createUserName);
					$("span#create_time").html(data.createTime);
					$("span#update_name").html(data.updateName);
					$("span#update_time").html(data.updateTime);
					showSlidePanel("#lookAnnouncement");
				});
			},

			deleteBTN : function() {
				$("#myPanel").slideUp();
				var idAry = $(listId).jqGrid("getGridParam", "selarrrow");
				if (idAry.length === 0) {
					window.message({
						text : "请选择要删除的记录!",
						title : "提示"
					});
					return;
				}

				window.message({
					text : "确认要删除所选择的记录吗?",
					title : "提醒",
					buttons : {
						"确认" : function() {
							window.top.$(this).dialog("close");
							POST(deleteUrl, {
								ids : idAry
							}, function(data) {
								$(listId).trigger("reloadGrid");
								if (window._delete) {
									window._delete();
								}
							});
						},
						"取消" : function() {
							window.top.$(this).dialog("close");
						}
					}
				});

			}
		}); //from page.common.js
		_initFormControls(); //from page.common.js
		_initValidateForXTypeForm(editorFormId);

		var _colModel, _colNames;//角色列表
		_colModel = [ {
			name : 'ROLE_ID',
			key : true,
			width : 60,
			hidden : true
		}, {
			name : 'ROLE_NAME',
			width : 160,
			align : "center"
		}, {
			name : 'ROLE_CODE',
			width : 300,
			align : "center"
		}, {
			name : 'ROLE_TYPE',
			autoWidth : true,
			align : "center",
			formatter : function(ar1, ar2, ar3) {
				if (ar3.ROLE_TYPE == "S") {
					return "系统角色";
				}
				if (ar3.ROLE_TYPE == "P") {
					return "私有角色";
				}
				if (ar3.ROLE_TYPE == "B") {
					return "业务角色";
				} else {
					return " ";
				}
			}
		}, {
			name : 'ROLE_DESCRIPTION',
			autoWidth : true,
			align : "center",
			sortable : false
		},

		], _colNames = [ '序号', '角色名称', '角色标识', '角色类型', '角色描述' ];

		$(listId).jqGrid($.extend(defaultGridOpts, {
			url : listUrl,
			colNames : _colNames,
			colModel : _colModel,
			pager : pagerId
		}));
		resizeFun();
	});
</script>
</head>
<body>


	<div id="more">
		<ul>
			<li id="setRoleUser" onclick="setRoleUser()">设置用户</li>
			<!-- <li id="setRolePost" onclick="setRolePost()">设置岗位</li>
			<li id="setRoleOrg" onclick="setRoleOrg()">设置组织</li> -->
		</ul>
	</div>
	<input type="hidden" id="showType" value="list" />

	<div class="page-list-panel">
		<div class="head-panel">
			<div class="toolbar">
				<table style="height: 100%;" cellpadding="0" cellspacing="0"
					border="0">
					<tr>
						<td style="padding-left: 12px; padding-right: 24px;"><i
							class="fa fa-list-ul micon"></i></td>
						<td class="buttons">
							<button id="insertBTN">
								<i class="fa fa-plus"></i>添加
							</button>
							<button id="edit_BTN">
								<i class="fa fa-pencil"></i>修改
							</button>
							<button id="deleteBTN">
								<i class="fa fa-trash-o"></i>删除
							</button>
							<button id="viewBTN">
								<i class="fa fa-paperclip"></i>查看
							</button>
							<button id="ui-loadMore-role" style="margin-left: 10px;"
								class="vis" onclick="more1()">
								<i class="fa fa-plus"></i>更多
							</button>
						</td>
						<td
							style="padding-left: 5px; padding-right: 5px; margin-left: 30px; margin-right: 10px">
							名&nbsp称：</td>
						<td style="padding-left: 10px; padding-right: 5px;"><input
							id="fastQueryText" type="text"
							style="line-height: 1em; font-size: 1em; cursor: text;" placeholder="请输入名称"/></td>
						<td>
							<button id="fastSearch" title="查询" style="margin-left: 0px;">
								<i class="fa fa-search" style="margin-right: 0px;"></i>查询
							</button> <!-- <button id="searchRip" title="点击展开高级查询面板">
								<i class="fa fa-angle-up" style="margin-right: 0px;"></i>
							</button> -->
						</td>
					</tr>
				</table>
			</div>
		</div>
		<table id="list2"></table>
		<div id="pager2"></div>
	</div>
	<div class="page-editors-panel full-drop-panel">
		<div class="title-bar">
		<h4>
			<i class="fa fa-plus"></i>
		</h4>
		<div class="btn-area">
			<div style="margin-top: 4px;">

				<button onclick="editorSave()">
					<i class="fa fa-check"></i>提交
				</button>
				<button onclick="cancelBTN()">
					<i class="fa fa-times"></i>取消
				</button>
			</div>
		</div>
	</div>
	
		<div class="page-content">
			<div class="page-inner-content">
				<form id="editorForm">
					<input id="roleId" type="hidden" name="roleId" />
					<table class="editorTable" cellpadding="0" cellspacing="0" >
						<tr>
							<td class="labelTd"><label for="text_menuName">角色名称：</label>
							</td>
							<td class="inputTd" style="position: relative; z-index: 1;">
								<input data-xtype="text" data-validate="{required:true}"
								name="roleName" id="text_dictionaryName" class="form-control" />
							</td>
							<td class="messageTd"></td>
						</tr>
						<tr>
							<td class="labelTd"><label for="text_menuCode">角色标识：</label>
							</td>
							<td class="inputTd" style="position: relative; z-index: 1;">
								<input data-xtype="text" data-validate="{required:true}"
								name="roleCode" class="form-control" />
							</td>
							<td class="messageTd"></td>
						</tr>
						<tr>
							<td class="labelTd" style="vertical-align: top;"><label
								for="p_remark">角色描述：</label></td>
							<td class="inputTd" style="position: relative; z-index: 1;">
								<textarea name="roleDescription" id="p_remark"
									style="width: 300px;" rows="9"></textarea>
							</td>
							<td class="messageTd"></td>
						</tr>
					</table>
				</form>
			</div>
		</div>
	</div>
	
	


	<div class="full-drop-panel page-content" id="lookAnnouncement"
		style="overflow: auto;">
		<div class="title-bar">
			<h4>
				<i class="fa fa-plus"></i>
			</h4>
			<div class="btn-area">
				<div style="margin-top: 4px;">
					<button type="button" onclick="returnTab();" id="return">
						<i class="fa fa-reply"></i>返回
					</button>
				</div>
			</div>
		</div>
		<div>
			<div class="page-inner-content" style="margin-top: 50px">
				<form id="lookForm">
					<input name="role_Id" type="hidden" name="role_Id" />
					<table cellspacing="0" border="0" class="tableTemplet">
						<thead>
							<tr height="46">
								<th colspan="3"><i class="fa fa-file-text"></i> <span>基本信息</span>
								</th>
							</tr>

						</thead>

						<tbody id="editorArea">
							<tr height="40">
								<td class="label">角色名称：</td>
								<td><span id="role_name"></span></td>
							</tr>
							<tr height="40">
								<td class="label">角色标识：</td>
								<td><span id="role_code"></span></td>
							</tr>

							<tr height="40">
								<td class="label">角色类型：</td>
								<td><span id="role_type"></span></td>
							</tr>
							<tr height="40">
								<td class="label">角色描述：</td>
								<td><span id="role_description"></span></td>
							</tr>
							<tr height="40">
								<td class="label">创建人：</td>
								<td><span id="create_name"></span></td>
							</tr>
							<tr height="40">
								<td class="label">创建时间：</td>
								<td><span id="create_time"></span></td>
							</tr>
							<tr height="40">
								<td class="label">修改人：</td>
								<td><span id="update_name"></span></td>
							</tr>
							<tr height="40">
								<td class="label">最后修改时间：</td>
								<td><span id="update_time"></span></td>
							</tr>
						</tbody>


					</table>

				</form>
			</div>
		</div>
	</div>
	
 <div class="page-editor-panel full-drop-panel" id="myPanel">
	<div class="page-list-panel" >
		<div class="head-panel" >
			<div class="toolbar">
				<table style="height: 100%;" cellpadding="0" cellspacing="0"
					border="0">
					<tr>
						<td style="padding-left: 12px; padding-right: 24px;"><i
							class="fa fa-list-ul micon"></i></td>
						<td class="buttons">
							<button id="addRoleUser" onclick="insertTree();">
								<i class="fa fa-plus"></i>新增
							</button>
							<button id="deleteRoleUser" onclick="deleteRoleUser();">
								<i class="fa fa-trash-o"></i>删除
							</button>
							<button id="cancelBTN3" onclick="cancel();" class="vis">
								<i class="fa fa-paperclip"></i>取消
							</button>
						</td>
					</tr>
				</table>
			</div>
		</div>
		<table id="list4"></table>
		<div id="pager4"></div>
	</div>
</div>


	<div class="page-editor-insert-panel full-drop-panel">
	
	<div class="title-bar">
		<h4>
			<i class="fa fa-plus"></i>
		</h4>
		<div class="btn-area">
			<div style="margin-top: 4px;">

				<button onclick="insertUser()" class="page-button">
					<i class="fa fa-check"></i>提交
				</button>
				<button onclick="cancleInsert()" class="page-button">
					<i class="fa fa-times"></i>取消
				</button>
			</div>
		</div>
	</div>
	
		<div class="page-content">
			<div class="page-inner-content">
				<form id="editorForm1">
					<input id="roleId" type="hidden" name="roleId" />
					<table style="height: 100%; margin-left: 300px" cellpadding="0" cellspacing="0"
					border="0" class="editorTable">
						<tr>
							<td class="labelTd" align="right"><label for="text_menuName">学校类型：</label>
							</td>
							<td>
							<select id="schoolType" name="schoolType" class="form-control" datatime-dic="{code:'xxlx'}" style="width:150px;">
									<option value="">选学校类型</option>
								</select>
							</td>
							
							<td class="messageTd"></td>
						</tr>
						<tr>
							<td class="labelTd"><label for="text_menuCode">学校：</label>
							</td>
							
							<td>
							<select id="school" class="form-control" style="width:280px;">
								<option value="">选择学校</option>
							</select>
							</td>
							<td class="messageTd"></td>
						</tr>
						<tr>
							<td class="labelTd" style="vertical-align: top;"><label
								for="p_remark">用户：</label></td>
							
							<td>
							<div class="multiselect">
							<select id="select_user" class="form-control" multiple="multiple" style="width:280px;">
							</select>
							</div>
							<span class="exam_time_validate" style="color: red;"></span>
							</td>
							<td class="messageTd"></td>
						</tr>
					</table>
				</form>
			</div>
		</div>
	</div>
	
	<div class="page-editor-insert-panel-org full-drop-panel">
		<div class="title-bar">
		<h4>
			<i class="fa fa-plus"></i>
		</h4>
		<div class="btn-area">
			<div style="margin-top: 4px;">

				<button onclick="insertOrg()" class="page-button">
					<i class="fa fa-check"></i>提交
				</button>
				<button onclick="cancleInsert()" class="page-button">
					<i class="fa fa-times"></i>取消
				</button>
			</div>
		</div>
	</div>
	
		<div class="page-content">
			<div class="page-inner-content">
				<form id="editorForm2">
					<input id="roleId" type="hidden" name="roleId" />
					<table style="height: 100%; margin-left: 300px" cellpadding="0" cellspacing="0"
					border="0" class="editorTable">
						<tr>
							<td class="labelTd"><label for="text_menuName">学校类型：</label>
							</td>
							<td>
							<select id="schoolTypeOrg" name="schoolType" class="form-control" data-dic="{code:'xxlx'}" style="width:150px;">
									<option value="">选学校类型</option>
								</select>
							</td>
							
							<td class="messageTd"></td>
						</tr>
							<td class="labelTd" style="vertical-align: top;"><label
								for="p_remark">学校：</label></td>
							
							<td>
							<div class="multiselect">
							<select id="select_org" class="form-control" multiple="multiple" style="width:280px;">
							</select>
							</div>
							<span class="yanzheng" style="color: red;"></span>
							</td>
							<td class="messageTd"></td>
						</tr>
					</table>
				</form>
			</div>
		</div>
	</div>
</body>
</html>