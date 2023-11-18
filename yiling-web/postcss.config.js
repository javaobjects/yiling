
module.exports = {
  plugins: [
    require('autoprefixer')({
      overrideBrowserslist: ['last 7 versions']
    }),
    // require('postcss-cssnext')({}),
    // require('postcss-px-to-viewport')({
    //   // 视窗的宽度，对应的是我们设计稿的宽度，一般是750
    //   viewportWidth: 1440,
    //   // 视窗的高度，根据750设备的宽度来指定，一般指定1334，也可以不配置
    //   viewportHeight: 900,
    //   // 指定`px`转换为视窗单位值的小数位数
    //   unitPrecision: 4,
    //   // 指定需要转换成的视窗单位
    //   viewportUnit: "vw",
    //   // 指定不转换为视窗单位的类，可以自定义，可以无限添加,建议定义一至两个通用的类名
    //   selectorBlackList: ['.no_vw_'],
    //   // 小于或等于`1px`不转换为视窗单位，你也可以设置为你想要的值
    //   minPixelValue: 1,
    //   // 允许在媒体查询中转换`px`
    //   mediaQuery: false,
    //   exclude: [
    //     /node_modules/,
    //     // /src\/styles\/element-variables/
    //   ]
    // }),
    // require('postcss-viewport-units')({
    //   filterRule: rule => rule. selector. indexOf( '::after') === - 1 && rule. selector. indexOf( '::before') === - 1 && rule. selector. indexOf( ':after') === - 1 && rule. selector. indexOf( ':before') === - 1
    // }),
    require('cssnano')({
      "cssnano-preset-advanced": {
        zindex: false,
        autoprefixer: false
      }
    })
  ]
}
