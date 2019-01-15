import React from "react";
import {Breadcrumb, Button, Card, Dialog, Form, Input, Select, Upload} from "element-react";
import PropTypes from "prop-types";
import {Link} from "react-router-dom";

import "./user.update.css";

const i18n = window.i18n;
export default class User extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            id: this.props.match.params.id,
            user: {
                username: null,
                nickname: null,
                password: null,
                email: null,
                phone: null,
                imageURL: null,
                userGroupIds: []
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
                nickname: [{
                    required: true,
                    message: window.ElementUI.i18n.t("user.nicknameRule"),
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
        fetch("/admin/api/user/group/find", {
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
        const userGroupIds = this.state.user.userGroupIds;
        userGroupIds.length = 0;
        for (let i = 0; i < selected.length; i += 1) {
            userGroupIds.push(selected[i]);
        }
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
                fetch("/admin/api/user/password", {
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
        window.console.log("UPDATE");
        const changePasswordRequest = this.state.changePasswordRequest;
        changePasswordRequest[key] = value;
        this.setState({changePasswordRequest});
    }

    isImage(path) {
        const regex = /(\.jpg)|(\.png)|(\.bmp)|(\.gif)$/;
        return regex.test(path);
    }

    addField() {
        const user = this.state.user;
        if (!user.fields) {
            user.fields = {};
        }
        const number = Object.entries(user.fields).length + 1;
        user.fields["field " + number] = "value " + number;
        this.setState({user});
    }

    removeField(key) {
        const user = this.state.user;
        delete user.fields[key];
        this.setState({user});
    }

    changeFieldKey(newKey, prevKey) {
        const user = this.state.user;
        const fields = {};
        for (const key in user.fields) {
            if (key === prevKey) {
                fields[newKey] = user.fields[prevKey];
            } else {
                fields[key] = user.fields[key];
            }
        }
        user.fields = fields;
        this.setState({user});
    }

    changeFieldValue(value, key) {
        const user = this.state.user;
        user.fields[key] = value;
        this.setState({user});
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
                        <Button type="button"><Link to="/admin/user/list">{window.ElementUI.i18n.t("user.cancel")}</Link></Button>
                    </div>
                </div>
                <div className="body">
                    <Card>
                        <Form model={this.state.user} rules={this.state.rules} ref={(c) => {
                            this.userForm = c;
                        }} labelWidth="150">
                            <Form.Item label={window.ElementUI.i18n.t("user.username")} prop="username">
                                <Input value={this.state.user.username} onChange={value => this.onChange("username", value)}/>
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
                                </Form.Item>
                            }

                            <Form.Item label={window.ElementUI.i18n.t("user.icon")}>
                                <Upload
                                    showFileList={false}
                                    action={"/admin/api/file/upload"}
                                    onSuccess={(response) => {
                                        const user = this.state.user;
                                        user.imageURL = response.path;
                                        this.setState({user: user});
                                    }}>
                                    {this.state.user.imageURL
                                        ? <div className="el-form-upload-preview">
                                            <img src={"/admin" + this.state.user.imageURL}/>
                                            <div className="el-form-upload-preview-delete-wrap"
                                                onClick={() => {
                                                    const user = this.state.user;
                                                    user.imageURL = null;
                                                    this.setState({user});
                                                }}>
                                                <Button type="text" className="el-form-upload-preview-delete">
                                                    <i className="iconfont icon-icon_delete"/>
                                                </Button>
                                            </div>
                                        </div>
                                        : <Button className="el-form-upload-button" size="large">
                                            <i className="el-icon-plus"/>
                                        </Button>}
                                </Upload>
                            </Form.Item>
                            <Form.Item label={window.ElementUI.i18n.t("user.nickname")} prop="nickname">
                                <Input value={this.state.user.nickname} onChange={value => this.onChange("nickname", value)}/>
                            </Form.Item>
                            <Form.Item label={window.ElementUI.i18n.t("user.phone")} prop="phone">
                                <Input value={this.state.user.phone} onChange={value => this.onChange("phone", value)}/>
                            </Form.Item>
                            <Form.Item label={window.ElementUI.i18n.t("user.email")} prop="email">
                                <Input value={this.state.user.email} onChange={value => this.onChange("email", value)}/>
                            </Form.Item>
                            <Form.Item label={window.ElementUI.i18n.t("user.description")} prop="description">
                                <Input type="textarea" value={this.state.user.description} onChange={value => this.onChange("description", value)}/>
                            </Form.Item>
                            <Form.Item label={window.ElementUI.i18n.t("user.userGroup")} prop="userGroup">
                                <Select value={this.state.user.userGroupIds} loading={this.state.userGroupLoading} multiple={true} onChange={selected => this.userGroupIdsChange(selected)}>
                                    {this.state.userGroupOptions.map(el => <Select.Option key={el.value} label={el.label} value={el.value}/>)}
                                </Select>
                            </Form.Item>
                            <Form.Item label={window.ElementUI.i18n.t("user.fields")} prop="fields">
                                {this.state.user.fields && Object.entries(this.state.user.fields).map((field, index) =>
                                    <Form.Item className="user-update__field" key={field.key} labelWidth="0">
                                        <Input value={field[1]}
                                            placeholder={i18n.t("user.fieldValue")}
                                            prepend={<Input value={field[0]} placeholder={i18n.t("user.fieldName")} onChange={val => this.changeFieldKey(val, field[0])}/>}
                                            onChange={val => this.changeFieldValue(val, field[0])}
                                            append={<Button size="small" onClick={() => this.removeField(field[0])} icon="minus"></Button>}/>
                                    </Form.Item>
                                )}
                                <Button onClick={() => this.addField()} icon="plus"></Button>
                            </Form.Item>
                        </Form>
                    </Card>
                </div>
            </div>
        );
    }
}

User.propTypes = {
    match: PropTypes.object.match,
    history: PropTypes.object.history
};