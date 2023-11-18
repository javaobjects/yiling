import store from '@/subject/pop/store'

function checkPermission(el, binding) {
  const { value } = binding
  const test = sessionStorage.getItem('ROUTE_TEST_MODEL') == 1
  if ((value instanceof Array && value.length && value[0] && process.env.NODE_ENV !== 'dev') || (process.env.NODE_ENV === 'dev' && test)) {
    const addRoutes = store.state.permission.addRoutes
    if (addRoutes.length > 0) {
      let find = 0
      for (let i=0; i<value.length; i++) {
        const valueItem = value[i]
        const identifyArray = valueItem.split(':')
        if (identifyArray.length > 2) {
          const module = addRoutes.find(item => item.meta && item.meta.identify === `${identifyArray[0]}:${identifyArray[1]}`)
          if (module) {
            const page = module.children.find(item => item.meta && item.meta.identify === valueItem)
            if (page) {
              find++
              break;
            }
          }
        }
      }
      if (!find) {
        // 移除元素
        el.parentNode && el.parentNode.removeChild(el)
      }
    } else {
      // 移除元素
      el.parentNode && el.parentNode.removeChild(el)
    }
  }
}

export default {
  inserted(el, binding) {
    checkPermission(el, binding)
  },
  update(el, binding) {
    if (JSON.stringify(binding.oldValue) !== JSON.stringify(binding.value)) {
      checkPermission(el, binding)
    }
  }
}
