<template>
  <div class="app-container">
    <!-- 内容区域 -->
    <div class="app-container-content">
      <!-- 中部 搜索条件 -->
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">量表名称</div>
              <el-input v-model="query.healthEvaluateName" @keyup.enter.native="searchEnter" placeholder="请输入量表名称" />
            </el-col>
            <el-col :span="8">
              <div class="title">量表类型</div>
              <el-select class="select-width" v-model="query.healthEvaluateType" placeholder="请选择">
                <el-option label="全部" :value="''"></el-option>
                <el-option
                  v-for="item in healthEvaluate"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="8">
              <div class="title">发布状态</div>
              <el-select class="select-width" v-model="query.publishFlag" placeholder="请选择">
                <el-option label="全部" :value="''"></el-option>
                <el-option label="未发布" :value="0"></el-option>
                <el-option label="已发布" :value="1"></el-option>
              </el-select>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">创建时间</div>
              <el-date-picker
                type="daterange"
                format="yyyy/MM/dd"
                value-format="yyyy-MM-dd"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                v-model="query.establishTime">
              </el-date-picker>
            </el-col>
            <el-col :span="8">
              <div class="title">最近修改时间</div>
              <el-date-picker
                type="daterange"
                format="yyyy/MM/dd"
                value-format="yyyy-MM-dd"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                v-model="query.updateTime">
              </el-date-picker>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn
                :total="query.total"
                @search="handleSearch"
                @reset="handleReset"/>
            </el-col>
          </el-row>
        </div>
      </div>
      <div class="down-box clearfix">
        <div class="btn">
          <yl-button type="primary" @click="addGaugeClick">创建新量表</yl-button>
        </div>
      </div>
       <!-- 底部列表 -->
      <div class="mar-t-8 pad-b-10 order-table-view">
        <yl-table
          border
          show-header
          :list="dataList"
          :total="query.total"
          :page.sync="query.page"
          :limit.sync="query.limit"
          :loading="loading"
          @getList="getList">
          <el-table-column align="center" min-width="150" label="量表名称" prop="healthEvaluateName"></el-table-column>
          <el-table-column align="center" min-width="150" label="量表类型">
            <template slot-scope="{ row }">
              <div>{{ row.healthEvaluateType | dictLabel(healthEvaluate) }}</div>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="80" label="参与测试人数" prop="userCount"></el-table-column>
          <el-table-column align="center" min-width="80" label="完成测试人数" prop="finishCount"></el-table-column>
          <el-table-column align="center" min-width="80" label="完成测试人次" prop="finishDistinctCount"></el-table-column>
          <el-table-column align="center" min-width="120" label="应用平台">
            <template slot-scope="{ row }">
              <div v-for="(item,index) in row.lineIdList" :key="index">
                {{ item | dictLabel(businessData) }}
              </div>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="100" label="创建人" prop="createUserName"></el-table-column>
          <el-table-column align="center" min-width="110" label="创建时间">
            <template slot-scope="{ row }">
              <div>{{ row.createTime | formatDate }}</div>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="110" label="最近修改时间">
            <template slot-scope="{ row }">
              <div>{{ row.updateTime | formatDate }}</div>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="80" label="状态">
            <template slot-scope="{ row }">
              <div>{{ row.publishFlag == 1 ? '已发布' : '未发布' }}</div>
            </template>
          </el-table-column>
          <el-table-column fixed="right" align="center" label="操作" min-width="180">
            <template slot-scope="{ row }">
              <yl-button type="text" @click="gaugeClick(row.id)">量表设置</yl-button>
              <yl-button type="text" @click="topicClick(row.id)">题目设置</yl-button>
              <yl-button type="text" @click="resultSettingsClick(row)">结果设置</yl-button>
              <yl-button type="text" @click="marketingClick(row)">营销设置</yl-button>
              <yl-button type="text" @click="releaseClick(row)">{{ row.publishFlag == 1 ? '取消发布' : '发布' }}</yl-button>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </div>
</template>
    
<script>
import { queryEvaluatePage, publishHealthEvaluate } from '@/subject/admin/api/content_api/evaluation'
import { hmcHealthEvaluate } from '@/subject/admin/busi/content/article_video'
import { displayLine } from '@/subject/admin/utils/busi'
export default {
  name: 'Gauge',
  computed: {
    healthEvaluate() {
      return hmcHealthEvaluate()
    },
    businessData() {
      return displayLine()
    }
  },
  data() {
    return {
      query: {
        healthEvaluateName: '',
        healthEvaluateType: '',
        publishFlag: '',
        establishTime: [],
        updateTime: [],
        total: 0,
        page: 1,
        limit: 10
      },
      loading: false,
      dataList: []
    }
  },
  activated() {
    //获取数据
    this.getList();
  },
  methods: {
    //点击发布
    releaseClick(row) {
      this.$confirm(`确认${row.publishFlag == 1 ? '取消发布' : '发布'} ${row.healthEvaluateName } ！`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      .then( async() => {
        //确定
        this.$common.showLoad();
        let data = await publishHealthEvaluate(
          row.id,
          row.publishFlag == 1 ? 0 : 1
        )
        this.$common.hideLoad();
        if (data !== undefined) {
          this.$common.n_success('发布成功!');
          this.getList();
        }
      })
      .catch(() => {
        //取消
      });
    },
    //点击新建量表
    addGaugeClick() {
      this.$router.push({
        name: 'AddGauge',
        params: {
          id: 0
        }
      });
    },
    //点击量表设置
    gaugeClick(val) {
      this.$router.push({
        name: 'AddGauge',
        params: {
          id: val
        }
      });
    },
    //获取数据
    async getList() {
      this.loading = true;
      let query = this.query;
      let data = await queryEvaluatePage(
        query.healthEvaluateName,
        query.healthEvaluateType,
        query.publishFlag,
        query.establishTime && query.establishTime.length > 0 ? query.establishTime[0] : '',
        query.establishTime && query.establishTime.length > 1 ? query.establishTime[1] : '',
        query.updateTime && query.updateTime.length > 0 ? query.updateTime[0] : '',
        query.updateTime && query.updateTime.length > 1 ? query.updateTime[1] : '',
        query.page,
        query.limit
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
      this.getList();
    },
    // 重置
    handleReset() {
      this.query = {
        healthEvaluateName: '',
        healthEvaluateType: '',
        publishFlag: '',
        establishTime: [],
        updateTime: [],
        total: 0,
        page: 1,
        limit: 10
      }
    },
    //点击题目设置
    topicClick(val) {
      this.$router.push({
        name: 'TopicSetting',
        params: {
          id: val
        }
      });
    },
    //点击结果设置
    resultSettingsClick(row) {
      this.$router.push({
        name: 'ResultSettings',
        params: {
          title: row.healthEvaluateName,
          id: row.id
        }
      });
    },
    //点击营销设置
    marketingClick(row) {
      this.$router.push({
        name: 'MarketingSettings',
        params: {
          title: row.healthEvaluateName,
          id: row.id
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>