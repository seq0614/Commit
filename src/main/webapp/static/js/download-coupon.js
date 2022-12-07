const downloadButtons = document.querySelectorAll('.btn-secondary');
//console.log(downloadButtons);

for(let i = 0; i < downloadButtons.length; i++){
	downloadButtons[i].addEventListener('click', function(e){
		let couponNumber = e.target.value;
		let downloadCoupon = {CP_IDX : couponNumber}
		
		//console.log("쿠폰 번호는 "+ couponNumber);
		$.ajax({
			type : 'post',
			url : '/coupon/download',
			data: JSON.stringify(downloadCoupon),
			contentType: 'application/json; charset=utf-8',
			datatype : 'text',
			beforeSend : function(XMLHttpRequest){
				XMLHttpRequest.setRequestHeader('AJAX', 'true');
			},
			success : function(message){
				alert(message);
			},
			error : function(error){
				if(error.status == 400){
					alert("로그인이 필요합니다.");
					location.href="/member/login";
				}
			}
			
		});
		
	});
}

