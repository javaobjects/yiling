<template>
  <el-row :gutter="16">
    <el-col :span="8">
      <el-select
        :disabled="disabled"
        :style="{width: width}"
        v-model="pro"
        v-loading="loading"
        @visible-change="visibleChange"
        @change="e => change(1, e)"
        placeholder="请选择省">
        <el-option v-if="isAll" key="" label="全部" value=""></el-option>
        <el-option
          v-for="item in provinceList"
          :key="item.code"
          :label="item.name"
          :value="item.code">
        </el-option>
      </el-select>
    </el-col>
    <el-col :span="8">
      <el-select
        :disabled="disabled || !pro"
        :style="{width: width}"
        v-model="cty"
        v-loading="loading1"
        @change="e => change(2, e)"
        placeholder="请选择市">
        <el-option v-if="isAll" key="" label="全部" value=""></el-option>
        <el-option
          v-for="item in cityList"
          :key="item.code"
          :label="item.name"
          :value="item.code">
        </el-option>
      </el-select>
    </el-col>
    <el-col :span="8" v-if="areaShow">
      <el-select
        :disabled="disabled || (!pro || !cty)"
        :style="{width: width}"
        v-model="are"
        v-loading="loading2"
        @change="e => change(3, e)"
        placeholder="请选择区/县">
        <el-option v-if="isAll" key="" label="全部" value=""></el-option>
        <el-option
          v-for="item in areaList"
          :key="item.code"
          :label="item.name"
          :value="item.code">
        </el-option>
      </el-select>
    </el-col>
  </el-row>
</template>

<script>
  import { getLocation } from '@/subject/pop/api/common'
  export default {
    name: 'YlChooseAddress',
    props: {
      province: {
        type: String,
        default: ''
      },
      city: {
        type: String,
        default: ''
      },
      area: {
        type: String,
        default: ''
      },
      areaShow: {
        type: Boolean,
        default: true
      },
      // 固定宽度
      width: {
        type: String,
        default: '168px'
      },
      // 是否显示全部
      isAll: {
        type: Boolean,
        default: false
      },
      // 禁用
      disabled: {
        type: Boolean,
        default: false
      }
    },
    data() {
      return {
        provinceList: [],
        cityList: [],
        areaList: [],
        loading: false,
        loading1: false,
        loading2: false
      }
    },
    computed: {
      pro: {
        get() {
          return this.province
        },
        set(val) {
          this.$emit('update:province', val)
        }
      },
      cty: {
        get() {
          return this.city
        },
        set(val) {
          this.$emit('update:city', val)
        }
      },
      are: {
        get() {
          return this.area
        },
        set(val) {
          this.$emit('update:area', val)
        }
      }
    },
    created() {
      if (this.pro) {
        this.getAddress(1, '')
      }
      if (this.cty && this.pro) {
        this.getAddress(2, this.pro)
      }
      if (this.cty && this.are) {
        this.getAddress(3, this.cty)
      }
    },
    methods: {
      // 获取区域选择器
      async getAddress(type, code) {
        if (type === 1) {
          this.loading = true
        } else if (type === 2) {
          this.loading1 = true
        } else if (type === 3) {
          this.loading2 = true
        }
        let data = await getLocation(code)
        this.$log(data)
        if (type === 1) {
          this.loading = false
          this.provinceList = data.list
        } else if (type === 2) {
          this.loading1 = false
          this.cityList = data.list
        } else if (type === 3) {
          this.loading2 = false
          this.areaList = data.list
        }
      },
      visibleChange(e) {
        if (e) {
          if (this.provinceList.length === 0) {
            this.getAddress(1, '')
          }
        }
      },
      change(type, e) {
        if (type === 1) {
          this.getAddress(2, e)
          this.cty = ''
          this.are = ''
        } else if (type === 2) {
          if (e) {
            this.getAddress(3, e)
            this.are = ''
          }
        }
      }
    }
  }
</script>

<style lang="scss">
</style>
