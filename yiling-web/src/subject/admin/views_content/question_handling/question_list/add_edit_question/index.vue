<template>
  <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <el-form
        class="demo-ruleForm"
        label-width="80px"
        :model="form"
        :rules="rules"
        ref="dataForm">
        <div class="c-box">
          <el-row >
            <el-col :span="18">
              <el-form-item label="标题" prop="title">
                <el-input
                  type="textarea"
                  show-word-limit
                  maxlength="50"
                  :rows="2"
                  style="max-width: 100%"
                  :disabled="stateTypes == 3" 
                  v-model.trim="form.title">
                </el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="50">
            <el-col :span="12">
              <el-form-item label="所属分类" prop="categoryId">
                <el-select v-model="form.categoryId" placeholder="请选择" :disabled="stateTypes == 3">
                  <el-option 
                    v-for="item in categoryData" 
                    :key="item.value" 
                    :label="item.label" 
                    :value="item.value">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <div>
            <el-row>
              <el-col :span="18">
                <el-form-item label="简述" prop="description">
                  <el-input
                    type="textarea"
                    :rows="3"
                    show-word-limit 
                    maxlength="100"
                    placeholder="请输入内容"
                    :disabled="stateTypes == 3"
                    v-model="form.description">
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </div>
          <div>
            <el-form-item label="相关文献">
              <div>
                <el-tag
                  type="info" 
                  :closable="stateTypes != 3"
                  class="item-el-tag"
                  v-for="(item,index) in literatureData" 
                  :key="index" 
                  @close="literatureClose(item,index)">
                   {{ item.title }} 
                </el-tag>
              </div>
              <span class="add-span" v-if="stateTypes != 3" @click="addClick(1)">添加</span>
            </el-form-item>
          </div>
          <div>
            <el-form-item label="关联药品">
              <div>
                <el-tag
                  class="item-el-tag"
                  type="info"
                  :closable="stateTypes != 3"
                  v-for="(item,index) in drugData"
                  :key="index"
                  @close="drugClose(item,index)">
                  {{ item.name }}
                </el-tag>
              </div>
              <span class="add-span" v-if="stateTypes != 3" @click="addClick(2)">添加</span>
            </el-form-item>
          </div>
          <div>
            <el-form-item label="内容详情" prop="content">
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
    <div class="flex-row-center bottom-view" >
      <yl-button type="primary" plain @click="$router.go(-1)">返回</yl-button>
      <yl-button type="primary" class="bottom-margin" v-if="stateTypes != 3" @click="preservationClick()">保存</yl-button>
    </div>
    <!-- 弹窗 -->
    <dialog-popup
      :show.sync="showDialog" 
      :dialog-type="dialogType" 
      :dialog-title="dialogTitle"
      :data-list="dataList"
      @addCommodity="addCommodity"
      @determine="determine"
      v-if="showDialog">
    </dialog-popup>
  </div>
</template>

<script>
import { wangEditor } from '@/subject/admin/components'
import dialogPopup from '../components/dialogPopup'
import { baseUpdate, detail} from '@/subject/admin/api/content_api/question_handling'
export default {
  name: 'AddEditQuestion',
  components: {
    wangEditor, dialogPopup
  },
  computed: {},
  data() {
    return {
      height: 600,
      form: {
        title: '',
        //所属分类
        categoryId: '', 
        //内容
        content: '', 
        id: '',
        //简述
        description: '' 
      },
      rules: {
        title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
        categoryId: [{ required: true, message: '请选择所属前台分类', trigger: 'change' }],
        description: [{ required: true, message: '请输入简述内容', trigger: 'blur' }],
        content: [{ required: true, message: '请输入内容详情', trigger: 'blur' }]
      },
      categoryData: [
        {
          label: '药品相关',
          value: 1
        }
      ],
      showDialog: false,
      dialogTitle: '',
      //药品数据
      drugData: [], 
      //文献数据
      literatureData: [], 
      // 弹窗类型 1 关联文献资料 2 关联药品 
      dialogType: 1 , 
       //传递给自组建的数据
      dataList: [],
      // 1 新增 2 编辑 3 详情
      stateTypes: '',
      content: ''
    }
  },
  mounted() {
    let query = this.$route.params;
    this.stateTypes = query.type;
    if (query.type && query.type != 1) {
      this.getContentData(query.id)
    }
  },
  methods: {
    //点击确定
    determine() {
      this.showDialog = false
    },
    // 从父级传递过来的值
    addCommodity(row) {
      if (this.dialogType == 1) {
        this.literatureData.push(row)
      }
      if (this.dialogType == 2) {
        this.drugData.push({
          ...row,
          standardId: row.id
        })
        // this.showDialog = false;
        // if (row && row.length > 0) {
        //   for (let y = 0; y < this.drugData.length; y++) {
        //     for (let i = 0; i < row.length; i++) {
        //       if (row[i].id == this.drugData[y].sellSpecificationsId || row[i].id == this.drugData[y].id) {
        //         row.splice(i,1)
        //       }
        //     }
        //   }
        //   row.forEach(e => {
        //     this.drugData.push(e)
        //   });
        // }
      }
    },
    // 删除关联药品
    drugClose(row,index) {
      if (this.stateTypes != 3) {
        this.drugData.splice(index,1)
      }
    },
    // 删除文献
    literatureClose(row,index) {
      if (this.stateTypes != 3) {
        this.literatureData.splice(index,1)
      }
    },
    // 添加
    addClick(row) {
      if (row == 1) {
        this.dialogTitle = '关联相关文献资料';
        this.dataList = this.literatureData;
      } else if (row == 2) {
        this.dialogTitle = '关联药品';
        this.dataList = this.drugData;
      }
      this.dialogType = row;
      this.showDialog = true;
    },
    // 获取 编辑数据
    async getContentData(id) {
      let data = await detail(id)
      if (data !== undefined) {
        data.documentList.forEach(e => {
          this.literatureData.push({
            title: e.documentTitle,
            id: e.documentId
          })
        });
        this.form = data;
        this.content = data.content;
        //药品
        this.drugData = data.standardGoodsList; 
      }
    },
     // 富文本编辑器回调
    handleContent(content, editor) {
      this.form.content = content
    },
    // 点击保存
    preservationClick() {
      if (this.drugData && this.drugData.length < 1) {
        this.$common.warn('请选择关联药品！')
      } else {
        this.$refs['dataForm'].validate( async(valid) => {
          if (valid) {
            let form = this.form;
            // 文献ID
            let literatureID = [];
            this.literatureData.forEach(e => {
              literatureID.push(e.id)
            })
            // 药品ID
            let drugID = [];
            this.drugData.forEach(e => {
              drugID.push({
                standardId: e.standardId
              })
            });
            this.$common.showLoad();
            let data = await baseUpdate(
              form.categoryId,
              form.content,
              literatureID,
              drugID,
              form.title,
              form.description,
              form.id
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
}
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
