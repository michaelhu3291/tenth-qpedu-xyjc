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
		width:100%;
		height:100%;
		border:none;
		text-align: center;
		background-color: #CAF1BD;
	}
	#stu_look td{
		height:36px;
		text-align: center;
	}
	#stu_look{
		width:92%;
		margin-top:8px;
		margin-left:auto;
		margin-right:auto;
		margin-bottom: 15px;"
}
</style>
<script type="text/javascript">
	var listId = "#list2",
	editorFormId = "#editorForm",
	pagerId = '#pager2',
	editorRelatedFormId = "#editorRelatedForm",
	  
	listUrl = "../applyForm/regTransfer.do?command=search";
	$(function() {
		_initButtons();//from page.common.js
		$("#tblInfo").find("button").button();
		 $( "#tabs" ).tabs( { heightStyle:"fill" } ) ;
		_initFormControls();//from page.common.js
		_initValidateForXTypeForm(editorFormId);
         showSlidePanel(".page-editor-panel").find("h4 i").removeClass();
	});
</script>
</head>
<body>
<div class="page-list-panel">
	<div class="head-panel">
		<div class="search-panel">
			<div class="form-panel">
			<input name="id" id="id" type="hidden">
			</div>
		</div>
		</div>
	</div>
<div class="page-editor-panel full-drop-panel">
		 <div class="title-bar">
           <h4>
                <i class="fa fa-plus">入党积极分子培养考察登记表</i>
            </h4>
            <div class="btn-area">
                <div style="margin-top: 4px;">
                    <button id="submit" onclick="submit()">
                        <i class="fa fa-check"></i>提交
                    </button>
                    <button id="editorSave" onclick="editorSave()">
                        <i class="fa fa-share"></i>导出
                    </button>
                    <button id="resetBTN" onclick="resetBTN()">
                        <i class="fa fa-undo"></i>重置
                    </button>
                </div>
            </div>
        </div>
        <div class="page-content">
            <div class="page-inner-content" style="width: 83%;margin-top:27px;background-color: #F6F6F6;margin-left:auto;margin-right:auto;">
	   
	   <h1 style="text-align: center;">
				入党积极分子培养考察登记表</h1>
		 <table id="stu_look"  cellspacing="0" border="1">
			<tr>
				<td style="width:60px;" colspan="2">学院</td>
				<td><input id="XY" name="XY" value="美术学院"></td>
				<td style="width:60px;">专业</td>
				<td> <input id="ZYMC" name="ZYMC" value="素描"></td>
				<td style="width:67px;">职务</td>
				<td><input id="ZW" name="ZW" value="学生处部长"></td>
			</tr>
			<tr>
				<td style="width:60px;" rowspan="2">姓名</td>
				<td style="width:60px;" >现名</td>
				<td><input id="NAME_NOW" name="NAME_NOW" value="李达"></td>
				<td style="width:60px;">性别</td>
				<td> <input id="XBM" name="XBM" value="男"></td>
				<td style="width:67px;">家庭出生</td>
				<td><input id="STUDENTS" name="STUDENTS" value="湖南长沙"></td>
			</tr>
			<tr>
				<td style="width:60px;" >曾用名</td>
				<td><input id="NAME_NOW" name="NAME_NOW" value="李小达"></td>
				<td style="width:60px;">出生年月</td>
				<td> <input id="XBM" name="XBM" value="1990-01-11"></td>
				<td style="width:67px;">本人成分</td>
				<td><input id="STUDENTS" name="STUDENTS" value="湖南长沙"></td>
			</tr>
			<tr>
				<td style="width:60px;" colspan="2">参加工作年月</td>
				<td><input id="XY" name="XY" ></td>
				<td style="width:60px;">文化程度</td>
				<td> <input id="ZYMC" name="ZYMC"></td>
				<td style="width:67px;">政治面貌</td>
				<td><input id="ZW" name="ZW" ></td>
			</tr>
			<tr>
				<td style="width:60px;" colspan="2">籍贯</td>
				<td colspan="3"><input id="XY" name="XY" ></td>
				<td style="width:67px;">民族</td>
				<td><input id="ZW" name="ZW" ></td>
			</tr>
			<tr>
				<td colspan="2">部门或班级</td>
				<td colspan="5"><input id="POLITICS_STATUS" name="POLITICS_STATUS" style="text-align: left;"></td>
			</tr>
			<tr>
				<td colspan="2">何时提出入党申请</td>
				<td colspan="2"><input id="POLITICS_STATUS" name="POLITICS_STATUS" value=""></td>
				<td colspan="2">何时确定为入党积极分子</td>
				<td ><input id="POLITICS_STATUS" name="POLITICS_STATUS" value=""></td>
			</tr>
			<tr style="height: 70px;">
				<td colspan="2">家庭主要成员及政治面貌</td>
				<td colspan="5"><input id="POLITICS_STATUS" name="POLITICS_STATUS" style="text-align: left;"></td>
			</tr>
			<tr style="height: 70px;">
				<td colspan="2">主要社会关系及政治面貌</td>
				<td colspan="5"><input id="POLITICS_STATUS" name="POLITICS_STATUS" style="text-align: left;"></td>
			</tr>
			<tr style="height: 70px;">
				<td colspan="2">主要经历</td>
				<td colspan="5"><input id="POLITICS_STATUS" name="POLITICS_STATUS" style="text-align: left;"></td>
			</tr>
			<tr style="height: 70px;">
				<td colspan="2">何时何地受何种奖励或处分</td>
				<td colspan="5"><input id="POLITICS_STATUS" name="POLITICS_STATUS" style="text-align: left;"></td>
			</tr>
			
			<tr>
				<td colspan="7" style="text-align: left;">
				<p style="text-align: center;">谈话记录</p>
				<textarea rows="9" cols="110"></textarea>
				<p style="text-align: right;">联系人签名：&nbsp;&nbsp;&nbsp;&nbsp;</p>
				<p style="text-align: right;">年&nbsp;&nbsp;月&nbsp;&nbsp;日</p>
				</td>
			</tr>
			<tr>
				<td colspan="7" style="text-align: left;">
				<textarea rows="9" cols="110"></textarea>
				<p style="text-align: right;">联系人签名：&nbsp;&nbsp;&nbsp;&nbsp;</p>
				<p style="text-align: right;">年&nbsp;&nbsp;月&nbsp;&nbsp;日</p>
				</td>
			</tr>
			
			<tr>
				<td colspan="7" style="text-align: left;">
				<p style="text-align: center;">联 系 人 考 察 意 见</p>
				<textarea rows="9" cols="110"></textarea>
				<p style="text-align: right;">联系人签名：&nbsp;&nbsp;&nbsp;&nbsp;</p>
				<p style="text-align: right;">年&nbsp;&nbsp;月&nbsp;&nbsp;日</p>
				</td>
			</tr>
			<tr>
				<td colspan="7" style="text-align: left;">
				<p style="text-align: center;">支  部 委 员 会 初 步 分 析 意 见</p>
				<textarea rows="9" cols="110"></textarea>
				<p style="text-align: right;">党支部书记签名：&nbsp;&nbsp;&nbsp;&nbsp;</p>
				<p style="text-align: right;">年&nbsp;&nbsp;月&nbsp;&nbsp;日</p>
				</td>
			</tr>
			<tr>
				<td  colspan="2"  style="text-align: left;">党总支意见
				</td>
				<td colspan="5">
				<textarea rows="10" cols="92"></textarea>
				<p style="text-align: right;">党支部书记签名：&nbsp;&nbsp;&nbsp;&nbsp;</p>
				<p style="text-align: right;">年&nbsp;&nbsp;月&nbsp;&nbsp;日</p>
				</td>
			</tr>
		
			<tr>
					<td colspan="7" style="line-height: 22px;text-align: justify;
									    text-justify: inter-ideograph;
									    text-indent: 3em;">说    明	

1.	本表由建党对象所在党支部保管。吸收入党后，连同其他审查材料一起报存上级党组织。
2.	联系人（党员担任）应定期记录同入党积极分子正式谈话的情况，每三个月记录一次。
3.	发展党员时，联系人应该根据入党条件填写入党积极分子的全面考察意见。
4.	支部委员会初步分析意见的内容包括：入党积极分子对党的认识、入党动机、政治思想、工作表现、业务水平、缺点和弱点，以及支委会讨论意见。
5.	本表一律用钢笔填写，要求字迹端正清楚。

					</td>
				</tr>	
			</table>
            </div>
        </div>
	</div>
</body>
</html>