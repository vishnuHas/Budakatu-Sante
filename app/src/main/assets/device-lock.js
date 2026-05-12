/**
 * Device Lock Script for Budakattu Sante
 * Enforces mobile/tablet-only view by showing a premium overlay on desktop.
 */

(function() {
    const DESKTOP_WIDTH_LIMIT = 1024;
    
    function createOverlay() {
        if (document.getElementById('device-lock-overlay')) return;

        const overlay = document.createElement('div');
        overlay.id = 'device-lock-overlay';
        overlay.style.cssText = `
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: #ffffff;
            z-index: 100000;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            text-align: center;
            padding: 40px;
            font-family: 'Manrope', sans-serif;
            color: #161c22;
        `;

        overlay.innerHTML = `
            <div style="max-width: 400px; width: 100%;">
                <div style="margin-bottom: 32px; display: inline-flex; align-items: center; justify-content: center; width: 80px; height: 80px; background: #006d3610; border-radius: 24px; color: #006d36;">
                    <span class="material-symbols-outlined" style="font-size: 48px;">phonelink_lock</span>
                </div>
                <h1 style="font-family: 'Epilogue', sans-serif; font-size: 32px; font-weight: 700; margin-bottom: 16px; letter-spacing: -0.04em;">Optimized for Mobile</h1>
                <p style="font-size: 16px; color: #444748; line-height: 1.6; margin-bottom: 40px;">This experience is exclusively designed for phone and tablet devices. Please scan the QR code or resize your browser to continue.</p>
                
                <div style="background: #ffffff; padding: 24px; border-radius: 32px; box-shadow: 0 40px 80px rgba(0, 109, 54, 0.08); border: 1px solid rgba(0, 109, 54, 0.1); margin-bottom: 40px; display: inline-block;">
                    <img src="https://api.qrserver.com/v1/create-qr-code/?size=180x180&data=${encodeURIComponent(window.location.href)}" alt="Scan to open on mobile" style="width: 180px; height: 180px;" />
                </div>
                
                <div style="font-size: 12px; font-family: 'Space Grotesk', sans-serif; text-transform: uppercase; letter-spacing: 0.1em; color: #006d36; font-weight: 600;">
                    Budakattu Sante Portal
                </div>
            </div>
        `;

        document.body.appendChild(overlay);
        document.body.style.overflow = 'hidden';
    }

    function removeOverlay() {
        const overlay = document.getElementById('device-lock-overlay');
        if (overlay) {
            overlay.remove();
            document.body.style.overflow = '';
        }
    }

    function checkDevice() {
        if (window.innerWidth > DESKTOP_WIDTH_LIMIT) {
            createOverlay();
        } else {
            removeOverlay();
        }
    }

    window.addEventListener('resize', checkDevice);
    window.addEventListener('load', checkDevice);
    checkDevice();
})();
