<template>
  <div>
    <index-km-chart ></index-km-chart>
  </div>
</template>

<script>
  import IndexKmChart from './IndexKmChart'


  export default {
    name: "Analysis",
    components: {
      IndexKmChart,
    },
    data() {
      return {

      }
    },
    created() {
    },
    methods: {

    }
  }
</script>


<style scoped>
  @import '~@assets/less/common.less';
</style>