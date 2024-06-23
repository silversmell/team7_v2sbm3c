/* admin > list_search_component.html 참조 */

document.addEventListener('DOMContentLoaded', function() {
	/* 날짜 검색 유효성 검사 */
	document.getElementById('search_frm').addEventListener('submit', function(event) {
		var start_date = document.getElementById('start_date').value;
		var end_date = document.getElementById('end_date').value;

		if (start_date && !end_date) {
			alert('조회할 기간의 마지막 날짜를 입력해주세요.');
			event.preventDefault(); // submit 중지
		} else if (!start_date && end_date) {
			alert('조회할 기간의 시작 날짜를 입력해주세요.');
			event.preventDefault(); // submit 중지
		}
	});
});
