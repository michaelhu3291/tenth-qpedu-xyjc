<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"   %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title></title>
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/theme/default/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/theme/default/dist/css/AdminLTE.min.css">
	<link rel="stylesheet" href="../theme/default/master.css"  />
	

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
  <script src="/js/html5shiv.min.js"></script>
  <script src="/js/respond.min.js"></script>
  <![endif]-->
	<script src="<%=request.getContextPath()%>/js/jquery.js"></script>
	<script src="<%=request.getContextPath()%>/js/jquery-ui/jquery-ui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/ui.jqgrid.js"></script>
	
	<script src="<%=request.getContextPath()%>/theme/default/dist/js/pages/dashboard.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/page.common.js"></script> 
	<script type="text/javascript" src="../js/echarts/echarts.js"></script>
	<script type="text/javascript" src="../js/echarts/theme/green.js"></script>
<style>
    body {
        font-family: 微软雅黑;
        overflow:auto;
        font-size: 12px;
    }
    .titl:link{
        white-space:nowrap;              /*不换行*/
        text-overflow: ellipsis;         /*超出的显示省略号*/
        overflow:hidden;                 /*超出部分隐藏*/
        word-wrap:normal;                /*长单词不换行-兼容ie*/
        display:block;
        width:360px;
        cursor:pointer;
    }
    .titl:visited{
        white-space:nowrap;              /*不换行*/
        text-overflow: ellipsis;         /*超出的显示省略号*/
        overflow:hidden;                 /*超出部分隐藏*/
        word-wrap:normal;                /*长单词不换行-兼容ie*/
        display:block;
        width:360px;
        cursor:pointer;
        color:#3B5617;
    }
    .riqi{
      float:right;
      margin-top:-20px;
    }
    .titel{
       height:27px; 
       margin-left:10px;
    }
    .imm{
      display:block;
      float:left;
      padding:7px 0;
      margin-left:5px;
      margin-right:10px;
    }
</style>

<script type="text/javascript">

   var noticeUrl = "../announce/announceManage.do?command=getNotice";
   var announceUrl = "../announce/announceManage.do?command=getAnnounce";
   var moreAnnounceUrl = "../announcement/announces.do";
   var moreNoticeUrl = "../announcement/notices.do";
   
    $(function(){
   
    //最新前几条公告
     $.ajax({
        url:announceUrl,
        type:"POST",
        data:{},
        dataType:"JSON",
        success : function(data) {
        var length = data.length;
        if(length > 0){     //有公告数据
          for (var i = 0; i < length; i++) {
             var titleName;      //标题
             var publishDate;    //发布时间
             var noticeId = data[i].id;   //公告id
             titleName = data[i].Title;
             publishDate = data[i].PublishDate;
             var d = new Date(publishDate);
             var month = d.getMonth() + 1;
             if(month >= 10){
               month = month;
             }else{
               month = "0" + month;
             }
             var day = d.getDate();
             if(day >= 10){
               day =day;
             }else{
               day = "0" + day;
             }
             var publishdate = month + "-" + day;

             $("#gonggao").append("<img class='imm' src='../theme/images/dot01.png' /><li class='titel'>"
                       + "<a class='titl' title='"+ titleName +"' href='../announceDetail.do?id="+noticeId+"' target='_blank'>" + titleName + "</a>"
                       + "<span class='riqi'>" + publishdate + "</span></li>");
           }
        }else{
             $("#gonggao").append("<label style='margin-left:10px;font:12px 微软雅黑;'>暂无数据</label>");
        }      
       }
     }); 
   
   
       //最新前几条通知
      $.ajax({
        url:noticeUrl,
        type:"POST",
        data:{},
        dataType:"JSON",
        success : function(data) {
        var length = data.length;
        if(length > 0){    //有通知数据
          for (var i = 0; i < length; i++) {
             var titleName;    //标题
             var publishDate;  //发布时间
             var announceId = data[i].id;  //通知id

             titleName = data[i].Title;
             publishDate = data[i].PublishDate;
             
             var d = new Date(publishDate);
             var month = d.getMonth() + 1;
             if(month >= 10){
               month = month;
             }else{
               month = "0" + month;
             }
             var day = d.getDate();
             if(day >= 10){
               day =day;
             }else{
               day = "0" + day;
             }
             var publishdate = month + "-" + day;


             $("#tongzhi").append("<img class='imm' src='../theme/images/dot01.png' /><li class='titel'>"
                       + "<a class='titl' title='"+ titleName +"' href='../announceDetail.do?id="+announceId+"' target='_blank'>" + titleName + "</a>"
                       + "<span class='riqi'>" + publishdate + "</span></li>");
            
          }
        }else{
             $("#tongzhi").append("<label style='margin-left:10px;font:12px 微软雅黑;'>暂无数据</label>");
        }   
       }
     }); 
       
   });
   

   //查看更多公告
   function lookMoreAnnounce(){
      window.location.href = moreAnnounceUrl;
   }
   
   //查看更多通知
   function lookMoreNotice(){
     window.location.href = moreNoticeUrl;
   }
  
  
  
</script>
</head>
<body class="hold-transition skin-blue " style="background-color: #e7fcd9;">
    <!-- <div class="l_statis">
		<div class="l_title02">管理员</div>
	</div> -->
    <input type="hidden" name="mytasktype" id="mytasktype" value="todo">
    <div class="">
        <div class="">
            <div class="content" style="background-color: #e7fcd9;">
                <div class="row">
                    <div class="col-md-7 connectedSortable ui-sortable" style="width: 50%;">
                        <div class="box box-solid bg-white-gradient">
                            <div class="box-header">
                                
                                <h3 class="box-title" style="color:#3b5617;font-size:15px;margin-left:10px;font: 15px 微软雅黑;">考&nbsp;务</h3>
                                <div class="pull-right box-tools">
                                    <!-- <a style="cursor:pointer;" onclick="lookMoreAnnounce();">更多>></a> -->
                                </div>
                            </div>
                            <div class="box-body no-padding">
                               <div class="box box-primary">
                                  <div class="box-body" style="height:180px;" id="tongzhi">
                                   
                                  </div> 
                               </div>
                            </div>
                        </div>
                        <div class="box box-solid bg-white-gradient">
                            <div class="box-header">
                                
                                <h3 class="box-title" style="color:#3b5617;font-size:15px;margin-left:10px;font: 15px 微软雅黑;">通&nbsp;知</h3>
                                <div class="pull-right box-tools">
                                    <!-- <a style="cursor:pointer;" onclick="lookMoreAnnounce();">更多>></a> -->
                                </div>
                            </div>
                            <div class="box-body no-padding">
                               <div class="box box-primary">
                                  <div class="box-body" style="height:180px;" id="tongzhi">
                                   
                                  </div> 
                               </div>
                            </div>
                        </div>
                        <div class="box box-solid bg-white-gradient">
                            <div class="box-header">
                                <i class="fa fa-calendar"></i>
                                <h3 class="box-title" style="color:#3b5617;font-size: 15px;font: 15px 微软雅黑;">测试考纲</h3>
                                <!-- tools box -->
                                <div class="pull-right box-tools" style="display: none;">
                                    <div class="btn-group">
                                        <button type="button" class="btn btn-info btn-sm dropdown-toggle" data-toggle="dropdown">
                                            <i class="fa fa-bars"></i>
                                        </button>
                                        <ul class="dropdown-menu pull-right" role="menu">
                                            <li><a href="#" onclick="addBoard();">创建公告</a>
                                            </li>
                                            <li class="divider"></li>
                                            <li><a href="#" onclick="moreBoard();">查看更多</a>
                                            </li>
                                        </ul>
                                    </div>
                                    <button type="button" class="btn btn-info btn-sm" data-widget="collapse"><i class="fa fa-minus"></i>
                                    </button>
                                    <button type="button" class="btn btn-info btn-sm" data-widget="remove"><i class="fa fa-times"></i>
                                    </button>
                                </div>
                            </div>
                            <div class="box-body no-padding">
                                <div class="box box-primary">
                                    <div class="box-body" style="height:180px;">
                                         <label style="margin-left:10px;font:12px 微软雅黑;">暂无数据</label>
                                    </div>
                                </div>
                            </div>
                        </div>      
                    </div>
                    <div class="col-md-5 connectedSortable" style="width: 50%;">
                        <div class="box box-solid bg-white-gradient">
                            <div class="box-header">
                                
                                <h3 class="box-title" style="color:#3b5617;font-size:15px;margin-left:10px;font: 15px 微软雅黑;">试&nbsp;卷</h3>
                                <!-- tools box -->
                                <div class="pull-right box-tools">
                                    <!-- <a style="cursor:pointer;" onclick="lookMoreNotice();">更多>></a> -->
                                    
                                </div>
                            </div>
                            <div class="box-body no-padding">
                                <div class="box box-primary">
                                    <div class="box-body" style="height:180px;" id="gonggao">
                                    
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="box box-solid bg-white-gradient">
                            <div class="box-header">
                                
                                <h3 class="box-title" style="color:#3b5617;font-size:15px;margin-left:10px;font: 15px 微软雅黑;">公&nbsp;告</h3>
                                <!-- tools box -->
                                <div class="pull-right box-tools">
                                    <!-- <a style="cursor:pointer;" onclick="lookMoreNotice();">更多>></a> -->
                                    
                                </div>
                            </div>
                            <div class="box-body no-padding">
                                <div class="box box-primary">
                                    <div class="box-body" style="height:180px;" id="gonggao">
                                    
                                    </div>
                                </div>
                            </div>
                        </div>
                       
                    </div>
                     
                        
                        <!-- <div class="box box-solid bg-white-gradient">
                            <div class="box-header">
                                <i class="fa fa-calendar"></i>
                                <h3 class="box-title" style="color:black;font-size: large;">学校学生四率统计</h3>
                            </div>
                            <div class="box-body no-padding">
                                <div class="box box-primary">
                                    <div class="box-body">
                                        <div class="l_statis">
											<div class="l_form">
												<div class="l_form01" id="main2" style="width: 480px;height:250px;margin-top: 0px;">
													<span>2014年和2015年学生标准分分布情况 </span>
													<div class="l_formnav">
														<img src="../theme/images/form_03.gif">
													</div>
												</div>
											</div>
										</div>
                                    </div>
                                </div>
                            </div>
                        </div> -->
                    
                    
                    
                </div>
            </div>
        </div>
    </div>


</body>
<script type="text/javascript">
    $.widget.bridge('uibutton', $.ui.button);
    //The Calender
    $("#calendar").datepicker({
        language: 'zh-CN'
    });
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
                $("#main1,#main2").html("<div style='padding-top:110px;padding-left:30px;'><img src='../theme/images/loading.gif'/></div>"); //在后台返回success之前显示loading图标
           	},
            success: function (data) {
                $.each(data.data, function (index, item) {
                	if(item.School_Type_Sequence!=""&&item.School_Type_Sequence==1){
                		schoolData1.push(item.School_Name);
                    	numberData1.push(item.Number);
                	}else if(item.School_Type_Sequence!=""&&item.School_Type_Sequence==2){
                		schoolData2.push(item.School_Name);
                    	numberData2.push(item.Number);
                	}
                });
                // 基于准备好的dom，初始化echarts实例
                var myChart1 = echarts.init(document.getElementById("main1"), "green");
                var myChart2 = echarts.init(document.getElementById("main2"), "green");
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
            }
        });
    });
</script>
</html>
