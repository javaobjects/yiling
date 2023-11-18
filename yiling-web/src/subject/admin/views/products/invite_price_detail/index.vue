<template>
  <div class="app-container">
    <div class="app-container-content">
      <div class="expand-row">
        <div class="expand-view">
          <div class="title">{{ data.name }}</div>
          <div class="desc-box flex-row-left">
            <el-image fit="contain" class="img" :src="data.pic"></el-image>
            <div class="right-text box1">
              <div class="text-item"><span class="font-title-color">生产厂家：</span>{{ data.manufacturer }}</div>
              <div class="text-item"><span class="font-title-color">批准文号：</span>{{ data.licenseNo }}</div>
              <div class="text-item"><span class="font-title-color">规格/型号：</span>{{ data.specifications }}</div>
            </div>
          </div>
        </div>
      </div>

      <div class="down-box font-important-color">
        已设置{{ data.locationCount }}省份
      </div>

      <div>
        <yl-table border :show-header="true" :list="data.locationPriceList">
          <el-table-column align="center" min-width="120" label="省份" prop="locationName">
          </el-table-column>
          <el-table-column align="center" min-width="120" label="招标挂网价" prop="price">
            <template slot-scope="{ row }">
              <span>{{ row.price | toThousand('￥') }}</span>
            </template>
          </el-table-column>
          <el-table-column fixed="right" align="center" label="操作" width="120">
            <template slot-scope="{ row }">
              <yl-button type="text" @click="edit(row)">编辑</yl-button>
              <yl-button type="text" @click="reset(row)">重置</yl-button>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </div>
</template>

<script>
import { getInviteDetail, editInvitePrice } from '@/subject/admin/api/products'
import {
  validateIsNumZ,
  validateNumFloatLength
} from '@/subject/admin/utils/validate'

export default {
  name: 'InvitePriceDetail',
  components: {
  },
  computed: {
  },
  data() {
    return {
      data: {}
    }
  },
  mounted() {
    this.id = this.$route.params.id
    if (this.id) {
      this.getDetail()
    }
  },
  methods: {
    async getDetail() {
      this.$common.showLoad()
      let data = await getInviteDetail(this.id)
      this.$common.hideLoad()
      if (data) {
        this.data = data
      }
    },
    // 编辑
    edit(row) {
      this.$prompt(`设置${row.locationName}的招标挂网价为`, '设置招标挂网价', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPlaceholder: '请输入价格',
        inputValidator: (value) => {
          if (validateIsNumZ(value)) {
            if (validateNumFloatLength(value, 2)) {
              return '仅支持输入2位小数'
            } else {
              return true
            }
          } else {
            return '请输入正确的价格'
          }
        },
        inputErrorMessage: '请输入价格'
      }).then(async ({ value }) => {
        this.$common.showLoad()
        let data = await editInvitePrice(this.data.id, value, row.locationCode)
        this.$common.hideLoad()
        if (data && data.result) {
          this.$common.n_success('设置招标挂网价成功')
          this.getDetail()
        }
      }).catch(err => { })
    },
    // 干掉价格
    async reset(row) {
      this.$common.showLoad()
      let data = await editInvitePrice(this.data.id, -1, row.locationCode)
      this.$common.hideLoad()
      if (data && data.result) {
        this.$common.n_success('重置成功')
        this.getDetail()
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
