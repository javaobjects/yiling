<template>
  <yl-dialog 
    width="60%" 
    title="添加商品" 
    :show-footer="true" 
    :visible.sync="showDialog"
    :show-cancle="false"
    @close="close"
    @confirm="close"
    >
    <div class="dialog-content choose-drug-view">
      <div class="search-box">
        <el-row>
          <el-col :span="8">
            <div class="title">商品名称</div>
            <el-input v-model="query.name" placeholder="请输入商品名称" @keyup.enter.native="handleSearch"/>
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
          <el-table-column align="center" width="80" label="商品ID" prop="id"></el-table-column>
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
// import { queryProcRelationGoodsOptionalPage, queryFactoryGoodsList } from '@/subject/pop/api/establish_purchase'

import { 
  queryProcTempalteGoodsOptionalPage,
  addGoodsToTemplate,
  addAllGoodsToTemplate
} from '@/subject/pop/api/establish_purchase'
export default {
  name: 'ChooseProduct',
  props: {
    chooseList: {
      type: Array,
      default: () => []
    },
    //模板id
    templateId: {
      type: [Number, String],
      default: ''
    },
    //工业eid
    factoryEid: {
      type: String,
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
  mounted() {
    console.log(this.factoryEid, 999)
  },
  methods: {
    // 获取选择药品弹窗列表
    async getGoodsList() {
      this.loading = true
      let query = this.query
      let data = await queryProcTempalteGoodsOptionalPage(
        this.templateId,
        this.factoryEid,
        query.name,
        query.licenseNo,
        query.isPatent,
        query.current,
        query.size
      )
      if (data !== undefined) {
        this.dataList = data.records
        this.total = data.total
      }
      this.loading = false
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
    async choose(row) {
      row.goodsSelStatus = true
      this.$common.showLoad();
      let data = await addGoodsToTemplate(
        this.templateId,
        this.factoryEid,
        row.isPatent,
        row.name,
        row.sellSpecifications,
        row.sellSpecificationsId,
        row.licenseNo,
        row.standardId,
        row.id
      )
      this.$common.hideLoad();
      if (data !== undefined) {
        this.$common.n_success('添加成功!')
      }
    },
    // 添加所有商品
    async handleAddAll() {
      let query = this.query
      this.$common.showLoad()
      let data = await addAllGoodsToTemplate(
        this.templateId,
        this.factoryEid,
        query.isPatent,
        query.licenseNo,
        query.name
      )
      this.$common.hideLoad()
      if (data !== undefined) {
        this.$common.n_success('添加成功!')
        this.getGoodsList()
      }
    },
    close() {
      this.showDialog = false
      this.$emit('selectItem')
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
