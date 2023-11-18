<template>
  <div class="app-container">
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">活动名称</div>
              <el-input v-model="query.activityName" placeholder="请输入活动名称" @keyup.enter.native="searchEnter"/>
            </el-col>
          </el-row>
        </div>
        <div class="search-btn-box mar-t-16">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn :total="query.total" @search="handleSearch" @reset="handleReset" />
            </el-col>
          </el-row>
        </div>
      </div>
      <div class="down-box">
        <div class="btn">
          <yl-button type="primary" @click="addAdvertisement">添加</yl-button>
        </div>
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
          <el-table-column align="center" min-width="140" label="活动名称" prop="activityName">
          </el-table-column>
          <el-table-column align="center" min-width="90" label="医生人数" prop="activityDoctorCount">
          </el-table-column>
          <el-table-column align="center" min-width="110" label="开始时间">
            <template slot-scope="{ row }">
              <div>{{ row.beginTime | formatDate }}</div>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="110" label="结束时间">
            <template slot-scope="{ row }">
              <div>{{ row.endTime | formatDate }}</div>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="110" label="创建时间">
            <template slot-scope="{ row }">
              <div>{{ row.createTime | formatDate }}</div>
            </template>
          </el-table-column>
          <el-table-column fixed="right" align="center" label="操作" min-width="140">
            <template slot-scope="{ row }">
              <yl-button type="text" @click="modifyClick(row)">编辑</yl-button>
              <yl-button type="text" @click="deleteClick(row)">删除</yl-button>
              <yl-button type="text" @click="seeClick(row)">查看医生</yl-button>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </div>
</template>
<script>
import { pageList, delActivityById } from '@/subject/admin/api/cmp_api/goods_promotion'
export default {
  name: 'GoodsPromotionList',
  data() {
    return {
      query: {
        activityName: '',
        total: 0,
        page: 1,
        limit: 10
      },
      dataList: [],
      loading: false
    }
  },
  activated() {
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
    // 获取数据列表
    async getList() {
      let query = this.query;
      this.loading = true;
      let data = await pageList(
        query.activityName,
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
        name: 'AddGoodsPromotion',
        params: {
          id: row.id
        }
      })
    },
    // 删除
    deleteClick(row) {
      this.$confirm(`确定删除 ${ row.activityName } 活动！`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }) 
      .then( async() => {
        this.$common.showLoad();
        let data = await delActivityById(row.id)
        this.$common.hideLoad();
        if (data !== undefined) {
          this.$common.n_success('删除成功');
          this.getList()
        }
      })
      .catch(() => {
        //取消
      });
    },
    // 查看医生
    seeClick(row) {
      this.$router.push({
        name: 'GoodsPromotionDoctor',
        params: {
          id: row.id
        }
      })
    },
    // 点击添加
    addAdvertisement() {
      this.$router.push({
        name: 'AddGoodsPromotion',
        params: {
          id: 0
        }
      })
    },
    // 搜索
    handleSearch() {
      this.query.page = 1;
      this.getList();
    },
    // 重置
    handleReset() {
      this.query = {
        activityName: '',
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
