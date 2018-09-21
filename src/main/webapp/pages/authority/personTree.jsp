<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
		<link href="../theme/default/ui.ztree.css" rel="stylesheet" />
		<link href="../theme/default/style01.css" rel="stylesheet" />
		<script type="text/javascript" src="../js/jquery.js"></script>
		<script type="text/javascript" src="../js/ui.ztree.js"></script>
		<script type="text/javascript" src="../js/jquery.cookie.js"></script>
		<script type="text/javascript" src="../js/ui.common.js"></script>
		
		<title>99999</title>
		<style type="text/css">
			body {
				font-size: 75%;
                padding:12px 12px 12px 12px;
			}
			
         .left {
			    min-width: 200px;
		}
		</style>
		<script type="text/javascript">
		/*
			window.chkStyle:"radio/checkbox" 单选框或者复选框
			window.radioType:"all/level" //单选框的单选组
			window.orgCheck:true/false 组织机构是否可选，true为不可选，false可选
			window.personCheck:true/false 人是否可选，true为不可选，false可选
			window.curPersons:[] 如果指定节点不可选USER_ID
		*/
	
		  var param = {
								 orgCheck:window.orgCheck || false,
								 chkStyle:window.chkStyle|| false,
								 personCheck:window.personCheck || false,
								 curPersons:window.curPersons || []
						 };
				
		  var arr = ["orgCheck",(window.orgCheck || false),
		             	"chkStyle",(window.chkStyle|| false),
		             	"personCheck",(window.personCheck || false),
		             	"curPersons",(window.curPersons || [])];
	   
          var checkData=[];
    	  var setting = {
	  			    isSimpleData : true, //数据是否采用简单 Array 格式，默认false
	  				treeNodeKey : "id", //在isSimpleData格式下，当前节点id属性
	  				treeNodeParentKey : "pId", //在isSimpleData格式下，当前节点的父节点id属性
	  				showLine : true, //是否显示节点间的连线
	  				checkable : true, //每个节点上是否显示 CheckBox
	  				check: {
		  					enable: true,
		  					chkStyle:window.chkStyle||"checkbox",
							radioType:window.radioType||"all",
		  					chkboxType: { "Y": "s", "N": "s" }
	  				},
	  				async: {
	  					enable: true,
	  					url: "../authority/authority.do?command=loadPerson",
	  					contentType : "application/json",
	  					type:"POST",
	  					dataType : "text",
	  					autoParam: ["id=curNode"],
	  					otherParam: arr
	  				}
	     
	  		};

    	  function getData(filter){
			    var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
	            var nodes = treeObj.getCheckedNodes(true);
	            
	            var arr=[];
	            for(var i in nodes)
	            {
	            	if(typeof filter == "object" )
	            	{
	            		var f = ( filter.tag && nodes[i].tag==filter.tag) || (filter.ids && ( typeof filter.ids=="object") && filter.ids.indexOf(nodes[i].id)>=0 ) ;
	            		if(f)continue;
	            	}
	            	var obj={};
	            	obj.id=nodes[i].id;
	            	obj.name=nodes[i].name;
	            	arr.push(obj);
	            }
	          return arr ;
		};

		$(document).ready(function(){
			// param.curNode="";
			  $.ajax({
                  url:  "../authority/authority.do?command=loadPerson",
                  type: "POST",
                  data: param,
                  dataType: "json",
                  success: function(data, xhr) {
                      $.fn.zTree.init($("#treeDemo"), setting,eval(data));
             
                  }
              });
			  
			 
		});
		</script>
	</head>
	<body>
	     <input type="hidden" id="hiddenIdData"/>
	     <input type="hidden" id="hiddenNameData"/>
	     <input type="hidden" id="idsParam" value="${idsParam}"/>
	     <input type="hidden" id="hiddenOrgId" value="${orgId}"/>
	     <input type="hidden" id="hiddenOrgName" value="${orgName}"/>
	     <input type="hidden" id="hiddenModuleType" value="${moduleType}"/>
	     <input type="hidden" id="currentUserId" value="${currentUserId}"/>
	     
		 <div align="center">
           <div class="cenbox_tree" align="center" style="float:left">
             <div class="zTreeDemoBackground left">
		       <ul id="treeDemo" class="ztree"></ul>
	         </div>
           </div>

        </div>
        <h2>8888988</h2>
	</body>
</html>
