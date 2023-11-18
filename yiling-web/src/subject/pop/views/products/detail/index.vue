<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <!-- 内容区 -->
    <div class="app-container-content has-bottom-bar">
      <el-form ref="data" :model="data" :rules="rules" label-width="auto">
        <div class="common-box box-1 top-bar">
          <div class="header-bar mar-b-10">
            <div class="sign"></div>
            基本信息
          </div>
          <el-row :gutter="24">
            <el-col :span="6">
              <div class="intro">
                <span class="font-title-color">商品名称：</span>{{ data.name }}
              </div>
            </el-col>
            <el-col :span="6">
              <div class="intro">
                <span class="font-title-color">批准文号/注册证编号：</span>{{ data.licenseNo }}
              </div>
            </el-col>
            <el-col :span="6">
              <div class="intro">
                <span class="font-title-color">售卖规格：</span>{{ data.specifications }}
              </div>
            </el-col>
            <el-col :span="6">
              <div class="intro">
                <span class="font-title-color">基本单位：</span>{{ data.unit }}
              </div>
            </el-col>
          </el-row>
          <el-row :gutter="24">
            <el-col :span="6">
              <div class="intro">
                <span class="font-title-color">生产地址：</span>{{ data.manufacturerAddress }}
              </div>
            </el-col>
            <el-col :span="6">
              <div class="intro">
                <span class="font-title-color">生产厂家：</span>{{ data.manufacturer }}
              </div>
            </el-col>
            <el-col :span="6">
              <div class="intro">
                <span class="font-title-color">销售组织：</span>{{ data.ename }}
              </div>
            </el-col>
          </el-row>
          <div class="header-bar mar-t-8">
            <div class="sign"></div>
            价格库存
          </div>
          <div class="mar-t-8">
            <div class="intro width-800">
              <el-form-item label="商品价格：" size="normal" prop="price">
                <el-input class="width-200" v-model="data.price" size="normal"></el-input>
                元
              </el-form-item>
              <span v-if="data.goodsLimitPrice && data.goodsLimitPrice.lowerLimitPrice" class="limitPrice">限价范围：{{ data.goodsLimitPrice.lowerLimitPrice | toThousand('￥') }} - {{ data.goodsLimitPrice.upperLimitPrice | toThousand('￥') }}</span>
            </div>
            <div>
              <div>
                <div class="intro">
                  <span class="font-title-color">销售规格：</span>
                </div>
              </div>
              <div>
                <div class="unit-content mar-b-8" v-for="(item, index) in unitList" :key="index">
                  <div class="unit-list">
                    <span class="unit-label">数量：</span>
                    <el-input class="package-number" style="width:84px" v-model="item.packageNumber" size="normal"></el-input>
                    <span class="mar-l-8">盒</span>
                    <!-- <span class="mar-l-18 unit-label">本店库存：</span>
                    <el-input style="width:84px" v-model.number="item.realQty" size="normal"></el-input> -->
                    <!-- <span class="mar-l-8">盒</span> -->
                    <span class="mar-l-18">ERP内码：</span>
                    <el-input class="erp-insn" v-model="item.inSn" size="normal"></el-input>
                    <span class="mar-l-18">ERP编码：</span>
                    <el-input class="erp-sn" v-model="item.sn" size="normal"></el-input>
                    <span class="mar-l-18">备注：</span>
                    <el-input class="remark" v-model="item.remark" size="normal"></el-input>
                    <yl-button 
                      class="subscribe" 
                      plain 
                      type="primary" 
                      @click="subscribe(item)" 
                      v-if="item.showSubscriptionButton && item.id"
                    >订阅库存</yl-button>
                    <el-checkbox class="unit-radios" v-model="item.hideFlag">前台隐藏</el-checkbox>
                  </div>
                  <div class="unit-icon" v-if="index + 1 === unitList.length" @click="addRow(item, index)">
                    <img src="@/subject/pop/assets/b2b-products/add.png"/>
                  </div>
                  <div class="unit-icon" v-if="index + 1 <= unitList.length && unitList.length > 1 && !item.id" >
                    <img src="@/subject/pop/assets/b2b-products/del.png" @click="delRow(item, index)"/>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <div v-if="data.standardGoodsAllInfo && data.standardGoodsAllInfo.picBasicsInfoList" >
            <div class="header-bar mar-b-10">
              <div class="sign"></div>
              详细信息
            </div>
            <div class="img-box">
              <div class="intro">
                <span class="font-title-color">商品图片</span>
              </div>
              <div class="flex-wrap imgs">
                <el-image v-for="(img, index) in data.standardGoodsAllInfo.picBasicsInfoList" :key="index" class="img" :src="img.pic"/>
              </div>
              <div class="intro-box font-title-color font-size-base" v-if="data.standardGoodsAllInfo && data.standardGoodsAllInfo.goodsInstructionsInfo">
                <div class="flex-row-left marb8">
                  <div class="box-view marr150">
                    <div class="box">
                      成分：{{ data.standardGoodsAllInfo.goodsInstructionsInfo.drugDetails || '- -' }}
                    </div>
                  </div>
                  <div class="box-view">
                    <div class="box">
                      性状：{{ data.standardGoodsAllInfo.goodsInstructionsInfo.drugProperties || '- -' }}
                    </div>
                  </div>
                </div>
                <div class="flex-row-left marb8">
                  <div class="box-view marr150">
                    <div class="box">
                      适应症：{{ data.standardGoodsAllInfo.goodsInstructionsInfo.indications || '- -' }}
                    </div>
                  </div>
                  <div class="box-view">
                    <div class="box">
                      用法用量：{{ data.standardGoodsAllInfo.goodsInstructionsInfo.usageDosage || '- -' }}
                    </div>
                  </div>
                </div>
                <div class="flex-row-left marb8">
                  <div class="box-view marr150">
                    <div class="box">
                      不良反应：{{ data.standardGoodsAllInfo.goodsInstructionsInfo.adverseEvents || '- -' }}
                    </div>
                  </div>
                  <div class="box-view">
                    <div class="box">
                      禁忌：{{ data.standardGoodsAllInfo.goodsInstructionsInfo.contraindication || '- -' }}
                    </div>
                  </div>
                </div>
                <div class="flex-row-left marb8">
                  <div class="box-view marr150">
                    <div class="box">
                      注意事项：{{ data.standardGoodsAllInfo.goodsInstructionsInfo.noteEvents || '- -' }}
                    </div>
                  </div>
                  <div class="box-view">
                    <div class="box">
                      药物相互作用：{{ data.standardGoodsAllInfo.goodsInstructionsInfo.interreaction || '- -' }}
                    </div>
                  </div>
                </div>
                <div class="flex-row-left marb8">
                  <div class="box-view marr150">
                    <div class="box">
                      保质期：{{ data.standardGoodsAllInfo.goodsInstructionsInfo.shelfLife || '- -' }}
                    </div>
                  </div>
                  <div class="box-view">
                    <div class="box">
                      执行标准：{{ data.standardGoodsAllInfo.goodsInstructionsInfo.executiveStandard || '- -' }}
                    </div>
                  </div>
                </div>
                <div class="flex-row-left marb8">
                  <div class="box-view marr150">
                    <div class="box">
                      贮藏：{{ data.standardGoodsAllInfo.goodsInstructionsInfo.storageConditions || '- -' }}
                    </div>
                  </div>
                  <div style="width: 506px;"></div>
                </div>
              </div>
              <!-- 医疗器械详细信息 -->
              <div class="intro-box font-title-color font-size-base" v-if="data.standardGoodsAllInfo && data.standardGoodsAllInfo.medicalInstrumentInfo">
                <div class="flex-row-left marb8">
                  <div class="box-view marr150">
                    <div class="box">
                      结构组成：{{ data.standardGoodsAllInfo.medicalInstrumentInfo.structure || '- -' }}
                    </div>
                  </div>
                  <div class="box-view">
                    <div class="box">
                      注意事项：{{ data.standardGoodsAllInfo.medicalInstrumentInfo.noteEvents || '- -' }}
                    </div>
                  </div>
                </div>
                <div class="flex-row-left marb8">
                  <div class="box-view marr150">
                    <div class="box">
                      适用范围：{{ data.standardGoodsAllInfo.medicalInstrumentInfo.useScope || '- -' }}
                    </div>
                  </div>
                  <div class="box-view">
                    <div class="box">
                      使用说明：{{ data.standardGoodsAllInfo.medicalInstrumentInfo.usageDosage || '- -' }}
                    </div>
                  </div>
                </div>
                <div class="flex-row-left marb8">
                  <div class="box-view marr150">
                    <div class="box">
                      储藏条件：{{ data.standardGoodsAllInfo.medicalInstrumentInfo.storageConditions || '- -' }}
                    </div>
                  </div>
                  <div class="box-view">
                    <div class="box">
                      包装：{{ data.standardGoodsAllInfo.medicalInstrumentInfo.packingInstructions || '- -' }}
                    </div>
                  </div>
                </div>
                <div class="flex-row-left marb8">
                  <div class="box-view marr150">
                    <div class="box">
                      有效期：{{ data.standardGoodsAllInfo.medicalInstrumentInfo.expirationDate || '- -' }}
                    </div>
                  </div>
                  <div class="box-view">
                    <div class="box">
                      备注：{{ data.standardGoodsAllInfo.medicalInstrumentInfo.remark || '- -' }}
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="header-bar mar-b-10">
            <div class="sign"></div>
            特殊信息
          </div>
          <div class="intro">
            <span class="font-title-color">上下架状态</span>
          </div>
          <div class="select-box flex-row-left mar-t-8 mar-l-8">
            <el-radio v-model="isPutOn" label="1">上架</el-radio>
            <el-radio v-model="isPutOn" label="2">下架</el-radio>
          </div>
        </div>
      </el-form>
      <div class="flex-row-center bottom-view">
        <yl-button plain type="primary" @click="$router.go(-1)">返回</yl-button>
        <yl-button type="primary" @click="saveChange">保存</yl-button>
      </div>
      <!-- 订阅弹窗 -->
      <yl-dialog 
        class="popup"
        title="库存订阅" 
        :visible.sync="showDialog" 
        width="800px" 
        @confirm="confirm">
        <div class="popup-box pad-16">
          <div class="title">
            <el-row>
              <el-col :span="8">库存组织</el-col>
              <el-col :span="8">库存类型</el-col>
              <el-col :span="5">来源方ERP内码</el-col>
              <el-col :span="3"></el-col>
            </el-row>
          </div>
          <div class="content" v-for="(item, index) in unSubscribe" :key="index" >
            <el-row>
              <el-col :span="8">
                <el-select v-model="item.subscriptionEid" placeholder="请选择库存组织" @change="changeBussiness($event, index)">
                  <template v-for="child in bussinessList">
                    <el-option 
                      :key="child.id" 
                      :label="child.name" 
                      :value="child.id"
                      v-if="child.id !== data.eid"
                      >
                    </el-option>
                  </template>
                </el-select>
              </el-col>
              <el-col :span="8">
                <el-select v-model="item.subscriptionType" placeholder="请选择库存类型">
                  <el-option 
                    v-for="child in getSubscriptio"
                    :key="child.value"
                    :label="child.label"
                    :value="child.value">
                  </el-option>
                </el-select>
              </el-col>
              <el-col :span="5">
                <el-input v-model="item.inSn" size="normal"></el-input>
              </el-col>
              <el-col :span="3" class="flex-left">
                <div class="unit-icon" v-if="index + 1 === unSubscribe.length" @click="addSubscribeRow(item, index)">
                  <img src="@/subject/pop/assets/b2b-products/add.png"/>
                </div>
                <div class="unit-icon" v-if="index + 1 <= unSubscribe.length && unSubscribe.length > 1 || (index === 0 && item.id)" >
                  <img src="@/subject/pop/assets/b2b-products/del.png" @click="delSubscribeRow(item, index)"/>
                </div>
              </el-col>
            </el-row>
          </div>
        </div>
      </yl-dialog>
    </div>
  </div>
</template>

<script>
import { getProductDetail, ProductEdit, getEnterpriseMainPart, mainPartList, getSubscriptionList } from '@/subject/pop/api/products';
import { getCurrentUser } from '@/subject/pop/utils/auth';
import {
  goodsStatus,
  enterpriseType,
  goodsOutReason,
  goodsPatent,
  subscription
} from '@/subject/pop/utils/busi';
export default {
  components: {},
  computed: {
    goodStatus() {
      return goodsStatus();
    },
    companyType() {
      return enterpriseType();
    },
    goodsReason() {
      return goodsOutReason();
    },
    // 专利非专利
    patent() {
      return goodsPatent();
    },
    isYiLing() {
      let user = getCurrentUser();
      let flag = null;
      if (user.currentEnterpriseInfo) {
        if (user.currentEnterpriseInfo.yilingFlag) {
          flag = !!user.currentEnterpriseInfo.yilingFlag;
        }
      }
      return flag;
    },
    getSubscriptio() {
      return subscription()
    }
  },
  data() {
    var isPriceVlidator = (rule, value, callback) => {
      var pattern = /^0\.([1-9]|\d[1-9])$|^[1-9]\d{0,8}\.\d{0,4}$|^[1-9]\d{0,8}$/;
      if (!pattern.test(value)) {
        return callback(new Error('价格必须大于0'));
      } else if (this.data.goodsLimitPrice && value > this.data.goodsLimitPrice.upperLimitPrice || (this.data.goodsLimitPrice && value < this.data.goodsLimitPrice.lowerLimitPrice)) {
        return callback(new Error('商品价格需在限价范围内'));
      } else {
        return callback();
      }
    };
    // var isQtyVlidator = (rule, value, callback) => {
    //   //最长9位数字，可修改
    //   var reg = /^((?!-1)\d{0,10})$/; 
    //   if (!reg.test(value)) {
    //     return callback(new Error('请输入正整数'));
    //   } else return callback();
    // };

    return {
      // 头部导航
      navList: [
        {
          title: '首页',
          path: '/dashboard'
        },
        {
          title: '商品管理',
          path: ''
        },
        {
          title: '商品列表',
          path: '/products/products_index'
        },
        {
          title: '商品详情'
        }
      ],
      data: {
        standardGoodsAllInfo: {
          baseInfo: {
            specificationInfo: []
          }
        }
      },
      type: '',
      rules: {
        price: [
          { required: true, validator: isPriceVlidator, trigger: 'blur' }
        ],
        // realQty: [{ required: true, validator: isQtyVlidator, trigger: 'blur' }],
        unit: [{ required: false, message: '请输入基本单位', trigger: 'blur' }]
      },
      unitList: [],
      isPutOn: '1',
      showDialog: false,
      bussinessList: [],
      unSubscribe: [
        {
          inSn: '',
          // 下拉框企业的id
          subscriptionEid: '',
          subscriptionEname: '',
          subscriptionType: ''
        }
      ],
      row: '',
      rowData: ''
    };
  },
  mounted() {
    this.id = this.$route.params.id;
    this.eid = this.$route.params.eid;
    this.getData();
    this.getMainList();
    this.$nextTick(() => {
      // type: see -只查看 reSubmit -重新提交 为空则为编辑
      this.type = this.$route.query.type;
    });
  },
  methods: {
    addRow(item, index) {
      let obj = {
        frozenQty: '',
        id: '',
        sn: '',
        inSn: '',
        packageNumber: '',
        // realQty: '',
        qty: '',
        remark: '',
        showSubscriptionButton: true
      };
      this.unitList.push(obj);
    },
    delRow(item, index) {
      this.unitList.splice(index, 1);
    },
    async getData() {
      this.$common.showLoad();
      let data = await getProductDetail(this.id);
      this.$common.hideLoad();
      if (data !== undefined) {
        this.data = data;
        this.unitList = data.goodsSkuList;
        if (data.goodsSkuList && data.goodsSkuList.length === 0) {
          this.addRow();
        }
        this.isPutOn = String(data.goodsStatus);
      }
    },
    // 保存
    async saveChange() {
      for (let i = 0; i < this.unitList.length; i ++) {
        if (!/(^[1-9]\d*$)/.test(this.unitList[i].packageNumber)) {
          return this.$common.error('数量只能是大于0的正整数');
        }
        // if (!/(^[0-9]\d*$)/.test(this.unitList[i].realQty)) {
        //   return this.$common.error('库存只能是正整数');
        // }
        // if (Number(this.unitList[i].packageNumber) > Number(this.unitList[i].realQty)) {
        //   return this.$common.error('商品库存必须大于数量');
        // }
        // if (Number(this.unitList[i].realQty) < Number(this.unitList[i].frozenQty)) {
        //   return this.$common.error('总库存必须大于等于占用库存');
        // }
      }
      this.$refs.data.validate(async (valid) => {
        if (valid) {
          this.$common.showLoad();
          this.unitList.forEach(v => {
            delete v.qty
          });
          let data = await ProductEdit(
            this.id,
            this.isPutOn,
            this.data.price,
            this.unitList
          );
          this.$common.hideLoad();
          if (data !== undefined) {
            this.$common.n_success('保存成功');
            this.$router.go(-1);
          }
        } else {
          return false;
        }
      });
    },
    // 获取以岭主体企业列表
    async getMainList() {
      let data = await getEnterpriseMainPart()
      if (data !== undefined) {
        this.bussinessList = data.list
      }
    },
    // 获取订阅列表
    async subscribe(row) {
      this.unSubscribe = [
        {
          inSn: '',
          // 下拉框企业的id
          subscriptionEid: '',
          subscriptionEname: '',
          subscriptionType: ''
        }
      ]
      this.$common.showLoad();
      let data = await getSubscriptionList(row.id)
      this.$common.hideLoad();
        if (data !== undefined) {
          this.$nextTick(()=>{
          data.length > 0 ? this.unSubscribe = data : ''

        })
      }
      this.row = row;
      this.showDialog = true;
    },
    async confirm() {
      // 保存
      for (let i = 0; i < this.unSubscribe.length; i ++) {
        console.log('this.unSubscribe[i]', this.unSubscribe[i]);
        if (this.unSubscribe[i].subscriptionEid == '') {
          return this.$common.error('请输入库存组织');
        }
        if (this.unSubscribe[i].subscriptionType == '') {
          return this.$common.error('请输入库存类型');
        }
        if (this.unSubscribe[i].inSn == '') {
          return this.$common.error('请输入来源方ERP内码');
        }
        for (let j = i + 1; j < this.unSubscribe.length; j ++) {
          if ( (this.unSubscribe[i]['subscriptionEid'] == this.unSubscribe[j]['subscriptionEid'] ) && ( this.unSubscribe[i]['inSn'] == this.unSubscribe[j]['inSn'] )) {
              return this.$common.error('库存组织+来源方ERP内码不能重复！');
          }
        }
      }
      this.$common.showLoad();
      let data = await mainPartList(
          this.data.eid,
          '',
          this.row.id,
          this.unSubscribe
      );
      this.$common.hideLoad();
      if (data !== undefined) {
        this.$common.n_success('保存成功');
        this.row = '';
        this.showDialog = false;
      }
    },
    addSubscribeRow(item, index) {
      let obj = {
        inSn: '',
        // 下拉框企业的id
        subscriptionEid: '',
        subscriptionEname: '',
        subscriptionType: ''
      };
      this.unSubscribe.push(obj);
    },
    delSubscribeRow(item, index) {
      this.unSubscribe.splice(index, 1);
    },
    changeBussiness(v, index) {
      for (let i = 0; i < this.bussinessList.length; i++) {
        const element = this.bussinessList[i];
        if (element.id === v) {
          this.unSubscribe[index].subscriptionEname = element.name
        }
      }
    }
  }
};
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
