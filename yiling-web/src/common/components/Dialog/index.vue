<template>
  <el-dialog
    class="yl-dialog-hs"
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
    <div class="container">
      <slot></slot>
    </div>
    <div slot="footer" class="footer" v-if="showFooter">
      <slot name="left-btn"></slot>
      <yl-button v-if="showCancle" plain @click="closeModal">
        {{ leftBtnName }}
      </yl-button>
      <yl-button v-if="showConfirm" type="primary" @click="handleConfirm">
        {{ rightBtnName }}
      </yl-button>
      <slot name="right-btn"></slot>
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
        default: '50%'
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
      leftBtnName: {
        type: String,
        default: '取消'
      },
      rightBtnName: {
        type: String,
        default: '确认'
      },
      // 点击确认按钮是否关闭弹框
      clickRightBtnClose: {
        type: Boolean,
        default: false
      },
      // 是否展示底部
      showFooter: {
        type: Boolean,
        default: true
      },
      // 是否展示确认
      showConfirm: {
        type: Boolean,
        default: true
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
      closeModal() {
        if (this.show) {
          this.$emit('update:visible', false)
        }
      },
      handleConfirm() {
        this.$emit('confirm', this.closeModal)
        if (this.clickRightBtnClose) {
          this.closeModal()
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
        // this.$refs.select.blur()
      }
    }
  }
</script>

<style lang="scss">
  .yl-dialog-hs {
    .container {
      // padding: 16px 16px 0 16px;
      /*box-shadow: 0px 2px 12px 0px rgba(100, 101, 102, 0.12);*/
      /*border-radius: 4px;*/
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
