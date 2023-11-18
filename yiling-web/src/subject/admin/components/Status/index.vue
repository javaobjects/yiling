<template>
  <yl-button type="text" @click="handleClick">{{ currentStatus === 1 ? '停用' : '启用' }}</yl-button>
</template>

<script>
  import request from '@/subject/admin/utils/request'
  export default {
    name: 'Status',
    props: {
      // 状态 1-启用；2-停用
      status: {
        type: Number,
        default: 2
      },
      // 状态key
      statusKey: {
        type: String,
        default: '',
        required: true
      },
      // 请求url
      url: {
        type: String,
        default: '',
        required: true
      },
      // 请求参数
      data: {
        type: Object,
        default: () => {}
      },
      // 停用弹框确认
      stopMsg: {
        type: String,
        default: ''
      },
      // 弹框文字是否是html
      isHtml: {
        type: Boolean,
        default: false
      }
    },
    computed: {
      currentStatus: {
        get() {
          return this.status
        },
        set(val) {
          this.$emit('update:status', val)
        }
      }
    },
    data() {
      return {
      }
    },
    mounted() {},
    methods: {
      handleClick() {
        if (this.currentStatus === 1) {
          this.downStatus()
        } else if (this.currentStatus === 2) {
          this.startStatus()
        }
      },
      // 启用
      startStatus() {
        this.checkParam(async () => {
          this.$common.showLoad()
          let data = await request({
            url: this.url,
            method: 'post',
            data: Object.assign(this.data, {
              [this.statusKey]: 1
            })
          })
          this.$common.hideLoad()
          if (data && data.result) {
            this.$emit('change', 1)
            this.$common.n_success('启用成功')
          }
        }, 1)
      },
      // 停用
      downStatus() {
        this.checkParam(async () => {
          this.$common.showLoad()
          let data = await request({
            url: this.url,
            method: 'post',
            data: Object.assign(this.data, {
              [this.statusKey]: 2
            })
          })
          this.$common.hideLoad()
          if (data && data.result) {
            this.$emit('change', 2)
            this.$common.n_success('停用成功')
          }
        }, 2)
      },
      // 检查参数
      checkParam(callback, type) {
        if (!this.url) {
          this.$common.error('请求地址为空')
          return
        }
        if (!this.statusKey) {
          this.$common.error('请求参数键为空')
          return
        }
        if (this.stopMsg && type === 2) {
          this.$common.confirm(this.stopMsg, r => {
            if (r) {
              if (callback) callback()
            }
          }, null, null, {
            dangerouslyUseHTMLString: this.isHtml
          })
          return
        }
        if (callback) callback()
      }
    }
  }
</script>

<style lang="scss" scoped>
</style>
