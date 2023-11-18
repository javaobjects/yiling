<template>
  <yl-dialog
    width="60%"
    title="选择商品"
    @confirm="chooseProDone"
    :visible.sync="showDialog">
    <div class="dialog-content choose-pro-view">
      <el-form label-width="72px" label-position="top">
        <el-row>
          <el-col :span="10">
            <el-form-item label="商品名称">
              <el-input v-model="query.name" @keyup.enter.native="searchEnter" placeholder="请输入商品名称" />
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="批准文号">
              <el-input v-model="query.licenseNo" @keyup.enter.native="searchEnter" placeholder="请输入批准文号" />
            </el-form-item>
          </el-col>
        </el-row>
        <div class="mar-tb-10 pad-tb-10">
          <yl-search-btn
            :total="query.total"
            @search="handleSearch"
            @reset="handleReset"
          />
        </div>
      </el-form>
      <yl-table
        border
        :show-header="true"
        :list="dataList"
        :total="query.total"
        :page.sync="query.page"
        :limit.sync="query.limit"
        :loading="loading"
        @getList="getChooseProList()">
        <el-table-column align="center" label="商品名称" prop="name">
        </el-table-column>
        <el-table-column align="left" label="商品信息">
          <template slot-scope="{ row }">
            <div class="font-title-color font-size-base">
              <div><span class="font-light-color">规格：</span>{{ row.sellSpecifications }}</div>
              <div><span class="font-light-color">批准文号：</span>{{ row.licenseNo }}</div>
              <div><span class="font-light-color">生产厂家：</span>{{ row.manufacturer }}</div>
            </div>
          </template>
        </el-table-column>
        <el-table-column align="center" width="120" label="销售价格" prop="price">
          <template slot-scope="{ row }">
            <span>￥{{ row.price }}</span>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="操作"
          width="100">
          <template slot-scope="{ row, $index }">
            <yl-button type="text" :disabled="row.choose" @click="choose(row, $index)">{{ row.choose ? '已选择' : '选择' }}</yl-button>
          </template>
        </el-table-column>
      </yl-table>
    </div>
  </yl-dialog>
</template>

<script>
  import { getChooseList } from '@/subject/admin/api/pop'
  export default {
    name: 'ChooseProduct',
    props: {
      // id参数，用于业务排除产品
      id: {
        type: Number,
        default: null
      },
      // id参数，用于业务排除产品
      categoryId: {
        type: Number,
        default: null
      },
      // 已选数量
      length: {
        type: Number,
        default: 0
      },
      // 已选数组
      selectList: {
        type: Array,
        default: () => []
      },
      // 可选最大数量
      maxlength: {
        type: Number,
        default: 10
      }
    },
    data() {
      return {
        query: {
          page: 1,
          limit: 10,
          total: 0
        },
        // 可选商品列表
        dataList: [],
        // 可选商品加载
        loading: false,
        chooseList: [],
        showDialog: false
      }
    },
    methods: {
      // Enter
      searchEnter(e) {
        const keyCode = window.event ? e.keyCode : e.which;
        if (keyCode === 13) {
          this.getChooseProList()
        }
      },
      // 获取可选商品列表
      async getChooseProList(callback) {
        this.chooseList = []
        let query = this.query
        if (query.page === 1) {
          this.$common.showLoad()
        } else {
          this.loading = true
        }
        let data = await getChooseList(
          this.id || undefined,
          this.categoryId || undefined,
          query.name,
          query.licenseNo,
          query.page,
          query.limit
        )
        if (query.page === 1) {
          this.$common.hideLoad()
        } else {
          this.loading = false
        }
        if (data && data.records) {

          data.records.forEach(item => {
            let hasIndex = this.selectList.findIndex(obj => {
              return (item.id == obj.id) || (item.id == obj.goodsId);
            });
            if (hasIndex != -1) {
              item.choose = true;
            } else {
              item.choose = false;
            }
          });

          this.dataList = data.records
          this.query.total = data.total
          if (callback) callback()
        }
      },
      // 打开选择商品
      chooseProduct() {
        this.getChooseProList(() => {
          this.showDialog = true
        })
      },
      closeProduct() {
        this.showDialog = false
      },
      // 商品中点击选择
      choose(row, index) {
        this.$log(row.id)
        if (this.chooseList.length >= (this.maxlength - this.length)) {
          this.$common.warn('已超过最大数量，不可继续选择')
          return
        }
        this.chooseList.push({
          id: row.id,
          goodsId: row.id,
          pic: row.pic,
          name: row.name,
          licenseNo: row.licenseNo,
          manufacturer: row.manufacturer,
          sellSpecifications: row.sellSpecifications,
          price: row.price,
          sort: 1,
          // 新增修改
          type: 1
        })
        this.dataList[index].choose = true
      },
      // 商品中选择完毕
      chooseProDone() {
        this.$emit('choose', this.chooseList)
        this.showDialog = false
      },
      // 商品搜索
      handleSearch() {
        this.query.page = 1
        this.getChooseProList()
      },
      handleReset() {
        this.query = {
          page: 1,
          limit: 10
        }
      }
    }
  }
</script>

<style lang="scss" scoped>
  .choose-pro-view {
    max-height: 500px;
    overflow-y: scroll;
    .el-input {
      width: 260px;
    }
  }
</style>
