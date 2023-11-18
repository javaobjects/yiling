// 进度条
<template>
  <div class="progress">
    <div class="progress-conter"> 
      <span v-for="(item, index) in progresData" :key="index" :style="{background: item.c, width: item.w + '%'}"></span>
    </div>
    <p v-if="ladderType == '1'" :style="{left: dataPercent + '%'}"></p>
  </div>
</template>

<script>
export default {
  props: {
    // 1 阶梯 2非阶梯
    ladderType: { 
      type: String,
      default: '1'
    },
    dataPercent: {
      type: String,
      default: ''
    },
    dataList: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      colorData: [
        {
          color: '#409eff'
        },
        {
          color: '#67c23a'
        },
        {
          color: '#b7ba6b'
        },
        {
          color: '#f56c6c'
        },
        {
          color: '#40f4ff'
        },
        {
          color: '#40ff51'
        },
        {
          color: '#ceff40'
        },
        {
          color: '#fab27b'
        },
        {
          color: '#f8aba6'
        },
        {
          color: '#817936'
        },
        {
          color: '#dea32c'
        },
        {
          color: '#006c54'
        },
        {
          color: '#472d56'
        },
        {
          color: '#918597'
        },
        {
          color: '#d3c6a6'
        }
      ],
      progresData: []
    }
  },
  mounted() {
    this.progresData = [];
    if (this.ladderType === '1') {
      for (let i = 0; i < this.dataList.length; i ++) {
        this.progresData.push({
          c: this.colorData[i].color == undefined ? '#f56c6c' : this.colorData[i].color,
          w: this.dataList[i]
        })
      }
    } else {
      this.progresData.push({
        c: this.colorData[0].color,
        w: this.dataPercent
      })
    }
  },
  methods: {}
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>