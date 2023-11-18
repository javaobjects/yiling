<template>
  <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">医生ID</div>
              <el-input v-model="query.doctorId" placeholder="请输入医生ID" @keyup.enter.native="searchEnter" @input="e => (query.doctorId = checkInput(e))"/>
            </el-col>
            <el-col :span="8">
              <div class="title">医生姓名</div>
              <el-input v-model="query.name" placeholder="请输入医生姓名" @keyup.enter.native="searchEnter"/>
            </el-col>
            <el-col :span="8">
              <div class="title">第一执业机构</div>
              <el-autocomplete
                style="width: 260px"
                v-model="query.hospitalName" 
                :fetch-suggestions="querySearchAsync" 
                placeholder="请输入执业机构名称" 
                @select="handleSelect"
                clearable>
              </el-autocomplete>
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
        <div class="btn">
          <yl-button type="primary" @click="addClick">添加医生</yl-button>
        </div>
        <div class="right">
          <yl-button type="primary" @click="importClick">批量导入医生数据</yl-button>
          <yl-button type="primary" @click="downLoadTemp">导出医生数据</yl-button>
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
          <el-table-column align="center" min-width="80" label="医生ID" prop="doctorId">
          </el-table-column>
          <el-table-column align="center" min-width="110" label="医生姓名" prop="name">
          </el-table-column>
          <el-table-column align="center" min-width="100" label="第一执业机构" prop="hospitalName">
          </el-table-column>
          <el-table-column align="center" min-width="100" label="所属省" prop="provinceName">
          </el-table-column>
          <el-table-column align="center" min-width="150" label="邀请患者数" prop="caseCount">
            <template slot-scope="{ row }">
              <div>
                <p>报道总人数：{{ row.reportCount }}</p>
                <p>新用户报道人数：{{ row.newReportCount }}</p>
                <p>处方审核通过人数：{{ row.prescriptionCount }}</p>
              </div>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="100" label="活动码">
            <template slot-scope="{ row }">
              <img class="img" @click="seeClick(row)" :src="row.qrcodeUrl" alt="">
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="120" label="创建时间">
            <template slot-scope="{ row }">
              <div>{{ row.createTime | formatDate }}</div>
            </template>
          </el-table-column>
          <el-table-column fixed="right" align="center" label="操作" min-width="130">
            <template slot-scope="{ row }">
              <yl-button type="text" @click="activityClick(row)"> {{ row.doctorStatus == 1 ? '取消活动资格' : '恢复活动资格' }}</yl-button>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
    <div class="bottom-view flex-row-center">
      <yl-button type="primary" plain @click="$router.go(-1)">返回</yl-button>
    </div>
    <!-- 添加医生弹窗 -->
    <activity-doctor
      :doctor-data="doctorData"
      :show.sync="showDialog"
      @addDoctor="addDoctor"
      :activity-id="query.id"
      v-if="showDialog"/>
    <!-- 图片放大弹窗 -->
    <el-image-viewer v-if="showViewer" :on-close="onClose" :url-list="imageList"/>
    <!-- excel导入 -->
    <import-send-dialog :visible.sync="importSendVisible" :excel-code="info.excelCode" :extral-data="info.extralData" ref="importSendRef"></import-send-dialog>
  </div>
</template>
<script>
import activityDoctor from '../../components/activity_doctor'
import { queryVocationalHospitalList, activityActivityDoctorPage, switchActivityDoctorQuality } from '@/subject/admin/api/cmp_api/education'
import ElImageViewer from 'element-ui/packages/image/src/image-viewer';
import ImportSendDialog from '@/subject/admin/components/ImportSendDialog'
import { createDownLoad } from '@/subject/admin/api/common'
export default {
  name: 'SeeDoctor',
  components: {
    activityDoctor,
    ElImageViewer,
    ImportSendDialog
  },
  data() {
    return {
      query: {
        doctorId: '',
        name: '',
        hospitalName: '',
        hospitalId: '',
        total: 0,
        page: 1,
        limit: 10,
        id: ''
      },
      dataList: [],
      loading: false,
      //活动医生
      doctorData: [],
      showDialog: false,
      //图片放大弹窗
      showViewer: false,
      imageList: [],
      // 导入
      importSendVisible: false,
      // 导入任务参数 
      info: {
        excelCode: 'importActivityDocPatientDoctor',
        extralData: {
          type: ''
        }
      }
    }
  },
  mounted() {
    let query = this.$route.params;
    if (query.id) {
      this.query.id = this.info.extralData.type = parseInt(query.id)
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
    onClose() {
      this.imageList = [];
      this.showViewer = false;
    },
    // 点击图片
    seeClick(val) {
      if (val.qrcodeUrl != '') {
        this.imageList.push(val.qrcodeUrl)
      }
      this.showViewer = true;
    },
    //活动医生返回数据
    addDoctor() {
      this.showDialog = false;
      this.getList()
    },
    // 获取数据列表
    async getList() {
      let query = this.query;
      this.loading = true;
      let data = await activityActivityDoctorPage(
        query.id,
        parseInt(query.doctorId),
        query.name,
        query.hospitalId,
        query.page,
        query.limit
      )
      if (data) {
        this.dataList = data.records;
        this.query.total = data.total;
      }
      this.loading = false;
    },
    //获取第一执业机构数据
    async querySearchAsync(queryString, cb) {
      var results = [];
      if (queryString == '') {
        cb(results)
      } else {
        this.$common.showLoad();
        let data = await queryVocationalHospitalList(
          queryString
        )
        this.$common.hideLoad();
        if (data !== undefined) {
          if (data && data.length > 0) {
            for (let i = 0; i < data.length; i ++) {
              results.push({
                value: data[i].designation,
                id: data[i].id
              })
            }
            cb(results)
          } else {
            results.push({
              value: '暂未搜索到数据',
              id: ''
            })
            cb(results)
          }
        } else {
          results = [];
          cb(results)
        }
      }
    },
    handleSelect(item) {
      if (item.id == '') {
        this.query.hospitalId = '';
        this.query.hospitalName = ''
      } else {
        this.query.hospitalId = item.id;
      }
    },
    // 点击添加医生
    addClick() {
      this.showDialog = true;
    },
    //导入数据
    importClick() {
      this.importSendVisible = true;
      this.$nextTick( () => {
        this.$refs.importSendRef.init()
      })
    },
    // 导出
    async downLoadTemp() {
      const query = this.query
      this.$common.showLoad()
      const data = await createDownLoad({
        className: 'hmcActivityDocExportService',
        fileName: '医生数据导出',
        groupName: '医生数据导出',
        menuName: '医带患-医生数据导出',
        searchConditionList: [
          {
            desc: '活动ID',
            name: 'activityId',
            value: query.id || ''
          },
          {
            desc: '医生ID',
            name: 'doctorId',
            value: query.doctorId || ''
          },
          {
            desc: '医生姓名',
            name: 'name',
            value: query.name || ''
          },
          {
            desc: '第一执业机构',
            name: 'hospitalId',
            value: query.hospitalId || ''
          }
        ]
      })
      this.$common.hideLoad()
      if (typeof data !== 'undefined') {
        this.$common.n_success('创建下载任务成功，请在下载中心查看')
      }
    },
    //点击恢复以及取消活动资格
    activityClick(row) {
      this.$confirm(`确定 ${ row.doctorStatus == 1 ? '取消' : '恢复' } ${ row.name } 医生的活动资格！`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }) 
      .then( async() => {
        this.$common.showLoad();
        let data = await switchActivityDoctorQuality(
          this.query.id,
          row.doctorId,
          row.doctorStatus == 1 ? 2 : 1
        )
        this.$common.hideLoad();
        if (data !== undefined) {
          this.$common.n_success('操作成功！');
          this.getList()
        }
      })
      .catch(() => {
        //取消
      });
    },
    checkInput(val) {
      val = val.replace(/[^0-9]/gi, '')
      return val
    },
    // 搜索
    handleSearch() {
      this.query.page = 1;
      this.getList();
    },
    // 重置
    handleReset() {
      this.query = {
        doctorId: '',
        name: '',
        hospitalName: '',
        hospitalId: '',
        total: 0,
        page: 1,
        limit: 10,
        id: this.query.id
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
