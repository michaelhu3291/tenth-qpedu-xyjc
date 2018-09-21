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
	var avgNameData1 = [], 
	       avgData1 = [], 
	       avgData2=[],
	       courses,
			avgCourse, 
	       listId = "#list2", 
	       editorFormId = "#editorForm", 
	       pagerId = "#pager2", 
	       editorRelatedFormId = "#editorRelatedForm", 
	       avgUrl = "../statisticsAnalysis/districtSubjectInstructor.do?command=getSubjectInstrutorsAvg";
	    		   
	$(function() {
		
		//统计图

		//查询条件

		$.ajax({
					type : "POST",
					url : "../dataManage/districtSubjectInstructorInfo.do?command=getPeriodByLoginName",
					data : {},
					dataType : "JSON",
					async : false,
					success : function(data) {
						for (var i = 0; i < data.length; i++) {
							var periodName;
							var periodCode;
							var period = data[i].PERIOD;
							var periodStr = period.split(", ");
							for (var j = 0; j < periodStr.length; j++) {
								periodCode = periodStr[j];
								if (periodCode == "0") {
									periodCode = "xx";
									periodName = "小学";
								}
								if (periodCode == "1") {
									periodCode = "cz";
									periodName = "初中";
								}
								if (periodCode == "2" || periodCode == "3"
										|| periodCode == "4") {
									periodCode = "gz";
									periodName = "高中";
								}
								$("#schoolType").append(
										"<option value='"+periodCode+"'>"
												+ periodName + "</option>");
							}
						}
					}
				});
		
		

		_initButtons({
			cancelBTN : function() {
				$(listId).trigger("reloadGrid");
				hideSlidePanel(".page-editor-panel");
				editId = '';
			},
		}); //from page.common.js
		$("#tblInfo").find("button").button();
		$("#tabs").tabs({
			heightStyle : "fill"
		});
		$("#tabs-2,#tabs-1").css("height", "auto");
		_initFormControls(); //from page.common.js
		_initValidateForXTypeForm(editorFormId);
		
		//加载数据字典
		
		
		loadDictionary(function() {
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
		});
		 
		 var schoolType = $("#schoolType").find("option:selected").val();
		var loadCourse=function(){
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
		}	
		loadCourse();
		
		  $("#schoolType").change(function () {
			  schoolType = $("#schoolType").find("option:selected").val();
			  $("#course option[value != '']").remove();
			  loadCourse();
		  });
				
			  
		
		var courseTxt = $("#course").find("option:selected").text();
		var courseVal = $("#course").find("option:selected").val();
		//成绩信息描述
		var scoreHtml;
		var course = $("#course").find("option:selected").val();
		if (courseTxt.length > 0) {
			course = $("#course").find("option:selected").text();
		}
		$(".l_statis").show();
		
		var loadAvg=function(){
		    course="";
		   var schoolType = $("#schoolType").find("option:selected").val();
		   course = $("#course").find("option:selected").val();
			var schoolYear = $("#schoolYear").find("option:selected").val();
			var term = $("#term").find("option:selected").val();
			var data={
				course:course,
				schoolYear:schoolYear,
				term:term,
				schoolType:schoolType
			}
		 $.ajax({
             url: avgUrl,
             type: "POST",
             data: data,
             dataType: "JSON",
             beforeSend:function(XMLHttpRequest){
                 $("#main1").html("<div style='padding-top:110px;padding-left:30px;'><img src='../theme/images/loading.gif'/></div>"); //在后台返回success之前显示loading图标
            	},
             success: function (data) {
            	 avgNameData1.length=0;
            	 avgData1.length=0;
            	 avgData2.length=0;
                 $.each(data.data, function (index, item) {
                 	    avgNameData1.push(item.School_Name);
                 	    avgData1.push(item.qzAvg);
                 	    avgData2.push(item.qmAvg);
                 });
                 courses=_types[data.course]
                 // 基于准备好的dom，初始化echarts实例
                 var myChart1 = echarts.init(document.getElementById("main1"), "green");
                 // 指定图表的配置项和数据
                 var option1 = {
                     title: {
                         text: "学生"+courses+"成绩", //主标题
                         subtext: "学生"+courses+"成绩平均分" //副标题
                     },
                     tooltip: {
                    	 trigger: 'axis'
                     },
                     legend: {
                         data:['期中','期末']
                     },
                     grid: {
                         left: '3%',
                         right: '4%',
                        /*  bottom: '35%', */
                         containLabel: true
                     },
                     /* X轴 */
                 xAxis: {
                     type: 'category',
                     boundaryGap: false,
                     data: avgNameData1,
                     axisLabel:{
                     	interval:0,//间隔
                     	margin:2,
                       /*  rotate:45,//旋转 */
                        formatter:function(val){
                            return val.split("").join("\n");
                        }
                     }
                 },
                 
                 yAxis: {
                     type: 'value'
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
				                                 + '</tr>';
				                    for (var i = 0, l = axisData.length; i < l; i++) {
				                        table += '<tr>'
				                                 + '<td style="text-align:left;border:solid #add9c0; border-width:0px 1px 1px 0px; padding:5px 10px;">' + axisData[i] + '</td>'
				                                 + '<td style="text-align:center;border:solid #add9c0; border-width:0px 1px 1px 0px; padding:10px 0px;">' + series[0].data[i] + '</td>'
				                                 + '<td style="text-align:center;border:solid #add9c0; border-width:0px 1px 1px 0px; padding:10px 0px;">' + series[1].data[i] + '</td>'
				                                 + '</tr>';
				                    }
				                    table += '</tbody></table>';
				                    return table;
				                }
		                    }, //数据预览
		                    saveAsImage : {show: true}//是否保存图片
		                }
		            },
 		           series: [{
 		                       name:'期中',
 		                       type:'line',
 		                       data:avgData1
 		                   },
 		                   {
 		                       name:'期末',
 		                       type:'line',
 		                       data:avgData2
 		                   } ]
               
                 };
             myChart1.setOption(option1);
         }
     });
	}
		loadAvg();
		$("#fastSearchs").click(function(){
			loadAvg();
		});
		
		
	});

</script>
</head>
<body style="overflow: auto;">
	<div class="page-list-panel"
		style="overflow-y: auto; overflow-x: hidden;">
		<div class="head-panel">
			<!-- 多条件 -->
			<div id="importCondition" style="margin: -5px 10px 10px 0px;">
				<div class="search-panel" style="display: block;">
					<p>
						学年 <select id="schoolYear" name="schoolYear" class="form-control" data-dic="{code:'ND'}" style="width: 150px;"></select> 
						&nbsp; 科目<select id="course" name="course" class="form-control" style="width: 150px;" >
						</select> 
						&nbsp; 学校类型 <select id="schoolType" name="schoolType"  class="form-control" style="width: 150px;">
						</select> 
						 &nbsp; 学期<select id="term" name="term" class="form-control" style="width: 150px;" data-dic="{code:'xq'}">
                        </select>
						<!-- &nbsp; 测试类型<select id="examType" name="examType" class="form-control" style="width: 150px;" data-dic="{code:'kslx'}">
                        </select>
                     
                         &nbsp; 年级 <select id="grade" name="grade" class="form-control" style="width: 150px;">
							<option value="">选择年级</option>
						</select> -->
						<span class="toolbar">
							<button id="fastSearchs" title="查询" style="margin-left: 0px;">
								<i class="fa fa-search"></i>分析
							</button>
							</span>
					</p>
				</div>
			</div>
		</div>
		<div class="l_statis">
			<div class="l_title02">教研员平均分图表统计</div>
			<div class="l_form01" id="main1" style="width: 508px; height: 415px;">
			</div>
		</div>
	</div>

</body>
</html>