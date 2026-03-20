window.PageModules = window.PageModules || {};
window.PageModules.public = window.PageModules.public || {};

(function () {
    async function initLogin() {
        const existing = window.MockEngine.getSession();
        if (existing) {
            window.location.href = window.UIKit.roleHome(existing.role);
            return;
        }

        const form = document.getElementById("login-form");
        if (!form) return;
        form.addEventListener("submit", async (event) => {
            event.preventDefault();
            const payload = window.UIKit.formToObject(form);
            const result = await window.ApiClient.authLogin(payload);
            if (!result.success) {
                window.UIKit.toast(result.error.message, "error");
                return;
            }
            window.UIKit.toast("Login successful.", "success");
            const role = result.data.user.role;
            setTimeout(() => {
                window.location.href = window.UIKit.roleHome(role);
            }, 280);
        });
    }

    async function initRegister() {
        const existing = window.MockEngine.getSession();
        if (existing) {
            window.location.href = window.UIKit.roleHome(existing.role);
            return;
        }

        const form = document.getElementById("register-form");
        if (!form) return;
        form.addEventListener("submit", async (event) => {
            event.preventDefault();
            const payload = window.UIKit.formToObject(form);
            const result = await window.ApiClient.authRegister(payload);
            if (!result.success) {
                window.UIKit.toast(result.error.message, "error");
                return;
            }
            window.UIKit.toast("Account created. Please sign in.", "success");
            setTimeout(() => {
                window.location.href = `${window.APP_CONTEXT}/pages/login`;
            }, 360);
        });
    }

    window.PageModules.public.login = initLogin;
    window.PageModules.public.register = initRegister;
})();
