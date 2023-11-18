<template>
  <!-- 添加参与规则 -->
  <yl-dialog title="添加参与规则" :visible.sync="addGoodsDialog" width="966px" :show-footer="true" @confirm="saveConfirm">
    <div class="dialog-content-box-customer">
      <!-- 已添加商品 -->
      <div class="dialog-content-top">
        <div class="search-box mar-t-10">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">适用平台</div>
              <el-select v-model="query.usePlatform" placeholder="请选择">
                <el-option label="全部" :value="0"></el-option>
                <el-option
                  v-for="item in usePlatformArray"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-col>
          </el-row>
        </div>
        <div class="search-box">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn
                :total="total"
                @search="bottomGoodsHandleSearch"
                @reset="bottomGoodsHandleReset"
              />
            </el-col>
          </el-row>
        </div>
      </div>
      <div class="mar-t-10" style="padding-bottom: 10px;background: #FFFFFF;">
        <yl-table
          ref="multipleTable"
          :stripe="true"
          :show-header="true"
          :list="dataList"
          :total="total"
          :page.sync="query.page"
          :limit.sync="query.limit"
          :loading="loading"
          :selection-change="handleSelectionChange"
          row-key="id"
          @getList="getBottomList"
        >
          <el-table-column type="selection" :reserve-selection="true" align="center" width="70"></el-table-column>
          <el-table-column label="参与规则ID" min-width="80" align="center">
            <template slot-scope="{ row }">
              <div class="font-size-base">{{ row.id }}</div>
            </template>
          </el-table-column>
          <el-table-column label="适用平台" min-width="80" align="center">
            <template slot-scope="{ row }">
              <div class="font-size-base">{{ row.usePlatform | dictLabel(usePlatformArray) }}</div>
            </template>
          </el-table-column>
          <el-table-column label="参与规则" min-width="162" align="center">
            <template slot-scope="{ row }">
              <div class="goods-desc">
                <div class="font-size-base">{{ row.joinRule }}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="说明" min-width="200" align="center">
            <template slot-scope="{ row }">
              <div class="font-size-base">{{ row.instruction }}</div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </yl-dialog>
</template>

<script>
import { lotteryActivityQueryRulePage } from '@/subject/admin/api/view_marketing/lottery_activity'

// 选择赠品
export default {
  name: 'SelectGiftDialog',
  components: {},
  computed: {
  },
  data() {
    return {
      usePlatformArray: [
        {
          label: 'B端',
          value: 1
        },
        {
          label: 'C端',
          value: 2
        }
      ],
      addGoodsDialog: false,
      loading: false,
      type: 1,
      query: {
        page: 1,
        limit: 10,
        usePlatform: 0
      },
      dataList: [],
      total: 0,
      // 当前界面勾选
      multipleSelection: [],
      // 已经选择
      selectList: []
    };
  },
  mounted() {
  },
  methods: {
    init(selectList) {
      this.multipleSelection = []
      this.selectList = selectList
      this.addGoodsDialog = true;
      this.getBottomList()
    },
    async getBottomList() {
      let query = this.query
      this.loading = true
      let data = await lotteryActivityQueryRulePage(
          query.page,
          query.limit,
          query.usePlatform
        )
      this.loading = false
      if (data) {
        this.dataList = data.records
        this.total = data.total
      }
    },
    // 搜索
    bottomGoodsHandleSearch() {
      this.query.page = 1
      this.getBottomList()
    },
    bottomGoodsHandleReset() {
      this.query = {
        page: 1,
        limit: 10,
        usePlatform: 0
      }
    },
    // 表格全选
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    getRowKeys(row) {
      return row.id
    },
    // 保存点击
    saveConfirm() {
      this.$log(this.multipleSelection)
      if (this.multipleSelection && this.multipleSelection.length > 0) {
        this.addGoodsDialog = false
        this.$emit('save', this.multipleSelection)
      } else {
        this.$common.warn('请选择参与规则')
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
