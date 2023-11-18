<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <div class="app-container-content has-bottom-bar">
      <div class="common-box">
        <div class="drug-agent-info">
          <div class="drug-agent-item">
            <div class="header-bar">
              <div class="sign"></div>
              药代基本信息
            </div>
            <div class="item-info mar-t-16">
              <el-row>
                <el-col :span="8">
                  <div class="item">
                    <div class="item-title">员工号：<span class="item-value">{{ detailData.baseInfo.code }}</span></div>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="item">
                    <div class="item-title">员工姓名：<span class="item-value">{{ detailData.baseInfo.name }}</span></div>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="item">
                    <div class="item-title">员工登录手机号：<span class="item-value">{{ detailData.baseInfo.mobile }}</span></div>
                  </div>
                </el-col>
              </el-row>
            </div>
          </div>
          <div class="drug-agent-item">
            <div class="header-bar">
              <div class="sign"></div>
              药代可售药品信息
            </div>
            <div class="item-info mar-t-16">
              <yl-table
                stripe
                :show-header="true"
                :list="detailData.salesGoodsInfoList">
                <el-table-column label="商品ID" min-width="100" align="center" prop="id"></el-table-column>
                <el-table-column label="药品名称" min-width="100" align="center" prop="name"></el-table-column>
                <el-table-column label="商品规格" min-width="100" align="center" prop="sellSpecifications"></el-table-column>
              </yl-table>
            </div>
          </div>
          <div class="drug-agent-item">
            <div class="header-bar">
              <div class="sign"></div>
              药代绑定医生信息
            </div>
            <div class="item-info mar-t-16">
              <yl-table
                stripe
                :show-header="true"
                :list="detailData.doctorInfoList">
                <el-table-column label="医生姓名" min-width="100" align="center" prop="name"></el-table-column>
                <el-table-column label="职称" min-width="100" align="center" prop="jobTitle"></el-table-column>
                <el-table-column label="医生手机号" min-width="100" align="center" prop="mobile"></el-table-column>
                <el-table-column label="医生证件号" min-width="100" align="center" prop="idNumber"></el-table-column>
                <el-table-column label="所在医疗机构" min-width="100" align="center" prop="orgName"></el-table-column>
                <el-table-column label="所在医疗机构科室" min-width="100" align="center" prop="deptName"></el-table-column>
                <el-table-column label="实名认证状态" min-width="100" align="center" prop="status"></el-table-column>
              </yl-table>
            </div>
          </div>
        </div>
      </div>
      <div class="flex-row-center bottom-bar-view">
        <yl-button plain type="primary" @click="$router.go(-1)">返回</yl-button>
      </div>
    </div>
  </div>
</template>

<script>
import { getDrugAgentDetail } from '@/subject/pop/api/cmp_api/drugAgent';

export default {
  name: 'DrugAgentDetail',
  components: {
  },
  computed: {
  },
  filters: {
  },
  mounted() {
    this.id = this.$route.params.id
    if (this.id) {
      this.getDetail(this.id)
    }
  },
  data() {
    return {
      // 头部导航
      navList: [
        {
          title: '首页',
          path: '/sale_dashboard'
        },
        {
          title: '药代管理',
          path: '/sale_drug_agent_manage/drug_agent_list'
        },
        {
          title: '药代详情'
        }
      ],
      detailData: {
        baseInfo: {},
        doctorInfoList: [],
        salesGoodsInfoList: []
      }
    }
  },
  methods: {
    async getDetail(id) {
      let data = await getDrugAgentDetail(id)
      if (data) {
        this.detailData = data
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
