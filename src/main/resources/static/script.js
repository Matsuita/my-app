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
	document.addEventListener(
	    "click",
	    function(e){

	        const colors = [

	            "#0d6efd",
	            "#00d4ff",
	            "#ffffff"

	        ];

	        for(let i = 0; i < 12; i++){

	            const particle =
	                document.createElement(
	                    "div");

	            particle.className =
	                "particle";

	            particle.style.left =
	                e.clientX + "px";

	            particle.style.top =
	                e.clientY + "px";

	            particle.style.background =
	                colors[
	                    Math.floor(
	                        Math.random()
	                        * colors.length)
	                ];

	            const x =
	                (Math.random() - 0.5)
	                * 160 + "px";

	            const y =
	                (Math.random() - 0.5)
	                * 160 + "px";

	            particle.style.setProperty(
	                "--x",
	                x);

	            particle.style.setProperty(
	                "--y",
	                y);

	            document.body.appendChild(
	                particle);

	            setTimeout(function(){

	                particle.remove();

	            },700);
	        }
	});

    setupToggle("password", "togglePassword");
    setupToggle("passwordConfirm", "togglePasswordConfirm");

});