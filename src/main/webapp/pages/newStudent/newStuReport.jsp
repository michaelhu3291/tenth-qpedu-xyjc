<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="<%=request.getContextPath()%>/theme/default/ui.custom.css" rel="stylesheet" />
<link href="<%=request.getContextPath()%>/theme/default/ui.jqgrid.css" rel="stylesheet" />
<link href="<%=request.getContextPath()%>/theme/default/font.awesome.css" rel="stylesheet" />
<link href="<%=request.getContextPath()%>/theme/default/ui.chosen.css" rel="stylesheet" />
<link href="<%=request.getContextPath()%>/theme/default/ui.uploadfile.css" rel="stylesheet" />
<link href="<%=request.getContextPath()%>/theme/default/page.common.css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/ui.custom.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/ui.jqgrid.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/ui.autosearch.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/ui.chosen.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/ui.uploadfile.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/ui.common.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.validate.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/page.common.js"></script>
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
#stu_look input{
		width:100%;
		height:100%;
		border:1px;
		text-align: center;
	}
	#stu_look td{
		height:30px;
	}
	
	#tabs-2{height: 847px;}
  
</style>
<script type="text/javascript">
	var listId = "#list2", editId = "", pagerId = '#pager2', //分页
	listUrl = "../propertyManage/intoStock.do?command=search";
	
	$("#tabs-2").css("height","auto");
	$(function() {
		$("#tblInfo").find("button").button();
		_initFormControls();
		_initButtons();
		
		(function searchStuData(){
			var personId = $('#personId', parent.document).val();
			$.ajax({
	            url: "<%=request.getContextPath()%>/newStudent/newStuReport.do?command=loadByStuPersonId",
	            type: "POST",
	            data: {personId : personId},
	            dataType: "JSON",
	            success: function(data, xhr) {
	            	console.log(data);

	            	$(".familyInf").remove();
	            	$("#familyCount").attr("rowspan",1);
	            	$("#familyInfo").attr("rowspan",3);
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
							var str = "<tr class='familyInf'>" + "<td style='text-align:center'>"+ familyMember[i].xm + "</td>"
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
	            }
			});
			$("#stu_look input").attr("readonly","readonly");
		})();
		
		
		
		$("#passBTN").click(function() {
			var cid = $('#clientId', parent.document).val(); 
			var personId = $('#personId', parent.document).val();
			 var pushUrl = "<%=request.getContextPath()%>/newStudent/studentManagement.do?command=pushApp";
				$.post(pushUrl, {
					status : "true",
					cid : cid,
					personId : personId
				}, function(data) {
					window.message({
	                    text: "审核成功!",
	                    title: "提示"
	                });
				});
		});
		
		<%-- $("#failedBTN").click(function() {
			var cid = $('#clientId', parent.document).val(); 
			var pushUrl = "<%=request.getContextPath()%>/newStudent/studentManagement.do?command=pushApp";
			$.post(pushUrl, {
				status : "false",
				cid : cid
			}, function(data) {
				window.message({
                    text: "审核未通过!",
                    title: "提示"
                });
			});
		}); --%>
	});
	function loadByRyh(){
		var ryh = $("#stuNum").val();
		$.ajax({
            url: "<%=request.getContextPath()%>/newStudent/newStuReport.do?command=loadStuInfoByRyh",
            type: "POST",
            data: {ryh : ryh},
            dataType: "JSON",
            success: function(data, xhr){
            	$(".familyInf").remove();
            	$("#familyCount").attr("rowspan",1);
            	$("#familyInfo").attr("rowspan",3);
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
						var str = "<tr class='familyInf'>" + "<td style='text-align:center'>"+ familyMember[i].xm + "</td>"
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
        	}
        });
	}
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
						<td class="buttons">
							 <button id="passBTN">
                                 <i class="fa fa-check"></i>审核通过
                             </button>
                             <button id="failedBTN">
                                 <i class="fa fa-times"></i>审核不通过
                             </button>
                             <button id="deleteBTN">
                                 <i class="fa fa-print"></i>打印迁移单
                             </button>
						</td>
						<td style="padding-left: 24px; padding-right: 5px;"><input
							id="stuNum" type="text"
							style="line-height: 1em; font-size: 1em; cursor: text;" /></td>
						<td>
							<button  title="查询" style="margin-left:0px;" onclick="loadByRyh()">
								<i class="fa fa-search" style="margin-right:0px;"></i>
							</button>
						</td>
					</tr>
				</table>
			</div>
		</div>
		<table id="list2"></table>
		<div id="pager2"></div>
		<div id="tlbInfo" style="height:90%; overflow: auto;">
    <table id="stu_look"style="width:90%;margin-top:8px;margin-left:auto;margin-right:auto;margin-bottom: 15px;"  cellspacing="0" border="1">
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
	
<div class="page-view-panel full-drop-panel"></div>
</body>
</html>