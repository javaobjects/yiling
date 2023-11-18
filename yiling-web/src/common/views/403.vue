<template>
  <div class="w">
    <div class="box">
      <img src="../assets/404_images/401.png" alt="">
      <div class="box-msg">
        <p>你的权限不足</p>
        <p>你无法操作此模块</p>
        <p>建议您联系管理员开通此模块的操作权限。</p>
      </div>
    </div>
  </div>
</template>

<script>

export default {
  name: 'Page401',
  computed: {
    message() {
      return '无法进入该页面'
    }
  }
}
</script>

<style lang="scss" scoped>
.w {
  width: 100%;
  height: calc(100vh - #{$topBarHeight});
  padding-top: 57px;
  background: #f4f4f4;
}
.box {
  display: flex;
  justify-content: center;
  width: 100%;
  img {
    width: 352px;
    height: 193px;
  }
  .box-msg {
    p {
      &:first-child {
        font-size: 28px;
        color: #000;
        font-weight: 500;
      }
      &:nth-child(2) {
        font-size: 22px;
        color: #646464;
        font-weight: 400;
      }
      &:nth-child(3) {
        font-size: 18px;
        color: #d7000f;
        font-weight: 400;
      }
    }
  }
}
</style>
