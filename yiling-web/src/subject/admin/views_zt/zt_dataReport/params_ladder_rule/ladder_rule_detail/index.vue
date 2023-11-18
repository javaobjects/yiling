<!--
 * @Description:
 * @Author: xuxingwang
 * @Date: 2022-02-22 18:07:11
 * @LastEditTime: 2022-03-01 10:40:32
 * @LastEditors: xuxingwang
-->
<template>
  <div class="app-container">
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">商业名称</div>
              <el-select
              v-model="query.eid"
              clearable
              filterable
              remote
              :remote-method="remoteSearchEnterprise"
              @clear="clearEnterprise"
              @blur="blurEnterpriseInput"
              :loading="searchLoading"
              placeholder="请搜索并选择商业">
                <el-option
                  v-for="item in sellerEnameOptions"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="8">
              <div class="title">操作时间</div>
              <el-date-picker v-model="query.time" type="daterange" format="yyyy 年 MM 月 dd 日" value-format="yyyy-MM-dd" range-separator="至" start-placeholder="开始时间" end-placeholder="结束时间">
              </el-date-picker>
            </el-col>
          </el-row>
          <el-row class="box mar-t-16">
            <el-col :span="8">
              <div class="title">商品名称</div>
              <el-input v-model="query.goodsName" placeholder="请输入" @keyup.enter.native="handleSearch"></el-input>
            </el-col>
            <el-col :span="8">
              <div class="title">操作人</div>
              <el-input v-model="query.updateUser" placeholder="请输入" @keyup.enter.native="handleSearch"></el-input>
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
      <!-- 导出按钮 -->
      <div class="down-box clearfix">
        <div class="btn">
          <el-link class="mar-r-10" type="primary" :underline="false" :href="'NO_13' | template">
            下载导入模板
          </el-link>
          <ylButton type="primary" plain @click="goImport">导入阶梯规则</ylButton>
        </div>
      </div>
      <div class="mar-t-8 pad-b-100">
        <yl-table border :show-header="true" :list="dataList" :total="query.total" :page.sync="query.page" :limit.sync="query.limit" :loading="loading" @getList="getList">
          <el-table-column min-width="100" align="center" label="ID" prop="id"></el-table-column>
          <el-table-column min-width="100" align="center" label="商品名称" prop="goodsName"></el-table-column>
          <el-table-column min-width="100" align="center" label="商品规格" prop="goodsSpecification"></el-table-column>
          <el-table-column min-width="100" align="center" label="商品内码" prop="goodsInSn"></el-table-column>
          <el-table-column min-width="100" align="center" label="订单来源" prop="orderSource">
            <template slot-scope="{ row }">
              <div>{{ row.orderSource | dictLabel(orderSource) }}</div>
            </template>
          </el-table-column>
          <el-table-column min-width="100" align="center" label="以岭商品ID" prop="ylGoodsId"></el-table-column>
          <el-table-column min-width="100" align="center" label="商业ID" prop="eid"></el-table-column>
          <el-table-column min-width="100" align="center" label="商业名称" prop="ename"></el-table-column>
          <el-table-column min-width="100" align="center" label="起步数量" prop="thresholdCount"></el-table-column>
          <el-table-column min-width="100" align="center" label="返利金额">
            <template slot-scope="{ row }">
              <div>
                <span>
                  <span v-if="row.rewardType == 1">{{ row.rewardAmount }}</span>
                  <span v-if="row.rewardType == 2">{{ row.rewardPercentage ? (row.rewardPercentage + "%") : "- -" }}</span>
                </span>
              </div>
            </template>
          </el-table-column>
          <el-table-column min-width="100" align="center" label="开始时间" prop="startTime">
            <template slot-scope="{ row }">
              <div>
                {{ row.startTime | formatDate }}
              </div>
            </template>
          </el-table-column>
          <el-table-column min-width="100" align="center" label="结束时间" prop="endTime">
            <template slot-scope="{ row }">
              <div>
                {{ row.endTime | formatDate }}
              </div>
            </template>
          </el-table-column>
          <el-table-column min-width="100" align="center" label="操作时间" prop="updateTime">
            <template slot-scope="{ row }">
              <div>
                {{ row.updateTime | formatDate }}
              </div>
            </template>
          </el-table-column>
          <el-table-column min-width="100" align="center" label="操作人" prop="updateUserName"></el-table-column>
          <el-table-column align="center" label="操作">
            <template slot-scope="{ row }">
              <yl-button type="text" @click="edit(row)">修改</yl-button>
              <yl-button type="text" @click="del(row)">删除</yl-button>
            </template>
          </el-table-column>
        </yl-table>
      </div>
      <div class="bottom-view flex-row-center">
        <yl-button type="primary" plain @click="goBack">返回</yl-button>
      </div>
      <!-- 添加商品 -->
      <yl-dialog :visible.sync="addShow" width="1200px" :title="'阶梯规则修改--' + dialogName" @confirm="confirm">
        <div class="dialog-content">
          <el-form class="pad-t-8" ref="form" :rules="formRules" :model="form" label-width="110px">
            <el-row>
              <el-col :span="12" :offset="0">
                <el-form-item label="商家名称：" prop="bussinessName">
                  <el-input v-model="form.bussinessName" disabled></el-input>
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
                <el-form-item label="商品名称：" prop="goodsName">
                  <el-select v-model="form.goodsName" filterable remote reserve-keyword placeholder="请输入关键词" :remote-method="remoteMethod" :loading="goodsloading" @change="selectGoods" disabled>
                    <el-option v-for="item in options" :key="item.id" :label="item.goodsName" :value="item">
                      <span>{{ item.goodsName }}---{{ item.goodsSpecifications }}---{{ item.goodsInSn }}</span>
                    </el-option>
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
                <el-form-item label="商品内码：" prop="goodsInSn">
                  <el-input v-model="form.goodsInSn" disabled></el-input>
                </el-form-item>
              </el-col>
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
            </el-row>
            <el-row>
              <el-col :span="12" :offset="0">
                <el-form-item label="起步数量：" prop="thresholdCount">
                  <el-input v-model="form.thresholdCount" placeholder="请输入内容"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" :offset="0">
                <el-form-item label="奖励金额：" prop="rewardAmount">
                  <el-input placeholder="请输入内容" v-model="form.rewardAmount" class="input-with-select">
                    <!-- 奖励类型：1-金额 2-百分比	 阶梯只能是元-->
                    <el-select v-model="form.rewardType" slot="append" placeholder="请选择">
                      <el-option label="元/盒" value="1"></el-option>
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
import { formatDate } from '@/common/utils/index';
import {
  getQueryParamSubGoodsPageList,
  getActivityGoodsList,
  updateLadderGoods,
  deleteParSubGoods,
  queryEnterpriseList
} from '@/subject/admin/api/zt_api/dataReport';
import { reportOrderSource } from '@/subject/admin/busi/zt/dataReport'

export default {
  name: 'ZtParamsLadderRuleDetail',
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
    const validateEnd = (rule, value, callback) => {
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
    return {
      dialogName: '',
      query: {
        total: 0,
        page: 1,
        limit: 10,
        goodsName: '',
        updateUser: '',
        time: []
      },
      loading: false,
      dataList: [],
      addShow: false,
      form: {
        id: '',
        bussinessId: '',
        bussinessName: '',
        goodsName: '',
        goodsUnit: '',
        ylGoodsId: '',
        thresholdCount: '',
        rewardAmount: '',
        rewardPercentage: '',
        rewardType: '1',
        startTime: '',
        endTime: '',
        orderSource: ''
      },
      formRules: {
        bussinessId: [
          { required: true, message: '请输入', trigger: 'trigger' }
        ],
        goodsName: [
          { required: true, message: '请输入', trigger: 'trigger' }
        ],
        goodsUnit: [
          { required: true, message: '请输入', trigger: 'trigger' }
        ],
        ylGoodsId: [
          { required: true, message: '请输入', trigger: 'trigger' }
        ],
        thresholdCount: [
          { required: true, message: '请输入', trigger: 'trigger' }
        ],
        rewardAmount: [
          { required: true, message: '请输入', trigger: 'trigger' }
        ],
        startTime: [
          { required: true, validator: validateStart, trigger: 'trigger' }
        ],
        endTime: [
          { required: true, validator: validateEnd, trigger: 'trigger' }
        ],
        orderSource: [
          { required: true, message: '请选择订单来源', trigger: 'blur' }
        ]
      },
      goodsloading: false,
      options: [],
      pickerOptions1: {
        disabledDate: (time) => {
          if (this.form.startTime) {
            return time.getTime() < new Date(this.form.startTime).getTime()
          }
        }
      },
      searchLoading: false,
      sellerEnameOptions: []
    };
  },
  mounted() {
    this.dialogName = this.$route.params.name
    this.getList();
  },
  methods: {
    async getList() {
      let query = this.query;
      this.loading = true;
      let data = await getQueryParamSubGoodsPageList(
        query.page,
        query.limit,
        this.$route.params.id,
        '',// 活动名称 阶梯中用不到
        query.eid, // 商业名称
        query.updateUser,
        query.goodsName,
        query.time && query.time.length > 0 ? query.time[0] : '',
        query.time && query.time.length > 1 ? query.time[1] : ''
      );
      this.loading = false;
      if (data != undefined) {
        this.dataList = data.records;
        this.query.total = data.total;
      }
    },
    handleSearch() {
      this.query.page = 1;
      this.getList();
    },
    handleReset() {
      this.query = {
        page: 1,
        limit: 10,
        time: []
      };
    },
    // 编辑
    edit(row) {
      this.form.id = row.id
      this.form.bussinessName = row.ename
      this.form.bussinessId = row.eid
      this.form.goodsName = row.goodsName
      this.form.goodsUnit = row.goodsSpecification
      this.form.goodsInSn = row.goodsInSn
      this.form.ylGoodsId = row.ylGoodsId
      this.form.thresholdCount = row.thresholdCount
      this.form.rewardType = String(row.rewardType)
      this.form.orderSource = row.orderSource

      if (row.rewardType == 1) {
        this.form.rewardAmount = row.rewardAmount
      } else {
        this.form.rewardAmount = row.rewardPercentage
      }
      this.form.startTime = formatDate(row.startTime, 'yyyy-MM-dd');
      this.form.endTime = formatDate(row.endTime, 'yyyy-MM-dd');
      this.addShow = true;
    },
    // 修改
    confirm() {
      this.$refs['form'].validate(async (valid) => {
        if (valid) {
          this.$common.showLoad();
          let rewardAmount = ''
          let rewardPercentage = ''
          if (this.form.rewardType == 1) {
            rewardAmount = this.form.rewardAmount
            rewardPercentage = ''
          } else {
            rewardAmount = ''
            rewardPercentage = this.form.rewardAmount
          }
          let data = await updateLadderGoods(
            this.form.id,
            this.form.thresholdCount,
            rewardAmount,
            rewardPercentage,
            this.form.rewardType,
            this.form.startTime,
            this.form.endTime,
            this.form.orderSource
          );
          this.$common.hideLoad();
          if (data !== undefined) {
            this.$common.success('编辑成功');
            this.addShow = false;
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
      if (query !== '' && this.form.bussinessId) {
        this.goodsloading = true;
        let data = await getActivityGoodsList(this.form.bussinessId, query);
        console.log(data);
        this.goodsloading = false;
        if (data !== undefined) {
          this.options = data.list;
        }
      } else {
        this.options = [];
      }
    },
    // 选择商品
    selectGoods(e) {
      console.log(e);
      this.form.goodsName = e.goodsName;
      this.form.ylGoodsId = e.ylGoodsId;
      this.form.goodsUnit = e.goodsSpecifications;
    },
    // 删除
    del(row) {
      this.$common.confirm('确定要删除当前商品吗?', async confirm => {
        if (confirm) {
          let data = await deleteParSubGoods(row.id)
          if (data !== undefined) {
            this.$common.success('操作成功');
            this.getList();
          }
        }
      })
    },
    changeDate() {
      if (!this.form.startTime) {
        this.$common.warn('请先选择开始时间')
        this.form.endTime = ''
      }
    },
    async remoteSearchEnterprise(query) {
      if (query.trim() != '') {
        this.searchLoading = true
        let data = await queryEnterpriseList( 1, 10, query.trim() )
        this.searchLoading = false
        if (data) {
          this.sellerEnameOptions = data.records
        }
      } else {
        this.sellerEnameOptions = []
      }
    },
    clearEnterprise() {
      this.sellerEnameOptions = []
    },
    blurEnterpriseInput(e) {
      if (e.target.value == '') {
        this.sellerEnameOptions = []
      }
    },
    //  导入
    goImport() {
      let id = this.$route.params.id
      let action = ''
      if (id == 1) {
        action = '/dataCenter/api/v1/report/param/importLadderFirst'
      } else if (id == 2) {
        action = '/dataCenter/api/v1/report/param/importLadderSecond'
      } else if (id == 3) {
        action = '/dataCenter/api/v1/report/param/importLadderThird'
      }
      if (!action) {
        this.$common.warn('暂无该阶梯规则导入')
        return
      }
      this.$router.push({
        path: '/importFile/importFile_index',
        query: {
          action: action
        }
      })
    },
    // 返回
    goBack() {
      this.$router.push({
        name: 'ZtParamsLadderRule',
        params: {
          id: this.$route.params.fatherId,
          type: this.$route.params.fatherType
        }
      })
    }
  }
};
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
