<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
<link href="../theme/default/jquery.multiselect.css" rel="stylesheet" />
<link href="../theme/default/jquery.multiselect.filter.css"	rel="stylesheet" />
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
<script type="text/javascript" src="../js/util.js"></script>
<script type="text/javascript" src="../js/jquery.multiselect.js"></script>
<script type="text/javascript" src="../js/jquery.multiselect.filter.js"></script>

<style>
 .customStyle{
		background:#FFFFFF;
		border:1px solid #3babe3;
		color:#3babe3;
		cursor:pointer;
		font-weight: bold;
		padding: .4em 1em;
	}
	.customStyle:hover{
		color:#FFFFFF;
		background:#3babe3;
		}
 	#tblInfo input{
		background-color: #F6F6F6;
	} 
	#stu_look input{
		width:100%;
		height:100%;
		border:1px;
		text-align: center;
		background-color: white;
	}
	#stu_look td{
		height:30px;
	}
	#tabs-2{height: 847px;}
	
	#editorForm tbody td {
		background: #FFF;
	}
	#frame1 li {list-style-type: none; margin-top: 10px;font-size: 14px; font-weight: bold;}
</style>
<script type="text/javascript">
	var listId = "#list2",
	detailId="#list3",
	editorFormId = "#editorForm", 
	 loginNames,
	 names,
	pagerId = '#pager2',
	editorRelatedFormId = "#editorRelatedForm",
	deleteUrl="../dataManage/districtSubjectInstructorInfo.do?command=delete",
	loadDistrictSubjectInstructor="../dataManage/districtSubjectInstructorInfo.do?command=loadDistrictSubjectInstructor",
	listUrl = "../dataManage/districtSubjectInstructorInfo.do?command=selectDistrictSubInstructorPaging",
	verifyCodeUrl="../dataManage/districtSubjectInstructorInfo.do?command=selectUserUidByUserId",
	url ="../dataManage/districtSubjectInstructorInfo.do?command=addUser";
	$(function () {
		
		  /*$("input[name='period']").click(function () {
		    	 $("#course option[value != '']").remove();
		    	var obj=document.getElementsByName('period'); 
		    	var period=[];
		    	 for(var i=0; i<obj.length; i++){ 
		    	 if(obj[i].checked) {
		    		 period.push(obj[i].value);
		    	 }//如果选中，将value添加到变量s中 
		    	 }  
		        	  if (period.length>0) {
		                  var url = "../dataManage/districtSubjectInstructorInfo.do?command=getCoursesBySchoolType";
		                  var data = {
		                		  period: period
		                  };
		                  $.ajax({
		                      url: url,
		                      type: "POST",
		                      data: data,
		                      dataType: "JSON",
		                      success: function (data) {
		                          for (var i = 0; i < data.length; i++) {
		                        	  $("#course").append("<option value='" + data[i].DictionaryCode + "'>" + data[i].DictionaryName + "</option>");
		                          }
		                      }
		                  });
		              }
		         });*/
		  
        $(".search-panel").show().data("show", true);
        
        //多选年级下拉框
        $("#grade").multiselect({
            checkAllText: "全选",
            uncheckAllText: "全不选",
            noneSelectedText: "选择年级",
            selectedText: '#' + "年级",
            minWidth:'270',
            selectedList: 5
 		});

        _initButtons({
            cancelBTN: function () {
	            	 $("#grade option[value!='']").remove();
	                 $("#grade").multiselect('refresh');
	                 loadDictionary();
                     $("#loginName").val("");
                     $("#name").val("");
                     hideSlidePanel(".page-addSubInstructor-panel");
                
                },
                
                updateDistrictSubInstructor : function(ev) {
                	//
                	//$("#grade").find("option:selected").attr("selected",false);
                	//$("#caseEditorForm").resetHasXTypeForm();
                	
                	$("#grade option[value!='']").remove();
                	loadDictionary();
 	       		    $("#grade").multiselect('refresh');
 	       		    
                    $("#tbl").text("");
                    $("#tbls").text("");
                    $('#name').blur(function () { 
                   	 $("#tbls").text("");
                   	    var n=$("#name").val();
                   	    if(n==""){
                   		 $("#tbls").text("姓名不能为空");
                   		 return;
                   	    }
                    });
                    $('#loginName').blur(function () { 
       		    	 /* 登录名 */
       		    	 $("#tbl").text("");
       		    	  var userId=$("#id").val();
       		    	  var loginName=$("#loginName").val();
       		    	  var returnVal=true;
       		 			$.ajax({
       		 	            url: verifyCodeUrl,
       		 	            type: "POST",
       		 	            data: {userId:$("#id").val(),loginName:$("#loginName").val()},
       		 	 	        dataType: "json",
       		 	 	   		async:false,
       		 	 	        success : function (data) {
       		 	            	returnVal=data.flag;
       		 	            	if($("#loginName").val()=="" || $("#loginName").val()==null){
       		 	            	$("#tbl").text("用户名不能为空");
       		 	            	}else{
       		 	            		if(returnVal==false){
       		 	            			$("#tbl").text("用户名已存在");
       		 	 	            	}else{
       		 	 	            		$("#tbl").text("");
       		 	 	            	}
       		 	            	}
       		 		        }
       		 	        });
       		 } );
       		  
                    //$("#caseEditorForm").find("#course option[selected='selected']").attr('selected',false);
          		    //$("#caseEditorForm").find(("input:checkbox[checked='checked']")).attr('checked',false);
                    //$("#course option[value != '']").remove();
    				var $i = $(ev.currentTarget).find("i"),
    				$span = $(ev.currentTarget).find("span"),
    				idAry = $(listId).jqGrid("getGridParam", "selarrrow");
    		        if (idAry.length === 0) {
    		            window.message({
    		                text: "请选择要修改的记录!",
    		                title: "提示"
    		            });
    		            return;
    		        }
    		        if (idAry.length > 1) {
    		            window.message({
    		                text: "每次只能修改单条记录!",
    		                title: "提示"
    		            });
    		            return;
    		        }
    		        
    		      GET(loadDistrictSubjectInstructor, {id: idAry[0]}, function (data) {
    		    	 
    		        	var $piel =showSlidePanel(".page-addSubInstructor-panel").find("h4 i").removeClass(),$span = $(".page-addSubInstructor-panel").find("h4 span");
    		        	if ($span.length) {
    	                    $span.remove();
    	                }
    		        	var courses=data[0].COURSE;
    		        	if(courses=="" || courses==null){
    		        		if ($i.length) {
        	                    $piel.addClass($i.attr("class"));
        	                    $piel.after("<span>修改教研员</span>");
        	                }
    		        	}else{
    		        		if ($i.length) {
        	                    $piel.addClass($i.attr("class"));
        	                    $piel.after("<span>修改学科教研员</span>");
        	                }
    		        	}
    		        	
    		        	//$("#caseEditorForm").resetHasXTypeForm();
    		        	$("#grade").find("option").attr("selected",false);
    		        	for(var i=0;i<data.length;i++){
    		        		   $("#id").val(data[i].USER_ID);
    		        		   $("#name").val(data[i].USER_NAME);
    		        		   $("#loginName").val(data[i].USER_UID);
    		        		   $("#grade").find("option[value='"+data[i].Grade+"']").attr("selected","selected");
    		        		   //$("#grade").multiselect('refresh');
    		        		   loginNames="";
     	                  	  loginNames=$("#loginName").val();
    		        		   
    		        		    //var course= data[i].COURSE;
    		        			 //var period=data[i].PERIOD;
                                 //var periodStr=period.split(", ");
    							
    							//for(var j=0;j<periodStr.length;j++){
    							//	$("input:checkbox[value='"+periodStr[j]+"']").attr("checked","checked");
    							//}
    							   //var obj=document.getElementsByName('period'); 
    			    		    	//var period=[];
    			    		    	 //for(var i=0; i<obj.length; i++){ 
    			    		    	 //if(obj[i].checked) {
    			    		    	//	 period.push(obj[i].value);
    			    		    	 //}
    			    		    	 //}  
    			    		        	  /*if (period.length>0) {
    			    		                  var url = "../dataManage/districtSubjectInstructorInfo.do?command=getCoursesBySchoolType";
    			    		                  var data = {
    			    		                		  period: period
    			    		                  };
    			    		                  $.ajax({
    			    		                      url: url,
    			    		                      type: "POST",
    			    		                      data: data,
    			    		                      dataType: "JSON",
    			    		                      success: function (data) {
    			    		                    	  if(course==null || course==""){
    			    		                    		  $("#courses").hide(); 
			    		    		        		   }else{
			    		    		        			   $("#courses").show(); 
			    		    		        			   for (var i = 0; i < data.length; i++) {
		    			    		                        	  $("#course").append("<option value='" + data[i].DictionaryCode + "'>" + data[i].DictionaryName + "</option>");
		    			    		                          }
		    			    		                          $("#course option[value='"+course+"']").attr("selected",true); 
		    			    		                      }
			    		    		        		   }
    			    		                         
    			    		                  });
    			    		               
    			    		              }*/
    								
    		        	}
    		        	//$("#grade").find("option:selected").attr("selected","selected");
    		        	$("#grade").multiselect('refresh');
    		        	//showSlidePanel(".page-addSubInstructor-panel");
    		        }); 
    			},
    			 deteleInstructor: function () {
    				 var idAry = $(listId).jqGrid("getGridParam", "selarrrow");
    		            if (idAry.length === 0) {
    		                window.message({
    		                    text: "请选择要删除的记录!",
    		                    title: "提示"
    		                });
    		                return;
    		            }
    	            window.message({
    	                text: "确认要删除所选择的记录吗?",
    	                title: "提醒",
    	                buttons: {
    	                    "确认": function () {
    	                        window.top.$(this).dialog("close");
    	                        POST(deleteUrl, {id: idAry}, function (data) {
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

    	        },
        }); //from page.common.js
        $("#tblInfo").find("button").button();
        $("#tabs").tabs({
            heightStyle: "fill"
        });
        $("#tabs-2,#tabs-1").css("height", "auto");
        _initFormControls(); //from page.common.js
        _initValidateForXTypeForm(editorFormId);
        //绑定jqgrid
        var _colModel = [{
                name: 'USER_ID',
                key: true,
                width: 60,
                hidden: true,
                search: false
            }, {
                name: 'USER_NAME',
                label : "姓名",
                sortable: false,
                width: 80,
                align: "center",
            }, {
                name: 'USER_UID',
                label : "登录名",
                sortable: false,
                width: 80,
                align: "center",
            },  {
                name: 'Position_Name',
                label : "职位",
                sortable: false,
                autoWidth: true,
                align: "left",
                /*formatter: function (ar1, ar2, ar3) {
                	if(ar3.COURSE=="" || ar3.COURSE==null){
                		return (_types[ar3.PERIOD]+[ar3.POSITION_NO]);
                	}
                    return (_types[ar3.PERIOD]+_types[ar3.COURSE]+[ar3.POSITION_NO]);
                }*/
            }];
     $(listId).jqGrid($.extend(defaultGridOpts, {
         url: listUrl,
         /* colNames: _colNames, */
         colModel: _colModel,
         pager: pagerId,
		multiselect : true,
	    autowidth: false,
     }));
     resizeFun();
     
     loadDictionary();
     
     $('#loginName').focus(function(){
    	 $("#tbl").text("");
     });
     $('#name').focus(function(){
    	 $("#tbls").text("");
     });
     $("input[name='period']").focus(function(){
    	 $("#tbl1").text("");
     })
     //添加操作
      
	  $("#saveBTN").click(function(ev){
		  var gradeArr =  $("#grade").find("option:selected");
		  if(gradeArr.length == 0){
			  window.Msg.alert("请选择年级");
			  return;
		  }
		  var gradeValArr = [];
		  var gradeTxtArr = [];
		  $.each(gradeArr,function(index,item){
			  gradeValArr.push($(item).val());
			  gradeTxtArr.push($(item).text());
		  });
		  //for(var i=0;i<gradeValArr.length;i++){
			//  alert(gradeValArr[i])
		  //}
		  
		  $.ajax({
	            url: verifyCodeUrl,
	            type: "POST",
	            data: {loginName:$("#loginName").val(),userId:$("#id").val()},
	 	        dataType: "json",
	 	        async:false,
	 	        success : function (data) {
	            	returnVal=data.flag;
	            	if($("#loginName").val()=="" || $("#loginName").val()==null){
	            	$("#tbl").text("用户名不能为空");
	            	}else{
	            		if(returnVal==false){
	            			$("#tbl").text("用户名已存在");
	 	                }else{
						   /*var objs=document.getElementsByName('period');
		    		    	var period1=new Array();
		    		    	 for(var i=0; i<objs.length; i++){ 
		    		    	 if(objs[i].checked) {
		    		    		 period1.push(objs[i].value);
		    		    	  }
		    		    	 } 
		    		    	 
	 	                   //if(period1.length>0){
	 	                	  //alert(777);
	 	                	  $("#tbl1").text("");
 	                    	    var n=$("#name").val();
 	                    	    if(n==""){
 	                    		 $("#tbls").text("姓名不能为空");
 	                    	    }else{
 	                    	    // 添加操作
 	      	 	            	 var obj=document.getElementsByName('period'); 
 	      	 	       	    	 var periods=[];
 	      	 	       	    	 for(var i=0; i<obj.length; i++){ 
 	      	 	       	    	 if(obj[i].checked) {
 	      	 	       	    		 periods.push(obj[i].value);
 	      	 	       	    	  }
 	      	 	       	    	 } 
 	      	 	       	  
 	      	 	       	      var period;
 	      	 	       		  var userId=$("#id").val();
 	      	 	       		  var loginName = $("#loginName").val();
 	      	 	       		  var course= $("#course").find("option:selected").val();
 	      	 	       		  var name=$("#name").val();
 	      	 	       	
 	      	 	       		  var s=[];
 	      	 	       		  $("input:checkbox[name='period']:checked").each(function() { // 遍历name=test的多选框
 	      	 	       			s.push($(this).val());  // 每一个被选中项的值
 	      	 	       		});*/
 	      	 	       	
 	      	 	       		//var b = s.join(", ");
 	      	 	       		 //if(course!=""){
 	      	 	       		//	  name = $("#course").find("option:selected").text()+"教研员";
 	      	 	       		//  }else{
 	      	 	       		//	  name =_types[b]+"教研员";
 	      	 	       		 // }
 	      	 	       		 var userId=$("#id").val();
 	      	 	       		 var loginName = $("#loginName").val();
 	      	 	       		 var course= $("#course").find("option:selected").val();
 	      	 	       		 var txt = gradeTxtArr.join(",");
 	      	 	       		 //alert(txt);
 	      	 	       		 var positionName = txt + "教研员";
 	      	 	       		 var name = $("#name").val();
 	      	 	       		   var data = {userId:userId,loginName:loginName,positionName:positionName,name:name,course:course,gradeValArr:gradeValArr};
 	      	 	       		   if(course != "" && loginNames!=""){
 	      	 	       			  window.message({
 	      	 	       		 			title:'提醒',
 	      	 	       		 			text:'确定修改此学科教研员吗?',
 	      	 	       		 			buttons:{
 	      	 	       		 				'确定':function(){
 	      	 	       		 					window.top.$(this).dialog("close");
 	      	 	       		 					 $.ajax({
 	      	 	       		 				   	       	url:url,
 	      	 	       		 				   	        type:"POST",
 	      	 	       		 				   	        data:data,
 	      	 	       		 				   	        dataType:"JSON",
 	      	 	       		 				   	        success: function(data, xhr) {
 	      	 	       		 				   	        	if(data.mess == 'success'){
 	      	 	       		 				   	        		window.Msg.alert("修改成功!");
 	      	 	       		 				   	        	}
 	      	 	       		 				   	        	$(listId).trigger("reloadGrid");
 	      	 	       		 				   	       		hideSlidePanel(".page-addSubInstructor-panel");
 	      	 	       		 				   	       }
 	      	 	       		 				   	    });  
 	      	 	       		 				},
 	      	 	       		 				'取消':function(){
 	      	 	       		 					window.top.$(this).dialog("close");
 	      	 	       		 				}
 	      	 	       		 			}		
 	      	 	       		 		});
 	      	 	       		  }else if(course!="" && loginNames==""){
 	      		 	       			window.message({
 	      	 	       		 			title:'提醒',
 	      	 	       		 			text:'确定添加吗?',
 	      	 	       		 			buttons:{
 	      	 	       		 				'确定':function(){
 	      	 	       		 					window.top.$(this).dialog("close");
 	      	 	       		 					 $.ajax({
 	      	 	       		 				   	       	url:url,
 	      	 	       		 				   	        type:"POST",
 	      	 	       		 				   	        data:data,
 	      	 	       		 				   	        dataType:"JSON",
 	      	 	       		 				   	        success: function(data, xhr) {
 	      	 	       		 				   	        	if(data.mess == 'success'){
 	      	 	       		 				   	        		window.Msg.alert("添加成功!");
 	      	 	       		 				   	        	}
 	      	 	       		 				   	 			$("#grade option[value!='']").remove();
 	      	 	       		 				   	 			loadDictionary();
 	      	 	       		                 				$("#grade").multiselect('refresh');
 	      	 	       		 				   	        	$(listId).trigger("reloadGrid");
 	      	 	       		 				   	       		hideSlidePanel(".page-addSubInstructor-panel");
 	      	 	       		 				   	       }
 	      	 	       		 				   	    });  
 	      	 	       		 				},
 	      	 	       		 				'取消':function(){
 	      	 	       		 					window.top.$(this).dialog("close");
 	      	 	       		 				}
 	      	 	       		 			}		
 	      	 	       		 		});
 	      		 	       		  }
 	      	 	       		   
 	      	 	       		   else if(course=="" && loginNames==""){
 	      	 	       			window.message({
 	       	       		 			title:'提醒',
 	       	       		 			text:'确定添加此教研员吗?',
 	       	       		 			buttons:{
 	       	       		 				'确定':function(){
 	       	       		 					window.top.$(this).dialog("close");
 	       	       		 					 $.ajax({
 	       	       		 				   	       	url:url,
 	       	       		 				   	        type:"POST",
 	       	       		 				   	        data:data,
 	       	       		 				   	        dataType:"JSON",
 	       	       		 				   	        success: function(data, xhr) {
 	       	       		 				   	        	if(data.mess == 'success'){
 	       	       		 				   	        		window.Msg.alert("添加成功!");
 	       	       		 				   	        	}
 	       	       		 				   	 			$("#grade option[value!='']").remove();
 	       	       		 				   	 			loadDictionary();
 	       		                 						$("#grade").multiselect('refresh');
 	       	       		 				   	        	$(listId).trigger("reloadGrid");
 	       	       		 				   	       		hideSlidePanel(".page-addSubInstructor-panel");
 	       	       		 				   	       }
 	       	       		 				   	    });  
 	       	       		 				},
 	       	       		 				'取消':function(){
 	       	       		 					window.top.$(this).dialog("close");
 	       	       		 				}
 	       	       		 			}		
 	       	       		 		});
 	      	 	       		  }else if(course=="" && loginNames!=""){
 	      	 	       			  window.message({
 	      	 	       		 			title:'提醒',
 	      	 	       		 			text:'确定修改此教研员吗?',
 	      	 	       		 			buttons:{
 	      	 	       		 				'确定':function(){
 	      	 	       		 					window.top.$(this).dialog("close");
 	      	 	       		 					 $.ajax({
 	      	 	       		 				   	       	url:url,
 	      	 	       		 				   	        type:"POST",
 	      	 	       		 				   	        data:data,
 	      	 	       		 				   	        dataType:"JSON",
 	      	 	       		 				   	        success: function(data, xhr) {
 	      	 	       		 				   	        	if(data.mess == 'success'){
 	      	 	       		 				   	        		window.Msg.alert("修改成功!");
 	      	 	       		 				   	        	}
		 	      	 	       		 				   	 	$("#grade option[value!='']").remove();
		 	      	 	       		 						loadDictionary();
		     		                 						$("#grade").multiselect('refresh');
		     		                 						
 	      	 	       		 				   	        	$(listId).trigger("reloadGrid");
 	      	 	       		 				   	       		hideSlidePanel(".page-addSubInstructor-panel");
 	      	 	       		 				   	       }
 	      	 	       		 				   	    });  
 	      	 	       		 				},
 	      	 	       		 				'取消':function(){
 	      	 	       		 					window.top.$(this).dialog("close");
 	      	 	       		 				}
 	      	 	       		 			}		
 	      	 	       		 		});
 	      	 	       		  }
 	                    	    }
	 	                   //}
	 	                   //else{
	 	                	  //$("#tbl1").text("请选择选段");
	 	                   //}
	 	            		 
	 	            		 
	 	            	
	}
											}
										//}
									});

						});

     
     
     
     
     
		//添加学科教研员页面下拉显示
		$("#addDistrictSubInstructor").click(function(ev) {
					$("#grade").find("option:selected").attr("selected",false);
					loginNames="";
                	loginNames=$("#loginName").val();
                	   $('#name').blur(function () { 
                         	 $("#tbls").text("");
                         	    var n=$("#name").val();
                         	    if(n==""){
                         		 $("#tbls").text("姓名不能为空");
                         		 return;
                         	    }
                          });
							$('#loginName').blur(
											function() {
												$("#tbl").text("");
												var loginName = $("#loginName")
														.val();
												var returnVal = true;
												$
														.ajax({
															url : verifyCodeUrl,
															type : "POST",
															data : {
																loginName : $(
																		"#loginName")
																		.val()
															},
															dataType : "json",
															async : false,
															success : function(
																	data) {
																returnVal = data.flag;
																if ($(
																		"#loginName")
																		.val() == ""
																		|| $(
																				"#loginName")
																				.val() == null) {
																	$("#tbl")
																			.text(
																					"用户名不能为空");
																} else {
																	if (returnVal == false) {
																		$(
																				"#tbl")
																				.text(
																						"用户名已存在");
																	} else {
																		$(
																				"#tbl")
																				.text(
																						"");
																	}
																}
															}
														});
											});
							$("#tbl").text("");
							$("#caseEditorForm").resetHasXTypeForm();
							$("#courses").show();
							$("#caseEditorForm").find(
									"#course option[selected='selected']")
									.attr('selected', false);
							$("#caseEditorForm").find(
									("input:checkbox[checked='checked']"))
									.attr('checked', false);
							var $i = $(ev.currentTarget).find("i"), $span = $(
									".page-addSubInstructor-panel").find(
									"h4 span"), $piel = $(
									".page-addSubInstructor-panel").show({
								effect : "slide",
								direction : "up",
								easing : 'easeInOutExpo',
								duration : 900
							}).find("h4 i").removeClass();
							if ($span.length) {
								$span.remove();
							}
							if ($i.length) {
								$piel.addClass($i.attr("class"));
								$piel.after("<span>添加学科教研员</span>");
							}
						});

		$("#addInstructor").click(function(ev) {
			
			loginNames="";
        	loginNames=$("#loginName").val();
        	 $('#name').blur(function () { 
             	 $("#tbls").text("");
             	    var n=$("#name").val();
             	    if(n==""){
             		 $("#tbls").text("姓名不能为空");
             	    }
              });
				$('#loginName').blur(
								function() {
									$("#tbl").text("");
									var loginName = $("#loginName")
											.val();
									var returnVal = true;
									$
											.ajax({
												url : verifyCodeUrl,
												type : "POST",
												data : {
													loginName : $(
															"#loginName")
															.val()
												},
												dataType : "json",
												async : false,
												success : function(
														data) {
													returnVal = data.flag;
													if ($(
															"#loginName")
															.val() == ""
															|| $(
																	"#loginName")
																	.val() == null) {
														$("#tbl")
																.text(
																		"用户名不能为空");
													} else {
														if (returnVal == false) {
															$(
																	"#tbl")
																	.text(
																			"用户名已存在");
														} else {
															$(
																	"#tbl")
																	.text(
																			"");
														}
													}
												}
											});
								});
							$("#tbl").text("");
							$("#caseEditorForm").resetHasXTypeForm();
							$("#caseEditorForm").find(
									"#course option[selected='selected']")
									.attr('selected', false);
							$("#caseEditorForm").find(
									("input:checkbox[checked='checked']"))
									.attr('checked', false);
							$("#courses").hide();
							var $i = $(ev.currentTarget).find("i"), $span = $(
									".page-addSubInstructor-panel").find(
									"h4 span"), $piel = $(
									".page-addSubInstructor-panel").show({
								effect : "slide",
								direction : "up",
								easing : 'easeInOutExpo',
								duration : 900
							}).find("h4 i").removeClass();
							if ($span.length) {
								$span.remove();
							}
							if ($i.length) {
								$piel.addClass($i.attr("class"));
								$piel.after("<span>添加教研员</span>");
							}
						});
	});
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
							
						<td class="buttons">
							<!-- <button id="addDistrictSubInstructor">
								<i class="fa fa-plus"></i>添加学科教研员
							</button>  -->
							<button id="addInstructor">
								<i class="fa fa-plus"></i>添加教研员
							</button>
							<button id="updateDistrictSubInstructor">
								<i class="fa fa-pencil"></i>修改
							</button> 
							<button id="deteleInstructor">
								<i class="fa fa-trash-o"></i>删除
							</button> 
							
						</td>
						<td style="padding-left: 24px; padding-right: 5px;">
							<input
							id="fastQueryText" type="text" placeholder="输入登录名"
							style="line-height: 1em;margin-left:-15px;font-size: 1em; cursor: text;" />
						</td>
						<td>
							<button id="fastSearch" title="查询" style="margin-left:0px;">
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
	<!-- 添加学科教研员 -->
	<div class="page-addSubInstructor-panel full-drop-panel">
		<div class="title-bar">
			<h4>
				<i class="fa fa-plus"></i>
			</h4>
			<div class="btn-area">
				<div style="margin-top: 4px;">
					<button id="saveBTN">
						<i class="fa fa-check"></i>保存
					</button>
					<button id="cancelBTN" >
						<i class="fa fa-times"></i>取消
					</button>
				</div>
			</div>
		</div>
		<div class="page-content">
			<div class="page-inner-content" style="margin: auto;">
			  	<form id="caseEditorForm">
			  	<input  type="hidden" name="id"  id="id" />
				    <table cellspacing="0" border="0" style="width:94.7%;" class="tableTemplet">
		                <!-- <thead>
							<tr>
								<th colspan="4"  style="color: black;">
									<i class="fa fa-file-text"></i>
									<span>添加学科教研员</span>
								</th>
							</tr>
						</thead> -->
						<tbody>
						<tr>
								<td class="label" style="width:15%"><span style="color:red;">*</span>姓名：</td>
								<td style="width:25%">
								<input data-xtype="text" name="name" id="name" />
								<span style="color:red;" id="tbls"></span>
								</td>
							</tr>
							<tr>
								<td class="label" style="width:15%"><span style="color:red;">*</span>登录名：</td>
								<td style="width:25%">
								<input data-xtype="text" name="loginName" id="loginName" />
								<span style="color:red;" id="tbl"></span>
								</td>
							</tr>
							<!--<tr>
								<td class="label" style="width:15%"><span style="color:red;">*</span>学段：</td>
								<td style="width:25%">
								    <input type="checkbox"  value="0" name="period"  id="xx"/>小学
								    <input type="checkbox"  value="1" name="period"  id="cz" />初中 
							        <input type="checkbox"  value="2" name="period"  id="gz" />高中
							        <span style="color:red;" id="tbl1"></span>
								</td>
								
							</tr>--> 
							<!-- <tr id="courses">
							    <td class="label" style="width:15%"><span style="color:red;">*</span>科目：</td>
								<td style="width:25%">
								  <select name="course"  id="course" style="width: 173px;">
								    <option value="">请选择科目</option>
								  </select>
								</td>
							</tr> -->
							
							<tr>
								<td class="label" style="width:15%"><span style="color:red;">*</span>年级：</td>
								<td style="width:25%">
								  	   <select id="grade" name="grade" class="form-control" multiple="multiple" data-dic="{code:'nj'}" style="height: 25px;">
				                       </select>
								</td>
							</tr>
		      </tbody>
		      	 </table>
			</form>
			</div>
		</div>
	</div>


</body>
</html>