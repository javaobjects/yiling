$(function(){

	$(".account_set").hover(function(){
		$("#account_set_ul").show();
	},function(){
		$("#account_set_ul").hide();
	});
	var urlstr = window.location.href;
	$("#menuInit li a").each(function(i,val){
		if(urlstr.indexOf($(this).attr('href'))>0){
			$(this).css("color","#ff6c60");
			$(this).closest("ul").show().siblings("a").addClass("active").find("span").addClass("dcjq-icon1");
		}
	});
	$("#featurebtn").on("click",function(){
		var clasnames = $(this).find("i").attr("class");
		if(clasnames == 'fa fa-angle-up'){
			$(this).find("i").removeClass("fa fa-angle-up").addClass("fa fa-angle-down");
			$("#featurebody").show();
			$("#featurebtn").closest(".row").removeClass("d_feature");
			$(".feature").css({"margin-right":"0px"});
		}else{
			$(this).find("i").removeClass("fa fa-angle-down").addClass("fa fa-angle-up");
			$("#featurebody").hide();
			$("#featurebtn").closest(".row").addClass("d_feature");
			$(".feature").css({"margin-right":"15px"});
		}
		$(".featuretop").slideToggle(500);
	});
});

//弹出加载提示框
var tipLoad = function (options, dom, opacity) {
        if (!dom) {
            dom = "body";
        }
        if (!options) {
            options = '正在加载...';
        }
        var tipDiv = $('<div class="tipLoad">' + options + '</div>');
        $(dom).css({ "position": "relative" });
        tipDiv.appendTo(dom);
        Maskshow(opacity, dom);
    };
    //添加遮罩层
var Maskshow = function (opacity, dom) {
		if (!dom) {
	            dom = "body";
	    }
        $('<div class="maskLayer"></div>').appendTo(dom);
        if (!opacity) {
            opacity = 50;
        }
        $(".maskLayer").css({
            "height": "100%",
            "width": "100%",
            "background": "#000",
            "filter": opacity,
            "opacity": opacity / 100,
            "top": 0,
            "overflow-x":"hidden",
            "overflow-y":"hidden",
            "bottom": 0,
            "right": 0,
            "zIndex":1051,
            "left": 0,
            "position": "fixed"
        });
};

    //删除遮罩层
var Maskremove = function () {
        $(".maskLayer").remove();
}

    //删除提示框
var tipRemove = function () {
        Maskremove();
        $(".tipLoad").remove();
}
var	alertModal = function (content, callback) {
        var confirmModalObj = $('<div class="alertm"><h2><span class="fl">温馨提示</span><i class="fr fa fa-close cancel" ></i></h2><div class="alertnews">' + content + '</div><p><button id="confirm" type="button" class="btn btn-info margin-r-10">确定</button><button type="button"  class="btn btn-default cancel">取消</button></p></div>');
        confirmModalObj.appendTo("body");
        Maskshow();
        confirmModalObj.find('#confirm').on('click', function () {
            if (callback) {
                callback();
            }
            confirmModalObj.hide();
            Maskremove();
        });
        confirmModalObj.find('.cancel').on('click', function () {
           $(".alertm").remove();
           Maskremove();
        });
        
    };
var	alertModalb = function (content, callback) {
        var confirmModalObj = $('<div class="alertm1"><h2><span class="fl">温馨提示</span><i class="fr fa fa-close cancel" ></i></h2><div class="alertnews">' + content + '</div><p><button type="button" class="btn btn-info cancel">确定</button></p></div>');
        confirmModalObj.appendTo("body");
        Maskshow();
        confirmModalObj.find('.cancel').on('click', function () {
            if (callback) {
                callback();
            }
            confirmModalObj.hide();
            Maskremove();
        });
        
    };
var JPlaceHolder = {
    //检测
    _check : function(){
        return 'placeholder' in document.createElement('input');
    },
    //初始化
    init : function(){
        if(!this._check()){
            this.fix();
        }
    },
    //修复
    fix : function(){
        jQuery(':input[placeholder]').each(function(index, element) {
            var self = $(this), txt = self.attr('placeholder');
            self.wrap($('<div></div>').css({position:'relative', zoom:'1', border:'none', background:'none', padding:'none', margin:'none'}));
            var pos = self.position(), h = self.outerHeight(true), paddingleft = self.css('padding-left');
            var holder = $('<span></span>').text(txt).css({position:'absolute', left:pos.left, top:pos.top, height:h,lineHeight:"34px", paddingLeft:paddingleft, color:'#aaa'}).appendTo(self.parent());
            if(self.val()==""){
            	holder.show();
            }else{
            	holder.hide();
            }
            
            
            self.focusin(function(e) {
                holder.hide();
            }).focusout(function(e) {
                if(!self.val()){
                    holder.show();
                }
            });
            holder.click(function(e) {
                holder.hide();
                self.focus();
            });
        });
    }
};