<template>
  <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <div class="table-view">
        <p class="title">
          <span class="group"></span>
          {{ dataObj.title }}
          <span class="sign" :class="dataObj.replyFlag == 2 ? 'span1' : 'span2'">{{ dataObj.replyFlag == 2 ? '已回复' : '未回复' }}</span>
        </p>
        <p class="conter-text">{{ dataObj.content }}</p>
        <div class="conter-img">
          <ul>
            <li v-for="(item,index) in dataObj.pictureList" :key="index" @click="seeClick(item, dataObj.pictureList)">
              <img :src="item.fileUrl" alt="">
            </li>
          </ul>
        </div>
        <div class="conter-commodity" v-if="dataObj.name !=''">
          {{ dataObj.name }} / {{ dataObj.sellSpecifications }}
        </div>
        <div class="questioner">
          <div class="questioner-right">
            <p>{{ dataObj.fromUserName }}</p>
            <p>
              <span>创建时间:</span> {{ dataObj.createTime | formatDate }}
            </p>
          </div>
          <div class="questioner-button" v-if="dataObj.showReplyButtonFlag == 1">
            <yl-button type="primary" plain @click="replyClick(dataObj)" class="bottom-margin">回复</yl-button>
          </div>
        </div>
        <!-- 回复内容 -->
        <div class="reply-content" v-if="dataObj.replyList && dataObj.replyList.length > 0">
          <div class="title titlt-margin">
            <span class="group"></span>
            回复
          </div>
          <div class="reply-bg" v-for="(item,index) in dataObj.replyList" :key="index">
            <div class="questioner">
              <div class="questioner-right">
                <p>{{ item.createUserName }}</p>
                <p>
                  <span>回复时间:</span> {{ item.createTime | formatDate }}
                </p>
              </div>
            </div>
            <div class="reply-text" v-dompurify-html="item.replyContent"></div>
            <div class="reply-data" v-if="item.replyDocumentList && item.replyDocumentList.length > 0">
              <p>关联文献：</p>
              <span v-for="(item2,index2) in item.replyDocumentList" :key="index2">
                <el-tag type="info">{{ item2.documentTitle }}</el-tag>
              </span>
            </div>
            <div class="reply-data" v-if="item.replyFileList && item.replyFileList.length > 0">
              <p>附件：</p>
              <span v-for="(item3,index3) in item.replyFileList" :key="index3">
                <a :href="item3.fileUrl" target="_blank">{{ item3.fileName }}</a>
              </span>
            </div>
          </div>
        </div>
      </div>
    </div>
    <el-image-viewer v-if="showViewer" :on-close="onClose" :url-list="imageList"/>
    <div class="flex-row-center bottom-view" >
      <yl-button type="primary" plain @click="$router.go(-1)">返回</yl-button>
    </div>
  </div>
</template>

<script>
import { delegateDetail } from '@/subject/admin/api/content_api/question_handling'
import ElImageViewer from 'element-ui/packages/image/src/image-viewer';
export default {
  name: 'CommunityDetails',
  components: {
    ElImageViewer
  },
  data() {
    return {
      dataObj: {},
      showViewer: false,
      imageList: []
    }
  },
  mounted() {
    let query = this.$route.params;
    if (query && query.id != '') {
      this.getList(query.id);
    }
  },
  methods: {
    async getList(val) {
      let data = await delegateDetail(val)
      if (data) {
        this.dataObj = data;
      }
    },
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
    onClose() {
      this.imageList = [];
      this.showViewer = false;
    },
    // 点击 回复
    replyClick(row) {
      this.$router.push({
        name: 'Reply',
        params: {
          id: row.id
        }
      });
    }
  }
}
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>