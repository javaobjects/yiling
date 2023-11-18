<template>
  <div class="app-container">
    <!-- 内容区 -->
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">企业名称</div>
              <el-input v-model="query.name" @keyup.enter.native="searchEnter" placeholder="请输入企业名称" />
            </el-col>
            <el-col :span="6">
              <div class="title">社会信用统一代码</div>
              <el-input v-model="query.licenseNumber" @keyup.enter.native="searchEnter" placeholder="请输入社会信用统一代码" />
            </el-col>
            <el-col :span="12">
              <div class="title">提交时间</div>
              <el-date-picker
                v-model="query.tjTime"
                type="daterange"
                format="yyyy/MM/dd"
                value-format="yyyy-MM-dd"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期">
              </el-date-picker>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">审核状态</div>
              <el-select v-model="query.status" placeholder="请选择审核状态">
                <el-option label="全部" :value="0"></el-option>
                <el-option
                  v-for="item in orderStatus"
                  v-show="item.value != 10 && item.value != 100"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-col>
            <el-col :span="12">
              <div class="title">企业类型</div>
              <el-select class="select-width" v-model="query.type" placeholder="请选择审核状态">
                <el-option :key="0" label="全部" :value="0"></el-option>
                <el-option v-for="itm in companyType" :key="itm.value" :label="itm.label" :value="itm.value">
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
      <!-- 底部列表 -->
       <div class="mar-t-8 bottom-content-view" style="padding-bottom: 10px;background: #FFFFFF;">
        <yl-table
          :list="dataList"
          :total="query.total"
          :page.sync="query.page"
          :limit.sync="query.limit"
          :loading="loading"
          :horizontal-border="false"
          :cell-no-pad="true"
          @getList="getList"
        >
          <el-table-column>
            <template slot-scope="{ row, $index }">
              <div class="table-view" :key="$index">
                <div class="content">
                   <div class="item content-left table-item text-l mar-l-16">
                    <div class="title font-size-lg bold">
                     {{ row.name }}
                    </div>
                    <div class="item-text font-size-base font-title-color">
                      企业ID：{{ row.eid }}
                    </div>
                    <div class="item-text font-size-base font-title-color">
                      提交时间：{{ row.submitTime | formatDate }}
                    </div>
                  </div>
                  <div class="content-left table-item">
                    <div class="item font-size-base font-title-color">
                      <span class="font-light-color">审核状态：</span>
                      {{ row.status == 1 ? '待审核' : (row.status == 2 ? '审核成功' : '审核失败') }}
                    </div>
                    <div class="item font-size-base font-title-color">
                      <span class="font-light-color">企业类型：</span>
                      {{ row.type | dictLabel(companyType) }}
                    </div>
                  </div>
                  <div class="content-left table-item">
                    <div class="item font-size-base font-title-color">
                      <span class="font-light-color">操作人：</span>
                      {{ row.auditUserName }}
                    </div>
                    <div class="item font-size-base font-title-color">
                      <span class="font-light-color">操作时间：</span>
                      {{ row.auditTime | formatDate }}
                    </div>
                  </div>
                  <div class="content-right flex-column-center">
                    <div v-if="row.status == 1">
                      <yl-button @click="toExamineClick(row)" type="text">审核</yl-button>
                    </div>
                    <div>
                      <yl-button @click="seeClick(row)" type="text">查看</yl-button>
                    </div>
                  </div>
                </div>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </div>
</template>

<script>
import { queryReceiptAccountPageList } from '@/subject/admin/api/zt_api/enterprise_audit';
import { enterpriseType } from '@/subject/admin/utils/busi'
export default {
  name: 'CollectionAccount',
  components: {

  },
  computed: {
    companyType() {
      return enterpriseType()
    }
  },
  data() {
    return {
      // 头部导航
      navList: [
        {
          title: '首页',
          path: '/dashboard'
        },
        {
          title: '企业审核'
        },
        {
          title: '企业收款账户列表'
        }
      ],
      loading: false,
      query: {
        name: '',
        licenseNumber: '',
        tjTime: [],
        status: '',
        type: '',
        page: 1,
        limit: 10,
        total: 0
      },
      orderStatus: [
        {
          value: 1,
          label: '待审核'
        },
        {
          value: 2,
          label: '审核成功'
        },
        {
          value: 3,
          label: '审核失败'
        }
      ],
      dataList: []
    };
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
      this.loading = true;
      let query = this.query;
      let data = await queryReceiptAccountPageList(
        query.page,
        query.licenseNumber,
        query.tjTime[1] ? query.tjTime[1] : '',
        query.tjTime[0] ? query.tjTime[0] : '',
        query.name,
        query.limit,
        query.status,
        query.type
      )
      if (data != undefined) {
        this.dataList = data.records;
        this.query.total = data.total;
      }
      console.log(this.dataList,11111)
      this.loading = false;
    },
    // 搜索点击
    handleSearch() {
      this.query.page = 1
      this.getList()
    },
    handleReset() {
      this.query = {
        name: '',
        licenseNumber: '',
        tjTime: [],
        status: '',
        type: '',
        page: 1,
        limit: 10,
        total: 0
      }
    },
    
    //点击审核 跳转审核界面
    toExamineClick(row) {
      let data = {
        id: row.id,
        data: 1 // 1代表是审核 2 代表查看
      };
      this.$router.push('/enterprise_audit/collection_account_toExamine/'+ JSON.stringify(data));
    },
    seeClick(row) {
      // true代表是审核 false 代表查看
      let data = {
        id: row.id,
        data: 2 // 1代表是审核 2 代表查看
      };
      this.$router.push('/enterprise_audit/collection_account_toExamine/' + JSON.stringify(data));
    },
 
    getCellClass(row) {
      if (!row.row.show) {
        return 'border-1px-b'
      }
      return ''
    }
  }
};
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
<style lang="scss">
  .order-table-view {
    .table-row {
      margin: 0 30px;
      td {
        .el-table__expand-icon{
          visibility: hidden;
        }
      }
    }
  }
</style>
