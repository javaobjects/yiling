<template>
  <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <div class="container">
        <div class="c-box">
          <el-form :model="form" :rules="rules" ref="dataForm" label-width="150px" class="demo-ruleForm">
            <el-form-item label="分类名称" prop="name">
              <el-input v-model="form.name" maxlength="6" show-word-limit placeholder="请输入分类名称"></el-input>
            </el-form-item>
            <el-form-item label="状态" prop="state">
              <el-radio-group v-model="form.status">
                <el-radio :label="1">启用</el-radio>
                <el-radio :label="2">停用</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="排序" prop="sort">
              <el-input v-model="form.sort" style="width: 100px;" placeholder="排序" @input="e => (form.sort = checkInput(e))"></el-input>
              <span class="font-size-base font-light-color" style="margin-left:20px;">排序逻辑 1-200，200权重最高，权重一样时候，将按照时间排序</span>
            </el-form-item>
          </el-form>
        </div>
        <div class="mar-tb-16">
          <yl-button type="primary" @click="addBanner">添加商品</yl-button>
        </div>
        <div>
          <yl-table :show-header="true" :list="dataList1" :loading="loading1">
            <el-table-column align="center" width="80" label="序号" prop="id">
            </el-table-column>
            <el-table-column align="center" label="商品图片">
              <template slot-scope="{ row }">
                <img class="goods-img" fit="contain" :src="row.pic">
              </template>
            </el-table-column>
            <el-table-column align="center" min-width="200" label="商品信息">
              <template slot-scope="{ row }">
                <div class="font-title-color font-size-base text-l">
                  <div>{{ row.name }}</div>
                  <div>{{ row.licenseNo }}</div>
                  <div>{{ row.sellSpecifications }}</div>
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
          <choose-product ref="choose" :length="dataList1.length" :select-list="dataList1" :maxlength="10" @choose="chooseProDone" :category-id="form.id" />
        </div>
      </div>
      <div class="bottom-view flex-row-center">
        <yl-button type="primary" plain @click="$router.go(-1)">返回</yl-button>
        <yl-button type="primary" @click="save">保存</yl-button>
      </div>
    </div>
  </div>
</template>

<script>
import { saveCategory, getCategoryDetail, updateCategory } from '@/subject/admin/api/pop'
import ChooseProduct from '../../recommend/component/ChooseProduct'

export default {
  name: 'GoodsCategoryEdit',
  components: {
    ChooseProduct
  },
  computed: {
  },
  data() {
    return {
      form: {
        name: '',
        status: 1,
        sort: ''
      },
      show: false,
      rules: {
        name: [{ required: true, message: '请输入分类名称', trigger: 'blur' }],
        status: [{ required: true, message: '请选择状态', trigger: 'blur' }],
        sort: [{ required: true, message: '请输入排序', trigger: 'blur' }]
      },
      // 已选择商品
      dataList1: [],
      // 已选择商品加载
      loading1: false
    }
  },
  mounted() {
    this.query = this.$route.params
    if (this.query.id) {
      this.getData()
    }
  },
  methods: {
    async getData() {
      this.$common.showLoad()
      let data = await getCategoryDetail(this.query.id)
      this.$common.hideLoad()
      if (data) {
        this.form = data
        this.dataList1 = data.categoryGoodsList
      }
    },
    // 添加商品
    addBanner() {
      this.$refs.choose.chooseProduct()
    },
    // 商品中选择完毕
    chooseProDone(data) {
      this.$refs.choose.closeProduct()
      this.$log(data)
      this.dataList1 = this.dataList1.concat(data).reverse()
    },
    // 删除推荐位内商品
    remove(row, index) {
      this.dataList1.splice(index, 1)
    },
    save() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          // eslint-disable-next-line no-empty
          if (this.dataList1.length > 0) {
          } else {
            this.$common.warn('请添加商品')
            return false
          }
          let form = this.form
          this.query = this.$route.params
          let data = null
          let list = this.dataList1.map(item => {
            let items = {
              goodsId: item.goodsId || item.id,
              sort: item.sort
            }
            return items
          })
          this.$common.showLoad()
          if (this.query.id) {
            data = await updateCategory(
              this.query.id,
              form.name,
              form.status,
              Number(form.sort),
              list
            )
          } else {
            data = await saveCategory(
              form.name,
              form.status,
              Number(form.sort),
              list
            )
          }
          this.$common.hideLoad()
          if (data && data.result) {
            this.$common.alert('保存成功', r => {
              this.$router.go(-1)
            })
          }
        } else {
          return false
        }
      })
    },
    checkInput(val) {
      val = val.replace(/[^0-9]/gi, '')
      if (val > 200) {
        val = 200
      }
      if (val < 1) {
        val = ''
      }
      return val
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
