<template>
  <div>
    <el-scrollbar wrap-class="scrollbar-wrapper">
      <el-menu
        :default-active="activeMenu"
        :collapse="false"
        :background-color="variables.menuBg"
        :text-color="variables.menuText"
        :unique-opened="false"
        :active-text-color="variables.menuActiveText"
        :collapse-transition="false"
        mode="vertical"
      >
        <sidebar-item v-for="route in accessRoutes" :key="route.path" :item="route" :base-path="route.path" />
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import SidebarItem from './SidebarItem'
import variables from '@/common/styles/variables.scss'

export default {
  components: { SidebarItem },
  computed: {
    ...mapGetters([
      'sidebar',
      'accessRoutes'
    ]),
    activeMenu() {
      const route = this.$route
      const { meta, path } = route
      if (meta.activeMenu instanceof Array) {
        let active = meta.activeMenu
        let fromPath = sessionStorage.getItem('FROM_ROUTER_PATH')
        if (active.length === 1) {
          if (active[0] === '*') {
            return fromPath
          }
          return active[0]
        } else if (active.length > 1) {
          if (fromPath) {
            let nowPath = active.find(item => item && item === fromPath)
            return nowPath
          }
        }
      }
      return path
    },
    variables() {
      return variables
    }
  }
}
</script>
