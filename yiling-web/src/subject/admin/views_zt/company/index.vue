<template>
  <div class="app-container">
    <div class="app-container-content">
      <el-collapse value="1">
        <el-collapse-item title="企业终端数量" name="1">
          <div class="header-box common-box">
            <div class="flex-row-left">
              <div class="flex-row-left flex1 border-1px-r">
                <div class="big-box flex-column-center">
                  <div class="num">{{ totalData.totalCount | toThousand }}</div>
                  <div class="title">企业总数（家)</div>
                </div>
                <div class="flex-column-top">
                  <div class="flex-row-left">
                    <div class="small-box flex-column-center">
                      <div class="num">{{ totalData.terminalCount | toThousand }}</div>
                      <div class="title">终端</div>
                    </div>
                    <div class="small-box flex-column-center">
                      <div class="num">{{ totalData.businessCount | toThousand }}</div>
                      <div class="title">商业</div>
                    </div>
                    <div class="small-box flex-column-center">
                      <div class="num">{{ totalData.industryCount | toThousand }}</div>
                      <div class="title">工业</div>
                    </div>
                  </div>
                  <div class="flex-row-left nex-box">
                    <div class="box1 flex1">
                      <div>启用：{{ totalData.enabledCount | toThousand }}</div>
                    </div>
                    <div class="box2 flex1">
                      <div>停用：{{ totalData.disabledCount | toThousand }}</div>
                    </div>
                  </div>
                </div>
              </div>
              <div class="flex-row-right flex1">
                <div class="big-box flex-column-center">
                  <div class="num">{{ totalData.terminalCount | toThousand }}</div>
                  <div class="title">终端总数（家)</div>
                </div>
                <div class="flex-column-top">
                  <div class="flex-row-left">
                    <div class="small-box flex-column-center">
                      <div class="num">{{ totalData.pharmacyCount | toThousand }}</div>
                      <div class="title">单体药店</div>
                    </div>
                    <div class="small-box flex-column-center">
                      <div class="num">{{ totalData.clinicCount | toThousand }}</div>
                      <div class="title">诊所</div>
                    </div>
                    <!-- <div class="small-box flex-column-center">
                      <div class="num">{{ totalData.hospitalCount | toThousand }}</div>
                      <div class="title">医院</div>
                    </div> -->
                    <div class="small-box flex-column-center">
                      <div class="num">{{ totalData.hospitalCount | toThousand }}</div>
                      <div class="title">医疗机构</div>
                    </div>

                    <!-- <div class="small-box flex-column-center">
                      <div class="num">{{ totalData.privateHospitalCount | toThousand }}</div>
                      <div class="title">民营医院</div>
                    </div>
                    <div class="small-box flex-column-center">
                      <div class="num">{{ totalData.level3HospitalCount | toThousand }}</div>
                      <div class="title">三级医院</div>
                    </div>
                    <div class="small-box flex-column-center">
                      <div class="num">{{ totalData.level2HospitalCount | toThousand }}</div>
                      <div class="title">二级医院</div>
                    </div>
                    <div class="small-box flex-column-center">
                      <div class="num">{{ totalData.communityCenterCount | toThousand }}</div>
                      <div class="title">社区中心</div>
                    </div> -->
                  </div>
                  <div class="flex-row-left box-2">
                    <!-- <div class="small-box flex-column-center">
                      <div class="num">{{ totalData.healthCenterCount | toThousand }}</div>
                      <div class="title">县镇卫生院</div>
                    </div>
                    <div class="small-box flex-column-center">
                      <div class="num">{{ totalData.communityStationCount | toThousand }}</div>
                      <div class="title" style="font-size:12px">社区站/村卫生所</div>
                    </div>
                    <div class="small-box flex-column-center">
                      <div class="num">{{ totalData.peopleHospitalCount | toThousand }}</div>
                      <div class="title" style="font-size:12px">县人民/中医院</div>
                    </div> -->
                    <div class="small-box flex-column-center">
                      <div class="num">{{ totalData.chainBaseCount | toThousand }}</div>
                      <div class="title">连锁总店</div>
                    </div>
                    <div class="small-box flex-column-center">
                      <div class="num">{{ totalData.chainJoinCount | toThousand }}</div>
                      <div class="title">连锁加盟</div>
                    </div>
                    <div class="small-box flex-column-center">
                      <div class="num">{{ totalData.chainDirectCount | toThousand }}</div>
                      <div class="title">连锁直营</div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </el-collapse-item>
      </el-collapse>
      <div class="down-box">
        <div class="btn">
          <yl-button v-role-btn="['1']" class="mar-r-10" @click="importClick('importUpdateManagerAccount')" type="text">导入修改管理员账号数据</yl-button>
          <yl-button v-role-btn="['2']" @click="importClick('importEnterprise')" type="text">导入企业</yl-button>
          <yl-button v-role-btn="['3']" @click="importClick('importEnterprisePlatform')" type="text">导入产品线</yl-button>
        </div>
      </div>

      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">企业名称</div>
              <el-input v-model="query.name" class="mar-r-16 flex1" @keyup.enter.native="searchEnter" placeholder="企业名称" />
            </el-col>
            <el-col :span="8">
              <div class="title">社会信用统一代码/医疗机构执业许可证</div>
              <el-input v-model="query.licenseNumber" class="mar-r-16 flex1" @keyup.enter.native="searchEnter" placeholder="社会信用统一代码/医疗机构执业许可证" />
            </el-col>
            <el-col :span="10">
              <div class="title">联系人手机号</div>
              <el-input v-model="query.contactorPhone" class="mar-r-16 flex1" @keyup.enter.native="searchEnter" placeholder="联系人手机号" />
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">企业ID</div>
              <el-input v-model="query.id" type="number" class="mar-r-16 flex1" @keyup.enter.native="searchEnter" placeholder="企业ID" />
            </el-col>
            <el-col :span="8">
              <div class="title">企业类型</div>
              <el-select class="select-width" v-model="query.type" placeholder="请选择">
                <el-option :key="0" label="全部" :value="0"></el-option>
                <el-option v-for="itm in companyType" :key="itm.value" :label="itm.label" :value="itm.value">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="10">
              <div class="title">企业所属区域</div>
              <yl-choose-address width="230px" :province.sync="query.provinceCode" :city.sync="query.cityCode" :area.sync="query.regionCode" is-all />
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">账号状态</div>
              <el-radio-group v-model="query.status">
                <el-radio :label="0">全部</el-radio>
                <el-radio :label="1">启用</el-radio>
                <el-radio :label="2">停用</el-radio>
              </el-radio-group>
            </el-col>
            <el-col :span="8">
              <div class="title">认证状态</div>
              <el-radio-group v-model="query.authStatus">
                <el-radio :label="0">全部</el-radio>
                <el-radio v-for="(itm, idx) in companyStatus" :key="idx" :label="itm.value">
                  {{ itm.label }}
                </el-radio>
              </el-radio-group>
            </el-col>
            <el-col :span="10">
              <div class="title">ERP对接级别</div>
              <el-radio-group v-model="query.erpSyncLevel">
                <el-radio :label="''">全部</el-radio>
                <el-radio v-for="(itm, idx) in companyErpSyncLevel" :key="idx" :label="itm.value">
                  {{ itm.label }}
                </el-radio>
              </el-radio-group>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box  company-checkbox-group">
            <el-col :span="24">
              <div class="title">标签</div>
              <div class="checkbox-group">
                <el-checkbox-group v-model="query.tagIds" size="mini">
                  <!-- <el-checkbox :label="0" border>不限</el-checkbox> -->
                  <el-checkbox border v-for="(itm, idx) in labelData" :key="idx" :label="itm.id">
                    {{ itm.name }}
                  </el-checkbox>
                </el-checkbox-group>
              </div>
            </el-col>
          </el-row>
        </div>

        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="24">
              <yl-search-btn :total="total" @search="handleSearch" @reset="handleReset" />
            </el-col>
          </el-row>
        </div>
      </div>
      <div class="mar-t-10">
        <yl-table :list="dataList" :total="total" :cell-class-name="() => 'border-1px-b'" :page.sync="query.page" :limit.sync="query.limit" :loading="loading" @getList="getList">
          <el-table-column>
            <template slot-scope="{ row, $index }">
              <div class="table-view" :key="$index">
                <div class="session font-size-base">
                  <span>账户状态：<span :class="row.enterpriseInfo.status === 1 ? 'col-down' : 'col-up'">{{ row.enterpriseInfo.status | enable }}</span></span>
                  <span class="erp-djjb">
                    ERP对接级别：{{ row.enterpriseInfo.erpSyncLevel | dictLabel(companyErpSyncLevel) }}
                  </span>
                  <div class="user-label" v-if="row.tagNames[0] != undefined">用户标签:
                    <p v-for="(item,index) in row.tagNames" :key="index">
                      <el-tag type="info">{{ item }}</el-tag>
                    </p>
                  </div>
                  <span>创建时间：{{ row.enterpriseInfo.createTime | formatDate }}</span>
                </div>
                <div class="content flex-row-left">
                  <div class="table-item flex1">
                    <div class="item font-size-lg pad-tb-10 bold">{{ row.enterpriseInfo.name }}</div>
                    <div class="item font-size-base font-important-color"><span class="font-title-color">联系人姓名：</span>{{ row.enterpriseInfo.contactor }}</div>
                    <div class="item font-size-base font-important-color"><span class="font-title-color">联系人电话：</span>{{ row.enterpriseInfo.contactorPhone }}</div>
                    <div class="item font-size-base font-important-color"><span class="font-title-color">企业ID：</span>{{ row.enterpriseInfo.id }}</div>
                    <div class="item font-size-base font-important-color"><span class="font-title-color">企业类型：</span>{{ row.enterpriseInfo.type | dictLabel(companyType) }}</div>
                    <div class="item font-size-base font-important-color"><span class="font-title-color">企业地址：</span>{{ row.enterpriseInfo.provinceName }}{{ row.enterpriseInfo.cityName }}{{ row.enterpriseInfo.regionName }}{{ row.enterpriseInfo.address }}</div>
                  </div>
                  <div class="table-item flex1">
                    <div class="item font-size-base font-important-color"><span class="font-title-color">账号数量：</span>{{ row.accountNum }}</div>
                    <div class="item font-size-base font-important-color"><span class="font-title-color">{{ row.enterpriseInfo.type == 7 ? '医疗机构：' : '社会统一信用代码：' }}</span>{{ row.enterpriseInfo.licenseNumber }}</div>
                    <div class="item font-size-base font-important-color" v-if="row.enterpriseInfo.type == 1 || row.enterpriseInfo.type == 2"><span class="font-title-color">是否开通商城：</span>{{ row.platformInfo.mallFlag | yes }}</div>
                    <div class="item font-size-base font-important-color" v-if="row.enterpriseInfo.type == 1 || row.enterpriseInfo.type == 2"><span class="font-title-color">是否开通销售助手：</span>{{ row.platformInfo.salesAssistFlag | yes }}</div>
                    <div class="item font-size-base font-important-color" v-if="row.enterpriseInfo.type == 1 || row.enterpriseInfo.type == 2"><span class="font-title-color">是否开POP：</span>{{ row.platformInfo.popFlag | yes }}</div>
                  </div>
                  <div class="table-item flex1">
                    <div class="item font-size-base font-important-color"><span class="font-title-color">最后审核时间：</span>{{ row.enterpriseInfo.authTime | formatDate }}</div>
                    <div class="item font-size-base font-important-color"><span class="font-title-color">最后审核人：</span>{{ row.enterpriseInfo.authUserName }}</div>
                    <div class="item font-size-base font-important-color"><span class="font-title-color">最后修改时间：</span>{{ row.enterpriseInfo.updateTime | formatDate }}</div>
                    <div class="item font-size-base font-important-color"><span class="font-title-color">最后修改人：</span>{{ row.enterpriseInfo.updateUserName }}</div>
                  </div>
                  <div class="flex-column-center">
                    <div>
                      <yl-button type="text" @click="showDetail(row, $index)">编辑</yl-button>
                    </div>
                    <div>
                      <yl-button type="text" @click="editLabel(row, $index)">编辑标签</yl-button>
                    </div>
                    <div v-if="row.enterpriseInfo">
                      <yl-status url="/dataCenter/api/v1/enterprise/updateStatus" :stop-msg="`<span>停用企业可能存在未完结的订单，请和相关部门沟通后进行操作；<br>停用后该企业所有员工将不能登录该企业；<br>确认是否停用该企业：${row.enterpriseInfo.name}？</span>`" :is-html="true" :status="row.enterpriseInfo.status" status-key="status" :data="{id: row.enterpriseInfo.id}" @change="getList(getTotal)">
                      </yl-status>
                    </div>
                  </div>
                </div>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
    <yl-dialog title="编辑标签" @confirm="confirm" width="570px" :visible.sync="showDialog">
      <div class="period-form company-checkbox-group">
        <el-checkbox-group v-model="editLabelList" size="mini">
          <el-checkbox border v-for="(itm, idx) in labelData" :key="idx" :label="itm.id">
            {{ itm.name }}
          </el-checkbox>
        </el-checkbox-group>
      </div>
    </yl-dialog>
    <!-- excel导入 -->
    <import-send-dialog :visible.sync="importSendVisible" :excel-code="info.excelCode" ref="importSendRef"></import-send-dialog>
  </div>
</template>

<script>
// import { getCompanyTotalInfo, getCompanyList } from '@/subject/admin/api/company'
import { pageList, quantityStatistics, options, listByEid, saveEnterpriseTags } from '@/subject/admin/api/zt_api/company';
import { enterpriseType, enterpriseAuthStatus, erpSyncLevel } from '@/subject/admin/utils/busi'
import { ylChooseAddress, ylStatus } from '@/subject/admin/components'
import ImportSendDialog from '@/subject/admin/components/ImportSendDialog'

export default {
  name: 'ZtCompanyIndex',
  components: {
    ylChooseAddress,
    ylStatus,
    ImportSendDialog
  },
  computed: {
    companyType() {
      return enterpriseType()
    },
    companyStatus() {
      return enterpriseAuthStatus()
    },
    companyErpSyncLevel() {
      return erpSyncLevel()
    }
  },
  data() {
    return {
      query: {
        name: '',
        licenseNumber: '',
        contactorPhone: '',
        id: '',
        status: 0,
        type: 0,
        authStatus: 0,
        erpSyncLevel: '',
        provinceCode: '',
        cityCode: '',
        regionCode: '',
        tagIds: [],
        page: 1,
        limit: 10
      },
      totalData: {},
      dataList: [],
      loading: false,
      total: 0,
      labelData: [],
      showDialog: false, //编辑标签弹窗
      editLabelList: [], //存储编辑标签 与企业建立关联关系
      qyId: '', //缓存 当前点击 编辑标签 的企业id
      // 导入
      importSendVisible: false,
      // 导入任务参数 excelCode: importUpdateManagerAccount-导入修改管理员账号数据 importEnterprise-企业数据导入 importEnterprisePlatform-企业产品线数据导入 
      info: {
        excelCode: ''
      }
    }
  },
  activated() {
    this.getTables();
    this.getTotal();
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
    async getTables() {
      let data = await options();
      this.labelData = data.list;

    },
    async getTotal() {
      this.totalData = await quantityStatistics()
    },
    async getList(call) {
      this.loading = true
      let query = this.query
      let data = await pageList(
        query.authStatus,
        query.cityCode,
        query.tagIds,
        query.contactorPhone,
        query.page,
        query.id,
        query.licenseNumber,
        query.name,
        query.provinceCode,
        query.regionCode,
        query.limit,
        query.status,
        query.type,
        query.erpSyncLevel
      )
      this.$log(data)
      this.loading = false
      if (data) {
        this.dataList = data.records
        this.total = data.total
      }
      if (call) {
        call()
      }
    },
    handleSearch() {
      this.query.page = 1
      this.getList()
    },
    handleReset() {
      this.query = Object.assign({}, {
        name: '',
        licenseNumber: '',
        contactorPhone: '',
        id: '',
        status: 0,
        type: 0,
        authStatus: 0,
        provinceCode: '',
        cityCode: '',
        regionCode: '',
        tagIds: [],
        page: 1,
        limit: 10,
        erpSyncLevel: ''
      })
    },
    // excel导入
    importClick(excelCode) {
      this.info.excelCode = excelCode
      this.importSendVisible = true
      this.$nextTick( () => {
        this.$refs.importSendRef.init()
      })
    },
    // 编辑标签
    async editLabel(row, index) {
      this.editLabelList = [];
      this.qyId = row.enterpriseInfo.id;
      this.showDialog = true;
      this.$common.showLoad();
      let data = await listByEid(row.enterpriseInfo.id)
      this.$common.hideLoad();
      if (data !== undefined) {
        for (let i = 0; i < data.list.length; i++) {
          if (data.list[i].checked == true) {
            this.editLabelList.push(data.list[i].id);
          }
        }
      }
    },
    async confirm() {
      this.$common.showLoad();
      let data = await saveEnterpriseTags(this.qyId, this.editLabelList)
      this.$common.hideLoad();
      if (data !== undefined) {
        this.$common.n_success('保存成功!');
        this.showDialog = false;
        this.getList();
      }
      // }
    },
    // 编辑
    showDetail(row, index) {
      this.$router.push('/zt_company/zt_company_detail/' + row.enterpriseInfo.id)
    }
  }
}
</script>
<style lang="scss" scoped>
@import './index.scss';
.company-checkbox-group ::v-deep .el-checkbox__input {
  display: none;
}
.company-checkbox-group ::v-deep .el-checkbox-group {
  margin-top: 6px;
}
.company-checkbox-group ::v-deep .el-checkbox {
  margin-right: 10px;
  margin-bottom: 10px;
}
.company-checkbox-group ::v-deep .el-checkbox.is-bordered + .el-checkbox.is-bordered {
  margin-left: 0;
}
</style>
