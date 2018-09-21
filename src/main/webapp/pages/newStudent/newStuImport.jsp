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
<link href="../theme/default/page.common.css" rel="stylesheet" />
<script type="text/javascript" src="../js/jquery.js"></script>
<script type="text/javascript" src="../js/ui.custom.js"></script>
<script type="text/javascript" src="../js/ui.jqgrid.js"></script>
<script type="text/javascript" src="../js/ui.autosearch.js"></script>
<script type="text/javascript" src="../js/ui.chosen.js"></script>
<script type="text/javascript" src="../js/ui.uploadfile.js"></script>
<script type="text/javascript" src="../js/ui.common.js"></script>
<script type="text/javascript" src="../js/jquery.validate.js"></script>
<script type="text/javascript" src="../js/page.common.js"></script>
<style>
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

.btn_edit {
	background: #FFFFFF;
	border: 1px solid #3babe3;
	width: auto;
	color: #3babe3;
	cursor: pointer;
	font-weight: bold;
	padding: .4em 1em;
}

.fonts_style {
	font-weight: bold;
	color: #3BABE3;
	font-size: larger
}
.checkbox 
{  
width: 600px; 
margin: 5px,6px,6px,6px; 
height: auto; 
overflow: hidden; 
} 

#loading{ background:url(bak.png) 0 0 no-repeat; width:397px; height:49px;}
#loading div{ background:url(pro.png) 0 0 no-repeat; line-height:49px;height:49px; text-align:right;}
  
</style>
<script type="text/javascript">
	var listId = "#list2", editId = "", pagerId = '#pager2', //分页
	listUrl = "../propertyManage/intoStock.do?command=search", 
	saveUrl = "../propertyManage/intoStock.do?command=submit", 
	updateUrl = "../propertyManage/intoStock.do?command=update", 
	deleteUrl = "../propertyManage/intoStock.do?command=delete", 
	saveuploadUrl = "../propertyManage/intoStock.do?command=saveupload", 
	removeUrl = "../propertyManage/intoStock.do?command=removeload", 
	uploadUrl = "../propertyManage/intoStock.do?command=upload";

	var progress_id = "loading";
	function SetProgress(progress) {
	if (progress) {
	$("#" + progress_id + " > div").css("width",String(progress) + "%"); //控制#loading div宽度
	$("#" + progress_id + " > div").html(String(progress) + "%"); //显示百分比
	}
	}
	var i = 0;
	function doProgress() {
	if (i > 500) {
	$("#message").html("恭喜您，歌曲上传成功！谢谢您对九酷的支持！").fadeIn("slow");//加载完毕提示
	return;
	}
	if (i <= 500) {
	setTimeout("doProgress()",500);
	SetProgress(i);
	i++;
	}
	}
	$(function() {
		doProgress();
		$("#tblInfo").find("button").button();
		$("#inputBTN").click(function() {
			$("#uploaddialog").dialog("open");
		});

		$("#uploaddialog").dialog({
			autoOpen : false,
			resizable : false,
			height : 250,
			width : 600,
			modal : true,
			buttons : {
				"确定" : function() {
					var data = $("#uploadfile").data().fileItems();
					var sendpath = [];
					for (var i = 0; i < data.length; i++) {
						var valdata = data[i];
						var filepath = valdata.path;
						sendpath.push(filepath);
					}
					$.post(saveuploadUrl, {
						sendId : sendpath.join("|")
					}, function(data) {
						var jsondata = $.parseJSON(data);
						if (jsondata.message == 'success') {
							if(jsondata.messageContext != null && jsondata.messageContext != ""){
								jsondata.messageContext = jsondata.messageContext.replace("::","\n");
								alert(jsondata.messageContext);
							}else{
								alert("保存成功");
							}
							$("#uploaddialog").dialog("close");
							/* $(listId).trigger("reloadGrid"); */
							window.location.reload();
						} else {
							alert("保存失败");
						}
					});
				},
				"取消" : function() {
					$(this).dialog("close");
				}
			}
		});

		//		附件上传
		$("#uploadfile").uploadFile({
			url : uploadUrl,
			removeUrl : removeUrl,
			returnType : "JSON",
			showDone : false,
			showStatusAfterSuccess : false
		});

		_initFormControls();
		_initButtons();
		});

</script>
</head>
</body>
<div class="page-list-panel">
	<div class="head-panel">
		<div class="toolbar">
			<table style="height: 100%;" cellpadding="0" cellspacing="0"
				border="0">
				<tr>
					<td style="padding-left: 12px; padding-right: 24px;"><i
						class="fa fa-list-ul micon"></i></td>
					<td style="padding-left:10px;padding-right: 10px;">年级：
						<select id="fastQueryText" type="text" name="text"
							style="line-height: 1em; font-size: 1em; cursor: text;width:150px;" class="form-control">
							<option>2016级新生</option>
							<option>2017级新生</option>
							<option>2018级新生</option>
							</select>
					</td>
					<td class="buttons">
						<button id="inputBTN">
							<i class="fa fa-paperclip"></i>导入
						</button>
					</td>
				</tr>
			</table>
		</div>
	</div>
</div>
<div id=”message”></div>
<div id=”loading”><div></div></div>
<div class="page-view-panel full-drop-panel"></div>

<div id="uploaddialog">
	<div id="uploadfile" style="width: 300px;">上传文件</div>
</div>
</body>
</html>