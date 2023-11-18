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
          <el-table-column>
            <template slot-scope="{ row, $index }">
              <div class="table-view" :key="$index" v-if="data.finishType == 3">
                <p class="pull-people-table-column">{{ row.terminalName }}</p>
                <div>
                  <el-row class="table-row">
                    <el-col :span="24">
                      <p>终端联系人姓名：{{ row.contactor }}</p>
                    </el-col>
                  </el-row>
                </div>
              </div>
              <div class="table-view" :key="$index" v-if="data.finishType == 7">
                <p class="pull-people-table-column">{{ row.name }}</p>
                <div>
                  <el-row class="table-row">
                    <el-col :span="8">
                      <p>注册时间：{{ row.registerTime | formatDate }}</p>
                    </el-col>
                    <el-col :span="8">
                      <p>联系电话：{{ row.mobile }}</p>
                    </el-col>
                  </el-row>
                </div>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
    <div class="flex-row-center bottom-view" >
      <yl-button type="primary" @click="$router.go(-1)">返回</yl-button>
    </div>
  </div>
</template>

<script>
import { listTaskTraceTerminalPage, listTaskRegisterUserPage } from '@/subject/admin/api/views_sale/task_administration'
export default {
  name: 'LahularenDetails',
  components: {},
  data() {
    return {
      dataList: [],
      query: {
        total: 0,
        page: 1,
        limit: 10
      },
      data: {
        id: '',
        userTaskId: '',
        finishType: ''
      },
      loading: false
    }
  },
  mounted() {
    let query = this.$route.params;
    if (query) {
      this.data = {
        id: query.id,
        userTaskId: query.userTaskId,
        finishType: query.finishType
      };
      this.getList();
    }
  },
  methods: {
    // 获取数据 拉户
    async getList() {
      this.loading = true;
      let query = this.query;
      // 拉户
      if (this.data.finishType == 3) {
        let data = await listTaskTraceTerminalPage(
          query.page,
          query.limit,
          this.data.userTaskId
        )
        if (data) {
          this.dataList = data.records;
          this.query.total = data.total;
        }
        //拉人;
      } else if (this.data.finishType == 7) {
        let data2 = await listTaskRegisterUserPage(
          query.page,
          query.limit,
          this.data.userTaskId
        )
        if (data2) {
          this.dataList = data2.records;
          this.query.total = data2.total;
        }
      }
      this.loading = false
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>