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
#add_exam_info {
	margin-left: 350px;
}
#add_exam_info ul li {
	list-style: none;
	margin-top: 30px;
}
.page-inner-content{
padding: 0px;
}
.title-bar input{
font: 1em Arial, Tahoma, Verdana, sans-serif;
    display: inline;
    padding: 0.4em 1em;
    line-height: 1em;
    border: 1px solid #3B5617;
        background-color: white;
}
#stuInfoList2{
width:1121px;}
</style>
<script type="text/javascript">
	var listId = "#list2", 
	       editorFormId = "#editorForm", 
	       pagerId = "#pager2",
           stuInfolistId = "#stuInfoList2";
			stuInfoPagerId = "#stuInfopager",
			stuInfolistId3 = "#stuInfoList3";
			stuInfoPagerId3 = "#stuInfopager3",
			examNumberStates="",
			stuIfnoListUrl = "../examNumberManagement/examNumberInfo.do?command=searchPaging",
			getImportParamsUrl= "../examNumberManagement/examNumberInfo.do?command=getImportParams",
			listUrl = "../examNumberManagement/schoolExamNumberManage.do?command=searchPaging",
            loadUrl="../examNumberManagement/examNumberInfo.do?command=loadExamNumberById",
            updateUrl="../examNumberManagement/examNumberInfo.do?command=updateExamNumberById",
            lookUrl="../examNumberManagement/examNumberInfo.do?command=selectIdByAssociatedExamNumber",
            verifyCodeUrl="../examNumberManagement/examNumberManage.do?command=examNumberIsExist",
           getStuExamNumberUrl="../examNumberManagement/examNumberInfo.do?command=getStuExamNumber";
     
           $(function() {
    	

    	 $(listId).trigger("reloadGrid");
		_initButtons({
			cancelBTN : function() {
				$(".ui-effects-wrapper").css("display","none");
				hideSlidePanel(".page-update-panel,.page_editor_panel2");
				$(listId).trigger("reloadGrid");
				showSlidePanel(".page-list-panel");
				
			},
			backBTN :function(){
				 $('.input ').val(""); 
				$("#examNumberState").find("option[selected=selected]").attr("selected",false);
				 $(".ui-effects-wrapper").css("display","none");
				hideSlidePanel(".page-editor-panel,.page-update-panel");
				showSlidePanel(".page-list-panel");
				$(listId).trigger("reloadGrid");
			},
			backBTNs :function(){
				$("#examNumberState").find("option[selected=selected]").attr("selected",false);
				hideSlidePanel(".page_file_panel");
				showSlidePanel(".page-list-panel");
				$(listId).trigger("reloadGrid");
			
			},
			saveBTN: function(ev) {
				  var id=$("#id").val();
				  var examNumber = $("#examNumber").val();
				  var data = {id:id,examNumber:examNumber};
				  window.message({
			 			title:'提醒',
			 			text:'确定修改该学生的考号吗?',
			 			buttons:{
			 				'确定':function(){
			 					window.top.$(this).dialog("close");
			 			    	   $.ajax({
			 				   	       	url:updateUrl,
			 				   	        type:"POST",
			 				   	        data:data,
			 				   	        dataType:"JSON",
			 				   	        success: function(data, xhr) {
			 				   	        	if(data.mess=="success"){
			 				   	        	window.Msg.alert("修改成功!");
			 				   	        	}
			 				   	        	$(stuInfolistId).trigger("reloadGrid");
			 				   	        hideSlidePanel(".page-update-panel");
			 				   	       }
			 				   	    });  
			 				}
			 				},'取消':function(){
			 					window.top.$(this).dialog("close");
			 				} 
			 			});
			},
			 
		}); //from page.common.js
		_initFormControls(); //from page.common.js
		_initValidateForXTypeForm(editorFormId);
		 loadDictionary();
		 

		var _colModel = [
				{
					name : 'Id',
					key : true,
					width : 40,
					hidden : true,
					search : false
				},{
					label : "测试日期",
					name : 'Exam_Time',
					sortable : false,
					width : 70,
					align : "center",
				},{
					label : "测试编号",
					name : 'Exam_Number',
					sortable : false,
					width : 90,
					align : "center",
				},{
					label : "测试名称",
					name : 'Exam_Name',
					sortable : false,
					autoWidth : true,
					align : "left",
				},{
					label : "测试班级",
					name : 'classIdList',
					sortable : false,
					width : 100,
					align : "left",
					formatter : function(ar1, ar2, ar3) {
						var classNames=[];
						 for(var i=0;i<ar3.classIdList.length;i++){
								var className=_typesClass[ar3.classIdList[i]];
							    classNames.push(className);
						 }
						 return classNames;
					} 
				},{
					label : "测试科目",
					name : 'courseIdlist',
					sortable : false,
					width : 100,
					align : "left",
					formatter : function(ar1, ar2, ar3) {
						var courseNames=[];
						 for(var i=0;i<ar3.courseIdlist.length;i++){
								var courseName=_types[ar3.courseIdlist[i]];
								courseNames.push(courseName);
						 }
						 return courseNames
					} 
				},{
					   label:"考号生成截止日期",
						name : 'Closing_Time',
						width : 120,
						search : false,
						align:"center",
						 formatter: function (ar1, ar2, ar3) {
							 if(ar3.Number_State != "1"){
				            		var dateDifference=getDateDifference(ar3.Closing_Time,getNowDate());
				                	if(dateDifference==0){
				                		 return ar3.Closing_Time +"<br/>今天截止"
				                	}else if(dateDifference>0){
				                		return ar3.Closing_Time +"<br/>距离截止<span style='color:#ffffff;' class='badge red'>"+dateDifference+"</span>天"
				                	}else{
				                		return ar3.Closing_Time;
				                	}
				            	}else{
				            		return ar3.Closing_Time;
				            	}
				            }
				},{
					label : "考号生成情况",
					name : 'testCaseGeneration',
					sortable : false,
					width : 150,
					align : "center",
					formatter : function(ar1, ar2, ar3) {
						if(ar3.Number_State!="1"){
							return "暂未生成考号"
						}else{
							return "已生成考号<a href='javascript:void(0)' style='color:#ffffff;' class='badge brightGreen'  onclick='queryStuIsExamNumber(\""
							+ ar3.Exam_Number
							+ "\",\""
							+ ar3.Grade_Code
							+ "\",\""
							+ ar3.testCaseGeneration
							+ "\",\""
							+ ar3.notTestCaseGeneration
							+ "\",\"" 
							+ ar3.Closing_Time
							+ "\",\""
							+ ar3.ev
							+ "\");'>"+ar3.testCaseGeneration+"</a>人"
							            +"<br>未生成考号<a href='javascript:void(0)' style='color:#ffffff;' class='badge red'  onclick='queryStuNotExamNumber(\""
										+ ar3.Exam_Number
										+ "\",\""
										+ ar3.Grade_Code
										+ "\",\""
										+ ar3.testCaseGeneration
										+ "\",\""
										+ ar3.notTestCaseGeneration
										+ "\",\"" 
										+ ar3.Closing_Time
										+ "\",\""
										+ ar3.ev
										+ "\");'>"+ar3.notTestCaseGeneration+"</a>人"
 						}
					}
				},{
					label : "操作",
					name : '',
					width : 120,
					align : "center",
					sortable : false,
					formatter : function(ar1, ar2, ar3) {
						if(ar3.Number_State!="1" && ar3.filesIsExist=="1"){
							var operStr;
							var today=getNowDate();
							if(today>ar3.Closing_Time){
							operStr
								= "<p>已截止生成考号</p>"+
									"<p><button id='queryFileOper' class='page-button' title='查看附件' onclick='queryFileOper(\""								
									+ ar3.Exam_Number
									+ "\",\""
									+ ar3.ev
									+ "\");'><i class='fa fa-file-text' style='margin-right: 5px;'></i>查看附件</button></p>";
							}else{
								operStr
								= "<p><button id='queryBTN' class='page-button' title='生成考号' onclick='queryOper(\""
									+ ar3.School_Year
									+ "\",\""
									+ ar3.School_Code
									+ "\",\""
									+ ar3.Exam_Type
									+ "\",\""
									+ ar3.Term
									+ "\",\""
									+ ar3.Exam_Number
									+ "\",\""
									+ ar3.Exam_Time
									+ "\",\""
									+ ar3.Grade_Code
									+"\",\""
									+ ar3.ev
									+ "\");'><i class='fa fa-file-text' style='margin-right: 5px;'></i>生成考号</button></p>"+
									"<p><button id='queryFileOper' class='page-button' title='生成考号' onclick='queryFileOper(\""								
									+ ar3.Exam_Number
									+ "\",\""
									+ ar3.ev
									+ "\");'><i class='fa fa-file-text' style='margin-right: 5px;'></i>查看附件</button></p>";
							}
						return operStr;
						}else if(ar3.Number_State=="1" && ar3.filesIsExist=="1"){
							var queryStuOper = "<p><button id='queryBTN' class='page-button' title='查看学生信息' onclick='queryStuOper(\""
								+ ar3.Exam_Number
								+ "\",\""
								+ ar3.Grade_Code
								+ "\",\""
								+ ar3.testCaseGeneration
								+ "\",\""
								+ ar3.notTestCaseGeneration
								+ "\",\"" 
								+ ar3.Closing_Time
								+ "\",\""
								+ ar3.ev
								+ "\");'><i class='fa fa-file-text' style='margin-right: 5px;'></i>查看学生信息</button></p>"+
								"<p><button id='queryFileOper' class='page-button' title='查看附件' onclick='queryFileOper(\""								
								+ ar3.Exam_Number
								+ "\",\""
								+ ar3.ev
								+ "\");'><i class='fa fa-file-text' style='margin-right: 5px;'></i>查看附件</button></p>";
						return queryStuOper;
						}else if(ar3.Number_State!="1" && ar3.filesIsExist=="0"){
							var operStr;
							var today=getNowDate();
							var colsingTime=ar3.Closing_Time;
							if(today>colsingTime){
								operStr="已截止生成考号"
							}else{
								operStr
								= "<p><button id='queryBTN' class='page-button' title='生成考号' onclick='queryOper(\""
									+ ar3.School_Year
									+ "\",\""
									+ ar3.School_Code
									+ "\",\""
									+ ar3.Exam_Type
									+ "\",\""
									+ ar3.Term
									+ "\",\""
									+ ar3.Exam_Number
									+ "\",\""
									+ ar3.Exam_Time
									+ "\",\""
									+ ar3.Grade_Code
									+ "\",\""
									+ ar3.ev
									+ "\");'><i class='fa fa-file-text' style='margin-right: 5px;'></i>生成考号</button></p>";
							}
							 return operStr;
						}else{
							var queryStuOper = "<p><button id='queryBTN' class='page-button' title='查看学生信息' onclick='queryStuOper(\""
								+ ar3.Exam_Number
								+ "\",\""
								+ ar3.Grade_Code
								+ "\",\""
								+ ar3.testCaseGeneration
								+ "\",\""
								+ ar3.notTestCaseGeneration
								+ "\",\"" 
								+ ar3.Closing_Time
								+ "\",\""
								+ ar3.ev
								+ "\");'><i class='fa fa-file-text' style='margin-right: 5px;'></i>查看学生信息</button></p>";
						return queryStuOper;
						}
					}
				} ];
		$(listId).jqGrid($.extend(defaultGridOpts, {
			url : listUrl,
			colModel : _colModel,
			pager : pagerId,  
			multiselect:false
		}));
		
		resizeFun();
		$.ajax({  
            type : "POST",  
            url : "../stuManagement/stuInfo.do?command=getSequenceBySchoolCode",  
            data : {},
            dataType:"JSON",
            async: false,
            success : function(data){
            	$("#schoolType").val(data[0].sequence);
            	//$("#schoolCode").val(data[0].schoolCode);
         }
       });
		var schoolType=$("#schoolType").val();
    	 if(schoolType=="4" || schoolType=="3" || schoolType=="2" ){
    		$("#chooseCourse").show();
    	}else{
    		$("#chooseCourse").hide();
    	}
    	 
    	 
		$('#examNumber').focus(function(){
	    	 $("#tbl").text("");
	     });
		
		//查询学生信息
		$("#fastSearchStu").click(function(){
			 var examNumber =$("#examNumbers").val();
				if(examNumber==""){
					examNumber =$("#queryParam").val();
				}
				var gradeCode=$("#gradeCode").val();
				if(gradeCode==""){
					gradeCode=$("#gradeId").val();
				}
			 var chooseCourse=$("#selectCourse option:selected").val();
			 var examNumberStates=$("#examNumberState").find("option:selected").val();
			 var nameOrXjfh=$("#nameOrXjfh").val();
			 var datas= {
				     examNumber: examNumber,
				     examNumberState:examNumberStates,
				     nameOrXjfh:nameOrXjfh,
				     gradeCode:gradeCode,
				     chooseCourse : chooseCourse,
				 };
			 $.ajax({
			     url: getImportParamsUrl,
			     type: "POST",
			     data: datas,
			     dataType: "JSON",
			     success: function (data) { 
			    	 $("#notTestCaseGeneration").val(data.notTestCaseGeneration);
			    	 $("#testCaseGeneration").val(data.testCaseGeneration);
			    	 if(getNowDate()>$("#closingTime").val()){
						  loadStuInfo2(examNumberStates);
						}else{
							loadStuInfo(examNumberStates);
						}
			   }
			 }); 
		});
		//点击生成考号
		$("#saveBTN2").click(function(){
			var stateCode=[];
			var txt = $("#stuState").find("option:selected").each(function() {
				stateCode.push($(this).val());
			});
			 $("#stuState").click(function(){
			 })
			showSlidePanel(".page_editor_panel2");
			if(stateCode.length<1){
				window.Msg.alert("请选择生成考号的条件...!");
			}else{
				$(".ui-effects-wrapper").css("display","none");
				var url = "../examNumberManagement/examNumberManage.do?command=queryDetailList";
				 var datas = {
					     schoolYear: $("#schoolYear").val(),
					     schoolCode: $("#schoolCode").val(),
					     examType:$("#examType").val(),
					     term:$("#term").val(),
					     examNumber:$("#examNumbers").val(),
					     examTime:$("#examTime").val(),
					     stateCode:stateCode,
					     gradeCode:$("#gradeCode").val(),
					 };
					 $.ajax({
					     url: url,
					     type: "POST",
					     data: datas,
					     dataType: "JSON",
					     beforeSend: function () {
                             window.message({
                                 text: "正在生成考号...",
                                 title: "提醒",
                             });
                         },
					     success: function (data) { 
					    	 if(data.message=="success"){
					    			//修改父页面div提示信息
                                 $('.ui-dialog-content.ui-widget-content', parent.document).html("考号生成完成");
					   	        	$("#queryParam").val(examNumber);
							     	$("#gradeId").val(gradeCode);
							     	 $("#testCaseGeneration").val(data.testCaseGeneration);
								     $("#notTestCaseGeneration").val(data.notTestCaseGeneration);
							     	$.ajax({
							     		url: getImportParamsUrl,
									     type: "POST",
									     data: datas,
									     dataType: "JSON",
									     success: function (data) { 
									      	hideSlidePanel(".page_editor_panel2");
									    	 loadStuInfo(examNumberStates);
									     }
							     	});
							     	$(stuInfolistId).trigger("reloadGrid");
			 		        		showSlidePanel(".page-editor-panel");	
					   	   }else{
					   		window.Msg.alert("考号生成失败,请重新生成!");
					   	   }
					   }
					 }); 
			}
		});
		
	}); 
           /* 得到是否存在考号的学生 */
          var  loadExitExamNumber=function(examNumber,gradeCode,testCaseGeneration,notTestCaseGeneration,examNumberStates){
        	  $("#examNumberState").empty();
              $("#examNumberState").append("<option value=''>全部</option>");
        	   $("#queryParam").val(examNumber);
      	      $("#gradeId").val(gradeCode);
       	     var datas = {
 				     examNumber: examNumber,
 				     gradeCode:gradeCode,
 				     testCaseGeneration:testCaseGeneration,
 				     notTestCaseGeneration:notTestCaseGeneration,
 				     examNumberState:examNumberStates
 				 };
 			$.ajax({
 				     		url: getImportParamsUrl,
 						     type: "POST",
 						     data: datas,
 						     dataType: "JSON",
 						    async:false,
 						     success: function (data) { 
 						    	 $("#testCaseGeneration").val(testCaseGeneration);
 								 $("#notTestCaseGeneration").val(notTestCaseGeneration);
 								  loadDictionary(function(){
 					                	$("#examNumberState").find("option[value='" + examNumberStates + "']").attr("selected", true);
 								        
 								  });
 								 if(getNowDate()>$("#closingTime").val()){
									    loadStuInfo2(examNumberStates);
									}else{
										loadStuInfo(examNumberStates);
									}
 						     }
 				     	});
           }
           //点击未生成考号人数
           function queryStuNotExamNumber(examNumber,gradeCode,testCaseGeneration,notTestCaseGeneration,closingTime,ev){
        	   var examNumberStates=0;
        	   $("#closingTime").val(closingTime);
        	   var $i = $(ev.currentTarget).find("i"), $piel = $(".page-editor-panel")
    		     .show({
    			effect : "slide",
    			direction : "up",
    			easing : 'easeInOutExpo',
    			duration : 900
    		});
        	   $("#examNumberState").find("option[selected=selected]").attr("selected",false);
        	  $("#examNumberState").find("option[value='"+examNumberStates+"']").attr("selected","selected");
        	 loadExitExamNumber(examNumber,gradeCode,testCaseGeneration,notTestCaseGeneration,examNumberStates);
         }
           //点击生成考号人数
           function queryStuIsExamNumber(examNumber,gradeCode,testCaseGeneration,notTestCaseGeneration,closingTime,ev){
        	 $("#closingTime").val(closingTime);
        	   var examNumberStates=1;
        	   var $i = $(ev.currentTarget).find("i"), $piel = $(".page-editor-panel")
         		.show({
         			effect : "slide",
         			direction : "up",
         			easing : 'easeInOutExpo',
         			duration : 900
         		});
        	   $("#examNumberState").find("option[selected=selected]").attr("selected",false);
        	   $("#examNumberState").find("option[value='"+examNumberStates+"']").attr("selected","selected");
        	   loadExitExamNumber(examNumber,gradeCode,testCaseGeneration,notTestCaseGeneration,examNumberStates);
           }
     //查看学生信息
	function queryStuOper(examNumber,gradeCode,testCaseGeneration,notTestCaseGeneration,closingTime,ev){
		$("#closingTime").val(closingTime);
		 $("#selectCourse option:selected").attr("selected",false);
		$("#examNumberState").find("option[selected=selected]").attr("selected",false);
    	 $("#examNumberState option[value='']").attr("selected",true);
    	 $("#queryParam").val(examNumber);
		$("#gradeId").val(gradeCode);
		 var datas = {
				     examNumber: examNumber,
				     gradeCode:gradeCode,
				     testCaseGeneration:testCaseGeneration,
				     notTestCaseGeneration:notTestCaseGeneration
				 };
			$.ajax({
				     		url: getImportParamsUrl,
						     type: "POST",
						     data: datas,
						     dataType: "JSON",
						     success: function (data) { 
						    	 $("#testCaseGeneration").val(testCaseGeneration);
								 $("#notTestCaseGeneration").val(notTestCaseGeneration);
								 if(getNowDate()>$("#closingTime").val()){
									 loadStuInfo2(examNumberStates);
									}else{
										loadStuInfo(examNumberStates);
									}
						      	
						     }
				     	});
		
		var $i = $(ev.currentTarget).find("i"), $piel = $(".page-editor-panel")
		.show({
			effect : "slide",
			direction : "up",
			easing : 'easeInOutExpo',
			duration : 900
		});
	}
   
	//加载jqgrid
	function loadStuInfo2(examNumberStates){
		var _caption="";
		var _colModels = [
			 				{
			 					name : 'id',
			 					key : true,
			 					width : 60,
			 					hidden : true,
			 					search : false
			 				},{
			 					label : "学籍号",
			 					name : 'XJFH',
			 					sortable : false,
			 					autoWidth : true,
			 					align : "center",
			 				},{
			 					label : "姓名",
			 					name : 'Stu_Name',
			 					sortable : false,
			 					width : 80,
			 					align : "center",
			 				},{
			 					label:"性别",
			 		            name:"Sex",
			 		            sortable:false,
			 		            width:60,
			 		            align:"center",
			 		           formatter : function(ar1, ar2, ar3) {
			 						if (ar3.Sex=="1") {
			 							return "男"
			 						}else if(ar3.Sex=="2") {
			 							return   "女"
			 						}else{
			 							return   "";
			 						}
			 					}
			 				},{
			 					label : "考号",
			 					name : 'Exam_Number',
			 					sortable : false,
			 				   	autoWidth : true,
			 					align : "center",
			 				},{
			 					label : "学籍状态",
			 					name : 'stuState',
			 					sortable : false,
			 					width:80,
			 					align : "center",
			 					formatter : function(ar1, ar2, ar3) {
			 						 return _stuState[ar3.stuState]
			 					}
			 					
			 				},{
			 					label : "班级",
			 					name : 'GradeName',
			 					sortable : false,
			 					width:80,
			 					align : "center",
			 					formatter : function(ar1, ar2, ar3) {
			 						var isXjb="";
			 						if(ar3.Is_Xjb=="1"){
			 							isXjb="<span style='color:red;'>(新疆班)</span>";
			 						}
			 						if (ar3.ClassName != "" || ar3.Class_Id != null) {
			 							return (_types[ar3.GradeName] + "(" + ar3.Class_Id
			 									+ ")班"+isXjb || "");
			 						} else {
			 							return (_types[ar3.GradeName]+isXjb);
			 						}
			 					}
			 				},{
		 						label : "",
			 					name : 'ClassName',
			 					hidden : true,
			 					align : "left",
			 			  } ,{
			 				    label : "",
				 				name : 'GradeName',
				 				hidden : true,
				 				align : "left",
				 		}];
		   if(examNumberStates=="1"){
        	   _caption="已分配人"+$('#testCaseGeneration').val();
           }else if(examNumberStates=="0"){
        	   _caption="未分配人数"+$('#notTestCaseGeneration').val()
           }else if(examNumberStates==""){
        	   _caption="已分配人"+$('#testCaseGeneration').val()+",未分配人"+$('#notTestCaseGeneration').val();
           }
	               
		            //重新创建列表
		              $(stuInfolistId).GridUnload();
		            $(stuInfolistId3).GridUnload();
			 		$(stuInfolistId3).jqGrid($.extend(defaultGridOpts, {
			 			url : stuIfnoListUrl,
			 			colModel : _colModels,
			 			pager : stuInfoPagerId3,
						height : "100%",
						multiselect : true,
					    width: $("body").innerWidth() - 20,
					    autowidth: false,
					    shrinkToFit: true,
						multiselect:false,
						caption :_caption,
	}));
			 		resizeFun("495");
	};
	
	
	
	//加载jqgrid
	function loadStuInfo(examNumberStates){
		var _caption="";
		var _colModels = [
			 				{
			 					name : 'id',
			 					key : true,
			 					width : 60,
			 					hidden : true,
			 					search : false
			 				},{
			 					label : "学籍号",
			 					name : 'XJFH',
			 					sortable : false,
			 					/*  width:230, */
			 					autoWidth : true,
			 					align : "center",
			 				},{
			 					label : "姓名",
			 					name : 'Stu_Name',
			 					sortable : false,
			 					width : 80,
			 					align : "center",
			 				},{
			 					label:"性别",
			 		            name:"Sex",
			 		            sortable:false,
			 		            width:60,
			 		            align:"center",
			 		           formatter : function(ar1, ar2, ar3) {
			 						if (ar3.Sex=="1") {
			 							return "男"
			 						}else if(ar3.Sex=="2") {
			 							return   "女"
			 						}else{
			 							return   "";
			 						}
			 					}
			 				},{
			 					label : "考号",
			 					name : 'Exam_Number',
			 					sortable : false,
			 				   	autoWidth : true,
			 					align : "center",
			 					editable:true,
			 				},{
			 					label : "学籍状态",
			 					name : 'stuState',
			 					sortable : false,
			 					width:80,
			 					align : "center",
			 					formatter : function(ar1, ar2, ar3) {
			 						 return _stuState[ar3.stuState]
			 					}
			 				},{
			 					label : "班级",
			 					name : 'GradeName',
			 					sortable : false,
			 					width:80,
			 					align : "center",
			 					formatter : function(ar1, ar2, ar3) {
			 						var isXjb="";
			 						if(ar3.Is_Xjb=="1"){
			 							isXjb="<span style='color:red;'>(新疆班)</span>";
			 						}
			 						if (ar3.ClassName != "" || ar3.Class_Id != null) {
			 							return (_types[ar3.GradeName] + "(" + ar3.Class_Id
			 									+ ")班"+isXjb || "");
			 						} else {
			 							return (_types[ar3.GradeName]+isXjb);
			 						}
			 					}
			 				},{
		 						label : "",
			 					name : 'ClassName',
			 					hidden : true,
			 					align : "left",
			 			  } ,{
			 				    label : "",
				 				name : 'GradeName',
				 				hidden : true,
				 				align : "left",
				 		}];
		
		
		   if(examNumberStates=="1"){
        	   _caption="已分配人"+$('#testCaseGeneration').val();
           }else if(examNumberStates=="0"){
        	   _caption="未分配人数"+$('#notTestCaseGeneration').val()
           }else if(examNumberStates==""){
        	   _caption="已分配人"+$('#testCaseGeneration').val()+",未分配人"+$('#notTestCaseGeneration').val();
           }
	               
		            //重新创建列表
		            $(stuInfolistId3).GridUnload();
		            $(stuInfolistId).GridUnload();
			 		$(stuInfolistId).jqGrid($.extend(defaultGridOpts, {
			 			url : stuIfnoListUrl,
			 			cellurl:updateUrl,
			 			colModel : _colModels,
			 			pager : stuInfoPagerId,
						height : "100%",
						multiselect : true,
						cellEdit:true,
					    width: $("body").innerWidth() - 20,
					    autowidth: false,
					    shrinkToFit: true,
						cellsubmit: 'clientArray',
						multiselect:false,
						caption :_caption,
						//修改考号
						beforeEditCell: function (rowid, cellname, value, iRow, iCol) {
                     	      var rec =$(stuInfolistId).jqGrid('getRowData', rowid);
                     	     if (value.length > 8) {
                                 setTimeout(function () {
                                     $(stuInfolistId).jqGrid('restoreCell', iRow, iCol);
                                 }, 1);
                             }
      					},
						formatCell:function(rowid, cellname, value, iRow, iCol){
        	 					var rowData = $(stuInfolistId).jqGrid("getRowData",rowid);
        	 					var examNumber=$("#examNumbers").val();
        						if(examNumber==""){
        		 						examNumber= $("#queryParam").val()
        	 					}
        	 					var data={classId:rowData.ClassName,gradeId:rowData.GradeName,examNumber:examNumber};
        					    $.ajax({
	 	            						url: getStuExamNumberUrl,
	 	          						  	type: "POST",
	 	            						data:data,
	 	 	        						dataType: "json",
	 	 	        						async:false,
	 	 	        						success : function (data) {
	 	 	        						$("#param").val(data);
	 	 	        						}
        						});
        						 if(value==""){
	        		 					return  $("#param").val();
	     						 } 
        						 if(value.length<9 && value.length>0){
        								 return  $("#param").val();
        	 						}
        					},
    					afterSaveCell : function(rowid,name,val,iRow,iCol) {
        	         			var id=rowid;
				     			var examNumber = val;
				     			if(name == 'Exam_Number') {
						   			if(examNumber.length!=9 ){
									window.Msg.alert("该考号不符合,请重新修改");
							  		val="";
									$(stuInfolistId).restoreCell(iRow,iCol)
									return;
				         		}else if(examNumber.length==9){
							 		 var s=examNumber.substring(0,6);
							  		if(s!=$("#param").val()){
								  		window.Msg.alert("该考号不符合,请重新修改");
								  		val="";
										$(stuInfolistId).restoreCell(iRow,iCol)
										return;
						 				}
								}
						   
						  	 var datas = {id:id,examNumber:examNumber};
						      var returnVal=true;
						 			$.ajax({
						 	            url: verifyCodeUrl,
						 	            type: "POST",
						 	            data: datas,
						 	 	        dataType: "json",
						 	 	        async:false,
						 	 	        success : function (data) {
						 	            	returnVal=data.flag;
						 	            if(returnVal==false){
							 						$("#examNumberState").find("option:selected").val();
							 						window.Msg.alert("考号已存在");
							 						$(stuInfolistId).restoreCell(iRow,iCol);
							 					  return;
						 	 	         }else{
						 	 	            	  $.ajax({
							 				   	       	url:updateUrl,
							 				   	        type:"POST",
							 				   	        data:datas,
							 				   	        dataType:"JSON",
							 				   	        success: function(data, xhr) {
							 				   	        if(data.mess=="success"){
							 				   	        	window.Msg.alert("修改成功!");
							 				   	       }
							 				   	        $('.input ').val(""); 
							 							$("#examNumberState").find("option:selected").attr("selected",false);
							 							$(stuInfolistId).trigger("reloadGrid");
							 				   	        } 
									        });
						 	 	            	}
						 	            	}
						 	        }); 
			}
	 }
	}));
			 		resizeFun("495");
	};
	
	//查看附件
	function queryFileOper(examCode,ev){
		var $i = $(ev.currentTarget).find("i"), $piel = $(".page_file_panel")
		.show({
			effect : "slide",
			direction : "up",
			easing : 'easeInOutExpo',
			duration : 900
		});
		 POST(lookUrl,{examCode: examCode},function (data) {
			 $("#filesList").empty();
			 var fileStr="";
			 for(var i=0;i<data.length;i++){
			   fileStr+="<a class='file-name' data-fid=\""+data[i].id+"\" href='<%=request.getContextPath()%>/platform/accessory_.do?command=download&id="+data[i].id+" '>"+data[i].FileName+"</a><p/>";
			 }
			 $("#filesList").append(fileStr);
		 });
	}
	
	//生成考号
	function queryOper(schoolYear, schoolCode, examType, term, examNumber,
			examTime,gradeCode, ev) {
		var $i = $(ev.currentTarget).find("i"), $piel = $(".page_editor_panel2")
		.show({
			effect : "slide",
			direction : "up",
			easing : 'easeInOutExpo',
			duration : 900
		});
		$("#stuState").empty();
		$("#examNumberState").empty();
        $("#examNumberState").append("<option value=''>全部</option>");
		 loadDictionary(function(){
	        $("#stuState").find("option[value='" + stuState + "']");
	        $("#stuState option").attr("selected",true);
	  		$("#stuState").multiselect({
	  		checkAllText : "全选",
	  		uncheckAllText : "全不选",
	  		noneSelectedText : "选择生成考号的条件",
	  		selectedText : '#' + " 条件",
	  		selectedList : 2
	    	});
	  	
	  	 $("#stuState").multiselect('refresh');
	    });
		$("#schoolYear").val(schoolYear);
		$("#schoolCode").val(schoolCode);
		$("#examType").val(examType);
		$("#term").val(term);
		$("#examNumbers").val(examNumber);
		$("#examTime").val(examTime);
		$("#gradeCode").val(gradeCode);
	 
   }
	
	
	//导出
	    var exportExcel = function(){
		var examCode =$("#examNumbers").val();
		if(examCode==""){
			 examCode =$("#queryParam").val();
		}
		var gradeId=$("#gradeCode").val();
		if(gradeId==""){
			 gradeId=$("#gradeId").val();
		}
		var chooseCourse=$("#selectCourse option:selected").val();
		var stuNameOrXjfh=$("#nameOrXjfh").val();
		 var examNumberState=$("#examNumberState").find("option:selected").val();
		var gradeName=_types[gradeId];
		var data={
				examCode:examCode,
				gradeId:gradeId,
				gradeName:gradeName,
				stuNameOrXjfh:stuNameOrXjfh,
				examNumberState:examNumberState,
				chooseCourse : chooseCourse,
		}
	
		 var url = "../examNumberManagement/examNumberManage.do?command=exportExcel&data="+JSON.stringify(data);
		 var form = $( "#fileReteExportExcel" ) ;
			form.attr( "action", url) ;
			form.get( 0 ).submit() ; 
	}
</script>
</head>
<body>
<form id="fileReteExportExcel" method="post"></form>
	<div class="page-list-panel">
		<div class="head-panel">
			<div class="toolbar">
				<table style="height: 100%;" cellpadding="0" cellspacing="0"
					border="0">
					<tr>
						<td style="padding-left: 12px; padding-right: 24px;"><i
							class="fa fa-list-ul micon"></i></td>
						<td style="padding-left: 24px; padding-right: 5px;">
						<input id="fastQueryText" type="text" placeholder="输入测试名称"
							style="line-height: 1em; font-size: 1em; cursor: text;" /></td>
						<input type="hidden" id="schoolType">
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
		    <input id="testCaseGeneration"  type="hidden"/>
			<input id="notTestCaseGeneration" type="hidden"/>
			<input id="param" type="hidden"/>
			<table style="height: 100%;" cellpadding="0" cellspacing="0" border="0">
					<tr>
						<td style="padding-left: 12px; padding-right: 24px;"><i class="fa fa-list-ul micon"></i></td>
					<td style="  padding-right: 5px;"> 
					    姓名或学籍号：
						<input class="input" id="nameOrXjfh"  placeholder="输入姓名或学籍号" type="text"  style="line-height: 1em; font-size: 1em; cursor: text;" />
					  </td>
					<td style=" padding-right: 5px;" id="isExamNumberState" > 是否分配考号
						<select id="examNumberState"    style="width:95px;height:26px;"   data-dic="{code:'examNumberState'}"   class="form-control"   >
						    <option value="">全部</option>
						 </select>
					 </td>
					 <td id="chooseCourse" style=" padding-right: 5px;"> 选科科目:
						<select id="selectCourse"  data-dic="{code:'chooseCourse'}" style="width:95px;height:26px;"   class="form-control"  >
						    <option value="">选择科目</option>
						 </select>
					 </td>
					<td>
				    	<input  id="gradeId"  type="hidden"  />
						<input id="queryParam" type="hidden"  style="line-height: 1em; font-size: 1em; cursor: text;" />
					</td>
					<td>
							<button id="fastSearchStu" title="查询" style="margin-left: 0px;">
								<i class="fa fa-search"></i>查询
							</button>
					        <button id="deriveStuInfo" onclick="exportExcel()">
					    	<i class="fa fa-paperclip"></i>导出学生信息
					        </button>
					<button id="backBTN">
						<i class="fa fa-reply"></i>返回
					</button>
					</td>
					</tr>
				</table>
			<div class="btn-area">
				<div style="margin-top: 4px;">
				</div>
			</div>
		</div>
		<div class="page-content">
			<div class="page-inner-content">
		    <table id="stuInfoList2"></table>
		   <div id="stuInfopager"></div>
		   
		   <table id="stuInfoList3"></table>
		   <div id="stuInfopager3"></div>
			</div>
		</div>
	</div>

<!-- 更新 -->
<div class="page-update-panel full-drop-panel">
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
		                <thead>
							<tr>
								<th colspan="4"  style="color: black;">
									<i class="fa fa-file-text"></i>
									<span>修改考号</span>
								</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td class="label" style="width:15%"><span style="color:red;">*</span>考号：</td>
								<td style="width:25%">
								<input data-xtype="text" name="examNumber" id="examNumber"  />
								<span style="color:red;" id="tbl"></span>
								</td>
							</tr>
		      	 </table>
			</form>
			</div>
		</div>
	</div>

<div class="page_editor_panel2 full-drop-panel">
<div class="title-bar">
			<h4>
				<i class="fa fa-plus"></i>
			</h4>
			<div class="btn-area">
				<div style="margin-top: 4px;">
					<button id="saveBTN2">
						<i class="fa fa-check"></i>生成
					</button>
					<button id="cancelBTN" >
						<i class="fa fa-times"></i>取消
					</button>
				</div>
			</div>
		</div>
		<div class="page-content">
			<div class="page-inner-content" style="margin: auto;">
			  	<form id="caseEditorForm2">
			  	                 <input type="hidden"  id="schoolYear" />
						     	 <input type="hidden"  id="schoolCode" />
							    <input type="hidden"  id="examType" /> 
								 <input type="hidden"  id="term" />
								 <input type="hidden"  id="examNumbers" />
								 <input type="hidden"  id="examTime" />
								 <input type="hidden"  id="stateCode" />
								<input type="hidden"  id="gradeCode" />
								<input type="hidden"  id="closingTime" />
				    <table cellspacing="0" border="0" style="width:94.7%;" class="tableTemplet">
		                <thead>
							<tr>
								<th colspan="4"  style="color: black;">
									<i class="fa fa-file-text"></i>
									<span>生成考号</span>
								</th>
							</tr>
						</thead>
						
						<tbody>
							<tr>
								<td class="label" style="width:15%">生成条件：</td>
								<td style="width:25%">
								     <select id="stuState"  data-dic="{code:'stuState'}"  multiple="multiple" class="form-control" style="width: 370px; height: 25px;">
								     </select>
								</td>
								<td><span style="color: red;" id="tal"></span></td>
							</tr>
		      	 </table>
			</form>
			</div>
		
		
		
		</div>

</div>


<div class="page_file_panel full-drop-panel">
<div class="title-bar">
			<h4>
				<i class="fa fa-plus"></i>
			</h4>
			<div class="btn-area">
				<div style="margin-top: 4px;">
					<button id="backBTNs" >
						<i class="fa fa-times"></i>返回
					</button>
				</div>
			</div>
		</div>
		<div class="page-content">
			<div class="page-inner-content" style="margin: auto;">
			  	<form id="d">
				    <table cellspacing="0" border="0" style="width:94.7%;" class="tableTemplet">
		                <thead>
							<tr>
								<th colspan="4"  style="color: black;">
									<i class="fa fa-file-text"></i>
									<span>下载附件</span>
								</th>
							</tr>
						</thead>
						
						<tbody>
						<tr height="50">
							<td class="label">相关附件：</td>
							<td>
								<div id="filesList" style="word-break: break-all;"></div>
							</td>
						</tr>
						</tbody>
		      	 </table>
			</form>
			</div>
		</div>
</div>
</body>
</html>