<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <!-- 内容区 -->
    <div class="app-container-content has-bottom-bar">
      <!-- 协议基本信息 -->
      <div class="top-box">
        <div class="flex-row-left item">
          <div class="line-view"></div>
          <span class="font-size-lg bold">基本信息</span>
        </div>
        <div class="flex-between">
          <div class="top-box-item">
            <div class="detail-item">
              <div class="detail-item-left font-title-color">创建日期:</div>
              <div class="detail-item-right">{{ agreementMainTerms.createTime | formatDate }}</div>
            </div>
            <div class="detail-item">
              <div class="detail-item-left font-title-color">所属公司:</div>
              <div class="detail-item-right"></div>
            </div>
            <div class="detail-item">
              <div class="detail-item-left font-title-color">协议负责人:</div>
              <div class="detail-item-right">{{ agreementMainTerms.mainUserName }}</div>
            </div>
          </div>
          <div class="top-box-item">
            <div class="detail-item">
              <div class="detail-item-left font-title-color">协议编号:</div>
              <div class="detail-item-right">{{ agreementMainTerms.agreementNo }}</div>
            </div>
            <div class="detail-item">
              <div class="detail-item-left font-title-color">甲方类型:</div>
              <div class="detail-item-right">{{ agreementMainTerms.firstType | dictLabel(agreementFirstType) }}</div>
            </div>
            <div class="detail-item">
              <div class="detail-item-left font-title-color">甲方:</div>
              <div class="detail-item-right">{{ agreementMainTerms.ename }}</div>
            </div>
          </div>
          <div class="top-box-item">
            <div class="detail-item">
              <div class="detail-item-left font-title-color">录入人:</div>
              <div class="detail-item-right">{{ agreementMainTerms.createUserName }}</div>
            </div>
            <div class="detail-item">
              <div class="detail-item-left font-title-color">协议类型:</div>
              <div class="detail-item-right">{{ agreementMainTerms.agreementType | dictLabel(agreementType) }}</div>
            </div>
            <div class="detail-item">
              <div class="detail-item-left font-title-color">乙方:</div>
              <div class="detail-item-right">{{ agreementMainTerms.secondName }}</div>
            </div>
          </div>
        </div>
      </div>
      <div class="tip-box mar-b-10">
        <i class="el-icon-warning"></i>
        <span>甲、乙双方本着互惠互利、相互保密的原则,经双方友好协商,达成如下协议</span>
      </div>
      <!-- 协议主条款 -->
      <div class="top-box">
        <div class="flex-row-left item">
          <div class="line-view"></div>
          <span class="font-size-lg bold">协议主条款</span>
        </div>
        <div class="detail-item">
          <div class="detail-item-left font-title-color">生效日期:</div>
          <div class="detail-item-right">{{ agreementMainTerms.startTime | formatDate('yyyy-MM-dd') }} - {{ agreementMainTerms.endTime | formatDate('yyyy-MM-dd') }}</div>
        </div>
        <div class="detail-item">
          <div class="detail-item-left font-title-color">签订日期:</div>
          <div class="detail-item-right">{{ agreementMainTerms.signTime | formatDate('yyyy-MM-dd') }}</div>
        </div>
        <div class="detail-item">
          <div class="detail-item-left font-title-color">甲方协议签订人:</div>
          <div class="detail-item-right">{{ agreementMainTerms.firstSignUserName }}</div>
        </div>
        <div class="detail-item">
          <div class="detail-item-left font-title-color">联系电话:</div>
          <div class="detail-item-right">{{ agreementMainTerms.firstSignUserPhone }}</div>
        </div>
        <div class="detail-item" v-if="agreementMainTerms.marginFlag == 1">
          <div class="detail-item-left font-title-color">保证金支付方:</div>
          <div class="detail-item-right">{{ agreementMainTerms.marginPayer == 2 ? '乙方' : '指定商业公司' }}</div>
        </div>
        <div class="detail-item" v-if="agreementMainTerms.marginFlag == 1 && agreementMainTerms.marginPayer == 3">
          <div class="detail-item-left font-title-color">指定商业公司名称:</div>
          <div class="detail-item-right">{{ agreementMainTerms.businessName }}</div>
        </div>
      </div>
      <!-- 返利条件设置 -->
      <div class="top-box">
        <div class="flex-row-left item">
          <div class="line-view"></div>
          <span class="font-size-lg bold">商业供货协议条款</span>
        </div>
        <!-- 支付形式 -->
        <div class="detail-item">
          <div class="detail-item-left font-title-color label-item">支付形式:</div>
          <div class="detail-item-right detail-item-right-box">
            <el-row type="flex" class="detail-item-right-item" v-for="item in agreementSettlementTerms.agreementPaymentMethodList" :key="item.id">
              <el-col :span="4" class="detail-item-right-item-col"><div>{{ item.payMethod | dictLabel(agreementPayMethod) }}</div></el-col>
              <el-col :span="4" class="detail-item-right-item-col"><div>占比{{ item.ratio }}%</div></el-col>
            </el-row>
          </div>
        </div>
        <!-- 结算方式 -->
        <div class="detail-item">
          <div class="detail-item-left font-title-color label-item">结算方式:</div>
          <div class="detail-item-right detail-item-right-box" v-if="agreementSettlementTerms.agreementSettlementMethod">
            <el-row type="flex" class="detail-item-right-item" v-if="agreementSettlementTerms.agreementSettlementMethod.advancePaymentFlag">
              <el-col :span="4" class="detail-item-right-item-col"><div>预付款</div></el-col>
            </el-row>
            <el-row type="flex" class="detail-item-right-item" v-if="agreementSettlementTerms.agreementSettlementMethod.paymentDaysFlag">
              <el-col :span="4" class="detail-item-right-item-col"><div>账期</div></el-col>
              <el-col :span="4" class="detail-item-right-item-col"><div>结算周期 {{ agreementSettlementTerms.agreementSettlementMethod.paymentDaysSettlementPeriod || '- -' }} 天</div></el-col>
              <el-col :span="4" class="detail-item-right-item-col"><div>结算日 {{ agreementSettlementTerms.agreementSettlementMethod.paymentDaysSettlementDay || '- -' }} 日</div></el-col>
              <el-col :span="4" class="detail-item-right-item-col"><div>支付日 {{ agreementSettlementTerms.agreementSettlementMethod.paymentDaysPayDays || '- -' }} 日</div></el-col>
            </el-row>
            <el-row type="flex" class="detail-item-right-item" v-if="agreementSettlementTerms.agreementSettlementMethod.actualSalesSettlementFlag">
              <el-col :span="4" class="detail-item-right-item-col"><div>实销实结</div></el-col>
              <el-col :span="4" class="detail-item-right-item-col"><div>结算周期 {{ agreementSettlementTerms.agreementSettlementMethod.actualSalesSettlementPeriod || '- -' }} 天</div></el-col>
              <el-col :span="4" class="detail-item-right-item-col"><div>结算日 {{ agreementSettlementTerms.agreementSettlementMethod.actualSalesSettlementDay || '- -' }} 日</div></el-col>
              <el-col :span="4" class="detail-item-right-item-col"><div>支付日 {{ agreementSettlementTerms.agreementSettlementMethod.actualSalesSettlementPayDays || '- -' }} 日</div></el-col>
            </el-row>
            <el-row type="flex" class="detail-item-right-item" v-if="agreementSettlementTerms.agreementSettlementMethod.payDeliveryFlag">
              <el-col :span="4" class="detail-item-right-item-col"><div>货到付款</div></el-col>
            </el-row>
            <el-row type="flex" class="detail-item-right-item" v-if="agreementSettlementTerms.agreementSettlementMethod.pressGroupFlag">
              <el-col :span="4" class="detail-item-right-item-col"><div>压批</div></el-col>
              <el-col :span="4" class="detail-item-right-item-col"><div>压批次数 {{ agreementSettlementTerms.agreementSettlementMethod.pressGroupNumber || '- -' }}</div></el-col>
            </el-row>
            <el-row type="flex" class="detail-item-right-item" v-if="agreementSettlementTerms.agreementSettlementMethod.creditFlag">
              <el-col :span="4" class="detail-item-right-item-col"><div>授信</div></el-col>
              <el-col :span="4" class="detail-item-right-item-col"><div>授信金额 {{ agreementSettlementTerms.agreementSettlementMethod.creditAmount || '- -' }} 万</div></el-col>
            </el-row>
          </div>
        </div>
        <!-- 结算方式 -->
        <div class="detail-item">
          <div class="detail-item-left font-title-color label-item">其他:&nbsp;&nbsp;</div>
          <div class="detail-item-right detail-item-right-box" v-if="agreementSettlementTerms.agreementSettlementMethod">
            <el-row type="flex" class="detail-item-right-item">
              <el-col :span="6" class="detail-item-right-item-col"><div>到货周期: {{ agreementSettlementTerms.agreementSettlementMethod.arrivePeriod || '- -' }} 天</div></el-col>
              <el-col :span="6" class="detail-item-right-item-col"><div>最小发货量: {{ agreementSettlementTerms.agreementSettlementMethod.minShipmentNumber || '- -' }} 件</div></el-col>
              <el-col :span="6" class="detail-item-right-item-col"><div>送货方式: {{ agreementSettlementTerms.agreementSettlementMethod.deliveryType | dictLabel(agreementDeliveryType) }}</div></el-col>
              <el-col :span="6" class="detail-item-right-item-col"><div>采购负责人: {{ agreementSettlementTerms.agreementSettlementMethod.buyerName }}</div></el-col>
            </el-row>
          </div>
        </div>
      </div>
      <!-- 协议供销条款 -->
      <div class="top-box">
        <div class="flex-row-left item">
          <div class="line-view"></div>
          <span class="font-size-lg bold">协议供销条款</span>
        </div>
        <div class="detail-item">
          <div class="detail-item-left font-title-color">购进渠道:</div>
          <div class="detail-item-right">{{ agreementSupplySalesTerms.buyChannel | dictLabel(agreementSupplySalesBuyChannel) }}</div>
        </div>
        <div class="pad-b-10" v-if="agreementSupplySalesTerms.buyChannel == 3">
          <yl-table
            border
            show-header
            :list="agreementSupplySalesTerms.supplySalesEnterpriseList"
            :total="0"
          >
            <el-table-column label="企业ID" align="center" prop="eid">
            </el-table-column>
            <el-table-column label="企业名称" align="center" prop="ename">
            </el-table-column>
          </yl-table>
        </div>
        <div class="title-label">协议商品</div>
        <div class="detail-item">
          <div class="detail-item-left font-title-color">是否全系列品种:</div>
          <div class="detail-item-right">{{ agreementSupplySalesTerms.allLevelKindsFlag == 1 ? '是' : '否' }}</div>
        </div>
        <!-- 不是全系列品种 -->
        <div v-if="agreementSupplySalesTerms.allLevelKindsFlag == 0 && agreementSupplySalesTerms.supplySalesGoodsGroupList.length > 0">
          <div class="goods-group-item" v-for="(item, index) in agreementSupplySalesTerms.supplySalesGoodsGroupList" :key="index">
            <!-- 商品表单 -->
            <div class="pad-b-10">
              <yl-table
                border
                show-header
                :max-height="600"
                :list="item.supplySalesGoodsList"
                :total="0"
              >
                <el-table-column label="商品编号" align="center">
                  <template slot-scope="{ row }">
                    <div class="font-size-base">{{ row.goodsId }}</div>
                  </template>
                </el-table-column>
                <el-table-column label="商品名称" min-width="162" align="center">
                  <template slot-scope="{ row }">
                    <div class="goods-desc">
                      <div class="font-size-base">{{ row.goodsName }}</div>
                    </div>
                  </template>
                </el-table-column>
                <el-table-column label="商品规格" min-width="150" align="center">
                  <template slot-scope="{ row }">
                    <div class="font-size-base">{{ row.specifications }}</div>
                  </template>
                </el-table-column>
                <el-table-column label="供货含税单价" min-width="150" align="center">
                  <template slot-scope="{ row }">
                    <div class="font-size-base">{{ row.supplyTaxPrice }}</div>
                  </template>
                </el-table-column>
                <el-table-column align="center" min-width="150">
                  <template slot="header" slot-scope="{}">
                    <span style="margin-right:8px;">出库价含税单价 |</span>
                    <el-checkbox :value="item.exitWarehouseTaxPriceFlag">维价</el-checkbox>
                  </template>
                  <template slot-scope="{ row }">
                    <div class="font-size-base">
                      <el-input v-if="item.exitWarehouseTaxPriceFlag" :value="row.exitWarehouseTaxPrice"></el-input>
                    </div>
                  </template>
                </el-table-column>
                <el-table-column align="center" min-width="150">
                  <template slot="header" slot-scope="{}">
                    <span style="margin-right:8px;">零售价含税单价 |</span>
                    <el-checkbox :value="item.retailTaxPriceFlag">维价</el-checkbox>
                  </template>
                  <template slot-scope="{ row }">
                    <div class="font-size-base">
                      <el-input v-if="item.retailTaxPriceFlag" :value="row.retailTaxPrice"></el-input>
                    </div>
                  </template>
                </el-table-column>
                <el-table-column label="是否独家" align="center" fixed="right">
                  <template slot-scope="{ row }">
                    <div class="operation-view">
                      <el-checkbox :value="row.exclusiveFlag"></el-checkbox>
                    </div>
                  </template>
                </el-table-column>
              </yl-table>
            </div>
            <div class="title-label">
              控销设置
            </div>
            <div class="detail-item mar-t-10">
              <div class="detail-item-left font-title-color">控销类型:</div>
              <div class="detail-item-right">{{ item.controlSaleType | dictLabel(agreementControlSaleType) }}</div>
            </div>
            <!-- 黑白名单 -->
            <div v-if="item.agreementControlList && item.agreementControlList.length > 0">
              <!-- 控销范围 -->
              <div class="detail-item mar-t-10 flex-row-left" v-if="item.agreementControlList.indexOf(1) != -1">
                <div class="detail-item-left font-title-color">控销范围:</div>
                <yl-button type="text" @click="selectClick(item.controlArea.jsonContent)">{{ item.controlArea.description }}</yl-button>
              </div>
              <!-- 客户类型 -->
              <div v-if="item.agreementControlList.indexOf(2) != -1">
                <div class="detail-label font-title-color">客户类型:</div>
                <yl-table
                  border
                  show-header
                  :list="item.controlCustomerTypeList"
                  :total="0"
                >
                  <el-table-column label="序号" align="center">
                    <template slot-scope="{ $index }">
                      <div class="font-size-base">{{ $index + 1 }}</div>
                    </template>
                  </el-table-column>
                  <el-table-column label="客户类型" align="center">
                    <template slot-scope="{ row }">
                      <div class="font-size-base">{{ row | dictLabel(enterpriseType) }}</div>
                    </template>
                  </el-table-column>
                </yl-table>
              </div>
            </div>
          </div>
        </div>
      </div>
      <!-- 协议返利条款 -->
      <div class="top-box">
        <div class="detail-item">
          <div class="detail-item-left font-title-color">是否底价供货:</div>
          <div class="detail-item-right">{{ agreementRebateTerms.reserveSupplyFlag == 1 ? '是' : '否' }}</div>
        </div>
        <div class="detail-item">
          <div class="detail-item-left font-title-color">返利支付方:</div>
          <div class="detail-item-right">{{ agreementRebateTerms.rebatePay | dictLabel(agreementRebatePay) }}</div>
        </div>
        <div class="pad-b-10" v-if="agreementRebateTerms.rebatePay == 2">
          <yl-table
            border
            show-header
            :list="agreementRebateTerms.agreementRebatePayEnterpriseList"
            :total="0"
          >
            <el-table-column label="企业ID" align="center" prop="eid">
            </el-table-column>
            <el-table-column label="企业名称" align="center" prop="ename">
            </el-table-column>
          </yl-table>
        </div>
        <el-row class="row-item-view">
          <el-col :span="8">
            <div class="detail-item">
              <div class="detail-item-left font-title-color">返利兑付方式:</div>
              <div class="detail-item-right">{{ agreementRebateTerms.rebateCashType | dictLabel(agreementRebateCashType) }}</div>
            </div>
          </el-col>
          <el-col :span="16">
            <div class="detail-item">
              <div class="detail-item-left font-title-color">其他备注:</div>
              <div class="detail-item-right">{{ agreementRebateTerms.otherRemark }}</div>
            </div>
          </el-col>
        </el-row>
        <el-row class="row-item-view">
          <el-col :span="8">
            <div class="detail-item">
              <div class="detail-item-left font-title-color">返利兑付时间:</div>
              <div class="detail-item-right">{{ agreementRebateTerms.rebateCashTime | dictLabel(agreementRebateCashTime) }}</div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="detail-item">
              <div class="detail-item-right">隔{{ agreementRebateTerms.rebateCashSegment }} {{ agreementRebateTerms.rebateCashUnit == 1 ? '月' : '天' }}兑付</div>
            </div>
          </el-col>
        </el-row>
        <div class="row-item-view" v-if="agreementRebateTerms.agreementOtherRebateList && agreementRebateTerms.agreementOtherRebateList.length > 0">
          <el-row class="row-item-view" v-for="(item, index) in agreementRebateTerms.agreementOtherRebateList" :key="index">
            <el-col :span="8">
              <div class="detail-item">
                <div class="detail-item-left font-title-color">其他（非商品）返利:</div>
                <div class="detail-item-right">{{ item.rebateType | dictLabel(agreementNotProductRebateType) }}</div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="detail-item">
                <div class="detail-item-left font-title-color">其他（非商品）返利:</div>
                <div class="detail-item-right">{{ item.amountType | dictLabel(agreementNotProductRebateAmountType) }} {{ item.amount }} {{ item.unit == 1 ? '元' : '%' }}</div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="detail-item">
                <div class="detail-item-left font-title-color">是否含税:</div>
                <div class="detail-item-right">含税</div>
              </div>
            </el-col>
          </el-row>
        </div>
        <div class="detail-item">
          <div class="detail-item-left font-title-color">设置方式:</div>
          <div class="detail-item-right">{{ agreementRebateTerms.goodsRebateRuleType | dictLabel(agreementGoodsRebateRuleType) }}</div>
        </div>
        <div class="detail-item">
          <div class="detail-item-left font-title-color">返利规则设置:</div>
          <div class="detail-item-right">（任务量设置为全品表单，不同的任务量商品请拆分协议录入）</div>
        </div>
        <!-- 时段 -->
        <div v-if="agreementRebateTerms.agreementRebateTimeSegmentList && agreementRebateTerms.agreementRebateTimeSegmentList.length > 0">
          <div v-for="(periodItem, periodIndex) in agreementRebateTerms.agreementRebateTimeSegmentList" :key="periodIndex" class="period-item">
            <div class="top-title mar-b-10">
              <span v-if="periodItem.type == 1">全时段</span>
              <span v-if="periodItem.type == 2">子时段: {{ periodItem.startTime | formatDate('yyyy-MM-dd') }} -- {{ periodItem.endTime | formatDate('yyyy-MM-dd') }}</span>
            </div>
            <div class="mar-b-10">
              <div class="top-label mar-b-10" v-if="periodItem.allProductIndex && periodItem.allProductIndex.length > 0">全品表单</div>
              <div v-for="(allProductItem, allProductIndex) in periodItem.allProductFormList" :key="allProductIndex">
                <yl-table
                  border
                  show-header
                  :list="[1]"
                  :total="0"
                >
                  <el-table-column label="商品" align="center">
                    <template slot-scope>
                      <div class="font-size-base">全品</div>
                    </template>
                  </el-table-column>
                  <el-table-column label="厂家/供应商" align="center">
                    <template slot-scope>
                      <div class="font-size-base">{{ allProductItem.ename }}</div>
                    </template>
                  </el-table-column>
                  <el-table-column label="当前商品数" align="center">
                    <template slot-scope>
                      <div class="font-size-base">{{ allProductItem.goodsNumber }}</div>
                    </template>
                  </el-table-column>
                  <el-table-column label="返利规则" align="center">
                    <template slot-scope>
                      <div class="font-size-base">{{ allProductItem.rebateRule }}</div>
                    </template>
                  </el-table-column>
                  <el-table-column label="任务量标准" align="center">
                    <template slot-scope>
                      <div class="font-size-base">{{ allProductItem.taskStandard | dictLabel(agreementRebateTaskStandard) }}</div>
                    </template>
                  </el-table-column>
                  <el-table-column label="任务量" align="center">
                    <template slot-scope>
                      <div class="font-size-base">{{ allProductItem.taskNum }}</div>
                    </template>
                  </el-table-column>
                  <el-table-column label="阶梯条件计算方式" align="center">
                    <template slot-scope>
                      <div class="font-size-base">{{ allProductItem.rebateStageMethod | dictLabel(agreementRebateStageMethod) }}</div>
                    </template>
                  </el-table-column>
                  <el-table-column label="返利计算规则" align="center">
                    <template slot-scope>
                      <div class="font-size-base">{{ allProductItem.rebateRuleType | dictLabel(agreementRebateRuleType) }}</div>
                    </template>
                  </el-table-column>
                  <el-table-column label="黑/白名单" align="center">
                    <template slot-scope>
                      <div class="font-size-base">{{ allProductItem.controlSaleType | dictLabel(agreementControlSaleType) }}</div>
                    </template>
                  </el-table-column>
                  <el-table-column label="返利区域范围设置" align="center">
                    <template slot-scope>
                      <div class="font-size-base"><yl-button type="text" @click="selectClick(allProductItem.jsonContent)">{{ allProductItem.description }}</yl-button></div>
                    </template>
                  </el-table-column>
                  <el-table-column label="返利客户类型设置" align="center">
                    <template slot-scope>
                      <div class="font-size-base">{{ allProductItem.customerType }}</div>
                    </template>
                  </el-table-column>
                  <el-table-column label="超任务部分是否计算返利" align="center">
                    <template slot-scope>
                      <div class="font-size-base">{{ allProductItem.overTaskNum }}</div>
                    </template>
                  </el-table-column>
                </yl-table>
              </div>
            </div>
            <div>
              <div class="top-label mar-b-10" v-if="periodItem.categoryProductFormList && periodItem.categoryProductFormList.length > 0">分类表单</div>
              <div v-for="(categoryProductItem, categoryProductIndex) in periodItem.categoryProductFormList" :key="categoryProductIndex">
                <yl-table
                  border
                  show-header
                  :list="categoryProductItem"
                  :total="0"
                >
                  <el-table-column label="商品编号" align="center" prop="goodsId">
                  </el-table-column>
                  <el-table-column label="商品名称" align="center" prop="goodsName">
                  </el-table-column>
                  <el-table-column label="规格" align="center" prop="specifications">
                  </el-table-column>
                  <el-table-column label="厂家" align="center" prop="producerManufacturer">
                  </el-table-column>
                  <el-table-column label="品牌厂家" align="center" prop="brandManufacturer">
                  </el-table-column>
                  <el-table-column label="返利规则" align="center" prop="rebateRule">
                  </el-table-column>
                  <el-table-column label="阶梯条件计算方式" align="center">
                    <template slot-scope="{ row }">
                      <div class="font-size-base">{{ row.rebateStageMethod | dictLabel(agreementRebateStageMethod) }}</div>
                    </template>
                  </el-table-column>
                  <el-table-column label="返利计算规则" align="center">
                    <template slot-scope="{ row }">
                      <div class="font-size-base">{{ row.rebateRuleType | dictLabel(agreementRebateRuleType) }}</div>
                    </template>
                  </el-table-column>
                  <el-table-column label="黑/白名单" align="center">
                    <template slot-scope="{ row }">
                      <div class="font-size-base">{{ row.controlSaleType | dictLabel(agreementControlSaleType) }}</div>
                    </template>
                  </el-table-column>
                  <el-table-column label="返利区域范围设置" align="center">
                    <template slot-scope="{ row }">
                      <div class="font-size-base"><yl-button type="text" @click="selectClick(row.jsonContent)">{{ row.description }}</yl-button></div>
                    </template>
                  </el-table-column>
                  <el-table-column label="返利客户类型设置" align="center">
                    <template slot-scope="{ row }">
                      <div class="font-size-base">{{ row.customerType }}</div>
                    </template>
                  </el-table-column>
                </yl-table>
              </div>
            </div>
          </div>
        </div>
      </div>
      <!-- 协议附件 -->
      <div class="top-box">
        <div class="flex-row-left item">
          <div class="line-view"></div>
          <span class="font-size-lg bold">协议附件</span>
        </div>
        <div class="detail-item">
          <div class="detail-item-left font-title-color">协议附件类型:</div>
          <div class="detail-item-right">{{ agreementMainTerms.attachmentType | dictLabel(agreementAttachmentType) }}</div>
        </div>
        <div class="detail-item">
          <div class="packing-img-box">
            <div class="packing-img" v-for="(item, index) in agreementMainTerms.agreementAttachmentList" :key="item.fileKey" @mouseenter="showDialogClick(index)" @mouseleave="hideDialog(index)">
              <el-image v-if="item.fileType == 1" class="packing-image" :src="item.fileUrl" fit="contain" :lazy="true"></el-image>
              <img v-if="item.fileType == 2" class="packing-image" src="@/subject/pop/assets/agreement/pdf.png" fit="contain"/>
              <div class="packing-cover" v-if=" packingShow && index === packingCurrent">
                <i v-if="item.fileType == 1" class="el-icon-zoom-in" @click="changeBigUrl(item.fileUrl)"></i>
                <i v-if="item.fileType == 2" class="el-icon-download" @click="downloadClick(item)"></i>
              </div>
            </div>
          </div>
        </div>
      </div>
      <!-- 备注 -->
      <div class="top-box">
        <div class="flex-row-left item">
          <div class="line-view"></div>
          <span class="font-size-lg bold">备注</span>
        </div>
        <div class="detail-item">
          <el-input type="textarea" :rows="3" placeholder="请输入备注" maxlength="200" :value="agreementRebateTerms.remark"></el-input>
        </div>
      </div>
      <!-- 协议审核记录 -->
      <div class="top-box" v-if="agreementStatusLogList && agreementStatusLogList.length > 0">
        <div class="flex-row-left item">
          <div class="line-view"></div>
          <span class="font-size-lg bold">协议审核记录</span>
        </div>
        <div class="period-item">
          <yl-table
            border
            show-header
            :list="agreementStatusLogList"
            :total="0"
          >
            <el-table-column label="审核状态" align="center">
              <template slot-scope="{ row }">
                <div>
                  {{ row.authStatus | dictLabel(agreementAuthStatus) }}
                </div>
              </template>
            </el-table-column>
            <el-table-column label="审核时间" align="center">
              <template slot-scope="{ row }">
                <div class="font-size-base">{{ row.authTime | formatDate }}</div>
              </template>
            </el-table-column>
            <el-table-column label="审核备注" align="center">
              <template slot-scope="{ row }">
                <div class="font-size-base">{{ row.authRejectReason }}</div>
              </template>
            </el-table-column>
          </yl-table>
        </div>
      </div>
      <!-- 审核 -->
      <div class="top-box" v-if="operateType == 2">
        <div class="mar-b-10 detail-item">
          <div class="detail-item-left font-title-color label-item text-height">纸质件编号:</div>
          <div class="detail-item-right detail-item-right-box">
            <el-input v-model="paperNo" placeholder="纸质件编号" />
          </div>
        </div>
        <!-- 审核 -->
        <div class="detail-item mar-b-10">
          <div class="detail-item-left font-title-color label-item">审核:</div>
          <div class="detail-item-right detail-item-right-box">
            <el-radio-group v-model="authStatus" @change="authStatusTypeChange">
              <el-radio :label="2">审核通过</el-radio>
              <el-radio :label="3">审核驳回</el-radio>
            </el-radio-group>
          </div>
        </div>
        <!-- 选择框 -->
        <div class="mar-b-10" v-if="authStatus == 3">
          <div class="detail-item">
            <div class="detail-item-left font-title-color label-item"></div>
            <div class="detail-item-right detail-item-right-box my-form-box">
              <div class="select-view">
                <span class="checkbox-title">基本信息:</span>
                <el-checkbox-group v-model="agreementRejectBasicList">
                  <div class="checkbox-view border-1px-b">
                    <div class="checkbox" v-for="item in agreementRejectBasic" :key="item.value">
                      <el-checkbox :label="item.label">{{ item.label }}</el-checkbox>
                    </div>
                  </div>
                </el-checkbox-group>
              </div>
              <div class="select-view">
                <span class="checkbox-title">协议主条款:</span>
                <el-checkbox-group v-model="agreementRejectMainTermsList">
                  <div class="checkbox-view border-1px-b">
                    <div class="checkbox" v-for="item in agreementRejectMainTerms" :key="item.value">
                      <el-checkbox :label="item.label">{{ item.label }}</el-checkbox>
                    </div>
                  </div>
                </el-checkbox-group>
              </div>
              <div class="select-view">
                <span class="checkbox-title">商业供货协议条款:</span>
                <el-checkbox-group v-model="agreementRejectMainTermsList">
                  <div class="checkbox-view border-1px-b">
                    <div class="checkbox" v-for="item in agreementRejectSettlementTerms" :key="item.value">
                      <el-checkbox :label="item.label">{{ item.label }}</el-checkbox>
                    </div>
                  </div>
                </el-checkbox-group>
              </div>
              <div class="select-view">
                <span class="checkbox-title">协议供销条款:</span>
                <el-checkbox-group v-model="agreementRejectMainTermsList">
                  <div class="checkbox-view border-1px-b">
                    <div class="checkbox" v-for="item in agreementRejectSupplySalesTerms" :key="item.value">
                      <el-checkbox :label="item.label">{{ item.label }}</el-checkbox>
                    </div>
                  </div>
                </el-checkbox-group>
              </div>
              <div class="select-view">
                <span class="checkbox-title">协议返利条款:</span>
                <el-checkbox-group v-model="agreementRejectMainTermsList">
                  <div class="checkbox-view border-1px-b">
                    <div class="checkbox" v-for="item in agreementRejectRebateTerms" :key="item.value">
                      <el-checkbox :label="item.label">{{ item.label }}</el-checkbox>
                    </div>
                  </div>
                </el-checkbox-group>
              </div>
              <div class="select-view">
                <span class="checkbox-title">协议附件:</span>
                <el-checkbox-group v-model="agreementRejectMainTermsList">
                  <div class="checkbox-view border-1px-b">
                    <div class="checkbox" v-for="item in agreementRejectAttachment" :key="item.value">
                      <el-checkbox :label="item.label">{{ item.label }}</el-checkbox>
                    </div>
                  </div>
                </el-checkbox-group>
              </div>
              <!-- 其他 -->
              <div class="select-view">
                <span class="checkbox-title">其他:</span>
                <div class="checkbox-view border-1px-b">
                  <div class="checkbox">
                    <el-checkbox v-model="isSelect">其他</el-checkbox>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <!-- 备注 -->
        <div class="detail-item" v-if="isSelect">
          <div class="detail-item-left font-title-color label-item text-height">备注:</div>
          <div class="detail-item-right detail-item-right-box">
            <el-input type="textarea" :rows="3" placeholder="请输入备注" maxlength="200" v-model="remark"></el-input>
          </div>
        </div>
      </div>
      <div class="flex-row-center bottom-view">
        <yl-button plain type="primary" @click="$router.go(-1)">返回</yl-button>
        <yl-button type="primary" v-if="operateType == 2" @click="submitClick">提交</yl-button>
      </div>
    </div>
    <!-- 弹窗 -->
    <yl-dialog :visible.sync="show" title="图片预览" @confirm="showDialogConfirm" :show-cancle="false">
      <div class="dialog-content flex-row-center">
        <el-image :src="bigUrl" fit="contain" :lazy="true"></el-image>
      </div>
    </yl-dialog>
    <!-- 选择区域 -->
    <city-select :show.sync="showCityDialog" :init-ids="selectedData" platform="pop" disabled @choose="citySelectConfirm"></city-select>
  </div>
</template>

<script>

import {
  getDetail,
  updateAuthAgreement
} from '@/subject/pop/api/agreement';
import { agreementFirstType, agreementType, agreementPayMethod, agreementDeliveryType, agreementSupplySalesBuyChannel, agreementControlSaleType, enterpriseType,
  agreementAuditRejectType, agreementRejectBasic, agreementRejectMainTerms, agreementRejectSettlementTerms, agreementRejectSupplySalesTerms, agreementRejectRebateTerms,
  agreementRejectAttachment, agreementRebatePay, agreementRebateCashType, agreementRebateCashTime, agreementNotProductRebateType, agreementNotProductRebateAmountType,
  agreementGoodsRebateRuleType, agreementRebateStageMethod, agreementRebateRuleType, agreementRebateTaskStandard, agreementAttachmentType, agreementAuthStatus
} from '@/subject/pop/utils/busi';
import citySelect from '@/subject/pop/components/CitySelect'

export default {
  components: {
    citySelect
  },
  computed: {
    // 甲方类型
    agreementFirstType() {
      return agreementFirstType()
    },
    // 协议类型
    agreementType() {
      return agreementType()
    },
    // 付款方式：1-支票 2-电汇 3-易贷 4-3个月承兑 5-6个月承兑
    agreementPayMethod() {
      return agreementPayMethod()
    },
    agreementDeliveryType() {
      return agreementDeliveryType()
    },
    // 购进渠道
    agreementSupplySalesBuyChannel() {
      return agreementSupplySalesBuyChannel()
    },
    // 控销类型
    agreementControlSaleType() {
      return agreementControlSaleType();
    },
    // 企业类型
    enterpriseType() {
      return enterpriseType();
    },
    // 协议审核驳回原因类型
    agreementAuditRejectType() {
      return agreementAuditRejectType();
    },
    // 基本信息
    agreementRejectBasic() {
      return agreementRejectBasic();
    },
    // 协议主条款
    agreementRejectMainTerms() {
      return agreementRejectMainTerms();
    },
    // 商业供货协议条款
    agreementRejectSettlementTerms() {
      return agreementRejectSettlementTerms();
    },
    // 协议供销条款
    agreementRejectSupplySalesTerms() {
      return agreementRejectSupplySalesTerms();
    },
    // 协议返利条款
    agreementRejectRebateTerms() {
      return agreementRejectRebateTerms();
    },
    // 协议附件
    agreementRejectAttachment() {
      return agreementRejectAttachment();
    },
    // 返利支付方
    agreementRebatePay() {
      return agreementRebatePay()
    },
    // 返利兑付方式
    agreementRebateCashType() {
      return agreementRebateCashType();
    },
    // 返利兑付时间
    agreementRebateCashTime() {
      return agreementRebateCashTime();
    },
    // 非商品返利方式
    agreementNotProductRebateType() {
      return agreementNotProductRebateType();
    },
    // 金额类型
    agreementNotProductRebateAmountType() {
      return agreementNotProductRebateAmountType();
    },
    // 商品返利规则设置方式
    agreementGoodsRebateRuleType() {
      return agreementGoodsRebateRuleType()
    },
    // 返利阶梯条件计算方法
    agreementRebateStageMethod() {
      return agreementRebateStageMethod();
    },
    // 返利计算规则类型
    agreementRebateRuleType() {
      return agreementRebateRuleType()
    },
    // 返利标准
    agreementRebateTaskStandard() {
      return agreementRebateTaskStandard();
    },
     // 协议附件类型
    agreementAttachmentType() {
      return agreementAttachmentType();
    },
    // 协议审核状态
    agreementAuthStatus() {
      return agreementAuthStatus()
    },
    // 计算梯度 单位
    unitStr: function () {
      if (this.agreementInfo.childType == 1) {
        return '元'
      } else if (this.agreementInfo.childType == 2) {
        return '件'
      } else {
        return '天'
      }
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
          label: '预付款支付',
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
          title: '协议详情'
        }
      ],
      // 图片遮罩层
      packingShow: false,
      packingCurrent: -1,
      // 图片预览
      show: false,
      bigUrl: '',
      // 省市区选择
      showCityDialog: false,
      selectedData: [],

      // operateType 1-详情 2-审核详情
      operateType: 1,
      showVisible: true,
      loading: false,
      query: {
        page: 1,
        limit: 10
      },
      total: 0,
      dataList: [],
      // 协议信息
      agreementInfo: {},
      agreementMainTerms: {},
      agreementSupplySalesTerms: {},
      agreementSettlementTerms: {},
      agreementRebateTerms: {},
      // 审核 基本信息
      agreementRejectBasicList: [],
      agreementRejectMainTermsList: [],
      agreementRejectSettlementTermsList: [],
      agreementRejectSupplySalesTermsList: [],
      agreementRejectRebateTermsList: [],
      agreementRejectAttachmentList: [],
      agreementStatusLogList: [],
      // 其他
      isSelect: false,
      paperNo: '',
      // 审核状态：1-待审核 2-审核通过 3-审核驳回 4-已归档
      authStatus: 2,
      authRejectReason: '',
      remark: ''
    };
  },
  mounted() {
    this.params = this.$route.params;
    this.operateType = this.$route.params.operateType
    this.getAgreementsDetailMethod()
  },
  methods: {
    async getAgreementsDetailMethod() {
      this.loading = true;
      let params = this.params
      let data = await getDetail(
        params.id
      );
      this.loading = false;
      if (data) {
        this.agreementInfo = data
        this.agreementMainTerms = data.agreementMainTerms
        this.agreementSupplySalesTerms = data.agreementSupplySalesTerms
        this.agreementSettlementTerms = data.agreementSettlementTerms
        this.agreementRebateTerms = data.agreementRebateTerms
        this.agreementStatusLogList = data.agreementStatusLogList
      }
    },
    authStatusTypeChange(type) {
      this.agreementRejectBasicList = []
      this.agreementRejectMainTermsList = []
      this.agreementRejectSettlementTermsList = []
      this.agreementRejectSupplySalesTermsList = []
      this.agreementRejectRebateTermsList = []
      this.agreementRejectAttachmentList = []
      this.isSelect= false
      this.remark = ''
    },
    //显示操作项
    showDialogClick(index, item) {
      this.packingShow = true;
      this.packingCurrent = index;
    },
    //隐藏蒙层
    hideDialog(index, item) {
      this.packingShow = false;
      this.packingCurrent = -1;
    },
    downloadClick(item) {
      let a = document.createElement('a')
      a.href = item.fileUrl
      a.target = '_blank'
      if (item.name) {
        a.download = item.name
      }
      a.click()
    },
    // 查看大图
    changeBigUrl(url) {
      this.show = true
      this.bigUrl = url
    },
    showDialogConfirm() {
      this.show = false
    },
    selectClick(jsonContent) {
      if (jsonContent) {
        let nodes = JSON.parse(jsonContent)
        let check = []
        this.getSelectId(nodes, check)
        this.selectedData = check
      }

      this.showCityDialog = true
    },
    // 递归获取选中的
    getSelectId(arr, selectArr) {
      arr.forEach(item => {
        if (item.children && item.children.length){
          this.getSelectId(item.children, selectArr)
        } else {
          selectArr.push(item.code)
        }
      });
    },
    citySelectConfirm() {
      this.showCityDialog = false
    },
    async submitClick() {
      // this.$common.showLoad()
      let authRejectReason = ''
      if (this.authStatus == 3) {
        if (this.agreementRejectBasicList.length > 0) {
          authRejectReason += this.agreementRejectBasicList.join(';')
          authRejectReason += ';'
        }
        if (this.agreementRejectMainTermsList.length > 0) {
          authRejectReason += this.agreementRejectMainTermsList.join(';')
        }
        if (this.agreementRejectSettlementTermsList.length > 0) {
          authRejectReason += this.agreementRejectSettlementTermsList.join(';')
        }
        if (this.agreementRejectSupplySalesTermsList.length > 0) {
          authRejectReason += this.agreementRejectSupplySalesTermsList.join(';')
        }
        if (this.agreementRejectRebateTermsList.length > 0) {
          authRejectReason += this.agreementRejectRebateTermsList.join(';')
        }
        if (this.agreementRejectAttachmentList.length > 0) {
          authRejectReason += this.agreementRejectAttachmentList.join(';')
        }
        if (this.isSelect && this.remark.length > 0) {
          if (authRejectReason.length > 0) {
            authRejectReason += ';'
          }
          authRejectReason += this.remark
        }
        if (!authRejectReason) {
          this.$common.warn('请选择驳回原因')
          return
        }
      }

      this.$log('authRejectReason:',authRejectReason)
      let params = this.params
      let data = await updateAuthAgreement(
        params.id,
        this.paperNo,
        this.authStatus,
        authRejectReason
      );
      this.$common.hideLoad()
      if (typeof data !== 'undefined') {
        this.$common.alert('提交成功', r => {
          this.$router.go(-1)
        })
      }
    }
  }
};
</script>

<style lang="scss" >
.my-form-box{
  .el-form-item{
    .el-form-item__label{
      color: $font-title-color;
    }
    label{
      font-weight: 400 !important;
    }
  }
  .my-form-item-right{
    label{
      font-weight: 400 !important;
    }
  }
}
</style>
<style lang="scss" scoped>
@import "./index.scss";
</style>
