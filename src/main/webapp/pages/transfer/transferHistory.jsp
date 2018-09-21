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
<script type="text/javascript" src="../js/lab2.js"></script>
 

<!--[if IE 7]>
         <link href="../theme/default/font.awesome.ie7.css" rel="stylesheet" />
         <link href="../theme/default/page.common.ie7.css" rel="stylesheet" />
    <![endif]-->
<style type="text/css">


</style>
<script type="text/javascript">

   var resizeFun = function()
   {
       $( "#tabs" ).tabs( "refresh" ) ;
   };
   $( function()
   {
       $( "#tabs" ).tabs( { heightStyle:"fill" } ) ;
       $("#tabs_a").click(function(){
    	   $("#tabs-1").html("");
           var objStr="<iframe marginheight='0' marginwidth='0' frameborder='0' src='../transfer/stuTransHistory.do'></iframe>";
 		$("#tabs-1").append(objStr);
   	});
   	$("#tabs_b").click(function(){
   		$("#tabs-2").html("");
   		 var objStr="<iframe marginheight='0' marginwidth='0' frameborder='0' src='../transfer/teaTransHistory.do'></iframe>";
   		$("#tabs-2").append(objStr);
   	  });
   });

   var reload = function(){
   	location.reload();
   }
</script>
<style>
#tabs_b,#tabs_a {
    color: #3B5615;
    background: white;
}
  .ui-tabs-nav{    color: #3B5615;}
  .ui-tabs-active{    color: #3B5615;}
</style>
</head>
<body>

 <div id="tabs" class="frametab">
    <ul>
      <li><a href="#tabs-1" id="tabs_a">学生</a></li>
      <li><a href="#tabs-2" id="tabs_b">老师</a></li>
    </ul>
    <div id="tabs-1"><iframe marginheight="0" marginwidth="0" frameborder="0" src="../transfer/stuTransHistory.do"></iframe></div>
    <div id="tabs-2"><iframe marginheight="0" marginwidth="0" frameborder="0" src="../transfer/teaTransHistory.do"></iframe></div>
   </div>
</body>
</html>