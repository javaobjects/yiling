<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <!-- 内容区 -->
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box" style="margin-top:0;">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">渠道商名称</div>
              <el-input v-model="query.name" placeholder="请输入渠道商名称" @keyup.enter.native="handleSearch" />
            </el-col>
            <el-col :span="6">
              <div class="title">渠道商社会统一信用代码</div>
              <el-input v-model="query.licenseNumber" placeholder="请输入渠道商社会统一信用代码" @keyup.enter.native="handleSearch" />
            </el-col>
            <el-col :span="12">
              <div class="title">地域查询</div>
              <div class="flex-row-left">
                <yl-choose-address
                  :province.sync="query.provinceCode"
                  :city.sync="query.cityCode"
                  :area.sync="query.regionCode"
                  is-all
                />
              </div>
            </el-col>
          </el-row>
        </div>
        <div class="search-box">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">渠道类型</div>
              <el-select v-model="query.channelId" placeholder="请选择渠道类型">
                <el-option
                  v-for="item in channelType"
                  v-show="item.value != 1"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
              <!-- <el-input
                class="mar-l-16"
                style="width:352px;"
                v-model="query.contactUserName"
                placeholder="请输入商务姓名"
                @keyup.enter.native="handleSearch"
              /> -->
            </el-col>
            <el-col :span="6">
              <div class="title">是否设置支付方式</div>
              <el-select v-model="query.paymentMethodScope" placeholder="请选择支付方式">
                <el-option :key="0" label="全部" :value="0"></el-option>
                <el-option :key="1" label="已设置" :value="1"></el-option>
                <el-option :key="2" label="未设置" :value="2"></el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <div class="title">是否设置采购关系</div>
              <el-select v-model="query.purchaseRelationScope" placeholder="请选择采购关系">
                <el-option :key="0" label="全部" :value="0"></el-option>
                <el-option :key="1" label="已设置" :value="1"></el-option>
                <el-option :key="2" label="未设置" :value="2"></el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <div class="title">是否设置商务负责人</div>
              <el-select v-model="query.customerContactScope" placeholder="请选择设置商务负责人">
                <el-option :key="0" label="全部" :value="0"></el-option>
                <el-option :key="1" label="已设置" :value="1"></el-option>
                <el-option :key="2" label="未设置" :value="2"></el-option>
              </el-select>
            </el-col>
          </el-row>
        </div>
        <div class="search-box">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn :total="total" @search="handleSearch" @reset="handleReset" />
            </el-col>
          </el-row>
        </div>
      </div>
      <!-- 导出、导入按钮 -->
      <div class="down-box clearfix">
        <div class="btn">
          <ylButton type="primary" v-role-btn="['3']" plain @click="importClick">批量导入业务员</ylButton>
          <ylButton v-role-btn="['1']" type="primary" plain @click="downLoadTemp">导出查询结果</ylButton>
          <!--<ylButton type="primary" plain @click="importBtnClick">批量导入采购关系</ylButton>-->
        </div>
      </div>
      <!-- 底部列表 -->
      <div class="mar-t-8 bottom-content-view" style="padding-bottom: 10px;background: #FFFFFF;">
        <yl-table
          :list="dataList"
          :total="total"
          :page.sync="query.page"
          :limit.sync="query.limit"
          :loading="loading"
          :horizontal-border="false"
          :cell-no-pad="true"
          @getList="getList"
        >
          <el-table-column>
            <template slot-scope="{ row, $index }">
              <div class="table-view" :key="$index">
                <div class="session font-size-base flex-between">
                  <div>
                    <span>
                      企业ID：
                      <span>{{ row.customerEid }}</span>
                    </span>
                    <span class="mar-l-32">
                      账户状态：
                      <span :class="[row.status == '启用' ? 'enable' : 'disable']">{{ row.status }}</span>
                    </span>
                  </div>
                  <div>
                    <span>创建时间：{{ row.createTime | formatDate }}</span>
                  </div>
                </div>
                <div class="content">
                  <div class="content-left table-item">
                    <div
                      class="item font-size-lg bold"
                      style="margin-bottom:12px;"
                    >{{ row.customerName }}</div>
                    <div class="item font-size-base font-title-color">
                      <span class="font-light-color">渠道类型：</span>
                      {{
                      row.channelName }}
                    </div>
                    <div class="item font-size-base font-title-color">
                      <span class="font-light-color">采购关系：</span>
                      <yl-button
                        type="text"
                        @click="viewInfoClick(row)"
                      >{{ row.purchaseRelationNum }}家</yl-button>
                    </div>
                    <div class="item font-size-base font-title-color">
                      <span class="font-light-color">社会统一信用代码：</span>
                      {{
                      row.licenseNumber }}
                    </div>
                    <div class="item font-size-base font-title-color">
                      <span class="font-light-color">企业地址：</span>
                      {{ row.address }}
                    </div>
                  </div>
                  <div class="content-center">
                    <div class="content-center-top">
                      <div
                        class="flex-row-left"
                        v-for="item in row.customerPaymentMethods"
                        :key="item.id"
                      >
                        <div class="line-view"></div>
                        <span class="font-size-large font-title-color">{{ item.name }}</span>
                      </div>

                      <!-- 未设置支付方式 -->
                      <div
                        v-if="!row.customerPaymentMethods || row.customerPaymentMethods.length == 0"
                      >
                        <span class="font-size-large" style="color:#EE0C24;">尚未设置支付方式，客户无法下单</span>
                      </div>
                    </div>
                    <div class="content-center-bottom">
                      <div class="item font-size-base font-title-color">
                        负责该渠道商的业务人员个数
                        <span
                          class="font-light-color"
                          style="color: #1790FF;"
                        >{{ row.customerContactNum }}</span>
                        个
                      </div>
                    </div>
                  </div>
                  <div class="content-right flex-column-center">
                    <div>
                      <yl-button v-role-btn="['2']" type="text" @click="showDetail(row)">查看并修改</yl-button>
                    </div>
                    <!-- <div>
                      <yl-button type="text" @click="handleSearch">修改日志</yl-button>
                    </div>-->
                  </div>
                </div>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
      <!-- 渠道商弹框 -->
      <yl-dialog
        title="查看"
        :visible.sync="showDialog"
        :show-footer="false"
        :destroy-on-close="true"
      >
        <div class="channel-content">
          <div class="flex-row-left item">
            <div class="line-view"></div>
            <span class="font-size-lg bold">渠道商信息</span>
          </div>
          <div class="font-size-base item-text">{{ currentItem.customerName }}</div>
          <div class="font-size-base item-text">{{ currentItem.channelName }}</div>
          <div class="font-size-base item-text">
            负责该渠道商的业务员个数
            <span style="color:#1790FF;">{{ currentItem.customerContactNum }}</span>个
          </div>
          <div class="flex-row-left item mar-t-10">
            <div class="line-view"></div>
            <span class="font-size-lg bold">采购关系信息</span>
          </div>
          <el-tabs
            v-if="channelType.length > 0"
            class="my-tabs"
            v-model="purchaseQuery.purchaseRelationType"
            @tab-click="handleTabClick"
          >
            <el-tab-pane :label="channelType[0].label + '(' + industryCount + ')'" name="1"></el-tab-pane>
            <el-tab-pane :label="channelType[1].label + '(' + industryDirectCount + ')'" name="2"></el-tab-pane>
            <el-tab-pane :label="channelType[2].label + '(' + level1Count + ')'" name="3"></el-tab-pane>
            <el-tab-pane :label="channelType[3].label + '(' + level2Count + ')'" name="4"></el-tab-pane>
            <el-tab-pane :label="channelType[5].label + '(' + z2p1Count + ')'" name="6"></el-tab-pane>
          </el-tabs>
          <!-- 采购关系信息 列表 -->
          <div class="mar-t-10" style="padding-bottom: 10px;background: #FFFFFF;">
            <yl-table
              :list="purchaseList"
              :total="purchaseTotal"
              :page.sync="purchaseQuery.page"
              :limit.sync="purchaseQuery.limit"
              :loading1="loading1"
              :cell-no-pad="true"
              @getList="getPurList"
            >
              <el-table-column>
                <template slot-scope="{ row, $index }">
                  <div class="purchase-table-view" :key="$index">
                    <div class="list-item">
                      <div class="item font-size-small bold">{{ row.name }}</div>
                      <div class="item font-size-small font-light-color">
                        <span class="font-light-color">企业 ID：</span>
                        {{
                        row.eid }}
                      </div>
                      <div class="item font-size-small font-light-color">
                        <span class="font-light-color">企业地址：</span>
                        {{
                        row.address }}
                      </div>
                    </div>
                  </div>
                </template>
              </el-table-column>
            </yl-table>
          </div>
        </div>
      </yl-dialog>
      <!-- 批量导入渠道类型弹框 -->
      <yl-dialog title :visible.sync="showimPortDialog" :show-footer="false">
        <div class="channel-content">
          <div class="flex-row-left item">
            <div class="line-view"></div>
            <span class="font-size-lg bold">渠道商信息</span>
          </div>
          <div class="font-size-base item-text">{{ currentItem.customerName }}</div>
          <div class="font-size-base item-text">{{ currentItem.channelName }}</div>
          <div class="font-size-base item-text">
            负责该渠道商的业务员个数
            <span style="color:#1790FF;">3</span>个
          </div>
          <div class="flex-row-left item mar-t-10">
            <div class="line-view"></div>
            <span class="font-size-lg bold">采购关系信息</span>
          </div>
        </div>
      </yl-dialog>
    </div>
    <!-- 导入 -->
    <import-send-dialog :visible.sync="importSendVisible" :excel-code="info.excelCode" ref="importSendRef"></import-send-dialog>
  </div>
</template>

<script>
import {
  getChannelList,
  getChannelCount,
  getPurchaseList
} from '@/subject/pop/api/channel';
import { createDownLoad } from '@/subject/pop/api/common';
import { channelType } from '@/subject/pop/utils/busi';
import { ylChooseAddress } from '@/subject/pop/components';
import { getCurrentUser } from '@/subject/pop/utils/auth';
import ImportSendDialog from '@/subject/pop/components/ImportSendDialog'
export default {
  name: 'Channel',
  components: {
    ylChooseAddress,
    ImportSendDialog
  },
  computed: {
    channelType() {
      return channelType();
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
          title: '客户管理'
        },
        {
          title: '渠道管理'
        }
      ],
      loading: false,
      query: {
        page: 1,
        limit: 10,
        paymentMethodScope: 0,
        purchaseRelationScope: 0,
        customerContactScope: 0
      },
      total: 0,
      dataList: [],
      // 渠道商弹框
      showDialog: false,
      //批量导入渠道类型弹框
      showimPortDialog: false,
      // 当前查看的采购关系
      currentItem: {},
      industryCount: 0,
      industryDirectCount: 0, //以岭直采个数
      level1Count: 0, //一级商个数
      level2Count: 0, //二级商个数
      // 专二普一商个数
      z2p1Count: 0,
      purchaseQuery: {
        //查询渠道商采购关系信息分页参数
        age: 1,
        limit: 10,
        purchaseRelationType: '1' //类型：0-以岭直采，1-一级商，2-二级商
      },
      loading1: false,
      purchaseList: [], //查询渠道商采购关系列表
      purchaseTotal: 0,
      // 导入展示
      importSendVisible: false,
      // 导入Code
      info: {
        excelCode: 'importCustomerContact'
      }
    };
  },
  activated() {
    this.getList();
  },
  methods: {
    async getList() {
      this.loading = true;
      let query = this.query;
      let data = await getChannelList(
        query.page,
        query.limit,
        query.name, //渠道商名称
        query.provinceCode,
        query.cityCode,
        query.regionCode,
        query.licenseNumber, //统一信用代码
        query.channelId,
        query.contactUserName, //商务联系人
        query.paymentMethodScope,
        query.purchaseRelationScope,
        query.customerContactScope
      );
      this.loading = false;
      if (data) {
        this.dataList = data.records;
        this.total = data.total;
      }
    },
    // 搜索点击
    handleSearch() {
      this.query.page = 1
      this.getList();
    },
    handleReset() {
      this.query = {
        page: 1,
        limit: 10,
        paymentMethodScope: 0,
        purchaseRelationScope: 0,
        customerContactScope: 0
      };
    },
    //tab切换
    handleTabClick(tab, event) {
      this.purchaseQuery.page = 1;
      this.purchaseQuery.limit = 10;
      this.getPurList();
    },
    // 渠道商弹框
    async viewInfoClick(row) {
      console.log(row);
      this.purchaseQuery.purchaseRelationType = '1';
      this.currentItem = row;
      this.showDialog = true;

      //// 获取采购商数量
      let data = await getChannelCount(row.customerEid);
      this.industryCount = data.industryCount || 0;
      this.industryDirectCount = data.industryDirectCount || 0;
      this.level1Count = data.level1Count || 0;
      this.level2Count = data.level2Count || 0;
      this.z2p1Count = data.z2p1Count || 0;

      this.getPurList();
    },
    // 获取采购关系信息分页列表
    async getPurList() {
      this.loading1 = true;
      let purchaseQuery = this.purchaseQuery;
      let purchaseData = await getPurchaseList(
        purchaseQuery.page,
        purchaseQuery.limit,
        parseInt(purchaseQuery.purchaseRelationType), //渠道商名称
        this.currentItem.customerEid
      );

      this.loading1 = false;
      this.purchaseList = purchaseData.records;
      this.purchaseTotal = purchaseData.total;

      this.$log('purchaseData:', purchaseData);
    },
    //跳转详情界面
    showDetail(row) {
      this.$log('row:', row);
      this.$router.push({
        name: 'ChannelEdit',
        params: { customerEid: row.customerEid, eid: row.eid }
      });
    },
    // 下载模板
    async downLoadTemp() {
      let user = getCurrentUser();
      let query = this.query;
      this.$common.showLoad();
      let data = await createDownLoad({
        className: 'channelCustomerExportService',
        fileName: 'string',
        groupName: '渠道商列表',
        menuName: '客户管理-渠道商列表',
        searchConditionList: [
          {
            desc: '企业ID',
            name: 'eid',
            value: user.id || ''
          },
          {
            desc: '渠道商名称',
            name: 'name',
            value: query.name || ''
          },
          {
            desc: '省份编码',
            name: 'provinceCode',
            value: query.provinceCode || ''
          },
          {
            desc: '城市编码',
            name: 'cityCode',
            value: query.cityCode || ''
          },
          {
            desc: '区域编码',
            name: 'regionCode',
            value: query.regionCode || ''
          },
          {
            desc: '社会信用统一代码',
            name: 'licenseNumber',
            value: query.licenseNumber || ''
          },
          {
            desc: '渠道类型',
            name: 'channelId',
            value: query.channelId || ''
          },
          {
            desc: '商务姓名',
            name: 'contactUserName',
            value: query.contactUserName || ''
          },
          {
            desc: '是否设置支付方式：0-全部 1-已设置 2-未设置',
            name: 'paymentMethodScope',
            value: query.paymentMethodScope
          },
          {
            desc: '是否设置采购关系：0-全部 1-已设置 2-未设置',
            name: 'purchaseRelationScope',
            value: query.purchaseRelationScope
          },
          {
            desc: '是否设置商务负责人：0-全部 1-已设置 2-未设置',
            name: 'customerContactScope',
            value: query.customerContactScope
          }
        ]
      });
      this.$log('data:', data);
      this.$common.hideLoad();
      if (data && data.result) {
        this.$common.n_success('创建下载任务成功，请在下载中心查看');
      }
    },
    //批量导入业务员
    importClick() {
      this.importSendVisible = true
      this.$nextTick( () => {
        this.$refs.importSendRef.init()
      })
    }
    //批量导入渠道类型弹框
    // importBtnClick() {
    //   // this.showimPortDialog = true
    // this.$router.push({
    //   path: '/importFile/importFile_index',
    //   query: {
    //     action: '/admin/pop/api/v1/purchase/relation/importData'
    //   }
    // })
    // }
  }
};
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
