<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title>调动详情</title>
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
<script type="text/javascript" src="../js/util.js"></script>
<script type="text/javascript" src="../js/lab2.js"></script>

<style type="text/css">
 
#submitOper {
    border: 1px solid #3B5615;
    background: #ffffff;
    font-weight: bold;
    color: #3B5615;
    padding: 0.4em 1em;
    cursor: pointer; 
}
</style>

<script type="text/javascript">

    $(function() {
       loadDictionary();
       //加载学校
		$("#schoolType").change(function(){
			$("#school option[value != '']").remove();
			var schoolType = $("#schoolType").val();
			if(schoolType != ""){
				$.ajax({
		   	       	url:"../transfer/teacherTransfer.do?command=selectAllSchools",
		   	        type: "POST",
		   	        data: {schoolType:schoolType},
		   	        dataType: "JSON",
		   	        success: function(data, xhr) {
		   	        	$.each(data,function(index,item){
		   	        		$("#school").append("<option value='"+item.School_Code+"' >"+item.School_Short_Name+"</option>");
		   	        	});
		   	       }
		   	    });  
			}
			
			
		});
    
       //教育局提交调动学生
		$("#submitOper").click(function(){
		     var url;
		     var datas;
			 var Id = getUrlParam("id"); //调动任务id
			 var roleCode = getUrlParam("code"); //角色code (0-学生，1-老师)
			 var schoolCode = getUrlParam("school"); //原属学校code
			 var name = getUrlParam("name");
			 var proposer = getUrlParam("proposer");
			 var applyTime = getUrlParam("applyTime");
			 var school = $("#school").val();//选择的学校
	    	 if(school == ""){
				 window.Msg.alert("请选择调动到的学校");
	    		 return;
			 }
			 if(roleCode == 0){//学生
			    url = "../transfer/studentTransfer.do?command=submitTransfer";
			    datas = {studentPk:Id,roleCode:roleCode,school:school,name:name,proposer:proposer,applyTime:applyTime}
			 }
			 if(roleCode == 1){//老师
			    url = "../transfer/teacherTransfer.do?command=submitTransfer";
			    datas = {teacherPk:Id,roleCode:roleCode,school:school,name:name,proposer:proposer,applyTime:applyTime}
			 }
	    	
	    	 window.message({
	   			title:'提醒',
	   			text:'确定提交此调动吗?',
	   			buttons:{
	   				'确定':function(){
	   					window.top.$(this).dialog("close");
	   					 $.ajax({
	   				   	       	url: url,
	   				   	        type: "POST",
	   				   	        data: datas,
	   				   	        dataType: "JSON",
	   				   	        success: function(data, xhr) {
	   				   	        	if(data.mess == 'success'){
	   				   	        	     
	   				   	        		window.Msg.alert("提交成功!");
	   				   	        		parent.location.reload();
	   				   	        	}
	   				   	        	
	   				   	      		if(data.mess == 'false'){
				   	        	     
 				   	        		window.Msg.alert("不能调动到本校!");
 				   	        		 
 				   	        		}
	   				   	        	
	   				   	       		
	   				   	       }
	   				   	    });  
	   				},
	   				'取消':function(){
	   					window.top.$(this).dialog("close");
	   					parent.location.reload();
	   				}
	   			}		
	   		}); 
	   		
		});
       
    
    });



    var  mouseOut=	 function(){
		 $("#submitOper").css({"border":"1px solid #3B5617","color":"#3B5615","background":"#ffffff"});
	 };
	var  mouseOver=	 function(){
		 $("#submitOper").css({"border":"1px solid #3B5617","background":"#3B5615","color":"#ffffff"});
	 };




</script>

</head>
  
<body>
    <div class="page-list-panel">
		<div class="head-panel">
			<div class="toolbar">
				<table style="height: 100%;" cellpadding="0" cellspacing="0"
					border="0">
					<tr>
						
						<td class="buttons">
							&nbsp;&nbsp;学校类型
								<select id="schoolType" name="schoolType" class="form-control" data-dic="{code:'xxlx'}" style="width:150px;">
									<option value="">选学校类型</option>
								</select>
							&nbsp;&nbsp;学校
							<select id="school" class="form-control" style="width:180px;">
								<option value="">选择学校</option>
							</select>
							<button id="submitOper" onmouseover="mouseOver()" onmouseout="mouseOut()">
								<i class="fa fa-check"></i>提交
							</button>
							<!-- <button id="deleteOper">
								<i class="fa fa-trash-o"></i>删除
							</button>-->
						</td>
					</tr>
				</table>
			</div>
		</div>
		<table id="list2"></table>
		<div id="pager2"></div>
	</div>
</body>
</html>
