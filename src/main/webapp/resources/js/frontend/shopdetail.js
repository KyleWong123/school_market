/**
 * 
 */
$(function(){
	
	var loading=false;
	//分页允许返回的最大条数，超过此条数禁止访问后台
	var maxItems = 20;
	//一页返回的最大条数
	var pageSize=1;
	//获取商品列表的url
	var listUrl = '/school-market/frontend/listproductsbyshop';
	
	//初始页码
	var pageNum = 1;
	//从地址栏中获取shopId,areaId,shopCategoryId
	var shopId = getQueryString('shopId');
	var productCategoryId = '';
	var productName = '';
	//获取店铺信息以及商品类别信息列表的url
	var searchDivUrl = '/school-market/frontend/listshopdetailpageinfo?shopId='+shopId;
	//渲染出商品类别列表以及店铺基本信息
	getSearchDivData();
	//预先加载2条店铺信息
	addItems(pageSize,pageNum);
	/**
	 * 获取本店铺信息以及商品类别列表
	 */
	
	function getSearchDivData(){
		//如果获取parentId则取出以及列表下的所有二级类别
		var url = searchDivUrl
		$.getJSON(url,function(data){
			if(data.success){
				//获取后台遍历出的店铺类别列表
				var shop = data.shop;
				$('#shop-cover-pic').attr('src',shop.shopImg);
				$('#shop-update-time').html(new Date(shop.lastEditTime).Format("yyyy-MM-dd"));
				$('#shop-name').html(shop.shopName);
				$('#shop-desc').html(shop.shopDesc);
				$('#shop-addr').html(shop.shopAddr);
				$('#shop-phone').html(shop.phone);
				//获取后台返回的该店铺的商品列表
				var productCategoryList = data.productCategoryList;
				var html='';
				//遍历商品列表
				productCategoryList.map(function(item,index){
					html+='<a href="#" class="button" data-product-search-id='
						+item.productCategoryId
						+'>'
						+item.productCategoryName
						+'</a>';
				});
				$('#shopdetail-button-div').html(html);
			}
		});
	}
	/**
	 * 获取分页展示的店铺列表信息
	 */
	function addItems(pageSize,pageIndex){
		var url = listUrl+'?pageIndex='+pageIndex+'&pageSize='+pageSize+'&shopId='+shopId+'&productCategoryId='+productCategoryId+'&productName='+productName;
		//设定加载符，若还在后台取数据则不能再次访问后台，避免多次重复加载
		loading=true;
		$.getJSON(url,function(data){
			if(data.success){
				//获取当前查询店铺的总数
				maxItems=data.count;
				var html='';
				data.productList.map(function(item,index){
					html+=''+'<div class="card" data-product-id="'
					+item.productId
					+'"><div class="card-header">'
					+item.productName+'</div>'
					+'<div class="card-content">'
					+'<div class="list-block media-list"><ul>'
					+'<li class="item-content">'
					+'<div class="item-media">'+'<img src="'
					+item.imgAddr
					+'" width="44"></div>'
					+'<div class="item-inner"><div class="item-subtitle">'
					+item.productDesc
					+'</div></div></li></ul></div></div><div class="card-footer"><p class="color-gray">'
					+new Date(item.lastEditTime).Format("yyyy-MM-dd")
					+'更新</p><span>点击查看</span></div></div>'
				});
				$('.list-div').append(html);
				var total = $('.list-div .card').length;
				if(total>=maxItems){
					
					//隐藏加载提示符
					$('.infinite-scroll-preloader').hide();
				}else{
					$('.infinite-scroll-preloader').show();
				}
				pageNum+=1;
				loading=false;
				//刷子农业面，显示新加载的店铺
				$.refreshScroller();
			}
		});
	}
	//下滑屏幕自动进行分页搜索
	$(document).on('infinite','.infinite-scroll-bottom',function(){
		if(loading)
			return;
			addItems(pageSize,pageNum);
	});
	//点击商品卡片进入详情
	$('.list-div').on('click','.card',function(e){
		var productId = e.currentTarget.dataset.productId;
		window.location.href='/school-market/frontend/productdetail?productId='+productId;
	});
	//选择新的商品类别后重置页码，清空原先的店铺列表，按照新的类别去查询
	$('#shopdetail-button-div').on('click','.button',function(e){

			productCategoryId = e.target.dataset.productSearchId;
			if(productCategoryId){
			if($(e.target).hasClass('button-fill')){
				$(e.target).removeClass('button-fill');
				productCategoryId='';
			}else{
				$(e.target).addClass('button-fill').siblings().removeClass('button-fill');
			}
			//由于查询条件改变，清空店铺列表在查询
			$('.list-div').empty();
			//重置页码
			pageNum=1;
			addItems(pageSize,pageNum);
		}
	});
	
	//需要查询的店铺名字发生变化后，重置页码，重新查询
	$('#search').on('input',function(e){
		productName = e.target.value;
		$('.list-div').empty();
		//重置页码
		pageNum=1;
		addItems(pageSize,pageNum);
	});
	
	
	//若点击我的则显示右侧栏
	$('#me').click(function(){
		$.openPanel("#panel-right-demo");
	});
	
	//初始化页面
	$.init();
});