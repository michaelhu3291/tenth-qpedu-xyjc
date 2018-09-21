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
.page-inner-content {
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
.ui-jqgrid  tr.jqgrow td {
white-space: normal !important;
height:auto;
}
</style>
<script type="text/javascript">
	var listId = "#list2", 
	editorFormId = "#editorForm", 
	pagerId = "#pager2", 
	markingArrangementId = "#markingArrangement", 
	markingArrangementpagerId = "#markingArrangementpager", 
	markingArrangementUrl = "../examInfo/markingArrangement_xjkw.do?command=serachCoursePaging", 
	listUrl = "../examInfo/examManage.do?command=searchPaging";

	
	
	$(function() {
		_initButtons({
			backBTN : function(){
				hideSlidePanel(".page-editor-panel");
				showSlidePanel(".page-list-panel");
				$(listId).trigger("reloadGrid");
			},
			cancelBTN : function() {
			 
			},
		
		}); //from page.common.js
		_initFormControls(); //from page.common.js
		_initValidateForXTypeForm(editorFormId);
		var _colModel = [{
			 					name : 'Id',
			 					key : true,
			 					width : 60,
			 					hidden : true,
			 					search : false
			 				},{
			 					name : 'Grade_Code',
			 					sortable : false,
			 					width : 60,
			 					hidden : true,
			 				},{
			 					label : "测试日期",
			 					name : 'Exam_Time',
			 					sortable : false,
			 					width : 100,
			 					align : "center",
			 				},{
			 					label : "测试编号",
			 					name : 'examNumber',
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
								label : "测试状态",
								name : 'examState',
								sortable : false,
								width : 80,
								align : "center",
								formatter : function(ar1, ar2, ar3) {
									var operStr;
								 if(ar3.examState==0){
									 operStr="未测试"
								 }
								 if(ar3.examState==1){
									 operStr="正在测试"
								 }
								 if(ar3.examState==2){
									 operStr="测试结束"
								 }
								return operStr;
								}

							},{
			 					label : "操作",
			 					name : '',
			 					sortable : false,
			 					width : 120,
			 					align : "center",
			 					formatter : function(ar1, ar2, ar3) {
			 						return "<button id='queryBTN' class='page-button' title='安排阅卷人' onclick='markingArrangements(\""
			 								+ ar3.examNumber
			 								+ "\",\""
			 								+ ar3.Grade_Code
			 								+ "\",\""
			 								+ ar3.ev
			 								+ "\");'><i class='fa fa-file-text' style='margin-right: 5px;'></i>安排阅卷人</button>"
			 					}
			 				} ];

			 		$(listId).jqGrid($.extend(defaultGridOpts, {
			 			url : listUrl,
			 			colModel : _colModel,
			 			pager : pagerId,
			 			multiselect:false,
			 			postData:{introducedState:"1"}
			 		}));
			 		resizeFun();
	});
	
	var loadCourseArrangement=function(){
 		//加载jqgrid
 			var _colModels = [{
 						name : 'Course',
 						key : true,
 						width : 60,
 						hidden : true,
 						search : false
 					},{
 						label : "科目",
 						name : '',
 						sortable : false,
 						width : 100,
 						align : "center",
 						formatter : function(ar1, ar2, ar3) {
 							return _types[ar3.Course]
 						}
 					},{
 						label : "测试日期",
 						name : 'Course_Exam_Time',
 						sortable : false,
 						width : 80,
 						align : "center",
 					},{
 						label : "测试时间",
 						name : 'Course_Start_Time',
 						sortable : false,
 						width : 90,
 						align : "center",
 						formatter : function(ar1, ar2, ar3) {
 							if( ar3.Course_Start_Time!=null &&  ar3.Course_Start_Time!=""){
 							return  ar3.Course_Start_Time+" 至 "+ar3.Course_End_Time;
 							}else{
 								return "";
 								}
 						}
 					},{
 						label : "测试时长(分钟)",
 						name : 'Exam_Length',
 						sortable : false,
 						width : 60,
 						align : "center",
 					},{
 						label : "测试形式",
 						name : 'Course_Exam_Type',
 						sortable : false,
 						width : 50,
 						align : "center",
 					},{
 						label : "阅卷人",
 						name : 'Teacher_Name',
 						sortable : false,
 						width : 100,
 						align : "left",
 					},{
 						label : "阅卷日期",
 						name : 'Marking_Time',
 						sortable : false,
 						width : 80,
 						align : "center",
 					},{
 						label : "阅卷时间",
 						name : 'Marking_Start_Time',
 						sortable : false,
 						width : 90,
 						align : "center",
 						formatter : function(ar1, ar2, ar3) {
 							if( ar3.Marking_Start_Time!=null &&  ar3.Marking_Start_Time!=""){
 								return ar3.Marking_Start_Time+" 至 "+ar3.Marking_End_Time;
 							}else{
 								return "";
 							}
 						}
 					},{
 						label : "安排阅卷人<br/>截止日期",
 						name : 'Marking_End_Date',
 						sortable : false,
 						width : 120,
 						align : "center",
 						formatter: function (ar1, ar2, ar3) {
			                	var dateDifference=getDateDifference(ar3.Marking_End_Date,getNowDate());
			                	if(dateDifference==0){
			                		 return ar3.Marking_End_Date +"<br/>今天截止"
			                	}else if(dateDifference>0){
			                		return ar3.Marking_End_Date +"<br/>距离截止<span style='color:#ffffff;' class='badge red'>"+dateDifference+"</span>天"
			                	}else{
			                		return ar3.Marking_End_Date;
			                	}
			            }
 					},{
 						label : "阅卷地点",
 						name : 'Marking_Place',
 						sortable : false,
 						autoWidth : true,
 						align : "left",
 					},{
 						name : 'examCode',
 						sortable : false,
 						width : 60,
 						hidden : true,
 					},{
 						label : "操作",
 						name : '',
 						sortable : false,
 						width : 120,
 						align : "center",
 						formatter : function(ar1,ar2,ar3){
 							 var nowDate=new Date();
  							nowDate=dateFormat(nowDate,"yyyy-MM-dd")
  							 if(nowDate<ar3.Marking_End_Date){
  								var butStr="<button id='queryBTN' class='page-button' title='安排阅卷人' onclick='loadTeacherByCourseAndGrade(\""
 									+ ar3.examCode
 								     + "\",\""
  									+ ar3.Course
 								+ "\");'><i class='fa fa-file-text' style='margin-right: 5px;'></i>安排阅卷人</button>";
  								return butStr;
  							 }else{
  								var butStr="已过安排阅卷人时间";
  								return  butStr;
  							 }
 						}
 					} ];
 			//重新创建列表
 			
 			$(markingArrangementId).GridUnload();
 			
 			$(markingArrangementId).jqGrid($.extend(defaultGridOpts, {
 				url : markingArrangementUrl,
 				colModel : _colModels,
 				pager : markingArrangementpagerId,
 				height : "100%",
 				multiselect : false,
 			    width: $("body").innerWidth() - 20,
 			    autowidth: false,
 			    shrinkToFit: true,
 				rowNum: 20,
 				rowList: [20, 30, 50, 100 ],
 			}));
 			resizeFun("495");

}
	//安排阅卷人，加载出该测试下某年级，某科目的教师
	function loadTeacherByCourseAndGrade(examCode,course){
		var queryParamsUrl="../examInfo/markingArrangement_xjkw.do?command=queryParams";
		var data={gradeId:$("#gradeCode").val(), course:course,examCode:examCode}
							$.ajax({
										url : queryParamsUrl,
										type : "POST",
										data : data,
										dataType : "JSON",
										async: false, 
										success : function(data) {
											 var arrangementTreeUrl="examInfo/arrangementTree.do?&gradeId="+$("#gradeCode").val();
											   frameDialog(arrangementTreeUrl,"所有"+_types[course]+"教师",
													   {mode:"middle",resizable:false,width:550,height:480,buttons:[
													              { text:"确定", icons:{ primary:"ui-icon-check" },click:function( ev )
													                {
													                     var $this   = window.top.$( this ),
													                     dial = $this.find( "iframe" )[0].contentWindow ;
													                     var data={id:dial.getData(),examCode:examCode,course:course}
													                     if(data!=false && data!=null){
													                     var addTeacherRefExamUrl = "../examInfo/markingArrangement_xjkw.do?command=addTeacherRefExam";
													                     POST(addTeacherRefExamUrl,data,function(data){
													                    	 if(data.success=="false" ){
													                    		 window.message({
													         						text : "请选择要修改的记录!",
													         						title : "提示"
													         					});
													                    	 }
													                    	 $(markingArrangementId).trigger("reloadGrid");
													                     }); 
													              $this.dialog( "close" ) ;   
													       }else{
													    	   window.message({
									         						text : "请选择阅卷人!",
									         						title : "提示"
									         					});
													       }
												}}, 
											          { text:"返回", icons:{ primary:"ui-icon-cancel" }, click:function( ev )
											                                {
											                                      var $this = window.top.$( this ) ;
											                                        $this.dialog( "close" ) ;
											                                       }}
												
											                                ]});					
										}
					});
	}

	function markingArrangements(examNumber,gradeCode, ev) {
		$("#gradeCode").val(gradeCode);
		 $("#queryParam").val(examNumber);
		loadCourseArrangement();
		var $i = $(ev.currentTarget).find("i"), $piel = $(".page-editor-panel")
		.show({
			effect : "slide",
			direction : "up",
			easing : 'easeInOutExpo',
			duration : 900
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

	<div class="page-editor-panel full-drop-panel">
			<div class="title-bar">
			<input  id="gradeCode"  type="hidden" />
			<input  id="queryParam"  type="hidden" />
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
		<table id="markingArrangement"></table>
		<div id="markingArrangementpager"></div>
		
		</div>
		</div>
	</div>

</body>
</html>