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
<script type="text/javascript" src='../js/highcharts.js'></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-ui-1.10.4.custom.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/url.js"></script>
<style>
	#tblInfo input{
		background-color: #F6F6F6;
		border:none;
	} 
	#stu_look input{
		height:80%;
		border:none;
		text-align: center;
		width:80%;
	}
	#stu_look td{
		height:36px;
		text-align: center;
		
	}
	#stu_look{
		width:96%;
		margin-top:8px;
		margin-left:auto;
		margin-right:auto;
		margin-bottom: 0px;"
	}
	.page-content .page-inner-content h1{
		margin: auto;
		width: 66%;
		text-align: center;
		margin-bottom: 23px;
	}
</style>
<script type="text/javascript">
	var listId = "#list2",
	editorFormId = "#editorForm",
	pagerId = '#pager2',
	editorRelatedFormId = "#editorRelatedForm",
	resultMap = <%=request.getAttribute("resultMap")%>,
	listUrl = "../applyForm/regTransfer.do?command=search";
	$(function() {
		
		_initButtons({
			editorSave : function(ev) {
				var obj = {},
					graduateToGo = $("select[name=GraduateToGo]").find("option:selected").val();
				var formData = $("#BYQX-KQYJS_Iframe").contents().find("table").getFormData();
				formData.DICTIONARYCODE = graduateToGo;
				formData.FLAG="2";
				$.post(projectName+"/leaveSchool/graduationToGo.do?command=save"+graduateToGo.replace("-",""),formData,function(data){
					if(data=="1"){
						window.Msg.alert("已成功提交!");
					}else{
						window.Msg.alert("提交失败!");
					}
					
				});
			},
			savemessage:function(ev){
				var obj = {},
				graduateToGo = $("select[name=GraduateToGo]").find("option:selected").val();
				var formData = $("#BYQX-KQYJS_Iframe").contents().find("table").getFormData();
				formData.DICTIONARYCODE = graduateToGo;
				formData.FLAG="1";
				$.post(projectName+"/leaveSchool/graduationToGo.do?command=save"+graduateToGo.replace("-",""),formData,function(data){
					if(data=="1"){
						window.Msg.alert("保存成功!");
					}else{
						window.Msg.alert("保存失败!");
					}
					
				});
			},
			resetBTN:function(ev){
				alert("resetBTN");
			}
		});//from page.common.js
		
		_initFormControls();//from page.common.js
		_initValidateForXTypeForm(editorFormId);
	});
	
</script>
</head>
<body>
<div class="page-list-panel">
	<div class="head-panel">
		<div class="search-panel">
			<div class="form-panel">
			<input data-dtype="string" data-xtype="text" name="id" id="id" type="hidden">
			</div>
		</div>
		</div>
</div>
<div class="page-editor-panel full-drop-panel" style="display:block;">
		 <div class="title-bar">
          	 <h4>
                <i class="fa fa-plus">&nbsp;&nbsp;入党积极分子选拔登记表</i>
            </h4>
         
            <div class="btn-area">
                <div style="margin-top: 4px;">
                    <button id="editorSave" >
                        <i class="fa fa-check"></i>提交
                    </button>
                    <button id="resetBTN" >
                        <i class="fa fa-undo"></i>重置
                    </button>
                </div>
            </div>
        </div>
        <div class="page-content">
            <div class="page-inner-content" style="width: 88%;margin-top:27px;background-color: #F6F6F6;margin-left:auto;margin-right:auto;margin-bottom: 50px;">
	   		<h1>上海视觉艺术学院入党积极分子选拔登记表</h1>
	   		<table id="stu_look"  cellspacing="0" border="1">
			<tr>
				<td style="width:100px;">姓名</td>
				<td><input data-dtype="string" data-xtype="text" id="ryh" name="ryh" ></td>
				<td style="width:100px;">性别</td>
				<td><input data-dtype="string" data-xtype="text" id="xm" name="xm"></td>
				<td style="width:100px;">出生年月</td>
				<td><input data-dtype="string" data-xtype="text" id="syd" name="syd"></td>
			</tr>
			<tr>
				<td style="width:100px;">进校时间</td>
				<td><input data-dtype="string" data-xtype="text" id="xy" name="xy"></td>
				<td>部门/学院</td>
				<td> <input data-dtype="string" data-xtype="text" id="zymc" name="zymc"></td>
				<td>入党申请时间</td>
				<td> <input data-dtype="string" data-xtype="text" name="zymc"></td>
			</tr>
			<tr>
				<td style="width:80px;">原单位</td>
				<td colspan="3"><input data-dtype="string" data-xtype="text" id="xy" name="xy" style="width:92%"></td>
				<td>培养联系人</td>
				<td> <input data-dtype="string" data-xtype="text" id="zymc" name="zymc"></td>
			</tr>
			<tr style="height: 130px;">
				<td>主要简历（学校﹨单位、从事工作）</td>
				<td colspan="5"><input data-dtype="string" data-xtype="text" id="sfzjh" name="sfzjh" style="width: 95.3%"></td>
			</tr>
			<tr style="height: 130px;">
				<td>奖励情况</td>
				<td colspan="5"><input data-dtype="string" data-xtype="text"  name="sfzjh" style="width: 95.3%"></td>
			</tr>
			<tr style="height: 130px;">
				<td>主要表现</td>
				<td colspan="5"><input data-dtype="string" data-xtype="text"  name="sfzjh" style="width: 95.3%"></td>
			</tr>
			<tr style="height: 130px;">
				<td>自我评价</td>
				<td colspan="5"><input data-dtype="string" data-xtype="text"  name="sfzjh" style="width: 95.3%"></td>
			</tr>
			<tr style="height: 130px;">
				<td>联系人意见</td>
				<td colspan="5"><input data-dtype="string" data-xtype="text"  name="sfzjh" style="width: 95.3%"></td>
			</tr>
			<tr style="height: 130px;">
				<td>支部意见</td>
				<td colspan="5"><input data-dtype="string" data-xtype="text"  name="sfzjh" style="width: 95.3%"></td>
			</tr>
			
			</table>
            </div>
        </div>
</div>
</body>
</html>