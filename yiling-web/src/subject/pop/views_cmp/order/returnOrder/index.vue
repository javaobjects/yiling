<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <div class="app-container-content">
      <!-- table  -->
      <div class="my-table">
        <div class="header-bar mar-b-10 font-600">
          <div class="sign"></div>已退单完成
        </div>
        <yl-table :show-header="true" stripe border :list="dataList" :total="total" :page.sync="query.page" :limit.sync="query.limit" :loading="loading" @getList="getList">
          <el-table-column label="退单号" align="center">
            <template slot-scope="{ row }">
              <div>{{ row.returnNo }}</div>
            </template>
          </el-table-column>
          <el-table-column label="退款申请金额" align="center">
            <template slot-scope="{ row }">
              <div>{{ row.returnAmount }}</div>
            </template>
          </el-table-column>
          <el-table-column label="药品服务终端" align="center">
            <template slot-scope="{ row }">
              <div>{{ row.ename }}</div>
            </template>
          </el-table-column>
          <el-table-column label="处理时间" align="center">
            <template slot-scope="{ row }">
              <div>{{ row.updateTime | formatDate }}</div>
            </template>
          </el-table-column>
          <el-table-column label="申请时间" align="center">
            <template slot-scope="{ row }">
              <div>{{ row.createTime | formatDate }}</div>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center">
            <template slot-scope="{ row }">
              <div>
                <yl-button v-role-btn="['1']" type="text" @click="showDetail(row)">详情</yl-button>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>

      <!-- 详情弹窗 -->
      <yl-dialog title="退单详情" width="878px" :show-footer="false" :visible.sync="show">
        <div class="dia-content">
          <el-row :gutter="24" class="mar-b-16">
            <el-col :span="8">
              <div class="font-size-base font-title-color">平台退单编号：<span class="font-important-color">{{ dataInfo.returnNo }}</span></div>
            </el-col>
            <el-col :span="8">
              <div class="font-size-base font-title-color">退款单联系人信息：<span class="font-important-color">{{ dataInfo.receiveUserName }}</span></div>
            </el-col>
          </el-row>
          <el-row :gutter="24" class="mar-b-8">
            <el-col :span="12" class="font-size-lg font-important-color bold dia-title">
              退货的商品订单来自{{ dataInfo.orderNo }}
            </el-col>
            <el-col :span="12">
              <yl-button type="text" @click="goDetail">查看订单详情</yl-button>
            </el-col>
          </el-row>
          <div class="mar-b-16">
            <yl-table :show-header="true" border stripe :list="goodsList">
              <el-table-column label="商品名称" align="center" prop="goodsName"></el-table-column>
              <el-table-column label="规格" align="center" prop="goodsSpecifications"></el-table-column>
              <el-table-column label="退药数量" align="center" prop="returnQuality"></el-table-column>
              <el-table-column label="单价" align="center" prop="returnPrice"></el-table-column>
              <el-table-column label="退单合计价格" align="center" prop="goodsAmount"></el-table-column>
            </yl-table>
          </div>
          <div class="flex-between font-important-color bold mar-b-10">
            <div></div>
            <div>合计：{{ dataInfo.returnAmount }}元</div>
          </div>
          <div class="header-bar mar-b-10 font-600">
            <div class="sign"></div>日志明细
          </div>
          <div class="mar-b-32 pad-r-33">
            <yl-table :show-header="true" border stripe :list="logList">
              <el-table-column label="操作者" align="center" prop="operateUserName"></el-table-column>
              <el-table-column label="意见内容" align="center" prop="remark"></el-table-column>
              <el-table-column label="时间" align="center">
                <template slot-scope="{ row }">
                  {{ row.operateTime | formatDate }}
                </template>
              </el-table-column>
            </yl-table>
          </div>
        </div>
      </yl-dialog>
    </div>
  </div>
</template>

<script>
import { getReturnList, getReturnOrderDetail } from '@/subject/pop/api/cmp_api/order'
import { goodsStatus, goodsOutReason } from '@/subject/pop/utils/busi'
export default {
  name: 'CmpReturnOrder',
  components: {
  },
  computed: {
    goodsReason() {
      return goodsOutReason()
    },
    goodStatus() {
      return goodsStatus()
    }
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
          title: '订单管理',
          path: ''
        },
        {
          title: '退货退单列表'
        }
      ],
      query: {
        page: 1,
        limit: 20,
        orderNo: '',
        createTime: [],
        // 全部，已上架，已下架 ，带设置
        status: 0
      },
      dataList: [
        {}
      ],
      loading: false,
      total: 0,
      show: false,
      dataInfo: {},
      logList: [],
      goodsList: []

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
    //  获取商品列表
    async getList() {
      this.loading = true
      let query = this.query
      let data = await getReturnList(
        query.page,
        query.limit
      )
      this.loading = false
      if (data) {
        this.dataList = data.records
        this.total = data.total
      }
    },
    showDetail(row) {
      this.show = true;
      this.getDetail(row.id)
    },
    // 获取详情
    async getDetail(id) {
      this.$common.showLoad();
      let data = await getReturnOrderDetail(id)
      this.$common.hideLoad();
      if (data !== undefined) {
        this.dataInfo = data
        this.logList = data.orderLogList
        this.goodsList = data.orderReturnDetailList
      }
    },
    goDetail() {
      this.show = false;
      // id/类型  3 代表退款订单列表，跳转到详情页面
      this.$router.push({
        name: 'CmpOrderDetail',
        params: {
          id: this.dataInfo.orderId,
          type: 3
        }
      });
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
