<template>
  <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <div class="c-box conter-top">
        <div class="title">温馨提示</div>
        <p>( 1 ) 公众号最多创建3个一级菜单，一级菜单名称名字不多于4个汉字或8个字母。</p>
        <p>( 2 ) 公众号每个一级菜单下的子菜单最多可创建5个，子菜单名称名字不多于8个汉字或16个字母。</p>
      </div>
      <el-form 
        :model="form" 
        :rules="rules" 
        ref="dataForm" 
        label-width="120px"
        v-if="form.dataList && form.dataList.length > 0">
        <div class="c-box mar-t-16" v-for="(item, index) in form.dataList" :key="index">
          <div class="title conter-title">
            菜单{{ index == 0 ? '一' : (index == 1 ? '二' : '三') }}
            <div class="conter-title-right">
              <span :style="{color: item.status == 1 ? '#1790ff' : '#BFBFBF'}">{{ item.status == 1 ? '启用' : '禁用' }}</span>
              <el-switch
                class="switch"
                v-model="item.status"
                active-color="#1790ff"
                inactive-color="#BFBFBF"
                active-value="1"
                inactive-value="2"
                @change="switchChange(item)">
              </el-switch>
            </div>
          </div>
          <div class="conter mar-t-16">
            <el-form-item 
              label="名称：" 
              class="item-button"
              :prop="'dataList.' + index + '.name'"
              :rules="item.status == 1 ? { required: true, message: '名称不能为空', trigger: 'blur'} : {}">
              <el-input v-model.trim="item.name" :disabled="item.status == 2">
                <el-button 
                  v-if="item.status == 1" 
                  slot="append" 
                  @click="insertClick(index)" 
                  style="color: #409EFF;font-size: 20px" 
                  icon="el-icon-circle-plus">
                </el-button>
              </el-input>
            </el-form-item>
            <!-- 当存在 子级的话 一下内容不展示 -->
            <div v-if="item.subButton && item.subButton.length < 1">
              <el-form-item
                label="消息类型："
                :prop="'dataList.' + index + '.type'"
                :rules="item.status == 1 ? { required: true, message: '消息类型不能为空', trigger: 'blur'} : {}">
                <el-radio-group v-model="item.type" :disabled="item.status == 2" @change="radioChange(item)">
                  <el-radio label="view">跳转网页</el-radio>
                  <el-radio label="miniprogram">跳转小程序</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="网页链接：" 
                v-if="item.type == 'view'"
                :prop="'dataList.' + index + '.url'"
                :rules="item.status == 1 ? { required: true, message: '网页链接不能为空', trigger: 'blur'} : {}">
                <el-input v-model.trim="item.url" placeholder="请输入网页链接" :disabled="item.status == 2"></el-input> 
              </el-form-item>
              <div v-if="item.type == 'miniprogram'">
                <el-form-item label="小程序URL：" 
                  :prop="'dataList.' + index + '.url'"
                  :rules="item.status == 1 ? { required: true, message: '小程序URL不能为空', trigger: 'blur'} : {}">
                  <el-input v-model.trim="item.url" placeholder="请输入小程序URL" :disabled="item.status == 2"></el-input>
                </el-form-item>
                <el-form-item 
                  label="小程序appid："
                  :prop="'dataList.' + index + '.appid'"
                  :rules="item.status == 1 ? { required: true, message: 'appid不能为空', trigger: 'blur'} : {}">
                  <el-input v-model.trim="item.appid" placeholder="请输入小程序appid" :disabled="item.status == 2"></el-input>
                </el-form-item>
                <el-form-item 
                  label="pagepath："
                  :prop="'dataList.' + index + '.pagepath'"
                  :rules="item.status == 1 ? { required: true, message: 'pagepath不能为空', trigger: 'blur'} : {}">
                  <el-input v-model.trim="item.pagepath" placeholder="请输入pagepath" :disabled="item.status == 2"></el-input>
                </el-form-item>
              </div>
            </div>
          </div>
          <!-- 子菜单 -->
          <div class="submenu" v-if="item.subButton && item.subButton.length > 0">
            <div class="submenu-conter" v-for="(item2, index2) in item.subButton" :key="index2">
              <div class="title submenu-title">
              子级菜单 {{ index2 + 1 }}
                <div class="conter-delete" @click="deleteClick(index, index2)"><i class="el-icon-close"></i></div>
                <div class="conter-title-right" style="margin-top: 5px;">
                  <span :style="{color: item2.status == 1 ? '#1790ff' : '#BFBFBF'}">{{ item2.status == 1 ? '启用' : '禁用' }}</span>
                  <el-switch
                    class="switch"
                    v-model="item2.status"
                    active-color="#1790ff"
                    inactive-color="#BFBFBF"
                    active-value="1"
                    inactive-value="2"
                    @change="switchChange2(item, item2)">
                  </el-switch>
                </div>
              </div>
              <div class="conter mar-t-16">
                <el-form-item 
                  label="名称：" 
                  class="item-button"
                  :prop="`dataList[${index}].subButton[${index2}].name`"
                  :rules="item2.status == 1 ? { required: true, message: '名称不能为空', trigger: 'blur'} : {}">
                  <el-input v-model.trim="item2.name" :disabled="item2.status == 2">
                    <el-button v-if="item2.status == 1" slot="append" @click="submenuInsertClick(index, index2)" style="color: #409EFF;font-size: 20px" icon="el-icon-circle-plus"></el-button>
                  </el-input>
                </el-form-item>
                <el-form-item 
                  label="消息类型："
                  :prop="`dataList[${index}].subButton[${index2}].type`"
                  :rules="item2.status == 1 ? { required: true, message: '消息类型不能为空', trigger: 'blur'} : {}">
                  <el-radio-group v-model="item2.type" :disabled="item2.status == 2" @change="radioChange2(item2)">
                    <el-radio label="view">跳转网页</el-radio>
                    <el-radio label="miniprogram">跳转小程序</el-radio>
                  </el-radio-group>
                </el-form-item>
                <el-form-item label="网页链接：" 
                  v-if="item2.type == 'view'"
                  :prop="`dataList[${index}].subButton[${index2}].url`"
                  :rules="item2.status == 1 ? { required: true, message: '网页链接不能为空', trigger: 'blur'} : {}">
                  <el-input v-model.trim="item2.url" placeholder="请输入网页链接" :disabled="item2.status == 2"></el-input>
                </el-form-item>
                <div v-if="item2.type == 'miniprogram'">
                  <el-form-item label="小程序URL：" 
                    :prop="`dataList[${index}].subButton[${index2}].url`"
                    :rules="item2.status == 1 ? { required: true, message: '小程序URL不能为空', trigger: 'blur'} : {}">
                    <el-input v-model.trim="item2.url" placeholder="请输入小程序URL" :disabled="item2.status == 2"></el-input>
                  </el-form-item>
                  <el-form-item 
                    label="小程序appid："
                    :prop="`dataList[${index}].subButton[${index2}].appid`"
                    :rules="item2.status == 1 ? { required: true, message: 'appid不能为空', trigger: 'blur'} : {}">
                    <el-input v-model.trim="item2.appid" placeholder="请输入小程序appid" :disabled="item2.status == 2"></el-input>
                  </el-form-item>
                  <el-form-item 
                    label="pagepath："
                    :prop="`dataList[${index}].subButton[${index2}].pagepath`"
                    :rules="item2.status == 1 ? { required: true, message: 'pagepath不能为空', trigger: 'blur'} : {}">
                    <el-input v-model.trim="item2.pagepath" placeholder="请输入pagepath" :disabled="item2.status == 2"></el-input>
                  </el-form-item>
                </div>
              </div>
            </div>
          </div>
          <div class="add-submenu">
            <span v-if="item.subButton && item.subButton.length < 5" @click="addSubmenuClick(item, index)"><i class="el-icon-plus"></i>添加子菜单</span>
          </div>
        </div>
      </el-form>
    </div>
    <div class="bottom-view flex-row-center">
      <yl-button type="primary" @click="preserveClick">提交</yl-button>
    </div>
    <!-- 弹窗 -->
    <yl-dialog
      width="600px"
      title="插入图标"
      :show-footer="true"
      :visible.sync="showDialog"
      @confirm="confirm">
      <div class="pop-up">
        <div class="dialog-icon">
          <p>请选择图标：</p>
          <span 
            v-for="(item, index) in iconData"
            :key="'iconData' + index" 
            :class="index == iconInd ? 'icon-color' : ''" 
            @click="iconClick(index)">
            {{ item.value }}
          </span>
        </div>
      </div>
    </yl-dialog>

  </div>
</template>

<script>
import { menuListInfo, publishMenu } from '@/subject/admin/api/cmp_api/official_account'
export default {
  name: 'Menu',
  data() {
    return {
      value: '',
      form: {
        dataList: []
      },
      rules: {
        dataList: [{ required: true, message: '请填写菜单', trigger: 'blur'} ]
      },
      iconData: [
        {
          value: '👉'
        },
        {
          value: '🧧'
        },
        {
          value: '❗️'
        },
        {
          value: '👇'
        },
        {
          value: '🔍'
        },
        {
          value: '🔥'
        }
      ],
      //弹窗
      showDialog: false,
      iconInd: -1,
      //图标插入当前第几条数据
      indexs: 0,
      //图标插入当前 子集第几条数据
      indexs2: -1
    }
  },
  activated() {
    this.getList();
  },
  methods: {
     //点击插入按钮
    insertClick(index) {
      this.iconInd = -1;
      this.indexs = index;
      this.showDialog = true;
    },
    //点击子级 插入图标
    submenuInsertClick(index, index2) {
      this.iconInd = -1;
      this.indexs = index;
      this.indexs2 = index2;
      this.showDialog = true;
    },
    //点击图标
    iconClick(val) {
      this.iconInd = val
    },
    confirm() {
      if (this.iconInd != -1 && this.indexs2 == -1) {
        this.form.dataList[this.indexs].name = this.form.dataList[this.indexs].name + this.iconData[this.iconInd].value;
      } else if (this.iconInd != -1 && this.indexs2 != -1) {
        this.form.dataList[this.indexs].subButton[this.indexs2].name = this.form.dataList[this.indexs].subButton[this.indexs2].name + this.iconData[this.iconInd].value;
      }
      this.showDialog = false;
      this.indexs2 = -1
    },
    //点击添加 子菜单
    addSubmenuClick(item, index) {
      if (item.subButton && item.subButton.length < 5) {
        this.form.dataList[index].subButton.push({
          type: 'view',
          name: '',
          url: '',
          appid: '',
          pagepath: '',
          status: item.status == 1 ? '1' : '2'
        })
      } else {
        this.$common.warn('最多配置 5 项子菜单')
      }
    },
    //获取菜单
    async getList() {
      let data = await menuListInfo()
      if (data) {
        this.form.dataList = data.button;
      }
    },
    //点击删除
    deleteClick(index, index2) {
      this.form.dataList[index].subButton.splice(index2, 1)
    },
    //点击父级开关
    switchChange(item) {
      if (item.status == 2) {
        if (item.subButton && item.subButton.length > 0) {
          item.subButton.forEach(element => {
            element.status = 2
          });
        }
      }
    },
    //点击子级开关
    switchChange2(item, item2) {
      if (item2.status == 1) {
        item.status = '1'
      }
    },
    //点击父级消息类型切换
    radioChange(item) {
      item.url = ''
    },
    //点击子级 消息类型切换
    radioChange2(item2) {
      item2.url = ''
    },
    preserveClick() {
      this.$refs['dataForm'].validate( async(valid) => {
        if (valid) {
          this.$common.showLoad();
          let data = await publishMenu(
            this.form.dataList
          )
          this.$common.hideLoad();
          if (data !== undefined) {
            this.$common.n_success('提交成功')
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