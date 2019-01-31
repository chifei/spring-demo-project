import React from "react";
import {Breadcrumb, Button, Card, Dialog, Form, Input, Select} from "element-react";
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
        if (typeof this.state.id == "undefined") {
            this.state.rules.password = [{
                required: true,
                message: window.ElementUI.i18n.t("user.passwordRule"),
                trigger: "blur"
            }];
        }
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
                            <Breadcrumb.Item>
                                {this.state.id
                                    ? window.i18n.t("user.updateUser")
                                    : window.i18n.t("user.createUser")}</Breadcrumb.Item>
                        </Breadcrumb>
                    </div>
                    <div className="toolbar-buttons">
                        <Button type="primary" onClick={() => this.update()}>{window.ElementUI.i18n.t("user.save")}</Button>
                        <Link to="/admin/user/list">{window.ElementUI.i18n.t("user.cancel")}</Link>
                    </div>
                </div>
                <div className="body">
                    <Card>
                        <Form model={this.state.user} rules={this.state.rules} ref={(c) => {
                            this.userForm = c;
                        }} labelWidth="150">
                            <Form.Item label={window.ElementUI.i18n.t("user.username")} prop="username">
                                <Input value={this.state.user.username} onChange={value => this.onChange("username", value)} disabled={this.state.user.id !== null}/>

                            </Form.Item>

                            {
                                !this.state.id &&
                                <Form.Item label={window.ElementUI.i18n.t("user.password")} prop="password">
                                    <Input type="password" value={this.state.user.password} onChange={value => this.onChange("password", value)}/>
                                </Form.Item>
                            }

                            {
                                this.state.id &&
                                <Form.Item label={window.ElementUI.i18n.t("user.password")} prop="password">
                                    <Button onClick={() => this.setState({updatePasswordVisible: true})}>{window.ElementUI.i18n.t("user.updatePassword")}</Button>
                                </Form.Item>
                            }
                            <Form.Item label={window.ElementUI.i18n.t("user.email")} prop="email">
                                <Input value={this.state.user.email} onChange={value => this.onChange("email", value)}/>
                            </Form.Item>
                            <Form.Item label={window.ElementUI.i18n.t("user.userGroup")} prop="userGroup">
                                <Select value={this.state.user.roleIds} loading={this.state.userGroupLoading} multiple={true} onChange={selected => this.userGroupIdsChange(selected)}>
                                    {this.state.userGroupOptions.map(el => <Select.Option key={el.value} label={el.label} value={el.value}/>)}
                                </Select>
                            </Form.Item>
                        </Form>
                        <Dialog title={window.ElementUI.i18n.t("user.updatePassword")} visible={this.state.updatePasswordVisible}
                            onCancel={() => this.setState({updatePasswordVisible: false})}>
                            <Dialog.Body>
                                <Form model={this.state.changePasswordRequest} rules={this.state.changePasswordRules} ref={(c) => {
                                    this.changePasswordForm = c;
                                }} labelWidth="120">
                                    <Form.Item label={window.ElementUI.i18n.t("user.password")} prop="password">
                                        <Input type="password" value={this.state.changePasswordRequest.password} onChange={value => this.updatePasswordChange("password", value)}/>
                                    </Form.Item>
                                    <Form.Item label={window.ElementUI.i18n.t("user.confirmPassword")} prop="confirmPassword">
                                        <Input type="password" value={this.state.changePasswordRequest.confirmPassword}
                                            onChange={value => this.updatePasswordChange("confirmPassword", value)}/>
                                    </Form.Item>
                                </Form>
                            </Dialog.Body>
                            <Dialog.Footer className="dialog-footer">
                                <Button onClick={() => this.setState({updatePasswordVisible: false})}>{window.ElementUI.i18n.t("user.cancel")}</Button>
                                <Button type="primary" onClick={() => this.updatePassword()}>{window.ElementUI.i18n.t("user.submit")}</Button>
                            </Dialog.Footer>
                        </Dialog>
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