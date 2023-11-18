<template>
  <div class="app-container">
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">推广方名称</div>
              <el-input v-model="query.promoterName" @keyup.enter.native="searchEnter" placeholder="请输入推广方名称" />
            </el-col>
            <el-col :span="8">
              <div class="title">推广方ID</div>
              <el-input v-model="query.promoterId" @keyup.enter.native="searchEnter" placeholder="请输入推广方ID" />
            </el-col>
            <el-col :span="8">
              <div class="title">下载时间</div>
              <el-date-picker 
                v-model="query.time" 
                type="daterange" 
                format="yyyy/MM/dd" 
                value-format="yyyy-MM-dd" 
                range-separator="至" 
                start-placeholder="开始日期" 
                end-placeholder="结束日期"
                >
              </el-date-picker>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="10">
              <div class="title">推广方地址</div>
              <yl-choose-address 
                width="230px" 
                :province.sync="query.provinceCode" 
                :city.sync="query.cityCode" 
                :area.sync="query.regionCode" 
                is-all
                />
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="24">
              <yl-search-btn :total="query.total" @search="handleSearch" @reset="handleReset" />
            </el-col>
          </el-row>
        </div>
      </div>
      <div class="down-box clearfix">
        <div class="btn">
          <ylButton type="primary" plain @click="downLoadTemp">查询结果导出</ylButton>
        </div>
      </div>
      <!-- 下部列表 -->
      <div class="search-bar mar-t-8">
        <yl-table 
          border 
          :show-header="true" 
          :list="dataList" 
          :total="query.total" 
          :page.sync="query.page" 
          :limit.sync="query.limit" 
          :loading="loading" 
          @getList="getList">
          <el-table-column align="center" min-width="90" label="ID" prop="id"></el-table-column>
          <el-table-column align="center" min-width="100" label="推广方ID" prop="promoterId">
          </el-table-column>
          <el-table-column align="center" min-width="150" label="推广方名称" prop="promoterName">
          </el-table-column>
          <el-table-column align="center" min-width="120" label="下载时间">
            <template slot-scope="{ row }">
              {{ row.downloadTime | formatDate }}
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="150" label="推广方地址" prop="">
            <template slot-scope="{ row }">
              {{ row.provinceName }}{{ row.cityName }}{{ row.regionName }}
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </div>
</template>

<script>
import { ylChooseAddress } from '@/subject/admin/components'
import { queryListPage } from '@/subject/admin/api/zt_api/company';
import { createDownLoad } from '@/subject/admin/api/common'
export default {
  name: 'DownloadPromotion',
  components: {
    ylChooseAddress
  },
  data() {
    return {
      query: {
        promoterName: '',
        promoterId: '',
        time: [],
        provinceCode: '',
        cityCode: '',
        regionCode: '',
        total: 0,
        page: 1,
        limit: 10
      },
      dataList: [],
      loading: false
    }
  },
  activated() {
    this.getList()
  },
  methods: {
    async getList() {
      this.loading = true
      let query = this.query
      let data = await queryListPage(
        query.promoterName,
        query.promoterId,
        query.time && query.time.length > 0 ? query.time[0] : '',
        query.time && query.time.length > 1 ? query.time[1] : '',
        query.provinceCode,
        query.cityCode,
        query.regionCode,
        query.page,
        query.limit
      )
      if (data) {
        this.dataList = data.records
        this.query.total = data.total
      }
      this.loading = false
    },
    handleSearch() {
      this.query.page = 1;
      this.getList();
    },
    handleReset() {
      this.query = {
        promoterName: '',
        promoterId: '',
        time: [],
        provinceCode: '',
        cityCode: '',
        regionCode: '',
        total: 0,
        page: 1,
        limit: 10
      }
    },
    // 导出
    async downLoadTemp() {
      const query = this.query
      this.$common.showLoad()
      const data = await createDownLoad({
        className: 'enterprisePromotionDownloadRecordExportService',
        fileName: '下载推广查看导出',
        groupName: '下载推广查看导出',
        menuName: '企业账号-下载推广查看导出',
        searchConditionList: [
          {
            desc: '推广方名称',
            name: 'promoterName',
            value: query.promoterName || ''
          },
          {
            desc: '推广方ID',
            name: 'promoterId',
            value: query.promoterId || ''
          },
          {
            desc: '下载时间开始',
            name: 'startDownloadTime',
            value: query.time && query.time.length > 0 ? query.time[0] : ''
          },
          {
            desc: '下载时间结束',
            name: 'endDownloadTime',
            value: query.time && query.time.length > 1 ? query.time[1] : ''
          },
          {
            desc: '省份',
            name: 'provinceCode',
            value: query.provinceCode || ''
          },
          {
            desc: '城市',
            name: 'cityCode',
            value: query.cityCode || ''
          },
          {
            desc: '区县',
            name: 'regionCode',
            value: query.regionCode || ''
          }
        ]
      })
      this.$common.hideLoad()
      if (typeof data !== 'undefined') {
        this.$common.n_success('创建下载任务成功，请在下载中心查看')
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>