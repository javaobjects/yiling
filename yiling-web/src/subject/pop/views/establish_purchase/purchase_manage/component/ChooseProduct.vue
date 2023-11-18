<template>
  <yl-dialog width="60%" title="添加商品" :show-footer="false" :visible.sync="showDialog">
    <div class="dialog-content choose-drug-view">
      <div class="search-box">
        <el-row>
          <el-col :span="8">
            <div class="title">商品名称</div>
            <el-input v-model="query.name" placeholder="请输入商品名称" @keyup.enter.native="handleSearch" />
          </el-col>
          <el-col :span="8">
            <div class="title">批准文号</div>
            <el-input v-model="query.licenseNo" placeholder="请输入批准文号" @keyup.enter.native="handleSearch" />
          </el-col>
          <el-col :span="8">
            <div class="title">专利类型</div>
            <el-select v-model="query.isPatent" placeholder="请选择专利类型">
              <el-option label="全部" :value="0"></el-option>
              <el-option v-for="item in isPatentOption" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
          </el-col>
        </el-row>
      </div>
      <div class="search-btn-box">
        <yl-search-btn :total="total" @search="handleSearch" @reset="handleReset" />
      </div>
      <div class="table-box mar-t-16">
        <div class="btn-box">
          <yl-button class="custom-btn" plain type="primary" @click="handleAddAll">批量添加</yl-button>
        </div>
        <yl-table
          :show-header="true"
          :list="dataList"
          :total="total"
          :page.sync="query.current"
          :limit.sync="query.size"
          :loading="loading"
          :stripe="true"
          @getList="getGoodsList()"
        >
          <el-table-column align="center" width="80" label="ID" prop="id"></el-table-column>
          <el-table-column align="center" label="专利类型" prop="isPatent">
            <template slot-scope="{ row }">
              <div>{{ row.isPatent | dictLabel(isPatentOption) }}</div>
            </template>
          </el-table-column>
          <el-table-column align="center" label="商品名称" prop="name"></el-table-column>
          <el-table-column align="center" label="规格" prop="sellSpecifications">
            <template slot-scope="{ row }">
              <span>{{ row.sellSpecifications ? row.sellSpecifications : '- -' }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" label="批准文号" prop="licenseNo">
            <template slot-scope="{ row }">
              <span>{{ row.licenseNo ? row.licenseNo : '- -' }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" label="操作" width="100">
            <template slot-scope="{ row, $index }">
              <yl-button type="text" :disabled="row.goodsSelStatus" @click="choose(row, $index)">{{
                row.goodsSelStatus === true ? '已选择' : '选择'
              }}</yl-button>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </yl-dialog>
</template>

<script>
import { queryProcRelationGoodsOptionalPage, queryFactoryGoodsList } from '@/subject/pop/api/establish_purchase'

export default {
  name: 'ChooseProduct',
  props: {
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
        name: '',
        licenseNo: '',
        isPatent: 0
      },
      total: 0,
      isPatentOption: [
        { label: '非专利', value: 1 },
        { label: '专利', value: 2 }
      ],
      dataList: [],
      loading: false,
      showDialog: false
    }
  },
  methods: {
    // 获取选择药品弹窗列表
    async getGoodsList() {
      this.loading = true
      let query = this.query
      let data = await queryProcRelationGoodsOptionalPage(
        query.current,
        query.size,
        this.id,
        query.name,
        query.licenseNo,
        query.isPatent
      )
      this.loading = false
      if (data && data.records) {
        //  已选的药品状态修改
        data.records.forEach(item => {
          let hasIndex = this.chooseList.findIndex(chooseListItem => {
            return chooseListItem.goodsId === item.id
          })
          if (hasIndex != -1) {
            item.goodsSelStatus = true
          } else {
            item.goodsSelStatus = false
          }
        })
        this.dataList = data.records
        this.total = data.total
      }
    },
    // 打开选择商品
    openGoodsDialog() {
      this.showDialog = true
      this.query = {
        current: 1,
        size: 10,
        name: '',
        licenseNo: '',
        isPatent: 0
      }
      this.getGoodsList()
    },
    // 点击选择
    choose(row, index) {
      row.goodsSelStatus = true
      this.$emit('selectItem', [
        {
          goodsId: row.id,
          goodsName: row.name,
          isPatent: row.isPatent,
          licenseNo: row.licenseNo,
          sellSpecifications: row.sellSpecifications,
          sellSpecificationsId: row.sellSpecificationsId,
          standardId: row.standardId,
          rebate: 0
        }
      ])
    },
    // 添加所有商品
    async handleAddAll() {
      this.$common.showLoad()
      let data = await queryFactoryGoodsList(this.id, this.query.name, this.query.licenseNo, this.query.isPatent)
      this.$common.hideLoad()
      if (data) {
        let selectAlldata = data.filter(item => {
          return this.chooseList.every(chooseItem => {
            return chooseItem.goodsId !== item.id
          })
        })
        this.$emit('selectItem', selectAlldata.map(item => {
          return {
            goodsId: item.id,
            goodsName: item.name,
            isPatent: item.isPatent,
            licenseNo: item.licenseNo,
            sellSpecifications: item.sellSpecifications,
            sellSpecificationsId: item.sellSpecificationsId,
            standardId: item.standardId,
            rebate: 0
          }
        }))
        this.showDialog = false
      }
    },
    // 商品搜索
    handleSearch() {
      this.query.current = 1
      this.getGoodsList()
    },
    // 重置搜索输入
    handleReset() {
      this.query = {
        current: 1,
        size: 10,
        name: '',
        licenseNo: '',
        isPatent: 0
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
