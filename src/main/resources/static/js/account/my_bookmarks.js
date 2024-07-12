/* my_bookmarks.html 참조 */

function mark(element) {
	let hidden = element.closest('tr').querySelector('input[type="hidden"]');
    let cate_no = hidden.getAttribute('data-cate-no');
    
    let con_no = element.id

	let url = "";
	let currentImg = element.src;

	if (currentImg.endsWith("bookmark_on.png")) {
		url = `/account/deletemark?cate_no=${cate_no}&con_no=${con_no}`;
	} else {
		url = `/account/insertmark?cate_no=${cate_no}&con_no=${con_no}`;
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
				if (currentImg.endsWith("bookmark_off.png")) {
					element.src = "/css/images/bookmark_on.png";
					alert("북마크에 저장되었습니다.");
				} else {
					element.src = "/css/images/bookmark_off.png";
				}
			}
		})
		.catch(error => {
			console.log('Error:', error);
		});
}
