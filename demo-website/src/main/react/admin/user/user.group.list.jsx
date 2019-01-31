import React from "react";
import {Button, Form, Input, Message as alert, MessageBox, Pagination, Table} from "element-react";
import {Link} from "react-router-dom";

const i18n = window.i18n;
export default class UserGroupList extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            query: {
                name: "",
                page: 1,
                limit: 20
            },
            data: {
                total: 0,
                page: 1,
                limit: 20,
                items: []
            },
            limitOptions: [20, 50, 100],
            statusOptions: [
                {
                    label: i18n.t("user.statusActive"),
                    value: "ACTIVE"
                },
                {
                    label: i18n.t("user.statusInactive"),
                    value: "INACTIVE"
                }
            ],
            columns: [
                {type: "selection"},
                {
                    label: i18n.t("user.name"),
                    prop: "name"
                },
                {
                    label: i18n.t("user.permission"),
                    render: function(data) {
                        return data.permissions.join(";");
                    }
                },
                {
                    label: i18n.t("user.status"),
                    prop: "status"
                },
                {
                    label: i18n.t("user.updatedTime"),
                    render: function(data) {
                        return data.updatedTime;
                    }
                },
                {
                    label: i18n.t("user.updatedBy"),
                    prop: "updatedBy"
                },
                {
                    label: i18n.t("user.action"),
                    fixed: "right",
                    width: 200,
                    render: function(data) {
                        return (
                            <span className="el-table__actions">
                                <Link to={{pathname: "/admin/user/role/" + data.id + "/view"}}> {i18n.t("user.view")} </Link>
                                <ElementUI.PermissionRequired permissions={["user.write"]}>
                                    <Link to={{pathname: "/admin/user/role/" + data.id + "/update"}}>{i18n.t("user.update")}</Link>
                                </ElementUI.PermissionRequired>
                                <ElementUI.PermissionRequired permissions={["user.write"]}>
                                    <Button onClick={e => this.delete(data, e)} type="text">{i18n.t("user.delete")}</Button>
                                </ElementUI.PermissionRequired>
                            </span>
                        );
                    }.bind(this)
                }
            ],
            selected: []
        };
    }

    componentWillMount() {
        this.find();
    }

    find() {
        fetch("/admin/api/user/role/find", {
            method: "PUT",
            body: JSON.stringify(this.state.query)
        }).then((response) => {
            this.setState({data: response});
        });
    }

    queryChange(key, value) {
        this.setState(
            {query: Object.assign(this.state.query, {[key]: value})}
        );
    }

    select(selected) {
        this.setState({selected: selected});
    }

    delete(data) {
        MessageBox.confirm(i18n.t("user.roleDeleteContent"), i18n.t("user.delete"), {type: "warning"}).then(() => {
            fetch("/admin/api/user/role/batch-delete", {
                method: "POST",
                body: JSON.stringify({ids: [data.id]})
            }).then(() => {
                alert({
                    type: "success",
                    message: i18n.t("user.deleteSuccessContent")
                });
                this.find();
            });
        });
    }

    batchDelete() {
        MessageBox.confirm(i18n.t("user.roleDeleteContent"), i18n.t("user.delete"), {type: "warning"}).then(() => {
            const list = this.state.selected, ids = [];
            if (list.length === 0) {
                return;
            }
            for (let i = 0; i < list.length; i += 1) {
                ids.push(list[i].id);
            }

            fetch("/admin/api/user/role/batch-delete", {
                method: "POST",
                body: JSON.stringify({ids: ids})
            }).then(() => {
                alert({
                    type: "success",
                    message: i18n.t("user.deleteSuccessContent")
                });
                this.select([]);
                this.find();
            });
        });
    }


    render() {
        return (
            <div className="page">
                <div className="toolbar">
                    <div className="toolbar-form">
                        <Form inline={true} model={this.state.name}>
                            <Form.Item>
                                <Input value={this.state.query.name} onChange={value => this.queryChange("name", value)} icon="fa fa-search"/>
                            </Form.Item>
                            <Form.Item>
                                <Button onClick={() => this.find()}>{i18n.t("user.search")}</Button>
                            </Form.Item>
                        </Form>
                    </div>
                    <div className="toolbar-buttons">
                        <ElementUI.PermissionRequired permissions={["user.write"]}>
                            <Button type="danger" style={this.state.selected.length > 0 ? {} : {"display": "none"}} onClick={() => this.batchDelete()}>{i18n.t("user.delete")}</Button>
                            <Link to={{pathname: "/admin/user/role/create"}}>{i18n.t("user.create")}</Link>
                        </ElementUI.PermissionRequired>
                    </div>
                </div>
                <div className="body body--full">
                    <Table
                        style={{width: "100%"}}
                        columns={this.state.columns}
                        data={this.state.data.items}
                        onSelectChange={selected => this.select(selected)}
                    />
                </div>
                <div className="footer">
                    <Pagination layout="total,sizes,prev,pager,next,jumper" total={this.state.data.total}
                        pageSizes={this.state.limitOptions} pageSize={this.state.query.limit}
                        currentPage={this.state.query.page} onSizeChange={limit => this.queryChange("limit", limit)}
                        onCurrentChange={page => this.queryChange("page", page)}/>
                </div>
            </div>
        );
    }
}