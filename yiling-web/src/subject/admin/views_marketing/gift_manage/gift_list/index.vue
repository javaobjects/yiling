<template>
  <div class="app-container">
    <!-- 内容区 -->
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">商品名称</div>
              <el-input v-model="query.name" placeholder="请输入商品名称" @keyup.enter.native="handleSearch" />
            </el-col>
            <el-col :span="6">
              <div class="title">商品编号</div>
              <el-input v-model="query.id" placeholder="请输入商品编号" @keyup.enter.native="handleSearch" />
            </el-col>
            <el-col :span="6">
              <div class="title">所属业务</div>
              <el-select v-model="query.businessType" placeholder="请选择">
                <el-option label="全部" :value="0"></el-option>
                <el-option
                  v-for="item in businessTypeArray"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
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
      <!-- 导出按钮 -->
      <div class="down-box clearfix">
        <ylButton type="primary" @click="addClick">新增库存信息</ylButton>
      </div>
      <!-- 底部列表 -->
      <div class="mar-t-8 pad-b-10 order-table-view">
        <yl-table
          border
          show-header
          :list="dataList"
          :total="0"
          :page.sync="query.page"
          :limit.sync="query.limit"
          :loading="loading"
          @getList="getList"
        >
          <el-table-column label="商品编号" min-width="120" align="center" prop="id">
          </el-table-column>
          <el-table-column label="商品名称" min-width="100" align="center" prop="name">
          </el-table-column>
          <el-table-column label="类别" min-width="100" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.goodsType | dictLabel(goodsTypeArray) }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="数量单位" min-width="120" align="center" prop="unit">
          </el-table-column>
          <el-table-column label="品牌" min-width="200" align="center" prop="brand">
          </el-table-column>
          <el-table-column label="所属业务" min-width="200" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.businessType | dictLabel(businessTypeArray) }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="商品价值" min-width="200" align="center" prop="price">
          </el-table-column>
          <el-table-column label="安全库存" min-width="200" align="center" prop="safeQuantity">
          </el-table-column>
          <el-table-column label="可用库存" min-width="200" align="center" prop="availableQuantity">
          </el-table-column>
          <el-table-column label="总库存" min-width="200" align="center" prop="quantity">
          </el-table-column>
          <el-table-column label="操作" min-width="120" align="center" fixed="right">
            <template slot-scope="{ row }">
              <div class="operation-view">
                <div class="option">
                  <yl-button type="text" :disabled="row.joinActivityFlag" @click="editClick(row)">修改</yl-button>
                  <yl-button type="text" :disabled="row.joinActivityFlag" @click="deletClick(row)">删除</yl-button>
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
import {
  getGiftList,
  giftDelete
} from '@/subject/admin/api/view_marketing/gift_manage';

export default {
  name: 'GiftList',
  components: {
  },
  computed: {
  },
  data() {
    return {
      // 活动状态
      businessTypeArray: [
        {
          label: 'B2B',
          value: 1
        },
        {
          label: '2C',
          value: 2
        }
      ],
      goodsTypeArray: [
        {
          label: '真实物品',
          value: 1
        },
        {
          label: '虚拟物品',
          value: 2
        },
        {
          label: '优惠券',
          value: 3
        },
        {
          label: '会员',
          value: 4
        }
      ],
      loading: false,
      query: {
        id: '',
        name: '',
        total: 0,
        businessType: 0
      },
      dataList: [],
      // 停用弹框
      stopVisible: false,
      // 作废弹框
      disposeVisible: false
    };
  },
  activated() {
    this.getList()
  },
  methods: {
    async getList() {
      this.loading = true
      let query = this.query
      let data = await getGiftList(
        query.name,
        query.id,
        query.businessType
      );
      this.loading = false
      if (data) {
        this.dataList = data.list
        this.query.total = data.list.length || 0
      }
    },
    // 搜索点击
    handleSearch() {
      this.query.page = 1
      this.getList()
    },
    handleReset() {
      this.query = {
        page: 1,
        limit: 10,
        businessType: 0
      }
    },
    // 新增
    addClick() {
      this.$router.push({
        name: 'GiftEdit'
      });
    },
    // 创建促销活动
    editClick(row) {
      this.$router.push({
        name: 'GiftEdit',
        params: { id: row.id }
      });
    },
    // 作废点击
    deletClick(row) {
      this.$confirm('确认删除 ！', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      .then( async() => {
        //确定
        let data = await giftDelete(row.id)
        if (data!==undefined) {
          this.$common.n_success('删除成功!');
          this.getList();
        }
      })
      .catch(() => {
        //取消
      });
    }

  }
};
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
<style lang="scss">
  .order-table-view {
    .table-row {
      margin: 0 30px;
      td {
        .el-table__expand-icon{
          visibility: hidden;
        }
      }
    }
  }
</style>
