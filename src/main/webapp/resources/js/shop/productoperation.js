/**
 * 
 */
$(function(){
	//获取productId
	var productId = getQueryString('productId');
	//通过productId获取商品信息的url
	var infoUrl = '/school-market/shopadmin/getproductbyid?productId='+productId;
	//获取当前店铺设定的商品类别列表
	var categoryUrl='/school-market/shopadmin/getproductcategorylist';
	//更新商品信息
	var productPostUrl='/school-market/shopadmin/modifyproduct';
	var isEdit=false;
	if(productId){
		//若有productId则为编辑
		getInfo(productId);
		isEdit=true;
	}else{
		getCategory();
		productPostUrl='/school-market/shopadmin/addproducts';
	}
	//获取需要编辑的商品信息，返回到表单
	function getInfo(id){
		$.getJSON(
		infoUrl,
		function(data){
			if(data.success){
				//从返回的JSON中获取product信息,赋值给html表单
				var product=data.product;
				$('#product-name').val(product.productName);
				$('#product-desc').val(product.productDesc);
				$('#priority').val(product.priority);
				$('#normal-price').val(product.normalPrice);
				$('#promotion-price').val(product.promotionPrice);
				//获取商品类别
				var optionHtml='';
				var optionArr=data.productCategoryList;
				var optionSelected = product.productCategory.productCategoryId;
				//生成前端html商品类别列表，并默认为选择编辑前的商品类别
				optionArr.map(function(item,index){
					var isSelected = optionSelected===item.productCategoryId?'selected'
							:'';
					optionHtml+='<option data-value="'
						+item.productCategoryId
						+'"'
						+isSelected
						+'>'
						+item.productCategoryName
						+'</option>';
				});
				$('#category').html(optionHtml);
			}
		});
	}
	//为商品添加操作提供所有的商品类别列表
	function getCategory(){
		$.getJSON(categoryUrl,function(data){
			if(data.success){
				var productCategoryList=data.data;
				var optionHtml='';
				productCategoryList.map(function(item,index){
					optionHtml+='<option data-value="'
						+item.productCategoryId
						+'">'
						+item.productCategoryName
						+'</option>';
				});
				$('#category').html(optionHtml);
			}
		});
	}
	//针对商品详情控件，若控件组的最后一个上传了照片。切空间数未达到6各，则生成新的控件
	$('.detail-img-div').on('change','.detail-img:last-child',function(){
		if($('.detail-img').length<6){
			$('#detail-img').append('<input type="file" class="detail-img"/>');
		}
	});
	//提交按钮的响应时间，并从表单中获取对应的属性值
	$('#submit').click(function(){
		//创建商品的JSON对象，并从表单中获取对应的属性值
		var product={};
		product.productName=$('#product-name').val();
		product.productDesc=$('#product-desc').val();
		product.priority=$('#priority').val();
		product.normalPrice=$('#normal-price').val();
		product.promotionPrice=$('#promotion-price').val();
		//获取选定的商品类别
		product.productCategory={
				productCategoryId:$('#category').find('option').not(
						function(){
							return !this.selected;
						}).data('value')
		};
		product.productId=productId;
		//获取缩略图文件流
		var thumbnail=$('#small-img')[0].files[0];
		//生成表单对象，用于接收参数并传给后台
		var formData = new FormData();
		formData.append('thumbnail',thumbnail);
		//遍历商品详情图控件
		$('.detail-img').map(function(index,item){
			//判断该控件是否选择了文件
			if($('.detail-img')[index].files.length>0){
				//将第i各文件流赋值给key为productImg的表单键值对中
				formData.append('productImg'+index,$('.detail-img')[index].files[0]);
			}
		});
		//将product json对象转换为productStr
		formData.append('productStr',JSON.stringify(product));
		//获取验证码
		var verifyCodeActual=$('#j_captcha').val();
		if(!verifyCodeActual){
			$.toast('请输入验证码');
			return;
		}
		formData.append('verifyCodeActual',verifyCodeActual);
		$.ajax({
			url:productPostUrl,
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
});