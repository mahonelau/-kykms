<template>
  <div>
    <index-chart v-if="indexStyle==1"></index-chart>
  </div>
</template>

<script>
  import IndexChart from './IndexChart'


  export default {
    name: "Analysis",
    components: {
      IndexChart,
    },
    data() {
      return {
        indexStyle:1
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