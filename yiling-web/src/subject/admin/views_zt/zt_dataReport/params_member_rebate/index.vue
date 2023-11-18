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
            <el-col :span="8">
              <div class="title">购买金额</div>
              <el-input v-model="query.thresholdAmount" placeholder="请输入购买金额" @keyup.enter.native="handleSearch"></el-input>
            </el-col>
          </el-row>
          <el-row class="box mar-t-16">
            <el-col :span="8">
              <div class="title">会员来源</div>
              <el-select placeholder="请选择会员来源" v-model="query.memberSource">
                <el-option label="全部" :value="0"></el-option>
                <el-option
                  v-for="item in reportMemberSource"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="8">
              <div class="title">操作人</div>
              <el-input v-model="query.updateUser" placeholder="请输入操作人" @keyup.enter.native="handleSearch"></el-input>
            </el-col>
            <el-col :span="8">
              <div class="title">会员名称</div>
              <el-select placeholder="请选择会员名称" v-model="query.memberId">
                <el-option label="全部" value=""></el-option>
                <el-option
                  v-for="item in memberNameOptions"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id">
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
      <div class="down-box">
        <div>
        </div>
        <div class="btn">
          <el-link class="mar-r-10" type="primary" :underline="false" :href="'NO_14' | template">
            下载导入模板
          </el-link>
          <ylButton type="primary" plain @click="goImport">导入会员</ylButton>
          <yl-button type="primary" plain @click="add">添加</yl-button>
        </div>
      </div>
      <div class="mar-t-8 pad-b-100">
        <yl-table border :show-header="true" :list="dataList" :total="query.total" :page.sync="query.page" :limit.sync="query.limit" :loading="loading" @getList="getList">
          <el-table-column min-width="100" align="center" label="商业ID" prop="eid"></el-table-column>
          <el-table-column min-width="100" align="center" label="商业名称" prop="ename"></el-table-column>
          <el-table-column min-width="100" align="center" label="会员售价" prop="thresholdAmount"></el-table-column>
          <el-table-column min-width="100" align="center" label="返利金额" prop="rewardAmount">
            <template slot-scope="{ row }">
              <div>
                <span v-if="row.rewardType == 1">{{ row.rewardAmount }}</span>
                <span v-if="row.rewardType == 2">{{ row.rewardPercentage ? (row.rewardPercentage + "%") : "- -" }}</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column min-width="100" align="center" label="会员来源">
            <template slot-scope="{ row }">
              <div>
                {{ row.memberSource | dictLabel(reportMemberSource) }}
              </div>
            </template>
          </el-table-column>
          <el-table-column min-width="100" align="center" label="会员名称" prop="memberName"></el-table-column>
          <el-table-column min-width="100" align="center" label="开始时间" prop="startTime">
            <template slot-scope="{ row }">
              <div>{{ row.startTime | formatDate }}</div>
            </template>
          </el-table-column>
          <el-table-column min-width="100" align="center" label="结束时间" prop="endTime">
            <template slot-scope="{ row }">
              <div>{{ row.endTime | formatDate }}</div>
            </template>
          </el-table-column>
          <el-table-column min-width="100" align="center" label="操作时间" prop="updateTime">
            <template slot-scope="{ row }">
              <div>{{ row.updateTime | formatDate }}</div>
            </template>
          </el-table-column>
          <el-table-column min-width="100" align="center" label="操作人" prop="updateUserName"></el-table-column>
          <el-table-column align="center" label="操作">
            <template slot-scope="{ row }">
              <yl-button type="text" @click="edit(row)">修改</yl-button>
            </template>
          </el-table-column>
        </yl-table>
      </div>
      <div class="bottom-view flex-row-center">
        <yl-button type="primary" plain @click="goBack">返回</yl-button>
      </div>
      <!-- 添加商品 -->
      <yl-dialog :visible.sync="addShow" width="1152px" title="会员返利" @confirm="confirm">
        <div class="dialog-content">
          <el-form class="pad-t-8" ref="form" :rules="formRules" :model="form" label-width="110px">
            <el-row>
              <el-col :span="12" :offset="0">
                <el-form-item label="商业名称：" prop="bussinessName">
                  <el-select v-model="form.bussinessName" filterable remote reserve-keyword placeholder="请输入关键词" :remote-method="remoteMethod1" :loading="goodsloading1" @change="selectGoods" :disabled="flag">
                    <el-option v-for="item in options1" :key="item.id" :label="item.name" :value="item.id"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12" :offset="0">
                <el-form-item label="商业ID：" prop="bussinessId">
                  <el-input v-model="form.bussinessId" disabled></el-input>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="12" :offset="0">
                <el-form-item label="会员售价：" prop="thresholdAmount">
                  <el-input v-model="form.thresholdAmount" placeholder="请输入内容"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" :offset="0">
                <el-form-item label="返利金额：" prop="rewardValue">
                  <el-input placeholder="请输入内容" v-model="form.rewardValue" class="input-with-select">
                    <el-select v-model="form.rewardType" slot="append" placeholder="请选择">
                      <el-option label="元" value="1"></el-option>
                      <el-option label="%" value="2"></el-option>
                    </el-select>
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12" :offset="0">
                <el-form-item label="会员来源：" prop="memberSource">
                  <el-select v-model="form.memberSource" placeholder="请选择会员来源">
                    <el-option v-for="item in reportMemberSource" :key="item.value" :label="item.label"
                      :value="item.value"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12" :offset="0">
                <el-form-item label="会员名称：" prop="memberId">
                  <el-select v-model="form.memberId" placeholder="请选择会员来源">
                    <el-option v-for="item in memberNameOptions" :key="item.id" :label="item.name"
                      :value="item.id"
                    ></el-option>
                  </el-select>
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
  getParamsTypeList,
  getBussinessInfoByBussinessName,
  addMemberType,
  editMemberType,
  queryEnterpriseList,
  queryMemberList
} from '@/subject/admin/api/zt_api/dataReport';
import { reportMemberSource } from '@/subject/admin/busi/zt/dataReport'

export default {
  name: 'ZtParamsMemberRebate',
  components: {},
  computed: {
    reportMemberSource() {
      return reportMemberSource()
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
    const validatePrice = (rule, value, callback) => {
      let reg = /^(([1-9]{1}\d*)|(0{1}))(\.\d{1,2})?$/
      if (!value) {
        callback(new Error('价格不能为空'))
      } else if (!reg.test(value)) {
        callback(new Error('请输入正确的格式，最多保留2位小数'))
      } else if (value.length > 10) {
        callback(new Error('最多可输入10个字符'))
      } else {
        callback();
      }
    }
    const validatePrice1 = (rule, value, callback) => {
      let reg = /^(([1-9]{1}\d*)|(0{1}))(\.\d{1,2})?$/
      if (!value) {
        callback(new Error('价格不能为空'))
      } else if (!reg.test(value)) {
        callback(new Error('请输入正确的格式，最多保留2位小数'))
      } else if (value.length > 10) {
        callback(new Error('最多可输入10个字符'))
      } else if (!this.form.rewardType) {
        callback(new Error('返利金额类型不能为空'));
      } else {
        callback();
      }
    }
    return {
      query: {
        total: 0,
        page: 1,
        limit: 10,
        eid: '',
        updateUser: '',
        thresholdAmount: '',
        memberSource: 0,
        time: [],
        // 会员名称
        memberId: ''
      },
      loading: false,
      dataList: [
        {}
      ],
      addShow: false,
      form: {
        id: '',
        bussinessName: '',
        bussinessId: '',
        thresholdAmount: '',
        rewardValue: '',
        rewardType: '1',
        memberSource: '',
        startTime: '',
        endTime: '',
        memberId: ''
      },
      formRules: {
        bussinessName: [
          { required: true, message: '请输入商业名称搜索选择', trigger: 'blur' }
        ],
        thresholdAmount: [
          { required: true, validator: validatePrice, trigger: 'blur' }
        ],
        rewardValue: [
          { required: true, validator: validatePrice1, trigger: 'blur' }
        ],
        rewardType: [
          { required: true, message: '请输入', trigger: 'blur' }
        ],
        memberSource: [
          { required: true, message: '请选择会员来源', trigger: 'change' }
        ],
        startTime: [
          { required: true, validator: validateStart, trigger: 'blur' }
        ],
        endTime: [
          { required: true, validator: validateEnd, trigger: 'blur' }
        ],
        memberId: [
          { required: true, message: '请选择会员名称', trigger: 'change' }
        ]
      },
      goodsloading: false,
      goodsloading1: false,
      options: [],
      options1: [],
      pickerOptions1: {
        disabledDate: (time) => {
          if (this.form.startTime) {
            return time.getTime() < new Date(this.form.startTime).getTime()
          }
        }
      },
      flag: false,
      searchLoading: false,
      sellerEnameOptions: [],
      // 会员名称
      memberNameOptions: []
    };
  },
  mounted() {
    this.getList();
    this.getMemberList()
  },
  methods: {
    async getList() {
      let query = this.query;
      this.loading = true;
      let data = await getParamsTypeList(
        query.page,
        query.limit,
        4,
        this.$route.params.id,
        query.eid,
        query.thresholdAmount,
        query.memberSource,
        query.updateUser,
        query.time && query.time.length > 0 ? query.time[0] : null,
        query.time && query.time.length > 1 ? query.time[1] : null,
        query.memberId
      );
      this.loading = false;
      if (data != undefined) {
        this.dataList = data.records;
        this.query.total = data.total;
      }
    },
    // 获取会员数据
    async getMemberList() {
      let data = await queryMemberList()
      if (data && data.list) {
        this.memberNameOptions = data.list
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
        eid: '',
        memberSource: 0,
        updateUser: '',
        thresholdAmount: '',
        time: [],
        memberId: ''
      };
    },
    // 添加
    add() {
      this.form.id = ''
      this.form.bussinessName = ''
      this.form.bussinessId = ''
      this.form.thresholdAmount = ''
      this.form.rewardValue = ''
      this.form.rewardType = ''
      this.form.memberSource = ''
      this.form.startTime = ''
      this.form.endTime = ''
      this.flag = false
      this.options1 = []
      this.form.memberId = ''
      this.addShow = true
    },
    // 编辑
    edit(row) {
      console.log(row);
      this.flag = true
      this.form.id = row.id
      this.form.bussinessName = row.ename
      this.form.bussinessId = row.eid
      this.form.thresholdAmount = row.thresholdAmount
      this.form.rewardValue = row.rewardType == 1 ? row.rewardAmount : row.rewardPercentage
      this.form.rewardType = String(row.rewardType)
      this.form.memberSource = row.memberSource || ''
      this.form.startTime = formatDate(row.startTime, 'yyyy-MM-dd')
      this.form.endTime = formatDate(row.endTime, 'yyyy-MM-dd')
      this.form.memberId = row.memberId
      this.addShow = true;
    },
    confirm() {
      this.$refs['form'].validate(async (valid) => {
        if (valid) {
          this.$common.showLoad();
          let data = '';
          if (!this.flag) {
            data = await addMemberType(
              this.$route.params.id,
              // 参数类型：1-商品类型 4-会员返利
              4,
              this.form.bussinessId,
              this.form.thresholdAmount,
              this.form.rewardValue,
              this.form.rewardType,
              this.form.memberSource,
              this.form.startTime,
              this.form.endTime,
              this.form.memberId
            );
          } else {
            data = await editMemberType(
              this.form.id,
              // 参数类型：1-商品类型 4-会员返利
              4,
              this.form.bussinessId,
              this.form.thresholdAmount,
              this.form.rewardValue,
              this.form.rewardType,
              this.form.memberSource,
              this.form.startTime,
              this.form.endTime,
              this.form.memberId
            );
          }
          this.$common.hideLoad();
          if (data !== undefined) {
            if (this.flag) {
              this.$common.success('编辑成功');
            } else {
              this.$common.success('添加成功');
            }
            this.addShow = false;
            this.getList();
          }
        } else {
          console.log('error submit!!');
          return false;
        }
      });

    },
    // 远程搜索商业名称详情
    async remoteMethod(query) {
      if (query !== '') {
        this.goodsloading = true;
        let data = await getBussinessInfoByBussinessName(1, 20, query);
        console.log(data);
        this.goodsloading = false;
        if (data !== undefined) {
          this.options = data.records;
        }
      } else {
        this.options = [];
      }
    },
    // form表单远程搜索商业名称详情
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
      this.form.bussinessId = e;
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
      this.$router.push({
        path: '/importFile/importFile_index',
        query: {
          action: '/dataCenter/api/v1/report/param/importMemberPar'
        }
      })
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
