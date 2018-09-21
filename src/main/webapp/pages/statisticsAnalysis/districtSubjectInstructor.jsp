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
        listUrl = "../statisticsAnalysis/districtSubjectInstructor.do?command=courseScoreSearch";

        
    $(function () {
		 $.ajax({  
	            type : "POST",  
	            url : "../dataManage/districtSubjectInstructorInfo.do?command=getPeriodByLoginName",  
	            data : {},
	            dataType:"JSON",
	            async: false,
	            success : function(data){
	            	for(var i=0;i<data.length;i++){
	            		var periodName;
	            		var periodCode;
	            		var period=data[i].PERIOD;
						var periodStr=period.split(", ");
						for(var j=0;j<periodStr.length;j++){
							periodCode=periodStr[j];
							if(periodCode=="0"){
		            			periodCode="xx";
		            			periodName="小学";
		            			}
		            		if(periodCode=="1"){
		            			periodCode="cz";
		            			periodName="初中";
		            			}
		            		if(periodCode=="2" || periodCode=="3" || periodCode=="4"){
		            			periodCode="gz";
		            			periodName="高中";
		            			}
		            		$("#schoolType").append("<option value='"+periodCode+"'>"+periodName+"</option>");
		            		} 
						}
	       }
	     });
		 
		 
		 /*根据登录名得到对应学科  */
		 
		 $.ajax({  
	            type : "POST",  
	            url : "../statisticsAnalysis/districtSubjectInstructor.do?command=getCourseByLoginName",  
	            data : {},
	            dataType:"JSON",
	            async: false,
	            success : function(data){
	            	for(var i=0;i<data.length;i++){
		            		$("#course").append("<option value='"+data[i].COURSE+"'>"+_types[data[i].COURSE]+"</option>");
		            		} 
						}
	     });
		 
		 
		 
        //多选下拉框
        $("#school").multiselect({
            checkAllText: "全选",
            uncheckAllText: "全不选",
            noneSelectedText: "选择学校",
            selectedText: '#' + " 所学校",
            selectedList: 2
        }).multiselectfilter({
            label: "学校名称",
            placeholder: "请输入校名"
        });

        _initButtons({
            cancelBTN: function () {
                    $(listId).trigger("reloadGrid");
                    hideSlidePanel(".page-editor-panel");
                    editId = '';
                },
        }); //from page.common.js
        $("#tblInfo").find("button").button();
        $("#tabs").tabs({
            heightStyle: "fill"
        });
        $("#tabs-2,#tabs-1").css("height", "auto");
        _initFormControls(); //from page.common.js
        _initValidateForXTypeForm(editorFormId);
        //加载数据字典
        loadDictionary(function () {
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
            $("#term").find("option[value = 'xxq']").attr("selected","selected");
            $("#examType").find("option[value = 'qm']").attr("selected","selected"); 
        });
        
        
        //年级
        $("#grade option[value != '']").remove();
        var schoolType = $("#schoolType").val();
        if (schoolType != "") {
            var url = "../platform/dictionary.do?command=getGradessByCode";
            var data = {
                schoolType: schoolType
            };
            $.ajax({
                url: url,
                type: "POST",
                data: data,
                dataType: "JSON",
                success: function (data) {
                    for (var i = 0; i < data.length; i++) {
                        $("#grade").append("<option value='" + data[i].DictionaryCode + "'>" + data[i].DictionaryName + "</option>");
                    }
                }
            });
        }
        
        //学校
        $("#school option[value != '']").remove();
        var schoolType = $("#schoolType").val();
        if (schoolType != "") {
            var url = "../platform/dictionary.do?command=getSchoolByCode";
            var data = {
                schoolType: schoolType
            };
            $.ajax({
                url: url,
                type: "POST",
                data: data,
                dataType: "JSON",
                success: function (data) {
                    for (var i = 0; i < data.length; i++) {
                        $("#school").append("<option value='" + data[i].School_Code + "'>" + data[i].School_Name + "</option>");
                    }
                    if ($("#school").find("option").length > 0) {
                        //刷新多选下拉框的值
                        $("#school").multiselect('refresh');
                    } 
                }
            });
        }
        
        //选择学校类型关联年级
        $("#schoolType").change(function () {
            $("#grade option[value != '']").remove();
            var schoolType = $("#schoolType").val();
            if (schoolType != "") {
                var url = "../platform/dictionary.do?command=getGradessByCode";
                var data = {
                    schoolType: schoolType
                };
                $.ajax({
                    url: url,
                    type: "POST",
                    data: data,
                    dataType: "JSON",
                    success: function (data) {
                        for (var i = 0; i < data.length; i++) {
                            $("#grade").append("<option value='" + data[i].DictionaryCode + "'>" + data[i].DictionaryName + "</option>");
                        }
                    }
                });
            }
        });

        
        //选择学校类型关联学校
        $("#schoolType").change(function () {
            $("#school option[value != '']").remove();
            var schoolType = $("#schoolType").val();
            if (schoolType != "") {
                var url = "../platform/dictionary.do?command=getSchoolByCode";
                var data = {
                    schoolType: schoolType
                };
                $.ajax({
                    url: url,
                    type: "POST",
                    data: data,
                    dataType: "JSON",
                    success: function (data) {
                        for (var i = 0; i < data.length; i++) {
                            $("#school").append("<option value='" + data[i].School_Code + "'>" + data[i].School_Name + "</option>");
                        }
                        if ($("#school").find("option").length > 0) {
                            //刷新多选下拉框的值
                            $("#school").multiselect('refresh');
                        } 
                    }
                });
            }
        });
        
        $("#fastSearch").click(function() {
        	  var courseTxt=$("#course").find("option:selected").text();
      		var courseVal= $("#course").find("option:selected").val();
      		var school = $("#school").find("option:selected").val();
      		var schoolYear = $("#schoolYear").find("option:selected").val();
			var term = $("#term").find("option:selected").val();
			var examType = $("#examType").find("option:selected").val();
			var grade = $("#grade").find("option:selected").val();
      			//绑定jqgrid
      			 var _colModel = [{
                      name: 'Id',
                      key: true,
                      width: 60,
                      hidden: true,
                      search: false
                  },{
                  	label:"学校",
                      name: "School_Name",
                      sortable: false,
                      autoWidth: true,
                      align: "left",
                  },{
                  	label:"班级",
                      name: "Grade_Name",
                      sortable: false,
                      autoWidth: true,
                      align: "center",
                      formatter: function (ar1, ar2, ar3) {
                          return (_types[ar3.Grade_Id] + "(" + ar3.Class_Id + ")班" || "");
                      }
                  },{
                  	label:"考号",
                      name: 'Exam_Number',
                      sortable: false,
                      autoWidth: true,
                      align: "center",
                  }, {
                  	label:"姓名",
                      name: 'Name',
                      sortable: false,
                      autoWidth: true,
                      align: "center",
                  },{
                  	label:courseTxt,
                      name: courseVal,
                      sortable: false,
                      autoWidth: true,
                      align: "center",
                  }];
      			//成绩信息描述
      			var scoreHtml;
      			var course = $("#course").find("option:selected").val();
      			if (courseTxt.length>0) {
      				course = $("#course").find("option:selected").text();
      			}
      			if(school!=undefined){
      				school = $("#school").find("option:selected").text();
      			}else{
    				window.message({
                        text: "请选择学校",
                        title: "提醒",
                        buttons: {
                            "确认": function () {
                                window.top.$(this).dialog("close");
                            }
                        },
                      });
    				return;
    			}
      			if(schoolYear!=""){
      				schoolYear = $("#schoolYear").find("option:selected").text();
      			}
      			if(grade!=""){
      				grade = $("#grade").find("option:selected").text();
      			}else{
    				window.message({
                        text: "请选择年级",
                        title: "提醒",
                        buttons: {
                            "确认": function () {
                                window.top.$(this).dialog("close");
                            }
                        },
                      });
    				return;
    			}
      			if(term!=""){
      				term = $("#term").find("option:selected").text();
      			}
      			if(examType!=""){
      				examType = $("#examType").find("option:selected").text();
      			}
      			
      			scoreHtml =  schoolYear+school+grade+term+examType+courseTxt + "成绩列表";
      			//重新创建列表
      		//	$(listId).GridUnload();
      			$(listId).jqGrid($.extend(defaultGridOpts, {
      				url : listUrl,
      				colModel : _colModel,
      				pager : pagerId,
      				caption : scoreHtml,
      				rowNum: 20,
      			    rowList: [ 20, 30, 50, 100 ],
      				height : "100%",
      				multiselect : false,
      				/*sortable:true,
      				 sortname:School_Code,
      				sortorder:"desc" */
      			}));
      			resizeFun("549");
      			$(".l_statis").show();
        });
    });
    window.onload = function () {
     //   $("#schoolType option[value != '']").remove();
        $("#term option[value != '']").remove();
        $("#examType option[value != '']").remove();
    }
</script>
</head>
<body style="overflow: auto;">
    <div class="page-list-panel" style="overflow-y: auto;overflow-x: hidden;">
        <div class="head-panel">
            <!-- 多条件 -->
            <div id="importCondition" style="margin: -5px 10px 10px 0px;">
                <div class="search-panel" style="display: block;">
                    <p>
                        学年
                        <select id="schoolYear" name="schoolYear" class="form-control" data-dic="{code:'ND'}" style="width: 150px;"></select>
                          &nbsp;  科目
                        <select id="course" name="course" class="form-control" >
                        </select>
                        &nbsp; 学校类型
                        <select id="schoolType" name="schoolType" class="form-control">
                           
                        </select>
                         &nbsp; 学期
                        <select id="term" name="term" class="form-control" style="width: 150px;" data-dic="{code:'xq'}">
                        </select>&nbsp; 测试类型
                        <select id="examType" name="examType" class="form-control" style="width: 150px;" data-dic="{code:'kslx'}">
                        </select>
                     
                    </p>
                </div>
                <div class="multiselect" style="margin: -5px 10px 10px 0px;">
                    <p>
                      年级
                     <select id="grade" name="grade" class="form-control" style="width: 150px;">
                            <option value="">选择年级</option>
                        </select>
                        学校
                        <select id="school" name="school" multiple="multiple" class="form-control">
                           <%--  <c:forEach items="${schoolList}" var="school">
                                <option value="${school.School_Code}">${school.School_Name }</option>
                            </c:forEach> --%>
                        </select> <span class="toolbar">
							<button id="fastSearch" title="查询" style="margin-left: 0px;">
								<i class="fa fa-search"></i>查询
							</button>
						</span>
                    </p>
                </div>
            </div>
        </div>
        <table id="list2"></table>
        <div id="pager2"></div>
    </div>
</body>
</html>