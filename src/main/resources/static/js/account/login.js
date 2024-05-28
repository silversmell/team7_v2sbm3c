/* login.html 참조 */

/* 로그인 처리 */
function send() {
	let acc_id = document.getElementById('acc_id');
	let acc_id_msg = document.getElementById('acc_id_msg');

	if (acc_id.value.trim().length == 0) {
		acc_id_msg.innerHTML = 'ID 입력은 필수 입니다. ID(이메일)는 3자이상 권장합니다.';
		acc_id_msg.classList.add('span_warning');    // class 적용
		acc_pw.focus();

		return false;  // 로그인 진행 중지

	}

	let acc_pw = document.getElementById('acc_pw');
	let acc_pw_msg = document.getElementById('acc_pw_msg');

	if (acc_pw.value.trim().length == 0) {
		acc_pw_msg.innerHTML = '패스워드 입력은 필수 입니다. 패스워드는 8자이상 권장합니다.';
		acc_pw_msg.classList.add('span_warning');    // class 적용
		acc_pw.focus();

		return false;  // 로그인 진행 중지

	}

	document.getElementById('login_frm').submit(); // required="required" 작동 안됨.
}


window.onload = () => {

}