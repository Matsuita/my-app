function toggleMenu() {
	const menu = document.getElementById("menu");
	menu.classList.toggle("active");
}

document.addEventListener("DOMContentLoaded", () => {

    function setupToggle(inputId, toggleId) {
        const input = document.getElementById(inputId);
        const toggle = document.getElementById(toggleId);

        toggle.addEventListener("click", () => {
            input.classList.toggle("mask");

            if (input.classList.contains("mask")) {
                toggle.classList.replace("bi-eye-slash", "bi-eye");
            } else {
                toggle.classList.replace("bi-eye", "bi-eye-slash");
            }
        });
    }

    setupToggle("password", "togglePassword");
    setupToggle("passwordConfirm", "togglePasswordConfirm");

});