import React from "react";
import {Link} from "react-router-dom";
import {Breadcrumb, Button, Card, Form, Input} from "element-react";
import PropTypes from "prop-types";

const i18n = window.i18n;
export default class UserPermissionUpdate extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            id: props.match.params.id,
            form: {},
            rules: {
                name: [{
                    required: true,
                    message: window.ElementUI.i18n.t("user.permissionIdRule"),
                    trigger: "blur"
                }],
                displayName: [{
                    required: true,
                    message: window.ElementUI.i18n.t("user.permissionNameRule"),
                    trigger: "blur"
                }]
            }
        };
    }

    componentWillMount() {
        if (this.state.id) {
            fetch("/admin/api/user/permission/" + this.state.id, {method: "GET"})
                .then((response) => {
                    this.setState({form: response});
                });
        }
    }

    formChange(key, value) {
        const form = this.state.form;
        form[key] = value;
        this.setState({form});
    }

    save() {
        this.permissionForm.validate((valid) => {
            if (valid) {
                if (this.state.id) {
                    fetch("/admin/api/user/permission/" + this.state.id, {
                        method: "put",
                        body: JSON.stringify(this.state.form)
                    }).then(() => {
                        this.props.history.push("/admin/user/permission/list");
                    });
                } else {
                    fetch("/admin/api/user/permission", {
                        method: "post",
                        body: JSON.stringify(this.state.form)
                    }).then(() => {
                        this.props.history.push("/admin/user/permission/list");
                    });
                }
            } else {
                return false;
            }
        });
    }

    render() {
        return (
            <div className="page">
                <div className="toolbar">
                    <div className="toolbar-form">
                        <Breadcrumb separator="/">
                            <Breadcrumb.Item><Link
                                to={{pathname: "/admin/"}}>{i18n.t("user.home")}</Link></Breadcrumb.Item>
                            <Breadcrumb.Item><Link
                                to={{pathname: "/admin/user/permission/list"}}>{i18n.t("user.userPermissionList")}</Link></Breadcrumb.Item>
                            <Breadcrumb.Item>{this.state.id ? i18n.t("product.updateProduct") : i18n.t("product.createProduct")}</Breadcrumb.Item>
                        </Breadcrumb>
                    </div>
                    <div className="toolbar-buttons">
                        <Button type="primary" nativeType="button" onClick={() => this.save()}>{i18n.t("user.submit")}</Button>
                        <Button type="button"><Link to="/admin/user/permission/list">{i18n.t("user.cancel")}</Link></Button>
                    </div>
                </div>
                <div className="body">
                    <Card>
                        <Form labelPosition="left" model={this.state.form} rules={this.state.rules} ref={(c) => {
                            this.permissionForm = c;
                        }} labelWidth="150">

                            <Form.Item label={i18n.t("user.permissionId")} prop="name">
                                <Input value={this.state.form.name} onChange={value => this.formChange("name", value)}/>
                            </Form.Item>

                            <Form.Item label={i18n.t("user.permissionName")} prop="displayName">
                                <Input value={this.state.form.displayName} onChange={value => this.formChange("displayName", value)}/>
                            </Form.Item>

                            <Form.Item label={i18n.t("user.permissionDescription")} prop="description">
                                <Input value={this.state.form.description} onChange={value => this.formChange("description", value)} type="textarea"/>
                            </Form.Item>

                        </Form>
                    </Card>
                </div>
            </div>
        );
    }
}

UserPermissionUpdate.propTypes = {
    match: PropTypes.object,
    history: PropTypes.object.object
};