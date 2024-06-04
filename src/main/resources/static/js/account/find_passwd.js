/* create.html 참조 */

let isVerified = false;	// 회원가입 전 메일 인증 확인

/* 아이디(이메일) 존재 여부 확인 */
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
					acc_id_msg.innerHTML = "입력하신 아이디가 존재합니다.";
					acc_id_msg.classList.remove("span_warning");
					acc_id_msg.classList.add("span_info");
					addResetBtnField();

				} else {	// 존재하지 않은 아이디
					acc_id_msg.innerHTML = "존재하지 않은 아이디입니다.";
					acc_id_msg.classList.remove("span_info");
					acc_id_msg.classList.add("span_warning");

					// 기존에 생성된 resetBtn 제거
					let existingResetBtn = document.getElementById("reset_btn");
					if (existingResetBtn) {
						existingResetBtn.remove();
					}

				}
			})
			.catch(error => {	// 서버 다운 등 통신 에러
				console.error("ERROR:", error);
			});

		/* fetch 종료 */
	}

}

/* 인증 메일 전송 */
function send_mail() {
	let receiver = document.getElementById("acc_id").value;
	console.log("----> receiver: " + receiver);

	let acc_id = document.getElementById("acc_id");
	let acc_id_msg = document.getElementById("acc_id_msg");

	// 아이디 미입력
	if (acc_id.value.trim().length == 0) {
		acc_id_msg.innerHTML = "아이디를 입력하세요.";
		acc_id_msg.classList.add("span_warning");
		acc_id.focus();

		return false; // 회원가입 진행 중지
	} else {
		acc_id_msg.innerHTML = "";
		acc_id_msg.classList.remove("span_info");
		acc_id_msg.classList.remove("span_warning");

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
					alert(receiver + "로 인증번호가 전송되었습니다.");
					console.log("메일 전송 성공, 인증번호 입력창 추가");

					addVerifyField(acc_id.value);
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



function addResetBtnField() {
	// 기존에 생성된 resetBtn 제거
	let existingResetBtn = document.getElementById("reset_btn");
	if (existingResetBtn) {
		existingResetBtn.remove();
	}

	let resetBtn = document.createElement('button');
	resetBtn.type = "button";
	resetBtn.id = "reset_btn";
	resetBtn.className = "btn btn-secondary btn-sm";
	resetBtn.innerText = "비밀번호 재설정";
	resetBtn.onclick = send_mail;
	/* resetBtn.onclick = function() {
		location.href = './reset_passwd';
	}; */

	let btnDiv = document.getElementById("btnDiv");
	if (btnDiv) {
		btnDiv.insertBefore(resetBtn, btnDiv.firstChild);
		console.log("버튼 DIV 생성");
	}

}

function addVerifyField(acc_id) {
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
        <button type="button" onclick="verifyCode('${acc_id}')" class="btn btn-secondary btn-sm">인증번호 확인</button>
        <span id="verify_code_msg"></span>
    `;

	let acc_id_msg = document.getElementById("acc_id_msg");
	if (acc_id_msg) {
		acc_id_msg.parentNode.insertBefore(verifyDiv, acc_id_msg.nextSibling);
		console.log("인증번호 입력창 생성");
	}
}




function verifyCode() {
	let acc_id = document.getElementById("acc_id").value;
	let verify_code = document.getElementById("verify_code").value;
	let verify_code_msg = document.getElementById("verify_code_msg");

	let url = "./verifyCode";
	fetch(url, {
		method: "POST",
		headers: {
			"Content-Type": "application/x-www-form-urlencoded"
		},
		body: new URLSearchParams({
			acc_id: acc_id,
			code: verify_code
		})
	})
		.then(response => response.json())
		.then(data => {
			if (data.status === "success") {
				alert("인증번호가 확인되었습니다.");
				// verify_code_msg.classList.remove("span_warning");
				// verify_code_msg.classList.remove("span_info");
				isVerified = true; // 인증번호 확인
				postToResetPasswd(acc_id);

				//window.location.href = `./reset_passwd?acc_id=${acc_id}`;
				//window.location.href = './reset_passwd';
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

/* POST 방식으로 reset_passwd 페이지로 이동 */
function postToResetPasswd(acc_id) {
	let form = document.createElement('form');
	form.method = 'POST';
	form.action = './find_passwd'

	let hiddenField = document.createElement('input');
	hiddenField.type = 'hidden';
	hiddenField.name = 'acc_id';
	hiddenField.value = acc_id;

	form.appendChild(hiddenField);
	document.body.appendChild(form);
	form.submit();
}



window.onload = () => {

}