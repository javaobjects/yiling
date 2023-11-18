<template>
  <div class="app-breadcrumb flex-row-left">
    <span class="font-title-color">您的位置：</span>
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <transition-group name="breadcrumb">
        <el-breadcrumb-item v-for="(item,index) in levelList" :key="item.path">
          <span v-if="item.redirect==='noRedirect'||index==levelList.length-1" class="no-redirect">{{ item.meta.title }}</span>
          <a class="redirect" v-else @click.prevent="handleLink(item)">{{ item.meta.title }}</a>
        </el-breadcrumb-item>
      </transition-group>
    </el-breadcrumb>
  </div>
</template>

<script>
import pathToRegexp from 'path-to-regexp'
import { mapGetters } from 'vuex'

export default {
  data() {
    return {
      levelList: null,
      title: ''
    }
  },
  computed: {
    ...mapGetters([
      'hasDashboard'
    ])
  },
  watch: {
    $route() {
      this.getBreadcrumb()
    }
  },
  created() {
    this.getBreadcrumb()
  },
  methods: {
    getBreadcrumb() {
      let matched = this.$route.matched.filter(item => item.meta && item.meta.title)
      if (matched.length) {
        const first = matched[0]

        if (first.meta && first.meta.breadcrumb === false) {
          return
        }
        if (!this.isDashboard(matched) && this.hasDashboard) {
          matched = [{ path: '/dashboard', meta: { title: '首页' }}].concat(matched)
        }

        this.levelList = matched.filter(item => item.meta && item.meta.title && item.meta.breadcrumb !== false)
      }
    },
    isDashboard(route) {
      if (route.length > 1) {
        const name = route[1].name
        if (!name) {
          return false
        }
        return name.trim().toLocaleLowerCase() === 'Dashboard'.toLocaleLowerCase()
      }
      return false
    },
    pathCompile(path) {
      const { params } = this.$route
      let toPath = pathToRegexp.compile(path)
      return toPath(params)
    },
    handleLink(item) {
      const { redirect, path } = item
      if (redirect) {
        this.$router.push(redirect)
        return
      }
      this.$router.push(this.pathCompile(path))
    }
  }
}
</script>

<style lang="scss" scoped>
  .app-breadcrumb {
    padding: 16px;
    font-size: 14px;
    &.el-breadcrumb {
      display: inline-block;
      font-size: 14px;
      line-height: $breadHeight;
      color: #999999;

      .no-redirect {
        color: $primary;
        cursor: text;
      }
      .redirect {
        color: #999999;
      }
    }
  }
</style>
