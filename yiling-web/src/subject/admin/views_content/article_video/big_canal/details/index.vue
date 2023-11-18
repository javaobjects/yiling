<template>
  <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <el-form
        class="demo-ruleForm"
        label-width="100px"
        ref="dataForm"
        :model="form"
        :rules="rules">
        <div class="c-box">
          <el-row :gutter="50">
            <el-col :span="24">
              <el-form-item label="引用类别">
                <el-radio-group v-model="form.type" @change="typeChange">
                  <el-radio :label="1">文章</el-radio>
                  <el-radio :label="2">视频</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="50">
            <el-col :span="24">
              <el-form-item :label="form.type == 1 ? '选择文章' : '选择视频'" prop="contentId">
                <el-tag
                  v-model="form.contentId"
                  closable 
                  type="info" 
                  class="item-el-tag" 
                  v-for="(item, index) in articleData" 
                  :key="index" 
                  @close="doctorClose(item, index)"> 
                  {{ item.title }}
                </el-tag>
                <span class="add-span" @click="addClick">添加</span>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="50">
            <el-col :span="24">
              <el-form-item label="业务引用">
                <business-quote :business-type="businessType" @addBusinessQuote="addBusinessQuote"></business-quote>
              </el-form-item>
            </el-col>
          </el-row>
        </div>
      </el-form>
    </div>
    <!-- 选择文章弹窗 -->
    <div v-if="showDialog">
      <select-article 
        :show-dialog="showDialog"
        :content-id="form.contentId"
        :source="0"
        @addArticle="addArticle" 
        @close="close">
      </select-article>
    </div>
    <!-- 选择视频弹窗 -->
    <div v-if="showVideo">
      <video-quote 
        :show-dialog="showVideo"
        :content-id="form.contentId"
        :source="0"
        @addArticle="addArticle" 
        @close="close">
      </video-quote>
    </div>
    <div class="flex-row-center bottom-view">
      <yl-button type="primary" plain @click="$router.go(-1)">返回</yl-button>
      <yl-button type="primary" @click="preservationClick" class="bottom-margin">保存</yl-button>
    </div>
  </div>
</template>

<script>
import businessQuote from '../../components/businessQuote';
import selectArticle from '../../components/selectArticle';
import videoQuote from '../../components/videoQuote';
import { dyhAddContent } from '@/subject/admin/api/content_api/multiterminal';
export default {
  name: 'AddCanal',
  components: {
    businessQuote, selectArticle, videoQuote
  },
  computed: {
    
  },
  data() {
    return {
      form: {
        // 1文章 2视频
        type: 1,
        title: '',
        //文章/视频id
        contentId: ''
      },
      rules: {
        contentId: [{ required: true, message: '请选择', trigger: 'change' }]
      },
      //文章弹窗
      showDialog: false,
      //视频弹窗
      showVideo: false,
      //文章/视频数据
      articleData: [],
      //业务线id 7 大运河
      businessType: 7,
      //所属板块以及栏目数组集合
      contentList: []
    }
  },
  mounted() {
    
  },
  methods: {
    //切换引用类别
    typeChange() {
      this.articleData = [];
      this.form.title = '';
      this.form.categoryId = ''
    },
    // 选择文章/视频
    addClick() {
      if (this.form.type == 1) {
        this.showDialog = true;
      } else {
        this.showVideo = true;
      }
    },
    // 点击弹窗顶部 关闭
    close() {
      if (this.form.type == 1) { 
        this.showDialog = false
      } else {
        this.showVideo = false;
      }
    },
    //文章/文章返回数据
    addArticle(val) {
      this.articleData = [{
        title: val.title,
        id: val.id
      }]
      this.form.contentId = val.id.toString();
      if (this.form.type == 1) { 
        this.showDialog = false
      } else {
        this.showVideo = false;
      }
    },
    // 业务引用传递过来的值
    addBusinessQuote(val) {
      for (let i = 0; i < this.contentList.length; i ++) {
        if (this.contentList[i].moduleId == val.moduleId) {
          this.contentList.splice(i, 1)
        }
      }
      this.$nextTick(() => {
        if (val.categoryId != -1) {
          this.contentList.push({
            categoryId: val.categoryId,
            contentId: '',
            lineId: this.businessType,
            moduleId: val.moduleId
          })
        }
      })
    },
    //删除所选文章
    doctorClose(row, index) {
      this.articleData.splice(index, 1)
      this.form.contentId = ''
    },
    // 点击保存
    preservationClick() {
      this.$refs['dataForm'].validate( async(valid) => {
        if (valid) {
          let form = this.form;
          let dataList = [];
          if (this.contentList && this.contentList.length > 0) {
            for (let i = 0; i < this.contentList.length; i ++) {
              dataList.push({
                ...this.contentList[i],
                contentId: form.contentId
              })
            }
            this.$common.showLoad();
            let data = await dyhAddContent(
              dataList
            )
            this.$common.hideLoad();
            if (data !== undefined) {
              this.$common.alert('保存成功', r => {
                this.$router.go(-1)
              })
            }
          } else {
            this.$message.warning('请选择栏目分类')
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
