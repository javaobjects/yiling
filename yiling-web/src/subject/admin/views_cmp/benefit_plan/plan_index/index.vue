<template>
  <div class="app-container">
    <!-- 内容区域 -->
    <div class="app-container-content">
      <!-- 下部列表 -->
      <div class="search-bar">
        <yl-table 
          border 
          :show-header="true" 
          :list="dataList" 
          :loading="loading" 
          @getList="getList">
          <el-table-column align="center" min-width="50" label="ID" prop="id">
          </el-table-column>
          <el-table-column align="center" min-width="120" label="用药福利计划名称" prop="name">
          </el-table-column>
          <el-table-column align="center" min-width="160" label="药品" prop="drugSellSpecifications">
          </el-table-column>
          <el-table-column align="center" min-width="160" label="活动周期" prop="hdzq">
            <template slot-scope="{ row }">
              {{ row.beginTime | formatDate }} - {{ row.endTime | formatDate }}
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="80" label="状态">
            <template slot-scope="{ row }">
              {{ row.status == 1 ? '启用' : '禁用' }}
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="100" label="创建时间">
            <template slot-scope="{ row }">
              {{ row.createTime | formatDate }}
            </template>
          </el-table-column>
          <el-table-column fixed="right" align="center" label="操作" min-width="80">
            <template slot-scope="{ row }">
              <div>
                <yl-button type="text" @click="modifyClick(row)">编辑</yl-button>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </div>
</template>
<script>
import { queryPage } from '@/subject/admin/api/cmp_api/benefit_plan'
export default {
  name: 'PlanIndex',
  components: {},
  data() {
    return {
      query: {
        total: 0,
        page: 1,
        limit: 10
      },
      loading: false,
      dataList: []
    }
  },
  activated() {
    this.getList();
  },
  methods: {
    async getList() {
      this.loading = true;
      let query = this.query;
      let data = await queryPage(
        query.page,
        query.limit
      )
      if (data) {
        this.dataList = data.records;
        this.query.total = data.total;
      }
      this.loading = false;
    },
    // 编辑
    modifyClick(row) {
      this.$router.push({
        name: 'PlanIndexEdit',
        params: {
          id: row.id
        }
      });
    }
  }
}
</script>