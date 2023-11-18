<template>
  <div class="app-container">
    <!-- 内容区 -->
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">领券活动名称</div>
              <el-input v-model="query.name" placeholder="请输入领券活动名称" @keyup.enter.native="handleSearch" />
            </el-col>
            <el-col :span="6">
              <div class="title">领取活动ID</div>
              <el-input v-model="query.id" placeholder="请输入领券活动ID" @keyup.enter.native="handleSearch" />
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
              <div class="title">领取活动状态</div>
              <el-select v-model="query.activityStatus" placeholder="请选择状态">
                <el-option label="全部" :value="0"></el-option>
                <el-option
                  v-for="item in activityStatusArray"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-col>
            <el-col :span="12">
              <div class="title">启用状态</div>
              <el-select v-model="query.status" placeholder="请选择启用状态">
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
        <ylButton type="primary" @click="addClick">添加自助领取</ylButton>
      </div>
      <!-- 底部列表 -->
      <div class="mar-t-8 order-table-view">
        <yl-table
          ref="table"
          stripe
          :list="dataList"
          :total="query.total"
          :row-class-name="() => 'table-row'"
          :cell-class-name="getCellClass"
          show-header
          :page.sync="query.page"
          :limit.sync="query.limit"
          :loading="loading"
          :cell-no-pad="true"
          @getList="getList">
          <el-table-column label-class-name="mar-l-16" label="优惠券信息" min-width="380" align="left">
            <template slot-scope="{ row }">
              <div class="item text-l mar-l-16">
                <div class="title font-size-lg bold">{{ row.name }}</div>
                <div class="item-text font-size-base font-title-color"><span>领券活动ID：</span>{{ row.id }}</div>
                <!-- <div class="item-text font-size-base font-title-color"><span>发券类型：</span>{{ row.type | dictLabel(typeArray) }}</div> -->
                <div class="item-text font-size-base font-title-color"><span>活动状态：</span>{{ row.activityStatus | dictLabel(activityStatusArray) }}</div>
                <div class="item-text font-size-base font-title-color"><span>活动时间：</span>{{ row.beginTime | formatDate }} - {{ row.endTime | formatDate }}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="优惠券使用状况" min-width="240" align="left">
            <template slot-scope="{ row }">
              <div class="item">
                <div class="title"></div>
                <div class="item-text font-size-base font-title-color"><span>已领取数量：</span><yl-button class="detail-btn" type="text" @click="sendNumClick(row)">{{ row.receivedNum }}</yl-button></div>
                <div class="item-text font-size-base font-title-color"><span>启用状态：</span>{{ row.status | dictLabel(statusArray) }}</div>
                <div class="item-text font-size-base font-title-color"></div>
                <div class="item-text font-size-base font-title-color"></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="商品信息" min-width="330" align="left">
            <template slot-scope="{ row }">
              <div class="item">
                <div class="title"></div>
                <div class="item-text font-size-base font-title-color"><span>创建人：</span>{{ row.createUserName }}</div>
                <div class="item-text font-size-base font-title-color"><span>创建时间：</span>{{ row.createTime | formatDate }}</div>
                <div class="item-text font-size-base font-title-color"><span>修改人：</span>{{ row.updateUserName }}</div>
                <div class="item-text font-size-base font-title-color"><span>修改时间：</span>{{ row.updateTime | formatDate }}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="操作" min-width="150" align="left">
            <template slot-scope="{ row }">
              <div class="operation-view">
                <div class="option">
                  <yl-button type="text" @click="showDetail(row)">查看</yl-button>
                  <yl-button type="text" :disabled="!row.updateFlag" @click="editClick(row)">修改</yl-button>
                  <yl-button :disabled="row.status == 3" v-if="row.status == 1" type="text" @click="stopClick(row)">停用</yl-button>
                  <yl-button :disabled="row.status == 3" v-else type="text" @click="enableClick(row)">启用</yl-button>
                </div>
                <div class="option">
                  <!-- <yl-button type="text" @click="showDetail(row)">导出</yl-button> -->
                  <yl-button type="text" @click="sendFailClick(row)">发放失败</yl-button>
                  <yl-button :disabled="!row.scrapFlag" type="text" @click="cancellationClick(row)">作废</yl-button>
                </div>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
    <!-- 已发放数量 -->
    <yl-dialog title="已领取优惠券查看" :visible.sync="sendNumDialog" :show-footer="false">
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
            @getList="getSendList"
          >
            <el-table-column label="优惠券ID" min-width="120" align="center" prop="id">
            </el-table-column>
            <el-table-column label="获券企业ID" min-width="100" align="center" prop="eid">
            </el-table-column>
            <el-table-column label="获券企业名称" min-width="120" align="center" prop="ename">
            </el-table-column>
            <el-table-column label="发放方式" min-width="100" align="center">
              <template slot-scope="{ row }">
                <div>{{ row.getType | dictLabel(getTypeArray) }}</div>
              </template>
            </el-table-column>
            <el-table-column label="发放时间" min-width="200" align="center">
              <template slot-scope="{ row }">
                <div>{{ row.getTime | formatDate }}</div>
              </template>
            </el-table-column>
          </yl-table>
        </div>
      </div>
    </yl-dialog>
    <!-- 停用弹框弹框 -->
    <yl-dialog
        title="停用"
        :visible.sync="stopVisible"
        :show-footer="true"
        :destroy-on-close="true"
        @confirm="stopConfirm"
      >
        <div class="stop-content">
          <div class="mar-b-10">
            您是否确定将优惠券状态变更“停用”？停用后优惠券将停止发货或领取，已领取用户不受影响。
          </div>
          <div class="font-size-base font-light-color">禁用后对应发放优惠券将停止，符合条件也不发放</div>
        </div>
    </yl-dialog>
    <!-- 作废弹框弹框 -->
    <yl-dialog
        title="作废"
        :visible.sync="cancellationVisible"
        :show-footer="true"
        :destroy-on-close="true"
        @confirm="cancellationConfirm"
      >
        <div class="stop-content">
          <div class="mar-b-10">
            您是否确定将优惠券状态变更“作废”？作废后所有优惠券将全部作废。
          </div>
          <div class="font-size-base font-light-color">作废后未领取、已领取、未使用优惠券全部禁止使用</div>
        </div>
    </yl-dialog>
  </div>
</template>

<script>
import { createDownLoad } from '@/subject/admin/api/common'
import { 
  autoGetQueryListPage, 
  autoGetQueryAutoGiveListPage,
  couponActivityAutoGetEnable,
  couponActivityAutoGetStop,
  couponActivityAutoGetScrap
  } from '@/subject/admin/api/b2b_api/discount_coupon'
import { activityStatusArray, getTypeArray } from '../discount_coupon_dict'

export default {
  name: 'SelfGetList',
  components: {
  },
  computed: {
    // 优惠券状态
    activityStatusArray() {
      return activityStatusArray()
    },
    // 发放方式
    getTypeArray() {
      return getTypeArray()
    }
  },
  data() {
    return {
      typeArray: [
        {
          label: '订单累计金额',
          value: 1
        },
        {
          label: '会员自动发券',
          value: 2
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
        },
        {
          label: '废弃',
          value: 3
        }
      ],
      loading: false,
      query: {
        page: 1,
        limit: 10,
        total: 0,
        name: '',
        id: '',
        time: [],
        activityStatus: 0,
        status: 0
      },
      dataList: [],
      // 已经发放数量
      loading2: false,
      sendNumDialog: false,
      sendNumList: [],
      sendNumTotal: 0,
      sendNumQuery: {
        page: 1,
        limit: 10
      },
      // 停用弹框
      currentOperationRow: {},
      stopVisible: false,
      cancellationVisible: false
    };
  },
  activated() {
    this.getList()
  },
  methods: {
    async getList() {
      this.loading = true
      let query = this.query
      let data = await autoGetQueryListPage(
        query.page,
        query.limit,
        query.name,
        query.id,
        query.time && query.time.length ? query.time[0] : undefined,
        query.time && query.time.length > 1 ? query.time[1] : undefined,
        query.activityStatus,
        query.status
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
        id: '',
        time: [],
        activityStatus: 0,
        status: 0
      }
    },
    //跳转详情界面 // 查看 operationType: 1-查看 2-修改 3-新增
    showDetail(row) {
      this.$router.push({
        name: 'SelfGetEdit',
        params: { 
          id: row.id,
          operationType: 1
        }
      });
    },
    // 添加自动发券
    addClick() {
      this.$router.push({
        name: 'SelfGetEdit',
        params: { 
          operationType: 3
        }
      });
    },
    editClick(row) {
      this.$router.push({
        name: 'SelfGetEdit',
        params: { 
          id: row.id,
          operationType: 2
        }
      });
    },
    // 已经发放数量点击
    sendNumClick(row) {

      if (row.receivedNum && row.receivedNum > 0) {
        this.sendNumDialog = true
        this.currentRow = row
        this.getSendList()
      } else {
        this.$common.warn('暂无领取数量')
      }
      
    },
    // 已经发放列表
    async getSendList() {
      this.loading2 = true
      let sendNumQuery = this.sendNumQuery
      let data = await autoGetQueryAutoGiveListPage(
        sendNumQuery.page,
        sendNumQuery.limit,
        this.currentRow.id
      );
      this.loading2 = false
      if (data) {
        this.sendNumList = data.records
        this.sendNumTotal = data.total
      }
    },
    // 启用点击
    async enableClick(row) {
      this.$common.showLoad()
      let data = await couponActivityAutoGetEnable(row.id)
      this.$common.hideLoad()
      if (typeof data != 'undefined') {
        this.$common.n_success('启用成功')
        this.getList()
      }
    },
    // 停用点击
    stopClick(row) {
      this.currentOperationRow = row
      this.stopVisible = true
    },
    async stopConfirm() {
      this.$common.showLoad()
      let data = await couponActivityAutoGetStop(this.currentOperationRow.id)
      this.$common.hideLoad()
      if (typeof data != 'undefined') {
        this.$common.n_success('停用成功')
        this.stopVisible = false
        this.getList()
      }
    },
    // 作废
    cancellationClick(row) {
      this.currentOperationRow = row
      this.cancellationVisible = true
    },
    async cancellationConfirm() {
      this.$common.showLoad()
      let data = await couponActivityAutoGetScrap(this.currentOperationRow.id)
      this.$common.hideLoad()
      if (typeof data != 'undefined') {
        this.$common.n_success('作废成功')
        this.cancellationVisible = false
        this.getList()
      }
    },
    // 发放失败
    sendFailClick(row) {
      this.$router.push({
        name: 'SendFailList',
        params: {
          autoGiveId: row.id
        }
      });
    },
    // 导出已发放优惠券
    async downLoadTemp() {
      this.$common.showLoad()
      let data = await createDownLoad({
        className: 'couponActivityAutoHasGetExportService',
        fileName: '自动领取优惠券已领取导出',
        groupName: '自动领取优惠券',
        menuName: '优惠券管理-自动领取优惠券',
        searchConditionList: [
          {
            desc: '优惠券活动id',
            name: 'couponActivityAutoId',
            value: this.currentRow.id || ''
          }
        ]
      })
      this.$common.hideLoad()
      if (data && data.result) {
        this.$common.n_success('创建下载任务成功，请在下载中心查看')
      }
    },
    getCellClass(row) {
      if (row.columnIndex == 3) {
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
