<template>
  <div class="app-container">
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">员工姓名</div>
              <el-input v-model="query.name" placeholder="请输入要查询的员工姓名" @keyup.enter.native="handleSearch" />
            </el-col>
            <el-col :span="6">
              <div class="title">员工手机号</div>
              <el-input v-model="query.mobile" placeholder="请输入员工手机号" @keyup.enter.native="handleSearch" />
            </el-col>
            <el-col :span="6">
              <div class="title">员工工号</div>
              <el-input v-model="query.code" placeholder="请输入员工工号" @keyup.enter.native="handleSearch" />
            </el-col>
            <el-col :span="6">
              <div class="title">部门</div>
              <el-select
                v-model="query.department"
                ref="select1"
                placeholder="请选择部门">
                <el-option :value="query.department" :label="query.parentName" style="height: auto; padding: 0;">
                  <department-tree
                    node-key="id"
                    :current-node-key="query.department"
                    :props="{ children: 'children', label: 'name' }"
                    @node-click="handleNodeQueryClick">
                  </department-tree>
                </el-option>
              </el-select>
            </el-col>
          </el-row>
        </div>
        <div class="search-box pad-t-16">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">用户类型</div>
              <el-select v-model="query.type" placeholder="请选择用户类型">
                <el-option label="全部" :value="0"></el-option>
                <el-option v-for="item in employeeType" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <div class="title">账号ID</div>
               <el-input v-model="query.userId" placeholder="请输入账号ID" @keyup.enter.native="handleSearch" />
            </el-col>
<!--            <el-col :span="8">-->
<!--              <div class="title">职位</div>-->
<!--              <el-select-->
<!--                class="position"-->
<!--                v-model="query.position"-->
<!--                filterable-->
<!--                remote-->
<!--                reserve-keyword-->
<!--                placeholder="请输入需搜索的职位名称"-->
<!--                @focus="jobRemoteMethod()"-->
<!--                :remote-method="jobRemoteMethod"-->
<!--                :loading="jobLoading">-->
<!--                <div slot="prefix" class="el-icon-search"></div>-->
<!--                <el-option-->
<!--                  v-for="item in userPosition"-->
<!--                  :key="item.id"-->
<!--                  :label="item.name"-->
<!--                  :value="item.id">-->
<!--                  <span style="float: left">{{ item.name }}</span>-->
<!--                  <span style="float: right;"></span>-->
<!--                </el-option>-->
<!--              </el-select>-->
<!--            </el-col>-->
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
        <el-row type="flex"> 
          <el-col :span="12">
            <ylButton v-role-btn="['1']" type="primary" @click="addNew">新增员工</ylButton>
          </el-col>
          <el-col :span="12" style="text-align: right">
            <ylButton type="primary" v-role-btn="['4']" plain @click="downLoadTemp">导出搜索结果</ylButton>
          </el-col>
        </el-row>
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
          <el-table-column label="账号ID" min-width="90" align="center" prop="userId">
          </el-table-column>
          <el-table-column label="姓名" min-width="139" align="center" prop="name">
            <template slot-scope="{ row }">
              <span class="role-desc">{{ row.name || '- -' }}</span>
            </template>
          </el-table-column>
          <el-table-column label="手机号" min-width="139" align="center" prop="mobile">
            <template slot-scope="{ row }">
              <span class="role-desc">{{ row.mobile || '- -' }}</span>
            </template>
          </el-table-column>
          <el-table-column label="工号" min-width="139" align="center" prop="code">
            <template slot-scope="{ row }">
              <span class="role-desc">{{ row.code || '- -' }}</span>
            </template>
          </el-table-column>
          <el-table-column label="部门" min-width="139" align="center" prop="departmentName">
            <template slot-scope="{ row }">
              <span class="role-desc">{{ row.departmentName || '- -' }}</span>
            </template>
          </el-table-column>
          <el-table-column label="角色" min-width="139" align="center" prop="roleName">
            <template slot-scope="{ row }">
              <span class="role-desc">{{ row.roleName || '- -' }}</span>
            </template>
          </el-table-column>
          <el-table-column label="用户类型" min-width="139" align="center" prop="type">
            <template slot-scope="{ row }">
              <span class="role-desc">{{ row.type | dictLabel(employeeType) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="上级领导" min-width="139" align="center" prop="parentName">
            <template slot-scope="{ row }">
              <span class="role-desc">{{ row.parentName || '- -' }}</span>
            </template>
          </el-table-column>
          <el-table-column label="状态" min-width="70" align="center">
            <template slot-scope="{ row }">
              <span :class="[row.status == 1 ? 'col-down' : 'col-up']">{{ row.status == 1 ? '启用' : '停用' }}</span>
            </template>
          </el-table-column>
          <el-table-column label="创建信息" min-width="139" align="center">
            <template slot-scope="{ row }">
              <div>{{ row.createUserName }}</div>
              <span>{{ row.createTime | formatDate() }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" min-width="139" fixed="right" align="center">
            <template slot-scope="{ row }">
              <div v-show="!row.adminFlag">
                <yl-button v-role-btn="['2']" type="text" @click="editNew(row)">编辑</yl-button>
                <yl-button v-role-btn="['3']" v-if="row.status === 2" type="text" @click="changeStatus(row, 1)">启用</yl-button>
                <yl-button v-role-btn="['3']" v-else-if="row.status === 1" type="text" @click="changeStatus(row, 2)">停用</yl-button>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
    <yl-dialog
      :title="form.isEdit ? '员工详情' : '新增员工'"
      width="60%"
      @confirm="confirm"
      :visible.sync="showDialog">
      <div class="new-user-view">
        <el-form
          ref="dataForm"
          :rules="rules"
          :model="form"
          label-width="110px"
          label-position="top">
          <div class="header-bar">
            <div class="sign"></div>基本信息
          </div>
          <div class="mar-l-16 info">
            <el-row :gutter="12">
              <el-col :span="8">
                <el-form-item label="员工姓名" prop="name">
                  <div v-if="form.nameType == 1">
                    <el-input show-word-limit maxlength="10" placeholder="请输入员工姓名" v-model.trim="form.name"></el-input>
                  </div>
                  <div v-else>
                    <el-input show-word-limit maxlength="10" :disabled="!!form.hasInfo || form.isEdit" placeholder="请输入员工姓名" v-model="form.name"></el-input>
                  </div>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="手机号" prop="mobile">
                  <el-input :disabled="true" placeholder="请输入员工手机号" v-model="form.mobile"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="工号" :prop="currentEnterpriseInfo.yilingFlag ? 'code' : ''">
                  <el-input placeholder="请输入员工工号" maxlength="10" v-model="form.code"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </div>
          <div class="header-bar">
            <div class="sign"></div>岗位信息
          </div>
          <div class="mar-l-16 mar-t-10 role-info">
            <el-row :gutter="12">
              <el-col :span="8">
                <el-form-item label="角色" prop="roleId">
                  <el-select
                    placeholder="请选择角色"
                    @visible-change="visibleChange"
                    v-model="form.roleId">
                    <el-option
                      v-for="item in roles"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="上级领导">
                  <el-select
                    v-model="form.parentId"
                    filterable
                    remote
                    reserve-keyword
                    placeholder="请选择上级领导"
                    @focus="remoteMethod()"
                    :remote-method="remoteMethod"
                    :loading="parentLoading">
                    <el-option
                      v-for="item in parentList"
                      :key="item.id"
                      :disabled="item.id == form.userId"
                      :label="item.name"
                      :value="item.id">
                      <span style="float: left">{{ item.name }}</span>
                      <span style="float: right;">{{ item.mobile }}</span>
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="用户类型" prop="type">
                  <template slot="label">
                    <span>用户类型
                      <span v-if="form.isEdit && form.type === 2" class="el-icon-warning remark">解绑绑定的药品之后进行用户类型更换</span>
                    </span>
                  </template>
                  <el-select
                    v-model="form.type"
                    placeholder="请选择用户类型">
                    <el-option
                      v-for="item in employeeType"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="12">
              <el-col :span="8">
                <el-form-item label="部门" prop="department">
                  <el-select
                    v-model="form.department"
                    multiple
                    @remove-tag="removeTag"
                    placeholder="请选择部门">
                    <el-option value="" style="height: auto; padding: 0;">
                      <department-tree
                        node-key="id"
                        :props="{ children: 'children', label: 'name' }"
                        @node-click="handleNodeFormClick">
                      </department-tree>
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="职位">
                  <el-select
                    class="position"
                    v-model="form.positionId"
                    filterable
                    remote
                    reserve-keyword
                    placeholder="请输入需搜索的职位名称"
                    @focus="jobRemoteMethod()"
                    :remote-method="jobRemoteMethod"
                    :loading="jobLoading">
                    <div slot="prefix" class="el-icon-search"></div>
                    <el-option
                      v-for="item in userPosition"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id">
                      <span style="float: left">{{ item.name }}</span>
                      <span style="float: right;"></span>
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
          </div>
        </el-form>
      </div>
    </yl-dialog>
  </div>
</template>

<script>
  import departmentTree from '../component/DepartmentTree'
  import { validateTel } from '@/subject/pop/utils/validate'
  import {
    getCompanyUserList,
    getCompanyRoleList,
    createEmployee,
    editEmployee,
    getEmployeeInfo,
    changeEmployeeStatus,
    checkUserTel,
    getJobList
  } from '@/subject/pop/api/zt_api/role'
  import { employeeType } from '@/subject/pop/busi/zt/roles'
  import { mapGetters } from 'vuex'
  import { createDownLoad } from '@/subject/pop/api/common';
  export default {
    name: 'ZtRolesUser',
    components: {
      departmentTree
    },
    computed: {
      ...mapGetters([
        'currentEnterpriseInfo'
      ]),
      // 用户类型
      employeeType() {
        return employeeType()
      }
    },
    data() {
      const validateMobile = (rule, value, callback) => {
        if (!validateTel(value)) {
          callback(new Error('请输入正确的手机号码'))
        } else {
          callback()
        }
      }
      return {
        navList: [
          {
            title: '首页',
            path: '/zt_dashboard'
          },
          {
            title: '权限管理'
          },
          {
            title: '员工管理'
          }
        ],
        loading: false,
        query: {
          page: 1,
          limit: 10,
          total: 0,
          type: 0,
          userId: ''
        },
        dataList: [],
        showDialog: false,
        form: {
          department: [],
          departmentId: []
        },
        rules: {
          name: [{ required: true, message: '请输入员工名称', trigger: 'blur' }],
          mobile: [{ required: true, validator: validateMobile, trigger: 'blur' }],
          code: [{ required: true, message: '请输入员工工号', trigger: 'blur' }],
          roleId: [{ required: true, message: '请选择角色', trigger: 'change' }],
          department: [{ required: true, message: '请选择部门', trigger: 'change' }],
          type: [{ required: true, message: '请选择用户类型', trigger: 'change' }]
        },
        // 上级领导
        parentLoading: false,
        parentList: [],
        // 选择框使用 全部角色
        roles: [],
        // 职位
        userPosition: [],
        jobLoading: false
      }
    },
    activated() {
      this.getList()
    },
    methods: {
      async getList() {
        this.loading = true
        let query = this.query
        let data = await getCompanyUserList(
          query.page,
          query.limit,
          query.mobile,
          query.name,
          null,
          query.code,
          query.position,
          query.department,
          query.type,
          query.userId
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
          type: 0,
          userId: ''
        }
      },
      // 获取全部角色
      async getAllRole(callback) {
        this.$common.showLoad()
        let data = await getCompanyRoleList(1, 999, null, 1)
        this.$common.hideLoad()
        if (data) {
          this.roles = data.records
          if (callback) callback()
        }
      },
      // 远程搜索员工
      async remoteMethod(query) {
        this.parentLoading = true;
        let data = await getCompanyUserList(
          1,
          999,
          null,
          query || '',
          1
        )
        this.parentLoading = false
        if (data) {
          this.parentList = data.records
        }
      },
      // 远程搜索职位
      async jobRemoteMethod(query) {
        this.jobLoading = true;
        let data = await getJobList(
          1,
          999,
          query || '',
          1
        )
        this.jobLoading = false
        if (data) {
          this.userPosition = data.records
        }
      },
      // 添加新员工
      addNew() {
        this.$prompt('请填写员工手机号', '新增员工', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputPlaceholder: '请输入手机号码',
          inputValidator: (value) => {
            if (validateTel(value)) {
              return true
            } else {
              return '请输入正确的手机号码'
            }
          },
          inputErrorMessage: '请输入手机号码'
        }).then(async ({ value }) => {
          this.$common.showLoad()
          let data = await checkUserTel(value)
          this.$common.hideLoad()
          if (data) {
            if (data.name) {
              this.form = {
                name: data.name,
                mobile: value,
                isEdit: false,
                hasInfo: true,
                department: [],
                departmentId: []
              }
            } else {
              this.form = {
                mobile: value,
                isEdit: false,
                department: [],
                departmentId: []
              }
            }
            this.showDialog = true
            this.roles = []
            this.getAllRole()
          }
        })
      },
      // 编辑员工
      async editNew(row) {
        this.$common.showLoad()
        let data = await getEmployeeInfo(row.id)
        this.$common.hideLoad()
        if (data && data.userId) {
          data.department = []
          data.departmentId = []
          data.departmentList.map(item => {
            data.department.push(item.name)
            data.departmentId.push(item)
          })
          this.form = Object.assign(data, {
            isEdit: true,
            hasInfo: true,
            roleIdCopy: data.roleId,
            roleId: data.roleName,
            parentIdCopy: data.parentId,
            parentId: data.parentName,
            positionIdCopy: data.positionId,
            positionId: data.positionName,
            nameType: data.name == '' ? 1 : 2
          })
          this.showDialog = true
          this.roles = []
          this.getAllRole()
        }
      },
      // 启用 停用
      changeStatus(row, status) {
        if (status === 2) {
          this.$common.confirm('停用时请注意该员工是否存在未完结订单以及其他流程，停用后员工将不能登录，是否确定停用？', r => {
            if (r) {
              this.changeRowStatus(row, status)
            }
          }, null, null, { type: 'warning' })
        } else {
          this.changeRowStatus(row, status)
        }
      },
      // 启用 停用
      async changeRowStatus(row, status) {
        this.$common.showLoad()
        let data = await changeEmployeeStatus(row.id, status)
        this.$common.hideLoad()
        if (typeof data !== 'undefined') {
          this.$common.n_success(status === 1 ? '启用成功' : '停用成功')
          this.getList()
        }
      },
      // 下拉框出现选择角色列表
      visibleChange(e) {
        // 下拉时
        if (e) {
          if (this.roles.length === 0) {
            this.getAllRole()
          }
        }
      },
      // 弹框确认事件
      confirm() {
        this.$refs.dataForm.validate(async valid => {
          if (valid) {
            let form = this.form
            let data = null
            this.$common.showLoad()
            const departmentId = form.departmentId.map(item => {
              return item.id
            })
            if (form.isEdit) {
              const roleId = typeof form.roleId !== 'number' ? form.roleIdCopy : form.roleId
              const parentId = typeof form.parentId !== 'number' ? form.parentIdCopy : form.parentId
              const positionId = typeof form.positionId !== 'number' ? form.positionIdCopy : form.positionId
              data = await editEmployee(
                form.userId, 
                parentId, 
                roleId, 
                positionId, 
                form.code, 
                departmentId, 
                form.type,
                form.name
              )
            } else {
              data = await createEmployee(
                form.mobile, 
                form.name, 
                form.parentId, 
                form.roleId, 
                form.positionId, 
                form.code, 
                departmentId, 
                form.type
              )
            }
            this.$common.hideLoad()
            if (typeof data !== 'undefined') {
              this.showDialog = false
              if (form.isEdit) {
                this.$common.n_success('修改成功')
              } else {
                this.$common.n_success('添加成功')
              }
              this.getList()
            }
          } else {
            return false
          }
        })
      },
      // 表单部门点击
      handleNodeFormClick(data) {
        let form = this.$common.clone(this.form)
        if (form.departmentId.findIndex(item => item.id == data.id) === -1) {
          form.department.push(data.name)
          form.departmentId.push(data)
        }
        this.form = form
      },
      // select框移除
      removeTag(tag) {
        const index = this.form.departmentId.findIndex(item => item.name === tag)
        if (index > -1) {
          this.form.departmentId.splice(index, 1)
        }
      },
      // 搜索部门点击
      handleNodeQueryClick(data) {
        let form = this.$common.clone(this.query)
        form.parentName = data.name
        form.department = data.id
        this.query = form
        this.$refs.select1.blur()
      },
      //导出
      async downLoadTemp() {
        let query = this.query;
        this.$common.showLoad();
        let data = await createDownLoad({
          className: 'enterpriseEmployeeExportService',
          fileName: '员工管理',
          groupName: '员工管理列表',
          menuName: '员工权限管理-员工管理',
          searchConditionList: [
            {
              desc: '员工姓名',
              name: 'name',
              value: query.name || ''
            },
            {
              desc: '员工手机号',
              name: 'mobile',
              value: query.mobile || ''
            },
            {
              desc: '员工工号',
              name: 'code',
              value: query.code || ''
            },
            {
              desc: '部门',
              name: 'department',
              value: query.department || ''
            },
            {
              desc: '用户类型',
              name: 'type',
              value: query.type || ''
            },
            {
              desc: '账号ID',
              name: 'userId	',
              value: query.userId || ''
            }
          ]
        });
        this.$common.hideLoad();
        if (data && data.result) {
          this.$common.n_success('创建下载任务成功，请在下载中心查看');
        }
      }
    }
  }
</script>

<style lang="scss" scoped>
  @import "./index.scss";
</style>
