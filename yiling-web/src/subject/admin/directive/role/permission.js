import store from '@/subject/admin/store'

// 查找按钮dom
// function findButtonElement(el) {
//   let className = el.getAttribute('class')
//   let isButton = false
//   let element = null
//   if (className) {
//     isButton = !!className.includes('el-button')
//   }
//   if (isButton) {
//     element = el
//   } else {
//     const dom = el.getElementsByClassName('el-button')
//     if (dom && dom.length) {
//       element = dom[0]
//     }
//   }
//   return element
// }

function checkPermission(el, binding) {
  const { value } = binding
  const test = sessionStorage.getItem('YY_ROUTE_TEST_MODEL') == 1
  if ((value instanceof Array && value.length && value[0] && process.env.NODE_ENV !== 'dev') || (process.env.NODE_ENV === 'dev' && test)) {
    const buttons = store.state.permission.currentButton
    if (buttons.length > 0) {
      const identify = sessionStorage.getItem('TO_ROUTER_IDENTIFY') || ''
      const permissionRoles = buttons.find(item => item.menuIdentification === `${identify}:${value[0]}`)
      if (!permissionRoles || permissionRoles.menuStatus !== 1) {
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
