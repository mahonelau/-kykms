<template>
  <div>
    <a-row  v-if="businessTypeList !== null && businessTypeList.length>0">
      <a-col >
        <a-space>
          <div v-for="(item,index) in businessTypeList" :key="index" class="key-word-wrap">
            <div class="key-word-item">{{item}}</div>
          </div>
        </a-space>
      </a-col>
    </a-row>
    <a-row style="margin-top: 10px;margin-bottom: 10px" v-if=" topicList!== null && topicList.length>0">
      <a-col >
        <a-space>
          <span>专&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;题:</span>
          <a-space v-if="topicList !== null && topicList.length>0">
            <div v-for="(item,index) in topicList" :key="index" class="key-word-wrap">
              <a-space>{{item}}</a-space>
            </div>
          </a-space>
        </a-space>
      </a-col>
    </a-row>
    <a-row :gutter="24"  style="margin-top: 10px;margin-bottom: 10px">
      <a-col :xl="6" :lg="7" :md="8" :sm="24">
        <a-space>
          <span>文件类型:</span><span>{{ model.fileType }}</span>
        </a-space>
      </a-col>
      <a-col :xl="6" :lg="7" :md="8" :sm="24">
        <a-space>
          <span>大小:</span><span>{{ model.fileSize }}</span>
        </a-space>
      </a-col>
      <a-col :xl="6" :lg="7" :md="8" :sm="24">
        <a-space v-if="model.category_dictText !== null && model.category_dictText !== undefined">
          <span>分类:</span><span>{{ model.category_dictText }}</span>
        </a-space>
      </a-col>
    </a-row>
    <div style="float:left;">
      <a-space size="middle"  >
        <a-popover v-if="!model.favourite" content="收藏数">
          <a-space>
            <a-icon  type="star" theme="twoTone" two-tone-color="#eb2f96"  ></a-icon>{{ model.favourites === undefined? 0:model.favourites}}
          </a-space>
        </a-popover>
        <a-popover v-else content="收藏数">
          <a-space>
            <a-icon  type="star" theme="filled"  :style="{ color: '#eb2f96'}"></a-icon>{{ model.favourites === undefined? 0:model.favourites}}
          </a-space>
        </a-popover>
        <a-popover content="预览数">
          <a-space>
            <a-icon  type="eye" theme="twoTone" two-tone-color="blue" ></a-icon>{{ model.views === undefined? 0: model.views }}
          </a-space>
        </a-popover>
        <a-popover content="评论数">
        <a-space>
            <a-icon type="message" theme="twoTone" two-tone-color="blue" ></a-icon>{{ model.comments === undefined? 0: model.comments}}
        </a-space>
        </a-popover>
        <a-popover content="下载数">
          <a-space>
            <a-icon  type="download"  ></a-icon>{{ model.downloads === undefined? 0:  model.downloads}}
          </a-space>
        </a-popover>
      </a-space>
    </div>
    <div style="float:right;">
      <a-space>
        <a-divider type="vertical"/>
        <a-tooltip   :title="model.createTime">
        <span style="color: #2c3e50;padding-right: 3px;font-weight: bold">{{ model.createBy }}</span> 创建于：{{ moment(model.createTime).fromNow() }}
        </a-tooltip>
      </a-space>
    </div>

  </div>
</template>

<script>
import moment from 'moment';
import {httpPostAction} from "@api/manage";

export default {
  name: 'DocDetail',
  props:{
    record:{
      type: Object
    }
  },
  data(){
    return {
      url:{
        queryById: "/KM/kmDoc/queryById",
        addFavouriteKmDoc: '/KM/kmDocFavourite/add',
      },
      model:{},
      keywordList: [],
      businessTypeList: [],
      topicList: [],
      moment
    }
  },
  methods:{
    refreshDocDetail(){
      let param = {id: this.model.id};
      let httpUrl  = this.url.queryById;
      let method = 'get';
      httpPostAction(httpUrl, param, method).then((res) => {
        if (res.success) {
          console.log("res.result",res.result)
          this.model = res.result.records[0]
          console.log("this.model",this.model)
        } else {
          this.$message.warning("刷新失败!");
        }
      }).finally(() => {
      })
    },
    //加收藏夹
    // addFavouriteKmDoc(record) {
    //   let httpurl  = this.url.addFavouriteKmDoc;
    //   let method = 'post';
    //   httpPostAction(httpurl, {docId: record.id}, method).then((res) => {
    //     if (res.success) {
    //       this.$message.success("收藏成功!");
    //       this.refreshDocDetail()
    //       model.favourite += 1;
    //     } else {
    //       this.$message.warning("收藏失败!" + res.message);
    //     }
    //   }).finally(() => {
    //   })
    // },
    generateList(){
      if(this.model.keywords !== undefined && this.model.keywords !== null && this.model.keywords.length !== 0) {
        let words = this.model.keywords.split(' ')
        this.keywordList = words;
      }
      if(this.model.businessTypes_dictText !== undefined && this.model.businessTypes_dictText !== null && this.model.businessTypes_dictText.length !== 0) {
        this.businessTypeList = this.model.businessTypes_dictText.split(',')
      }
      if(this.model.topicIds_dictText !== undefined && this.model.topicIds_dictText !== null && this.model.topicIds_dictText.length !== 0) {
        this.topicList = this.model.topicIds_dictText.split(',')
      }else{
        if(this.model.topicCodes_dictText !== undefined && this.model.topicCodes_dictText !== null && this.model.topicCodes_dictText.length !== 0) {
          this.topicList = this.model.topicCodes_dictText.split(',')
        }
      }
    }
  },
  computed:{
    fileSize() {
      if (this.model.fileSize > 1000000000) {
        return Math.round(this.model.fileSize / 10000000000) + 'G'
      } else if (this.model.fileSize > 1000000) {
        return Math.round(this.model.fileSize / 1000000) + 'M'
      } else if (this.model.fileSize > 1000) {
        return Math.round(this.model.fileSize / 1000) + 'K'
      }else  {
        return  this.model.fileSize  + 'B'
      }
    },
  },
  watch:{
    record: {
      immediate:true,
      handler(newVal,loldVal) {
        console.log("in docdetail props record newVal:", newVal)
        this.keywordList = []
        this.businessTypeList =[]
        this.model = Object.assign({}, newVal)
        this.generateList()
      }
    }
  }
}
</script>
<style scoped>
.key-word-itemDiv{
  display: inline-block;
  margin: 3px 0px 0px 10px
}
.key-word-item{
  margin :0px 3px 0px 3px;
  padding: 0px 1px 0px 1px;
  float: left;
  display: inline-flex;
  /*font-size: x-small;*/
  border-radius:5px;
  background: #D6E3F6FF;
  border-style: solid;
  border-width: 1px;

}

.key-word-wrap{
  display: inline-block;
  /*margin: 0px 3px 0px 3px*/
}
</style>