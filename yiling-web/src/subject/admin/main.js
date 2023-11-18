import Vue from 'vue';
import ElementUI from 'element-ui';
import '@/common/styles/index.scss'
import '@/subject/admin/styles/index.scss'
import App from './App';
import store from './store';
import router from './router';
import * as common from '@/subject/admin/utils/index';
import * as filters from '@/common/filters';
import ylButton from '@/common/components/Button'
import ylDialog from '@/common/components/Dialog'
import ylPagination from '@/common/components/Pagination'
import ylToolTip from '@/common/components/ToolTip'
import ylSearchBtn from '@/common/components/SearchBtn'
import Permission from '@/subject/admin/directive/role'
import {
  ylTable
} from '@/subject/admin/components'
import '@/subject/admin/icons'
import '@/subject/admin/permission'

// medium、small、mini
Vue.use(ElementUI, {
  size: 'medium'
})

// 注册全局过滤器
Object.keys(filters).forEach(key => {
  Vue.filter(key, filters[key])
})

import { template } from '@/subject/admin/filters/link'
Vue.filter('template', template)

Vue.config.productionTip = false;

Vue.prototype.$store = store;
// 公共方法
Vue.prototype.$common = common;
// 打印，仅development模式有效
Vue.prototype.$log = common.log;

common.log(process.env)

// 引入
import VueDOMPurifyHTML from 'vue-dompurify-html'
Vue.use(VueDOMPurifyHTML)

Vue.component('ylButton', ylButton);
Vue.component('ylDialog', ylDialog);
Vue.component('ylPagination', ylPagination);
Vue.component('ylTable', ylTable);
Vue.component('ylSearchBtn', ylSearchBtn);
Vue.component('ylToolTip', ylToolTip);
Vue.directive('roleBtn', Permission);

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
});
