(function ($) {
		$.widget("ui.pagedmenu", {
			version: "2.0",
		    contentElement: null,
		    options: {
		        cls: ".leftPane",
		        change: null, //Events
		        sourceUrl: null,
		        selectedItem: undefined,
		        leftPaneWidth:"190px"
		    },
		    _create: function () {
		 	   this.contentElement =this.element.find(".nx-left");
		 	   var _this = this;
		 	   //this.scrollListen();
		 	   this.element.find(".zoom-leftPane").click(function(){
		 		  _this.zoom.zoomClick(_this);
		 	   });
		 	  
		 	   if (this.options.sourceUrl) {
		            this.loadMenusForUrl(this.options.sourceUrl);
		        }
		    },
		    _initContentConfig: function () {
		    },
		    loadMenusForUrl: function (url) {
		        var _this = this;
		        if (url && url!="") {
		        	_this.options.sourceUrl = url;
		        }
		        _this._clearItems();
		        $.getJSON(this.options.sourceUrl).done(function (data) {
		        	_this._addItems(JSON.parse(data.response).data);
		        	_this._initContentConfig();
		          /*   vthis._refresh();  */
		        }).fail(function () {
		        	_this._initContentConfig();
		            _this._refresh();
		        });
		        
		    }, 
		    _clearItems: function () {
		        this.contentElement.children().remove();
		    }, 
		    /*
		     * 收缩菜单导航跟随菜单移动
		     * l_title022大于leftPane最大高度是，固定在leftPane底部
		     */
		    divChange:function(){
				var height=$(".nx-left").height();
				if(height>$(".leftPane").height()-32){
					$(".l_title022").height($(".leftPane").height()-32);
				}else{
					$(".l_title022").css("height",'');
				}
			},
		    _addItems: function (items) {//添加一级菜单
		        var $cel = this.contentElement, elAry = [], item = undefined,_this=this;
		        for (var i = 0, len = items.length; i < len; i++) {
		            item = items[i];
		            if(item.title.length>10)
			    	{
		            	item.title = item.title.substr(0,10)+"...";
			    	}//一级菜单
		            
		            $li = $("<li>").addClass("ui-pagedmenu-menu-item")
		            					.append([$("<img>")
					    	  			.attr("src","theme/images/"+(item.menuImgSrc||"c_icon02")+".png").hide()])
		                                .append($("<span>").addClass("textSpan").text(item.title))
		            					.data(item)
		                                .addClass(item.cls)
		                                .click(function(){
		                                	_this._openTwoLevelMenu($(this));//展开二级菜单事件
		                                	
		                                });
		            
		            elAry.push($li);
		            if(item.menus.length>0)//二级菜单
		            {
		            	  var $ul = $("<ul>").addClass("ui-ul-itemClickDown"),$lis = [];
					      for(var k in item.menus)
					      {
					    	  var menu = item.menus[k];
					    	  if(menu.title.length>10)
					    	  {
					    		  menu.title = menu.title.substr(0,10)+"...";
					    	  }
					    	 /* .append([$("<div>")
   	  								.addClass("copuTextSpan")
   	  								.html(menu.title)
   	  								.css({"border":"2px solid black","color":"black","z-index":"1","top":"20px"})
   	  								])*/
					    	  var content = $("<li>").addClass("ui-li-itemClickDown")
					    	  								.data(menu)
					    	  								.append([$("<img>")
					    	  								.attr("src","theme/images/"+(menu.menuImgSrc||"c_icon02")+".png").hide()])
					    	  								.append("&nbsp;&nbsp;&nbsp;&nbsp;")
					    	  								.append([$("<span>")
					    	  								.addClass("textSpan")
					    	  								.html(menu.title)])
					    	  								.click(function(){
															    		  _this._itemClick($(this));
															    		  
															 });
					    	  $lis.push(content);
					      }
					      $ul.append($lis);
					      elAry.push($ul);
		            }
		        }
		        $cel.append(elAry);
		        
		        this.menuIconHover();
		        this.contentElement.mCustomScrollbar({//自定义滚动条
		        	callbacks:{
		        		onScrollStart:function (){
		        			$(".ui-pagedmenu-menuBox").remove();
		        		}
		        	}
		        });
		        
		    },
		    _openTwoLevelMenu: function(le){//展开二级菜单
				  $(".ui-pagedmenu-menuBox").remove();
			      var data = le.data(),_this=this;
			      
			      var $ul = le.next("ul");
			      _this._trigger("change", new jQuery.Event(), le);
			      if($ul.length<1)return this;
			      
			      if(!$ul.data("show"))
			      {
			    	  $ul.slideDown(function() {
			    		  _this.divChange();
					}).data({show:true});
			    	  le.find(".fa-arrow-circle-right").removeClass("fa-arrow-circle-right").addClass("fa-arrow-circle-down");
			      }else{
			    	  le.find(".fa-arrow-circle-down").removeClass("fa-arrow-circle-down").addClass("fa-arrow-circle-right");
			    	  $ul.slideUp(function() {
			    		  _this.divChange();
					}).data({show:false});
			      }
		    	  return this;
			  },
		    _setOptions: function (options) {//OVERVIEW SETOPTIONS
		        var that = this,
				  reload = false;
			      $.each(options, function (key, value) {
			          that._setOption(key, value);
			          if (key === "sourceUrl") {
			              reload = true;
			          }
			      });
			      if (reload) {
			          this.loadMenusForUrl(this.options.sourceUrl);
			      }
		  },
		  zoom:{//菜单缩放
		  	 open:true,
		  	 element:".zoom-leftPane",
		  	 zoomClick:function(_this)
		  	 {
		  		var el=_this.element;
		  		 if(this.open=!this.open)
		  		{
		  			$(el).find("span").show(200);
		  			_this.options.leftPaneWidth="170px";
		  			wh=-140;
		  			//收缩导航在菜单右侧，跟随滚动条滑动
		  			/*$(".l_right").css("padding-left","186px");
		  			$(".ui-li-itemClickDown").css("padding-left","25px");
		  			$(".ui-pagedmenu-menu-item").css("padding-left","25px");
		  			$(".mCSB_container").css("width","176px");
		  			$(".zoom-box").css("left","150px");
		  			$(".ui-pagedmenu-menu-item .mCS_img_loaded").hide();
		  			$(".l_dou").find("img").attr("src","theme/images/l_dou_left.png");
		  			$(".l_dou_view").text("收缩");*/
		  			 /*
				     * 收缩菜单导航在菜单底部
				     * l_title022大于leftPane最大高度是，固定在leftPane底部
				     */
		  			$(".l_right").css("padding-left","186px");
		  			$(".ui-li-itemClickDown").css("padding-left","25px");
		  			$(".ui-pagedmenu-menu-item").css("padding-left","25px");
		  			$(".mCSB_container").css("width","176px");
		  			$(".l_dou").find("img").attr("src","theme/images/l_dou.png");
		  			$(".l_dou_view").css("left","172px");
		  			$(".l_dou_view").text("收缩")
		  			$(".ui-pagedmenu-menu-item .mCS_img_loaded").hide();
		  			$(".mCS_img_loaded").hide();
		  			/*$(this.element).html("<span class='l_dou'><img src='theme/images/l_dou.png'></span>");
		  			 $(this.element).html("<span class='l_dou' onmouseover='show();' onmouseout='hide();'><img src='theme/images/l_dou_left.png'>" +
  					"<span class='l_dou_view'>收缩</span></span>");*/
		  			
		  		}else{
		  			$(el).find(".textSpan").hide();//隐藏菜单下面的span
		  			_this.options.leftPaneWidth="40px";
		  			wh=140;
		  			/*$(".ui-li-itemClickDown,.ui-pagedmenu-menu-item").css("padding-left","5px");
		  			//$(".l_dou img").css("padding-left","5px");
		  			$(".mCS_img_loaded").show();
		  			$(".l_right").css("padding-left","50px");
		  			$(".mCSB_container").css("width","45px");
		  			$(".zoom-box").css("left","20px");
		  			$(".l_dou").find("img").attr("src","theme/images/l_dou_right.png");
		  			$(".l_dou_view").text("展开")*/
		  			$(".ui-li-itemClickDown,.ui-pagedmenu-menu-item").css("padding-left","5px");
		  			$(".l_dou img").css("padding-left","5px");
		  			$(".mCS_img_loaded").show();
		  			$(".l_right").css("padding-left","50px");
		  			$(".mCSB_container").css("width","45px");
		  			$(".l_dou").find("img").attr("src","theme/images/l_dou_r.png");
		  			$(".l_dou_view").css("left","42px");
		  			$(".l_dou_view").text("展开")
		  			/*$(this.element).html("<span class='l_dou'><img src='theme/images/l_dou_r.png'></span>");
		  			$(this.element).html("<span class='l_dou' onmouseover='show();' onmouseout='hide();'><img src='theme/images/l_dou_right.png'>" +
		  					"<span class='l_dou_view'>展开</span></span>");*/
		  		}
		  		
		  		$(el).css({"width":_this.options.leftPaneWidth});
				$(el).find(".ui-pagedmenu-menu-item").css({"width":_this.options.leftPaneWidth});
		  		$(".ui-pagedmenu-menuBox").css({"left":_this.options.leftPaneWidth});
		  		
		  		$.resetLayout();//页面重新布局
		  		
		  	 }
		  },
		  _itemClick: function (le) {//点击一级菜单
			  $(".checked").removeClass("checked");
			  le.addClass("checked");
			  $(".ui-pagedmenu-menuBox").remove();
		      var data = le.data(),_this=this;
		      
		      _this._trigger("change", new jQuery.Event(), le);
		      
		      var menuBox = [];
		      for(var k in data.menus)
		      {
		    	  var $row = $("<div>").addClass("ui-pagedmenu-row");
		    	  var menu = data.menus[k];
		    	  var two_level = $("<span>").addClass("ui-pagedmenu-twolevel").data(menu).html(menu.title).click(function(){
		    		  	_this._trigger("change", new jQuery.Event(), this);
		    	  });
		    	  $row.append(two_level);
		    	  
		    	  if(menu.menus && menu.menus.length>0)
		    	 {
		    		  var $ul = $("<ul>").addClass("ui-pagedmenu-ul");
		    		  for(var i in menu.menus)
		    		  {
			    			  var three_level = $("<li>").html(menu.menus[i].title).click(function(){
				  	      		  		_this._trigger("change", new jQuery.Event(), this);
				  	      	  });
			    			  $ul.append(three_level);
		    		  }
		    		  
		    		  $row.append($ul);
		    	 }
		    	  
		    	  menuBox.push($row);
		      }
		      if(menuBox.length==0)return ;
		     var  childMenuPane = $("<div>").addClass("ui-pagedmenu-menuBox").append(menuBox).appendTo(document.body);
		     	var clas = {};
			     if((le.offset().top+childMenuPane.height())>document.body.clientHeight)
			    {
			    	 clas.bottom=0;
			    }else{
			    	 clas.top=le.offset().top;
			    }
			    clas.left=$(_this.options.cls).width();
			    
			    var listen = setTimeout(function(){
			    	childMenuPane.remove();
		     	},2000);
			    
		     	childMenuPane.css(clas).data({listen:listen}).
		     	hover(function(){
		     		clearTimeout($(this).data("listen"));
                 },function(){
                	 $(this).remove();
                 });
		     	 
		  	},
		  	menuIconHover:function(){//菜单图标鼠标移动事件
		  		var _this = this;
		  	  $(".ui-pagedmenu-menu-item,.ui-li-itemClickDown").hover(function(){
		  		  if(!_this.zoom.open)
		  		  {
			  			var data = $(this).data();
			        	var top = $(this).offset().top;
			        	var left = $(this).outerWidth();
			        	$(".menu-icon-hover").css({"left":left+"px","top":top+"px"}).show().find(".content").html(data.title);
		  		   }
		        },function(){
		        	$(".menu-icon-hover").hide();
		        });
		  	}
		});
		
		
})(jQuery);
