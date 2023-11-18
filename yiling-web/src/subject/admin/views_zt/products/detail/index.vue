<template>
  <div class="app-container">
    <div class="app-container-content">
      <div class="flex-row-right font-light-color font-size-base">
        <span class="font-important-color">{{ companyName }}</span>供应商：
      </div>
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">商品名称</div>
              <el-input v-model="query.name" placeholder="请输入商品名称" />
            </el-col>
            <el-col :span="8">
              <div class="title">批准文号</div>
              <el-input v-model="query.licenseNo" placeholder="请输入批准文号" />
            </el-col>
            <el-col :span="8">
              <div class="title">生产厂家</div>
              <el-input v-model="query.manufacturer" placeholder="请输入生产厂家" />
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">一级分类</div>
              <el-select v-model="query.standardCategoryId1" @change="selectChange" placeholder="请选择一级分类">
                <el-option label="全部" :value="0"></el-option>
                <el-option v-for="item in category" :key="item.id" :label="item.name" :value="item.id">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="8">
              <div class="title">二级分类</div>
              <el-select v-model="query.standardCategoryId2" placeholder="请选择二级分类">
                <el-option label="全部" :value="0"></el-option>
                <el-option v-for="item in cateChild" :key="item.id" :label="item.name" :value="item.id">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="8">
              <div class="title">标准库ID</div>
              <el-input v-model="query.standardId" placeholder="请输入标准库ID" />
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="24">
              <yl-search-btn :total="total" @search="handleSearch" @reset="handleReset" />
            </el-col>
          </el-row>
        </div>
      </div>
      <div class="down-box">
        <div class="left-btn">
          <el-link type="primary" :underline="false" @click="outputData">导出查询结果</el-link>
        </div>
        <!-- <div class="btn">
          <el-link class="mar-r-10" type="primary" :underline="false" :href="'NO_5' | template">
            下载模板
          </el-link>
          <el-link class="mar-r-10" type="primary" :underline="false" @click="goImport">导入更新商品信息</el-link>

        </div> -->
      </div>
      <div class="mar-t-10">
        <el-tabs class="bg-white" v-model="query.goodsStatus" type="card" @tab-click="handleTabClick">
          <el-tab-pane :label="'全部商品'" name="0"></el-tab-pane>
          <el-tab-pane :label="'审核通过'" name="4"></el-tab-pane>
          <el-tab-pane :label="'待审核'" name="5"></el-tab-pane>
          <el-tab-pane :label="'审核不通过'" name="6"></el-tab-pane>
        </el-tabs>
        <yl-table :list="dataList" :total="total" :page.sync="query.page" :limit.sync="query.limit" :loading="loading" @getList="getList">
          <el-table-column>
            <template slot-scope="{ row, $index }">
              <div class="table-view" :key="$index">
                <div class="session font-size-base">
                  <span class="font-light-color">商品ID：</span>{{ row.id }}
                </div>
                <div class="content flex-row-left">
                  <div class="table-img">
                    <img :src="row.pic" />
                  </div>
                  <div class="table-item mar-l-10">
                    <div class="font-size-base font-title-color">{{ row.name }}</div>
                    <div class="font-size-base font-title-color">{{ row.licenseNo }}</div>
                    <div class="font-size-base font-title-color">{{ row.specifications }}</div>
                    <div class="font-size-base font-title-color">{{ row.manufacturer }}</div>
                  </div>
                  <div class="table-item">
                    <div class="item font-size-base font-title-color"><span class="font-light-color">使用产品线：</span>{{ row.goodsLineDesc }}</div>
                  </div>
                  <div class="flex1">
                    <span class="font-light-color">商品状态：</span>
                    <el-tag :type="row.auditStatus === 1 ? 'success' : 'info'">
                      {{ row.auditStatus | dictLabel(goodStatus) }}
                      <el-tooltip>
                        <div slot="content">下架原因：{{ row.outReason | dictLabel(goodsReason) }}</div>
                        <i v-if="row.auditStatus === 2" class="el-icon-warning-outline"></i>
                      </el-tooltip>
                    </el-tag>
                    <div class="item font-size-base font-title-color"><span class="font-light-color"></span></div>
                    <div class="item font-size-base font-title-color"><span class="font-light-color"></span></div>
                  </div>
                  <!-- <div class="flex-column-center edit-btn">
                    <div v-if="row.goodsStatus !== 6">
                      <yl-button v-if="row.goodsStatus === 2" type="text" @click="upDownProduct(row, $index, 1)">上架</yl-button>
                      <yl-button v-if="row.goodsStatus === 1" type="text" @click="upDownProduct(row, $index, 2)">下架</yl-button>
                    </div>
                  </div> -->
                </div>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </div>
</template>

<script>
import { getCompanyProductList, getProductCategory, changeProductInfo } from '@/subject/admin/api/zt_api/products'
import { createDownLoad } from '@/subject/admin/api/common'
import { goodsStatus, enterpriseType, goodsOutReason } from '@/subject/admin/utils/busi'

export default {
  name: 'ZtProductsDetail',
  components: {
  },
  computed: {
    goodStatus() {
      return goodsStatus()
    },
    companyType() {
      return enterpriseType()
    },
    goodsReason() {
      return goodsOutReason()
    }
  },
  data() {
    return {
      query: {
        goodsStatus: '0',
        page: 1,
        limit: 10,
        standardCategoryId1: 0,
        standardCategoryId2: 0,
        specifications: ''
      },
      dataList: [],
      dataCount: {},
      loading: false,
      total: 0,
      companyName: '',
      category: [],
      cateChild: []
    }
  },
  mounted() {
    this.id = this.$route.params.id
    this.$nextTick(() => {
      this.companyName = this.$route.params.name
    })
    if (this.id) {
      this.getList(true)
    }
    this.getCate()
  },
  methods: {
    async getList(type) {
      this.loading = true
      let query = this.query
      let data = await getCompanyProductList(
        query.page,
        query.limit,
        this.id,
        query.goodsStatus,
        query.licenseNo,
        query.manufacturer,
        query.name,
        query.specifications,
        query.standardCategoryId1,
        query.standardCategoryId2,
        query.standardId
      )
      this.$log(data)
      this.loading = false
      this.dataList = data.records
      this.total = data.total
    },
    // 获取tab统计数据
    async getListCount() {
      let query = this.query
      let data = await getCompanyProductList(
        query.page,
        query.limit,
        this.id,
        null,
        query.licenseNo,
        query.manufacturer,
        query.name,
        query.specifications,
        query.standardCategoryId1,
        query.standardCategoryId2,
        query.standardId
      )
      if (data) {
        this.dataCount = data
      }
    },
    handleSearch() {
      this.query.page = 1
      this.query.goodsStatus = '0'
      this.getList()
    },
    handleReset() {
      this.query = {
        page: 1,
        limit: 10,
        goodsStatus: '0',
        standardCategoryId1: 0,
        standardCategoryId2: 0
      }
    },
    // 去导入页面
    goImport() {
      this.$router.push({
        path: '/importFile/importFile_index',
        query: {
          action: '/dataCenter/api/v1/admin/goods/importUpdateGoods'
        }
      })
    },
    // 上下架
    upDownProduct(row, index, type) {
      // 上架
      if (type === 1) {
        this.changeProduct(row.id, 1, type, index)
      }
      // 下架
      else if (type === 2) {
        this.$common.confirm('下架该商品会下架前台商品，但已有订单信息不受影响，请确认是否下架？', r => {
          if (r) {
            this.changeProduct(row.id, 2, type, index)
          }
        })
      }
    },
    async changeProduct(id, status, type, index) {
      this.$common.showLoad()
      let data = await changeProductInfo(status, [id])
      this.$common.hideLoad()
      if (data && data.result) {
        if (type === 1) {
          this.$common.n_success('上架成功')
        } else {
          this.$common.n_success('下架成功')
        }
        this.getList()
        // this.getListCount()
      }
    },
    // 获取分类
    async getCate() {
      if (this.category.length === 0) {
        let { list } = await getProductCategory()
        this.category = list
        this.$log(list)
      }
    },
    // 选择分类大类
    selectChange(value) {
      this.$log(value)
      if (this.query.standardCategoryId2) {
        this.query.standardCategoryId2 = null
      }
      let data = this.category.find(item => item.id === value)
      if (data.children && data.children.length) {
        this.cateChild = data.children
      }
    },
    handleTabClick() {
      this.query.page = 1
      this.query.limit = 10
      this.getList()
    },
    async outputData() {
      this.$common.showLoad()
      let query = this.query
      let data = await createDownLoad(
        'goodsCentrePageListExportService',
        '商品信息',
        '供应商商品导出',
        '管理后台 - 商品管理',
        [
          {
            desc: '企业ID',
            name: 'eid',
            value: this.id
          },
          {
            desc: '商品名称',
            name: 'name',
            value: query.name
          },
          {
            desc: '批准文号',
            name: 'licenseNo',
            value: query.licenseNo || ''
          },
          {
            desc: '生产厂家',
            name: 'manufacturer',
            value: query.manufacturer || ''
          },
          {
            desc: '一级分类',
            name: 'standardCategoryId1',
            value: query.standardCategoryId1
          },
          {
            desc: '二级分类',
            name: 'standardCategoryId2',
            value: query.standardCategoryId2
          },
          {
            desc: '标准库ID',
            name: 'standardId',
            value: query.standardId
          },
          {
            desc: '药品规格',
            name: 'specifications',
            value: query.specifications
          },
          {
            desc: '商品状态',
            name: 'auditStatus',
            value: query.goodsStatus
          }
        ]
      )
      this.$common.hideLoad()
      if (data && data.result) {
        this.$common.n_success('创建下载任务成功，请在下载中心查看')
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
