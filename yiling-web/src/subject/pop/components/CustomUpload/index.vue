<template>
  <div class="upload_image" :style="{ 'width': width, 'height': height }">
    <el-upload
      :action="action"
      :http-request="uploadFile"
      list-type="picture-card"
      multiple
      :before-upload="beforeUpload"
      :on-success="handleAddUrlSucess"
      :on-error="unpladError"
      :on-preview="handlePictureCardPreview"
      :on-remove="handleRemove"
      :on-exceed="onExceed"
      :limit="limit"
      :file-list="fileList"
      :data="extralData"
      :disabled="loading"
      :show-file-list="showFileList"
    >
      <div v-show="!loading">
        <slot name="list"></slot>
        <div v-if="!$slots.default">
          <div class="flex-row-center up-box" :style="{ 'width': width, 'height': height }">
            <img
              v-if="defaultUrl"
              style="object-fit: contain;"
              :src="defaultUrl">
            <i v-else class="el-icon-plus"></i>
          </div>
        </div>
      </div>
      <div class="flex-column-center load" :style="{ 'width': width, 'height': height }" v-show="loading">
        <i class="el-icon-loading" />
      </div>
    </el-upload>
  </div>
</template>

<script>
  import request from '@/subject/pop/utils/request'
  import { getOssKey } from '@/subject/pop/api/common'
  import axios from 'axios'

  export default {
    name: 'Upload',
    props: {
      // 默认图片, 设置则显示，不设则不显示，现无上传成功后自动设置，需手动设置
      defaultUrl: {
        type: String,
        default: ''
      },
      // 请求额外参数
      extralData: {
        type: Object,
        default: () => { }
      },
      // 请求地址
      action: {
        type: String,
        default: '/admin/dataCenter/api/v1/file/upload'
      },
      // 上传个数限制
      limit: {
        type: Number,
        default: 1
      },
      // 允许上传的文件,逗号分隔，默认jpg,png,jpeg
      fileType: {
        type: String,
        default: 'jpg,png,jpeg'
      },
      // 上传图片大小限制 这里是不超过多少k(以 KB 为单位),默认为 4096KB 也就是 4M
      maxSize: {
        type: Number,
        default: 4096
      },
      fileList: {
        type: Array,
        default: () => []
      },
      showFileList: {
        type: Boolean,
        default: true
      },
      // 有值则是oss直接上传，值是后台给的上传type
      ossKey: {
        type: String,
        default: ''
      },
      // 传入宽度
      width: {
        type: String,
        default: '100px'
      },
      // 传入高度
      height: {
        type: String,
        default: '100px'
      }
    },
    data() {
      return {
        loading: false
      }
    },
    mounted() {
    },
    methods: {
      // 检查文件格式
      checkFileType(file) {
        // 以检查文件是否为.jpg为例
        const fileName = file.name
        const fileType = fileName.substring(fileName.lastIndexOf('.'))
        const fileTypeList = this.fileType.split(',')
        if (!fileType) {
          this.$common.error('文件格式为空')
          return false
        }
        const find = fileTypeList.find(item => fileType === `.${item}`)
        if (find) {
          return true
        } else {
          this.$common.error(`只允许上传${this.fileType}格式`)
          return false
        }
      },
      // 上传前
      beforeUpload(file) {
        const allow = this.checkFileType(file)
        if (!allow) {
          return false
        }
        // 检测大小
        const isLtMB = file.size / 1024 < this.maxSize;
        if (!isLtMB) {
          this.$common.error(`上传文件大小不能超过 ${this.maxSize < 1024 ? this.maxSize + 'KB' : this.maxSize / 1024 + 'MB' } !`);
        }
        return isLtMB;
      },
      // 超过上传个数限制
      onExceed(files, fileList) {
        this.$common.error(`一次最多只能上传${this.limit}个文件`);
      },
      // 上传成功
      handleAddUrlSucess(response, file, fileList) {
        // this.$refs.upload.clearFiles()
        // this.$emit("on-success", response, file, fileList);
      },
      // 上传失败
      unpladError(err, file, fileList) {
        this.$log(err);
        this.$common.error('上传失败，请重新上传！');
        //   this.$emit("on-error", err, file, fileList);
      },
      // 大图预览
      handlePictureCardPreview(file, fileList) {
        this.$emit('on-preview', file, fileList);
      },
      // 图片删除
      handleRemove(file, fileList) {
        this.$emit('on-remove', file, fileList);
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
          this.$common.showLongLoad()
          const imgData = await axios.post(ossData.host, formData, {
            headers: {
              'Content-Type': 'multipart/form-data'
            }
          })
          this.$common.hideLoad()
          if (imgData.data.code === 200) {
            this.$emit('onSuccess', {
              key: key,
              url: imgData.data.data,
              name: params.file.name,
              desc: '开发人员注意：oss直接上传后url值仅供展示使用，切勿用作接口入参'
            })
          }
        } else {
          formData.append('file', file)
          this.$common.showLongLoad()
          const actionUrl = '/admin/dataCenter/api/v1/file/upload'
          let imgData = await request({
            url: this.action ? this.action : actionUrl,
            method: 'post',
            data: formData
          })
          this.$common.hideLoad()
          if (imgData) {
            this.$emit('onSuccess', imgData)
          }
        }
        this.loading = false
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
  };
</script>

<style lang="scss" scoped>
  .upload_image {
    ::v-deep .el-upload--picture-card {
      width: 100% !important;
      height: 100% !important;
      line-height: 1 !important;
    }
    .up-box {
      img {
        width: 100%;
        height: 100%;
      }
    }
  }
</style>
