import React from "react";
import {Button, Form, Input, Message as alert, MessageBox, Pagination, PermissionRequired, Select, Table} from "element-react";
import {Link} from "react-router-dom";

const i18n = window.i18n;
export default class UserList extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            query: {
                query: "",
                status: "ACTIVE",
                userGroupId: null,
                page: 1,
                limit: 20,
                sortingField: "createdTime",
                desc: true
            },
            data: {
                total: 0,
                page: 1,
                limit: 0,
                items: []
            },
            limitOptions: [20, 50, 100],
            columns: [
                {type: "selection"},
                {
                    label: i18n.t("user.username"),
                    prop: "username"
                },
                {
                    label: i18n.t("user.email"),
                    prop: "email"
                },
                {
                    label: i18n.t("user.userGroup"),
                    prop: "userGroup"
                },
                {
                    label: i18n.t("user.status"),
                    prop: "status",
                    width: 100
                },
                {
                    label: i18n.t("user.updatedTime"),
                    render: function(data) {
                        return data.updatedTime;
                    }
                },
                {
                    label: i18n.t("user.action"),
                    width: 200,
                    fixed: "right",
                    render: function(data) {
                        return (
                            <span className="el-table__actions">
                                <Button type="text"> <Link to={{pathname: "/admin/user/" + data.id + "/view"}}> {i18n.t("user.view")} </Link></Button>
                                {/*<PermissionRequired permissions={["user.write"]}>*/}
                                {/*<Button type="text"> <Link to={{pathname: "/admin/user/" + data.id + "/update"}}> {i18n.t("user.update")} </Link></Button>*/}
                                {/*</PermissionRequired>*/}
                                {/*<PermissionRequired permissions={["user.write"]}>*/}
                                {/*<Button onClick={e => this.delete(data, e)} type="text">{i18n.t("user.delete")}</Button>*/}
                                {/*</PermissionRequired>*/}
                            </span>
                        );
                    }.bind(this)
                }
            ],
            userGroupOptions: [],
            userGroupLoading: true,
            selected: []
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
        this.find();
    }

    find() {
        fetch("/admin/api/user/find", {
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

    roleChange(key, value) {
        this.setState(
            {query: Object.assign(this.state.query, {[key]: value})}
        );

        this.find();
    }

    select(selected) {
        this.setState({selected: selected});
    }

    delete(data) {
        MessageBox.confirm(i18n.t("user.userDeleteContent"), i18n.t("user.delete"), {type: "warning"})
            .then(() => {
                fetch("/admin/api/user/batch-delete", {
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
        MessageBox.confirm(i18n.t("user.userDeleteContent"), i18n.t("user.delete"), {type: "warning"})
            .then(() => {
                const list = this.state.selected, ids = [];
                if (list.length === 0) {
                    return;
                }
                for (let i = 0; i < list.length; i += 1) {
                    ids.push(list[i].id);
                }
                fetch("/admin/api/user/batch-delete", {
                    method: "POST",
                    body: JSON.stringify({ids: ids})
                }).then(() => {
                    alert({
                        type: "success",
                        message: i18n.t("user.deleteSuccessContent")
                    });
                    this.find();
                });
            });
    }

    render() {
        return (
            <div className="page">
                <div className="toolbar">
                    <div className="toolbar-form">
                        <Form inline={true} model={this.state.query}>
                            <Form.Item>
                                <Select
                                    loading={this.state.userGroupLoading}
                                    placeholder={i18n.t("user.userGroup")}
                                    clearable={true}
                                    onChange={value => this.roleChange("roleId", value)}>
                                    {this.state.userGroupOptions.map(el => <Select.Option key={el.value} label={el.label} value={el.value}/>)}
                                </Select>
                            </Form.Item>
                            <Form.Item>
                                <Input value={this.state.query.username} onChange={value => this.queryChange("username", value)} icon="fa fa-search"/>
                            </Form.Item>
                            <Form.Item>
                                <Button onClick={() => this.find()}>{i18n.t("user.search")}</Button>
                            </Form.Item>
                        </Form>
                    </div>
                    <div className="toolbar-buttons">
                        <Button type="danger" style={this.state.selected.length > 0 ? {} : {"display": "none"}} onClick={() => this.batchDelete()}>{i18n.t("user.delete")}</Button>
                        <Button type="primary"> <Link to={{pathname: "/admin/user/create"}}>{i18n.t("user.create")}</Link> </Button>
                    </div>
                </div>
                <div className="body body--full">
                    <Table style={{width: "100%"}}
                        stripe={true}
                        highlightCurrentRow={true}
                        columns={this.state.columns}
                        data={this.state.data.items}
                        onSelectChange={selected => this.select(selected)}/>
                </div>
                <div className="footer">
                    <Pagination layout="total,sizes,prev,pager,next,jumper" total={this.state.data.total} pageSizes={this.state.limitOptions} pageSize={this.state.query.limit}
                        currentPage={this.state.query.page}
                        onSizeChange={(limit) => {
                            this.queryChange("page", 1);
                            this.queryChange("limit", limit);
                            this.find();
                        }}
                        onCurrentChange={(page) => {
                            this.queryChange("page", page);
                            this.find();
                        }}/>
                </div>
            </div>
        );
    }
}