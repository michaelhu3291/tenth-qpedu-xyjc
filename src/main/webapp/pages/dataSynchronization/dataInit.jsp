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
    var initTeaData = "../dataSynchronization/dataInit.do?command=addTeachers";
    var initUnitData = "../dataSynchronization/dataInit.do?command=addSchools";
    var initStuData = "../dataSynchronization/dataInit.do?command=addStudents";
    var initCourseData = "../dataSynchronization/dataInit.do?command=addCourse";
    var initClassData="../dataSynchronization/dataInit.do?command=addClass";
    var initKnowledgeData="../dataSynchronization/dataInit.do?command=initKnowledgeData";
    $(function () {
        _initButtons({
        	init_knowledgeInfo : function (){
        		window.message({
                    text: "确定同步知识点数据？",
                    title: "提醒",
                    buttons: {
                        "确认": function () {
                                window.top.$(this).dialog("close");
                                $.ajax({
                                    type: "GET",
                                    url: initKnowledgeData,
                                    data: {},
                                    dataType: "json",
                                    beforeSend: function () {
                                            window.message({
                                                text: "正在同步知识点数据...",
                                                title: "提醒",
                                            });
                                        },
                                        success: function (data) {
                                        	//修改父页面div提示信息
                                            $('.ui-dialog-content.ui-widget-content', parent.document).html(data.message);
                                        }
                                });
                                showSlidePanel(".page-list-panel");
                            },
                            "取消": function () {
                                window.top.$(this).dialog("close");
                            }
                    }
                });
                showSlidePanel(".page-list-panel");
        	},
        	init_classInfo: function () {
                window.message({
                    text: "确定同步班级数据？",
                    title: "提醒",
                    buttons: {
                        "确认": function () {
                                window.top.$(this).dialog("close");
                                $.ajax({
                                    type: "GET",
                                    url: initClassData,
                                    data: {},
                                    dataType: "json",
                                    beforeSend: function () {
                                            window.message({
                                                text: "正在同步班级数据...",
                                                title: "提醒",
                                            });
                                        },
                                        success: function (data) {
                                        	//修改父页面div提示信息
                                            $('.ui-dialog-content.ui-widget-content', parent.document).html(data.message);
                                        }
                                });
                                showSlidePanel(".page-list-panel");
                            },
                            "取消": function () {
                                window.top.$(this).dialog("close");
                            }
                    }
                });
                showSlidePanel(".page-list-panel");
            },
        	
        	
            init_teacherInfo: function () {
                    window.message({
                        text: "确定同步教师数据？",
                        title: "提醒",
                        buttons: {
                            "确认": function () {
                                    window.top.$(this).dialog("close");
                                    $.ajax({
                                        type: "GET",
                                        url: initTeaData,
                                        data: {},
                                        dataType: "json",
                                        beforeSend: function () {
                                                window.message({
                                                    text: "正在同步教师数据...",
                                                    title: "提醒",
                                                });
                                            },
                                            success: function (data) {
                                            	//修改父页面div提示信息
                                                $('.ui-dialog-content.ui-widget-content', parent.document).html(data.message);
                                            }
                                    });
                                    showSlidePanel(".page-list-panel");
                                },
                                "取消": function () {
                                    window.top.$(this).dialog("close");
                                }
                        }
                    });
                    showSlidePanel(".page-list-panel");
                },
                init_UnitInfo: function () {
                    var isSure = false;
                    window.message({
                        text: "确定同步教育单位数据？",
                        title: "提醒",
                        buttons: {
                            "确认": function () {
                                    window.top.$(this).dialog("close");
                                    isSure = true;
                                    if (isSure) {
                                        $.ajax({
                                            type: "GET",
                                            url: initUnitData,
                                            data: {},
                                            dataType: "json",
                                            beforeSend: function () {
                                                    window.message({
                                                        text: "正在同步教育单位数据...",
                                                        title: "提醒",
                                                    });
                                                },
                                                success: function (data) {
                                                	//修改父页面div提示信息
                                                    $('.ui-dialog-content.ui-widget-content', parent.document).html(data.message);
                                                }
                                        });
                                    }
                                    showSlidePanel(".page-list-panel");
                                },
                                "取消": function () {
                                    window.top.$(this).dialog("close");
                                }
                        }
                    });
                    showSlidePanel(".page-list-panel");
                },

                init_studentInfo: function () {
                    var isSure = false;
                    window.message({
                        text: "确定同步学生数据？",
                        title: "提醒",
                        buttons: {
                            "确认": function () {
                                    window.top.$(this).dialog("close");
                                    isSure = true;
                                    if (isSure) {
                                        $.ajax({
                                            type: "GET",
                                            url: initStuData,
                                            data: {},
                                            dataType: "json",
                                            beforeSend: function () {
                                                    window.message({
                                                        text: "正在同步学生数据...",
                                                        title: "提醒",
                                                    });
                                                },
                                                success: function (data) {
                                                	//修改父页面div提示信息
                                                    $('.ui-dialog-content.ui-widget-content', parent.document).html(data.message);
                                                }
                                        });
                                    }
                                    showSlidePanel(".page-list-panel");
                                },
                                "取消": function () {
                                    window.top.$(this).dialog("close");
                                }
                        }
                    });
                    showSlidePanel(".page-list-panel");
                },

                init_courseInfo: function () {
                    var isSure = false;
                    window.message({
                        text: "确定同步科目数据？",
                        title: "提醒",
                        buttons: {
                            "确认": function () {
                                    window.top.$(this).dialog("close");
                                    isSure = true;
                                    if (isSure) {
                                        $.ajax({
                                            type: "GET",
                                            url: initCourseData,
                                            data: {},
                                            dataType: "json",
                                            beforeSend: function () {
                                                    window.message({
                                                        text: "正在同步科目数据...",
                                                        title: "提醒",
                                                    });
                                                },
                                                success: function (data) {
                                                    //修改父页面div提示信息
                                                    $('.ui-dialog-content.ui-widget-content', parent.document).html(data.message);
                                                }
                                        });
                                    }
                                    showSlidePanel(".page-list-panel");
                                },
                                "取消": function () {
                                    window.top.$(this).dialog("close");
                                }
                        }
                    });
                    showSlidePanel(".page-list-panel");
                }
        });
    });
</script>
</head>
<body>
<div id="mask" class="mask"></div>  
	<div class="page-list-panel">
		<div class="head-panel">
			<div class="toolbar">
				<table style="height: 100%;" cellpadding="0" cellspacing="0"
					border="0">
					<tr>
						<td style="padding-left: 12px; padding-right: 24px;"><i
							class="fa fa-list-ul micon"></i></td>
						<td class="buttons">
							<button id="init_UnitInfo">
								<i class="fa fa-plus"></i>同步教育单位基本数据
							</button>
							<button id="init_teacherInfo">
								<i class="fa fa-plus"></i>同步教师基本数据
							</button>
							<button id="init_studentInfo">
								<i class="fa fa-plus"></i>同步学生基本数据
							</button>
							<button id="init_courseInfo">
								<i class="fa fa-plus"></i>同步科目基本数据
							</button>
							<button id="init_classInfo">
								<i class="fa fa-plus"></i>同步班级基本数据
							</button>
							<button id="init_knowledgeInfo"> 
							  <i class="fa fa-plus">同步知识点数据</i>
							</button>
						</td>
					</tr>
				</table>
			</div>
		</div>
	</div>
</body>
</html>