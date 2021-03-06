<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title>教研员--综合查询页面</title>
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
        editorFormId = "#editorForm",
        pagerId = "#pager2",
        editorRelatedFormId = "#editorRelatedForm",
        listUrl = "../scoreManage/overallSearch.do?command=overallSearchForAdmin";
        var courseTxt = [];
        var courseVal = [];
        var idx="",
    	ci="",
    	so="";
        
        var getPrint=function(){
        	if($("#grade").val() === ""){
    			 window.Msg.alert("请选择年级");
    			 return;
    		 }
        	if(courseVal !=""){
        		var schoolYear=$("#schoolYear").val();
            	var grade=$("#grade").val();
            	var term=$("#term").val();
            	var examType=$("#examType").val();
            	var school=$("#school").val();
            	var examNumberOrStuCode=$("#examNumberOrStuCode").val();
            	var schoolYearTxt = $("#schoolYear").find("option:selected").text();
        		var termTxt = $("#term").find("option:selected").text();
        		var examTypeTxt = $("#examType").find("option:selected").text();
        		var gradeTxt = $("#grade").find("option:selected").text();
            	var scoreHtml = schoolYearTxt + termTxt + examTypeTxt + gradeTxt + "测试成绩列表";
            	var printUrl="../scoreManage/overallSearch.do?command=printLookForAdmin&schoolYear="+ schoolYear + "&grade="+grade+"&term="+term+"&examType="+examType+"&school="+school+"&examNumberOrStuCode="+examNumberOrStuCode+"&courseVal="+courseVal+"&courseTxt="+courseTxt+"&idx="+idx+"&so="+so+"&scoreHtml="+scoreHtml+"&gradeTxt="+gradeTxt;
            	window.open(printUrl);
        	}else{
        		window.Msg.alert("没有数据，无法打印预览");
    			 return;
        	}
        	
        }    
    $(function () {
        //多选下拉框
        $("#school").multiselect({
            checkAllText: "全选",
            uncheckAllText: "全不选",
            noneSelectedText: "所有学校",
            selectedText: '#' + "所学校",
            minWidth:340,
            selectedList: 2
        }).multiselectfilter({
			label : "学校名称",
			placeholder : "请输入校名"
		});
        
        var aa= function(){
        	//$("#course").find("option").attr("value").remove();
	       	 $("#course option[value != '']").remove();
	       	var schoolYear=$("#schoolYear").val();
	       	var grade = $("#grade").val();
	       	var term=$("#term").val();
	       	var examType=$("#examType").val();
	       	 var url = "../statisticsAnalysis/scoreSearch.do?command=getCoursesByCode";
	       	 //data = {grade:grade};
	       	 if(grade != ""){
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
	       		var data = {schoolType:schoolType,schoolYear:schoolYear,grade:grade,term:term,examType:examType};
	       		$.ajax({
	                url: url,
	                type: "POST",
	                data: data,
	                dataType: "JSON",
	                success: function (data) {
	                 courseTxt = [];
	                 courseVal = [];
	                 if(data.length > 0) {
	                	 for(var i = 0;i < data.length;i++){
	                		 courseVal.push(data[i].Course);
	                		 switch(data[i].Course){
	                		 case 'yw':
	                			 courseTxt.push("语文");
	                			 break;
	                		 case 'sx':
	                			 courseTxt.push("数学");
	                			 break;
	                		 case 'yy':
	                			 courseTxt.push("外语");
	                			 break;
	                		 case 'zr':
	                			 courseTxt.push("自然");
	                			 break;
	                		 case 'sxzz':
	                			 courseTxt.push("思想政治");
	                			 break;
	                		 case 'xxkj':
	                			 courseTxt.push("信息科技");
	                			 break;
	                		 case 'ms':
	                			 courseTxt.push("美术");
	                			 break;
	                		 case 'ty':
	                			 courseTxt.push("体育");
	                			 break;
	                		 case 'yyue':
	                			 courseTxt.push("音乐");
	                			 break;
	                		 case 'yjxkc':
	                			 courseTxt.push("研究型课程");
	                			 break;
	                		 case 'tzxkc':
	                			 courseTxt.push("拓展型课程");
	                			 break;
	                		 case 'wl':
	                			 courseTxt.push("物理");
	                			 break;
	                		 case 'hx':
	                			 courseTxt.push("化学");
	                			 break;
	                		 case 'ls':
	                			 courseTxt.push("历史");
	                			 break;
	                		 case 'dl':
	                			 courseTxt.push("地理");
	                			 break;
	                		 case 'sw':
	                			 courseTxt.push("生物");
	                			 break;
	                		 case 'njyy':
	                			 courseTxt.push("牛津英语");
	                			 break;
	                		 case 'xsjyy':
	                			 courseTxt.push("新世纪英语");
	                			 break;
	                		 }
	               		 }
	                	 $("#gradeHidden").val(courseVal);
	                 }	
	               	 
	                }
	            });
	       	 }
        }
        
        /*选择年级关联科目(jqgrid头动态显示科目)*/
        $("#grade").change(function(){
        	aa();
        });
        $("#schoolYear").change(function(){
        	aa();
        });
        $("#term").change(function(){
        	aa();
        });
        $("#examType").change(function(){
        	aa();
        });
        $("#school").change(function(){
        	aa();
        });
        $("#examNumberOrStuCode").change(function(){
        	aa();
        });
        /*$("#course").multiselect({
            checkAllText: "全选",
            uncheckAllText: "全不选",
            noneSelectedText: "选择科目",
            selectedText: '#' + " 科目",
            selectedList: 2
        });*/
        $(".search-panel").show().data("show", true);
        /*$("#grade").multiselect({
            checkAllText: "全选",
            uncheckAllText: "全不选",
            noneSelectedText: "选择年级",
            selectedText: '#' + "年级",
            selectedList: 10
 	});*/
        /*$("#class").multiselect({
            checkAllText: "全选",
            uncheckAllText: "全不选",
            noneSelectedText: "选择班级",
            selectedText: '#' + "班级",
            selectedList: 10
 	});*/

        _initButtons({
            cancelBTN: function () {
                    $(listId).trigger("reloadGrid");
                    hideSlidePanel(".page-editor-panel");
                    editId = '';
                },
                /* fastSearch_instructor: function () {
                    //清空科目数组
                    if ($(listId)[0].p.postData.course != null)
                        $(listId)[0].p.postData.course.length = 0;
                    //清空学校数组
                    if ($(listId)[0].p.postData.school != null)
                        $(listId)[0].p.postData.school.length = 0;
                    $(listId).trigger("reloadGrid", [{
                        page: 1
                    }]);
                } */
        }); //from page.common.js
        $("#tblInfo").find("button").button();
        $("#tabs").tabs({
            heightStyle: "fill"
        });
        $("#tabs-2,#tabs-1").css("height", "auto");
        _initFormControls(); //from page.common.js
        _initValidateForXTypeForm(editorFormId);
        $("#fastSearch_instructor").click(function() {
			
			 if($("#grade").val() === ""){
				 window.Msg.alert("请选择年级");
				 return;
			 }
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
                width: 80,
                align: "center",
            },{
            	label:"学籍号",
                name: 'XJFH',
                sortable: false,
                width: 140,
                align: "center",
            }, {
            	label:"姓名",
                name: 'Name',
                sortable: false,
                width: 60,
                align: "center",
            },{
            	label:"学校",
                name: 'School_Short_Name',
                sortable: false,
                width: 80,
                align: "left",
            },{
            	label:"年级",
                name: 'Grade_Id',
                sortable: false,
                width: 60,
                align: "center",
                formatter: function (ar1, ar2, ar3) {
                    return (_types[ar3.Grade_Id] || "");
                }
            },{
            	label:"班级",
                name: 'Class_Id',
                sortable: false,
                width: 40,
                align: "center",
                formatter: function (ar1, ar2, ar3) {
                    return (_typesClass[ar3.Class_Id] || "");
                }
            }];
			
			//声明对象
				function Object(label, name, width,
						align,autoWidth) {
					this.label = label;
					this.name = name;
					this.width = width;
					this.align = align;
					this.autoWidth = autoWidth;
				}
								
				for (var a = 0; a < courseTxt.length
						&& a < courseVal.length; a++) {
					//追加列表信息
					_colModel.push(new Object(courseTxt[a],courseVal[a], 40, "center",true));
				}
	            
				_colModel.push(new Object("总分","Total_Score", 40, "center",true));
				
				_colModel.push(new Object("区级名次", "orderDistrict", 70, "center",false));
			    _colModel.push(new Object("年级名次", "orderClass", 70, "center",false));
			
			//绑定jqgrid
			var schoolYear = $("#schoolYear").val();
			var term = $("#term").val();
			var examType = $("#examType").val();
			var grade = $("#grade").val();
			var school = $("#school").find("option:selected").val();
			
			var scoreHtml;
			var schoolYearTxt = $("#schoolYear").find("option:selected").text();
			var termTxt = $("#term").find("option:selected").text();
			var examTypeTxt = $("#examType").find("option:selected").text();
			var gradeTxt = $("#grade").find("option:selected").text();
			
			scoreHtml = schoolYearTxt + termTxt + examTypeTxt + gradeTxt + "测试成绩列表";
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
				multiselect : false,
				onSortCol: function (index, columnIndex, sortOrder) {
					idx=index;
					ci=columnIndex;
					so=sortOrder;
			    }
			}));
			resizeFun("549");
			$(".l_statis").show();
			//}
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
            $("#term").find("option[value='xxq']").attr("selected","selected");
            $("#examType").find("option[value='qm']").attr("selected","selected");
        });
        
        //根据登录名查询当前教研员所关联的年级
        $.ajax({
			 url : "../statisticsAnalysis/scoreSearch.do?command=selectGradesForInstructor",
			 type : "POST",
			 data : {},
			 dataType : "JSON",
			 success : function(data) {
			 $("#grade").find("option[value != '']").remove();
			 var gradeList = data;
			 $.each(gradeList,function(index,item){
				 $("#grade").append("<option value='"+item.Grade+"'>"+ _types[item.Grade] + "</option>");
			 });
		     
		}
	});
        
        //选择年级关联学校
        $("#grade").change(function(){
        	var grade = $("#grade").find("option:selected").val();
        	
        	if(grade != "") {
        		$.ajax({
		       			 url : "../statisticsAnalysis/scoreSearch.do?command=getSchoolsByGrade",
		       			 type : "POST",
		       			 data : {grade : grade},
		       			 dataType : "JSON",
		       			 success : function(data) {
		       			 $("#school").find("option[value != '']").remove();
		       			 if(data.length > 0){
		       				 $.each(data,function(index,item){
				   				 $("#school").append("<option value='"+item.School_Code+"'>"+ item.School_Short_Name + "</option>");
				   			  });
		       				$("#school").multiselect('refresh');
		       			 }
		       		}
		       	});
        	}
        });
    }); 
    
  	/*教研员导出成绩查询数据*/
    var exportExcel = function(){
    	if($("#grade").val() === ""){
			 window.Msg.alert("请选择年级");
			 return;
		 }
    	
    	var schoolYear = $("#schoolYear").val();
		var term = $("#term").val();
		var examType = $("#examType").val();
		var grade = $("#grade").val();
		var examNumberOrStuCode = $("#examNumberOrStuCode").val();
		var school = $("#school").find("option:selected");
		var schoolArr = [];
		if(school.length > 0){
			$.each(school,function(index,item){
				schoolArr.push($(item).val());
			});
		}
		var scoreHtml;
		var schoolYearTxt = $("#schoolYear").find("option:selected").text();
		var termTxt = $("#term").find("option:selected").text();
		var examTypeTxt = $("#examType").find("option:selected").text();
		var gradeTxt = $("#grade").find("option:selected").text();
		
		scoreHtml = schoolYearTxt + termTxt + examTypeTxt + gradeTxt + "测试成绩列表";
		
	var data={
			schoolYear:schoolYear,
			term:term,
			examType:examType,
			grade:grade,
			school:schoolArr,
			examNumberOrStuCode:examNumberOrStuCode,
			scoreHtml:scoreHtml,
			courseTxt:courseTxt,
			courseVal:courseVal,
			idx:idx,
			ci:ci,
			so:so
	}

	 var url = "../scoreManage/overallSearch.do?command=exportExcelForAdmin&data="+JSON.stringify(data);
	 var form = $( "#ExportQueryData" ) ;
		form.attr( "action", url) ;
		form.get( 0 ).submit() ; 
  }
</script>
</head>
<body style="overflow: auto;">
	<form id="ExportQueryData" method="post"></form>
    <div class="page-list-panel" style="overflow-y: auto;overflow-x: hidden;">
        <div class="head-panel">
            <!-- 多条件 -->
            <div id="importCondition" style="margin: -5px 10px 10px 0px;">
                <div class="search-panel" style="display: block;">
                   <div> 
                      &nbsp;学年
                        <select id="schoolYear" name="schoolYear" class="form-control" data-dic="{code:'ND'}" style="width: 150px;"></select>
                        <!--&nbsp; 学校类型  <select id="schoolType" name="schoolType" class="form-control" style="width: 150px;" data-dic="{code:'xxlx'}">
                            <option value="">选学校类型</option>
                        </select>-->
                      
                        <!--  &nbsp; 科目
                        <select id="course" name="course" class="form-control" style="width: 150px;">
                             <option value="">选择科目</option>
                        </select>-->
                        &nbsp; 年级
                        <select id="grade" name="grade" class="form-control"  style="width: 150px;">
                           <option value="">选择年级</option>
                        </select>
                        &nbsp; 学期
                        <select id="term" name="term" class="form-control" style="width: 150px;" data-dic="{code:'xq'}">
                             <!--  <option value="">选择学期</option>-->
                        </select>&nbsp; 测试类型
                        <select id="examType" name="examType" class="form-control" style="width: 150px;" data-dic="{code:'kslx'}">
                            <!--  <option value="">选测试类型</option>-->
                        </select>
                    </div>
                </div>
                 <div class="multiselect" style="margin: 10px 10px 10px 0px;">
                    	<p>
                    	&nbsp;学校
                        <select id="school" name="school" class="form-control" multiple="multiple"  style="width:340px;height: 25px;">
                           <!--  <option value="">选择学校</option>-->
                        </select>
                        
                        <input type="text" id="examNumberOrStuCode" name="examNumberOrStuCode" placeholder="输入考号或学籍号" style="margin-left:5px;height:19px;"/>
                        <span class="toolbar">
							<button id="fastSearch_instructor" title="查询" style="margin-left: 0px;">
								<i class="fa fa-search"></i>查询
							</button>
							<button id="deriveStuInfo" onclick="exportExcel()">
								<i class="fa fa-paperclip"></i>导出
							</button>
						</span>
						<span class="toolbar">
							<button id="print" title="打印" style="margin-left: 0px;" onClick="getPrint();">
								<i class="fa fa-print"></i>打印预览
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