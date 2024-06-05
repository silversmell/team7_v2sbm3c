/* read.html 참조 */

/* 이미지 선택 여부 확인 */
function checkImg() {
	let acc_img_mf = document.getElementById("acc_img_mf");
	let acc_img_msg = document.getElementById("acc_img_msg");
	if (acc_img_mf.files.length === 0) {
		acc_img_msg.innerHTML = "사진을 선택해주세요.";
		acc_img_msg.classList.add("span_warning");

		return false;
	}
}

/* 이름(닉네임) 중복 확인 */
let check_name = null;

function checkName() {
	let my_acc_name = document.getElementById("my_acc_name").value;
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
					if (my_acc_name != acc_name.value) {
						acc_name_msg.innerHTML = "이미 사용 중인 이름 입니다.";
						acc_name_msg.classList.add("span_warning");
						acc_name.focus();
						
						check_name = false;
					} else {
						acc_name_msg.innerHTML = "현재 사용 중인 이름 입니다.";
						acc_name_msg.classList.remove("span_warning");

						check_name = true;
					}
				} else {	// 존재하지 않은 이름
					acc_name_msg.innerHTML = "사용 가능한 이름 입니다.";
					acc_name_msg.classList.remove("span_warning");

					check_name = true;
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


/* 회원 정보 수정 */
function send() {
	let my_acc_name = document.getElementById("my_acc_name").value;
	let acc_name = document.getElementById('acc_name');
	let acc_name_msg = document.getElementById('acc_name_msg');

	// 이름 미입력
	if (acc_name.value.length == 0) {
		acc_name_msg.innerHTML = '이름(닉네임)을 입력하세요.';
		acc_name_msg.classList.add('span_warning');
		acc_name.focus();

		return false;
	}

	// 해시태그 체크 여부 확인
	if (!checkTag()) {	// 선택된 해시태그 없음
		return false; // 회원가입 진행 중지
	}

	// 이름 중복 확인
	if (check_name == null) {
		if (my_acc_name != acc_name.value) {
			alert("이름의 중복 확인이 필요합니다.");
			return false; // 회원 가입 중지
		}
	} else if (!check_name) {
		alert("이미 사용 중인 이름 입니다.")
		return false; // 회원가입 진행 중지
	}

	document.getElementById('update_frm').submit(); // required="required" 작동 안 됨
}

