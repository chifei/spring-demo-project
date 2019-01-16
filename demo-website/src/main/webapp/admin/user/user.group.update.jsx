import React from "react";
import {Breadcrumb, Button, Card, Form, Input, Table} from "element-react";
import PropTypes from "prop-types";
import {Link} from "react-router-dom";

const i18n = window.i18n;
export default class UserGroupUpdate extends React.Component {
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
        fetch("/admin/api/user/role/permissions", {method: "GET"}).then((permissions) => {
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

    select(selected) {
        const userGroup = this.state.userGroup;
        const permissions = [];
        for (let i = 0; i < selected.length; i += 1) {
            permissions.push(selected[i].name);
        }
        userGroup.permissions = permissions;
        this.setState({
            selected: selected,
            userGroup: userGroup
        });
    }

    onChange(key, value) {
        this.setState(
            {userGroup: Object.assign(this.state.userGroup, {[key]: value})}
        );
    }

    save() {
        this.userGroupForm.validate((valid) => {
            if (valid) {
                fetch("/admin/api/user/role", {
                    method: "post",
                    body: JSON.stringify(this.state.userGroup)
                }).then(() => {
                    this.props.history.push("/admin/user/role/list");
                });
            } else {
                return false;
            }
        });
    }

    update() {
        this.userGroupForm.validate((valid) => {
            if (valid) {
                fetch("/admin/api/user/role/" + this.state.id, {
                    method: "put",
                    body: JSON.stringify(this.state.userGroup)
                }).then(() => {
                    this.props.history.push("/admin/user/role/list");
                });
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
                            <Breadcrumb.Item><Link to="/admin">{i18n.t("user.home")}</Link></Breadcrumb.Item>
                            <Breadcrumb.Item><Link to="/admin/user/role/list">{i18n.t("user.userGroupList")}</Link></Breadcrumb.Item>
                            <Breadcrumb.Item>
                                {this.state.id
                                    ? i18n.t("user.userGroupUpdate")
                                    : i18n.t("user.userGroupCreate")}</Breadcrumb.Item>
                        </Breadcrumb>
                    </div>
                    <div className="toolbar-buttons">
                        {this.state.id
                            ? <Button type="primary" onClick={() => this.update()}>{i18n.t("user.save")}</Button>
                            : <Button type="primary" onClick={() => this.save()}>{i18n.t("user.save")}</Button>}
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
                                    onSelectChange={selected => this.select(selected)}
                                />
                            </Form.Item>
                        </Form>
                    </Card>
                </div>
            </div>
        );
    }
}

UserGroupUpdate.propTypes = {
    match: PropTypes.object,
    history: PropTypes.object
};