<template>
  <div class="app-container">
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">商业名称</div>
              <el-select
                v-model="query.eid"
                clearable
                filterable
                remote
                reserve-keyword
                :remote-method="remoteMethod"
                @clear="clearEnterprise"
                :loading="searchLoading"
                placeholder="请搜索并选择商业">
                <el-option v-for="item in sellerEnameOptions" :key="item.id" :label="item.name" :value="item.id"></el-option>
              </el-select>
            </el-col>
            <el-col :span="8">
              <div class="title">以岭品名称</div>
              <el-select
                v-model="query.ylGoodsId"
                clearable
                filterable
                remote
                reserve-keyword
                :remote-method="goodsRemoteMethod"
                :loading="goodsloading"
                @clear="goodClearEnterprise"
                placeholder="请搜索以岭品名称">
                <el-option v-for="item in options" :key="item.id" :label="item.name" :value="item.id">
                  <span>{{ item.name }}---{{ item.sellSpecifications }}</span>
                </el-option>
              </el-select>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="24">
              <yl-search-btn :total="query.total" @search="handleSearch" @reset="handleReset" />
            </el-col>
          </el-row>
        </div>
      </div>
      <div class="down-box">
        <div></div>
        <div class="btn">
          <yl-button type="primary" plain @click="downLoadTemp">导出查询结果</yl-button>
        </div>
      </div>
      <div class="mar-t-8 pad-b-100">
        <yl-table
          ref="summaryTableRef"
          border
          :show-header="true"
          row-key="id"
          :list="dataList"
          :total="query.total"
          :page.sync="query.page"
          :limit.sync="query.limit"
          :loading="loading"
          @getList="getList"
        >
          <el-table-column min-width="100" align="center" label="商业ID" prop="eid"></el-table-column>
          <el-table-column min-width="100" align="center" label="商业名称" prop="ename"></el-table-column>
          <el-table-column min-width="100" align="center" label="商品内码" prop="goodsInSn"></el-table-column>
          <el-table-column min-width="100" align="center" label="商品名称" prop="goodsName"></el-table-column>
          <el-table-column min-width="140" align="center" label="商品规格" prop="goodsSpecifications"></el-table-column>
          <el-table-column min-width="100" align="center" label="以岭品ID" prop="ylGoodsId"></el-table-column>
          <el-table-column min-width="100" align="center" label="以岭品名称" prop="ylGoodsName"></el-table-column>
          <el-table-column min-width="100" align="center" label="以岭品规格" prop="ylGoodsSpecifications"></el-table-column>
          <el-table-column min-width="100" align="center" label="采购库存" prop="totalPoQuantity"></el-table-column>
          <el-table-column min-width="100" align="center" label="购进渠道" prop="ename">
            <template slot-scope="{ row }">
              <div>{{ row.poSource | poSourceList }}</div>
            </template>
          </el-table-column>
          <el-table-column min-width="100" align="center" label="已返利库存" prop="ename">
            <template slot-scope="{ row }">
              <div><yl-button type="text" @click="detail(1, row)">{{ row.rebateStock }}</yl-button></div>
            </template>
          </el-table-column>
          <el-table-column min-width="100" align="center" label="采购剩余库存" prop="poQuantity"></el-table-column>
          <el-table-column min-width="100" align="center" label="调整库存" prop="ename">
              <template slot-scope="{ row }">
              <div><yl-button type="text" @click="detail(2, row)">{{ row.reviseStock }}</yl-button></div>
            </template>
          </el-table-column>
          <el-table-column min-width="60" fixed="right" align="center" label="操作">
            <template slot-scope="{ row }">
              <div><yl-button type="text" @click="edit(row)">调整</yl-button></div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
      <!-- 调整库存 -->
      <yl-dialog :visible.sync="show" width="600px" title="调整库存" @confirm="confirm">
        <div class="dialog-content">
          <el-form class="pad-t-8" ref="form" :rules="formRules" :model="form" label-width="100px">
            <el-row>
              <el-col :span="24" :offset="0">
                <el-form-item label="商业名称：">
                  <span class="mar-l-8">{{ form.ename }}</span>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24" :offset="0">
                <el-form-item label="商品名称：">
                  <span class="mar-l-8">{{ form.goodsName }}</span>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24" :offset="0">
                <el-form-item label="商品内码：">
                  <span class="mar-l-8">{{ form.goodsInSn }}</span>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24" :offset="0">
                <el-form-item label="购进渠道：">
                  <span class="mar-l-8">{{ form.poSource | poSourceList }}</span>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24" :offset="0">
                <el-form-item label="调整数量：" prop="reviseStock">
                  <el-input v-model="form.reviseStock" placeholder="请输入数量"></el-input>
                  <span class="mar-l-8">正负整数，将影响购进数量</span>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </div>
      </yl-dialog>
    </div>
  </div>
</template>

<script>
import { queryPurchasePageList, reviseStockList, queryEnterpriseList, queryGoodsList } from '@/subject/admin/api/zt_api/dataReport';
import { createDownLoad } from '@/subject/admin/api/common'

export default {
  name: 'InvoicingSystem',
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
    }
  },
  data() {
    return {
      query: {
        total: 0,
        page: 1,
        limit: 10,
        eid: '',
        goodsName: '',
        ylGoodsId: '',
        goodsUnit: '',
        goodsInSn: ''
      },
      options: [],
      goodsloading: false,
      searchLoading: false,
      loading: false,
      dataList: [],
      show: false,
      form: {
        reviseStock: '',
        ename: '',
        goodsName: '',
        goodsInSn: '',
        poSource: '',
        id: ''
      },
      formRules: {
        reviseStock: [
          { required: true, message: '请输入数量', trigger: 'blur' }
        ]
      },
      sellerEnameOptions: []
    };
  },
  activated() {
    // this.getList();
  },
  methods: {
    async getList() {
      const query = this.query;
      if (!query.eid && !query.ylGoodsId) {
        this.dataList = []
        return this.$common.warn('商业名称和以岭品名称不能同时为空')
      }
      this.loading = true;
      const data = await queryPurchasePageList(
        query.page,
        query.limit,
        query.eid,
        query.ylGoodsId
      );
      this.loading = false;
      if (data != undefined) {
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
        eid: '',
        goodsName: ''
      }
      this.sellerEnameOptions = []
    },
    async remoteMethod(query) {
      if (query.trim() != '') {
        this.searchLoading = true
        let data = await queryEnterpriseList(1, 10, query.trim())
        this.searchLoading = false
        if (data) {
          this.sellerEnameOptions = data.records
        }
      } else {
        this.sellerEnameOptions = []
      }
    },
    // 远程搜索商品
    async goodsRemoteMethod(query) {
      if (query.trim() !== '') {
        this.goodsloading = true;
        let data = await queryGoodsList(query);
        this.goodsloading = false;
        if (data !== undefined) {
          this.options = data.list;
        }
      } else {
        this.options = [];
      }
    },
    // 选择商品
    selectGoods(e) {
      this.query.goodsName = e.name;
      this.query.ylGoodsId = e.id;
    },
    // 调整
    edit(row) {
      console.log('row',row);
      this.form.ename = row.ename || ''
      this.form.goodsName = row.goodsName || ''
      this.form.goodsInSn = row.goodsInSn || ''
      this.form.poSource = row.poSource || ''
      this.form.id = row.id
      this.form.reviseStock = ''
      this.show = true
    },
    // 调整库存
    confirm() {
      this.$refs['form'].validate(async (valid) => {
        if (valid) {
          this.$common.showLoad();
          let data = await reviseStockList(
            this.form.id,
            Number(this.form.reviseStock)
          );
          this.$common.hideLoad();
          if (data !== undefined) {
            this.getList();
            this.show = false
            this.$common.success('调整成功');
          }
        } else {
          return false;
        }
      });
    },
    detail(type, e) {
      if (type == 1) {
        //已返利库存
        this.$router.push(`/zt_dataReport/invoicing_buy_detail/${e.eid}/${e.goodsInSn}/${e.poSource}/${e.ylGoodsId}`)
      } else if (type == 2){
        //调整库存
        this.$router.push(`/zt_dataReport/invoicing_edit_detail/${e.id}`)
      }
    },
    async downLoadTemp() {
      const query = this.query
      this.$common.showLoad()
      const data = await createDownLoad({
        className: 'reportPurchaseStockExportServiceImpl',
        fileName: '导出报表进销存信息',
        groupName: '数据报表',
        menuName: '数据报表-进销存中心',
        searchConditionList: [
          {
            desc: '商业eid',
            name: 'eid',
            value: query.eid || ''
          },
          {
            desc: '以岭品id',
            name: 'ylGoodsId',
            value: query.ylGoodsId || ''
          }
        ]
      })
      this.$common.hideLoad()
      if (typeof data !== 'undefined') {
        this.$common.n_success('创建下载任务成功，请在下载中心查看')
      }
    },

    clearEnterprise() {
      this.sellerEnameOptions = []
    },
    goodClearEnterprise() {
      this.options = []
    }
  }
};
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
