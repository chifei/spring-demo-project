const path = require("path");
const webpack = require(path.resolve("node_modules/webpack"));
const fs = require("fs");
const StyleLintPlugin = require(path.resolve("node_modules/stylelint-webpack-plugin"));
const BundleAnalyzerPlugin = require('webpack-bundle-analyzer').BundleAnalyzerPlugin;

const styleOptions = JSON.parse(fs.readFileSync(path.resolve(__dirname, "./stylelint.json"), "utf8"));
let configFile = path.resolve(__dirname, "./eslint.json");

module.exports = (env, {mode}) => {
    const rules = [
        {
            test: /\.(js|jsx)$/,
            loader: "babel-loader",
            exclude: /node_modules/,
            options: {
                presets: ["es2015", "stage-2", "react"],
                babelrc: false,
                cacheDirectory: true
            }
        },
        {
            test: /\.(css|scss|sass)$/,
            use: [
                {loader: "style-loader"},
                {
                    loader: "css-loader",
                    options: {
                        minimize: {safe: true},
                        sourceMap: true
                    }
                }
            ]
        },
        {
            test: /\.less$/,
            use: [
                {loader: "style-loader"},
                {
                    loader: "css-loader",
                    options: {
                        minimize: {safe: true},
                        sourceMap: true
                    }
                },
                {loader: "less-loader"}
            ]
        },
        {
            test: /\.(png|jpe?g|gif|svg)$/,
            loader: "url-loader?limit=1000"
        },
        {
            test: /\.(woff|woff2|eot|ttf)$/,
            loader: "url-loader?limit=1000"
        }
    ];
    if (mode === "production") {
        rules.push({
            test: /\.jsx?$/,
            loader: "eslint-loader",
            exclude: /node_modules/,
            enforce: "pre",
            options: {
                parser: "babel-eslint",
                configFile: configFile,
                parserOptions: {
                    sourceType: "module",
                    ecmaFeatures: {jsx: true}
                },
                envs: ["es6", "browser"],
                failOnWarning: true,
                failOnError: true
            }
        });
    }
    return {
        plugins: [
            new StyleLintPlugin(styleOptions),
            new webpack.LoaderOptionsPlugin({
                options: {
                    eslint: {
                        camelcase: true,
                        emitErrors: false,
                        failOnHint: false,
                        failOnWarning: false,
                        failOnError: true,
                        fix: false
                    }
                }
            }),
            // new BundleAnalyzerPlugin({
            //     generateStatsFile: true,
            //     statsOptions: {source: false}
            // })
        ],
        module: {rules},
        resolve: {extensions: [".js", ".jsx"]},
        externals: {
            "react": "React",
            "react-dom": "ReactDOM",
            "react-router": "ReactRouter",
            "react-router-dom": "ReactRouterDom",
            "element-react": "ElementUI",
            "prop-types": "PropTypes"
        }
    };
};