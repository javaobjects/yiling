<template>
  <div class="app-container">
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="12">
              <div class="title">角色名称</div>
              <el-input v-model="query.name" placeholder="请输入要查询的角色名称" @keyup.enter.native="handleSearch" />
            </el-col>
            <el-col :span="12">
              <div class="title">角色状态</div>
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
          <ylButton v-role-btn="['1']" type="primary" plain @click="addNewRole">新增角色</ylButton>
        </div>
      </div>
      <!-- 底部列表 -->
      <div class="table-view">
        <yl-table
        border
        :show-header="true"
        :list="dataList"
        :total="query.total"
        :page.sync="query.page"
        :limit.sync="query.limit"
        :loading="loading"
        @getList="getList">
        <el-table-column label="ID" min-width="60" align="center" prop="id">
        </el-table-column>
        <el-table-column label="角色名称" min-width="139" align="center" prop="name">
        </el-table-column>
        <el-table-column label="角色描述" min-width="139" align="center">
          <template slot-scope="{ row }">
            <span class="role-desc">{{ row.remark }}</span>
          </template>
        </el-table-column>
        <el-table-column label="关联人数" min-width="90" align="center" prop="userCount">
        </el-table-column>
        <el-table-column label="启用状态" min-width="90" align="center">
          <template slot-scope="{ row }">
            <span :class="[row.status == 1 ? 'col-down' : 'col-up']">{{ row.status == 1 ? '启用' : '停用' }}</span>
          </template>
        </el-table-column>
          <el-table-column label="角色类型" min-width="100" align="center">
            <template slot-scope="{ row }">
              <span class="role-desc">{{ row.type === 1 ? '系统角色' : '自定义角色' }}</span>
            </template>
          </el-table-column>
        <el-table-column label="创建信息" min-width="110" align="center">
          <template slot-scope="{ row }">
            <div>{{ row.createUserName }}</div>
            <span>{{ row.createTime | formatDate() }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" min-width="139" fixed="right" align="center">
          <template slot-scope="{ row }">
            <yl-button v-role-btn="['2']" type="text" @click="editRole(row)">修改</yl-button>
            <yl-button v-role-btn="['3']" v-if="row.status == 1" type="text" @click="updateRoles(row, 2)">停用</yl-button>
            <yl-button v-role-btn="['3']" v-else type="text" @click="updateRoles(row, 1)">启用</yl-button>
            <yl-button v-role-btn="['4']" type="text" @click="addAll(row)">批量转移</yl-button>
          </template>
        </el-table-column>
      </yl-table>
      </div>
      <yl-dialog
        title="批量转移角色"
        @confirm="confirmAddAll"
        width="500px"
        :visible.sync="showDialog1">
        <div class="pad-16 role-all flex-row-center">
          <el-form
            class="form-view"
            ref="dataForm1"
            :rules="rules1"
            :model="form1"
            label-position="left">
            <div class="title">
              批量将{{ form1.roleName }}角色下人员转移至
            </div>
            <el-form-item prop="roleId">
              <el-select v-model="form1.roleId">
                <el-option
                  v-for="(item, idx) in roles"
                  :disabled="item.id == form1.id"
                  :key="idx"
                  :label="item.name"
                  :value="item.id">
                </el-option>
              </el-select>
              <yl-tool-tip class="tooltip">
                执行操作后，人员将马上拥有新角色的权限
              </yl-tool-tip>
            </el-form-item>
          </el-form>
        </div>
      </yl-dialog>
    </div>
  </div>
</template>

<script>
  import { getCompanyRoleList, transferRole, updateRoleStatus } from '@/subject/pop/api/zt_api/role'

  export default {
    name: 'ZtRolesIndex',
    components: {
    },
    computed: {
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
            path: ''
          },
          {
            title: '角色管理'
          }
        ],
        loading: false,
        query: {
          page: 1,
          limit: 10,
          total: 0,
          status: 0
        },
        dataList: [],
        // 选择全部角色
        roles: [],
        // 新增弹框
        showDialog1: false,
        form1: {},
        rules1: {
          roleId: [{ required: true, message: '请选择需要转移的角色', trigger: 'change' }]
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
        let data = await getCompanyRoleList(
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
      // 更新角色状态
      async updateRoles(row, type) {
        if (type === 2 && row.userCount > 0) {
          this.$common.alertWarn('角色下还有成员，不可停用，您需要先将成员转移至其他角色下才可停用')
          return
        }
        this.$common.showLoad()
        let data = await updateRoleStatus(row.id, type)
        this.$common.hideLoad()
        if (typeof data !== 'undefined') {
          this.$common.n_success(type === 2 ? '停用成功' : '启用成功')
          this.getList()
        }
      },
      // 新增角色
      addNewRole() {
        this.$router.push('/zt_roles/roles_detail/')
      },
      // 编辑角色
      editRole(row) {
        if (!row.id) {
          this.$common.warn('角色ID为空')
          return
        }
        this.$router.push(`/zt_roles/roles_detail/${row.id}`)
      },
      // 批量转移
      addAll(row) {
        this.getAllRole(() => {
          this.form1 = {}
          this.form1.id = row.id
          this.form1.roleName = row.name
          this.showDialog1 = true
        })
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
      // 确认批量转移
      confirmAddAll() {
        this.$refs.dataForm1.validate(async valid => {
          if (valid) {
            let form = this.form1
            this.$common.showLoad()
            let data = await transferRole(form.id, form.roleId)
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
      }
    }
  }
</script>

<style lang="scss" scoped>
  @import "./index.scss";
</style>
