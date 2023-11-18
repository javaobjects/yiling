<template>
  <div>
    <yl-dialog
      width="800px"
      title="选择商品"
      :visible.sync="showDialog"
      @confirm="confirm">
      <div class="pop-up">
        <div class="common-box">
          <div class="search-box">
            <el-row class="box">
              <el-col :span="8">
                <div class="title">商品名称</div>
                <el-input v-model="dialog.goodsName" placeholder="请输入商品名称" @keyup.enter.native="searchEnter"/>
              </el-col>
              <el-col :span="8">
                <div class="title">批准文号/生产许可证号</div>
                <el-input v-model="dialog.licenseNo" placeholder="请输入批准文号/生产许可证号" @keyup.enter.native="searchEnter"/>
              </el-col>
              <el-col :span="8">
                <div class="title">生产厂家</div>
                <el-input v-model="dialog.manufacturer" placeholder="请输入生产厂家" @keyup.enter.native="searchEnter"/>
              </el-col>
            </el-row>
          </div>
          <div class="search-box mar-t-16">
            <el-row class="box">
              <el-col :span="16">
                <yl-search-btn
                  :total="dialog.total"
                  @search="handleSearch"
                  @reset="handleReset"/>
              </el-col>
            </el-row>
          </div>
        </div>
        <div class="table-conter">
          <yl-table
            ref="singTable"
            border
            show-header
            :list="dialogData"
            :total="dialog.total"
            :page.sync="dialog.page"
            :limit.sync="dialog.limit"
            :loading="loading"
            @getList="getList"
            :selection-change="handleSelectionChange">
            <el-table-column type="selection" width="70" :selectable="selected">
            </el-table-column>
            <el-table-column align="center" min-width="70" label="ID" prop="id"></el-table-column>
            <el-table-column align="center" min-width="250" label="商品信息" >
              <template slot-scope="{ row }">
                  <div class="products-box">
                    <el-image class="my-image" :src="row.pic" fit="contain"/>
                    <div class="products-info">
                      <div>商品：{{ row.id }}</div>
                      <div>{{ row.name }}</div>
                      <div>{{ row.sellSpecifications }}</div>
                      <div>{{ row.licenseNo }}</div>
                      <div>{{ row.manufacturer }}</div>
                    </div>
                  </div>
                </template>
            </el-table-column>
            <el-table-column align="center" min-width="90" label="商品类型">
              <template slot-scope="{ row }">
                {{ row.goodsType | dictLabel(standardGoodsTypeData) }}
              </template>
            </el-table-column>
            <el-table-column align="center" min-width="70" label="价格" prop="price"></el-table-column>
          </yl-table>
        </div>
      </div>
    </yl-dialog>
  </div>
</template>
<script>
import { b2bList } from '@/subject/pop/api/b2b_api/floor_manage'
import { standardGoodsType } from '@/subject/pop/utils/busi'
export default {
  props: {
    // 是否显示 弹窗
    show: {
      type: Boolean,
      default: true
    },
    // 从父级传递过来的 数据
    dataList: {
      type: Array,
      default: () => []
    }
  },
  computed: {
    showDialog: {
      get() {
        return this.show
      },
      set(val) {
        this.$emit('update:show', val)
      }
    },
    // 商品类型
    standardGoodsTypeData() {
      return standardGoodsType()
    }
  },
  data() {
    return {
      dialogData: [],
      loading: false,
      dialog: {
        //商品状态0 全部 1上架 2下架 3待设置
        goodsStatus: 1,
        goodsName: '',
        licenseNo: '',
        manufacturer: '',
        total: 0,
        page: 1,
        limit: 10
      },
      //勾选后的商品
      productData: []
    }
  },
  mounted() {
    this.getList();
  },
  methods: {
    // Enter
    searchEnter(e) {
      const keyCode = window.event ? e.keyCode : e.which;
      if (keyCode === 13) {
        this.getList()
      }
    },
    //判断是否勾选
    selected(row, index) {
      if (row.state == 1) {
        return false
      } else {
        return true
      }
    },
    async getList() {
      this.loading = true;
      let dialog = this.dialog;
      let data = await b2bList(
        dialog.goodsStatus,
        dialog.goodsName,
        dialog.licenseNo,
        dialog.manufacturer,
        dialog.page,
        dialog.limit
      )
      if (data) {
        this.dialogData = data.records
        this.dialog.total = data.total
      }
      this.$nextTick(() => {
       if (this.dataList && this.dataList.length > 0) {
          for (let l = 0; l < this.dialogData.length; l ++) {
            for (let y = 0; y < this.dataList.length; y ++) {
              if (this.dialogData[l].id == this.dataList[y].goodsId) {
                this.$refs.singTable.toggleRowSelectionMethod(this.dialogData[l], true)
                this.dialogData[l].state = 1
                break;
              }
            }
          }
        }
    
      })
      this.loading = false;
    },
    // 表格全选
    handleSelectionChange(val) {
      this.productData = val;
    },
    //点击确定
    confirm() {
      let productDataList = [];
      for (let i = 0; i < this.productData.length; i ++) {
        if (!this.productData[i].state && this.productData[i].state != 1) {
          productDataList.push(this.productData[i])
        }
      }
      this.$emit('confirmChange', productDataList)
    },
    // 搜索
    handleSearch() {
      this.dialog.page = 1;
      this.getList();
    },
    // 重置
    handleReset() {
      this.dialog = {
        goodsName: '',
        licenseNo: '',
        manufacturer: '',
        total: 0,
        page: 1,
        limit: 10
      }
    }
  }
}
</script>
<style lang="scss" scoped>
@import './index.scss';
</style>
