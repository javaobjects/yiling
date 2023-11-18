<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <!-- 内容区 -->
    <div class="app-container-content has-bottom-bar">
      <!-- 头部统计数据 -->
      <div class="top-box">
        <div class="flex-row-left item">
          <div class="line-view"></div>
          <span class="font-size-lg bold">当前渠道商信息</span>
        </div>
        <div class="font-size-base font-import-color item-text">{{ enterpriseInfo.name || '---' }}</div>
        <div class="font-size-base font-title-color item-text">{{ enterpriseInfo.channelId | dictLabel(channelType) }}</div>
        <div class="font-size-base font-title-color item-text">负责该渠道商的业务人员个数：<span style="color:#1790FF;">{{ customerContactCount }}个</span></div>
      </div>
      <!-- 协议基本信息 -->
        <div class="top-box">
          <div class="flex-row-left item">
            <div class="line-view"></div>
            <span class="font-size-lg bold">协议基本信息</span>
          </div>
          <!-- 协议类型 -->
          <div class="detail-item">
            <div class="detail-item-left font-title-color">{{ protocolType == 1 ? '年度协议' : '补充协议' }}</div>
            <div></div>
          </div>
          <!-- 修改原因 -->
          <div class="my-form-item2">
            <div class="my-form-item-top">修改原因</div>
            <el-input type="textarea" v-model="remark" placeholder="请填写修改原因" maxlength="100" show-word-limit></el-input>
          </div>
          <!-- 选择协议主体 -->
          <div class="detail-item" v-show="protocolType == 1">
            <div class="detail-item-left font-title-color">协议主体 :</div>
            <div class="detail-item-right">{{ agreementInfo.ename }}</div>
          </div>
          <!-- 选择/更换年度协议 -->
          <div class="detail-item" v-show="protocolType == 2">
            <div class="detail-item-left font-title-color">年度协议 :</div>
            <div class="detail-item-right">{{ agreementInfo.parentName }}</div>
          </div>
          <!-- 选择/更换第三方 -->
          <div class="detail-item" v-show="protocolType == 2 && agreementInfo.mode == 2">
            <div class="detail-item-left font-title-color">第三方 :</div>
            <div class="detail-item-right">{{ agreementInfo.thirdName }}</div>
          </div>
          <!-- 协议名称 -->
          <div class="my-form-item2">
            <div class="my-form-item-top">协议名称</div>
            <el-input v-model="name" placeholder="请填写协议名称"></el-input>
          </div>
          <!-- 协议描述 -->
          <div class="my-form-item2">
            <div class="my-form-item-top">协议描述</div>
            <el-input type="textarea" v-model="content" placeholder="请填写协议描述" maxlength="100" show-word-limit></el-input>
          </div>
          <!-- 履约时间 -->
          <div class="detail-item">
            <div class="detail-item-left font-title-color">履约时间 :</div>
            <div class="detail-item-right">{{ agreementInfo.startTime | formatDate }} - {{ agreementInfo.endTime | formatDate }}</div>
          </div>
          <!-- 协议类型 -->
          <div class="detail-item" v-if="protocolType == 2">
            <div class="detail-item-left font-title-color">协议描述 :</div>
            <div class="detail-item-right">{{ type == 1 ? '购进类' : '其他' }}</div>
          </div>
          <!-- 数据直连 -->
          <div class="detail-item" v-if="type == 2">
            <div class="detail-item-left font-title-color"></div>
            <div class="detail-item-right">
              <div v-if="childType == 4">数据直连</div>
              <div v-if="childType == 5">破损返利</div>
            </div>
          </div>
        </div>
        <!-- 设置返利条件 -->
        <div class="top-box" v-if="protocolType == 2">
          <div class="flex-row-left item">
            <div class="line-view"></div>
            <span class="font-size-lg bold">设置返利条件</span>
          </div>
          <div v-if="type == 1">
            <!-- 商品相关 -->
            <div class="my-form-item mar-t-20">
              <div class="my-form-item-left">商品相关：</div>
              <div class="my-form-item-right">
                <el-radio-group v-model="isPatent" disabled>
                  <el-radio :label="0">全品种</el-radio>
                  <el-radio :label="2">专利药品</el-radio>
                  <el-radio :label="1">非专利药品</el-radio>
                </el-radio-group>
              </div>
            </div>
            <!-- 采购相关： -->
            <div class="my-form-item mar-t-20">
              <div class="my-form-item-left">采购相关：</div>
              <div class="my-form-item-right">
                <el-radio-group v-model="childType" disabled>
                  <el-radio :label="1">购进额</el-radio>
                  <el-radio :label="2">购进量</el-radio>
                  <el-radio :label="3">回款额</el-radio>
                </el-radio-group>
              </div>
            </div>
            <div class="my-form-item mar-t-20">
              <div class="my-form-item-left"></div>
              <div class="my-form-item-right">
                <el-radio-group v-model="conditionRule" disabled>
                  <el-radio v-for="item in gradientArray[childType-1]" :label="item.label" :key="item.value">{{ item.value }}</el-radio>
                </el-radio-group>
              </div>
            </div>
            <!-- 需购进：条件总额 -->
            <div class="my-form-item mar-t-20 flex-row-left" v-if="conditionRule != 4 && conditionRule != 5">
              <div class="my-form-item-left">{{ unitobj.label }}：</div>
              <div class="my-form-item-right flex-row-left">
                <el-input v-model="totalAmount" @keyup.native="totalAmount = onInput(totalAmount, childType == 2 ? 0 : 2)" @change="totalAmountInputChange" placeholder="请输入"></el-input>
              </div>
              <div class="font-size-base" style="margin-left:9px;">{{ unitobj.unit }}商品</div>
            </div>
            <!-- 按季度 按月份 -->
            <div class="my-table mar-t-10" v-if="conditionRule == 2 || conditionRule == 3">
              <div class="my-table-col" >
                <div class="my-table-row row-time">
                  <div v-if="conditionRule == 2">月份</div>
                  <div v-if="conditionRule == 3">季度</div>
                </div>
                <div class="my-table-row">
                  <div>比例</div>
                </div>
                <div class="my-table-row">
                  <div v-if="childType == 2">数量</div>
                  <div v-else>金额</div>
                </div>
              </div>
              <div class="my-table-col" v-for="(item,index) in settingArray" :key="index">
                <div class="my-table-row row-time">{{ item.time }}</div>
                <div class="my-table-row">
                  <el-input class="my-input" v-model="item.proportion" @blur="(e) => proportionInputBlur(e, index)" placeholder="请输入"></el-input>%
                </div>
                <div class="my-table-row">
                  <!-- <el-input class="my-input" v-model="item.num" placeholder="请输入"></el-input> -->
                  {{ item.num || '- -' }}
                </div>
              </div>
            </div>
            <!-- 梯度 -->
            <div class="my-list-view" v-if="conditionRule == 4">
              <div class="my-list-item" v-for="(item,index) in gradient" :key="index">
                <div class="my-list-item-left">第{{ item.label }}梯度：</div>
                <div class="my-list-item-right">
                  <el-input class="my-list-item-input" v-model="item.mixValue" @keyup.native="item.mixValue = onInput(item.mixValue, childType == 1 ? 2 : 0)" placeholder="请输入"></el-input>
                  <span class="separate">至</span>
                  <el-input class="my-list-item-input" v-model="item.maxValue" @keyup.native="item.maxValue = onInput(item.maxValue, childType == 1 ? 2 : 0)" placeholder="请输入"></el-input>
                  <span class="separate">{{ unitStr }}</span>
                  <!-- <span class="separate2">奖励</span>
                  <el-input class="my-list-item-input" placeholder="请输入"></el-input>
                  <span class="separate">%</span> -->
                  <!-- <yl-button v-if="index == 0" class="add-btn" type="text" @click="addGradient">添加梯度</yl-button>
                  <yl-button v-else class="add-btn" type="text" @click="removeGradient">删除</yl-button> -->
                </div>
              </div>
              <div class="my-list-item">
                <div class="my-list-item-left"></div>
                <div class="tip-box">
                  <i class="el-icon-warning"></i>
                  <span>请注意：新增的梯度的值只能比上一个梯度的值大！</span>
                </div>
              </div>
            </div>
            <!-- 按回款时间点 -->
            <div class="my-form-item mar-t-20 flex-row-left" v-if="conditionRule == 5">
              <div class="my-form-item-left">回款日期：</div>
              <div class="my-form-item-right flex-row-left">
                <el-input v-model="timeNode" @input="e => (timeNode = checkInput(e))" @keyup.native="timeNode = onInput(timeNode, 0)" placeholder="请输入"></el-input>
              </div>
              <div class="font-size-base" style="margin-left:9px;">号</div>
            </div>
            <!-- 支付设置： -->
            <div class="my-form-item mar-t-20">
              <div class="my-form-item-left">支付设置：</div>
              <div class="my-form-item-right">
                <el-radio-group v-model="payType" @change="payTypeRadioChange" disabled>
                  <el-radio :label="0">全部</el-radio>
                  <el-radio :label="1">指定</el-radio>
                </el-radio-group>
              </div>
            </div>
            <!-- 支付形式 -->
            <div v-if="payType == 1">
              <div class="my-form-item mar-t-20">
                <div class="my-form-item-left">支付形式：</div>
                <div class="my-form-item-right">
                  <el-checkbox-group v-model="payTypeValues" disabled>
                    <el-checkbox :label="2">账期支付</el-checkbox>
                    <el-checkbox :label="3">预付款支付</el-checkbox>
                  </el-checkbox-group>
                </div>
              </div>
              <div class="my-form-item">
                <div class="my-form-item-left"></div>
                <div class="tip-box">
                    <i class="el-icon-warning"></i>
                    <span>请务必选择，否则将不计算返利金额</span>
                  </div>
              </div>
            </div>
            <!-- 回款设置： -->
            <div class="my-form-item mar-t-20">
              <div class="my-form-item-left">回款设置：</div>
              <div class="my-form-item-right">
                <el-radio-group v-model="backAmountType" @change="backAmountTypeChange" disabled>
                  <el-radio :label="0">全部</el-radio>
                  <el-radio :label="1">指定</el-radio>
                </el-radio-group>
              </div>
            </div>
            <!-- 回款形式 -->
            <div v-if="backAmountType == 1">
              <div class="my-form-item mar-t-20">
                <div class="my-form-item-left">回款形式：</div>
                <div class="my-form-item-right">
                  <el-checkbox-group v-model="backAmountTypeValues" disabled>
                    <el-checkbox :label="1">电汇</el-checkbox>
                    <el-checkbox :label="3">银行承兑</el-checkbox>
                  </el-checkbox-group>
                </div>
              </div>
              <div class="my-form-item">
                <div class="my-form-item-left"></div>
                <div class="tip-box">
                  <i class="el-icon-warning"></i>
                  <span>请务必选择，否则将不计算返利金额</span>
                </div>
              </div>
            </div>
          </div>
          <!-- 返利周期 -->
          <div>
            <div class="my-form-item mar-t-20">
              <div class="my-form-item-left">返利周期：</div>
              <div class="my-form-item-right ">
                <el-radio-group v-model="rebateCycle" class="my-radio-group" disabled>
                  <el-radio :label="1" :disabled="rebateCycleDisable || backAmountType == 1">立即通过订单进行返利</el-radio>
                  <div class="tip-box mar-t-10 mar-b-10">
                    <i class="el-icon-warning"></i>
                    <span>在履约周期内，所有订单直接立享折扣进行购买</span>
                  </div>
                  <el-radio :label="2">以天为单位进行返利计算，返利金额进入返利池，以备后期返利兑付使用</el-radio>
                  <div class="tip-box mar-t-10">
                    <i class="el-icon-warning"></i>
                    <span>在履约周期内，已达标订单进行以天为单位的返利计算，返利金额会进入返利资金池，以备后期返利兑付使用</span>
                  </div>
                </el-radio-group>
              </div>
            </div>
          </div>
          <!-- 返利政策 -->
          <div>
            <div class="my-form-item mar-t-20" v-if="conditionRule != 4 || type == 2">
              <div class="my-form-item-left">返利政策：</div>
              <div class="my-form-item-right">
                <el-radio-group v-model="policyType" disabled>
                  <el-radio :label="1">购进额</el-radio>
                  <el-radio :label="2">回款额</el-radio>
                </el-radio-group>
              </div>
            </div>
            <div class="my-form-item" v-if="conditionRule != 4 || type == 2">
              <div class="my-form-item-left"></div>
              <div class="my-list-item-right font-size-base">
                <span v-if="policyType == 1" class="separate">购进总额的</span>
                <span v-if="policyType == 2" class="separate">回款总额的</span>
                <el-input class="my-list-item-input" v-model="policyValue" @keyup.native="policyValue = onInput(policyValue, 2)" placeholder="请输入"></el-input>
                <span class="separate2">%，作为任务奖励</span>
              </div>
            </div>
            <!-- 按梯度 -->
            <div class="my-list-view" v-if="conditionRule == 4 && type == 1">
              <div class="my-list-item" v-for="(item,index) in gradient" :key="index">
                <div class="my-list-item-left">{{ index == 0 ? '返利政策：': '' }}</div>
                <div class="my-list-item-right">
                   <span class="separate">第{{ item.label }}梯度：</span>
                    <el-input class="my-list-item-input" v-model="item.proportion" @keyup.native="item.proportion = onInput(item.proportion, 2)" placeholder="请输入"></el-input>
                    <span class="separate">%，作为任务奖励</span>
                </div>
              </div>
            </div>

          </div>
          <!-- 返还设置： -->
            <div class="my-form-item mar-t-20" v-if="rebateCycle == 2">
              <div class="my-form-item-left">返还设置：</div>
              <div class="my-form-item-right">
                <el-radio-group v-model="restitutionType" disabled>
                  <el-radio :label="0">全部</el-radio>
                  <el-radio :label="1">指定</el-radio>
                </el-radio-group>
              </div>
            </div>
          <!-- 返还形式 -->
          <div class="my-form-item mar-t-20" v-if="restitutionType == 1 && rebateCycle == 2">
            <div class="my-form-item-left">返还形式：</div>
            <div class="my-form-item-right">
              <el-checkbox-group v-model="restitutionTypeValues" disabled>
                <el-checkbox :label="1">折票</el-checkbox>
                <el-checkbox :label="2">现金（咨询服务费）</el-checkbox>
                <el-checkbox :label="3">冲红</el-checkbox>
                <el-checkbox :label="4">健康城卡</el-checkbox>
              </el-checkbox-group>
            </div>
          </div>
        </div>
      <!-- 可采协议商品 -->
      <div class="top-box">
        <div class="flex-row-left item btn-item">
          <div class="flex-row-left ">
            <div class="line-view"></div>
            <span class="font-size-lg bold">可采协议商品</span>
          </div>
          <yl-button class="view-btn" type="primary" plain @click="addGoodsClick">添加可采商品</yl-button>
        </div>
        <div class="mar-t-10" style="padding-bottom: 10px;background: #FFFFFF;">
            <yl-table
              :stripe="true"
              :show-header="true"
              :list="agreementGoodsList"
              :total="total"
              :page.sync="query.page"
              :limit.sync="query.limit"
              @getList="getBottomGoodsList"
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
                    <div class="font-size-base">{{ row.name || row.goodsName }}</div>
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
        <yl-button type="primary" @click="saveChange">保存</yl-button>
      </div>
    </div>
    <!-- 商品添加 弹框 -->
    <yl-dialog title="商品添加" :visible.sync="addGoodsDialog" :show-footer="false" width="868px">
      <div class="dialog-content-box-customer">
        <div class="dialog-content-top">
          <div class="search-box">
            <el-row class="box">
              <el-col :span="8">
                <div class="title">商品名称</div>
                <el-input v-model="goodsQuery.goodsName" placeholder="请输入商品名称" @keyup.enter.native="goodsHandleSearch" />
              </el-col>
              <el-col :span="8">
                <div class="title">批准文号</div>
                <el-input v-model="goodsQuery.licenseNo" placeholder="请输入批准文号" @keyup.enter.native="goodsHandleSearch" />
              </el-col>
              <el-col :span="8">
                <div class="title">请选择药品分类</div>
                <el-select v-model="goodsQuery.isPatent" placeholder="请选择药品分类" :disabled="isPatentDisable">
                  <el-option
                    v-for="item in isPatentArray"
                    :key="item.label"
                    :label="item.label"
                    :value="item.value">
                  </el-option>
                </el-select>
              </el-col>
            </el-row>
          </div>
          <div class="search-box">
            <el-row class="box">
              <el-col :span="16">
                <yl-search-btn
                  :total="goodsTotal"
                  @search="goodsHandleSearch"
                  @reset="goodsHandleReset"
                />
              </el-col>
            </el-row>
          </div>
        </div>
        <div class="mar-t-10" style="padding-bottom: 10px;background: #FFFFFF;">
          <yl-table
            :stripe="true"
            :show-header="true"
            :list="goodsList"
            :total="goodsTotal"
            :page.sync="goodsQuery.page"
            :limit.sync="goodsQuery.limit"
            :loading="loading1"
            @getList="getList"
          >
            <el-table-column label="序号" min-width="55" align="center">
              <template slot-scope="{ $index }">
                <div class="font-size-base">{{ (goodsQuery.page - 1) * goodsQuery.limit + $index + 1 }}</div>
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
                  <div class="font-size-base">{{ row.name || row.goodsName }}</div>
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
            <el-table-column label="操作" min-width="55" align="center">
              <template slot-scope="{ row }">
                <div>
                  <yl-button class="view-btn" type="text" v-if="!row.goodsDisableVO.agreementDisable" @click="goodsDialogItemClick(row)">添加</yl-button>
                  <yl-button class="edit-btn" v-if="row.goodsDisableVO.agreementDisable" disabled type="text">已添加</yl-button>
                </div>
              </template>
            </el-table-column>
          </yl-table>
        </div>
      </div>
    </yl-dialog>
  </div>
</template>

<script>

import {
  getCustomerEnterpriseInfo,
  getGoodsPopList,
  getGoodsList,
  getYearAgreementsDetail,
  getSupplementAgreementsDetail,
  getGoodsInfoList,// 获取底部商品列表
  agreementAddGoods,
  updateAgreement
} from '@/subject/pop/api/protocol';
import { channelType } from '@/subject/pop/utils/busi';
import { onInputLimit } from '@/common/utils'

export default {
  components: {
  },
  computed: {
    channelType() {
      return channelType();
    },
    // 计算梯度 单位
    unitStr: function () {
      if (this.childType == 1) {
        return '元'
      } else if (this.childType == 2) {
        return '件'
      } else {
        return '天'
      }
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
          title: '修改协议'
        }
      ],
      // 是否是一级商，一级商不需要选三方，二级商需要选三方
      isFirstChannelType: false,
      mode: 1, // 协议类型 1-双方协议 2-三方协议
      enterpriseInfo: {},
      customerContactCount: 0,
      loading: false,
      // 底部可采协议商品列表
      query: {
        page: 1,
        limit: 10
      },
      total: 0,
      agreementGoodsList: [], // 协议商品列表
      // 添加商品弹框
      addGoodsDialog: false,
      isPatentDisable: false,
      // 添加商品弹框的商品条件
      loading1: false,
      goodsQuery: {
        page: 1,
        limit: 10
      },
      goodsTotal: 0,
      goodsList: [],
      //协议类型
      protocolType: 1, // 1-年度协议 2-补充协
      //协议类型
      type: 1, //1-采购类 2-其他
      isPatent: 0, //专利类型 0-全部 1-非专利 2-专利
      childType: 1,//采购类型 1-购进额 2-购进量 3-回款额 4-数据直连 5-破损返利
      otherChildType: 4, // 4-数据直连 5-破损返利
      conditionRule: 1,//	条件规则 1-购进总额 2-月度拆解 3-季度拆解 4-梯度 5-时间点
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
            value: '按回款实际天数梯度'
          },
          {
            label: 5,
            value: '按回款时间点'
          }
        ]
      ],
      // 采购单位对象
      unitobj: {
        label: '需购进',
        unit: '元'
      },
      // 支付设置
      payType: 0,
      payTypeValues: [],
      // 回款设置
      backAmountType: 0,
      backAmountTypeValues: [],
      // 返还设置
      restitutionType: 0,
      restitutionTypeValues: [],
      // 条件总额
      totalAmount: '',
      // 返利政策
      policyType: 1, //协议政策：1-购进额 2-回款额
      policyValue: '',
      rebateCycle: 1, //返利周期 1-订单立返 2-进入返利池
      rebateCycleDisable: false,// 1.订单立返不可用
      timeNode: '', // 回款日期
      // 选择协议主体弹框
      selectMainAgreementDialog: false,
      agreementMainPartList: [],
      remark: '',// 修改原因
      eid: 0, // 协议主体ID（甲方）
      ename: '', //协议主体名字（甲方)
      name: '', //  协议名称
      content: '', // 协议描述
      date: '', // 时间
      //采购设置梯度数组
      settingArray: [],
      // 按梯度设置
      gradient: [
        {
          label: '一',
          mixValue: '',
          maxValue: '',
          proportion: ''
        }
      ],
      quarterLabelArray: ['一', '二', '三', '四', '五', '六', '七', '八', '九', '十', '十一', '十二', '十三', '十四', '十五', '十六', '十七', '十八', '十九', '二十'],
      checkList: [],
      // 协议信息
      agreementInfo: {},
      // 协议返利条件id
      conditionId: ''
    };
  },
  mounted() {
    this.params = this.$route.params;
    this.protocolType = this.params.agreementType
    // 获取企业信息
    this.getCustomerEnterpriseInfoMethod()
    // 获取协议详情
    if (this.params.agreementType == 1){
      // 获取年度协议信息
      this.getYearAgreementsDetailMethod()
    } else {
      // 获取补充协议信息
      this.getSupplementAgreementsDetailMethod()
    }
    // 获取底部商品列表
    this.getBottomGoodsList()
  },
  methods: {
    // 支付设置
    payTypeRadioChange(type) {
      if (type == 0) {
        this.payTypeValues = []
      }
    },
    // 回款设置
    backAmountTypeRadioChange(type) {
      if (type == 0) {
        this.backAmountTypeValues = []
      }
    },
    // 返还设置
    restitutionTypeRadioChange(type) {
      if (type == 0) {
        this.restitutionTypeValues = []
      }
    },
    // 获取渠道商信息
    async getCustomerEnterpriseInfoMethod() {
      let params = this.params
      let data = await getCustomerEnterpriseInfo(
        params.customerEid
      );
      this.enterpriseInfo = data.enterpriseInfo
      this.customerContactCount = data.customerContactCount
      this.$log('this.dataList:', data);
    },
    // 获取年度协议信息
    async getYearAgreementsDetailMethod() {
      let params = this.params
      let data = await getYearAgreementsDetail(
        params.customerEid,
        params.agreementId
      );
      this.agreementInfo = data

      this.name = data.name || ''
      this.content = data.content || ''

      this.$log('this.dataList:', data);
    },
    // 获取补充协议详情
    async getSupplementAgreementsDetailMethod() {
      let params = this.params
      let data = await getSupplementAgreementsDetail(
        params.agreementId
      );
      this.agreementInfo = data

      this.name = data.name || ''
      this.content = data.content || ''
      this.type = data.type
      this.childType = data.childType

      if (data.type == 1){
        this.conditionRule = data.agreementsCondition.conditionRule
        this.isPatent = data.agreementsCondition.isPatent
        if (this.isPatent != 0) {
          // 设置药品分类禁用
          // this.isPatentDisable = true
        }

        if (data.agreementsCondition.conditionRule == 1) {
          this.totalAmount = data.agreementsCondition.totalAmount.toString()
          this.conditionId = data.agreementsCondition.conditions[0].id
        }

        if (data.agreementsCondition.conditionRule == 2 || data.agreementsCondition.conditionRule == 3) {
          let settingArray = []
          this.totalAmount = data.agreementsCondition.totalAmount.toString()
          let conditions = data.agreementsCondition.conditions || []
          let labelArray = new Array('一', '二', '三', '四');
          for (let i = 0; i < conditions.length; i++){
              let item = conditions[i]
              let obj = {}
              if (data.agreementsCondition.conditionRule == 2 ) {
                obj.time = item.rangeNo + '月'
              } else {
                if (item.rangeNo >= 1){
                  obj.time = '第' + labelArray[item.rangeNo-1] + '季度'
                }
              }
              obj.proportion = item.percentage.toString()
              obj.num = item.amount.toString()
              obj.id = item.id
              obj.rangeNo = item.rangeNo
              settingArray.push(obj)
          }
          this.settingArray = settingArray
        }

        if (data.agreementsCondition.conditionRule == 4) {
          let policys = data.policys
          let gradient = []
          for (let i = 0; i < policys.length; i++){
            let item = policys[i]
            let obj = {}

            obj.id = item.id
            obj.rangeNo = item.rangeNo

            obj.label = this.quarterLabelArray[i]
            obj.mixValue = item.mixValue.toString()
            obj.maxValue = item.maxValue.toString()
            obj.proportion = item.policyValue.toString()
            gradient.push(obj)

          }
          this.gradient = gradient
        } else {
          this.policyType = data.policys[0].policyType
          this.policyValue = data.policys[0].policyValue.toString()
        }

        if (data.agreementsCondition.conditionRule && data.agreementsCondition.conditionRule == 5){
          this.timeNode = data.agreementsCondition.conditions[0].timeNode.toString()
          this.conditionId = data.agreementsCondition.conditions[0].id
        }

        // 设置支付形式
        this.payType = data.agreementsCondition.payType
        this.payTypeValues = data.agreementsCondition.payTypeValues

        // 设置回款形式
        this.backAmountType = data.agreementsCondition.backAmountType
        this.backAmountTypeValues = data.agreementsCondition.backAmountTypeValues

        // 返利周期
        this.rebateCycle = data.rebateCycle

      } else { // 其它
        // 返利周期
        this.rebateCycle = data.rebateCycle

        if (data.policys && data.policys.length > 0) {
          this.policyType = data.policys[0].policyType
          this.policyValue = data.policys[0].policyValue.toString()
          this.conditionId = data.policys[0].id
        }
      }
      // 设置返还形式
      if (this.rebateCycle == 2){
          this.restitutionType = data.restitutionType
          this.restitutionTypeValues = data.restitutionTypeValues
        } else {
          this.restitutionType = 0
          this.restitutionTypeValues = []
        }

    },
    // 获取底部可采商品列表
    async getBottomGoodsList() {
      this.loading = true;
      let params = this.params
      let query = this.query;
      let data = await getGoodsInfoList(
        query.page,
        query.limit,
        params.agreementId
      );
      this.loading = false;
      this.agreementGoodsList = data.records;
      this.total = data.total;

      this.$log('this.dataList:', this.dataList);
    },
    // 添加商品弹框点击
    addGoodsClick() {
      this.goodsQuery = {
        page: 1,
        limit: 10
      };
      this.addGoodsDialog = true
      this.getList()
    },
    // 获取年度协议 所有可采商品列表
    async getList() {
      this.loading1 = true;
      let goodsQuery = this.goodsQuery;

      if (this.protocolType == 1){ // 1-年度协议 2-补充协
        let params = this.params
        let data = await getGoodsPopList(
          goodsQuery.page,
          goodsQuery.limit,
          this.agreementInfo.eid,
          params.agreementId,
          goodsQuery.goodsName,
          goodsQuery.licenseNo,
          goodsQuery.isPatent
        );
        this.loading1 = false;

        this.goodsList = data.records;
        this.goodsTotal = data.total;

        this.$log('this.dataList:', this.goodsList);

      } else {
        let params = this.params
        let data = await getGoodsList(
          goodsQuery.page,
          goodsQuery.limit,
          this.agreementInfo.parentId,
          params.agreementId,
          goodsQuery.goodsName,
          goodsQuery.licenseNo,
          goodsQuery.isPatent
        );
        this.loading1 = false;

        this.goodsList = data.records;
        this.goodsTotal = data.total;

        this.$log('this.dataList:')
      }

    },
    // 商品添加弹框中 每一个商品点击
    goodsDialogItemClick(row) {
      this.agreementAddGoodsMethod(row)
    },
    // 添加商品
    async agreementAddGoodsMethod(row) {
      this.$log('row:', row);
      this.$common.showLoad();
      let params = this.params
      let good = {
        ...row,
        goodsId: row.goodsId || row.id,
        goodsName: row.goodsName || row.name
      }
      let addParams = {
        id: params.agreementId,
        agreementGoodsList: [
          good
        ]
      }
      let data = await agreementAddGoods(addParams);
      this.$common.hideLoad();
      if (data && data.result) {
        this.$common.n_success('添加成功');
        this.getList()
        this.getBottomGoodsList()
      }
    },
    goodsHandleSearch() {
      this.goodsQuery.page = 1
      this.getList();
    },
    goodsHandleReset() {
      this.goodsQuery = {
        page: 1,
        limit: 10
      };
    },
    // 回款设置 改变事件
    backAmountTypeChange(backAmountType) {
      if (backAmountType == 1){
        this.rebateCycle = 2
      } else {
        this.rebateCycle = 1
      }
    },
    // 添加梯度
    addGradient() {

      let labelArray = new Array('一', '二', '三', '四', '五', '六', '七', '八', '九', '十', '十一', '十二', '十三', '十四', '十五', '十六', '十七', '十八', '十九', '二十');
      let gradient = this.gradient
      if (gradient.length >= 10) {
        this.$common.warn('最多可添加十个梯度')
        return
      }
      // let lastObj = gradient[gradient.length - 1]
      // eslint-disable-next-line no-new-object
      let newObj = new Object();
      newObj.label = labelArray[gradient.length]
      newObj.mixValue = ''
      newObj.maxValue = ''
      this.gradient = [...gradient, newObj]
    },
    // 删除梯度
    removeGradient() {
      let gradient = this.gradient
      gradient.pop()
      this.gradient = gradient
    },
    // 保存按钮
    saveChange() {
      if (this.checkInputData()) {
        this.updateAgreementMethod()
      }
    },
    // 更新协议
    async updateAgreementMethod() {
      let params = this.params
      let addParams = {
        id: params.agreementId,
        remark: this.remark,
        name: this.name,
        content: this.content
      }
      if (this.protocolType == 2){ // 补充协议
        // 校验输入框数据
        if (this.type == 1){ // 购进类 totalAmount policyValue
          if (this.conditionRule == 1 || this.conditionRule == 2 || this.conditionRule == 3){
            if (this.totalAmount.trim() == ''){
              this.$common.warn(`请输入${this.unitobj.label}`)
              return false
            }
            let str = ''
            if (this.policyType == 1) {
              str = '购进总额'
            } else {
              str = '回款总额'
            }
            if (this.policyValue.trim() == ''){
              this.$common.warn(str)
              return false
            }
          }
          if (this.conditionRule == 4){// 梯度
            let hasEmpty = false
            let hasEmpty1 = false // 返利政策输入框
            let lastMaxValue = -1
            let checkGradient = false
            for (let i = 0; i < this.gradient.length; i++){
              let item = this.gradient[i]
              if (item.mixValue.trim() == '' || item.maxValue.trim() == ''){
                hasEmpty = true
              } else {
                // eslint-disable-next-line no-empty
                if (Number(item.mixValue) > lastMaxValue && Number(item.maxValue) > Number(item.mixValue)) {
                } else {
                  checkGradient = true
                }
                lastMaxValue = Number(item.maxValue)
              }
              if (item.proportion.trim() == '') {
                hasEmpty1 = true
              }
            }
            if (hasEmpty) {
              this.$common.warn('请确保输入的梯度数据正确')
              return false
            }
            if (checkGradient) {
              this.$common.warn('新增的梯度的值只能比上一个梯度的值大!')
              return false
            }
            if (hasEmpty1) {
              this.$common.warn('请输入返利政策梯度')
              return false
            }

          }
          if (this.conditionRule == 5){// 回款日
            if (this.timeNode.trim() == ''){
              this.$common.warn('请输入回款日')
              return false
            }
            let str = ''
            if (this.policyType == 1) {
              str = '购进总额'
            } else {
              str = '回款总额'
            }
            if (this.policyValue.trim() == ''){
              this.$common.warn(str)
              return false
            }
          }

        } else {
          let str = ''
            if (this.policyType == 1) {
              str = '请输入购进总额'
            } else {
              str = '请输入回款总额'
            }
            if (this.policyValue.trim() == ''){
              this.$common.warn(str)
              return false
            }
        }

        // 补充协议参数
        let conditionFormList = []
        if (this.type == 1){ // 购进类
          if (this.conditionRule == 1){
            let obj = {}
            obj.id = this.conditionId
            obj.totalAmount = Number(this.totalAmount)
            obj.amount = Number(this.totalAmount)

            obj.policyValue = Number(this.policyValue)
            conditionFormList.push(obj)
          } else if (this.conditionRule == 2 || this.conditionRule == 3){ // 按月度和季度拆解 需满足总共 100%

            let totalProportion = 0
            for (let i = 0; i < this.settingArray.length; i++){
              let item = this.settingArray[i]
              let obj = {}
              obj.totalAmount = Number(this.totalAmount)

              obj.rangeNo = item.rangeNo
              obj.id = item.id
              obj.amount = Number(item.num)
              obj.percentage = Number(item.proportion)

              obj.policyValue = Number(this.policyValue)
              conditionFormList.push(obj)

              totalProportion += Number(item.proportion)

            }
            if (totalProportion != 100){
              this.$common.warn('拆解比例总共必须满足100%')
              return false
            }

          } else if (this.conditionRule == 4) { // 梯度
            for (let i = 0; i < this.gradient.length; i++){
              let item = this.gradient[i]
              let obj = {}

              obj.rangeNo = item.rangeNo
              obj.id = item.id

              obj.mixValue = Number(item.mixValue)
              obj.maxValue = Number(item.maxValue)
              obj.policyValue = Number(item.proportion)
              conditionFormList.push(obj)

            }

          } else if (this.conditionRule == 5) { // 时间点
            let obj = {}
            obj.id = this.conditionId
            obj.timeNode = Number(this.timeNode)

            obj.policyValue = Number(this.policyValue)
            conditionFormList.push(obj)

          }
        } else { // 其它
            let obj = {}
            obj.policyValue = Number(this.policyValue)
            obj.id = this.conditionId
            conditionFormList.push(obj)
        }
        addParams.conditionFormList = conditionFormList
      }

      this.$common.showLoad();
      let data = await updateAgreement(
        addParams
      );
      this.$common.hideLoad();
      if (data && data.result) {
        this.$common.n_success('修改成功');
        this.$router.go(-1)
      }
    },
    //  校验数据
    checkInputData() {
      if (this.remark.trim() == ''){
        this.$common.warn('请填写修改原因')
        return false
      } else if (this.name.trim() == '') {
        this.$common.warn('请填写协议名称')
        return false
      } else {
        return true
      }
    },
    // 购进总额 总量改变
    totalAmountInputChange(value) {
      for (let i = 0; i < this.settingArray.length; i++) {
        let item = this.settingArray[i]
        item.proportion = ''
        item.num = ''
      }
    },
    // 输入比例
    proportionInputBlur(e, index) {
      let totalAmount = Number(this.totalAmount)
      let value = e.currentTarget.value
      let item = this.settingArray[index]
      let num1 = this.$common.mul(value, totalAmount)
       let num2 = this.$common.div(num1, 100)
      item.num = this.$common.interceptNumber(num2, 2)
    },
    // 校验两位小数
    onInput(value, limit) {
      return onInputLimit(value, limit)
    },
    checkInput(val) {
      val = val.replace(/[^0-9]/gi, '')
      if (val > 28) {
        val = '28'
      }
      if (val < 1) {
        val = ''
      }
      return val
    }
  }
};
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
<style lang="scss">
.my-input {
  .el-input__inner{
    text-align: center;
    border: none !important;
    height: 2.4vw;
  }
}
</style>

