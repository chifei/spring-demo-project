import React from "react";
import ReactDOM from "react-dom";
import "whatwg-fetch";
import PropTypes from "prop-types";
import {BrowserRouter, Link, NavLink, Redirect, Route, Switch as RouteSwitch, withRouter} from "react-router-dom";
import Ellipsis from "./ellipsis";
import DateFormatter from "./dateformatter";
import PermissionRequired from "./permission-required";
import ValidateForm from "./validate-form";
import TreeTable from "./treetable";
import TagList from "./taglist";

import {
    Alert,
    AutoComplete,
    Badge,
    Breadcrumb,
    Button,
    Card,
    Carousel,
    Cascader,
    Checkbox,
    Collapse,
    ColorPicker,
    DatePicker,
    DateRangePicker,
    Dialog,
    Dropdown,
    Form,
    i18n,
    Icon,
    Input,
    InputNumber,
    Layout,
    Loading,
    Menu,
    Message,
    MessageBox,
    Notification,
    Pagination,
    Popover,
    Progress,
    Radio,
    Rate,
    Select,
    Slider,
    Steps,
    Switch,
    Table,
    Tabs,
    Tag,
    TimePicker,
    TimeRangePicker,
    TimeSelect,
    Tooltip,
    Transfer,
    Tree,
    Upload
} from "element-react";

const elementI18n = i18n;
window.React = React;
window.ReactDOM = ReactDOM;
window.ElementUI = {
    i18n: {
        locale: {},
        use(lang) {
            Object.assign(this.locale, lang);
            elementI18n.use(this.locale);
        },
        t(path, options) {
            try {
                return elementI18n.t(path, options);
            } catch (e) {
                window.console.log("missing message, key=" + path);
                return null;
            }
        }
    },
    Alert: Alert,
    Button: Button,
    Card: Card,
    Layout: Layout,
    Loading: Loading,
    Message: Message,
    MessageBox: MessageBox,
    Notification: Notification,
    Radio: Radio,
    Dialog: Dialog,
    Rate: Rate,
    Progress: Progress,
    Badge: Badge,
    Tabs: Tabs,
    Tree: Tree,
    Input: Input,
    Icon: Icon,
    Menu: Menu,
    Steps: Steps,
    Breadcrumb: Breadcrumb,
    Tooltip: Tooltip,
    InputNumber: InputNumber,
    Checkbox: Checkbox,
    Slider: Slider,
    Table: Table,
    Switch: Switch,
    Form: Form,
    ValidateForm: ValidateForm,
    Upload: Upload,
    Tag: Tag,
    Select: Select,
    Dropdown: Dropdown,
    Popover: Popover,
    Pagination: Pagination,
    AutoComplete: AutoComplete,
    TimeSelect: TimeSelect,
    TimePicker: TimePicker,
    TimeRangePicker: TimeRangePicker,
    DatePicker: DatePicker,
    DateRangePicker: DateRangePicker,
    Carousel: Carousel,
    Collapse: Collapse,
    ColorPicker: ColorPicker,
    Cascader: Cascader,
    Transfer: Transfer,
    TagList: TagList,
    TreeTable: TreeTable,
    Ellipsis: Ellipsis,
    DateFormatter: DateFormatter,
    PermissionRequired: PermissionRequired
};

window.ReactRouterDom = {
    BrowserRouter: BrowserRouter,
    Link: Link,
    Route: Route,
    Redirect: Redirect,
    Switch: RouteSwitch,
    NavLink: NavLink,
    withRouter: withRouter
};

window.i18n = window.ElementUI.i18n;
window.PropTypes = PropTypes;