/* admin > acc_list.html 참조 */

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

// 현재 OracleDB TIMESTAMP 시간대는 ASIA/SEOUL 이므로 사용 X
// <form id="search_frm" name="search_frm" method="get" action="/account/log_list" onsubmit="return handleFormSubmit()">
/*
function formatDateToUTC(date) { // UTC로 변환
	alert("기존: " + date)
	const offset = date.getTimezoneOffset();
	const utcDate = new Date(date.getTime() + (offset * 60 * 1000));
	alert("바꿈: " + utcDate.toISOString().split('T')[0])
	return utcDate.toISOString().split('T')[0];
}

function handleFormSubmit() {
	const search_frm = document.getElementById('search_frm');
	const startDate = new Date(document.getElementById('start_date').value);
	const endDate = new Date(document.getElementById('end_date').value);

	const startDateUTC = formatDateToUTC(startDate);
	const endDateUTC = formatDateToUTC(endDate);

	search_frm.start_date.value = startDateUTC;
	search_frm.end_date.value = endDateUTC;

	return true;
}
*/
