<template>
  <div class="login-container">
    <div class="login-box">
      <div class="top-box">
        <div class="box">
          <img src="@/subject/admin/assets/login-top.png" />
          <login />
        </div>
      </div>
<!--      <div class="bottom-text">-->
<!--        <div>-->
<!--          京ICP备07504877号 | 互联网药品交易服务资格证编号：国A20150007 | 经营许可证编号：京B2-20192540 | 互联网药品信息服务资格证编号：(京)-经营性-2017-0019-->
<!--        </div>-->
<!--        <div>-->
<!--          医疗器械网络交易服务第三方平台备案凭证:（京）网械平台备字（2019）第00006号-->
<!--        </div>-->
<!--        <div>-->
<!--          Copyright©2019 Mypharma.com 北京瑞康医药有限公司 版权所有 京公网安备 11010502037300号-->
<!--        </div>-->
<!--      </div>-->
    </div>
  </div>
</template>

<script>
  import Login from './component/login'

  export default {
    components: {
      Login
    },
    data() {
      return {
      }
    },
    watch: {
    },
    computed: {

    },
    mounted() {

    },
    methods: {
    }
  }
</script>

<style lang="scss" scoped>
  .login-container {
    width: 100vw;
    height: 100vh;
    padding: 0;
    margin: 0;
    .login-box {
      width: 100vw;
      position: relative;
      height: 100vh;
      background: url("../../assets/login-back-admin.png") no-repeat center;
      background-size: 100% 100%;
      .top-box {
        position: absolute;
        left: 50%;
        top: 50%;
        transform: translate(-50%, -50%);
        .box {
          img {
            width: 355px;
            height: 46px;
            margin-bottom: 12px;
          }
        }
      }
      .bottom-text {
        position: absolute;
        width: 100%;
        bottom: 88px;
        text-align: center;
        div {
          height: 17px;
          font-size: 12px;
          font-weight: 400;
          color: #333333;
          line-height: 17px;
          margin-bottom: 10px;
        }
      }
    }
    .content {
      padding: 38px 60px;
      .mat-b-20 {
        margin-bottom: 20px;
        text-align: center;
      }
      .btn {
        margin-top: 80px;
      }
    }
  }
</style>
