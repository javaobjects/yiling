<template>
  <div class="app-container">
    <div class="app-container-content">
      <!-- 顶部切换 -->
      <div class="app-tabs mar-b-10">
        <el-tabs v-model="activeName" @tab-click="handleClick">
          <el-tab-pane label="sql查询" name="1"></el-tab-pane>
          <el-tab-pane label="保存任务" name="2"></el-tab-pane>
          <el-tab-pane label="版本升级" name="3"></el-tab-pane>
        </el-tabs>
      </div>
      <div v-if="activeName == '1'">
        <div class="common-box box-search">
          <div class="search-box">
            <el-row class="box">
              <el-col :span="24">
                <div class="title">sql语句</div>
                <el-input 
                  type="textarea" 
                  class="elInput" 
                  :rows="5" 
                  placeholder="请输入sql语句" 
                  show-word-limit 
                  v-model="query.sqlContext">
                </el-input>
              </el-col>
            </el-row>
          </div>
          <div class="search-box mar-t-16">
            <el-row class="box">
              <el-col :span="16">
                <yl-search-btn :total="query.total" @search="handleSearch" @reset="handleReset" />
              </el-col>
            </el-row>
          </div>
        </div>
      </div>
      <div v-if="activeName == '2'">
        <div class="common-box box-search">
          <div class="search-box">
            <el-row class="box">
              <el-col :span="6">
                <div class="title">系统信息</div>
                <el-select class="select-width" v-model="taskNo" placeholder="请选择" @change="selectChange">
                  <el-option
                    v-for="item in xtData"
                    :key="item.taskNo"
                    :label="item.taskName"
                    :value="item.taskNo">
                  </el-option>
                </el-select>
              </el-col>
              <el-col :span="6">
                <div class="title">对接选项</div>
                <el-radio-group v-model="query2.taskStatus" :disabled="taskNo == '' ? true : false">
                  <el-radio :label="'1'">开启对接</el-radio>
                  <el-radio :label="'0'">关闭对接</el-radio>
                </el-radio-group>
              </el-col>
              <el-col :span="6">
                <div class="title">同步频率</div>
                <el-input v-model="query2.taskInterval" :disabled="taskNo == '' ? true : false" placeholder="同步频率1440分钟(24小时)以内" />
              </el-col>
              <el-col :span="6" v-if="taskNo == '40000006' || taskNo == '40000007'">
                <div class="title">流向数据天数</div>
                <el-input v-model="query2.flowDateCount" :disabled="taskNo == '' ? true : false" placeholder="请输入流向数据天数" />
              </el-col>
            </el-row>
            <el-row class="box mar-t-16">
              <el-col :span="24">
                <div class="title">sql语句</div>
                <el-input 
                  type="textarea" 
                  class="elInput" 
                  :disabled="taskNo == '' ? true : false" 
                  :rows="5" 
                  placeholder="请输入sql语句" 
                  show-word-limit 
                  v-model="query2.taskSql">
                </el-input>
              </el-col>
            </el-row>
            <el-row class="box mar-t-16">
              <el-col :span="24">
                <div class="title">对接字段描述：{{ query2.describe }}</div>
              </el-col>
            </el-row>
          </div>
          <div class="search-box mar-t-16">
            <yl-button type="primary" @click="preservationClick">保存</yl-button>
            <yl-button type="primary" @click="sqlClick">执行sql</yl-button>
            <yl-button type="primary" @click="deleteClick">清除缓存数据</yl-button>
          </div>
          <div class="data-length" v-if="tableType">
            （已为您检索出{{ query.total }}条数据）
          </div>
        </div>
      </div>
      <div v-if="activeName == '3'">
        <div class="common-box box-search">
          <div class="search-box">
            <yl-button type="primary" @click="upgradeClick">升级</yl-button>
          </div>
          <div>
            <p v-for="(item,index) in logData" :key="index">
              {{ item }}
            </p>
          </div>
        </div>
      </div>
     
      <div v-if="activeName == '1' || tableType">
        <yl-table 
          border 
          :show-header="true" 
          :list="dataList" 
          :total="query.total" 
          :loading="loading" 
          :show-pagin="false">
          <el-table-column 
            v-for="(item,index) in keys" 
            :key="index" 
            :label="item" 
            min-width="100" 
            align="center" 
            :prop="item">
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </div>
</template>

<script>
import { 
  querySql, 
  systeMassage, 
  taskGet, 
  taskSave, 
  deleteCache, 
  sqlExecute, 
  clientUpdate, 
  updateLog 
} from '@/subject/admin/api/zt_api/erpMonitorDetails'
export default {
  name: 'ErpMonitorDetails',
  data() {
    return {
      query: {
        total: 0,
        sqlContext: ''
      },
      //传递过来的 id值
      suId: '', 
      activeName: '1',
      xtData: [],
      dataList: [],
      loading: false,
      keys: [],
      //系统信息的 key
      taskNo: '', 
      query2: {
        taskSql: '',
        taskInterval: '',
        taskStatus: ''
      },
      tableType: false,
      timer: '',
      //升级输出
      logData: [] 
    }
  },
  mounted() {
    let data = this.$route.params;
    if (data) {
      this.suId = data.id;
      // 获取系统信息
      this.systeData(); 
    }
  },
  methods: {
    // 获取系统信息
    async systeData() {
      let data = await systeMassage(this.suId)
      if (data) {
        this.xtData = data;
      }
    },
    // 点击了 系统信息
    async selectChange() {
      this.$common.showLoad();
      let data = await taskGet(this.suId, this.taskNo)
      this.$common.hideLoad();
      if (data !== undefined) {
        this.query2 = data;
      }
    },
    // 点击保存
    async preservationClick() {
      this.tableType = false;
      let query = this.query2;
      this.$common.showLoad();
      let data = await taskSave(
        query.createTime,
        query.flowDateCount,
        query.methodName,
        query.springId,
        query.suId,
        query.syncStatus,
        query.syncTime,
        query.taskInterval,
        query.taskKey,
        query.taskName,
        this.taskNo,
        query.taskSql,
        query.taskStatus,
        query.updateTime
      )
      this.$common.hideLoad();
      if (data !== undefined) {
        this.$common.alert('保存成功', r => {})
        this.taskNo = '';
        this.query2 = {};
      }
    },
    // 清除缓存数据
    async deleteClick() {
      this.tableType = false;
      this.$common.showLoad();
      let data = await deleteCache(
        this.suId,
        this.taskNo
      )
      this.$common.hideLoad();
      if (data !== undefined) {
        this.$common.alert('清除成功', r => {})
        this.taskNo = '';
        this.query2 = {};
      }
    },
    // 点击执行sql
    async sqlClick() {
      this.keys = [];
      this.tableType = true;
      this.loading = true;
      let query = this.query2;
      let data = await sqlExecute(
        query.flowDateCount,
        query.suId,
        query.taskNo,
        query.taskSql	
      )
      if (data) {
        this.$common.n_success('执行成功')
        let erpData = JSON.parse(data);
        if (erpData.code == 200) {
          this.dataList = erpData.rows;
          this.query.total = erpData.total;
          for (let i in erpData.rows[0])
            this.keys.push(i);
        }
      }
      this.loading = false;
    },
    // 内容
    async getList() {
      this.keys = [];
      let query = this.query;
      this.loading = true;
      let data = await querySql(
        query.sqlContext,
        '2000',
        this.suId
      )
      if (data) {
        this.query.total = data.total;
        if (data.jsonData) {
          let erpData = JSON.parse(data.jsonData);
          this.dataList = erpData;
          for (let i in erpData[0]) {
            this.keys.push(i);
          }
        }
      }
      this.loading = false;
    },
    // 点击顶部切换
    handleClick(tab, event) {
      this.activeName = tab.name;
      this.tableType = false;
      this.dataList = [];
      this.keys = [];
      this.query = {
        total: 0,
        sqlContext: ''
      }
      clearInterval(this.timer)
    },
    // 点击升级
    async upgradeClick() {
      this.$common.showLoad();
      let data = await clientUpdate(this.suId)
      this.$common.hideLoad();
      if (data !== undefined) {
        if (data.result) {
          this.$common.n_success('升级成功，日志正在疯狂输出中...')
          this.timer = setInterval(this.logApi, 2000)
        }
      }
    },
    async logApi() {
      this.$common.showLoad();
      let data = await updateLog(this.suId)
      this.$common.hideLoad();
      if (data !== undefined) {
        data.message.forEach(element => {
          this.logData.push(element)
        });
        if (data.status == '1') {
          clearInterval(this.timer)
        }
      }
    },
    // 搜索
    handleSearch() {
      this.dataList = [];
      this.getList();
    },
    // 清空
    handleReset() {
      this.query = {
        total: 0,
        sqlContext: ''
      }
    }
  },
  beforeDestroy() {
    clearInterval(this.timer)
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>