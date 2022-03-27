<!--<template>-->
  <!--<div id="loader-wrapper">-->
    <!--<div id="loader"></div>-->
    <!--<div class="loader-section section-left"></div>-->
    <!--<div class="loader-section section-right"></div>-->
    <!--<div class="load_title">正在加载 科亿知识库,请耐心等待</div>-->
  <!--</div>-->
<!--</template>-->

<!--<script>-->
  <!--import {timeFix} from "@/utils/util"-->
  <!--import {mapActions} from 'vuex'-->


  <!--export default {-->
    <!--name: "Login",-->
    <!--data() {-->
      <!--return {-->
        <!--systemName: '',-->
        <!--loginName: '',-->
        <!--token: '',-->
      <!--}-->
    <!--},-->
    <!--created() {-->

      <!--this.loginFun();-->

    <!--},-->
    <!--methods: {-->
      <!--...mapActions(['LoginFun']),-->
      <!--// 获取sessionStorage的值-->
      <!--getSessionStorage(key) {-->
        <!--return sessionStorage.getItem(key);-->
      <!--},-->

      <!--// 判断跳转界面-->
      <!--loginFun() {-->
         <!--let username = this.getSessionStorage("loginName");-->
        <!--let Authorization = this.getSessionStorage("accessToken");-->
          <!--if (username == null || Authorization == null) {-->
           <!--}-->
        <!--let loginParams = {-->
          <!--userName: username,-->
          <!--thirdType: "oneSystem",-->
          <!--thirdToken: Authorization,-->
        <!--};-->
        <!--console.log("loginParams", loginParams);-->
        <!--this.LoginFun(loginParams).then((res) => {-->
          <!--this.jumpPage()-->
        <!--}).catch((err) => {-->

        <!--});-->
      <!--},-->

      <!--jumpPage() {-->
        <!--this.$router.push({name: "defaultDocSearch"}).catch(() => {-->
          <!--console.log('登录跳转界面出错,这个错误从哪里来的')-->
        <!--});-->
        <!--this.$notification.success({-->
          <!--message: '欢迎',-->
          <!--description: `${timeFix()}，欢迎回来`,-->
        <!--});-->
      <!--}-->


    <!--},-->
  <!--}-->
<!--</script>-->

<!--<style scoped>-->
  <!--@import '~@assets/less/common.less'-->
<!--</style>-->