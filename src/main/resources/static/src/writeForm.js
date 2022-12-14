'use strict'

const submit = document.querySelector('#write_btn');
const titleInput = document.querySelector('#boardTitle');
const contentInput = document.querySelector('#boardContent');

submit.addEventListener('click', (e) => {

    if(titleInput.value == "" ) {
        e.preventDefault();
        alert('제목을 입력하세요');
        titleInput.focus();
    }else if(contentInput.value == "") {
        e.preventDefault();
        alert('내용을 입력하세요');
        contentInput.focus();
    }
})