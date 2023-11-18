<template>
  <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <div class="c-box">
        <el-form :model="form" :rules="rules" ref="dataForm" label-width="150px" class="demo-ruleForm">
          <el-form-item label="banner标题" prop="title">
            <el-input v-model.trim="form.title" maxlength="10" show-word-limit></el-input>
          </el-form-item>
          <el-form-item label="分类">
            <el-radio-group v-model="form.usageScenario">
              <el-radio :label="1">主Banner</el-radio>
              <el-radio :label="2">副Banner</el-radio>
              <el-radio :label="3">会员中心Banner</el-radio>
              <el-radio :label="4">店铺Banner</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="上传Banner图片" prop="pic">
            <yl-upload
              :default-url="form.pic"
              :extral-data="{type: 'bannerPicture'}"
              @onSuccess="onSuccess"
            />
          </el-form-item>
          <p class="explain">建议尺寸：{{ form.usageScenario == 1 ? '710 X 260 像素' : ( form.usageScenario == 3 ? '690 X 180 像素' : '750 X 200 像素') }}，支持.jpg 、.gif、.png格式</p>
          <el-form-item label="排序" prop="sort">
            <el-input v-model="form.sort" style="width: 100px;"
              @input="e => (form.sort = checkInput(e))" placeholder="排序"
            ></el-input>
            <span class="font-size-base font-light-color" style="margin-left:20px;">排序逻辑 1-200，200权重最高，权重一样时候，将按照时间排序</span>
          </el-form-item>
           <el-form-item label="时间" prop="time">
            <el-date-picker
              v-model="form.time"
              type="daterange"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              range-separator="至"
              start-placeholder="开始时间"
              end-placeholder="结束时间">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="显示状态" prop="bannerStatus">
            <el-radio-group v-model="form.bannerStatus">
              <el-radio :label="1">启用</el-radio>
              <el-radio :label="2">停用</el-radio>
            </el-radio-group>
          </el-form-item>
          <!-- 投放店铺 -->
          <el-form-item label="投放店铺" v-if="form.usageScenario == 4">
            <span class="span-color" @click="shopClick">配置投放店铺</span>
            <div class="launch-shop">
              <yl-table
                border
                :show-header="true"
                :list="stopList">
                <el-table-column align="center" min-width="90" label="企业ID" prop="eid">
                </el-table-column>
                <el-table-column align="center" min-width="130" label="企业名称" prop="ename">
                </el-table-column>
                <el-table-column align="center" label="操作" width="100">
                  <template slot-scope="{ row, $index }">
                    <div>
                      <yl-button type="text" @click="removeClick(row, $index)">移除</yl-button>
                    </div>
                  </template>
                </el-table-column>
              </yl-table>
            </div>
          </el-form-item>
          <!-- <div class="launch-shop">
            
          </div> -->
          <el-form-item label="页面配置" prop="linkType">
            <el-select class="select-width" v-model="form.linkType" placeholder="请选择">
              <el-option
                v-for="item in bannerType"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="页面参数配置" prop="configuration">
            <div v-show="form.linkType == ''">
            </div>
            <div v-show="form.linkType == 1">
              <el-input v-model="form.configuration" placeholder="请输入跳转链接"></el-input>
            </div>
            <div v-show="form.linkType == 3">
              <el-input v-model="form.configuration" placeholder="请输入关键词"></el-input>
            </div>
            <div v-show="form.linkType == 4">
              <el-input v-model="form.configuration" placeholder="请输入商品ID"></el-input>
            </div>
            <div v-show="form.linkType == 5">
              <el-input v-model="form.configuration" placeholder="请输入店铺ID"></el-input>
            </div>
            <div v-show="form.linkType == 6">
              <el-input v-model="form.configuration" placeholder="请输入会员ID"></el-input>
            </div>
          </el-form-item>
        </el-form>

      </div>
    </div>
    <div class="bottom-view flex-row-center">
      <yl-button type="primary" plain @click="$router.go(-1)">返回</yl-button>
      <yl-button type="primary" @click="saveClick">保存</yl-button>
    </div>
    <!-- 配置投放店铺 -->
    <yl-dialog 
      title="配置投放店铺" 
      :show-cancle="false" 
      width="890px"
      @confirm="confirm"
      :visible.sync="showDialog">
      <div class="dialogTc">
        <div class="common-box">
          <div class="search-box">
            <el-row class="box">
              <el-col :span="12">
                <div class="title">企业ID</div>
                <el-input v-model.trim="dialog.eid" placeholder="请输入企业ID" @keyup.enter.native="searchEnter"/>
              </el-col>
              <el-col :span="12">
                <div class="title">企业名称</div>
                <el-input v-model.trim="dialog.ename" placeholder="请输入企业名称" @keyup.enter.native="searchEnter"/>
              </el-col>
            </el-row>
          </div>
          <div class="search-box mar-t-16">
            <el-row class="box">
              <el-col :span="16">
                <yl-search-btn :total="dialog.total" @search="handleSearch" @reset="handleReset"/>
              </el-col>
            </el-row>
          </div>
        </div>
        <div class="dialog-table">
          <yl-table
            border
            :show-header="true"
            :list="dialogData"
            :total="dialog.total"
            :page.sync="dialog.page"
            :limit.sync="dialog.limit"
            :loading="dialogLoading"
            @getList="getShopList">
            <el-table-column align="center" min-width="90" label="企业ID" prop="eid">
            </el-table-column>
            <el-table-column align="center" min-width="130" label="企业名称" prop="ename">
            </el-table-column>
            <el-table-column align="center" label="操作" width="120">
              <template slot-scope="{ row }">
                <span class="add-type" :style="{color: row.statusType == 1 ? '#1790ff' : '#c8c9cc'}" @click="choiceClick(row)">
                    {{ row.statusType == 1 ? '选择' : '已选择' }}
                </span>
              </template>
            </el-table-column>
          </yl-table>
        </div>
      </div>
    </yl-dialog>
  </div>
</template>
<script>
 import { bannerSave, bannerGetById, queryEnterpriseListPage } from '@/subject/admin/api/b2b_api/b2bAdministration'
import { ylUpload } from '@/subject/admin/components'
import { formatDate } from '@/subject/admin/utils'
import { b2bAppBannerType } from '@/subject/admin/busi/b2b/marketing_activity'
export default {
  name: 'PopBannerEdit',
  components: {
    ylUpload
  },
  computed: {
    bannerType() {
      return b2bAppBannerType()
    }
  },
  data() {
    return {
      form: {
        title: '',
        pic: '',
        bannerStatus: 1,
        linkType: '',
        time: [],
        sort: '',
        id: '',
        eid: '',
        activityLinks: '',
        searchKeywords: '',
        goodsManufacturer: '',
        goodsId: '',
        sellerEid: '',
        usageScenario: 1,
        configuration: ''
      },
      show: false,
      rules: {
        title: [{ required: true, message: '请输入banner标题', trigger: 'blur' }],
        pic: [{ required: true, message: '请上传banner图片', trigger: 'blur' }],
        bannerStatus: [{ required: true, message: '请选择状态', trigger: 'blur' }],
        time: [{ required: true, message: '请设置时间', trigger: 'blur' }],
        sort: [{ required: true, message: '请输入排序', trigger: 'blur' }],
        linkType: [{ required: true, message: '请选择页面配置', trigger: 'change' }],
        activityLinks: [{ required: true, message: '请输入跳转链接', trigger: 'blur' }],
        searchKeywords: [{ required: true, message: '请输入关键词', trigger: 'blur' }],
        goodsId: [{ required: true, message: '请输入商品ID', trigger: 'blur' }],
        sellerEid: [{ required: true, message: '请输入供应商ID', trigger: 'blur' }],
        configuration: [{ required: true, message: '请输入页面参数配置', trigger: 'blur' }]
      },
      //投放店铺数据
      stopList: [],
      //店铺弹窗
      showDialog: false,
      dialogData: [],
      dialogLoading: false,
      dialog: {
        eid: '',
        ename: '',
        total: 0,
        page: 1,
        limit: 10
      }
    }
  },
  mounted() {
    let query = this.$route.params;
    if (query.id && query.id !='0') {
      this.form.id = query.id;
      this.getData()
    }
  },
  methods: {
    // Enter
    searchEnter(e) {
      const keyCode = window.event ? e.keyCode : e.which;
      if (keyCode === 13) {
        this.getShopList()
      }
    },
    confirm() {
      this.showDialog = false
    },
    async getData() {
      let data = await bannerGetById(this.form.id)
      if (data) {
        let times = [];
        times = [formatDate(data.startTime, 'yyyy-MM-dd'), formatDate(data.stopTime, 'yyyy-MM-dd')]
        this.form = {
          ...data,
          time: times,
          goodsManufacturer: '',
          configuration: data.linkType == 1 || data.linkType == 6 ? data.activityLinks : (data.linkType == 3 ? data.searchKeywords : (data.linkType == 4 ? data.goodsId : (data.linkType == 5 ? data.sellerEid : '')))
        }
        this.stopList = data.bannerEnterpriseList
      }
    },
    saveClick() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          let form = this.form;
          if (form.usageScenario == 4 ) {
            if (this.stopList && this.stopList.length > 0) {
              this.$common.showLoad();
              let data1 = await bannerSave(
                form.linkType == 1 || form.linkType == 6 ? form.configuration : '',
                form.bannerStatus,
                form.eid,
                form.linkType == 4 ? form.configuration : '',
                form.goodsManufacturer,
                form.id,
                form.linkType,
                form.pic,
                form.linkType == 3 ? form.configuration : '',
                form.linkType == 5 ? form.configuration : '',
                form.sort,
                form.time && form.time.length > 0 ? form.time[0] : '',
                form.time && form.time.length > 1 ? form.time[1] : '',
                form.title,
                form.usageScenario,
                this.stopList
              )
              this.$common.hideLoad();
              if (data1 !== undefined) {
                this.$common.alert('保存成功', r => {
                  this.$router.go(-1)
                })
              }
            } else {
              this.$common.warn('请配置投放店铺！')
            }
          } else {
            this.$common.showLoad();
            let data = await bannerSave(
              form.linkType == 1 || form.linkType == 6 ? form.configuration : '',
              form.bannerStatus,
              form.eid,
              form.linkType == 4 ? form.configuration : '',
              form.goodsManufacturer,
              form.id,
              form.linkType,
              form.pic,
              form.linkType == 3 ? form.configuration : '',
              form.linkType == 5 ? form.configuration : '',
              form.sort,
              form.time && form.time.length > 0 ? form.time[0] : '',
              form.time && form.time.length > 1 ? form.time[1] : '',
              form.title,
              form.usageScenario,
              []
            )
            this.$common.hideLoad();
            if (data !== undefined) {
              this.$common.alert('保存成功', r => {
                this.$router.go(-1)
              })
            }
          }
        } else {
          return false
        }
      })
    },
    //点击配置投放店铺
    shopClick() {
      this.showDialog = true;
      //获取弹窗投放店铺列表
      this.getShopList();
    },
    async getShopList() {
      this.dialogData = [];
      let dialog = this.dialog;
      this.dialogLoading = true;
      let data = await queryEnterpriseListPage(
        dialog.eid,
        dialog.ename,
        dialog.page,
        dialog.limit
      )
      if (data) {
        data.records.forEach(element => {
          this.dialogData.push({
            ...element,
            statusType: 1
          });
        });
        // 判断会员权益是否已经添加过了
        if (this.stopList && this.stopList.length > 0) {
          for (let i = 0; i < this.dialogData.length; i ++) {
            for (let y = 0; y < this.stopList.length; y ++) {
              if (this.dialogData[i].eid == this.stopList[y].eid) {
                this.dialogData[i].statusType = 2
                break;
              } else {
                this.dialogData[i].statusType = 1
              }
            }
          }
        }
        this.dialog.total = data.total

      }
      this.dialogLoading = false
    },
    // 点击 选择 
    choiceClick(row) {
      if (row.statusType == 1) {
        this.stopList.push(row)
      }
      row.statusType = 2;
    },
    //点击移除投放店铺
    removeClick(row, index) {
      row.statusType = 1;
      this.stopList.splice(index, 1)
    },
    // 图片上传成功
    async onSuccess(data) {
      if (data.key) {
        this.form.pic = data.url
      }
    },
    handleSearch() {
      this.dialog.page = 1;
      this.getShopList();
    },
    handleReset() {
      this.dialog = {
        eid: '',
        ename: '',
        total: 0,
        page: 1,
        limit: 10
      }
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
  @import "./index.scss";
</style>
