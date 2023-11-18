<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <div class="app-container-content has-bottom-bar">
      <div>
        <el-form :model="form" :rules="baseRules" ref="dataForm" label-position="left" label-width="130px" class="demo-ruleForm">
          <div class="top-bar">
            <div class="header-bar header-renative">
              <div class="sign"></div>基本信息
            </div>
            <div class="content-box my-form-box">
              <el-form-item label="商品组合名称:" prop="name">
                <el-input class="show-word-limit" v-model="form.name" maxlength="30" show-word-limit></el-input>
              </el-form-item>
              <el-form-item label="快速采购推荐位:" prop="quickPurchaseFlag">
                <el-radio-group v-model="form.quickPurchaseFlag">
                  <el-radio :label="0">是</el-radio>
                  <el-radio :label="1">否</el-radio>
                </el-radio-group>
              </el-form-item>
            </div>
            <div class="header-bar header-renative">
              <div class="sign"></div>商品信息
              <yl-button class="edit-btn" plain type="primary" @click="addGoodsClick">添加商品</yl-button>
            </div>
            <div class="content-box my-form-box">
              <yl-table
                border
                :show-header="true"
                :list="goodsLimitList"
                :total="0"
              >
                <el-table-column label="序号" min-width="55" align="center">
                  <template slot-scope="{ $index }">
                    <div class="font-size-base">{{ $index + 1 }}</div>
                  </template>
                </el-table-column>
                <el-table-column label="商品名称" min-width="162" align="center">
                  <template slot-scope="{ row }">
                    <div class="goods-desc">
                      <div class="font-size-base">{{ row.name }}</div>
                    </div>
                  </template>
                </el-table-column>
                <el-table-column label="批准文号" min-width="162" align="center">
                  <template slot-scope="{ row }">
                    <div class="goods-desc">
                      <div class="font-size-base">{{ row.licenseNo }}</div>
                    </div>
                  </template>
                </el-table-column>
                <el-table-column label="规格" min-width="100" align="center">
                  <template slot-scope="{ row }">
                    <div class="font-size-base">{{ row.sellSpecifications }}</div>
                  </template>
                </el-table-column>
                <el-table-column label="生产厂家" min-width="162" align="center">
                  <template slot-scope="{ row }">
                    <div class="goods-desc">
                      <div class="font-size-base">{{ row.manufacturer }}</div>
                    </div>
                  </template>
                </el-table-column>
                <el-table-column label="操作" align="center">
                  <template slot-scope="{ $index }">
                    <yl-button type="text" @click="goodsRemoveClick($index)">删除</yl-button>
                  </template>
                </el-table-column>
              </yl-table>
            </div>
          </div>
        </el-form>
      </div>
      <div class="flex-row-center bottom-view">
        <yl-button plain type="primary" @click="$router.go(-1)">返回</yl-button>
        <yl-button type="primary" @click="saveClick">保存</yl-button>
      </div>
    </div>
    <!-- 添加商品 -->
    <yl-dialog title="添加" :visible.sync="addGoodsDialog" width="966px" :show-footer="false">
      <div class="dialog-content-box-customer">
        <div class="dialog-content-top">
          <div class="search-box">
            <el-row class="box">
              <el-col :span="8">
                <div class="title">商品名称</div>
                <el-input v-model="goodsQuery.goodsName" placeholder="请输入商品名称" @keyup.enter.native="goodsHandleSearch" />
              </el-col>
              <el-col :span="8">
                <div class="title">批准文号</div>
                <el-input v-model="goodsQuery.licenseNo" placeholder="请输入批准文号" @keyup.enter.native="goodsHandleSearch" />
              </el-col>
              <el-col :span="8">
                <div class="title">生产厂家</div>
                <el-input v-model="goodsQuery.manufacturer" placeholder="请输入生产厂家" @keyup.enter.native="goodsHandleSearch" />
              </el-col>
            </el-row>
          </div>
          <div class="search-box">
            <el-row class="box">
              <el-col :span="16">
                <yl-search-btn :total="goodsTotal" @search="goodsHandleSearch" @reset="goodsHandleReset" />
              </el-col>
            </el-row>
          </div>
        </div>
        <div class="mar-t-10" style="padding-bottom: 10px;background: #FFFFFF;">
          <yl-table :stripe="true" :show-header="true" :list="goodsList" :total="goodsTotal" :page.sync="goodsQuery.page" :limit.sync="goodsQuery.limit" :loading="loading" @getList="getGoodsList">
            <el-table-column label="商品名称" min-width="162" align="center">
              <template slot-scope="{ row }">
                <div class="goods-desc">
                  <div class="font-size-base">{{ row.name }}</div>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="批准文号" min-width="162" align="center">
              <template slot-scope="{ row }">
                <div class="goods-desc">
                  <div class="font-size-base">{{ row.licenseNo }}</div>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="规格" min-width="100" align="center">
              <template slot-scope="{ row }">
                <div class="font-size-base">{{ row.sellSpecifications }}</div>
              </template>
            </el-table-column>
            <el-table-column label="生产厂家" min-width="162" align="center">
              <template slot-scope="{ row }">
                <div class="goods-desc">
                  <div class="font-size-base">{{ row.manufacturer }}</div>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="操作" min-width="65" align="center">
              <template slot-scope="{ row }">
                <div>
                  <yl-button class="view-btn" type="text" v-if="!row.isSelect" @click="goodsItemAddClick(row)">添加</yl-button>
                  <yl-button class="edit-btn" v-if="row.isSelect && row.goodsDisableVO.isAllowSelect != 1" disabled type="text">已添加</yl-button>
                </div>
              </template>
            </el-table-column>
          </yl-table>
        </div>
      </div>
    </yl-dialog>
  </div>
</template>

<script>
import { recommendGoodsGroupGetGroup, getGoodsPopList, recommendGoodsGroupSaveOrUpdate } from '@/subject/pop/api/products'

export default {
  components: {},
  computed: {
  },
  data() {
    return {
      // 头部导航
      navList: [
        {
          title: '首页',
          path: '/dashboard'
        },
        {
          title: '商品管理'
        },
        {
          title: '商品组合',
          path: '/products/products_group'
        },
        {
          title: '编辑'
        }
      ],
      id: '',
      form: {
        name: '',
        quickPurchaseFlag: 1
      },
      baseRules: {
        name: [{ required: true, message: '请输入商品组合名称', trigger: 'blur' }],
        quickPurchaseFlag: [{ required: true, message: '请选择快速采购推荐位', trigger: 'change' }]
      },
      // 添加商品弹框
      addGoodsDialog: false,
      loading: false,
      goodsQuery: {
        page: 1,
        limit: 10,
        goodsName: '',
        licenseNo: '',
        manufacturer: ''
      },
      goodsList: [],
      goodsTotal: 0,
      // 已选商品列表
      goodsLimitList: []
    };
  },
  mounted() {
    this.id = this.$route.params.id
    if (this.id) {
      this.getDetail()
    }
  },
  methods: {
    async getDetail() {
      this.$common.showLoad();
      let data = await recommendGoodsGroupGetGroup(this.id);
      this.$common.hideLoad();
      if (data) {
        this.form = {
          name: data.name,
          quickPurchaseFlag: data.quickPurchaseFlag
        }
        this.goodsLimitList = data.relationList
      }
    },
    // 添加商品
    addGoodsClick() {
      this.addGoodsDialog = true
      this.getGoodsList()
    },
    goodsHandleSearch() {
      this.goodsQuery.page = 1
      this.getGoodsList()
    },
    goodsHandleReset() {
      this.goodsQuery = {
        page: 1,
        limit: 10,
        goodsName: '',
        licenseNo: '',
        manufacturer: ''
      }
    },
    async getGoodsList() {
      this.loading = true
      let goodsQuery = this.goodsQuery
      let data = await getGoodsPopList(
        goodsQuery.page,
        goodsQuery.limit,
        goodsQuery.goodsName,
        goodsQuery.licenseNo,
        goodsQuery.manufacturer
      );
      this.loading = false
      if (data) {

        data.records.forEach(item => {
          let hasIndex = this.goodsLimitList.findIndex(obj => {
            return obj.goodsId == item.id;
          });
          if (hasIndex != -1) {
            item.isSelect = true;
          } else {
            item.isSelect = false;
          }
        });

        this.goodsList = data.records
        this.goodsTotal = data.total
      }
    },
    // 弹框每一项企业点击
    goodsItemAddClick(row) {
      let currentArr = this.goodsLimitList
      if (currentArr && currentArr.length == 10) {
        this.$common.warn('商品最多维护10个')
        return
      }
      row.goodsId = row.id
      currentArr.push(row)
      this.goodsLimitList = currentArr

      let arr = this.goodsList;
      arr.forEach(item => {
        let hasIndex = currentArr.findIndex(obj => {
          return obj.goodsId == item.id;
        });
        if (hasIndex != -1) {
          item.isSelect = true;
        } else {
          item.isSelect = false;
        }
      });
      this.goodsList = arr;
    },
    goodsRemoveClick(index) {
      let currentArr = this.goodsLimitList
      currentArr.splice(index, 1)
      this.goodsLimitList = currentArr
    },
    // 底部提交
    async saveClick() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {

          let form = this.form
          let relationList = []
          for (let i = 0; i < this.goodsLimitList.length; i++) {
            let goodsItem = this.goodsLimitList[i]
            let item = {
              goodsId: goodsItem.goodsId
            }
            relationList.push(item)
          }
          if (this.checkInputData()) {
            this.$common.showLoad()
            let data = await recommendGoodsGroupSaveOrUpdate(this.id, form.name, form.quickPurchaseFlag, relationList)
            this.$common.hideLoad()
            if (data) {
              this.$common.n_success('保存成功')
              this.$router.go(-1)
            }

          }

        } else {
          console.log('error submit!!');
          return false;
        }
      })

    },
    checkInputData() {
      if (!this.goodsLimitList || this.goodsLimitList.length == 0) {
        this.$common.warn('列表中至少选择一个商品')
        return false
      }
      return true
    }
  }
};
</script>

<style lang="scss" >
.my-form-box{
  .el-form-item{
    .el-form-item__label{
      color: $font-title-color; 
    }
    label{
      font-weight: 400 !important;
    }
  }
  .my-form-item-right{
    label{
      font-weight: 400 !important;
    }
  }
}
</style>
<style lang="scss" scoped>
@import './index.scss';
</style>
