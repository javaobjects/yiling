<template>
  <div class="app-container">
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <!-- 内容区 -->
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">采购关系编号</div>
              <div class="flex-box">
                <el-input v-model="query.procRelationNumber" placeholder="请输入采购关系编号" @keyup.enter.native="handleSearch" />
              </div>
            </el-col>
            <el-col :span="6">
              <div class="title">渠道商</div>
              <div class="flex-box">
                <el-select
                  v-model="query.channelPartnerEid"
                  clearable
                  filterable
                  remote
                  :remote-method="getChannelPartnerData"
                  @clear="getChannelPartnerData"
                  :loading="channelLoading"
                  placeholder="请搜索并选择渠道商"
                >
                  <el-option v-for="item in chennelOption" :key="item.id" :label="item.customerName" :value="item.customerEid">
                  </el-option>
                </el-select>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="title">渠道类型</div>
              <div class="flex-box">
                <el-select v-model="query.channelId" placeholder="请选择渠道类型">
                  <el-option label="全部" :value="0"></el-option>
                  <el-option
                    v-for="item in popProcChannelType"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  ></el-option>
                </el-select>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="title">配送类型</div>
              <div class="flex-box">
                <el-select v-model="query.deliveryType" placeholder="请选择配送类型">
                  <el-option label="全部" :value="0"></el-option>
                  <el-option
                    v-for="item in popProcRelaDeliType"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  ></el-option>
                </el-select>
              </div>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">工业主体</div>
              <div class="flex-box">
                <el-select v-model="query.factoryEid" placeholder="请选择工业主体">
                  <el-option
                    v-for="item in mainPartOption"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                  ></el-option>
                </el-select>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="title">配送商</div>
              <div class="flex-box">
                <el-select
                  v-model="query.deliveryEid"
                  clearable
                  filterable
                  remote
                  :remote-method="getDeliveryPartnerPage"
                  @clear="getDeliveryPartnerPage"
                  :loading="deliveryLoading"
                  placeholder="请搜索并选择配送商"
                >
                  <el-option v-for="item in deliveryOption" :key="item.id" :label="item.name" :value="item.id">
                  </el-option>
                </el-select>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="title">状态</div>
              <div class="flex-box">
                <el-select v-model="query.procRelationStatus" placeholder="请选择状态">
                  <el-option label="全部" :value="0"></el-option>
                  <el-option
                    v-for="item in popProcRelaStatus"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  ></el-option>
                </el-select>
              </div>
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
      <!-- 导出按钮 -->
      <div class="down-box">
        <div class="left-box">
          <ylButton v-role-btn="['1']" type="primary" @click="handleAddNew">新增</ylButton>
        </div>
        <div class="right-box">
          <ylButton type="primary" plain v-role-btn="['7']" @click="importClick">导入</ylButton>
          <ylButton v-role-btn="['2']" type="primary" plain @click="downLoadTemp">导出</ylButton>
        </div>
      </div>
      <!-- 底部列表 -->
      <div class="table-data-box mar-t-8 pad-b-10">
        <yl-table
          class="table-box company-fixed-table"
          border
          stripe
          show-header
          :header-filter="true"
          :list="dataList"
          :total="query.total"
          :page.sync="query.current"
          :limit.sync="query.size"
          :loading="loading"
          :selection-change="handleSelectionChange"
          @getList="getProcRelationPageData"
        >
          <el-table-column type="selection" width="55"> </el-table-column>
          <el-table-column label="采购关系编号" min-width="120" align="center" prop="procRelationNumber"></el-table-column>
          <el-table-column label="渠道商" min-width="220" align="center" prop="channelPartnerName"></el-table-column>
          <el-table-column label="渠道类型" min-width="90" align="center" prop="channelPartnerChannelId">
            <template slot-scope="{ row }">
              <div>{{ row.channelPartnerChannelId | dictLabel(popProcChannelType) }}</div>
            </template>
          </el-table-column>
          <el-table-column label="配送类型" min-width="90" align="center" prop="deliveryType">
            <template slot-scope="{ row }">
              <div>{{ row.deliveryType | dictLabel(popProcRelaDeliType) }}</div>
            </template>
          </el-table-column>
          <el-table-column label="工业主体" min-width="220" align="center" prop="factoryName"></el-table-column>
          <el-table-column label="配送商" min-width="220" align="center" prop="deliveryName"></el-table-column>
          <el-table-column label="履约开始时间" min-width="100" align="center">
            <template slot-scope="{ row }">
              <div>{{ row.startTime | formatDate('yyyy-MM-dd') }}</div>
            </template>
          </el-table-column>
          <el-table-column label="履约结束时间" min-width="100" align="center">
            <template slot-scope="{ row }">
              <div>{{ row.endTime | formatDate('yyyy-MM-dd') }}</div>
            </template>
          </el-table-column>
          <el-table-column label="状态" min-width="100" align="center" prop="procRelationStatus">
            <template slot-scope="{ row }">
              <div>{{ row.procRelationStatus | dictLabel(popProcRelaStatus) }}</div>
            </template>
          </el-table-column>
          <el-table-column label="创建人" min-width="80" align="center" prop="createUserStr"></el-table-column>
          <el-table-column label="创建时间" min-width="160" align="center">
            <template slot-scope="{ row }">
              <div>{{ row.createTime | formatDate('yyyy-MM-dd hh:mm:ss') }}</div>
            </template>
          </el-table-column>
          <el-table-column label="修改人" min-width="80" align="center" prop="updateUserStr"></el-table-column>
          <el-table-column label="修改时间" min-width="160" align="center">
            <template slot-scope="{ row }">
              <div>{{ row.updateTime | formatDate('yyyy-MM-dd hh:mm:ss') }}</div>
            </template>
          </el-table-column>
          <el-table-column label="操作" min-width="140" align="center" fixed="right">
            <template slot-scope="{ row }">
              <div>
                <yl-button v-if="row.procRelationStatus === 1 || row.procRelationStatus === 2" v-role-btn="['3']" type="text" @click="handleEdit(row)">编辑</yl-button>
                <yl-button v-if="row.procRelationStatus === 2" v-role-btn="['4']" type="text" @click="handleClose(row)">停用</yl-button>
                <yl-button v-if="row.procRelationStatus === 1" v-role-btn="['5']" type="text" @click="handleDelete(row)">删除</yl-button>
                <yl-button v-role-btn="['6']" type="text" @click="handleDetail(row)">详情</yl-button>
              </div>
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
import { createDownLoad } from '@/subject/pop/api/common'
import { popProcRelaStatus, popProcChannelType, popProcRelaDeliType } from '@/subject/pop/busi/pop/establish_purchase'
import {
  queryProcRelationPage,
  queryChannelPartnerPage,
  queryProcurementRelationMainPart,
  queryDeliveryPartnerPage,
  queryDeleteProcRelationById,
  queryCloseProcRelationById
} from '@/subject/pop/api/establish_purchase'
import { formatDate } from '@/common/utils'
import ImportSendDialog from '@/subject/pop/components/ImportSendDialog'
export default {
  name: 'PurchaseManage',
  components: {
    ImportSendDialog
  },
  computed: {
    // 渠道类型
    popProcChannelType() {
      return popProcChannelType()
    },
    // 配送类型
    popProcRelaDeliType() {
      return popProcRelaDeliType()
    },
    // 状态
    popProcRelaStatus() {
      return popProcRelaStatus()
    }
  },
  data() {
    return {
      // 头部导航
      navList: [
        {
          title: '建采管理'
        },
        {
          title: '建采管理'
        }
      ],
      loading: false,
      query: {
        current: 1,
        size: 10,
        // 采购关系编号
        procRelationNumber: '',
        // 	渠道商eid
        channelPartnerEid: '',
        // 渠道类型-用于查询渠道商,非用于列表
        channelId: 0,
        // 	配送类型：1-工业直配 2-三方配送
        deliveryType: 0,
        // 工业主体eid
        factoryEid: '',
        // 配送商eid
        deliveryEid: '',
        // 采购关系状态：1-未开始 2-进行中 3-已停用 4-已过期
        procRelationStatus: 0,
        total: 0
      },
      // 工业主体下拉选项
      mainPartOption: [],
      channelLoading: false,
      // 渠道商选项
      chennelOption: [],
      deliveryLoading: false,
      // 配送商选项
      deliveryOption: [],
      // 列表数据
      dataList: [],
      // 列表勾选
      multipleSelection: [],
      info: {
        excelCode: 'importPopProcRelation'
      },
      importSendVisible: false
    }
  },
  activated() {
    this.getProcurementRelationMainPart()
    this.getProcRelationPageData()
  },
  methods: {
    // 获取工业主体下拉选项数据
    async getProcurementRelationMainPart() {
      let data = await queryProcurementRelationMainPart()
      if (data) {
        this.mainPartOption = data
      }
    },
    // 获取渠道商下拉数据
    async getChannelPartnerData(query) {
      if (query) {
        this.channelLoading = true
        let data = await queryChannelPartnerPage( 1, 10, query.trim())
        this.channelLoading = false
        if (data) {
          this.chennelOption = data.records
        }
      } else {
        this.chennelOption = []
      }
    },
    // 获取配送商下拉数据
    async getDeliveryPartnerPage(query) {
      if (query) {
        this.deliveryLoading = true
        let data = await queryDeliveryPartnerPage( 1, 10, query.trim())
        this.deliveryLoading = false
        if (data) {
          this.deliveryOption = data.records
        }
      } else {
        this.deliveryOption = []
      }
    },
    // 获取建采管理列表数据
    async getProcRelationPageData() {
      this.loading = true
      let query = this.query
      let data = await queryProcRelationPage(
        query.current,
        query.size,
        query.procRelationNumber,
        query.channelPartnerEid,
        query.channelId,
        query.deliveryType,
        query.factoryEid,
        query.deliveryEid,
        query.procRelationStatus
      )
      this.loading = false
      if (data) {
        this.dataList = data.records
        this.query.total = data.total
      }
    },
    handleSearch() {
      this.query.current = 1
      this.getProcRelationPageData()
    },
    handleReset() {
      this.query = {
        current: 1,
        size: 10,
        procRelationNumber: '',
        channelPartnerEid: '',
        channelId: 0,
        deliveryType: 0,
        factoryEid: '',
        deliveryEid: '',
        procRelationStatus: 0
      }
    },
    // 列表勾选
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    // 新建
    handleAddNew() {
      this.$router.push('/establish_purchase/purchase_manage_add')
    },
    // 编辑
    handleEdit(row) {
      this.$router.push(`/establish_purchase/purchase_manage_edit/${row.id}`)
    },
    // 删除
    handleDelete(row) {
      this.$confirm('确定删除吗？', '温馨提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(async () => {
          // 确认删除
          this.$common.showLoad()
          let data = await queryDeleteProcRelationById(row.id)
          this.$common.hideLoad()
          if (data !== undefined) {
            this.$common.success('操作成功')
            this.getProcRelationPageData()
          }
        })
        .catch(async () => {
          // 取消
        })
    },
    // 停用
    handleClose(row) {
      this.$confirm('停用后不能再启用，确定停用吗？', '温馨提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(async () => {
          // 确认删除
          this.$common.showLoad()
          let data = await queryCloseProcRelationById(row.id)
          this.$common.hideLoad()
          if (data !== undefined) {
            this.$common.success('操作成功')
            this.getProcRelationPageData()
          }
        })
        .catch(async () => {
          // 取消
        })
    },
    // 详情
    handleDetail(row) {
      this.$router.push(`/establish_purchase/purchase_manage_detail/${row.id}`)
    },
    // 导出全部
    async downLoadTemp() {
      const dateTimeStr = formatDate(new Date(), 'yyyy-MM-dd hh:mm:ss').replace(/[^0-9]/g, '')
      let query = this.query
      this.$common.showLoad()
      let data = await createDownLoad({
        className: 'popProcRelationExportService',
        fileName: `建采管理${dateTimeStr}`,
        groupName: '建采管理',
        menuName: '建采管理',
        searchConditionList: [
          {
            desc: '采购关系编号',
            name: 'procRelationNumber',
            value: query.procRelationNumber
          },
          {
            desc: '渠道商',
            name: 'channelPartnerEid',
            value: query.channelPartnerEid
          },
          {
            desc: '渠道类型',
            name: 'channelId',
            value: query.channelId
          },
          {
            desc: '配送类型',
            name: 'deliveryType',
            value: query.deliveryType
          },
          {
            desc: '工业主体',
            name: 'factoryEid',
            value: query.factoryEid
          },
          {
            desc: '配送商',
            name: 'deliveryEid',
            value: query.deliveryEid
          },
          {
            desc: '采购关系状态',
            name: 'procRelationStatus',
            value: query.procRelationStatus
          }
        ]
      })
      this.$common.hideLoad()
      if (typeof data !== 'undefined') {
        this.$common.n_success('创建下载任务成功，请在下载中心查看')
      }
    },
    //导入
    importClick() {
      this.importSendVisible = true
      this.$nextTick( () => {
        this.$refs.importSendRef.init()
      })
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
