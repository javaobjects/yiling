<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">人员名</div>
              <el-input v-model="query.name" @keyup.enter.native="searchEnter" placeholder="请输入人员姓名" />
            </el-col>
            <el-col :span="8">
              <div class="title">手机号</div>
              <el-input v-model="query.mobile" @keyup.enter.native="searchEnter" placeholder="请输入手机号" />
            </el-col>

          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn :total="total" @search="handleSearch" @reset="handleReset" />
            </el-col>
          </el-row>
        </div>
      </div>
      <!-- 导出、导入按钮 -->
      <div class="down-box clearfix">
        <div class="btn">
          <ylButton v-role-btn="['1']" type="primary" plain @click="add">添加</ylButton>
        </div>
      </div>
      <!-- table  -->
      <div class="my-table mar-t-8">
        <yl-table 
          :show-header="true" 
          stripe 
          :list="dataList" 
          :total="total" 
          :page.sync="query.page" 
          :limit.sync="query.limit" 
          :loading="loading" 
          @getList="getList">
          <el-table-column label="姓名" prop="name" align="center"></el-table-column>
          <el-table-column label="手机号" prop="mobile" align="center"></el-table-column>
          <el-table-column label="状态" prop="" align="center">
            <template slot-scope="{ row }">
              <div class="item">
                <div class="item-text font-size-base" v-if="row.status == 1"><span>启用</span></div>
                <div class="item-text font-size-base color-red" v-else-if="row.status == 2"><span>停用</span></div>
                <div v-else>--</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="添加时间" prop="createTime" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.createTime | formatDate }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="操作" prop="" align="center">
            <template slot-scope="{ row }">
              <div class="item">
                <yl-button v-role-btn="['2']" type="text" @click="showDetail(row)">查看二维码</yl-button>
                <yl-button v-role-btn="['3']" type="text">
                  <span v-if="row.showDrugWelfareButtonFlag" @click="benefitCodeClick(row)">药品福利计划二维码</span>
                </yl-button>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
      <!-- 库存弹窗 -->
      <yl-dialog 
        title="二维码" 
        width="410px" 
        :visible.sync="show1" 
        @confirm="submit" 
        :right-btn-name="'下载'"
        >
        <div class="dia-content2 flex-row-center">
          <div class="img-box">
            <img :src="qrCodeUrl" alt="">
          </div>
        </div>
      </yl-dialog>
      <!-- 药品福利计划二维码 -->
      <yl-dialog 
        title="用药福利计划的二维码" 
        :width="dialogList && dialogList.length > 1 ? '840px' : '410px'" 
        :visible.sync="show2" 
        @confirm="downloadClick" 
        :right-btn-name="'下载'"
        >
        <div class="dia-content2 dia-content-code" v-for="(item, index) in dialogList" :key="index">
          <div class="img-box">
            <img :src="item.url" alt="">
          </div>
          <div class="qrCode-conter">
            <p>{{ item.name }}</p>
            <p>{{ item.sellSpecifications }}</p>
          </div>
        </div>
      </yl-dialog>
    </div>
  </div>
</template>

<script>
import { getStaffList, getQrcode, getDrugWelfareQrcode } from '@/subject/pop/api/cmp_api/adminManager'
import { handleSelect } from '@/subject/pop/utils/index';
export default {
  name: 'CmpStaffManager',
  components: {
  },
  computed: {
  },
  data() {
    return {
      // 头部导航
      navList: [
        {
          title: '首页',
          path: ''
        },
        {
          title: '员工管理',
          path: ''
        },
        {
          title: '销售人员管理'
        }
      ],
      query: {
        page: 1,
        limit: 10,
        name: '',
        mobile: ''
      },
      loadingName: false,
      optionsName: [],
      dataList: [],
      QRCodeId: '',
      loading: false,
      total: 0,
      show1: false,
      qrCodeUrl: '',
      show2: false,
      dialogList: []
    }
  },
  activated() {
    this.getList()
  },
  created() {
  },
  mounted() {
  },
  methods: {
    // Enter
    searchEnter(e) {
      const keyCode = window.event ? e.keyCode : e.which;
      if (keyCode === 13) {
        this.getList()
      }
    },
    //  获取商品列表
    async getList() {
      this.loading = true
      let query = this.query
      let data = await getStaffList(
        query.page,
        query.limit,
        query.mobile,
        query.name
      )
      this.loading = false
      console.log(data);
      if (data !== undefined) {
        this.dataList = data.records
        this.total = data.total
      }
    },

    handleSearch() {
      this.query.page = 1
      this.query.size = 20
      this.getList()
    },
    handleReset() {
      this.query = {
        page: 1,
        limit: 10,
        name: '',
        mobile: ''
      }
    },
    remoteMethod(query) {
      if (query !== '') {
        this.loadingName = true;
        setTimeout(() => {
          this.loadingName = false;
          this.optionsName = [
            { label: '李嘻嘻', value: 1 }
          ]
        }, 200);
      } else {
        this.optionsName = [];
      }
    },
    // 下载模板
    async downLoadTemp() {
      let query = this.query
      if (this.dataList.length == 0) return this.$message.warning('请搜索数据再导出')
      this.$common.showLoad()
      let data = await createDownLoad({
        className: 'flowSalePageListExportService',
        fileName: '销售流向导出',
        groupName: 'erp流向',
        menuName: 'POP管理-erp流向',
        searchConditionList: [
          {
            desc: '订单编号',
            name: 'ename',
            value: query.orderNo || ''
          },
          {
            desc: '配送状态',
            name: 'ename',
            value: query.status || ''
          },
          {
            desc: '下单时间-开始',
            name: 'startTime',
            value: query.createTime && query.createTime.length > 1 ? query.createTime[0] : ''
          }, {
            desc: '下单时间-结束',
            name: 'endTime',
            value: query.createTime && query.createTime.length > 1 ? query.createTime[1] : ''
          }
        ]

      })
      this.$common.hideLoad()
      if (data && data.result) {
        this.$common.n_success('创建下载任务成功，请在下载中心查看')
      }
    },
    // 查看二维码 获取二维码链接展示
    async showDetail(e) {
      this.qrCodeUrl = ''
      let data = await getQrcode(e.id)
      console.log(data);
      if (data !== undefined) {
        this.qrCodeUrl = data.url
        this.show1 = true
      }
    },
    edit(e) {
      this.$router.push(`/cmp_order/cmp_choice_send_goods/${e.id}/1`)
    },
    // 添加员工
    add() {
      handleSelect(5, '/zt_roles/roles_user');
    },

    // 下载二维码
    submit() {
      var alink = document.createElement('a');
      alink.target = '_self';
      alink.href = this.qrCodeUrl;
      alink.download = '二维码'; //图片名
      alink.click();
    },
    // 药品福利计划二维码
    async benefitCodeClick(row) {
      this.$common.showLoad();
      let data = await getDrugWelfareQrcode(row.id)
      this.$common.hideLoad();
      if (data !== undefined) {
        this.dialogList = data;
        this.show2 = true;
      }
    },
    // 下载福利计划二维码
    downloadClick() {
      for (let i = 0; i < this.dialogList.length; i ++ ) {
        let a = document.createElement('a');
        a.href = this.dialogList[i].url;
        // a.target = '_blank';
        a.download = this.dialogList[i].name;
        a.click();
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
