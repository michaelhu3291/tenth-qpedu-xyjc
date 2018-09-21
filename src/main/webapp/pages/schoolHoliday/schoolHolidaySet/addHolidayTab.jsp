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
.ui-custom-btn{border:1px solid #009E98;background:#009E95 url(../../theme/default/images/ui-bg_flat_15_009E98_40x100.png) 50% 50% repeat-x;font-weight:bold;padding:.4em 1em;color:#ffffff;}
.noLine td,.noLine th{
	border:none !important;
}
table.tableTemplet{
		font-family: "微软雅黑";
		color: #000000;
		border-collapse: collapse;
		width:480px;
		margin-left: auto;
		margin-right: auto;
		font-size:10px;
		margin-top: 10px;
	}
table.tableTemplet tbody td {
		border: 0px solid #E9E9E9;
		font-size: 12px;
		padding: 5px;
	}
	
table.tableTemplet input{
		width:250px;
		border:1;
		height: 30px;
	}


td span{
	color:red;
}
span b{
	color:#A9A9A9;
}
#text_A{
	width:100px;
}
</style>
<script type="text/javascript">	 
	function getData(){
	
	if($("#shortName").val().length>1){
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
	}
	
	var data=$("#editorForm").getFormData();
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
		$("input[name='age']").datepicker();
		$("#imgFile").find("span").remove();
		if(getUrlParam("id")!=null&&getUrlParam("id")!="undefined"){
			$("#holiId").val(getUrlParam("id"));
			 POST("../../schoolHoliday/schoolHolidaySet/addHolidayTab.do?command=loadHolidayTab",
					 {ID:getUrlParam("id")},function(data){
             	  			//console.log(data);
             	  			$("#holiType").val(data[0].HOLI_TYPE);
             	  			$("#shortName").val(data[0].SHORT_NAME); 
             	  			$("#holidayName").val(data[0].NAME); 
             	  			$("#remark").val(data[0].REMARK);
             	  			
             });
		}
		resizeFun();
	});
</script>
</head>
<body style="background-color: #efefef;">
		<div>
			<div id="checkDormitory">
				<form id="editorForm">
				    <input name="holiId"  id="holiId" type="hidden" />
				   <table class="editorTable" cellpadding="0" cellspacing="0" style="width: 80%;">
                            <tr>
                                <td class="labelTd"><label for="xymc">节假日名称：</label>
                                </td>
                                <td class="inputTd" style="position:relative;z-index:1;">
                                <input data-xtype="text" data-validate="{required:true}" name="holidayName" id="holidayName" class="form-control" /></td>
                                <td class="messageTd"></td>
                            </tr>
                            <tr>
                                <td class="labelTd"><label for="xybm">简称：</label>
                                </td>
                                <td class="inputTd" style="position:relative;z-index:1;"><input
                                        data-xtype="text" data-validate="{required:true}" 
                                        name="shortName" id="shortName" class="form-control" /></td>
                                <td class="messageTd"></td>
                            </tr>
                            <tr>
                                <td class="labelTd"><label for="xyfer">假日类型：</label>
                                </td>
                                <td>
								 <select data-xtype="text" style="width:300px;" name="holiType" id="holiType" class="form-control" >
                                       <c:forEach items="${holidayType}" var="holidayType">
                                       		<option value="${holidayType.id}">${holidayType.DictionaryName}</option>
                                       </c:forEach>
                                 </select>
						    	</td>
                            </tr>
                            <tr>
                                <td class="labelTd" style="vertical-align: top;"><label
                                        for="remark">描&nbsp;&nbsp;述：</label></td>
                                <td class="inputTd" style="position:relative;z-index:1;"><textarea  
                                        name="remark" id="remark" style="width:300px;" rows="5"></textarea></td>
                            </tr>
                        </table>
				</form>
			</div>
			
	</div>
	
</body>
</html>