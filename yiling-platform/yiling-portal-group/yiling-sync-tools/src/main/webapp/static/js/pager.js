
/*
    说明：  分页插件
    用法：  $('.pager').each(function(){
                $(this).pager();
            });
*/
$.fn.pager = function(opts){
    var set = $.extend({
        url_form:'', //如果附带表单，则传入表单数据
        requestType:'get',//请求方式
        asyn:0, //是否ajax加载
        showNum:1, // 是否显示页数
        showForm:1, //是否显示跳转表单
        showPrevAndNext:1, //是否显示上一页，下一页
        timeout:30000, //ajax加载的超时界限
        contentBox:'#contentbox', //ajax加载的内容容器
        size:9, //分页显示的长度，一般为9,也可以是7或者5，不能为偶数，其它的数也没什么意义
        dataType:'json',
        beforeSend:function(){},
		wrongPageCall:function(page){alert("请输入一个正确的页码！")},
		error:function(){},
        callback:function(data,index){} //每次ajax加载成功后的回调函数，传为的第一个参数是加截进来的数据，第二个参数是当前页数
    },opts||{});

    var _this = $(this),xhr,$contentBox = $(set.contentBox),pPage,nPage;
    var current = _this.attr('current') - 0 || 0,
        total = _this.attr('total') - 0 || 0,
        url = _this.attr('url') || '',
        size = set.size;
        url_form = set.url_form;

    var PREVPAGE = '<a href="javascript:void(0)" class="pager_prev">\u4e0a\u4e00\u9875</a>',
        NEXTPAGE = '<a href="javascript:void(0)" class="pager_next">\u4e0b\u4e00\u9875</a>',
        JUMPFROM = '<span class="page_total">共<em>'+total+'</em>页</span><span class="form_pageJump"><label>到<input type="text" name="page" class="input_item input_item_shortest" value="" autocomplete="off" />页</label><a href="javascript:void(0)" class="btn_blue btn_submit" data-form-button="submit">确定</a></span>';

    var sideLen=Math.floor((size - 2)/2),leftSize = sideLen+3,rightSize = sideLen+1;

    //如果只有一页或者没有
//    if(total<2) return;

    create(current);

    function create(current){
        var html = '',index=0,len,c=0;
        var pages = new Array(total+1).join('t,').replace(/t/g,function(){ return ++index;}).split(',');
        pages.pop();
        len = pages.length;
         c = current;
        if(len > size){
            if(current < leftSize){
                pages.splice(size-1,len-size,'<span class="pager_dot">...</span>');
                c = current;
            }else if(current >= leftSize && current < total - rightSize){
                pages.splice(current+sideLen,len-current-rightSize,'<span class="pager_dot">...</span>');
                pages.splice(1,current-leftSize+1,'<span class="pager_dot">...</span>');
                c = Math.round(size/2);
            }else{
                pages.splice(1,len-size,'<span class="pager_dot">...</span>');
                c = size + current - total ;
            };
        };

        for(var i=0;i<pages.length;i++){
            html += /^\d+$/.test(pages[i]) ? '<a href="javascript:void(0)" class="pager_item">'+(pages[i]-0)+'</a>' : pages[i];
        };

        html = set.showNum ? (set.showPrevAndNext ? (set.showForm ? (PREVPAGE + html +  NEXTPAGE + JUMPFROM) : (PREVPAGE + html +  NEXTPAGE)) : (set.showForm ? (html + JUMPFROM) : (html))) : (set.showPrevAndNext ? (set.showForm ? (PREVPAGE + NEXTPAGE + JUMPFROM) : (PREVPAGE + NEXTPAGE)) : (set.showForm ? (JUMPFROM) : ''));

        _this.html(html);
        set.showNum && _this.find('a').eq(set.showPrevAndNext ? c : c-1).addClass('active');

        pPage = $('.pager_prev',_this);
        nPage = $('.pager_next',_this);
        setClass(current);
    };

    function go(index,event){
        event.preventDefault();
        if(index <= 0 || index > total) return;
        var href = '';
        href = url + (url.indexOf('?') > -1 ? '&page=' : '?page=');
        if(!set.asyn){
            window.location.href = (url_form == '' ? href+index : href+index+'&'+url_form);
        }else{
            setClass(index);
            xhr && xhr.abort();
            xhr = $.ajax({
                url:href+index,
                timeout:set.timeout,
                data:set.url_form,
                type:set.requestType,
                dataType:set.dataType,
                beforeSend:function(){
                    set.beforeSend && set.beforeSend(index);
                },
				error: function(){
					set.error && set.error(index);
				},
                success:function(data){
                    $contentBox.removeClass('isLoading').html(data);
                    set.callback && set.callback(data,index);
                }
            });

            current = index;
            create(current);
        };
    };

    function setClass(index){
        pPage.removeClass('pager_disable pager_disable_prev');
        nPage.removeClass('pager_disable pager_disable_next');
        pPage[index == 1 ? 'addClass' : 'removeClass']('pager_disable pager_disable_prev');
        nPage[index == total ? 'addClass' : 'removeClass']('pager_disable pager_disable_next');
    };

    $(this).find('input[type="text"]').on('keyup', function(e) {
        this.value = this.value.replace(/\D/,'');
    });
    $(this).off("click").on('click',function(event){
        var target = event.target,$t = $(target);
        if($t.hasClass('pager_disable') || $t.hasClass('active')) return;
        if($t.hasClass('pager_prev')) go(--current,event);
        if($t.hasClass('pager_next')) go(++current,event);
        if($t.hasClass('pager_item')) go($t.text()-0,event);
        if($t.hasClass('btn_submit')){
            var v = $t.closest('.form_pageJump').find('input[type="text"]').val() - 0;
            if(isNaN(v) || v < 1 || v > total ){
				set.wrongPageCall && set.wrongPageCall.call(total,v);
				return;
			}
            go(v,event);
        };
    });
};