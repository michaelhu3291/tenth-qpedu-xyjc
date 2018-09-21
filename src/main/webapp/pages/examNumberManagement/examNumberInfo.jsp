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
	<style>
		#add_exam_info{margin-left:350px;}
		#add_exam_info ul li{list-style:none;margin-top:30px;}
	</style>
<script type="text/javascript">
var editorFormId = "#editorForm", 
		stuInfolistId = "#stuInfoList2";
		stuInfoPagerId = "#stuInfopager",
		stuIfnoListUrl = "../examNumberManagement/examNumberInfo.do?command=searchPaging";
		listUrl = "../examNumberManagement/examNumberManage.do?command=searchPaging";
 		loadUrl="../examNumberManagement/examNumberInfo.do?command=loadExamNumberById";
 		updateUrl="../examNumberManagement/examNumberInfo.do?command=updateExamNumberById";

 		_initButtons({
			cancelBTN : function() {
				$(".ui-effects-wrapper").css("display","none");
				hideSlidePanel(".page-update-panel,.page_editor_panel2");
			},
			backBTN :function(){
				hideSlidePanel(".page-editor-panel,.page-update-panel");
			},
			updateExamNumber : function(ev) {
				$(".ui-effects-wrapper").css("display","block");
				$("#caseEditorForm").resetHasXTypeForm();
				  $('#examNumber').blur(function () { 
			    	  var examNumber=$("#examNumber").val();
			    	  var id=$("#id").val();
			    	  var returnVal=true;
			 		    var verifyCodeUrl="../examNumberManagement/examNumberManage.do?command=examNumberIsExist";
			 			$.ajax({
			 	            url: verifyCodeUrl,
			 	            type: "POST",
			 	            data: {examNumber:$("#examNumber").val()
			 	            	     ,id:$("#id").val()},
			 	 	        dataType: "json",
			 	 	        async:false,
			 	 	        success : function (data) {
			 	            	returnVal=data.flag;
			 	            	returnVal=data.flag;
			 	            	if($("#examNumber").val()=="" || $("#examNumber").val()==null){
			 	            	$("#tbl").text("考号不能为空");
			 	            	}else{
			 	            		if(returnVal==false){
			 	            			$("#tbl").text("考号已存在");
			 	 	            	}else{
			 	 	            		$("#tbl").text("");
			 	 	            	}
			 	            	}
			 		        }
			 	        }); 
			 } );
			
				var $i = $(ev.currentTarget).find("i"),
 				idAry = $(stuInfolistId).jqGrid("getGridParam", "selarrrow");
 		        if (idAry.length === 0) {
 		            window.message({
 		                text: "请选择要修改的记录!",
 		                title: "提示"
 		            });
 		            return;
 		        }
 		        if (idAry.length > 1) {
 		            window.message({
 		                text: "每次只能修改单条记录!",
 		                title: "提示"
 		            });
 		            return;
 		        }
 		  GET(loadUrl, {id: idAry[0]}, function (data) {
 		        	for(var i=0;i<data.length;i++){
 		        		   $("#id").val(data[i].id);
 		        		   $("#examNumber").val(data[i].Exam_Number);
 		        		  showSlidePanel(".page-update-panel");
 		        	}
 		        }); 
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
			 					width : 100,
			 					align : "center",
			 				},{
			 					label : "考号",
			 					name : 'Exam_Number',
			 					sortable : false,
			 					autoWidth : true,
			 					align : "center",
			 				},{
			 					label : "学籍状态",
			 					name : 'STATE',
			 					sortable : false,
			 					autoWidth : true,
			 					align : "center",
			 				},{
			 					label : "班级",
			 					name : 'Grade_Id',
			 					sortable : false,
			 					autoWidth : true,
			 					align : "center",
			 					formatter : function(ar1, ar2, ar3) {
			 						if (ar3.className != "" || ar3.Class_Id != null) {
			 							return (_types[ar3.Grade_Id] + "(" + ar3.Class_Id
			 									+ ")班" || "");
			 						} else {
			 							return (_types[ar3.Grade_Id]);
			 						}

			 					}
			 				} ];
		            //重新创建列表
		          $(stuInfolistId).GridUnload();
			 		$(stuInfolistId).jqGrid($.extend(defaultGridOpts, {
			 			url : stuIfnoListUrl,
			 			colModel : _colModels,
			 			pager : stuInfoPagerId,
						height : "100%",
						multiselect : true,
						width:1124,
			 		}));
			 		resizeFun("495");
		
		$('#examNumber').focus(function(){
	    	 $("#tbl").text("");
	     });
		
		
		$("#fastSearchStu").click(function(){
			 var examNumber=$("#queryParam").val();
			 var examNumberState=$("#examNumberState").find("option:selected").val();
			 var stuName=$("#stuName").val();
			 var data = {
				     examNumber: examNumber,
				     examNumberState:examNumberState,
				 };
			 $.ajax({
			     url: stuIfnoListUrl,
			     type: "POST",
			     data: data,
			     dataType: "JSON",
			     success: function (data) { 
			    	 loadStuInfo();
			   }
			 }); 
		});
		
		
</script>
</head>
<body>


<div class="page-editor-panel full-drop-panel">
	<div class="head-panel">
	   <div id="importCondition" style="margin: -5px 10px 10px 0px;">
				<div class="search-panel" style="display: block;">
				<p>
				     姓名或学籍号：
						<input id="stuName"  placeholder="输入姓名或学籍号" type="text"  style="line-height: 1em; font-size: 1em; cursor: text;" />
				&nbsp;
				是否分配考号
						<select id="examNumberState"  style="width:175px;height:26px;"   data-dic="{code:'examNumberState'}"   class="form-control"  >
						    <option value="">全部</option>
						 </select>
						<input id="queryParam" type="hidden"  style="line-height: 1em; font-size: 1em; cursor: text;" /> 
				<button id="fastSearchStu" title="查询" style="margin-left: 0px;">
								<i class="fa fa-search"></i>查询
				</button>
				</p>
				
				</div>
	</div>
	</div>
		
		<div class="page-content">
			<div class="page-inner-content">
		    <table id="stuInfoList2"></table>
		   <div id="stuInfopager"></div>
			</div>
		</div>
	</div>

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
							</tr>
		      	 </table>
			</form>
			</div>
		</div>
</div>
</body>
</html>