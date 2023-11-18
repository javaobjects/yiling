<template>
  <div class="app-container-nav nav-view">
    <span class="left">您的位置：</span>
    <el-breadcrumb separator-class="el-icon-arrow-right" class="app-breadcrumb">
      <el-breadcrumb-item v-for="(item, index) in navigateList" :key="index">
        <span class="no-redirect" :class="{current: (navigateList.length - 1) == index}" v-if="!item.path || item.path == ''">{{ item.title }}</span>
        <a class="redirect" :class="{current: (navigateList.length - 1) == index}" v-else @click.prevent="handleLink(item)">{{ item.title }}</a>
      </el-breadcrumb-item>
    </el-breadcrumb>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
export default {
  props: {
      navList: {
        type: Array,
        default: () => []
      }
    },
  data() {
    return {
      navigateList: []
    }
  },
  watch: {
    navList: {
      handler(newValue, oldValue) {
        this.getBreadcrumb()
      },
      deep: true
    }
  },
  computed: {
    ...mapGetters([
      'hasDashboard'
    ])
  },
  created() {
    this.getBreadcrumb()
  },
  methods: {
    handleLink(item) {
      const { path, name } = item
      if (name && name != '') {
        this.$router.push({
          name: name
        })
      } else {
        this.$router.push(path)
      }
    },
    getBreadcrumb() {
      let list = [].concat(this.navList)
      if (!this.hasDashboard && list.length) {
        list.splice(0, 1)
      }
      this.navigateList = list
    }
  }
}
</script>

<style lang="scss" scoped>
.nav-view{
  display: flex;
  align-items: center;
  .left {
      font-size: $font-size-base;
      color: $font-title-color;
      font-weight: 500;
      margin-right: 10px;
  }
  .app-breadcrumb {
    &.el-breadcrumb {
      display: inline-block;
      color: #999999 !important;
      line-height: 20px;
      .no-redirect {
        color: #999999 !important;
        cursor: text;
        font-size: 14px !important;
      }
      .redirect {
        color: #999999 !important;
        font-size: 14px !important;
        &:hover{
          color: $primary !important;
        }
      }
      .current{
        color: $primary !important;
      }

    }
  }
}
</style>

