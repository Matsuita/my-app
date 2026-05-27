function setupToggle(inputId, toggleId) {
    const input = document.getElementById(inputId);
    const toggle = document.getElementById(toggleId);

    if (!input || !toggle) return;

    toggle.addEventListener("click", () => {

        if (input.type === "password") {
            input.type = "text";
            toggle.classList.replace("bi-eye-fill", "bi-eye-slash-fill");
        } else {
            input.type = "password";
            toggle.classList.replace("bi-eye-slash-fill", "bi-eye-fill");
        }

    });
}

document.addEventListener("DOMContentLoaded", () => {
    setupToggle("password", "togglePassword");
});