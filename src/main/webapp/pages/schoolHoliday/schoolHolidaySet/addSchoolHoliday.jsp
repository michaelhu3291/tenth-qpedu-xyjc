<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="../../theme/default/ui.custom.css" rel="stylesheet" />
<link href="../../theme/default/ui.jqgrid.css" rel="stylesheet" />
<link href="../../theme/default/font.awesome.css" rel="stylesheet" />
<link href="../../theme/default/ui.chosen.css" rel="stylesheet" />
<link href="../../theme/default/ui.uploadfile.css" rel="stylesheet" />
<link href="../../theme/default/page.common.css" rel="stylesheet" />
<script type="text/javascript" src="../../js/jquery.js"></script>
<script type="text/javascript" src="../../js/ui.custom.js"></script>
<script type="text/javascript" src="../../js/ui.jqgrid.js"></script>
<script type="text/javascript" src="../../js/ui.uploadfile.js"></script>
<script type="text/javascript" src="../../js/ui.common.js"></script>
<script type="text/javascript" src="../../js/jquery.validate.js"></script>
<script type="text/javascript" src="../../js/page.common.js"></script>
<script type="text/javascript" src="../../js/util.js"></script>
<style>
.ui-custom-btn {
	border: 1px solid #009E98;
	background: #009E95
		url(../../theme/default/images/ui-bg_flat_15_009E98_40x100.png) 50%
		50% repeat-x;
	font-weight: bold;
	padding: .4em 1em;
	color: #ffffff;
}

.noLine td, .noLine th {
	border: none !important;
}

table.tableTemplet {
	font-family: "微软雅黑";
	color: #000000;
	border-collapse: collapse;
	width: 480px;
	margin-left: auto;
	margin-right: auto;
	font-size: 10px;
	margin-top: 10px;
}

table.tableTemplet tbody td {
	border: 0px solid #E9E9E9;
	font-size: 12px;
	padding: 5px;
}

table.tableTemplet input {
	width: 250px;
	border: 1;
	height: 30px;
}

td span {
	color: red;
}

span b {
	color: #A9A9A9;
}

#text_A {
	width: 100px;
}
</style>
<script type="text/javascript">	 
	function getData(){
	if($("#startTime").val()==""){
		 window.message({
             text: "<b style='color:#0099cc;'>时间都为</b>为必填项",
             title: "提醒",
             buttons: {
                 "确认": function(){
                	 window.top.$(this).dialog("close");
                 }
             }
         });
		return false;
	}
	var startTime=convert($("#startTime").val()+" "+$("#start_hm").val());
	var endTime=convert($("#endTime").val()+" "+$("#end_hm").val());
	
	if(startTime>endTime){
		 window.message({
             text: "<b style='color:#0099cc;'>结束时间</b>不能大于<b style='color:#0099cc;'>开始时间</b>",
             title: "提醒",
             buttons: {
                 "确认": function () {
                	 window.top.$(this).dialog("close");
                 }
             }
         });
		return false ;
	}
	
	/* if($("#shortName").val().length>1){
		 window.message({
           text: "<b style='color:#0099cc;'>校历简称</b>只能为一个字",
           title: "提醒",
           buttons: {
               "确认": function () {
              	 window.top.$(this).dialog("close");
               }
           }
       });
		return false;
	} */
	
	var data=$("#editorForm").getFormData();
	data["holidayName"]=$.trim($.trim($("#shortName").find("option:selected").text()));
	return data;
 }
	
	var convert=function(curDate){
	    
           var dateAry=curDate.split(" ");
       	   dateAryOne=dateAry[0].split("-");
       	   dateAryTwo=dateAry[1].split(":");
       	   var result="";
        
      	   for(var i=0;i<dateAryOne.length;i++ ){
          		result+=dateAryOne[i];
           }
           for(var i=0;i<dateAryTwo.length;i++ ){
          		result+=dateAryTwo[i];
           }
           
            return parseInt(result);
	     
	 };
	$(function() {
		_initButtons({});//from page.common.js
		_initFormControls();//from page.common.js
		_initValidateForXTypeForm(editorFormId, {
                    errorElement: "span",
                    errorPlacement: function(error, element) {
                   	   error.appendTo(element.parent("td"));
                    }
                });
		loadHoliType();
		$("input[name='age']").datepicker();
		$("#imgFile").find("span").remove();
		if(getUrlParam("startDate")!=null && getUrlParam("startDate")!="undefined" && getUrlParam("startDate") !=""){
			$("#startTime").val(getUrlParam("startDate"));
		}
		if(getUrlParam("id")!=null&&getUrlParam("id")!="undefined"){
			$("#mainId").val(getUrlParam("id"));
			 POST("../../schoolHoliday/schoolHolidaySet/addSchoolHoliday.do?command=loadHoliday",
					 {ID:getUrlParam("id")},function(data){
             	  			//console.log(data);
             	  			var pic = $("#holiType").val();
             	  			$("#holiType").val(data[0].HOLI_TYPE);
             	  			if(pic != data[0].HOLI_TYPE){
             	  				loadHoliType(data[0].SHORT_ID);
             	  			}else{
             	  				$("#shortName").val(data[0].SHORT_ID);
             	  			}
             	  			$("#startTime").val(dateFormat(data[0].START_TIME,"yyyy-MM-dd"));
             	  			var end = $.trim(data[0].END_TIME);
             	  			if(end.length>0){
             	  			$("#endTime").val(dateFormat(data[0].END_TIME,"yyyy-MM-dd"));
             	  			}
             });
		}
		/* 节假类型联动下拉框节日名称
		*/
		$("#holiType").change(function () {
			var tag="";
			loadHoliType(tag);
	});
		resizeFun();
		
	});
	function loadHoliType(tag){
		var url="../../schoolHoliday/schoolHolidaySet/addSchoolHoliday.do?command=getHoliType";
		var id = $("#holiType").val();
		$.post(url,{typeId:id},function(data){
			var zyList=$.parseJSON(data);
			var str="";
			for(var i = 0;i<zyList.length;i++){
				var str= str+ "<option value='"+zyList[i].ID+"'>"+ zyList[i].NAME+"</option>";
			}
			$("#shortName option").remove();
			$("#shortName").append(str);
			if(tag){
				$("#shortName").val(tag);
			}
		});
	}
</script>
</head>
<body style="background-color: #efefef;">
		<div>
			<div id="checkDormitory">
				<form id="editorForm">
				    <input name="mainId"  id="mainId" type="hidden" />
				    <table cellpadding="6" cellspacing="10" border="0" class="tableTemplet">
						<tbody>
							<tr>
								<td style="width: 130px;height:30px;text-align: right;">校历类型:</td>	
								<td>
								 <select data-xtype="text" style="width:254px;" name="holiType" id="holiType" class="form-control" >
                                       <c:forEach items="${holidayType}" var="holidayType">
                                       		<option value="${holidayType.id}">${holidayType.DictionaryName}</option>
                                       </c:forEach>
                                 </select>
						    	</td>							
							</tr>
							<tr>
								<td style="width: 130px;height: 30px;text-align: right;">节点名称:</td>	
								<td>
								<select data-xtype="text" style="width:254px;" name="shortName" id="shortName" class="form-control" >
                                 </select>
								<span>*必填</span>
								</td>							
							</tr>
							<tr>
								<td style="width: 130px;height: 30px;text-align: right;">开始时间:</td>	
								<td>
								<input data-xtype="datetime" name="startTime" id="startTime" />
								<span>*必填</span>
								</td>							
							</tr>
							<tr>
								<td style="width: 130px;height: 30px;text-align: right;">结束时间:</td>	
								<td>
								<input data-xtype="datetime" name="endTime" id="endTime" />
								</td>							
							</tr>
							<tr><td colspan="2" style="font-size: 14px;color:red;">*如果结束时间不填，则默认该校历的期长仅为开始时间当天</td></tr>
							<!-- <tr>
								<td style="width: 130px;height: 30px;">主要内容:</td>	
								<td>
								<input data-xtype="text" name="profession1" id="profession1" style="width: 100%;height: 30px;border:0;">
								</td>							
							</tr>		 -->				
						</tbody>
						
					</table>
				</form>
			</div>
			
	</div>
	
</body>
</html>