<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"   %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>学科教研员图表统计</title>
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
	<script src="<%=request.getContextPath()%>/theme/default/dist/js/pages/dashboard.js"></script>
	<script type="text/javascript" src="../js/echarts/echarts.js"></script>
	<script type="text/javascript" src="../js/echarts/theme/green.js"></script>
	<script type="text/javascript" src="../js/page.common.js"></script>
<style>
    body {
        font-family: 微软雅黑;
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
       font-size:14px;
       margin-left:10px;
    }
    .imm{
      display:block;
      float:left;
      padding:10px 0;
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
        if(length > 0){
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
        if(length > 0){
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
	<div class="l_statis">
		<!-- <div class="l_title02">学科教研员</div> -->
	</div>
	<select id="schoolType" name="schoolType"  style="display: none"  class="form-control"></select>
    <input type="hidden" name="mytasktype" id="mytasktype" value="todo">
    <div class="">
        <div class="">
            <div class="content" style="background-color: #e7fcd9;">
                <div class="row">
                    <div class="col-md-7 connectedSortable ui-sortable" style="width: 50%;">
                        <div class="box box-solid bg-white-gradient">
                            <div class="box-header">
                                <i class="fa fa-calendar"></i>
                                <h3 class="box-title" style="color:#3b5617;font-size: 16px;margin-left:10px;font: 15px 微软雅黑;">通&nbsp;知</h3>
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
                                <h3 class="box-title" style="color:black;font-size: large;">学科教研员平均分统计</h3>
                            </div>
                            <div class="box-body no-padding">
                                <div class="box box-primary">
                                    <div class="box-body">
                                        <div class="l_statis">
											<div class="l_form">
												<div class="l_form01" id="main1" style="width: 480px;height:250px;margin-top: 0px;">
												</div>
											</div>
										</div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-5 connectedSortable" style="width: 50%;">
                        <div class="box box-solid bg-white-gradient">
                            <div class="box-header">
                                <i class="fa fa-calendar"></i>
                                <h3 class="box-title" style="color:#3b5617;font-size: 16px;margin-left:10px;font: 15px 微软雅黑;">公&nbsp;告</h3>
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
                        <!-- <div class="box box-solid bg-white-gradient">
                            <div class="box-header">
                                <i class="fa fa-calendar"></i>
                                <h3 class="box-title" style="color:black;font-size: large;">学科教研员四率统计</h3>
                            </div>
                            <div class="box-body no-padding">
                                <div class="box box-primary">
                                    <div class="box-body">
                                        <div class="l_statis">
											<div class="l_form">
												<div class="l_form01" id="main2" style="width: 480px;height:250px;margin-top: 0px;">
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
    </div>
</body>
<script type="text/javascript">
    var avgNameData1 = [],
        avgData1 = [],
        avgData2=[],
        course,
        siLvNameData=[],
        ylLvData=[],
        llLvData=[],
        yllLvData=[],
        jglLvData=[],
        siLvCourse=[],
        schoolData3=[],
        numberData3=[],
        dataArry3=[],
        avgUrl = "../statisticsAnalysis/districtSubjectInstructor.do?command=getSubjectInstrutorsAvg";
        siLvUrl="../statisticsAnalysis/districtSubjectInstructor.do?command=getSubjectInstrutorsSiLv";
        $(function () {
        
        	
        	
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
							if (periodCode == "5") {
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

        	
        	
            var schoolType=$("#schoolType").find("option:selected").val();
			 $.ajax({
	             url: avgUrl,
	             type: "POST",
	             data: {schoolType:schoolType},
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
	                 cousre=_types[data.course];
	                 // 基于准备好的dom，初始化echarts实例
	                 var myChart1 = echarts.init(document.getElementById("main1"), "green");
	                 // 指定图表的配置项和数据
	                 var option1 = {
	                     title: {
	                         text: "学生"+cousre+"成绩", //主标题
	                         subtext: "学生"+cousre+"成绩平均分" //副标题
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
	                         bottom: '3%',
	                         containLabel: true
	                     },
	                     toolbox: {
	                         feature: {
	                             saveAsImage: {}
	                         }
	                     },
	                     /* X轴 */
	                 xAxis: {
	                     type: 'category',
	                     boundaryGap: false,
	                     data: avgNameData1,
	                     axisTick : {//小标记
			                 show : false  
			             },
			             axisLabel:{
                        	show:false,
                         }
	                 },
	                 
	                 yAxis: {
	                     type: 'value'
	                 },
	                     toolbox: {
	 		                show: true,
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
            
            
			 $.ajax({
	                url: siLvUrl,
	                type: "POST",
	                data: {schoolType:schoolType},
	                dataType: "JSON",
	                beforeSend:function(XMLHttpRequest){
	                    $("#main2").html("<div style='padding-top:110px;padding-left:30px;'><img src='../theme/images/loading.gif'/></div>"); //在后台返回success之前显示loading图标
	                   
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
	                    cousre=_types[data.course];
	                    // 基于准备好的dom，初始化echarts实例
	                    var myChart2 = echarts.init(document.getElementById("main2"), "green");
	                    // 指定图表的配置项和数据
	                    var option2 = {
	                        title: {
	                            text: '学生'+cousre+'四率', //主标题
	                            subtext: '学生'+cousre+'四率显示' //副标题
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
	                        axisTick : {//小标记
			                    show : false  
			                },
	                        axisLabel:{
	                        	/* interval:0,//间隔
	                        	margin:2, */
	                        	show:false,
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
    });
</script>
</html>