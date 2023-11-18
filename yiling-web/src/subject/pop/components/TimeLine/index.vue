<template>
  <div class="my-yl-time-line-1">
    <el-scrollbar :wrap-class="wrapClass" v-if="data.length">
      <el-timeline>
        <el-timeline-item
          v-for="(activity, index) in data"
          :key="index"
          :class="index === 0 ? 'active' : ''"
          :timestamp="activity.info">
        </el-timeline-item>
      </el-timeline>
    </el-scrollbar>
    <yl-empty v-else></yl-empty>
  </div>
</template>

<script>
  import ylEmpty from '@/common/components/Empty'
  export default {
    name: 'TimeLine',
    props: {
      // 样式
      wrapClass: {
        type: String,
        default: ''
      },
      // 数据
      data: {
        type: Array,
        default: () => []
      }
    },
    components: {
      ylEmpty
    },
    methods: {
    }
  }
</script>

<style lang="scss" scoped>
  .my-yl-time-line-1 {
    ::v-deep {
      .el-scrollbar__wrap {
        padding: 3px 0 0 2px;
        height: 150px;
      }
    }
    ::v-deep .el-timeline {
      padding-left: 0 !important;
      .el-timeline-item {
        padding-bottom: 12px;
        .el-timeline-item__tail {
          border-left: 2px solid #D8D8D8 !important;
          z-index: 1;
          left: 2px !important;
        }
        .el-timeline-item__node--normal {
          width: 8px;
          height: 8px;
          border: 2px solid #BCBCBC;
          border-radius: 50%;
          background: #FFFFFF !important;
          z-index: 2;
        }
        .el-timeline-item__wrapper {
          padding-left: 12px;
        }
        .el-timeline-item__content {
          font-size: 12px !important;
          color: $font-title-color !important;
        }
        .el-timeline-item__timestamp {
          margin-top: 0;
        }
      }
      .active {
        .el-timeline-item__node--normal {
          border: 2px solid #1B9AEE;
        }
      }
    }
  }
</style>
