import Vue from 'vue'
import ElementUI from 'element-ui'
import '@/common/styles/index.scss'
import '@/subject/pop/styles/index.scss'
import App from './App'
import store from './store'
import router from './router'
import * as common from '@/subject/pop/utils/index'
import * as filters from '@/common/filters'
import ylButton from '@/common/components/Button'
import ylDialog from '@/common/components/Dialog'
import ylPagination from '@/common/components/Pagination'
import ylToolTip from '@/common/components/ToolTip'
import ylSearchBtn from '@/common/components/SearchBtn'
import ylTopNavView from '@/common/components/TopNavView'
import Permission from '@/subject/pop/directive/role'
import {
  ylTable
} from '@/subject/pop/components'

import '@/subject/pop/icons'
import '@/subject/pop/permission'

// medium、small、mini
Vue.use(ElementUI, {
  size: 'medium'
})

// Vue.prototype.$ELEMENT = { size: 'medium' }

// 注册全局过滤器
Object.keys(filters).forEach(key => {
  Vue.filter(key, filters[key])
})

import { template } from '@/subject/pop/filters/link'
Vue.filter('template', template)

Vue.config.productionTip = false

Vue.prototype.$store = store

// 公共方法
Vue.prototype.$common = common
// 打印，仅development模式有效
Vue.prototype.$log = common.log

// common.log(process.env)

Vue.component('ylButton', ylButton)
Vue.component('ylDialog', ylDialog)
Vue.component('ylPagination', ylPagination)
Vue.component('ylTable', ylTable)
Vue.component('ylSearchBtn', ylSearchBtn)
Vue.component('ylToolTip', ylToolTip)
Vue.component('ylTopNavView', ylTopNavView)
Vue.directive('roleBtn', Permission);

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
