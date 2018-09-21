
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title>学校管理员--位次查询页面</title>
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
        listUrl = "../scoreManage/overallSearch.do?command=overallSearchForAdmin";
        var courseTxt = [];
        var courseVal = [];
        var examName = [];
        var examNumber = [];
        var idx="",
    	ci="",
    	so="";
        
        var getPrint=function(){
        	if($("#grade").val() === ""){
    			 window.Msg.alert("请选择年级");
    			 return;
    		 }
        	if($("#examNumber").val() === ""){
   			 window.Msg.alert("请选择测试名称");
   			 return;
   		 }
        	if(courseVal !=""){
        		var schoolYear=$("#schoolYear").val();
            	var grade=$("#grade").val();
            	var term=$("#term").val();
            	var examType=$("#examType").val();
            	var school=$("#school").val();
            	var examNumberOrStuCode=$("#examNumberOrStuCode").val();
            	var examNum=$("#examNumber").val();
        		var termTxt = $("#term").find("option:selected").text();
        		var examTypeTxt = $("#examType").find("option:selected").text();
        		var gradeTxt = $("#grade").find("option:selected").text();
            	if(grade != ""){
       			 scoreHtml = schoolYear + "学年" + gradeTxt + termTxt + examTypeTxt + "测试成绩列表";  
       		 } else {
       			 scoreHtml = schoolYear + "学年" + termTxt + examTypeTxt + "测试成绩列表";
       		 }
            	if(school==null){
            		var printUrl="../scoreManage/orderSearch.do?command=printLookForAdmin&schoolYear="+ schoolYear + "&grade="+grade+"&term="+term+"&examType="+examType+"&examNumberOrStuCode="+examNumberOrStuCode+"&courseVal="+courseVal+"&courseTxt="+courseTxt+"&idx="+idx+"&so="+so+"&examNum="+examNum+"&scoreHtml="+scoreHtml+"&gradeTxt="+gradeTxt;
            	}else{
            		var printUrl="../scoreManage/orderSearch.do?command=printLookForAdmin&schoolYear="+ schoolYear + "&grade="+grade+"&term="+term+"&examType="+examType+"&school="+school+"&examNumberOrStuCode="+examNumberOrStuCode+"&courseVal="+courseVal+"&courseTxt="+courseTxt+"&idx="+idx+"&so="+so+"&examNum="+examNum+"&scoreHtml="+scoreHtml+"&gradeTxt="+gradeTxt;
            	}
            	window.open(printUrl);
        	}else{
        		window.Msg.alert("没有数据，无法打印预览");
    			 return;
        	}
        	
        }    
    $(function () {
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
            
          //$("#term").find("option[value = 'xxq']").attr("selected","selected");
          //$("#examType").find("option[value = 'qm']").attr("selected","selected");
      
        });
        $("#examName").change(function(){
        	var examCode=$("#examName").val();
        	$("#examNumber").val(examCode);
        	if(examCode !=""){
        		$("#course option[value != '']").remove();
            	var url = "../statisticsAnalysis/scoreSearch.do?command=getCourseByExamNumber";
            	var data = {examCode:examCode};
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
        	
        	
        });
       	var aaa=function(){
       	//$("#course").find("option").attr("value").remove();
          	 $("#course option[value != '']").remove();
          	$("#examName option[value != '']").remove();
          	var schoolYear=$("#schoolYear").val();
          	var grade = $("#grade").val();
          	var term=$("#term").val();
          	var examType=$("#examType").val();
          	 var url = "../statisticsAnalysis/scoreSearch.do?command=getExamNameByCode";
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
                    examName = [];
                    examNumber = [];
                    if(data.length > 0) {
                   	 for(var i = 0;i < data.length;i++){
                   		 
                   		 $("#examName").append("<option value='" + data[i].Exam_Number + "'>" + data[i].Exam_Name + "</option>");
                   		 /* courseTxt.push(data[i].DictionaryName);
                   		 courseVal.push(data[i].DictionaryCode); */
                  		 }
                   	 //$("#gradeHidden").val(courseVal);
                    }	
                  	 
                   }
               });
          	 }
       	}
      	/*选择年级关联科目(jqgrid头动态显示科目)*/
        $("#grade").change(function(){
        	$("#examNumber").val("");
        	$("#examNumberOrStuCode").val("");
       	 	aaa();
        });
        $("#schoolYear").change(function(){
        	$("#examNumber").val("");
        	$("#examNumberOrStuCode").val("");
       	 	aaa();
        });
        $("#term").change(function(){
        	$("#examNumber").val("");
        	$("#examNumberOrStuCode").val("");
       	 	aaa();
        });
        $("#examType").change(function(){
        	$("#examNumber").val("");
        	$("#examNumberOrStuCode").val("");
       	 	aaa();
        });
        $("#examNumber").change(function(){
        	$("#examNumberOrStuCode").val("");
       	 	aaa();
        });
        $("#examNumberOrStuCode").change(function(){
       	 	aaa();
        });
        
      //学校类型加载年级
      $.ajax({
             url: "../statisticsAnalysis/schoolPlainAdminScoreSearch.do?command=getSchoolCodeAndSchoolName",
             type: "POST",
             data: {},
             dataType: "JSON",
             success: function (data) {
             	    //$("#course option[value != '']").remove();
             	    $("#grade option[value != '']").remove();
             	    //$("#schoolType option[value != '']").remove();
             		//学校名称和学校code
                     //$("#school").append("<option value='" + data.map.School_Code + "'>" + data.map.School_Name + "</option>");
                   	//学校类型
                     //$("#schoolType").append("<option value='" + data.paramMap.schoolTypeCode + "'>" + data.paramMap.schoolTypeName + "</option>");
                   	//科目
                   	var dataList = data.paramList;
                     for (var i = 0; i < dataList.length; i++) {
                         $("#grade").append("<option value='" + dataList[i].DictionaryCode + "'>" + dataList[i].DictionaryName + "</option>");
                     }
             }
      });

        //选择学校类型关联年级
        /*$("#schoolType").change(function () {
            $("#grade option[value != '']").remove();
            var schoolType = $("#schoolType").val();
            if (schoolType != "") {
                var url = "../platform/dictionary.do?command=getGradessByCode";
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
                            $("#grade").append("<option value='" + data[i].DictionaryCode + "'>" + data[i].DictionaryName + "</option>");
                        }
                    }
                });
            }
        });*/
        $("#tabs-2,#tabs-1").css("height", "auto");
        _initFormControls(); //from page.common.js
        _initValidateForXTypeForm(editorFormId);
        
		$("#fastSearch_schoolAdmin").click(function() {
			if($("#grade").val() === ""){
				 window.Msg.alert("请选择年级");
				 return;
			 }
			if($("#examNumber").val() === ""){
				 window.Msg.alert("请选择测试名称");
				 return;
			 }
			/* if($("#examNumber").val() === ""){
				 window.Msg.alert("请选择测试名称");
				 return;
			 } */
			 //成绩信息描述
			 var scoreHtml;
			 var schoolYear = $("#schoolYear").find("option:selected").text();
			 //var schoolType = $("#schoolType").find("option:selected").text();
			 var term = $("#term").find("option:selected").text();
			 var examType = $("#examType").find("option:selected").text();
			 var gradeVal = $("#grade").find("option:selected").val();
			 var gradeTxt = $("#grade").find("option:selected").text();
			 var _colModel = [{
				 label:"考号",
	             name: 'Exam_Number',
	             sortable: false,
	             width: 90,
	             align: "center",

	         },{
	        	 label:"学籍号",
	             name: 'XJFH',
	             sortable: false,
	             width: 150,
	             align: "center",

	         },{
	        	 label:"姓名",
	             name: 'Name',
	             sortable: false,
	             width: 70,
	             align: "center",

	         },{
	        	 label:"年级",
	             name: 'Grade_Id',
	             sortable: false,
	             width: 70,
	             align: "center",
	             formatter : function(ar1, ar2, ar3) {
						/*if (ar3.className != "" || ar3.Class_Id != null) {
							var classId=ar3.Class_Id;
							if(classId.substring(0,1)=="0"){
								classId=classId.substring(classId.length-1);
							}
							return (_types[ar3.Grade_Id] + "(" + classId
									+ ")班" || "");
						} else {
							return (_types[ar3.Grade_Id]);
						}*/
						return (_types[ar3.Grade_Id] || "");
					}

	         }, {
	        	 label:"班级",
	             name: 'Class_Id',
	             sortable: false,
	             width: 50,
	             align: "center",
	             formatter : function(ar1, ar2, ar3) {
						/*if (ar3.className != "" || ar3.Class_Id != null) {
							var classId=ar3.Class_Id;
 							if(classId.substring(0,1)=="0"){
 								classId=classId.substring(classId.length-1);
 							}
 							return (_types[ar3.Grade_Id] + "(" + classId
 									+ ")班" || "");
						} else {
							return (_types[ar3.Grade_Id]);
						}*/
						return (_typesClass[ar3.Class_Id] || "");
					}
	         }];
			 //声明对象
				function Object(label, name, width,
						align,autoWidth,sortable) {
					this.label = label;
					this.name = name;
					this.width = width;
					this.align = align;
					this.autoWidth = autoWidth;
					this.sortable = sortable;
				}

				
				for (var a = 0; a < courseTxt.length
						&& a < courseVal.length; a++) {
					//追加列表信息
					_colModel.push(new Object(courseTxt[a],courseVal[a], 40, "center",true,true));
				}
	            
				
				_colModel.push(new Object("年级名次", "orderSchool", 60, "center",false,"false"));
			    _colModel.push(new Object("班级名次", "orderClass", 60, "center",false,"false"));
			    
	         //_colNames = ['考号','学籍号','姓名','年级','班级','语文','数学','外语','物理','化学','总分'];
			 if(gradeVal != ""){
				 scoreHtml = schoolYear + "学年" + gradeTxt + term + examType + "测试成绩列表";  
			 } else {
				 scoreHtml = schoolYear + "学年" + term + examType + "测试成绩列表";
			 }
			  
			//重新创建列表
			$(listId).GridUnload();	 
	     $(listId).jqGrid($.extend(defaultGridOpts, {
	         url: listUrl,
	         colModel: _colModel,
	         pager: pagerId,
	         caption : scoreHtml,
	         rowNum: 20,
		     rowList: [z20, 30, 50, 100],
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
		});
    });
    window.onload = function () {
    	//$("#classs option[value != '']").remove();
        $("#term option[value != '']").remove();
        $("#examType option[value != '']").remove();
    }
    
  	/*导出成绩查询数据*/
    var exportExcel = function(){
    	if($("#grade").val() === ""){
			 window.Msg.alert("请选择年级");
			 return;
		 }
    	if($("#examNumber").val() == ""){
			 window.Msg.alert("请选择测试名称");
			 return;
		 }
    	var scoreHtml;
    	var schoolYear = $("#schoolYear").find("option:selected").text();
		//var schoolType = $("#schoolType").find("option:selected").val();
		var term = $("#term").find("option:selected").val();
		var termTxt = $("#term").find("option:selected").text();
		var examType = $("#examType").find("option:selected").val();
		var examTypeTxt = $("#examType").find("option:selected").text();
		var grade = $("#grade").find("option:selected").val();
		var gradeTxt = $("#grade").find("option:selected").text();
		var examNumberOrStuCode = $("#examNumberOrStuCode").val();
		
		 if(grade != ""){
			 scoreHtml = schoolYear + "学年" + gradeTxt + termTxt + examTypeTxt + "测试成绩列表";  
		 } else {
			 scoreHtml = schoolYear + "学年" + termTxt + examTypeTxt + "测试成绩列表";
		 }
		//_search=false, nd=1478237272386, rows=15, page=1, sidx=, sord=asc, schoolYear=2016-2017, grade=[16], class=[16,01], course=yw, term=sxq, examType=qz, examNumberOrStuCode=, isFast=false, q=, o=
	var data={
			schoolYear:schoolYear,
			term:term,
			examType:examType,
			grade:grade,
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
                                                                        学年
                        <select id="schoolYear" name="schoolYear" class="form-control" data-dic="{code:'ND'}" style="width: 130px;"></select>
                        &nbsp; 年级
                        <select id="grade" name="grade"  class="form-control" style="width: 130px;">
                        	<option value="">请选择年级</option>
                        </select>
                        &nbsp; 学期
                        <select id="term" name="term" class="form-control" style="width: 130px;" data-dic="{code:'xq'}">
                        	<!--  <option value="">请选择学期</option>-->
                        </select>
                       	&nbsp;测试类型
                      	<select id="examType" name="examType" class="form-control" style="width: 130px;" data-dic="{code:'kslx'}">
                      		<!--<option value="">请选择类型</option>-->
                        </select>
                      </div><br/>

                                                                        测试名称
                      	<select id="examName" name="examName" class="form-control" style="width: 260px;">
                      		<option value="">请选择测试名称</option>
                        </select>
                     
                        &nbsp;测试编号
                      	<input type="text" id="examNumber" name="examNumber" style="margin-left:5px;height:19px;" readonly="readonly"/>
                        <input type="text" id="examNumberOrStuCode" name="examNumberOrStuCode" placeholder="输入考号或学籍号" style="margin-left:5px;height:19px;"/>
                     <!--&nbsp;年级
                        <select id="grade" name="grade" class="form-control" style="width: 180px;">
                        </select>-->
                        	  <!--科目
                        	<select id="course" name="course" class="form-control" style="width: 180px;"></select>-->
	                        <!--&nbsp;学校
	                        <select id="school" name="school" style="width: 200px;" class="form-control"></select>-->
	                        <span class="toolbar">
		                      <button id="fastSearch_schoolAdmin" title="查询" style="margin-left: 0px;">
										<i class="fa fa-search"></i>查询
							  </button>
							</span>
							<span class="toolbar">
							  <button id="deriveStuInfo" onclick="exportExcel()">
									<i class="fa fa-paperclip"></i>导出
							  </button>
							  </span>
							  <span class="toolbar">
							<button id="print" title="打印" style="margin-left: 0px;" onClick="getPrint();">
								<i class="fa fa-print"></i>打印预览
							</button>
						</span>
							</span>
							
						      
                  </div>
                
            </div>
        </div>
        <table id="list2"></table>
        <div id="pager2"></div>
    </div>
</body>
</html>