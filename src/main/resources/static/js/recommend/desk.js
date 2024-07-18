window.onload = () => {
	send.addEventListener("click", async function() {
		let send = document.getElementById("send");
		let close = document.getElementById('close');

		// let tags = document.getElementsByName("recommend");
		let tags = document.querySelectorAll("[name='recommend']"); // 여러개의 태그 검색
		let values = []; // 서버로 전달할 값 저장
		// console.log('-> ' + tags.length);

		let pick_count = 0; // 선택한 이미지 카운터
		for (let i = 0; i < tags.length; i++) { // 25번 순환
			let tag = tags[i] // 태그 추출
			let pick = tag.getAttribute("data-value"); // 0 또는 1, 이미지 선택 여부
			// console.log('pick: ' + pick);

			if (pick == 1) { // 선택한 이미지이면 카운터 증가
				let pick_filename = tag.getAttribute("data-filename");
				values.push(pick_filename); // 태그의 기본값은 0이나 이미지 선택시 1로 변경된 값이 할당됨.
				pick_count = pick_count + 1;
			}
		}

		//alert("서버 전달: " + values);
		//console.log(values);
		//return;

		if (pick_count == 8) { // 이미지가 5개 선택되면 다음으로 자동 진행
			// console.log(values);
			let desk = values.join(","); // 배열의 값을 ','로 연결
			console.log('-> desk: ' + desk)

			let acc_no = document.getElementById("acc_no").value;
			document.getElementById("processing").innerHTML = '<img src="/static/images/progress.gif" style="width: 3%; margin-top: 10px;">';

			// await: 처리가 끝날때까지 대기 상태로 진입, 다른 기능이 실행 정지됨.
			await fetch("http://192.168.12.157:5000/recommend/desk", {
				"method": "post",
				"headers": {
					"Content-Type": "application/json"
				},
				body: JSON.stringify({ desk, acc_no }) // {"desk":desk}
			})
				.then((response) => response.json())
				.then((data) => {
					console.log(data);
					// result.innerHTML = data; // 응답 문자열 출력, json 객체
					// result.innerHTML = data['res']; // 응답 문자열 출력
					result.innerHTML = '추천에 응해 주셔서 감사합니다.';
					document.getElementById("processing").innerHTML = ""; // animation 삭제

					send.style.display = 'none';
					close.style.display = 'block';

				});
		} else if (pick_count > 8) { // 이미지가 8개 이상 선택되면 메시지 출력
			document.getElementById("processing").innerHTML = "<br>이미지 선택은 5개만 가능합니다.<br>";
		} else {
			document.getElementById("processing").innerHTML = "<br>이미지 선택이 부족합니다.<br>";
		}
	});
}

function applyCSS(event) {  // 이벤트 정보를 가지고 있음.
	const img = event.target; // event: click을 한 태그
	const data_selected_panel = document.getElementById('data_selected_panel');

	let sw = img.getAttribute("data-value");   // 값 추출, data-: 개발자가 추가한 속성
	let data_selected = parseInt(data_selected_panel.getAttribute("data-selected"));

	if (data_selected >= 8 && sw == 0) {
		alert('이미지는 최대 8장까지 선택할 수 있습니다.');
	} else {
		img.classList.toggle('selected'); // class 토글 효과, selected가 없으면 적용, 있으면 해제 ★
		img.classList.toggle('brighten'); // class 토글 효과 

		if (sw == 0) { // 반전 효과
			img.setAttribute("data-value", 1); // 선택
			data_selected = data_selected + 1;
		} else {
			img.setAttribute("data-value", 0);
			data_selected = data_selected - 1;
		}

		data_selected_panel.setAttribute("data-selected", data_selected); // 변경된 카운트 기록
		data_selected_panel.innerHTML = ' 현재 ' + data_selected + '개 이미지 선택';

		document.getElementById("result").innerHTML = "";     // 추천 분야 출력
		document.getElementById("processing").innerHTML = ""; // 처리중 animation 삭제
	}
}
