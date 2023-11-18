<template>
  <div class="app-container">
    <!-- 内容区 -->
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">活动名称</div>
              <el-input v-model="query.name" placeholder="请输入活动名称" @keyup.enter.native="handleSearch" />
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
            <el-col :span="6">
              <div class="title">活动进度</div>
              <el-select v-model="query.progress" placeholder="请选择">
                <el-option label="全部" :value="0"></el-option>
                <el-option
                  v-for="item in progressArray"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <div class="title">创建人</div>
              <el-input v-model="query.createUserName" placeholder="请输入创建人姓名" @keyup.enter.native="handleSearch" />
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">创建人手机号</div>
              <el-input v-model="query.createTel" placeholder="请输入创建人手机号" @keyup.enter.native="handleSearch" />
            </el-col>
            <el-col :span="6">
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
            <el-col :span="6">
              <div class="title">分类</div>
              <el-select v-model="query.sponsorType" placeholder="请选择">
                <el-option label="全部" :value="0"></el-option>
                <el-option
                  v-for="item in sponsorTypeArray"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <div class="title">运营备注</div>
              <el-input v-model="query.operatingRemark" placeholder="请输入运营备注" @keyup.enter.native="handleSearch" />
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
        <ylButton type="primary" @click="addClick">添加</ylButton>
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
          <el-table-column label="活动ID" min-width="100" align="center" prop="id">
          </el-table-column>
          <el-table-column label="活动名称" min-width="180" align="center" prop="name">
          </el-table-column>
          <el-table-column label="分类" min-width="100" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.sponsorType | dictLabel(sponsorTypeArray) }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="开始时间" min-width="160" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.beginTime | formatDate }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="结束时间" min-width="160" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.endTime | formatDate }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="创建时间" min-width="160" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.createTime | formatDate }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="创建人" min-width="120" align="center" prop="createUserName">
          </el-table-column>
          <el-table-column label="创建人手机号" min-width="150" align="center" prop="createUserTel">
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
          <el-table-column label="运营备注" min-width="200" align="center" prop="operatingRemark">
          </el-table-column>
          <el-table-column label="预售订单数量" min-width="120" align="center">
            <template slot-scope="{ row }">
              <div>
                <yl-button type="text" @click="presaleOrderNumClick(row)">{{ row.presaleOrderNum }}</yl-button>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="操作" min-width="180" align="center" fixed="right">
            <template slot-scope="{ row }">
              <div class="operation-view">
                <div class="option">
                  <yl-button type="text" :disabled="!row.lookFlag" @click="showDetail(row)">查看</yl-button>
                  <yl-button type="text" :disabled="!row.updateFlag" @click="editClick(row)">编辑</yl-button>
                  <yl-button type="text" :disabled="!row.copyFlag" @click="copyClick(row)">复制</yl-button>
                </div>
                <div class="option">
                  <yl-button type="text" :disabled="!row.stopFlag" @click="stopClick(row)">停用</yl-button>
                </div>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
    <!-- 预售订单数量明细 -->
    <yl-dialog title="预售订单数量明细" :visible.sync="presaleOrderNumDialog" :show-footer="false">
      <div class="dialog-content-box">
        <div>
          <ylButton type="primary" plain @click="downLoadTemp">导出</ylButton>
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
            <el-table-column label="序号" min-width="55" align="center">
              <template slot-scope="{ $index }">
                <div class="font-size-base">{{ (sendNumQuery.page - 1) * sendNumQuery.limit + $index + 1 }}</div>
              </template>
            </el-table-column>
            <el-table-column label="时间" min-width="160" align="center">
              <template slot-scope="{ row }">
                <div>
                  {{ row.createTime | formatDate }}
                </div>
              </template>
            </el-table-column>
            <el-table-column label="活动ID" min-width="100" align="center" prop="id">
            </el-table-column>
            <el-table-column label="活动名称" min-width="200" align="center" prop="name">
            </el-table-column>
            <el-table-column label="企业ID" min-width="100" align="center" prop="eid">
            </el-table-column>
            <el-table-column label="企业名称" min-width="200" align="center" prop="ename">
            </el-table-column>
            <el-table-column label="订单编号" min-width="200" align="center" prop="orderNo">
            </el-table-column>
            <el-table-column label="商品编号" min-width="100" align="center" prop="goodsId">
            </el-table-column>
            <el-table-column label="商品名称" min-width="200" align="center" prop="goodsName">
            </el-table-column>
            <el-table-column label="规格" min-width="100" align="center" prop="sellSpecifications">
            </el-table-column>
            <el-table-column label="生产厂家" min-width="200" align="center" prop="manufacturer">
            </el-table-column>
            <el-table-column label="商家" min-width="200" align="center" prop="sellerEname">
            </el-table-column>
            <el-table-column label="预售价" min-width="100" align="center" prop="presalePrice">
              <template slot-scope="{ row }">
                <div>
                  {{ row.presalePrice | toThousand('￥') }}
                </div>
              </template>
            </el-table-column>
            <el-table-column label="数量" min-width="100" align="center" prop="goodsQuantity">
            </el-table-column>
            <el-table-column label="预售类型" min-width="100" align="center" prop="presaleTypeStr">
            </el-table-column>
            <el-table-column label="定金比例" min-width="100" align="center" prop="depositRatio">
              <template slot-scope="{ row }">
                <div>
                  {{ row.depositRatio }}%
                </div>
              </template>
            </el-table-column>
            <el-table-column label="促销方式" min-width="200" align="center" prop="goodsPresaleType">
            </el-table-column>
            <el-table-column label="定金" min-width="100" align="center" prop="depositAmount">
              <template slot-scope="{ row }">
                <div>
                  {{ row.depositAmount | toThousand('￥') }}
                </div>
              </template>
            </el-table-column>
            <el-table-column label="尾款" min-width="100" align="center" prop="balanceAmount">
              <template slot-scope="{ row }">
                <div>
                  {{ row.balanceAmount | toThousand('￥') }}
                </div>
              </template>
            </el-table-column>
            <el-table-column label="优惠金额" min-width="100" align="center" prop="discountAmount">
              <template slot-scope="{ row }">
                <div>
                  {{ row.discountAmount | toThousand('￥') }}
                </div>
              </template>
            </el-table-column>
            <el-table-column label="实付总金额" min-width="100" align="center" prop="totalAmount">
              <template slot-scope="{ row }">
                <div>
                  {{ row.totalAmount | toThousand('￥') }}
                </div>
              </template>
            </el-table-column>
            <el-table-column label="状态" min-width="100" align="center" prop="status">
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
            <span>是否确认停用？</span>
          </div>
        </div>
    </yl-dialog>
  </div>
</template>

<script>

import { presaleActivityPageList, pageListForPresaleOrderInfo, presaleActivityUpdateStatus, presaleActivityCopy } from '@/subject/admin/api/view_marketing/goods_presell'
import { createDownLoad } from '@/subject/admin/api/common'

export default {
  name: 'B2bPresellList',
  components: {
  },
  computed: {
  },
  data() {
    return {
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
      // 活动状态
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
      // 分类
      sponsorTypeArray: [
        {
          label: '平台活动',
          value: 1
        },
        {
          label: '商家活动',
          value: 2
        }
      ],
      loading: false,
      query: {
        page: 1,
        limit: 10,
        total: 0,
        status: 0,
        progress: 0,
        sponsorType: 0,
        time: []
      },
      dataList: [],
      // 预售订单数量弹框
      loading2: false,
      presaleOrderNumDialog: false,
      sendNumList: [],
      sendNumTotal: 0,
      sendNumQuery: {
        page: 1,
        limit: 10
      },
      stopVisible: false
    };
  },
  activated() {
    this.getList()
  },
  methods: {
    async getList() {
      this.loading = true
      let query = this.query
      let data = await presaleActivityPageList(
        query.page,
        query.limit,
        query.name,
        query.status,
        query.progress,
        query.createUserName,
        query.createTel,
        query.time && query.time.length ? query.time[0] : undefined,
        query.time && query.time.length > 1 ? query.time[1] : undefined,
        query.sponsorType,
        query.operatingRemark
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
        status: 0,
        progress: 0,
        sponsorType: 0,
        time: []
      }
    },
    // 查看 operationType: 1-查看 2-修改 3-新增 4-复制
    addClick() {
      this.$router.push({
        name: 'PresellEdit',
        params: {
          operationType: 3
        }
      });
    },
    // 查看 operationType: 1-查看 2-修改 3-新增 4-复制
    showDetail(row) {
      // 跳转详情
      this.$router.push({
        name: 'PresellEdit',
        params: {
          id: row.id,
          operationType: 1
        }
      });
    },
    // 修改
    editClick(row) {
      this.$router.push({
        name: 'PresellEdit',
        params: {
          id: row.id,
          operationType: 2
        }
      });
    },
    // 复制
    async copyClick(row) {
      this.$common.showLoad()
      let data = await presaleActivityCopy(row.id)
      this.$common.hideLoad()
      if (data) {
        this.$common.n_success('复制成功');
        // 进入修改
        this.$router.push({
          name: 'PresellEdit',
          params: {
            id: data,
            operationType: 4
          }
        })
      }

    },
    // 预售订单数量明细
    presaleOrderNumClick(row) {
      this.presaleOrderNumDialog = true
      this.currentOperationRow = row
      this.sendNumTotalListPageListMethod()
    },
    async sendNumTotalListPageListMethod() {
      this.loading2 = true
      let sendNumQuery = this.sendNumQuery
      let data = await pageListForPresaleOrderInfo(
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
    async stopConfirm() {
      this.$common.showLoad()
      let data = await presaleActivityUpdateStatus(this.currentOperationRow.id)
      this.$common.hideLoad()
      if (typeof data != 'undefined') {
        this.$common.n_success('停用成功')
        this.stopVisible = false
        this.getList()
      }
    },
    // 导出
    async downLoadTemp() {
      this.$common.showLoad()
      let data = await createDownLoad({
        className: 'PresaleActivityOrderInfoExportService',
        fileName: '预售活动关联订单信息导出',
        groupName: '预售活动关联订单信息',
        menuName: '营销管理-预售活动',
        searchConditionList: [
          {
            desc: '预售活动id',
            name: 'id',
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
