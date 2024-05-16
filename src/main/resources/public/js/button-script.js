function setValidityRequest(change) {
    change.setCustomValidity('')
    document.querySelector("#length, #width, #height").addEventListener('change', showButtons1());
}

function setValidityOffer(change) {
    change.setCustomValidity('')
    document.querySelector("#material-price, #new-price, #instruction").addEventListener('change', showButtons2());
}

function showButtons2() {
    document.getElementById('save').style.visibility='visible';
    document.getElementById('send').style.visibility='hidden';
    document.getElementById('description').style.visibility='hidden';
    document.getElementById('drawing').style.visibility='hidden';
}

function showButtons1() {
    document.getElementById('save').style.visibility='visible';
    document.getElementById('cancel').style.visibility='visible';
    document.getElementById('calculate').style.visibility='hidden';
}

function cancelSuccess() {
    alert("Du har fortrudt dine ændringer.");
}