function toggleMenu() {
    const menu = document.getElementById("menu");
    if (!menu) return;
    menu.classList.toggle("active");
}

window.addEventListener('resize', function() {
    const menu = document.getElementById("menu");
    if (window.innerWidth > 768) {
        menu.classList.remove("active");
    }
});
//  🔥 パーティクルは外に出す
function setupParticle() {
    let lastTime = 0;

    document.addEventListener("click", function(e) {

        const now = Date.now();
        if (now - lastTime < 100) return;
        lastTime = now;

        const colors = ["#0d6efd", "#00d4ff", "#ffffff"];

        for (let i = 0;i < 6;i++) {
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
document.addEventListener("DOMContentLoaded", () => {

    const slotLayer = document.getElementById("slot-layer");
    const overlay = document.getElementById("overlay");

    if (!overlay || !slotLayer) return;

    const STATE = {
        IDLE: 0,
        NAV: 1,
        GOD: 2
    };

    let state = STATE.IDLE;

    function showSlot() {
        slotLayer.style.opacity = "1";
        slotLayer.style.pointerEvents = "auto";
    }

    function hideSlot() {
        slotLayer.style.opacity = "0";
        slotLayer.style.pointerEvents = "none";
    }

    hideSlot();

    function playSE(name) {
        try {
            const a = new Audio("/sounds/" + name);
            a.volume = 1;
            a.play().catch(() => {});
        } catch (e) {}
    }

    // ===== 777入力（裏起動）=====
    let buffer = "";

    document.addEventListener("keydown", (e) => {
        const key = e.key.toLowerCase();

        if (state === STATE.IDLE && key === "7") {
            buffer += "7";
            if (buffer.length > 3) buffer = buffer.slice(-3);

            if (buffer === "777") {
                buffer = "";
                startNav();
            }
        }

        if (state === STATE.NAV) {
            handleNav(key);
        }
    });

    // ===== ナビ =====
    const steps = [
        { img: "/images/left.png", key: "g" },
        { img: "/images/center.png", key: "o" },
        { img: "/images/right.png", key: "d" }
    ];

    let stepIndex = 0;

    function startNav() {
		buffer = ""; // ←これも入れる
		 showSlot();
        state = STATE.NAV;
        stepIndex = 0;
        showNav();
        flash();
    }

    function showNav() {
        overlay.innerHTML = `
	        <img src="${steps[stepIndex].img}" 
	             class="nav-img">
	    `;
    }
    function playMiniVideo(id, start, end) {
        const v = document.getElementById(id);
        if (!v) return;

        v.style.display = "block";
        v.style.position = "fixed";
        v.style.inset = "0";
        v.style.zIndex = "10000";

        v.muted = false;

        // 🔥 すぐ設定（これが重要）
        try {
            v.currentTime = start;
        } catch (e) {}

        v.play().catch(() => {});

        const stop = () => {
            if (v.currentTime >= end) {
                v.pause();
                v.style.display = "none";
                v.removeEventListener("timeupdate", stop);
            }
        };

        v.removeEventListener("timeupdate", stop);
        v.addEventListener("timeupdate", stop);
    }
    function handleNav(key) {
        if (key === steps[stepIndex].key) {

            const current = stepIndex; // ←これ追加（超重要）
			if (current === 2) {
			           overlay.innerHTML = "";
					   
			       }

			 flash();
            shake(200);
            setTimeout(() => {

                if (current === 0) {
                    playMiniVideo("leftVideo", 4.8, 6.8);   // ←1秒〜1.5秒
                }

                if (current === 1) {
                    playMiniVideo("centerVideo", 7, 9);   // ←2秒〜3秒
                }

                if (current === 2) {
                    overlay.innerHTML = "";
                    playMovie(); // ←直接ラスト動画
                }


            }, 120);
            stepIndex++;

            if (stepIndex < steps.length) {
                showNav();
            } else {

            }
        }
    }
    function playMovie() {
        const movie = document.getElementById("movie");
        const video = document.getElementById("bgVideo");

        movie.style.display = "block";
        video.style.display = "block"; // ←これ追加（超重要）
		video.style.opacity = "0";

        video.muted = true;   // ←まずミュートで確実に再生
		video.pause();
		   video.currentTime = 10;
		    const playAfterSeek = () => {
				video.style.opacity = "1";   
				video.play().then(() => {
		               video.muted = false;
		           }).catch(() => {});

		           video.removeEventListener("seeked", playAfterSeek);
		       };

		       video.addEventListener("seeked", playAfterSeek);
			   video.onended = () => {
			       video.style.opacity = "0";

			       setTimeout(() => {
			           video.pause();
			           video.currentTime = 0;

			           video.style.display = "none";
			           movie.style.display = "none";
					   reset();
			       }, 50);
			   };
		    }   
		    // ===== GOD演出MAX =====
    function startGOD() {
        state = STATE.GOD;

        // ブラックアウト
        overlay.innerHTML = `<div style="
            position:fixed;
            inset:0;
            background:black;
            z-index:9999;
        "></div>`;

        playSE("god.mp3");

        // 点滅 + フラッシュ連打
        for (let i = 0;i < 15;i++) {
            setTimeout(() => {
                flash();
                redFlash();
            }, i * 80);
        }

        // 強シェイク
        shake(1500);
        setTimeout(() => {}, 120);
        // GOD出現
        // GOD出現 → 動画に変更
        setTimeout(() => {
            overlay.innerHTML = "";
            playMovie();
        }, 500);

        setTimeout(reset, 10000);
    }

    function flash() {
        const f = document.createElement("div");
        f.className = "flash";
        document.body.appendChild(f);
        setTimeout(() => f.remove(), 100);
    }

    function redFlash() {
        const r = document.createElement("div");
        r.style.position = "fixed";
        r.style.inset = "0";
        r.style.background = "red";
        r.style.opacity = "0.3";
        r.style.zIndex = "9999";
        document.body.appendChild(r);
        setTimeout(() => r.remove(), 80);
    }

    function shake(time) {
        const start = Date.now();
        const id = setInterval(() => {
            document.body.style.transform =
                `translate(${(Math.random() - 0.5) * 25}px, ${(Math.random() - 0.5) * 25}px)`;

            if (Date.now() - start > time) {
                clearInterval(id);
                document.body.style.transform = "";
            }
        }, 20);
    }

    function reset() {
        state = STATE.IDLE;
		buffer = ""; // ★これ追加（超重要）
		overlay.innerHTML = "";
        hideSlot();

        const movie = document.getElementById("movie");
        const video = document.getElementById("bgVideo");

        video.pause();
        video.currentTime = 0;

        movie.style.display = "none";
    }

});
