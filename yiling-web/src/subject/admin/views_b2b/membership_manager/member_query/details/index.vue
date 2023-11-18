<template>
  <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <div class="common-box">
        <div class="header-bar">
          <div class="sign"></div>
          基本信息
        </div>
        <div class="examine-row">
          <el-row>
            <el-col :span="12">
              <div class="examine-row-conter">
                <p>企业ID：{{ member.eid }}</p>
                <p>企业名称：{{ member.ename }}</p>
                <p>企业地址：{{ member.enterpriseAddress }}</p>
                <p>详细地址：{{ member.address }}</p>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="examine-row-conter">
                <p>企业类型：{{ member.type | dictLabel(companyType) }}</p>
                <p>统一社会信用代码：{{ member.licenseNumber }}</p>
                <p>联系人姓名：{{ member.contactor }}</p>
                <p>联系人电话：{{ member.contactorPhone }}</p>
                <p>账户状态：{{ member.status == 1 ? '正常' : '过期' }}</p>
              </div>
            </el-col>
          </el-row>
        </div>
      </div>
      <div class="common-box">
        <div class="header-bar">
          <div class="sign"></div>
          会员卡信息
        </div>
        <div class="membership-card">
          <p>
            <span v-if="member.status == 1">
              <img :src="member.lightPicture" alt="">
            </span>
            <span v-else>
              <img :src="member.extinguishPicture	" alt="">
            </span>
            <span>{{ member.memberName }}</span>
            <span v-if="member.status == 1">有效期至：{{ member.endTime | formatDate }}</span>
            <span v-else>有效期至：- -</span>
            <span>首次开卡日期：{{ member.openTime | formatDate }}</span>
          </p>
        </div>
      </div>
    </div>
    <div class="bottom-view flex-row-center">
      <yl-button type="primary" plain @click="$router.go(-1)">返回</yl-button>
    </div>
  </div>
</template>
<script>
import { enterpriseType } from '@/subject/admin/utils/busi'
import { getDetail } from '@/subject/admin/api/b2b_api/membership'
export default {
  name: 'MemberQueryDetails',
  components: {},
  computed: {
    companyType() {
      return enterpriseType()
    }
  },
  data() {
    return {
      member: {
        eid: '',
        ename: '',
        address: '',
        type: '',
        licenseNumber: '',
        contactor: '',
        contactorPhone: '',
        status: '',
        memberName: '',
        endTime: '',
        openTime: '',
        enterpriseAddress: '',
        lightPicture: '',
        extinguishPicture: ''
      }
    }
  },
  mounted() {
    let valueID = this.$route.params
    if (valueID.id) {
      this.getData(valueID.id)
    }
  },
  methods: {
    async getData(val) {
      let data = await getDetail(val)
      if (data) {
        this.member = data;
      }
    }
  }
}
</script>
<style lang="scss" scoped>
@import './index.scss';
</style>