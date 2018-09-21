/**
 * Created by zhangyuanyuan on 2014/12/18.
 */

var projectName = "";

//获取url参数值
function getUrlParam(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if (r != null)
		return unescape(r[2]);
	return null;
}

/**
 * 函数描述：获取input type=file的图像全路径
 * 
 * @obj input type=file的对象
 */
function getFullPath(obj) {
	if (obj) {
		// ie
		if (window.navigator.userAgent.indexOf("MSIE") >= 1) {
			obj.select();
			return document.selection.createRange().text;
		}
		// firefox
		else if (window.navigator.userAgent.indexOf("Firefox") >= 1) {
			if (obj.files) {
				return obj.files.item(0).getAsDataURL();
			}
			return obj.value;
		}
		return obj.value;
	}
}

function encodeParam(param) {
	return encodeURI(param).replace(/%/g, "Z");
}

function decodeParam(param) {
	if (getUrlParam(param) == null) {
		return "";
	} else {
		return decodeURI(getUrlParam(param).replace(/Z/g, "%"));
	}
}

function getFileName(path) {
	var pos1 = path.lastIndexOf('/');
	var pos2 = path.lastIndexOf('\\');
	var pos = Math.max(pos1, pos2)
	if (pos < 0)
		return path;
	else
		return path.substring(pos + 1);
}

function parseTime(timeStr)// 19900414
{
	timeStr = $.trim(timeStr);
	var time = {
		Y : timeStr.substr(0, 4),
		M : timeStr.substr(4, 2),
		D : timeStr.substr(6, 2),
		H : timeStr.substr(8, 2),
		m : timeStr.substr(10, 2),
		s : timeStr.substr(12, 2),
		S : timeStr.substr(14, 3)
	}
	return new Date(time.Y, time.M, time.D, time.H, time.m, time.s, time.S);
}

// 格式化日期方法
function dateFormat(date, fmt) { // author: meizz

	if (!(date instanceof Date)) {
		if (date == undefined || date == "" || $.trim(date) == "")
			return "";
		date = parseTime(date);
	}

	var o = {
		"M+" : date.getMonth(), // 月份
		"d+" : date.getDate(), // 日
		"h+" : date.getHours(), // 小时
		"m+" : date.getMinutes(), // 分
		"s+" : date.getSeconds(), // 秒
		"q+" : Math.floor((date.getMonth() + 3) / 3), // 季度
		"S" : date.getMilliseconds()
	// 毫秒
	};
	if (/(y+)/.test(fmt))
		fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
	for ( var k in o)
		if (new RegExp("(" + k + ")").test(fmt))
			fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
	return fmt;
}

function checknull(param) {
	param = $.trim(param);
	if (param == null || param === "" || param === 'null') {
		return null;
	} else {
		return param;
	}
}

var dic_seting = {
	val : "DIC_CODE",
	lable : "DIC_NAME"
};

/*将日期转换成年月日*/
function dateTime(examTimes){
	 var dateTimes=examTimes.substring(0, 10);
	 dateTimes=dateTimes.replace(/\-/g,"");
	  var   year =dateTimes.substring(0,4);
      var  month = dateTimes.substring(4,6);
      if(month.substring(0,1)==0){
    	  month=month.substring(1,2)
      }
      var day = dateTimes.substring(6,8);
      if(day.substring(0,1)==0){
    	  day=day.substring(1,2)
      }
      dateTimes=year+"年"+month+"月"+day+"日"
	 return dateTimes;
}
/*加载数据字典*/
function loadDictionary(fn) {
	var url = projectName + "/platform/dictionary.do?command=loadChiadNode";
	var $node = $("[data-dic]");
	var codes = [];
	var nodes = {};
	$node.each(function(i, n) {
		var obj = eval('(' + $(this).attr("data-dic") + ')');
		codes.push(obj.code);
		nodes[obj.code] = $(this);
	});
	if (codes.length == 0)
		return;
	$.post(url, {
		codes : codes
	}, function(data) {
//		console.log(data);
		var d = data;
		for ( var k in d) {
			nodes[d[k]["P_DIC_CODE"]].append("<option value='" + d[k].DictionaryCode + "'>" + d[k].DictionaryName + "</option>");
		}
		if (fn && typeof fn === "function") {
			return fn();
		}
	}, "json");
}

/*加载数据字典,考试起始时间*/
function loadTimeDictionary(fn) {
	var url = projectName + "/platform/dictionary.do?command=loadChiadNode";
	var $node = $("[datatime-dic]");
	var codes = [];
	var nodes = {};
	$node.each(function(i, n) {
		var obj = eval('(' + $(this).attr("datatime-dic") + ')');
		codes.push(obj.code);
		nodes[obj.code] = $(this);
	});
	if (codes.length == 0)
		return;
	$.post(url, {
		codes : codes
	}, function(data) {
//		console.log(data);
		var d = data;
		for ( var k in d) {
			nodes[d[k]["P_DIC_CODE"]].append("<option value='" + d[k].DictionaryCode + "'>" + d[k].DictionaryName + "</option>");
		}
		if (fn && typeof fn === "function") {
			return fn();
		}
	}, "json");
}

/* 加载说明信息 */
function loadHTML(str, $el) {
	var url = "../description/descriptionList.do?command=loadDescription";
	POST(url, {
				smlx : str
			}, function(data) {
				$el.append(data.context);
			});
}

/* 加载当前年度 */
function loadSemesterYear() {
	var year = new Date().getFullYear();
	var month = new Date().getMonth() + 1;
	var currentYear = "";
	if (month < 9) {
		currentYear = (year - 1) + "-" + year;
	} else {
		currentYear = year + "-" + (year + 1);
	}
	return currentYear;
}
/* 得到某一天的前一天*/
function  loadStrYesterday(exam_time){
	var today = new Date(exam_time);
	var yesterday_milliseconds = today.getTime() - 1000 * 60 * 60 * 24;
	var yesterday = new Date();
	yesterday.setTime(yesterday_milliseconds);
	var strYear = yesterday.getFullYear();
	var strDay = yesterday.getDate();
	var strMonth = yesterday.getMonth() + 1;
	if (strMonth < 10) {
		strMonth = "0" + strMonth;
	}
	var strYesterday = strYear + "-" + strMonth + "-" + strDay;
	return strYesterday;
}


//得到某一天的后一天
function  loadStrTmorrowday(course_exam_time){
	var today = new Date(course_exam_time);
	var tmorrow_milliseconds = today.getTime() + 1000 * 60 * 60 * 24;
	var  tmorrow = new Date();
	tmorrow.setTime(tmorrow_milliseconds);
	var strYear = tmorrow.getFullYear();
	var strDay = tmorrow.getDate();
	var strMonth = tmorrow.getMonth() + 1;
	if (strMonth < 10) {
		strMonth = "0" + strMonth;
	}
	if (strDay < 10) {
		strDay = "0" + strDay;
	}
	var strTmorrow = strYear + "-" + strMonth + "-" + strDay;
	return strTmorrow;
}

/*得到当前年*/

function getNowDate(){
	var today = new Date();
	var strYear = today.getFullYear();
	var strDay = today.getDate();
	var strMonth = today.getMonth() + 1;
	if (strMonth < 10) {
		strMonth = "0" + strMonth;
	}
	if(strDay<10){
		strDay="0"+strDay
	}
	var today = strYear + "-" + strMonth + "-" + strDay;
	return today;
}

//得到某一时间多少分钟之后的时间
var   addMinutes=function(time,minutes)
 {     
	var hour=time.split(":")[0]*60*1000*60;//得到开始时间的小时
    var   min=time.split(":")[1]*1000*60;   //得到开始时间的分钟
    var startTime=parseInt(hour)+parseInt(min);//开始时间变成时分秒
    var hourLength=parseInt(minutes)*1000*60;//考试时长
    var stop=parseInt(startTime)+parseInt(hourLength);
   var stopTime = parseFloat(stop) /1000;  
   var stopHour;
   var stopMinutes;
   if (null!= stopTime &&""!= stopTime) {  
        if (stopTime >60&& stopTime <60*60) {  
        	stopHour= parseInt(stopTime /60.0) ;
        	stopMinutes=parseInt(Math.round((parseFloat(stopTime /60.0))));
        	if(stopMinutes==0){
        		 stopTime =stopHour +":00"
             }else{
            	 stopTime =stopHour+":"+stopMinutes ;
             }
        }else if (stopTime >=60*60&& stopTime <60*60*24) {  
        	stopHour=parseInt(stopTime /3600.0) ;
        	stopMinutes=parseInt(Math.round((parseFloat(stopTime /3600.0) -parseInt(stopTime /3600.0)) *60));
             if(stopMinutes==0){
            	 stopTime =stopHour +":00"
             }else{
            	 stopTime = stopHour +":"+stopMinutes;
             }
        	
        }
    }else{  
    	stopTime = "";  
    }  
    return stopTime;  
  }

/*两个日期相差天数*/
var getDateDifference=function (startDate,endDate){
	var startTime = new Date(Date.parse(startDate.replace(/-/g,   "/"))).getTime();     
    var endTime = new Date(Date.parse(endDate.replace(/-/g,   "/"))).getTime();     
   // var dateDifference = Math.abs((startTime - endTime))/(1000*60*60*24);   
    var dateDifference = parseInt((startTime - endTime)/(1000*60*60*24));  
    
     return dateDifference;
}


function array_remove_repeat(a) { // 去重
    var r = [];
    for(var i = 0; i < a.length; i ++) {
        var flag = true;
        var temp = a[i];
        for(var j = 0; j < r.length; j ++) {
            if(temp === r[j]) {
                flag = false;
                break;
            }
        }
        if(flag) {
            r.push(temp);
        }
    }
    return r;
}
function array_difference(a, b) { // 差集 a - b
    var clone = a.slice(0);
    for(var i = 0; i < b.length; i ++) {
        var temp = b[i];
        for(var j = 0; j < clone.length; j ++) {
            if(temp === clone[j]) {
                clone.splice(j,1);
            }
        }
    }
    return array_remove_repeat(clone);
}