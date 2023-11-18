'use strict'
const path = require('path')
const Config = require('./config/config')
const CompressionWebpackPlugin = require('compression-webpack-plugin')
const HtmlWebpackPlugin = require('html-webpack-plugin')
const { CleanWebpackPlugin } = require('clean-webpack-plugin')
const CopyWebpackPlugin = require('copy-webpack-plugin')

function resolve(dir) {
  return path.join(__dirname, dir)
}

let projectName = process.env.PRO_NAME
const timeStamp = new Date().getTime()
const isNotDev = process.env.NODE_ENV !== 'dev'

module.exports = {
  publicPath: isNotDev ? './' : '/',
  outputDir: 'dist',
  assetsDir: 'static',
  lintOnSave: !isNotDev,
  filenameHashing: false,
  productionSourceMap: !isNotDev,
  configureWebpack: config => {
    let obj = {
      name: Config[projectName].name,
      resolve: {
        alias: {
          '@': resolve('src')
        }
      },
      entry:{
        // app: find`./src/subject/${projectName}/main.js`
        app: `./src/subject/${projectName}/main.js`
      },
      output: {
        filename: `static/js/[name].${ timeStamp }.js`,
        chunkFilename: `static/js/[name].${ timeStamp }.js`
      },
      plugins: [
        new CleanWebpackPlugin(),
        new HtmlWebpackPlugin({
          minify:{
            collapseWhitespace: true
          },
          title: Config[projectName].name,
          favicon: resolve('public/favicon.ico'),
          cdn: isNotDev ? Config[projectName].cdn : { css: Config[projectName].cdn.css },
          alwaysCdn: Config[projectName].alwaysCdn,
          filename: `./index.html`,
          template: `./index.html`,
          inject: true
        })
      ]
    }
    if (isNotDev) {
      obj.externals = {
        'vue': 'Vue',
        'vuex': 'Vuex',
        'vue-router': 'VueRouter',
        'axios': 'axios',
        'element-ui': 'ELEMENT',
        'nprogress': 'NProgress',
        'echarts': 'echarts',
        'wangeditor': 'wangEditor',
        'lodash': '_'
      }
      obj.plugins.push(
        new CopyWebpackPlugin([
          {
            from: resolve('src/common/package'),
            to: resolve('dist/static/package')
          }
        ])
      )
      obj.plugins.push(
        new CompressionWebpackPlugin({
          algorithm: 'gzip',
          test: /\.(js|css)$/,// 匹配文件名
          threshold: 10240, // 对超过10k的数据压缩
          deleteOriginalAssets: false, // 不删除源文件
          minRatio: 0.8 // 压缩比
        })
      )
    }
    return obj
  },
  css: {
    loaderOptions: {
      sass: {
        prependData: `@import "src/common/styles/variables.scss";`,
      }
    },
    extract:{
      filename:`static/css/[name].${ timeStamp }.css`,
      chunkFilename:`static/css/[name].${ timeStamp }.css`,
    }
  },
  chainWebpack(config) {
    // 首屏加速
    config.plugin('preload').tap(() => [
      {
        rel: 'preload',
        fileBlacklist: [/\.map$/, /hot-update\.js$/, /runtime\..*\.js$/],
        include: 'initial'
      }
    ])
    config.plugins.delete('prefetch')
    // svg-sprite-loader
    config.module
      .rule('svg')
      .exclude.add(resolve(`src/subject/${projectName}/icons`))
      .end()
    config.module
      .rule('icons')
      .test(/\.svg$/)
      .include.add(resolve(`src/subject/${projectName}/icons`))
      .end()
      .use('svg-sprite-loader')
      .loader('svg-sprite-loader')
      .options({
        symbolId: 'icon-[name]'
      })
      .end()
    config.when(isNotDev, config => {
      config.optimization.splitChunks({
        chunks: 'all',
        cacheGroups: {
          libs: {
            name: 'chunk-libs',
            test: /[\\/]node_modules[\\/]/,
            priority: 10,
            chunks: 'initial'
          },
          elementUI: {
            name: 'chunk-elementUI',
            priority: 20,
            test: /[\\/]node_modules[\\/]_?element-ui(.*)/
          },
          commons: {
            name: 'chunk-commons',
            test: resolve(`src/subject/${projectName}/components`),
            minChunks: 2,
            priority: 15,
            reuseExistingChunk: true
          },
          echarts: {
            name: "chunk-echarts",
            test: /[\\/]node_modules[\\/](vue-)?echarts[\\/]/,
            chunks: "all",
            priority: 4,
            reuseExistingChunk: true,
            enforce: true
          }
        }
      })
      config.optimization.runtimeChunk('single')
    })
    config.when(process.env.useAnalyzer,
      config => {
        config.plugin("webpack-bundle-analyzer").use(require("webpack-bundle-analyzer").BundleAnalyzerPlugin);
      }
    )
  }
}
