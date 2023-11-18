<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box" style="margin-top:0;">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">商品组合名称</div>
              <el-input v-model="query.name" placeholder="请输入商品组合名称" @keyup.enter.native="handleSearch" />
            </el-col>
            <el-col :span="8">
              <div class="title">快速采购推荐位</div>
              <el-radio-group v-model="query.quickPurchaseFlag">
                <el-radio class="option-class" label="">全部</el-radio>
                <el-radio class="option-class" :label="0">是</el-radio>
                <el-radio class="option-class" :label="1">否</el-radio>
              </el-radio-group>
            </el-col>
          </el-row>
        </div>
        <div class="search-box">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn :total="total" @search="handleSearch" @reset="handleReset" />
            </el-col>
          </el-row>
        </div>
      </div>
      <!-- 导出、导入按钮 -->
      <div class="down-box clearfix">
        <div class="btn">
          <ylButton v-role-btn="['1']" type="primary" plain @click="addGoodsGroupClick">添加商品组合</ylButton>
        </div>
      </div>
      <!-- 底部列表 -->
      <div class="mar-t-10" style="padding-bottom: 10px;background: #FFFFFF;border-radius: 4px;">
        <yl-table
          border
          :show-header="true"
          :list="dataList"
          :total="total"
          :page.sync="query.page"
          :limit.sync="query.limit"
          :loading="loading"
          @getList="getList"
        >
          <el-table-column label="序号" min-width="55" align="center">
            <template slot-scope="{ $index }">
              <div class="font-size-base">{{ (query.page - 1) * query.limit + $index + 1 }}</div>
            </template>
          </el-table-column>
          <el-table-column label="商品组合名称" min-width="200" align="center">
            <template slot-scope="{ row }">
              <div>{{ row.name }}</div>
            </template>
          </el-table-column>
          <el-table-column label="商品信息" min-width="200" align="center">
            <template slot-scope="{ row, $index }">
              <div class="product-desc">
                <div v-for="item in row.relationList.slice(0, 5)" :key="item.goodsId + '' + $index">
                  {{ item.name }} {{ item.sellSpecifications }}
                </div>
                <span v-if="row.relationList.length > 5">...</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="商品数" min-width="100" align="center">
            <template slot-scope="{ row }">
              <div>{{ row.relationNum }}</div>
            </template>
          </el-table-column>
          <el-table-column label="快速采购推荐位" min-width="100" align="center">
            <template slot-scope="{ row }">
              <div>{{ row.quickPurchaseFlag == 1 ? '否' : '是' }}</div>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center">
            <template slot-scope="{ row }">
              <div><yl-button v-role-btn="['2']" type="text" @click="editDetailClick(row)">编辑</yl-button></div>
              <div><yl-button v-role-btn="['3']" type="text" @click="deleteClick(row)">删除</yl-button></div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </div>
</template>

<script>
import { recommendGoodsGroupPage, recommendGoodsGroupDeleteGroup } from '@/subject/pop/api/products'

export default {
  name: 'ProductsGroup',
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
          path: '/dashboard'
        },
        {
          title: '商品管理'
        },
        {
          title: '商品组合'
        }
      ],
      loading: false,
      query: {
        page: 1,
        limit: 10,
        name: '',
        quickPurchaseFlag: ''
      },
      total: 0,
      dataList: []
    };
  },
  activated() {
    this.getList();
  },
  methods: {
    async getList() {
      this.loading = true;
      let query = this.query;
      let data = await recommendGoodsGroupPage(
        query.page,
        query.limit,
        query.name,
        query.quickPurchaseFlag
      );
      this.loading = false;
      if (data) {
        this.dataList = data.records;
        this.total = data.total;
      }
    },
    // 搜索点击
    handleSearch() {
      this.query.page = 1
      this.getList();
    },
    handleReset() {
      this.query = {
        page: 1,
        limit: 10,
        name: '',
        quickPurchaseFlag: ''
      };
    },
    // 厂家删除点击
    deleteClick(row) {
      this.$confirm('确认删除 ！', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      .then( async() => {
        // //确定
        let data = await recommendGoodsGroupDeleteGroup(row.id)
        if (typeof data !== 'undefined') {
          this.$common.n_success('删除成功!');
          this.getList();
        }
      })
      .catch(() => {
        //取消
      });
    },
    // 添加商品组合
    addGoodsGroupClick() {
      this.$router.push({
        name: 'ProductsGroupEdit',
        params: {}
      });
    },
    //跳转详情界面
    editDetailClick(row) {
      this.$router.push({
        name: 'ProductsGroupEdit',
        params: { id: row.id}
      });
    }
  }
};
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
