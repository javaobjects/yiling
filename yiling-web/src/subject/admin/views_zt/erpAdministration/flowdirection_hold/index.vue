<template>
  <div class="app-container">
    <!-- 内容区 -->
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">商业ID</div>
              <el-input v-model="query.eid" @keyup.enter.native="searchEnter" placeholder="请输入采购商ID" />
            </el-col>
            <el-col :span="6">
              <div class="title">商业名称</div>
              <el-input v-model="query.ename" @keyup.enter.native="searchEnter" placeholder="请输入商业名称" />
            </el-col>
            <el-col :span="6">
              <div class="title">流向类型</div>
              <el-select v-model="query.type" placeholder="请选择">
                <el-option label="全部" :value="0"></el-option>
                <el-option
                  v-for="item in erpFlowType"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">封存状态</div>
              <el-select v-model="query.status" placeholder="请选择">
                <el-option label="全部" :value="0"></el-option>
                <el-option
                  v-for="item in erpFlowSealedStatus"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
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
        <div>
          <ylButton type="primary" @click="addHoldClick">添加封存</ylButton>
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
          <el-table-column align="center" min-width="80" label="ID" prop="id">
          </el-table-column>
          <el-table-column align="center" label="商业ID" prop="eid">
          </el-table-column>
          <el-table-column align="center" min-width="120" label="商业名称" prop="ename">
          </el-table-column>
          <el-table-column align="center" width="120" label="流向类型">
            <template slot-scope="{ row }">
              <div>
                {{ row.type | dictLabel(erpFlowType) }}
              </div>
            </template>
          </el-table-column>
          <el-table-column align="center" width="120" label="封存状态">
            <template slot-scope="{ row }">
              <div>
                {{ row.status | dictLabel(erpFlowSealedStatus) }}
              </div>
            </template>
          </el-table-column>
          <el-table-column align="center" label="封存月份">
            <template slot-scope="{ row }">
              <div>{{ row.month }}</div>
            </template>
          </el-table-column>
          <el-table-column align="center" label="操作人">
            <template slot-scope="{ row }">
              <div>{{ row.operName }}</div>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="120" label="操作时间">
            <template slot-scope="{ row }">
              <span>{{ row.opTime | formatDate }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" label="操作" width="120">
            <template slot-scope="{ row }">
              <div>
                <yl-button v-if="row.status == 1" type="text" @click="statusClick(row, 1)">封存</yl-button>
                <yl-button v-if="row.status == 2" type="text" @click="statusClick(row, 2)">解封</yl-button>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </div>
</template>

<script>
import {
  flowSealedQueryListPage,
  erpFlowSealedLock,
  erpFlowSealedUnLock
} from '@/subject/admin/api/zt_api/erpAdministration'
import { erpFlowType, erpFlowSealedStatus } from '@/subject/admin/utils/busi'

export default {
  name: 'FlowdirectionHold',
  components: {
  },
  computed: {
    // 流向类型
    erpFlowType() {
      return erpFlowType()
    },
    // 封存状态
    erpFlowSealedStatus() {
      return erpFlowSealedStatus()
    }
  },
  data() {
    return {
      loading: false,
      query: {
        page: 1,
        limit: 10,
        total: 0,
        type: 0,
        status: 0
      },
      dataList: []
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
      let data = await flowSealedQueryListPage(
        query.page,
        query.limit,
        query.eid,
        query.ename,
        query.type,
        query.status
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
        type: 0,
        status: 0
      }
    },
    addHoldClick() {
      this.$router.push({
        name: 'FlowdirectionHoldAdd'
      });
    },
    // 删除
    statusClick(row, type) {
      // type 1-封存 2-解封
      this.$confirm(`企业ID${row.eid}对应${row.month}${row.type == 1 ? '采购' : '销售'}流向${type == 1 ? '封存' : '解封'}后,将${type == 1 ? '不' : ''}允许erp对接的数据进行更新处理,请确认是否${type == 1 ? '封存' : '解封'}`, `数据${type == 1 ? '封存' : '解封'}确认`, {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      .then(async () => {
        //确定
        this.$common.showLoad()
        let data = null
        if (type == 1) {
          data = await erpFlowSealedLock(row.id)
        } else {
          data = await erpFlowSealedUnLock(row.id)
        }
        this.$common.hideLoad()
        if (typeof data != 'undefined') {
          this.$common.n_success(`${type == 1 ? '封存' : '解封'}成功`);
          this.getList()
        }
      })
      .catch(() => {
        //取消
      });
    }
  }
};
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
<style lang="scss">
  .order-table-view {
    .table-row {
      margin: 0 30px;
      td {
        .el-table__expand-icon{
          visibility: hidden;
        }
      }
    }
  }
</style>
