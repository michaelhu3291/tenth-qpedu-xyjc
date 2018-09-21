<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
<!--[if IE 7]>
         <link href="../theme/default/font.awesome.ie7.css" rel="stylesheet" />
         <link href="../theme/default/page.common.ie7.css" rel="stylesheet" />
    <![endif]-->
<style type="text/css">
#dialog_addLink ul li {
	list-style: none;
	margin-top: 30px;
	margin-left: 320px;
}
</style>
<script type="text/javascript">
    var listId = "#list2",
        editorFormId = "#editorForm",
        pagerId = "#pager2",
        loadUrl = "../link/linkNet.do?command=selectLinkById",
        deleteUrl = "../link/linkNet.do?command=delete",
        saveUrl = "../link/linkNet.do?command=addLink",
        listUrl = "../link/linkNet.do?command=searchPaging";

    $(function () {
        _initButtons({
            insertBTN: function (ev) {
                var $i = $(ev.currentTarget).find("i"),
                    $piel = $(
                        ".page-editor-panel").show({
                        effect: "slide",
                        direction: "up",
                        easing: 'easeInOutExpo',
                        duration: 900
                    }).find("h4 i").removeClass();
                if ($i.length) {
                    $piel.addClass($i.attr("class"));
                }
                window._EDITDATA = undefined;
                var $grid = $(listId),
                    idAry = $grid.jqGrid("getGridParam",
                        "selarrrow");
                if (idAry.length === 0) {
                    $(editorFormId).resetHasXTypeForm();
                } else {
                    var data = $grid.jqGrid("getRowData", idAry[0]);
                    $(editorFormId).resetHasXTypeForm({
                        "parentDictionary": [{
                            text: data.dictionaryName,
                            value: data.id
                        }]
                    });
                }
            },
            cancelBTN: function () {
	            hideSlidePanel(".page-editor-panel");
	        },
        }); //from page.common.js
        _initFormControls(); //from page.common.js
        _initValidateForXTypeForm(editorFormId);

        var types = {
            "bsxj": "本市县级教育网站",
            "bsjy": "本市教育网站",
            "cyjy": "常用教育资源类网站",
            "cywz": "常用网站",
            "gov": "政府相关网站"
        };
        var _colModel = [{
                name: 'Id',
                key: true,
                width: 60,
                hidden: true,
                search: false
            }, {
                name: 'Name',
                sortable: false,
                autoWidth: true,
                align: "left"
            }, {
                name: 'Url',
                sortable: false,
                autoWidth: true,
                align: "left"
            }, {
                name: 'Code',
                sortable: false,
                autoWidth: true,
                align: "left",
                formatter: function (ar1, ar2, ar3) {
                    return (types[ar3.Code] || "");
                }
            }],
            _colNames = ["", "名称", "地址", "类型"];


        $(listId).jqGrid($.extend(defaultGridOpts, {
            url: listUrl,
            colNames: _colNames,
            colModel: _colModel,
            pager: pagerId
        }));
        resizeFun();
        loadDictionary();//加载数据字典
        $("#insertBTN").click(function () {
            //清空文本框的值
            $("#link_name").val("");
            $("#link_address").val("");
            //清空报错信息值
            $(".link_ming").html("");
            $(".link_addr").html("");
        });
        //添加修改链接
        $("#edit").click(function () {
            var id = $(listId).jqGrid('getGridParam', 'selrow');
            //清空报错信息值
            $(".link_ming").html("");
            $(".link_addr").html("");
            var selectMenu = $("#selectMenu").val();
            var link_name = $("#link_name").val();
            var link_address = $("#link_address").val();
            var reg = /^(http:\/\/|https:\/\/)[a-zA-Z0-9_.\/]+$/;
            //校验文本框是否为空
            if (link_name == '' || link_name == null) {
                $(".link_ming").html("名称必须填写!");
                return;
            }
            if (link_address == '' || link_address == null) {
                $(".link_addr").html("地址必须填写!");
                return;
            }
            if (!reg.test(link_address)) {
                $(".link_addr").html("地址格式不正确!");
                return;
            }
            $.ajax({
                url: saveUrl,
                type: "POST",
                data: {
                    id: id,
                    selectMenu: selectMenu,
                    link_name: link_name,
                    link_address: link_address
                },
                dataType: "JSON",
                success: function (data, xhr) {
                    hideSlidePanel(".page-editor-panel");
                    $(listId).trigger("reloadGrid");
                }
            });
        });

        $("#link_name,#link_address").change(function () {
            if ($("#link_name").val != '') {
                $(".link_ming").html("");
            }
            if ($("#link_address").val != '') {
                $(".link_addr").html("");
            }
        });
    });
</script>
</head>
<body>
	<div class="page-list-panel">
		<div class="head-panel">
			<div class="toolbar" >
				<table style="height: 100%;" cellpadding="0" cellspacing="0"
					border="0">
					<tr>
						<td style="padding-left: 12px; padding-right: 24px;"><i
							class="fa fa-list-ul micon"></i></td>
						<td class="buttons">
							<button id="insertBTN">
								<i class="fa fa-plus"></i>添加
							</button>
							<button id="editBTN">
								<i class="fa fa-pencil"></i>修改
							</button>
							<button id="deleteBTN">
								<i class="fa fa-trash-o"></i>删除
							</button>
						</td>
						<td style="padding-left: 24px; padding-right: 5px;"><input
							id="fastQueryText" type="text" placeholder="输入链接名称"
							style="line-height: 1em; font-size: 1em; cursor: text;" /></td>
						<td>
							<button id="fastSearch" title="查询">
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
			<div class="btn-area">
				<div style="margin-top: 4px;">
					<button id="edit">
						<i class="fa fa-check"></i>保存
					</button>
					<button id="cancelBTN">
						<i class="fa fa-times"></i>取消
					</button>
				</div>
			</div>
		</div>
		<div class="page-content">
			<div class="page-inner-content">
				<form id="editorForm">
					<div id="dialog_addLink">
						<ul>
							<li>链接类型： <select id="selectMenu" name="selectMenu" data-dic="{code:'LJLX'}" class="form-control">
									<!--<option value="bsxj">本市县级教育网站</option>
									<option value="bsjy">本市教育网站</option>
									<option value="gov">政府相关网站</option>
									<option value="cyjy">常用教育资源类网站</option>
									<option value="cywz">常用网站</option>-->
							</select>
							</li>
							<li><label for="link_name"> 链接名称：<input type="text"
									class="form-control" placeholder="请输入链接名称" id="link_name"
									name="link_name" style="width:150px;"/>
							</label> <span class="link_ming" style="color: red; font-weight: bold;"></span>
							</li>
							<li><label for="link_address"> 链接地址：<input
									type="text" class="form-control" placeholder="请输入链接地址"
									id="link_address" name="link_address" style="width:150px;"/>
							</label> <span class="link_addr" style="color: red; font-weight: bold;"></span>
							</li>
						</ul>
					</div>
				</form>
			</div>
		</div>
	</div>
	<div class="page-view-panel full-drop-panel"></div>
</body>
</html>