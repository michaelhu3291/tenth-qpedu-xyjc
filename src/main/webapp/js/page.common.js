
(function (topwin) {
    var tfun = function (obj) {
        window.console.log(obj);
    };
    window.message = topwin.Msg ? topwin.Msg.message : tfun;
    window.Msg = {error: topwin.Msg ? topwin.Msg.error : tfun, alert: topwin.Msg ? topwin.Msg.alert : tfun};
    window.error = topwin.Msg ? topwin.Msg.message : tfun;
    window.frameDialog = topwin.frameDialog;
    window.dialog = topwin.dialog;
    window.frameMask = topwin.frameMask; //全局遮罩
    window.frameUnMask = topwin.frameUnMask; //取消全局遮罩
    window.fullShipFrame = topwin.fullShipFrame;
})(window.top);
var editorFormId = "#editorForm";//默认编辑区表单ID
//默认网格控件配置
var defaultGridOpts = {
    datatype: "json",
    mtype: "POST",
    altclass: 'alt-row',
    altRows: true,
    hoverrows: false,
    viewrecords: true,
    width: $("body").innerWidth() - 24,
    autowidth: true,
    shrinkToFit: false,
    rownumbers: true,
    rownumWidth: 30,
    rowNum: 20,
    rowList: [ 10, 20, 30, 50, 100 ],
    multiboxonly: true,
    multiselect: true,
    gridview: true,
    beforeRequest: function () {
        $(this).jqGrid('setGridParam', {
            postData: getRequestParams ? getRequestParams() : {}
        });
    }
};
var projectName = "";
/**
 *POST提交方法
 * url
 * data 数据
 * mask bool类型  提交的同时是否要有遮罩 默认是true
 * mel jquery查询表达式 可以是任何block布局的元素  默认是“body”
 * 示例  POST("http://localhost:8080/tmis/masterdata/taxFilingUnit.do?command=load",{id:"1"},function(data){}})
 */
window.POST = function (url, data, callback, mask, mel) {
    var isMask = typeof mask === "undefined" ? true : mask, $maskEl = null;
    if (isMask) {
        $maskEl = typeof mel === "undefined" ? $("body") : $(mel);
        $maskEl.mask("与服务器交互数据中,请耐心等待...");
    }
    return $.ajax({"url": url, "data": data, type: "post", dataType: "json", success: function (data, xhr) {
        if (data.validateErrors) {
            var status = data.status;
            if (status == 0) {
                var errors = [], validateErrors = data.validateErrors;
                for (var i in validateErrors) {
                    var cdata = validateErrors[i];
                    errors.push(cdata.message);
                }
                var msg = errors.join("<br/>");
                window.error({"title": "验证错误", text: msg});
            } else {

            }
            return;
        }
        if (data.error) {
            /*window.error({"title":"发生错误",text:data.error});*/
            window.Msg.error(data.error);
            return;
        }
        callback(data, xhr);
        if (data.message) {
            window.Msg.alert(data.message,
                "消息", "info", 2
            );
        }
    }}).fail(function (err, xhr) {
        if (err && ( err.status == 0 || err.status == 200)) {
            return;
        }
        window.error({title: "发生" + err.status + "错误", text: err.statusText});
    }).always(function () {
        if (isMask) {
            $maskEl.unMask();
        }
    });
};

/**
 *GET提交方法
 * url
 * data 数据
 * mask bool类型  提交的同时是否要有遮罩 默认是true
 * mel jquery查询表达式 可以是任何block布局的元素  默认是“body”
 * 示例  GET("http://localhost:8080/tmis/masterdata/taxFilingUnit.do?command=load",{id:"1"},function(data){}})
 */
window.GET = function (url, data, callback, mask, mel) {
    var isMask = typeof mask === "undefined" ? true : mask, $maskEl = null;
    if (isMask) {
        $maskEl = typeof mel === "undefined" ? $("body") : $(mel);
        $maskEl.mask("与服务器交互数据中,请耐心等待...");
    }
    return $.ajax({"url": url, "data": data, type: "get", dataType: "json", success: function (data, xhr) {
        if (data.error) {
            window.error({"title": "发生错误", text: data.error});
            return;
        }
        callback(data, xhr);
        if (data.message) {
            window.message({
                text: data.message,
                title: "消息"
            });
        }
    }}).fail(function (err, xhr) {
        if (err && ( err.status == 0 || err.status == 200)) {
            return;
        }
        window.error({title: "发生" + err.status + "错误", text: err.statusText});
    }).always(function () {
        if (isMask) {
            $maskEl.unMask();
        }
    });
};

/**
 * 重置窗体大小
 */
var resizeFun = function (laveHei) {
   var $doc = $("body"), allHei = $doc.height(), allWid = $doc.width(), /*laveHei = 495,*/ laveWid = allWid - 26,autoHei = false;
   if (laveHei==undefined||laveHei==""||laveHei==null) {
	   autoHei = true;
	   var laveHei=12;
	   $(".head-panel,.ui-jqgrid-view .ui-jqgrid-titlebar,.ui-jqgrid-view .ui-jqgrid-hdiv,.ui-jqgrid-pager").each(function (index, item) {
	        var $item = $(item);
	        if ($item.css("display") !== "none") {
	            laveHei += $item.outerHeight(true);
	        }
	    });
	   laveHei = allHei - laveHei;
   }
    var $list = $(typeof listId === "string" ? listId : "");
    if ($list.length > 0) {
        if (allWid === window.oldWidth) {
        	if(autoHei){
        		$list.jqGrid("setGridHeight", laveHei).jqGrid("setGridWidth", laveWid, false).jqGrid("autoFillWidth");
        	}else {
        		$list.jqGrid("setGridWidth", laveWid, false).jqGrid("autoFillWidth");
			}
        } else if (laveHei === window.oldHeight) {
            $list.jqGrid("setGridWidth", laveWid, false).jqGrid("autoFillWidth");
        } else {
        	if(autoHei){
        		$list.jqGrid("setGridHeight", laveHei).jqGrid("setGridWidth", laveWid, false).jqGrid("autoFillWidth");
        	}else {
        		$list.jqGrid("setGridWidth", laveWid, false).jqGrid("autoFillWidth");
			}
        }
    }
    window.oldWidth = allWid;
    window.oldHeight = laveHei;
    if (window._resize) {
        window._resize(allWid, allHei);
    }
};

/**
 * 重置窗体大小
 */
var resizeFunFour = function (laveHei) {
   var $doc = $("body"), allHei = $doc.height(), allWid = $doc.width(), /*laveHei = 495,*/ laveWid = allWid - 26,autoHei = false;
   if (laveHei==undefined||laveHei==""||laveHei==null) {
	   autoHei = true;
	   var laveHei=12;
	   $(".head-panel,.ui-jqgrid-view .ui-jqgrid-titlebar,.ui-jqgrid-view .ui-jqgrid-hdiv,.ui-jqgrid-pager").each(function (index, item) {
	        var $item = $(item);
	        if ($item.css("display") !== "none") {
	            laveHei += $item.outerHeight(true);
	        }
	    });
	   laveHei = allHei - laveHei;
   }
	var $list4 = $(typeof list4Id === "string" ? list4Id : "");
	if ($list4.length > 0) {
	    if (allWid === window.oldWidth) {
	    	if(autoHei){
	    		$list4.jqGrid("setGridHeight", laveHei).jqGrid("setGridWidth", laveWid, false).jqGrid("autoFillWidth");
	    	}else {
	    		$list4.jqGrid("setGridWidth", laveWid, false).jqGrid("autoFillWidth");
	    		$list4.jqGrid("setGridHeight", laveHei).jqGrid("setGridWidth", laveWid, false).jqGrid("autoFillWidth");
			}
	    } else if (laveHei === window.oldHeight) {
	        $list4.jqGrid("setGridWidth", laveWid, false).jqGrid("autoFillWidth");
	    } else {
	    	if(autoHei){
	    		$list4.jqGrid("setGridHeight", laveHei).jqGrid("setGridWidth", laveWid, false).jqGrid("autoFillWidth");
	    	}else {
	    		$list4.jqGrid("setGridWidth", laveWid, false).jqGrid("autoFillWidth");
	    		$list4.jqGrid("setGridHeight", laveHei).jqGrid("setGridWidth", laveWid, false).jqGrid("autoFillWidth");
			}
	    }
	}
	window.oldWidth = allWid;
	window.oldHeight = laveHei;
	if (window._resize) {
	    window._resize(allWid, allHei);
	}
};

/**
 * 获取需要附加的请求参数
 */
var getRequestParams = function (vals) {
    var result = $(".search-panel,.multiselect").getFormData();
    if ($(".search-panel").data("show")) {
        result["isFast"] = false;
    } else {
        result["isFast"] = true;
    }
    result["q"] = $.trim($("#fastQueryText").val());
    result["o"] = $.trim($("#queryParam").val());
    return result;
};

/**
 * 从上方显示出panel
 */
var showSlidePanel = function (el) {
    return $(el).show({
        effect: "slide",
        direction: "up",
        easing: 'easeInOutExpo',
        duration: 600
    });
};
/**
 * 往上方收起panel
 */
var hideSlidePanel = function (el) {
    return $(el).hide({
        effect: "slide",
        direction: "up",
        easing: 'easeInOutExpo',
        duration: 400,
        complete:function(){
        	$(".ui-effects-wrapper div:eq(0)").unwrap();
        }
    });
};


var showShipPanel = function ($el, callback) {
    $el.show(
        {
            effect: "fade",
            duration: 200,
            complete: function () {
                $(this).find(".bottom-panel").show(
                    {
                        effect: "slide",
                        direction: "down",
                        easing: 'easeInOutExpo',
                        duration: 300,
                        complete: function () {
                            if (callback) {
                                callback();
                            }
                        }
                    }
                );
            }
        }
    );
};
var hideShipPanel = function ($el, callback) {
    $el.find(".bottom-panel").hide(
        {
            effect: "slide",
            direction: "down",
            easing: 'easeInOutExpo',
            duration: 200,
            complete: function () {
                $el.hide(
                    {
                        effect: "fade",
                        duration: 400,
                        complete: function () {
                            if (callback) {
                                callback();
                            }
                        }
                    }
                );
            }
        }
    );
};

/**
 * 初始化按钮
 *
 * funs 对象{buttonIdOrName:function(){}} 处理程序，可以进行覆盖原始的方法
 * buttons 数组，jquery查询表达式  可以把不在默认检索区域的按钮加入
 */
var _initButtons = function (funs, buttons) {//
    var overFuns = $.extend({
        insertBTN: function (ev) {
            //console.log(ev.target);
            var $i = $(ev.currentTarget).find("i"),
                $piel = showSlidePanel(".page-editor-panel").find("h4 i").removeClass();
            if ($i.length) {
                $piel.addClass($i.attr("class"));
            }
            window._EDITDATA = undefined;
            $(editorFormId).resetHasXTypeForm();
            if (window._insert) {
                window._insert(ev);
            }
            //console.log(this);
        },
        deployData: function (ev) {
            //console.log(ev.target);
            var $i = $(ev.currentTarget).find("i"),
                idAry = $(listId).jqGrid("getGridParam", "selarrrow");
            if (idAry.length === 0) {
                window.message({
                    text: "请选择要发布的记录!",
                    title: "提示"
                });
                return;
            }
            if (idAry.length > 1) {
                window.message({
                    text: "每次只能发布单条记录!",
                    title: "提示"
                });
                return;
            }
            $piel = showSlidePanel("#test").find("h4 i").removeClass();
        },
        editBTN: function (ev) {
            var $i = $(ev.currentTarget).find("i"),
                idAry = $(listId).jqGrid("getGridParam", "selarrrow");
            if (idAry.length === 0) {
                window.message({
                    text: "请选择要修改的记录!",
                    title: "提示"
                });
                return;
            }
            if (idAry.length > 1) {
                window.message({
                    text: "每次只能修改单条记录!",
                    title: "提示"
                });
                return;
            }
            GET(loadUrl, {id: idAry[0], dc: (new Date()).getTime()}, function (data) {
                var $piel = showSlidePanel(".page-editor-panel").find("h4 i").removeClass();
                if ($i.length) {
                    $piel.addClass($i.attr("class"));
                }
                if (data.fieldConfig) {
                    $(editorFormId).configFormField(data.fieldConfig);
                    window._FIELDCONFIG = data.fieldConfig;
                    $(editorFormId).resetHasXTypeForm(data.entity);
                    window._EDITDATA = data.entity;
                } else {
                    $(editorFormId).resetHasXTypeForm(data);
                    window._EDITDATA = data;
                }
                if (window._edit) {
                    window._edit();
                }
            });
        },
        viewBTN: function (ev) {
            var $i = $(ev.currentTarget).find("i"),
                idAry = $(listId).jqGrid("getGridParam", "selarrrow");
            if (idAry.length === 0) {
                window.message({
                    text: "请选择要查看的记录!",
                    title: "提示"
                });
                return;
            }
            if (idAry.length > 1) {
                window.message({
                    text: "每次只能查看单条记录!",
                    title: "提示"
                });
                return;
            }
            GET(loadUrl, {id: idAry[0], dc: (new Date()).getTime()}, function (data) {
                var $piel = showSlidePanel(".page-editor-panel").find("h4 i").removeClass();
                if ($i.length) {
                    $piel.addClass($i.attr("class"));
                }
                if (data.fieldConfig) {
                    $(editorFormId).configFormField(data.fieldConfig);
                    window._FIELDCONFIG = data.fieldConfig;
                    $(editorFormId).resetHasXTypeForm(data.entity);
                    window._EDITDATA = data.entity;
                } else {
                    $(editorFormId).resetHasXTypeForm(data);
                    //window._EDITDATA = data;
                    $(editorFormId).find("input,textarea").each(function () {
                        $(this).attr("disabled", "disabled");
                    });
                    $("#editorSave").css("display", "none");
                    $("#resetBTN").css("display", "none");
                }
                if (window._edit) {
                    window._edit();
                }
            });
        },
        editorSave: function () {
            if ($(editorFormId + " [data-validate]").valid()) {
                POST(saveUrl, $(editorFormId).getFormData(), function (data) {
                    $(listId).trigger("reloadGrid");
                    hideSlidePanel(".page-editor-panel");
                });
            }
        },
        resetBTN: function (ev) {
            var $pn = $(ev.target).closest(".page-editor-panel");
            if (window._FIELDCONFIG) {
                $(editorFormId).configFormField(window._FIELDCONFIG);
            }
            $pn.find("form").resetHasXTypeForm(window._EDITDATA);
            if (window._reset) {
                window._reset($pn);
            }
        },
        deleteBTN: function () {
            var idAry = $(listId).jqGrid("getGridParam", "selarrrow");
            if (idAry.length === 0) {
                window.message({
                    text: "请选择要删除的记录!",
                    title: "提示"
                });
                return;
            }

            window.message({
                text: "确认要删除所选择的记录吗?",
                title: "提醒",
                buttons: {
                    "确认": function () {
                        window.top.$(this).dialog("close");
                        POST(deleteUrl, {id: idAry}, function (data) {
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
        cancelBTN: function () {
            hideSlidePanel(".page-editor-panel,.page-view-panel");
            $(listId).trigger("reloadGrid");
        },
        cancelBTNForSelf: function () {
            hideSlidePanel(".page-editor-panel,.page-view-panel");
            $("#editorSave").show();
            $("#resetBTN").show();
        },
        cancelBTNForDeploy: function () {
            hideSlidePanel("#test");
        },
        searchRip: function (ev) {
            $(".search-panel").show().data("show", true);
            $(ev.target).closest("td").hide().prev().hide();
            resizeFun();
        },
        searchRipClose: function () {
            $(".search-panel").hide().data("show", false);
            $(".toolbar table td:last").show().prev().show();
            resizeFun();
        },
        advancedSearch: function () {
            $(listId).trigger("reloadGrid", [
                {page: 1}
            ]);
        },
        fastSearch: function () {
            $(listId).trigger("reloadGrid", [
                {page: 1}
            ]);
        },
        exportBTN: function () {
            var url = "/TMIS/common/exportExcel.do?command=export&type=" + exportKey;
            var form = $("#exportExcelForm");
            form.attr("action", url);
            form.find("input[name=data]").val(encodeURI(JSON.stringify(getRequestParams())));
            form.get(0).submit();
        }
    }, funs);
    var $btns = $(".toolbar button,.title-bar button,.search-panel button,.bottom-bar button");
    if (buttons) {
        for (var i in buttons) {
            $btns = $btns.add(buttons[i]);
        }
    }
    $btns.button().click(function (ev) {
        var id = $(this).attr("id") || $(this).attr("name");
        if (id && overFuns[id]) {
            overFuns[id](ev);
        }
    });
};


var _types = {
        "01": "小班",
        "02": "中班",
        "03": "大班",
        "11": "一年级",
        "12": "二年级",
        "13": "三年级",
        "14": "四年级",
        "15": "五年级",
        "16": "六年级",
        "17": "七年级",
        "18": "八年级",
        "19": "九年级",
        "31": "高一",
        "32": "高二",
        "33": "高三",
        "34" : "高预(民族班)",
        "yw":"语文",
        "sx":"数学",
        "yy":"外语",
        "wl":"物理",
        "hx":"化学",
        "ty":"体育",
        "sxzz":"思想政治",
        "ls":"历史",
        "dl":"地理",
        "sw":"生物",
        "ms":"美术",
        "zr":"自然",
        "yyue":"音乐",
        "xxkj":"信息科技",
        "tzxkc":"拓展型课程",
        "yjxkc":"研究型课程",
        "kx":"科学",
        "njyy":"牛津英语",
        "xsjyy":"高中新世纪英语",
        "sxq":"第一学期",
        "xxq":"第二学期",
        "qz":"期中",
        "qm":"期末",
        "aj":"A卷",
        "bj":"B卷",
        "lk":"理科",
        "wk":"文科",
        "lhhj":"理化合卷",
        "0":"小学",
        "1":"初中",
        "2":"高中",
        "0, 1":"一贯制",
        "1, 2":"完校",
        "0, 1, 2":"其它",
        "qb":"全部",
        "sbjd":"随班就读",
        "bx":"本校",
        "yx":"原校",
        "xjb":"新疆班",
    };
	
var _typesClass = {
        "01": "(1)班",
        "02": "(2)班",
        "03": "(3)班",
        "04": "(4)班",
        "05": "(5)班",
        "06": "(6)班",
        "07": "(7)班",
        "08": "(8)班",
        "09": "(9)班",
        "10": "(10)班",
        "11": "(11)班",
        "12": "(12)班",
        "13": "(13)班",
        "14": "(14)班",
        "15": "(15)班",
        "16": "(16)班",
        "17": "(17)班",
        "18": "(18)班",
        "19": "(19)班",
        "20": "(20)班",
        "21": "(21)班",
        "22": "(22)班",
        "23": "(23)班",
        "24": "(24)班",
		"25": "(25)班",
	    "26": "(26)班",
	    "27": "(27)班",
	    "28": "(28)班",
	    "29": "(29)班"
    };

  var _stuState={
	  "0":"在读",
	  "1":"随班就读",
	  "2":"调出",
	  "3":"待调动",
	  "4":"调入",
	  "5":"新疆班"
  };
/**
 *初始化表单中的控件
 * ovFuns 对象{inputNameOrId:function(el){}}  可以复写对某个控件的处理
 */
var _initFormControls = function (ovFuns) {
    var $forms = $("body");
    $forms.find("[data-xtype]").each(function (index, item) {
        var $item = $(item), data = $item.data(),
            ename = $item.attr("name"), eid = $item.attr("id");
        if (data.opt && typeof data.opt === "string") {
            data.opt = new Function("return " + data.opt + ";")();
        }
        if (ovFuns && ovFuns[ename]) {
            ovFuns[ename].call($item);
            return;
        }
        switch (data.xtype.toLowerCase()) {
            case "ajaxchosen":
                $item.ajaxChosen();
                break;
            case "chosen":
                $item.chosen();
                break;
            case "buttonset":
                $item.buttonset();
                break;
            case "datetime":
            	$item.datepicker($.extend({
                    dateFormat: "yy-mm-dd",
                    changeMonth: true,
                    changeYear: true
                }, data.opt));
                break;
            case "upload":
            	(function ($titem) {
                    $titem.uploadFile($.extend({
                        url: projectName+"/platform/accessory_.do?command=upload",
                        removeUrl: projectName+"/platform/accessory_.do?command=remove",
                        dowloadUrl: projectName+"/platform/accessory_.do?command=download&id={id}",
                        returnType: "JSON",
                        showDone: false,
                        showStatusAfterSuccess: false
                    }, data.opt));
                })($item);
            	
                break;
            case "digits":
                break;
            case "number":
                break;
            case "email":
                break;
            case "url":
                break;
            case "hidden":
                break;
            case "pick":
                $item.pick();
                break;
        }
    });
};
/**
 *   yuanyuan.zhang
 *   begin
 *
 *   设置表单的属性 data-xtype 时候的生成控件
 *
 */
var formuser=function($parent,deferred){
    if(typeof deferred !="undefined"){
        setFormuser($parent,deferred);
        return deferred.promise();
    }else{
        setFormuser($parent);
    }
};

var setFormuser = function ($parent,deferred){
    var formElements = $parent.find("input[data-xtype]:not([type=button]):not([type=radio]),select[data-xtype],textarea[data-xtype],input[data-xtype][type=radio]:checked,input[data-xtype][type=checkbox]");
    for (var i = 0; i < formElements.length; i++) {
        var item = formElements[i], $item = $(item),
            data = $item.data(), dtype = data.dtype,
            isread = $item.attr("data-isread"), isreadonly = $item.attr("data-isreadonly"),
            xtype = data.xtype, datacode = data.datacode,code = $item.attr("data-code"),
            label = $item.attr("name") || $item.attr("id");
        var date=new Date(),uploadTime="fileListTD"+date.getTime();
        if ( isread && isreadonly) {
            if (typeof dtype === "undefined") {
                dtype = "string";
            }
            switch (xtype) {
                case "mychosen":{
                    $item.chosen();
                    break;
                }
                case "defaultadvice"://默认意见标签
                {
                	 $item.hide();
                     $("<div class='countersign-div' name='defaultadvice-"+label+"-div'></div>").insertAfter($item);
                     var $pardiv = $item.next(),appendhtml,param=$item.attr("param"), selstr, $sel,paramid,paramkey;
                     if(param && param !=null && param !=""){
                         var paramarr=param.split(":");
                         paramkey=paramarr[0];
                         paramid=paramarr[1];
                     }else{
                         paramkey=null;
                         paramid=null;
                     }
                     $pardiv.empty();
                     if(isread === "1" && isreadonly === "2") {//显示
                         appendhtml="<div><input type='hidden' name='defaultadvice'/><input style='width: 80%;border: none;' type='text' name='set-defaultadvice'/><button name='sel-" + label + "' type='button' class='ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only' style='width:60px;height:28px;margin-left:5px;' value='选择'>选择</button><span name='set-name' style='display: inline-block;size: 14px;margin-left: 10px;'></span><span name='time' style='display: inline-block;size: 14px;margin-left: 10px;'></span></div>"+
                         "<div id='tabs' class='frametab' title='选择意见' style='display: none;' > <select id='sel-advice' style='width:200px;margin-bottom:10px;'><option></option><option value='1'>同意</option><option value='2'>不同意</option><option value='3'>同意，请#同志#阅示</option></select>"+
                         "<div id='tabs-1' class='subTab' style='display:none;'><div class='zTreeDemoBackground left'><ul id='treeDemo' class='ztree'></ul></div> </div></div>";
                         
                     }else if((isread === "1" && isreadonly === "1")){//只读
                         appendhtml="<div><input type='hidden' name='defaultadvice'/><input readonly='readonly' style='background: #ffffff;width: 80%;border: none;' type='text' name='set-defaultadvice'/><span name='set-name' style='display: inline-block;size: 14px;margin-left: 10px;'></span><span name='time' style='display: inline-block;size: 14px;margin-left: 10px;'></span></div>";
                     }
                     $pardiv.append(appendhtml);
                     selstr = "button[name=sel-" + label + "]";
                     $sel = $(selstr);
                     setClick($sel,paramid,paramkey);
                     break;
                }
                case "advice" ://意见标签
                {
                    $item.hide();
                    var $befdiv= $item.parent().children("span"),divWid = $befdiv.attr("width");
                    $("<div class='advice' name='sel-" + label + "-div'></div>").insertAfter($item);
                    var $pardiv = $item.next("div"),param=$item.attr("param"), selstr, $sel,paramid,paramkey;
                    if(param && param !=null && param !=""){
                        var paramarr=param.split(":");
                        paramkey=paramarr[0];
                        paramid=paramarr[1];
                    }else{
                        paramkey=null;
                        paramid=null;
                    }
                    $pardiv.empty();
                    //新advice控件
                    if (isread === "1" && isreadonly === "2") {
                        $pardiv.append("<div class='advice-div'><input type='hidden' name='ids-" + label + "'><textarea readonly='readonly' name='names-" + label + "' class='savecheckednametextArea'></textarea><button name='sel-" + label + "' type='button' class='ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only' style='width:60px;height:28px;margin-top:-13px;margin-left:5px;' value='选择'>选择</button></div>" +
                            "<div style='display: none;'> <div name='showtree' style='overflow-x: hidden;white-space:normal'><div class='cenbox_tree' align='center' style='float:left'> <div class='zTreeDemoBackground left'> " +
                            "<ul name='ztreeul' class='ztree'></ul></div></div></div> </div>");
                        if(code === "YES"){
                        	var $defaText = $("textarea[name=names-" + label + "]");
                        	getNowUserName($defaText,paramkey);
                        }
                    } else if ((isread === "1" && isreadonly === "1")) {
                        $pardiv.append("<div class='advice-div'><input type='hidden' name='ids-" + label + "'><textarea readonly='readonly' style='background:#ffffff !important;line-height: 28px;' class='savecheckednametextArea' name='names-" + label + "'></textarea><button name='sel-" + label + "' type='button' style='display:none;'></button></div>" +
                            "<div style='display: none;'> <div name='showtree' style='overflow-x: hidden;white-space:normal'><div class='cenbox_tree' align='center' style='float:left'> <div class='zTreeDemoBackground left'> " +
                            "<ul name='ztreeul' class='ztree'></ul></div></div></div> </div>");
                    }
                    //新advice触发事件
                      selstr = "button[name=sel-" + label + "]";
                      $sel = $(selstr);
                      addClick($sel,paramkey,paramid);//给按钮添加点击事件
                    break;
                }
                case "defaultsign":{
                	$item.hide();
                	$("<div class='defaultSign-div' name='defaultSign-"+label+"-div'></div>").insertAfter($item);
                    var $pardiv = $item.next("div"),appendhtml;$pardiv.empty();
                    if(isread === "1" && isreadonly === "2"){
                    	 $pardiv.append("<span name='ids-" + label + "' style='display: inline-block;width: 80%;height: 30px;margin-left: 30px;margin-top: 14px;cursor: pointer;'>" +
                            "<input type='hidden' name='savenowtime'/></span><span name='showname' class='defaultSign-showname' ></span><span  name='showdate'  class='defaultSign-showdate'></span>");
                        var $defaultSign = $("span[name=ids-" + label + "]");
                        setNameAndDate($defaultSign);
                    }else if ((isread === "1" && isreadonly === "1")){
                    	$pardiv.append("<span style='display: inline-block;width: 80%;height: 30px;margin-left: 30px;margin-top: 14px;cursor: pointer;'>" +
                            "<input type='hidden' name='savenowtime'/></span><span name='showname' class='defaultSign-showname' ></span><span  name='showdate'  class='defaultSign-showdate'></span>");
                    }
                    break;
                }
                case "sign"://签字标签
                {
                    $item.hide();
                    $("<div class='sign-div' name='sign-"+label+"-div'></div>").insertAfter($item);
                    var $pardiv = $item.next("div"),appendhtml;$pardiv.empty();
                    if(isread === "1" && isreadonly === "2"){
                        appendhtml="<span onclick='setNameAndDate(this)' style='display: inline-block;width: 30px;height: 50px;margin-left: 30px;margin-top: 14px;cursor: pointer;'>" +
                            "<img style='width:37px;' src='"+projectName+"/theme/image/pen.png'/><input type='hidden' name='savenowtime'/></span><span name='showname' class='sign-showname' ></span><span  name='showdate'  class='sign-showdate'></span>";
                    }else if ((isread === "1" && isreadonly === "1")){
                        appendhtml="<span style='display: inline-block;width: 30px;height: 50px;margin-left: 30px;margin-top: 14px;cursor: pointer;'>" +
                            "<input type='hidden' name='savenowtime'/></span><span name='showname' class='sign-showname' ></span><span  name='showdate'  class='sign-showdate'></span>";
                    }
                    $pardiv.append(appendhtml);
                    break;
                }
                case "endorse":{//签署意见
                    $item.hide();
                    $("<div class='endorse-div' name='endorse-"+label+"-div'></div>").insertAfter($item);
                    var $pardiv = $item.next("div"),appendhtml;$pardiv.empty();
                    if(isread === "1" && isreadonly === "2"){//显示不只读
                        appendhtml="<textarea  class='endorse-textarea' style='width:98%;height: 33px;'></textarea><span style='display:inline-block;position:absolute;right:128px;width: 100px;height: 24px; font-size: 13px;color: black;line-height: 24px;bottom: 1px; '>" +
                            "</span> <input type='hidden' name='savenowtime'/> <span style='display: inline-block; position: absolute; right: 17px;bottom: 1px;width: 108px;font-size: 13px; color: black;height: 24px;line-height: 24px;'></span>";
                    }else if((isread === "1" && isreadonly === "1")){ //只读
                        appendhtml="<textarea  readonly='readonly' class='endorse-textarea' style='width:98%; background: #ffffff;height: 33px;'></textarea><span style='display:inline-block;position:absolute;right:128px;width: 100px;height: 24px; font-size: 13px;color: black;line-height: 24px;bottom: 1px; '>" +
                            "</span><input type='hidden' name='savenowtime'/><span style='display: inline-block; position: absolute; right: 17px;bottom: 1px;width: 108px;font-size: 13px; color: black;height: 24px;line-height: 24px;'></span>";
                    }
                    $pardiv.append(appendhtml);
                    break;
                }
                case "fileadvice":{//附件意见标签
                    $item.hide();
                    $("<div class='fileadvice-div' name='fileadvice-"+label+"-div'></div>").insertAfter($item);
                    var $pardiv = $item.next("div"),appendhtml;$pardiv.empty();
                    $("<div class='fileAdvice-message'><span name='date' style='display: inline-block; margin-left: 10px; margin-top: 5px; margin-bottom: 5px; font-size: 14px;color: black; font-style: italic'></span><span style='display: inline-block; font-size: 14px;margin-left: 10px;color: black; font-style: italic' name='dept'></span><span style='display: inline-block; margin-left: 10px; font-size: 14px;color: black; font-style: italic' name='user'></span></div>").appendTo($pardiv);
                    if(isread === "1" && isreadonly === "2") {//显示不只读
                        appendhtml="<div class='fileadvice-div-firstchild'><textarea name='showadivce'></textarea></div>"+
                            "<div style='margin-left: 8px;'><input data-xtype='upload' data-appendto='div[name="+uploadTime+"]' type='file' name='"+label+"-upload'  style='width:255px;' data-button-text='请上传文件'/><div name='"+uploadTime+"'></div></div>";
                        $pardiv.append(appendhtml);
                        var uploaditem=$pardiv.find("input[data-xtype=upload]").eq(0);
                        uploadFile(uploaditem);
                    }else if((isread === "1" && isreadonly === "1")){//只读
                        appendhtml="<div class='fileadvice-div-firstchild'><textarea readonly='readonly' name='showadivce' style='background:#FFFFFF'></textarea></div>"+
                            "<div style='margin-left: 8px;'><input  data-xtype='upload' data-appendto='div[name="+uploadTime+"]' type='file' name='"+label+"-upload'  style='width:255px;' data-button-text='请上传文件'/><div name='"+uploadTime+"'></div></div>";
                        $pardiv.append(appendhtml);
                        var uploaditem=$pardiv.find("input[data-xtype=upload]").eq(0);
                        uploadFile(uploaditem, function (uploaditem) {
                            uploaditem.attr({"data-disabled":true});
                        });
                        uploaditem.data().onlyView(true);
                    }
                    break;
                }
                case "officeupload":{//公文文档上传控件(套红标签)
                    $item.hide();
                    $("<div class='officeupload-div' name='officeupload-"+label+"-div'></div>").insertAfter($item);
                    var $pardiv = $item.next(),appendhtml;$pardiv.empty();
                    $appendhtml=$("<input data-xtype='upload'  data-appendto='div[name="+uploadTime+"]' type='file' name='"+label+"-upload'  style='width:255px;' data-button-text='请上传文件'/><div name='"+uploadTime+"'></div>");
                    $pardiv.append($appendhtml);
                    var uploaditem=$pardiv.find("input[data-xtype=upload]").eq(0);
                    if(isread === "1" && isreadonly === "2"){//显示
                        officeuploadFile(uploaditem,{render:_fileRender},true);
                    }else if((isread === "1" && isreadonly === "1")){//只读
                        officeuploadFile(uploaditem,{render:_fileRender},false,function (uploaditem) {
                            uploaditem.data().onlyView(true);
                        })
                    }
                    break;
                }
                case "uploadactivex":{//普通文件上传控件
                    $item.hide();
                    $("<div class='uploadactivex-div' name='uploadactivex-"+label+"-div'></div>").insertAfter($item);
                    var $pardiv = $item.next(),appendhtml,uploaditem;$pardiv.empty();
                    appendhtml="<div><input data-xtype='upload' data-appendto='div[name="+uploadTime+"]' type='file' name='"+label+"-upload'  style='width:255px;' data-button-text='请上传文件'/><div name='"+uploadTime+"'></div></div>";
                    $pardiv.append(appendhtml);
                    uploaditem=$pardiv.find("input[data-xtype=upload]").eq(0);
                    if(isread === "1" && isreadonly === "2") {//显示
                        uploadFile(uploaditem);
                    }else if((isread === "1" && isreadonly === "1")) {//只读
                        uploadFile(uploaditem, function (uploaditem) {
                            uploaditem.data().onlyView(true);
                        });
                    }
                    break;
                }
                case "countersign":{//会签意见
                    $item.hide();
                    $("<div class='countersign-div' name='countersign-"+label+"-div'></div>").insertAfter($item);
                    var $pardiv = $item.next(),appendhtml;$pardiv.empty();
                    if(isread === "1" && isreadonly === "2") {//显示
                        appendhtml="<div><input type='hidden' name='countersign'/><input style='width: 95.5%;border: none;' type='text' name='set-countersign'/><span name='set-name' style='display: inline-block;size: 14px;margin-left: 10px;'></span><span name='time' style='display: inline-block;size: 14px;margin-left: 10px;'></span></div>";
                    }else if((isread === "1" && isreadonly === "1")){//只读
                        appendhtml="<div><input type='hidden' name='countersign'/><input readonly='readonly' style='background: #ffffff;width: 95.5%;border: none;' type='text' name='set-countersign'/><span name='set-name' style='display: inline-block;size: 14px;margin-left: 10px;'></span><span name='time' style='display: inline-block;size: 14px;margin-left: 10px;'></span></div>";
                    }
                    $pardiv.append(appendhtml);
                    break;
                }
                case "filenumber":{//文号标签
                    $item.hide();
                    $("<div class='filenumber-div' name='filenumber-"+label+"-div'></div>").insertAfter($item);
                    var $filenumdiv = $item.next(),appendfilehtml,fileparam=$item.attr("data-datacode");
                    $filenumdiv.empty();
                    if(isread === "1" && isreadonly === "2"){//显示
                        appendfilehtml="<input type='text' style='width:92%;' name='filenumber-"+label+"'/>";
                    }else if((isread === "1" && isreadonly === "1")){//只读
                        appendfilehtml="<input disabled='disabled' style='width:92%;background:#FFFFFF' type='text' name='filenumber-"+label+"'/>";
                    }
                    $filenumdiv.append(appendfilehtml);
                    fileNumAutoCompete(fileparam,$filenumdiv.find("input").eq(0));
                    break;
                }
                case "uploadread":{//收文阅件
                    $item.hide();
                    $("<div class='uploadread-div' name='uploadread-"+label+"-div'></div>").insertAfter($item);
                    var $pardiv = $item.next(),appendhtml;$pardiv.empty();
                    $appendhtml=$("<input data-xtype='upload'  data-appendto='div[name="+uploadTime+"]' type='file' name='"+label+"-upload'  style='width:255px;' data-button-text='请上传文件'/><div class='saveuploadMessDiv' name='"+uploadTime+"'></div>");
                    $pardiv.append($appendhtml);
                    var uploaditem=$pardiv.find("input[data-xtype=upload]").eq(0);
                    if(isread === "1" && isreadonly === "2"){//显示
                        uploadread(uploaditem,true);
                    }else if((isread === "1" && isreadonly === "1")){//只读
                        uploadread(uploaditem,false,function(uploaditem){
                        	uploaditem.data().onlyView(true);
                        });
                    }
                    break;
                }
                case "fileuploadcountersign"://附件意见会签
                {
                    $item.hide();
                    $("<div class='fileuploadcountersign-div' name='fileuploadcountersign-"+label+"-div'></div>").insertAfter($item);
                    var $pardiv = $item.next(),appendhtml,uploaditem,
                        uploadPlus= "<div name='uploadactivex' style='margin-left: 10px;'><input data-xtype='upload' data-appendto='div[name="+uploadTime+"]' type='file' name='"+label+"-upload'  style='width:255px;' data-button-text='请上传文件'/><div name='"+uploadTime+"'></div></div>";
                    $pardiv.empty();
                    appendhtml="<input style='width: 98%;border: none;' type='text' name='countersigncontent'/>"+uploadPlus+"<div class='fileuploadPsersonMess'></div>";//保存人员信息
                    $pardiv.append(appendhtml);
                    uploaditem=$pardiv.find("div[name=uploadactivex] input[type=file][data-xtype=upload]").eq(0);
                    console.log(uploaditem);
                    uploadFile(uploaditem);
                    if((isread === "1" && isreadonly === "1")){//只读
                        uploaditem.data().onlyView(true);
                        $pardiv.find("input[name=countersigncontent]").css({background: "#ffffff"}).attr({readonly:"readonly"});
                    }
                    break;
                }
                default: {
                    break;
                }
            }
        }
    }
    if(typeof deferred !="undefined"){
        deferred.resolve();
    }
};


/**
 * 生成 收文阅件(uploadread) 的方法
 * @param $titem 在js中动态的添加type=file的控件
 * @param opt 要扩展的方法
 */
var uploadread= function ($titem,flag,callback) {
	var username = "";
	var url = "",idarr=[];
	var getNameUrl=projectName+"/workFlow/FileManager/CommonForm.do?command=getUserNameAndDept";
	$.ajax({  
         type : "post",  
         url : getNameUrl,  
         data : "",  
         async : false,  
         success : function(data){  
        	var josndata = $.parseJSON(data);
     		if (!$.isEmptyObject(josndata)) {
     			username = josndata.name;
     			url = projectName+"/platform/accessory_.do?command=upload&userId="+username;
     		} 
         }  
    });
    $titem.uploadFile($.extend({
        url: url,
        removeUrl: projectName+"/platform/accessory_.do?command=remove",
        dowloadUrl: projectName+"/platform/accessory_.do?command=download&id={id}",
        returnType: "JSON",
        showDone: false,
        onSuccess: function (files, response, xhr, pd) {
            var $parentNode=$titem.parent().parent().find("div[class=saveuploadMessDiv]").eq(0),
                $nextTable=$parentNode.find("table tbody tr");
            if($nextTable && $nextTable.length && typeof $nextTable[0] !="undefined"){
                for(var i= 0,len=$nextTable.length;i<len;i++){
                    var item=$nextTable[i],$itme=$(item);
                    idarr.push($itme.attr("id"));
                }
            }
            function getuploadfileid(response) {
                if(response && !$.isEmptyObject(response)){
                    idarr.push(response[0].id);
                }
            }
            getuploadfileid(response);
            setUploadDataInToTable($parentNode,idarr,flag);
        },
        showStatusAfterSuccess: false
    }));
    if(typeof callback !=='undefined' ){
        callback($titem);
    }
};

var setUploadactivex =function($item){
	$item.hide();
	var label = $item.attr("name") || $item.attr("id"),code=$item.attr("data-code"),
		isread = $item.attr("data-isread"), isreadonly = $item.attr("data-isreadonly"),
	    date=new Date(),uploadTime="fileListTD"+date.getTime();
	 $("<div class='uploadactivex-div' name='uploadactivex-"+label+"-div'></div>").insertAfter($item);
     var $pardiv = $item.next(),appendhtml,uploaditem;$pardiv.empty();
     appendhtml="<div><input data-xtype='upload' data-appendto='div[name="+uploadTime+"]' type='file' name='"+label+"-upload'  style='width:255px;' data-button-text='请上传文件'/><div name='"+uploadTime+"'></div></div>";
     $pardiv.append(appendhtml);
     uploaditem=$pardiv.find("input[data-xtype=upload]").eq(0);
     /*uploadFile(uploaditem);*/
    if((isread === "1" && isreadonly === "1")) {//只读
         uploadFile(uploaditem,"-1", function (uploaditem) {
             uploaditem.data().onlyView(true);
         });
     }else{
    	 uploadFile(uploaditem,code);
     }
     
};


var selectZW = function (id){
	$("#"+id).removeClass("whiteZW"); 
	$("#"+id).addClass("selectZW"); 
};
var removeZW = function (id){
	$("#"+id).removeClass("selectZW");
	$("#"+id).addClass("whiteZW"); 
};
var inRed = function (fileName,fileId,replaceData){
	var inRedUrl = projectName+"/workFlow/FileManager/RedTemplateList.do?command=inRed";
	$.post(inRedUrl,{templatefileName:fileName,fileId:fileId,replaceData:replaceData},function(data){
		if ($.isNotBlank(data)) {
            var jsondata = $.parseJSON(data);
	        if(!$.isEmptyObject(jsondata)){
	        	editfile(jsondata.fileId);
	        	$("#dialog-inRed").dialog( "close" ) ;
	        	$("#list2").empty();
	        }
		}
	});
};

//remove图标的方法
var removeuploadFiles= function (node,id) {
    $(node).closest("div[class=officeupload-div]").find("div").eq(0).show();
};
//编辑文件
var editfile= function (id) {
    loadFileById(id, function (data) {
        if(!$.isEmptyObject(data)){
            var filerealpath=data.filePathInServer,operator=data.username,
                url=projectName+"/pages/workFlow/FileManager/officeupload/editfile.jsp?path="+filerealpath+"&operator="+operator+"&id="+data.id;
            lookflowChart(url,"编辑文件");
        }
    });
};
var downloadfile= function (href) {
    window.location.href=href;
};


/**
 * 由文件的数组id加载所有文件信息
 * @param idsArr 数组id
 * @param callback 返回相信的文件信息数据
 */
var loadfilesByIdsArr=function(idsArr,callback){
    POST(projectName+"/platform/accessory_.do?command=loadFileByIdsArr",{id:idsArr},function(data){
        if(!$.isEmptyObject(data)){
            callback(data);
        }
    });

};

var lookflowChart = function (url,title) {
    frameDialog(url, title,
        {
            mode: "full",
            buttons: [
                { text: "返回", icons: { primary: "ui-icon-cancel" }, click: function (ev) {
                    var $this = window.top.$(this);
                    $this.dialog("close");
                }}
            ]
        });
};

/**
 * 这个方法是设置表单上所有元素为只读
 * @param $parent 表单元素对象
 */
var setFormIsOnlyRead= function ($parent) {
    var formElements = $parent.find("input[data-xtype]:not([type=button]):not([type=radio]),select[data-xtype],textarea[data-xtype],input[data-xtype][type=radio]:checked,input[data-xtype][type=checkbox]");
    for (var i = 0; i < formElements.length; i++){
        var item = formElements[i], $item = $(item),
            data = $item.data(),xtype = data.xtype;
        switch (xtype){
            case "advice" ://意见标签
            {
                var $pardiv=$item.next();
                $pardiv.find("button").hide();
                $pardiv.find("div").eq(0).find("textarea").attr({readonly:"readonly"}).css({background:"#FFFFFF"});
                break;
            }
            case "defaultadvice":{
                var $parentNode= $item.next();
                if($parentNode.length > 0){
                    $parentNode.find("input[name=set-defaultadvice]").attr({readonly:"readonly"}).css({background:"#FFFFFF"});
                }
                break;
            }
            case "sign"://签字标签
            {
                var  $pardiv=$item.next();
                var $span=$pardiv.find("span").eq(0);
                $span.removeAttr("onclick");
                $span.hide();
                break;
            }
            case "endorse"://签署意见
            {
                var $pardiv=$item.next();
                $pardiv.find("textarea").attr({readonly:"readonly"}).css({background:"#FFFFFF"});
                break;
            }
            case "fileadvice"://附件意见标签
            {
                var $pardiv=$item.next();
                if($pardiv.length >0){
                    $pardiv.find("textarea").attr({readonly:"readonly"}).css({background:"#FFFFFF"});
                    if( $pardiv.find("input[data-xtype]").data()){
                        $pardiv.find("input[data-xtype]").data().onlyView(true);
                    }
                }
                break;
            }
            case  "uploadactivex":{//附件上传的
                var $pardiv=$item.next();
                if($pardiv.length > 0){
                    if( $pardiv.find("input[data-xtype]").data()){
                        $pardiv.find("input[data-xtype]").data().onlyView(true);
                    }
                }
                break;
            }
            case "uploadread":{//内部签报拟稿
                var $pardiv=$item.next(),$button=$pardiv.find("div[name=fileListTD] table tbody tr");
                $pardiv.find("input[data-xtype=upload]").eq(0).prev().hide();
                break;
            }
            case "datetime":{
                $item.attr({"disabled":"disabled"}).css({background:"#FFFFFF"});
                break;
            }
            case "filenumber":{
                $item.next().find("input[type=text]").attr({disabled:"disabled"}).css({background:"#FFFFFF"});
                break;
            }
            case "mychosen":{
                $item.attr({disabled:"disabled"});
                break;
            }
            case "defaultsign":{
                $item.attr({disabled:"disabled"});
                break;
            }
            case "officeupload":{
                $item.next().find("div[name=fileListTD]").find("#MainBody-upload_list").addClass("officeuploadreadonly");
                break;
            }
            case "fileuploadcountersign":{
                var $parentNode= $item.next();
                if($parentNode.length > 0){
                    $parentNode.find("input[name=countersigncontent]").attr({readonly:"readonly"}).css({background:"#FFFFFF"});
                    $parentNode.find("div[name=uploadactivex] input[data-xtype=upload]").data().onlyView(true);
                }
                break;
            }
            case "countersign":{
                var $parentNode= $item.next();
                if($parentNode.length > 0){
                    $parentNode.find("input[name=set-countersign]").attr({readonly:"readonly"}).css({background:"#FFFFFF"});
                }
                break;
            }
            default :
            {
                $item.attr({"readonly": "readonly"}).css({background:"#FFFFFF"});
                break;
            }
        }
    }
};

/**
 *
 * @param $titem
 * @param opt
 */
var officeuploadFile=function($titem,opt,flag,callback){
	var username = "",url ="";
	var getNameUrl=projectName+"/workFlow/FileManager/CommonForm.do?command=getUserNameAndDept";
	$.ajax({  
         type : "post",  
         url : getNameUrl,  
         data : "",  
         async : false,  
         success : function(data){  
        	var josndata = $.parseJSON(data);
     		if (!$.isEmptyObject(josndata)) {
     			username = josndata.name;
     			url = projectName+"/platform/accessory_.do?command=upload&userId="+username;
     		} 
         }  
    });
	    $titem.uploadFile($.extend({
	        url: url,
	        removeUrl: projectName+"/platform/accessory_.do?command=remove",
	        dowloadUrl: projectName+"/platform/accessory_.do?command=download&id={id}",
	        returnType: "JSON",
	        showDone: false,
	        allowedTypes:"doc,docx",
	        extErrorStr:"您只能上传doc和docx格式的文件",
	        maxFileCountErrorStr:"您只能上传一份文件",
	        maxFileCount:1,
	        flag:flag,
	        onSuccess: function (files, response, xhr, pd) {
	            $titem.prev().hide();
	        },
	        showStatusAfterSuccess: false
	    },opt));
        if(typeof callback !=='undefined' ){
            callback($titem);
        }
    
};
/**
 * 封装upload方法
 * @param $titem <input type='file'>的控件
 * @param callback 当本方法执行完要执行的函数
 */
var uploadFile= function ($titem,callback) {
	var username = "";
	var url = "";
	var getNameUrl=projectName+"/workFlow/FileManager/CommonForm.do?command=getUserNameAndDept";
	$.ajax({  
         type : "post",  
         url : getNameUrl,  
         data : "",  
         async : false,  
         success : function(data){  
        	var josndata = $.parseJSON(data);
     		if (!$.isEmptyObject(josndata)) {
     			username = josndata.name;
     			url = projectName+"/platform/accessory_.do?command=upload&userId="+username;
     		} 
         }  
    });
		 $titem.uploadFile({
             url: url,
             removeUrl: projectName+"/platform/accessory_.do?command=remove",
             dowloadUrl: projectName+"/platform/accessory_.do?command=download&id={id}",
             returnType: "JSON",
             showDone: false,
             showStatusAfterSuccess: false
         });
         if(typeof callback !=='undefined' ){
             callback($titem);
         }
};
var getNowUserName = function($parentNode,paramkey){
	var getNameUrl=projectName+"/workFlow/FileManager/CommonForm.do?command=getUserNameAndDept";
	$.post(getNameUrl,function(data){
		var josndata = $.parseJSON(data);
		if (!$.isEmptyObject(josndata)) {
            var username = josndata.name,
            	 userId = josndata.personId,
            	 deptId = josndata.deptId,
                    dept = josndata.dept;
            if(paramkey==="user"){
            	$parentNode.html(username);
            	$parentNode.parent().find("input[type=hidden]").val(userId);
            	$parentNode.next().hide();
        	}else{
        		$parentNode.html(dept);
        		$parentNode.parent().find("input[type=hidden]").val(deptId);
        		$parentNode.next().hide();
        	}
        }
	});
};
/**
 * 给data-xtype='sign' 加点击事件
 * @param node span节点
 */
var setNameAndDate= function (node) {
    var getNameUrl=projectName+"/workFlow/FileManager/FormPowerSetting.do?command=loadUserMessage";
    $.getJSON(getNameUrl,null, function (data) {
        var $node=$(node),showname=$node.next(),savenowtime=$node.find("input[name=savenowtime]"),
            showdate=$node.next().next();
        var nowdate=new Date();
        showdate.text(dateFormat(nowdate,'yyyy-MM-dd'));
        showname.text(data.chineseName);
        savenowtime.val(dateFormat(nowdate,'yyyy-MM-dd hh:mm:ss'));
        $node.hide();
    });
};
var namesD = [];
var addOption = function ($parentNode, datacode,paramkey,paramid) {
    var addoptionurl = projectName+"/workFlow/FileManager/CommonForm.do?command=loadSelect";
    $.getJSON(addoptionurl, {code: datacode}, function (data) {
        addNode2ParentNode($parentNode, data,paramkey,paramid);
    });
};
var addClick = function($parentNode,paramkey,paramid){
	$parentNode.attr({tempflag: paramkey});
	$parentNode.click(function(ev){
		var tempFlag = $(this).attr("tempflag");//弹出框要显示的类型，是人员还是部门
        if (tempFlag != null && tempFlag != "") {
            var $selparentDiv = $(this).closest("div"),
                $treediv = $selparentDiv.find("div[name=showtree]"),//得到ztree需要的div
                $saveIds = $selparentDiv.find("input[type=hidden]"),//得到点中的保存在页面上的所有id
                $saveNames = $selparentDiv.find("textarea");//得到点中的保存在页面上的所有name值
            if(($saveIds != null && $saveIds != "")&&($saveNames != null && $saveNames != "")){
            	clickopenDialog(tempFlag,paramid, $treediv, $saveIds, $saveNames);
            }else{
            	$saveIds.val("");
                $saveNames.val("");
            }
                
        }
	});
	
};
var initTab1 = function($tabs,paramid,paramkey){
	var setting = {
		    isSimpleData : true, 
			treeNodeKey : "id", 
			treeNodeParentKey : "pId", 
			showLine : true, 
			checkable : true,
			check: {
				enable: true
			},
			callback: {
				enable: true,
				onCheck: zTreeOnCheck
			}
		};
	$.ajax({
        url:  projectName+"/common/choosePerson.do?command=selectAllDatas&moduleType=organization&orgId="+paramid+"&orgName=",
        type: "POST",
        data: {
            
        },
        dataType: "JSON",
        success: function(data, xhr) {
            if (data.error) {
                 window.Msg.error(data.error,"加载发生错误！");
                return;
            }
            var treeDemo = $tabs.find("#treeDemo");
            tree1 = $.fn.zTree.init($(treeDemo), setting,eval(data));
        }
    });
};
var setClick = function($sel,paramid,paramkey){
	var $item = $sel.parent();
	$("#tabs").dialog({
	    autoOpen: false,
	    width: 230,
	    modal: true,
	    buttons: {
	      "确定": function() {
	      	$("#tabs").dialog( "close" );
	      	var selectText = $("#tabs").find("option:selected").text(),
	      		selectVal = $("#tabs").find("select[id=sel-advice]").val(),
	      		selName = namesD;
	      	if(selectVal == 3){
	      		var strarr = selectText.split("#"),
	      			begintext = strarr[0],
	      			enttext = strarr[2];
	      			$item.find("input[name=set-defaultadvice]").val(begintext + selName.join("、") + "同志" + enttext);
	      		
	      	}else{
	      		$item.find("input[name=set-defaultadvice]").val(selectText);
	      	}
	      	
	      },
	      "取消": function() {
	      	$("#tabs").dialog( "close" );
	      }
	    }
	  });
	$sel.click(function(ev){
    	 var This = this;
    	 $("#tabs").dialog("open");
    	 var $tabs = $("#tabs"),
    	 	 $selectAdv = $tabs.find("select[id=sel-advice]");
    	 changeSelect($selectAdv,$tabs,$(This).parent(),paramid,paramkey);
	 });
};
var changeSelect = function($parentNode,$tabs,$pardiv,paramid,paramkey){
	$parentNode.change(function(ev){
		var selectVal = $(this).val();
		if(selectVal == 3 ){
			$tabs.find("div[id=tabs-1]").show();
			initTab1($tabs,paramid,paramkey);
		}else{
			$tabs.find("div[id=tabs-1]").hide();
		}
	});
	 
};
var zTreeOnCheck = function(event,treeId,treeNode) {
	var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
    var nodes = treeObj.getCheckedNodes(true);
    namesD=[];
    for(var i=0;i<nodes.length;i++){
    	var isParent = nodes[i].isParent;
    	if(isParent == false) {
            var itemName = nodes[i].name.replace(/\s/g, "");
            if (itemName.length == 3) {
            	namesD.push(itemName.substring(1, 3));
            } else if (itemName.length == 4) {
            	namesD.push(itemName.substring(2, 4));
            } else {
            	namesD.push(itemName);
            }
    	}
    }
};
var addNode2ParentNode = function ($parentNode, jsondata,paramkey,paramid) {
    if (!$.isEmptyObject(jsondata)) {
        $parentNode.empty();
        var htmlArr = [];
        htmlArr.push("<option value=''>请选择</option>");
        for (var i = 0; i < jsondata.length; i++) {
            var val = jsondata[i], html = "<option value=" + val.id + " id=" + val.id + ">" + val.label + "</option>";
            htmlArr.push(html);
        }
        $parentNode.append(htmlArr.join(""));
        $parentNode.attr({tempflag: paramkey});
        $parentNode.change(function (ev) {
            var tempFlag = $(this).attr("tempflag");//弹出框要显示的类型，是人员还是部门
            if (tempFlag != null && tempFlag != "") {
                var $selparentDiv = $(this).closest("div"),
                    selectval = $(this).val(),//下拉列表选中的值
                    selectTempText = $(this).find("option:selected").text(),//得选中的下拉列表的文本值
                    $treediv = $selparentDiv.find("div[name=showtree]"),//得到ztree需要的div
                    $saveIds = $selparentDiv.find("input[type=hidden]"),//得到点中的保存在页面上的所有id
                    $saveNames = $selparentDiv.find("textarea");//得到点中的保存在页面上的所有name值
                if (selectval != null && selectval != "") {
                    changeopenDialog(tempFlag,paramid, $treediv, $saveIds, $saveNames, selectTempText);
                } else {
                    $saveIds.val("");
                    $saveNames.val("");
//                    changeopenDialog(tempFlag,$treediv,$saveIds,$saveNames,selectTempText);
                }
            }
        });
    }
};
//给按钮添加click事件
var clickopenDialog = function(tempFlag,paramid, $treediv, $saveIds, $saveNames){
	 var getSelectTreeurl = projectName+"/common/choosePerson.do?command=selectAllDatas", sendurl;
	    if (tempFlag === "user") {//显示人和部门
	        sendurl = getSelectTreeurl + "&moduleType=organization&orgId="+paramid+"&orgName=";
	    } else {//org
	        sendurl = getSelectTreeurl + "&moduleType=meetingApplyReturnList&orgId="+paramid+"&orgName=";
	    }

	    $.post(sendurl,{}, function (data) {
	        if (data != null && data != "") {
	            var json = $.parseJSON(data);
	            var setting = {
	                isSimpleData: true, //数据是否采用简单 Array 格式，默认false
	                treeNodeKey: "id", //在isSimpleData格式下，当前节点id属性
	                treeNodeParentKey: "pId", //在isSimpleData格式下，当前节点的父节点id属性
	                showLine: true, //是否显示节点间的连线
	                checkable: true, //每个节点上是否显示 CheckBox
	                check: {
	                    enable: true
	                }
	            };
	            $.fn.zTree.init($treediv.find("ul[name=ztreeul]"), setting, json);
	            clicksettreedialog(tempFlag,paramid, $saveIds, $saveNames);
	        }
	    });
};

//给表单的select下拉列表加change事件
var changeopenDialog = function (tempFlag,paramid, $treediv, $saveIds, $saveNames, selectTempText) {
    var getSelectTreeurl = projectName+"/common/choosePerson.do?command=selectAllDatas", sendurl;
    if (tempFlag === "user") {//显示人和部门
        sendurl = getSelectTreeurl + "&moduleType=organization&orgId="+paramid+"&orgName=''";
    } else {//org
        sendurl = getSelectTreeurl + "&moduleType=meetingApplyReturnList&orgId="+paramid+"&orgName=";
    }

    $.post(sendurl,{}, function (data) {
        if (data != null && data != "") {
            var json = $.parseJSON(data);
            var setting = {
                isSimpleData: true, //数据是否采用简单 Array 格式，默认false
                treeNodeKey: "id", //在isSimpleData格式下，当前节点id属性
                treeNodeParentKey: "pId", //在isSimpleData格式下，当前节点的父节点id属性
                showLine: true, //是否显示节点间的连线
                checkable: true, //每个节点上是否显示 CheckBox
                check: {
                    enable: true
                }
            };
            $.fn.zTree.init($treediv.find("ul[name=ztreeul]"), setting, json);
            settreedialog(tempFlag,paramid, $saveIds, $saveNames, selectTempText);
        }
    });
};

/*
 meetingApplyReturnList  代表 部门
 organization  人

 */

var clicksettreedialog = function (tempflag,paramid, $saveIds, $saveNames){
	var ids = $saveIds.val(), url;
    if (tempflag === "user") { //user
    	if (ids != null && ids != "") {
    		url="common/choosePerson.do?command=init&orgId="+paramid+"&idsParam="+ids+"&moduleType=organization&orgName=''";
        } else {
            url=projectName+"/common/choosePerson.do?command=init&orgId="+paramid+"&moduleType=organization&orgName=''";
        }
    } else {//org
        if (ids != null && ids != "") {
            url = projectName+"/common/choosePerson.do?command=init&idsParam=" + ids + "&moduleType=meetingApplyReturnList&orgName=dfg";
        } else {
            url = projectName+"/common/choosePerson.do?command=init" + "&moduleType=meetingApplyReturnList&orgName=aaa";
        }
    }
    frameDialog(url, "请选择", {mode: "middle", width: 227, height: 340,
        buttons: [
            { text: "确定", icons: { primary: "ui-icon-check" }, click: function (ev) {
                var $this = window.top.$(this),
                    dial = $this.find("iframe")[0].contentWindow;
                var selectData=dial.getData();
                if(selectData){
                	var nameidarr = selectData.split(";"), names = nameidarr[0], ids = nameidarr[1],
                    namesarr = names.split(","), idsarr = ids.split(","), alldata = [];
                	for (var i = 0; i < namesarr.length; i++) {
                		alldata.push({
                			id: idsarr[i],
                			name: namesarr[i]
                		});
                	}

                	if (alldata.length > 0) {
                		var idArr = [], nameArr = [];
                		for (var i = 0; i < alldata.length; i++) {//给advice控件编辑框中加数据
                			var item = alldata[i],
                				itemName = item.name.replace(/\s/g, "");
                			idArr.push(item.id);
                			nameArr.push(itemName);
                			
                		}
                		$saveIds.val(idArr.join(","));
                		$saveNames.val(nameArr.join("、"));
                		
                	} else {
                		window.Msg.alert("请选择要通知的人!");
                		return;
                	}
                }
                $this.dialog("close");
            }},
            { text: "返回", icons: { primary: "ui-icon-cancel" }, click: function (ev) {
                var $this = window.top.$(this);
                $this.dialog("close");
            }}
        ]});
};
var settreedialog = function (tempflag,paramid, $saveIds, $saveNames, selectTempText) {
    var ids = $saveIds.val(), url, begintext, enttext;
    if (tempflag === "user") {
        //  sendurl= projectName+"/common/choosePerson.do?command=init&orgId="+paramid+"&orgName=''&moduleType=organization";
        if (ids != null && ids != "") {

//            url = projectName+"/common/choosePerson.do?command=init&idsParam=" + ids + "&moduleType=organization&orgName=''";
            url="common/choosePerson.do?command=init&orgId="+paramid+"&idsParam="+ids+"&moduleType=organization&orgName=''";

        } else {

            //url = projectName+"/common/choosePerson.do?command=init" + "&moduleType=organization&orgName=''";
            url=projectName+"/common/choosePerson.do?command=init&orgId="+paramid+"&moduleType=organization&orgName=''";

        }
    } else {//org
        if (ids != null && ids != "") {
            url = projectName+"/common/choosePerson.do?command=init&idsParam=" + ids + "&moduleType=meetingApplyReturnList&orgName=dfg";
        } else {
            url = projectName+"/common/choosePerson.do?command=init" + "&moduleType=meetingApplyReturnList&orgName=aaa";
        }
    }
    if (selectTempText != null && selectTempText !== "" && selectTempText != "请选择") {

        var strarr = selectTempText.split("#");
        begintext = strarr[0];
        enttext = strarr[2];
    }
    frameDialog(url, "请选择", {mode: "middle", width: 227, height: 340,
        buttons: [
            { text: "确定", icons: { primary: "ui-icon-check" }, click: function (ev) {
                var $this = window.top.$(this),
                    dial = $this.find("iframe")[0].contentWindow;
                var selectData=dial.getData();
                if(selectData){
                	var nameidarr = selectData.split(";"), names = nameidarr[0], ids = nameidarr[1],
                    namesarr = names.split(","), idsarr = ids.split(","), alldata = [];
                	for (var i = 0; i < namesarr.length; i++) {
                		alldata.push({
                			id: idsarr[i],
                			name: namesarr[i]
                		});
                	}

                	if (alldata.length > 0) {
                		var idArr = [], nameArr = [];
                		for (var i = 0; i < alldata.length; i++) {//给advice控件编辑框中加数据
                			var item = alldata[i],
                				itemName = item.name.replace(/\s/g, "");
                			idArr.push(item.id);
                			nameArr.push(itemName);
                			
                		}
                		$saveIds.val(idArr.join(","));
                		if ($.trim(paramid) == '00EB41BF-EB1D-46F9-837E-8F73714407BE') {
                			$saveNames.val(begintext + nameArr.join("、") + "同志" + enttext);
                		} else {
                			$saveNames.val(begintext + nameArr.join("、") + enttext);
                		}
                	} else {
                		window.Msg.alert("请选择要通知的人!");
                		return;
                	}
                }
                $this.dialog("close");
            }},
            { text: "返回", icons: { primary: "ui-icon-cancel" }, click: function (ev) {
                var $this = window.top.$(this);
                $this.dialog("close");
            }}
        ]});
};


/**
 * 提供一个方法 若规定 data-xtype='advice' 的时候 要把整个表单的意见标签所有数据封装成一个数组
 *  若规定了 data-xtype='advice' 又给了一个 data-datacode='YJMB_BM' 的时候把满足条件的表单的意见标签所有数据封装成一个数组
 * */

var getAdviceData = function ($form, givedatacode) {
    var formElements = $form.find("input[data-xtype]:not([type=button]):not([type=radio]),select[data-xtype],textarea[data-xtype],input[data-xtype][type=radio]:checked,input[data-xtype][type=checkbox]"),
        objArr = [];
    for (var i = 0; i < formElements.length; i++) {
        var item = formElements[i], $item = $(item), $itemdiv = $item.next("div"),
            data = $item.data(), dtype = data.dtype, xtype = data.xtype, datacode = data.datacode,
            label = $item.attr("name") || $item.attr("id"), obj = {};
        if ($.trim(xtype).toLowerCase() === "advice") {
            var val = $item.val(),//advice 文本的值。
                advicetype = $itemdiv.find("select").attr("tempflag"),//是人员意思还是部门意见 1，是人员 。 2是部门
                selval = $itemdiv.find("select").val(),//表单控件生成的select选中的value值
                checkedids = $itemdiv.find("input[name=savecheckedids]").val(),//弹出框选中的人或者部门的所有ids
                checkednames = $itemdiv.find("textarea[name=savecheckedname]").val();//弹出框选中的内容，即textarea中出现的文字
            if (typeof givedatacode !== "undefined") {
                if ($.trim(givedatacode).toLowerCase() === $.trim(datacode).toLowerCase()) {
                    obj = {
                        adviceid: val,
                        advicenameattr: label,
                        advicetype: advicetype === "person" ? "1" : "2",//1 是人员 2 是部门
                        advicetemptid: selval,
                        advicecheckedids: checkedids,
                        adviceContext: checkednames
                    };
                    objArr.push(obj);
                }
            } else {
                obj = {
                    adviceid: val,
                    advicenameattr: label,
                    advicetype: advicetype === "person" ? "1" : "2",//1 是人员 2 是部门
                    advicetemptid: selval,
                    advicecheckedids: checkedids,
                    adviceContext: checkednames
                };
                objArr.push(obj);
            }
        }
    }
    return objArr;
};
var getItemByName=function($parentNode,nodeName){
	//console.log(window.parent.document.getElementById("contentFrame").contentWindow.document);
       var alldata= $($parentNode).getDtypeFormData(),obj;
        if(alldata.length > 0 ){
            for(var i=0;i<alldata.length ; i++){
                var item=alldata[i],name=item.name;
                if(name==nodeName){
                    obj=item;
                    break;
                }
            }
        }
    return obj;
};
var setAdvice = function($item){
	$item.hide();
    var $befdiv= $item.parent().children("span"),divWid = $befdiv.attr("width"),data = $item.data(), dtype = data.dtype,
    isread = $item.attr("data-isread"), isreadonly = $item.attr("data-isreadonly"),
    xtype = data.xtype, datacode = data.datacode,code = $item.attr("data-code"),
    label = $item.attr("name") || $item.attr("id");
    $("<div class='advice' name='sel-" + label + "-div'></div>").insertAfter($item);
    var $pardiv = $item.next("div"),param=$item.attr("param"), selstr, $sel,paramid,paramkey;
    if(param && param !=null && param !=""){
        var paramarr=param.split(":");
        paramkey=paramarr[0];
        paramid=paramarr[1];
    }else{
        paramkey=null;
        paramid=null;
    }
    $pardiv.empty();
    //新advice控件
    if (isread === "1" && isreadonly === "2") {
    	$html = "<div class='advice-div'><input type='hidden' name='ids-" + label + "'><textarea readonly='readonly' name='" + label + "' style='width: 150px; height: 72%;'></textarea><button name='sel-" + label + "' type='button' class='ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only' style='width:40px;height:30px;margin-top:-25px;margin-left:5px;' value='选择'>选择</button></div>" +
            "<div style='display: none;'> <div name='showtree' style='overflow-x: hidden;white-space:normal'><div class='cenbox_tree' align='center' style='float:left'> <div class='zTreeDemoBackground left'> " +
            "<ul name='ztreeul' class='ztree'></ul></div></div></div> </div>";
        if(code === "YES"){
        	var $defaText = $("textarea[name=" + label + "]");
        	getNowUserName($defaText,paramkey);
        }
    } else if ((isread === "1" && isreadonly === "1")) {
    	$html = "<div class='advice-div'><input type='hidden' name='ids-" + label + "'><textarea readonly='readonly' style='background:#ffffff !important;line-height:28px;width: 150px; height: 72%;' name='" + label + "'></textarea><button name='sel-" + label + "' type='button' style='display:none;'></button></div>" +
            "<div style='display: none;'> <div name='showtree' style='overflow-x: hidden;white-space:normal'><div class='cenbox_tree' align='center' style='float:left'> <div class='zTreeDemoBackground left'> " +
            "<ul name='ztreeul' class='ztree'></ul></div></div></div> </div>";
    }
    	$pardiv.append($html);
    //新advice触发事件
      selstr = $($pardiv).find("button[name=sel-" + label + "]");
      $sel = $(selstr);
      addClick($sel,paramkey,paramid);//给按钮添加点击事件
}; 
/**
 *给endorse 即意见标签加默认值
 * @param $parNote 意见标签
 * @param content 默认内容
 */
var _endorse_Text=function($parNote,content){
    var xtype=$parNote.attr("data-xtype"),
        show=$parNote.attr("data-isread"),
        read=$parNote.attr("data-isreadonly");
    if(xtype==="endorse" && show==='1' && read==='2'){//显示的时候
        $parNote.next().find(".endorse-textarea").val(content);
    }
};
var _defaultadvice_Text = function($parNote,content){
    var xtype=$parNote.attr("data-xtype"),
    show=$parNote.attr("data-isread"),
    read=$parNote.attr("data-isreadonly");
    if(xtype==="defaultadvice" && show==='1' && read==='2'){//显示的时候
    	$parNote.next().find("input[name=set-defaultadvice]").val(content);
    }
};
var _countersign_Text=function($parNote,content){
    var xtype=$parNote.attr("data-xtype"),
        show=$parNote.attr("data-isread"),
        read=$parNote.attr("data-isreadonly");
    if(xtype==="countersign" && show==='1' && read==='2'){//显示的时候
        $parNote.next().find("input[name=set-countersign]").val(content);
    }
};




/**
 *   yuanyuan.zhang
 *   end
 */


/**
 * 初始化表单的验证(带xtype)
 * formQuery 表单
 * tobj 可以覆盖原先的验证配置
 */
var _initValidateForXTypeForm = function (formQuery, tobj) {
    var $queryForm = $(formQuery), r = {}, m = {};
    $queryForm.find("[data-validate]").each(function (index, item) {
        var $item = $(item), nOi = $item.attr("name") || $item.attr("id");
        if (nOi) {
            r[nOi] = new Function("return " + $item.data("validate") + ";")();
            if ($item.data("errormessage")) {
                m[nOi] = $item.data("errormessage");
            }
        }
    });
    var obj = $.extend({
        rules: r,
        messages: m
    }, tobj);
    $queryForm.validate(obj);
};

//$.metadata.setType("attr", "data-validate");

$("body").ready(function () {
    //$( document ).ajaxStart(function() {$( "body" ).mask();}).ajaxComplete(function(){$("body").unMask();}).ajaxError( function(ar1, ar2, ar3) {console.log(ar3);var eobj = {"text" : ar3 ? ar3.message || ar3 : "","title" : "发生错误"};/*window.message(eobj);*/});
    if (resizeFun) {
        $(window).resize(resizeFun);
    }
});

/*点击endter查询*/
$(document).keyup(function(event) {
	if (event.keyCode == 13) {
		$("#fastSearch").trigger("click");
	}
});

/*门户考试信息详情弹出框*/
var viewExamInfoInFrameDialog = function (id, examNumber, examName) {
	var examName = encodeURI(encodeURI(examName));
    var testCaseGeUrl = "examInfo/examInfoLook.do?command=init&id=" + id;
    frameDialog(testCaseGeUrl + "&examNumber=" + examNumber + "&examName=" + examName,
        "考试信息详情", {
            mode: "middle",
            resizable: false,
            width: 1060,
            height: 400,
            buttons: [{
                text: "关闭",
                icons: {
                    primary: "ui-icon-cancel"
                },
                click: function (ev) {
                    var $this = window.top.$(this);
                    $this.dialog("close");
                }
            }]
        });
};

var downloadExamPaper = function(id){
	location.href = encodeURI("../examInfo/examPaperUpload.do?command=download&id="+id);
};



//门户页面--最新几条申请调动任务
var loadTaskInfo=function (){
	  //最新任务
     $.ajax({
        url:"../transfer/studentTransfer.do?command=getStuAndTeacherTrans",
        type:"POST",
        data:{},
        dataType:"JSON",
        success : function(data) {
        $("#woderengwu").html("");
        var length = data.length;
        if(length > 0){     //有公告数据
          for (var i = 0; i < length; i++) {
             var name  = data[i].Name;   //名字
             var applyTime=data[i].Apply_Time;;    //申请时间
             var pkId = data[i].Pk_Id;   //待办任务id
             var proposer = data[i].Proposer; //申请人
             var direction = data[i].Direction;//去向
             var roleState = data[i].Role_State;
             var title=data[i].Title; //原属学校code
             var schoolCode=data[i].School_Code; //原属学校code
             
             var d = new Date(applyTime);
             var month = d.getMonth() + 1;
             if(month >= 10){
                month = month;
             }else{
                month = "0" + month;
             }
             var day = d.getDate();
             if(day >= 10){
                day =day;
             }else{
                day = "0" + day;
             }
             var publishdate = month + "-" + day;
             applyTime = d.getFullYear() + "-" + month + "-" + day;
             $("#woderengwu").append("<div><img class='imm' src='../theme/images/dot01.png' /><li class='titel'>"
                     + "<a class='Ttitl' title='"+ title +"' href='javascript:void(0)' onclick='submitTransfer(\""+pkId+"\",\""+roleState+"\",\""+schoolCode+"\",\""+name+"\",\""+proposer+"\",\""+applyTime+"\")'>" + title + "</a></li></div>"
                     + "<div class='Ttitel'><span class='dep'>申请人："+proposer+"</span><span class='Triqi'>" + publishdate + "</span></div>");
          }
         }else{
             $("#woderengwu").append("<label style='margin-left:10px;font:12px 微软雅黑;'>暂无数据</label>");
          }
       }
     }); 
}

