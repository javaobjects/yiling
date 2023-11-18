<template>
  <div class="app-container">
    <div class="app-container-content">

      <div class="mar-tb-16">
        <yl-button type="primary" @click="add">新建分类</yl-button>
      </div>
      <div class="search-bar">
        <div class="class-tree pad-t-8">
          <el-tree class="filter-tree" accordion :expand-on-click-node="false" :data="list" :props="defaultProps" node-key="id" ref="tree">
            <div class="custom-tree-node" slot-scope="{ node, data }">
              <div>
                <el-input v-if="isEdit && data.parentId === EditParentId && data.id === editId" v-model="editName" placeholder="" size="normal" clearable @focus.stop=""></el-input>
                <span v-else>{{ data.name }}</span>

              </div>
              <!-- 操作按钮 -->
              <div>
                <yl-button type="text" v-if="isEdit && data.parentId === EditParentId && data.id === editId" @click="save(data,node)">确定</yl-button>
                <yl-button type="text" v-if="isEdit && data.parentId === EditParentId && data.id === editId" @click="cancel(data,node)">取消</yl-button>
                <yl-button type="text" v-else @click="edit(data,node)">编辑</yl-button>
                <yl-button type="text" v-if="node.level === 1" @click="addSecondCate(data,node)">添加二级分类</yl-button>
                <yl-button type="text" v-if="node.level === 2" @click="changeCate(data,node)">归类</yl-button>
              </div>
            </div>
          </el-tree>
        </div>
        <!-- 新增一级分类，二级分类 -->
        <yl-dialog :visible.sync="show" :title="title" :show-cancle="false" :show-footer="false">
          <div class="dialog-content">
            <el-form ref="form" :model="form" :rules="rules" label-width="216px" label-position="right">
              <el-form-item v-if="addType === 'addFirst'" label="选择分类级别：" prop="parentId">
                <el-radio-group v-model="form.parentId">
                  <el-radio v-for="item in options" :label="item.value" :key="item.value">
                    {{ item.label }}
                  </el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item v-if="addType === 'addSecond'" label="一级分类：" prop="parentId">
                <el-select v-model="form.parentId" disabled>
                  <el-option v-for="item in list" :key="item.id" :label="item.name" :value="item.id"></el-option>
                </el-select>
              </el-form-item>

              <el-form-item :label="addLabel" prop="name">
                <el-input v-model="form.name" placeholder="请输入分类名称" />
              </el-form-item>
              <el-form-item>
                <yl-button type="primary" @click="onSubmit('form')">确定</yl-button>
                <yl-button @click="resetForm('form')">取消</yl-button>
              </el-form-item>
            </el-form>

          </div>
        </yl-dialog>
        <!-- 二级分类 归类 -->
        <yl-dialog :visible.sync="show1" title="归类" :show-cancle="false" :show-footer="false">
          <div class="dialog-content">
            <ylToolTip class="mar-b-16">*归类后将把此二级分类下的所有产品转移至新的一级类目下</ylToolTip>
          </div>
          <div class="dialog-content">
            <el-form ref="form1" :model="form1" :rules="rules1" label-width="216px" label-position="right">
              <el-form-item label="一级分类：" prop="parentId">
                <el-select v-model="form1.parentId">
                  <el-option :disabled="disableId == item.id" v-for="item in list" :key="item.id" :label="item.name" :value="item.id"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item>
                <yl-button type="primary" @click="onSubmit1('form1')">确定</yl-button>
                <yl-button @click="resetForm1('form1')">取消</yl-button>
              </el-form-item>
            </el-form>

          </div>
        </yl-dialog>
      </div>

    </div>
  </div>
</template>

<script>
import { getGoodsClassCate, editCateInfo, saveStandardGoodsCate, updateTypeStandardGoodsCate } from '@/subject/admin/api/quality'
export default {
  name: 'DrugsClassIndex',
  data() {
    return {
      list: [],
      defaultProps: {
        children: 'children',
        label: 'name'
      },
      isEdit: false,
      editName: '',
      EditParentId: '',
      editId: '',
      //   新建弹窗
      show: false,
      form: {
        name: '',
        parentId: ''
      },
      rules: {
        name: [
          { required: true, message: '请输入分类名称', trigger: 'blur' }
        ],
        parentId: [
          { required: true, message: '请选择分类级别', trigger: 'change' }
        ]

      },
      show1: false,
      // 编辑二级分类， 归类
      form1: {
        id: '',
        parentId: ''
      },
      rules1: {
        parentId: [
          { required: true, message: '请选择分类', trigger: 'change' }
        ]
      },
      options: [
        { value: 0, label: '一级分类' }
      ],
      title: '分类添加',
      //   addFirst 添加一级分类   addSecond 添加二级分类
      addType: '',
      addLabel: '新增一级分类名称：',
      disableId: ''
    }

  },
  activated() {
    this.getCate();
  },
  methods: {
    //   新增一级分类
    add() {
      this.form.name = ''
      this.form.parentId = ''
      this.addType = 'addFirst'
      this.title = '分类添加'
      this.addLabel = '新增一级分类名称：'
      this.show = true
    },
    //  提交 一级分类，二级分类 新增
    onSubmit(formName) {
      this.$refs[formName].validate(async (valid) => {
        if (valid) {
          this.$common.showLoad()
          let data = await saveStandardGoodsCate(this.form.name, this.form.parentId)
          console.log(data);
          this.$common.hideLoad()
          if (data) {
            this.show = false
            this.getCate();
          }
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
      this.show = false
    },
    // 二级分类，归类编辑
    onSubmit1(formName) {
      this.$refs[formName].validate(async (valid) => {
        if (valid) {
          this.$common.showLoad()
          let data = await updateTypeStandardGoodsCate(this.form1.id, this.form1.parentId)
          console.log(data);
          this.$common.hideLoad()
          if (data) {
            this.show1 = false
            this.getCate();
          }
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    resetForm1(formName) {
      this.$refs[formName].resetFields();
      this.show1 = false
    },
    // 添加二级分类
    async addSecondCate(data, node) {
      this.form.name = ''
      // 添加二级分类和一级分类走同一个接口，添加一级分类的时候parentId = 0,添加二级分类，parentId = 一级分类的id
      this.form.parentId = data.id
      this.title = '添加二级分类'
      this.addType = 'addSecond'
      this.addLabel = '新增二级分类名称：'
      this.show = true
    },
    // 二级分类归类
    changeCate(data, node) {
      console.log(data);
      this.show1 = true
      this.form1.parentId = ''
      this.form1.id = data.id
      this.disableId = data.parentId
    },
    // 编辑分类名称
    edit(data, node) {
      console.log(data);
      this.isEdit = true
      this.EditParentId = data.parentId
      this.editId = data.id
      this.editName = data.name
    },
    // 确定编辑 编辑药品分类name
    async save() {
      if (!this.editName) {
        return this.$common.error('分类名称不能为空')
      }
      this.$common.showLoad()
      let data = await editCateInfo(this.editId, this.editName)
      this.$common.hideLoad()
      if (data !== undefined) {
        this.getCate();
        this.isEdit = false
        this.editName = ''
        this.EditParentId = ''
        this.editId = ''
      }
    },
    // 取消编辑
    cancel(data, node) {
      this.isEdit = false
      this.editName = ''
      this.EditParentId = ''
      this.editId = ''
    },
    // 获取分类list
    async getCate() {
      let data = await getGoodsClassCate();
      console.log(data);
      if (data && data.list) {
        this.list = data.list
      }
    }
  }

}
</script>

<style lang="scss" scoped>
@import './index.scss';
.class-tree {
  //   width: 600px;
  .custom-tree-node {
    width: 100%;
    display: flex;
    justify-content: space-between;
    align-items: center;
    span {
      margin-left: 10px;
    }
  }
  ::v-deep .el-tree-node__content {
    height: 50px !important;
  }
}
</style>
