<template>
  <yl-dialog :visible.sync="showDialog" width="520px" :title="title" :show-cancle="false" @confirm="confirm" @close="close">
    <div class="dialog-content">
      <el-form :model="channelForm" class="channelForm" ref="channelFormRef" :rules="channelFormRules" label-position="left" label-width="120px">
        <el-form-item label="渠道商名称" prop="id">
          <el-select v-model="channelForm.id" filterable remote :remote-method="remoteMethod" @change="handleSelect" :loading="loading" :disabled="type === 'EDIT'" placeholder="请搜索选择渠道商">
            <el-option
              v-for="item in channelOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="组织机构代码证" prop="licenseNumber">
          <el-input v-model="channelForm.licenseNumber" placeholder="请输入组织机构代码证" disabled> </el-input>
        </el-form-item>
        <el-form-item label="业务渠道类型" prop="channelType">
          <el-select v-model="channelForm.channelType" placeholder="请选择业务渠道类型">
            <el-option
              v-for="item in hmcPurchaseChannelType"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
    </div>
    <template slot="left-btn">
      <yl-button plain @click="close">取消</yl-button>
    </template>
  </yl-dialog>
</template>

<script>
import { queryEnterpriseList, addPurchase } from '@/subject/admin/api/cmp_api/insurance_basic_setting'
import { hmcPurchaseChannelType } from '@/subject/admin/busi/cmp/insurance_basic_setting'

export default {
  name: 'AddChannel',
  props: {
    title: {
      type: String,
      default: '添加渠道商'
    },
    type: {
      type: String,
      default: 'ADD'
    },
    formData: {
      type: [Function, Object],
      default: () => {}
    },
    goodsControlId: {
      type: [Number, String],
      default: undefined
    }
  },
  filters: {
    controlStatus(e) {
      return e === 1 ? '开启' : '关闭'
    },
    channelTypeFilter(e) {
      let res
      switch (e) {
        case 1:
          res = '线上'
          break;
        case 2:
          res = '线下'
          break;
        default:
          res = '- -'
          break;
      }
      return res
    }
  },
  computed: {
    hmcPurchaseChannelType() {
      return hmcPurchaseChannelType()
    }
  },
  mounted() {
  },
  data() {
    return {
      loading: false,
      channelOptions: [],
      // 业务渠道类型一期只有 大运河2B 1
      channelTypeOptions: [
        { value: 1, label: '大运河2B' }
      ],
      showDialog: false,
      channelForm: {
        id: '',
        name: '',
        licenseNumber: '',
        channelType: 1
      },
      channelFormRules: {
        id: [
          { required: true, message: '请输入渠道商名称进行搜索', trigger: 'blur' }
        ],
        licenseNumber: [
          { required: true, message: '请输入组织机构代码证', trigger: 'blur' }
        ],
        channelType: [
          { required: true, message: '请选择业务渠道类型', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    async remoteMethod(query) {
      if (query.trim() !== '') {
        this.$refs.channelFormRef.clearValidate('name')
        this.loading = true
        let data = await queryEnterpriseList( query )
        this.loading = false
        if (data) {
          this.channelOptions = data.list
        }
      } else {
        this.$refs.channelFormRef.validateField('name')
      }
    },
    handleSelect(id) {
      this.channelForm.licenseNumber = this.channelOptions.filter(item => item.id === id)[0].licenseNumber
    },
    openDialog() {
      this.$nextTick(async () => {
        if (this.type === 'EDIT') {
          this.channelForm = { ...this.formData }
          this.loading = true
          let data = await queryEnterpriseList( this.channelForm.enterpriseName )
          this.loading = false
          if (data) {
            this.channelOptions = data.list
            this.channelForm.id = data.list[0].id
          }
        }
        this.showDialog = true
      })
    },
    close() {
      this.channelForm = {}
      this.$refs.channelFormRef.resetFields()
      this.showDialog = false
    },
    confirm() {
      this.$refs.channelFormRef.validate(async (valid) => {
        if (valid) {
          if (this.type === 'ADD') {
            //  新建
            let form = this.channelForm
            this.$common.showLoad()
            let data = await addPurchase(
              this.goodsControlId,
              form.id,
              form.channelType
            )
            this.$common.hideLoad()
            if (data !== undefined) {
              this.showDialog = false
              this.channelForm = {}
              this.$common.n_success('操作成功')
              this.$emit('freshData')
            }
          } else if (this.type === 'EDIT') {
            //  修改-一期业务渠道类型只有 大运河2B 一种, 编辑暂无逻辑和接口修改
            this.showDialog = false
            this.close()
            this.channelOptions = []
          }
        }
      })
    }
  }
}
</script>
<style lang="scss" scoped>
  .dialog-content {
    .el-form {
      .el-form-item {
        ::v-deep .el-form-item__label {
          font-weight: 400;
        }
        .el-form-item__content {
          .el-select {
            width: 100%;
          }
        }
      }
    }
  }
</style>