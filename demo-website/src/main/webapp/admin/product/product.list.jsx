import React from "react";
import {Link} from "react-router-dom";
import {Button, Form, Input, Message as notification, Pagination, Select, Table} from "element-react";

const i18n = window.i18n;
export default class ProductList extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            limitOptions: [20, 50, 100],
            statusOptions: [{
                value: "ACTIVE",
                label: "ACTIVE"
            }, {
                value: "INACTIVE",
                label: "INACTIVE"
            }],
            query: {
                name: null,
                status: null,
                limit: 20,
                page: 1
            },
            data: {
                total: 0,
                page: 1,
                limit: 0,
                items: []
            },
            selected: [],
            columns: [
                {
                    label: i18n.t("product.name"),
                    prop: "displayName",
                    width: 200,
                    render: function(data) {
                        return (
                            <a href={data.originalURL} target="_blank">{data.displayName}</a>
                        );
                    }
                },
                {
                    label: i18n.t("product.parentSku"),
                    prop: "parentSku"
                },
                {
                    label: i18n.t("product.vendorNumber"),
                    prop: "vendorNumber"
                },
                {
                    label: i18n.t("product.price"),
                    prop: "price"
                },
                {
                    label: i18n.t("product.createdTime"),
                    width: 200,
                    render: function(data) {
                        return (
                            <ElementUI.DateFormatter date={data.createdTime}/>
                        );
                    }
                },
                {
                    label: i18n.t("product.action"),
                    width: 200,
                    render: function(data) {
                        return (
                            <span className="el-table__actions">
                                <Button type="text" size="mini">{i18n.t("product.delete")}</Button>
                                <Button type="text" size="mini"><Link to={{pathname: "/admin/product/" + data.id + "/update"}}>{i18n.t("product.edit")}</Link></Button>
                            </span>
                        );
                    }
                }]
        };
    }

    componentWillMount() {
        this.find();
    }

    find() {
        fetch("/admin/api/product/find", {
            method: "PUT",
            body: JSON.stringify(this.state.query)
        }).then((response) => {
            this.setState({data: response});
        });
    }

    search() {
        this.find();
    }

    statusChange(value) {
        let status = value;
        if (!value) {
            status = null;
        }
        this.setState({query: Object.assign(this.state.query, {status: status})}, () => {
            this.find();
        });
    }

    queryChange(key, value, find) {
        if (find) {
            this.setState({query: Object.assign(this.state.query, {[key]: value})}, () => {
                this.find();
            });
        } else {
            this.setState({query: Object.assign(this.state.query, {[key]: value})});
        }
    }

    select(item, checked) {
        const list = this.state.selected;
        if (checked) {
            list.push(item);
        } else {
            for (let i = 0; i < list.length; i += 1) {
                if (list[i].id === item.id) {
                    list.splice(i, 1);
                }
            }

        }
        this.setState({selected: list});
    }

    batchSelect(list, checked) {
        if (checked) {
            this.setState({selected: list});
        } else {
            this.setState({selected: []});
        }
    }

    batchDelete(e) {
        e.preventDefault();
        const list = this.state.selected;
        if (list.length === 0) {
            return;
        }
        const ids = [];
        for (let i = 0; i < list.length; i += 1) {
            ids.push(list[i].id);
        }
        fetch("/admin/api/product/batch-delete", {
            method: "POST",
            body: JSON.stringify({ids: ids})
        }).then(() => {
            notification({
                title: i18n.t("product.successTitle"),
                type: "success",
                message: i18n.t("product.deleteSuccessMessage")
            });
            this.setState({selected: []});
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
                                <Select placeholder={i18n.t("product.statusPlaceHolder")} value={this.state.query.status} onChange={k => this.statusChange(k)} clearable={true}>{
                                    this.state.statusOptions.map(el => <Select.Option key={el.value} label={el.label} value={el.value}/>)
                                }</Select>
                            </Form.Item>
                            <Form.Item>
                                <Input icon="fa fa-search" value={this.state.query.name} placeholder={i18n.t("product.queryPlaceHolder")} onChange={k => this.queryChange("name", k)}/>
                            </Form.Item>
                            <Form.Item>
                                <Button nativeType="button" onClick={e => this.search(e)}>{i18n.t("product.search")}</Button>
                            </Form.Item>
                        </Form>
                    </div>
                    <div className="toolbar-buttons">
                        {this.state.selected.length > 0 ? <Button type="danger" onClick={e => this.batchDelete(e)} nativeType="button">{i18n.t("product.batchDelete")}</Button> : ""}
                        <Button type="primary" nativeType="button">
                            <Link to={{pathname: "/admin/product/create"}}>
                                {i18n.t("product.createProduct")}
                            </Link>
                        </Button>
                    </div>
                </div>
                <div className="body body--full">
                    <Table
                        stripe={true}
                        columns={this.state.columns}
                        data={this.state.data.items}
                        onSelectChange={(dataItem, checked) => this.select(dataItem.id, checked)}
                        onSelectAll={(dataList, checked) => this.batchSelect(dataList, checked)}
                    />
                </div>
                <div className="footer">
                    <Pagination layout="total,sizes,prev,pager,next,jumper" total={this.state.data.total} pageSizes={this.state.limitOptions} pageSize={this.state.query.limit}
                        currentPage={this.state.query.page} onSizeChange={size => this.queryChange("limit", size, true)} onCurrentChange={currentPage => this.queryChange("page", currentPage, true)}/>
                </div>
            </div>
        );
    }
}