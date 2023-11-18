<template>
  <div class="app-container">
    <div class="app-container-content">
      <div class="search-bar">
        <el-row>
          <el-col :span="8">
            <div class="title">角色名称</div>
            <el-input v-model="query.name" placeholder="请输入角色名称" @keyup.enter.native="handleSearch"/>
          </el-col>
          <el-col :span="8">
            <div class="title">状态</div>
            <el-radio-group v-model="query.status">
              <el-radio :label="0">全部</el-radio>
              <el-radio :label="1">启用</el-radio>
              <el-radio :label="2">停用</el-radio>
            </el-radio-group>
          </el-col>
        </el-row>
        <div class="mar-tb-16">
          <yl-search-btn
            :total="query.total"
            @search="handleSearch"
            @reset="handleReset"
          />
        </div>
      </div>
      <div class="mar-tb-16">
        <yl-button type="primary" @click="addBtnClick">新增角色</yl-button>
      </div>
      <!-- 底部列表 -->
      <div class="mar-t-16">
        <el-tabs class="bg-white" v-model="appId" type="card" @tab-click="handleTabClick">
          <el-tab-pane v-for="item in menuApps" :key="item.appId" :label="item.name" :name="item.appId + ''"></el-tab-pane>
          <!-- <el-tab-pane label="运营后台" name="1"></el-tab-pane>
          <el-tab-pane label="POP后台" name="2"></el-tab-pane>
          <el-tab-pane label="B2B后台" name="3"></el-tab-pane>
          <el-tab-pane label="互联网医院后台" name="4"></el-tab-pane>
          <el-tab-pane label="数据中台" name="5"></el-tab-pane>
          <el-tab-pane label="销售助手" name="6"></el-tab-pane>
          <el-tab-pane label="健康管理中心" name="9"></el-tab-pane> -->
        </el-tabs>
        <yl-table
          border
          :show-header="true"
          :list="dataList"
          :total="query.total"
          :page.sync="query.page"
          :limit.sync="query.limit"
          :loading="loading"
          @getList="getList">
          <el-table-column align="center" label="角色名称" prop="name">
          </el-table-column>
          <el-table-column align="center" label="角色编码" prop="code">
          </el-table-column>
          <el-table-column align="center" label="备注" prop="remark">
          </el-table-column>
          <el-table-column align="center" label="状态">
            <template slot-scope="{ row }">
              <el-tag :type="row.status == 1 ? 'success' : 'danger'">{{ row.status | enable }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column align="center" label="角色类型">
            <template slot-scope="{ row }">
              <div>{{ row.type == 1 ? '系统角色' : '自定义角色' }}</div>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="80" label="创建时间">
            <template slot-scope="{ row }">
              <div>{{ row.createTime | formatDate }}</div>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="80" label="修改时间">
            <template slot-scope="{ row }">
              <div>{{ row.updateTime | formatDate }}</div>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="操作"
            width="120">
            <template slot-scope="{ row }">
              <div><yl-button type="text" @click="edit(row)">修改</yl-button></div>
              <div><yl-button type="text" @click="settingClick(row)">权限设置</yl-button></div>
              <div v-show="row.type == 2"><yl-button type="text" @click="deleteClick(row)">删除</yl-button></div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
    <!-- 新增员工 -->
    <yl-dialog
      :title="isEdit ? '修改' : '新增角色'"
      @confirm="confirm"
      :visible.sync="showDialog">
      <div class="dialog-content">
        <el-form
          ref="dataForm"
          class="recome-view-form"
          :rules="rules"
          :model="form"
          label-width="80px"
          label-position="right"
        >
          <el-row>
            <el-col :span="12">
              <el-form-item label="角色名称" prop="name">
                <el-input v-model="form.name" placeholder="请输入角色名称" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12" v-show="isEdit && currentRole.type == 1">
              <el-form-item label="角色编码" prop="code">
                <el-input v-model="form.code" placeholder="请输入角色编码" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="状态" prop="status">
                <el-radio-group v-model="form.status">
                  <el-radio :label="1">启用</el-radio>
                  <el-radio :label="2">停用</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="备注" prop="remark">
                <el-input type="textarea" v-model="form.remark" placeholder="请输入备注" />
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
    </yl-dialog>
    <!-- 权限设置 -->
    <yl-dialog
      title="权限设置"
      @confirm="settingConfirm"
      :visible.sync="showSettingDialog">
      <div class="dialog-content">
        <el-tree
          class="my-tree"
          :data="rolesArray"
          show-checkbox
          :expand-on-click-node="false"
          check-on-click-node
          default-expand-all
          node-key="id"
          ref="tree"
          :default-checked-keys="checkRoles"
          highlight-current
          :props="{ children: 'children',label: 'menuName' }">
        </el-tree>
      </div>
    </yl-dialog>
  </div>
</template>

<script>
  import {
    listMenuApps,
    getSystemRoleList,
    createRole,
    deleteRole,
    updateRole,
    getRoleMenu,
    updateRoleMenu
  } from '@/subject/admin/api/role'
  import permission from '@/subject/admin/directive/role'

export default {
  name: 'RoleManage',
  components: {
  },
  directives: { permission },
  data() {
    return {
      isEdit: false,
      loading: false,
      appId: '1',
      query: {
        page: 1,
        limit: 10,
        name: '',
        status: 0
      },
      dataList: [],
      showDialog: false,
      form: {
        // 角色名称
        name: '',
        // 角色描述
        remark: '',
        // 状态：1-启用 2-停用
        status: 1,
        // 角色类型：1-系统角色 2-自定义角色
        type: 2
      },
      rules: {
        name: [{ required: true, message: '请输入角色名称', trigger: 'blur' }],
        status: [{ required: true, message: '请选择状态', trigger: 'change' }]
      },
      currentRole: {},
      showSettingDialog: false,
      // 菜单
      rolesArray: [],
      // 已选权限 用于修改
      checkRoles: [],
      menuApps: []
    }
  },
  activated() {
    this.getListMenuApps()
    // this.getList()
  },
  methods: {
    async getListMenuApps() {
      this.$common.showLoad()
      let data = await listMenuApps()
      this.$common.hideLoad()
      if (data && data.list) {
        this.menuApps = data.list
        if (data.list.length > 0) {
          this.appId = data.list[0].appId + ''
          this.getList()
        }
      }
    },
    async getList() {
      this.loading = true
      let query = this.query
      let data = await getSystemRoleList(
        query.page,
        query.limit,
        Number(this.appId),
        query.name,
        query.status
      )
      this.loading = false
      if (data) {
        this.dataList = data.records
        this.query.total = data.total
      }
    },
    handleTabClick() {
      this.query.page = 1
      this.getList()
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
        status: 0,
        name: ''
      }
    },
    // 信息修改
    async edit(row) {
      this.currentRole = row
      this.form = {
        name: row.name,
        status: row.status,
        type: Number(row.type),
        remark: row.remark,
        code: row.code,
        id: row.id
      }
      this.isEdit = true
      this.showDialog = true
    },
    // 新增员工
    async addBtnClick() {
      this.form = {
        name: '',
        remark: '',
        status: 1,
        type: 2
      }
      this.isEdit = false
      this.showDialog = true
    },
    // 确认点击
    confirm() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          this.$common.showLoad()
          let form = this.form
          let data = null
          if (this.isEdit) {
            data = data = await updateRole(
              form.id,
              form.name,
              form.status,
              form.type,
              form.remark,
              form.code
            )
          } else {
            data = data = await createRole(
              form.name,
              form.status,
              form.type,
              form.remark
            )
          }
          this.$common.hideLoad()
          if (data && data.result) {
            this.$common.n_success('保存成功');
            this.showDialog = false
            this.getList()
          }
        } else {
          return false
        }
      })
    },
    // 重置密码
    async deleteClick(row) {
      this.$common.showLoad()
      let data = await deleteRole( [row.id] )
      this.$common.hideLoad()
      if (data && data.result) {
        this.$common.n_success('删除成功');
        this.getList()
      }
    },
    // 权限设置
    settingClick(row) {
      this.currentRole = row
      this.checkRoles = []
      this.getAllList(row, () => {
        this.showSettingDialog = true
      })
    },
    // 权限设置确认
    async settingConfirm() {
      let roles = this.$refs.tree.getCheckedKeys()
      let nodes = this.$refs.tree.getHalfCheckedKeys()
      this.$common.showLoad()
      let data = await updateRoleMenu(this.currentRole.id, roles.concat(nodes))
      this.$common.hideLoad()
      if (data && data.result) {
        this.showSettingDialog = false
        this.$common.n_success('权限设置成功')
      }
    },
    // 获取菜单
    async getAllList(row, callback) {
      this.$common.showLoad()
      let data = await getRoleMenu(row.id)
      this.$common.hideLoad()
      if (data) {
        this.rolesArray = data || []
        let check = []
        this.getSelectId(data, check)
        this.$common.log('check:',check)
        this.checkRoles = check
        if (callback) callback()
      }
    },
    // 递归获取选中的
    getSelectId(arr, selectArr) {
      arr.forEach(item => {
        if (item.children && item.children.length > 0){
          this.getSelectId(item.children, selectArr)
        } else {
          if (item.selected) {
            selectArr.push(item.id)
          }
        }
      });
    }
  }
}
</script>

<style lang="scss" scoped>
  @import "./index.scss";
</style>
<style lang="scss">
  .recome-view-form{
    padding-right: 20px;
    .el-input{
      width: 100% !important;
    }
    .el-select {
      width: 100% !important;
      max-width: 100% !important;
    }
  }
</style>
