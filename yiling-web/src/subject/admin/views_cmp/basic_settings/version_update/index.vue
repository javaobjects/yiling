<template>
  <div class="app-container">
    <!-- 内容区域 -->
    <div class="app-container-content has-bottom-bar">
      <!-- 中部 搜索条件 -->
      <div class="common-box">
        <div class="search-box" >
          <el-row class="box">
            <el-col :span="12">
              <div class="title">App类型</div>
              <el-select class="select-width" v-model="query.appType" placeholder="请选择">
                <el-option
                  v-for="item in appType"
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
            <el-col :span="16">
              <yl-search-btn :total="query.total" @search="handleSearch" @reset="handleReset" />
            </el-col>
          </el-row>
        </div>
      </div>
      <div class="down-box clearfix">
        <div class="btn">
          <ylButton type="primary" @click="downLoadTemp">新建</ylButton>
        </div>
      </div>
      <!-- 下部列表 -->
      <div class="search-bar">
        <yl-table 
          border 
          :show-header="true" 
          :list="dataList" 
          :total="query.total" 
          :page.sync="query.page" 
          :limit.sync="query.limit" 
          :loading="loading" 
          @getList="getList">
          <el-table-column align="center" min-width="150" label="APP名称" prop="appName"></el-table-column>
          <el-table-column align="center" min-width="120" label="客户端">
           <template slot-scope="{ row }">
             {{ row.appType == 1 ? 'Android' : 'IOS' }}
           </template>
          </el-table-column>
          <el-table-column align="center" min-width="150" label="版本名称" prop="name"></el-table-column>
          <el-table-column align="center" min-width="100" label="版本号" prop="version">
          </el-table-column>
          <el-table-column align="center" min-width="80" label="是否强制升级">
            <template slot-scope="{ row }">
             {{ row.forceUpgradeFlag == 1 ? '是' : '否' }}
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="220" label="版本描述" >
            <template slot-scope="{ row }">
              <p>{{ row.description }}</p>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="150" label="上传时间" >
            <template slot-scope="{ row }">
              <p>{{ row.createTime | formatDate }}</p>
            </template>
          </el-table-column>
         <el-table-column fixed="right" align="center" label="操作" min-width="140">
            <template slot-scope="{ row }">
              <div>
                <yl-button type="text" @click="modifyClick(row)">编辑</yl-button>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
    <yl-dialog :title="title" @confirm="confirm" width="700px" :visible.sync="showDialog">
      <div class="dialogTc">
        <el-form :model="form" :rules="rules" ref="dataForm1" label-width="150px" class="demo-ruleForm">
          <el-form-item label="选择APP" prop="apps">
            <el-select class="select-width" style="width:90%" v-model="form.apps" placeholder="请选择">
              <el-option
                v-for="item in pageData"
                :key="item.id"
                :label="item.name"
                :value="item.id + ';' + item.appType + ';' + item.channelCode">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="版本名称" prop="name">
            <el-input v-model="form.name" style="width: 90%;" placeholder="请输入版本名称"
            ></el-input>
          </el-form-item>
          <el-form-item label="app下载地址" prop="packageUrl" >
             <yl-upload-file v-if="form.appType != 2" class="upload-file" :action="info.action" :massage="info.describe" :extral-data="info.extralData" :describe="info.describe" :file-type="'apk'" @onSuccess="onSuccess"></yl-upload-file>
            <el-input style="width:90%" v-model="form.packageUrl"/>
          </el-form-item>
          <el-form-item label="App版本号" prop="version">
           <el-input v-model="form.version" style="width: 90%" placeholder="请输入版本号"
            ></el-input>
          </el-form-item>
          <el-form-item label="文件校验MD5" v-if="form.appType != 2">
           <el-input v-model="form.packageMd5" style="width: 90%" disabled></el-input>
          </el-form-item>
          <el-form-item label="是否强制升级" prop="forceUpgradeFlag">
            <el-radio-group v-model="form.forceUpgradeFlag">
              <el-radio :label="1">是</el-radio>
              <el-radio :label="2">否</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="升级提示语">
            <el-input type="textarea" class="elInput" :rows="2" placeholder="请输入提示语" show-word-limit v-model="form.upgradeTips" maxlength="200"></el-input>
          </el-form-item>
          <el-form-item label="版本描述">
            <el-input type="textarea" class="elInput" :rows="3" placeholder="请输入内容" show-word-limit v-model="form.description" maxlength="200"></el-input>
          </el-form-item>
        </el-form>
      </div>
    </yl-dialog>
  </div>
</template>
<script>
import { versionPageList, listAppInfo, appSave } from '@/subject/admin/api/cmp_api/advertisement';
import { ylUploadFile } from '@/subject/admin/components';
export default {
  name: 'VersionUpdate',
  components: {
    ylUploadFile
  },
  computed: {
  },
  data() {
    return {
      query: {
        appType: 0,
        total: 0,
        page: 1,
        limit: 10
      },
      appType: [
         {
          label: '全部',
          value: 0
        },
        {
          label: 'Android',
          value: 1
        },
        {
          label: 'IOS',
          value: 2
        }
      ],
      loading: false,
      dataList: [],
      showDialog: false,
      form: {
        apps: '',
        id: '',
        name: '',
        version: '',
        upgradeTips: '',
        description: '',
        forceUpgradeFlag: 1,
        packageUrl: '',
        appId: '',
        appType: '',
        channelCode: '',
        packageMd5: '',
        packageSize: '',
        appUrl: ''
      },
      rules: {
        apps: [{ required: true, message: '请选择APP', trigger: 'change' }],
        sort: [{ required: true, message: '请输入排序', trigger: 'blur' }],
        name: [{ required: true, message: '请输入版本名称', trigger: 'blur' }],
        version: [{ required: true, message: '请输入版本号', trigger: 'blur' }],
        forceUpgradeFlag: [{ required: true, message: '请选择是否强制升级', trigger: 'change' }],
        packageUrl: [{ required: true, message: '请上传.apk文件或填写下载地址', trigger: 'blur' }]
      },
      title: '',
      pageData: [],
      info: {
        action: '/system/api/v1/file/upload',
        extralData: { type: 'appInstallPackage'},
        describe: '.apk'
      }
     
    }
  },
   activated() {
    this.appDataApi();
    this.getList();
  },
  watch: {
    'form.apps': {
        handler(newName, oldName) {
          if (newName!='') {
            let val = newName.split(';')
            this.form.appId = val[0];
            this.form.appType = val[1];
            this.form.channelCode = val[2];
          }
        },
        immediate: true,
        deep: true
      }
  },
  methods: {
    // 搜索
    handleSearch() {
      this.query.page = 1;
      this.getList();
    },
    // 重置
    handleReset() {
      this.query = {
        appType: 0,
        total: 0,
        page: 1,
        limit: 10
      }
    },
    // 获取app
    async appDataApi() {
      let data = await listAppInfo();
      if (data !== undefined) {
        this.pageData = data;
      }
    },
    onSuccess(val) {
      this.form.packageUrl = val.url;
      // this.form.appUrl = val.url;
      this.form.packageMd5 = val.md5;
      this.form.packageSize = val.size;
    },
    // 获取列表 
    async getList() {
      this.loading = true;
      let query = this.query;
      let data = await versionPageList(
        query.appType,
        query.page,
        query.limit
      )
      if (data!=undefined) {
        this.dataList = data.records;
        this.query.total = data.total;
      }
      this.loading = false;

    },
    getCellClass({row,rowIndex}) {
      return 'border-1px-b'
    },
    confirm() {
      this.$refs['dataForm1'].validate(async (valid) => {
        if (valid) {
          let form = this.form;
          this.$common.showLoad();
          let data = await appSave(
            form.appId,
            form.appType,
            form.appUrl,
            form.channelCode,
            form.description,
            form.forceUpgradeFlag,
            form.id,
            form.name,
            form.packageMd5,
            form.packageSize,
            form.packageUrl,
            form.upgradeTips,
            form.version
          )
          this.$common.hideLoad();
          if (data!=undefined) {
            this.showDialog = false;
             this.$common.n_success('保存成功!');
            this.getList();
          }
        }
      })
    },
    //新增
    downLoadTemp() {
      this.title = '新增';
      let val = '';
      let page = this.pageData;
      for (let i = 0; i < page.length; i++) {
        if (page[i].appType == 1) {
          val = page[i].id + ';' + page[i].appType + ';' + page[i].channelCode;
        }
      }
      this.form= {
        apps: val,
        id: '',
        name: '',
        version: '',
        upgradeTips: '',
        description: '',
        forceUpgradeFlag: 1,
        packageUrl: '',
        appId: '',
        appType: '',
        channelCode: '',
        packageMd5: '',
        packageSize: '',
        appUrl: ''
      };
      this.showDialog = true;
    },
    //编辑
    modifyClick(row) {
      this.title = '编辑';
      this.showDialog = true;
      this.form={
        apps: row.appId + ';' + row.appType + ';' + row.channelCode,
        id: row.id,
        name: row.name,
        version: row.version,
        upgradeTips: row.upgradeTips,
        description: row.description,
        forceUpgradeFlag: row.forceUpgradeFlag,
        packageUrl: row.packageUrl,
        appType: row.appType,
        packageMd5: row.packageMd5,
        packageSize: row.packageSize,
        appUrl: row.appUrl
      };
    },
    checkInput(val) {
      val = val.replace(/[^0-9]/gi, '')
      if (val > 200) {
        val = 200
      }
      if (val < 1) {
        val = ''
      }
      return val
    }

  }
}
</script>
<style lang="scss" scoped>
@import './index.scss';
</style>
