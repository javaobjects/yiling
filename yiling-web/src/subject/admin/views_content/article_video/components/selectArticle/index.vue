<template>
  <div>
    <!-- 文章 -->
    <yl-dialog
      width="900px"
      title="选择文章"
      :show-footer="false"
      :show-flose="false"
      :visible.sync="showDialog"
      @close="close">
      <div class="pop-up">
        <div class="common-box">
          <div class="search-box">
            <el-row class="box">
              <el-col :span="8">
                <div class="title">文章标题</div>
                <el-input v-model="dialog.title" placeholder="请输入文章标题" @keyup.enter.native="searchEnter"/>
              </el-col>
              <el-col :span="8">
                <div class="title">内容所属医生</div>
                <el-autocomplete
                  v-model="dialog.doctorName"
                  :fetch-suggestions="querySearchAsync"
                  placeholder="请输入内容所属医生"
                  @select="handleSelect"
                  clearable>
                </el-autocomplete>
              </el-col>
              <el-col :span="8">
                <div class="title">创建来源</div>
                <el-select class="select-width" v-model="dialog.createSource" placeholder="请选择">
                  <el-option
                    v-for="item in createSourceData"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                  </el-option>
                </el-select>
              </el-col>
            </el-row>
          </div>
          <div class="search-box mar-t-16">
            <el-row class="box">
              <el-col :span="8">
                <div class="title">创建者</div>
                <el-input v-model="dialog.createUserName" @keyup.enter.native="searchEnter" placeholder="请输入创建者姓名" />
              </el-col>
              
            </el-row>
          </div>
          <div class="search-box mar-t-16">
            <el-row class="box">
              <el-col :span="16">
                <yl-search-btn
                  :total="dialog.total"
                  @search="handleSearch"
                  @reset="handleReset"/>
              </el-col>
            </el-row>
          </div>
        </div>
        <div class="table-conter">
          <yl-table
            border
            show-header
            :list="dialogData"
            :total="dialog.total"
            :page.sync="dialog.page"
            :limit.sync="dialog.limit"
            :loading="loading"
            @getList="getList">
            <el-table-column align="center" min-width="150" label="文章名称" prop="title"></el-table-column>
            <el-table-column align="center" min-width="100" label="内容所属医生" prop="docName"></el-table-column>
            <el-table-column align="center" min-width="90" label="创建来源">
              <template slot-scope="{ row }">
                {{ row.createSource | dictLabel(createSourceData) }}
              </template>
            </el-table-column>
            <el-table-column align="center" min-width="120" label="创建时间">
              <template slot-scope="{ row }">
                {{ row.createTime | formatDate }}
              </template>
            </el-table-column>
            <el-table-column fixed="right" align="center" label="操作" min-width="80">
              <template slot-scope="{ row }">
                <yl-button type="text" @click="seeClick(row)">
                  <span :style="{color: row.statusType == 1 ? '#1790ff' : '#c8c9cc'}">
                    {{ row.statusType == '1' ? '选择' : '已添加' }}
                  </span>
                </yl-button>
              </template>
            </el-table-column>
          </yl-table>
        </div>
      </div>
    </yl-dialog>
  </div>
</template>

<script>
import { queryContentPage, queryDoctorByNameAndHospital } from '@/subject/admin/api/content_api/article_video'
import { cmsContentCreateSource } from '@/subject/admin/busi/content/article_video'
export default {
  props: {
    // 是否显示 弹窗
    showDialog: {
      type: Boolean,
      default: true
    },
    contentId: {
      type: String,
      default: ''
    },
    //来源  0-所有 1-站内创建 2-外链
    source: {
      type: Number,
      default: 1
    }
  },
  computed: {
    createSourceData() {
      return cmsContentCreateSource()
    }
  },
  data() {
    return {
      dialog: {
        title: '',
        doctorName: '',
        //内容所属医生ID
        docId: '',
        //创建来源
        createSource: '',
        //创建者
        createUserName: '',
        total: 0,
        page: 1,
        limit: 10
      },
      loading: false,
      dialogData: []
    }
  },
  watch: {
    'dialog.doctorName'(newValue, oldValue) {
      if (newValue == '') {
        this.dialog.docId = ''
      }
    }
  },
  mounted() {
    this.getList();
  },
  methods: {
    // Enter
    searchEnter(e) {
      const keyCode = window.event ? e.keyCode : e.which;
      if (keyCode === 13) {
        this.getList()
      }
    },
    handleSearch() {
      this.dialog.page = 1;
      this.getList();
    },
    handleReset() {
      this.dialog = {
        title: '',
        doctorName: '',
        docId: '',
        createSource: '',
        createUserName: '',
        total: 0,
        page: 1,
        limit: 10
      }
    },
    //点击关闭按钮
    close() {
      this.$emit('close')
    },
    async querySearchAsync(queryString, cb) {
      var results = [];
      if (queryString == '') {
        cb(results)
      } else {
        this.$common.showLoad();
        let data = await queryDoctorByNameAndHospital(
          queryString,
          '',
          1,
          50
        )
        this.$common.hideLoad();
        if (data !== undefined) {
          if (data.records && data.records.length > 0) {
            for (let i = 0; i < data.records.length; i ++) {
              results.push({
                value: data.records[i].doctorName + '-' + data.records[i].hospitalName,
                name: data.records[i].doctorName,
                id: data.records[i].id
              })
            }
            cb(results)
          } else {
            results.push({
              value: '暂未搜索到数据',
              name: '',
              id: ''
            })
            cb(results)
          }
        } else {
          results = [];
          cb(results)
        }
      }
    },
    handleSelect(item) {
      if (item.id == '') {
        this.dialog.docId = '';
        this.dialog.doctorName = '';
      } else {
        this.dialog.docId = item.id;
        this.dialog.doctorName = item.name
      }
    },
    async getList() {
      let dialog = this.dialog;
      this.loading = true;
      if (dialog.createUserName != '' && dialog.createSource == '') {
        this.$message.warning('请选择创建来源')
      } else {
        this.dialogData = [];
        let data = await queryContentPage(
          1,
          dialog.page,
          '',
          '',
          '',
          dialog.limit,
          dialog.title,
          dialog.docId,
          dialog.createUserName,
          '',
          dialog.createSource,
          this.source
        )
        if (data) {
          for (let i = 0; i < data.records.length; i ++) {
            this.dialogData.push({
              ...data.records[i],
              statusType: this.contentId == data.records[i].id ? 2 : 1
            })
          }

          this.dialog.total = data.total
        }
      }
      this.loading = false;
    },
    //点击选择
    seeClick(row) {
      this.$emit('addArticle', row)
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
