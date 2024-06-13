/* admin > create.html 참조 */

let isVerified = false;	// 회원가입 전 메일 인증 확인

/* 인증 메일 전송 */
function send_mail() {
	let receiver = document.getElementById("adm_email").value;
	console.log("----> receiver: " + receiver);

	let adm_email = document.getElementById("adm_email");
	let adm_email_msg = document.getElementById("adm_email_msg");

	// 이메일 미입력
	if (adm_email.value.trim().length == 0) {
		adm_email_msg.innerHTML = "이메일을 입력하세요.";
		adm_email_msg.classList.add("span_warning");
		adm_email.focus();

		return false; // 회원가입 진행 중지
	} else {
		adm_email_msg.classList.remove("span_warning");

		/* fetch 시작 */
		let url = "./mail";
		fetch(url, {
			method: "POST",
			headers: {
				"Content-Type": "application/x-www-form-urlencoded"
			},
			body: new URLSearchParams({
				receiver: receiver
			})
		})
			.then(response => response.json())
			.then(data => {
				if (data.status === "success") {
					alert("인증 메일이 전송되었습니다.");
					console.log("메일 전송 성공, 인증번호 입력창 추가");
					addVerifyField();
				} else {
					alert("ERROR1: " + data.message);
				}
			})
			.catch(error => {
				console.error("ERROR2: " + error);
				alert("ERROR3: " + error);
			})
		/* fetch 종료 */

	}

}


function addVerifyField() {
	console.log("addVerifyField() 호출됨"); // 디버깅 메시지

	// 기존에 생성된 verifyDiv 제거
	let existingVerifyDiv = document.getElementById("verifyDiv");
	if (existingVerifyDiv) {
		existingVerifyDiv.remove();
	}

	let verifyDiv = document.createElement('div');
	verifyDiv.id = "verifyDiv"; // 새로운 div에 ID 추가

	verifyDiv.innerHTML = `
        <label for="verify_code">인증번호:</label>
        <input type="text" id="verify_code" name="verify_code" required>
        <button type="button" onclick="verifyCode();" class="btn btn-secondary btn-sm">인증번호 확인</button>
        <span id="verify_code_msg"></span>
    `;

	let adm_email_msg = document.getElementById("adm_email_msg");
	if (adm_email_msg) {
		adm_email_msg.parentNode.insertBefore(verifyDiv, adm_email_msg.nextSibling);
		console.log("인증번호 입력창 생성");
	}
}


function verifyCode() {
	let adm_email = document.getElementById("adm_email").value;
	let verify_code = document.getElementById("verify_code").value;
	let verify_code_msg = document.getElementById("verify_code_msg");

	let url = "./verifyCode";
	fetch(url, {
		method: "POST",
		headers: {
			"Content-Type": "application/x-www-form-urlencoded"
		},
		body: new URLSearchParams({
			adm_id: adm_email,
			code: verify_code
		})
	})
		.then(response => response.json())
		.then(data => {
			if (data.status === "success") {
				verify_code_msg.innerHTML = "인증번호가 확인되었습니다.";
				verify_code_msg.classList.remove("span_warning");
				verify_code_msg.classList.add("span_info");
				isVerified = true; // 인증번호 확인
			} else {
				verify_code_msg.innerHTML = "인증번호가 일치하지 않습니다.";
				verify_code_msg.classList.remove("span_info");
				verify_code_msg.classList.add("span_warning");
				isVerified = false;	// 인증번호 불일치
			}
		})
		.catch(error => {
			console.error("ERROR2: " + error);
			document.getElementById("verify_code_msg").textContent = "ERROR3: " + error;
		});
}


/* 아이디 중복 확인 */
function checkID() {
	let adm_id = document.getElementById("adm_id");
	let adm_id_msg = document.getElementById("adm_id_msg");

	if (adm_id.value.trim().length == 0) {
		adm_id_msg.innerHTML = "아이디를 입력하세요.";
		adm_id_msg.classList.add("span_warning");
		adm_id.focus();

		return false;	// 회원가입 진행 중지
	} else {
		adm_id_msg.classList.remove("span_warning");

		/* fetch 시작 */
		let url = "./checkID?adm_id=" + adm_id.value;

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
					adm_id_msg.innerHTML = "이미 사용 중인 아이디 입니다.";
					adm_id_msg.classList.remove("span_info");
					adm_id_msg.classList.add("span_warning");
					adm_id.focus();

				} else {	// 존재하지 않은 아이디
					adm_id_msg.innerHTML = "사용 가능한 아이디 입니다.";
					adm_id_msg.classList.remove("span_warning");
					adm_id_msg.classList.add("span_info");
					adm_id.focus();
				}
			})
			.catch(error => {	// 서버 다운 등 통신 에러
				console.error("ERROR:", error);
			});

		/* fetch 종료 */
	}

}

/* 이름 중복 확인 */
function checkName() {
	let adm_name = document.getElementById("adm_name");
	let adm_name_msg = document.getElementById("adm_name_msg");

	if (adm_name.value.trim().length == 0) {
		adm_name_msg.innerHTML = "이름(닉네임)을 입력하세요.";
		adm_name_msg.classList.add("span_warning");
		adm_name.focus();

		return false;	// 회원가입 진행 중지
	} else {
		adm_name_msg.classList.remove("span_warning");

		/* fetch 시작 */
		let url = "./checkName?adm_name=" + adm_name.value;

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
					adm_name_msg.innerHTML = "이미 사용 중인 이름 입니다.";
					adm_name_msg.classList.add("span_warning");
					adm_name.focus();
				} else {	// 존재하지 않은 이름
					adm_name_msg.innerHTML = "사용 가능한 이름 입니다.";
					adm_name_msg.classList.add("span_info");
					document.getElementById("adm_pw").focus();
				}
			})
			.catch(error => {	// 서버 다운 등 통신 에러
				console.error("ERROR:", error);
			});
		/* fetch 종료 */

	}

}

/* 이메일 중복 확인 */
function checkEmail() {
	let adm_email = document.getElementById("adm_email");
	let adm_email_msg = document.getElementById("adm_email_msg");

	if (adm_email.value.trim().length == 0) {
		adm_email_msg.innerHTML = "이메일을 입력하세요.";
		adm_email_msg.classList.add("span_warning");
		adm_email.focus();

		return false;	// 회원가입 진행 중지
	} else {
		adm_email_msg.classList.remove("span_warning");

		/* fetch 시작 */
		let url = "./checkEmail?adm_email=" + adm_email.value;

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
					adm_email_msg.innerHTML = "이미 사용 중인 이메일 입니다.";
					adm_email_msg.classList.remove("span_info");
					adm_email_msg.classList.add("span_warning");
					document.getElementById("btn_sendMail").disabled = true; // 메일 인증 활성화
					adm_email.focus();

				} else {	// 존재하지 않은 아이디
					adm_email_msg.innerHTML = "사용 가능한 이메일 입니다.";
					adm_email_msg.classList.remove("span_warning");
					adm_email_msg.classList.add("span_info");
					document.getElementById("btn_sendMail").disabled = false; // 메일 인증 비활성화
					adm_email.focus();
				}
			})
			.catch(error => {	// 서버 다운 등 통신 에러
				console.error("ERROR:", error);
			});

		/* fetch 종료 */
	}

}

/* 회원 가입 처리 */
function send() {
	let adm_id = document.getElementById("adm_id");
	let adm_id_msg = document.getElementById("adm_id_msg");

	// 아이디 미입력
	if (adm_id.value.trim().length == 0) {
		adm_id_msg.innerHTML = "아이디를 입력하세요.";
		adm_id_msg.classList.add("span_warning");
		adm_id.focus();

		return false; // 회원가입 진행 중지
	}

	// 패스워드 확인
	let adm_pw = document.getElementById("adm_pw");
	let check_pw = document.getElementById("check_pw");
	let check_pw_msg = document.getElementById("check_pw_msg");

	if (adm_pw.value != check_pw.value) {
		check_pw_msg.innerHTML = "입력된 패스워드와 일치하지 않습니다.";
		check_pw_msg.classList.add("span_warning");
		adm_pw.focus();

		return false;	// 회원가입 진행 중지
	}

	// 카테고리 선택 확인
	let select_cate = document.getElementById("select_cate");
	if (select_cate.selectedIndex === 0) {
		alert("카테고리를 선택하세요.");
		select_cate.focus();
		return false; // 회원가입 진행 중지
	}

	// 이름 확인
	let adm_name = document.getElementById("adm_name");
	let adm_name_msg = document.getElementById("adm_name_msg");

	if (adm_name.value.length == 0) {
		adm_name_msg.innerHTML = "이름을 입력하세요.";
		adm_name_msg.classList.add("span_warning");
		adm_name.focus();

		return false;	// 회원가입 진행 중지 
	}

	// 이메일 확인
	let adm_email = document.getElementById("adm_email");
	let adm_email_msg = document.getElementById("adm_email_msg");

	if (adm_email.value.length == 0) {
		adm_email_msg.innerHTML = "이메일을 입력하세요.";
		adm_email_msg.classList.add("span_warning");
		adm_email.focus();

		return false;	// 회원가입 진행 중지 
	}

	// 인증번호 확인 여부
	if (!isVerified) {
		alert("메일을 인증해주세요.");
		return false; // 회원가입 진행 중지
	}

	// 전화번호 확인
	let adm_tel = document.getElementById("adm_tel");
	let adm_tel_msg = document.getElementById("adm_tel_msg");

	if (adm_tel.value.trim().length == 0) {
		adm_tel_msg.innerHTML = "전화번호 입력하세요.";
		adm_tel_msg.classList.add("span_warning");
		adm_tel.focus();

		return false; // 회원가입 진행 중지
	}

	// FormData 생성
	let formData = new FormData();
	formData.append('adm_id', adm_id.value);
	formData.append('adm_pw', adm_pw.value);
	formData.append('adm_name', adm_name.value);
	formData.append('adm_email', adm_email.value);
	formData.append('adm_tel', adm_tel.value);
	formData.append('cate_no', select_cate.value);

	/* fetch 시작 */

	// 회원가입 데이터 전송
	fetch("/admin/create", {
		method: "POST",
		body: formData
	})
		.then(response => response.json())
		.then(data => {
			let cnt = data.cnt;
			let code = data.code;

			window.location.href = "/admin/msg?cnt=" + cnt + "&code=" + code; // 리다이렉션
		})
		.catch(error => {
			console.error("ERROR:", error);
		});
	/* fetch 종료 */


}



window.onload = () => {

}