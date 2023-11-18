<template>
  <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">医生姓名</div>
              <el-input v-model="query.name" placeholder="请输入医生姓名" @keyup.enter.native="searchEnter"/>
            </el-col>
            <el-col :span="10">
              <div class="title">第一执业机构</div>
              <el-autocomplete
                style="width: 260px"
                v-model="query.hospitalName" 
                :fetch-suggestions="querySearchAsync" 
                placeholder="请输入执业机构名称" 
                @select="handleSelect">
              </el-autocomplete>
            </el-col>
          </el-row>
        </div>
        <div class="search-btn-box mar-t-16">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn :total="query.total" @search="handleSearch" @reset="handleReset"/>
            </el-col>
          </el-row>
        </div>
      </div>
      <div class="down-box">
        <div class="btn">
          <div>
            <yl-button type="primary" @click="addDoctor">添加活动医生</yl-button>
          </div>
          <div class="right">
            <yl-button type="primary" plain @click="exportDoctor">导出活动医生</yl-button>
          </div>
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
          <el-table-column align="center" min-width="70" label="医生ID" prop="doctorId">
          </el-table-column>
          <el-table-column align="center" min-width="120" label="医生姓名" prop="name">
          </el-table-column>
          <el-table-column align="center" min-width="150" label="第一执业医院" prop="hospitalName">
          </el-table-column>
          <el-table-column align="center" min-width="110" label="邀请病例数" prop="caseCount">
          </el-table-column>
          <el-table-column align="center" min-width="130" label="活动码">
            <template slot-scope="{ row }">
              <div class="imgUrl" @click="seeClick(row)">
                <el-image style="width: 100%; height: 100%" :src="row.qrcodeUrl"></el-image>
              </div>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="120" label="参与时间">
            <template slot-scope="{ row }">
              <div>
                {{ row.createTime | formatDate }}
              </div>
            </template>
          </el-table-column>
          <el-table-column fixed="right" align="center" label="操作" min-width="90">
            <template slot-scope="{ row }">
              <yl-button type="text" @click="deleteClick(row)">移除</yl-button>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
    <div class="bottom-view flex-row-center">
      <yl-button type="primary" plain @click="$router.go(-1)">返回</yl-button>
    </div>
    <!-- 添加活动医生 -->
    <yl-dialog
      title="添加活动医生"
      width="800px"
      :visible.sync="showDialog"
      :show-footer="true"
      :show-close="false"
      @confirm="confirm"
      class="dialog-updata">
      <div class="dialog-conter">
        <div class="common-box">
          <div class="search-box">
            <el-row class="box">
              <el-col :span="8">
                <div class="title">医生姓名</div>
                <el-input v-model="dialog.name" placeholder="请输入医生姓名" @keyup.enter.native="searchEnter2"/>
              </el-col>
              <el-col :span="10">
                <div class="title">第一执业机构</div>
                <el-autocomplete 
                  style="width: 260px"
                  v-model="dialog.hospitalName" 
                  :fetch-suggestions="querySearchAsync" 
                  placeholder="请输入执业机构名称" 
                  @select="handleSelect2">
                </el-autocomplete>
              </el-col>
              <el-col :span="6">
                <div class="title">来源</div>
                <el-select v-model="dialog.source" placeholder="请选择">
                  <el-option
                    v-for="item in sourceData"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                  </el-option>
                </el-select>
              </el-col>
            </el-row>
          </div>
          <div class="search-box mar-t-16">
            <el-row class="box">
              <el-col :span="12">
                <div class="title">医生创建日期</div>
                <el-date-picker
                  v-model="dialog.establishTime"
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
          <div class="search-btn-box mar-t-16">
            <el-row class="box">
              <el-col :span="16">
                <yl-search-btn :total="dialog.total" @search="dialogSearch" @reset="dialogReset"/>
              </el-col>
            </el-row>
          </div>
        </div>
        <!-- 下部列表 -->
        <div class="search-bar mar-t-8">
          <yl-table
            border
            ref="singTable"
            :selection-change="handleSelectionChange"
            :stripe="true"
            :show-header="true"
            :list="dialogList"
            :total="dialog.total"
            :page.sync="dialog.page" 
            :limit.sync="dialog.limit" 
            :loading="dialogLoading"
            @getList="addDoctor">
            <el-table-column type="selection" align="center" width="70" :selectable="selected"></el-table-column>
            <el-table-column label="医生ID" min-width="70" align="center" prop="doctorId"></el-table-column>
            <el-table-column label="医生姓名" min-width="90" align="center" prop="name"></el-table-column>
            <el-table-column label="第一执业医院" min-width="110" align="center" prop="hospitalName"></el-table-column>
            <el-table-column label="来源" min-width="110" align="center">
              <template slot-scope="{ row }">
                <div>
                  {{ row.source == 0 ? '用户自主完善' : (row.source == 1 ? '销售助手APP' : '运营人员导入或创建') }}
                </div>
              </template>
            </el-table-column>
            <el-table-column label="医生创建时间" min-width="110" align="center">
              <template slot-scope="{ row }">
                <div>{{ row.createTime | formatDate }}</div>
              </template>
            </el-table-column>
          </yl-table>
        </div>
      </div>
    </yl-dialog>
    <!-- 图片放大 -->
    <el-image-viewer v-if="showViewer" :on-close="onClose" :url-list="imageList"/>
  </div>
</template>
<script>
import { queryActivityDoctorPage, queryVocationalHospitalList, doctorPage, doctorSave, deleteActivityDoctor } from '@/subject/admin/api/cmp_api/education'
import { createDownLoad } from '@/subject/admin/api/common'
import ElImageViewer from 'element-ui/packages/image/src/image-viewer';
export default {
  name: 'ActivityDoctor',
  components: {
    ElImageViewer
  },
  data() {
    return {
      query: {
        name: '',
        hospitalId: '',
        hospitalName: '',
        total: 0,
        page: 1,
        limit: 10
      },
      loading: false,
      //第一执业机构数据
      practiceData: [],
      // 医生列表
      dataList: [],
      showDialog: false,
      dialog: {
        name: '',
        hospitalName: '',
        hospitalId: '',
        source: '',
        establishTime: [],
        total: 10,
        page: 1,
        limit: 10
      },
      // 来源数据
      sourceData: [
        {
          label: '全部',
          value: ''
        },
        {
          label: '用户自主完善',
          value: '0'
        },
        {
          label: '销售助手APP',
          value: '1'
        },
        {
          label: '运营人员导入或创建',
          value: '2'
        }
      ],
      dialogLoading: false,
      dialogList: [],
      // 当前选中 的活动医生
      multipleSelection: [],
      // 活动id	
      activityId: '',
      //图片放大弹窗
      showViewer: false,
      // 放大缓存图
      imageList: []
    }
  },
  mounted() {
    let query = this.$route.params;
    if (query.id && query.id !== '0') {
      this.activityId = query.id;
      console.log(this.activityId, 9999)
      this.getList()
    }
  },
  methods: {
    // 判断 医生是否勾选
    selected(row, index) {
      if (row.activityState == 1) {
        return false
      } else {
        return true
      }
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
    //弹窗执业 机构的选择
    handleSelect2(item) {
      if (item.id == '') {
        this.dialog.hospitalId = '';
        this.dialog.hospitalName = ''
      } else {
        this.dialog.hospitalId = item.id;
      }
    },
    // 点击图片
    seeClick(val) {
      if (val.qrcodeUrl != '') {
        this.imageList.push(val.qrcodeUrl)
      }
      this.showViewer = true;
    },
    // 关闭图片放大弹窗
    onClose() {
      this.imageList = [];
      this.showViewer = false;
    },
    async getList() {
      this.loading = true;
      let query = this.query;
      let data = await queryActivityDoctorPage(
        this.activityId,
        query.page,
        query.hospitalId,
        query.name,
        query.limit
      )
      if (data) {
        this.dataList = data.records;
        this.query.total = data.total;
      }
      this.loading = false;
    },
    // 导出
    async exportDoctor() {
      let query = this.query
      this.$common.showLoad()
      let data = await createDownLoad({
        className: 'hmcActivityDoctorExportService',
        fileName: '导出活动医生',
        groupName: '导出活动医生',
        menuName: '患者教育活动 - 活动医生',
        searchConditionList: [
          {
            desc: '活动id',
            name: 'activityId',
            value: this.activityId || ''
          },
          {
            desc: '第一执业机构',
            name: 'hospitalId',
            value: query.hospitalId || ''
          },
          {
            desc: '医生姓名',
            name: 'name',
            value: query.name || ''
          }
        ]
      })
      this.$common.hideLoad()
      if (data && data.result) {
        this.$common.n_success('创建下载任务成功，请在下载中心查看')
      }
    },
    // 移除
    deleteClick(row) {
      this.$confirm(`确定移除 ${ row.name } 医生！`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      .then( async() => {
        this.$common.showLoad();
        let data = await deleteActivityDoctor(
          this.activityId,
          row.doctorId
        )
        this.$common.hideLoad();
        if (data !== undefined) {
          this.$common.n_success('移除成功');
          this.getList()
        }
      })
      .catch(() => {
        //取消
      });
    },
    // 添加活动医生
    async addDoctor() {
      this.showDialog = true;
      this.dialogLoading = true;
      let dialog = this.dialog;
      let data = await doctorPage(
        this.activityId,
        dialog.hospitalId,
        dialog.name,
        dialog.source,
        dialog.establishTime && dialog.establishTime.length > 0 ? dialog.establishTime[0] : '',
        dialog.establishTime && dialog.establishTime.length > 1 ? dialog.establishTime[1] : '',
        dialog.page,
        dialog.limit
      )
      this.dialogLoading = false;
      if (data) {
        this.dialogList = data.records;
        this.dialog.total = data.total
      }
      this.$nextTick(() => {
        let arr = []
        this.dialogList.forEach(row => {
          if (row.activityState == 1) {
            this.$refs.singTable.toggleRowSelectionMethod(row, true)
            arr.push({ doctorId: row.doctorId })
            this.multipleSelection = arr
          }
        })
      })
    },
    // 确认
    async confirm() {
      if (this.multipleSelection && this.multipleSelection.length > 0) {
        this.$common.showLoad();
        let data = await doctorSave(
          this.activityId,
          this.multipleSelection
        )
        this.$common.hideLoad();
        if (data !== undefined) {
          this.$common.n_success('添加医生成功');
          this.showDialog = false;
          this.getList();
        }
      }
    },
    // 搜索
    handleSearch() {
      this.query.page = 1;
      this.getList();
    },
    handleReset() {
      this.query = {
        name: '',
        hospitalId: '',
        hospitalName: '',
        total: 0,
        page: 1,
        limit: 10
      }
    },
    // 弹窗 搜索
    dialogSearch() {
      this.dialog.page = 1;
      this.addDoctor();
    },
    // 清空 弹窗查询条件
    dialogReset() {
      this.dialog = {
        name: '',
        hospitalName: '',
        hospitalId: '',
        source: '',
        establishTime: [],
        total: 10,
        page: 1,
        limit: 10
      }
    },
    handleSelectionChange(val) {
      this.multipleSelection = [];
      val.forEach(element => {
        this.multipleSelection.push({ doctorId: element.doctorId })
      });
    },
    searchEnter(e) {
      const keyCode = window.event ? e.keyCode : e.which;
      if (keyCode === 13) {
        this.getList()
      }
    },
    searchEnter2(e) {
      const keyCode = window.event ? e.keyCode : e.which;
      if (keyCode === 13) {
        this.addDoctor()
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>