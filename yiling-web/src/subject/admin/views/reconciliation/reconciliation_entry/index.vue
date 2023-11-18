<template>
  <div class="app-container">
    <!-- 内容区 -->
    <div class="app-container-content">
      <!-- 上部条件搜索 -->
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="7">
              <div class="title">企业名称</div>
              <el-input v-model="query.name" @keyup.enter.native="searchEnter" placeholder="请输入企业名称" />
            </el-col>
            <el-col :span="7">
              <div class="title">企业编码</div>
              <el-input v-model="query.easCode" @keyup.enter.native="searchEnter" placeholder="请输入企业编码" />
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
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="7">
              <div class="title">申请单号</div>
              <el-input v-model="query.code" @keyup.enter.native="searchEnter" placeholder="请输入申请单号" />
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn
                :total="query.total"
                @search="handleSearch"
                @reset="handleReset"
              />
            </el-col>
          </el-row>
        </div>
      </div>
      <!-- 下部 企业入账信息列表-->
      <div>
        <!-- 导出按钮 -->
        <div class="down-box clearfix">
          <div class="btn">
            <ylButton type="primary" plain @click="downLoadTemp">导出查询结果</ylButton>
          </div>
        </div>
        <!-- 表格 -->
        <div class="mar-t-8 pad-b-10 order-table-view">
          <yl-table
            ref="table"
            :list="dataList"
            :total="query.total"
            :cell-class-name="() => 'border-1px-b'"
            show-header
            :page.sync="query.page"
            :limit.sync="query.size"
            :loading="loading"
            @getList="getList">
            <el-table-column label-class-name="mar-l-16" label="企业信息" min-width="380" align="left">
              <template slot-scope="{ row }">
                <div class="item text-l mar-l-16">
                  <div class="title font-size-lg bold">{{ row.name || '- -' }}</div>
                  <div class="item-text font-size-base font-title-color"><span>渠道类型：</span>{{ row.channelId | dictLabel(channelType) }}</div>
                  <div class="item-text font-size-base font-title-color"><span>企业状态：</span>{{ row.entStatus | enable }}</div>
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
                <!-- <div v-show="type == 2 && row.status == 1"><yl-button type="text" @click="auditClick(row)">审核入账</yl-button></div> -->
              </template>
            </el-table-column>
          </yl-table>
      </div>
      </div>
    </div>
  </div>
</template>

<script>
import { createDownLoad } from '@/subject/admin/api/common'
import { agreementApplyStatus,channelType } from '@/subject/admin/utils/busi'
import { queryApplyPageList } from '@/subject/admin/api/reconciliation'
export default {
  name: 'ReconciliationEntry',
  components: {},
  computed: {
    agreementApplyStatus(){
      return agreementApplyStatus()
    },
    channelType() {
      return channelType()
    }
  },
  data() {
    return {
      query: {
        name: '',
        easCode: '',
        code: '',//申请单号
        page: 1,
        size: 10,
        total: 0,
        // 审核状态
        status: 0
      },
      dataList: [],
      loading: false
     
    }
  },
  activated(){
    // 获取表格信息
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
    // 获取表格信息
    async getList() {
      let query =this.query;
      let data = await queryApplyPageList(
        query.code,
        query.page,
        query.name,
        query.easCode,
        // queryType
        query.size,
        query.status
      )
       if (data) {
         this.dataList=data.records;
         this.query.total = data.total
       }
    },
    // 搜索点击
    handleSearch(){
      this.query.page = 1;
      // 获取表格信息
      this.getList()
    },
    //清空
    handleReset(){
      this.query = {
        page: 1,
        size: 10,
        status: 0
      }
    },
    // 查看 跳转详情
    showDetail(val){
      this.$router.push({
        name: 'ReconciliationEntryInfo',
        params: { id: val.id}
      });
    },
    // 导出
    async downLoadTemp(){
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
            name: 'provinceCode',
            value: query.provinceCode || ''
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
          // {
          //   desc: '查询类型（默认商务） 1-商务 2-财务',
          //   name: 'queryType',
          //   value: this.type
          // }
        ]
      })
      this.$common.hideLoad()
      if (data && data.result) {
        this.$common.n_success('创建下载任务成功，请在下载中心查看')
      }
    },
    // 下载明细
    async downLoadDetail(row){
      this.$common.showLoad();
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
    }

  }

}
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
