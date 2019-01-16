import React from "react";
import {Route} from "react-router-dom";
import PropTypes from "prop-types";

import UserList from "./user.list";
import UserGroupList from "./user.group.list";
import UserUpdate from "./user.update";
import UserView from "./user.view";
import UserGroupUpdate from "./user.group.update";
import UserGroupView from "./user.group.view";
import UserLogin from "./user.login";
import UserLogout from "./user.logout";
import UserProfile from "./user.profile";

export default class UserIndex extends React.Component {
    constructor(props) {
        super(props);
        this.state = {};
    }

    /* global fetchIntercept */


    render() {
        return (
            <div>

            </div>
        );
    }
}

UserIndex.propTypes = {history: PropTypes.object};