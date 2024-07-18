/* my_bookmarks.html 참조 */

document.addEventListener('DOMContentLoaded', function() {
	let share_marks = document.querySelectorAll('tr[data-type="share"]');
	let qna_marks = document.querySelectorAll('tr[data-type="qna"]');
	let share_title = document.getElementById("share_title");
	let qna_title = document.getElementById("qna_title");
	let cate_no = 1;

	function changeType(selectedType) {
		if (selectedType === 'share') {
			share_marks.forEach(function(mark) {
				mark.style.display = '';
			});
			qna_marks.forEach(function(mark) {
				mark.style.display = 'none';
			});
			share_title.style.display = '';
			qna_title.style.display = 'none';
			cate_no = 1;
		} else {
			share_marks.forEach(function(mark) {
				mark.style.display = 'none';
			});
			qna_marks.forEach(function(mark) {
				mark.style.display = '';
			});
			share_title.style.display = 'none';
			qna_title.style.display = '';
			cate_no = 2;
		}
		window.cate_no = cate_no;
	}

	document.getElementById('contentsType').addEventListener('change', function() {
		let selectedType = this.value;
		changeType(selectedType);
	});

	changeType('share');
});


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
