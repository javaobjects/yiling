<template>
  <div class="multiple-image-upload-q">
    <el-upload
      ref="upload"
      action="#"
      multiple
      list-type="picture-card"
      :http-request="uploadFile"
      :on-error="handleError"
      :on-exceed="onExceed"
      :on-success="successUpload"
      :before-upload="beforeUpload"
      :limit="maxLimit"
      :disabled="disabled">
      <div slot="default" class="multiple-up-box flex-row-center">
        <div class="box" @click="handleClick">
          <i class="el-icon-plus upload-icon"></i>
          <div class="upload-text">
            上传图片
          </div>
        </div>
      </div>
      <div slot="file" slot-scope="{file}">
        <div class="img-box">
          <img class="el-upload-list__item-thumbnail" :src="file.url" alt="">
          <div class="sign" v-show="file.status === 'success'"><i class="el-icon-circle-check"></i></div>
        </div>
        <span class="el-upload-list__item-actions">
          <span @click="handlePictureCardPreview(file)">
            <i class="el-icon-zoom-in"></i>
          </span>
          <span
            v-if="canRemove"
            @click="handleRemove(file)">
            <i class="el-icon-delete"></i>
          </span>
        </span>
      </div>
    </el-upload>
    <el-dialog :visible.sync="dialogVisible">
      <img width="100%" :src="dialogImageUrl" alt="">
    </el-dialog>
  </div>
</template>

<script>
  import request from '@/subject/pop/utils/request'
  import { getOssKey } from '@/subject/pop/api/common'
  import axios from 'axios'

  export default {
    name: 'MultipleImageUpload',
    props: {
      extralData: {
        type: Object,
        default: () => {}
      },
      // 请求地址
      action: {
        type: String,
        default: '/admin/dataCenter/api/v1/file/upload'
      },
      // 上传个数限制
      limit: {
        type: Number,
        default: 5
      },
      // 默认图片
      defaultList: {
        type: Array,
        default: () => []
      },
      // 是否可删除
      canRemove: {
        type: Boolean,
        default: false
      },
      // 有值则是oss直接上传，值是后台给的上传type
      ossKey: {
        type: String,
        default: ''
      }
    },
    data() {
      return {
        disabled: false,
        dialogImageUrl: '',
        dialogVisible: false,
        uploadList: []
      }
    },
    computed: {
      maxLimit() {
        let num = this.limit - this.defaultList.length
        return num < 0 ? 0 : num
      }
    },
    methods: {
      // 上传前
      beforeUpload(file) {
        // this.$log(file)
        const isIMAGE = file.type.indexOf('image') !== -1
        const isLt1M = file.size / 1024 / 1024 < 4
        if (!isIMAGE) {
          this.$common.warn('上传文件只能是图片格式!')
          return false
        }
        if (!isLt1M) {
          this.$common.warn('上传文件大小不能超过 4MB!')
          return false
        }
        if (this.maxLimit <= 0) {
          this.$common.warn('无法继续上传，超出限制')
          return false
        }
        this.uploadList = []
        return true
      },
      handleClick() {
        this.$refs.upload.clearFiles()
      },
      // 超过上传个数限制
      onExceed(files, fileList) {
        this.$common.warn(`您本次最多只能上传${this.maxLimit}张图片，限制${this.limit}张`)
      },
      // 上传成功
      successUpload(response, file, fileList) {
        this.disabled = false
        if (this.uploadList.length === fileList.length) {
          this.$common.hideLoad()
          // 上传成功
          this.$emit('onSuccess', this.uploadList)
        }
      },
      handleError(err, file, fileList) {
        this.disabled = false
        this.$common.hideLoad()
        this.$refs.upload.clearFiles()
      },
      async uploadFile(params) {
        this.disabled = true
        let file = params.file
        let formData = new FormData()
        if (this.extralData && Object.keys(this.extralData).length) {
          Object.keys(this.extralData).map(key => {
            let value = this.extralData[key]
            formData.append([key], value)
          })
        }
        this.$common.showLongLoad()
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
            }
          })
          if (imgData.data.code === 200) {
            const obj = {
              key: key,
              url: imgData.data.data,
              name: params.file.name,
              desc: '开发人员注意：oss直接上传后url值仅供展示使用，切勿用作接口入参'
            }
            this.uploadList.push(obj)
          } else {
            this.$common.hideLoad()
          }
        } else {
          formData.append('file', file)
          let imgData = await request({
            url: this.action,
            method: 'post',
            data: formData
          })
          if (imgData) {
            this.uploadList.push(imgData)
          } else {
            this.$common.hideLoad()
          }
        }
      },
      handleRemove(file) {
        this.$log(file)
        // this.imgList.splice(0, 1)
        // this.$emit('onSuccess', null)
      },
      handlePictureCardPreview(file) {
        this.dialogImageUrl = file.url
        this.dialogVisible = true
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
  .multiple-image-upload-q {
    position: relative;
    .el-upload--picture-card {
      width: 124px;
      height: 124px;
      line-height: unset;
      background: rgba(0, 0, 0, 0.04);
    }
    .el-upload-list__item,
    .el-upload-list__item-thumbnail,
    .el-upload-list__item-actions {
      width: 124px;
      height: 124px;
    }
    .img-box {
      width: 124px;
      height: 124px;
      position: relative;
      .sign {
        position: absolute;
        pointer-events: none;
        right: 5px;
        top: 5px;
        .el-icon-circle-check {
          font-size: 20px;
          color: $success;
        }
      }
    }
    .multiple-up-box {
      width: 124px;
      height: 124px;
      .box {
        margin: 0 auto;
        .upload-icon {
          font-size: 22px;
          color: rgba(0, 0, 0, 0.45);
          width: 30px;
          height: 30px;
          text-align: center;
        }
        .upload-text {
          color: rgba(0, 0, 0, 0.65);
          font-size: 14px;
          margin-top: 12px;
          height: 22px;
        }
      }
    }
  }
</style>
