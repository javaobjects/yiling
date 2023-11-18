<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script>
  import fackSelect from '@/common/mixins/fackSelect'
  export default {
    name: 'App',
    mixins: [fackSelect]
  }
</script>
