<template>
  <div class="common-box">
    <div class="search-box" style="margin-top: 0;">
      <el-row class="box">
        <el-col :span="6">
          <div class="title">商品名称</div>
          <el-input v-model="query.name" placeholder="请输入商品名称" />
        </el-col>
        <el-col :span="6">
          <div class="title">批准文号/注册证编号</div>
          <el-input v-model="query.licenseNo" placeholder="请填写唯一编号" />
        </el-col>
        <el-col :span="6">
          <div class="title">生产厂家</div>
          <el-input v-model="query.manufacturer" placeholder="请输入生产厂家" />
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
      <yl-tool-tip class="hint-info">提示：标准库保存了大量药品信息，您可使用批准文号/生产许可证号/备案凭证编号/注册证编号或药品名称，生产厂家，进行搜索选择使用，可节省录入成本。<br>
        若无搜索结果，请新增药品，质管人员会进行审核。审核通过后才可上架销售。</yl-tool-tip>
    </div>
    <div class="flex-between mar-t-16">
      <div></div>
      <div>
        <span class="font-14">未找到商品？</span>
        <yl-button class="mar-l-16" type="text" @click="add">新增商品</yl-button>
      </div>
    </div>

    <div class="my-table mar-t-16">
      <yl-table 
        :show-header="true" 
        stripe 
        :list="dataList" 
        :total="total" 
        :page.sync="query.page" 
        :limit.sync="query.limit" 
        :loading="loading" 
        :selection-change="handleSelectionChange" 
        @getList="getList">
        <el-table-column label="商品信息" prop="id">
          <template slot-scope="{ row }">
            <div>
              <div class="products-box">
                <el-image class="my-image" :src="row.picUrl" fit="contain" />
                <div class="products-info">
                  <div>{{ row.name }}</div>
                  <div>{{ row.licenseNo }}</div>
                  <div>{{ row.manufacturer }}</div>
                </div>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="规格信息" align="center">
          <template slot-scope="{ row }">
            <div class="table-content">包含{{ row.specificationsCount }}个规格</div>
          </template>
        </el-table-column>

        <el-table-column label="操作" align="center">
          <template slot-scope="{ row }">
            <div class="table-content">
              <yl-button type="text" @click="select(row.id)">选择</yl-button>
            </div>
          </template>
        </el-table-column>
      </yl-table>
    </div>

    <!--  新增弹窗 -->
    <!-- 审核详情弹窗 -->
    <yl-dialog title="新增商品" :visible.sync="show" @confirm="confirm">
      <div class="pad-16">
        <el-form 
          :model="form" 
          ref="form" 
          :rules="rules" 
          label-width="120px" 
          :inline="false" 
          size="normal">
          <el-form-item label="录入类型：" prop="goodsType">
            <el-select v-model="form.goodsType" placeholder="请选择录入类型">
              <el-option v-for="item in standardGoodsType" :key="item.value" :label="item.label" :value="item.value"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="商品类型：" prop="isCn">
            <el-radio-group v-model="form.isCn">
              <el-radio v-for="item in standardGoodsIsCn" :key="item.value" :label="item.value">
                {{ item.label }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
        </el-form>
      </div>
    </yl-dialog>
  </div>
</template>

<script>
import { getStandardProductsList } from '@/subject/pop/api/zt_api/zt_products'
import { standardGoodsType, standardGoodsIsCn } from '@/subject/pop/utils/busi'
export default {
  computed: {
    // 录入类型， 药品类型
    standardGoodsType() {
      return standardGoodsType()
    },
    // 是否国产
    standardGoodsIsCn() {
      return standardGoodsIsCn()
    }
  },
  data() {
    return {
      query: {
        page: 1,
        limit: 10,
        name: '',
        licenseNo: '',
        manufacturer: ''
      },
      loading: false,
      total: 100,
      dataList: [
        { id: 1, price: 100, goodsStatus: 1 },
        { id: 2, price: 100, goodsStatus: 2 },
        { id: 3, price: 100, goodsStatus: 3 },
        { id: 4, price: 100, goodsStatus: 6 }
      ],
      form: {
        isCn: '',
        goodsType: ''
      },
      rules: {
        isCn: [{ required: true, message: '请选择录入类型', trigger: 'change' }],
        goodsType: [{ required: true, message: '请选择商品类型', trigger: 'change' }]
      },
      show: false
    }
  },
  mounted() {
    this.getList()
  },
  methods: {
    // 选择的标准id
    select(id) {
      this.$emit('change', id)
    },
    add() {
      this.form = {
        isCn: '',
        goodsType: ''
      }
      this.show = true
    },
    confirm() {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          this.show = false
          this.$router.push({
            path: '/zt_products/create_products/-1',
            query: {
              type: 'add',
              goodsType: this.form.goodsType,
              isCn: this.form.isCn
            }
            // path: `/zt_products/products_again/0`,
          })
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    handleSelectionChange() {

    },
    async getList() {
      this.loading = true
      let data = await getStandardProductsList(
        this.query.page,
        this.query.limit,
        this.query.name,
        this.query.licenseNo,
        this.query.manufacturer
      )
      this.loading = false
      if (data !== undefined) {
        this.dataList = data.records
        this.total = data.total
      }
    },
    handleSearch() {
      this.query.page = 1
      this.getList()
    },
    handleReset() {
      this.query = {
        page: 1,
        limit: 10,
        name: '',
        licenseNo: '',
        manufacturer: ''
      }
    }

  }
}
</script>
<style lang="scss" scoped>
.common-box {
  .el-input {
    width: 260px;
  }
  .search-box {
    margin-top: 24px;
    padding: 0 16px 0 0px;
    position: relative;
    .box {
      .title {
        font-size: $font-size-large;
        line-height: $font-size-large-lh;
        color: $font-important-color;
        margin-bottom: 8px;
      }
    }
    .el-select {
      width: 260px;
    }

    .bottom-btn {
      position: absolute;
      bottom: 0;
      right: 0;
    }
  }
}
.my-table {
  .my-image {
    width: 80px;
    height: 80px;
  }

  .pad-b-8 {
    padding-bottom: 8px;
  }
  .switch {
    margin-top: 14px;
  }
  ::v-deep .el-table .cell {
    overflow: auto;
  }
}
.products-box {
  position: relative;
  display: flex;
  padding-bottom: 8px;
}
.products-info {
  margin-left: 17px;
}
.products-id {
  position: absolute;
  top: 5px;
  left: 0;
}
.table-content {
  display: flex;
  align-items: center;
  justify-content: center;
  padding-bottom: 8px;
}

.color-666 {
  color: #666;
}
.color-green {
  color: #52c41a;
}
.color-red {
  color: #e62412;
}
.mar-l-32 {
  margin-left: 32px;
}
.mar-t-24 {
  margin-top: 24px;
}

.line-content {
  height: 140px;
}
.w-100 {
  width: 100%;
}
.font-14 {
  font-size: 14px;
}
.hint-info {
  width: 100%;
}
</style>