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
<link href="../theme/default/page.common.css" rel="stylesheet" />
<script type="text/javascript" src="../js/jquery.js"></script>
<script type="text/javascript" src="../js/ui.custom.js"></script>
<script type="text/javascript" src="../js/ui.jqgrid.js"></script>
<script type="text/javascript" src="../js/ui.autosearch.js"></script>
<script type="text/javascript" src="../js/ui.chosen.js"></script>
<script type="text/javascript" src="../js/ui.uploadfile.js"></script>
<script type="text/javascript" src="../js/ui.common.js"></script>
<script type="text/javascript" src="../js/jquery.validate.js"></script>
<!--<script type="text/javascript" src="../js/jquery.metadata.js"></script>-->
<script type="text/javascript" src="../js/page.common.js"></script>
<!--[if IE 7]>
         <link href="../theme/default/font.awesome.ie7.css" rel="stylesheet" />
         <link href="../theme/default/page.common.ie7.css" rel="stylesheet" />
    <![endif]-->

<style type="text/css">
  .inp{
              border:1px solid #009E98;
              display: block;
              padding: .4em 1em;
              font-size: 1em;
              font-weight: bold;
			   color: #ffffff;
              background: #009E98 url(../theme/default/images/ui-bg_flat_15_009E98_40x100.png) 50% 50% repeat-x;
            }
    </style>
    <script type="text/javascript">
   /*   var saveData=function(callback){
        POST("../homePage_/updatePasswd.do?command=updatePasswd", $("#editorForm").getFormData(), function(data, xhr) {
                callback();
          });
     }; */
     
     $(function(){
    	 _initValidateForXTypeForm("#editorForm");
     });
     
     var getData=function()
     {
    	 if($("#editorForm [data-validate]").valid())
    	{
    		return $("#editorForm").getFormData();
    	}
    	return false;
     }
 </script>
    </head>
  <body>
				<form id="editorForm">
					<table class="editorTable" cellpadding="0" cellspacing="0">
						<tr>
							<td class="labelTd"><label for="text_prevPasswd"><font style="color:#ff0000;"></font>原始密码：</label>
							</td>
							<td class="inputTd" style="position:relative;z-index:1;">
								<input data-xtype="text" data-validate="{required:true,maxlength:50}" 
									name="prevPasswd" id="text_prevPasswd" class="form-control" type="password"/>
							</td>
							<td class="messageTd"></td>
						</tr>
						
						<tr>
							<td class="labelTd"><label for="text_newPasswd"><font style="color:#ff0000;"></font>新密码：</label>
							</td>
							<td class="inputTd" style="position:relative;z-index:1;">
								<input data-xtype="text" data-validate="{required:true,maxlength:50}" 
								name="newPasswd" id="text_newPasswd" class="form-control"  type="password"/>
								
								</td>
							<td class="messageTd"></td>
						</tr>
						
						<tr>
							<td class="labelTd"><label for="text_confirmNewPasswd"><font style="color:#ff0000;"></font>新密码确认：</label>
							</td>
							<td class="inputTd" style="position:relative;z-index:1;"><input
								data-xtype="text" data-validate="{required:true,maxlength:50}" 
								name="confirmNewPasswd" id="text_confirmNewPasswd" class="form-control" type="password"/></td>
							<td class="messageTd"></td>
						</tr>
					</table>
				</form>
			</div>
</body>
</html>