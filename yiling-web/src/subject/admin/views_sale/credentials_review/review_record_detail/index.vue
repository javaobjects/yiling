<template>
  <div class="app-container">
    <div class="app-container-content ">
      <div class="common-box">
        <div class="search-box">
          <div class="header-bar">
            <div class="sign"></div>
            审核详情
          </div>
          <el-row class="infobox">
            <el-col :span="6">
              <div class="userinfo">
                <span class="title">姓名：</span>
                <span class="infoitem">{{ userInfo.name }}</span>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="userinfo">
                <span class="title">证件号：</span>
                <span class="infoitem">{{ userInfo.idNumber }}</span>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="userinfo">
                <span class="title">状态：</span>
                <span :class="['infoitem', userInfo.auditStatus === 2 ? 'col-down' : '', userInfo.auditStatus === 3 ? 'col-up' : '']">{{
                  userInfo.auditStatus | reviewStatus
                }}</span>
              </div>
            </el-col>
          </el-row>
          <el-row class="infobox">
            <el-col :span="6">
              <div class="userinfo">
                <span class="title">审核人：</span>
                <span class="infoitem">{{ userInfo.auditUserName || "- -" }}</span>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="userinfo">
                <span class="title">审核时间：</span>
                <span class="infoitem">{{ userInfo.auditTime | formatDate }}</span>
              </div>
            </el-col>
            <el-col :span="12" v-if="userInfo.auditStatus === 3">
              <div class="userinfo">
                <span class="title">驳回原因：</span>
                <span class="infoitem">{{ userInfo.auditRejectReason }}</span>
              </div>
            </el-col>
          </el-row>
          <div class="userimg">
            <div class="imgbox">
              <el-image class="img" :src="userInfo.idCardFrontPhotoUrl" :preview-src-list="[userInfo.idCardFrontPhotoUrl]"> </el-image>
              <span>手持证件照正面</span>
            </div>
            <div class="imgbox">
              <el-image class="img" :src="userInfo.idCardBackPhotoUrl" :preview-src-list="[userInfo.idCardBackPhotoUrl]"> </el-image>
              <span>手持证件照反面</span>
            </div>
          </div>
        </div>
      </div>
      <div class="flex-row-center bottom-view">
        <yl-button type="primary" @click="$router.go(-1)">返回</yl-button>
      </div>
    </div>
  </div>
</template>

<script>
import { reviewDetail } from '@/subject/admin/api/views_sale/credentials_review'

export default {
  name: 'ReviewRecordDetail',
  components: {},
  filters: {
    reviewStatus(e) {
      let res
      switch (e) {
        case 0:
          res = '全部'
          break;
        case 1:
          res = '待审核'
          break;
        case 2:
          res = '审核通过'
          break;
        case 3:
          res = '审核驳回'
          break;
        default:
          res = '- -'
          break;
      }
      return res
    }
  },
  computed: {},
  mounted() {
    this.id = this.$route.params.id
    if (this.id) {
      this.getDetail()
    }
  },
  data() {
    return {
      id: '',
      userInfo: {}
    }
  },
  methods: {
    // 获取审核详情
    async getDetail() {
      this.$common.showLoad()
      let data = await reviewDetail(this.id)
      this.$common.hideLoad()
      if (data) {
        this.userInfo = data
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
