function setValidity(change) {
    change.setCustomValidity('')
    document.querySelector("#length, #width, #height").addEventListener('change', showButton());
}

function showButton() {
    document.getElementById('save').style.visibility='visible';
    document.getElementById('cancel').style.visibility='visible';
    document.getElementById('calculate').style.visibility='hidden';
}

function savedSuccess() {
    alert("Dine ændringer er blevet gemt!");
}

function cancelSuccess() {
    alert("Du har fortrudt dine ændringer.");
}