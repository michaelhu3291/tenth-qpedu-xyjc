<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<title>测试管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv=“X-UA-Compatible” content=“IE=8″>
<link href="../theme/default/ui.custom.css" rel="stylesheet" />
<link href="../theme/default/ui.jqgrid.css" rel="stylesheet" />
<link href="../theme/default/font.awesome.css" rel="stylesheet" />
<link href="../theme/default/ui.chosen.css" rel="stylesheet" />
<link href="../theme/default/ui.uploadfile.css" rel="stylesheet" />
<link href="../theme/default/ui.pick.css" rel="stylesheet" />
<link href="../theme/default/page.common.css" rel="stylesheet" />
<link href="../theme/default/jquery.multiselect.css" rel="stylesheet" />
<link href="../theme/default/jquery.multiselect.filter.css" rel="stylesheet" />
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
<script type="text/javascript" src="../js/datePicker/WdatePicker.js"></script>


<style>
#add_exam_info ul li {
	list-style: none;
	margin-top: 30px;
}
.arrangement{    
   text-align: center;
    font-size: 14px;}
    
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
</style>
<script type="text/javascript">

	var listId = "#list2", 
	editorFormId = "#editorForm", 
	pagerId = "#pager2", 
	loadUrl = "../examInfo/examManage.do?command=selectExamById",
    saveUrl ,  
    userRole,
	exam_time_hide="",
	schoolYear_hide="",
	grade_hide="" ,
	gradeTxt_hide="",
	term_hide="",
	examType_hide="",
	closingTime_hide="",
	introducedTime_hide="",
	introducedState_hide="",
	files_hide=[],
	course_hide=[],
	classId_hide=[],
    listUrl = "../examInfo/examManage.do?command=searchPaging", 
	schoolSelectUrl = "../examInfo/examManage.do?command=selectSchoolByExamCode",
	getUserRoleUrl="../examInfo/examManage.do?command=getUserRole",
	classAndCourseSelectUrl = "../examInfo/examManageSchool.do?command=getClassIdAndCourse",
	showArrangementByExamUrl="../examInfo/markingArrangement_xjkw.do?command=showArrangementByExam",
	getCourseByGradeUrl="../authority/dataDictionary.do?command=getCoursesByCode",
	showSelectArrangementByExamUrl="../examInfo/markingArrangement_xjkw.do?command=showSelectArrangementByExam";
	
	$(function() {
		
	
		//得到用户的角色
		$.ajax({
			url : getUserRoleUrl,
			type : "POST",
			data : {},
			dataType : "JSON",
			async: false, 
			success : function(data) {
				  $("#userRole").val(data);
			}
		});
		var usreRole=$("#userRole").val();
		  if( $("#userRole").val()=="qpAdmin"){
			$(".schoolAdmin").hide();
			saveUrl="../examInfo/examManage.do?command=addExam";
		} else{
			$(".qpAdmin").hide();
			$(".schoolAdmin").show();
			saveUrl="../examInfo/examManageSchool.do?command=addSchoolExam";
		}  
		 //根据年级加载学校
		var loadSchool = function() {
			$("#school option[value != '']").remove();
			var grade = $("#grade").val();
			if (grade != "") {
				var url = "../statisticsAnalysis/scoreSearch.do?command=getSchoolsByGrade";
				var data = {
					grade : grade
				};
				$.ajax({
					url : url,
					type : "POST",
					data : data,
					dataType : "JSON",
					success : function(data) {
						var Brevity_Code;
						for (var i = 0; i < data.length; i++) {
							if(data[i].Brevity_Code==undefined){
								Brevity_Code="";
							}else{
							   Brevity_Code=data[i].Brevity_Code;
							}
							$("#school").append("<option value='"+data[i].School_Code+"'  brevityCode='"+Brevity_Code+"'>"+ data[i].School_Short_Name+ "</option>");
						}
						$("#school").multiselect('refresh');
					}
				});
			} else {
				$("#school").multiselect('refresh');
			}
		}
		//根据年级加载班级
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
							$("#classId").append("<option value='"+data[i].Class_No+"'>"+ _typesClass[data[i].Class_No]+ "</option>");
						}
						if ($("#classId").find("option").length > 0) {
							$("#classId").multiselect({
								checkAllText : "全选",
								uncheckAllText : "全不选",
								noneSelectedText : "选择班级",
								selectedText : '#' + " 个班级",
								selectedList : 2,
								// minWidth:'200',
							});
							$("#classId").multiselect('refresh');
						}
					}
				});
			} else {
				$("#classId").multiselect('refresh');
			}
		}
		//根据学校类型加载科目
		var loadCourse=function(){
			var grade=$("#grade").val();
			var courseChange=1;
			loadCourseByGrade(examCode,courseChange);
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
			$("#course option[value != '']").remove();
			$.ajax({
	            url: getCourseByGradeUrl,
	            type: "POST",
	            data: {schoolType:schoolType},
	            dataType: "JSON",
	            success: function (data) {
	                for (var i = 0; i < data.length; i++) {
	                    $("#course").append("<option value='" + data[i].DictionaryCode + "' sortNumber='"+data[i].SortNumber+"'>" + data[i].DictionaryName + "</option>");
	                    if(grade!="19"){
	                		 $("#course option[value='lhhj']").remove();
	                	}
	                }
	            	if ($("#course").find("option").length > 0) {
						$("#course").multiselect({
							checkAllText : "全选",
							uncheckAllText : "全不选",
							noneSelectedText : "选择科目",
							selectedText : '#' + " 科",
							selectedList : 2,
							 //minWidth:'200',
						});
						$("#course").multiselect('refresh');
					}else{
						$("#course").multiselect('refresh');
					}
				}
			});
		}
         var schoolStr="<tr id=couresName>"+
			"<th style='width:3%;''>序号</th>"+
			"<th style='font-size: 14px; text-align: center;  width:10%;'>科目</th>"+
			"<th style='font-size: 14px; text-align: center; width:6%'>测试时长<br/>(分钟)</th>"+
			"<th style='font-size: 14px; text-align: center; width:5%;'>测试形式</th>"+
			"<th style='font-size: 14px; text-align: center; width:5%;'>总分</th>"+
			"<th style='font-size: 14px; text-align: center; width:5%'>测试日期</th>"+
			"<th style='font-size: 14px; text-align: center; width:8%'>测试时间</th>"+
			"<th style='font-size: 14px; text-align: center; width:5%'> 阅卷日期</th>"+
			"<th style='font-size: 14px; text-align: center; width:8%'> 阅卷时间</th>"+
			"<th style='font-size: 14px; text-align: center; width:13%'> 安排阅卷人<br/>截止日期</th>"+
			"<th style='font-size: 14px; text-align: center; width:15%'> 阅卷地点</th>"+
			"</tr>";
			var adminStr="<tr id=couresName>"+
			"<th style='width:3%;''>序号</th>"+
			"<th style='font-size: 14px; text-align: center;  width:13%'>科目</th>"+
			"<th style='font-size: 14px; text-align: center; width:3%;'>是否测试</th>"+
			"<th style='font-size: 14px; text-align: center; width:7%;'>测试时长<br/>(分钟)</th>"+
			"<th style='font-size: 14px; text-align: center; width:5%;'>测试形式</th>"+
			"<th style='font-size: 14px; text-align: center; width:5%;'>总分</th>"+
			"<th style='font-size: 14px; text-align: center; width:5%'>测试日期</th>"+
			"<th style='font-size: 14px; text-align: center; width:8%'>测试时间</th>"+
			"<th style='font-size: 14px; text-align: center; width:5%'> 阅卷日期</th>"+
			"<th style='font-size: 14px; text-align: center; width:8%'> 阅卷时间</th>"+
			"<th style='font-size: 14px; text-align: center; width:13%'> 安排阅卷人<br/>截止日期</th>"+
			"<th style='font-size: 14px; text-align: center; width:15%'> 阅卷地点</th>"+
			"</tr>";
			var numberFun=function(){
				 var  numberStr=[]; 
				 $("#editor_course #editorCourse .courseExam").each(function(){
							 str=$(this).attr("id").substr(0, $(this).attr("id").indexOf('_'));
							 numberStr.push(str);
					 });
				 for(var i=0;i<numberStr.length;i++){
					 $("#"+numberStr[i]+"_number").text("");
					 $("#"+numberStr[i]+"_number").text((i+1));
				 }
				 $('#editorCourse tbody tr:odd').css('background','#f3f3f3'); 
			}
		//根据年级加载科目
		var loadCourseByGrade=function(examCode,courseChange){
			var grade=$("#grade").val();
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
			if($("#userRole").val() == "qpAdmin"){
				$("#editorCourse tbody").html("");
				if($("#grade").val()!=""){
					 $.ajax({
							url : getCourseByGradeUrl,
							type : "POST",
							data : {schoolType:schoolType},
							dataType : "JSON",
							success : function(data) {
									$("#editorCourse").append(adminStr);
								    for(var i=0;i<data.length;i++){
									$("#editorCourse").append(
	                                 	   "<tr class='courseExam' id='"+data[i].DictionaryCode+"_courseExam'>"+
	                                 	   "<td style='text-align:center; '>"+(i+1)+"</td>"+
	                                 	   "<td style='text-align:left;' >"+data[i].DictionaryName+
	                                 	   "<input type='hidden' value='"+data[i].DictionaryCode+"' id='"+data[i].DictionaryCode+"_course'/>"+
	                                 	   "<input type='hidden' value='"+data[i].SortNumber+"' id='"+data[i].DictionaryCode+"_sortNumber'/></td>"+
	                                 	   "<td style='text-align:center;' ><input id='"+data[i].DictionaryCode+"_examState' class='examState' type='checkbox' value='1' /></td>"+
	                                 	   "<td  style='text-align:center;'><input type='text' id='"+data[i].DictionaryCode+"_examLength' style='width:50px; height:14px;' class='form-control examLength' /> </td>"+
	                                 	  "<td  style='text-align:center;'><input type='text' id='"+data[i].DictionaryCode+"_examType' style='width:50px; height:14px;' text-align='center' value='闭卷'  class='form-control'/> </td>"+
	                                 	  "<td  style='text-align:center;'><select  id='"+data[i].DictionaryCode+"_zf' style='width:55px;' class='form-control'><option value='8BDFCD91-2AF1-4A88-975F-342EEC8D5880'>100</option><option value='33818257-BB25-4963-AF5C-2ABA75E55F50'>120</option><option value='75454002-07EA-42F5-9355-6828F66B146E'>150</option></select></td>"+
	                                 	   "<td style='text-align:center;' >"+"<input type='text'   id='"+data[i].DictionaryCode+"_startTime'  class='form-control examStartTime' placeholder='请输入测试日期'  readonly='readonly'  style='width: 100px;' /> </td>"+
	                                 	   "<td style='text-align:center;'><input type='text' id='"+data[i].DictionaryCode+"_startTimeType' class='form-control startTimeType' placeholder='开始时间'  readonly='readonly'  onfocus='startTime()' style='width:60px;height:14px;background-color: #CAF1BD; vertical-align: middle;  text-align: center;'/>"+
	                                 	   " &nbsp;<input type='text' id='"+data[i].DictionaryCode+"_stopTimeType'    placeholder='结束时间' class='form-control stopTimeType' readonly='readonly'  style='width:60px;height:14px; background-color:#e7fcd9; vertical-align: middle;  text-align: center;'/></td>"+	                                 	   
	                                 	   "<td  style='text-align:center;'><input type='text'   id='"+data[i].DictionaryCode+"_markingTime'   class='form-control markingTime' placeholder='请输入阅卷日期' readonly='readonly'  style='width: 100px;' /></td>"+
	                                 	   "<td style='text-align:center;'><input type='text' id='"+data[i].DictionaryCode+"_markingArrangementStartTime' class='form-control markingArrangementStartTime' placeholder='开始时间' readonly='readonly'  onfocus='markingStartTime()' style='width:60px;height:14px;background-color: #CAF1BD; vertical-align: middle;  text-align: center;'/>"+
	                                 	   " &nbsp;<input type='text' id='"+data[i].DictionaryCode+"_markingArrangementEndTime' readonly='readonly'    placeholder='结束时间'   AutoPostBack='true' onfocus='markingEndTime()'  class='form-control markingArrangementEndTime'  style='width:60px;height:14px; background-color:#CAF1BD; vertical-align: middle;  text-align: center;'/></td>"+	
	                                 	  "<td  style='text-align:center;'><input type='text'   id='"+data[i].DictionaryCode+"_markingArrangementEndDate'  readonly='readonly'  class='form-control markingArrangementStartDate' placeholder='截止日期' style='width: 100px;' /></td>"+
	                                 	   "<td  style='text-align:center;'><input type='text' id='"+data[i].DictionaryCode+"_markingPlace' style='width:200px;' class='form-control markingPlace'/></td>"+
	                                 	  "</tr>");
									//初始化日期插件
									$(".examStartTime,.markingTime,.markingArrangementStartDate").datepicker($.extend({
						                dateFormat: "yy-mm-dd",
						                changeMonth: true,
						                changeYear: true
						            }));
									//加载数据字典
									loadTimeDictionary();	
								}
								    $('#editorCourse tbody tr:odd').css('background','#f3f3f3'); 
								if(grade!="19"){
				                		 $("#lhhj_courseExam").remove();
				                 }
								//修改时得到相应的值
								if(examCode!=undefined){
									$.ajax({
		                              	   url:showSelectArrangementByExamUrl,
		                              	   type : "POST",
													data : {examCode : examCode},
													dataType : "JSON",
													success : function(data) {
														for(var i=0;i<data.length;i++){
															var course=data[i].course;
														   $("#"+course+"_examState").attr("checked",true);
														   $("#"+course+"_startTime,#"+course+"_startTimeType,#"+course+
																   "_stopTimeType,#"+course+"_examLength,#"+course+"_markingPlace,#"+course+
																   "_markingArrangementEndDate,#"+course+"_markingArrangementStartTime,#"
																   +course+"_markingArrangementEndTime,#"+course+"_examType,#"
																   +course+"_markingTime").attr("disabled", false);
														   $("#"+course+"_startTime").val(data[i].Course_Exam_Time);
														   $("#"+course+"_startTimeType").val(data[i].Course_Start_Time);
														   $("#"+course+"_stopTimeType").val(data[i].Course_End_Time);
														   $("#"+course+"_examLength").val(data[i].Exam_Length);
														   $("#"+course+"_markingTime").val(data[i].Marking_Time);
														   $("#"+course+"_markingPlace").val(data[i].Marking_Place);
														   $("#"+course+"_sortNumber").val(data[i].SortNumber);
														   $("#"+course+"_markingArrangementStartTime").val(data[i].Marking_Start_Time);
														   $("#"+course+"_markingArrangementEndTime").val(data[i].Marking_End_Time);
														   $("#"+course+"_markingArrangementEndDate").val(data[i].Marking_End_Date);
														   $("#"+course+"_examType").val(data[i].Course_Exam_Type);								
														   $("#"+course+"_startTime,#"+course+"_startTimeType,#"+course+"_markingTime,#"+
																   course+"_markingPlace,#"+course+"_examLength,#"+course+"_examType ,#"+
																  course+"_markingArrangementStartTime,#"+course+"_markingArrangementEndTime,#"+
																  course+"_markingArrangementEndDate"
																   ).css("background","#CAF1BD");
													}
											 }
		                                 });
								}
								$('#editorCourse tbody tr:odd').css('background','#f3f3f3'); 
								 examTime();
								 $("#editorCourse").find("input[type='text'],select").attr("disabled", "disabled");
								 $("#editorCourse").find("select,input").css("background","#e7fcd9");
							}
					    });
				}
			}else{
				if($("#course option").length==$("#course option:selected").length && $("#course option:selected").length>0 ){
					$("#editor_course #editorCourse tbody").html("");
					$("#editorCourse").append(schoolStr);
					var selectCourse=[];
					var val = $("#course").find("option:selected").each(function() {
						var params={};
						params["course"]=$(this).val()
						params["sortNumber"] = $(this).attr("sortNumber");
						params["courseName"] = $(this).text();
						selectCourse.push(params);
			       }); 
					for(var i=0;i<selectCourse.length;i++){
						sortNumber=selectCourse[i].course.sortNumber;
						$("#editorCourse").append(
                      	   "<tr class='courseExam' id='"+selectCourse[i].course+"_courseExam'><td style='text-align:center; ' id='"+selectCourse[i].course+"_number'>"+(i+1)+"</td>"+
                      	   "<td style='text-align:left;' >"+_types[selectCourse[i].course]+"<input type='hidden' value='"+selectCourse[i].course+"' id='"+selectCourse[i].course+"_course'/>"+
                      	   "<input type='hidden' value='"+sortNumber+"' id='"+selectCourse[i].course+"_sortNumber'/></td>"+
                      	   "<td  style='text-align:center; '><input type='text' id='"+selectCourse[i].course+"_examLength' style='width:50px;  text-align: center;' class='form-control examLength' /> </td>"+
                      	   "<td  style='text-align:center;'><input type='text' id='"+selectCourse[i].course+"_examType' style='width:50px; height:14px;' text-align='center' value='闭卷'  class='form-control'/></td>"+
                      	   "<td  style='text-align:center;'><select  id='"+selectCourse[i].course+"_zf' style='width:55px;' class='form-control'><option value='8BDFCD91-2AF1-4A88-975F-342EEC8D5880'>100</option><option value='33818257-BB25-4963-AF5C-2ABA75E55F50'>120</option><option value='75454002-07EA-42F5-9355-6828F66B146E'>150</option></select></td>"+
                      	   "<td style='text-align:center; ' ><input type='text'   readonly='readonly'  id='"+selectCourse[i].course+"_startTime'  class='form-control examStartTime' placeholder='请输入测试日期' style='width: 100px;' /></td>"+
                      	   "<td style='text-align:center; ' ><input type='text' id='"+selectCourse[i].course+"_startTimeType' class='form-control startTimeType' placeholder='开始时间'  readonly='readonly'  onfocus='startTime()' style='width:60px;height:14px; background-color:#CAF1BD; vertical-align: middle;  text-align: center;'/>"+
                    	   " &nbsp;<input type='text' id='"+selectCourse[i].course+"_stopTimeType'  readonly='readonly'   placeholder='结束时间' class='form-control stopTimeType'  style='width:60px;height:14px; background-color:#e7fcd9; vertical-align: middle;  text-align: center;'/></td>"+
                  	       "<td  style='text-align:center;'><input type='text'   id='"+selectCourse[i].course+"_markingTime' readonly='readonly'   class='form-control markingTime' placeholder='请输入阅卷日期' style='width: 100px;' /></td>"+
                  	       "<td style='text-align:center;'><input type='text' id='"+selectCourse[i].course+"_markingArrangementStartTime' readonly='readonly'  class='form-control markingArrangementStartTime' placeholder='开始时间' onfocus='markingStartTime()' style='width:60px;height:14px;background-color: #CAF1BD; vertical-align: middle;  text-align: center;'/>"+
                  	       " &nbsp;<input type='text' id='"+selectCourse[i].course+"_markingArrangementEndTime'    placeholder='结束时间' readonly='readonly'  onfocus='markingEndTime()'  class='form-control markingArrangementEndTime'  style='width:60px;height:14px; background-color:#CAF1BD; vertical-align: middle;  text-align: center;'/></td>"+	
                  	       "<td  style='text-align:center;'><input type='text'   id='"+selectCourse[i].course+"_markingArrangementEndDate' readonly='readonly'    class='form-control markingArrangementStartDate' placeholder='截止日期' style='width: 100px;' /></td>"+
                  	       "<td  style='text-align:center;'><input type='text' id='"+selectCourse[i].course+"_markingPlace' style='width:200px;' class='form-control markingPlace'/></td>"+
                      	   "</tr>");
						//初始化日期插件
						$(".examStartTime,.markingTime,.markingArrangementStartDate").datepicker($.extend({
			                dateFormat: "yy-mm-dd",
			                changeMonth: true,
			                changeYear: true
			            }));
						$("#"+selectCourse[i].course+"_startTime").val($("#exam_time").val());
						if($("#exam_time").val()!=""){
							$("#"+selectCourse[i].course+"_markingArrangementEndDate").val(loadStrTmorrowday($("#exam_time").val()));
						}else{
							$("#"+selectCourse[i].course+"_markingArrangementEndDate").val($("#exam_time").val());
						}
						//加载数据字典
						loadTimeDictionary();	
					}
					numberFun();
					$('#showArrangement tbody tr:odd').css('background','#f3f3f3');
				}else{
					 var examCode=$("#examCode").val();
						//修改时得到相应的值
							if(courseChange==1){
								var course=[];
								var shortNumbr=[];
								var existCourse=[];
								var selectCourse=[];
								var tblExistCourse=[];
								//得到option选中的值
								var val = $("#course").find("option:selected").each(function() {
										course.push($(this).val());
							}); 
								//得到table列表里面的值
								var number=[];
						    $("#editor_course #editorCourse .courseExam").each(function(){
								 str=$(this).attr("id").substr(0, $(this).attr("id").indexOf('_'));
								  existCourse.push(str);
								  number.push($("#"+str+"_number").text());
							 });
							 selectCourse = array_difference(course, existCourse);	
							 tblExistCourse = array_difference(existCourse, course);
					        if(tblExistCourse.length>0){
								 for(var i=0;i<tblExistCourse.length;i++){
									 $("#editor_course #editorCourse #"+tblExistCourse[i]+"_courseExam").remove();
								 }
							 } 
					    	var j=Math.max.apply(null, number);
					 		if(j<0){
					 			j=1;
					 		}else{
					 			j=j+1;
					 		}
						 	if(selectCourse.length>0){
									for(var i=0;i<selectCourse.length;i++){
										sortNumber=$("#course option[value='"+selectCourse[i]+"']").attr("sortnumber");
										$("#editorCourse").append(
				                      	   "<tr class='courseExam' id='"+selectCourse[i]+"_courseExam'><td style='text-align:center; ' id='"+selectCourse[i]+"_number'>"+j+"</td>"+
				                      	   "<td style='text-align:left;' >"+_types[selectCourse[i]]+"<input type='hidden' value='"+selectCourse[i]+"' id='"+selectCourse[i]+"_course'/>"+
				                      	   "<input type='hidden' value='"+sortNumber+"' id='"+selectCourse[i]+"_sortNumber'/></td>"+
				                      	   "<td  style='text-align:center; '><input type='text' id='"+selectCourse[i]+"_examLength' style='width:50px;  text-align: center;' class='form-control examLength' /> </td>"+
				                      	   "<td  style='text-align:center;'><input type='text' id='"+selectCourse[i]+"_examType' style='width:50px; height:14px;' text-align='center' value='闭卷'  class='form-control'/></td>"+
				                      	 "<td  style='text-align:center;'><select  id='"+selectCourse[i]+"_zf' style='width:55px;' class='form-control'><option value='8BDFCD91-2AF1-4A88-975F-342EEC8D5880'>100</option><option value='33818257-BB25-4963-AF5C-2ABA75E55F50'>120</option><option value='75454002-07EA-42F5-9355-6828F66B146E'>150</option></select></td>"+
				                      	   "<td style='text-align:center; ' ><input type='text'   id='"+selectCourse[i]+"_startTime'  class='form-control examStartTime' readonly='readonly'  placeholder='请输入测试日期' style='width: 100px;' /></td>"+
				                      	   "<td style='text-align:center; ' ><input type='text' id='"+selectCourse[i]+"_startTimeType' class='form-control startTimeType' placeholder='开始时间' readonly='readonly'   onfocus='startTime()' style='width:60px;height:14px; background-color:#CAF1BD; vertical-align: middle;  text-align: center;'/>"+
				                    	   " &nbsp;<input type='text' id='"+selectCourse[i]+"_stopTimeType'    placeholder='结束时间' readonly='readonly'  class='form-control stopTimeType'  style='width:60px;height:14px; background-color:#e7fcd9; vertical-align: middle;  text-align: center;'/></td>"+
		                          	       "<td  style='text-align:center;'><input type='text'   id='"+selectCourse[i]+"_markingTime' readonly='readonly'   class='form-control markingTime' placeholder='请输入阅卷日期' style='width: 100px;' /></td>"+
		                          	       "<td style='text-align:center;'><input type='text' id='"+selectCourse[i]+"_markingArrangementStartTime' readonly='readonly'  class='form-control markingArrangementStartTime' placeholder='开始时间' onfocus='markingStartTime()' style='width:60px;height:14px;background-color: #CAF1BD; vertical-align: middle;  text-align: center;'/>"+
		                          	       " &nbsp;<input type='text' id='"+selectCourse[i]+"_markingArrangementEndTime'    placeholder='结束时间' readonly='readonly'  onfocus='markingEndTime()'  class='form-control markingArrangementEndTime'  style='width:60px;height:14px; background-color:#CAF1BD; vertical-align: middle;  text-align: center;'/></td>"+	
		                          	       "<td  style='text-align:center;'><input type='text'   id='"+selectCourse[i]+"_markingArrangementEndDate'  readonly='readonly'  class='form-control markingArrangementStartDate' placeholder='截止日期' style='width: 100px;' /></td>"+
		                          	       "<td  style='text-align:center;'><input type='text' id='"+selectCourse[i]+"_markingPlace' style='width:200px;' class='form-control markingPlace'/></td>"+
				                      	   "</tr>");
										//初始化日期插件
										$(".examStartTime,.markingTime,.markingArrangementStartDate").datepicker($.extend({
							                dateFormat: "yy-mm-dd",
							                changeMonth: true,
							                changeYear: true
							            }));
										$("#"+selectCourse[i]+"_startTime").val($("#exam_time").val());
										if($("#exam_time").val()!=""){
											$("#"+selectCourse[i]+"_markingArrangementEndDate").val(loadStrTmorrowday($("#exam_time").val()));
										}else{
											$("#"+selectCourse[i]+"_markingArrangementEndDate").val($("#exam_time").val());
										}
										//加载数据字典
										loadTimeDictionary();	
									}
									
								}  
						 	  numberFun();
						 	 $('#editorCourse tbody tr:odd').css('background','#f3f3f3'); 
							 if(grade!="19"){
					             $("#lhhj_courseExam").remove();
					           }
								//点击修改
							}else{
								var course = [];
								var val = $("#course").find("option:selected").each(function() {
									var params = {};
									params["course"] = $(this).val();
									params["sortNumber"] = $(this).attr("sortNumber");
									params["courseName"] = $(this).text();
									course.push(params);
								}); 			
							   $("#editorCourse").append(schoolStr);
								for(var i=0;i<course.length;i++){
									$("#editorCourse").append(
											   "<tr class='courseExam' id='"+course[i].course+"_courseExam'><td style='text-align:center; ' id='"+course[i].course+"_number'>"+(i+1)+"</td>"+
					                      	   "<td style='text-align:left;' >"+course[i].courseName+"<input type='hidden' value='"+course[i].course+"' id='"+course[i].course+"_course'/>"+
					                      	   "<input type='hidden' value='"+course[i].sortNumber+"' id='"+course[i].course+"_sortNumber'/></td>"+
					                      	   "<td  style='text-align:center; '><input type='text' id='"+course[i].course+"_examLength' style='width:50px;  text-align: center;' class='form-control examLength' /> </td>"+
					                      	   "<td  style='text-align:center;'><input type='text' id='"+course[i].course+"_examType' style='width:50px; height:14px;' text-align='center' value='闭卷'  class='form-control'/></td>"+
					                      	   "<td  style='text-align:center;'><select  id='"+course[i].course+"_zf' style='width:55px;' class='form-control'><option value='8BDFCD91-2AF1-4A88-975F-342EEC8D5880'>100</option><option value='33818257-BB25-4963-AF5C-2ABA75E55F50'>120</option><option value='75454002-07EA-42F5-9355-6828F66B146E'>150</option></select></td>"+
					                      	   "<td style='text-align:center; ' ><input type='text'   id='"+course[i].course+"_startTime' readonly='readonly'   class='form-control examStartTime' placeholder='请输入测试日期' style='width: 100px;' /></td>"+
					                      	   "<td style='text-align:center; ' ><input type='text' id='"+course[i].course+"_startTimeType' class='form-control startTimeType' readonly='readonly'  placeholder='开始时间'   onfocus='startTime()' style='width:60px;height:14px; background-color:#CAF1BD; vertical-align: middle;  text-align: center;'/>"+
					                    	   " &nbsp;<input type='text' id='"+course[i].course+"_stopTimeType'    placeholder='结束时间' class='form-control stopTimeType' readonly='readonly'  style='width:60px;height:14px; background-color:#e7fcd9; vertical-align: middle;  text-align: center;'/></td>"+
		                                 	   "<td  style='text-align:center;'><input type='text'   id='"+course[i].course+"_markingTime' readonly='readonly'   class='form-control markingTime' placeholder='请输入阅卷日期' style='width: 100px;' /></td>"+
		                                 	   "<td style='text-align:center;'><input type='text' id='"+course[i].course+"_markingArrangementStartTime' readonly='readonly'  class='form-control markingArrangementStartTime' placeholder='开始时间' onfocus='markingStartTime()' style='width:60px;height:14px;background-color: #CAF1BD; vertical-align: middle;  text-align: center;'/>"+
		                                 	   " &nbsp;<input type='text' id='"+course[i].course+"_markingArrangementEndTime'    placeholder='结束时间' readonly='readonly'  onfocus='markingEndTime()'  class='form-control markingArrangementEndTime'  style='width:60px;height:14px; background-color:#CAF1BD; vertical-align: middle;  text-align: center;'/></td>"+	
		                                 	   "<td  style='text-align:center;'><input type='text'   id='"+course[i].course+"_markingArrangementEndDate'  readonly='readonly'   class='form-control markingArrangementStartDate' placeholder='截止日期' style='width: 100px;' /></td>"+
		                                 	   "<td  style='text-align:center;'><input type='text' id='"+course[i].course+"_markingPlace' style='width:200px;' class='form-control markingPlace'/></td>"+
					                      	   "</tr>");
									//初始化日期插件
									$(".examStartTime,.markingTime,.markingArrangementStartDate").datepicker($.extend({
						                dateFormat: "yy-mm-dd",
						                changeMonth: true,
						                changeYear: true
						            }));
									$("#"+course[i].course+"_startTime").val($("#exam_time").val());
									if($("#exam_time").val()!=""){
										$("#"+course[i].course+"_markingArrangementEndDate").val(loadStrTmorrowday($("#exam_time").val()));
									}else{
										$("#"+course[i].course+"_markingArrangementEndDate").val($("#exam_time").val());
									}
									//加载数据字典
									loadTimeDictionary();	
								}
								$.ajax({
	                              	   url:showSelectArrangementByExamUrl,
	                              	   type : "POST",
												data : {examCode : examCode},
												dataType : "JSON",
												success : function(data) {
													for(var i=0;i<data.length;i++){
														var course=data[i].course
													   $("#"+course+"_startTime").val(data[i].Course_Exam_Time);
													   $("#"+course+"_startTimeType").val(data[i].Course_Start_Time);
													   $("#"+course+"_stopTimeType").val(data[i].Course_End_Time);
													   $("#"+course+"_examLength").val(data[i].Exam_Length);
													   $("#"+course+"_markingTime").val(data[i].Marking_Time);
													   $("#"+course+"_markingPlace").val(data[i].Marking_Place);
													   $("#"+course+"_sortNumber").val(data[i].SortNumber);
													   $("#"+course+"_markingArrangementStartTime").val(data[i].Marking_Start_Time);
													   $("#"+course+"_markingArrangementEndTime").val(data[i].Marking_End_Time);
													   $("#"+course+"_markingArrangementEndDate").val(data[i].Marking_End_Date);
													   $("#"+course+"_examType").val(data[i].Course_Exam_Type);								
													   $("#"+course+"_startTime,#"+course+"_startTimeType,#"+course+"_markingTime,#"+
															   course+"_markingPlace,#"+course+"_examLength,#"+course+"_examType ,#"+
															  course+"_markingArrangementStartTime,#"+course+"_markingArrangementEndTime,#"+
															  course+"_markingArrangementEndDate"
															   ).css("background","#CAF1BD");
													}
										 }
	                                 });
							};
				}
				         $('#editorCourse tbody tr:odd').css('background','#f3f3f3'); 
						 examTime();
						 $(".stopTimeType").attr("disabled", "disabled");
				}
			}
		//改变年级加科目
		$("#grade").change(function() {
			if($("#userRole").val()=="qpAdmin"){
				loadCourseByGrade();
				loadSchool();
			}else{
				loadClass();
				loadCourse(); 
				var courseChange=1;
				$("#editor_course #editorCourse tbody").html("");
				loadCourseByGrade(examCode,courseChange);
			}
		});
		//改变科目
		$("#course").change(function(){
			var courseChange=1;
			var examCode;
			if($("#course").find("option:selected").length==0){
				$("#editor_course #editorCourse tbody").html("");
			}
			if($("#couresName").length==0 && $("#course").find("option:selected").length!=0){
				$("#editorCourse").append(schoolStr);
			}
			loadCourseByGrade(examCode,courseChange);
		});
		_initButtons({
			cancelBTN : function() {
				$("#school option[value != '']").remove();
				$("#classId option[value != '']").remove();
				$("#course option[value != '']").remove();
				$("#editorForm")[0].reset();
				$("#id").val("");
				$(".exam_time_validate,.schoolYear_validate,.grade_validate,.term_validate,"+
						   ".examType_validate,.closingTime_validate,.introducedTime_validate,"+
						   ".school_validate,.classId_validate,.course_validate").html("");
				hideSlidePanel(".page-editor-panel");
				$(listId).trigger("reloadGrid");
			},
			backBTN : function() {
				$("#school option[value != '']").remove();
				$("#classId option[value != '']").remove();
				$("#course option[value != '']").remove();
				 $("#filesList").empty();
				 $("#showArrangement tbody,#editorCourse tbody,#schools,#classIds,#courses").html("")
				 $("#showForm")[0].reset();
				hideSlidePanel(".page-show-panel");
			},
			editBTN : function(ev) {
				var $i = $(ev.currentTarget).find("i"), idAry = $(listId).jqGrid("getGridParam", "selarrrow");
				$("#filesList").empty();
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
				$("#editor_course #editorCourse tbody").html("");
				 $("#editor_course_hide").html("");
				 exam_time_hide,schoolYear_hide,grade_hide ,gradeTxt_hide,
				 term_hide,examType_hide,closingTime_hide,introducedTime_hide
				 ,introducedState_hide="",
				files_hide,course_hide,classId_hide=[];
				var rowData = $(listId).jqGrid("getRowData", idAry[0]);
				var datas = {
					examCode : rowData.examNumber
				}
				//修改之前的考试编号
				var oldExamCode = $("#examCode").val(rowData.examNumber);
				$("#school option[value != '']").remove();
				$("#classId option[value != '']").remove();
				$("#course option[value != '']").remove();
				$.ajax({
							url : loadUrl,
							type : "POST",
							data : {
								id : idAry[0]
							},
							dataType : "JSON",
							async : false,
							success : function(data) {
								exam_time_hide=data.exam_time.substring(0, 10);
								schoolYear_hide=data.school_Year;
								grade_hide=data.grade_Code ;
								
								term_hide=data.term;
								examType_hide=data.exam_Type;
								closingTime_hide= data.closing_Time;
								if (closingTime_hide != null) {
									closingTime_hide = closingTime_hide.substring(0, 10);
								}
								introducedTime_hide=data.introduced_Time;
								if (introducedTime_hide != null) {
									introducedTime_hide = introducedTime_hide.substring(0, 10);
								}
							
								var $piel = showSlidePanel(".page-editor-panel")
										.find("h4 i").removeClass();
								if ($i.length) {
									$piel.addClass($i.attr("class"));
								}
								$("#files_list").find("span").remove();
								$("#exam_time").val(data.exam_time.substring(0, 10));
								
								$("#id").val(data.id);
								$("#exam_name").val(data.exam_Name);
								$("#schoolYear").val(data.school_Year);
								$("#term").val(data.term);
								$("#examType").val(data.exam_Type);
								$("#grade").val(data.grade_Code);
								gradeTxt_hide= $("#grade").find("option:selected").text();
								var introducedTime = data.introduced_Time;
								if (introducedTime != null) {
									introducedTime = introducedTime.substring(0, 10);
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
									var fileObj = data.files[i];
									fileParams["id"] = fileObj.id;
									fileParams["path"] = fileObj.path;
									fileParams["fileName"] = fileObj.fileName;
									fileObject.push(fileParams);
									files_hide.push(fileParams);
								}
								$("#files").data().fileItems(fileObject);
								if ($("#userRole").val() == "qpAdmin") {
									$.ajax({
												url : schoolSelectUrl,
												type : "POST",
												data : datas,
												dataType : "JSON",
												success : function(data) {
													var schoolObject = data;
													var grade = $("#grade").val();
													if (grade != "") {
														//根据年级得到学校
														var url = "../statisticsAnalysis/scoreSearch.do?command=getSchoolsByGrade";
														$.ajax({
																	url : url,
																	type : "POST",
																	data : {
																		grade : grade
																	},
																	dataType : "JSON",
																	success : function(data) {
																	 for (var i = 0; i < data.length; i++) {
																		 if(data[i].Brevity_Code==undefined){
																				Brevity_Code="";
																			}else{
																			   Brevity_Code=data[i].Brevity_Code;
																			}
																			$("#school").append("<option value='"+data[i].School_Code+"'  brevityCode='"+Brevity_Code+"'>"+ data[i].School_Short_Name+ "</option>");
																		} 
																		for (var i = 0; i < schoolObject.length; i++) {
																			$("#school").find("option[value='"+ schoolObject[i].School_Code+ "']").attr("selected","selected");
																		}
																		 loadCourseByGrade(rowData.examNumber);
																		 $("#school").multiselect('refresh');
																	}
																});
													}
												
												}
											});
								} else {
									var grade = $("#grade") .val();
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
									$.ajax({
												url : classAndCourseSelectUrl,
												type : "POST",
												data : datas,
												dataType : "JSON",
												success : function(data) {
													var classObject = data;
													$.ajax({
																url :getCourseByGradeUrl,
																type : "POST",
																data : {schoolType:schoolType},
																dataType : "JSON",
																success : function(data) {
																	for (var i = 0; i < data.length; i++) {
																		$("#course").append("<option value='" + data[i].DictionaryCode + "' sortNumber='"+data[i].SortNumber+"'>"+ data[i].DictionaryName+ "</option>");
																	}
																	 if(grade!="19"){
												                		 $("#course option[value='lhhj']").remove();
												                 }
																	for (var i = 0; i < classObject.courselist.length; i++) {
																		var params = {};
																		params["course"] = classObject.courselist[i].Course;
																		params["courseName"] = _types[classObject.courselist[i].Course];
																		course_hide.push(params);
																		$("#course").find("option[value='"+ classObject.courselist[i].Course+ "']").attr("selected","selected");
																	}
																	 loadCourseByGrade(rowData.examNumber,courseChange=0);
																	$("#course").multiselect('refresh');
																}
															});
													if (grade != "") {
														//根据学校和年级得到班级
														var url = "../examInfo/examManageSchool.do?command=getClassBySchoolAndGrade";
														var data = { gradeId : grade };
														$.ajax({
																	url : url,
																	type : "POST",
																	data : data,
																	dataType : "JSON",
																	success : function(data) {
																		for (var i = 0; i < data.length; i++) {
																			$("#classId").append("<option value='"+data[i].Class_Id+"'>"+ _typesClass[data[i].Class_Id]+ "</option>");
																		}
																		for (var i = 0; i < classObject.list.length; i++) {
																			var classParams = {};
																			classParams["classId"] =classObject.list[i].Class_Id;
																			classParams["className"] = _typesClass[classObject.list[i].Class_Id];
																			classId_hide.push(classParams);
																			$("#classId").find("option[value='"+ classObject.list[i].Class_Id+ "']").attr("selected","selected");
																		}
																	  $("#classId").multiselect('refresh');
																	}
																});
													}
												}
											});
								}
							}
						});
			}
		}); //from page.common.js
		_initFormControls();   //from page.common.js
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
		}, {
			label : "发布状态",
			name : 'Introduced_State',
			sortable : false,
			width : 100,
			align : "center",
			formatter : function(ar1, ar2, ar3) {
				var str;
				if (ar3.Introduced_State == "0") {
					  str="<span style='font-weight:bold;'>未发布</span>";
				} else if (ar3.Introduced_State == "1") {
					  str=  "<span>已发布</span>"
				}
				return str;
			}
		},{
			label : "操作",
			sortable : false,
			width : 120,
			align : "center",
			formatter : function(ar1, ar2, ar3) {
				var operStr
				= "<button id='queryBTN' class='page-button' title='查看测试详情' onclick='queryExamOper(\""
					+ ar3.examNumber
					+ "\",\""
					+ ar3.Id
					+ "\",\""
					+ ar3.Exam_Name
					+ "\",\""
					+ ar3.ev
					+ "\");'><i class='fa fa-file-text' style='margin-right: 5px;'></i>查看测试详情</button>";
			return operStr;
			}
		}];
		$(listId).jqGrid($.extend(defaultGridOpts, {
			url : listUrl,
			colModel : _colModel,
			pager : pagerId,
			loadComplete: function(){//禁用复选框
                var rowIds = $(listId).jqGrid('getDataIDs');//获取jqgrid中所有数据行的id
                for(var i=0; i<rowIds.length; i++) {
                	var curRowData = $(listId).jqGrid('getRowData', rowIds[i]);//获取指定id所在行的所有数据.
                	if (curRowData!=null) {
                		var str="已发布";
                		var s=curRowData.Introduced_State.indexOf(str);
                    	if (parseInt(s)>0) {
                    		$("input[type='checkbox']")[i+1].disabled = true;//设置该行不能被选中。
                    		$("td")[i+1].disabled = true;;//设置该行不能被选中。
						}
					}
                }
            },
            onSelectRow:function(id){//选择某行时触发事件
                var curRowData = $(listId).jqGrid('getRowData', id);
                if (curRowData!=null) {
            		var str="已发布";
            		var s=curRowData.Introduced_State.indexOf(str);
            		if (parseInt(s)>0) {
                   		$(listId).jqGrid("setSelection", id,false);//设置该行不被选中。
					}
				}
            }
		}));
		var myGrid = $(listId);
	    $("#cb_"+myGrid[0].id).hide();
		resizeFun();
	
		
		loadDictionary(function() {
			var currentYear = loadSemesterYear();
	        //显示当前年
	    	$("#schoolYear").append('<option id="schoolYearOption" value="'+currentYear+'">'+currentYear+'</option>');
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
		$("#exam_time,#exam_name,#grade,#schoolYear,#term,#examType,#introducedTime,#closingTime,#school,#classId,#course").change(function() {
			if ($("#exam_time").val != '') {
				$(".exam_time_validate").html("");
			}
			if ($("#exam_name").val != '') {
				$(".exam_name_validate").html("");
			}
			if ($("#grade").val != '') {
				$(".grade_validate").html("");
			}
			if ($("#examType").val != '') {
				$(".examType_validate").html("");
			}
			if ($("#schoolYear").val != '') {
				$(".schoolYear_validate").html("");
			}
			if ($("#term").val != '') {
				$(".term_validate").html("");
			}
			if ($("#introducedTime").val != '') {
				$(".introducedTime_validate").html("");
			}
			if ($("#closingTime").val != '') {
				$(".closingTime_validate").html("");
			}
			if ($("#school option:selected").length >0) {
				$(".school_validate").html("");
			}
			if ($("#course option:selected").length >0) {
				$(".course_validate").html("");
			}
			if ($("#classId option:selected").length >0) {
				$(".classId_validate").html("");
			}
		});
		//添加页面显示
		$("#insertExam").click(function(ev) {
					$("#editorForm")[0].reset();
					$("#id").val("");
					$("#files_list").find("span").remove();
					$("#school option[value != '']").remove();
					$("#editorCourse tbody").html("");
					$("#examCode").val("");
					$("#fileListTD").find("#files_list").next().html("");
					 $(".page-editor-panel .title-bar h4 .fa").attr("class","fa fa-plus")
					var $i = $(ev.currentTarget).find("i"), $piel = $(".page-editor-panel").show({
						effect : "slide",
						direction : "up",
						easing : 'easeInOutExpo',
						duration : 900,
					});
				});
		//根据学校code加载年级
		$.ajax({
					url : "../statisticsAnalysis/schoolPlainAdminScoreSearch.do?command=getSchoolCodeAndSchoolName",
					type : "POST",
					data : {},
					dataType : "JSON",
					success : function(data) {
						$("#grade option[value != '']").remove();
						//科目
						var dataList = data.paramList;
						for (var i = 0; i < dataList.length; i++) {
							$("#grade").append(
									"<option value='" + dataList[i].DictionaryCode + "'>"+ dataList[i].DictionaryName+ "</option>");
						}
					}
				});
		$("#deleteOper").click(function() {
			var selArr = $(listId).jqGrid('getGridParam', 'selarrrow');
			if (selArr.length == 0) {
				window.Msg.alert("请选择要删除的记录");
				return;
			}
			var examNumberList = [];
			for (var i = 0; i < selArr.length; i++) {
				var rowId = selArr[i];
				var rowData =  $(listId).jqGrid('getRowData', rowId);
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
									window.Msg.alert("此测试已经发布不能删除!");
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
		
 //得到添加考试所需要的参数
	var getParam = function() {
			var exam_time = $("#exam_time").val();
			var schoolYear = $("#schoolYear").val();
			var term = $("#term").val();
			var examType = $("#examType").val();
			var grade = $("#grade").val();
			var gradeTxt = $("#grade").find("option:selected").text();
			var closingTime = $("#closingTime").val();
			var introducedTime = $("#introducedTime").val();
			//得到选中的学校集合
			var school = [];
			var val = $("#school").find("option:selected").each(function() {
				var params = {};
				params["schoolVal"] = $(this).val();
				params["schoolText"] = $(this).text();
				params["brevityCode"]=$(this).attr("brevityCode");
				school.push(params);
			});
			var course = [];
			//得到选中的科目集合
			if($("#userRole").val()=="qpAdmin"){
				var val = $("#course").find("option:selected").each(function() {
					var params = {};
					params["course"] = $(this).val();
					params["courseName"] = $(this).text();
					course.push(params);
				});
			}else{
				
				var val = $("#course").find("option:selected").each(function() {
					var params = {};
					params["course"] = $(this).val().split(",")[0];
					params["courseName"] = $(this).text();
					course.push(params);
				});
			}
		
			//得到选中的班级集合
			var classId = [];
			var val = $("#classId").find("option:selected").each(function() {
				var classParams = {};
				classParams["classId"] = $(this).val();
				classParams["className"] = $(this).text();
				classId.push(classParams);
			});
			var dataParam = $(editorFormId).getFormData();
			//得到上传的附件集合
			var files = [];
			$.each(dataParam.files, function(index, item) {
				var param = {};
				param["id"] = item.id;
				param["fileName"] = item.fileName;
				files.push(param);
			});
			//校验
			if (exam_time == '' || exam_time == null) {
				$(".exam_time_validate").html("请选择测试日期!");
				return;
			}
  
			if (exam_time <getNowDate()) {
				$(".exam_time_validate").html("测试日期必须大于当前日期!");
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
			if($("#school option:selected").length==0 && introducedTime!="" && introducedTime!=null && $("#userRole").val()=="qpAdmin"){
				$(".school_validate").html("请选择学校");
				return;
			}
			if($("#classId option:selected").length==0 && introducedTime!="" && introducedTime!=null && $("#userRole").val()!="qpAdmin"){
				$(".classId_validate").html("请选择班级");
				return;
			}
			if($("#course option:selected").length==0 && introducedTime!="" && introducedTime!=null && $("#userRole").val()!="qpAdmin"){
				$(".course_validate").html("请选择科目");
				return;
			}
			
			if (exam_time < introducedTime) {
				$(".introducedTime_validate").html("测试发布时间不能大于测试时间!");
				return;
			}
			if (closingTime == '' || closingTime == null) {
				$(".closingTime_validate").html("请选择考号截止日期!");
				return;
			}
			
			var strYesterday=loadStrYesterday(exam_time)

			if (closingTime > strYesterday) {
				$(".closingTime_validate").html("考号生成时间至少在测试时间的前一天!");
				return;
			}
			if($("#editorCourse").find(".examState:checked").length==0 && introducedTime!="" && introducedTime!=null && $("#userRole").val()=="qpAdmin" ){
				window.Msg.alert("请选择科目");
				return;
			}
			
			
			var arrayStr=[];
		    var str;
			if($("#userRole").val()=="qpAdmin"){
				$("#editorCourse").find(".examState:checked").each(function(){
					  var examStartId=$(this).attr("id");
					 str=examStartId.substr(0, examStartId.indexOf('_'));
			 });	
			}else{
				var val = $("#course").find("option:selected").each(function() {
					  str=$(this).val().split(",")[0];
				});
			}
			//如果填写发布时间，校验科目考试时长必须填写
		 if(introducedTime!="" && introducedTime!=null && $("#"+str+"_examLength").val()=="" ){
					  arrayStr.push(_types[str]);
					  window.Msg.alert("已确定发布日期,请填写"+arrayStr+"测试时长");
				      return;
		   }
		 if(introducedTime!="" && introducedTime!=null && $("#"+str+"_startTime").val()==""){
			  		arrayStr.push(_types[str]);
					window.Msg.alert("已确定发布日期,请填写"+arrayStr+"测试日期");
		    		return;
       		} 	
		 if(introducedTime!="" && introducedTime!=null && $("#"+str+"_markingArrangementEndDate").val()==""){
		  		arrayStr.push(_types[str]);
				window.Msg.alert("已确定发布日期,请填写"+arrayStr+"安排阅卷人截止日期");
	    		return;
		} 	
		 if(introducedTime!="" && introducedTime!=null && $("#"+str+"_startTimeType").val()==""){
			  		arrayStr.push(_types[str]);
			  		window.Msg.alert("已确定发布日期,请填写"+arrayStr+"测试日期");
		     		return;
       		}
		 if($(".fileSize").text()=="false"){
			 window.Msg.alert("上传的附件有误");
	     		return;
		 }
			var oldExamCode = $("#examCode").val();
			var addExamByUserRoleParam = {};
			var params=[];
			if($("#userRole").val()=="qpAdmin"){
				if($("#grade").val()!=""){
					//得到选中科目的考试时间
					var dataParam = $("#editorCourse").getFormData();
					var grade=$("#grade").val();
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
					 $.ajax({
							url : getCourseByGradeUrl,
							type : "POST",
							data : {schoolType:schoolType},
							dataType : "JSON",
							async : false,
							success : function(data) {
								for(var i=0;i<data.length;i++){
										//判断考试状态是否选中
										if($("#"+data[i].DictionaryCode+"_examState").is(':checked')){
										    var param={};
											param["course"]=$("#"+data[i].DictionaryCode+"_course").val();
		                               		param["courseExamTime"]=$("#"+data[i].DictionaryCode+"_startTime").val();
		                               		param["courseStartTime"]=$("#"+data[i].DictionaryCode+"_startTimeType").val();
		                               		param["courseEndTime"]=$("#"+data[i].DictionaryCode+"_stopTimeType").val();
		                               		param["markingTime"]=$("#"+data[i].DictionaryCode+"_markingTime").val();
		                               		param["markingPlace"]=$("#"+data[i].DictionaryCode+"_markingPlace").val();
		                               		param["examLength"]=$("#"+data[i].DictionaryCode+"_examLength").val();
		                               		param["sortNumber"]=$("#"+data[i].DictionaryCode+"_sortNumber").val();
		                               		param["markingStartTime"]=$("#"+data[i].DictionaryCode+"_markingArrangementStartTime").val();
		                               		param["markingEndTime"]=$("#"+data[i].DictionaryCode+"_markingArrangementEndTime").val();
		                               		param["markingEndDate"]=$("#"+data[i].DictionaryCode+"_markingArrangementEndDate").val();
		                               		param["courseExamType"]=$("#"+data[i].DictionaryCode+"_examType").val();
		                               		param["zf"]=$("#"+data[i].DictionaryCode+"_zf").val();
		                               		params.push(param);
		                               }
							}
						}
					  });
				}
			}else{
				if($("#course").val()!=undefined){			
					for(var i=0;i<course.length;i++){
					    var param={};
						param["course"]=$("#"+course[i].course+"_course").val();
                   		param["courseExamTime"]=$("#"+course[i].course+"_startTime").val();
                   		param["courseStartTime"]=$("#"+course[i].course+"_startTimeType").val();
                   		param["courseEndTime"]=$("#"+course[i].course+"_stopTimeType").val();
                   		param["markingTime"]=$("#"+course[i].course+"_markingTime").val();
                   		param["markingPlace"]=$("#"+course[i].course+"_markingPlace").val();
                   		param["examLength"]=$("#"+course[i].course+"_examLength").val();
                   		param["sortNumber"]=$("#"+course[i].course+"_sortNumber").val();
                   		param["markingStartTime"]=$("#"+course[i].course+"_markingArrangementStartTime").val();
                   		param["markingEndTime"]=$("#"+course[i].course+"_markingArrangementEndTime").val();
                   		param["markingEndDate"]=$("#"+course[i].course+"_markingArrangementEndDate").val();
                   		param["courseExamType"]=$("#"+course[i].course+"_examType").val();
                   		param["zf"]=$("#"+course[i].course+"_zf").val();
                   		params.push(param);
					}
				}
			}
			//添加区级的考试
			if ($("#userRole").val() == "qpAdmin") {
				addExamByUserRoleParam = {
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
					school : school,
					oldExamCode : oldExamCode,
					id :$("#id").val(),
					params:params,
				}
			} else {                                            	//添加校级的考试
				   addExamByUserRoleParam = {
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
					classId : classId,
					oldExamCode : oldExamCode,
					id : $("#id").val(),
					params:params,
				}
			}
			POST(saveUrl, addExamByUserRoleParam, function(data) {
				/* if (data.mess == "examRepeat") {
					window.Msg.alert("不能添加已发布的测试!");
				} */
				;
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
		};
 
 
 
		//添加操作
		$("#editAdd").click(function() {
			 $("#editor_course_hide").html("");
			introducedState = "0";
			getParam();
		});

		//点击发布
		$("#introduced").click(function() {
			 $("#editor_course_hide").html("");
			introducedState = "1";	
			var introducedTime = $("#introducedTime").val();
			var arrayStr=[];
		    var str;
			if($("#userRole").val()=="qpAdmin"){
				$("#editorCourse").find(".examState:checked").each(function(){
					  var examStartId=$(this).attr("id");
					 str=examStartId.substr(0, examStartId.indexOf('_'));
			 });	
			}else{
				var val = $("#course").find("option:selected").each(function() {
					  str=$(this).val();
				});
			}
			if($("#school option:selected").length==0 && $("#userRole").val()=="qpAdmin"){
				$(".school_validate").html("请选择学校");
				return;
			}
			if($("#course option:selected").length==0 && $("#userRole").val()!="qpAdmin"){
				$(".course_validate").html("请选择科目");
				return;
			}
			if($("#classId option:selected").length==0 && $("#userRole").val()!="qpAdmin"){
				$(".classId_validate").html("请选择班级");
				return;
			}
			if($("#editorCourse").find(".examState:checked").length==0 && $("#userRole").val()=="qpAdmin"){
				window.Msg.alert("请选择科目");
				return;
			}
			 if($("#"+str+"_examLength").val()=="" )	{
		     		arrayStr.push(_types[str]);
		     		window.Msg.alert("已确定发布日期,请填写"+arrayStr+"测试时长");
	         		return;
	     	}
	    if($("#"+str+"_startTime").val()=="" )	{
		 			arrayStr.push(_types[str]);
		 	 		window.Msg.alert("已确定发布日期,请填写"+arrayStr+"测试日期");
	     	 		return;
		}
		if($("#"+str+"_startTimeType").val()=="" )	{
					arrayStr.push(_types[str]);
					window.Msg.alert("已确定发布日期,请填写"+arrayStr+"测试日期");
	      			return;
		}
		if($("#"+str+"_markingArrangementEndDate").val()=="" )	{
			arrayStr.push(_types[str]);
			window.Msg.alert("已确定发布日期,请填写"+arrayStr+"安排阅卷人截止日期");
  			return;
      }
		
		
			window.message({
				title : '提醒',
				text : '该测试发布后,不可以修改和删除,确定发布?',
				buttons : {
					'确定' : function() {
						window.top.$(this).dialog("close");
					$("#introducedTime").val(getNowDate());
						getParam();
					},
					'取消' : function() {
						window.top.$(this).dialog("close");
					}
				}
			});
			
		});
		$("#school").multiselect({
			checkAllText : "全选",
			uncheckAllText : "全不选",
			noneSelectedText : "选择学校",
			selectedText : '#' + " 所学校",
			selectedList : 2,
			// minWidth:'200',
		}).multiselectfilter({
			label : "学校名称",
			placeholder : "请输入校名"
		});
		$("#classId").multiselect({
			checkAllText : "全选",
			uncheckAllText : "全不选",
			noneSelectedText : "选择班级",
			selectedText : '#' + " 个班级",
			selectedList : 3
		});
		$("#course").multiselect({
			checkAllText : "全选",
			uncheckAllText : "全不选",
			noneSelectedText : "选择科目",
			selectedText : '#' + " 个科目",
			selectedList : 3
		})
		//改变考试日期的时候加载出考号截止日期
		$("#exam_time").change(function() {
			var examTime = $("#exam_time").val();
			var strYesterday =loadStrYesterday(examTime);
			$("#closingTime").val(strYesterday);
			if($("#userRole").val()=="qpAdmin"){
				 $("#editorCourse").find(".examState:checked").each(function(){
					  var examStartId=$(this).attr("id");
					  var str=examStartId.substr(0, examStartId.indexOf('_'));
					  $("#"+str+"_startTime").val(examTime);
					  $("#"+str+"_markingArrangementEndDate").val(loadStrTmorrowday(examTime));
				 });
			}else{
				var val = $("#course").find("option:selected").each(function() {
					$("#"+$(this).val()+"_startTime").val(examTime);
					  $("#"+$(this).val()+"_markingArrangementEndDate").val(loadStrTmorrowday(examTime));
				});
			}
			
		});
	});
	
	//校验考试日期和学科考试日期
	var examTime=function(){
		 $("#editorCourse").find(".examState").click(function(){
			  var examStateId=$(this).attr("id");
			  var str=examStateId.substr(0, examStateId.indexOf('_'));
			  if($("#"+examStateId).is(':checked')){
				   var examTime=$("#exam_time").val();
				   $("#"+str+"_startTime").val(examTime);
				   if(examTime!=""){
					   $("#"+str+"_markingArrangementEndDate").val(loadStrTmorrowday(examTime));
				   }else{
					   $("#"+str+"_markingArrangementEndDate").val(examTime);
				   }
				 
				   $("#"+str+"_startTime,#"+str+"_startTimeType,#"+str+"_markingTime,#"+str+"_examType,#"
						   +str+"_markingPlace,#"+str+"_examLength,#"+str+"_markingArrangementEndTime,#"+str+"_zf,#"+
						   str+"_markingArrangementStartTime,#"+str+"_markingArrangementEndDate")
						   .attr("disabled", false);
				   $("#"+str+"_startTime,#"+str+"_startTimeType,#"+str+"_examType,#"+str+
						   "_markingTime,#"+str+"_markingPlace,#"+str+"_markingArrangementEndTime,#"+
						   str+"_examLength,#"+str+"_markingArrangementStartTime,#"+str+"_markingArrangementEndDate")
						   .css("background","#CAF1BD");
			  }else{
				$("#"+str+"_startTime,#"+str+"_startTimeType,#"+str+
					"_stopTimeType,#"+str+"_markingTime,#"+str+"_markingPlace,#"
					+str+"_examLength,#"+str+"_markingArrangementEndTime,#"+
					str+"_markingArrangementStartTime,#"+str+"_markingArrangementEndDate")
					.val("");
				$("#"+str+"_examType").val("闭卷");
				   $("#"+str+"_startTime,#"+str+"_startTimeType,#"+str+"_examType,#"+str+
						   "_stopTimeType,#"+str+"_markingTime,#"+str+"_markingPlace,#"
						   +str+"_examLength,#"+str+"_markingArrangementEndTime,#"+
							  str+"_markingArrangementStartDate,#"+str+"_markingArrangementEndDate")
						   .attr("disabled", true);
				   $("#"+str+"_startTime,#"+str+"_startTimeType,#"+str+"_markingTime,#"+str+"_markingPlace,#"+str+"_examType,#"
						   +str+"_examLength,#"+str+"_markingArrangementEndTime,#"+
							  str+"_markingArrangementStartTime,#"+str+"_markingArrangementEndDate")
						   .css("background","#e7fcd9");
			  }
		 });
		 //改变学科考试日期时，校验学科考试日期不能小于考试日期
	 $("#editorCourse").find(".examStartTime").change(function(){
			      var examStartId=$(this).attr("id");
			      var examStartTime=$("#"+examStartId).val();
			      var examTime=$("#exam_time").val();
			      var str=examStartId.substr(0, examStartId.indexOf('_'));
			      $("#"+str+"_markingTime,#"+str+"_markingArrangementStartTime,#"+str+"_markingArrangementEndTime").val("");
			      if(examStartTime<examTime && examStartTime !=""){
					window.Msg.alert("学科测试日期不能小于本次测试的日期!");
					$("#"+examStartId).val("");
					return;
			      }
			      var endDate=$("#"+str+"_markingArrangementEndDate").val();
			    $("#"+str+"_markingArrangementEndDate").val(loadStrTmorrowday(examStartTime));
	 });
	 //改变学科考试日期时，校验阅卷日期不能小于科目考试日期
	 $("#editorCourse").find(".markingTime").change(function(){
		
	      var markingTimeId=$(this).attr("id");
	      var markingTime=$("#"+markingTimeId).val();
	      var str=markingTimeId.substr(0, markingTimeId.indexOf('_'));
	      var examStartTime=$("#"+str+"_startTime").val();
	      
	      if(examStartTime=="" || examStartTime==null){
	    	  window.Msg.alert("请先填写测试日期!");
	    	  $("#"+markingTimeId).val("");
	    	  return;
	      }
	     if(examStartTime>=markingTime){
			window.Msg.alert("阅卷日期需大于学科测试日期!");
			$("#"+markingTimeId).val("");
			  $("#"+str+"_markingArrangementEndDate").val(loadStrTmorrowday(examStartTime));
			return;
	      } 
      });
	 
	 //改变安排阅卷人截止日期时，安排阅卷人截止日期不能小于考试日期
	 $("#editorCourse").find(".markingArrangementStartDate").change(function(){
			      var endDateId=$(this).attr("id");
			      var markingArrangementEndDate=$("#"+endDateId).val();
			      var examTime=$("#exam_time").val();
			      var str=endDateId.substr(0, endDateId.indexOf('_'));
			      var examStartTime=$("#"+str+"_startTime").val();
			      if(examTime==""){
			    		window.Msg.alert("请先填写测试日期!");
						$("#"+endDateId).val("");
						return;
			      }
			    
			      if(markingArrangementEndDate<examTime && markingArrangementEndDate !=""){
					window.Msg.alert("安排阅卷人截止日期不能小于本次测试的日期!");
					  if(examStartTime==""){
						  $("#"+endDateId).val(examStartTime);
				      }else{
				    	  $("#"+endDateId).val(loadStrTmorrowday(examStartTime));
				      }
					return;
			      }
	 });
	 
 $("#editorCourse").find(".markingArrangementEndTime").blur(function(){
		 var endDateId=$(this).attr("id");
		 var endStr=endDateId.substr(0, endDateId.indexOf('_'));
		 var startTime=$("#"+endStr+"_markingArrangementStartTime").val();
		 var endTime=$("#"+endStr+"_markingArrangementEndTime").val();
		 var a=startTime.split(":");  
		 var   b=endTime.split(":"); 
		 var date = new Date();
			
		 if(date.setHours(b[0],b[1])<=date.setHours(a[0],a[1]) && endTime!=""){
			 $("#"+endStr+"_markingArrangementEndTime").val("");
		 }
	 });    
		   /*校验时长只能是正整数  */
		      $("#editorCourse").find(".examLength").blur(function(){
		    	   var examLengthId=$(this).attr("id");
		    	   var inpVal =$("#"+examLengthId).val();
		    	   var startS=examLengthId.substr(0, examLengthId.indexOf('_'));
		    	   var startTime=$("#"+startS+"_startTimeType").val();
		    	   if(startTime!=null && startTime!=""){
		    		   var hour=startTime.split(":")[0];  
		    		   var   minutes=startTime.split(":")[1]; 
		    			if(minutes=="0"){
		    				minutes="00";
		    			}
		    			var time=hour+":"+minutes;
		    			var examLength=$("#"+startS+"_examLength").val();
		    			var stopTime=addMinutes(time,examLength);
		    		   $("#"+startS+"_stopTimeType").val(stopTime);
		    	   }
		    	   if(inpVal!=inpVal.replace(/\D/g,'')){
		    			window.Msg.alert("请输入正整数!");
		    			$("#"+examLengthId).val("");
		    			return;
		    	   }
		    }); 
	}; 

	  //点击查看考试详情
	function queryExamOper(examNumber,Id,examName,ev){
		var $i = $(ev.currentTarget).find("i"), $piel = $(".page-show-panel")
		.show({
			effect : "slide",
			direction : "up",
			easing : 'easeInOutExpo',
			duration : 900
		});
		$("#examName").val(examName);
		$("#showForm").find('input,select').attr("disabled", "disabled");
		$("#showForm").find('input,select').css({"background":"#e7fcd9","border":"0"});
		var datas = {
			examCode : examNumber
		}
		$("#school option[value != '']").remove();
		$("#classId option[value != '']").remove();
		$("#course option[value != '']").remove();
		$.ajax({
					url : loadUrl,
					type : "POST",
					data : { id : Id},
					dataType : "JSON",
					success : function(data) {
						var $piel = showSlidePanel(".page-show-panel").find("h4 i").removeClass();
						if ($i.length) {
							$piel.addClass($i.attr("class"));
						}
						 $("#filesList").empty();
						$("#exam_times").val(dateTime(data.exam_time));
						$("#exam_names").val(data.exam_Name);
						$("#schoolYears").val(data.school_Year);
						$("#terms").val(_types[data.term]);
						$("#examTypes").val(_types[data.exam_Type]);
						$("#grades").val(_types[data.grade_Code]);
						$("#gradeCode").val(data.grade_Code);
						var introducedTime = data.introduced_Time;
						if (introducedTime != null) {
							$("#introducedTimes").val(dateTime(data.introduced_Time));
						}else{
							$("#introducedTimes").val(data.introduced_Time);
						}
					
						var closingTime = data.closing_Time;
						if (closingTime != null) {
							$("#closingTimes").val(dateTime(data.closing_Time));
						}else{
							$("#closingTimes").val(closingTime);
						}
						
						 var fileStr="";
						 for (var i = 0; i < data.files.length; i++) 
						 {
								 var fileObj = data.files[i];
						         fileStr+="<p><a class='file-name' data-fid=\""+fileObj.id+"\" href='<%=request.getContextPath()%>/platform/accessory_.do?command=download&id="+ fileObj.id+ " '>"+ fileObj.fileName + "</a></p>";
						}
						$("#filesList").append(fileStr);
						if ($("#userRole").val() == "qpAdmin") {
							$.ajax({
									url : schoolSelectUrl,
									type : "POST",
									data : datas,
									dataType : "JSON",
									success : function(data) {
										var schoolNames=[];
										var schoolNum=1;
									    for (var i = 0; i < data.length; i++) 
									    {
									    	var schoolName=data[i].School_Name;
									    	if(1<schoolNum  && schoolNum<=data.length){
									    		teacherNames.push(",");
											}
									    	schoolNames.push(schoolName);
										}
									    $("#schools").append("<span>" + schoolNames+ "</span>");
										    $.ajax({
												url : showSelectArrangementByExamUrl,
												type : "POST",
												data : {gradeId:$("#gradeCode").val(),examCode:examNumber},
												dataType : "JSON",
												success : function(data) {
													$("#examCode").val(examNumber);
													 var str="<tr id=arrangement>"+
														"<th style='width:33px;'>序号</th>"+
														"<th style='font-size: 14px; text-align: center; width:100px;'>科目</th>"+
														"<th style='font-size: 14px; text-align: center; width:75px;'>测试形式</th>"+
														"<th style='font-size: 14px; text-align: center; width:90px;'>测试日期</th>"+
														"<th style='font-size: 14px; text-align: center; width:100px;'>测试时间</th>"+
														"<th style='font-size: 14px; text-align: center; width:75px;'>测试时长<br/>(分钟)</th>"+
														"<th style='font-size: 14px; text-align: center; width:140px;'>阅卷人</th>"+
														"<th style='font-size: 14px; text-align: center; width:90px;'>阅卷日期</th>"+
														"<th style='font-size: 14px; text-align: center; width:100px;'>阅卷时间</th>"+
														"<th style='font-size: 14px; text-align: center; width:95px;'>安排阅卷人<br/>截止日期</th>"+
														"<th style='font-size: 14px; text-align: center; width:20%;'>阅卷地点</th>"+
														"</tr>"
														$("#showArrangement").append(str);
													for(var i=0;i<data.length;i++){
														if(data[i].Course!=null && data[i].Course!=""){
															var str;
															if(data[i].schoolSum==data[i].schoolNum){
																var isExistArrangement="allExistArrangement";
																 str="共<a href='javascript:void(0)' onclick='queryArrangemet(\""+ data[i].Course
																	+ "\",\""
																	+$("#examCode").val()
																	+ "\",\""
																	+$("#examName").val()
																	+ "\",\""
																	+_types[ data[i].Course]
																    + "\",\""
																	+isExistArrangement
																	+ "\");'  class='badge'>"+data[i].schoolSum+"</a>所<br/>已全部安排<br/>已安排<span class='badge brightGreen'>"+data[i].teacherCount+"</span>人"
															}else if(data[i].schoolSum==data[i].schoolNotNum){
																var isExistArrangement="allNotExistArrangement";
																str="共<a class='badge' href='javascript:void(0)' onclick='queryArrangemet(\""+ data[i].Course
																	+ "\",\""
																	+$("#examCode").val()
																	+ "\",\""
																	+$("#examName").val()
																	+ "\",\""
																	+_types[ data[i].Course]
																    + "\",\""
																	+isExistArrangement
																	+ "\");'  class='badge'>"+data[i].schoolSum+"</a>所<br/>"+
																	"暂未安排"
															}else{
																var isExistArrangement="allArrangement",
																       existArrangement="existArrangement",
																       notExistArrangement="notExistArrangement",
																 str="&nbsp;&nbsp;共<a href='javascript:void(0)' onclick='queryArrangemet(\""+ data[i].Course
																	+ "\",\""
																	+$("#examCode").val()
																	+ "\",\""
																	+$("#examName").val()
																	+ "\",\""
																	+_types[ data[i].Course]
																    + "\",\""
																	+isExistArrangement
																	+ "\");'  class='badge'>"+data[i].schoolSum+"</a>所<br/>"+
																	"已安排<a href='javascript:void(0)' onclick='queryExistArrangemet(\""+ data[i].Course
																	+ "\",\""
																	+$("#examCode").val()
																	+ "\",\""
																	+$("#examName").val()
																	+ "\",\""
																	+_types[ data[i].Course]
																	+ "\",\""
																	+existArrangement
																	+ "\");'  class='badge brightGreen'>"+data[i].schoolNum+"</a>所<br/>"+
																	"未安排<a href='javascript:void(0)' onclick='queryNotArrangemet(\""+ data[i].Course
																	+ "\",\""
																	+$("#examCode").val()
																	+ "\",\""
																	+$("#examName").val()
																	+ "\",\""
																	+_types[ data[i].Course]
																	+ "\",\""
																	+notExistArrangement
																	+ "\");'  class='badge red'>"+data[i].schoolNotNum+"</a>所<br/>"+
																	"已安排<span class='badge brightGreen' style='margin:1px 2px;'>"+data[i].teacherCount+"</span>人"
															}
															 
															var makingTime="";
															if(data[i].Marking_Start_Time!="" && data[i].Marking_Start_Time!=null){
																makingTime=data[i].Marking_Start_Time+"至"
                                                                   +data[i].Marking_End_Time;
															}
														   var courseTime="";
														   if(data[i].Course_Start_Time!="" && data[i].Course_Start_Time!=null){
															   courseTime=data[i].Course_Start_Time+"至"
                                                               +data[i].Course_End_Time;
														   }
															var arrangementStr = 
                                                            	   "<tr><td style='text-align:center; '>"+(i+1)+"</td>"+
                                                            	   "<td style='text-align:left; ' >"+_types[data[i].Course]+"</td>"+
                                                            	   "<td style='text-align:center; ' >"+data[i].Course_Exam_Type+"</td>"+
                                                            	   "<td style='text-align:center; ' >"+data[i].Course_Exam_Time+"</td>"+
                                                            	   "<td style='text-align:center; ' >"+courseTime+"</td>"+
                                                            	   "<td style='text-align:center; ' >"+data[i].Exam_Length+"</td>"+
                                                            	   "<td style='text-align:center; '>"+str+"</td>"+
                                                            	   "<td style='text-align:center;' >"+data[i].Marking_Time+"</td>"+
                                                            	   "<td style='text-align:center; ' >"+makingTime+"</td>"+
                                                                   "<td style='text-align:center; ' >"+data[i].Marking_End_Date+"</td>"+
                                                            	   "<td style='text-align:left; '>"+data[i].Marking_Place+"</td></tr>";
															   $("#showArrangement").append(arrangementStr);
														}
														$('#showArrangement tbody tr:odd').css('background','#f3f3f3');
													}
												}
										    });
								   }
								});
					} else {
						  $.ajax({
								  url : classAndCourseSelectUrl,
								  type : "POST",
								  data : datas,
								  dataType : "JSON",
								  success : function(data) {
										var courseNames=[];
										var courseNum=1;
										var classNames=[];
										var classNum=1;
										//得到选中的科目
									 for (var i = 0; i < data.courselist.length; i++) 
										 {
										 	var courseName=_types[data.courselist[i].Course];
									    	if(1<courseNum  && courseNum<=data.courselist.length){
									    		courseNames.push(",");
											}
									    	courseNames.push(courseName);
										}
									 $("#courses").append("<span>"+ courseNames+ "</span>");
									 
								     for (var i = 0; i < data.list.length; i++) 
								 		{
								    	 var className= _typesClass[data.list[i].Class_Id];
									    	if(1<classNum  && classNum<=data.list.length){
									    		classNames.push(",");
											}
									    	classNames.push(className);
										}
								     $("#classIds").append("<span>"+ classNames+ "</span>");
								     $.ajax({
											url : showSelectArrangementByExamUrl,
											type : "POST",
											data : {gradeId:$("#gradeCode").val(),examCode:examNumber},
											dataType : "JSON",
											success : function(data) {
												 var str="<tr id=arrangement>"+
													"<th style='width:3%;''>序号</th>"+
													"<th style='font-size: 14px; text-align: center; width:100px;'>科目</th>"+
													"<th style='font-size: 14px; text-align: center; width:70px;'>测试形式</th>"+
													"<th style='font-size: 14px; text-align: center; width:90px;'>测试日期</th>"+
													"<th style='font-size: 14px; text-align: center; width:100px;'>测试时间</th>"+
													"<th style='font-size: 14px; text-align: center; width:65px'>测试时长<br/>(分钟)</th>"+
													"<th style='font-size: 14px; text-align: center; width:183px;'>阅卷人</th>"+
													"<th style='font-size: 14px; text-align: center; width:90px;'>阅卷日期</th>"+
													"<th style='font-size: 14px; text-align: center; width:100px;'>阅卷时间</th>"+
													"<th style='font-size: 14px; text-align: center; width:95px;'>安排阅卷人<br/>截止日期</th>"+
													"<th style='font-size: 14px; text-align: center; width:20%;'>阅卷地点</th>"+
													"</tr>"
													$("#showArrangement").append(str);
												for(var i=0;i<data.length;i++){
													var makingTime="";
													if(data[i].Marking_Start_Time!="" && data[i].Marking_Start_Time!=null){
														makingTime=data[i].Marking_Start_Time+"至"
                                                         +data[i].Marking_End_Time;
													}
												   var courseTime="";
												   if(data[i].Course_Start_Time!="" && data[i].Course_Start_Time!=null){
													   courseTime=data[i].Course_Start_Time+"至"
                                                         +data[i].Course_End_Time;
												   }
												  var arrangementStr = 
	                                                     	   "<tr><td style='text-align:center;'>"+(i+1)+"</td>"+
	                                                     	   "<td style='text-align:left; width:10%'>"+_types[data[i].Course]+"</td>"+
	                                                     	   "<td style='text-align:center; width:6%;' >"+data[i].Course_Exam_Type+"</td>"+
	                                                     	  "<td style='text-align:center; width:8%;' >"+data[i].Course_Exam_Time+"</td>"+
	                                                     	   "<td style='text-align:center; width:8%;' >"+courseTime+"</td>"+
	                                                    	  "<td style='text-align:center; width:6%;' >"+data[i].Exam_Length+"</td>"+
	                                                    	  "<td style='text-align:left;  width:17%;'>"+data[i].Teacher_Name+"</td>"+
	                                                    	  "<td style='text-align:center; width:8%;' >"+data[i].Marking_Time+"</td>"+
	                                                    	  "<td style='text-align:center; width:8%;' >"+makingTime+"</td>"+
                                                              "<td style='text-align:center; width:8%;' >"+data[i].Marking_End_Date+"</td>"+
                                                       	   "<td style='text-align:left;  width:25%;'>"+data[i].Marking_Place+"</td></tr>";
	                                                        $("#showArrangement").append(arrangementStr);
													}
												$('#showArrangement tbody tr:odd').css('background','#f3f3f3');
												}
									    });
									}
								});
						}
			}
	 });
	};
	
	function queryArrangemet(course,examCode,examName,courseName,isExistArrangement){
		var queryArrangemetUrl="examInfo/markingArrangementInfo.do?&examCode="
				                                    +examCode+"&course="+course+"&examName="+examName+"&courseName="
				                                    +courseName+"&isExistArrangement="+isExistArrangement;
		   frameDialog(queryArrangemetUrl, "阅卷人安排详情",
				   {mode:"middle",resizable:false,width:660,height:385,buttons:[
	               { text:"关闭", icons:{ primary:"ui-icon-cancel" }, click:function( ev )
	                        {
	                            var $this = window.top.$( this ) ;
	                                 $this.dialog( "close" ) ;
	                         }}
	                ]});
		 
	};
	
	function queryExistArrangemet(course,examCode,examName,courseName,existArrangement){
		var queryArrangemetUrl="examInfo/markingArrangementInfo.do?&examCode="+examCode
				                                    +"&course="+course+"&examName="+examName+"&courseName="
				                                    +courseName+"&existArrangement="+existArrangement;
		   frameDialog(queryArrangemetUrl, "阅卷人安排详情",
				   {mode:"middle",resizable:false,width:660,height:385,buttons:[
	               { text:"关闭", icons:{ primary:"ui-icon-cancel" }, click:function( ev )
	                        {
	                            var $this = window.top.$( this ) ;
	                                 $this.dialog( "close" ) ;
	                         }}
	                ]});
		 
	};
	
	function queryNotArrangemet(course,examCode,examName,courseName,notExistArrangement){
		var queryArrangemetUrl="examInfo/markingArrangementInfo.do?&examCode="+examCode
				                                    +"&course="+course+"&examName="+examName+"&courseName="
				                                    +courseName+"&notExistArrangement="+notExistArrangement;
		   frameDialog(queryArrangemetUrl, "阅卷人安排详情",
				   {mode:"middle",resizable:false,width:660,height:385,buttons:[
	               { text:"关闭", icons:{ primary:"ui-icon-cancel" }, click:function( ev )
	                        {
	                            var $this = window.top.$( this ) ;
	                                 $this.dialog( "close" ) ;
	                         }}
	                ]});
		 
	};
	 
	 
	  //时间(时分)
		var startTime=function(dp){
			var onpickingFunc = function(){
				//$dp.cal  日期控件对象
				var startTimeTypeId=$dp.srcEl.id; //得到当前选中的id
				startTimeTypeId=startTimeTypeId.substr(0, startTimeTypeId.indexOf('_'));
				var hour=$dp.cal.newdate.H;
				var minutes=$dp.cal.newdate.m;
				if(minutes=="0"){
					minutes="00";
				}
				var time=hour+":"+minutes;
				var examLength=$("#"+startTimeTypeId+"_examLength").val();
				 if(examLength==""){
					window.Msg.alert("请输入测试时长");
					 return;
				} 
				var stopTime=addMinutes(time,examLength);
			   $("#"+startTimeTypeId+"_stopTimeType").val(stopTime);
			}
			WdatePicker({skin:'whyGreen',dateFmt:'H:mm',onpicking:onpickingFunc})
		 };
		 
		var markingStartTime=function(){
			
			var onpickingFunc = function(){
				//$dp.cal  日期控件对象
				var startTimeTypeId=$dp.srcEl.id; //得到当前选中的id
				startTimeTypeId=startTimeTypeId.substr(0, startTimeTypeId.indexOf('_'));
				var hour=$dp.cal.newdate.H;
				var minutes=$dp.cal.newdate.m;
				if(minutes=="0"){
					minutes="00";
				}
				var time=hour+":"+minutes;
				var markingEndTime=$("#"+startTimeTypeId+"_markingArrangementEndTime").val();
				var date = new Date();
				var a = time.split(":");
				var b = markingEndTime.split(":");
			    if(date.setHours(a[0],a[1]) >= date.setHours(b[0],b[1])&& markingEndTime!=""){
			    	$("#"+startTimeTypeId+"_markingArrangementEndTime").val("");
			    	window.Msg.alert("阅卷结束时间需大于阅卷开始时间");
					 return;
			    }
			}
			 WdatePicker({skin:'whyGreen',dateFmt:'H:mm',onpicking:onpickingFunc});
		} ;
		
		
		var markingEndTime=function(){
			var errDealMode;
			var onpickingFunc = function(){
				//$dp.cal  日期控件对象
				var startTimeTypeId=$dp.srcEl.id; //得到当前选中的id
				startTimeTypeId=startTimeTypeId.substr(0, startTimeTypeId.indexOf('_'));
				var hour=$dp.cal.newdate.H;
				var minutes=$dp.cal.newdate.m;
				if(minutes=="0"){
					minutes="00";
				}
				var time=hour+":"+minutes;
				var markingStartTime=$("#"+startTimeTypeId+"_markingArrangementStartTime").val();
				var date = new Date();
				var a = time.split(":");
				var b = markingStartTime.split(":");
			    if(markingStartTime!='' && date.setHours(a[0],a[1]) <= date.setHours(b[0],b[1])){
			      	$("#"+startTimeTypeId+"_markingArrangementEndTime").val("");
			    	window.Msg.alert("阅卷结束时间需大于阅卷开始时间");
					 return;
			    }
			}
			 WdatePicker({skin:'whyGreen',dateFmt:'H:mm',onpicking:onpickingFunc})
			 
		};
		
</script>
</head>
<body>
	<div class="page-list-panel">
		<div class="head-panel">
			<div class="toolbar">
				<table style="height: 100%;" cellpadding="0" cellspacing="0" border="0">
					<tr>
						 <td style="padding-left: 12px; padding-right: 24px;">
						       <i class="fa fa-list-ul micon"></i></td>
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
						<td style="padding-left: 24px; padding-right: 5px;"><input id="fastQueryText" type="text" placeholder="输入测试名称" style="line-height: 1em; font-size: 1em; cursor: text;" /></td>
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
		         <form id="editorForm" >
			        <div id="add_exam_info">
						     <input id="examCode" type="hidden" />
						     <input id="examName" type="hidden" />
						      <input id="userRole"type="hidden" />
						      <input id="id"  type="hidden"  value=""/>
						<table cellspacing="0" border="0" style="width: 94.7%;" class="tableTemplet">
							<thead>
								<tr>
									<th colspan="4" style="color: black;">
									<i class="fa fa-file-text" style="margin-right: 5px;"></i> <span>添加测试</span></th>
								</tr>
							</thead>
								<tr>
									<td class="label" style="width: 8%">测试日期：</td>
									<td style="width: 6%">
									<!-- <input id="exam_time" type="text" class="Wdate" onFocus="WdatePicker({minDate:'%y-%M-{%d+1}'})" placeholder="请输入测试时间" style="width: 205px;"/> -->
									    <input type="text" id="exam_time"  class="form-control exam_time_datePicker"  readonly="readonly" placeholder="请输入测试日期" style="width: 205px;" /> 
										<span class="exam_time_validate" style="color: red;"></span>
								   </td>
									<td class="label" style="width: 2%">学年：</td>
									<td style="width: 15%">
									      <select id="schoolYear" name="schoolYear" class="form-control" style="width: 213px;"></select> 
									     <span class="schoolYear_validate" style="color: red;"></span>
									</td>
								</tr>
								<tr>
									<td class="label" style="width: 5%">年级：</td>
									<td style="width: 6%">
									    <select id="grade" name="grade" class="form-control" style="width: 213px;">
											<option value="">请选择年级</option>
									   </select> 
									   <span class="grade_validate" style="color: red;"></span>
									</td>
									<td class="label" style="width: 5%">学期：</td>
									<td>
									    <select id="term" name="term" class="form-control" data-dic="{code:'xq'}" style="width: 213px;">
											<option value="">请选择学期</option>
									     </select>
									    <span class="term_validate" style="color: red;"></span>
									</td>
								</tr>
								<tr>
									<td class="label" style="width: 5%">测试类型：</td>
									 <td style="width: 15%" colspan="3">
									 <select id="examType" name="examType" class="form-control" data-dic="{code:'kslx'}" style="width: 213px;">
											<option value="">请选择测试类型</option>
									</select> 
									<span class="examType_validate" style="color: red;"></span></td>
								</tr>
								<tr class="qpAdmin">
									<td class="label" style="width: 5%">学校：</td>
									<td style="width: 15%" colspan="3">
										<div class="multiselect">
											<select id="school" name="school" class="form-control"  multiple="multiple" style="width: 213px; height: 26px;"></select>
										 <span class="school_validate" style="color: red;"></span>
										 </div> 
										 
									</td>
								</tr>
								<tr class="schoolAdmin">
									<td class="label" style="width: 5%">班级：</td>
									<td style="width: 15%" colspan="3">
										<div class="multiselect">
											<select id="classId" name="classId" class="form-control" multiple="multiple" style="width: 213px; height: 26px;">
											</select>
											<span class="classId_validate" style="color: red;"></span>
										</div> 
										</td>
								</tr>
								<tr class="schoolAdmin">
									<td class="label" style="width: 5%">科目：</td>
									<td style="width: 15%" colspan="3">
										<div class="multiselect">
											<select id="course" name="course" class="form-control" multiple="multiple" style="width: 213px; height: 26px;">
											</select>
											<span class="course_validate" style="color: red;"></span>
										</div>
									</td>
								</tr>
								<tr>
									<td class="label" style="width: 5%">发布日期：</td>
									<td style="width: 15%">
									<input type="text"  id="introducedTime" class="form-control exam_time_datePicker"  readonly="readonly"  placeholder="请输入发布测试日期" style="width: 205px" /> 
									<span class="introducedTime_validate" style="color: red;"></span>
									</td>
									<td class="label" style="width: 8%">考号生成截止日期：</td>
									<td style="width: 15%">
									<input type="text" id="closingTime"  class="form-control exam_time_datePicker" readonly="readonly"  placeholder="请输入生成考号截止日期" style="width: 205px" /> 
									<span class="closingTime_validate" style="color: red;"></span>
									</td>
								</tr>
								 <tr>
								    <td class="label">相关附件：</td>
									<td colspan="3" class="uplodFile">
									     <input data-xtype="upload" data-appendto="#fileListTD" type="file" name="files" id="files" style="width: 667px;" data-button-text="上传附件" />
										 <div id="fileListTD" class="fileListTD" style="word-break: break-all;"></div>
							 	   </td>
								 </tr>
						</table>
						
						<!--测试科目 -->
					<div id="editor_course"  >
						<table cellspacing="0" border="0" style="width: 94.7%;" class="tableTemplet" id="editorCourse">
							<thead>
								<tr>
									<th colspan="11" style="color: black;">
									<i class="fa fa-file-text"></i> 
									<span>测试相关科目信息</span>
									</th>
								</tr>
							</thead>
							<tbody>
							 
							</tbody>
						</table>
					</div>
					<div id="editor_course_hide"  style="display: none;" ></div>
					</div>				
			    </form>
		     </div>
		</div>
	</div>
   <div class="page-show-panel full-drop-panel">
		<div class="title-bar">
			<h4>
				<i class="fa fa-plus"></i>
			</h4>
			<div class="btn-area">
				<div style="margin-top: 4px;">
					<button id="backBTN">
						<i class="fa fa-reply"></i>返回
					</button>
				</div>
			</div>
		</div>
		<div class="page-content">
		     <div class="page-inner-content">
		         <form id="showForm" >
			        <div id="show_exam_info">
						<table cellspacing="0" border="0" style="width: 94.7%;" class="tableTemplet">
							<thead>
								<tr>
									<th colspan="4" style="color: black;"><i
										class="fa fa-file-text" style="margin-right: 5px;"></i> <span>测试信息</span></th>
								</tr>
							</thead>
								<tr>
									<td class="label" style="width: 8%">测试日期：</td>
									<td style="width: 6%"><input type="text" id="exam_times" /></td>
									<td class="label" style="width: 6%">学年:</td>
									<td style="width: 15%"><input id="schoolYears" name="schoolYear" /></td>
								</tr>
								<tr>
									<td class="label" style="width: 6%">年级：</td>
									<td style="width: 6%"> <input id="grades" type="text"></input><input id="gradeCode" type="hidden"></input></td>
									<td class="label" style="width: 6%">学期：</td>
									<td><input id="terms" type="text"></input></td>
								</tr>
								<tr>
									<td class="label" style="width: 6%">测试类型：</td>
									 <td style="width: 15%" colspan="3"><input id="examTypes" type="text"></input></td>
								  </tr>
								<tr class="qpAdmin">
									<td class="label" style="width: 6%">学校：</td>
									<td style="width: 15%" colspan="3"><div id="schools"></div></td>
								</tr>
								<tr class="schoolAdmin">
									<td class="label" style="width: 6%">班级：</td>
									<td style="width: 15%" colspan="3"><div id="classIds"></div></td>
								</tr>
								<tr class="schoolAdmin">
									<td class="label" style="width: 6%">科目：</td>
									<td style="width: 15%" colspan="3"><div id="courses"></div></td>
								</tr>
								<tr>
									<td class="label" style="width: 6%">发布日期：</td>
									<td style="width: 15%"><input type="text" id="introducedTimes"  /> </td>
									<td class="label" style="width: 8%">考号生成截止日期：</td>
									<td style="width: 15%"><input type="text" id="closingTimes"/>  </td>
								</tr>
								 <tr>
								 <td class="label">相关附件：</td>									
								    <td id="showFile"  colspan="3"><div id="filesList" style="word-break: break-all;"></div></td>
								 </tr>
						</table>
					</div>
					<!-- 阅卷人 -->
					<div id="show_arrangement"  >
						<table cellspacing="0" border="0" style="width: 1078px;" class="tableTemplet" id="showArrangement">
							<thead>
								<tr>
									<th colspan="11" style="color: black;"><i
										class="fa fa-file-text" style="margin-right: 5px;"></i> <span>测试相关科目信息</span></th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
					</div>
			    </form>
		     </div>
		</div>
	</div>
</body>
</html>