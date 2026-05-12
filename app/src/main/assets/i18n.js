/**
 * Budakattu-Sante Premium Navigation Engine
 * Restores the original high-fidelity side-drawer navigation.
 * Managed surgically to prevent layout interference.
 */

(function () {
    /**
     * STUB: applyLocalization
     * Kept for backward compatibility with pages calling it.
     * App is now strictly English as requested.
     */
    window.applyLocalization = function () {
        console.log("Budakattu-Sante: Running in English-only mode.");
    };

    /**
     * Injects the premium side navigation drawer dynamically.
     * Features glassmorphism, fluid transitions, and haptic-ready interactions.
     */
    function injectSidebar() {
        if (document.getElementById("custom-sidebar-drawer")) return;

        // Premium sidebar styles for glassmorphism & fluid transitions
        const drawerStyles = `
            #custom-sidebar-drawer {
                font-family: 'Outfit', 'Inter', sans-serif;
            }
            .sidebar-backdrop-active {
                opacity: 1 !important;
                pointer-events: auto !important;
            }
            .sidebar-panel-active {
                transform: translateX(0) !important;
            }
            #sidebar-panel {
                scrollbar-width: none;
                -ms-overflow-style: none;
            }
            #sidebar-panel::-webkit-scrollbar {
                display: none;
            }
        `;
        const sidebarStyleSheet = document.createElement("style");
        sidebarStyleSheet.innerText = drawerStyles;
        document.head.appendChild(sidebarStyleSheet);

        const drawer = document.createElement("div");
        drawer.id = "custom-sidebar-drawer";
        drawer.className = "fixed inset-0 z-[10000] pointer-events-none transition-all duration-300";

        drawer.innerHTML = `
            <!-- Backdrop Overlay -->
            <div id="sidebar-backdrop" class="absolute inset-0 bg-black/30 backdrop-blur-[2px] opacity-0 transition-opacity duration-300 pointer-events-none"></div>
            
            <!-- Content Panel (Premium Glassmorphism) -->
            <div id="sidebar-panel" class="absolute top-0 left-0 h-full w-[300px] bg-white/95 dark:bg-surface-container-highest/95 backdrop-blur-[40px] border-r border-white/40 shadow-[20px_0_60px_rgba(0,109,54,0.12)] flex flex-col justify-between transform -translate-x-full transition-transform duration-400 ease-[cubic-bezier(0.16,1,0.3,1)] pointer-events-auto overflow-y-auto">
                <div>
                    <!-- Header -->
                    <div class="p-8 border-b border-black/5 flex justify-between items-center bg-secondary/5">
                        <div class="flex flex-col">
                            <span class="font-h2 text-[20px] font-bold tracking-tight text-secondary">Budakattu-Sante</span>
                            <span class="text-[10px] font-bold tracking-widest text-tertiary uppercase mt-1">Forest-To-Home</span>
                        </div>
                        <button id="btn-close-sidebar" class="text-on-surface-variant hover:text-secondary hover:scale-110 active:scale-90 transition-all w-9 h-9 flex items-center justify-center rounded-full bg-white/60 border border-white/40 shadow-sm">
                            <span class="material-symbols-outlined text-[20px]">close</span>
                        </button>
                    </div>

                    <!-- Navigation Items -->
                    <div class="py-6 px-4 space-y-2">
                        <a href="discover.html" class="flex items-center gap-4 px-5 py-4 text-[15px] font-semibold text-on-surface-variant hover:text-secondary hover:bg-secondary/10 rounded-2xl transition-all group">
                            <span class="material-symbols-outlined text-secondary group-hover:scale-110 transition-transform">explore</span>
                            <span>Discover Marketplace</span>
                        </a>

                        <a href="tracking.html" class="flex items-center gap-4 px-5 py-4 text-[15px] font-bold text-secondary bg-secondary/5 border border-secondary/10 rounded-2xl transition-all shadow-sm">
                            <span class="material-symbols-outlined text-secondary animate-pulse">local_shipping</span>
                            <span>Track My Orders</span>
                        </a>

                        <div class="h-px bg-black/5 my-4 mx-4"></div>

                        <a href="wishlist.html" class="flex items-center gap-4 px-5 py-4 text-[15px] font-semibold text-on-surface-variant hover:text-secondary hover:bg-secondary/10 rounded-2xl transition-all group">
                            <span class="material-symbols-outlined text-secondary group-hover:scale-110 transition-transform">favorite</span>
                            <span>Favorites</span>
                        </a>

                        <a href="cart.html" class="flex items-center gap-4 px-5 py-4 text-[15px] font-semibold text-on-surface-variant hover:text-secondary hover:bg-secondary/10 rounded-2xl transition-all group">
                            <span class="material-symbols-outlined text-secondary group-hover:scale-110 transition-transform">shopping_bag</span>
                            <span>Shopping Cart</span>
                        </a>

                        <a href="account.html" class="flex items-center gap-4 px-5 py-4 text-[15px] font-semibold text-on-surface-variant hover:text-secondary hover:bg-secondary/10 rounded-2xl transition-all group">
                            <span class="material-symbols-outlined text-secondary group-hover:scale-110 transition-transform">person</span>
                            <span>Account Profile</span>
                        </a>

                        <a href="notifications.html" class="flex items-center gap-4 px-5 py-4 text-[15px] font-semibold text-on-surface-variant hover:text-secondary hover:bg-secondary/10 rounded-2xl transition-all group">
                            <span class="material-symbols-outlined text-secondary group-hover:scale-110 transition-transform">notifications</span>
                            <span>Notifications</span>
                        </a>
                    </div>
                </div>

                <!-- Footer -->
                <div class="p-8 border-t border-black/5 bg-secondary/5">
                    <div class="flex items-center gap-3 mb-4">
                        <div class="w-10 h-10 rounded-full bg-secondary/20 flex items-center justify-center text-secondary">
                            <span class="material-symbols-outlined">eco</span>
                        </div>
                        <div class="flex flex-col">
                            <span class="text-[12px] font-bold text-on-surface">Forest Patron</span>
                            <span class="text-[10px] text-on-surface-variant">Sustainable Choice</span>
                        </div>
                    </div>
                    <a href="login.html" onclick="localStorage.removeItem('userLoggedIn')" class="flex items-center justify-center gap-2 w-full py-3 bg-error/10 text-error font-bold rounded-xl hover:bg-error hover:text-white transition-all text-sm">
                        <span class="material-symbols-outlined text-[18px]">logout</span>
                        <span>Logout Session</span>
                    </a>
                </div>
            </div>
        `;

        document.body.appendChild(drawer);

        const backdrop = document.getElementById("sidebar-backdrop");
        const panel = document.getElementById("sidebar-panel");
        const closeBtn = document.getElementById("btn-close-sidebar");

        function openSidebar() {
            drawer.classList.remove("pointer-events-none");
            backdrop.classList.add("sidebar-backdrop-active");
            panel.classList.add("sidebar-panel-active");
            document.body.style.overflow = "hidden"; // Prevent background scroll
        }

        function closeSidebar() {
            drawer.classList.add("pointer-events-none");
            backdrop.classList.remove("sidebar-backdrop-active");
            panel.classList.remove("sidebar-panel-active");
            // Only restore scroll if we're not on a page that forces hidden overflow
            if (!document.body.style.overflow.includes("hidden")) {
                document.body.style.overflow = "";
            }
        }

        /**
         * Reliable trigger binding for hamburger icons.
         * Scans the header for "menu" icons and attaches click events.
         */
        const bindHamburger = () => {
            const menuIcons = Array.from(document.querySelectorAll("header .material-symbols-outlined, header button .material-symbols-outlined")).filter(el => {
                return el.textContent.trim() === "menu";
            });

            menuIcons.forEach(icon => {
                const trigger = icon.closest("button") || icon.parentElement;
                trigger.style.cursor = "pointer";
                trigger.onclick = (e) => {
                    e.preventDefault();
                    e.stopPropagation();
                    openSidebar();
                };
            });
        };

        bindHamburger();
        // Fallback for dynamic content loading
        setTimeout(bindHamburger, 500);
        setTimeout(bindHamburger, 2000);

        backdrop.addEventListener("click", closeSidebar);
        closeBtn.addEventListener("click", closeSidebar);

        // Also close on ESC key
        document.addEventListener("keydown", (e) => {
            if (e.key === "Escape") closeSidebar();
        });
    }

    // Initialize on load
    if (document.readyState === "loading") {
        document.addEventListener("DOMContentLoaded", injectSidebar);
    } else {
        injectSidebar();
    }
})();
