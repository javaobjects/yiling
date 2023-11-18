<template>
  <div class="step-box flex-row-center">
    <div
      v-for="(item, index) in steps"
      :key="index"
      class="flex-row-left" :class="active >= (index + 1) ? 'active' : ''">
      <div class="main-box flex-row-left">
        <div class="box">{{ index + 1 }}</div>
        {{ item }}
      </div>
      <div class="line" v-show="index !== steps.length - 1" :class="active > (index + 1) ? 'active-line' : ''"></div>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'Steps',
    props: {
      steps: {
        type: Array,
        default: () => []
      },
      active: {
        type: Number,
        default: 1
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
  .step-box {
    font-size: 16px;
    color: $font-light-color;
    .line {
      width: 238px;
      height: 1px;
      background: #CCCCCC;
      margin: 0 16px;
    }
    .main-box {
      height: 32px;
      line-height: 32px;
      .box {
        width: 32px;
        height: 100%;
        background: #FFFFFF;
        border: 1px solid #999999;
        border-radius: 50%;
        text-align: center;
        font-size: 14px;
        color: $font-light-color;
        overflow: hidden;
        margin-right: 8px;
      }
    }
    .active {
      .main-box {
        color: $font-important-color !important;
        .box {
          color: $white !important;
          background: $primary !important;
          border: 1px solid $primary !important;
        }
      }
    }
    .active-line {
      background: $primary !important;
    }
  }
</style>
