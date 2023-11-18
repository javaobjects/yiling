<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <div class="app-container-content">
      <div class="tool-tip">
        <yl-tool-tip>
          提示：价格优先级为：客户定价 > 分组定价 > 基价
        </yl-tool-tip>
      </div>
      <div class="common-box">
        <div class="search-box" style="margin-top: 0;">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">商品名称</div>
              <el-input v-model="query.name" @keyup.enter.native="searchEnter" placeholder="请输入商品名称" />
            </el-col>
            <el-col :span="6">
              <div class="title">批准文号/注册证编号</div>
              <el-input v-model="query.licenseNo" @keyup.enter.native="searchEnter" placeholder="请输入批准文号/注册证编号" />
            </el-col>
            <el-col :span="6">
              <div class="title">商品状态</div>
              <el-select
                v-model="query.goodsStatus"
                placeholder="请选择商品状态">
                <el-option label="全部" :value="0"></el-option>
                <el-option
                  v-for="item in goodStatus"
                  v-show="item.value != 5 && item.value != 6"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <div class="title">生产厂家</div>
              <el-input v-model="query.manufacturer" @keyup.enter.native="searchEnter" placeholder="请输入生产厂家" />
            </el-col>
          </el-row>
        </div>
        <div class="search-box">
          <div class="mar-t-16 pad-t-16">
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
      </div>
      <div class="top-btn font-size-lg">
        <span class="bold">数据列表</span>
<!--        <div class="right-btn">-->
<!--          <yl-button v-role-btn="[7]" type="primary" @click="downLoadTemp" plain>下载模板</yl-button>-->
<!--          <yl-button v-role-btn="[8]" type="primary" @click="goImport" plain>批量导入商品定价</yl-button>-->
<!--        </div>-->
      </div>
      <div class="my-table common-box common-box-no-pad">
        <yl-table
          border
          :show-header="true"
          :list="dataList"
          :total="query.total"
          :page.sync="query.page"
          :limit.sync="query.limit"
          :loading="loading"
          @getList="getList">
          <el-table-column label="商品ID" min-width="80" align="center">
            <template slot-scope="{ row }">
              <div>{{ row.id }}</div>
            </template>
          </el-table-column>
          <el-table-column label="药品图片" min-width="165" align="center">
            <template slot-scope="{ row }">
              <el-image
                class="my-image"
                :src="row.pic"
                fit="contain" />
            </template>
          </el-table-column>
          <el-table-column label="药品信息" min-width="360" align="center">
            <template slot-scope="{ row }">
              <div class="product-desc">
                <div>{{ row.name }}</div>
                <div>{{ row.sellSpecifications }}</div>
                <div>{{ row.licenseNo }}</div>
                <div>{{ row.manufacturer || '- -' }}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="价格" min-width="100" align="center">
            <template slot-scope="{ row }">
              <div>{{ row.price | toThousand('￥') }}</div>
            </template>
          </el-table-column>
          <el-table-column label="客户定价" min-width="241" align="center">
            <template slot-scope="{ row }">
              <div style="padding-bottom: 8px;">客户数：{{ row.customerPriceNum }}</div>
              <div>{{ row.customerPrice }}</div>
            </template>
          </el-table-column>
          <el-table-column label="分组定价" min-width="241" align="center">
            <template slot-scope="{ row }">
              <div style="padding-bottom: 8px;">分组数：{{ row.customerGroupPriceNum }}</div>
              <div>{{ row.customerGroupPrice }}</div>
            </template>
          </el-table-column>
          <el-table-column label="操作" min-width="127" align="center">
            <template slot-scope="{ row }">
              <div><yl-button v-role-btn="[5]" type="text" @click="goPricePage(row, 1)">客户定价</yl-button></div>
              <div><yl-button v-role-btn="[6]" type="text" @click="goPricePage(row, 2)">分组定价</yl-button></div>
              <div><yl-button v-role-btn="[7]" type="text" @click="goPricePage(row, 3)">商品限价</yl-button></div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
      <yl-dialog 
        title="商品限价" 
        width="600px" 
        :visible.sync="showDialog"
        @confirm="confirmDialog"
      >
        <el-form
          ref="dataForm"
          :rules="rules"
          class="limitPrice"
          :model="dialogQuery"
          label-width="130px"
          label-position="right"
        >
          <el-row>
            <el-col :span="17">
              <el-form-item label="商业售价下限：" prop="lowerLimitPrice">
                <el-input v-model="dialogQuery.lowerLimitPrice" placeholder="请输入商业售价下限" />
                <span class="units">元</span>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="17">
              <el-form-item label="商品售价上限：" prop="upperLimitPrice">
                <el-input v-model="dialogQuery.upperLimitPrice" placeholder="请输入商品售价上限" />
                <span class="units">元</span>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </yl-dialog>
    </div>
  </div>
</template>

<script>
import { getPricePageList, saveOrUpdateLimitPrice, getLimitPrice } from '@/subject/pop/api/products'
import { createDownLoad } from '@/subject/pop/api/common'
import { goodsStatus } from '@/subject/pop/utils/busi'

export default {
  name: 'ProductsPrice',
  components: {
  },
  computed: {
    goodStatus() {
      return goodsStatus()
    }
  },
  data() {
    const reg = /^0\.([1-9]|\d[1-9])$|^[1-9]\d{0,8}\.\d{0,4}$|^[1-9]\d{0,8}$/
    // 商业售价下限
    const validateLower = (rule, value, callback) => {
      if (!reg.test(value)) {
        callback(new Error('请输入正确的数字、最多四位小数且大于0'))
      } else {
        callback()
      }
    }
    // 商品售价上限
    const validateUpper = (rule, value, callback) => {
      if (!reg.test(value)) {
        callback(new Error('请输入正确的数字、最多四位小数且大于0'))
      } else if (value < this.dialogQuery.lowerLimitPrice) {
        callback(new Error('请输入的数字符合商品售价上限大于商品售价下限'))
      } else {
        callback()
      }
    }
    return {
      // 头部导航
      navList: [
        {
          title: '首页',
          path: '/dashboard'
        },
        {
          title: '商品管理',
          path: ''
        },
        {
          title: '商品定价'
        }
      ],
      query: {
        total: 0,
        page: 1,
        limit: 10,
        goodsStatus: 0
      },
      dataList: [],
      loading: false,
      showDialog: false,
      dialogQuery: {
        lowerLimitPrice: '',
        upperLimitPrice: ''
      },
      rules: {
        lowerLimitPrice: [
          { required: true, trigger: 'blur', validator: validateLower },
          { required: true, message: '请输入正确内容', trigger: 'blur' }
        ],
        upperLimitPrice: [
          { required: true, trigger: 'blur', validator: validateUpper },
          { required: true, message: '请输入正确内容', trigger: 'blur' }
        ]
      },
      rowData: {}
    }
  },
  activated() {
    this.getList()
  },
  methods: {
    // Enter
    searchEnter(e) {
      const keyCode = window.event ? e.keyCode : e.which;
      if (keyCode === 13) {
        this.getList()
      }
    },
    async getList() {
      this.loading = true
      let query = this.query
      let data = await getPricePageList(
        query.page,
        query.limit,
        '',
        query.goodsStatus,
        query.licenseNo,
        query.name,
        query.manufacturer
      )
      this.loading = false
      this.dataList = data.records
      this.query.total = data.total
    },
    handleSearch() {
      this.query.page = 1
      this.getList()
    },
    handleReset() {
      this.query = {
        page: 1,
        limit: 10,
        goodsStatus: 0
      }
    },
    // 去导入页面
    goImport() {
      this.$router.push({
        path: '/importFile/importFile_index',
        query: {
          action: '/admin/pop/api/v1/price/import'
        }
      })
    },
    // 下载模板
    async downLoadTemp() {
      let query = this.query
      this.$common.showLoad()
      let data = await createDownLoad({
        className: 'goodsPriceMouldExportService',
        fileName: 'string',
        groupName: 'pop后台-商品定价',
        menuName: '商品定价',
        searchConditionList: [
          {
            desc: '企业ID',
            name: 'eid',
            value: ''
          },
          {
            desc: '商品状态',
            name: 'goodsStatus',
            value: query.goodsStatus || ''
          },
          {
            desc: '商品名称',
            name: 'name',
            value: query.name || ''
          },
          {
            desc: '注册证号',
            name: 'licenseNo',
            value: query.licenseNo || ''
          },
          {
            desc: '生产厂家',
            name: 'manufacturer',
            value: query.manufacturer || ''
          }
        ]
      })
      this.$common.hideLoad()
      if (data && data.result) {
        this.$common.n_success('创建下载任务成功，请在下载中心查看')
      }
    },
    // 跳转定价页面
    goPricePage(row, type) {
      // 客户定价
      if (type === 1) {
        this.$router.push(`/products/price_user/${row.id}`)
      } else if ( type === 2 ) {
        this.$router.push(`/products/price_group/${row.id}`)
      } else if ( type === 3 ) {
        this.dialogQuery = {
          lowerLimitPrice: '',
          upperLimitPrice: ''
        }
        this.getLimitPrice(row.id)
      }
    },
    // 获取商品限价信息
    async getLimitPrice(id) {
      this.$common.showLoad()
      let data = await getLimitPrice(id)
      this.$common.hideLoad()
      if (data) {
        this.rowData = data
        this.dialogQuery = {
          lowerLimitPrice: data.lowerLimitPrice,
          upperLimitPrice: data.upperLimitPrice
        }
      } else {
        this.dialogQuery = {
          lowerLimitPrice: '',
          upperLimitPrice: ''
        }
        this.rowData = {}
      }
      this.rowData.goodsId = id
      this.showDialog = true
    },
    async confirmDialog() {
      this.$refs.dataForm.validate(async valid => {
        if (valid) {
          this.$common.showLoad()
          let data = await saveOrUpdateLimitPrice(
            this.rowData.goodsId,
            this.rowData.id,
            Number(this.dialogQuery.lowerLimitPrice),
            Number(this.dialogQuery.upperLimitPrice)
          )
          this.$common.hideLoad()
          if (data !== undefined) {
            this.getList()
            this.$common.n_success('保存成功');
            this.showDialog = false
          }
        } else {
          return false
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
  @import "./index.scss";
</style>
