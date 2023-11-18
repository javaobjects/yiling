<template>
  <div :class="{'hidden': hidden}" class="pagination-yl">
    <el-pagination
      :small="easy"
      :background="background"
      :current-page.sync="currentPage"
      :page-size.sync="pageSize"
      :layout="layout"
      :page-sizes="pageArr"
      :pager-count="pageCount"
      :total="total"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </div>
</template>

<script>
import { scrollTo } from '@/common/utils/scroll-to'

export default {
  name: 'Pagination',
  props: {
    // 是否启用简单模式
    easy: {
      type: Boolean,
      default: false
    },
    total: {
      required: true,
      type: Number
    },
    page: {
      type: Number,
      default: 1
    },
    limit: {
      type: Number,
      default: 10
    },
    // 大于多少页折叠
    pageCount: {
      type: Number,
      default: 7
    },
    layout: {
      type: String,
      default: 'total, sizes, prev, pager, next, jumper'
    },
    background: {
      type: Boolean,
      default: true
    },
    autoScroll: {
      type: Boolean,
      default: true
    },
    hidden: {
      type: Boolean,
      default: false
    },
    pageSizeList: {
      type: Array,
      default: () => []
    }
  },
  computed: {
    currentPage: {
      get() {
        return this.page
      },
      set(val) {
        this.$emit('update:page', val)
      }
    },
    pageSize: {
      get() {
        return this.limit
      },
      set(val) {
        this.$emit('update:limit', val)
      }
    },
    // 底部页码选择
    pageArr() {
      if (this.pageSizeList && this.pageSizeList.length > 0) {
        return this.pageSizeList
      } else {
        return [10, 20, 30]
      }
    }
  },
  methods: {
    handleSizeChange(val) {
      this.$emit('pagination', { page: this.currentPage, limit: val })
      if (this.autoScroll) {
        scrollTo(0, 800)
      }
    },
    handleCurrentChange(val) {
      this.$emit('pagination', { page: val, limit: this.pageSize })
      if (this.autoScroll) {
        scrollTo(0, 800)
      }
    }
  }
}
</script>

<style lang="scss">
  .pagination-yl {
    background: #fff;

    &.hidden {
      display: none;
    }
    /*
    .el-pagination {
      font-weight: 400;
      font-size: $font-size-base;
      .el-input {
        margin: 0;
      }
      .el-pagination__total {
        margin-right: 8px;
        font-weight: 400;
        height: 32px;
        line-height: 32px;
        color: $font-title-color;
      }
      .el-pagination__sizes {
        margin: 0 8px 0 0;
        font-weight: normal;
        color: $font-important-color;
        .el-input--mini {
          .el-input__inner {
            height: 32px;
            line-height: 32px;
          }
        }
      }
      .el-pager {
        li {
          margin: 0 8px 0 0;
          font-weight: normal;
          background-color: $white;
          color: $font-title-color;
          width: 32px;
          border-radius: 4px;
          border: 1px solid #DDDDDD;
          padding: 0;
          vertical-align: middle;
          display: inline-block;
          font-size: 14px;
          height: 32px;
          line-height: 32px;
          cursor: pointer;
          -webkit-box-sizing: border-box;
          box-sizing: border-box;
          text-align: center;
        }
        .btn-quicknext,
        .btn-quickprev {
          border: none;
          color: #DDDDDD;
        }
      }
      .btn-next,
      .btn-prev {
        background-color: $white;
        padding: 0;
        width: 32px;
        height: 32px;
        border-radius: 4px;
        border: 1px solid #DDDDDD;
        color: $font-title-color;
        font-size: $font-size-base;
        margin: 0 8px 0 0;
        .el-icon {
          font-size: 14px;
        }
      }
      .el-pagination__jump {
        margin-left: 8px;
        margin-right: 8px;
        font-weight: 400;
        height: 32px;
        line-height: 32px;
        color: $font-title-color;
        .el-input--medium {
          font-size: $font-size-base;
        }
      }
      .el-pagination__editor {
        width: 48px;
        height: 32px;
        margin: 0 8px;
        .el-input__inner {
          height: 32px;
          line-height: 32px;
        }
      }
    }
    */
  }
</style>
