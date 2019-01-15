import React from "react";
import {Breadcrumb, Button, Card, Dialog, Form, Input} from "element-react";
import {Link} from "react-router-dom";

import "./user.update.css";

export default class UserProfile extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            user: window.app.user,
            updatePasswordVisible: false,
            changePasswordRequest: {},
            rules: {
                oldPassword: [{
                    required: true,
                    message: window.ElementUI.i18n.t("user.passwordRule"),
                    trigger: "blur"
                }],
                newPassword: [{
                    required: true,
                    message: window.ElementUI.i18n.t("user.newPasswordRequire"),
                    trigger: "blur"
                }],
                confirmPassword: [{
                    required: true,
                    message: window.ElementUI.i18n.t("user.newPasswordNotSame"),
                    trigger: "blur"
                }, {
                    validator: (rule, value, callback) => {
                        if (value === this.state.changePasswordRequest.newPassword) {
                            return callback();
                        }
                        return callback(new Error(window.ElementUI.i18n.t("user.newPasswordNotSame")));
                    }
                }]
            }
        };
    }


    updatePassword() {
        this.changePasswordForm.validate((valid) => {
            if (valid) {
                fetch("/web/api/user/self/password", {
                    method: "put",
                    body: JSON.stringify(this.state.changePasswordRequest)
                }).then(() => {
                    this.setState({
                        updatePasswordVisible: false,
                        changePasswordRequest: {}
                    });
                }).catch(() => {
                    const changePasswordRequest = this.state.changePasswordRequest;
                    changePasswordRequest.oldPassword = "";
                    this.changePasswordForm.validate();
                });
            } else {
                return false;
            }
        });
    }

    updatePasswordChange(key, value) {
        window.console.log("UPDATE");
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
                            <Breadcrumb.Item>{window.i18n.t("user.userProfile")}</Breadcrumb.Item>
                        </Breadcrumb>
                    </div>
                </div>
                <div className="body">
                    <Card>
                        <Form model={this.state.user} rules={this.state.rules} labelWidth="150">
                            <Form.Item label={window.ElementUI.i18n.t("user.username")} prop="username">
                                <span>{this.state.user.username}</span>
                            </Form.Item>
                            <Form.Item label={window.ElementUI.i18n.t("user.nickname")} prop="nickname">
                                <span>{this.state.user.nickname}</span>
                            </Form.Item>
                            <Form.Item label={window.ElementUI.i18n.t("user.icon")}>
                                <div className="el-form-upload-preview">
                                    <img src={"/admin" + this.state.user.imageURL}/>
                                    <div className="el-form-upload-preview-delete-wrap"></div>
                                </div>
                            </Form.Item>
                            <Form.Item label={window.ElementUI.i18n.t("user.password")}>
                                <Button onClick={() => this.setState({updatePasswordVisible: true})}>{window.ElementUI.i18n.t("user.updatePassword")}</Button>

                                <Dialog title={window.ElementUI.i18n.t("user.updatePassword")} visible={this.state.updatePasswordVisible}
                                    onCancel={() => this.setState({updatePasswordVisible: false})}>
                                    <Dialog.Body>
                                        <Form model={this.state.changePasswordRequest} rules={this.state.rules} ref={(c) => {
                                            this.changePasswordForm = c;
                                        }} labelWidth="120">
                                            <Form.Item label={window.ElementUI.i18n.t("user.password")} prop="oldPassword">
                                                <Input type="password" value={this.state.changePasswordRequest.oldPassword} onChange={value => this.updatePasswordChange("oldPassword", value)}/>
                                            </Form.Item>
                                            <Form.Item label={window.ElementUI.i18n.t("user.newPassword")} prop="newPassword">
                                                <Input type="password" value={this.state.changePasswordRequest.newPassword}
                                                    onChange={value => this.updatePasswordChange("newPassword", value)}/>
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
                            </Form.Item>

                        </Form>
                    </Card>
                </div>
            </div>
        );
    }
}