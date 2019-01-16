import React from "react";
import {Link} from "react-router-dom";
import {Breadcrumb, Button, Card, Form, Input, Select, Upload} from "element-react";
import PropTypes from "prop-types";

const i18n = window.i18n;
export default class ProductUpdate extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            id: props.match.params.id,
            form: {
                status: null,
                tags: [],
                keywords: [],
                features: []
            },
            occasionOptions: [],
            statusOptions: [{
                value: "ACTIVE",
                label: i18n.t("product.statusActive")
            }, {
                value: "INACTIVE",
                label: i18n.t("product.statusInactive")
            }]
        };
    }

    componentWillMount() {
        if (this.state.id) {
            fetch("/admin/api/product/" + this.state.id, {method: "GET"})
                .then((response) => {
                    if (!response.occasionIds) {
                        response.occasionIds = [];
                    }
                    this.setState({form: response});
                });
        }
    }

    formChange(key, value) {
        this.setState(
            {form: Object.assign(this.state.form, {[key]: value})}
        );
    }

    save() {
        this.productForm.validate((valid) => {
            if (valid) {
                if (this.state.id) {
                    fetch("/admin/api/product/" + this.state.id, {
                        method: "put",
                        body: JSON.stringify(this.state.form)
                    }).then(() => {
                        this.props.history.push("/admin/product/list");
                    });
                } else {
                    fetch("/admin/api/product", {
                        method: "post",
                        body: JSON.stringify(this.state.form)
                    }).then(() => {
                        this.props.history.push("/admin/product/list");
                    });
                }
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
                            <Breadcrumb.Item><Link to={{pathname: "/admin/"}}>{i18n.t("product.home")}</Link></Breadcrumb.Item>
                            <Breadcrumb.Item><Link to={{pathname: "/admin/product/list"}}>{i18n.t("product.productList")}</Link></Breadcrumb.Item>
                            <Breadcrumb.Item>{this.state.id ? i18n.t("product.updateProduct") : i18n.t("product.createProduct")}</Breadcrumb.Item>
                        </Breadcrumb>
                    </div>
                    <div className="toolbar-buttons">
                        <Button type="primary" nativeType="button" onClick={() => this.save()}>{i18n.t("product.save")}</Button>
                    </div>
                </div>
                <div className="body">
                    <Card>
                        <Form labelPosition="left" model={this.state.form} rules={this.state.rules} ref={(c) => {
                            this.productForm = c;
                        }} labelWidth="150">
                            <Form.Item label={i18n.t("product.originalURL")} prop="originalURL">
                                <Input value={this.state.form.originalURL} onChange={value => this.formChange("originalURL", value)}/>
                            </Form.Item>

                            <Form.Item label={i18n.t("product.vendorNumber")} prop="vendorNumber">
                                <Input value={this.state.form.vendorNumber} onChange={value => this.formChange("vendorNumber", value)}/>
                            </Form.Item>

                            <Form.Item label={i18n.t("product.parentSku")} prop="parentSku">
                                <Input value={this.state.form.parentSku} onChange={value => this.formChange("parentSku", value)}/>
                            </Form.Item>

                            <Form.Item label={i18n.t("product.countryCode")} prop="countryCode">
                                <Input value={this.state.form.countryCode} onChange={value => this.formChange("countryCode", value)}/>
                            </Form.Item>

                            <Form.Item label={i18n.t("product.name")} prop="name">
                                <Input value={this.state.form.name} onChange={value => this.formChange("name", value)}/>
                            </Form.Item>

                            <Form.Item label={i18n.t("product.displayName")} prop="displayName">
                                <Input value={this.state.form.displayName} onChange={value => this.formChange("displayName", value)}/>
                            </Form.Item>

                            <Form.Item label={i18n.t("product.description")} prop="description">
                                <Input value={this.state.form.description} onChange={value => this.formChange("description", value)} type="textarea"/>
                            </Form.Item>

                            <Form.Item label={i18n.t("product.tips")} prop="tips">
                                <Input value={this.state.form.tips} onChange={value => this.formChange("tips", value)} type="textarea"/>
                            </Form.Item>

                            <Form.Item label={i18n.t("product.keywords")}>
                                <ElementUI.TagList list={this.state.form.keywords} onChange={val => this.formChange("keywords", val)}/>
                            </Form.Item>

                            <Form.Item label={i18n.t("product.tags")}>
                                <ElementUI.TagList list={this.state.form.tags} onChange={val => this.formChange("tags", val)}/>
                            </Form.Item>

                            <Form.Item label={i18n.t("product.features")}>
                                <ElementUI.TagList list={this.state.form.features} onChange={val => this.formChange("features", val)}/>
                            </Form.Item>

                            <Form.Item label={window.ElementUI.i18n.t("product.imageURL")}>
                                <Upload
                                    showFileList={false}
                                    action={"/admin/api/file/upload"}
                                    onSuccess={(response) => {
                                        const form = this.state.form;
                                        form.imageURLs = [response.path];
                                        this.setState({form});
                                    }}>
                                    {this.state.form.imageURLs && this.state.form.imageURLs[0]
                                        ? <div className="el-form-upload-preview">
                                            <img src={"/admin/file" + this.state.form.imageURLs[0]}/>
                                            <div className="el-form-upload-preview-delete-wrap"
                                                onClick={() => {
                                                    const form = this.state.form;
                                                    form.imageURLs = [];
                                                    this.setState({form});
                                                }}>
                                                <Button type="text" className="el-form-upload-preview-delete">
                                                    <i className="iconfont icon-icon_delete"/>
                                                </Button>
                                            </div>
                                        </div>
                                        : <Button className="el-form-upload-button" size="large">
                                            <i className="el-icon-plus"/>
                                        </Button>}
                                </Upload>
                            </Form.Item>

                            <Form.Item label={i18n.t("product.harmonizedCode")} prop="harmonizedCode">
                                <Input value={this.state.form.harmonizedCode} onChange={value => this.formChange("harmonizedCode", value)}/>
                            </Form.Item>

                            <Form.Item label={i18n.t("product.weight")} prop="weight">
                                <Input value={this.state.form.weight} onChange={value => this.formChange("weight", value)} type="number"/>
                            </Form.Item>

                            <Form.Item label={i18n.t("product.price")} prop="price">
                                <Input value={this.state.form.price} onChange={value => this.formChange("price", value)} type="number"/>
                            </Form.Item>

                            <Form.Item label={i18n.t("product.salePrice")} prop="salePrice">
                                <Input value={this.state.form.salePrice} onChange={value => this.formChange("salePrice", value)} type="number"/>
                            </Form.Item>

                            <Form.Item label={i18n.t("product.status")} prop="status">
                                <Select value={this.state.form.status} onChange={val => this.formChange("status", val)}
                                    placeholder={i18n.t("product.statusOptions")} clearable={true}>
                                    {this.state.statusOptions.map(el => <Select.Option key={el.value} label={el.label}
                                        value={el.value}/>)}
                                </Select>
                            </Form.Item>
                        </Form>
                    </Card>
                </div>
            </div>
        );
    }
}

ProductUpdate.propTypes = {
    match: PropTypes.object,
    history: PropTypes.object.object
};