<template>
  <div>
    <yl-dialog
      width="900px"
      title="选择医生"
      :show-footer="false"
      :visible.sync="showDialog">
      <div class="pop-up">
        <div class="common-box">
          <div class="search-box">
            <el-row class="box">
              <el-col :span="8">
                <div class="title">医生姓名</div>
                <el-input v-model="dialog.doctorName" placeholder="请输入医生姓名" @keyup.enter.native="searchEnter"/>
              </el-col>
              <el-col :span="8">
                <div class="title">所属医院</div>
                <el-input v-model="dialog.hospitalName" placeholder="请输入医院名称" @keyup.enter.native="searchEnter"/>
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
            show-header
            :list="dialogData"
            :total="dialog.total"
            :page.sync="dialog.page"
            :limit.sync="dialog.limit"
            :loading="loading"
            @getList="getList">
            <el-table-column align="center" min-width="90" label="医生ID" prop="id"></el-table-column>
            <el-table-column align="center" min-width="150" label="医生姓名" prop="doctorName"></el-table-column>
            <el-table-column align="center" min-width="180" label="所属医院" prop="hospitalName"></el-table-column>
            <el-table-column align="center" min-width="150" label="所属医院科室" prop="hospitalDepartment"></el-table-column>
            <el-table-column fixed="right" align="center" label="操作" min-width="100">
              <template slot-scope="{ row }">
                <yl-button type="text" @click="seeClick(row)">
                  <span :style="{color: row.statusType == 1 ? '#1790ff' : '#c8c9cc'}">
                    {{ row.statusType == '1' ? '选择' : '已添加' }}
                  </span>
                </yl-button>
              </template>
            </el-table-column>
          </yl-table>
        </div>
      </div>
    </yl-dialog>
  </div>
</template>
<script>
import { queryDoctorByNameAndHospital } from '@/subject/admin/api/content_api/article_video'
export default {
  props: {
    // 是否显示 弹窗
    show: {
      type: Boolean,
      default: true
    },
    // 从父级传递过来的 数据
    dataList: {
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
      dialogData: [],
      loading: false,
      dialog: {
        doctorName: '',
        hospitalName: '',
        total: 0,
        page: 1,
        limit: 6
      }
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
    async getList() {
      this.loading = true;
      let dialog = this.dialog;
      this.dialogData = [];
      let data = await queryDoctorByNameAndHospital(
        dialog.doctorName,
        dialog.hospitalName,
        dialog.page,
        dialog.limit
      )
      if (data) {
        for (let i = 0; i < data.records.length; i ++) {
          this.dialogData.push({
            ...data.records[i],
            statusType: 1
          })
        }
        this.dialog.total = data.total;
        if (this.dataList && this.dataList.length > 0) {
          for (let l = 0; l < this.dialogData.length; l ++) {
            for (let y = 0; y < this.dataList.length; y ++) {
              if (this.dialogData[l].id == this.dataList[y].id) {
                this.dialogData[l].statusType = 2;
                break;
              } else {
                this.dialogData[l].statusType = 1;
              }
            }
          }
        }
      }
      this.loading = false;
    },
    seeClick(row) {
      this.$emit('addDoctor', row)
    },
    // 搜索
    handleSearch() {
      this.dialog.page = 1;
      this.getList();
    },
    // 重置
    handleReset() {
      this.dialog = {
        doctorName: '',
        hospitalName: '',
        total: 0,
        page: 1,
        limit: 6
      }
    }
  }
}
</script>
<style lang="scss" scoped>
@import './index.scss';
</style>