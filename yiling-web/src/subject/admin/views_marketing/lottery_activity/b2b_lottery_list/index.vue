<template>
  <div class="app-container">
    <!-- 内容区 -->
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">活动名称</div>
              <el-input v-model="query.activityName" placeholder="请输入活动名称" @keyup.enter.native="handleSearch" />
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
                  v-for="item in lotteryActivityProgress"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <div class="title">分类</div>
              <el-select v-model="query.category" placeholder="请选择">
                <el-option label="全部" :value="0"></el-option>
                <el-option
                  v-for="item in categoryArray"
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
              <div class="title">创建人</div>
              <el-input v-model="query.createUserName" placeholder="请输入创建人姓名" @keyup.enter.native="handleSearch" />
            </el-col>
            <el-col :span="6">
              <div class="title">创建人手机号</div>
              <el-input v-model="query.mobile" placeholder="请输入创建人手机号" @keyup.enter.native="handleSearch" />
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
              <div class="title">运营备注</div>
              <el-input v-model="query.opRemark" placeholder="请输入运营备注" @keyup.enter.native="handleSearch" />
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
          <el-table-column label="活动名称" min-width="180" align="center" prop="activityName">
          </el-table-column>
          <el-table-column label="分类" min-width="100" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.category | dictLabel(categoryArray) }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="开始时间" min-width="160" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.startTime | formatDate }}
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
          <el-table-column label="创建人手机号" min-width="150" align="center" prop="mobile">
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
                {{ row.progress | dictLabel(lotteryActivityProgress) }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="运营备注" min-width="200" align="center" prop="opRemark">
          </el-table-column>
          <el-table-column label="预算金额" min-width="100" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.budgetAmount | toThousand }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="关联活动" min-width="120" align="center">
            <template slot-scope="{ row }">
              <div>
                <yl-button type="text" @click="unionActivityNumClick(row)">{{ row.unionActivityNum }}</yl-button>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="抽奖机会" min-width="120" align="center">
            <template slot-scope="{ row }">
              <div>
                <yl-button type="text" @click="getJoinNumClick(row)">{{ row.getJoinNum }}</yl-button>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="抽奖次数" min-width="120" align="center">
            <template slot-scope="{ row }">
              <div>
                <yl-button type="text" @click="joinNumClick(1, row)">{{ row.joinNum }}</yl-button>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="中奖次数" min-width="120" align="center">
            <template slot-scope="{ row }">
              <div>
                <yl-button type="text" @click="joinNumClick(2, row)">{{ row.hitNum }}</yl-button>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="操作" min-width="180" align="center" fixed="right">
            <template slot-scope="{ row }">
              <div class="operation-view">
                <div class="option">
                  <yl-button type="text" @click="showDetail(row)">查看</yl-button>
                  <yl-button type="text" :disabled="row.progress == 3" @click="editClick(row)">编辑</yl-button>
                  <yl-button type="text" @click="copyClick(row)">复制</yl-button>
                </div>
                <div class="option">
                  <yl-button type="text" :disabled="row.progress == 3" @click="stopClick(row)">停用</yl-button>
                  <yl-button type="text" @click="urlClick(row)">复制链接</yl-button>
                </div>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
    <!-- 关联活动明细 -->
    <yl-dialog title="关联活动明细" :visible.sync="unionActivityNumDialog" :show-footer="false">
      <div class="dialog-content-box">
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
            <el-table-column label="促销活动ID" min-width="100" align="center" prop="id">
            </el-table-column>
            <el-table-column label="促销活动名称" min-width="200" align="center" prop="name">
            </el-table-column>
            <el-table-column label="促销活动类型" min-width="100" align="center">
              <template slot-scope="{ row }">
                <div>
                  {{ row.strategyType | dictLabel(strategyType) }}
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
          </yl-table>
        </div>
      </div>
    </yl-dialog>
    <!-- 抽奖机会明细 -->
    <yl-dialog title="抽奖机会明细" :visible.sync="getJoinNumDialog" :show-footer="false">
      <div class="dialog-content-box">
        <div>
          <ylButton type="primary" plain @click="downLoadTemp1">导出</ylButton>
        </div>
        <div class="mar-t-10" style="padding-bottom: 10px;background: #FFFFFF;">
          <yl-table
            border
            show-header
            :list="getJoinNumList"
            :total="getJoinNumTotal"
            :page.sync="getJoinNumQuery.page"
            :limit.sync="getJoinNumQuery.limit"
            :loading="loading3"
            @getList="lotteryActivityQueryLotteryGetPageMethod"
          >
            <el-table-column label="序号" min-width="55" align="center">
              <template slot-scope="{ $index }">
                <div class="font-size-base">{{ (getJoinNumQuery.page - 1) * getJoinNumQuery.limit + $index + 1 }}</div>
              </template>
            </el-table-column>
            <el-table-column label="时间" min-width="160" align="center" prop="giftName">
              <template slot-scope="{ row }">
                <div>
                  {{ row.createTime | formatDate }}
                </div>
              </template>
            </el-table-column>
            <el-table-column label="活动ID" min-width="100" align="center" prop="lotteryActivityId">
            </el-table-column>
            <el-table-column label="活动名称" min-width="200" align="center" prop="activityName">
            </el-table-column>
            <el-table-column label="用户ID" min-width="100" align="center" prop="uid">
            </el-table-column>
            <el-table-column label="用户名称" min-width="200" align="center" prop="uname">
            </el-table-column>
            <el-table-column label="获取途径" min-width="200" align="center" prop="getType">
              <template slot-scope="{ row }">
                <div>
                  {{ row.getType | dictLabel(lotteryActivityGetType) }}
                </div>
              </template>
            </el-table-column>
            <el-table-column label="获取次数" min-width="100" align="center" prop="getTimes">
            </el-table-column>
          </yl-table>
        </div>
      </div>
    </yl-dialog>
    <!-- 抽奖次数 -->
    <yl-dialog :title="pageType == 1 ? '参与次数明细' : '中奖次数明细'" :visible.sync="joinNumDialog" :show-footer="false">
      <div class="dialog-content-box">
        <div class="down-box clearfix" style="margin-top: 0;">
          <ylButton type="primary" plain @click="downLoadTemp">导出</ylButton>
          <div class="btn">
            <ylButton type="primary" plain @click="cashClick(3)">兑付全部</ylButton>
            <ylButton type="primary" plain @click="cashClick(2)">兑付当前页</ylButton>
          </div>
        </div>
        <div class="mar-t-10" style="padding-bottom: 10px;background: #FFFFFF;">
          <yl-table
            border
            show-header
            :list="joinNumList"
            :total="joinNumTotal"
            :page.sync="joinNumQuery.page"
            :limit.sync="joinNumQuery.limit"
            :loading="loading4"
            @getList="lotteryActivityQueryJoinDetailPageMethod"
          >
            <el-table-column label="序号" min-width="55" align="center">
              <template slot-scope="{ $index }">
                <div class="font-size-base">{{ (joinNumQuery.page - 1) * joinNumQuery.limit + $index + 1 }}</div>
              </template>
            </el-table-column>
            <el-table-column label="时间" min-width="160" align="center" prop="giftName">
              <template slot-scope="{ row }">
                <div>
                  {{ row.lotteryTime | formatDate }}
                </div>
              </template>
            </el-table-column>
            <el-table-column label="活动ID" min-width="100" align="center" prop="lotteryActivityId">
            </el-table-column>
            <el-table-column label="活动名称" min-width="200" align="center" prop="activityName">
            </el-table-column>
            <el-table-column label="用户ID" min-width="100" align="center" prop="uid">
            </el-table-column>
            <el-table-column label="用户名称" min-width="200" align="center" prop="uname">
            </el-table-column>
            <el-table-column label="商家" min-width="200" align="center" prop="shopEname">
            </el-table-column>
            <el-table-column label="奖品类型" min-width="200" align="center" prop="getType">
              <template slot-scope="{ row }">
                <div>
                  {{ row.rewardType | dictLabel(lotteryActivityRewardType) }}
                </div>
              </template>
            </el-table-column>
            <el-table-column label="奖品名称" min-width="200" align="center" prop="rewardName">
            </el-table-column>
            <el-table-column label="奖品数量" min-width="100" align="center">
              <template slot-scope="{ row }">
                <div v-if="row.rewardType == 5">- -</div>
                <div v-else>
                  {{ row.rewardNumber }}
                </div>
              </template>
            </el-table-column>
            <el-table-column label="兑付状态" min-width="80" align="center">
              <template slot-scope="{ row }">
                <div v-if="row.rewardType == 5">- -</div>
                <div v-else>
                  {{ row.status == 1 ? '已兑付' : '未兑付' }}
                </div>
              </template>
            </el-table-column>
            <el-table-column label="收货人" min-width="100" align="center" prop="contactor">
              <template slot-scope="{ row }">
                <div v-if="row.rewardType == 1">{{ row.contactor }}</div>
              </template>
            </el-table-column>
            <el-table-column label="联系电话" min-width="110" align="center" prop="contactorPhone">
              <template slot-scope="{ row }">
                <div v-if="row.rewardType == 1">{{ row.contactorPhone }}</div>
              </template>
            </el-table-column>
            <el-table-column label="收货地址-省" min-width="100" align="center" prop="address">
              <template slot-scope="{ row }">
                <div v-if="row.rewardType == 1">{{ row.provinceName }}</div>
              </template>
            </el-table-column>
            <el-table-column label="收货地址-市" min-width="100" align="center" prop="address">
              <template slot-scope="{ row }">
                <div v-if="row.rewardType == 1">{{ row.cityName }}</div>
              </template>
            </el-table-column>
            <el-table-column label="收货地址-区" min-width="100" align="center" prop="address">
              <template slot-scope="{ row }">
                <div v-if="row.rewardType == 1">{{ row.regionName }}</div>
              </template>
            </el-table-column>
            <el-table-column label="收货地址" min-width="200" align="center" prop="address">
              <template slot-scope="{ row }">
                <div v-if="row.rewardType == 1">{{ row.address }}</div>
              </template>
            </el-table-column>
            <el-table-column label="快递单号" min-width="200" align="center" prop="expressOrderNo">
              <template slot-scope="{ row }">
                <div v-if="row.rewardType == 1">
                  <div>{{ row.expressCompany }}</div>
                  <div>{{ row.expressOrderNo }}</div>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="操作" min-width="200" align="center" fixed="right">
              <template slot-scope="{ row }">
                <div v-if="row.rewardType == 1 && row.status == 1">
                  <yl-button type="text" @click="cashClick(1, row)">修改</yl-button>
                </div>
                <div>
                  <yl-button v-if="(row.rewardType == 1 || row.rewardType == 2 || row.rewardType == 3 || row.rewardType == 4) && row.status == 2" type="text" @click="cashClick(1, row)">兑付</yl-button>
                  <yl-button v-if="row.rewardType == 1 && row.status == 2" type="text" @click="editAddressClick(row)">修改地址</yl-button>
                </div>
              </template>
            </el-table-column>
          </yl-table>
        </div>
      </div>
    </yl-dialog>
    <!-- 真实物品兑付 -->
    <yl-dialog
      :visible.sync="show"
      :title="cashType == 1 ? '兑付奖品' : cashType == 2 ? '兑付当前页' : '兑付全部'"
      width="600px"
      @confirm="cashConfirm">
      <div class="dialog-content">
        <div class="tip" v-if="!(cashType == 1 && cashCurrentRow.status == 1)">
          确定兑付{{ cashType == 1 ? '' : cashType == 2 ? '当前页的' : '全' }}奖品吗？
        </div>
        <el-form
          v-if="cashType == 1 && cashCurrentRow.rewardType == 1"
          ref="dataForm"
          :rules="rules"
          :model="form"
          label-width="auto"
          label-position="right">
          <el-form-item label="收货人" prop="contactor">
            <el-input v-model="form.contactor" maxlength="10" placeholder="请输入收货人" />
          </el-form-item>
          <el-form-item label="联系电话" prop="contactorPhone">
            <el-input v-model="form.contactorPhone" maxlength="11" placeholder="请输入联系电话" />
          </el-form-item>
          <el-form-item label="收货地址" prop="regionCode">
            <yl-choose-address :province.sync="form.provinceCode" :city.sync="form.cityCode" :area.sync="form.regionCode" />
          </el-form-item>
          <el-form-item label="详细地址" prop="address">
            <el-input v-model="form.address" maxlength="30" placeholder="请输入详细地址" />
          </el-form-item>
          <el-form-item label="快递公司" prop="expressCompany">
            <el-select v-model="form.expressCompany" placeholder="请选择快递公司">
              <el-option
                v-for="item in expressCompanyArray"
                :key="item.value"
                :label="item.label"
                :value="item.label"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="快递单号" prop="expressOrderNo">
            <el-input v-model="form.expressOrderNo" placeholder="请输入快递单号" />
          </el-form-item>
        </el-form>
      </div>
    </yl-dialog>
    <!-- 未兑付修改地址 -->
    <yl-dialog
      :visible.sync="editShow"
      title="修改地址"
      width="600px"
      @confirm="editAddressConfirm">
      <div class="dialog-content">
        <el-form
          ref="dataForm"
          :rules="rules"
          :model="form"
          label-width="auto"
          label-position="right">
          <el-form-item label="收货人" prop="contactor">
            <el-input v-model="form.contactor" maxlength="10" placeholder="请输入收货人" />
          </el-form-item>
          <el-form-item label="联系电话" prop="contactorPhone">
            <el-input v-model="form.contactorPhone" maxlength="11" placeholder="请输入联系电话" />
          </el-form-item>
          <el-form-item label="收货地址" prop="regionCode">
            <yl-choose-address :province.sync="form.provinceCode" :city.sync="form.cityCode" :area.sync="form.regionCode" />
          </el-form-item>
          <el-form-item label="详细地址" prop="address">
            <el-input v-model="form.address" maxlength="30" placeholder="请输入详细地址" />
          </el-form-item>
        </el-form>
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

import { lotteryActivityQueryListPage, lotteryActivityCopyLottery, lotteryActivityStop, strategyActivityLotteryPageList, lotteryActivityQueryLotteryGetPage, lotteryActivityQueryJoinDetailPage, lotteryActivityCashReward, lotteryActivityUpdateCashInfo } from '@/subject/admin/api/view_marketing/lottery_activity'
import { createDownLoad } from '@/subject/admin/api/common'
import { lotteryActivityProgress, lotteryActivityGetType, lotteryActivityRewardType } from '@/subject/admin/busi/marketing/lottery'
import { strategyType } from '@/subject/admin/busi/b2b/marketing_activity'
import { ylChooseAddress } from '@/subject/admin/components'
import { isTel } from '@/subject/admin/utils/rules'

export default {
  name: 'B2bLotteryList',
  components: {
    ylChooseAddress
  },
  computed: {
    // 活动进度：1-未开始 2-进行中 3-已结束
    lotteryActivityProgress() {
      return lotteryActivityProgress()
    },
    // 获取方式
    lotteryActivityGetType() {
      return lotteryActivityGetType()
    },
    // 奖品类型：1-真实物品 2-虚拟物品 3-商品优惠券 4-会员优惠券 5-空奖 6-抽奖机会
    lotteryActivityRewardType() {
      return lotteryActivityRewardType()
    },
    // 策略类型：1-订单累计金额/2-签到天数/3-时间周期/4-购买会员
    strategyType() {
      return strategyType()
    }
  },
  data() {
    return {
      expressCompanyArray: [
        {
          label: '韵达快递',
          value: 1
        },
        {
          label: '天天快递',
          value: 2
        },
        {
          label: '申通快递',
          value: 3
        },
        {
          label: '圆通速递',
          value: 4
        },
        {
          label: '德邦物流',
          value: 5
        },
        {
          label: '百世汇通',
          value: 6
        },
        {
          label: '顺丰速运',
          value: 7
        },
        {
          label: '京东物流',
          value: 8
        },
        {
          label: '中通速递',
          value: 9
        },
        {
          label: '邮政EMS',
          value: 10
        },
        {
          label: '其他',
          value: 11
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
      categoryArray: [
        {
          label: '平台活动',
          value: 1
        },
        {
          label: '商家活动',
          value: 2
        }
      ],
      // 平台类型：1-B端 2-C端
      platformType: 1,
      loading: false,
      query: {
        page: 1,
        limit: 10,
        total: 0,
        status: 0,
        progress: 0,
        category: 0,
        time: []
      },
      dataList: [],
      // 活动使用数量弹框
      loading2: false,
      unionActivityNumDialog: false,
      sendNumList: [],
      sendNumTotal: 0,
      sendNumQuery: {
        page: 1,
        limit: 10
      },
      // 抽奖机会明细
      loading3: false,
      getJoinNumDialog: false,
      getJoinNumList: [],
      getJoinNumTotal: 0,
      getJoinNumQuery: {
        page: 1,
        limit: 10
      },
      // 抽奖次数
      loading4: false,
      joinNumDialog: false,
      joinNumList: [],
      joinNumTotal: 0,
      joinNumQuery: {
        page: 1,
        limit: 10
      },
      // 停用弹框
      stopVisible: false,
      currentOperationRow: {},
      // 1-抽奖次数弹窗 2-中奖次数弹窗
      pageType: 1,
      // 兑付 1-单个兑付 2-兑付当前页 3-兑付全部
      cashType: 1,
      show: false,
      cashCurrentRow: {},
      form: {},
      rules: {
        contactor: [{ required: true, message: '请输入联系人姓名', trigger: 'blur' }],
        contactorPhone: [{ required: true, validator: isTel, trigger: 'blur' }],
        regionCode: [{ required: true, message: '请选择收货地址', trigger: 'change' }],
        address: [{ required: true, message: '请输入详细地址', trigger: 'blur' }],
        expressCompany: [{ required: true, message: '请选择快递公司', trigger: 'change' }],
        expressOrderNo: [{ required: true, message: '请输入快递单号', trigger: 'blur' }]
      },
      // 未兑付修改地址
      editShow: false
    };
  },
  activated() {
    this.$log('activated:', this.$route.fullPath)
    this.resetData()
    this.getList()
  },
  methods: {
    resetData () {
      // 根据不同路由地址，返回不同数据
      if (this.$route.fullPath.indexOf('/b2b_lottery_list') != -1) {
        this.platformType = 1
      } else if (this.$route.fullPath.indexOf('/c_lottery_list') != -1) {
        this.platformType = 2
      }
    },
    async getList() {
      this.loading = true
      let query = this.query
      let data = await lotteryActivityQueryListPage(
        query.page,
        query.limit,
        undefined,
        this.platformType,
        query.activityName,
        query.status,
        query.progress,
        undefined,
        query.createUserName,
        query.mobile,
        query.time && query.time.length ? query.time[0] : undefined,
        query.time && query.time.length > 1 ? query.time[1] : undefined,
        query.category,
        query.opRemark
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
        category: 0,
        time: []
      }
    },
    // 查看 operationType: 1-查看 2-修改 3-新增 4-复制
    addClick() {
      this.$router.push({
        name: 'LotteryEdit',
        params: {
          operationType: 3,
          platformType: this.platformType
        }
      });
    },
    // 查看 operationType: 1-查看 2-修改 3-新增 4-复制
    showDetail(row) {
      // 跳转详情
      this.$router.push({
        name: 'LotteryEdit',
        params: {
          id: row.id,
          operationType: 1,
          platformType: this.platformType
        }
      });
    },
    // 修改
    editClick(row) {
      this.$router.push({
        name: 'LotteryEdit',
        params: {
          id: row.id,
          operationType: 2,
          platformType: this.platformType
        }
      });
    },
    // 复制
    async copyClick(row) {
      this.$common.showLoad()
      let data = await lotteryActivityCopyLottery(row.id)
      this.$common.hideLoad()
      if (data) {
        this.$common.n_success('复制成功');
        // 进入修改
        this.$router.push({
          name: 'LotteryEdit',
          params: {
            id: data,
            operationType: 4,
            platformType: this.platformType
          }
        })
      } 

    },
    // 复制链接
    urlClick(row) {
      if (row.id != '' && row.id != undefined && row.id !== null) {
        let cInput = document.createElement('input');
        if (this.platformType == 1) {
          cInput.value = process.env.VUE_APP_H5_URL + '/#/active/luckyDraw/app?activeId=' + row.id;
        } else {
          cInput.value = process.env.VUE_APP_H5_URL + '/#/active/luckyDraw/wechat?activeId=' + row.id;
        }
        document.body.appendChild(cInput);
        cInput.select();
        document.execCommand('Copy');
        this.$common.n_success('复制链接成功')
        cInput.remove();
      }
    },
    // 关联活动明细
    unionActivityNumClick(row) {
      this.unionActivityNumDialog = true
      this.currentOperationRow = row
      this.sendNumTotalListPageListMethod()
    },
    async sendNumTotalListPageListMethod() {
      this.loading2 = true
      let sendNumQuery = this.sendNumQuery
      let data = await strategyActivityLotteryPageList(
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
    // 抽奖机会明细
    getJoinNumClick(row) {
      this.getJoinNumDialog = true
      this.currentOperationRow = row
      this.lotteryActivityQueryLotteryGetPageMethod()
    },
    async lotteryActivityQueryLotteryGetPageMethod() {
      this.loading3 = true
      let getJoinNumQuery = this.getJoinNumQuery
      let data = await lotteryActivityQueryLotteryGetPage(
        getJoinNumQuery.page,
        getJoinNumQuery.limit,
        this.currentOperationRow.id
      );
      this.loading3 = false
      if (data) {
        this.getJoinNumList = data.records
        this.getJoinNumTotal = data.total
      }
    },
    // 抽奖次数/中奖次数
    joinNumClick(pageType, row) {
      this.pageType = pageType
      this.joinNumDialog = true
      this.currentOperationRow = row
      this.joinNumQuery = {
        page: 1,
        limit: 10
      }
      this.lotteryActivityQueryJoinDetailPageMethod()
    },
    async lotteryActivityQueryJoinDetailPageMethod() {
      this.loading4 = true
      let joinNumQuery = this.joinNumQuery
      let data = await lotteryActivityQueryJoinDetailPage(
        joinNumQuery.page,
        joinNumQuery.limit,
        this.pageType,
        this.currentOperationRow.id
      );
      this.loading4 = false
      if (data) {
        this.joinNumList = data.records
        this.joinNumTotal = data.total
      }
    },
    // 兑付点击
    cashClick(cashType, row) {
      if (cashType == 1) {
        this.cashCurrentRow = row
        // 修改
        if (row.rewardType == 1) {
          this.form = {
            contactor: row.contactor,
            contactorPhone: row.contactorPhone,
            provinceCode: row.provinceCode,
            cityCode: row.cityCode,
            regionCode: row.regionCode,
            address: row.address,
            expressCompany: row.expressCompany,
            expressOrderNo: row.expressOrderNo
          }
        } else {
          this.form = {}
        }
      }
      this.cashType = cashType
      this.show = true
    },
    cashConfirm() {
      if (this.cashType == 1) {
        // 真实物品
        if (this.cashCurrentRow.rewardType == 1) {
          this.$refs['dataForm'].validate((valid) => {
            if (valid) {
              // 修改
              if (this.cashCurrentRow.status == 1) {
                this.lotteryActivityUpdateCashInfoMethod()
              } else {
                this.lotteryActivityCashRewardMethod()
              }
              
            } else {
              return false
            }
          })
        } else {
          this.lotteryActivityCashRewardMethod()
        }
        
      } else {
        this.lotteryActivityCashRewardMethod()
      }
      
    },
    // 兑付奖品确认
    async lotteryActivityCashRewardMethod() {
      
      let addParams = {
        cashType: this.cashType,
        lotteryActivityId: this.currentOperationRow.id
      }
      if (this.cashType == 1) {
        addParams.id = this.cashCurrentRow.id
        // 真实物品
        if (this.cashCurrentRow.rewardType == 1) {
          addParams.activityReceiptInfo = this.form
        }
      } else if (this.cashType == 2) {
        addParams.current = this.joinNumQuery.page
        addParams.size = this.joinNumQuery.limit
      } 
      this.$common.showLoad()
      let data = await lotteryActivityCashReward(addParams)
      this.$common.hideLoad()
      if (typeof data !== 'undefined') {
        this.show = false
        this.$common.n_success('兑付成功')
        this.lotteryActivityQueryJoinDetailPageMethod()
      }
    },
    // 兑付奖品修改确认
    async lotteryActivityUpdateCashInfoMethod() {
      let addParams = this.form
      addParams.joinDetailId = this.cashCurrentRow.id
      this.$common.showLoad()
      let data = await lotteryActivityUpdateCashInfo(addParams)
      this.$common.hideLoad()
      if (typeof data !== 'undefined') {
        this.show = false
        this.editShow = false
        this.$common.n_success('修改成功')
        this.lotteryActivityQueryJoinDetailPageMethod()
      }
    },
    // 未兑付修改地址
    editAddressClick(row) {
      this.cashCurrentRow = row
      this.editShow = true
      this.form = {
        contactor: row.contactor,
        contactorPhone: row.contactorPhone,
        provinceCode: row.provinceCode,
        cityCode: row.cityCode,
        regionCode: row.regionCode,
        address: row.address
      }
    },
    // 未兑付修改地址确认
    editAddressConfirm() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.lotteryActivityUpdateCashInfoMethod()
        } else {
          return false
        }
      })
    },
    // 停用点击
    stopClick(row) {
      this.currentOperationRow = row
      this.stopVisible = true
    },
    async stopConfirm() {
      this.$common.showLoad()
      let data = await lotteryActivityStop(this.currentOperationRow.id)
      this.$common.hideLoad()
      if (typeof data != 'undefined') {
        this.$common.n_success('停用成功')
        this.stopVisible = false
        this.getList()
      }
    },
    // 导出 抽奖次数、中奖次数
    async downLoadTemp() {
      this.$common.showLoad()
      let fileName = this.pageType == 1 ? '导出抽奖次数' : '导出中奖次数'
      let groupName = this.pageType == 1 ? '抽奖次数导出' : '中奖次数导出'
      let menuName = this.pageType == 1 ? '抽奖次数' : '中奖次数'
      let data = await createDownLoad({
        className: 'lotteryActivityJoinDetailExportService',
        fileName: fileName,
        groupName: groupName,
        menuName: menuName,
        searchConditionList: [
          {
            desc: '页面类型',
            name: 'pageType',
            value: this.pageType
          },
          {
            desc: '活动id',
            name: 'lotteryActivityId',
            value: this.currentOperationRow.id
          }
        ]
      })
      this.$common.hideLoad()
      if (typeof data != 'undefined') {
        this.$common.n_success('创建下载任务成功，请在下载中心查看')
      }
    },
    // 导出
    async downLoadTemp1() {
      this.$common.showLoad()
      let data = await createDownLoad({
        className: 'lotteryActivityGetExportService',
        fileName: '导出抽奖机会明细',
        groupName: '抽奖机会明细导出',
        menuName: '抽奖机会明细',
        searchConditionList: [
          {
            desc: '活动id',
            name: 'lotteryActivityId',
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
