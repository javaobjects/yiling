<template>
  <div class="single-image-upload" :style="{'width': width, 'height': height}">
    <el-upload
      ref="upload"
      multiple
      :show-file-list="false"
      class="image-uploader"
      :drag="drag"
      action="http"
      :http-request="uploadFile"
      :on-error="handleError"
      :on-exceed="onExceed"
      :on-success="successUpload"
      :before-upload="beforeUpload"
      :limit="limit"
      :disabled="loading"
      :style="{'width': width, 'height': height}"
    >
      <img v-if="url || defaultUrl" :src="url || defaultUrl" class="avatar" style="object-fit: contain;" :style="{'width': width, 'height': height}">
      <div v-else class="flex-row-center up-box">
        <div>
          <i class="el-icon-plus avatar-uploader-icon"></i>
          <div class="uploader-text">
            上传图片
          </div>
        </div>
      </div>
    </el-upload>
  </div>
</template>

<script>
import request from '@/subject/admin/utils/request'
import { getOssKey } from '@/subject/admin/api/common'
import axios from 'axios'

export default {
  name: 'SingleImageUpload',
  props: {
    extralData: {
      type: Object,
      default: () => {}
    },
    // 请求地址
    action: {
      type: String,
      default: '/system/api/v1/file/upload'
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
    defaultUrl: {
      type: String,
      default: ''
    },
    // 传入宽度，高度
    width: {
      type: String,
      default: '124px'
    },
    height: {
      type: String,
      default: '124px'
    },
    // 上传图片大小限制 这里是不超过多少k(以 KB 为单位),默认为 4096KB 也就是 4M
    maxSize: {
      type: Number,
      default: 4096
    },
    // 有值则是oss直接上传，值是后台给的上传type
    ossKey: {
      type: String,
      default: ''
    },
    // 上传个数限制
    limitWidth: {
      type: Number,
      default: 0
    },
    limitHeight: {
      type: Number,
      default: 0
    }
  },
  data() {
    return {
      loading: false,
      url: ''
    }
  },
  mounted() {
  },
  methods: {
    // 上传前
    beforeUpload(file) {
      return new Promise((resolve, reject) => {
        // this.$log(file)
        const isIMAGE = file.type.indexOf('image') !== -1
        // const isLt1M = file.size / 1024 / 1024 < 4
        const isLt1M = file.size / 1024 < this.maxSize
        if (!isIMAGE) {
          this.$common.warn('上传文件只能是图片格式!')
          // eslint-disable-next-line prefer-promise-reject-errors
          return reject(false)
        } else if (!isLt1M) {
          this.$common.warn(`上传文件大小不能超过 ${this.maxSize < 1024 ? this.maxSize + 'KB' : this.maxSize / 1024 + 'MB' } !`)
          // eslint-disable-next-line prefer-promise-reject-errors
          return reject(false)
        } else if (this.limitWidth && this.limitHeight) {
          this.$log('this.limitWidth:', this.limitWidth)
          //调用[限制图片尺寸]函数
          this.limitFileWH(this.limitWidth, this.limitHeight, file).then((res) => {
            file.isFlag = res;
            if (file.isFlag) {
              return resolve(true)
            } else {
              // eslint-disable-next-line prefer-promise-reject-errors
              return reject(false)
            }
          });
        } else {
          return resolve(true);
        }
      })
    },
    //限制图片尺寸
    limitFileWH(limitWidth, limitHeight, file) {
      let width = limitWidth;
      let height = limitHeight;
      const isSize = new Promise(function (resolve, reject) {
        let _URL = window.URL || window.webkitURL;
        let image = new Image();
        image.onload = function () {
          let valid = image.width == width && image.height == height;
          valid ? resolve() : reject();
        };
        image.src = _URL.createObjectURL(file);
      }).then(
        () => {
          return true;
        },
        () => {
          this.$common.error(`上传图片尺寸不符合,只能是宽${limitWidth}*高${limitHeight}`);
          return false;
        }
      );
      return isSize
    },
    // 超过上传个数限制
    onExceed(files, fileList) {
      this.$common.warn(`一次最多只能上传${this.limit}个文件`)
    },
    // 上传成功
    successUpload(response, file, fileList) {
      this.$refs.upload.clearFiles()
    },
    handleError(err, file, fileList) {
      this.$refs.upload.clearFiles()
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
        const src = `${ossData.domain}/system/api/v1/file/getUrl?type=${this.ossKey}&key=${key}`
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
          this.url = imgData.data.data
          this.$emit('onSuccess', {
            key: key,
            url: imgData.data.data,
            name: params.file.name,
            desc: '开发人员注意：oss直接上传后url值仅供展示使用，切勿用作接口入参'
          })
        }
      } else {
        formData.append('file', file)
        let imgData = await request({
          url: this.action,
          method: 'post',
          data: formData,
          onUploadProgress: (event) => {
            this.percent = (event.loaded / event.total).toFixed(2) * 100
          }
        })
        if (imgData) {
          this.url = imgData.url
          this.$emit('onSuccess', imgData)
        }
      }
      this.loading = false
    },
    rmImage() {
      // this.imgList.splice(0, 1)
      // this.$emit('onSuccess', null)
    },
    async getOssKeyId() {
      if (this.ossKey) {
        const ossKey = `yl_oss_ssc_${process.env.NODE_ENV}_${this.ossKey}`
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
.single-image-upload {
  width: 124px;
  height: 124px;
  position: relative;
  .image-uploader {
    width: 100%;
    height: 100%;
    .el-upload {
      width: 100%;
      height: 100%;
      border-radius: 2px;
      .el-upload-dragger {
        width: 100%;
        height: 100%;
        background: rgba(0, 0, 0, 0.04);
      }
    }
    .up-box {
      width: 100%;
      height: 100%;
    }
    .avatar-uploader-icon {
      font-size: 22px;
      color: rgba(0, 0, 0, 0.45);
      width: 30px;
      height: 30px;
      text-align: center;
    }
    .uploader-text {
      color: rgba(0, 0, 0, 0.65);
      font-size: 14px;
      padding-top: 12px;
    }
    .avatar {
      width: 124px;
      height: 124px;
    }
  }
  /*.image-uploader {*/
  /*  height: 100%;*/
  /*}*/
  /*.el-upload {*/
  /*  .el-upload-dragger {*/
  /*    width: 100%;*/
  /*    height: 100px;*/
  /*    .el-icon-upload {*/
  /*      font-size: 50px;*/
  /*      color: #C0C4CC;*/
  /*      margin: 20px;*/
  /*      line-height: 30px;*/
  /*    }*/
  /*  }*/
  /*}*/

  /*.image-preview {*/
  /*  width: 100%;*/
  /*  height: 100%;*/
  /*  position: absolute;*/
  /*  left: 0px;*/
  /*  top: 0px;*/
  /*  border: 1px dashed #d9d9d9;*/
  /*  .image-preview-wrapper {*/
  /*    position: relative;*/
  /*    width: 100%;*/
  /*    height: 100%;*/
  /*    img {*/
  /*      width: 100%;*/
  /*      height: 100%;*/
  /*    }*/
  /*  }*/
  /*  .image-preview-action {*/
  /*    position: absolute;*/
  /*    width: 100%;*/
  /*    height: 100%;*/
  /*    left: 0;*/
  /*    top: 0;*/
  /*    cursor: default;*/
  /*    text-align: center;*/
  /*    color: #fff;*/
  /*    opacity: 0;*/
  /*    font-size: 20px;*/
  /*    background-color: rgba(0, 0, 0, .5);*/
  /*    transition: opacity .3s;*/
  /*    cursor: pointer;*/
  /*    text-align: center;*/
  /*    line-height: 200px;*/
  /*    .el-icon-delete {*/
  /*      font-size: 36px;*/
  /*    }*/
  /*  }*/
  /*  &:hover {*/
  /*    .image-preview-action {*/
  /*      opacity: 1;*/
  /*    }*/
  /*  }*/
  /*}*/
}
</style>
