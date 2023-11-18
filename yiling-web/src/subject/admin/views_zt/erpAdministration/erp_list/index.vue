<template>
 <div class="app-container">
    <!-- 内容区 -->
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">终端名称</div>
              <el-input v-model.trim="query.name" @keyup.enter.native="searchEnter" placeholder="请输入终端名称" />
            </el-col>
            <el-col :span="6">
              <div class="title">失败原因</div>
              <el-select class="select-width" v-model="query.errorCode" placeholder="请选择">
                <el-option :key="0" label="全部" :value="0"></el-option>
                <el-option v-for="itm in erpCustomer" :key="itm.value" :label="itm.label" :value="itm.value">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="12">
              <div class="title">唯一代码</div>
              <el-input v-model.trim="query.licenseNo" @keyup.enter.native="searchEnter" placeholder="请输入唯一代码" />
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">供应商名称</div>
              <el-select v-model="query.suId" filterable placeholder="请选择">
                <el-option
                  v-for="item in options"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id">
                </el-option>
              </el-select>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn :total="query.total" @search="handleSearch" @reset="handleReset" />
            </el-col>
          </el-row>
        </div>
      </div>
      <div class="down-box clearfix">
        <div class="btn">
          <yl-button v-role-btn="['1']" @click="downLoadTemp" type="text">查询结果导出</yl-button>
          <yl-button v-role-btn="['2']" class="mar-r-10" @click="importClick" type="text">导入</yl-button>
        </div>
      </div>
      <!-- 下部 表格 -->
      <div class="search-bar mar-t-10">
        <yl-table border :show-header="true" :list="dataList" :total="query.total" :page.sync="query.page" :limit.sync="query.limit" :loading="loading" @getList="getList">
          <el-table-column align="center" label="ID" width="80" prop="id">
          </el-table-column>
          <el-table-column align="center" min-width="200" label="企业名称" prop="name">
          </el-table-column>
          <el-table-column align="center" min-width="200" label="统一社会信用代码/医疗机构许可证" prop="licenseNo">
          </el-table-column>
          <el-table-column align="center" min-width="90" label="企业类型" prop="customerType">
          </el-table-column>
          <el-table-column align="center" min-width="100" label="联系人" prop="contact">
          </el-table-column>
          <el-table-column align="center" min-width="100" label="联系电话" prop="phone">
          </el-table-column>
          <el-table-column align="center" min-width="120" label="省市区">
            <template slot-scope="{ row }">
              <div>
                {{ row.province }}{{ row.city }}{{ row.region }}
              </div>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="200" label="详细地址" prop="address">
          </el-table-column>
          <el-table-column align="center" min-width="200" label="客户内码/编码" prop="articleDesc">
            <template slot-scope="{ row }">
              <div>
                内码：{{ row.innerCode }}
              </div>
              <div>
                编码：{{ row.sn }}
              </div>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="200" label="失败原因" prop="syncMsg">
          </el-table-column>
          <el-table-column align="center" min-width="200" label="供应商" prop="suName">
          </el-table-column>
          <el-table-column fixed="right" align="center" label="操作" min-width="140">
            <template slot-scope="{ row }">
              <div v-if="row.syncStatus == 3">
                <yl-button type="text" @click="matchingClick(row)">匹配</yl-button>         
              </div>
              <!-- <div v-if="row.syncStatus == 3">
                <yl-button type="text" @click="maintainClick(row)">维护</yl-button>
              </div> -->
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
    <!-- excel导入 -->
    <import-send-dialog :visible.sync="importSendVisible" :excel-code="info.excelCode" ref="importSendRef"></import-send-dialog>
  </div>
</template>

<script>
import { abnormalCustomerList, erpSupplierList } from '@/subject/admin/api/zt_api/erpAdministration'
import { erpCustomerError } from '@/subject/admin/utils/busi'
import ImportSendDialog from '@/subject/admin/components/ImportSendDialog'
import { createDownLoad } from '@/subject/admin/api/common'

export default {
  name: 'ErpList',
  components: {
    ImportSendDialog
  },
  computed: {
    erpCustomer() {
      return erpCustomerError()
    }
  },
  data() {
    return {
      query: {
        name: '',
        errorCode: 0,
        licenseNo: '',
        // suidName: '',
        suId: '',
        total: 0,
        page: 1,
        limit: 10
      },
      dataList: [],
      loading: false,
      options: [],
      // 导入
      importSendVisible: false,
      // 导入任务参数 excelCode: importAbnormalCustomer
      info: {
        excelCode: 'importAbnormalCustomer'
      }
    }
  },
  activated() {
    this.getList();
    this.gysDataList(); //获取 供应商名称
  },
  watch: {

  },
  methods: {
    // Enter
    searchEnter(e) {
      const keyCode = window.event ? e.keyCode : e.which;
      if (keyCode === 13) {
        this.getList()
      }
    },
    async gysDataList() {
      let data = await erpSupplierList('')
      if (data !== undefined) {
        this.options = data;
      }
    },
    async getList() {
      this.loading = true;
      let query = this.query;
      let data = await abnormalCustomerList(
        query.page,
        query.errorCode,
        query.licenseNo,
        query.name,
        query.limit,
        query.suId
      )
      if (data !== undefined) {
        this.dataList = data.records;
        this.query.total = data.total;
      }
      this.loading = false;
    },
    // 点击维护
    // maintainClick(row) {
    //   this.$router.push('/erpAdministration/erp_maintain/' + row.id);
    // },
    // 点击匹配
    matchingClick(row) {
      this.$router.push('/erpAdministration/erp_matching/' + row.id);
    },
    // 搜索点击
    handleSearch() {
      this.query.page = 1;
      this.getList()
    },
    // 清空查询
    handleReset() {
      this.query = {
        name: '',
        errorCode: 0,
        licenseNo: '',
        suId: '',
        total: 0,
        page: 1,
        limit: 10
      }
    },
    // excel导入
    importClick() {
      this.importSendVisible = true
      this.$nextTick( () => {
        this.$refs.importSendRef.init()
      })
    },
    // 导出
    async downLoadTemp() {
      const query = this.query
      this.$common.showLoad()
      const data = await createDownLoad({
        className: 'erpAbnormalCustomerExportService',
        fileName: '异常客户导出',
        groupName: '异常客户导出',
        menuName: 'ERP对接管理-异常客户导出',
        searchConditionList: [
          {
            desc: '终端名称',
            name: 'name',
            value: query.name || ''
          },
          {
            desc: '失败原因',
            name: 'errorCode',
            value: query.errorCode
          },
          {
            desc: '唯一代码',
            name: 'licenseNo',
            value: query.licenseNo || ''
          },
          {
            desc: '供应商名称',
            name: 'suId',
            value: query.suId || ''
          }
        ]
      })
      this.$common.hideLoad()
      if (typeof data !== 'undefined') {
        this.$common.n_success('创建下载任务成功，请在下载中心查看')
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>