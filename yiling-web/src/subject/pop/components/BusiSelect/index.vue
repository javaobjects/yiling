<template>
  <el-select
    v-model="choose"
    :disabled="disabled"
    :style="{'width': width}"
    v-loading="companyLoading"
    @visible-change="visibleChange"
    @change="change"
    :placeholder="placeholder">
    <el-option
      v-for="item in companyList"
      :key="item.id"
      :label="item.name"
      :value="item.id">
    </el-option>
  </el-select>
</template>

<script>
  import { getCompanyList } from '@/subject/pop/api/products'
  export default {
    name: 'BusiSelect',
    props: {
      // 业务标致
      busi: {
        type: String,
        default: 'COMPANY_LIST',
        require: true
      },
      // 绑定变量
      value: {
        type: [String, Number],
        default: ''
      },
      placeholder: {
        type: String,
        default: '请选择'
      },
      // 宽度
      width: {
        type: String,
        default: '320px'
      },
      // 初始化时是否加载
      init: {
        type: Boolean,
        default: false
      },
      // 禁用
      disabled: {
        type: Boolean,
        default: false
      }
    },
    computed: {
      choose: {
        get() {
          return this.value
        },
        set(val) {
          this.$emit('update:value', val)
        }
      }
    },
    data() {
      return {
        companyList: [],
        companyLoading: false
      }
    },
    created() {
      if (this.init) {
        this.getCompany()
      }
    },
    methods: {
      change() {
        this.$log('change')
      },
      visibleChange(e) {
        if (e) {
          if (this.companyList.length === 0) {
            this.getCompany()
          }
        }
      },
      async getCompany() {
        this.companyLoading = true
        let data = null
        if (this.busi === 'COMPANY_LIST') {
          data = await getCompanyList()
        }
        this.companyLoading = false
        if (data && Array.isArray(data.list)) {
          this.companyList = data.list
        }
      }
    }
  }
</script>

<style lang="scss">
</style>
