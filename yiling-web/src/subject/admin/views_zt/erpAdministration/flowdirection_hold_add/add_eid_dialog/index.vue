<template>
  <!-- 添加商品弹框 -->
  <yl-dialog title="添加/修改" :visible.sync="addGoodsDialog" width="980px" right-btn-name="保存" @close="closeClick" @confirm="confirmClick">
    <div class="dialog-content-box-customer">
      <div class="dialog-content-top">
        <div class="title-view">
          <div class="sign"></div>选择需添加商业
        </div>
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">商业ID</div>
              <el-input v-model="goodsQuery.eid" placeholder="请输入商业ID" />
            </el-col>
            <el-col :span="8">
              <div class="title">商业名称</div>
              <el-input v-model="goodsQuery.ename" placeholder="请输入商业名称" />
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
          <ylButton type="primary" plain @click="batchAddClick">批量添加</ylButton>
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
          <el-table-column label="企业ID" align="center">
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
          <el-table-column label="操作" min-width="55" align="center" fixed="right">
            <template slot-scope="{ row }">
              <div>
                <yl-button class="view-btn" v-if="!row.isSelect" type="text" @click="addClick(row)">添加</yl-button>
                <yl-button class="view-btn" v-if="row.isSelect" disabled type="text">已添加</yl-button>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
      <!-- 已添加商品 -->
      <div class="dialog-content-top">
        <div class="title-view">
          <div class="sign"></div>已选择商业
        </div>
      </div>
      <div class="down-box clearfix">
        <div class="btn">
          <ylButton type="primary" plain @click="batchRemoveClick">批量删除</ylButton>
        </div>
      </div>
      <div class="mar-t-10" style="padding-bottom: 10px;background: #FFFFFF;">
        <yl-table
          :stripe="true"
          :show-header="true"
          :list="bottomGoodsList"
          :total="0"
        >
          <el-table-column label="企业ID" align="center">
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
          <el-table-column label="操作" min-width="55" align="center" fixed="right">
            <template slot-scope="{ row, $index }">
              <div>
                <yl-button class="view-btn" type="text" @click="removeClick(row, $index)">删除</yl-button>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </yl-dialog>
</template>

<script>
import { flowSealedEnterprisePage } from '@/subject/admin/api/zt_api/erpAdministration'

export default {
  name: 'AddEidDialog',
  components: {},
  computed: {
  },
  data() {
    return {
      addGoodsDialog: false,
      loading1: false,
      goodsQuery: {
        page: 1,
        limit: 10
      },
      goodsList: [],
      goodsTotal: 0,
      bottomGoodsList: []
    };
  },
  mounted() {
  },
  methods: {
    init(selectEidList) {
      console.log('init:', selectEidList)
      this.goodsList = []
      this.bottomGoodsList = selectEidList || []
      this.addGoodsDialog = true
      this.getList()
    },
    closeClick() {
      this.goodsList = []
      this.bottomGoodsList = []
    },
    async getList() {
      this.loading1 = true
      let goodsQuery = this.goodsQuery
      let data = await flowSealedEnterprisePage(
        goodsQuery.page,
        goodsQuery.limit,
        parseInt(goodsQuery.eid),
        goodsQuery.ename
      );
      this.loading1 = false
      if (data) {
        
        let currentArr = this.bottomGoodsList
        if (currentArr.length > 0) {
          data.records.forEach(item => {
            let hasIndex = currentArr.findIndex(obj => {
              return obj.eid == item.eid;
            });
            if (hasIndex != -1) {
              item.isSelect = true;
            } else {
              item.isSelect = false;
            }
          });
        }
        this.goodsList = data.records
        this.goodsTotal = data.total
      }
    },
    // 保存点击
    async confirmClick() {
      this.addGoodsDialog = false;
      this.$emit('sendSuccess', this.$common.clone(this.bottomGoodsList))
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
    addClick(row) {

      let currentArr = this.bottomGoodsList
      currentArr.push(this.$common.clone(row))
      this.bottomGoodsList = currentArr

      let arr = this.goodsList;
      arr.forEach(item => {
        let hasIndex = currentArr.findIndex(obj => {
          return obj.eid == item.eid;
        });
        if (hasIndex != -1) {
          item.isSelect = true;
        } else {
          item.isSelect = false;
        }
      });
      this.goodsList = this.$common.clone(arr);

    },
    // 批量添加商品
    async batchAddClick() {

      let currentArr = this.bottomGoodsList;
      this.goodsList.forEach(item => {
        if (!item.isSelect) { // 没有添加
          currentArr.push(this.$common.clone(item));
        }
      });
      this.bottomGoodsList = currentArr

      let arr = this.goodsList;
      arr.forEach(item => {
        let hasIndex = currentArr.findIndex(obj => {
          return obj.eid == item.eid;
        });
        if (hasIndex != -1) {
          item.isSelect = true;
        } else {
          item.isSelect = false;
        }
      });
      this.goodsList = this.$common.clone(arr);

    },
    // 商品搜索
    bottomGoodsHandleSearch() {
      this.bottomGoodsQuery.page = 1
      this.getBottomList()
    },
    bottomGoodsHandleReset() {
      this.bottomGoodsQuery = {
        page: 1,
        limit: 10,
        etype: 0,
        regionCode: ''
      }
    },
    async removeClick(row, index) {
      let currentArr = this.bottomGoodsList;
      currentArr.splice(index, 1);
      this.bottomGoodsList = currentArr;

      let arr = this.goodsList;
      arr.forEach(item => {
        let hasIndex = currentArr.findIndex(obj => {
          return obj.eid == item.eid;
        });
        if (hasIndex != -1) {
          item.isSelect = true;
        } else {
          item.isSelect = false;
        }
      });
      this.goodsList = this.$common.clone(arr);

    },
    // 批量添加商品
    batchRemoveClick() {
      this.bottomGoodsList = []
      this.getList()
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
