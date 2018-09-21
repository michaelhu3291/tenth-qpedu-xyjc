<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title>成绩查询老师页面</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="../theme/default/ui.custom.css" rel="stylesheet" />
<link href="../theme/default/ui.jqgrid.css" rel="stylesheet" />
<link href="../theme/default/font.awesome.css" rel="stylesheet" />
<link href="../theme/default/ui.chosen.css" rel="stylesheet" />
<link href="../theme/default/ui.uploadfile.css" rel="stylesheet" />
<link href="../theme/default/ui.pick.css" rel="stylesheet" />
<link href="../theme/default/page.common.css" rel="stylesheet" />
<link href="../theme/default/jquery.multiselect.css" rel="stylesheet" />
<link href="../theme/default/jquery.multiselect.filter.css"	rel="stylesheet" />
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
</style>
<script type="text/javascript">
    var listId = "#list2",
        editorFormId = "#editorForm",
        pagerId = "#pager2",
        editorRelatedFormId = "#editorRelatedForm",
        listUrl = "../statisticsAnalysis/scoreSearch.do?command=searchScore";
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
		var scoreHtml = schoolYear + "学年" + courseTxt + termTxt + examTypeTxt +"测试成绩列表";
		 if(gradeOptions.length === 0){
			 window.Msg.alert("请选择年级");
			 return;
		 }
		 if(classOptions.length === 0){
			 window.Msg.alert("请选择班级");
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
    $(function () {
        $(".search-panel").show().data("show", true);
        $("#grade").multiselect({
        	minWidth : '200',
        	checkAllText: "全选",
            uncheckAllText: "全不选",
            noneSelectedText: "选择年级",
            selectedText: '#' + "年级",
            selectedList: 10
 		});
        $("#class").multiselect({
        	minWidth : '200',
            checkAllText: "全选",
            uncheckAllText: "全不选",
            noneSelectedText: "选择班级",
            selectedText: '#' + "班级",
            selectedList: 10
 		});

        _initButtons({
            cancelBTN: function () {
                    $(listId).trigger("reloadGrid");
                    hideSlidePanel(".page-editor-panel");
                    editId = '';
                }
        }); //from page.common.js
        $("#tblInfo").find("button").button();
        $("#tabs").tabs({
            heightStyle: "fill"
        });
        $("#tabs-2,#tabs-1").css("height", "auto");
        _initFormControls(); //from page.common.js
        _initValidateForXTypeForm(editorFormId);
        $("#fastSearch_teacher").click(function() {
			var gradeOptions = $("#grade").find("option:selected");
			var classOptions = $("#class").find("option:selected");
			 if(gradeOptions.length === 0){
				 window.Msg.alert("请选择年级");
				 return;
			 }
			 if(classOptions.length === 0){
				 window.Msg.alert("请选择班级");
				 return;
			 }
			 
			 var _colModel = [{
                name: 'Id',
                key: true,
                width: 60,
                hidden: true,
                search: false
            },{
            	label:"考号",
                name: 'Exam_Number',
                sortable: false,
                width: 100,
                align: "center",
            },{
            	label:"学籍号",
                name: 'XJFH',
                sortable: false,
                autoWidth: true,
                align: "center",
            },{
            	label:"姓名",
                name: 'Name',
                sortable: false,
                autoWidth: true,
                align: "center",
            },{
            	label:"年级",
                name: 'Grade_Id',
                sortable: false,
                width: 100,
                align: "center",
                formatter: function (ar1, ar2, ar3) {
                    return (_types[ar3.Grade_Id] || "");
                }
            },{
            	label:"班级",
                name: 'Class_Id',
                sortable: false,
                width: 100,
                align: "center",
                formatter: function (ar1, ar2, ar3) {
                    return (_typesClass[ar3.Class_Id] || "");
                }
            },{
            	label:"总分",
                name: 'Total_Score',
                width: 100,
                align: "center",
            }];
			 
			function Object(label, name, autoWidth,
					align) {
				this.label = label;
				this.name = name;
				this.autoWidth = autoWidth;
				this.align = align;
			}
			//成绩信息描述
			var scoreHtml;
			var schoolYear = $("#schoolYear").find("option:selected").text();
			//var schoolType = $("#schoolType").find("option:selected").val();
			var term = $("#term").find("option:selected").val();
			var termTxt = $("#term").find("option:selected").text();
			var examType = $("#examType").find("option:selected").val();
			var examTypeTxt = $("#examType").find("option:selected").text();
			var grade = $("#grade").find("option:selected").val();
			var course = $("#course").find("option:selected").val();
			var courseTxt = $("#course").find("option:selected").text();

			scoreHtml = schoolYear + "学年" + courseTxt + termTxt + examTypeTxt +"测试成绩列表";
			//重新创建列表
			$(listId).GridUnload();
			$(listId).jqGrid($.extend(defaultGridOpts, {
				url : listUrl,
				colModel : _colModel,
				pager : pagerId,
				viewsortcols: [false,'vertical',true],
				caption : scoreHtml,
				rowNum: 20,
			    rowList: [20, 30, 50, 100 ],
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
			//}
		});
        //加载数据字典
        loadDictionary(function () {
            //显示当前年
        	var currentYear = loadSemesterYear();
    	  	$("#schoolYear").append('<option id="schoolYearOption" value="'+currentYear+'">'+currentYear+'</option>');
            $("#term").find("option[value='xxq']").attr("selected","selected");
            $("#examType").find("option[value='qm']").attr("selected","selected");
        });
        
        //查询老师所带的年级和科目
        $.ajax({
			 url : "../statisticsAnalysis/scoreSearch.do?command=selectGradeForTeacher",
			 type : "POST",
			 data : {},
			 dataType : "JSON",
			 success : function(data) {
			 $("#grade").find("option[value != '']").remove();
			 //$("#class").find("option[value != '']").remove();
			 $("#course").find("option[value != '']").remove();
			 var gradeList = data.gradeList;
			 //var classList = data.classList;
			 var courseList = data.courseList;
			 $.each(courseList,function(index,item){
				 $("#course").append("<option value='"+item.Course_Id+"'>"+ _types[item.Course_Id] + "</option>");
			 });
			 $.each(gradeList,function(index,item){
				 $("#grade").append("<option value='"+item+"'>"+ _types[item] + "</option>");
			 });
			 if($("#grade").find("option").length > 0){
				 $("#grade").multiselect('refresh');
			 }
		     
		}
	});
        
        //选择年级关联班级
        $("#grade").change(function(){
        	var gradeSelectedOptions = $("#grade").find("option:selected");
        	var gradeArr = [];
        	if(gradeSelectedOptions.length > 0) {
        		$.each(gradeSelectedOptions,function(index,item){
        			gradeArr.push($(item).val());
   			  });
        		
        		$.ajax({
		       			 url : "../statisticsAnalysis/scoreSearch.do?command=selectClassByGrade",
		       			 type : "POST",
		       			 data : {gradeArr : gradeArr},
		       			 dataType : "JSON",
		       			 success : function(data) {
		       			 $("#class").find("option[value != '']").remove();
		       			 if(data.length > 0){
		       				 $.each(data,function(index,item){
				   				 $("#class").append("<option value='"+item.Grade_Id+','+item.Class_Id+"'>"+ _types[item.Grade_Id] + _typesClass[item.Class_Id] + "</option>");
				   			  });
		       				$("#class").multiselect('refresh');
		       			 }
		       			
       		     
		       		}
		       	});
        	}
        });
    });
    
    
  //导出成绩查询数据
    var exportExcel = function(){
    	var schoolYear = $("#schoolYear").find("option:selected").text();
		//var schoolType = $("#schoolType").find("option:selected").val();
		var term = $("#term").find("option:selected").val();
		var termTxt = $("#term").find("option:selected").text();
		var examType = $("#examType").find("option:selected").val();
		var examTypeTxt = $("#examType").find("option:selected").text();
		var grade = $("#grade").find("option:selected").val();
		var classArr = $("#class").find("option:selected").val();
		var course = $("#course").find("option:selected").val();
		var examNumberOrStuCode = $("#examNumberOrStuCode").val();
		var courseTxt = $("#course").find("option:selected").text();
		var gradeOptions = $("#grade").find("option:selected");
		var classOptions = $("#class").find("option:selected");
		 if(gradeOptions.length === 0){
			 window.Msg.alert("请选择年级");
			 return;
		 }
		 if(classOptions.length === 0){
			 window.Msg.alert("请选择班级");
			 return;
		 }
		var classArr = [];
		if(classOptions.length > 0){
			$.each(classOptions,function(index,item){
				var str = $(item).val();
				classArr.push(str);
			});
		}
		scoreHtml = schoolYear + "学年" + courseTxt + termTxt + examTypeTxt +"测试成绩列表";
		//_search=false, nd=1478237272386, rows=15, page=1, sidx=, sord=asc, schoolYear=2016-2017, grade=[16], class=[16,01], course=yw, term=sxq, examType=qz, examNumberOrStuCode=, isFast=false, q=, o=
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
    <div class="page-list-panel" style="overflow-y: auto;overflow-x: hidden;">
        <div class="head-panel">
            <!-- 多条件 -->
            <div id="importCondition" style="margin: -5px 10px 10px 0px;">
                <div class="search-panel" style="display: block;">
                   <div style="float: left;padding-right: 5px;"> 
                      	学年
                        <select id="schoolYear" name="schoolYear" class="form-control"  style="width: 135px;"></select>
                        <!--&nbsp; 学校类型  <select id="schoolType" name="schoolType" class="form-control" style="width: 135px;" data-dic="{code:'xxlx'}">
                            <option value="">选学校类型</option>
                        </select>-->
                    </div>
                </div>
                <div class="multiselect">
                    <p>
						年级
                        <select id="grade" name="grade" class="form-control" multiple="multiple">
                           <!--  <option value="">选择年级</option>-->
                        </select>  班级
                        <select id="class" name="class" class="form-control" multiple="multiple">
                            <!--<option value="">选择班级</option>-->
                        </select>
                    </p>
                </div>
                <div class="search-panel">
               		科目
                    <select id="course" name="course" class="form-control" style="width: 135px;">
                        <!--  <option value="">选择科目</option>-->
                    </select>
                                                               学期
                    <select id="term" name="term" class="form-control" style="width: 135px;" data-dic="{code:'xq'}">
                         <!--  <option value="">选择学期</option>-->
                    </select> 测试类型
                    <select id="examType" name="examType" class="form-control" style="width: 135px;" data-dic="{code:'kslx'}">
                        <!--  <option value="">选测试类型</option>-->
                    </select>
                    
                    <input type="text" id="examNumberOrStuCode" name="examNumberOrStuCode" placeholder="输入考号或学籍号" style="margin-left:5px;"/>
                    <span class="toolbar">
						<button id="fastSearch_teacher" title="查询" style="margin-left: 0px;">
							<i class="fa fa-search"></i>查询
						</button>
						
						<button id="deriveStuInfo" onclick="exportExcel()">
					    	<i class="fa fa-paperclip"></i>导出
					    </button>
					</span>
					<span class="toolbar" style="display: none;">
							<button id="print" title="打印预览" style="margin-left: 0px;" onClick="getPrint();">
								<i class="fa fa-print"></i>打印预览
							</button>
						</span>
                </div>

            </div>
        </div>
        <table id="list2"></table>
        <div id="pager2"></div>
    </div>
</body>
</html>