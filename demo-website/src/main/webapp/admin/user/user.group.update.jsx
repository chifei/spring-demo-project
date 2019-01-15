import React from "react";
import {Breadcrumb, Button, Card, Checkbox, Form, Input} from "element-react";
import PropTypes from "prop-types";
import {Link} from "react-router-dom";
import uuid from "react-native-uuid";

const i18n = window.i18n;
export default class UserGroup extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            id: props.match.params.id,
            userGroup: {
                name: null,
                description: null,
                roles: []
            },
            allRoles: [],
            rules: {
                name: [{
                    required: true,
                    message: i18n.t("user.userGroupNameRule"),
                    trigger: "blur"
                }]
            },
            keyColumn: {
                label: i18n.t("user.roles"),
                render: function(data) {
                    return <Checkbox checked={data.checked} indeterminate={data.indeterminate} onChange={() => this.check(data)}>{data.role}</Checkbox>;
                }.bind(this)
            },
            columns: []
        };
    }

    componentWillMount() {
        fetch("/admin/api/role", {method: "GET"}).then((response) => {
            const allRoles = [];
            for (let i = 0; i < response.length; i += 1) {
                const item = response[i],
                    children = [];
                for (let j = 0; j < item.roles.length; j += 1) {
                    const role = item.roles[j];
                    children.push({
                        id: uuid.v4(),
                        module: item.name,
                        indeterminate: false,
                        checked: false,
                        role: item.name + "." + role,
                        children: []
                    });
                }
                allRoles.push({
                    id: uuid.v4(),
                    module: item.name,
                    indeterminate: false,
                    checked: false,
                    role: item.name,
                    children: children
                });
            }
            this.setState({allRoles: allRoles});
        });
        if (typeof this.state.id !== "undefined") {
            fetch("/admin/api/user/group/" + this.state.id)
                .then((response) => {
                    this.setState({userGroup: response});
                    const roles = this.state.userGroup.roles, allRoles = this.state.allRoles;
                    for (let i = 0; i < allRoles.length; i += 1) {
                        const parent = allRoles[i];
                        parent.checked = true;
                        parent.indeterminate = false;
                        for (let j = 0; j < parent.children.length; j += 1) {
                            if (roles.indexOf(parent.children[j].role) > 0) {
                                parent.children[j].checked = true;
                            }
                        }
                    }
                    this.setState({allRoles: allRoles});
                });
        }
    }

    check(data) {
        window.console.log(data);
        const allRoles = this.state.allRoles;
        data.checked = !data.checked;
        if (data.children.length === 0) {
            this.childCheck(allRoles, data);
        } else {
            this.parentCheck(allRoles, data, data.checked);
        }
        this.setState({allRoles: allRoles});
        this.select();
    }

    parentCheck(allRoles, data, checked) {
        for (let i = 0; i < allRoles.length; i += 1) {
            const parent = allRoles[i];
            if (parent.role === data.role) {
                parent.indeterminate = false;
                for (let j = 0; j < parent.children.length; j += 1) {
                    parent.children[j].checked = checked;
                }
            }
        }
    }

    childCheck(allRoles, data) {
        for (let i = 0; i < allRoles.length; i += 1) {
            if (allRoles[i].role === data.module) {
                allRoles[i].checked = true;
                allRoles[i].indeterminate = false;
                for (let j = 0; j < allRoles[i].children.length; j += 1) {
                    if (allRoles[i].children[j].checked) {
                        allRoles[i].indeterminate = true;
                    } else {
                        allRoles[i].checked = false;
                    }
                }
                if (allRoles[i].checked && allRoles[i].indeterminate) {
                    allRoles[i].indeterminate = false;
                }
            }
        }
    }

    select() {
        const roles = this.state.userGroup.roles;
        for (let i = 0; i < this.state.allRoles.length; i += 1) {
            if (this.state.allRoles[i].checked) {
                roles.push(this.state.allRoles[i].role);
            }
        }
        this.onChange("roles", roles);
    }

    onChange(key, value) {
        this.setState(
            {userGroup: Object.assign(this.state.userGroup, {[key]: value})}
        );
    }

    save() {
        this.userGroupForm.validate((valid) => {
            if (valid) {
                fetch("/admin/api/user/group", {
                    method: "post",
                    body: JSON.stringify(this.state.userGroup)
                }).then(() => {
                    this.props.history.push("/admin/user/group/list");
                });
            } else {
                return false;
            }
        });
    }

    update() {
        this.userGroupForm.validate((valid) => {
            if (valid) {
                fetch("/admin/api/user/group/" + this.state.id, {
                    method: "put",
                    body: JSON.stringify(this.state.userGroup)
                }).then(() => {
                    this.props.history.push("/admin/user/group/list");
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
                            <Breadcrumb.Item><Link to="/admin/user/group/list">{i18n.t("user.userGroupList")}</Link></Breadcrumb.Item>
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
                        <Button type="button"><Link to="/admin/user/group/list">{i18n.t("user.cancel")}</Link></Button>
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
                            <Form.Item label={i18n.t("user.description")}>
                                <Input value={this.state.userGroup.description}
                                    onChange={value => this.onChange("description", value)}/>
                            </Form.Item>
                            <Form.Item>
                                <ElementUI.TreeTable treeData={this.state.allRoles} keyColumn={this.state.keyColumn} treeColumns={this.state.columns}/>
                            </Form.Item>
                        </Form>
                    </Card>
                </div>
            </div>
        );
    }
}

UserGroup.propTypes = {
    match: PropTypes.object.match,
    history: PropTypes.object.history
};