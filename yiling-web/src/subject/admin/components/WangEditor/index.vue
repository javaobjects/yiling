<template>
  <div>
    <div ref="editor" class="editor-view"></div>
  </div>
</template>

<script>
  import E from 'wangeditor'
  import PaddingMenu from './padBtn'
  import { getOssKey } from '@/subject/admin/api/common'
  import axios from 'axios'

  export default {
    name: 'WangEditor',
    data () {
      return {
        editor: null,
        editorContent: ''
      }
    },
    props: {
      // 回调内容
      handleContent: {
        type: Function,
        default: () => {}
      },
      // 传入内容
      content: {
        type: String,
        default: ''
      },
      // 额外参数
      extralData: {
        type: Object,
        default: () => {}
      },
      // 高度
      height: {
        type: Number,
        default: 300
      }
    },
    watch: {
      content () {
        this.editor.txt.html(this.content)
      }
    },
    mounted () {
      const menuKey = 'paddingMenu'
      // 注册菜单
      E.registerMenu(menuKey, PaddingMenu)

      this.editor = new E(this.$refs.editor)
      this.editor.config.onchange = (html) => {
        this.editorContent = html
        this.handleContent(this.editorContent, this.editor)
      }
      this.editor.config.placeholder = '请编辑内容'
      // 最大上传张数
      this.editor.config.uploadImgMaxLength = 9
      // this.editor.config.uploadImgServer = '你的上传图片的接口'
      // this.editor.config.uploadFileName = '你自定义的文件名'
      // 样式过滤关闭
      this.editor.config.pasteFilterStyle = false
      // 忽略粘贴图片
      // this.editor.config.pasteIgnoreImg = true
      // 设置编辑区域高度为 500px
      this.editor.config.height = this.height
      this.editor.config.zIndex = 10
      // 菜单配置
      this.editor.config.menus = [
        // 标题
        'head',
        // 粗体
        'bold',
        // 字号
        'fontSize',
        // 字体
        'fontName',
        // 斜体
        'italic',
        // 下划线
        'underline',
        // 删除线
        'strikeThrough',
        //缩进
        // 'indent',
        //行高
        'lineHeight',
        // 文字颜色
        'foreColor',
        // 背景颜色
        'backColor',
        // 插入链接
        'link',
        // 列表
        'list',
        // 对齐方式
        'justify',
        // 引用
        'quote',
        // 表情
        'emoticon',
        // 插入图片
        'image',
        // 表格
        'table',
        // 插入代码
        // 'code',
        // 撤销
        'undo',
        // 重复
        'redo'
      ]
      // 自定义上传方法
      this.editor.config.customUploadImg = (files, insertImgFn) => {
        if (files && files.length) {
          let fileArray = this.handleFileSize(files)
          fileArray = fileArray.map(item => {
            return this.uploadFile(item)
          })
          Promise.all(fileArray).then(datas => {
            datas.map(item => {
              insertImgFn(item)
            })
          })
        }
      }
      this.editor.config.customAlert = function (s, t) {
        switch (t) {
          case 'success':
            this.$common.success(s)
            break
          case 'info':
            this.$common.message(s)
            break
          case 'warning':
            this.$common.warn(s)
            break
          case 'error':
            this.$common.error(s)
            break
          default:
            this.$common.message(s)
            break
        }
      }
      // 创建富文本实例
      this.editor.create()
    },
    methods: {
      handleFileSize(files) {
        // const isIMAGE = file.type.indexOf('image') !== -1
        let array = []
        files.forEach(file => {
          // 限制图片6M
          const isLt4M = file.size / 1024 / 1024 < 6
          if (isLt4M) {
            array.push(file)
          } else {
            this.$common.warn(`${file.name}因超出6MB无法上传，已被过滤`)
          }
        })
        return array
      },
      uploadFile(file) {
        return new Promise((resolve, reject) => {
          let formData = new FormData()
          if (this.extralData && Object.keys(this.extralData).length) {
            Object.keys(this.extralData).map(key => {
              let value = this.extralData[key]
              formData.append([key], value)
            })
          }
          this.getOssKeyId().then(ossData => {
            const key = `${ossData.dir}/${this.$common.getUUID().replace(/-/g, '')}${file.name.substr(file.name.lastIndexOf('.'))}`
            const src = `${ossData.domain}/system/api/v1/file/getUrl?type=${this.extralData.type}&key=${key}`
            formData.append('key', key)
            formData.append('Cache-Control', 'no-cache')
            formData.append('OSSAccessKeyId', ossData.accessKeyId)
            formData.append('policy', ossData.policy)
            formData.append('Signature', ossData.signature)
            formData.append('success_action_redirect', src)
            formData.append('file', file)
            axios.post(ossData.host, formData, {
              headers: {
                'Content-Type': 'multipart/form-data'
              }
            }).then(imgData => {
              if (imgData.data.code === 200) {
                resolve(imgData.data.data)
              } else {
                reject()
              }
            })
          })
        })
      },
      async getOssKeyId() {
        if (this.extralData && this.extralData.type) {
          const ossKey = `yl_oss_ssc_${process.env.NODE_ENV}_${this.extralData.type}`
          const data = this.$common.getSessionCache(ossKey)
          if (data) {
            return data
          } else {
            const data = await getOssKey(this.extralData.type)
            if (data) {
              this.$common.setSessionCache(ossKey, data)
              return data
            }
          }

        }
      },
      disableMethod() {
        this.editor.disable()
      },
      enableMethod() {
        this.editor.enable()
      }
    }
  }
</script>

<style lang="scss" scoped>
  .editor-view {
  }
</style>
