<template>
  <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <el-form
        label-width="100px"
        :model="form" 
        :rules="rules"
        class="demo-ruleForm"
        ref="dataForm">
        <div class="c-box">
          <el-row :gutter="50">
            <el-col :span="12">
              <el-form-item label="文献标题" prop="title">
                <el-input
                  type="textarea"
                  maxlength="200" 
                  show-word-limit
                  style="max-width: 90%"
                  :autosize="{ minRows: 2, maxRows: 2}"
                  placeholder="请输入文献标题"
                  v-model.trim="form.title">
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="所属分类" prop="categoryId">
                <el-select v-model="form.categoryId" placeholder="请选择">
                  <el-option 
                    v-for="item in categoryData" 
                    :key="item.id" 
                    :label="item.categoryName" 
                    :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="50">
            <el-col :span="12">
              <el-form-item label="来源">
                <el-input
                  type="textarea"
                  maxlength="30" 
                  show-word-limit
                  style="max-width: 90%"
                  :autosize="{ minRows: 2, maxRows: 2}"
                  placeholder="请输入来源"
                  v-model.trim="form.source">
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="作者">
                <el-input maxlength="10" show-word-limit style="max-width: 90%" v-model.trim="form.author"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="50">
            <el-col :span="12">
              <el-form-item label="是否公开">
                <el-radio-group v-model="form.isOpen">
                  <el-radio :label="1">是</el-radio>
                  <el-radio :label="0">否</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="发布状态">
                <el-checkbox :true-label="2" :false-label="1" v-model="form.status">立即发布</el-checkbox>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="引用业务线" prop="displayLines">
                <el-checkbox-group v-model="form.displayLines">
                  <el-checkbox 
                    v-for="item in businessData" 
                    :key="item.value" 
                    :label="item.value">
                    {{ item.label }}
                  </el-checkbox>
                </el-checkbox-group>
              </el-form-item>
            </el-col>
          </el-row>
          <div>
            <el-form-item label="关联药品">
              <div>
                <el-tag
                  closable 
                  type="info" 
                  class="item-el-tag" 
                  v-for="(item,index) in drugData" 
                  :key="index"
                  @close="drugClose(item,index)"> 
                  {{ item.name }} 
                </el-tag>
              </div>
              <span class="add-span" @click="addClick">添加</span>
            </el-form-item>
          </div>
          <div>
            <el-row>
              <el-col :span="12">
                <el-form-item
                  label="上传文献" 
                  prop="documentFileUrlKey" 
                  :rules="{ required: true, message: '请上传文献', trigger: 'change' }">
                  <el-input style="display:none" v-model="form.documentFileUrlKey"/>
                  <yl-upload-file
                    class="upload-file" 
                    :action="info.action" 
                    :oss-key="'documentFileUrl'" 
                    :file-type="'pdf'" 
                    @onSuccess="onSuccess">
                  </yl-upload-file>
                  <a class="upload-file-url" :href="fileData.url" target="_blank">{{ fileData.name }}</a>
                </el-form-item>
              </el-col>
            </el-row>
          </div>
          <div>
            <el-form-item label="简述">
              <el-input class="elInput" type="textarea" show-word-limit maxlength="100" :rows="3" placeholder="请输入内容" v-model="form.resume"></el-input>
            </el-form-item>
          </div>
          <div>
            <el-form-item label="内容概述" prop="content">
              <wang-editor
                :height="height" 
                :content="content" 
                :extral-data="{type: 'richTextEditorFile'}" 
                :handle-content="handleContent"/>
            </el-form-item>
          </div>
        </div>
      </el-form>
    </div>
    <div class="flex-row-center bottom-view">
      <yl-button type="primary" plain @click="$router.go(-1)">返回</yl-button>
      <yl-button type="primary" class="bottom-margin" @click="preservationClick()">保存</yl-button>
    </div>
    <!-- 弹窗 -->
    <dialog-disease-drugs
      :show.sync="showDialog" 
      :dialog-type="dialogType" 
      :dialog-title="dialogTitle"
      :data-list="dataList"
      @addCommodity="addCommodity" 
      v-if="showDialog">
    </dialog-disease-drugs>
  </div>
</template>

<script>
import { wangEditor, ylUploadFile } from '@/subject/admin/components'
import { queryCategoryList, addContent, getDocumentById, updateContent } from '@/subject/admin/api/content_api/literature'
import { displayLine } from '@/subject/admin/utils/busi'
import dialogDiseaseDrugs from '../../article_video/establish/dialogDiseaseDrugs'
export default {
  name: 'AddEdit',
  components: {
    wangEditor, dialogDiseaseDrugs, ylUploadFile
  },
  computed: {
    businessData() {
      return displayLine()
    }
  },
  data() {
    return {
      height: 600,
      form: {
        //作者
        author: '', 
        //文献栏目id
        categoryId: '', 
         //内容
        content: '',
         //显示业务线
        displayLines: [],
        // 文献pdf key
        documentFileUrlKey: '', 
        // 是否公开 0-否 1-是
        isOpen: 0, 
        //简述
        resume: '', 
        //来源
        source: '', 
        //药品
        standardGoodsIdList: [], 
        // 状态：1-未发布 2-已发布
        status: 1, 
        // 标题
        title: '', 
        id: ''
      },
      rules: {
        title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
        categoryId: [{ required: true, message: '请选择所属前台分类', trigger: 'change' }],
        displayLines: [{ required: true, message: '请选择引用业务线', trigger: 'change' }],
        documentFileUrlKey: [{ required: true, message: '请上传文献', trigger: 'change' }],
        content: [{ required: true, message: '请输入内容概述', trigger: 'blur' }]
      },
      categoryData: [],
      // 1 新增 2 编辑
      isAdd: 1, 
      showDialog: false,
      dialogTitle: '',
      //药品数据
      drugData: [], 
      // 弹窗类型 1 关联疾病 2 关联药品 3关联科室
      dialogType: 2, 
      //传递给自组建的数据
      dataList: [], 
      info: {
        action: '/system/api/v1/file/upload',
        extralData: { type: 'documentFileUrl'}
      },
      //上传文件成功后的数据
      fileData: {
        name: '',
        url: ''
      },
      content: ''
    }
  },
  mounted() {
    this.categoryApi(); 
    let query = this.$route.params;
    if (query.type && query.type != 1) {
      this.isAdd = query.type;
      this.getContentData(query.id)
    }
  },
  methods: {
    // 从父级传递过来的值
    addCommodity(row) {
      this.drugData.push(row)
    },
    // 删除关联疾病
    diseaseClose(row,index) {
      this.diseaseData.splice(index,1)
    },
    // 删除关联药品
    drugClose(row,index) {
      this.drugData.splice(index,1)
    },
    // 删除科室数据
    departmentClose(row,index) {
      this.departmentData.splice(index,1)
    },
    // 添加
    addClick() {
      this.dataList = this.drugData;
      this.dialogTitle = '关联药品';
      this.dialogType = 2;
      this.showDialog = true;
    },
    // 获取 数据
    async getContentData(id) {
      let data = await getDocumentById(id)
      if (data) {
        this.drugData = data.standardGoodsList;
        this.fileData = {
          name: data.documentFileName,
          url: data.documentFileUrl
        }
        this.content = data.content;
        this.form = data;
      }
    },
    async categoryApi() {
      let data = await queryCategoryList()
      if (data) {
        this.categoryData = data.list;
      }
    },
    // 上传文献
    onSuccess(data) {
      if (data.key) {
        this.$common.n_success('上传成功！');
        this.form.documentFileUrlKey = data.key;
        this.fileData = data;
      }
    },
    // 富文本编辑器回调
    handleContent(content, editor) {
      this.form.content = content
    },
    // 点击保存
    preservationClick() {
      this.$refs['dataForm'].validate( async(valid) => {
        if (valid) {
          let form = this.form;
          let drug = [];
          this.drugData.forEach(e => {
            drug.push(e.id)
          });
          if (this.isAdd == 1) {
            this.$common.showLoad();
            let data = await addContent(
              form.author,
              form.categoryId,
              form.content,
              form.displayLines,
              form.documentFileUrlKey,
              this.fileData.name,
              form.isOpen,
              form.resume,
              form.source,
              drug,
              form.status,
              form.title
            )
            this.$common.hideLoad();
            if (data !== undefined) {
              this.$common.alert('保存成功', r => {
                this.$router.go(-1)
              })
            }
          } else {
            this.$common.showLoad();
            let data2 = await updateContent(
              form.author,
              form.categoryId,
              form.content,
              form.displayLines,
              form.documentFileUrlKey,
              this.fileData.name,
              form.isOpen,
              form.resume,
              form.source,
              drug,
              form.status,
              form.title,
              form.id
            )
            this.$common.hideLoad();
            if (data2 !== undefined) {
              this.$common.alert('保存成功', r => {
                this.$router.go(-1)
              })
            }
          }
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
