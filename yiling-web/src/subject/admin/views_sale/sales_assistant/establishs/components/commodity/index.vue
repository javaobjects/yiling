<template>
  <yl-dialog title="添加商品" @confirm="confirm" width="890px" :visible.sync="addItemShow" :show-footer="true">
    <div class="dialog-content-box">
      <!-- 顶部搜索 -->
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">商品名称</div>
              <el-input v-model="query.goodsName" placeholder="请输入商品名称" />
            </el-col>
            <el-col :span="8">
              <div class="title">商品ID</div>
              <el-input v-model="query.goodsId" placeholder="请输入商品ID" />
            </el-col>
            <el-col :span="8">
              <div class="title">生产厂家</div>
              <el-input v-model="query.manufacturer" placeholder="请输入生产厂商名称" />
            </el-col>
          </el-row>
        </div>
        <div class="mar-t-16">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn
                :total="query.total"
                @search="handleSearch"
                @reset="handleReset"
              />
            </el-col>
          </el-row>
        </div>
      </div>
      <!-- 中间商品个数 -->
      <div class="commodityNum">
        <!-- 已添加<span>1</span>个商品 -->
      </div>
      <!-- 下部 表格 -->
      <!-- 底部列表 -->
      <div class="bottom-content-view">
        <yl-table
          :list="dataList"
          :total="query.total"
          :cell-class-name="getCellClass"
          :show-header="true"
          :page.sync="query.page"
          :limit.sync="query.limit"
          :loading="loading"
          :cell-no-pad="true"
          @getList="getList"
          >
            <el-table-column align="center" label="商品ID" min-width="80" prop="goodsId">
            </el-table-column>
            <el-table-column align="center" label="商品名称" min-width="150" prop="goodsName">
            </el-table-column>
            <el-table-column align="center" label="商品信息" min-width="400">
              <template slot-scope="{ row }">
                <div class="content">
                  <div class="content-left">
                    <img class="content-left-img" :src="row.goodsPic" alt="">
                  </div>
                  <div class="content-right">
                    <p> {{ row.sellSpecifications }} </p>
                    <p>{{ row.licenseNo }}</p>
                    <p>{{ row.manufacturer }}</p>
                  </div>
                </div>
              </template>
            </el-table-column>
            <el-table-column align="center" label="商品基价" min-width="80" prop="price"></el-table-column>
            <el-table-column align="center" label="是否添加" min-width="80">
              <template slot-scope="{ row }">
                <span class="add-type" :style="{color: row.zt == 1 ? '#1790ff' : '#c8c9cc'}" @click="addType(row)">
                  {{ row.zt == 1 ? '添加' : '已添加' }}
                </span>
              </template>
            </el-table-column>
        </yl-table>
      </div>
      <!-- 已添加的商品 -->
      <div class="bottom-content-view" style="background: #FFFFFF;" v-if="dataList2.length>0">
        <div class="flex-row-left item">
          <div class="line-view"></div>
          <span class="font-size-lg bold">已添加的商品</span>
        </div>
        <yl-table
          :list="dataList2"
          :cell-class-name="getCellClass"
          :show-header="true"
          :cell-no-pad="true"
          >
            <el-table-column align="center" label="商品ID" min-width="80" prop="goodsId">
            </el-table-column>
            <el-table-column align="center" label="商品名称" min-width="150" prop="goodsName">
            </el-table-column>
            <el-table-column align="center" label="商品信息" min-width="400">
              <template slot-scope="{ row }">
                <div class="content">
                  <div class="content-left">
                    <img class="content-left-img" :src="row.goodsPic" alt="">
                  </div>
                  <div class="content-right">
                    <p> {{ row.sellSpecifications }} </p>
                    <p>{{ row.licenseNo }}</p>
                    <p>{{ row.manufacturer }}</p>
                  </div>
                </div>
              </template>
            </el-table-column>
            <el-table-column align="center" label="商品基价" min-width="80" prop="price"></el-table-column>
            <el-table-column align="center" label="操作" min-width="80">
              <template slot-scope="{ row, $index }">
                <span v-if="row.taskStatus != 1" class="add-type" style="color:#1790ff" @click="deleteClick(row, $index)">删除</span>
              </template>
            </el-table-column>
        </yl-table>
      </div>
    </div>
  </yl-dialog>
</template>
<script>
import { queryGoodsForAdd, deleteGoods } from '@/subject/admin/api/views_sale/task_administration.js'
export default {
  props: {
    show: {
      type: Boolean,
      default: true
    },
    dataCommodity: {
      type: Array,
      default: ()=> []
    }
  },
  computed: {
    addItemShow: {
      get() {
        return this.show
      },
      set(val) {
        this.$emit('update:show', val)
      }
    }
  },
  data() {
    return {
      query: {
        taskType: 0,
        goodsName: '',
        goodsId: '',
        manufacturer: '',
        total: 0,
        page: 1,
        limit: 10
      },
      dataList: [],
      loading: false,
      // 已添加 的商品
      dataList2: []
    }
  },
  watch: {
    dataCommodity: {
      handler(newVal, oldVal) {
        this.dataList2 = newVal;
      },
      deep: true,
      immediate: true
    }
  },
  mounted() {
    this.getList(); //获取商品
  },
  methods: {
    // 数据
    async getList() {
      let query = this.query;
      this.dataList = [];
      this.loading = true;
      let data = await queryGoodsForAdd(
        query.taskType,
        query.page,
        query.goodsId,
        query.goodsName,
        query.manufacturer,
        query.limit
      )
      if (data !== undefined) {
        for (let i = 0; i< data.records.length; i++) {
          this.dataList.push({
            ...data.records[i],
            zt: 1
          })
        }
        if (this.dataCommodity && this.dataCommodity.length > 0) {
          for (let l = 0; l < this.dataList.length; l++) {
            for (let y = 0; y < this.dataCommodity.length; y++) {
              if (this.dataList[l].goodsId == this.dataCommodity[y].goodsId) {
                this.dataList[l].zt = 2
                break;
              } else {
                this.dataList[l].zt = 1
              }
            }
          }
        }
        this.query.total = data.total;
      }
      this.loading = false;
    },
    // 点击搜索
    handleSearch() {
      this.query.page = 1;
      this.getList();
    },
    // 点击添加
    addType(row) {
      if (row.zt == 1) {
        row.commission = '';
        this.dataList2.push(row)
        // this.$emit('addCommodity',row)
      }
      row.zt = 2;
    },
    // 点击删除
    async deleteClick(row,index) {
      if (row.taskGoodsId) {
        this.$common.showLoad();
        let data = await deleteGoods(row.taskGoodsId)
        this.$common.hideLoad();
        if (data !== undefined) {
          this.$common.n_success('移除成功!');
        }
      }
      this.dataList2.splice(index,1);
        for (let l = 0; l < this.dataList.length; l++) {
          if (this.dataCommodity && this.dataCommodity.length > 0) {
            for (let y = 0; y < this.dataCommodity.length; y++) {
              if (this.dataList[l].goodsId == this.dataCommodity[y].goodsId) {
                this.dataList[l].zt = 2
                break;
              } else {
                this.dataList[l].zt = 1
              }
            }
          } else {
            this.dataList[l].zt = 1;
          }

        }
      // }
    },
    // 清空查询条件
    handleReset() {
      this.query = {
        taskType: 0,
        goodsName: '',
        goodsId: '',
        manufacturer: '',
        total: 0,
        page: 1,
        limit: 10
      }
    },
    getCellClass({row,rowIndex}) {
      return 'border-1px-b'
    },
    // 点击确定
    confirm() {
      if (this.dataList2 && this.dataList2.length > 0 ) {
        this.$emit('addCommodity', this.dataList2)
      } else {
        this.$common.error('请添加 商品！')
      }

    }
  }
}
</script>
<style lang="scss" scoped>
@import "./index.scss";
</style>