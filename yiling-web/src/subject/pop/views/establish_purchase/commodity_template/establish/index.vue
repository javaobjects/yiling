<template>
  <div class="app-container">
    <!-- 内容区 -->
    <div class="app-container-content has-bottom-bar">
      <el-form 
        :model="model" 
        :rules="rules" 
        ref="dataForm" 
        inline-message 
        label-width="110px" 
        class="demo-ruleForm">
        <div class="top-box">
          <div class="flex-row-left item">
            <div class="line-view"></div>
            <span class="font-size-lg bold">基本信息</span>
          </div>
          <el-row class="box">
            <el-col :span="12">
              <el-form-item label="模板编号：">
                <el-input v-model="model.templateNumber" disabled/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="模板名称：" prop="templateName">
                <el-input
                  type="textarea"
                  maxlength="50" 
                  show-word-limit
                  style="max-width: 90%"
                  :autosize="{ minRows: 2, maxRows: 2}"
                  placeholder="请输入来源"
                  v-model.trim="model.templateName">
                </el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <div class="button-right">
            <yl-button type="primary" @click="saveClick">保存</yl-button>
          </div>
        </div>
      </el-form>
      <div class="top-box">
        <div class="flex-row-left item">
          <div class="line-view"></div>
          <span class="font-size-lg bold">商品信息</span>
        </div>
        <el-tabs v-model="activeName" @tab-click="handleClick">
          <el-tab-pane v-for="(item, index) in tabData" :key="index" :label="item.name" :name="item.id.toString()"></el-tab-pane>
        </el-tabs>
        <div class="button-right">
          <yl-button type="primary" plain @click="zkClick">批量设置折扣</yl-button>
          <yl-button type="primary" plain @click="handleAddGoods">添加可采商品</yl-button>
        </div>
        <div class="mar-t-8"> 
          <yl-table
            ref="multipleTable"
            :stripe="true"
            :show-header="true"
            :list="dataList"
            :loading="loading"
            :selection-change="handleSelectionChange">
            <el-table-column type="selection" align="center" width="70"></el-table-column>
            <el-table-column label="专利类型" min-width="100" align="center">
              <template slot-scope="{ row }">
              {{ row.isPatent == 1 ? '非专利' : '专利' }}
            </template>
            </el-table-column>
            <el-table-column label="商品名" min-width="120" align="center" prop="goodsName"></el-table-column>
            <el-table-column label="商品规格" min-width="100" align="center" prop="sellSpecifications"></el-table-column>
            <el-table-column label="批准文号" min-width="100" align="center" prop="licenseNo"></el-table-column>
            <el-table-column label="商品优惠折扣%" min-width="100" align="center" prop="rebate"></el-table-column>
            <el-table-column label="操作" min-width="90" align="center" fixed="right">
              <template slot-scope="{ row }">
                <yl-button type="text" @click="deleteClick(row)">移除</yl-button>
              </template>
            </el-table-column>
          </yl-table>
        </div>
      </div>
    </div>
    <div class="bottom-bar-view flex-row-center">
      <yl-button plain type="primary" @click="$router.go(-1)">返回</yl-button>
    </div>
    <!-- 批量设置折扣弹窗 -->
    <yl-dialog 
      title="批量设置折扣" 
      :show-footer="true" 
      @confirm="zkConfirm"
      width="480px" 
      :visible.sync="zkDialog">
      <div class="dialogTc">
        商品优惠比例：
        <el-input v-model="proportion" @keyup.native="proportion = onInput(proportion, 2)"/>
        %
      </div>
    </yl-dialog>
    <!-- 选择商品 -->
    <choose-product
      ref="choose" 
      :template-id="model.id"
      :factory-eid="activeName"
      :choose-list="dataList" 
      @selectItem="selectItem"/>
  </div>
</template>

<script>
import ChooseProduct from '../component/ChooseProduct.vue'
import { 
  saveTemplate, 
  mainPartList,
  queryTemplateGoodsList,
  updateTemplateGoodsRebate,
  deleteTemplateGoods,
  queryTemplateInfo
} from '@/subject/pop/api/establish_purchase'
import { onInputLimit } from '@/common/utils'
export default {
  components: {
    ChooseProduct
  },
  data() {
    return {
      model: {
        id: '',
        templateName: '',
        templateNumber: ''
      },
      rules: {
        templateName: [{ required: true, message: '请输入模板名称', trigger: 'blur' }]
      },
      activeName: '',
      tabData: [],
      dataList: [],
      loading: false,
      zkDialog: false,
      proportion: '',
      //添加可采商品
      relationId: '',
      factoryEid: '',
      //批量选中的 商品id
      idList: []
    }
  },
  created() {
    //查询商品信息头部切换
    this.tabListApi();
  },
  watch: {
    activeName: {
      handler(newValue, oldValue) {
        if (newValue && newValue != 0 && this.model.id != '') {
          this.getList()
        }
      },
      deep: true
    }
  },
  mounted() {
    let query = this.$route.params;
    if (query.id && query.id != '' && query.id != 0) {
      this.model.id = query.id;
      //获取基本信息
      this.getData()
    }
  },
  methods: {
    //获取基本信息
    async getData () {
      let data = await queryTemplateInfo(
        this.model.id
      )
      if (data) {
        this.model.templateName = data.templateName;
        this.model.templateNumber = data.templateNumber;
      }
    },
    //查询商品信息头部切换
    async tabListApi() {
      let data = await mainPartList()
      if (data) {
        if (data && data.length > 0) {
          this.$nextTick(()=> {
            this.activeName = data[0].id.toString()
          })
        }
        this.tabData = data
      }
    },
    async getList() {
      this.loading = true;
      let data = await queryTemplateGoodsList(
        this.model.id,
        this.activeName
      )
      if (data) {
        this.dataList = data;
      }
      this.loading = false;
    },
    handleClick(tab, event) {
      console.log(tab);
    },
    handleSelectionChange(array) {
      this.idList = []
      array.forEach(element => {
        this.idList.push(element.id)
      });
    },
    //点击批量设置折扣
    zkClick() {
      if (this.idList && this.idList.length > 0) {
        this.proportion = ''
        this.zkDialog = true;
      } else {
        this.$common.warn('请先选择商品！')
      }
    },
    //点击保存 基本信息
    saveClick() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          let model = this.model;
          this.$common.showLoad();
          let data = await saveTemplate(
            model.id,
            model.templateName
          )
          this.$common.hideLoad();
          if (data !== undefined) {
            this.model.id = data;
            this.getData()
            this.$common.n_success('保存成功!')
          }
        }
      })
    },
    async zkConfirm() {
      this.$common.showLoad();
      let data = await updateTemplateGoodsRebate(
        this.idList,
        this.proportion
      )
      this.$common.hideLoad();
      if (data !== undefined) {
        this.$common.n_success('保存成功!')
        this.zkDialog = false;
        this.getList()
      }
    },
    //添加可采商品
    handleAddGoods() {
      if (this.model.id) {
        this.$refs.choose.openGoodsDialog()
      } else {
        this.$common.warn('请先保存基本信息！')
      }
    },
    //点击移除
    deleteClick(row) {
      this.$confirm(`确定移除商品 ${row.goodsName}？`, '温馨提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning'
      })
      .then(async () => {
        // 确认删除
        this.$common.showLoad()
        let data = await deleteTemplateGoods(row.id)
        this.$common.hideLoad()
        if (data !== undefined) {
          this.$common.n_success('操作成功')
          this.getList()
        }
      })
      .catch(async () => {
        // 取消
      })
    },
    //可采商品返回
    selectItem() {
      this.getList()
    },
    onInput(value, limit) {
      let val = onInputLimit(value, limit)
      if (val < 100) {
        return val
      } else {
        this.$common.warn('请输入优惠比例小于100并不超过两位小数！')
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>