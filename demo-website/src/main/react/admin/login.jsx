import React from "react";
import ReactDOM from "react-dom";
import UserLogin from "./user/user.login";

window.ElementUI.i18n.use(window.messages);

class App extends React.Component {
    constructor(props) {
        super(props);
        this.state = {};
    }

    render() {
        return (
            <div>
                <UserLogin/>
            </div>
        );
    }
}

ReactDOM.render(<App/>, document.getElementById("app"));

