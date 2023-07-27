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
/*document.getElementById('formNameBtn').disabled = true;
document.getElementById('formSurnameBtn').disabled = true;
document.getElementById('formBirthdayBtn').disabled = true;
document.getElementById('formEmailBtn').disabled = true;*/
document.getElementById('formPassBtn').disabled = true;

document.getElementById('formDataBtn').disabled = true;
document.getElementById('name').addEventListener('keyup', e => {
    //Check for the input's value
    document.getElementById('formDataBtn').disabled = e.target.value === "";
});
document.getElementById('surname').addEventListener('keyup', e => {
    //Check for the input's value
    document.getElementById('formDataBtn').disabled = e.target.value === "";
});
document.getElementById('birthday').addEventListener('keyup', e => {
    //Check for the input's value
    document.getElementById('formBirthdayBtn').disabled = e.target.value === "";
});
document.getElementById('email').addEventListener('keyup', e => {
    //Check for the input's value
    document.getElementById('formDataBtn').disabled = e.target.value === "";
});

/*document.getElementById('name').addEventListener('keyup', e => {
    //Check for the input's value
    document.getElementById('formNameBtn').disabled = e.target.value === "";
});
document.getElementById('surname').addEventListener('keyup', e => {
    //Check for the input's value
    document.getElementById('formSurnameBtn').disabled = e.target.value === "";
});
document.getElementById('birthday').addEventListener('keyup', e => {
    //Check for the input's value
    document.getElementById('formBirthdayBtn').disabled = e.target.value === "";
});
document.getElementById('email').addEventListener('keyup', e => {
    //Check for the input's value
    document.getElementById('formEmailBtn').disabled = e.target.value === "";
});*/
document.getElementById('old_password').addEventListener('keyup', e => {
    //Check for the input's value
    document.getElementById('formPassBtn').disabled = e.target.value === "";
});
document.getElementById('new_password').addEventListener('keyup', e => {
    //Check for the input's value
    document.getElementById('formPassBtn').disabled = e.target.value === "";
});
document.getElementById('new_password_rep').addEventListener('keyup', e => {
    //Check for the input's value
    document.getElementById('formPassBtn').disabled = e.target.value === "";
});
