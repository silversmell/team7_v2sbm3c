/* my_bookmarks.html 참조 */

function mark(element) {
	let con_no = element.id
	let cate_no = 0;

	let url;
	let currentImg = element.src;

	if (currentImg.endsWith("bookmark_on.png")) {
		url = '/account/deletetmark/' + con_no + '?cate_no=' + cate_no;
	} else {
		url = '/account/insertmark/' + con_no + '?cate_no=' + cate_no;
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
