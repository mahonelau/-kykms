<template>
  <div class="logo">
    <a @click="jumIndex">
      <img :src = siteInfo.siteAvatar  alt="logo">
      <span v-if="showTitle" class="showTitle">{{ siteInfo.siteTitle  }}</span>
    </a>
  </div>
</template>

<script>
  import { mixin } from '@/utils/mixin.js'
  import Vue from 'vue'

  export default {
    name: 'Logo',
    mixins: [mixin],
    props: {
      title: {
        type: String,
        default: "科亿知识库",
        required: false
      },
      showTitle: {
        type: Boolean,
        default: true,
        required: false
      }
    },
    data () {
      return {
        siteInfo: {},
        kmConfig: {},
        indexUrl:'/front/DefaultDocSearch'
      }
    },
    created() {
      this.siteInfo = this.$store.getters.siteInfo
      this.kmConfig = this.$store.getters.kmConfig
      if(this.kmConfig !== undefined && this.kmConfig.DefaultPageUseTopicList === '1')
        this.indexUrl = '/front/RecommendTopicList'
    },
    methods:{
      jumIndex(){
        let routeData = this.$router.resolve({
          path: this.indexUrl,
          query: {}
        })
        window.open(routeData.href, '_blank')
      }
    }
  }
</script>
<style lang="less" scoped>

  .showTitle{
    margin-left: 10px;
    font-size: 15px;
    /*color: #303133;*/
    font-weight: 600;
  }

  .sider {
    box-shadow: none !important;
    .logo {

      box-shadow: none !important;
      transition: background 300ms;

      a {
        color: white;
        &:hover {
          color: rgba(255, 255, 255, 0.8);
        }
      }
    }

    &.light .logo {
      background-color: @primary-color;
    }
  }
</style>