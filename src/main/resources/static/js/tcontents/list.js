/* tcontents > list.html 참조 */

function like(element) {
    let tcon_no = element.id

	let url = "";
	let currentImg = element.src;

	if (currentImg.endsWith("like_off.png")) {
		url = `/tcontents/insertlike?tcon_no=${tcon_no}`;
	} else {
		url = `/tcontents/deletelike?tcon_no=${tcon_no}`;
	}
	
	fetch(url, {
		method: 'GET',
	})
		.then(response => response.json())
		.then(rdata => {
			if (rdata.cnt == 0) {
				alert("로그인이 필요합니다.");
				return;
			}
			if (rdata.cnt > 0) {
				//console.log("우선순위 변경 선공");
				if (currentImg.endsWith("like_off.png")) {
					element.src = "/css/images/like_on.png";
					alert("북마크에 저장되었습니다.");
				} else {
					element.src = "/css/images/like_off.png";
				}
			}
		})
		.catch(error => {
			console.log('Error:', error);
		});
}
