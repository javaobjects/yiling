<template>
  <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <el-form
        class="demo-ruleForm"
        label-width="106px"
        ref="dataForm"
        :model="form"
        :rules="rules">
        <div class="c-box">
          <el-row :gutter="50">
            <el-col :span="12">
              <el-form-item label="标题" prop="title">
                <el-input
                  type="textarea"
                  maxlength="50" 
                  show-word-limit
                  style="max-width: 90%"
                  :autosize="{ minRows: 2, maxRows: 2}"
                  placeholder="请输入标题"
                  v-model.trim="form.title">
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="副标题" v-if="query.category == 1">
                <el-input
                  type="textarea"
                  maxlength="50" 
                  show-word-limit
                  style="max-width: 90%"
                  :autosize="{ minRows: 2, maxRows: 2}"
                  placeholder="请输入副标题"
                  v-model.trim="form.subtitle">
                </el-input>
              </el-form-item>
              <el-form-item label="主讲人" v-if="query.category == 2">
                 <el-input
                  type="textarea"
                  maxlength="50" 
                  show-word-limit
                  style="max-width: 90%"
                  :autosize="{ minRows: 2, maxRows: 2}"
                  placeholder="请输入主讲人"
                  v-model.trim="form.speaker">
                </el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="50">
            <!-- <el-col :span="12">
              <el-form-item label="所属前台分类" prop="categoryId">
                <el-select v-model="form.categoryId" placeholder="请选择">
                  <el-option 
                    v-for="item in categoryData" 
                    :key="item.id" 
                    :label="item.categoryName" 
                    :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col> -->
            <el-col :span="12">
              <el-form-item label="发布状态">
                <el-checkbox :true-label="2" :false-label="1" v-model="form.status">立即发布</el-checkbox>
              </el-form-item>
            </el-col>
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
          </el-row>
          <el-row :gutter="50">
            <el-col :span="12">
              <el-form-item label="是否公开" prop="isOpen">
                <el-radio-group v-model="form.isOpen">
                  <el-radio :label="1">是</el-radio>
                  <el-radio :label="0">否</el-radio>
                </el-radio-group>
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
              <el-form-item label="关联会议" v-if="query.category == 2">
                <div>
                  <el-tag
                    closable 
                    type="info" 
                    class="item-el-tag" 
                    v-for="(item, index) in meetingData" 
                    :key="index"
                    @close="meetingClose(item, index)">
                     {{ item.title }} 
                  </el-tag>
                </div>
                <span class="add-span" @click="addClick(4)">添加</span>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="50">
            <el-col :span="12">
              <el-form-item label="内容权限" v-if="limitShow">
                <el-radio-group v-model="form.viewLimit">
                  <el-radio :label="1">仅登录</el-radio>
                  <el-radio :label="2">需认证通过</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="50">
            <el-col :span="12">
              <el-form-item label="封面" v-if="query.category == 1">
                <yl-upload
                  :default-url="form.cover"
                  :extral-data="{type: 'contentCover'}"
                  :oss-key="'contentCover'"
                  @onSuccess="onSuccess"/>
                <p class="explain">建议尺寸：720 x 400像素，支持.jpg 、.png格式</p>
              </el-form-item>
              <el-form-item 
                label="封面" 
                prop="cover" 
                :rules="{ required: true, message: '请上传视频封面', trigger: 'change' }"
                v-if="query.category == 2">
                <el-input style="display:none" v-model="form.cover"/>
                <yl-upload
                  :default-url="form.cover"
                  :extral-data="{type: 'contentCover'}"
                  :oss-key="'contentCover'"
                  @onSuccess="onSuccess"/>
                <p class="explain">建议尺寸：720 x 400像素，支持.jpg 、.png格式</p>
              </el-form-item>
            </el-col>
          </el-row>
          <div v-if="query.category == 2">
            <el-form-item label="内容来源" prop="sourceContentType">
              <el-radio-group v-model="form.sourceContentType">
                <el-radio :label="1">站内创建</el-radio>
                <el-radio :label="2">外链</el-radio>
              </el-radio-group>
            </el-form-item>
          </div>
          <el-row :gutter="50" v-if="query.category == 2 && form.sourceContentType != 2">
            <el-col :span="12">
              <el-form-item label="视频外部地址">
                <el-input
                  v-model.trim="externalUrl"
                  type="textarea"
                  style="max-width: 90%"
                  :autosize="{ minRows: 2, maxRows: 2}"
                  placeholder="请输入视频外部地址"
                  @change="inputChange">
                </el-input>
                <p class="explain">只支持使用本系统提供的上传工具复制的地址使用。其他外部视频地址不支持。
                  <br/>域名为yl-public.oss-cn-zhangjiakou.aliyuncs.com</p>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="50" v-if="query.category == 2 && form.sourceContentType != 2">
            <el-col :span="12">
              <el-form-item
                label="视频文件" 
                prop="vedioFileUrlKey" 
                :rules="{ required: true, message: '请上传视频文件', trigger: 'change' }"
                v-if="query.category == 2">
                <el-input style="display:none" v-model="form.vedioFileUrlKey"/>
                <yl-upload-file
                  class="upload-file" 
                  :action="info.action" 
                  :oss-key="'vedioContent'" 
                  :file-type="'mp4'" 
                  @onSuccess="onSuccessVideo">
                </yl-upload-file>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="query.category == 2">
              <div ref="dynamicVideo"></div>
            </el-col>
          </el-row>
          <div>
            <el-form-item label="内容归属医生">
              <div>
                <el-tag
                  closable 
                  type="info" 
                  class="item-el-tag" 
                  v-for="(item, index) in doctorData" 
                  :key="index" 
                  @close="doctorClose(item, index)"> 
                  {{ item.hospitalName }} - {{ item.hospitalDepartment }} - {{ item.doctorName }}
                </el-tag>
              </div>
              <span class="add-span" @click="addClick(5)">添加</span>
            </el-form-item>
          </div>
          <div>
            <el-form-item label="关联疾病">
              <div>
                <el-tag
                  closable 
                  type="info" 
                  class="item-el-tag" 
                  v-for="(item, index) in diseaseData" 
                  :key="index" 
                  @close="diseaseClose(item, index)"> 
                  {{ item.name }} 
                </el-tag>
              </div>
              <span class="add-span" @click="addClick(1)">添加</span>
            </el-form-item>
          </div>
          <div>
            <el-form-item label="关联药品">
              <div>
                <el-tag
                  closable 
                  type="info" 
                  class="item-el-tag" 
                  v-for="(item, index) in drugData" 
                  :key="index" 
                  @close="drugClose(item, index)"> 
                  {{ item.name }} 
                </el-tag>
              </div>
              <span class="add-span" @click="addClick(2)">添加</span>
            </el-form-item>
          </div>
          <div>
            <el-form-item label="关联科室">
              <div>
                <el-tag
                  closable 
                  type="info" 
                  class="item-el-tag" 
                  v-for="(item, index) in departmentData" 
                  :key="index" 
                  @close="departmentClose(item, index)"> 
                  {{ item.label }} 
                </el-tag>
              </div>
              <span class="add-span" @click="addClick(3)">添加</span>
            </el-form-item>
          </div>
          <div v-if="query.category == 1">
            <el-form-item label="内容来源" prop="sourceContentType">
              <el-radio-group v-model="form.sourceContentType">
                <el-radio :label="1">站内创建</el-radio>
                <el-radio :label="2">外链</el-radio>
              </el-radio-group>
            </el-form-item>
          </div>
          <div v-if="form.sourceContentType == 1">
            <el-form-item label="内容详情" prop="content">
              <wang-editor
                :height="height"
                :content="content"
                :extral-data="{type: 'richTextEditorFile'}"
                :handle-content="handleContent"/>
            </el-form-item>
          </div>
          <div v-if="form.sourceContentType == 2">
            <el-row :gutter="50">
              <el-col :span="12">
                <el-form-item label="H5链接" prop="linkUrl">
                  <el-input style="max-width: 90%" v-model.trim="form.linkUrl"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </div>
        </div>
      </el-form>
    </div>
    <div class="flex-row-center bottom-view">
      <yl-button type="primary" plain @click="$router.go(-1)">返回</yl-button>
      <yl-button type="primary" class="bottom-margin" @click="preservationClick('0')">保存</yl-button>
      <yl-button type="primary" plain v-if="query.type == 1 && query.category == 1" @click="preservationClick('1')">存为草稿</yl-button>
      <yl-button plain @click="previewClick">预览</yl-button>
    </div>
    <preview-dialog 
      title="预览"
      :visible.sync="showPreviewDialog"
      :content="form.content">
    </preview-dialog>
    <!-- 弹窗 -->
    <!-- 疾病/药品 弹窗 -->
    <dialog-disease-drugs
      :data-list="dataList"
      :show.sync="showDialog"
      :dialog-type="dialogType"
      :dialog-title="dialogTitle"
      @addCommodity="addCommodity"
      v-if="showDialog"/>
    <!-- 科室弹窗 -->
    <dialog-tree
      :data-list="departmentData"
      :show.sync="showDialog2"
      @addTree="addTree"
      v-if="showDialog2"/>
    <!-- 会议弹窗 -->
    <dialog-meeting
      :data-list="meetingData"
      :show.sync="showDialog3"
      @addMeeting="addMeeting"
      v-if="showDialog3"/>
    <!-- 内容所属医生 -->
    <dialog-doctor 
      :data-list="doctorData"
      :show.sync="showDialog5"
      @addDoctor="addDoctor"
      v-if="showDialog5"/>
  </div>
</template>

<script>
import { wangEditor, ylUpload, ylUploadFile } from '@/subject/admin/components'
import PreviewDialog from '@/common/components/PreviewDialog'
import { addContent, getContentById, updateContent } from '@/subject/admin/api/content_api/article_video'
import dialogDiseaseDrugs from './dialogDiseaseDrugs'
import dialogTree from './dialogTree'
import dialogMeeting from './dialogMeeting'
import dialogDoctor from './dialogDoctor'
export default {
  name: 'ContentEstablish',
  components: {
    wangEditor, 
    ylUpload, 
    PreviewDialog, 
    dialogDiseaseDrugs, 
    dialogTree, 
    ylUploadFile, 
    dialogMeeting,
    dialogDoctor
  },
  computed: {
    
  },
  data() {
    return {
      height: 600,
      form: {
        title: '',
        subtitle: '',
        //所属前台分类id
        categoryId: '', 
        cover: '',
        coverKey: '',
        source: '',
        author: '',
        // 状态：1-未发布 2-已发布
        status: 1, 
        isTop: 0,
        // displayLines: [],
        //内容
        content: '', 
        id: '',
        // 是否公开 0-否 1-是
        isOpen: 0, 
        //关联会议
        meetingId: 0, 
         //视频地址
        vedioFileUrlKey: '',
        //主讲人
        speaker: '',
        // 内容权限 1-仅登录 2-需认证通过
        viewLimit: 1,
        sourceContentType: 1,
        linkUrl: ''
      },
      rules: {
        title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
        subtitle: [{ required: true, message: '请输入副标题', trigger: 'blur' }],
        categoryId: [{ required: true, message: '请选择所属前台分类', trigger: 'change' }],
        isOpen: [{ required: true, message: '请选择是否公开', trigger: 'change' }],
        // displayLines: [{ required: true, message: '请选择业务引用', trigger: 'change' }],
        vedioFileUrlKey: [{ required: true, message: '请上传视频文件', trigger: 'change' }],
        cover: [{ required: true, message: '请上传视频封面', trigger: 'change' }],
        content: [{ required: true, message: '请输入内容详情', trigger: 'blur' }],
        sourceContentType: [{ required: true, message: '请选择内容来源', trigger: 'change' }],
        linkUrl: [{ required: true, message: '请输入H5链接地址', trigger: 'blur' }]
      },
      categoryData: [],
      //预览
      showPreviewDialog: false, 
      showDialog: false,
      dialogTitle: '',
      // 疾病数据
      diseaseData: [], 
      //药品数据
      drugData: [], 
      //科室数据
      departmentData: [], 
      // 弹窗类型 1 关联疾病 2 关联药品 3关联科室
      dialogType: 1, 
      //传递给自组建的数据
      dataList: [], 
      query: {
        // 1 文章 2视频
        category: 0, 
        id: 0,
        //1 新增 2 编辑
        type: 0 
      },
      //关联科室弹窗
      showDialog2: false, 
      //会议弹窗
      showDialog3: false, 
      // 所属医生弹窗
      showDialog5: false,
      info: {
        action: '/system/api/v1/file/upload'
      },
      //会议数据
      meetingData: [],
      content: '',
      // 是否显示内容权限
      limitShow: false,
      // 所属医生数据
      doctorData: [],
      // 视频外部地址
      externalUrl: ''
    }
  },
  mounted() {
    let query = this.$route.params;
    this.query = query;
    if (query.type && query.type != 1) {
      this.getContentData(query.id)
    }
  },
  methods: {
    // 视频外部地址 鼠标移出时 触发
    inputChange(value) {
      let value1 = value.lastIndexOf('.');
      let value2 = value.length;
      //获取文件后缀名
      let fileUrl = value.substring(value1, value2);
      if (fileUrl == '.mp4') {
        let val = value.indexOf('.com')
        // let url = value.slice(0, val + 4)
        let urlKey = value.slice(val + 5, value2)
        this.onSuccessVideo({
          url: value,
          key: urlKey
        })
      } else {
        this.$message.warning('请输入格式为.mp4的地址链接')
      }
    },
    // 业务引用 判断是否选中了医生
    // businessChange(e) {
    //   if (e && e.length > 0) {
    //     if (e.indexOf(2) > -1) {
    //       this.limitShow = true
    //     } else {
    //       this.limitShow = false
    //     }
    //   } else {
    //     this.limitShow = false
    //   }
    // },
    // 从父级传递过来的值
    addCommodity(row) {
      if (this.dialogType == 1) {
        this.diseaseData.push(row)
      } else if (this.dialogType == 2) {
        this.drugData.push(row)
      }
    },
    // 从组件传递过来的 会议信息
    addMeeting(val) {
      this.showDialog3 = false;
      this.form.meetingId = val.id;
      this.meetingData = [val];
    },
    // 从组件传过来的科室 信息
    addTree(row) {
      this.showDialog2 = false;
      this.departmentData = row;
    },
    // 从组件传过来的医生 信息
    addDoctor(row) {
      this.showDialog5 = false;
      this.doctorData = [row]
    },
    // 删除关联疾病
    diseaseClose(row,index) {
      this.diseaseData.splice(index, 1)
    },
    // 删除关联药品
    drugClose(row, index) {
      this.drugData.splice(index, 1)
    },
    // 删除科室数据
    departmentClose(row, index) {
      this.departmentData.splice(index, 1)
    },
    // 删除会议数据
    meetingClose(row, index) {
      this.form.meetingId = 0;
      this.meetingData.splice(index, 1)
    },
    // 删除内容归属医生
    doctorClose(row, index) {
      this.doctorData.splice(index, 1)
    },
    // 添加
    addClick(val) {
      switch (val) {
        case 1:
          this.dialogTitle = '关联疾病';
          this.dataList = this.diseaseData;
          this.dialogType = val;
          this.showDialog = true;
          break;
        case 2:
          this.dataList = this.drugData;
          this.dialogTitle = '关联药品';
          this.dialogType = val;
          this.showDialog = true;
          break;
        case 3:
          this.showDialog2 = true;
          break;
        case 4:
          this.showDialog3 = true;
          break;
        case 5:
          this.showDialog5 = true;
          break;
      }
    },
    // 获取 编辑数据
    async getContentData(id) {
      let data = await getContentById(id)
      if (data) {
        this.content = data.content;
        this.form = data;
        this.externalUrl = data.vedioFileUrl;
        if (data.vedioFileUrl && data.vedioFileUrl != '') {
          let newVideo = this.$refs.dynamicVideo;
          newVideo.innerHTML = `
          <video
            width="300" 
            height="160" 
            controls
            disablePictureInPicture
            controlslist="nodownload noplaybackrate">
            <source src="${data.vedioFileUrl}" type="video/mp4">
          </video>`
        }
        //疾病
        this.diseaseData = data.diseaseVOList; 
        //药品
        this.drugData = data.standardGoodsList; 
        //科室
        this.departmentData = data.hospitalDeptVOS; 
        //会议
        if (data.meetingName == '') {
          this.meetingData = [];
        } else {
          this.meetingData = [
            { 
              id: data.meetingId,
              title: data.meetingName
            }
          ]
        }
        // 内容所属医生
        if (data.docName) {
          this.doctorData = [
            {
              doctorName: data.docName,
              hospitalDepartment: data.hospitalDepartment,
              hospitalName: data.hospitalName,
              id: data.docId
            }
          ]
        }
        // 是否显示 内容权限
        // this.businessChange(data.displayLines)
      }
    },
    // 图片上传成功
    onSuccess(data) {
      if (data.key) {
        this.form.coverKey = data.key
        this.form.cover = data.url
      }
    },
    // 视频上传成功
    onSuccessVideo(data) {
      let newVideo = this.$refs.dynamicVideo;
      newVideo.innerHTML = `
      <video
        width="300" 
        height="160" 
        controls
        disablePictureInPicture
        controlslist="nodownload noplaybackrate">
        <source src="${data.url}" type="video/mp4">
      </video>`
      this.externalUrl = data.url;
      this.form.vedioFileUrlKey = data.key;
    },
     // 富文本编辑器回调
    handleContent(content, editor) {
      this.form.content = content
    },
    // 点击预览
    previewClick() {
      this.showPreviewDialog = true;
    },
    // 点击保存
    preservationClick(val) {
      this.$refs['dataForm'].validate( async(valid) => {
        if (valid) {
          // 科室ID 
          let departmentID = [];
          this.departmentData.forEach(e => {
            departmentID.push(e.id)
          })
          // 疾病ID
          let diseaseID = [];
          this.diseaseData.forEach(e => {
            diseaseID.push(e.id)
          });
          // 药品ID
          let drugID = [];
          this.drugData.forEach(e => {
            drugID.push(e.id)
          });
          let form = this.form;
          if (this.query.type == 1) {
            this.$common.showLoad();
            let data = await addContent(
              form.author,
              form.categoryId,
              form.content,
              this.query.category,
              form.coverKey,
              departmentID,
              diseaseID,
              // form.displayLines,
              val,
              form.isOpen,
              form.isTop,
              form.meetingId,
              form.source,
              form.speaker,
              drugID,
              form.status,
              form.subtitle,
              form.title,
              form.vedioFileUrlKey,
              this.limitShow ? form.viewLimit : '',
              this.doctorData && this.doctorData.length > 0 ? this.doctorData[0].id : 0,
              form.sourceContentType,
              form.linkUrl
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
              this.query.category,
              form.coverKey,
              departmentID,
              diseaseID,
              // form.displayLines,
              val,
              form.isOpen,
              form.isTop,
              form.meetingId,
              form.source,
              form.speaker,
              drugID,
              form.status,
              form.subtitle,
              form.title,
              form.vedioFileUrlKey,
              form.id,
              this.limitShow ? form.viewLimit : '',
              this.doctorData && this.doctorData.length > 0 ? this.doctorData[0].id : 0,
              form.sourceContentType,
              form.linkUrl
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
@import './index.scss';
</style>
