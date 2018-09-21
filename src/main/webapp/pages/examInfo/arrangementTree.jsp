<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="../theme/default/ui.custom.css" rel="stylesheet" />
<link href="../theme/default/ui.jqgrid.css" rel="stylesheet" />
<link href="../theme/default/font.awesome.css" rel="stylesheet" />
<link href="../theme/default/ui.chosen.css" rel="stylesheet" />
<link href="../theme/default/ui.pick.css" rel="stylesheet" />
<link href="../theme/default/page.common.css" rel="stylesheet" />
<script type="text/javascript" src="../js/jquery.js"></script>
<script type="text/javascript" src="../js/ui.custom.js"></script>
<script type="text/javascript" src="../js/ui.jqgrid.js"></script>
<script type="text/javascript" src="../js/ui.autosearch.js"></script>
<script type="text/javascript" src="../js/ui.chosen.js"></script>
<script type="text/javascript" src="../js/ui.pick.js"></script>
<script type="text/javascript" src="../js/ui.common.js"></script>
<script type="text/javascript" src="../js/jquery.validate.js"></script>
<script type="text/javascript" src="../js/page.common.js"></script>
<script type="text/javascript" src='../js/highcharts.js'></script>
<script type="text/javascript" src='../js/util.js'></script>
<script type="text/javascript" src="../js/lab2.js"></script>
<style type="text/css">
body {
	font-size: 75%;
	padding: 12px 12px 12px 12px;
}

.ztree li a {
	padding: 1px 3px 0 0;
	margin: 0;
	cursor: pointer;
	height: 17px;
	color: #333;
	background-color: transparent;
	text-decoration: none;
	vertical-align: top;
	display: inline-block;
	width: 300px;
}

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
	width: 530px;
	height: 400px;
	background-color: #94c476;
	margin: 0 auto;
}

#d2 {
	height: 30px;
	font-size: 24px;
	background-color: #3B5617;
	color: white;
	text-align: center;
}


#fastSearch {
    border: 1px solid #3B5615;
    background: #ffffff;
    font-weight: bold;
    color: #3B5615;
    padding: 0.4em 1em;
    cursor: pointer; 
}
</style>
<script type="text/javascript">
	var listId = "#list2", editorFormId = "#editorForm", 
	pagerId = "#pager2",
	getCourseByGradeUrl="../authority/dataDictionary.do?command=getCoursesByCode";
	$(function() {
		/*遍历待选择的学科目 */
		var reSetTeacherList = function(date) {
			var selObj = $("#teacher_s1");
			selObj.find("option").remove();
			$.each(date, function(i, val) {
				selObj.append("<option value='"+val.Teacher_Pk+"' >"
						+ val.Teacher_Name + "</option>");
			});
		}
		/*遍历已选择的科目  */
		var reSetTeacherByIdList = function(date) {
			var selObj = $("#teacher_s2");
			selObj.find("option").remove();
			$.each(date, function(i, val) {
				selObj.append("<option value='"+val.Teacher_Pk+"'>"
						+ val.Teacher_Name + "</option>");
			});
			/* if(selObj.find("option").length==0){
				$("#teacher_s2").attr("disabled",true);
				selObj.append("<option value='' '>请选择老师</option>");
			} */
		};
			loadCourse=function(){
				var grade=$("#gradeId").val();
				var schoolType = ""; 
		   		if(grade == "11" || grade == "12" || grade == "13" || grade == "14" || grade == "15"){
		   			schoolType = "xx";
		   		}
		   		if(grade == "16" || grade == "17" || grade == "18" || grade == "19"){
		   			schoolType = "cz";
		   		} 
		   		if(grade == "31" || grade == "32" || grade == "33"){
		   			schoolType = "gz";
		   		}
		   		$("#course option[value != '']").remove();
				 $.ajax({
		            url: getCourseByGradeUrl,
		            type: "POST",
		            data: {schoolType:schoolType},
		            dataType: "JSON",
		            success: function (data) {
		                for (var i = 0; i < data.length; i++) {
		                	$("#fastQueryText").append("<option value='" + data[i].DictionaryCode + "'>" + data[i].DictionaryName + "</option>");
		                	$("#fastQueryText option[value='lhhj']").remove();
		                }
		                var url="../examInfo/markingArrangement_xjkw.do?command=getTeacherByCourseAndExamCode";
		    			$.ajax({
		    				url : url,
		    				type : "POST",
		    				data : {},
		    				dataType : "JSON",
		    				success : function(data) {
		    					 $("#fastQueryText").find("option[value='"+data.course+ "']").attr("selected","selected");
		    				}
		    			});
		            }
				});    
			}
			loadCourse();
			
			/* if($("#teacher_s2 option").length>0){
				$("#teacher_s2").attr("disabled",false);
				$("#teacher_s2").find("option[value='']").remove();
			} */
		var loadTeacher=function(course){
			var url = "../examInfo/markingArrangement_xjkw.do?command=getTeacherByCourseAndGrade";
			$.ajax({
				url : url,
				type : "POST",
				data : {courses:course},
				dataType : "JSON",
				async : false,
				success : function(data) {
				
					var teacherByExamCodeList = data.teacherByExamCodeList;
					var teacherList = data.teacherList;
					reSetTeacherList(teacherList);
					reSetTeacherByIdList(teacherByExamCodeList);
				}
			});
		}
		loadTeacher();
		$("#fastQueryText").change(function(){
			 var course=$("#fastQueryText option:selected").val();
				 loadTeacher(course);
		});
	});
	
	
	function getData() {
		var optionVar = $("#teacher_s2").find("option");
		var teacherList = [];
		for (var i = 0; i < optionVar.length; i++) {
				var params = {};
				params["id"] = optionVar[i].value;
				teacherList.push(params);
		}
		return teacherList;
	};
	
	var  mouseOut=	 function(){
		 $("#fastSearch").css({"border":"1px solid #3B5617","color":"#3B5615","background":"#ffffff"});
	 };
	var  mouseOver=	 function(){
		 $("#fastSearch").css({"border":"1px solid #3B5617","background":"#3B5615","color":"#ffffff"});
	 };
</script>
</head>
<body>
	<!-- 老师 -->
	<div align="center">
		<div id="d1">
		<input type="hidden" id="gradeId" value="${gradeId}"/>
			<div id="d2">选择老师</div>
			<div id="d3">
				<table cellpadding="0" cellspacing="8">
				<tr>
				      <td>
					    <select id="fastQueryText" style="line-height: 1em; font-size: 1em; cursor: text;height:20px; width: 120px;"> </select>
							<!-- <button id="fastSearch" title="查询"  onmouseover="mouseOver()" onmouseout="mouseOut()">
								<i class="fa fa-search"></i>查询
							</button> -->
						</td>
				</tr>
					<tr>
						<td style="font-size: 16px;">未安排</td>
						<td>&nbsp;</td>
						<td style="font-size: 16px;">已安排</td>
					</tr>
					<tr>
						<td><select id="teacher_s1" name="s1"
							style="width: 180px; height: 200px;" multiple="multiple">
						</select></td>
						<td>
							<p>
								<input id="teacher_b1" type="button" class="s1"
									value="选中右移&nbsp;&gt;" />
							</p>
							<p>
								<input type="button" id="teacher_b2" class="s1"
									value="全部右移&nbsp;&gt;&gt;" />
							</p>
							<p>
								<input type="button" id="teacher_b3" class="s1"
									value="&lt;&nbsp;选中左移" />
							</p>
							<p>
								<input type="button" id="teacher_b4" class="s1"
									value="&lt;&lt;&nbsp;全部左移" />
							</p>
						</td>
						<td><select id="teacher_s2" name="s2"
							style="width: 180px; height: 200px;" multiple="multiple"></select></td>
					</tr>
				</table>
			</div>
		</div>
	</div>

</body>
</html>