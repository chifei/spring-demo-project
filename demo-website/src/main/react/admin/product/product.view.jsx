import React from "react";
import {Link} from "react-router-dom";
import {Breadcrumb, Button, Card, Form} from "element-react";
import PropTypes from "prop-types";

const i18n = window.i18n;
export default class ProductView extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            id: props.match.params.id,
            form: {},
            rules: {
                name: [{
                    required: true,
                    message: window.ElementUI.i18n.t("product.nameRule"),
                    trigger: "blur"
                }]
            }
        };
    }

    componentWillMount() {
        if (this.state.id) {
            fetch("/admin/api/product/" + this.state.id, {method: "GET"})
                .then((response) => {
                    this.setState({form: response});
                });
        }
    }

    render() {
        return (
            <div className="page">
                <div className="toolbar">
                    <div className="toolbar-form">
                        <Breadcrumb separator="/">
                            <Breadcrumb.Item><Link
                                to={{pathname: "/admin/"}}>{i18n.t("product.home")}</Link></Breadcrumb.Item>
                            <Breadcrumb.Item><Link
                                to={{pathname: "/admin/product/list"}}>{i18n.t("product.productList")}</Link></Breadcrumb.Item>
                            <Breadcrumb.Item>{i18n.t("product.view")}</Breadcrumb.Item>
                        </Breadcrumb>
                    </div>
                    <div className="toolbar-buttons">
                        <Button type="button"><Link to="/admin/product/list">{i18n.t("product.cancel")}</Link></Button>
                    </div>
                </div>
                <div className="body">
                    <Card>
                        <Form labelPosition="left" model={this.state.form} rules={this.state.rules} ref={(c) => {
                            this.productForm = c;
                        }} labelWidth="150">
                            <Form.Item label={i18n.t("product.name")} prop="name">
                                {this.state.form.name}
                            </Form.Item>

                            <Form.Item label={i18n.t("product.description")} prop="description">
                                {this.state.form.description}
                            </Form.Item>

                        </Form>
                    </Card>
                </div>
            </div>
        );
    }
}

ProductView.propTypes = {
    match: PropTypes.object,
    history: PropTypes.object.object
};