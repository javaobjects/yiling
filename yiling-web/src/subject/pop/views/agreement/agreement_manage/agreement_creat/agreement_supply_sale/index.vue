<template>
  <el-form :model="agreementSupplySalesTerms" :rules="agreementSupplySalesTermsRules" ref="dataForm" class="agreement-main" label-position="left">
    <div>
      <div class="top-bar">
        <div class="header-bar header-renative">
          <div class="sign"></div>商业供销协议条款
        </div>
        <div class="content-box my-form-box">
          <el-form-item label="购进渠道:" prop="buyChannel" label-width="150px">
            <el-radio-group v-model="agreementSupplySalesTerms.buyChannel" @change="buyChannelRadioChange">
              <el-radio v-for="item in agreementSupplySalesBuyChannel" :key="item.value" :label="item.value">
                {{ item.label }}
              </el-radio>
              <yl-button icon="el-icon-circle-plus-outline" type="primary" v-if="agreementSupplySalesTerms.buyChannel == 3" @click="addProviderClick">指定商业公司</yl-button>
            </el-radio-group>
          </el-form-item>
          <div class="pad-b-10">
            <yl-table
              border
              show-header
              :list="agreementSupplySalesTerms.supplySalesEnterpriseList"
              :total="0"
            >
              <el-table-column label="企业ID" align="center" prop="eid">
              </el-table-column>
              <el-table-column label="企业名称" align="center" prop="ename">
              </el-table-column>
              <el-table-column label="操作" align="center" fixed="right">
                <template slot-scope="{ $index }">
                  <div class="operation-view">
                    <yl-button type="text" @click="providerRemoveClick($index)">删除</yl-button>
                  </div>
                </template>
              </el-table-column>
            </yl-table>
          </div>
          <el-form-item label="是否拉单支持:" prop="pullOrderFlag" label-width="150px">
            <el-radio-group v-model="agreementSupplySalesTerms.pullOrderFlag">
              <el-radio :label="1">是</el-radio>
              <el-radio :label="0">否</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="是否分销协议支持:" prop="distributionAgreementFlag" label-width="150px">
            <el-radio-group v-model="agreementSupplySalesTerms.distributionAgreementFlag">
              <el-radio :label="1">是</el-radio>
              <el-radio :label="0">否</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="是否全系列品种:" prop="allLevelKindsFlag" label-width="150px">
            <el-radio-group v-model="agreementSupplySalesTerms.allLevelKindsFlag" @change="allLevelKindsFlagRadioChange">
              <el-radio :label="1">是</el-radio>
              <el-radio :label="0">否</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item >
            <yl-button icon="el-icon-circle-plus-outline" type="primary" v-if="agreementSupplySalesTerms.allLevelKindsFlag == 0" @click="addSupplySalesGoodsGroupListClick">新建表单</yl-button>
          </el-form-item>
          <!-- 商品组件 -->
          <div v-if="agreementSupplySalesTerms.allLevelKindsFlag == 0 && agreementSupplySalesTerms.supplySalesGoodsGroupList.length > 0">
            <div class="goods-group-item" v-for="(item, index) in agreementSupplySalesTerms.supplySalesGoodsGroupList" :key="index">
              <!-- 商品表单 -->
              <div class="pad-b-10">
                <!-- 导出按钮 -->
                <div class="down-box clearfix mar-b-10">
                  <div class="btn">
                    <ylButton type="primary" plain @click="deleteTableClick(index)">删除表单</ylButton>
                    <ylButton type="primary" plain @click="addGoodsClick(index)">新建商品</ylButton>
                    <ylButton type="primary" plain @click="deleteGoodsClick(index)">删除商品</ylButton>
                    <ylButton type="primary" plain @click="downLoadTemplate">模版导出</ylButton>
                    <!-- <el-link class="mar-r-10" type="primary" :underline="false" :href="'NO_10' | template">
                      下载模板
                    </el-link> -->
                    <ylButton type="primary" @click="goImport(index)">导入</ylButton>
                  </div>
                </div>
                <yl-table
                  :selection-change="(selection) => deleteSelectionChange(selection, index)"
                  border
                  show-header
                  :height="600"
                  :list="item.supplySalesGoodsList"
                  :total="0"
                >
                  <el-table-column type="selection" width="55"></el-table-column>
                  <el-table-column label="商品编号" align="center">
                    <template slot-scope="{ row }">
                      <div class="font-size-base">{{ row.goodsId }}</div>
                    </template>
                  </el-table-column>
                  <el-table-column label="商品名称" min-width="162" align="center">
                    <template slot-scope="{ row }">
                      <div class="goods-desc">
                        <div class="font-size-base">{{ row.goodsName }}</div>
                      </div>
                    </template>
                  </el-table-column>
                  <el-table-column label="商品规格" min-width="150" align="center">
                    <template slot-scope="{ row }">
                      <div class="font-size-base">{{ row.specifications }}</div>
                    </template>
                  </el-table-column>
                  <el-table-column label="供货含税单价" min-width="150" align="center">
                    <template slot-scope="{ row }">
                      <div class="font-size-base">{{ row.supplyTaxPrice }}</div>
                    </template>
                  </el-table-column>
                  <el-table-column align="center">
                    <template slot="header" slot-scope="{}">
                      <span style="margin-right:8px;">出库价含税单价 |</span>
                      <el-checkbox v-model="item.exitWarehouseTaxPriceFlag">维价</el-checkbox>
                    </template>
                    <template slot-scope="{ row, $index }">
                      <div class="font-size-base">
                        <el-input v-if="item.exitWarehouseTaxPriceFlag" v-model="row.exitWarehouseTaxPrice" @input="changeOnInput(index, $index, 1)"></el-input>
                      </div>
                    </template>
                  </el-table-column>
                  <el-table-column align="center">
                    <template slot="header" slot-scope="{}">
                      <span style="margin-right:8px;">零售价含税单价 |</span>
                      <el-checkbox v-model="item.retailTaxPriceFlag">维价</el-checkbox>
                    </template>
                    <template slot-scope="{ row, $index }">
                      <div class="font-size-base">
                        <el-input v-if="item.retailTaxPriceFlag" v-model="row.retailTaxPrice" @input="changeOnInput(index, $index, 2)"></el-input>
                      </div>
                    </template>
                  </el-table-column>
                  <el-table-column label="是否独家" align="center" fixed="right">
                    <template slot-scope="{ row, $index }">
                      <div class="operation-view">
                        <el-checkbox v-model="row.exclusiveFlag" @input="change(index, $index)"></el-checkbox>
                      </div>
                    </template>
                  </el-table-column>
                </yl-table>
              </div>
              <div class="header-bar header-renative mar-t-10">
                <div class="sign"></div>控销设置
              </div>
              <el-form-item label="控销类型:" label-width="150px">
                <el-radio-group v-model="item.controlSaleType" @change="(type) => controlSaleTypeRadioChange(type, index)">
                  <el-radio v-for="typeItem in agreementControlSaleType" :key="typeItem.value" :label="typeItem.value">
                    {{ typeItem.label }}
                  </el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="控销条件:" label-width="150px" v-if="item.controlSaleType != 1">
                <el-checkbox-group v-model="item.agreementControlList" class="money-form-item">
                  <el-checkbox :label="1">区域</el-checkbox>
                  <el-form-item label="" class="money-form-item mar-r-16" v-if="item.agreementControlList.indexOf(1) != -1">
                    <yl-button type="text" @click="selectClick(index)">{{ item.controlArea.jsonContent ? item.controlArea.selectedDesc : '选择区域' }}</yl-button>
                  </el-form-item>
                  <el-checkbox style="margin-left:30px;" :label="2">客户类型</el-checkbox>
                  <el-form-item :prop="'supplySalesGoodsGroupList.' + index + '.controlCustomerTypeList'" :rules="{ required: true, message: '请选择', trigger: 'change' }" class="money-form-item mar-l-16" v-if="item.agreementControlList.indexOf(2) != -1">
                    <el-select collapse-tags v-model="item.controlCustomerTypeList" multiple placeholder="请选择">
                      <el-option
                        v-for="enterpriseTypeItem in enterpriseType"
                        :key="enterpriseTypeItem.value"
                        :label="enterpriseTypeItem.label"
                        :value="enterpriseTypeItem.value">
                      </el-option>
                    </el-select>
                  </el-form-item>
                </el-checkbox-group>
              </el-form-item>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- 指定商业公司 -->
    <yl-dialog title="供应商信息查询" :visible.sync="addProviderVisible" width="966px" :show-footer="false">
      <div class="dialog-content-box-customer">
        <div class="dialog-content-top">
          <div class="search-box">
            <el-row class="box">
              <el-col :span="8">
                <div class="title">供应商名称</div>
                <el-input v-model="addProviderQuery.ename" placeholder="请输入供应商名称" @keyup.enter.native="addProviderHandleSearch" />
              </el-col>
            </el-row>
          </div>
          <div class="search-box">
            <el-row class="box">
              <el-col :span="16">
                <yl-search-btn
                  :total="addProviderTotal"
                  @search="addProviderHandleSearch"
                  @reset="addProviderHandleReset"
                />
              </el-col>
            </el-row>
          </div>
        </div>
        <div class="mar-t-10" style="padding-bottom: 10px;background: #FFFFFF;">
          <yl-table
            :stripe="true"
            :show-header="true"
            :list="addProviderList"
            :total="addProviderTotal"
            :page.sync="addProviderQuery.page"
            :limit.sync="addProviderQuery.limit"
            :loading="loading3"
            @getList="getBusinessPage"
          >
            <el-table-column label="企业ID" min-width="55" align="center">
              <template slot-scope="{ row }">
                <div class="font-size-base">{{ row.eid }}</div>
              </template>
            </el-table-column>
            <el-table-column label="供应商名称" min-width="162" align="center">
              <template slot-scope="{ row }">
                <div class="goods-desc">
                  <div class="font-size-base">{{ row.ename }}</div>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="供应商类型" min-width="162" align="center">
              <template slot-scope="{ row }">
                <div class="goods-desc">
                  <div class="font-size-base">{{ row.type | dictLabel(enterpriseType) }}</div>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="地区" min-width="162" align="center">
              <template slot-scope="{ row }">
                <div class="goods-desc">
                  <div class="font-size-base">{{ row.address }}</div>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="操作" min-width="55" align="center">
              <template slot-scope="{ row }">
                <div>
                  <yl-button class="view-btn" v-if="!row.isSelect" type="text" @click="addProviderItemClick(row)">添加</yl-button>
                  <yl-button class="view-btn" v-if="row.isSelect" disabled type="text">已添加</yl-button>
                </div>
              </template>
            </el-table-column>
          </yl-table>
        </div>
      </div>
    </yl-dialog>
    <!-- 选择区域 -->
    <city-select :show.sync="showCityDialog" :init-ids="selectedData" platform="pop" @choose="citySelectConfirm"></city-select>
    <!-- 添加商品弹窗 -->
    <yl-dialog title="添加商品" :visible.sync="addGoodsDialog" width="966px" @confirm="addGoodsConfirm">
      <div class="dialog-content-box-customer">
        <div class="dialog-content-top">
          <div class="search-box">
            <el-row class="box">
              <el-col :span="8">
                <div class="title">商品编号</div>
                <el-input v-model="goodsQuery.goodsId" placeholder="请输入商品编号" @keyup.enter.native="goodsHandleSearch" />
              </el-col>
              <el-col :span="8">
                <div class="title">商品名称</div>
                <el-input v-model="goodsQuery.goodsName" placeholder="请输入商品名称" @keyup.enter.native="goodsHandleSearch" />
              </el-col>
            </el-row>
          </div>
          <div class="search-box">
            <el-row class="box">
              <el-col :span="16">
                <yl-search-btn
                  :total="goodsTotal"
                  @search="goodsHandleSearch"
                  @reset="goodsHandleReset"
                />
              </el-col>
            </el-row>
          </div>
        </div>
        <div class="mar-t-10" style="padding-bottom: 10px;background: #FFFFFF;">
          <yl-table
            ref="multipleTable"
            :selection-change="handleSelectionChange"
            :stripe="true"
            :show-header="true"
            :list="goodsList"
            :total="goodsTotal"
            :page.sync="goodsQuery.page"
            :limit.sync="goodsQuery.limit"
            :loading="loading2"
            @getList="getGoodsList"
          >
            <el-table-column type="selection" align="center" width="70"></el-table-column>
            <el-table-column label="商品编号" align="center">
              <template slot-scope="{ row }">
                <div class="font-size-base">{{ row.goodsId }}</div>
              </template>
            </el-table-column>
            <el-table-column label="商品名称" min-width="162" align="center">
              <template slot-scope="{ row }">
                <div class="goods-desc">
                  <div class="font-size-base">{{ row.goodsName }}</div>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="商品规格" min-width="150" align="center">
              <template slot-scope="{ row }">
                <div class="font-size-base">{{ row.specifications }}</div>
              </template>
            </el-table-column>
          </yl-table>
        </div>
      </div>
    </yl-dialog>
    <!-- 导入商品 -->
    <yl-dialog title="导入" :visible.sync="importDialog" aria-atomic="" :show-footer="false">
      <div class="import-dialog-content flex-row-center">
        <yl-upload-file
          :action="info.action"
          :extral-data="info.extralData"
          @onSuccess="onSuccess"
        />
      </div>
    </yl-dialog>
  </el-form>
</template>

<script>
import {
  queryBusinessPage,
  queryGoodsPageList
} from '@/subject/pop/api/agreement';
import { agreementSupplySalesBuyChannel, channelType, agreementControlSaleType, enterpriseType } from '@/subject/pop/utils/busi';
import citySelect from '@/subject/pop/components/CitySelect'
import { downloadByUrl } from '@/subject/pop/utils'
import { ylUploadFile } from '@/subject/pop/components'
import { onInputLimit } from '@/common/utils'

export default {
  name: 'AgreementSupplySale',
  components: {
    citySelect,
    ylUploadFile
  },
  computed: {
    // 购进渠道
    agreementSupplySalesBuyChannel() {
      return agreementSupplySalesBuyChannel()
    },
    // 供应商类型
    channelType() {
      return channelType();
    },
    // 企业类型
    enterpriseType() {
      return enterpriseType();
    },
    // 控销类型
    agreementControlSaleType() {
      return agreementControlSaleType();
    }
  },
  watch: {
  },
  data() {
    return {
      NO_10: 'https://yl-public.oss-cn-zhangjiakou.aliyuncs.com/common/template/import_agreementv2_goods_template_v20220316.xls',
      // 导入商品弹窗
      importDialog: false,
      info: {
        action: '',
        extralData: {}
      },
      // 甲方ID
      eid: '',
      // 甲方类型
      firstType: '',
      // 指定商业公司
      addProviderVisible: false,
      loading3: false,
      addProviderQuery: {
        page: 1,
        limit: 10
      },
      addProviderList: [],
      addProviderTotal: 0,
      // 选择区域
      currentAreaIndex: 0,
      showCityDialog: false,
      selectedDesc: '',
      selectedData: [],
      // 添加商品弹窗
      addGoodsDialog: false,
      loading2: false,
      goodsQuery: {
        page: 1,
        limit: 10
      },
      goodsList: [],
      goodsTotal: 0,
      // 表格选择
      multipleSelection: [],

      agreementSupplySalesTerms: {
        // 购进渠道：1-直供 2-所有商业公司购进 3-指定商业公司购进
        buyChannel: 1,
        // 指定商业公司集合（只有购进渠道选择指定商业公司，才传入值）
        supplySalesEnterpriseList: [],
        // 是否拉单支持：0-否 1-是
        pullOrderFlag: 0,
        // 是否分销协议支持：0-否 1-是
        distributionAgreementFlag: 0,
        // 是否全系列品种：0-否 1-是
        allLevelKindsFlag: 0,
        // 供销商品组集合（只有是否全系列品种字段选择否，才传入值）
        supplySalesGoodsGroupList: [
          {
            // 供销商品集合
            supplySalesGoodsList: [],
            // 出库价含税是否维价
            exitWarehouseTaxPriceFlag: false,
            // 零售价含税是否维价
            retailTaxPriceFlag: false,
            // 控销类型：1-无 2-黑名单 3-白名单
            controlSaleType: 1,
            // 控销条件集合
            agreementControlList: [],
            // 控销区域对象（只有勾选区域后，才必须有值）
            controlArea: {
              jsonContent: '',
              selectedDesc: ''
            },
            // 控销客户类型
            controlCustomerTypeList: []
          }
        ]
      },
      agreementSupplySalesTermsRules: {
        buyChannel: [{ required: true, message: '请选择购进渠道', trigger: 'change' }],
        pullOrderFlag: [{ required: true, message: '请选择是否拉单支持', trigger: 'change' }],
        distributionAgreementFlag: [{ required: true, message: '请选择是否分销协议支持', trigger: 'change' }],
        allLevelKindsFlag: [{ required: true, message: '请选择是否全系列品种', trigger: 'change' }]
      }
    };
  },
  mounted() {
  },
  methods: {
    init(eid, firstType, agreementSupplySalesTerms) {
      this.$log('agreementSupplySalesTerms:', eid, firstType, agreementSupplySalesTerms)
      this.eid = eid
      this.firstType = firstType
      this.agreementSupplySalesTerms = agreementSupplySalesTerms
    },
    // 下一步点击
    stepClickMethods(callback) {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          if (this.checkInputData()) {
            this.$log('checkInputData')
            if (callback) callback(this.agreementSupplySalesTerms)
          }
        } else {
          return false;
        }
      })
    },
    // 购进渠道切换
    buyChannelRadioChange(type) {
      if (type != 3) {
        this.agreementSupplySalesTerms.supplySalesEnterpriseList = []
      }
    },
    // 是否全系列品种
    allLevelKindsFlagRadioChange(type) {
      if (type == 1) {
        this.agreementSupplySalesTerms.supplySalesGoodsGroupList = []
      } else {
        let supplySalesGoodsGroupList = [
          {
            // 供销商品集合
            supplySalesGoodsList: [],
            // 出库价含税是否维价
            exitWarehouseTaxPriceFlag: false,
            // 零售价含税是否维价
            retailTaxPriceFlag: false,
            // 控销类型：1-无 2-黑名单 3-白名单
            controlSaleType: 1,
            // 控销条件集合
            agreementControlList: [],
            // 控销区域对象（只有勾选区域后，才必须有值）
            controlArea: {
              jsonContent: '',
              selectedDesc: ''
            },
            // 控销客户类型
            controlCustomerTypeList: []
          }
        ]
        this.agreementSupplySalesTerms.supplySalesGoodsGroupList = supplySalesGoodsGroupList
      }
    },
    // 控销类型切换
    controlSaleTypeRadioChange(type, index) {
      if (type == 1) {
        let supplySalesGoodsGroupItem = this.agreementSupplySalesTerms.supplySalesGoodsGroupList[index]
        let controlArea = {
          jsonContent: '',
          selectedDesc: ''
        }
        supplySalesGoodsGroupItem.agreementControlList = []
        supplySalesGoodsGroupItem.controlArea = controlArea
        supplySalesGoodsGroupItem.controlCustomerTypeList = []
      }
    },
    // 指定商业公司
    addProviderClick() {
      this.addProviderVisible = true
      this.getBusinessPage()
    },
    addProviderHandleSearch() {
      this.addProviderQuery.page = 1
      this.getBusinessPage()
    },
    addProviderHandleReset() {
      this.addProviderQuery = {
        page: 1,
        limit: 10
      }
    },
    // 查询指定商业公司
    async getBusinessPage() {
      this.loading3 = true
      let addProviderQuery = this.addProviderQuery
      let data = await queryBusinessPage(
        addProviderQuery.page,
        addProviderQuery.limit,
        addProviderQuery.ename
      );
      this.loading3 = false
      if (data) {
        let currentArr = this.agreementSupplySalesTerms.supplySalesEnterpriseList
        if (currentArr.length > 0) {
          data.records.forEach(item => {
            let hasIndex = currentArr.findIndex(obj => {
              return obj.eid == item.eid;
            });
            if (hasIndex != -1) {
              item.isSelect = true;
            } else {
              item.isSelect = false;
            }
          });
        }
        this.addProviderList = data.records
        this.addProviderTotal = data.total
      }
    },
    addProviderItemClick(row) {
      let currentArr = this.agreementSupplySalesTerms.supplySalesEnterpriseList
      if (currentArr && currentArr.length > 0 && currentArr.length == 5) {
        this.$common.warn('最多指定五个商业公司')
        return
      }

      currentArr.push(this.$common.clone(row))
      this.agreementSupplySalesTerms.supplySalesEnterpriseList = currentArr

      let arr = this.addProviderList;
      arr.forEach(item => {
        let hasIndex = currentArr.findIndex(obj => {
          return obj.eid == item.eid;
        });
        if (hasIndex != -1) {
          item.isSelect = true;
        } else {
          item.isSelect = false;
        }
      });

      this.addProviderList = this.$common.clone(arr);

    },
    providerRemoveClick(index) {
      let currentArr = this.agreementSupplySalesTerms.supplySalesEnterpriseList
      currentArr.splice(index, 1)
      this.agreementSupplySalesTerms.supplySalesEnterpriseList = currentArr
    },
    // 添加新建表单
    addSupplySalesGoodsGroupListClick() {
      let supplySalesGoodsGroupList = this.agreementSupplySalesTerms.supplySalesGoodsGroupList
      if (supplySalesGoodsGroupList && supplySalesGoodsGroupList.length > 0 && supplySalesGoodsGroupList.length == 6) {
        this.$common.warn('最多新建六个表单')
        return
      }
      // 供销商品组
      let supplySalesGoodsGroupItem = {
        // 供销商品集合
        supplySalesGoodsList: [],
        // 出库价含税是否维价
        exitWarehouseTaxPriceFlag: false,
        // 零售价含税是否维价
        retailTaxPriceFlag: false,
        // 控销类型：1-无 2-黑名单 3-白名单
        controlSaleType: 1,
        // 控销条件集合
        agreementControlList: [],
        // 控销区域对象（只有勾选区域后，才必须有值）
        controlArea: {
          jsonContent: '',
          selectedDesc: ''
        },
        // 控销客户类型
        controlCustomerTypeList: []
      }

      this.agreementSupplySalesTerms.supplySalesGoodsGroupList.push(supplySalesGoodsGroupItem)
    },
    // 选择区域
    selectClick(index) {
      this.currentAreaIndex = index
      let supplySalesGoodsGroupItem = this.agreementSupplySalesTerms.supplySalesGoodsGroupList[index]
      let controlArea = supplySalesGoodsGroupItem.controlArea

      let jsonContent = controlArea.jsonContent
      if (jsonContent) {
        let nodes = JSON.parse(jsonContent)
        let check = []
        this.getSelectId(nodes, check)
        this.selectedData = check
      }

      this.showCityDialog = true
    },
    citySelectConfirm(data) {
      let supplySalesGoodsGroupItem = this.agreementSupplySalesTerms.supplySalesGoodsGroupList[this.currentAreaIndex]
      let controlArea = supplySalesGoodsGroupItem.controlArea

      this.showCityDialog = false
      let jsonContent = JSON.stringify(data.nodes)
      controlArea.jsonContent = jsonContent
      controlArea.selectedDesc = data.desc
    },
    // 递归获取选中的
    getSelectId(arr, selectArr) {
      arr.forEach(item => {
        if (item.children && item.children.length){
          this.getSelectId(item.children, selectArr)
        } else {
          selectArr.push(item.code)
        }
      });
    },
    //------------------管理商品弹框------------------
    // 删除表单
    deleteTableClick(index) {
      let supplySalesGoodsGroupList = this.agreementSupplySalesTerms.supplySalesGoodsGroupList
      if (supplySalesGoodsGroupList.length == 1) {
        this.$common.warn('只剩一个表单，不能删除')
        return
      }
      supplySalesGoodsGroupList.splice(index, 1)
    },
    // 添加商品
    addGoodsClick(index) {
      this.currentAreaIndex = index
      this.addGoodsDialog = true
      this.getGoodsList()
    },
    // 删除表格勾选
    deleteSelectionChange(val, index) {
      let supplySalesGoodsGroupItem = this.agreementSupplySalesTerms.supplySalesGoodsGroupList[index]
      supplySalesGoodsGroupItem.deleteGoodsList = val

    },
    // 删除商品
    deleteGoodsClick(index) {
      let supplySalesGoodsGroupItem = this.agreementSupplySalesTerms.supplySalesGoodsGroupList[index]
      let supplySalesGoodsList = supplySalesGoodsGroupItem.supplySalesGoodsList

      let deleteGoodsList = supplySalesGoodsGroupItem.deleteGoodsList || []
      if (!deleteGoodsList || deleteGoodsList.length == 0) {
        this.$common.warn('请选择需要删除的商品')
        return
      }

      for (let i = 0; i < supplySalesGoodsList.length; i++) {
        let goodsItem = supplySalesGoodsList[i]

        let hasIndex = deleteGoodsList.findIndex(obj => {
          return obj.specificationGoodsId == goodsItem.specificationGoodsId;
        });
        if (hasIndex != -1) {
          supplySalesGoodsList.splice(i, 1)
          i--
        }
      }
      deleteGoodsList = []
    },
    // 商品搜索
    goodsHandleSearch() {
      this.goodsQuery.page = 1
      this.getGoodsList()
    },
    goodsHandleReset() {
      this.goodsQuery = {
        page: 1,
        limit: 10
      }
    },
    downLoadTemplate() {
      downloadByUrl(this.NO_10)
    },
    // 获取商品列表
    async getGoodsList() {

      let goodsQuery = this.goodsQuery
      this.loading2 = false
      let data = await queryGoodsPageList(
        goodsQuery.page,
        goodsQuery.limit,
        goodsQuery.goodsId,
        goodsQuery.goodsName,
        this.eid,
        this.firstType
      );
      this.loading2 = false
      if (data) {
        this.goodsList = data.records
        this.goodsTotal = data.total

        let supplySalesGoodsGroupItem = this.agreementSupplySalesTerms.supplySalesGoodsGroupList[this.currentAreaIndex]
        let supplySalesGoodsList = supplySalesGoodsGroupItem.supplySalesGoodsList
        this.$nextTick(() => {
          this.goodsList.forEach(row => {
            let hasIndex = supplySalesGoodsList.findIndex(obj => {
              return obj.specificationGoodsId == row.specificationGoodsId;
            });
            if (hasIndex != -1) {
              this.$refs.multipleTable.toggleRowSelectionMethod(row);
            }
          });
        })
      }
    },
    // 表格全选
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    // 添加商品 确认点击
    async addGoodsConfirm() {
      if (this.multipleSelection.length > 0) {
        let supplySalesGoodsGroupItem = this.agreementSupplySalesTerms.supplySalesGoodsGroupList[this.currentAreaIndex]
        let supplySalesGoodsList = supplySalesGoodsGroupItem.supplySalesGoodsList

        // 排除其他商品组已经添加的商品
        let hasAddGoodsList = []
        for (let j = 0; j < this.agreementSupplySalesTerms.supplySalesGoodsGroupList.length; j++) {
          let groupItem = this.agreementSupplySalesTerms.supplySalesGoodsGroupList[j]
          if (j != this.currentAreaIndex) {
            hasAddGoodsList.push(...groupItem.supplySalesGoodsList)
          }
        }
        this.$log('hasAddGoodsList:',hasAddGoodsList)
        if (hasAddGoodsList.length > 0) {
          let hasAdd = false
          for (let i = 0; i < this.multipleSelection.length; i++) {
              let goodsItem = this.multipleSelection[i]

              let hasOtherIndex = hasAddGoodsList.findIndex(obj => {
                return obj.specificationGoodsId == goodsItem.specificationGoodsId;
              });
              if (hasOtherIndex != -1) {
                hasAdd = true
              }
          }
          if (hasAdd) {
            this.$common.warn('一个商品只能存在一个商品组')
            return
          }
        }

        for (let i = 0; i < this.multipleSelection.length; i++) {
          let goodsItem = this.multipleSelection[i]

          let hasIndex = supplySalesGoodsList.findIndex(obj => {
            return obj.specificationGoodsId == goodsItem.specificationGoodsId;
          });
          if (hasIndex == -1) {
            goodsItem.exitWarehouseTaxPrice = ''
            goodsItem.retailTaxPrice = ''
            goodsItem.supplyTaxPrice = ''
            goodsItem.exclusiveFlag = false

            supplySalesGoodsList.push(goodsItem)
          }
        }
        this.addGoodsDialog = false
        this.$log('addGoodsConfirm')
      } else {
        this.$common.warn('请选择需要添加的商品')
      }
    },
    change(group, index) {
      // 重新赋值
      let supplySalesGoodsGroupItem = this.agreementSupplySalesTerms.supplySalesGoodsGroupList[group]
      let supplySalesGoodsList = supplySalesGoodsGroupItem.supplySalesGoodsList

      let tmpObj = supplySalesGoodsList[index];
      this.$set(supplySalesGoodsList, index, tmpObj);
    },
    changeOnInput(group, index, type) {
      // 重新赋值
      let supplySalesGoodsGroupItem = this.agreementSupplySalesTerms.supplySalesGoodsGroupList[group]
      let supplySalesGoodsList = supplySalesGoodsGroupItem.supplySalesGoodsList

      let tmpObj = supplySalesGoodsList[index];
      if (type == 1) {
        tmpObj.exitWarehouseTaxPrice = onInputLimit(tmpObj.exitWarehouseTaxPrice, 2)
      } else {
        tmpObj.retailTaxPrice = onInputLimit(tmpObj.retailTaxPrice, 2)
      }
      this.$set(supplySalesGoodsList, index, tmpObj);
    },
    // 校验数据
    checkInputData() {
      let agreementSupplySalesTerms = this.agreementSupplySalesTerms

      if (agreementSupplySalesTerms.buyChannel == 3 && agreementSupplySalesTerms.supplySalesEnterpriseList.length == 0){
        this.$common.warn('请添加指定商业公司')
        return false
      }
      // 不是全系列品
      if (agreementSupplySalesTerms.allLevelKindsFlag == 0) {
        let supplySalesGoodsGroupList = agreementSupplySalesTerms.supplySalesGoodsGroupList
        // 是否有组未添加商品
        // eslint-disable-next-line no-unused-vars
        let hasAddGoods = false
        //  需要填写出库价含税单价
        // eslint-disable-next-line no-unused-vars
        let hasExitWarehouseTaxPrice = false
        //  需要填写零售价含税单价
        // eslint-disable-next-line no-unused-vars
        let hasdRetailTaxPrice = false
        //  需要填写控销区域
        // eslint-disable-next-line no-unused-vars
        let hasControlSaleType = false
        //  需要填写控销区域
        // eslint-disable-next-line no-unused-vars
        let hasControlArea = false

        if (supplySalesGoodsGroupList.length > 0) {
          for (let i = 0; i < supplySalesGoodsGroupList.length; i++) {
            let goodsItem = supplySalesGoodsGroupList[i]

            if (goodsItem.supplySalesGoodsList.length == 0) {
              hasAddGoods = true
            }
            if (goodsItem.exitWarehouseTaxPriceFlag || goodsItem.retailTaxPriceFlag) {
              for (let j = 0; j < goodsItem.supplySalesGoodsList.length; j++) {
                let item = goodsItem.supplySalesGoodsList[j]
                if (goodsItem.exitWarehouseTaxPriceFlag && (!item.exitWarehouseTaxPrice || item.exitWarehouseTaxPrice == 0)) {
                  hasExitWarehouseTaxPrice = true
                }
                if (goodsItem.retailTaxPriceFlag && (!item.retailTaxPrice || item.retailTaxPrice == 0)) {
                  hasdRetailTaxPrice = true
                }
              }
            }
            if (goodsItem.controlSaleType != 1 && goodsItem.agreementControlList.length == 0) {
              hasControlSaleType = true
            }
            if (goodsItem.agreementControlList && goodsItem.agreementControlList.length > 0 && goodsItem.agreementControlList.indexOf(1) != -1) {
              if (!goodsItem.controlArea.jsonContent) {
                hasControlArea = true
              }
            }

          }
        }
        if (hasAddGoods) {
          this.$common.warn('请在表单中添加商品')
          return false
        }
        if (hasExitWarehouseTaxPrice) {
          this.$common.warn('请填写出库价含税单价,不能为0')
          return false
        }
        if (hasdRetailTaxPrice) {
          this.$common.warn('请填写零售价含税单价,不能为0')
          return false
        }
        if (hasControlSaleType) {
          this.$common.warn('请选择控销条件')
          return false
        }
        if (hasControlArea) {
          this.$common.warn('请选择控销区域')
          return false
        }

      }

      return true
    },
    // 导入商品
    goImport(index) {
      this.currentAreaIndex = index
      this.importDialog = true

      let extralData = {
        eid: this.eid,
        firstType: this.firstType
      }

      let info = {
        action: '/admin/pop/api/v1/agreementTerms/importAgreementGoods',
        extralData: extralData
      }
      this.info = info
    },
    onSuccess(data) {
      this.$log(data)
      if (data && data.length > 0) {
        this.$common.n_success('成功导入：' + data.length + '条')
        this.importDialog = false

        let supplySalesGoodsGroupItem = this.agreementSupplySalesTerms.supplySalesGoodsGroupList[this.currentAreaIndex]
        let supplySalesGoodsList = supplySalesGoodsGroupItem.supplySalesGoodsList

        if (supplySalesGoodsList && supplySalesGoodsList.length > 0) {
          data.forEach(item => {
            let hasIndex = supplySalesGoodsList.findIndex(obj => {
              return obj.specificationGoodsId == item.specificationGoodsId;
            });
            this.$log(item)
            if (hasIndex != -1) { // 已经存在
            } else {
              item.exitWarehouseTaxPrice = ''
              item.retailTaxPrice = ''
              item.supplyTaxPrice = ''
              item.exclusiveFlag = false

              supplySalesGoodsList.push(this.$common.clone(item))
            }
          });
        } else {
          this.$log('data:',data)
          data.forEach(item => {
            item.exitWarehouseTaxPrice = ''
            item.retailTaxPrice = ''
            item.supplyTaxPrice = ''
            item.exclusiveFlag = false

            supplySalesGoodsList.push(this.$common.clone(item))
          })
        }

      } else {
        this.$common.warn('暂无匹配商品')
      }
    },
    // 校验输入数值
    onInput(value, limit) {
      return onInputLimit(value, limit)
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
.steps-view{
  .el-step{
    .is-finish{
      .el-step__icon{
        background: #1790ff;
      }
    }
    .el-step__icon{
      background: #CCCCCC;
      border: none;
      .el-step__icon-inner{
        color: $white;
      }
    }
    .el-step__title{
      color: #333;
    }
  }
}
</style>
<style lang="scss" scoped>
@import './index.scss';
</style>
