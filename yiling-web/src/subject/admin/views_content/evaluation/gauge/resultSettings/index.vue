<template>
  <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <div class="c-box">
        <div class="title">当前量表：{{ title }}</div>
        <div> 
          <div class="result-for" v-for="(item, index) in resultData" :key="index">
            <span class="form-item-title">结果 {{ index + 1 }}</span>
            <div class="form-item-icon">
              <span class="item-details" @click="detailsResultClick(item, index)">
                <i class="el-icon-delete icon_style"></i>
              </span>
              <span class="item-modify" @click="modifyClick(item)">
                <i class="el-icon-edit icon_style"></i>
              </span>
            </div>
            <div class="result-conter">
              <div class="examine-row-conter">
                对应分值：{{ item.scoreStartType | dictLabel(sectionData) }}
                {{ item.scoreStart }} 至 {{ item.scoreEndType | dictLabel(sectionData) }} {{ item.scoreEnd }}
              </div>
              <div class="examine-row-conter">
                测评结果：{{ item.evaluateResult }}
              </div> 
              <div class="examine-row-conter">
                结果描述：{{ item.resultDesc }}
              </div> 
              <div class="examine-row-conter">
                健康小贴士：
                <div class="examine-row-conter-item" v-dompurify-html="item.healthTip"></div>
              </div>  
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="flex-row-center bottom-view">
      <yl-button type="primary" @click="addResultClick">添加结果</yl-button>
      <yl-button type="primary" plain @click="$router.go(-1)">返回</yl-button>
    </div>
    <div v-show="show">
      <yl-dialog
        width="800px"
        title="结果设置"
        :visible.sync="showDialog"
        @confirm="confirm">
        <div class="dialogTc">
          <result-details ref="resultDetails" :health-evaluate-id="healthEvaluateId" :result-details-data="resultDetailsData" @dialogPop="dialogPop"></result-details>
        </div>
      </yl-dialog>
    </div>
    
  </div>
</template>

<script>

import { getResultListByEvaluateId, delHealthEvaluateResultById } from '@/subject/admin/api/content_api/evaluation'
import resultDetails from '../../components/resultDetails'
export default {
  name: 'ResultSettings',
  components: {
    resultDetails
  },
  data() {
    return {
      ifResult: 1,
      sectionData: [
        {
          value: 1,
          label: '小于'
        },
        {
          value: 2,
          label: '小于等于'
        },
        {
          value: 3,
          label: '等于'
        },
        {
          value: 4,
          label: '大于等于'
        },
        {
          value: 5,
          label: '大于'
        }
      ],
      title: '',
      healthEvaluateId: 0,
      showDialog: true,
      show: false,
      resultData: [],
      resultDetailsData: {}
    }
  },
  created() {
   
  },
  mounted() {
    let dataTitle = this.$route.params;
    if (dataTitle.title) {
      this.title = dataTitle.title;
      this.healthEvaluateId = parseInt(dataTitle.id);
      this.getData()
    }
  },
  methods: {
    //获取结果数据
    async getData() {
      let data = await getResultListByEvaluateId(this.healthEvaluateId)
      if (data) {
        this.resultData = data;
      }
      this.showDialog = false;
      this.show = true;
    },
    confirm() {
      this.$refs.resultDetails.preservation()
    },
    //点击添加结果
    addResultClick() {
      this.resultDetailsData = {};
      this.showDialog = true;
    },
    //点击删除结果
    detailsResultClick(row, index) {
      this.$confirm(`确认删除 题目${index + 1 } ！`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then( async() => {
          //确定
          this.$common.showLoad();
          let data = await delHealthEvaluateResultById(
            row.id
          )
          this.$common.hideLoad();
          if (data !== undefined) {
            this.$common.n_success('删除成功!');
            this.getData()
          }
        })
        .catch(() => {
          //取消
        });
    },
    // 点击编辑
    modifyClick(row) {
      this.showDialog = true;
      this.resultDetailsData = row;
    },
    //关闭弹窗
    dialogPop() {
      this.showDialog = false;
      this.getData()
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>