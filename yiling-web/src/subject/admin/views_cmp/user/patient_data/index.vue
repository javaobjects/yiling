<template>
  <div class="app-container">
    <!-- 内容区域 -->
    <div class="app-container-content">
      <!-- 中部 搜索条件 -->
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">患者姓名</div>
              <el-input v-model="query.patientName" @keyup.enter.native="searchEnter" placeholder="请输入患者姓名" />
            </el-col>
            <el-col :span="6">
              <div class="title">性别</div>
              <el-select class="select-width" v-model="query.gender" placeholder="请选择">
                <el-option
                  v-for="item in sixData"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <div class="title">创建时间段</div>
              <el-date-picker
                v-model="query.establishTime"
                type="daterange"
                format="yyyy/MM/dd"
                value-format="yyyy-MM-dd"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期">
              </el-date-picker>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">年龄段</div>
              <input-range v-model="inputData" :range-separator="'-'" @input="inputChange"></input-range>
            </el-col>
            <el-col :span="14">
              <div class="title">地区</div>
              <yl-choose-address
                width="230px"
                :province.sync="query.provinceCode"
                :city.sync="query.cityCode"
                :area.sync="query.regionCode"
                is-all/>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn :total="query.total" @search="handleSearch" @reset="handleReset"/>
            </el-col>
          </el-row>
        </div>
      </div>
      <!-- 下部列表 -->
      <div class="search-bar mar-t-8">
        <yl-table 
          border 
          :show-header="true" 
          :list="dataList" 
          :total="query.total" 
          :page.sync="query.page" 
          :limit.sync="query.limit" 
          :loading="loading" 
          @getList="getList">
          <el-table-column align="center" min-width="100" label="就诊人" prop="patientName">
          </el-table-column>
          <el-table-column align="center" min-width="70" label="年龄">
            <template slot-scope="{ row }">
              <div>
                {{ row.patientAge ? row.patientAge : '' }}
              </div>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="120" label="身份证号" prop="idCard">
          </el-table-column>
          <el-table-column align="center" min-width="100" label="手机号" prop="mobile">
          </el-table-column>
          <el-table-column align="center" min-width="70" label="性别" prop="gender">
            <template slot-scope="{ row }">
              {{ row.gender == 1 ? '男' : ( row.gender == 2 ? '女' : '未知') }}
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="200" label="地址">
            <template slot-scope="{ row }">
              {{ row.province }} {{ row.city }} {{ row.region }} {{ row.address }}
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="80" label="绑定的用户数" prop="bindUserCount">
          </el-table-column>
          <el-table-column align="center" min-width="80" label="绑定的医生数">
            <template slot-scope="{ row }">
              <span class="span-count" @click="doctorCountClick(row)">{{ row.bindDoctorCount }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="120" label="病史标签" prop="diseaseTags">
          </el-table-column>
          <el-table-column align="center" min-width="120" label="药品标签" prop="medicationTags">
          </el-table-column>
          <el-table-column align="center" min-width="100" label="创建时间">
            <template slot-scope="{ row }">
              {{ row.createTime | formatDate }}
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="100" label="最新修改时间">
            <template slot-scope="{ row }">
              {{ row.updateTime | formatDate }}
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
    <yl-dialog
      width="900px"
      title="绑定的医生数"
      :show-footer="true"
      :show-cancle="false"
      :visible.sync="showDialog"
      @confirm="confirm">
      <div class="pop-up">
        <yl-table 
          border 
          :show-header="true" 
          :list="doctorDataList">
          <el-table-column align="center" min-width="70" label="医生ID" prop="doctorId">
          </el-table-column>
          <el-table-column align="center" min-width="100" label="医生姓名" prop="doctorName">
          </el-table-column>
          <el-table-column align="center" min-width="120" label="第一执业医院" prop="hospitalName">
          </el-table-column>
          <el-table-column align="center" min-width="120" label="绑定来源" prop="sourceType">
          </el-table-column>
          <el-table-column align="center" min-width="110" label="绑定业务线" prop="businessType">
          </el-table-column>
          <el-table-column align="center" min-width="130" label="绑定时间">
            <template slot-scope="{ row }">
              {{ row.createTime | formatDate }}
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </yl-dialog>
  </div>
</template>
<script>
import { ylChooseAddress } from '@/subject/admin/components'
import { queryPatientPage, queryPatientBindDoctorByPatientId } from '@/subject/admin/api/cmp_api/user'
import inputRange from '@/common/components/InputRange'
export default {
  name: 'PatientData',
  components: {
    ylChooseAddress, 
    inputRange
  },
  computed: {
  },
  data() {
    return {
      query: {
        patientName: '',
        gender: null,
        total: 0,
        page: 1,
        limit: 10,
        establishTime: [],
        provinceCode: '',
        cityCode: '',
        regionCode: ''
      },
      inputData: [],
      loading: false,
      sixData: [
        {
          value: null,
          label: '全部'
        },
        {
          value: '1',
          label: '男'
        },
        {
          value: '2',
          label: '女'
        },
        {
          value: '0',
          label: '未知'
        }
      ],
      dataList: [],
      showDialog: false,
      doctorDataList: []
    }
  },
  activated() {
    this.getList();
  },
  methods: {
    // Enter
    searchEnter(e) {
      const keyCode = window.event ? e.keyCode : e.which;
      if (keyCode === 13) {
        this.getList()
      }
    },
    // 搜索
    handleSearch() {
      this.query.page = 1;
      this.getList();
    },
    // 重置
    handleReset() {
      this.query = {
        patientName: '',
        gender: null,
        total: 0,
        page: 1,
        limit: 10,
        establishTime: [],
        provinceCode: '',
        cityCode: '',
        regionCode: ''
      }
      this.inputData = [];
    },
    async getList() {
      this.loading = true;
      let query = this.query;
      let data = await queryPatientPage(
        query.page,
        query.limit,
        query.patientName,
        query.gender,
        this.inputData && this.inputData.length > 0 ? this.inputData[0] : '',
        this.inputData && this.inputData.length > 1 ? this.inputData[1] : '',
        query.provinceCode,
        query.cityCode,
        query.regionCode,
        query.establishTime && query.establishTime.length > 0 ? query.establishTime[0] : '',
        query.establishTime && query.establishTime.length > 1 ? query.establishTime[1] : ''
      )
      if (data) {
        this.dataList = data.records;
        this.query.total = data.total;
      }
      this.loading = false;
    },
    inputChange(val) {
      this.inputData = val;
    },
    //点击绑定的医生数
    async doctorCountClick(row) {
      this.$common.showLoad();
      let data = await queryPatientBindDoctorByPatientId(
        row.id
      )
      this.$common.hideLoad();
      if (data !== undefined) {
        this.doctorDataList = data;
      }
      this.showDialog = true
    },
    confirm() {
      this.showDialog = false
    }
  }
}
</script>
<style lang="scss" scoped>
@import './index.scss';
</style>