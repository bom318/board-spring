'use strict'

const newContent = document.querySelector('.repl_input');
const submitRepl = document.querySelector('.submitRepl');
let boardNum = document.querySelector('.boardNum').value;

const reply_items = document.querySelector('.reply_items');

async function getReples(boardNum) {
    const rs = await fetch(`/boardDetail/${boardNum}/getRepl`);
    const data = await rs.json();

    return data;
}
async function saveReples(boardNum,newContent) {
    const rs = await fetch(`/boardDetail/${boardNum}/saveRepl`, {
        method: "POST",
        body: "reContent=" + newContent.value,
        headers: {'Content-type': 'application/x-www-form-urlencoded'}
    });
}
async function deleteReples(boardNum, reId) {

}
submitRepl.addEventListener('click', () => {
    saveReples(boardNum,newContent);
    reply_items.innerHTML="";
    getReples(boardNum).then(reples => {
        reples.forEach(reple => {
            console.log(reple.reWriter)
            console.log(reple.reContent)
            console.log(reple.reDate)
    
            const reply_item = document.createElement('div');
            reply_item.classList.add('reply_item');
            reply_items.appendChild(reply_item);
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
            deleteBtn.classList.add('repl_delete-btn');
            deleteBtn.innerText = 'X';
            reply_item.appendChild(deleteBtn);
    
        });
    }).catch(console.log);
    
})

window.addEventListener('load', () => {
    repleLoad;
})

//댓글 로드
const repleLoad = getReples(boardNum).then(reples => {
    reples.forEach(reple => {
        console.log(reple.reWriter)
        console.log(reple.reContent)
        console.log(reple.reDate)

        const reply_item = document.createElement('div');
        reply_item.classList.add('reply_item');
        reply_items.appendChild(reply_item);
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
        deleteBtn.classList.add('repl_delete-btn');
        deleteBtn.innerText = 'X';
        reply_item.appendChild(deleteBtn);

    });
}).catch(console.log);

