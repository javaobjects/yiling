<template>
  <div :class="myClass">
    <el-tree
      :data="dataList"
      :expand-on-click-node="false"
      check-on-click-node
      :node-key="nodeKey"
      :ref="myRef"
      :indent="indent"
      :current-node-key="currentNodeKey"
      @node-click="handleNodeClick"
      highlight-current
      :props="props">
    </el-tree>
  </div>
</template>

<script>
  import { queryDeptTree } from '@/subject/admin/api/views_sale/task_administration.js'
  export default {
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
      this.getList();
    },
    methods: {
      async getList() {
        let data = await queryDeptTree()
        if (data) {
          this.dataList = data
        }
      },
      handleNodeClick(data) {
        this.$emit('node-click', data)
      }
    }
  }
</script>

<style lang="scss" scoped>
  @import './index.scss';
</style>
