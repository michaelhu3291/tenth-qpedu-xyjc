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
</style>
<script type="text/javascript">
	var listId = "#list2", 
	editorFormId = "#editorForm",
	pagerId = '#pager2', 
	listUrl = "../teaManagement/teacherInfo.do?command=searchPaging",
	loadUrl="../teaManagement/teacherInfo.do?command=getSchoolById"; 
	saveSchoolUrl="../teaManagement/teacherInfo.do?command=saveSchool";
	loadClassUrl="../teaManagement/teacherInfo.do?command=getClassById";
	saveClassUrl="../teaManagement/teacherInfo.do?command=saveClass";
	loadCourseUrl="../teaManagement/teacherInfo.do?command=getCourseById";
	saveCourseUrl="../teaManagement/teacherInfo.do?command=saveCourse";
	/*遍历待选择的学校  */
	var reSetSchoolList=function(date){
		var selObj = $("#s1");
		selObj.find("option").remove();
		$.each( date, function(i, val){
			 selObj.append("<option value='"+val.School_Code+"' >"+val.School_Name+"</option>");
	   });
	}
	/*遍历已选择的学校  */
	var reSetSchoolByIdList=function(date){
		var selObj = $("#s2");
		selObj.find("option").remove();
		$.each( date, function(i, val){
			if(val!=null){
				if(typeof(val.School_Name) != 'undefined'){
					selObj.append("<option value='"+val.School_Code+"'>"+val.School_Name+"</option>");
					}
			}
	}); 
	}
	/*遍历待选择的班级  */
	var reSetClassList=function(date){
		var selObj = $("#class_s1");
		selObj.find("option").remove();
		$.each( date, function(i, val){
			 selObj.append("<option value='"+val.id+"' >"+val.Class_Name+"</option>");
	   });
	}
	/*遍历已选择的班级  */
	var reSetClassByIdList=function(date){
		var selObj = $("#class_s2");
		selObj.find("option").remove();
		$.each( date, function(i, val){
			    	selObj.append("<option value='"+val.Class_Id+"'>"+val.Class_Name+"</option>");
			    
	}); 
	}
	
	/*遍历待选择的学科目 */
	var reSetCourseList=function(date){
		var selObj = $("#course_s1");
		selObj.find("option").remove();
		$.each( date, function(i, val){
			 selObj.append("<option value='"+val.id+"' >"+val.Course_Name+"</option>");
	   });
	}
	/*遍历已选择的科目  */
	var reSetCourseByIdList=function(date){
		var selObj = $("#course_s2");
		selObj.find("option").remove();
		$.each( date, function(i, val){
			    	selObj.append("<option value='"+val.Course_Id+"'>"+val.Course_Name+"</option>");
			    
	}); 
	}

	$(function() {
		$(".search-panel").show().data("show", true);
		//根据登录用户显示其所属的学校类型和学校名称
		 $.ajax({  
	            type : "POST",  
	            url : "../teaManagement/teacherInfo.do?command=getSchoolTypeAndSchoolName",  
	            data : {},
	            dataType:"JSON",
	            async: false,
	            success : function(data){
	            	//$("#schoolType").val(data[0].School_Type_Sequence);
	            	//$("#schoolType").html(data[0].School_Type);
	            	//$("#school").val(data[0].School_Code);
	            	//$("#school").html(data[0].School_Name);
	            	if(data[0].mess != null && data[0].mess == "admin"){
	            		for(var i = 0;i < data.length;i++){
	            			$("#schoolType").append("<option value='"+data[i].DictionaryCode+"'>"+data[i].DictionaryName+"</option>");
	            		}
	            	}
	            	else{
	            		$("#schoolType option[value = '']").remove();
	            		$("#school option[value = '']").remove();
	            		$("#schoolType").append("<option value='"+data[0].School_Type_Sequence+"'>"+data[0].School_Type+"</option>");
		            	$("#school").append("<option value='"+data[0].School_Code+"'>"+data[0].School_Name+"</option>");
	            	}
	            	
	       }
	     });
		
		//选择学校类型关联学校
	        $("#schoolType").change(function () {
	            $("#school option[value != '']").remove();
	            var schoolType = $("#schoolType").val();
	            if(schoolType == "2"){
	            	schoolType = "xx";
	            }
	            if(schoolType == "3"){
	            	schoolType = "cz";
	            }
	            if(schoolType == "4"){
	            	schoolType = "gz";
	            }
	            if (schoolType != "") {
	                var url = "../platform/dictionary.do?command=getSchoolByCode";
	                var data = {
	                    schoolType: schoolType
	                };
	                $.ajax({
	                    url: url,
	                    type: "POST",
	                    data: data,
	                    dataType: "JSON",
	                    success: function (data) {
	                        for (var i = 0; i < data.length; i++) {
	                            $("#school").append("<option value='" + data[i].School_Code + "'>" + data[i].School_Name + "</option>");
	                        }
	                        /*if ($("#school").find("option").length > 0) {
	                            //多选下拉框
	                            $("#course").multiselect({
	                                checkAllText: "全选",
	                                uncheckAllText: "全不选",
	                                noneSelectedText: "选择科目",
	                                selectedText: '#' + " 科目",
	                                selectedList: 2
	                            });
	                            //刷新多选下拉框的值
	                            $("#school").multiselect('refresh');
	                        } else {
	                            $("#course").multiselect({
	                                checkAllText: "全选",
	                                uncheckAllText: "全不选",
	                                noneSelectedText: "选择科目",
	                                selectedText: '#' + " 科目",
	                            });
	                        }*/
	                    }
	                });
	            }
	        });
		 
		_initButtons({
			add_school : function(ev) {
				var $i = $(ev.currentTarget).find("i"),
				idAry = $(listId).jqGrid("getGridParam", "selarrrow");
		        if (idAry.length === 0) {
		            window.message({
		                text: "请选择要关联的记录!",
		                title: "提示"
		            });
		            return;
		        }
		        if (idAry.length > 1) {
		            window.message({
		                text: "每次只能关联单条记录!",
		                title: "提示"
		            });
		            return;
		        }
		        $.ajax({  
		            type : "GET",  
		            url : loadUrl,  
		            data : {id:idAry[0]},
		            dataType:"json",
		            async : false,  
		            success : function(data){
		            	var schoolByIdList=data.schoolByIdList;
		            	var schoolList=data.schoolList;
		            	reSetSchoolList(schoolList);
		            	reSetSchoolByIdList(schoolByIdList);
		       }
		     });
		        showSlidePanel(".page-school-panel");
	   				
			},
			schoolSaves : function() {
				var optionVar=$("#s2").find("option");
				var schoolIds=[];
				//得到选中后学校id的集合
				if(optionVar!=null){
					for(var i=0;i<optionVar.length;i++){
						schoolId=optionVar.eq(i).val();
						schoolIds.push(schoolId);
					}
				}
				var teacherId=$(listId).jqGrid("getGridParam", "selarrrow")[0];
						POST(saveSchoolUrl,{"schoolIds":schoolIds,"teacherId":teacherId},function(data){
							$(listId).trigger("reloadGrid");
							hideSlidePanel(".page-school-panel");
						});
			},
		  add_class : function(ev){
				 var $i = $(ev.currentTarget).find("i"),
					idAry = $(listId).jqGrid("getGridParam", "selarrrow");
			        if (idAry.length === 0) {
			            window.message({
			                text: "请选择要关联的记录!",
			                title: "提示"
			            });
			            return;
			        }
			        if (idAry.length > 1) {
			            window.message({
			                text: "每次只能关联单条记录!",
			                title: "提示"
			            });
			            return;
			        }
			        $.ajax({  
			            type : "GET",  
			            url : loadClassUrl,  
			            data : {id:idAry[0]},
			            dataType:"json",
			            async : false,  
			            success : function(data){
			            	var classByIdList=data.classByIdList;
			            	var classList=data.classList;
			            	reSetClassList(classList);
			            	reSetClassByIdList(classByIdList);
			       }
			     });
			        showSlidePanel(".page-class-panel");
		   				
				},
				classSaves : function() {
					var optionVar=$("#class_s2").find("option");
					//得到选中后班级id集合
					var classIds=[];
					if(optionVar!=null){
						for(var i=0;i<optionVar.length;i++){
							classId=optionVar.eq(i).val();
							classIds.push(classId);
						}
					}
				var teacherId=$(listId).jqGrid("getGridParam", "selarrrow")[0];
							POST(saveClassUrl,{"classIds":classIds,"teacherId":teacherId},function(data){
								$(listId).trigger("reloadGrid");
								hideSlidePanel(".page-class-panel");
							});
			},
			add_course : function(ev){
				 var $i = $(ev.currentTarget).find("i"),
					idAry = $(listId).jqGrid("getGridParam", "selarrrow");
			        if (idAry.length === 0) {
			            window.message({
			                text: "请选择要关联的记录!",
			                title: "提示"
			            });
			            return;
			        }
			        if (idAry.length > 1) {
			            window.message({
			                text: "每次只能关联单条记录!",
			                title: "提示"
			            });
			            return;
			        }
			        $.ajax({  
			            type : "GET",  
			            url : loadCourseUrl,  
			            data : {id:idAry[0]},
			            dataType:"json",
			            async : false,  
			            success : function(data){
			            	var courseByIdList=data.courseByIdList;
			            	var courseList=data.courseList;
			            	reSetCourseList(courseList);
			            	reSetCourseByIdList(courseByIdList);
			       }
			     });
			        showSlidePanel(".page-course-panel");
				},
				courseSaves : function() {
					var optionVar=$("#course_s2").find("option");
					//得到选中后科目id集合
					var courseIds=[];
					if(optionVar!=null){
						for(var i=0;i<optionVar.length;i++){
							courseId=optionVar.eq(i).val();
							courseIds.push(courseId); 
						}
					}
					var teacherId=$(listId).jqGrid("getGridParam", "selarrrow")[0];  //得到选中行的id 
					var rowData = $(listId).jqGrid("getRowData",teacherId);       // 根据id得到选中行的数据
					var teacherType=rowData.Teacher_Position;   //得到选中行的教师类型
							POST(saveCourseUrl,{"courseIds":courseIds,"teacherId":teacherId,"teacherType":teacherType},function(data){
								$(listId).trigger("reloadGrid");
								hideSlidePanel(".page-course-panel");
							});
			},
			cancelBTN : function() {
				hideSlidePanel(".page-school-panel,.page-class-panel,.page-course-panel");
			},
		});//from page.common.js
		_initFormControls();//from page.common.js
		_initValidateForXTypeForm(editorFormId);
		var _colModel = [ {
			name : 'id',
			key : true,
			hidden : true,
			search : false
			},{
				name : 'schoolTypeSequence',
				key : true,
				hidden : true,
			    search : false
		      },{
			name : 'teaType',
			key : true,
			hidden : true,
		    search : false
	      },{
			name : 'teaName',
			index:"teaName",
			sortable : false,
			autoWidth : true,
			align : "center"
		}, {
			name : 'loginName',
			sortable : false,
			autoWidth : true,
			align : "center"
		}, {
			name : 'schoolName',
			sortable : false,
			autoWidth : true,
			align : "left"
		}, {
			name : 'className',
			sortable : false,
			autoWidth : true,
			align : "left"
		} 
		], _colNames = [ '', '','','姓名', '账号','学校', '班级'];
		$(listId).jqGrid($.extend(defaultGridOpts, {
			url : listUrl,
			colNames : _colNames,
			colModel : _colModel,
			pager : pagerId
		}));
		resizeFun();

	});
</script>
</head>
<body>
	<div class="page-list-panel">
		<div class="head-panel">
			<div class="search-panel" style="display:block;">
				<div class="form-panel" style="margin-left:-15px">
				  <form id="searchForm">
					  
						<table style="height: 100%;" cellpadding="0" cellspacing="0"
							border="0">
							<tr>
								<!--<td style="padding-left: 12px; padding-right: 24px;"><i
									class="fa fa-list-ul micon"></i></td>-->
								<td class="buttons">
									<!--<button id="add_school">
										<i class="fa fa-plus"></i>关联学校
									</button>
									<button id="add_class">
										<i class="fa fa-plus"></i>关联班级
									</button>
									<button id="add_course">
										<i class="fa fa-plus"></i>关联科目
									</button>-->
								&nbsp学校类型 
								<select id="schoolType" name="schoolType" class="form-control"  style="width: 190px;" >
									<option value="">选择学校类型</option>
								</select>
								&nbsp学校 
								<select id="school" name="school" class="form-control"  style="width: 190px;" >
									<option value="">选择学校</option>
								</select>
								</td>
								
								
							</tr>
						</table>
					
				  </form>
				
				</div>
			</div>
		</div>
		
		<div class="toolbar" style="position:absolute;left:540px;top:17px;background-color:#E7FCD9;border:none;margin-left:-30px;">
				<table style="height: 100%;" cellpadding="0" cellspacing="0"
					border="0">
					<tr>
						<td style="padding-left: 24px; padding-right: 5px;"><input
									id="fastQueryText"  type="text" placeholder="输入名称或账号"
									style="line-height: 1em; font-size: 1em; cursor: text;margin-left:-20px;" /></td>
								<td>
								
								<!-- <td style="padding-left: 24px; padding-right: 5px;"><input
									id="queryParam" type="text" placeholder=""
									style="line-height: 1em; font-size: 1em; cursor: text;" /></td>
								<td> -->
									<button id="fastSearch" title="查询" style="margin-left: 0px;">
										<i class="fa fa-search"></i>查询
									</button>
								</td>
					</tr>
				</table>
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
				<div id="d2">选班级</div>
				<div id="d3">
					<table cellpadding="0" cellspacing="8">
						<tr>
							<td style="font-size: 16px;">可选班级</td>
							<td>&nbsp;</td>
							<td style="font-size: 16px;">已选班级</td>
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
</body>
</html>