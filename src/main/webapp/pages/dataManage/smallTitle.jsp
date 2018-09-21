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
	
	#editorForm tbody td {
		background: #FFF;
	}

</style>
<script type="text/javascript">
    var listId = "#list2",
        detailId = "#list3",
        editorFormId = "#editorForm",
        pagerId = '#pager2',
        editorRelatedFormId = "#editorRelatedForm",
        listUrl = "../dataManage/smallTitle.do?command=searchSmallTitlePaging",
        detailListUrl = "../dataManage/smallTitle.do?command=queryDetailList";
    $(function () {
        $(".search-panel").show().data("show", true);

        _initButtons({
            cancelBTN: function () {
                hideSlidePanel(".page-editor-panel");
            }
        }); //from page.common.js
        $("#tblInfo").find("button").button();
        $("#tabs").tabs({
            heightStyle: "fill"
        });
        $("#tabs-2,#tabs-1").css("height", "auto");
        _initFormControls(); //from page.common.js
        _initValidateForXTypeForm(editorFormId);

        //绑定jqgrid
        var _colModel = [{
                name: 'Id',
                key: true,
                width: 60,
                hidden: true,
                search: false
            }, {
                name: 'Exam_Number',
                sortable: false,
                width: 100,
                align: "center",
            }, {
                name: 'Name',
                sortable: false,
                width: 100,
                align: "center",
            }, {
                name: 'School_Year',
                width: 100,
                hidden: false,
                search: false,
                align: "center",
            }, {
                name: 'Exam_Type',
                autoWidth: true,
                hidden: false,
                search: false,
                align: "center",
                formatter: function (ar1, ar2, ar3) {
                    return (_types[ar3.Exam_Type]);
                }
            }, {
                name: 'Term',
                autoWidth: true,
                search: false,
                align: "center",
                formatter: function (ar1, ar2, ar3) {
                    return (_types[ar3.Term]);
                }
            }, {
                name: 'Course',
                sortable: false,
                autoWidth: true,
                align: "center",
                formatter: function (ar1, ar2, ar3) {
                    return _types[ar3.Course] || "";
                }
            }, {
                name: 'Total_Score',
                sortable: false,
                width: 60,
                align: "center",
            }, {
                name: '',
                autoWidth: true,
                align: "center",
                sortable: false,
                formatter: function (ar1, ar2, ar3) {
                    var operStr = "<button id='queryBTN' class='page-button' title='查看详情' onclick='queryOper(\"" + ar3.School_Year + "\",\"" + ar3.Exam_Type + "\",\"" + ar3.School_Type + "\",\"" + ar3.Term + "\",\"" + ar3.Exam_Number + "\",\"" + ar3.Course + "\",\"" + ar3.ev + "\");'><i class='fa fa-file-text' style='margin-right: 5px;'></i>查看详情</button>";
                    return operStr;
                }
            }],
            _colNames = ['', '考号', '姓名', '学年', '测试类型', '学期', '科目', '总分', '操作'];

        $(listId).jqGrid($.extend(defaultGridOpts, {
            url: listUrl,
            colNames: _colNames,
            colModel: _colModel,
            pager: pagerId,
            multiselect: false,
        }));
        resizeFun();

        //加载数据字典
        loadDictionary(function () {
            var currentYear = loadSemesterYear();
            //显示当前年
            $("#schoolYear").append('<option id="schoolYearOption" value="' + currentYear + '">' + currentYear + '</option>');
            $("#schoolYear1").append('<option id="schoolYearOption" value="' + currentYear + '">' + currentYear + '</option>');
        });
        //选择学校类型关联科目
        $("#schoolType").change(function () {
            $("#course option[value != '']").remove();
            var schoolType = $("#schoolType").val();
            var url = "../platform/dictionary.do?command=getCoursesByCode";
            var data = {
                schoolType: schoolType
            };
            if (schoolType != "") {
                $.ajax({
                    url: url,
                    type: "POST",
                    data: data,
                    dataType: "JSON",
                    success: function (data) {
                        for (var i = 0; i < data.length; i++) {
                            $("#course").append("<option value='" + data[i].DictionaryCode + "'>" + data[i].DictionaryName + "</option>");
                        }
                    }
                });
            }

        });

        //选择学校类型关联科目
        $("#schoolType1").change(function () {
            alert("123");
            $("#course1 option[value != '']").remove();
            var schoolType1 = $("#schoolType1").val();
            var url = "../platform/dictionary.do?command=getCoursesByCode";
            var data = {
                schoolType: schoolType1
            };
            if (schoolType1 != "") {
                $.ajax({
                    url: url,
                    type: "POST",
                    data: data,
                    dataType: "JSON",
                    success: function (data) {
                        for (var i = 0; i < data.length; i++) {
                            $("#course1").append("<option value='" + data[i].DictionaryCode + "'>" + data[i].DictionaryName + "</option>");
                        }
                    }
                });
            }

        });

        $("#importData").click(function () {
            $("#schoolType1 option[value != '']").remove();
            $("#term1 option[value != '']").remove();
            $("#examType1 option[value != '']").remove();
            $("#course1 option[value != '']").remove();
            loadTimeDictionary(function () {
                window.message({
                    text: $(".page-dialog-panel").html(),
                    title: "导入数据",
                    width: 500,
                    height: 300,
                    buttons: {
                        "导入": function () {
                                var schoolYear1 = parent.$("#schoolYear1").val();
                                var schoolType1 = parent.$("#schoolType1").val();
                                var term1 = parent.$("#term1").val();
                                var examType1 = parent.$("#examType1").val();
                                var course1 = parent.$("#course1").val();
                                if (parent.$("#schoolType1 option:selected").attr("value") == "") {
                                    window.Msg.alert("请选择学校类型!");
                                    return;
                                }
                                if (parent.$("#term1 option:selected").attr("value") == "") {
                                    window.Msg.alert("请选择学期!");
                                    return;
                                }
                                if (parent.$("#examType1 option:selected").attr("value") == "") {
                                    window.Msg.alert("请选择测试类型!");
                                    return;
                                }
                                if (parent.$("#course1 option:selected").attr("value") == "") {
                                    window.Msg.alert("请选择科目!");
                                    return;
                                }
                                $("input[name='xlsFile']").click();
                                var data = {
                                    schoolYear: schoolYear1,
                                    schoolType: schoolType1,
                                    term: term1,
                                    examType: examType1,
                                    course: course1
                                };
                                $.ajax({
                                    url: "../dataManage/smallTitle.do?command=getImportParams",
                                    type: "POST",
                                    data: data,
                                    dataType: "JSON",
                                    success: function (data) {

                                    }
                                });
                            },
                            "关闭": function () {
                                window.top.$(this).dialog("close");
                            }
                    }
                });
            });
            $("#schoolType1").val("");
            $("#term1").val("");
            $("#examType1").val("");
            $("#course1").val("");
        });
    });
    /* 导入小题分	 */
    function importNewStu(_this) {
        var options = {
            url: "../dataManage/smallTitle.do?command=importSmallTitle",
            type: 'post',
            dataType: "JSON",
            success: function (data, xhr) {
            	//修改父页面div提示信息
               var messageInfo="";
                    if (data.mess == "noData") {
                    	messageInfo="文件中无数据，请重新检查!";
                    	$('.ui-dialog-content.ui-widget-content', parent.document).eq(1).html(messageInfo);
                         return;
                    };
                    var info = "";
                    if (data.message == "error") {
                    	messageInfo="科目不符请重新检查!";
                    	$('.ui-dialog-content.ui-widget-content', parent.document).eq(1).html(messageInfo);
                        return;
                    }
                    if (data.mess == "fileFormatError") {
                    	messageInfo="文件格式错误，请重新检查!";
                    	   //修改父页面div提示信息
                        $('.ui-dialog-content.ui-widget-content', parent.document).eq(1).html(messageInfo);
                         return;
                    };
                    if (data.mess == "success") {
                        info = "导入成功!";
                    } else {
                        info = "导入失败！";
                    }
                    parent.$('.ui-dialog-content.ui-widget-content').eq(1).html(info); 
                    $(listId).trigger("reloadGrid");
                    $("#fileForm").replaceWith($("#fileForm").clone());
                    return false;
                },
                beforeSend: function () {
                    window.message({
                        text: "数据导入中请稍后...",
                        title: "提醒",
                    });
                }, complete: function () {
                   
                }
        };
        $("#fileForm").ajaxSubmit(options);
    }

    /* 查看明细 */
    function queryOper(schoolYear, examType, schoolType, term, examNumber, course, ev) {
        $("#queryDetail thead tr td").remove();
        $("#queryDetail tbody tr td").remove();
        /* 点击详情按钮时把参数传到后台 */
        var url = "../dataManage/smallTitle.do?command=queryDetailList";
        var data = {
            schoolYear: schoolYear,
            examType: examType,
            schoolType: schoolType,
            term: term,
            examNumber: examNumber,
            course: course
        };
        $.ajax({
            url: url,
            type: "POST",
            data: data,
            dataType: "JSON",
            success: function (data) {
                $(".stuName").html(data[0].Name);
                $(".stuExamNumber").html(data[0].Exam_Number);
                $(".schoolYear").html(data[0].School_Year);
                $(".term").html(data[0].Term);
                $(".examType").html(data[0].Exam_Type);
                $(".course").html(data[0].Course);
                $("#queryDetail thead tr").append("<td style='text-align:center;font-weight:bold;width:200px;height:30px;background-color:#3B5617;font-size:14px;color:#fff;word-break:break-all;white-space:nowrap;'>题号</td>");
                $("#queryDetail tbody tr").append("<td style='width:200px;height:30px;text-align:center;'>分数</td>");
                for (var i = 0; i < data.length; i++) {
                    $("#queryDetail thead tr").append("<td style='text-align:center;font-weight:bold;width:200px;height:30px;background-color:#3B5617;font-size:14px;color:#fff'>" + data[i].Question_Number + "</td>");
                    $("#queryDetail tbody tr").append("<td style='width:200px;height:30px;text-align:center;'>" + data[i].Score + "</td>");
                }
            }
        });
        var $i = $(ev.currentTarget).find("i"),
            $piel = $(
                ".page-editor-panel").show({
                effect: "slide",
                direction: "up",
                easing: 'easeInOutExpo',
                duration: 900
            });
    }
    function importExcel(state){
	    var url="../dataManage/smallTitle.do?command=importExcel";
	    var d = {};
	    var t = $("#searchForm").serializeArray();
	    $.each(t, function() {
	      d[this.name] = this.value;
	    });
		var form = $( "#fileReteExportExcel" ) ;
		var paramValue=JSON.stringify(d);
			$("#paramData").val(paramValue);
			form.attr( "action", url) ;
			form.get( 0 ).submit() ; 
    }
</script>
</head>
<body>
    <form id="fileForm" enctype="multipart/form-data" method="post" style="display:none;">
        <input type="file" name="xlsFile" onchange="importNewStu(this);" />
    </form>
    <div class="page-list-panel">
    <form id="fileReteExportExcel" method="post">
		<input id="paramData" name="data" type="hidden" value=""/>
	</form>
        <div class="head-panel">
            <div class="search-panel" style="display:block;">
                <div class="form-panel">
                    <form id="searchForm">
                        <table cellpadding="0" cellspacing="0" border="0">
                            <tr>
                                <td>学年
                                    <select id="schoolYear" name="schoolYear" class="form-control"   style="width:150px;">

                                    </select>
                                </td>
                                <td>
                                    &nbsp;&nbsp;学校类型
                                    <select id="schoolType" name="schoolType" class="form-control" data-dic="{code:'xxlx'}" style="width:150px;">
                                        <option value="">选学校类型</option>
                                    </select>
                                </td>
                                <td>
                                    &nbsp;&nbsp;学期
                                    <select id="term" name="term" class="form-control" data-dic="{code:'xq'}" style="width:150px;">
                                        <option value="">选择学期</option>
                                    </select>
                                </td>
                                <td>
                                    &nbsp;&nbsp;测试类型
                                    <select id="examType" name="examType" class="form-control" data-dic="{code:'kslx'}" style="width:150px;">
                                        <option value="">选测试类型</option>
                                    </select>
                                </td>
                                <td>
                                    &nbsp;&nbsp;科目
                                    <select id="course" name="course" class="form-control" style="width:150px;">
                                        <option value="">选择科目</option>
                                    </select>
                                </td>
                            </tr>
                        </table>
                    </form>
                </div>
            </div>
            <div class="toolbar">
                <table style="height: 100%;" cellpadding="0" cellspacing="0" border="0">
                    <tr>
                        <td style="padding-left: 12px; padding-right: 24px;"><i class="fa fa-list-ul micon"></i>
                        </td>

                        <td class="buttons">
                             <!--  <button id="openFile">
                                <i class="fa fa-sign-in"></i>导入数据
                            </button>-->
                            <button id="importData">
                                <i class="fa fa-sign-in"></i>导入数据
                            </button>
                            <button onclick="importExcel('2')" title="导出" style="margin-left: 0px;">
								<i class="fa fa-search"></i>导出
							</button>
                        </td>
                        <td style="padding-left: 24px; padding-right: 5px;">
                            <input id="fastQueryText" type="text" placeholder="输入考号或姓名" style="line-height: 1em;margin-left:-15px;font-size: 1em; cursor: text;" />
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
    <!-- 查看详情页面 -->
    <div class="page-editor-panel full-drop-panel">
        <div class="title-bar">
            <h4>
				<i class="fa fa-file-text"></i>
			</h4>
            <div class="btn-area">
                <div style="margin-top: 4px;">
                    <button id="cancelBTN">
                        <i class="fa fa-times"></i>返回
                    </button>
                </div>
            </div>
        </div>
        <div class="page-content">
            <div class="page-inner-content">
                <form id="editorForm">
                    <div id="dialog-queryDetail">
                        <div style="text-align: center;font-weight: bold;font-size: medium;">
                            <ul style="list-style:none;margin:0px;padding:0px;">
                                <li>
                                    <span class="stuName"></span>(<span class="stuExamNumber"></span>) &nbsp;
                                    <span class="schoolYear"></span>学年 &nbsp;
                                    <span class="term"></span>
                                    &nbsp;
                                    <span class="examType"></span>
                                    &nbsp;
                                    <span class="course"></span>
                                </li>
                            </ul>
                        </div>
                        <table id="queryDetail" style="margin:10px;margin-top:20px;">
                            <thead>
                                <tr class="text-center">
                                    <!--  <td style="text-align:center;font-weight:bold;">题号</td>
                                    <td style="text-align:center;font-weight:bold;">分数</td>-->
                                </tr>
                            </thead>
                            <tbody>
								<tr class="text-center">
                                    
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </form>
            </div>
        </div>
    </div>
    
    
    <div class="page-dialog-panel full-drop-panel">
    	<div class="head-panel">
            <div class="" style="display:block;position: relative;">
                <div class="form-panel">
		        <form id="searchFormDialog">
		           <table cellpadding="0" cellspacing="0" border="0" style="width: 100%;text-align: center;border-spacing: 10px 10px;">
		               <tr>
		                   <td>
		                       	<span>学&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;年</span>
		                       	<select id="schoolYear1" name="schoolYear" class="form-control"   style="width:150px;">
		
		                       </select>
		                   </td>
		               </tr>
						<tr>
							<td>
								<span>学校类型</span>
								 <select id="schoolType1" name="schoolType" class="form-control" datatime-dic="{code:'xxlx'}" style="width:150px;" onchange="schoolTypeChange()">
								<option value="">选学校类型</option>
								</select>
							</td>
						</tr>
		               <tr>
		               	<td>
		               	<span>学&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;期</span>
		                       	<select id="term1" name="term" class="form-control" datatime-dic="{code:'xq'}" style="width:150px;">
		                           <option value="">选择学期</option>
		                       </select>
		                   </td>
		               </tr>
		               <tr>
		               	<td>
		                       	<span>测试类型</span>
		                       	<select id="examType1" name="examType" class="form-control" datatime-dic="{code:'kslx'}" style="width:150px;">
		                           <option value="">选测试类型</option>
		                       </select>
		                   </td>
		               </tr>
		               <tr>
		               	<td>
		                      	 <span>科&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;目</span>
		                      	 <select id="course1" name="course" class="form-control" style="width:150px;">
		                           <option value="">选择科目</option>
		                       </select>
		                   </td>
		               </tr>
		           </table>
		        </form>
        	</div>
        </div>
    </div>
   </div>
</body>
</html>