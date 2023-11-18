<template>
  <div class="app-container">
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">活动名称</div>
              <el-input v-model="query.activityName" placeholder="请输入活动名称" @keyup.enter.native="searchEnter"/>
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
          <yl-button type="primary" @click="modifyClick(0, 1)">创建添加</yl-button>
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
          <el-table-column align="center" min-width="80" label="活动ID" prop="id">
          </el-table-column>
          <el-table-column align="center" min-width="140" label="活动名称" prop="activityName">
          </el-table-column>
          <el-table-column align="center" min-width="100" label="活动页UV" prop="uvCount">
          </el-table-column>
          <el-table-column align="center" min-width="100" label="参与医生人数" prop="activityDoctorCount">
          </el-table-column>
          <el-table-column align="center" min-width="90" label="拉新患者数" prop="patientCount">
          </el-table-column>
          <el-table-column align="center" min-width="90" label="活动状态">
            <template slot-scope="{ row }">
              {{ row.activityStatus == 1 ? '启用' : '禁用' }}
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="90" label="活动进度">
            <template slot-scope="{ row }">
              {{ row.activityProgress == 1 ? '未开始' : (row.activityProgress == 2 ? '进行中' : '已结束') }}
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="110" label="活动开始时间">
            <template slot-scope="{ row }">
              <div>{{ row.beginTime | formatDate }}</div>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="110" label="活动结束时间">
            <template slot-scope="{ row }">
              <div>{{ row.endTime | formatDate }}</div>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="110" label="活动创建时间">
            <template slot-scope="{ row }">
              <div>{{ row.createTime | formatDate }}</div>
            </template>
          </el-table-column>
          <el-table-column fixed="right" align="center" label="操作" min-width="200">
            <template slot-scope="{ row }">
              <yl-button type="text" @click="seeClick(row, 'SeeDoctor')">查看医生</yl-button>
              <yl-button type="text" @click="seeClick(row, 'ViewPatient')">查看患者</yl-button>
              <yl-button type="text" @click="modifyClick(row.id, 2)">查看活动</yl-button>
              <yl-button type="text" @click="modifyClick(row.id, 3)" v-if="row.activityProgress == 1">编辑</yl-button>
              <yl-button type="text" @click="stopClick(row)" v-if="row.activityStatus == 1 && row.activityProgress != 3">
                <div>停用</div>
              </yl-button>
              <yl-button type="text" @click="urlClick(row)">复制链接</yl-button>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </div>
</template>
<script>
import { docToPatientPageList, stopActivity } from '@/subject/admin/api/cmp_api/education'
export default {
  name: 'DoctorPatient',
  data() {
    return {
      query: {
        activityName: '',
        total: 0,
        page: 1,
        limit: 10
      },
      dataList: [],
      loading: false
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
    // 复制链接
    urlClick(row) {
      if (row.activityLink != '') {
        let cInput = document.createElement('input');
        cInput.value = row.activityLink;
        document.body.appendChild(cInput);
        cInput.select();
        document.execCommand('Copy');
        this.$common.n_success('复制链接成功')
        cInput.remove();
      }
    },
    // 获取数据列表
    async getList() {
      let query = this.query;
      this.loading = true;
      let data = await docToPatientPageList(
        query.activityName,
        query.page,
        query.limit
      )
      if (data) {
        this.dataList = data.records;
        this.query.total = data.total;
      }
      this.loading = false;
    },
    // 编辑
    modifyClick(row, type) {
      // type 1 新增， 2查看， 3编辑
      this.$router.push({
        name: 'ActivityEstablish',
        params: {
          id: row,
          type: type
        }
      })
    },
    // 查看医生/患者
    seeClick(row, val) {
      this.$router.push({
        name: val,
        params: {
          id: row.id
        }
      })
    },
    //点击停用活动
    stopClick(row) {
      this.$confirm(`确定 ${ row.activityName } 活动停用！`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }) 
      .then( async() => {
        this.$common.showLoad();
        let data = await stopActivity(row.id)
        this.$common.hideLoad();
        if (data !== undefined) {
          this.$common.n_success('停用成功！');
          this.getList()
        }
      })
      .catch(() => {
        //取消
      });
    },
    // 搜索
    handleSearch() {
      this.query.page = 1;
      this.getList();
    },
    // 重置
    handleReset() {
      this.query = {
        activityName: '',
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
