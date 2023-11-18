<template>
  <div class="packing-box">
    <div v-for="(item, index) in otherList" :key="`${index}-other`">
      <div v-if="item.type === 'image'">
        <div class="packing-img" :style="{ 'width': width, 'height': height, 'margin-right': marginRight }">
          <el-image class="packing-image" fit="contain" :src="item.url" :lazy="true"></el-image>
          <div class="packing-cover">
            <i class="el-icon-zoom-in" @click="showImageViewer(item)"></i>
            <i class="el-icon-delete" v-if="!item.hideDel" @click="deleteFile(item)"></i>
          </div>
        </div>
        <div :style="{ 'width': width }" class="font-size-sm font-title-color clamp-text-view" :title="item.name" v-if="showName && item.name">{{ item.name.split('.')[0] }}</div>
      </div>
      <div v-else>
        <div class="packing-img" :style="{ 'width': width, 'height': height , 'margin-right': marginRight }">
          <img class="packing-image" src="@/common/assets/file_images/pdf.png" fit="contain" v-if="item.type === 'pdf'"/>
          <img class="packing-image" src="@/common/assets/file_images/word.png" fit="contain" v-if="item.type === 'word'"/>
          <img class="packing-image" src="@/common/assets/file_images/excel.png" fit="contain" v-if="item.type === 'excel'"/>
          <img class="packing-image" src="@/common/assets/file_images/ppt.png" fit="contain" v-if="item.type === 'ppt'"/>
          <img class="packing-image" src="@/common/assets/file_images/video.png" fit="contain" v-if="item.type === 'video'"/>
          <img class="packing-image" src="@/common/assets/file_images/radio.png" fit="contain" v-if="item.type === 'radio'"/>
        <img class="packing-image" src="@/common/assets/file_images/txt.png" fit="contain" v-if="item.type === 'txt'"/>
        <img class="packing-image" src="@/common/assets/file_images/other.png" fit="contain" v-if="item.type === 'other'"/>
          <div class="packing-cover">
            <i class="el-icon-view" @click="downloadClick(item, index)"></i>
            <i class="el-icon-delete" v-if="!item.hideDel" @click="deleteFile(item)"></i>
          </div>
        </div>
        <div :style="{ 'width': width }" class="font-size-sm font-title-color clamp-text-view" :title="item.name" v-if="showName && item.name">{{ item.name.split('.')[0] }}</div>
      </div>
    </div>
    <image-viewer
      v-if="viewer.show"
      :url-list="viewer.list"
      :on-close="onClose"
      :initial-index="viewer.index"
      />
  </div>
</template>

<script>
 /*
  *示例
  * <upload-file-list width="80px" height="80px" :file-list.sync="fileList"></upload-file-list>
  */
  import imageViewer from 'element-ui/packages/image/src/image-viewer'

  export default {
    name: 'FileList',
    components: {
      imageViewer
    },
    props: {
      /*
        文件数组，示例
        [
          {url: '文件地址', name: 'xxx.png'},
          {url: '文件地址', name: 'xxx.pdf'},
          {url: '文件地址', name: 'xxx.xls'}
        ]
      */
      fileList: {
        type: Array,
        default: () => []
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
      },
      // 列表间距
      marginRight: {
        type: String,
        default: '10px'
      },
      // 展示文件名称
      showName: {
        type: Boolean,
        default: false
      }
    },
    data() {
      return {
        viewer: {
          list: [],
          show: false,
          showViewerIndex: 1
        }
      }
    },
    computed: {
      otherList() {
        return Object.assign([], this.fileList.filter(item => {
          item.type = this.matchType(item.url)
            return item
        }))
      }
    },
    mounted() {
    },
    methods: {
      // 显示图片预览
      showImageViewer(item) {
        this.viewer.list = Object.assign([], this.otherList.filter(i => i.type === 'image').map(i => { return i.url }))
        this.viewer.show = true
        this.viewer.index = this.viewer.list.findIndex(i => i === item.url)
      },
      // 预览关闭
      onClose() {
        this.viewer.list = []
        this.viewer.show = false
      },
      // 删除图片或文件
      deleteFile(item) {
        const list = Object.assign([], this.fileList)
        const index = this.fileList.findIndex(i => i.url === item.url)
        if (index > -1) {
          list.splice(index, 1)
          this.$emit('update:fileList', list)
        }
      },
      // 点击下载/预览文件
      downloadClick(item) {
        const xRequest = new XMLHttpRequest()
        xRequest.open('GET', item.url, true)
        xRequest.responseType = 'blob'
        xRequest.onload = () => {
          const url = window.URL.createObjectURL(xRequest.response)
          const a = document.createElement('a')
          a.href = url
        if (item.name) {
            a.download = item.name
        }
          a.click()
        }
        xRequest.send()
      },
      /*
       * @param: fileUrl - 修改为文件地址
       * @param: 数据返回 1) 无后缀匹配 - false
       * @param: 数据返回 2) 匹配图片 - image
       * @param: 数据返回 3) 匹配 txt - txt
       * @param: 数据返回 4) 匹配 excel - excel
       * @param: 数据返回 5) 匹配 word - word
       * @param: 数据返回 6) 匹配 pdf - pdf
       * @param: 数据返回 7) 匹配 ppt - ppt
       * @param: 数据返回 8) 匹配 视频 - video
       * @param: 数据返回 9) 匹配 音频 - radio
       * @param: 数据返回 10) 其他匹配项 - other
       */
      matchType(fileUrl) {
        // 后缀获取
        let suffix = ''
        // 获取类型结果
        let result = ''
        try {
          const flieArr = fileUrl.split('.')
          suffix = flieArr[flieArr.length - 1].toLowerCase()
        } catch (err) {
          suffix = ''
        }
        // fileUrl无后缀返回 false
        if (!suffix) {
          result = false
          return result
        }
        // 图片格式
        const imglist = ['png', 'jpg', 'jpeg', 'bmp', 'gif']
        // 进行图片匹配
        result = imglist.some(function (item) {
          return item === suffix
        })
        if (result) {
          result = 'image'
          return result
        }
        // 匹配txt
        const txtlist = ['txt']
        result = txtlist.some(function (item) {
          return item === suffix
        })
        if (result) {
          result = 'txt'
          return result
        }
        // 匹配 excel
        const excelist = ['xls', 'xlsx']
        result = excelist.some(function (item) {
          return item === suffix
        })
        if (result) {
          result = 'excel'
          return result
        }
        // 匹配 word
        const wordlist = ['doc', 'docx']
        result = wordlist.some(function (item) {
          return item === suffix
        })
        if (result) {
          result = 'word'
          return result
        }
        // 匹配 pdf
        const pdflist = ['pdf']
        result = pdflist.some(function (item) {
          return item === suffix
        })
        if (result) {
          result = 'pdf'
          return result
        }
        // 匹配 ppt
        const pptlist = ['ppt', 'pptx']
        result = pptlist.some(function (item) {
          return item === suffix
        })
        if (result) {
          result = 'ppt'
          return result
        }
        // 匹配 视频
        const videolist = ['mp4', 'm2v', 'mkv']
        result = videolist.some(function (item) {
          return item === suffix
        })
        if (result) {
          result = 'video'
          return result
        }
        // 匹配 音频
        const radiolist = ['mp3', 'wav', 'wmv']
        result = radiolist.some(function (item) {
          return item === suffix
        })
        if (result) {
          result = 'radio'
          return result
        }
        // 其他 文件类型
        result = 'other'
        return result
      }
    }
  }
</script>

<style lang="scss" scoped>
  .packing-box {
    flex-wrap: wrap;
    display: flex;
    flex-direction: row;
    justify-content: flex-start;
    align-items: flex-start;
    .clamp-text-view {
      overflow: hidden;
      text-overflow: ellipsis;
      display: -webkit-box;
      -webkit-box-orient: vertical;
      -webkit-line-clamp: 2;
      white-space: normal;
      max-height: 36px;
      line-height: 18px;
      text-align: center;
      word-break:break-all;
      word-wrap:break-word;
    }
    .packing-img {
      position: relative;
      border-radius: 6px;
      overflow: hidden;
      border: 1px dashed $border-line;
      margin-bottom: 8px;
      .packing-image {
        width: 100%;
        height: 100%;
      }
      .packing-cover {
        position: absolute;
        left: 0px;
        top: 0px;
        display: flex;
        align-items: center;
        justify-content: space-between;
        border-radius: 6px;
        background: rgba(0, 0, 0, 0.4);
        width: 100%;
        height: 100%;
        box-sizing: border-box;
        padding: 0 10px;
        filter: alpha(opacity=60);
        opacity: 0;
        z-index: 999;
        color: #fff;
        font-size: 20px;
        i {
          cursor: pointer;
        }
        &:hover {
          opacity: 0.6;
        }
      }
    }
  }
</style>
