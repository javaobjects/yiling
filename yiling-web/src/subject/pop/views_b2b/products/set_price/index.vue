<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <!-- 内容区 -->
    <div class="app-container-content has-bottom-bar">
      <!-- 基本信息 -->
      <div class="common-box box-1 top-bar">
        <div class="header-bar mar-b-10">
          <div class="sign"></div>基本信息
        </div>
        <div class="info">
          <div class="info-img">
            <el-image class="my-image" :src="data.picUrl" fit="contain" />
          </div>
          <div class="info-desc">
            <el-row :gutter="24">
              <el-col :span="8" :offset="0">
                <div class="intro"><span class="font-title-color">商品名称：</span>{{ data.name }}</div>
              </el-col>
              <el-col :span="8" :offset="0">
                <div class="intro"><span class="font-title-color">批准文号/注册证编号：</span>{{ data.licenseNo }}</div>
              </el-col>
              <el-col :span="8" :offset="0">
                <div class="intro"><span class="font-title-color">生产地址：</span>{{ data.manufacturerAddress }}</div>
              </el-col>

            </el-row>
            <el-row :gutter="24">
              <el-col :span="8" :offset="0">
                <div class="intro"><span class="font-title-color">包装规格：</span>{{ data.specifications }}</div>
              </el-col>
              <el-col :span="8" :offset="0">
                <div class="intro"><span class="font-title-color">生产厂家：</span>{{ data.manufacturer }}</div>
              </el-col>

              <el-col :span="8" :offset="0">
                <div class="intro"><span class="font-title-color">基本单位：</span>{{ data.unit }}</div>
              </el-col>

            </el-row>
            <el-row :gutter="24">
              <el-col :span="8" :offset="0">
                <div class="intro"><span class="font-title-color">商品价格：</span>{{ data.price }}</div>
              </el-col>
              <el-col :span="8" :offset="0">
                <div class="intro"><span class="font-title-color">包装规格：</span>{{ data.sellSpecifications }}</div>
              </el-col>
              <el-col :span="8" :offset="0">
                <div class="intro"><span class="font-title-color">生产日期：</span>{{ data.manufacturingDate | formatDate('yyyy-MM-dd') }}</div>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="8" :offset="0">
                <div class="intro"><span class="font-title-color">有效期至：</span>{{ data.expiryDate | formatDate('yyyy-MM-dd') }}</div>
              </el-col>
            </el-row>
          </div>
        </div>

      </div>
      <!-- tab -->
      <div class="flex-between mar-t-16 mar-b-8">
        <div class="tab ">
        </div>
        <div class="btn">
          <ylButton type="text" plain @click="allDelet">批量删除</ylButton>
          <ylButton type="primary" plain @click="add">新建</ylButton>
        </div>
      </div>
      <!-- table -->
      <div class="common-box">
        <div class="search-box" style="margin-top:0">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">企业类型</div>
              <el-select v-model="query.type" placeholder="请选择">
                <el-option label="全部" :value="0"></el-option>
                <el-option v-for="item in enterpriseType" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="12">
              <div class="title">区域</div>
              <div class="flex-row-left">
                <yl-choose-address is-all width="140px" :province.sync="query.provinceCode" :city.sync="query.cityCode" :area.sync="query.regionCode" />
              </div>
            </el-col>
          </el-row>
        </div>
        <div class="search-box">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn :total="total" @search="handleSearch" @reset="handleReset" />
            </el-col>
          </el-row>
        </div>
        <div class="mar-t-16">
          <yl-table stripe :show-header="true" :list="dataList" :total="total" :page.sync="query.page" :limit.sync="query.limit" :loading="loading" @getList="getList" :selection-change="handleSelectionChange">
            <el-table-column type="selection" width="70"></el-table-column>
            <el-table-column label="序号" type="index" align="center" width="100"></el-table-column>
            <el-table-column label="条件" align="center" prop="describe">
            </el-table-column>
            <el-table-column label="价格" align="center">
              <template slot-scope="{ row }">
                <div>¥{{ row.price }}</div>
              </template>
            </el-table-column>
            <el-table-column label="最后编辑人" align="center" prop="updateUserName"> </el-table-column>
            <el-table-column label="最后编辑时间" align="center">
              <template slot-scope="{ row }">
                <div>
                  {{ row.updateTime | formatDate }}
                </div>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center">
              <template slot-scope="{ row }">
                <yl-button type="text" @click="edit(row)">编辑</yl-button>
                <yl-button type="text" @click="delet(row)">删除</yl-button>
              </template>
            </el-table-column>
          </yl-table>
        </div>
      </div>
      <!-- 弹窗 -->
      <!-- 批量导入渠道类型弹框 -->
      <yl-dialog :title="title" :visible.sync="show" @confirm="submit" width="692px">
        <div class="common-box">
          <div class="search-box">
            <el-form :model="form" :rules="rules" ref="form" label-width="72px" label-position="top">
              <el-row>
                <el-col :span="24">
                  <el-form-item label="区域" prop="regionCode">
                    <yl-choose-address is-all :province.sync="form.provinceCode" :city.sync="form.cityCode" :area.sync="form.regionCode" />
                  </el-form-item>
                </el-col>
                <el-col :span="24">
                  <el-form-item label="企业类型" prop="type">
                    <el-select v-model="form.type" placeholder="请选择">
                      <el-option label="全部" :value="0"></el-option>
                      <el-option v-for="item in enterpriseType" :key="item.value" :label="item.label" :value="item.value">
                      </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="24">
                  <el-form-item label="限价价格" prop="price">
                    <el-input v-model="form.price" placeholder="请输入" />
                  </el-form-item>
                </el-col>
              </el-row>
            </el-form>
          </div>

        </div>
      </yl-dialog>
    </div>
  </div>
</template>

<script>
import { b2bProductDetail, b2bProSetPriceAdd, b2bProSetPriceDel, b2bProSetPriceList, b2bProSetPriceUpdate } from '@/subject/pop/api/b2b_api/b2b_products'
import { getCurrentUser } from '@/subject/pop/utils/auth'
import { goodsStatus, enterpriseType, goodsOutReason, goodsPatent } from '@/subject/pop/utils/busi'
import { ylChooseAddress } from '@/subject/pop/components'
export default {
  components: {
    ylChooseAddress
  },
  computed: {
    goodStatus() {
      return goodsStatus()
    },
    // 企业类型
    enterpriseType() {
      let arr = enterpriseType()
      let arrs = arr.filter(function (item) {
        return item.value !== 1 && item.value !== 2
      })
      return arrs;
    },
    goodsReason() {
      return goodsOutReason()
    },
    // 专利非专利
    patent() {
      return goodsPatent()
    },
    isYiLing() {
      let user = getCurrentUser()
      let flag = null;
      if (user.currentEnterpriseInfo) {
        if (user.currentEnterpriseInfo.yilingFlag) {
          flag = !!user.currentEnterpriseInfo.yilingFlag
        }
      }
      return flag
    }
  },
  data() {
    var isQtyVlidator = (rule, value, callback) => {
      // var reg = /^((?!-1)\d{0,10})$/;//最长9位数字，可修改
      var reg = /^(?!0+(?:\.0+)?$)(?:[1-9]\d*|0)(?:\.\d{1,2})?$/;//最长9位数字，可修改
      if (value) {
        if (!reg.test(value)) {
          return callback(new Error('请输入价格，最多可保留2位小数'))
        } else return callback()
      } else {
        return callback(new Error('请输入限价价格'))
      }

    }

    return {
      // 头部导航
      navList: [
        {
          title: '首页',
          path: '/b2b_dashboard'
        },
        {
          title: '商品管理',
          path: ''
        },
        {
          title: '商品列表',
          path: '/b2b_products/products_list'
        },
        {
          title: '限价详情页'
        }
      ],
      data: {
        standardGoodsAllInfo: {
          baseInfo: {
            specificationInfo: []
          }
        }
      },
      query: {
        page: 1,
        limit: 10,
        type: 0,
        provinceCode: '',
        cityCode: '',
        regionCode: ''
      },
      total: 0,
      dataList: [
      ],
      loading: false,
      show: false,
      form: {
        provinceCode: '',
        cityCode: '',
        regionCode: '',
        type: 0,
        price: '',
        id: ''
      },
      rules: {
        // regionCode: [{ required: true, message: '请选择区域', trigger: 'change' }],
        enterprise: [{ required: true, message: '请选择企业类型', trigger: 'change' }],
        price: [{ required: true, validator: isQtyVlidator, trigger: 'change' }]
      },
      // 表格选择
      multipleSelection: [],
      title: '新建条件',
      isAdd: 'add'

    }
  },
  mounted() {
    this.id = this.$route.params.id
    if (this.id) {
      this.id = parseFloat(this.id)
      this.getData()
      this.getList()
    }
    this.$log(this.$route)
  },
  methods: {
    add() {
      this.title = '新建条件'
      this.isAdd = 'add'
      this.form.provinceCode = ''
      this.form.cityCode = ''
      this.form.regionCode = ''
      this.form.type = 0
      this.form.price = ''
      this.form.id = ''
      this.show = true
    },
    edit(row) {
      this.title = '编辑条件'
      this.isAdd = 'edit'
      this.show = true
      this.form.provinceCode = row.provinceCode
      this.form.cityCode = row.cityCode
      this.form.regionCode = row.regionCode
      this.form.type = row.customerType
      this.form.price = row.price
      this.form.id = row.id
    },

    // 删除单个
    delet(row) {
      let _this = this;
      _this.$confirm('删除后将该条件消失无法对该条件用户限价，确定删除吗？', '删除', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          //确定
          _this.removeGroupMethod(row.id);
        })
        .catch(() => {
          //取消
        });
    },
    allDelet() {
      if (!this.multipleSelection.length > 0) return this.$message.warning('请选择需要批量删除的数据')
      this.removeGroupMethod()
    },
    // 表格全选
    handleSelectionChange(val) {
      this.multipleSelection = val;
      this.$log('this.multipleSelection:', val);
    },
    // 删除
    async removeGroupMethod(id) {
      let ids = []
      if (id) {
        ids.push(id)
      } else {
        for (let i = 0; i < this.multipleSelection.length; i++) {
          ids.push(this.multipleSelection[i].id)
        }
      }
      this.$common.showLoad()
      let data = await b2bProSetPriceDel(ids)
      this.$common.hideLoad()
      if (data !== undefined) {
        this.$common.n_success('删除成功')
        this.getList()
      }
    },
    handleSearch() {
      this.getList()
    },
    handleReset() {
      this.query = {
        page: 1,
        limit: 10,
        type: '',
        provinceCode: '',
        cityCode: '',
        regionCode: ''
      }
      this.getList();
    },
    // 新增，编辑   b2b后台控价模块-添加商品控价条件
    submit() {
      this.$refs['form'].validate(async (valid) => {
        if (valid) {
          let form = this.form
          this.$common.showLoad()
          let data = {}
          if (this.isAdd == 'edit') {
            data = await b2bProSetPriceUpdate(
              form.id,// 商品id
              form.type,
              form.provinceCode,
              form.cityCode,
              form.regionCode,
              form.price
            )
          } else {
            data = await b2bProSetPriceAdd(
              this.id,// 商品id
              form.type,
              form.provinceCode,
              form.cityCode,
              form.regionCode,
              form.price
            )
          }

          this.$common.hideLoad()
          if (data !== undefined) {
            this.show = false
            if (this.isAdd == 'add') {
              this.$common.n_success('新建成功')
            } else {
              this.$common.n_success('修改成功')
            }
            this.getList()
          }
        } else {
          return false
        }
      })
    },
    //  获取限价列表
    // b2b后台控价模块-商品限价条件列表
    async getList() {
      this.loading = true
      let query = this.query
      let data = await b2bProSetPriceList(
        query.page,
        query.limit,
        this.id,
        query.type,
        query.provinceCode,
        query.cityCode,
        query.regionCode
      )
      this.loading = false
      if (data !== undefined) {
        this.dataList = data.records
        this.total = data.total
      }
    },
    // 获取详情
    async getData() {
      this.$common.showLoad()
      let data = await b2bProductDetail(this.id)
      this.$common.hideLoad()
      console.log(data)
      if (data !== undefined) {
        this.data = data
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
<style lang="scss" scoped>
.top-bar {
  .el-form-item__label {
    font-weight: 400;
    color: $font-title-color;
  }
  .el-input {
    width: 320px;
  }
}
</style>
