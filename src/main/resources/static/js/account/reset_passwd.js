/* reset_passwd.html 참조 */

function send() {
	/* fetch 시작 */
	let acc_id = document.querySelector('input[name="acc_id"]').value;	// input hidden acc_no 가져왔는지 확인
	let new_passwd = document.getElementById('new_passwd').value;
	let check_passwd = document.getElementById('check_passwd').value;

	//alert("new_passwd: " + new_passwd + "\ncheck_passwd: " + check_passwd);

	if (new_passwd.trim() === "" || check_passwd.trim() === "") {
		alert('비밀번호를 입력해주세요.');
		return;
	}

	if (new_passwd !== check_passwd) {
		alert('비밀번호가 일치하지 않습니다.');
		return;
	}

	fetch('./reset_passwd', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json' // JSON 형식으로 데이터 전송을 알림
		},
		body: JSON.stringify({
			acc_id: acc_id,
			new_passwd: new_passwd
		}) // JSON 데이터를 문자열로 변환하여 요청 본문에 포함   
	})
		.then(response => response.json())
		.then(rdata => {
			console.log("Server response: ", rdata);
			//alert("rdata cnt: " + rdata.cnt);
			if (rdata.cnt != 0) { // 비밀번호 변경 성공
				//alert('비밀번호가 변경되었습니다.');
			    //window.location.href = './login';
			    window.location.href = `./msg?code=${rdata.code}&cnt=${rdata.cnt}`;
			} else {
				alert('비밀번호 변경에 실패하였습니다.');
			}
		})
		.catch(error => { // 서버 다운등 통신 에러
			console.error('Error:', error);
		});

	/* fetch 종료 */
}    