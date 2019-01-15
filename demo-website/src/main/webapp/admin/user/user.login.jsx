import React from "react";
import {Button, Card, Checkbox, Form, Input} from "element-react";
import PropTypes from "prop-types";
import "./user.login.css";

const i18n = window.ElementUI.i18n;
export default class UserLogin extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            login: {
                username: null,
                password: null,
                captchaCode: null,
                autoLogin: false
            },
            rules: {
                username: [{
                    required: true,
                    message: i18n.t("user.usernameRule"),
                    trigger: "blur"
                }],
                password: [{
                    required: true,
                    message: i18n.t("user.passwordRule"),
                    trigger: "blur"
                }]
            },
            captchaCodeURL: "/captcha.jpg?" + new Date().getTime(),
            valid: false
        };
    }

    valid() {
        return this.state.login.username && this.state.login.password;
    }

    changeCaptchaCode() {
        this.setState({captchaCodeURL: "/captcha.jpg?" + new Date().getTime()});
    }

    onChange(key, value) {
        this.setState(
            {login: Object.assign(this.state.login, {[key]: value})}
        );
    }

    login() {
        this.loginForm.validate((valid) => {
            if (valid) {
                fetch("/admin/api/user/login", {
                    method: "post",
                    body: JSON.stringify(this.state.login)
                }).then((loginResponse) => {
                    if (this.hasFromURL(loginResponse.fromURL)) {
                        window.location.href = loginResponse.fromURL;
                    } else {
                        const rules = this.state.rules;
                        rules["username"].push({
                            trigger: "callback",
                            validator: (rule, value, callback) => callback(new Error(loginResponse.message))
                        });
                        this.setState({rules});
                        this.loginForm.validateField("username", () => {
                            rules["username"] = rules["username"].filter(rule => rule.trigger !== "callback");
                            this.setState({rules});
                        });
                    }
                });
            }
        });
        return false;
    }

    hasFromURL(fromURL) {
        return fromURL && fromURL !== "/login" && fromURL !== "/logout";
    }

    cancel() {
        window.console.log("cannot cancel");
    }

    handleKeyPress(event) {
        if (event.key === "Enter") {
            this.login();
        }
    }

    render() {
        return (
            <div className="login-wrap">
                <div className="login-panel">
                    <Card header={i18n.t("user.login")}>
                        <Form model={this.state.login} rules={this.state.rules} ref={(c) => {
                            this.loginForm = c;
                        }}>
                            <Form.Item prop="username">
                                <Input placeholder={i18n.t("user.username")}
                                    ref={(input) => {
                                        this.usernameInput = input;
                                    }}
                                    value={this.state.login.username}
                                    onChange={value => this.onChange("username", value)}
                                    onKeyPress={event => this.handleKeyPress(event)}/>
                            </Form.Item>
                            <Form.Item prop="password">
                                <Input placeholder={i18n.t("user.password")}
                                    value={this.state.login.password} type="password"
                                    onChange={value => this.onChange("password", value)}
                                    onKeyPress={event => this.handleKeyPress(event)}/>
                            </Form.Item>
                            <Button disabled={!this.valid()} type="primary" onClick={() => this.login()}>{i18n.t("user.login")}</Button>
                        </Form>
                    </Card>
                </div>
            </div>
        );
    }
}

UserLogin.propTypes = {history: PropTypes.object.history};