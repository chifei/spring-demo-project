import React from "react";
import {Button, DateFormatter, Form, Input, Message as alert, MessageBox as dialog, Pagination, Select, Table} from "element-react";
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
            statusOptions: [
                {
                    label: i18n.t("user.statusActive"),
                    value: "ACTIVE"
                },
                {
                    label: i18n.t("user.statusInactive"),
                    value: "INACTIVE"
                },
                {
                    label: i18n.t("user.statusLocked"),
                    value: "LOCKED"
                }
            ],
            columns: [
                {type: "selection"},
                {
                    label: i18n.t("user.icon"),
                    prop: "imageURL",
                    width: 100,
                    render: function(user) {
                        return <img style={{
                            "max-height": "32px",
                            "vertical-align": "middle"
                        }} src={"/admin" + user.imageURL}/>;
                    }
                },
                {
                    label: i18n.t("user.username"),
                    prop: "username"
                },
                {
                    label: i18n.t("user.phone"),
                    prop: "phone"
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
                    label: i18n.t("user.createdTime"),
                    render: function(data) {
                        return <DateFormatter date={new Date(data.createdTime)}/>;
                    }
                },
                {
                    label: i18n.t("user.updatedTime"),
                    render: function(data) {
                        return <DateFormatter date={new Date(data.updatedTime)}/>;
                    }
                },
                {
                    label: i18n.t("user.action"),
                    width: 200,
                    fixed: "right",
                    render: function(data) {
                        return (
                            <span className="el-table__actions">
                                <Button type="text"> <Link to={{pathname: "/admin/user/" + data.id + "/update"}}> {i18n.t("user.update")} </Link></Button>
                                {data.status === "INACTIVE" ? <Button onClick={e => this.revert(data, e)} type="text">{i18n.t("user.revert")}</Button>
                                    : <Button onClick={e => this.delete(data, e)} type="text">{i18n.t("user.delete")}</Button>}
                                {data.status === "LOCKED" ? <Button type="text" onClick={e => this.updateStatus(data)}> {i18n.t("user.unlock")} </Button>
                                    : <Button type="text" onClick={e => this.updateStatus(data)}> {i18n.t("user.lock")} </Button>}
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

    statusChange(key, value) {
        const status = value ? value : null;
        this.setState(
            {query: Object.assign(this.state.query, {[key]: status})}
        );

        this.find();
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

    updateStatus(data) {
        if (data.status === "LOCKED") {
            data.status = "ACTIVE";
        } else {
            data.status = "LOCKED";
        }
        fetch("/admin/api/user/" + data.id, {
            method: "put",
            body: JSON.stringify(data)
        }).then(() => {
            this.find();
        });
    }

    revert(data) {
        dialog.confirm(i18n.t("user.userRevertContent"))
            .then(() => {
                fetch("/admin/api/user/" + data.id + "/revert", {method: "PUT"}).then(() => {
                    this.find();
                });
            })
            .catch(() => {
                alert({
                    type: "info",
                    message: i18n.t("user.revertCancelContent")
                });
            });
    }

    delete(data) {
        fetch("/admin/api/user/" + data.id, {method: "DELETE"})
            .then(() => {
                alert({
                    type: "success",
                    message: i18n.t("user.deleteSuccessContent")
                });
                this.find();
            });
    }

    batchDelete() {
        const list = this.state.selected, ids = [];
        if (list.length === 0) {
            return;
        }
        for (let i = 0; i < list.length; i += 1) {
            ids.push(list[i].id);
        }
        fetch("/admin/api/user", {
            method: "PUT",
            body: JSON.stringify({ids: ids})
        }).then(() => {
            alert({
                type: "success",
                message: i18n.t("user.deleteSuccessContent")
            });
            this.find();
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
                                    onChange={value => this.roleChange("userGroupId", value)}>
                                    {this.state.userGroupOptions.map(el => <Select.Option key={el.value} label={el.label} value={el.value}/>)}
                                </Select>
                            </Form.Item>
                            <Form.Item>
                                <Select
                                    value={this.state.query.status}
                                    placeholder={i18n.t("user.status")}
                                    clearable={true}
                                    onChange={value => this.statusChange("status", value)}>
                                    {
                                        this.state.statusOptions.map(el => <Select.Option key={el.value}
                                            label={el.label} value={el.value}/>)
                                    }
                                </Select>
                            </Form.Item>
                            <Form.Item>
                                <Input value={this.state.query.query} onChange={value => this.queryChange("query", value)} icon="fa fa-search"/>
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