<template>
  <div class="app-container">
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">部门名称</div>
              <el-input v-model="query.name" placeholder="请输入要查询的部门名称" @keyup.enter.native="handleSearch" />
            </el-col>
            <el-col :span="8">
              <div class="title">部门状态</div>
              <div class="radio-group flex-row-left">
                <el-radio-group v-model="query.status">
                  <el-radio :label="0">全部</el-radio>
                  <el-radio :label="1">启用</el-radio>
                  <el-radio :label="2">停用</el-radio>
                </el-radio-group>
              </div>
            </el-col>
          </el-row>
        </div>
        <div class="search-box">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn
                :total="query.total"
                @search="handleSearch"
                @reset="handleReset"
              />
            </el-col>
          </el-row>
        </div>
      </div>
      <!-- 新增按钮 -->
      <div class="down-box clearfix">
        <div class="btn">
          <ylButton v-role-btn="['1']" type="primary" plain @click="addNewRole">新增部门</ylButton>
        </div>
      </div>
      <!-- 底部列表 -->
      <div class="mar-t-8">
        <yl-table
          border
          :show-header="true"
          :list="dataList"
          :total="query.total"
          :page.sync="query.page"
          :limit.sync="query.limit"
          :loading="loading"
          @getList="getList">
          <el-table-column label="ID" min-width="90" align="center" prop="id">
          </el-table-column>
          <el-table-column label="部门名称" min-width="139" align="center" prop="name">
          </el-table-column>
          <el-table-column label="部门负责人" min-width="100" align="center">
            <template slot-scope="{ row }">
              <span class="role-desc">{{ row.managerName || '- -' }}</span>
            </template>
          </el-table-column>
          <el-table-column label="部门人数" min-width="100" align="center" prop="employeeNum">
          </el-table-column>
          <el-table-column label="上级部门" min-width="139" align="center" prop="parentName">
            <template slot-scope="{ row }">
              <div>{{ row.parentName || '- -' }}</div>
            </template>
          </el-table-column>
          <el-table-column label="状态" min-width="100" align="center">
            <template slot-scope="{ row }">
              <span :class="[row.status == 1 ? 'col-down' : 'col-up']">{{ row.status == 1 ? '启用' : '停用' }}</span>
            </template>
          </el-table-column>
          <el-table-column label="创建信息" min-width="100" align="center">
            <template slot-scope="{ row }">
              <div>{{ row.createUserName }}</div>
              <span>{{ row.createTime | formatDate() }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" min-width="139" fixed="right" align="center">
            <template slot-scope="{ row }">
              <yl-button v-role-btn="['2']" type="text" @click="editRole(row)">编辑</yl-button>
              <yl-button v-role-btn="['3']" v-if="row.status == 1" type="text" @click="updateRoles(row, 2)">停用</yl-button>
              <yl-button v-role-btn="['3']" v-else type="text" @click="updateRoles(row, 1)">启用</yl-button>
              <yl-button v-role-btn="['4']" type="text" v-show="row.employeeNum" @click="addAll(row)">批量转移</yl-button>
            </template>
          </el-table-column>
        </yl-table>
      </div>
      <yl-dialog
        title="新增部门"
        width="540px"
        @confirm="confirmAddNew"
        :visible.sync="showDialog">
        <div class="new-role">
          <el-form
            ref="dataForm"
            :rules="rules"
            :model="form"
            label-width="150px"
            label-position="right">
            <el-form-item label="上级部门" prop="parentId">
              <el-select
                v-model="form.parentId"
                ref="select1"
                placeholder="请选择上级部门">
                <el-option :value="form.parentId" :label="form.parentName" style="height: auto; padding: 0;">
                  <department-tree
                    node-key="id"
                    :props="{ children: 'children', label: 'name' }"
                    @node-click="handleNodeClick">
                  </department-tree>
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="部门名称" prop="name">
              <el-input maxlength="15" placeholder="请输入部门名称" v-model="form.name"></el-input>
            </el-form-item>
            <el-form-item label="部门编码" prop="code">
              <el-input maxlength="20" placeholder="请输入部门编码" v-model="form.code"></el-input>
            </el-form-item>
          </el-form>
        </div>
      </yl-dialog>
      <yl-dialog
        title="批量转移"
        width="500px"
        @confirm="confirmAddAll"
        :visible.sync="showDialog1">
        <div class="pad-16 role-all flex-row-center">
          <el-form
            class="form-view"
            ref="dataForm1"
            :rules="rules1"
            :model="form1"
            label-position="left">
            <div class="title">
              批量将{{ form1.name }}部门下人员转移至
            </div>
            <el-form-item prop="parentId">
              <el-select
                v-model="form1.parentId"
                ref="select"
                placeholder="请选择部门">
                <el-option :value="form1.parentId" :label="form1.parentName" style="height: auto; padding: 0;">
                  <department-tree
                    node-key="id"
                    :current-node-key="form1.parentId"
                    :disable-key="form1.id"
                    :props="{ children: 'children', label: 'name' }"
                    @node-click="handleAllNodeClick">
                  </department-tree>
                </el-option>
              </el-select>
            </el-form-item>
            <div class="flex-row-center">
              <yl-tool-tip class="tooltip">
                执行操作后，人员将马上拥有新部门的权限
              </yl-tool-tip>
            </div>
          </el-form>
        </div>
      </yl-dialog>
    </div>
  </div>
</template>

<script>
  import departmentTree from '../component/DepartmentTree'
  import {
    addDepartment,
    transferDepart,
    changeDepartStatus,
    getDepartmentList
  } from '@/subject/pop/api/zt_api/role'

  export default {
    name: 'ZtRolesDepartment',
    components: {
      departmentTree
    },
    computed: {
    },
    data() {
      const checkSame = (rule, value, callback) => {
        if (value == this.form1.id) {
          callback(new Error('不可转移到自身'))
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
            path: ''
          },
          {
            title: '部门管理'
          }
        ],
        loading: false,
        query: {
          page: 1,
          limit: 10,
          status: 0,
          range: []
        },
        dataList: [],
        // 新增弹框
        showDialog: false,
        showDialog1: false,
        form: {
          parentId: null
        },
        rules: {
          name: [{required: true, message: '请输入部门名称', trigger: 'blur'}]
          // parentId: [{required: true, message: '请选择上级部门', trigger: 'change'}]
        },
        form1: {
          parentId: null
        },
        rules1: {
          parentId: [
            {required: true, message: '请选择需要转移的部门', trigger: 'change'},
            {required: true, validator: checkSame, trigger: 'change'}
          ]
        }
      }
    },
    activated() {
      this.getList()
    },
    methods: {
      async getList() {
        this.loading = true
        let query = this.query
        let data = await getDepartmentList(
          query.page,
          query.limit,
          query.name,
          query.status
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
      // 更新状态
      async updateRoles(row, type) {
        if (type === 2 && row.employeeNum > 0) {
          this.$common.alertWarn('部门下还有成员，不可停用，您需要先将部门成员转移至其他部门下才可停用')
          return
        }
        this.$common.showLoad()
        let data = await changeDepartStatus(row.id, type)
        this.$common.hideLoad()
        if (typeof data !== 'undefined') {
          this.$common.n_success(type === 2 ? '停用成功' : '启用成功')
          this.getList()
        }
      },
      // 新增部门
      addNewRole() {
        this.form = {}
        this.showDialog = true
      },
      // 编辑部门
      editRole(row) {
        if (!row.id) {
          this.$common.warn('部门ID为空')
          return
        }
        this.$router.push(`/zt_roles/roles_department_detail/${row.id}`)
      },
      // 批量转移部门
      addAll(row) {
        this.form1 = {
          id: row.id,
          name: row.name
        }
        this.showDialog1 = true
      },
      // 确认添加新部门
      confirmAddNew() {
        this.$refs.dataForm.validate(async valid => {
          if (valid) {
            let form = this.form
            this.$common.showLoad()
            let data = await addDepartment(form.name, form.parentId, form.code)
            this.$common.hideLoad()
            if (typeof data !== 'undefined') {
              this.showDialog = false
              this.$common.n_success('新增部门成功')
              this.getList()
            }
          } else {
            return false
          }
        })
      },
      // 确认批量转移
      confirmAddAll() {
        this.$refs.dataForm1.validate(async valid => {
          if (valid) {
            let form = this.form1
            this.$common.showLoad()
            let data = await transferDepart(form.id, form.parentId)
            this.$common.hideLoad()
            if (typeof data !== 'undefined') {
              this.showDialog1 = false
              this.$common.n_success('批量转移成功')
              this.getList()
            }
          } else {
            return false
          }
        })
      },
      // 表单部门点击
      handleNodeClick(data) {
        this.form = {
          parentName: data.name,
          parentId: data.id
        }
        this.$refs.select1.blur()
      },
      // 批量表单部门点击
      handleAllNodeClick(data) {
        let form = this.$common.clone(this.form1)
        form.parentName = data.name
        form.parentId = data.id
        this.form1 = form
        this.$refs.select.blur()
      }
    }
  }
</script>

<style lang="scss" scoped>
  @import "./index.scss";
</style>
