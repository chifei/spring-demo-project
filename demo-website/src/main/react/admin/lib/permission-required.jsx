import React from "react";
import PropTypes from "prop-types";
import AppContext from "../context";

export default class PermissionRequired extends React.Component {
    render() {
        return <AppContext.Consumer>
            {(context) => {
                if (this.props.permissions) {
                    const permissions = this.props.permissions;
                    for (let i = 0; i < permissions.length; i += 1) {
                        if (!context.user.permissions.includes(permissions[i])) {
                            return <span/>;
                        }
                    }
                    return this.props.children;
                }
                return this.props.children;
            }}
        </AppContext.Consumer>;
    }
}

PermissionRequired.propTypes = {
    permissions: PropTypes.array,
    children: PropTypes.object
};
