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
        <div class="detail-item">
          <div class="detail-item-left font-title-color">{{ agreementInfo.category == 1 ? '年度协议' : '补充协议' }}</div>
          <div></div>
        </div>
        <div class="detail-item">
          <div class="detail-item-left font-title-color">协议主体 :</div>
          <div class="detail-item-right">{{ agreementInfo.ename }}</div>
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
      </div>
      <!-- 补充协议 -->
      <div class="top-box">
        <div class="flex-row-left item" style="margin-bottom:4px;">
          <div class="line-view"></div>
          <span class="font-size-lg bold">补充协议</span>
        </div>
        <div class="font-size-base font-title-color item-text" style="margin-bottom:0;">该年度协议下共有 <span>{{ agreementInfo.supplementAgreementNum || 0 }}</span> 个补充协议 <yl-button class="view-btn" type="text" @click="viewSupplementalDialog">查看</yl-button></div>
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
    <!-- 补充协议列表 -->
    <yl-dialog
      title="补充协议列表"
      :visible.sync="supplementalVisible"
      :show-footer="false"
      :destroy-on-close="true"
      width="1200px"
    >
      <div class="append-channel-content">
        <!-- 头部年度协议信息 -->
        <div class="top-view">
          <div class="flex-row-left item">
            <div class="line-view"></div>
            <span class="font-size-lg bold">年度协议</span>
          </div>
          <div style="padding-left:7px;">
            <el-row class="box">
              <el-col :span="12">
                <div class="font-title-color font-size-base">协议ID：<span class="font-important-color">{{ agreementInfo.id }}</span></div>
              </el-col>
              <el-col :span="12">
                <div class="font-title-color font-size-base">签订时间：<span class="font-important-color">{{ agreementInfo.createTime | formatDate }}</span></div>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <div class="font-title-color font-size-base"><span class="font-important-color">{{ agreementInfo.name }}</span></div>
              </el-col>
              <el-col :span="12">
                <div class="font-title-color font-size-base">履约时间：<span class="font-important-color">{{ agreementInfo.startTime | formatDate }} - {{ agreementInfo.endTime | formatDate }}</span></div>
              </el-col>
            </el-row>
          </div>
        </div>
        <div class="tabs-view">
          <el-tabs
            class="my-tabs"
            v-model="agreementQuery.agreementStatus"
            @tab-click="handleTabClick"
          >
            <el-tab-pane :label="agreementStatus[0].label + ' (' + start + ')'" name="1"></el-tab-pane>
            <el-tab-pane :label="agreementStatus[1].label + ' (' + unStart + ')'" name="2"></el-tab-pane>
            <el-tab-pane :label="agreementStatus[2].label + ' (' + stop + ')'" name="3"></el-tab-pane>
            <el-tab-pane :label="agreementStatus[3].label + ' (' + expire + ')'" name="4"></el-tab-pane>
          </el-tabs>
          <div class="font-size-base font-title-color num-view">补充协议共{{ agreementInfo.supplementAgreementNum || 0 }}个</div>
        </div>
        <!-- 列表 -->
        <div style="padding-bottom: 10px;background: #FFFFFF;">
          <yl-table
            :list="supplementList"
            :total="supplementTotal"
            :page.sync="agreementQuery.page"
            :limit.sync="agreementQuery.limit"
            :loading="loading2"
            :cell-no-pad="true"
            @getList="getSupplementAgreementPageListMethod"
          >
            <el-table-column>
              <template slot-scope="{ row, $index }">
                <div class="supplementa-table-view" :key="$index">
                  <div class="list-item">
                    <div class="item-top">
                      <div class="top-left-view">
                        <div class="item font-size-small font-important-color"><span class="font-light-color">协议ID：</span>{{ row.id }}</div>
                        <div class="item font-size-small font-important-color"><span class="font-light-color">履约时间：</span>{{ row.startTime | formatDate }}-{{ row.endTime | formatDate }}</div>
                      </div>
                      <div class="top-left-view">
                        <div class="item font-size-small font-important-color"><span class="font-light-color">创建人：</span>{{ row.createUserName }}</div>
                        <div class="item font-size-small font-important-color"><span class="font-light-color">创建时间：</span>{{ row.createTime | formatDate }}</div>
                      </div>
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
                            修改人：<span class="font-important-color">{{ row.updateUserName }}</span>
                          </div>
                          <div class="time-view font-size-small font-light-color">修改时间：<span class="font-important-color">{{ row.updateTime | formatDate }}</span></div>
                        </div>
                      </div>
                      <div class="item-right flex-column-center">
                        <div>
                          <yl-button type="text" @click="showDetailClick(row)">查看</yl-button>
                        </div>
                        <div v-if="row.agreementStatus == 1">
                          <yl-button v-if="false" type="text" @click="stopDetailClick(row)">停用</yl-button>
                        </div>
                        <div v-if="row.agreementStatus == 2">
                          <yl-button v-if="false" type="text" @click="deleteDetailClick(row)">删除</yl-button>
                        </div>
                        <div v-if="row.agreementStatus == 1 || row.agreementStatus == 2">
                          <yl-button v-if="false" type="text" @click="editDetailClick(row)">修改</yl-button>
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
    <!-- 年度协议停用弹框 -->
    <yl-dialog
        title="年度协议停用"
        :visible.sync="stopVisible"
        :show-footer="true"
        :destroy-on-close="true"
        @confirm="confirm"
      >
        <div class="stop-content">
          补充协议停用后不可在启用，是否继续执行？
        </div>
    </yl-dialog>
  </div>
</template>

<script>

import {
  getCustomerEnterpriseInfo,
  getYearAgreementsDetail,
  getGoodsInfoList,
  getSupplementAgreementPageList,
  agreementClose,
  getTempAgreementStatusCount
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
          title: '年度协议详情页'
        }
      ],
      enterpriseInfo: {},
      customerContactCount: 0,
      loading: false,
      query: {
        page: 1,
        limit: 10
      },
      total: 0,
      dataList: [],
      // 协议信息
      agreementInfo: {},
      // 补充协议列表 弹框
      supplementalVisible: false,
      agreementStatus: [
        {
          label: '进行中'
        },
        {
          label: '未开始'
        },
        {
          label: '已停用'
        },
        {
          label: '已过期'
        }
      ],
      agreementQuery: {
        page: 1,
        limit: 10,
        agreementStatus: '1'
      },
      loading2: false,
      supplementList: [],
      supplementTotal: 0,
      // 年度协议弹框
      stopVisible: false,
      // 当前选择的协议
      currentSelectAgreement: {},
      start: 0,
      unStart: 0,
      stop: 0,
      expire: 0
    };
  },
  mounted() {
    this.params = this.$route.params;
    // 获取企业信息
    this.getCustomerEnterpriseInfoMethod()
    // 获取年度协议信息
    this.getYearAgreementsDetailMethod()
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
        params.id
      );
      this.loading = false;
      this.dataList = data.records;
      this.total = data.total;

      this.$log('this.dataList:', this.dataList);
    },
    async getCustomerEnterpriseInfoMethod() {
      let params = this.params
      let data = await getCustomerEnterpriseInfo(
        params.customerEid
      );
      this.enterpriseInfo = data.enterpriseInfo
      this.customerContactCount = data.customerContactCount
      this.$log('this.dataList:', data);
    },
    async getYearAgreementsDetailMethod() {
      let params = this.params
      let data = await getYearAgreementsDetail(
        params.customerEid,
        params.id
      );
      this.agreementInfo = data

      this.$log('this.dataList:', data);
    },
    // 查看补充协议弹框
    viewSupplementalDialog() {
      this.supplementalVisible = true
      //获取补充协议列表
      this.getSupplementAgreementPageListMethod();
      //根据年度协议id查询议状态数量
      this.getTempAgreementStatusCountMethod();
    },
    async getTempAgreementStatusCountMethod() {
      let params = this.params
      let data = await getTempAgreementStatusCount(
        params.id
      );
      if (data) {
        this.start = data.start
        this.unStart = data.unStart
        this.stop = data.stop
        this.expire = data.expire
      }
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

    },
    // 查看点击
    showDetailClick(row) {
      //跳转补充协议详情界面
      this.$log('showDetailClick:', row);
      this.$router.push({
        name: 'SupplementDetail',
        params: { supplementId: row.id }
      });
    },
    // 停用 弹框
    stopDetailClick(row) {
      this.currentSelectAgreement = row
      this.stopVisible = true
    },
    // 删除协议点击
    deleteDetailClick(row) {
      this.currentSelectAgreement = row
      this.agreementDeletdMethod()
    },
    // 修改协议点击
    editDetailClick(row) {
      this.$log('row:', row);
      let protocolType = 2 // 1-年度协议 2-补充协议
      let params = this.params
      this.$router.push({
        name: 'ProtocolEdit',
        params: { customerEid: params.customerEid, agreementId: row.id, agreementType: protocolType }
      });
    },
    // 年度协议停用 确认点击
    confirm() {
      this.agreementCloseMethod()
    },
    async agreementCloseMethod() {
      this.$common.showLoad();
      let currentAgreement = this.currentSelectAgreement
      let data = await agreementClose(
        currentAgreement.id,
        2
      );
      this.$common.hideLoad();
      if (data && data.result) {
        this.$common.n_success('停用成功');
        this.stopVisible = false
        this.getSupplementAgreementPageListMethod(); //刷新列表
      }
    },
    //  协议删除
    async agreementDeletdMethod() {
      this.$common.showLoad();
      let currentAgreement = this.currentSelectAgreement
      let data = await agreementClose(
        currentAgreement.id,
        1
      );
      this.$common.hideLoad();
      if (data && data.result) {
        this.$common.n_success('删除成功');
        this.stopVisible = false
        this.getSupplementAgreementPageListMethod(); //刷新列表
        // 获取年度协议信息
        this.getYearAgreementsDetailMethod()
      }
    }

  }
};
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
