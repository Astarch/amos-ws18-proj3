const webpack = require('webpack');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const FriendlyErrorsPlugin = require('friendly-errors-webpack-plugin');
const BrowserSyncPlugin = require('browser-sync-webpack-plugin');

const pkg = require('../package.json');

const webpackConfig = {
    devtool: '#cheap-module-eval-source-map',
    devServer: {
        historyApiFallback: true,
        noInfo: true,
    },
    performance: {
        hints: false,
    },
    module: {
        rules: [{
            test: /\.scss$/,
            loaders: ['style-loader', 'css-loader', 'sass-loader'],
        }, {
            test: /\.css$/,
            loaders: ['style-loader', 'css-loader'],
        }, ],
    },
    plugins: [
        new webpack.NoEmitOnErrorsPlugin(),
        new BrowserSyncPlugin({
            host: 'localhost',
            port: pkg.config.port,
            // Proxy the default webpack dev-server port
            proxy: 'http://localhost:8080/',
            notify: false,
            open: false,
            // Let webpack handle the reload
            codeSync: false
        }),
        // https://github.com/ampedandwired/html-webpack-plugin
        new HtmlWebpackPlugin({
            filename: 'index.html',
            template: 'index.html',
            inject: true,
        }),
        new FriendlyErrorsPlugin(),
        new webpack.ProvidePlugin({
            $: 'jquery',
            jQuery: 'jquery',
            'window.jQuery': 'jquery',
            Popper: ['popper.js', 'default'],
        })
    ],
};
module.exports = webpackConfig;
