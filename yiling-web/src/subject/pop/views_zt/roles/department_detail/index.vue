<template>
  <div class="app-container">
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <div class="app-container-content has-bottom-bar">
      <div class="common-box common-header">
        <div class="header-bar"><div class="sign"></div>部门信息</div>
        <div class="header" v-show="!edit">
          <el-row>
            <el-col :span="12">
              <div class="text"><span>部门ID：</span>{{ form.id }}</div>
              <div class="text"><span>部门负责人：</span>{{ form.managerName || '- -' }}</div>
              <div class="text"><span>上级部门：</span>{{ form.parentName || '- -' }}</div>
            </el-col>
            <el-col :span="12">
              <div class="text"><span>部门名称：</span>{{ form.name }}</div>
              <div class="text"><span>部门人数：</span>{{ form.employeeNum }}</div>
              <div class="text"><span>部门编码：</span>{{ form.code || '- -' }}</div>
            </el-col>
          </el-row>
        </div>
        <div class="edit-box" v-show="edit">
          <el-form
            ref="dataForm"
            :rules="rules"
            :model="form"
            label-position="top">
            <el-row class="box">
              <el-col :span="8">
                <el-form-item label="部门ID" prop="id">
                  <el-input :value="form.id" disabled />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="部门名称" prop="name">
                  <el-input maxlength="15" v-model="form.name" placeholder="请输入要查询的部门名称"/>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="部门负责人" prop="people">
                  <el-select
                    v-model="form.managerName"
                    filterable
                    remote
                    reserve-keyword
                    placeholder="请输入需搜索的员工名称"
                    @focus="remoteMethod()"
                    :remote-method="remoteMethod"
                    :loading="userLoading">
                    <div slot="prefix" class="el-icon-search"></div>
                    <el-option
                      v-for="item in userList"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id">
                      <span style="float: left">{{ item.name }}</span>
                      <span style="float: right;">{{ item.mobile }}</span>
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="box">
              <el-col :span="8">
                <el-form-item label="部门人数（人）" prop="num">
                  <el-input :value="form.employeeNum" disabled />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="上级部门" prop="parentId">
                  <el-select
                    v-model="form.parentId"
                    placeholder="请选择上级部门">
                    <el-option :value="form.parentId" :label="form.parentName" style="height: auto; padding: 0;">
                      <department-tree
                        node-key="id"
                        :current-node-key="form.parentId"
                        :props="{ children: 'children', label: 'name' }"
                        @node-click="handleNodeClick">
                      </department-tree>
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="部门编码" prop="code">
                  <el-input maxlength="20" v-model="form.code" placeholder="请输入部门编码"/>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </div>
        <div class="down-box">
          <ylButton v-if="!edit" type="primary" plain @click="editInfo">编辑</ylButton>
          <ylButton v-else type="primary" @click="editDone">完成</ylButton>
        </div>
      </div>
      <!-- 底部列表 -->
      <div class="common-box table-box">
        <div class="header-bar"><div class="sign"></div>部门人员</div>
        <yl-table
          class="mar-t-8"
          border
          :show-header="true"
          :list="dataList"
          :total="query.total"
          :page.sync="query.page"
          :limit.sync="query.limit"
          :loading="loading"
          @getList="getList">
          <el-table-column label="序号" min-width="70" align="center" type="index">
          </el-table-column>
          <el-table-column label="姓名" min-width="139" align="center" prop="name">
          </el-table-column>
          <el-table-column label="手机号" min-width="139" align="center" prop="mobile">
          </el-table-column>
          <el-table-column label="工号" min-width="139" align="center" prop="code">
          </el-table-column>
          <el-table-column label="角色" min-width="139" align="center" prop="roleName">
          </el-table-column>
<!--          <el-table-column label="状态" min-width="139" align="center" prop="userCount">-->
<!--            <template slot-scope="{ row }">-->
<!--              <span :class="[row.status == 1 ? 'col-down' : 'col-up']">{{ row.status == 1 ? '启用' : '停用' }}</span>-->
<!--            </template>-->
<!--          </el-table-column>-->
        </yl-table>
      </div>
      <div class="bottom-bar-view flex-row-center">
        <yl-button type="primary" plain @click="$router.go(-1)">返回</yl-button>
      </div>
    </div>
  </div>
</template>

<script>
  import departmentTree from '../component/DepartmentTree'
  import {
    getDepartmentDetail,
    getCompanyUserList,
    editDepartment
  } from '@/subject/pop/api/zt_api/role'

  export default {
    name: 'ZtRolesDepartmentDetail',
    components: {
      departmentTree
    },
    computed: {
    },
    data() {
      const checkSame = (rule, value, callback) => {
        if (value == this.form.id) {
          callback(new Error('不可选择自身'))
        } else {
          callback()
        }
      }
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
            title: '部门详情'
          }
        ],
        loading: false,
        query: {
          page: 1,
          limit: 10,
          status: 0
        },
        // 列表数据
        dataList: [],
        // 负责人数据
        userList: [],
        // 负责人等待
        userLoading: false,
        // 编辑
        edit: false,
        form: {
          parentId: null
        },
        rules: {
          name: [{required: true, message: '请输入部门名称', trigger: 'blur'}],
          parentId: [
            {required: false, message: '请选择上级部门', trigger: 'change'},
            {required: false, validator: checkSame, trigger: 'change'}
          ]
        }
      }
    },
    mounted() {
      this.departmentId = this.$route.params.id
      this.oldForm = null
      if (this.departmentId) {
        this.getData()
        this.getList()
      }
    },
    methods: {
      // 详情
      async getData() {
        this.$common.showLoad()
        let data = await getDepartmentDetail(
          this.departmentId
        )
        this.$common.hideLoad()
        if (data) {
          data.parentId = data.parentId ? data.parentId : null
          this.form = data
        }
      },
      // 员工列表
      async getList() {
        this.loading = true
        let query = this.query
        let data = await getCompanyUserList(
          query.page,
          query.limit,
          null,
          null,
          0,
          null,
          null,
          this.departmentId
        )
        this.loading = false
        if (data) {
          this.dataList = data.records
          this.query.total = data.total
        }
      },
      // 搜索点击
      handleSearch() {
        this.query.page = 1
        this.getList()
      },
      handleReset() {
        this.query = {
          page: 1,
          limit: 10,
          status: 0
        }
      },
      // 编辑信息
      editInfo() {
        this.oldForm = JSON.stringify(this.form)
        this.edit = true
      },
      // 修改完成
      editDone() {
        this.$refs.dataForm.validate(async valid => {
          if (valid) {
            let form = this.form
            let newForm = JSON.stringify(form)
            if (newForm === this.oldForm) {
              this.edit = false
              this.$common.message('暂无修改')
            } else {
              this.$common.showLoad()
              const managerId = typeof form.managerName === 'number' ? form.managerName : form.managerId
              let data = await editDepartment(form.id, form.name, form.parentId, form.code, null, managerId)
              this.$common.hideLoad()
              if (typeof data !== 'undefined') {
                this.edit = false
                this.$common.n_success('修改成功')
                this.getData()
              }
            }
          } else {
            return false
          }
        })
      },
      // 远程搜索员工
      async remoteMethod(query) {
        this.userLoading = true;
        let data = await getCompanyUserList(
          1,
          999,
          null,
          query,
          0,
          null,
          null,
          this.departmentId
        )
        this.userLoading = false
        if (data) {
          this.userList = data.records
        }
      },
      // 部门点击
      handleNodeClick(data) {
        let form = this.$common.clone(this.form)
        form.parentName = data.name
        form.parentId = data.id
        this.form = form
      }
    }
  }
</script>

<style lang="scss" scoped>
  @import "./index.scss";
</style>
