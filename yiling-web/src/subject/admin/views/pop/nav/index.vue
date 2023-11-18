<template>
  <div class="app-container">
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">导航名称</div>
              <el-input v-model="query.name" @keyup.enter.native="searchEnter" placeholder="导航名称" />
            </el-col>
            <el-col :span="8">
              <div class="title">创建时间</div>
              <el-date-picker v-model="query.time" type="daterange" format="yyyy 年 MM 月 dd 日" value-format="yyyy-MM-dd" range-separator="至" start-placeholder="开始月份" end-placeholder="结束月份"></el-date-picker>
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
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn :total="query.total" @search="handleSearch" @reset="handleReset" />
            </el-col>
          </el-row>
        </div>
      </div>

      <div class="mar-tb-16">
        <yl-button type="primary" @click="addNew">新增导航</yl-button>
      </div>

      <div>
        <yl-table border :show-header="true" :list="dataList" :total="query.total" :page.sync="query.page" :limit.sync="query.limit" :loading="loading" @getList="getList">
          <el-table-column align="center" label="状态">
            <template slot-scope="{ row }">
              <el-tag :type="row.state === 1 ? 'success' : 'danger'">{{ row.state | enable }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column align="center" label="排序" prop="sort">
          </el-table-column>
          <el-table-column align="center" min-width="120" label="名称" prop="name">
          </el-table-column>
          <el-table-column align="center" min-width="260" label="链接" prop="link">
          </el-table-column>
          <el-table-column align="center" min-width="120" label="创建时间">
            <template slot-scope="{ row }">
              <span>{{ row.createTime | formatDate }}</span>
            </template>
          </el-table-column>
          <el-table-column fixed="right" align="center" label="操作" width="120">
            <template slot-scope="{ row }">
              <div>
                <yl-button type="text" @click="edit(row)">修改</yl-button>
              </div>
              <div>
                <yl-status url="/pop/api/v1/navigation/info/update" :status="row.state" status-key="state" :data="{id: row.id}" @change="getList">
                </yl-status>
              </div>
            </template>
          </el-table-column>
        </yl-table>
        <yl-dialog :title="isEdit ? '编辑导航' : '新增导航'" @confirm="confirm" :visible.sync="showDialog">
          <div class="dialog-content">
            <el-form ref="dataForm" :rules="rules" :model="form" label-width="120px" label-position="right">
              <el-form-item label="状态" prop="state">
                <el-radio-group v-model="form.state">
                  <el-radio :label="1">启用</el-radio>
                  <el-radio :label="2">停用</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="导航名称" prop="name">
                <el-input v-model="form.name" :maxlength="6" placeholder="请输入导航名称" />
              </el-form-item>
              <el-form-item label="排序" prop="sort">
                <el-input-number v-model="form.sort" :min="1" :max="200" label="请输入排序"></el-input-number>
              </el-form-item>
              <yl-tool-tip style="margin-left: 120px;">排序逻辑 1-200，200权重最高，权重一样时候，将按照时间排序</yl-tool-tip>
              <el-form-item label="链接" prop="link">
                <el-input v-model="form.link" type="textarea" placeholder="请输入跳转链接，例：https://www.baidu.com" />
              </el-form-item>
            </el-form>
          </div>
        </yl-dialog>
      </div>
    </div>

  </div>
</template>

<script>
import { getNavList, saveNav, updateNav } from '@/subject/admin/api/pop'
import { ylStatus } from '@/subject/admin/components'

export default {
  name: 'PopNav',
  components: {
    ylStatus
  },
  computed: {
  },
  data() {
    return {
      query: {
        page: 1,
        limit: 10,
        total: 0,
        time: [],
        status: 0
      },
      dataList: [],
      loading: false,
      showDialog: false,
      isEdit: false,
      form: {
        sort: 1,
        state: 1
      },
      rules: {
        name: [{ required: true, message: '请输入导航名称', trigger: 'blur' }],
        link: [{ required: true, message: '请输入跳转地址', trigger: 'blur' }],
        state: [{ required: true, message: '请选择状态', trigger: 'change' }]
      }
    }
  },
  activated() {
    this.getList()
  },
  methods: {
    // Enter
    searchEnter(e) {
      const keyCode = window.event ? e.keyCode : e.which;
      if (keyCode === 13) {
        this.getList()
      }
    },
    async getList() {
      this.loading = true
      let query = this.query
      if (!query.time) {
        query.time = []
      }
      let data = await getNavList(
        query.page,
        query.limit,
        query.status,
        query.name,
        query.time && query.time.length > 0 ? query.time[0] : null,
        query.time && query.time.length > 1 ? query.time[1] : null
      )
      this.loading = false
      if (data) {
        this.dataList = data.records
        this.query.total = data.total
      }
    },
    handleSearch() {
      this.query.page = 1
      this.getList()
    },
    handleReset() {
      this.query = {
        page: 1,
        limit: 10,
        time: [],
        status: 0
      }
    },
    // 编辑
    edit(row) {
      this.isEdit = true
      this.showDialog = true
      this.form = Object.assign({}, row)
    },
    // 新增
    addNew() {
      this.isEdit = false
      this.showDialog = true
      this.form = {
        sort: 1,
        state: 1
      }
    },
    // 点击弹框确认
    confirm() {
      this.$refs.dataForm.validate(async valid => {
        if (valid) {
          let form = this.form
          let data = null
          this.$common.showLoad()
          if (this.isEdit) {
            data = await updateNav(form.id, form.link, form.name, form.sort, form.state)
          } else {
            data = await saveNav(form.link, form.name, form.sort, form.state)
          }
          this.$common.hideLoad()
          if (data && data.result) {
            this.showDialog = false
            this.$common.n_success(this.isEdit ? '修改成功' : '新增成功')
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
@import './index.scss';
</style>
