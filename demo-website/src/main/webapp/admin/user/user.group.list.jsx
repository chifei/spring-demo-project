import React from "react";
import {Button, DateFormatter, Form, Input, Message as alert, MessageBox as dialog, Pagination, Select, Table} from "element-react";
import {Link} from "react-router-dom";

const i18n = window.i18n;
export default class UserGroupList extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            query: {
                query: "",
                status: "ACTIVE",
                page: 1,
                limit: 5
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
                    label: i18n.t("user.description"),
                    prop: "description"
                },
                {
                    label: i18n.t("user.status"),
                    prop: "status"
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
                    fixed: "right",
                    width: 200,
                    render: function(data) {
                        return (
                            <span className="el-table__actions">
                                <Button type="text"> <Link to={{pathname: "/admin/user/group/" + data.id + "/update"}}>{i18n.t("user.update")}</Link> </Button>
                                {data.status === "INACTIVE" ? <Button onClick={e => this.revert(data, e)} type="text">{i18n.t("user.revert")}</Button>
                                    : <Button onClick={e => this.delete(data, e)} type="text">{i18n.t("user.delete")}</Button>}
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
        fetch("/admin/api/user/group/find", {
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

    revert(data) {
        dialog.confirm(i18n.t("user.userGroupRevertContent"))
            .then(() => {
                fetch("/admin/api/user/group/" + data.id + "/revert", {method: "PUT"}).then(() => {
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
        fetch("/admin/api/user/group/" + data.id, {method: "DELETE"})
            .then(() => {
                alert({
                    type: "success",
                    message: i18n.t("user.deleteSuccessContent")
                });
                this.find();
            });
    }

    batchDelete() {
        const list = this.state.selected,
            ids = [];
        if (list.length === 0) {
            return;
        }
        for (let i = 0; i < list.length; i += 1) {
            ids.push(list[i].id);
        }
        fetch("/admin/api/user/group", {
            method: "PUT",
            body: JSON.stringify({ids: ids})
        }).then(() => {
            alert({
                type: "success",
                message: i18n.t("user.deleteSuccessContent")
            });
            this.select([]);
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
                                    placeholder={i18n.t("user.status")}
                                    value={this.state.query.status}
                                    clearable={true}
                                    onChange={value => this.queryChange("status", value)}>
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
                        <Button type="primary"><Link to={{pathname: "/admin/user/group/create"}}>{i18n.t("user.create")}</Link></Button>
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