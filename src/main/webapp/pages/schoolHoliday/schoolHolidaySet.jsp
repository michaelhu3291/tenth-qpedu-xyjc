<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="../theme/default/ui.custom.css" rel="stylesheet" />
<link href="../theme/default/ui.jqgrid.css" rel="stylesheet" />
<link href="../theme/default/font.awesome.css" rel="stylesheet" />
<link href="../theme/default/ui.chosen.css" rel="stylesheet" />
<link href="../theme/default/ui.uploadfile.css" rel="stylesheet" />
<link href="../theme/default/ui.pick.css" rel="stylesheet" />
<link href="../theme/default/page.common.css" rel="stylesheet" />
<link href='../theme/default/fullcalendar/fullcalendar.css' rel='stylesheet' />
<link href='../theme/default/fullcalendar/fullcalendar.print.css' rel='stylesheet' media='print' />

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
<script type="text/javascript" src='../js/fullcalendar/moment.min.js'></script>
<script type="text/javascript" src='../js/fullcalendar/fullcalendar.js'></script>
<script type="text/javascript" src='../js/fullcalendar/zh-cn.js'></script>
<script type="text/javascript" src="../js/util.js"></script>
<!--[if IE 7]>
<link href="../theme/default/font.awesome.ie7.css" rel="stylesheet" />
<link href="../theme/default/page.common.ie7.css" rel="stylesheet" />
<![endif]-->
<style>
#show_record  tr td{text-align:center;}
#show_record thead tr td{text-align:center;font-weight:bold;}
#show_record_add  tr td{text-align:center;}
#show_record_add thead tr td{text-align:center;font-weight:bold;}
#dialog-delete table a:hover{background-color:#3B5617;color:white;}
.operateCalendar{
	margin:15px 0px 0px 8px;
  }
.operateCalendar a{
	margin-left:4px;
}
.meeting_but {
	background: #FFFFFF;
	border: 1px solid #3babe3;
	width: auto;
	height: 30px;
	color: #3babe3;
	cursor: pointer;
	line-height: 1px;
}

.meeting_but:hover {
	color: #FFFFFF;
	background: #3babe3;
}

.input_span {
	background: url('../../theme/default/images/unchecked.gif') no-repeat;
	padding: 0px 0px 2px 16px;
	margin-left: .3em;
	color: red;
	margin-top: 6px;
	display: inline;
	font-size: 12px;
}

#calendar {
	position: relative;
	top: 12px;
	margin: 0 12px;
}

#editorForm tbody th {
	color: #FFF;
	background: #36A9E0;
}

#editorForm tbody td {
	background: #FFF;
}

.ui-state-default .ui-icon {
	background-image:
		url('../theme/default/images/ui-icons_454545_256x240.png');
}

.full-choose-dialog .ui-dialog-content iframe, .choose-dialog .ui-dialog-content iframe{
  	width: 100%;
	height: 100%;
	border: none;
  	}
.meeting_but {
	background: #FFFFFF;
	border: 1px solid #3babe3;
	width: auto;
	height: 30px;
	color: #3babe3;
	cursor: pointer;
	line-height: 1px;
}

.meeting_but:hover {
	color: #FFFFFF;
	background: #3babe3;
}

.input_span {
	background: url('../../theme/default/images/unchecked.gif') no-repeat;
	padding: 0px 0px 2px 16px;
	margin-left: .3em;
	color: red;
	margin-top: 6px;
	display: inline;
	font-size: 12px;
}

#calendar {
	position: relative;
	top: 12px;
	margin: 0 12px;
}

#editorForm tbody th {
	color: #FFF;
	background: #36A9E0;
}

#editorForm tbody td {
	background: #FFF;
}

.ui-state-default .ui-icon {
	background-image:
		url('../theme/default/images/ui-icons_454545_256x240.png');
}

.meeting_but {
	background: #FFFFFF;
	border: 1px solid #3babe3;
	width: auto;
	height: 30px;
	color: #3babe3;
	cursor: pointer;
	line-height: 1px;
}

.meeting_but:hover {
	color: #FFFFFF;
	background: #3babe3;
}

.input_span {
	background: url('../../theme/default/images/unchecked.gif') no-repeat;
	padding: 0px 0px 2px 16px;
	margin-left: .3em;
	color: red;
	margin-top: 6px;
	display: inline;
	font-size: 12px;
}

#calendar {
	position: relative;
	top: 12px;
	margin: 0 12px;
}

#editorForm tbody th {
	color: #FFF;
	background: #36A9E0;
}

#editorForm tbody td {
	background: #FFF;
}

.ui-state-default .ui-icon {
	background-image:
		url('../theme/default/images/ui-icons_454545_256x240.png');
}

.meeting_but {
	background: #FFFFFF;
	border: 1px solid #3babe3;
	width: auto;
	height: 30px;
	color: #3babe3;
	cursor: pointer;
	line-height: 1px;
}

.meeting_but:hover {
	color: #FFFFFF;
	background: #3babe3;
}

.input_span {
	background: url('../../theme/default/images/unchecked.gif') no-repeat;
	padding: 0px 0px 2px 16px;
	margin-left: .3em;
	color: red;
	margin-top: 6px;
	display: inline;
	font-size: 12px;
}

#calendar {
	position: relative;
	top: 12px;
	margin: 0 12px;
}

#editorForm tbody th {
	color: #FFF;
	background: #36A9E0;
}

#editorForm tbody td {
	background: #FFF;
}

.ui-state-default .ui-icon {
	background-image:
		url('../theme/default/images/ui-icons_454545_256x240.png');
}

.time-send{
	width: 80px;
	border: 1px solid #aedd99;
}
</style>
<script type="text/javascript">
var listId = "#list2", exportKey="brand";
var saveUrl = "../schoolHoliday/schoolHolidaySet/addSchoolHoliday.do?command=saveHoliday",
	searchUrl = "../schoolHoliday/schoolHolidaySet.do?command=searchHoliday";
var cancelBTN = function() {
	hideSlidePanel(".page-editor-panel");
}

var cancelUpdateBTN = function() {
	hideSlidePanel(".page-editor-panel-update");
}
var cancelDeleteBTN = function() {
	hideSlidePanel(".page-editor-panel-delete");
}
/*
var resizeFun = function()
{
    $( "#tabs" ).tabs( "refresh" ) ;
};*/
var relayout = function () {
    $('#calendar').fullCalendar("option", "height", $("body").height() - 24);
};
$( function(){
	$( "#tabs" ).tabs( { heightStyle:"fill" } ) ;
	$("#calendar").fullCalendar({
		header: {
			left: 'prev,next today',
			center: 'title',
			right: 'month,agendaWeek,agendaDay'
		},
		eventLimit: true,
	    theme:true,
        selectable: false,
		editable: false,                    //是否允许拖拽
		dayClick: function (date, allDay, jsEvent, view) {
		var startDate=date.format("YYYY-MM-DD") ;
		var url="schoolHoliday/schoolHolidaySet/addSchoolHoliday.do?command=init&startDate="+startDate ;
		    frameDialog(url, "新增校历安排", {mode:"middle",resizable:false,width:580,height:380,buttons:[
	        { text:"保存", icons:{ primary:"ui-icon-check" },click:function( ev )
	        {
	        	var $this   = window.top.$( this ),
	        	  dial = $this.find( "iframe" )[0].contentWindow ;
	        	var data = dial.getData() ;
	        	if(data!="" && data!=null){
					POST(saveUrl,data,function(data){
					 $("#calendar").fullCalendar("refetchEvents") ;
					 $this.dialog( "close" );
						window.location.reload();
					});
					
	        	}
	        	
	        }}, 
	        { text:"关闭", icons:{ primary:"ui-icon-cancel" }, click:function( ev )
	        {
	            var $this = window.top.$( this ) ;
	            $this.dialog( "close" ) ;
	        }}
	     ]});
         },
         eventClick:function(calEvent, jsEvent, view){
	        if (calEvent.type=='') {
	        	var url="schoolHoliday/schoolHolidaySet/addSchoolHoliday.do?command=init";
				     frameDialog(url+"&id="+calEvent.id, 
				    		 "修改校历", {mode:"middle",resizable:false,width:480,height:380,buttons:[
			        
			        { text:"保存", icons:{ primary:"ui-icon-check" },click:function( ev )
			         {
			        	var $this   = window.top.$( this ),
		              	 dial = $this.find( "iframe" )[0].contentWindow ;
		                var data = dial.getData();
		                if(data!="" && data!=null){
		                 POST(saveUrl,data,function(data){
		                 	  $("#calendar").fullCalendar("refetchEvents") ;
		                 	 $this.dialog( "close" ) ;
		                    window.location.reload();
		                 });
		                 
		             	} 
			        }},
			        { text:"删除", icons:{ primary:"ui-icon-trash" }, click:function( ev )
				        {
				            var $this = window.top.$( this ) ;
				            dial = $this.find( "iframe" )[0].contentWindow ;
				            var data = dial.getData();
				            window.message({
				                text: "确认要删除该校历吗?",
				                title: "提醒",
				                buttons: {
				                    "确认": function () {
							            POST("../schoolHoliday/schoolHolidaySet/addSchoolHoliday.do?command=deleteHoliday",data,function(data){
					                      	  $("#calendar").fullCalendar("refetchEvents") ;
					                      	 	
									            window.location.reload();
					                     });
							            $this.dialog( "close" ) ;
							            window.top.$(this).dialog("close");
							           
				                    },
				                    "取消": function () {
				                        window.top.$(this).dialog("close");
				                    }
				                }
				            });
				        }}, 
			        { text:"关闭", icons:{ primary:"ui-icon-cancel" }, click:function( ev )
			        {
			            var $this = window.top.$( this ) ;
			            $this.dialog( "close" ) ;
			        }}
			     ]});
			}
         },
         eventDrop: function(event,dayDelta,minuteDelta,allDay,revertFunc) { 
             var data={};
             data.id=event.id;
             data.startTime=event.start.format("YYYY-MM-DD HH:mm:ss");
             data.endTime=event.end.format("YYYY-MM-DD HH:mm:ss");
             POST("../schoolHoliday/schoolHolidaySet/addSchoolHoliday.do?command=submit",data,function(data){
                      if( data.error )
			            {
			                window.Msg.error( data.error, "发生错误" ) ;
			                revertFunc();
			                return ;
			            }
                  	  $("#calendar").fullCalendar("refetchEvents") ;
                  });
	      },
	       events:function( start, end, timezone,callback ) {
	    	 POST(searchUrl,{},function(data){
	    		 var events = [];
	            for(var i =0;i<data.length;i++){
                        var endtiime = new  Date((parseTime(data[i].END_TIME)/1000+86400)*1000);
	            	events.push({
	            		id:data[i].ID,
                        title: data[i].HOLIDAY_NAME,
                        start: dateFormat(data[i].START_TIME,"yyyy-MM-dd"), // will be parsed
                        end: dateFormat(endtiime,"yyyy-MM-dd"),  // will be parsed
                        color  : randomColor(),
                        type: data[i].TYPE,
                    });
	            }
	            callback(events); 
	    	 })
	     },  
	});
	$(".fc-right .fc-button-group").before("<button class='page-button' onclick='toggleView();'>节假日维护</button>");
	$(".fc-left").append("<button class='page-button' id='insertBTN'>添加校历</button>");
	$(".fc-left").append("<button class='page-button' id='updateBTN'>修改校历</button>");
	$(".fc-left").append("<button class='page-button' id='deleteBTN'>删除校历</button>");
	$(".fc-right .fc-button-group").remove();
	
	//添加前查询操作
	$("#insertBTN").click(function(ev){
		var schoolYear = $(".year").html();
		//alert(schoolYear);
		var getStartTimeUrl = "../schoolHoliday/schoolHolidaySet.do?command=getStartTimeBySchoolYear";
		$.ajax({
			url:getStartTimeUrl,
			type:"post",
			dataType: "JSON",
			data:{schoolYear:schoolYear},
			success:function(data){
				var flag = $.trim(data.flag);
				if(flag=="1"){
					window.Msg.alert("选择的学年已经存在！重新选择或到更新页面更新学年信息");
					return;
				}else{
					var startTime = $.trim(data.startTime);
					$("#last_beginTime").val(startTime);
				}
			}
		});
		
		//清空文本框的值
		$("#dialog-confirm input[type='text']").css("height","22px").val("");
		
		$("#show_record_add tbody tr").remove();
		//点击添加按钮时先去数据库把所有添加的数据查出来
		 $.ajax({
   	       	url:"../schoolHoliday/schoolHolidaySet.do?command=selectHoliday",
   	        type: "POST",
   	        data: {},
   	        dataType: "JSON",
   	        success: function(data, xhr) {
   	        	for(var i = 0;i < data.length;i++){
   	        		var prev_term = data[i].last_Begin_Time.substring(0,10)+" — "+data[i].last_End_Time.substring(0,10);
   	        		var winter = data[i].winter_Begin_Time.substring(0,10)+" — "+data[i].winter_End_Time.substring(0,10);
   	        		var next_term = data[i].next_Begin_Time.substring(0,10)+" — "+data[i].next_End_Time.substring(0,10);
   	        		var summer = data[i].summer_Begin_Time.substring(0,10)+" — "+data[i].summer_End_Time.substring(0,10);
   	        		$("#dialog-confirm table:last tbody").append("<tr style='height:30px;'><td style='width:100px;text-align:center'>"+data[i].school_Year+"</td><td style='width:180px;text-align:center'>"+prev_term+"</td><td style='width:180px;text-align:center'>"+winter+"</td><td style='width:180px;text-align:center'>"+next_term+"</td><td style='width:180px;text-align:center'>"+summer+"</td></tr>");
   	        	}
   	        }
   	    });  
		
		var $i = $(ev.currentTarget).find("i"), $piel = $(
		".page-editor-panel").show({
			effect : "slide",
			direction : "up",
			easing : 'easeInOutExpo',
			duration : 900
		});
});
	
	//添加操作
	$("#editorSave").click(function(){
		var last_beginTime = $("#last_beginTime").val();
		  var last_endTime = $("#last_endTime").val();
		  var next_beginTime = $("#next_beginTime").val();
		  var next_endTime = $("#next_endTime").val();
		  
		  var winter_beginTime = $("#winter_beginTime").val();
		  var winter_endTime = $("#winter_endTime").val();
		  var summer_beginTime = $("#summer_beginTime").val();
		  var summer_endTime = $("#summer_endTime").val();
  	  //校验文本框是否为空
  	  if(last_beginTime == '' || last_endTime == '' || next_beginTime == '' || next_endTime == '' || winter_endTime == '' || summer_endTime == ''){
  		 
			 window.message({
                   text: "请输入完整数据!",
                   title: "提示"
               });
			 return;
		}
  	  $.ajax({
  	       	url:"../schoolHoliday/schoolHolidaySet.do?command=addHoliday",
  	        type: "POST",
  	        data: {
  	        	last_beginTime:last_beginTime,
  	        	last_endTime:last_endTime,
  	        	next_beginTime:next_beginTime,
  	        	next_endTime:next_endTime,
  	        	winter_beginTime:winter_beginTime,
  	        	winter_endTime:winter_endTime,
  	        	summer_beginTime:summer_beginTime,
  	        	summer_endTime:summer_endTime
  	        },
  	        dataType: "JSON",
  	        success: function(data, xhr) {
  	        	console.log(data);
  	        	if(data.mess == "success"){
  	        		 window.message({
         	                    text: "保存成功!",
         	                    title: "提示"
         	                });
  	        		 $("#show_record_add tbody tr").remove();
  	        		 //添加成功后再去查询
  	        		 $.ajax({
  	        	   	       	url:"../schoolHoliday/schoolHolidaySet.do?command=selectHoliday",
  	        	   	        type: "POST",
  	        	   	        data: {},
  	        	   	        dataType: "JSON",
  	        	   	        success: function(data, xhr) {
  	        	   	        	for(var i = 0;i < data.length;i++){
  	        	   	        		var prev_term = data[i].last_Begin_Time.substring(0,10)+" — "+data[i].last_End_Time.substring(0,10);
  	        	   	        		var winter = data[i].winter_Begin_Time.substring(0,10)+" — "+data[i].winter_End_Time.substring(0,10);
  	        	   	        		var next_term = data[i].next_Begin_Time.substring(0,10)+" — "+data[i].next_End_Time.substring(0,10);
  	        	   	        		var summer = data[i].summer_Begin_Time.substring(0,10)+" — "+data[i].summer_End_Time.substring(0,10);
  	        	   	        		$("#dialog-confirm table:last tbody").append("<tr style='height:30px;'><td style='width:100px;text-align:center'>"+data[i].school_Year+"</td><td style='width:180px;text-align:center'>"+prev_term+"</td><td style='width:180px;text-align:center'>"+winter+"</td><td style='width:180px;text-align:center'>"+next_term+"</td><td style='width:180px;text-align:center'>"+summer+"</td></tr>");
  	        	   	        	}
  	        	   	        }
  	        	   	    });  
  	        	}
  	        }
  	    }); 
		
	});
	
	//修改前查询
	$("#updateBTN").click(function(ev){
		$("#dialog-update input[type='text']").css("height","22px");
		$("#show_record tbody tr").remove();
		//点击修改按钮时先去数据库把最新添加的数据查出来
		 $.ajax({
   	       	url:"../schoolHoliday/schoolHolidaySet.do?command=selectHoliday",
   	        type: "POST",
   	        data: {},
   	        dataType: "JSON",
   	        success: function(data, xhr) {
   	        	//alert(data.length);
   	        	$(".school_year").text(data[0].school_Year);
   	        	$("#last_beginTime_update").val(data[0].last_Begin_Time.substring(0,10));
   	        	$("#last_endTime_update").val(data[0].last_End_Time.substring(0,10));
   	        	$("#next_beginTime_update").val(data[0].next_Begin_Time.substring(0,10));
   	        	$("#next_endTime_update").val(data[0].next_End_Time.substring(0,10));
   	        	
   	        	$("#winter_beginTime_update").val(data[0].winter_Begin_Time.substring(0,10));
   	        	$("#winter_endTime_update").val(data[0].winter_End_Time.substring(0,10));
   	        	$("#summer_beginTime_update").val(data[0].summer_Begin_Time.substring(0,10));
   	        	$("#summer_endTime_update").val(data[0].summer_End_Time.substring(0,10));
   	        	$("#hidden_id").val(data[0].id);
   	        	//if(data.length < 2){
   	        	//	$("#show_record").hide();
   	        	//}
   	        	//else{
   	        		//$("#show_record").();
   	        		for(var i = 1;i < data.length;i++){
   	   	        		var prev_term = data[i].last_Begin_Time.substring(0,10)+" — "+data[i].last_End_Time.substring(0,10);
   	   	        		var winter = data[i].winter_Begin_Time.substring(0,10)+" — "+data[i].winter_End_Time.substring(0,10);
   	   	        		var next_term = data[i].next_Begin_Time.substring(0,10)+" — "+data[i].next_End_Time.substring(0,10);
   	   	        		var summer = data[i].summer_Begin_Time.substring(0,10)+" — "+data[i].summer_End_Time.substring(0,10);
   	   	        		$("#dialog-update table:last tbody").append("<tr style='height:30px;'><td style='width:100px;text-align:center'>"+data[i].school_Year+"</td><td style='width:180px;text-align:center'>"+prev_term+"</td><td style='width:180px;text-align:center'>"+winter+"</td><td style='width:180px;text-align:center'>"+next_term+"</td><td style='width:180px;text-align:center'>"+summer+"</td></tr>");
   	   	        	}
   	        		
   	        	//}
   	        	
   	        }
   	    });
		
		 var $i = $(ev.currentTarget).find("i"), $piel = $(
			".page-editor-panel-update").show({
				effect : "slide",
				direction : "up",
				easing : 'easeInOutExpo',
				duration : 900
			});
	});
	
	//修改操作
	$("#editorUpdate").click(function(){
		 var last_beginTime = $("#last_beginTime_update").val();
		  var last_endTime = $("#last_endTime_update").val();
		  var next_beginTime = $("#next_beginTime_update").val();
		  var next_endTime = $("#next_endTime_update").val();
   	  //校验文本框是否为空
   	  if(last_beginTime == '' || last_endTime == '' || next_beginTime == '' || next_endTime == ''){
   		 
			 window.Msg.alert("请输入完整数据！");
			 return;
		}
   	  $.ajax({
   	       	url:"../schoolHoliday/schoolHolidaySet.do?command=updateHoliday",
   	        type: "POST",
   	        data: {
   	        	last_beginTime:last_beginTime,
   	        	last_endTime:last_endTime,
   	        	next_beginTime:next_beginTime,
   	        	next_endTime:next_endTime,
   	        	
   	        	winter_beginTime_update:$("#winter_beginTime_update").val(),
   	        	winter_endTime_update:$("#winter_endTime_update").val(),
   	        	summer_beginTime_update:$("#summer_beginTime_update").val(),
   	        	summer_endTime_update:$("#summer_endTime_update").val(),
   	        	hidden_id:$("#hidden_id").val()
   	        },
   	        dataType: "JSON",
   	        success: function(data, xhr) {
   	        	if(data.mess == "success"){
   	        		window.Msg.alert("修改成功！")
   	        	}
   	        	//$("#updateBTN").show();
   	        	//hideSlidePanel(".page-editor-panel-update");
   	        	
   	        }
   	    });
		
	});
	
	//删除前查询(下拉)
	
	$("#deleteBTN").click(function(ev){
		//$(this).hide();
		$("#show_record_delete tbody tr").remove();
		 $.ajax({
	   	       	url:"../schoolHoliday/schoolHolidaySet.do?command=selectHoliday",
	   	        type: "POST",
	   	        data: {},
	   	        dataType: "JSON",
	   	        success: function(data, xhr) {
	   	        	for(var i = 0;i < data.length;i++){
	   	        		var prev_term = data[i].last_Begin_Time.substring(0,10)+" — "+data[i].last_End_Time.substring(0,10);
	   	        		var winter = data[i].winter_Begin_Time.substring(0,10)+" — "+data[i].winter_End_Time.substring(0,10);
	   	        		var next_term = data[i].next_Begin_Time.substring(0,10)+" — "+data[i].next_End_Time.substring(0,10);
	   	        		var summer = data[i].summer_Begin_Time.substring(0,10)+" — "+data[i].summer_End_Time.substring(0,10);
	   	        		$("#dialog-delete table tbody").append("<tr style='height:30px;'><td style='width:100px;text-align:center'>"+data[i].school_Year+"</td><td style='width:180px;text-align:center'>"+prev_term+"</td><td style='width:180px;text-align:center'>"+winter+"</td><td style='width:180px;text-align:center'>"+next_term+"</td><td style='width:180px;text-align:center'>"+summer+"</td><td style='width:100px;text-align:center'><a  class='btn_edit ui-state-default' style='cursor:pointer;padding:2px;padding-right:8px;'  onclick='delCalendar(\"" + data[i].id + "\");'><i class='fa fa-times'></i>删除</a></td></tr>");
	   	        	}
	   	        }
	   	    });
		 var $i = $(ev.currentTarget).find("i"), $piel = $(
			".page-editor-panel-delete").show({
				effect : "slide",
				direction : "up",
				easing : 'easeInOutExpo',
				duration : 900
			});
	});
	
	//删除前查询(弹窗)
	/*$("#dialog-delete").dialog({autoOpen:false});
	$("#deleteBTN").click(function(){
		$("#show_record_delete tbody tr").remove();
		 $.ajax({
	   	       	url:"../schoolHoliday/schoolHolidaySet.do?command=selectHoliday",
	   	        type: "POST",
	   	        data: {},
	   	        dataType: "JSON",
	   	        success: function(data, xhr) {
	   	        	for(var i = 0;i < data.length;i++){
	   	        		var prev_term = data[i].last_Begin_Time.substring(0,10)+" — "+data[i].last_End_Time.substring(0,10);
	   	        		var winter = data[i].winter_Begin_Time.substring(0,10)+" — "+data[i].winter_End_Time.substring(0,10);
	   	        		var next_term = data[i].next_Begin_Time.substring(0,10)+" — "+data[i].next_End_Time.substring(0,10);
	   	        		var summer = data[i].summer_Begin_Time.substring(0,10)+" — "+data[i].summer_End_Time.substring(0,10);
	   	        		$("#dialog-delete table tbody").append("<tr><td style='width:100px;text-align:center'>"+data[i].school_Year+"</td><td style='width:180px;text-align:center'>"+prev_term+"</td><td style='width:180px;text-align:center'>"+winter+"</td><td style='width:180px;text-align:center'>"+next_term+"</td><td style='width:180px;text-align:center'>"+summer+"</td><td style='width:100px;text-align:center'><span class='btn_edit ui-state-default'  onclick='delCalendar(\"" + data[i].id + "\");'>删除</span></td></tr>");
	   	        	}
	   	        }
	   	    });
		 
		 $("#dialog-delete").dialog({
				modal:true,
				autoOpen:true,
				resizable:false,
				width:1000,
				height:500,
				 buttons: {
				      "取消": function() { 
				       $(this).dialog("close"); 
				      } 
				     }
			});
		 
	});*/
	_initFormControls();//from page.common.js
	_initValidateForXTypeForm(editorFormId);
	
	 
	 $(window).resize(relayout);
     $(window).trigger("resize");
     resizeFun();
     $("#allChoose").change(function(){//全选
    	 if($("#allChoose").prop("checked")){
    		 $(".tableTemplet tr:gt(0)").each(function(index,tr){
    			 $(tr).find("input:checkbox").each(function(){
	 	 				$(this).prop("checked",true);
	 	 			});
    		 });
    	 }else{
    		 $(".tableTemplet tr:gt(0)").each(function(index,tr){
    			 $(tr).find("input:checkbox").each(function(){
	 	 				$(this).prop("checked",false);
	 	 			});
    		 });
    	 }
		});
	//$( "#tabs" ).tabs( { heightStyle:"fill" } ) ;
	//显示当前年
    var year = new Date().getFullYear();
    var month = new Date().getMonth() + 1;
    var currentYear = "";
    if (month < 9) {
        currentYear = (year - 1) + "-" + year;
    } else {
        currentYear = year + "-" + (year + 1);
    }
    $(".year").html(currentYear);
	
	$(".datepicker").datepicker({
		dateFormat:"yy-mm-dd",
		dayNamesMin:["日","一","二","三","四","五","六"],
		monthNames:["一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月"],
		changeYear:true,
		changeMonth:true,
	});
	//判断上学期结束时间是否小于开始时间
	$("#last_endTime,#last_beginTime").change(function(){
		//alert("结束时间不能小于开始时间!");
		//上学期开始时间
		var lastEndTime = $("#last_endTime").val();
		var last_startTime = $("#last_beginTime").val();
		if(last_startTime == ''){
			window.Msg.alert("上学期开始时间不能为空！");
			return;
		}
		if(parseInt(last_startTime.substring(0,4)) < new Date().getFullYear()){
			window.Msg.alert("上学期开始时间不能小于当前年份！");
			$("#last_beginTime").val("");
			return;
		}
		
		var addOneDayUrl = "../schoolHoliday/schoolHolidaySet.do?command=addOneDay";
			$.ajax({
				url:addOneDayUrl,
				type:"post",
				dataType: "JSON",
				data:{Time:lastEndTime},
				success:function(data){
					var addedTime = $.trim(data.addedTime);
					$("#winter_beginTime").val(addedTime);
				}
			});
		var start_year = parseInt(last_startTime.substring(0,last_startTime.indexOf("-")));
		var start_month = parseInt(last_startTime.substring(last_startTime.indexOf("-")+1,last_startTime.lastIndexOf("-")));
		var start_day = parseInt(last_startTime.substring(last_startTime.lastIndexOf("-")+1));
		//alert(start_year+":" + start_month+":" + start_day);
		//上学期结束时间
		var last_endTime = $("#last_endTime").val();
		var end_year = parseInt(last_endTime.substring(0,last_endTime.indexOf("-")));
		var end_month = parseInt(last_endTime.substring(last_endTime.indexOf("-")+1,last_endTime.lastIndexOf("-")));
		var end_day = parseInt(last_endTime.substring(last_endTime.lastIndexOf("-")+1));
		//alert(end_year+":" + end_month+":" + end_day);
		if(last_startTime != '' && last_endTime != ''){
			if(end_year < start_year){
				window.Msg.alert("结束时间不能小于开始时间！");
				 $("#last_endTime").val("");
				return;
			} else if (end_year == start_year){
				if(end_month < start_month){
					window.Msg.alert("结束时间不能小于开始时间！");
					 $("#last_endTime").val("");
					return;
				} else if (end_month == start_month){
					if(end_day < start_day){
						window.Msg.alert("结束时间不能小于开始时间！");
						$("#last_endTime").val("");
						return;
					}
				}
			}
		}
		
	});
	
	//判断下学期结束时间是否小于开始时间
	$("#next_beginTime,#next_endTime").change(function(){
		//alert("结束时间不能小于开始时间!");
		//下学期开始时间
		if($("#last_beginTime").val() == '' || $("#last_endTime").val() == '' ){
			window.Msg.alert("请把上学期开学时间填写完整！");
			$("#next_beginTime").val("");
			return;
		}
		//上学期结束时间
		var last_endTime = $("#last_endTime").val();
		var last_year = parseInt(last_endTime.substring(0,last_endTime.indexOf("-")));
		var last_month = parseInt(last_endTime.substring(last_endTime.indexOf("-")+1,last_endTime.lastIndexOf("-")));
		var last_day = parseInt(last_endTime.substring(last_endTime.lastIndexOf("-")+1));
		//下学期开始时间
		var next_startTime = $("#next_beginTime").val();
		var start_year = parseInt(next_startTime.substring(0,next_startTime.indexOf("-")));
		var start_month = parseInt(next_startTime.substring(next_startTime.indexOf("-")+1,next_startTime.lastIndexOf("-")));
		var start_day = parseInt(next_startTime.substring(next_startTime.lastIndexOf("-")+1));
		//判断下学期开始时间是否小于上学期结束时间
		if(last_endTime != '' && next_startTime != ''){
			if(start_year < last_year){
				window.Msg.alert("下学期开始时间不能小于上学期结束时间");
				$("#next_beginTime").val("");
				return;
			} else if (start_year == last_year){
				if(start_month < last_month){
					window.Msg.alert("下学期开始时间不能小于上学期结束时间！");
					$("#next_beginTime").val("");
					return;
				} else if (start_month == last_month){
					if(start_day < last_day){
						window.Msg.alert("下学期开始时间不能小于上学期结束时间！");
						$("#next_beginTime").val("");
						return;
					}
				}
			}
		}
		//alert(start_year+":" + start_month+":" + start_day);
		//下学期结束时间
		var next_endTime = $("#next_endTime").val();
		var end_year = parseInt(next_endTime.substring(0,next_endTime.indexOf("-")));
		var end_month = parseInt(next_endTime.substring(next_endTime.indexOf("-")+1,next_endTime.lastIndexOf("-")));
		var end_day = parseInt(next_endTime.substring(next_endTime.lastIndexOf("-")+1));
		if(next_startTime != '' && next_endTime != ''){
			if(end_year < start_year){
				window.Msg.alert("结束时间不能小于开始时间！");
				$("#next_endTime").val("");
				return;
			} else if (end_year == start_year){
				if(end_month < start_month){
					window.Msg.alert("结束时间不能小于开始时间！");
					$("#next_endTime").val("");
					return;
				} else if (end_month == start_month){
					if(end_day < start_day){
						window.Msg.alert("结束时间不能小于开始时间！");
						$("#next_endTime").val("");
						return;
					}
				}
			}
		}
		
	});
	
	/* 选择寒假结束时间触发事件，提示下半学年开始时间 */
	$("#winter_endTime").change(function(){
		var winter_endTime = $("#winter_endTime").val();
		var winter_beginTime = $("#winter_beginTime").val();
		if(winter_endTime<=winter_beginTime){
			window.Msg.alert("结束时间应大于开始时间！");
			$("#winter_endTime").val("");
			return;
		}
		var addOneDayUrl = "../schoolHoliday/schoolHolidaySet.do?command=addOneDay";
		$.ajax({
			url:addOneDayUrl,
			type:"post",
			dataType: "JSON",
			data:{Time:winter_endTime},
			success:function(data){
				var addedTime = $.trim(data.addedTime);
				$("#next_beginTime").val(addedTime);
			}
		});
	});
	
	/*校验暑假结束时间不能小于开始时间*/
	$("#summer_endTime").change(function(){
		var summer_endTime = $("#summer_endTime").val();
		var summer_beginTime = $("#summer_beginTime").val();
		if(summer_endTime<=summer_beginTime){
			window.Msg.alert("结束时间应大于开始时间！");
			$("#summer_endTime").val("");
			return;
		}
	});
	
	/* 选择下半学年结束时间触发事件，提示暑假开始时间 */
	$("#next_endTime").change(function(){
		var next_endTime = $("#next_endTime").val();
		var next_beginTime = $("#next_beginTime").val();
		if(next_endTime<=next_beginTime){
			window.Msg.alert("结束时间应大于开始时间！");
			$("#next_endTime").val("");
			return;
		}
		var addOneDayUrl = "../schoolHoliday/schoolHolidaySet.do?command=addOneDay";
		$.ajax({
			url:addOneDayUrl,
			type:"post",
			dataType: "JSON",
			data:{Time:next_endTime},
			success:function(data){
				var addedTime = $.trim(data.addedTime);
				$("#summer_beginTime").val(addedTime);
			}
		});
	});
	
	/* 选择学年触发事件，提示上半学年开始日期 */
	/*$("#last_beginTime").change(function(){
		var schoolYear = $(".year").html();
		//alert(schoolYear);
		var getStartTimeUrl = "../schoolHoliday/schoolHolidaySet.do?command=getStartTimeBySchoolYear";
		$.ajax({
			url:getStartTimeUrl,
			type:"post",
			dataType: "JSON",
			data:{schoolYear:schoolYear},
			success:function(data){
				var flag = $.trim(data.flag);
				if(flag=="1"){
					window.Msg.alert("选择的学年已经存在！重新选择或到更新页面更新学年信息");
				}else{
					var startTime = $.trim(data.startTime);
					$("#last_beginTime").val(startTime);
				}
			}
		});
	});*/
	
});
//生成随机颜色
function randomColor() {  
    var rand = "#"+("00000"+((Math.random()*16777215+0.5)>>0).toString(16)).slice(-6);  
        return rand;  
};

var toggleView=function(){
	 $("#calendar").hide();
	 $("#tabs").show();
    $( "#tabs" ).tabs( "refresh" ) ;
}
var back=function(){
	 //$("#calendar").fullCalendar("refetchEvents") ;
	 $("#calendar").show();
	 $("#tabs").hide();
	// loadCalendarData();
}

//删除校历
function delCalendar(id){
	window.message({
		title:'提醒',
		text:'确定删除此校历吗?',
		buttons:{
			'确定':function(){
				window.top.$(this).dialog("close");
				 $.ajax({
			   	       	url:"../schoolHoliday/schoolHolidaySet.do?command=deleteHoliday",
			   	        type: "POST",
			   	        data: {id:id},
			   	        dataType: "JSON",
			   	        success: function(data, xhr) {
			   	        	if(data.mess == 'success'){
			   	        		window.Msg.alert("删除成功!");
			   	        	}
			   	         $("#show_record_delete tbody tr").remove();
			   	        // 删除成功后查询
			   	      $.ajax({
	        	   	       	url:"../schoolHoliday/schoolHolidaySet.do?command=selectHoliday",
	        	   	        type: "POST",
	        	   	        data: {},
	        	   	        dataType: "JSON",
	        	   	        success: function(data, xhr) {
	        	   	        	for(var i = 0;i < data.length;i++){
	        	   	        		var prev_term = data[i].last_Begin_Time.substring(0,10)+" — "+data[i].last_End_Time.substring(0,10);
	        	   	        		var winter = data[i].winter_Begin_Time.substring(0,10)+" — "+data[i].winter_End_Time.substring(0,10);
	        	   	        		var next_term = data[i].next_Begin_Time.substring(0,10)+" — "+data[i].next_End_Time.substring(0,10);
	        	   	        		var summer = data[i].summer_Begin_Time.substring(0,10)+" — "+data[i].summer_End_Time.substring(0,10);
	        	   	        		$("#dialog-delete table tbody").append("<tr style='height:30px;'><td style='width:100px;text-align:center'>"+data[i].school_Year+"</td><td style='width:180px;text-align:center'>"+prev_term+"</td><td style='width:180px;text-align:center'>"+winter+"</td><td style='width:180px;text-align:center'>"+next_term+"</td><td style='width:180px;text-align:center'>"+summer+"</td><td style='width:100px;text-align:center'><a  class='btn_edit ui-state-default'  style='cursor:pointer;padding:2px;padding-right:8px;'  onclick='delCalendar(\"" + data[i].id + "\");'><i class='fa fa-times'></i>删除</a></td></tr>");
	        	   	        	}
	        	   	        }
	        	   	    });  
			   	       }
			   	    });  
			},
			'取消':function(){
				window.top.$(this).dialog("close");
			}
		}		
	});
}


</script>
</head>
<body style="overflow: auto;">
 <!-- <div class="operateCalendar">
 	 <a  role="button" class="page-button" id='insertBTN'>添加校历</a>
 	 <a  role="button" class="page-button" id='updateBTN'>修改校历</a>
 	 <a  role="button" class="page-button" id='deleteBTN'>删除校历</a>
 </div> -->
  <div id="tabs" class="frametab" style="display: none;">
  	
    <ul>
      <li><a href="#tabs-1">节假日维护</a></li>
      <li style="float: right;margin-right: 38px;">
        <button onclick="back();" class="page-button">
			<i class="fa fa-undo"></i>返回校历
		</button>
	 </li>
    </ul>
    <div id="tabs-1"><iframe marginheight="0" marginwidth="0" frameborder="0" src="../schoolHoliday/schoolHolidaySet/holidayTab.do" style="width:100%;height:100%"></iframe></div>
  </div>
   <div id='calendar'>
  			<div class="page-view-panel full-drop-panel"></div>
  	</div>
	
	<div class="page-editor-panel full-drop-panel">
	
		<div class="title-bar">
			<h4>
				<i class="fa fa-plus"></i>
			</h4>
			<div class="btn-area">
				<div style="margin-top: 4px;">
					<button id="editorSave" class="page-button">
						<i class="fa fa-check"></i>保存
					</button>
					<button id="cancelSaveBTN" class="page-button" onclick="cancelBTN()">
						<i class="fa fa-times"></i>取消
					</button>
				</div>
			</div>
		</div>
		<div class="page-content">
			<div class="page-inner-content">
				<form id="editorForm">
					<input name="id" type="hidden" />
					<!-- 添加校历 -->
  	 
				  	<div id="dialog-confirm" title="新增校历">
					  <table style="padding-left:20px;margin-left:210px;">
					  	<thead>
						  	<tr>
						  		<th style="border:0px;text-align:center;font-size:16px;">学年</th>
						  		<th style="border:0px;text-align:center;font-size:16px;">上学期</th>
						  		<th style="border:0px;text-align:center;font-size:16px;">寒假</th>
						  		<th style="border:0px;text-align:center;font-size:16px;">下学期</th>
						  		<th style="border:0px;text-align:center;font-size:16px;">暑假</th>
						  	</tr>
					  	</thead>
					  	<tbody>
						  	<tr>
						  		<td><span class="year"></span></td>
						  		<td>
						  			<input type="hidden" name="hide_blur"/>
						  			开始时间：<input type="text" class="datepicker time-send" id="last_beginTime" name="last_beginTime" /><br/><br/>
						  			结束时间：<input type="text" class="datepicker time-send" id="last_endTime" name="last_endTime"/>
						  		</td>
						  		<td>
						  			开始时间：<input type="text" class="datepicker time-send" id="winter_beginTime" name="winter_beginTime" /><br/><br/>
						  			结束时间：<input type="text" class="datepicker time-send" id="winter_endTime" name="winter_endTime"/>
						  		</td>
						  		<td>
						  			开始时间：<input type="text" class="datepicker time-send" id="next_beginTime" name="next_beginTime" /><br/><br/>
						  			结束时间：<input type="text" class="datepicker time-send" id="next_endTime" name="next_endTime"/>
						  		</td>
						  		<td>
						  			开始时间：<input type="text" class="datepicker time-send" id="summer_beginTime" name="summer_beginTime" /><br/><br/>
						  			结束时间：<input type="text" class="datepicker time-send" id="summer_endTime" name="summer_endTime"/>
						  		</td>
						  	</tr>
					  	</tbody>
					  </table>
					  
					  <table  id="show_record_add" style="margin-top:50px;margin-left:150px;">
							<thead>
								<tr class="text-center">
									<td style="font-size:16px;">学年</td>
									<td style="font-size:16px;">上学期</td>
									<td style="font-size:16px;">寒假</td>
									<td style="font-size:16px;">下学期</td>
									<td style="font-size:16px;">暑假</td>
								</tr>
							</thead>
							<tbody>
								
							</tbody>
					  </table>
						
					</div>
				</form>
			</div>
			<!--<div style="text-align: center; height: 33px;">
				<button id="edit" class="page-button">
					<i class="fa fa-check"></i>提交
				</button>
				<button id="cancelBTN" onclick="cancelBTN()" class="page-button">
					<i class="fa fa-times"></i>取消
				</button>
			</div> -->
		</div>
	</div>
	
	<!-- 修改操作 -->
	<div class="page-editor-panel-update full-drop-panel">
		<div class="title-bar">
			<h4>
				<i class="fa fa-pencil"></i>
			</h4>
			<div class="btn-area">
				<div style="margin-top: 4px;">
					<button id="editorUpdate" class="page-button">
						<i class="fa fa-check"></i>保存
					</button>
					<button id="cancelUpdateBTN" class="page-button" onclick="cancelUpdateBTN()">
						<i class="fa fa-times"></i>取消
					</button>
				</div>
			</div>
		</div>
		<div class="page-content">
			<div class="page-inner-content">
				<form id="editorForm">
					<input name="id" type="hidden" />
					<!-- 修改校历 -->
  	 				<div id="dialog-update" title="修改校历">
						  <table style="padding-left:20px;margin-left:210px;">
						  	<thead>
							  	<tr>
							  		<th style="border:0px;text-align:center;font-size:16px;">学年</th>
							  		<th style="border:0px;text-align:center;font-size:16px;">上学期</th>
							  		<th style="border:0px;text-align:center;font-size:16px;">寒假</th>
							  		<th style="border:0px;text-align:center;font-size:16px;">下学期</th>
							  		<th style="border:0px;text-align:center;font-size:16px;">暑假</th>
							  	</tr>
						  	</thead>
						  	<tbody>
							  	<tr>
							  		<td><span class="school_year"></span></td>
							  		<td>
							  			开始时间：<input type="text"  id="last_beginTime_update" class="datepicker time-send"/><br/><br/>
							  			结束时间：<input type="text"  id="last_endTime_update" class="datepicker time-send"/>
							  		</td>
							  		<td>
							  			开始时间：<input type="text"  id="winter_beginTime_update" class="datepicker time-send"/><br/><br/>
							  			结束时间：<input type="text"  id="winter_endTime_update" class="datepicker time-send"/>
							  		</td>
							  		<td>
							  			开始时间：<input type="text"  id="next_beginTime_update" class="datepicker time-send"/><br/><br/>
							  			结束时间：<input type="text"  id="next_endTime_update" class="datepicker time-send"/>
							  			<input type="hidden"  id="hidden_id"  name="hidden_id"/>
							  		</td>
							  		<td>
							  			开始时间：<input type="text"  id="summer_beginTime_update" class="datepicker time-send"/><br/><br/>
							  			结束时间：<input type="text"  id="summer_endTime_update" class="datepicker time-send"/>
							  		</td>
							  	</tr>
						  	</tbody>
						  </table>
						
							<table  id="show_record" style="margin-top:50px;margin-left:150px;">
								<thead>
									<tr class="text-center">
										<td style="font-size:16px;">学年</td>
										<td style="font-size:16px;">上学期</td>
										<td style="font-size:16px;">寒假</td>
										<td style="font-size:16px;">下学期</td>
										<td style="font-size:16px;">暑假</td>
									</tr>
								</thead>
								<tbody>
									
								</tbody>
							</table>
					</div>
				  	
				</form>
			</div>
			<!--<div style="text-align: center; height: 33px;">
				<button id="edit" class="page-button">
					<i class="fa fa-check"></i>提交
				</button>
				<button id="cancelBTN" onclick="cancelBTN()" class="page-button">
					<i class="fa fa-times"></i>取消
				</button>
			</div> -->
		</div>
	</div>
	
	<!-- 删除操作(下拉) -->
	<div class="page-editor-panel-delete full-drop-panel">
		<div class="title-bar">
			<h4>
				<i class="fa fa-times"></i>
			</h4>
			<div class="btn-area">
				<div style="margin-top: 4px;">
					<button id="cancelDel" class="page-button" onclick="cancelDeleteBTN()">
						<i class="fa fa-times"></i>取消
					</button>
				</div>
			</div>
		</div>
		<div class="page-content">
			<div class="page-inner-content">
				<form id="editorForm">
					<input name="id" type="hidden" />
					<!-- 删除校历 -->
  	 				<div id="dialog-delete">
							<table  id="show_record_delete" style="margin-left:90px">
								<thead>
									<tr class="text-center">
										<th style="font-size:16px;border:0px;">学年</th>
										<th style="font-size:16px;border:0px;">上学期</th>
										<th style="font-size:16px;border:0px;">寒假</th>
										<th style="font-size:16px;border:0px;">下学期</th>
										<th style="font-size:16px;border:0px;">暑假</th>
										<th style="font-size:16px;border:0px;">操作</th>
									</tr>
								</thead>
								<tbody>
									
								</tbody>
							</table>
					</div>
				  	
				</form>
			</div>
			<!--<div style="text-align: center; height: 33px;">
				<button id="edit" class="page-button">
					<i class="fa fa-check"></i>提交
				</button>
				<button id="cancelBTN" onclick="cancelBTN()" class="page-button">
					<i class="fa fa-times"></i>取消
				</button>
			</div> -->
		</div>
	</div>
	
	<!-- 删除校历 (弹框)-->
  	<!--<div id="dialog-delete" title="删除校历">
			<table class="table table-striped table-bordered table-hover text-center" id="show_record_delete">
				<thead>
					<tr class="text-center">
						<th style="font-size:16px;border:0px;">学年</th>
						<th style="font-size:16px;border:0px;">上学期</th>
						<th style="font-size:16px;border:0px;">寒假</th>
						<th style="font-size:16px;border:0px;">下学期</th>
						<th style="font-size:16px;border:0px;">暑假</th>
						<th style="font-size:16px;border:0px;">操作</th>
					</tr>
				</thead>
				<tbody>
					
				</tbody>
			</table>
	</div>-->
	<div class="page-view-panel full-drop-panel"></div>
</body>
</html>