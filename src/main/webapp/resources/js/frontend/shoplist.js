/**
 * 
 */
$(function(){
	
	var loading=false;
	//分页允许返回的最大条数，超过此条数禁止访问后台
	var maxItems = 999;
	//一页返回的最大条数
	var pageSize=2;
	//获取店铺列表的url
	var listUrl = '/school-market/frontend/listshops';
	//获取店铺类别列表以及区域列表的url
	var searchDivUrl = '/school-market/frontend/listshoppageinfo';
	//初始页码
	var pageNum = 1;
	//从地址栏中获取parentId,shopId,areaId,shopCategoryId
	var parentId = getQueryString('parentId');
	var areaId = '';
	var shopCategoryId='';
	var shopName = '';
	//渲染出店铺类别列表以及区域列表
	getSearchDivData();
	//预先加载10条店铺信息
	addItems(pageSize,pageNum);
	/**
	 * 获取店铺类别列表以及区域信息
	 */
	
	function getSearchDivData(){
		//如果获取parentId则取出以及列表下的所有二级类别
		var url = searchDivUrl+'?'+'parentId='+parentId;
		$.getJSON(url,function(data){
			if(data.success){
				//获取后台遍历出的店铺类别列表
				var shopCategoryList = data.shopCategoryList;
				var html='';
				html+='<a href="#" class="button" data-category-id="">全部类别</a>';
				//遍历店铺类别列表
				shopCategoryList.map(function(item,index){
					html+='<a href="#" class="button" data-category-id='
						+item.shopCategoryId
						+'>'
						+item.shopCategoryName
						+'</a>';
				});
				$('#shoplist-search-div').html(html);
				var selectOptions = '<option value="">全部街道</option>';
				//获取后台传来的区域信息
				var areaList = data.areaList;
				areaList.map(function(item,index){
					selectOptions+='<option value="'
						+item.areaId
						+'">'
						+item.areaName
						+'</option>';
				});
				$('#area-search').html(selectOptions);
			}
		});
	}
	/**
	 * 获取分页展示的店铺列表信息
	 */
	function addItems(pageSize,pageIndex){
		var url = listUrl+'?pageIndex='+pageIndex+'&pageSize='+pageSize+'&parentId='+parentId+'&areaId='
		+areaId+'&shopCategoryId='+shopCategoryId+'&shopName='+shopName;
		//设定加载符，若还在后台取数据则不能再次访问后台，避免多次重复加载
		loading=true;
		$.getJSON(url,function(data){
			if(data.success){
				//获取当前查询店铺的总数
				maxItems=data.count;
				var html='';
				data.shopList.map(function(item,index){
					html+=''+'<div class="card" data-shop-id="'
					+item.shopId
					+'"><div class="card-header">'
					+item.shopName+'</div>'
					+'<div class="card-content">'
					+'<div class="list-block media-list"><ul>'
					+'<li class="item-content">'
					+'<div class="item-media">'+'<img src="'
					+item.shopImg
					+'" width="44"></div>'
					+'<div class="item-inner"><div class="item-subtitle">'
					+item.shopDesc
					+'</div></div></li></ul></div></div><div class="card-footer"><p class="color-gray">'
					+new Date(item.lastEditTime).Format("yyyy-MM-dd")
					+'更新</p><span>点击查看</span></div></div>'
				});
				$('.list-div').append(html);
				var total = $('.list-div .card').length;
				if(total>=maxItems){
					
					//删除加载提示符
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
	//点击店铺卡片进入详情
	$('.shop-list').on('click','.card',function(e){
		var shopId = e.currentTarget.dataset.shopId;
		window.location.href='/school-market/frontend/shopdetail?shopId='+shopId;
	});
	//选择新的店铺后重置页码，清空原先的店铺列表，按照新的类别去查询
	$('#shoplist-search-div').on('click','.button',function(e){
		if(parentId){
			shopCategoryId = e.target.dataset.categoryId;
			if($(e.target).hasClass('button-fill')){
				$(e.target).removeClass('button-fill');
				shopCategoryId='';
			}else{
				$(e.target).addClass('button-fill').siblings().removeClass('button-fill');
			}
			//由于查询条件改变，清空店铺列表在查询
			$('.list-div').empty();
			//重置页码
			pageNum=1;
			addItems(pageSize,pageNum);
		}else{
			parentId=e.target.dataset.categoryId;
			if($(e.target).hasClass('button-fill')){
				$(e.target).removeClass('button-fill');
				parentId='';
			}else{
				$(e.target).addClass('button-fill').siblings().removeClass('button-fill');
			}
			$('.list-div').empty();
			//重置页码
			pageNum=1;
			addItems(pageSize,pageNum);
			parentId='';
		}
	});
	
	//需要查询的店铺名字发生变化后，重置页码，重新查询
	$('#search').on('change',function(e){
		shopName = e.target.value;
		$('.list-div').empty();
		//重置页码
		pageNum=1;
		addItems(pageSize,pageNum);
	});
	
	//需要查询的区域信息发生变化后，重置页码，重新查询
	$('#area-search').on('change',function(e){
		areaId = $('#area-search').val();
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