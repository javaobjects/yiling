<template>
  <div class="common-box">
    <div class="box">
      <img src="@/subject/pop/assets/ok.png" alt="">
      <div class="mar-l-16">
        <p>添加成功</p>
        <p>如新增规格或商品请等待审核</p>
      </div>
    </div>
    <div class="nav">
      <div class="title">相关功能</div>
      <div class="tap" @click="add">1、继续“添加商品”</div>
      <!-- <div class="tap">2、进入“商品列表”</div> -->
      <!-- <div class="tap">3、设置“优惠券活动”</div> -->
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {

    }
  },
  methods: {
    add() {
      this.$emit('change', true)
    }
  }
}
</script>
<style lang="scss" scoped>
.box {
  display: flex;
  justify-content: center;
  align-items: center;
  img {
    width: 48px;
    height: 48px;
  }
  p {
    margin: 0;
    padding: 0;
    &:nth-child(1) {
      font-size: 16px;
      font-weight: 600;
      margin-bottom: 4px;
    }
    &:nth-child(2) {
      font-size: 14px;
    }
  }
}
.nav {
  margin-top: 32px;
  .title {
    margin-bottom: 12px;
    font-size: 16px;
    color: #333;
    font-weight: 600;
  }
  div {
    font-size: 16px;
    color: #1790ff;
    margin-bottom: 8px;
  }
  .tap{
    cursor: pointer;
  }
}
</style>