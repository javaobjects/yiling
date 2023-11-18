<template>
  <div class="app-container">
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">终端ID</div>
              <el-input v-model="query.eid" @keyup.enter.native="searchEnter" placeholder="请输入终端ID" />
            </el-col>
            <el-col :span="8">
              <div class="title">终端名称</div>
              <el-input v-model="query.ename" @keyup.enter.native="searchEnter" placeholder="请输入终端名称" />
            </el-col>
            <el-col :span="8">
              <div class="title">是否过期</div>
              <el-select class="select-width" v-model="query.expire" placeholder="请选择是否过期">
                <el-option v-for="item in expireFlag" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">推广方名称</div>
              <el-input v-model="query.promoterName" @keyup.enter.native="searchEnter" placeholder="请输入推广方名称" />
            </el-col>
            <el-col :span="8">
              <div class="title">推广方ID</div>
              <el-input v-model="query.promoterId" @keyup.enter.native="searchEnter" placeholder="请输入推广方ID" />
            </el-col>
            <el-col :span="8">
              <div class="title">推广人名称</div>
              <el-input v-model="query.promoterUserName" @keyup.enter.native="searchEnter" placeholder="请输入推广人名称" />
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">推广人ID</div>
              <el-input v-model="query.promoterUserId" @keyup.enter.native="searchEnter" placeholder="请输入推广人ID" />
            </el-col>
            <el-col :span="8">
              <div class="title">开通类型</div>
              <el-select class="select-width" v-model="query.openStatus" placeholder="请选择开通类型">
                <el-option label="全部" :value="0"></el-option>
                <el-option
                  v-for="item in memberOpenType"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="8">
              <div class="title">购买时间</div>
              <el-date-picker
                v-model="query.time"
                type="daterange"
                format="yyyy 年 MM 月 dd 日"
                value-format="yyyy-MM-dd"
                range-separator="至"
                start-placeholder="开始月份"
                end-placeholder="结束月份">
              </el-date-picker>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">是否退款</div>
              <el-select class="select-width" v-model="query.refund" placeholder="请选择是否退款">
                <el-option v-for="item in refundFlag" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="8">
              <div class="title">终端地址</div>
              <yl-choose-address
                width="230px"
                :province.sync="query.provinceCode"
                :city.sync="query.cityCode"
                :area.sync="query.regionCode"
                is-all/>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="24">
              <div class="title">推广方地址</div>
              <yl-choose-address
                width="230px"
                :province.sync="query.promoterProvinceCode"
                :city.sync="query.promoterCityCode"
                :area.sync="query.promoterRegionCode"
                is-all/>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="24">
              <div class="title">数据来源</div>
              <el-checkbox-group v-model="query.sourceList" size="mini">
                <el-checkbox v-for="item in memberDataSource" :key="item.value" :label="item.value">
                  {{ item.label }}
                </el-checkbox>
              </el-checkbox-group>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="24">
              <div class="title">会员卡名称</div>
              <el-checkbox-group v-model="query.memberIdList">
                <el-checkbox 
                  class="option-class" 
                  v-for="item in tagList" 
                  :key="item.id" 
                  :label="item.id" >
                  {{ item.name }}(ID：{{ item.id }})
                </el-checkbox>
              </el-checkbox-group >
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
          <yl-button type="primary" plain @click="downLoadTemp">导出查询结果</yl-button>
        </div>
      </div>

      <div class="mar-t-8">
        <yl-table
          border
          :show-header="true"
          :list="dataList"
          :total="query.total"
          :page.sync="query.page"
          :limit.sync="query.limit"
          :loading="loading"
          @getList="getList">
          <el-table-column align="center" min-width="140" label="订单ID" prop="orderNo">
          </el-table-column>
          <el-table-column align="center" min-width="150" label="会员名称">
            <template slot-scope="{ row }">
              {{ row.memberName }}<span>（ID：{{ row.memberId }}）</span>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="90" label="终端ID" prop="eid">
          </el-table-column>
          <el-table-column align="center" min-width="120" label="终端名称" prop="ename">
          </el-table-column>
          <el-table-column align="center" min-width="120" label="终端地址" prop="address">
          </el-table-column>
          <el-table-column align="center" min-width="120" label="企业管理员手机号" prop="contactorPhone">
          </el-table-column>
          <el-table-column align="center" min-width="120" label="会员购买规则" prop="buyRule">
          </el-table-column>
          <el-table-column align="center" min-width="120" label="购买时间">
            <template slot-scope="{ row }">
              <span>{{ row.createTime | formatDate }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="120" label="推广方ID">
            <template slot-scope="{ row }">
              <div>
                <span v-if="row.promoterId">{{ row.promoterId }}</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="120" label="推广方名称" prop="promoterName">
          </el-table-column>
          <el-table-column align="center" min-width="120" label="推广人ID">
            <template slot-scope="{ row }">
              <div>
                <span v-if="row.promoterUserId">{{ row.promoterUserId }}</span>
                <yl-button v-else type="text" @click="promoterIdClick(row, 2)">填写推广人</yl-button>
              </div>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="120" label="推广人名称" prop="promoterUserName">
          </el-table-column>
          <el-table-column align="center" min-width="120" label="会员起始时间">
            <template slot-scope="{ row }">
              <span>{{ row.startTime | formatDate }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="120" label="会员结束时间">
            <template slot-scope="{ row }">
              <span>{{ row.endTime | formatDate }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" label="支付方式" prop="payMethodName">
          </el-table-column>
          <el-table-column align="center" label="原价" >
            <template slot-scope="{ row }">
              <span>{{ row.originalPrice | toThousand('￥') }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" label="优惠金额">
            <template slot-scope="{ row }">
              <span>{{ row.discountAmount | toThousand('￥') }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" label="支付金额" width="120">
            <template slot-scope="{ row }">
              <span>{{ row.payAmount | toThousand('￥') }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" label="退款金额" width="120">
            <template slot-scope="{ row }">
              <span>{{ row.returnAmount | toThousand('￥', true) }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" label="是否退款" width="120">
            <template slot-scope="{ row }">
              <span>{{ row.returnFlag | dictLabel(refundFlag) }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" label="是否过期" width="120">
            <template slot-scope="{ row }">
              <span>{{ row.expireFlag | dictLabel(expireFlag) }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" label="开通类型" width="120">
            <template slot-scope="{ row }">
              <span>{{ row.openType | dictLabel(memberOpenType) }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" label="数据来源" width="120">
            <template slot-scope="{ row }">
              <span>{{ row.source | dictLabel(memberDataSource) }}</span>
            </template>
          </el-table-column>
          <el-table-column
            fixed="right"
            align="center"
            label="操作"
            width="120">
            <template slot-scope="{ row }">
              <yl-button v-if="row.showReturnFlag" type="text" @click="rebackMoney(row)">退款</yl-button>
              <yl-button type="text" @click="promoterIdClick(row, 1)">推广方ID</yl-button>
              <yl-button type="text" v-if="row.source == 4 || row.source == 5" @click="cancelClick(row)">取消记录</yl-button>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
    <!-- 弹框 -->
    <yl-dialog
      title="提示"
      :visible.sync="promoterVisible"
      :show-footer="true"
      :destroy-on-close="true"
      width="500px"
      @confirm="promoterConfirm"
      >
        <div class="stop-content">
          <div class="left-view">
            <span class="font-size-base font-title-color mar-r-8">{{ type == 1 ? '填写推广方ID' : '推广人手机号' }}</span>
            <el-input v-model="inputNum" @input="e => (inputNum = checkInputNum(e))" :placeholder="type == 1 ? '填写推广方ID' : '推广人手机号'"></el-input>
          </div>
        </div>
    </yl-dialog>

    <yl-dialog
      title="会员退款"
      :visible.sync="returnShow"
      :show-footer="true"
      width="600px"
      @confirm="returnConfirm"
    >
      <div class="return-content pad-16">
        <el-form :model="form" :rules="rules" ref="dataForm" label-width="150px">
          <el-form-item label="订单编号：" style="margin-bottom: 0;">
            <span>{{ form.orderNo }}</span>
          </el-form-item>
          <el-form-item label="终端名称：" style="margin-bottom: 0;">
            <span>{{ form.ename }}</span>
          </el-form-item>
          <el-form-item label="会员名称：" style="margin-bottom: 0;">
            <span>{{ form.memberName }}</span>
          </el-form-item>
          <el-form-item label="会员购买规则：" style="margin-bottom: 0;">
            <span>{{ form.buyRule }}</span>
          </el-form-item>
          <el-form-item label="支付金额：" style="margin-bottom: 0;">
            <span>{{ form.payAmount }}元</span>
          </el-form-item>
          <el-form-item label="支付方式：" style="margin-bottom: 0;">
            <span>{{ form.payMethodName }}</span>
          </el-form-item>
          <el-form-item label="支付时间：" style="margin-bottom: 0;">
            <span>{{ form.createTime | formatDate }}</span>
          </el-form-item>
          <el-form-item label="退款金额：" prop="returnAmount">
            <el-input type="number" v-model="form.returnAmount">
              <template slot="append">元</template>
            </el-input>
            <yl-tool-tip>退款金额不得大于实际支付金额</yl-tool-tip>
          </el-form-item>
          <el-form-item label="退款原因：" prop="returnReason">
            <el-select v-model="form.returnReason" placeholder="请选择退款原因">
              <el-option label="开通终端错误" value="开通终端错误"></el-option>
              <el-option label="误操作付款" value="误操作付款"></el-option>
              <el-option label="会员档位开通错误" value="会员档位开通错误"></el-option>
              <el-option label="其他" value="其他"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="备注：">
            <el-input
              resize="none"
              :autosize="{ minRows: 2, maxRows: 2}"
              type="textarea"
              maxlength="20"
              v-model="form.returnRemark"
              placeholder="请输入备注" />
          </el-form-item>
        </el-form>
      </div>
    </yl-dialog>
    <yl-dialog
      title="取消记录"
      :visible.sync="recordShow"
      :show-footer="true"
      width="600px"
      @confirm="recordConfirm">
        <div class="dialogTc">
          <div>
            <span class="font-size-base font-title-color mar-r-8">终端名称：</span>
            {{ record.ename }}
          </div>
          <div>
            <span class="font-size-base font-title-color mar-r-8">会员卡：</span>
            {{ record.memberName }}
          </div>
          <div>
            <span class="font-size-base font-title-color mar-r-8">购买规则：</span>
            {{ record.buyRule }}
          </div>
          <div>
            <span class="font-size-base font-title-color mar-r-8">数据来源：</span>
            {{ record.source | dictLabel(memberDataSource) }}
          </div>
          <div class="dialogTc-col">
            取消记录后终端将失去这条记录中的会员身份
          </div>
        </div>
     </yl-dialog>
  </div>
</template>

<script>
  import { ylChooseAddress } from '@/subject/admin/components'
  import { createDownLoad } from '@/subject/admin/api/common'
  import { isNumZ } from '@/subject/admin/utils/rules'
  import { 
    queryBuyRecordListPage, 
    updateBuyMemberPromoter, 
    submitMemberReturn, 
    getMemberReturnDetail, 
    cancelBuyRecord,
    getMemberList
  } from '@/subject/admin/api/b2b_api/membership'
  import { memberDataSource, memberOpenType } from '@/subject/admin/busi/b2b/menbership'
  export default {
    name: 'BuyMemberList',
    components: {
       ylChooseAddress
    },
    computed: {
      memberDataSource() {
        return memberDataSource()
      },
      memberOpenType() {
        return memberOpenType()
      }
    },
    data() {
      return {
        query: {
          page: 1,
          limit: 10,
          total: 0,
          memberName: '',
          ename: '',
          time: [],
          promoterName: '',
          provinceCode: '',
          cityCode: '',
          regionCode: '',
          openStatus: 0,
          sourceList: [],
          refund: 0,
          expire: 0,
          memberIdList: [],
          promoterProvinceCode: '',
          promoterCityCode: '',
          promoterRegionCode: ''
        },
        dataList: [],
        loading: false,
        promoterVisible: false,
        inputNum: '',
        type: 1,
        currentRow: {},
        returnShow: false,
        form: {
          name: '',
          description: '',
          icon: '',
          status: true
        },
        refundFlag: [
            {
              label: '全部',
              value: 0
            },
            {
              label: '未退款',
              value: 1
            },
            {
              label: '已退款',
              value: 2
            }
        ],
        expireFlag: [
            {
              label: '全部',
              value: 0
            },
            {
              label: '未过期',
              value: 1
            },
            {
              label: '已过期',
              value: 2
            }
        ],
        rules: {
          returnAmount: [
            { required: true, trigger: 'blur', validator: isNumZ, fixed: 2 },
            { required: true, trigger: 'blur', validator: this.validateReturnAmount }
          ],
          returnReason: [{ required: true, trigger: 'change', message: '请选择退款原因' }]
        },
        // 取消记录弹窗
        recordShow: false,
        record: {
          id: '',
          ename: '',
          memberName: '',
          buyRule: '',
          source: ''
        },
        tagList: []
      }
    },
    activated() {
      this.getList();
      //获取会员卡名称
      this.memberList()
    },
    methods: {
      // Enter
      searchEnter(e) {
        const keyCode = window.event ? e.keyCode : e.which;
        if (keyCode === 13) {
          this.getList()
        }
      },
      async memberList() {
        let data = await getMemberList()
        if (data) {
          this.tagList = data.list;
        }
      },
      //点击取消记录
      cancelClick(row) {
        this.record = { ...row };
        this.recordShow = true
      },
      async recordConfirm() {
        this.$common.showLoad()
        let data = await cancelBuyRecord(this.record.id)
        this.$common.hideLoad()
        if (data !== undefined) {
          this.recordShow = false
          this.$common.n_success('取消成功')
          this.getList()
        }
      },
      async getList() {
        this.loading = true
        let query = this.query
        let data = await queryBuyRecordListPage(
          query.page,
          query.limit,
          query.memberName,
          query.ename,
          query.time && query.time.length > 0 ? query.time[0] : null,
          query.time && query.time.length > 1 ? query.time[1] : null,
          query.promoterName,
          query.promoterId,
          query.promoterUserName,
          query.promoterUserId,
          query.eid,
          query.cityCode,
          query.expire,
          query.openStatus,
          query.provinceCode,
          query.regionCode,
          query.refund,
          query.sourceList,
          query.memberIdList,
          query.promoterProvinceCode,
          query.promoterCityCode,
          query.promoterRegionCode
        )
        this.loading = false
        if (data) {
          this.dataList = data.records
          this.query.total = data.total
        }
      },
      // 退款
      async rebackMoney(row) {
        this.$common.showLoad()
        const data = await getMemberReturnDetail(row.id)
        this.$common.hideLoad()
        if (data) {
          if (!data.showReturnFlag) {
            this.$common.warn('状态已更新，无法发起退款，请刷新列表')
            return
          }
          data.returnAmount = data.payAmount
          this.form = data
          this.returnShow = true
        }
      },
      handleSearch() {
        this.query.page = 1
        this.getList()
      },
      handleReset() {
        this.query = {
          page: 1,
          limit: 10,
          total: 0,
          memberName: '',
          ename: '',
          time: [],
          promoterName: '',
          provinceCode: '',
          cityCode: '',
          regionCode: '',
          openStatus: 0,
          sourceList: [],
          refund: 0,
          expire: 0,
          memberIdList: [],
          promoterProvinceCode: '',
          promoterCityCode: '',
          promoterRegionCode: ''
        }
      },
      promoterIdClick(row, type) {
        this.inputNum = ''
        this.promoterVisible = true
        this.type = type
        this.currentRow = row
        if (type == 1) {
          this.inputNum = row.promoterId ? row.promoterId : ''
        }
      },
      async promoterConfirm() {
        let type = this.type
        let currentRow = this.currentRow
        if (!this.inputNum) {
          let message = (type == 1) ? '请填写推广方ID' : '请填写推广人ID'
          this.$common.warn(message)
          return
        }
        this.$common.showLoad()
        let params = {
          id: currentRow.id
        }
        if (type == 1) {
          params.promoterId = this.inputNum
        } else {
          params.mobile = this.inputNum
        }
        let data = await updateBuyMemberPromoter(params)
        this.$common.hideLoad()
        if (typeof data !== 'undefined') {
          this.promoterVisible = false
          this.$common.n_success('更新成功')
          this.getList()
        }
      },
      // 校验数字
      checkInputNum(val) {
        val = val.replace(/[^0-9]/gi, '')
        return val
      },
      // 会员退款确认
      returnConfirm() {
        this.$refs['dataForm'].validate(async (valid) => {
          if (valid) {
            this.$common.showLoad()
            let form = this.form
            let data = await submitMemberReturn(
              form.id,
              form.returnReason,
              form.returnRemark,
              form.returnAmount
            )
            this.$common.hideLoad()
            if (typeof data !== 'undefined') {
              this.$common.n_success('退款申请成功，请等待审核');
              this.returnShow = false
              this.getList()
            }
          } else {
            return false
          }
        })
      },
      validateReturnAmount(rule, value, callback) {
        if (value > this.form.payAmount) {
          callback(new Error('退款金额不得大于实际支付金额'))
        } else {
          callback()
        }
      },
      // 导出
      async downLoadTemp() {
        let query = this.query
        this.$common.showLoad()
        let data = await createDownLoad({
          className: 'b2bMemberBuyRecordExportService',
          fileName: '导出已购买会员记录',
          groupName: '已购买会员记录导出',
          menuName: '已购买会员列表',
          searchConditionList: [
            {
              desc: '会员名称',
              name: 'memberName',
              value: query.memberName || ''
            },
            {
              desc: '终端名称',
              name: 'ename',
              value: query.ename || ''
            },
            {
              desc: '终端ID',
              name: 'eid',
              value: query.eid || ''
            },
            {
              desc: '购买开始时间',
              name: 'buyStartTime',
              value: query.time && query.time.length ? query.time[0] : ''
            },
            {
              desc: '购买结束时间',
              name: 'buyEndTime',
              value: query.time && query.time.length > 1 ? query.time[1] : ''
            },
            {
              desc: '推广方名称',
              name: 'promoterName',
              value: query.promoterName || ''
            },
            {
              desc: '推广方ID',
              name: 'promoterId',
              value: query.promoterId || ''
            },
            {
              desc: '推广人名称',
              name: 'promoterUserName',
              value: query.promoterUserName || ''
            },
            {
              desc: '推广人ID',
              name: 'promoterUserId',
              value: query.promoterUserId || ''
            },
            {
              desc: '终端ID',
              name: 'eid',
              value: query.eid || ''
            },
            {
              desc: '省',
              name: 'provinceCode',
              value: query.provinceCode || ''
            },
            {
              desc: '市',
              name: 'cityCode',
              value: query.cityCode || ''
            },
            {
              desc: '区',
              name: 'regionCode',
              value: query.regionCode || ''
            },
            {
              desc: '开通类型',
              name: 'openType',
              value: query.openStatus || 0
            },
            {
              desc: '数据来源',
              name: 'sourceList',
              value: query.sourceList.join() || ''
            },
            {
              desc: '是否退款',
              name: 'returnFlag',
              value: query.refund || 0
            },
            {
              desc: '是否过期',
              name: 'expireFlag',
              value: query.expire || 0
            },
            {
              desc: '会员卡名称',
              name: 'memberIdList',
              value: query.memberIdList.join() || ''
            },
            {
              desc: '推广方省份',
              name: 'promoterProvinceCode',
              value: query.promoterProvinceCode || ''
            },
            {
              desc: '推广方城市',
              name: 'promoterCityCode',
              value: query.promoterCityCode || ''
            },
            {
              desc: '推广方区域',
              name: 'promoterRegionCode',
              value: query.promoterRegionCode || ''
            }
          ]
        })
        this.$common.hideLoad()
        if (typeof data !== 'undefined') {
          this.$common.n_success('创建下载任务成功，请在下载中心查看')
        }
      }
    }
  }
</script>

<style lang="scss" scoped>
  @import "./index.scss";
</style>

