<template>
  <div class="app-container">
    <div class="app-container-content">
      <div class="common-box box-search">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">企业名称</div>
              <el-input v-model="query.name" @keyup.enter.native="searchEnter" placeholder="请输入企业名称" />
            </el-col>
            <el-col :span="6">
              <div class="title">社会统一信用代码/医疗机构执业许可证</div>
              <el-input v-model="query.licenseNumber" @keyup.enter.native="searchEnter" placeholder="请输入社会统一信用代码/医疗机构执业许可证" />
            </el-col>
            <el-col :span="6">
              <div class="title">状态</div>
              <el-select v-model="query.status" placeholder="请选择状态">
                <el-option label="全部" :value="0"></el-option>
                <el-option
                  v-for="item in statusOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-col>
          </el-row>
        </div>
        <div class="search-btn-box">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn :total="total" @search="handleSearch" @reset="handleReset" />
            </el-col>
          </el-row>
        </div>
      </div>
      <!-- 导出按钮 -->
      <div class="down-box clearfix">
        <div class="btn">
          <ylButton type="primary" plain @click="multiUpdate(1)" :disabled="multipleSelection.length > 0 ? false : true">批量启动</ylButton>
          <ylButton type="primary" plain @click="multiUpdate(2)" :disabled="multipleSelection.length > 0 ? false : true">批量锁定</ylButton>
          <ylButton type="primary" plain @click="importClick">导入</ylButton>
          <ylButton type="primary" plain @click="addNew">添加</ylButton>
        </div>
      </div>
      <div class="table-box mar-t-8">
        <yl-table
          border
          :show-header="true"
          :list="dataList"
          :total="total"
          :page.sync="query.current"
          :limit.sync="query.size"
          :loading="loading"
          :selection-change="handleSelectionChange"
          @getList="getList"
        >
          <el-table-column type="selection" width="55"> </el-table-column>
          <el-table-column label="企业名称" align="center" prop="name" min-width="150"> </el-table-column>
          <el-table-column label="企业类型" align="center" prop="type">
            <template slot-scope="{ row }">
              <div> {{ row.type | dictLabel(queryEnterpriseType) }} </div>
            </template>
          </el-table-column>
          <el-table-column label="社会统一信用代码/医疗机构执业许可证" align="center" prop="licenseNumber" min-width="150"> </el-table-column>
          <el-table-column label="状态" align="center" prop="status">
            <template slot-scope="{ row }">
              <span :class="[row.status === 1 ? 'col-down' : '', row.status === 2 ? 'col-up' : '', ]">{{ row.status | statusFilter }}</span>
            </template>
          </el-table-column>
          <el-table-column label="所属省份" align="center" prop="provinceName"> </el-table-column>
          <el-table-column label="所属城市" align="center" prop="cityName"> </el-table-column>
          <el-table-column label="所属区县" align="center" prop="regionName"> </el-table-column>
          <el-table-column label="详细地址" align="center" prop="address"> </el-table-column>
          <el-table-column label="所属板块" align="center" prop="plate"> </el-table-column>
          <el-table-column label="操作" align="center">
            <template slot-scope="{ row }">
              <div>
                <yl-button type="text" v-if="row.status === 2" @click="update(row)">启用</yl-button>
                <yl-button type="text" v-if="row.status === 1" @click="update(row)">禁用</yl-button>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
      <!-- 添加用户弹窗 -->
      <yl-dialog :visible.sync="dialogShow" width="640px" title="添加企业" :show-cancle="false" @confirm="confirm" @close="closeDialog">
        <div class="dialog-content">
          <el-form :model="addForm" class="addForm" ref="addFormRef" :rules="addFormRules" label-position="right" label-width="80px">
            <el-form-item label="企业名称" prop="name">
              <el-input v-model="addForm.name" placeholder="请输入企业名称" > </el-input>
            </el-form-item>
            <el-form-item label="企业类型" prop="type">
              <el-select v-model="addForm.type" placeholder="请选择企业类型">
                <el-option
                  v-for="item in queryEnterpriseType"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="许可证号" prop="licenseNumber">
              <el-input v-model="addForm.licenseNumber" placeholder="请输入社会统一信用代码/医疗机构执业许可证" > </el-input>
            </el-form-item>
            <el-form-item label="企业状态" prop="status">
              <el-select v-model="addForm.status" placeholder="请选择企业状态">
                <el-option
                  v-for="item in statusOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="所属地区" prop="region">
              <div class="select-address">
                <yl-choose-address
                  :province.sync="addForm.provinceCode"
                  :city.sync="addForm.cityCode"
                  :area.sync="addForm.regionCode"
                />
              </div>
            </el-form-item>
            <el-form-item label="详细地址" prop="address">
              <el-input v-model="addForm.address" placeholder="请输入详细地址" > </el-input>
            </el-form-item>
            <el-form-item label="所属板块" prop="plate">
              <el-input v-model="addForm.plate" placeholder="请输入所属板块" > </el-input>
            </el-form-item>
          </el-form>
        </div>
      </yl-dialog>
      <!-- 导入 -->
      <import-send-dialog :visible.sync="importSendVisible" :excel-code="info.excelCode" ref="importSendRef"></import-send-dialog>
    </div>
  </div>
</template>

<script>
import { lockCustomerList, addLockCustomer, updateStatus } from '@/subject/admin/api/views_sale/sell_manage'
import { enterpriseType } from '@/subject/admin/utils/busi'
import { ylChooseAddress } from '@/subject/admin/components'
import ImportSendDialog from '@/subject/admin/components/ImportSendDialog'

export default {
  name: 'UserControl',
  components: {
    ylChooseAddress,
    ImportSendDialog
  },
  computed: {
    // 企业类型
    queryEnterpriseType() {
      return enterpriseType()
    }
  },
  filters: {
    statusFilter(e) {
      let res
      switch (e) {
        case 0:
          res = '全部'
          break;
        case 1:
          res = '启用'
          break;
        case 2:
          res = '停用'
          break;
        default:
          res = '- -'
          break;
      }
      return res
    }
  },
  data() {
    var regionValidator = (rule, value, callback) => {
      if (this.addForm.provinceCode === '' | this.addForm.cityCode === '' | this.addForm.regionCode === '') {
        callback(new Error('请选择地址区域'))
      } else {
        callback()
      }
    }
    return {
      statusOptions: [
        { label: '启用', value: 1 },
        { label: '停用', value: 2 }
      ],
      query: {
        current: 1,
        size: 10,
        name: '',
        licenseNumber: '',
        status: 0
      },
      total: 0,
      // 列表
      dataList: [],

      loading: false,
      dialogShow: false,
      addForm: {
        name: '',
        type: '',
        licenseNumber: '',
        status: '',
        provinceCode: '',
        cityCode: '',
        regionCode: '',
        address: '',
        plate: ''
      },
      addFormRules: {
        name: [ { required: true, message: '请输入企业名称', trigger: 'blur' } ],
        type: [ { required: true, message: '请选择企业类型', trigger: 'blur' } ],
        licenseNumber: [ { required: true, message: '请输入社会统一信用代码/医疗机构执业许可证', trigger: 'blur' } ],
        status: [ { required: true, message: '请选择状态', trigger: 'blur' } ],
        region: [ { required: true, message: '请选择地址区域', trigger: 'blur', validator: regionValidator } ],
        address: [ { required: true, message: '请输入详细地址', trigger: 'blur' } ],
        plate: [ { required: true, message: '请输入所属板块', trigger: 'blur' } ]
      },
      multipleSelection: [],
      // 导入弹窗
      importSendVisible: false,
      // 导入Code
      info: {
        excelCode: 'importLockCustomer'
      }
    }
  },
  activated() {
    this.getList()
  },
  methods: {
    // Enter
    searchEnter(e) {
      const keyCode = window.event ? e.keyCode : e.which;
      if (keyCode === 13) {
        this.getList()
      }
    },
    async getList() {
      this.loading = true
      let query = this.query
      let data = await lockCustomerList(
        query.current,
        query.size,
        query.name,
        query.licenseNumber,
        query.status
      )
      this.loading = false
      if (data) {
        this.dataList = data.records
        this.total = data.total
      }
    },
    handleSearch() {
      this.query.current = 1
      this.getList()
    },
    handleReset() {
      this.query = {
        current: 1,
        size: 10,
        name: '',
        licenseNumber: '',
        status: 0
      }
    },
    // 列表勾选
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    // 操作
    update(row) {
      this.$confirm(row.status === 1 ? '该客户禁止参与本次推广活动' : '该客户启用之后将继续参与推广佣金计算', '提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning',
        customClass: 'update-cfm'
      }).then(async () => {
        let newStatus = row.status === 1 ? 2 : 1
        let data = await updateStatus([row.id], newStatus)
        if (data !== undefined) {
          this.$common.success('操作成功')
          this.getList()
        }
      })
    },
    // 批量操作
    async multiUpdate(status) {
      let idList = this.multipleSelection.map(item => item.id)
      let data = await updateStatus(idList, status)
      if (data !== undefined) {
        this.$common.success('操作成功')
        this.getList()
      }
    },
    //  导入
    goImport() {
      this.$router.push({
        path: '/importFile/importFile_index',
        query: {
          action: '/salesAssistant/api/v1/lockCustomer/importLockCustomer'
        }
      })
    },
    // 导入
    importClick() {
      this.importSendVisible = true
      this.$nextTick( () => {
        this.$refs.importSendRef.init()
      })
    },
    //  新建
    addNew() {
      this.dialogShow = true
    },
    //  添加
    confirm() {
      this.$refs.addFormRef.validate(async valid => {
        if (valid) {
          let form = this.addForm
          let data = await addLockCustomer(
            form.name,
            form.type,
            form.licenseNumber,
            form.status,
            form.provinceCode,
            form.cityCode,
            form.regionCode,
            form.address,
            form.plate
          )
          if (data !== undefined) {
            this.$common.success('添加成功')
            this.dialogShow = false
            this.$refs.addFormRef.resetFields()
            this.addForm.provinceCode = ''
            this.addForm.cityCode = ''
            this.addForm.regionCode = ''
            this.getList()
          }
        }
      })
    },
    closeDialog() {
      this.$refs.addFormRef.resetFields()
      this.addForm.provinceCode = ''
      this.addForm.cityCode = ''
      this.addForm.regionCode = ''
    }
  }
}
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
<style lang="scss">
.update-cfm {
  .el-message-box__header {
    padding-top: 12px;
    padding-bottom: 12px;
    border-bottom: 1px solid #f0f0f0;
    .el-message-box__title {
      font-size: 16px;
      font-weight: 500;
      line-height: 24px;
      text-align: center;
    }
  }
  .el-message-box__container {
    display: flex;
    justify-content: center;
    align-items: center;
    padding: 31px 0;
    .el-message-box__status {
      position: relative;
      color: #fa8c15;
      font-size: 16px !important;
      transform: translateY(0);
    }
    .el-message-box__message {
      padding-left: 3px;
    }
  }
  .el-message-box__btns {
    border-top: 1px solid #f0f0f0;
  }
}
</style>
