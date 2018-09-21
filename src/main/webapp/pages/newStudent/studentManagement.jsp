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
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-ui-1.10.4.custom.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/url.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/util.js"></script>

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
</style>
<script type="text/javascript">
	var listId = "#list2", editorFormId = "#editorForm", pagerId = '#pager2',editorRelatedFormId = "#editorRelatedForm",
	  
	listUrl = "../newStudent/studentManagement.do?command=searchNewComer";
	$(function() {
		loadXY();
		_initButtons({
			cancelBTN : function() {
				$(listId).trigger("reloadGrid");
				hideSlidePanel(".page-editor-panel");
				editId = '';
			}
		});//from page.common.js
		$("#tblInfo").find("button").button();
		 $( "#tabs" ).tabs( { heightStyle:"fill" } ) ;
		 $("#tabs-2,#tabs-1").css("height","auto");
		_initFormControls();//from page.common.js
		_initValidateForXTypeForm(editorFormId);
		
		
		/* 学院联动下拉框专业 
		*** 点击学院，拼接专业的下拉框
		*/
		function loadXY(){
			var time = new Date();
			var year = time.getFullYear();
			xyUrl="../newStudent/studentManagement.do?command=getXyList";
			POST(xyUrl,{year:year},function(xyList){
				var str="<option value=''>全选</option>";
				for(var i = 0;i<xyList.length;i++){
					var str= str+ "<option value='"+xyList[i].name+"'>"+ xyList[i].name+"</option>";
				}
				$("#college option").remove();
				$("#college").append(str);
			});
		}
		$("#college").change(function () {
				var url="../newStudent/studentManagement.do?command=getZyBjList";
				var Name = $("#college").val();
				$.post(url,{Name:Name},function(data){
					var zyList=$.parseJSON(data);
					var str="<option value=''>全选</option>";
					for(var i = 0;i<zyList.length;i++){
						var str= str+ "<option value='"+zyList[i].name+"'>"+ zyList[i].name+"</option>";
					}
					$("#profession option").remove();
					$("#profession").append(str);
				});
		});
		$("#profession").change(function () {
			var url="../newStudent/studentManagement.do?command=getZyBjList";
			var Name = $("#profession").val();
			$.post(url,{Name:Name},function(data){
				var bjList=$.parseJSON(data);
				var str="<option value=''>全选</option>";
				for(var i = 0;i<bjList.length;i++){
					var str= str+ "<option value='"+bjList[i].name+"'>"+ bjList[i].name+"</option>";
				}
				$("#grade option").remove();
				$("#grade").append(str);
			});
	});
		
		var _colModel = [
			       		    {name : 'ID',key : true,width:40,hidden : true},
			       		    {name : 'RYH',width : 100,align : "center"},
			       		    {name : 'XM',width : 80,align : "left"},
			       		    {name : 'xymc',width : 130,align : "left"},
			       		    {name : 'zymc',width : 130,align : "left"},
			       		    {name : 'bjmc',width : 130,align : "left"},
			       		    {name : 'DORM_NUM',width:80,align : "center"},
			       		    {name : 'BDZT',width : 60,align : "center",formatter:function(a,b,c){
			       		    	if(c.BDZT=="1"){
			       		    		return "<span>已报道<span>";
			       		    	}else{
			       		    		return "<span>未报道<span>";
			       		    	}
			       		    }},
			       		    {name : 'JFZT',width:60,align : "center"},
			       		    {name : 'XSDA',width : 60,align : "center",formatter:function(a,b,c){
			       		    	if(c.XSDA=="1"){
			       		    		return "<span>已接收<span>";
			       		    	}else{
			       		    		return "<span>未接收<span>";
			       		    	}
			       		    }},
			       		    {name : 'HJQY',width:60,align : "center",formatter:function(a,b,c){
			       		    	if(c.HJQY=="1"){
			       		    		return "<span>已接收<span>";
			       		    	}else{
			       		    		return "<span>未接收<span>";
			       		    	}
			       		    }},
			       		    {name : 'LYGX',width : 60,align : "center",formatter:function(a,b,c){
			       		    	if(c.LYGX=="1"){
			       		    		return "<span>已接收<span>";
			       		    	}else{
			       		    		return "<span>未接收<span>";
			       		    	}
			       		    }},
			       		    {name : 'DTGX',width:60,align : "center",formatter:function(a,b,c){
			       		    	if(c.DTGX=="1"){
			       		    		return "<span>已接收<span>";
			       		    	}else{
			       		    		return "<span>未接收<span>";
			       		    	}
			       		    }},
			       		    {name : 'BZ',width : 100,align : "left"},
			       		   {
			     					name:'', width:80, align:"center", sortable:false, formatter:function( ar1, ar2, ar3 )
			     			        {
			     						 var operStr="<button id='queryBTN' class='page-button' style='border: 1px solid #F58D06;'title='修改' onclick='queryOper(\""
			     								+ ar3.ID + "\");'><i class='fa fa-file-text'></i>修改</button>";
			     						 
			     			             return operStr;
			     			        } 
			     			}
			       			], 
			       			_colNames = ['','学号 ','姓名', '学院','专业','班级','寝室','报到状态','缴费情况 ','学生档案',
			       			             '户籍迁移','粮油关系','党团关系','备注','操作'];
 
			     		$(listId).jqGrid($.extend(defaultGridOpts,
			     			{url:listUrl,
			     			colNames:_colNames,
			     			colModel:_colModel,
			     			pager:pagerId}));
			     		resizeFun();
			     		
	     		});
			//修改新生信息
			function queryOper(id) {
				showSlidePanel(".page-editor-panel").find("h4 i").removeClass();
				$("#stuId").val(id)
				$("#tblInfo input").attr("disabled",true);
				$("#stu_look input").attr("disabled",true);
				loadInfo(id);
			};	
			
			
			
			//加载新生信息
			function loadInfo(id){

				  var stuInforUrl = "../newStudent/studentManagement.do?command=loadByStuId"
				  var $grid=$(listId);
				  var data= $grid.jqGrid("getRowData",id);
				  $("#stuName1").val(data.XM);
				  $("#stuNumber1").val(data.RYH);
				  $("#college1").val(data.xymc);
				  $("#profession1").val(data.zymc);
				  $("#zhuanye").val(data.zymc);
				  $("#banJi").val(data.bjmc);
				  $("#dangAn").val(data.XSDA);
				  $("#COLLEGE").val(data.xymc);
				  $("#PROFESSION").val(data.zymc);
				  
				  
				  POST(stuInforUrl, {stuId:data.ID}, function(data) {
						$("#XY").val(data.stuPersonInfo.xy);
		            	$("#ZYMC").val(data.stuPersonInfo.zymc);
		            	$("#RYH").val(data.stuPersonInfo.ryh);
		            	$("#XM").val(data.stuPersonInfo.xm);
		            	if(data.stuPersonInfo.xbm=="1"){
		            		$("#SEX").val("男");
		            	}else{
		            		$("#SEX").val("女");
		            	}
		            	$("#CSRQ").val(data.stuPersonInfo.csrq);
		            	$("#ZZMM").val(data.stuPersonInfo.zzmm);
		            	$("#MZ").val(data.stuPersonInfo.mz);
		            	$("#SFPG").val(data.stuPersonInfo.sfpg);
		            	$("#SFYJ").val(data.stuPersonInfo.sfyj);
		            	$("#JG").val(data.stuPersonInfo.jg);
		            	$("#YDDH").val(data.stuPersonInfo.yddh);
		            	$("#DZXX").val(data.stuPersonInfo.dzxx);
		            	$("#JSTXH_QQ").val(data.stuPersonInfo.jstxhqq);
		            	$("#JSTXH_WX").val(data.stuPersonInfo.jstxhwx);
		            	$("#SFZJH").val(data.stuPersonInfo.sfzjh);
		            	$("#SG").val(data.stuPersonInfo.sg);
		            	$("#TZ").val(data.stuPersonInfo.tz);
		            	$("#XZM").val(data.stuPersonInfo.xzm);
		            	$("#YFCC").val(data.stuPersonInfo.yfcc);
		            	$("#SW").val(data.stuPersonInfo.sw);
		            	$("#JTZZ").val(data.stuPersonInfo.jtzz);
		            	$("#JTYZBM").val(data.stuPersonInfo.jtyzbm);
		            	$("#JTQK").val(data.stuPersonInfo.jtqk);
		            	$("#CRZW").val(data.stuPersonInfo.crzw);
		            	$("#JCQK").val(data.stuPersonInfo.jcqk);
		            	$("#XQAH").val(data.stuPersonInfo.xqah);
		            	$("#ZZZW").val(data.stuPersonInfo.zzzw);
		            	$("#ZZJS").val(data.stuPersonInfo.zzjs);
		            	$("#JTPKQK").val(data.stuPersonInfo.jtpkqk);
		            	$("#LXBF").val(data.stuPersonInfo.lxbf);
		            	$("#JKZKM").val(data.stuPersonInfo.jkzkm);
		            	$("#TC").val(data.stuPersonInfo.tc);
		            	
		            	var familyMember = data.listStuFamilyMember;
		            	if(familyMember.length>0){
							var num = $("#familyInfo").attr("rowspan");//获取家庭情况总行
							var row = $("#familyCount").attr("rowspan");//获取家长的总行
							for(var i = 0;i<familyMember.length;i++){
								var str = "<tr>" + "<td style='text-align:center'>"+ familyMember[i].xm + "</td>"
										+ "<td style='text-align:center'>"+ familyMember[i].gx+ "</td>" 
										+ "<td style='text-align:center'>"+ familyMember[i].zzmm + "</td>" 
										+ "<td style='text-align:center'>"+ familyMember[i].gzdwjzw+ "</td>" 
										+ "<td style='text-align:center'>"+ familyMember[i].jtdh + "</td>" 
										+ "<td style='text-align:center'>"+ familyMember[i].yddh + "</td>" 
										+ "<td style='text-align:center'>"+ familyMember[i].jtdzxx + "</td>" 
										+ "</tr>";
									$("#family").after(str);
								}
							$("#familyInfo").attr("rowspan",num*1+familyMember.length);//合并家庭情况行
							$("#familyCount").attr("rowspan",row*1+familyMember.length);//合并家长行
							
						}
					
					});
				  
			}
			
		
			
			
			//保存修改新生信息
			function editorSave(){
				var saveUrl = "";
				var zhuanye = $("#zhuanye").val()
				if(zhuanye==""){
					zhuanye = $("#profession1").val();
				}
				POST(saveUrl, {
					stuName:$("#stuName1").val(),
					college:$("#college1").val(),
					profession:zhuanye,
					banJi : $("#banJi").val(),
					louHao : $("#louHao").val(),
					qinShi : $("#qinShi").val(),
					id : $("#stuId").val()
				}, function(data) {
					//$("#stock").val(data[0].stock);
				});
			}
			//取消新生修改
			 function cancelBTN() {
				$(listId).trigger("reloadGrid");
				hideSlidePanel(".page-editor-panel");
				editId = '';
			} 
			//重置修改信息
			function resetBTN(){
					$("#xuehao").val("");
					$("#louHao").val("");
					//$("select[name=nameModel]").data("chosen").selectedItem("");
					$("#louCeng").val("");
					$("#qinShi").val("");
					$("#banJi").val("");
					$("#dangAn").val("");
					$("#xueJi").val("");
			}
		
</script>
</head>
<body>
	<div class="page-list-panel">
		<div class="head-panel">
			<div class="search-panel">
				<div class="form-panel">
				<form id="searchForm">
					<table cellpadding="0" cellspacing="0" border="0">
						<tr>
							<td>学院：</td>
							<td>

								<select  id="college"  name="college" style='width: 146px;height:26px; text-align: center;'>

									<option value="">全选</option>
								</select>
							</td>
							<td style="padding-left: 12px;text-align:right;">专业：</td>
							<td>
								<select id="profession"  name="profession" style='width:146px;height:26px; text-align: center;'>
											<option value="">全选</option>
								</select>
							</td>
							<td style="padding-left: 25px;text-align:right;">姓名：</td>
							<td><input name="stuName" id="stuName" type="text" style="width:130px;height:20px;padding-top: 0.2em;padding-right: 1em"></td>
							<td style="padding-left:25px;text-align:right;" >学号：</td>
							<td><input name="stuNumber" id="stuNumber" type="text" style="width:130px;height:20px;padding-top: 0.2em;padding-right: 1em;margin-right:30px;"></td>
						</tr>
						<tr style="height:50px;">
							<td>报到：</td>
							<td>
								<select id="reportStatus"  name="reportStatus" type="chosen" style='width: 143px;height:26px; text-align: center;'>
											<option value="">全选</option>
											<option value="1">已报到</option>
											<option value="0">未报到</option>
								</select>
							</td>
							<td style="text-align: right">班级：</td>
							<td>
								<select id="grade"  name="grade" style='width: 146px;height:26px; text-align: center;'>
											<option value="">全选</option>
											
								</select>
							</td>
							<td style="text-align: right;">缴费：</td>
							<td>
								<select id="paymenStatus"  name="paymenStatus" style='width:147px;height:26px; text-align: center;'>
											<option value="">全选</option>
											<option value="1">已缴费</option>
											<option value="0">未缴费</option>
								</select>
							</td>
							<td style="padding-left: 25px;" colspan="2">
								<button type="button" id="advancedSearch">
									<i class="fa fa-search"></i>查询
								</button>
								<button type="button" id="resetSearch">
									<i class="fa fa-undo"></i>重置
								</button>
								<button type="button" id="searchRipClose" title="点击收起查询面板">
									<i class="fa  fa-angle-down" style="margin-right:0px;"></i>
								</button>
							</td>
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
							<button id="insertNum">
							<i class="fa"></i>导入预分学号
						</button>
						<button id="insertQinShi">
							<i class="fa"></i>导入预分寝室
						</button>
						<button id="insertGrade">
							<i class="fa"></i>导入预分班级
						</button>
						<button id="insertFile">
							<i class="fa"></i>导入学生档案
						</button>
						<button id="outHuJi">
							<i class="fa"></i>导出户籍迁移
						</button>
						</td>
						<td style="padding-left: 24px; padding-right: 5px;"><input
							id="fastQueryText" type="text"
							style="line-height: 1em; font-size: 1em; cursor: text;" /></td>
						<td>
							<button id="fastSearch" title="查询" style="margin-left:0px;">
								<i class="fa fa-search" style="margin-right:0px;"></i>
							</button>
							<button id="searchRip" title="点击展开高级查询面板">
								<i class="fa fa-angle-up" style="margin-right:0px;"></i>
							</button>
						</td>
					</tr>
				</table>
			</div>
		</div>
		<table id="list2"></table>
		<div id="pager2"></div>
	</div>
 <input id ="stuId" name="stuId" type="hidden" />
<div class="page-editor-panel full-drop-panel" style="overflow:auto;">
 <div id="tabs" class="frametab ui-tabs ui-widget ui-widget-content ui-corner-all">
     <ul>
      <li><a href="#tabs-1">新生信息修改</a></li>
      <li><a href="#tabs-2">新生信息查看</a></li>
       <li style="float:right;">
                <div style="margin-top: 4px;">
                    <button id="cancelBTN" style="height: 23px;width: 55px;margin-right: 24px;border: 1px solid #3babe3;
			   				background: #ffffff;font-weight: bold;color: #3babe3;" onclick="cancelBTN();">
                       <i class="fa fa-times"></i>取消
                    </button>
                </div>	
        </li>		
    </ul>
    								
    <div id="tabs-1">
			<table id='tblInfo' cellspacing="0" border="0" class="tableTemplet" style="height:100%;margin-top: 30px;width:80%;">
				<tbody id="body1">
				<tr><td colspan="8" style="text-align:left;height:30px;font-size: 20px;color: #3BB2E4">
				<i class="fa fa-user" style="color: #3BB2E4;"></i>新生基本信息</td></tr>
					<tr style="height:20px;">
							<td style='width: 10%; text-align: center;' class='fonts_style'>姓名:</td>
							<td style='width: 15%'>
								<input data-xtype="text" name="stuName1" id="stuName1" style="width: 132px;border:none;">
							</td>
							<td style='width: 10%; text-align: center;' class='fonts_style'>学号:</td>
							<td style='width: 15%'>
								<input data-xtype="text" name="stuNumber1" id="stuNumber1" style="width: 132px;border:none;">
							</td>
					</tr>
					<tr style="height:20px;">
							<td style="width: 10%; text-align:center;"class="fonts_style">学院:</td>
							<td style='width: 15%'>
								<input data-xtype="text" name="college1" id="college1" style="width:132px;border:none;">
							</td>
							<td style="width:10%;text-align: center;" class='fonts_style'>专业:</td>
							<td style='width: 15%'>
								<input data-xtype="text" name="profession1" id="profession1" style="width: 132px;border:none;">
							</td>
						</tr>
						</tbody>
			</table>
			<table cellspacing="0" border="0" class="tableTemplet" style="margin-top:15px;width:80%">
			<tbody>
			<tr ><td colspan="8" style="text-align:left;height:30px;font-size: 20px;color: #3BB2E4">
				<i class='fa fa-file-text' style="color: #3BB2E4;"></i>信息修改</td></tr>
					<tr style="height:50px;">
						<td style="width: 10%; text-align: center;" class='fonts_style'>专业:</td>
						<td><input type="text" name="zhuanye" id="zhuanye"></td>
						<td style="width: 10%; text-align: center;" class='fonts_style'>班级:</td>
						<td><input type="text" name="banJi" id="banJi"></td>
					</tr>
					<tr style="height:50px;">
						<td style="text-align: center" class='fonts_style'>寝室楼号：</td>
						<td >
						<select   id="louHao"  name="louHao" style="width:148px;"
										class="selectSty" data-dic="{code:'LHWH'}">
						</select>
						</td>
						<td style="text-align: center" class='fonts_style'>寝室号：</td>
						<td colspan='2' >
						<input type="text" id="qinShi"  name="qinShi"></td>
					</tr>
					<!-- <tr style="height:50px;">
							<td style='width: 10%; text-align: center;' class='fonts_style'>档案:</td>
							<td style='width: 15%'>
									<select id="dangAn"  name="dangAn" style='width:148px; text-align: center;'>
									<option value=""></option>
									<option value="1">已接受</option>
									<option value="0">未接收</option>
						</select>							</td>
							<td style='width: 10%; text-align: center;' class='fonts_style'>学籍:</td>
							<td style='width: 15%'>
							<select id="xueJi"  name="xueJi" style='width: 148px; text-align: center;'>
									<option value=""></option>
									<option value="1">已接受</option>
									<option value="0">未接收</option>
							</select>
							</td>
					</tr> -->
					<tr>
					<td colspan="4" style="line-height: 55px;text-align: center;">
						<button id="editorSave" class='btn_edit ui-state-default' 
						 style="border: 1px solid #3babe3; height:24px;"onclick="editorSave();">
							<i class="fa fa-check"></i>保存
						</button>
						<button id="resetBTN" class='btn_edit ui-state-default' 
						style="border: 1px solid #3babe3;height:24px;margin-left:10px;" onclick="resetBTN();">
							<i class="fa fa-undo"></i>重置
						</button>
					</td>
					</tr>
				</tbody>
			</table>
    </div>
    <div id="tabs-2" style="height:800px;">
    <table id="stu_look"style="width:86%;margin-top:20px;margin-left:auto;margin-right:auto;margin-bottom: 20px;"  cellspacing="0" border="1">
			<tr>
				<td colspan="9" style="text-align: center;">新生入学信息登记表</td></tr>
				<tr>
				<td colspan="2" style="text-align: center;" >学院</td>
				<td style="width:120px;"> <input id="XY" name="XY" ></td>
				<td>专业</td>
				<td colspan="2" style="width: 190px;"> <input id="ZYMC" name="ZYMC" ></td>
				<td style="text-align: center;">学号</td>
				<td colspan="2"> <input id="RYH" name="RHY"></td>
			</tr>
			<tr>
				<td rowspan="7" style="width:60px;"><h4 style="width:24px;margin:auto;">个人基本信息</h4></td>
				<td style="width:60px;">姓名</td>
				<td><input id="XM" name="XM" ></td>
				<td>性别</td>
				<td style="width:150px;"><input id="SEX" name="SEX" ></td>
				<td>出生年月</td>
				<td><input id="CSRQ" name="CSRQ"></td>
				<td rowspan="6" colspan="2"><h4 style="margin:auto;width:74px;">上传照片（一寸免冠正面彩色电子照片）</h4></td>
			</tr>
			<tr>
				<td>政治面貌</td>
				<td><input id="ZZMM" name="ZZMM"></td>
				<td>民族</td>
				<td><input id="MZ" name="MZ"></td>
				<td>生源地</td>
				<td><input id="STUDENTS" name="STUDENTS"></td>
			</tr>
			<tr>
				<td>是否普高</td>
				<td><input id="SFPG" name="SFPG"></td>
				<td>是否应届</td>
				<td><input id="SFYJ" name="SFYJ"></td>
				<td>原户籍所在地</td>
				<td><input id="JG" name="JG"></td>
			</tr>
			<tr>
				<td>电话</td>
				<td><input id="YDDH" name="YDDH"></td>
				<td>邮箱</td>
				<td><input id="DZXX" name="DZXX"></td>
				<td>QQ</td>
				<td><input id="JSTXH_QQ" name="JSTXH_QQ"></td>
			</tr>
			<tr>
				<td>微信</td>
				<td><input id="JSTXH_WX" name="JSTXH_WX"></td>
				<td>身份证号码</td>
				<td colspan="3"><input id="SFZJH" name="SFZJH"></td>
			</tr>
			<tr>
				<td>身高</td>
				<td><input id="SG" name="SG"></td>
				<td>体重</td>
				<td><input id="TZ" name="TZ"></td>
				<td>鞋码</td>
				<td><input id="XZM" name="XZM"></td>
			</tr>
			<tr>
				<td>衣服尺寸</td>
				<td><input id="YFCC" name="YFCC"></td>
				<td>三围</td>
				<td colspan="3"><input id="SW" name="SW"></td>
				<td colspan="2" style="color: red">注：___专业必须填写</td>
			</tr>
			<tr>
				<td id="familyInfo" rowspan="3" style="width:60px;"><h4 style="width:24px;margin:auto;">家庭情况</h4></td>
				<td colspan="3">家庭住址（非高中地址具体到省、市、区、路、弄、楼、几零几）</td>
				<td colspan="5"><input id="JTZZ" name="JTZZ"></td>
			</tr>
			<tr>
				<td>家庭邮编</td>
				<td><input id="JTYZBM" name="JTYZBM"></td>
				<td>家庭情况</td>
				<td colspan="5"><input id="JTQK" name="JTQK"></td>
			</tr>
			<tr id="family">
				<td id="familyCount" rowspan="1">家长信息</td>
				<td>姓名</td>
				<td>关系</td>
				<td>政治面貌</td>
				<td>工作单位职务</td>
				<td>家庭电话</td>
				<td>手机</td>
				<td>邮箱</td>
			</tr>
			
			<tr>
				<td rowspan="6"><h4 style="width:24px;margin:auto;">个人经历</h4></td>
				<td colspan="2">小学-高中担任职务</td>
				<td colspan="6"><input id="CRZW" name="CRZW"></td>
			</tr>
			<tr>
				<td colspan="2">奖惩情况</td>
				<td colspan="6"><input id="JCQK" name="JCQK"></td>
			</tr>
			<tr>
				<td colspan="2">兴趣爱好</td>
				<td colspan="6"><input id="XQAH" name="XQAH"></td>
			</tr>
			<tr>
				<td colspan="2">特长</td>
				<td colspan="6"><input id="TC" name="TC"></td>
			</tr>
			<tr>
				<td colspan="2">有无担任学习校或班级组织职务如学生会主席、班长</td>
				<td colspan="6"><input id="ZZZW" name="ZZZW"></td>
			</tr>
			<tr>
				<td colspan="2">曾组织过的活动并在其中担任的角色</td>
				<td colspan="6"><input id="ZZJS" name="ZZJS"></td>
			</tr>
			
			<tr>
				<td rowspan="3"><h4 style="width:24px;margin:auto;">其他</h4></td>
				<td colspan="2">家庭贫困情况说明</td>
				<td colspan="6"><input id="JTPKQK" name="JTPKQK"></td>
			</tr>
			<tr>
				<td colspan="2">身体状况</td>
				<td colspan="6"><input id="JKZKM" name="JKZKM"></td>
			</tr>
			<tr>
				<td colspan="2">理想抱负</td>
				<td colspan="6"><input id="LXBF" name="LXBF"></td>
			</tr>
			</table>
    </div>
  </div>
</div>
	<div class="page-view-panel full-drop-panel"></div>
</body>
</html>