import React from "react";
import {Link} from "react-router-dom";
import {Button, Form, Input, Message as notification, MessageBox, Pagination, Table, Upload, Loading} from "element-react";

const i18n = window.i18n;
export default class ProductList extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            loading: false,
            selected: [],
            limitOptions: [20, 50, 100],
            query: {
                name: null,
                status: null,
                limit: 20,
                page: 1,
                sortingField: "updatedTime",
                desc: true
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
                    prop: "name",
                    sortable: "custom"
                },
                {
                    label: i18n.t("product.description"),
                    prop: "description",
                    sortable: "custom"
                },
                {
                    label: i18n.t("product.updatedTime"),
                    render: function(data) {
                        return data.updatedTime;
                    },
                    sortable: "custom"
                },
                {
                    label: i18n.t("product.updatedBy"),
                    prop: "updatedBy",
                    sortable: "custom"
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

    handleSort(data) {
        var sort = {
            sortingField: "updatedTime",
            desc: true
        };
        if (data.prop && data.order) {
            sort.sortingField = data.prop;
            sort.desc = data.order === "descending";
        }
        this.setState(
            {query: Object.assign(this.state.query, sort)}
        );
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

    downloadTemplate() {
        location.href = "/admin/product/template/download";
    }

    exportCsv() {
        if (this.state.query.name === null) {
            location.href = "/admin/product/export";
        } else {
            location.href = "/admin/product/export?name=" + this.state.query.name;
        }
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
                            <Upload className="product-upload-btn" accept=".csv" showFileList={false} action={"/admin/api/product/upload"} onSuccess={(response) => {
                                notification({
                                    type: "success",
                                    message: i18n.t("product.uploadSuccess")
                                });
                                this.search();
                                this.setState({loading: false});
                            }} onError={(response) => {
                                notification.error(i18n.t("product.uploadError"));
                                this.setState({loading: false});
                            }} onProgress={() => {
                                this.setState({loading: true});
                            }}>
                                <Button>{i18n.t("product.upload")}</Button>
                            </Upload>
                        </ElementUI.PermissionRequired>
                        <Button onClick={() => this.exportCsv()} nativeType="button">{i18n.t("product.export")}</Button>
                        <Button onClick={() => this.downloadTemplate()} nativeType="button">{i18n.t("product.templateDownload")}</Button>
                    </div>
                </div>
                <div className="body body--full">
                    <Loading text={i18n.t("product.uploading")} loading={this.state.loading}>
                        <Table style={{width: "100%"}} stripe={true} highlightCurrentRow={true} columns={this.state.columns} data={this.state.data.items} onSelectChange={selected => this.select(selected)} onSortChange={data => this.handleSort(data)}/>
                    </Loading>
                </div>
                <div className="footer">
                    <Pagination layout="total,sizes,prev,pager,next,jumper" total={this.state.data.total} pageSizes={this.state.limitOptions} pageSize={this.state.query.limit}
                        currentPage={this.state.query.page} onSizeChange={size => this.queryChange("limit", size, true)} onCurrentChange={currentPage => this.queryChange("page", currentPage, true)}/>
                </div>
            </div>
        );
    }
}