<template>
  <div class="app-container">
    <!-- 内容区 -->
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">企业名称</div>
              <el-input v-model.trim="query.clientName" @keyup.enter.native="searchEnter" placeholder="请输入企业名称" />
            </el-col>
            <el-col :span="6">
              <div class="title">对接负责人</div>
              <el-input v-model.trim="query.installEmployee" @keyup.enter.native="searchEnter" placeholder="请输入对接负责人" />
            </el-col>
            <el-col :span="6">
              <div class="title">对接级别</div>
              <el-select class="select-width" v-model="query.depth" placeholder="请选择">
                <el-option key="-1" label="全部" value="-1"></el-option>
                <el-option key="0" label="未对接" value="0"></el-option>
                <el-option key="1" label="基础对接" value="1"></el-option>
                <el-option key="2" label="订单提取" value="2"></el-option>
                <el-option key="3" label="发货单对接" value="3"></el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <div class="title">流向级别</div>
              <el-select class="select-width" v-model="query.flowLevel" placeholder="请选择">
                <el-option key="-1" label="全部" value="-1"></el-option>
                <el-option key="0" label="未对接" value="0"></el-option>
                <el-option key="1" label="以岭流向" value="1"></el-option>
                <el-option key="2" label="全品流向" value="2"></el-option>
              </el-select>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">crmID</div>
              <el-input v-model.trim="query.crmEnterpriseId" @keyup.enter.native="searchEnter" placeholder="请输入crmID" />
            </el-col>
            <el-col :span="6">
              <div class="title">同步状态</div>
              <el-select class="select-width" v-model="query.syncStatus" placeholder="请选择">
                <el-option key="-1" label="全部" value="-1"></el-option>
                <el-option key="0" label="未开启" value="0"></el-option>
                <el-option key="1" label="已开启" value="1"></el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <div class="title">监控状态</div>
              <el-select class="select-width" v-model="query.monitorStatus" placeholder="请选择">
                <el-option key="-1" label="全部" value="-1"></el-option>
                <el-option key="0" label="禁用" value="0"></el-option>
                <el-option key="1" label="启用" value="1"></el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <div class="title">终端激活状态</div>
              <el-select class="select-width" v-model="query.clientStatus" placeholder="请选择">
                <el-option key="-1" label="全部" value="-1"></el-option>
                <el-option
                  v-for="item in clientStatus"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
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
      <div class="down-box clearfix">
        <div>
          <ylButton type="primary" @click="addClick">新增对接客户</ylButton>
        </div>
      </div>
      <!-- 下部 表格 -->
      <div class="search-bar">
        <yl-table
          border 
          :show-header="true" 
          :list="dataList" 
          :total="query.total" 
          :page.sync="query.page" 
          :limit.sync="query.limit" 
          :loading="loading" 
          @getList="getList">
          <el-table-column align="center" label="企业ID" width="100" prop="rkSuId"></el-table-column>
          <el-table-column align="center" min-width="200" label="企业名称" prop="clientName">
          </el-table-column>
          <el-table-column align="center" label="crmID" width="100" prop="crmEnterpriseId"></el-table-column>
          <el-table-column align="center" min-width="200" label="APPKEY" prop="clientKey">
          </el-table-column>
          <el-table-column align="center" min-width="200" label="密钥" prop="clientSecret">
          </el-table-column>
          <el-table-column align="center" min-width="100" label="对接级别">
            <template slot-scope="{ row }">
              <div>
                {{ row.depth == 0 ? '未对接' : (row.depth == 1 ? '基础对接' : (row.depth == 2 ? '订单提取' :(row.depth == 3 ? '发货单对接' : '- -'))) }}
              </div>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="100" label="流向级别">
            <template slot-scope="{ row }">
              <div>
                {{ row.flowLevel == 0 ? '未对接' : (row.flowLevel == 1 ? '以岭流向' : (row.flowLevel == 2 ? '全品流向': '- -')) }}
              </div>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="120" label="负责人" prop="installEmployee">
          </el-table-column>
          <el-table-column align="center" min-width="150" label="对接时间">
            <template slot-scope="{ row }">
              <div>
                {{ row.depthTime | formatDate }}
              </div>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="100" label="同步状态">
            <template slot-scope="{ row }">
              <div>
                {{ row.syncStatus == 0 ? '未开启' : '已开启' }}
              </div>
            </template>
          </el-table-column>
          <el-table-column fixed="right" align="center" label="是否开启监控" min-width="80">
            <template slot-scope="{ row }">
              <div class="table-switch">
                <el-switch 
                  disabled 
                  active-color="#13ce66" 
                  inactive-color="#ff4949"
                  :active-value="1" 
                  :inactive-value="0"
                  v-model="row.monitorStatus"
                  @click.native="monitorStatusChange(row)">
                </el-switch>
              </div>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="100" label="终端激活状态">
            <template slot-scope="{ row }">
              <div>
                {{ row.clientStatus | dictLabel(clientStatus) }}
              </div>
            </template>
          </el-table-column>
          <el-table-column fixed="right" align="center" label="操作" min-width="140">
            <template slot-scope="{ row }">
              <div>
                <yl-button type="text" @click="exportClick(row)">导出</yl-button>
              </div>
              <div>
                <yl-button type="text" @click="detailClick(row)">编辑</yl-button>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
    <!-- 弹窗 -->
    <yl-dialog 
      title="导出选择" 
      @confirm="confirm" 
      width="570px" 
      :visible.sync="showDialog">
      <div class="dialogTc">
        <el-form 
          :model="form" 
          :rules="rules" 
          ref="dataForm" 
          label-width="80px" 
          class="demo-ruleForm">
          <el-form-item label="选择公司" prop="parentFlag">
            <el-radio-group v-model="form.parentFlag">
              <el-radio :label="1">当前公司</el-radio>
              <el-radio :label="2">总公司</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="选择月份" prop="queryDate">
            <el-date-picker 
              v-model="form.queryDate" 
              format="yyyy 年 MM 月"
              value-format="yyyy-MM"
              type="month"
              placeholder="请选择年月"
              :picker-options="pickerData">
            </el-date-picker>
          </el-form-item>
        </el-form>
      </div>
    </yl-dialog>
  </div>
</template>

<script>
import { queryListPage, updateMonitorStatus } from '@/subject/admin/api/zt_api/erpAdministration'
import { createDownLoad } from '@/subject/admin/api/common'
import { erpClientStatus } from '@/subject/admin/busi/erp/flowDirection'
export default {
  name: 'Enterprise',
  computed: {
    //终端激活状态
    clientStatus() {
      return erpClientStatus()
    }
  },
  data() {
    return {
      query: {
        clientName: '',
        installEmployee: '',
        depth: '-1',
        flowLevel: '-1',
        crmEnterpriseId: '',
        syncStatus: '-1',
        total: 0,
        page: 1,
        limit: 10,
        monitorStatus: '-1',
        clientStatus: '-1'
      },
      loading: false,
      dataList: [],
      //导出选择弹窗
      showDialog: false,
      form: {
        eid: '',
        parentFlag: 1,
        queryDate: ''
      },
      rules: {
        parentFlag: [{ required: true, message: '请选择当前公司', trigger: 'blur' }],
        queryDate: [{ required: true, message: '请选择月份', trigger: 'blur' }]
      },
      pickerData: {
        disabledDate: (time) => {
          //当前日期（具体到秒）
          const date = new Date()
          //当前年份
          const year = date.getFullYear() 
          //当前月份
          let month = date.getMonth() + 1
          // 1-9月份前加0
          if (month >= 1 && month <= 9) {
            month = '0' + month
          }
          const currentdate = year.toString() + '-' + month.toString()
          const timeyear = time.getFullYear()
          let timemonth = time.getMonth() + 1
          if (timemonth >= 1 && timemonth <= 9) {
              timemonth = '0' + timemonth
            }
          const timedate = timeyear.toString() + timemonth.toString()
          // /**当月 */
          let reduce = this.calcMonths(currentdate, - 5)
          let plus = year.toString() + month.toString()
          return (timedate < reduce || plus < timedate) || time.getFullYear() < '2020' || time.getFullYear() > new Date().getFullYear()
        }
      }
    }
  },
  activated() {
    this.getList();
  },
  methods: {
    // 年月加减
    calcMonths(originalYtd, monthNum) {
      let arr = originalYtd.split('-');
      let year = parseInt(arr[0]);
      let month = parseInt(arr[1]);
      month = month + monthNum;
      if (month > 12) {
        let yearNum = parseInt((month - 1) / 12);
        month = month % 12 == 0 ? 12 : month % 12;
        year += yearNum;
      } else if (month <= 0) {
        month = Math.abs(month);
        let yearNum = parseInt((month + 12) / 12);
        let n = month % 12;
        if (n == 0) {
          year -= yearNum;
          month = 12
        } else {
          year -= yearNum;
          month = Math.abs(12 - n)
        }
 
      }
      month = month < 10 ? '0' + month : month;
      return year + '' + month;
    },
    // Enter
    searchEnter(e) {
      const keyCode = window.event ? e.keyCode : e.which;
      if (keyCode === 13) {
        this.getList()
      }
    },
    // 是否开启监控
    async monitorStatusChange(row) {
      this.$confirm(
        `确认 ${row.monitorStatus == '0' ? '开启' : '停用'} ${row.clientName} 的监控！`, '提示', 
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
      )
      .then( async() => {
        //确定
        this.$common.showLoad();
        let data = await updateMonitorStatus(
          row.monitorStatus == 0 ? 1 : 0,
          row.rkSuId
        )
        this.$common.hideLoad();
        if (data !== undefined) {
          this.$common.n_success(`${row.monitorStatus == 0 ? '开启' : '停用'} 成功!`);
          this.getList();
        }
      })
      .catch(() => {
        //取消
      });
    },
    async getList() {
      this.loading = true;
      let query = this.query;
      let data = await queryListPage(
        query.clientName,
        query.page,
        query.depth,
        query.flowLevel,
        query.installEmployee,
        query.limit,
        query.syncStatus,
        query.monitorStatus,
        query.clientStatus,
        query.crmEnterpriseId
      )
      if (data) {
        this.dataList = data.records;
        this.query.total = data.total;
      }
      this.loading = false;
    },
    // 新增对接客户
    addClick() {
      this.$router.push({
        name: 'EnterpriseAdded'
      });
    },
    // 搜索点击
    handleSearch() {
      this.query.page = 1;
      this.getList();
    },
    // 点击编辑
    detailClick(row) {
      this.$router.push({
        name: 'EnterpriseDetail',
        params: {
          id: row.rkSuId
        }
      });
    },
    // 清空查询
    handleReset() {
      this.query = {
        clientName: '',
        installEmployee: '',
        depth: '-1',
        flowLevel: '-1',
        crmEnterpriseId: '',
        syncStatus: '-1',
        total: 0,
        page: 1,
        limit: 10,
        monitorStatus: '-1',
        clientStatus: '-1'
      }
    },
    // 导出
    exportClick(row) {
      this.showDialog = true;
      this.form.eid = row.rkSuId;
    },
    //导出弹窗 确定
    confirm() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          let form = this.form;
          this.$common.showLoad()
          let data = await createDownLoad(
            'flowMonthBiTaskExportService',
            '流向数据',
            '流向数据导出',
            'ERP对接管理 - 对接企业管理',
            [
              {
                desc: '企业ID',
                name: 'eid',
                value: form.eid || ''
              },
              {
                desc: '当前公司',
                name: 'parentFlag',
                value: form.parentFlag || ''
              },
              {
                desc: '选择月份',
                name: 'queryDate',
                value: form.queryDate || ''
              }
            ]
          )
          this.$common.hideLoad();
          if (data && data.result) {
            this.$common.n_success('创建下载任务成功，请在下载中心查看')
            this.form = {
              eid: '',
              parentFlag: 1,
              queryDate: ''
            }
          }
          this.showDialog = false;
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>