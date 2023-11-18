<template>
  <div class="app-container">
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">会员卡ID</div>
              <el-input v-model="query.id" @keyup.enter.native="searchEnter" placeholder="请输入" />
            </el-col>
            <el-col :span="6">
              <div class="title">会员卡名称</div>
              <el-input v-model="query.memberName" @keyup.enter.native="searchEnter" placeholder="请输入" />
            </el-col>
            <el-col :span="8">
              <div class="title">创建时间</div>
              <el-date-picker
                v-model="query.time"
                type="daterange"
                format="yyyy 年 MM 月 dd 日"
                value-format="yyyy-MM-dd"
                range-separator="至"
                start-placeholder="开始月份"
                end-placeholder="结束月份">
              </el-date-picker>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="16">
             <yl-search-btn
              :total="query.total"
              @search="handleSearch"
              @reset="handleReset"/>
            </el-col>
          </el-row>
        </div>
      </div>
      <!-- 导出按钮 -->
      <div class="down-box clearfix">
        <yl-button type="primary" @click="addBanner">新建</yl-button>
      </div>
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
          <el-table-column align="center" min-width="80" label="会员ID" prop="id">
          </el-table-column>
          <el-table-column align="center" min-width="120" label="会员名称" prop="name">
          </el-table-column>
          <el-table-column align="center" min-width="120" label="会员描述" prop="description">
          </el-table-column>
          <el-table-column align="center" min-width="80" label="已开通终端数" prop="openNum"></el-table-column>
          <el-table-column align="center" min-width="80" label="排序" prop="sort">
          </el-table-column>
          <el-table-column align="center" min-width="120" label="创建时间">
            <template slot-scope="{ row }">
              <span>{{ row.createTime | formatDate }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" label="最后维护信息">
            <template slot-scope="{ row }">
              <div>{{ row.updateName }}</div>
              <span>{{ row.updateTime | formatDate }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" label="操作" width="120">
            <template slot-scope="{ row }">
              <div>
                <yl-button type="text" @click="edit(row)">编辑</yl-button>
                <yl-button type="text" @click="sortClick(row)">排序</yl-button>
                <yl-button v-if="row.stopGet == '1'" type="text" @click="stopGetMemberClickMethod(row)">开始获得</yl-button>
                <yl-button v-else type="text" @click="stopGetMemberClick(row)">停止获得</yl-button>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
    <yl-dialog title="排序" @confirm="confirm" width="570px" :visible.sync="showDialog">
      <div class="dialogTc">
        <el-form :model="form" :rules="rules" ref="dataForm1" label-width="80px" class="demo-ruleForm">
          <el-form-item label="排序" prop="sort">
            <el-input v-model="form.sort" style="width: 200px;"
              @input="e => (form.sort = checkInput(e))" placeholder="排序"
            ></el-input>
          </el-form-item>
        </el-form>
      </div>
    </yl-dialog>
  </div>
</template>

<script>
  import { getMemberListPage, stopGetMember, updateSort } from '@/subject/admin/api/b2b_api/membership'
  export default {
    name: 'MembershipList',
    components: {
    },
    computed: {
    },
    data() {
      return {
        getTypeDict: [
          {
            value: 1,
            label: '付费'
          },
          {
            value: 2,
            label: '活动赠送'
          }
        ],
        query: {
          id: '',
          memberName: '',
          page: 1,
          limit: 10,
          total: 0,
          time: []
        },
        dataList: [],
        loading: false,
        //排序弹窗
        showDialog: false,
        form: {
          sort: '',
          id: ''
        },
        rules: {
          sort: [{ required: true, message: '请输入排序', trigger: 'blur' }]
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
        let data = await getMemberListPage(
          query.page,
          query.limit,
          query.memberName,
          query.time && query.time.length > 0 ? query.time[0] : null,
          query.time && query.time.length > 1 ? query.time[1] : null,
          query.id
        )
        this.loading = false
        if (data) {
          this.dataList = data.records
          this.query.total = data.total
        }
      },
      // 点击排序
      sortClick(row) {
        this.form = {
          sort: row.sort,
          id: row.id
        }
        this.showDialog = true;
      },
      // 排序点击保存
      confirm() {
        this.$refs['dataForm1'].validate( async(valid) => {
          if (valid) {
            this.$common.showLoad();
            let data = await updateSort(
              this.form.id,
              this.form.sort
            )
            this.$common.hideLoad();
            if (data !== undefined) {
              this.showDialog = false;
              this.$common.n_success('修改成功!');
              this.getList();
            }
          }
        })
      },
      handleSearch() {
        this.query.page = 1
        this.getList()
      },
      handleReset() {
        this.query = {
          id: '',
          memberName: '',
          page: 1,
          limit: 10,
          total: 0,
          time: []
        }
      },
      // 修改状态
      async stopGetMemberClick(row) {
        this.$confirm('执行操作后，系统将停止计算该会员的相关内容，前台获取通道关闭。', '停止获得', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        .then(() => {
          //确定
          this.stopGetMemberClickMethod(row);
        })
        .catch(() => {
          //取消
        });
      },
      async stopGetMemberClickMethod(row) {
        this.$common.showLoad()
        let data = await stopGetMember(row.id)
        this.$common.hideLoad()
        if (data != undefined) {
          this.$common.n_success('修改成功');
          this.getList()
        }
      },
      addBanner() {
        this.$router.push({
          name: 'MembershipEdit',
          params: {}
        });
      },
      edit(row) {
        // 跳转详情
        this.$router.push({
          name: 'MembershipEdit',
          params: { id: row.id }
        });
      },
      // 启用、停用
      async editStatus(row) {
        this.$common.showLoad()
        let currentStatus = row.state
        // eslint-disable-next-line no-unused-vars
        let str = ''
        if (currentStatus == 1) {
          row.state = 2
          str = '停用成功'
        } else {
          row.state = 1
          str = '启用成功'
        }
        let data = await updateHotWords(row.id, undefined, row.state)
        this.$common.hideLoad()
        if (data && data.result) {
          this.$common.n_success(str)
          this.getList()
        }
      },
      checkInput(val) {
        val = val.replace(/[^0-9]/gi, '')
        if (val > 200) {
          val = 200
        }
        if (val < 1) {
          val = ''
        }
        return val
      }
    }
  }
</script>

<style lang="scss" scoped>
  @import './index.scss';
</style>