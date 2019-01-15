import React from "react";
import PropTypes from "prop-types";

export default class DateFormatter extends React.Component {
    constructor(props) {
        super(props);
        this.state = {date: props.date};
    }

    render() {
        if (this.state.date) {
            const d = new Date(this.state.date);
            return <span>{d.getFullYear() + "-" + ("0" + (d.getMonth() + 1)).slice(-2) + "-" + ("0" + d.getDate()).slice(-2) + " " + ("0" + d.getHours()).slice(-2) + ":" + ("0" + d.getMinutes()).slice(-2) + ":" + ("0" + d.getSeconds()).slice(-2)}</span>;
        }
        return <span></span>;
    }
}

DateFormatter.propTypes = {date: PropTypes.string};
