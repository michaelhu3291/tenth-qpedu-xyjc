<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="../../theme/default/ui.custom.css" rel="stylesheet" />
<link href="../../theme/default/ui.jqgrid.css" rel="stylesheet" />
<link href="../../theme/default/font.awesome.css" rel="stylesheet" />
<link href="../../theme/default/ui.chosen.css" rel="stylesheet" />
<link href="../../theme/default/ui.uploadfile.css" rel="stylesheet" />
<link href="../../theme/default/page.common.css" rel="stylesheet" />
<script type="text/javascript" src="../../js/jquery.js"></script>
<script type="text/javascript" src="../../js/ui.custom.js"></script>
<script type="text/javascript" src="../../js/ui.jqgrid.js"></script>
<script type="text/javascript" src="../../js/ui.autosearch.js"></script>
<script type="text/javascript" src="../../js/ui.chosen.js"></script>
<script type="text/javascript" src="../../js/ui.uploadfile.js"></script>
<script type="text/javascript" src="../../js/ui.common.js"></script>
<script type="text/javascript" src="../../js/jquery.validate.js"></script>
<script type="text/javascript" src="../../js/page.common.js"></script> 

<script type="text/javascript">
	var listId = "#list2", exportKey="brand",
	editorFormId = "#editorForm", 
	pagerId = '#pager2', //分页
	saveUrl = "../../schoolHoliday/schoolHolidaySet/holidayTab.do?command=submit",
	deleteUrl = "../../schoolHoliday/schoolHolidaySet/holidayTab.do?command=delete",
	listUrl = "../../schoolHoliday/schoolHolidaySet/holidayTab.do?command=search";

	//编辑修改节假日维护
	var editOper = function(ev){
		var editUrl="schoolHoliday/schoolHolidaySet/addHolidayTab.do?command=init&id="+ev;//加载节点日信息	  
		  frameDialog(editUrl, "节日信息", {mode:"middle",resizable:false,width:480,height:350,buttons:[
		                 
		                                                                             
		                 { text:"保存", icons:{ primary:"ui-icon-check" },click:function( ev )
		                         {
		                             var $this   = window.top.$( this ),
		                              	 dial = $this.find( "iframe" )[0].contentWindow ;
		                                 var data = dial.getData();
		                                if(data!="" && data!=null){
		                                 POST(saveUrl,data,function(data){
		                                	 $(listId).trigger("reloadGrid");
		                                 	 hideSlidePanel(".page-editor-panel");
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
		                                      		    
	};	     
	
	//新增节假日维护
	var addHoliday = function(){
		var editUrl="schoolHoliday/schoolHolidaySet/addHolidayTab.do?command=init";//加载节点日信息	  
		  frameDialog(editUrl, "节日信息", {mode:"middle",resizable:false,width:480,height:350,buttons:[
		                 
		                                                                             
		                 { text:"保存", icons:{ primary:"ui-icon-check" },click:function( ev )
		                         {
		                             var $this   = window.top.$( this ),
		                              	 dial = $this.find( "iframe" )[0].contentWindow ;
		                                 var data = dial.getData();
		                                if(data!="" && data!=null){
		                                 POST(saveUrl,data,function(data){
		                                	 $(listId).trigger("reloadGrid");
		                                 	 //hideSlidePanel(".page-editor-panel");
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
		                                      		    
	};	     

   
	$(function() {
		var def;
		_initButtons({
  			logicDelete : function(ev){//逻辑删除
  				var idAry = $(listId).jqGrid("getGridParam", "selarrrow");
	  			  if (idAry.length === 0) {
	  				  window.message({
                          text: "请选择要删除的项",
                          title: "提醒",
                          buttons: {
                              "确认": function () {
                             	 window.top.$(this).dialog("close");
                              }
                          }
                      });
	                  return;
	              }
	  			   window.message({
	  	                text: "确认要删除所选择的记录吗?",
	  	                title: "提醒",
	  	                buttons: {
	  	                    "确认": function () {
	  	                        window.top.$(this).dialog("close");
	  	                        POST(logicDeleteUrl, {id: idAry}, function (data) {
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
		  	    resetSearch:function(ev){
	  	        	$("select[name=taskDistribute]").data().chosen._clearSelectedOptions();
	  	        	$("#enableTimeStart").val("");
	  	        	$("#enableTimeEnd").val("");
		  	    }
	  		});
		//_initButtons({});//from page.common.js
		_initFormControls();//from page.common.js
		_initValidateForXTypeForm(editorFormId, {
            errorElement: "span",
            errorPlacement: function(error, element) {
                error.appendTo(element.parent("td"));
            }
        });
		
		var _colModel = [
			  {name: 'ID', key: true, width: 60,hidden: true,title: false,resizable: false, search: false},
			  {name : 'NAME',autoWidth : true,index:"TITLENAME",align:"center"},
			  {name : 'SHORT_NAME',width:120,align:"center"}, 
			  {name : 'DictionaryName',autoWidth : true,index:'RECEIVER',align:"center"},
			  {name:'',width:120, align:"center", sortable:false, formatter:function( ar1, ar2, ar3 )
		          {
				  var operStr="<button id='queryBTN'  class='page-button' title='编辑' onclick='editOper(\""
						+ ar3.ID + "\");'><i class='fa fa-file-text' style='margin-right: 5px;'></i>编辑</button>";
	             return operStr;
		          }
			  }
			]
			_colNames = ['','节假日名称','简称','节假类型','操作'];

		
	
	$(listId).jqGrid($.extend(defaultGridOpts, {
			url : listUrl,
			colNames : _colNames,
			colModel : _colModel,
			pager : pagerId,
			rowNum: 20,
		    rowList: [  20, 30, 50, 100 ],
			height : "100%",
			multiselect : false,
			
		}));
	 resizeFun("549");
	});

	//保存
	/*  function editorSave(){
	  var data = getData();
		if (data.verification == true) {
			POST(saveUrl, data, function(data) {
				hideSlidePanel(".page-editor-panel");
				$(listId).trigger("reloadGrid");
			});
		}
	 } */

	var getData = function() {
		var data = {
			hoilidayName : $("#hoilidayName").val(),
			shortName : $("#shortName").val(),
			holiType : $("#holiType").val(),
			remark : $("#remark").val(),
			holiId : $("#holiId").val()
		};
		data.verification = true;
		if ($("#hoilidayName").val() == null
				|| $("#hoilidayName").val().replace(/\s/g, "") == "") {
			verificationInfo("节点名称");
			return data.verification = false;
		}
		if ($("#shortName").val() == null
				|| $("#shortName").val().replace(/\s/g, "") == "") {
			verificationInfo("简称");
			return data.verification = false;
		}
		return data;
	};

	var verificationInfo = function(info) {//拼接校验信息

		window.message({
			text : "<b style='color:#0099cc;'>" + info + "</b>为必填项",
			title : "提醒",
			buttons : {
				"确认" : function() {
					window.top.$(this).dialog("close");
				}
			}
		});
	};

	//"返回隐藏下拉"

	function cancel() {
		hideSlidePanel(".page-editor-panel");
	}
</script>

<style>
 
	a{
		cursor:pointer;
	}
 
</style>
</head>
<body>
	<div class="page-list-panel">
		<div class="head-panel">
			<div class="toolbar">
				<table style="height:100%;" cellpadding="0" cellspacing="0" border="0">
					<tr>
						<td style="padding-left: 12px; padding-right: 24px;"><i
							class="fa fa-list-ul micon"></i></td>
						<td class="buttons">
						     <button id="addHoliday" onclick="addHoliday();">
								<i class="fa fa-plus"></i>添加
							</button>
							<button id="deleteBTN">
								<i class="fa fa-trash-o"></i>删除
							</button>
							</td>
						<td style="padding-left: 24px; padding-right: 5px;">
							<input id="fastQueryText" type="text" name="text" placeholder="输入节假日名称"
								style="line-height: 1em; font-size: 1em; cursor: text;width:150px;" class="form-control" />
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
	
	
  <div class="page-editor-panel full-drop-panel">
		 <div class="title-bar">
                <h4>
                    <i class="fa fa-plus"></i>
                </h4>
                
            </div>
		<div class="page-content">
                <div class="page-inner-content">
                    <form id="editorForm">
                        <input id = "holiId" name="holiId" type="hidden" />
                        <table class="editorTable" cellpadding="0" cellspacing="0">
                            <tr>
                                <td class="labelTd"><label for="xymc">节点名称：</label>
                                </td>
                                <td class="inputTd" style="position:relative;z-index:1;">
                                <input data-xtype="text" data-validate="{required:true}" name="hoilidayName" id="hoilidayName" class="form-control" /></td>
                                <td class="messageTd"></td>
                            </tr>
                            <tr>
                                <td class="labelTd"><label for="xybm">简称：</label>
                                </td>
                                <td class="inputTd" style="position:relative;z-index:1;"><input
                                        data-xtype="text" data-validate="{required:true}" 
                                        name="shortName" id="shortName" class="form-control" /></td>
                                <td class="messageTd"></td>
                            </tr>
                            <tr>
                                <td class="labelTd"><label for="xyfer">假日类型：</label>
                                </td>
                                <td>
								 <select data-xtype="text" style="width:300px;" name="holiType" id="holiType" class="form-control" >
                                       <c:forEach items="${holidayType}" var="holidayType">
                                       		<option value="${holidayType.id}">${holidayType.DictionaryName}</option>
                                       </c:forEach>
                                 </select>
						    	</td>
                            </tr>
                            <tr>
                                <td class="labelTd" style="vertical-align: top;"><label
                                        for="remark">描&nbsp;&nbsp;述：</label></td>
                                <td class="inputTd" style="position:relative;z-index:1;"><textarea  
                                        name="remark" id="remark" style="width:300px;" rows="5"></textarea></td>
                            </tr>
                        </table>
                    </form>
                    <div class="btn-area" style="width: 250px;margin: auto;">
                    <div style="margin-top: 4px;">
                        <button class='page-button' onclick="editorSave();">
                            <i class="fa fa-check"></i>保存
                        </button>
                        <button class='page-button' onclick="cancel();">
                            <i class="fa fa-times"></i>取消
                        </button>
                    </div>
                </div>
                </div>
		</div>
	</div>
</body>
</html>