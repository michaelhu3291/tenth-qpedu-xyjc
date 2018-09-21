<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="../theme/default/master.css" rel="stylesheet" />
<link href="../theme/default/ui.custom.css" rel="stylesheet" />
<link href="../theme/default/ui.jqgrid.css" rel="stylesheet" />
<link href="../theme/default/font.awesome.css" rel="stylesheet" />
<link href="../theme/default/ui.chosen.css" rel="stylesheet" />
<link href="../theme/default/ui.uploadfile.css" rel="stylesheet" />
<link href="../theme/default/ui.pick.css" rel="stylesheet" />
<link href="../theme/default/page.common.css" rel="stylesheet" />
<link href="../theme/default/jquery.multiselect.css" rel="stylesheet" />
<link href="../theme/default/jquery.edittable.min.css" rel="stylesheet" />
<link href="../theme/default/jquery.multiselect.filter.css"
    rel="stylesheet" />
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
<script type="text/javascript" src="../js/echarts/echarts.js"></script>
<script type="text/javascript" src="../js/echarts/theme/green.js"></script>


<style>
.page-list-panel {
    height: auto;
    width: 100%;
    background-color: #e7fcd9;
    position: absolute;
    z-index: 1;
}
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

.ui-state-default em {
    font-size: 16px;
    color: white;
}
table.inputtable td, table.inputtable th {
    border: 1px solid #eee;
    text-align: center;
    height: 10px;
    width: 120px;
    vertical-align: middle;
    font-size: 12px;
    padding: 5px;
}
</style>

<script type="text/javascript">
    var listId = "#list2", editorFormId = "#editorForm", pagerId = '#pager2', editorRelatedFormId = "#editorRelatedForm", listUrl = "../statisticsAnalysis/scoreAnalysis.do?command=searchScore";
    $(function() {
    	$.ajax({
            url: "../statisticsAnalysis/schoolPlainAdminScoreSearch.do?command=getSchoolCodeAndSchoolName",
            type: "POST",
            data: {},
            dataType: "JSON",
            success: function (data) {
           	    $("#grade option[value != '']").remove();
                 	var dataList = data.paramList;
                    for (var i = 0; i < dataList.length; i++) {
                        $("#grade").append("<option value='" + dataList[i].DictionaryCode + "'>" + dataList[i].DictionaryName + "</option>");
                    }
           		}
     	});
     $.ajax({
         url: "../statisticsAnalysis/politicalInstructor.do?command=getCoursesBySchoolType",
         type: "POST",
         data: {},
         dataType: "JSON",
         success: function (data) {
             for (var i = 0; i < data.length; i++) {
                 $("#course").append("<option value='" + data[i].DictionaryCode + "'>" + data[i].DictionaryName + "</option>");
             }
             $("#course").multiselect({
             	multiple : false,
             	checkAllText : "全选",
     			uncheckAllText : "全不选",
     			noneSelectedText : "选择科目",
     			selectedText : '#' + " 科目",
     			selectedList : 2
             }).multiselectfilter({
                 label : "科目名称",
                 placeholder : "请输入科目名"
             });
             $("#course").multiselect('refresh');
         }
     });
        $("#class").multiselect({
            checkAllText : "全选",
            uncheckAllText : "全不选",
            noneSelectedText : "选择班级",
            selectedText : '#' + " 个班级",
            selectedList : 2
        }).multiselectfilter({
            label : "班级名称",
            placeholder : "请输入班名"
        });
		
        _initButtons({});
        $("#tblInfo").find("button").button();
        $("#tabs").tabs({
            heightStyle : "fill"
        });
        $("#tabs-2,#tabs-1").css("height", "auto");
        _initFormControls();
        _initValidateForXTypeForm(editorFormId);

        loadDictionary(function() {
            var year = new Date().getFullYear();
            var month = new Date().getMonth() + 1;
            var currentYear = "";
            if (month < 9) {
                currentYear = (year - 1) + "-" + year;
            } else {
                currentYear = year + "-" + (year + 1);
            }
            $("#schoolYear").val(currentYear);
        });
        
        $("#chooseFunction").change(function() {
            if($("#chooseFunction").val()==1){
            	$("#course").multiselect({
    				multiple : false,
    	        	checkAllText : "全选",
    				uncheckAllText : "全不选",
    				noneSelectedText : "选择科目",
    				selectedText : '#' + " 科目",
    				selectedList : 2
    			});
            }else{
            	$("#course").multiselect({
    				multiple : true,
    	        	checkAllText : "全选",
    				uncheckAllText : "全不选",
    				noneSelectedText : "选择科目",
    				selectedText : '#' + " 科目",
    				selectedList : 2
    			});
            }
    		$("#course").multiselect('refresh');
        });
       $("#grade").change(function() {
   	   		  $("#class").empty();
       	      var grade = $("#grade").val();
       	      if (grade != "") {
       	        var url = "../schoolQualityAnalysis/excellentRate.do?command=getClassByGrade";
       	        var data = {grade : grade};
       	        $.ajax({
       	              url : url,
       	              type : "POST",
       	              data : data,
       	              dataType : "JSON",
       	              success : function(data) {
       	            	  if(data.classList.length>0){
       	            		  var str="";
       	            		  for(var i=0;i<data.classList.length;i++){
       	            			  str+="<option value="+data.classList[i].classId+">"+data.classList[i].className+"</option>";
       	            		  }
       	            		  $("#class").append(str);
       	            		for (var i = 0; i < data.classList.length; i++) {
								$("#class").find("option[value='"+ data.classList[i].classId+ "']").attr("selected","selected");
							}
       	            		$("#class").multiselect('refresh');
       	            	  }
       	              }
       	            });
       	      }
       	});
    });
    
    function fastSearch(){
        $("#scoreTable").empty();
        $("#chartDiv1").empty();
        $("#chartDiv2").empty();
        $("#scoreTable").height(0);
        $("#chartDiv1").height(0);
        $("#chartDiv2").height(0);
        var schoolYear = $("#schoolYear").find("option:selected").val();
        var term = $("#term").find("option:selected").val();
        var examType = $("#examType").find("option:selected").val();
        var grade = $("#grade").find("option:selected").val();
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
        var classCode = [];
        var className = [];
        $("#class").find("option:selected").each(function() {
        	classCode.push($(this).val());
        	className.push($(this).text());
		});
        var course = [];
        var courseName = [];
        $("#course").find("option:selected").each(function() {
        	course.push($(this).val());
        	courseName.push($(this).text());
		});
        
        if (term == ""){ 
           window.Msg.alert("请选择学期");
            return;
        }
        if (examType == ""){
            window.Msg.alert("请选择测试类型");
            return;
        }
        if (grade == ""){
            window.Msg.alert("请选择年级");
            return;
        }
        if (classCode.length<1){
           window.Msg.alert("请选择班级");
            return;
        }
        if (course.length<1){
            window.Msg.alert("请选择科目");
            return;
        }
        var url = "../schoolQualityAnalysis/schoolSegment.do?command=analyzeSegment";
        var data2 = {
        		course : course,
        		courseName : courseName,
        		schoolYear : schoolYear,
        		schoolType : schoolType,
        		term : term,
        		examType : examType,
        		grade : grade,
        		classCode : classCode,
        		className : className,
        		chooseFunction : $("#chooseFunction").val()
        	};
        $.ajax({
            url : url,
            type : "POST",
            data : data2,
            dataType : "JSON",
            beforeSend: function () {
            	$("#waittingDiv").append("<img src='../theme/images/loading.gif'/>");
            },
            success : function(data1) {
            	$("#waittingDiv").empty();
            	if(data1.message=="faile"){
            		window.Msg.alert("暂无数据！");
                    return;
            	}else{
            		var tableContent="";
                	var secondTh="<tr>";
                	var classList = [];
                	var seriesData =[];
                	var barList=[];
                	tableContent+="<table class='inputtable'><tbody><tr><th rowspan='2'>班级</th><th rowspan='2'>实考人数</th>"
                	for(var i=0;i<data1.titleList.length;i++){
                		var tempbar={name:data1.titleList[i]};
                		tempbar.data=[];
                		tempbar.type='bar';
                		tempbar.stack='总量';
                		tempbar.itemStyle={ normal: {label : {show: true, position: 'insideRight',formatter:'{c} %'}}};
                		tempbar.barMaxWidth=60;
                		barList.push(tempbar);
                		tableContent+="<th colspan='4'>"+data1.titleList[i]+"</th>";
                		secondTh+="<th>人数</th><th>百分比</th><th>累计人数</th><th>累计百分比</th>";
                	}
                	secondTh+="</tr>";
                	var bodyContent="";
                	for(var i=0;i<data1.inforamtionList.length;i++){
                		var detail = data1.inforamtionList[i];
                		classList.push(detail.className);
                		var tempSeriesData={name:detail.className};
                		tempSeriesData.type='line';
                		
                		var list = new Array();
                		bodyContent+="<tr><td>"+detail.className+"</td><td>"+detail.ksrs+"</td>";
                		for(var j=0;j<detail.tempList.length;j++){
                			list.push(detail.tempList[j].bfbNoChar);
                			bodyContent+="<td>"+detail.tempList[j].rs+"</td><td>"+detail.tempList[j].bfb+"</td><td>"+detail.tempList[j].ljrs+"</td><td>"+detail.tempList[j].ljbfb+"</td>";
                			for(var k=0;k<barList.length;k++){
                				if(detail.tempList[j].range==barList[k].name){
                					barList[k].data.push(detail.tempList[j].bfbNoChar);
                				}
                			}
                		}
                		tempSeriesData.data=list;
                		seriesData.push(tempSeriesData);
                		bodyContent+="</tr>";
                	}
                	$("#scoreTable").height(84+27*(data1.inforamtionList.length+2));
                	var countMap = data1.countMap;
                	var count="<tr><td>小计</td><td>"+countMap.ksrs+"</td>";
                	for(var i=0;i<countMap.tempList.length;i++){
                		count+="<td>"+countMap.tempList[i].rs+"</td><td>"+countMap.tempList[i].bfb+"</td><td>"+countMap.tempList[i].ljrs+"</td><td>"+countMap.tempList[i].ljbfb+"</td>";
                	}
                	tableContent+="</tr>"+secondTh+bodyContent+count;
                	$("#scoreTable").append(tableContent);
                	
                	option1 = {
                		    title : {
                		        text: '分数段——变化线形图'
                		    },
                		    tooltip : {
                		        trigger: 'axis'
                		    },
                		    legend: {
                		        data:classList,
                		        x: 'right',
                		        orient: 'vertical'
                		    },
                		    toolbox: {
                		        show : true,
                		        feature : {
                		            saveAsImage : {show: true}
                		        }
                		    },
                		    grid:{
                                x:50,
                                y:80,
                                x2:250,
                                y2:80
                            },
                		    calculable : true,
                		    xAxis : [
                		        {
                		            type : 'category',
                		            boundaryGap : false,
                		            data : data1.titleList
                		        }
                		    ],
                		    yAxis : [
                		        {
                		            type : 'value',
                		            
                		            axisLabel : {
                		                formatter: '{value} %'
                		            }
                		        }
                		    ],
                		    series :seriesData
                		};
                	
                		barList.reverse(); 
                		option2 = {
                				title : {
                    		        text: '分数段——堆积条形图'
                    		    },
                    		    tooltip : {
                    		        trigger: 'axis',
                    		        
                    		        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                    		            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                    		        },
                    		        textStyle: {
                    		        	fontSize: 8,
                    		        	},
                    		    },
                    		    legend: {
                    		    	data:data1.titleList,
                    		    	x: 'right',
                    		    	zlevel:1,
                    		        orient: 'vertical'
                    		    },
                    		    grid:{
                                    x:50,
                                    y:80,
                                    x2:250,
                                    y2:80
                                },
                    		    toolbox: {
                    		        show : true,
                    		        feature : {
                    		            saveAsImage : {show: true}
                    		        }
                    		    },
                    		    calculable : true,
                    		    xAxis : [
                    		             {
                    		                 
                    		              	type : 'category',
                    		                 data : classList,
                    		                 axisLabel: {        
        	                                     show: true,
        	                                     interval:0,
        	                                     textStyle: {
        	                                         color: '#555',
        	                                         fontSize:'10'
        	                                       	
        	                                     },
        	                                     rotate : '-20'
                    		                  }
                    		             },
                    		           
                    		         ],
                    		    yAxis : [
                    		       {
                    		            type : 'value',
                    		            max:100
                    		        }
                    		    ],
                    		    series : barList
                    		};
            		$("#chartDiv1").height(600);
                    $("#chartDiv2").height(600);
               		var myChart1 = echarts.init(document.getElementById("chartDiv1"), "green");
                   	myChart1.setOption(option1); 
                	var myChart2 = echarts.init(document.getElementById("chartDiv2"), "green");
                	myChart2.setOption(option2); 
            	}
            }
          });
    };
    window.onload = function() {
        $("#schoolType option[value != '']").remove();
        $("#term option[value != '']").remove();
        $("#examType option[value != '']").remove();

    }
    function importExcel(state){
    	
        var schoolYear = $("#schoolYear").find("option:selected").val();
        var term = $("#term").find("option:selected").val();
        var examType = $("#examType").find("option:selected").val();
        var grade = $("#grade").find("option:selected").val();
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
        var classCode = [];
        var className = [];
        $("#class").find("option:selected").each(function() {
        	classCode.push($(this).val());
        	className.push($(this).text());
		});
        var course = [];
        var courseName = [];
        $("#course").find("option:selected").each(function() {
        	course.push($(this).val());
        	courseName.push($(this).text());
		});
        
        if (term == ""){ 
           window.Msg.alert("请选择学期");
            return;
        }
        if (examType == ""){
            window.Msg.alert("请选择测试类型");
            return;
        }
        if (grade == ""){
            window.Msg.alert("请选择年级");
            return;
        }
        if (classCode.length<1){
           window.Msg.alert("请选择班级");
            return;
        }
        if (course.length<1){
            window.Msg.alert("请选择科目");
            return;
        }
        var url = "../schoolQualityAnalysis/schoolSegment.do?command=analyzeSegment";
        var data2 = {
        		course : course,
        		courseName : courseName,
        		schoolYear : schoolYear,
        		schoolType : schoolType,
        		term : term,
        		examType : examType,
        		grade : grade,
        		classCode : classCode,
        		className : className,
        		chooseFunction : $("#chooseFunction").val()
        	};
        $.ajax({
            url : url,
            type : "POST",
            data : data2,
            dataType : "JSON",
            beforeSend: function () {
            	$("#waittingDiv").append("<img src='../theme/images/loading.gif'/>");
            },
            success : function(data1) {
            	$("#waittingDiv").empty();
            	if(data1.message=="faile"){
            		window.Msg.alert("暂无数据！");
                    return;
            	}else{
            		var url1="../schoolQualityAnalysis/schoolSegment.do?command=importExcel";
         
           		var form = $( "#fileReteExportExcel" ) ;
           		var paramValue=JSON.stringify(data2);
           			$("#paramData").val(paramValue);
           			form.attr( "action", url1) ;
           			form.get( 0 ).submit() ; 
            	}
            }
        })
	    
   }
</script>


</head>
<body>
   <div class="page-list-panel">
   <form id="fileReteExportExcel" method="post">
		<input id="paramData" name="data" type="hidden" value=""/>
	</form>
        <div class="head-panel">
			<div id="importCondition" style="margin: -5px 10px 10px 0px;">
			    <div class="search-panel" style="display: block;">
			        <p>
			            学年 <select id="schoolYear" name="schoolYear" class="form-control" data-dic="{code:'ND'}" style="width: 150px;"></select>&nbsp; 
			          
			            年级 <select id="grade" name="grade" class="form-control" style="width: 150px;">
			                <option value="">
			                   	 选择年级
			                </option>
			           </select>&nbsp; 
			           学期 <select id="term" name="term" class="form-control" style="width: 150px;" data-dic="{code:'xq'}">
			                <option value="">
			                    	选择学期
			                </option>
			            </select> &nbsp; 
			      测试类型 <select id="examType" name="examType" class="form-control" style="width: 150px;" data-dic="{code:'kslx'}">
			                <option value="">
			                   	 选测试类型
			                </option>
			            </select>
			        </p>
			    </div>
			    <div class="multiselect" style="margin: -5px 10px 10px 0px;">
			        <p>
			      功能选择&nbsp;<select id="chooseFunction"  class="form-control" >
			      			<option value="1">单科</option>
			      			<option value="2">总分</option>
			      	  </select>&nbsp;
			            科目 <select id="course" name="course" class="form-control"  multiple="multiple" ></select>&nbsp; 
			            班级<select id="class" name="class" class="form-control"  multiple="multiple" ></select>&nbsp;
			            <span class="toolbar">
			                <button onclick="fastSearch()" title="分析" style="margin-left: 0px;">
			                    <i class="fa fa-search"></i>分析
			                </button>
			                <button onclick="importExcel('2')" title="导出" style="margin-left: 0px;">
								<i class="fa fa-search"></i>导出
							</button>
			            </span>
			        </p>
			    </div>
			</div>
        </div>
    </div>
    <div id="waittingDiv" style="position:absolute;width:100px;height:100px;top:100px;left:430px"></div>
    <div class="page-content"  style="position:absolute;width:100%;top:80px;left:0px;background-color:transparent;overflow-y:auto;overflow-x:hidden;">
		<div id="scoreTable"  style="height:684px;overflow-x:scroll;overflow-y:hidden;"></div>
		<div class="l_form01" id="chartDiv1" style="width: 95%; height: 0px;" ></div>
		<div class="l_form01" id="chartDiv2" style="width: 95%; height: 0px;" ></div>  
	 </div>	
</body>
</html>