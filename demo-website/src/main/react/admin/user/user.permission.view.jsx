import React from "react";
import {Link} from "react-router-dom";
import {Breadcrumb, Button, Card, Form} from "element-react";
import PropTypes from "prop-types";

const i18n = window.i18n;
export default class UserPermissionView extends React.Component {
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
                        <Button type="button"><Link to="/admin/user/permission/list">{i18n.t("user.cancel")}</Link></Button>
                    </div>
                </div>
                <div className="body">
                    <Card>
                        <Form labelPosition="left" model={this.state.form} rules={this.state.rules} ref={(c) => {
                            this.permissionForm = c;
                        }} labelWidth="150">

                            <Form.Item label={i18n.t("user.permissionId")} prop="name">
                                <span>{this.state.form.name}</span>
                            </Form.Item>

                            <Form.Item label={i18n.t("user.permissionName")} prop="displayName">
                                <span>{this.state.form.displayName}</span>
                            </Form.Item>

                            <Form.Item label={i18n.t("user.permissionDescription")} prop="description">
                                <span>{this.state.form.description}</span>
                            </Form.Item>

                        </Form>
                    </Card>
                </div>
            </div>
        );
    }
}

UserPermissionView.propTypes = {
    match: PropTypes.object,
    history: PropTypes.object.object
};