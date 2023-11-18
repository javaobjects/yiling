<template>
  <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <div class="common-box">
        <div class="header-bar">
          <div class="sign"></div>
          流向核对结果
        </div>
        <div class="examine-row">
          <div class="examine-row-conter">{{ dataList.result | dictLabel(auditStatusList) }}</div>
        </div>
      </div>
      <div class="common-box">
        <div class="header-bar">
          <div class="sign"></div>
          ERP流向数据
        </div>
        <div class="examine-row">
          <el-row>
            <el-col :span="24">
              <div class="examine-row-conter">
                <!-- <span class="erp-info">订单编号：{{ dataList.result !== 4 ? dataList.docCode : '- -' }}</span> -->
                <span class="erp-info">发货日期：{{ dataList.erpDeliveryTime | formatDate('yyyy-MM-dd') }}</span>
                <span class="erp-info">发货单位：{{ dataList.distributorEname || '- -' }}</span>
                <span class="erp-info">收货单位：{{ dataList.recvEname || '- -' }}</span>
              </div>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <div class="erp-box" v-if="dataList && dataList.erpGoodsList">
                <p class="title">商品信息：</p>
                <yl-table :list="dataList.erpGoodsList" border :show-header="true">
                  <el-table-column align="center" label="商品名称" prop="ylGoodsName"></el-table-column>
                  <el-table-column align="center" width="120" label="规格" prop="ylGoodsSpecifications"></el-table-column>
                  <el-table-column align="center" width="120" label=" 数量" prop="quantity"></el-table-column>
                </yl-table>
              </div>
            </el-col>
          </el-row>
              
        </div>
      </div>
      <div class="common-box">
        <div class="header-bar">
          <div class="sign"></div>
          CRM流向数据
        </div>
        <div class="examine-row" v-if="dataList.result !== 2">
          <el-row>
            <el-col :span="24">
              <div class="examine-row-conter">
                <span class="erp-info">发货日期：{{ dataList.crmDeliveryTime | formatDate('yyyy-MM-dd') }}</span>
                <span class="erp-info">发货单位：{{ dataList.distributorEname || '- -' }}</span>
                <span class="erp-info">收货单位：{{ dataList.recvEname || '- -' }}</span>
              </div>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <div class="erp-box" v-if="dataList && dataList.crmGoodsList">
                <p class="title">商品信息：</p>
                <yl-table :list="dataList.crmGoodsList" border :show-header="true">
                  <el-table-column align="center" label="商品名称" prop="ylGoodsName"></el-table-column>
                  <el-table-column align="center" width="120" label="规格" prop="ylGoodsSpecifications"></el-table-column>
                  <el-table-column align="center" width="120" label=" 数量" prop="quantity"></el-table-column>
                </yl-table>
              </div>
            </el-col>
          </el-row>
              
        </div>
        <div class="examine-row" v-else>无</div>
      </div>
      <el-image-viewer v-if="showViewer" :on-close="onClose" :url-list="imageList"/>
    </div>
    <div class="bottom-view flex-row-center">
      <yl-button type="primary" plain @click="$router.go(-1)">返回</yl-button>
    </div>
  </div>
</template>
<script>
import { getMatchBillDetail } from '@/subject/admin/api/views_sale/resource_management'
import { accompanyBillMatchStatus } from '@/subject/admin/busi/sale/resource'
import ElImageViewer from 'element-ui/packages/image/src/image-viewer';

export default {
  name: 'FlowDirectionDetail',
  components: {
    ElImageViewer
  },
  computed: {
    auditStatusList(){
      return accompanyBillMatchStatus()
    }
  },
  data() {
    return {
      dataList: {},
      imageList: [],
      showViewer: false
    }
  },
  mounted() {
    let params = this.$route.params
    if (params.id) {
      this.getData(params.id)
    }
  },
  methods: {
    async getData(val) {
      let data = await getMatchBillDetail(val)
      if (data) {
        this.dataList = data;
      }
    }
  }
}
</script>
<style lang="scss" scoped>
@import './index.scss';
</style>