window.PageModules = window.PageModules || {};

(function () {
    function initPage(role, page) {
        const roleModules = window.PageModules[role] || {};
        const initializer = roleModules[page];
        if (typeof initializer === "function") {
            initializer();
        }
    }

    document.addEventListener("DOMContentLoaded", () => {
        window.UIKit.bindGlobalActions();
        const role = document.body.dataset.role || "public";
        const page = document.body.dataset.page || "";
        initPage(role, page);
    });
})();
