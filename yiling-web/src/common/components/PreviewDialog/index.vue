<template>
  <el-dialog
    class="yl-preview-dialog"
    v-if="show"
    v-el-drag-dialog
    :title="title"
    :show-close="true"
    :modal-append-to-body="modalAppendToBody"
    :append-to-body="appendToBody"
    :destroy-on-close="destroyOnClose"
    :close-on-click-modal="false"
    :modal="modal"
    :width="width"
    :fullscreen="fullscreen"
    :top="top"
    :center="false"
    @open="open"
    @opened="opened"
    @close="close"
    @closed="closed"
    @dragDialog="handleDrag"
    :visible.sync="show">
    <div class="container" v-dompurify-html="content">
    </div>
  </el-dialog>
</template>

<script>
  import elDragDialog from '@/common/directive/elDragDialog'
  export default {
    name: 'YlDialog',
    directives: { elDragDialog },
    props: {
      // 展示顶部关闭按钮
      showClose: {
        type: Boolean,
        default: true
      },
      // 展示取消按钮
      showCancle: {
        type: Boolean,
        default: true
      },
      visible: {
        type: Boolean,
        default: false
      },
      // 标题
      title: {
        type: String,
        default: ''
      },
      width: {
        type: String,
        default: '375px'
      },
      // 距离顶部距离
      top: {
        type: String,
        default: '14vh'
      },
      modal: {
        type: Boolean,
        default: true
      },
      fullscreen: {
        type: Boolean,
        default: false
      },
      modalAppendToBody: {
        type: Boolean,
        default: true
      },
      appendToBody: {
        type: Boolean,
        default: false
      },
      // 关闭时销毁
      destroyOnClose: {
        type: Boolean,
        default: true
      },
      content: {
        type: String,
        default: ''
      }
    },
    computed: {
      show: {
        get() {
          return this.visible
        },
        set(val) {
          this.$emit('update:visible', val)
        }
      }
    },
    methods: {
      showModal() {
        if (!this.show) {
          this.$emit('update:visible', true)
        }
      },
      open() {
        this.$emit('open')
      },
      opened() {
        this.$emit('opened')
      },
      close() {
        this.$emit('close')
      },
      closed() {
        this.$emit('closed')
      },
      handleDrag() {
      }
    }
  }
</script>

<style lang="scss">
  .yl-preview-dialog {
    .container{
      height: 696px;
      /* table 样式 */
      table {
        border-top: 1px solid #ccc;
        border-left: 1px solid #ccc;
      }
      table td,
      table th {
        border-bottom: 1px solid #ccc;
        border-right: 1px solid #ccc;
        padding: 3px 5px;
      }
      table th {
        border-bottom: 2px solid #ccc;
        text-align: center;
      }

      /* blockquote 样式 */
      blockquote {
        display: block;
        border-left: 8px solid #d0e5f2;
        padding: 5px 10px;
        margin: 10px 0;
        line-height: 1.4;
        font-size: 100%;
        background-color: #f1f1f1;
      }

      /* code 样式 */
      code {
        display: inline-block;
        *display: inline;
        *zoom: 1;
        background-color: #f1f1f1;
        border-radius: 3px;
        padding: 3px 5px;
        margin: 0 3px;
      }
      pre code {
        display: block;
      }

      /* ul ol 样式 */
      ul, ol {
        margin: 10px 0 10px 20px;
      }
    }
    .el-dialog__header {
      position: relative;
      border-radius: 14px 14px 0 0;
      padding: 12px 0;
      border-bottom: 1px solid #F0F0F0;
      text-align: center;
      .el-dialog__title {
        height: 24px;
        font-size: 16px;
        font-weight: 500;
        color: $font-important-color;
        line-height: 24px;
      }
      .el-dialog__headerbtn {
        position: absolute;
        top: 10px;
        right: 12px;
        height: 28px;
        width: 28px;
        padding: 0;
        background: transparent;
        border: none;
        outline: none;
        cursor: pointer;
        font-size: 18px;
        line-height: 28px;
        &:hover {
          background: #F2FBFF;
          border-radius: 4px;
        }
      }
    }
    .el-dialog__body {
      padding: 0;
      color: #606266;
      font-size: 14px;
      word-break: break-all;
      max-height: 70vh;
      overflow: auto;
    }
    .el-dialog__footer {
      padding: 0;
    }
    .footer {
      padding: 8px 16px;
      text-align: right;
      border-top: 1px solid #F0F0F0;
    }
  }
</style>
