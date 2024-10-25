<template>
  <div class="footer">
    <div class="links">
      <a :href= siteInfo.siteHomePageUrl target="_blank">{{ siteInfo.siteTitle }}首页</a>
    </div>
    <div class="copyright">
      Copyright
      <a-icon type="copyright"/>
      2019 <span>{{ siteInfo.siteCopyRight }}</span>
    </div>
  </div>
</template>

<script>
  export default {
    name: "LayoutFooter",
    data () {
      return {
        siteInfo:{}
      }
    },
    created() {
      this.siteInfo = this.$store.getters.siteInfo
    },
  }
</script>

<style lang="less" scoped>
  .footer {
    padding: 16px;
    margin: 20px 0 0 0;
    text-align: center;

    .links {
      margin-bottom: 8px;

      a {
        color: rgba(0, 0, 0, .45);

        &:hover {
          color: darkorange;
        }

        &:not(:last-child) {
          margin-right: 40px;
        }
      }
    }
    .copyright {
      color: rgba(0, 0, 0, .45);
      font-size: 14px;
    }
  }
</style>