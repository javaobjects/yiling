<template>
  <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <div class="c-box">
        <el-form 
          :model="form" 
          :rules="rules" 
          ref="dataForm" 
          label-width="150px" 
          class="demo-ruleForm">
          <el-form-item label="兑换商品：" prop="behaviorId">
            <div v-show="dataList && dataList.length > 0">
              <yl-table
                :show-header="true"
                :stripe="true"
                :list="dataList"
                >
                <el-table-column :label="dataType == 3 ? '商品id' : '优惠券ID'" min-width="80" align="center" prop="goodsId">
                </el-table-column>
                <el-table-column :label="dataType == 3 ? '商品' : '优惠券名称'" min-width="120" align="center" prop="goodsName">
                </el-table-column>
                <el-table-column :label="dataType == 3 ? '价值' : '面额'" min-width="120" align="center" prop="price">
                </el-table-column>
                <el-table-column label="可用库存" min-width="120" align="center" prop="availableQuantity">
                </el-table-column>
              </yl-table>
            </div>
            <span class="add-span" @click="addClick" v-if="type == 1">设置兑换商品</span>
          </el-form-item>
          <el-form-item label="兑换所需积分：" prop="exchangeUseIntegral">
            <el-input 
              v-model.trim="form.exchangeUseIntegral" 
              maxlength="10" 
              placeholder="请输入"
              :disabled="type == 2"
              @input="e => (form.exchangeUseIntegral = checkInput(e))">
            </el-input> 积分
          </el-form-item>
          <el-form-item label="可兑换数量：" prop="canExchangeNum">
            <el-input 
              v-model.trim="form.canExchangeNum" 
              maxlength="10" 
              placeholder="请输入"
              :disabled="type == 2"
              @blur="blurNum"
              @input="e => (form.canExchangeNum = checkInput(e))">
            </el-input> 库存
          </el-form-item>
          <el-form-item label="单品兑换限制：" prop="singleMaxExchange">
            <el-input 
              v-model.trim="form.singleMaxExchange" 
              maxlength="10" 
              placeholder="请输入整数，0代表不限制"
              :disabled="type == 2"
              @input="e => (form.singleMaxExchange = checkInput(e))">
            </el-input> 份/用户
          </el-form-item>
          <el-form-item label="兑换限制生效时间：" >
            <el-date-picker
              v-model="form.time"
              type="datetimerange"
              format="yyyy/MM/dd HH:mm:ss"
              value-format="yyyy-MM-dd HH:mm:ss"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              :default-time="['00:00:00', '23:59:59']"
              :disabled="type == 2"
              :picker-options="pickerOptions">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="是否区用户身份：" prop="userFlag">
            <el-radio-group v-model="form.userFlag" :disabled="type == 2">
              <el-radio :label="1">全部</el-radio>
              <el-radio :label="2">指定会员类型</el-radio>
            </el-radio-group>
            
          </el-form-item>
          <el-form-item label="" prop="memberIdList" v-if="form.userFlag == 2">
            <el-checkbox-group v-model="form.memberIdList" :disabled="type == 2"> 
              <el-checkbox 
                class="option-class" 
                v-for="item in tagList" 
                :key="item.id" 
                :label="item.id" >
                {{ item.name }}(ID：{{ item.id }})
              </el-checkbox>
            </el-checkbox-group>
          </el-form-item>
          <el-form-item label="有效期(上下架)：" prop="upDownTime">
            <el-date-picker
              v-model="form.upDownTime"
              type="datetimerange"
              format="yyyy/MM/dd HH:mm:ss"
              value-format="yyyy-MM-dd HH:mm:ss"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              :default-time="['00:00:00', '23:59:59']"
              :disabled="type == 2"
              :picker-options="pickerOptions">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="排序值：" prop="sort">
            <el-input 
              v-model.trim="form.sort" 
              maxlength="3" 
              show-word-limit 
              placeholder="请输入排序值，排序值越大越靠前"
              :disabled="type == 2"
              @input="e => (form.sort = checkInput(e))">
            </el-input>
          </el-form-item>
        </el-form>
      </div>
    </div>
    <div class="bottom-view flex-row-center">
      <yl-button type="primary" plain @click="$router.go(-1)">返回</yl-button>
      <yl-button type="primary" v-if="type != 2" @click="submitClick">提交</yl-button>
    </div>
    <add-commodity v-if="commodityShow" ref="commodityRef" @commodityChange="commodityChange"></add-commodity>
  </div>
</template>

<script>
import addCommodity from '../../components/addCommodity'
import { integralSave, integralExchangeGoodsGet } from '@/subject/admin/api/b2b_api/integral_record'
import { getMemberList } from '@/subject/admin/api/b2b_api/membership'
import { formatDate } from '@/subject/admin/utils'
export default {
  name: 'PointsRedemptionEstablish',
  components: {
    addCommodity
    
  },
  data() {
    return {
      form: {
        id: '',
        exchangeUseIntegral: '',
        canExchangeNum: '',
        singleMaxExchange: '',
        time: [],
        userFlag: '',
        memberIdList: [],
        upDownTime: [],
        sort: ''
      },
      rules: {
        exchangeUseIntegral: [{ required: true, message: '请输入兑换所需积分', trigger: 'blur' }],
        canExchangeNum: [{ required: true, message: '请输入可兑换数量', trigger: 'blur' }],
        singleMaxExchange: [{ required: true, message: '请输入单品兑换限制', trigger: 'blur' }],
        userFlag: [{ required: true, message: '请选择是否区用户身份', trigger: 'change' }],
        upDownTime: [{ required: true, message: '请选择有效期', trigger: 'change' }],
        memberIdList: [{ required: true, message: '请选择指定会员', trigger: 'change' }]
      },
       // 选择当前日期之后的时间
      pickerOptions: {
        disabledDate(time) {
          //如果没有后面的-8.64e7就是不可以选择今天的
          return time.getTime() < Date.now() - 8.64e7;
        }
      },
      //1 创建 2查看 3编辑
      type: 1,
      //兑换商品弹窗是否显示
      commodityShow: false,
      //表格数据
      dataList: [],
      //区分表格title文案
      dataType: 1,
      tagList: []
    }
  },
  mounted() {
    //获取标签数据
    this.labelData();
    let query = this.$route.params;
    // query.type  //1 创建 2查看 3编辑
    if (query.type && query.type != 1) {
      this.type = query.type
      this.getData(query.id)
    }
  },
  methods: {
    //判断可兑换数量小于可用库存
    blurNum(val) {
      if (this.dataList && this.dataList.length > 0) {
        if (this.form.canExchangeNum > this.dataList[0].availableQuantity) {
          this.$common.warn('可兑换数量不能大于可用库存')
          this.form.canExchangeNum = ''
        }
      } else {
        this.$common.warn('请先设置兑换商品')
        this.form.canExchangeNum = ''
      }
    },
    //获取查询数据
    async getData(id) {
      let data = await integralExchangeGoodsGet(
        id
      )
      if (data) {
        this.form = {
          ...data
        }
        this.$nextTick(() => {
          if (data.validStartTime && data.validStartTime != -28800000) {
            this.$set(this.form, 'upDownTime', [formatDate(data.validStartTime, 'yyyy-MM-dd hh:mm:ss'), formatDate(data.validEndTime	, 'yyyy-MM-dd hh:mm:ss')]);
          } else {
            this.$set(this.form, 'upDownTime', [])
          }
          if (data.exchangeStartTime && data.exchangeStartTime != -28800000) {
            this.$set(this.form, 'time', [formatDate(data.exchangeStartTime, 'yyyy-MM-dd hh:mm:ss'), formatDate(data.exchangeEndTime	, 'yyyy-MM-dd hh:mm:ss')]);
          }
        });
        if (data.goodsType == 1 || data.goodsType == 2) {
          this.dataType = 3
        } else {
          this.dataType = 1
        }
        this.dataList = [{
          goodsId: data.goodsId,
          goodsName: data.goodsName,
          price: data.price	,
          availableQuantity: data.availableQuantity,
          goodsType: data.goodsType
        }]
      }
    },
    //获取标签数据
    async labelData() {
      let data = await getMemberList()
      if (data) {
        this.tagList = data.list;
      }
    },
    //设置兑换商品 返回
    commodityChange(val) {
      let data = val.conter;
      this.dataType = val.type;
      if (val.type == 1) {
        this.dataList = [
          {
            goodsId: data.id,
            goodsName: data.name,
            price: data.faceValue,
            availableQuantity: data.surplusCount,
            goodsType: 3
          }
        ]
      } else if (val.type == 2) {
        this.dataList = [
          {
            goodsId: data.id,
            goodsName: data.name,
            price: data.faceValue,
            availableQuantity: data.surplusCount,
            goodsType: 4
          }
        ]
      } else {
        this.dataList = [{
          goodsId: data.id,
          goodsName: data.name,
          price: data.price,
          availableQuantity: data.availableQuantity,
          goodsType: data.goodsType
        }]
      }
      
    },
    //点击设置兑换商品
    addClick() {
      this.commodityShow = true;
      this.$nextTick(() => {
        this.$refs.commodityRef.init(true)
      })
    },
    //点击提交
    submitClick() {
      this.$refs['dataForm'].validate( async(valid) => {
        if (valid) {
          if (this.dataList && this.dataList.length < 1) {
            this.$common.warn('请设置兑换商品')
          } else {
            let form = this.form;
            if (form.canExchangeNum > this.dataList[0].availableQuantity) {
              this.$common.warn('可兑换数量不能大于可用库存')
              this.form.canExchangeNum = ''
            } else {
              this.$common.showLoad();
              let data = await integralSave(
                form.id,
                this.dataList && this.dataList.length > 0 ? this.dataList[0].goodsId : '',
                this.dataList && this.dataList.length > 0 ? this.dataList[0].goodsName : '',
                this.dataList && this.dataList.length > 0 ? this.dataList[0].goodsType : '',
                form.exchangeUseIntegral,
                form.canExchangeNum,
                form.singleMaxExchange,
                form.time && form.time.length > 0 ? form.time[0] : '',
                form.time && form.time.length > 0 ? form.time[1] : '',
                form.userFlag,
                form.memberIdList,
                form.upDownTime && form.upDownTime.length > 0 ? form.upDownTime[0] : '',
                form.upDownTime && form.upDownTime.length > 1 ? form.upDownTime[1] : '',
                form.sort
              )
              this.$common.hideLoad();
              if (data !== undefined) {
                this.$common.alert('保存成功', r => {
                  this.$router.go(-1)
                })
              }
            }  
          }
        }
      })
    },
    checkInput(val) {
      val = val.replace(/[^0-9]/gi, '')
      if (val < 0) {
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