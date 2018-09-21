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
	var listId = "#list2",detailId="#list3",editorFormId = "#editorForm", pagerId = '#pager2',editorRelatedFormId = "#editorRelatedForm",
	
	detailListUrl="../dataManage/smallTitle.do?command=queryDetailList";
    $(function () {
        //$(".search-panel").show().data("show", true);

        _initButtons({
            cancelBTN: function () {
                    hideSlidePanel(".page-editor-panel");
                },
                openFile: function () {
                    //data:{schoolYear:document.getElementById("schoolYear").value,schoolType:document.getElementById("schoolType").value,term:document.getElementById("term").value,examType:document.getElementById("examType").value,course:document.getElementById("course").value},
                    var schoolYear = $("#schoolYear").val();
                    var schoolType = $("#schoolType").val();
                    var term = $("#term").val();
                    var examType = $("#examType").val();
                    //var course = $("#course").val();
                    if ($("#schoolType option:selected").attr("value") == "") {
                        window.Msg.alert("请选择学校类型!");
                        return;
                    }
                    if ($("#term option:selected").attr("value") == "") {
                        window.Msg.alert("请选择学期!");
                        return;
                    }
                    if ($("#examType option:selected").attr("value") == "") {
                        window.Msg.alert("请选择测试类型!");
                        return;
                    }
                    //if ($("#course option:selected").attr("value") == "") {
                    //    window.Msg.alert("请选择科目!");
                    //   return;
                    //}
                    $("input[name='xlsFile']").click();
                    var data = {
                        schoolYear: schoolYear,
                        schoolType: schoolType,
                        term: term,
                        examType: examType
                    };
                    $.ajax({
                        url: "../dataManage/studentCode.do?command=getImportParams",
                        type: "POST",
                        data: data,
                        dataType: "JSON",
                        success: function (data) {

                        }
                    });
                }
        }); //from page.common.js
        $("#tblInfo").find("button").button();
        $("#tabs").tabs({
            heightStyle: "fill"
        });
        $("#tabs-2,#tabs-1").css("height", "auto");
        _initFormControls(); //from page.common.js
        _initValidateForXTypeForm(editorFormId);

        
     //resizeFun();
     
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
     });
        //选择学校类型关联科目
        $("#schoolType").change(function () {
            $("#course option[value != '']").remove();
            var schoolType = $("#schoolType").val();
            var url = "../platform/dictionary.do?command=getCoursesByCode";
            var data = {
                schoolType: schoolType
            };
            if(schoolType != ""){
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
    });
    /* 导入小题分	 */
    function importNewStu(_this) {
            var options = {
                url: "../dataManage/studentCode.do?command=importStuCodeExamCode",
                type: 'post',
                dataType: "JSON",
                success: function (data, xhr) {
                    /*if (data.message == "error") {
                        window.Msg.alert("科目不符请重新检查!");
                        return;
                    };*/
                    if (data.mess == "success") {
                        window.Msg.alert("导入成功!");
                    };
                    $("#fileForm").replaceWith($("#fileForm").clone());
                    return false;
                }
            };
            $("#fileForm").ajaxSubmit(options);
        }
			
</script>
</head>
<body>

<form id="fileForm" enctype="multipart/form-data"  method="post" style="display:none;">
	<input type="file" name="xlsFile" onchange="importNewStu(this);"/>
</form>
	<div class="page-list-panel">
		<div class="head-panel">
			<div class="search-panel" style="display:block;">
				<div class="form-panel">
				  <form id="searchForm">
					<table cellpadding="0" cellspacing="0" border="0">
						<tr>
					   		<td>学年
								<select id="schoolYear" name="schoolYear" class="form-control" data-dic="{code:'ND'}" style="width:150px;">
					
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
					   		<!--<td>
					   			&nbsp;&nbsp;科目
								<select id="course" name="course" class="form-control" style="width:150px;">
									<option value="">选择科目</option>
								</select>
					   		</td>-->
						  </tr>
						</table>
					</form>
				</div>
			</div>
			<div class="toolbar">
				<table style="height: 100%;" cellpadding="0" cellspacing="0"
					border="0">
					<tr>
						<td style="padding-left: 12px; padding-right: 24px;"><i
							class="fa fa-list-ul micon"></i></td>
							
						<td class="buttons">
							<button id="openFile">
								<i class="fa fa-plus"></i>学籍号考号关系导入
							</button> 
						</td>
					</tr>
				</table>
			</div>
		</div>
		<table id="list2"></table>
		<div id="pager2"></div>
	</div>
 	
 	<!-- 查看详情页面 -->
 	<!--  <div class="page-list-panel-detail">
		<div class="head-panel">
			
		</div>
		<table id="list3"></table>
	</div>-->
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
  	 								<span class="stuName"></span>(<span class="stuExamNumber"></span>)
  	 								&nbsp;<span class="schoolYear"></span>年
  	 								&nbsp;<span class="term"></span>
  	 								&nbsp;<span class="examType"></span>
  	 								&nbsp;<span class="course"></span>
  	 								</li>
  	 							</ul>
  	 						</div>
							<table  id="queryDetail" style="margin-left:360px;margin-top:20px;">
								<thead>
									<tr class="text-center">
										<td style="text-align:center;font-weight:bold;">题号</td>
										<td style="text-align:center;font-weight:bold;">分数</td>
									</tr>
								</thead>
								<tbody>
								
								</tbody>
							</table>
					</div>
				</form>
			</div>
		</div>
	</div>
</body>
</html>