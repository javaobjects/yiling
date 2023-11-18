<template>
  <div>
    <el-upload
      ref="upload"
      multiple
      :show-file-list="false"
      :class="showUploadButton ? 'image-uploader-hide' : 'image-uploader'"
      :drag="drag"
      action="http"
      :http-request="uploadFile"
      :on-error="handleError"
      :on-exceed="onExceed"
      :on-change="onFileChange"
      :on-success="successUpload"
      :before-upload="beforeUpload"
      :before-remove="beforeRemove"
      :limit="limit"
      :disabled="loading">
      <div v-show="!loading && !showUploadButton">
        <i class="el-icon-upload" />
        <div class="el-upload__text">
          拖拽{{ fileType }}文件到这里或<em>点击上传</em>
        </div>
      </div>
      <div class="flex-column-center" style="height: 100%;" v-show="loading && !showUploadButton">
        <i class="el-icon-loading" />
        <div class="el-upload__text font-light-color">
          上传中...
        </div>
      </div>
      <div class="flex-row-left">
        <el-button
          v-if="showUploadButton"
          size="small"
          type="primary"
          :disabled="disableUploadButton">
          {{ uploadBtnText }}
        </el-button>
        <div v-if="showTip" class="el-upload__tip mar-l-8">{{ tipMessage }}</div>
      </div>
    </el-upload>
    <div v-show="showProgress && progress">
      <el-progress
        :format="format"
        class="up-el-progress"
        :text-inside="true"
        :stroke-width="16"
        :percentage="percent"
        status="success">
      </el-progress>
    </div>
    <div class="mar-t-10" v-if="listErrMode && beforeUpList.length">
      <yl-button style="float: right;" type="text" @click="clearBeforeList">清理拦截记录</yl-button>
      <yl-table
        border
        show-header
        :max-height="350"
        :show-pagin="false"
        :list="beforeUpList">
        <el-table-column label="文件名称" min-width="120" align="center" prop="name" :key="Math.random()">
        </el-table-column>
        <el-table-column label="状态" min-width="50" align="center" :key="Math.random()">
          <template slot-scope="{ row }">
            <div class="item">
              <span class="col-down" v-if="row.upType === 1">上传成功</span>
              <span class="col-up" v-if="row.upType === 2">上传失败</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="拦截类型" min-width="50" align="center" :key="Math.random()">
          <template slot-scope="{ row }">
            <div class="item">
              <span class="col-up" v-if="row.errType === 1">大小超限</span>
              <span class="col-up" v-if="row.errType === 2">格式错误</span>
              <span class="col-up" v-if="row.errType === 3">文件重复</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="拦截原因" align="center" prop="errMsg" :key="Math.random()">
        </el-table-column>
      </yl-table>
    </div>
  </div>
</template>

<script>
import request from '@/subject/pop/utils/request'
import { getOssKey } from '@/subject/pop/api/common'
import axios from 'axios'
export default {
  name: 'UploadFile',
  props: {
    extralData: {
      type: Object,
      default: () => { }
    },
    // 请求地址
    action: {
      type: String,
      default: ''
    },
    // 上传个数限制
    limit: {
      type: Number,
      default: 1
    },
    // 是否可拖曳
    drag: {
      type: Boolean,
      default: true
    },
    // 允许上传的文件,逗号分隔，默认jpg,png,jpeg
    fileType: {
      type: String,
      default: 'xlsx,xls'
    },
    // 有值则是oss直接上传，值是后台给的上传type
    ossKey: {
      type: String,
      default: ''
    },
    // 上传大小限制 这里是不超过多少k(以 KB 为单位),4096KB 也就是 4M
    maxSize: {
      type: Number,
      default: 0
    },
    // 列表展示错误模式
    listErrMode: {
      type: Boolean,
      default: false
    },
    // 是否上传按钮
    showUploadButton: {
      type: Boolean,
      default: false
    },
    // 是否禁用上传按钮
    disableUploadButton: {
      type: Boolean,
      default: false
    },
    uploadBtnText: {
      type: String,
      default: '点击上传'
    },
    // 是否展示上传备注
    showTip: {
      type: Boolean,
      default: false
    },
    // 上传备注内容 如:只能上传pdf,最多上传1个文件
    tipMessage: {
      type: String,
      default: ''
    },
    // 后端过滤重复文件给的api地址
    callbackUrl: {
      type: String,
      default: ''
    },
    // 是否打开进度条
    showProgress: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      loading: false,
      percent: 0,
      currentFileNum: 0,
      totalFileNum: 0,
      beforeUpList: [],
      progress: false
    }
  },
  mounted() {
    this.timer = null
    this.progressTimer = null
  },
  methods: {
    // 检查文件格式
    checkFileType(file, handleErr) {
      // 以检查文件是否为.jpg为例
      const fileName = file.name;
      //取得文件后缀，并将其转为小写
      const fileType = fileName.substring(fileName.lastIndexOf('.')).toLowerCase();
      // 获取传递过来的文件后缀，并将其转为小写
      const fileTypeList = this.fileType.toLowerCase().split(',');
      if (!fileType) {
        if (this.listErrMode && handleErr) {
          this.beforeUpList.unshift({
            name: file.name,
            upType: 2,
            errType: 2,
            errMsg: '格式无法识别'
          })
        } else {
          this.$common.error('文件格式为空')
        }
        return false
      }
      const find = fileTypeList.find(item => fileType === `.${item}`)
      if (find) {
        if (this.maxSize > 0) {
          const isLt1M = file.size / 1024 < this.maxSize
          if (!isLt1M) {
            const msg = `上传文件大小不能超过 ${this.maxSize < 1024 ? this.maxSize + 'KB' : this.maxSize / 1024 + 'MB' } !`
            if (this.listErrMode && handleErr) {
              this.beforeUpList.unshift({
                name: file.name,
                upType: 2,
                errType: 1,
                errMsg: msg
              })
            } else {
              this.$common.warn(msg)
            }
            return false
          }
        }
        return true
      } else {
        const msg = `只允许上传${this.fileType}格式`
        if (this.listErrMode && handleErr) {
          this.beforeUpList.unshift({
            name: file.name,
            upType: 2,
            errType: 2,
            errMsg: msg
          })
        } else {
          this.$common.warn(msg)
        }
        return false
      }
    },
    // 上传前
    beforeUpload(file) {
      const allow = this.checkFileType(file, true)
      return !!allow
    },
    // 清理数组
    clearBeforeList() {
      this.beforeUpList = []
    },
    // 添加错误文件 errType --> 1-大小超限； 2-格式错误；3-文件重复
    addErrFile(name, errType) {
      let errMsg = ''
      if (errType === 3) {
        errMsg = '文件已重复存在'
      }
      this.beforeUpList.unshift({
        name,
        upType: 1,
        errType,
        errMsg
      })
    },
    // 移除前
    beforeRemove(file, fileList) {
      this.$log('beforeRemove')
      // this.$emit('onUploadDone', [].concat(this.beforeUpList), this.clearBeforeList, )
    },
    // 超过上传个数限制
    onExceed(files, fileList) {
      this.$common.warn(`一次最多只能上传${this.limit}个文件`)
    },
    // 判断文件个数处理函数
    handleFileNum(file, fileList) {
      if (fileList.length === 1) {
        const allow = this.checkFileType(file, false)
        if (allow) {
          this.totalFileNum = fileList.length
          this.progress = true
          this.percent = 0
        }
      } else if (fileList.length > 1) {
        this.totalFileNum = fileList.length
        this.progress = true
        this.percent = 0
      }
    },
    // 文件变化监听
    onFileChange(file, fileList) {
      clearTimeout(this.timer)
      this.timer = setTimeout(() => {
        this.handleFileNum(file, fileList)
      }, 50)
    },
    // 上传成功
    successUpload(response, file, fileList) {
      this.currentFileNum = fileList.length
      this.percent = (this.currentFileNum / this.totalFileNum).toFixed(2) * 100
      this.$refs.upload.clearFiles()
      if (this.percent === 100) {
        clearTimeout(this.progressTimer)
        this.progressTimer = setTimeout(() => {
          this.progress = false
        }, 3000)
      }
    },
    handleError(err, file, fileList) {
      this.percent = 0
      this.$log(err)
    },
    async uploadFile(params) {
      this.loading = true
      let file = params.file
      let formData = new FormData()
      if (this.extralData && Object.keys(this.extralData).length) {
        Object.keys(this.extralData).map(key => {
          let value = this.extralData[key]
          formData.append([key], value)
        })
      }
      // oss直接上传
      if (this.ossKey) {
        const ossData = await this.getOssKeyId()
        const key = `${ossData.dir}/${this.$common.getUUID().replace(/-/g, '')}${params.file.name.substr(params.file.name.lastIndexOf('.'))}`
        const src = `${ossData.domain}/admin/dataCenter/api/v1/file/getUrl?type=${this.ossKey}&key=${key}`
        formData.append('key', key)
        formData.append('Cache-Control', 'no-cache')
        formData.append('OSSAccessKeyId', ossData.accessKeyId)
        formData.append('policy', ossData.policy)
        formData.append('Signature', ossData.signature)
        formData.append('success_action_redirect', src)
        formData.append('file', file)
        const imgData = await axios.post(ossData.host, formData, {
          headers: {
            'Content-Type': 'multipart/form-data'
          },
          onUploadProgress: (event) => {
            if (this.totalFileNum === 1) {
              this.percent = (event.loaded / event.total).toFixed(2) * 100
            }
          }
        })
        if (imgData.data.code === 200) {
          this.$emit('onSuccess', {
            key: key,
            url: imgData.data.data,
            name: params.file.name,
            desc: '开发人员注意：oss直接上传后url值仅供展示使用，切勿用作接口入参'
          }, this.addErrFile)
        }
      } else {
        formData.append('file', file)
        let imgData = await request({
          url: this.action,
          method: 'post',
          data: formData,
          onUploadProgress: (event) => {
            if (this.totalFileNum === 1) {
              this.percent = (event.loaded / event.total).toFixed(2) * 100
            }
          }
        })
        if (imgData) {
          this.$emit('onSuccess', imgData, this.addErrFile)
        }
      }
      this.loading = false
    },
    format(text) {
      return `进度${text}%（${this.currentFileNum}/${this.totalFileNum}）`
    },
    async getOssKeyId() {
      if (this.ossKey) {
        const ossKey = `yl_oss_ssp_${process.env.NODE_ENV}_${this.ossKey}`
        const data = this.$common.getSessionCache(ossKey)
        if (data) {
          return data
        } else {
          const data = await getOssKey(this.ossKey)
          if (data) {
            this.$common.setSessionCache(ossKey, data)
            return data
          }
        }
      }
    }
  }
}
</script>

<style lang="scss">
  .image-uploader {
    padding-bottom: 8px;
.upload-container {
  width: 400px;
  height: 200px;
  position: relative;
  .image-uploader {
    height: 100%;
  }
  .el-upload {
    .el-upload-dragger {
      width: 100%;
      height: 200px;
        }
      }
    }
  }
  .image-uploader-hide {
    padding-bottom: 5px;
    .el-upload {
      .el-upload-dragger {
        width: auto;
        height: auto;
        border: none;
      }
    }
    .el-button--small {
      border-radius: 5px;
    }
    }
  .up-el-progress {
    .el-progress-bar {
      max-width: 500px;
  }
}
</style>
