<template>
  <div>
    <router-link :to="{name:'defaultDocSearch'}">
          <span :style="{paddingLeft:'15px'}"  >
            <span :style="{verticalAlign: 'middle'}"><img :style="{ maxWidth:'22px'}" src="~@/assets/logo.png" alt="logo"></span>
            <span :style="{verticalAlign: 'middle',fontSize: '20px',fontWeight: 'normal',color: '#FFFFFF',paddingLeft: '10px'}">科亿知识库</span>
          </span>
    </router-link>
    <span  v-if="title != '' " :style="{verticalAlign: 'middle',fontSize: '20px',fontWeight: 'normal',color: '#FFFFFF',paddingLeft: '10px'}"> -> {{ title }} </span>

    <span style="float: right" >
      <user-menu :style="{verticalAlign: 'middle',fontSize: '15',fontWeight: 'normal',color: '#FFFFFF',float:'left'}" />
            <a-icon @click="jumpKmDocFavouritePage" theme="twoTone"  type='star' title="收藏夹"   :style="{ fontSize:'23px',minWidth:'30px'}"/>
            <a-icon @click="jumpDraftsPage" theme="twoTone"  type='folder' title="上传文件"   :style="{ fontSize:'23px',minWidth:'30px'}"/>
            <a-icon @click="jumpManage()" type='setting' title="进入管理" theme="twoTone" :style="{  fontSize:'23px',minWidth:'30px'}"/>
         </span>
  </div>
</template>

<script>
  import UserMenu from '@/components/tools/UserMenu'
  import { mixin } from '@/utils/mixin.js'

  export default {
    name: "SearchHeader",
    mixins: [mixin],
    components: {UserMenu},
    props: {
      title:{
        type: String,
        required: false,
        default: ''
      },
      theme: {
        type: String,
        required: false,
        default: 'white'
      }
    },
    data() {
      return {
      }
    },
    methods: {

      // 进入管理首页
      jumpManage() {
        this.$router.push('/dashboard/analysis');
      },
      // 跳转到收藏夹
      jumpKmDocFavouritePage(){
        this.$router.push('/km/filemanagement/KmDocFavouriteList');
      },
      // 跳转到个人草稿文件夹
      jumpDraftsPage(){
        this.$router.push('/km/filemanagement/DraftsList');
      },
    }
  }
</script>

