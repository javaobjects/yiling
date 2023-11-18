<template>
  <div class="yl-item flex-row-left flex1">
    <div class="flex-row-center">
      <i v-if="icon.includes('el-icon')" :class="[icon, 'sub-el-icon']" />
      <svg-icon :icon-class="icon" />
    </div>
    <div class="sub-item-span flex-row-left" slot="title">
      <div v-if="title && !icon" class="sub-item-dot mar16"></div>
      {{ title }}
    </div>
  </div>
</template>
<script>
export default {
  name: 'MenuItem',
  // functional: true,
  props: {
    icon: {
      type: String,
      default: ''
    },
    title: {
      type: String,
      default: ''
    }
  }
}
</script>

<style lang="scss" scoped>
  .yl-item {
    /*height: 40px;*/

    .sub-el-icon {
      color: currentColor;
      width: 1em;
      height: 1em;
    }
    .sub-item-span {
      /*display: inline-block;*/
      .sub-item-dot {
        width: 2px;
        height: 2px;
        margin-right: 2px;
        border-radius: 50%;
        overflow: hidden;
        background-color: #fff;
      }
      .mar16 {
        margin-left: 16px;
      }
    }
  }

  /*.el-menu-item {*/
  /*  .sub-item-span {*/
  /*    padding: 0 0 0 16px !important;*/
  /*  }*/
  /*}*/
</style>
