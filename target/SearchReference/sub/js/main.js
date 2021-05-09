//функция вставки ссылки в value input
function insert(obj) {
    let urlId = obj.id.match('j_idt[0-9]+:[0-9]+:') + 'addUrl';
    document.getElementById('inputform:input').value = document.getElementById(urlId);
}

function blackout() {
    document.getElementById('body').classList.add('overlay');
}

function lighten() {
    document.getElementById('body').classList.remove('overlay');
}