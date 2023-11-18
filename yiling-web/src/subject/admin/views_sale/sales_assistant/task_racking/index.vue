<template>
  <div class="app-container">
    <!-- 内容区域 -->
    <div class="app-container-content has-bottom-bar">
      <!-- 顶部 -->
      <div class="task-racking-top">
        <el-row class="box" :gutter="8">
          <el-col :span="12">
            <div class="box-bg" :style="{borderRight: topData.finishType != 10 ? '4px solid #f4f4f4' : 0}">
              <div class="flex-row-left item">
                <div class="line-view"></div>
                <span class="font-size-lg bold">{{ topData.taskName }}</span>
              </div>
              <div class="font-size-base font-title-color item-text">
                任务状态：{{ topData.taskStatus == '0' ? '未开始': ( topData.taskStatus == '1' ? '进行中' : (topData.taskStatus == '2' ? '已结束' : (topData.taskStatus == '3' ? '停用' : '')) ) }}
              </div>
              <div class="font-size-base font-title-color item-text">
                开始及结束时间：{{ topData.startTime | formatDate('yyyy-MM-dd') }} 至 {{ topData.endTime | formatDate('yyyy-MM-dd') }}
              </div>
              <div class="font-size-base font-title-color item-text" v-if="topData.finishType != 10">
                销售商品：<span style="width:150pxs">{{ topData.taskGoodsList && topData.taskGoodsList.length }}个品种</span>
                投放区域：<span>{{ topData.taskArea }}</span>
              </div>
              <div class="font-size-base font-title-color item-text">
                任务设置具体条件：
                <!-- 阶梯条件下 -->
                <div v-if="ladderType == '1'">
                  <div v-if="topData.commRuleVOList && topData.commRuleVOList.length>0">
                    <div v-for="(item,index) in topData.commRuleVOList" :key="index">
                      阶梯{{ index+1 }}：销售额{{ item.commissionCondition }} 元，每盒给予 {{ item.commission }}元
                    </div>
                  </div>
                </div>
                <!-- 非阶梯条件下 -->
                <div v-if="ladderType == '2'" style="display: inline-block;">
                  总计销售 {{ noladderNum }} 盒
                </div>
              </div>
            </div>
          </el-col>
          <el-col :span="12" style="text-align: conter;height:100%" v-if="topData.finishType != 10">
            <div class="box-bg">
              <div class="box-bg2">
                <el-row class="box" :gutter="16">
                  <el-col :span="12">
                    <div class="box-bg-table box-bg-table-col1">
                     <img src="@/subject/admin/assets/chengjieNum.png" alt="">
                     <div class="box-bg-table-conter">
                       <p>{{ topRightData.takeCount }}</p>
                       <span>承接次数</span>
                     </div>
                    </div>
                  </el-col>
                  <el-col :span="12" v-if="ladderType != '1'">
                    <div class="box-bg-table box-bg-table-col2">
                      <img src="@/subject/admin/assets/yiwanchengNum.png" alt="">
                      <div class="box-bg-table-conter">
                       <p>{{ topRightData.finishCount }}</p>
                       <span>任务完成次数</span>
                      </div>
                    </div>
                  </el-col>
                </el-row>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
      <!-- 中部搜索 -->
      <div class="common-box">
        <div class="search-box" >
          <el-row class="box">
            <el-col :span="8">
              <div class="title">承接时间</div>
              <el-date-picker
                v-model="query.cjrTime"
                type="daterange"
                format="yyyy/MM/dd"
                value-format="yyyy-MM-dd"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期">
              </el-date-picker>
            </el-col>
            <el-col :span="8">
              <div class="title">承接人姓名</div>
              <el-input v-model="query.name" @keyup.enter.native="searchEnter" placeholder="请输入承接人姓名" />
            </el-col>
            <el-col :span="8">
              <div class="title">承接人手机号</div>
              <el-input v-model="query.mobile" @keyup.enter.native="searchEnter" @input="e => (query.mobile = checkInput(e))" placeholder="请输入承接人手机号" />
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
      <!-- 下部表格 -->
      <div class="task-racking-bottom">
        <div class="flex-row-left item flex-row-title">
          <div class="line-view"></div>
          <span class="font-size-lg bold">承接人员明细及进度</span>
        </div>
        <yl-table
          border
          stripe
          show-header
          :list="dataList"
          :total="query.total"
          :page.sync="query.page"
          :limit.sync="query.limit"
          :loading="loading"
          :horizontal-border="false"
          @getList="getTableApi">
          <el-table-column label="承接人" min-width="80" align="center" prop="userName">
          </el-table-column>
          <el-table-column label="承接人联系方式" min-width="150" align="center" prop="mobile">
          </el-table-column>
          <el-table-column label="承接时间" min-width="150" align="center">
            <template slot-scope="{ row }">
              {{ row.createdTime | formatDate }}
            </template>
          </el-table-column>
          <el-table-column label="承接任务进度" min-width="600" align="center">
            <template slot-scope="{ row, $index }">
              <div class="table-view" :key="$index">
                <div class="table-row-progress" v-if="!loading">
                  <progres 
                    style="margin:0 auto" 
                    :ladder-type.sync="ladderType" 
                    :data-percent.sync="row.percent" 
                    :data-list.sync="row.percentList">
                  </progres>
                </div>
                <div class="table-view-jd">
                  实际完成:  {{ row.finishValue }} {{ ladderType == '2' ? '盒' : '' }}
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="操作" min-width="80" align="center">
            <template slot-scope="{ row }">
              <yl-button type="text" @click="seeClick(row)">查看详情</yl-button>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
    <div class="flex-row-center bottom-view">
      <yl-button type="primary" @click="$router.go(-1)">返回</yl-button>
    </div>
  </div>
</template>

<script>
import { getDetailById, getTaskTrace, queryTaskUserPage } from '@/subject/admin/api/views_sale/task_administration'
import { taskFinishType } from '@/subject/admin/utils/busi'
import progres from './progres'
export default {
  components: {
    progres
  },
  computed: {
    // 任务类型
    companyTaskFinishType() {
      return taskFinishType()
    }
  },
  data() {
    return {
      query: {
        total: 0,
        page: 1,
        limit: 10,
        cjrTime: [],
        name: '',
        mobile: ''
      },
      //顶部左边信息
      topData: {
        finishType: 10
      }, 
      //顶部右侧信息
      topRightData: {}, 
      dataList: [],
      loading: false,
      id: '',
      //阶梯状态 1 阶梯 2非阶梯
      ladderType: '1', 
      //非阶梯下 的交易量 总量
      noladderNum: '' 
    }
  },
  mounted() {
    let query = this.$route.params;
    if (query.id) {
      this.id = query.id;
      //获取顶部左上数据
      this.getList(); 
      //获取顶部右上 数据
      this.getTaskTraceApi(); 
    }
  },
  methods: {
    // Enter
    searchEnter(e) {
      const keyCode = window.event ? e.keyCode : e.which;
      if (keyCode === 13) {
        this.getTableApi()
      }
    },
    // 获取数据
    async getList() {
      let data = await getDetailById(this.id);
      if (data) {
        this.topData = {
          ...data
        }
        //查找是 阶梯条件还是 非阶梯条件
        for (let i = 0; i < data.finishRuleVOList.length; i ++) {
          if (data.finishRuleVOList[i].ruleKey == 'STEP_CONDITION') {
            this.ladderType = data.finishRuleVOList[i].ruleValue
          }
          if (data.finishRuleVOList[i].ruleKey == 'SALE_CONDITION') {
            this.noladderNum = data.finishRuleVOList[i].ruleValue
          }
        }
       
      }
      //获取底部表格数据
      this.getTableApi(); 
    },
    // 获取顶部右边 数据
    async getTaskTraceApi() {
      let data = await getTaskTrace(this.id);
      if (data) {
        this.topRightData = {
          ...data
        }
      }
    },
    // 获取底部表格数据
    async getTableApi() {
      this.loading = true;
      let query = this.query;
      let data = await queryTaskUserPage(
        this.id,
        query.page,
        query.cjrTime && query.cjrTime.length > 1 ? query.cjrTime[1] : '',
        query.limit,
        query.cjrTime && query.cjrTime.length > 0 ? query.cjrTime[0] : '',
        query.name,
        query.mobile
      )
      if (data !== undefined) {
        this.dataList = data.records;
        this.query.total = data.total;
      }
      this.loading = false;
    },
    // 查询
    handleSearch() {
      this.query.page = 1;
      this.getTableApi();
    },
    // 清空查询
    handleReset() {
      this.query = {
        total: 0,
        page: 1,
        limit: 10,
        cjrTime: [],
        name: '',
        mobile: ''
      };
      this.getTableApi();
    },
    // 查看详情
    seeClick(row) {
      if (this.topData.finishType == 10) {
        this.$router.push({
          name: 'DepartmentDetails',
          params: {
            userTaskId: row.userTaskId
          }
        });
      } else {
        this.$router.push({
          name: 'PullPeople',
          params: {
            userTaskId: row.userTaskId,
            id: this.id
          }
        });
      }
      
    },
    checkInput(val) {
      val = val.replace(/[^0-9]/gi, '')
      return val
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
