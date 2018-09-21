<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
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
			</div>
			<div class="l_form01" id="main2" style="width: 508px;height:315px;">
			</div>
			<div class="l_form01" id="main3" style="width: 508px;height:315px;">
			</div>
			<div class="l_form01" id="main4" style="width: 508px;height:315px;">
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
    var avgNameData1 = [],
        avgData1 = [],
        siLvNameData=[],
        ylLvData=[],
        llLvData=[],
        yllLvData=[],
        jglLvData=[],
        schoolData3=[],
        numberData3=[],
        dataArry3=[],
        url = "../statisticsAnalysis/schoolPlainAdminScoreSearch.do?command=getAvgScore";
        siLvUrl="../statisticsAnalysis/subTeaScoreSearch.do?command=getSiLvForSchoolPlainAdmin";
        $(function () {
        	//教导处查询平均分
        	$.ajax({
                url: url,
                type: "POST",
                data: {},
                dataType: "JSON",
                beforeSend:function(XMLHttpRequest){
                    $("#main1").html("<div style='padding-top:110px;padding-left:30px;'><img src='../theme/images/loading.gif'/></div>"); //在后台返回success之前显示loading图标
               	},
                success: function (data) {
                    $.each(data, function (index, item) {
                    		//alert(item.Grade_Id);
                    	    avgNameData1.push(item.Grade_Id);
                    	    avgData1.push(item.Avg_Score);
                    });
                    // 基于准备好的dom，初始化echarts实例
                    var myChart1 = echarts.init(document.getElementById("main1"), "green");
                    // 指定图表的配置项和数据
                    var option1 = {
                        title: {
                            text: '学生学科成绩', //主标题
                            subtext: '学生学科成绩平均分' //副标题
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
                            data: ['平均分']
                        },
                        /* X轴 */
                        
                         xAxis: {
                        data: avgNameData1,
                        axisLabel:{
                        	interval:0,//间隔
                        	//rotate:45,//旋转
                        	margin:2,
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
    				                                 + '<td style="border:solid #add9c0; border-width:0px 1px 1px 0px; padding:10px 0px;">名称</td>'
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
                            name: '平均分',
                            type: 'bar',
                            data: avgData1
                        }]
                    };
                
                
                myChart1.setOption(option1);
            }
        });
            
            
            $.ajax({
                url: siLvUrl,
                type: "POST",
                data: {},
                dataType: "JSON",
                beforeSend:function(XMLHttpRequest){
                    $("#main2").html("<div style='padding-top:110px;padding-left:30px;'><img src='../theme/images/loading.gif'/></div>"); //在后台返回success之前显示loading图标
               	},
                success: function (data) {
                	var itemArr = data.stuCount;
                	$.each(itemArr,function(index,item){
                		ylLvData.push(item.yw_yl);
                		ylLvData.push(item.sx_yl);
                		ylLvData.push(item.yy_yl);
                		ylLvData.push(item.wl_yl);
                		ylLvData.push(item.hx_yl);
                		
                        llLvData.push(item.yw_ll);
                        llLvData.push(item.sx_ll);
                        llLvData.push(item.yy_ll);
                        llLvData.push(item.wl_ll);
                        llLvData.push(item.hx_ll);
                        
                        yllLvData.push(item.yw_yll);
                        yllLvData.push(item.sx_yll);
                        yllLvData.push(item.yy_yll);
                        yllLvData.push(item.wl_yll);
                        yllLvData.push(item.hx_yll);
                        
                        jglLvData.push(item.yw_jkl);
                        jglLvData.push(item.sx_jkl);
                        jglLvData.push(item.yy_jkl);
                        jglLvData.push(item.wl_jkl);
                        jglLvData.push(item.hx_jkl);
                	});
                	
                		
                		
//                     $.each(data.stuCount, function (index, item) {
//                     		siLvNameData.push(item.Class_Id);
//                     	    ylLvData.push(item.Yl);
//                     	    llLvData.push(item.Ll);
//                     	    yllLvData.push(item.Yll);
//                     	    jglLvData.push(item.Jgl);
//                     });
//                     console.info(siLvNameData);
//                     console.info(ylLvData);
//                     console.info(yllLvData);
                    // 基于准备好的dom，初始化echarts实例
                    var myChart2 = echarts.init(document.getElementById("main2"), "green");
                    // 指定图表的配置项和数据ylLvData[0], ylLvData[1], ylLvData[2], ylLvData[3], ylLvData[4]
                    var option2 = {
					    tooltip : {
					        trigger: 'axis',
					        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
					            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
					        }
					    },
					    legend: {
					        data:['优率','良率','优良率','及格率']
					    },
					    grid: {
					        left: '3%',
					        right: '4%',
					        bottom: '3%',
					        containLabel: true
					    },
					    xAxis : [
					        {
					            type : 'category',
					            data : ['语文','数学','外语','物理','化学']
					        }
					    ],
					    yAxis: {
	                    	type:"value",
	                    	min:0,
	                    	max:100,
	                    	axisLabel: {
	                                 formatter: '{value} %'
	                             }
	                    },
					    series : [
					        {
					            name:'优率',
					            type:'bar',
					            
					            data:[ylLvData[0], ylLvData[1], ylLvData[2], ylLvData[3], ylLvData[4]]
					        },
					        {
					            name:'良率',
					            type:'bar',
					            data:[llLvData[0], llLvData[1], llLvData[2], llLvData[3], llLvData[4]]
					        },
					        {
					            name:'优良率',
					            type:'bar',
					            data:[yllLvData[0], yllLvData[1], yllLvData[2], yllLvData[3], yllLvData[4]]
					        },
					        {
					            name:'及格率',
					            type:'bar',
					            data:[jglLvData[0], jglLvData[1], jglLvData[2], jglLvData[3], jglLvData[4]]
					        }
					    ]
					};
                    myChart2.setOption(option2);
            }
        });
        
        //成绩分段学生数
        $.ajax({
            url: "../statisticsAnalysis/schoolPlainAdminScoreSearch.do?command=getIntervalstuCountforSchoolPlainAdmin",
            type: "POST",
            data: {},
            dataType: "JSON",
            beforeSend:function(XMLHttpRequest){
                $("#main3").html("<div style='padding-top:110px;padding-left:30px;'><img src='../theme/images/loading.gif'/></div>"); //在后台返回success之前显示loading图标
           	},
            success: function (data) {
            	var dataArr = data.stuCount;
            	//alert(dataArr.length);
            	$.each(dataArr,function(index,item){
            		schoolData3.push(item.Total_Score);
                	numberData3.push(item.Count);
                	var  data3 = {};
                	data3.value=item.Count;
                	data3.name=item.Total_Score;
                	dataArry3.push(data3);
            	})
            	 // 基于准备好的dom，初始化echarts实例
                var myChart3 = echarts.init(document.getElementById("main3"), "green");
            	
            	var option3 = {
            		    title: {
            		        text: '分数段学生人数'
            		    },
            		    tooltip: {},
            		    legend: {
            		        data: ['六年级', '七年级']
            		    },
            		    radar: {
            		        // shape: 'circle',
            		        indicator: [
            		           { name: '60分以下'},
            		           { name: '61-70'},
            		           { name: '71-80'},
            		           { name: '81-90'},
            		           { name: '91-100'}
            		        ]
            		    },
            		    series: [{
            		        name: '预算 vs 开销（Budget vs spending）',
            		        type: 'radar',
            		        // areaStyle: {normal: {}},
            		        data : [
            		            {
            		                value : [numberData3[0], numberData3[1], numberData3[2], numberData3[3],0],
            		                name : '六年级'
            		            },
            		             {
            		                value : [numberData3[4], numberData3[5], numberData3[6], numberData3[7],0],
            		                name : '七年级'
            		            }
            		        ]
            		    }]
            		};
            	
            	
            	myChart3.setOption(option3);
            }
        });
    });
        
</script>
</html>