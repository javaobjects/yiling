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
          <el-table-column align="center" min-width="80" label="引用业务线">
            <template slot-scope="{ row }">
              <div>{{ row.lineId | dictLabel(businessData) }}</div>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="100" label="所属板块">
            <template slot-scope="{ row }">
              <div v-if="row.lineId == 1">{{ row.moduleId | dictLabel(hmcModuleData) }}</div>
              <div v-if="row.lineId == 2">{{ row.moduleId | dictLabel(ihModuleData) }}</div>
              <div v-if="row.lineId == 6">{{ row.moduleId | dictLabel(saModuleData) }}</div>
              <div v-if="row.lineId == 7">{{ row.moduleId | dictLabel(popModuleData) }}</div>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="140" label="栏目名称" prop="categoryName"></el-table-column>
          <el-table-column align="center" min-width="90" label="排序" prop="categorySort">
          </el-table-column>
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
          <el-table-column fixed="right" align="center" label="操作" min-width="110">
            <template slot-scope="{ row }">
              <div class="button-display">
                <yl-button type="text" @click="modifyClick(row)">编辑</yl-button>
              </div>
              <div class="button-display">
                <yl-button type="text" @click="sortClick(row)">排序</yl-button>
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
          :model="form"
          :rules="rules"
          :label-position="'left'"
          label-width="100px"
          ref="dataForm"
          class="demo-ruleForm">
          <el-form-item label="引用业务线" prop="lineId">
            <el-radio-group v-model="form.lineId" @change="lineChange">
              <el-radio v-for="item in businessData" :key="item.value" :label="item.value">
                {{ item.label }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="所属板块" prop="moduleId">
            <el-radio-group v-model="form.moduleId" v-if="form.lineId == 1">
              <el-radio v-for="item in hmcModuleData" :key="item.value" :label="item.value">
                {{ item.label }}
              </el-radio>
            </el-radio-group>
            <el-radio-group v-model="form.moduleId" v-if="form.lineId == 2">
              <el-radio v-for="item in ihModuleData" :key="item.value" :label="item.value">
                {{ item.label }}
              </el-radio>
            </el-radio-group>
            <el-radio-group v-model="form.moduleId" v-if="form.lineId == 3">
              <el-radio v-for="item in ihPatientData" :key="item.value" :label="item.value">
                {{ item.label }}
              </el-radio>
            </el-radio-group>
            <el-radio-group v-model="form.moduleId" v-if="form.lineId == 6">
              <el-radio v-for="item in saModuleData" :key="item.value" :label="item.value">
                {{ item.label }}
              </el-radio>
            </el-radio-group>
            <el-radio-group v-model="form.moduleId" v-if="form.lineId == 7">
              <el-radio v-for="item in popModuleData" :key="item.value" :label="item.value">
                {{ item.label }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="栏目名称" prop="categoryName">
            <el-input 
              maxlength="8" 
              show-word-limit 
              placeholder="请输入名称" 
              style="width: 90%" 
              v-model="form.categoryName">
            </el-input>
          </el-form-item> 
        </el-form>
      </div>
    </yl-dialog>
    <yl-dialog title="排序" @confirm="sortConfirm" width="570px" :visible.sync="sortDialog">
      <div class="dialogTc">
        <el-form 
          :model="sortForm" 
          :rules="sortRules" 
          ref="dataForm1" 
          label-width="80px">
          <el-form-item label="排序" prop="categorySort">
            <el-input v-model="sortForm.categorySort" style="width: 200px;"
              @input="e => (sortForm.categorySort = checkInput(e))" placeholder="排序"
            ></el-input>
          </el-form-item>
        </el-form>
      </div>
    </yl-dialog>
  </div>
</template>
<script>
import { queryCategoryList, addContent, updateCategory, statusType, querySort } from '@/subject/admin/api/content_api/category'
import { displayLine } from '@/subject/admin/utils/busi'
import { 
  hmcModule, 
  ihModule, 
  ihPatientModule, 
  saModule, 
  popModule 
} from '@/subject/admin/busi/content/article_video'
export default {
  name: 'ClassificationSetting',
  components: {},
  computed: {
    businessData() {
      return displayLine()
    },
    //健康管理中心 板块
    hmcModuleData() {
      return hmcModule()
    },
    //医生端板块
    ihModuleData() {
      return ihModule()
    },
    //ih患者端板块
    ihPatientData() {
      return ihPatientModule()
    },
    //销售助手板块
    saModuleData() {
      return saModule()
    },
    //大运河板块
    popModuleData() {
      return popModule()
    }
  },
  data() {
    return {
      query: {
        total: 0,
        page: 1,
        limit: 10
      },
      form: {
        lineId: '',
        moduleId: '',
        categoryName: '',
        id: ''
      },
      dataList: [],
      showDialog: false,
      loading: false,
      rules: {
        lineId: [{ required: true, message: '请选择引用业务线', trigger: 'change' }],
        moduleId: [{ required: true, message: '请选择所属板块', trigger: 'change' }],
        categoryName: [{ required: true, message: '请输入名称', trigger: 'blur' }]
      },
      // 排序弹窗
      sortDialog: false,
      sortForm: {
        lineId: '',
        moduleId: '',
        categorySort: '',
        id: ''
      },
      sortRules: {
        categorySort: [{ required: true, message: '请输入排序', trigger: 'blur' }]
      }
    }
  },
  activated() {
    this.getList();
  },
  methods: {
    async getList() {
      let data = await queryCategoryList('')
      if (data) {
        this.dataList = data.list;
      }
    },
    lineChange() {
      this.form.moduleId = 1
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
      let data = await statusType(
        val.lineId,
        val.moduleId,
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
      }
    },
    // 添加
    addClick() {
      this.form = {
        lineId: '',
        moduleId: '',
        categoryName: '',
        id: ''
      }
      this.showDialog = true;
    },
    // 编辑
    modifyClick(row) {
      this.showDialog = true;
      this.form = {
        lineId: row.lineId,
        moduleId: row.moduleId,
        categoryName: row.categoryName,
        id: row.id
      }
    },
    confirm() {
      this.$refs['dataForm'].validate(async(valid) => {
        if (valid) {
          let form = this.form;
          if (form.id == '') {
            this.$common.showLoad();
            let data = await addContent(
              form.lineId,
              form.moduleId,
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
              form.lineId,
              form.moduleId,
              form.categoryName,
              form.id
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
    },
    // 点击排序
    sortClick(row) {
      this.sortDialog = true;
      this.sortForm = {
        lineId: row.lineId,
        moduleId: row.moduleId,
        categorySort: row.categorySort,
        id: row.id
      }
    },
    // 排序点击确定
    sortConfirm() {
      this.$refs['dataForm1'].validate(async (valid) => {
        if (valid) {
          let sortForm = this.sortForm;
          this.$common.showLoad();
          let data = await querySort(
            sortForm.lineId,
            sortForm.moduleId,
            sortForm.categorySort,
            sortForm.id
          )
          this.$common.hideLoad();
          if (data !== undefined) {
            this.$common.n_success('修改成功!');
            this.sortDialog = false;
            this.getList();
          }
        }
      })
    },
    checkInput(val) {
      val = val.replace(/[^0-9]/gi, '')
      if (val > 200) {
        val = 200
      }
      if (val < 1) {
        val = ''
      }
      return val
    }
  }
}
</script>
<style lang="scss" scoped>
@import './index.scss';
</style>
