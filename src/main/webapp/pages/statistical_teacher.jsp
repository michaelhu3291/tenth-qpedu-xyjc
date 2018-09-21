<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"   %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>学校老师门户页面</title>
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

   var examUrl = "../examInfo/examInfoLook.do?command=showExam";
   var paperUrl = "../examInfo/examPaperUpload.do?command=showPaper";
   var noticeUrl = "../announce/announceManage.do?command=getNotice";
   var announceUrl = "../announce/announceManage.do?command=getAnnounce";
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

             $("#gonggao").append("<img class='Timm' src='../theme/images/dot01.png' /><li class='titel'>"
                       + "<a class='titl' title='"+ titleName +"' href='../announceDetail.do?id="+noticeId+"' target='_blank'>" + titleName + "</a>"
                       + "<span class='riqi'>" + publishdate + "</span></li>");            
         }
        }else{
             $("#gonggao").append("<label style='margin-left:10px;font:12px 微软雅黑;'>暂无数据</label>");
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
	<select id="schoolType" name="schoolType"  style="display: none"  class="form-control"></select>
    <input type="hidden" name="mytasktype" id="mytasktype" value="todo">
    <input type="hidden" name="schoolName" id="schoolName" value="">
    <div class="">
        <div class="">
            <div class="content" style="background-color: #e7fcd9;">
                <div class="row">
                    <div class="col-md-7 connectedSortable ui-sortable" style="width: 50%;">
                        <div class="box box-solid bg-white-gradient">
                            <div class="box-header">
                                
                                <h3 class="box-title" style="color:#3b5617;font-size: 16px;margin-left:10px;font: 15px 微软雅黑;">考务管理</h3>
                                <div class="pull-right box-tools">
                                   <!-- <a style="cursor:pointer;" onclick="lookMoreAnnounce();">更多>></a> -->
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
                                
                                <h3 class="box-title" style="color:#3b5617;font-size:15px;margin-left:10px;font: 15px 微软雅黑;">通&nbsp;知</h3>
                                <div class="pull-right box-tools">
                                   <!-- <a style="cursor:pointer;" onclick="lookMoreAnnounce();">更多>></a> -->
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
                                <!--  <h3 class="box-title" style="color:black;font-size: large;">学科教导员四率统计</h3>-->
		                     <div>
		                        <span style="color:black;">学年</span>
		                        <select id="schoolYear" name="schoolYear" class="form-control" style="width: 100px;height:28px;display:inline;">
		                        </select>
		                        <span style="color:black;">年级</span>
		                        <select id="grade" name="grade"  class="form-control" style="width: 80px;height:28px;display:inline;">
								</select>
								<span style="color:black;">科目</span>
		                        <select id="course" name="course"  style="width: 100px;height:28px;display:inline;"  class="form-control" >
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
		                        <input type="hidden" id="schoolName" name="schoolName" value=""/>
		                      <span class="toolbar">
									<button id="fastSearchsiLv" title="查询" class="page-button" style="margin-left: 0px;">
										<i class="fa fa-search"></i>分析
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
												<div class="l_form01" id="main2" style="width: 480px;height:250px;margin-top: 0px;">
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
                                
                                <h3 class="box-title" style="color:#3b5617;font-size: 16px;margin-left:10px;font: 15px 微软雅黑;">试卷管理</h3>
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
                                
                                <h3 class="box-title" style="color:#3b5617;font-size:15px;margin-left:10px;font: 15px 微软雅黑;">公&nbsp;告</h3>
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
            
                        <!-- <div class="box box-solid bg-white-gradient">
                            <div class="box-header">
                                <i class="fa fa-calendar"></i>
                                <h3 class="box-title" style="color:black;font-size: large;">学科教导员四率统计</h3>
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
                        </div>-->
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
        avgUrl = "../statisticsAnalysis/politicalInstructor.do?command=getSubjectInstrutorsAvg";
        siLvUrl="../statisticsAnalysis/scoreSearch.do?command=getSiLvForTeacher";
        $(function () {
        	loadDictionary(function () {
                //显示当前年
                var currentYear = loadSemesterYear();
                $("#schoolYear").append('<option id="schoolYearOption" value="'+currentYear+'">'+currentYear+'</option>');
                $("#term").find("option[value = 'xxq']").attr("selected","selected");
                $("#examType").find("option[value = 'qm']").attr("selected","selected");
            });
        	
            var schoolType=$("#schoolType").find("option:selected").val();
            
          //根据学校code加载年级
	        /*$.ajax({
	               url: "../statisticsAnalysis/schoolPlainAdminScoreSearch.do?command=getSchoolCodeAndSchoolName",
	               type: "POST",
	               data: {},
	               dataType: "JSON",
	               success: function (data) {
               			//学校名称放到隐藏域中schoolName
               			$("#schoolName").val(data.map.School_Name);
	               }
	        });*/
          
	      
	      //查询老师所带的年级和科目
	        $.ajax({
				 url : "../statisticsAnalysis/scoreSearch.do?command=selectGradeForTeacher",
				 type : "POST",
				 data : {},
				 dataType : "JSON",
				 success : function(data) {
				 $("#grade").find("option[value != '']").remove();
				 $("#course").find("option[value != '']").remove();
				 var gradeList = data.gradeList;
				 var courseList = data.courseList;
				 $.each(courseList,function(index,item){
					 $("#course").append("<option value='"+item.Course_Id+"'>"+ _types[item.Course_Id] + "</option>");
				 });
				 $.each(gradeList,function(index,item){
					 $("#grade").append("<option value='"+item+"'>"+ _types[item] + "</option>");
				 });
			     
			}
		});
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
	                 	    avgNameData1.push(_typesClass[item.classId]);
	                 	    avgData1.push(item.qzAvg);
	                 	    avgData2.push(item.qmAvg);
	                 });
	                 // 基于准备好的dom，初始化echarts实例
	                 var myChart1 = echarts.init(document.getElementById("main1"), "green");
	                 // 指定图表的配置项和数据
	                 var option1 = {
	                     title: {
	                         text: _types[data.gradeId]+_types[data.course]+"", //主标题
	                         subtext: $("#term").find("option:selected").text() //副标题 
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
	                     //X轴 
	                 xAxis: {
	                     type: 'category',
	                     boundaryGap: false,
	                     data: avgNameData1,
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
					                                 + '<td style="border:solid #add9c0; border-width:0px 1px 1px 0px; padding:10px 0px;">班级</td>'
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
	     });*/
	
	     	var data={};
			var loadSiLv=function(){
				var course = $("#course").find("option:selected").val();
				//schoolType=$("#schoolType").find("option:selected").val();
				var schoolYear = $("#schoolYear").find("option:selected").val();
				var gradeId = $("#grade").find("option:selected").val();
				var state = $("#state").find("option:selected").val();
				var term = $("#term").find("option:selected").val();
				var examType = $("#examType").find("option:selected").val();
				data={
						course:course,
						gradeId:gradeId,
						state:state,
						schoolYear:schoolYear,
						term:term,
						examType:examType
				}	 
			 //四率首页显示
			 $.ajax({
	                url: siLvUrl,
	                type: "POST",
	                data: data,
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
		                    /*$.each(data.data, function (index, item) {
	                    		siLvNameData.push(_typesClass[item.classId]);
	                    	    ylLvData.push(item.Yl);
	                    	    llLvData.push(item.Ll);
	                    	    yllLvData.push(item.Yll);
	                    	    jglLvData.push(item.Jgl);
	                    	   
	                    });*/
	                    var dataList = data.data;
	                    //如果大于5只取前5个
	                    if(dataList.length > 5){
	                    	for(var i = 0;i < 5;i++){
		                    	siLvNameData.push(_typesClass[dataList[i].classId]);
	                    	    ylLvData.push(dataList[i].Yl);
	                    	    llLvData.push(dataList[i].Ll);
	                    	    yllLvData.push(dataList[i].Yll);
	                    	    jglLvData.push(dataList[i].Jgl);
		                    }
	                    } else {
	                    	for(var i = 0;i < dataList.length;i++){
		                    	siLvNameData.push(_typesClass[dataList[i].classId]);
	                    	    ylLvData.push(dataList[i].Yl);
	                    	    llLvData.push(dataList[i].Ll);
	                    	    yllLvData.push(dataList[i].Yll);
	                    	    jglLvData.push(dataList[i].Jgl);
		                    }
	                    }
		                var t_gradeName = '',t_courseName='';
		                if (data.gradeId!='') {
		                	t_gradeName = _types[data.gradeId];
						}
		                if (data.course!='') {
		                	t_courseName = _types[data.course]
						}
		                console.info(data.course);
		                var txt = data.schoolYear+"学年"+$("#schoolName").val()+t_gradeName+_types[data.term]+_types[data.examType]+t_courseName+"测试";
		                $("#titleInfo").html(txt);
	                    // 基于准备好的dom，初始化echarts实例
	                    var myChart2 = echarts.init(document.getElementById("main2"), "green");
	                    // 指定图表的配置项和数据
	                    var option2 = {
	                        title: {
	                            text: _types[data.state]+"四率", //主标题
	                            subtext: data.schoolYear+"学年"+$("#schoolName").val()+t_gradeName+_types[data.term]+_types[data.examType]+t_courseName+"测试" //副标题
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
					                                 + '<td style="border:solid #add9c0; border-width:0px 1px 1px 0px; padding:10px 0px;">班级</td>'
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