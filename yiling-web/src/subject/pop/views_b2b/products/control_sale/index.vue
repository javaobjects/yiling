<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <!-- 内容区 -->
    <div class="app-container-content has-bottom-bar">
      <!-- 基本信息 -->
      <div class="common-box  top-bar">
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
                <div class="intro"><span class="font-title-color">批准文号/生产许可证：</span>{{ data.licenseNo }}</div>
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
        <div class="tab">
          <div v-for="(item,index) in tabList" :key="index" class="tab-item" :class="tabActive === index?'tab-active':''" @click="clickTab(index)">{{ item }}</div>
        </div>
      </div>
      <!-- 区域类型 -->
      <div class="common-box box-1" v-show="tabActive ===0">
        <div>
          <span class="mar-r-16 color-666">类型设置</span>
          <el-radio v-model="typeSet" label="0">不设置</el-radio>
          <el-radio v-model="typeSet" label="1">设置类型</el-radio>
        </div>
        <div v-show="typeSet == 1" class="mar-t-16   mar-l-177">
          <el-checkbox-group v-model="checkTypeList" v-for="(item,index) in enterpriseType" :key="index">
            <el-checkbox :label="item.label" class="mar-r-8"></el-checkbox>
          </el-checkbox-group>
        </div>
        <div class="mar-t-16">
          <span class="mar-r-16 color-666">区域设置</span>
          <el-radio v-model="regionSet" label="0" @change="changeCity">不设置</el-radio>
          <el-radio v-model="regionSet" label="1" @change="changeCity">设置类型</el-radio>
        </div>
        <div v-if="regionSet !=0 " class="mar-t-16 mar-l-177 city" @click="showCity = true">
          <span>{{ chooseCityDesc }}</span>
        </div>

        <!-- v-show="region == 2" -->
        <CitySelect platform="pop" :show.sync="showCity" :init-ids="showCityIdsList" @choose="chooseCity"></CitySelect>
      </div>
      <!-- 控销客户 -->
      <div class="common-box box-1" v-show="tabActive ===1">
        <div>
          <span class="mar-r-16 color-666">客户设置</span>
          <el-radio v-model="customerTypeSet" label="0">不设置</el-radio>
          <el-radio v-model="customerTypeSet" label="1">设置客户</el-radio>
        </div>
        <div class="mar-t-16" v-show="customerTypeSet == 1">
          <ylButton type="primary" plain @click="addCustomer">添加客户</ylButton>
        </div>
        <div class="color-990 mar-t-16" v-show="customerTypeSet == 1">(已设置<span class="color-yellow">{{ total }}</span>个客户)</div>
        <div class="mar-t-16" v-show="customerTypeSet == 1">
          <div class="header-bar mar-b-10">
            <div class="sign"></div>已添加客户列表
          </div>
          <div class="search-box">
            <el-form :model="form" :rules="rules" ref="form" label-width="72px" label-position="top">
              <el-row :gutter="24">
                <el-col :span="8">
                  <el-form-item label="客户名称">
                    <el-input v-model="form.name" placeholder="请输入" />
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="客户分组">
                    <el-select v-model="form.customerGroupId" placeholder="请选择">
                      <el-option label="全部" :value="0"></el-option>
                      <el-option v-for="item in GroupList" :key="item.id" :label="item.name" :value="item.id">
                      </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="客户类型">
                    <el-select v-model="form.type" placeholder="请选择">
                      <el-option label="全部" :value="0"></el-option>
                      <el-option v-for="item in enterpriseType" :key="item.value" :label="item.label" :value="item.value">
                      </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row :gutter="24">
                <el-col :span="10">
                  <el-form-item label="省份">
                    <yl-choose-address is-all :province.sync="form.provinceCode" :city.sync="form.cityCode" :area.sync="form.regionCode" />
                  </el-form-item>
                </el-col>
              </el-row>

              <el-row :gutter="24">
                <el-col :span="24">
                  <yl-search-btn :total="total" @search="handleSearch" @reset="handleReset" />
                </el-col>
              </el-row>
            </el-form>
          </div>
          <!-- tab -->
          <div class="flex-between mar-t-16 mar-b-8">
            <div class="tab ">
            </div>
            <div class="btn">
              <ylButton type="primary" plain @click="allDelet">批量删除</ylButton>
            </div>
          </div>
          <yl-table stripe :show-header="true" :list="dataList" :total="total" :page.sync="form.page" :limit.sync="form.limit" :loading="loading" @getList="getCustomer" :selection-change="handleSelectionChange">
            <el-table-column type="selection" width="70"></el-table-column>
            <el-table-column label="用户名称" align="center" prop="customerName"></el-table-column>
            <el-table-column label="省" align="center" prop="provinceName"></el-table-column>
            <el-table-column label="市" align="center" prop="cityName"></el-table-column>
            <el-table-column label="区" align="center" prop="regionName"></el-table-column>
            <!-- <el-table-column label="市" align="center"> </el-table-column> -->
            <!-- <el-table-column label="区" align="center"></el-table-column> -->
            <el-table-column label="用户类型" align="center" prop="customerType"></el-table-column>
            <el-table-column label="用户分组" align="center" prop="customerGroupName"></el-table-column>
            <el-table-column label="操作" align="center">
              <template slot-scope="{ row }">
                <yl-button type="text" @click="del(row)">删除</yl-button>
              </template>
            </el-table-column>
          </yl-table>
        </div>
      </div>
      <!-- footer -->
      <div class="flex-row-center bottom-view">
        <yl-button plain type="primary" @click="$router.go(-1)">返回</yl-button>
        <yl-button type="primary" @click="saveChange">保存</yl-button>
      </div>
      <!-- 弹窗 -->
      <!-- 批量导入渠道类型弹框 -->
      <yl-dialog title="选择客户" :visible.sync="show" @confirm="show = false">
        <div class="common-box">
          <div>
            <el-form :model="form1" :rules="rules" ref="form1" label-width="72px" label-position="top">
              <el-row>
                <el-col :span="8">
                  <el-form-item label="客户名称">
                    <el-input v-model="form1.name" placeholder="请输入" />
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="客户分组">
                    <el-select v-model="form1.customerGroupId" placeholder="请选择">
                      <el-option label="全部" :value="0"></el-option>
                      <el-option v-for="item in GroupList" :key="item.id" :label="item.name" :value="item.id">
                      </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="客户类型">
                    <el-select v-model="form1.type" placeholder="请选择">
                      <el-option label="全部" :value="0"></el-option>
                      <el-option v-for="item in enterpriseType" :key="item.value" :label="item.label" :value="item.value">
                      </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="16">
                  <el-form-item label="省份">
                    <yl-choose-address is-all :province.sync="form1.provinceCode" :city.sync="form1.cityCode" :area.sync="form1.regionCode" />
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="24">
                  <yl-search-btn :total="total1" @search="handleSearch1" @reset="handleReset1" />
                </el-col>
              </el-row>
            </el-form>
          </div>
          <!-- tab -->
          <div class="flex-between mar-t-16 mar-b-8">
            <div class="tab ">
            </div>
            <div class="btn">
              <ylButton type="primary" plain @click="allAdd">批量添加</ylButton>
            </div>
          </div>
          <yl-table stripe :show-header="true" :list="dataList1" :total="total1" :page.sync="form1.page" :limit.sync="form1.limit" :loading="loading1" @getList="getCustomerDialog" :selection-change="handleSelectionChange1">
            <!-- <el-table-column type="selection" width="70"></el-table-column> -->
            <el-table-column label="用户名称" align="center" prop="customerName"></el-table-column>
            <el-table-column label="省" align="center" prop="provinceName"></el-table-column>
            <el-table-column label="市" align="center" prop="cityName"></el-table-column>
            <el-table-column label="区" align="center" prop="regionName"></el-table-column>
            <el-table-column label="用户类型" align="center" prop="customerType"></el-table-column>
            <el-table-column label="用户分组" align="center" prop="customerGroupName"></el-table-column>
            <el-table-column label="操作" align="center">
              <template slot-scope="{ row }">
                <yl-button v-if="row.customerDisableVO.controlDisable" disabled type="text">已添加</yl-button>
                <yl-button v-else type="text" @click="addOne(row)">添加</yl-button>
              </template>
            </el-table-column>
          </yl-table>
        </div>
      </yl-dialog>
    </div>
  </div>
</template>

<script>
import { b2bProductDetail, b2bProControlSellRegion, b2bProControlSellCustomer, b2bProControlSellSaveRegion, b2bProControlSellGroupList, b2bProControlSellPageCustomer, b2bProControlSellSaveCustomer, b2bProControlSellAllAdd, b2bProControlSellAllDel } from '@/subject/pop/api/b2b_api/b2b_products'
import { getCurrentUser } from '@/subject/pop/utils/auth'
import { goodsStatus, enterpriseType, goodsOutReason, goodsPatent } from '@/subject/pop/utils/busi'
import { ylChooseAddress } from '@/subject/pop/components'
import CitySelect from '@/subject/pop/components/CitySelect'
export default {
  components: {
    ylChooseAddress, CitySelect
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
      var reg = /^((?!-1)\d{0,10})$/;//最长9位数字，可修改
      if (value) {
        if (!reg.test(value)) {
          return callback(new Error('请输入正整数'))
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
          title: '控销详情'
        }
      ],
      data: {},
      form: {
        page: 1,
        limit: 10,
        name: '',
        type: '',
        customerGroupId: '',
        provinceCode: '',
        cityCode: '',
        regionCode: ''
      },
      dataList: [],
      show: false,
      // 弹窗客户查询条件
      form1: {
        page: 1,
        limit: 10,
        name: '',
        type: '',
        customerGroupId: '',
        provinceCode: '',
        cityCode: '',
        regionCode: ''
      },
      dataList1: [],
      total: 0,
      total1: 0,
      loading: false,
      loading1: false,
      rules: {
        regionCode: [{ required: true, message: '请选择区域', trigger: 'change' }],
        enterprise: [{ required: true, message: '请选择企业类型', trigger: 'change' }],
        price: [{ required: true, validator: isQtyVlidator, trigger: 'change' }]
      },
      // 表格选择
      multipleSelection: [],
      // 弹窗客户列表全选
      multipleSelection1: [],

      tabList: ['区域类型', '控销客户'],
      tabActive: 0,
      typeSet: '0', // 区域类型，类型设置
      regionSet: '0',// 区域类型，区域设置
      customerTypeSet: '0', // 控销客户，客户设置
      checkTypeList: [],
      showCity: false,
      showCityList: [
        // {
        //   code: '110000',
        //   name: '北京市',
        //   selected: true,
        //   children: [
        //     // {code:"110101",name: '东城区',selected: true,},
        //     // {code: "110102",name: '西城区',selected: true,}
        //   ]
        // }

      ],
      // 保存并回显选择省份。
      showCityIdsList: [],
      chooseCityDesc: '',
      // 客户分组列表
      GroupList: [],
      // 删除客户用的客户控销id
      controlId: ''
    }
  },
  mounted() {
    this.id = this.$route.params.id
    if (this.id) {
      this.id = parseFloat(this.id)
      this.getData()
      this.getRegion()
      this.getCustomer()
      this.getGroupList()
    }
    this.$log(this.$route)
  },
  methods: {
    clickTab(e) {
      this.tabActive = e
    },
    add() {
      this.show = true
    },
    // 添加单独客户
    async addOne(row) {
      // this.show = true
      let data = await b2bProControlSellSaveCustomer(
        0, //0没有设置 1全部 2部分设置
        this.id,
        row.customerEid
      )
      if (data !== undefined) {
        this.$common.n_success('添加成功')
        this.getCustomerDialog()
        // 在添加的时候不要切换set
        this.getCustomer(1)
      }
    },

    // 批量添加
    async allAdd() {
      this.$common.showLoad()
      let data = await b2bProControlSellAllAdd(
        this.form1.page,
        this.form1.limit,
        this.id,
        this.form1.name,
        this.form1.customerGroupId,
        this.form1.type,
        this.form1.provinceCode,
        this.form1.cityCode,
        this.form1.regionCode
      )
      this.$common.hideLoad()
      console.log(data);
      if (data !== undefined) {
        this.getCustomerDialog()
        this.getCustomer(1)
        this.$common.n_success('添加成功')
      }
    },
    // 批量删除
    async allDelet() {
      if (this.multipleSelection.length == 0) {
        return this.$common.error('请选择需要删除的客户')
      } else {
        let arr = []
        for (let i = 0; i < this.multipleSelection.length; i++) {
          arr.push(this.multipleSelection[i].customerEid)
        }
        this.deletCustomer(arr)
      }

    },
    // 单独删除
    async del(row) {
      console.log(row);
      this.deletCustomer([row.customerEid])
    },
    //  删除客户
    async deletCustomer(arr) {
      let data = await b2bProControlSellAllDel(
        arr,
        this.controlId
      )
      console.log(data);
      if (data !== undefined) {
        this.$common.n_success('删除成功')
        this.getCustomer(1)
      }
    },
    // 表格全选
    handleSelectionChange(val) {
      this.multipleSelection = val;
      this.$log('this.multipleSelection:', val);
    },
    // 弹窗客户列表全选
    handleSelectionChange1(val) {
      this.multipleSelection1 = val;
      this.$log('this.multipleSelection:', val);
    },

    // b2b后台商品控销-获取客户控销
    handleSearch() {
      this.getCustomer(1)
    },
    handleReset() {
      this.form = {
        page: 1,
        limit: 10,
        name: '',
        type: '',
        customerGroupId: '',
        provinceCode: '',
        cityCode: '',
        regionCode: ''
      }
      this.getCustomer(1);
    },
    handleSearch1() {
      this.getCustomerDialog()
    },
    handleReset1() {
      this.form1 = {
        page: 1,
        limit: 10,
        name: '',
        type: '',
        customerGroupId: '',
        provinceCode: '',
        cityCode: '',
        regionCode: ''
      }
      this.getCustomerDialog();
    },
    addCustomer() {
      this.getCustomerDialog()
      this.show = true;
    },
    //  保存
    // 区域类型，类型设置
    async saveChange() {
      console.log(this.checkTypeList);
      // 区域类型，区域设置
      console.log(this.showCityIdsList);
      console.log(this.typeSet)
      console.log(this.regionSet);
      console.log(this.enterpriseType);
      let arr = []
      for (let i = 0; i < this.checkTypeList.length; i++) {
        for (let j = 0; j < this.enterpriseType.length; j++) {
          if (this.checkTypeList[i] === this.enterpriseType[j].label) {
            arr.push(this.enterpriseType[j].value)
          }
        }
      }
      if (this.typeSet == 1 && arr.length < 1) return this.$message.error('请至少选择一个类型')
      let data = await b2bProControlSellSaveRegion(
        this.typeSet, //	客户类型设置 0没有设置 1全部 2部分设置
        arr, // 客户id集合
        this.showCityIdsList,// 区域id集合
        this.regionSet,//	区域类型设置 0没有设置 1全部 2部分设置
        this.id,
        this.chooseCityDesc
      )
      console.log(data);
      if (data !== undefined) {
        this.saveCustomer()
      }

    },
    async saveCustomer() {
      // 保存客户设置。
      let data = await b2bProControlSellSaveCustomer(
        this.customerTypeSet, //0没有设置 1全部 2部分设置
        this.id
        // customerId,
      )
      console.log(data);
      if (data !== undefined) {
        this.$common.n_success('保存成功')
        // this.customerTypeSet = String(data.setType)
      }
    },
    submit() {
      this.$refs['form'].validate(async (valid) => {
        if (valid) {
          let form = this.form
          this.$common.showLoad()
          let data = await changeCompany(
            form.provinceCode,
            form.cityCode,
            form.regionCode,
            form.enterprise,
            form.price
          )
          this.$common.hideLoad()
          if (data !== undefined) {
            this.show = false
            this.getData()
            this.$common.n_success('修改成功')
          }
        } else {
          return false
        }
      })
    },
    // 获取详情
    async getData() {
      this.$common.showLoad()
      let data = await b2bProductDetail(this.id)
      this.$common.hideLoad()
      this.$log(data)
      if (data !== undefined) {
        this.data = data
      }
    },
    // b2b后台商品控销-获取区域控销
    async getRegion() {
      this.$common.showLoad()
      let data = await b2bProControlSellRegion(this.id)
      this.$common.hideLoad()
      console.log(data)
      if (data !== undefined) {
        if (data.customerTypeControlVO) {
          this.typeSet = String(data.customerTypeControlVO.setType)
          // customerTypes
          let arr = data.customerTypeControlVO.customerTypes

          for (let i = 0; i < arr.length; i++) {
            for (let j = 0; j < this.enterpriseType.length; j++) {
              if (arr[i] == this.enterpriseType[j].value) {
                this.checkTypeList.push(this.enterpriseType[j].label)
              }
            }
          }
        }
        if (data.regionControlVO) {
          this.regionSet = String(data.regionControlVO.setType)
          this.showCityIdsList = data.regionControlVO.regionIds
          this.chooseCityDesc = data.regionControlVO.controlDescribe
        }
      }
    },
    // b2b后台商品控销-获取客户控销
    async getCustomer(type) {
      this.$common.showLoad()
      let data = await b2bProControlSellCustomer(
        this.form.page,
        this.form.limit,
        this.id,
        this.form.name,
        this.form.type,
        this.form.customerGroupId,
        this.form.provinceCode,
        this.form.cityCode,
        this.form.regionCode)
      this.$common.hideLoad()
      console.log(data)
      if (data !== undefined) {
        this.dataList = data.records
        this.total = data.total
        this.controlId = data.id
        if (type !== 1) {
          this.customerTypeSet = String(data.setType)
        }
      }
    },
    // 获取弹窗可数列表
    async getCustomerDialog() {
      this.$common.showLoad()
      let data = await b2bProControlSellPageCustomer(
        this.form1.page,
        this.form1.limit,
        this.id,
        this.form1.name,
        this.form1.type,
        this.form1.customerGroupId,
        this.form1.provinceCode,
        this.form1.cityCode,
        this.form1.regionCode)
      this.$common.hideLoad()
      console.log(data)
      if (data !== undefined) {
        this.dataList1 = data.records
        this.total1 = data.total
      }
    },
    // 获取客户分组列表
    async getGroupList() {
      let data = await b2bProControlSellGroupList()
      if (data !== undefined) {
        this.GroupList = data.list
      }
    },
    //
    changeCity(e) {
      console.log(e);
      if (e == 1) {
        this.showCity = true
      } else {
        this.showCity = false
      }
    },
    // 选择选择省市区反参
    chooseCity(e) {
      console.log(e);
      let arr = e.nodes
      // 获取选择的省市区
      this.showCityIdsList = []
      for (let i = 0; i < arr.length; i++) {
        for (let j = 0; j < arr[i].children.length; j++) {
          for (let k = 0; k < arr[i].children[j].children.length; k++) {
            this.showCityIdsList.push(arr[i].children[j].children[k].code)
          }
        }
      }
      this.chooseCityDesc = e.desc;
      console.log(this.showCityIdsList);
      this.showCity = false
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
<style lang="scss" scoped>
</style>
