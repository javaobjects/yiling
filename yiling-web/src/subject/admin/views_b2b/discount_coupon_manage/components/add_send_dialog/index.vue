<template>
  <!-- 添加商品弹框 -->
  <yl-dialog title="发放" :visible.sync="addGoodsDialog" width="980px" right-btn-name="保存" @close="closeClick" @confirm="confirmClick">
    <div class="dialog-content-box-customer">
      <div class="dialog-content-top">
        <div class="title-view">
          <div class="sign"></div>选择需发券的采购商
        </div>
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">企业名称</div>
              <el-input v-model="goodsQuery.ename" placeholder="请输入企业名称" @keyup.enter.native="goodsHandleSearch" />
            </el-col>
            <el-col :span="8">
              <div class="title">企业ID</div>
              <el-input v-model="goodsQuery.eid" placeholder="请输入企业ID" @keyup.enter.native="goodsHandleSearch" />
            </el-col>
            <el-col :span="8">
              <div class="title">企业类型</div>
              <el-select v-model="goodsQuery.etype" placeholder="请选择企业类型">
                <el-option label="全部" :value="0"></el-option>
                <el-option
                  v-for="item in enterpriseType"
                  v-show="item.value != 1 && item.value != 2"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-col>
          </el-row>
        </div>
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">终端区域</div>
              <el-select v-loading="provinceLoading" v-model="goodsQuery.regionCode" @visible-change="visibleChange"
                placeholder="请选择终端区域">
                <el-option label="全部" value=""></el-option>
                <el-option
                  v-for="(item, index) in provinceList"
                  :key="item.code + index"
                  :label="item.name"
                  :value="item.code">
                </el-option>
              </el-select>
            </el-col>
          </el-row>
        </div>
        <div class="search-box">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn
                :total="goodsTotal"
                @search="goodsHandleSearch"
                @reset="goodsHandleReset"
              />
            </el-col>
          </el-row>
        </div>
      </div>
      <div class="down-box clearfix">
        <div class="left-view">
          <span class="red-text">*</span>
          <span class="font-size-base font-title-color">每个采购商发放优惠券数量</span>
          <el-input v-model="giveNum" @keyup.native="giveNum = onInput(giveNum, 0)"></el-input>
          <span class="font-size-base font-light-color">必须填写，大于0的正整数，默认数量为1</span>
        </div>
        <div class="btn">
          <ylButton type="primary" plain @click="batchAddClick">批量添加</ylButton>
        </div>
      </div>
      <div class="mar-t-10" style="padding-bottom: 10px;background: #FFFFFF;">
        <yl-table
          :stripe="true"
          :show-header="true"
          :list="goodsList"
          :total="goodsTotal"
          :page.sync="goodsQuery.page"
          :limit.sync="goodsQuery.limit"
          :loading="loading1"
          @getList="getList"
        >
          <el-table-column label="企业ID" align="center">
            <template slot-scope="{ row }">
              <div class="font-size-base">{{ row.eid }}</div>
            </template>
          </el-table-column>
          <el-table-column label="企业名称" min-width="162" align="center">
            <template slot-scope="{ row }">
              <div class="goods-desc">
                <div class="font-size-base">{{ row.ename }}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="企业状态" min-width="55" align="center">
            <template slot-scope="{ row }">
              <div class="font-size-base">{{ row.authStatus | dictLabel(enterpriseAuthStatus) }}</div>
            </template>
          </el-table-column>
          <el-table-column label="区域" min-width="55" align="center">
            <template slot-scope="{ row }">
              <div class="font-size-base">{{ row.regionName }}</div>
            </template>
          </el-table-column>
          <el-table-column label="企业类型" min-width="55" align="center">
            <template slot-scope="{ row }">
              <div class="font-size-base">{{ row.etype | dictLabel(enterpriseType) }}</div>
            </template>
          </el-table-column>
          <el-table-column label="已发放数量" min-width="55" align="center">
            <template slot-scope="{ row }">
              <div class="font-size-base">{{ row.giveNum }}</div>
            </template>
          </el-table-column>
          <el-table-column label="操作" min-width="55" align="center" fixed="right">
            <template slot-scope="{ row }">
              <div>
                <yl-button class="view-btn" v-if="!row.isSelect" type="text" @click="addClick(row)">添加</yl-button>
                <yl-button class="view-btn" v-if="row.isSelect" disabled type="text">已添加</yl-button>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
      <!-- 已添加商品 -->
      <div class="dialog-content-top">
        <div class="title-view">
          <div class="sign"></div>已选择发券采购商
        </div>
      </div>
      <div class="down-box clearfix">
        <div class="btn">
          <ylButton type="primary" plain @click="batchRemoveClick">批量删除</ylButton>
        </div>
      </div>
      <div class="mar-t-10" style="padding-bottom: 10px;background: #FFFFFF;">
        <yl-table
          :stripe="true"
          :show-header="true"
          :list="bottomGoodsList"
          :total="0"
        >
          <el-table-column label="企业ID" align="center">
            <template slot-scope="{ row }">
              <div class="font-size-base">{{ row.eid }}</div>
            </template>
          </el-table-column>
          <el-table-column label="企业名称" min-width="162" align="center">
            <template slot-scope="{ row }">
              <div class="goods-desc">
                <div class="font-size-base">{{ row.ename }}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="企业状态" min-width="55" align="center">
            <template slot-scope="{ row }">
              <div class="font-size-base">{{ row.authStatus | dictLabel(enterpriseAuthStatus) }}</div>
            </template>
          </el-table-column>
          <el-table-column label="区域" min-width="55" align="center">
            <template slot-scope="{ row }">
              <div class="font-size-base">{{ row.regionName }}</div>
            </template>
          </el-table-column>
          <el-table-column label="企业类型" min-width="55" align="center">
            <template slot-scope="{ row }">
              <div class="font-size-base">{{ row.etype | dictLabel(enterpriseType) }}</div>
            </template>
          </el-table-column>
          <el-table-column label="已发放数量" min-width="55" align="center">
            <template slot-scope="{ row }">
              <div class="font-size-base">{{ row.giveNum }}</div>
            </template>
          </el-table-column>
          <el-table-column label="操作" min-width="55" align="center" fixed="right">
            <template slot-scope="{ row, $index }">
              <div>
                <yl-button class="view-btn" type="text" @click="removeClick(row, $index)">删除</yl-button>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </yl-dialog>
</template>

<script>
import { giveQueryEnterpriseListPage, addEnterpriseGiveRecord } from '@/subject/admin/api/b2b_api/discount_coupon'
import { getLocation } from '@/subject/admin/api/common'
import { enterpriseType, enterpriseAuthStatus } from '@/subject/admin/utils/busi'
import { onInputLimit } from '@/common/utils'

export default {
  name: 'AddSendDialog',
  components: {},
  computed: {
    enterpriseType() {
      return enterpriseType()
    },
    enterpriseAuthStatus() {
      return enterpriseAuthStatus()
    }
  },
  data() {
    return {
      // 省份
      provinceLoading: false,
      provinceList: [],
      addGoodsDialog: false,
      // 搜索商品
      loading1: false,
      goodsQuery: {
        page: 1,
        limit: 10,
        etype: 0,
        regionCode: ''
      },
      goodsList: [],
      goodsTotal: 0,
      loading2: false,
      bottomGoodsQuery: {
        page: 1,
        limit: 10,
        etype: 0,
        regionCode: ''
      },
      bottomGoodsList: [],
      bottomGoodsTotal: 0,
      // 发放数量
      giveNum: 1,
      // 优惠券活动ID
      couponActivityId: ''
    };
  },
  mounted() {
  },
  methods: {
    init(couponActivityId) {
      this.giveNum = 1
      this.goodsList = []
      this.bottomGoodsList = []
      this.couponActivityId = couponActivityId
      this.addGoodsDialog = true;
      this.getList()
    },
    closeClick() {
      this.goodsList = []
      this.bottomGoodsList = []
    },
    async getList() {
      this.loading1 = true
      let goodsQuery = this.goodsQuery
      let data = await giveQueryEnterpriseListPage(
        goodsQuery.page,
        goodsQuery.limit,
        this.couponActivityId,
        goodsQuery.ename,
        Number(goodsQuery.eid),
        goodsQuery.etype,
        goodsQuery.regionCode
      );
      this.loading1 = false
      if (data) {
        
        let currentArr = this.bottomGoodsList
        if (currentArr.length > 0) {
          data.records.forEach(item => {
            let hasIndex = currentArr.findIndex(obj => {
              return obj.eid == item.eid;
            });
            if (hasIndex != -1) {
              item.isSelect = true;
            } else {
              item.isSelect = false;
            }
          });
        }
        this.goodsList = data.records
        this.goodsTotal = data.total
      }
    },
    // 保存点击
    async confirmClick() {
      if (!this.giveNum || this.giveNum == '0') {
        this.$common.warn('请填写发放优惠券数量')
        return false
      }

      if (this.bottomGoodsList.length == 0) {
        this.$common.warn('请添加发券采购商')
        return false
      }

      let data = await addEnterpriseGiveRecord(this.giveNum, this.bottomGoodsList)
      if (data) {
        this.$common.n_success('保存成功');
        this.addGoodsDialog = false;
        this.$emit('sendSuccess')
      } 
    },
    // 商品搜索
    goodsHandleSearch() {
      this.goodsQuery.page = 1
      this.getList()
    },
    goodsHandleReset() {
      this.goodsQuery = {
        page: 1,
        limit: 10,
        etype: 0,
        regionCode: ''
      }
    },
    addClick(row) {

      let currentArr = this.bottomGoodsList
      row.couponActivityId = this.couponActivityId
      currentArr.push(this.$common.clone(row))
      this.bottomGoodsList = currentArr

      let arr = this.goodsList;
      arr.forEach(item => {
        let hasIndex = currentArr.findIndex(obj => {
          return obj.eid == item.eid;
        });
        if (hasIndex != -1) {
          item.isSelect = true;
        } else {
          item.isSelect = false;
        }
      });
      this.goodsList = this.$common.clone(arr);

    },
    // 批量添加商品
    async batchAddClick() {

      let currentArr = this.bottomGoodsList;
      this.goodsList.forEach(item => {
        if (!item.isSelect) { // 没有添加
          item.couponActivityId = this.couponActivityId
          currentArr.push(this.$common.clone(item));
        }
      });
      this.bottomGoodsList = currentArr

      let arr = this.goodsList;
      arr.forEach(item => {
        let hasIndex = currentArr.findIndex(obj => {
          return obj.eid == item.eid;
        });
        if (hasIndex != -1) {
          item.isSelect = true;
        } else {
          item.isSelect = false;
        }
      });
      this.goodsList = this.$common.clone(arr);

    },
    // 商品搜索
    bottomGoodsHandleSearch() {
      this.bottomGoodsQuery.page = 1
      this.getBottomList()
    },
    bottomGoodsHandleReset() {
      this.bottomGoodsQuery = {
        page: 1,
        limit: 10,
        etype: 0,
        regionCode: ''
      }
    },
    async removeClick(row, index) {
      let currentArr = this.bottomGoodsList;
      currentArr.splice(index, 1);
      this.bottomGoodsList = currentArr;

      let arr = this.goodsList;
      arr.forEach(item => {
        let hasIndex = currentArr.findIndex(obj => {
          return obj.eid == item.eid;
        });
        if (hasIndex != -1) {
          item.isSelect = true;
        } else {
          item.isSelect = false;
        }
      });
      this.goodsList = this.$common.clone(arr);

    },
    // 批量添加商品
    batchRemoveClick() {
      this.bottomGoodsList = []
      this.getList()
    },
    // 省份
    visibleChange(e) {
      if (e) {
        if (this.provinceList.length === 0) {
          this.getAddress('')
        }
      }
    },
    // 获取区域选择器
    async getAddress() {
      this.provinceLoading = true
      let data = await getLocation('')
      this.provinceLoading = false
      this.provinceList = data.list
    },
    // 校验两位小数
    onInput(value, limit) {
      return onInputLimit(value, limit)
    }
  }
};
</script>

<style lang="scss" >
.my-form-box{
  .el-form-item{
    .el-form-item__label{
      color: $font-title-color; 
    }
    label{
      font-weight: 400 !important;
    }
  }
  .my-form-item-right{
    label{
      font-weight: 400 !important;
    }
  }
}
</style>
<style lang="scss" scoped>
@import './index.scss';
</style>
