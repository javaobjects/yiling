<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <!-- 内容区 -->
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box" style="margin-top:0;">
          <el-row class="box">
            <el-col :span="7">
              <div class="title">企业名称</div>
              <el-input v-model="query.name" placeholder="请输入企业名称" @keyup.enter.native="handleSearch" />
            </el-col>
            <el-col :span="7">
              <div class="title">企业编码</div>
              <el-input v-model="query.easCode" placeholder="请输入企业编码" @keyup.enter.native="handleSearch" />
            </el-col>
            <el-col :span="10">
              <div class="title">审核状态</div>
              <el-select v-model="query.status" placeholder="请选择审核状态">
                <el-option label="全部" :value="0"></el-option>
                <el-option
                  v-for="item in agreementApplyStatus"
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
            <el-col :span="7">
              <div class="title">申请单号</div>
              <el-input v-model="query.code" placeholder="请输入申请单号" @keyup.enter.native="handleSearch" />
            </el-col>
          </el-row>
        </div>
        <div class="search-box pad-t-8">
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
          <ylButton v-role-btn="['1']" type="primary" plain @click="downLoadTemp">导出查询结果</ylButton>
          <ylButton v-role-btn="['2']" type="primary" plain @click="applyClick">申请返利入账</ylButton>
          <ylButton v-role-btn="['3']" type="primary" plain @click="goImport">导入更新信息</ylButton>
        </div>
      </div>
      <!-- 底部列表 -->
      <div class="mar-t-8 pad-b-10 order-table-view">
        <yl-table
          ref="table"
          :list="dataList"
          :total="query.total"
          :cell-class-name="() => 'border-1px-b'"
          show-header
          :page.sync="query.page"
          :limit.sync="query.limit"
          :loading="loading"
          :cell-no-pad="true"
          @getList="getList">
          <el-table-column label-class-name="mar-l-16" label="企业信息" min-width="380" align="left">
            <template slot-scope="{ row }">
              <div class="item text-l mar-l-16">
                <div class="title font-size-lg bold">{{ row.name || '- -' }}</div>
                <div class="item-text font-size-base font-title-color"><span>渠道类型：</span>{{ row.channelId | dictLabel(channelType) }}</div>
                <div class="item-text font-size-base font-title-color"><span>企业状态：</span><span :class="[row.entStatus == '1' ? 'enable' : 'disable']">{{ row.entStatus | enable }}</span></div>
                <div class="item-text font-size-base font-title-color"><span>企业编码：</span>{{ row.easCode || '- -' }}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="申请入账信息" min-width="284" align="left">
            <template slot-scope="{ row }">
              <div class="item">
                <div class="title"></div>
                <div class="item-text font-size-base font-title-color"><span>申请金额：</span>{{ row.totalAmount | toThousand('￥') }} <yl-button class="detail-btn" type="text" @click="showDetail(row)">查看</yl-button></div>
                <div class="item-text font-size-base font-title-color"><span>申请状态：</span>{{ row.status | dictLabel(agreementApplyStatus) }}</div>
                <div class="item-text font-size-base font-title-color"><span>归属年月：</span>{{ row.dateTypeStr || '- - ' }}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="申请人/时间" min-width="306" align="left">
            <template slot-scope="{ row }">
              <div class="item">
                <div class="title"></div>
                <div class="item-text font-size-base font-title-color"><span>申请人：</span>{{ row.createUserName || '- -' }}</div>
                <div class="item-text font-size-base font-title-color"><span>申请时间：</span>{{ row.createTime	| formatDate }}</div>
                <div class="item-text font-size-base font-title-color"><span>申请单号：</span>{{ row.code || '- -' }}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="操作" min-width="120" align="center">
            <template slot-scope="{ row }">
              <div><yl-button type="text" @click="downLoadDetail(row)">下载明细</yl-button></div>
              <div v-show="row.status == 1"><yl-button v-role-btn="['4']" type="text" @click="auditClick(row)">审核入账</yl-button></div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </div>
</template>

<script>
import {
  getApplyPageList
} from '@/subject/pop/api/rebate';
import { createDownLoad } from '@/subject/pop/api/common'
import { agreementApplyStatus, channelType } from '@/subject/pop/utils/busi'

export default {
  name: 'RebateApply',
  components: {
  },
  computed: {
    // 退货单审核状态
    agreementApplyStatus() {
      return agreementApplyStatus()
    },
    channelType() {
      return channelType()
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
          title: '企业返利'
        },
        {
          title: '入账申请'
        }
      ],
      // type: 1-商务 2-信用科财务
      type: 1,
      loading: false,
      query: {
        page: 1,
        limit: 10,
        total: 0,
        // 审核状态
        status: 0
      },
      dataList: [],
      // 汇总数据
      totalData: {},
      // 审核弹框
      showAuditDialog: false,
      // 备注
      remark: '',
      // 审核当前点击的 row
      currentRow: {}
    };
  },
  activated() {
    // this.type = this.$route.meta.type
    this.getList()
  },
  methods: {
    async getList() {
      this.loading = true
      let query = this.query
      let data = await getApplyPageList(
        query.page,
        query.limit,
        query.name,
        query.easCode,
        query.status,
        query.code
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
        status: 0
      }
    },
    //跳转详情界面
    showDetail(row) {
      // 商务 type  1-商务 2-信用财务科  status 3-驳回
      let isEdit = row.status == 3
      // 跳转详情
      this.$router.push({
        name: 'RebateApplyInfo',
        params: { id: row.id, isAudit: 'false', isEdit: isEdit.toString() }
      });
    },
    // 审核点击
    auditClick(row) {
      // 跳转详情
      this.$router.push({
        name: 'RebateApplyInfo',
        params: { id: row.id, isAudit: 'true', isEdit: 'false' }
      });
    },
    //  申请返利入账
    applyClick() {
      this.$router.push({
        name: 'EnterAccountApply'
      });
    },
    // 去导入页面
    goImport() {
      this.$router.push({
        path: '/importFile/importFile_index',
        query: {
          action: '/admin/pop/api/v1/rebate/importRebateApplyOrderDetail'
        }
      })
    },
    // 下载明细
    async downLoadDetail(row) {
      this.$common.showLoad()
      let data = await createDownLoad({
        className: 'agreementApplyDetailExportService',
        fileName: '返利申请单',
        groupName: '返利申请导出',
        menuName: '企业返利 - 入账申请',
        searchConditionList: [
          {
            desc: '申请单id',
            name: 'applyId',
            value: row.id || ''
          }
        ]
      })
      this.$common.hideLoad()
      if (data && data.result) {
        this.$common.n_success('创建下载任务成功，请在下载中心查看')
      }
    },
    // 下载模板
    async downLoadTemp() {
      let query = this.query
      this.$common.showLoad()
      let data = await createDownLoad({
        className: 'agreementApplyBatchExportService',
        fileName: '导出查询结果',
        groupName: '返利申请导出',
        menuName: '企业返利 - 返利申请',
        searchConditionList: [
          {
            desc: '企业名称',
            name: 'name',
            value: query.name || ''
          },
          {
            desc: '企业编码',
            name: 'easCode',
            value: query.easCode || ''
          },
          {
            desc: '申请单号',
            name: 'code',
            value: query.code
          },
          {
            desc: '申请单状态',
            name: 'status',
            value: query.status
          }
        ]
      })
      this.$common.hideLoad()
      if (data && data.result) {
        this.$common.n_success('创建下载任务成功，请在下载中心查看')
      }
    }
  }
};
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
<style lang="scss">
  .order-table-view {
    .table-cell {
      border-bottom: 1px solid #DDDDDD;
    }
  }
</style>
