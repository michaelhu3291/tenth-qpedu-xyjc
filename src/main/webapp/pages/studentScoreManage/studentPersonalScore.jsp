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
    $(function() {
    	
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
             	multiple : true,
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
     
     $("#chooseFunction").change(function() {
    	 var chooseFunction = $("#chooseFunction").val();
	   	if(chooseFunction=="1"){
	   		$("#functionSpan").show();
	   		$("#import").show();
	   	}else{
	   		$("#functionSpan").hide();
	   		$("#import").hide();
	   	}
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
        
        
       
    });
    
    function fastSearch(){
    	$("#scoreTable").empty();
    	$("#div4").empty();
    	$("#scoreTable").height(0);
        var schoolYear = $("#schoolYear").find("option:selected").val();
        var term = $("#term").find("option:selected").val();
        var examType = $("#examType").find("option:selected").val();
        var course = [];
        var courseName = [];
        
        $("#course").find("option:selected").each(function() {
        	course.push($(this).val());
        	courseName.push($(this).text());
		});
        var chooseFunction = $("#chooseFunction").val();
        if(chooseFunction=="1"){
            if (term == ""){ 
               window.Msg.alert("请选择学期");
                return;
            }
            if (examType == ""){
                window.Msg.alert("请选择测试类型");
                return;
            }
            if (course.length<1){
                window.Msg.alert("请选择科目");
                return;
            }
            var url = "../studentScoreManage/studentPersonalScore.do?command=searchScore";
            var data2 = {
            		course : course,
            		courseName : courseName,
            		schoolYear : schoolYear,
            		term : term,
            		examType : examType
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
                	if(data1.newList.length<1){
                		window.Msg.alert("暂无数据！");
                        return;
                	}else{
                		var tableContent="";
                		tableContent+="<table class='inputtable'><thead><tr><th>序号</th><th>学年</th><th>学期</th><th>测试类型</th><th>科目</th><th>等第</th></tr></thead><tbody>";
                		for(var i=0;i<data1.newList.length;i++){
                			tableContent+="<tr><td>"+(i+1)+"</td><td>"+data1.newList[i].schoolYear+"</td><td>"+data1.newList[i].term+"</td><td>"+data1.newList[i].examType+"</td><td>"+data1.newList[i].course+"</td><td>"+data1.newList[i].totalScore+"</td></tr>"
                		}
                		$("#scoreTable").height(27*(data1.newList.length+3));
                		tableContent+="</tbody></table>";
                		$("#scoreTable").append(tableContent);
                	}
                }
              });
	   	}else{
	   		var url = "../studentScoreManage/studentPersonalScore.do?command=searchScoretrend";
            var data2 = {
            		course : course,
            		courseName : courseName
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
                	if(data1.newList.length>0){
                		var o=0;
                		for(var i=0;i<data1.newList.length;i++){
                			$("#div4").append("<div id=chartDiv"+o+" style='width: 50%; height: 500px;float:left;'></div>")
                    	    var myChart1 = echarts.init(document.getElementById("chartDiv"+o), "green");
                    		var zf = data1.newList[i].zf;
                    		var splitNumber=parseInt(data1.newList[i].zf)/5;
                    		var course = data1.newList[i].course;
                			option = {
                    			    title : {
                    			        text: data1.newList[i].course+'分数走势'
                    			    },
                    			    tooltip : {
                    			    	formatter: function (params, ticket, callback) {  
                           		            /* var showContent=""
                           		        	for(var i=0;i<params.length;i++){
                           		        		showContent+=params[i].seriesName+" "+params[i].data+"%"+"</br>";
                           		            }
                           		            return showContent; */
                           		         	console.log(params);
                           		         	var showContent="";
	                           		        if (params.value>=splitNumber*4) {return params.name+"   "+"<br>"+'A'}  
	             			                else if (params.value>=splitNumber*3) {return params.name+"   "+"<br>"+'B'}
	             			                else if (params.value>=splitNumber*2) {return params.name+"   "+"<br>"+'C'} 
	             			                else if (params.value>=splitNumber*1) {return params.name+"   "+"<br>"+'D'} 
	             			                else if (params.value>=0) {return params.name+"   "+"<br>"+'E'}
                           		            
                           		        } 
                    			    },
                    			    grid:{
                                        x2:80,
                                        y2:100
                                    },
                    			    xAxis : [
                    			        {
                    			            type : 'category',
                    			            data : data1.examNameList,
                    			            axisLabel: {        
       	                                     show: true,
       	                                     interval:0,
       	                                     textStyle: {
       	                                         color: '#555',
       	                                         fontSize:'8'
       	                                       	
       	                                     },
       	                                     rotate : '-40'
                  		                  }
                    			        }
                    			    ],
                    			    yAxis : [
                    			        {
                    			            type : 'value',
                    			            max : zf,
                    			            splitNumber:splitNumber,
                    			            axisLabel:{  
                    			            formatter : function(v) {  
                    			                if (v==splitNumber*4) {return 'A'}  
                    			                else if (v==splitNumber*3) {return 'B'}
                    			                else if (v==splitNumber*2) {return 'C'} 
                    			                else if (v==splitNumber*1) {return 'D'} 
                    			                else if (v==0) {return 'E'}     
                    			                 }  
                    			            },  
                    			        }
                    			    ],
                    			    series : [
                    			        {
                    			            name:'分数',
                    			            type:'line',
                    			            data:data1.newList[i].scoreList
                    			        }
                    			    ]
                    			};
                    		myChart1.setOption(option);
                    		o++;
                		}
                		
                		
                	}
                }
              });
	   	}
        
    };
    window.onload = function() {
        $("#schoolType option[value != '']").remove();
        $("#term option[value != '']").remove();
        $("#examType option[value != '']").remove();

    }
    function importExcel(){
    	var schoolYear = $("#schoolYear").find("option:selected").val();
        var term = $("#term").find("option:selected").val();
        var examType = $("#examType").find("option:selected").val();
        var course = [];
        var courseName = [];
        
        $("#course").find("option:selected").each(function() {
        	course.push($(this).val());
        	courseName.push($(this).text());
		});
        var chooseFunction = $("#chooseFunction").val();
        if(chooseFunction=="1"){
            if (term == ""){ 
               window.Msg.alert("请选择学期");
                return;
            }
            if (examType == ""){
                window.Msg.alert("请选择测试类型");
                return;
            }
            if (course.length<1){
                window.Msg.alert("请选择科目");
                return;
            }
            var url = "../studentScoreManage/studentPersonalScore.do?command=searchScore";
            var data = {
            		course : course,
            		courseName : courseName,
            		schoolYear : schoolYear,
            		term : term,
            		examType : examType,
            		state:"1"
            	};
			var form = $( "#fileReteExportExcel" ) ;
			var paramValue=JSON.stringify(data);
			$("#paramData").val(paramValue);
			form.attr( "action", url) ;
			form.get( 0 ).submit() ; 
    }
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
			           功能选择 <select id="chooseFunction" name="chooseFunction" class="form-control" style="width: 100px;">
			           	<option value="1">成绩查询</option>
			           	<option value="2">走势查询</option>
			           </select>&nbsp;
			      <span id="functionSpan">      
			            学年 <select id="schoolYear" name="schoolYear" class="form-control" data-dic="{code:'ND'}" style="width: 100px;"></select>&nbsp; 
			            学期 <select id="term" name="term" class="form-control" style="width: 100px;" data-dic="{code:'xq'}">
			                <option value="">
			                    	选择学期
			                </option>
			            </select> &nbsp; 
			      测试类型 <select id="examType" name="examType" class="form-control" style="width: 100px;" data-dic="{code:'kslx'}">
			                <option value="">
			                   	 选测试类型
			                </option>
			            </select>
			      </span>      
		            科目 <select id="course" name="course" class="form-control"  multiple="multiple" ></select>&nbsp; 
		          
		            <span class="toolbar">
		                <button onclick="fastSearch()" title="分析" style="margin-left: 0px;">
		                    <i class="fa fa-search"></i>查询
		                </button>
		                <button onclick="importExcel()" title="导出" id="import" style="margin-left: 0px;">
								<i class="fa fa-search"></i>导出
						</button>
		            </span>
			        </p>
			    </div>
			    
			</div>
        </div>
        <table id="list2"></table>
		<div id="pager2"></div>
    </div>
    <div id="waittingDiv" style="position:absolute;width:100px;height:100px;top:100px;left:430px"></div>
    <div class="page-content"  style="position:absolute;width:100%;top:60px;left:0px;background-color:transparent;overflow-y:auto;overflow-x:hidden;">
		<div id="scoreTable"  style="height:0px;overflow-y:auto;"></div>
		<div id="div4" style="height:500px;width:100%;"></div>
	 </div>	
</body>
</html>