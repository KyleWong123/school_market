/**
 * 
 */
$(function(){
	//定义访问后台，获取一级列表即头条的url
	var url = '/school-market/frontend/listmainpageinfo';
	//访问后台获取头条及一级列表
	$.getJSON(url,function(data){
		if(data.success){
			//获取后台传来的头条
			var headLineList = data.headLineList;
			var swiperHtml='';
			//遍历头条列表，并评出轮播
			headLineList.map(function(item,index){
				swiperHtml+=''+'<div class="swiper-slide img-wrap>'
				+'<a href="'+item.lineLink
				+'"external><img class="banner-img" src="'+item.lineImg
				+'"alt="'+item.lineName+'"></a>'+'</div>';
				
			});
			//将轮播图组赋值给前端轮播控件
			$('.swiper-wrapper').html(swiperHtml);
			//设置轮播时间
			$(".swiper-container").swiper({
				autoplay:3000,
			//对轮播图进行操作时，是否停止
				autoplayDisableOnInteraction:false
			});
			//获取后台传来的商品列表
			var shopCategoryList = data.shopCategoryList;
			var categoryHtml='';
			///遍历列表，两两拼接为一行
			shopCategoryList.map(function(item,index){
				categoryHtml+=''+'<div class="col-50 shop-classify" data-category='
				+item.shopCategoryId+'>'+'<div class="word">'
				+'<p class="shop-title">'+item.shopCategoryName
				+'</p>'+'<p class="shop-desc">'
				+item.shopCategoryDesc+'</p>'+'</div>'
				+'<div class="shop-classify-img-warp">'
				+'<img class="shop-img" src="'+item.shopCategoryImg
				+'">'+'</div> </div>';
			});
			//拼接到前端的控件中
			$('.row').html(categoryHtml);
		}
		
	});
	//若点击我的则显示右侧栏
	$('#me').click(function(){
			  $.openPanel("#panel-right-demo");
	});
	//点击类别按钮跳转
	$('.row').on('click','.shop-classify',function(e){
		var shopCategoryId = e.currentTarget.dataset.category;
		var newUrl = '/school-market/frontend/shoplist?parentId='+shopCategoryId;
		window.location.href=newUrl;
		
	});
	
});