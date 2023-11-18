<template>
  <div class="app-container">
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <!-- 内容区 -->
    <div class="app-container-content has-bottom-bar">
      <!-- 基本信息 -->
      <div class="common-box">
        <div class="title-box">
          <div class="title-wrap">
            <div class="sign"></div>
            <div class="title">基本信息</div>
          </div>
        </div>
        <div class="basic-info-box">
          <el-row>
            <el-col :span="6">
              <span class="info-title">采购关系编号:</span>
              <span class="info-value">{{ basicInfoData.procRelationNumber }}</span>
            </el-col>
            <el-col :span="6">
              <span class="info-title">渠道商:</span>
              <span class="info-value">{{ basicInfoData.channelPartnerName }}</span>
            </el-col>
            <el-col :span="6">
              <span class="info-title">渠道商类型:</span>
              <span class="info-value">{{
                basicInfoData.channelPartnerChannelId | dictLabel(popProcChannelType)
              }}</span>
            </el-col>
            <el-col :span="6">
              <span class="info-title">配送类型:</span>
              <span class="info-value">{{ basicInfoData.deliveryType | dictLabel(popProcRelaDeliType) }}</span>
            </el-col>
          </el-row>
          <el-row class="mar-t-8">
            <el-col :span="6">
              <span class="info-title">工业主体:</span>
              <span class="info-value">{{ basicInfoData.factoryName }}</span>
            </el-col>
            <el-col :span="6">
              <span class="info-title">配送商:</span>
              <span class="info-value">{{ basicInfoData.deliveryName }}</span>
            </el-col>
            <el-col :span="6">
              <span class="info-title">履约时间:</span>
              <span class="info-value"
                >{{ basicInfoData.startTime | formatDate('yyyy-MM-dd') }} - {{ basicInfoData.endTime | formatDate('yyyy-MM-dd') }}</span
              >
            </el-col>
          </el-row>
          <el-row class="mar-t-8">
            <el-col>
              <yl-button type="text" v-if="showRecord" @click="handleRecord">修改记录</yl-button>
            </el-col>
          </el-row>
        </div>
      </div>
      <!-- 可采商品 -->
      <div class="common-box mar-t-16">
        <div class="title-box">
          <div class="title-wrap">
            <div class="sign"></div>
            <div class="title">可采商品</div>
          </div>
        </div>
        <div class="form-info-box table-box">
          <yl-table class="table-data-box" ref="table" border :list="basicInfoData.goodsList" show-header>
            <el-table-column label="专利类型" min-width="140" align="center" prop="isPatent">
              <template slot-scope="{ row }">
                <div>{{ row.isPatent | dictLabel(isPatentOption) }}</div>
              </template>
            </el-table-column>
            <el-table-column label="商品名" min-width="80" align="center" prop="goodsName"></el-table-column>
            <el-table-column label="商品规格" min-width="80" align="center" prop="sellSpecifications"></el-table-column>
            <el-table-column label="批准文号" min-width="120" align="center" prop="licenseNo"></el-table-column>
            <el-table-column label="商品优惠折扣%" min-width="100" align="center" prop="rebate"></el-table-column>
          </yl-table>
        </div>
      </div>
      <!-- 底部按钮 -->
      <div class="bottom-bar-view flex-row-center">
        <yl-button class="custom-btn" type="primary" plain @click="$router.go(-1)">返回</yl-button>
      </div>
      <!-- 设置参保价格 -->
      <yl-dialog :visible.sync="snapshotDialogShow" width="580px" title="修改记录" type="add" :show-footer="false">
        <div class="dialog-content">
          <yl-table
            border
            :show-header="true"
            :list="dataList"
            :total="total"
            :page.sync="query.current"
            :limit.sync="query.size"
            :loading="loading"
            :stripe="true"
            @getList="getProcRelationSnapshotList()"
          >
            <el-table-column align="center" label="修改时间" prop="updateTime">
              <template slot-scope="{ row }">
                <span> {{ row.updateTime | formatDate }} </span>
              </template>
            </el-table-column>
            <el-table-column align="center" label="修改前版本" prop="version">
              <template slot-scope="{ row }">
                <div>
                  <yl-button type="text" @click="handleRecordDetail(row)">{{ row.version }}</yl-button>
                </div>
              </template>
            </el-table-column>
          </yl-table>
        </div>
      </yl-dialog>
    </div>
  </div>
</template>

<script>
import { queryProcRelationDetail, queryProcRelationSnapshotList } from '@/subject/pop/api/establish_purchase'
import { popProcChannelType, popProcRelaDeliType } from '@/subject/pop/busi/pop/establish_purchase'

export default {
  name: 'PurchaseManageDetail',
  components: {},
  computed: {
    // 渠道类型
    popProcChannelType() {
      return popProcChannelType()
    },
    // 配送类型
    popProcRelaDeliType() {
      return popProcRelaDeliType()
    }
  },
  mounted() {
    this.relationId = this.$route.params.id
    if (this.relationId) {
      this.getBasicDetailData()
      this.getProcRelationSnapshotList()
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
          title: '建采管理',
          path: '/establish_purchase/purchase_manage'
        },
        {
          title: '详情'
        }
      ],
      relationId: '',
      isPatentOption: [
        { label: '非专利', value: 1 },
        { label: '专利', value: 2 }
      ],
      // 建采关系详情数据
      basicInfoData: {
        goodsList: []
      },
      loading: false,
      snapshotDialogShow: false,
      query: {
        current: 1,
        size: 10
      },
      total: 0,
      dataList: [],
      showRecord: false
    }
  },
  methods: {
    // 获取基本信息详情
    async getBasicDetailData() {
      this.$common.showLoad()
      let data = await queryProcRelationDetail(this.relationId)
      this.$common.hideLoad()
      if (data) {
        this.basicInfoData = data
      }
    },
    // 查询快照列表
    handleRecord() {
      this.getProcRelationSnapshotList()
      this.snapshotDialogShow = true
    },
    // 查询快照列表
    async getProcRelationSnapshotList() {
      let query = this.query
      let data = await queryProcRelationSnapshotList(query.current, query.size, this.relationId)
      if (data) {
        this.dataList = data
        this.showRecord = this.dataList.length > 0
      }
    },
    // 查询快照详情
    async handleRecordDetail(row) {
      this.$router.push(`/establish_purchase/purchase_snapshot_detail/${row.versionId}`)
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
