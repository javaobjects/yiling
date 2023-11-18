<template>
  <!-- 选择需添加的会员方案 -->
  <yl-dialog 
    title="设置" 
    :visible.sync="addGoodsDialog" 
    width="900px" 
    :show-footer="false">
    <div class="dialog-content-box-customer">
      <div v-if="type != 2">
        <div class="dialog-content-top">
          <div class="title-view">
            <div class="sign"></div>选择需添加的会员方案
          </div>
        </div>
        <div class="mar-t-10" style="padding-bottom: 10px;background: #FFFFFF;">
          <yl-table
            :stripe="true"
            :show-header="true"
            :list="goodsList"
            :total="goodsQuery.total"
            :loading="loading1"
            @getList="getList"
          >
            <el-table-column label="会员名称" align="center">
              <template slot-scope="{ row }">
                <div class="font-size-base">{{ row.name }}</div>
              </template>
            </el-table-column>
            <el-table-column label="操作" min-width="55" align="center">
              <template slot-scope="{ row }">
                <div>
                  <yl-button class="view-btn" type="text" @click="addClick(row)">添加</yl-button>
                </div>
              </template>
            </el-table-column>
          </yl-table>
        </div>
      </div>
      <!-- 已添加会员方案 -->
      <div class="dialog-content-top">
        <div class="title-view">
          <div class="sign"></div>已添加会员方案
        </div>
      </div>
      <div class="mar-t-10" style="padding-bottom: 10px;background: #FFFFFF">
        <yl-table
          :stripe="true"
          :show-header="true"
          :list="bottomGoodsList"
          :total="bottomGoodsQuery.total"
          :loading="loading2"
          @getList="getBottomList"
        >
          <el-table-column label="会员名称" align="center">
            <template slot-scope="{ row }">
              <div class="font-size-base">{{ row.memberName }}</div>
            </template>
          </el-table-column>
          <el-table-column label="操作" min-width="55" align="center" v-if="type != 2">
            <template slot-scope="{ row }">
              <div>
                <yl-button class="view-btn" type="text" @click="removeClick(row)">删除</yl-button>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </yl-dialog>
</template>

<script>
import { giveMemberPage, giveMemberAdd, giveMemberPageList, giveMemberDelete } from '@/subject/admin/api/b2b_api/integral'
export default {
  components: {},
  computed: {
  },
  data() {
    return {
      addGoodsDialog: false,
      // 搜索商品
      loading1: false,
      goodsQuery: {
        total: 0,
        page: 1,
        limit: 10
      },
      goodsList: [],
      loading2: false,
      bottomGoodsQuery: {
        total: 0,
        page: 1,
        limit: 10
      },
      bottomGoodsList: [],
      //传递过来的规则id
      ruleId: '',
      //1 创建 2查看 3编辑
      type: 1
    };
  },
  mounted() {},
  methods: {
    init(ruleId, show, type) {
      this.ruleId = ruleId;
      this.addGoodsDialog = show;
      //1 创建 2查看 3编辑
      this.type = type;
      this.getList()
      this.getBottomList()
    },
    async getList() {
      this.loading1 = true
      let query = this.goodsQuery
      let data = await giveMemberPage(
        query.page,
        query.limit
      );
      if (data) {
        this.goodsList = data.records
        this.goodsQuery.total = data.total
      }
      this.loading1 = false
    },
    async getBottomList() {
      this.loading2 = true
      let query = this.bottomGoodsQuery
      let data = await giveMemberPageList(
        this.ruleId,
        query.page,
        query.limit
      );
      if (data) {
        this.bottomGoodsList = data.records
        this.bottomGoodsQuery.total = data.total
        this.$emit('memberChange', data.total)
      }
      this.loading2 = false
    },
    // 商品搜索
    goodsHandleSearch() {
      this.goodsQuery.page = 1
      this.getList()
    },
    goodsHandleReset() {
      this.goodsQuery = {
        page: 1,
        limit: 10
      }
    },
    async addClick(row) {
      this.$common.showLoad();
      let data = await giveMemberAdd(
        this.ruleId, 
        row.id
      )
      this.$common.hideLoad();
      if (data !== undefined) {
        this.$common.n_success('添加成功');
        this.getBottomList()
      } 
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
    },
    async removeClick(row) {
      this.$common.showLoad();
      let data = await giveMemberDelete(
        this.ruleId, 
        row.memberId
      )
      this.$common.hideLoad();
      if (data !== undefined) {
        this.$common.n_success('删除成功');
        // 如果当前页数据 全部删除，需要回前一页
        if (this.bottomGoodsQuery.page > 1 && this.bottomGoodsList.length < 2) {
          this.bottomGoodsQuery.page = this.bottomGoodsQuery.page - 1 || 1
        }
        this.getBottomList()
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
