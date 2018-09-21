<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title>班级管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
<script type="text/javascript" src="../js/util.js"></script>

<style>


</style>
<script type="text/javascript">
	var listId = "#list2",
		editorFormId = "#editorForm",
		pagerId = '#pager2',
		listUrl = "../schoolManage/classManagement.do?command=searchPaging",
		setAtXjbUrl="../schoolManage/classManagement.do?command=setAtXjb",
		cancelXjbUrl="../schoolManage/classManagement.do?command=cancelXjb";
		$(function () {
	        _initButtons({
	        	set_at_xjb:function(ev){
	                idAry = $(listId).jqGrid("getGridParam", "selarrrow");
	            		if (idAry.length === 0) {
	                		window.message({
	                   	text: "请选择要设置的记录!",
	                       title: "提示"
	               	});
	                	return;
	            		}
	            		if(idAry.length > 0){
	            			 window.message({
	    			 			title:'提醒',
	    			 			text:'确定设为新疆班吗?',
	    			 			buttons:{
	    			 				'确定':function(){
	    			 					window.top.$(this).dialog("close");
	    			 			    	   $.ajax({
	    			 				   	       	url:setAtXjbUrl,
	    			 				   	        type:"POST",
	    			 				   	        data:{"classIdList":idAry},
	    			 				   	        dataType:"JSON",
	    			 				   	        success: function(data, xhr) {
	    			 				   	        	if(data.success=="true"){
	    			 				   	        	window.Msg.alert("设置成功!");
	    			 				   	        	}
	    			 				   	        	$(listId).trigger("reloadGrid");
	    			 				   	       }
	    			 				   	    });  
	    			 				},
	    			 				"取消":function(){
	       							 window.top.$(this).dialog("close");
	       							}
	    			 			}
	    			 		});
	            		}
	      		   	},
	      		   	
	      		  cancel_xjb:function(ev){
		                idAry = $(listId).jqGrid("getGridParam", "selarrrow");
		            		if (idAry.length === 0) {
		                		window.message({
		                   	text: "请选择要取消的记录!",
		                       title: "提示"
		               	});
		                	return;
		            		}
		            		if(idAry.length > 0){
		            			 window.message({
		    			 			title:'提醒',
		    			 			text:'确定取消新疆班吗?',
		    			 			buttons:{
		    			 				'确定':function(){
		    			 					window.top.$(this).dialog("close");
		    			 			    	   $.ajax({
		    			 				   	       	url:cancelXjbUrl,
		    			 				   	        type:"POST",
		    			 				   	        data:{"classIdList":idAry},
		    			 				   	        dataType:"JSON",
		    			 				   	        success: function(data, xhr) {
		    			 				   	        	if(data.success=="true"){
		    			 				   	        	window.Msg.alert("取消成功!");
		    			 				   	        	}
		    			 				   	        $(listId).trigger("reloadGrid");
		    			 				   	       }
		    			 				   	    });  
		    			 				},
		    			 				"取消":function(){
		       							 window.top.$(this).dialog("close");
		       							}
		    			 			}
		    			 		});
		            		}
		      		   	},
	        }); //from page.common.js
	        _initFormControls(); //from page.common.js
	        _initValidateForXTypeForm(editorFormId);
	        var _colModel = [{
	                name: 'Class_Pk',
	                label: '',
	                key: true,
	                hidden: true,
	                search: false
	            }, {
	                name: 'Class_No',
	                label: '班级名称',
	                autoWidth: true,
	                align: "center",
	                search: false,
	                formatter: function (ar1, ar2, ar3) {
	                     return  _types[ar3.Grade_No] + _typesClass[ar3.Class_No]||"";
	                 }
	            }, {
	                name: 'isXjb',
	                label: '是否新疆班',
	                sortable: false,
	                autoWidth: true,
	                align: "center",
	                formatter: function (ar1, ar2, ar3) {
	                    if(ar3.Is_Xjb=="0"){
	                    	return "否";
	                    }else{
	                    	return "<span style='color:red;'>是</span>";
	                    }
	                 }
	              }, {
		            name: 'Grade_No',
		            label: '年级',
		            hidden: true,
		            search: false
	        	} ];
	        $(listId).jqGrid($.extend(defaultGridOpts, {
	            url: listUrl,
	            colModel: _colModel,
	            pager: pagerId,
	        }));
	        resizeFun();
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
							<button id="set_at_xjb">
								<i class="fa fa-plus"></i>设为新疆班
							</button>
						</td>
						<td class="buttons">
							<button id="cancel_xjb">
								<i class="fa fa-plus"></i>取消新疆班
							</button>
						</td>
						<!-- <td style="padding-left: 24px; padding-right: 5px;"><input
							id="fastQueryText" type="text" placeholder=""
							style="line-height: 1em; font-size: 1em; cursor: text;" /></td>
						<td>
							<button id="fastSearch" title="查询" style="margin-left: 0px;">
								<i class="fa fa-search"></i>查询
							</button>
						</td> -->
					</tr>
				</table>
			</div>
		</div>
		<table id="list2"></table>
		<div id="pager2"></div>
	</div>
</body>
</html>