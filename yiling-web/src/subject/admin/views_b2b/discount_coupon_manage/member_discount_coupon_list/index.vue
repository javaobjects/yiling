<template>
  <div class="app-container">
    <!-- 内容区 -->
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">优惠券名称</div>
              <el-input v-model="query.name" placeholder="请输入优惠券名称" @keyup.enter.native="handleSearch" />
            </el-col>
            <el-col :span="6">
              <div class="title">优惠券ID</div>
              <el-input v-model="query.id" placeholder="请输入优惠券ID" @keyup.enter.native="handleSearch" />
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
              <div class="title">活动类型</div>
              <el-select v-model="query.sponsorType" placeholder="请选择活动类型">
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
              <div class="title">优惠券状态</div>
              <el-select v-model="query.status" placeholder="请选择优惠券状态">
                <el-option label="全部" :value="0"></el-option>
                <el-option
                  v-for="item in statusArray"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-col>
            <el-col :span="12">
              <div class="title">预算编号</div>
              <el-input v-model="query.budgetCode" placeholder="请输入预算编号" @keyup.enter.native="handleSearch" />
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">运营备注</div>
              <el-input v-model="query.remark" placeholder="请输入运营备注" @keyup.enter.native="handleSearch" />
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
        <div class="btn">
          <ylButton v-role-btn="['1']" type="primary" plain @click="importClick">excel导入发券</ylButton>
        </div>
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
          <el-table-column label-class-name="mar-l-16" label="优惠券信息" min-width="350" align="left">
            <template slot-scope="{ row }">
              <div class="item text-l mar-l-16">
                <div class="title font-size-lg bold">{{ row.name }} <span class="status-type" :class="[row.status == 1 ? 'active' : '', row.status == 2 ? 'stop' : '', row.status == 3 ? 'cancellation' : '']">{{ row.status | dictLabel(statusArray) }}</span></div>
                <div class="item-text font-size-base font-title-color"><span>优惠券ID：</span>{{ row.id }}</div>
                <div class="multi-text font-size-base font-title-color"><span>适用会员方案：</span>{{ row.memberLimit == 1 ? '全部' : '部分' }}</div>
                <div class="item-text font-size-base font-title-color"><span>优惠规则：</span>{{ row.couponRules }}</div>
                <div class="item-text font-size-base font-title-color"></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="支付信息" min-width="300" align="left">
            <template slot-scope="{ row }">
              <div class="item">
                <div class="title"></div>
                <div class="item-text font-size-base font-title-color"><span>活动类型：</span>{{ row.sponsorType | dictLabel(sponsorTypeArray) }}</div>
                <div class="item-text font-size-base font-title-color"><span>预算编号：</span>{{ row.budgetCode }}</div>
                <div v-if="row.useDateType == 1" class="item-text font-size-base font-title-color"><span>使用时间：</span>{{ row.beginTime | formatDate }} - {{ row.endTime | formatDate }}</div>
                <div v-else-if="row.useDateType == 2" class="item-text font-size-base font-title-color"><span>使用时间：</span>{{ row.giveOutEffectiveRules }}</div>
                <div class="item-text font-size-base font-title-color"></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="优惠券使用状况" min-width="220" align="left">
            <template slot-scope="{ row }">
              <div class="item">
                <div class="title"></div>
                <div class="item-text font-size-base font-title-color"><span>优惠券数量：</span>{{ row.totalCount }}</div>
                <div class="item-text font-size-base font-title-color"><span>已发放数量：</span><yl-button class="detail-btn" type="text" @click="sendNumClick(row)">{{ row.giveCount }}</yl-button></div>
                <div class="item-text font-size-base font-title-color"><span>已使用数量：</span><yl-button class="detail-btn" type="text" @click="usedNumClick(row)">{{ row.useCount }}</yl-button></div>
                <div class="item-text font-size-base font-title-color"><span></span></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="商品信息" min-width="210" align="left">
            <template slot-scope="{ row }">
              <div class="item">
                <div class="title"></div>
                <div class="item-text font-size-base font-title-color"><span>创建人：</span>{{ row.createUserName || '- -' }}</div>
                <div class="item-text font-size-base font-title-color"><span>创建时间：</span>{{ row.createTime | formatDate }}</div>
                <div class="item-text font-size-base font-title-color"><span>修改时间：</span>{{ row.updateTime | formatDate }}</div>
                <div class="item-text font-size-base font-title-color remark"><span>运营备注：</span>{{ row.remark || '- -' }}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="操作" min-width="150" align="left">
            <template slot-scope="{ row }">
              <div class="operation-view">
                <div class="option">
                  <yl-button type="text" @click="showDetail(row)">查看</yl-button>
                  <yl-button type="text" :disabled="!row.updateFlag" @click="editClick(row)">修改</yl-button>
                  <yl-button type="text" :disabled="!row.increaseFlag" @click="addCouponClick(row)">增券</yl-button>
                </div>
                <div class="option">
                  <yl-button type="text" :disabled="!row.giveFlag" @click="sendClick(row)">发放</yl-button>
                  <yl-button type="text" :disabled="!row.copyFlag" @click="copyClick(row)">复制</yl-button>
                  <yl-button type="text" :disabled="!row.stopFlag" @click="stopClick(row)">停用</yl-button>
                </div>
                <div class="option">
                  <yl-button type="text" :disabled="!row.scrapFlag" @click="cancellationClick(row)">作废</yl-button>
                </div>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
    <!-- 已发放数量 -->
    <yl-dialog title="已发放数量明细" :visible.sync="sendNumDialog" :show-footer="false">
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
            <el-table-column label="优惠规则" min-width="200" align="center" prop="couponRules">
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
            <el-table-column label="使用状态" min-width="100" align="center">
              <template slot-scope="{ row }">
                <div>{{ row.usedStatus == 1 ? '未使用' : '已使用' }}</div>
              </template>
            </el-table-column>
            <el-table-column label="发放时间" min-width="200" align="center">
              <template slot-scope="{ row }">
                <div>{{ row.getTime | formatDate }}</div>
              </template>
            </el-table-column>
            <el-table-column label="有效期" min-width="200" align="center">
              <template slot-scope="{ row }">
                <div>{{ row.effectiveTime }}</div>
              </template>
            </el-table-column>
            <el-table-column label="是否作废" min-width="100" align="center">
              <template slot-scope="{ row }">
                <div>{{ row.status == 1 ? '正常' : '废弃' }}</div>
              </template>
            </el-table-column>
            <el-table-column label="发放人" min-width="120" align="center" prop="getUserName">
            </el-table-column>
          </yl-table>
        </div>
      </div>
    </yl-dialog>
    <!-- 已使用数量 -->
    <yl-dialog title="已使用优惠券查看" :visible.sync="usedNumDialog" :show-footer="false">
      <div class="dialog-content-box">
        <div>
          <ylButton type="primary" plain @click="downLoadUsedTemp">导出</ylButton>
        </div>
        <div class="mar-t-10" style="padding-bottom: 10px;background: #FFFFFF;">
          <yl-table
            border
            show-header
            :list="usedNumList"
            :total="usedNumTotal"
            :page.sync="usedNumQuery.page"
            :limit.sync="sendNumQuery.limit"
            :loading="loading3"
            @getList="getUsedList"
          >
            <el-table-column label="优惠券ID" min-width="120" align="center" prop="couponId">
            </el-table-column>
            <el-table-column label="优惠规则" min-width="100" align="center" prop="couponRules">
            </el-table-column>
            <el-table-column label="获券企业ID" min-width="100" align="center" prop="eid">
            </el-table-column>
            <el-table-column label="获券企业名称" min-width="120" align="center" prop="ename">
            </el-table-column>
            <el-table-column label="获取方式" min-width="100" align="center">
              <template slot-scope="{ row }">
                <div>{{ row.getType | dictLabel(getTypeArray) }}</div>
              </template>
            </el-table-column>
            <el-table-column label="使用状态" min-width="100" align="center">
              <template slot-scope="{ row }">
                <div>{{ row.usedStatus == 1 ? '未使用' : '已使用' }}</div>
              </template>
            </el-table-column>
            <el-table-column label="获券时间" min-width="200" align="center">
              <template slot-scope="{ row }">
                <div>{{ row.getTime | formatDate }}</div>
              </template>
            </el-table-column>
            <el-table-column label="使用时间" min-width="200" align="center">
              <template slot-scope="{ row }">
                <div>{{ row.useTime | formatDate }}</div>
              </template>
            </el-table-column>
            <el-table-column label="购买会员方案" min-width="150" align="center" prop="memberName">
            </el-table-column>
            <el-table-column label="会员时长" min-width="100" align="center">
              <template slot-scope="{ row }">
                <div>{{ row.validTime }}天</div>
              </template>
            </el-table-column>
            <el-table-column label="优惠金额" min-width="100" align="center" prop="discountAmount">
            </el-table-column>
            <el-table-column label="实付金额" min-width="120" align="center" prop="payAmount">
            </el-table-column>
          </yl-table>
        </div>
      </div>
    </yl-dialog>
    <!-- 增加生成优惠券 -->
    <yl-dialog title="增加生成优惠券" :visible.sync="addCouponDialog" width="600px"
      @confirm="addCouponConfirm"
    >
      <div class="add-dialog-content-box">
        <div style="padding-bottom: 10px;background: #FFFFFF;margin-top: 20px;">
          <el-form :model="addForm" :rules="addRules" ref="dataForm" label-position="left" label-width="120px">
            <el-form-item label="新增生券数量" prop="quantity">
              <el-input v-model="addForm.quantity" @keyup.native="addForm.quantity = onInput(addForm.quantity, 0)"></el-input>
            </el-form-item>
            <div class="add-dialog font-light-color">新增生券数量为正整数，将增加对应数量的优惠券</div>
            <el-form-item label="增券备注">
              <el-input v-model="addForm.remark" maxlength="20" show-word-limit></el-input>
            </el-form-item>
          </el-form>  
        </div>
      </div>
    </yl-dialog>
    <!-- 停用弹框 -->
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
    <!-- 作废弹框 -->
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
    <!-- 发放弹框 -->
    <add-send-dialog v-if="addSendVisible" ref="addSendRef" @sendSuccess="addSendSaveClick"></add-send-dialog>
    <!-- excel导入发券 -->
    <import-send-dialog :visible.sync="importSendVisible" :excel-code="info.excelCode" ref="importSendRef"></import-send-dialog>
  </div>
</template>

<script>
import { createDownLoad } from '@/subject/admin/api/common'
import { memberCouponActivityQueryListPage, memberCouponActivityCopy, memberCouponActivityIncrease, memberCouponActivityScrap, memberCouponActivityStop, memberCouponQueryGiveListPage, memberCouponQueryUseListPage } from '@/subject/admin/api/b2b_api/discount_coupon'
import { statusArray, couponTypeArray, getTypeArray } from '../discount_coupon_dict'
import { onInputLimit } from '@/common/utils'
import AddSendDialog from '../components/add_send_dialog'
import ImportSendDialog from '@/subject/admin/components/ImportSendDialog'

export default {
  name: 'MemberdiscountCouponList',
  components: {
    AddSendDialog,
    ImportSendDialog
  },
  computed: {
    // 优惠券状态
    statusArray() {
      return statusArray()
    },
    couponTypeArray() {
      return couponTypeArray()
    },
    // 发放方式
    getTypeArray() {
      return getTypeArray()
    }
  },
  data() {
    return {
      // 发放弹框
      addSendVisible: false,
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
      currentOperationRow: {},
      loading: false,
      query: {
        page: 1,
        limit: 10,
        total: 0,
        name: '',
        id: '',
        time: [],
        sponsorType: 0,
        budgetCode: '',
        status: 0,
        // 运营备注
        remark: ''
      },
      dataList: [],
      // 已经发放数量
      sendNumDialog: false,
      loading2: false,
      sendNumList: [],
      sendNumTotal: 0,
      sendNumQuery: {
        page: 1,
        limit: 10
      },
      // 已经使用数量
      usedNumDialog: false,
      loading3: false,
      usedNumList: [],
      usedNumTotal: 0,
      usedNumQuery: {
        page: 1,
        limit: 10
      },
      // 增券
      addCouponDialog: false,
      addForm: {
        quantity: '',
        remark: ''
      },
      addRules: {
        quantity: [{ required: true, message: '新增生券数量', trigger: 'blur' }]
      },
      // 停用弹框
      stopVisible: false,
      cancellationVisible: false,
      currentRow: {},
      // 导入发券
      importSendVisible: false,
      // 导入任务参数 excelCode: sendMemberCoupon-商品优惠券 
      info: {
        excelCode: 'sendMemberCoupon'
      }
    };
  },
  activated() {
    this.getList()
  },
  methods: {
    async getList() {
      this.loading = true
      let query = this.query
      let data = await memberCouponActivityQueryListPage(
        query.page,
        query.limit,
        query.name,
        query.id,
        query.time && query.time.length ? query.time[0] : undefined,
        query.time && query.time.length > 1 ? query.time[1] : undefined,
        query.sponsorType,
        query.budgetCode,
        query.status,
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
        id: '',
        time: [],
        sponsorType: 0,
        budgetCode: '',
        status: 0,
        remark: ''
      }
    },
    // 添加优惠券
    addClick() {
      this.$router.push({
        name: 'MemberdiscountCouponEdit',
        params: {
          operationType: 3
        }
      });
    },
    // 查看 operationType: 1-查看 2-修改 3-新增 4-复制
    showDetail(row) {
      // 跳转详情
      this.$router.push({
        name: 'MemberdiscountCouponEdit',
        params: { 
          id: row.id,
          operationType: 1
        }
      });
    },
    // 修改
    editClick(row) {
      this.$router.push({
        name: 'MemberdiscountCouponEdit',
        params: { 
          id: row.id,
          operationType: 2
        }
      });
    },
    // 增券数量
    addCouponClick(row) {
      this.currentOperationRow = row
      this.addCouponDialog = true
    },
    addCouponConfirm() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          
          let addForm = this.addForm
          addForm.id = this.currentOperationRow.id
          
          this.$common.showLoad()
          let data = await memberCouponActivityIncrease(addForm.id, addForm.quantity, addForm.remark)
          this.$common.hideLoad()
          if (typeof data != 'undefined') {
            this.$common.n_success('增券成功');
            this.addCouponDialog = false
            this.getList()
          } 
        } else {
          console.log('error submit!!');
          return false;
        }
      })
    },
    // 发放
    sendClick(row) {
      if (!row.id) {
        this.$common.warn('暂无优惠券活动ID')
        return
      }
      this.addSendVisible = true
      this.$nextTick( () => {
        this.$refs.addSendRef.init(row.id)
      })
    },
    addSendSaveClick() {
      this.addSendVisible = false
      this.getList()
    },
    // 复制 1-查看 2-修改 3-新增 4-复制
    async copyClick(row) {
      this.$common.showLoad()
      let data = await memberCouponActivityCopy(row.id)
      this.$common.hideLoad()
      if (data) {
        this.$common.n_success('复制成功');
        // 进入修改
        this.$router.push({
          name: 'MemberdiscountCouponEdit',
          params: { 
            id: data,
            operationType: 4
          }
        });
      } 
    },
    // 已经发放数量点击
    sendNumClick(row) {
      if (row.giveCount && row.giveCount > 0) {
        this.sendNumDialog = true
        this.currentRow = row
        this.getSendList()
      } else {
        this.$common.warn('暂无发放数量')
      }
    },
    // 已经发放列表
    async getSendList() {
      this.loading2 = true
      let sendNumQuery = this.sendNumQuery
      let data = await memberCouponQueryGiveListPage(
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
    // 已经使用列表
    usedNumClick(row) {
      if (row.useCount && row.useCount > 0) {
        this.usedNumDialog = true
        this.currentRow = row
        this.getUsedList()
      } else {
        this.$common.warn('暂无使用数量')
      }
    },
    // 已经使用列表
    async getUsedList() {
      this.loading3 = true
      let usedNumQuery = this.usedNumQuery
      let data = await memberCouponQueryUseListPage(
        usedNumQuery.page,
        usedNumQuery.limit,
        this.currentRow.id
      );
      this.loading3 = false
      if (data) {
        this.usedNumList = data.records
        this.usedNumTotal = data.total
      }
    },
    // 停用点击
    stopClick(row) {
      this.currentOperationRow = row
      this.stopVisible = true
    },
    async stopConfirm() {
      this.$common.showLoad()
      let data = await memberCouponActivityStop(this.currentOperationRow.id)
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
      let data = await memberCouponActivityScrap(this.currentOperationRow.id)
      this.$common.hideLoad()
      if (typeof data != 'undefined') {
        this.$common.n_success('作废成功')
        this.cancellationVisible = false
        this.getList()
      }
    },
    // 导出已发放优惠券
    async downLoadTemp() {
      this.$common.showLoad()
      let data = await createDownLoad({
        className: 'couponActivityHasGiveExportService',
        fileName: '优惠券活动已发放导出',
        groupName: '优惠券活动',
        menuName: '优惠券管理-优惠券活动',
        searchConditionList: [
          {
            desc: '优惠券活动id',
            name: 'couponActivityId',
            value: this.currentRow.id || ''
          }
        ]
      })
      this.$common.hideLoad()
      if (data && data.result) {
        this.$common.n_success('创建下载任务成功，请在下载中心查看')
      }
    },
    // 导出已使用优惠券
    async downLoadUsedTemp() {
      this.$common.showLoad()
      let data = await createDownLoad({
        className: 'MemberCouponUsedInfoExportService',
        fileName: '会员优惠券已使用导出',
        groupName: '优惠券管理',
        menuName: '优惠券管理-会员优惠券',
        searchConditionList: [
          {
            desc: '优惠券活动id',
            name: 'couponActivityId',
            value: this.currentRow.id || ''
          }
        ]
      })
      this.$common.hideLoad()
      if (data && data.result) {
        this.$common.n_success('创建下载任务成功，请在下载中心查看')
      }
    },
    // excel导入发券
    importClick() {
      this.importSendVisible = true
      this.$nextTick( () => {
        this.$refs.importSendRef.init()
      })
    },
    // 校验两位小数
    onInput(value, limit) {
      return onInputLimit(value, limit)
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
