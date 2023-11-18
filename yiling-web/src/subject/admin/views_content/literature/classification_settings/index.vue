<template>
  <div class="app-container">
    <!-- 内容区域 -->
    <div class="app-container-content">
       <!-- 中部 搜索条件 -->
      <div class="down-box clearfix">
        <div class="btn">
          <yl-button type="primary" class="bottom-margin" @click="addClick">添加</yl-button>
        </div>
      </div>
      <!-- 底部列表 -->
      <div class="mar-t-8 pad-b-10 order-table-view">
        <yl-table
          border 
          :show-header="true" 
          :list="dataList" 
          :total="query.total" 
          :page.sync="query.page" 
          :limit.sync="query.limit" 
          :loading="loading" 
          @getList="getList">
          <el-table-column align="center" min-width="140" label="栏目名称" prop="categoryName"></el-table-column>
          <el-table-column align="center" min-width="120" label="创建时间">
            <template slot-scope="{ row }">
              <div>{{ row.createTime | formatDate }}</div>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="100" label="最新更新时间">
            <template slot-scope="{ row }">
              <div>{{ row.updateTime | formatDate }}</div>
            </template>
          </el-table-column>
         <el-table-column fixed="right" align="center" label="操作" min-width="140">
            <template slot-scope="{ row }">
              <div class="button-display">
                <yl-button type="text" @click="modifyClick(row)">编辑</yl-button>
              </div>
              <div class="button-display">
                <el-switch
                  :active-value="1" 
                  :inactive-value="0" 
                  v-model="row.status" 
                  @change="statusChange(row)">
                </el-switch>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
    <yl-dialog 
      width="700px" 
      title="栏目设置"
      :visible.sync="showDialog"
      @confirm="confirm">
      <div class="pop-up">
        <el-form
          class="demo-ruleForm"
          label-width="100px"
          :label-position="'left'"
          :model="form"
          :rules="rules" 
          ref="dataForm">
          <el-form-item label="名称" prop="categoryName">
            <el-input maxlength="6" show-word-limit placeholder="请输入名称" style="width: 90%;" v-model="form.categoryName"></el-input>
          </el-form-item>
        </el-form>
      </div>
    </yl-dialog>
  </div>
</template>
<script>
import { queryCategoryList, addCategory, updateCategory } from '@/subject/admin/api/content_api/literature'
export default {
  name: 'ClassificationSettings',
  components: {
  },
  computed: {
  },
  data() {
    return {
      query: {
        total: 0,
        page: 1,
        limit: 10
      },
      form: {
        categoryName: '',
        id: ''
      },
      dataList: [],
      showDialog: false,
      loading: false,
      rules: {
        categoryName: [{ required: true, message: '请输入名称', trigger: 'blur' }]
      }
    }
  },
  activated() {
    this.getList();
  },
  methods: {
    async getList() {
      this.loading = true;
      let data = await queryCategoryList()
      if (data) {
        this.dataList = data.list;
      }
      this.loading = false;
    },
    handleSearch() {
      this.query.page = 1;
      this.getList();
    },
    // 重置
    handleReset() {
      this.query = {
        total: 0,
        page: 1,
        limit: 10
      }
    },
    // 启用禁用
    async statusChange(val) {
      this.$common.showLoad();
      let data = await updateCategory(
        val.categoryName,
        val.id,
        val.status
      )
      this.$common.hideLoad();
      if (data !== undefined) {
        if (val.status == 1) {
          this.$common.n_success('已启用');
        } else {
          this.$common.n_warn('已禁用');
        }
        this.getList();
      }
    },
    // 添加
    addClick() {
      this.form = {
        categoryName: '',
        id: ''
      };
      this.showDialog = true;
    },
    // 编辑
    modifyClick(row) {
      this.showDialog = true;
      this.form = {
        categoryName: row.categoryName,
        id: row.id
      };
    },
    confirm() {
      this.$refs['dataForm'].validate( async(valid) => {
        if (valid) {
          let form = this.form;
          if (form.id == '') {
            this.$common.showLoad();
            let data = await addCategory(
              form.categoryName
            )
            this.$common.hideLoad();
            if (data !== undefined) {
              this.$common.n_success('添加成功!');
              this.showDialog = false;
              this.getList();
            }
          } else {
            this.$common.showLoad();
            let data2 = await updateCategory(
              form.categoryName,
              form.id,
              form.status
            )
            this.$common.hideLoad();
            if (data2 !== undefined) {
              this.$common.n_success('修改成功!');
              this.showDialog = false;
              this.getList();
            }
          }
        }
      })
    }
  }
}
</script>
<style lang="scss" scoped>
@import "./index.scss";
</style>
