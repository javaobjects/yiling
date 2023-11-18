<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <div class="app-container-content has-bottom-bar">
      <div class="top-bar">
        <div class="header-bar mar-b-10">
          <div class="sign"></div>基本信息设置
        </div>
        <div class="content-box mar-b-16">
          <div class="mar-b-16">
            <div class="logo_title mar-b-10">店铺logo</div>
            <div class="logo_view">
              <yl-upload
                  :width="'80px'"
                  :height="'80px'"
                  :default-url="form.shopLogo"
                  :extral-data="{type: 'shopLogoPicture'}"
                  @onSuccess="onSuccess"
                />
            </div>
          </div>
          <div class="mar-b-16 item_box">
            <div class="logo_title mar-b-10">企业简介</div>
            <el-input
                type="textarea"
                :rows="15"
                placeholder="请输入"
                maxlength="1200" show-word-limit
                v-model="form.shopDesc">
              </el-input>
          </div>
          <div class="item_box">
            <div class="logo_title mar-b-10">销售区域</div>
            <yl-button type="text" @click="selectClick">{{ selectedData.length > 0 ? selectedDesc : '选择区域' }}</yl-button>
          </div>
        </div>
        <div class="header-bar mar-b-10">
          <div class="sign"></div>其他信息
        </div>
        <div class="content-box mar-b-16">
          <div class="mar-b-10 item_box">
            <div class="intro"><span>起配金额：</span><el-input v-model="form.startAmount" @keyup.native="form.startAmount = onInput(form.startAmount, 2)"></el-input> 元</div>
          </div>
        </div>
        <!-- 支付方式 -->
        <div class="header-bar mar-b-10">
          <div class="sign"></div>支付方式选择
        </div>
        <div class="check-box">
          <el-checkbox-group v-model="paymentMethodList">
            <span v-for="item in paymentMethodListArray" :key="item.id" class="check-item">
              <el-checkbox :label="item.id" :disabled="item.disabled">{{ item.name }}</el-checkbox>
            </span>
          </el-checkbox-group>
        </div>
      </div>
      <div class="flex-row-center bottom-bar-view">
        <yl-button v-role-btn="['1']" type="primary" @click="saveClick">保存</yl-button>
      </div>
    </div>
    <city-select :show.sync="showCityDialog" :init-ids="selectedData" platform="pop" @choose="citySelectConfirm"></city-select>
  </div>
</template>

<script>
import { setShop, getShop } from '@/subject/pop/api/b2b_api/store_manage';
import { onInputLimit } from '@/common/utils'
import { ylUpload } from '@/subject/pop/components'
import citySelect from '@/subject/pop/components/CitySelect'
import { mapGetters } from 'vuex'

export default {
  name: 'StoreManage',
  components: {
    ylUpload,
    citySelect
  },
  computed: {
    ...mapGetters([
      'currentEnterpriseInfo'
    ])
  },
  data() {
    return {
      // 头部导航
      navList: [
        {
          title: '首页',
          path: '/b2b_dashboard'
        },
        {
          title: '店铺管理'
        },
        {
          title: '店铺设置'
        }
      ],
      showCityDialog: false,
      form: {
        shopLogo: '',
        shopDesc: '',
        startAmount: ''
      },
      selectedDesc: '',
      selectedData: [],
      nodes: [],
      paymentMethodListArray: [],
      paymentMethodList: []
    };
  },
  mounted() {
    this.getDetail()
  },
  methods: {
    async getDetail() {
      let data = await getShop()
      if (data) {
        let form = {
          shopLogo: data.shopLogo,
          shopDesc: data.shopDesc,
          startAmount: data.startAmount + ''
        }
        this.form = form
        this.paymentMethodListArray = data.paymentMethodList || []

        let paymentMethodList = []
        data.paymentMethodList.forEach( (item) => {
          if (item.checked) {
            paymentMethodList.push(item.id)
          }
        })
        this.paymentMethodList = paymentMethodList

        if (data.areaJsonString) {
          let areaJsonString = data.areaJsonString
          let nodes = JSON.parse(areaJsonString)
          this.nodes = nodes

          let check = []
          this.getSelectId(nodes, check)
          this.selectedData = check
          this.selectedDesc = '已选' + data.description
        }
      }
    },
    selectClick() {
      this.showCityDialog = true
    },
    citySelectConfirm(data) {
      this.showCityDialog = false
      this.$log('data:', data)
      this.selectedDesc = data.desc
      this.nodes = data.nodes
      let check = []
      this.getSelectId(data.nodes, check)
      this.selectedData = check
    },
    // 递归获取选中的
    getSelectId(arr, selectArr) {
      arr.forEach(item => {
        if (item.children && item.children.length){
          this.getSelectId(item.children, selectArr)
        } else {
          selectArr.push(item.code)
        }
      });
    },
    async saveClick() {

      if (this.checkInputData()) {
        let nodes = this.nodes
        let areaJsonString = JSON.stringify(nodes)
        let form = this.form
        this.$common.showLoad()
        let data = await setShop(
          form.shopLogo,
          form.shopDesc,
          areaJsonString,
          form.startAmount,
          this.paymentMethodList
          )
          this.$common.hideLoad()
        if (typeof data != 'undefined') {
          this.$common.n_success('保存成功')
        }
      }
    },
    // 图片上传成功
    async onSuccess(data) {
      if (data.key) {
        this.form.fileKey = data.key
        this.form.shopLogo = data.url
      }
    },
    checkInputData() {
      let form = this.form
      if (!form.shopLogo) {
        this.$common.warn('请上传店铺logo')
        return false
      }
      if (this.selectedData.length == 0) {
        this.$common.warn('请设置销售区域')
        return false
      }
      if (!form.startAmount) {
        this.$common.warn('请设置起配金额')
        return false
      }
      if (this.paymentMethodList.length == 0) {
        this.$common.warn('请选择支付方式')
        return false
      }
      return true
    },
    // 校验两位小数
    onInput(value, limit) {
      return onInputLimit(value, limit)
    }
  }
};
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
