<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <div class="app-container-content">
      <div class="common-box">
        <div class="">添加商品</div>
        <div class="mar-t-16 pad-b-16 border-1px-b">
          <!-- <el-steps :active="active">
            <el-step title="选择商品"></el-step>
            <el-step title="选择规格"></el-step>
            <el-step title="填写商品信息"></el-step>
            <el-step title="完成"></el-step>
          </el-steps> -->
          <steps :active="active" :finish-status="status" :steps="['选择商品', '选择规格', '填写商品信息','完成']" />
        </div>
      </div>
      <div v-show="boxActive == 1">
        <FirstSelectProducts @change="changeFirst"></FirstSelectProducts>
      </div>
      <div v-show="boxActive == 2">
        <SecondSelectNorms @change="changeSecond" :select-goods-id="selectGoodsId"></SecondSelectNorms>
      </div>
      <div v-show="boxActive == 3">
        <ThreeWirteInfo ref="threeWirteInfo" @change="changeThree" :select-goods-id="selectUnitIdAndId"></ThreeWirteInfo>
      </div>
      <div v-show="boxActive == 4">
        <FourthSuccess @change="changeFourth"></FourthSuccess>
      </div>
      <div style="height:64px"></div>
    </div>
    <!-- footer -->
    <div class="flex-row-center bottom-view" v-if="boxActive == 2">
      <yl-button v-if="boxActive == 2 " plain type="primary" @click="goFirst">返回上一步</yl-button>
    </div>
    <div class="flex-row-center bottom-view" v-if="boxActive == 3">
      <yl-button plain type="primary" @click="goSecond">返回上一步</yl-button>
      <yl-button plain type="primary" @click="goFourth">下一步</yl-button>
    </div>
  </div>
</template>

<script>
import { goodsStatus } from '@/subject/pop/utils/busi'
import FirstSelectProducts from './components/first_select_products.vue'
import SecondSelectNorms from './components/second_select_norms.vue'
import ThreeWirteInfo from './components/three_wirte_info.vue'
import FourthSuccess from './components/fourth_success.vue'
import Steps from '@/common/components/Steps';
export default {
  name: 'ZtProductsAdd',
  components: {
    Steps, FirstSelectProducts, SecondSelectNorms, ThreeWirteInfo, FourthSuccess
  },
  computed: {
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
          path: '/zt_dashboard'
        },
        {
          title: '商品管理',
          path: ''
        },
        {
          title: '添加商品'
        }
      ],

      active: 1,
      status: 'finish',
      boxActive: '1',
      // 选择商品选择的商品标准id
      selectGoodsId: -1,
      // 传递个组件3的  规格id 和商品标准id
      selectUnitIdAndId: {
        SpecificationsId: -1,
        standardId: -1
      }
    }
  },
  activated() {
    // this.getList(true)
    // this.getCate()
    // this.chooseProduct = []
  },
  created() {
    this.active = 1
    this.boxActive = '1'
    // 选择商品选择的商品标准id
    this.selectGoodsId = -1;
    // 传递个组件3的  规格id 和商品标准id
    this.selectUnitIdAndId = {
      SpecificationsId: -1,
      standardId: -1
    }
  },
  methods: {
    goFirst() {
      this.active = 1
      this.boxActive = 1
    },
    goSecond() {
      this.active = 2
      this.boxActive = 2
    },
    // 第三步，点击确定。
    goFourth() {
      this.$refs.threeWirteInfo.submit(this.selectGoodsId)
    },
    // 获取组件1 标准库商品列表 的传参
    changeFirst(id) {
      this.active = 2
      this.boxActive = 2
      // 标准库，标准商品id
      this.selectGoodsId = id
      console.log(id);
    },
    // 获取组件2 规格的传参
    changeSecond(standardId, id) {
      this.active = 3
      this.boxActive = 3
      // 规格id，和 标准库商品id
      this.selectUnitIdAndId = {
        SpecificationsId: id,
        standardId: standardId
      }
    },
    // 获组件3，保存后的穿参
    changeThree(e) {
      console.log(e);
      if (e) {
        this.active = 4
        this.boxActive = 4
      }
    },
    // 获取组件4，完成组件传参
    changeFourth(e) {
      if (e) {
        this.active = 1
        this.boxActive = '1'
        // 选择商品选择的商品标准id
        this.selectGoodsId = -1;
        // 传递个组件3的  规格id 和商品标准id
        this.selectUnitIdAndId = {
          SpecificationsId: -1,
          standardId: -1
        }
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
