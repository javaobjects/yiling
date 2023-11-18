<template>
  <div class="app-container">
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">权益类型</div>
              <el-select v-model="query.type" placeholder="请选择权益类型">
                <el-option label="全部" :value="0"></el-option>
                <el-option
                  v-for="item in typeList"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-col>
            <el-col :span="8">
              <div class="title">权益名称</div>
              <el-input v-model="query.name" @keyup.enter.native="searchEnter" placeholder="请输入权益名称" />
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn
              :total="query.total"
              @search="handleSearch"
              @reset="handleReset"/>
            </el-col>
          </el-row>
        </div>
      </div>
      <!-- 导出按钮 -->
      <div class="down-box clearfix">
        <yl-button type="primary" @click="addBanner">新建</yl-button>
      </div>
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
          <el-table-column label="序号" min-width="55" align="center">
            <template slot-scope="{ $index }">
              <div class="font-size-base">{{ (query.page - 1) * query.limit + $index + 1 }}</div>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="100" label="权益图标">
            <template slot-scope="{ row }">
              <div class="img-view">
                <el-image fit="contain" class="img" :src="row.icon"></el-image>
              </div>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="120" label="权益名称" prop="name">
          </el-table-column>
          <el-table-column align="center" min-width="120" label="权益类型">
            <template slot-scope="{ row }">
              <div>
                {{ row.type | dictLabel(typeList) }}
              </div>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="180" label="权益说明" prop="description">
          </el-table-column>
          <el-table-column align="center" width="80" label="权益状态">
            <template slot-scope="{ row }">
              <div>
                <el-switch
                  @change="statusChange(row)"
                  :value="row.status == 1">
                </el-switch>
              </div>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="120" label="最后修改时间">
            <template slot-scope="{ row }">
              <span>{{ row.updateTime | formatDate }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" label="操作" width="120">
            <template slot-scope="{ row }">
              <div>
                <yl-button type="text" @click="edit(row)">编辑</yl-button>
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
  import { memberEquityListPage, updateStatus, deleteEquity } from '@/subject/admin/api/b2b_api/membership'

  export default {
    name: 'MemberRightsList',
    components: {
    },
    computed: {
    },
    data() {
      return {
        typeList: [
          {
            label: '系统生成',
            value: 1
          },
          {
            label: '自定义',
            value: 2
          }
        ],
        query: {
          page: 1,
          limit: 10,
          total: 0,
          type: 0,
          name: ''
        },
        dataList: [],
        loading: false
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
        let data = await memberEquityListPage(
          query.page,
          query.limit,
          query.type,
          query.name
        )
        this.loading = false
        if (data) {
          this.dataList = data.records
          this.query.total = data.total
        }
      },
      handleSearch() {
        this.query.page = 1
        this.getList()
      },
      handleReset() {
        this.query = {
          page: 1,
          limit: 10,
          type: 0
        }
      },
      addBanner() {
        this.$router.push({
          name: 'MemberRightsEdit',
          params: {}
        });
      },
      // 修改状态
      async statusChange(row) {
        this.$common.showLoad()
        let data = await updateStatus(row.id)
        this.$common.hideLoad()
        if (data != undefined) {
          this.$common.n_success('修改成功');
          this.getList()
        }
      },
      edit(row) {
        // 跳转详情
        this.$router.push({
          name: 'MemberRightsEdit',
          params: { id: row.id }
        });
      },
      // 删除
      deleteClick(row) {
        this.$confirm('确定删除此权益信息吗？', '删除权益', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        .then(() => {
          //确定
          this.removeMethod(row);
        })
        .catch(() => {
          //取消
        });
      },
      async removeMethod(row) {
        this.$common.showLoad()
        let data = await deleteEquity(row.id)
        this.$common.hideLoad()
        if (data != undefined) {
          this.$common.n_success('删除成功');
          this.getList()
        }
      }
    }
  }
</script>

<style lang="scss" scoped>
  @import "./index.scss";
</style>

