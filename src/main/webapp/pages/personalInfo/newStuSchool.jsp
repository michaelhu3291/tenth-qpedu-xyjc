<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<link rel="stylesheet" href="<%=request.getContextPath()%>/theme/default/normalize.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/theme/default/main.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/theme/default/jquery.steps.css">

<link href="<%=request.getContextPath()%>/theme/default/ui.custom.css" rel="stylesheet" />


<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.steps.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/ui.custom.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/ui.jqgrid.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/ui.autosearch.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/ui.chosen.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/ui.uploadfile.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/ui.common.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.validate.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/page.common.js"></script>
<style>
	
	.applyBTN {
		width: 180px;
		height: 180px;
		margin-top: 100px;
	}
	
	.applyDIV {
		width: 33%;
		height: 100%;
		float: left;
		text-align: center;
	}
	#stu_family input{
		width:95%;
		height:85%;
		border:none;
		text-align: center;
		background-color: #caf1bd;
		margin-left:auto;
		margin-right:auto;
	    
	}
	#stu_info input{
		width:95%;
		height:85%;
		border:none;
		text-align: center;
		background-color: #caf1bd;
		margin-left:auto;
		margin-right:auto;
	    
	}
	#stu_info td{
		height:30px;
		text-align: left;
		background-color: #F6F6F6;
	   
	}
	.wizard > .content{
	height:1172px;
	}
</style>

</head>
<body >

        <div class="content"  style="margin-left: 20px;">
            <!-- <h2 style="color: #3BB2E4;">&nbsp;新生入学信息</h2> -->

            <script>
            var editorFormId = "#example-advanced-form";
            var f = [];
                $(function ()
                {
                	loadStuInfo();
                	
                	_initValidateForXTypeForm(editorFormId,{
                        errorPlacement: function(error, element) {
                        	if(!error.html())return;
	                        var $td = element.parent().prev();
	                        $td.find("span").remove();
	                        
	                        var errorMessage ="<span style='color:#F58D06;font-weight:700;'>"+$td.html()+"</span>"+error.html(); 
	                        
                            f.push(errorMessage);
                        }
                    });
                	
                    var form = $("#example-advanced-form").show();
                    var settings = {
                   		headerTag: "h3",
                        bodyTag: "fieldset",
                        transitionEffect: "slideLeft",
                        autoFocus: true,
                        labels: {
                            cancel: "取消",
                            current: "当前步骤:",
                            pagination: "分页",
                            finish: "完成",
                            next: "下一步",
                            previous: "上一步",
                            loading: "正在加载 ..."
                           },
                        onStepChanging: function (event, currentIndex, newIndex)
                        {
                            // Allways allow previous action even if the current form is not valid!
                            if (currentIndex > newIndex)
                            {
                                return true;
                            }
                           
                            // Needed in some cases if the user went back (clean up)
                            if (currentIndex < newIndex)
                            {
                                // To remove error styles
                                form.find(".body:eq(" + newIndex + ") label.error").remove();
                                form.find(".body:eq(" + newIndex + ") .error").removeClass("error");
                            }
                            form.validate().settings.ignore = ":disabled,:hidden";
                            return form.valid();
                        },
                        onStepChanged: function (event, currentIndex, priorIndex)
                        {
                            // Used to skip the "Warning" step if the user is old enough.
                            if (currentIndex === 2 && Number($("#age-2").val()) >= 18)
                            {
                                form.steps("next");
                            }
                            // Used to skip the "Warning" step if the user is old enough and wants to the previous step.
                            if (currentIndex === 2 && priorIndex === 3)
                            {
                                form.steps("previous");
                            }
                        },
                       /*  onFinishing: function (event, currentIndex)
                        {
                            form.validate().settings.ignore = ":disabled";
                            return form.valid();
                        }, */
                        onFinished: function (event, currentIndex)
                        {	
                        	f = [];
                        	if ($(editorFormId + " [data-validate]").valid()) {
                        		
                        		var sfqrhj = $("input[name='SFQRHJ']:checked").val();
                        		var yhjszddz = $("#YHJSZDDZ").val();
                        		if(sfqrhj=="1"&&yhjszddz==""){
        							window.message({
    									text : "选择迁入户籍的同学必须填写原户籍所在地地址",
    					   				title : "提醒",
    								});
    								return;
        						}
                        		
	                        	var saveUrl= "../personalInfo/newStuSchool.do?command=savePersonalInfo";
	                         	var studata=$("#example-advanced-form").getFormData();
	                         	
	                         	var list=[];
	                         	for(var i = 0; i < 4; i++){
	                         		var num = i+1;
	                         		var param={};
	                         		if($("#xm"+i).val()!=null&&$("#xm"+i).val()!=""){
	                         			var id = $("#id"+i).val(),
	                         				xm = $("#xm"+i).val(),
	                         				gx = $("#gx"+i).val(),
	                         				zzmm = $("#zzmm"+i).val(),
	                         				gzdwjzw = $("#gzdwjzw"+i).val(),
	                         				jtdh = $("#jtdh"+i).val(),
	                         				yddh = $("#yddh"+i).val(),
	                         				jtdzxx = $("#jtdzxx"+i).val();
	                         			
	                         			if(xm.length>20){
	                         				window.message({
	        									text : "家庭成员"+num+"：姓名不能超过20个字",
	        					   				title : "提醒",
	        								});
	        								return;
	                         			}
	                         			if(gx.length>20){
	                         				window.message({
	        									text : "家庭成员"+num+"：关系不能超过20个字",
	        					   				title : "提醒",
	        								});
	        								return;
	                         			}
	                         			if(zzmm.length>20){
	                         				window.message({
	        									text : "家庭成员"+num+"：政治面貌不能超过20个字",
	        					   				title : "提醒",
	        								});
	        								return;
	                         			}
	                         			if(gzdwjzw.length>100){
	                         				window.message({
	        									text : "家庭成员"+num+"：工作单位及职务不能超过100个字",
	        					   				title : "提醒",
	        								});
	        								return;
	                         			}
	                   					var mobile_rule = /^1[3|4|5|7|8]\d{9}$/;
	                   					if(!mobile_rule.test(yddh)){
	                   						
		                         				window.message({
		        									text : "家庭成员"+num+"：手机格式有误",
		        					   				title : "提醒",
		        								});
		        								return;
		                         			
	                   					}
	                         			var email_rule = /^[a-zA-Z0-9.!#$%&'*+\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/;
	                         			if(!email_rule.test(jtdzxx)){
	                   						
		                         				window.message({
		        									text : "家庭成员"+num+"邮箱格式有误",
		        					   				title : "提醒",
		        								});
		        								return;
		                         			
	                   					}
	                   					
		                         		param["id"] = $("#id"+i).val();
		                         		param["xm"] = $("#xm"+i).val();
		                         		param["gx"] = $("#gx"+i).val();
		                         		param["zzmm"] = $("#zzmm"+i).val();
		                         		param["gzdwjzw"] = $("#gzdwjzw"+i).val();
		                         		param["jtdh"] = $("#jtdh"+i).val();
		                         		param["yddh"] = $("#yddh"+i).val();
		                         		param["jtdzxx"] = $("#jtdzxx"+i).val();
		                         		list.push(param);
	                         		}
	                         		else continue;
	                         	}
	                         	if(list.length<1){
	                         		window.message({
    									text : "请至少填写一位家庭成员信息",
    					   				title : "提醒",
    								});
    								return;
	                         	}
	                         	
	    						POST(saveUrl,{"studata":studata,"list":list},function(data){
	    							
	                         		console.log(data);
	                         		$("#stuId").val(data.id);
	                         		window.message({
    									text : "个人信息保存成功",
    					   				title : "提醒",
    								});
    								return;
	                         	});
                        	}else{
                        		if(f.length>5){
                        			var f_5 = [];
                        			for( var i = 0 ; i < 5 ; i++){
                        				f_5[i] = f[i];
                        			}
                        			window.message({
        								text : f_5.join("<br/>"),
        				   				title : "提醒",
        							});
                        		}else{
	                         		window.message({
	    								text : f.join("<br/>"),
	    				   				title : "提醒",
	    							});
                        		}
                         	}
                        }	
                 };
                    
                 form.steps(settings).validate({
                     errorPlacement: function errorPlacement(error, element) { element.before(error); },
                     rules: {
                     }
                 });
                 
                 
               
                 
                 $("#addRow").click(function(){
                	 
                	 /*新增家庭成员行  */
                    
                     	var num = $("#familyInfo").attr("rowspan");//获取家庭情况总行
     					var row = $("#familyCount").attr("rowspan");//获取家长的总行
     					
                    		var str = "<tr>" 
                  		+ "<td style='text-align:center'><input type='text' style='width: 100%;height: 100%;border: none;text-align: center;background-color: #caf1bd;'></td>"
     					+ "<td style='text-align:center'><input type='text' style='width: 100%;height: 100%;border: none;text-align: center;background-color: #caf1bd;'></td>" 
     					+ "<td style='text-align:center'><input type='text' style='width: 100%;height: 100%;border: none;text-align: center;background-color: #caf1bd;'></td>" 
     					+ "<td style='text-align:center'><input type='text' style='width: 100%;height: 100%;border: none;text-align: center;background-color: #caf1bd;'></td>" 
     					+ "<td style='text-align:center'><input type='text' style='width: 100%;height: 100%;border: none;text-align: center;background-color: #caf1bd;'></td>"  
     					+ "<td style='text-align:center'><input type='text' style='width: 100%;height: 100%;border: none;text-align: center;background-color: #caf1bd;'></td>" 
     					+ "<td style='text-align:center'><input type='text' style='width: 100%;height: 100%;border: none;text-align: center;background-color: #caf1bd;'></td>" 
     					+ "</tr>";
     					
                    		$("#family").after(str);
                    		$("#familyInfo").attr("rowspan",num*1+1);//合并家庭情况行
     					$("#familyCount").attr("rowspan",row*1+1);//合并家长行
                    
                 });
                 
                 
                 $("input[name='SFQRHJ']").change(function(){
             		var sfqrhj = $("input[name='SFQRHJ']:checked").val();
      				if(sfqrhj=="1"){
      					$("#sfqrhj_star").attr("hidden",false);
      				}else{
      					$("#sfqrhj_star").attr("hidden",true);
      				}
             	});

                });
                
                
              //加载学生信息
                function loadStuInfo(){
               	 var searchUrl= "../personalInfo/newStuSchool.do?command=searchPersonalInfo";
               	 POST(searchUrl,{stuId:$("#stuId").val()},function(data){
               		 var stuPersonInfo = data.stuPersonInfo;
               		 var haveZP = data.haveZP;
               		 
               		 //学院专业班级信息
               		 $("#stuId").val(stuPersonInfo.ID);
               		 $("#xy_name").val(stuPersonInfo.XY_NAME);
               		 $("#zymc_name").val(stuPersonInfo.ZY_NAME);
               		 
               		 //照片信息
               		 if(haveZP==1){
               			$("#uploadTips").remove();
  					  	$("#stuPhoto").remove();
      				  	var $img = $("<img id='stuPhoto' style='width:105px;height:135px;margin-top: auto;' src='<%=request.getContextPath()%>/personalInfo/newStuSchool.do?command=loadPhoto'/>");
      				  	$("#zpTemp").append($img);
               		 }
               		 
               		 var xbm = stuPersonInfo.XBM;
               		 var sfpg = stuPersonInfo.SFPG;
               		 var sfyj = stuPersonInfo.SFYJ;
               		 var sfqrhj = stuPersonInfo.SFQRHJ;
            		$("input[name='XBM'][value='"+xbm+"']").attr({"checked":true}); 
            		$("input[name='SFPG'][value='"+sfpg+"']").attr({"checked":true}); 
            		$("input[name='SFYJ'][value='"+sfyj+"']").attr({"checked":true}); 
            		$("input[name='SFQRHJ'][value='"+sfqrhj+"']").attr({"checked":true}); 
               		 
            		checkYhjszddz();
            		
               		/*
               		以后改成数据字典模式需要的代码，不要删除！
               		$(".mingzu option[value='"+stuPersonInfo.mz+"']").attr({"selected":true});
               		$("#sfpg[value='"+stuPersonInfo.sfpg+"']").attr({"checked":true}); */
               		for(var k in stuPersonInfo){
						$("#"+k).val(stuPersonInfo[k]);
					}
               		var familyMember = data.listStuFamilyMember;
               		if(familyMember.length>0){
               			for(var i = 0; i < familyMember.length; i++ ){
               				$("#xm"+i).val(familyMember[i].xm);
               				$("#gx"+i).val(familyMember[i].gx);
               				$("#gzdwjzw"+i).val(familyMember[i].gzdwjzw);
               				$("#jtdh"+i).val(familyMember[i].jtdh);
               				$("#zzmm"+i).val(familyMember[i].zzmm);
               				$("#yddh"+i).val(familyMember[i].yddh);
               				$("#jtdzxx"+i).val(familyMember[i].jtdzxx);
               				$("#id"+i).val(familyMember[i].id);
               				
               			}
               		}
	            	
                 	})
                 
                }
              	
                function checkYhjszddz(){
     				var sfqrhj = $("input[name='SFQRHJ']:checked").val();
     				if(sfqrhj=="1"){
     					$("#sfqrhj_star").attr("hidden",false);
     				}else{
     					$("#sfqrhj_star").attr("hidden",true);
     				}
     			}
              
              /*
              	form中不能嵌套form，将上传图片的form隐藏在主form之外
              	在主form中添加一个事件让上传的form中事件触发
              */
              function gotoZp(){
            	  $("#image").click();
              }
              function uploadZp(){
            	  
            	  var url="../personalInfo/newStuSchool.do?command=savePhoto&name=1111";
            	  var type = $("#image").val();
            	  var rule = "^[\\S]+\.(jpg|jpeg|bmp|png)$";
            	  var f = $("#image").files;
            	  var options = {
            			  url:"../personalInfo/newStuSchool.do?command=savePhoto&name=1111",
            			  type:'post',
            			  success:function(flag,data,xhr){
            				  if(flag==0){
           					  	window.message({
      								text : "请上传7KB以下的一寸照！",
      				   				title : "提醒",
      							});
            				  }else{
            					  $("#uploadTips").remove();
            					  $("#stuPhoto").remove();
            					  //js页面缓存问题，需要加个随机数
            					  var n=Math.random();
            					  var $img = $("<img id='stuPhoto' style='width:105px;height:135px;margin-top: auto;' src='<%=request.getContextPath()%>/personalInfo/newStuSchool.do?command=loadPhoto&n="+n+"'/>");
                				  $("#zpTemp").append($img);
            				  }
            			  }
            	  };
            	  if(type!=""){
	            	  if( type.match(rule) ){
	            		  $("#zpForm").attr("action",url).ajaxSubmit(options);
	            	  }
	            	  else{
	            		  window.message({
								text : "请上传jpg、png、bmp格式的图片！",
				   				title : "提醒",
							});
						 return;
	            	  }
            	  }else return;
              }
            </script>

            <div id="wizard" style=" height: 900px;margin-top: 20px;">
                	<!-- <h4 style="margin:auto;width:74px;">上传照片（一寸免冠正面彩色电子照片）</h4> -->
					
					<form id="zpForm" method="post" enctype="multipart/form-data" style="display:none">
						<input type="file" id="image" name="zp" onchange="uploadZp()" >
						<input type = "text" id = "test" name = "test">
					</form>
					
				    <form id="example-advanced-form" action="#" style="height:150%;">
				    <input id="stuId" name="stuId" hidden="true">
				    <input id="RXRQ" name="RXRQ" hidden="true">
				    <input id="BJID" name="BJID" hidden="true">
				        <h3>入学流程图</h3>
				        <fieldset>
				        	<div style="width: 100%;height: 100%;">
				        		<!-- 流程图 -->
				        		<img style="width: 100%;height: 1097px;" src="../theme/image/gotoSchool.jpg">
				        	</div>
				        </fieldset>
				     
				        <h3>基本信息填写</h3>
				        <fieldset style="background-color: white;">
				            <table class="tableTemplet" id="stu_info"style="width:100%;margin-top:20px;margin-left:auto;margin-right:auto;margin-bottom: 20px;border-collapse: separate;"  cellspacing="0" border="1">
							<tr>
								<td colspan="9" style="text-align: center;">新生入学信息登记表</td></tr>
								<tr>
								<td colspan="2" style="text-align: center;"style="width:101px;" >学院</td>
								<td style="width:120px;"> <input id="xy_name" name="xy_name" readonly style="background-color: white;"></td>
								<td style="width: 70px;">专业</td>
								<td > <input id="zymc_name" name="zymc_name" readonly style="background-color: white;"></td>
								<td style="text-align: center;">学号</td>
								<td colspan="3"> <input id="RYH" name="RYH" readonly style="background-color: white;"></td>
							</tr>
							<tr>
								<td rowspan="8" style="width:55px;"><h4 style="width:24px;margin:auto;">个人基本信息</h4></td>
								<td style="width:70px;"><span style="color:red">*</span>姓名</td>
								<td><input id="XM" name="XM" data-validate="{required:true,maxlength:20}" ></td>
								<td><span style="color:red">*</span>性别</td>
								<td style="width:140px;">
								<!-- 	 <input style="width:100%;" id="xbm" name="xbm"> -->
									<div style="background-color:#caf1bd;width:95%;height:85%;margin-left:auto;margin-right:auto;">
										<li style="list-style-type: none;float: left;height: 12px;margin-left: 20%;">
											<input  name="XBM" type="radio" style="width: 15px;margin-left: 15px;height: inherit;margin: auto;float: left;margin-top: 5px;" value="1">
											<label style="float: left;">男</label>
										</li>
										
										<li style="list-style-type: none;">
											<input  name="XBM" type="radio" style="width: 15px;height: inherit;vertical-align: -11px;float: left;margin-top: 5px;margin-left: 10px;" value="2">
											<lable style="float: left;">女</lable>
										</li>
									</div>
								</td>
								<td style="width:97px;"><span style="color:red">*</span>出生年月</td>
								<td style="width:110px;"><input id="CSRQ" name="CSRQ" data-validate="{required:true,maxlength:8,minlength:8}"></td>
								<td rowspan="4" colspan="2" style="margin:auto;width:74px;">
									<!-- <h4 style="margin:auto;width:74px;">上传照片（一寸免冠正面彩色电子照片）</h4> -->
									<div type="button" id="zpTemp" name="zpTemp" onclick="gotoZp()" value="选择照片" style="text-align: center;">
										<img id="uploadTips" style="width: 100%;height:100%;" src="../theme/image/uploadPhoto.png">
									</div>
									
								</td>
							</tr>
							<tr>
								<td><span style="color:red">*</span>政治面貌</td>
								<td><input id="ZZMM" name="ZZMM" data-validate="{required:true,maxlength:20}"></td>
								<td><span style="color:red">*</span>民族</td>
								<td>
									<input id="MZ" name="MZ" data-validate="{required:true,maxlength:20}">
									<!-- 
									以后改成数据字典模式需要的代码，不要删除！
									<select  id="mz"  name="mz" class="mingzu" style='width: 100%;height:100%; text-align: center;background-color: #caf1bd;'>
										<option value="1">汉族</option>
										<option value="2">壮族</option>
										<option value="3">朝鲜族</option>
									</select> -->
								</td>
								<td><span style="color:red">*</span>生源地</td>
								<td><input id="SYD" name="SYD" data-validate="{required:true,maxlength:20}"></td>
							</tr>
							<tr>
								<td><span style="color:red">*</span>是否普高</td>
								<td>
									<!-- <input id="sfpg" name="sfpg"> -->
									<div style="background-color:#caf1bd;width:95%;height:85%;margin-left:auto;margin-right:auto;">
										<li style="list-style-type: none;float: left;height: 12px;margin-left: 20%;">
											<input  name="SFPG" type="radio" style="width: 15px;margin-left: 15px;height: inherit;margin: auto;float: left;margin-top: 8px;" value="1">
											<label style="float: left;margin-top: 5px;">是</label>
										</li>
										
										<li style="list-style-type: none;">
											<input  name="SFPG" type="radio" style="width: 15px;height: inherit;vertical-align: -11px;float: left;margin-top: 8px;margin-left: 10px;" value="2">
											<lable style="float: left;margin-top: 5px;">否</lable>
										</li>
									</div>
								</td>
								<td><span style="color:red" >*</span>是否应届</td>
								<td>
									<!-- <input id="sfyj" name="sfyj"> -->
									<div style="background-color:#caf1bd;width:95%;height:85%;margin-left:auto;margin-right:auto;">
										<li style="list-style-type: none;float: left;height: 12px;margin-left: 20%;">
											<input  name="SFYJ" type="radio" style="width: 15px;margin-left: 15px;height: inherit;margin: auto;float: left;margin-top: 8px;" value="1">
											<label style="float: left;margin-top: 5px;">是</label>
										</li>
										
										<li style="list-style-type: none;">
											<input  name="SFYJ" type="radio" style="width: 15px;height: inherit;vertical-align: -11px;float: left;margin-top: 8px;margin-left: 10px;" value="2">
											<lable style="float: left;margin-top: 5px;">否</lable>
										</li>
									</div>
								</td>
								<td><span style="color:red" >*</span>原户籍所在地</td>
								<td><input id="JG" name="JG" data-validate="{required:true,maxlength:20}"></td>
							</tr>
							<tr>
								<td><span style="color:red">*</span>手机</td>
								<td><input id="YDDH" name="YDDH" data-validate="{required:true,mobile:true}"></td>
								<td><span style="color:red">*</span>邮箱</td>
								<td><input id="DZXX" name="DZXX" data-validate="{required:true,email:true,maxlength:20}"></td>
								<td>QQ</td>
								<td><input id="JSTXHQQ" name="JSTXHQQ" data-validate="{maxlength:20}"></td>
							</tr>
							<tr>
								<td>是否迁入户籍</td>
								<td>
									<!-- <input id="sfqrhj" type="text" name="sfqrhj" onchange="checkYhjszddz();"> -->
									<div style="background-color:#caf1bd;width:95%;height:85%;margin-left:auto;margin-right:auto;">
										<li style="list-style-type: none;float: left;height: 12px;margin-left: 20%;">
											<input  name="SFQRHJ" type="radio" style="width: 15px;margin-left: 15px;height: inherit;margin: auto;float: left;margin-top: 16px;" value="1">
											<label style="float: left;margin-top: 10px;">是</label>
										</li>
										
										<li style="list-style-type: none;">
											<input  name="SFQRHJ" type="radio" style="width: 15px;height: inherit;vertical-align: -11px;float: left;margin-top: 16px;margin-left: 10px;" value="2" checked>
											<lable style="float: left;margin-top: 10px;">否</lable>
										</li>
									</div>
								</td>
								<td><span id="sfqrhj_star" type="text" style="color:red" hidden="true">*</span>原户籍所在地址</td>
								<td colspan="4"><input id="YHJSZDDZ" name="YHJSZDDZ" data-validate="{maxlength:50}" ></td>
							</tr>
							<tr>
								<td>微信</td>
								<td><input id="jstxhwx" name="jstxhwx" data-validate="{maxlength:20}" ></td>
								<td><span style="color:red">*</span>身份证号码</td>
								<td colspan="4"><input id="SFZJH" name="SFZJH" data-validate="{required:true,idCard:true}"  ></td>
							</tr>
							<tr>
								<td>身高</td>
								<td><input id="SG" name="SG" data-validate="{maxlength:20}" ></td>
								<td>体重</td>
								<td><input id="TZ" name="TZ" data-validate="{maxlength:20}" ></td>
								<td>鞋码</td>
								<td colspan="2"><input id="XZM" name="XZM" data-validate="{maxlength:20}" ></td>
							</tr>
							<tr>
								<td>衣服尺寸</td>
								<td><input id="YFCC" name="YFCC" data-validate="{maxlength:20}" ></td>
								<td>三围</td>
								<td colspan="3"><input id="SW" name="SW" data-validate="{maxlength:20}" ></td>
								<td colspan="2" style="color: red">注：___专业必须填写</td>
							</tr>
							<tr>
								<td id="familyInfo" rowspan="7" ><h4 style="width:24px;margin:auto;">家庭情况</h4></td>
								<td>家庭邮编</td>
								<td><input id="YTYZBM" name="JTYZBM" data-validate="{maxlength:20}"></td>
								<td>家庭情况</td>
								<td colspan="5"><input id="JTQK" name="JTQK" data-validate="{maxlength:20}"></td>
							</tr>
							<tr>
								<td colspan="3">家庭住址（非高中地址具体到省、市、区、路、弄、楼、几零几）</td>
								<td colspan="5"><input id="JTZZ" name="JTZZ" data-validate="{maxlength:100}"></td>
							</tr>
							<tr id="family"   style="height:50px;">
									<td rowspan="5" id="familyCount" rowspan="1">
										<p style="width:0px;margin:auto">家庭成员</p>
									</td>
									<td style="text-align: center;width: 65px;" >姓名</td>
									<td style="text-align: center;width: 55px;" >关系</td>
									<td style="text-align: center;width: 80px;" >政治面貌</td>
									<td style="text-align: center;width: 150px;" >工作单位职务</td>
									<td style="text-align: center;width: 100px;" hidden="true" >家庭电话</td>
									<td style="text-align: center;" >手机</td>
									<td style="text-align: center;width: 130px;" >邮箱</td>
								</tr>
								<tr style="height:50px;">
									<td style="text-align: center;" ><input type="text" id="xm0"></td>
									<td style="text-align: center;" ><input type="text" id="gx0" ></td>
									<td style="text-align: center;" ><input type="text" id="zzmm0" ></td>
									<td style="text-align: center;" ><input type="text" id="gzdwjzw0" ></td>
									<td style="text-align: center;" hidden="true"><input type="text" id="jtdh0"></td>
									<td style="text-align: center;" ><input type="text" id="yddh0" ></td>
									<td style="text-align: center;" ><input type="text" id="jtdzxx0" ><input type="text" id="id0" style="display:none;"></td>
								</tr>
								<tr style="height:50px;">
									<td style="text-align: center;" ><input type="text" id="xm1"></td>
									<td style="text-align: center;" ><input type="text" id="gx1"></td>
									<td style="text-align: center;" ><input type="text" id="zzmm1"></td>
									<td style="text-align: center;" ><input type="text" id="gzdwjzw1"></td>
									<td style="text-align: center;" hidden="true"><input type="text" id="jtdh1"></td>
									<td style="text-align: center;" ><input type="text" id="yddh1"></td>
									<td style="text-align: center;" ><input type="text" id="jtdzxx1"><input type="text" id="id1" style="display:none;"></td>
								</tr>
								<tr style="height:50px;">
									<td style="text-align: center;" ><input type="text" id="xm2"></td>
									<td style="text-align: center;" ><input type="text" id="gx2"></td>
									<td style="text-align: center;" ><input type="text" id="zzmm2"></td>
									<td style="text-align: center;" ><input type="text" id="gzdwjzw2"></td>
									<td style="text-align: center;" hidden="true"><input type="text" id="jtdh2"></td>
									<td style="text-align: center;" ><input type="text" id="yddh2"></td>
									<td style="text-align: center;" ><input type="text" id="jtdzxx2"><input type="text" id="id2" style="display:none;"></td>
								</tr>
								<tr style="height:50px;">
									<td style="text-align: center;" ><input type="text" id="xm3"></td>
									<td style="text-align: center;" ><input type="text" id="gx3"></td>
									<td style="text-align: center;" ><input type="text" id="zzmm3"></td>
									<td style="text-align: center;" ><input type="text" id="gzdwjzw3"></td>
									<td style="text-align: center;" hidden="true"><input type="text" id="jtdh3"></td>
									<td style="text-align: center;" ><input type="text" id="yddh3"></td>
									<td style="text-align: center;" ><input type="text" id="jtdzxx3"><input type="text" id="id3" style="display:none;"></td>
								</tr>
								<tr>
									<td rowspan="6"><h4 style="width:24px;margin:auto;">个人经历</h4></td>
									<td colspan="2">小学-高中担任职务</td>
									<td colspan="6"><input type="text" id="CRZW" name="CRZW" data-validate="{maxlength:100}"></td>
								</tr>
								<tr>
									<td colspan="2">奖惩情况</td>
									<td colspan="6"><input type="text" id="JCQK" name="JCQK" data-validate="{maxlength:100}"></td>
								</tr>
								<tr>
									<td colspan="2">兴趣爱好</td>
									<td colspan="6"><input type="text" id="XQAH" name="XQAH" data-validate="{maxlength:50}"></td>
								</tr>
								<tr>
									<td colspan="2">特长</td>
									<td colspan="6"><input type="text" id="TC" name="TC" data-validate="{maxlength:20}"></td>
								</tr>
								<tr>
									<td colspan="2">有无担任学习校或班级组织职务如学生会主席、班长</td>
									<td colspan="6"><input type="text" id="ZZZW" name="ZZZW" data-validate="{maxlength:100}"></td>
								</tr>
								<tr>
									<td colspan="2">曾组织过的活动并在其中担任的角色</td>
									<td colspan="6"><input type="text" id="ZZJS" name="ZZJS" data-validate="{maxlength:50}"></td>
								</tr>
								<tr>
									<td rowspan="3"><h4 style="width:24px;margin:auto;">其他</h4></td>
									<td colspan="2">家庭贫困情况说明</td>
									<td colspan="6"><input type="text" id="JTPKQK" name="JTPKQK" data-validate="{maxlength:20}"></td>
								</tr>
								<tr>
									<td colspan="2">身体状况</td>
									<td colspan="6"><input type="text" id="JKZKM" name="JKZKM" data-validate="{maxlength:20}"></td>
								</tr>
								<tr>
									<td colspan="2">理想抱负</td>
									<td colspan="6"><input type="text" id="LXBF" name="LXBF" data-validate="{maxlength:20}"></td>
								</tr>
							</table>
				        </fieldset>
				    </form>
            </div>
        </div>
    </body>
</body>
</html>