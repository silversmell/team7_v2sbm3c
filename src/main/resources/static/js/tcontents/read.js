/* tcontents > read.html 참조 */

window.onload = () => {
    let tcontent = document.getElementById("tcontent");
    tcontent = tcontent.innerHTML;
    
    let content = tcontent.replace(/<br\s*\/?>/gi, '\n');
    content = content.replace(/\n/g, '<br>');

    // 연속된 공백을 &nbsp;로 변환
    content = content.replace(/ {2,}/g, function(match) {
        return match.replace(/ /g, '&nbsp;');
    });
    tcontent.innerHTML = content;
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
