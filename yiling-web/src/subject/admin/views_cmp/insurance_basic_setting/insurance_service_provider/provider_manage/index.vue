<template>
  <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <div class="common-box box-search">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">商家名称</div>
              <el-input v-model="query.companyName" placeholder="请输入商家名称" @keyup.enter.native="handleSearch"/>
            </el-col>
          </el-row>
        </div>
        <div class="search-btn-box">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn :total="total" @search="handleSearch" @reset="handleReset" />
            </el-col>
          </el-row>
        </div>
      </div>
      <div class="down-box clearfix">
        <div class="btn">
          <yl-button type="primary" plain @click="add">添加</yl-button>
        </div>
      </div>
      <div class="table-box mar-t-8">
        <yl-table
          stripe
          :show-header="true"
          :list="dataList"
          :total="total"
          :page.sync="query.current"
          :limit.sync="query.size"
          :loading="loading"
          @getList="getList"
        >
          <el-table-column label="ID" min-width="100" align="center" prop="id"></el-table-column>
          <el-table-column label="名称" min-width="100" align="center" prop="companyName"></el-table-column>
          <el-table-column label="创建时间" min-width="100" align="center">
            <template slot-scope="{ row }">
              <span>{{ row.createTime | formatDate }}</span>
            </template>
          </el-table-column>
          <el-table-column label="备注" min-width="200" align="left" prop="remark"></el-table-column>
          <el-table-column label="状态" min-width="100" align="center" prop="status">
            <template slot-scope="{ row }">
              <span v-if="row.status === 1" :class="['provider-status', row.status === 1 ? 'col-down' : '' ]">{{ row.status | providerStatus }}</span>
              <span v-else :class="['provider-status', row.status === 2 ? 'col-up' : '' ]">{{ row.status | providerStatus }}</span>
              <el-switch :value="row.status === 1" class="switch" @change="e => switchChange(row)"> </el-switch>
            </template>
          </el-table-column>
          <el-table-column label="操作" min-width="120" align="center">
            <template slot-scope="{ row }">
                <yl-button type="text" @click="edit(row)">编辑</yl-button>
                <yl-button type="text" @click="detail(row)">服务内容</yl-button>
                <yl-button type="text" @click="del(row)">删除</yl-button>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </div>
</template>

<script>
import { getInsuranceCompanyList, modifyStatus, deleteInsuranceCompany } from '@/subject/admin/api/cmp_api/insurance_basic_setting'
export default {
  name: 'ServiceProviderManage',
  components: {
  },
  computed: {
  },
  filters: {
    providerStatus(e) {
      let res
      switch (e) {
        case 1:
          res = '启用'
          break;
        case 2:
          res = '停用'
          break;
        default:
          res = '- -'
          break;
      }
      return res
    }
  },
activated() {
    this.getList()
  },
  data() {
    return {
      query: {
        current: 1,
        size: 10,
        companyName: ''
      },
      total: 0,
      // 列表
      dataList: [],
      loading: false
    }
  },
  methods: {
    async getList() {
      this.loading = true
      let query = this.query
      let data = await getInsuranceCompanyList(
        query.current,
        query.size,
        query.companyName
      )
      this.loading = false
      if (data) {
        this.dataList = data.records
        this.total = data.total
      }
    },
    handleSearch() {
      this.query.current = 1
      this.getList()
    },
    handleReset() {
      this.query = {
        current: 1,
        size: 10,
        companyName: ''
      }
    },
    // 状态修改
    async switchChange(row) {
      if (row.status !== 2) {
        this.$confirm('确定停用该服务商吗？', '停用', {
          confirmButtonText: '停用',
          cancelButtonText: '取消',
          type: 'warning',
          customClass: 'stop-service-provider-cfm'
        }).then(async () => {
          // 停用
          row.status = 2
          this.$common.showLoad()
          let data = await modifyStatus(row.id, row.status)
          this.$common.hideLoad()
          if (data !== undefined) {
            this.$common.n_success('操作成功')
            this.getList()
          } else {
            this.getList()
          }
        }).catch(() => {})
      } else {
        // 开启
        row.status = 1
        this.$common.showLoad()
        let data = await modifyStatus(row.id, row.status)
        this.$common.hideLoad()
        if (data !== undefined) {
          this.$common.n_success('操作成功')
          this.getList()
        }
      }

    },
    // 添加
    add() {
      this.$router.push({
        name: 'AddServiceProvider'
      })
    },
    // 修改服务商
    edit(row) {
      this.$router.push({
        name: 'EditServiceProvider',
        params: {
          id: row.id
        }
      })
    },
    // 服务内容
    detail(row) {
      this.$router.push({
        name: 'ServiceProviderContent',
        params: {
          id: row.id,
          companyName: row.companyName
        }
      })
    },
    // 删除
    del(row) {
      this.$confirm('确定删除该保险服务商吗？', '温馨提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning',
        customClass: 'del-service-provider-cfm'
      }).then(async () => {
        // 确认删除
        this.$common.showLoad()
        let data = await deleteInsuranceCompany(row.id)
        this.$common.hideLoad()
        if (data !== undefined) {
          this.$common.success('操作成功')
          this.getList()
        }
      }).catch(async () => {
        // 取消
      })
    }
  }
}
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
<style lang="scss">
.del-service-provider-cfm, .stop-service-provider-cfm {
  .el-message-box__header {
    padding-top: 12px;
    padding-bottom: 12px;
    border-bottom: 1px solid #f0f0f0;
    .el-message-box__title {
      font-size: 16px;
      font-weight: 500;
      line-height: 24px;
      text-align: center;
    }
  }
  .el-message-box__container {
    display: flex;
    justify-content: center;
    align-items: center;
    padding: 31px 0;
    .el-message-box__status {
      position: relative;
      color: #fa8c15;
      font-size: 16px !important;
      transform: translateY(0);
    }
    .el-message-box__message {
      padding-left: 3px;
    }
  }
  .el-message-box__btns {
    border-top: 1px solid #f0f0f0;
  }
}
</style>
