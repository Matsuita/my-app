function toggleMenu() {
    const menu = document.getElementById("menu");
    if (!menu) return;
    menu.classList.toggle("active");
}

// 🔥 パーティクルは外に出す
function setupParticle() {
    let lastTime = 0;

    document.addEventListener("click", function(e){

        const now = Date.now();
        if (now - lastTime < 100) return;
        lastTime = now;

        const colors = ["#0d6efd", "#00d4ff", "#ffffff"];

        for(let i = 0; i < 6; i++){
            const particle = document.createElement("div");
            particle.className = "particle";

            particle.style.left = e.clientX + "px";
            particle.style.top = e.clientY + "px";

            particle.style.background =
                colors[Math.floor(Math.random() * colors.length)];

            const x = (Math.random() - 0.5) * 100 + "px";
            const y = (Math.random() - 0.5) * 100 + "px";

            particle.style.setProperty("--x", x);
            particle.style.setProperty("--y", y);

            document.body.appendChild(particle);

            setTimeout(() => particle.remove(), 500);
        }
    });
}

// 🔥 toggleも外で定義
function setupToggle(inputId, toggleId) {
    const input = document.getElementById(inputId);
    const toggle = document.getElementById(toggleId);

    if (!input || !toggle) return;

    toggle.addEventListener("click", () => {
        input.classList.toggle("mask");

        if (input.classList.contains("mask")) {
            toggle.classList.replace("bi-eye-slash", "bi-eye");
        } else {
            toggle.classList.replace("bi-eye", "bi-eye-slash");
        }
    });
}

// 🔥 初期化だけまとめる
document.addEventListener("DOMContentLoaded", () => {
    setupToggle("password", "togglePassword");
    setupToggle("passwordConfirm", "togglePasswordConfirm");
    setupParticle();
});