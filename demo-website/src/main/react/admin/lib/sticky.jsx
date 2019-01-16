import React, {Component} from "react";

export class Sticky extends Component {
    componentDidMount() {
        window.addEventListener("scroll", Sticky.handleScroll);
    }

    componentWillUnmount() {
        window.removeEventListener("scroll", Sticky.handleScroll);
        const toolbar = document.querySelector(".toolbar");
        const page = document.querySelector(".page");
        if (toolbar) {
            toolbar.classList.remove("fixed");
        }
        if (page) {
            page.style.paddingTop = 0;
        }
    }

    static handleScroll() {
        const toolbar = document.querySelector(".toolbar");
        const page = document.querySelector(".page");
        let top = toolbar.getAttribute("top");
        if (!top) {
            top = window.scrollY + toolbar.getBoundingClientRect().top;
            toolbar.setAttribute("top", top);
        }
        if (window.scrollY >= top) {
            page.style.paddingTop = toolbar.clientHeight + "px";
            toolbar.classList.add("fixed");
        } else if (window.scrollY <= top) {
            page.style.paddingTop = 0;
            toolbar.classList.remove("fixed");
        }
    }

    render() {
        return <div />;
    }
}
