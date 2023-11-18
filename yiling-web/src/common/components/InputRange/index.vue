<template>
  <div class="range-input" :style="{ width: width + 'px' }">
    <el-input
      :style="{ width: (width - 30)/2 + 'px' }"
      v-model="value[0]"
      :clearable="clearable"
      @change="minChange"
      @input="minInput"
      :placeholder="min ? `最小值${min}` : startPlaceholder">
    </el-input>
    <div class="separator">{{ rangeSeparator }}</div>
    <el-input
      :style="{ width: (width - 30)/2 + 'px' }"
      v-model="value[1]"
      :clearable="clearable"
      @change="maxChange"
      @input="maxInput"
      :placeholder="max ? `最大值${max}` : endPlaceholder">
    </el-input>
  </div>
</template>

<script>
  import { onInputLimit } from '@/common/utils'
  export default {
    name: 'InputRange',
    props: {
      // 绑定值
      value: {
        type: Array,
        default: () => [null, null]
      },
      // 总宽度
      width: {
        type: Number,
        default: 280
      },
      // 最小值
      min: {
        type: Number,
        default: 0
      },
      // 最大值
      max: {
        type: Number,
        default: 0
      },
      // 分隔符
      rangeSeparator: {
        type: String,
        default: '至'
      },
      startPlaceholder: {
        type: String,
        default: '请输入最小值'
      },
      endPlaceholder: {
        type: String,
        default: '请输入最大值'
      },
      // 是否可清空
      clearable: {
        type: Boolean,
        default: false
      },
      // 最小值小数位
      startFixed: {
        type: Number,
        default: 0
      },
      // 最大值小数位
      endFixed: {
        type: Number,
        default: 0
      }
    },
    data() {
      return {
      }
    },
    computed: {
      start: {
        get() {
          return this.value[0]
        },
        set(val) {
          this.$emit('input', [val, this.value[1] || ''])
        }
      },
      end: {
        get() {
          return this.value[1]
        },
        set(val) {
          this.$emit('input', [this.value[0] || '', val])
        }
      }
    },
    mounted() {
    },
    methods: {
      // 校验是否为数字
      validateIsNum(nubmer) {
        let reg = /(^[\-0-9][0-9]*(.[0-9]+)?)$/
        if (!reg.test(nubmer)) {
          return false
        }
        return true
      },
      // start监听
      minInput() {
        if (!this.validateIsNum(parseFloat(this.start))) {
          this.start = ''
        } else {
          if (this.startFixed >= 0) {
            this.start = onInputLimit(this.start, this.startFixed)
          }
        }
      },
      // end监听
      maxInput() {
        if (!this.validateIsNum(parseFloat(this.end))) {
          this.end = ''
        } else {
          if (this.endFixed >= 0) {
            this.end = onInputLimit(this.end, this.endFixed)
          }
        }
      },
      // 失焦监听
      minChange() {
        if (!this.validateIsNum(this.start)) {
          this.start = ''
          return
        }
        // 判断不可小于最小值
        if (this.min && this.start) {
          if (parseFloat(this.start) < this.min) {
            this.start = ''
          }
        }
        // 判断不可超出最大值
        if (this.end) {
          if (parseFloat(this.start || 0) >= parseFloat(this.end || 0)) {
            this.start = ''
          }
        }
      },
      // 失焦监听
      maxChange() {
        if (!this.validateIsNum(this.end)) {
          this.end = ''
          return
        }
        // 判断不可超出最大值
        if (this.max && this.end) {
          if (parseFloat(this.end) > this.max) {
            this.end = ''
          }
        }
        // 判断不可小于最小值
        if (this.start) {
          if (parseFloat(this.end || 0) < parseFloat(this.start || 0)) {
            this.end = ''
          }
        }
      }
    }
  }
</script>

<style lang="scss" scoped>
  .range-input {
    .separator {
      font-size: 14px;
      color: $font-important-color;
      display: inline-block;
      width: 30px;
      text-align: center;
    }
  }
</style>
