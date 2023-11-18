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
          <el-table-column min-width="100" align="center" label="返利总表ID" prop="reportId"></el-table-column>
          <el-table-column min-width="100" align="center" label="返利类型" prop="type">
            <template slot-scope="{ row }">
              <div>{{ row.type | reportIdList }}</div>
            </template>
          </el-table-column>
          <el-table-column min-width="100" align="center" label="订单号" prop="orderNo"></el-table-column>
          <el-table-column min-width="100" align="center" label="erp订单号" prop="soNo"></el-table-column>
          <el-table-column min-width="100" align="center" label="商业名称" prop="sellerName"></el-table-column>
          <el-table-column min-width="100" align="center" label="商业ID" prop="sellerEid"></el-table-column>
          <el-table-column min-width="100" align="center" label="商品内码" prop="goodsInSn"></el-table-column>
          <el-table-column min-width="100" align="center" label="商品名称" prop="goodsName"></el-table-column>
          <el-table-column min-width="100" align="center" label="商品规格" prop="specifications"></el-table-column>
          <el-table-column min-width="100" align="center" label="以岭品ID" prop="ylGoodsId"></el-table-column>
          <el-table-column min-width="100" align="center" label="以岭品名称" prop="ylGoodsName"></el-table-column>
          <el-table-column min-width="100" align="center" label="以岭品规格" prop="ylGoodsSpecification"></el-table-column>
          <el-table-column min-width="100" align="center" label="数量" prop="soQuantity"></el-table-column>
          <el-table-column min-width="100" align="center" label="单位" prop="soUnit"></el-table-column>
          <el-table-column min-width="100" align="center" label="购进渠道" prop="purchaseChannel">
            <template slot-scope="{ row }">
              <div>{{ row.purchaseChannel | poSourceList }}</div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
      <div class="bottom-view flex-row-center">
        <yl-button type="primary" plain @click="$router.go(-1)">返回</yl-button>
      </div>
    </div>
  </div>
</template>

<script>
import { queryStockOccupyPageList } from '@/subject/admin/api/zt_api/dataReport';
export default {
  name: 'ZtParamsBuyPrice',
  components: {},
  filters: {
    poSourceList(e) {
      let res
      switch (e) {
        case 1:
          res = '大运河采购'
          break;
        case 2:
          res = '京东采购'
          break;
        default:
          res = '- -'
          break;
      }
      return res
    },
    reportIdList(e) {
      let res
      switch (e) {
        case 1:
          res = 'B2B返利'
          break;
        case 2:
          res = '流向返利'
          break;
        default:
          res = '- -'
          break;
      }
      return res
    }
  },
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
    const query = this.$route.params;
    this.query.eid = query.eid;
    this.query.goodsInSn = query.goodsInSn;
    this.query.purchaseChannel = query.purchaseChannel;
    this.query.ylGoodsId = query.ylGoodsId;
    this.getList();
  },
  methods: {
    async getList() {
      let query = this.query;
      this.loading = true;
      let data = await queryStockOccupyPageList(
        query.page,
        query.limit,
        query.eid,
        query.goodsInSn,
        query.purchaseChannel,
        query.ylGoodsId
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
        ylGoodsId: ''
      };
    }
  }
};
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
