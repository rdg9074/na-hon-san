const CracoAlias = require("craco-alias");
const { BundleAnalyzerPlugin } = require("webpack-bundle-analyzer");
const CompressionPlugin = require("compression-webpack-plugin");

module.exports = {
  webpack: {
    plugins: [
      // new BundleAnalyzerPlugin({
      //   analyzerMode: "server",
      //   analyzerHost: "127.0.0.1",
      //   analyzerPort: 8888,
      //   openAnalyzer: true
      // }),
      // new CompressionPlugin({
      //   algorithm: "gzip",
      //   test: /\.js$/
      // })
      // new CompressionPlugin({
      //   algorithm: "gzip",
      //   test: /\.(js|html)$/,
      //   threshold: 10240, // 10kb
      //   minRatio: 0.8
      // })
    ]
  },
  style: {
    sass: {
      loaderOptions: {
        additionalData: `
          @import "src/assets/styles/_variables.scss";
          @import "src/assets/styles/_utils.scss";
        `
      }
    }
  },
  plugins: [
    {
      plugin: CracoAlias,
      options: {
        source: "tsconfig",
        baseUrl: "./src",
        tsConfigPath: "./tsconfig.extend.json"
      }
    }
  ]
};
