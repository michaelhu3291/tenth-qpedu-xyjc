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
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-ui-1.10.4.custom.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/url.js"></script>
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
 	
	#stu_look input{
		width:100%;
		height:100%;
		border:none;
		text-align: center;
		background-color: #FFFFFF;
		
	}
	#stu_look td{
		height:30px;
		text-align: center;
		background-color: #F6F6F6
	}
	#stu_family input{
		width:100%;
		height:100%;
		border:none;
		text-align: center;
		background-color: #FFFFFF;
	}
	#stu_family td{
		height:30px;
	}
	#stu_family{
		width:92%;
		margin-top:20px;
		margin-left:auto;
		margin-right:auto;
		margin-bottom: 20px;
	}
.ui-card{
	border:1px solid #CCC;
	width:220px;
	text-align: center;
	float: left;
	margin-left: 10px;
	margin-top: 10px;
	padding: 5px;
}
.ui-card-img{
	width:200px;
	height:150px;
}
 #gallery { 
  		margin-top:5px;
  		width: 100%;
    	margin-right: auto;
    	margin-left: auto;
    	min-height:330px;
   }
  .gallery.custom-state-active { background: #eee; }
  .gallery li {
  		 float: left; 
  		 width: 260px;
  		 height:110px;
  		 margin-bottom: 10px;
  		 box-shadow: 3px 2px 5px #8A8678;
  		 margin-left:38px;
  	}    
  	 .gallery dl{
  		 margin-left: 95px;
  	 }
  	  .gallery dd{
  	   margin-bottom: 10px;
  	 }
  	    
  	 .ui-img{
   		    width: 115px;
		    height: 100px;
		    float: left;
		    line-height: 110px;
		    margin-top: 5px;
		    margin-left: 5px;
   }
   .addBTN{
   	margin-left: 10px;
   	height: 27px;
   	border: 1px solid #3babe3;
	background: #ffffff;
	font-weight: bold;
	color: #3babe3;"
   }
	.frametab .ui-tabs-panel {
    padding: 0px;
    overflow: auto;
	}
 .ajax-file-upload {
    font-family: Arial, Helvetica, sans-serif;
    font-size: 12px;
    font-weight: bold;
    padding: 0px;
    cursor: pointer;
    height: 22px;
    line-height: 22px;
    margin: 0px; 
    display: block;
    float: left;
    border: 1px solid #e8e8e8;
    color: #888;
    text-decoration: none;
    color: #fff;
    background: #2f8ab9;
    border: none;
    vertical-align: middle;
    width: 100px;
    margin-top: 10px;
} 

/*.ajax-upload-dragdrop {
     background-color: #F6F6F6;
     color: #c9c9c9; 
    text-align: left;
    vertical-align: middle;
    height: 0px; 
    border: 0px solid #aaa;
    
    background-image: linear-gradient(rgb(238, 238, 238) 1%,
 rgb(255, 255, 255) 15%);
    -webkit-transition: border-color ease-in-out .15s, box-shadow
 ease-in-out .15s;
    transition: border-color ease-in-out .15s, box-shadow ease-in-out .15s;
    -webkit-rtl-ordering: logical;
    -webkit-user-select: text;
}
 fileListTD span{
	width:130px;
	text-align: left;
	padding-left: 2px;
	margin-right: 63px;
	word-break: break-all;
}
#fileListTD div{
	margin-right: 63px;
}
#imgFile .ajax-upload-dragdrop{
	height:33px;
}
#imgFile .ajax-file-upload{
	  margin: 3px 3px 3px 3px;
}
#imgFile span{
	  display:none;
} */
.ajax-upload-dragdrop{
	width:300px;
}
.ajax-upload-dragdrop span{
	 display:none;
}

</style>
<script type="text/javascript">
	var listId = "#list2", editorFormId = "#editorForm", pagerId = '#pager2',editorRelatedFormId = "#editorRelatedForm",arr,
	  
	loadIdUrl = "../personalInfo/personalInfor.do?command=loadCurrentId",
	addZhengShuUrl = "../personalInfo/personalInfor.do?command=saveZhengShu";
	
	function initUploadFile()
	{
		$("#uploadfile").uploadFile({
	        url: "<%=request.getContextPath()%>/platform/accessory_.do?command=uploadImg",
	        removeUrl: "",
	        returnType: "JSON",
	        onSuccess: function (files, data, xhr) {
			       var $img = $("<img src='<%=request.getContextPath()%>/platform/accessory_.do?command=loadImg'/>");
			       $("#uploadfile_list").append($img);
	        },
	        showDone: false,
	        showStatusAfterSuccess: false,
	        showMyDelete:true
		});
	}
	
	
	$(function() {
		//initUploadFile();
		
		showSlidePanel(".page-editor-panel").find("h4 i").removeClass();
		_initButtons({
			
		});//from page.common.js
		$("#tblInfo").find("button").button();
		 $( "#tabs" ).tabs( { heightStyle:"fill" } ) ;
		 $("#tabs-2,#tabs-1,#tabs-3,#tabs-7","#tabs-4","#tabs-5","#tabs-6","#tabs-8","#tabs-9").css("height","auto");
		 _initFormControls();//from page.common.js
			_initValidateForXTypeForm(editorFormId, {
	            errorElement: "span",
	            errorPlacement: function(error, element) {
	                error.appendTo(element.parent("td"));
	            }
	        });
		//初始化加载所有信息

		loadStuInfo();
		loadSjjl();
		
			     	    $('#container').highcharts({
					        chart: {
					            type: 'line'
					        },
					        title: {
					            text: '学生学期排名情况'
					        },
					        subtitle: {
					            text: ''
					        },
					        xAxis: {
					            categories: ['1学期', '2学期', '3学期', '4学期', '5学期', '6学期', '7学期', '8学期']
					        },
					        yAxis: {
					            title: {
					                text: ''
					            }
					        },
					        tooltip: {
					            enabled: false,
					            formatter: function() {
					                return '<b>'+ this.series.name +'</b><br/>'+this.x +': '+ this.y +'°C';
					            }
					        },
					        plotOptions: {
					            line: {
					                dataLabels: {
					                    enabled: true
					                },
					                enableMouseTracking: false
					            }
					        },
					        series: [{
					            name: '李达',
					            data: [7, 10, 17, 7, 8, 10, 6, 10]
					        }]
					    });
				 
				 $('#container2').highcharts({
				        chart: {
				            type: 'column'
				        },
				        title: {
				            text: '学生每学期挂科数'
				        },
				        subtitle: {
				            text: ''
				        },
				        xAxis: {
				        	 categories: ['1学期', '2学期', '3学期', '4学期', '5学期', '6学期', '7学期', '8学期']
				        },
				        yAxis: {
				            min: 0,
				            title: {
				                text: ''
				            }
				        },
				        tooltip: {
				            headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
				            pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
				                '<td style="padding:0"><b>{point.y} 科</b></td></tr>',
				            footerFormat: '</table>',
				            shared: true,
				            useHTML: true
				        },
				        plotOptions: {
				            column: {
				                pointPadding: 0.2,
				                borderWidth: 0
				            }
				        },
				        series: [{
				            name: '李达',
				            data: [1, 2, 1, 1, 2, 0, 0, 0]
				        }]
				    });
				 
			     	});

 		$(".ajax-upload-dragdrop").css("width","300px");
 		$(".ajax-file-upload-statusbar").css("width","300px");
		loadStuInfo();//加载学生信息
		loadStuZhengShu();//加载学生校外证书信息
     	    /* $('#container').highcharts({
		        chart: {
		            type: 'line'
		        },
		        title: {
		            text: '学生学期排名情况'
		        },
		        subtitle: {
		            text: ''
		        },
		        xAxis: {
		            categories: ['1学期', '2学期', '3学期', '4学期', '5学期', '6学期', '7学期', '8学期']
		        },
		        yAxis: {
		            title: {
		                text: ''
		            }
		        },
		        tooltip: {
		            enabled: false,
		            formatter: function() {
		                return '<b>'+ this.series.name +'</b><br/>'+this.x +': '+ this.y +'°C';
		            }
		        },
		        plotOptions: {
		            line: {
		                dataLabels: {
		                    enabled: true
		                },
		                enableMouseTracking: false
		            }
		        },
		        series: [{
		            name: '李达',
		            data: [7, 10, 17, 7, 8, 10, 6, 10]
		        }]
		    });
	 
	 $('#container2').highcharts({
	        chart: {
	            type: 'column'
	        },
	        title: {
	            text: '学生每学期挂科数'
	        },
	        subtitle: {
	            text: ''
	        },
	        xAxis: {
	        	 categories: ['1学期', '2学期', '3学期', '4学期', '5学期', '6学期', '7学期', '8学期']
	        },
	        yAxis: {
	            min: 0,
	            title: {
	                text: ''
	            }
	        },
	        tooltip: {
	            headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
	            pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
	                '<td style="padding:0"><b>{point.y} 科</b></td></tr>',
	            footerFormat: '</table>',
	            shared: true,
	            useHTML: true
	        },
	        plotOptions: {
	            column: {
	                pointPadding: 0.2,
	                borderWidth: 0
	            }
	        },
	        series: [{
	            name: '李达',
	            data: [1, 2, 1, 1, 2, 0, 0, 0]
	        }]
	    }); */
	
	

	         //保存新增的社会经历
	           var saveSjjl = function(){
		              saveUrl= "../personalInfo/personalInfor.do?command=saveOrUpdateSjjl";
		              console.info($("#Practice").children("tbody").find("tr[name=sjjl]").length);
		              var list = [];
		              for(i=0;i<$("#Practice").children("tbody").find("tr[name=sjjl]").length;i++){
				               var param={};
				               var tdArr = $("#Practice").children("tbody").find("tr[name=sjjl]").eq(i).find("td");
				               var sjdw = tdArr.eq(0).find("input").val();
				               var sjsjStart = tdArr.eq(1).find("input").val();
				               var sjsjEnd = tdArr.eq(2).find("input").val();
				               var sjnr = tdArr.eq(3).find("input").val();
				               var SjjlId = tdArr.eq(4).find("input").val();
	                  if(( sjdw!=null&& sjdw!="")&&(sjsjEnd!=null&&sjsjEnd!="")&&(sjsjStart!=null&&sjsjStart!="")&&(sjnr!=null&&sjnr!="")){
				               param["sjdw"]=sjdw;
				               param["sjsjStart"]=sjsjStart;
				               param["sjsjEnd"]=sjsjEnd; 
				               param["sjnr"]=sjnr;
				               param["SjjlId"]=SjjlId;
				               list.push(param);						 
			         } 
		            }
		              if(list.length!=arr.length){
		            	  window.message({
				                text: "请仔细检查，提交后不可修改",
				                title: "提醒",
				                buttons: {
				                    "确认": function () {
				                    	 POST(saveUrl, {
				     				        SjjlList:list
				     			    }, function(data) {
				     			    	loadSjjl();
				     			    })
			                         window.top.$(this).dialog("close");
			                        },
				                		
				                     "取消": function () {
				                      window.top.$(this).dialog("close");
				                    }
				                }
				            });
			         
		              }else{
		            	  alert("不能提交空值");
		              }
		             
	          };
	            function loadSjjl(){
	            	var rowNmb = 1;
	            	var trjtcy, row = "";
	            	var searchUrl= "../personalInfo/personalInfor.do?command=loadSjjl";
	            	POST(searchUrl,{},function(data){
	            		var sjjl = data.sjjlList;
	            	    arr = eval(sjjl);
	            	    if(arr.length>0){
	            	    $("#tr1").hide();
	            	    $("#body7 tr:gt(0)").not(":last").remove();  
	            	    }else{
	            	    	$("#tr1").show();
	            	    }
	            	    
	            	    for (var j = 0; j < arr.length; j++) {
	            	        trjtcy = "tr" + rowNmb;
	            	        row = '<tr style="background-color: white;" name ="sjjl"  id="' + trjtcy + '">'
	            	        row+= '<td style="text-align:center"><input type="text" value="' + arr[j].sjdw + '" id ="sjdw" name="sjdw" style="width: 100%;height: 100%;border: none;text-align: center;background-color: #FFFFFF;"disabled="disabled" ></td>'
	            	        row+= '<td style="text-align:center"><input type="text" value="' + arr[j].sjsjStart + '" id ="sjsjStart" name="sjsjStart" style="width: 100%;height: 100%;border: none;text-align: center;background-color: #FFFFFF;"disabled="disabled" ></td>'
	            	        row+= '<td style="text-align:center"><input type="text" value="' + arr[j].sjsjEnd + '" id ="sjsjEnd" name="sjsjEnd" style="width: 100%;height: 100%;border: none;text-align: center;background-color: #FFFFFF;"disabled="disabled" ></td>' 
	            	        row+= '<td style="text-align:center"><input type="text" value="' + arr[j].sjnr + '" id="sjnr"  name="sjnr" style="width: 100%;height: 100%;border: none;text-align: center;background-color: #FFFFFF;"disabled="disabled" ></td>' 
	            	        row+= '<td style="display:none;"><input type="hidden" id="SjjlId" name="SjjlId" value="' + arr[j].id + '" id="sjnr" /></td> '
	            	        row+= '</tr>';
	            	        var afterTr = "#tr" + (rowNmb - 1);
	            	        ++rowNmb;
	            	        $(afterTr).after(row);
	            	    }
	            	})
	            }



				 function loadStuInfo(){
	               	 var searchUrl= "../personalInfo/newStuSchool.do?command=searchPersonalInfo";
	               	 POST(searchUrl,{},function(data){
	               		 var stuPersonInfo = data.stuPersonInfo;
	               		 $("#stuId").val(stuPersonInfo.ID);
	               		 $("#xy_name").val(stuPersonInfo.XY_NAME);
	               		 $("#zymc_name").val(stuPersonInfo.ZY_NAME);
	               		 
	               		 var xbm = stuPersonInfo.XBM;
	               		 var sfpg = stuPersonInfo.SFPG;
	               		 var sfyj = stuPersonInfo.SFYJ;
	               		 var sfqrhj = stuPersonInfo.SFQRHJ;
	               		 if(xbm=="1"){
	               			 $("#xbm_temp").val("男");
	               		 }else{
	               			 $("#xbm_temp").val("女");
	               		 } 
	               		if(sfpg=="1"){
	               			 $("#sfpg_temp").val("是");
	               		 }else{
	               			 $("#sfpg_temp").val("否");
	               		 } 
	               		if(sfyj=="1"){
	               			 $("#sfyj_temp").val("是");
	               		 }else{
	               			 $("#sfyj_temp").val("否");
	               		 } 
	               		if(sfqrhj=="1"){
	               			 $("#sfqrhj_temp").val("是");
	               		 }else{
	               			 $("#sfqrhj_temp").val("否");
	               		 } 
	               		
	               		for(var k in stuPersonInfo){
							$("#"+k).val(stuPersonInfo[k]);
						}
	               		
	               	//照片信息
	               		var haveZP = data.haveZP;
	               		 if(haveZP==1){
	               			$("#uploadTips").remove();
	  					  	$("#stuPhoto").remove();
	      				  	var $img = $("<img id='stuPhoto' style='width:105px;height:135px;margin-top: 5px;' src='<%=request.getContextPath()%>/personalInfo/newStuSchool.do?command=loadPhoto'/>");
	      				  	$("#zpTemp").append($img);
	               		 }
	               		
	               		var familyMember = data.listStuFamilyMember;
	               		if(familyMember.length>0){
	               			for(var i = 0; i < familyMember.length; i++ ){
	               				var str = "<tr>" + "<td style='text-align:center;background-color:white;'>"+ familyMember[i].xm + "</td>"
								+ "<td style='text-align:center;background-color:white;'>"+ familyMember[i].gx+ "</td>" 
								+ "<td style='text-align:center;background-color:white;'>"+ familyMember[i].zzmm + "</td>" 
								+ "<td style='text-align:center;background-color:white;'>"+ familyMember[i].gzdwjzw+ "</td>" 
								+ "<td style='text-align:center;background-color:white;'>"+ familyMember[i].jtdh + "</td>" 
								+ "<td style='text-align:center;background-color:white;'>"+ familyMember[i].yddh + "</td>" 
								+ "<td style='text-align:center;background-color:white;'>"+ familyMember[i].jtdzxx + "</td>" 
								+ "</tr>";
							$("#family").after(str);
	               				
	               			}
	               		}
	               		$("#stu_look input").attr({"readonly":"readonly"});
	                 	})
	                 
	                }
				//加载学生校外证书信息
				function loadStuZhengShu(){
					POST("../personalInfo/personalInfor.do?command=loadZhengShu",{},function(data){
						console.log(data);
						var obj = data.list;
						for(var i=0;i<obj.length;i++){
							var str = "<li class='ui-widget-content ui-corner-tr'><img class='ui-img' src='"+obj[i].path+"'><dl><dd>"
							+obj[i].TIME.substring(0,4)+"年"+obj[i].TIME.substring(4,obj[i].TIME.length)+"月"
							+"</dd><dd>"+obj[i].TITLE+"</dd></dl></li>";
							$("#gallery li:last").before(str);
						}
					})
				}
			
			//取消新生修改
			 function cancelBTN() {
				$(listId).trigger("reloadGrid");
				hideSlidePanel(".page-editor-panel");
				editId = '';
			} 
			function addRow(){
				var str =  "<tr style='background-color: white;'>" + "<td style='text-align:center'><input type='text' style='width: 100%;height: 100%;border: none;text-align: center;background-color: #FFFFFF;'></td>"
				+ "<td style='text-align:center'><input type='text' style='width: 100%;height: 100%;border: none;text-align: center;background-color: #FFFFFF;'></td>" 
				+ "</tr>";
				$('#body8 tr:last').before(str);
			}
			//增加实践经历
			function addJingLiRow(){
				var jttr = $("#Practice").children("tbody").find("tr[name=sjjl]");
				var i = jttr.length+1;
				var trId = "tr"+i;
				var sjsjStart = "sjsjStart"+i;
				var sjsjEnd = "sjsjEnd"+i; 
				var str =  '<tr style="background-color: white;" name ="sjjl" id='+trId+'>'
				+ '<td style="text-align:center"><input type="text" id ="sjdw" data-validate="{required:true,maxlength:19}"  name="sjdw" style="width: 100%;height: 100%;border: none;text-align: center;background-color: #FFFFFF;"></td>'
				+ '<td style="text-align:center"><input type="text" id ='+sjsjStart+' data-xtype="datetime" readonly="readonly" data-validate="{required:true,maxlength:7}"  name="sjsjStart" style="width: 100%;height: 100%;border: none;text-align: center;background-color: #FFFFFF;"></td>' 
				+ '<td style="text-align:center"><input type="text" id ='+sjsjEnd+' data-xtype="datetime" readonly="readonly" data-validate="{required:true,maxlength:7}"  name="sjsjEnd" style="width: 100%;height: 100%;border: none;text-align: center;background-color: #FFFFFF;"></td>' 
				+ '<td style="text-align:center"><input type="text" id="sjnr" name="sjnr" style="width: 100%;height: 100%;border: none;text-align: center;background-color: #FFFFFF;"></td>' 
				+ '<td style="display:none;"><input type="hidden" id="SjjlId" name="SjjlId"/></td> '
				+ '</tr>';
				$('#body7 tr:last').before(str);
				 _initFormControls();
			}
			
			
			function addFamilyRow(){
				var str =  "<tr style='background-color: white;height: 40px;'>" + "<td style='text-align:center'><input type='text' style='width: 100%;height: 100%;border: none;text-align: center;background-color: #FFFFFF;'></td>"
				+ "<td style='text-align:center'><input type='text' style='width: 100%;height: 100%;border: none;text-align: center;background-color: #FFFFFF;'></td>" 
				+ "<td style='text-align:center'><input type='text' style='width: 100%;height: 100%;border: none;text-align: center;background-color: #FFFFFF;'></td>" 
				+ "<td style='text-align:center'><input type='text' style='width: 100%;height: 100%;border: none;text-align: center;background-color: #FFFFFF;'></td>" 
				+ "<td style='text-align:center'><input type='text' style='width: 100%;height: 100%;border: none;text-align: center;background-color: #FFFFFF;'></td>" 
				+ "<td style='text-align:center'><input type='text' style='width: 100%;height: 100%;border: none;text-align: center;background-color: #FFFFFF;'></td>" 
				+ "<td style='text-align:center'><input type='text' style='width: 100%;height: 100%;border: none;text-align: center;background-color: #FFFFFF;'></td>" 
				+ "</tr>";
				$('#body2 tr:last').before(str);
			}
			
			
			function loadExperience()
			{
				if($("#tabs-8 table tr:gt(0)").length>0)return;
				var url="../personalInfo/personalInfor.do?command=loadExperience";
				$.post(url,{},function(data){
					var htm="";
					for(var i in data)
					{
						var item = data[i];
						htm+="<tr><td style=\"text-align:center;\">"+item.CREATETIME+"</td><td>"+item.EVENT+"</td></tr>";
					}
					$("#tabs-8 table tr:gt(0)").remove();
					$("#tabs-8 table tr:eq(0)").after(htm);
				},"json");
			}

	//添加校外证书的内容
		function addZS(){
		var userName = $("#XM").val();
			$("#addZhengShu").dialog({
			    autoOpen: true,
			    resizable: false,
			    height: 350,
			    width: 550,
			    modal: true,
			    closeText:"关闭",
			    buttons: {
			        "确定": function () {
			        	var data = $("#zhengShuForm").getFormData();
			        	if(data.files.length>1){
			        		window.message({
	                             text: "每条记录只限上传一张",
	                             title: "提醒",
	                             buttons: {
	                                 "确认": function () {
	                                	 window.top.$(this).dialog("close");
	                                 }
	                             }
	                         });
			        		return false;
			        	}else if(data.files.length==0){
			        		window.message({
	                             text: "请选择上传照片",
	                             title: "提醒",
	                             buttons: {
	                                 "确认": function () {
	                                	 window.top.$(this).dialog("close");
	                                 }
	                             }
	                         });
			        		return false;
			        	}else if(!/\.(gif|jpg|jpeg|bmp)$/.test(data.files[0].fileName)){
			        		window.message({
	                             text: "图片类型必须是.gif,jpeg,jpg,bmp中的一种",
	                             title: "提醒",
	                             buttons: {
	                                 "确认": function () {
	                                	 window.top.$(this).dialog("close");
	                                 }
	                             }
	                         });
			        		return false;
			        	}
			        	data["stuId"]= $("#stuId").val();
			                $.post(addZhengShuUrl,data, function (data) {
			                    var jsondata = $.parseJSON(data);
			                    if (jsondata.message == '保存成功') {
			                    	 $("#addZhengShu").dialog("close");
			                    	 window.message({
			                             text: "保存成功",
			                             title: "提醒",
			                             buttons: {
			                                 "确认": function () {
			                                	 window.top.$(this).dialog("close");
			                                	 var length = $("#gallery li").length;
						                    	 $("#gallery li").each(function(i){
						                    		 if(i==length-1){
						                    			 return false;
						                    		 }else{
						                    			 this.remove();
						                    		 }
						                    	 });
						                    	 loadStuZhengShu();
			                                 }
			                             }
			                         });
			                    	 
			                        /* $(listId).trigger("reloadGrid"); */
			                        $(listId).trigger("reloadGrid");
			                	    //hideSlidePanel(".page-editor-panel");
			                	    parent.refreshframe("../counselorWork/counselorAudit/counselorAuditCompletedList.do");
			                    } else {
			                        alert("保存失败");
			                    }
			                });
			            },
			            "取消": function () {
			                $(this).dialog("close");
			            }
			    }
			});
			$(".ui-dialog-titlebar-close").css("background-color","#f58d06");
		}
			
			/*加载辅导员信息*/
			function loadFdyInfo(){
				if($("#tabs-9 table tr:gt(0)").length>0)return;
				var url="../personalInfo/personalInfor.do?command=loadFdyInfo";
				$.post(url,{},function(data){
					var htm="";
					for(var i in data)
					{
						var item = data[i];
						var fdyName=item.ChineseName;
						var fdyType=item.fdyType==1?"辅导员":"代理辅导员";
						var startDate=item.STARTDATE;
						var endDate=item.ENDDATE==undefined?"至今":item.ENDDATE;
						var phone=item.phone;
						
						htm+="<tr><td style=\"text-align:center;\">"
						+fdyName+"</td><td>"+phone+"</td><td>"
						+fdyType+"</td><td>"+startDate+"——"+endDate+"</td></tr>";
					}
					$("#tabs-9 table tr:gt(0)").remove();
					$("#tabs-9 table tr:eq(0)").after(htm);
				},"json");
			}
</script>
</head>
<body>
	
 <input id ="stuId" name="stuId" type="hidden" />
<div  class="page-editor-panel full-drop-panel" style="overflow:auto;">
 <div id="tabs" class="frametab ui-tabs ui-widget ui-widget-content ui-corner-all">
     <ul>
      <li><a href="#tabs-1">基本信息</a></li>
      <li><a href="#tabs-2">家庭成员信息</a></li>
      <li><a href="#tabs-4">奖惩信息</a></li>
      <li><a href="#tabs-5">校外证书</a></li>
      <li><a href="#tabs-6">资助信息</a></li>
      <li><a href="#tabs-7" >社会实践经历</a></li>
      <li><a href="#tabs-8" onclick="loadExperience();">个人历程</a></li>
      <li><a href="#tabs-9" onclick="loadFdyInfo()">辅导员信息</a></li>
    </ul>											
    <div id="tabs-1" style="height:800px;">
    <div style="height:561px;overflow:auto">
    <table id="stu_look"style="width:86%;height:561px;margin-top:20px;margin-left:auto;margin-right:auto;margin-bottom: 20px;"  cellspacing="0" border="1">
			<tr>
				<td colspan="9" style="text-align: center;">新生入学信息登记表</td></tr>
				<tr>
				<td colspan="2" style="text-align: center;" >学院</td>
				<td style="width:120px;"> <input id="xy_name" name="xy_name"></td>
				<td style="width: 60px;">专业</td>
				<td > <input id="zymc_name" name="zymc_name"></td>
				<td style="text-align: center;">学号</td>
				<td colspan="3"> <input id="RYH" name="RYH"></td>
			</tr>
			<tr>
				<td rowspan="8" style="width:60px;"><h4 style="width:24px;margin:auto;">个人基本信息</h4></td>
				<td style="width:60px;">姓名</td>
				<td><input id="XM" name="XM"></td>
				<td>性别</td>
				<td style="width:80px;"><input style="width:140px;" id="xbm_temp"></td>
				<td style="width:97px;">出生年月</td>
				<td style="width: 110px;"><input id="CSRQ" name="CSRQ"></td>
				<td rowspan="7" colspan="2">
					<div type="button" id="zpTemp" name="zpTemp"  value="选择照片" style="text-align: center;">
						<h4 id="uploadTips" style="margin:auto;width:74px;">未上传照片</h4>
					</div>
				</td>
			</tr>
			<tr>
				<td>政治面貌</td>
				<td><input id="ZZMM" name="ZZMM"></td>
				<td>民族</td>
				<td><input id="MZ" name="MZ"></td>
				<td>生源地</td>
				<td><input id="SYD" name="SYD"></td>
			</tr>
			<tr>
				<td>是否普高</td>
				<td>
				<!-- <label><input id="sfpg" name="sfpg" type="checkbox" style ="width: 12px;margin-left: 10px;vertical-align: -11px;" value="1">普高</label>
				<label><input id="sfpg" name="sfpg" type="checkbox" style ="width: 12px;vertical-align: -11px;" value="2">艺校</label> -->
					<input id="sfpg_temp">
				</td>
				<td>是否应届</td>
				<td><input id="sfyj_temp" ></td>
				<td>原户籍所在地</td>
				<td><input id="JG" name="JG"></td>
			</tr>
			<tr>
				<td>手机</td>
				<td><input id="YDDH" name="YDDH"></td>
				<td>邮箱</td>
				<td><input id="DZXX" name="DZXX"></td>
				<td>QQ</td>
				<td><input id="JSTXHQQ" name="JSTXHQQ"></td>
			</tr>
			<tr>
				<td>是否迁入户籍</td>
				<td><input id="sfqrhj_temp" type="text" ></td>
				<td>原户籍所在地址</td>
				<td colspan="3"><input id="YHJSZDDZ" name="YHJSZDDZ" readonly></td>
			</tr>
			<tr>
				<td>微信</td>
				<td><input id="JSTXHWX" name="JSTXHWX"></td>
				<td>身份证号码</td>
				<td colspan="3"><input id="SFZJH" name="SFZJH"></td>
			</tr>
			<tr>
				<td>身高</td>
				<td><input id="SG" name="SG"></td>
				<td>体重</td>
				<td><input id="TZ" name="TZ"></td>
				<td>鞋码</td>
				<td colspan="2"><input id="XZM" name="XZM"></td>
			</tr>
			<tr>
				<td>衣服尺寸</td>
				<td><input id="YFCC" name="YFCC"></td>
				<td>三围</td>
				<td colspan="3"><input id="SW" name="SW"></td>
				<td colspan="2" style="color: red">注：___专业必须填写</td>
			</tr>
			<tr>
				<td id="familyInfo" rowspan="2" style="width:60px;"><h4 style="width:24px;margin:auto;">家庭情况</h4></td>
				<td colspan="3">家庭住址（非高中地址具体到省、市、区、路、弄、楼、几零几）</td>
				<td colspan="5"><input id=JTZZ name="JTZZ"></td>
			</tr>
			<tr>
				<td>家庭邮编</td>
				<td><input id="JTYZBM" name="JTYZBM"></td>
				<td>家庭情况</td>
				<td colspan="5"><input id="JTQK" name="JTQK"></td>
			</tr>
			<tr>
				<td rowspan="6"><h4 style="width:24px;margin:auto;">个人经历</h4></td>
				<td colspan="2">小学-高中担任职务</td>
				<td colspan="6"><input type="text" id="CRZW" name="CRZW"></td>
			</tr>
			<tr>
				<td colspan="2">奖惩情况</td>
				<td colspan="6"><input type="text" id="JCQK" name="JCQK"></td>
			</tr>
			<tr>
				<td colspan="2">兴趣爱好</td>
				<td colspan="6"><input type="text" id="XQAH" name="XQAH"></td>
			</tr>
			<tr>
				<td colspan="2">特长</td>
				<td colspan="6"><input type="text" id="TC" name="TC"></td>
			</tr>
			<tr>
				<td colspan="2">有无担任学习校或班级组织职务如学生会主席、班长</td>
				<td colspan="6"><input type="text" id="ZZZW" name="ZZZW"></td>
			</tr>
			<tr>
				<td colspan="2">曾组织过的活动并在其中担任的角色</td>
				<td colspan="6"><input type="text" id="ZZJS" name="ZZJS"></td>
			</tr>
			<tr>
				<td rowspan="3"><h4 style="width:24px;margin:auto;">其他</h4></td>
				<td colspan="2">家庭贫困情况说明</td>
				<td colspan="6"><input type="text" id="JTPKQK" name="JTPKQK"></td>
			</tr>
			<tr>
				<td colspan="2">身体状况</td>
				<td colspan="6"><input type="text" id="JKZKM" name="JKZKM"></td>
			</tr>
			<tr>
				<td colspan="2">理想抱负</td>
				<td colspan="6"><input type="text" id="LXBF" name="LXBF"></td>
			</tr>
			</table>
    </div>
    </div>
   
   <div id="tabs-2">
   		<table id="stu_family" class="tableTemplet"   cellspacing="0" border="0">
			<tbody id="body2">
			<tr id="family" style="height: 50px;background-color: #f6f6f6;">
				<td style="text-align: center;width: 50px;" >姓名</td>
				<td style="text-align: center;width: 50px;" >关系</td>
				<td style="text-align: center;width: 53px;" >政治面貌</td>
				<td style="text-align: center;" >工作单位职务</td>
				<td style="text-align: center;width: 95px;" >家庭电话</td>
				<td style="text-align: center;width: 95px;" >手机</td>
				<td style="text-align: center;width: 125px;" >邮箱</td>
			</tr>
			<!-- <tr style="height:40px;background-color: white;">
				<td><input type="text" ></td>
				<td><input type="text" ></td>
				<td><input type="text" ></td>
				<td><input type="text" ></td>
				<td><input type="text" ></td>
				<td><input type="text" ></td>
				<td><input type="text" ></td>
			</tr>
			<tr style="height: 43px;">
				<td colspan="7" style="text-align: center;">
					<button id="addRow" class="addBTN" onclick="addFamilyRow();">
						<i class="fa fa-plus"></i>新增行
					</button>
					<button id="familySave" class="addBTN" onclick="familySave();">
                       <i class="fa fa-check"></i>保存
                  </button>
				</td>
			</tr> -->
			</tbody>
		</table>
    </div>
    
    
     <div id="tabs-4">
     <h1 style="margin: auto;width: 35%;margin-top: 40px;text-align: center;">校内学年荣誉奖励信息表</h1>
   		<table id="stu_inHonor"style="width:86%;margin-top:20px;margin-left:auto;margin-right:auto;margin-bottom: 20px;"class="tableTemplet"  cellspacing="0" border="0">
			
			<tr style="height: 50px;background-color: #f6f6f6;">
				<td style="text-align: center;" >学年</td>
				<td style="text-align: center;" >奖励等级</td>
				<td style="text-align: center;" >荣誉</td>
			</tr>
			<tr style="height: 35px;background-color: white;">
				<td style="text-align: center;">2014年-2015年</td>
				<td style="text-align: center;">院级</td>
				<td style="text-align: left;">XXX竞赛一等奖</td>
			</tr>
			<tr style="height: 35px;background-color: white;">
				<td style="text-align: center;">2015年-2016年</td>
				<td style="text-align: center;">校级</td>
				<td style="text-align: left;">XX竞赛一等奖</td>
			</tr>
			</table>
			 <h1 style="margin: auto;width: 35%;margin-top: 40px;text-align: center;">校外荣誉奖励信息表</h1>
		<table id="stu_outHonor"style="width:86%;margin-top:20px;margin-left:auto;margin-right:auto;margin-bottom: 20px;"class="tableTemplet"  cellspacing="0" border="0">
			<tr style="height: 50px;background-color: #f6f6f6;">
				<td style="text-align: center;" >年度</td>
				<td style="text-align: center;" >参赛名称</td>
				<td style="text-align: center;" >奖励等级</td>
				<td style="text-align: center;" >举办方</td>
			</tr>
			<tr style="height: 35px;background-color: white;">
				<td style="text-align: center;">2014年03</td>
				<td style="text-align: left;">XX省绘画比赛</td>
				<td style="text-align: left;">一等奖</td>
				<td style="text-align: left;">XX省XX市政府</td>
			</tr>
			<tr style="height: 35px;background-color: white;">
				<td style="text-align: center;">2015年07</td>
				<td style="text-align: left;">超级女声</td>
				<td style="text-align: left;">优秀XXX奖</td>
				<td style="text-align: left;">湖南卫视</td>
			</tr>
		</table>
    </div>
	
	<div id="tabs-5" style="width: 96%;height: 415px;;margin: 19px 30px 56px;">
		<ul id="gallery" class="gallery ui-helper-reset ui-helper-clearfix">
			 <!--  
			  <li class="ui-widget-content ui-corner-tr">
			      <img class="ui-img" src="../theme/image/addzhengshu.jpg" style="background-color: #E6B453;">
					<dl>
							<dd>2012年11月</dd>
							<dd>2012年获学术论文一等奖</dd>
					</dl>
			  </li> -->
			  
			  <li id = "lastLi" class="ui-widget-content ui-corner-tr">
			      <button style="cursor: pointer;" onclick="addZS();"><img class="ui-img" src="../theme//image/addzhengshu.jpg" style="margin-top: 3px;width: 49%;height: 100px;float: none;"></button>
			  </li>
			</ul>
	</div>
	
	<div id="tabs-6" style="width: 640px;height:350px;margin-top:19px;margin-left: auto;margin-right: auto;margin-top: 20px;">
	</div>
	
	<div id="tabs-7">
	<table id='Practice' cellspacing="0" border="0" class="tableTemplet" style="margin-top: 30px;width:90%;">
				<tbody id="body7">
				<tr id="tr0">
				<td style="text-align: center;"><h4>实践单位</h4></td>
				<td style="text-align: center;"><h4>实践开始时间</h4></td>
				<td style="text-align: center;"><h4>实践结束时间</h4></td>
				<td style="text-align: center;"><h4>实践内容</h4></td>
				</tr>
				<tr style="background-color: white;" name = "sjjl"  id="tr1" ><
				<td style="text-align:center"><input type="text" id ="sjdw" data-validate="{required:true,maxlength:19}"  name="sjdw" style="width: 100%;height: 100%;border: none;text-align: center;background-color: #FFFFFF;"></td>
				<td style="text-align:center"><input type="text" data-xtype="datetime" readonly="readonly" id ="sjsjStart" data-validate="{required:true,maxlength:7}"  name="sjsjStart" style="width: 100%; height: 100%; border: none; text-align: center; background-color: #FFFFFF;"></td>
				<td style="text-align:center"><input type="text" data-xtype="datetime" readonly="readonly" id ="sjsjEnd" data-validate="{required:true,maxlength:7}"  name="sjsjEnd" style="width: 100%; height: 100%; border: none; text-align: center; background-color: #FFFFFF;"></td>
				<td style="text-align:center"><input type="text" id="sjnr"  name="sjnr" style="width: 100%;height: 100%;border: none;text-align: center;background-color: #FFFFFF;"></td>
				<td style="display:none;"><input type="hidden" id="SjjlId" name="SjjlId"/></td> 
				</tr>
				</tr>
				<!-- <tr style="background-color: white;">
				<td style="text-align: left;">XX县XX小学</td>
				<td style="text-align: center;">2015-07-02至2015-08-28</td>
				<td style="text-align: left;">在XX省XX市XX县XX小学为小学1年级到四年级的学生辅导功课</td>
				</tr>
				<tr style="background-color: white;">
				<td style="text-align: left;">XXXXXX有限公司</td>
				<td style="text-align: center;">2015-12-03至2015-02-01</td>
				<td style="text-align: left;">在XX工厂。。。。。。</td>
				</tr>
				<tr style="background-color: white;">
				<td style="text-align: left;">XX服装城。。。。。。。</td>
				<td style="text-align: center;">2014-06-30至2014-08-17</td>
				<td style="text-align: left;">在XX服装城。。。。。。。</td>
				</tr> -->
				<tr>
				<td colspan="4" style="text-align: center;height: 25px;">
					<button id="addRow" class="addBTN"  onclick="addJingLiRow();">
						<i class="fa fa-plus"></i>新增行
					</button>
					<button id="saveSjjl" onclick="saveSjjl();" class="addBTN">
                        <i class="fa fa-check"></i>保存
                    </button>
				</td>
				</tr>
				</tbody>
			</table>
    </div>
    <div id="tabs-8">
			<table id='course' cellspacing="0" border="0" class="tableTemplet" style="margin-top: 30px;width:90%;">
				<tbody id="body8">
					<tr>
						<td style="text-align: center;"><h4>事件时间</h4></td>
						<td style="text-align: center;"><h4>事件内容</h4></td>
					</tr>
				<!-- 	<tr style="background-color: white;">
						<td style="text-align: center;">2015-03-03</td>
						<td style="text-align: left;">申请2015-2016年度奖学金记录。。。。。。。</td>
					</tr> -->
		
				</tbody>
			</table>
    </div>
    
     <div id="tabs-9">
     <h1 style="margin: auto;width: 35%;margin-top: 40px;text-align: center;">辅导员信息表</h1>
   		<table id="stu_inHonor"style="width:86%;margin-top:20px;margin-left:auto;margin-right:auto;margin-bottom: 20px;"class="tableTemplet"  cellspacing="0" border="0">
			
			<tr style="height: 50px;background-color: #f6f6f6;">
				<td style="text-align: center;" >姓名</td>
				<td style="text-align: center;" >联系方式</td>
				<td style="text-align:center">辅导员类型</td>
				<td style="text-align: center;" >起止时间</td>
			</tr>
		
			</table>
    </div>
	
  </div>
</div>
<div id="addZhengShu" style="display: none;height: 140px;">
<form id="zhengShuForm">
	<table style="width: 80%; margin: auto;">
		<tr style="height: 50px;">
			<td class="labelTd">
				<label for="remark" style="text-align: right;">事件时间：</label>
			</td>
			<td class="labelTd" style="width: 80%;">
				 <select data-xtype="text" style="width:90px;" name="zhengShuYear" id="zhengShuYear" class="form-control" >
                         <c:forEach items="${zhengShuYear}" var="zhengShuYear">
                         		<option value="${zhengShuYear.DictionaryCode}">${zhengShuYear.DictionaryName}</option>
                         </c:forEach>
                 </select>年
				<select data-xtype="text" style="width:90px;" name="zhengShuMonth" id="zhengShuMonth" class="form-control" >
                         <c:forEach items="${zhengShuMonth}" var="zhengShuMonth">
                         		<option value="${zhengShuMonth.DictionaryCode}">${zhengShuMonth.DictionaryName}</option>
                         </c:forEach>
                 </select>月
			</td>
		</tr>
		<tr style="height: 50px;">
			<td class="labelTd" >
				<label for="remark" style="text-align: right;">证书标题：</label>
			</td>
			<td class="labelTd" style="width: 80%;">
				<input id='zhengShuContent' name="zhengShuContent" type="text" />
			</td>
		</tr>
		<tr style="height: 50px;">
			<td class="labelTd" >
				<label for="remark" style="text-align: right;">相关附件：</label>
			</td>
			<td class="labelTd" style="width: 70%;height: 20px;">
				<!-- <div id="imgFile" style="width:130px;">
					<input data-xtype="upload" data-appendto="#fileListTD"
             					type="file" name="files" id="files" style="width:130px;" data-button-text="图片上传" />				                				
				</div>
				<div id="fileListTD" style="position:absolute;width:153px;height:35px;overflow-y:auto;margin-left:50px;"></div> -->
					<input data-xtype="upload" data-appendto="#fileListTD" 
               		type="file" name="files" id="files"  data-button-text="图片上传" />
               		<div id="fileListTD" style="width:10px;"></div>
			</td>
		</tr>
	</table>
	</form>
</div>
</body>
</html>