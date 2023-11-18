<template>
  <div class="container">

  </div>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'Dashboard',
  computed: {
    ...mapGetters([
    ])
  }
}
</script>

<style lang="scss" scoped>
  .container {
    &-text {
      font-size: 30px;
      line-height: 46px;
    }
  }
</style>
