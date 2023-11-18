<template>
  <yl-dialog width="60%" title="选择药品" :show-footer="false" :visible.sync="showDialog">
    <div class="dialog-content choose-drug-view">
      <div class="search-box">
        <el-row>
          <el-col :span="8" v-if="type !== 'standardGoods'">
            <div class="title">药品名称</div>
            <el-input v-model="query.name" placeholder="请输入药品名称" @keyup.enter.native="handleSearch"/>
          </el-col>
          <el-col :span="8" v-if="type === 'standardGoods'">
            <div class="title">批准文号</div>
            <el-input v-model="query.licenseNo" placeholder="请输入批准文号" @keyup.enter.native="handleSearch"/>
          </el-col>
        </el-row>
      </div>
      <div class="search-btn-box">
        <yl-search-btn :total="total" @search="handleSearch" @reset="handleReset" />
      </div>
      <div class="table-box mar-t-16">
        <yl-table
          :show-header="true"
          :list="dataList"
          :total="total"
          :page.sync="query.current"
          :limit.sync="query.size"
          :loading="loading"
          :stripe="true"
          @getList="getGoodsList()"
          :empty-text="type == 'goodsInAddContent' ? '药品尚未加入保险药品管控中，请先添加' : '暂无数据'"
        >
          <el-table-column align="center" label="药品名称" prop="name"></el-table-column>
          <el-table-column align="center" label="规格" prop="sellSpecifications">
            <template slot-scope="{ row }">
              <span>{{ row.sellSpecifications ? row.sellSpecifications : "- -" }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" label="批准文号" prop="licenseNo">
            <template slot-scope="{ row }">
              <span>{{ row.licenseNo ? row.licenseNo : "- -" }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" label="操作" width="100">
            <template slot-scope="{ row, $index }">
              <yl-button type="text" :disabled="row.chose" @click="choose(row, $index)"> {{ row.chose === true ? "已选择" : "选择" }} </yl-button>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </yl-dialog>
</template>

<script>
import { queryStandardSpecificationPage, queryPageInsuranceGoods, queryInsuranceContentGoods } from '@/subject/admin/api/cmp_api/insurance_basic_setting'

export default {
  name: 'ChooseProduct',
  props: {
    type: {
      type: String,
      default: ''
    },
    mutiSelect: {
      type: Boolean,
      default: false
    },
    chooseList: {
      type: Array,
      default: () => []
    },
    id: {
      type: [Number, String],
      default: ''
    }
  },
  data() {
    return {
      query: {
        current: 1,
        size: 10,
        licenseNo: '',
        name: ''
      },
      total: 0,
      //  可选商品列表
      dataList: [],
      loading: false,
      showDialog: false
    }
  },
  methods: {
    //  获取选择药品弹窗列表
    async getGoodsList() {
      this.loading = true
      let query = this.query
      if (this.type === 'standardGoods') {
        let data = await queryStandardSpecificationPage(
          query.current,
          query.size,
          query.licenseNo
        )
        this.loading = false
        if (data && data.records) {
          //  已选的药品状态修改
          data.records.forEach(item => {
            let hasIndex = this.chooseList.findIndex(chooseListItem => {
              return chooseListItem.sellSpecificationsId === item.id
            })
            if (hasIndex != -1 ) {
              item.chose = true
            } else {
              item.chose = false
            }
          })
          this.dataList = data.records
          this.total = data.total
        }
      } else if (this.type === 'insuranceGoods') {
        let data = await queryPageInsuranceGoods(
          query.current,
          query.size,
          query.name
        )
        this.loading = false
        if (data && data.records) {
          //  已选的药品状态修改
          data.records.forEach(item => {
            let hasIndex = this.chooseList.findIndex(chooseListItem => {
              return chooseListItem.sellSpecificationsId === item.sellSpecificationsId
            })
            if (hasIndex != -1 ) {
              item.chose = true
            } else {
              item.chose = false
            }
          })
          this.dataList = data.records
          this.total = data.total
        }
      } else if (this.type === 'goodsInAddContent') {
        let data = await queryInsuranceContentGoods(
          query.current,
          query.size,
          query.name,
          this.id
        )
        this.loading = false
        if (data && data.records) {
          //  已选的药品状态修改
          data.records.forEach(item => {
            item.chose = item.goodsDisableVO.isAllowSelect === 1 || this.chooseList.some(chooseListItem => chooseListItem.sellSpecificationsId === item.sellSpecificationsId)
          })
          this.dataList = data.records
          this.total = data.total
        }
      }
    },
    //  打开选择商品
    openGoodsDialog() {
      this.showDialog = true
      this.query = {
        current: 1,
        size: 10,
        licenseNo: '',
        name: ''
      }
      this.getGoodsList()
    },
    //  点击选择
    choose(row, index) {
      if (this.mutiSelect) {
        row.chose = true
        this.$emit('selectItem', row)
      } else {
        row.chose = true
        this.showDialog = false
        this.$emit('selectItem', row)
      }
    },
    //  商品搜索
    handleSearch() {
      this.query.current = 1
      this.getGoodsList()
    },
    //  重置搜索输入
    handleReset() {
      this.query = {
        current: 1,
        size: 10,
        name: '',
        licenseNo: ''
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
