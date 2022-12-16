'use strict'

const newContent = document.querySelector('.repl_input');
const submitRepl = document.querySelector('.submitRepl');
const deleteBtn = document.querySelector('.repl_delete-btn');
let boardNum = document.querySelector('.boardNum').value;



const reply_items = document.querySelector('.reply_items');

//댓글 저장 api 통신
async function saveReples(boardNum, newContent) {
    const rs = await fetch(`/boardDetail/${boardNum}/saveRepl`, {
        method: "POST",
        body: "reContent=" + newContent.value,
        headers: { 'Content-type': 'application/x-www-form-urlencoded' }
    });
    const data = await rs.json();
    return data;
}
//댓글 삭제 api 통신
async function deleteReples(boardNum, reId) {
    const rs = await fetch(`/boardDetail/${boardNum}/deleteRe/${reId}`, {
        method: "POST",
        redirect: "follow"
    });
}

//댓글 삭제 onclick
function deleteRepl(boardNum, reId) {
    deleteReples(boardNum, reId);
    document.querySelector(`#repl_${reId}`).remove();
}

//댓글 저장
submitRepl.addEventListener('click', () => {
    saveReples(boardNum, newContent).then(reple => {
        const reply_item = document.createElement('div');
        reply_item.setAttribute('id',`repl_${reple.reId}`);
        reply_item.classList.add('reply_item');
        reply_items.appendChild(reply_item);
        const reIdInput = document.createElement('input');
        reIdInput.classList.add('reIdInput');
        reIdInput.setAttribute('type', 'hidden');
        reIdInput.setAttribute('value', `${reple.reId}`);
        reply_item.appendChild(reIdInput);
        const writer = document.createElement('span');
        writer.classList.add('repl_writer');
        writer.innerText = reple.reWriter;
        reply_item.appendChild(writer);
        const content = document.createElement('span');
        content.classList.add('repl_content');
        content.innerText = reple.reContent;
        reply_item.appendChild(content);
        const date = document.createElement('span');
        date.classList.add('repl_date');
        date.innerText = reple.reDate;
        reply_item.appendChild(date);
        const deleteBtn = document.createElement('a');
        deleteBtn.setAttribute('onclick',`deleteRepl(${boardNum},${reple.reId})`);
        deleteBtn.classList.add('repl_delete-btn');
        deleteBtn.innerText = 'X';
        reply_item.appendChild(deleteBtn);
    }).catch(console.log);
    newContent.value="";

})

