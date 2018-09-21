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
		
	}
	#stu_look td{
		height:30px;
		text-align: center;
		background-color: #F6F6F6;
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
  		 margin-left: 60px;
  	 }
  	  .gallery dd{
  	   margin-bottom: 10px;
  	 }
  	    
  	 .ui-img{
   		    width: 80px;
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

</style>
<script type="text/javascript">
	var listId = "#list2", editorFormId = "#editorForm", pagerId = '#pager2',editorRelatedFormId = "#editorRelatedForm",
	  
	loadIdUrl = "../personalInfo/personalInfor.do?command=loadCurrentId";
	$(function() {
		showSlidePanel(".page-editor-panel").find("h4 i").removeClass();
		_initButtons();//from page.common.js
		$("#tblInfo").find("button").button();
		 $( "#tabs" ).tabs( { heightStyle:"fill" } ) ;
		 $("#tabs-2,#tabs-1,#tabs-3,#tabs-7","#tabs-4","#tabs-5","#tabs-6","#tabs-8").css("height","auto");
		_initFormControls();//from page.common.js
		_initValidateForXTypeForm(editorFormId);
		
		//初始化加载所有信息
		searchAllInfor();
		
		
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
	
				//初始化加载所有信息
				 function searchAllInfor(){
					  var stuInforUrl = "<%=request.getContextPath()%>/personalInfo/personalInfor.do?command=searchStuInfor";
					  var $grid=$(listId);
					  var data= $grid.jqGrid("getRowData",id);
					  POST(stuInforUrl, {
							stuNumber:data.STU_NUMBER,
						}, function(data) {
							var obj = data.family;
							for(var k in data){
								 if(k.indexOf("DAY")>0)
					    		 {  
					    			 var vl = (data[k]&&k.indexOf("DAY")>0&&!isNaN(data[k]*1))?dateFormat(new Date(data[k]*1),"yyyy-MM-dd"):data[k];
					    			 $("#"+k).val(vl);
					    		 }else{ 
								$("#"+k).val(data[k]);
							}
							}
							if(obj.length>0){
								var num = $("#familyInfo").attr("rowspan");//获取家庭情况总行
								var row = $("#familyCount").attr("rowspan");//获取家长的总行
								for(var i = 0;i<obj.length;i++){
									var str = "<tr>" + "<td style='text-align:center'>"+ obj[i].NAME + "</td>"
											+ "<td style='text-align:center'>"+ obj[i].RELATION+ "</td>" 
											+ "<td style='text-align:center'>"+ obj[i].POLITICS_STATUS + "</td>" 
											+ "<td style='text-align:center'>"+ obj[i].JOB+ "</td>" 
											+ "<td style='text-align:center'>"+ obj[i].PHONE + "</td>" 
											+ "<td style='text-align:center'>"+ obj[i].MOBILE + "</td>" 
											+ "<td style='text-align:center'>"+ obj[i].MAIL + "</td>" 
											+ "</tr>";
										$("#family").after(str);
									}
								$("#familyInfo").attr("rowspan",num*1+obj.length);//合并家庭情况行
								$("#familyCount").attr("rowspan",row*1+obj.length);//合并家长行
								
							}
						});
					  
				}
		
			
</script>
</head>
<body>
	
 <input id ="stuId" name="stuId" type="hidden" />
<div class="page-editor-panel full-drop-panel" style="overflow:auto;">
 <div id="tabs" class="frametab ui-tabs ui-widget ui-widget-content ui-corner-all">
     										
    <div id="tabs-1" style="height:800px;">
              <h1 style="text-align: center">预备党员考察登记表</h1>
    <table id="stu_look"style="width:86%;margin-top:20px;margin-left:auto;margin-right:auto;margin-bottom: 20px;"  cellspacing="0" border="1">
			<tr>
				<td style="width:60px;">姓名</td>
				<td style="width:120px;"><input id="STU_NAME" name="STU_NAME"></td>
				<td style="width:100px;">性别</td>
				<td style="width:80px;"><input style="width:140px;" id="SEX" name="SEX"></td>
				<td style="width:97px;">出生年月</td>
				<td style="width: 110px;"><input id="BIRTHDAY" name="BIRTHDAY"></td>
				<td style="width:97px;">文化程度</td>
				<td style="width: 110px;"><input id="whcd" name="whcd"></td>
				
			</tr>
			<tr>
				<td colspan="2">职务∕职称</td>
				<td colspan="2"><input id="zwzc" name="zwzc"></td>
				<td>入党年月</td>
				<td><input id="rdny" name="rdny"></td>
				<td rowspan="2">介绍人姓名</td>
				<td><input id="csrxmA" name="jcrxmA"></td>
			</tr>
			<tr>
				<td colspan="2">部门或班级</td>
				<td colspan="4"><input id="bmbj" name="bmbj"></td>
				<td><input id="csrxmB" name="jcrxmB"></td>
			</tr>
			<tr>
				<td colspan="2">何支部发展</td>
				<td colspan="3"><input id="hzbfc" name="hzbfz"></td>
				<td colspan="2">预备党员联系电话</td>
				<td ><input id="ybdydh" name="ybdydh"></td>
				
			</tr>
			<tr style="height: 120px">
				<td>入党时主要优缺点</td>
				<td colspan="7"><textarea style="height: 100%; width: 100%;"></textarea></td>
			</tr>
			<tr style="height: 120px">
				<td>参加党内活动情况</td>
				<td colspan="7"><textarea style="height: 100%; width: 100%;"></textarea></td>
			</tr>
			<tr style="height: 120px">
				<td>工作学习情况</td>
				<td colspan="7"><textarea style="height: 100%; width: 100%;"></textarea></td>
			</tr>
			<tr style="height: 120px">
				<td>第一季度考察情况</td>
				<td colspan="7"><textarea style="height: 100%; width: 100%;"></textarea></td>
			</tr>
			<tr style="height: 120px">
				<td>第二季度考察情</td>
				<td colspan="7"><textarea style="height: 100%; width: 100%;"></textarea></td>
			</tr>
			<tr  style="height: 120px">
			    <td rowspan="3">半年考察情况意见汇总</td>
			    <td colspan="7">
			    
			          <p align="left" style="height: 20px">&nbsp;&nbsp;&nbsp;&nbsp;介绍人意见：</p>
			          <p style="height: 60px"></p>
			          <p align="right">介绍人签名： &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  </p>
			          <p align="right">年&nbsp;&nbsp;&nbsp;&nbsp;月&nbsp;&nbsp;&nbsp;&nbsp;日&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </p>
			      
			    </td>
			</tr>
			<tr  style="height: 120px">
			    
			    <td colspan="7">
			          <p align="left" style="height: 20px">&nbsp;&nbsp;&nbsp;&nbsp;党支部意见：</p>
			          <p style="height: 60px"></p>
			          <p align="right">支部书记签名： &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  </p>
			          <p align="right">年&nbsp;&nbsp;&nbsp;&nbsp;月&nbsp;&nbsp;&nbsp;&nbsp;日&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </p>
			      
			    </td>
			</tr>
			<tr  style="height:120px">
			    
			    <td colspan="7">
			         <p align="left" style="height: 20px">&nbsp;&nbsp;&nbsp;&nbsp;党总支意见：</p>
			          <p style="height: 60px"></p>
			          <p align="right">党总支书记签名： &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  </p>
			          <p align="right">年&nbsp;&nbsp;&nbsp;&nbsp;月&nbsp;&nbsp;&nbsp;&nbsp;日&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </p>
			      
			    </td>
			</tr>
			<tr style="height: 120px">
				<td>第三季度考察情况</td>
				<td colspan="7"><textarea style="height: 100%; width: 100%;"></textarea></td>
			</tr>
			<tr style="height: 120px">
				<td>第四季度考察情况</td>
				<td colspan="7"><textarea style="height: 100%; width: 100%;"></textarea></td>
			</tr>
			<tr style="height: 120px">
				<td>获奖情况</td>
				<td colspan="7"><textarea style="height: 100%; width: 100%;"></textarea></td>
			</tr>
			<tr style="height: 120px">
				<td rowspan="4">全年考察情况意见汇总</td>
				<td colspan="7">
				      <p align="left" style="height: 20px">&nbsp;&nbsp;&nbsp;&nbsp;介绍人意见：</p>
			          <p style="height: 60px"></p>
			          <p></p>
			          <p></p>
				</td>
			</tr>
			<tr style="height: 120px">
				
				<td colspan="7">
				      <p align="left" style="height: 20px">&nbsp;&nbsp;&nbsp;&nbsp;听取党内外群众意见：</p>
			          <p style="height: 60px"></p>
			          <p></p>
			          <p></p>
				</td>
			</tr>
			<tr style="height: 120px">
				
				<td colspan="7">
				      <p align="left" style="height: 20px">&nbsp;&nbsp;&nbsp;&nbsp;党支部意见：</p>
			          <p style="height: 60px"></p>
			          <p align="right">支部书记签名： &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  </p>
			          <p align="right">年&nbsp;&nbsp;&nbsp;&nbsp;月&nbsp;&nbsp;&nbsp;&nbsp;日&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </p>
			      
				</td>
			</tr>
			<tr style="height: 120px">
				
				<td colspan="7">
				      <p align="left" style="height: 20px">&nbsp;&nbsp;&nbsp;&nbsp;党委或党总支意见：</p>
			          <p style="height: 60px"></p>
			          <p align="right">书记签名： &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  </p>
			          <p align="right">年&nbsp;&nbsp;&nbsp;&nbsp;月&nbsp;&nbsp;&nbsp;&nbsp;日&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </p>
			      
				</td>
			</tr>
			<tr style="height: 100px">
				<td>备注</td>
				<td colspan="7"><textarea style="height: 100%; width: 100%;"></textarea></td>
			</tr>
			</table>
    </div>
   
    
	
	
	
    
	
  </div>
</div>
	<div class="page-view-panel full-drop-panel"></div>
</body>
</html>