/**
 * 
 */
$(function(){
	//获取shopId
	var shopId = getQueryString('shopId');
	//若果有shopId则为修改店铺信息，否则为注册店铺信息
	var isEdit=shopId?true:false;
	var initUrl='/school-market/shopadmin/getshopinitinfo';
	var registershopUrl ='/school-market/shopadmin/registershop';
	var shopInfoUrl="/school-market/shopadmin/getshopbyid?shopId="+shopId;
	var editShopUrl='/school-market/shopadmin/modifyshop';
	//getShopInitInfo();
/*	alert(initUrl);
 * 
*/	
		//若果为isEdit修改店铺信息，否则为注册店铺信息
		if(!isEdit){
			getShopInitInfo();
		}else{
			getShopInfo(shopId);
		}
		
	function getShopInfo(shopId){
		$.getJSON(shopInfoUrl,function(data){
			if(data.success){
			var shop =data.shop;
			$('#shop-id').val(shop.shopId);
			$('#shop-name').val(shop.shopName);
			$('#shop-addr').val(shop.shopAddr);
			$('#shop-phone').val(shop.phone);
			$('#shop-desc').val(shop.shopDesc);
			var shopCategory='<option data-id="'
				+shop.shopCategory.shopCategoryId+'"selected>'
			+shop.shopCategory.shopCategoryName+'</option>';
			var tempAreaHtml='';
			data.areaList.map(function(item,index){
				tempAreaHtml+='<option data-id="'+item.areaId+'">'
				+item.areaName+'</option>';
			});
		$('#shop-category').html(shopCategory);
		$('#shop-category').attr('disabled','disabled');//店铺分类不能修改
		$('#shop-area').html(tempAreaHtml);
		$("#shop-area option[data-id='"+shop.area.areaId+"']").attr("selected","selected");
			}
		});
	}
	function getShopInitInfo(){
		$.getJSON(initUrl,function(data){
			if(data.success){
				var tempHtml='';
				var tempAreaHtml='';
				data.shopCategoryList.map(function(item,index){
					tempHtml += '<option data-id="'+item.shopCategoryId+'">'
					+item.shopCategoryName+'</option>';
					});
				data.areaList.map(function(item,index){
					tempAreaHtml+='<option data-id="'+item.areaId+'">'
					+item.areaName+'</option>';
				});
				$('#shop-category').html(tempHtml);
				$('#shop-area').html(tempAreaHtml);
				
			}
		});
	}
		$('#submit').click(function(){
			var shop={};
			if(isEdit){
				shop.shopId=shopId;
			}
			shop.shopName=$('#shop-name').val();
			shop.shopAddr = $('#shop-addr').val();
			shop.phone = $('#shop-phone').val();
			shop.shopDesc = $('#shop-desc').val();
/*			shop.shopId = $('#shop-id').val();
*/			shop.shopCategory={
					shopCategoryId:$('#shop-category').find('option').not(function(){
						return !this.selected;
					}).data('id')
			};
			shop.area={
					areaId:$('#shop-area').find('option').not(function(){
						return !this.selected;
					}).data('id')
			};
			var shopImg=$('#shop-img')[0].files[0];
			var formData = new FormData();
			formData.append('shopImg',shopImg);
			formData.append('shopStr',JSON.stringify(shop));
			var verifyCodeActual=$('#j_captcha').val();
			if(!verifyCodeActual){
				$.toast('请输入验证码');
				return;
			}
			formData.append('verifyCodeActual',verifyCodeActual);
			$.ajax({
			 	url:(isEdit?editShopUrl:registershopUrl),//若果为isEdit修改店铺信息，否则为注册店铺信息
				//url:registershopUrl,
				type:'POST',
				data:formData,
				contentType:false,
				processData:false,
				cache:false,
				success:function(data){
					if(data.success){
						$.toast('提交成功');
					}
					else{
						$.toast('提交失败'+data.errMsg);
					}
					$('#captcha_img').click();
				}
			
			});
		
		});
})
			