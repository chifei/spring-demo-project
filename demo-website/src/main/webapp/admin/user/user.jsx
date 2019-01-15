import React from "react";
import {Route} from "react-router-dom";
import PropTypes from "prop-types";

import UserList from "./user.list";
import UserGroupList from "./user.group.list";
import User from "./user.update";
import UserGroup from "./user.group.update";
import UserLogin from "./user.login";
import UserLogout from "./user.logout";
import Forbidden from "./forbidden";
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
                window.location = "/admin/user/login";
            }
            if (response.status === 403) {
                window.location = "/admin/user/unauthorized";
            }
            return response;
        });
    }

    render() {
        return (
            <div>
                <Route exact path="/admin/user/unauthorized" component={Forbidden}/>
                <Route exact path="/admin/user/login" component={UserLogin}/>
                <Route exact path="/admin/user/logout" component={UserLogout}/>
                <Route exact path="/admin/user/list" component={UserList}/>
                <Route exact path="/admin/user/group/list" component={UserGroupList}/>
                <Route exact path="/admin/user/:id/update" component={User}/>
                <Route exact path="/admin/user/create" component={User}/>
                <Route exact path="/admin/user/group/:id/update" component={UserGroup}/>
                <Route exact path="/admin/user/group/create" component={UserGroup}/>
                <Route exact path="/admin/user/profile" component={UserProfile}/>
            </div>
        );
    }
}

Index.propTypes = {history: PropTypes.object.history};