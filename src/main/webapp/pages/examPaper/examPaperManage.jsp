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
<script type="text/javascript" src="../js/util.js"></script>

<style>
 .customStyle{
		background:#FFFFFF;
		border:1px solid #3babe3;
		color:#3babe3;
		cursor:pointer;
		font-weight: bold;
		padding: .4em 1em;
	}
	.customStyle:hover{
		color:#FFFFFF;
		background:#3babe3;
		}
 	#tblInfo input{
		background-color: #F6F6F6;
	} 
	#stu_look input{
		width:100%;
		height:100%;
		border:1px;
		text-align: center;
		background-color: white;
	}
	#stu_look td{
		height:30px;
	}
	#tabs-2{height: 847px;}
	
	.condition  td{
		height:50px;
	}
	
</style>
<script type="text/javascript">
	var listId = "#list2",editorFormId = "#editorForm", pagerId = '#pager2',editorRelatedFormId = "#editorRelatedForm",
	
	listUrl = "../examPaper/examPaperManage.do?command=searchPaging",detailListUrl="../dataManage/smallTitle.do?command=queryDetailList";
	var cancelBTN = function() {
		hideSlidePanel(".page-editor-panel");
	}
	$(function() {
		//$(".search-panel").show().data("show", true);
		 
		_initButtons({
			cancelBTN : function() {
				hideSlidePanel(".page-editor-panel");
			},
			
		});//from page.common.js
		$("#tblInfo").find("button").button();
		 $( "#tabs" ).tabs( { heightStyle:"fill" } ) ;
		 $("#tabs-2,#tabs-1").css("height","auto");
		_initFormControls();//from page.common.js
		_initValidateForXTypeForm(editorFormId);
		
		
		//绑定jqgrid
		 var _colModel = [{
             name: 'Id',
             key: true,
             width: 60,
             hidden: true,
             search: false
         }, {
             name: 'Exampaper_Name',
             sortable: false,
             autoWidth: true,
             align: "left",

         }, {
             name: 'School_Year',
             sortable: false,
             width: 120,
             align: "center",
         },{
             name: 'Term',
             sortable: false,
             width: 120,
             align: "center",
             formatter: function (ar1, ar2, ar3) {
                 return _types[ar3.Term] || "";
             }
         },{
             name: 'Grade',
             sortable: false,
             width: 120,
             align: "center",
             formatter: function (ar1, ar2, ar3) {
                 return _types[ar3.Grade] || "";
             }
         },{
        	 name: 'Exam_Type',
        	 width: 120,
        	 sortable: false,
             align: "center",
             formatter: function (ar1, ar2, ar3) {
                 return _types[ar3.Exam_Type] || "";
             }
         },{
        	 name: 'Exampaper_Type',
        	 width: 120,
        	 sortable: false,
             align: "center",
             formatter: function (ar1, ar2, ar3) {
                 return _types[ar3.Exampaper_Type] || "";
             }
         },{
        	 name: 'Course',
        	 width: 120,
        	 sortable: false,
             align: "center",
             formatter: function (ar1, ar2, ar3) {
                 return _types[ar3.Course] || "";
             }
         }
         ],
         _colNames = ['','试卷名称', '年度', '学期','年级','测试类型','试卷类型','科目'];

     $(listId).jqGrid($.extend(defaultGridOpts, {
         url: listUrl,
         colNames: _colNames,
         colModel: _colModel,
         pager: pagerId
     }));
     resizeFun();
     
     loadDictionary();
     
     //选择学校类型关联科目
     /*$("#schoolType").change(function(){
    	 //$("#course").find("option").attr("value").remove();
    	 $("#course option[value != '']").remove();
    	 var schoolType = $("#schoolType").val();
    	 var url = "../platform/dictionary.do?command=getCoursesByCode";
    	 data = {schoolType:schoolType};
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
     });*/
   
   //选择年级关联科目
     /*$("#grade").change(function(){
    	 //$("#course").find("option").attr("value").remove();
    	 $("#course option[value != '']").remove();
    	 var grade = $("#grade").val();
    	 var url = "../platform/dictionary.do?command=getCoursesByGradeCode";
    	 data = {grade:grade};
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
     });*/
    
     //加载数据字典
     /*$.ajax({
         url: "../platform/dictionary.do?command=getDataDictionaryByCode",
         type: "POST",
         data: {schoolYear:"ND",schoolType:"xxlx",term:"xq",examType:"kslx"},
         dataType:"JSON",
         success: function (data) {
        	 	 
    			 $("#schoolType").append("<option value='"+data[1].DictionaryCode+"'>"+data[1].DictionaryName+"</option>");
    			 $("#schoolType").append("<option value='"+data[14].DictionaryCode+"'>"+data[14].DictionaryName+"</option>");
    			 $("#schoolType").append("<option value='"+data[18].DictionaryCode+"'>"+data[18].DictionaryName+"</option>");
    			 $("#term").append("<option value='"+data[15].DictionaryCode+"'>"+data[15].DictionaryName+"</option>");
    			 $("#term").append("<option value='"+data[4].DictionaryCode+"'>"+data[4].DictionaryName+"</option>");
    			 $("#examType").append("<option value='"+data[2].DictionaryCode+"'>"+data[2].DictionaryName+"</option>");
    			 $("#examType").append("<option value='"+data[11].DictionaryCode+"'>"+data[11].DictionaryName+"</option>");
    			 $("#schoolYear").append("<option value='"+data[20].DictionaryCode+"'>"+data[20].DictionaryName+"</option>");
    			 $("#schoolYear").append("<option value='"+data[5].DictionaryCode+"'>"+data[5].DictionaryName+"</option>");
    			 $("#schoolYear").append("<option value='"+data[8].DictionaryCode+"'>"+data[8].DictionaryName+"</option>");
    			 $("#schoolYear").append("<option value='"+data[10].DictionaryCode+"'>"+data[10].DictionaryName+"</option>");
    			 $("#schoolYear").append("<option value='"+data[21].DictionaryCode+"'>"+data[21].DictionaryName+"</option>");
    			 $("#schoolYear").append("<option value='"+data[19].DictionaryCode+"'>"+data[19].DictionaryName+"</option>");
    			 $("#schoolYear").append("<option value='"+data[7].DictionaryCode+"'>"+data[7].DictionaryName+"</option>");
    			 $("#schoolYear").append("<option value='"+data[9].DictionaryCode+"'>"+data[9].DictionaryName+"</option>");
    			 $("#schoolYear").append("<option value='"+data[16].DictionaryCode+"'>"+data[16].DictionaryName+"</option>");
    			 $("#schoolYear").append("<option value='"+data[0].DictionaryCode+"'>"+data[0].DictionaryName+"</option>");
    			 $("#schoolYear").append("<option value='"+data[13].DictionaryCode+"'>"+data[13].DictionaryName+"</option>");
    			 $("#schoolYear").append("<option value='"+data[12].DictionaryCode+"'>"+data[12].DictionaryName+"</option>");
    			 $("#schoolYear").append("<option value='"+data[6].DictionaryCode+"'>"+data[6].DictionaryName+"</option>");
    			 $("#schoolYear").append("<option value='"+data[3].DictionaryCode+"'>"+data[3].DictionaryName+"</option>");
    			 $("#schoolYear").append("<option value='"+data[17].DictionaryCode+"'>"+data[17].DictionaryName+"</option>");
         }
     });*/
     
   //通过学校类型(xx,cz,gz)得到科目
     $.ajax({
         url: "../statisticsAnalysis/politicalInstructor.do?command=getCoursesBySchoolType",
         type: "POST",
         data: {},
         dataType: "JSON",
         success: function (data) {
             for (var i = 0; i < data.length; i++) {
                 $("#course").append("<option value='" + data[i].DictionaryCode + "'>" + data[i].DictionaryName + "</option>");
             }
         }
     });
    
     //添加
     $("#saveBTN").click(function(){
    	 var schoolType = $("#schoolType").val();
    	 var url = "../examPaper/examPaperManage.do?command=addExamPaper";
    	 var data = {
    		id:$("#hiddenId").val(),	 
    		examPaperName:$("#examPaperName").val(),
    		examType:$("#examType").val(),
    		schoolYear:$("#schoolYear").val(),
    		examPaperType:$("#examPaperType").val(),
    		grade:$("#grade").val(),
    		term:$("#term").val(),
    		course:$("#course").val()
    	 };
    	 $.ajax({
             url: url,
             type: "POST",
             data: data,
             dataType: "JSON",
             success: function (data) {
            	if(data.mess == 'addSuccess'){
            		window.Msg.alert("添加成功");
            	}
            	if(data.mess == 'updateSuccess'){
            		window.Msg.alert("修改成功");
            	}
            	$(listId).trigger("reloadGrid");
            	hideSlidePanel(".page-editor-panel");
             }
         });
     });
     
     //删除
     $("#deleteOper").click(function(){
    	 var selArr = $(listId).jqGrid('getGridParam','selarrrow');
    	 if(selArr.length == 0){
    		 window.Msg.alert("请选择要删除的记录");
    		 return;
    	 }
    	 var url = "../examPaper/examPaperManage.do?command=deleteExamPaper";
    	 window.message({
    			title:'提醒',
    			text:'确定删除此试卷吗?',
    			buttons:{
    				'确定':function(){
    					window.top.$(this).dialog("close");
    					 $.ajax({
    				   	       	url:url,
    				   	        type: "POST",
    				   	        data: {selArr:selArr},
    				   	        dataType: "JSON",
    				   	        success: function(data, xhr) {
    				   	        	if(data.mess == 'success'){
    				   	        		window.Msg.alert("删除成功!");
    				   	        	}
    				   	        	$(listId).trigger("reloadGrid");
    				   	       }
    				   	    });  
    				},
    				'取消':function(){
    					window.top.$(this).dialog("close");
    				}
    			}		
    		});
     });
});
		
		/*window.onload = function(){
			//显示学年为当前年
		     var year = new Date().getFullYear();
		     var month = new Date().getMonth()+1;
		     if(month < 9){
		    	 $("#schoolYear option:selected").html((year-1)+"-"+year);
		    	 $("#schoolYear option:selected").attr("value",(year-1)+"-"+year);
		    	
		     } else {
		    	 $("#schoolYear option:selected").html(year+"-"+(year+1));
		    	 $("#schoolYear option:selected").attr("value",year+"-"+(year+1));
		     }
		}*/
		//修改
		function updateOper(ev){
			 var selArr = $(listId).jqGrid('getGridParam','selarrrow');
	    	 if(selArr.length == 0){
	    		 window.Msg.alert("请选择要修改的记录");
	    		 return;
	    	 }
	    	 if(selArr.length > 1){
	    		 window.Msg.alert("每次只能修改单条记录");
	    		 return;
	    	 }
			var url = "../examPaper/examPaperManage.do?command=SelectExamPaperById";
			var id = $(listId).jqGrid('getGridParam','selrow');
			var data = {id:id};
			 $.ajax({
	             url: url,
	             type: "POST",
	             data: data,
	             dataType: "JSON",
	             success: function (data) {
	            	 $("#course option[value != '']").remove();
	            	//回显数据{Course=wl, School_Year=2015-2016, Grade=bnj, Exampaper_Name=北大联考, Create_Time=2016-08-14 02:09:00.0, Id=F4145C37-E047-49EF-B7D8-AED8EF617CE0, Exam_Type=qm, Exampaper_Type=, Create_Person=238F62E4-AFBE-4976-AF3B-B1F1417CD13D, Term=sxq}
	            	$("#hiddenId").val(data.Id);
	            	$("#examPaperName").val(data.Exampaper_Name);
		    		$("#examType").val(data.Exam_Type);
		    		$("#schoolYear").val(data.School_Year);
		    		$("#examPaperType").val(data.Exampaper_Type);
		    		$("#grade").val(data.Grade);
		    		$("#term").val(data.Term);
		    		$("#course").val(data.Course);
		    		//$("#course option[value='"+data.Course+"']").attr("selected","selected");
		    		var course = data.Course;
		    		//alert(course);
			       	 var grade = $("#grade").val();
			       	 var url = "../platform/dictionary.do?command=getCoursesByGradeCode";
			       	 data = {grade:grade};
			       	 $.ajax({
			                url: url,
			                type: "POST",
			                data: data,
			                dataType: "JSON",
			                success: function (data) {
			               	 for(var i = 0;i < data.length;i++){
			           			 $("#course").append("<option value='"+data[i].DictionaryCode+"'>"+data[i].DictionaryName+"</option>");
			           		 }
			               	$("#course option[value='"+course+"']").attr("selected","selected");
			                }
			            });
			       	 //
			       		
			       		//$("#course").val(data.Course);
				       	var $i = $(ev.currentTarget).find("i"),
			             $piel = $(".page-editor-panel").show({
			                 effect: "slide",
			                 direction: "up",
			                 easing: 'easeInOutExpo',
			                 duration: 900
			             });
	             }
	         });
		}
		
</script>
</head>
<body>

<form id="fileForm" enctype="multipart/form-data"  method="post" style="display:none;">
	<input type="file" name="xlsFile" onchange="importNewStu(this);"/>
</form>
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
							<button id="editOper" onclick="updateOper('+ev+')">
								<i class="fa fa-pencil"></i>修改
							</button>
							<button id="deleteOper">
								<i class="fa fa-trash-o"></i>删除
							</button>
						</td>
						<td style="padding-left: 24px; padding-right: 5px;">
							<input
							id="fastQueryText" type="text" placeholder="输入试卷名称"
							style="line-height: 1em;margin-left:-15px;font-size: 1em; cursor: text;" />
						</td>
						<td>
							<button id="fastSearch" title="查询" style="margin-left:0px;">
								<i class="fa fa-search"></i>查询
							</button>
							
							<!-- 高级查询 -->
							<!-- <button id="searchRip" title="点击展开高级查询面板">
								<i class="fa fa-angle-up" style="margin-right:0px;"></i>
							</button>-->
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
				<i class="fa  fa-pencil"></i>
			</h4>
			<div class="btn-area">
				<div style="margin-top: 4px;">
					<button id="saveBTN">
						<i class="fa fa-check"></i>保存
					</button>
					<button id="cancelBTN"  onclick="cancelBTN()">
						<i class="fa fa-times"></i>取消
					</button>
				</div>
			</div>
		</div>
		<div class="page-content">
			<div class="page-inner-content">
				<form id="editorForm">
					<table class="condition" cellpadding="0" cellspacing="0" border="0" style="margin-left:350px;margin-top:30px;">
						<tr>
							<td colspan="2">
								<input type="hidden" id="hiddenId"/>	
								试卷名称
									<input type="text" id="examPaperName" name="examPaperName" class="form-control" style="width:365px;"/>
							</td>		
							
						</tr>
						<tr>
							<td style="padding-left:25px;">	
								学年
									<select id="schoolYear" name="schoolYear" data-dic="{code:'ND'}" class="form-control" style="width:150px;">
						
									</select>
										
							</td>
							<td>		
								学期
									<select id="term" name="term" class="form-control" data-dic="{code:'xq'}" style="width:150px;">
									</select>
							</td>	
							
							
								
						</tr>
						
						<tr>
							<td>		
								试卷类型
									<select id="examPaperType" name="term" class="form-control" data-dic="{code:'sjlx'}" style="width:150px;">
										<option value="">--请选择--</option>
									</select>
									<span style="color:red;font-weight:bold;">*可选</span>
							</td>
							<td>		
								年级
									<select id="grade" name="grade" data-dic="{code:'nj'}" class="form-control" style="width:150px;">
										<option value="">请选择年级</option>
									</select>
							</td>		
						</tr>
						
						<tr>
							<td>	
								测试类型
									<select id="examType" name="examType" class="form-control" data-dic="{code:'kslx'}" style="width:150px;">
									</select>	
							</td>
							
							<td>
								科目
									<select id="course" name="course" class="form-control"  style="width:150px;">
										<option value="">请选择科目</option>
									</select>
							</td>		
						</tr>
					</table>
				</form>
			</div>
		</div>
	</div>
</body>
</html>