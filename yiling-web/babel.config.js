const plugins = []
if (process.env.NODE_ENV === 'production') {
  plugins.push("transform-remove-console")
}
module.exports = {
  presets: [
    '@vue/cli-plugin-babel/preset'
  ],
  plugins,
  'env': {
    'development': {
      // 加快热更新速度
      'plugins': ['dynamic-import-node']
    }
  }
}
