<template>
  <div class="app-container">
    <div class="app-container-content">
      <div class="common-box box-search">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">保险提供商</div>
              <el-select class="select-width" v-model="query.insuranceCompanyId" placeholder="请选择">
                <el-option label="全部" :value="'0'"></el-option>
                <el-option
                  v-for="item in underwriter"
                  :key="item.id"
                  :label="item.companyName"
                  :value="item.id">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <div class="title">是否有处方</div>
              <el-select class="select-width" v-model="query.prescriptionStatus" placeholder="请选择">
                <el-option
                  v-for="item in prescriptionData"
                  :key="item.id"
                  :label="item.label"
                  :value="item.id">
                </el-option>
              </el-select>
            </el-col>
          </el-row>
        </div>
        <div class="search-btn-box">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn :total="query.total" @search="handleSearch" @reset="handleReset" />
            </el-col>
          </el-row>
        </div>
      </div>
      <!-- 底部表格 -->
      <div class="table-list mar-t-8" v-for="(item, index) in dataList" :key="index">
        <div class="session">
          <el-row>
            <el-col :span="7">
              <span class="item-col-color">订单编号：{{ item.orderNo }}</span>
            </el-col>
            <el-col :span="4">
              <span class="item-col-color">订单总额：{{ item.totalAmount | toThousand('￥') }}</span>
            </el-col>
            <el-col :span="4">
              <span class="item-col-color">保司试算理赔金额：{{ item.insuranceSettleAmountTrial | toThousand('￥') }}</span>
            </el-col>
            <el-col :span="4">
              <span class="item-col-color">以岭计算理赔额：{{ item.insuranceSettleAmount | toThousand('￥') }}</span>
            </el-col>
            <el-col :span="5">
              <span class="item-col-color">保司：{{ item.insuranceCompanyName }}</span>
            </el-col>
          </el-row>
        </div>
        <div>
          <yl-table
            border
            :show-header="true"
            :list="item.syncInsuranceOrderDetailList"
            :cell-no-pad="false">
            <el-table-column label="商品名称" min-width="150" align="center" prop="goodsName"> </el-table-column>
            <el-table-column label="数量" min-width="80" align="center" prop="goodsQuantity"> </el-table-column>
            <el-table-column label="参保单价" min-width="80" align="center">
              <template slot-scope="{ row }">
                <div>
                  {{ row.insurancePrice | toThousand('￥') }}
                </div>
              </template>
            </el-table-column>
            <el-table-column label="以岭给终端的结算单价" min-width="150" align="center">
              <template slot-scope="{ row }">
                <div>
                  {{ row.terminalSettlePrice | toThousand('￥') }}
                </div>
              </template>
            </el-table-column>
            <el-table-column label="保司给以岭的结算单价" min-width="150" align="center">
              <template slot-scope="{ row }">
                <div>
                  {{ row.settlePrice | toThousand('￥') }}
                </div>
              </template>
            </el-table-column>
            <el-table-column label="管控采购渠道" min-width="150" align="center">
              <template slot-scope="{ row }">
                <div v-for="val in row.channelNameList" :key="val">
                  {{ val }}
                </div>
              </template>
            </el-table-column>
          </yl-table>
        </div>
        <!-- 下部图片 -->
        <div class="table-imgs">
          <div class="table-imgs-div">
            处方信息:
            <li v-if="item.orderPrescriptionDetail.prescription == null || item.orderPrescriptionDetail.prescription.prescriptionSnapshotUrlList.length < 1">
              未上传，请联系药店进行上传
            </li>
            <div v-else class="img-bottom">
              <li v-for="(item2, index2) in item.orderPrescriptionDetail.prescription.prescriptionSnapshotUrlList" :key="index2" @click="prescriptionClick(item2, item.orderPrescriptionDetail.prescription.prescriptionSnapshotUrlList)">
                <el-image :src="item2" lazy></el-image>
              </li>
            </div>
          </div>
          <div class="table-imgs-div">
            票据信息:
            <div class="img-bottom">
              <li v-for="(item3, index3) in item.orderReceiptsList" :key="index3" @click="prescriptionClick(item3, item.orderReceiptsList)">
                <el-image :src="item3" lazy></el-image>
              </li>
            </div>
            
          </div>
        </div>
        <div class="table-fail">
          <div class="table-fail-con" v-if="item.failReason !=''">
            <span>保司同步失败:</span>
            {{ item.failReason }}
          </div>
          <div class="synchronization">
            <ylButton type="primary" plain @click="synchronizationClick(item, index)">同步给保司</ylButton>
          </div>
        </div>
      </div>
      <div v-show="query.total > 0" class="bg-white flex-row-right pagin">
        <yl-pagination
          :total="query.total"
          :page.sync="query.current"
          :limit.sync="query.size"
          @pagination="getData"/>
      </div>
    </div>
     <!-- 图片放大 -->
    <el-image-viewer v-if="showViewer" :on-close="onClose" :url-list="imageList"/>
    <!-- 提示弹窗 -->
    <yl-dialog
      title="提示"
      width="420px"
      :visible.sync="showDialog"
      :show-footer="false"
      :show-close="false"
      class="dialog-updata">
      <div class="dialog-conter">
        <i class="el-icon-info"></i>
        确定要将数据同步给保司吗？同步之前一定要进行核验哦。尤其以岭计算理赔金额与保司计算的额度是否一致！
        <br/>
        (因同步数据量比较大，大约需要 60s,请耐心等待...)
        <div class="dialog-button">
          <ylButton plain @click="cancelClick">取消</ylButton>
          <ylButton type="primary" @click="confirmClick" :loading="buttonLoading">{{ buttonLoading ? '疯狂同步中' : '确认' }}</ylButton>
        </div>
      </div>
    </yl-dialog>
  </div>
</template>

<script>
import ElImageViewer from 'element-ui/packages/image/src/image-viewer';
import { getInsuranceCompanyList } from '@/subject/admin/api/cmp_api/insurance_basic_setting'
import { querySyncInsuranceOrder, syncOrder, getSyncOrderResult } from '@/subject/admin/api/cmp_api/insurance_financial_bill'
export default {
  name: 'Reconciliation',
  components: {
    ElImageViewer
  },
  computed: {},
  data() {
    return {
      query: {
        insuranceCompanyId: '0',
        prescriptionStatus: '0',
        current: 1,
        size: 10,
        total: 0
      },
      // 列表
      dataList: [],
      underwriter: [],
      showViewer: false,
      imageList: [],
      prescriptionData: [
        {
          label: '全部',
          id: '0'
        },
        {
          label: '仅显示有处方数据',
          id: '1'
        },
        {
          label: '仅显示无处方数据',
          id: '2'
        }
      ],
      showDialog: false,
      // 轮循 定时器
      intervalTime: '',
      // 倒计时 定时器
      timeoutTime: '',
      // 同步id
      synchronizationID: '',
      // 同步的当前元素下标
      synchronizationIndex: null,
      // 按钮loading
      buttonLoading: false
    }
  },
  activated() {
    // 获取保险提供商数据
    this.insuranceCompany();
    this.getList();
  },
  methods: {
    async insuranceCompany() {
      let data = await getInsuranceCompanyList(
        1,
        100,
        ''
      )
      if (data) {
        this.underwriter = data.records;
      }
    },
    async getList() {
      const query = this.query;
      let data = await querySyncInsuranceOrder(
        query.insuranceCompanyId,
        query.prescriptionStatus,
        query.current,
        query.size
      )
      if (data) {
        this.dataList = data.records
        this.query.total = data.total;
      }
    },
    // 点击分页
    getData({ page, limit }) {
      this.query.current = page;
      this.getList();
    },
    // 关闭弹窗图片
    onClose() {
      this.imageList = [];
      this.showViewer = false;
    },
    // 点击图片
    prescriptionClick(val, dataList) {
      if (val) {
        this.imageList = [val];
      }
      if (dataList && dataList.length > 1) {
        for (let i = 0; i < dataList.length; i ++) {
          if (val != dataList[i]) {
            this.imageList.push(
              dataList[i]
            )
          }
        }
      }
      this.showViewer = true;
    },
    handleSearch() {
      this.query.current = 1
      this.getList()
    },
    handleReset() {
      this.query = {
        insuranceCompanyId: '0',
        prescriptionStatus: '0',
        current: 1,
        size: 10
      };
    },
    // 点击同步给保司
    synchronizationClick(item, index) {
      this.synchronizationID = item.id;
      this.synchronizationIndex = index;
      this.showDialog = true;
    },
    // 点击确定
    async confirmClick() {
      this.buttonLoading = true;
      this.$common.showLoad();
      let data = await syncOrder(this.synchronizationID);
      this.$common.hideLoad();
      if (data !== undefined) {
        this.timeoutTime = setTimeout(() => {
          clearInterval(this.intervalTime)
          this.$common.warn('同步失败，稍后请重试！')
          this.buttonLoading = false;
        }, 60000)
        this.intervalTime = setInterval( async() => {
          this.$common.showLoad();
          let data2 = await getSyncOrderResult(this.synchronizationID);
          this.$common.hideLoad();
          if (data2 == 'success') {
            clearInterval(this.intervalTime)
            clearTimeout(this.timeoutTime)
            this.buttonLoading = false;
            this.showDialog = false;
            this.$common.n_success('同步成功')
            this.getList()
          } else { 
            const { code, message } = data2;
            if (code == 500) {
              this.dataList[this.synchronizationIndex].failReason = message;
              clearInterval(this.intervalTime)
              clearTimeout(this.timeoutTime)
              this.buttonLoading = false;
              this.showDialog = false;
            }
          }
        }, 5000)
      } else {
        this.buttonLoading = false;
      }
    },
    // 点击取消
    cancelClick() {
      clearInterval(this.intervalTime);
      clearTimeout(this.timeoutTime);
      this.buttonLoading = false;
      this.showDialog = false;
    }
  },
  beforeDestroy() {
    clearInterval(this.intervalTime);
    clearTimeout(this.timeoutTime)
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
