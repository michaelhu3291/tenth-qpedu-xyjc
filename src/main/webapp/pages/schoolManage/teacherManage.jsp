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



.s1 {
	width: 120px;
    background-color: #e7fcd9;
    border: 0px;
    color: #3b5617;
    cursor: pointer;
}
.s1:HOVER {
    background-color: #3b5617;
    color: #ffffff;
}

#d1 {
	width: 810px;
	height: 400px;
	background-color: #94c476;
	margin: 0 auto;
	margin-top: 60px;
}

#d2 {
	height: 30px;
	font-size: 24px;
	background-color: #3B5617;
	color: white;
	text-align: center;
}

#d3 {
	padding-left: 120px;
}

#tabs ul li a{
	cursor:pointer;
}

#frame1 ul li{
	list-style:none;
	margin-top:20px;
}

#addTeacherInfo{
	margin-top:20px;
}

#addTeacherInfo thead td{
	text-align:center;
	font-weight:bold;
}

#addTeacherInfo tbody td{
	background:#E7FCD9;
	width:150px;
	text-align:center;
}

#addTeacherInfo tbody a:hover{
	background:#3B5617;
	color:#ffffff;
}
.searchParam{
                       margin-left: 10px;
   					   width: 200px;
    				   height: 25px;
                }
</style>
<script type="text/javascript">
    var listId = "#list2",
        editorFormId = "#editorForm",
        pagerId = '#pager2',
        listUrl = "../schoolManage/teacherManage.do?command=searchPaging",
        loadUrl = "../teaManagement/teacherInfo.do?command=getSchoolById";
    loadClassUrl = "../teaManagement/teacherInfo.do?command=getClassById";
    saveClassUrl = "../teaManagement/teacherInfo.do?command=saveClass";
    loadCourseUrl = "../teaManagement/teacherInfo.do?command=getCourseById";
    saveCourseUrl = "../teaManagement/teacherInfo.do?command=saveCourse";
    var cancelBTN = function () {
        hideSlidePanel(".page-addTeacher-panel");
    }
 
    /*遍历待选择的班级  */
    var reSetClassList = function (date) {
            var selObj = $("#class_s1");
            selObj.find("option").remove();
            $.each(date, function (i, val) {
                selObj.append("<option value='" + val.Class_No + ',' + val.Grade_No + "'>" +val.Class_Name + "</option>");
            });
        }

    /*遍历待选择的科目  */
    var reSetCourseList = function (date) {
        var selObj = $("#course_s1");
        selObj.find("option").remove();
        $.each(date, function (i, val) {
            selObj.append("<option value='" + val.DictionaryCode + "'>" + val.DictionaryName + "</option>");
        });
    };
 
    
    
    //得到所有的科目
    var getCourse = function (course,idAry) {
        $.ajax({
            type: "GET",
            url: loadCourseUrl,
            data: {},
            dataType: "json",
            async: false,
            success: function (data) {
                var courseList = data.courseList;
                if (course == "addCourse") {
                    reSetCourseList(courseList);
                }
                if (course == "searchCourse") {
                    $("#teacherCourse option").remove();
                    $("#teacherCourse").append("<option value=''>请选择科目</option>");
                    for (var i = 0; i < courseList.length; i++) {
                        $("#teacherCourse").append("<option value='" + courseList[i].DictionaryCode + "'>" + courseList[i].DictionaryName + "</option>");
                    }
                }
            }
        });
    };

    var getClass = function () {
        //根据学校code加载年级
        $.ajax({
            url: "../statisticsAnalysis/schoolPlainAdminScoreSearch.do?command=getSchoolCodeAndSchoolName",
            type: "POST",
            data: {},
            dataType: "JSON",
            success: function (data) {
                $("#teacherGrade option").remove();
                $("#teacherGrade").append("<option value=''>请选择年级</option>");
                //科目
                var dataList = data.paramList;
                for (var i = 0; i < dataList.length; i++) {
                    $("#teacherGrade").append("<option value='" + dataList[i].DictionaryCode + "'>" + dataList[i].DictionaryName + "</option>");
                }
            }
        });
    };

    
    
    $(function () {
        $("#tabs").tabs({
            event: "mouseover",
            active: 0,
        });
        $("#tabs ul li:first").css("background", "#E7FCD9");
        $("#tabs ul li a").mouseover(function () {
            $(this).parent().next().css("background", "#9bc609");
            $(this).parent().prev().css("background", "#9bc609");
            $(this).parent().css("background", "#E7FCD9");
        });
        
        
        _initButtons({
        	
        	searchRip : function (ev) {
                $(".search-panel").show().data("show", true);
                var idAry = [];
                var course = "searchCourse";
                getClass();
                var idAry=[];
                getCourse(course,idAry);
                $(ev.target).closest("td").hide().prev().hide();
                resizeFun();
            },
            add_class: function (ev) {
                    var $i = $(ev.currentTarget).find("i"),
                        idAry = $(listId).jqGrid("getGridParam", "selarrrow");
                    if (idAry.length === 0) {
                        window.message({
                            text: "请选择要关联的记录!",
                            title: "提示"
                        });
                        return;
                    }
                    
                    var selObj = $("#class_s2");
                    selObj.find("option").remove();
                    $.ajax({
                        type: "GET",
                        url: loadClassUrl,
                        data: {
                            id: idAry
                        },
                        dataType: "json",
                        async: false,
                        success: function (data) {
                            var classList = data.classList;
                            reSetClassList(classList);
                        }
                    });
                    showSlidePanel(".page-class-panel");
                },
                classSaves: function () {
                    var optionVar = $("#class_s2").find("option");
                    var classIds = [];
                    if (optionVar != null) {
                        $.each(optionVar, function (index, item) {
                            var str = $(item).val();
                            classIds.push(str.split(","));
                        });
                    }
                    var teacherIds = $(listId).jqGrid("getGridParam", "selarrrow");
                    POST(saveClassUrl, {
                        "classIds": classIds,
                        "teacherIds": teacherIds,
                        "roleCode":"1"
                    }, function (data) {
                        $(listId).trigger("reloadGrid");
                        hideSlidePanel(".page-class-panel");
                    });
                },
                //关联科目
                add_course: function (ev) {
                    var $i = $(ev.currentTarget).find("i"),
                        idAry = $(listId).jqGrid("getGridParam", "selarrrow");
                    if (idAry.length === 0) {
                        window.message({
                            text: "请选择要关联的记录!",
                            title: "提示"
                        });
                        return;
                    }
                    var selObj = $("#course_s2");
                    selObj.find("option").remove();
                    var course="addCourse";
                    getCourse(course,idAry);
                  /*   $.ajax({
                        type: "GET",
                        url: loadCourseUrl,
                        data: {
                            id: idAry
                        },
                        dataType: "json",
                        async: false,
                        success: function (data) {
                            var courseList = data.courseList;
                            reSetCourseList(courseList);
                        }
                    }); */
                    showSlidePanel(".page-course-panel");
                },
                courseSaves: function () {
                    var optionVar = $("#course_s2").find("option");
                    //得到选中后科目id集合
                    var courseIds = [];
                    if (optionVar != null) {
                        for (var i = 0; i < optionVar.length; i++) {
                            courseId = optionVar.eq(i).val();
                            courseIds.push(courseId);
                        }
                    }
                    var teacherIds = $(listId).jqGrid("getGridParam", "selarrrow"); //得到选中行的id 
                    POST(saveCourseUrl, {
                        "courseIds": courseIds,
                        "teacherIds": teacherIds,
                    }, function (data) {
                        $(listId).trigger("reloadGrid");
                        hideSlidePanel(".page-course-panel");
                    });
                },
                cancelBTN: function () {
                    hideSlidePanel(".page-school-panel,.page-class-panel,.page-course-panel");
                },
        }); //from page.common.js
        _initFormControls(); //from page.common.js
        _initValidateForXTypeForm(editorFormId);
        var _colModel = [{
                name: 'id',
                label: '教师ID',
                key: true,
                hidden: true,
                search: false
            }, {
				name : 'state',
				label: '状态',
				key : true,
				hidden : true,
				search : false
			}, {
                name: 'schoolTypeSequence',
                label: '学校类型',
                key: true,
                hidden: true,
                search: false
            }, {
                name: 'teaType',
                label: '老师职位',
                key: true,
                hidden: true,
                search: false
            }, {
                name: 'teaName',
                label: '姓名',
                index: "teaName",
                sortable: false,
                width: 100,
                align: "center"
            }, {
                name: 'loginName',
                label: '账号',
                sortable: false,
                width: 100,
                align: "center"
            }, {
                name: 'schoolName',
                label: '学校',
                sortable: false,
                autoWidth: true,
                align: "left"
            }, {
                name: 'courseName',
                label: '科目',
                sortable: false,
                width: 100,
                align: "left",
                formatter: function (ar1, ar2, ar3) {
                	
                    var arrList = ar3.courseName;
                    var arr = [];
                    var strJoin;
                    var resultStr;
                    if (arrList.length > 0) {
                        $.each(arrList, function (index, item) {
                            var str = _types[item.Course_Id];
                            arr.push(str);
                        });
                    }
                    if(ar3.state==2){
                		return "";
                	}
                    resultStr = arr.join(",");
                    return resultStr || "";
                }
            }, {
                name: 'className',
                label: '班级',
                sortable: false,
                width: 100,
                align: "center",
                formatter: function (ar1, ar2, ar3) {
                    var arrList = ar3.className;
                    var arr = [];
                    var strJoin;
                    var resultStr;
                    $.each(arrList, function (index, item) {
                        var str = _types[item.Grade_Id] + _typesClass[item.Class_Id];
                        arr.push(str);
                    });
                    if(ar3.state==2){
                		return "";
                	}
                    resultStr = arr.join(",");
                    return resultStr || "";

                }

            }];
        $(listId).jqGrid($.extend(defaultGridOpts, {
            url: listUrl,
            colModel: _colModel,
            pager: pagerId,
			loadComplete: function(){//禁用复选框
                var rowIds = $(listId).jqGrid('getDataIDs');//获取jqgrid中所有数据行的id
                for(var i=0; i<rowIds.length; i++) {
                	var curRowData = $(listId).jqGrid('getRowData', rowIds[i]);//获取指定id所在行的所有数据.
                	if (curRowData!=null) {
                    	if (curRowData.state==2) {
                    		$("input[type='checkbox']")[i+1].disabled = true;//设置该行不能被选中。
                    		$("td")[i+1].disabled = true;;//设置该行不能被选中。
						}
					}
                }
            },
            /* beforeSelectRow: function (rowid, e) {//取消行点击选中事件
                var $myGrid = $(this),
                i = $.jgrid.getCellIndex($(e.target).closest('td')[0]),
                cm = $myGrid.jqGrid('getGridParam', 'colModel');
                return (cm[i].name === 'cb'); //不取消点击事件的td 
            }, */
            onSelectAll: function(rowid, status) { //点击全选时触发事件
                var rowIds = $(listId).jqGrid('getDataIDs');//获取jqgrid中所有数据行的id
                for(var k=0; k<rowIds.length; k++) {
                    var curRowData = $(listId).jqGrid('getRowData', rowIds[k]);//获取指定id所在行的所有数据.
                    if (curRowData!=null) {
                    	if (curRowData.state==2) {
                    		$(listId).jqGrid("setSelection", rowIds[k],false);//设置该行不被选中。
						}
					}
                }
            },
            onSelectRow:function(id){//选择某行时触发事件
                var curRowData = $(listId).jqGrid('getRowData', id);
                if (curRowData!=null) {
                   	if (curRowData.state==2) {
                   		$(listId).jqGrid("setSelection", id,false);//设置该行不被选中。
					}
				}
            }
        }));
        var myGrid = $(listId);
        $("#cb_"+myGrid[0].id).hide();
        resizeFun();


        //添加页面下拉显示
        $("#insertBTN").click(function (ev) {
            $("#chinesename").val('');
            $("#username").val('');
            var $i = $(ev.currentTarget).find("i"),
                $piel = $(".page-addTeacher-panel").show({
                    effect: "slide",
                    direction: "up",
                    easing: 'easeInOutExpo',
                    duration: 900
                });
        });

        //添加操作
        $("#saveBTN").click(function (ev) {
            var url = "../schoolManage/teacherManage.do?command=addTeacher";
            var chinesename = $("#chinesename").val();
            var username = $("#username").val();
            var password = $("#password").val();
            var data = {
                chinesename: chinesename,
                username: username,
                password: password
            };
            //校验
            if (chinesename == "" || chinesename == null) {
                window.Msg.alert("姓名不能为空");
                return;
            }
            if (username == "" || username == null) {
                window.Msg.alert("用户名不能为空");
                return;
            }
            /* if(password == "" || password == null){
			  window.Msg.alert("密码不能为空");
			  return;
		  } */

            window.message({
                title: '提醒',
                text: '确定添加此教师吗?',
                buttons: {
                    '确定': function () {
                            window.top.$(this).dialog("close");
                            $.ajax({
                                url: url,
                                type: "POST",
                                data: data,
                                dataType: "JSON",
                                success: function (data, xhr) {
                                    if (data.mess == 'success') {
                                        window.Msg.alert("添加成功!");
                                    }
                                    $(listId).trigger("reloadGrid");
                                    hideSlidePanel(".page-addTeacher-panel");
                                }
                            });
                        },
                        '取消': function () {
                            window.top.$(this).dialog("close");
                        }
                }
            });
        });
        //根据姓名查询外校老师信息
        $("#searchoutOfTeacher").click(function () {
            var url = "../schoolManage/teacherManage.do?command=selectTeacherByName";
            var realname = $("#realname").val();
            if (realname == null || realname == "") {
                window.Msg.alert("姓名不能为空");
                return;
            }
            var data = {
                realname: realname
            };

            $.ajax({
                url: url,
                type: "POST",
                data: data,
                dataType: "JSON",
                success: function (data, xhr) {
                    $("#addTeacherInfo tbody tr").remove();
                    if (data.mess == 'notFound') {
                        window.Msg.alert("此老师不存在,请重新检查!");
                        return;
                    }
                    for (var i = 0; i < data.length; i++) {
                        $("#addTeacherInfo tbody").append("<tr style='height:30px;'><td>" + data[i].Teacher_Name + "</td><td>" + data[i].Login_Name + "</td><td>" + data[i].School_Name + "</td><td style='width:60px;'><a class='btn_edit ui-state-default' style='cursor:pointer;padding:2px;padding-right:8px;'  onclick='addTeacherOper(\"" + data[i].Teacher_Pk + "\");'><i class='fa fa-plus'></i>添加</a></td></tr>");
                    }
                    //$(listId).trigger("reloadGrid");
                    //hideSlidePanel(".page-addTeacher-panel");
                }
            });

        });

        //删除
        $("#deleteOper").click(function () {
            var selArr = $(listId).jqGrid('getGridParam', 'selarrrow');
            if (selArr.length == 0) {
                window.Msg.alert("请选择要删除的记录");
                return;
            }
            var url = "../schoolManage/teacherManage.do?command=deleteTeacher";
            window.message({
                title: '提醒',
                text: '确定删除此教师吗?',
                buttons: {
                    '确定': function () {
                            window.top.$(this).dialog("close");
                            $.ajax({
                                url: url,
                                type: "POST",
                                data: {
                                    selArr: selArr
                                },
                                dataType: "JSON",
                                success: function (data, xhr) {
                                    if (data.mess == "notSubmit") {
                                        window.Msg.alert("申请的老师已经被调走,不能删除");
                                        return;
                                    }
                                    if (data.mess == 'notDelete') {
                                        window.Msg.alert("只能删除社招和兼职的老师");
                                        return;
                                    }
                                    if (data.mess == 'success') {
                                        window.Msg.alert("删除成功!");
                                    }
                                    $(listId).trigger("reloadGrid");
                                }
                            });
                        },
                        '取消': function () {
                            window.top.$(this).dialog("close");
                        }
                }
            });
        });

        /*学校管理员申请调度*/
        $("#applyOper").click(function () {
            var selArr = $(listId).jqGrid('getGridParam', 'selarrrow');
            if (selArr.length == 0) {
                window.Msg.alert("请选择要申请的记录");
                return;
            } 
            if (selArr.length > 1) {
            	 window.Msg.alert("每次只能申请单条记录!");               
                return;
            }
            var rowData = $(listId).jqGrid("getRowData",selArr);
       	    var rowState = rowData.state;
       	  if (rowState == 3){
       		 window.Msg.alert("该老师已经申请,不能重复申请");
       		 return;
       	 }
       	 if (rowState == 2){
       		 window.Msg.alert("该老师已经调动,不能重复申请");
       		 return;
       	 } 
            var url = "../schoolManage/teacherManage.do?command=submitApplyTeacher";
            window.message({
                title: '提醒',
                text: $(".page-dialog-panel").html(),
                
                buttons: {
                	
                    '确定': function () {
                    	
                            //window.top.$(this).dialog("close");
                            var val = parent.$("#newadr").val();
                            
                            if(val !== null && val !== ""){
                            	window.top.$(this).dialog("close");
	                            $.ajax({
	                                url: url,
	                                type: "POST",
	                                data: {
	                                    selArr: selArr,
	                                    newadr:val
	                                },
	                                dataType: "JSON",
	                                success: function (data, xhr) {
	                                    /* if (data.mess == "notSubmit") {
	                                        window.Msg.alert("申请的老师已经被调走,不能申请");
	                                        return;
	                                    }
	                                    if (data.mess == "repeatApply") {
	                                        window.Msg.alert("该老师已经申请调动,不能重复申请");
	                                        return;
	                                    } */
	                                                                                                                                            
	                                    if (data.mess == 'success') {
	                                        
	                                        
	                                    	window.Msg.alert("提交成功!");
	                                    	
	                                                                         
	                                    }                                                                      
	                                   $(listId).trigger("reloadGrid");
	                                    //hideSlidePanel(".page-addTeacher-panel");*/                                   
	                                }
	                            });
                            }
                        },
                        '取消': function () {
                            window.top.$(this).dialog("close");
                        }
                }
            });

        });

        /*学校管理员撤销调度*/
        $("#cancelOper").click(function () {
            var selArr = $(listId).jqGrid('getGridParam', 'selarrrow');
            if (selArr.length == 0) {
                window.Msg.alert("请选择要撤销的记录");
                return;
            }
            var url = "../schoolManage/teacherManage.do?command=cancelsubmitApplyTeacher";
            window.message({
                title: '提醒',
                text: '确定撤销此申请吗?',
                buttons: {
                    '确定': function () {
                            window.top.$(this).dialog("close");
                            $.ajax({
                                url: url,
                                type: "POST",
                                data: {
                                    selArr: selArr
                                },
                                dataType: "JSON",
                                success: function (data, xhr) {
                                    if (data.mess == "notSubmit") {
                                        window.Msg.alert("撤销的记录中有未提交调动的");
                                        return;
                                    }
                                    if (data.mess == 'success') {
                                        window.Msg.alert("撤销成功!");
                                    }
                                    $(listId).trigger("reloadGrid");
                                    //hideSlidePanel(".page-addTeacher-panel");*/

                                }
                            });
                        },
                        '取消': function () {
                            window.top.$(this).dialog("close");
                        }
                }
            });

        });
    });
    //本校管理员添加外校老师
    function addTeacherOper(teacher_pk) {
        var url = "../schoolManage/teacherManage.do?command=addTeacherInfo";
        var data = {
            teacher_pk: teacher_pk
        };
        //alert(teacher_pk);
        window.message({
            title: '提醒',
            text: '确定添加此教师吗?',
            buttons: {
                '确定': function () {
                        window.top.$(this).dialog("close");
                        $.ajax({
                            url: url,
                            type: "POST",
                            data: data,
                            dataType: "JSON",
                            success: function (data, xhr) {
                                if (data.mess == "isExist") {
                                    window.Msg.alert("此老师已经添加,不能重复添加");
                                    return;
                                }
                                if (data.mess == 'success') {
                                    window.Msg.alert("添加成功!");
                                }
                                $(listId).trigger("reloadGrid");
                                hideSlidePanel(".page-addTeacher-panel");
                            }
                        });
                    },
                    '取消': function () {
                        window.top.$(this).dialog("close");
                    }
            }
        });
    }
</script>
</head>
<body>
	<div class="page-list-panel">
		<div class="head-panel">
		     <div class="search-panel">
                        <div class="form-panel">
                            <table cellpadding="0" cellspacing="0" border="0" class="select">
                                <tr>
                                    <td style="font-size: 14px;">年级:</td>
                                    <td>
                                        <select   class="searchParam" id="teacherGrade" name="teacherGrade" style="width:199px;">
                                        </select>
                                    </td>
                                     <td  style="padding-left: 80px; font-size: 14px;">学科:</td>
                                    <td>
                                        <select class="searchParam" id="teacherCourse" name="teacherCourse" style="width:199px;">
                                        </select>
                                    </td>
                                    <td style="padding-left: 30px;">
                                        <button id="advancedSearch">
                                            <i class="fa fa-search"></i>查询
                                        </button>
                                        <button type="button" id="searchRipClose" title="点击收起查询面板">
                                            <i class="fa  fa-angle-down" style="margin-right:0px;"></i>
                                        </button>
                                    </td>
                                </tr>
                            </table>
                        </div>
                    </div>
			<div class="toolbar">
				<table style="height: 100%;" cellpadding="0" cellspacing="0"
					border="0">
					<tr>
						<td style="padding-left: 12px; padding-right: 24px;"><i
							class="fa fa-list-ul micon"></i></td>
						<td class="buttons">
							<!--  <button id="add_school">
								<i class="fa fa-plus"></i>关联学校
							</button>-->
							<button id="insertBTN">
								<i class="fa fa-plus"></i>添加
							</button>
							<button id="deleteOper">
								<i class="fa fa-trash-o"></i>删除
							</button>
							
							<button id="applyOper">
								<i class="fa fa-plus"></i>申请调动
							</button>
							<button id="cancelOper">
								<i class="fa fa-trash-o"></i>撤销调动
							</button>
							
							<button id="add_course">
								<i class="fa fa-plus"></i>关联科目
							</button>
							<button id="add_class">
								<i class="fa fa-plus"></i>关联年级班级
							</button>
							
						</td>
						<td style="padding-left: 24px; padding-right: 5px;"><input
							id="fastQueryText" type="text" placeholder="输入名称或账号"
							style="line-height: 1em; font-size: 1em; cursor: text;" /></td>
						<td>
							<button id="fastSearch" title="查询" style="margin-left: 0px;">
								<i class="fa fa-search"></i>查询
							</button>
							<button id="searchRip" title="点击展开高级查询面板" class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only tradition" role="button" aria-disabled="false">
                                    <span class="ui-button-text" style="margin: -4px;">
	                                <i class="fa fa-angle-up" style="margin-right:0px;"></i>
	                                </span>
                            </button>
						</td>
					</tr>
				</table>
			</div>
		</div>
		<table id="list2"></table>
		<div id="pager2"></div>
	</div>
	
<!-- 关联学校 -->
	<div class="page-school-panel full-drop-panel">
		<div class="title-bar">
			<h4>
				<i class="fa fa-plus"></i>
			</h4>
			<div class="btn-area">
				<div style="margin-top: 4px;">
					<button id="schoolSaves">
						<i class="fa fa-check"></i>保存
					</button>
					<button id="cancelBTN">
						<i class="fa fa-times"></i>取消
					</button>
				</div>
			</div>
		</div>
		<div class="page-content">
		 <form id="editorForm">
			<div id="d1">
				<div id="d2">选学校</div>
				<div id="d3">
					<table cellpadding="0" cellspacing="8">
						<tr>
							<td style="font-size: 16px;">可选学校</td>
							<td>&nbsp;</td>
							<td style="font-size: 16px;">已选学校</td>
						</tr>
						<tr>
							<td><select id="s1" name="s1" style="width: 220px; height: 300px;" multiple="multiple">
							</select></td>
							<td>
								<p>
									<input id="b1" type="button" class="s1" value="选中右移&nbsp;&gt;" />
								</p>
								<p>
									<input type="button" id="b2" class="s1" value="全部右移&nbsp;&gt;&gt;" />
								</p>
								<p>
									<input type="button" id="b3" class="s1" value="&lt;&nbsp;选中左移" />
								</p>
								<p>
									<input type="button" id="b4" class="s1" value="&lt;&lt;&nbsp;全部左移" />
								</p>
							</td>
							<td><select id="s2" name="schoolId"
								style="width: 220px; height: 300px;" multiple="multiple"></select></td>
						 </tr>
					</table>
				</div>
			</div>
			</form>
		</div>
	</div>


<!-- 关联班级 -->
    <div class="page-class-panel full-drop-panel">
		<div class="title-bar">
			<h4>
				<i class="fa fa-plus"></i>
			</h4>
			<div class="btn-area">
				<div style="margin-top: 4px;">
					<button id="classSaves">
						<i class="fa fa-check"></i>保存
					</button>
					<button id="cancelBTN">
						<i class="fa fa-times"></i>取消
					</button>
				</div>
			</div>
		</div>
		<div class="page-content">
			<div id="d1">
				<div id="d2">选年级班级</div>
				<div id="d3">
					<table cellpadding="0" cellspacing="8">
						<tr>
							<td style="font-size: 16px;">可选年级班级</td>
							<td>&nbsp;</td>
							<td style="font-size: 16px;">已选年级班级</td>
						</tr>
						<tr>
							<td><select id="class_s1" name="s1"
								style="width: 220px; height: 300px;" multiple="multiple">
							</select></td>
							<td>
								<p>
									<input id="class_b1" type="button" class="s1" value="选中右移&nbsp;&gt;" />
								</p>
								<p>
									<input type="button" id="class_b2" class="s1" value="全部右移&nbsp;&gt;&gt;" />
								</p>
								<p>
									<input type="button" id="class_b3" class="s1" value="&lt;&nbsp;选中左移" />
								</p>
								<p>
									<input type="button" id="class_b4" class="s1" value="&lt;&lt;&nbsp;全部左移" />
								</p>
							</td>
							<td><select id="class_s2" name="s2"
								style="width: 220px; height: 300px;" multiple="multiple"></select></td>
						</tr>
					</table>
				</div>
			</div>
		</div>
	</div>




<!-- 关联科目 -->
    <div class="page-course-panel full-drop-panel">
		<div class="title-bar">
			<h4>
				<i class="fa fa-plus"></i>
			</h4>
			<div class="btn-area">
				<div style="margin-top: 4px;">
					<button id="courseSaves">
						<i class="fa fa-check"></i>保存
					</button>
					<button id="cancelBTN">
						<i class="fa fa-times"></i>取消
					</button>
				</div>
			</div>
		</div>
		<div class="page-content">
			<div id="d1">
				<div id="d2">选科目</div>
				<div id="d3">
					<table cellpadding="0" cellspacing="8">
						<tr>
							<td style="font-size: 16px;">可选科目</td>
							<td>&nbsp;</td>
							<td style="font-size: 16px;">已选科目</td>
						</tr>
						<tr>
							<td><select id="course_s1" name="s1"
								style="width: 220px; height: 300px;" multiple="multiple">
							</select></td>
							<td>
								<p>
									<input id="course_b1" type="button" class="s1" value="选中右移&nbsp;&gt;" />
								</p>
								<p>
									<input type="button" id="course_b2" class="s1" value="全部右移&nbsp;&gt;&gt;" />
								</p>
								<p>
									<input type="button" id="course_b3" class="s1" value="&lt;&nbsp;选中左移" />
								</p>
								<p>
									<input type="button" id="course_b4" class="s1" value="&lt;&lt;&nbsp;全部左移" />
								</p>
							</td>
							<td><select id="course_s2" name="s2"
								style="width: 220px; height: 300px;" multiple="multiple"></select></td>
						</tr>
					</table>
				</div>
			</div>
		</div>
	</div>
	<!-- 申请调动 -->
	
	
	<!-- 添加教师 -->
	<div class="page-addTeacher-panel full-drop-panel">
		<div class="title-bar">
			<h4>
				<i class="fa fa-plus"></i>
			</h4>
			<div class="btn-area">
				<div style="margin-top: 4px;">
					<button id="saveBTN">
						<i class="fa fa-check"></i>保存
					</button>
					<button id="cancelBTN"  onclick="cancelBTN()">
						<i class="fa fa-times"></i>取消
					</button>
				</div>
			</div>
		</div>
		<div class="page-content">
			<div class="page-inner-content">
				<div id="tabs">
					<ul>
						<li style="background:#9bc609;"><a href="#frame1">社会人员</a></li>
						<!-- <li style="background:#9bc609;"><a href="#frame2">外校老师</a></li> -->
					</ul>
					<div id="frame1">
						<ul>
							<li>
								<label for="chinesename">
									<span style="margin-left:-12px">姓名</span>
									<input id="chinesename" class="form-control" type="text" style="width:150px;" name="chinesename" placeholder="请输入姓名">
								</label>
							</li>
							<li>
								<label for="username">
									<span style="margin-left:-12px">账号</span>
									<input id="username" class="form-control" type="text" style="width:150px;" name="username" placeholder="请输入账号">
								</label>
							</li>
							<li style="display: none;">
								<label for="password">
									密码
									<input id="password" class="form-control" type="password" style="width:150px;" name="password" placeholder="请输入密码">
								</label>
							</li>
						</ul>	
					</div>
					<!-- <div id="frame2">
						<input id="realname" class="form-control" type="text" style="width:150px;margin-top:-3px;" name="realname" placeholder="请输入姓名或账号">
						<button id="searchoutOfTeacher" title="查询" class="page-button" style="margin-left: 0px;">
								<i class="fa fa-search"></i>查询
						</button>
						<table  id="addTeacherInfo">
								<thead>
									<tr class="text-center">
										<td style="font-size:16px;">姓名</td>
										<td style="font-size:16px;">账号</td>
										<td style="font-size:16px;">学校</td>
										<td style="font-size:16px;">操作</td>
									</tr>
								</thead>
								<tbody>
									
								</tbody>
						</table>
					</div> -->
				</div>
			</div>
		</div>
	</div> 
	<!-- 申请调动去向  -->
	 <div class="page-dialog-panel full-drop-panel">
    	<div class="head-panel">
            <div class="" style="display:block;position: relative;">
                <div class="form-panel">
		        <form id="searchFormDialog">
		           <table cellpadding="0" cellspacing="0" border="0" style="width: 100%;text-align: center;border-spacing: 10px 10px;">		               
						<tr>
							<td>
							<div style="text-align:left;">
								<span>调动去向:</span></div><br>
								<form id="newform" >								
								<textarea id="newadr" name="newadr" rows="0.5px" cols="25px"></textarea>
								</form>
								<span style="color: red;">*此项必填</span>
							</td>
						</tr>
		             
		           </table>
		        </form>
        	</div>
        </div>
    </div>
   </div> 
</body>
</html>