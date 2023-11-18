<template>
  <div>
    <div class="box-view">
      <yl-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</yl-button>
      <yl-button class="btn" type="text" @click="handleReset">清空查询条件</yl-button>
    </div>
    <div class="font-size-sm font-light-color desc">（已为您检索出{{ total }}条数据）</div>
  </div>
</template>

<script>
  export default {
    name: 'Search',
    props: {
      // 数目
      total: {
        type: Number,
        default: 0
      }
    },
    data() {
      return {
      }
    },
    methods: {
      handleSearch() {
        this.$emit('search')
      },
      handleReset() {
        this.$emit('reset')
        setTimeout(() => {
          this.handleSearch()
        }, 50)
      }
    }
  }
</script>

<style lang="scss" scoped>
  .desc {
    margin-top: 16px;
    font-size: $font-size-base;
    color: $font-light-color;
  }
  .box-view {
    display: flex;
    flex-direction: row;
    justify-content: flex-start;
    align-items: flex-end;
    .btn {
      margin-left: 32px;
      &.el-button--medium {
        padding: 0 !important;
      }
    }
  }
</style>
