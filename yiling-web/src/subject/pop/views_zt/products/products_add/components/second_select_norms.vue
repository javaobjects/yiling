
<template>
  <div class="common-box">
    <!-- 搜索一期不要 -->
    <!-- <div class="search-box" style="margin-top: 0;">
      <el-row class="box">
        <el-col :span="6">
          <div class="title">包装规格</div>
          <el-input v-model="query.name" placeholder="请输入规格名称" />
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
      <yl-tool-tip class="w-100">提示：规格库保存了大量药品规格信息，您可进行搜索选择使用，可节省录入成本。<br>
        若无搜索结果，请新增药品规格信息，质管人员会进行审核。审核通过后才可上架销售</yl-tool-tip>
    </div> -->
    <div class="flex-between">
      <div></div>
      <div>
        <span class="font-14">未找到规格？</span>
        <yl-button class="mar-l-16" type="text" @click="addNorms">新增规格</yl-button>
      </div>
    </div>

    <div class="my-table mar-t-16">
      <yl-table :show-header="true" stripe :list="dataList" :total="total" :page.sync="query.page" :limit.sync="query.limit" :loading="loading" :selection-change="handleSelectionChange" @getList="getList">
        <el-table-column label="包装规格" prop="sellSpecifications">
        </el-table-column>
        <el-table-column label="单位" align="center" prop="unit">
        </el-table-column>
        <el-table-column label="商品图片" align="center">
          <template slot-scope="{ row }">
            <div v-if="row.picInfoList && row.picInfoList.length>0" class="flex-wrap imgs">
              <el-image v-for="(img, index) in row.picInfoList" :key="index" fit="contain" class="img" :src="img.picUrl" />
            </div>
            <div v-else class="imgs">--</div>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center">
          <template slot-scope="{ row }">
            <div class="table-content">
              <yl-button type="text" @click="select(row.standardId,row.id)">选择</yl-button>
            </div>
          </template>
        </el-table-column>
      </yl-table>
    </div>
  </div>
</template>

<script>
import { getStandardProductsSpecificationList } from '@/subject/pop/api/zt_api/zt_products'
export default {
  props: {
    // 获取的商品id
    selectGoodsId: {
      type: Number,
      default: () => { }
    }
  },
  data() {
    return {
      query: {
        page: 1,
        limit: 10,
        name: ''
      },
      dataList: [],
      loading: false,
      total: 0

    }
  },
  watch: {
    selectGoodsId: {
      handler(newVal, oldVal) {
        console.log(newVal);
        if (newVal != -1) {
          this.getList();
        }
      },
      deep: true
    }
  },
  created() {
  },
  mounted() {
  },
  methods: {
    select(standardId, id) {
      this.$emit('change', standardId, id)
    },
    handleSelectionChange() {

    },
    editAndDetail() {

    },
    handleSearch() {

    },
    handleReset() {

    },
    async getList() {
      let data = await getStandardProductsSpecificationList(this.selectGoodsId)
      console.log(data);
      if (data !== undefined) {
        this.dataList = data.list
      }
    },
    addNorms() {
      this.$router.push({
        path: '/zt_products/create_products/' + this.selectGoodsId,
        query: {
          type: 'addNorms'
        }
      })
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
    margin-top: 16px;
    padding: 0 16px 0 8px;
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
  // padding-bottom: 8px;
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
.imgs {
  display: flex;
  align-items: center;
  justify-content: center;
  .img {
    width: 100px;
    height: 100px;
    // margin-left: 8px;
    margin-right: 16px;
    border-radius: 4px;
    border: 1px solid #d8d8d8;
  }
}
</style>