<template>
  <div class="businessQuote">
    <div class="item-business">
      <div class="item-business-title">{{ businessType | dictLabel(businessData) }}</div>
      <div class="item-business-content" v-for="(item, index) in dataList" :key="index">
        <el-row>
          <el-col :span="4" style="width:180px">
            所属板块：
            <span v-if="businessType == 1">{{ item.moduleId | dictLabel(hmcModuleData) }}</span>
            <span v-if="businessType == 2">{{ item.moduleId | dictLabel(ihModuleData) }}</span>
            <span v-if="businessType == 6">{{ item.moduleId | dictLabel(saModuleData) }}</span>
            <span v-if="businessType == 7">{{ item.moduleId | dictLabel(popModuleData) }}</span>
          </el-col>
          <el-col :span="2" style="width:70px">
            选择分类：
          </el-col>
          <el-col :span="16" >
            <div class="businessQuote-type">
              <el-radio-group v-model="item.dataRadio" @change="radioChange(item)">
                <el-radio v-for="item1 in item.moduleCategoryList" :key="item1.categoryId" :label="item1.categoryId">
                  {{ item1.categoryName }}
                </el-radio>
                <el-radio :label="-1" class="cancel">取消选择</el-radio>
              </el-radio-group>
            </div>
          </el-col>
        </el-row>
      </div>
      <div v-if="businessType == 2">
        <el-row>
          <el-col :span="2" style="width:100px">
            内容权限:
          </el-col>
          <el-col :span="20">
            <el-radio-group v-model="dataRadio" @change="contentChange">
              <el-radio :label="1">仅登录</el-radio>
              <el-radio :label="2">需认证通过</el-radio>
            </el-radio-group>
          </el-col>
        </el-row>
      </div>
    </div>
  </div>
</template>

<script>
import { displayLine } from '@/subject/admin/utils/busi'
import { getModulesByLineId } from '@/subject/admin/api/content_api/multiterminal'
import { hmcModule, ihModule, saModule, popModule } from '@/subject/admin/busi/content/article_video'
export default {
  props: {
    //业务线
    businessType: {
      type: Number,
      default: 1
    }
  },
  computed: {
    //业务线
    businessData() {
      return displayLine()
    },
    //健康管理中心 板块
    hmcModuleData() {
      return hmcModule()
    },
    //医生端板块
    ihModuleData() {
      return ihModule()
    },
    //销售助手板块
    saModuleData() {
      return saModule()
    },
    //大运河板块
    popModuleData() {
      return popModule()
    }
  },
  data() {
    return {
      dataList: [],
      dataRadio: 1,
      checkedData: []
    }
  },
  mounted() {
    this.getLine()
  },
  methods: {
    //根据业务线id获取 所属所有板块和栏目
    async getLine() {
      let data = await getModulesByLineId(
        this.businessType
      )
      if (data) {
        this.dataList = data;
      }
    },
    radioChange(value) {
      this.$emit('addBusinessQuote', {
        moduleId: value.moduleId,
        categoryId: value.dataRadio
        }
      )
    },
    cancelClick() {

    },
    // 点击内容权限
    contentChange() {
      this.$emit('addContent', {
        contentAuth: this.dataRadio
        }
      )
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>