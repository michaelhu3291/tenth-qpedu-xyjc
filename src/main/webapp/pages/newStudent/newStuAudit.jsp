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
.search-panel {
	display: block;
	position: relative;
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
	font-size: larger
}

.td_background {
	background-color: #F6F6F6;
}
.xueshengFile{
	position: absolute;
	margin-top:20px;
	width: 30%;
	height: 158px;
	background: url("<%=request.getContextPath()%>/theme/image/haveRecieve.png") no-repeat 4px 5px;
	background-size: 259px,132px;
	margin-left: 8%;
}

.hujiqianyi{
	position: absolute;
	margin-top:20px;
	width: 30%;
	height:158px;
	background: url("<%=request.getContextPath()%>/theme/image/haveRecieve.png")no-repeat 4px 5px ;
	background-size: 259px,132px;
	margin-left: 41%;
}
.oilrelation{
	position: absolute;
	margin-top: 185px;
	width: 30%;
	height: 158px;
	background: url("<%=request.getContextPath()%>/theme/image/haveRecieve.png")no-repeat 4px 5px ;
	background-size: 258px,100px;
	margin-left: 8%;
}
.partyRelation{
	position:absolute;
	margin-top:185px;
	width:30%;
	height: 158px;
	background: url("<%=request.getContextPath()%>/theme/image/haveRecieve.png")no-repeat 4px 5px;
	background-size: 258px,100px;
	margin-left: 41%;
}
#mainPage{
	margin-top: 15px;
    height: 80%;
    position: absolute;
    z-index: 2;
    background-color: #EEEEEE;
    width: 87%;
    margin-left: 7%;
}
#tblInfo input{
		background-color: rgba(128, 128, 128, 0.05);
}
</style>
<script type="text/javascript">
	var listId = "#list2", editorFormId = "#editorForm", pagerId = '#pager2',

	listUrl = "../newStudent/studentManagement.do?command=search";
	$(function() {
		searchStuData();
		_initFormControls();
		_initButtons({
			resetSearch:function(ev){
				$("#stuName").val("");
			},
		});
     		resizeFun();
     		$("input:radio").click(function(){
     			if($(this).val()==1){
     				$(this).parent().parent().parent().parent().parent().css(
     						{'background':'url("../theme/image/haveRecieve.png")no-repeat 4px 5px','background-size':'259px,120px'});
    			}
     			else{
     				$(this).parent().parent().parent().parent().parent().css(
     						{'background':'url("../theme/image/noRecieve.png")no-repeat 4px 5px','background-size':'259px,120px'});
     			}
     		})
    	});
	/* 按钮是根据学号查询的 */
	function searchByRyh(){
		var ryh = $("#ryh").val();
		$.ajax({
            url: "../newStudent/newStuAudit.do?command=searchByRhy",
            type: "POST",
            data: {ryh : ryh},
            dataType: "JSON",
            success: function(data, xhr) {
            	console.log(data);
            	var school = data.school;
            	var stuInfo = data.stuInfo;
            	if(data==null||data==""){
            		$("#tag_1").css("display","none");
            		$("#tag_2").css("display","block");
            	}else{
            		$("#tag_1").css("display","block");
            		$("#tag_2").css("display","none");
            		$("#personId").val(stuInfo.personId);
            		$("#xm").val(stuInfo.xm);
            		$("#xy").val(school.xy_name);
            		$("#zymc").val(school.zymc_name);
            		$("#tblInfo input").attr("disabled",true);
            		$("#xsdasm").val(stuInfo.xsdasm);
            		$("#hjqysm").val(stuInfo.hjqysm);
            		$("#lygxsm").val(stuInfo.lygxsm);
            		$("#dtgxsm").val(stuInfo.dtgxsm);
            		
            		$(".dangan[value='"+stuInfo.xsda+"']").attr({"checked":true});
            		if(stuInfo.xsda!=1){
            			$(".xueshengFile").css({'background':'url("<%=request.getContextPath()%>/theme/image/noRecieve.png")no-repeat 4px 5px',
            									'background-size':'259px,132px'});
            		}
            		
            		$(".huji[value='"+stuInfo.hjqy+"']").attr({"checked":true});
            		if(stuInfo.hjqy!=1){
            			$(".hujiqianyi").css({'background':'url("<%=request.getContextPath()%>/theme/image/noRecieve.png")no-repeat 4px 5px',
												'background-size':'259px,132px'});
            		}
            		
            		$(".liangyou[value='"+stuInfo.lygx+"']").attr({"checked":true});
            		if(stuInfo.lygx!=1){
            			$(".oilrelation").css({'background':'url("<%=request.getContextPath()%>/theme/image/noRecieve.png")no-repeat 4px 5px',
												'background-size':'258px,100px'});
            		}
            		
            		$(".dangtuan[value='"+stuInfo.dtgx+"']").attr({"checked":true});
            		if(stuInfo.dtgx!=1){
            			$(".partyRelation").css({'background':'url("<%=request.getContextPath()%>/theme/image/noRecieve.png")no-repeat 4px 5px',
												'background-size':'258px,100px'});
            		}
            	}
            	
            }
        });
	}
	/* 跳转是根据personId查询的 */
	  function searchStuData(){
		var personId = $('#personId', parent.document).val(); 
		/* var personId = "EAC45928-984A-486D-B49F-E885A4C3E182"; */
		if(personId==null||personId==""){
			return;
		}else{
		$.ajax({
            url: "../newStudent/newStuAudit.do?command=search",
            type: "POST",
            data: {personId : personId},
            dataType: "JSON",
            success: function(data, xhr) {
            	console.log(data);
            	var school = data.school;
            	var stuInfo = data.stuInfo;
            	if(data==null||data==""){
            		$("#tag_1").css("display","none");
            		$("#tag_2").css("display","block");
            	}else{
            		$("#tag_1").css("display","block");
            		$("#tag_2").css("display","none");
            		$("#personId").val(stuInfo.personId);
            		$("#xm").val(stuInfo.xm);
            		$("#xy").val(school.xy_name);
            		$("#zymc").val(school.zymc_name);
            		$("#tblInfo input").attr("disabled",true);
            		$("#xsdasm").val(stuInfo.xsdasm);
            		$("#hjqysm").val(stuInfo.hjqysm);
            		$("#lygxsm").val(stuInfo.lygxsm);
            		$("#dtgxsm").val(stuInfo.dtgxsm);
            		
            		$(".dangan[value='"+stuInfo.xsda+"']").attr({"checked":true});
            		if(stuInfo.xsda!=1){
            			$(".xueshengFile").css({'background':'url("<%=request.getContextPath()%>/theme/image/noRecieve.png")no-repeat 4px 5px',
            									'background-size':'259px,132px'});
            		}
            		
            		$(".huji[value='"+stuInfo.hjqy+"']").attr({"checked":true});
            		if(stuInfo.hjqy!=1){
            			$(".hujiqianyi").css({'background':'url("<%=request.getContextPath()%>/theme/image/noRecieve.png")no-repeat 4px 5px',
												'background-size':'259px,132px'});
            		}
            		
            		$(".liangyou[value='"+stuInfo.lygx+"']").attr({"checked":true});
            		if(stuInfo.lygx!=1){
            			$(".oilrelation").css({'background':'url("<%=request.getContextPath()%>/theme/image/noRecieve.png")no-repeat 4px 5px',
												'background-size':'258px,100px'});
            		}
            		
            		$(".dangtuan[value='"+stuInfo.dtgx+"']").attr({"checked":true});
            		if(stuInfo.dtgx!=1){
            			$(".partyRelation").css({'background':'url("<%=request.getContextPath()%>/theme/image/noRecieve.png")no-repeat 4px 5px',
												'background-size':'258px,100px'});
            		}
            	}
            	
            }
        });
		}
	} 
	//保存材料信息的方法
	function saveAudit(){
		url = "../newStudent/newStuAudit.do?command=save";
		POST(url,{
			personId:$("#personId").val(),
			xsda:$("input[name='xsda']:checked").val(),
			hjqy:$("input[name='hjqy']:checked").val(),
			lygx:$("input[name='lygx']:checked").val(),
			dtgx:$("input[name='dtgx']:checked").val(),
			xsdasm:$("#xsdasm").val(),
			hjqysm:$("#hjqysm").val(),
			lygxsm:$("#lygxsm").val(),
    		dtgxsm:$("#dtgxsm").val()
		},function(data){
			console.log(data);
		})
	}
</script>
</head>
</body>
<div class="page-list-panel" style="overflow: auto;background-color: rgba(128, 128, 128, 0.09);">

	<div id="mainPage">
	<div style="margin-top: 10px;">
		<table style="margin-top: 10px;width: 60%;" cellpadding="0" cellspacing="0" border="0">
				<tr style="height:22px;">
					<td style="padding-left: 19px;text-align:right; width:43px;" >学号：</td>
					<td><input name="ryh" id="ryh" type="text" style="padding-top: 0.2em;height:20px;"></td>
					<td>
								<button id='queryBTN' style="height: 30px;" class='page-button' onclick='searchByRyh()'>
									<i class="fa fa-search"></i>查询</button>
								<button id='queryBTN' style="height: 30px;" class='page-button' onclick='saveAudit()'>
								<i class='fa fa-file-text' style='margin-right: 5px;'></i>保存</button>
					</td>
				</tr>
		</table>
	</div>
   <div id="tag_1" style="position: absolute;width: 100%;height: 80%;margin: 19px 6px 9px 0px;  border-top: 1px solid #000;">
		<div style="width: 68%;margin:auto;margin-top:20px;">
			<div id="xueshengFile" class="xueshengFile">
			
			<input type="text" id="personId" hidden="true"/>
			
			<table style="color:white;"><tbody>
		    <tr><td colspan="2" style="height:41px;padding-left:20px;font-size: 20px;font-weight: bolder">学生档案</td></tr>
		    <tr>
			    <td rowspan="4" style="width:47%;">
			    <img src="../theme/image/stuFile.png" style="width:91%;margin-top:-18px;" />
			    </td>
			    <td style="width: 60px;"><input name="xsda" class="dangan" type="radio"  value="1">已接受</td>
		    </tr>
		    <tr>
		    <td style="width: 60px;"><input name="xsda" class="dangan" type="radio"  value="0">未接受</td>
		    </tr>
		    <tr>
		    	<td style="width: 60px;">情况说明</td>
		    </tr>
		    <tr>
		    <td style="width: 60px;"><input id ="xsdasm" style="width:96%;"type="text"></td>
		    </tr>
		    </tbody></table>
		</div>
		<div class="hujiqianyi">
			<table style="color:white;"><tbody>
		    <tr><td colspan="2" style="height:41px;padding-left:20px;font-size: 20px;font-weight: bolder">户籍迁移</td></tr>
		    <tr>
			    <td rowspan="4" style="width:47%;">
			    <img src="../theme/image/huJiMove.png" style="width:91%;margin-top: -18px;" />
			    </td>
			    <td style="width: 60px;"><input name="hjqy" class="huji" type="radio" value="1">已接受</td>
		    </tr>
		    <tr>
		    <td style="width: 60px;"><input name="hjqy" class="huji" type="radio"  value="0">未接受</td>
		    </tr>
		    <tr>
		    <td style="width: 60px;">情况说明</td>
		    </tr>
		    <tr>
		    <td style="width: 60px;"> <input id ="hjqysm" style="width:96%;"type="text"></td>
		    </tr>
		    </tbody></table>
		</div>
		<div class="oilrelation">
			<table style="color:white;"><tbody>
		    <tr><td colspan="2" style="height:41px;padding-left:20px;font-size: 20px;font-weight: bolder">粮油关系</td></tr>
		    <tr>
			    <td rowspan="4" style="width:47%;">
			    <img src="../theme/image/oilrelation.png" style="width:91%;margin-top: -18px;"  />
			    </td>
			    <td style="width: 60px;"><input name="lygx" class="liangyou" type="radio" value="1">已接受</td>
		    </tr>
		    <tr>
		    	<td style="width: 60px;"><input name="lygx" class="liangyou" type="radio" value="0">未接受</td>
		    </tr>
		    <tr>
		    <td style="width: 60px;">情况说明</td>
		    </tr>
		    <tr>
		    <td style="width: 60px;"> <input id ="lygxsm" style="width:96%;"type="text"></td>
		    </tr>
		    </tbody></table>
	</div>
	<div class="partyRelation" >
	<table style="color:white;">
		<tbody>
	    <tr><td colspan="2" style="height:41px;padding-left:20px;font-size: 20px;font-weight: bolder">党团关系</td></tr>
	    <tr>
		    <td rowspan="4" style="width:47%;">
		    <img src="../theme/image/partyRelation.png" style="width:91%;margin-top: -18px;"  />
		    </td>
		    <td style="width: 60px;"><input name="dtgx" class="dangtuan" type="radio" value="1">已接受</td>
	    </tr>
	    <tr>
	    	<td style="width: 60px;"><input name="dtgx" class="dangtuan" type="radio" value="0">未接受</td>
	    </tr>
	    <tr>
	    	<td style="width: 60px;">情况说明</td>
	    </tr>
	    <tr>
	    	<td style="width: 60px;"> <input id="dtgxsm" style="width:96%;"type="text"></td>
	    </tr>
	    </tbody>
    </table>
	</div>
	</div>
	<!-- 学生名片 -->
	<div style="width: 100%;background: #EEEEEE; margin-top: -45px;">
		<table id='tblInfo' cellspacing="0" border="0" class="tableTemplet" style="height:100%;margin-top: 50px;width:100%;">
			<tbody id="body1" style="background: #EEEEEE;">
				<tr style="height:20px;">
						<td style='width: 10%; text-align: center;' class='fonts_style'>姓名:</td>
						<td style='width: 15%'>
							<input data-xtype="text" name="xm" id="xm" style="width: 132px;border:none;color:blue;">
						</td>
						<td style="width: 10%; text-align:center;"class="fonts_style">学院:</td>
						<td style='width: 15%'>
							<input data-xtype="text" name="xy" id="xy" style="width:132px;border:none;color:blue;">
						</td>
						<td style="width:10%;text-align: center;" class='fonts_style'>专业:</td>
						<td style='width: 15%'>
							<input data-xtype="text" name="zymc" id="zymc" style="width: 132px;border:none;color:blue;">
						</td>
					</tr>
					</tbody>
			</table>
	</div>
	<!-- <div style="width: 126px;box-shadow: #666 0px 0px 10px;background: #EEFF99;height: auto; margin-top: 45px;margin-left: 6%;">
	<table style="width:100%">
		<tbody>
	    <tr>
		    <td>
		   <img class="ui-img" src="../theme/default/images/u_w.png" style="background-color: #E6B453;width:100%;">
		</td>
	    </tr>
	    <tr>
	    	<td style="width: 60px;">姓名</td>
	    </tr>
	     <tr>
	    	<td style="width: 60px;">学院</td>
	    </tr>
	     <tr>
	    	<td style="width: 60px;">专业</td>
	    </tr>
	    </tbody>
    </table>
	</div> -->
	</div>
	<div id="tag_2" style="position: absolute;display:none;width: 34%;height: 80%;margin-top:82px;margin-left: 32%;font-size: 27px;line-height: 40px;">
	对不起，该学生入学信息还未通过审核，请先完善入学信息通过报到审核，再来递交材料。
	</div>
</div>
</div>
</body>
</html>