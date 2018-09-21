
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
<link href="../theme/default/jquery.edittable.min.css" rel="stylesheet" />
<link href="../theme/default/jquery.multiselect.filter.css" rel="stylesheet" />
<link href="../js/StickyTableHeaders/css/oocss.css" rel="stylesheet" />
<link href="../js/StickyTableHeaders/css/custom.css" rel="stylesheet" />
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
<script type="text/javascript" src="../js/StickyTableHeaders/js/jquery.stickytableheaders.min.js"></script>



<style>
.page-list-panel {
    height: auto;
    width: 100%;
    background-color: #e7fcd9;
    position: absolute;
    z-index: 1;
}
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
    var listId = "#list2", editorFormId = "#editorForm", pagerId = '#pager2', editorRelatedFormId = "#editorRelatedForm", listUrl = "../statisticsAnalysis/scoreAnalysis.do?command=searchScore";
    $(function() {
    	
        //多选下拉框
        $("#school").multiselect({
            checkAllText : "全选",
            uncheckAllText : "全不选",
            noneSelectedText : "选择学校",
            selectedText : '#' + " 所学校",
            selectedList : 2
        }).multiselectfilter({
            label : "学校名称",
            placeholder : "请输入校名"
        });

        $("#course").multiselect({
        	multiple : false,
        	checkAllText : "全选",
			uncheckAllText : "全不选",
			noneSelectedText : "选择科目",
			selectedText : '#' + " 科目",
			selectedList : 2
        });

        _initButtons({}); //from page.common.js
        _initFormControls(); //from page.common.js
        _initValidateForXTypeForm(editorFormId);

        
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
        
        $("#schoolType").change(function() {
       	    	$("#course").empty();
           		$("#grade").empty();
           		$("#school").empty();
        	      var schoolType = $("#schoolType").val();
        	      if (schoolType != "") {
        	        var url = "../platform/dictionary.do?command=getDataByCode";
        	        var data = {
        	          schoolType : schoolType
        	        };
        	        $.ajax({
        	              url : url,
        	              type : "POST",
        	              data : data,
        	              dataType : "JSON",
        	              success : function(data) {
        	            	  if (data.courseList.length > 0) {
									for (var i = 0; i < data.courseList.length; i++) {
									$("#course").append("<option value='"+data.courseList[i].DictionaryCode+"'>"+ data.courseList[i].DictionaryName+ "</option>");
									}
									$("#course").multiselect({
										multiple : false,
							        	checkAllText : "全选",
										uncheckAllText : "全不选",
										noneSelectedText : "选择科目",
										selectedText : '#' + " 科目",
										selectedList : 2
									});
									$("#course").multiselect('refresh');
								}else{
									$("#course").empty();
									$("#course").multiselect('refresh');
								}
        	            	  if (data.gradeList.length > 0) {
									for (var i = 0; i < data.gradeList.length; i++) {
										$("#grade").append("<option value='" + data.gradeList[i].DictionaryCode + "'>"+ data.gradeList[i].DictionaryName+ "</option>");
									}
								}
        	            	  if (data.schoolList.length > 0) {
        	            		  for (var i = 0; i < data.schoolList.length; i++) {
										$("#school").append("<option value='" + data.schoolList[i].School_Code + "'>"+ data.schoolList[i].School_Name+ "</option>");
									}
        	            		  for (var i = 0; i < data.schoolList.length; i++) {
										$("#school").find("option[value='"+ data.schoolList[i].School_Code+ "']").attr("selected","selected");
									}
        	            		  $("#school").multiselect({
        	            	            checkAllText : "全选",
        	            	            uncheckAllText : "全不选",
        	            	            noneSelectedText : "选择学校",
        	            	            selectedText : '#' + " 所学校",
        	            	            selectedList : 2
        	            	        }).multiselectfilter({
        	            	            label : "学校名称",
        	            	            placeholder : "请输入校名"
        	            	        });
        	            		  $("#school").multiselect('refresh');
								}else{
									$("#school").empty();
									$("#school").multiselect('refresh');
								}
        	              }
        	            });
        	      }
        	});
    });
    
    function fastSearch(state){
        var scoreHtml;
        var course =$("#course").find("option:selected").val();
        var schoolYear = $("#schoolYear").find("option:selected").val();
        var schoolType = $("#schoolType").find("option:selected").val();
        var term = $("#term").find("option:selected").val();
        var examType = $("#examType").find("option:selected").val();
        var grade = $("#grade").find("option:selected").val();
        var school = [];
        $("#school").find("option:selected").each(function() {
        	school.push($(this).val());
		});
        
		if (schoolType == "") {
            window.Msg.alert("请选择学校类型");
            return;
        }
        if (term == ""){ 
           window.Msg.alert("请选择学期");
            return;
        }
        if (examType == ""){
            window.Msg.alert("请选择测试类型");
            return;
        }
        if (grade == ""){
            window.Msg.alert("请选择年级");
            return;
        }
        if (school.length<1){
           window.Msg.alert("请选择学校");
            return;
        }
        if (course == ""||typeof(course) == "undefined"){
            window.Msg.alert("请选择科目");
            return;
        }
        var url = "../qualityAnalysis/personScoreSummary.do?command=analyzePersonalSamllScore";
        var data = {
        		course : course,
        		schoolYear : schoolYear,
        		schoolType : schoolType,
        		term : term,
        		examType : examType,
        		grade : grade,
        		school : school,
        		state:state
        	};
        $("table").stickyTableHeaders('destroy');
        $("#targetTable").empty();
        $.ajax({
            url : url,
            type : "POST",
            data : data,
            dataType : "JSON",
            beforeSend: function () {
            	$("#waittingDiv").append("<img src='../theme/images/loading.gif'/>");
            },
            success : function(data) {
            	$("#waittingDiv").empty();
            	if(data.questionList.length<1){
            		window.Msg.alert("未发布过考试或暂无分数上传！");
                    return;
            	}else{
            		var questionTh="<thead><tr><th>学校</th><th>班级</th><th>学籍号</th><th>考号</th><th>姓名</th>";
            		for(var i=0;i<data.questionList.length;i++){
            			questionTh+="<th>"+data.questionList[i]+"</th>";
            		}
            		questionTh+="<th>总分</th></tr></thead>";
            		$("#targetTable").append(questionTh);
            	}
            	if(data.scoreList.length>0){
            		var scoreContent="<tbody>";

            		for(var i=0;i<data.scoreList.length;i++){
            			var scoreEntity= data.scoreList[i];
            			scoreContent+="<tr><td>"+scoreEntity.School_Name+"</td><td>"+scoreEntity.Class_Name+"</td><td>"+scoreEntity.XJH+"</td><td>"+scoreEntity.Exam_Number+"</td><td>"+scoreEntity.Name+"</td>";
            			var score= scoreEntity.Score_List.split(",");
						for(j=0;j<score.length;j++){
							scoreContent+="<td>"+score[j]+"</td>";
						}
						scoreContent+="<td>"+scoreEntity.TotalScore+"</tr></tbody>";
            		}

            		$("#targetTable").append(scoreContent);
            	}
            	var offset = 96;
                $("table").stickyTableHeaders({fixedOffset: offset});
                $("#targetTable .tableFloatingHeaderOriginal tr").attr("height","56px")
            }
          });
    };
    function importExcel(state){
        var course =$("#course").find("option:selected").val();
        var schoolYear = $("#schoolYear").find("option:selected").val();
        var schoolType = $("#schoolType").find("option:selected").val();
        var term = $("#term").find("option:selected").val();
        var examType = $("#examType").find("option:selected").val();
        var grade = $("#grade").find("option:selected").val();
        var school = [];
        $("#school").find("option:selected").each(function() {
        	school.push($(this).val());
		});
        
		if (schoolType == "") {
            window.Msg.alert("请选择学校类型");
            return;
        }
        if (term == ""){ 
           window.Msg.alert("请选择学期");
            return;
        }
        if (examType == ""){
            window.Msg.alert("请选择测试类型");
            return;
        }
        if (grade == ""){
            window.Msg.alert("请选择年级");
            return;
        }
        if (school.length<1){
           window.Msg.alert("请选择学校");
            return;
        }
        if (course == ""||typeof(course) == "undefined"){
            window.Msg.alert("请选择科目");
            return;
        }
        var url = "../qualityAnalysis/personScoreSummary.do?command=analyzePersonalSamllScore";
        var data = {
        		course : course,
        		schoolYear : schoolYear,
        		schoolType : schoolType,
        		term : term,
        		examType : examType,
        		grade : grade,
        		school : school,
        		state:state
        	};
		var form = $( "#fileReteExportExcel" ) ;
		var paramValue=JSON.stringify(data);
			$("#paramData").val(paramValue);
			form.attr( "action", url) ;
			form.get( 0 ).submit() ; 
    }
    window.onload = function() {
        $("#schoolType option[value != '']").remove();
        $("#term option[value != '']").remove();
        $("#examType option[value != '']").remove();

    }
</script>


</head>
<body style="overflow: auto;">
<form id="fileReteExportExcel" method="post">
	<input id="paramData" name="data" type="hidden" value=""/>
</form>
    <div class="page-list-panel" style="overflow-y: auto; overflow-x: hidden;">
        <div class="head-panel">
			<div id="importCondition" style="margin: -5px 10px 10px 0px;">
			    <div class="search-panel" style="display: block;">
			        <p>
			            学年 <select id="schoolYear" name="schoolYear" class="form-control" data-dic="{code:'ND'}" style="width: 150px;"></select>&nbsp; 
			            学校类型 <select id="schoolType" name="schoolType" class="form-control" style="width: 150px;" data-dic="{code:'xxlx'}">
			                <option value="">
			                    	选择学校类型
			                </option>
			              </select>&nbsp; 
			            年级 <select id="grade" name="grade" class="form-control" style="width: 150px;">
			                <option value="">
			                   	 选择年级
			                </option>
			           </select>&nbsp; 
			           学期 <select id="term" name="term" class="form-control" style="width: 150px;" data-dic="{code:'xq'}">
			                <option value="">
			                    	选择学期
			                </option>
			            </select> &nbsp; 
			      测试类型 <select id="examType" name="examType" class="form-control" style="width: 150px;" data-dic="{code:'kslx'}">
			                <option value="">
			                   	 选测试类型
			                </option>
			            </select>
			        </p>
			    </div>
			    <div class="multiselect" style="margin: -5px 10px 10px 0px;">
			        <p>
			            科目 <select id="course" name="course" class="form-control"  multiple="multiple" ></select>&nbsp; 
			            学校<select id="school" name="school" class="form-control"  multiple="multiple" ></select>&nbsp;
			           <!--  班级<select id="class" name="class" class="form-control"></select>&nbsp;  -->
			            <span class="toolbar">
			                <button onclick="fastSearch('0')" title="分析" style="margin-left: 0px;">
			                    <i class="fa fa-search"></i>分析
			                </button>
			                <button onclick="importExcel('1')" title="导出" style="margin-left: 0px;">
			                    <i class="fa fa-search"></i>导出
			                </button>
			            </span>
			        </p>
			    </div>
			</div>
        </div>
    </div>
    <div id="waittingDiv" style="position:absolute;width:100px;height:100px;top:100px;left:430px"></div>
    <div class="page-content" style="position:absolute;width:100%;height:684px;top:80px;left:0px;overflow-y: auto; overflow-x: hidden;background-color: transparent;">
		<table class='inputtable' id="targetTable"></table>
	</div>
</body>
</html>