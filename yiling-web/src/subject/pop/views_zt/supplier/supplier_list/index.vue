<template>
  <div class="app-container">
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">供应商名称</div>
              <el-select
                filterable
                clearable
                remote
                :remote-method="querySearchAsync"
                v-model="query.eid"
                @clear="querySearchAsync"
                :no-data-text="noDataText"
                placeholder="请输入供应商名称">
                <el-option
                  v-for="item in resultsData"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                ></el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <div class="title">社会统一信用代码</div>
              <el-input v-model="query.licenseNumber" placeholder="请输入社会统一信用代码" @keyup.enter.native="searchEnter"/>
            </el-col>
            <el-col :span="6">
              <div class="title">ERP供应商名称</div>
               <el-input v-model="query.erpName" placeholder="请输入ERP供应商名称" @keyup.enter.native="searchEnter"/>
            </el-col>
            <el-col :span="6">
              <div class="title">采购员名称</div>
               <el-input v-model="query.buyerName" placeholder="请输入采购员名称" @keyup.enter.native="searchEnter"/>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn
                :total="query.total"
                @search="handleSearch"
                @reset="handleReset"
              />
            </el-col>
          </el-row>
        </div>
      </div>
      <!-- 新增按钮 -->
      <div class="down-box clearfix">
        <div class="btn">
          <ylButton type="primary" plain @click="downLoadTemp" v-role-btn="['1']">导出查询结果</ylButton>
        </div>
      </div>
      <!-- 底部列表 -->
      <div class="mar-t-8">
        <yl-table
          border
          :show-header="true"
          :list="dataList"
          :total="query.total"
          :page.sync="query.page"
          :limit.sync="query.limit"
          :loading="loading"
          @getList="getList">
          <el-table-column label="供应商名称" min-width="139" align="center" prop="ename"></el-table-column>
          <el-table-column label="社会统一信用代码" min-width="140" align="center" prop="licenseNumber"></el-table-column>
          <el-table-column label="ERP供应商名称" min-width="140" align="center" prop="erpName"></el-table-column>
          <el-table-column label="ERP编码" min-width="100" align="center" prop="erpCode"></el-table-column>
          <el-table-column label="ERP内码" min-width="100" align="center" prop="erpInnerCode"></el-table-column>
          <el-table-column label="采购员编码" min-width="100" align="center" prop="buyerCode"></el-table-column>
          <el-table-column label="采购员名称" min-width="100" align="center" prop="buyerName"></el-table-column>
          <el-table-column label="操作" min-width="110" fixed="right" align="center">
            <template slot-scope="{ row }">
              <yl-button type="text" v-role-btn="['2']" @click="editClick(row)">编辑</yl-button>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
    <!-- 弹窗 -->
    <yl-dialog title="编辑" @confirm="confirm" width="660px" :visible.sync="showDialog">
      <div class="dialogTc">
        <el-form 
          :model="form" 
          :rules="rules" 
          ref="dataForm" 
          label-width="140px" 
          class="demo-ruleForm">
          <el-form-item label="供应商名称：">
            {{ form.ename }}
          </el-form-item>
          <el-form-item label="社会统一信用代码：">
            {{ form.licenseNumber }}
          </el-form-item>
          <el-form-item label="ERP供应商名称：">
            <el-input
              type="textarea"
              maxlength="50" 
              show-word-limit
              :autosize="{ minRows: 3, maxRows: 3}"
              placeholder="请输入ERP供应商名称"
              v-model.trim="form.erpName">
            </el-input>
          </el-form-item>
          <el-form-item label="ERP编码：">
            <el-input v-model="form.erpCode" placeholder="请输入ERP编码" maxlength="30" show-word-limit/>
          </el-form-item>
          <el-form-item label="ERP内码：">
            <el-input v-model="form.erpInnerCode" placeholder="请输入ERP内码" maxlength="30" show-word-limit/>
          </el-form-item>
          <el-form-item label="采购员编码：">
            <el-input v-model="form.buyerCode" placeholder="请输入采购员编码" maxlength="30" show-word-limit/>
          </el-form-item>
          <el-form-item label="采购员名称：">
            <el-input v-model="form.buyerName" placeholder="请输入采购员名称" maxlength="30" show-word-limit/>
          </el-form-item>
        </el-form>
      </div>
    </yl-dialog>
  </div>
</template>

<script>
import { chooseSupplierList, pageList, update } from '@/subject/pop/api/zt_api/supplier';
import { createDownLoad } from '@/subject/pop/api/common'
export default {
  name: 'SupplierList',
  components: {
  },
  computed: {
  },
  data() {
    return {
      // 头部导航
      navList: [
        {
          title: '首页',
          path: '/zt_dashboard'
        },
        {
          title: '供应商管理',
          path: ''
        },
        {
          title: '供应商列表'
        }
      ],
      loading: false,
      query: {
        eid: '',
        licenseNumber: '',
        erpName: '',
        buyerName: '',
        total: 0,
        page: 1,
        limit: 10
      },
      noDataText: '无数据',
      resultsData: [],
      dataList: [],
      //弹窗
      showDialog: false,
      form: {
        id: '',
        ename: '',
        licenseNumber: '',
        erpName: '',
        erpCode: '',
        erpInnerCode: '',
        buyerCode: '',
        buyerName: ''
      },
      rules: {}
    }
  },
  activated() {
    this.getList()
  },
  methods: {
    // Enter
    searchEnter(e) {
      const keyCode = window.event ? e.keyCode : e.which;
      if (keyCode === 13) {
        this.getList()
      }
    },
    async getList() {
      this.loading = true
      let query = this.query
      let data = await pageList(
        query.eid,
        query.licenseNumber,
        query.erpName,
        query.buyerName,
        query.page,
        query.limit
      )
      if (data) {
        this.dataList = data.records
        this.query.total = data.total
      }
      this.loading = false
    },
    async querySearchAsync(value) {
      this.resultsData = [];
      if (value != '') {
        this.noDataText = '加载中'
        this.$common.showLoad();
        let data = await chooseSupplierList(
          value,
          1,
          60
        )
        this.$common.hideLoad();
        if (data !== undefined) {
          if (data.records && data.records.length > 0) {
            this.resultsData = data.records
          } else {
            this.resultsData = []
            this.noDataText = '暂无数据'
          }
        }
      }
    },
    //点击编辑
    editClick(row) {
      this.form = { ...row }
      this.showDialog = true
    },
    //点击弹窗保存
    async confirm() {
      let form = this.form;
      this.$common.showLoad();
      let data = await update(
        form.id,
        form.erpName,
        form.erpCode,
        form.erpInnerCode,
        form.buyerCode,
        form.buyerName
      )
      this.$common.hideLoad();
      if (data !== undefined) {
        this.showDialog = false
        this.getList()
      }

    },
    //导出
    async downLoadTemp() {
      let query = this.query
      this.$common.showLoad()
      let data = await createDownLoad({
        className: 'enterpriseSupplierExportService',
        fileName: '供应商管理',
        groupName: '供应商列表',
        menuName: '供应商管理-供应商列表',
        searchConditionList: [
          {
            desc: '供应商名称',
            name: 'eid',
            value: query.eid || ''
          },
          {
            desc: '社会统一信用代码',
            name: 'licenseNumber',
            value: query.licenseNumber || ''
          },
          {
            desc: 'ERP供应商名称',
            name: 'erpName',
            value: query.erpName || ''
          },
          {
            desc: '采购员名称',
            name: 'buyerName',
            value: query.buyerName || ''
          }
        ]
      })
      this.$common.hideLoad()
      if (data !== undefined) {
        this.$common.n_success('创建下载任务成功，请在下载中心查看')
      }
    },
    // 搜索点击
    handleSearch() {
      this.query.page = 1
      this.getList()
    },
    handleReset() {
      this.query = {
        eid: '',
        licenseNumber: '',
        erpName: '',
        buyerName: '',
        total: 0,
        page: 1,
        limit: 10
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
