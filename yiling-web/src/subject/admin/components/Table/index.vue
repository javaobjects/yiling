<template>
  <div
    class="yl-table-eh"
    :class="{ 'no-border': !border, 'no-pad': cellNoPad, 'has-border': border }"
  >
    <div style="height: 16px;" v-if="headerFilter">
      <el-popover
        placement="top-start"
        width="200"
        trigger="click">
        <div>
          <el-checkbox :indeterminate="checkBox.isIndeterminate" v-model="checkBox.checkAll" @change="handleCheckAllChange">全选</el-checkbox>
          <el-checkbox-group v-model="checkList" @change="handleCheckedChange">
            <el-checkbox
              style="display: block;"
              v-for="(item, index) in columnList"
              :key="item.columnId"
              v-model="item.check"
              @change="e => handleChange(e, item, index)"
              :label="item.label">
              {{ item.label }}
            </el-checkbox>
          </el-checkbox-group>
        </div>
        <svg-icon style="cursor: pointer;" slot="reference" icon-class="filter"></svg-icon>
      </el-popover>
    </div>
    <el-table
      ref="table"
      :stripe="stripe"
      :row-style="rowStyle"
      :row-class-name="rowClassName"
      :cell-class-name="cellClassName"
      :border="border"
      :highlight-current-row="false"
      :header-cell-style="headerCellStyle"
      :header-cell-class-name="headerCellClassName"
      :show-header="showHeader"
      @expand-change="expandChange"
      @selection-change="selectionChange"
      @select="select"
      @select-all="selectAll"
      :data="list"
      v-loading="loading"
      :height="height"
      :max-height="maxHeight"
      :row-key="rowKey"
      @row-click="rowClick"
      :tree-props="treeProps"
      style="width: 100%"
      :empty-text="emptyText"
      :show-summary="showSummary"
      :sum-text="sumText"
      :summary-method="summaryMethod"
      :span-method="spanMethod"
    >
      <column v-if="checkBox.update" :check-list="handleList">
      </column>
    </el-table>
    <div v-show="total > 0 && showPagin" class="bg-white flex-row-right pagin">
      <yl-pagination
        :page-count="pageCount"
        :total="total"
        :page.sync="currentPage"
        :limit.sync="pageSize"
        :page-size-list="pageSizeList"
        @pagination="getData"
      />
    </div>
  </div>
</template>

<script>
  import column from './column.js'
  export default {
    name: 'YlTable',
    components: {
      column
    },
    props: {
      list: {
        type: Array,
        default: () => []
      },
      page: {
        type: Number,
        default: 1
      },
      limit: {
        type: Number,
        default: 10
      },
      total: {
        type: Number,
        default: 0
      },
      // 固定高度
      height: {
        type: [Number, String],
        default: null
      },
      // 最大高度
      maxHeight: {
        type: Number,
        default: null
      },
      loading: {
        type: Boolean,
        default: false
      },
      // 是否展示表头
      showHeader: {
        type: Boolean,
        default: false
      },
      // 是否展示分页
      showPagin: {
        type: Boolean,
        default: true
      },
      headerCellStyle: {
        type: [Function, Object],
        default: () => {}
      },
      headerCellClassName: {
        type: Function,
        default: () => {
          return 'header-1';
        }
      },
      rowStyle: {
        type: [Function, Object],
        default: () => {}
      },
      rowClassName: {
        type: Function,
        default: () => {
          return 'row-1';
        }
      },
      cellClassName: {
        type: Function,
        default: () => {
          return '';
        }
      },
      // 纵向边框
      border: {
        type: Boolean,
        default: false
      },
      // 横向边框 默认有
      noBorder: {
        type: Boolean,
        default: true
      },
      selectionChange: {
        type: Function,
        default: () => {}
      },
      select: {
        type: Function,
        default: () => {}
      },
      selectAll: {
        type: Function,
        default: () => {}
      },
      // 是否为斑马纹
      stripe: {
        type: Boolean,
        default: false
      },
      // cell是否有pad 默认有
      cellNoPad: {
        type: Boolean,
        default: false
      },
      // 树形数据时，该属性是必填的
      rowKey: {
        type: String,
        default: ''
      },
      // 渲染嵌套数据的配置选项
      treeProps: {
        type: Object,
        default: () => {
          return { children: 'children', hasChildren: 'hasChildren' };
        }
      },
      pageCount: {
        type: Number
      },
      emptyText: {
        type: String,
        default: '暂无数据'
      },
      showSummary: {
        type: Boolean,
        default: false
      },
      sumText: {
        type: String,
        default: '合计'
      },
      summaryMethod: {
        type: Function,
        default: () => []
      },
      pageSizeList: {
        type: Array,
        default: () => []
      },
      spanMethod: {
        type: Function,
        default: () => {}
      },
      // 列过滤
      headerFilter: {
        type: Boolean,
        default: false
      }
    },
    computed: {
      currentPage: {
        get() {
          return this.page;
        },
        set(val) {
          this.$emit('update:page', val);
        }
      },
      pageSize: {
        get() {
          return this.limit;
        },
        set(val) {
          this.$emit('update:limit', val);
          this.$emit('update:page', 1);
        }
      }
    },
    data() {
      return {
        checkBox: {
          isIndeterminate: false,
          checkAll: true,
          update: true
        },
        handleList: [],
        checkList: [],
        columnList: []
      };
    },
    mounted() {
      this.$nextTick(() => {
        const myTable = this.$refs.table
        if (myTable.$children.length) {
          let columnList = []
          myTable.$children.forEach(item => {
            if (item.$options._componentTag === 'el-table-column') {
              columnList.push({
                label: item.label,
                columnId: item.columnId
              })
            }
          })
          this.columnList = columnList
          this.checkList = columnList.map(i => { return i.label })
        }
      })
    },
    methods: {
      handleChange(val, item, index) {
        let arr = [].concat(this.handleList)
        if (val) {
          const findIndex = arr.findIndex(i => i === index)
          if (findIndex > -1) {
            arr.splice(findIndex, 1)
          }
        } else {
          arr.push(index)
        }
        arr.sort((a, b) => { return a - b })
        this.handleList = arr
        this.checkBox.update = false
        setTimeout(() => {
          this.checkBox.update = true
        }, 10)
      },
      handleCheckAllChange(val) {
        const list = this.columnList.map(i => { return i.label })
        this.checkList = val ? list : []
        this.checkBox.isIndeterminate = false
        if (val) {
          this.handleList = []
        } else {
          let arr = []
          list.map((i, n) => {
            arr.push(n)
          })
          this.handleList = arr
        }
        this.checkBox.update = false
        setTimeout(() => {
          this.checkBox.update = true
        }, 10)
      },
      handleCheckedChange(value, e) {
        const checkedCount = value.length
        const list = this.columnList.map(i => { return i.label })
        this.checkBox.checkAll = checkedCount === list.length
        this.checkBox.isIndeterminate = checkedCount > 0 && checkedCount < list.length
      },
      getData({ page, limit }) {
        this.$emit('getList', page, limit);
      },
      uuid(row) {
        return Math.random().toString(36).substr(2, 15);
      },
      expandChange(row, status) {
        this.$emit('expand-change', row, status);
      },
      rowClick(row, column, event) {
        this.$emit('row-click', row, column, event);
      },
      toggleRowSelectionMethod(row) {
        this.$refs.table.toggleRowSelection(row)
      },
      clearSelection() {
        this.$refs.table.clearSelection()
      }
    }
  };
</script>

<style lang="scss">
  .yl-table-eh {
    .pagin {
      padding: 16px;
    }
    .header-1 {
      font-size: $font-size-small;
      color: $font-title-color;
      background-color: $back-color-1;
      border-bottom: none !important;
      border-right: 1px solid #eeeeee;
      font-weight: 400;
    }
    .el-table {
      border-radius: 4px 4px 0px 0px;
    }
    .el-table--border,
    .el-table--group {
      border: none !important;
    }
    .row-1 {
      color: #333333;
      font-size: 14px;
      line-height: 22px;
      font-weight: 400;
      border: none !important;
    }
    /* 去除hover样式 */
    .el-table--enable-row-hover .el-table__body tr:hover > td {
      background-color: rgba(0, 0, 0, 0) !important;
    }
  }
  .no-border {
    .el-table td,
    .el-table th.is-leaf {
      border: none;
    }
  }
  .has-border {
    .el-table {
      td,
      th {
        border-right: 1px solid #eeeeee;
        &:first-child {
          border-left: 1px solid #eeeeee;
        }
      }
      th.is-leaf {
        border-bottom: 1px solid #eeeeee;
      }
      .el-table__header {
        td,
        th {
          border-top: 1px solid #eeeeee;
        }
      }
      .el-table__empty-block{
        border-left: 1px solid #EEEEEE;
      }
    }
  }
  .no-pad {
    .el-table .cell {
      padding-left: 0;
      padding-right: 0;
    }
    .el-table td {
      padding: 0;
    }
  }
</style>
