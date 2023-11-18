<template>
  <div class="app-container">
    <!-- 内容区域 -->
    <div class="app-container-content">
      <!-- 中部 搜索条件 -->
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">类型</div>
              <el-select class="select-width" v-model="query.source" placeholder="请选择">
                <el-option label="全部" :value="''"></el-option>
                <el-option
                  v-for="item in businessData"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="8">
              <div class="title">创建时间</div>
              <el-date-picker
                v-model="query.establishTime"
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
            <el-col :span="16">
              <yl-search-btn :total="query.total" @search="handleSearch" @reset="handleReset" />
            </el-col>
          </el-row>
        </div>
      </div>
      <!-- 底部内容 -->
      <div class="mar-t-8 order-table-view">
        <yl-table
          :list="dataList" 
          :total="query.total" 
          :page.sync="query.page" 
          :limit.sync="query.limit" 
          :loading="loading" 
          @getList="getList">
          <el-table-column>
            <template slot-scope="{ row, $index }">
              <div class="table-view" :key="$index">
                <p class="conter-text">{{ row.feedbackText }}</p>
                <div class="conter-img">
                  <ul>
                    <li v-for="(item, index) in row.feedbackPicList" :key="index" @click="seeClick(item, row.feedbackPicList)">
                      <img :src="item" alt="">
                    </li>
                  </ul>
                </div>
                <div class="questioner">
                  <div class="questioner-right">
                    <p>
                      {{ row.name }}
                      <span v-show="row.name">/</span>
                      {{ row.nickName }}
                    </p>
                    <p>
                      <span>手机号：</span> 
                      {{ row.mobile }}
                      <span class="pad-left">创建时间：</span>
                      {{ row.createTime | formatDate }}
                    </p>
                  </div>
                  <div class="questioner-button">
                    <el-tag>{{ row.source | dictLabel(businessData) }}</el-tag>
                  </div>
                </div>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
    <el-image-viewer v-if="showViewer" :on-close="onClose" :url-list="imageList"/>
  </div>
</template>

<script>
import { displayLine } from '@/subject/admin/utils/busi'
import { queryPage } from '@/subject/admin/api/content_api/feedback'
import ElImageViewer from 'element-ui/packages/image/src/image-viewer';
export default {
  name: 'FeedbackList',
  components: {
    ElImageViewer
  },
  computed: {
    businessData() {
      return displayLine()
    }
  },
  data() {
    return {
      query: {
        source: '',
        establishTime: [],
        total: 0,
        page: 1,
        limit: 10
      },
      dataList: [],
      loading: false,
      imageList: [],
      showViewer: false
    }
  },
  activated() {
    this.getList();
  },
  methods: {
    // 获取数据
    async getList() {
      this.loading = true;
      let query = this.query;
      let data = await queryPage(
        query.source,
        query.establishTime && query.establishTime.length > 0 ? query.establishTime[0] : '',
        query.establishTime && query.establishTime.length > 1 ? query.establishTime[1] : '',
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
        source: '',
        establishTime: [],
        total: 0,
        page: 1,
        limit: 10
      }
    },
    // 点击图片
    seeClick(val, dataList) {
      if (val != '') {
        this.imageList.push(val)
      }
      if (dataList && dataList.length > 1) {
        for (let i = 0; i < dataList.length; i ++) {
          if (val != dataList[i]) {
            this.imageList.push(
              dataList[i]
            )
          }
        }
      }
      this.showViewer = true;
    },
    // 关闭图片放大弹窗
    onClose() {
      this.imageList = [];
      this.showViewer = false;
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>