<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>   
  <title>测试信息详情</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="../theme/default/ui.custom.css" rel="stylesheet" />
<link href="../theme/default/ui.jqgrid.css" rel="stylesheet" />
<link href="../theme/default/font.awesome.css" rel="stylesheet" />
<link href="../theme/default/ui.chosen.css" rel="stylesheet" />
<link href="../theme/default/ui.uploadfile.css" rel="stylesheet" />
<link href="../theme/default/ui.pick.css" rel="stylesheet" />
<link href="../theme/default/page.common.css" rel="stylesheet" />
<link href="../theme/default/jquery.multiselect.css" rel="stylesheet" />
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
<script type="text/javascript" src='../js/highcharts.js'></script>
<script type="text/javascript" src='../js/util.js'></script>
<script type="text/javascript" src="../js/jquery.multiselect.js"></script>
<script type="text/javascript" src="../js/jquery.multiselect.filter.js"></script>

<script type="text/javascript">
    var loadUrl = "../examInfo/examManage.do?command=selectExamById",
        schoolSelectUrl = "../examInfo/examManage.do?command=selectSchoolByExamCode",
        classAndCourseSelectUrl = "../examInfo/examManageSchool.do?command=getClassIdAndCourse",
        getUserRoleUrl = "../examInfo/examManage.do?command=getUserRole",
        showSelectArrangementByExamUrl = "../examInfo/markingArrangement_xjkw.do?command=showSelectArrangementByExam";

    $(function () {
    
    
        //得到用户的角色
        $.ajax({
            url: getUserRoleUrl,
            type: "POST",
            data: {},
            dataType: "JSON",
            async: false,
            success: function (data) {
                $("#userRole").val(data);
            }
        });
        
        var isqp = false;
        if ($("#userRole").val() == "qpAdmin"||$("#userRole").val() == "instructor" || $("#userRole").val() == "quAdmin") {
        	isqp = true;
		}

        if (isqp) {
            $(".schoolAdmin").hide();
        } else {
            $(".qpAdmin").hide();
            $(".schoolAdmin").show();
        }


        var Id = getUrlParam("id");
        var examNumber = getUrlParam("examNumber");
        var s = examNumber.substring(examNumber.length-4,examNumber.length-3);
        if("1" == s || "9" == s){   //区级发布的考试， 显示学校
            $(".qpAdmin").show();
            $(".schoolAdmin").hide();
        }else{//校级发布的考试 ，显示班级、科目
            $(".schoolAdmin").show();
        }
        $("#examCodeInfo").html(examNumber);
        var datas = {
            examCode: examNumber
        };
        
        var examName = getUrlParam("examName");
        $("#examName").val(examName); 
        $("#showForm").find('input,select').attr("disabled", "disabled");
        $("#showForm").find('input,select').css({
            "background": "#e7fcd9",
            "border": "0"
        });

        $.ajax({
            url: loadUrl,
            type: "POST",
            data: {
                id: Id
            },
            dataType: "JSON",
            success: function (data) {
                   /* var $piel = showSlidePanel(".page-show-panel").find("h4 i").removeClass();
						if ($i.length) {
							$piel.addClass($i.attr("class"));
						} */
						$("#filesList").empty();
						$("#exam_times").val(dateTime(data.exam_time));
	
						$("#exam_names").val(data.exam_Name);
						$("#schoolYears").val(data.school_Year);
						$("#terms").val(_types[data.term]);
						$("#examTypes").val(_types[data.exam_Type]);
						$("#grades").val(_types[data.grade_Code]);
						$("#gradeCode").val(data.grade_Code);
						var introducedTime = data.introduced_Time;
						if (introducedTime != null) {
							$("#introducedTimes").val(dateTime(introducedTime));
						}else{
						    $("#introducedTimes").val(introducedTime);
						}
						
						var closingTime = data.closing_Time;
						if (closingTime != null) {
							$("#closingTimes").val(dateTime(closingTime));
						}else{
						    $("#closingTimes").val(closingTime);
						}
						 var fileStr="";
						 for (var i = 0; i < data.files.length; i++) 
						 {
								 var fileObj = data.files[i];
						         fileStr+="<p><a class='file-name' data-fid=\""+fileObj.id+"\" href='<%=request.getContextPath()%>/platform/accessory_.do?command=download&id="+ fileObj.id+ " '>"+ fileObj.fileName + "</a></p>";
						}
						$("#filesList").append(fileStr);

                if (isqp) {
                    $.ajax({
                        url: schoolSelectUrl,
                        type: "POST",
                        data: datas,
                        dataType: "JSON",
                        success: function (data) {
                            var schoolNames = [];
                            var schoolNum = 1;
                            for (var i = 0; i < data.length; i++) {
                                var schoolName = data[i].School_Name;
                                if (1 < schoolNum && schoolNum <= data.length) {
                                    teacherNames.push(",");
                                }
                                schoolNames.push(schoolName);
                            }
                            $("#schools").append("<span>" + schoolNames + "</span>");
                            $.ajax({
                                url: showSelectArrangementByExamUrl,
                                type: "POST",
                                data: {
                                    gradeId: $("#gradeCode").val(),
                                    examCode: examNumber
                                },
                                dataType: "JSON",
                                success: function (data) {
                                    $("#examCode").val(examNumber);
                                    var str="<tr id=arrangement>"+
											"<th style='width:33px;''>序号</th>"+
											"<th style='font-size: 14px; text-align: center; width:100px;'>科目</th>"+
											"<th style='font-size: 14px; text-align: center; width:75px;'>测试形式</th>"+
											"<th style='font-size: 14px; text-align: center; width:90px;'>测试日期</th>"+
											"<th style='font-size: 14px; text-align: center; width:100px;'>测试时间</th>"+
											"<th style='font-size: 14px; text-align: center; width:75px;'>测试时长<br/>(分钟)</th>"+
											"<th style='font-size: 14px; text-align: center; width:140px;'>阅卷人</th>"+
											"<th style='font-size: 14px; text-align: center; width:90px;'>阅卷日期</th>"+
											"<th style='font-size: 14px; text-align: center; width:100px;'>阅卷时间</th>"+
											"<th style='font-size: 14px; text-align: center; width:95px;'>安排阅卷人截止日期</th>"+
											"<th style='font-size: 14px; text-align: center; width:20%;'>阅卷地点</th>"+
											"</tr>"
                                    $("#showArrangement").append(str);
                                    for (var i = 0; i < data.length; i++) {
                                        if (data[i].Course != null && data[i].Course != "") {
                                            var str;
											if(data[i].schoolSum==data[i].schoolNum){
												var isExistArrangement="allExistArrangement";
												str="共<a href='javascript:void(0)' onclick='queryArrangemet(\""+ data[i].Course
														+ "\",\""
														+$("#examCode").val()
														+ "\",\""
														+$("#examName").val()
														+ "\",\""
														+_types[ data[i].Course]
														+ "\",\""
														+isExistArrangement
														+ "\");'  class='badge'>"+data[i].schoolSum+"</a>所<br/>已全部安排<br/>已安排<span class='badge brightGreen'>"+data[i].teacherCount+"</span>人"
											}else if(data[i].schoolSum==data[i].schoolNotNum){
												var isExistArrangement="allNotExistArrangement";
												str="共<a href='javascript:void(0)' onclick='queryArrangemet(\""+ data[i].Course
														+ "\",\""
														+$("#examCode").val()
														+ "\",\""
														+$("#examName").val()
														+ "\",\""
														+_types[ data[i].Course]
														+ "\",\""
														+isExistArrangement
														+ "\");'  class='badge'>"+data[i].schoolSum+"</a>所<br/>"+
														"暂未安排"
											}else{
												var isExistArrangement="allArrangement",
														existArrangement="existArrangement",
														notExistArrangement="notExistArrangement",
												str="共<a href='javascript:void(0)' onclick='queryArrangemet(\""+ data[i].Course
														+ "\",\""
														+$("#examCode").val()
														+ "\",\""
														+$("#examName").val()
														+ "\",\""
														+_types[ data[i].Course]
														+ "\",\""
														+isExistArrangement
														+ "\");'  class='badge'>"+data[i].schoolSum+"</a>所<br/>"+
														"已安排<a href='javascript:void(0)' onclick='queryExistArrangemet(\""+ data[i].Course
														+ "\",\""
														+$("#examCode").val()
														+ "\",\""
														+$("#examName").val()
														+ "\",\""
														+_types[ data[i].Course]
														+ "\",\""
														+existArrangement
														+ "\");'  class='badge brightGreen'>"+data[i].schoolNum+"</a>所<br/>"+
														"未安排<a href='javascript:void(0)' onclick='queryNotArrangemet(\""+ data[i].Course
														+ "\",\""
														+$("#examCode").val()
														+ "\",\""
														+$("#examName").val()
														+ "\",\""
														+_types[ data[i].Course]
														+ "\",\""
														+notExistArrangement
														+ "\");'  class='badge red'>"+data[i].schoolNotNum+"</a>所<br/>"+
														"已安排<span class='badge brightGreen' style='margin:1px 2px;'>"+data[i].teacherCount+"</span>人"
												}
                                                
                                                
                                            var makingTime="";
												if(data[i].Marking_Start_Time!="" && data[i].Marking_Start_Time!=null){
														makingTime=data[i].Marking_Start_Time+"至"
		                                                    +data[i].Marking_End_Time;
										         }
											var courseTime="";
											    if(data[i].Course_Start_Time!="" && data[i].Course_Start_Time!=null){
														courseTime=data[i].Course_Start_Time+"至"
                                                            +data[i].Course_End_Time;
											    }    
                                                
                                           var arrangementStr = 
                                                 "<tr ><td style='text-align:center; '>"+(i+1)+"</td>"+
                                                 "<td style='text-align:left; width:10%;' >"+_types[data[i].Course]+"</td>"+
                                                 "<td style='text-align:center; width:7%;' >"+data[i].Course_Exam_Type+"</td>"+
                                                 "<td style='text-align:center; width:9%;' >"+data[i].Course_Exam_Time+"</td>"+
                                                 "<td style='text-align:center; width:11%;' >"+courseTime+"</td>"+
                                                 "<td style='text-align:center; width:7%;' >"+data[i].Exam_Length+"</td>"+
                                                 "<td style='text-align:center;  width:16%;'>"+str+"</td>"+
                                                 "<td style='text-align:center; width:9%;' >"+data[i].Marking_Time+"</td>"+
                                                 "<td style='text-align:center; width:11%;' >"+makingTime+"</td>"+
                                                 "<td style='text-align:center; width:10%;' >"+data[i].Marking_End_Date+"</td>"+
                                                 "<td style='text-align:left;  width:30%;'>"+data[i].Marking_Place+"</td></tr>";
											$("#showArrangement").append(arrangementStr);
                                        }
                                         
                                            $('#showArrangement tbody tr:odd').css('background','#f3f3f3');
                          
                                    }
                                }
                            });
                        }
                    });
                } else {
                   if("1" == s || "9" == s){
                      $.ajax({
                        url: schoolSelectUrl,
                        type: "POST",
                        data: datas,
                        dataType: "JSON",
                        success: function (data) {
                            var schoolNames = [];
                            var schoolNum = 1;
                            for (var i = 0; i < data.length; i++) {
                                var schoolName = data[i].School_Name;
                                if (1 < schoolNum && schoolNum <= data.length) {
                                    teacherNames.push("，");
                                }
                                schoolNames.push(schoolName);
                            }
                            $("#schools").append("<span>" + schoolNames + "</span>");
                            $.ajax({
                                url: showSelectArrangementByExamUrl,
                                type: "POST",
                                data: {
                                    gradeId: $("#gradeCode").val(),
                                    examCode: examNumber
                                },
                                dataType: "JSON",
                                success: function (data) {
                                    $("#examCode").val(examNumber);
                                    var str="<tr id=arrangement>"+
											"<th style='width:33px;''>序号</th>"+
											"<th style='font-size: 14px; text-align: center; width:100px;'>科目</th>"+
											"<th style='font-size: 14px; text-align: center; width:75px;'>测试形式</th>"+
											"<th style='font-size: 14px; text-align: center; width:90px;'>测试日期</th>"+
											"<th style='font-size: 14px; text-align: center; width:100px;'>测试时间</th>"+
											"<th style='font-size: 14px; text-align: center; width:75px;'>测试时长<br/>(分钟)</th>"+
											"<th style='font-size: 14px; text-align: center; width:140px;'>阅卷人</th>"+
											"<th style='font-size: 14px; text-align: center; width:90px;'>阅卷日期</th>"+
											"<th style='font-size: 14px; text-align: center; width:100px;'>阅卷时间</th>"+
											"<th style='font-size: 14px; text-align: center; width:95px;'>安排阅卷人截止日期</th>"+
											"<th style='font-size: 14px; text-align: center; width:30%;'>阅卷地点</th>"+
											"</tr>"
                                    $("#showArrangement").append(str);
                                    for (var i = 0; i < data.length; i++) {
                                                        
                                            var makingTime="";
												if(data[i].Marking_Start_Time!="" && data[i].Marking_Start_Time!=null){
														makingTime=data[i].Marking_Start_Time+"至"
		                                                    +data[i].Marking_End_Time;
										         }
											var courseTime="";
											    if(data[i].Course_Start_Time!="" && data[i].Course_Start_Time!=null){
														courseTime=data[i].Course_Start_Time+"至"
                                                            +data[i].Course_End_Time;
											    }    
                                                
                                           var arrangementStr = 
                                                 "<tr><td style='text-align:center; '>"+(i+1)+"</td>"+
                                                 "<td style='text-align:left; width:10%;' >"+_types[data[i].Course]+"</td>"+
                                                 "<td style='text-align:center; width:7%;' >"+data[i].Course_Exam_Type+"</td>"+
                                                 "<td style='text-align:center; width:9%;' >"+data[i].Course_Exam_Time+"</td>"+
                                                 "<td style='text-align:center; width:11%;' >"+courseTime+"</td>"+
                                                 "<td style='text-align:center; width:7%;' >"+data[i].Exam_Length+"</td>"+
                                                 "<td style='text-align:left;  width:16%;'>"+data[i].Teacher_Name+"</td>"+
                                                 "<td style='text-align:center; width:9%;' >"+data[i].Marking_Time+"</td>"+
                                                 "<td style='text-align:center; width:11%;' >"+makingTime+"</td>"+
                                                 "<td style='text-align:center; width:10%;' >"+data[i].Marking_End_Date+"</td>"+
                                                 "<td style='text-align:left;  width:30%;'>"+data[i].Marking_Place+"</td></tr>";
											$("#showArrangement").append(arrangementStr);
											
											$('#showArrangement tbody tr:odd').css('background','#f3f3f3');
                                        }
                                    
                                }
                            });
                        }
                    });
                   }else{                 
                    $.ajax({
                        url: classAndCourseSelectUrl,
                        type: "POST",
                        data: datas,
                        dataType: "JSON",
                        success: function (data) {
                            var courseNames = [];
                            var courseNum = 1;
                            var classNames = [];
                            var classNum = 1;
                            //得到选中的科目
                            for (var i = 0; i < data.courselist.length; i++) {
                                var courseName = _types[data.courselist[i].Course];
                                if (1 < courseNum && courseNum <= data.courselist.length) {
                                    courseNames.push(",");
                                }
                                courseNames.push(courseName);
                            }
                            $("#courses").append("<span>" + courseNames + "</span>");

                            for (var i = 0; i < data.list.length; i++) {
                                var className = _typesClass[data.list[i].Class_Id];
                                if (1 < classNum && classNum <= data.list.length) {
                                    classNames.push(",");
                                }
                                classNames.push(className);
                            }
                            $("#classIds").append("<span>" + classNames + "</span>");
                            $.ajax({
                                url: showSelectArrangementByExamUrl,
                                type: "POST",
                                data: {
                                    gradeId: $("#gradeCode").val(),
                                    examCode: examNumber
                                },
                                dataType: "JSON",
                                success: function (data) {
                                    var str="<tr id=arrangement>"+
													"<th style='width:33px;''>序号</th>"+
													"<th style='font-size: 14px; text-align: center; width:100px;'>科目</th>"+
													"<th style='font-size: 14px; text-align: center; width:70px;'>测试形式</th>"+
													"<th style='font-size: 14px; text-align: center; width:90px;'>测试日期</th>"+
													"<th style='font-size: 14px; text-align: center; width:100px;'>测试时间</th>"+
													"<th style='font-size: 14px; text-align: center; width:65px;'>测试时长<br/>(分钟)</th>"+
													"<th style='font-size: 14px; text-align: center; width:183px;'>阅卷人</th>"+
													"<th style='font-size: 14px; text-align: center; width:90px;'>阅卷日期</th>"+
													"<th style='font-size: 14px; text-align: center; width:100px;'>阅卷时间</th>"+
													"<th style='font-size: 14px; text-align: center; width:95px;'>安排阅卷人截止日期</th>"+
													"<th style='font-size: 14px; text-align: center; width:20%;'>阅卷地点</th>"+
													"</tr>"
													$("#showArrangement").append(str);
												for(var i=0;i<data.length;i++){
													var makingTime="";
													if(data[i].Marking_Start_Time!="" && data[i].Marking_Start_Time!=null){
														makingTime=data[i].Marking_Start_Time+"至"
                                                         +data[i].Marking_End_Time;
													}
												   var courseTime="";
												   if(data[i].Course_Start_Time!="" && data[i].Course_Start_Time!=null){
													   courseTime=data[i].Course_Start_Time+"至"
                                                     +data[i].Course_End_Time;
												   }
											          
														  var arrangementStr = 
	                                                     	   "<tr><td style='text-align:center;'>"+(i+1)+"</td>"+
	                                                     	   "<td style='text-align:left; width:10%'>"+_types[data[i].Course]+"</td>"+
	                                                     	   "<td style='text-align:center; width:7%;' >"+data[i].Course_Exam_Type+"</td>"+
	                                                     	   "<td style='text-align:center; width:9%;' >"+data[i].Course_Exam_Time+"</td>"+
	                                                     	   "<td style='text-align:center; width:11%;' >"+courseTime+"</td>"+
	                                                    	   "<td style='text-align:center; width:7%;' >"+data[i].Exam_Length+"</td>"+
	                                                    	   "<td style='text-align:left;  width:16%;'>"+data[i].Teacher_Name+"</td>"+
	                                                    	   "<td style='text-align:center; width:9%;' >"+data[i].Marking_Time+"</td>"+
	                                                    	   "<td style='text-align:center; width:11%;' >"+makingTime+"</td>"+
                                                               "<td style='text-align:center; width:10%;' >"+data[i].Marking_End_Date+"</td>"+
                                                       	       "<td style='text-align:left;  width:25%;'>"+data[i].Marking_Place+"</td></tr>";
	                                                        $("#showArrangement").append(arrangementStr);
	                                                        
	                                                        $('#showArrangement tbody tr:odd').css('background','#f3f3f3');
													}
                                                      
												}
                                
                            });
                        }
                    });
                   }
                }
            }
        });
    });
    
    function queryArrangemet(course,examCode,examName,courseName,isExistArrangement){
		var queryArrangemetUrl="examInfo/markingArrangementInfo.do?&examCode="
				                                    +examCode+"&course="+course+"&examName="+examName+"&courseName="
				                                    +courseName+"&isExistArrangement="+isExistArrangement;
		   frameDialog(queryArrangemetUrl, "阅卷人安排详情",
				   {mode:"middle",resizable:false,width:660,height:385,buttons:[
	               { text:"关闭", icons:{ primary:"ui-icon-cancel" }, click:function( ev )
	                        {
	                            var $this = window.top.$( this ) ;
	                                 $this.dialog( "close" ) ;
	                         }}
	                ]});
		 
	}
	
	function queryExistArrangemet(course,examCode,examName,courseName,existArrangement){
		var queryArrangemetUrl="examInfo/markingArrangementInfo.do?&examCode="+examCode
				                                    +"&course="+course+"&examName="+examName+"&courseName="
				                                    +courseName+"&existArrangement="+existArrangement;
		   frameDialog(queryArrangemetUrl, "阅卷人安排详情",
				   {mode:"middle",resizable:false,width:660,height:385,buttons:[
	               { text:"关闭", icons:{ primary:"ui-icon-cancel" }, click:function( ev )
	                        {
	                            var $this = window.top.$( this ) ;
	                                 $this.dialog( "close" ) ;
	                         }}
	                ]});
		 
	}
	
	function queryNotArrangemet(course,examCode,examName,courseName,notExistArrangement){
		var queryArrangemetUrl="examInfo/markingArrangementInfo.do?&examCode="+examCode
				                                    +"&course="+course+"&examName="+examName+"&courseName="
				                                    +courseName+"&notExistArrangement="+notExistArrangement;
		   frameDialog(queryArrangemetUrl, "阅卷人安排详情",
				   {mode:"middle",resizable:false,width:660,height:385,buttons:[
	               { text:"关闭", icons:{ primary:"ui-icon-cancel" }, click:function( ev )
	                        {
	                            var $this = window.top.$( this ) ;
	                                 $this.dialog( "close" ) ;
	                         }}
	                ]});
		 
	}
    
</script>
</head>
<body style="overflow-y:scroll;">
    <form id="showForm" style="margin-top: 15px;">
        <div id="show_exam_info">
            <input id="examCode" type="hidden" />
            <input id="examName" type="hidden" />
            <input id="userRole" type="hidden" />
            <table cellspacing="0" border="0" style="width: 94.7%;" class="tableTemplet">
                <thead>
                    <tr>
                        <th colspan="3" style="color: black;">
                        	<i class="fa fa-file-text" style="margin-right: 5px;"></i>
                        	<span>测试信息</span>
                        </th>
                        <th><span>测试编号：</span><span id="examCodeInfo"></span></th>
                    </tr>
                </thead>
                <tr>
                    <td class="label" style="width: 8%">测试日期:</td>
                    <td style="width: 6%">
                        <input type="text" id="exam_times" />
                    </td>
                    <td class="label" style="width: 6%">学年:</td>
                    <td style="width: 15%">
                        <input id="schoolYears" name="schoolYear" />
                    </td>
                </tr>
                <tr>
                    <td class="label" style="width: 6%">年级:</td>
                    <td style="width: 6%">
                        <input id="grades" type="text"></input>
                        <input id="gradeCode" type="hidden"></input>
                    </td>
                    <td class="label" style="width: 6%">学期:</td>
                    <td>
                        <input id="terms" type="text"></input>
                    </td>
                </tr>
                <tr>
                    <td class="label" style="width: 6%">测试类型:</td>
                    <td style="width: 15%" colspan="3">
                        <input id="examTypes" type="text"></input>
                    </td>
                </tr>
                <tr class="qpAdmin">
                    <td class="label" style="width: 6%">学校:</td>
                    <td style="width: 15%" colspan="3">
                        <div id="schools"></div>
                    </td>
                </tr>
                <tr class="schoolAdmin">
                    <td class="label" style="width: 6%">班级:</td>
                    <td style="width: 15%" colspan="3">
                        <div id="classIds"></div>
                    </td>
                </tr>
                <tr class="schoolAdmin">
                    <td class="label" style="width: 6%">科目:</td>
                    <td style="width: 15%" colspan="3">
                        <div id="courses"></div>
                    </td>
                </tr>
                <tr>
                    <td class="label" style="width: 6%">发布日期:</td>
                    <td style="width: 15%">
                        <input type="text" id="introducedTimes" />
                    </td>
                    <td class="label" style="width: 6%">考号生成截止日期:</td>
                    <td style="width: 15%">
                        <input type="text" id="closingTimes" />
                    </td>
                </tr>
                <tr>
                    <td class="label">相关附件：</td>
                    <td id="showFile" colspan="3">
                        <div id="filesList" style="word-break: break-all;"></div>
                    </td>
                </tr>
            </table>
        </div>
        <!-- 阅卷人 -->
        <div id="show_arrangement">
            <table cellspacing="0" border="0" style="width: 94.7%;" class="tableTemplet" id="showArrangement">
                <thead>
                    <tr>
                        <th colspan="7" style="color: black;"><i class="fa fa-file-text" style="margin-right: 5px;"></i>  <span>阅卷人</span>
                        </th>
                    </tr>
                </thead>
                <tbody>
                </tbody>
            </table>
        </div>
    </form>
</body>
</html>
