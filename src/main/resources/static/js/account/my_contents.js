/* my_contents.html 참조 */

document.addEventListener('DOMContentLoaded', function() {
    let scontents = document.querySelectorAll('tr[data-type="share"]');
    let qcontents = document.querySelectorAll('tr[data-type="qna"]');
    let share_title = document.getElementById("share_title");
    let qna_title = document.getElementById("qna_title");
    let cate_no = 1;

    function changeType(selectedType) {
        if (selectedType === 'share') {
            scontents.forEach(function(mark) {
                mark.style.display = '';
            });
            qcontents.forEach(function(mark) {
                mark.style.display = 'none';
            });
            if (share_title) {
                share_title.style.display = '';
            }
            if (qna_title) {
                qna_title.style.display = 'none';
            }
            cate_no = 1;
        } else {
            scontents.forEach(function(mark) {
                mark.style.display = 'none';
            });
            qcontents.forEach(function(mark) {
                mark.style.display = '';
            });
            if (share_title) {
                share_title.style.display = 'none';
            }
            if (qna_title) {
                qna_title.style.display = '';
            }
            cate_no = 2;
        }
        window.cate_no = cate_no;
    }

    document.getElementById('contentsType').addEventListener('change', function() {
        let selectedType = this.value;
        changeType(selectedType);
    });

    changeType('share');
});
