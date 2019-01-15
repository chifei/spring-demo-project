const path = require('path');
const webpack = require("webpack");
const fs = require('fs');
const StyleLintPlugin = require("stylelint-webpack-plugin");
const ExtractTextPlugin = require("extract-text-webpack-plugin");

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
                cacheDirectory: true,
                plugins: ["syntax-dynamic-import"]
            }
        },
        {
            test: /\.(css|scss|sass)$/,
            use: ExtractTextPlugin.extract({
                use: [{
                    loader: "css-loader",
                    options: {
                        minimize: {safe: true},
                        sourceMap: true
                    }
                }],
                fallback: "style-loader"
            })
        },
        {
            test: /\.less$/,
            use: ExtractTextPlugin.extract({
                use: [
                    {
                        loader: "css-loader",
                        options: {
                            minimize: {safe: true},
                            sourceMap: true
                        }
                    },
                    {loader: "less-loader"}
                ],
                fallback: "style-loader"
            })
        },
        {
            test: /\.(html)$/,
            use: {
                loader: "html-loader",
                options: {attrs: [":data-src"]}
            }
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
            // new BundleAnalyzerPlugin(),
            new ExtractTextPlugin("[name].min.css")
        ],
        module: {rules},
        resolve: {extensions: [".js", ".jsx"]}
    };
};