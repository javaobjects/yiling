<template>
  <div class="app-container">
    <!-- 内容区域 -->
    <div class="app-container-content">
      <!-- 中部 搜索条件 -->
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">用药福利计划名称</div>
              <el-select filterable v-model="query.drugWelfareId" placeholder="请选择">
                <el-option
                  v-for="item in businessData"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id">
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
            <el-col :span="6">
              <div class="title">服药人姓名</div>
              <el-input v-model.trim="query.medicineUserName" @keyup.enter.native="searchEnter" placeholder="请输入服药人姓名" />
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">服药人手机号</div>
                <el-input v-model.trim="query.medicineUserPhone" @keyup.enter.native="searchEnter" @input="e => (query.medicineUserPhone = checkInput(e))" placeholder="请输入服药人手机号" />
            </el-col>
            <el-col :span="6">
              <div class="title">用户入组ID</div>
                <el-input v-model.trim="query.groupId" @keyup.enter.native="searchEnter" @input="e => (query.groupId = checkInput(e))" placeholder="请输入用户入组ID" />
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
        <el-row>
          <el-col :span="12">
            <el-input v-model.trim="couponCode" placeholder="请输入券码" class="input-with"/>
            <ylButton type="primary" v-role-btn="['3']" @click="confirmClick">确认核销</ylButton>
            <ylButton type="primary" v-role-btn="['4']" @click="recordClick">核销记录</ylButton>
          </el-col>
          <el-col :span="12" class="couponCode">
            <ylButton v-role-btn="['1']" type="primary" @click="downloadGroup">导出入组数据</ylButton>
            <ylButton v-role-btn="['2']" type="primary" @click="downloadWelfare">导出福利券统计数据</ylButton>
          </el-col>
        </el-row>
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
          <el-table-column align="center" min-width="150" label="用药福利计划名称" prop="drugWelfareName">
          </el-table-column>
          <el-table-column align="center" min-width="100" label="商家销售人员" prop="sellerUserName">
          </el-table-column>
          <el-table-column align="center" min-width="100" label="服药人姓名" prop="medicineUserName">
          </el-table-column>
          <el-table-column align="center" min-width="110" label="服药人手机号" prop="medicineUserPhone">
          </el-table-column>
          <el-table-column align="center" min-width="90" label="使用进度" prop="useSchedule">
          </el-table-column>
          <el-table-column align="center" min-width="100" label="用户入组ID" prop="joinGroupId">
          </el-table-column>
          <el-table-column align="center" min-width="150" label="入组时间">
            <template slot-scope="{ row }">
              <div>
                {{ row.createTime | formatDate }}
              </div>
            </template>
          </el-table-column>
          <el-table-column fixed="right" align="center" label="操作" min-width="130">
            <template slot-scope="{ row }">
              <div>
                <yl-button type="text" v-role-btn="['5']" @click="statisticsClick(row)">福利券使用统计</yl-button>
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
import { queryDrugWelfareList, querySellerUser, queryPage, queryCouponList, verificationDrugWelfareGroupCoupon } from '@/subject/pop/api/cmp_api/drugWelfare'
import { createDownLoad } from '@/subject/pop/api/common'
export default {
  name: 'DrugWelfare',
  computed: {},
  data() {
    return {
      query: {
        drugWelfareId: '',
        sellerUserId: '',
        medicineUserName: '',
        medicineUserPhone: '',
        groupId: '',
        establishTime: [],
        total: 0,
        page: 1,
        limit: 10
      },
      loading: false,
      dataList: [],
      // 用药福利计划名称
      businessData: [],
      // 商家销售人员
      salesmanData: [],
      reviewShow: false,
      dialogLoading: false,
      dialogDataList: [],
      // 确定核销 券码
      couponCode: ''
    }
  },
  activated() {
    // 获取顶部用药福利计划名称
    this.drugWelfareList();
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
    // 获取顶部用药福利计划名称
    async drugWelfareList() {
      let data = await queryDrugWelfareList()
      if (data) {
        this.businessData = data.list;
      }
    },
    // 获取商家销售人员
    async getSalesman() {
      let data = await querySellerUser()
      if (data) {
        this.salesmanData = data.list;
      }
    },
    // 点击核销记录
    recordClick() {
      this.$router.push({
        name: 'WriteOffRecord',
        params: {}
      });
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
        drugWelfareId: '',
        sellerUserId: '',
        medicineUserName: '',
        medicineUserPhone: '',
        groupId: '',
        establishTime: [],
        total: 0,
        page: 1,
        limit: 10
      }
    },
    //确认核销
    async confirmClick() {
      if (this.couponCode == '') {
        this.$confirm('券码不能为空!', '提示', {
          confirmButtonText: '确定',
          type: 'error',
          showCancelButton: false
        }).then(() => {
        }).catch(() => {});
      } else {
        this.$common.showLoad()
        let data = await verificationDrugWelfareGroupCoupon(this.couponCode)
        this.$common.hideLoad();
        if (data !== undefined) {
          switch (data.status) {
            case 1:
              this.$confirm('券码输入错误，请检查重试!', '提示', {
                confirmButtonText: '确定',
                type: 'error',
                showCancelButton: false
              }).then(() => {
                this.couponCode = ''
              }).catch(() => {});
              break;
            case 2:
              this.$confirm('券码已经核销，请勿重复操作!', '提示', {
                confirmButtonText: '确定',
                type: 'warning',
                showCancelButton: false
              }).then(() => {
                this.couponCode = ''
              }).catch(() => {});
              break;
            case 3:
              this.$confirm('活动不存在!', '提示', {
                confirmButtonText: '确定',
                type: 'error',
                showCancelButton: false
              }).then(() => {
                this.couponCode = ''
              }).catch(() => {});
              break;
            case 4:
              this.$confirm('活动未开始!', '提示', {
                confirmButtonText: '确定',
                type: 'warning',
                showCancelButton: false
              }).then(() => {
                this.couponCode = ''
              }).catch(() => {});
              break;
            case 5:
              this.$confirm('活动已过期!', '提示', {
                confirmButtonText: '确定',
                type: 'warning',
                showCancelButton: false
              }).then(() => {
                this.couponCode = ''
              }).catch(() => {});
              break;
            case 6:
              this.$confirm('活动已结束!', '提示', {
                confirmButtonText: '确定',
                type: 'warning',
                showCancelButton: false
              }).then(() => {
                this.couponCode = ''
              }).catch(() => {});
              break;
            case 7:
              this.$alert(
              `<div>
                <p>用药福利：${ data.drugWelfareName }</p>
                <p>入组姓名：${ data.medicineUserName }</p>
                <p>入组ID：${ data.joinGroupId }</p>
                <p>核销成功，福利券已发放至您的大运河平台账户中，请登录大运河App个人中心查看</p>
              </div>`,
              {
                dangerouslyUseHTMLString: true,
                type: 'success'
              });
              this.getList();
              break;
          }
        }
      }
    },
    async getList() {
      this.loading = true;
      let query = this.query;
      let data = await queryPage(
        query.drugWelfareId,
        query.sellerUserId,
        query.medicineUserName,
        query.medicineUserPhone,
        query.groupId,
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
    async downloadGroup(){
      let query = this.query
      this.$common.showLoad()
      let data = await createDownLoad({
        className: 'hmcAdminDrugWelfareStatisticsExportService',
        fileName: '导出入组数据',
        groupName: '药品福利计划',
        menuName: '订单管理 - 药品福利计划',
        searchConditionList: [
          {
            desc: '用药福利计划名称',
            name: 'drugWelfareId',
            value: query.drugWelfareId || ''
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
            name: 'groupId',
            value: query.groupId || ''
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
        className: 'hmcAdminDrugWelfareGroupCouponStatisticsExportService',
        fileName: '导出福利券统计数据',
        groupName: '药品福利计划',
        menuName: '订单管理 - 药品福利计划',
        searchConditionList: [
          {
            desc: '用药福利计划名称',
            name: 'drugWelfareId',
            value: query.drugWelfareId || ''
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
            name: 'groupId',
            value: query.groupId || ''
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