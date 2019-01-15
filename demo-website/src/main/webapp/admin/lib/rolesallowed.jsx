import PropTypes from "prop-types";


export default function RolesAllowed(props) {
    if (props.roles) {
        const user = window.user;
        const roles = props.roles;
        if (roles.push) {
            if (roles.length === 0) {
                return props.children;
            }
            for (let i = 0; i < roles.length; i += 1) {
                if (user.hasRole(roles[i])) {
                    return props.children;
                }
            }
        } else if (user.hasRole(roles)) {
            return props.children;
        } else {
            return null;
        }
    }
    return props.children;
}

RolesAllowed.propTypes = {
    roles: PropTypes.oneOfType([
        PropTypes.array,
        PropTypes.string
    ])
};
