<template>
  <div class="app-container">
    <!-- 内容区域 -->
    <div class="app-container-content has-bottom-bar">
      <!-- 顶部 -->
      <div class="common-box mar-b-16 order-total">
        <div class="box border-1px-r">
          <div class="flex-row-center">
            <svg-icon class="svg-icon" icon-class="last_order"></svg-icon>
            <span>任务总量(个)<span>包含所有的任务状态</span></span>
          </div>
          <div class="title">
            {{ topData.count }}
          </div>
        </div>
        <div class="box border-1px-r">
          <div class="flex-row-center">
            <svg-icon class="svg-icon" icon-class="today_order"></svg-icon>
            <span>平台任务(个)</span>
          </div>
          <div class="title">
            {{ topData.platformCount }}
          </div>
        </div>
        <div class="box">
          <div class="flex-row-center">
            <svg-icon class="svg-icon" icon-class="year_order"></svg-icon>
            <span>企业任务(个)</span>
          </div>
          <div class="title">
            {{ topData.enterpriseCount }}
          </div>
        </div>
      </div>
      <!-- 中部 搜索条件 -->
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">任务名称</div>
              <el-input v-model="query.taskName" @keyup.enter.native="searchEnter" placeholder="请输入任务名称" />
            </el-col>
            <el-col :span="8">
              <div class="title">创建时间</div>
              <el-date-picker
                v-model="query.cjTime"
                type="daterange"
                format="yyyy/MM/dd"
                value-format="yyyy-MM-dd"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期">
              </el-date-picker>
            </el-col>
            <el-col :span="8">
              <div class="title">任务开始时间</div>
              <el-date-picker
                v-model="query.rwksTime"
                type="daterange"
                format="yyyy/MM/dd"
                value-format="yyyy-MM-dd"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期">
              </el-date-picker>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">任务结束时间</div>
              <el-date-picker
                v-model="query.rwjsTime"
                type="daterange"
                format="yyyy/MM/dd"
                value-format="yyyy-MM-dd"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期">
              </el-date-picker>
            </el-col>
            <el-col :span="8">
              <div class="title">任务类型</div>
              <el-select class="select-width" v-model="query.finishType" placeholder="请选择">
                <el-option :key="-1" label="全部" :value="-1"></el-option>
                <el-option v-for="item in companyTaskFinishType" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="8">
              <div class="title">任务状态</div>
              <el-select class="select-width" v-model="query.taskStatus" placeholder="请选择">
                <el-option :key="-1" label="全部" :value="-1"></el-option>
                <el-option v-for="item in companyTaskStatus" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
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
      <!-- 底部列表 -->
      <div class="mar-t-8 bottom-content-view" style="padding-bottom: 30px;background: #FFFFFF;">
        <yl-table
          :list="dataList"
          :total="query.total"
          :page.sync="query.page"
          :limit.sync="query.limit"
          :loading="loading"
          :horizontal-border="false"
          :cell-no-pad="true"
          @getList="getList">
          <el-table-column>
            <template slot-scope="{ row, $index }">
              <div class="table-view" :key="$index">
                <div class="session font-size-base flex-between">
                  <div>
                    <span>
                      任务状态：
                      <span :style="{color:row.taskStatus == '0'?'#E6A23C':( row.taskStatus == '1'? '#15AD31' : ( row.taskStatus == '2' ? '#909399':'#F56C6C'))}">
                        {{ row.taskStatus == '0' ? '未开始': ( row.taskStatus == '1' ? '进行中' : (row.taskStatus == '2' ? '已结束' : (row.taskStatus == '3' ? '停用' : '')) ) }}
                      </span>
                    </span>
                    <span class="mar-l-32">
                    </span>
                  </div>
                  <div>
                    <span style="padding-right:20px">创建人：{{ row.createdBy }}</span>
                    <span>创建时间：{{ row.createdTime | formatDate }}</span>
                  </div>
                </div>
                <div class="content flex-row-left">
                  <div class="content-left">
                    <div class="content-left-title">{{ row.taskName || '- -' }}</div>
                    <div class="item" style="font-size:14px;font-weight: normal;">
                      <span class="font-title-color">任务类型：</span>
                      {{ row.finishType | dictLabel(companyTaskFinishType) }}
                    </div>
                  </div>
                  <div class="content-center font-size-base font-important-color">
                    <div class="item"></div>
                    <div class="item">
                      <span class="font-title-color">起止时间：</span>
                      {{ row.startTime | formatDate('yyyy-MM-dd') }} - {{ row.endTime | formatDate('yyyy-MM-dd') }} 
                    </div>
                  </div>
                  <div class="content-center-1 font-size-base font-important-color flex1">
                  </div>
                  <div class="content-right flex-column-center table-button">
                    <yl-button type="text" @click="seeClick(row)">查看详情</yl-button>
                    <yl-button @click="deleteClick(row)" type="text" v-if="row.taskStatus == 0">删除</yl-button>
                    <yl-button @click="stopClick(row)" type="text" v-if="row.taskStatus == 1 && row.finishType !== 10">停用</yl-button>
                    <yl-button @click="modifyClick(row)" type="text" v-if="row.taskStatus == 0">修改</yl-button>
                    <yl-button @click="modifyTypeClick(row)" type="text" v-if="row.finishType == 2 && row.taskStatus == 1">修改</yl-button>
                    <yl-button style="margin:0" @click="trackClick(row)" type="text" v-if="row.taskStatus != 0">任务追踪</yl-button>
                  </div>
                </div>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
    <div class="flex-row-center bottom-view">
      <yl-button type="primary" @click="establishClick">创建</yl-button>
    </div>
  </div>
</template>

<script>
import { getTaskCount, queryTaskListPage, stopTask, deleteTask } from '@/subject/admin/api/views_sale/task_administration'
import { taskFinishType, taskStatus } from '@/subject/admin/utils/busi'
export default {
  name: 'TaskAdministration',
  components: {
  },
  computed: {
    // 任务类型
    companyTaskFinishType() {
      return taskFinishType()
    },
    // 任务状态
    companyTaskStatus() {
      return taskStatus()
    }
  },
  data() {
    return {
      topData: {
        count: '',
        enterpriseCount: '',
        platformCount: ''
      },
      query: {
        //任务名称
        taskName: '',
        //创建时间
        cjTime: [], 
        //任务开始时间
        rwksTime: [],
        //任务结束时间
        rwjsTime: [], 
        //任务类型
        finishType: -1, 
        //任务状态 0未开始1进行中2已结束3停用
        taskStatus: -1, 
        total: 0,
        //当前页码
        page: 1,
        //分页数量
        limit: 10,
        taskType: 0
      },
      //0 平台任务 1 企业任务
      taskType: '0', 
      dataList: [],
      loading: false
    }
  },
  activated() {
    //获取顶部信息
    this.taskTopApi(); 
    //获取数据
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
    // 停用
    stopClick(row) {
      this.$confirm(`因任务涉及到分佣等多个内容，停用 "${row.taskName}" 后任务将停止进行计算，用户端任务将销售并会收到APP的相关推送。停止前产生的订单或佣金按照任务设置进行发放 ！`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      .then( async() => {
        //确定
        this.$common.showLoad();
        let data = await stopTask(
          row.id
        )
        this.$common.hideLoad();
        if (data !== undefined) {
          this.$common.n_success('停用成功!');
          this.getList();
        }
      })
      .catch(() => {
        //取消
      });
    },
    // 任务追踪
    trackClick(row) {
      // 交易额 交易量
      if (row.finishType == 1 || row.finishType == 2 || row.finishType == 10) { 
        this.$router.push({
          name: 'TaskRacking',
          params: {
            id: row.id
          }
        });
        // 新人 新户
      } else if (row.finishType == 3 || row.finishType == 7) { 
        this.$router.push({
          name: 'LahuLapeople',
          params: {
            id: row.id
          }
        });
        //会员
      } else if (row.finishType == 8) { 
        this.$router.push({
          name: 'MemberInformation',
          params: {
            id: row.id
          }
        });
      }
    },
    //获取顶部数据
    async taskTopApi() {
      let data = await getTaskCount();
      if (data) {
        this.topData = data;
      }
    },
    // 获取数据
    async getList() {
      this.loading = true
      let query = this.query;
      let data = await queryTaskListPage(
        query.taskName,
        query.cjTime && query.cjTime.length > 0 ? query.cjTime[0] : '',
        query.cjTime && query.cjTime.length > 1 ? query.cjTime[1] : '',
        query.rwksTime && query.rwksTime.length > 0 ? query.rwksTime[0] : '',
        query.rwksTime && query.rwksTime.length > 1 ? query.rwksTime[1] : '',
        query.rwjsTime && query.rwjsTime.length > 0 ? query.rwjsTime[0] : '',
        query.rwjsTime && query.rwjsTime.length > 1 ? query.rwjsTime[1] : '',
        query.finishType,
        query.taskStatus,
        query.page,
        query.limit,
        query.taskType
      )
      if (data) {
        this.dataList = data.records;
        this.query.total = data.total;
      }
      this.loading = false;
    },
    // 搜索
    handleSearch() {
      this.query.page = 1;
      this.getList()
    },
    // 重置
    handleReset() {
      this.query = {
        //任务名称
        taskName: '',
        //创建时间
        cjTime: [], 
        //任务开始时间
        rwksTime: [],
        //任务结束时间
        rwjsTime: [], 
        //任务类型
        finishType: -1, 
        //任务状态
        taskStatus: -1, 
        total: 0,
        //当前页码
        page: 1,
        //分页数量
        limit: 10,
        taskType: 0
      }
    },
    // 创建
    establishClick() {
      this.$router.push({
        name: 'Establishs',
        params: {
          type: 0,
          id: 0
        }
      })
    },
    // 查看
    seeClick(row) {
      this.$router.push({
        name: 'Establishs',
        params: {
          type: 1,
          id: row.id
        }
      })
    },
    // 修改
    modifyClick(row) {
      this.$router.push({
        name: 'Establishs',
        params: {
          type: 2,
          id: row.id
        }
      })
    },
    // 修改 只能添加商品
    modifyTypeClick(row) {
      this.$router.push({
        name: 'Establishs',
        params: {
          type: 3,
          id: row.id
        }
      })
    },
     // 删除
    deleteClick(row) {
      this.$confirm(`确认删除 ${row.taskName} 任务！`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      .then( async() => {
        //确定
        this.$common.showLoad();
        let data = await deleteTask(
          row.id
        )
        this.$common.hideLoad();
        if (data !== undefined) {
          this.$common.n_success('删除成功!');
          this.getList()
        }
      })
      .catch(() => {
        //取消
      })
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>