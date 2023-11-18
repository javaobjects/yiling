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
          <span class="font-size-lg bold">{{ enterpriseInfo.name || '---' }}</span>
        </div>
        <div class="font-size-base item-text">{{ enterpriseInfo.channelId | dictLabel(channelType) }}</div>
        <div class="font-size-base font-title-color item-text" style="margin-bottom:0;">负责该渠道商的业务人员个数：<span style="color:#1790FF;">{{ customerContactCount }}个</span></div>
      </div>
      <!-- 新增协议 -->
      <div class="add-view">
        <div class="title">数据列表</div>
        <yl-button v-if="false" type="primary" plain @click="addProtocolClick">新增协议</yl-button>
      </div>
      <div class="top-bar">
        <div class="content-box-bottom">
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
          <!-- 列表 -->
          <div class="mar-t-10" style="padding-bottom: 10px;background: #FFFFFF;">
            <yl-table
              :list="dataList"
              :total="total"
              :page.sync="agreementQuery.page"
              :limit.sync="agreementQuery.limit"
              :loading="loading"
              :cell-no-pad="true"
              @getList="getList"
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
                        <div>共有 <span style="color:#1790FF;">{{ row.tempCount }}</span> 个补充协议</div>
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
      </div>
      <div class="flex-row-center bottom-view">
        <yl-button plain type="primary" @click="$router.go(-1)">返回</yl-button>
      </div>
      <!-- 年度协议停用弹框 -->
      <yl-dialog
          title="年度协议停用"
          :visible.sync="stopVisible"
          :show-footer="true"
          :destroy-on-close="true"
          @confirm="confirm"
        >
          <div class="stop-content">
            <span v-show="currentSelectAgreement.tempCount && currentSelectAgreement.tempCount > 0">该年度协议下包含 {{ currentSelectAgreement.tempCount }} 个临时协议，</span>若停用年度协议后临时协议将全部停用，协议停用后不可在启用，是否继续执行？
          </div>
      </yl-dialog>
    </div>
  </div>
</template>

<script>

import {
  getAgreementPageList,
  getCustomerEnterpriseInfo,
  getAgreementStatusCount,
  agreementClose
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
          title: '协议列表'
        }
      ],
      // 顶部企业信息
      enterpriseInfo: {},
      // 业务人员数
      customerContactCount: 0,
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
      start: 0,
      unStart: 0,
      stop: 0,
      expire: 0,
      loading: false,
      agreementQuery: {
        page: 1,
        limit: 10,
        agreementStatus: '1'
      },
      total: 0,
      dataList: [
        {
          title: '1'
        }
      ],
      // 年度协议弹框
      stopVisible: false,
      // 当前选择的协议
      currentSelectAgreement: {}
    };
  },
  mounted() {
    this.params = this.$route.params;
    // 获取企业信息
    this.getCustomerEnterpriseInfoMethod()
    this.getYearAgreementStatusCountMethod()
    this.getList()
  },
  methods: {
    async getYearAgreementStatusCountMethod() {
      let params = this.params
      let data = await getAgreementStatusCount(
        params.customerEid,
        1
      );
      this.start = data.start
      this.unStart = data.unStart
      this.stop = data.stop
      this.expire = data.expire

      this.$log('this.dataList:', data);
    },
    async getCustomerEnterpriseInfoMethod() {
      let params = this.params
      let data = await getCustomerEnterpriseInfo(
        params.customerEid
      );
      if (data) {
        this.enterpriseInfo = data.enterpriseInfo || {}
        this.customerContactCount = data.customerContactCount
        this.$log('this.dataList:', data);
      }
    },
    async getList() {
      this.loading = true;
      let params = this.params
      let agreementQuery = this.agreementQuery;
      let data = await getAgreementPageList(
        agreementQuery.page,
        agreementQuery.limit,
        parseInt(agreementQuery.agreementStatus),
        params.customerEid
      );
      this.loading = false;
      this.dataList = data.records;
      this.total = data.total;

      this.$log('this.dataList:', this.dataList);
    },
    //tab切换
    handleTabClick(tab, event) {
      this.agreementQuery.page = 1;
      this.agreementQuery.limit = 10;
      this.getList();
    },
    // 跳转新增协议界面
    addProtocolClick() {
      let params = this.params
      this.$router.push({
        name: 'ProtocolAdd',
        params: { customerEid: params.customerEid }
      });
    },
    //跳转详情界面
    showDetailClick(row) {
      this.$log('row:', row);
      let params = this.params
      this.$router.push({
        name: 'ProtocolDetail',
        params: { customerEid: params.customerEid, id: row.id }
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
      let protocolType = 1 // 1-年度协议 2-补充协议
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
        this.getList(); //刷新列表
        //刷新数量
        this.getYearAgreementStatusCountMethod()
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
        this.getList(); //刷新列表
        //刷新数量
        this.getYearAgreementStatusCountMethod()
      }
    }

  }
};
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
