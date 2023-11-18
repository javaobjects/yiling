<template>
  <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <div class="insurance-detail-info">
        <div class="common-box insurance-detail-item">
          <div class="header-bar">
            <div class="sign"></div>基础信息
          </div>
          <div class="item-info mar-t-16">
            <el-row>
              <el-col :span="8">
                <div class="item">
                  <div class="item-title">
                    申请时间: <span class="item-value">{{ detailData.applyTime | formatDate }}</span>
                  </div>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="item">
                  <div class="item-title">
                    数据来源: <span class="item-value">{{ detailData.source === 1 ? '销售助手APP' : '大运河APP' }}</span>
                  </div>
                </div>
              </el-col>
            </el-row>
          </div>
        </div>
        <div class="common-box insurance-detail-item">
          <div class="header-bar">
            <div class="sign"></div>账号信息
          </div>
          <div class="item-info mar-t-16">
            <el-row>
              <el-col :span="8">
                <div class="item">
                  <div class="item-title">
                    账号ID: <span class="item-value">{{ detailData.userId || '--' }}</span>
                  </div>
                </div>
                <div class="item mar-t-20">
                  <div class="item-title">
                    联系人姓名: <span class="item-value">{{ detailData.name || '--' }}</span>
                  </div>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="item">
                  <div class="item-title">
                    联系人电话: <span class="item-value">{{ detailData.mobile || '--' }}</span>
                  </div>
                </div>
              </el-col>
            </el-row>
            <div class="item-info-firm" v-if=" detailData.enterpriseList && detailData.enterpriseList.length > 0 " >
              <el-card
                v-for="(item, index) in detailData.enterpriseList"
                :key="index"
                class="list"
                shadow="hover"
                :body-style="{ padding: '15px' }"
              >
                <div>
                  <p>{{ item.name }}</p>
                  <p>{{ item.identity }}</p>
                </div>
              </el-card>
            </div>
            <p class="hint-info">注销原因: {{ detailData.rejectReason }}</p>
          </div>
        </div>
        <div class="common-box insurance-detail-item">
          <div class="header-bar mar-b-16">
            <div class="sign"></div> 审核
          </div>
          <div class="examine-row">
            <el-form
              :model="form"
              :rules="rules"
              label-position="left"
              ref="dataForm"
              label-width="80px"
              class="demo-ruleForm"
            >
              <el-form-item label="审核" prop="authStatus">
                <el-radio-group v-model="form.authStatus">
                  <el-radio :label="2">通过</el-radio>
                  <el-radio :label="3">撤销</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-form>
          </div>
        </div>
      </div>
      <div class="flex-row-center bottom-view">
        <yl-button type="primary" @click="save">保存</yl-button>
        <yl-button plain type="primary" @click="$router.go(-1)">返回</yl-button>
      </div>
    </div>
  </div>
</template>

<script>
import { userAuthDetail, userAuthUpdateAuth } from '@/subject/admin/api/zt_api/enterprise_audit';

export default {
  name: 'CancelAuditDetail',
  components: {},
  computed: {},
  data() {
    return {
      query: {},
      detailData: {},
      form: {
        // 1-待注销 2-已注销 3-已撤销
        authStatus: 2
      },
      rules: {
        authStatus: [
          { required: true, message: '请选择审核状态', trigger: 'change' }
        ]
      }
    };
  },
  mounted() {
    this.query.eid = this.$route.params.id;
    if (this.query.eid) {
      this.getDetail(this.query.eid);
    }
  },
  methods: {
    async getDetail(id) {
      this.$common.showLoad();
      let data = await userAuthDetail(id);
      this.$common.hideLoad();
      if (data !== undefined) {
        this.detailData = data;
      }
    },
    // 保存
    save() {
      this.$refs['dataForm'].validate(async (valid) => {
        const form = this.form;
        const query = this.query;
        this.$common.showLoad();
        let data = await userAuthUpdateAuth(query.eid, form.authStatus);
        this.$common.hideLoad();
        if (data !== undefined) {
          this.$message.success('保存成功');
          this.$router.go(-1);
        }
      });
    }
  }
};
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
