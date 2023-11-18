<template>
  <div class="app-container">
    <div class="app-container-content">
      <div class="c-box">
        <el-form 
          :model="form" 
          :rules="rules" 
          ref="dataForm" 
          label-width="150px" 
          class="demo-ruleForm">
          <el-form-item label="标签名称" prop="name">
            <el-input v-model="form.name" maxlength="10" show-word-limit></el-input>
          </el-form-item>
          <el-form-item label="标签描述">
            <el-input 
              type="textarea" 
              class="elInput" 
              :rows="3" 
              placeholder="请输入内容" 
              show-word-limit 
              v-model="form.description" 
              maxlength="20"
            ></el-input>
          </el-form-item>
          <el-form-item label="标签类型">
            <el-radio-group v-model="form.type">
              <el-radio v-for="(item, index) in tableType" :key="index" :label="item.value">
                {{ item.label }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
        </el-form>
      </div>
    </div>
    <div class="bottom-view flex-row-center">
      <yl-button type="primary" plain @click="$router.go(-1)">返回</yl-button>
      <yl-button type="primary" @click="save">保存</yl-button>
    </div>
  </div>
</template>

<script>

import { createTags, updateTags } from '@/subject/admin/api/quality';
export default {
  name: 'GoodsLabelAdd',
  data() {
    return {
      form: {
        name: '',
        description: '',
        type: 1,
        id: ''
      },
      rules: {
        name: [{ required: true, message: '请输入标签名称', trigger: 'blur' }],
        description: [{ required: true, message: '请输入标签描述', trigger: 'blur' }]
      },
      tableType: [
        {
          value: 1,
          label: '手动标签'
        }
      ]
    }
  },
  mounted() {
    let query = this.$route.params;
    console.log('query----', query);
    if ( query !== undefined ) {
      if (query.add === 0) {
        this.form = { name: '', description: '', type: 1, id: '' }
      } else {
        this.form = { ...query }
      }
      
    }
  },
  methods: {
    save() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          let form = this.form;
          let data = null;
          this.$common.showLoad();
          if ( form.id !== '' ) {
            data = await updateTags(
              form.description,
              form.id,
              form.name,
              form.type
            )
          } else {
            data = await createTags(
              form.description,
              form.name,
              form.type
            )
          }
          this.$common.hideLoad();
          if ( data != undefined ) {
            this.$common.alert('保存成功', r => {
              this.$router.go(-1)
            })
          }
        } else {
          return false
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
  @import "./index.scss";
</style>
