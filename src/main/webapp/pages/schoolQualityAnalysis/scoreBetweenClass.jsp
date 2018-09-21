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
    	//查询老师所带的年级和科目
        $.ajax({
			 url : "../statisticsAnalysis/scoreSearch.do?command=selectGradeForTeacher",
			 type : "POST",
			 data : {},
			 dataType : "JSON",
			 success : function(data) {
			 $("#grade").find("option[value != '']").remove();
			 //$("#class").find("option[value != '']").remove();
			 $("#course").find("option[value != '']").remove();
			 var gradeList = data.gradeList;
			 //var classList = data.classList;
			 var courseList = data.courseList;
			 $.each(courseList,function(index,item){
				 $("#course").append("<option value='"+item.Course_Id+"'>"+ _types[item.Course_Id] + "</option>");
			 });
			 $.each(gradeList,function(index,item){
				 $("#grade").append("<option value='"+item+"'>"+ _types[item] + "</option>");
			 });
			 if($("#grade").find("option").length > 0){
				 //$("#grade").multiselect('refresh');
			 }
		     
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
        
        $("#grade").change(function() {
	   		  $("#class").empty();
	      var grade = $("#grade").val();
	      if (grade != "") {
	        var url = "../schoolQualityAnalysis/excellentRate.do?command=getClassByTeacher";
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
	            			  str+="<option value="+data.classList[i].classPk+">"+data.classList[i].className+"</option>";
	            		  }
	            		  $("#class").append(str);
	            		  for (var i = 0; i < data.classList.length; i++) {
								$("#class").find("option[value='"+ data.classList[i].classPk+ "']").attr("selected","selected");
						  }
	            	  }
	            	  $("#class").multiselect('refresh');
	              }
	            });
	      }
	});
      
        $("#chooseFunction").change(function() {
            if($("#chooseFunction").val()==1){
            	$("#functionChangeSpan").show();
            	$("#import").show();
            }else{
            	$("#functionChangeSpan").hide();
            	$("#import").hide();
            }
    		
        });
    });
    
    
    
    function fastSearch(){
    	$("#div1").empty();
    	$("#div2").empty();
    	$("#div3").empty();
    	$("#div1").height(0);
    	$("#div2").height(0);
    	$("#div3").height(0);
    	$("#waittingDiv").empty();
    	var schoolYear = $("#schoolYear").find("option:selected").val();
    	var term = $("#term").find("option:selected").val();
    	var grade = $("#grade").find("option:selected").val();
        var examType = $("#examType").find("option:selected").val();
        var classCode = [];
        var className = [];
        $("#class").find("option:selected").each(function() {
        	classCode.push($(this).val());
        	className.push($(this).text());
		});
        if (grade == ""){
            window.Msg.alert("请选择年级");
            return;
        }
        if (classCode.length<1){
           window.Msg.alert("请选择班级");
            return;
        }
        if($("#chooseFunction").val()==1){
        	if (term == ""){ 
                window.Msg.alert("请选择学期");
                 return;
             }
             if (examType == ""){
                 window.Msg.alert("请选择测试类型");
                 return;
             }
             var url = "../schoolQualityAnalysis/scoreBetweenClass.do?command=analyzeScoreBetweenClass";
             var Data = {
             		schoolYear : schoolYear,
             		term : term,
             		examType : examType,
             		classCode : classCode,
             		className : className,
             		grade:grade
             	};
             $.ajax({
                 url : url,
                 type : "POST",
                 data : Data,
                 dataType : "JSON",
                 beforeSend: function () {
                 	$("#waittingDiv").append("<img src='../theme/images/loading.gif'/>");
                 },
                 success : function(data1) {
                	 $("#waittingDiv").empty();
                	 if(data1.classesScoreList.length>0){
                		 var tableContent = "<table class='inputtable'><thead><tr><th>序号</th><th>班级</th><th>姓名</th><th>等第</th></tr></thead><tbody>";
                		 for(var i=0;i<data1.classesScoreList.length;i++){
                			 tableContent+="<tr><td>"+(i+1)+"</td><td>"+data1.classesScoreList[i].className+"</td><td>"+data1.classesScoreList[i].name+"</td><td>"+data1.classesScoreList[i].totalScore+"</td></tr>"
                		 }
                		 $("#div1").height(27*(data1.classesScoreList.length+3));
                		 tableContent+="</tbody></table>";
                		 $("#div1").append(tableContent);
                		 
                		var abcdeContent = "<table class='inputtable'><thead><tr><th>班级</th><th>A</th><th>B</th><th>C</th><th>D</th><th>E</th></tr></thead><tbody>";
                		var A = [];
                 		var B = [];
                 		var C = [];
                 		var D = [];
                 		var E = [];
                 		var className = [];
                		 for(var i=0;i<data1.classList.length;i++){
                			 abcdeContent+="<tr><td>"+data1.classList[i].className+"</td><td>"+data1.classList[i].Abfb+"%("+data1.classList[i].A+"人)"+"</td><td>"+data1.classList[i].Bbfb+"%("+data1.classList[i].B+"人)"+"</td><td>"+data1.classList[i].Cbfb+"%("+data1.classList[i].C+"人)"+"</td><td>"+data1.classList[i].Dbfb+"%("+data1.classList[i].D+"人)"+"</td><td>"+data1.classList[i].Ebfb+"%("+data1.classList[i].E+"人)"+"</td></tr>";
                			 A.push(data1.classList[i].Abfb);
                 			 B.push(data1.classList[i].Bbfb);
                 			 C.push(data1.classList[i].Cbfb);
                 			 D.push(data1.classList[i].Dbfb);
                 			 E.push(data1.classList[i].Ebfb);
                 			 className.push(data1.classList[i].className);		
                		 }
                		 abcdeContent+="</tbody></table>";
                		 $("#div2").height(27*(data1.classList.length+3));
                		 $("#div2").append(abcdeContent);
                		 $("#div3").height(684);
                		 var myChart1 = echarts.init(document.getElementById("div3"), "green");
                 		 option = {
                 		    tooltip : {
                 		        trigger: 'axis',
                 		       formatter: function (params, ticket, callback) {  
               		            var showContent=""
               		        	for(var i=0;i<params.length;i++){
               		        		showContent+=params[i].seriesName+" "+params[i].data+"%"+"</br>";
               		            }
               		            return showContent;
               		        }, 
                 		        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                 		            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                 		        }
                 		    },
                 		    legend: {
                 		    	data:['A','B','C','D','E']
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
                 		                 data : className,
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
                 		    series : [
                 		              {
                 		                  name:'E',
                 		                  type:'bar',
                 		                  stack: '总量',
                 		                  itemStyle : { normal: {label : {show: true, position: 'insideRight',formatter:'{c} %'}}},
                 		                  barMaxWidth:60,
                 		                  data:E
                 		              },
                 		              {
                 		                  name:'D',
                 		                  type:'bar',
                 		                  stack: '总量',
                 		                  itemStyle : { normal: {label : {show: true, position: 'insideRight',formatter:'{c} %'}}},
                 		                  barMaxWidth:60,
                 		                  data:D
                 		              },
                 		              {
                 		                  name:'C',
                 		                  type:'bar',
                 		                  stack: '总量',
                 		                  itemStyle : { normal: {label : {show: true, position: 'insideRight',formatter:'{c} %'}}},
                 		                  barMaxWidth:60,
                 		                  data:C
                 		              },
                 		              {
                 		                  name:'B',
                 		                  type:'bar',
                 		                  stack: '总量',
                 		                  itemStyle : { normal: {label : {show: true, position: 'insideRight',formatter:'{c} %'}}},
                 		                  barMaxWidth:60,
                 		                  data:B
                 		              },
                 		              {
                 		                  name:'A',
                 		                  type:'bar',
                 		                  stack: '总量',
                 		                  itemStyle : { normal: {label : {show: true, position: 'insideRight',formatter:'{c} %'}}},
                 		                  barMaxWidth:60,
                 		                  data:A
                 		              }
                 		          ]
                 		};
                 		myChart1.setOption(option);
                	 }else{
                		 window.Msg.alert("暂无数据！");
                         return;
                	 }
                 }
               });
        }else{
        	var url = "../schoolQualityAnalysis/scoreBetweenClass.do?command=analyzeClassTrend";
            var Data = {
            		classCode : classCode,
            		className : className,
            		grade:grade
            	};
            $.ajax({
                url : url,
                type : "POST",
                data : Data,
                dataType : "JSON",
                beforeSend: function () {
                	$("#waittingDiv").append("<img src='../theme/images/loading.gif'/>");
                },
                success : function(data1) {
               	 $("#waittingDiv").empty();
               	$("#div3").height(684);
               	var seriesData =[];
               	for(var key in data1.bigMap){
               		var tempSeriesData={name:key};
               		tempSeriesData.type='line';
               		tempSeriesData.data=data1.bigMap[key];
               		seriesData.push(tempSeriesData);
               	}
               	var zf = data1.zf;
               	var splitNumber=parseInt(zf)/5;
               	var myChart1 = echarts.init(document.getElementById("div3"), "green");
               	option = {
               		    title : {
               		        text: '带班成绩走势'
               		    },
               		    tooltip : {
               		        trigger: 'axis',
               		     formatter: function (params, ticket, callback) {  
         		            
               		    	var showContent=""
         		        	for(var i=0;i<params.length;i++){
         		        		showContent+=params[i].seriesName+" ";
       		        			if (params[i].data>=splitNumber*4) {showContent+='A'+"</br>"}  
    			                else if (params[i].data<splitNumber*4 && params[i].data>=splitNumber*3) {showContent+='B'+"</br>"}
    			                else if (params[i].data<splitNumber*3 && params[i].data>=splitNumber*2) {showContent+='C'+"</br>"} 
    			                else if (params[i].data<splitNumber*2 && params[i].data>=splitNumber*1) {showContent+='D'+"</br>"} 
    			                else if (params[i].data<splitNumber*1 && params[i].data>=splitNumber*0) {showContent+='E'+"</br>"}    
         		        		
         		            }
         		            return showContent;
         		        }, 
               		    },
               		    legend: {
               		        data:data1.availableClass
               		    },
	               		 grid:{
	                         x:50,
	                         y:80,
	                         x2:200,
	                         y2:100
	                     },
               		    toolbox: {
               		        show : true,
               		        feature : {
               		            mark : {show: true},
               		            dataView : {show: true, readOnly: false},
               		            magicType : {show: true, type: ['line', 'bar']},
               		            restore : {show: true},
               		            saveAsImage : {show: true}
               		        }
               		    },
               		    calculable : true,
               		    xAxis : [
               		        {
               		            type : 'category',
               		            boundaryGap : false,
               		            data : data1.category,
               		         	axisLabel: {        
                                  show: true,
                                  interval:0,
                                  textStyle: {
                                      color: '#555',
                                      fontSize:'10'
                                  },
                                  rotate : '-20'
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
	     			            }
               		        }
               		    ],
               		    series : seriesData,
               		 optionToContent:function(opt) {
               	      	var axisData = opt.xAxis[0].data;
               	     	var series = opt.series;
                   		var table = '<table style="width:100%;text-align:center;border:solid #add9c0; border-width:1px 0px 0px 1px;"><tbody><tr>'
                               	  + '<td style="border:solid #add9c0; border-width:0px 1px 1px 0px; padding:10px 0px; width:300px;">学校名称</td>'
                   		for(var k=0, ser = series.length; k<ser; k++){
                	  		 table+="<td style='border:solid #add9c0; border-width:0px 1px 1px 0px; padding:10px 0px;'>"+series[k].name +"</td>";
                   		}
                  		table+="</tr>";
                   		for (var i = 0, l = axisData.length; i < l; i++) {
                      		 table += '<tr>'
                             	   + '<td style="text-align:left;border:solid #add9c0; border-width:0px 1px 1px 0px; padding:5px 10px;">' + axisData[i] + '</td>'
                             	   for(var k=0, ser = series.length; k<ser; k++){
                             		  var seriesStr=series[k].data[i];
                             			if(seriesStr === undefined){
                             				seriesStr="";
                             			}
                             	 		table += '<td style="text-align:center;border:solid #add9c0; border-width:0px 1px 1px 0px; padding:10px 0px;">' + seriesStr + '</td>'
                             	   }
                             	   + '</tr>';
                   		}
                   		table += '</tbody></table>';
                  		return table;
               	   },
                };
               	myChart1.setOption(option);                  
                }
              });
        }
        
        
    };
    window.onload = function() {
        $("#schoolType option[value != '']").remove();
        $("#term option[value != '']").remove();
        $("#examType option[value != '']").remove();

    }
    function importExcel(state){
    	var schoolYear = $("#schoolYear").find("option:selected").val();
    	var term = $("#term").find("option:selected").val();
    	var grade = $("#grade").find("option:selected").val();
        var examType = $("#examType").find("option:selected").val();
        var classCode = [];
        var className = [];
        $("#class").find("option:selected").each(function() {
        	classCode.push($(this).val());
        	className.push($(this).text());
		});
        if (grade == ""){
            window.Msg.alert("请选择年级");
            return;
        }
        if (classCode.length<1){
           window.Msg.alert("请选择班级");
            return;
        }
        if($("#chooseFunction").val()==1){
        	if (term == ""){ 
                window.Msg.alert("请选择学期");
                 return;
             }
             if (examType == ""){
                 window.Msg.alert("请选择测试类型");
                 return;
             }
             var url = "../schoolQualityAnalysis/scoreBetweenClass.do?command=analyzeScoreBetweenClass";
             var data = {
             		schoolYear : schoolYear,
             		term : term,
             		examType : examType,
             		classCode : classCode,
             		className : className,
             		grade:grade,
             		state:state
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
			         功能选择 <select id="chooseFunction"  class="form-control">
			      			<option value="1">带班成绩对比</option>
			      			<option value="2">带班成绩走势</option>
			      	   </select>
			      年级 <select id="grade" name="grade" class="form-control" style="width: 150px;">
			                <option value="">
			                   	 选择年级
			                </option>
			           </select>&nbsp;
			      <span id="functionChangeSpan"> 
			           学年 <select id="schoolYear" name="schoolYear" class="form-control" data-dic="{code:'ND'}" style="width: 150px;"></select>&nbsp; 
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
				</span>
			        </p>
			    </div>
			    <div class="multiselect" style="margin: -5px 10px 10px 0px;">
			        <p>
			      
			            
			            班级<select id="class" name="class" class="form-control"  multiple="multiple" ></select>&nbsp;
			            <span class="toolbar">
			                <button onclick="fastSearch()" title="分析" style="margin-left: 0px;">
			                    <i class="fa fa-search"></i>分析
			                </button>
			                <button onclick="importExcel('1')" title="导出" id="import" style="margin-left: 0px;">
								<i classs="fa fa-search"></i>导出
							</button>
			            </span>
			        </p>
			    </div>
			</div>
        </div>
    </div>
    <div id="waittingDiv" style="position:absolute;width:100px;height:100px;top:100px;left:430px"></div>
     <div class="page-content" id="scoreTable"  style="position:absolute;width:100%;top:80px;left:0px;background-color:transparent;overflow-y:auto;">
		<div id="div2" style="height:auto;width:100%;"></div>
		<div id="div3" style="height:0px;width:100%;"></div>
		<div id="div1" style="height:0px;width:100%;"></div>
	 </div>	
</body>
</html>