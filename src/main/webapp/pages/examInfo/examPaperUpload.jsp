<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<title>试卷管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="../theme/default/ui.custom.css" rel="stylesheet" />
<link href="../theme/default/ui.jqgrid.css" rel="stylesheet" />
<link href="../theme/default/font.awesome.css" rel="stylesheet" />
<link href="../theme/default/ui.uploadfile1.css" rel="stylesheet" />
<link href="../theme/default/page.common.css" rel="stylesheet" />

<script type="text/javascript" src="../js/jquery.js"></script>
<script type="text/javascript" src="../js/ui.custom.js"></script>
<script type="text/javascript" src="../js/ui.jqgrid.js"></script>
<script type="text/javascript" src="../js/ui.uploadfile.js"></script>
<script type="text/javascript" src="../js/ui.common.js"></script>
<script type="text/javascript" src="../js/jquery.validate.js"></script>
<script type="text/javascript" src="../js/page.common.js"></script>


<style>
#add_exam_info ul li {
	list-style: none;
	margin-top: 30px;
}

.fileTable{
  border:1px solid #cccccc;
  margin-top:20px;
  margin-bottom:80px;
}

.td1{
  border:1px solid #cccccc;
}

.th1{
  padding:5px;
  border:1px solid #cccccc;
}

    .open1{
        background: rgba(59,86,23,1);
    }
    .open2{
        top: 2px;
        right: 1px;
    }
    .close1{
        background: rgba(255,255,255,0.4);
        border:2px solid rgba(0,0,0,0.15);
        border-left: transparent;
    }
    .close2{
        left: 0px;
        top: 0px;
        border:2px solid rgba(0,0,0,0.1);
    }


</style>
<script type="text/javascript">

	var listId = "#list2", 
	editorFormId = "#editorForm", 
	pagerId = "#pager2",
	loadPaperUrl = "../examInfo/examPaperUpload.do?command=loadPaper", 
	examPaperUrl = "../examInfo/examPaperUpload.do?command=searchExamPaper", 
	listUrl = "../examInfo/examManage.do?command=searchPaging",
	paperUpdateUrl = "../examInfo/examPaperUpload.do?command=updatePaper";
	

	$(function() {
		_initButtons({
			backBTN : function(){
				hideSlidePanel(".page-editor-panel");
				showSlidePanel(".page-list-panel");
				$(listId).trigger("reloadGrid");
			}
	
		}); //from page.common.js
		_initFormControls(); //from page.common.js
		_initValidateForXTypeForm(editorFormId);
		
		var _colModel = [{
			 					name : 'Id',
			 					key : true,
			 					width : 60,
			 					hidden : true,
			 					search : false
			 				},{
			 					name : 'Grade_Code',
			 					sortable : false,
			 					width : 60,
			 					hidden : true,
			 				},{
			 					label : "测试日期",
			 					name : 'Exam_Time',
			 					sortable : false,
			 					width : 100,
			 					align : "center",
			 				},{
			 					label : "测试编号",
			 					name : 'examNumber',
			 					sortable : false,
			 					width : 150,
			 					align : "center",
			 				},{
			 					label : "测试名称",
			 					name : 'Exam_Name',
			 					sortable : false,
			 					autoWidth : true,
			 					align : "left",
			 				},{
			 					label : "操作",
			 					name : '',
			 					sortable : false,
			 					width : 100,
			 					align : "center",
			 					formatter : function(ar1, ar2, ar3) {
			 						return "<button id='queryBTN' class='page-button' title='上传试卷' onclick='showExamPapers(\""
			 								+ ar3.examNumber
			 								+ "\",\""
			 								+ ar3.Grade_Code
			 								+ "\",\""
			 								+ ar3.Id
			 								+ "\",\""
			 								+ ar3.Exam_Name
			 								+ "\");'><i class='fa fa-file-text' style='margin-right:5px;'></i>上传试卷</button>"
			 					}
			 				} ];

			 		$(listId).jqGrid($.extend(defaultGridOpts, {
			 			url : listUrl,
			 			colModel : _colModel,
			 			pager : pagerId,
			 			postData:{introducedState:"1"},
			 			multiselect:false,
			 		}));
			 		resizeFun();
	});
	
	

    //加载科目和试卷附件信息
	function showExamPapers(examNumber,gradeCode, id,Exam_Name) {
	
		var $i = $(id.currentTarget).find("i"), $piel = $(".page-editor-panel")
				.show({
					effect : "slide",
					direction : "up",
					easing : 'easeInOutExpo',
					duration : 900
				}); 
				
        $("#filesTB").html("");
	    var data = {examCode:examNumber,gradeCode:gradeCode,id:id};
	     
 	     $.ajax({
	       url: "../examInfo/examPaperUpload.do?command=searchExamPaper",
	       type: "POST",
	       data: data,
	       dataType: "JSON",
	       async:false,
	       success: function(data){
	       	 
	         var lengths = data.length;
	         for(var i = 0; i < lengths; i++){
	            var fileId;       //试卷id
	            var examId;       //考试id
	            var examCode;     //考试编号
	            var courseCode;   //科目编号
	            var courseName;   //科目名称
	            var fileList;     //试卷集合
	            var fileName;     //试卷名称
	            var isPublic;
	            var examName = Exam_Name;	            
	            examId = id;	            
	            examCode = examNumber;
                courseCode = data[i].Course;
	            courseName = _types[courseCode];
	            fileList = data[i].FileList;
	            var size = fileList.length;	            
	            var examPagersFile="";
	            if(size > 0){   //有试卷附件存在
	              
	                for(var j = 0 ;j < size; j++){
	                   fileId = data[i].FileList[j].Id;
	                   fileName = fileList[j].FileName;
	                   isPublic = fileList[j].IsPublic;
	                   var paperDownload = projectName+"/examInfo/examPaperUpload.do?command=download&id="+fileId+"";
	                   
	                   examPagersFile += "<span id='"+fileId+"'><a title='"+fileName+"' class='file-name' data-fid='" + fileId + "' href='"+paperDownload+"'>"
	                         + "<i class='fa fa-file-text-o' style='margin-right:3px;font-size:14px;'></i>" + fileName + "</a>"
	                         + "<a title='移除'  onclick='deletePaper(\""+fileId+"\",\""+i+"\")' class='remove'>"
	                         + "<i class='fa fa-times' ></i></a></span>";
	                    	                   
	                } 
	             
	                $("#filesTB").append("<tr><td class='td1'>"+courseName+"</td>"
	                    + "<td class='td1'><div id='div1-"+i+"' style='margin-left: 8px;width: 40px;height: 24px;border-radius: 12px;position: relative;'"
	                    + "onclick='upTop(\""+i+"\",\""+examCode+"\",\""+courseCode+"\");' class='close1'>"
	                    + "<div id='div2-"+i+"' style='width: 20px;height: 20px;border-radius: 10px;position: absolute;background: white;box-shadow: 0px 2px 4px rgba(0,0,0,0.4);' class='close2'></div></div></td>"
	                    + "<td class='td1'><div id='fileListTD"+i+"' class='fileListTD' style='word-break: break-all;'>"
	                    + "<div id='uploadInput"+i+"_list' class='file-area' style='width:470px;'>"+examPagersFile+"</div></div></td>"
	                    + "<td><input data-xtype='upload' id='uploadInput"+i+"' data-appendto='#fileListTD"+i+"'"
	                    + "type='file' name='files' style='width: 470px;' data-button-text='上传附件' /></td></tr>");
	              
	                   if(isPublic == 0){       //公开
	                     document.getElementById("div1-"+i+"").className="open1";
	                     document.getElementById("div2-"+i+"").className="open2";
	                     // $("#public-"+i+"").css("color","red");
	                   }else{              //不公开（默认）
	                     document.getElementById("div1-"+i+"").className="close1";
	                     document.getElementById("div2-"+i+"").className="close2";
	                     // $("#public-"+i+"").css("color","#808080");
	                   }
	              
	            }else{       //无试卷附件
	            
	            $("#filesTB").append("<tr><td class='td1'>"+courseName+"</td>"
	                    + "<td class='td1'><div id='div1-"+i+"' style='margin-left: 8px;width: 40px;height: 24px;border-radius: 12px;position: relative;'"
	                    + "onclick='upTop(\""+i+"\",\""+examCode+"\",\""+courseCode+"\");' class='close1'>"
	                    + "<div id='div2-"+i+"' style='width: 20px;height: 20px;border-radius: 10px;position: absolute;background: white;box-shadow: 0px 2px 4px rgba(0,0,0,0.4);' class='close2'></div></div></td>"
	                    + "<td class='td1'><div id='fileListTD"+i+"' class='fileListTD' style='word-break: break-all;'></div></td>"
	                    + "<td><input data-xtype='upload' id='uploadInput"+i+"' data-appendto='#fileListTD"+i+"'"
	                    + "type='file' name='files' style='width: 470px;' data-button-text='上传附件' /></td></tr>");
                }
 			     (function ($titem) {
                    $titem.uploadFile($.extend({
                        url: projectName+"/examInfo/examPaperUpload.do?command=upload&examId="+examId+"&examCode="+examCode+"&course="+courseCode+"&courseName="+courseName+"&examName="+examName+"",
                        removeUrl: projectName+"/examInfo/examPaperUpload.do?command=remove",
                        dowloadUrl: projectName+"/examInfo/examPaperUpload.do?command=download&id={id}",
                        returnType: "JSON",
                        showDone: false,
                        showStatusAfterSuccess: false
                      }, data.opt));
                   })
                   ($("#uploadInput"+i));

                                         
	         }	               
	       }
	       
	     }); 
	     
	     
	     
	     
	}
	
	 //删除试卷
	 function deletePaper(fileId,i){
	     var deleteUrl = projectName+"/examInfo/examPaperUpload.do?command=remove&id="+fileId+"";
         $.ajax({
           url:deleteUrl,
           data:{},
           type:"GET",
           dataType:"JSON",
           success:function(){
                           
               }
           });
        $("#uploadInput"+i+"_list").find("#"+fileId+"").remove();
        
        if($("#uploadInput"+i+"_list").children().length == 0){
             document.getElementById("div1-"+i+"").className="close1";
	         document.getElementById("div2-"+i+"").className="close2";
        }
        
     }
	
	//试卷是否公开
	function upTop(i,examCode,courseCode){
	         $.ajax({
	            url: loadPaperUrl,
	            type: "POST",
	            data: {examCode: examCode,courseCode: courseCode},
	            dataType: "JSON",
	            success:function(data){
	        var gongkai = data;
	      if(gongkai.length == 0){
	        window.Msg.alert("请先上传试卷！");
	      }else if(gongkai == 1){
				window.message({
		            text: "是否将试卷  公开？ ",
		            title: "提醒",
		            buttons: {
		                "确认": function () {
		                    var isPublic = "0";    //公开
		                    POST(paperUpdateUrl, {examCode: examCode,courseCode: courseCode,isPublic: isPublic}, function (data) {
		                        
		                    });
		                    document.getElementById("div1-"+i+"").className="open1";
	                        document.getElementById("div2-"+i+"").className="open2";
		                    window.top.$(this).dialog("close");
		                    
		                },
		                "关闭": function () {
		                    window.top.$(this).dialog("close");
		                }
		            }
		        });

			}else{
				window.message({
		            text: "是否将试卷 取消 公开？",
		            title: "提醒",
		            buttons: {
		                "确认": function () {
		                	var isPublic = "1";   //不公开
		                    POST(paperUpdateUrl, {examCode: examCode,courseCode: courseCode, isPublic: isPublic}, function (data) {	
		                        
		                    });
		                    document.getElementById("div1-"+i+"").className="close1";
	                        document.getElementById("div2-"+i+"").className="close2";
		                    window.top.$(this).dialog("close");
		                },
		                "关闭": function () {
		                    window.top.$(this).dialog("close");
		                }
		            }
		        });

			}
           }
	      });
		}

</script>
</head>
<body>

	<div class="page-list-panel">
		<div class="head-panel">
			<div class="toolbar">
				<table style="height: 100%;" cellpadding="0" cellspacing="0"
					border="0">
					<tr>
						<td style="padding-left: 12px; padding-right: 24px;"><i
							class="fa fa-list-ul micon"></i></td>
						<td style="padding-left: 24px; padding-right: 5px;"><input
							id="fastQueryText" type="text" placeholder="输入测试名称"
							style="line-height: 1em; font-size: 1em; cursor: text;" /></td>
						<td>
							<button id="fastSearch" title="查询" style="margin-left: 0px;">
								<i class="fa fa-search"></i>查询
							</button>
						</td>
					</tr>
				</table>
			</div>
		</div>
		<table id="list2"></table>
		<div id="pager2"></div>
	</div>



	<div class="page-editor-panel full-drop-panel">		
			<div class="title-bar">
			<input  id="gradeCode"  type="hidden" />
				<div class="btn-area">
				  <div style="margin-top: 4px;">
					<button id="backBTN">
						<i class="fa fa-reply"></i>返回
					</button>
				</div>
				</div>
                           
			</div>
	
	<div class="page-content">
	   
	    
	    <table id="fileTable" class="fileTable" border="0" cellspacing="0" cellpadding="0" align="center">
		    <thead style="height:30px;">
		       <th class="th1">科目</th>
		       <th class="th1">是否公开</th>
		       <th class="th1">试卷名称</th>
		       <th class="th1">操作</th>
		       
		    </thead>
		    <tbody id="filesTB" style="text-align:center;border:1px solid #cccccc">     

		    </tbody>
		   
		 
		 </table>  
	<!--	    <table id="markingArrangement"></table>
	 	<div id="markingArrangementpager"></div>
		 -->
		
		</div>
	</div>

           

</body>
</html>