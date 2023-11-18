<template>
  <!-- 添加商品弹框 -->
  <yl-dialog :title="title" :visible.sync="show" width="966px" :show-footer="false">
    <div class="import-dialog-content">
      <div class="title-view flex-row-left">
        <el-radio-group class="flex1" v-model="importType" @change="typeChange">
          <el-radio-button :label="1">excel导入</el-radio-button>
          <el-radio-button :label="2">历史记录</el-radio-button>
        </el-radio-group>
        <div v-if="importType == 1">
          <el-link class="mar-l-10 mar-r-10 font-size-lg" type="primary" :underline="false" :href="templateUrl"> 下载模板 </el-link>
        </div>
      </div>
      <div class="font-size-base mar-t-10" v-if="importType == 1">
        <div v-if="descriptionList && descriptionList.length > 0">
<!--          操作提示：-->
          <div v-for="(item, index) in descriptionList" :key="'view' + index">{{ item }}</div>
        </div>
        <div class="mar-t-8 text-c">
          <yl-upload-file
            :limit="limit"
            :list-err-mode="true"
            :max-size="limitSize"
            :action="action"
            :extral-data="currentExtralData"
            :file-type="fileType"
            :oss-key="ossKey"
            :show-progress="true"
            @onSuccess="onSuccess" />
        </div>
      </div>
      <div class="mar-t-10" style="padding-bottom: 10px;background: #FFFFFF;" v-if="importType == 2">
        <yl-table
          border
          show-header
          :list="goodsList"
          :total="goodsTotal"
          :page.sync="goodsQuery.page"
          :limit.sync="goodsQuery.limit"
          :loading="loading"
          @getList="getList"
        >
          <el-table-column label="导入时间" min-width="210" align="center">
            <template slot-scope="{ row }">
              <div class="item">{{ row.createTime | formatDate }}</div>
            </template>
          </el-table-column>
          <el-table-column label="文件" min-width="160" align="center" prop="fileName">
          </el-table-column>
          <el-table-column label="操作人" min-width="100" align="center" prop="createName">
          </el-table-column>
          <el-table-column label="成功 / 总数量" min-width="210" align="center">
            <template slot-scope="{ row }">
              <div class="item">{{ row.successNumber }} / {{ row.successNumber + row.failNumber }}</div>
            </template>
          </el-table-column>
          <el-table-column label="处理状态" min-width="100" align="center">
            <template slot-scope="{ row }">
              <div class="item">
                <span :class="row.status === 2 ? 'col-down' : (row.status === 3 ? 'col-up' : 'col-yellow')">{{ row.status | dictLabel(excelExportStatus) }}</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="日志" min-width="100" align="center">
            <template slot-scope="{ row }">
              <div v-if="row.status === 2">
                <el-tooltip class="item" effect="dark" popper-class="import-tooltip-view" :content="row.downloadName" placement="top-end">
                  <yl-button type="text" @click="downLoad(row)">下载</yl-button>
                </el-tooltip>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </yl-dialog>
</template>

<script>
// 使用示例，接口权限配置 `/admin/dataCenter/api/v1/excel/import/${excelCode}/**`
// <import-send-dialog :visible.sync="importSendVisible" :excel-code="info.excelCode" ref="importSendRef"></import-send-dialog>

import { importGetConfig, importDownLoadFile, importPage } from '@/subject/pop/api/common'
import { ylUploadFile } from '@/subject/pop/components'
import { excelExportStatus } from '@/subject/pop/utils/busi'

// 导入发券弹框
export default {
  name: 'ImportSendDialog',
  components: {
    ylUploadFile
  },
  computed: {
    excelExportStatus() {
      return excelExportStatus()
    },
    show: {
      get() {
        return this.visible
      },
      set(val) {
        this.$emit('update:visible', val)
      }
    },
    currentExtralData() {
      return this.extralData || {}
    }
  },
  props: {
      visible: {
        type: Boolean,
        default: false
      },
      // 后台提供excelCode
      excelCode: {
        type: String,
        default: ''
      },
      // 上传参数
      extralData: {
        type: Object,
        default: () => { }
      },
      // 有值则是oss直接上传，值是后台给的上传type
      ossKey: {
        type: String,
        default: ''
    },
    // 上传个数限制
    limit: {
      type: Number,
      default: 1
      }
  },
  data() {
    return {
      addGoodsDialog: false,
      loading: false,
      goodsQuery: {
        page: 1,
        limit: 10
      },
      goodsList: [],
      goodsTotal: 0,
      importType: 1,
      title: '',
      descriptionList: [],
      action: '',
      fileType: '',
      limitSize: 0,
      templateUrl: ''
    };
  },
  methods: {
    init() {
      this.goodsList = []
      this.importType = 1
      this.getConfig()
    },
    async getConfig() {
      this.$common.showLoad()
      let data = await importGetConfig(
        this.excelCode
      );
      this.$common.hideLoad()
      if (data) {
        this.title = data.title
        this.descriptionList = data.descriptionList
        this.action = `/admin/dataCenter/api/v1/excel/import/${data.excelCode}/save`
        this.currentExtralData.excelCode = data.excelCode
        this.fileType = data.limitType
        this.limitSize = data.limitSize
        this.templateUrl = data.templateUrl
      }
    },
    async getList() {
      this.loading1 = true
      let goodsQuery = this.goodsQuery
      let data = await importPage(
        goodsQuery.page,
        goodsQuery.limit,
        this.currentExtralData.excelCode
      );
      this.loading1 = false
      if (data) {
        this.goodsList = data.records
        this.goodsTotal = data.total
      }
    },
    // 优惠券类型切换
    typeChange(type) {
      if (type == 2) {
        this.getList()
      }
    },
    onSuccess(data) {
      if (typeof data != 'undefined') {
        this.$common.n_success('导入成功')
      }
    },
    async downLoad(row) {
      if (row.id) {
        this.$common.showLoad()
        let data = await importDownLoadFile(this.currentExtralData.excelCode, row.id)
        this.$common.hideLoad()
        if (data && data.url) {
          const xRequest = new XMLHttpRequest()
          xRequest.open('GET', data.url, true)
          xRequest.responseType = 'blob'
          xRequest.onload = () => {
            const url = window.URL.createObjectURL(xRequest.response)
            const a = document.createElement('a')
            a.href = url
            a.download = row.downloadName
            a.click()
          }
          xRequest.send()
        }
      }
    },
    getFileName(path) {
        let url = path.split('?')[0];
        var pos = url.lastIndexOf('/');
        if (pos > 0) {
          return url.substring(pos + 1)
        }
    }
  }
};
</script>

<style lang="scss" >
.my-form-box{
  .el-form-item{
    .el-form-item__label{
      color: $font-title-color; 
    }
    label{
      font-weight: 400 !important;
    }
  }
  .my-form-item-right{
    label{
      font-weight: 400 !important;
    }
  }
}
.import-tooltip-view{
  max-width: 200px;
}
</style>
<style lang="scss" scoped>
@import './index.scss';
</style>
