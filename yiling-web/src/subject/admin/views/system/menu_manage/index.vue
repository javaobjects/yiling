<template>
  <div class="app-container">
    <div class="app-container-content">
      <div class="mar-tb-16">
        <yl-button type="primary" @click="addBtnClick">新增菜单</yl-button>
      </div>
      <!-- 底部列表 -->
      <div class="mar-t-16">
        <el-tabs class="bg-white" v-model="appId" type="card" @tab-click="handleTabClick">
          <el-tab-pane v-for="item in menuApps" :key="item.appId" :label="item.name" :name="item.appId + ''"></el-tab-pane>
          <!-- <el-tab-pane label="运营后台" name="1"></el-tab-pane>
          <el-tab-pane label="企业数据管理" name="5"></el-tab-pane>
          <el-tab-pane label="POP管理" name="2"></el-tab-pane>
          <el-tab-pane label="B2B管理" name="3"></el-tab-pane>
          <el-tab-pane label="销售助手" name="6"></el-tab-pane>
          <el-tab-pane label="健康管理中心" name="9"></el-tab-pane> -->
        </el-tabs>
        <yl-table
          border
          :show-header="true"
          :list="dataList"
          :total="0"
          :page.sync="query.page"
          :limit.sync="query.limit"
          :loading="loading"
          cell-no-pad
          :row-key="'id'"
          :tree-props="{ children: 'children' }"
          @getList="getList">
          <el-table-column label="菜单名称" prop="menuName">
          </el-table-column>
          <el-table-column align="center" min-width="80" label="类型">
            <template slot-scope="{ row }">
              <div>{{ row.menuType | dictLabel(menuTypeArray) }}</div>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="100" label="标识" prop="menuIdentification">
          </el-table-column>
          <el-table-column align="left" min-width="200" label="接口url">
            <template slot-scope="{ row }">
              <div class="menu-url-view">{{ row.menuUrl | lineBreak }}</div>
            </template>
          </el-table-column>
          <el-table-column align="center" width="80" label="状态">
            <template slot-scope="{ row }">
              <el-tag :type="row.menuStatus == 1 ? 'success' : 'danger'">{{ row.menuStatus | enable }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="40" label="排序" prop="sortNum">
          </el-table-column>
          <el-table-column align="center" label="备注" prop="remark">
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
            fixed="right"
            align="center"
            label="操作"
            width="120">
            <template slot-scope="{ row }">
              <div>
                <yl-button type="text" @click="edit(row)">修改</yl-button>
                <el-popconfirm
                  class="popconfirm"
                  @confirm="deleteClick(row)"
                  title="确定删除吗,如果存在下级节点则一并删除，此操作不能撤销">
                  <yl-button slot="reference" type="text">删除</yl-button>
                </el-popconfirm>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
    <!-- 新增员工 -->
    <yl-dialog
      :title="isEdit ? '修改' : '新增菜单'"
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
            <el-col :span="16">
              <el-form-item label="权限类型" prop="menuType">
                <el-radio-group v-model="form.menuType">
                  <el-radio-button :label="1">目录</el-radio-button>
                  <el-radio-button :label="2">菜单</el-radio-button>
                  <el-radio-button :label="3">按钮</el-radio-button>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="名称" prop="menuName">
                <el-input v-model="form.menuName" placeholder="请输入菜单名称" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="状态" prop="menuStatus">
                <el-radio-group v-model="form.menuStatus">
                  <el-radio :label="1">启用</el-radio>
                  <el-radio :label="2">停用</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="标识">
                <el-input v-model="form.menuIdentification" placeholder="菜单或按钮标识"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="父级ID" prop="parentId" class="select-parent">
                <el-select
                  v-model="form.parentId"
                  clearable
                  @clear="handleClear"
                  placeholder="请选择"
                  ref="selectParentId"
                >
                  <el-option hidden key="upResId" :value="form.parentId" :label="parentName" style="height: auto;">
                  </el-option>

                  <el-tree
                    class="my-tree"
                    :data="catalogTree"
                    :expand-on-click-node="false"
                    check-on-click-node
                    node-key="id"
                    ref="tree"
                    :current-node-key="form.parentId"
                    @node-click="handleNodeClick"
                    highlight-current
                    :props="{ children: 'children',label: 'menuName' }">
                  </el-tree>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="菜单排序" prop="sortNum">
                <el-input v-model="form.sortNum" placeholder="菜单排序" type="number"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="接口url">
                <el-input resize="none" :autosize="{ minRows: 5, maxRows: 5}" type="textarea" v-model="form.menuUrl" placeholder="接口url"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="备注" prop="remark">
                <el-input resize="none" :autosize="{ minRows: 3, maxRows: 3}" type="textarea" v-model="form.remark" placeholder="请输入备注" />
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
    </yl-dialog>
  </div>
</template>

<script>
import {
  listMenuApps,
  getMenusList,
  queryCatalogTree,
  createMenu,
  deleteMenu,
  updateMenu
} from '@/subject/admin/api/role'

export default {
  name: 'MenuManage',
  components: {
  },
  computed: {
  },
  data() {
    return {
      // 类型
      menuTypeArray: [
        {
          label: '目录',
          value: 1
        },
        {
          label: '菜单',
          value: 2
        },
        {
          label: '按钮',
          value: 3
        }
      ],
      isEdit: false,
      loading: false,
      appId: '1',
      query: {
        page: 1,
        limit: 10
      },
      dataList: [],
      // 弹框菜单目录树
      catalogTree: [],
      showDialog: false,
      form: {
        menuType: 1,
        // 菜单名称
        menuName: '',
        // 状态：1-启用 2-停用
        menuStatus: 1,
        menuIdentification: '',
        menuUrl: '',
        sortNum: '',
        parentId: '',
        remark: ''
      },
      parentName: '',
      rules: {
        menuType: [{ required: true, message: '请选择权限类型', trigger: 'change' }],
        menuName: [{ required: true, message: '请输入菜单名称', trigger: 'blur' }],
        menuStatus: [{ required: true, message: '请选择菜单状态', trigger: 'change' }]
      },
      menuApps: []
    }
  },
  filters: {
    // 换行
    lineBreak: function (value) {
      if (!value) return ''
      value = value.replace(/,/g, ',\n')
      return value
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
          this.getCatalogTree()
        }
      }
    },
    async getList() {
      this.loading = true
      let data = await getMenusList(
        Number(this.appId)
      )
      this.loading = false
      if (data) {
        this.dataList = data.list
      }
    },
    // 获取菜单目录树
    async getCatalogTree() {
      this.$common.showLoad()
      let data = await queryCatalogTree(
        Number(this.appId)
      )
      this.$common.hideLoad()
      if (data) {
        this.catalogTree = data.list
      }
    },
    handleTabClick() {
      this.getList()
    },
    edit(row) {
      this.isEdit = true
      this.form = {}
      this.parentName = ''
      this.showDialog = true
      this.getCatalogTree()
      this.form = {
        menuType: row.menuType,
        menuName: row.menuName,
        menuStatus: row.menuStatus,
        menuIdentification: row.menuIdentification,
        menuUrl: row.menuUrl,
        sortNum: row.sortNum || '',
        parentId: row.parentId || '',
        remark: row.remark,
        id: row.id
      }
      this.$nextTick(() => {
        let currentNode = this.$refs.tree.getCurrentNode()
        this.parentName = currentNode ? currentNode.menuName : ''
      })
    },
    // 新增点击
    addBtnClick() {
      this.isEdit = false
      this.form = {
        menuType: 1,
        menuName: '',
        menuStatus: 1,
        menuIdentification: '',
        menuUrl: '',
        sortNum: '',
        parentId: '',
        remark: ''
      }
      this.parentName = ''
      this.showDialog = true
      this.getCatalogTree()
    },
    handleNodeClick(data) {
      this.parentName = data.menuName
      this.form.parentId = data.id
    },
    handleClear() {
      // 将选择器的值置空
      this.parentName = ''
      this.form.parentId = ''
    },
    // 确认点击
    confirm() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          this.$common.showLoad()
          let form = this.form
          let data = null
          if (this.isEdit) {
            data = await updateMenu(
              form.id,
              Number(this.appId),
              form.menuType,
              form.menuName,
              form.menuStatus,
              form.menuIdentification,
              form.menuUrl,
              Number(form.sortNum),
              form.parentId ? Number(form.parentId) : 0,
              form.remark
            )
          } else {
            data = await createMenu(
              Number(this.appId),
              form.menuType,
              form.menuName,
              form.menuStatus,
              form.menuIdentification,
              form.menuUrl,
              Number(form.sortNum),
              form.parentId ? Number(form.parentId) : 0,
              form.remark
            )
          }
          this.$common.hideLoad()
          if (data && data.result) {
            this.showDialog = false
            this.$common.n_success('保存成功');
            this.getList()
          }
        } else {
          return false
        }
      })
    },
    // 删除
    async deleteClick(row) {
      this.$common.log(row)
      this.$common.showLoad()
      let data = await deleteMenu(
        [row.id]
      )
      this.$common.hideLoad()
      if (data && data.result) {
        this.showDialog = false
        this.$common.n_success('删除成功');
        this.getList()
      }
    },
    checkSortNum(val) {
      val = val.replace(/[^0-9]/gi, '')
      return val
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
  .setting-dialog-content{
    max-height: 600px;
    overflow-y: auto;
  }
  .select-parent{
    .el-input,
    .el-select {
      max-width: 100%;
    }
  }
</style>
