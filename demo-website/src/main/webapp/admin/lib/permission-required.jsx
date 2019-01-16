import React from "react";
import PropTypes from "prop-types";


export default class PermissionRequired extends React.Component {
    render() {
        if (this.props.permissions) {
            const permissions = this.props.permissions;
            for (let i = 0; i < permissions.length; i += 1) {
                if (!window.user.permissions.includes(permissions[i])) {
                    return <span/>;
                }
            }
            return this.props.children;
        }
        return this.props.children;
    }
}

PermissionRequired.propTypes = {
    permissions: PropTypes.array,
    children: PropTypes.object
};
