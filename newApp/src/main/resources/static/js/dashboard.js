document.addEventListener('DOMContentLoaded', function() {
    // Toggle sidebar collapse
    const toggleSidebar = document.createElement('button');
    toggleSidebar.className = 'sidebar-toggle';
    toggleSidebar.innerHTML = '<i class="fas fa-bars"></i>';
    document.querySelector('.topbar').prepend(toggleSidebar);
    
    toggleSidebar.addEventListener('click', function() {
        document.querySelector('.sidebar').classList.toggle('collapsed');
    });
    
    // Mobile sidebar toggle
    const sidebar = document.querySelector('.sidebar');
    const sidebarToggle = document.querySelector('.sidebar-toggle');
    
    function handleSidebarToggle() {
        if (window.innerWidth < 992) {
            sidebar.classList.add('mobile');
            sidebar.classList.remove('collapsed');
        } else {
            sidebar.classList.remove('mobile');
        }
    }
    
    window.addEventListener('resize', handleSidebarToggle);
    handleSidebarToggle();
    
    sidebarToggle.addEventListener('click', function() {
        if (sidebar.classList.contains('mobile')) {
            sidebar.classList.toggle('active');
        }
    });
    
    // Submenu toggle
    const menuItems = document.querySelectorAll('.has-submenu');
    
    menuItems.forEach(item => {
        item.addEventListener('click', function(e) {
            if (e.target === this || e.target === this.querySelector('a')) {
                e.preventDefault();
                this.classList.toggle('active');
            }
        });
    });
    
    // Animate stats cards on scroll
    const statsCards = document.querySelectorAll('.stat-card');
    
    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.style.transform = 'translateY(0)';
                entry.target.style.opacity = '1';
            }
        });
    }, { threshold: 0.1 });
    
    statsCards.forEach(card => {
        card.style.transform = 'translateY(20px)';
        card.style.opacity = '0';
        card.style.transition = 'transform 0.5s ease, opacity 0.5s ease';
        observer.observe(card);
    });
    
    // Table row click
    const tableRows = document.querySelectorAll('.crud-table tbody tr');
    
    tableRows.forEach(row => {
        row.addEventListener('click', function(e) {
            if (!e.target.classList.contains('action-btn')) {
                console.log('View details for row:', this);
                // Implement your row click logic here
            }
        });
    });
    
    // Tooltips for action buttons
    tippy('.action-btn', {
        content(reference) {
            const title = reference.getAttribute('title');
            return title;
        },
        onShow(instance) {
            const btn = instance.reference;
            if (btn.classList.contains('view')) {
                instance.setContent('Voir');
            } else if (btn.classList.contains('edit')) {
                instance.setContent('Modifier');
            } else if (btn.classList.contains('delete')) {
                instance.setContent('Supprimer');
            }
        },
        theme: 'light',
    });
});