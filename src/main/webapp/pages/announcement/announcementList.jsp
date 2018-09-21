<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="<%=request.getContextPath() %>/theme/default/ui.custom.css"
	rel="stylesheet" />
<link href="<%=request.getContextPath() %>/theme/default/ui.jqgrid.css"
	rel="stylesheet" />
<link
	href="<%=request.getContextPath() %>/theme/default/font.awesome.css"
	rel="stylesheet" />
<link href="<%=request.getContextPath() %>/theme/default/ui.chosen.css"
	rel="stylesheet" />
<link
	href="<%=request.getContextPath() %>/theme/default/ui.uploadfile.css"
	rel="stylesheet" />
<link
	href="<%=request.getContextPath() %>/theme/default/page.common.css"
	rel="stylesheet" />
<link href="../theme/default/jquery.multiselect.css" rel="stylesheet" />
<link href="../theme/default/jquery.multiselect.filter.css"
	rel="stylesheet" />
<script type="text/javascript"
	src="<%=request.getContextPath() %>/js/jquery.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath() %>/js/ui.custom.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath() %>/js/ui.jqgrid.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath() %>/js/ui.autosearch.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath() %>/js/ui.chosen.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath() %>/js/ui.uploadfile.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath() %>/js/ui.common.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath() %>/js/jquery.validate.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath() %>/js/page.common.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath() %>/js/util.js"></script>

<script type="text/javascript" src="../js/jquery.multiselect.js"></script>
<script type="text/javascript" src="../js/jquery.multiselect.filter.js"></script>
<style>
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

.meeting_but_Win8 {
	background: #FFFFFF !important;
	border: 1px solid #f58d06 !important;
	width: auto !important;
	height: 30px !important;
	color: #f58d06 !important;
	cursor: pointer !important;
	line-height: 1px !important;
}

.meeting_but_Win8:hover {
	color: #FFFFFF !important;
	background: #f58d06 !important;
}

.input_span {
	background:
		url('<%=request.getContextPath() %>/theme/default/images/unchecked.gif')
		no-repeat;
	padding: 0px 0px 2px 16px;
	margin-left: .3em;
	color: red;
	margin-top: 6px;
	display: inline;
	font-size: 12px;
}

.select td {
	text-align: right;
	width: 100px;
}

#editorArea tr {
	height: 46px;
}

i:hover {
	cursor: pointer;
}

#replyTB th {
	height: 30px;
	background: #36A9E0;
	color: #ffffff;
	font-size: 12px;
	text-align: center;
	font-weight: bold;
}

#replyTB td {
	text-align: center;
	background: #fff;
	height: 30px;
}

.tradition {
	border: 1px solid #3babe3;
	background: #ffffff;
	font-weight: bold;
	color: #3babe3;
}

.tradition:hover {
	border: 1px solid #3babe3;
	background: #3babe3;
	font-weight: bold;
	color: white;
}

.win8 {
	border: 1px solid #f58d06;
	background: #ffffff;
	font-weight: bold;
	color: #f58d06;
}

.win8:hover {
	border: 1px solid #f58d06;
	background: #f58d06;
	font-weight: bold;
	color: white;
}
</style>
<script type="text/javascript">
   
 	var listId = "#list2",
	editorFormId = "#editorForm", 
	pagerId = '#pager2', //分页
	listUrl = "<%=request.getContextPath() %>/announcement/announcementList.do?command=search",
	saveUrl = "<%=request.getContextPath() %>/announcement/announcementList.do?command=submit", 
	publishUrl = "<%=request.getContextPath() %>/announcement/announcementList.do?command=publish", 
	loadUrl = "<%=request.getContextPath() %>/announcement/announcementList.do?command=load",
	deleteUrl="<%=request.getContextPath() %>/announcement/announcementList.do?command=delete",
	lookUrl="<%=request.getContextPath() %>/announcement/announcementList.do?command=lookAnnouncement",
	updateSeqNumsUrl="<%=request.getContextPath() %>/announcement/announcementList.do?command=updateSeqNums",frameWindow,editor;
	var publish=function(){
			$(".input_span").remove();//时间必填校验
			if($("#enableTimeEndInput").val()=="" ||$("#enableTimeStartInput").val()==""){
				$("#enableTimeEndInput").after("<span class='input_span'>必须填写完整</span>");
				return ;
			}else{
				var startTime=$("#enableTimeStartInput").val();
				var endTime=$("#enableTimeEndInput").val();
				startTime=convert(startTime);
			    endTime=convert(endTime);
			   if(endTime>startTime){
				   if ($(editorFormId + " [data-validate]").valid()) {
						var dataParam=$(editorFormId).getFormData();
						frameWindow.getValue(function(data){
						    dataParam.context=data;
						});
							dataParam.paramAry=$("#partakePersonIDs").val();
							dataParam.userName=$("#partakePersons").val();
							
							var partakeSchoolCodes=[];
								var val = $("#partakeSchoolCode").find("option:selected")
								.each(function() {
									var params={};
									params["schoolVal"]=$(this).val();
									partakeSchoolCodes.push(params);
								});
								dataParam.partakeSchool=partakeSchoolCodes;
								var partakeSchoolName=[];
								var val = $("#partakeSchoolCode").find("option:selected")
								.each(function() {
									var params={};
									params["schoolText"]=$(this).text();
									partakeSchoolName.push(params);
								});
								dataParam.partakeSchoolName=partakeSchoolName;
						POST(publishUrl,dataParam,function(data){
							$(listId).trigger("reloadGrid");
							hideSlidePanel(".page-editor-panel");
							clearCache();
						});
					}
			   }else{
  						$("#enableTimeEndInput").after("<span class='input_span'>注意有效时间大小</span>");
  						return;
  					}
	   }
	   		
	};
	
	
	function removeHTMLTag(str) {
            str = str.replace(/<\/?[^>]*>/g,''); //去除HTML tag
            str = str.replace(/[ | ]*\n/g,'\n'); //去除行尾空白
            str = str.replace(/\n[\s| | ]*\r/g,'\n'); //去除多余空行
            str=str.replace(/&nbsp;/ig,'');//去掉&nbsp;
            return str;
    }

	var viewItem = function( id )
		{			
		   frameDialog("meetingMangement/meetingApplyReturnList/meetingNoticeBasicInfoForAccepterView.do?command=init&id="+id, "会议通知内容",
		    {
		        mode    : "full",
		        buttons : [
		            {text:"确定",icons:{ primary: "ui-icon-check"},click:function(ev){
           		            var $this=window.top.$(this),dial= $this.find("iframe")[0].contentWindow;
           		                 dial.saveData(function(){
              		            	setTimeout(function(){ $this.dialog("close");},200);
              		            	 $(listId).trigger("reloadGrid");
              		            	 window.parent.frames[1].$("#list3").trigger("reloadGrid");
              		            	}); 
          		            	}
          		            }
		        ]
		    });
		};
		
	var editOper=function(id){
	   GET(loadUrl,{id:id,dc: (new Date()).getTime()},function(data){
                var $piel =showSlidePanel(".page-editor-panel").find("h4 i").removeClass();
				if(data.fieldConfig){
					$(editorFormId).configFormField(data.fieldConfig);
					window._FIELDCONFIG=data.fieldConfig;
					$(editorFormId).resetHasXTypeForm(data.entity);
					window._EDITDATA = data.entity;
				}else{
					$(editorFormId).resetHasXTypeForm(data);
					window._EDITDATA = data;
					clearCache();
					 $("#partakePersons").val(data.partakePersons);
					 $("#partakePersonOrgIDs").val(data.teamNames);
					 $("#partakeSchoolType").val(data.schoolType);
					var schoolCode=data.partakeSchoolIDs;
					$("#files").data().fileItems(data.announcementBasicInfoEntity.files);
					$("#kendioeditframe").attr("src","<%=request.getContextPath() %>/pages/kendioEdit.jsp?flag=update&id="+data.id);
					var schoolType=$("#partakeSchoolType").find("option:selected").val();
		 			if(schoolType!=""){
		 				var url = "../platform/dictionary.do?command=selectSchoolsBySchoolType";
		 				var data = {
		 	                    schoolType: schoolType
		 	                };
		 				$.ajax({
		                    url: url,
		                    type: "POST",
		                    data: data,
		                    dataType: "JSON",
		                    success: function (data) {
		                        for (var i = 0; i < data.length; i++) {
		                        	  $("#partakeSchoolCode").append("<option value='" + data[i].School_Code + "'>" + data[i].School_Short_Name + "</option>");
		                        }
		                        if( $("#partakeSchoolCode").find("option").length>0){
		                        	if(schoolCode!=null){
		        						var strCode=schoolCode.split(",");
		        						 for(i=0;i<strCode.length;i++){
		        							 $("#partakeSchoolCode").find("option[value='"+strCode[i]+"']").attr("selected","selected");
		        							 }
		        						 $("#school").multiselect('refresh');
		        					}
		                        	$("#partakeSchoolCode").multiselect('refresh');
		                        	
		                        }else{
		                        	$("#partakeSchoolCode").multiselect('refresh');
		                        }
		                    }
		                });
		 			}
					
				
				}
				if(window._edit){
					window._edit();
				}
			});
	 };	
	 var upTop = function(ev){//置顶
			if(ev){
				window.message({
		            text: "是否将该公告 置顶 ",
		            title: "提醒",
		            buttons: {
		                "确认": function () {
		                	var idAry = $(listId).jqGrid("getGridParam", "selrow");
		                    POST(updateSeqNumsUrl, {id: idAry,top:ev}, function (data) {
		                    		
		                        $(listId).trigger("reloadGrid");
		                    });
		                    window.top.$(this).dialog("close");
		                },
		                "关闭": function () {
		                    window.top.$(this).dialog("close");
		                }
		            }
		        });
			}else{
				window.message({
		            text: "是否将该条公告 取消  置顶 ",
		            title: "提醒",
		            buttons: {
		                "确认": function () {
		                	var idAry = $(listId).jqGrid("getGridParam", "selrow");
		                    POST(updateSeqNumsUrl, {id: idAry,top:ev}, function (data) {	
		                        $(listId).trigger("reloadGrid");
		                    });
		                    window.top.$(this).dialog("close");
		                },
		                "关闭": function () {
		                    window.top.$(this).dialog("close");
		                }
		            }
		        });
			}
			
		}
		
	 var updateReply = function(id){
		   var url="announcement/announcementList/replyContent.do?id="+id;
		   frameDialog(url, "联系人信息", {mode:"middle",width:430,height:300,buttons:[
				        { text:"提交", icons:{ primary:"ui-icon-check" },click:function( ev )
				        {
				        	var $this   = window.top.$( this ),
				        	  dial = $this.find( "iframe" )[0].contentWindow ;
				        	
				        	var data = dial.getData();
				        	data.noticeID =  $("#announcementId").val();
				        	data.id=id;
				        	if(data!=false && data!=null){
				        		var insertUrl="<%=request.getContextPath() %>/announcement/announcementList/replyContent.do?command=submit";
								POST(insertUrl,data,function(data){
									loadReplyInfo();
								});
								$this.dialog( "close" ) ;
				        	}

				        }},
				        { text:"返回", icons:{ primary:"ui-icon-cancel" }, click:function( ev )
				        {
				            var $this = window.top.$( this ) ;
				            $this.dialog( "close" ) ;
				        }}
				     ]});
	 }
	 
	 var deleteReply = function(ev){
		 var url = "<%=request.getContextPath() %>/announcement/announcementList/replyContent.do?command=delete";
		 window.message({
			 text:"确认删除该记录？",
			 title:"提示",
			 buttons:{
				 "确定":function(){
					 POST(url,{id:ev},function(data){
						 loadReplyInfo();
					 });
					 window.top.$(this).dialog("close");
				 },
				 "取消":function(){
					 window.top.$(this).dialog("close");
				 }
			 }
		 })
	 }
	 
	 var lookAnnouncement=function(ev){//查看
		 POST(lookUrl,{id: ev},function (data) {
			 $("#filesList").empty();
			 $("span#context").html(data.context);
			 $("span#publishPerson").html(data.publishPerson);
			 $("#announcementType").html(data.announcementType);
			 $("#activeTime").html(data.activeTime);
			 $("#title").html(data.title);
			 $("#publishTime").html(data.publishTime);
			 $("#announcementId").val(data.id);
			 $("#partakePersons_span").html(data.announcementEntity.partakePersons);
			 
			 var fileStr="";
			 for(var i=0;i<data.announcementEntity.files.length;i++){
			   var fileObj=data.announcementEntity.files[i];
			   fileStr+="<a class='file-name' data-fid=\""+fileObj.id+"\" href='<%=request.getContextPath()%>/platform/accessory_.do?command=download&id="+fileObj.id+" '>"+fileObj.fileName+"</a><p/>";
			 }
			 $("#filesList").append(fileStr);
         	 showSlidePanel("#lookAnnouncement");
         	 
         	$("#reply_but,.replyRow").remove();
         	
         	/*  if((data.replyStatus=="1"||data.replyStatus==1) && (data.announcementEntity.status==2 || data.announcementEntity.status=="2")){//参与人，和发布人可以回复
         		 $("#reply_table").show();
         		 $("#lookForm #editorArea").append("<tr class='replyRow'><td class='label' colspan='2' style='text-align:left;' ><button type='button' id='reply_but' class='meeting_but' onclick='reply();'>回复</button></td></tr>");
            	
         	 }else{
         		 $("#reply_table").hide();
         	 } */
         	
         	 if($("#role").val()=="admin"){
         		$("#reply_table").show();
         	 }  
         	
         	loadReplyInfo();
         	 
       });
	};
	
	var formatTime = function (ev){
		var paramTime = new Date(parseInt(ev));
		return  paramTime.getFullYear()+"-"+
							((paramTime.getMonth()+1)<10?"0"+(paramTime.getMonth()+1):paramTime.getMonth()+1)+"-"+
						 		(paramTime.getDate()<10?"0"+paramTime.getDate():paramTime.getDate())+" "+
						 		(paramTime.getHours()<10?("0"+paramTime.getHours()):paramTime.getHours()+":"+
						 				(paramTime.getMinutes()<10?("0"+paramTime.getMinutes()):paramTime.getMinutes())); 
	}

	
	var loadReplyInfo = function(){
		 var url = "<%=request.getContextPath() %>/announcement/announcementList.do?command=loadReplyInfo" ;
 		 POST(url,{id:$("#announcementId").val()},function(data){
			 var str ="";
			 console.log(data);
         	 for(var k=0;k<data.list.length;k++){
         		var but ="" ;
         		 if( data.deletePower || $("#currentUserId").val()==data.list[k].USERID){//如果是当前用户发布的，可以删除所有人回复。如果不是当前用户发布，只能操作自己的
         			but = "<button type='button' class='meeting_but' onclick='deleteReply(\"" + data.list[k].ID + "\");'>删除</button>";
         		 }
         		 if($("#currentUserId").val()==data.list[k].USERID){
         			but = but+" <button type='button' onclick='updateReply(\"" + data.list[k].ID + "\");' class='meeting_but'>修改</button>";
         		 }
         		 
         		str = str+"<tr><td>"+(k+1)+"</td><td>"+data.list[k].USERNAME+"</td><td>"+formatTime([data.list[k].REPLYTIME])+"</td><td style='word-break:break-all;'>"+data.list[k].REPLYCONTENT+"</td><td>"+but+"</td></tr>";
         	 }      
         	 $("#replyTB tr:gt(0)").remove();
         	 $("#replyTB").append(str);
		});
	}
	
var	reply = function(){
 var url="announcement/announcementList/replyContent.do";
 frameDialog(url, "公告回复", {mode:"middle",width:430,height:300,buttons:[
		        { text:"提交", icons:{ primary:"ui-icon-check" },click:function( ev )
		        {
		        	var $this   = window.top.$( this ),
		        	  dial = $this.find( "iframe" )[0].contentWindow ;
		        	
		        	var data = dial.getData();
		        	data.noticeID =  $("#announcementId").val();
		        	if(data!=false && data!=null){
		        		var insertUrl="<%=request.getContextPath() %>/announcement/announcementList/replyContent.do?command=submit";
						POST(insertUrl,data,function(data){
							loadReplyInfo();
						});
						$this.dialog( "close" ) ;
		        	}

		        }},
		        { text:"返回", icons:{ primary:"ui-icon-cancel" }, click:function( ev )
		        {
		            var $this = window.top.$( this ) ;
		            $this.dialog( "close" ) ;
		        }}
		     ]});
}
	

	
	var returnTab=function(){//返回公告主页
		 	hideSlidePanel("#lookAnnouncement");	
	};

     function getchildwindow(){
     	 frameWindow= $("#kendioeditframe")[0].contentWindow;
     	 editor=frameWindow.editor;
     };

     var convert=function(curDate){
      	  dateAryOne=curDate.split("-");
            var result="";
         for(var i=0;i<dateAryOne.length;i++ ){
          result+=dateAryOne[i];
         }
           return parseInt(result);
	  };
	  
		
	    var getSystemAdmin=function(){
		   
	       var idsParam=$("#partakePersonIDs").val();
	       var url="";
	       if(idsParam!="undefined" && idsParam!=""){
			      url="platform/choosePerson.do?command=init&idsParam="+idsParam+"&orgIDs="+$("#partakePersonOrgIDs").val()+"&orgCheck=false&personCheck=true+&chkStyle=checkbox";
	       		}else{
			      url="platform/choosePerson.do?&orgCheck=false&personCheck=false&chkStyle=checkbox";
			 }

		   frameDialog(url, "请选择", {mode:"middle",width:230,height:480,buttons:[
				        { text:"确定", icons:{ primary:"ui-icon-check" },click:function( ev )
				        {
				             var $this   = window.top.$( this ),
				                dial    = $this.find( "iframe" )[0].contentWindow ;
				            var rowData = dial.getData().split(";");
				            var rowIdData = rowData[1];
				            var rowNameData = rowData[0];
				            var orgIDs = rowData[2];
				            
				            var tempId=rowIdData.split(",");
			                $("#partakePersons").val(rowNameData);
				            $("#partakePersonIDs").val(rowIdData);
				            $("#partakePersonOrgIDs").val(orgIDs);
				            $this.dialog( "close" ) ;
				        }},
				        { text:"返回", icons:{ primary:"ui-icon-cancel" }, click:function( ev )
				        {
				            var $this = window.top.$( this ) ;
				            $this.dialog( "close" ) ;
				        }}
				     ]});
		       }; 
	var deleteAnnouncement =function(ev){
		var arr = [];
		arr[0]=ev;
        window.message({
            text: "确认要删除所选择的记录吗?",
            title: "提醒",
            buttons: {
                "确认": function () {
                    window.top.$(this).dialog("close");
                    POST(deleteUrl, {id: arr}, function (data) {
                    		
                        $(listId).trigger("reloadGrid");
                        if (window._delete) {
                            window._delete();
                        }
                    });
                },
                "取消": function () {
                    window.top.$(this).dialog("close");
                }
            }
        });
	};
	
	var clearCache=function(){
		$("#teamNamesId input:checked").each(function(){
			$(this).attr("checked",false);
		});
	 	
	};
	
	//当是内部征询意见时，才有回复功能。
	var selectAnnounceType=function(ev){
	  var val=$(ev).val();
	  if(val=="内部征询意见"){
	    $("#replyTr").show();
	  }else{
	     $("#replyTr").hide();
	     $("#replyStatus").val("1");
	     $("#replyStatus").prop("checked","");
	  }
	};

	var tishi=function()
	{
	       window.message({
	            text: "你没有对该记录进行这样操作的权限！",
	            title: "提醒",
	            buttons: {
	                "确认": function () {
	                    window.top.$(this).dialog("close");
	                }
	            }
	        });
	}
	
	$(function() {
		
		
		loadDictionary(function() {
			//去掉select option值是ygz,wz的学校类型
			$("#partakeSchoolType option[value='ygz'],[value='wz']").remove();
		});
		
		var loadSchool=function(){
 			$("#partakeSchoolCode option[value != '']").remove();
 			var schoolType=$("#partakeSchoolType").find("option:selected").val();
 			if(schoolType!=""){
 				var url = "../platform/dictionary.do?command=selectSchoolsBySchoolType";
 				var data = {
 	                    schoolType: schoolType
 	                };
 				$.ajax({
                    url: url,
                    type: "POST",
                    data: data,
                    dataType: "JSON",
                    success: function (data) {
                        for (var i = 0; i < data.length; i++) {
                        	  $("#partakeSchoolCode").append("<option value='" + data[i].School_Code + "'>" + data[i].School_Short_Name + "</option>");
                        }
                        if( $("#partakeSchoolCode").find("option").length>0){
                        	$("#partakeSchoolCode").multiselect('refresh');
                        	
                        }else{
                        	$("#partakeSchoolCode").multiselect('refresh');
                        }
                    }
                });
 			}else{
 				$("#partakeSchoolCode").multiselect('refresh');
 			}
 		}
		$("#partakeSchoolCode").multiselect({
			checkAllText : "全选",
			uncheckAllText : "全不选",
			noneSelectedText : "选择学校",
			selectedText : '#' + " 所学校",
			selectedList : 2
		}).multiselectfilter({
			label : "学校名称",
			placeholder : "请输入校名"
		});
		window.paneStyle = $("#paneStyle").val(); 
		var def;
		_initButtons({
  			resetBTN : function(ev) {
  				var $pn=$(ev.target).closest(".page-editor-panel");
  				if(window._FIELDCONFIG){
  						$(editorFormId).configFormField(window._FIELDCONFIG);
  				}
  				$pn.find("form").resetHasXTypeForm(window._EDITDATA);
  				if(window._reset){
  					window._reset($pn);
  				}
  			},
  			cancelBTN: function () {
  				 $("#partakeSchoolCode option[value != '']").remove();
              hideSlidePanel(".page-editor-panel,.page-view-panel");
              $("#replyTr").hide();
	          $("#replyStatus").val("1");
	          $("#replyStatus").prop("checked","");
            },
  			editorSave : function() {
  				
  				$(".input_span").remove();//时间必填校验
  				if($("#enableTimeEndInput").val()=="" ||$("#enableTimeStartInput").val()==""){
  					$("#enableTimeEndInput").after("<span class='input_span'>必须填写完整</span>");
  					return ;
  				}else{
  					var startTime=$("#enableTimeStartInput").val();
  					var endTime=$("#enableTimeEndInput").val();
  					startTime=convert(startTime);
					endTime=convert(endTime);
  					if(endTime>startTime){
  						 if ($(editorFormId + " [data-validate]").valid()) {
  								var dataParam=$(editorFormId).getFormData();
  								dataParam.context=$("context").val
  								dataParam.userName=$("#partakePersons").val();
  								dataParam.teamNames=$("#partakePersonOrgIDs").val();
  								dataParam.schoolType=$("#partakeSchoolType").val();
  								var partakeSchoolCodes=[];
								var val = $("#partakeSchoolCode").find("option:selected")
								.each(function() {
									var params={};
									params["schoolVal"]=$(this).val();
									partakeSchoolCodes.push(params);
								});
								dataParam.partakeSchool=partakeSchoolCodes;
								
								var partakeSchoolName=[];
								var val = $("#partakeSchoolCode").find("option:selected")
								.each(function() {
									var params={};
									params["schoolText"]=$(this).text();
									partakeSchoolName.push(params);
								});
								var files = [];
  								$.each(dataParam.files, function(index, item) {
  									var param = {};
  									param["id"] = item.id;
  									param["fileName"] = item.fileName;
  									files.push(param);
  								});
  								dataParam.files=files;
								dataParam.partakeSchoolName=partakeSchoolName;
  								frameWindow.getValue(function(data){
  								    dataParam.context=data;
  								});
  								POST(saveUrl,dataParam,function(data){
  									$(listId).trigger("reloadGrid");
  									hideSlidePanel(".page-editor-panel");
  									clearCache();
  								});
  							}
  					}else{
  						$("#enableTimeEndInput").after("<span class='input_span'>注意有效时间大小</span>");
  						return;
  					}
  					
  					
  				}
  				
			
		 },

		 returnTab : function(){
			 hideSlidePanel(".page-editor-panel");
		 },
		 resetSearch:function(ev){
			$("#context_search").val("");
			$("#publishPerson").val(""); 
			$("#enableTimeStart").val("");
			$("#enableTimeEnd").val("");
		 },
		 insertBTN: function (ev) {
            var $i = $(ev.currentTarget).find("i"),
                $piel = showSlidePanel(".page-editor-panel").find("h4 i").removeClass();
            if ($i.length) {
                $piel.addClass($i.attr("class"));
            }
            $("#kendioeditframe").attr("src","<%=request.getContextPath() %>/pages/kendioEdit.jsp?flag=add");
				window._EDITDATA = undefined;
				$(editorFormId).resetHasXTypeForm();
				if (window._insert) {
					window._insert(ev);
				}
			},

			deleteBTN : function() {

				var idAry = $(listId).jqGrid("getGridParam", "selarrrow");
				if (idAry.length === 0) {
					window.message({
						text : "请选择要删除的项",
						title : "提醒",
						buttons : {
							"确认" : function() {
								window.top.$(this).dialog("close");
							}
						}
					});
					return;
				}

				window.message({
					text : "确认要删除所选择的记录吗?",
					title : "提醒",
					buttons : {
						"确认" : function() {
							window.top.$(this).dialog("close");
							POST(deleteUrl, {
								id : idAry
							}, function(data) {

								$(listId).trigger("reloadGrid");
								if (window._delete) {
									window._delete();
								}
							});
						},
						"取消" : function() {
							window.top.$(this).dialog("close");
						}
					}
				});

			}
		});
		_initFormControls();//from page.common.js
		_initValidateForXTypeForm(editorFormId, {
			errorElement : "span",
			errorPlacement : function(error, element) {
				error.appendTo(element.parent("td"));
			}
		});

		var _colModel = [
				{
					name : 'id',
					key : true,
					width : 60,
					hidden : true
				},
				{
					name : '',
					width : 30,
					align : "center",
					sortable : false,
					formatter : function(ar1, ar2, ar3) {

						if (ar3.seqNums == 0) {
							return ar3.edit == "ok" ? "<i style='color:red;' onclick='upTop(false);' class='fa fa-flag'></i>"
									: "<i style='color:red;' onclick='tishi();' class='fa fa-flag'></i>";
						} else {
							return ar3.edit == "ok" ? "<i style='color:#808080;' onclick='upTop(true);' class='fa fa-flag'></i>"
									: "<i style='color:#808080;' onclick='tishi();' class='fa fa-flag'></i>";
						}
					}
				} ,
				{
					name : 'title',
					sortable : false,
					autoWidth : true,
					align : "left"
				},
				/* {
					name : 'announcementType',
					index : 'AnnouncementType',
					width : 100,
					align : "center"
				}, */
				{
					name : 'publishPerson',
					sortable : false,
					width : 100,
					align : "center"
				},
				{
					name : 'publishDate',
					index : 'PublishDate',
					width : 80,
					align : "center"
				},
				{
					name : 'enableTime',
					index : 'EnableTimeStart',
					width : 150,
					align : "center"
				},
				{
					name : 'status',
					width : 50,
					align : "center",
					sortable : false,
					formatter : function(status) {
						if (status == "2") {
							return "已发布";
						} else if (status == "1") {
							return "未发布";
						}else if(status=="3"){
							return "已过期";
						}else {
							return "";
						}
					}

				},
				{
					name : '',
					width : 200,
					align : "center",
					sortable : false,
					formatter : function(ar1, ar2, ar3) {//onclick='editBTN(\"" + ar3.id + "\");'
						var str = "";
						var _meeting_but = window.paneStyle == 'traditionStyle' ? "meeting_but"
								: "meeting_but_Win8";
						if ($("#role").val() != "admin" && ar3.edit == "ok") {//对于自己创建的的公告，可删除
							str = " <button class='page-button' onclick='deleteAnnouncement(\""
									+ ar3.id
									+ "\");'><i class='fa fa-trash-o'></i> 删除</button> ";
						}
						if (parseInt(ar3.status) == 1 && ar3.edit == "ok") {//对于自己创建的的公告，在未发布的情况下可已修改
							str = str
									+ "<button class='page-button' title='修改' onclick='editOper(\""
									+ ar3.id
									+ "\");'><i class='fa fa-pencil'></i> 修改</button> ";
						}else if(parseInt(ar3.status) == 3){
							str = " <button class='page-button' onclick='deleteAnnouncement(\""
								+ ar3.id
								+ "\");'><i class='fa fa-trash-o'></i> 删除</button> ";
						}
						return "<button class='page-button' title='查看' onclick='lookAnnouncement(\""
								+ ar3.id
								+ "\");'><i class='fa fa-pencil'></i> 查看</button> "
								+ str;
					}
				} ], _colNames = [ '编号', '置顶', '标题', /* '类型', */ '发布人', '发布日期',
				'有效期', '状态', '操作' ];

		$(listId).jqGrid($.extend(defaultGridOpts, {
			url : listUrl,
			colNames : _colNames,
			colModel : _colModel,
			multiselect : false,
			pager : pagerId
		}));
		resizeFun();

		if ($("#role").val() != "admin") {
			$("#deleteBTN").hide();
		}
		$("#partakeSchoolType").change(function() {
			loadSchool();
		});
	});
</script>

</head>
</body>
<%--<input type="hidden" name="id" id="currentUserId" value="${currentUserId}" />
    <input type="hidden" name="paneStyle" id="paneStyle" value="<%=paneStyle %>"/>
     <input type="hidden" name="operType" id="operType" value="<%=request.getParameter("operType") %>" />
    <input type="text" name="announceId" id="announceId" value="<%=request.getParameter("announceId") %>" /> 
    <input type="hidden" name="operType" id="operType" value="${operType}" />
    <input type="hidden" name="announceId" id="announceId" value="${announceId}" />--%>
<div class="page-list-panel">
	<div class="head-panel">
		<div class="search-panel">
			<div class="form-panel">

				<table cellpadding="0" cellspacing="0" border="0" class="select">
					<tr>

						<td>详细内容：</td>
						<td><input name="context" style="width: 292px;"
							id="context_search" /> <input name="role" id="role"
							type="hidden" value="${role}" /></td>
						<td>发布人：</td>
						<td><input style="width: 195px;" name="publishPerson"
							id="publishPerson" placeholder="请输入发布人"/></td>
					</tr>
					<tr>
						<td>有效时间：</td>
						<td><input data-xtype="datetime" readonly="readonly"
							name="enableTimeStart" id="enableTimeStart" style="width: 132px;"
							readonly="readonly" /> 至 <input data-xtype="datetime"
							readonly="readonly" name="enableTimeEnd" id="enableTimeEnd"
							style="width: 132px;"
							readonly="readonly" /></td>
						<td>类型：</td>
						<td><input style="width: 195px;" name="" value="公告"
							readonly="readonly" /></td>
						<td style="width: 180px; text-align: center;"><input
							id="history" type="checkbox" name="history" /> <label
							for="history">是否查询历史记录</label></td>
						<td style="padding-left: 30px;">
							<button id="advancedSearch">
								<i class="fa fa-search"></i>查询
							</button>
							<button id="resetSearch" type="button">
								<i class="fa fa-undo"></i>重置
							</button>
							<button type="button" id="searchRipClose" title="点击收起查询面板">
								<i class="fa  fa-angle-down" style="margin-right: 0px;"></i>
							</button>
						</td>
					</tr>
				</table>

			</div>
		</div>

		<div class="toolbar">
			<table style="height: 100%;" cellpadding="0" cellspacing="0"
				border="0">
				<tr>
					<td style="padding-left: 12px; padding-right: 24px;"><i
						class="fa fa-list-ul micon"></i></td>
					<td>
						<button id="insertBTN">
							<i class="fa fa-plus"></i>添加
						</button>
					</td>
					<td style="padding-left: 24px; padding-right: 5px;"><input
						id="fastQueryText" type="text" name="text"
						style="line-height: 1em; font-size: 1em; cursor: text; width: 150px;"
						class="form-control" placeholder="请输入标题"/></td>
					<td>
						<button id="fastSearch" title="查询" style="margin-left: 0px;">
							<i class="fa fa-search"></i>查询
						</button>

						<button id="searchRip" title="点击展开高级查询面板">
							<i class="fa fa-angle-up" style="margin-right: 0px;"></i>
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
		<h4>
			<i class="fa fa-plus"></i>
		</h4>
		<div class="btn-area">
			<div style="margin-top: 4px;">

				<button id="editorSave">
					<i class="fa fa-check"></i>保存
				</button>
				<button id="publish" onclick="publish();">
					<i class="fa fa-check"></i>发布
				</button>
				<!-- <button id="resetBTN" style="margin-right: 24px;">
						<i class="fa fa-undo"></i>重置
					</button>
					 -->
				<button id="cancelBTN">
					<i class="fa fa-times"></i>取消
				</button>
			</div>
		</div>
	</div>
	<div class="page-content">
		<div class="page-inner-content">
			<form id="editorForm">
				<input name="id" type="hidden" />
				<table cellspacing="0" border="0" class="tableTemplet" style="width:900px;">
					<thead>
						<tr height="46">
							<th colspan="3"><i class="fa fa-file-text" style="margin-right: 5px;"></i> <span>基本信息</span>
							</th>
						</tr>
					</thead>

					<tbody id="editorArea">
						<tr>
							<td class="label">类型：</td>
							<td><input data-xtype="text" name="announcementType"
								id="text_title" style="width: 665px; background: #ffffff;"
								value="公告" readonly="readonly" /></td>
						</tr>
						<tr>
							<td class="label">标题：</td>
							<td colspan="1"><input data-xtype="text"
								data-validate="{required:true,maxlength:50}" name="title"
								id="text_title" style="width: 665px; background: #ffffff;" /></td>
						</tr>
						<tr>
							<td class="label">内容：</td>
							<td><iframe id='kendioeditframe' frameborder="0"
									onload="getchildwindow()" width="665px"
									style="overflow: hidden;" height="240px"></iframe></td>
						</tr>
						<tr>
							<td class="label">学校类型：</td>
							<td style="width: 15%">
							<select   id="partakeSchoolType" name="partakeSchoolType"
								style="width: 213px; height: 26px;" class="form-control "
								data-dic="{code:'xxlx'}">
									<option value="">请选择学校类型</option>
							</select>
								</div> <span class="school_validate" style="color: red;"></span></td>
							<input type="hidden" id="SchoolCode" name="SchoolCode"
								style="background: #ffffff;" />
						</tr>
						<tr>
							<td class="label">参与学校：</td>
							<td style="width: 15%">
								<div class="multiselect">
									<select id="partakeSchoolCode" name="partakeSchoolCode"
										class="form-control" multiple="multiple"
										style="width: 213px; height: 26px;">
									</select>
								</div> <span class="school_validate" style="color: red;"></span>
							</td>
							<input type="hidden" id="SchoolCode" name="SchoolCode"
								style="background: #ffffff;" />
						</tr>

						<tr>
							<td class="label">有效时间：</td>
							<td><input data-xtype="datetime" readonly="readonly"
								name="enableTimeStartInput" id="enableTimeStartInput"
								style="width: 210px; height: 20px; background: #ffffff;"
								readonly="readonly" /> 至 <input data-xtype="datetime"
								readonly="readonly" name="enableTimeEndInput"
								id="enableTimeEndInput"
								style="width: 210px; height: 20px; background: #ffffff;"
								readonly="readonly" /></td>
						</tr>
						<tr height="50">
							<td class="label">相关附件：</td>
							<td><input data-xtype="upload" data-appendto="#fileListTD"
								type="file" name="files" id="files" style="width: 667px;"
								data-button-text="上传附件" />
								<div id="fileListTD" style="word-break: break-all;"></div></td>
						</tr>
					</tbody>

				</table>


			</form>
		</div>
	</div>
	<!--<div class="tool-bar"></div>-->
</div>

<div class="full-drop-panel page-content" id="lookAnnouncement"
	style="overflow: auto;">
	<div class="title-bar">
		<h4>
			<i class="fa fa-plus"></i>
		</h4>
		<div class="btn-area">
			<div style="margin-top: 4px;">
				<button type="button" onclick="returnTab();" id="return">
					<i class="fa fa-reply"></i>返回
				</button>
			</div>
		</div>
	</div>
	<div style="padding-top: 50px;">
		<table cellspacing="0" border="0" class="tableTemplet"  style="width: 1024px;">
			<thead>
				<tr height="46">
					<th colspan="3"><i class="fa fa-file-text" style="margin-right: 5px;"></i> <span>公告内容</span>
					</th>
				</tr>
				<tr>
					<td style="height: 30px;"><span
						style="color: #000; font-weight: normal; margin-left: 50px; word-break: break-all;"
						id="context"></span></td>
				</tr>
			</thead>
		</table>

	</div>
	<div>
		<div class="page-inner-content">
			<form id="lookForm">
				<input name="id" type="hidden" /> <input name="announcementId"
					id="announcementId" type="hidden" />
				<table cellspacing="0" border="0" class="tableTemplet"  style="width:1024px;">
					<thead>
						<tr height="46">
							<th colspan="3"><i class="fa fa-file-text" style="margin-right: 5px;"></i> <span>基本信息</span>
							</th>
						</tr>

					</thead>

					<tbody id="editorArea">
						<tr>
							<td class="label">类型:</td>
							<td><span id="announcementType"></span></td>
						</tr>
						<tr>
							<td class="label">标题：</td>
							<td><span id="title" style="word-break: break-all;"></span>
							</td>
						</tr>

						<tr>
							<td class="label">有效日期：</td>
							<td><span id="activeTime"></span></td>
						</tr>
						<tr height="50">
							<td class="label">相关附件：</td>
							<td>
								<div id="filesList" style="word-break: break-all;"></div>
							</td>
						</tr>
						<tr>
							<td class="label">发布人：</td>
							<td><span id="publishPerson"></span></td>
						</tr>
						<tr>
							<td class="label">参与学校：</td>
							<td><span id="partakePersons_span"></span></td>
						</tr>
						<tr>
							<td class="label">发布日期：</td>
							<td><span id="publishTime"></span></td>
						</tr>
					</tbody>


				</table>

				<table cellspacing="0" border="0" id="reply_table"
					class="tableTemplet" style="margin-top: 20px; display: none;">
					<tbody id="replyTB">
						<tr>
							<th style="width: 50px;">序号</th>
							<th style="width: 80px;">回复人</th>
							<th style="width: 150px;">回复时间</th>
							<th>回复内容</th>
							<th style="width: 120px;">操作</th>
						</tr>
					</tbody>
				</table>


			</form>
		</div>
	</div>
</div>
<div class="page-view-panel full-drop-panel"></div>
<script type="text/javascript">
	$(function() {
		var operType = $("#operType").val();
		if (operType == 'add') {
			$("#insertBTN").click();
		}
		if (operType == 'look') {
			lookAnnouncement($("#announceId").val());
		}
		if (operType == 'edit') {
			editOper($("#announceId").val());
		}

	})
</script>
</body>
</html>