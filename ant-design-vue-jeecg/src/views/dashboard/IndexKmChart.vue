<template>
  <div class="page-header-index-wide">

    <a-row :gutter="24">
      <a-col :sm="24" :md="12" :xl="12" :style="{ marginBottom: '24px' }">
        <a-card  >
          <a-space>
          <a-icon type="home" theme="filled" style="color: #1a53ba"></a-icon>
          <span style="font-size: small">知识总数:   </span> <span style="font-size: large;font-weight: bold">{{kmTotalAmount |numberFormat}}</span>
          </a-space>
        </a-card>
      </a-col>

      <a-col :sm="24" :md="12" :xl="12" :style="{ marginBottom: '24px' }">
        <a-card>
          <a-space>
            <a-icon type="folder" theme="filled" style="color: #1a53ba"></a-icon>
            <span style="font-size: small">存储使用:   </span> <span style="font-size: large;font-weight: bold">{{spaceTotal}}</span>
          </a-space>
        </a-card>
      </a-col>
    </a-row>

    <a-row :gutter="24">
      <a-col :sm="24" :md="12" :xl="12" :style="{ marginBottom: '24px' }">
        <a-card >
          <pie title="分类数量占比" :data-source="categoryAmount">
          </pie>
        </a-card>
      </a-col>
      <a-col :sm="24" :md="12" :xl="12" :style="{ marginBottom: '24px' }">
        <a-card >
          <pie title="分类空间占比" :data-source="categorySpace">
          </pie>
        </a-card>
      </a-col>
    </a-row>

    <a-row :gutter="24">
      <a-col :sm="24" :md="12" :xl="12" :style="{ marginBottom: '24px' }">
        <a-card>
          <rank-list title="热词榜" icon="fire" :list="rankListKeyword"/>

        </a-card>
      </a-col>
      <a-col :sm="24" :md="12" :xl="12" :style="{ marginBottom: '24px' }">
        <a-card>
          <rank-list title="热门专题榜" icon="fire"  :list="rankListTopic"/>
        </a-card>
      </a-col>
    </a-row>

    <a-card :loading="loading" :bordered="false" :body-style="{padding: '0'}">
      <div class="salesCard">
        <a-tabs default-active-key="1" size="large" :tab-bar-style="{marginBottom: '24px', paddingLeft: '16px'}">

          <a-tab-pane loading="true" tab="专题数量统计" key="1">
            <a-row>
              <a-col :xl="16" :lg="12" :md="12" :sm="24" :xs="24">
                <bar :dataSource="topicAmount"/>
              </a-col>
            </a-row>
          </a-tab-pane>
          <a-tab-pane tab="专题空间统计" key="2">
            <a-row>
              <a-col :xl="16" :lg="12" :md="12" :sm="24" :xs="24">
                <bar   :dataSource="topicSpace"/>
              </a-col>
            </a-row>
          </a-tab-pane>
        </a-tabs>
      </div>
    </a-card>

    <a-row>
      <a-col :span="24">
        <a-card :loading="loading" :bordered="false" title="最近一周访问量统计" :style="{ marginTop: '24px' }">
          <a-row>
            <a-col :span="6">
              <head-info title="今日IP" :content="loginfo.todayIp"></head-info>
            </a-col>
            <a-col :span="2">
              <a-spin class='circle-cust'>
                <a-icon slot="indicator" type="environment" style="font-size: 24px"  />
              </a-spin>
            </a-col>
            <a-col :span="6">
              <head-info title="今日访问" :content="loginfo.todayVisitCount"></head-info>
            </a-col>
            <a-col :span="2">
              <a-spin class='circle-cust'>
                <a-icon slot="indicator" type="team" style="font-size: 24px"  />
              </a-spin>
            </a-col>
            <a-col :span="6">
              <head-info title="总访问量" :content="loginfo.totalVisitCount"></head-info>
            </a-col>
            <a-col :span="2">
              <a-spin class='circle-cust'>
                <a-icon slot="indicator" type="rise" style="font-size: 24px"  />
              </a-spin>
            </a-col>
          </a-row>
          <line-chart-multid :fields="visitFields" :dataSource="visitInfo"></line-chart-multid>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script>
  import AreaChartTy from '@/components/chart/AreaChartTy'
  import Pie from '@/components/chart/Pie'
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

  import Trend from '@/components/Trend'
  import { getLoginfo,getVisitInfo } from '@/api/api'
  import {httpAction} from "@api/manage";

  export default {
    name: "IndexKmChart",
    components: {
      AreaChartTy,
      Pie,
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
        kmChartVO:{},
        categoryAmount:[],
        categorySpace:[],
        topicAmount:[],
        topicSpace:[],
        rankListKeyword:[],
        rankListTopic:[],
        kmTotalAmount: 0,
        spaceTotal:'0',
        url: {
          list: "/KM/HomePage/getCharData",
        },
        loading: true,
        center: null,
        loginfo:{},
        visitFields:['ip','visit'],
        visitInfo:[],
        indicator: <a-icon type="loading" style="font-size: 24px" spin />
      }
    },
    created() {
      // setTimeout(() => {
      //   this.loading = !this.loading
      // }, 1000)
      this.listData();
    },
    methods: {

      listData(){
        console.log("listData...............")
        let httpurl = ''
        let method = ''
        httpurl += this.url.list
        method = 'get'
        this.loading = true
        httpAction(httpurl,null, method).then((res) => {
          if (res.success) {
            console.log("res.result:",res.result)
            this.kmChartVO = res.result
            this.kmTotalAmount = res.result.kmDocSummaryVO.kmAmount
            this.spaceTotal = res.result.kmDocSummaryVO.fileSizeString
            this.formHotKeywordList(res.result.hotKeywordList)
            this.formHotTopicList(res.result.hotTopicList)
            this.formCategoryChart(res.result.categoryChartList)
            this.formTopicChart(res.result.topicChartList)
          }
        }).finally(() => {
          this.loading = false
        })
        this.initLogInfo()
      },
      formHotKeywordList(hotKeywordList){
        if(hotKeywordList === null || hotKeywordList === undefined)
          return
        for(let i = 0; i<hotKeywordList.length; i++){
          this.rankListKeyword.push({name:hotKeywordList[i]})
        }
      },
      formHotTopicList(hotTopicList){
        if(hotTopicList === null || hotTopicList === undefined)
          return
        for(let i = 0; i<hotTopicList.length; i++){
          this.rankListTopic.push({name:hotTopicList[i]})
        }
      },
      formCategoryChart(categoryChartList){
        if(categoryChartList === null || categoryChartList === undefined)
          return
        for(let i = 0; i<categoryChartList.length; i++){
          this.categoryAmount.push({
            item: categoryChartList[i].itemText,
            count: categoryChartList[i].amount,
          })
          this.categorySpace.push({
            item: categoryChartList[i].itemText,
            count: categoryChartList[i].fileSizeInt,
          })
        }
      },
      formTopicChart(topicChartList){
        if(topicChartList === null || topicChartList === undefined)
          return
        for(let i = 0; i<topicChartList.length; i++){
          this.topicAmount.push({
            x: topicChartList[i].itemText,
            y: topicChartList[i].amount,
          })
          this.topicSpace.push({
            x: topicChartList[i].itemText,
            y: topicChartList[i].fileSizeInt,
          })
        }
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
      },
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
      color: rgba(0, 0, 0, .45);
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