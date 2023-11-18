<template>
  <div style="display: inline-block;">
    <div class="contain">
      <div>
        <i class="el-icon-warning"></i>
      </div>
      <div>
        <slot></slot>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'ToolTip',
    props: {
      width: {
        type: String,
        default: ''
      }
    },
    computed: {
    },
    data() {
      return {
      }
    },
    created() {

    },
    methods: {
    }
  }
</script>

<style lang="scss" scoped>
  .contain {
    display: flex;
    flex-direction: row;
    justify-content: flex-start;
    align-items: flex-start;
    padding: 8px 16px;
    font-size: $font-size-base;
    line-height: $font-size-base-lh;
    color: $font-important-color;
    background: #FFFBE6;
    border-radius: 4px;
    border: 1px solid #FFE58F;
    i {
      width: 21px;
      height: 100%;
      color: #FA8C15 !important;
    }
  }
</style>
