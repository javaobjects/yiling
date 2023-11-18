<template>
   <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <div class="c-box">
        <el-form 
          :model="form" 
          :rules="rules" 
          ref="dataForm" 
          label-width="140px" 
          class="demo-ruleForm">
          <el-form-item label="用药计划的名称：" prop="name">
            <el-input v-model.trim="form.name" maxlength="20" show-word-limit></el-input>
          </el-form-item>
          <el-form-item label="福利药品：">
            {{ form.drugSellSpecifications }}
          </el-form-item>
          <el-form-item label="福利卷包：" prop="dataList">
            <div class="conter_div">
              <el-row>
                <el-col :span="8">
                  满X盒
                </el-col>
                <el-col :span="8" >
                  赠X盒
                </el-col>
                <el-col :span="8">
                  B2B福利券ID
                </el-col>
              </el-row>
              <div v-for="(item, index) in form.drugWelfareCouponList" :key="index" class="conter_div_table">
                <el-row>
                  <el-col :span="8">
                    <el-form-item :label="''" :prop="'drugWelfareCouponList.' + index + '.requirementNumber'" :rules="{ required: true, message: '满X盒不能为空', trigger: 'blur'}">
                      <el-input size="mini" class="el_input" placeholder="请输入" v-model="item.requirementNumber"></el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="8">
                    <el-form-item :label="''" :prop="'drugWelfareCouponList.' + index + '.giveNumber'" 
                    :rules="{ required: true, message: '赠X盒不能为空', trigger: 'blur'}">
                      <el-input size="mini" class="el_input" placeholder="请输入" v-model="item.giveNumber"></el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="8">
                    <el-form-item :label="''" :prop="'drugWelfareCouponList.' + index + '.couponId'" 
                    :rules="{ required: true, message: 'B2B福利券ID不能为空', trigger: 'blur'}">
                      <el-input size="mini" class="el_input" placeholder="请输入" v-model="item.couponId"></el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
              </div>
            </div>
          </el-form-item>
          <el-form-item label="有效时间：" prop="time">
            <el-date-picker
              v-model="form.time"
              type="datetimerange"
              format="yyyy/MM/dd HH:mm:ss"
              value-format="yyyy-MM-dd HH:mm:ss"
              range-separator="至"
              start-placeholder="开始时间"
              end-placeholder="结束时间"
              align="right"
              :default-time="['00:00:00', '23:59:59']">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="状态：">
            <el-radio-group v-model="form.status">
              <el-radio :label="1">启用</el-radio>
              <el-radio :label="2">停用</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="备注：">
            <el-input 
              type="textarea" 
              style="width:360px" 
              show-word-limit 
              :rows="5"
              maxlength="255" 
              placeholder="请输入备注" 
              v-model="form.remark">
            </el-input>
          </el-form-item>
        </el-form>
      </div>
    </div>
    <div class="flex-row-center bottom-view" >
      <yl-button type="primary" plain @click="$router.go(-1)">返回</yl-button>
      <yl-button type="primary" class="bottom-margin" @click="preservationClick">保存</yl-button>
    </div>
  </div>
</template>
<script>
import { getDrugWelfareDetailById, updateDrugWelfare } from '@/subject/admin/api/cmp_api/benefit_plan'
import { formatDate } from '@/subject/admin/utils'
export default {
  name: 'PlanIndexEdit',
  data() {
    return {
      form: {
        name: '',
        drugSellSpecifications: '',
        time: [],
        status: Number,
        remark: '',
        drugWelfareCouponList: []
      },
      rules: {
        name: [{ required: true, message: '请输入用药计划的名称', trigger: 'blur' }],
        drugWelfareCouponList: [{ required: true, message: '请输入福利卷包', trigger: 'blur' }],
        time: [{ required: true, message: '请选择有效时间', trigger: 'blur' }]
      }
    }
  },
  mounted() {
    let query = this.$route.params;
    if (query&& query.id) {
      this.getData(query.id)
    }
  },
  methods: {
    // 获取数据
    async getData(id) {
      let data = await getDrugWelfareDetailById(id)
      if (data) {
        this.form = { 
          ...data,
          time: [formatDate(data.beginTime) , formatDate(data.endTime)]
        }
      }
    },
    // 点击保存
    preservationClick() {
      this.$refs['dataForm'].validate( async(valid) => {
        if (valid) {
          let query = this.form;
          console.log(query, 999)
          this.$common.showLoad();
          let data = await updateDrugWelfare(
            query.time && query.time.length > 0 ? query.time[0] : '',
            query.time && query.time.length > 1 ? query.time[1] : '',
            query.id,
            query.name,
            query.remark,
            query.status,
            query.sellSpecificationsId,
            query.drugWelfareCouponList
          )
          this.$common.hideLoad();
          if (data !== undefined) {
            this.$common.alert('保存成功', r => {
              this.$router.go(-1)
            })
          }
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
  @import './index.scss';
</style>