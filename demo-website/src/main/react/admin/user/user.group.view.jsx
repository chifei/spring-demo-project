import React from "react";
import {Breadcrumb, Button, Card, Form, Input, Table} from "element-react";
import PropTypes from "prop-types";
import {Link} from "react-router-dom";

const i18n = window.i18n;
export default class UserGroupView extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            id: props.match.params.id,
            userGroup: {
                name: null,
                permissions: []
            },
            selected: [],
            permissions: [],
            rules: {
                name: [{
                    required: true,
                    message: i18n.t("user.userGroupNameRule"),
                    trigger: "blur"
                }]
            },
            columns: [
                {type: "selection"},
                {
                    label: i18n.t("user.permissionId"),
                    prop: "name"
                },
                {
                    label: i18n.t("user.permissionName"),
                    prop: "displayName"
                },
                {
                    label: i18n.t("user.permissionDescription"),
                    prop: "description"
                }
            ]

        };
    }

    componentWillMount() {
        fetch("/admin/api/user/permissions", {method: "GET"}).then((permissions) => {
            this.setState({permissions: permissions}, () => {
                if (typeof this.state.id !== "undefined") {
                    fetch("/admin/api/user/role/" + this.state.id)
                        .then((userGroup) => {
                            const selected = [];
                            for (let i = 0; i < permissions.length; i += 1) {
                                const permission = permissions[i];
                                if (userGroup.permissions.includes(permission.name)) {
                                    selected.push(permission);
                                }
                            }
                            this.setState({
                                userGroup: userGroup,
                                selected: selected
                            });

                            if (this.table) {
                                for (let i = 0; i < selected.length; i += 1) {
                                    this.table.toggleRowSelection(selected[i], true);
                                }
                            }
                        });
                }
            });
        });

    }

    render() {
        return (
            <div className="page">
                <div className="toolbar">
                    <div className="toolbar-form">
                        <Breadcrumb separator="/">
                            <Breadcrumb.Item><Link to="/admin">{i18n.t("user.home")}</Link></Breadcrumb.Item>
                            <Breadcrumb.Item><Link to="/admin/user/role/list">{i18n.t("user.userGroupList")}</Link></Breadcrumb.Item>
                            <Breadcrumb.Item>{i18n.t("user.view")}</Breadcrumb.Item>
                        </Breadcrumb>
                    </div>
                    <div className="toolbar-buttons">
                        <Button type="button"><Link to="/admin/user/role/list">{i18n.t("user.cancel")}</Link></Button>
                    </div>
                </div>
                <div className="body">
                    <Card>
                        <Form model={this.state.userGroup} rules={this.state.rules} ref={(c) => {
                            this.userGroupForm = c;
                        }} labelWidth="150">
                            {this.state.id
                                ? <Form.Item label={i18n.t("user.name")}>
                                    <span>{this.state.userGroup.name}</span>
                                </Form.Item>
                                : <Form.Item label={i18n.t("user.name")} prop="name">
                                    <Input value={this.state.userGroup.name} onChange={value => this.onChange("name", value)}/>
                                </Form.Item>}
                            <Form.Item>
                                <Table
                                    ref={(c) => {
                                        this.table = c;
                                    }}
                                    style={{width: "100%"}}
                                    columns={this.state.columns}
                                    data={this.state.permissions}
                                />
                            </Form.Item>
                        </Form>
                    </Card>
                </div>
            </div>
        );
    }
}

UserGroupView.propTypes = {
    match: PropTypes.object,
    history: PropTypes.object
};