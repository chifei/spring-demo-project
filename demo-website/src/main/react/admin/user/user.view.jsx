import React from "react";
import {Breadcrumb, Button, Card, Form, Select} from "element-react";
import PropTypes from "prop-types";
import {Link} from "react-router-dom";

import "./user.update.css";

export default class User extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            id: this.props.match.params.id,
            user: {
                id: null,
                username: null,
                password: null,
                email: null,
                roleIds: []
            },
            userGroupOptions: [],
            userGroupLoading: true,
            updatePasswordVisible: false,
            changePasswordRequest: {
                userId: this.props.match.params.id,
                password: null,
                confirmPassword: null
            },
            rules: {
                username: [{
                    required: true,
                    message: window.ElementUI.i18n.t("user.usernameRule"),
                    trigger: "blur"
                }],
                email: [{
                    type: "email",
                    required: true,
                    message: window.ElementUI.i18n.t("user.emailRule"),
                    trigger: "blur"
                }]
            },
            changePasswordRules: {
                password: [{
                    required: true,
                    message: window.ElementUI.i18n.t("user.passwordRule"),
                    trigger: "blur"
                }],
                confirmPassword: [{
                    required: true,
                    message: window.ElementUI.i18n.t("user.confirmPasswordRule"),
                    trigger: "blur"
                }, {
                    validator: (rule, value, callback) => {
                        if (value !== this.state.changePasswordRequest.password) {
                            return callback(new Error(window.ElementUI.i18n.t("user.confirmPasswordRule")));
                        }
                        callback();
                    },
                    trigger: "blur"
                }]
            }
        };
    }

    componentWillMount() {
        fetch("/admin/api/user/role/find", {
            method: "PUT",
            body: JSON.stringify({
                page: 1,
                limit: 100
            })
        }).then((response) => {
            const userGroupOptions = [];
            for (let i = 0; i < response.items.length; i += 1) {
                const item = response.items[i];
                userGroupOptions.push({
                    label: item.name,
                    value: item.id
                });
            }
            this.setState({userGroupOptions: userGroupOptions});
            this.setState({userGroupLoading: false});
        });
        if (typeof this.state.id !== "undefined") {
            fetch("/admin/api/user/" + this.state.id)
                .then((response) => {
                    this.setState({user: response});
                    this.setState(
                        {changePasswordRequest: Object.assign(this.state.changePasswordRequest, {"user": response.username})}
                    );
                });
        }
    }

    onChange(key, value) {
        this.setState(
            {user: Object.assign(this.state.user, {[key]: value})}
        );
    }

    userGroupIdsChange(selected) {
        this.setState(
            {user: Object.assign(this.state.user, {"roleIds": selected})}
        );
    }

    save() {
        this.userForm.validate((valid) => {
            if (valid) {
                fetch("/admin/api/user", {
                    method: "post",
                    body: JSON.stringify(this.state.user)
                }).then(() => {
                    this.props.history.push("/admin/user/list");
                });
            } else {
                return false;
            }
        });
    }

    update() {
        this.userForm.validate((valid) => {
            if (valid) {
                if (this.state.id) {
                    fetch("/admin/api/user/" + this.state.id, {
                        method: "put",
                        body: JSON.stringify(this.state.user)
                    }).then(() => {
                        this.props.history.push("/admin/user/list");
                    });
                } else {
                    fetch("/admin/api/user", {
                        method: "post",
                        body: JSON.stringify(this.state.user)
                    }).then(() => {
                        this.props.history.push("/admin/user/list");
                    });
                }
            } else {
                return false;
            }
        });
    }

    updatePassword() {
        this.changePasswordForm.validate((valid) => {
            if (valid) {
                fetch("/admin/api/user/" + this.state.changePasswordRequest.userId + "/password", {
                    method: "put",
                    body: JSON.stringify(this.state.changePasswordRequest)
                }).then(() => {
                    this.setState({updatePasswordVisible: false});
                });
            } else {
                return false;
            }
        });
    }

    updatePasswordChange(key, value) {
        const changePasswordRequest = this.state.changePasswordRequest;
        changePasswordRequest[key] = value;
        this.setState({changePasswordRequest});
    }

    render() {
        return (
            <div className="page">
                <div className="toolbar">
                    <div className="toolbar-form">
                        <Breadcrumb separator="/">
                            <Breadcrumb.Item><Link to="/admin">{window.ElementUI.i18n.t("user.home")}</Link></Breadcrumb.Item>
                            <Breadcrumb.Item><Link to="/admin/user/list">{window.ElementUI.i18n.t("user.userList")}</Link></Breadcrumb.Item>
                            <Breadcrumb.Item>{window.i18n.t("user.view")}</Breadcrumb.Item>
                        </Breadcrumb>
                    </div>
                    <div className="toolbar-buttons">
                        <Button type="button"><Link to="/admin/user/list">{window.ElementUI.i18n.t("user.cancel")}</Link></Button>
                    </div>
                </div>
                <div className="body">
                    <Card>
                        <Form model={this.state.user} rules={this.state.rules} ref={(c) => {
                            this.userForm = c;
                        }} labelWidth="150">
                            <Form.Item label={window.ElementUI.i18n.t("user.username")} prop="username">
                                <span>{this.state.user.username}</span>
                            </Form.Item>
                            <Form.Item label={window.ElementUI.i18n.t("user.email")} prop="email">
                                <span>{this.state.user.email}</span>
                            </Form.Item>
                            <Form.Item label={window.ElementUI.i18n.t("user.userGroup")} prop="userGroup">
                                <Select value={this.state.user.roleIds} loading={this.state.userGroupLoading} disabled={true} multiple={true} onChange={selected => this.userGroupIdsChange(selected)}>
                                    {this.state.userGroupOptions.map(el => <Select.Option key={el.value} label={el.label} value={el.value}/>)}
                                </Select>
                            </Form.Item>
                        </Form>
                    </Card>
                </div>
            </div>
        );
    }
}

User.propTypes = {
    match: PropTypes.object,
    history: PropTypes.object
};