<template>
  <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <div class="c-box">
        <el-form :model="form" :rules="rules" ref="dataForm" label-width="80px">
          <div class="title">基础会员设置</div>
          <el-row :gutter="100">
            <el-col :span="12">
              <el-form-item label="会员名称" prop="name">
                <el-input v-model="form.name" maxlength="10" show-word-limit placeholder="请输入会员名称"></el-input>
              </el-form-item>
              <el-form-item label="会员描述">
                <el-input
                  style="width: 500px"
                  type="textarea"
                  :rows="2"
                  placeholder="请输入会员描述"
                  maxlength="30" 
                  show-word-limit
                  v-model="form.description">
                </el-input>
              </el-form-item>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="会员图标" prop="lightPictureUrl">
                    <span>点亮</span>
                    <yl-upload
                      :default-url="form.lightPictureUrl"
                      :extral-data="{type: 'memberLightPicture'}"
                      @onSuccess="onSuccess1"/>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="" prop="extinguishPictureUrl">
                    <span>熄灭</span>
                    <yl-upload
                      :default-url="form.extinguishPictureUrl"
                      :extral-data="{type: 'memberLightPicture'}"
                      @onSuccess="onSuccess2"/>
                  </el-form-item>
                </el-col>
                <p class="explain">建议尺寸：48 x 48像素，支持.jpg 、.png格式</p>
              </el-row>
              <el-form-item label="会员卡面" prop="bgPictureUrl">
                <yl-upload
                  :default-url="form.bgPictureUrl"
                  :extral-data="{type: 'memberBackgroundPicture'}"
                  @onSuccess="onSuccess3"
                />
              </el-form-item>
              <span class="explain">建议尺寸：666 x 320像素，支持.jpg 、.png格式</span>
            </el-col>
            <el-col :span="12">
              <!-- 卡片 -->
              <div class="box-right" v-if="form.bgPictureUrl">
                <div class="card-box" :style="`background-image: url(${ form.bgPictureUrl })`">
                  <div class="card-box-top">
                    <div>{{ form.name }}</div>
                  </div>
                  <div class="card-box-conter">
                    {{ form.description }}
                  </div>
                  <div class="card-box-bottom">
                    <div class="card-item-title">累计已省:</div>
                    <div class="money"><span>¥</span><span>970.17</span></div>
                  </div>
                  <div class="card-box-time">
                    2099.12.31到期
                  </div>
                </div>
              </div>
            </el-col>
          </el-row>
          <el-form-item label="获得条件" required class="mar-t-16">
            <div class="check-box-item">
              <div class="font-size-base check-box-item-right">
                <div v-for="(item,index) in form.memberBuyStageList" :key="index" class="check-item">
                  <el-form-item :prop="'memberBuyStageList.' + index + '.price'" :rules="{ required: true, message: '请输入', trigger: 'blur' }">
                    <el-input class="my-check-item-input" v-model="item.price" @keyup.native="item.price = onInput(item.price, 2)" placeholder="请输入"></el-input>
                  </el-form-item>
                  <span class="unit">元</span>
                  <span class="check-time">有效期</span>
                  <el-form-item :prop="'memberBuyStageList.' + index + '.validTime'" :rules="{ required: true, message: '请输入', trigger: 'blur' }">
                    <el-input class="my-check-item-input" v-model="item.validTime" @keyup.native="item.validTime = onInput(item.validTime, 0)" placeholder="请输入"></el-input>
                  </el-form-item>
                  <span class="unit">天</span>
                  <span class="check-time">展示名称</span>
                  <el-form-item :prop="'memberBuyStageList.' + index + '.name'" :rules="{ required: true, message: '请输入展示名称', trigger: 'blur' }">
                    <el-input 
                      style="width:125px"
                      v-model.trim="item.name"
                      maxlength="5" 
                      show-word-limit 
                      placeholder="请输入">
                    </el-input>
                  </el-form-item>
                  <span class="check-time">标签名称</span>
                  <el-form-item>
                    <el-input 
                      style="width:125px"
                      v-model.trim="item.tagName"
                      maxlength="5" 
                      show-word-limit 
                      placeholder="请输入">
                    </el-input>
                  </el-form-item>
                  <yl-button v-if="index == 0" class="add-btn" type="text" @click="addGradient">增加购买条件</yl-button>
                  <yl-button v-else class="add-btn" type="text" @click="removeGradient(index)">删除</yl-button>
                </div>
              </div>
            </div>
          </el-form-item>
          <el-form-item label="会员权益">
            <span class="span-color" @click="configureClick">配置会员权益</span>
            <div>
              <yl-table
                border
                :show-header="true"
                :list="equityList"
                :loading="equityLoading">
                <el-table-column align="center" min-width="80" label="权益图标">
                  <template slot-scope="{ row }">
                    <span class="equity-img">
                      <img :src="row.icon" alt="">
                    </span>
                  </template>
                </el-table-column>
                <el-table-column align="center" min-width="120" label="权益名称" prop="name">
                </el-table-column>
                <el-table-column align="center" min-width="100" label="权益类型">
                  <template slot-scope="{ row }">
                    <span>
                      {{ row.type == 1 ? '系统生成' : '自定义' }}
                    </span>
                  </template>
                </el-table-column>
                <el-table-column align="center" min-width="130" label="权益说明" prop="description">
                </el-table-column>
                <el-table-column align="center" min-width="80" label="是否启用">
                  <template slot-scope="{ row }">
                    <span>{{ row.status == 1 ? '启用' : '禁用' }}</span>
                  </template>
                </el-table-column>
                <el-table-column align="center" min-width="80" label="排序" prop="sort"></el-table-column>
                <el-table-column align="center" label="操作" width="120">
                  <template slot-scope="{ row, $index }">
                    <div>
                      <yl-button type="text" @click="sortClick(row, $index)">排序</yl-button>
                      <yl-button type="text" @click="removeClick(row, $index)">移除</yl-button>
                    </div>
                  </template>
                </el-table-column>
              </yl-table>
            </div>
          </el-form-item>
          <div class="title title-border">其他设置</div>
          <el-form-item label="续卡提醒" prop="renewalWarn">
            <el-radio-group v-model="form.renewalWarn" @change="renewalWarnChange">
              <div class="radio-item">
                <el-radio :label="0">不提醒</el-radio>
                <el-radio :label="1">提醒</el-radio>
              </div>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="" prop="warnDays" v-if="form.renewalWarn == 1">
            距离到期前 <el-input class="form-item-input" v-model="form.warnDays" @keyup.native="form.warnDays = onInput(form.warnDays, 0)" placeholder="请输入" /> 天
          </el-form-item>
          <el-form-item label="排序" prop="sort">
            <el-input v-model="form.sort" style="width: 100px;"
              @input="e => (form.sort = checkInput(e))" placeholder="排序"
            ></el-input>
            <span class="font-size-base font-light-color" style="margin-left:20px;">排序逻辑 1-200，200权重最高，权重一样时候，将按照时间排序</span>
          </el-form-item>
        </el-form>
      </div>
    </div>
    <div class="bottom-view flex-row-center">
      <yl-button type="primary" plain @click="$router.go(-1)">返回</yl-button>
      <yl-button type="primary" @click="save">保存</yl-button>
    </div>
    <!-- 配置会员权益弹窗 -->
    <yl-dialog 
      title="配置会员权益"
      :show-cancle="false" 
      width="890px"
      @confirm="confirm"
      :visible.sync="showDialog">
      <div class="dialogTc">
        <div class="common-box">
          <div class="search-box">
            <el-row class="box">
              <el-col :span="12">
                <div class="title">权益名称</div>
                <el-input v-model.trim="dialog.name" placeholder="请输入权益名称" @keyup.enter.native="searchEnter"/>
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
            @getList="getMemberList">
            <el-table-column align="center" min-width="80" label="权益图标">
              <template slot-scope="{ row }">
                <span class="equity-img">
                  <img :src="row.icon" alt="">
                </span>
              </template>
            </el-table-column>
            <el-table-column align="center" min-width="120" label="权益名称" prop="name">
            </el-table-column>
            <el-table-column align="center" min-width="100" label="权益类型">
              <template slot-scope="{ row }">
                <span>
                  {{ row.type == 1 ? '系统生成' : '自定义' }}
                </span>
              </template>
            </el-table-column>
            <el-table-column align="center" min-width="130" label="权益说明" prop="description">
            </el-table-column>
            <el-table-column align="center" min-width="80" label="最后修改时间">
              <template slot-scope="{ row }">
                <span>{{ row.updateTime | formatDate }}</span>
              </template>
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
    <!-- 配置会员权益排序 -->
    <yl-dialog 
      title="排序"
      :show-cancle="false" 
      width="570px"
      @confirm="sortConfirm"
      :visible.sync="sortDialog">
      <div class="dialogTc">
        <el-form :model="sortForm" :rules="sortRules" ref="dataForm1" label-width="80px" class="demo-ruleForm">
          <el-form-item label="排序" prop="sort">
            <el-input v-model="sortForm.sort" style="width: 200px;"
              @input="e => (sortForm.sort = checkInput(e))" placeholder="排序"
            ></el-input>
          </el-form-item>
        </el-form>
      </div>
    </yl-dialog>
  </div>
</template>

<script>
import { createMember, getMember, memberEquityListPage, updateMember } from '@/subject/admin/api/b2b_api/membership'
import { onInputLimit } from '@/common/utils'
import { ylUpload } from '@/subject/admin/components'
export default {
  name: 'PopBannerEdit',
  components: {
    ylUpload
  },
  computed: {
  },
  data() {
    return {
      form: {
        id: '',
        name: '',
        description: '',
        lightPicture: '',
        lightPictureUrl: '',
        extinguishPicture: '',
        extinguishPictureUrl: '',
        bgPicture: '',
        bgPictureUrl: '',
        memberBuyStageList: [
          {
            price: '',
            validTime: '',
            name: '',
            tagName: ''
          }
        ],
        renewalWarn: 0,
        warnDays: '',
        sort: ''
      },
      rules: {
        name: [{ required: true, message: '请输入会员名称', trigger: 'blur' }],
        lightPictureUrl: [{ required: true, message: '请上传会员点亮图片', trigger: 'blur' }],
        extinguishPictureUrl: [{ required: true, message: '请上传会员熄灭图片', trigger: 'blur' }],
        bgPictureUrl: [{ required: true, message: '请上传会员卡面', trigger: 'blur' }],
        memberEquityList: [{ type: 'array', required: true, message: '请选择会员权益', trigger: 'change' }],
        renewalWarn: [{ required: true, message: '请选择续费提醒', trigger: 'change' }],
        warnDays: [{ required: true, message: '请输入距离到期时间', trigger: 'blur' }],
        price: [{ required: true, message: '请输入', trigger: 'blur' }],
        validTime: [{ required: true, message: '请输入', trigger: 'blur' }],
        sort: [{ required: true, message: '请输入排序', trigger: 'blur' }]
      },
      selected: {},
      // 权益列表
      rightsList: [],
      equityLoading: false,
      // 会员权益数据
      equityList: [],
      // 权益弹窗
      showDialog: false,
      dialog: {
        name: '',
        total: 0,
        page: 1,
        limit: 10
      },
      // 权益弹窗数据
      dialogData: [],
      dialogLoading: false,
      //排序数据
      sortDialog: false,
      sortForm: {
        sort: '',
        index: ''
      },
      sortRules: {
        sort: [{ required: true, message: '请输入排序', trigger: 'blur' }]
      }
    }
  },
  mounted() {
    let queryID = this.$route.params
    if (queryID.id) {
      this.getData(queryID.id)
    }
  },
  methods: {
     // Enter
    searchEnter(e) {
      const keyCode = window.event ? e.keyCode : e.which;
      if (keyCode === 13) {
        this.getMemberList();
      }
    },
    // 点击配置会员权益弹窗确认
    confirm() {
      this.showDialog = false;
    },
    //点击排序
    sortClick(row, index) {
      this.sortDialog = true;
      this.sortForm = {
        sort: row.sort ? row.sort : '',
        index: index
      }
    },
    // 排序保存
    sortConfirm() {
      this.$refs['dataForm1'].validate((valid) => {
        if (valid) {
          let val = this.sortForm;
          this.equityList[val.index].sort = val.sort;
          this.sortDialog = false;
          this.sortForm = { 
            sort: '',
            index: ''
          }
        }
      })
    },
    async getData(id) {
      let data = await getMember(id)
      if (data) {
        this.form = data;
        // 会员权益数据
        this.equityList = data.memberEquityList;
      }
    },
    // 点击续卡提醒
    renewalWarnChange() {
      this.form.warnDays = ''
    },
    // 添加购买条件
    addGradient() {
      this.form.memberBuyStageList.push({
        price: '',
        validTime: '',
        name: '',
        tagName: ''
      })
    },
    // 删除购买条件
    removeGradient(index) {
      this.form.memberBuyStageList.splice(index, 1)
    },
    save() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          if (this.equityList && this.equityList.length > 0) {
            let equityData = [];
            for (let i = 0; i < this.equityList.length; i ++) {
              equityData.push({
                equityId: this.equityList[i].id,
                sort: this.equityList[i].sort
              })
            }
            let form = this.form
            if (form.id) {
              // 修改
              this.$common.showLoad()
              let data1 = await updateMember(
                form.id,
                form.name,
                form.description,
                form.lightPicture,
                form.extinguishPicture,
                form.bgPicture,
                form.memberBuyStageList,
                equityData,
                form.renewalWarn,
                form.warnDays,
                form.sort
              )
              this.$common.hideLoad()
              if (data1 != undefined) {
                this.$common.alert('修改成功', r => {
                  this.$router.go(-1)
                })
              }
            } else {
              // 新增
              this.$common.showLoad()
              let data = await createMember(
                form.name,
                form.description,
                form.lightPicture,
                form.extinguishPicture,
                form.bgPicture,
                form.memberBuyStageList,
                equityData,
                form.renewalWarn,
                form.warnDays,
                form.sort
              )
              this.$common.hideLoad()
              if (data != undefined) {
                this.$common.alert('保存成功', r => {
                  this.$router.go(-1)
                })
              }
            }
          } else {
            this.$common.warn('请配置会员权益！')
          }
        } else {
          return false
        }
      })
    },
    // 校验两位小数
    onInput(value, limit) {
      return onInputLimit(value, limit)
    },
    // 点亮图标
    onSuccess1(row) {
      if (row) {
        this.form.lightPictureUrl = row.url;
        this.form.lightPicture = row.key
      }
    },
    // 熄灭图标
    onSuccess2(row) {
      if (row) {
        this.form.extinguishPictureUrl = row.url;
        this.form.extinguishPicture = row.key
      }
    },
    // 会员卡面图片
    onSuccess3(row) {
      if (row) {
        this.form.bgPictureUrl = row.url;
        this.form.bgPicture = row.key
      }
    },
    // 点击配置会员权益
    configureClick() {
      this.showDialog = true;
      // 获取配置权益信息
      this.getMemberList();
    },
    async getMemberList() {
      this.dialogData = [];
      let dialog = this.dialog;
      this.dialogLoading = true;
      let data = await memberEquityListPage(
        dialog.page,
        dialog.limit,
        '',
        dialog.name,
        1
      )
      if (data) {
        data.records.forEach(element => {
          this.dialogData.push({
            ...element,
            statusType: 1
          });
        });
        // 判断会员权益是否已经添加过了
        if (this.equityList && this.equityList.length > 0) {
          for (let i = 0; i < this.dialogData.length; i ++) {
            for (let y = 0; y < this.equityList.length; y ++) {
              if (this.dialogData[i].id == this.equityList[y].id) {
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

      this.dialogLoading = false;
    },
    // 权益弹窗 点击搜索
    handleSearch() {
      this.dialog.page = 1;
      // 获取配置权益信息
      this.getMemberList();
    },
    // 权益弹窗 点击清空查询条件
    handleReset() {
      this.dialog = {
        name: '',
        total: 0,
        page: 1,
        limit: 10
      }
    },
    // 点击配置会员权益 的选择
    choiceClick(row) {
      if (row.statusType == 1) {
        this.equityList.push(row)
      }
      row.statusType = 2;
    },
    // 点击移除会员权益
    removeClick(row, index) {
      row.statusType = 1;
      this.equityList.splice(index, 1)
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
