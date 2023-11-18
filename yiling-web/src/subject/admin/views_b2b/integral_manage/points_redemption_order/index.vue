<template>
  <div class="app-container">
    <!-- 内容区域 -->
    <div class="app-container-content">
      <!-- 中部 搜索条件 -->
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">用户ID</div>
              <el-input v-model="query.uid" @keyup.enter.native="searchEnter" placeholder="请输入用户ID" />
            </el-col>
            <el-col :span="6">
              <div class="title">用户名称</div>
              <el-input v-model="query.uname" @keyup.enter.native="searchEnter" placeholder="请输入用户名称" />
            </el-col>
            <el-col :span="6">
              <div class="title">兑换提交时间</div>
              <el-date-picker
                v-model="query.submitTime"
                type="daterange"
                format="yyyy/MM/dd"
                value-format="yyyy-MM-dd"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期">
              </el-date-picker>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">兑换订单号</div>
              <el-input v-model="query.orderNo" @keyup.enter.native="searchEnter" placeholder="请输入兑换订单号"/>
            </el-col>
            <el-col :span="6">
              <div class="title">兑换商品名称</div>
              <el-input v-model="query.goodsName" @keyup.enter.native="searchEnter" placeholder="请输入兑换商品名称" />
            </el-col>
            <el-col :span="6">
              <div class="title">订单兑付时间</div>
              <el-date-picker
                v-model="query.cashingTime"
                type="daterange"
                format="yyyy/MM/dd"
                value-format="yyyy-MM-dd"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期">
              </el-date-picker>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">兑付状态</div>
              <el-select class="select-width" v-model="query.status" placeholder="请选择">
                <el-option label="全部" value=""></el-option>
                <el-option label="未兑付" value="1"></el-option>
                <el-option label="已兑付" value="2"></el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <div class="title">提交人手机号</div>
              <el-input v-model="query.mobile" @keyup.enter.native="searchEnter" placeholder="请输入用户名称" />
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
          <yl-button type="primary" plain @click="downLoadTemp">导出兑换记录</yl-button>
        </div>
      </div>
      <!-- 下部列表 -->
      <div class="search-bar mar-t-8">
        <yl-table 
          border 
          :show-header="true" 
          :list="dataList" 
          :total="query.total" 
          :page.sync="query.page" 
          :limit.sync="query.limit" 
          :loading="loading" 
          @getList="getList">
          <el-table-column label="序号" min-width="60" align="center">
            <template slot-scope="scope">
              <span>{{ (query.page - 1) * query.limit + scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column label="兑换提交时间" min-width="130" align="center">
            <template slot-scope="{ row }">
              {{ row.submitTime | formatDate }}
            </template>
          </el-table-column>
          <el-table-column label="兑换订单号" min-width="120" align="center" prop="orderNo"></el-table-column>
          <el-table-column label="兑换商品类型" min-width="100" align="center">
            <template slot-scope="{ row }">
              {{ row.goodsType | dictLabel(integralGoodsTypeData) }}
            </template>
          </el-table-column>
          <el-table-column label="兑换商品名称" min-width="100" align="center" prop="goodsName"></el-table-column>
          <el-table-column label="兑换数量" min-width="70" align="center" prop="exchangeNum"></el-table-column>
          <el-table-column label="订单积分值" min-width="70" align="center" prop="orderIntegral"></el-table-column>
          <el-table-column label="兑付状态" min-width="70" align="center">
            <template slot-scope="{ row }">
              {{ row.status == 1 ? '未兑付' : '已兑付' }}
            </template>
          </el-table-column>
          <el-table-column label="用户ID" min-width="70" align="center" prop="uid"></el-table-column>
          <el-table-column label="用户名称" min-width="120" align="center" prop="uname"></el-table-column>
          <el-table-column label="提交人手机号" min-width="100" align="center" prop="mobile"></el-table-column>
          <el-table-column label="收货人" min-width="90" align="center" prop="contactor"></el-table-column>
          <el-table-column label="联系电话" min-width="100" align="center" prop="contactorPhone"></el-table-column>
          <el-table-column label="收货地址" min-width="200" align="center" >
            <template slot-scope="{ row }">
              {{ row.provinceName }} {{ row.cityName }} {{ row.regionName }} {{ row.address }}
            </template>
          </el-table-column>
          <el-table-column label="快递单号" min-width="100" align="center" prop="expressOrderNo"></el-table-column>
          <el-table-column label="订单兑付时间" min-width="100" align="center" prop="">
            <template slot-scope="{ row }">
              {{ row.exchangeTime | formatDate }}
            </template>
          </el-table-column>
          <el-table-column label="操作" min-width="100" align="center" fixed="right">
            <template slot-scope="{ row }">
               <yl-button type="text" v-if="row.status == 1" @click="immediateCashingClick(row)">立即兑付</yl-button>
               <yl-button type="text" @click="cashingClick(row)" v-else>兑付详情</yl-button>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
    <yl-dialog 
      title="订单兑付详情" 
      :visible.sync="dialogShow" 
      width="600px" 
      :show-confirm="type == 1"
      @confirm="confirm">
      <div class="dialog-popUp">
        <div v-if="type == 1">
          <el-form 
            :model="form" 
            :rules="rules" 
            ref="dataForm" 
            label-width="130px" 
            class="demo-ruleForm">
            <el-form-item label="收货人" prop="contactor">
              <el-input 
                v-model.trim="form.contactor" 
                maxlength="10" 
                show-word-limit
                :disabled="reviseShow == 1"
                placeholder="请输入收货人名称">
              </el-input>
            </el-form-item>
            <el-form-item label="收货人手机号" prop="contactorPhone">
              <el-input 
                v-model.trim="form.contactorPhone"
                :disabled="reviseShow == 1"
                placeholder="请输入收货人手机号">
              </el-input>
            </el-form-item>
            <el-form-item label="收货地址" prop="regionCode">
              <yl-choose-address 
                width="230px" 
                :province.sync="form.provinceCode" 
                :city.sync="form.cityCode" 
                :area.sync="form.regionCode" 
                :disabled="reviseShow == 1"
                />
            </el-form-item>
            <el-form-item label="" prop="address">
               <el-input 
                v-model.trim="form.address"
                :disabled="reviseShow == 1"
                placeholder="请输入详细地址">
              </el-input>
            </el-form-item>
            <div style="text-align:right" v-if="status == 1">
               <yl-button type="text" v-if="reviseShow == 1" @click="reviseClick">修改</yl-button>
               <yl-button type="text" v-else @click="preserveClick">保存</yl-button>
            </div>
            <el-form-item label="快递公司" prop="expressCompany">
              <el-select class="select-width" v-model="form.expressCompany" placeholder="请选择">
                <el-option
                  v-for="item in expressCompanyData"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="快递单号" prop="expressOrderNo">
               <el-input 
                v-model.trim="form.expressOrderNo"
                placeholder="请输入快递单号">
              </el-input>
            </el-form-item>
          </el-form>
        </div>
        <div v-if="type == 2" class="dialog-conter">
          <p v-for="(item, index) in cardInfoList" :key="index">
            卡号：{{ item.cardNo }} 密码：{{ item.password }}
          </p>
        </div>
        <div v-if="type == 3 || type == 4" class="dialog-conter">
          <p>
            优惠券ID：{{ couponId }}
          </p>
        </div>
      </div>
    </yl-dialog>
  </div>
</template>

<script>
import { 
  integralQueryListPage, 
  exchangeDetail,
  dfUpdate, 
  updateAddress,
  atOnceExchange
} from '@/subject/admin/api/b2b_api/integral_record'
import { integralGoodsType } from '@/subject/admin/busi/b2b/integral'
import { ylChooseAddress } from '@/subject/admin/components'
import { createDownLoad } from '@/subject/admin/api/common'
export default {
  name: 'PointsRedemptionOrder',
  components: {
    ylChooseAddress
  },
  computed: {
    integralGoodsTypeData() {
      return integralGoodsType()
    }
  },
  data() {
    return {
      query: {
        uid: '',
        uname: '',
        submitTime: [],
        orderNo: '',
        goodsName: '',
        cashingTime: [],
        status: '',
        mobile: '',
        total: 0,
        page: 1,
        limit: 10
      },
      dataList: [],
      loading: false,
      //弹窗
      dialogShow: false,
      //1-真实物品 2-虚拟物品 3-商品优惠券 4-会员优惠券
      type: 1,
      form: {
        //兑付订单ID
        id: '',
        contactor: '',
        contactorPhone: '',
        provinceCode: '',
        cityCode: '',
        regionCode: '',
        address: '',
        expressCompany: '',
        expressOrderNo: ''
      },
      rules: {
        contactor: [{ required: true, message: '请输入收货人', trigger: 'blur' }],
        contactorPhone: [{ required: true, message: '请输入收货人手机号', trigger: 'blur' }],
        regionCode: [{ required: true, message: '请选择收货地址', trigger: 'change' }],
        address: [{ required: true, message: '请输入详细地址', trigger: 'blur' }],
        expressCompany: [{ required: true, message: '请选择快递公司', trigger: 'change' }],
        expressOrderNo: [{ required: true, message: '请输入快递单号', trigger: 'blur' }]
      },
      expressCompanyData: [
        {
          label: '韵达快递',
          value: '韵达快递'
        },
        {
          label: '天天快递',
          value: '天天快递'
        },
        {
          label: '申通快递',
          value: '申通快递'
        },
        {
          label: '圆通速递',
          value: '圆通速递'
        },
        {
          label: '德邦物流',
          value: '德邦物流'
        },
        {
          label: '百世汇通',
          value: '百世汇通'
        },
        {
          label: '顺丰速运',
          value: '顺丰速运'
        },
        {
          label: '京东物流',
          value: '京东物流'
        },
        {
          label: '中通速递',
          value: '中通速递'
        },
        {
          label: '邮政EMS',
          value: '邮政EMS'
        },
        {
          label: '其他',
          value: '其他'
        }
      ],
      cardInfoList: [],
      //优惠券id
      couponId: '',
      //是否展示弹窗的确定按钮
      showConfirm: false,
      //兑换状态：1-未兑换 2-已兑换
      status: 0,
      //是否修改地址 1修改 2保存
      reviseShow: 1
    }
  },
  activated() {
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
    //点击弹窗的修改
    reviseClick() {
      this.reviseShow = 2
    },
    //点击弹窗的 保存
    async preserveClick() {
      const { contactor, contactorPhone, regionCode, address } = this.form
      if (contactor && contactorPhone && regionCode && address) {
        //执行校验成功的相关操作
        let form = this.form;
        this.$common.showLoad();
        let data = await updateAddress(
          form.id,
          form.contactor,
          form.contactorPhone,
          form.provinceCode,
          form.cityCode,
          form.regionCode,
          form.address
        )
        this.$common.hideLoad();
        if (data !== undefined) {
          this.$common.n_success('修改成功')
          this.getList();
        }
      } else {
        this.$refs['dataForm'].validateField(['contactor', 'contactorPhone', 'regionCode', 'address'], valid => {
        })
      }
    },
    async getList() {
      let query = this.query;
      this.loading = true;
      let data = await integralQueryListPage(
        query.uid,
        query.uname,
        query.submitTime && query.submitTime.length > 0 ? query.submitTime[0] : '',
        query.submitTime && query.submitTime.length > 1 ? query.submitTime[1] : '',
        query.orderNo,
        query.goodsName,
        query.cashingTime && query.cashingTime.length > 0 ? query.cashingTime[0] : '',
        query.cashingTime && query.cashingTime.length > 1 ? query.cashingTime[1] : '',
        query.status,
        query.mobile,
        query.page,
        query.limit
      )
      if (data) {
        this.dataList = data.records;
        this.query.total = data.total;
      }
      this.loading = false;
    },
    //点击弹窗保存
    confirm() {
      this.$refs['dataForm'].validate( async(valid) => {
        if (valid) {
          let form = this.form;
          if (this.status && this.status == 1) {
            this.$common.showLoad();
            let data = await atOnceExchange(
              form.id,
              form.expressCompany,
              form.expressOrderNo
            )
            this.$common.hideLoad();
            if (data !== undefined) {
              this.$common.n_success('兑付成功')
              this.getList();
              this.dialogShow = false;
            }
          } else {
            this.$common.showLoad();
            let data2 = await dfUpdate(
              form.id,
              form.contactor,
              form.contactorPhone,
              form.provinceCode,
              form.cityCode,
              form.regionCode,
              form.address,
              form.expressCompany,
              form.expressOrderNo
            )
            this.$common.hideLoad();
            if (data2 !== undefined) {
              this.$common.n_success('修改成功')
              this.getList();
              this.dialogShow = false;
            }
          }
        }
      })
    },
    //点击兑付详情
    async cashingClick(row) {
      this.type = row.goodsType;
      //兑付状态
      this.status = row.status;
      this.$common.showLoad();
      let data = await exchangeDetail(
        row.id
      )
      this.$common.hideLoad();
      if (data !== undefined) {
        if (row.goodsType == 1) {
          this.form = {
            id: data.id,
            ...data.receiptInfo	
          }
        } else if (row.goodsType == 2) {
          this.cardInfoList = data.cardInfoList;
        } else if (row.goodsType == 3 || row.goodsType == 4) {
          this.couponId = data.couponId
        }
      }
      this.dialogShow = true;
    },
    //点击立即兑付
    immediateCashingClick(row) {
      this.type = row.goodsType;
      //兑付状态
      this.status = row.status;
      this.form = {
        id: row.id,
        contactor: row.contactor,
        contactorPhone: row.contactorPhone,
        provinceCode: row.provinceCode,
        cityCode: row.cityCode,
        regionCode: row.regionCode,
        address: row.address,
        expressCompany: '',
        expressOrderNo: ''
      }
      this.dialogShow = true;
    },
    // 搜索
    handleSearch() {
      this.query.page = 1;
      this.getList();
    },
    // 重置
    handleReset() {
      this.query = {
        uid: '',
        uname: '',
        submitTime: [],
        orderNo: '',
        goodsName: '',
        cashingTime: [],
        status: '',
        mobile: '',
        total: 0,
        page: 1,
        limit: 10
      }
    },
    //导出兑换记录
    async downLoadTemp() {
      let query = this.query
      this.$common.showLoad()
      let data = await createDownLoad({
        className: 'integralExchangeOrderExportService',
        fileName: '导出兑换记录',
        groupName: '导出兑换记录',
        menuName: '积分兑换订单列表',
        searchConditionList: [
          {
            desc: '用户ID',
            name: 'uid',
            value: query.uid || ''
          },
          {
            desc: '用户名称',
            name: 'behaviorName',
            value: query.behaviorName || ''
          },
          {
            desc: '提交人手机号',
            name: 'uname',
            value: query.uname || ''
          },
          {
            desc: '兑换提交开始时间',
            name: 'startSubmitTime',
            value: query.submitTime && query.submitTime.length > 0 ? query.submitTime[0] : ''
          },
          {
            desc: '兑换提交结束时间',
            name: 'endSubmitTime',
            value: query.submitTime && query.submitTime.length > 1 ? query.submitTime[1] : ''
          },
          {
            desc: '兑换订单号',
            name: 'orderNo',
            value: query.orderNo || ''
          },
          {
            desc: '兑换商品名称',
            name: 'goodsName',
            value: query.goodsName || ''
          },
          {
            desc: '订单兑付开始时间',
            name: 'startExchangeTime',
            value: query.cashingTime && query.cashingTime.length > 0 ? query.cashingTime[0] : ''
          },
          {
            desc: '订单兑付结束时间',
            name: 'endExchangeTime',
            value: query.cashingTime && query.cashingTime.length > 1 ? query.cashingTime[1] : ''
          },
          {
            desc: '兑付状态',
            name: 'status',
            value: query.status || ''
          },
          {
            desc: '提交人手机号',
            name: 'mobile',
            value: query.mobile || ''
          }
        ]
      })
      this.$common.hideLoad()
      if (data !== undefined) {
        this.$common.n_success('创建下载任务成功，请在下载中心查看')
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>