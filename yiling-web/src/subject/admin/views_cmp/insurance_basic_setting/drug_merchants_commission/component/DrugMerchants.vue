<template>
  <yl-dialog width="60%" title="选择商家" :show-footer="false" :visible.sync="showDialog">
    <div class="dialog-content choose-pro-view">
      <el-form :model="query" label-width="72px" label-position="top">
        <el-row>
          <el-col :span="8">
            <el-form-item label="商家名称" prop="ename">
              <el-input v-model="query.ename" placeholder="请输入商家名称" @keyup.enter.native="handleSearch"/>
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
        @getList="getEnterpriseAccountList()"
      >
        <el-table-column align="center" label="商家名称" prop="name"></el-table-column>
        <el-table-column align="center" label="许可证号" prop="licenseNumber"></el-table-column>
        <el-table-column align="center" label="操作">
          <template slot-scope="{ row, $index }">
            <yl-button type="text" :disabled="row.chose" @click="choose(row, $index)"> {{ row.chose ? "已选择" : "选择" }} </yl-button>
          </template>
        </el-table-column>
      </yl-table>
    </div>
  </yl-dialog>
</template>

<script>
import { addEnterpriseAccountList } from '@/subject/admin/api/cmp_api/insurance_basic_setting'
export default {
  name: 'ChooseDrugMerchants',
  props: {
    mutiSelect: {
      type: Boolean,
      default: false
    },
    chooseItem: {
      type: [Number, String],
      default: ''
    }
  },
  data() {
    return {
      query: {
        current: 1,
        size: 10,
        ename: '',
        total: 0
      },
      //  可选商家列表
      dataList: [],
      loading: false,
      //  选择商品id集合列表
      chooseList: [],
      showDialog: false
    }
  },
  methods: {
    //  获取可选商品列表
    async getEnterpriseAccountList() {
      this.loading = true
      let query = this.query
      let data = await addEnterpriseAccountList(
        query.current,
        query.size,
        query.ename
      )
      this.loading = false
      if (data && data.records) {
        if (this.chooseItem) {
          let hasIndex = data.records.findIndex(item => item.id == this.chooseItem)
          if (hasIndex !== -1) {
            data.records[hasIndex].chose = true
          }
        }
        this.dataList = data.records
        this.query.total = data.total
      }
    },
    //  打开选择商家
    openEnterpriseDialog() {
      this.showDialog = true
      this.query = {
        current: 1,
        size: 10,
        ename: '',
        total: 0
      }
      this.chooseList = []
      this.getEnterpriseAccountList()
    },
    //  商品中点击选择
    choose(row, index) {
      if (row.hmcType == 1) {
        this.$common.warn('此商家未开通“C端药+险的销售与药品兑付”功能请先设置')
      } else {
        row.chose = true
        this.showDialog = false
        this.$emit('selectData', row)
      }
    },
    //  企业搜索
    handleSearch() {
      this.query.current = 1
      this.getEnterpriseAccountList()
    },
    //  重置搜索输入
    handleReset() {
      this.query = {
        current: 1,
        size: 10,
        ename: ''
      }
    }
  }
}
</script>

<style lang="scss" scoped>
</style>
