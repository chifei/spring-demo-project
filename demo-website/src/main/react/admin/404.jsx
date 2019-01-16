import React from "react";
import {Button} from "element-react";
import {Link} from "react-router-dom";

export default function Error404() {
    return (
        <div className="page error__page">
            <div className="error__container">
                <img src="/admin/app/static/404.png" alt="" className="error__image"/>
                <p className="error__text">This page is empty!  I find a mistake!！！</p>
                <Button type="primary" className="error__button">
                    <Link to={{pathname: "/admin/"}}> Index </Link>
                </Button>
            </div>
        </div>
    );
}