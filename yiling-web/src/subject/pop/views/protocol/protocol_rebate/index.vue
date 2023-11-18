<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <!-- 内容区 -->
    <div class="app-container-content">
      <!-- 头部统计数据 -->
      <div class="top-box">
        <el-row class="box">
          <el-col :span="6">
            <div class="font-size-base font-title-color">协议总数（个）：<span class="font-important-color">{{ totalAgreementCount }}</span></div>
          </el-col>
          <el-col :span="6">
            <div class="font-size-base font-title-color">主协议总数（个）：<span class="font-important-color">{{ yearAgreementCount }}</span></div>
          </el-col>
          <el-col :span="6">
            <div class="font-size-base font-title-color">补充协议总数（个）：<span class="font-important-color">{{ tempAgreementCount }}</span></div>
          </el-col>
        </el-row>
        <div class="tip-box">
          <i class="el-icon-warning"></i>
          <span>包含停用和已到期状态的协议数量</span>
        </div>
      </div>
      <!-- 搜索框 -->
      <div class="common-box">
        <div class="search-box" style="margin-top:0;">
          <el-row class="box">
            <el-col :span="11">
              <div class="title">渠道商名称</div>
              <el-input v-model="query.name" placeholder="请输入要查询的渠道商名称" @keyup.enter.native="handleSearch" />
            </el-col>
            <el-col :span="13">
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
            <el-col :span="11">
              <div class="title">渠道商社会统一信用代码</div>
              <el-input v-model="query.licenseNumber" placeholder="请输入渠道商社会统一信用代码" @keyup.enter.native="handleSearch" />
            </el-col>
            <el-col :span="13">
              <div class="title">渠道类型</div>
              <el-radio-group v-model="query.channelId">
                <el-radio :key="0" :label="0">全部</el-radio>
                <el-radio
                  v-for="item in myChannelType"
                  :key="item.value"
                  :label="item.value"
                >{{ item.label }}</el-radio>
              </el-radio-group>
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
      <!-- 底部列表 -->
      <div style="padding-bottom: 10px;background: #FFFFFF;">
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
                <div class="content">
                  <div class="content-left table-item">
                    <div
                      class="item font-size-lg bold"
                      style="margin-bottom:12px;"
                    >{{ row.customerName }}</div>
                    <div class="item font-size-base font-title-color">
                      <span class="font-light-color">渠道类型：</span>
                      {{ row.channelId | dictLabel(channelType) }}
                    </div>
                    <div class="item font-size-base font-title-color">
                      <span class="font-light-color">负责该渠道商的业务人员个数：</span>
                      <span>{{ row.customerContactNum || '0' }}个</span>
                    </div>
                    <div class="item font-size-base font-title-color">
                      <span class="font-light-color">账户状态：</span>
                      <span v-if="row.status" :class="[row.status == '1' ? 'enable' : 'disable']">{{ row.status == '1' ? '启用' : '停用' }}</span>
                    </div>
                  </div>
                  <div class="content-center">
                    <div class="content-center-top"><span class="font-light-color">双方补充协议数量：</span><span class="num">{{ row.tempAgreementCount }}</span><span class="font-light-color">个</span></div>
                    <div class="content-center-top"><span class="font-light-color">三方补充协议数量：</span><span class="num">{{ row.thirdAgreementCount }}</span><span class="font-light-color">个</span></div>
                    <div class="content-center-top"><span class="font-light-color">年度协议：</span><span class="num">{{ row.yearAgreementCount }}</span><span class="font-light-color">个</span></div>
                    <div class="font-light-color">
                      三方协议共与
                      <yl-button
                        type="text"
                        @click="viewInfoClick(row)"
                      >{{ row.thirdAgreementsOtherCount }}家</yl-button>
                      签订
                      </div>
                  </div>
                  <div class="content-right flex-column-center">
                    <div>
                      <yl-button v-role-btn="['4']" type="text" @click="showDetail(row)">查看</yl-button>
                    </div>
                    <div>
                      <yl-button type="text" v-role-btn="['1']" @click="addProtocolClick(row)">新增协议</yl-button>
                    </div>
                  </div>
                </div>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
      <!-- 查看弹框 -->
      <yl-dialog
        title="查看"
        :visible.sync="showDialog"
        :show-footer="false"
        :destroy-on-close="true"
      >
        <!-- 补充协议列表 -->
        <yl-dialog
          title="补充协议列表"
          :visible.sync="supplementalVisible"
          :show-footer="false"
          :destroy-on-close="true"
          :append-to-body="true"
          width="1200px"
        >
          <div class="append-channel-content">
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
              <div class="font-size-base font-title-color num-view">补充协议共{{ supplementalItem.supplementAgreementCount || 0 }}个</div>
            </div>
            <!-- 列表 -->
            <div style="padding-bottom: 10px;background: #FFFFFF;">
              <yl-table
                :list="supplementList"
                :total="supplementTotal"
                :page.sync="agreementQuery.page"
                :limit.sync="agreementQuery.limit"
                :loading2="loading2"
                :cell-no-pad="true"
                @getList="getEntSupplementAgreementPageListMethod"
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
                              <yl-button type="text" @click="stopDetailClick(row)">停用</yl-button>
                            </div>
                            <div v-if="row.agreementStatus == 2">
                              <yl-button type="text" @click="deleteDetailClick(row)">删除</yl-button>
                            </div>
                            <div v-if="row.agreementStatus == 1 || row.agreementStatus == 2">
                              <yl-button type="text" @click="editDetailClick(row)">修改</yl-button>
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
        <!-- 查看联系人列表 -->
        <yl-dialog
          title="查看联系人"
          :visible.sync="innerVisible"
          :show-footer="false"
          :destroy-on-close="true"
          :append-to-body="true"
        >
          <div class="append-channel-content">
            <div class="channel-item" v-for="item in customerContactList" :key="item.id">
              <div class="font-size-base item-text">商务负责人：<span class="font-important-color">{{ item.name || '---' }}</span></div>
              <div class="font-size-base item-text">负责人联系方式：<span>{{ item.mobile || '---' }}</span></div>
            </div>
          </div>
        </yl-dialog>
        <div class="channel-content">
          <div class="flex-row-left item">
            <div class="line-view"></div>
            <span class="font-size-lg bold">{{ currentItem.customerName }}</span>
          </div>
          <div class="font-size-base item-text">{{ currentItem.channelId | dictLabel(channelType) }}</div>
          <div class="contact-view" v-if="contactList">
            <div v-for="item in contactList" :key="item.id">
              <div class="font-size-base item-text">商务负责人：<span class="font-important-color">{{ item.name || '---' }}</span></div>
              <div class="font-size-base item-text">负责人联系方式：<span>{{ item.mobile || '---' }}</span></div>
            </div>
            <div v-if="contactList && contactList.length > 0">
              <yl-button type="text" @click="showInnerVisible()">查看全部联系人</yl-button>
            </div>
          </div>
          <div class="num">共与<span style="color:#1790FF;">{{ currentItem.thirdAgreementsOtherCount }}家</span>签订三方协议</div>
          <div class="mar-t-10" style="padding-bottom: 10px;background: #FFFFFF;">
            <yl-table
              :list="thirdAgreementEntList"
              :total="thirdAgreementEntTotal"
              :page.sync="thirdQuery.page"
              :limit.sync="thirdQuery.limit"
              :loading1="loading1"
              :cell-no-pad="true"
              @getList="getThirdAgreementEntListMethod"
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
                      <div class="list-item-center font-light-color font-size-base">协议总数：<span class="agreement-count">{{ row.supplementAgreementCount }}</span> <span class="font-size-sm">/个</span></div>
                      <div class="list-item-right"><yl-button type="text" @click="viewSupplementalDialog(row)">查看具体内容</yl-button></div>
                    </div>
                  </div>
                </template>
              </el-table-column>
            </yl-table>
          </div>
        </div>
      </yl-dialog>
      <!-- 补充协议禁用弹框 -->
      <yl-dialog
          title="补充协议停用"
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
  </div>
</template>

<script>
import {
  getAgreementStatistics,
  getAgreementList,
  getCustomerEnterpriseInfo,
  getThirdAgreementEntList,
  getEntSupplementAgreementPageList,
  agreementClose,
  getEntSupplementAgreementCount
} from '@/subject/pop/api/protocol';
import { channelType } from '@/subject/pop/utils/busi';
import { ylChooseAddress } from '@/subject/pop/components';

export default {
  name: 'ProtocolRebate',
  components: {
    ylChooseAddress
  },
  computed: {
    channelType() {
      return channelType();
    },
    myChannelType() {
      let arr = []
      channelType().forEach((item) => {
        if (item.value != '1'){
          arr.push(item)
        }
      })
      return arr;
    },
    contactList() {
      if (this.customerContactList && this.customerContactList.length > 2) {
        return this.customerContactList.slice(0,2)
      } else {
        return this.customerContactList
      }
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
          title: '协议返利'
        }
      ],
      // 协议总数
      totalAgreementCount: 0,
      // 主协议数
      tempAgreementCount: 0,
      yearAgreementCount: 0,
      loading: false,
      query: {
        page: 1,
        limit: 10,
        channelId: 0
      },
      total: 0,
      dataList: [],
      // 补充协议弹框
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
      //当前点击的三方企业
      supplementalItem: {},
      // 商务联系人
      customerContactList: [],
      innerVisible: false,
      // 查看
      showDialog: false,
      // 当前查看的三方协议家数点击
      currentItem: {},
      thirdQuery: {
        page: 1,
        limit: 10
      },
      loading1: false,
      thirdAgreementEntList: [],
      thirdAgreementEntTotal: 0,
      // 年度协议弹框
      stopVisible: false,
      // 当前选择的协议
      currentSelectAgreement: {},
      // 三方补充协议数量
      start: 0,
      unStart: 0,
      stop: 0,
      expire: 0
    };
  },
  activated() {
    this.getStatistics();
    this.getList();
  },
  methods: {
    async getStatistics() {
      this.loading = true;
      let query = this.query;
      let data = await getAgreementStatistics(
        query.page,
        query.limit,
        query.channelId,
        query.name, //渠道商名称
        query.provinceCode,
        query.cityCode,
        query.regionCode,
        query.licenseNumber //统一信用代码
      );
      this.tempAgreementCount = data.tempAgreementCount
      this.yearAgreementCount = data.yearAgreementCount
      this.totalAgreementCount = data.tempAgreementCount + data.yearAgreementCount

      this.$log('this.dataList:', data);
    },
    async getList() {
      this.loading = true;
      let query = this.query;
      let data = await getAgreementList(
        query.page,
        query.limit,
        query.channelId,
        query.name, //渠道商名称
        query.provinceCode,
        query.cityCode,
        query.regionCode,
        query.licenseNumber //统一信用代码
      );
      this.loading = false;
      this.dataList = data.records;
      this.total = data.total;

      this.$log('this.dataList:', this.dataList);
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
        channelId: 0
      };
    },
    // 查看全部联系人
    showInnerVisible() {
      this.innerVisible = true
    },
    //跳转详情界面
    showDetail(row) {
      this.$log('row:', row);
      this.$router.push({
        name: 'ProtocolList',
        params: { customerEid: row.customerEid }
      });
    },
    // 跳转新增协议界面
    addProtocolClick(row) {
      this.$log('row:', row);
      this.$router.push({
        name: 'ProtocolAdd',
        params: { customerEid: row.customerEid }
      });
    },
    // 跳转补充协议详情界面
    showDetailClick(row) {
      this.showDialog = false
      this.supplementalVisible = false
      this.$log('showDetailClick:', row);
      this.$router.push({
        name: 'SupplementDetail',
        params: { supplementId: row.id }
      });
    },
    // 三方协议企业列表弹框
    viewInfoClick(row) {
      console.log(row);

      this.currentItem = row;
      this.showDialog = true;
      this.thirdAgreementEntList = []
      this.thirdAgreementEntTotal = 0
      this.getCustomerEnterpriseInfoMethod(row)
      this.getThirdAgreementEntListMethod(row);
    },
    async getCustomerEnterpriseInfoMethod(row) {
      let data = await getCustomerEnterpriseInfo(
        row.customerEid
      );
      this.customerContactList = data.customerContactList
      this.$log('this.dataList:', data);
    },
    // 查看三方协议签订企业列表
    async getThirdAgreementEntListMethod(item) {
      this.loading1 = true;
      let thirdQuery = this.thirdQuery;
      let data = await getThirdAgreementEntList(
        item.customerEid || this.currentItem.customerEid,
        thirdQuery.page,
        thirdQuery.limit
      );
      this.loading1 = false;
      this.thirdAgreementEntList = data.records;
      this.thirdAgreementEntTotal = data.total;

      this.$log('this.dataList:', this.dataList);
    },
    // 查看补充协议弹框
    viewSupplementalDialog(row) {
      this.supplementalVisible = true
      //当前点击的三方企业
      this.supplementalItem = row
      //获取补充协议列表
      this.getEntSupplementAgreementPageListMethod();
      //获取补充协议数量
      this.getEntSupplementAgreementCountMethod()
    },
    async getEntSupplementAgreementCountMethod() {
      let currentItem = this.currentItem
      let supplementalItem = this.supplementalItem 
      let data = await getEntSupplementAgreementCount(
        currentItem.customerEid,
        supplementalItem.id
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
      this.getEntSupplementAgreementPageListMethod();
    },
    //获取补充协议列表
    async getEntSupplementAgreementPageListMethod() {
      this.loading2 = true;
      let agreementQuery = this.agreementQuery;
      let currentItem = this.currentItem;
      let supplementalItem = this.supplementalItem;
      let data = await getEntSupplementAgreementPageList(
        agreementQuery.page,
        agreementQuery.limit,
        parseInt(agreementQuery.agreementStatus),
        currentItem.customerEid,
        supplementalItem.id
      );
      this.loading2 = false;
      this.supplementList = data.records;
      this.supplementTotal = data.total;
      this.$log('this.dataList:', data);
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
      this.showDialog = false
      this.supplementalVisible = false
      //当前点击的三方企业
      let currentItem = this.currentItem
      let protocolType = 2 // 1-年度协议 2-补充协议
      this.$router.push({
        name: 'ProtocolEdit',
        params: { customerEid: currentItem.customerEid, agreementId: row.id, agreementType: protocolType }
      });
    },
    // 年度协议停用 确认点击
    confirm() {
      this.agreementCloseMethod()
    },
    // 协议停用
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
        this.getEntSupplementAgreementPageListMethod(); //刷新列表
      }
    },
    // 协议删除
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
        this.getEntSupplementAgreementPageListMethod(); //刷新列表
      }
    }
  }
};
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
