<template>
  <div class="app-container">
    <!-- 内容区 -->
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">发券活动名称</div>
              <el-input v-model="query.autoGiveName" placeholder="请输入发券活动名称" @keyup.enter.native="handleSearch" />
            </el-col>
            <el-col :span="6">
              <div class="title">发券活动ID</div>
              <el-input v-model="query.autoGiveId" placeholder="请输入发券活动ID" @keyup.enter.native="handleSearch" />
            </el-col>
            <el-col :span="12">
              <div class="title">创建时间</div>
              <el-date-picker
                v-model="query.time"
                type="daterange"
                format="yyyy/MM/dd"
                value-format="yyyy-MM-dd"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期">
              </el-date-picker>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">优惠券ID</div>
              <el-input v-model="query.couponActivityId" placeholder="请输入优惠券ID" @keyup.enter.native="handleSearch" />
            </el-col>
            <el-col :span="6">
              <div class="title">获券企业ID</div>
              <el-input v-model="query.eid" placeholder="请输入获券企业ID" @keyup.enter.native="handleSearch" />
            </el-col>
            <el-col :span="12">
              <div class="title">获券企业名称</div>
              <el-input v-model="query.ename" placeholder="请输入获券企业名称" @keyup.enter.native="handleSearch" />
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
      <div class="down-box clearfix">
        <div class="btn">
          <ylButton type="primary" plain @click="downLoadTemp">导出查询信息</ylButton>
        </div>
      </div>
      <!-- 底部列表 -->
      <div class="mar-t-8 pad-b-10 order-table-view">
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
          <el-table-column label="发券活动ID" min-width="120" align="center" prop="couponActivityAutoGiveId">
          </el-table-column>
          <el-table-column label="发券活动名称" min-width="100" align="center" prop="couponActivityAutoGiveName">
          </el-table-column>
          <el-table-column label="优惠券ID" min-width="100" align="center" prop="couponActivityId">
          </el-table-column>
          <el-table-column label="优惠券名称" min-width="120" align="center" prop="couponActivityName">
          </el-table-column>
          <el-table-column label="获券企业ID" min-width="200" align="center" prop="eid">
          </el-table-column>
          <el-table-column label="获券企业名称" min-width="200" align="center" prop="ename">
          </el-table-column>
          <el-table-column label="失败原因" min-width="200" align="center" prop="replyCode">
          </el-table-column>
          <el-table-column label="发放失败时间" min-width="200" align="center">
            <template slot-scope="{ row }">
              <div>{{ row.createTime | formatDate }}</div>
            </template>
          </el-table-column>
          <el-table-column label="操作" min-width="150" align="center">
            <template slot-scope="{ row }">
              <div>
                <yl-button type="text" @click="disposeClick(row)">运营处理</yl-button>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
    <!-- 作废弹框弹框 -->
    <yl-dialog
        title="自动发券运营处理"
        :visible.sync="disposeVisible"
        :show-footer="true"
        :destroy-on-close="true"
        @confirm="disposeConfirm"
      >
        <div class="stop-content">
          <div class="mar-b-10">
            优惠券已经添加，是否触发发放优惠券？
          </div>
        </div>
    </yl-dialog>
  </div>
</template>

<script>
import { createDownLoad } from '@/subject/admin/api/common'
import {
  queryGiveFailListPage,
  couponActivityAutoGiveAutoGive
} from '@/subject/admin/api/b2b_api/discount_coupon';

export default {
  name: 'SendFailList',
  components: {
  },
  computed: {
  },
  data() {
    return {
      loading: false,
      query: {
        page: 1,
        limit: 10,
        total: 0,
        autoGiveName: '',
        autoGiveId: '',
        time: [],
        couponActivityId: '',
        eid: '',
        ename: ''
      },
      dataList: [],
      // 停用弹框
      disposeVisible: false,
      currentRow: {}
    };
  },
  mounted() {
    this.query.autoGiveId = this.$route.params.autoGiveId
    this.getList()
  },
  methods: {
    async getList() {
      this.loading = true
      let query = this.query
      let data = await queryGiveFailListPage(
        query.page,
        query.limit,
        query.autoGiveName,
        query.autoGiveId,
        query.time && query.time.length ? query.time[0] : undefined,
        query.time && query.time.length > 1 ? query.time[1] : undefined,
        query.couponActivityId,
        query.eid,
        query.ename
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
        autoGiveName: '',
        autoGiveId: '',
        time: [],
        couponActivityId: '',
        eid: '',
        ename: ''
      }
    },
    // 停用点击
    disposeClick(row) {
      this.currentRow = row
      this.disposeVisible = true
    },
    async disposeConfirm() {
      this.$common.showLoad()
      let data = await couponActivityAutoGiveAutoGive(this.currentRow.id)
      this.$common.hideLoad()
      if (data != undefined) {
        this.$common.n_success('保存成功')
        this.disposeVisible = false
        this.$router.go(-1)
      }
    },
    // 导出
    async downLoadTemp() {
      let query = this.query
      this.$common.showLoad()
      let data = await createDownLoad({
        className: 'couponActivityAutoGiveFailPageListExportService',
        fileName: '自动发放失败优惠券导出',
        groupName: '自动发放优惠券',
        menuName: '优惠券管理-自动发放优惠券',
        searchConditionList: [
          {
            desc: '发券活动名称',
            name: 'couponActivityAutoGiveName',
            value: query.autoGiveName || ''
          },
          {
            desc: '发券活动ID',
            name: 'couponActivityAutoGiveId',
            value: query.autoGiveId || ''
          },
          {
            desc: '优惠券ID',
            name: 'couponActivityId',
            value: query.couponActivityId || ''
          },
          {
            desc: '获券企业ID',
            name: 'eid',
            value: query.eid
          },
          {
            desc: '获券企业名称',
            name: 'ename',
            value: query.ename
          },
          {
            desc: '发放失败时间-开始',
            name: 'createBeginTime',
            value: query.time && query.time.length ? query.time[0] : ''
          },
          {
            desc: '发放失败时间-结束',
            name: 'createEndTime',
            value: query.time && query.time.length > 1 ? query.time[1] : ''
          }
        ]
      })
      this.$common.hideLoad()
      if (data && data.result) {
        this.$common.n_success('创建下载任务成功，请在下载中心查看')
      }
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
