<template>
  <div id="app" v-cloak>
<!--    <keep-alive>-->
      <router-view />
<!--    </keep-alive>-->
  </div>
</template>

<script>
  import fackSelect from '@/common/mixins/fackSelect'
  export default {
    name: 'App',
    mixins: [fackSelect]
  }
</script>
<style>
  [v-cloak]{
    display: none;
  }
</style>
