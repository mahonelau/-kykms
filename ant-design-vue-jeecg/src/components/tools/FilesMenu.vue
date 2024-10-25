<template>
  <div class="user-wrapper"  >
    <a-dropdown>
      <span class="action action-full ant-dropdown-link user-dropdown-menu"
            style="line-height: 64px; height: 60px;color: #000c17;padding: 0px 20px;margin: 0">
        <span style="margin: 0;padding-right: 10px;color: white">
          <a-icon type="menu"/>
          快捷菜单
        </span>
      </span>
      <a-menu slot="overlay" class="user-dropdown-menu-wrapper">

        <a-menu-item key="1" @click="jumpKmDocFavouritePage">
          <a-icon type="star"/>
          <span>我的收藏</span>
        </a-menu-item>
        <a-menu-item key="2" @click="jumpDraftsPage">
          <a-icon type="home"/>
          <span>我的知识</span>
        </a-menu-item>

        <a-menu-item key="3">
          <a href="javascript:;" @click="jumpKmDashBoard">
            <a-icon type="chrome"/>
            <span>统计数据</span>
          </a>
        </a-menu-item>

      </a-menu>
    </a-dropdown>
<!--    <user-password ref="userPassword"></user-password>-->
  </div>
</template>

<script>
import { mixinDevice } from '@/utils/mixin.js'
import Vue from 'vue'
import { ACCESS_TOKEN } from '@/store/mutation-types'

export default {
    name: 'FilesMenu',
    mixins: [mixinDevice],
    data() {
      return {
        token:''
      }
    },
    created() {
      this.token = Vue.ls.get(ACCESS_TOKEN)
    },

    methods: {
      jumpKmDocFavouritePage() {
        let token = this.token
        if (token === '' || token === null) {
          this.$router.push('/front/user/login')
        } else {
          let routeData = this.$router.resolve({
            path: '/km/filemanagement/KmDocFavouriteList',
            query: {}
          })
          window.open(routeData.href, '_blank')
        }
      },
      jumpKmDashBoard() {
        let token = this.token
        if (token === '' || token === null) {
          this.$router.push('/front/user/login')
        } else {
          let routeData = this.$router.resolve({
            path: '/dashboard/Analysis',
            query: {}
          })
          window.open(routeData.href, '_blank')
        }
      },
      // 跳转到个人草稿文件夹
      jumpDraftsPage() {
        let token = this.token
        if (token == '' || token == null) {
          this.$router.push('/front/user/login')
        } else {
          let routeData = this.$router.resolve({
            path: '/km/filemanagement/FilesList',
            query: {}
          })
          window.open(routeData.href, '_blank')
        }
      },
    }
  }
</script>

<style lang="less" scoped>
  /* update_begin author:zhaoxin date:20191129 for: 让搜索框颜色能随主题颜色变换*/
  /* update-begin author:sunjianlei date:20191220 for: 解决全局样式冲突问题 */
  .user-wrapper .search-input {
    width: 180px;
    color: inherit;

    /deep/ .ant-select-selection {
      background-color: inherit;
      border: 0;
      border-bottom: 1px solid white;

      &__placeholder, &__field__placeholder {
        color: inherit;
      }
    }
  }

  /* update-end author:sunjianlei date:20191220 for: 解决全局样式冲突问题 */
  /* update_end author:zhaoxin date:20191129 for: 让搜索框颜色能随主题颜色变换*/
</style>

<style scoped>
  .logout_title {
    color: inherit;
    text-decoration: none;
  }
</style>