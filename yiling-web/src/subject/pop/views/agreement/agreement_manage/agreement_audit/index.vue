<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <!-- 内容区 -->
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box" style="margin-top:0;">
          <el-row class="box">
            <el-col :span="10">
              <div class="title">创建时间</div>
              <el-date-picker
                v-model="query.createTime"
                type="daterange"
                format="yyyy/MM/dd"
                value-format="yyyy-MM-dd"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期">
              </el-date-picker>
            </el-col>
            <el-col :span="7">
              <div class="title">协议编号</div>
              <el-input v-model="query.agreementNo" placeholder="请输入协议编号" @keyup.enter.native="handleSearch" />
            </el-col>
            <el-col :span="7">
              <div class="title">协议负责人</div>
              <el-input v-model="query.mainUserName" placeholder="请输入协议负责人" @keyup.enter.native="handleSearch" />
            </el-col>
          </el-row>
        </div>
        <div class="search-box">
          <el-row class="box">
            <el-col :span="10">
              <div class="title">单据类型</div>
              <el-select v-model="query.billsType" placeholder="请选择单据类型">
                <el-option label="全部" :value="0"></el-option>
                <el-option
                  v-for="item in agreementBillsType"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-col>
            <el-col :span="7">
              <div class="title">审批状态</div>
              <el-select v-model="query.authStatus" placeholder="请选择审批状态">
                <el-option label="全部" :value="0"></el-option>
                <el-option
                  v-for="item in agreementAuthStatus"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-col>
            <el-col :span="7">
              <div class="title">协议类型</div>
              <el-select v-model="query.type" placeholder="请选择支付方式">
                <el-option label="全部" :value="0"></el-option>
                <el-option
                  v-for="item in agreementType"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-col>
          </el-row>
        </div>
        <div class="search-box">
          <el-row class="box">
            <el-col :span="10">
              <div class="title">甲方</div>
              <el-input v-model="query.ename" placeholder="请输入甲方" @keyup.enter.native="handleSearch" />
            </el-col>
            <el-col :span="7">
              <div class="title">乙方</div>
              <el-input v-model="query.secondName" placeholder="请输入乙方" @keyup.enter.native="handleSearch" />
            </el-col>
          </el-row>
        </div>
        <div class="search-box">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn :total="query.total" @search="handleSearch" @reset="handleReset" />
            </el-col>
          </el-row>
        </div>
      </div>
      <!-- 底部列表 -->
      <div class="mar-t-8 pad-b-10 order-table-view" style="padding-bottom: 10px;background: #FFFFFF;border-radius: 4px;">
        <div class="header-bar mar-b-10">
          <div class="sign"></div>协议列表
        </div>
        <yl-table
          border
          show-header
          :list="dataList"
          :total="query.total"
          :page.sync="query.page"
          :limit.sync="query.limit"
          :loading="loading"
          @getList="getList"
        >
          <el-table-column align="center" width="80" label="序号" prop="id">
          </el-table-column>
          <el-table-column label="单据类型" min-width="120" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.billsType | dictLabel(agreementBillsType) }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="创建时间" min-width="200" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.createTime | formatDate }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="协议编号" min-width="200" align="center">
            <template slot-scope="{ row }">
              <div>
                <yl-button @click="showDetail(row)" type="text">{{ row.agreementNo }}</yl-button>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="甲方" min-width="200" align="center" prop="ename">
          </el-table-column>
          <el-table-column label="生效日期" min-width="200" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.startTime | formatDate('yyyy-MM-dd') }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="结束日期" min-width="200" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.endTime | formatDate('yyyy-MM-dd') }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="公司乙方" min-width="200" align="center" prop="secondName">
          </el-table-column>
          <el-table-column label="协议负责人" min-width="100" align="center" prop="mainUserName">
          </el-table-column>
          <el-table-column label="协议类型" min-width="100" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.agreementType | dictLabel(agreementType) }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="甲方类型" min-width="200" align="center" prop="name">
            <template slot-scope="{ row }">
              <div>
                {{ row.firstType | dictLabel(agreementFirstType) }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="审批状态" min-width="100" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.authStatus | dictLabel(agreementAuthStatus) }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="归档编号" min-width="200" align="center" prop="archiveNo">
          </el-table-column>
          <el-table-column label="归档备注" min-width="200" align="center" prop="archiveRemark">
          </el-table-column>
          <el-table-column label="驳回原因" min-width="200" align="center" prop="authRejectReason">
            <template slot-scope="{ row }">
              <el-tooltip
                class="item"
                effect="dark"
                :content="row.authRejectReason"
                popper-class="longLogTooltip"
                placement="top"
              >
                <span v-if="row.authRejectReason.length > 50">{{ row.authRejectReason.slice(0, 50) + "..." }}</span>
                <span v-else>{{ row.authRejectReason || "" }}</span>
              </el-tooltip>
            </template>
          </el-table-column>
          <el-table-column label="操作" min-width="180" align="center" fixed="right">
            <template slot-scope="{ row }">
              <div class="operation-view">
                <yl-button v-role-btn="[1]" type="text" @click="showDetail(row)">查看</yl-button>
                <yl-button v-role-btn="[2]" type="text" v-if="row.authStatus == 1 || row.authStatus == 3" @click="auditClick(row)">审核</yl-button>
                <yl-button v-role-btn="[3]" type="text" v-if="row.archiveFlag" @click="archiveClick(row)">归档</yl-button>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
    <!-- 归档 -->
    <yl-dialog title="归档备注" width="600px" @confirm="archiveConfirm" :visible.sync="showDialog">
      <div class="dialog-content">
        <el-form ref="dataForm" :rules="rules" :model="form" label-width="100px" label-position="right">
          <el-form-item label="归档编号" prop="archiveNo">
            <el-input v-model="form.archiveNo" :maxlength="10" placeholder="10位字符以内（数字和字母）" />
          </el-form-item>
          <el-form-item label="归档备注" prop="archiveRemark">
            <el-input v-model="form.archiveRemark" type="textarea" placeholder="不超过200个字" />
          </el-form-item>
        </el-form>
      </div>
    </yl-dialog>
  </div>
</template>

<script>
import {
  queryAgreementAuthPage,
  updateArchiveAgreement
} from '@/subject/pop/api/agreement';
import { agreementType, agreementBillsType, agreementFirstType, agreementAuthStatus } from '@/subject/pop/utils/busi'

export default {
  name: 'AgreementAudit',
  components: {
  },
  computed: {
    // 协议类型
    agreementType() {
      return agreementType()
    },
    // 单据类型
    agreementBillsType() {
      return agreementBillsType()
    },
    // 甲方类型
    agreementFirstType() {
      return agreementFirstType()
    },
    // 协议审核状态
    agreementAuthStatus() {
      return agreementAuthStatus()
    }
  },
  data() {
    return {
      // 头部导航
      navList: [
        {
          title: '首页',
          path: '/dashboard'
        },
        {
          title: '协议管理'
        },
        {
          title: '协议审核'
        }
      ],
      loading: false,
      query: {
        page: 1,
        limit: 10,
        total: 0,
        createTime: [],
        agreementNo: '',
        mainUserName: '',
        billsType: 0,
        authStatus: 0,
        type: 0,
        ename: '',
        secondName: ''
      },
      dataList: [],
      // 当前选中的 
      currentRow: {},
      // 归档
      showDialog: false,
      form: {
        archiveNo: '',
        archiveRemark: ''
      },
      rules: {
        archiveNo: [{ required: true, message: '请输入归档编号', trigger: 'blur' }],
        archiveRemark: [{ required: true, message: '请输入归档备注', trigger: 'blur' }]
      }
    };
  },
  activated() {
    this.getList()
  },
  methods: {
    async getList() {
      this.loading = true
      let query = this.query
      let data = await queryAgreementAuthPage(
        query.page,
        query.limit,
        query.createTime && query.createTime.length ? query.createTime[0] : undefined,
        query.createTime && query.createTime.length > 1 ? query.createTime[1] : undefined,
        query.agreementNo,
        query.mainUserName,
        query.billsType,
        query.authStatus,
        query.type,
        query.ename,
        query.secondName
      );
      this.loading = false
      if (data) {
        this.dataList = data.records
        this.query.total = data.total
      }
    },
    // 搜索点击
    handleSearch() {
      this.query.page = 1
      this.getList()
    },
    handleReset() {
      this.query = {
        page: 1,
        limit: 10,
        createTime: [],
        agreementNo: '',
        mainUserName: '',
        billsType: 0,
        authStatus: 0,
        type: 0,
        ename: '',
        secondName: ''
      }
    },
    // 查看
    showDetail(row) {
      // 跳转详情 operateType 1-详情 2-审核详情
      this.$router.push({
        name: 'AgreementDetail',
        params: { 
          id: row.id,
          operateType: 1
        }
      });
    },
    auditClick(row) {
      // 跳转详情 operateType 1-详情 2-审核详情
      this.$router.push({
        name: 'AgreementDetail',
        params: { 
          id: row.id,
          operateType: 2
        }
      });
    },
    // 归档
    archiveClick(row) {
      this.showDialog = true
      this.currentRow = row
    },
    // 归档确认
    archiveConfirm() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          this.$common.showLoad()
          let form = this.form
          let data = await updateArchiveAgreement(
            this.currentRow.id,
            form.archiveNo,
            form.archiveNo
          );
          this.$common.hideLoad()
          if (typeof data !== 'undefined') {
            this.showDialog = false
            this.$common.n_success('归档成功')
            this.getList()

          }
        } else {
          return false;
        }
      })
    },
    getCellClass(row) {
      if (row.columnIndex == 4) {
        return 'border-1px-l'
      } 
      return ''
    }
  }
};
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
<style lang="scss">
  .longLogTooltip {
    max-width: 20%;
  }
  .order-table-view {
    .table-row {
      margin: 0 30px;
      td {
        .el-table__expand-icon{
          visibility: hidden;
        }
      }
    }
  }
</style>
