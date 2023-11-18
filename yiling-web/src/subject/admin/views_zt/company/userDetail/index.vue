<template>
  <div class="app-container" v-if="row.id">
    <div class="app-container-content">
    <div class="header-bar"><div class="sign"></div>企业联系人信息</div>
    <div class="container-header flex-row-left">
      <div class="flex1">
        <span class="font-light-color">账号ID：</span>
        {{ row.id }}
        <span class="font-important-color" style="padding-left: 30px;">
          <span class="font-light-color">创建时间：</span>{{ row.createTime | formatDate }}
        </span>
      </div>
      <div>
        <el-switch
          v-if="row.status != 9"
          :value="row.status === 1"
          @change="switchChange"
          active-text="启用"
          inactive-text="停用">
        </el-switch>
      </div>
    </div>

    <div class="header-bar"><div class="sign"></div>个人信息</div>
    <div class="c-box">
      <div class="flex-row-left">
        <span class="font-light-color">姓名：</span>
        <el-input v-model="row.name" :disabled="!edit.name" placeholder="请输入姓名" />
        <div v-if="row.status != 9">
          <yl-button v-if="!edit.name" type="text" @click="edit.name = true">编辑</yl-button>
          <yl-button v-else type="text" @click="editDone(1)" >确认修改</yl-button>
        </div>
      </div>
    </div>
    <div class="c-box">
      <div class="flex-row-left">
        <span class="font-light-color">手机号：</span>
        <el-input 
          v-model="row.mobile" 
          :disabled="!edit.mobile" 
          placeholder="请输入手机号" 
        />
        <div v-if="row.status != 9">
          <yl-button v-if="!edit.mobile" type="text" @click="edit.mobile = true">变更手机号</yl-button>
          <yl-button v-else type="text" @click="editDone(2)" >确认修改</yl-button></div>
      </div>
    </div>
    <div v-if="row.idCardPhotoVO.frontPhoto || row.idCardPhotoVO.backPhoto">
        <div class="header-bar">
        <div class="sign"></div>实名认证信息</div>
        <div class="c-box">
            <div class="flex-row-left">
                <span class="font-light-color">身份证号：</span>
                <el-input 
                  :value="row.idNumber" 
                  placeholder="" 
                  size="normal" 
                  clearable 
                  disabled="disabled"
                ></el-input>
            </div>
        </div>
        <div class="c-box userimg">
            <div class="imgbox">
                <el-image class="img" :src="row.idCardPhotoVO.frontPhoto" :preview-src-list="[row.idCardPhotoVO.frontPhoto]" ></el-image>
                <span>手持证件照（正）</span>
            </div>
            <div class="imgbox">
                <el-image class="img" :src="row.idCardPhotoVO.backPhoto" :preview-src-list="[row.idCardPhotoVO.backPhoto]" ></el-image>
                <span>手持证件照（反）</span>
            </div>
        </div>
    </div>
    <div v-if="row.enterpriseList.length>0">
        <div class="header-bar flex-wrap"><div class="sign"></div>企业信息</div>
        <div class="com-card">
        <el-card shadow="hover" v-for="(item, index) in row.enterpriseList" :key="index">
          <div class="font-size-base flex-row-left">
            <div class="flex1">
              <div class="font-title-color flex1">{{ item.name }}</div>
              <span class="font-light-color">{{ item.roleName }}</span>
            </div>
            <yl-button 
              size="mini" 
              plain 
              type="primary" 
              @click="unbind(item.id, item.status)"
              v-if="row.status != 9"
              >{{ item.status === 2 ? '启用' : '停用' }}</yl-button>
          </div>
        </el-card>
      </div>
    </div>
    </div>
  </div>
</template>

<script>
  import { getCompanyUserDetail, updateCompanyUser, updateCompanyUserName, updateCompanyTel, unbindCompanyUser } from '@/subject/admin/api/zt_api/company';
  import { validateTel } from '@/subject/admin/utils/validate'

  export default {
    name: 'ZtCompanyUserDetail',
    components: {
    },
    data() {
      return {
        form: {},
        edit: {
          name: false,
          mobile: false
        },
        row: {}
      }
    },
    mounted() {
      this.query = this.$route.params;
      if (this.query.id) {
        this.getData()
      }
    },
    methods: {
      async getData() {
        let data = await getCompanyUserDetail(this.query.id)
        if (data) {
          this.row = Object.assign(data.staffVO, {
            enterpriseList: data.enterpriseList,
            idCardPhotoVO: data.idCardPhotoVO
          })
          this.mobile = this.row.mobile
        }
      },
      // 解除关系
      unbind(companyId, status) {
        if (companyId) {
          if (status === 1) {
            this.$common.confirm('该员工将不能登录该企业，请确认是否进行停用操作？', r => {
              if (r) {
                this.changeStatus(companyId, status)
              }
            }, null, null, null, '员工停用确认')
          } else {
            this.changeStatus(companyId, status)
          }
        }
      },
      async changeStatus(companyId, status) {
        let type = status === 1 ? 2 : 1
        this.$common.showLoad()
        let data = await unbindCompanyUser(this.row.id, companyId, type)
        this.$common.hideLoad()
        if (data && data.result) {
          this.$common.n_success(type === 2 ? '停用成功' : '启用成功')
          this.getData()
        }
      },
      editDone(type) {
        if (type === 1) {
          // 修改名字
          let name = this.row.name
          if (name) {
            this.changeInfo(type, name)
          } else {
            this.$common.error('姓名不可为空')
          }
        } else if (type === 2) {
          // 修改手机号
          let mobile = this.row.mobile
          if (!validateTel(mobile)) {
            this.$common.error('请输入正确的手机号')
            return
          }
          if (mobile === this.mobile) {
            this.edit.mobile = false
            this.$common.message('无修改')
            return
          }
          if (mobile) {
            this.$common.confirm(`由于手机号是重要的登录方式，请确认将手机号修改为${mobile}吗？`, async r => {
              if (r) {
                this.changeInfo(type, mobile)
              }
            })
          } else {
            this.$common.error('手机号不可为空')
          }
        }
      },
      // 修改名字以及手机号
      async changeInfo(type, desc) {
        let data = null
        this.$common.showLoad()
        if (type === 1) {
          // 修改名字
          data = await updateCompanyUserName(this.row.id, desc)
        } else if (type === 2) {
          // 修改手机号
          data = await updateCompanyTel(this.row.id, desc)
        }
        this.$common.hideLoad()
        if (data && data.result) {
          if (type === 1) {
            this.edit.name = false
          } else if (type === 2) {
            this.edit.mobile = false
            this.mobile = desc
          }
          this.$common.n_success('修改成功')
        }
      },
      switchChange(e) {
        const update = async () => {
          this.$common.showLoad()
          let data = await updateCompanyUser(this.row.id, e ? 1 : 2)
          this.$common.hideLoad()
          if (data && data.result) {
            this.row.status = e ? 1 : 2
          }
        }
        if (!e) {
          this.$common.confirm('确认停用该账号？', async r => {
            if (r) {
              update()
            }
          })
        } else {
          update()
        }
      }
    }
  }
</script>

<style lang="scss" scoped>
  @import "./index.scss";
</style>
