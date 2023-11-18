<template>
  <div class="app-container">
    <div class="app-container-content">
      <div class="common-box box-search">
        <div class="search-box">
          <el-row class="box">

            <el-col :span="6">
              <div class="title">请求ID</div>
              <el-input v-model="query.requestId" placeholder="请输入请求ID" @keyup.enter.native="handleSearch"/>
            </el-col>
            <el-col :span="6">
              <div class="title">操作人ID</div>
              <el-input v-model="query.operId" placeholder="请输入操作人ID" @keyup.enter.native="handleSearch"/>
            </el-col>

            <el-col :span="12">
              <div class="title">请求URL</div>
              <el-input v-model="query.requestUrl" placeholder="请输入请求URL" @keyup.enter.native="handleSearch"/>
            </el-col>
            <!-- <el-col :span="6">
              <div class="title">请求方法</div>
              <el-select v-model="query.requestMethod" placeholder="请选择状态">
                <el-option label="全部" value=""></el-option>
                <el-option label="POST" value="POST"></el-option>
                <el-option label="GET" value="GET"></el-option>
              </el-select>
            </el-col> -->
            <!-- <el-col :span="6">
              <div class="title">业务类型</div>
              <el-select v-model="query.businessType" placeholder="请选择业务类型">
                <el-option label="全部" value=""></el-option>
                <el-option
                  v-for="item in queryLogBusinessType"
                  :label="item.label"
                  :value="item.value"
                  :key="item.id"
                ></el-option>
              </el-select>
            </el-col> -->

          </el-row>
          <el-row class="box">
            <el-col :span="6">
              <div class="title">系统标识</div>
              <el-select v-model="query.systemId" placeholder="请选择系统标识">
                <el-option label="全部" value=""></el-option>
                <el-option
                  v-for="item in queryLogSystemType"
                  :label="item.label"
                  :value="item.value"
                  :key="item.id"
                ></el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <div class="title">操作状态</div>
              <el-select v-model="query.status" placeholder="请选择状态">
                <el-option label="全部" :value="0"></el-option>
                <el-option label="正常" :value="1"></el-option>
                <el-option label="异常" :value="2"></el-option>
              </el-select>
            </el-col>
            <el-col :span="12">
              <div class="title">错误信息</div>
              <el-input v-model="query.errorMsg" placeholder="请输入操作错误信息" @keyup.enter.native="handleSearch"/>
            </el-col>
          </el-row>
          <el-row class="box">
            <el-col :span="6">
              <div class="title">请求标题</div>
              <el-input v-model="query.title" placeholder="请输入请求标题" @keyup.enter.native="handleSearch"/>
            </el-col>
            <el-col :span="12">
              <div class="title">操作时间</div>
               <!-- <el-date-picker
                v-model="query.czTime"
                type="datetimerange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期">
              </el-date-picker> -->
              <el-date-picker
                v-model="query.czTime"
                type="datetimerange"
                format="yyyy/MM/dd HH:mm:ss"
                value-format="yyyy-MM-dd HH:mm:ss"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                :default-time="['00:00:00', '23:59:59']">
              </el-date-picker>
            </el-col>
          </el-row>
        </div>
        <div class="search-btn-box">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn :total="query.total" @search="handleSearch" @reset="handleReset" />
            </el-col>
          </el-row>
        </div>
      </div>
      <div class="top-btn font-size-lg">
        <span class="bold">数据列表</span>
      </div>
      <div class="table-box">
        <yl-table
          border
          :show-header="true"
          :list="dataList"
          :total="query.total"
          :page.sync="query.current"
          :limit.sync="query.size"
          :loading="loading"
          @getList="getList"
        >
          <el-table-column label="系统标识" min-width="200" align="left" prop="systemId"> </el-table-column>
          <el-table-column label="业务类型" min-width="80" align="center" prop="businessType"> </el-table-column>
          <el-table-column label="请求方法" min-width="70" align="center" prop="requestMethod"> </el-table-column>
          <el-table-column label="请求标题" min-width="139" align="left" prop="title"> </el-table-column>
          <el-table-column label="请求URL" min-width="400" align="left" prop="requestUrl"> </el-table-column>
          <el-table-column label="操作状态" min-width="70" align="center">
            <template slot-scope="{ row }">
              <span :class="[row.status === 1 ? 'col-down' : 'col-up']">{{ row.status | logStatus }}</span>
            </template>
          </el-table-column>
          <el-table-column label="请求数据" min-width="370" align="left" prop="requestData">
            <template slot-scope="{ row }">
              <el-tooltip
                class="item"
                effect="dark"
                :content="row.requestData"
                popper-class="longLogTooltip"
                placement="top"
              >
                <span v-if="row.requestData.length > 50">{{ row.requestData.slice(0, 50) + "..." }}</span>
                <span v-else>{{ row.requestData || "- -" }}</span>
              </el-tooltip>
            </template>
          </el-table-column>
          <el-table-column label="响应结果" min-width="380" align="left" prop="responseData">
            <template slot-scope="{ row }">
              <el-tooltip
                class="item"
                effect="dark"
                :content="row.responseData"
                popper-class="longLogTooltip"
                placement="top"
              >
                <span v-if="row.responseData.length > 50">{{ row.responseData.slice(0, 50) + "..." }}</span>
                <span v-else>{{ row.responseData || "- -" }}</span>
              </el-tooltip>
            </template>
          </el-table-column>
          <el-table-column label="执行方法" min-width="370" align="left" prop="classMethod">
            <template slot-scope="{ row }">
              <el-tooltip
                class="item"
                effect="dark"
                :content="row.classMethod"
                popper-class="longLogTooltip"
                placement="top"
              >
                <span v-if="row.classMethod.length > 50">{{ row.classMethod.slice(0, 50) + "..." }}</span>
                <span v-else>{{ row.classMethod || "- -" }}</span>
              </el-tooltip>
            </template>
          </el-table-column>
          <el-table-column label="请求ID" min-width="270" align="center" prop="requestId">
            <template slot-scope="{ row }">
              <span>{{ row.requestId ? row.requestId : "- -" }}</span>
            </template>
          </el-table-column>
          <el-table-column label="执行时间(毫秒)" min-width="110" align="center" prop="consumeTime"> </el-table-column>
          <el-table-column label="操作错误信息" min-width="370" align="center" prop="errorMsg">
            <template slot-scope="{ row }">
              <el-tooltip
                class="item"
                effect="dark"
                :content="row.errorMsg"
                popper-class="longLogTooltip"
                placement="top"
              >
                <span v-if="row.errorMsg.length > 50">{{ row.errorMsg.slice(0, 50) + "..." }}</span>
                <span v-else>{{ row.errorMsg || "- -" }}</span>
              </el-tooltip>
            </template>
          </el-table-column>
          <el-table-column label="操作时间" min-width="155" align="center" prop="opTime">
            <template slot-scope="{ row }">
              <span>{{ row.opTime | formatDate }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作人ID" min-width="80" align="center" prop="operId"> </el-table-column>
          <el-table-column label="操作人名称" min-width="100" align="center" prop="operName">
            <template slot-scope="{ row }">
              <span>{{ row.operName ? row.operName : "- -" }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作人IP" min-width="120" align="center" prop="operIp"> </el-table-column>
        </yl-table>
      </div>
    </div>
  </div>
</template>

<script>
import { getSystemLogList } from '@/subject/admin/api/log'
import { logBusinessType, logSystemType } from '@/subject/admin/utils/busi'
import { formatDate } from '@/subject/admin/utils'
export default {
  name: 'LogManage',
  components: {},
  computed: {
    // 业务类型
    queryLogBusinessType() {
      return logBusinessType()
    },
    // 系统标识
    queryLogSystemType() {
      return logSystemType()
    }
  },
  filters: {
    logStatus(e) {
      return parseInt(e) === 1 ? '正常' : '异常'
    }
  },
  data() {
    return {
      query: {
        //  页码
        current: 1,
        //  每页记录数
        size: 10,
        //  操作状态
        status: 0,
        //  请求方法
        requestMethod: '',
        //  请求标题
        title: '',
        //  业务类型
        businessType: '',
        // 系统标识
        systemId: '',
        //  错误信息
        errorMsg: '',
        //  请求数据
        requestData: '',
        //  响应数据
        responseData: '',
        requestId: '',
        requestUrl: '',
        operId: '',
        czTime: []
      },
      // 列表
      dataList: [],
      loading: false
    }
  },
  activated() {
    this.getList()
  },
  methods: {
    async getList() {
      this.loading = true
      let query = this.query
      let data = await getSystemLogList(
        query.current,
        query.size,
        query.status,
        // query.requestMethod,
        query.title,
        // query.businessType,
        query.systemId,
        query.errorMsg,
        query.requestId,
        query.requestUrl,
        query.operId,
        query.czTime && query.czTime.length > 0 ? formatDate(query.czTime[0], 'yyyy-MM-dd hh:mm:ss') : '',
        query.czTime && query.czTime.length > 0 ? formatDate(query.czTime[1], 'yyyy-MM-dd hh:mm:ss') : ''
      )
      this.loading = false
      if (data) {
        this.dataList = data.records
        query.total = data.total
      }
    },
    handleSearch() {
      this.query.current = 1
      this.getList()
    },
    handleReset() {
      this.query = {
        current: 1,
        size: 10,
        status: 0,
        // requestMethod: "",
        title: '',
        // businessType: "",
        systemId: '',
        errorMsg: '',
        requestData: '',
        responseData: '',
        requestId: '',
        requestUrl: '',
        operId: '',
        czTime: []
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
<style lang="scss">
.longLogTooltip {
  max-width: 50%;
}
</style>
