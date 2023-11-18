import { debounce } from '@/common/utils/index'

let debounceEle
let debounceFn
export default {
  bind(el, { name, value, oldValue, expression, arg, modifiers }) {
    debounceEle = debounce(value, 400, true)
    debounceFn = function(e){
      if (e.keyCode === 13) {
        debounceEle()
      }
    }
    const {enter, click} = modifiers
    if (enter) {
      // 元素上绑定回车事件
      el.addEventListener('keydown', debounceFn, false)
    }
    if (click) {
      el.addEventListener('click', debounceEle, false)
    }
  },
  unbind(el, { modifiers }) {
    const {enter, click} = modifiers
    if (enter) {
      el.removeEventListener('keydown', debounceFn)
    }
    if (click) {
      el.removeEventListener('click', debounceEle)
    }
    debounceEle = null
  }
}
