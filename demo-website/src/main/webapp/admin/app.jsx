import "babel-polyfill";
import React from "react";
import ReactDOM from "react-dom";
import {Redirect, Route, Router, Switch} from "react-router-dom";
import createHistory from "history/createBrowserHistory";
import {Button, Menu} from "element-react";
import "./lib/fetch";
import "./lib/vendor";
import Error404 from "./404";

import "./css/theme.css";
import "./css/main.css";
import "./css/iconfont.css";
import "./css/font-awesome.css";

const history = createHistory({basename: document.querySelector("base").getAttribute("href")});



class Page extends React.Component {
    constructor(props) {
        super(props);
        this.state = {app: window.app};
    }

    shouldComponentUpdate() {
        return false;
    }

    render() {
        return (
            <Router history={history}>
                <Switch>
                    <Route exact path="/admin/" render={() => <Redirect to="/admin/dashboard/report"/>}/>
                    <Route component={Error404}/>
                </Switch>
            </Router>
        );
    }
}

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
        window.app.menus.forEach((menu) => {
            if (menu.children && this.isActive(menu.path)) {
                defaultOpeneds.push(menu.path);
            }
        });
    }

    hasRoles(roles) {
        if (roles && roles.length) {
            for (let i = 0; i < roles.length; i += 1) {
                if (window.app.user.hasRole(roles[i])) {
                    return true;
                }
            }
            return false;
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
            <div className={window.app.user.id ? "" : "unauthenticated"}>
                <Sticky/>
                {!this.state.fixedMenu && this.state.shownMenu && <div className="nav__mask" onClick={() => this.toggleMenu()}></div>}
                <div className={(this.state.shownMenu ? "nav--fixed " : "") + "nav"}>
                    <div className="nav__header">
                        <Button className="nav__toggle" type="text" icon="menu" onClick={() => this.toggleMenu()}></Button>
                        <div className="logo">jWeb</div>
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
                                            {window.app.user.imageURL ? <img src={window.app.user.imageURL}/> : <i className="fa fa-user"/>}
                                        </div>
                                        {window.app.user.nickname}
                                    </span>
                                }>
                                {
                                    window.app.bundle("adminBundle").options.languages.length > 1 &&
                                    window.app.bundle("adminBundle").options.languages.map(language =>
                                        <Menu.Item index={language.value}
                                            key={language.value}>
                                            {language.displayName}
                                        </Menu.Item>
                                    )
                                }
                                <Menu.Item index="/admin/user/logout"
                                    key={"item-logout"}>
                                    {window.ElementUI.i18n.t("menu.logout")}
                                </Menu.Item>
                            </Menu.SubMenu>
                            {
                                window.app.menus.map((menu, i) => {
                                    if (this.hasRoles(menu.rolesAllowed)) {
                                        if (menu.children) {
                                            return (
                                                <Menu.SubMenu index={menu.path} title={menu.displayName} key={menu.path}
                                                    className={(this.isActive(menu.path) ? "is-active" : "") + (menu.path === "/admin/setting" ? " submenu-fixed" : "")}>
                                                    {menu.children.map((item, j) => {
                                                        if (this.hasRoles(item.rolesAllowed)) {
                                                            return (
                                                                <Menu.Item index={item.path} key={"item-" + i + "-" + j} className={this.isItemActive(item.path) ? "is-active" : ""}>
                                                                    {item.displayName}
                                                                </Menu.Item>
                                                            );
                                                        }
                                                    })}
                                                </Menu.SubMenu>
                                            );
                                        }
                                        return (
                                            <Menu.Item index={menu.path} key={"item-" + i}
                                                className={(this.isItemActive(menu.path) ? "is-active" : "") + (menu.path === "/admin/setting" ? " submenu-fixed" : "")}>
                                                {menu.displayName}
                                            </Menu.Item>
                                        );
                                    }
                                })
                            }
                        </Menu>
                    </div>
                </div>
                <Button className="nav__toggle--fixed" type="text" icon="menu" onClick={() => this.toggleMenu()}></Button>
                <div className={"page-container" + (this.state.pageFixed ? " page-container--fixed" : "")}>
                    <Page/>
                </div>
            </div>
        );
    }

    onSelect(index) {
        if (index.startsWith("/")) {
            history.push(index);
        } else {
            fetch("/admin/api/switch-language/" + index, {method: "GET"})
                .then(function() {
                    window.location.replace(window.location.href);
                });
        }
    }
}

ReactDOM.render(<App/>, document.getElementById("app"));

