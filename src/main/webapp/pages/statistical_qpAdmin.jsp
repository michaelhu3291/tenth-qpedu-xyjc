<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"   %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>区级管理员门户页面</title>
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/theme/default/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/theme/default/dist/css/AdminLTE.min.css">
	<link rel="stylesheet" href="../theme/default/master.css"  />
	<link href="../theme/default/ui.custom.css" rel="stylesheet" />
	<link href="../theme/default/ui.jqgrid.css" rel="stylesheet" />
	<link href="../theme/default/master.css" rel="stylesheet" />
	<link href="../theme/default/font.awesome.css" rel="stylesheet" />
	<link href="../theme/default/ui.chosen.css" rel="stylesheet" />
	<link href="../theme/default/ui.uploadfile.css" rel="stylesheet" />
	<link href="../theme/default/ui.pick.css" rel="stylesheet" />
	<link href="../theme/default/page.common.css" rel="stylesheet" />
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
	<script type="text/javascript" src="../js/util.js"></script>
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
        width:430px;
        cursor:pointer;
    }
    .titl:visited{
        white-space:nowrap;              /*不换行*/
        text-overflow: ellipsis;         /*超出的显示省略号*/
        overflow:hidden;                 /*超出部分隐藏*/
        word-wrap:normal;                /*长单词不换行-兼容ie*/
        display:block;
        width:430px;
        cursor:pointer;
        color:#3B5617;
        
    }
    .Ttitl{
        display:block;
        width:430px;
        cursor:pointer;
        padding-top:10px;
    }
    .riqi{
      float:right;
      margin-top:-20px;    
      font-size:10px;
      color:#B78134;
    }
    .Triqi{
      float:right;    
      font-size:10px;
      color:#B78134;
    }
    .titel{
       height:27px;
       font-size:14px;
       margin-left:10px;
       
    }
    .Ttitel{
       height:27px;
       font-size:14px;
       margin-left:10px;
       padding-top:0;
    }
    .imm{
      display:block;
      float:left;
      padding:10px 0;
      margin-left:5px;
      margin-right:10px;
      margin-top:10px;
    }
    .Timm{
      display:block;
      float:left;
      padding:10px 0;
      margin-left:5px;
      margin-right:10px;
    }
    .dep{
      font-size:10px;
      color: #B78134;      
    }
    
</style>

<script type="text/javascript">

   var examUrl = "../examInfo/examInfoLook.do?command=showExam";                  //考务
   var paperUrl = "../examInfo/examPaperUpload.do?command=showPaper";             //试卷
   var noticeUrl = "../announce/announceManage.do?command=getNotice";             //通知
   var announceUrl = "../announce/announceManage.do?command=getAnnounce";         //公告
   var moreAnnounceUrl = "../announcement/announces.do";
   var moreNoticeUrl = "../announcement/notices.do";
   
    $(function(){
     //最新前几条测试信息
      $.ajax({
         url: examUrl,
         type: "POST",
         data: {},
         dataType: "JSON",
         success:function(data){
         var length = data.length;
         if(length > 0){
            for(var i = 0; i < length; i++){
               var id = data[i].Id;
               var examCode = data[i].Exam_Number;
               var examName = data[i].Exam_Name;
               var examTime = data[i].Introduced_Time;
               var publishCode = data[i].Brevity_Code;
               var publishDept = data[i].School_Short_Name;
               
               if(publishCode == undefined){
                  publishDept = "学业监测中心";
               }
               var d = new Date(examTime);
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
               
            $("#kaowu").append("<div><img class='imm' src='../theme/images/dot01.png' /><li class='titel'>"
                       + "<a class='Ttitl' style='cursor:pointer;' title='"+ examName +"' href='javascript:void(0)'"
                       + "onclick='viewExamInfoInFrameDialog(\""+id+"\",\""+examCode+"\",\""+examName+"\")'>" + examName + "</a></li></div>"
                       + "<div class='Ttitel'><span class='dep'>发布单位："+publishDept+"</span><span class='Triqi'>" + publishdate + "</span></div>");
            }
         }else{
           $("#kaowu").append("<label style='margin-left:10px;font:12px 微软雅黑;'>暂无数据</label>");
         }
           
         }
      });
     //最新前几条试卷信息
      $.ajax({
         url:paperUrl,
         type:"POST",
         data:{},
         dataType:"JSON",
         success:function(data){
         var length = data.length;
         if(length > 0){
            for(var i = 0; i < length; i++){
               var id = data[i].Id;
               var fileName = data[i].FileName;
               var uploadTime = data[i].FileUploadTime;
               var publishCode = data[i].Brevity_Code;
               var publishDept = data[i].School_Short_Name;
               
               if(publishCode == undefined){
                  publishDept = "学业监测中心";
               }
               var files = fileName.split(".");
               var paperName = files[0];
               var d = new Date(uploadTime);
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
               $("#shijuan").append("<div><img class='imm' src='../theme/images/dot01.png' /><li class='titel'>"
                       + "<a class='Ttitl' title='"+ paperName +"' href='javascript:void(0)' onclick='downloadExamPaper(\""+id+"\")'>" + paperName + "</a></li></div>"
                       + "<div class='Ttitel'><span class='dep'>上传单位："+publishDept+"</span><span class='Triqi'>" + publishdate + "</span></div>");
            }
          }else{
             $("#shijuan").append("<label style='margin-left:10px;font:12px 微软雅黑;'>暂无数据</label>");
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


             $("#tongzhi").append("<img class='Timm' src='../theme/images/dot01.png' /><li class='titel'>"
                       + "<a class='titl' title='"+ titleName +"' href='../announceDetail.do?id="+announceId+"' target='_blank'>" + titleName + "</a>"
                       + "<span class='riqi'>" + publishdate + "</span></li>");           
           }
          }else{
             $("#tongzhi").append("<label style='margin-left:10px;font:12px 微软雅黑;'>暂无数据</label>");

          }
        }
      });
   
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

             $("#gonggao").append("<img class='Timm' src='../theme/images/dot01.png' /><li class='titel'>"
                       + "<a class='titl' title='"+ titleName +"' href='../announceDetail.do?id="+noticeId+"' target='_blank'>" + titleName + "</a>"
                       + "<span class='riqi'>" + publishdate + "</span></li>");             
           }
         }else{
             $("#gonggao").append("<label style='margin-left:10px;font:12px 微软雅黑;'>暂无数据</label>");
          }
       }
     }); 
     loadTaskInfo();  //最新前几条申请调动任务
   });
	
	
   //查看更多公告
   function lookMoreAnnounce(){
      window.location.href = moreAnnounceUrl;
   }
   
   //查看更多通知
   function lookMoreNotice(){
     window.location.href = moreNoticeUrl;
   }
   var  submitTransfer=function(id,code,school,name,proposer,applyTime){
      var name = encodeURI(name);
      var proposer = encodeURI(proposer);
	  var lookHistory = "transfer/lookTransferHistory.do?command=init&id=" + id;
	  frameDialog(lookHistory + "&code=" + code + "&school=" + school + "&name=" + name + "&proposer=" + proposer + "&applyTime=" + applyTime,
	    "调动信息详情", {
            mode: "middle",
            resizable: false,
            width: 600,
            height: 200,
            buttons: [{
                text: "关闭",
                icons: {
                    primary: "ui-icon-cancel"
                },
                click: function (ev) {
                    var $this = window.top.$(this);
                    $this.dialog("close");
                    loadTaskInfo();  
                }
            }]
        });
     /* $.ajax({
	        url:"../transfer/studentTransfer.do?command=submitStuOrTeacherTransfer",
	        type:"POST",
	        data:{id:id,code:code,school:school},
	        dataType:"JSON",
	        success : function(data) {
	        	 loadTaskInfo();
	        	 if(data.mess=="success"){
	        		alert("提交成功");
	        		loadTaskInfo();
	        	} 
	       }
	     }); */ 
   };
</script>

</head>
<body class="hold-transition skin-blue " style="background-color: #e7fcd9;">
	<div class="l_statis">
		<!-- <div class="l_title02">教研员</div> -->
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
                                <h3 class="box-title" style="color:#3b5617;font-size: 16px;font: 15px 微软雅黑;">考务管理</h3>
                                <div class="pull-right box-tools">
                                    <div class="pull-right box-tools">
                                       <!-- <a style="cursor:pointer;" onclick="lookMoreAnnounce();">更多>></a> -->
                                    </div>
                                </div>
                            </div>
                            <div class="box-body no-padding">
                                <div class="box box-primary">
                                    <div class="box-body" style="height:180px;" id="kaowu">
                                    
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="box box-solid bg-white-gradient">
                            <div class="box-header">
                                <i class="fa fa-calendar"></i>
                                <h3 class="box-title" style="color:#3b5617;font-size: 16px;font: 15px 微软雅黑;">通&nbsp;知</h3>
                                <div class="pull-right box-tools">
                                    <div class="pull-right box-tools">
                                       <!-- <a style="cursor:pointer;" onclick="lookMoreAnnounce();">更多>></a> -->
                                    </div>
                                </div>
                            </div>
                            <div class="box-body no-padding">
                                <div class="box box-primary">
                                    <div class="box-body" style="height:100px;" id="tongzhi">
                                    
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="box box-solid bg-white-gradient">
                            <div class="box-header" style="background:white;">
                                <!--  <i class="fa fa-calendar"></i>-->
                                <!--  <h3 class="box-title" style="color:black;font-size: large;">教研员平均分统计</h3>-->
                                <!--<h3 class="box-title" style="color:black;font-size: large;">教研员四率统计</h3>-->
                                
                                
		                    <div>		
		                        	<span style="color:black;">学年</span>
		                        <select id="schoolYear" name="schoolYear" class="form-control"  data-dic="{code:'ND'}" style="width: 100px;height:28px;display:inline;"></select>
		                        <span style="color:black;">年级</span>
		                        <select id="grade" name="grade"  class="form-control" data-dic="{code:'nj'}"  style="width: 80px;height:28px;display:inline;">
		                        	 <option value="">选择年级</option>
								</select>
							
								 
		                            <span style="color:black;">科目</span>
		                        <select id="course" name="course"  style="width: 100px;height:28px;display:inline;"  class="form-control" >
		                        	<option value="">选择科目</option>
		                        </select>
		                     
		                        
		                          <span style="color:black;">学期</span>
		                        <select id="term" name="term" class="form-control" style="width: 100px;height:28px;display:inline;" data-dic="{code:'xq'}">
		                        </select>
		                   </div>     
		                   <div style="margin-top:10px;">
		                    
		                   		 <span style="color:black;">测试类型</span>    
		                        <select id="examType" name="examType" class="form-control" style="width: 100px;height:28px;display:inline;" data-dic="{code:'kslx'}">
		                        </select>
		                         <span style="color:black;">学籍状态</span>
		                        <select id="state" name="state" class="form-control" style="width: 100px;height:28px;display:inline;" data-dic="{code:'stuState'}">
		                        	<option value="qb">全部</option>
		                        </select>
		                        
		                      	<span class="toolbar">
									<button id="fastSearchsiLv" class="page-button" title="查询" style="margin-left: 0px;">
										<i class="fa fa-search" ></i>分析
									</button>
								</span>
		                    </div>
                
                            <div id="titleInfo" style="color:#3B5617;font-size:16px;font-weight:bold;margin-top:10px;text-align:center;"></div>    
                         
                            </div>
                            <div class="box-body no-padding">
                                <div class="box box-primary">
                                    <div class="box-body">
                                        <div class="l_statis">
											<div class="l_form">
												<div class="l_form01" id="main1" style="width: 480px;height:250px;margin-top: 0px;">
													<!-- <span>2014年和2015年学生标准分分布情况 </span>
													<div class="l_formnav">
														<img src="../theme/images/form_03.gif">
													</div> -->
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
                                <h3 class="box-title" style="color:#3b5617;font-size: 16px;font: 15px 微软雅黑;">试卷管理</h3>
                                <!-- tools box -->
                                <div class="pull-right box-tools">                                 
                                    <!-- <a style="cursor:pointer;" onclick="lookMoreNotice();">更多>></a> -->                                                                  
                                </div>
                            </div>
                            <div class="box-body no-padding">
                                <div class="box box-primary">
                                    <div class="box-body" style="height:180px;" id="shijuan">
                                    
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="box box-solid bg-white-gradient">
                            <div class="box-header">
                                <i class="fa fa-calendar"></i>
                                <h3 class="box-title" style="color:#3b5617;font-size: 16px;font: 15px 微软雅黑;">公&nbsp;告</h3>
                                <!-- tools box -->
                                <div class="pull-right box-tools">                                 
                                    <!-- <a style="cursor:pointer;" onclick="lookMoreNotice();">更多>></a> -->                                                                  
                                </div>
                            </div>
                            <div class="box-body no-padding">
                                <div class="box box-primary">
                                    <div class="box-body" style="height:100px;" id="gonggao">
                                    
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="box box-solid bg-white-gradient">
                            <div class="box-header">
                                <i class="fa fa-calendar"></i>
                                <h3 class="box-title" style="color:#3b5617;font-size: 16px;font: 15px 微软雅黑;">待办任务</h3>
                                <div class="pull-right box-tools">                                 
                                </div>
                            </div>
                            <div class="box-body no-padding">
                                <div class="box box-primary">
                                    <div class="box-body" style="height:180px;" id="woderengwu">
                                    </div>
                                </div>
                            </div>
                        </div>
                        <!--<div class="box box-solid bg-white-gradient">
                            <div class="box-header">
                                <i class="fa fa-calendar"></i>
                                <h3 class="box-title" style="color:black;font-size: large;">教研员四率统计</h3>
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
                        </div>-->
                    </div>
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
    var avgNameData1 = [],
        avgData1 = [],
        avgData2=[],
        cousre,
        siLvNameData=[],
        ylLvData=[],
        llLvData=[],
        yllLvData=[],
        jglLvData=[],
        avgUrl = "../statisticsAnalysis/districtSubjectInstructor.do?command=getSubjectInstrutorsAvg";
        siLvUrl="../statisticsAnalysis/districtSubjectInstructor.do?command=getSiLvForqpAdmin";
        $(function () {
        	//$("#fastSearchsiLv").button();
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
        	
        	$.ajax({
				type : "POST",
				url : "../dataManage/districtSubjectInstructorInfo.do?command=getPeriodByLoginName",
				data : {},
				dataType : "JSON",
				async : false,
				success : function(data) {
					/*for (var i = 0; i < data.length; i++) {
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
					}*/
				}
			});

        	
        	
            var schoolType=$("#schoolType").find("option:selected").val();
			 /*$.ajax({
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
	                     // X轴 
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
	     });*/
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
	     
	     	var data={};
			var loadSiLv=function(){
				var course = $("#course").find("option:selected").val();
				//schoolType=$("#schoolType").find("option:selected").val();
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
	                data: data,
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
	                    /*$.each(data.data, function (index, item) {
	                    		siLvNameData.push(item.School_Name);
	                    	    ylLvData.push(item.Yl);
	                    	    llLvData.push(item.Ll);
	                    	    yllLvData.push(item.Yll);
	                    	    jglLvData.push(item.Jgl);
	                    	   
	                    });*/
	                    var dataList = data.data;
	                    //如果大于5只取前5个
	                    if(dataList.length > 5){
	                    	for(var i = 0;i < 5;i++){
		                    	siLvNameData.push(dataList[i].School_Name);
	                    	    ylLvData.push(dataList[i].Yl);
	                    	    llLvData.push(dataList[i].Ll);
	                    	    yllLvData.push(dataList[i].Yll);
	                    	    jglLvData.push(dataList[i].Jgl);
		                    }
	                    } else {
	                    	for(var i = 0;i < dataList.length;i++){
		                    	siLvNameData.push(dataList[i].School_Name);
	                    	    ylLvData.push(dataList[i].Yl);
	                    	    llLvData.push(dataList[i].Ll);
	                    	    yllLvData.push(dataList[i].Yll);
	                    	    jglLvData.push(dataList[i].Jgl);
		                    }
	                    }
	                    
	                    //var grade1 = $("#grade").find("option:selected").val();
	                  //加载年级
	                  /*if(grade1 == ""){
	                	  $("#grade option[value != '']").remove();
		                    $.each(data.gradeList,function(index,item){
		                    	$("#grade").append("<option value='"+item.Grade+"'>"+_types[item.Grade]+"</option>")
		                    });
	                  }*/
	                    
	                    cousre=_types[data.course];
	                    var txt = data.schoolYear+"学年"+_types[data.grade]+_types[data.term]+_types[data.examType]+_types[data.course]+'测试';
	                    $("#titleInfo").html(txt);
	                    // 基于准备好的dom，初始化echarts实例
	                    var myChart2 = echarts.init(document.getElementById("main1"), "green");
	                    // 指定图表的配置项和数据
	                    var option2 = {
	                        title: {
	                            text: _types[data.state]+'四率', //主标题
	                            subtext:data.schoolYear+"学年"+_types[data.grade]+_types[data.term]+_types[data.examType]+_types[data.course]+'测试' //副标题
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
</html>