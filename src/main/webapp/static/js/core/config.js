window.TARS_CONFIG = {
    dataSource: "mock", // switch to "api" when backend is ready
    apiBasePath: `${window.APP_CONTEXT || ""}/api/v1`,
    pagination: {
        defaultPage: 1,
        defaultSize: 8
    },
    dateFormat: "YYYY-MM-DD"
};
