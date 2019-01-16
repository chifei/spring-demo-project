import React from "react";
import PropTypes from "prop-types";
import {Table} from "element-react";
import "../css/tree-table.css";

export default class TreeTable extends React.Component {
    constructor(props) {
        super(props);
        const data = this.initData(props.treeData);
        this.state = {
            origin: data,
            treeData: data.concat([]),
            treeColumns: this.initColumn(props.keyColumn, props.treeColumns)
        };
        this.loadChildren = props.loadChildren;
    }

    static getDerivedStateFromProps(nextProps, prevState) {
        const data = initData(nextProps.treeData);
        return {
            origin: data,
            treeData: data.concat([])
        };
    }

    initData(list) {
        const result = [];
        this.traversal(list, result, 0);
        return result;
    }

    initChildren(list, depth) {
        const result = [];
        this.traversal(list, result, depth);
        return result;
    }

    traversal(firstLevels, list, depth) {
        for (let i = 0; i < firstLevels.length; i += 1) {
            const node = firstLevels[i];
            node.fold = false;
            node.index = list.length;
            node.depth = depth;
            list.push(node);
            this.traversal(node.children, list, depth + 1);
        }
    }

    initColumn(key, list) {
        const render = key.render;
        key.render = function (data) {
            return (
                <div className={"key-column depth-" + data.depth} onClick={() => this.click(data)}>
                    {data.children && data.children.length > 0
                        ? <span>
                            {data.fold
                                ? <i className="el-icon-arrow-right"></i>
                                : <i className="el-icon-arrow-down"></i>}
                        </span>
                        : <span>
                            {data.load
                                ? <span className="empty-children"></span>
                                : <span>
                                    {this.loadChildren
                                        ? <i className="el-icon-arrow-right"></i>
                                        : <span className="empty-children"></span>}
                                </span>}
                        </span>}
                    <span className="key-column-name">
                        {render ? render(data) : this.getValue(key.prop, data)}
                    </span>
                </div>
            );
        }.bind(this);
        key.width = 600;
        list.splice(0, 0, key);
        return list;
    }

    getValue(keyProp, data) {
        const splits = keyProp.split(".");
        let origin = data;
        for (let i = 0; i < splits.length; i += 1) {
            origin = origin[splits[i]];
        }
        return origin;
    }

    click(data) {
        if (data.children && data.children.length > 0) {
            if (data.fold) {
                this.setState({treeData: this.open(data)});
            } else {
                this.setState({treeData: this.fold(data)});
            }
        } else if (this.loadChildren) {
            this.loadChildren(data);
        }
    }

    open(data) {
        const list = this.state.treeData;
        const index = this.getIndex(data, this.state.treeData);
        list[index].fold = false;
        this.openFromOrigin(list, index, data);
        return list.concat([]);
    }

    openFromOrigin(list, currentIndex, data) {
        let count = 1;
        const origin = this.state.origin;
        for (let i = data.index + 1; i < origin.length; i += 1) {
            if (origin[i].depth > data.depth) {
                list.splice(currentIndex + count, 0, origin[i]);
                count += 1;
            } else {
                break;
            }
        }
    }

    fold(data) {
        const list = this.state.treeData;
        const index = this.getIndex(data, list);
        list[index].fold = true;
        let count = 0;
        for (let i = index + 1; i < list.length; i += 1) {
            if (list[i].depth > data.depth) {
                count += 1;
            } else {
                break;
            }
        }
        list.splice(index + 1, count);
        return list.concat([]);
    }

    getIndex(data, list) {
        for (let i = 0; i < list.length; i += 1) {
            if (list[i].id === data.id) {
                return i;
            }
        }
        return -1;
    }

    render() {
        return (
            <div className="el-tree-table">
                <Table
                    style={{width: "100%"}}
                    columns={this.state.treeColumns}
                    data={this.state.treeData}
                />
            </div>
        );
    }
}
TreeTable.propTypes = {
    treeData: PropTypes.object.treeData,
    treeColumns: PropTypes.array.treeColumns,
    keyColumn: PropTypes.array.keyColumn,
    loadChildren: PropTypes.func
};

function initData(list) {
    const result = [];
    traversal(list, result, 0);
    return result;
}

function traversal(firstLevels, list, depth) {
    for (let i = 0; i < firstLevels.length; i += 1) {
        const node = firstLevels[i];
        node.fold = false;
        node.index = list.length;
        node.depth = depth;
        list.push(node);
        traversal(node.children, list, depth + 1);
    }
}