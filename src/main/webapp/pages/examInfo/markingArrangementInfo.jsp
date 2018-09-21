<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<title>区级考号管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="../theme/default/ui.custom.css" rel="stylesheet" />
<link href="../theme/default/ui.jqgrid.css" rel="stylesheet" />
<link href="../theme/default/font.awesome.css" rel="stylesheet" />
<link href="../theme/default/ui.chosen.css" rel="stylesheet" />
<link href="../theme/default/ui.pick.css" rel="stylesheet" />
<link href="../theme/default/page.common.css" rel="stylesheet" />
<script type="text/javascript" src="../js/jquery.js"></script>
<script type="text/javascript" src="../js/ui.custom.js"></script>
<script type="text/javascript" src="../js/ui.jqgrid.js"></script>
<script type="text/javascript" src="../js/ui.autosearch.js"></script>
<script type="text/javascript" src="../js/jquery.validate.js"></script>
<script type="text/javascript" src="../js/ui.chosen.js"></script>
<script type="text/javascript" src="../js/ui.pick.js"></script>
<script type="text/javascript" src="../js/ui.common.js"></script>
<script type="text/javascript" src="../js/page.common.js"></script>
<script type="text/javascript" src='../js/highcharts.js'></script>
<script type="text/javascript" src='../js/util.js'></script>
<style>


</style>
<script type="text/javascript">
	var listId = "#list2", 
	       editorFormId = "#editorForm", 
	       pagerId = "#pager2",
			//listUrl = "../examInfo/markingArrangement_xjkw.do?command=serachCourseAdminPaging";
          listUrl="../examInfo/markingArrangementInfo.do?command=schoolArrangementPading";
			$(function() {
    	 $(listId).trigger("reloadGrid");
		_initButtons({
 
		}); //from page.common.js
		_initFormControls(); //from page.common.js
		_initValidateForXTypeForm(editorFormId);
		 loadDictionary();
		var _colModel = [
				{
					name : 'Brevity_Code',
					key : true,
					width : 60,
					hidden : true,
					search : false,
					sortable : true,
					sortorder:"desc"
				},
				{
					label : "学校",
					name : 'School_Short_Name',
					sortable : false,
					wdth : 150,
					align : "center",
				},{
					label : "阅卷人",
					name : 'Teacher_Name',
					sortable : false,
					autoWidth : true,
					align : "center",
				} ];
		$(listId).jqGrid($.extend(defaultGridOpts, {
			url : listUrl,
			colModel : _colModel,
			pager : pagerId,
			rowNum: 5,
		    rowList: [ 5, 10],
		    multiselect:false //去掉复选框
		}));
		resizeFun();
});
			//导出
	var exportExcel = function(){
			 var data={}
			 var url = "../examInfo/markingArrangementInfo.do?command=exportArrangementExcel&data="+JSON.stringify(data);
			 var form = $( "#fileReteExportExcel" ) ;
				form.attr( "action", url) ;
				form.get( 0 ).submit() ; 
		}
 
</script>
</head>
<body>
<form id="fileReteExportExcel" method="post"></form>
	<div class="page-list-panel">
		<div class="head-panel"  >
			<div class="toolbar">
				<table style="height: 100%;" cellpadding="0" cellspacing="0"
					border="0">
					<tr>
					<td>
						 <button id="deriveArramgement" onclick="exportExcel()">
					    	<i class="fa fa-paperclip"></i>导出
					        </button>
						</td>
					</tr>
				</table>
			</div>
		</div>
		<table id="list2"></table>
		<div id="pager2"></div>  
	</div>
	