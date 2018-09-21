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
<script type="text/javascript" src="../js/util.js"></script>
<script type="text/javascript" src="../js/lab2.js"></script>

<!--[if IE 7]>
         <link href="../theme/default/font.awesome.ie7.css" rel="stylesheet" />
         <link href="../theme/default/page.common.ie7.css" rel="stylesheet" />
    <![endif]-->
<style type="text/css">

.s1 {
	width: 120px;
    background-color: #e7fcd9;
    border: 0px;
    color: #3b5617;
    cursor: pointer;
}
.s1:HOVER {
    background-color: #3b5617;
    color: #ffffff;
}

#d1 {
	width: 810px;
	height: 400px;
	background-color: #94c476;
	margin: 0 auto;
	margin-top: 60px;
}

#d2 {
	height: 30px;
	font-size: 24px;
	background-color: #3B5617;
	color: white;
	text-align: center;
}

#d3 {
	padding-left: 120px;
}

#tabs ul li a{
	cursor:pointer;
}

#frame1 ul li{
	list-style:none;
	margin-top:20px;
}

#addTeacherInfo{
	margin-top:20px;
}

#addTeacherInfo thead td{
	text-align:center;
	font-weight:bold;
}

#addTeacherInfo tbody td{
	background:#E7FCD9;
	width:150px;
	text-align:center;
}

#addTeacherInfo tbody a:hover{
	background:#3B5617;
	color:#ffffff;
}
</style>
<script type="text/javascript">
	var listId = "#list2", 
	editorFormId = "#editorForm",
	pagerId = '#pager2', 
	listUrl = "../transfer/teacherTransfer.do?command=searchPaging";
	
	var cancelBTN = function() {
		hideSlidePanel(".page-addTeacher-panel");
	}
	
	
	$(function() {
		$(listId).trigger("reloadGrid");
		_initButtons({
			
		});//from page.common.js
		_initFormControls();//from page.common.js
		$("#tblInfo").find("button").button();
		$("#tabs").tabs({
			heightStyle : "fill"
		});
		$("#tabs-2,#tabs-1").css("height", "auto");
		_initValidateForXTypeForm(editorFormId);
		var _colModel = [ {
			name : 'Id',
			key : true,
			hidden : true,
			search : false
			},{
				name : 'Teacher_Name',
				align:"center",
				sortable : false,
				autoWidth:true,
			    search : false
		    },{
		    	  name : 'School_Short_Name',
		    	    align:"left",
					sortable : false,
					autoWidth:true,
				    search : false
	      },{
			name : 'Create_Person',
			autoWidth:true,
			sortable : false,
			align : "center",
			search : false
		}, {
			name : 'School_New_Adr',
			autoWidth:true,
			sortable : false,
			align : "left",
			search : false
		}, {
			name : 'Create_Time',
			sortable : false,
			autoWidth:true,
			align : "center",
			search : false
		},
		], _colNames = [ '','姓名', '学校','申请人','去向','申请时间'];
		$(listId).jqGrid($.extend(defaultGridOpts, {
			url : listUrl,
			colNames : _colNames,
			colModel : _colModel,
			pager : pagerId
		}));
		resizeFun();
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
		 
		//教育局提交调动老师
		$("#submitOper").click(function(){
			 var selArr = $(listId).jqGrid('getGridParam','selarrrow');
			 var rowData = $(listId).jqGrid('getRowData', selArr);
			 var teacherName = rowData.Teacher_Name;
			 var proposer = rowData.Create_Person;
			 var applyTime = rowData.Create_Time;
			 var school = $("#school").val();
	    	 if(selArr.length == 0){
	    		 window.Msg.alert("请选择要提交的记录");
	    		 return;
	    	 }
	    	 if(selArr.length > 1){
	    		 window.Msg.alert("每次只能提交单条记录");
	    		 return;
	    	 }
	    	 if(school == ""){
				 window.Msg.alert("请选择调动到的学校");
	    		 return;
			 }
	    	 var url = "../transfer/teacherTransfer.do?command=submitTransfer";
	    	 window.message({
	   			title:'提醒',
	   			text:'确定提交此调动吗?',
	   			buttons:{
	   				'确定':function(){
	   					window.top.$(this).dialog("close");
	   					 $.ajax({
	   				   	       	url:url,
	   				   	        type:"POST",
	   				   	        data:{teacherPk:selArr[0],teacherName:teacherName,proposer:proposer,school:school,applyTime:applyTime},
	   				   	        dataType:"JSON",
	   				   	        success: function(data, xhr) {
	   				   	        	if(data.mess == 'success'){
	   				   	        		window.Msg.alert("提交成功!");
	   				   	        	}
	   				   	           if(data.mess == 'false'){
				   	        		window.Msg.alert("不能提交该老师去本校!");
				   	        	   }
	   				   	        	$(listId).trigger("reloadGrid");
	   				   	       		//hideSlidePanel(".page-addTeacher-panel");*/
	   				   	       		
	   				   	       }
	   				   	    });  
	   				},
	   				'取消':function(){
	   					window.top.$(this).dialog("close");
	   				}
	   			}		
	   		});
	    	 
		});
	});  
	
	
	
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
							<select id="school" class="form-control" style="width:280px;">
								<option value="">选择学校</option>
							</select>
							<button id="submitOper">
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
	
<!-- 关联学校 -->
	<div class="page-school-panel full-drop-panel">
		<div class="title-bar">
			<h4>
				<i class="fa fa-plus"></i>
			</h4>
			<div class="btn-area">
				<div style="margin-top: 4px;">
					<button id="schoolSaves">
						<i class="fa fa-check"></i>保存
					</button>
					<button id="cancelBTN">
						<i class="fa fa-times"></i>取消
					</button>
				</div>
			</div>
		</div>
		<div class="page-content">
		 <form id="editorForm">
			<div id="d1">
				<div id="d2">选学校</div>
				<div id="d3">
					<table cellpadding="0" cellspacing="8">
						<tr>
							<td style="font-size: 16px;">可选学校</td>
							<td>&nbsp;</td>
							<td style="font-size: 16px;">已选学校</td>
						</tr>
						<tr>
							<td><select id="s1" name="s1" style="width: 220px; height: 300px;" multiple="multiple">
							</select></td>
							<td>
								<p>
									<input id="b1" type="button" class="s1" value="选中右移&nbsp;&gt;" />
								</p>
								<p>
									<input type="button" id="b2" class="s1" value="全部右移&nbsp;&gt;&gt;" />
								</p>
								<p>
									<input type="button" id="b3" class="s1" value="&lt;&nbsp;选中左移" />
								</p>
								<p>
									<input type="button" id="b4" class="s1" value="&lt;&lt;&nbsp;全部左移" />
								</p>
							</td>
							<td><select id="s2" name="schoolId"
								style="width: 220px; height: 300px;" multiple="multiple"></select></td>
						 </tr>
					</table>
				</div>
			</div>
			</form>
		</div>
	</div>


<!-- 关联班级 -->
    <div class="page-class-panel full-drop-panel">
		<div class="title-bar">
			<h4>
				<i class="fa fa-plus"></i>
			</h4>
			<div class="btn-area">
				<div style="margin-top: 4px;">
					<button id="classSaves">
						<i class="fa fa-check"></i>保存
					</button>
					<button id="cancelBTN">
						<i class="fa fa-times"></i>取消
					</button>
				</div>
			</div>
		</div>
		<div class="page-content">
			<div id="d1">
				<div id="d2">选年级班级</div>
				<div id="d3">
					<table cellpadding="0" cellspacing="8">
						<tr>
							<td style="font-size: 16px;">可选年级班级</td>
							<td>&nbsp;</td>
							<td style="font-size: 16px;">已选年级班级</td>
						</tr>
						<tr>
							<td><select id="class_s1" name="s1"
								style="width: 220px; height: 300px;" multiple="multiple">
							</select></td>
							<td>
								<p>
									<input id="class_b1" type="button" class="s1" value="选中右移&nbsp;&gt;" />
								</p>
								<p>
									<input type="button" id="class_b2" class="s1" value="全部右移&nbsp;&gt;&gt;" />
								</p>
								<p>
									<input type="button" id="class_b3" class="s1" value="&lt;&nbsp;选中左移" />
								</p>
								<p>
									<input type="button" id="class_b4" class="s1" value="&lt;&lt;&nbsp;全部左移" />
								</p>
							</td>
							<td><select id="class_s2" name="s2"
								style="width: 220px; height: 300px;" multiple="multiple"></select></td>
						</tr>
					</table>
				</div>
			</div>
		</div>
	</div>




<!-- 关联科目 -->
    <div class="page-course-panel full-drop-panel">
		<div class="title-bar">
			<h4>
				<i class="fa fa-plus"></i>
			</h4>
			<div class="btn-area">
				<div style="margin-top: 4px;">
					<button id="courseSaves">
						<i class="fa fa-check"></i>保存
					</button>
					<button id="cancelBTN">
						<i class="fa fa-times"></i>取消
					</button>
				</div>
			</div>
		</div>
		<div class="page-content">
			<div id="d1">
				<div id="d2">选科目</div>
				<div id="d3">
					<table cellpadding="0" cellspacing="8">
						<tr>
							<td style="font-size: 16px;">可选科目</td>
							<td>&nbsp;</td>
							<td style="font-size: 16px;">已选科目</td>
						</tr>
						<tr>
							<td><select id="course_s1" name="s1"
								style="width: 220px; height: 300px;" multiple="multiple">
							</select></td>
							<td>
								<p>
									<input id="course_b1" type="button" class="s1" value="选中右移&nbsp;&gt;" />
								</p>
								<p>
									<input type="button" id="course_b2" class="s1" value="全部右移&nbsp;&gt;&gt;" />
								</p>
								<p>
									<input type="button" id="course_b3" class="s1" value="&lt;&nbsp;选中左移" />
								</p>
								<p>
									<input type="button" id="course_b4" class="s1" value="&lt;&lt;&nbsp;全部左移" />
								</p>
							</td>
							<td><select id="course_s2" name="s2"
								style="width: 220px; height: 300px;" multiple="multiple"></select></td>
						</tr>
					</table>
				</div>
			</div>
		</div>
	</div>
	
	
	<!-- 添加教师 -->
	<div class="page-addTeacher-panel full-drop-panel">
		<div class="title-bar">
			<h4>
				<i class="fa fa-plus"></i>
			</h4>
			<div class="btn-area">
				<div style="margin-top: 4px;">
					<button id="saveBTN">
						<i class="fa fa-check"></i>保存
					</button>
					<button id="cancelBTN"  onclick="cancelBTN()">
						<i class="fa fa-times"></i>取消
					</button>
				</div>
			</div>
		</div>
		<div class="page-content">
			<div class="page-inner-content">
				<div id="tabs">
					<ul>
						<li style="background:#9bc609;"><a href="#frame1">社会人员</a></li>
						<!-- <li style="background:#9bc609;"><a href="#frame2">外校老师</a></li> -->
					</ul>
					<div id="frame1">
						<ul>
							<li>
								<label for="chinesename">
									姓名
									<input id="chinesename" class="form-control" type="text" style="width:150px;" name="chinesename" placeholder="请输入姓名">
								</label>
							</li>
							<li>
								<label for="username">
									<span style="margin-left:-12px">用户名</span>
									<input id="username" class="form-control" type="text" style="width:150px;" name="username" placeholder="请输入用户名">
								</label>
							</li>
							<li>
								<label for="password">
									密码
									<input id="password" class="form-control" type="password" style="width:150px;" name="password" placeholder="请输入密码">
								</label>
							</li>
						</ul>	
					</div>
					<div id="frame2">
						<input id="realname" class="form-control" type="text" style="width:150px;margin-top:-3px;" name="realname" placeholder="请输入姓名或账号">
						<button id="searchoutOfTeacher" title="查询" class="page-button" style="margin-left: 0px;">
								<i class="fa fa-search"></i>查询
						</button>
						<table  id="addTeacherInfo">
								<thead>
									<tr class="text-center">
										<td style="font-size:16px;">姓名</td>
										<td style="font-size:16px;">账号</td>
										<td style="font-size:16px;">学校</td>
										<td style="font-size:16px;">操作</td>
									</tr>
								</thead>
								<tbody>
									
								</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>  
</body>
</html>