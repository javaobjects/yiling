<template>
  <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <div class="common-box box-search">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">渠道名称</div>
              <el-input v-model="query.enterpriseName" placeholder="请输入渠道名称" @keyup.enter.native="handleSearch"/>
            </el-col>
          </el-row>
        </div>
        <div class="search-btn-box">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn :total="query.total" @search="handleSearch" @reset="handleReset" />
            </el-col>
          </el-row>
        </div>
      </div>
      <div class="down-box clearfix">
        <div class="btn">
          <yl-button type="primary" plain @click="addChannel">添加</yl-button>
        </div>
      </div>
      <div class="table-box mar-t-8">
        <yl-table
          stripe
          :show-header="true"
          :list="dataList"
          :total="query.total"
          :page.sync="query.current"
          :limit.sync="query.size"
          :loading="loading"
          @getList="getList"
        >
          <el-table-column label="渠道名称" min-width="100" align="center" prop="enterpriseName"></el-table-column>
          <el-table-column label="机构代码" min-width="100" align="center" prop="licenseNumber"></el-table-column>
          <el-table-column label="业务渠道类型" min-width="120" align="center">
            <template slot-scope="{ row }">
              <div>{{ row.channelType | dictLabel(hmcPurchaseChannelType) }}</div>
            </template>
          </el-table-column>
          <el-table-column label="进货渠道管控状态" min-width="100" align="center">
            <template slot-scope="{ row }">
              <span v-if="row.controlStatus === 1" :class="['channel-status', row.controlStatus === 1 ? 'col-down' : '' ]">{{ row.controlStatus | controlStatusFilter }}</span>
              <span v-else :class="['channel-status', row.controlStatus == 0 ? 'col-up' : '' ]">{{ row.controlStatus | controlStatusFilter }}</span>
              <el-switch :value="row.controlStatus === 1" class="switch" @change="e => switchChange(e, row)"> </el-switch>
            </template>
          </el-table-column>
        </yl-table>
      </div>
      <div class="flex-row-center bottom-view">
        <yl-button plain type="primary" @click="$router.go(-1)">返回</yl-button>
      </div>
      <!-- 新建/修改渠道商 -->
      <add-channel
        ref="channel"
        :title="channelTitle"
        :goods-control-id="$route.params.id"
        :type="channelType"
        :form-data="editFormData"
        @freshData="getList"
      >
      </add-channel>
    </div>
  </div>
</template>

<script>
import { queryPurchaseList, updatePurchase } from '@/subject/admin/api/cmp_api/insurance_basic_setting'
import { formatDate } from '@/common/utils'
import { hmcPurchaseChannelType } from '@/subject/admin/busi/cmp/insurance_basic_setting'

import AddChannel from '../component/AddChannel'
export default {
  name: 'InsuranceChannelManage',
  components: {
    AddChannel
  },
  computed: {
    hmcPurchaseChannelType() {
      return hmcPurchaseChannelType()
    }
  },
  filters: {
    controlStatusFilter(e) {
      return e === 1 ? '开启' : '关闭'
    }
  },
  mounted() {
    this.id = this.$route.params.id
    if (this.id) {
      this.getList()
    }
  },
  data() {
    return {
      query: {
        current: 1,
        size: 10,
        total: 0,
        enterpriseName: ''
      },
      // 列表
      dataList: [],
      loading: false,
      channelTitle: '',
      channelType: 'ADD',
      editFormData: {}
    }
  },
  methods: {
    async getList() {
      this.loading = true
      let query = this.query
      let data = await queryPurchaseList(
        query.current,
        query.size,
        this.id,
        query.enterpriseName
      )
      this.loading = false
      if (data) {
        this.dataList = data.records
        query.total = data.total
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
        enterpriseName: ''
      }
    },
    // 状态修改
    async switchChange(e, row) {
      const h = this.$createElement
      if (row.controlStatus !== 0) {
        this.$confirm('', '停用', {
          title: '停用',
          message: h('div', { class: 'message-info' }, [
            h('div', { class: 'name' }, `${row.enterpriseName} `),
            h('div', { class: 'info-item' }, `创建时间: ${formatDate(row.createTime)}`),
            h('div', { class: 'info-item' }, `采购业务渠道: ${this.hmcPurchaseChannelType.find(item => item.value == row.channelType).label}`),
            h('div', { class: 'info-item' }, `机构代码: ${row.licenseNumber}`)
          ]),
          confirmButtonText: '停止',
          cancelButtonText: '取消',
          type: 'warning',
          customClass: 'stop-channel-cfm'
        }).then(async () => {
          // 停用
          row.controlStatus = 0
          this.$common.showLoad()
          let data = await updatePurchase( row.id, 0)
          this.$common.hideLoad()
          if (data !== undefined) {
            this.$common.n_success('操作成功')
            this.getList()
          }
        })
      } else {
        // 开启
        row.controlStatus = 1
        this.$common.showLoad()
        let data = await updatePurchase( row.id, 1)
        this.$common.hideLoad()
        if (data !== undefined) {
          this.$common.n_success('操作成功')
          this.getList()
        }
      }
    },
    // 添加
    addChannel() {
      this.channelTitle = '添加渠道商'
      this.channelType = 'ADD'
      this.$refs.channel.openDialog()
    }
  }
}
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
<style lang="scss">
.stop-channel-cfm {
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
  .el-message-box__content {
    padding: 16px;
    .el-message-box__container {
      display: flex;
      align-items: center;
      .el-message-box__status {
        display: none;
        position: relative;
        color: #fa8c15;
        font-size: 16px !important;
        transform: translateY(0);
      }
      .el-message-box__message {
        padding-left: 3px;
        .message-info {
          .name {
            font-size: 16px;
            font-weight: 600;
          }
          .info-item {
            margin-top: 8px;
          }
        }
      }
    }
  }
  .el-message-box__btns {
    border-top: 1px solid #f0f0f0;
  }
}
</style>