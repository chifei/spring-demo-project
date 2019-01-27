import React from "react";

const AppContext = React.createContext({
    app: window.app,
    user: window.user
});

export default AppContext;
