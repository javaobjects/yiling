<template>
  <div class="app-container">
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <div class="app-container-content has-bottom-bar">
      <div class="common-box new-role">
        <el-form
          ref="dataForm"
          :rules="rules"
          :model="form"
          label-width="110px"
          label-position="top">
          <el-row>
            <el-col :span="9">
              <el-form-item label="角色名称" prop="name">
                <el-input maxlength="10" placeholder="请输入角色名称" v-model="form.name"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="9">
              <el-form-item label="启停状态" prop="status">
                <el-radio-group v-model="form.status">
                  <el-radio :label="1">启用</el-radio>
                  <el-radio :label="2">停用</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="9">
              <el-form-item label="角色说明">
                <el-input
                  v-model="form.remark"
                  type="textarea"
                  placeholder="请输入角色说明"
                  maxlength="50"
                  show-word-limit
                  :autosize="{ minRows: 3, maxRows: 4}" />
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
      <div class="mar-t-16">
        <el-tabs v-model="activeTab" @tab-click="handleTabClick">
          <el-tab-pane
            v-for="(item, index) in topList"
            v-show="item.name"
            :key="index"
            :index="(item.appId + '')"
            :label="item.name"
            :name="item.appId + ''">
          </el-tab-pane>
        </el-tabs>
      </div>
      <div>
        <div v-for="(item, index) in treeData" :key="index">
          <div v-show="item.appId == activeTab">
            <role-tree
              :ref="'ref' + item.appId"
              :show-role="item.dataScopeFlag"
              :role="item.dataScope"
              :data="item.menuList">
            </role-tree>
          </div>
        </div>
      </div>
      <div class="bottom-bar-view flex-row-center">
        <yl-button type="primary" plain @click="$router.go(-1)">返回</yl-button>
        <yl-button type="primary" @click="save">保存</yl-button>
      </div>
    </div>
  </div>
</template>

<script>
  import roleTree from '../component/RoleTree'
  import { mapGetters } from 'vuex'
  import { getAllMenus, getRoleInfo, updateRole, createRole } from '@/subject/pop/api/zt_api/role'

  export default {
    name: 'ZtRoleDetail',
    components: {
      roleTree
    },
    computed: {
      ...mapGetters([
        'topList'
      ])
    },
    data() {
      return {
        // 头部导航
        navList: [
          {
            title: '首页',
            path: '/zt_dashboard'
          },
          {
            title: '权限管理',
            path: '/zt_roles/roles_index'
          },
          {
            title: '角色详情'
          }
        ],
        // 总的数据
        form: {},
        // tab
        activeTab: '',
        rules: {
          name: [{ required: true, message: '请输入角色名称', trigger: 'blur' }],
          status: [{ required: true, message: '请选择角色状态', trigger: 'change' }]
        },
        // 全部菜单
        allRolesArray: [],
        // 各平台
        treeData: {},
        // 总的数据
        data: {}
      }
    },
    mounted() {
      if (this.topList.length) {
        this.activeTab = this.topList[0].appId + ''
      }
      this.getData()
    },
    methods: {
      // 初始化数据
      getData() {
        this.id = this.$route.params.id
        if (this.id) {
          this.getSingeRoleInfo(this.id)
        } else {
          this.getAllList()
        }
      },
      // 处理返回数组
      handleList(list) {
        this.treeData = list
      },
      // 获取单独角色下信息
      async getSingeRoleInfo(id) {
        this.$common.showLoad()
        let data = await getRoleInfo(id)
        this.$common.hideLoad()
        if (data) {
          this.form = {
            id: data.id,
            status: data.status,
            name: data.name,
            remark: data.remark
          }
          this.handleList(data.systemPermissionList)
        }
      },
      // 获取全部菜单
      async getAllList() {
        this.$common.showLoad()
        let data = await getAllMenus()
        this.$common.hideLoad()
        if (data) {
          this.handleList(data.list)
        }
      },
      // 最终保存
      save() {
        this.$refs.dataForm.validate(async valid => {
          if (valid) {
            let form = this.form
            let array = []
            let needDataRole = ''
            this.treeData.map(item => {
              const obj = this.$refs[`ref${item.appId}`][0].getChooseNodes(item.appId)
              if (item.dataScopeFlag && !obj.dataScope) {
                const topItem = this.topList.find(i => i.appId === item.appId)
                needDataRole += ' ' + topItem.name + ' '
              }
              array.push(obj)
            })
            if (needDataRole) {
              this.$common.warn(`请先选择${needDataRole}的数据权限`)
              return
            }
            let data = null
            this.$common.showLoad()
            if (this.id) {
              data = await updateRole(form.id, form.name, form.remark, array, form.status)
            } else {
              data = await createRole(form.name, form.remark, array, form.status)
            }
            this.$common.hideLoad()
            if (typeof data !== 'undefined') {
              this.showDialog = false
              if (this.id) {
                this.$common.n_success('修改角色成功')
                this.getData()
              } else {
                this.$common.n_success('添加角色成功')
                this.$router.go(-1)
              }
            }
          } else {
            return false
          }
        })
      },
      // tab点击
      handleTabClick() {
        // console.log('tab')
      }
    }
  }
</script>

<style lang="scss" scoped>
  @import "./index.scss";
</style>
