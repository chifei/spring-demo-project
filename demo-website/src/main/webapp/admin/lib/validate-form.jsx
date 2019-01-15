import React from "react";
import {Form, i18n} from "element-react";
import PropTypes from "prop-types";

export default class ValidateForm extends React.Component {
    constructor(props) {
        super(props);
        this.state = {rules: props.rules ? props.rules : {}};
    }

    static getDerivedStateFromProps(props) {
        return {rules: props.rules};
    }

    showFieldError(field, message) {
        const rules = this.state.rules;
        rules[field].push({
            trigger: "callback",
            validator: (rule, value, callback) => callback(new Error(i18n.t(message)))
        });
        this.setState({rules});
        this.form.validateField(field, () => {
            rules[field] = rules[field].filter(rule => rule.trigger !== "callback");
            this.setState({rules});
        });
    }

    validate(callback) {
        return this.form.validate(callback);
    }

    validateField(field, callback) {
        return this.form.validateField(field, callback);
    }

    render() {
        return <Form rules={this.state.rules}
            model={this.props.model}
            inline={this.props.inline}
            labelPosition={this.props.labelPosition}
            labelWidth={this.props.labelWidth}
            labelSuffix={this.props.labelSuffix}
            ref={(form) => {
                this.form = form;
            }}
        >{this.props.children}</Form>;
    }
}

ValidateForm.propTypes = {
    rules: PropTypes.object,
    model: PropTypes.object,
    inline: PropTypes.string,
    labelPosition: PropTypes.string,
    labelWidth: PropTypes.string,
    labelSuffix: PropTypes.string,
    children: PropTypes.object
};