<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
                    width: 650px;
                    height: 382px;
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
                    padding-left: 30px;
                }
                #frame1 ul li {
                    list-style: none;
                    margin-top: 20px;
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
                    listUrl = "../stuManagement/stuInfo.do?command=searchPaging",
                    loadCourseUrl = "../stuManagement/stuInfo.do?command=getCourseById",
                    loadClassUrl = "../teaManagement/teacherInfo.do?command=getClassById",
                    saveCourseUrl = "../stuManagement/stuInfo.do?command=saveCourse",
                    saveClassUrl = "../teaManagement/teacherInfo.do?command=saveClass",
                    updateStuStateUrl = "../stuManagement/stuInfo.do?command=updateStudentState",
                    cencelSbjdUrl = "../stuManagement/stuInfo.do?command=cencelSbjd";
                var cancelBTN = function () {
                        hideSlidePanel(".page-addStudent-panel");
                    }
                    /*遍历待选择的学科目 */
                var reSetCourseList = function (date) {
                    var selObj = $("#course_s1");
                    selObj.find("option").remove();
                    $.each(date, function (i, val) {
                        selObj.append("<option value='" + val.DictionaryCode + "' >" + val.DictionaryName + "</option>");
                    });
                };

                /*遍历待选择的班级  */
                var reSetClassList = function (date) {
                    var selObj = $("#class_s1");
                    selObj.find("option").remove();
                    $.each(date, function (i, val) {
                        var isXjb = "";
                        if (val.Is_Xjb == "1") {
                            isXjb = "<span style='color:red;'>(新疆班)</span>";
                        }
                        selObj.append("<option value='" + val.Class_Pk + "' grade='" + val.Grade_No + "' classNo='" + val.Class_No + "' >" + val.Class_Name + isXjb + "</option>");
                    });
                };
                 //得到所有的科目
                var getCourse = function (course) {
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
                                $("#studentCourse option").remove();
                                $("#studentCourse").append("<option value=''>请选择科目</option>");
                                for (var i = 0; i < courseList.length; i++) {
                                    $("#studentCourse").append("<option value='" + courseList[i].DictionaryCode + "'>" + courseList[i].DictionaryName + "</option>");
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
                            $("#studentGrade option").remove();
                            $("#studentGrade").append("<option value=''>请选择年级</option>");
                            //科目
                            var dataList = data.paramList;
                            for (var i = 0; i < dataList.length; i++) {
                                $("#studentGrade").append("<option value='" + dataList[i].DictionaryCode + "'>" + dataList[i].DictionaryName + "</option>");
                            }
                        }
                    });
                };

                $(function () {
                    _initButtons({
                        searchRip: function (ev) {
                                $("#studentState option").remove();
                                $(".search-panel").show().data("show", true);
                                var classs = "searchClass";
                                var idAry = [];
                                var course = "searchCourse";
                                getClass();
                                getCourse(course);
                                $("#studentState").append("<option value=''>请选择学生状态</option>");
                                $("#studentState").append("<option value='0'>在籍</option>" +
                                    "<option value='4'>调入</option>" +
                                    "<option value='2'>调出</option>" +
                                    "<option value='5'>新疆班</option>" +
                                    "<option value='1'>随班就读</option>");
                                $(ev.target).closest("td").hide().prev().hide();
                                resizeFun();
                            },
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
                                var course = "addCourse";
                                getCourse(course);
                                showSlidePanel(".page-course-panel");
                            },
                            courseSaves: function () {
                                idAry = $(listId).jqGrid("getGridParam", "selarrrow");
                                var optionVar = $("#course_s2").find("option");
                                //得到选中后科目id集合
                                var courseIds = [];
                                if (optionVar != null) {
                                    for (var i = 0; i < optionVar.length; i++) {
                                        courseId = optionVar.eq(i).val();
                                        courseIds.push(courseId);
                                    }
                                }
                                var sfzjhArr = [];
                                for (var i = 0; i < idAry.length; i++) {
                                    var rowData = $(listId).jqGrid("getRowData", idAry[i]);
                                    var sfzjh = rowData.SFZJH;
                                    sfzjhArr.push(sfzjh);

                                }
                                POST(saveCourseUrl, {
                                    "courseIds": courseIds,
                                    "sfzjhArr": sfzjhArr
                                }, function (data) {
                                    $(listId).trigger("reloadGrid");
                                    hideSlidePanel(".page-course-panel");
                                });
                            },
                            //关联班级
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
                                var classs = "addClass";
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
                                if (optionVar.length > 1) {
                                    window.Msg.alert("只能关联单个班级!");
                                    return;
                                }
                                var classId = $("#class_s2").find("option").val();
                                var gradeNo = $("#class_s2").find("option").attr("grade");
                                var classNo = $("#class_s2").find("option").attr("classNo");
                                var idAry = $(listId).jqGrid("getGridParam", "selarrrow");
                                var studentIds = [];

                                for (var i = 0; i < idAry.length; i++) {
                                    var rowData = $(listId).jqGrid("getRowData", idAry[i]);
                                    var param = {};
                                    param["sfzjh"] = rowData.SFZJH;
                                    param["xjfh"] = rowData.stuCode;
                                    param["stuPk"] = rowData.stuPk;
                                    studentIds.push(param);
                                }
                                POST(saveClassUrl, {
                                        "classId": classId,
                                        "studentIds": studentIds,
                                        "roleCode": "0",
                                        "gradeNo": gradeNo,
                                        "classNo": classNo,
                                        "studentList": idAry
                                    },
                                    function (data) {
                                        $(listId).trigger("reloadGrid");
                                        hideSlidePanel(".page-class-panel");
                                    });
                            },
                            update_StuState: function (ev) {
                                idAry = $(listId).jqGrid("getGridParam", "selarrrow");
                                if (idAry.length === 0) {
                                    window.message({
                                        text: "请选择要设置的学生!",
                                        title: "提示"
                                    });
                                    return;
                                }
                                if (idAry.length > 0) {
                                    var studentIds = [];
                                    for (var i = 0; i < idAry.length; i++) {
                                        var rowData = $(listId).jqGrid("getRowData", idAry[i]);
                                        var param = {};
                                        param["state"] = rowData.state;
                                        param["studentPk"] = rowData.stuPk;
                                        studentIds.push(param);
                                    }
                                    window.message({
                                        title: '提醒',
                                        text: '确定设为随班就读吗?',
                                        buttons: {
                                            '确定': function () {
                                                    window.top.$(this).dialog("close");
                                                    $.ajax({
                                                        url: updateStuStateUrl,
                                                        type: "POST",
                                                        data: {
                                                            "studentIds": studentIds
                                                        },
                                                        dataType: "JSON",
                                                        success: function (data, xhr) {
                                                            if (data.success == "true") {
                                                                window.Msg.alert("设置成功!");
                                                            }
                                                            $(listId).trigger("reloadGrid");
                                                        }
                                                    });
                                                },
                                                "取消": function () {
                                                    window.top.$(this).dialog("close");
                                                }
                                        }
                                    });
                                }
                            },

                            cencel_Sbjd: function (ev) {
                                idAry = $(listId).jqGrid("getGridParam", "selarrrow");
                                if (idAry.length === 0) {
                                    window.message({
                                        text: "请选择要取消随班就读的学生!",
                                        title: "提示"
                                    });
                                    return;
                                }
                                if (idAry.length > 0) {
                                    var studentIds = [];
                                    for (var i = 0; i < idAry.length; i++) {
                                        var rowData = $(listId).jqGrid("getRowData", idAry[i]);
                                        var param = {};
                                        param["state"] = rowData.state;
                                        param["studentPk"] = rowData.stuPk;
                                        studentIds.push(param);
                                    }
                                    window.message({
                                        title: '提醒',
                                        text: '确定取消随班就读吗?',
                                        buttons: {
                                            '确定': function () {
                                                    window.top.$(this).dialog("close");
                                                    $.ajax({
                                                        url: cencelSbjdUrl,
                                                        type: "POST",
                                                        data: {
                                                            "studentIds": studentIds
                                                        },
                                                        dataType: "JSON",
                                                        success: function (data, xhr) {
                                                            if (data.success == "true") {
                                                                window.Msg.alert("取消成功!");
                                                            }
                                                            $(listId).trigger("reloadGrid");
                                                        }
                                                    });
                                                },
                                                "取消": function () {
                                                    window.top.$(this).dialog("close");
                                                }
                                        }
                                    });
                                }
                            },

                            cancelBTN: function () {
                                hideSlidePanel(".page-course-panel,.page-class-panel");
                            },
                    }); //from page.common.js
                    _initFormControls(); //from page.common.js
                    _initValidateForXTypeForm(editorFormId);
                    var _colModel = [{
                        name: 'stuPk',
                        label: '学生ID',
                        key: true,
                        hidden: true,
                        search: false
                    }, {
                        name: 'stuName',
                        label: '姓名',
                        align: "center",
                        search: false,
                        sortable: false,
                        formatter: function (ar1, ar2, ar3) {
                            return ar3.stuName + "(" + _stuState[ar3.state] + ")" || at3.stuName + ""
                        }
                    }, {
                        name: 'state',
                        label: '状态',
                        hidden: true,

                    }, {
                        name: 'stuCode',
                        label: '学籍号',
                        sortable: false,
                        width: 150,
                        align: "center"
                    }, {
                        name: 'SFZJH',
                        hidden: true,
                        search: false
                    }, {
                        name: 'schoolName',
                        label: '学校',
                        sortable: false,
                        autoWidth: true,
                        align: "left"
                    }, {
                        name: 'Class_No',
                        label: '班级',
                        sortable: false,
                        width: 120,
                        align: "center",
                        formatter: function (ar1, ar2, ar3) {
                            var isXjb = "";
                            if (ar3.Is_Xjb == "1") {
                                isXjb = "<span style='color:red;'>(新疆班)</span>";
                            }
                            if (ar3.Class_No != undefined && ar3.Class_No != "") {
                                return _types[ar3.Grade_No] + "(" + ar3.Class_No + ")班" + isXjb
                            } else {
                                return "";
                            }
                        }
                    }, {
                        name: 'courseList',
                        label: '科目',
                        sortable: false,
                        width: 100,
                        formatter: function (ar1, ar2, ar3) {

                            var arrList = ar3.courseList;
                            var arr = [];
                            if (arrList.length > 0) {
                                $.each(arrList, function (index, item) {
                                    var str = _types[item.Course];
                                    arr.push(str);
                                });
                            }
                            return arr || "";
                        }

                    }];
                    $(listId).jqGrid($.extend(defaultGridOpts, {
                        url: listUrl,
                        colModel: _colModel,
                        pager: pagerId,
                        loadComplete: function () { //禁用复选框
                                var rowIds = $(listId).jqGrid('getDataIDs'); //获取jqgrid中所有数据行的id
                                for (var i = 0; i < rowIds.length; i++) {
                                    var curRowData = $(listId).jqGrid('getRowData', rowIds[i]); //获取指定id所在行的所有数据.
                                    if (curRowData != null) {
                                        if (curRowData.state == 2) {
                                            $("input[type='checkbox']")[i + 1].disabled = true; //设置该行不能被选中。
                                            $("td")[i + 1].disabled = true;; //设置该行不能被选中。
                                            console.info($("td")[i + 1]);
                                        }
                                    }
                                }
                            },
                            onSelectAll: function (rowid, status) { //点击全选时触发事件
                                var rowIds = $(listId).jqGrid('getDataIDs'); //获取jqgrid中所有数据行的id
                                for (var k = 0; k < rowIds.length; k++) {
                                    var curRowData = $(listId).jqGrid('getRowData', rowIds[k]); //获取指定id所在行的所有数据.
                                    if (curRowData != null) {
                                        if (curRowData.state == 2) {
                                            $(listId).jqGrid("setSelection", rowIds[k], false); //设置该行不被选中。
                                        }
                                    }
                                }
                            },
                            onSelectRow: function (id) { //选择某行时触发事件
                                var curRowData = $(listId).jqGrid('getRowData', id);
                                if (curRowData != null) {
                                    if (curRowData.state == 2) {
                                        $(listId).jqGrid("setSelection", id, false); //设置该行不被选中。
                                    }
                                }
                            }
                    }));
                    var myGrid = $(listId);
                    $("#cb_" + myGrid[0].id).hide();
                    resizeFun();

                    $.ajax({
                        type: "POST",
                        url: "../stuManagement/stuInfo.do?command=getSequenceBySchoolCode",
                        data: {},
                        dataType: "JSON",
                        async: false,
                        success: function (data) {
                            $("#schoolType").val(data[0].sequence);
                            $("#schoolCode").val(data[0].schoolCode);
                        }
                    });

                    var schoolType = $("#schoolType").val();
                    if (schoolType == "4" || schoolType == "3" || schoolType == "2") {
                        $("#add_course").show();
                    } else {
                        $("#add_course").hide();
                    }

                    //添加页面下拉显示
                    $("#insertBTN").click(function (ev) {

                        var $i = $(ev.currentTarget).find("i"),
                            $piel = $(".page-addStudent-panel").show({
                                effect: "slide",
                                direction: "up",
                                easing: 'easeInOutExpo',
                                duration: 900
                            });
                    });

                    //添加操作
                    $("#saveBTN").click(function (ev) {
                        var url = "../stuManagement/stuInfo.do?command=addStudent";
                        var chinesename = $("#chinesename").val();
                        var idCard = $("#idCard").val();
                        var data = {
                            chinesename: chinesename,
                            idCard: idCard
                        };
                        //校验
                        if (chinesename == "" || chinesename == null) {
                            window.Msg.alert("姓名不能为空");
                            return;
                        }
                        if (idCard == "" || idCard == null) {
                            window.Msg.alert("身份证不能为空");
                            return;
                        }
                        window.message({
                            title: '提醒',
                            text: '确定添加此学生吗?',
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
                                                hideSlidePanel(".page-addStudent-panel");
                                            }
                                        });
                                    },
                                    '取消': function () {
                                        window.top.$(this).dialog("close");
                                    }
                            }
                        });
                    });

                    /*学校管理员申请调度学生*/
                    $("#applyOper").click(function () {
                        var selArr = $(listId).jqGrid('getGridParam', 'selarrrow');
                        //var rowData = $(listId).jqGrid('getRowData',rowId);
                        console.info(selArr);
                        if (selArr.length == 0) {
                            window.Msg.alert("请选择要申请的记录");
                            return;
                        }
                        if (selArr.length > 1) {
                            window.Msg.alert("每次只能申请单条记录!");
                            return;
                        }
                        var rowData = $(listId).jqGrid("getRowData", selArr);
                        var rowState = rowData.state;
                        if (rowState == 3) {
                            window.Msg.alert("该学生已经申请,不能重复申请");
                            return;
                        }
                        if (rowState == 2) {
                            window.Msg.alert("该学生已经调动,不能重复申请");
                            return;
                        }
                        var url = "../transfer/studentTransfer.do?command=submitApplyStudent";
                        window.message({
                            title: '提醒',
                            text: $(".page-dialog-panel").html(),
                            buttons: {
                                '确定': function () {
                                        var val = parent.$("#newadr").val();
                                        if (val !== null && val !== "") {
                                            window.top.$(this).dialog("close");
                                            $.ajax({
                                                url: url,
                                                type: "POST",
                                                data: {
                                                    selArr: selArr,
                                                    newadr: val
                                                },
                                                dataType: "JSON",
                                                success: function (data, xhr) {
                                                    /* if(data.mess == "repeatApply"){
    				   	        		window.Msg.alert("该学生已经申请,不能重复申请");
    				   	        		return;
    				   	        	} 
		       				   	     if(data.mess == "notSubmit"){
						   	        	window.Msg.alert("该学生已经调走,不能操作");
						   	        	return;
						   	        } */
                                                    if (data.mess == 'success') {
                                                        window.Msg.alert("申请成功!");
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

                    /*学校管理员撤销调度学生*/
                    $("#cancelOper").click(function () {
                        var selArr = $(listId).jqGrid('getGridParam', 'selarrrow');
                        if (selArr.length == 0) {
                            window.Msg.alert("请选择要撤销的记录");
                            return;
                        }
                        var url = "../transfer/studentTransfer.do?command=cancelsubmitApplyStudent";
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
            </script>
        </head>

        <body>
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
                                                <span>调动去向:</span>
                                            </div>
                                            <br>
                                            <form id="newform">
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

            <div class="page-list-panel">
                <div class="head-panel">
                    <div class="search-panel">
                        <div class="form-panel">
                            <table cellpadding="0" cellspacing="0" border="0" class="select">
                                <tr>
                                    <td style="font-size: 14px;">学生状态:</td>
                                    <td>
                                        <select class="searchParam" id="studentState" name="studentState" style="width:199px;">
                                        </select>
                                    </td>
                                    <td style="padding-left: 175px; font-size: 14px;">年级:</td>
                                    <td>
                                        <select class="searchParam" id="studentGrade" name="studentGrade" style="width:199px;">
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="font-size: 14px;">学科:</td>
                                    <td>
                                        <select class="searchParam" id="studentCourse" name="studentCourse" style="width:199px;">
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
                        <input type="hidden" id="schoolType" />
                        <input type="hidden" id="schoolCode" />
                        <table style="height: 100%;" cellpadding="0" cellspacing="0" border="0">

                            <tr>
                                <td style="padding-left: 12px; padding-right: 24px;"><i class="fa fa-list-ul micon"></i>
                                </td>
                                <td class="buttons">
                                    <button id="insertBTN">
                                        <i class="fa fa-plus"></i>添加
                                    </button>
                                    <button id="add_course">
                                        <i class="fa fa-plus"></i>关联科目
                                    </button>
                                    <button id="update_StuState">
                                        <i class="fa fa-plus"></i>设为随班就读
                                    </button>
                                    <button id="cencel_Sbjd">
                                        <i class="fa fa-plus"></i>取消随班就读
                                    </button>
                                    <button id="add_class">
                                        <i class="fa fa-plus"></i>关联班级
                                    </button>
                                    <button id="applyOper">
                                        <i class="fa fa-plus"></i>申请调动
                                    </button>
                                    <button id="cancelOper">
                                        <i class="fa fa-trash-o"></i>撤销调动
                                    </button>
                                </td>
                                <td style="padding-left: 24px; padding-right: 5px;">
                                    <input id="fastQueryText" type="text" placeholder="输入姓名或学籍号" style="line-height: 1em; font-size: 1em; cursor: text;" />
                                </td>
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
                                    <td>
                                        <select id="course_s1" name="s1" style="width: 150px; height: 220px;" multiple="multiple">
                                        </select>
                                    </td>
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
                                    <td>
                                        <select id="course_s2" name="s2" style="width: 150px; height: 220px;" multiple="multiple"></select>
                                    </td>
                                </tr>
                            </table>
                        </div>
                    </div>
                </div>
            </div>


            <!-- 添加随班就读的学生 -->
            <div class="page-addStudent-panel full-drop-panel">
                <div class="title-bar">
                    <h4>
				    <i class="fa fa-plus"></i>
			       </h4>
                    <div class="btn-area">
                        <div style="margin-top: 4px;">
                            <button id="saveBTN">
                                <i class="fa fa-check"></i>保存
                            </button>
                            <button id="cancelBTN" onclick="cancelBTN()">
                                <i class="fa fa-times"></i>取消
                            </button>
                        </div>
                    </div>
                </div>
                <div class="page-content">
                    <div class="page-inner-content">


                        <div id="frame1" style="margin-left:300px;margin-top:50px;">
                            <ul>
                                <li>
                                    <label for="chinesename">
                                        姓名
                                        <input id="chinesename" class="form-control" type="text" style="width:200px;" name="chinesename" placeholder="请输入姓名">
                                    </label>
                                </li>
                                <li>
                                    <label for="idCard">
                                        <span style="margin-left:-12px">身份证</span>
                                        <input id="idCard" class="form-control" type="text" style="width:200px;" name="idCard" placeholder="请输入身份证号">
                                    </label>
                                </li>
                            </ul>
                        </div>


                    </div>
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
                        <div id="d2">选班级</div>
                        <div id="d3">
                            <table cellpadding="0" cellspacing="8">
                                <tr>
                                    <td style="font-size: 16px;">可选班级</td>
                                    <td>&nbsp;</td>
                                    <td style="font-size: 16px;">已选班级</td>
                                </tr>
                                <tr>
                                    <td>
                                        <select id="class_s1" name="s1" style="width: 220px; height: 300px;" multiple="multiple">
                                        </select>
                                    </td>
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
                                    <td>
                                        <select id="class_s2" name="s2" style="width: 220px; height: 300px;" multiple="multiple"></select>
                                    </td>
                                </tr>
                            </table>
                        </div>
                    </div>
                </div>
            </div>

        </html>