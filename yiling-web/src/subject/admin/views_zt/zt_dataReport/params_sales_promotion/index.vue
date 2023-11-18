<!--
 * @Description:
 * @Author: xuxingwang
 * @Date: 2022-02-23 16:46:25
 * @LastEditTime: 2022-02-28 18:05:55
 * @LastEditors: xuxingwang
-->
<template>
  <div class="app-container">
    <div class="app-container-content">
      <div class="pad-b-100">
        <yl-table border :show-header="true" :list="dataList" :total="query.total" :page.sync="query.page" :limit.sync="query.limit" :loading="loading" @getList="getList">
          <el-table-column min-width="100" align="center" label="类型ID" prop="id"></el-table-column>
          <el-table-column min-width="100" align="center" label="促销类型" prop="name"></el-table-column>
          <el-table-column min-width="100" align="center" label="操作时间" prop="updateTime">
            <template slot-scope="{ row }">
              <div>{{ row.updateTime | formatDate }}</div>
            </template>
          </el-table-column>
          <el-table-column min-width="100" align="center" label="操作人" prop="updateUserName"></el-table-column>
          <el-table-column align="center" label="操作">
            <template slot-scope="{ row }">
              <yl-button type="text" @click="see(row)">查看</yl-button>
              <yl-button type="text" @click="addGoods(row)">添加商品</yl-button>
            </template>
          </el-table-column>
        </yl-table>
      </div>
      <div class="bottom-view flex-row-center">
        <yl-button type="primary" plain @click="goBack">返回</yl-button>
      </div>
      <!-- 添加商品 -->
      <yl-dialog :visible.sync="addShow" width="1200px" :title="addGoodsTitle" @confirm="confirm">
        <div class="dialog-content">
          <el-form class="pad-t-8" ref="form" :rules="formRules" :model="form" label-width="110px">
            <el-row>
              <el-col :span="12" :offset="0">
                <el-form-item label="促销名称：" prop="activityName">
                  <el-input v-model="form.activityName" placeholder="请输入"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" :offset="0">
                <el-form-item label="商品规格：" prop="goodsUnit">
                  <el-input v-model="form.goodsUnit" disabled></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>

              <el-col :span="12" :offset="0">
                <el-form-item label="商家名称：" prop="bussinessId">
                  <el-select v-model="form.bussinessId" filterable remote reserve-keyword placeholder="请输入关键词" :remote-method="remoteMethod1" :loading="goodsloading1">
                    <el-option v-for="item in options1" :key="item.id" :label="item.name" :value="item.id"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12" :offset="0">
                <el-form-item label="以岭商品ID：" prop="ylGoodsId">
                  <el-input v-model="form.ylGoodsId" disabled></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12" :offset="0">
                <el-form-item label="商品名称：" prop="goodsName">
                  <el-select v-model="form.goodsName" filterable remote reserve-keyword placeholder="请输入关键词" :remote-method="remoteMethod" :loading="goodsloading" @change="selectGoods">
                    <el-option v-for="item in options" :key="item.id" :label="item.goodsName" :value="item">
                      <span>{{ item.goodsName }}---{{ item.goodsSpecifications }}---{{ item.goodsInSn }}</span>
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12" :offset="0">
                <el-form-item label="商品内码：" prop="goodsInSn">
                  <el-input v-model="form.goodsInSn" disabled></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12" :offset="0">
                <el-form-item label="订单来源：" prop="orderSource">
                  <el-select v-model="form.orderSource" placeholder="请选择订单来源">
                    <el-option
                      v-for="item in orderSource"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12" :offset="0">
                <el-form-item label="奖励金额：" prop="rewardAmount">
                  <el-input placeholder="请输入内容" v-model="form.rewardAmount" class="input-with-select">
                    <!-- 奖励类型：1-金额 2-百分比	 阶梯只能是元-->
                    <el-select v-model="form.rewardType" slot="append" placeholder="请选择">
                      <el-option label="元/盒" value="1"></el-option>
                      <!-- 小三元活动 id=9 rewardType 只能选择1金额 不能选择百分比， -->
                      <el-option label="%销售额" value="2"></el-option>
                    </el-select>
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12" :offset="0" class="time-form">
                <el-form-item label="执行时间：" prop="startTime">
                  <!--  -->
                  <el-date-picker v-model="form.startTime" type="date" format="yyyy 年 MM 月 dd 日" value-format="yyyy-MM-dd" range-separator="至" placeholder="开始时间">
                  </el-date-picker>
                </el-form-item>
                <div class="line">-</div>
                <el-form-item label="" label-width="0" prop="endTime">
                  <el-date-picker v-model="form.endTime" type="date" format="yyyy 年 MM 月 dd 日" value-format="yyyy-MM-dd" range-separator="至" placeholder="结束时间" :picker-options="pickerOptions1" @change="changeDate">
                  </el-date-picker>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </div>
      </yl-dialog>
    </div>
  </div>
</template>

<script>
import { getParamsTypeList, getBussinessInfoByBussinessName, saveActivityGoods, getActivityGoodsList } from '@/subject/admin/api/zt_api/dataReport';
import { reportOrderSource } from '@/subject/admin/busi/zt/dataReport'

export default {
  name: 'ZtParamsSalesPromotion',
  components: {},
  computed: {
    // 订单来源
    orderSource() {
      const reportOrderData = reportOrderSource()
      reportOrderData.forEach(item => {
        item.value = Number(item.value)
      })
      return reportOrderData
    }
  },
  data() {
    const validateStart = (rule, value, callback) => {
      console.log(value);
      if (value) {
        let startTime = new Date(this.form.startTime).getTime();
        let endTime = new Date(this.form.endTime).getTime();
        if (startTime > endTime) {
          callback(new Error('结束日期必须大于开始日期，请重新选择！'));
        } else {
          callback();
        }
      } else {
        callback(new Error('请选择'));
      }
    };
    var validateEnd = (rule, value, callback) => {
      console.log(value);
      if (value) {
        let startTime = new Date(this.form.startTime).getTime();
        let endTime = new Date(this.form.endTime).getTime();
        if (startTime > endTime) {
          callback(new Error('结束日期必须大于开始日期，请重新选择！'));
        } else {
          callback();
        }
      } else {
        callback(new Error('请选择'));
      }
    };
    var isRewardVlidator = (rule, value, callback) => {
      var pattern = /^(([1-9]{1}\d*)|(0{1}))(\.\d{1,2})?$/
      if (!pattern.test(value)) {
        return callback(new Error('请输入奖励金额,不可超过两位小数'))
      } else if (value == 0) {
        return callback(new Error('请输入正确的奖励金额不可为0'))
      } else {
        return callback()
      }
    }
    return {
      typeID: '',
      dataList: [],
      query: {
        total: 0,
        page: 1,
        limit: 10
      },
      loading: false,
      addShow: false,
      addGoodsTitle: '促销活动--',
      ruleId: '',// 查看商品列表传参
      form: {
        id: '',
        activityName: '',
        bussinessId: '',
        goodsName: '',
        goodsUnit: '',
        ylGoodsId: '',
        rewardAmount: '',
        rewardType: '1',
        rewardPercentage: '',
        startTime: '',
        endTime: '',
        goodsInSn: '',
        orderSource: ''
      },
      formRules: {
        activityName: [
          { required: true, message: '请输入', trigger: 'blur' }
        ],
        bussinessId: [
          { required: true, message: '请输入', trigger: 'blur' }
        ],
        goodsName: [
          { required: true, message: '请输入', trigger: 'blur' }
        ],
        goodsUnit: [
          { required: true, message: '请输入', trigger: 'blur' }
        ],
        ylGoodsId: [
          { required: true, message: '请输入', trigger: 'blur' }
        ],
        thresholdCount: [
          { required: true, message: '请输入', trigger: 'blur' }
        ],
        rewardAmount: [
          { required: true, validator: isRewardVlidator, message: '请输入正确的奖励金额', trigger: ['change', 'blur'] }
        ],
        startTime: [
          { required: true, validator: validateStart, trigger: 'blur' }
        ],
        endTime: [
          { required: true, validator: validateEnd, trigger: 'blur' }
        ],
        orderSource: [
          { required: true, message: '请选择订单来源', trigger: 'blur' }
        ]
      },
      // 远程搜索参数
      goodsloading: false,
      options: [],
      goodsloading1: false,
      options1: [],
      pickerOptions1: {
        disabledDate: (time) => {
          if (this.form.startTime) {
            return time.getTime() < new Date(this.form.startTime).getTime()
          }
        }
      },
      //  查看商品类型下的商品
      showTypeList: false,
      seeTypeName: ''// 点击查看商品列表传参
    };
  },
  mounted() {
    if (this.$route.params.id) {
      this.typeID = this.$route.params.id
    }
    this.getList();
  },
  methods: {
    async getList() {
      // 1-商品类型 2-促销活动 3-阶梯规则 4-会员返利
      this.loading = true;
      let data = await getParamsTypeList(this.query.page, this.query.limit, 2, this.typeID);
      console.log(data);
      this.loading = false;
      if (data !== undefined) {
        this.dataList = data.records;
        this.query.total = data.total;
      }
    },
    // 查看商品
    see(row) {
      this.$router.push({
        name: 'ZtParamsSalesPromotionDetail',
        params: {
          id: row.id,
          name: row.name,
          fatherId: this.$route.params.id,
          fatherType: this.$route.params.type
        }
      })
    },
    //  添加商品
    addGoods(row) {
      this.form.activityName = ''
      this.form.bussinessId = ''
      this.form.goodsName = ''
      this.form.goodsUnit = ''
      this.form.ylGoodsId = ''
      this.form.rewardAmount = ''
      this.form.rewardPercentage = ''
      this.form.startTime = ''
      this.form.endTime = ''
      this.form.id = row.id;
      this.form.orderSource = ''
      this.addGoodsTitle = '促销活动--' + row.name;
      this.addShow = true;
      this.form.goodsInSn = ''
      this.options1 = []
      this.options = []
    },
    confirm() {
      this.$refs['form'].validate(async (valid) => {
        if (valid) {
          this.$common.showLoad();
          /**
           *  小三元活动    rewardType 只能选择1金额 不能选择百分比，rewardAmount 传金额 rewardPercentage 不用传
           *  其他活动，  rewardType 选择金额1，  rewardAmount 传金额   选择百分比， rewardPercentage传金额
           *
          */
          let rewardAmount = ''
          let rewardPercentage = ''
          if (this.form.rewardType == 1) {
            rewardAmount = this.form.rewardAmount
            rewardPercentage = ''
          } else {
            rewardAmount = ''
            rewardPercentage = this.form.rewardAmount
          }
          let data = await saveActivityGoods(
            this.form.id,
            this.form.activityName,// 促销名称
            this.form.bussinessId,
            this.form.ylGoodsId,
            this.form.goodsName,
            this.form.goodsUnit,
            this.form.rewardType,
            rewardAmount,
            rewardPercentage,
            this.form.startTime,
            this.form.endTime,
            this.form.goodsInSn,
            this.form.orderSource
          );
          this.$common.hideLoad();
          if (data !== undefined) {
            this.addShow = false;
            this.$common.success('保存成功');
            this.getList();
          }
        } else {
          console.log('error submit!!');
          return false;
        }
      });

    },
    // 远程搜索商品
    async remoteMethod(query) {
      if (!this.form.bussinessId) {
        this.$message.warning('请先选择商家，在搜索商品')
        return false
      }
      if (query.trim() !== '') {
        this.goodsloading = true;
        let data = await getActivityGoodsList(this.form.bussinessId, query);
        console.log(data);
        this.goodsloading = false;
        if (data !== undefined) {
          this.options = data.list;
        }
      } else {
        this.options = [];
        this.$message.warning('请输入名称搜索并选择相应的商品');
      }
    },
    // 远程搜索商业
    async remoteMethod1(query) {
      if (query !== '') {
        this.goodsloading1 = true;
        let data = await getBussinessInfoByBussinessName(1, 20, query);
        console.log(data);
        this.goodsloading1 = false;
        if (data !== undefined) {
          this.options1 = data.records;
        }
      } else {
        this.options1 = [];
      }
    },
    // 选择商品
    selectGoods(e) {
      console.log(e);
      this.form.goodsName = e.goodsName;
      this.form.ylGoodsId = e.ylGoodsId;
      this.form.goodsUnit = e.goodsSpecifications;
      this.form.goodsInSn = e.goodsInSn;
    },
    changeDate() {
      if (!this.form.startTime) {
        this.$common.warn('请先选择开始时间')
        this.form.endTime = ''
      }
    },
    // 返回
    goBack() {
      this.$router.push({
        name: 'ZtDataReportList'
      })
    }
  }
};
</script>
<style lang="scss" scoped>
@import './index.scss';
</style>
