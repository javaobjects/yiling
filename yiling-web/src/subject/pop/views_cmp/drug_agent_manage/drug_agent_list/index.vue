<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <!-- 内容区 -->
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">员工姓名</div>
              <el-input v-model="query.name" placeholder="请输入员工姓名" @keyup.enter.native="handleSearch"/>
            </el-col>
            <el-col :span="8">
              <div class="title">员工手机号</div>
              <el-input v-model="query.mobile" placeholder="请输入员工手机号" @keyup.enter.native="handleSearch"/>
            </el-col>
            <el-col :span="8">
              <div class="title">员工工号</div>
              <el-input v-model="query.code" placeholder="请输入员工工号" @keyup.enter.native="handleSearch"/>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn :total="query.total" @search="handleSearch" @reset="handleReset" />
            </el-col>
          </el-row>
        </div>
      </div>
      <!-- 导出按钮 -->
      <div class="down-box clearfix">
        <div class="btn">
          <ylButton type="primary" plain v-role-btn="['6']" @click="jumpAddUser">添加医药代表</ylButton>
          <ylButton type="primary" plain v-role-btn="['1']" @click="downLoadTemp">导出药代数据</ylButton>
          <ylButton type="primary" plain v-role-btn="['2']" @click="downLoadTemp1">导出可售商品数据</ylButton>
          <ylButton type="primary" plain v-role-btn="['3']" @click="importClick">导入药代数据</ylButton>
        </div>
      </div>
      <!-- 底部列表 -->
      <div class="mar-t-8 pad-b-10 order-table-view">
        <yl-table
          :list="dataList"
          :total="query.total"
          show-header
          :page.sync="query.current"
          :limit.sync="query.size"
          :loading="loading"
          @getList="getList">
          <el-table-column label="序号" width="80" align="center" type="index">
            <template slot-scope="scope">
              <span>{{ (query.current - 1) * query.size + scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column label="姓名" min-width="120" align="center" prop="name"></el-table-column>
          <el-table-column label="手机号" min-width="120" align="center" prop="mobile"></el-table-column>
          <el-table-column label="工号" min-width="120" align="center" prop="code"></el-table-column>
          <el-table-column label="状态" min-width="120" align="center" prop="status">
            <template slot-scope="{ row }">
              <span :class="row.status === 1 ? 'col-down' : 'col-up'">{{ row.status === 1 ? "启用" : "停用" }}</span>
            </template>
          </el-table-column>
          <el-table-column label="可售药品是否设置" min-width="120" align="center" prop="hasSalesGoodsSettingFlag">
            <template slot-scope="{ row }">
              <span>{{ row.hasSalesGoodsSettingFlag === true ? "已设置" : "未设置" }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" min-width="120" align="center">
            <template slot-scope="{ row }">
              <yl-button type="text" v-role-btn="['4']" @click="setting(row)">可售药品</yl-button>
              <yl-button type="text" v-role-btn="['5']" @click="detail(row)">查看</yl-button>
            </template>
          </el-table-column>
        </yl-table>
      </div>
      <!-- 添加商品 -->
      <yl-dialog width="60%" title="添加" :visible.sync="showDialog" :show-footer="false" @close="closeDialog">
        <div class="choose-pro-view">
          <div class="common-box">
            <div class="search-box">
              <el-row>
                <el-col :span="8">
                  <div class="title">商品名称</div>
                  <el-input v-model="drugQuery.name" placeholder="请输入商品名称" @keyup.enter.native="handleDrugSearch"/>
                </el-col>
              </el-row>
            </div>
            <div class="search-box mar-t-16">
              <yl-search-btn :total="drugQuery.total" @search="handleDrugSearch" @reset="handleDrugReset" />
            </div>
          </div>
          <div class="common-box">
            <yl-table
              :show-header="true"
              :list="drugDataList"
              :total="drugQuery.total"
              :page.sync="drugQuery.current"
              :limit.sync="drugQuery.size"
              :loading="loading1"
              :stripe="true"
              @getList="getDrugList">
              <el-table-column align="center" label="商品ID" prop="id"></el-table-column>
              <el-table-column align="center" label="商品名称" prop="name"></el-table-column>
              <el-table-column align="center" label=" 销售规格" prop="sellSpecifications">
                <template slot-scope="{ row }">
                  <span>{{ row.sellSpecifications ? row.sellSpecifications : "- -" }}</span>
                </template>
              </el-table-column>
              <el-table-column align="center" label="商品状态" prop="goodsStatus">
                <template slot-scope="{ row }">
                  <span>{{ row.goodsStatus | dictLabel(goodsStatus) }}</span>
                </template>
              </el-table-column>
              <el-table-column align="center" label="添加状态" prop="addedFlag">
                <template slot-scope="{ row }">
                  <span :class="row.addedFlag === true ? 'col-down' : 'col-up'">{{ row.addedFlag == true ? "已添加" : "未添加" }}</span>
                </template>
              </el-table-column>
              <el-table-column align="center" label="操作" width="100">
                <template slot-scope="{ row }">
                  <yl-button type="text" v-if="row.addedFlag === true" @click="operation(row, 2)">删除</yl-button>
                  <yl-button type="text" v-if="row.addedFlag === false" @click="operation(row, 1)">添加</yl-button>
                </template>
              </el-table-column>
            </yl-table>
          </div>
        </div>
      </yl-dialog>
      <!-- 导入 -->
      <import-send-dialog :visible.sync="importSendVisible" :excel-code="info.excelCode" ref="importSendRef"></import-send-dialog>
    </div>
  </div>
</template>

<script>
import { getDrugAgentList, salesGoodsPageList, addOrRemoveSalesGoods } from '@/subject/pop/api/cmp_api/drugAgent';
import { createDownLoad } from '@/subject/pop/api/common'
import { goodsStatus } from '@/subject/pop/utils/busi'
import { handleSelect } from '@/subject/pop/utils/index'
import ImportSendDialog from '@/subject/pop/components/ImportSendDialog'

export default {
  name: 'DrugAgentList',
  components: {
    ImportSendDialog
  },
  computed: {
    // 商品状态
    goodsStatus() {
      return goodsStatus()
    }
  },
  data() {
    return {
      // 头部导航
      navList: [
        {
          title: '首页',
          path: '/sale_dashboard'
        },
        {
          title: '药代管理'
        },
        {
          title: '药代信息'
        }
      ],
      loading: false,
      query: {
        current: 1,
        size: 10,
        total: 0,
        name: '',
        mobile: '',
        code: ''
      },
      dataList: [],
      showDialog: false,
      loading1: false,
      drugQuery: {
        current: 1,
        size: 10,
        name: '',
        employeeId: '',
        total: 0
      },
      // 可售商品
      drugDataList: [],
      // 添加删除操作-暂存员工id
      employeeId: '',
      // 导入展示
      importSendVisible: false,
      // 导入Code
      info: {
        excelCode: 'importMrSalesGoodsData'
      }
    };
  },
  activated() {
    this.getList()
  },
  methods: {
    async getList() {
      this.loading = true
      let query = this.query
      let data = await getDrugAgentList(
        query.current,
        query.size,
        query.name,
        query.mobile,
        query.code
      );
      this.loading = false
      if (data) {
        this.dataList = data.records
        this.query.total = data.total
      }
    },
    // 搜索点击
    handleSearch() {
      this.query.current = 1
      this.getList()
    },
    handleReset() {
      this.query = {
        current: 1,
        size: 10,
        name: '',
        mobile: '',
        code: ''
      }
    },
    // 设置可售药品
    setting(row) {
      this.drugQuery.employeeId = row.id
      this.employeeId = row.id
      this.drugQuery.name = ''
      this.drugQuery.total = 0
      this.showDialog = true
      this.getDrugList()
    },
    // 获取可售商品
    async getDrugList() {
      this.loading1 = true
      let drugQuery = this.drugQuery
      let data = await salesGoodsPageList(
        drugQuery.current,
        drugQuery.size,
        drugQuery.name,
        drugQuery.employeeId
      )
      this.loading1 = false
      if (data && data.records) {
        this.drugDataList = data.records
        this.drugQuery.total = data.total
      }
    },
    handleDrugSearch() {
      this.drugQuery.current = 1
      this.getDrugList()
    },
    handleDrugReset() {
      this.drugQuery.current = 1
      this.drugQuery.size = 10
      this.drugQuery.name = ''
    },
    // 添加/删除可售商品
    async operation(row, opType) {
      this.$common.showLoad();
      let data = await addOrRemoveSalesGoods(this.employeeId, [row.id], opType)
      this.$common.hideLoad();
      if (data !== undefined) {
        this.getDrugList();
        this.$common.success('操作成功')
      }
    },
    detail(row) {
      this.$router.push({
        name: 'DrugAgentDetail',
        params: { id: row.id}
      })
    },
    // 导出药代数据
    async downLoadTemp() {
      let query = this.query
      this.$common.showLoad()
      let data = await createDownLoad({
        className: 'mrPageListDataExportService',
        fileName: '药代数据导出',
        groupName: '药代信息列表',
        menuName: '药代管理-药代信息列表',
        searchConditionList: [
          {
            desc: '员工姓名',
            name: 'name',
            value: query.name || ''
          },
          {
            desc: '员工手机号',
            name: 'mobile',
            value: query.mobile || ''
          },
          {
            desc: '员工工号',
            name: 'code',
            value: query.code || ''
          }
        ]
      })
      this.$common.hideLoad()
      if (data && data.result) {
        this.$common.n_success('创建下载任务成功，请在下载中心查看')
      }
    },
    // 导出可售商品数据
    async downLoadTemp1() {
      let query = this.query
      this.$common.showLoad()
      let data = await createDownLoad({
        className: 'mrSalesGoodsPageListDataExportService',
        fileName: '可售商品数据导出',
        groupName: '可售商品列表',
        menuName: '药代信息-可售商品列表',
        searchConditionList: [
          {
            desc: '员工姓名',
            name: 'name',
            value: query.name || ''
          },
          {
            desc: '员工手机号',
            name: 'mobile',
            value: query.mobile || ''
          },
          {
            desc: '员工工号',
            name: 'code',
            value: query.code || ''
          }
        ]
      })
      this.$common.hideLoad()
      if (data && data.result) {
        this.$common.n_success('创建下载任务成功，请在下载中心查看')
      }
    },
    // 导入
    importClick() {
      this.importSendVisible = true
      this.$nextTick( () => {
        this.$refs.importSendRef.init()
      })
    },
    closeDialog() {
      this.employeeId = ''
      this.getList()
    },
    // 跳转用户管理
    jumpAddUser() {
      handleSelect(5, '/zt_roles/roles_user');
    }
  }
};
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
