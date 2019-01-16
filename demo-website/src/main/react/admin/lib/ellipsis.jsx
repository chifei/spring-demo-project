import React from "react";
import PropTypes from "prop-types";
import {Tooltip} from "element-react";

export default class Ellipsis extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            text: props.text,
            length: props.length,
            mark: props.mark
        };
    }

    ellipsisText() {
        let text = this.state.text;
        if (text.length <= this.state.length) {
            return text;
        }
        const length = this.state.length - this.state.mark.length;
        if (/[^0-9a-zA-Z]/.test(text.charAt(length))) {
            return text.substr(0, length) + this.state.mark;
        }
        if (/[^a-zA-Z]/.test(text.charAt(length - 1))) {
            return text.substr(0, length) + this.state.mark;
        }
        text = text.substr(0, length);
        for (let i = this.state.length - 1; i > 0; i -= 1) {
            if (/[^0-9a-zA-Z]/.test(text.charAt(i))) {
                return text.substr(0, i + 1) + this.state.mark;
            }
        }
    }

    render() {
        return (
            <Tooltip effect="light" content={this.state.text} placement="top-start">
                <span>{this.ellipsisText()}</span>
            </Tooltip>
        );
    }
}

Ellipsis.defaultProps = {
    text: "",
    length: 0,
    mark: "..."
};

Ellipsis.propTypes = {
    text: PropTypes.string,
    length: PropTypes.number,
    mark: PropTypes.string
};
