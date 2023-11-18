<template>
  <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <div class="common-box">
        <div class="header-bar">
          <div class="sign"></div>
          资料审核信息
        </div>
        <div class="examine-row">
          <el-row>
            <el-col :span="12">
              <div class="examine-row-conter">
                <p>审核结果：{{ dataList.auditStatus | dictLabel(auditStatusList) }}</p>
                <p>驳回原因：{{ dataList.rejectionReason }}</p>
                <p>发货单位：{{ dataList.distributorEname }}</p>
              </div>
              <div class="pic-box">
                <p>随货同行单图片:</p>
                <div class="conter-img">
                  <el-image class="pic" :src="dataList.accompanyingBillPic" :preview-src-list="[dataList.accompanyingBillPic]"></el-image>
                </div>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="examine-row-conter">
                <p>单据编号：{{ dataList.docCode }}</p>
                <p>审核时间：{{ dataList.auditTime | formatDate }}</p>
                <p>提交人：{{ dataList.createUserName || '- -' }}</p>
              </div>
            </el-col>
          </el-row>
        </div>
      </div>
      <el-image-viewer v-if="showViewer" :on-close="onClose" :url-list="imageList"/>
    </div>
    <div class="bottom-view flex-row-center">
      <yl-button type="primary" plain @click="$router.go(-1)">返回</yl-button>
    </div>
  </div>
</template>
<script>
import { getDetails } from '@/subject/admin/api/views_sale/resource_management'
import { accompanyBillAuditStatus } from '@/subject/admin/busi/sale/resource'
import ElImageViewer from 'element-ui/packages/image/src/image-viewer';

export default {
  name: 'MemberQueryDetails',
  components: {
    ElImageViewer
  },
  computed: {
    auditStatusList(){
      return accompanyBillAuditStatus()
    }
  },
  data() {
    return {
      dataList: {},
      imageList: [],
      showViewer: false
    }
  },
  mounted() {
    let params = this.$route.params
    if (params.id) {
      this.getData(params.id)
    }
  },
  methods: {
    async getData(val) {
      let data = await getDetails(val)
      if (data) {
        this.dataList = data;
      }
    }
  }
}
</script>
<style lang="scss" scoped>
@import './index.scss';
</style>