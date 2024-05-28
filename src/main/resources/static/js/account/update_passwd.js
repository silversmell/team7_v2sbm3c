/* update_passwd.html 참조 */

function send() {
	/* fetch 시작 */
	let url = './check_passwd';
	let current_passwd = document.getElementById("current_passwd").value;

	fetch(url, {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json' // JSON 형식으로 데이터 전송을 알림
		},
		body: JSON.stringify({ current_passwd: current_passwd }) // JSON 데이터를 문자열로 변환하여 요청 본문에 포함   
	})
		.then(response => response.json())
		.then(rdata => {
			if (rdata.cnt == 0) { // 패스워드 불일치
				alert('현재 비밀번호가 일치하지 않습니다.');
				current_passwd.focus();
				return false;
			} else {
				if (document.getElementById('new_passwd').value.trim().length == 0) {
					alert('비밀번호를 입력해주세요.');
					document.getElementById('new_passwd').focus();
					return false;  // 비밀번호 변경 중지
				}

				if (document.getElementById('check_passwd').value.trim().length == 0) {
					alert('비밀번호를 입력해주세요.');
					document.getElementById('check_passwd').focus();
					return false;  // 비밀번호 변경 중지
				}

				// 새로운 패스워드 입력 확인
				// 패스워드를 정상적으로 2번 입력했는지 확인
				let new_passwd = document.getElementById('new_passwd');
				let check_passwd = document.getElementById('check_passwd');

				if (new_passwd.value != check_passwd.value) {
					alert('새로운 비밀번호가 일치하지 않습니다.');
					new_passwd.focus();

					return false;  // 비밀번호 변경 중지
				}

				document.getElementById('update_passwd_frm').submit();
			}
		})
		.catch(error => { // 서버 다운등 통신 에러
			console.error('Error:', error);
		});

	/* fetch 종료 */
}    