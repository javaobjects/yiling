<template>
  <div>
    <yl-dialog
      title="关联科室"
      width="570px"
      :visible.sync="showDialog"
      :show-footer="false"
      @confirm="confirm">
      <div class="pop-up">
        <el-tree
          show-checkbox
          node-key="id"
          default-expand-all
          :data="data"
          :expand-on-click-node="true"
          :default-checked-keys="defaultCheckedKeys"
          :current-node-key="currentNodeKey"
          @check-change="checkChange">
        </el-tree>
      </div>
    </yl-dialog>
  </div>
</template>

<script>
import { queryDepartmentList } from '@/subject/admin/api/content_api/article_video'
export default {
  props: {
    // 是否显示 弹窗
    show: {
      type: Boolean,
      default: true
    },
    // 从父级传递过来的 数据
    dataList: {
      type: Array,
      default: () => []
    },
    // 弹窗类型 1 关联疾病 2 关联药品 3关联科室
    dialogType: {
      type: Number,
      default: 0
    }
  },
  computed: {
    showDialog: {
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
      data: [],
      //默认选中的节点
      defaultCheckedKeys: [], 
      currentNodeKey: '',
      treeList: []
    }
  },
  mounted() {
    this.getList();
  },
  methods: {
    async getList() {
      let data = await queryDepartmentList()
      if (data) {
        this.data = data.list;
      }
      this.treeList = this.dataList;
      if (this.dataList && this.dataList.length > 0) {
        for (let i = 0; i < this.dataList.length; i ++) {
          this.defaultCheckedKeys.push(this.dataList[i].id)
        }
      }
    },
    // 节点选中状态发生变化时的回调
    checkChange(data, checked) {
      if (checked) {
        if (data.children && data.children.length < 1) {
          this.treeList.push(data);
        }
      } else {
        if (data.children && data.children.length < 1) {
          for (let i = 0; i < this.treeList.length; i ++) {
            if (this.treeList[i].id == data.id) {
              this.treeList.splice(i,1)
            }
          }
        }
      }
    },
    // 点击弹窗确定
    confirm() {
      this.$emit('addTree', this.treeList)
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>