
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="../theme/default/master.css" rel="stylesheet" />
<link href="../theme/default/ui.custom.css" rel="stylesheet" />
<link href="../theme/default/ui.jqgrid.css" rel="stylesheet" />
<link href="../theme/default/font.awesome.css" rel="stylesheet" />
<link href="../theme/default/ui.chosen.css" rel="stylesheet" />
<link href="../theme/default/ui.uploadfile.css" rel="stylesheet" />
<link href="../theme/default/ui.pick.css" rel="stylesheet" />
<link href="../theme/default/page.common.css" rel="stylesheet" />
<link href="../theme/default/jquery.multiselect.css" rel="stylesheet" />
<link href="../theme/default/jquery.multiselect.filter.css"
	rel="stylesheet" />
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

.ui-state-default em {
	font-size: 16px;
	color: white;
}

</style>

<script type="text/javascript">
	var listId = "#list2", editorFormId = "#editorForm", pagerId = '#pager2', editorRelatedFormId = "#editorRelatedForm", listUrl = "../statisticsAnalysis/scoreAnalysis.do?command=searchScore";
	$(function() {
		//多选下拉框
		$("#school").multiselect({
			checkAllText : "全选",
			uncheckAllText : "全不选",
			noneSelectedText : "选择学校",
			selectedText : '#' + " 所学校",
			selectedList : 2
		}).multiselectfilter({
			label : "学校名称",
			placeholder : "请输入校名"
		});

		$("#course").multiselect({
			checkAllText : "全选",
			uncheckAllText : "全不选",
			noneSelectedText : "选择科目",
			selectedText : '#' + " 科目",
			selectedList : 2
		});

		_initButtons({
			cancelBTN : function() {
				$(listId).trigger("reloadGrid");
				hideSlidePanel(".page-editor-panel");
				editId = '';
			},
		}); //from page.common.js
		$("#tblInfo").find("button").button();
		$("#tabs").tabs({
			heightStyle : "fill"
		});
		$("#tabs-2,#tabs-1").css("height", "auto");
		_initFormControls(); //from page.common.js
		_initValidateForXTypeForm(editorFormId);

		$("#fastSearch")
				.click(
						function() {
							//绑定jqgrid
							var _colModel = [ {
								name : 'Exam_Number',
								key : true,
								autoWidth : true,
								hidden : true,
								search : false
							}, {
								label : "学校名称",
								name : 'School_Name',
								sortable : false,
								autoWidth : true,
								align : "left",
							} ];
							var fruit = "";
							var courseTxt = [];
							var cousreVal = [];
							//循环遍历得到选中的科目
							var txt = $("#course").find("option:selected")
									.each(function() {
										courseTxt.push($(this).text());
										cousreVal.push($(this).val());
									});
							//声明对象
							function Object(label, name, sortable, autoWidth,
									align) {
								this.label = label;
								this.name = name;
								this.sortable = sortable;
								this.autoWidth = autoWidth;
								this.align = align;
							}
							for (var a = 0; a < courseTxt.length
									&& a < cousreVal.length; a++) {
								//追加列表信息
								_colModel.push(new Object("优率", cousreVal[a]
										+ "Yl", false, true, "center"));
								_colModel.push(new Object("良率", cousreVal[a]
								+ "Ll", false, true, "center"));
								_colModel.push(new Object("优良率", cousreVal[a]
										+ "Yll", false, true, "center"));
								_colModel.push(new Object("及格率", cousreVal[a]
										+ "Jgl", false, true, "center"));
								_colModel.push(new Object("平均分", cousreVal[a]
										+ "sAvg", false, true, "center"));
							}
							//成绩信息描述
							var scoreHtml;
							var schoolYear = $("#schoolYear").find(
									"option:selected").text();
							var schoolType = $("#schoolType").find(
									"option:selected").val();
							var term = $("#term").find("option:selected").val();
							var examType = $("#examType").find(
									"option:selected").val();
							var grade = $("#grade").find("option:selected")
									.val();
							var course = $("#course").find("option:selected")
									.val();
							var school = $("#school").find("option:selected")
									.val();
							if (schoolType != "") {
								schoolType = $("#schoolType").find(
										"option:selected").text();
							} else {
								window.message({
									text : "请选择学校类型",
									title : "提醒",
									buttons : {
										"确认" : function() {
											window.top.$(this).dialog("close");
										}
									},
								});
								return;
							}
							if (term != "") {
								term = $("#term").find("option:selected")
										.text();
							} else {
								window.message({
									text : "请选择学期",
									title : "提醒",
									buttons : {
										"确认" : function() {
											window.top.$(this).dialog("close");
										}
									},
								});
								return;
							}
							if (examType != "") {
								examType = $("#examType").find(
										"option:selected").text();
							} else {
								window.message({
									text : "请选择测试类型",
									title : "提醒",
									buttons : {
										"确认" : function() {
											window.top.$(this).dialog("close");
										}
									},
								});
								return;
							}
							if (grade != "") {
								grade = $("#grade").find("option:selected")
										.text()
										+ "年级";
							} else {
								window.message({
									text : "请选择年级",
									title : "提醒",
									buttons : {
										"确认" : function() {
											window.top.$(this).dialog("close");
										}
									},
								});
								return;
							}
							if (school != "") {
								school = $("#school").find("option:selected")
										.text();
							}else {
								window.message({
									text : "请选择学校",
									title : "提醒",
									buttons : {
										"确认" : function() {
											window.top.$(this).dialog("close");
										}
									},
								});
								return;
							}
							if (courseTxt.length > 0) {
								course = $("#course").find("option:selected")
										.text();
							} else {
								window.message({
									text : "请选择科目",
									title : "提醒",
									buttons : {
										"确认" : function() {
											window.top.$(this).dialog("close");
										}
									},
								});
								return;
							}
							//列表名称
							scoreHtml = schoolYear + examType + school + grade
									+ term + courseTxt + "成绩分析列表";

							//重新创建列表
							$(listId).GridUnload();
							$(listId).jqGrid($.extend(defaultGridOpts, {
								url : listUrl,
								colModel : _colModel,
								pager : pagerId,
								caption : scoreHtml,
								rowNum : 15,
								rowList : [ 15, 20, 30, 50, 100 ],
								height : "100%",
								multiselect : false,
								loadComplete : function() {
									loadScoreByCourse();
								}
							}));
							//在表头行上增加附加的列需要参数数组
							var groupHeader = [];
							//循环遍历得到参数
							for (var i = 0; i < courseTxt.length
									&& i < cousreVal.length; i++) {
								function getGroupHeader(startColumnName,
										numberOfColumns, titleText) {
									this.startColumnName = startColumnName;
									this.numberOfColumns = numberOfColumns;
									this.titleText = titleText;
								}
								groupHeader.push(new getGroupHeader(
										cousreVal[i] + "Yl", 5, '<em>'
												+ courseTxt[i] + '<em>'));
							}
							//在表头行上增加附加的列方法
							function loadScoreByCourse() {
								$(listId).jqGrid('setGroupHeaders', {
									useColSpanStyle : true,
									groupHeaders : groupHeader,
								});
							}
							resizeFun("495");
// 							$(".l_statis").show();
						});
		//加载数据字典
		loadDictionary(function() {
			//显示当前年
			var year = new Date().getFullYear();
			var month = new Date().getMonth() + 1;
			var currentYear = "";
			if (month < 9) {
				currentYear = (year - 1) + "-" + year;
			} else {
				currentYear = year + "-" + (year + 1);
			}
			$("#schoolYear").val(currentYear);
		});
		//选择学校类型 得到相对应的学科
		$("#schoolType").change(
						function() {
							$("#course").attr("multiple", "multiple");
							$("#course option[value != '']").remove();
							var schoolType = $("#schoolType").val();
							if (schoolType != "") {
								var url = "../platform/dictionary.do?command=getCoursesByCode";
								var data = {
									schoolType : schoolType
								};
								$.ajax({
											url : url,
											type : "POST",
											data : data,
											dataType : "JSON",
											success : function(data) {
												for (var i = 0; i < data.length; i++) {
													$("#course").append("<option value='"+data[i].DictionaryCode+"'>"
																			+ data[i].DictionaryName
																			+ "</option>");
												}
												if ($("#course").find("option").length > 0) {
													//多选下拉框
													$("#course").multiselect(
																	{
																		checkAllText : "全选",
																		uncheckAllText : "全不选",
																		noneSelectedText : "选择科目",
																		selectedText : '#'
																				+ " 科目",
																		selectedList : 2
																	});
													//刷新多选下拉框的值
													$("#course").multiselect('refresh');
												} else {
													$("#course").multiselect(
																	{
																		checkAllText : "全选",
																		uncheckAllText : "全不选",
																		noneSelectedText : "选择科目",
																		selectedText : '#'
																				+ " 科目",
																	});
												}
											}
										});
							} else {
								$("#course").multiselect('refresh');
							}
						});

		//选择学校类型关联年级
		$("#schoolType").change(function() {
							$("#grade option[value != '']").remove();
							var schoolType = $("#schoolType").val();
							if (schoolType != "") {
								var url = "../platform/dictionary.do?command=getGradessByCode";
								var data = {
									schoolType : schoolType
								};
								$.ajax({
											url : url,
											type : "POST",
											data : data,
											dataType : "JSON",
											success : function(data) {
												for (var i = 0; i < data.length; i++) {
													$("#grade").append(
																	"<option value='" + data[i].DictionaryCode + "'>"
																			+ data[i].DictionaryName
																			+ "</option>");
												}
											}
										});
							}

						});

		//选择学校类型关联学校
		$("#schoolType").change(function() {
							$("#school option[value != '']").remove();
							var schoolType = $("#schoolType").val();
							if (schoolType != "") {
								var url = "../platform/dictionary.do?command=getSchoolByCode";
								var data = {
									schoolType : schoolType
								};
								$.ajax({
											url : url,
											type : "POST",
											data : data,
											dataType : "JSON",
											success : function(data) {
												for (var i = 0; i < data.length; i++) {
													$("#school").append(
																	"<option value='" + data[i].School_Code + "'>"
																			+ data[i].School_Name
																			+ "</option>");
												}
												if ($("#school").find("option").length > 0) {
													//多选下拉框
													$("#course").multiselect(
																	{
																		checkAllText : "全选",
																		uncheckAllText : "全不选",
																		noneSelectedText : "选择科目",
																		selectedText : '#'
																				+ " 科目",
																		selectedList : 2
																	});
													//刷新多选下拉框的值
													$("#school").multiselect(
															'refresh');
												} else {
													$("#course").multiselect(
																	{
																		checkAllText : "全选",
																		uncheckAllText : "全不选",
																		noneSelectedText : "选择科目",
																		selectedText : '#'
																				+ " 科目",
																	});
												}
											}
										});
							}
						});
		
		
	});
	window.onload = function() {
		$("#schoolType option[value != '']").remove();
		$("#term option[value != '']").remove();
		$("#examType option[value != '']").remove();

	}
</script>


</head>
<body style="overflow: auto;">
	<div class="page-list-panel"
		style="overflow-y: auto; overflow-x: hidden;">
		<div class="head-panel">
			<!-- 多条件 -->
			<div id="importCondition" style="margin: -5px 10px 10px 0px;">
				<div class="search-panel" style="display: block;">
					<p>
						学年 <select id="schoolYear" name="schoolYear" class="form-control"
							data-dic="{code:'ND'}" style="width: 150px;"></select>&nbsp; 学校类型
						<select id="schoolType" name="schoolType" class="form-control"
							style="width: 150px;" data-dic="{code:'xxlx'}">
							<option value="">选学校类型</option>
						</select>&nbsp; 年级 <select id="grade" name="grade" class="form-control"
							style="width: 150px;">
							<option value="">选择年级</option>
						</select>&nbsp; 学期 <select id="term" name="term" class="form-control"
							style="width: 150px;" data-dic="{code:'xq'}">
							<option value="">选择学期</option>
						</select>&nbsp; 测试类型 <select id="examType" name="examType"
							class="form-control" style="width: 150px;"
							data-dic="{code:'kslx'}">
							<option value="">选测试类型</option>
						</select>
					</p>
					<!-- <p>
					功能类型 
						<select id="gnType" name="gnType" class="form-control" style="width: 150px;"data-dic="{code:'gnType'}">
							<option value="">选择功能类型</option>
						</select>
					&nbsp;
					功能选择
					    <select id="yllAndAvg" name="yllAndAvg" class="form-control" style="width: 150px;">
							<option value="">选择功能类型</option>
						</select>
					</p> -->
				</div>
				<div class="multiselect" style="margin: -5px 10px 10px 0px;">
					<p>
						科目 <select id="course" name="course" class="form-control"
							style="width: 370px; height: 25px;"></select>&nbsp; 学校 <select
							id="school" name="school" multiple="multiple"
							class="form-control">
							<%-- <c:forEach items="${schoolList}" var="school">
                                <option value="${school.School_Code}">${school.School_Name }</option>
                            </c:forEach> --%>
						</select> <span class="toolbar">
							<button id="fastSearch" title="分析" style="margin-left: 0px;">
								<i class="fa fa-search"></i>分析
							</button>
						</span>
					</p>
				</div>
			</div>
		</div>
		<table id="list2"></table>
		<div id="pager2"></div>
		<div class="l_statis" style="display: none;">
			<div class="l_title02">图表统计</div>
			<div class="l_form">
				<div class="l_form01" id="main1"
					style="width: 508px; height: 315px;"></div>
				<div class="l_form01" id="main2"
					style="width: 508px; height: 315px;"></div>
				<div class="l_form01" id="main3"
					style="width: 508px; height: 315px;"></div>
				<div class="l_form01" id="main4"
					style="width: 508px; height: 315px;"></div>
			</div>
		</div>
	</div>
</body>
</html>