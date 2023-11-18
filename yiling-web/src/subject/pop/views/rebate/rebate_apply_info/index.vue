<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <!-- 内容区 -->
    <div class="app-container-content has-bottom-bar">
      <!-- 企业信息  -->
      <div class="top-box">
        <div class="flex-row-left item">
          <div class="line-view"></div>
          <span class="font-size-lg bold">{{ data.name || '- -' }}</span>
        </div>
        <el-row>
          <el-col :span="8"><div class="font-size-base font-title-color item-text">渠道类型：<span class="font-important-color">{{ data.channelId | dictLabel(channelType) }}</span></div></el-col>
          <el-col :span="8"><div class="font-size-base font-title-color item-text">企业状态：<span :class="[data.status == '1' ? 'enable' : 'disable']">{{ data.status | enable }}</span></div></el-col>
          <el-col :span="8"><div class="font-size-base font-title-color item-text">申请金额：<span class="font-important-color">{{ data.totalAmount }}</span></div></el-col>
        </el-row>
        <el-row>
          <el-col :span="8"><div class="font-size-base font-title-color item-text">企业编码：<span class="font-important-color">{{ data.easCode || '- -' }}</span></div></el-col>
          <el-col :span="8"><div class="font-size-base font-title-color item-text">入账申请单号：<span class="font-important-color">{{ data.code || '- - ' }}</span></div></el-col>
          <el-col :span="8"><div class="font-size-base font-title-color item-text">申请状态：<span class="font-important-color">{{ data.applyStatus | dictLabel(agreementApplyStatus) }}</span></div></el-col>
        </el-row>
        <el-row>
          <el-col :span="8"><div class="font-size-base font-title-color item-text">入账申请时间：<span class="font-important-color">{{ data.createTime | formatDate }}</span></div></el-col>
        </el-row>
      </div>
      <el-tabs
        class="my-tabs"
        v-model="activeTab"
        @tab-click="handleTabClick"
      >
        <el-tab-pane label="入账明细" name="1"></el-tab-pane>
        <el-tab-pane label="协议订单商品明细" name="2"></el-tab-pane>
      </el-tabs>
      <!-- 底部列表 -->
      <div class="mar-t-8 pad-b-10 order-table-view">
        <yl-table
          v-if="activeTab == 1"
          border
          stripe
          show-header
          :list="dataList"
          :total="query.total"
          :page.sync="query.page"
          :limit.sync="query.limit"
          :loading="loading"
          @getList="getList">
          <el-table-column label="入账类型" min-width="60" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.detailType == 1 ? '协议' : '其它返利' }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="入账原因" min-width="250" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.detailType == 1 ? row.name : row.entryDescribe }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="协议内容" min-width="250" align="center">
            <template slot-scope="{ row }">
              <div>{{ row.content || '- -' }}</div>
            </template>
          </el-table-column>
          <el-table-column label="订单数量" min-width="100" align="center" prop="orderCount"></el-table-column>
          <el-table-column label="返利金额" min-width="100" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.amount }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="发货组织" min-width="100" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.sellerName }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="申请时间" min-width="100" align="center">
            <template slot-scope="{ row }">
              <div>{{ row.applyTime || '- -' }}</div>
            </template>
          </el-table-column>
          <el-table-column label="返利种类" min-width="100" align="center">
            <template slot-scope="{ row }">
              <div>{{ row.rebateCategory }}</div>
            </template>
          </el-table-column>
          <el-table-column label="费用科目" min-width="100" align="center">
            <template slot-scope="{ row }">
              <div>{{ row.costSubject }}</div>
            </template>
          </el-table-column>
          <el-table-column label="费用归属部门" min-width="100" align="center">
            <template slot-scope="{ row }">
              <div>{{ row.costDept }}</div>
            </template>
          </el-table-column>
          <el-table-column label="执行部门" min-width="100" align="center">
            <template slot-scope="{ row }">
              <div>{{ row.executeDept }}</div>
            </template>
          </el-table-column>
          <el-table-column label="批复代码" min-width="100" align="center">
            <template slot-scope="{ row }">
              <div>{{ row.replyCode }}</div>
            </template>
          </el-table-column>
          <el-table-column key="isEdit" label="操作" min-width="100" align="center" v-if="isEdit && activeTab == 1">
            <template slot-scope="{ row }">
              <div v-if="row.detailType == 2 && data.applyStatus == 3"><yl-button v-role-btn="['5']" type="text" @click="editClick(row)">修改</yl-button></div>
            </template>
          </el-table-column>
        </yl-table>
        
        <yl-table
          v-if="activeTab == 2"
          border
          stripe
          show-header
          :list="dataList"
          :total="query.total"
          :page.sync="query.page"
          :limit.sync="query.limit"
          :loading="loading"
          @getList="getList">
          <el-table-column label="协议ID" min-width="60" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.id }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="协议名称" min-width="250" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.name }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="协议内容" min-width="250" align="center">
            <template slot-scope="{ row }">
              <div>{{ row.content }}</div>
            </template>
          </el-table-column>
          <el-table-column label="订单号" min-width="100" align="center" prop="orderCode"></el-table-column>
          <el-table-column label="商品ID" min-width="100" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.goodsId }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="商品名称" min-width="100" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.goodsName }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="商品erp内码" min-width="100" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.erpCode }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="成交数量" min-width="100" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.goodsQuantity }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="采购金额" min-width="100" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.price }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="返利金额" min-width="100" align="center" fixed="right">
            <template slot-scope="{ row }">
              <div>
                {{ row.discountAmount }}
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
      <div class="flex-row-center bottom-view">
        <yl-button plain type="primary" @click="$router.go(-1)">返回</yl-button>
        <yl-button v-role-btn="['6']" plain type="primary" v-show="isAudit" @click="auditClick(1)">审核不通过</yl-button>
        <yl-button v-role-btn="['6']" type="primary" v-show="isAudit" @click="auditClick(2)">审核通过</yl-button>
      </div>
    </div>
    <!-- 审核 -->
    <yl-dialog
      title="不通过原因"
      :visible.sync="showAuditDialog"
      :destroy-on-close="false"
      @confirm="confirm"
      >
      <div class="order-audit-content">
        <el-input
          type="textarea"
          :autosize="{ minRows: 3 }"
          placeholder="请输入不通过原因"
          v-model="auditRemark">
        </el-input>
      </div>
    </yl-dialog>
    <!-- 修改其他返利 -->
    <yl-dialog title="修改其他返利" :visible.sync="showEditDialog" @confirm="editConfirm">
        <div class="Eas-content-dialog">
          <div class="mar-t-10" style="padding-bottom: 10px;background: #FFFFFF;">
            <el-form :model="form" :rules="rules" ref="dataForm" label-width="120px" class="demo-ruleForm">
              <el-form-item label="返利金额" prop="amount">
                <el-input v-model="form.amount" @keyup.native="form.amount = onInput(form.amount)" placeholder="请输入返利金额"></el-input>
              </el-form-item>
              <el-form-item label="返利原因" prop="entryDescribe">
                <el-input v-model="form.entryDescribe" :maxlength="20" show-word-limit placeholder="请输入返利原因"></el-input>
              </el-form-item>
            </el-form>
          </div>
        </div>
      </yl-dialog>
  </div>
</template>

<script>
import {
  queryRebateApplyPageList,
  queryRebateApplyOrderDetailPageList,
  auditApply,
  editApplyDetail
} from '@/subject/pop/api/rebate';
import { channelType, agreementApplyStatus } from '@/subject/pop/utils/busi'
import { onInputLimit } from '@/common/utils'

export default {
  components: {
  },
  computed: {
    channelType() {
      return channelType();
    },
    // 退货单审核状态
    agreementApplyStatus() {
      return agreementApplyStatus()
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
          title: '申请入账返利信息'
        }
      ],
      loading: false,
      query: {
        page: 1,
        limit: 10,
        total: 0
      },
      data: {},
      dataList: [],
      activeTab: '1',
      isAudit: false,
      showAuditDialog: false,
      // 审核 原因
      auditRemark: '',
      // 修改
      isEdit: false,
      showEditDialog: false,
      form: {},
      rules: {
        amount: [{ required: true, message: '请输入返利金额', trigger: 'blur' }],
        entryDescribe: [{ required: true, message: '请输入返利原因', trigger: 'blur' }]
      }
    };
  },
  mounted() {
    this.id = this.$route.params.id
    if (this.$route.params.isAudit && this.$route.params.isAudit == 'true') {
      this.isAudit = true
    }
    if (this.$route.params.isEdit && this.$route.params.isEdit == 'true') {
      this.isEdit = true
    }
    if (this.id) {
      this.getList()
    }
  },
  methods: {
    async getList() {
      this.loading = true
      let query = this.query
      let data = null
      if (this.activeTab == 1) {
        data = await queryRebateApplyPageList(
          query.page,
          query.limit,
          this.id
        )
      } else {
        data = await queryRebateApplyOrderDetailPageList(
          query.page,
          query.limit,
          this.id
        )
      }
      this.loading = false
      if (data) {
        this.data = data
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
        limit: 10
      }
    },
    //tab切换
    handleTabClick(tab, event) {
      this.dataList = []
      this.query = {
        page: 1,
        limit: 10
      }
      this.getList()
    },
    // 审核 1-审核不通过 2-审核通过
    auditClick(type) {
      this.auditRemark = ''
      if (type == 1) {
        this.showAuditDialog = true
      } else {
        this.auditApplyMethod(2)
      }
      
    },
    // 审核不通过 确认点击
    confirm() {
      this.auditApplyMethod(3)
    },
    // type 状态 2-已入账 3-驳回
    async auditApplyMethod(type) {
      if (type == 3 && this.auditRemark.trim() == '') {
        this.$common.warn('请输入不通过原因')
        return
      }
      this.$common.showLoad();
      let data = await auditApply(this.id, type, this.auditRemark);
      this.$common.hideLoad();
      if (data && data.result) {
        this.$common.n_success('提交成功');
        this.$router.go(-1)
      }
    },
    // 修改点击
    editClick(row) {
      this.form = {}
      this.showEditDialog = true
      this.form = {
        amount: row.amount,
        entryDescribe: row.entryDescribe,
        id: row.id
      }
    },
    editConfirm() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          let form = this.form;
          this.$common.showLoad()
          let data = await editApplyDetail(
            this.id,
            form.id,
            form.amount,
            form.entryDescribe
          );
          this.$common.hideLoad()
          if (data && data.result) {
            this.$common.n_success('修改成功')
            this.showEditDialog = false
            this.$router.go(-1)
          }
        } else {
          return false
        }
      })
    },
    onInput(value) {
      return onInputLimit(value, 2)
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
