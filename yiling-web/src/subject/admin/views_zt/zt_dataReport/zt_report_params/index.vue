<!--
 * @Description: 
 * @Author: xuxingwang
 * @Date: 2022-02-22 18:02:32
 * @LastEditTime: 2022-02-28 16:45:00
 * @LastEditors: xuxingwang
-->

<template>
  <div class="app-container">
    <div class="app-container-content">
      <div class="search-bar">
        <div class="header-bar">
          <div class="sign"></div>报表参数
        </div>
        <yl-table border :show-header="true" :list="dataList" :total="query.total" :page.sync="query.page" :limit.sync="query.limit" :loading="loading" @getList="getList">
          <el-table-column align="center" label="参数ID" prop="id"></el-table-column>
          <el-table-column align="center" label="参数名称" prop="paramName"></el-table-column>
          <el-table-column align="center" label="备注" prop="remark"></el-table-column>
          <el-table-column align="center" label="操作">
            <template slot-scope="{ row }">
              <yl-button type="text" @click="edit(row)">维护</yl-button>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </div>
</template>

<script>
import { getReportParamsList } from '@/subject/admin/api/zt_api/dataReport'
export default {
  name: 'ZtDataReportList',
  data() {
    return {
      query: {
        total: 0,
        page: 1,
        limit: 10
      },
      loading: false,
      dataList: []
    }
  },
  activated() {
    this.getList();
  },
  methods: {
    async getList() {
      this.loading = true
      let data = await getReportParamsList(this.query.page, this.query.limit)
      this.loading = false
      console.log(data);
      if (data !== undefined) {
        this.dataList = data.records
      }
    },
    edit(e) {
      // 跳转维护商品类型
      if (e.id == 4) {
        this.$router.push({
          name: 'ZtParamsEditType',
          params: { id: e.id, type: e.paramName }
        })
      }
      else if (e.id == 5) {
        // 跳转 促销活动
        this.$router.push({
          name: 'ZtParamsSalesPromotion',
          params: { id: e.id, type: e.paramName }
        })
      } else if (e.id == 6) {
        // 跳转阶梯规则
        this.$router.push({
          name: 'ZtParamsLadderRule',
          params: { id: e.id, type: e.paramName }
        })
      } else if (e.id == 7) {
        // 跳转商家品和以岭对应关系
        this.$router.push({
          name: 'ZtParamsBussinessAndYLCorresponding',
          params: { id: e.id, type: e.paramName }
        })
      } else if (e.id == 8) {
        // 跳转会员返利
        this.$router.push({
          name: 'ZtParamsMemberRebate',
          params: { id: e.id, type: e.paramName }
        })
      } else {
        // 跳转维护价格
        this.$router.push({
          name: 'ZtParamsEditPrice',
          params: { id: e.id, type: e.paramName }
        })
      }
    }

  }
}
</script>
<style lang="scss" scoped>
@import './index.scss';
</style>
