<template>
  <div class="app-container">
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box" >
          <el-row class="box">
            <el-col :span="8">
              <div class="title">单据编号</div>
              <el-input v-model="query.docCode" @keyup.enter.native="searchEnter" placeholder="请输入单据编号" />
            </el-col>
            <el-col :span="8">
              <div class="title">发货单位</div>
              <el-input v-model="query.distributorEname" @keyup.enter.native="searchEnter" placeholder="请输入发货单位" />
            </el-col>
            <el-col :span="8">
              <div class="title">资料审核状态</div>
              <el-select class="select-width" v-model="query.auditStatus" placeholder="请选择">
                <el-option key="" label="全部" value=""></el-option>
                <el-option
                  v-for="item in auditStatusList"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-col>
          </el-row>  
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">第一次资料上传时间</div>
              <el-date-picker
                v-model="query.uploadTime"
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
        <yl-tool-tip class="mar-t-16">提示：请在27号之前审核完毕上个月提交的随货同行单数据，过期审核的数据视为无效流向，不计算佣金。</yl-tool-tip>
      </div>
      <div class="mar-t-10">
        <yl-table :list="dataList" :cell-class-name="() => 'border-1px-b'" :total="query.total" :page.sync="query.page" :limit.sync="query.limit" :loading="loading" @getList="getList">
          <el-table-column>
            <template slot-scope="{ row, $index }">
              <div class="table-view" :key="$index">
                <div class="content">
                  <div class="info">
                    <div class="table-item">
                      <span class="font-title-color" v-if="row.auditStatus !== 5">状态：<span class="result">{{ row.auditStatus | dictLabel(auditStatusList) }}</span></span>
                      <span class="font-title-color" v-else>状态：<span class="result">流向匹配失败</span></span>
                      <div class="item font-weight">
                        <span class="font-weight receipts">单据编号：{{ row.docCode }}</span>
                        <span class="font-weight unit">发货单位：{{ row.distributorEname || '- -' }}</span>
                      </div>
                      <div class="item font-light-color">
                        <span class="font-light-color">第一次资料上传时间：<span class="result">{{ row.uploadTime | formatDate }}</span></span>
                        <span class="font-light-color undate-time">最新资料上传时间：<span class="result">{{ row.lastUploadTime | formatDate }}</span></span>
                        <span class="font-light-color undate-time">审核时间：<span class="result">{{ row.auditTime | formatDate }}</span></span>
                      </div>
                    </div>
                    <div class="product-num">
                      <div class="font-light-color">提交人：<span class="result">{{ row.createUserName || '- -' }}</span></div>
                      <div class="item font-weight visibility">-</div>
                    </div>
                  </div>
                  <div class="edit-btn">
                    <div class="font-light-color">审核人员：<span class="result">{{ row.auditUserName || '- -' }}</span></div>
                    <yl-button v-role-btn="['1']" type="text" class="btn-details" @click="showDetail(row)">查看详情</yl-button>
                    <!-- <yl-button v-role-btn="['2']" type="text" class="btn-approver" v-if="row.auditStatus === 6" @click="showDialogRow(row)">审核</yl-button> -->
                  </div>
                </div>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
    <yl-dialog 
      title="资料审核" 
      width="840px" 
      :visible.sync="showDialog"
      :show-cancle="false" 
      :show-footer="false"
      >
      <div class="dialogTc">
        <el-form 
          :model="form" 
          :rules="rules" 
          ref="dataForm" 
          class="demo-ruleForm">
          <div class="info-box">
            <span>单据编号：{{ rowData.docCode }}</span>
            <span>发货单位：{{ rowData.distributorEname }}</span>
          </div>
          <el-image class="pic" :src="rowData.accompanyingBillPic" :preview-src-list="[rowData.accompanyingBillPic]"></el-image>
          <el-form-item>
            <div class="title"><span>*</span>审批意见 </div>
            <el-input 
              class="textarea"
              type="textarea" 
              placeholder="请输入内容"
              v-model="form.opinion"
              maxlength="200"
              show-word-limit
              :autosize="{ minRows: 3, maxRows: 4}"
            />
          </el-form-item>
          <el-form-item class="reviewbtn">
            <yl-button @click="confirm(3)">驳回</yl-button>
            <yl-button type="primary" @click="confirm(1)">通过</yl-button>
          </el-form-item>
        </el-form>
      </div>
    </yl-dialog>
  </div>
</template>

<script>
import { queryPage, audit } from '@/subject/admin/api/views_sale/resource_management'
import { accompanyBillAuditStatus } from '@/subject/admin/busi/sale/resource'

export default {
  name: 'ResourceAuditList',
  computed: {
    auditStatusList(){
      return accompanyBillAuditStatus()
    }
  },
  data() {
    return {
      query: {
        page: 1,
        limit: 10,
        total: 0,
        docCode: '',
        uploadTime: [],
        auditStatus: 6,
        distributorEname: ''
      },
      dataList: [],
      loading: false,
      showDialog: false,
      form: {
        opinion: ''
      },
      rules: {
        opinion: [
          { required: false, message: '请输入意见', trigger: 'blur' },
          { min: 3, max: 5, message: '长度在 3 到 5 个字符', trigger: 'blur' }
        ]
      },
      rowData: {},
      showViewer: false,
      imageList: []
    }
  },
  activated() {
    this.getList()
  },
  methods: {
    // Enter
    searchEnter(e) {
      const keyCode = window.event ? e.keyCode : e.which;
      console.log('keyCode', keyCode);
      if (keyCode === 13) {
        this.getList()
      }
    },
    async getList() {
      this.loading = true
      let query = this.query
      let data = await queryPage(
        query.page,
        query.limit,
        query.auditStatus,
        query.docCode,
        query.uploadTime && query.uploadTime.length ? query.uploadTime[0] : undefined,
        query.uploadTime && query.uploadTime.length > 1 ? query.uploadTime[1] : undefined,
        query.distributorEname
      )
      this.loading = false
      if (data) {
        this.dataList = data.records
        this.query.total = data.total
      }
    },
    handleSearch() {
      this.query.page = 1
      this.getList()
    },
    handleReset() {
      this.query = {
        page: 1,
        limit: 10,
        total: 0,
        docCode: '',
        uploadTime: [],
        auditStatus: 6
      }
    },
    // 查看详情
    showDetail(row) {
      this.$router.push({
        path: `/resource_management/resource_auditList_detail/${row.id}`
      })
    },
    showDialogRow(row) {
      this.form.opinion = ''
      this.rowData = row
      this.showDialog = true
    },
    async confirm(val) {
      if (val === 3 && this.form.opinion === '') {
        this.$common.error('请输入审批意见！')
        return false
      }
      // 驳回
      this.$common.showLoad()
      let data = await audit(
        val,
        this.rowData.id,
        this.form.opinion
      )
      this.$common.hideLoad()
      if (typeof data != 'undefined') {
        this.$common.n_success('审批完成');
        this.showDialog = false
        this.getList()
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
