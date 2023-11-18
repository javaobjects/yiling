<template>
  <div class="app-container">
    <!-- 内容区域 -->
    <div class="app-container-content has-bottom-bar">
      <!-- 下部表格 -->
      <div class="pull-people-buttom">
        <p class="task-bottom-title">销售记录</p>
        <yl-table
          border
          stripe
          :list="dataList"
          :total="query.total"
          :page.sync="query.page"
          :limit.sync="query.limit"
          :loading="loading"
          :horizontal-border="false"
          @getList="getList">
          <el-table-column >
            <template slot-scope="{ row, $index }">
              <div class="table-view" :key="$index">
                <p class="pull-people-table-column">{{ row.recvEname }}</p>
                <div>
                  <el-row class="table-row">
                    <el-col :span="8">
                      <p>单据编号：</p>
                      <span>{{ row.docCode }}</span>
                    </el-col>
                    <el-col :span="16">
                      <p>第一次上传资料时间：</p>
                      <span>{{ row.uploadTime | formatDate }}</span>
                    </el-col>
                  </el-row>
                </div>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
    <div class="flex-row-center bottom-view">
      <yl-button type="primary" @click="$router.go(-1)">返回</yl-button>
    </div>
  </div>
</template>

<script>
import { queryTaskAccompanyBillPage } from '@/subject/admin/api/views_sale/task_administration'
export default {
  name: 'DepartmentDetails',
  computed: {},
  components: {},
  data() {
    return {
      dataList: [],
      query: {
        total: 0,
        page: 1,
        limit: 10
      },
      loading: false
    }
  },
  mounted() {
    let query = this.$route.params;
    if (query) {
      this.getList(query.userTaskId);
    }
  },
  methods: {
    // 获取数据
    async getList(userTaskId) {
      this.loading = true;
      let query = this.query;
      let data = await queryTaskAccompanyBillPage(
        userTaskId,
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