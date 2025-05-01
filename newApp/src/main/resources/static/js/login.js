document.addEventListener('DOMContentLoaded', function() {
    // Animation des inputs
    const inputs = document.querySelectorAll('.input-group input');
    
    inputs.forEach(input => {
        input.addEventListener('focus', function() {
            this.parentNode.querySelector('.underline').style.width = '100%';
        });
        
        input.addEventListener('blur', function() {
            if (!this.value) {
                this.parentNode.querySelector('.underline').style.width = '0';
            }
        });
    });
    
    // Effet de vague sur le bouton
    const loginButton = document.querySelector('.login-button');
    
    if (loginButton) {
        loginButton.addEventListener('click', function(e) {
            if (!this.classList.contains('loading')) {
                this.classList.add('loading');
                this.innerHTML = '<span>Connexion en cours...</span>';
                
                // Simuler un chargement
                setTimeout(() => {
                    this.classList.remove('loading');
                    this.innerHTML = '<span>Se connecter</span><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor"><path d="M10 17l5-5-5-5v10z"/></svg>';
                }, 2000);
            }
        });
    }
    
    // Effet parallaxe pour le background
    document.addEventListener('mousemove', function(e) {
        const background = document.querySelector('.background');
        const x = e.clientX / window.innerWidth;
        const y = e.clientY / window.innerHeight;
        
        if (background) {
            background.style.transform = `translate(-${x * 20}px, -${y * 20}px)`;
        }
    });
});