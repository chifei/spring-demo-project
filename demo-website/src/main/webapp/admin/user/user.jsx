import React from "react";
import {Route} from "react-router-dom";
import PropTypes from "prop-types";
import {Notification as notification} from "element-react";

import UserList from "./user.list";
import UserGroupList from "./user.group.list";
import User from "./user.update";
import UserView from "./user.view";
import UserGroup from "./user.group.update";
import UserLogin from "./user.login";
import UserLogout from "./user.logout";
import UserProfile from "./user.profile";

export default class Index extends React.Component {
    constructor(props) {
        super(props);
        this.state = {};
    }

    /* global fetchIntercept */
    componentWillMount() {
        fetchIntercept((response) => {
            if (response.status === 401) {
                window.location.href = "/login";
            }
            if (response.status === 403) {
                notification({
                    title: "ERROR",
                    message: response.json.message,
                    type: "error"
                });
            }
            return response;
        });
    }

    render() {
        return (
            <div>
                <Route exact path="/admin/user/login" component={UserLogin}/>
                <Route exact path="/admin/user/logout" component={UserLogout}/>
                <Route exact path="/admin/user/list" component={UserList}/>
                <Route exact path="/admin/user/role/list" component={UserGroupList}/>
                <Route exact path="/admin/user/:id/update" component={User}/>
                <Route exact path="/admin/user/:id/view" component={UserView}/>
                <Route exact path="/admin/user/create" component={User}/>
                <Route exact path="/admin/user/role/:id/update" component={UserGroup}/>
                <Route exact path="/admin/user/role/create" component={UserGroup}/>
                <Route exact path="/admin/user/profile" component={UserProfile}/>
            </div>
        );
    }
}

Index.propTypes = {history: PropTypes.object};