<template>
  <div class="app-container">
    <!-- 内容区域 -->
    <div class="app-container-content">
      <!-- 中部 搜索条件 -->
      <div class="common-box">
        <div class="search-box" >
          <el-row class="box">
            <el-col :span="6">
              <div class="title">标题</div>
              <el-input v-model="query.title" @keyup.enter.native="searchEnter" placeholder="请输入标题" />
            </el-col>
            <el-col :span="6">
              <div class="title">状态</div>
              <el-select class="select-width" v-model="query.vajraStatus" placeholder="请选择">
                <el-option
                  v-for="item in ztData"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
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
      <div class="down-box clearfix">
        <div class="btn">
          <ylButton type="primary" @click="downLoadTemp">创建金刚位</ylButton>
        </div>
      </div>
      <!-- 下部列表 -->
      <div class="search-bar">
        <yl-table border :show-header="true" :list="dataList" :total="query.total" :page.sync="query.page" :limit.sync="query.limit" :loading="loading" @getList="getList">
          <el-table-column align="center" label="#" type="index" width="100">
            <template slot-scope="scope">
              <span>{{ (query.page - 1) * query.limit + scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="150" label="图片" prop="name">
            <template slot-scope="{ row }">
              <img class="img" :src="row.pic" alt="">
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="200" label="标题" prop="title"></el-table-column>
          <el-table-column align="center" min-width="200" label="条件">
            <template slot-scope="{ row }">
              <p v-show="row.linkType == 1">
                超链接: {{ row.activityLinks }}
              </p>
              <p v-show="row.linkType == 3">
                关键词: {{ row.searchKeywords }}
              </p>
              <p v-show="row.linkType == 4">
                商品ID: {{ row.goodsId }}
              </p>
              <p v-show="row.linkType == 5">
                供应商ID: {{ row.sellerEid }}
              </p>
              <p v-show="row.linkType == 8">
                会员ID: {{ row.activityLinks }}
              </p>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="80" label="排序" prop="sort"></el-table-column>
          <el-table-column align="center" min-width="80" label="状态" >
            <template slot-scope="{ row }">
              <div>{{ row.vajraStatus == 1 ? '启用' : '停用' }}</div>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="150" label="创建信息" >
            <template slot-scope="{ row }">
              <p>{{ row.createUserName }}</p>
              <p>{{ row.createTime | formatDate }}</p>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="150" label="最后维护信息" >
            <template slot-scope="{ row }">
              <p>{{ row.updateUserName }}</p>
              <p>{{ row.updateTime | formatDate }}</p>
            </template>
          </el-table-column>
         <el-table-column fixed="right" align="center" label="操作" min-width="140">
            <template slot-scope="{ row }">
              <div>
                <yl-button type="text" @click="modifyClick(row)">编辑</yl-button>
              </div>
              <div>
                <yl-button type="text" @click="sortClick(row)">排序</yl-button>
              </div>
              <div>
                <yl-button type="text" @click="deactivateClick(row)">{{ row.vajraStatus == 1 ? '停用' : '启用' }}</yl-button>
              </div>
              <div>
                <yl-button type="text" @click="deleteClick(row)">删除</yl-button>
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
 import { pageList,editStatus,dataDelete, editWeight } from '@/subject/admin/api/b2b_api/b2bAdministration'
export default {
  name: 'B2BHome',
  components: {
  },
  computed: {
  },
  data() {
    return {
      query: {
        title: '',
        vajraStatus: 0,
        total: 0,
        page: 1,
        limit: 10
      },
      ztData: [
         {
          label: '全部',
          value: 0
        },
        {
          label: '启用',
          value: 1
        },
        {
          label: '停用',
          value: 2
        }
      ],
      loading: false,
      dataList: [],
      showDialog: false, //排序弹窗
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
    this.getList();
  },
  methods: {
    // Enter
    searchEnter(e) {
      const keyCode = window.event ? e.keyCode : e.which;
      if (keyCode === 13) {
        this.getList()
      }
    },
    // 搜索
    handleSearch() {
      this.query.page = 1;
      this.getList();
    },
    // 重置
    handleReset() {
      this.query = {
        title: '',
        vajraStatus: 0,
        total: 0,
        page: 1,
        limit: 10
      }
    },
    async getList() {
      this.loading = true;
      let query = this.query;
      let data = await pageList(
        query.page,
        query.limit,
        query.title,
        query.vajraStatus
      )
      if (data != undefined) {
        this.dataList = data.records;
        this.query.total = data.total;
      }
      this.loading = false;
     
    },
    getCellClass({row,rowIndex}) {
      return 'border-1px-b'
    },
    // 创建金刚位
    downLoadTemp() {
      this.$router.push('/b2bAdministration/b2b_home_establish/' + '0');
      // this.$router.push({
      //   name: 'B2BHomeEstablish'
      // })
    },
    // 修改
    modifyClick(row) {
      this.$router.push('/b2bAdministration/b2b_home_establish/' + row.id);
    },
    //停用 
    deactivateClick(row) {
      this.$confirm(`确认${row.vajraStatus == 1 ? '停用' : '启用'} ！`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then( async() => {
          //确定
          this.$common.showLoad();
          let data = await editStatus(
            row.id,
            row.vajraStatus == 1 ? 2 : 1
          )
          this.$common.hideLoad();
          if (data!==undefined) {
            this.$common.n_success(`${row.vajraStatus == 1 ? '停用' : '启用'} 成功!`);
            this.getList();
          }
        })
        .catch(() => {
          //取消
        });
    },
    // 删除 
    deleteClick(row) {
      this.$confirm(`确认删除 ${row.title} ！`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then( async() => {
          //确定
          this.$common.showLoad();
          let data = await dataDelete(
            row.id
          )
          this.$common.hideLoad();
          if (data!==undefined) {
            this.$common.n_success('删除成功!');
            this.getList();
          }
        })
        .catch(() => {
          //取消
        });
    },
    // 排序
    sortClick(row) {
      this.form ={
        sort: row.sort,
        id: row.id
      }
      this.showDialog = true;

    },
    // 排序点击保存
    confirm() {
      this.$refs['dataForm1'].validate(async (valid) => {
        if (valid) {
          this.$common.showLoad();
          let data = await editWeight(
            this.form.id,
            this.form.sort
          )
          this.$common.hideLoad();
          if (data!=undefined) {
            this.showDialog = false;
             this.$common.n_success('修改成功!');
            this.getList();
          }
        }
      })
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
@import "./index.scss";
  .table-button::v-deep .el-button--medium {
    padding: 6px 20px;
  }
</style>
