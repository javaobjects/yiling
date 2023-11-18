<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box" style="margin-top: 0;">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">楼层名称</div>
              <el-input v-model="query.name" @keyup.enter.native="searchEnter" placeholder="请输入楼层名称" />
            </el-col>
            <el-col :span="6">
              <div class="title">楼层状态</div>
              <el-select class="select-width" v-model="query.status" placeholder="请选择">
                <el-option label="全部" :value="''"></el-option>
                <el-option label="启用" :value="1"></el-option>
                <el-option label="禁用" :value="2"></el-option>
              </el-select>
            </el-col>
            <el-col :span="8">
              <div class="title">创建时间</div>
              <el-date-picker
                v-model="query.time"
                type="daterange"
                format="yyyy/MM/dd"
                value-format="yyyy-MM-dd"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期">
              </el-date-picker>
            </el-col>
          </el-row>
        </div>
        <div class="search-box">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn :total="query.total" @search="handleSearch" @reset="handleReset" />
            </el-col>
          </el-row>
        </div>
      </div>
      <div class="down-box clearfix">
        <div class="btn">
          <yl-button v-role-btn="['1']" type="primary" @click="addClick(1, 0)">新建</yl-button>
        </div>
      </div>
      <!-- table  -->
      <div class="my-table mar-t-8">
        <yl-table 
          :show-header="true" 
          stripe 
          :list="dataList" 
          :total="query.total" 
          :page.sync="query.page" 
          :limit.sync="query.limit" 
          :loading="loading" 
          @getList="getList">
          <el-table-column label="序号" min-width="60" align="center">
            <template slot-scope="{ $index }">
              <div class="font-size-base">{{ (query.page - 1) * query.limit + $index + 1 }}</div>
            </template>
          </el-table-column>
          <el-table-column label="楼层名称" min-width="150" align="center" prop="name">
          </el-table-column>
          <el-table-column label="包含商品数" min-width="80" align="center" prop="goodsNum">
          </el-table-column>
          <el-table-column label="排序" min-width="60" align="center" prop="sort">
          </el-table-column>
          <el-table-column label="状态" min-width="90" align="center">
            <template slot-scope="{ row }">
              <div :class="row.status == 1 ? 'status-color-green' : 'status-color-red'">
                {{ row.status == 1 ? '启用' : '停用' }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="创建时间" min-width="140" align="center">
            <template slot-scope="{ row }">
              {{ row.createTime | formatDate }}
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" min-width="140">
            <template slot-scope="{ row }">
              <div class="table-content">
                <yl-button type="text" v-role-btn="['2']" @click="addClick(1, row.id)">编辑</yl-button>
                <yl-button type="text" v-role-btn="['3']" @click="statusClick(row)">{{ row.status == 1 ? '停用' : '启用' }}</yl-button>
                <yl-button type="text" v-role-btn="['4']" @click="deleteClick(row)">删除</yl-button>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </div>
</template>

<script>
import { queryListPage, floorDelete, updateStatus } from '@/subject/pop/api/b2b_api/floor_manage'
export default {
  name: 'FloorManage',
  components: {
  },
  computed: {
   
  },
  data() {
    return {
      // 头部导航
      navList: [
        {
          title: '首页',
          path: '/b2b_dashboard'
        },
        {
          title: '店铺管理',
          path: ''
        },
        {
          title: '楼层管理'
        }
      ],
      query: {
        name: '',
        status: '',
        time: '',
        total: 0,
        page: 1,
        limit: 10
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
   
    //  获取控价商品列表
    async getList() {
      this.loading = true
      let query = this.query
      let data = await queryListPage(
        query.name,
        query.status,
        query.time && query.time.length > 0 ? query.time[0] : '',
        query.time && query.time.length > 1 ? query.time[1] : '',
        query.page,
        query.limit
      )
      if (data !== undefined) {
        this.dataList = data.records
        this.query.total = data.total
      }
      this.loading = false
    },
    //编辑
    addClick(type, id) {
      //type 1创建 2编辑
      this.$router.push({
        name: 'FloorManageEstablish',
        params: {
          type: type,
          id: id
        }
      })
    },
    //启用/禁用
    statusClick(row) {
      this.$confirm(`${row.status == 1 ? '停用' : '启用'} ${ row.name } 后该楼层位置及内容将在店铺首页${row.status == 1 ? '隐藏' : '展示'}，确定${row.status == 1 ? '停用' : '启用'}吗？`, `${row.status == 1 ? '停用' : '启用'}楼层`, {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      .then( async() => {
        //确定
        this.$common.showLoad();
        let data = await updateStatus(
          row.id,
          row.status == 1 ? 2 : 1
        )
        this.$common.hideLoad();
        if (data!==undefined) {
          this.$common.n_success(`${row.status == 1 ? '停用' : '启用'} 成功!`);
          this.getList();
        }
      })
      .catch(() => {
        //取消
      });
    },
    //删除
    deleteClick(row) {
      this.$confirm(`删除 ${ row.name } 后该楼层位置及内容将无法找回，确定删除吗？`, '删除楼层', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      .then( async() => {
        //确定
        this.$common.showLoad();
        let data = await floorDelete(
          row.id
        )
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
    handleSearch() {
      this.query.page = 1
      this.getList()
    },
    handleReset() {
      this.query = {
        page: 1,
        limit: 10,
        name: '',
        licenseNo: '',
        manufacturer: '',
        outReason: 0,
        status: ''
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>

