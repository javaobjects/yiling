<template>
  <div class="app-container">
    <!-- 内容区域 -->
    <div class="app-container-content">
      <!-- 中部 搜索条件 -->
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="12">
              <div class="title">投放位置</div>
              <el-select class="select-width" v-model="query.position" placeholder="请选择">
                <el-option :value="0" label="全部"></el-option>
                <el-option
                  v-for="item in positionData"
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
              <yl-search-btn :total="query.total" @search="handleSearch" @reset="handleReset" />
            </el-col>
          </el-row>
        </div>
      </div>
      <div class="down-box clearfix">
        <div class="btn">
          <yl-button type="primary" @click="addAdvertisement">新增广告</yl-button>
        </div>
      </div>
      <!-- 下部列表 -->
      <div class="order-table-view mar-t-8 pad-b-10">
        <yl-table border show-header :list="dataList" :total="query.total" :page.sync="query.page" :limit.sync="query.limit" :loading="loading" @getList="getList">
           <el-table-column align="center" min-width="100" label="标题" prop="title"></el-table-column>
          <el-table-column align="center" min-width="120" label="广告图片">
            <template slot-scope="{ row }">
              <img class="img" :src="row.picUrl" alt="">
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="140" label="广告时段">
            <template slot-scope="{ row }">
              <span v-if="row.startTime < 0">
                长期有效
              </span>
              <span v-else>
                {{ row.startTime | formatDate }} 至 {{ row.stopTime | formatDate }}
              </span>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="80" label="投放位置">
            <template slot-scope="{ row }">
              <div v-for="(item,index) in row.position" :key="index">
                {{ item | dictLabel(positionData) }}
              </div>
            </template>
          </el-table-column>
         <el-table-column fixed="right" align="center" label="操作" min-width="100">
            <template slot-scope="{ row }">
              <div>
                <yl-button type="text" @click="modifyClick(row)">编辑</yl-button>
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
import { pageList, deletes } from '@/subject/admin/api/cmp_api/advertisement'
import { hmcAdvertisementPosition } from '@/subject/admin/utils/busi'
export default {
  name: 'AdvertisingManage',
  computed: {
    positionData() {
      return hmcAdvertisementPosition()
    }
  },
  data() {
    return {
      query: {
        position: 0,
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
    // 搜索
    handleSearch() {
      this.query.page = 1;
      this.getList();
    },
    // 重置
    handleReset() {
      this.query = {
        position: 0,
        total: 0,
        page: 1,
        limit: 10
      }
    },
    // 获取列表 
    async getList() {
      this.loading = true;
      let query = this.query;
      let data = await pageList(
        query.page,
        query.position,
        query.limit
      )
      if (data !== undefined) {
        this.dataList = data.records;
        this.query.total = data.total;
      }
      this.loading = false;
    },
    // 新增广告
    addAdvertisement() {
      this.$router.push({
        name: 'AdvertisingManageDetails',
        params: { 
          id: 0
        }
      });
    },
     // 删除 
    deleteClick(row) {
      this.$confirm(`确认删除 ${row.title} ！`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then( async() => {
          //确定
          this.$common.showLoad();
          let data = await deletes(row.id)
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
    //编辑
    modifyClick(row) {
      this.$router.push({
        name: 'AdvertisingManageDetails',
        params: { 
          id: row.id
        }
      })
    }
  }
}
</script>
<style lang="scss" scoped>
@import "./index.scss";
</style>
