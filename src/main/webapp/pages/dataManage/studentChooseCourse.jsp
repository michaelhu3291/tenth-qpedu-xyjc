<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
	.excelDownLoad{
		text-decoration:none;
		font-size:16px;
	}
	.excelDownLoad:hover{
		text-decoration:underline;
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
                    
                    $("input[name='xlsFile']").click();
                    
                   
                }
                //downFile: function(){
                	//$("input[name='downloadFile']").click();
                //}
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
     /*loadDictionary(function () {
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
     });*/
        //选择学校类型关联科目
        /*$("#schoolType").change(function () {
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
           
        });*/
        
    });
    /* 导入考号和科目(学生选课)	 */
    function importNewStu(_this) {
            var options = {
                url: "../dataManage/studentChooseCourse.do?command=importStuCodeAndCourse",
                type: 'post',
                dataType: "JSON",
                success: function (data, xhr) {
                	if (data.mess == "noData") {
                        window.Msg.alert("文件中无数据，请重新检查!");
                        return;
                    };
                    if (data.mess == "fileFormatError") {
                        window.Msg.alert("文件格式错误，请重新检查!");
                        return;
                    };
                    if (data.mess == "courseNotMatch") {
                        window.Msg.alert("科目不符，请重新检查!");
                        return;
                    };
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
			<div class="toolbar">
				<table style="height: 100%;" cellpadding="0" cellspacing="0"
					border="0">
					<tr>
						<td style="padding-left: 12px; padding-right: 24px;"><i
							class="fa fa-list-ul micon"></i></td>
							
						<td class="buttons">
							<button id="openFile">
								<i class="fa fa-sign-in"></i>学生选科数据导入
							</button>
							<button id="downloadExcel">
								<i class="fa fa-download"></i><a style="font-size: 12px;" href="../../academic/download/学生选科模板.xlsx" class="excelDownLoad">模板下载</a>
							</button>
							<!-- <a href="#" role="button" onclick="javascript:document.execCommand('saveAs')">另存为HTML文档</a>-->
						</td>
					</tr>
				</table>
			</div>
		</div>
		<div class="head-panel" style="color: red;font-size: 15px;font-family:微软雅黑;">注：导入数据前请先下载模板</div>
	</div>
</body>
</html>