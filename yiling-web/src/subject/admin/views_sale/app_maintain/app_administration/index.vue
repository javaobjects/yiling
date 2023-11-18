<template>
  <div class="app-container">
    <!-- 内容区 -->
    <div class="app-container-content has-bottom-bar">
       <!-- 上部条件搜索 -->
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="7">
              <div class="title">标题</div>
              <el-input v-model="query.title" @keyup.enter.native="searchEnter" placeholder="请输入标题" />
            </el-col>
            <el-col :span="7">
              <div class="title">状态</div>
              <el-select class="select-width" v-model="query.bannerStatus" placeholder="请选择">
                <el-option label="全部" value="0"></el-option>
                <el-option label="启用" value="1"></el-option>
                <el-option label="停用" value="2"></el-option>
              </el-select>
            </el-col>
            <el-col :span="10">
              <div class="title">创建时间</div>
              <el-date-picker
                v-model="query.cjTime"
                type="daterange"
                format="yyyy/MM/dd"
                value-format="yyyy-MM-dd"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期">
              </el-date-picker>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="7">
              <div class="title">banner位置</div>
              <el-select class="select-width" v-model="query.usageScenario" placeholder="请选择">
                <el-option label="全部" value="0"></el-option>
                <el-option label="主Banner" value="1"></el-option>
                <el-option label="副Banner" disabled value="2"></el-option>
              </el-select>
            </el-col>
            <!-- <el-col :span="7">
              <div class="title">企业名称</div>
              <el-select class="select-width" v-model="query.eid" placeholder="请选择">
                <el-option
                  v-for="item in qyData"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id">
                </el-option>
              </el-select>
            </el-col> -->
            <el-col :span="10">
              <div class="title">投放开始时间</div>
              <el-date-picker
                v-model="query.tfTime"
                type="daterange"
                format="yyyy/MM/dd"
                value-format="yyyy-MM-dd"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期">
              </el-date-picker>
            </el-col>
          </el-row>
        </div>
        <div class="mar-t-16">
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
       <!-- 添加banner -->
      <div class="down-box clearfix">
        <div class="btn">
          <ylButton type="primary" plain @click="addBanner">添加banner</ylButton>
        </div>
      </div>
      <div class="mar-t-8 pad-b-10 order-table-view">
        <yl-table
        border
        :show-header="true"
        :list="dataList"
        :total="query.total"
        :page.sync="query.page"
        :limit.sync="query.limit"
        :loading="loading"
        @getList="getList">
          <el-table-column align="center" label="序号" type="index" width="80">
            <template slot-scope="scope">
              <span>{{ (query.page - 1) * query.limit + scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" label="Banner名称" width="150" prop="title">
          </el-table-column>
          <el-table-column align="center" label="图片" width="200" prop="pic">
            <template slot-scope="{ row }">
              <div class="img-view">
                <el-image fit="contain" class="img" :src="row.pic"></el-image>
              </div>
            </template>
          </el-table-column>
          <el-table-column align="center" label="排序" min-width="100" prop="sort">
          </el-table-column>
          <el-table-column align="center" label="状态" min-width="80">
            <template slot-scope="{ row }">
              {{ row.bannerStatus == 1 ? '启用' : '停用' }}
            </template>
          </el-table-column>
          <el-table-column align="center" label="创建信息" min-width="180">
            <template slot-scope="{ row }">
              <div class="title font-size-large font-title-color">开始时间：{{ row.startTime | formatDate }}</div>
              <div class="ftitle font-size-large font-title-color">结束时间：{{ row.stopTime | formatDate }}</div>
              <div class="ftitle font-size-large font-title-color">创建时间：{{ row.createTime | formatDate }}</div>
            </template>
          </el-table-column>
          <el-table-column align="center" label="最后维护信息" min-width="200">
            <template slot-scope="{ row }">
              <div class="title font-size-large font-title-color">操作人：{{ row.updateUserName }} </div>
              <div class="title font-size-large font-title-color">操作时间：{{ row.updateTime | formatDate }}</div>
            </template>
          </el-table-column>
          <el-table-column fixed="right" align="center" label="操作" min-width="100">
            <template slot-scope="{ row }">
              <div>
                <yl-button type="text" @click="modifyClick(row)">编辑</yl-button>
              </div>
              <div>
                <yl-button type="text" @click="deactivateClick(row)">{{ row.bannerStatus == 1 ? '停用' : '启用' }}</yl-button>
              </div>
              <div>
                <yl-button type="text" @click="sortClick(row)">排序</yl-button>
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
        <el-form :model="form" :rules="rules" ref="dataForm1" label-width="200px" class="demo-ruleForm">
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
import { enterpriseType } from '@/subject/admin/utils/busi'
import { bannerPageList, editStatus, editWeight, bannerDelete } from '@/subject/admin/api/views_sale/app_maintain'
export default {
  name: 'AppAdministration',
  computed: {
    companyType() {
      return enterpriseType()
    }
  },
  data() {
    return {
      query: {
        title: '',
        bannerStatus: '0',
        cjTime: [],
        usageScenario: '0',
        tfTime: [],
        total: 0,
        page: 1,
        limit: 10
      },
      dataList: [],
      loading: false,
      qyData: [], //企业信息
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
    // this.qyApi();
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
    // 企业信息
    // async qyApi() {
    //   let data = await listYiLingEnterprise();
    //   if (data !== undefined) {
    //     this.qyData = data;
    //   }
    //   console.log(data,999)
    // },
    // 表格数据
    async getList() {
      this.loading = true;
      let query = this.query;
      let data = await bannerPageList(
        query.bannerCondition,
        query.bannerStatus,
        query.cjTime && query.cjTime.length > 0 ? query.cjTime[1] : '',
        query.cjTime && query.cjTime.length > 0 ? query.cjTime[0] : '',
        query.page,
        query.limit,
        query.title,
        query.usageScenario,
        query.tfTime && query.tfTime.length > 0 ? query.tfTime[1] : '',
        query.tfTime && query.tfTime.length > 0 ? query.tfTime[0] : ''
      );
      if (data !== undefined) {
        this.dataList = data.records;
        this.query.total = data.total;
      }
      this.loading = false;
    },
    // 添加banner
    addBanner() {
       this.$router.push('/app_maintain/app_administration_details/' + 0)
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
        bannerStatus: '0',
        cjTime: [],
        usageScenario: '0',
        tfTime: [],
        total: 0,
        page: 1,
        limit: 10
      }
    },
    // 点击编辑
    modifyClick(row) {
      this.$router.push('/app_maintain/app_administration_details/' + row.id)
    },
    // 停用/启用
    async deactivateClick(row) {
      this.$confirm( `确认 ${row.bannerStatus == 1 ? '停用' : '启用'} ${row.title}`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      .then(async() => {
        this.$common.showLoad();
        let data = await editStatus(
          row.bannerStatus == 1 ? 2 : 1,
          row.id
        )
        this.$common.hideLoad();
        if (data !== undefined) {
          this.$common.n_success('操作成功！')
          this.getList();
        }
      })
      .catch(() => {
        // this.$common.n_success('已取消')    
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
          let data = await bannerDelete(
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
    // 上架  下架
    // editStatus(row) {
    //   this.$confirm(`确定下架 ${row.title}？`, "提示", {
    //       confirmButtonText: "确定",
    //       cancelButtonText: "取消",
    //       type: "warning"
    //     })
    //     .then(async() => {
    //       let data = await release(
    //         row.id,
    //         1
    //       )
    //       if (data !== undefined) {
    //         this.$common.n_success('下架成功！')
    //         this.getList();
    //       }
    //     })
    //     .catch(() => {
    //       // this.$common.n_success('已取消')    
    //     });
    // }
  }
}
</script>

<style lang="scss" scoped>
    @import "./index.scss";
</style>
<style lang="scss">
  .order-table-view {
    .table-cell {
      border-bottom: 1px solid #DDDDDD;
    }
  }
</style>