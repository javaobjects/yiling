<template>
  <div class="app-container">
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="12">
              <div class="title">职位名称</div>
              <el-input v-model="query.name" placeholder="请输入要查询的角色名称" @keyup.enter.native="handleSearch" />
            </el-col>
            <el-col :span="12">
              <div class="title">职位状态</div>
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
          <ylButton v-role-btn="['1']" type="primary" plain @click="addNewRole">新增职位</ylButton>
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
          <el-table-column label="ID" min-width="70" align="center" prop="id">
          </el-table-column>
          <el-table-column label="职位名称" min-width="139" align="center" prop="name">
            <template slot-scope="{ row }">
              <span class="role-desc">{{ row.name || '- -' }}</span>
            </template>
          </el-table-column>
          <el-table-column label="职位描述" min-width="139" align="center" prop="description">
            <template slot-scope="{ row }">
              <span class="role-desc">{{ row.description || '- -' }}</span>
            </template>
          </el-table-column>
          <el-table-column label="关联人数" min-width="100" align="center" prop="peopleNumber">
          </el-table-column>
          <el-table-column label="状态" min-width="70" align="center" prop="userCount">
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
              <yl-button v-role-btn="['2']" type="text" @click="editRole(row)">编辑</yl-button>
              <yl-button v-role-btn="['3']" v-if="row.status == 1" type="text" @click="updateRoles(row, 2)">停用</yl-button>
              <yl-button v-role-btn="['3']" v-else type="text" @click="updateRoles(row, 1)">启用</yl-button>
            </template>
          </el-table-column>
        </yl-table>
      </div>
      <yl-dialog
        :title="isEdit ? '编辑职位' : '新增职位'"
        width="580px"
        @confirm="confirmAddNew"
        :visible.sync="showDialog">
        <div class="new-role">
          <el-form
            ref="dataForm"
            :rules="rules"
            :model="form"
            label-width="130px"
            label-position="right">
            <el-form-item label="职位名称" prop="name">
              <el-input maxlength="6" placeholder="请输入职位名称（最多6个字）" v-model="form.name"></el-input>
            </el-form-item>
            <el-form-item label="职位描述">
              <el-input
                v-model="form.description"
                type="textarea"
                placeholder="请输入职位描述"
                maxlength="10"
                show-word-limit
                :autosize="{ minRows: 5, maxRows: 5}" />
            </el-form-item>
          </el-form>
        </div>
      </yl-dialog>
    </div>
  </div>
</template>

<script>
  import {
    getJobList,
    editJob,
    changeJobStatus
  } from '@/subject/pop/api/zt_api/role'
  export default {
    name: 'ZtRolesJob',
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
            title: '职位管理'
          }
        ],
        loading: false,
        query: {
          page: 1,
          limit: 10,
          status: 0
        },
        dataList: [],
        // 新增弹框
        showDialog: false,
        form: {},
        rules: {
          name: [{ required: true, message: '请输入职位名称', trigger: 'blur' }]
        },
        // 编辑状态
        isEdit: false
      }
    },
    activated() {
      this.getList()
    },
    methods: {
      async getList() {
        this.loading = true
        let query = this.query
        let data = await getJobList(
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
        if (type === 2 && row.peopleNumber > 0) {
          this.$common.alertWarn('职位下还有成员关联，不可停用')
          return
        }
        this.$common.showLoad()
        let data = await changeJobStatus(row.id, type)
        this.$common.hideLoad()
        if (typeof data !== 'undefined') {
          this.$common.n_success(type === 2 ? '停用成功' : '启用成功')
          this.getList()
        }
      },
      // 新增职位
      addNewRole() {
        this.isEdit = false
        this.form = {}
        this.showDialog = true
      },
      // 编辑职位
      editRole(row) {
        this.isEdit = true
        this.form = {
          id: row.id,
          name: row.name,
          description: row.description
        }
        this.showDialog = true
      },
      // 确认添加新角色
      confirmAddNew() {
        this.$refs.dataForm.validate(async valid => {
          if (valid) {
            let form = this.form
            let data = null
            this.$common.showLoad()
            if (this.isEdit) {
              data = await editJob(form.id, form.name, form.description)
            } else {
              data = await editJob(null, form.name, form.description)
            }
            this.$common.hideLoad()
            if (typeof data !== 'undefined') {
              this.showDialog = false
              if (this.isEdit) {
                this.$common.n_success('修改职位成功')
              } else {
                this.$common.n_success('添加职位成功')
              }
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
