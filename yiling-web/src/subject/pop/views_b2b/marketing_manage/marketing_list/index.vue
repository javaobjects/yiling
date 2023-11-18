<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <!-- 内容区 -->
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box" style="margin-top:0;">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">活动名称</div>
              <el-input v-model="query.name" placeholder="请输入活动名称" @keyup.enter.native="handleSearch" />
            </el-col>
            <el-col :span="6">
              <div class="title">活动类型</div>
              <el-select v-model="query.type" placeholder="请选择">
                <el-option label="全部" :value="0"></el-option>
                <el-option
                  v-for="item in typeArray"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <div class="title">活动状态</div>
              <el-select v-model="query.status" placeholder="请选择">
                <el-option label="全部" :value="0"></el-option>
                <el-option
                  v-for="item in statusArray"
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
            <el-col :span="6">
              <div class="title">活动创建人</div>
              <el-input v-model="query.createUserName" placeholder="请输入创建人姓名" @keyup.enter.native="handleSearch" />
            </el-col>
            <el-col :span="6">
              <div class="title">创建人手机号</div>
              <el-input v-model="query.createUserTel" placeholder="请输入创建人手机号" @keyup.enter.native="handleSearch" />
            </el-col>
            <el-col :span="12">
              <div class="title">活动有效期</div>
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
        <div class="search-box">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">运营备注</div>
              <el-input v-model="query.remark" placeholder="请输入运营备注" @keyup.enter.native="handleSearch" />
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
      <!-- 导出按钮 -->
      <div class="down-box clearfix">
        <div class="btn">
          <ylButton v-role-btn="['1']" type="primary" plain @click="addClick">创建促销活动</ylButton>
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
          <el-table-column label="活动ID" min-width="120" align="center" prop="id">
          </el-table-column>
          <el-table-column label="促销名称" min-width="100" align="center" prop="name">
          </el-table-column>
          <el-table-column label="开始时间" min-width="100" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.beginTime | formatDate }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="结束时间" min-width="120" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.endTime | formatDate }}
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
          <el-table-column label="创建人" min-width="200" align="center" prop="createUserName">
          </el-table-column>
          <el-table-column label="创建人手机号" min-width="150" align="center" prop="createUserTel">
          </el-table-column>
          <el-table-column label="活动类型" min-width="100" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.type | dictLabel(typeArray) }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="活动承担方" min-width="100" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.bear | dictLabel(bearArray) }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="活动状态" min-width="100" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.status | dictLabel(statusArray) }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="活动进度" min-width="100" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.progress | dictLabel(progressArray) }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="上限金额" min-width="100" align="center" prop="budgetAmount">
          </el-table-column>
          <el-table-column label="活动使用数量" min-width="120" align="center" prop="replyCode">
            <template slot-scope="{ row }">
              <div>
                <yl-button type="text" @click="useNumClick(row)">{{ row.activityQuantity }}</yl-button>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="运营备注" min-width="200" align="center" prop="remark">
          </el-table-column>
          <el-table-column label="操作" min-width="150" align="center" fixed="right">
            <template slot-scope="{ row }">
              <div class="operation-view">
                <div class="option">
                  <yl-button v-role-btn="['2']" type="text" @click="showDetail(row)">查看</yl-button>
                  <yl-button v-role-btn="['3']" type="text" :disabled="row.progress != 1" @click="editClick(row)">编辑</yl-button>
                  <yl-button v-role-btn="['4']" type="text" @click="copyClick(row)">复制</yl-button>
                  <yl-button v-role-btn="['5']" type="text" :disabled="row.status == 2 || row.status == 3" @click="stopClick(row)">停用</yl-button>
                </div>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
    <!-- 活动使用数量 -->
    <yl-dialog title="活动使用数量" :visible.sync="useNumDialog" :show-footer="false">
      <div class="dialog-content-box">
        <div>
          <ylButton type="primary" plain @click="downLoadTemp">导出</ylButton>
        </div>
        <div>
          <el-row class="mar-t-10">
            <el-col :span="6">
              <div class="font-size-base font-title-color"><span class="font-light-color mar-r-8">活动类型：</span>{{ currentOperationRow.type | dictLabel(typeArray) }}</div>
            </el-col>
          </el-row>
        </div>
        <div class="mar-t-10" style="padding-bottom: 10px;background: #FFFFFF;">
          <yl-table
            border
            show-header
            :list="sendNumList"
            :total="sendNumTotal"
            :page.sync="sendNumQuery.page"
            :limit.sync="sendNumQuery.limit"
            :loading="loading2"
            @getList="sendNumTotalListPageListMethod"
          >
            <el-table-column label="商品名称" min-width="120" align="center" prop="giftName">
            </el-table-column>
            <el-table-column label="关联订单" min-width="100" align="center" prop="orderNo">
            </el-table-column>
            <el-table-column label="员工姓名" min-width="100" align="center" prop="buyerName">
            </el-table-column>
            <el-table-column label="员工电话" min-width="120" align="center" prop="buyerTel">
            </el-table-column>
            <el-table-column label="企业地址" min-width="200" align="center" prop="address">
            </el-table-column>
          </yl-table>
        </div>
      </div>
    </yl-dialog>
    <!-- 停用弹框 -->
    <yl-dialog
        title="提示"
        :visible.sync="stopVisible"
        :show-footer="true"
        :destroy-on-close="true"
        width="500px"
        @confirm="stopConfirm"
      >
        <div class="stop-content">
          <div class="tip-box">
            <i class="el-icon-warning"></i>
            <span>活动“停用”未领取用户活动券用户将不允许在领取活动券，剩余活动券停止发放</span>
          </div>
        </div>
    </yl-dialog>
    <!-- 作废弹框 -->
    <yl-dialog
        title="提示"
        :visible.sync="disposeVisible"
        :show-footer="true"
        :destroy-on-close="true"
        width="500px"
        @confirm="disposeConfirm"
      >
        <div class="stop-content">
          <div class="tip-box">
            <i class="el-icon-warning"></i>
            <span>活动“作废”未领取、已领取未使用用户活动券将不允许在领取和使用活动券，全部未使用活动券隐藏</span>
          </div>
        </div>
    </yl-dialog>
  </div>
</template>

<script>
import {
  promotionActivityPageList,
  promotionActivityStatus,
  pageGiftOrder
} from '@/subject/pop/api/b2b_api/marketing_manage';
import { createDownLoad } from '@/subject/pop/api/common'

export default {
  name: 'MarketingList',
  components: {
  },
  computed: {

  },
  data() {
    return {
      // 头部导航
      navList: [
        {
          title: '首页',
          path: '/b2b_dashboard'
        },
        {
          title: '营销管理'
        },
        {
          title: '营销活动'
        }
      ],
      // 活动进度
      progressArray: [
        {
          label: '待开始',
          value: 1
        },
        {
          label: '进行中',
          value: 2
        },
        {
          label: '已结束',
          value: 3
        }
      ],
      // 活动类型
      typeArray: [
        {
          label: '满赠',
          value: 1
        }
      ],
      statusArray: [
        {
          label: '启用',
          value: 1
        },
        {
          label: '停用',
          value: 2
        }
      ],
      bearArray: [
        {
          label: '平台',
          value: 1
        },
        {
          label: '商家',
          value: 2
        }
      ],
      loading: false,
      query: {
        page: 1,
        limit: 10,
        total: 0,
        name: '',
        type: 0,
        status: 0,
        createUserName: '',
        createUserTel: '',
        time: [],
        remark: ''
      },
      dataList: [],
      // 活动使用数量弹框
      loading2: false,
      useNumDialog: false,
      sendNumList: [],
      sendNumTotal: 0,
      sendNumQuery: {},
      // 停用弹框
      stopVisible: false,
      // 作废弹框
      disposeVisible: false,
      currentOperationRow: {}
    };
  },
  activated() {
    this.getList()
  },
  methods: {
    async getList() {
      this.loading = true
      let query = this.query
      let data = await promotionActivityPageList(
        query.page,
        query.limit,
        query.name,
        query.type,
        query.status,
        query.createUserName,
        query.createUserTel,
        query.time && query.time.length ? query.time[0] : undefined,
        query.time && query.time.length > 1 ? query.time[1] : undefined,
        query.remark
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
        name: '',
        type: 0,
        status: 0,
        createUserName: '',
        time: [],
        remark: ''
      }
    },
    // 创建促销活动 // 查看 operationType: 1-查看 2-修改 3-新增 4-复制
    addClick() {
      this.$router.push({
        name: 'MarketingEdit',
        params: {
          operationType: 3
        }
      });
    },
    // 查看 operationType: 1-查看 2-修改 3-新增 4-复制
    showDetail(row) {
      // 跳转详情
      this.$router.push({
        name: 'MarketingEdit',
        params: {
          id: row.id,
          operationType: 1
        }
      });
    },
    // 修改
    editClick(row) {
      this.$router.push({
        name: 'MarketingEdit',
        params: {
          id: row.id,
          operationType: 2
        }
      });
    },
    // 复制
    async copyClick(row) {
      this.$router.push({
        name: 'MarketingEdit',
        params: {
          id: row.id,
          operationType: 4
        }
      });
    },
    // 使用数量点击
    useNumClick(row) {
      this.useNumDialog = true
      this.currentOperationRow = row
      this.sendNumTotalListPageListMethod()
    },
    async sendNumTotalListPageListMethod() {
      this.loading2 = true
      let sendNumQuery = this.sendNumQuery
      let data = await pageGiftOrder(
        sendNumQuery.page,
        sendNumQuery.limit,
        this.currentOperationRow.id
      );
      this.loading2 = false
      if (data) {
        this.sendNumList = data.records
        this.sendNumTotal = data.total
      }
    },
    // 停用点击
    stopClick(row) {
      this.currentOperationRow = row
      this.stopVisible = true
    },
    // 作废点击
    disposeClick(row) {
      this.currentOperationRow = row
      this.disposeVisible = true
    },
    async stopConfirm() {
      this.$common.showLoad()
      let data = await promotionActivityStatus(this.currentOperationRow.id ,2)
      this.$common.hideLoad()
      if (typeof data != 'undefined') {
        this.$common.n_success('停用成功')
        this.stopVisible = false
        this.getList()
      }
    },
    async disposeConfirm() {
      this.$common.showLoad()
      let data = await promotionActivityStatus(this.currentOperationRow.id ,3)
      this.$common.hideLoad()
      if (typeof data != 'undefined') {
        this.$common.n_success('作废成功')
        this.disposeVisible = false
        this.getList()
      }
    },
    // 导出
    async downLoadTemp() {
      this.$common.showLoad()
      let data = await createDownLoad({
        className: 'promotionGoodsGiftUsedExportService',
        fileName: '导出满赠活动已使用信息',
        groupName: '满赠活动已使用信息导出',
        menuName: '满赠活动已使用信息',
        searchConditionList: [
          {
            desc: '活动id',
            name: 'promotionActivityId',
            value: this.currentOperationRow.id
          }
        ]
      })
      this.$common.hideLoad()
      if (typeof data != 'undefined') {
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
