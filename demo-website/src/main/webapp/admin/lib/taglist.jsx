import React from "react";
import PropTypes from "prop-types";
import {Button, Input, Tag} from "element-react";
import "../css/tag-list.css";

export default class TagList extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            adding: false,
            draft: "",
            label: props.label,
            list: props.list || [],
            readOnly: props.readOnly,
            onChangeHandler: props.onChange
        };
    }

    static getDerivedStateFromProps(nextProps) {
        return {list: nextProps.list};
    }

    onChange() {
        if (this.state.onChangeHandler) {
            this.state.onChangeHandler(this.state.list);
        }
    }

    onClose(index) {
        this.state.list.splice(index, 1);
        this.forceUpdate();
    }

    toggleAdding() {
        this.setState({adding: !this.state.adding});
    }

    onTagKeyUp(e) {
        if (e.keyCode === 13) {
            this.addTag();
        }
    }

    onDraftChanged(value) {
        this.setState({draft: value});
    }

    addTag() {
        const list = this.state.list;
        if (this.state.draft && this.state.list.indexOf(this.state.draft) < 0) {
            list.push(this.state.draft);
        }
        this.setState({
            list: list,
            draft: ""
        });
        this.onChange();
    }

    render() {
        return (
            <div className="el-tag-list">
                {this.state.list.map((tag, index) =>
                    <Tag
                        key={tag}
                        closable={!this.state.readOnly}
                        onClose={() => this.onClose(index)}>{tag}</Tag>
                )}
                {!this.state.readOnly && <Button size="small" className="button-new-tag"
                    onClick={() => this.toggleAdding()}>{this.props.label}</Button>}
                {!this.state.readOnly && <Input
                    className={"input-new-tag " + (this.state.adding && "open")}
                    value={this.state.draft}
                    onChange={value => this.onDraftChanged(value)}
                    onKeyUp={e => this.onTagKeyUp(e)}
                    onBlur={(e) => {
                        this.addTag();
                        this.setState({adding: false});
                    }}
                />}
            </div>
        );
    }
}

TagList.defaultProps = {
    list: [],
    readOnly: false,
    label: "New tag"
};

TagList.propTypes = {
    label: PropTypes.string,
    list: PropTypes.array,
    readOnly: PropTypes.bool,
    onChange: PropTypes.func
};
