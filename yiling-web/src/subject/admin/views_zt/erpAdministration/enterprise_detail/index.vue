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
          <el-form-item label="企业ID">
            {{ form.rkSuId }}
          </el-form-item>
          <el-form-item label="企业名称">
            {{ form.clientName }}
          </el-form-item>
          <el-form-item label="父类企业">
            {{ form.clientNameParent }}
          </el-form-item>
          <el-form-item label="APPKEY">
            {{ form.clientKey }}
          </el-form-item>
          <el-form-item label="密钥">
            {{ form.clientSecret }}
          </el-form-item>
          <el-form-item label="分公司编码">
            <el-input v-model="form.suDeptNo" placeholder="请输入"></el-input>
          </el-form-item>
          <el-form-item label="crm企业" class="select-width">
            <el-select
              filterable
              clearable
              remote
              :remote-method="querySearchAsync"
              v-model="form.crmData"
              @change="handleSelect"
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
              <el-option :key="0" label="未对接" :value="0"></el-option>
              <el-option :key="1" label="基础对接" :value="1"></el-option>
              <el-option :key="2" label="订单提取" :value="2"></el-option>
              <el-option :key="3" label="发货单对接" :value="3"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="流向级别" prop="flowLevel" class="select-width">
            <el-select v-model="form.flowLevel" placeholder="请选择">
              <el-option :key="0" label="未对接" :value="0"></el-option>
              <el-option :key="1" label="以岭流向" :value="1"></el-option>
              <el-option :key="2" label="全品流向" :value="2"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="负责人工号" prop="installEmpId">
            <el-input v-model="form.installEmpId" placeholder="请输入" @blur="installBlur"></el-input>
          </el-form-item>
          <el-form-item label="负责人">
            <el-input v-model="form.installEmployee" :disabled="true" placeholder="请输入"></el-input>
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
  </div>
</template>

<script>
import { 
  queryDetail, 
  queryUpdate, 
  queryCrmEnterpriseList, 
  getByEmpIdOrJobId 
} from '@/subject/admin/api/zt_api/erpAdministration'
import { erpClientCommand } from '@/subject/admin/utils/busi'
import { erpClientFlowMode, erpClientStatus } from '@/subject/admin/busi/erp/flowDirection'
export default {
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
        rkSuId: '',
        clientName: '',
        clientNameParent: '',
        clientKey: '',
        clientSecret: '',
        suDeptNo: '',
        crmData: '',
        crmEnterpriseId: '',
        flowMode: '',
        depth: '',
        flowLevel: '',
        installEmployee: '',
        // command: '',
        syncStatus: '',
        erpBrand: '',
        businessEmployee: '',
        remark: '',
        clientStatus: 1,
        id: '',
        //负责人工号
        installEmpId: '',
        //月流向采集时间
        flowMonthCollectTime: ''
      },
      rules: {
        depth: [{ required: true, message: '请选择对接级别', trigger: 'change' }],
        flowLevel: [{ required: true, message: '请选择流向级别', trigger: 'change' }],
        syncStatus: [{ required: true, message: '请选择同步状态', trigger: 'change' }],
        flowMode: [{ required: true, message: '请选择工具对接方式', trigger: 'change' }],
        crmData: [{ required: true, message: '请选择crm企业', trigger: 'change' }],
        installEmpId: [{ required: true, message: '请输入负责人工号', trigger: 'blur' }],
        flowMonthCollectTime: [{ required: true, message: '请选择月流向采集时间', trigger: 'change' }]
      },
      //crm企业数据
      resultsData: [],
      noDataText: '无数据'
    }
  },
  mounted() {
    let queryId = this.$route.params;
    if (queryId.id) {
      this.getData(queryId.id)
    }
  },
  methods: {
    handleSelect(item) {
      this.form.crmEnterpriseId = item
    },
    async getData(val) {
      let data = await queryDetail(val)
      if (data) {
        this.form = {
          ...data,
          // command: data.command == 0 ? data.command.toString() : data.command,
          clientStatus: data.clientStatus == 0 ? data.clientStatus.toString() : data.clientStatus,
          crmEnterpriseId: data.crmEnterpriseId ? data.crmEnterpriseId : '',
          crmData: data.crmEnterpriseId ? data.crmEnterpriseName + '（id:' + data.crmEnterpriseId + '）' : ''
        }
      }
    },
    saveClick() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          let form = this.form;
          this.$common.showLoad();
          let data = await queryUpdate(
              form.businessEmployee,
              // form.command,
              form.depth,
              form.erpBrand,
              form.flowLevel,
              form.id,
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
          this.$common.hideLoad()
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
    }
  }
}
</script>

<style lang="scss" scoped>
  @import './index.scss';
</style>
