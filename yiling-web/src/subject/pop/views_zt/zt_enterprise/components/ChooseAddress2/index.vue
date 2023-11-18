<template>
  <el-row :gutter="16">
    <el-col :span="8">
      <el-select
        :style="{width: width}"
        v-model="provinceName"
        v-loading="loading"
        @visible-change="visibleChange"
        @change="e => change1(e)"
        placeholder="请选择省">
        <el-option
          v-for="item in provinceList"
          :key="item.code"
          :label="item.name"
          :value="item.code+';'+item.name">
        </el-option>
      </el-select>
    </el-col>
    <el-col :span="8">
      <el-select
        :style="{width: width}"
        v-model="cityName"
        v-loading="loading1"
        @change="e => change2(e)"
        placeholder="请选择市">
        <el-option
          v-for="item in cityList"
          :key="item.code"
          :label="item.name"
          :value="item.code+';'+item.name">
        </el-option>
      </el-select>
    </el-col>
    <el-col :span="8" v-if="areaShow">
      <el-select
        :style="{width: width}"
        v-model="areName"
        v-loading="loading2"
        @change="e => change3(e)"
        placeholder="请选择区/县">
        <el-option
          v-for="item in areaList"
          :key="item.code"
          :label="item.name"
          :value="item.code+';'+item.name">
        </el-option>
      </el-select>
    </el-col>
  </el-row>
</template>

<script>
  import { getLocation } from '@/subject/pop/api/common'
  export default {
    name: 'YlChooseAddress2',
    props: {
      // 省
      province: {
        type: String,
        default: ''
      },
      // 市
      city: {
        type: String,
        default: ''
      },
      // 区
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
      }
    },
    data() {
      return {
        provinceList: [],
        cityList: [],
        areaList: [],
        loading: false,
        loading1: false,
        loading2: false,
        provinceName: '',
        cityName: '',
        areName: ''
      }
    },
    watch: {
      // 省
      province: {
        handler(newName, oldName) { 
          if (newName!='') {
            let val = newName.split(';');
            if (val.length>1) {
              this.provinceName = val[1];
              this.getAddress(2, val[0])
            }
          }
        },
        immediate: true
      },
      // 市
      city: {
        handler(newName, oldName) {
          if (newName!='') {
            let val = newName.split(';');
            if (val.length>1) {
              this.cityName = val[1];
              this.getAddress(3, val[0])
            }
          }
        },
        immediate: true
      },
      // 区
      area: {
        handler(newName, oldName) {
          if (newName!='') {
            let val = newName.split(';');
            if (val.length>1) {
              this.areName = val[1];
              // this.getAddress(3, val[0])
            }
          }
        },
        immediate: true
      }
    },
    created() {
      // if (this.pro) {
      //   this.getAddress(1, '')
      // }
      // if (this.cty && this.pro) {
      //   this.getAddress(2, this.pro)
      // }
      // if (this.cty && this.are) {
      //   this.getAddress(3, this.cty)
      // }
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
      change1(e) {
        if (e) {
          let val = e.split(';');
          if (val.length>1) {
            this.provinceName = val[1];
            this.getAddress(2, val[0])
          }
          // this.getAddress(2, e)
          this.cityName = ''
          this.areName = ''
          this.$emit('update:province', e)
        }
      },
      change2(e) {
        if (e) {
          let val = e.split(';');
          if (val.length>1) {
            this.cityName = val[1];
            this.getAddress(3, val[0])
          }
          this.areName = '';
          this.$emit('update:city', e)
        }
      },
      change3(e) {
        if (e) {
          let val = e.split(';');
          if (val.length>1) {
            this.areName = val[1];
          }
          this.$emit('update:area', e)
        }
      }
      // change(type, e) {
      //   console.log(e,99)
      //   if (type === 1) {
      //     if (e) {
      //       this.getAddress(2, e)
      //       this.cty = ''
      //       this.are = ''
      //     }
      //   } else if (type === 2) {
      //     if (e) {
      //       this.getAddress(3, e)
      //       this.are = ''
      //     }
      //   }
      // }
    }
  }
</script>

<style lang="scss">
</style>
