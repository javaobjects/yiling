<template>
  <!-- 添加商品弹框 -->
  <yl-dialog title="设置商品" :visible.sync="addGoodsDialog" width="966px" :show-footer="false">
    <div class="dialog-content-box-customer">
      <div class="dialog-content-top">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">商品名称</div>
              <el-input v-model="goodsQuery.goodsName" placeholder="请输入商品名称" @keyup.enter.native="goodsHandleSearch" />
            </el-col>
            <el-col :span="8">
              <div class="title">商品ID</div>
              <el-input v-model="goodsQuery.goodsId" placeholder="请输入商品ID" @keyup.enter.native="goodsHandleSearch" />
            </el-col>
            <el-col :span="8">
              <div class="title">企业名称</div>
              <el-input v-model="goodsQuery.ename" placeholder="请输入企业名称" @keyup.enter.native="goodsHandleSearch" />
            </el-col>
          </el-row>
        </div>
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">以岭品</div>
              <el-select v-model="goodsQuery.yilingGoodsFlag" placeholder="请选择">
                <el-option label="全部" :value="0"></el-option>
                <el-option label="以岭品" :value="1"></el-option>
                <el-option label="非以岭品" :value="2"></el-option>
              </el-select>
            </el-col>
            <el-col :span="8">
              <div class="title">上下架状态</div>
              <el-select v-model="goodsQuery.goodsStatus" placeholder="请选择">
                <el-option label="全部" :value="0"></el-option>
                <el-option label="上架" :value="1"></el-option>
                <el-option label="下架" :value="2"></el-option>
                <el-option label="待设置" :value="3"></el-option>
              </el-select>
            </el-col>
          </el-row>
        </div>
        <div class="search-box">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn
                :total="goodsTotal"
                @search="goodsHandleSearch"
                @reset="goodsHandleReset"
              />
            </el-col>
          </el-row>
        </div>
      </div>
      <div class="down-box clearfix">
        <div class="btn">
          <ylButton type="primary" plain :disabled="running || operationType == 1" @click="batchAddClick">添加当前页</ylButton>
        </div>
      </div>
      <div class="mar-t-10" style="padding-bottom: 10px;background: #FFFFFF;">
        <yl-table
          :stripe="true"
          :show-header="true"
          :list="goodsList"
          :total="goodsTotal"
          :page.sync="goodsQuery.page"
          :limit.sync="goodsQuery.limit"
          :loading="loading1"
          @getList="getList"
        >
          <el-table-column label="商品ID" align="center">
            <template slot-scope="{ row }">
              <div class="font-size-base">{{ row.goodsId }}</div>
            </template>
          </el-table-column>
          <el-table-column label="商品类型" min-width="80" align="center">
            <template slot-scope="{ row }">
              <div class="goods-desc">
                <div class="font-size-base">{{ row.goodsType | dictLabel(standardGoodsType) }}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="商品名称" min-width="250" align="center">
            <template slot-scope="{ row }">
              <div class="goods-detail">
                <el-tooltip class="item" effect="dark" popper-class="my-tooltip-view" :content="row.goodsName" placement="top-start">
                  <div class="font-size-base detail">{{ row.goodsName }}</div>
                </el-tooltip>
                <el-tooltip class="item" effect="dark" popper-class="my-tooltip-view" :content="row.sellSpecifications" placement="top-start">
                  <div class="font-size-base detail">{{ row.sellSpecifications }}</div>
                </el-tooltip>
                <el-tooltip class="item" effect="dark" popper-class="my-tooltip-view" :content="row.manufacturer" placement="top-start">
                  <div class="font-size-base detail">{{ row.manufacturer }}</div>
                </el-tooltip>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="企业名称" min-width="100" align="center">
            <template slot-scope="{ row }">
              <div class="font-size-base">{{ row.ename }}</div>
            </template>
          </el-table-column>
          <el-table-column label="销售单价" min-width="55" align="center">
            <template slot-scope="{ row }">
              <div class="font-size-base">¥{{ row.price }}</div>
            </template>
          </el-table-column>
          <el-table-column label="库存数量（单位）" min-width="80" align="center">
            <template slot-scope="{ row }">
              <div class="font-size-base">{{ row.goodsInventory }} {{ row.sellUnit }}</div>
            </template>
          </el-table-column>
          <el-table-column label="状态" min-width="55" align="center">
            <template slot-scope="{ row }">
              <div v-if="row.goodsStatus == 1" class="font-size-base up">上架</div>
              <div v-else-if="row.goodsStatus == 2" class="font-size-base">下架</div>
              <div v-else class="font-size-base">待设置</div>
            </template>
          </el-table-column>
          <el-table-column label="以岭品" min-width="55" align="center">
            <template slot-scope="{ row }">
              <div class="font-size-base">{{ row.yilingGoodsFlag == 1 ? '是' : '否' }}</div>
            </template>
          </el-table-column>
          <el-table-column label="操作" min-width="55" align="center">
            <template slot-scope="{ row }">
              <div>
                <yl-button class="view-btn" type="text" v-if="!row.isSelect" :disabled="running || operationType == 1" @click="addClick(row)">添加</yl-button>
                <yl-button class="view-btn" v-if="row.isSelect" disabled type="text">已添加</yl-button>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </yl-dialog>
</template>

<script>
import { strategyLimitEnterpriseGoodsList } from '@/subject/admin/api/b2b_api/marketing_manage'
import { standardGoodsType } from '@/subject/admin/utils/busi'

export default {
  name: 'AddPlatformGoodsDialog',
  components: {},
  computed: {
    // 商品类型
    standardGoodsType() {
      return standardGoodsType()
    }
  },
  data() {
    return {
      // 活动是否已开始：true-已开始 false-未开始
      running: false,
      operationType: 1,
      // 商家范围类型（1-全部商家；2-指定商家；）
      conditionSellerType: 1,
      addGoodsDialog: false,
      // 选择的商品
      selectGoodsList: [],
      // 搜索商品
      loading1: false,
      goodsQuery: {
        page: 1,
        limit: 10,
        yilingGoodsFlag: 0,
        goodsStatus: 0
      },
      goodsList: [],
      goodsTotal: 0
    };
  },
  mounted() {
  },
  methods: {
    init(selectGoodsList, running, operationType) {
      this.selectGoodsList = selectGoodsList
      this.running = running
      this.operationType = operationType
      this.addGoodsDialog = true;
      this.getList()
    },
    async getList() {
      this.loading1 = true
      let goodsQuery = this.goodsQuery
      let data = await strategyLimitEnterpriseGoodsList(
        goodsQuery.page,
        goodsQuery.limit,
        0,
        this.conditionSellerType,
        goodsQuery.goodsName,
        goodsQuery.goodsId,
        goodsQuery.ename,
        goodsQuery.yilingGoodsFlag,
        goodsQuery.goodsStatus
      );
      this.loading1 = false
      if (data) {
        data.records.forEach(item => {
          let hasIndex = this.selectGoodsList.findIndex(obj => {
            return obj.goodsId == item.goodsId;
          });
          if (hasIndex != -1) {
            item.isSelect = true;
          } else {
            item.isSelect = false;
          }
        });

        this.goodsList = data.records
        this.goodsTotal = data.total
      }
    },
    // 商品搜索
    goodsHandleSearch() {
      this.goodsQuery.page = 1
      this.getList()
    },
    goodsHandleReset() {
      this.goodsQuery = {
        page: 1,
        limit: 10,
        yilingGoodsFlag: 0,
        goodsStatus: 0
      }
    },
    async addClick(row) {
      let currentArr = this.selectGoodsList
      currentArr.push(row)
      this.selectGoodsList = currentArr

      let arr = this.goodsList;
      arr.forEach(item => {
        let hasIndex = currentArr.findIndex(obj => {
          return obj.goodsId == item.goodsId;
        });
        if (hasIndex != -1) {
          item.isSelect = true;
        } else {
          item.isSelect = false;
        }
      });
      this.goodsList = arr;
      this.$emit('addGoodsSave', [row])
    },
    // 批量添加商品
    async batchAddClick() {
      let currentArr = this.selectGoodsList
      let seleceList = []
      this.goodsList.forEach(item => {
        if (!item.isSelect) {
          item.isSelect = true
          currentArr.push(item)
          seleceList.push(item)
        }
      });
      this.selectGoodsList = currentArr
      this.$emit('addGoodsSave', seleceList)
    }
  }
};
</script>

<style lang="scss" >
.my-form-box{
  .el-form-item{
    .el-form-item__label{
      color: $font-title-color; 
    }
    label{
      font-weight: 400 !important;
    }
  }
  .my-form-item-right{
    label{
      font-weight: 400 !important;
    }
  }
}
.my-tooltip-view{
  max-width: 500px;
}
</style>
<style lang="scss" scoped>
@import './index.scss';
</style>
