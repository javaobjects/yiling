<template>
  <div class="app-container">
    <!-- 内容区 -->
    <div class="app-container-content">
      <div class="common-box">
        <div class="explain-view">企业从以岭购买的非以岭品，维护后对应数据出现在企业流向中（以岭卖以岭品不需要维护）</div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">商业名称</div>
              <el-input v-model="query.ename" @keyup.enter.native="searchEnter" placeholder="请输入商业名称" />
            </el-col>
            <el-col :span="6">
              <div class="title">商业ID</div>
              <el-input v-model="query.eid" @keyup.enter.native="searchEnter" placeholder="请输入商业ID" />
            </el-col>
            <el-col :span="8">
              <div class="title">操作时间</div>
              <el-date-picker v-model="query.time" type="daterange" format="yyyy 年 MM 月 dd 日" value-format="yyyy-MM-dd" range-separator="至" start-placeholder="开始月份" end-placeholder="结束月份">
              </el-date-picker>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">商品名称</div>
              <el-input v-model="query.goodsName" @keyup.enter.native="searchEnter" placeholder="请输入商品名称" />
            </el-col>
            <el-col :span="6">
              <div class="title">商品内码</div>
              <el-input v-model="query.goodsInSn" @keyup.enter.native="searchEnter" placeholder="请输入商品内码" />
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
        <el-row>
          <el-col :span="12">
            <ylButton type="primary" @click="addClick">添加</ylButton>
          </el-col>
          <el-col :span="12" style="text-align: right;">
            <!-- <ylButton type="primary" plain @click="crmDownLoad">crm商品未映射数据</ylButton> -->
            <!-- <ylButton type="primary" plain @click="importClick" >crm商品未映射数据导入</ylButton> -->
          </el-col>
        </el-row>
      </div>
      <!-- 底部列表 -->
      <div class="mar-t-8">
        <yl-table border :show-header="true" :list="dataList" :total="query.total" :page.sync="query.page" :limit.sync="query.limit" :loading="loading" @getList="getList">
          <el-table-column align="center" min-width="80" label="ID" prop="id">
          </el-table-column>
          <el-table-column align="center" min-width="120" label="商品名称" prop="goodsName">
          </el-table-column>
          <el-table-column align="center" label="商品规格">
            <template slot-scope="{ row }">
              <div>{{ row.specifications }}</div>
            </template>
          </el-table-column>
          <el-table-column align="center" label="商品内码">
            <template slot-scope="{ row }">
              <div>{{ row.goodsInSn }}</div>
            </template>
          </el-table-column>
          <el-table-column align="center" label="批准文号" prop="licenseNo">
          </el-table-column>
          <el-table-column align="center" label="生产厂家" prop="manufacturer">
          </el-table-column>
          <el-table-column align="center" label="商业名称" prop="ename">
          </el-table-column>
          <el-table-column align="center" label="商业ID" prop="eid">
          </el-table-column>
          <el-table-column align="center" min-width="120" label="操作时间">
            <template slot-scope="{ row }">
              <span>{{ row.opTime | formatDate }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" label="操作人" prop="operName">
          </el-table-column>
          <el-table-column align="center" label="操作" width="120">
            <template slot-scope="{ row }">
              <div>
                <yl-button type="text" @click="deleteClick(row)">删除</yl-button>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
    <!-- 添加 -->
    <yl-dialog title="添加" :visible.sync="returnShow" :show-footer="true" width="600px" @confirm="addConfirm">
      <div class="return-content pad-16">
        <el-form :model="form" :rules="rules" ref="dataForm" label-width="150px">
          <el-form-item label-width="0px">
            <div style="text-align:center;">企业以岭商品配置（非以岭生产，从以岭采购，需展示在流向中的品）</div>
          </el-form-item>
          <el-form-item label="商业名称：" prop="ename">
            <el-autocomplete v-model="form.ename" :fetch-suggestions="querySearchAsync" :trigger-on-focus="false" placeholder="请输入商业名称" @input="handle" @select="handleSelect"></el-autocomplete>
          </el-form-item>
          <el-form-item label="商业ID：" prop="eid">
            <el-input disabled :value="form.eid" placeholder="商业ID" />
          </el-form-item>
          <el-form-item label="商品规格：" prop="specifications">
            <el-input v-model="form.specifications" placeholder="请输入商品规格" />
          </el-form-item>
          <el-form-item label="商品名称：" prop="goodsName">
            <el-input v-model="form.goodsName" placeholder="请输入商品名称" />
          </el-form-item>
          <el-form-item label="商品内码：" prop="goodsInSn">
            <el-input v-model="form.goodsInSn" placeholder="请输入商品内码" />
          </el-form-item>
          <el-form-item label="批准文号：">
            <el-input v-model="form.licenseNo" placeholder="请输入批准文号" />
          </el-form-item>
          <el-form-item label="生产厂家：" prop="manufacturer">
            <el-input v-model="form.manufacturer" placeholder="请输入生产厂家" />
          </el-form-item>
        </el-form>
      </div>
    </yl-dialog>
    <!-- excel导入 -->
    <import-send-dialog :visible.sync="importSendVisible" :excel-code="info.excelCode" ref="importSendRef"></import-send-dialog>
  </div>
</template>

<script>
import {
  FlowGoodsConfigList,
  getEnterpriseId,
  flowGoodsConfigSave,
  flowGoodsConfigDelete
} from '@/subject/admin/api/zt_api/erpAdministration'
import { createDownLoad } from '@/subject/admin/api/common';
import ImportSendDialog from '@/subject/admin/components/ImportSendDialog'
export default {
  name: 'EnterpriseGoodsConfig',
  components: {
    ImportSendDialog
  },
  computed: {
  },
  data() {
    return {
      loading: false,
      query: {
        page: 1,
        limit: 10,
        total: 0,
        time: []
      },
      dataList: [],
      returnShow: false,
      form: {
        ename: '',
        eid: '',
        specifications: '',
        goodsName: '',
        goodsInSn: '',
        licenseNo: '',
        manufacturer: ''
      },
      rules: {
        ename: [{ required: true, trigger: 'blur', message: '请输入商业名称' }],
        eid: [{ required: true, trigger: 'blur', message: '请选择商业ID' }],
        specifications: [{ required: true, trigger: 'blur', message: '请输入商品规格' }],
        goodsName: [{ required: true, trigger: 'blur', message: '请输入商品名称' }],
        goodsInSn: [{ required: true, trigger: 'blur', message: '请输入商品内码' }],
        manufacturer: [{ required: true, trigger: 'blur', message: '请输入生产厂家' }]
      },
      importSendVisible: false,
      // 导入任务参数
      info: {
        excelCode: 'importCrmGoodsMapping'
      }
    };
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
      let data = await FlowGoodsConfigList(
        query.page,
        query.limit,
        query.ename,
        query.eid,
        query.goodsName,
        query.goodsInSn,
        query.time && query.time.length > 0 ? query.time[0] : null,
        query.time && query.time.length > 1 ? query.time[1] : null
      );
      this.loading = false
      if (data) {
        this.dataList = data.records
        this.query.total = data.total
      }
    },
    // 搜索点击
    handleSearch() {
      this.query.page = 1
      this.getList()
    },
    handleReset() {
      this.query = {
        page: 1,
        limit: 10,
        time: []
      }
    },
    addClick() {
      this.form = {
        ename: '',
        eid: '',
        specifications: '',
        goodsName: '',
        goodsInSn: '',
        licenseNo: '',
        manufacturer: ''
      }
      this.returnShow = true
    },
    async querySearchAsync(queryString, cb) {
      this.$common.showLoad()
      let data = await getEnterpriseId(queryString)
      this.$common.hideLoad()
      if (data) {
        data.forEach(item => {
          item.value = item.ename
          console.log(item)
        });
        cb(data)
      }
    },
    handle() {
      this.form.eid = ''
    },
    handleSelect(item) {
      this.form.eid = item.eid
    },
    addConfirm() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          this.$common.showLoad()
          let form = this.form
          let data = await flowGoodsConfigSave(
            form.ename,
            form.eid,
            form.specifications,
            form.goodsName,
            form.goodsInSn,
            form.licenseNo,
            form.manufacturer
          )
          this.$common.hideLoad()
          if (typeof data !== 'undefined') {
            this.$common.n_success('添加成功');
            this.returnShow = false
            this.getList()
          }
        } else {
          return false
        }
      })
    },
    // 删除
    deleteClick(row) {
      this.$confirm('删除确认', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(async () => {
          //确定
          this.$common.showLoad()
          let data = await flowGoodsConfigDelete(row.id)
          this.$common.hideLoad()
          if (typeof data != 'undefined') {
            this.$common.n_success('删除成功');
            this.getList()
          }
        })
        .catch(() => {
          //取消
        });
    },
    //导出
    async crmDownLoad() {
      this.$common.showLoad()
      let query = this.query;
      let data = await createDownLoad({
        className: 'unmappedCrmGoodsExportService',
        fileName: 'crm未映射商品',
        groupName: 'crm未映射商品',
        menuName: '“ERP对接管理 - ERP流向 - 企业流向商品配黑',
        searchConditionList: [
          {
            desc: '商业名称',
            name: 'ename',
            value: query.ename || ''
          },
          {
            desc: '商业ID',
            name: 'ename',
            value: query.eid || ''
          },
          {
            desc: '操作时间-开始',
            name: 'startCreateTime',
            value: query.time && query.time.length > 0 ? query.time[0] : ''
          },
          {
            desc: '操作时间-结束',
            name: 'endCreateTime',
            value: query.time && query.time.length > 1 ? query.time[1] : ''
          },
          {
            desc: '商品名称',
            name: 'goodsName',
            value: query.goodsName || ''
          },
          {
            desc: '商品内码',
            name: 'goodsInSn',
            value: query.goodsInSn || ''
          }
        ]
      })
      this.$common.hideLoad()
      if (data && data.result) {
        this.$common.n_success('创建下载任务成功，请在下载中心查看')
      }
    },
    //导入
    importClick() {
      this.importSendVisible = true
      this.$nextTick( () => {
        this.$refs.importSendRef.init()
      })
    }
  }
};
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
<style lang="scss">
.order-table-view {
  .table-row {
    margin: 0 30px;
    td {
      .el-table__expand-icon {
        visibility: hidden;
      }
    }
  }
}
</style>
