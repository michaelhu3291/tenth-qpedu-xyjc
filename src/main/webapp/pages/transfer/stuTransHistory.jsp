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
<script type="text/javascript" src="../js/util.js"></script>
<script type="text/javascript" src="../js/lab2.js"></script>


<script type="text/javascript">

	var listId = "#list2", 
	editorFormId = "#editorForm",
	pagerId = '#pager2', 
	listUrl = "../transfer/stuTransHistory.do?command=stuHistoryPage";

  $(function() {
		_initButtons({
			
		});//from page.common.js
		_initFormControls();//from page.common.js
		var _colModel = [ {
			name : 'Id',
			key : true,
			hidden : true,
			search : false
			},{
				name : 'Name',
				align:"center",
				sortable : false,
				autoWidth:true,
			    search : false
		    },{
		    	name : 'schoolName',
		    	align:"left",
			    sortable : false,
				autoWidth:true,
				search : false
	      },{
			    name : 'Proposer',
			    autoWidth:true,
			    sortable : false,
			    align : "center",
			    search : false
		}, {
			    name : 'direction',
			    autoWidth:true,
			    sortable : false,
			    align : "left",
			    search : false
		},  {
			    name : 'applyTime',
			    sortable : false,
			    autoWidth:true,
			    align : "center",
			    search : false
		},
		], _colNames = [ '','姓名', '学校','申请人','去向','申请时间'];
		
		$(listId).jqGrid($.extend(defaultGridOpts, {
			url : listUrl,
			colNames : _colNames,
			colModel : _colModel,
			pager : pagerId,
			multiselect : false
		}));
		resizeFun();
		

   });
</script>
</head>
  
  <body>
    <div class="page-list-panel">
		<div class="head-panel">
			<div class="toolbar">
				<table cellpadding="0" cellspacing="0"
					border="0">
					<tr>
					    <td style="padding-left: 12px; padding-right: 24px;">
						       <i class="fa fa-list-ul micon"></i></td>
					    <td style="padding-left: 24px; padding-right: 5px;"><input id="fastQueryText" type="text" placeholder="输入姓名" style="line-height: 1em; font-size: 1em; cursor: text;" /></td>
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
  </body>
</html>
