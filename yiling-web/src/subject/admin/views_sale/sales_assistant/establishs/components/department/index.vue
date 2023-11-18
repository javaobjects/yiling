<template>
  <yl-dialog 
    title="添加部门" 
    @confirm="confirm" 
    width="890px" 
    :visible.sync="departmentShow" 
    :show-footer="true"
    :show-confirm="!disabled">
    <div class="dialogTc">
      <el-tree
        ref="tree"
        :data="treeData"
        show-checkbox
        default-expand-all
        :default-checked-keys="checkedKeys"
        node-key="id"
        highlight-current
        :props="defaultProps"
        >
      </el-tree>
    </div>
  </yl-dialog>
</template>
<script>
import { queryDeptTree } from '@/subject/admin/api/views_sale/task_administration.js'
export default {
  name: 'Department',
  props: {
    show: {
      type: Boolean,
      default: true
    },
    dataCommodity: {
      type: Array,
      default: ()=> []
    },
    departmentData: {
      type: Array,
      default: ()=> []
    },
    //是否显示确定按钮
    disabled: {
      type: Boolean,
      default: true
    }
  },
  computed: {
    departmentShow: {
      get() {
        return this.show
      },
      set(val) {
        this.$emit('update:show', val)
      }
    }
  },
  data() {
    return {
      treeData: [],
      checkedKeys: [],
      defaultProps: {
        label: 'name'
      },
      recursionData: []
    }
  },
  mounted() {
    this.dataList();
    if (this.departmentData && this.departmentData.length > 0) {
      this.checkedKeys = this.departmentData
    }
  },
  methods: {
    //获取部门树组件
    async dataList() {
      let data = await queryDeptTree()
      if (data) {
        this.treeData = data
      }
    },
    confirm() {
      let val = this.$refs.tree.getCheckedKeys()
      this.$emit('departmentConfirm', val)
    }
    // recursion(treeData, val) {
    //   for (let y = 0; y < val.length; y ++) {
    //     for (let i = 0; i < treeData.length; i ++) {
    //       if (val[y] == treeData[i].id) {
    //           this.recursionData.push({
    //             id: treeData[i].id,
    //             cj: treeData[i].cj,
    //             name: treeData[i].name,
    //             children: []
    //           })
    //         val.splice(y, 1)
    //       }
    //       if (treeData[i].children) {
    //         this.recursion(treeData[i].children, val)
    //       }
    //     }
    //   }
    // }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>