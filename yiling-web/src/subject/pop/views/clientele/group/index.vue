<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box" style="margin-top:0;">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">分组名称</div>
              <el-input v-model="query.name" placeholder="请输入要查询的分组名称" @keyup.enter.native="handleSearch" />
            </el-col>
            <el-col :span="6">
              <div class="title">分组类型</div>
              <el-select v-model="query.type" placeholder="请选择分组类型">
                <el-option
                  v-for="item in grouptype"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-col>
          </el-row>
        </div>
        <div class="search-box">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn :total="total" @search="handleSearch" @reset="handleReset" />
            </el-col>
          </el-row>
        </div>
      </div>
      <!-- 导出、导入按钮 -->
      <div class="down-box clearfix">
        <div class="btn">
          <ylButton v-role-btn="['1']" type="primary" plain @click="addGroupClick">新建分组</ylButton>
        </div>
      </div>
      <!-- 底部列表 -->
      <div
        class="mar-t-8"
        style="padding-bottom: 10px;background: #FFFFFF;border-radius: 4px;"
      >
        <yl-table
          border
          :show-header="true"
          :list="dataList"
          :total="total"
          :page.sync="query.page"
          :limit.sync="query.limit"
          :loading="loading"
          @getList="getList"
        >
          <el-table-column label="序号" min-width="55" align="center">
            <template slot-scope="{ $index }">
              <div>{{ (query.page - 1) * query.limit + $index + 1 }}</div>
            </template>
          </el-table-column>
          <el-table-column label="分组名称" min-width="162" align="center">
            <template slot-scope="{ row }">
              <div class="product-desc">
                <div>{{ row.name }}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="分组类型" min-width="76" align="center">
            <template slot-scope="{ row }">
              <div>{{ row.type | dictLabel(grouptype) }}</div>
            </template>
          </el-table-column>
          <el-table-column label="客户数" min-width="120" align="center" props="customerNum">
            <template slot-scope="{ row }">
              <div>{{ row.customerNum }}</div>
            </template>
          </el-table-column>
          <el-table-column label="分组创建时间" min-width="174" align="center">
            <template slot-scope="{ row }">
              <div>{{ row.updateTime | formatDate }}</div>
            </template>
          </el-table-column>
          <el-table-column label="操作" min-width="174" align="center">
            <template slot-scope="{ row }">
              <div>
                <div><yl-button v-role-btn="['2']" type="text" @click="editGroupClick(row)">编辑分组</yl-button></div>
                <div><yl-button v-role-btn="['3']" type="text" @click="groupDeleteClick(row)">删除分组</yl-button></div>
                <div><yl-button v-role-btn="['4']" type="text" @click="groupMoveClick(row)">移动本组客户</yl-button></div>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
      <!-- 移动分组弹框 -->
      <el-dialog
        width="33%"
        :visible.sync="showMoveGroupDialog"
        custom-class="move-dialog"
        :show-close="true"
        :destroy-on-close="true"
        :close-on-click-modal="false"
        @close="moveGroupDialogClose"
      >
        <div class="move-dialog-content">
          <div class="icon-view">
            <i class="el-icon-warning icon"></i>
            <span>移动本组客户</span>
          </div>
          <div>
            <el-form
              :model="groupForm"
              :rules="rules"
              ref="groupRef"
              label-width="110px"
              class="demo-ruleForm"
            >
              <el-form-item label="请选择目标组" prop="groupId">
                <el-select
                  v-model="groupForm.groupId"
                  placeholder="请选择目标组"
                  v-loading="selectLoading"
                >
                  <el-option
                    v-for="item in allGroupList"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                    :disabled="item.id == currentGroupId"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-form>
          </div>
        </div>
        <span slot="footer" class="dialog-footer">
          <yl-button @click="showMoveGroupDialog = false">取 消</yl-button>
          <yl-button type="primary" @click="moveToTargetClick">确 定</yl-button>
        </span>
      </el-dialog>
      <!-- 新建分组弹框 -->
      <el-dialog
        title="新建分组"
        width="33%"
        :visible.sync="showAddGroupDialog"
        custom-class="add-dialog"
        :show-close="true"
        :destroy-on-close="true"
        :close-on-click-modal="false"
      >
        <div class="move-dialog-content">
          <div>
            <el-form
              :model="addGroupForm"
              :rules="addGroupRules"
              ref="addGroupRef"
              label-width="78px"
              class="demo-ruleForm"
            >
              <el-form-item label="分组名称" prop="name">
                <el-input v-model="addGroupForm.name" :maxlength="10" show-word-limit></el-input>
              </el-form-item>
            </el-form>
          </div>
        </div>
        <span slot="footer" class="dialog-footer">
          <yl-button @click="addGroupDialogClick(1)">稍后配置客户</yl-button>
          <yl-button type="primary" @click="addGroupDialogClick(2)">立即配置客户</yl-button>
        </span>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import {
  getCustomerGroupList,
  addGroup,
  removeGroup,
  moveCustomers
} from '@/subject/pop/api/channel';

export default {
  name: 'Group',
  components: {},
  computed: {},
  data() {
    return {
      // 头部导航
      navList: [
        {
          title: '首页',
          path: '/dashboard'
        },
        {
          title: '客户管理'
        },
        {
          title: '客户分组'
        }
      ],
      grouptype: [
        {
          label: '全部',
          value: 0
        },
        {
          label: '平台创建',
          value: 1
        },
        {
          label: 'ERP同步',
          value: 2
        }
      ],
      loading: false,
      query: {
        page: 1,
        limit: 10
      },
      total: 0,
      dataList: [],
      showMoveGroupDialog: false,
      selectLoading: false,
      groupForm: {
        groupId: ''
      },
      rules: {
        groupId: [
          { required: true, message: '请选择目标组', trigger: 'change' }
        ]
      },
      allGroupList: [],
      // 新建分组弹框
      showAddGroupDialog: false,
      addGroupForm: {
        name: ''
      },
      addGroupRules: {
        name: [{ required: true, message: '请输入分组名称', trigger: 'blur' }]
      }
    };
  },
  activated() {
    this.getList();
  },
  methods: {
    async getList() {
      this.loading = true;
      let query = this.query;
      let data = await getCustomerGroupList(
        query.page,
        query.limit,
        0,
        query.type,
        query.name //渠道商名称
      );
      this.loading = false;
      this.dataList = data.records;
      this.total = data.total;

      this.$log('this.dataList:', this.dataList);
    },
    // 搜索点击
    handleSearch() {
      this.query.page = 1
      this.getList();
    },
    handleReset() {
      this.query = {
        page: 1,
        limit: 10
      };
    },
    // 删除分组
    groupDeleteClick(row) {
      this.$confirm('删除该分组和该组关联的信息也会删除！', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          //确定
          this.removeGroupMethod(row.id);
        })
        .catch(() => {
          //取消
        });
    },
    // 删除分组
    async removeGroupMethod(groupId) {
      this.$common.showLoad();
      let data = await removeGroup(groupId);
      this.$common.hideLoad();
      if (data && data.result) {
        this.$common.n_success('删除成功');
        this.getList(); //刷新列表
      }
    },
    moveGroupDialogClose() {
      this.$refs['groupRef'].resetFields()
    },
    // 移动分组
    async groupMoveClick(row) {
      this.groupForm = {}
      this.currentGroupId = row.id;
      this.showMoveGroupDialog = true;
      this.selectLoading = true;
      // 获取所有分组列表
      let data = await getCustomerGroupList(1, 999, 0);
      this.selectLoading = false;
      this.allGroupList = data.records;
    },
    // 移动分组 点击
    moveToTargetClick() {
      this.$refs['groupRef'].validate(valid => {
        if (valid) {
          this.moveCustomersMethod();
        } else {
          return false;
        }
      });
    },
    async moveCustomersMethod() {
      this.$common.showLoad();
      let groupForm = this.groupForm;
      let data = await moveCustomers(this.currentGroupId, groupForm.groupId);
      this.$common.hideLoad();
      if (data && data.result) {
        this.$common.n_success('移动分组成功');
        this.showMoveGroupDialog = false;
        this.getList(); //刷新列表
      }
      this.$log('adata:', data);
    },
    // 新建分组 弹框显示
    addGroupClick() {
      this.addGroupForm = {}
      this.showAddGroupDialog = true;
    },
    addGroupDialogClick(type) {
      //type: 1:稍后配置  2:立即配置
      this.$refs['addGroupRef'].validate(valid => {
        if (valid) {
          this.addGroupMethod(type);
        } else {
          return false;
        }
      });
    },

    async addGroupMethod(type) {
      this.$common.showLoad();
      let addGroupForm = this.addGroupForm;
      let data = await addGroup(
        addGroupForm.name //渠道商名称
      );
      this.$common.hideLoad();
      if (data && data.id) {
        let groupId = data.id;
        this.$common.n_success('新建成功');
        this.showAddGroupDialog = false;
        if (type == 1) {
          //1:稍后配置
          this.getList(); //刷新列表
        } else if (type == 2) {
          //2:立即配置
          // 进入编辑界面
          this.$router.push({
            name: 'GroupEdit',
            params: { customerGroupId: groupId, name: addGroupForm.name }
          });
        }
      }
      this.$log('adata:', data);
    },
    // 编辑分组
    editGroupClick(row) {
      let groupId = row.id;
      // 进入编辑界面
      this.$router.push({
        name: 'GroupEdit',
        params: { customerGroupId: groupId, name: row.name }
      });
    }
  }
};
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>

<style lang="scss">
.move-dialog {
  .el-dialog__header {
    padding: 0;
  }
  .el-dialog__body {
    padding: 32px 46px 0 46px;
  }
}
.add-dialog {
  .el-dialog__header {
    padding-top: 16px;
    text-align: center;
  }
  .el-dialog__body {
    padding: 32px 46px 0 46px;
  }
  .el-input--medium {
    width: 264px;
  }
}
</style>
