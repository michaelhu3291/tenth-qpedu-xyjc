<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title></title>
<link href="../theme/default/ui.custom.css" rel="stylesheet" />
<link href="../theme/default/ui.jqgrid.css" rel="stylesheet" />
<link href="../theme/default/font.awesome.css" rel="stylesheet" />
<link href="../theme/default/ui.chosen.css" rel="stylesheet" />
<link href="../theme/default/ui.uploadfile.css" rel="stylesheet" />
<link href="../theme/default/page.common.css" rel="stylesheet" />
<link href="../theme/default/ui.ztree.css" rel="stylesheet" />
<link href="../theme/default/ui.jqpagination.css" rel="stylesheet" />
<script type="text/javascript" src="../js/jquery.js"></script>
<script type="text/javascript" src="../js/ui.custom.js"></script>
<script type="text/javascript" src="../js/ui.jqgrid.js"></script>
<script type="text/javascript" src="../js/ui.autosearch.js"></script>
<script type="text/javascript" src="../js/ui.chosen.js"></script>
<script type="text/javascript" src="../js/ui.uploadfile.js"></script>
<script type="text/javascript" src="../js/ui.common.js"></script>
<script type="text/javascript" src="../js/jquery.validate.js"></script>
<script type="text/javascript" src="../js/ui.ztree.js"></script>
<script type="text/javascript" src="../js/page.common.js"></script>
<script type="text/javascript" src="../js/ui.jqpagination.js"></script>
<script type="text/javascript" src="../js/util.js"></script>
<script type="text/javascript" src="../js/kindeditor-all.js"></script>
<script type="text/javascript" src="../js/lang/zh_CN.js"></script>
<script type="text/css">
.meeting_but{
		background:#FFFFFF;
		border:1px solid #3babe3;
		width:auto;
		height:30px;
		color:#3babe3;
		cursor:pointer;
		line-height:1px;
	}
	.meeting_but:hover{
		color:#FFFFFF;
		background:#3babe3;
		}
	.select td{
		text-align:right;
		width:100px;
	}
</script>
</head>
<script type="text/javascript">
	var listId = "#list2",
	editorFormId = "#editorForm", 
	listUrl = "../description/descriptionList.do?command=load",editor;

	var setKindEdit=function(){
			
  		KindEditor.ready(function(K) {
				editor = K.create('textarea[name="context"]', {
					resizeType : 1,
					allowPreviewEmoticons : false,
					allowImageUpload : false,
					items : [
						'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline',
						'removeformat', '|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',
						'insertunorderedlist', '|', 'emoticons', 'image', 'link']
				});
			});
		
		};
  		
  	$(function() {
  		setKindEdit();
		//初始化按钮
		_initButtons({});
		_initFormControls();
		 _initValidateForXTypeForm(editorFormId);
		 
		resizeFun();
	});
	
  	var	editorSave=function()
    {
  		var data=$(editorFormId).getFormData();
  			data.context=editor.html();
		var saveUrl = "../description/descriptionList.do?command=submit";
    	POST(saveUrl,data, function (data) {
        });
    	
    }
  	
  /* 	var setdata=function(){
	  		$.post(listUrl,null,function(data){
	  		         var obj=$.parseJSON(data);
					 editor.insertHtml(obj.context);	
			});
  		}; */

</script>
<body>
	<div class="page-list-panel">
	<input type="hidden" id="id" name="id">
	<input type="hidden" id="stuId" name="stuId">
		<div class="head-panel">
		</div>
		
		<div class="title-bar">
			<h4>
				<i class="fa fa-plus"></i>
			</h4>
			<div class="btn-area">
				<div style="margin-top: 4px;">
					<button id="editorSave" onclick="editorSave()">
						<i class="fa fa-check"></i>保存
					</button>
				</div>
			</div>
		</div>
		<div class="page-content">
			<div class="page-inner-content">
				<form id="editorForm">
				    <input name="id" type="hidden"/>
				    <table class="tableTemplet" style="width:780px;height:508px;overflow:auto;margin-top:30px;margin-left:auto;margin-right:auto;">
						<thead>
							<tr height="42">
								<th colspan="2">
									<i class="fa fa-file-text"></i>
									<span>说明信息</span>
								</th>
							</tr> 
						</thead>
						<tbody id="editorArea">
							<tr>
								<td class="label">说明类型:</td>
								<td>
									<select data-xtype="chosen" data-validate="{required:true}" name="smlx" style="width:658px;">
										<c:forEach items="${smlx}" var="A">
											<option value="${A.DictionaryName}">${A.DictionaryName}</option>
										</c:forEach>
									</select>
								</td>
							</tr>
							<tr>
								<td class="label">内容：</td>
								<td>
									<textarea name="context" style="width:500px;height:400px;"></textarea>
								</td>
							</tr>
						</tbody>
					</table>
				    
				</form>
			</div>
		</div>
		<table id="list2"></table>
	</div>
  <div class="page-view-panel full-drop-panel"></div>
</body>
</html>