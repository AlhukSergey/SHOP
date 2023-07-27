// Disable form submissions if there are invalid fields
(function () {
    'use strict';
    window.addEventListener('load', function () {
        // Get the forms we want to add validation styles to
        const forms = document.getElementsByClassName('needs-validation');
        // Loop over them and prevent submission
        const validation = Array.prototype.filter.call(forms, function (form) {
            form.addEventListener('submit', function (event) {
                if (form.checkValidity() === false) {
                    event.preventDefault();
                    event.stopPropagation();
                }
                form.classList.add('was-validated');
            }, false);
        });
    }, false);
})();

// Disable submit button if all fields are empty
document.getElementById('loginBtn').disabled = true;

document.getElementById('email').addEventListener('keyup', e => {
    //Check for the input's value
    document.getElementById('loginBtn').disabled = e.target.value === "";
});

document.getElementById('password').addEventListener('keyup', e => {
    //Check for the input's value
    document.getElementById('loginBtn').disabled = e.target.value === "";
});