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
              <div class="title">企业名称</div>
              <el-autocomplete
                v-model="query.ename"
                value-key="name"
                value="id"
                :fetch-suggestions="eidQuerySearchAsync"
                :trigger-on-focus="false"
                placeholder="请选择企业"
                @input="enameHandle"
                @select="eidHandleSelect"
              ></el-autocomplete>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">创建人</div>
              <el-input v-model="query.createUserName" placeholder="请输入创建人姓名" @keyup.enter.native="handleSearch" />
            </el-col>
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
              <div class="title">承担方</div>
              <el-select v-model="query.bear" placeholder="请选择">
                <el-option label="全部" :value="0"></el-option>
                <el-option
                  v-for="item in bearArray"
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
            <el-col :span="6">
              <div class="title">活动类型</div>
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
            <el-col :span="12">
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
          <el-table-column label="活动名称" min-width="120" align="center" prop="name">
          </el-table-column>
          <el-table-column label="分类" min-width="100" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.sponsorType | dictLabel(sponsorTypeArray) }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="支付方式" min-width="100" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.payType }}
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
          <el-table-column label="承担方" min-width="100" align="center">
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
          <el-table-column label="预算金额" min-width="100" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.budgetAmount }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="运营备注" min-width="200" align="center" prop="operatingRemark">
          </el-table-column>
          <el-table-column label="参与次数" min-width="120" align="center">
            <template slot-scope="{ row }">
              <div>
                <yl-button type="text" @click="useNumClick(row)">{{ row.totalNum }}</yl-button>
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
    <!-- 参与次数 -->
    <yl-dialog title="参与次数明细" :visible.sync="useNumDialog" :show-footer="false">
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
                  {{ row.participateTime | formatDate }}
                </div>
              </template>
            </el-table-column>
            <el-table-column label="活动ID" min-width="100" align="center" prop="marketingPayId">
            </el-table-column>
            <el-table-column label="活动名称" min-width="200" align="center" prop="name">
            </el-table-column>
            <el-table-column label="买家企业ID" min-width="120" align="center" prop="eid">
            </el-table-column>
            <el-table-column label="买家企业名称" min-width="200" align="center" prop="ename">
            </el-table-column>
            <el-table-column label="订单号" min-width="200" align="center" prop="orderNo">
            </el-table-column>
            <el-table-column label="订单状态" min-width="100" align="center">
              <template slot-scope="{ row }">
                <div>{{ row.status }}</div>
              </template>
            </el-table-column>
            <el-table-column label="优惠金额" min-width="100" align="center">
              <template slot-scope="{ row }">
                <div>
                  {{ row.discountAmount | toThousand }}
                </div>
              </template>
            </el-table-column>
            <el-table-column label="订单实付金额" min-width="100" align="center">
              <template slot-scope="{ row }">
                <div>
                  {{ row.payment | toThousand }}
                </div>
              </template>
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
import { queryActivityEidByName } from '@/subject/admin/api/b2b_api/discount_coupon'
import {
  payPromptionActivityPageList,
  payPromptionActivityCopy,
  payPromptionActivityStop,
  getPayPromotionParticipatePageById
} from '@/subject/admin/api/b2b_api/marketing_manage'
import { createDownLoad } from '@/subject/admin/api/common'
import { orderStatus } from '@/subject/admin/utils/busi'

export default {
  name: 'PayPromotionList',
  components: {
  },
  computed: {
    // 订单状态
    orderStatus() {
      return orderStatus()
    }
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
      payTypeArray: [
        {
          label: '线下支付',
          value: 1
        },
        {
          label: '账期',
          value: 2
        },
        {
          label: '在线支付',
          value: 4
        }
      ],
      loading: false,
      query: {
        page: 1,
        limit: 10,
        total: 0,
        name: '',
        status: 0,
        progress: 0,
        createUserName: '',
        createTel: '',
        time: [],
        bear: 0,
        sponsorType: 0,
        operatingRemark: '',
        ename: '',
        eid: ''
      },
      dataList: [],
      // 活动使用数量弹框
      loading2: false,
      useNumDialog: false,
      sendNumList: [],
      sendNumTotal: 0,
      sendNumQuery: {
        page: 1,
        limit: 10
      },
      // 停用弹框
      stopVisible: false,
      currentOperationRow: {}
    };
  },
  activated() {
    this.getList()
  },
  methods: {
    async eidQuerySearchAsync(queryString, cb) {
      let data = await queryActivityEidByName(queryString)
      if (data && data.list) {
        cb(data.list)
      }
    },
    eidHandleSelect(item) {
      this.query.eid = item.id
    },
    enameHandle() {
      this.query.eid = ''
    },
    async getList() {
      this.loading = true
      let query = this.query
      let data = await payPromptionActivityPageList(
        query.page,
        query.limit,
        query.name,
        query.status,
        query.progress,
        query.eid,
        query.createUserName,
        query.createTel,
        query.time && query.time.length ? query.time[0] : undefined,
        query.time && query.time.length > 1 ? query.time[1] : undefined,
        query.bear,
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
        name: '',
        status: 0,
        progress: 0,
        createUserName: '',
        createTel: '',
        time: [],
        bear: 0,
        sponsorType: 0,
        operatingRemark: '',
        ename: '',
        eid: ''
      }
    },
    // 查看 operationType: 1-查看 2-修改 3-新增 4-复制
    addClick() {
      this.$router.push({
        name: 'PayPromotionEdit',
        params: {
          operationType: 3
        }
      });
    },
    // 查看 operationType: 1-查看 2-修改 3-新增 4-复制
    showDetail(row) {
      // 跳转详情
      this.$router.push({
        name: 'PayPromotionEdit',
        params: {
          id: row.id,
          operationType: 1
        }
      });
    },
    // 修改
    editClick(row) {
      this.$router.push({
        name: 'PayPromotionEdit',
        params: {
          id: row.id,
          operationType: 2
        }
      });
    },
    // 复制
    async copyClick(row) {
      this.$common.showLoad()
      let data = await payPromptionActivityCopy(row.id)
      this.$common.hideLoad()
      if (data) {
        this.$common.n_success('复制成功');
        // 进入修改
        this.$router.push({
          name: 'PayPromotionEdit',
          params: {
            id: data.id,
            operationType: 4
          }
        })
      } 

    },
    // 参与次数点击
    useNumClick(row) {
      this.sendNumQuery = {
        page: 1,
        limit: 10
      }
      this.useNumDialog = true
      this.currentOperationRow = row
      this.sendNumTotalListPageListMethod()
    },
    async sendNumTotalListPageListMethod() {
      this.loading2 = true
      let sendNumQuery = this.sendNumQuery
      let data = await getPayPromotionParticipatePageById(
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
      let data = await payPromptionActivityStop(this.currentOperationRow.id ,2)
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
        className: 'payPromotionOrderInfoExportService',
        fileName: '支付促销参与明细',
        groupName: '营销管理支付促销',
        menuName: '营销管理支付促销',
        searchConditionList: [
          {
            desc: '活动id',
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
