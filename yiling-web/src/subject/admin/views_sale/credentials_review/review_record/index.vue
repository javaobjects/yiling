<template>
  <div class="app-container">
    <div class="app-container-content">
      <div class="common-box box-search">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">姓名</div>
              <el-input v-model="query.name" @keyup.enter.native="searchEnter" placeholder="请输入姓名" />
            </el-col>
            <el-col :span="6">
              <div class="title">证件号</div>
              <el-input v-model="query.idNumber" @keyup.enter.native="searchEnter" placeholder="请输入证件号" />
            </el-col>
            <el-col :span="6">
              <div class="title">审核时间</div>
              <el-date-picker
                v-model="reviewTimeRange"
                type="daterange"
                format="yyyy/MM/dd"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                value-format="yyyy-MM-dd"
              >
              </el-date-picker>
            </el-col>
          </el-row>
          <el-row class="box">
            <el-col :span="6">
              <div class="title">审核状态</div>
              <el-select v-model="query.auditStatus" placeholder="请选择状态">
                <el-option label="全部" :value="0"></el-option>
                <el-option label="待审核" :value="1"></el-option>
                <el-option label="审核通过" :value="2"></el-option>
                <el-option label="审核驳回" :value="3"></el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <div class="title">审核人</div>
              <el-input v-model="query.auditUserName" @keyup.enter.native="searchEnter" placeholder="请输入审核人姓名" />
            </el-col>
          </el-row>
        </div>
        <div class="search-btn-box">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn :total="total" @search="handleSearch" @reset="handleReset" />
            </el-col>
          </el-row>
        </div>
      </div>
      <div class="table-box mar-t-16">
        <yl-table
          border
          :show-header="true"
          :list="dataList"
          :total="total"
          :page.sync="query.current"
          :limit.sync="query.size"
          :loading="loading"
          @getList="getList"
        >
          <el-table-column label="姓名" align="center" prop="name"> </el-table-column>
          <el-table-column label="联系方式" align="center" prop="mobile"> </el-table-column>
          <el-table-column label="证件号" align="center" prop="idNumber"> </el-table-column>
          <el-table-column label="状态" align="center" prop="auditStatus">
            <template slot-scope="{ row }">
              <span :class="[row.auditStatus === 2 ? 'col-down' : '', row.auditStatus === 3 ? 'col-up' : '', ]">{{ row.auditStatus | reviewStatus }}</span>
            </template>
          </el-table-column>
          <el-table-column label="提交时间" align="center" prop="createTime">
            <template slot-scope="{ row }">
              <div>{{ row.createTime | formatDate }}</div>
            </template>
          </el-table-column>
          <el-table-column label="审核时间" align="center" prop="auditTime">
            <template slot-scope="{ row }">
              <div>{{ row.auditTime | formatDate }}</div>
            </template>
          </el-table-column>
          <el-table-column label="审核人" align="center" prop="auditUserName">
            <template slot-scope="{ row }">
              <div>{{ row.auditUserName || "- -" }}</div>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center">
            <template slot-scope="{ row }">
              <div>
                <yl-button type="text" @click="detail(row)">查看</yl-button>
                <yl-button type="text" @click="review(row.id)" v-if="row.auditStatus === 1">审核</yl-button>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
      <!-- 反驳弹窗 -->
      <yl-dialog :visible.sync="reviewShow" width="40%" title="证件审核" :show-cancle="false" :show-footer="false">
        <div class="dialog-content">
          <div class="userinfo">
            <span class="name">姓名：{{ reviewForm.name }}</span>
            <span class="credential">证件号：{{ reviewForm.idNumber }}</span>
          </div>
          <div class="userimg">
            <div class="imgbox">
              <el-image class="img" :src="reviewForm.idCardFrontPhotoUrl" :preview-src-list="[reviewForm.idCardFrontPhotoUrl]" > </el-image>
              <span>手持证件照正面</span>
            </div>
            <div class="imgbox">
              <el-image class="img" :src="reviewForm.idCardBackPhotoUrl" :preview-src-list="[reviewForm.idCardBackPhotoUrl]" > </el-image>
              <span>手持证件照反面</span>
            </div>
          </div>
          <el-form :model="reviewForm" class="reviewForm" ref="reviewFormRef" :rules="reviewRules" label-position="right">
            <el-row>
              <el-col>
                <el-form-item label="驳回原因：" prop="auditRejectReason">
                  <el-input
                    type="textarea"
                    v-model="reviewForm.auditRejectReason"
                    placeholder="驳回必须填写原因!"
                    maxlength="50"
                    show-word-limit
                    :rows="2"
                  >
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item class="reviewbtn">
              <yl-button @click="handleReview(3)">驳回</yl-button>
              <yl-button type="primary" @click="handleReview(2)">通过</yl-button>
            </el-form-item>
          </el-form>
        </div>
      </yl-dialog>
    </div>
  </div>
</template>

<script>
import { staffExternalAuditList, reviewCredential, reviewDetail } from '@/subject/admin/api/views_sale/credentials_review'

export default {
  name: 'ReviewRecord',
  components: {},
  computed: {},
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
  data() {
    return {
      query: {
        current: 1,
        size: 10,
        name: '',
        idNumber: '',
        auditStatus: 1,
        auditUserName: ''
      },
      total: 0,
      // 列表
      dataList: [],
      reviewTimeRange: [],
      loading: false,
      reviewShow: false,
      reviewForm: {},
      reviewRules: {
        auditRejectReason: [
          { required: true, message: '请输入驳回原因', trigger: 'blur' },
          { min: 1, max: 50, message: '长度在50个字符以内', trigger: 'blur' }
        ]
      },
      userinfoImgPreview: []

    }
  },
  activated() {
    this.getList()
  },
  methods: {
    // Enter
    searchEnter(e) {
      const keyCode = window.event ? e.keyCode : e.which;
      if (keyCode === 13) {
        this.getList()
      }
    },
    async getList() {
      this.loading = true
      let query = this.query
      let data = await staffExternalAuditList(
        query.current,
        query.size,
        query.name,
        query.idNumber,
        query.auditStatus,
        query.auditUserName,
        this.reviewTimeRange && this.reviewTimeRange.length ? this.reviewTimeRange[0] : '',
        this.reviewTimeRange && this.reviewTimeRange.length ? this.reviewTimeRange[1] : ''
      )
      this.loading = false
      if (data) {
        this.dataList = data.records
        this.total = data.total
      }
    },
    handleSearch() {
      this.query.current = 1
      this.getList()
    },
    handleReset() {
      this.query = {
        current: 1,
        size: 10,
        name: '',
        idNumber: '',
        auditStatus: 1,
        auditUserName: ''
      }
      this.reviewTimeRange = []
    },
    // 审核弹窗
    async review(id) {
      this.reviewShow = true
      let data = await reviewDetail(id)
      if (data) {
        this.reviewForm = { ...data }
      }
    },
    // 审核提交
    async handleReview(auditStatus) {
      if (auditStatus === 3) {
        this.$refs.reviewFormRef.validate(async valid => {
          if (valid) {
            let data = await reviewCredential(this.reviewForm.id, auditStatus, this.reviewForm.auditRejectReason)
            if (data !== undefined) {
              this.$common.n_success('驳回成功')
              this.reviewShow = false
              await this.getList()
            }
          }
        })
      } else if (auditStatus === 2) {
        let data = await reviewCredential(this.reviewForm.id, auditStatus)
        if (data !== undefined) {
          this.$common.n_success('审核通过')
          this.reviewShow = false
          await this.getList()
        }
      }
    },
    // 查看详情
    detail(row) {
      this.$router.push({
        name: 'ReviewRecordDetail',
        params: { id: row.id }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
