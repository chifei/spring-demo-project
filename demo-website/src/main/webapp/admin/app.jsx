import "babel-polyfill";
import React from "react";
import ReactDOM from "react-dom";
import createHistory from "history/createBrowserHistory";
import {Route, Router, Switch} from "react-router-dom";
import {Button, Menu, Notification as notification} from "element-react";

import "./lib/fetch";
import "./lib/vendor";

import "./css/theme.css";
import "./css/main.css";
import "./css/iconfont.css";
import "./css/font-awesome.css";

import {Sticky} from "./lib/sticky";
import Error404 from "./404";
import ProductUpdate from "./product/product.update";
import ProductList from "./product/product.list";

import UserList from "./user/user.list";
import UserGroupList from "./user/user.group.list";
import UserUpdate from "./user/user.update";
import UserView from "./user/user.view";
import UserGroupUpdate from "./user/user.group.update";
import UserGroupView from "./user/user.group.view";
import UserLogin from "./user/user.login";
import UserLogout from "./user/user.logout";

const history = createHistory({basename: "/"});
window.ElementUI.i18n.use(window.messages);
const i18n = window.i18n;

class App extends React.Component {
    constructor(props) {
        super(props);
        const defaultOpeneds = [];
        this.state = {
            defaultOpeneds: defaultOpeneds,
            pathname: history.location.pathname,
            pageFixed: true,
            shownMenu: true,
            fixedMenu: true
        };
        history.listen((location) => {
            this.setState({pathname: location.pathname});
        });
    }

    componentWillMount() {
        window.fetchIntercept((response) => {
            if (response.status === 401) {
                window.location.href = "/login";
            }
            if (response.status === 403) {
                notification({
                    title: "ERROR",
                    message: response.json.message,
                    type: "error"
                });
            }
            return response;
        });
    }

    hasPermission(permissions) {
        for (let i = 0; i < permissions.length; i += 1) {
            if (!window.user.permissions.includes(permissions[i])) {
                return false;
            }
        }
        return true;
    }

    isActive(route) {
        const pathname = this.state.pathname || history.location.pathname;
        return route && pathname.startsWith(route);
    }

    isItemActive(route) {
        const pathname = this.state.pathname || history.location.pathname;
        return route && pathname === route;
    }

    toggleMenu() {
        this.setState({shownMenu: !this.state.shownMenu}, () => this.fixPage());
    }

    fixMenu() {
        this.setState({fixedMenu: !this.state.fixedMenu}, () => this.fixPage());
    }

    fixPage(fixed) {
        this.setState({pageFixed: this.state.shownMenu && this.state.fixedMenu});
    }

    render() {
        return (
            <div className={window.user.id ? "" : "unauthenticated"}>
                <Sticky/>
                {!this.state.fixedMenu && this.state.shownMenu && <div className="nav__mask" onClick={() => this.toggleMenu()}></div>}
                <div className={(this.state.shownMenu ? "nav--fixed " : "") + "nav"}>
                    <div className="nav__header">
                        <Button className="nav__toggle" type="text" icon="menu" onClick={() => this.toggleMenu()}></Button>
                        <div className="logo">DEMO</div>
                        <Button className={"nav__fix" + (this.state.fixedMenu ? " nav__fix--fixed" : "")} type="text" onClick={() => this.fixMenu()}><i className="fa fa-thumb-tack"></i></Button>
                    </div>
                    <div className="menu">
                        <Menu theme="dark" mode="vertical" onSelect={this.onSelect}
                            uniqueOpened={true}
                            defaultOpeneds={this.state.defaultOpeneds}
                            ref={(menu) => {
                                if (!this.menu) {
                                    this.menu = menu;
                                }
                            }}>

                            <Menu.SubMenu index="user" key="user" className="submenu-fixed"
                                title={
                                    <span>
                                        <div className="menu-user__image">
                                            {window.user.imageURL ? <img src={window.user.imageURL}/> : <i className="fa fa-user"/>}
                                        </div>
                                        {window.user.username}
                                    </span>
                                }>
                                {
                                    window.languages.length > 1 &&
                                    window.languages.map(language =>
                                        <Menu.Item index={language.language}
                                            key={language.language}>
                                            {language.displayName}
                                        </Menu.Item>
                                    )
                                }
                                <Menu.Item index="/admin/user/logout"
                                    key={"item-logout"}>
                                    {i18n.t("user.logout")}
                                </Menu.Item>
                            </Menu.SubMenu>

                            {
                                this.hasPermission(["user.read"]) && <Menu.SubMenu index="/user" title={i18n.t("user.user")} key="/user"
                                    className={(this.isActive("/user") ? "is-active" : "")}>
                                    <Menu.Item index="/admin/user/list" key="/admin/user/list" className={this.isItemActive("/admin/user/list") ? "is-active" : ""}>
                                        {i18n.t("user.userList")}
                                    </Menu.Item>
                                    <Menu.Item index="/admin/user/role/list" key="/admin/user/role/list" className={this.isItemActive("/admin/user/role/list") ? "is-active" : ""}>
                                        {i18n.t("user.userGroupList")}
                                    </Menu.Item>
                                </Menu.SubMenu>
                            }

                            {
                                this.hasPermission(["product.read"]) &&
                                <Menu.Item index="/admin/product/list" key="/admin/product/list" className={this.isItemActive("/admin/product/list") ? "is-active" : ""}>
                                    Product
                                </Menu.Item>
                            }
                        </Menu>
                    </div>
                </div>
                <Button className="nav__toggle--fixed" type="text" icon="menu" onClick={() => this.toggleMenu()}></Button>
                <div className={"page-container" + (this.state.pageFixed ? " page-container--fixed" : "")}>
                    <Router history={history}>
                        <Switch>
                            <Route path="/admin/product/list" component={ProductList}/>
                            <Route path="/admin/product/create" component={ProductUpdate}/>
                            <Route path="/admin/product/:id/update" component={ProductUpdate}/>
                            <Route exact path="/admin/user/login" component={UserLogin}/>
                            <Route exact path="/admin/user/logout" component={UserLogout}/>
                            <Route exact path="/admin/user/list" component={UserList}/>
                            <Route exact path="/admin/user/role/list" component={UserGroupList}/>
                            <Route exact path="/admin/user/create" component={UserUpdate}/>
                            <Route exact path="/admin/user/:id/update" component={UserUpdate}/>
                            <Route exact path="/admin/user/:id/view" component={UserView}/>
                            <Route exact path="/admin/user/role/:id/update" component={UserGroupUpdate}/>
                            <Route exact path="/admin/user/role/:id/view" component={UserGroupView}/>
                            <Route exact path="/admin/user/role/create" component={UserGroupUpdate}/>
                            <Route component={Error404}/>
                        </Switch>
                    </Router>

                </div>
            </div>
        );
    }

    onSelect(index) {
        if (index.startsWith("/")) {
            history.push(index);
        } else {
            window.location.href = window.location.href + "?lang=" + index;
        }
    }
}

ReactDOM.render(<App/>, document.getElementById("app"));

