<template>
  <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <div class="c-box">
        <el-form ref="dataForm" label-width="100px" class="demo-ruleForm">
          <el-form-item label="商业ID:">
            <span class="eid-text" v-for="item in eidList" :key="item">{{ item }}</span>
            <yl-button type="text" @click="addClick">添加/修改</yl-button>
          </el-form-item>
          <el-form-item label="流向类型:">
            <el-checkbox-group v-model="typeList">
              <el-checkbox v-for="item in getErpFlowType" :label="item.value" :key="item.value">{{ item.label }}</el-checkbox>
            </el-checkbox-group>
          </el-form-item>
          <el-form-item label="封存月份:">
            <el-checkbox-group v-model="monthList">
              <el-checkbox :label="item" v-for="(item, index) in selectMonthList" :key="index">{{ item }}</el-checkbox>
            </el-checkbox-group>
          </el-form-item>
          <div class="explain-view">说明：封存月份仅支持前推6个整月</div>
        </el-form>
      </div>
    </div>
    <div class="bottom-view flex-row-center">
      <yl-button type="primary" plain @click="$router.go(-1)">返回</yl-button>
      <yl-button type="primary" @click="save">保存</yl-button>
    </div>
    <!-- 添加弹框 -->
    <add-eid-dialog v-if="addEidVisible" ref="addEidRef" @sendSuccess="addSaveClick"></add-eid-dialog>
  </div>
</template>

<script>
import { erpFlowSealedMonthList, flowSealedSave } from '@/subject/admin/api/zt_api/erpAdministration'
import AddEidDialog from './add_eid_dialog'
import { erpFlowType } from '@/subject/admin/busi/zt/erpAdministration'

export default {
  name: 'PopBannerEdit',
  components: {
    AddEidDialog
  },
  computed: {
    // 流向类型
    getErpFlowType() {
      return erpFlowType()
    }
  },
  data() {
    return {
      eidList: [],
      typeList: [],
      monthList: [],
      selectMonthList: [],
      addEidVisible: false,
      selectEidList: []
    }
  },
  mounted() {
    this.erpFlowSealedMonthListMethods()
  },
  methods: {
    async erpFlowSealedMonthListMethods() {
      this.$common.showLoad()
      let data = await erpFlowSealedMonthList()
      this.$common.hideLoad()
      if (data) {
        this.selectMonthList = data
      }
    },
    addClick() {
      this.addEidVisible = true
      this.$nextTick( () => {
        this.$refs.addEidRef.init(this.$common.clone(this.selectEidList))
      })
    },
    addSaveClick(addList) {
      this.addEidVisible = false
      this.selectEidList = addList
      let eidList = []
      addList.forEach(item => {
        eidList.push(item.eid)
      });
      console.log('addSaveClick:', eidList)
      this.eidList = eidList
    },
    async save() {
      if (!this.eidList || this.eidList.length == 0) {
        this.$common.warn('请先添加商业ID');
        return false
      }
      if (!this.typeList || this.typeList.length == 0) {
        this.$common.warn('请先选择流向类型');
        return false
      }
      if (!this.monthList || this.monthList.length == 0) {
        this.$common.warn('请先选择封存月份');
        return false
      }
      this.$common.showLoad()
      let data = await flowSealedSave(this.eidList, this.typeList, this.monthList)
      this.$common.hideLoad()
      if (data) {
        this.$router.go(-1)
        this.$common.alert('保存成功', r => {
          this.$router.go(-1)
        })
      } 
    }
  }
}
</script>

<style lang="scss" scoped>
  @import "./index.scss";
</style>
