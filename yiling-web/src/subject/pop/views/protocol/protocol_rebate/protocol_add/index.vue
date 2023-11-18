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
        <div class="my-form-item">
          <div class="my-form-item-right">
            <el-radio-group v-model="protocolType" @change="mainProtocolTypeChange">
              <el-radio :label="1">年度协议</el-radio>
              <el-radio :label="2">补充协议</el-radio>
            </el-radio-group>
          </div>
        </div>
        <!-- 选择协议主体 -->
        <div class="my-form-item2" v-if="protocolType == 1">
          <yl-button type="text" @click="selectMainAgreementClick()">选择/更换协议主体</yl-button>
          <div class="font-title-color font-size-base">{{ ename }}</div>
        </div>
        <!-- 选择/更换年度协议 -->
        <div class="my-form-item2" v-if="protocolType == 2">
          <yl-button type="text" @click="selectYearAgreementClick()">选择/更换年度协议</yl-button>
          <div class="font-title-color font-size-base">{{ currentYearAgreement.name }}</div>
        </div>
        <!-- 协议类型 -->
        <div v-if="protocolType == 2" class="my-form-item mar-t-20">
          <div class="my-form-item-left" style="width:100px;">补充协议类型：</div>
          <div class="my-form-item-right">
            <el-radio-group v-model="mode" @change="modeTypeChange">
              <el-radio :label="1">双方补充协议</el-radio>
              <el-radio :label="2">三方补充协议</el-radio>
            </el-radio-group>
          </div>
        </div>
        <!-- 选择/更换第三方 -->
        <div class="my-form-item2" v-if="protocolType == 2 && mode == 2">
          <yl-button type="text" @click="selectThirdEnterpriseClick()">选择/更换第三方</yl-button>
          <div class="font-title-color font-size-base">{{ currentThirdEnterprise.name }}</div>
        </div>
        <!-- 协议名称 -->
        <div class="my-form-item2 mar-t-20">
          <div class="my-form-item-top">协议名称</div>
          <el-input v-model="name" placeholder="请填写协议名称"></el-input>
        </div>
        <!-- 协议描述 -->
        <div class="my-form-item2">
          <div class="my-form-item-top">协议描述</div>
          <el-input type="textarea" v-model="content" placeholder="请填写协议描述" maxlength="100" show-word-limit></el-input>
        </div>
        <!-- 履约时间 -->
        <div class="my-form-item flex-row-left">
          <div class="my-form-item-left">履约时间：</div>
          <div class="my-form-item-right">
            <el-date-picker
              v-model="date"
              :picker-options="pickerOptions"
              @blur="dateBlur"
              type="daterange"
              format="yyyy/MM/dd"
              value-format="yyyy-MM-dd"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期">
            </el-date-picker>
          </div>
        </div>
        <!-- 协议类型 -->
        <div v-if="protocolType == 2" class="my-form-item mar-t-20">
          <div class="my-form-item-left">协议类型：</div>
          <div class="my-form-item-right">
            <el-radio-group v-model="type" @change="protocolTypeChange">
              <el-radio :label="1">购进类</el-radio>
              <el-radio :label="2">其他</el-radio>
            </el-radio-group>
          </div>
        </div>
        <!-- 数据直连 -->
        <div v-show="type == 2" class="my-form-item mar-t-20">
          <div class="my-form-item-left"></div>
          <div class="my-form-item-right">
            <el-radio-group v-model="otherChildType" @change="otherChildTypeChange">
              <el-radio :label="4">数据直连</el-radio>
              <el-radio :label="5">破损返利</el-radio>
            </el-radio-group>
          </div>
        </div>
        <!-- 返利类型 -->
        <div v-if="protocolType == 2" class="my-form-item mar-t-20">
          <div class="my-form-item-left">返利类型：</div>
          <div class="my-form-item-right">
            <el-radio-group v-model="rebateType">
              <el-radio :label="1">年度返利</el-radio>
              <el-radio :label="2">临时政策返利</el-radio>
            </el-radio-group>
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
              <el-radio-group v-model="isPatent" @change="isPatentTypeChange">
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
              <el-radio-group v-model="childType" @change="childTypeChange">
                <el-radio :label="1">购进额</el-radio>
                <el-radio :label="2">购进量</el-radio>
                <el-radio :label="3">回款额</el-radio>
              </el-radio-group>
            </div>
          </div>
          <div class="my-form-item mar-t-20">
            <div class="my-form-item-left"></div>
            <div class="my-form-item-right">
              <el-radio-group v-model="conditionRule" @change="conditionRuleChange">
                <el-radio v-for="item in gradientArray[childType-1]" :label="item.label" :key="item.value">{{ item.value }}</el-radio>
              </el-radio-group>
            </div>
          </div>
          <!-- 需购进：条件总额 -->
          <div class="my-form-item mar-t-20 flex-row-left" v-if="conditionRule != 4 && conditionRule != 5">
            <div class="my-form-item-left">{{ unitobj.label }}：</div>
            <div class="my-form-item-right flex-row-left">
              <el-input v-if="unitobj.unit == '件'" v-model="totalAmount" @keyup.native="totalAmount = onInput(totalAmount, 0)" @change="totalAmountInputChange" placeholder="请输入"></el-input>
              <el-input v-else v-model="totalAmount" @keyup.native="totalAmount = onInput(totalAmount, 2)" @change="totalAmountInputChange" placeholder="请输入"></el-input>
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
                <el-input class="my-input" v-model="item.proportion" @blur="(e) => proportionInputBlur(e, index)" @keyup.native="item.proportion = onInput(item.proportion, childType == 2 ? 0 : 2)" placeholder="请输入"></el-input>%
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
                <yl-button v-if="index == 0" class="add-btn" type="text" @click="addGradient">添加梯度</yl-button>
                <yl-button v-else class="add-btn" type="text" @click="removeGradient">删除</yl-button>
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
              <el-radio-group v-model="payType" @change="payTypeRadioChange">
                <el-radio :label="0">不指定</el-radio>
                <el-radio :label="1">指定</el-radio>
              </el-radio-group>
            </div>
          </div>
          <!-- 支付形式 -->
          <div v-if="payType == 1">
            <div class="my-form-item mar-t-20">
              <div class="my-form-item-left">支付形式：</div>
              <div class="my-form-item-right">
                <el-checkbox-group v-model="payTypeValues">
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
              <el-radio-group v-model="backAmountType" @change="backAmountTypeChange">
                <el-radio :label="0">不指定</el-radio>
                <el-radio :label="1">指定</el-radio>
              </el-radio-group>
            </div>
          </div>
          <!-- 回款形式 -->
          <div v-if="backAmountType == 1">
            <div class="my-form-item mar-t-20">
              <div class="my-form-item-left">回款形式：</div>
              <div class="my-form-item-right">
                <el-checkbox-group v-model="backAmountTypeValues">
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
              <el-radio-group v-model="rebateCycle" class="my-radio-group" @change="rebateCycleTypeChange">
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
              <el-radio-group v-model="policyType">
                <el-radio :label="1">购进额</el-radio>
                <el-radio :label="2" :disabled="policyTypeDisable || rebateCycle == 1">回款额</el-radio>
              </el-radio-group>
            </div>
          </div>
          <div class="my-form-item" v-if="conditionRule != 4 || type == 2">
            <div class="my-form-item-left"></div>
            <div class="my-list-item-right">
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
              <el-radio-group v-model="restitutionType">
                <el-radio :label="0">不指定</el-radio>
                <el-radio :label="1">指定</el-radio>
              </el-radio-group>
            </div>
          </div>
        <!-- 返还形式 -->
        <div class="my-form-item mar-t-20" v-if="restitutionType == 1 && rebateCycle == 2">
          <div class="my-form-item-left">返还形式：</div>
          <div class="my-form-item-right">
            <el-checkbox-group v-model="restitutionTypeValues">
              <el-checkbox :label="1">票折</el-checkbox>
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
              <el-table-column label="操作" min-width="55" align="center">
              <template slot-scope="{ $index }">
                <div>
                  <yl-button type="text" @click="goodsRemoveClick($index)">移除</yl-button>
                </div>
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
    <!-- 选择协议主体 -->
    <yl-dialog
      width="40%"
      title="选择协议主体"
      :show-footer="false"
      :visible.sync="selectMainAgreementDialog">
      <div class="content">
        <el-radio-group style="width: 100%;" v-model="eid">
          <el-row style="width: 100%;">
            <el-col
              class="mat-b-20"
              v-for="(item, index) in agreementMainPartList"
              :key="index"
              :span="12">
              <el-radio :label="item.id">{{ item.name }}</el-radio>
            </el-col>
          </el-row>
        </el-radio-group>
        <div class="flex-row-center btn">
          <yl-button style="width: 212px;" type="primary" :disabled="eid == null || !eid || eid <= 0" @click="selectMainAgreementSure">
            确定
          </yl-button>
        </div>
      </div>
    </yl-dialog>
    <!-- 商品添加 弹框 -->
    <yl-dialog title="商品添加" :visible.sync="addGoodsDialog" :show-footer="false" width="866px">
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
        <div class="down-box clearfix">
          <div class="btn">
            <ylButton type="primary" plain @click="batchAddClick">批量添加</ylButton>
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
            :page-size-list="[500, 300, 100]"
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
                  <yl-button class="view-btn" type="text" v-if="!row.isSelect" @click="goodsDialogItemClick(row)">添加</yl-button>
                  <yl-button class="edit-btn" v-if="row.isSelect" disabled type="text">已添加</yl-button>
                </div>
              </template>
            </el-table-column>
          </yl-table>
        </div>
      </div>
    </yl-dialog>
    <!-- 选择年度协议 弹框 -->
    <yl-dialog title="选择年度协议" :visible.sync="addYearAgreementDialog" :show-footer="false" width="868px">
      <div class="dialog-content-box-customer">
        <div class="" style="padding-bottom: 10px;background: #FFFFFF;">
          <yl-table
            :list="yearAgreementList"
            :total="yearAgreementTotal"
            :page.sync="agreementQuery.page"
            :limit.sync="agreementQuery.limit"
            :loading="loading2"
            :cell-no-pad="true"
            @getList="getYearAgreementList"
          >
            <el-table-column>
              <template slot-scope="{ row, $index }">
                <div class="purchase-table-view" :key="$index">
                  <div class="list-item">
                    <div class="item-top">
                      <div class="top-left-view">
                        <div class="item font-size-small font-light-color"><span class="font-light-color">协议ID：</span>{{ row.id }}</div>
                        <div class="item font-size-small font-light-color"><span class="font-light-color">协议主体：</span>{{ row.ename }}</div>
                        <div class="item font-size-small font-light-color"><span class="font-light-color">履约时间：</span>{{ row.startTime | formatDate }}-{{ row.endTime | formatDate }}</div>
                      </div>
                      <!-- <div>共有 <span style="color:#1790FF;">{{ row.tempCount }}</span> 个补充协议</div> -->
                    </div>
                    <div class="item-bottom">
                      <div class="item-left">
                        <div class="mar-b-16 title">{{ row.name }}</div>
                        <div class="detail">{{ row.content }}</div>
                      </div>
                      <div class="item-center">
                        <div class="item-center-top">
                          <div class="name-view font-size-small font-light-color">
                            <div class="line-view"></div>
                            创建人：<span class="font-important-color">{{ row.createUserName }}</span>
                          </div>
                          <div class="time-view font-size-small font-light-color">创建时间：<span class="font-important-color">{{ row.createTime | formatDate }}</span></div>
                        </div>
                        <div class="item-center-top">
                          <div class="name-view font-size-small font-light-color">
                            <div class="line-view"></div>
                            修改人：<span class="font-important-color">{{ row.updateUserName }}</span>
                          </div>
                          <div class="time-view font-size-small font-light-color">修改时间：<span class="font-important-color">{{ row.updateTime | formatDate }}</span></div>
                        </div>
                      </div>
                      <div class="item-right flex-column-center">
                        <div>
                          <yl-button v-if="!row.isSelect" type="text" @click="agreementDialogItemClick(row)">添加</yl-button>
                          <yl-button v-if="row.isSelect" disabled type="text">已添加</yl-button>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </template>
            </el-table-column>
          </yl-table>
        </div>
      </div>
    </yl-dialog>
    <!-- 三方弹框 -->
    <yl-dialog
      title="三方列表"
      :visible.sync="thirdEnterpriseDialog"
      :show-footer="false"
      :destroy-on-close="true"
      >
      <div class="channel-content">
        <div class="dialog-content-top">
          <div class="search-box">
            <el-row class="box">
              <el-col :span="8">
                <div class="title">企业ID</div>
                <el-input v-model="thirdEnterpriseQuery.sellerEid" @input="e => (thirdEnterpriseQuery.sellerEid = sellerEidCheckInput(e))" placeholder="请输入企业ID" @keyup.enter.native="thirdEnterpriseHandleSearch" />
              </el-col>
              <el-col :span="8">
                <div class="title">企业名称</div>
                <el-input v-model="thirdEnterpriseQuery.sellerName" placeholder="请输入企业名称" @keyup.enter.native="thirdEnterpriseHandleSearch" />
              </el-col>
            </el-row>
          </div>
          <div class="search-box">
            <el-row class="box">
              <el-col :span="16">
                <yl-search-btn
                  :total="thirdEnterpriseTotal"
                  @search="thirdEnterpriseHandleSearch"
                  @reset="thirdEnterpriseHandleReset"
                />
              </el-col>
            </el-row>
          </div>
        </div>
        <div class="mar-t-10" style="padding-bottom: 10px;background: #FFFFFF;">
          <yl-table
            :list="thirdEnterpriseList"
            :total="thirdEnterpriseTotal"
            :page.sync="thirdEnterpriseQuery.page"
            :limit.sync="thirdEnterpriseQuery.limit"
            :loading1="loading3"
            :cell-no-pad="true"
            @getList="getThirdEnterpriseEntListMethod"
          >
            <el-table-column>
              <template slot-scope="{ row, $index }">
                <div class="purchase-table-view" :key="$index">
                  <div class="list-item">
                    <div class="list-item-left">
                      <div class="item font-size-small bold">{{ row.name }}</div>
                      <div class="item font-size-small font-light-color">
                        <span class="font-light-color">企业 ID：</span>
                        {{
                        row.id }}
                      </div>
                      <div class="item font-size-small font-light-color">
                        <span class="font-light-color">企业地址：</span>
                        {{
                        row.address }}
                      </div>
                    </div>
                    <!-- <div class="list-item-center">协议总数：<span class="agreement-count">{{ row.supplementAgreementCount }}</span> /个</div> -->
                    <div class="list-item-right">
                      <yl-button v-if="!row.isSelect" type="text" @click="thirdEnterpriseItemClick(row)">添加</yl-button>
                      <yl-button v-if="row.isSelect" disabled type="text">已添加</yl-button>
                    </div>
                  </div>
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
  getAgreementMainPartList,
  getGoodsPopList,
  getGoodsList,
  saveYearAgreement,
  getAgreementPageList,
  getThirdEnterpriseList,
  saveTempAgreement
} from '@/subject/pop/api/protocol';
import { channelType } from '@/subject/pop/utils/busi';
import { formatDate } from '@/subject/pop/utils'
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
      selectDate: null,
      pickerOptions: {
        disabledDate: time => {
          if (this.selectDate === null) {
            return false
          } else {
            return (this.selectDate.getFullYear() != time.getFullYear())
          }
        },
        onPick: date => {
          // 如果只选择一个则保存至selectDate 否则selectDate 为空
          if (date.minDate) {
            this.selectDate = date.minDate
          } else {
              this.selectDate = null
          }
        }
      },
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
          title: '新增协议'
        }
      ],
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
        limit: 500
      },
      goodsTotal: 0,
      goodsList: [],
      // 选择年度协议弹框
      loading2: false,
      addYearAgreementDialog: false,
      agreementQuery: {
        page: 1,
        limit: 10
      },
      yearAgreementTotal: 0,
      yearAgreementList: [],
      currentYearAgreement: {},
      // 选择第三方 弹框
      loading3: false,
      thirdEnterpriseDialog: false,
      thirdEnterpriseQuery: {
        page: 1,
        limit: 10
      },
      thirdEnterpriseTotal: 0,
      thirdEnterpriseList: [],
      currentThirdEnterprise: {},
      //协议类型
      protocolType: 1, // 1-年度协议 2-补充协
      //协议类型
      type: 1, //1-采购类 2-其他
      //返利类型
      rebateType: 1, // 1-年度返利 2-临时政策返利
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
      policyTypeDisable: false,// 2.协议政策 回款额不可选
      policyType: 1, //协议政策：1-购进额 2-回款额
      policyValue: '',
      rebateCycle: 1, //返利周期 1-订单立返 2-进入返利池
      rebateCycleDisable: false,// 1.订单立返不可用
      timeNode: '', // 回款日期
      // 选择协议主体弹框
      selectMainAgreementDialog: false,
      agreementMainPartList: [],

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
      checkList: []
    };
  },
  mounted() {
    this.params = this.$route.params;
    // 获取企业信息
    this.getCustomerEnterpriseInfoMethod()
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
    async getCustomerEnterpriseInfoMethod() {
      let params = this.params
      let data = await getCustomerEnterpriseInfo(
        params.customerEid
      );
      this.enterpriseInfo = data.enterpriseInfo
      
      this.customerContactCount = data.customerContactCount
      this.$log('this.dataList:', data.customerContactCount);
    },
    // 选择主体协议点击
    selectMainAgreementClick() {
      this.selectMainAgreementDialog = true
      this.getAgreementMainPartListMethod()
    },
    // 获取协议主体
    async getAgreementMainPartListMethod() {
      let data = await getAgreementMainPartList(
      );
      this.agreementMainPartList = data.list
      this.$log('this.dataList:', data);
    },
    // 选择主体协议确认点击
    selectMainAgreementSure() {
      this.agreementMainPartList.forEach( (item) => {
        if (item.id == this.eid){
          if (item.name != this.ename) {
            this.agreementGoodsList = []
          }
          this.ename = item.name
        }
      })
      this.selectMainAgreementDialog = false
    },
    // 选择年度协议点击
    selectYearAgreementClick() {
      this.addYearAgreementDialog = true
      this.getYearAgreementList()
    },
    async getYearAgreementList() {
      this.loading2 = true;
      let params = this.params
      let agreementQuery = this.agreementQuery;
      let data = await getAgreementPageList(
        agreementQuery.page,
        agreementQuery.limit,
        5,
        params.customerEid
      );

      let arr = data.records || [];
      arr.forEach(item => {
        if (this.currentYearAgreement && JSON.stringify(this.currentYearAgreement) != '{}' && item.id == this.currentYearAgreement.id){
          item.isSelect = true;
        } else {
          item.isSelect = false;
        }
      });

      this.loading2 = false;
      this.yearAgreementList = arr;
      this.yearAgreementTotal = data.total || 0;

      this.$log('this.dataList:', this.yearAgreementList);
    },
    // 年度协议弹框中 每一个年度协议点击
    agreementDialogItemClick(row) {
      this.$log('agreementDialogItemClick:', row);
      this.agreementGoodsList = []
      this.currentYearAgreement = row

      let date = []
      date = [formatDate(row.startTime, 'yyyy-MM-dd'), formatDate(row.endTime, 'yyyy-MM-dd')]
      this.date = date

      let arr = this.yearAgreementList || [];
      arr.forEach(item => {
        if (item.id == row.id){
          item.isSelect = true;
        } else {
          item.isSelect = false;
        }
      });
      this.yearAgreementList = arr
      if (this.protocolType == 2) {
        if (this.conditionRule == 2 || this.conditionRule == 3) {
          this.conditionRuleChange(this.conditionRule)
        }
      }
    },
    // 选择三方确认点击
    selectThirdEnterpriseClick() {
      this.thirdEnterpriseDialog = true
      this.getThirdEnterpriseEntListMethod()
    },
    async getThirdEnterpriseEntListMethod() {
      this.loading3 = true;
      let params = this.params
      let thirdEnterpriseQuery = this.thirdEnterpriseQuery;
      let data = await getThirdEnterpriseList(
        thirdEnterpriseQuery.page,
        thirdEnterpriseQuery.limit,
        params.customerEid,
        thirdEnterpriseQuery.sellerEid,
        thirdEnterpriseQuery.sellerName
      );
      this.loading3 = false;

      let arr = data.records || [];
      arr.forEach(item => {
        if (this.currentThirdEnterprise && JSON.stringify(this.currentThirdEnterprise) != '{}' && item.id == this.currentThirdEnterprise.id){
          item.isSelect = true;
        } else {
          item.isSelect = false;
        }
      });
      this.thirdEnterpriseList = arr;
      this.thirdEnterpriseTotal = data.total || 0;
      this.$log('getThirdEnterpriseEntListMethod:', data);
    },
    thirdEnterpriseHandleSearch() {
      this.thirdEnterpriseQuery.page = 1
      this.getThirdEnterpriseEntListMethod();
    },
    thirdEnterpriseHandleReset() {
      this.thirdEnterpriseQuery = {
        page: 1,
        limit: 10
      }
    },
    thirdEnterpriseItemClick(row) {
      this.$log('thirdEnterpriseItemClick:', row);
      this.currentThirdEnterprise = row

      this.mode = 2

      let arr = this.thirdEnterpriseList || [];
      arr.forEach(item => {
        if (item.id == row.id){
          item.isSelect = true;
        } else {
          item.isSelect = false;
        }
      });
      this.thirdEnterpriseList = arr
    },
    // 添加商品弹框点击
    addGoodsClick() {
      if (this.protocolType == 1){
        if (this.eid == 0){
          this.$common.warn('请选择协议主体')
          return false
        }
      } else {
        if (JSON.stringify(this.currentYearAgreement) == '{}'){
          this.$common.warn('请选择年度协议')
          return false
        }
      }
      if (this.type == 1 && this.isPatent != 0) {
        this.goodsQuery = {
          page: 1,
          limit: 500,
          isPatent: this.isPatent
        };
        this.isPatentDisable = true
      } else {
        this.goodsQuery = {
          page: 1,
          limit: 500
        };
        this.isPatentDisable = false
      }
      this.addGoodsDialog = true
      this.getList()
    },
    // 获取年度协议 所有可采商品列表
    async getList() {
      this.loading1 = true;
      let goodsQuery = this.goodsQuery;

      if (this.protocolType == 1){ // 1-年度协议 2-补充协
        let data = await getGoodsPopList(
          goodsQuery.page,
          goodsQuery.limit,
          this.eid,
          0,
          goodsQuery.goodsName,
          goodsQuery.licenseNo,
          goodsQuery.isPatent
        );
        this.loading1 = false;

        data.records.forEach(item => {
          let hasIndex = this.agreementGoodsList.findIndex(obj => {
            return obj.id == item.id;
          });
          if (hasIndex != -1) {
            item.isSelect = true;
          } else {
            item.isSelect = false;
          }
        });

        this.goodsList = data.records;
        this.goodsTotal = data.total;

        this.$log('this.dataList:', this.goodsList);

      } else {
        let data = await getGoodsList(
          goodsQuery.page,
          goodsQuery.limit,
          this.currentYearAgreement.id,
          undefined,
          goodsQuery.goodsName,
          goodsQuery.licenseNo,
          goodsQuery.isPatent
        );
        this.loading1 = false;

        data.records.forEach(item => {
          let hasIndex = this.agreementGoodsList.findIndex(obj => {
            return obj.id == item.id;
          });
          if (hasIndex != -1) {
            item.isSelect = true;
          } else {
            item.isSelect = false;
          }
        });

        this.goodsList = data.records;
        this.goodsTotal = data.total;

        this.$log('this.dataList:')
      }

    },
    // 商品添加弹框中 每一个商品点击
    goodsDialogItemClick(row) {
      let currentArr = this.agreementGoodsList
      currentArr.unshift(row)
      this.agreementGoodsList = currentArr

      let arr = this.goodsList;
      arr.forEach(item => {
        let hasIndex = currentArr.findIndex(obj => {
          return obj.id == item.id;
        });
        if (hasIndex != -1) {
          item.isSelect = true;
        } else {
          item.isSelect = false;
        }
      });
      this.goodsList = arr;

    },
    // 批量添加商品
    batchAddClick() {
      let arr = [];
      this.goodsList.forEach(item => {
        if (!item.isSelect) { // 没有添加
          arr.push(item);
        }
      });

      let currentArr = this.agreementGoodsList
      if (arr.length > 0) {
        for (let i = 0; i < arr.length; i++) {
          let currentItem = arr[i]
          currentItem.isSelect = true;
          currentArr.unshift(currentItem)
        } 
      }
      this.agreementGoodsList = currentArr
    },
    // 商品移除点击
    goodsRemoveClick(index) {
      this.$common.log(index)
      let arr = this.agreementGoodsList;
      arr.splice(index, 1);
      this.agreementGoodsList = arr;
    },
    goodsHandleSearch() {
      this.goodsQuery.page = 1
      this.getList();
    },
    goodsHandleReset() {
      if (this.isPatent == 0) {
        this.goodsQuery = {
          page: 1,
          limit: 500
        }
      } else {
        this.goodsQuery = {
          page: 1,
          limit: 500,
          isPatent: this.goodsQuery.isPatent
        }
      }

    },
    // 年度协议 补充协议类型切换
    mainProtocolTypeChange(type) {
      // 清空选择商品
      this.agreementGoodsList = []
      // 履约时间
      this.date = ''
      this.isPatent = 0
    },
    // 协议类型
    protocolTypeChange(type) {
      this.$nextTick(() => {
        if (type == 1){// 购进类
          this.isPatent = 0
          this.childType = 1
          this.conditionRule = 1
          this.payType = 0
          this.payTypeValues = []
          this.backAmountType = 0
          this.backAmountTypeValues = []

        } else {//其它
          this.isPatent = 0
          this.otherChildType = 4
          this.childType = 4
          this.rebateCycleDisable = false
          this.rebateCycle = 1
        }
      })
      this.agreementGoodsList = []
    },
    otherChildTypeChange(otherChildType) {
      this.childType = otherChildType
    },
    // 商品相关 改变事件
    isPatentTypeChange(type) {
      this.agreementGoodsList = []
    },
    // 回款设置 改变事件
    backAmountTypeChange(backAmountType) {
      if (backAmountType == 1){
        this.rebateCycle = 2
      } else {
        this.rebateCycle = 1
      }
    },
    // 返利周期
    rebateCycleTypeChange(rebateCycleType) {
      if (rebateCycleType == 1){
        this.policyTypeDisable = true
        this.policyType = 1
        this.re
      } else {
        this.policyTypeDisable = false
      }
    },
    // 采购类型 改变事件
    childTypeChange(childType) {
      this.conditionRule = 1
      // 清空需购进
      this.totalAmount = ''
      if (childType == 1){
        this.unitobj = {
          label: '需购进',
          unit: '元'
        }
        this.rebateCycleDisable = false
        this.rebateCycle = 1

        this.policyTypeDisable = true
        this.policyType = 1
      } else if (childType == 2){
        this.unitobj = {
          label: '需购进',
          unit: '件'
        }
        this.rebateCycleDisable = false
        this.rebateCycle = 1

        this.policyTypeDisable = true
        this.policyType = 1

      } else if (childType == 3){
        this.unitobj = {
          label: '需回款',
          unit: '元'
        }
        this.rebateCycleDisable = true
        this.rebateCycle = 2

        this.policyTypeDisable = false
      }
    },
    // 补充协议类型 改变事件
    modeTypeChange(modeType) {
      if (modeType == 1) {
        this.currentThirdEnterprise = {}
      }
    },
    // 履约时间改变
    dateBlur(e) {
      this.$log('e:', e.value);
      // 补充协议 按月度、季度拆解
      if (this.protocolType == 2) {
        if (this.conditionRule == 2 || this.conditionRule == 3) {
          this.conditionRuleChange(this.conditionRule)
        }
      }
    },
    // 校验是否选择履约时间
    checkDate() {
      if (!this.date || this.date == '') {
        return false
      } else return true
    },
    // 采购梯度 改变事件
    conditionRuleChange(conditionRuleType) {
      
      if (conditionRuleType == 2){//创建月度拆解
        let currentMonth = 0
        let endMonth = 11
        // 选择履约时间
        if (this.checkDate()) {
          let myDate = new Date(Date.parse(this.date[0]));
          currentMonth = myDate.getMonth(); //获取当前月份(0-11,0代表1月)

          let endDate = new Date(Date.parse(this.date[1]));
          endMonth = endDate.getMonth(); //获取当前月份(0-11,0代表1月)
        }
        this.$log('this.dataList:', currentMonth + 1);
        let settingArray = []
        for (let i = currentMonth + 1; i <= endMonth + 1; i++){
          let obj = {}
          obj.time = i + '月'
          obj.proportion = ''
          obj.num = ''
          obj.currentNum = i
          settingArray.push(obj)
        }
        this.settingArray = settingArray

      } else if (conditionRuleType == 3){//创建季度拆解
        let currMonth = 0
        let endMonth = 11
        // 选择履约时间
        if (this.checkDate()) {
          let myDate = new Date(Date.parse(this.date[0]));
          currMonth = myDate.getMonth(); //获取当前月份(0-11,0代表1月)

          let endDate = new Date(Date.parse(this.date[1]));
          endMonth = endDate.getMonth(); //获取当前月份(0-11,0代表1月)
        }
        let currQuarter = Math.floor( ( (currMonth + 1) % 3 == 0 ? ( (currMonth + 1) / 3 ) : ( (currMonth + 1) / 3 + 1 ) ) );
        let endQuarter = Math.floor( ( (endMonth + 1) % 3 == 0 ? ( (endMonth + 1) / 3 ) : ( (endMonth + 1) / 3 + 1 ) ) );
        this.$log('this.dataList:', currQuarter, endQuarter);
        let labelArray = new Array('一', '二', '三', '四');
        let settingArray = []
        for (let i = currQuarter; i <= endQuarter; i++){
          let obj = {}
          obj.time = '第' + labelArray[i - 1] + '季度'
          obj.proportion = ''
          obj.num = ''
          obj.currentNum = i
          settingArray.push(obj)
        }
        this.settingArray = settingArray
      } else if (conditionRuleType == 4){//梯度
        this.gradient = [
          {
            label: '一',
            mixValue: '',
            maxValue: '',
            proportion: ''
          }
        ]
      }

      if (conditionRuleType == 1){
        this.rebateCycleDisable = false
        this.rebateCycle = 1

        this.policyTypeDisable = true
        this.policyType = 1
      } else {
        this.rebateCycleDisable = true
        this.rebateCycle = 2

        this.policyTypeDisable = false
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
      newObj.proportion = ''
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
      if (this.protocolType == 1){// 年度协议
        this.saveYearAgreementMethod()
      } else {// 补充协议
        this.saveTempAgreementMethod()
      }
    },
    // 新增补充协议
    async saveTempAgreementMethod() {
      if (this.checkTempData()) {
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
                this.$common.log(lastMaxValue,Number(item.mixValue),Number(item.maxValue))
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
              this.$common.warn('请输入回款日期')
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
          // 支付设置 回款设置
          if (this.payType == 2 && this.payTypeValues.length == 0) {
            this.$common.warn('请选择指定支付形式')
            return false
          }
          if (this.backAmountType == 2 && this.backAmountTypeValues.length == 0) {
            this.$common.warn('请选择指定回款形式')
            return false
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

        if (this.rebateCycle == 2 && this.restitutionType == 1 && this.restitutionTypeValues.length == 0) {
          this.$common.warn('请选择指定返还形式')
          return false
        }

        let addParams = {
          parentId: this.currentYearAgreement.id,
          name: this.name,
          content: this.content,
          mode: this.mode, //协议类型 1-双方协议 2-三方协议
          startTime: this.date[0],
          endTime: this.date[1],
          type: this.type, // 协议类型 1-采购类 2-其他
          rebateType: this.rebateType, // 返利类型 1-年度返利 2-临时政策返利
          isPatent: this.isPatent, // 专利类型 0-全部 1-非专利 2-专利
          childType: this.childType, //1-购进额 2-购进量 3-回款额 4-数据直连 5-破损返利
          conditionRule: this.conditionRule, // 1-购进总额 2-月度拆解 3-季度拆解 4-梯度 5-时间点
          rebateCycle: this.rebateCycle //返利周期
        }
        if (this.rebateCycle == 2){
          addParams.restitutionType = this.restitutionType
          addParams.restitutionTypeValues = this.restitutionTypeValues
        } else {
          addParams.restitutionType = 0
          addParams.restitutionTypeValues = []
        }
        if (this.mode == 2){
          addParams.thirdChannelName = this.currentThirdEnterprise.channelName
          addParams.thirdEid = this.currentThirdEnterprise.id
          addParams.thirdName = this.currentThirdEnterprise.name
        }

        let agreementConditionList = []
        if (this.type == 1){ // 购进类
          if (this.conditionRule == 1){
            let obj = {}
            obj.totalAmount = Number(this.totalAmount)
            obj.amount = Number(this.totalAmount)
            obj.payType = this.payType
            obj.payTypeValues = this.payTypeValues
            obj.backAmountType = this.backAmountType
            obj.backAmountTypeValues = this.backAmountTypeValues

            obj.policyType = this.policyType
            obj.policyValue = Number(this.policyValue)
            agreementConditionList.push(obj)
          } else if (this.conditionRule == 2 || this.conditionRule == 3){ // 按月度和季度拆解 需满足总共 100%

            let totalProportion = 0
            for (let i = 0; i < this.settingArray.length; i++){
              let item = this.settingArray[i]
              let obj = {}
              obj.totalAmount = Number(this.totalAmount)

              obj.payType = this.payType
              obj.payTypeValues = this.payTypeValues

              obj.backAmountType = this.backAmountType
              obj.backAmountTypeValues = this.backAmountTypeValues
              obj.rangeNo = item.currentNum
              obj.amount = Number(item.num)
              obj.percentage = Number(item.proportion)

              obj.policyType = this.policyType
              obj.policyValue = Number(this.policyValue)
              agreementConditionList.push(obj)

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

              obj.payType = this.payType
              obj.payTypeValues = this.payTypeValues

              obj.backAmountType = this.backAmountType
              obj.backAmountTypeValues = this.backAmountTypeValues
              obj.rangeNo = i + 1

              obj.mixValue = Number(item.mixValue)
              obj.maxValue = Number(item.maxValue)
              obj.policyValue = Number(item.proportion)
              agreementConditionList.push(obj)
            }

          } else if (this.conditionRule == 5) { // 时间点
            let obj = {}
            obj.timeNode = Number(this.timeNode)

            obj.payType = this.payType
            obj.payTypeValues = this.payTypeValues
            obj.backAmountType = this.backAmountType
            obj.backAmountTypeValues = this.backAmountTypeValues

            obj.policyType = this.policyType
            obj.policyValue = Number(this.policyValue)
            agreementConditionList.push(obj)

          }
        } else { // 其它
            let obj = {}

            obj.policyType = this.policyType
            obj.policyValue = Number(this.policyValue)
            agreementConditionList.push(obj)
        }

        let agreementGoodsList = []
        for (let i = 0; i < this.agreementGoodsList.length; i++){
          let item = this.agreementGoodsList[i]
          let obj = {}
          obj.goodsId = item.goodsId
          obj.goodsName = item.name || item.goodsName
          obj.isPatent = item.isPatent
          obj.sellSpecificationsId = item.sellSpecificationsId
          obj.standardId = item.standardId
          obj.licenseNo = item.licenseNo
          obj.sellSpecifications = item.sellSpecifications
          agreementGoodsList.push(obj)
        }

        addParams.agreementConditionList = agreementConditionList
        addParams.agreementGoodsList = agreementGoodsList
        this.$common.showLoad();
        let data = await saveTempAgreement(
          addParams
        );
        this.$common.hideLoad();
        if (data && data.result) {
          this.$common.n_success('保存成功');
          this.$router.go(-1)
        }
      }
    },
    // 新增年度协议
    async saveYearAgreementMethod() {
      if (this.checkInputData()) {
        let channelName = ''
        channelType().forEach(item => {
          if (item.value == this.enterpriseInfo.channelId){
            channelName = item.label
          }
        });

        let addGoodsList = []
        for (let i = 0; i < this.agreementGoodsList.length; i++){
          let item = this.agreementGoodsList[i]
          let obj = {}
          obj.goodsId = item.id
          obj.goodsName = item.name
          obj.isPatent = item.isPatent
          obj.sellSpecificationsId = item.sellSpecificationsId
          obj.standardId = item.standardId
          obj.licenseNo = item.licenseNo
          obj.sellSpecifications = item.sellSpecifications
          addGoodsList.push(obj)
        }

        this.$common.showLoad();
        let params = this.params
        let data = await saveYearAgreement(
          this.eid,
          this.ename,
          this.name,
          this.content,
          this.date[0],
          this.date[1],
          channelName,
          params.customerEid,
          this.enterpriseInfo.name,
          addGoodsList
        );
        this.$common.hideLoad();
        if (data && data.result) {
          this.$common.n_success('保存成功');
          this.$router.go(-1)
        }
      }
    },
    // 新增补充协议
    checkTempData() {
      if (JSON.stringify(this.currentYearAgreement) == '{}'){
        this.$common.warn('请选择年度协议')
        return false
      } else if (this.mode == 2 && JSON.stringify(this.currentThirdEnterprise) == '{}') {
        this.$common.warn('请选择第三方')
        return false
      } else if (this.name.trim() == '') {
        this.$common.warn('请填写协议名称')
        return false
      } else if (!this.date || this.date == '') {
        this.$common.warn('请选择时间')
        return false
      } else if (!this.agreementGoodsList || this.agreementGoodsList.length == 0) {
        this.$common.warn('请添加可采商品')
        return false
      } else {
        return true
      }
    },
    //  校验数据
    checkInputData() {
      if (this.eid == 0){
        this.$common.warn('请选择协议主体')
        return false
      } else if (this.name.trim() == '') {
        this.$common.warn('请填写协议名称')
        return false
      } else if (!this.date || this.date == '') {
        this.$common.warn('请选择时间')
        return false
      } else if (!this.agreementGoodsList || this.agreementGoodsList.length == 0) {
        this.$common.warn('请添加可采商品')
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
      },
    sellerEidCheckInput(val) {
      val = val.replace(/[^0-9]/gi, '')
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
    height: 35px;
  }
}
</style>

