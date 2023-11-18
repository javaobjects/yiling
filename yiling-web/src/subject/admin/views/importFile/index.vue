<template>
  <div class="app-container">
    <div class="down flex-row-center">
      <yl-upload-file
        :action="info.action"
        :extral-data="info.extralData"
        :file-type="info.fileType"
        @onSuccess="onSuccess"
      />
    </div>
    <yl-dialog
      title="批量导入结果"
      :show-footer="false"
      width="585px"
      :visible.sync="show">
      <div class="dialog-content">
        <div class="content">
          <div class="content-top">
            <div>本次预导入数据：<span class="font-important-color">{{ total || 0 }}</span> 条</div>
            <div>成功导入：<span class="important-success-color">{{ result.successCount || 0 }}条</span></div>
            <div>导入失败：<span class="important-fail-color">{{ result.failCount || 0 }}条</span></div>
          </div>
        </div>
        <div class="down-btn" v-if="result.failCount">
          <yl-button type="primary" @click="handleDownLoad">
            导出失败结果
          </yl-button>
        </div>
      </div>
    </yl-dialog>
  </div>
</template>

<script>
  import { ylUploadFile } from '@/subject/admin/components'

  export default {
    components: {
      ylUploadFile
    },
    computed: {
      total() {
        return this.result.successCount + this.result.failCount
      }
    },
    data() {
      return {
        info: {
          action: '',
          extralData: {},
          fileType: ''
        },
        show: false,
        result: {}
      }
    },
    mounted() {
      let params = this.$route.query
      this.$log(params)
      if (params) {
        this.info = params
      }
    },
    methods: {
      onSuccess(data) {
        // this.$log(data)
        this.result = data
        this.show = true
      },
      handleDownLoad() {
        if (this.result && this.result.failUrl) {
          const xRequest = new XMLHttpRequest()
          xRequest.open('GET', this.result.failUrl, true)
          xRequest.responseType = 'blob'
          xRequest.onload = () => {
            const url = window.URL.createObjectURL(xRequest.response)
            const a = document.createElement('a')
            a.href = url
            a.download = this.result.sheetName ? `${this.result.sheetName}.xls` : ''
            a.click()
          }
          xRequest.send()
        }
      }
    }
  }
</script>

<style lang="scss" scoped>
  .app-container {
    .down {
      height: 100%;
      width: 100%;
    }
    .content {
      background: #EDF7FF;
      width: 552px;
      border-radius: 4px;
      box-sizing: border-box;
      margin: 0 auto;
      margin-top: 16px;
      margin-bottom: 18px;
      .content-top{
        height: 100px;
        padding: 16px;
        box-sizing: border-box;
        font-size: 14px;
        font-weight: 600;
        color: #333333;
        display: flex;
        flex-direction: column;
        justify-content: space-between;
        .important-success-color{
          color: #15AD31;
        }
        .important-fail-color{
          color: #E62412;
        }

      }
    }
    .down-btn {
      display: flex;
      justify-content: center;
      padding-bottom: 24px;
    }
  }
</style>
