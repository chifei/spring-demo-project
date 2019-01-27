import React from "react";
import {Button} from "element-react";
import {Link} from "react-router-dom";

export default class Forbidden extends React.Component {
    constructor(props) {
        super(props);
        this.state = {};
    }

    cancel() {
        window.console.log("cannot cancel");
    }

    render() {
        return (
            <div className="page sited-error__page">
                <div className="sited-error__container">
                    <img src="/admin/runtime/403.png" alt="" className="sited-error__image"/>
                    <p className="sited-error__text">You do not have permission to visit this page, please contact the administrator</p>
                    <Button type="primary" className="sited-error__button">
                        <Link to={{pathname: "/admin/"}}> Index </Link>
                    </Button>
                </div>
            </div>
        );
    }
}