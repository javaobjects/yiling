<template>
  <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">患者姓名</div>
              <el-input v-model="query.patientName" placeholder="请输入患者姓名" @keyup.enter.native="searchEnter"/>
            </el-col>
            <el-col :span="6">
              <div class="title">医生ID</div>
              <el-input v-model="query.doctorId" placeholder="请输入医生ID" @keyup.enter.native="searchEnter"/>
            </el-col>
            <el-col :span="6">
              <div class="title">医生姓名</div>
              <el-input v-model="query.doctorName" placeholder="请输入医生姓名" @keyup.enter.native="searchEnter"/>
            </el-col>
            <el-col :span="6">
              <div class="title">凭证状态</div>
              <el-select v-model="query.certificateState" placeholder="请选择">
                <el-option label="全部" :value="0"></el-option>
                <el-option
                  v-for="item in certificateStateData"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-col>
          </el-row>
        </div>
        <div class="search-btn-box mar-t-16">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn :total="query.total" @search="handleSearch" @reset="handleReset" />
            </el-col>
          </el-row>
        </div>
      </div>
      <div class="down-box">
        <div class="right">
          <yl-button type="primary" @click="downLoadTemp">导出数据</yl-button>
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
          <el-table-column align="center" min-width="150" label="邀请医生信息">
            <template slot-scope="{ row }">
              <div>
                <p>{{ row.doctorName }}[ID:{{ row.doctorId }}]</p>
                <p>{{ row.hospitalName }}</p>
                <p>{{ row.province }}</p>
              </div>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="140" label="患者姓名" prop="patientName">
          </el-table-column>
          <el-table-column align="center" min-width="100" label="手机号" prop="mobile">
          </el-table-column>
          <el-table-column align="center" min-width="100" label="性别" prop="gender">
          </el-table-column>
          <el-table-column align="center" min-width="100" label="年龄">
            <template slot-scope="{ row }">
              <span>{{ row.age ? row.age : '- -' }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="100" label="病史标签" prop="diseaseTags">
          </el-table-column>
          <el-table-column align="center" min-width="100" label="药品标签" prop="medicationTags">
          </el-table-column>
          <el-table-column align="center" min-width="100" label="是否新用户">
            <template slot-scope="{ row }">
              {{ row.userState == 1 ? '是' : '否' }}
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="100" label="绑定时间">
            <template slot-scope="{ row }">
              {{ row.createTime | formatDate }}
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="90" label="提审时间">
            <template slot-scope="{ row }">
              {{ row.arraignmentTime | formatDate }}
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="90" label="凭证状态" prop="">
            <template slot-scope="{ row }">
              {{ row.certificateState | dictLabel(certificateStateData) }}
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="90" label="审核时间">
            <template slot-scope="{ row }">
              {{ row.auditTime | formatDate }}
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="90" label="驳回原因" prop="rejectReason">
          </el-table-column>
          <el-table-column fixed="right" align="center" label="操作" min-width="100">
            <template slot-scope="{ row }">
              <yl-button type="text" @click="seeClick(row)" v-if="row.certificateState == 2">审核</yl-button>
              <yl-button type="text" @click="seeClick(row)" v-if="row.certificateState == 3 || row.certificateState == 4">查看凭证</yl-button>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
    <div class="bottom-view flex-row-center">
      <yl-button type="primary" plain @click="$router.go(-1)">返回</yl-button>
    </div>
  
  </div>
</template>
<script>
import { queryActivityDocPatientPage } from '@/subject/admin/api/cmp_api/education'
import { createDownLoad } from '@/subject/admin/api/common'
import { hmcActPatientCerStateType } from '@/subject/admin/busi/cmp/activity'
export default {
  name: 'ViewPatient',
  computed: {
    certificateStateData() {
      return hmcActPatientCerStateType()
    }
  },
  data() {
    return {
      query: {
        id: '',
        patientName: '',
        doctorId: '',
        doctorName: '',
        certificateState: 0,
        total: 0,
        page: 1,
        limit: 10
      },
      dataList: [],
      loading: false
    }
  },
  mounted() {
    let query = this.$route.params;
    if (query.id) {
      this.query.id = parseInt(query.id)
      this.getList();
    }
  },
  methods: {
    // Enter
    searchEnter(e) {
      const keyCode = window.event ? e.keyCode : e.which;
      if (keyCode === 13) {
        this.getList()
      }
    },
    // 获取数据列表
    async getList() {
      let query = this.query;
      this.loading = true;
      let data = await queryActivityDocPatientPage(
        query.id,
        query.patientName,
        query.doctorId,
        query.doctorName,
        query.certificateState,
        query.page,
        query.limit
      )
      if (data) {
        this.dataList = data.records;
        this.query.total = data.total;
      }
      this.loading = false;
    },
    //点击审核/查看凭证
    seeClick(row, type) {
      this.$router.push({
        name: 'ViewPatientsEstablish',
        params: {
          id: row.id
        }
      })
    },
    //导出
    async downLoadTemp() {
      const query = this.query
      this.$common.showLoad()
      const data = await createDownLoad({
        className: 'hmcActivityPatientExportService',
        fileName: '患者数据导出',
        groupName: '患者数据导出',
        menuName: '医带患-患者数据导出',
        searchConditionList: [
          {
            desc: '活动ID',
            name: 'activityId',
            value: query.id || ''
          },
          {
            desc: '患者姓名',
            name: 'patientName',
            value: query.patientName || ''
          },
          {
            desc: '医生ID',
            name: 'doctorId',
            value: query.doctorId || ''
          },
          {
            desc: '医生姓名',
            name: 'doctorName',
            value: query.doctorName || ''
          },
          {
            desc: '凭证状态',
            name: 'certificateState',
            value: query.certificateState || ''
          }
        ]
      })
      this.$common.hideLoad()
      if (data !== undefined) {
        this.$common.n_success('创建下载任务成功，请在下载中心查看')
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
        id: this.query.id,
        patientName: '',
        doctorId: '',
        doctorName: '',
        certificateState: 0,
        total: 0,
        page: 1,
        limit: 10
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
