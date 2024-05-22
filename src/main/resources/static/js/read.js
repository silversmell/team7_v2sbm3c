/* read.html 참조 */


/* 이름(닉네임) 중복 확인 */
function checkName() {
	let acc_name = document.getElementById("acc_name");
	let acc_name_msg = document.getElementById("acc_name_msg");

	if (acc_name.value.trim().length == 0) {
		acc_name_msg.innerHTML = "이름(닉네임)을 입력하세요.";
		acc_name_msg.classList.add("span_warning");
		acc_name.focus();

		return false;	// 회원가입 진행 중지
	} else {
		acc_name_msg.classList.remove("span_warning");

		/* fetch 시작 */
		let url = "./checkName?acc_name=" + acc_name.value;

		fetch(url, {
			method: "GET",
			// headers: {
			//   'Content-Type': 'application/json' // JSON 형식으로 데이터 전송을 알림
			// },
			// body: JSON.stringify(dataToSend) // JSON 데이터를 문자열로 변환하여 요청 본문에 포함      
		})
			.then(response => response.json())
			.then(rdata => {
				if (rdata.cnt > 1) {	// 존재하는 이름
					acc_name_msg.innerHTML = "이미 사용 중인 이름 입니다.";
					acc_name_msg.classList.add("span_warning");
					acc_name.focus();
				} else {	// 존재하지 않은 이름
					acc_name_msg.innerHTML = "사용 가능한 이름 입니다.";
					acc_name_msg.classList.add("span_info");
				}
			})
			.catch(error => {	// 서버 다운 등 통신 에러
				console.error("ERROR:", error);
			});
		/* fetch 종료 */

	}

}


/* 회원 정보 수정 */
function send() {
	let acc_name = document.getElementById('acc_name');
	let acc_name_msg = document.getElementById('acc_name_msg');

	// 이름 미입력
	if (acc_name.value.length == 0) {
		acc_name_msg.innerHTML = '이름(닉네임)을 입력하세요.';
		acc_name_msg.classList.add('span_warning');
		acc_name.focus();

		return false;
	}

	document.getElementById('update_frm').submit(); // required="required" 작동 안 됨
}    