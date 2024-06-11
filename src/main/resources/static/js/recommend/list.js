/* recommend > list.html 참조 */

window.onload = () => {
    let code = document.getElementById("code").getAttribute("data-code");
    
    console.log("code: " + code);
    
    if(code == "login_need") {
		alert("로그인이 필요합니다.");
		window.location.href = '/account/login';
	}
}