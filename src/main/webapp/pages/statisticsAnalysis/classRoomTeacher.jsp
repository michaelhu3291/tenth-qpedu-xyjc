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
<link href="../theme/default/jquery.multiselect.css" rel="stylesheet" />
<link href="../theme/default/jquery.multiselect.filter.css"	rel="stylesheet" />
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
<script type="text/javascript" src="../js/jquery.multiselect.js"></script>
<script type="text/javascript" src="../js/jquery.multiselect.filter.js"></script>


<style>
.ui-multiselect-checkboxes {
	border: 1px solid #aaa;
	background: #fff;
	color: #555;
}
.ui-button-text {
	font-weight: bold;
}

.ui-multiselect, span {
	font-weight: normal;
}

.ui-multiselect {
	border: 1px solid #aaa;
}

.ui-multiselect.ui-state-hover {
	border: 1px solid #3B5615;
	background: #ffffff;
	font-weight: bold;
	color: #3B5615;
}

.form-control {
	color: #3B5615;
}

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

#tblInfo input {
	background-color: #F6F6F6;
}

#stu_look input {
	width: 100%;
	height: 100%;
	border: 1px;
	text-align: center;
	background-color: white;
}

#stu_look td {
	height: 30px;
}

#tabs-2 {
	height: 847px;
}

.ui-widget-header a {
	color: white;
}

.ui-jqgrid .ui-jqgrid-titlebar {
	font-size: 16px;
}
</style>
<script type="text/javascript">
    var listId = "#list2",
  //根据登录用户显示其所属的年级和班级
	    classNameByGrade=[],
	    classIdByGrade=[],
	    courseNameByLoginName=[],
	    courseCodeByLoginName=[],
        editorFormId = "#editorForm",
        pagerId = "#pager2",
        editorRelatedFormId = "#editorRelatedForm",
        listUrl = "../statisticsAnalysis/classRoomTeacher.do?command=searchScore";
    $(function () {
    	$(".ui-multiselect-none").hide();
		  $("#course").multiselect({
	            checkAllText: "全选",
	            uncheckAllText: "全不选",
	            noneSelectedText: "选择科目",
	            selectedText: '#' + " 科目",
	            selectedList: 2
	        });
    	//根据登录用户显示其所属的学校类型和学校名称
		 $.ajax({  
	            type : "POST",  
	            url : "../statisticsAnalysis/subTeaScoreSearch.do?command=getSchoolTypeAndSchoolNameByLoginName",  
	            data : {},
	            dataType:"JSON",
	            async: false,
	            success : function(data){
	            	$("#schoolType").append("<option value='"+data[0].School_Type_Sequence+"'>"+data[0].School_Type+"</option>");
	            	$("#school").append("<option value='"+data[0].School_Code+"'>"+data[0].School_Name+"</option>");
	       }
	     });
    	
    	
		//根据登录名得到年级和班级
		 $.ajax({  
	            type : "POST",  
	            url : "../statisticsAnalysis/subTeaScoreSearch.do?command=getGradeAndClassByLoginName",  
	            data : {},
	            dataType:"JSON",
	            async: false,
	            success : function(data){
	            	for(var i=0;i<data.length;i++){
	            		var gradeId=$("#grade").find("option").val();
	            		if(gradeId!=data[i].Grade_Id){
	            			$("#grade").append("<option value='"+data[i].Grade_Id+"'>"+data[i].Grade_Name+"</option>");
	            		}
	            		var className=data[i].Class_Name;
						var classIdOneStr=className.substring(0, 1);
						if(classIdOneStr==0){
							className=className.substring(className.length-1);
							className="("+className+")班";
						}else{
							className="("+className+")班";
						}
						$("#classs").append("<option value='"+data[i].Class_Name+"'>"+ className+ "</option>");
						//classIdByGrade.push(data[i].Class_Name);
		            	//classNameByGrade.push(className);
	            	}
	            
	       }
	     });
		//根据登录用户显示其教科目的名称
	/* 	  $.ajax({  
	            type : "POST",  
	            url : "../statisticsAnalysis/subTeaScoreSearch.do?command=getCourseByLoginName",  
	            data : {},
	            dataType:"JSON",
	            async: false,
	            success : function(data){
	            	for(var i=0;i<data.length;i++){
	            		courseNameByLoginName.push(data[i].Course_Name);
	            		courseCodeByLoginName.push(data[i].Course_Code);
	            	}
	       }
	     });  */
		
		  $("#course").attr("multiple", "multiple");
          $("#course option[value != '']").remove();
          var schoolType = $("#schoolType").val();
          if (schoolType != "") {
              var url = "../platform/dictionary.do?command=getCoursesByCode";
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
                          $("#course").append("<option value='" + data[i].DictionaryCode + "'>" + data[i].DictionaryName + "</option>");
                      }
                      if ($("#course").find("option").length > 0) {
                          //多选下拉框
                          $("#course").multiselect({
                              checkAllText: "全选",
                              uncheckAllText: "全不选",
                              noneSelectedText: "选择科目",
                              selectedText: '#' + " 科目",
                              selectedList: 2
                          });
                          //刷新多选下拉框的值
                          $("#course").multiselect('refresh');
                      } else {
                          $("#course").multiselect({
                              checkAllText: "全选",
                              uncheckAllText: "全不选",
                              noneSelectedText: "选择科目",
                              selectedText: '#' + " 科目",
                          });
                      }
                  }
              });
          } else {
              $("#course").multiselect('refresh');
          }
		
	
        _initButtons({
            cancelBTN: function () {
                    $(listId).trigger("reloadGrid");
                    hideSlidePanel(".page-editor-panel");
                    editId = '';
                },
        }); //from page.common.js
        $("#tblInfo").find("button").button();
        $("#tabs").tabs({
            heightStyle: "fill"
        });
        
        //加载数据字典
        loadDictionary(function () {
            //显示当前年
            var year = new Date().getFullYear();
            var month = new Date().getMonth() + 1;
            var currentYear = "";
            if (month < 9) {
                currentYear = (year - 1) + "-" + year;
            } else {
                currentYear = year + "-" + (year + 1);
            }
            $("#schoolYear").val(currentYear);
            
           $("#term").find("option[value = 'xxq']").attr("selected","selected");
            $("#examType").find("option[value = 'qm']").attr("selected","selected");
      
        });
        $("#tabs-2,#tabs-1").css("height", "auto");
        _initFormControls(); //from page.common.js
        _initValidateForXTypeForm(editorFormId);
        
        
        
      //  loadClassByGrade();
		//loadCourseBySchoolType();
		
		$("#fastSearch").click(function() {
			//if($("#course").find("option:selected").length>0){
			//绑定jqgrid
			 var _colModel = [{
                name: 'Id',
                key: true,
                width: 60,
                hidden: true,
                search: false
            }, {
            	label:"考号",
                name: 'Exam_Number',
                sortable: false,
                autoWidth: true,
                align: "center",
            }, {
            	label:"姓名",
                name: 'Name',
                sortable: false,
                autoWidth: true,
                align: "center",
            }];
			var fruit = "";
			var courseTxt = [];
			var cousreVal = [];
			//循环遍历得到选中的科目
			var txt = $("#course").find("option:selected")
					.each(function() {
						courseTxt.push($(this).text());
						cousreVal.push($(this).val());
					});
			//声明对象
			function Object(label, name, sortable, autoWidth,
					align) {
				this.label = label;
				this.name = name;
				this.sortable = sortable;
				this.autoWidth = autoWidth;
				this.align = align;
			}
			for (var a = 0; a < courseTxt.length
					&& a < cousreVal.length; a++) {
				//追加列表信息
				_colModel.push(new Object(courseTxt[a],
						cousreVal[a], false, true, "center"));
			}
			_colModel.push(new Object("总分",
					"total", false, true, "center"));
			//成绩信息描述
			var scoreHtml;
			var schoolYear = $("#schoolYear").find("option:selected").text();
			var schoolType = $("#schoolType").find("option:selected").val();
			var term = $("#term").find("option:selected").val();
			var examType = $("#examType").find("option:selected").val();
			var grade = $("#grade").find("option:selected").val();
			var course = $("#course").find("option:selected").val();
			var school = $("#school").find("option:selected").val();
			if (schoolType != "") {
				schoolType = $("#schoolType").find("option:selected").text();
			}
			if (term != "") {
				term = $("#term").find("option:selected").text();
			}
			if (examType != "") {
				examType = $("#examType").find("option:selected").text();
			}
			if (grade != "") {
				grade = $("#grade").find("option:selected").text()+ "年级";
			}
			if (school != "") {
				school = $("#school").find("option:selected").text();
			}
			if (courseTxt.length>0) {
				course = $("#course").find("option:selected").text();
			}else{
				window.message({
                    text: "请选择科目",
                    title: "提醒",
                    buttons: {
                        "确认": function () {
                            window.top.$(this).dialog("close");
                        }
                    },
                  });
				return;
			}
			scoreHtml = schoolYear + examType + school + grade
					+ term + courseTxt + "成绩列表";
			//重新创建列表
			$(listId).GridUnload();
			$(listId).jqGrid($.extend(defaultGridOpts, {
				url : listUrl,
				colModel : _colModel,
				pager : pagerId,
				caption : scoreHtml,
				rowNum: 20,
			    rowList: [20, 30, 50, 100 ],
				height : "100%",
				multiselect : false
			}));
			resizeFun("549");
			$(".l_statis").show();
			//}
		});
    });
    
    
    
	//根据年级得到班级集合
	function loadClassByGrade(){
		$("#classs").attr("multiple", "multiple");
		$("#classs option[value != '']").remove();
		var gradeId = $("#grade").val();
		 if (gradeId != "") {
			 var url = "../statisticsAnalysis/subTeaScoreSearch.do?command=getClassByGrade";
			 var data = {gradeId : gradeId}; 
			 $.ajax({
					 url : url,
					 type : "POST",
					 data : data,
					 dataType : "JSON",
					 success : function(data) {
						 var className;
							 for (var i = 0; i < data.length; i++) 
							 {
								   className=data[i].Class_Name;
									var classIdOneStr=className.substring(0, 1);
									if(classIdOneStr==0){
										className=className.substring(className.length-1);
										className="("+className+")班";
									}else{
										className="("+className+")班";
									}
								 	$("#classs").append("<option value='"+data[i].Class_Name+"'>"+ className+ "</option>");
								 	var classNames=$("#classs").find("option").eq(i).val();
								 	for (var j=0;j<classNameByGrade.length && j<classIdByGrade.length;j++) {
								 		if(className==classNameByGrade[j]){
												if(classNames==classIdByGrade[j]){
													$("#classs").find("option[value='"+classNames+"']").attr("selected",true);
												}
										   }
									 }  
							 }
						 if ($("#classs").find("option").length > 0) {
							 //多选下拉框
								$(".ui-multiselect-none").hide();
								$("#classs").multiselect(
												{
													checkAllText : "全选",
												    uncheckAllText : "全不选",
													noneSelectedText : classNameByGrade,
													selectedText : '#'
															+ " 班级",
													selectedList : 3
												});
								//刷新多选下拉框的值
							$("#classs").multiselect('refresh');
							} 
					 }
			 });
		 }
	};
	 
	
	
	
	
	
	function loadCourseBySchoolType(){
		        var schoolType = $("#schoolType").val();
				if (schoolType != "") {
					var url = "../platform/dictionary.do?command=getCoursesByCode";
					var data = {
						schoolType : schoolType
					};
					$.ajax({
								url : url,
								type : "POST",
								data : data,
								dataType : "JSON",
								success : function(data) {
									for (var i = 0; i < data.length; i++) {
										$("#course").append("<option value='"+data[i].DictionaryCode+"'>"
																+ data[i].DictionaryName
																+ "</option>");
									
									 	var courseNames=$("#course").find("option").eq(i).val();
									 	for (var j=0;j<courseNameByLoginName.length && j<courseCodeByLoginName.length;j++) {
									 		if(data[i].DictionaryName==courseNameByLoginName[j]){
													if(courseNames==courseCodeByLoginName[j]){
														$("#course").find("option[value='"+courseNames+"']").attr("selected",true);
													}
											   }
										 }  
									
									}
									if ($("#course").find("option").length > 0) {
										//多选下拉框
										$("#course").multiselect(
														{
															checkAllText : "全选",
															uncheckAllText : "全不选",
															noneSelectedText :courseNameByLoginName,
															selectedText : '#'
																	+ " 科目",
															selectedList : 2
														});
										//刷新多选下拉框的值
										$("#course").multiselect('refresh');
									}
								}
							});
				} else {
					$("#course").multiselect('refresh');
				}
	}
    
    window.onload = function () {
    	//$("#classs option[value != '']").remove();
        $("#term option[value != '']").remove();
        $("#examType option[value != '']").remove();
    }
</script>
</head>
<body style="overflow: auto;">
    <div class="page-list-panel" style="overflow-y: auto;overflow-x: hidden;">
        <div class="head-panel">
            <!-- 多条件 -->
            <div id="importCondition" style="margin: -5px 10px 10px 0px;">
                <div class="search-panel" style="display: block;">
                    <p>
                                                                        学年
                        <select id="schoolYear" name="schoolYear" class="form-control" data-dic="{code:'ND'}" ></select>
                        &nbsp; 学校类型
                        <select id="schoolType" name="schoolType" class="form-control" style="width: 150px;">
                        </select>
                        &nbsp; 学期
                        <select id="term" name="term" class="form-control" style="width: 150px;" data-dic="{code:'xq'}">
                        </select>
                       &nbsp;测试类型
                      <select id="examType" name="examType" class="form-control" style="width: 150px;" data-dic="{code:'kslx'}">
                        </select>
                     &nbsp;年级
                        <select id="grade" name="grade" class="form-control" style="width: 150px;">
                        </select>
                        
                    </p>
                </div>
                <div class="multiselect" style="margin: -5px 10px 10px 0px;">
                    <p>
                          班级
                        <select id="classs" name="classs" class="form-control" style="width: 150px;" ></select>                                    
                                                   
                         &nbsp;  科目
                        <select id="course" name="course" class="form-control" style="width: 150px;"></select>
                     
                        &nbsp; 学校
                        <select id="school" name="school" style="width: 200px;" class="form-control">
                        </select> <span class="toolbar">
							<button id="fastSearch" title="查询" style="margin-left: 0px;">
								<i class="fa fa-search"></i>查询
							</button>
						</span>
                    </p>
                </div>
            </div>
        </div>
        <table id="list2"></table>
        <div id="pager2"></div>
    </div>
</body>
</html>