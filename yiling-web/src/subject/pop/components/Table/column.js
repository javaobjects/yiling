import Vue from 'vue'
export default Vue.component('column', {
  functional: true,
  props: {
    checkList: {
      type: Array,
      default: () => []
    }
  },
  render: function (createElement, context) {
    const checkList = context.props.checkList
    const el = [].concat(context.parent.$slots.default)
    if (checkList.length) {
      for (let i = checkList.length - 1; i >= 0; i--) {
        el.splice(checkList[i], 1)
      }
    }
    return el
  }
})
