(function () {
	/*本市县级教育网站*/
	$.ajax({
       	url:"link/linkNet.do?command=searchLink",
        type: "POST",
        data: {
        	_thisTitle:"bsxj"
        },
        dataType: "JSON",
        success: function(data, xhr) {
        	var dataList = data;
        	var count = dataList.length;
        	var j = 0;
        	var li_id="#bsxj_lj"+(j);
        	$(".panel-container #bsxj ul").append("<li style='float:left' id='bsxj_lj"+j+"'></li>");
        	for(var i = 1;i <= dataList.length;i++){
        		li_id="#bsxj_lj"+(j);
    			if( i % 6 == 0){
    				j++;
    				$(li_id).append("<a target='_blank' href='"+dataList[i-1].url+"'><span class='last'>"+dataList[i-1].name+"</span></a>");
    				$(".panel-container #bsxj ul").append("<li style='float:left' id='bsxj_lj"+j+"'></li>");
    			}else{
    				$(li_id).append("<a target='_blank' href='"+dataList[i-1].url+"'><span>"+dataList[i-1].name+"</span></a>");
    			}
        	}
        }
    });
	/*本市教育网站*/
	$.ajax({
		url:"link/linkNet.do?command=searchLink",
		type: "POST",
		data: {
			_thisTitle:"bsjy"
		},
		dataType: "JSON",
		success: function(data, xhr) {
			var dataList = data;
			var count = dataList.length;
			var j = 0;
			var li_id="#bsjy_lj"+(j);
			$(".panel-container #bsjy ul").append("<li style='float:left' id='bsjy_lj"+j+"'></li>");
			for(var i = 1;i <= dataList.length;i++){
				li_id="#bsjy_lj"+(j);
				if( i % 6 == 0){
					j++;
					$(li_id).append("<a target='_blank' href='"+dataList[i-1].url+"'><span class='last'>"+dataList[i-1].name+"</span></a>");
					$(".panel-container #bsjy ul").append("<li style='float:left' id='bsjy_lj"+j+"'></li>");
				}else{
					$(li_id).append("<a target='_blank' href='"+dataList[i-1].url+"'><span>"+dataList[i-1].name+"</span></a>");
				}
			}
		}
	});
	/*政府相关网站*/
	$.ajax({
		url:"link/linkNet.do?command=searchLink",
		type: "POST",
		data: {
			_thisTitle:"gov"
		},
		dataType: "JSON",
		success: function(data, xhr) {
			var dataList = data;
			var count = dataList.length;
			var j = 0;
			var li_id="#gov_lj"+(j);
			$(".panel-container #gov ul").append("<li style='float:left' id='gov_lj"+j+"'></li>");
			for(var i = 1;i <= dataList.length;i++){
				li_id="#gov_lj"+(j);
				if( i % 6 == 0){
					j++;
					$(li_id).append("<a target='_blank' href='"+dataList[i-1].url+"'><span class='last'>"+dataList[i-1].name+"</span></a>");
					$(".panel-container #gov ul").append("<li style='float:left' id='gov_lj"+j+"'></li>");
				}else{
					$(li_id).append("<a target='_blank' href='"+dataList[i-1].url+"'><span>"+dataList[i-1].name+"</span></a>");
				}
			}
		}
	});
	/*常用教育资源类网站*/
	$.ajax({
		url:"link/linkNet.do?command=searchLink",
		type: "POST",
		data: {
			_thisTitle:"cyjy"
		},
		dataType: "JSON",
		success: function(data, xhr) {
			var dataList = data;
			var count = dataList.length;
			var j = 0;
			var li_id="#cyjy_lj"+(j);
			$(".panel-container #cyjy ul").append("<li style='float:left' id='cyjy_lj"+j+"'></li>");
			for(var i = 1;i <= dataList.length;i++){
				li_id="#cyjy_lj"+(j);
				if( i % 6 == 0){
					j++;
					$(li_id).append("<a target='_blank' href='"+dataList[i-1].url+"'><span class='last'>"+dataList[i-1].name+"</span></a>");
					$(".panel-container #cyjy ul").append("<li style='float:left' id='cyjy_lj"+j+"'></li>");
				}else{
					$(li_id).append("<a target='_blank' href='"+dataList[i-1].url+"'><span>"+dataList[i-1].name+"</span></a>");
				}
			}
		}
	});
	/*常用网站*/
	$.ajax({
		url:"link/linkNet.do?command=searchLink",
		type: "POST",
		data: {
			_thisTitle:"cywz"
		},
		dataType: "JSON",
		success: function(data, xhr) {
			var dataList = data;
			var count = dataList.length;
			var j = 0;
			var li_id="#cywz_lj"+(j);
			$(".panel-container #cywz ul").append("<li style='float:left' id='cywz_lj"+j+"'></li>");
			for(var i = 1;i <= dataList.length;i++){
				li_id="#cywz_lj"+(j);
				if( i % 6 == 0){
					j++;
					$(li_id).append("<a target='_blank' href='"+dataList[i-1].url+"'><span class='last'>"+dataList[i-1].name+"</span></a>");
					$(".panel-container #cywz ul").append("<li style='float:left' id='cywz_lj"+j+"'></li>");
				}else{
					$(li_id).append("<a target='_blank' href='"+dataList[i-1].url+"'><span>"+dataList[i-1].name+"</span></a>");
				}
			}
		}
	});
})(jQuery);