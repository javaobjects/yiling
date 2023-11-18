<template>
  <div class="app-container">
    <!-- 内容区域 -->
    <div class="app-container-content">
      <!-- 中部 搜索条件 -->
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">商品名称</div>
              <el-input v-model="query.goodsName" @keyup.enter.native="searchEnter" placeholder="请输入商品名称" />
            </el-col>
            <el-col :span="6">
              <div class="title">上架状态</div>
              <el-select class="select-width" v-model="query.shelfStatus" placeholder="请选择">
                <el-option label="全部" value=""></el-option>
                <el-option label="已上架" value="1"></el-option>
                <el-option label="已下架" value="2"></el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <div class="title">商品类型</div>
              <el-select class="select-width" v-model="query.goodsType" placeholder="请选择">
                <el-option label="全部" value=""></el-option>
                <el-option
                  v-for="item in integralGoodsTypeData"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-col>
          </el-row>
        </div>
        <div class="search-box  mar-t-16">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">创建人</div>
              <el-input v-model="query.createUserName" @keyup.enter.native="searchEnter" placeholder="请输入创建人" />
            </el-col>
            <el-col :span="6">
              <div class="title">创建人手机号</div>
              <el-input v-model="query.mobile" @keyup.enter.native="searchEnter" placeholder="请输入创建人手机号" />
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
          <ylButton type="primary" @click="seeClick(1, 0)">添加</ylButton>
        </div>
      </div>
      <!-- 下部列表 -->
      <div class="search-bar">
        <yl-table 
          border 
          :show-header="true" 
          :list="dataList" 
          :total="query.total" 
          :page.sync="query.page" 
          :limit.sync="query.limit" 
          :loading="loading" 
          @getList="getList">
          <el-table-column label="商品ID" min-width="60" align="center" prop="id">
          </el-table-column>
          <el-table-column label="商品名称" min-width="120" align="center" prop="goodsName">
          </el-table-column>
          <el-table-column label="所需积分" min-width="90" align="center" prop="exchangeUseIntegral">
          </el-table-column>
          <el-table-column label="上架状态" min-width="90" align="center">
            <template slot-scope="{ row }">
              {{ row.shelfStatus == 1 ? '已上架' : '已下架' }}
            </template>
          </el-table-column>
          <el-table-column label="商品类型" min-width="120" align="center">
            <template slot-scope="{ row }">
              {{ row.goodsType | dictLabel(integralGoodsTypeData) }}
            </template>
          </el-table-column>
          <el-table-column label="排序值" min-width="80" align="center" prop="sort">
          </el-table-column>
          <el-table-column label="剩余可兑换数量" min-width="110" align="center" prop="canExchangeNum">
          </el-table-column>
          <el-table-column label="近30天兑换数量" min-width="110" align="center" prop="recentExchangeNum">
          </el-table-column>
          <el-table-column label="创建时间" min-width="120" align="center">
            <template slot-scope="{ row }">
              {{ row.createTime | formatDate }}
            </template>
          </el-table-column>
          <el-table-column label="最近修改时间" min-width="120" align="center">
            <template slot-scope="{ row }">
              {{ row.updateTime | formatDate }}
            </template>
          </el-table-column>
          <el-table-column label="创建人" min-width="120" align="center" prop="createUserName">
          </el-table-column>
          <el-table-column label="创建人手机号" min-width="120" align="center" prop="mobile">
          </el-table-column>
          <el-table-column label="操作" min-width="110" align="center" fixed="right">
            <template slot-scope="{ row }">
              <yl-button type="text" @click="seeClick(2, row.id)">查看</yl-button>
              <yl-button type="text" @click="seeClick(3, row.id)">编辑</yl-button>
              <yl-button type="text" @click="upDownClick(row)">{{ row.shelfStatus == 1 ? '下架' : '上架' }}</yl-button>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </div>
</template>
<script>
import { integralGoodsType } from '@/subject/admin/busi/b2b/integral'
import { queryListPage, updateShelfStatus } from '@/subject/admin/api/b2b_api/integral_record'

export default {
  name: 'PointsRedemption',
  computed: {
    integralGoodsTypeData() {
      return integralGoodsType()
    }
  },
  data() {
    return {
      query: {
        goodsName: '',
        shelfStatus: '',
        goodsType: '',
        createUserName: '',
        mobile: '',
        total: 0,
        page: 1,
        limit: 10
      },
      loading: false,
      dataList: []
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
      this.loading = true;
      let query = this.query;
      let data = await queryListPage(
        query.goodsName,
        query.shelfStatus,
        query.goodsType,
        query.createUserName,
        query.mobile,
        query.page,
        query.limit
      )
      if (data) {
        this.dataList = data.records;
        this.query.total = data.total;
      }
      this.loading = false;
    },
    seeClick(type, id) {
      this.$router.push({
        name: 'PointsRedemptionEstablish',
        params: {
          //1 创建 2查看 3编辑
          type: type,
          id: id
        }
      });
    },
    //点击上下架
    upDownClick(row) {
      this.$confirm(`确认 ${ row.shelfStatus == 1 ? '下架' : '上架' } ${ row.goodsName }`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      .then( async() => {
        //确定
        this.$common.showLoad();
        let data = await updateShelfStatus(
          row.id,
          row.shelfStatus == 1 ? 2 : 1
        )
        this.$common.hideLoad();
        if (data !== undefined) {
          this.$common.n_success('操作成功!');
          this.getList()
        }
      })
      .catch(() => {
        //取消
      });
    },
    handleSearch() {
      this.query.page = 1;
      this.getList()
    },
    handleReset() {
      this.query = {
        goodsName: '',
        shelfStatus: '',
        goodsType: '',
        createUserName: '',
        mobile: '',
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