<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <div class="app-container-content has-bottom-bar">
      <div class="c-box">
        <el-form 
          :model="form" 
          :rules="rules" 
          ref="dataForm" 
          label-width="100px" 
          class="demo-ruleForm">
          <h4>基础信息：</h4>
          <el-form-item label="楼层名称：" prop="name">
            <el-input 
              v-model.trim="form.name" 
              maxlength="10" 
              show-word-limit 
              placeholder="请输入楼层名称">
            </el-input>
          </el-form-item>
          <el-form-item label="排序：" prop="sort">
            <el-input v-model="form.sort" style="width: 100px;"
              @input="e => (form.sort = checkInput(e))" placeholder="排序"
            ></el-input>
            <span class="font-size-base font-light-color" style="margin-left:20px;">排序逻辑 1-200，200权重最高，权重一样时候，将按照时间排序</span>
          </el-form-item>
          <el-form-item label="楼层状态：" prop="status">
            <el-radio-group v-model="form.status">
              <el-radio :label="1">启用</el-radio>
              <el-radio :label="2">停用</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-form>
      </div>
      <div class="c-box">
        <h4>楼层商品列表：</h4>
        <div class="down-box">
          <yl-button type="primary" @click="addClick">添加商品</yl-button>
        </div>
        <div class="my-table mar-t-8">
          <yl-table 
            :show-header="true" 
            stripe 
            :list="dataList" 
            :loading="loading" 
            @getList="getFloorGoodsPage">
              <el-table-column label="商品信息" min-width="180" align="center">
                <template slot-scope="{ row }">
                  <div class="products-box">
                    <el-image class="my-image" :src="row.pic" fit="contain"/>
                    <div class="products-info">
                      <div>商品：{{ row.goodsId }}</div>
                      <div>{{ row.name }}</div>
                      <div>{{ row.sellSpecifications }}</div>
                      <div>{{ row.licenseNo }}</div>
                      <div>{{ row.manufacturer }}</div>
                    </div>
                  </div>
                </template>
              </el-table-column>
              <el-table-column label="价格" min-width="80" align="center">
                <template slot-scope="{ row }">
                  <div class="table-content">￥{{ row.price }}</div>
                </template>
              </el-table-column>
              <el-table-column label="库存信息" min-width="70" align="center">
                <template slot-scope="{ row }">
                  <div class="table-content">
                    <yl-button type="text" @click="stockDetail(row)">库存详情</yl-button>
                  </div>
                </template>
              </el-table-column>
              <el-table-column label="排序值" min-width="90" align="center">
                <template slot-scope="{ row }">
                  <el-input
                    size="mini" 
                    v-model="row.sort" 
                    @input="e => (row.sort = checkInput(e))" 
                    style="width: 80px" 
                    placeholder="请输入" 
                    maxlength="10">
                  </el-input>
                </template>
              </el-table-column>
              <el-table-column label="操作" min-width="80" align="center">
                <template slot-scope="{ row, $index }">
                  <yl-button type="text" @click="deleteClick(row, $index)">删除</yl-button>
                </template>
              </el-table-column>
          </yl-table>
        </div>
      </div>
    </div>
    <div class="bottom-view flex-row-center">
      <yl-button type="primary" plain @click="$router.go(-1)">返回</yl-button>
      <yl-button type="primary" @click="saveClick">保存</yl-button>
    </div>
    <!-- 添加商品弹窗 -->
    <addProduct 
      :data-list="dataList"
      :show.sync="productShow"
      @confirmChange="confirmChange"
      v-if="productShow">
    </addProduct>
    <!-- 库存弹窗 -->
    <yl-dialog title="查看库存信息" :visible.sync="showDialog" :show-footer="false">
      <div class="pad-16">
        <div>
          <span class="font-title-color">商品名称：</span>
          <span class="font-important-color">{{ dialog.name }}</span>
          <span class="font-title-color mar-l-32">商品ID：</span>
          <span class="font-important-color">{{ dialog.goodsId }}</span>
        </div>
        <div class="mar-t-24">
          <yl-table 
            :show-header="true" 
            stripe 
            border 
            :list="stockDetailList">
            <el-table-column label="ERP内码" align="center" prop="inSn"></el-table-column>
            <el-table-column label="ERP编码" align="center" prop="sn"></el-table-column>
            <el-table-column label="包装数量" align="center" prop="packageNumber"></el-table-column>
            <el-table-column label="总库存" align="center" prop="qty"></el-table-column>
            <el-table-column label="占有库存" align="center" prop="frozenQty"></el-table-column>
            <el-table-column label="可售库存" align="center">
              <template slot-scope="{ row }">
                <div>{{ row.qty - row.frozenQty }}</div>
              </template>
            </el-table-column>
          </yl-table>
        </div>
      </div>
    </yl-dialog>
  </div>
</template>

<script>
import addProduct from '../components/addProduct'
import { floorSave, getFloor, queryFloorGoodsList } from '@/subject/pop/api/b2b_api/floor_manage'
export default {
  name: 'FloorManageEstablish',
  components: {
    addProduct
  },
  data() {
    return {
      // 头部导航
      navList: [
        {
          title: '首页',
          path: '/b2b_dashboard'
        },
        {
          title: '楼层管理',
          path: '/b2b_store/floor_manage'
        },
        {
          title: '新建/编辑楼层'
        }
      ],
      form: {
        id: '',
        name: '',
        sort: '',
        status: 1
      },
      rules: {
        name: [{ required: true, message: '请输入楼层名称', trigger: 'blur' }],
        sort: [{ required: true, message: '请输入排序', trigger: 'blur' }],
        status: [{ required: true, message: '请选择楼层状态', trigger: 'change' }]
      },
      dataList: [],
      loading: false,
      //添加商品弹窗
      productShow: false,
      //库存详情弹窗
      showDialog: false,
      dialog: {
        name: '',
        id: ''
      },
      stockDetailList: []
    }
  },
  mounted() {
    let query = this.$route.params;
    if (query.id) {
      this.getData(query.id)
      this.getFloorGoodsPage(query.id)
    }
  },
  methods: {
    //获取数据
    async getData(id) {
      let data = await getFloor(
        id
      )
      if (data) {
        this.form = data
      }
    },
    //获取楼层商品数据
    async getFloorGoodsPage(id) {
      this.loading = true;
      let data = await queryFloorGoodsList(
        id
      )
      if (data) {
        this.dataList = data.list
      }
      this.loading = false
    },
    //点击保存
    saveClick() {
      this.$refs['dataForm'].validate( async(valid) => {
        if (valid) {
          if (this.dataList && this.dataList.length > 0) {
            //商品信息 数据
            let commodityData = [];
            for (let i = 0; i < this.dataList.length; i ++) {
              commodityData.push({
                goodsId: this.dataList[i].goodsId,
                sort: this.dataList[i].sort ? parseInt(this.dataList[i].sort) : 1
              })
            }
            let form = this.form;
            this.$common.showLoad();
            let data = await floorSave(
              form.id,
              form.name,
              form.sort,
              form.status,
              commodityData
            )
            this.$common.hideLoad();
            if (data !== undefined) {
              this.$common.alert('保存成功', r => {
                this.$router.go(-1)
              })
            }
          } else {
            this.$common.warn('请添加商品！')
          }
        }
      })
    },
    //点击添加商品
    addClick() {
      this.productShow = true
    },
    //点击删除
    deleteClick(row, index) {
      this.$confirm(`确认删除 ${ row.name } ！`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      .then(() => {
        this.dataList.splice(index, 1)
      })
      .catch(() => {
        //取消
      });
    },
    //传递过来的商品数据
    confirmChange(val) {
      console.log(val, 999)
      for (let i = 0; i < val.length; i ++) {
        this.dataList.push({
          ...val[i],
          goodsId: val[i].id,
          sort: val[i].sort ? val[i].sort : 1
        })
      }
      this.productShow = false;
    },
    //库存信息
    stockDetail(row) {
      this.dialog = row
      this.stockDetailList = row.goodsSkuList
      this.showDialog = true
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