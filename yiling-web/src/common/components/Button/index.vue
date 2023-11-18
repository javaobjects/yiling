<template>
  <el-button
    :class="[btnClass, edge ? 'yl-button' : '']"
    :size="size"
    :disabled="disabled"
    :type="type"
    :icon="icon"
    :plain="plain"
    :round="round"
    :circle="circle"
    :loading="loading"
    :autofocus="autofocus"
    :native-type="nativeType"
    v-debounce.click="handleClick">
    <slot />
  </el-button>
</template>

<script>
  import Debounce from '@/common/directive/debounce'

  export default {
    name: 'Button',
    directives: {
      Debounce
    },
    props: {
      // 样式
      btnClass: {
        type: String,
        default: ''
      },
      size: {
        type: String,
        default: 'medium'
      },
      disabled: {
        type: Boolean,
        default: false
      },
      type: {
        type: String,
        default: ''
      },
      icon: {
        type: String,
        default: ''
      },
      plain: {
        type: Boolean,
        default: false
      },
      round: {
        type: Boolean,
        default: false
      },
      circle: {
        type: Boolean,
        default: false
      },
      loading: {
        type: Boolean,
        default: false
      },
      autofocus: {
        type: Boolean,
        default: false
      },
      nativeType: {
        type: String,
        default: 'button'
      }
    },
    computed: {
      edge() {
        return this.$common.browser() === 'Edge'
      }
    },
    methods: {
      handleClick() {
        this.$emit('click')
      }
    }
  }
</script>

<style lang="scss" scoped>
  .yl-button {
    &.el-button--text {
      color: #1790FF !important;
    }
    &.is-disabled {
      &.el-button--text {
        color: $btn-disable !important;
        background-color: transparent;
        border-color: transparent;
      }
    }
  }
  /*$normal: #1790FF;
  $hover: $btn-hover;
  $active: $btn-active;
  $disable: $btn-disable;

  .yl-button {
    display: inline-block;

    &.el-button--primary {
      background: $normal;
      border-color: $normal;
      color: $white;

      &:hover {
        background: $hover;
        border-color: $hover;
        color: $white;
      }

      &:active {
        background: $active;
        border-color: $active;
        color: $white;
        outline: none;
      }
    }

    &.el-button--text {
      background: transparent;
      border-color: transparent;
      color: $normal;
      &:hover {
        color: $hover;
      }
      &:active {
        color: $active;
      }
    }

    &.is-disabled {
      color: #FFFFFF;
      background-color: $disable;;
      border-color: $disable;
      &:hover,
      &:focus,
      &:active {
        color: #FFFFFF;
        background-color: $disable;;
        border-color: $disable;
        outline:0
      }
      &.is-plain {
        color: $disable;
        background-color: transparent;
        border-color: $disable;
        &:hover,
        &:focus,
        &:active {
          color: $disable;
          background-color: transparent;
          border-color: $disable;
        }
      }
      &.el-button--text {
        color: $disable;
        background-color: transparent;
        border-color: transparent;
      }
    }

    &.is-plain {
      color: $normal;
      background-color: transparent;
      border-color: rgba(23, 144, 255, 0.5);
      &:hover {
        background: #F2FBFF;
        border-color: $hover;
        color: $hover;
      }
      &:active {
        background: #CCECFF;
        border-color: rgba(23, 144, 255, 0.29);
        color: $active;
      }
    }

    &.el-button + .el-button {
      margin-left: 16px;
    }
    &.el-button--small + .el-button--small {
      margin-left: 12px;
    }
    &.el-button--mini + .el-button--mini {
      margin-left: 8px;
    }
  }*/
</style>
