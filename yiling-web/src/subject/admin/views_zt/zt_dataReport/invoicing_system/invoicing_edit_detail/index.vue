<template>
  <div class="app-container">
    <div class="app-container-content">
      <div class="mar-t-8 pad-b-100">
        <yl-table
          border
          :show-header="true"
          :list="dataList"
          :total="query.total"
          :page.sync="query.page"
          :limit.sync="query.limit"
          :loading="loading"
          @getList="getList"
        >
          <el-table-column min-width="100" align="center" label="商业名称" prop="ename"></el-table-column>
          <el-table-column min-width="100" align="center" label="商业ID" prop="eid"></el-table-column>
          <el-table-column min-width="100" align="center" label="商品内码" prop="goodsInSn"></el-table-column>
          <el-table-column min-width="100" align="center" label="商品名称" prop="goodsName"></el-table-column>
          <el-table-column min-width="100" align="center" label="商品规格" prop="goodsSpecifications"></el-table-column>
          <el-table-column min-width="100" align="center" label="以岭品ID" prop="ylGoodsId"></el-table-column>
          <el-table-column min-width="100" align="center" label="以岭品名称" prop="ylGoodsName"></el-table-column>
          <el-table-column min-width="100" align="center" label="以岭品规格" prop="ylGoodsSpecifications"></el-table-column>
          <el-table-column min-width="140" align="center" label="调整时间" prop="createTime">
            <template slot-scope="{ row }">
              <div>{{ row.createTime | formatDate }}</div>
            </template>
          </el-table-column>
          <el-table-column min-width="100" align="center" label="调整数量" prop="poQuantity"></el-table-column>
          <el-table-column min-width="100" align="center" label="操作人" prop="opUserName"></el-table-column>
        </yl-table>
      </div>
      <div class="bottom-view flex-row-center">
        <yl-button type="primary" plain @click="$router.go(-1)">返回</yl-button>
      </div>
    </div>
  </div>
</template>

<script>
import { queryStockRevisePageList } from '@/subject/admin/api/zt_api/dataReport';
export default {
  name: 'ZtParamsEditPrice',
  components: {},
  data() {
    return {
      query: {
        total: 0,
        page: 1,
        limit: 10,
        eid: '',
        goodsInSn: '',
        purchaseChannel: '',
        ylGoodsId: ''
      },
      loading: false,
      dataList: []
    };
  },
  mounted() {
    const { id } = this.$route.params;
    if ( id !== undefined ) {
      this.query.id = id;
      this.getList();
    }
  },
  methods: {
    async getList() {
      let query = this.query;
      this.loading = true;
      let data = await queryStockRevisePageList(
        query.page,
        query.limit,
        Number(query.id)
      );
      this.loading = false;
      if (data) {
        this.dataList = data.records;
        this.query.total = data.total;
      }
    },
    handleSearch() {
      this.query.page = 1;
      this.getList();
    },
    handleReset() {
      this.query = {
        total: 0,
        page: 1,
        limit: 10,
        goodsName: '',
        updateUser: '',
        goodsInSn: '',
        purchaseChannel: '',
        reviseStock: '',
        ylGoodsId: ''
      };
    }
  }
};
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
