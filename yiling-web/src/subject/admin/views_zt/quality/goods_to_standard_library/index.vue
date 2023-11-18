<template>
  <div class="app-container">
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="12">
              <div class="title">商品名称</div>
              <el-input v-model="query.name" @keyup.enter.native="searchEnter" placeholder="商品名称" />
            </el-col>
            <el-col :span="12">
              <div class="title">批准文号/生产许可证/备案凭证编号/注册证编号</div>
              <el-input v-model="query.licenseNo" @keyup.enter.native="searchEnter" placeholder="批准文号/生产许可证/备案凭证编号/注册证编号" />
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="12">
              <div class="title">供应商</div>
              <el-input v-model="query.ename" @keyup.enter.native="searchEnter" placeholder="供应商" />
            </el-col>
            <el-col :span="12">
              <div class="title">生产厂家</div>
              <el-input v-model="query.manufacturer" @keyup.enter.native="searchEnter" placeholder="生产厂家" />
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">数据来源</div>
              <el-select v-model="query.source" placeholder="数据来源">
                <el-option label="全部" :value="0"></el-option>
                <el-option v-for="item in goodsAuditSource" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="24">
              <yl-search-btn :total="query.total" @search="handleSearch" @reset="handleReset" />
            </el-col>
          </el-row>
        </div>
      </div>
      <div class="search-bar">
        <div class="header-bar">
            <div class="sign"></div>数据列表
        </div>
        <div class="down-box">
          <div class="btn">
            <el-link class="mar-r-10" type="primary" :underline="false" :href="'NO_17' | template">
              下载合并模板
            </el-link>
            <el-link class="mar-r-10" type="primary" :underline="false" @click="goImport">规格合并清洗</el-link>
          </div>
        </div>
        <div>
          <yl-table 
            border 
            :show-header="true" 
            :list="dataList" 
            :total="query.total" 
            :page.sync="query.page" 
            :limit.sync="query.limit" 
            :loading="loading" 
            @getList="getList">
            <el-table-column align="center" label="#" type="index" width="100">
              <template slot-scope="scope">
                <span>{{ (query.page - 1) * query.limit + scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column align="center" min-width="280" label="商品名称" prop="name"></el-table-column>
            <el-table-column align="center" min-width="200" label="批准文号/生产许可证/备案凭证编号/注册证编号" prop="licenseNo"></el-table-column>
            <el-table-column align="center" min-width="200" label="包含规格">
              <template slot-scope="{ row }">
                <div>
                  {{ row.specifications }}
                </div>
              </template>
            </el-table-column>
            <el-table-column align="center" min-width="260" label="生产厂家" prop="manufacturer"></el-table-column>
            <el-table-column align="center" min-width="260" label="生产地址" prop="manufacturerAddress"></el-table-column>
            <el-table-column align="center" min-width="200" label="供应商/药店" prop="ename"></el-table-column>
            <el-table-column align="center" min-width="200" label="申请时间" prop="createTime">
              <template slot-scope="{ row }">
                <div>{{ row.createTime | formatDate }}</div>
              </template>

            </el-table-column>
            <el-table-column align="center" min-width="200" label="数据来源" prop="source">
              <template slot-scope="{ row }">
                <div>{{ row.source | filterSource }}</div>
              </template>
            </el-table-column>
            <el-table-column fixed="right" align="center" label="操作" min-width="200">
              <template slot-scope="{ row }">
                <div>
                  <yl-button type="text" @click="matching(row)">匹配</yl-button>
                  <yl-button type="text" @click="reject(row)">商品驳回</yl-button>
                </div>
              </template>
            </el-table-column>
          </yl-table>
        </div>

      </div>

      <!-- 匹配弹窗 -->
      <yl-dialog 
        :visible.sync="show" 
        width="1152px" 
        title="包装规格" 
        @confirm="confirm" 
        :show-footer="false">
        <div class="dialog-content">
          <el-row class="normls">
            <el-col :span="12">
              <div class="normls-title">
                <div>待匹配商品</div>
                <div>
                  <p>商品名称：{{ goodsAuditInfoVO.name ? goodsAuditInfoVO.name : '' }}</p>
                  <p>生产厂家：{{ goodsAuditInfoVO.manufacturer ? goodsAuditInfoVO.manufacturer : '' }}</p>
                  <p>生产地址：{{ goodsAuditInfoVO.manufacturerAddress ? goodsAuditInfoVO.manufacturerAddress : '' }}</p>
                  <p>批准文号/生产许可证/备案凭证编号/注册证编号：{{ goodsAuditInfoVO.licenseNo ? goodsAuditInfoVO.licenseNo : '' }}</p>
                </div>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="normls-title">
                <div class="normls-title-s">当前已选择商品</div>
                <div>
                  <p>商品名称：{{ selcetGoodsObj.name }}</p>
                  <p>生产厂家：{{ selcetGoodsObj.manufacturer }}</p>
                  <p>生产地址：{{ selcetGoodsObj.manufacturerAddress }}</p>
                  <p>批准文号/生产许可证/备案凭证编号/注册证编号：{{ selcetGoodsObj.licenseNo }}</p>
                </div>
              </div>
            </el-col>
          </el-row>

          <el-form 
            :model="form" 
            :rules="rules" 
            ref="form" 
            label-width="87px" 
            label-position="right" 
            class="mar-tb-10">
            <!-- 未匹配到显示 -->
            <el-row v-show="showType === 1">
              <el-col :span="8">
                <el-form-item label="商品名称">
                  <el-input v-model="form.name" placeholder="商品名称" clearable />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="批准文号/生产许可证/备案凭证编号/注册证编号" label-width="173px" class="txtLine">
                  <el-input v-model="form.licenseNo" placeholder="批准文号/生产许可证/备案凭证编号/注册证编号" clearable />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="生产厂家">
                  <el-input v-model="form.manufacturer" placeholder="生产厂家" clearable />
                </el-form-item>
              </el-col>
            </el-row>
            <!-- 未匹配到显示  end-->
            <!-- 匹配到显示 -->
            <el-row v-show="showType === 2">
              <el-col :span="8">
                <el-form-item label="包装规格" prop="sellSpecifications">
                  <el-input v-model="form.sellSpecifications" placeholder="请输入包装规格" />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="单位" prop="unit">
                  <el-input v-model="form.unit" placeholder="请输入单位" />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="条形码" prop="barcode">
                  <el-input v-model="form.barcode" placeholder="请输入无或13位以内的数字" />
                </el-form-item>
              </el-col>
            </el-row>
            <div class="spce">
              <div></div>
              <yl-button class="mar-l-16" type="primary" @click="saveSpec('form')">确认入库并绑定</yl-button>
            </div>
            <!-- 匹配到显示 end-->
          </el-form>
          <!-- 未匹配到药品显示 -->
          <div v-show="showType === 1">
            <div class="mar-tb-10 flex-between">
              <yl-button type="text">商品列表</yl-button>
              <div>
                <yl-button type="primary" size="default" @click="searchDialogList">搜索</yl-button>
                <yl-button type="primary" size="default" @click="add">新增商品</yl-button>
              </div>
            </div>
            <yl-table 
              :list="goodsStandardList" 
              :total="form.total" 
              :page.sync="form.page" 
              :limit.sync="form.limit" 
              border 
              show-header 
              @getList="getDialogList" 
              :loading="dialogLoading">
              <el-table-column prop="id" align="center" label="标准库ID"></el-table-column>
              <el-table-column prop="name" align="center" label="商品名称"></el-table-column>
              <el-table-column prop="licenseNo" align="center" label="批准文号/生产许可证">
                <template slot-scope="{ row }">
                  <div>
                    {{ row.licenseNo }}
                  </div>
                </template>
              </el-table-column>
              <el-table-column prop="manufacturer" align="center" label="生产厂家"></el-table-column>
              <el-table-column prop="manufacturerAddress" align="center" label="生产地址"></el-table-column>
              <el-table-column align="center" label="操作">
                <template slot-scope="{ row }">
                  <div>
                    <yl-button type="text" size="mini" @click="selectGoods(row)">选择</yl-button>
                  </div>
                </template>
              </el-table-column>
            </yl-table>
          </div>
          <!-- 未匹配到药品显示  end-->

          <!-- 匹配到显示 -->
          <div v-if="showType === 2">
            <div class="mar-tb-10 flex-between">
              <yl-button type="text">已有规格</yl-button>
              <yl-button size="default" @click="changeMatching">更改匹配商品</yl-button>
            </div>
            <yl-table 
              :list="specificationsList" 
              :total="form.total" 
              :page.sync="form.page" 
              :limit.sync="form.limit" 
              @getList="getDialogSpceList" 
              border 
              show-header>
              <el-table-column align="center" label="规格" prop="sellSpecifications"></el-table-column>
              <el-table-column align="center" label="单位" prop="unit"></el-table-column>
              <el-table-column align="center" label="条形码" prop="barcode">
                <template slot-scope="{ row }">
                  <div>
                    {{ row.barcode === '' ? '无' : row.barcode }}
                  </div>
                </template>
              </el-table-column>
              <el-table-column align="center" label="操作" prop="">
                <template slot-scope="{ row }">
                  <div>
                    <yl-button type="text" size="mini" @click="select(row)">选择</yl-button>
                  </div>
                </template>
              </el-table-column>
            </yl-table>
          </div>
          <!-- 匹配到显示 end  -->

        </div>
      </yl-dialog>
      <!-- 反驳弹窗 -->
      <yl-dialog :visible.sync="remarkShow" title="商品反驳" :show-cancle="false" :show-footer="false">
        <div class="dialog-content">
          <el-form 
            :model="remarkForm" 
            :rules="remarkRules" 
            ref="remarkForm" 
            label-width="144px" 
            label-position="right" 
            class="demo-ruleForm">
            <el-row>
              <el-col :span="20">
                <el-form-item label="商品名称：" prop="name">
                  <el-input v-model="remarkForm.name" disabled></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="22">
                <el-form-item label="反馈详情：" prop="remark">
                  <el-input 
                    type="textarea" 
                    v-model="remarkForm.remark" 
                    placeholder="请输入内容" 
                    maxlength="200" 
                    show-word-limit 
                    :autosize="{ minRows: 5, maxRows: 10 }">
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item>
              <yl-button type="primary" @click="rejectSubmit('remarkForm')">确定</yl-button>
              <yl-button @click="rejectRest('remarkForm')">取消</yl-button>
            </el-form-item>
          </el-form>
        </div>
      </yl-dialog>
    </div>
  </div>
</template>

<script>
import { goodsAuditSource } from '@/subject/admin/utils/busi'
import { goodsToStandardLibraryList, goodsToStandardLibraryStatus, goodsToStandardLibraryDialogList, goodsToStandardLibraryDialogSpecList, saveSellSpecifications, rejectGoodsToStandardLibrary, saveSpecificationAndGoLibrary } from '@/subject/admin/api/quality'

export default {
  name: 'GoodsToStandardLibrary',
  components: {
    // ylStatus
  },
  filters: {
    filterSource(val) {
      let arr = goodsAuditSource()
      for (let i = 0; i < arr.length; i++) {
        if (val == arr[i].value) return arr[i].label
      }
    }
  },
  computed: {
    // 数据来源
    goodsAuditSource() {
      return goodsAuditSource()
    }
  },
  data() {
    const barcodeValidate = (rule, value, callback) => {
      let reg = /^[0-9]*[1-9][0-9]*$/
      if (value) {
        if (value === '无' || (reg.test(value) && value.length <= 13)) {
          callback();
        } else {
          callback(new Error('请输入无或13位以内的数字'));
        }
      } else {
        callback(new Error('请输入无或13位以内的数字'));
      }
    };
    return {
      query: {
        page: 1,
        limit: 20,
        total: 0,
        source: 0
      },
      // 一级分类
      category: [],
      dataList: [],
      loading: false,
      dialogLoading: false,
      show: false,
      showId: '',
      showName: '',
      //  弹窗 商品列表
      goodsStandardList: [],
      //  弹窗 规格列表
      specificationsList: [],
      // 弹窗搜索条件
      form: {
        page: 1,
        limit: 10,
        total: 0,
        name: '',
        manufacturer: '',
        licenseNo: '',
        id: '',
        standardId: '',
        // 入库绑定规格
        sellSpecifications: '',
        // 入库绑定单位
        unit: '',
        barcode: ''
      },
      rules: {
        sellSpecifications: [
          { required: true, message: '请输入包装规格', trigger: 'blur' },
          { max: 20, message: '请输入不超过20个字符', trigger: 'blur' }
        ],
        unit: [
          { required: true, message: '请输入单位', trigger: 'blur' },
          { min: 1, max: 5, message: '请输入不超过5个字符', trigger: 'blur' }
        ],
        barcode: [
          { required: true, validator: barcodeValidate, trigger: 'blur' }
        ]
      },
      // 待匹配的商品
      selcetGoodsObj: {},
      // 当前已选择商品
      goodsAuditInfoVO: {},
      // 1 显示选择商品弹窗  2 显示选择规格弹窗
      showType: 1,
      // 反驳
      remarkShow: false,
      remarkForm: {
        name: '',
        remark: ''
      },
      remarkRules: {
        remark: [
          { required: true, message: '请输入驳回原因', trigger: 'blur' }
        ]
      },
      // 新增商品跳转id
      standardAddGid: ''
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
    // 获取基础药品列表
    async getList() {
      this.loading = true
      let query = this.query
      let data = await goodsToStandardLibraryList(
        query.page,
        query.ename,
        query.licenseNo,
        query.manufacturer,
        query.name,
        query.limit,
        query.source
      )
      this.loading = false
      if (data) {
        this.dataList = data.records
        this.query.total = data.total
      }
    },
    handleSearch() {
      this.query.page = 1
      this.getList()
    },
    handleReset() {
      this.query = {
        page: 1,
        limit: 20,
        source: 0
      }
    },
    goImport() {
      this.$router.push({
        path: '/importFile/importFile_index',
        query: {
          action: '/dataCenter/api/v1/goods/audit/mergeImport'
        }
      })
    },
    // 点击匹配
    async matching(row) {
      this.selcetGoodsObj = {}
      this.goodsAuditInfoVO = {}
      this.form.id = row.id
      this.form.page = 1
      this.form.limit = 10
      this.form.name = ''
      this.form.licenseNo = ''
      this.form.manufacturer = ''
      this.form.standardId = ''
      this.form.sellSpecifications = ''
      this.form.unit = ''
      this.form.barcode = ''
      this.$common.showLoad()
      let data = await goodsToStandardLibraryStatus(this.form.id)
      this.$common.hideLoad()
      /**
       * matchStatus
       * 0 未匹配任何信息 打开选择商品弹窗
       * 1 匹配了商品没匹配规格，  打开规格弹窗
       * 2 商品和规格都匹配了  打开规格弹窗
      */
      if (data) {
        //  标准库 id
        this.form.standardId = data.standardId
        if (data.matchStatus === 0) {
          this.showType = 1
          this.show = true
          this.getDialogList()
        } else {
          this.showType = 2
          this.show = true
          this.getDialogSpceList()
        }
      }
    },
    // 弹窗搜索
    searchDialogList() {
      this.form.page = 1
      this.form.limit = 10
      this.getDialogList();
    },
    // 弹窗新增商品
    add() {
      if (this.standardAddGid) {
        this.show = false;
        this.$router.push({
          name: 'BasicDrugsEdit',
          params: { id: this.standardAddGid, type: 'toStandardAdd' }
          // params: { type: 'add' }
        })
      }
    },
    // 匹配弹窗查询列表
    async getDialogList() {
      this.dialogLoading = true
      let data = await goodsToStandardLibraryDialogList(
        this.form.page,
        // 主键id
        this.form.id,
        this.form.licenseNo,
        this.form.manufacturer,
        this.form.name,
        this.form.limit
      )
      this.dialogLoading = false
      if (data && data.records) {
        this.goodsStandardList = data.records
      }
      if (data.goodsAuditInfoVO) {
        this.goodsAuditInfoVO = data.goodsAuditInfoVO
        this.form.sellSpecifications = data.goodsAuditInfoVO.specifications
        // 点击新增商品时，带到新增商品页面的gid
        this.standardAddGid = data.goodsAuditInfoVO.gid
      }
      this.form.total = data.total
    },
    //  匹配弹窗 规格列表
    async getDialogSpceList() {
      this.$common.showLoad()
      let data = await goodsToStandardLibraryDialogSpecList(
        this.form.page,
        this.form.id,
        this.form.limit,
        this.form.standardId
      )
      this.$common.hideLoad()
      if (data) {
        if (data.records) {
          this.specificationsList = data.records
        }
        if (data.goodsAuditInfoVO) {
          this.goodsAuditInfoVO = data.goodsAuditInfoVO
          this.form.sellSpecifications = data.goodsAuditInfoVO.specifications
          this.form.unit = data.goodsAuditInfoVO.unit
        }
        if (data.standardGoodsInfoVO) {
          this.selcetGoodsObj = data.standardGoodsInfoVO
        }
        this.form.total = data.total
      }
      console.log(data);
    },
    // 关闭规格弹窗
    confirm() {
      this.show = false
    },
    // 选择匹配规格
    async select(row) {
      //  待匹配商品主建id， 销售规格ID ，标准库id
      this.$common.showLoad()
      let data = await saveSellSpecifications(this.goodsAuditInfoVO.id, row.id, this.selcetGoodsObj.id)
      this.$common.hideLoad()
      if (data !== undefined) {
        this.show = false
        this.$common.success('规格匹配成功')
        this.getList()
      }
    },
    // 确定入库并绑定规格
    saveSpec(formName) {
      this.$refs.form.validate(async (valid) => {
        if (valid) {
          this.$common.showLoad()
          let data = await saveSpecificationAndGoLibrary(
            // 规格
            this.form.sellSpecifications,
            //id
            // this.form.standardId,
            this.selcetGoodsObj.id,
            // 单位
            this.form.unit,
            // 条形码   产品要求 显示无，但是后台要求传空字符串
            this.form.barcode === '无' ? '' : this.form.barcode
          )
          this.$common.hideLoad()
          if (data && data.id) {
            this.form.sellSpecifications = ''
            this.form.unit = ''
            this.form.barcode = ''
            this.select({ id: data.id })
            this.$common.success('入库成功')
          }
        } else {
          console.log('error submit!!');
          return false;
        }
      });

    },
    // 更改匹配商品
    changeMatching() {
      // 清空当前已选择商品
      this.selcetGoodsObj = {}
      //  1 显示选择商品弹窗  2 显示选择规格弹窗
      this.showType = 1
      //  查询匹配的商品列表
      this.getDialogList();
    },
    // 选择商品
    async selectGoods(item) {
      console.log(item);
      this.selcetGoodsObj = item
      this.form.standardId = item.id
      this.showType = 2
      this.form.page = 1
      this.$common.showLoad()
      let data = await goodsToStandardLibraryDialogSpecList(
        this.form.page,
        item.id,
        this.form.limit,
        // 选择的标准库商品 标准id
        item.id
      )
      this.$common.hideLoad()
      if (data) {
        if (data.records) {
          this.specificationsList = data.records
        }
        this.form.total = data.total
      }
    },

    // 点击驳回
    reject(item) {
      this.remarkShow = true
      this.remarkForm.name = item.name
      this.remarkForm.id = item.id
    },
    // 点击驳回确定
    rejectSubmit(formName) {
      this.$refs[formName].validate(async (valid) => {
        if (valid) {
          this.$common.showLoad()
          let data = await rejectGoodsToStandardLibrary(this.remarkForm.id, this.remarkForm.remark)
          this.$common.hideLoad()
          if (data !== undefined) {
            this.remarkShow = false
            this.$common.success('驳回成功')
            this.getList()
          }
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    // 点击驳回取消
    rejectRest(formName) {
      this.$refs[formName].resetFields();
      this.remarkForm.id = ''
      this.remarkForm.name = ''
      this.remarkForm.remark = ''
      this.remarkShow = false
    }

  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
