<template>
  <div>
    <yl-dialog
      width="900px"
      title="添加活动医生"
      :show-footer="true"
      :visible.sync="showDialog"
      @confirm="confirm">
      <div class="pop-up">
        <div class="common-box">
          <div class="search-box">
            <el-row class="box">
              <el-col :span="8">
                <div class="title">医生姓名</div>
                <el-input v-model="dialog.name" placeholder="请输入医生姓名" @keyup.enter.native="searchEnter"/>
              </el-col>
              <el-col :span="8">
                <div class="title">第一执业机构</div>
                <el-autocomplete 
                  style="width: 260px"
                  v-model="dialog.hospitalName" 
                  :fetch-suggestions="querySearchAsync" 
                  placeholder="请输入执业机构名称" 
                  @select="handleSelect"
                  clearable>
                </el-autocomplete>
              </el-col>
              <el-col :span="8">
                <div class="title">来源</div>
                <el-select v-model="dialog.source" placeholder="请选择">
                  <el-option label="全部" :value="''"></el-option>
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
              <el-col :span="8">
                <div class="title">医生ID</div>
                <el-input v-model="dialog.doctorId" placeholder="请输入医生ID" @keyup.enter.native="searchEnter"/>
              </el-col>
              <el-col :span="8">
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
          <div class="search-box mar-t-16">
            <el-row class="box">
              <el-col :span="16">
                <yl-search-btn
                  :total="dialog.total"
                  @search="handleSearch"
                  @reset="handleReset"/>
              </el-col>
            </el-row>
          </div>
        </div>
        <div class="table-conter">
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
            @getList="getList">
            <el-table-column type="selection" align="center" width="70" :selectable="selected"></el-table-column>
            <el-table-column label="医生ID" min-width="70" align="center" prop="doctorId"></el-table-column>
            <el-table-column label="医生姓名" min-width="90" align="center" prop="name"></el-table-column>
            <el-table-column label="第一执业医院" min-width="110" align="center" prop="hospitalName"></el-table-column>
            <el-table-column label="来源" min-width="110" align="center">
              <template slot-scope="{ row }">
                <div>
                  {{ row.source | dictLabel(sourceData) }}
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
  </div>
</template>
<script>
import { activityDoctorPage, activitySave, queryVocationalHospitalList } from '@/subject/admin/api/cmp_api/education'
export default {
  props: {
    // 是否显示 弹窗
    show: {
      type: Boolean,
      default: true
    },
    activityId: {
      type: Number,
      default: null
    },
    // 从父级传递过来的 数据
    doctorData: {
      type: Array,
      default: () => []
    }
  },
  computed: {
    showDialog: {
      get() {
        return this.show
      },
      set(val) {
        this.$emit('update:show', val)
      }
    }
  },
  data() {
    return {
      dialogList: [],
      dialogLoading: false,
      dialog: {
        name: '',
        hospitalName: '',
        hospitalId: '',
        source: '',
        establishTime: [],
        doctorId: '',
        total: 0,
        page: 1,
        limit: 10
      },
      // 来源数据
      sourceData: [
        {
          label: '用户自主完善',
          value: 0
        },
        {
          label: '销售助手APP',
          value: 1
        },
        {
          label: '运营人员导入或创建',
          value: 2
        }
      ],
      multipleSelection: []
    }
  },
  mounted() {
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
    // 判断 医生是否勾选
    selected(row, index) {
      if (row.activityState == 1) {
        return false
      } else {
        return true
      }
    },
    // 添加活动医生
    async getList() {
      this.showDialog = true;
      this.dialogLoading = true;
      let dialog = this.dialog;
      let data = await activityDoctorPage(
        this.activityId,
        dialog.hospitalId,
        dialog.name,
        dialog.source,
        dialog.establishTime && dialog.establishTime.length > 0 ? dialog.establishTime[0] : '',
        dialog.establishTime && dialog.establishTime.length > 1 ? dialog.establishTime[1] : '',
        dialog.page,
        dialog.limit,
        dialog.doctorId
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
    //点击确定
    async confirm() {
      if (this.multipleSelection && this.multipleSelection.length > 0) {
        this.$common.showLoad();
        let data = await activitySave(
          this.activityId,
          this.multipleSelection
        )
        this.$common.hideLoad();
        if (data !== undefined) {
          this.$common.n_success('添加医生成功');
          this.$emit('addDoctor')
        }
      } else {
         this.$common.warn('请选择活动医生')
      }
    },
    // 表格全选
    handleSelectionChange(val) {
      this.multipleSelection = [];
      val.forEach(element => {
        this.multipleSelection.push({ doctorId: element.doctorId })
      });
    },
    // 搜索
    handleSearch() {
      this.dialog.page = 1;
      this.getList();
    },
    // 重置
    handleReset() {
      this.dialog = {
        name: '',
        hospitalName: '',
        hospitalId: '',
        source: '',
        establishTime: [],
        doctorId: '',
        total: 0,
        page: 1,
        limit: 10
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
        this.dialog.hospitalId = '';
        this.dialog.hospitalName = ''
      } else {
        this.dialog.hospitalId = item.id;
      }
    }
  }
}
</script>
<style lang="scss" scoped>
@import './index.scss';
</style>