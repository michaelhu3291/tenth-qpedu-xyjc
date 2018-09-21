/* 双向列表框 */
$(function(){
	//学校
	$('#b1').click(function(){
		$obj =$('#s1 option:selected').clone(true);
		if($obj.size() == 0){
			 window.message({
	                text: "请至少选择一条!",
	                title: "提示"
	            });
		}
		$('#s2').append($obj);
		$('#s1 option:selected').remove();
	});
	
	$('#b2').click(function(){
		/*$('#s1 option').attr("selected","selected");*/
		$('#s2').append($('#s1 option'));
	});
	
	$('#b3').click(function(){
		$obj = $('#s2 option:selected').clone(true);
		if($obj.size() == 0){
			window.message({
                text: "请至少选择一条!",
                title: "提示"
            });
		}
		/*$obj.removeAttr("selected");*/
		$('#s1').append($obj);
		$('#s2 option:selected').remove();
	});
	
	$('#b4').click(function(){
		$('#s1').append($('#s2 option'));
	});
	$("#s1").dblclick(function(){
		$("#s1 option:selected").appendTo("#s2");	
		$("#s2 option:selected").removeAttr("selected");		
	});
	$("#s2").dblclick(function(){
		$("#s2 option:selected").appendTo("#s1");	
		$("#s1 option:selected").removeAttr("selected");
	});	
	
	//班级
	$('#class_b1').click(function(){
		$obj = $('#class_s1 option:selected').clone(true);
		if($obj.size() == 0){
			window.message({
                text: "请至少选择一条!",
                title: "提示"
            });
		}
		$('#class_s2').append($obj);
		$('#class_s1 option:selected').remove();
	});
	
	$('#class_b2').click(function(){
		$('#class_s2').append($('#class_s1 option'));
	});
	
	$('#class_b3').click(function(){
		$obj = $('#class_s2 option:selected').clone(true);
		if($obj.size() == 0){
			window.message({
                text: "请至少选择一条!",
                title: "提示"
            });
		}
		$('#class_s1').append($obj);
		$('#class_s2 option:selected').remove();
	});
	
	$('#class_b4').click(function(){
		$('#class_s1').append($('#class_s2 option'));
	});
	
	$("#class_s1").dblclick(function(){
		$("#class_s1 option:selected").appendTo("#class_s2");	
		$("#class_s2 option:selected").removeAttr("selected");
	});
	$("#class_s2").dblclick(function(){
		$("#class_s2 option:selected").appendTo("#class_s1");	
		$("#class_s1 option:selected").removeAttr("selected");
	});	
	
	
	
	
	//科目
	$('#course_b1').click(function(){
		$obj = $('#course_s1 option:selected').clone(true);
		if($obj.size() == 0){
			window.message({
                text: "请至少选择一条!",
                title: "提示"
            });
		}
		$('#course_s2').append($obj);
		$('#course_s1 option:selected').remove();
	});
	
	$('#course_b2').click(function(){
		$('#course_s2').append($('#course_s1 option'));
	});
	
	$('#course_b3').click(function(){
		$obj = $('#course_s2 option:selected').clone(true);
		if($obj.size() == 0){
			window.message({
                text: "请至少选择一条!",
                title: "提示"
            });
		}
		$('#course_s1').append($obj);
		$('#course_s2 option:selected').remove();
	});
	
	$('#course_b4').click(function(){
		$('#course_s1').append($('#course_s2 option'));
	});

	$("#course_s1").dblclick(function(){
		$("#course_s1 option:selected").appendTo("#course_s2");	
		$("#course_s2 option:selected").removeAttr("selected");
	});
	$("#course_s2").dblclick(function(){
		$("#course_s2 option:selected").appendTo("#course_s1");	
		$("#course_s1 option:selected").removeAttr("selected");
		
	});	
	
	
	
	 
	//老师
	$('#teacher_b1').click(function(){
		$obj = $('#teacher_s1 option:selected').clone(true);
		if($obj.size() == 0){
			window.message({
                text: "请至少选择一条!",
                title: "提示"
            });
		}
		$('#teacher_s2').append($obj);
		$('#teacher_s1 option:selected').remove();
	});
	
	$('#teacher_b2').click(function(){
		$('#teacher_s2').append($('#teacher_s1 option'));
	});
	
	$('#teacher_b3').click(function(){
		$obj = $('#teacher_s2 option:selected').clone(true);
		if($obj.size() == 0){
			window.message({
                text: "请至少选择一条!",
                title: "提示"
            });
		}
		$('#teacher_s1').append($obj);
		$('#teacher_s2 option:selected').remove();
	});
	
	$('#teacher_b4').click(function(){
		$('#teacher_s1').append($('#teacher_s2 option'));
	});

	$("#teacher_s1").dblclick(function(){
		$("#teacher_s1 option:selected").appendTo("#teacher_s2");	
		$("#teacher_s2 option:selected").removeAttr("selected");
	});
	$("#teacher_s2").dblclick(function(){
		$("#teacher_s2 option:selected").appendTo("#teacher_s1");	
		$("#teacher_s1 option:selected").removeAttr("selected");
		
	});	
	
});