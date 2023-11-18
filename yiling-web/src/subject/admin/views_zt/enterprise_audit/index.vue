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
              <div class="title">企业营业执照号/医疗机构许可证号</div>
              <el-input v-model="query.licenseNumber" @keyup.enter.native="searchEnter" placeholder="请输入企业营业执照号/医疗机构许可证号" />
            </el-col>
            <el-col :span="12">
              <div class="title">地域查询</div>
              <div class="flex-row-left">
                <yl-choose-address
                  :province.sync="query.provinceCode"
                  :city.sync="query.cityCode"
                  :area.sync="query.regionCode"
                  is-all
                />
              </div>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">企业类型：</div>
              <el-select class="select-width" v-model="query.type" placeholder="请选择">
                <el-option :key="0" label="全部" :value="0"></el-option>
                <el-option v-for="itm in companyType" :key="itm.value" :label="itm.label" :value="itm.value">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <div class="title">审核类型：</div>
              <el-select class="select-width" v-model="query.authType" placeholder="请选择">
                <el-option :key="0" label="全部" :value="0"></el-option>
                <el-option :key="1" label="首次认证" :value="1"></el-option>
                <el-option :key="2" label="资质更新" :value="2"></el-option>
                <el-option :key="3" label="驳回后再次认证" :value="3"></el-option>
              </el-select>
              <!-- <el-radio-group v-model="query.authType">
                <el-radio :label="0">全部</el-radio>
                <el-radio :label="1">首次认证</el-radio>
                <el-radio :label="2">资质更新</el-radio>
                <el-radio :label="3">驳回后再次认证</el-radio>
              </el-radio-group> -->
            </el-col>
            <el-col :span="12">
              <div class="title">申请时间</div>
              <el-date-picker
                v-model="query.sendTime"
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
            <el-col :span="12">
              <div class="title">数据来源：</div>
              <el-select class="select-width" v-model="query.source" placeholder="请选择">
                <el-option :key="0" label="全部" :value="0"></el-option>
                <el-option v-for="itm in sourceType" :key="itm.value" :label="itm.label" :value="itm.value">
                </el-option>
              </el-select>
              <!-- <el-radio-group v-model="query.source">
                <el-radio :label="0">全部</el-radio>
                <el-radio :label="1">B2B</el-radio>
                <el-radio :label="2">销售助手</el-radio>
              </el-radio-group> -->
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
      <!-- 下部表格 -->
      <el-tabs
        class="my-tabs"
        v-model="activeTab"
        @tab-click="handleTabClick">
        <el-tab-pane label="待审核" name="1"></el-tab-pane>
        <el-tab-pane label="审核通过" name="2"></el-tab-pane>
        <el-tab-pane label="审核驳回" name="3"></el-tab-pane>
      </el-tabs>
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
                <div class="session font-size-base flex-between">
                  <div>
                    <span>
                      任务ID：
                      <span>{{ row.id }}</span>
                    </span>
                  </div>
                  <div>
                    <span>申请时间：{{ row.createTime | formatDate }}</span>
                  </div>
                </div>
                <div class="content">
                   <div class="item content-left table-item text-l mar-l-16">
                    <div class="title font-size-lg bold">
                     {{ row.name }}
                    </div>
                    <div class="item-text font-size-base font-title-color">
                      {{ row.type | dictLabel(companyType) }}
                    </div>
                    <div class="item-text font-size-base font-title-color">
                      {{ row.licenseNumber }}
                    </div>
                    <div class="item-text font-size-base font-title-color">
                      {{ row.provinceName }}{{ row.cityName }}{{ row.regionName }} {{ row.address }}
                    </div>
                  </div>
                  <div class="content-left table-item">
                    <div class="item font-size-base font-title-color">
                      <span class="font-light-color">联系人姓名：</span>
                      {{ row.contactor }}
                    </div>
                    <div class="item font-size-base font-title-color">
                      <span class="font-light-color">联系人电话：</span>
                      {{ row.contactorPhone }}
                    </div>

                  </div>
                  <div class="content-left table-item">
                    <div class="item font-size-base font-title-color">
                      <span class="font-light-color">审核类型：</span>
                      {{ row.authType == 1 ? '首次认证 ' : (row.authType == 2 ? '资质更新' : '驳回后再次认证') }}
                    </div>
                    <div class="item font-size-base font-title-color">
                      <span class="font-light-color">数据来源：</span>
                      {{ row.source | dictLabel(sourceType) }}
                    </div>
                  </div>
                  <div class="content-left table-item">
                    <div class="item font-size-base font-title-color">
                      <span class="font-light-color">审核人：</span>
                      {{ row.authUserName || '- -'	}}
                    </div>
                    <div class="item font-size-base font-title-color">
                      <span class="font-light-color">审核时间：</span>
                      {{ row.authTime	| formatDate }}
                    </div>
                  </div>
                  <div class="content-right flex-column-center" v-if="activeTab==1">
                    <div>
                      <yl-button @click="toExamineClick(row)" type="text">审核</yl-button>
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
import { ylChooseAddress } from '@/subject/admin/components';
import { pageList } from '@/subject/admin/api/zt_api/enterprise_audit';
import { enterpriseType, enterpriseSource } from '@/subject/admin/utils/busi'
export default {
  name: 'EnterpriseAuditList',
  components: {
    ylChooseAddress
  },
  computed: {
    companyType() {
      return enterpriseType()
    },
    sourceType() {
      return enterpriseSource()
    }
  },
  data() {
    return {
      query: {
        name: '',
        licenseNumber: '',
        provinceCode: '',
        cityCode: '',
        regionCode: '',
        sendTime: [],
        type: 0,
        authType: 0,
        source: 0,
        page: 1,
        limit: 10,
        total: 0
      },
      activeTab: '1',
      loading: false,
      dataList: []
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
    async getList() {
      this.loading = true;
      let query = this.query;
      let data = await pageList(
        this.activeTab,
        query.authType,
        query.cityCode,
        query.page,
        query.sendTime[1] ? query.sendTime[1] : '',
        query.licenseNumber,
        query.name,
        query.provinceCode,
        query.regionCode,
        query.limit,
        query.source,
        query.sendTime[0] ? query.sendTime[0] : '',
        query.type
      );
      if (data != undefined) {
        this.dataList = data.records;
        this.query.total = data.total;
      }
      this.loading = false;
      console.log(data,999)
    },
    // 搜索点击
    handleSearch() {
      this.query.page = 1
      this.getList()
    },
    // 清空查询
    handleReset() {
      this.query = {
        name: '',
        licenseNumber: '',
        provinceCode: '',
        cityCode: '',
        regionCode: '',
        sendTime: [],
        type: 0,
        authType: 0,
        source: 0,
        page: 1,
        limit: 10,
        total: 0
      }
    },
    // 点击切换
    handleTabClick(tab, event) {
      this.activeTab = tab.name;
      this.getList();
    },
    //审核
    toExamineClick(row) {
      if (row.authType == 1) {
        this.$router.push('/enterprise_audit/to_examine/' + row.eid)
      } else {
        this.$router.push('/enterprise_audit/to_examine_two/' + row.eid)
      }

    }
  }
}
</script>
<style lang="scss" scoped>
@import "./index.scss";
.my-tabs{
  padding-top: 20px;
}
.my-tabs ::v-deep .el-tabs__nav-wrap:after{
  background-color: rgba(255,255,255,0);
}
</style>
