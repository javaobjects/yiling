<template>
  <yl-dialog width="60%" title="添加商品" @confirm="chooseProDone" :visible.sync="showDialog">
    <div class="dialog-content choose-pro-view">
      <el-form :model="query" label-width="72px" label-position="top">
        <el-row>
          <el-col :span="8">
            <el-form-item label="商品名称" prop="name">
              <el-input v-model="query.name" @keyup.enter.native="searchEnter" placeholder="请输入商品名称" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="批准文号" prop="licenseNo">
              <el-input v-model="query.licenseNo" @keyup.enter.native="searchEnter" placeholder="请输入批准文号" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="生产厂家" prop="manufacturer">
              <el-input v-model="query.manufacturer" @keyup.enter.native="searchEnter" placeholder="请输入生产厂家" />
            </el-form-item>
          </el-col>
        </el-row>
        <div class="search-box">
          <yl-search-btn :total="query.total" @search="handleSearch" @reset="handleReset" />
        </div>
      </el-form>
      <yl-table
        :show-header="true"
        :list="dataList"
        :total="query.total"
        :page.sync="query.current"
        :limit.sync="query.size"
        :loading="loading"
        :stripe="true"
        @getList="getChooseProList()"
      >
        <el-table-column align="center" label="商品名称" prop="name"></el-table-column>
        <el-table-column align="center" label="批准文号" prop="licenseNo">
          <template slot-scope="{ row }">
            <span>{{ row.licenseNo ? row.licenseNo : "- -" }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="规格" prop="sellSpecifications">
          <template slot-scope="{ row }">
            <span>{{ row.sellSpecifications ? row.sellSpecifications : "- -" }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="生产厂家" width="280" prop="manufacturer"></el-table-column>
        <el-table-column align="center" label="操作" width="100">
          <template slot-scope="{ row, $index }">
            <yl-button type="text" :disabled="row.overSoldType === 1 ? true : false" @click="choose(row, $index)">
              {{ row.overSoldType ? "已选择" : "选择" }}
            </yl-button>
          </template>
        </el-table-column>
      </yl-table>
    </div>
  </yl-dialog>
</template>

<script>
import { getOverSoldPopList, updateOverSold } from '@/subject/pop/api/products'
export default {
  name: 'ChooseProduct',
  data() {
    return {
      query: {
        current: 1,
        size: 10,
        name: '',
        licenseNo: '',
        manufacturer: '',
        total: 0
      },
      //  可选商品列表
      dataList: [],
      loading: false,
      //  选择商品id集合列表
      chooseList: [],
      showDialog: false,
      //  是否超卖 0-非超卖 1-超卖
      overSoldType: 1
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
    //  获取可选商品列表
    async getChooseProList() {
      this.loading = true
      let query = this.query
      let data = await getOverSoldPopList(
        query.current,
        query.size,
        query.name,
        query.licenseNo,
        query.manufacturer
      )
      this.loading = false
      if (data && data.records) {
        //  已选的商品状态修改
        if (this.chooseList.length > 0) {
          data.records.forEach(item => {
            let hasIndex = this.chooseList.findIndex(id => {
              return id == item.id
            })
            if (hasIndex != -1 ) {
              item.overSoldType = 1
            }
          })
        }
        this.dataList = data.records
        this.query.total = data.total
      }
    },
    //  打开选择商品
    chooseProduct() {
      this.showDialog = true
      this.query = {
        current: 1,
        size: 10,
        name: '',
        licenseNo: '',
        manufacturer: '',
        total: 0
      }
      this.chooseList = []
      this.getChooseProList()
    },
    //  商品中点击选择
    choose(row, index) {
      this.chooseList.push(row.id)
      this.dataList[index].overSoldType = 1
    },
    //  商品中选择完毕
    async chooseProDone() {
      if (this.chooseList.length > 0) {
        let data = await updateOverSold(this.chooseList, this.overSoldType)
        if (data && data.result == true) {
          this.showDialog = false
          this.$emit('refreshList')
        }
      } else {
        this.showDialog = false
      }
    },
    //  商品搜索
    handleSearch() {
      this.query.current = 1
      this.getChooseProList()
    },
    //  重置搜索输入
    handleReset() {
      this.query = {
        current: 1,
        size: 10,
        name: '',
        licenseNo: '',
        manufacturer: ''
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
