<template>
  <div class="upload_image">
    <el-upload :action="action"
      :http-request="uploadFile"
      list-type="picture-card"
      multiple
      :before-upload="beforeadUrlUpload"
      :on-success="handleAddUrlSucess"
      :on-error="unpladError"
      :on-preview="handlePictureCardPreview"
      :on-remove="handleRemove"
      :limit="limit"
      :file-list="fileList"
      :data="extralData"
      :disabled="loading"
      :show-file-list="showFileList"
    >
      <slot></slot>
      <!-- <div @click="clickUpload(item, index)">
        <i class="el-icon-plus"></i>
      </div> -->
    </el-upload>
  </div>
</template>

<script>
import request from '@/subject/admin/utils/request';
import { getOssKey } from '@/subject/admin/api/common'
import axios from 'axios'

export default {
  name: 'YlUploadImage',
  props: {
    extralData: {
      type: Object,
      default: () => { }
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
    }
  },
  data() {
    return {
      loading: false
    };
  },
  watch: {
    // imgs(newVal, oldVal) {
    //   this.imgList = [].concat(newVal)
    // }
  },
  mounted() {
    // this.imgList = this.imgs
  },
  methods: {
    // 上传前
    beforeadUrlUpload(file) {
      // 上传图片前处理函数
      const isJPG = file.type === 'image/jpeg' || file.type === 'image/png'
      const isLt4M = file.size / 1024 / 1024 < 4;
      if (!isJPG) {
        this.$common.error('上传图片只能是jpg,png格式!');
      }
      if (!isLt4M) {
        this.$common.error('上传头像图片大小不能超过4MB!');
      }
      const isSize = new Promise(function (resolve, reject) {
        let width = 720;
        let height = 720;
        let _URL = window.URL || window.webkitURL;
        let image = new Image();
        image.onload = function () {
          let valid = image.width == width && image.height == height;
          valid ? resolve() : reject();
        };
        image.src = _URL.createObjectURL(file);
      }).then(
        () => {
          return file;
        },
        () => {
          this.$common.error('上传图片尺寸不符合,只能是720*720');
          return Promise.reject();
        }
      );
      return isJPG && isLt4M && isSize;
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
          this.$emit('onSuccess', imgData)
        }
      }
      this.loading = false
    }
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
};
</script>

<style lang="scss" scoped>
.upload_image {
  ::v-deep .el-upload-list__item {
    width: 80px;
    height: 80px;
    // margin: 0 !important;
    margin-right: 8px;

    // .el-upload-list--picture-card .el-upload-list__item
  }
  ::v-deep .el-upload-list--picture-card .el-upload-list__item {
  }
  ::v-deep .el-upload--picture-card {
    width: 80px !important;
    height: 80px !important;
    line-height: 80px !important;
    // margin-top: 10px;
  }
}
</style>
