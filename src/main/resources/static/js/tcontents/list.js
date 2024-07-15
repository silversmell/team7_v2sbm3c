/* tcontents > list.html 참조 */

window.onload = () => {
    let item_content = document.querySelectorAll('.gallery-item-content span');
    
    item_content.forEach(span => {
        let text = span.innerText;
        text = text.replace(/<br\s*\/?>/gi, ' ');
        span.innerText = text;
    });
};


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
					window.location.reload();
				} else {
					element.src = "/css/images/like_off.png";
					window.location.reload();
				}
			}
		})
		.catch(error => {
			console.log('Error:', error);
		});
}
