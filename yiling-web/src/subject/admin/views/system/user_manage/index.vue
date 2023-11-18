<template>
  <div class="app-container">
    <div class="app-container-content">
      <div class="search-bar">
        <el-row>
          <el-col :span="8">
            <div class="title">用户名</div>
            <el-input v-model="query.username" placeholder="请输入用户名" @keyup.enter.native="handleSearch"/>
          </el-col>
          <el-col :span="8">
            <div class="title">创建时间</div>
            <el-date-picker
              v-model="query.time"
              type="daterange"
              format="yyyy 年 MM 月 dd 日"
              value-format="yyyy-MM-dd"
              range-separator="至"
              start-placeholder="开始时间"
              end-placeholder="结束时间">
            </el-date-picker>
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
          <yl-search-btn :total="query.total" @search="handleSearch" @reset="handleReset" />
        </div>
      </div>
      <div class="mar-tb-16">
        <yl-button type="primary" @click="addBtnClick">新增员工</yl-button>
      </div>
      <!-- 底部列表 -->
      <div class="mar-t-16">
        <yl-table
          border
          :show-header="true"
          :list="dataList"
          :total="query.total"
          :page.sync="query.page"
          :limit.sync="query.limit"
          :loading="loading"
          @getList="getList"
          >
          <el-table-column align="center" label="用户名" prop="username">
          </el-table-column>
          <el-table-column align="center" label="姓名" prop="name">
          </el-table-column>
          <el-table-column align="center" label="昵称" prop="nickname">
          </el-table-column>
          <el-table-column align="center" label="性别">
            <template slot-scope="{ row }">
              <div>{{ row.gender == 1 ? '男' : '女' }}</div>
            </template>
          </el-table-column>
          <el-table-column align="center" label="邮箱" prop="email">
          </el-table-column>
          <el-table-column align="center" width="120" label="状态">
            <template slot-scope="{ row, $index }">
              <el-switch @change="statusChange(row, $index)" :value="row.status == 1">
              </el-switch>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="150" label="角色">
            <template slot-scope="{ row }">
              <div>
                <el-tag style="margin-right:10px;margin-bottom:5px;" v-for="(item, index) in row.roleList" :key="index" type="info">{{ item.name }}</el-tag>
              </div>
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
          <el-table-column fixed="right" align="center" label="操作" width="120">
            <template slot-scope="{ row }">
              <div><yl-button type="text" @click="edit(row)">修改</yl-button></div>
              <div>
                <el-popconfirm class="popconfirm" @confirm="resetPasswordClick(row)" title="确定重置密码吗，此操作不能撤销">
                  <yl-button slot="reference" type="text">重置密码</yl-button>
                </el-popconfirm>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
    <!-- 新增员工 -->
    <yl-dialog
      :title="isEdit ? '修改' : '新增员工'"
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
              <el-form-item label="用户名" prop="username">
                <el-input v-model="form.username" placeholder="请输入用户名" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="手机号" prop="mobile">
                <el-input v-model="form.mobile" placeholder="请输入手机号" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="姓名" prop="name">
                <el-input v-model="form.name" placeholder="请输入姓名" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="昵称">
                <el-input v-model="form.nickname" placeholder="请输入昵称" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="生日">
                <el-date-picker v-model="form.birthday" type="date" value-format="yyyy-MM-dd" placeholder="选择日期">
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="邮箱">
                <el-input v-model="form.email" placeholder="请输入邮箱" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="角色" prop="roleIdList">
                <el-select v-model="form.roleIdList" multiple placeholder="请选择">
                  <el-option v-for="item in roleList" :key="item.id" :label="item.name" :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
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
            <el-col :span="12">
              <el-form-item label="性别" prop="gender">
                <el-radio-group v-model="form.gender">
                  <el-radio :label="1">男</el-radio>
                  <el-radio :label="2">女</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="!isEdit">
              <el-form-item label="密码" prop="password">
                <el-input v-model="form.password" placeholder="请输入密码" />
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
    </yl-dialog>
  </div>
</template>

<script>
  import { validateUserName, validatePassWord, validateTel } from '@/subject/admin/utils/validate'
  import { formatDate } from '@/subject/admin/utils'
  import {
    getSystemAdminList,
    getAllRoleList,
    saveSystemAdmin,
    systemupdateStatus,
    getSystemAdminDetail,
    resetPassword
  } from '@/subject/admin/api/role'
export default {
  name: 'UserManage',
  components: {
  },
  computed: {
  },
  data() {
    const validateMobile = (rule, value, callback) => {
      if (!validateTel(value)) {
        callback(new Error('请输入正确的手机号码'))
      } else {
        callback()
      }
    }
    const validateName= (rule, value, callback) => {
      if (!validateUserName(value)) {
        callback(new Error('字母、数字、下划线，4到16位'))
      } else {
        callback()
      }
    }
    const validatePass= (rule, value, callback) => {
      if (!validatePassWord(value)) {
        callback(new Error('包含字母、数字，4到16位'))
      } else {
        callback()
      }
    }
    return {
      isEdit: false,
      loading: false,
      query: {
        page: 1,
        limit: 10,
        total: 0,
        status: 0,
        time: []
      },
      dataList: [],
      showDialog: false,
      roleList: [],
      form: {
        username: '',
        name: '',
        nickname: '',
        birthday: '',
        email: '',
        gender: 1,
        status: 1,
        roleIdList: [],
        password: '',
        mobile: ''
      },
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { validator: validateName, trigger: 'blur' }],
        mobile: [{ required: true, validator: validateMobile, trigger: 'blur' }],
        name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
        gender: [{ required: true, message: '请选择性别', trigger: 'change' }],
        status: [{ required: true, message: '请选择状态', trigger: 'change' }],
        roleIdList: [{ required: true, message: '请选择角色', trigger: 'change' }],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { validator: validatePass, trigger: 'blur' }
        ]
      }
    }
  },
  activated() {
    this.getAllRoleListMethod()
    this.getList()
  },
  methods: {
    async getAllRoleListMethod() {
      let data = await getAllRoleList()
      if (data) {
        this.roleList = data
      }
    },
    async getList() {
      this.loading = true
      let query = this.query
      let data = await getSystemAdminList(
        query.page,
        query.limit,
        query.username,
        query.status,
        query.time && query.time.length > 0 ? query.time[0] : null,
        query.time && query.time.length > 1 ? query.time[1] : null
      )
      this.loading = false
      if (data) {
        data.records.forEach(item => {
          let roleList = []
          item.roleIdList.forEach((roleItem) => {
            let hasIndex = this.roleList.findIndex(obj => {
              return obj.id == roleItem;
            });
            if (hasIndex != -1) {
              roleList.push(this.roleList[hasIndex]);
            }
          })
          item.roleList = roleList
        });
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
        status: 0,
        time: []
      }
    },
    // 修改状态
    async statusChange(row, $index) {
      this.$common.showLoad()
      let status = row.status == 1 ? 2 : 1
      let data = await systemupdateStatus(row.id, status)
      this.$common.hideLoad()
      if (data && data.result) {
        this.$common.n_success('修改成功');
        this.getList()
      }
    },
    // 员工信息修改
    async edit(row) {
      this.form = {
        username: '',
        name: '',
        nickname: '',
        birthday: '',
        email: '',
        gender: 1,
        status: 1,
        roleIdList: [],
        password: '',
        mobile: ''
      }
      let data = await getAllRoleList()
      if (data) {
        this.roleList = data
      }
      this.$common.showLoad()
      let itemData = await getSystemAdminDetail(row.id)
      this.$common.hideLoad()
      if (itemData) {
          let time = formatDate(itemData.birthday, 'yyyy-MM-dd') == '- -' ? '' : formatDate(itemData.birthday, 'yyyy-MM-dd')
          this.form = {
            username: itemData.username,
            name: itemData.name,
            nickname: itemData.nickname,
            birthday: time,
            email: itemData.email,
            gender: itemData.gender,
            status: itemData.status,
            roleIdList: itemData.roleIdList,
            id: itemData.id,
            mobile: itemData.mobile
          }
      }
      this.isEdit = true
      this.showDialog = true
    },
    // 新增员工
    async addBtnClick() {
      this.isEdit = false
      this.form = {
        username: '',
        name: '',
        nickname: '',
        birthday: '',
        email: '',
        gender: 1,
        status: 1,
        roleIdList: [],
        password: '',
        mobile: ''
      }
      this.showDialog = true
      let data = await getAllRoleList()
      if (data) {
        this.roleList = data
      }
    },
    // 确认点击
    confirm() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          this.$common.showLoad()
          let form = this.form
          let data = await saveSystemAdmin(
            form.username,
            form.name,
            form.nickname,
            form.birthday,
            form.email,
            form.gender,
            form.status,
            form.roleIdList,
            form.password,
            form.id,
            form.mobile
          )
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
    async resetPasswordClick(row) {
      this.$common.showLoad()
      let data = await resetPassword( row.mobile )
      this.$common.hideLoad()
      if (data && data.result) {
        this.$common.n_success('保存成功');
      }
    }
  }
}
</script>

<style lang="scss" scoped>
  @import "./index.scss";
</style>
