module.exports = {
  root: true,
  parserOptions: {
    parser: 'babel-eslint'
  },
  env: {
    browser: true
  },
  extends: [
    'plugin:vue/recommended',
    'eslint:recommended'
  ],
  plugins: [
    'vue'
  ],
  rules: {
    'vue/html-self-closing': 0,
    'vue/attributes-order': 0,
    'vue/max-attributes-per-line': 0,
    'vue/html-indent': 0,
    'vue/html-closing-bracket-newline': 0,
    'vue/singleline-html-element-content-newline': 0,
    'vue/multiline-html-element-content-newline': 0,

    'generator-star-spacing': 'off',
    'no-debugger': process.env.NODE_ENV === 'production' ? 'error' : 'off',
    // 忽略callback传值不规范问题
    'standard/no-callback-literal': 0,
    'prefer-promise-reject-errors': ['error', {
      'allowEmptyReject': true
    }],
    // 字符串必须使用单引号
    'quotes': [1, 'single'],
    // 对象字面量项尾不能有逗号
    'comma-dangle': [2, 'never'],
    // 必须使用合法的typeof的值
    'valid-typeof': 1,
    // 禁止覆盖原生对象
    'no-native-reassign': 2,
    // 关键字后边加空格
    'keyword-spacing': 1,
    // 函数声明时括号与函数名间加空格
    'space-before-function-paren': 0,
    // 始终使用 === 替代 ==。obj == null 可以用来检查 null || undefined。
    'eqeqeq': 0,
    // 不强制使用驼峰
    'camelcase': 2,
    // 对象key重复
    'no-dupe-keys': 2,
    // 遇到分号时空格要后留前不留
    'semi-spacing': 1,
    'vue/no-dupe-keys': 2,
    'vue/valid-v-model': 0,
    // 禁止出现语法错误
    'vue/no-parsing-error': 2,
    'vue/order-in-components': 0,
    'vue/html-closing-bracket-spacing': 0,
    'vue/no-use-v-if-with-v-for': ['error', {
      'allowUsingIterationVar': false
    }],
    // 对象字面量中冒号的前后空格
    'key-spacing': [1, {
      'beforeColon': false,
      'afterColon': true
    }],
    // 关闭默认props
    'vue/require-default-prop': 0,
    // 禁止在条件中使用常量表达式 if(true) if(1)
    'no-constant-condition': 1,
    'no-class-assign': 2,
    'no-cond-assign': 2,
    'no-const-assign': 2,
    'no-control-regex': 0,
    'no-delete-var': 2,
    'no-dupe-args': 2,
    'no-dupe-class-members': 2,
    'no-duplicate-case': 2,
    'no-empty-character-class': 2,
    'no-empty-pattern': 2,
    'no-empty': 1,
    'no-eval': 2,
    'no-ex-assign': 2,
    'no-extend-native': 2,
    'no-extra-bind': 2,
    'no-extra-boolean-cast': 2,
    'no-extra-parens': [2, 'functions'],
    'no-fallthrough': 2,
    'no-floating-decimal': 2,
    'no-func-assign': 2,
    'no-implied-eval': 2,
    'no-inner-declarations': [2, 'functions'],
    'no-invalid-regexp': 2,
    'no-irregular-whitespace': 2,
    'no-iterator': 2,
    'no-label-var': 2,
    'no-labels': [2, {
      'allowLoop': false,
      'allowSwitch': false
    }],
    'no-lone-blocks': 2,
    'no-mixed-spaces-and-tabs': 2,
    'no-multi-spaces': 2,
    'no-multi-str': 0,
    'no-multiple-empty-lines': [2, {
      'max': 1
    }],
    'no-negated-in-lhs': 2,
    'no-new-object': 2,
    'no-new-require': 2,
    'no-new-symbol': 2,
    'no-new-wrappers': 2,
    'no-obj-calls': 2,
    'no-octal': 2,
    'no-octal-escape': 2,
    'no-path-concat': 2,
    'no-redeclare': 2,
    'no-regex-spaces': 2,
    'no-return-assign': [2, 'except-parens'],
    'no-self-assign': 2,
    'no-self-compare': 2,
    'no-sequences': 2,
    'no-shadow-restricted-names': 2,
    'no-spaced-func': 2,
    'no-sparse-arrays': 2,
    'no-this-before-super': 2,
    'no-throw-literal': 2,
    'no-undef': 0,
    'no-undef-init': 2,
    'no-unexpected-multiline': 2,
    'no-unmodified-loop-condition': 2,
    'no-unneeded-ternary': [2, {
      'defaultAssignment': false
    }],
    'no-unreachable': 2,
    'no-unsafe-finally': 2,
    'no-unused-vars': [2, {
      'vars': 'all',
      'args': 'none'
    }],
    // 没必要的return
    'no-else-return': 0,
    // 不允许出现不必要的分号
    'no-extra-semi': 2,
    // 不允许对null用==或者!=
    'no-eq-null': 1,
    'no-useless-call': 2,
    'no-useless-computed-key': 2,
    'no-useless-constructor': 2,
    'no-useless-escape': 0,
    'no-whitespace-before-property': 2,
    'no-with': 2,
    'one-var': [2, {
      'initialized': 'never'
    }]
  }
}
