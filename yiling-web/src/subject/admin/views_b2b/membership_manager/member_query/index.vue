<template>
  <div class="app-container">
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">企业名称</div>
              <el-input v-model="query.ename" @keyup.enter.native="searchEnter" placeholder="请输入企业名称" />
            </el-col>
            <el-col :span="8">
              <div class="title">企业ID</div>
              <el-input v-model="query.eid" @keyup.enter.native="searchEnter" placeholder="请输入企业ID" />
            </el-col>
            <el-col :span="8">
              <div class="title">统一社会信用代码/医疗机构执业许可证</div>
              <el-input v-model.trim="query.licenseNumber" @keyup.enter.native="searchEnter" placeholder="请输入统一社会信用代码/医疗机构执业许可证" />
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">联系人手机号</div>
              <el-input v-model="query.contactorPhone" @keyup.enter.native="searchEnter" placeholder="请输入联系人手机号" />
            </el-col>
            <el-col :span="8">
              <div class="title">联系人姓名</div>
              <el-input v-model="query.contactor" @keyup.enter.native="searchEnter" placeholder="请输入联系人姓名" />
            </el-col>
            <el-col :span="8">
              <div class="title">企业类型</div>
              <el-select class="select-width" v-model="query.type" placeholder="请选择">
                <el-option :key="0" label="全部" :value="0"></el-option>
                <el-option v-for="item in companyType" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">会员状态</div>
              <el-select class="select-width" v-model="query.status" placeholder="请选择">
                <el-option 
                  v-for="item in memberTypeData" 
                  :key="item.value" 
                  :label="item.label" 
                  :value="item.value">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="8">
              <div class="title">企业地址</div>
              <yl-choose-address 
                width="230px" 
                :province.sync="query.provinceCode" 
                :city.sync="query.cityCode" 
                :area.sync="query.regionCode" 
                is-all />
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="24">
              <div class="title">会员卡名称</div>
              <el-checkbox-group v-model="query.memberIdList">
                <el-checkbox 
                  class="option-class" 
                  v-for="item in tagList" 
                  :key="item.id" 
                  :label="item.id" >
                  {{ item.name }}(ID：{{ item.id }})
                </el-checkbox>
              </el-checkbox-group >
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
      <!-- 批量导入会员 -->
      <div class="down-box clearfix">
        <div class="btn">
          <el-link class="mar-r-10" type="primary" :underline="false" :href="'NO_18' | template" >
            下载模板
          </el-link> 
          <yl-button type="primary" plain @click="importClick">批量赠送会员</yl-button>
        </div>
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
          <el-table-column align="center" min-width="80" label="企业ID" prop="eid">
          </el-table-column>
          <el-table-column align="center" min-width="120" label="企业名称" prop="ename">
          </el-table-column>
          <el-table-column align="center" min-width="80" label="企业类型">
            <template slot-scope="{ row }">
              <span>{{ row.type | dictLabel(companyType) }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="100" label="唯一代码" prop="licenseNumber">
          </el-table-column>
          <el-table-column align="center" min-width="120" label="企业地址">
            <template slot-scope="{ row }">
              <span>{{ row.completeAddress }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="120" label="联系人信息">
            <template slot-scope="{ row }">
              <div>{{ row.contactor }}</div>
              <span>{{ row.contactorPhone }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="120" label="开通会员信息">
            <template slot-scope="{ row }">
              <span>
                {{ row.memberName }}
                <span v-if="row.memberId">【ID：{{ row.memberId }}】</span>
              </span>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="80" label="会员状态">
            <template slot-scope="{ row }">
              <span>{{ row.status == 1 ? '正常' : '过期' }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="160" label="最后操作信息">
            <template slot-scope="{ row }">
              <div>操作人：{{ row.updateUserName }}</div>
              <span>操作时间：{{ row.updateTime | formatDate }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" label="操作" width="110">
            <template slot-scope="{ row }">
              <div>
                <yl-button type="text" @click="viewDetailsClick(row)">查看详情</yl-button>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
    <!-- 批量赠送会员导入 -->
    <import-send-dialog :visible.sync="importSendVisible" :excel-code="excelCode" ref="importSendRef"></import-send-dialog>
  </div>
</template>

<script>
  import { enterpriseType } from '@/subject/admin/utils/busi'
  import { ylChooseAddress } from '@/subject/admin/components'
  import { queryEnterpriseMemberPage, getMemberList } from '@/subject/admin/api/b2b_api/membership'
  import ImportSendDialog from '@/subject/admin/components/ImportSendDialog'
  export default {
    name: 'MemberQuery',
    components: {
      ylChooseAddress,
      ImportSendDialog
    },
    computed: {
      companyType() {
        return enterpriseType()
      }
    },
    data() {
      return {
        query: {
          ename: '',
          eid: '',
          licenseNumber: '',
          contactorPhone: '',
          contactor: '',
          type: '',
          status: '0',
          provinceCode: '',
          cityCode: '',
          regionCode: '',
          memberIdList: [],
          page: 1,
          limit: 10,
          total: 0
        },
        dataList: [],
        // 标签列表
        tagList: [],
        loading: false,
        // 会员状态
        memberTypeData: [
          {
            label: '全部',
            value: '0'
          },
          {
            label: '正常',
            value: '1'
          },
          {
            label: '过期',
            value: '2'
          }
        ],
        //是否显示导入会员
        importSendVisible: false,
        excelCode: 'importOpenMember'
      }
    },
    activated() {
      // 获取列表数据
      this.getList()
      // 获取商业标签
      this.labelData()
    },
    methods: {
      // Enter
      searchEnter(e) {
        const keyCode = window.event ? e.keyCode : e.which;
        if (keyCode === 13) {
          this.getList()
        }
      },
      // 获取标签数据
      async labelData() {
        let data = await getMemberList()
        if (data) {
          this.tagList = data.list;
        }
      },
      async getList() {
        this.loading = true;
        let query = this.query;
        let data = await queryEnterpriseMemberPage(
          query.ename,
          query.eid,
          query.licenseNumber,
          query.contactorPhone,
          query.contactor,
          query.type,
          query.status,
          query.provinceCode,
          query.cityCode,
          query.regionCode,
          query.memberIdList,
          query.page,
          query.limit
        )
        if (data) {
          this.dataList = data.records
          this.query.total = data.total
        }
        this.loading = false
      },
      // 点击查看详情
      viewDetailsClick(row) {
        this.$router.push({
          name: 'MemberQueryDetails',
          params: { 
            id: row.id 
          }
        })
      },
      handleSearch() {
        this.query.page = 1
        this.getList()
      },
      handleReset() {
        this.query = {
          ename: '',
          eid: '',
          licenseNumber: '',
          contactorPhone: '',
          contactor: '',
          type: '',
          status: '0',
          provinceCode: '',
          cityCode: '',
          regionCode: '',
          memberIdList: [],
          page: 1,
          limit: 10,
          total: 0
        }
      },
      // 导入会员
      importClick() {
        this.importSendVisible = true
        this.$nextTick( () => {
          this.$refs.importSendRef.init()
        })
        // this.$router.push({
        //   path: '/importFile/importFile_index',
        //   query: {
        //     action: '/b2b/api/v1/member/importOpenMember'
        //   }
        // })
      }
    }
  }
</script>

<style lang="scss" scoped>
  @import './index.scss';
</style>

