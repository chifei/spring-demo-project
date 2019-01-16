import React from "react";
import {Link} from "react-router-dom";
import {Button, Form, Input, Message as notification, MessageBox, Pagination, Table} from "element-react";

const i18n = window.i18n;
export default class ProductList extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            selected: [],
            limitOptions: [20, 50, 100],
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
            columns: [
                {type: "selection"},
                {
                    label: i18n.t("product.name"),
                    prop: "name"
                },
                {
                    label: i18n.t("product.description"),
                    prop: "description"
                },
                {
                    label: i18n.t("product.updatedTime"),
                    render: function(data) {
                        return data.updatedTime;
                    }
                },
                {
                    label: i18n.t("product.updatedBy"),
                    prop: "updatedBy"
                },
                {
                    label: i18n.t("product.action"),
                    width: 200,
                    fixed: "right",
                    render: function(data) {
                        return (
                            <span className="el-table__actions">
                                <Button type="text" size="mini"><Link to={{pathname: "/admin/product/" + data.id + "/view"}}>{i18n.t("product.view")}</Link></Button>
                                <ElementUI.PermissionRequired permissions={["product.write"]}>
                                    <Button type="text" size="mini"><Link to={{pathname: "/admin/product/" + data.id + "/update"}}>{i18n.t("product.edit")}</Link></Button>
                                </ElementUI.PermissionRequired>
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

    queryChange(key, value) {
        this.setState(
            {query: Object.assign(this.state.query, {[key]: value})}
        );
    }

    select(selected) {
        this.setState({selected: selected});
    }

    batchDelete() {
        MessageBox.confirm(i18n.t("product.productDeleteContent"), i18n.t("product.delete"), {type: "warning"})
            .then(() => {
                const list = this.state.selected, ids = [];
                if (list.length === 0) {
                    return;
                }
                for (let i = 0; i < list.length; i += 1) {
                    ids.push(list[i].id);
                }
                fetch("/admin/api/product/batch-delete", {
                    method: "POST",
                    body: JSON.stringify({ids: ids})
                }).then(() => {
                    notification({
                        type: "success",
                        message: i18n.t("product.deleteSuccessContent")
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
                                <Input icon="fa fa-search" value={this.state.query.name} placeholder={i18n.t("product.queryPlaceHolder")} onChange={value => this.queryChange("name", value)}/>
                            </Form.Item>
                            <Form.Item>
                                <Button nativeType="button" onClick={e => this.search(e)}>{i18n.t("product.search")}</Button>
                            </Form.Item>
                        </Form>
                    </div>
                    <div className="toolbar-buttons">
                        <ElementUI.PermissionRequired permissions={["product.write"]}>
                            {this.state.selected.length > 0 ? <Button type="danger" onClick={() => this.batchDelete()} nativeType="button">{i18n.t("product.delete")}</Button> : ""}
                            <Button type="primary" nativeType="button">
                                <Link to={{pathname: "/admin/product/create"}}>
                                    {i18n.t("product.createProduct")}
                                </Link>
                            </Button>
                        </ElementUI.PermissionRequired>
                    </div>
                </div>
                <div className="body body--full">
                    <Table style={{width: "100%"}} stripe={true} highlightCurrentRow={true} columns={this.state.columns} data={this.state.data.items}
                        onSelectChange={selected => this.select(selected)}/>
                </div>
                <div className="footer">
                    <Pagination layout="total,sizes,prev,pager,next,jumper" total={this.state.data.total} pageSizes={this.state.limitOptions} pageSize={this.state.query.limit}
                        currentPage={this.state.query.page} onSizeChange={size => this.queryChange("limit", size, true)} onCurrentChange={currentPage => this.queryChange("page", currentPage, true)}/>
                </div>
            </div>
        );
    }
}