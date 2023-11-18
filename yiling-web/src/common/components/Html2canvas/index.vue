<template>
  <yl-dialog
    title="图片绘制"
    :width="width"
    :show-confirm="!isCreate"
    right-btn-name="立即绘制"
    @confirm="createCanvas"
    :visible.sync="showDialog">
    <div class="canvas-view flex-column-center">
      <div id="canvas" v-show="!isCreate">
        <slot></slot>
      </div>
      <div v-show="isCreate">
        <el-image
          id="done-img"
          class="canvas-img"
          :preview-src-list="[imgUrl]"
          :src="imgUrl"/>
      </div>
    </div>
    <yl-button slot="right-btn" v-show="isCreate" type="primary" @click="downloadImg">
      保存到本地
    </yl-button>
  </yl-dialog>
</template>

<script>
  // https://cdn.jsdelivr.net/npm/qrcodejs2@0.0.2/qrcode.min.js
  const html2canvasCDN = 'https://yl-public.oss-cn-zhangjiakou.aliyuncs.com/web/prd/package/html2canvas.min.js'
  import load from './dynamicLoad'
  /*
  * 背景图片（background: url("")）要转为base64，不然有兼容问题
  * */
  export default {
    name: 'Html2canvas',
    components: {},
    props: {
      // 展示
      show: {
        type: Boolean,
        default: false
      },
      // 弹框宽度
      width: {
        type: String,
        default: '600px'
      }
    },
    data() {
      return {
        imgUrl: '',
        // 是否绘制完成
        isCreate: false
      }
    },
    computed: {
      showDialog: {
        get() {
          if (this.show) {
            this.clearCreate()
          }
          return this.show
        },
        set(val) {
          this.$emit('update:show', val)
        }
      }
    },
    mounted() {
      load(html2canvasCDN, (err) => {
        if (err) {
          this.$common.warn(err.message)
          return
        }
      })
    },
    methods: {
      clearCreate() {
        this.isCreate = false
        this.imgUrl = ''
      },
      // 转为base64图片
      getBase64Image(img) {
        let canvas = document.createElement('canvas')
        canvas.width = img.width
        canvas.height = img.height
        let ctx = canvas.getContext('2d')
        ctx.drawImage(img, 0, 0, canvas.width, canvas.height)
        let dataURL = canvas.toDataURL('image/png')
        return dataURL
      },
      createCanvas(options = {}) {
        this.$common.showLongLoad(null, '.yl-dialog-hs')
        const container = document.querySelector('#canvas')
        const scale = window.devicePixelRatio;
        // 传入节点原始宽高
        const width = container.offsetWidth;
        const height = container.offsetHeight;

        let imgs = container.querySelectorAll('img')
        let count = 0 // 计数用

        // 排除base64图片，因为base64图片不会有跨域问题
        imgs = Array.from(imgs).filter(elem => {
          return !/^data:image/.test(elem.src)
        })
        const createCanvas = () => {
          // html2canvas配置项
          const ops = {
            scale: scale < 2 ? 2 : scale,
            width,
            height,
            useCORS: true,
            allowTaint: false,
            ...options
          }
          window.html2canvas(container, ops).then(canvas => {
            // 返回图片的二进制数据
            this.imgUrl = canvas.toDataURL('image/png')
            this.$common.hideLoad()
            this.isCreate = true
          }).catch(err => {
            console.error(err)
          })
        }
        if (imgs.length) {
          // 将会跨域的图片转为支持跨域base64图片，最后再执行html2canvas
          imgs.forEach((elem, index, arr) => {
            let image = new Image()
            image.crossOrigin = '*' // 支持跨域图片
            image.src = elem.src
            image.onload = () => {
              elem.src = this.getBase64Image(image)
              count++
              // 全部图片加载完毕
              if (count === arr.length) {
                createCanvas()
              }
            }
          })
        } else {
          createCanvas()
        }
      },
      downloadImg() {
        let imgData = this.imgUrl
        const fixImgType = function (type) {
          type = type.toLocaleLowerCase().replace(/jpg/i, 'jpeg')
          return 'image/' + type.match(/png|jpeg|bmp|gif/)[0]
        }
        imgData = imgData.replace(fixImgType('png'), 'image/octet-stream')
        const saveImg = (data, filename) => {
          let saveLink = document.createElementNS('http://www.w3.org/1999/xhtml', 'a')
          saveLink.href = data
          saveLink.download = filename
          let event = document.createEvent('MouseEvents')
          event.initMouseEvent('click', true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null)
          saveLink.dispatchEvent(event)
        }
        const saveImgIe = (url, fileName) => {
          const bstr = atob(url.split(',')[1])
          let n = bstr.length
          const u8arr = new Uint8Array(n)
          while (n--) {
            u8arr[n] = bstr.charCodeAt(n)
          }
          const blob = new Blob([u8arr])
          window.navigator.msSaveOrOpenBlob(blob, fileName)
        }
        const imgName = this.$common.parseTime(new Date()) + '.png'
        const myBrowser = this.myBrowser()
        if (myBrowser === 'IE' || myBrowser === 'Edge'){
          saveImgIe(imgData, imgName)
        } else {
          saveImg(imgData, imgName)
        }
      },
      myBrowser() {
        const userAgent = navigator.userAgent
        const isOpera = userAgent.indexOf('Opera') > -1
        if (isOpera) {
          return 'Opera'
        }
        if (userAgent.indexOf('Firefox') > -1) {
          return 'FF'
        }
        if (userAgent.indexOf('Chrome') > -1){
          return 'Chrome'
        }
        if (userAgent.indexOf('Safari') > -1) {
          return 'Safari'
        }
        if (userAgent.indexOf('compatible') > -1 && userAgent.indexOf('MSIE') > -1 && !isOpera) {
          return 'IE'
        }
        if (userAgent.indexOf('Trident') > -1) {
          return 'Edge'
        }
      }
    }
  }
</script>

<style lang="scss" scoped>
  .canvas-view {
    background: #f2f3f4;

    .canvas-img {
      width: 320px;
      min-height: 568px;
    }
  }
</style>
