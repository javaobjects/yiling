<template>
  <!-- 添加商品弹框 -->
  <yl-dialog title="查看供应商" :visible.sync="addGoodsDialog" width="966px" :show-footer="false">
    <div class="dialog-content-box-customer">
      <!-- 已添加商品 -->
      <div class="dialog-content-top">
        <div class="title-view">
          <div class="sign"></div>已添加供应商
        </div>
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">企业ID</div>
              <el-input v-model="bottomGoodsQuery.eid" placeholder="请输入企业ID" @keyup.enter.native="bottomGoodsHandleSearch" />
            </el-col>
            <el-col :span="8">
              <div class="title">企业名称</div>
              <el-input v-model="bottomGoodsQuery.ename" placeholder="请输入企业名称" @keyup.enter.native="bottomGoodsHandleSearch" />
            </el-col>
          </el-row>
        </div>
        <div class="search-box">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn
                :total="bottomGoodsTotal"
                @search="bottomGoodsHandleSearch"
                @reset="bottomGoodsHandleReset"
              />
            </el-col>
          </el-row>
        </div>
      </div>
      <div class="mar-t-10" style="padding-bottom: 10px;background: #FFFFFF;">
        <yl-table
          :stripe="true"
          :show-header="true"
          :list="bottomGoodsList"
          :total="bottomGoodsTotal"
          :page.sync="bottomGoodsQuery.page"
          :limit.sync="bottomGoodsQuery.limit"
          :loading="loading2"
          @getList="getBottomList"
        >
          <el-table-column label="企业ID" min-width="55" align="center">
            <template slot-scope="{ row }">
              <div class="font-size-base">{{ row.eid }}</div>
            </template>
          </el-table-column>
          <el-table-column label="企业名称" min-width="162" align="center">
            <template slot-scope="{ row }">
              <div class="goods-desc">
                <div class="font-size-base">{{ row.ename }}</div>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </yl-dialog>
</template>

<script>
import { queryEnterpriseLimitListPage } from '@/subject/admin/api/b2b_api/discount_coupon'

export default {
  name: 'CheckProviderDialog',
  components: {},
  computed: {
  },
  data() {
    return {
      couponActivityId: '',
      addGoodsDialog: false,
      loading2: false,
      bottomGoodsQuery: {
        page: 1,
        limit: 10
      },
      bottomGoodsList: [],
      bottomGoodsTotal: 0
    };
  },
  mounted() {
  },
  methods: {
    init(couponActivityId) {
      this.couponActivityId = couponActivityId
      this.addGoodsDialog = true;
      this.getBottomList()
    },
    async getBottomList() {
      this.loading2 = true
      let bottomGoodsQuery = this.bottomGoodsQuery
      let data = await queryEnterpriseLimitListPage(
        bottomGoodsQuery.page,
        bottomGoodsQuery.limit,
        this.couponActivityId,
        Number(bottomGoodsQuery.eid),
        bottomGoodsQuery.ename
      );
      this.loading2 = false
      if (data) {
        this.bottomGoodsList = data.records
        this.bottomGoodsTotal = data.total
      }
    },
    // 保存点击
    confirmClick() {
      this.addGoodsDialog = false;
    },
  
    // 商品搜索
    bottomGoodsHandleSearch() {
      this.bottomGoodsQuery.page = 1
      this.getBottomList()
    },
    bottomGoodsHandleReset() {
      this.bottomGoodsQuery = {
        page: 1,
        limit: 10
      }
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
</style>
<style lang="scss" scoped>
@import './index.scss';
</style>
