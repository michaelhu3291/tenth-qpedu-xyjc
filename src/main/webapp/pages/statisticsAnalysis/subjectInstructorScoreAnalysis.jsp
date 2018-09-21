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
<link href="../theme/default/master.css" rel="stylesheet" />
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
<script type="text/javascript" src="../js/echarts/echarts.js"></script>
<script type="text/javascript" src="../js/echarts/theme/green.js"></script>


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
        listUrl = "../statisticsAnalysis/districtSubjectInstructor.do?command=courseScoreSearch",
        avgNameData1 = [],
        avgData1 = [],
        avgCourse,
        siLvNameData=[],
        ylLvData=[],
        llLvData=[],
        yllLvData=[],
        jglLvData=[],
        siLvCourse=[],
        schoolData3=[],
        numberData3=[],
        dataArry3=[],
        siLvUrl="../statisticsAnalysis/districtSubjectInstructor.do?command=getSiLvForqpAdmin";
    $(function () {
    	//统计图
    
            
            //查询条件
    
		 $.ajax({  
	            type : "POST",  
	            url : "../dataManage/districtSubjectInstructorInfo.do?command=getPeriodByLoginName",  
	            data : {},
	            dataType:"JSON",
	            async: false,
	            success : function(data){
	            	/*for(var i=0;i<data.length;i++){
	            		var periodName;
	            		var periodCode;
	            		//var period=data[i].PERIOD;
						//var periodStr=period.split(", ");
						for(var j=0;j<periodStr.length;j++){
							periodCode=periodStr[j];
							if(periodCode=="0"){
		            			periodCode="xx";
		            			periodName="小学";
		            			}
		            		if(periodCode=="1"){
		            			periodCode="cz";
		            			periodName="初中";
		            			}
		            		if(periodCode=="2" || periodCode=="3" || periodCode=="4"){
		            			periodCode="gz";
		            			periodName="高中";
		            			}
		            		$("#schoolType").append("<option value='"+periodCode+"'>"+periodName+"</option>");
		            		} 
						}*/
	       }
	     });
    	
    	
		  var schoolType = $("#schoolType").find("option:selected").val();
			/*var loadCourse=function(){
				
				if(schoolType!=null){
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
		                  }
		              });
				  }
			}*/	
			//loadCourse();
			  $("#schoolType").change(function () {
				  schoolType = $("#schoolType").find("option:selected").val();
				  $("#course option[value != '']").remove();
				  //loadCourse();
			  });
        //多选下拉框
        $("#school").multiselect({
            checkAllText: "全选",
            uncheckAllText: "全不选",
            noneSelectedText: "选择学校",
            selectedText: '#' + " 所学校",
            selectedList: 2
        }).multiselectfilter({
            label: "学校名称",
            placeholder: "请输入校名"
        });

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
        $("#tabs-2,#tabs-1").css("height", "auto");
        _initFormControls(); //from page.common.js
        _initValidateForXTypeForm(editorFormId);
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
        
        
        //选择年级关联科目
        $("#grade").change(function(){
       	 //$("#course").find("option").attr("value").remove();
       	 $("#course option[value != '']").remove();
       	 var grade = $("#grade").val();
       	 var url = "../authority/dataDictionary.do?command=getCoursesByCode";
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
       		var data = {schoolType:schoolType};
       		$.ajax({
                url: url,
                type: "POST",
                data: data,
                dataType: "JSON",
                success: function (data) {
               	 for(var i = 0;i < data.length;i++){
           			 $("#course").append("<option value='"+data[i].DictionaryCode+"'>"+data[i].DictionaryName+"</option>");
           		 }
                }
            });
       	 }
       	 
        });
        var courseTxt=$("#course").find("option:selected").text();
		var courseVal= $("#course").find("option:selected").val();
		
			//成绩信息描述
			var scoreHtml;
			$(".l_statis").show();
			var data={};
			var loadSiLv=function(){
				var course = $("#course").find("option:selected").val();
				var schoolYear = $("#schoolYear").find("option:selected").val();
				var grade = $("#grade").find("option:selected").val();
				var state = $("#state").find("option:selected").val();
				var term = $("#term").find("option:selected").val();
				var examType = $("#examType").find("option:selected").val();
				
				data={
						course:course,
						grade:grade,
						state:state,
						schoolYear:schoolYear,
						term:term,
						examType:examType
				}
				 $.ajax({
		                url: siLvUrl,
		                type: "POST",
		                data:data,
		                dataType: "JSON",
		                beforeSend:function(XMLHttpRequest){
		                    $("#main1").html("<div style='padding-top:110px;padding-left:30px;'><img src='../theme/images/loading.gif'/></div>"); //在后台返回success之前显示loading图标
		                   
		                },
		                success: function (data) {
		                	   siLvNameData.length=0;
			                    ylLvData.length=0;
			                    llLvData.length=0;
			                    yllLvData.length=0;
			                    jglLvData.length=0;
		                    $.each(data.data, function (index, item) {
		                    		siLvNameData.push(item.School_Name);
		                    	    ylLvData.push(item.Yl);
		                    	    llLvData.push(item.Ll);
		                    	    yllLvData.push(item.Yll);
		                    	    jglLvData.push(item.Jgl);
		                    	   
		                    });

		                    avgCourse=_types[data.course]
		                    
		                    // 基于准备好的dom，初始化echarts实例
		                    var myChart2 = echarts.init(document.getElementById("main1"), "green");
		                    // 指定图表的配置项和数据
		                    var option2 = {
		                        title: {
		                            text: $("#state").find("option:selected").text()+'四率', //主标题
		                            subtext: $("#schoolYear").find("option:selected").text()+"学年"+_types[data.grade]+$("#term").find("option:selected").text()+$("#examType").find("option:selected").text()+_types[data.course]+'测试' //副标题
		                        },
		                        
		                        tooltip: {
		                            trigger: 'axis',
		                            showDelay : 0, // 显示延迟，添加显示延迟可以避免频繁切换，单位ms
		                            axisPointer: { // 坐标轴指示器，坐标轴触发有效
		                                type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
		                            }
		                        },
		                       grid: {
		                            left: '3%',
		                            right: '4%',
		                            bottom: '3%',
		                            containLabel: true
		                        }, 
		                        legend: {
		                            data: ['优率','良率','优良率','及格率']
		                        },
		                        /* X轴 */
		                        xAxis: {
		                        data: siLvNameData,
		                        axisLabel:{
		                        	interval:0,//间隔
		                        	margin:2,
		                            /* rotate:45,//旋转 */
		                        }
		                    },
		                    yAxis: {
		                    	type:"value",
		                    	min:0,
		                    	max:100,
		                    	axisLabel: {
		                                 formatter: '{value} %'
		                             }
		                    },
		                    toolbox: {
				                show: true,
				                feature: {
				                    dataView: { 
				                    	readOnly: true,
				                    	backgroundColor:"#e7fcd9",
				                    	textareaColor: "#e7fcd9",
				                    	textareaBorderColor: "#67891b",
				                    	buttonColor: "#67891b",
				                    	optionToContent: function(opt) {
						                    var axisData = opt.xAxis[0].data;
						                    var series = opt.series;
						                    var table = '<table style="width:100%;text-align:center;border:solid #add9c0; border-width:1px 0px 0px 1px;"><tbody><tr>'
						                                 + '<td style="border:solid #add9c0; border-width:0px 1px 1px 0px; padding:10px 0px;">学校名称</td>'
						                                 + '<td style="border:solid #add9c0; border-width:0px 1px 1px 0px; padding:10px 0px;">' + series[0].name + '</td>'
						                                 + '<td style="border:solid #add9c0; border-width:0px 1px 1px 0px; padding:10px 0px;">' + series[1].name + '</td>'
						                                 + '<td style="border:solid #add9c0; border-width:0px 1px 1px 0px; padding:10px 0px;">' + series[2].name + '</td>'
						                                 + '<td style="border:solid #add9c0; border-width:0px 1px 1px 0px; padding:10px 0px;">' + series[3].name + '</td>'
						                                 + '</tr>';
						                    for (var i = 0, l = axisData.length; i < l; i++) {
						                        table += '<tr>'
						                                 + '<td style="text-align:left;border:solid #add9c0; border-width:0px 1px 1px 0px; padding:5px 10px;">' + axisData[i] + '</td>'
						                                 + '<td style="text-align:center;border:solid #add9c0; border-width:0px 1px 1px 0px; padding:10px 0px;">' + series[0].data[i] + '</td>'
						                                 + '<td style="text-align:center;border:solid #add9c0; border-width:0px 1px 1px 0px; padding:10px 0px;">' + series[1].data[i] + '</td>'
						                                 + '<td style="text-align:center;border:solid #add9c0; border-width:0px 1px 1px 0px; padding:10px 0px;">' + series[2].data[i] + '</td>'
						                                 + '<td style="text-align:center;border:solid #add9c0; border-width:0px 1px 1px 0px; padding:10px 0px;">' + series[3].data[i] + '</td>'
						                                 + '</tr>';
						                    }
						                    table += '</tbody></table>';
						                    return table;
						                }
				                    }, //数据预览
				                    saveAsImage : {show: true}//是否保存图片
				                }
				            },
		    		            series: [
		    		                     {
		    		                         name:'优率',
		    		                         type:'bar',
		    		                         data:ylLvData
		    		                     },
		    		                     {
		    		                         name:'良率',
		    		                         type:'bar',
		    		                         data:llLvData
		    		                     },
		    		                     {
		    		                         name:'优良率',
		    		                         type:'bar',
		    		                         data:yllLvData
		    		                     }
		    		                     ,
		    		                     {
		    		                         name:'及格率',
		    		                         type:'bar',
		    		                         data:jglLvData
		    		                     }
		    		                 ]
		                    };
		                    myChart2.setOption(option2);
		            }
		        });
			}
			loadSiLv();
			
			
			
			
			$("#fastSearchsiLv").click(function(){
				loadSiLv();
			});
		});
    
    
    
    
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
                        <select id="schoolYear" name="schoolYear" class="form-control" data-dic="{code:'ND'}" style="width: 100px;"></select>
                        &nbsp; 年级
                        <select id="grade" name="grade"  class="form-control" data-dic="{code:'nj'}" style="width: 100px;">
                        	<option value="">选择年级</option>
						</select> 
                          &nbsp;  科目
                        <select id="course" name="course"  style="width: 100px;"  class="form-control" >
                        	<option value="">选择科目</option>
                        </select>
                        
                         &nbsp; 学期
                        <select id="term" name="term" class="form-control" style="width: 100px;" data-dic="{code:'xq'}">
                        </select>&nbsp; 测试类型
                        <select id="examType" name="examType" class="form-control" style="width: 100px;" data-dic="{code:'kslx'}">
                        </select>
                        &nbsp; 学籍状态
                        <select id="state" name="state" class="form-control" style="width: 100px;" data-dic="{code:'stuState'}">
                        	<option value="qb">全部</option>
                        </select>
                      <span class="toolbar">
							<button id="fastSearchsiLv" title="查询" style="margin-left: 0px;">
								<i class="fa fa-search"></i>分析
							</button>
						</span>
                    </p>
                </div>
                <div class="multiselect" style="margin: -5px 10px 10px 0px;">
                    <p>
                       
                    </p>
                </div>
            </div>
        </div>
        <div class="l_statis">
		<!--  <div class="l_title02">超级管理员图表统计</div>-->
		<div class="l_form">
			<div class="l_form01" id="main1" style="width: 508px;height:315px;">
			</div>
			<div class="l_form01" id="main1" style="width: 508px;height:315px;">
			</div>
		</div>
	</div>
    </div>

</body>
</html>