<template>
  <div class="page-header-index-wide">
    <a-card :loading="loading" :bordered="false" :body-style="{padding: '0'}">
      <div class="salesCard">
        <a-tabs default-active-key="1" size="large" :tab-bar-style="{marginBottom: '24px', paddingLeft: '16px'}">

          <a-tab-pane loading="true" tab="热搜" key="1">
            <a-row>
              <a-col :xl="8" :lg="12" :md="12" :sm="24" :xs="24">
                <rank-list title="热词榜" :list="rankListKeyword"/>
              </a-col>
              <a-col :xl="8" :lg="12" :md="12" :sm="24" :xs="24">
                <rank-list title="热门专题榜" :list="rankListTopic"/>
              </a-col>
            </a-row>
          </a-tab-pane>

        </a-tabs>
      </div>
    </a-card>


  </div>
</template>

<script>
  import ChartCard from '@/components/ChartCard'
  import ACol from "ant-design-vue/es/grid/Col"
  import ATooltip from "ant-design-vue/es/tooltip/Tooltip"
  import MiniArea from '@/components/chart/MiniArea'
  import MiniBar from '@/components/chart/MiniBar'
  import MiniProgress from '@/components/chart/MiniProgress'
  import RankList from '@/components/chart/RankList'
  import Bar from '@/components/chart/Bar'
  import LineChartMultid from '@/components/chart/LineChartMultid'
  import HeadInfo from '@/components/tools/HeadInfo.vue'

  import {downFile, httpAction, getAction,downloadFile} from '@/api/manage'

  import Trend from '@/components/Trend'
  import { getLoginfo,getVisitInfo } from '@/api/api'
  import Vue from "vue";

  export default {
    name: "IndexChart",
    components: {
      ATooltip,
      ACol,
      ChartCard,
      MiniArea,
      MiniBar,
      MiniProgress,
      RankList,
      Bar,
      Trend,
      LineChartMultid,
      HeadInfo
    },
    data() {
      return {
        url: {
          listHotKey: "/KM/kmSearchRecord/hotKeywordReport",
          listHotTopic: "/KM/kmSearchRecord/hotTopicReport"
        },
        loading: true,
        center: null,
        rankListKeyword:[],
        rankListTopic:[],
        barData:[],
        loginfo:{},
        visitFields:['ip','visit'],
        visitInfo:[],
        indicator: <a-icon type="loading" style="font-size: 24px" spin />
      }
    },
    created() {
      this.getHotKeyList();
      this.getHotTopicList();
      setTimeout(() => {
        this.loading = !this.loading
      }, 1000)
      this.initLogInfo();
    },
    methods: {

      getHotKeyList(){
        let httpurl = '';
        let method = '';
        httpurl += this.url.listHotKey;
        method = 'get';
        httpAction(httpurl,null, method).then((res) => {
          if (res.success) {
            Object.keys(res.result).forEach(key=>{
              this.rankListKeyword.push({
                name: res.result[key]
              })
            })

          }
        }).finally(() => {
        })
      },
      getHotTopicList(){
        let httpurl = '';
        let method = '';
        httpurl += this.url.listHotTopic;
        method = 'get';
        httpAction(httpurl,null, method).then((res) => {
          if (res.success) {
            Object.keys(res.result).forEach(key=>{
              this.rankListTopic.push({
                name: res.result[key]
              })
            })

          }
        }).finally(() => {
        })
      },
      initLogInfo () {
        getLoginfo(null).then((res)=>{
          if(res.success){
            Object.keys(res.result).forEach(key=>{
              res.result[key] =res.result[key]+""
            })
            this.loginfo = res.result;
          }
        })
        getVisitInfo().then(res=>{
          if(res.success){
             this.visitInfo = res.result;
           }
         })
      }
    }
  }
</script>

<style lang="less" scoped>
  .circle-cust{
    position: relative;
    top: 28px;
    left: -100%;
  }
  .extra-wrapper {
    line-height: 55px;
    padding-right: 24px;

    .extra-item {
      display: inline-block;
      margin-right: 24px;

      a {
        margin-left: 24px;
      }
    }
  }

  /* 首页访问量统计 */
  .head-info {
    position: relative;
    text-align: left;
    padding: 0 32px 0 0;
    min-width: 125px;

    &.center {
      text-align: center;
      padding: 0 32px;
    }

    span {
      color: #606266;;
      display: inline-block;
      font-size: .95rem;
      line-height: 42px;
      margin-bottom: 4px;
    }
    p {
      line-height: 42px;
      margin: 0;
      a {
        font-weight: 600;
        font-size: 1rem;
      }
    }
  }
</style>