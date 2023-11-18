<template>
  <div class="app-container">
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">推荐位名称</div>
              <el-input v-model="query.name" @keyup.enter.native="searchEnter" placeholder="请输入推荐位名称" />
            </el-col>
            <el-col :span="8">
              <div class="title">状态</div>
                <el-radio-group v-model="query.status">
                  <el-radio :label="0">全部</el-radio>
                  <el-radio :label="1">启用</el-radio>
                  <el-radio :label="2">停用</el-radio>
                </el-radio-group>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn :total="query.total" @search="handleSearch" @reset="handleReset" />
            </el-col>
          </el-row>
        </div>
      </div>
      <div class="mar-tb-16">
        <yl-button type="primary" @click="addNew">新增推荐位</yl-button>
      </div>
      <div>
        <yl-table border :show-header="true" :list="dataList" :total="query.total" :page.sync="query.page" :limit.sync="query.limit" :loading="loading" @getList="getList">
          <el-table-column align="center" min-width="120" label="推荐位名称" prop="title">
          </el-table-column>
          <el-table-column align="center" width="120" label="状态">
            <template slot-scope="{ row }">
              <el-tag :type="row.status === 1 ? 'success' : 'danger'">{{ row.status | enable }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column fixed="right" align="center" label="操作" width="120">
            <template slot-scope="{ row }">
              <div>
                <yl-button type="text" @click="edit(row)">修改</yl-button>
              </div>
              <div>
                <yl-status url="/pop/api/v1/recommend/update/status" :status="row.status" status-key="status" :data="{id: row.id}" @change="getList">
                </yl-status>
              </div>
            </template>
          </el-table-column>
        </yl-table>
        <yl-dialog width="60%" :title="isEdit ? '编辑推荐位' : '新增推荐位'" @confirm="confirm" :visible.sync="showDialog">
          <div class="dialog-content">
            <el-form ref="dataForm" class="recome-view" :rules="rules" :model="form" label-width="120px" label-position="left">
              <el-form-item label="状态" prop="status">
                <el-radio-group v-model="form.status">
                  <el-radio :label="1">启用</el-radio>
                  <el-radio :label="2">停用</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="推荐位标题" prop="title">
                <el-input v-model="form.title" :maxlength="6" placeholder="请输入推荐位标题" />
              </el-form-item>
              <div class="mar-b-10">
                <yl-button type="primary" @click="chooseProduct">选择商品</yl-button>
              </div>
              <el-form-item label-width="0">
                <yl-table :show-header="true" :list="dataList1" :loading="loading1">
                  <el-table-column align="center" width="80" label="序号" prop="id">
                  </el-table-column>
                  <el-table-column align="center" width="130" label="商品图片">
                    <template slot-scope="{ row }">
                      <img :src="row.pic">
                    </template>
                  </el-table-column>
                  <el-table-column align="center" min-width="200" label="商品信息">
                    <template slot-scope="{ row }">
                      <div class="font-title-color font-size-base text-l">
                        <div>{{ row.goodsName }}</div>
                        <div>{{ row.licenseNo }}</div>
                        <div>{{ row.specifications }}</div>
                        <div>{{ row.manufacturer }}</div>
                      </div>
                    </template>
                  </el-table-column>
                  <el-table-column align="center" width="100" label="价格">
                    <template slot-scope="{ row }">
                      <span>￥{{ row.price }}</span>
                    </template>
                  </el-table-column>
                  <el-table-column align="center" min-width="180" label="商品排序" prop="link">
                    <template slot-scope="{ row }">
                      <el-input-number v-model="row.sort" size="mini" :min="1" :max="200" label="请输入排序"></el-input-number>
                    </template>
                  </el-table-column>
                  <el-table-column fixed="right" align="center" label="操作" width="80">
                    <template slot-scope="{ row, $index }">
                      <div>
                        <yl-button type="text" @click="remove(row, $index)">移除</yl-button>
                      </div>
                    </template>
                  </el-table-column>
                </yl-table>
              </el-form-item>
            </el-form>
          </div>
        </yl-dialog>
        <choose-product ref="choose" :length="dataList1.length" :maxlength="10" @choose="chooseProDone" :id="form.id" />
      </div>
    </div>
  </div>
</template>

<script>
import { getRecommendList, saveRecommend, updateRecommend, getRecommendProductList } from '@/subject/admin/api/pop'
import { ylStatus } from '@/subject/admin/components'
import ChooseProduct from './component/ChooseProduct'

export default {
  name: 'PopRecommend',
  components: {
    ylStatus,
    ChooseProduct
  },
  computed: {
  },
  data() {
    return {
      query: {
        page: 1,
        limit: 10,
        total: 0,
        status: 0
      },
      query1: {
        page: 1,
        limit: 10,
        total: 0,
        status: 0
      },
      // 推荐位列表
      dataList: [],
      // 推荐位中已选择商品
      dataList1: [],
      // 列表加载
      loading: false,
      // 推荐位中已选择商品加载
      loading1: false,
      // 新增编辑
      showDialog: false,
      isEdit: false,
      form: {
        status: 1
      },
      rules: {
        title: [{ required: true, message: '请输入推荐位名称', trigger: 'blur' }],
        status: [{ required: true, message: '请选择状态', trigger: 'change' }]
      }
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
      let data = await getRecommendList(
        query.page,
        query.limit,
        query.status,
        query.name
      )
      this.loading = false
      if (data) {
        this.dataList = data.records
        this.query.total = data.total
      }
    },
    // 首页搜索
    handleSearch() {
      this.query.page = 1
      this.getList()
    },
    handleReset() {
      this.query = {
        page: 1,
        limit: 10,
        status: 0
      }
    },
    // 编辑
    edit(row) {
      this.form = row
      this.getProductList((data) => {
        this.isEdit = true
        this.showDialog = true
        this.dataList1 = data
      })
    },
    // 新增
    addNew() {
      this.isEdit = false
      this.showDialog = true
      this.dataList1 = []
      this.form = {
        status: 1
      }
    },
    // 获取推荐位内商品列表
    async getProductList(callback) {
      let query = this.query1
      if (query.page === 1) {
        this.$common.showLoad()
      } else {
        this.loading1 = true
      }
      let data = await getRecommendProductList(
        this.form.id || null,
        query.page,
        query.limit
      )
      if (query.page === 1) {
        this.$common.hideLoad()
      } else {
        this.loading1 = false
      }
      if (data) {
        this.dataList1 = data.records.map(item => {
          item.type = 1
          return item
        })
        this.query1.total = data.total
        if (callback) callback(data.records)
      }
    },
    // 点击弹框确认
    confirm() {
      this.$refs.dataForm.validate(async valid => {
        if (valid) {
          if (this.dataList1.length === 0) {
            this.$common.error('请先选择商品')
            return
          }
          let form = this.form
          let data = null
          let list = this.dataList1.map(item => {
            let items = {
              goodsId: item.goodsId,
              sort: item.sort
            }
            return items
          })
          this.$common.showLoad()
          if (this.isEdit) {
            data = await updateRecommend(form.id, form.title, form.status, list)
          } else {
            data = await saveRecommend(form.title, form.status, list)
          }
          this.$common.hideLoad()
          if (data && data.result) {
            this.showDialog = false
            this.$common.n_success(this.isEdit ? '修改成功' : '新增成功')
            this.getList()
          }
        } else {
          return false
        }
      })
    },
    // 删除推荐位内商品
    remove(row, index) {
      this.dataList1.splice(index, 1)
    },
    // 打开选择商品
    chooseProduct() {
      this.$refs.choose.chooseProduct()
    },
    // 商品中选择完毕
    chooseProDone(data) {
      this.$refs.choose.closeProduct()
      this.$log(data)
      this.dataList1 = this.dataList1.concat(data).reverse()
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>

