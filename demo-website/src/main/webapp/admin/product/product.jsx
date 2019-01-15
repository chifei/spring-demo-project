import React from "react";
import {Route} from "react-router-dom";

import ProductUpdate from "./product.update";
import ProductList from "./product.list";


export default function AffiliateIndex() {
    return (
        <div>
            <Route path="/admin/product/list" component={ProductList}/>
            <Route path="/admin/product/create" component={ProductUpdate}/>
            <Route path="/admin/product/:id/update" component={ProductUpdate}/>
        </div>
    );
}