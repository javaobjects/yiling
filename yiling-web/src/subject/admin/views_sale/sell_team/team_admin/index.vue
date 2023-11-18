<template>
  <div class="app-container">
    <div class="app-container-content">
      <div class="common-box box-search">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">姓名</div>
              <el-input v-model="query.name" @keyup.enter.native="searchEnter" placeholder="请输入姓名" />
            </el-col>
            <el-col :span="8">
              <div class="title">注册时间</div>
              <el-date-picker 
                v-model="query.date" 
                type="daterange" 
                format="yyyy/MM/dd" 
                value-format="yyyy-MM-dd" 
                range-separator="至" 
                start-placeholder="开始日期" 
                end-placeholder="结束日期">
              </el-date-picker>
            </el-col>
            <el-col :span="8">
              <div class="title">联系方式</div>
              <el-input v-model="query.mobilePhone" @keyup.enter.native="searchEnter" placeholder="请输入联系方式" />
            </el-col>
          </el-row>
        </div>
        <div class="mar-t-16">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn :total="total" @search="handleSearch" @reset="handleReset" />
            </el-col>
          </el-row>
        </div>
      </div>
      <div class="mar-t-16">
        <yl-table 
          :list="dataList" 
          :total="total" 
          :cell-class-name="() => 'border-1px-b'" 
          :page.sync="query.page" 
          :limit.sync="query.limit" 
          :loading="loading" 
          @getList="getList" 
          class="team-table">
          <el-table-column>
            <template slot-scope="{ row, $index }">
              <div class="table-view" :key="$index">
                <div class="session font-size-base">
                  <span>状态：<span :class="row.registerStatus === 1 ? 'col-down' : 'col-up'">{{ row.registerStatus | enRegister }}</span></span>
                  <span>注册时间：{{ row.registerTime | formatDate }}</span>
                  <span class="mar-l-16" v-if="row.registerStatus === 9">注销时间：{{ row.deregisterTime | formatDate }}</span>
                </div>
                <div class="table-content flex-row-left">
                  <div class="table-item flex1">
                    <div class="item font-size-base font-important-color">
                      <span class="font-title-color">姓名：</span>{{ row.name }}
                    </div>
                    <div class="item font-size-base font-important-color">
                      <span class="font-title-color">联系方式：</span>{{ row.mobilePhone }}
                    </div>
                    <div class="item font-size-base font-important-color">
                      <span class="font-title-color">获取方式：</span>{{ row.inviteType | inviteType }}
                    </div>
                    <div class="item font-size-base font-important-color">
                      <span class="font-title-color">证件号：</span>{{ row.idNumber }}
                    </div>
                  </div>
                  <div class="table-item flex1">
                    <div class="item font-size-base font-important-color">
                      <span class="font-title-color">拉取人：</span>{{ row.inviteName }}
                    </div>
                    <div class="item font-size-base font-important-color h-22">
                      <span class="font-title-color"></span>
                    </div>
                    <div class="item font-size-base font-important-color h-22">
                      <span class="font-title-color"></span>
                    </div>
                  </div>
                  <div class="table-item flex1">
                    <div class="item font-size-base font-important-color">
                      <span class="font-title-color">所属团队：</span><span class="color-blue" @click="clickMemberNumber(row)">{{ row.teamNum }}个</span>
                    </div>
                    <div class="item font-size-base font-important-color h-22">
                      <span class="font-title-color"></span>
                    </div>
                    <div class="item font-size-base font-important-color">
                      <span class="font-title-color">职务：</span>{{ row.position }}
                    </div>
                  </div>
                  <div class="table-item flex1">
                    <div class="item font-size-base font-important-color item-border">
                      <span class="font-title-color">订单总金额：</span>{{ row.orderAmount | toThousand('￥') }}
                    </div>
                  </div>
                </div>
                <div class="table-bottom"></div>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
      <yl-dialog 
        width="580px" 
        title="所属团队" 
        :visible.sync="show" 
        :show-footer="true" 
        :show-header="false" 
        @confirm="show = false" 
        :show-cancle="false">
        <div class="dialog-content">
          <yl-table :list="memberList" :show-header="true" stripe>
            <el-table-column prop="teamName" center label="团队"></el-table-column>
            <el-table-column prop="position" center label="职务"></el-table-column>
            <el-table-column prop="memberNum" center label="团队人数"></el-table-column>
          </yl-table>
        </div>
      </yl-dialog>
    </div>
  </div>
</template>

<script>
import {
  saleTeamManager,
  getTeamManager
} from '@/subject/admin/api/views_sale/sale_team_admin.js';
export default {
  name: 'TeamAdmin',
  filters: {
    enRegister(e) {
      // 0 未注册  1 已注册  9 已注销
      if (parseInt(e) === 0) {
        return '未注册';
      } else if (parseInt(e) === 1) {
        return '已注册';
      } else if (parseInt(e) === 9) {
        return '已注销';
      } else {
        return '- -';
      }
    },
    inviteType(e) {
      //  1 短信    2 微信
      if (parseInt(e) === 1) {
        return '短信';
      } else if (parseInt(e) === 2) {
        return '微信';
      } else {
        return '- -';
      }
    }
  },
  data() {
    return {
      query: {
        name: '',
        date: '',
        mobilePhone: '',
        page: 1,
        limit: 20
      },
      total: 0,
      loading: false,
      dataList: [],
      show: false,
      memberList: []
    };
  },
  created() {
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
    handleSearch() {
      this.query.page = 1;
      this.getList();
    },
    handleReset() {
      this.query = Object.assign(
        {},
        {
          name: '',
          date: '',
          mobilePhone: '',
          page: 1,
          limit: 20
        }
      );
    },
    // 获取成员所属团队
    async clickMemberNumber(row) {
      this.memberList = [];
      this.$common.showLoad()
      let data = await getTeamManager(row.userId);
      console.log(data);
      this.show = true;
      if (data !== undefined) {
        this.memberList = data;
      }
    },
    showDetail(row) {
      this.$router.push({
        name: 'TeamAdminDetail',
        params: { id: row.userId }
      });
    },
    async getList() {
      this.loading = true;
      let query = this.query;
      let data = await saleTeamManager(
        query.page,
        query.mobilePhone,
        query.name,
        query.date && query.date.length > 0 ? query.date[0] : '',
        query.date && query.date.length > 1 ? query.date[1] : '',
        query.limit
      );
      this.loading = false;
      if (data) {
        this.dataList = data.records;
        this.total = data.total;
      }
    }
  }
};
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>