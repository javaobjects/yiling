<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <!-- 内容区 -->
    <div class="app-container-content has-bottom-bar">
      <!-- 头部统计数据 -->
      <div class="top-info-view">
        <div class="top-box-item">
          <div class="flex-row-left item">
            <div class="line-view"></div>
            <span class="font-size-lg bold">甲方</span>
          </div>
          <div class="font-size-base font-import-color item-text">{{ agreementInfo.ename || '- -' }}</div>
        </div>
        <div class="top-box-item">
          <div class="flex-row-left item">
            <div class="line-view"></div>
            <span class="font-size-lg bold">乙方</span>
          </div>
          <div class="font-size-base font-import-color item-text">{{ agreementInfo.secondName || '- -' }}</div>
          <div class="font-size-base font-title-color item-text">{{ agreementInfo.secondChannelName }}</div>
          <div class="font-size-base font-title-color item-text">负责该渠道商的业务人员个数：<span style="color:#1790FF;">{{ agreementInfo.secondSalesmanNum }}个</span></div>
        </div>
        <!--  -->
        <div class="top-box-item" v-if="agreementInfo.mode == 2">
          <div class="flex-row-left item">
            <div class="line-view"></div>
            <span class="font-size-lg bold">丙方</span>
          </div>
          <div class="font-size-base font-import-color item-text">{{ agreementInfo.thirdName || '- -' }}</div>
          <div class="font-size-base font-title-color item-text">{{ agreementInfo.thirdChannelName }}</div>
          <div class="font-size-base font-title-color item-text">负责该渠道商的业务人员个数：<span style="color:#1790FF;">{{ agreementInfo.thirdSalesmanNum }}个</span></div>
        </div>
      </div>
      <!-- 协议基本信息 -->
      <div class="top-box">
        <div class="flex-row-left item">
          <div class="line-view"></div>
          <span class="font-size-lg bold">协议基本信息</span>
        </div>
        <div class="detail-item">
          <div class="detail-item-left font-title-color">补充协议</div>
          <div class="detail-item-right"></div>
        </div>
        <!-- 附属年度协议： -->
        <div></div>
        <div class="detail-item">
          <div class="detail-item-left font-title-color">附属年度协议 :</div>
          <div class="detail-item-right">{{ agreementInfo.parentName }}</div>
        </div>
        <div class="detail-item">
          <div class="detail-item-left font-title-color">协议名称 :</div>
          <div class="detail-item-right">{{ agreementInfo.name }}</div>
        </div>
        <div class="detail-item">
          <div class="detail-item-left font-title-color">协议描述 :</div>
          <div class="detail-item-right">{{ agreementInfo.content }}</div>
        </div>
        <div class="detail-item">
          <div class="detail-item-left font-title-color">履约时间 :</div>
          <div class="detail-item-right">{{ agreementInfo.startTime | formatDate }} - {{ agreementInfo.endTime | formatDate }}</div>
        </div>
        <div class="detail-item">
          <div class="detail-item-left font-title-color">协议类型 :</div>
          <div class="detail-item-right">{{ agreementInfo.type == 1 ? '采购类' : '其他' }}</div>
        </div>
        <div class="detail-item" v-show="agreementInfo.rebateType">
          <div class="detail-item-left font-title-color">返利类型 :</div>
          <div class="detail-item-right">{{ agreementInfo.rebateType == 1 ? '年度返利' : '临时政策返利' }}</div>
        </div>
      </div>
      <!-- 返利条件设置 -->
      <div class="top-box" v-if="agreementInfo.type == 1">
        <div class="flex-row-left item">
          <div class="line-view"></div>
          <span class="font-size-lg bold">返利条件设置</span>
        </div>
        <div class="detail-item">
          <div class="detail-item-left font-title-color">商品相关 :</div>
          <div v-if="agreementInfo.agreementsCondition.isPatent == 1 " class="detail-item-right">非专利</div>
          <div v-else-if="agreementInfo.agreementsCondition.isPatent == 2 " class="detail-item-right">专利</div>
          <div v-else class="detail-item-right">全品种</div>
        </div>
        <div class="detail-item">
          <div class="detail-item-left font-title-color">采购相关 :</div>
          <div class="detail-item-right">
            <span v-if="agreementInfo.childType == 1">购进额</span>
            <span v-if="agreementInfo.childType == 2">购进量</span>
            <span v-if="agreementInfo.childType == 3">回款额</span>
            -
            <span v-if="agreementInfo.agreementsCondition.conditionRule && agreementInfo.agreementsCondition.conditionRule > 0">{{ gradientArray[agreementInfo.childType-1][agreementInfo.agreementsCondition.conditionRule -1 ].value }}</span>
          </div>
        </div>
        <div class="detail-item" v-if="agreementInfo.agreementsCondition.conditionRule != 4 && agreementInfo.agreementsCondition.conditionRule != 5">
          <div class="detail-item-left font-title-color">
            <span v-if="agreementInfo.childType == 1">需购进</span>
            <span v-if="agreementInfo.childType == 2">需购进</span>
            <span v-if="agreementInfo.childType == 3">需回款</span>
          </div>
          <div class="detail-item-right">
            {{ agreementInfo.agreementsCondition.totalAmount }}
            <span class="font-title-color" v-if="agreementInfo.childType == 1">元</span>
            <span class="font-title-color" v-if="agreementInfo.childType == 2">件</span>
            <span class="font-title-color" v-if="agreementInfo.childType == 3">元</span>
          </div>
        </div>
        <!-- 按季度 按月份 -->
        <div class="my-table mar-t-10" v-if="agreementInfo.agreementsCondition.conditionRule == 2 || agreementInfo.agreementsCondition.conditionRule == 3">
          <div class="my-table-col" >
            <div class="my-table-row row-time">
              <div v-if="agreementInfo.agreementsCondition.conditionRule == 2">月份</div>
              <div v-if="agreementInfo.agreementsCondition.conditionRule == 3">季度</div>
            </div>
            <div class="my-table-row">
              <div>比例</div>
            </div>
            <div class="my-table-row">
              <div v-if="agreementInfo.childType == 2">数量</div>
              <div v-else>金额</div>
            </div>
          </div>
          <div class="my-table-col" v-for="(item,index) in agreementInfo.agreementsCondition.conditions" :key="index">
            <div class="my-table-row row-time" v-if="agreementInfo.agreementsCondition.conditionRule == 2">{{ item.rangeNo }}月</div>
            <div class="my-table-row row-time" v-if="agreementInfo.agreementsCondition.conditionRule == 3">第{{ quarter[item.rangeNo - 1] }}季度</div>
            <div class="my-table-row">
              {{ item.percentage }}%
            </div>
            <div class="my-table-row">
              {{ item.amount }}
            </div>
          </div>
        </div>
        <!-- 按梯度 -->
        <div class="my-list-view" v-if="agreementInfo.agreementsCondition.conditionRule == 4">
          <div class="my-list-item" v-for="(item,index) in agreementInfo.policys" :key="index">
            <div class="my-list-item-left">第{{ gradient[index] }}梯度：</div>
            <div class="my-list-item-right">
              <div class="gradient-value">{{ item.mixValue }}</div>
              <span class="separate">至</span>
              <div class="gradient-value">{{ item.maxValue }}</div>
              <span class="separate">{{ agreementInfo.childType == 2 ? '件' : '元' }}</span>
            </div>
          </div>
        </div>
        <!-- 按时间点 -->
        <div class="detail-item" v-if="agreementInfo.agreementsCondition.conditionRule == 5">
          <div class="detail-item-left font-title-color">
            <span>回款日期 :</span>
          </div>
          <div class="detail-item-right">
            {{ agreementInfo.agreementsCondition.conditions[0].timeNode }}
            <span class="font-title-color">号</span>
          </div>
        </div>
        <!-- 支付形式 -->
        <div class="detail-item">
          <div class="detail-item-left font-title-color">支付形式 :</div>
          <div class="detail-item-right">
            <div v-if="agreementInfo.agreementsCondition.payType == 0">全部</div>
            <div v-if="agreementInfo.agreementsCondition.payType == 1">{{ agreementInfo.agreementsCondition.payTypeValues | payTypeCapitalize }}</div>
          </div>
        </div>
        <!-- 回款形式 -->
        <div class="detail-item">
          <div class="detail-item-left font-title-color">回款形式 :</div>
          <div class="detail-item-right">
            <div v-if="agreementInfo.agreementsCondition.backAmountType == 0">全部</div>
            <div v-if="agreementInfo.agreementsCondition.backAmountType == 1">{{ agreementInfo.agreementsCondition.backAmountTypeValues | backAmountTypeCapitalize }}</div>
          </div>
        </div>
      </div>
      <!-- 返利周期设置 -->
      <div class="top-box">
        <div class="flex-row-left item">
          <div class="line-view"></div>
          <span class="font-size-lg bold">返利周期设置</span>
        </div>
        <div class="font-size-base font-title-color item-text" style="margin-bottom:0px;" v-if="agreementInfo.rebateCycle == 1">
          <div>立即通过订单进行返利</div>
          <div class="tip-box mar-t-10">
            <i class="el-icon-warning"></i>
            <span>在履约周期内，所有订单直接立享折扣进行购买</span>
          </div>
        </div>
        <div class="font-size-base font-title-color item-text" style="margin-bottom:0px;" v-if="agreementInfo.rebateCycle == 2">
          <div>以天为单位进行返利计算，返利金额进入返利池，以备后期返利兑付使用</div>
          <div class="tip-box mar-t-10">
            <i class="el-icon-warning"></i>
            <span>在履约周期内，已达标订单进行以天为单位的返利计算，返利金额会进入返利资金池，以备后期返利兑付使用</span>
          </div>
        </div>
      </div>
      <!-- 返利政策设置 -->
      <div class="top-box">
        <div class="flex-row-left item">
          <div class="line-view"></div>
          <span class="font-size-lg bold">返利政策设置</span>
        </div>
        <div class="font-size-base font-title-color item-text" v-for="(item,index) in agreementInfo.policys" :key="index">
          <div v-if="agreementInfo.agreementsCondition.conditionRule == 4">第{{ gradient[index] }}梯度: {{ item.policyValue }}%，作为任务奖励</div>
          <div v-else>{{ item.policyType == 1 ? '购进' : '回款' }}总额的{{ item.policyValue }}%，作为任务奖励</div>
        </div>
      </div>
      <!-- 返利形式设置 restitutionType -->
      <div class="top-box">
        <div class="flex-row-left item">
          <div class="line-view"></div>
          <span class="font-size-lg bold">返利形式设置</span>
        </div>
        <div class="font-size-base font-title-color item-text" >
          <div v-if="agreementInfo.restitutionType == 0">全部</div>
          <div v-if="agreementInfo.restitutionType == 1">{{ agreementInfo.restitutionTypeValues | restitutionCapitalize }}</div>
        </div>
      </div>
      <!-- 可采协议商品 -->
      <div class="top-box">
        <div class="flex-row-left item btn-item">
          <div class="flex-row-left ">
            <div class="line-view"></div>
            <span class="font-size-lg bold">可采协议商品</span>
          </div>
        </div>
        <div class="mar-t-10" style="padding-bottom: 10px;background: #FFFFFF;">
            <yl-table
              :stripe="true"
              :show-header="true"
              :list="dataList"
              :total="total"
              :page.sync="query.page"
              :limit.sync="query.limit"
              :loading="loading"
              @getList="getList"
            >
              <el-table-column label="序号" min-width="55" align="center">
                <template slot-scope="{ $index }">
                  <div class="font-size-base">{{ (query.page - 1) * query.limit + $index + 1 }}</div>
                </template>
              </el-table-column>
              <el-table-column label="品类" align="center">
                <template slot-scope="{ row }">
                  <div class="font-size-base">{{ row.isPatent == '1' ? '非专利' : '专利' }}</div>
                </template>
              </el-table-column>
              <el-table-column label="商品名" min-width="162" align="center">
                <template slot-scope="{ row }">
                  <div class="goods-desc">
                    <div class="font-size-base">{{ row.goodsName }}</div>
                  </div>
                </template>
              </el-table-column>
              <el-table-column label="商品规格" min-width="55" align="center">
                <template slot-scope="{ row }">
                  <div class="font-size-base">{{ row.sellSpecifications }}</div>
                </template>
              </el-table-column>
              <el-table-column label="销售单价" min-width="55" align="center">
                <template slot-scope="{ row }">
                  <div class="font-size-base">¥{{ row.price }}</div>
                </template>
              </el-table-column>
            </yl-table>
          </div>
      </div>
      <div class="flex-row-center bottom-view">
        <yl-button plain type="primary" @click="$router.go(-1)">返回</yl-button>
      </div>
    </div>
  </div>
</template>

<script>

import {
  getSupplementAgreementsDetailBySnapshotId,
  getGoodsInfoList
} from '@/subject/pop/api/protocol';
import { channelType } from '@/subject/pop/utils/busi';

export default {
  components: {
  },
  computed: {
    channelType() {
      return channelType();
    }
  },
  filters: {
    payTypeCapitalize: function (value) {
      if (!value) return ''
      let restitutionValues = [
        {
          label: '账期支付',
          value: 2
        },
        {
          label: ' 预付款支付',
          value: 3
        }
      ]
      let arr = []
      for (let i = 0; i < value.length; i++) {
        let currentValue = value[i]
        let hasIndex = restitutionValues.findIndex(obj => {
            return currentValue == obj.value;
          });
        if (hasIndex != -1) {
          arr.push(restitutionValues[hasIndex].label)
        }
      }
      return arr.join('、')
    },
    backAmountTypeCapitalize: function (value) {
      if (!value) return ''
      let restitutionValues = [
        {
          label: '电汇',
          value: 1
        },
        {
          label: '银行承兑',
          value: 3
        }
      ]
      let arr = []
      for (let i = 0; i < value.length; i++) {
        let currentValue = value[i]
        let hasIndex = restitutionValues.findIndex(obj => {
            return currentValue == obj.value;
          });
        if (hasIndex != -1) {
          arr.push(restitutionValues[hasIndex].label)
        }
      }
      return arr.join('、')
    },
    restitutionCapitalize: function (value) {
      if (!value) return ''
      let restitutionValues = [
        {
          label: '票折',
          value: 1
        },
        {
          label: '现金',
          value: 2
        },
        {
          label: '冲红',
          value: 3
        },
        {
          label: '健康城卡',
          value: 4
        }
      ]
      let arr = []
      for (let i = 0; i < value.length; i++) {
        let currentValue = value[i]
        let hasIndex = restitutionValues.findIndex(obj => {
            return currentValue == obj.value;
          });
        if (hasIndex != -1) {
          arr.push(restitutionValues[hasIndex].label)
        }
      }
      return arr.join(' ')
    }
  },
  data() {
    return {
      // 药品分类
      isPatentArray: [
        {
          label: '全部',
          value: 0
        },
        {
          label: '非专利',
          value: 1
        },
        {
          label: '专利',
          value: 2
        }
      ],
      quarter: ['一', '二', '三', '四'],
      gradient: ['一', '二', '三', '四', '五', '六', '七', '八', '九', '十', '十一', '十二', '十三', '十四', '十五', '十六', '十七', '十八', '十九', '二十'],
      // 头部导航
      navList: [
        {
          title: '首页',
          path: '/dashboard'
        },
        {
          title: '协议管理'
        },
        {
          title: '年度协议详情页'
        },
        {
          title: '补充协议修改详情页'
        }
      ],
      loading: false,
      query: {
        page: 1,
        limit: 10
      },
      total: 0,
      dataList: [],
      // 协议信息
      agreementInfo: {},
      gradientArray: [ //  梯度数组
        [
          {
            label: 1,
            value: '购进总额'
          },
          {
            label: 2,
            value: '按月度进行购进总额拆解'
          },
          {
            label: 3,
            value: '按季度进行购进总额拆解'
          },
          {
            label: 4,
            value: '按购进实际金额梯度'
          }
        ],
        [
          {
            label: 1,
            value: '购进总量'
          },
          {
            label: 2,
            value: '按月度进行购进总量拆解'
          },
          {
            label: 3,
            value: '按季度进行购进总量拆解'
          },
          {
            label: 4,
            value: '按购进实际数量梯度'
          }
        ],
        [
          {
            label: 1,
            value: '回款总额'
          },
          {
            label: 2,
            value: '按月度进行回款总额拆解'
          },
          {
            label: 3,
            value: '按季度进行回款总额拆解'
          },
          {
            label: 4,
            value: '按购进实际回款金额梯度'
          },
          {
            label: 5,
            value: '按回款时间点'
          }
        ]
      ]
    };
  },
  mounted() {
    this.params = this.$route.params;
    // 获取补充协议信息
    this.getSupplementAgreementsDetailMethod()
    // 获取底部商品列表
    this.getList()
  },
  methods: {
    async getList() {
      this.loading = true;
      let params = this.params
      let query = this.query;
      let data = await getGoodsInfoList(
        query.page,
        query.limit,
        params.supplementId
      );
      this.loading = false;
      if (data) {
        this.dataList = data.records;
        this.total = data.total;
      }
    },
    // 获取补充协议详情
    async getSupplementAgreementsDetailMethod() {
      let params = this.params
      let data = await getSupplementAgreementsDetailBySnapshotId(
        params.supplementId,
        params.agreementSnapshotId
      );
      if (data) {
        this.agreementInfo = data
      }
    },
    // 查看补充协议弹框
    viewSupplementalDialog() {
      this.supplementalVisible = true

      //获取补充协议列表
      this.getSupplementAgreementPageListMethod();
    },
    //tab切换
    handleTabClick(tab, event) {
      this.agreementQuery.page = 1;
      this.agreementQuery.limit = 10;
      this.supplementList = []
      this.supplementTotal = 0
      //获取补充协议列表
      this.getSupplementAgreementPageListMethod();
    },
    //获取补充协议列表
    async getSupplementAgreementPageListMethod() {
      this.loading2 = true;
      let agreementQuery = this.agreementQuery;
      let params = this.params
      let data = await getSupplementAgreementPageList(
        agreementQuery.page,
        agreementQuery.limit,
        parseInt(agreementQuery.agreementStatus),
        params.id
      );
      this.loading2 = false;
      this.supplementList = data.records;
      this.supplementTotal = data.total;
      this.$log('this.dataList:', data);

    }

  }
};
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
