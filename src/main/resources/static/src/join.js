'use strict'

const submit_btn = document.querySelector('#submit_btn');
const inputId = document.querySelector('#memberId');
const inputPw = document.querySelector('#password');
const inputName = document.querySelector('#memberName');
const checkId = document.querySelector('#checkId');
const checkPassword = document.querySelector('#checkPassword');

//정규표현식
const id = /^[a-zA-Z][0-9a-zA-Z]{7,}$/;
const password = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@$!%*#?&]{7,}$/;


//id 유효성 검사
inputId.addEventListener('keyup', () => {
    if (inputId.value == "") {
        changeBorder(inputId);
        changeText(checkId, "필수 입력 항목 입니다");
    } else if (id.test(inputId.value) == false) {
        changeBorder(inputId);
        changeText(checkId, "아이디는 영문+숫자 8자리 이상으로 생성해주세요");
    } else {
        returnBorder(inputId);
        changeText(checkId, "");
    }
})

//pw 유효성 검사
inputPw.addEventListener('keyup', () => {
    if (inputPw.value == "") {
        changeBorder(inputPw);
        changeText(checkPassword, "필수 입력 항목 입니다");
    } else if (password.test(inputPw.value) == false) {
        changeBorder(inputPw);
        changeText(checkPassword, "비밀번호는 영문 대 소문자+숫자+특수기호 8자리 이상으로 생성해주세요");
    } else {
        returnBorder(inputPw);
        changeText(checkPassword, "");
    }
})

//전체 유효성 검사
submit_btn.addEventListener('click', (e) => {
    if (inputId.value == "") {
        e.preventDefault();
        changeBorder(inputId);
        changeText(checkId, "필수 입력 항목 입니다");
        inputId.focus();
        exit;
    } else if (id.test(inputId.value) == false) {
        e.preventDefault();
        changeBorder(inputId);
        changeText(checkId, "아이디는 영문+숫자 8자리 이상으로 생성해주세요");
        inputId.focus();
        exit;
    } else if (inputPw.value == "") {
        e.preventDefault();
        changeBorder(inputPw);
        changeText(checkPassword, "필수 입력 항목 입니다");
        inputPw.focus();
        exit;
    } else if (password.test(inputPw.value) == false) {
        e.preventDefault();
        changeBorder(inputPw);
        changeText(checkPassword, "비밀번호는 영문 대 소문자+숫자+특수기호 8자리 이상으로 생성해주세요");
        inputPw.focus();
        exit;
    }
    // validateDuplicationId().then(result => {
    //     if (result == false) {
    //         e.preventDefault();
    //         changeBorder(inputId);
    //         changeText(checkId, "이미 존재하는 아이디입니다");
    //         inputId.focus();
    //         exit;
    //     }else {
    //         return;
    //     }
    // })


})

//border 함수 추출
function changeBorder(target) {
    target.style.border = "1px solid red";
}
function returnBorder(target) {
    target.style.border = "none";
}

//innerText 함수 추출
function changeText(target, message) {
    target.innerText = `${message}`;
}

//fetch
// async function validateDuplicationId() {
//     const response = await fetch(`/join/checkId/${inputId.value}`);
//     const data = await response.json();
//     return data;
// }