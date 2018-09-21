<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>图表统计</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="../theme/default/master.css" rel="stylesheet" />
<script type="text/javascript" src="../js/jquery.js"></script>
<script type="text/javascript" src="../js/echarts/echarts.js"></script>
<script type="text/javascript" src="../js/echarts/theme/green.js"></script>

</head>
<body>
	<div class="l_statis">
		<div class="l_title02">图表统计</div>
		<div class="l_form">
			<div class="l_form01" id="main1" style="width: 508px;height:315px;">
<!-- 				<span>2014年和2015年学生标准分分布情况 </span> -->
<!-- 				<div class="l_formnav"> -->
<!-- 					<img src="../theme/images/form_03.gif"> -->
<!-- 				</div> -->
			</div>
			<div class="l_form01" id="main2" style="width: 508px;height:315px;">
<!-- 				<span>2014、2015年学生总分分布情况</span> -->
<!-- 				<div class="l_formnav"> -->
<!-- 					<img src="../theme/images/form_04.gif"> -->
<!-- 				</div> -->
			</div>
			<div class="l_form01" id="main3" style="width: 508px;height:315px;">
<!-- 				<span>2014年和2015年学生标准分分布情况 </span> -->
<!-- 				<div class="l_formnav"> -->
<!-- 					<img src="../theme/images/form_01.gif"> -->
<!-- 				</div> -->
			</div>
			<div class="l_form01" id="main4" style="width: 508px;height:315px;">
<!-- 				<span>2014、2015年学生总分分布情况</span> -->
<!-- 				<div class="l_formnav"> -->
<!-- 					<img src="../theme/images/form_02.gif"> -->
<!-- 				</div> -->
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
    var schoolData1 = [],
        numberData1 = [],
    schoolData2 = [],
        numberData2 = [],
    schoolData3 = [],
        numberData3 = [],
        dataArry3=[],
    schoolData4 = [],
        numberData4 = [],
        url = "../echarts.do?command=getSchoolNumber";
    $(function () {
        $.ajax({
            url: url,
            type: "POST",
            data: {},
            dataType: "JSON",
            beforeSend:function(XMLHttpRequest){
                $("#main1,#main2,#main3,#main4").html("<div style='padding-top:110px;padding-left:30px;'><img src='../theme/images/loading.gif'/></div>"); //在后台返回success之前显示loading图标
           	},
            success: function (data) {
                $.each(data.data, function (index, item) {
                	if(item.School_Type_Sequence!=""&&item.School_Type_Sequence==1){
                		schoolData1.push(item.School_Name);
                    	numberData1.push(item.Number);
                	}else if(item.School_Type_Sequence!=""&&item.School_Type_Sequence==2){
                		schoolData2.push(item.School_Name);
                    	numberData2.push(item.Number);
                	}else if(item.School_Type_Sequence!=""&&item.School_Type_Sequence==3){
                		schoolData3.push(item.School_Name);
                    	numberData3.push(item.Number);
                    	var  data3 = {};
                    	data3.value=item.Number;
                    	data3.name=item.School_Name;
                    	dataArry3.push(data3);
                	}else if(item.School_Type_Sequence!=""&&item.School_Type_Sequence==4){
                		schoolData4.push(item.School_Name);
                    	numberData4.push(item.Number);
                	} 
                });
                // 基于准备好的dom，初始化echarts实例
                var myChart1 = echarts.init(document.getElementById("main1"), "green");
                var myChart2 = echarts.init(document.getElementById("main2"), "green");
                var myChart3 = echarts.init(document.getElementById("main3"), "green");
                var myChart4 = echarts.init(document.getElementById("main4"), "green");
                // 指定图表的配置项和数据
                var option1 = {
                    title: {
                        text: '学校学生', //主标题
                        subtext: '全区幼儿园' //副标题
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
                    /* grid: { // 控制图的大小，调整下面这些值就可以，
						x: 40,
						x2: 100,
						y2: 100// y2可以控制 X轴跟Zoom控件之间的间隔，避免以为倾斜后造成 label重叠到zoom上
					}, */
                    legend: {
                        data: ['人数']
                    },
                    /* X轴 */
                    xAxis: {
                        data: schoolData1,
                        axisTick : {//小标记
		                    show : false  
		                },
                        axisLabel:{
                        	show:false,
                        	/* interval:0,//间隔 */
                        	textStyle:{//样式
                        		/* color:"#e7fcd9" */
                        	}
                        	/* formatter:function(val){
							    return val.split("").join("\n");
							} */
                        }
                    },
                    yAxis: {type:"value"},
                    toolbox: {
		                show: true,
		                feature: {
		                	magicType : {show: true, type: ['line', 'bar']},//支持柱形图和折线图的切换
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
				                                 + '</tr>';
				                    for (var i = 0, l = axisData.length; i < l; i++) {
				                        table += '<tr>'
				                                 + '<td style="text-align:left;border:solid #add9c0; border-width:0px 1px 1px 0px; padding:5px 10px;">' + axisData[i] + '</td>'
				                                 + '<td style="text-align:center;border:solid #add9c0; border-width:0px 1px 1px 0px; padding:10px 0px;">' + series[0].data[i] + '</td>'
				                                 + '</tr>';
				                    }
				                    table += '</tbody></table>';
				                    return table;
				                }
		                    }, //数据预览
		                    restore : {show: true}, //复原
							myTool: {//自定义按钮 danielinbiti,这里增加，myTool可以随便取名字
								show: true,
								title: '自定义方法',//鼠标移动上去显示的文字  
								icon: 'image://http://echarts.baidu.com/images/favicon.png',//图标  
								onclick: function (option1){//点击事件,这里的option1是chart的option信息
									alert(option1);
									
								}
							},
		                    saveAsImage : {show: true}//是否保存图片
		                }
		            },
                    series: [{
                        name: '人数',
                        type: 'bar',
                        data: numberData1
                    }]
                };
                var option2 = {
                    title: {
                        text: '学校学生', //主标题
                        subtext: '全区小学' //副标题
                    },
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: { // 坐标轴指示器，坐标轴触发有效
                            type: 'line' // 默认为直线，可选为：'line' | 'shadow'
                        }
                    },
                    grid: {
                        left: '3%',
                        right: '4%',
                        bottom: '3%',
                        containLabel: true
                    },
                    legend: {
                        data: ['人数']
                    },
                    xAxis: {
                    	axisTick : {//小标记
		                    show : false  
		                },
		                axisLabel:{
                        	show:false,
                        	/* interval:0,//间隔 */
                        	textStyle:{//样式
                        		/* color:"#e7fcd9" */
                        	}
                        	/* formatter:function(val){
							    return val.split("").join("\n");
							} */
                        },
                        data: schoolData2
                    },
                    yAxis: {},
                    series: [{
                        name: '人数',
                        type: 'line',
                        data: numberData2
                    }]
                };
                var option3 = {
                    title: {
                        text: '学校学生', //主标题
                        subtext: '全区中学', //副标题
                        x:'right'
                    },
                    tooltip: {
                        trigger: 'item',
                        /* axisPointer: { // 坐标轴指示器，坐标轴触发有效
                            type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
                        } */
                        formatter: "{a} <br/>{b} : {c} ({d}%)"
                    },
                    /* grid: {
                        left: '3%',
                        right: '4%',
                        bottom: '3%',
                        containLabel: true
                    }, */
                    legend: {
                        orient: 'vertical',
				        left: 'left',
				        data: schoolData3
                    },
                    series: [{
                        name: '人数',
                        type: 'pie',
                       /*  radius : '50%', */
            			center: ['65%', '60%'],
            			avoidLabelOverlap: false,
            			label: {
			                normal: {
			                    show: false,
			                    position: 'center'
			                },
			                emphasis: {
			                    show: false,
			                    textStyle: {
			                        fontSize: '30',
			                        fontWeight: 'bold'
			                    }
			                }
			            },
			            labelLine: {
			                normal: {
			                    show: false
			                }
			            },
                        data: dataArry3,
                        itemStyle: {
			                emphasis: {
			                    shadowBlur: 10,
			                    shadowOffsetX: 0,
			                    shadowColor: 'rgba(0, 0, 0, 0.5)'
			                }
			            }
                    }]
                };
                var option4 = {
                    title: {
                        text: '学校学生', //主标题
                        subtext: '全区一贯制学校' //副标题
                    },
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: { // 坐标轴指示器，坐标轴触发有效
                            type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
                        }
                    },
                    grid: {
                        left: '3%',
                        right: '4%',
                        bottom: '35%',
                        containLabel: true
                    },
                    legend: {
                        data: ['人数']
                    },
                    xAxis: {
                        data: schoolData4,
                        axisLabel:{
                        	interval:0,//间隔
                        	rotate:45,//旋转
                        	margin:2,
                        }
                    },
                    yAxis: {},
                    series: [{
                        name: '人数',
                        type: 'bar',
                        data: numberData4
                    }]
                };
                // 使用刚指定的配置项和数据显示图表。
                myChart1.setOption(option1);
                myChart2.setOption(option2);
                myChart3.setOption(option3);
                myChart4.setOption(option4);
            }
        });
    });
</script>
</html>