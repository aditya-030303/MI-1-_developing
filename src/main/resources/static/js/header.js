document.addEventListener('DOMContentLoaded', () => {
    // Mobile menu toggle
    const mobileMenuToggle = document.querySelector('.mobile-menu-toggle');
    const siteHeader = document.querySelector('.site-header');
    
    if (mobileMenuToggle) {
        mobileMenuToggle.addEventListener('click', () => {
            siteHeader.classList.toggle('mobile-menu-active');
        });
    }
    
    // Theme toggle functionality
    const themeToggle = document.querySelector('.theme-toggle');
    
    if (themeToggle) {
        // Check if dark mode is already set in localStorage
        const isDarkMode = localStorage.getItem('darkMode') === 'true';
        
        // Set initial theme based on localStorage or system preference
        if (isDarkMode || (!localStorage.getItem('darkMode') && 
            window.matchMedia('(prefers-color-scheme: dark)').matches)) {
            document.body.classList.add('dark-mode');
        }
        
        themeToggle.addEventListener('click', () => {
            document.body.classList.toggle('dark-mode');
            // Save preference to localStorage
            localStorage.setItem('darkMode', document.body.classList.contains('dark-mode'));
        });
    }
});