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
                <el-input v-model.trim="query.userId" @keyup.enter.native="searchEnter" @input="e => (query.userId = checkInput(e))" placeholder="请输入用户ID" />
            </el-col>
            <el-col :span="6">
              <div class="title">用药福利计划名称</div>
              <el-select filterable v-model="query.drugWelfareId" placeholder="请选择">
                <el-option
                  v-for="item in benefitPlanData"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <div class="title">终端商家</div>
              <el-select v-model="query.eid" filterable placeholder="请选择">
                <el-option
                  v-for="item in businessData"
                  :key="item.eid"
                  :label="item.ename"
                  :value="item.eid">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <div class="title">商家销售人员</div>
              <el-select filterable v-model="query.sellerUserId" placeholder="请选择">
                <el-option
                  v-for="item in salesmanData"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id">
                </el-option>
              </el-select>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">服药人姓名</div>
                <el-input v-model.trim="query.medicineUserName" @keyup.enter.native="searchEnter" placeholder="请输入服药人姓名" />
            </el-col>
            <el-col :span="6">
              <div class="title">服药人手机号</div>
                <el-input v-model.trim="query.medicineUserPhone" @keyup.enter.native="searchEnter" @input="e => (query.medicineUserPhone = checkInput(e))" placeholder="请输入服药人手机号" />
            </el-col>
            <el-col :span="6">
              <div class="title">用户入组ID</div>
                <el-input v-model.trim="query.joinGroupId" @keyup.enter.native="searchEnter" @input="e => (query.joinGroupId = checkInput(e))" placeholder="请输入用户入组ID" />
            </el-col>
            <el-col :span="6">
              <div class="title">入组时间</div>
                <el-date-picker
                  v-model="query.establishTime"
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
            <el-col :span="16">
              <yl-search-btn :total="query.total" @search="handleSearch" @reset="handleReset"/>
            </el-col>
          </el-row>
        </div>
      </div>
      <div class="down-box">
        <ylButton type="primary" @click="downloadGroup">导出入组数据</ylButton>
        <ylButton type="primary" @click="downloadWelfare">导出福利券统计数据</ylButton>
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
          <el-table-column align="center" min-width="80" label="用户ID" prop="userId">
          </el-table-column>
          <el-table-column align="center" min-width="300" label="用户信息">
            <template slot-scope="{ row }">
              <el-image class="img" :src="row.avatarUrl">
                <div slot="error" class="image-slot">
                  <i class="el-icon-picture-outline"></i>
                </div>
              </el-image>
              <div class="user-right">
                <p><span>昵称：</span>{{ row.nickName }}</p>
                <p><span>性别：</span>{{ row.gender == 1 ? '男' : (row.gender == 2 ? '女' : '未知') }}</p>
                <p><span>联系方式：</span>{{ row.mobile }}</p>
              </div>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="230" label="用药福利计划名称">
            <template slot-scope="{ row }">
              {{ row.drugWelfareName }} 【ID：{{ row.drugWelfareId }}】
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="150" label="终端商家" prop="ename">
          </el-table-column>
          <el-table-column align="center" min-width="100" label="商家销售人员" prop="sellerUserName">
          </el-table-column>
          <el-table-column align="center" min-width="100" label="服药人姓名" prop="medicineUserName">
          </el-table-column>
          <el-table-column align="center" min-width="110" label="服药人手机号" prop="medicineUserPhone">
          </el-table-column>
          <el-table-column align="center" min-width="90" label="使用进度">
            <template slot-scope="{ row }">
              <div>{{ row.useSchedule }}</div>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="100" label="用户入组ID" prop="joinGroupId">
          </el-table-column>
          <el-table-column align="center" min-width="100" label="入组时间" >
            <template slot-scope="{ row }">
              <div>
                {{ row.createTime | formatDate }}
              </div>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="150" label="有效期">
            <template slot-scope="{ row }">
              <div>
                {{ row.beginTime | formatDate }}
                至
                {{ row.endTime | formatDate }}
              </div>
            </template>
          </el-table-column>
          <el-table-column fixed="right" align="center" label="操作" min-width="130">
            <template slot-scope="{ row }">
              <div>
                <yl-button type="text" @click="statisticsClick(row)">福利券使用统计</yl-button>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
    <yl-dialog 
      :visible.sync="reviewShow" 
      width="800px" 
      title="福利卷使用统计" 
      :show-footer="false">
      <div class="dialog-content">
        <yl-table 
          border 
          :show-header="true" 
          :list="dialogDataList" 
          :loading="dialogLoading">
          <el-table-column align="center" min-width="150" label="福利详情">
            <template slot-scope="{ row }">
              <div>
                单次满{{ row.requirementNumber }}盒赠{{ row.giveNumber }}盒
              </div>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="100" label="券码" prop="couponCode">
          </el-table-column>
          <el-table-column align="center" min-width="100" label="激活时间">
            <template slot-scope="{ row }">
              <div>
                {{ row.activeTime | formatDate }}
              </div>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="90" label="状态">
            <template slot-scope="{ row }">
              <div>
                {{ row.couponStatus == 1 ? '待激活' : (row.couponStatus == 2 ? '已激活' : (row.couponStatus == 3 ? '已核销' : '')) }}
              </div>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="90" label="谁核销的" prop="verificationName">
          </el-table-column>
          <el-table-column align="center" min-width="100" label="核销时间">
            <template slot-scope="{ row }">
              <div>
                {{ row.verifyTime | formatDate }}
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </yl-dialog>
  </div>
</template>
<script>
import { queryEnterpriseList, queryDrugWelfareList } from '@/subject/admin/api/cmp_api/benefit_plan'
import { querySellerUser, queryPage, queryCouponList } from '@/subject/admin/api/cmp_api/insurance_statistical'
import { createDownLoad } from '@/subject/admin/api/common'
export default {
  name: 'WelfareStatistics',
  computed: {},
  data() {
    return {
      query: {
        userId: '',
        drugWelfareId: '',
        eid: '',
        sellerUserId: '',
        medicineUserName: '',
        medicineUserPhone: '',
        joinGroupId: '',
        establishTime: [],
        total: 0,
        page: 1,
        limit: 10
      },
      loading: false,
      // 列表数据
      dataList: [],
      // 终端商家
      businessData: [],
      // 用药福利计划名称
      benefitPlanData: [],
      // 商家销售人员
      salesmanData: [],
      // 福利券是否显示
      reviewShow: false,
      dialogLoading: false,
      dialogDataList: []
    }
  },
  activated() {
    // 获取商家下拉选单
    this.getDusinessList();
    // 获取用药福利计划名称
    this.getBenefitPlan();
    // 获取商家销售人员
    this.getSalesman();
    this.getList();
  },
  methods: {
    // Enter
    searchEnter(e) {
      const keyCode = window.event ? e.keyCode : e.which;
      if (keyCode === 13) {
        this.getList()
      }
    },
    // 获取商家下拉选单
    async getDusinessList() {
      let data = await queryEnterpriseList()
      if (data) {
        this.businessData = data.list;
      }
    },
    // 获取用药福利计划名称
    async getBenefitPlan() {
      let data = await queryDrugWelfareList()
      if (data) {
        this.benefitPlanData = data.list;
      }
    },
    // 获取商家销售人员
    async getSalesman() {
      let data = await querySellerUser()
      if (data) {
        this.salesmanData = data.list;
      }
    },
    // 点击福利卷使用统计
    async statisticsClick(row) {
      this.dialogLoading = true;
      let data = await queryCouponList(row.id)
      if (data !== undefined) {
        this.dialogDataList = data.list;
      }
      this.reviewShow = true;
      this.dialogLoading = false;
    },
    // 搜索
    handleSearch() {
      this.query.page = 1;
      this.getList();
    },
    // 重置
    handleReset() {
      this.query = {
        userId: '',
        drugWelfareId: '',
        eid: '',
        sellerUserId: '',
        medicineUserName: '',
        medicineUserPhone: '',
        joinGroupId: '',
        establishTime: [],
        total: 0,
        page: 1,
        limit: 10
      }
    },
    // 列表数据
    async getList() {
      this.loading = true;
      let query = this.query;
      let data = await queryPage(
        query.userId,
        query.drugWelfareId,
        query.eid,
        query.sellerUserId,
        query.medicineUserName,
        query.medicineUserPhone,
        query.joinGroupId,
        query.establishTime && query.establishTime.length > 0 ? query.establishTime[0] : '',
        query.establishTime && query.establishTime.length > 1 ? query.establishTime[1] : '',
        query.page,
        query.limit
      )
      if (data) {
        this.dataList = data.records;
        this.query.total = data.total;
      }
      this.loading = false;
    },
    // 导出组数据
    async downloadGroup() {
      let query = this.query
      this.$common.showLoad()
      let data = await createDownLoad({
        className: 'drugWelfareStatisticsExportService',
        fileName: '导出入组数据',
        groupName: '用药福利统计',
        menuName: '统计 - 用药福利统计',
        searchConditionList: [
          {
            desc: '用户ID',
            name: 'userId',
            value: query.userId || ''
          },
          {
            desc: '用药福利计划名称',
            name: 'drugWelfareId',
            value: query.drugWelfareId || ''
          },
          {
            desc: '终端商家',
            name: 'eid',
            value: query.eid || ''
          },
          {
            desc: '商家销售人员',
            name: 'sellerUserId',
            value: query.sellerUserId || ''
          },
          {
            desc: '服药人姓名',
            name: 'medicineUserName',
            value: query.medicineUserName || ''
          },
          {
            desc: '服药人手机号',
            name: 'medicineUserPhone',
            value: query.medicineUserPhone || ''
          },
          {
            desc: '用户入组ID',
            name: 'joinGroupId',
            value: query.joinGroupId || ''
          },
          {
            desc: '入组开始时间',
            name: 'startTime',
            value: query.establishTime && query.establishTime.length > 0 ? query.establishTime[0] : ''
          },
          {
            desc: '入组结束时间',
            name: 'endTime',
            value: query.establishTime && query.establishTime.length > 1 ? query.establishTime[1] : ''
          }
        ]
      })
      this.$common.hideLoad()
      if (data && data.result) {
        this.$common.n_success('创建下载任务成功，请在下载中心查看')
      }
    },
    // 导出福利券统计数据
    async downloadWelfare() {
      let query = this.query
      this.$common.showLoad()
      let data = await createDownLoad({
        className: 'drugWelfareGroupCouponStatisticsExportService',
        fileName: '导出福利券统计数据',
        groupName: '用药福利统计',
        menuName: '统计 - 用药福利统计',
        searchConditionList: [
          {
            desc: '用户ID',
            name: 'userId',
            value: query.userId || ''
          },
          {
            desc: '用药福利计划名称',
            name: 'drugWelfareId',
            value: query.drugWelfareId || ''
          },
          {
            desc: '终端商家',
            name: 'eid',
            value: query.eid || ''
          },
          {
            desc: '商家销售人员',
            name: 'sellerUserId',
            value: query.sellerUserId || ''
          },
          {
            desc: '服药人姓名',
            name: 'medicineUserName',
            value: query.medicineUserName || ''
          },
          {
            desc: '服药人手机号',
            name: 'medicineUserPhone',
            value: query.medicineUserPhone || ''
          },
          {
            desc: '用户入组ID',
            name: 'joinGroupId',
            value: query.joinGroupId || ''
          },
          {
            desc: '入组开始时间',
            name: 'startTime',
            value: query.establishTime && query.establishTime.length > 0 ? query.establishTime[0] : ''
          },
          {
            desc: '入组结束时间',
            name: 'endTime',
            value: query.establishTime && query.establishTime.length > 1 ? query.establishTime[1] : ''
          }
        ]
      })
      this.$common.hideLoad()
      if (data && data.result) {
        this.$common.n_success('创建下载任务成功，请在下载中心查看')
      }
    },
    checkInput(val) {
      val = val.replace(/[^0-9]/gi, '')
      if (val < 0) {
        val = ''
      }
      return val
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>