<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<title>区级学校生成考号详情</title>
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
	       schoolSum=0,
		   notExitSchoolNum=0,
		   exitExamNumberSchoolNum=0,
		   listUrl = "../examNumberManagement/testCaseGeneration.do?command=testCastGenerationSearchPading",
		   countSchoolByExamCodeUrl="../examNumberManagement/testCaseGeneration.do?command=countSchoolByExamCode";
			$(function() {
        	   $.ajax({
		     		url:countSchoolByExamCodeUrl ,
				     type: "POST",
				     data: {},
				     dataType: "JSON",
				     async:false,
				     success: function (data) { 
				    	 schoolSum=data.schoolSum;
				    	 notExitSchoolNum=data.notExitSchoolNum;
				    	 exitExamNumberSchoolNum=data.exitExamNumberSchoolNum;
				     }
		     	});
    	 $(listId).trigger("reloadGrid");
		_initButtons({
 
		}); //from page.common.js
		_initFormControls(); //from page.common.js
		_initValidateForXTypeForm(editorFormId);
		 loadDictionary();
		var _colModel = [
				{
					name : 'School_Code',
					key : true,
					width : 60,
					hidden : true,
					search : false
				},
				{
					label : "名称",
					name : 'School_Name',
					sortable : false,
					autoWidth : true,
					align : "left",
				},{
					label : "考号生成类型",
					name : 'candidateNumberTypes',
					sortable : false,
					autoWidth : true,
					align : "center",
					formatter : function(ar1, ar2, ar3) {
						var arrList = ar3.candidateNumberType;
	                    var arr = [];
	                    var strJoin;
	                    var resultStr;
	                    if ("1"==ar3.Is_Exist_Exam_Number && arrList.length > 0) {
	                        $.each(arrList, function (index, item) {
	                            var str = _types[item.CandidateNumber_Type];
	                            arr.push(str);
	                        });
	                    }
	                    resultStr = arr.join(",");
	                    return resultStr || "";
					}
					
				},{
					label : "考号生成情况",
					name : 'Is_Exist_Exam_Number',
					sortable : false,
					autoWidth : true,
					align : "center",
					formatter : function(ar1, ar2, ar3) {
						if(parseInt(ar3.Is_Exist_Exam_Number)==1){
							return "已生成";
						}else{
							return "未生成";
						}
					}
				} ];
		var _caption;
		if(schoolSum==exitExamNumberSchoolNum){
			_caption="需要生成考号的学校共<span style='color: rgba(14, 232, 235, 0.93);'>（"+schoolSum+"）</span>所，所有学校考号已生成完毕！";
		}else if(schoolSum==notExitSchoolNum){
			_caption="需要生成考号的学校共<span style='color: rgba(14, 232, 235, 0.93);'>（"+schoolSum+"）</span>所，暂未学校生成考号！";
		}else{
			_caption="需要生成考号的学校共<span style='color: rgba(14, 232, 235, 0.93);'>（"+schoolSum+"）</span>所，已有<span style='color: rgba(14, 232, 235, 0.93);'>（"+exitExamNumberSchoolNum+"）</span>所学校生成考号，"+
			                   "还有<span style='color: rgba(14, 232, 235, 0.93);'>（"+notExitSchoolNum+"）</span>所学校未生成考号";
		}
		$(listId).jqGrid($.extend(defaultGridOpts, {
			url : listUrl,
			colModel : _colModel,
			caption:_caption,
			pager : pagerId,
			rowNum: 5,
		    rowList: [ 5, 10],
		    multiselect:false//去掉多选框
		}));
		resizeFun();
});
 
 
 
</script>
</head>
<body>
<form id="fileReteExportExcel" method="post"></form>
	<div class="page-list-panel">
		<div class="head-panel">
			<div class="toolbar" style="height: 0px;">
			</div>
		</div>
		<table id="list2"></table>
		<div id="pager2"></div>  
	</div>
	                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                