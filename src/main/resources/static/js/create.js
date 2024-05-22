/* create.html 참조 */

/* 아이디(이메일) 중복 확인 */
function checkID() {
	let acc_id = document.getElementById("acc_id");
	let acc_id_msg = document.getElementById("acc_id_msg");

	if (acc_id.value.trim().length == 0) {
		acc_id_msg.innerHTML = "아이디를 입력하세요.";
		acc_id_msg.classList.add("span_warning");
		acc_id.focus();

		return false;	// 회원가입 진행 중지
	} else {
		acc_id_msg.classList.remove("span_warning");

		/* fetch 시작 */
		let url = "./checkID?acc_id=" + acc_id.value;

		fetch(url, {
			method: "GET",
			// headers: {
			//   'Content-Type': 'application/json' // JSON 형식으로 데이터 전송을 알림
			// },
			// body: JSON.stringify(dataToSend) // JSON 데이터를 문자열로 변환하여 요청 본문에 포함      
		})
			.then(response => response.json())
			.then(rdata => {
				if (rdata.cnt > 0) {	// 존재하는 아이디
					acc_id_msg.innerHTML = "이미 사용 중인 이메일 입니다.";
					acc_id_msg.classList.add("span_warning");
					acc_id.focus();
				} else {	// 존재하지 않은 아이디
					acc_id_msg.innerHTML = "사용 가능한 이메일 입니다.";
					acc_id_msg.classList.add("span_info");
					document.getElementById("acc_pw").focus();
				}
			})
			.catch(error => {	// 서버 다운 등 통신 에러
				console.error("ERROR:", error);
			});

		/* fetch 종료 */
	}

}

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
				if (rdata.cnt > 0) {	// 존재하는 이름
					acc_name_msg.innerHTML = "이미 사용 중인 이름 입니다.";
					acc_name_msg.classList.add("span_warning");
					acc_name.focus();
				} else {	// 존재하지 않은 이름
					acc_name_msg.innerHTML = "사용 가능한 이름 입니다.";
					acc_name_msg.classList.add("span_info");
					document.getElementById("acc_pw").focus();
				}
			})
			.catch(error => {	// 서버 다운 등 통신 에러
				console.error("ERROR:", error);
			});
		/* fetch 종료 */

	}

}

/* 해시태그 체크 여부 확인 */
function checkTag() {
	let selected_hashtags = document.querySelectorAll('input[name="selected_hashtags"]:checked');
	let hashtag_msg = document.getElementById("hashtag_msg");

	if (selected_hashtags.length === 0) { // 선택된 해시태그가 없는 경우
		hashtag_msg.innerHTML = "해시태그를 하나 이상 선택해 주세요.";
		hashtag_msg.classList.add("span_warning");
		return false; // 회원가입 진행 중지
	}
	hashtag_msg.classList.remove("span_warning");
	return true; // 선택된 해시태그가 있는 경우 회원가입 진행
}


/* 회원 가입 처리 */
function send() {
	let acc_id = document.getElementById("acc_id");
	let acc_id_msg = document.getElementById("acc_id_msg");

	// 아이디 미입력
	if (acc_id.value.trim().length == 0) {
		acc_id_msg.innerHTML = "아이디를 입력하세요.";
		acc_id_msg.classList.add("span_warning");
		acc_id.focus();

		return false; // 회원가입 진행 중지
	}

	// 패스워드 확인
	let acc_pw = document.getElementById("acc_pw");
	let check_pw = document.getElementById("check_pw");
	let check_pw_msg = document.getElementById("check_pw_msg");

	if (acc_pw.value != check_pw.value) {
		check_pw_msg.innerHTML = "입력된 패스워드와 일치하지 않습니다.";
		check_pw_msg.classList.add("span_warning");
		acc_pw.focus();

		return false;	// 회원가입 진행 중지
	}

	// 이름(닉네임) 확인
	let acc_name = document.getElementById("acc_name");
	let acc_name_msg = document.getElementById("acc_name_msg");

	if (acc_name.value.length == 0) {
		acc_name_msg.innerHTML = "이름(닉네임)을 입력하세요.";
		acc_name_msg.classList.add("span_warning");
		acc_name.focus();

		return false;	// 회원가입 진행 중지 
	}

	// 해시태그 체크 여부 확인
	if (!checkTag()) {	// 선택된 해시태그 없음
		return false; // 회원가입 진행 중지
	}

	// 선택된 해시태그 가져오기
	let selected_hashtags = document.querySelectorAll('input[name="selected_hashtags"]:checked');
	let selected_hashtags_values = Array.from(selected_hashtags).map(checkbox => checkbox.value);

	// FormData 생성
	let formData = new FormData();
	formData.append('acc_id', acc_id.value);
	formData.append('acc_pw', acc_pw.value);
	formData.append('acc_name', acc_name.value);
	formData.append('selected_hashtags', selected_hashtags_values.join(','));

	/* fetch 시작 */
	// 회원가입 데이터 전송
	fetch("/account/create", {
		method: "POST",
		body: formData
	})
		.then(response => response.json())
		.then(data => {
			let cnt = data.cnt;
			let code = data.code;

			window.location.href = "/account/msg?cnt=" + cnt + "&code=" + code; // 리다이렉션
		})
		.catch(error => {
			console.error("ERROR:", error);
		});
	/* fetch 종료 */


}



window.onload = () => {

}