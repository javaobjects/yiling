<template>
  <div class="app-container">
    <!-- 内容区域 -->
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="12">
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
            <el-col :span="8">
              <div class="title">回复状态</div>
              <el-select class="select-width" v-model="query.replyFlag" placeholder="请选择">
                <el-option label="全部" :value="0"></el-option>
                <el-option label="未回复" :value="1"></el-option>
                <el-option label="已回复" :value="2"></el-option>
              </el-select>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn
                :total="query.total" 
                @search="handleSearch" 
                @reset="handleReset" />
            </el-col>
          </el-row>
        </div>
      </div>
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
                <p class="title" @click="detailsClick(row)">
                 {{ row.title }}
                 <span :class="row.replyFlag == 2 ? 'span1' : 'span2'">{{ row.replyFlag == 2 ? '已回复' : '未回复' }}</span>
                </p>
                <p class="conter-text">{{ row.content }}</p>
                <div class="conter-img">
                  <ul>
                    <li v-for="(item,index) in row.fileList" :key="index" @click="seeClick(item, row.fileList)">
                      <img :src="item.fileUrl" alt="">
                    </li>
                  </ul>
                </div>
                <div class="conter-commodity" v-if="row.name !=''">
                  {{ row.name }} / {{ row.sellSpecifications }}
                </div>
                <div class="questioner">
                  <div class="questioner-right">
                    <p>{{ row.fromUserName }}</p>
                    <p>
                      <span>创建时间:</span> {{ row.createTime | formatDate }}
                      <span class="pad-left">问题回复人:</span> {{ row.toUserName }}
                    </p>
                  </div>
                  <div class="questioner-button" v-if="row.showReplyButtonFlag == 1">
                    <yl-button type="primary" plain @click="replyClick(row)" class="bottom-margin">回复</yl-button>
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
import { communityList } from '@/subject/admin/api/content_api/question_handling';
import ElImageViewer from 'element-ui/packages/image/src/image-viewer';
export default {
  name: 'CommunityList',
  components: {
    ElImageViewer
  },
  data() {
    return {
      query: {
        establishTime: [],
        replyFlag: 0,
        total: 0,
        page: 1,
        limit: 10
      },
      dataList: [],
      loading: false,
      showViewer: false,
      imageList: []
    }
  },
  activated() {
    this.getList();
  },
  methods: {
    async getList() {
      this.loading = true;
      let query = this.query;
      let data = await communityList(
        query.establishTime && query.establishTime.length > 0 ? query.establishTime[0] : '',
        query.establishTime && query.establishTime.length > 1 ? query.establishTime[1] : '',
        query.replyFlag,
        query.page,
        query.limit
      )
      if (data) {
        this.dataList = data.records;
        this.query.total = data.total;
      }
      this.loading = false;
    },
    // 点击标题去详情界面
    detailsClick(row) {
      this.$router.push({
        name: 'CommunityDetails',
        params: {
          id: row.id
        }
      });
    },
    // 点击 回复
    replyClick(row) {
      this.$router.push({
        name: 'Reply',
        params: {
          id: row.id
        }
      });
    },
    // 点击图片
    seeClick(val, dataList) {
      if (val.fileUrl != '') {
        this.imageList.push(val.fileUrl)
      }
      if (dataList && dataList.length > 1) {
        for (let i = 0; i < dataList.length; i ++) {
          if (val.fileUrl != dataList[i].fileUrl) {
            this.imageList.push(
              dataList[i].fileUrl
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
    },
    // 搜索
    handleSearch() {
      this.query.page = 1;
      this.getList();
    },
    // 重置
    handleReset() {
      this.query = {
        establishTime: [],
        replyFlag: 0,
        total: 0,
        page: 1,
        limit: 10
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>