<template>
  <div class="app-container">
    <!-- 内容区 -->
    <div class="app-container-content has-bottom-bar">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">商品名称</div>
              <el-select v-model="query.specificationId" filterable clearable placeholder="请选择">
                <el-option
                  v-for="item in commodityData"
                  :key="item.specificationId"
                  :label="item.goodsNameSpecDesc"
                  :value="item.specificationId">
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
          <ylButton type="primary" @click="addClick">匹配商品信息</ylButton>
        </div>
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
          <el-table-column align="center" min-width="80" label="商品名称" prop="goodsName">
          </el-table-column>
          <el-table-column align="center" min-width="80" label="规格" prop="spec">
          </el-table-column>
          <el-table-column align="center" min-width="80" label="销售数量" prop="soQuantity">
          </el-table-column>
          <el-table-column align="center" min-width="80" label="采购数据" prop="poQuantity">
          </el-table-column>
          <el-table-column align="center" min-width="80" label="月初库存" prop="beginMonthQuantity">
          </el-table-column>
          <el-table-column align="center" min-width="80" label="月末库存" prop="endMonthQuantity">
          </el-table-column>
        </yl-table>
      </div>
    </div>
    <!-- 弹窗 -->
    <yl-dialog 
      width="880px"
      title="匹配商品信息" 
      :visible.sync="dialogShow" 
      :show-footer="true"
      @confirm="confirm">
      <div>
        <yl-table
          border
          ref="dialogTables"
          :selection-change="handleSelectionChange"
          :stripe="true"
          :show-header="true" 
          :list="dataCommodity" 
          :loading="loading2">
          <el-table-column type="selection" align="center" width="70"></el-table-column>
          <el-table-column align="center" min-width="80" label="待匹配商品名称" prop="goodsName">
          </el-table-column>
          <el-table-column align="center" min-width="80" label="待匹配规格" prop="spec">
          </el-table-column>
          <el-table-column align="center" min-width="100" label="厂家" prop="manufacturer">
          </el-table-column>
          <el-table-column align="center" min-width="160" label="推荐商品名+规格">
            <template slot-scope="{ row, $index }">
              <div :key="$index">
                <el-select
                  v-model="row.goodsSpecification"
                  filterable
                  remote
                  reserve-keyword
                  placeholder="请输入商品名称"
                  :remote-method="remoteMethod"
                  :loading="goodsloading"
                  @change="selectGoods"
                  >
                    <el-option 
                      v-for="item in options" 
                      :key="item.name + '-' + item.sellSpecificationsId + '-' + item.sellSpecifications + '-' + $index + '-' + item.manufacturer" 
                      :label="item.name + '-' + item.sellSpecifications" 
                      :value="item.name + '-' + item.sellSpecificationsId + '-' + item.sellSpecifications + '-' + $index + '-' + item.manufacturer">
                      <span>{{ item.name }}---{{ item.sellSpecifications }}---{{ item.manufacturer }}</span>
                    </el-option>
                </el-select>
              </div>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="80" label="推荐分数" prop="recommendScore">
          </el-table-column>
        </yl-table>
      </div>
    </yl-dialog>
    <div class="flex-row-center bottom-view">
      <yl-button type="primary" plain @click="$router.go(-1)">返回</yl-button>
    </div>
  </div>
</template>

<script>
import { statisticsList, infoList, noMatchedList, getRecommendScore, specificationIdFlush } from '@/subject/admin/api/zt_api/month_flowto_monitor'
import { queryGoodsSpecification } from '@/subject/admin/api/zt_api/dataReport';
export default {
  name: 'CommodityDetails',
  data() {
    return {
      query: {
        specificationId: '',
        total: 0,
        page: 1,
        limit: 10
      },
      dataList: [],
      loading: false,
      paramsData: {
        eid: '', 
        monthTime: ''
      },
      // 商品名称列表
      commodityData: [],
      // 弹窗是否显示
      dialogShow: false,
      // 弹窗 商品信息列表
      dataCommodity: [],
      // 弹窗 商品loading状态
      loading2: false,
      options: [],
      goodsloading: false,
      matching: '',
      //点击确认传递给后端的 数组
      commodityDataList: []
    }
  },
  mounted() {
    let query = this.$route.params;
    if (query.eid) {
      this.paramsData = query;
      // 表格数据
      this.getList();
      // 商品列表
      this.commodityList()
    }
  },
  methods: {
    async getList() {
      this.loading = true;
      let query = this.query;
      let data = await statisticsList(
        this.paramsData.eid,
        this.paramsData.monthTime,
        query.specificationId,
        query.page,
        query.limit
      )
      if (data) {
        this.dataList = data.records;
        this.query.total = data.total
      }
      this.loading = false
    },
    async commodityList() {
      let data = await infoList(
        this.paramsData.eid,
        this.paramsData.monthTime
      )
      if (data) {
        this.commodityData = data
      }
    },
    // 搜索点击
    handleSearch() {
      this.query.page = 1;
      this.getList()
    },
    // 清空查询
    handleReset() {
      this.query = {
        specificationId: '',
        total: 0,
        page: 1,
        limit: 10
      }
    },
    // 点击匹配商品信息
    async addClick() {
      this.dataCommodity = [];
      this.loading2 = true;
      let data = await noMatchedList(
        this.paramsData.eid,
        this.paramsData.monthTime,
        this.query.specificationId
      )
      if (data && data.length > 0) {
        data.forEach(element => {
          this.dataCommodity.push({
            ...element,
            goodsSpecification: element.recommendGoods && element.recommendSpec ? element.recommendGoods + '-' + element.recommendSpec : ''
          })
        });
        this.dialogShow = true;
        this.$nextTick(() => { 
          this.dataCommodity.forEach(row => { 
            if (row.recommendScore && row.recommendScore >= 8000) {
              this.$refs.dialogTables.toggleRowSelectionMethod(row, true)
              this.commodityDataList.push(row)
            }
          })
        })
      } else {
        this.$common.warn('该企业无未匹配的商品信息')
      }
      this.loading2 = false
    },
    // 远程搜索商品
    async remoteMethod(query) {
      if (query != '') {
        this.goodsloading = true;
        let data = await queryGoodsSpecification(
          query
        )
        if (data) {
          this.options = data.list;
        }
        this.goodsloading = false
      }
    },
    // 选择 推荐商品名+规格
    async selectGoods(e) {
      if (e) {
        let val = e.split('-');
        console.log(val, 999)
        if (val && val.length > 3) {
          this.$common.showLoad();
          let data = await getRecommendScore(
            this.dataCommodity[val[3]].goodsName,
            this.dataCommodity[val[3]].spec,
            val[0],
            val[2],
            val[4],
            this.dataCommodity[val[3]].manufacturer
          )
          this.$common.hideLoad();
          if (data !== undefined) {
            this.dataCommodity[val[3]].recommendScore = data;
            this.dataCommodity[val[3]].recommendGoodsName = val[0];
            this.dataCommodity[val[3]].recommendSpec = val[2];
            this.dataCommodity[val[3]].recommendSpecificationId = val[1];
            this.dataCommodity[val[3]].goodsSpecification = val[0] + '-' + val[2]
          }
          this.$nextTick(() => {
            if (data >= 8000) {
              this.$refs.dialogTables.toggleRowSelectionMethod(this.dataCommodity[val[3]], true)
              this.commodityDataList.push(this.dataCommodity[val[3]])
            }
          })
        }
      }
    },
    // 点击弹窗保存
    async confirm() {
      this.$common.showLoad();
      let data = await specificationIdFlush(
        this.paramsData.eid,
        this.commodityDataList
      )
      this.$common.hideLoad();
      if (data !== undefined) {
        this.$common.success('操作成功');
        this.dialogShow = false;
        this.commodityList()
      }
    },
    // 点击勾选
    handleSelectionChange(val) {
      this.commodityDataList = val;
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>