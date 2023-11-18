<template>
  <div :class="myClass">
    <el-tree
      :data="dataList"
      :expand-on-click-node="false"
      check-on-click-node
      :node-key="nodeKey"
      :ref="myRef"
      :load="loadNode"
      :indent="indent"
      lazy
      :current-node-key="currentNodeKey"
      @node-click="handleNodeClick"
      highlight-current
      :props="props">
    </el-tree>
  </div>
</template>

<script>
  import { getDepartmentTreeList } from '@/subject/pop/api/zt_api/role'
  export default {
    name: 'DepartmentTree',
    components: {
    },
    props: {
      // 样式类别
      classType: {
        type: String,
        default: ''
      },
      // 数据
      data: {
        type: Array,
        default: () => []
      },
      nodeKey: {
        type: String,
        default: 'id'
      },
      myRef: {
        type: String,
        default: 'newTree'
      },
      currentNodeKey: {
        type: Number
      },
      props: {
        type: Object,
        default: () => {
          return {
            children: 'children', label: 'label'
          }
        }
      },
      indent: {
        type: Number,
        default: 16
      }
    },
    data() {
      return {
        dataList: []
      }
    },
    computed: {
      myClass() {
        let type = ''
        if (this.classType === '1') {
          type = 'depart-left-tree'
        }
        return type
      }
    },
    mounted() {
    },
    methods: {
      async getList(parentId, callback) {
        let data = await getDepartmentTreeList(parentId)
        if (callback) callback(data.list || [])
      },
      handleParent(e) {
        if (e && typeof e.data === 'object' && !Array.isArray(e.data) && Object.keys(e.data).length ) {
          this.nodeName.unshift(e.data.name)
          this.handleParent(e.parent)
        }
      },
      handleNodeClick(data, e) {
        this.nodeName = []
        this.handleParent(e.parent)
        this.nodeName.push(data.name)
        data.nodeName = this.nodeName.join('-')
        this.$emit('node-click', data)
      },
      loadNode(node, resolve) {
        if (node.level === 0) {
          this.getList('', (data) => {
            resolve(data)
          })
          return
        }
        this.getList(node.data.id, (data) => {
          resolve(data)
        })
      }
    }
  }
</script>

<style lang="scss" scoped>
  .depart-left-tree {
    ::v-deep .el-tree {
      .el-tree-node__content {
        height: 30px;
        margin-bottom: 8px;
        .el-tree-node__expand-icon {
          font-size: 14px;
          padding: 6px 8px 6px 0;
        }
        .el-tree-node__label {
          flex: 1;
          color: $font-important-color;
          height: 30px;
          border-radius: 4px;
          line-height: 30px;
          padding-left: 8px;
          border: 1px solid #D8D8D8;
        }
      }
      .el-tree-node__expand-icon.expanded {
        -webkit-transform: rotate(0deg);
        transform: rotate(0deg);
      }
      //有子节点 且未展开
      .el-icon-caret-right:before {
        background: url("../../../assets/role/department-expand-on.png") no-repeat center;
        content: '';
        display: block;
        width: 18px;
        height: 18px;
        font-size: 16px;
        background-size: 16px;
      }

      //有子节点 且已展开
      .el-tree-node__expand-icon.expanded.el-icon-caret-right:before {
        background: url("../../../assets/role/department-expand-off.png") no-repeat center;
        content: '';
        display: block;
        width: 18px;
        height: 18px;
        font-size: 16px;
        background-size: 16px;
      }

      //没有子节点
      .el-tree-node__expand-icon.is-leaf::before {
        background: transparent no-repeat center;
        content: '';
        display: block;
        width: 16px;
        height: 16px;
        font-size: 16px;
        background-size: 16px;
      }
    }
  }
</style>
