<template>
  <div class="app-container">
    <!-- 内容区域 -->
    <div class="app-container-content">
      <!-- 中部 搜索条件 -->
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
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
              <div class="title">创建时间</div>
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
        <ylButton type="primary" @click="addClick">添加</ylButton>
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
          <el-table-column align="center" min-width="80" label="ID" prop="id">
          </el-table-column>
          <el-table-column align="center" min-width="150" label="终端商家" prop="ename">
          </el-table-column>
          <el-table-column align="center" min-width="230" label="用药福利计划名称">
            <template slot-scope="{ row }">
              {{ row.drugWelfareName }} 【ID：{{ row.drugWelfareId }}】
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
                <yl-button type="text" @click="deleteClick(row)">删除</yl-button>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </div>
</template>
<script>
import { queryEnterpriseList, queryDrugWelfareList, enterpriseQueryPage, enterpriseDelete } from '@/subject/admin/api/cmp_api/benefit_plan'
export default {
  name: 'TerminalMerchants',
  computed: {
  },
  data() {
    return {
      query: {
        eid: '',
        drugWelfareId: '',
        establishTime: [],
        total: 0,
        page: 1,
        limit: 10
      },
      loading: false,
      // 列表展示数据
      dataList: [],
      // 终端商家
      businessData: [],
      // 用药福利计划名称
      benefitPlanData: []
    }
  },
  activated() {
    // 获取商家下拉选单
    this.getDusinessList();
    // 获取用药福利计划名称
    this.getBenefitPlan();
    // 获取列表数据
    this.getList();
  },
  methods: {
    // 搜索
    handleSearch() {
      this.query.page = 1;
      this.getList();
    },
    // 重置
    handleReset() {
      this.query = {
        
      }
    },
    // 获取商家下拉选单
    async getDusinessList() {
      let data = await queryEnterpriseList()
      if (data) {
        this.businessData = data.list;
      }
    },
    async getBenefitPlan() {
      let data = await queryDrugWelfareList()
      if (data) {
        this.benefitPlanData = data.list;
      }
    },
    // 点击删除
    deleteClick(row) {
      this.$confirm(`确认删除 ${ row.ename } ！`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      .then(async() => {
        //确定
        this.$common.showLoad();
        let data = await enterpriseDelete(row.id)
        this.$common.hideLoad();
        if (data !== undefined) {
          this.$common.n_success('删除成功!');
          this.getList();
        }
      })
      .catch(() => {
      //取消
      });
    },
    // 点击添加
    addClick() {
      this.$router.push({
        name: 'TerminalMerchantsAdd'
      });
    },
    async getList() {
      this.loading = true;
      let query = this.query;
      let data = await enterpriseQueryPage(
        query.eid,
        query.drugWelfareId,
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
    }
  }
}
</script>
<style lang="scss" scoped>
@import './index.scss';
</style>