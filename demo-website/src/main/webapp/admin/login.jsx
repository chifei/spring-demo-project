import "babel-polyfill";
import React from "react";
import ReactDOM from "react-dom";
import {Button, Menu} from "element-react";
import "./lib/fetch";
import "./lib/vendor";

import "./css/theme.css";
import "./css/main.css";
import "./css/iconfont.css";
import "./css/font-awesome.css";
import UserLogin from "./user/user.login"

window.console.log(window.messages);
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

