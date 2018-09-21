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
<link href="../theme/default/ui.uploadfile.css" rel="stylesheet" />
<link href="../theme/default/ui.pick.css" rel="stylesheet" />
<link href="../theme/default/page.common.css" rel="stylesheet" />
<link href="../theme/default/jquery.multiselect.css" rel="stylesheet" />
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
<script type="text/javascript" src='../js/highcharts.js'></script>
<script type="text/javascript" src='../js/util.js'></script>
<script type="text/javascript" src="../js/jquery.multiselect.js"></script>
<style>


</style>
<script type="text/javascript">
	var listId = "#list2", 
	editorFormId = "#editorForm", 
	pagerId = "#pager2", 
	listUrl = "../examNumberManagement/examNumberManage.do?command=searchPaging";
	$(function() {
		$(listId).trigger("reloadGrid");
		_initButtons({
			cancelBTN : function() {
			},
			backBTN : function() {

			},
		}); //from page.common.js
		_initFormControls(); //from page.common.js
		_initValidateForXTypeForm(editorFormId);
		loadDictionary();
		var _colModel = [
				{
					name : 'Id',
					key : true,
					width : 60,
					hidden : true,
					search : false
				},
				{
					label : "测试时间",
					name : 'Exam_Time',
					sortable : false,
					width : 100,
					align : "center",
				},
				{
					label : "测试编号",
					name : 'Exam_Number',
					sortable : false,
					width : 150,
					align : "center",
				},
				{
					name : 'Grade_Code',
					sortable : false,
					width : 60,
					hidden : true,
				},
				{
					label : "测试名称",
					name : 'Exam_Name',
					sortable : false,
					autoWidth : true,
					align : "left",
				},
				{
					label : "阅卷人分配情况",
					name : '',
					sortable : false,
					width : 110,
					align : "center",
					formatter : function(ar1, ar2, ar3) {
						var operStr	= "<button id='queryBTN' class='page-button' title='查看' onclick='selectExamState(\""
							+ ar3.Exam_Number
							+ "\",\""
							+ ar3.Grade_Code
							+ "\");'><i class='fa fa-file-text' style='margin-right: 5px;'></i>查看</button>";
						return operStr;
					}
				} ];
		$(listId).jqGrid($.extend(defaultGridOpts, {
			url : listUrl,
			colModel : _colModel,
			pager : pagerId
		}));
		resizeFun();
	});

	//查看考号生成情况详情
	function selectExamState(examCode, gradeCode) {
		var queryExamCodeAndGradeCodeUrl = "../examInfo/markingArrangement_xjkw.do?command=queryExamCodeAndGradeCode";
		var data = {
			gradeId : gradeCode,
			examCode : examCode
		}
		$.ajax({
					url : queryExamCodeAndGradeCodeUrl,
					type : "POST",
					data : data,
					dataType : "JSON",
					success : function(data) {
						var markingArrangemetnInfoUrl = "examInfo/markingArrangementInfo.do";
						frameDialog(markingArrangemetnInfoUrl, "阅卷人分配详情", {
							mode : "middle",
							resizable : false,
							width : 660,
							height : 328,
							buttons : [ {
								text : "返回",
								icons : {
									primary : "ui-icon-cancel"
								},
								click : function(ev) {
									var $this = window.top.$(this);
									$this.dialog("close");
								}
							} ]
						});
					}
				});

	}
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
						<td style="padding-left: 24px; padding-right: 5px;"><input
							id="fastQueryText" type="text" placeholder="输入测试名称"
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