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
  	a:hover {text-decoration:underline; color:red; cursor:pointer;}
</style>
<script type="text/javascript">
	var listId = "#list2", 
	       editorFormId = "#editorForm", 
	       pagerId = "#pager2",
           stuInfolistId = "#stuInfoList2";
			stuInfoPagerId = "#stuInfopager",
			examNumberStates="",
			stuIfnoListUrl = "../examNumberManagement/examNumberInfo.do?command=searchPaging",
			getImportParamsUrl= "../examNumberManagement/examNumberInfo.do?command=getImportParams",
			listUrl = "../examNumberManagement/examNumberManage.do?command=searchPaging",
            updateUrl="../examNumberManagement/examNumberInfo.do?command=updateExamNumberById",
            verifyCodeUrl="../examNumberManagement/examNumberManage.do?command=examNumberIsExist",
           getStuExamNumberUrl="../examNumberManagement/examNumberInfo.do?command=getStuExamNumber",
           getSchoolByExamNumber="../examNumberManagement/examNumberManage_admin.do?command=getSchoolByExamCode";
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
				 $(".ui-effects-wrapper").css("display","none");
				 $("#school").find("option:selected").attr("selected",false);
					$("#school option").eq(1).remove();
				$("#examNumberState").find("option:selected").attr("selected",false);
				hideSlidePanel(".page-editor-panel,.page-update-panel");
				$("#deriveStuInfo").attr("disabled",false);
	    		 $("#deriveStuInfo").css({"border":"1px solid #3B5617","color":"#3B5615","background":"#ffffff"});
				showSlidePanel(".page-list-panel");
				$(listId).trigger("reloadGrid");
				   //重新创建列表
		          $(stuInfolistId).GridUnload();
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
					label : "测试日期",
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
				},{
					label : "测试名称",
					name : 'Exam_Name',
					sortable : false,
					autoWidth : true,
					align : "left",
				},{
					label : "考号生成情况",
					name : '',
					sortable : false,
					width : 100,
					align : "center",
					formatter : function(ar1, ar2, ar3) {
						var operStr
						= "<button id='queryBTN' class='page-button' title='查看' onclick='selectExamState(\""
							+ ar3.Exam_Number
							+ "\",\""
							+ ar3.ev
							+ "\");'><i class='fa fa-file-text' style='margin-right: 5px;'></i>查看</button>";
					return operStr;
					}

				},{
					label : "操作",
					name : '',
					width : 120,
					align : "center",
					sortable : false,
					formatter : function(ar1, ar2, ar3) {
							var operStr
							= "<button id='queryBTN' class='page-button' title='查看测试详情' onclick='queryExamOper(\""
								+ ar3.Exam_Number
								+ "\",\""
								+ ar3.Grade_Code
								+ "\",\""
								+ ar3.ev
								+ "\");'><i class='fa fa-file-text' style='margin-right: 5px;'></i>查看测试详情</button>";
						return operStr;
					}
				} ];
		$(listId).jqGrid($.extend(defaultGridOpts, {
			url : listUrl,
			colModel : _colModel,
			pager : pagerId,
			multiselect:false,
		}));
		resizeFun();
		
		$('#examNumber').focus(function(){
	    	 $("#tbl").text("");
	     });
		//查询学生信息
		$("#fastSearchStu").click(function(){
			 var schoolCode=$("#school option:selected").val();
			 var chooseCourse=$("#selectCourse option:selected").val();
			 var examNumber=$("#queryParam").val();
			 var gradeCode=$("#gradeId").val();
			 var examNumberState=$("#examNumberState").find("option:selected").val();
			 var nameOrXjfh=$("#nameOrXjfh").val();
			 var datas= {
					 schoolCode:schoolCode,
				     examNumber: examNumber,
				     examNumberState:examNumberState,
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
			    	 if(data.message=="false"){
			    		 window.Msg.alert("暂无考号生成...!");
			    		 $("#testCaseGeneration").val("");
			    		 $(stuInfolistId).GridUnload();
			    		 $(".ui-jqgrid-title").text("暂无考号生成....!");
			    		 loadStuInfo(examNumberState);
			    	 }else{
			    		 $(stuInfolistId).GridUnload();
			    		 $("#notTestCaseGeneration").val(data.notTestCaseGeneration);
				    	 $("#testCaseGeneration").val(data.testCaseGeneration);
				    	 $('#isTestCaseGeneration').val(data.isTestCaseGeneration);
				    	 loadStuInfo(examNumberState);
			    	 }
			   }
			 }); 
		});
		
		
		
		//改变学校
		$("#school").change(function(){
			 var schoolCode=$("#school option:selected").val();
			 var examNumber=$("#queryParam").val();
			 var gradeCode=$("#gradeId").val();
			 var examNumberState=$("#examNumberState").find("option:selected").val();
			 var nameOrXjfh=$("#nameOrXjfh").val();
			 var datas= {
					 schoolCode:schoolCode,
				     examNumber: examNumber,
				     examNumberState:examNumberState,
				     nameOrXjfh:nameOrXjfh,
				     gradeCode:gradeCode
				 };
			 $.ajax({
			     url: getImportParamsUrl,
			     type: "POST",
			     data: datas,
			     dataType: "JSON",
			     success: function (data) { 
			    	 if(data.message=="false"){
			    			 $("#deriveStuInfo").css({"background":"#e9e9e9","color":"black","border: ":"1px solid #3B5615"});
				    		 $("#deriveStuInfo").attr("disabled","disabled");
			    	 } else{
			    		 $("#deriveStuInfo").attr("disabled",false);
			    		 $("#deriveStuInfo").css({"border":"1px solid #3B5617","color":"#3B5615","background":"#ffffff"});
			    	 }
			   }
			 }); 
		});
	}); 
           
           
           
   //查看考号生成情况详情
   function selectExamState(examNumber){
		var testCaseGeUrl="examNumberManagement/testCaseGeneration.do?&examNumber="+examNumber;
	   frameDialog(testCaseGeUrl+"&examNumber="+examNumber, 
			   "考号生成详情",
			   {mode:"middle",resizable:false,width:660,height:385,buttons:[
               { text:"关闭", icons:{ primary:"ui-icon-cancel" }, click:function( ev )
                        {
                            var $this = window.top.$( this ) ;
                                 $this.dialog( "close" ) ;
                         }}
                ]});
   };
  
    //点击查看测试详情得到该测试下面的学校
	function queryExamOper(examNumber, gradeCode, ev) {
	    if ($("#school option").length > 1) {
	        $("#school").chosen('destroy');
	    }
	    $("#school option[value!='']").remove();
	    $("#selectCourse option:selected").attr("selected", false);
	    $("#gradeId").val(gradeCode);
	    $("#queryParam").val(examNumber);
	    $.ajax({
	        url: getSchoolByExamNumber,
	        type: "POST",
	        data: {
	            examNumber: examNumber
	        },
	        dataType: "JSON",
	        success: function (data) {
	            $("#school").append("  <option value=''>全部</option>");
	            for (var i = 0; i < data.length; i++) {
	                $("#school").append("<option value='" + data[i].School_Code + "'>" + data[i].School_Name + "</option>");
	                $("#school").trigger("chosen:updated");
	            }
	            $("#school").chosen();
	        }
	    });
	
	    var $i = $(ev.currentTarget).find("i"),
	        $piel = $(".page-editor-panel")
	        .show({
	            effect: "slide",
	            direction: "up",
	            easing: 'easeInOutExpo',
	            duration: 900
	        });
	};
	//加载jqgrid
	function loadStuInfo(examNumberStates){
		var _colModels = [
			 				{
			 					name : 'id',
			 					key : true,
			 					width : 60,
			 					hidden : true,
			 					search : false
			 				},{
			 					name : 'BrevityCode',
			 					key : true,
			 					width : 60,
			 					hidden : true,
			 					search : true,
			 				},{
			 					label : "学校",
			 					name : 'School_Name',
			 					sortable : false,
			 				   	autoWidth : true,
			 					align : "left",
			 				},{
			 					name : 'School_Code',
			 					sortable : false,
			 					width : 60,
			 					hidden : true,
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
			 					width : 100,
			 					align : "center",
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
						var _caption;
						if($("#testCaseGeneration").val()!="" ){
							if(examNumberStates=="1"){
								  _caption="已分配人"+$('#testCaseGeneration').val();
							}else if(examNumberStates=="0"){
								_caption="未分配人数"+$('#notTestCaseGeneration').val();
							}else if(examNumberStates==""){
								 _caption="已分配人"+$('#testCaseGeneration').val()+",未分配人"+$('#notTestCaseGeneration').val();
							}
						}else{
							_caption="暂无考号生成...!"
						}
						
		            //重新创建列表
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
					    caption : _caption,
						//修改考号
						beforeEditCell: function (rowid, cellname, value, iRow, iCol) {
                     	    var rec =$(stuInfolistId).jqGrid('getRowData', rowid);
            			     if (value.length>8){
                			     setTimeout(function () {
                				 $(stuInfolistId).jqGrid('restoreCell', iRow, iCol);
                		    }, 1);
               			  }
         			    },
						formatCell:function(rowid, cellname, value, iRow, iCol){
        	 					var rowData = $(stuInfolistId).jqGrid("getRowData",rowid);
             					var  examNumber= $("#queryParam").val()
        	 					var data={classId:rowData.ClassName,gradeId:rowData.GradeName,examNumber:examNumber,
        			              				schoolCode:rowData.School_Code };
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
						      if(name == 'Exam_Number') {
						          var id=rowid;
								  var examNumber = val;
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
								 						$(stuInfolistId).restoreCell(iRow,iCol)
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
	
	//导出
	    var exportExcel = function(){
	    	var examNumberState=$("#examNumberState").find("option:selected").val();
	    	var schoolCode=$("#school option:selected").val();
	    	var chooseCourse=$("#selectCourse option:selected").val();
	    	var examCode =$("#queryParam").val();
  			var gradeId=$("#gradeId").val();
  			var gradeName=_types[gradeId];
  			var stuNameOrXjfh=$("#nameOrXjfh").val();
  			var data={
  					examCode:examCode,
  					gradeId:gradeId,
  					gradeName:gradeName,
  					schoolCode:schoolCode,
  					examNumberState:examNumberState,
  					stuNameOrXjfh:stuNameOrXjfh,
  					chooseCourse:chooseCourse,
  			}
  			 var url = "../examNumberManagement/examNumberManage.do?command=exportExcel&data="+JSON.stringify(data);
  			 var form = $( "#fileReteExportExcel" ) ;
  				form.attr( "action", url) ;
  				form.get( 0 ).submit() ; 
 
	};
	var  mouseOut=	 function(){
		 $("#deriveStuInfo").css({"border":"1px solid #3B5617","color":"#3B5615","background":"#ffffff"});
	 };
	var  mouseOver=	 function(){
		 $("#deriveStuInfo").css({"border":"1px solid #3B5617","background":"#3B5615","color":"#ffffff"});
	 };
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
		
		 <div id="tabShow" >
		<table class="examInfoShow" id="examInfoShow" cellpadding="0" cellspacing="0">
		</table>
       </div>  
	</div>
	<div class="page-editor-panel full-drop-panel">
		<div class="title-bar">
		<input id="testCaseGeneration"  type="hidden"/>
		<input id="notTestCaseGeneration" type="hidden"/>
		<input id="isTestCaseGeneration" type="hidden"/>
		<input id="param" type="hidden"/>
			<table style="height: 100%;" cellpadding="0" cellspacing="0" border="0">
					<tr>
						<td style="padding-left: 12px; padding-right: 15px;"><i class="fa fa-list-ul micon"></i></td>
				
					<td style="  padding-right: 5px;  "> 
					    学校:
						   <select data-placeholder="全部"  id="school" style="width:130px;height: 26px;">
						        <option value=""></option>
						   </select>
					  </td>
					<td style=" padding-right: 5px;" id="isExamNumberState" > 是否分配考号:
						<select id="examNumberState"    style="width:95px;height:26px;"   data-dic="{code:'examNumberState'}"   class="form-control"  >
						    <option value="">全部</option>
						 </select>
					 </td>
					 <td id="chooseCourse" style=" padding-right: 5px;"> 选科科目:
						<select id="selectCourse" data-dic="{code:'chooseCourse'}"  style="width:95px;height:26px;" class="form-control"  >
						    <option value="">选择科目</option>
						 </select>
					 </td>
					<td style=" padding-right: 5px;" > 
					    姓名或学籍号:
						<input class="input" id="nameOrXjfh"  placeholder="输入姓名或学籍号" type="text"  style="line-height: 1em; font-size: 1em; cursor: text;" />
					</td>
					<td>
				    	<input  id="gradeId"  type="hidden"  />
						<input id="queryParam" type="hidden"  style="line-height: 1em; font-size: 1em; cursor: text;" />
					</td>
					<td>
							<button id="fastSearchStu" title="查询" style="margin-left: 0px;">
								<i class="fa fa-search"></i>查询
							</button>
					        <button id="deriveStuInfo" onclick="exportExcel()"  onmouseover="mouseOver()" onmouseout="mouseOut()">
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
			</div>
		</div>
	</div>
