
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
	var listId = "#list2", editorFormId = "#editorForm", pagerId = '#pager2', editorRelatedFormId = "#editorRelatedForm", listUrl = "../statisticsAnalysis/scoreSearch.do?command=searchScore";
	var idx="",
	ci="",
	so="";
	var getPrint=function(){
    	var schoolYear = $("#schoolYear").find("option:selected").text();
		var term = $("#term").find("option:selected").val();
		var termTxt = $("#term").find("option:selected").text();
		var examType = $("#examType").find("option:selected").val();
		var examTypeTxt = $("#examType").find("option:selected").text();
		var grade = $("#grade").find("option:selected").val();
		var gradeTxt = $("#grade").find("option:selected").text();
		var classArr = $("#class").find("option:selected").val();
		var course = $("#course").find("option:selected").val();
		var examNumberOrStuCode = $("#examNumberOrStuCode").val();
		var courseTxt = $("#course").find("option:selected").text();
		var gradeOptions = $("#grade").find("option:selected");
		var classOptions = $("#class").find("option:selected");
		var scoreHtml = schoolYear+ "学年" + gradeTxt + termTxt + examTypeTxt + courseTxt + "测试成绩列表";
		var courseOptions = $("#course").find("option:selected");
		 if(gradeOptions.length === 0){
			 window.Msg.alert("请选择年级");
			 return;
		 }
		 if(classOptions.length === 0){
			 window.Msg.alert("请选择班级");
			 return;
		 }
		 if(courseOptions.val() == ""){
			 window.Msg.alert("请选择科目");
			 return;
		 }
		 
    	if(course !=""){
    		var classArr = [];
			if(classOptions.length > 0){
				$.each(classOptions,function(index,item){
					var str = $(item).val();
					classArr.push(str);
				});
			}
    		var data={
    				schoolYear:schoolYear,
    				term:term,
    				examType:examType,
    				course:course,
    				grade:grade,
    				gradeTxt:gradeTxt,
    				classArr:classArr,
    				examNumberOrStuCode:examNumberOrStuCode,
    				courseTxt:courseTxt,
    				scoreHtml:scoreHtml,
    				idx:idx,
    				ci:ci,
    				so:so
    		}
        	var printUrl="../statisticsAnalysis/printLook.do?command=searchScoreForTeacher&data="+JSON.stringify(data);
        	window.open(printUrl);
    	}else{
    		window.Msg.alert("没有数据，无法打印预览");
			 return;
    	}
    	
    }
	
	$(function() {
		//多选下拉框
		/*$("#school").multiselect({
			checkAllText : "全选",
			uncheckAllText : "全不选",
			noneSelectedText : "选择学校",
			selectedText : '#' + " 所学校",
			selectedList : 2
		}).multiselectfilter({
			label : "学校名称",
			placeholder : "请输入校名"
		});*/

		$("#class").multiselect({
			minWidth : 220,
			checkAllText : "全选",
			uncheckAllText : "全不选",
			noneSelectedText : "选择班级",
			selectedText : '#' + "个班级",
			selectedList : 4
			/* hide : null,
			autoOpen : false */
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

		$("#fastSearch_history").click(function() {
			
			 //成绩信息描述
			 var scoreHtml;
			 var schoolYearTxt = $("#schoolYear").find("option:selected").text();
			 var schoolYear = $("#schoolYear").find("option:selected").val();
			 //var schoolType = $("#schoolType").find("option:selected").text();
			 var term = $("#term").find("option:selected").val();
			 var termTxt = $("#term").find("option:selected").text();
			 var examType = $("#examType").find("option:selected").val();
			 var examTypeTxt = $("#examType").find("option:selected").text();
			 var grade = $("#grade").find("option:selected").val();
			 var gradeTxt = $("#grade").find("option:selected").text();
			 var course = $("#course").find("option:selected").val();
			 var courseTxt = $("#course").find("option:selected").text();
			 var classOptions = $("#class").find("option:selected");
			 
			 var gradeOptions = $("#grade").find("option:selected");
				var courseOptions = $("#course").find("option:selected");
				 if(gradeOptions.length === 0){
					 window.Msg.alert("请选择年级");
					 return;
				 }
				 if(classOptions.length === 0){
					 window.Msg.alert("请选择班级");
					 return;
				 }
				 if(courseOptions.val() == ""){
					 window.Msg.alert("请选择科目");
					 return;
				 }
			 var classArr = [];
			 if(classOptions.length > 0) {
				 $.each(classOptions,function(index,item){
					 var strArr = $(item).val().split(",");
				 }); 
			 }
			 //$("#classHidden").val(classArr);
			 
			 var _colModel = [{
				 name: 'Id',
	                key: true,
	                width: 60,
	                hidden: true,
	                search: false
	            },{
	            	name: 'Exam_Number',
	                sortable: false,
	                width: 100,
	                align: "center",

	         },{
	        	 name: 'XJFH',
	                sortable: false,
	                autoWidth: true,
	                align: "center",

	         },{
	                name: 'Name',
	                sortable: false,
	                autoWidth: true,
	                align: "center",

	         }, {
	                name: 'Grade_Id',
	                sortable: false,
	                width: 100,
	                align: "center",
	                formatter: function (ar1, ar2, ar3) {
	                    return (_types[ar3.Grade_Id] || "");
	                }

	         },{
	        	 name: 'Class_Id',
	                sortable: false,
	                width: 100,
	                align: "center",
	                formatter: function (ar1, ar2, ar3) {
	                    return (_typesClass[ar3.Class_Id] || "");
	                }

	         },{
	        	 name: 'orderGrade',
	                sortable: false,
	                width: 100,
	                align: "center",
	         },{
	        	 name: 'orderClass',
	                sortable: false,
	                width: 100,
	                align: "center",
	         },{
	                name: 'Total_Score',
	                width: 100,
	                align: "center",
	         }
	         ],
	         _colNames = ['','考号','学籍号','姓名','年级','班级','年级名次','班级名次','总分'];
			 /*if(grade != ""){
				 scoreHtml = schoolYear + "学年" + gradeTxt + term + examType + "测试成绩列表";  
			 } else {
				 scoreHtml = schoolYear + "学年" + term + examType + "测试成绩列表";
			 }*/
			 if(grade == ""){
				 scoreHtml = schoolYearTxt + "学年" + termTxt + examTypeTxt + "测试成绩列表";  
			 } 
			 else if(grade != ""){
				 
				 if(course == ""){
					 scoreHtml = schoolYearTxt + "学年" + gradeTxt + termTxt + examTypeTxt + "测试成绩列表";  
				 } else {
					 scoreHtml = schoolYearTxt + "学年" + gradeTxt + termTxt + examTypeTxt + courseTxt + "测试成绩列表";  
				 }
			 }
			 
			//重新创建列表
			$(listId).GridUnload();	 
	     $(listId).jqGrid($.extend(defaultGridOpts, {
	         url: listUrl,
	         colNames: _colNames,
	         colModel: _colModel,
	         pager: pagerId,
	         caption : scoreHtml,
	         rowNum: 20,
		     rowList: [20, 30, 50, 100],
			 height : "100%",
			 multiselect : false,
			 onSortCol: function (index, columnIndex, sortOrder) {
					idx=index;
					ci=columnIndex;
					so=sortOrder;
			    }
	     }));
	     resizeFun("549");
		 $(".l_statis").show();
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
		
		//根据学校code查询年级
		$.ajax({
            url: "../statisticsAnalysis/schoolPlainAdminScoreSearch.do?command=getSchoolCodeAndSchoolName",
            type: "POST",
            data: {},
            dataType: "JSON",
            success: function (data) {
            	    //$("#course option[value != '']").remove();
            	    $("#grade option[value != '']").remove();
            	    //$("#schoolType option[value != '']").remove();
            		//学校名称和学校code
                    //$("#school").append("<option value='" + data.map.School_Code + "'>" + data.map.School_Name + "</option>");
                  	//学校类型
                    //$("#schoolType").append("<option value='" + data.paramMap.schoolTypeCode + "'>" + data.paramMap.schoolTypeName + "</option>");
                  	//科目
                  	if(data.paramList){
                  		var dataList = data.paramList;
                        for (var i = 0; i < dataList.length; i++) {
                            $("#grade").append("<option value='" + dataList[i].DictionaryCode + "'>" + dataList[i].DictionaryName + "</option>");
                        }
                  	}
                  	if(data.gradeList){
                  		var dataList = data.gradeList;
                        for (var i = 0; i < dataList.length; i++) {
                            $("#grade").append("<option value='" + dataList[i].Grade + "'>" + _types[dataList[i].Grade] + "</option>");
                        }
                  	}
                  	
            }
     });
		
		//选择年级关联科目
        $("#grade").change(function(){
       	 //$("#course").find("option").attr("value").remove();
       	 $("#course option[value != '']").remove();
       	 var grade = $("#grade").val();
       	 var url = "../authority/dataDictionary.do?command=getCoursesByCode";
       	 //data = {grade:grade};
       	 if(grade != ""){
       		var schoolType = ""; 
       		if(grade == "11" || grade == "12" || grade == "13" || grade == "14" || grade == "15"){
       			schoolType = "xx";
       		}
       		if(grade == "16" || grade == "17" || grade == "18" || grade == "19"){
       			schoolType = "cz";
       		} 
       		if(grade == "31" || grade == "32" || grade == "33"){
       			schoolType = "gz";
       		}
       		var data = {schoolType:schoolType};
       		$.ajax({
                url: url,
                type: "POST",
                data: data,
                dataType: "JSON",
                success: function (data) {
               	 for(var i = 0;i < data.length;i++){
           			 $("#course").append("<option value='"+data[i].DictionaryCode+"'>"+data[i].DictionaryName+"</option>");
           		 }
                }
            });
       	 }
       	 
        });
		
      //选择年级关联班级
        $("#grade").change(function(){
       	 //$("#course").find("option").attr("value").remove();
       	 $("#class option[value != '']").remove();
       	 $("#class").multiselect('refresh');
       	 var grade = $("#grade").val();
       	 var url = "../scoreManage/historyScore.do?command=getclassesByGradeCode";
       	 if(grade != ""){
       		var data = {grade:grade};
       		$.ajax({
                url: url,
                type: "POST",
                data: data,
                dataType: "JSON",
                success: function (data) {
                if(data.length > 0)	{
                	 for(var i = 0;i < data.length;i++){
               			 $("#class").append("<option value='"+data[i].Grade_No+','+data[i].Class_No+"'>"+data[i].Class_Name+"</option>");
               		 }
                	 //_types[data[i].NJID]+_typesClass[data[i].Class_Name]
                	 //$("#class option:first").attr("selected",false);
                	 $("#class").multiselect('refresh');
                 }
               	
                }
            });
       	 }
       	 
        });

		
		//选择学校类型关联年级
		/*$("#schoolType").change(function() {
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

						});*/

		//选择学校类型关联学校
		/*$("#schoolType").change(function() {
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
		
		
		/* $("#gnType").change(function(){
			$("#yllAndAvg option[value != '']").remove();
			var gnType = $("#gnType").val();
			if (gnType != "") {
				var url = "../platform/dictionary.do?command=loadChildCode";
				var data = {
						codes : gnType
				};
				$.ajax({
							url : url,
							type : "POST",
							data : data,
							dataType : "JSON",
							success : function(data) {
								for (var i = 0; i < data.length; i++) {
									$("#yllAndAvg").append(
													"<option value='" + data[i].DictionaryCode + "'>"
															+ data[i].DictionaryName
															+ "</option>");
								}
							}
						});
			}
			
		}); */
		
	});
	
	//老师,教导员,学校管理员导出成绩查询数据
    var exportExcel = function(){
    	
    	 //成绩信息描述
		 var scoreHtml;
		 var schoolYearTxt = $("#schoolYear").find("option:selected").text();
		 var schoolYear = $("#schoolYear").find("option:selected").val();
		 //var schoolType = $("#schoolType").find("option:selected").text();
		 var term = $("#term").find("option:selected").val();
		 var termTxt = $("#term").find("option:selected").text();
		 var examType = $("#examType").find("option:selected").val();
		 var examTypeTxt = $("#examType").find("option:selected").text();
		 var grade = $("#grade").find("option:selected").val();
		 var gradeTxt = $("#grade").find("option:selected").text();
		 var course = $("#course").find("option:selected").val();
		 var courseTxt = $("#course").find("option:selected").text();
		 var examNumberOrStuCode = $("#examNumberOrStuCode").val();
		 var classOptions = $("#class").find("option:selected");
		 
		 var gradeOptions = $("#grade").find("option:selected");
			var courseOptions = $("#course").find("option:selected");
			 if(gradeOptions.length === 0){
				 window.Msg.alert("请选择年级");
				 return;
			 }
			 if(classOptions.length === 0){
				 window.Msg.alert("请选择班级");
				 return;
			 }
			 if(courseOptions.val() == ""){
				 window.Msg.alert("请选择科目");
				 return;
			 }
		/* var classArr = [];
		 if(classOptions.length > 0) {
			 $.each(classOptions,function(index,item){
				 var strArr = $(item).val().split(",");
				 classArr.push(strArr);
			 }); 
		 }*/
		
		var classArr = [];
		if(classOptions.length > 0){
			$.each(classOptions,function(index,item){
				var str = $(item).val();
				classArr.push(str);
			});
		}
		if(grade == ""){
			 scoreHtml = schoolYearTxt + "学年" + termTxt + examTypeTxt + "测试成绩列表";  
		 } 
		 else if(grade != ""){
			 
			 if(course == ""){
				 scoreHtml = schoolYearTxt + "学年" + gradeTxt + termTxt + examTypeTxt + "测试成绩列表";  
			 } else {
				 scoreHtml = schoolYearTxt + "学年" + gradeTxt + termTxt + examTypeTxt + courseTxt + "测试成绩列表";  
			 }
		 }
	var data={
			schoolYear:schoolYear,
			term:term,
			examType:examType,
			course:course,
			grade:grade,
			classArr:classArr,
			examNumberOrStuCode:examNumberOrStuCode,
			scoreHtml:scoreHtml,
			idx:idx,
			ci:ci,
			so:so
	}

	 var url = "../statisticsAnalysis/scoreSearch.do?command=exportExcel&data="+JSON.stringify(data);
	 var form = $( "#ExportQueryData" ) ;
		form.attr( "action", url) ;
		form.get( 0 ).submit() ; 
  }

	
</script>


</head>
<body style="overflow: auto;">
	<form id="ExportQueryData" method="post"></form>
	<div class="page-list-panel"
		style="overflow-y: auto; overflow-x: hidden;">
		<div class="head-panel">
			<!-- 多条件 -->
			<div id="importCondition" style="margin: -5px 10px 10px 0px;">
			
				<div class="search-panel" style="display: block;">
                   <div style="float: left;padding-right: 5px;"> 
                      	学年 <select id="schoolYear" name="schoolYear" class="form-control"
							data-dic="{code:'ND'}" style="width: 125px;"></select>
							<!--  &nbsp; 学校类型
						<select id="schoolType" name="schoolType" class="form-control"
							style="width: 150px;" data-dic="{code:'xxlx'}">
							<option value="">选学校类型</option>
						</select>-->
						&nbsp;年级 <select id="grade" name="grade" class="form-control"
							style="width: 100px;">
							<option value="">选择年级</option>
						</select>
                    </div>
                </div>
                
                <div class="multiselect" style="margin: -5px 10px 10px 0px;">
					<p>
						班级 <select id="class" name="class" class="form-control" multiple="multiple" style="height: 25px;">
								
							</select>
					</p>
				</div>
				
				<div class="search-panel" style="display: block;">
               		科目
                    <select id="course" name="course" class="form-control" style="width: 125px;">
                        <option value="">选择科目</option>
                    </select>
                                                               学期
                    <select id="term" name="term" class="form-control" style="width: 100px;" data-dic="{code:'xq'}">
                         <!--  <option value="">选择学期</option>-->
                    </select> 测试类型
                    <select id="examType" name="examType" class="form-control" style="width: 135px;" data-dic="{code:'kslx'}">
                        <!--  <option value="">选测试类型</option>-->
                    </select>
                    
                    <input type="text" id="examNumberOrStuCode" name="examNumberOrStuCode" placeholder="输入考号或学籍号" style="margin-left:5px;"/>
                    <span class="toolbar">
						<button id="fastSearch_history" title="查询" style="margin-left: 0px;">
							<i class="fa fa-search" style="margin-right: 0px;"></i>查询
						</button>
						<button id="deriveStuInfo" onclick="exportExcel()">
							<i class="fa fa-paperclip"></i>导出
						</button>
						
					</span>
					<span class="toolbar" style="display: none;" >
							<button id="print" title="打印预览" style="margin-left: 0px;" onClick="getPrint();">
								<i class="fa fa-print"></i>打印预览
							</button>
						</span>
                </div>
			
			</div>
		</div>
		<table id="list2"></table>
		<div id="pager2"></div>
		<!--  <div class="l_statis" style="display: none;">
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
		</div>-->
	</div>
</body>
</html>