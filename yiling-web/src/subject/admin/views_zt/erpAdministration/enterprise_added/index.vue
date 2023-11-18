<template>
  <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <div class="container">
        <div class="c-box">
          <el-form 
            :model="form" 
            :rules="rules" 
            ref="dataForm" 
            label-width="120px" 
            class="demo-ruleForm">
            <el-form-item label="企业名称" prop="title" class="demo-ruleForm-form">
              <el-input v-model.trim="form.title" show-word-limit></el-input>
              <div class="drop-down" v-if="downShow">
                <ul>
                  <li v-for="(item, index) in optionsData" :key="index" @click="optionsClick(item)">
                    {{ item.clientName }}
                  </li>
                </ul>
              </div>
              <span class="choice" @click="popupClick">选择父类企业</span>
            </el-form-item>
            <el-form-item label="父类企业">
              <el-input v-model.trim="form.ftitle" :disabled="true"></el-input>
            </el-form-item>
            <el-form-item label="分公司编码">
              <el-input v-model.trim="form.suDeptNo"></el-input>
            </el-form-item>
            <el-form-item label="crm企业" class="select-width">
              <el-select
                filterable
                clearable
                remote
                :remote-method="querySearchAsync"
                v-model="form.crmEnterpriseId"
                @clear="querySearchAsync"
                placeholder="请选择crm企业"
                :no-data-text="noDataText">
                <el-option
                  v-for="item in resultsData"
                  :key="item.crmEnterpriseId"
                  :label="item.crmEnterpriseName + '（id:' + item.crmEnterpriseId + '）' "
                  :value="item.crmEnterpriseId"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="工具对接方式" prop="flowMode" class="select-width">
              <el-select v-model="form.flowMode" placeholder="请选择">
                <el-option
                  v-for="item in erpFlowMode"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="对接级别" prop="depth" class="select-width">
              <el-select v-model="form.depth" placeholder="请选择">
                <el-option label="未对接" value="0"></el-option>
                <el-option label="基础对接" value="1"></el-option>
                <el-option label="订单提取" value="2"></el-option>
                <el-option label="发货单对接" value="3"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="流向级别" prop="flowLevel" class="select-width">
              <el-select v-model="form.flowLevel" placeholder="请选择">
                <el-option label="未对接" value="0"></el-option>
                <el-option label="以岭流向" value="1"></el-option>
                <el-option label="全品流向" value="2"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="负责人工号" prop="installEmpId">
              <el-input v-model="form.installEmpId" placeholder="请输入" @blur="installBlur"></el-input>
            </el-form-item>
            <el-form-item label="负责人">
              <el-input v-model="form.installEmployee" :disabled="true" show-word-limit placeholder="" maxlength="10"></el-input>
            </el-form-item>
            <!-- <el-form-item label="远程命令">
              <el-select v-model="form.command" placeholder="请选择">
                <el-option
                  v-for="item in erpClient"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item> -->
            <el-form-item label="月流向采集时间" class="select-width" prop="flowMonthCollectTime">
              <el-time-picker
                v-model="form.flowMonthCollectTime"
                format="HH:mm:ss"
                value-format="HH:mm:ss"
                placeholder="请选择">
              </el-time-picker>
            </el-form-item>
            <el-form-item label="终端激活状态" class="select-width">
              <el-select v-model="form.clientStatus" placeholder="请选择">
                <el-option
                  v-for="item in clientStatus"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="同步状态" prop="syncStatus">
              <el-radio-group v-model="form.syncStatus">
                <el-radio :label="0">未开启</el-radio>
                <el-radio :label="1">已开启</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="ERP品牌">
              <el-input v-model="form.erpBrand" show-word-limit placeholder="请输入" maxlength="20"></el-input>
            </el-form-item>
            <el-form-item label="商务人员">
              <el-input v-model="form.businessEmployee" show-word-limit placeholder="请输入" maxlength="10"></el-input>
            </el-form-item>
            <el-form-item label="备注">
              <el-input type="textarea" style="width:360px" show-word-limit :rows="4" placeholder="请输入备注" maxlength="100" v-model="form.remark">
              </el-input>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </div>
    <div class="bottom-view flex-row-center">
      <yl-button type="primary" plain @click="$router.go(-1)">返回</yl-button>
      <yl-button type="primary" @click="saveClick">保存</yl-button>
    </div>
    <!-- 弹窗 -->
    <yl-dialog :title="title" :show-footer="false" width="700px" :visible.sync="showDialog">
      <div class="dialogTc">
          <div class="common-box">
            <div class="search-box">
              <el-row class="box">
                <el-col :span="12">
                  <div class="title">企业名称</div>
                  <el-input v-model.trim="query.name" placeholder="请输入企业名称" />
                </el-col>
                <el-col :span="12">
                  <div class="title">企业ID</div>
                  <el-input v-model.trim="query.rkSuId" placeholder="请输入企业ID" />
                </el-col>
              </el-row>
            </div>
            <div class="search-box mar-t-16">
              <el-row class="box">
                <el-col :span="16">
                  <yl-search-btn :total="query.total" @search="handleSearch" @reset="handleReset"/>
                </el-col>
              </el-row>
            </div>
          </div>
          <!-- 下部 表格 -->
          <div class="search-bar">
            <yl-table 
              border 
              :show-header="true" 
              :list="dataList" 
              :total="query.total" 
              :page.sync="query.page" 
              :limit.sync="query.limit" 
              :loading="loading" 
              @getList="getData">
              <el-table-column align="center" min-width="80" label="企业ID" prop="rkSuId">
              </el-table-column>
              <el-table-column align="center" min-width="200" label="企业名称" prop="clientName">
              </el-table-column>
              <el-table-column fixed="right" align="center" label="操作" min-width="80">
                <template slot-scope="{ row }">
                  <div>
                    <yl-button type="text" @click="addClick(row)" >
                      <span :style="{color: row.zt == 1 ? '#1790ff' : '#c8c9cc'}">
                        {{ row.zt == 1 ? '添加' : '已添加' }}
                      </span>
                    </yl-button>
                  </div>
                </template>
              </el-table-column>
            </yl-table>
          </div>
      </div>
    </yl-dialog>
  </div>
</template>
<script>
import { 
  queryParentListPage, 
  queryEnterpriseList, 
  erpSave,
  queryCrmEnterpriseList,
  getByEmpIdOrJobId
} from '@/subject/admin/api/zt_api/erpAdministration'
import { erpClientCommand } from '@/subject/admin/utils/busi'
import { erpClientFlowMode, erpClientStatus } from '@/subject/admin/busi/erp/flowDirection'
export default {
  name: 'EnterpriseAdded',
  components: {},
  computed: {
    erpClient() {
      return erpClientCommand()
    },
    erpFlowMode() {
      return erpClientFlowMode()
    },
    //终端激活状态
    clientStatus() {
      return erpClientStatus()
    }
  },
  data() {
    return {
      form: {
        title: '', 
        erpFlowMode: '',
        flowMode: '',
        //分公司编码
        suDeptNo: '',
        //crm企业
        crmEnterpriseId: '', 
        depth: '',
        flowLevel: '',
        installEmployee: '',
        // command: '',
        syncStatus: 0,
        erpBrand: '',
        businessEmployee: '',
        remark: '',
        //父级 名称
        ftitle: '', 
        //父级企业id
        suId: '', 
        //企业ID
        rkSuId: '',
        clientStatus: 1,
        //负责人工号
        installEmpId: '',
        //月流向采集时间
        flowMonthCollectTime: ''
      },
      rules: {
        title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
        suDeptNo: [{ required: true, message: '请输入分公司编码', trigger: 'blur' }],
        suDeptNo2: [{ required: false, message: '请输入分公司编码', trigger: 'blur' }],
        depth: [{ required: true, message: '请选择对接级别', trigger: 'change' }],
        flowLevel: [{ required: true, message: '请选择流向级别', trigger: 'change' }],
        flowMode: [{ required: true, message: '请选择工具对接方式', trigger: 'change' }],
        crmEnterpriseId: [{ required: true, message: '请选择crm企业', trigger: 'change' }],
        installEmpId: [{ required: true, message: '请输入负责人工号', trigger: 'blur' }],
        flowMonthCollectTime: [{ required: true, message: '请选择月流向采集时间', trigger: 'change' }]
      },
      title: '添加企业',
      showDialog: false,
      dataList: [],
      query: {
        name: '',
        rkSuId: '',
        total: 0,
        page: 1,
        limit: 10
      },
      loading: false,
      // 获取企业名称
      optionsData: [], 
      //企业下拉 弹窗
      downShow: false, 
      optionS: '',
      //crm企业数据
      resultsData: [],
      noDataText: '无数据'
    }
  },
  watch: {
    'form.title': {
      handler(newName, oldName) {
        if (newName != this.optionS){
          if (newName != '') {
            //获取企业名称
            this.qyDataApi(); 
            this.downShow = true;
          } else {
            this.downShow = false;
          }
        }
      },
      deep: true
    }
  },
  mounted() {
    this.getData();
  },
  methods: {
    async getData() {
      this.loading = true;
      let query = this.query;
      this.dataList = [];
      let data = await queryParentListPage(
        query.name,
        query.page,
        query.rkSuId,
        query.limit
      )
      if (data !== undefined) {
        for (let i = 0; i < data.records.length; i++) {
          this.dataList.push({
            clientName: data.records[i].clientName,
            rkSuId: data.records[i].rkSuId,
            zt: 1
          })
        }
        this.query.total = data.total;
      }
      this.loading = false;
    },
    // 点击下拉框的 内容
    optionsClick(item) {
      this.form.title = this.optionS = item.clientName;
      this.form.rkSuId = item.suId;
      this.downShow = false;
    },
    // 获取企业名称
    async qyDataApi() {
      let data = await queryEnterpriseList(this.form.title)
      if (data !== undefined) {
        this.optionsData = data;
      }
    },
    // 点击 添加
    addClick(row) {
      for (let i = 0; i < this.dataList.length; i++) {
        if (row.rkSuId == this.dataList[i].rkSuId) {
          this.dataList[i].zt = 2;
        } else {
          this.dataList[i].zt = 1;
        }
      }
      this.form.ftitle = row.clientName;
      this.form.suId = row.rkSuId;
    },
    // 点击 选择企业
    popupClick() {
      this.showDialog = true;
    },
    saveClick() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          let form = this.form;
          this.$common.showLoad();
          let data = await erpSave(
            form.businessEmployee,
            // form.command,
            form.depth,
            form.erpBrand,
            form.flowLevel,
            form.installEmployee,
            form.remark,
            form.rkSuId,
            form.suDeptNo,
            form.suId,
            form.syncStatus,
            form.flowMode,
            form.clientStatus,
            form.crmEnterpriseId,
            form.installEmpId,
            form.flowMonthCollectTime
          )
          this.$common.hideLoad();
          if (data !== undefined) {
            this.$common.alert('保存成功', r => {
              this.$router.go(-1)
            })
          }
        } else {
          return false
        }
      })
    },
    handleSearch() {
      this.query.page = 1;
      this.getData()
    },
    // 清空查询
    handleReset() {
      this.query = {
        name: '',
        rkSuId: '',
        total: 0,
        page: 1,
        limit: 10
      }
    },
    async querySearchAsync(value) {
      this.noDataText = '加载中'
      this.resultsData = [];
      if (value != '' && value) {
        this.$common.showLoad();
        let data = await queryCrmEnterpriseList(
          value
        )
        this.$common.hideLoad();
        if (data !== undefined) {
          if (data && data.length > 0) {
            this.resultsData = data
          } else {
            this.resultsData = []
            this.noDataText = '无数据'
          } 
        }
      }
    },
    //输入工号
    async installBlur() {
      if (this.form.installEmpId && this.form.installEmpId != '') {
        this.$common.showLoad();
        let data = await getByEmpIdOrJobId(this.form.installEmpId)
        this.$common.hideLoad();
        if (data && data !== undefined) {
          this.form.installEmployee = data.empName
        } else {
          this.form.installEmpId = '';
          this.form.installEmployee = '';
          this.$common.warn('负责人工号输入有误！')
        }
      } 
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
