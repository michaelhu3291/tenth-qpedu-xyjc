<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
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
<script type="text/javascript" src='../js/highcharts.js'></script>
<script type="text/javascript" src='../js/util.js'></script>
<script type="text/javascript" src="../js/jquery.multiselect.js"></script>
<script type="text/javascript" src="../js/jquery.multiselect.filter.js"></script>
<style>
#add_exam_info ul li {
	list-style: none;
	margin-top: 30px;
}
</style>
<script type="text/javascript">
	var listId = "#list2", 
	         editorFormId = "#editorForm", 
	         pagerId = "#pager2", 
	         loadUrl = "../examInfo/examManage.do?command=selectExamById", 
			 saveUrl = "../examInfo/examManageSchool.do?command=addSchoolExam",
			 listUrl = "../examInfo/examManage.do?command=searchPaging", 
			classAndCourseSelectUrl = "../examInfo/examManageSchool.do?command=getClassIdAndCourse";

	$(function() {
		var loadClass = function() {
			$("#classId option[value != '']").remove();
			var grade = $("#grade").val();
			if (grade != "") {
				//得到科目id查询
				var url = "../examInfo/examManageSchool.do?command=getClassBySchoolAndGrade";
				var data = {
					gradeId : grade
				};
				$.ajax({
					url : url,
					type : "POST",
					data : data,
					dataType : "JSON",
					success : function(data) {
						for (var i = 0; i < data.length; i++) {
							$("#classId").append("<option value='"+data[i].Class_Id+"'>"
													+ _typesClass[data[i].Class_Id]
													+ "</option>");
						}
						if ($("#classId").find("option").length > 0) {
							$("#classId").multiselect({
								checkAllText : "全选",
								uncheckAllText : "全不选",
								noneSelectedText : "选择班级",
								selectedText : '#' + " 个班级",
								selectedList : 2
							});
							$("#classId").multiselect('refresh');
						}
					}
				});
			} else {
				$("#classId").multiselect('refresh');
			}
		}
		
		var loadCourse=function(){
			$("#course option[value != '']").remove();
			$.ajax({
	            url: "../statisticsAnalysis/politicalInstructor.do?command=getCoursesBySchoolType",
	            type: "POST",
	            data: {},
	            dataType: "JSON",
	            success: function (data) {
	                for (var i = 0; i < data.length; i++) {
	                    $("#course").append("<option value='" + data[i].DictionaryCode + "'>" + data[i].DictionaryName + "</option>");
	                }
	            	if ($("#course").find("option").length > 0) {
						$("#course").multiselect({
							checkAllText : "全选",
							uncheckAllText : "全不选",
							noneSelectedText : "选择科目",
							selectedText : '#' + " 科",
							selectedList : 2
						});
						$("#course").multiselect('refresh');
					}else{
						$("#course").multiselect('refresh');
					}
				}
			});
		}
		_initButtons({
			cancelBTN : function() {
				$("#classId option[value != '']").remove();
				$("#course option[value != '']").remove();
				$("#editorForm")[0].reset();
				hideSlidePanel(".page-editor-panel");
			},
			editBTN : function(ev) {
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
				var rowData = $(listId).jqGrid("getRowData", idAry[0]);
				var datas = {
					examCode : rowData.examNumber
				}
				var oldExamCode = $("#examCode").val(rowData.examNumber);
				$("#classId option[value != '']").remove();
				$("#course option[value != '']").remove();
				$.ajax({
							url : loadUrl,
							type : "POST",
							data : {
								id : idAry[0]
							},
							dataType : "JSON",
							success : function(data) {
								var $piel = showSlidePanel(".page-editor-panel")
										.find("h4 i").removeClass();
								if ($i.length) {
									$piel.addClass($i.attr("class"));
								}
								$("#files_list").find("span").remove();
								$("#exam_time").val(
										data.exam_time.substring(0, 10));
								$("#exam_name").val(data.exam_Name);
								$("#schoolYear").val(data.school_Year);
								$("#term").val(data.term);
								$("#examType").val(data.exam_Type);
								$("#grade").val(data.grade_Code);
								//loadClass();
							//	loadCourse();
								var introducedTime = data.introduced_Time;
								if (introducedTime != null) {
									introducedTime = introducedTime.substring(
											0, 10);
								}
								$("#introducedTime").val(introducedTime);
								var closingTime = data.closing_Time;
								if (closingTime != null) {
									closingTime = closingTime.substring(0, 10);
								}
								$("#closingTime").val(closingTime);
								var fileStr = "";
								var fileObject = [];
								for (var i = 0; i < data.files.length; i++) {
									var fileParams = {};
									//$("#files").data().fileItems([{id:"",path:"",fileName:""}]);
									var fileObj = data.files[i];
									fileParams["id"] = fileObj.id;
									fileParams["path"] = fileObj.path;
									fileParams["fileName"] = fileObj.fileName;
									fileObject.push(fileParams);
								}
								$("#files").data().fileItems(fileObject);
								$.ajax({
											url : classAndCourseSelectUrl,
											type : "POST",
											data : datas,
											dataType : "JSON",
											success : function(data) {
												var object=data;
												$.ajax({
										            url: "../statisticsAnalysis/politicalInstructor.do?command=getCoursesBySchoolType",
										            type: "POST",
										            data: {},
										            dataType: "JSON",
										            success: function (data) {
										            	
										                for (var i = 0; i < data.length; i++) {
										                    $("#course").append("<option value='" + data[i].DictionaryCode + "'>" + data[i].DictionaryName + "</option>");
										                }
										                for (var i = 0; i < object.courselist.length; i++) {
															$("#course").find("option[value='"
																					+ object.courselist[i].Course
																					+ "']").attr("selected","selected");
														}
										            	if ($("#course").find("option").length > 0) {
															$("#course").multiselect({
																checkAllText : "全选",
																uncheckAllText : "全不选",
																noneSelectedText : "选择科目",
																selectedText : '#' + " 科",
																selectedList : 2
															});
															$("#course").multiselect('refresh');
														}else{
															$("#course").multiselect('refresh');
														}
													}
												});
												
												
												var grade = $("#grade").val();
												if (grade != "") {
													//得到科目id查询
													var url = "../examInfo/examManageSchool.do?command=getClassBySchoolAndGrade";
													var data = {
														gradeId : grade
													};
													$.ajax({
														url : url,
														type : "POST",
														data : data,
														dataType : "JSON",
														success : function(data) {
															for (var i = 0; i < data.length; i++) {
																$("#classId").append("<option value='"+data[i].Class_Id+"'>"
																						+ _typesClass[data[i].Class_Id]
																						+ "</option>");
															}
															for (var i = 0; i < object.list.length; i++) {
																$("#classId").find("option[value='"
																						+ object.list[i].Class_Id
																						+ "']").attr("selected","selected");
															}
															if ($("#classId").find("option").length > 0) {
																$("#classId").multiselect({
																	checkAllText : "全选",
																	uncheckAllText : "全不选",
																	noneSelectedText : "选择班级",
																	selectedText : '#' + " 个班级",
																	selectedList : 2
																});
																$("#classId").multiselect('refresh');
															}
														}
													});
												}
												
											}
										});
							}
						});
			}
		}); //from page.common.js
		_initFormControls(); //from page.common.js
		_initValidateForXTypeForm(editorFormId);

		var _colModel = [ {
			name : 'Id',
			key : true,
			width : 60,
			hidden : true,
			search : false
		}, {
			label : "测试日期",
			name : 'Exam_Time',
			sortable : false,
			width : 100,
			align : "center",
		}, {
			label : "测试编号",
			name : 'examNumber',
			sortable : false,
			width : 150,
			align : "center",
		}, {
			label : "测试名称",
			name : 'Exam_Name',
			sortable : false,
			autoWidth : true,
			align : "left",
		} ];

		$(listId).jqGrid($.extend(defaultGridOpts, {
			url : listUrl,
			colModel : _colModel,
			pager : pagerId
		}));
		resizeFun();
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
		$(".exam_time_datePicker").datepicker(
				{
					dateFormat : "yy-mm-dd",
					dayNamesMin : [ "日", "一", "二", "三", "四", "五", "六" ],
					monthNames : [ "一月", "二月", "三月", "四月", "五月", "六月", "七月",
							"八月", "九月", "十月", "十一月", "十二月" ],
					changeYear : true,
					changeMonth : true,
				})
		//校验 
		$("#exam_time,#exam_name,#grade,#schoolYear,#term,#examType,"+
				"#introducedTime,#closingTime,#classId,#course")
				.change(function() {
					if ($("#exam_time").val != '') {
						$(".exam_time_validate").html("");
					}
					if ($("#exam_name").val != '') {
						$(".exam_name_validate").html("");
					}
					if ($("#grade").val != '') {
						$(".grade_validate").html("");
					}

					if ($("#schoolYear").val != '') {
						$(".schoolYear_validate").html("");
					}
					if ($("#term").val != '') {
						$(".term_validate").html("");
					}
					if ($("#classId").val != '') {
						$(".classId_validate").html("");
					}
					if ($("#course").val != '') {
						$(".course_validate").html("");
					}
					if ($("#introducedTime").val != '') {
						$(".introducedTime_validate").html("");
					}
					if ($("#closingTime").val != '') {
						$(".closingTime_validate").html("");
					}
				});
 
		//添加页面显示
		$("#insertExam").click(
				function(ev) {
					$("#editorForm")[0].reset();
					$("#files_list").find("span").remove();
					$("#classId option[value != '']").remove();
					$("#course option[value != '']").remove();
					 $("#fileListTD").find("#files_list").next().html("");
					var $i = $(ev.currentTarget).find("i"), $piel = $(
							".page-editor-panel").show({
						effect : "slide",
						direction : "up",
						easing : 'easeInOutExpo',
						duration : 900
					});
					loadCourse();
				});

	
		
		//根据学校code加载年级
		$.ajax({
					url : "../statisticsAnalysis/schoolPlainAdminScoreSearch.do?command=getSchoolCodeAndSchoolName",
					type : "POST",
					data : {},
					dataType : "JSON",
					success : function(data) {
						//$("#course option[value != '']").remove();
						$("#grade option[value != '']").remove();
						//$("#schoolType option[value != '']").remove();
						//学校名称和学校code
						//$("#school").append("<option value='" + data.map.School_Code + "'>" + data.map.School_Name + "</option>");
						//学校类型
						//$("#schoolType").append("<option value='" + data.paramMap.schoolTypeCode + "'>" + data.paramMap.schoolTypeName + "</option>");
						//科目
						var dataList = data.paramList;
						for (var i = 0; i < dataList.length; i++) {
							$("#grade").append(
									"<option value='" + dataList[i].DictionaryCode + "'>"
											+ dataList[i].DictionaryName
											+ "</option>");
						}
					}
				});
		$("#deleteOper").click(function() {
			var selArr = $(listId).jqGrid('getGridParam', 'selarrrow');

			//alert(selArr[0]);
			//return;
			if (selArr.length == 0) {
				window.Msg.alert("请选择要删除的记录");
				return;

			}
			var examNumberList = [];
			for (var i = 0; i < selArr.length; i++) {
				var rowId = selArr[i];
				var rowData = $("#gridTable").jqGrid('getRowData', rowId);
				var examNumber = rowData.examNumber;
				examNumberList.push(examNumber);
			}

			var url = "../examInfo/examManage.do?command=delete";
			window.message({
				title : '提醒',
				text : '确定删除此测试吗?',
				buttons : {
					'确定' : function() {
						window.top.$(this).dialog("close");
						$.ajax({
							url : url,
							type : "POST",
							data : {
								selArr : selArr,
								examNumberList : examNumberList
							},
							dataType : "JSON",
							success : function(data, xhr) {
								if (data.mess == 'notDelete') {
									window.Msg.alert("此测试考号已经生成不能删除!");
									return;
								}
								if (data.mess == 'success') {
									window.Msg.alert("删除成功!");
								}
								$(listId).trigger("reloadGrid");
							}
						});
					},
					'取消' : function() {
						window.top.$(this).dialog("close");
					}
				}
			});
		});
		var introducedState = "";
		var getParam = function() {
			var exam_time = $("#exam_time").val();
			var schoolYear = $("#schoolYear").val();
			var term = $("#term").val();
			var examType = $("#examType").val();
			var grade = $("#grade").val();
			var gradeTxt = $("#grade").find("option:selected").text();
			var closingTime = $("#closingTime").val();
			var introducedTime = $("#introducedTime").val();
			var course = [];
			var val = $("#course").find("option:selected").each(function() {
				var params = {};
				params["course"] = $(this).val();
				params["courseName"] = $(this).text();
				course.push(params);
			});
			var classId = [];
			var val = $("#classId").find("option:selected").each(function() {
				var classParams = {};
				classParams["classId"] = $(this).val();
				classParams["className"] = $(this).text();
				classId.push(classParams);
			});
			var dataParam = $(editorFormId).getFormData();
			var files = [];
			$.each(dataParam.files, function(index, item) {
				var param = {};
				param["id"] = item.id;
				param["fileName"] = item.fileName;
				files.push(param);
			})
			//校验
			if (exam_time == '' || exam_time == null) {
				$(".exam_time_validate").html("请选择测试日期!");
				return;
			}
			if (schoolYear == '' || schoolYear == null) {
				$(".schoolYear_validate").html("请选择学年!");
				return;
			}
			if (grade == '' || grade == null) {
				$(".grade_validate").html("请选择年级!");
				return;
			}
			if (term == '' || term == null) {
				$(".term_validate").html("请选择学期!");
				return;
			}
			if (examType == '' || examType == null) {
				$(".examType_validate").html("请选择测试类型!");
				return;
			}
			   if (course== undefined) {
			     $(".course_validate").html("请选择学科!");
			     return;
			 }   
			   if (classId== undefined) {
				     $(".classId_validate").html("请选择班级!");
				     return;
				 }   
			if (introducedTime == '' || introducedTime == null) {
				$(".introducedTime_validate").html("请选择测试发布日期!");
				return;
			}
			if (closingTime == '' || closingTime == null) {
				$(".closingTime_validate").html("请选择考号截止日期!");
				return;
			}

			if (exam_time < introducedTime) {
				$(".introducedTime_validate").html("测试发布日期不能大于测试日期!");
				return;
			}
			var today = new Date(exam_time);
			var yesterday_milliseconds = today.getTime() - 1000 * 60 * 60 * 24;
			var yesterday = new Date();
			yesterday.setTime(yesterday_milliseconds);
			var strYear = yesterday.getFullYear();
			var strDay = yesterday.getDate();
			var strMonth = yesterday.getMonth() + 1;
			if (strMonth < 10) {
				strMonth = "0" + strMonth;
			}
			var strYesterday = strYear + "-" + strMonth + "-" + strDay;
			//$("#closingTime").val(strYesterday);
			if (closingTime > strYesterday) {
				$(".closingTime_validate").html("考号生成时间在测试时间的前一天!");
				return;
			}
			var oldExamCode = $("#examCode").val();
			POST(saveUrl, {
				exam_time : exam_time,
				schoolYear : schoolYear,
				grade : grade,
				gradeTxt : gradeTxt,
				term : term,
				examType : examType,
				closingTime : closingTime,
				introducedTime : introducedTime,
				introducedState : introducedState,
				files : files,
				course : course,
				classId:classId,
				oldExamCode : oldExamCode,
				id : $(listId).jqGrid("getGridParam", "selrow"),
			}, function(data) {
				if (data.mess == "examNumberMax") {
					window.Msg.alert("测试已达上限,请联系管理员!");
				}
				;
				if (data.mess == "addSuccess") {
					window.Msg.alert("添加成功!");
				}

				;
				if (data.mess == "notUpdate") {
					window.Msg.alert("此测试考号已经生成不能修改!");
				}
				;
				if (data.mess == "updateSuccess") {
					window.Msg.alert("修改成功!");
				}
				;
				hideSlidePanel(".page-editor-panel");
				$(listId).trigger("reloadGrid");
			});
		}
		//添加操作
		$("#editAdd").click(function() {
			introducedState = "0";
			getParam();
		});

		$("#introduced").click(function() {
			introducedState = "1";
			getParam();
		});
		$("#classId").multiselect({
			checkAllText : "全选",
			uncheckAllText : "全不选",
			noneSelectedText : "选择班级",
			selectedText : '#' + " 个班级",
			selectedList : 2
		});
		
		$("#course").multiselect({
			checkAllText : "全选",
			uncheckAllText : "全不选",
			noneSelectedText : "选择科目",
			selectedText : '#' + " 科",
			selectedList : 2
		});

		$("#grade").change(function() {
			loadClass();
		});

		$("#exam_time").change(function() {
			var examTime = $("#exam_time").val();
			var today = new Date(examTime);
			var yesterday_milliseconds = today.getTime() - 1000 * 60 * 60 * 24;
			var yesterday = new Date();
			yesterday.setTime(yesterday_milliseconds);
			var strYear = yesterday.getFullYear();
			var strDay = yesterday.getDate();
			var strMonth = yesterday.getMonth() + 1;
			if (strMonth < 10) {
				strMonth = "0" + strMonth;
			}
			var strYesterday = strYear + "-" + strMonth + "-" + strDay;
			$("#closingTime").val(strYesterday);
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
							<button id="insertExam">
								<i class="fa fa-plus"></i>添加
							</button>
							<button id="editBTN">
								<i class="fa fa-pencil"></i>修改
							</button>
							<button id="deleteOper">
								<i class="fa fa-trash-o"></i>删除
							</button>
						</td>
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
	<div class="page-editor-panel full-drop-panel">
		<div class="title-bar">
			<h4>
				<i class="fa fa-plus"></i>
			</h4>
			<div class="btn-area">
				<div style="margin-top: 4px;">
					<button id="introduced">
						<i class="fa fa-check"></i>发布
					</button>
					<button id="editAdd">
						<i class="fa fa-check"></i>保存
					</button>
					<button id="cancelBTN">
						<i class="fa fa-times"></i>取消
					</button>
				</div>
			</div>
		</div>
		<div class="page-content">
			<div class="page-inner-content">
				<form id="editorForm">
					<div id="add_exam_info">
						<input id="examCode" type="hidden" />
						<table cellspacing="0" border="0" style="width: 94.7%;"
							class="tableTemplet">
							<thead>
								<tr>
									<th colspan="4" style="color: black;"><i
										class="fa fa-file-text" style="margin-right: 5px;"></i> <span>添加测试</span></th>
								</tr>
							</thead>
							<tbody id="editorArea">
								<tr>
									<td class="label" style="width: 8%">测试日期:</td>
									<td style="width: 6%"><input type="text" id="exam_time"
										class="form-control exam_time_datePicker"
										placeholder="请输入测试时间" style="width: 205px;" /> <span
										class="exam_time_validate" style="color: red;"></span></td>

									<td class="label" style="width: 2%">学年:</td>
									<td style="width: 15%"><select id="schoolYear"
										name="schoolYear" data-dic="{code:'ND'}" class="form-control"
										style="width: 213px;">
											<option value="">请选择学年</option>
									</select> <span class="schoolYear_validate" style="color: red;"></span>
									</td>
								</tr>
								<tr>
									<td class="label" style="width: 5%">年级:</td>
									<td style="width: 6%"><select id="grade" name="grade"
										class="form-control" style="width: 213px;">
											<option value="">请选择年级</option>
									</select> <span class="grade_validate" style="color: red;"></span></td>
									<td class="label" style="width: 5%">学期:</td>
									<td><select id="term" name="term" class="form-control"
										data-dic="{code:'xq'}" style="width: 213px;">
											<option value="">请选择学期</option>
									</select> <span class="term_validate" style="color: red;"></span></td>
								</tr>
								<tr>
									<td class="label" style="width: 5%">测试类型:</td>
									<td style="width: 15%" colspan="3"><select id="examType"
										name="examType" class="form-control" data-dic="{code:'kslx'}"
										style="width: 213px;">
											<option value="">请选择测试类型</option>
									</select> <span class="examType_validate" style="color: red;"></span></td>
								</tr>
								<tr>
									<td class="label" style="width: 5%">班级:</td>
									<td style="width: 15%" colspan="3">
										<div class="multiselect">
											<select id="classId" name="classId" class="form-control"
												multiple="multiple" style="width: 213px; height: 26px;">
											</select>
										</div> <span class="classId_validate" style="color: red;"></span>
									</td>
								</tr>
								<tr>
								<tr>
									<td class="label" style="width: 5%">科目:</td>
									<td style="width: 15%" colspan="3">
										<div class="multiselect">
											<select id="course" name="course" class="form-control"
												multiple="multiple" style="width: 213px; height: 26px;">
											</select>
										</div> <span class="course_validate" style="color: red;"></span>
									</td>
								</tr>
								<tr>
									<td class="label" style="width: 5%">发布日期:</td>
									<td style="width: 15%"><input type="text"
										id="introducedTime" class="form-control exam_time_datePicker"
										placeholder="请输入发布测试时间" style="width: 205px" /> <span
										class="introducedTime_validate" style="color: red;"></span></td>
									<td class="label" style="width: 5%">考号生成截止日期:</td>
									<td style="width: 15%"><input type="text" id="closingTime"
										class="form-control exam_time_datePicker"
										placeholder="请输入生成考号截止日期" style="width: 205px" /> <span
										class="closingTime_validate" style="color: red;"></span></td>
								</tr>
								<tr>
									<td class="label">相关附件：</td>
									<td colspan="3"><input data-xtype="upload"
										data-appendto="#fileListTD" type="file" name="files"
										id="files" style="width: 667px;" data-button-text="上传附件" />
										<div id="fileListTD" class="fileListTD"
											style="word-break: break-all;"></div></td>
								</tr>
						</table>
					</div>
				</form>
			</div>
		</div>
	</div>
</body>
</html>