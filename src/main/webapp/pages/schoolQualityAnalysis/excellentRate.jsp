
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
<link href="../theme/default/jquery.multiselect.filter.css" rel="stylesheet" />
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

        

        _initButtons({
            cancelBTN : function() {
                $(listId).trigger("reloadGrid");
                hideSlidePanel(".page-editor-panel");
                editId = '';
            },
        });
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
    function fastSearch(){
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
        var url = "../schoolQualityAnalysis/excellentRate.do?command=analyzeExcellentRate";
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
        		chooseFunction : $("#chooseFunction").val(),
        		grade:$("#grade").val()
        	};
        $("#chartDiv").empty();
    	$("#scoreTable").empty();
    	$("#scoreTable2").empty();
    	$("#chartDiv").height(0);
    	$("#scoreTable").height(0);
    	$("#scoreTable2").height(0);
    	$("#scoreTable3").empty();
    	$("#scoreTable3").height(0);
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
            		var chooseFunction = $("#chooseFunction").val();
                	if(chooseFunction==1){//单科
                		
                		if(data1.rateList.length>0){
                			var schoolsData = data1.rateList[0].schools;
                			var allSchool=data1.rateList[1].zjallSchool;
                			var schoolNames = [];
                    		var yxl = [];
                    		var lhl = [];
                    		var hgl = [];
                    		var bjgl = [];
                    		var tableContent="";
                    		tableContent+="<table class='inputtable'><tbody><tr><th>班级名称</th><th>实考人数</th><th>最高分</th><th>最低分</th><th>平均分</th><th>平均分排名</th>";
                    		tableContent+="<th>优率</th><th>良率</th><th>优良率</th><th>优良率排名</th><th>合格率</th><th>及格率</th>";
                    		tableContent+="<th>极差率</th><th>标准差</th><th>标准分</th><th>T值</th><th>差异系数</th><th>超均率</th><th>偏差率</th><th>偏差率排名</th></tr>";

                    		var districtDatas = data1.rateList[0].district;
                    		$("#scoreTable").height(42+36*(schoolsData.length+2)+135);
                    		for(var i=0;i<schoolsData.length;i++){
                    			var temp=schoolsData[i];
                    			tableContent+="<tr><td>"+temp.schoolName+"</td><td>"+temp.skrs+"</td><td>"+temp.zgf+"</td><td>"+temp.zdf+"</td><td>"+temp.pjf+"</td><td>"+temp.pjfpm+"</td>";
                    			tableContent+="<td>"+temp.yxl+"</td><td>"+temp.lhl+"</td><td>"+temp.yll+"</td><td>"+temp.yllpm+"</td><td>"+temp.hgl+"</td><td>"+temp.jgl+"</td>";
                    			tableContent+="<td>"+temp.jcl+"</td><td>"+temp.bzc+"</td><td>"+temp.bzf+"</td><td>"+temp.T+"</td><td>"+temp.cyxs+"</td><td>"+temp.cjl+"</td><td>"+temp.pcl+"</td><td>"+temp.pclpm+"</td></tr>";
                    			schoolNames.push(temp.schoolName);
                    			yxl.push((parseFloat(temp.yxrs)*100/parseFloat(temp.skrs)).toFixed(2));
                    			lhl.push((parseFloat(temp.lhrs)*100/parseFloat(temp.skrs)).toFixed(2));
                    			hgl.push((parseFloat(temp.hgrs)*100/parseFloat(temp.skrs)).toFixed(2));
                    			bjgl.push((parseFloat(temp.bjgrs)*100/parseFloat(temp.skrs)).toFixed(2));
                    		}
                    		tableContent+="<tr class='xj'><td>(校级)小计</td><td>"+districtDatas.skrs+"</td><td>"+districtDatas.zgf+"</td><td>"+districtDatas.zdf+"</td><td>"+districtDatas.pjf+"</td><td></td>";
                    		tableContent+="<td>"+districtDatas.yxl+"</td><td>"+districtDatas.lhl+"</td><td>"+districtDatas.yll+"</td><td></td><td>"+districtDatas.hgl+"</td><td>"+districtDatas.jgl+"</td>";
                    		tableContent+="<td>"+districtDatas.jcl+"</td><td>"+districtDatas.bzc+"</td><td></td><td></td><td>"+districtDatas.cyxs+"</td><td></td><td></td><td></td></tr>";
                    		
                    		//全区详细信息
                    		tableContent+="<tr class='xj'><td>(区级)小计</td><td>"+allSchool.skrs+"</td><td>"+allSchool.zgf+"</td><td>"+allSchool.zdf+"</td><td>"+allSchool.pjf+"</td><td></td>";
                    		tableContent+="<td>"+allSchool.yxl+"</td><td>"+allSchool.lhl+"</td><td>"+allSchool.yll+"</td><td></td><td>"+allSchool.hgl+"</td><td>"+allSchool.jgl+"</td>";
                    		tableContent+="<td>"+allSchool.jcl+"</td><td>"+allSchool.bzc+"</td><td></td><td></td><td>"+allSchool.cyxs+"</td><td></td><td></td><td></td></tr>";
                    		tableContent+="<tr><td colspan='20'><span><p style='color:red;text-align:left'>(班级/学校)标准分=（(班级/学校）平均分-（学校/区）平均分）/（学校/区）标准差；T值=标准分*10+50；差异系数=标准差/平均分</p><p style='color:red;text-align:left'>(班级/学校)超均率=(班级/学校）大于等于（学校/区）平均分的人数/（班级/学校）实考人数</p><p style='color:red;text-align:left'>(班级/学校)偏差率=（(班级/学校）平均分-（学校/区）平均分）/（学校/区）平均分*100%</p>";
                    		var Course = data1.courseList[0].courseName;
                    		var Exam_Zf = data1.courseList[0].Exam_Zf;
                    		var Exam_Yx = data1.courseList[0].Exam_Yx;
                    		var Exam_Lh = data1.courseList[0].Exam_Lh;
                    		var Exam_Jg = data1.courseList[0].Exam_Jg;
                    		tableContent+="<p style='color:red;text-align:left'>"+Course+"各段分数划分：优秀 "+Exam_Zf+"-"+Exam_Yx+"；良好 "+(Exam_Yx-1)+"-"+Exam_Lh+"；合格 "+(Exam_Lh-1)+"-"+Exam_Jg+"；不及格 "+(Exam_Jg-1)+"-0；</p>";
                    		tableContent+="</span></td></tr>";
                    		tableContent+="</tbody></table>";
                    		$("#scoreTable").append(tableContent);
                    		//添加小计列的样式
                    		$(".xj td").css({
                				"background-color" : "#67891B",
                				"color":"#ffffff",
                				"font-weight":"bold",
                			})
                			//tr 奇数行和偶数行样式
                			var trStr=$("#scoreTable .inputtable tr");
                			for(var i=0;i<trStr.length;i++){
                				if(i>0 && i<trStr.length-2 && i%2==0){
                					var tdStr= $(trStr)[i];
                					$(tdStr).children().css("background-color","#e7fcd9")
                				}
                			}
                            $("#chartDiv").height(684);
                    		var myChart1 = echarts.init(document.getElementById("chartDiv"), "green");
                    		option = {
                    		    tooltip : {
                    		        trigger: 'axis',
                    		        
                    		        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                    		            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                    		        }
                    		    },
                    		    legend: {
                    		    	data:['优率', '良率','合格率','不及格率']
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
                    		                 data : schoolNames,
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
                    		            max:100,
                    		            axisLabel : {
                    		                formatter: '{value} %'
                    		            }
                    		        }
                    		    ],
                    		    series : [
                    		              {
                    		                  name:'不及格率',
                    		                  type:'bar',
                    		                  stack: '总量',
                    		                  itemStyle : { normal: {label : {show: true, position: 'insideRight',formatter:'{c} %'}}},
                    		                  barMaxWidth:60,
                    		                  data:bjgl
                    		              },
                    		              {
                    		                  name:'合格率',
                    		                  type:'bar',
                    		                  stack: '总量',
                    		                  itemStyle : { normal: {label : {show: true, position: 'insideRight',formatter:'{c} %'}}},
                    		                  barMaxWidth:60,
                    		                  data:hgl
                    		              },
                    		              {
                    		                  name:'良率',
                    		                  type:'bar',
                    		                  stack: '总量',
                    		                  itemStyle : { normal: {label : {show: true, position: 'insideRight',formatter:'{c} %'}}},
                    		                  barMaxWidth:60,
                    		                  data:lhl
                    		              },
                    		              {
                    		                  name:'优率',
                    		                  type:'bar',
                    		                  stack: '总量',
                    		                  itemStyle : { normal: {label : {show: true, position: 'insideRight',formatter:'{c} %'}}},
                    		                  barMaxWidth:60,
                    		                  data:yxl
                    		              }
                    		          ]
                    		};
                    		myChart1.setOption(option);
                    	}else{
                    		window.Msg.alert("暂无数据！");
                            return;
                    	}
                		if(data1.minAndMax.length>0){
                			var divContent="<table class='inputtable'><thead> <tr><th colspan='3'>最高分最低分名单</th></tr> <tr><th>班级名称</th> <th>最高分名单</th> <th>最低分名单</th> </tr></thead>";
                			for(var i=0;i<data1.minAndMax.length;i++){
                				var temp = data1.minAndMax[i];
                				divContent+="<tr><td>"+temp.className+"</td><td>"+temp.maxList+"</td><td>"+temp.minList+"</td></tr>";
                			}
                			divContent+="</table>";
                			$("#scoreTable3").height((data1.minAndMax.length+3)*36)
                			$("#scoreTable3").append(divContent);
                		}
                	}else{
                		if(data1.rateList.length>0){
                			var divContent="<table><tr>";
                    		for(var i=0;i<data1.rateList.length;i++){
                    			var schoolsData = data1.rateList[i].schools;
                        		var districtDatas = data1.rateList[i].district;
                        		var allSchools=data1.rateList[i].allSchool;
                        		if(districtDatas){
                        			if(i==0){
                            			divContent+="<td><table class='inputtable'><thead>  <tr><th rowspan='2'>班级</th><th rowspan='2'>实考人数</th><th colspan='4'>"+districtDatas.courseName+"</th></tr>  <tr><th>及格率</th><th>优良率</th><th>平均分</th><th>平均分排名</th></tr></thead><tbody>";
                            		}else{
                            			divContent+="<td><table class='inputtable'><thead>  <tr><th colspan='4'>"+districtDatas.courseName+"</th></tr>  <tr><th>及格率</th><th>优良率</th><th>平均分</th><th>平均分排名</th></tr></thead><tbody>";
                            		}
                        		}
                        		if(schoolsData){
                        			$("#scoreTable2").height(114+36*(schoolsData.length+2)+32*(data1.courseList.length+1));
                            		for(var j=0;j<schoolsData.length;j++){
                            			
                            			var temp=schoolsData[j];
                            			if(i==0){
                            				divContent+="<tr><td>"+temp.schoolName+"</td><td>"+temp.skrs+"</td><td>"+temp.jgl+"</td><td>"+temp.yll+"</td><td>"+temp.pjf+"</td><td>"+temp.pjfpm+"</td></tr>";
                                		}else{
                                			divContent+="<tr><td>"+temp.jgl+"</td><td>"+temp.yll+"</td><td>"+temp.pjf+"</td><td>"+temp.pjfpm+"</td></tr>";
                                		}
                            		}
                        		}
                        		if(districtDatas){
                        			if(i==0){
                            			divContent+="<tr class='xj'><td>(校级)小计</td><td>"+districtDatas.skrs+"</td><td>"+districtDatas.jgl+"</td><td>"+districtDatas.yll+"</td><td>"+districtDatas.pjf+"</td><td></td></tr>";
                            		}else{
                            			divContent+="<tr class='xj'><td>"+districtDatas.jgl+"</td><td>"+districtDatas.yll+"</td><td>"+districtDatas.pjf+"</td><td></td></tr>";
                            		}
                        		}
                        		 if(allSchools){
                    			if(i==0){
                        			divContent+="<tr class='xj'><td>(区级)小计</td><td>"+allSchools.skrs+"</td><td>"+allSchools.jgl+"</td><td>"+allSchools.yll+"</td><td>"+allSchools.pjf+"</td><td></td></tr></tbody></table></td>";
                        		}else{
                        			divContent+="<tr class='xj'><td>"+allSchools.jgl+"</td><td>"+allSchools.yll+"</td><td>"+allSchools.pjf+"</td><td></td></tr></tbody></table></td>";
                        		}
                    		} 
                    		}
                   			divContent+="<td><table class='inputtable'><thead><tr><th colspan='4'>总分</th></tr>  <tr><th>及格率</th><th>优良率</th><th>平均分</th><th>平均分排名</th></tr></thead><tbody>";
                   			for(var i=0;i<data1.districtList.length;i++){
                    			var temp=data1.districtList[i];
                    			divContent+="<tr><td>"+temp.jgl+"</td><td>"+temp.yll+"</td><td>"+temp.pjf+"</td><td>"+temp.pjfpm+"</td></tr>";
                    		}
                    		divContent+="<tr class='xj'><td>"+data1.xjLastMap.jgl+"</td><td>"+data1.xjLastMap.yll+"</td><td>"+data1.xjLastMap.pjf+"</td><td></td></tr>";
                    		divContent+="<tr class='xj'><td>"+data1.lastMap.jgl+"</td><td>"+data1.lastMap.yll+"</td><td>"+data1.lastMap.pjf+"</td><td></td></tr></tbody></table></td></tr><tr><td colspan='4'><span>"
                    		var allZf = 0;
                    		var allYx = 0;
                    		var allLh = 0;
                    		var allJg = 0;
                    		for(var h=0;h<data1.courseList.length;h++){
                    			var Course = data1.courseList[h].courseName;
                        		var Exam_Zf = data1.courseList[h].Exam_Zf;
                        		var Exam_Yx = data1.courseList[h].Exam_Yx;
                        		var Exam_Lh = data1.courseList[h].Exam_Lh;
                        		var Exam_Jg = data1.courseList[h].Exam_Jg;
                        		divContent+="<p style='color:red;text-align:left'>"+Course+"各段分数划分：优秀 "+Exam_Zf+"-"+Exam_Yx+"；良好 "+(Exam_Yx-1)+"-"+Exam_Lh+"；合格 "+(Exam_Lh-1)+"-"+Exam_Jg+"；不及格 "+(Exam_Jg-1)+"-0；</p>";
                        		allZf+=Exam_Zf;
                        		allYx+=Exam_Yx;
                        		allLh+=Exam_Lh;
                        		allJg+=Exam_Jg;
                    		}
                    		divContent+="<p style='color:red;text-align:left'>总分各段分数划分：优秀 "+allZf+"-"+allYx+"；良好 "+(allYx-data1.courseList.length)+"-"+allLh+"；合格 "+(allLh-data1.courseList.length)+"-"+allJg+"；不及格 "+(allJg-data1.courseList.length)+"-0；</p>";
                    		divContent+="</span></td></tr></table>"
                    		
                    		$("#scoreTable2").append(divContent);
                    		//添加小计列的样式
                    		$(".xj td").css({
                				"background-color" : "#67891B",
                				"color":"#ffffff",
                				"font-weight":"bold",
                			})
                				//tr 奇数行和偶数行样式
                			var tableStr=$("#scoreTable2 .inputtable");
                    		for(var i=0;i<tableStr.length;i++){
                    			var tableStr1=$(tableStr)[i];
                    			var tbodyStr=$(tableStr1).find("tbody").children();
                    			for(var k=0;k<tbodyStr.length;k++){
                    				if(k>0 && (k+1)%2==0){
                    				var tdStr= $(tbodyStr)[k];
                    				if($(tdStr).attr("class")!="xj"){
                    					$(tdStr).children().css("background-color","#e7fcd9")
                    				}
                    				}
                    			}
                    		}
                		}else{
                    		window.Msg.alert("暂无数据！");
                            return;
                    	}
                	}
            	}
            	 
            }
          });
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
    var url = "../schoolQualityAnalysis/excellentRate.do?command=analyzeExcellentRate";
    var url1 = "../schoolQualityAnalysis/excellentRate.do?command=analyzeExcellentRateImport";
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
    		chooseFunction : $("#chooseFunction").val(),
    		grade:$("#grade").val()
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
        	if(data1.message=="faile"){
        		window.Msg.alert("暂无数据！");
                return;
        	}else{
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
			                <button onclick="importExcel()" title="导出" style="margin-left: 0px;">
								<i class="fa fa-search"></i>导出
							</button>
			            </span>
			        </p>
			    </div>
			</div>
        </div>
    </div>
    <div id="waittingDiv" style="position:absolute;width:100px;height:100px;top:100px;left:430px"></div>
    <div class="page-content"   style="position:absolute;width:100%;top:80px;left:0px;background-color:transparent;overflow-y:auto;overflow-x:hidden;">
		<div id="scoreTable" style="height:0px;width:100%;overflow-y:hidden;overflow-x:auto;"></div>
		<div id="scoreTable2" style="height:0px;width:100%;overflow-x:scroll;overflow-y:hidden;"></div>
		<div id="scoreTable3" style="height:0px;width:100%;"></div>
		<div id="chartDiv" style="height:0px;width:100%;"></div>
	 </div>	
</body>
</html>