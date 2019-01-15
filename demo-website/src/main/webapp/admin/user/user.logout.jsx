import React from "react";

export default class UserLogout extends React.Component {
    componentWillMount() {
        fetch("/admin/api/user/logout", {method: "GET"}).then(() => {
            window.location.href = "/admin/";
        });
    }
}
