<template>
  <a-layout class="layout">
    <a-layout-header class="searchHeader header-shadow"
                     :style="{background: kmConfig.HeaderBackgroundColor,width: '100%', height: '64px',position: 'fixed',top:'0',zIndex:'999'}">
      <SearchHeader  :title='pageTitle'/>
    </a-layout-header>

    <a-layout-content >

      <div style="background-color: white;margin-top: 60px" :bordered="false">
        <div style="text-align:center;padding-top:30px;padding-bottom:20px;">
      <!-- 查询区域 -->
      <div class="table-page-search-wrapper">
        <!--<p></p>-->
        <a-form layout="inline">
          <a-row>
            <a-col :span="24" style="text-align: center;">
              <div style="display:inline-block">
                <div style="float: left">
                  <a-input style="width: 800px;" size="large" placeholder="标题、关键字、全文" v-model="content"
                           @pressEnter="pressEnterFun">
                    <a-dropdown slot="addonBefore" >
                        <span>
                          {{knowledgeTitle}}
                          <a-icon type="down" />
                        </span>
                      <a-menu slot="overlay" style="margin-top: 8px;margin-left: -12px;min-width: 200px">
                        <a-tree
                          checkStrictly
                          checkable
                          v-model="topicCodesTree"
                          @check="onTopicNodeCheck"
                          :selectable="boolSelect"
                          :tree-data="treeData"
                        />
                      </a-menu>
                    </a-dropdown>
                    <a slot="addonAfter" style="color: #303133" @click="searchDocFun(true)">结果中检索</a>
                    <a-icon slot="suffix" @click="searchDocFun(false)" type="search" style="color:#1890FF;fontSize:22px"/>
                  </a-input>
                  <div style="width: 750px;text-align: center;margin-top: 10px;margin-left: 20px">
                    <a-checkbox-group :options="dicCategoryOptions" v-model="category" @change="onCategoryChange"/>
                  </div>
                </div>
                <div style="float: left;width: 100px;text-align: left;margin-top: -2px">
                  <a @click="advancedSearch" style="color: black;margin-left: 20px;">高级检索&nbsp;&nbsp;&nbsp;></a>
                  <span style="margin-left: 20px">
                   <span>精确匹配</span>
                      <a-checkbox  class="checkbox" v-model="phraseMatchSearchFlag" style="margin-left: 8px"  />
                  </span>
                </div>
              </div>
            </a-col>
          </a-row>
        </a-form>
      </div>
      <!-- 查询区域-END -->
    </div>

        <div class="paramPathDiv">

          <div class="paramPathTitle"><span v-if="itemList.length >= 0">检索历史：</span></div>
          <div v-for="(item,index) in itemList" :key="index" class="paramPathContainer">

            <div class="paramPath">
              <a href="#" class="historySearchHref" @click="historySearch(index)"> {{ item }} </a>
            </div>
            <template v-if="index != itemList.length-1">
              <div style="float:left;display: inline-flex;font-size: x-small">></div>
            </template>

        </div>
      </div>

        <!-- 详情区域-BEGIN -->
        <a-drawer
          :title="docTitleOriginal"
          :width="720"
          :visible="drawerVisible"
          :body-style="{ paddingBottom: '80px' }"
          :footer-style="{ textAlign: 'right' }"
          @close="onDrawerClose"
        >
          <doc-detail ref="docDetailRef" :record="showDocDetail"></doc-detail>
          <br/>

          <a-divider style="margin: 5px 0 15px 0 "></a-divider>

          <doc-comments
            ref="comments"
            @new="refreshDocDetail"
            :doc-id="showDocDetail.id">
          </doc-comments>

        </a-drawer>
        <!-- 详情区域-END -->
        <!-- 表格区域 -->
    <div style="background-color:white;margin: 2px 1px 1px 1px;padding: 0px 0px 1px 0px">

      <!-- 操作按钮区域 -->
      <div class="table-operator" >
        <!--      <a-button type="primary" icon="download" @click="handleExportXls('草稿文件夹')">导出</a-button>-->
        <a-dropdown v-has="'searchList:batchDownload'" v-if="selectedRowKeys.length > 0">
          <a-menu slot="overlay">
            <a-menu-item key="1" @click="downloadKmDocBatch">
              <a-icon type="vertical-align-bottom"/>
              批量下载
            </a-menu-item>
          </a-menu>
          <a-button style="margin-left: 8px"> 批量操作
            <a-icon type="down"/>
          </a-button>
        </a-dropdown>
      </div>

      <!-- table区域-begin -->
      <div>
        <a-table
          ref="table"
          size="middle"
          rowKey="id"
          :columns="columns"
          :dataSource="dataSource"
          :pagination="ipagination"
          :loading="loading"
          :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
          @change="handleTableChange">

          <span slot="docTitle" slot-scope="text,record">
            <a @click="showDrawer(record)"  :title ="[ record.fileType + '文件 - 大小:' + record.fileSize +'B, By ' + record.createBy + '/' + record.orgCode_dictText + ' 下载  ' + record.downloads + ' 次' ] ">
              <span style="color: #303133" v-html="concatTitleContent(record)"> </span>
<!--              <span style="color: #303133" v-html=" record.title "> </span>-->
            </a>
          </span>

          // 添加自定义列表插槽
          <div slot="filterDropdown">
            <a-card style="width: 350px">
              <a-checkbox-group @change="onColSettingsChange" v-model="settingColumns" :defaultValue="settingColumns">
                <a-row>
                  <template v-for="(item,index) in defColumns">
                    <template v-if="item.key!='rowIndex'&& item.dataIndex!='action'">
                      <a-col :span="12">
                        <a-checkbox :value="item.dataIndex">{{ item.title }}</a-checkbox>
                      </a-col>
                    </template>
                  </template>
                </a-row>
              </a-checkbox-group>
            </a-card>
          </div>
          <a-icon slot="filterIcon" type='setting' title="自定义列"
                  :style="{ fontSize:'18px',color:  '#108ee9',minWidth:'35px'}"/>

          <span slot="indexNum" slot-scope="text, record,index">
                  <p>{{index+1+(ipagination.current-1)*ipagination.pageSize}}</p>
               </span>
          <span slot="action" slot-scope="text, record">
                  <a-space>
                            <a-popover content="预览">
                             <a-icon  type="read" title="预览" @click="previewKmDoc(record)"
                                      :style="{ fontSize: '16px', color: '#1890FF'}"/>
                            </a-popover>
                            <a-popover  :content="record.downloadFlag===1?'下载':'禁止下载'">
                               <a-icon v-if="record.downloadFlag===1" type="download" title="下载" @click="downloadKmDoc(record)"
                                       :style="{ fontSize: '16px', color: '#1890FF'}"/>
                               <a-icon v-else type="download" title="禁止下载"
                                       :style="{ fontSize: '16px', color: '#909399'}"/>
                            </a-popover>
                            <a-popover  :content="record.favourite===0?'收藏':'取消收藏'">
                             <a-icon v-if="record.favourite===0" type="star"  @click="addFavouriteKmDoc(record)"
                                     :style="{ fontSize: '18px', color: '#1890FF', }"/>
                             <a-icon v-else type="star" theme="filled"  @click="delFavouriteKmDoc(record)"
                                     :style="{ fontSize: '18px', color: '#1890FF', }"/>
                            </a-popover>
                    </a-space>
               </span>

        </a-table>
      </div>

      <b-j-modal :title="title"
                 :width="width"
                 :visible="visible"
                 @cancel="handleCancel"
                 cancelText="关闭"
                 :okButtonProps="{ class:{'jee-hidden': true} }">
        <p-d-f-modal :p-d-furl="PDFurl" :iframeWidth="width"/>
      </b-j-modal>

      <a-layout-footer >
        <global-footer/>
      </a-layout-footer>

    </div>
  </div>
    </a-layout-content>
  </a-layout>
</template>

<script>
import {ajaxGetDictItems, getDictItemsFromCache} from '@/api/api'
import {ACCESS_TOKEN} from "@/store/mutation-types"
import {downloadFileName, getAction, httpPostAction, postAction} from "../../../api/manage";
import {AJeecgListMixin} from '@/mixins/AJeecgListMixin'
import Vue from "vue";
import IframePageContent from "../../../components/layouts/IframeFReportView";
import SearchHeader from '../Common/SearchHeader'
import GlobalFooter from '@/components/page/GlobalFooter'
import DocComments from '@views/km/Common/Comments.vue'
import DocDetail from '@views/km/Common/DocDetail.vue'

export default {
    name: "docSearch",
    mixins: [AJeecgListMixin],
    components: { DocDetail, DocComments, IframePageContent,SearchHeader,GlobalFooter},
    data() {
      return {
        pageTitle:"检索结果",
        phraseMatchSearchFlag: false,
        knowledgeTitle: "知识专题",
        checkedArray: [],
        topicCodesTree: {
          checked: [],
          halfChecked: [],
        },
        topicCodes:"",
        // businessTypes:"1",
        boolSelect: false,
        treeData: [],
        confirmLoading: false,
        PDFurl: '',
        visible: false,
        pdfLoading: false,
        pdfShow: true,
        isorter: {
          column: '_score',
          order: 'desc',
        },
        labelCol: {
          xs: {span: 24},
          sm: {span: 5},
        },
        wrapperCol: {
          xs: {span: 24},
          sm: {span: 16},
        },
        category: [],
        title: "预览",
        width: "900",
        // hotTopicReportList: [],
        defaultBusinessTypeList: [],
        docDataSource: [],
        withInSearchFlag: false,
        filterOptions:[],
        //要filter的字段
        filterDictCode: 'km_dict_source',
        //结果中搜索的参数列表
        filterParamArray:[],
        filterParamPaths:[],
        //表头
        columns: [],
        //列设置
        settingColumns: [],
        //列定义
        defColumns: [
          {
            title: '#',
            dataIndex: '',
            key: 'rowIndex',
            width: 60,
            align: "center",
            scopedSlots: {customRender: 'indexNum'}
          },
          {
            title: '标题',
            align: "left",
            dataIndex: 'title',
            scopedSlots: {customRender: 'docTitle'}
          },
          {
            title: '分类',
            align: "left",
            dataIndex: 'category_dictText',
            width: 100,
            filters: [],
            customCell: () => {
              return {
                style: {
                  'max-width': '6em',
                  overflow: 'hidden',
                  whiteSpace: 'nowrap',
                  textOverflow: 'ellipsis'
                },
              };
            },
          },
          {
            title: '标签',
            align: "left",
            dataIndex: 'businessType_dictText',
            customCell: () => {
              return {
                style: {
                  'max-width': '6em',
                  overflow: 'hidden',
                  whiteSpace: 'nowrap',
                  textOverflow:'ellipsis'
                },
              };
            },
          },
          {
            title: '操作',
            dataIndex: 'action',
            align: "center",
            fixed: "right",
            width: 100,
            scopedSlots: {
              filterDropdown: 'filterDropdown',
              filterIcon: 'filterIcon',
              customRender: 'action'
            }
          },
        ],
        dicCategoryOptions: [],
        content: '',
        checkedValues: '',
        itemList: [],
        // 要加载多选的字典
        categoryDict: 'km_dict_category',

        url: {
          rootList: "/sys/category/loadTreeRoot",
          list: '/KM/kmDoc/searchDoc',
          hotTopicReport: '/KM/kmSearchRecord/hotTopicReport',
          defaultBusinessTypeList: '/KM/HomePage/listBusinessType',
          previewKmDoc: '/KM/kmDoc/previewKmDoc',
          downloadKmDoc: "/KM/kmDoc/downloadKmDoc",
          addFavouriteKmDoc: '/KM/kmDocFavourite/add',
          delFavouriteKmDoc: '/KM/kmDocFavourite/delete',
        },
        showDocDetail:{},
        docTitleOriginal:'',
        drawerVisible: false,
        indexUrl:'/front/DefaultDocSearch',
        kmConfig: {},
      }
    },
    created() {
      this.kmConfig = this.$store.getters.kmConfig
      this.loadTree()
      // 调用初始化自定义table列表函数
      this.initColumns()
      this.initFilterDict()
      if(this.kmConfig !== undefined && this.kmConfig.DefaultPageUseTopicList === '1')
        this.indexUrl = '/RecommendTopicList'

      //设置全局token
      Vue.prototype.token = Vue.ls.get(ACCESS_TOKEN)
      window._CONFIG['token'] = Vue.prototype.token
      this.initDict(this.categoryDict)
      let params = this.$route.params
      this.topicCodes = params.topicCodes
      this.category = params.category
      this.businessTypes = params.businessTypes
      console.log('this.$route.params:',this.$route.params)
      console.log('this.topicCodes:',this.topicCodes)

      if(!this.topicCodes ){
        if (Object.keys(params).length > 0) {
          this.phraseMatchSearchFlag = params.phraseMatchSearchFlag
          this.content = params.content

          let replaceTitle=this.content
          if(replaceTitle)
            replaceTitle = replaceTitle.replace(/，/g, ',')
          if(this.phraseMatchSearchFlag){
            params.phraseMatchSearchFlag = this.phraseMatchSearchFlag
          }
          params.content = this.content
          params.title = replaceTitle
          params.keywords = this.content
          params.advSearchFlag = false

          if (params.topicCodes != null) {
            params.topicCodes = params.topicCodes.split(',')
          }
          if (params.businessTypes != null) {
            params.businessTypes = params.businessTypes.split(',')
          }
          if (params.keywords != null) {
            params.keywords = params.keywords.split(',')
          }
        }
      }else{
        this.topicCodesTree = params.topicCodesTree
        this.knowledgeTitle = params.knowledgeTitle
        params.topicCodes = this.topicCodes.split(',')
      }
      this.searchDoSend(params)
    },

    methods: {
      concatTitleContent(record){
        return '<h4 class="result_title">' +record.title  +'</h4>' + '<span style="font-size: xx-small">'  + record.content + '</span>'
      },
      //打开文档详情
      showDrawer(record) {
        this.showDocDetail = record
        this.docTitleOriginal = this.removeHTMLTag(record.title)
        this.drawerVisible = true
      },
      removeHTMLTag(title){
        var regex = /(<([^>]+)>)/ig
        return title.replace(regex, "")
      },
      onDrawerClose() {
        this.drawerVisible = false
      },
      // 加载 分类数据
      initDict(dictCode) {
        this.dicCategoryOptions = this.getDictOptions(dictCode)
      },

      getDictOptions(dictCode){
        let dictOptions = []
        //优先从缓存中读取字典配置
        if (getDictItemsFromCache(dictCode)) {
          let options = getDictItemsFromCache(dictCode)
          options.forEach((item, index) => {
            let person = {}
            person.label = item.title
            person.value = item.value
            dictOptions.push(person)
          })
          return dictOptions
        }

        //根据字典Code, 初始化字典数组
        ajaxGetDictItems(dictCode, null).then((res) => {
          if (res.success) {
            let options = res.result
            options.forEach((item, index) => {
              let person = {}
              person.label = item.title
              person.value = item.value
              dictOptions.push(person)
            })
          }
        })
        return dictOptions
      },

      // 加载filter字段选项
      initFilterDict() {
        //优先从缓存中读取字典配置
        // if (getDictItemsFromCache(this.categoryDict)) {
        let options = getDictItemsFromCache(this.filterDictCode)
        if(options){
          options.forEach((item, index) => {
            let filterOption = {}
            filterOption.text = item.title
            filterOption.value = item.value
            this.filterOptions.push(filterOption)
          })
          //console.log(this.filterOptions)
          this.defColumns[2].filters =this.filterOptions
          return
        }

        //根据字典Code, 初始化字典数组
        ajaxGetDictItems(this.filterDictCode, null).then((res) => {
          if (res.success) {
            let options = res.result
            // console.log(options)
            options.forEach((item, index) => {
              let filterOption = {}
              filterOption.text = item.title
              filterOption.value = item.value
              this.filterOptions.push(filterOption)
            })
            this.defColumns[2].filters =this.filterOptions
          }
        })

      },

      // 是否全文检索
      onFTSCheck(e) {
        this.phraseMatchSearchFlag = e.target.checked
        //console.log(`checked = ${e.target.checked}`)
      },

      // 加载树节点，获取树数据
      loadTree() {
        let params = {
          async: false,
          pcode: ""
        }
        getAction(this.url.rootList, params).then(res => {
          if (res.success) {
            if (res.result && res.result.length > 0) {
              this.treeData = res.result
            } else {
              this.treeData = []
            }
          } else {
            this.$message.warning(res.message)
          }
        }).finally(() => {
        })
      },

      // 查找一个节点的所有父节点
      familyTree(treeData, id) {
        var arrTree = []
        var forFn = function (arr, key) {
          for (var i = 0; i < arr.length; i++) {
            var item = arr[i]
            if (item.key === key) {
              if (item.parentId === "0") {
                break
              } else {
                //console.log("父节点", item.parentId)
                arrTree.push(item.parentId)
                forFn(treeData, item.parentId)
              }
              break
            } else {
              if (item.children != null) {
                forFn(item.children, key)
              }
            }
          }
        }
        forFn(treeData, id)
        return arrTree
      },

      // 树节点选择触发
      onTopicNodeCheck(checkedKeys, checkedNodes) {
        if (checkedKeys.checked.length > 1) {
          let checkKeys = checkedKeys.checked[1]
          checkedKeys.checked = []
          checkedKeys.checked.push(checkKeys)
          let checkNodesTitle = checkedNodes.checkedNodes[1]
          checkedNodes.checkedNodes = []
          checkedNodes.checkedNodes.push(checkNodesTitle)
        }
        //console.log("checkedKeys", checkedKeys)
        //console.log("checkedNodes", checkedNodes)
        let temp = new Array()
        let tempArray = new Array()
        this.checkedArray = checkedKeys.checked

        for (let i = 0; i < this.checkedArray.length; i++) {
          let arrTemp = this.familyTree(this.treeData, this.checkedArray[i])
          temp = temp.concat(arrTemp)
        }
        // 数组去重
        tempArray = [...new Set(temp)]
        // console.log("tempArray", tempArray)
        checkedKeys.halfChecked = tempArray
        let checkedTitle = checkedNodes.checkedNodes
        this.knowledgeTitle = ""
        for (let i = 0; i < checkedTitle.length; i++) {
          if (this.knowledgeTitle === "") {
            this.knowledgeTitle = checkedTitle[i].data.props.title
          } else {
            if (checkedTitle[i].data.props.data != null) {
              this.knowledgeTitle = this.knowledgeTitle + "," + checkedTitle[i].data.props.title
            }
          }
        }
        if (this.knowledgeTitle === "") {
          this.knowledgeTitle = "知识专题"
          this.topicCodes = null
        }
        if(checkedTitle.length <=0 )
        {
          return
        }
        let param = this.getQueryParams()//查询条件互相影响
        // let param={}
        this.topicCodes=checkedTitle[0].data.props.code
        param["topicCodes"]=this.topicCodes.split(',')
        this.searchDoSend(param)
        // this.businessTypes=null

      },
      // 自定义列表  列设置更改事件
      onColSettingsChange(checkedValues) {
        var key = this.$route.name + ":colsettings"
        //console.log("colsettings", key)
        Vue.ls.set(key, checkedValues, 30 * 7 * 24 * 60 * 60 * 1000)
        this.settingColumns = checkedValues
        const cols = this.defColumns.filter(item => {
          if (item.key === 'rowIndex' || item.dataIndex === 'action') {
            return true
          }
          if (this.settingColumns.includes(item.dataIndex)) {
            return true
          }
          return false
        })
        this.columns = cols
      },
      // 自定义列表  初始化
      initColumns() {
        //权限过滤（列权限控制时打开，修改第二个参数为授权码前缀）
        //this.defColumns = colAuthFilter(this.defColumns,'testdemo:')

        var key = this.$route.name + ":colsettings"
        //console.log("colsettings", key)
        let colSettings = Vue.ls.get(key)
        if (colSettings === null || colSettings === undefined) {
          let allSettingColumns = []
          this.defColumns.forEach(function (item, i, array) {
            allSettingColumns.push(item.dataIndex)
          })
          this.settingColumns = allSettingColumns
          this.columns = this.defColumns
        } else {
          this.settingColumns = colSettings
          const cols = this.defColumns.filter(item => {
            if (item.key === 'rowIndex' || item.dataIndex === 'action') {
              return true
            }
            if (colSettings.includes(item.dataIndex)) {
              return true
            }
            return false
          })
          this.columns = cols
        }
      },

      loadData() {
        this.searchDocFun(false)
      },

      // 多选框选择触发
      onCategoryChange(checkedValues) {
        this.checkedValues = checkedValues.toString()
      },
      // 按回车键触发方法
      pressEnterFun(e) {
        this.$nextTick(() => {
          this.searchDocFun(false)
        })
      },
      //处理检索条件历史
      handleParamHistory(params){
        if(params.withinSearchFlag === undefined || !params.withinSearchFlag) {
          //首次检索，清空历史路径
          this.filterParamPaths = []
          this.filterParamArray = []
        }
        //结果中检索
         let hisParam = this.buildHisFilterParams(params)
        this.filterParamArray.push(hisParam)
        let paramPath = this.buildParamPath(hisParam)
        this.filterParamPaths.push(paramPath)
      },
      //建立历史条件，为结果中查询服务
      buildHisFilterParams(params){
        let hisParam = {}
        hisParam.phraseMatchSearchFlag = params.phraseMatchSearchFlag
        for (let key in params) {
          if (params[key] !== '' && params[key] !== null) {
            if(key === "title"
              || key === "keywords"
              || key === "content"
              || key === "category"
              || key === "businessTypes"
              || key === "topicCodes"
              || key === "advSearchFlag"
              || key === "createTimeStart"
              || key === "createTimeEnd" ){
              hisParam[key] = params[key]
            }
            }
          }
        return hisParam
      },
      //组装参数路径
      buildParamPath(params) {
        let paramPath = ""
        // console.log("buildParamPath",params)
        if (!params.advSearchFlag  && params.content !== undefined) {
          paramPath = "综合:" + params.content + "；"
        }
        let dictNames = ""
        for (let key in params) {
          if (params[key] !== '' && params[key] !== null) {
            switch (key) {
              case "title":
                if(params.advSearchFlag )
                  paramPath = paramPath + "标题:" + params.title + "；"
                break
              case "keywords":
                if(params.advSearchFlag )
                  paramPath = paramPath + "关键字:" + params.keywords + "；"
                break
              case "content":
                if(params.advSearchFlag )
                  paramPath = paramPath +"全文:"+ params.content + "；"
                break
              case "category":
                if(params.category.length > 0) {
                  dictNames = this.convertDictCodeToName("km_dict_category",params.category )
                  paramPath = paramPath +"分类:" + dictNames + "；"
                }
                break
              case "businessTypes":
                dictNames = this.convertDictCodeToName("km_dict_business",params.businessTypes  )
                paramPath = paramPath +"标签:" + dictNames + "；"
                break
              case "topicCodes":
                //console.log("params.topicCodes",params.topicCodes)
                dictNames = this.convertTreeDateToName(params.topicCodes )
                paramPath = paramPath +"专题:" + dictNames + "；"
                break
              case "createTimeStart":
                paramPath = paramPath +"时间起:" + params.createTimeStart + "；"
                break
              case "createTimeEnd":
                paramPath = paramPath +"时间止:" + params.createTimeEnd + "；"
                break
            }
          }
        }
        if(paramPath.length > 0){
          paramPath = paramPath.substring(0,paramPath.length -1)
        }
        return paramPath
      },
      //树code转name
      convertTreeDateToName(codes){
        let dictNames = ""
        if(this.treeData.length>0){
          for(let code of codes) {
            for (let item of this.treeData) {
              if (item.code === code) {
                dictNames = dictNames + item.title + "|"
              }
            }
          }
        }
        if(dictNames.length >0)
          dictNames = dictNames.substring(0,dictNames.length -1)

        return dictNames
      },
      //转换dict
      convertDictCodeToName(dictCode,codes){
        let dictOptions  = this.getDictOptions(dictCode)
        let dictNames = ""
        //console.log("convertDictCodeToName-----------",dictOptions)
        //console.log("codes-----------",codes)
        // debugger
        if (dictOptions.length > 0) {
          for(let code of codes) {
            for (let item of dictOptions) {
              if (item.value === code) {
                dictNames = dictNames + item.label + "|"
              }
            }
          }
        }
        if(dictNames.length >0)
          dictNames = dictNames.substring(0,dictNames.length -1)

        return dictNames
      },
      // 检索
      searchDocFun(withInSearchFlag) {
        this.withInSearchFlag = withInSearchFlag
        let searchParam = this.getQueryParams()//查询条件
        if (withInSearchFlag) {
          //启用用过滤
          this.defColumns[2].filters =this.filterOptions
        } else if (withInSearchFlag) {
          //结果中搜索嵌套限制
          if(this.filterParamArray.length>=50){
            alert("查询条件过多！请勿过度使用结果中检索")
            this.loading = false
            return
          }
          //禁用过滤
          this.defColumns[2].filters = []
        }

        // this.ipagination.current = 1
        this.loading = true
        if (this.content !== "" && this.content != null) {
          let replaceTitle=this.content
          if(this.content)
            replaceTitle = replaceTitle.replace(/，/g, ',').replace(' ',',')
          // if(this.phraseMatchSearchFlag){
          //   searchParam.content = replaceTitle
          // }else{
          //   delete searchParam["content"]
          // }
          searchParam.content = this.content
          searchParam.title = replaceTitle
          searchParam.keywords =  replaceTitle.split(',')
        }
        searchParam.advSearchFlag = false
        searchParam.phraseMatchSearchFlag = this.phraseMatchSearchFlag
        if (this.category !== undefined && this.category.length > 0) {
          searchParam['category'] = this.category
        }
        if( this.topicCodes!=null){
          searchParam['topicCodes']=this.topicCodes.split(',')
        }
        if(this.businessTypes!=null){
          searchParam['businessTypes']=this.businessTypes.split(',')
        }

        if (!this.empty(searchParam)) {
          if (this.withInSearchFlag) {
            searchParam.withinSearchFlag = true
            searchParam.filterParams = this.filterParamArray
          }

          //准备搜索接口参数
          searchParam.pageNo = this.ipagination.current
          searchParam.pageSize = this.ipagination.pageSize
          this.loading = true
          this.searchDoSend(searchParam)
        } else {
          this.loading = false
          this.$message.info("请输入搜索条件")
        }
      },

      //发送检索请求和处理结果
      searchDoSend(params) {
        postAction(this.url.list, params).then((res) => {
          if (res.success) {
            this.handleParamHistory(params)
            this.dataSource = res.result.kmSearchResultVOPage.records
            this.itemList = this.filterParamPaths
            if (res.result.kmSearchResultVOPage.total) {
              this.ipagination.total = res.result.kmSearchResultVOPage.total
            } else {
              this.ipagination.total = 0
            }
          } else {
            this.$message.error(res.message)
          }
          this.loading = false
        })
      },

      //历史条件搜索
      historySearch(paramIndex){
        let hisParams = this.filterParamArray
        if(hisParams.length === 0 ||paramIndex<0 || paramIndex>=hisParams.length)
          return
        let newParam = hisParams[paramIndex]
        this.filterParamArray.splice(paramIndex)
        this.filterParamPaths.splice(paramIndex)
        newParam.filterParams = this.filterParamArray
        this.content = newParam.content
        this.category = newParam.category
        this.topicCodes = newParam.topicCodes
        newParam.withinSearchFlag = true
        newParam.advSearchFlag = false
        newParam.pageNo = this.ipagination.current
        newParam.pageSize = this.ipagination.pageSize
        this.loading = true

        this.searchDoSend(newParam)
      },

      loadDefaultBusinessTypeFunc(){
        getAction(this.url.defaultBusinessTypeList).then(res => {
          if (res.success) {
            this.defaultBusinessTypeList = res.result
          } else {
            this.$message.error("业务加载失败")
          }
        })
      },

      //  判断对象是否为空
      empty(obj) {
        for (let key in obj) {
          return false
        }
        return true
      },

      //批量下载
      downloadKmDocBatch() {
        //console.log("批量下载")
        if (!this.url.downloadKmDoc) {
          this.$message.error("请设置url.downloadKmDoc属性!")
          return
        }
        if (this.selectionRows.length <= 0) {
          this.$message.warning('请选择一条记录！')
          return
        } else {
          var that = this
          this.$confirm({
            title: "确认下载",
            content: "是否下载选中数据?",
            onOk: function () {
              for (var i = 0; i < that.selectionRows.length; i++) {
                let fileName = i
                downloadFileName(that.url.downloadKmDoc, {docId: that.selectionRows[i].id})
              }
              that.onClearSelected()
            }
          })
        }
      },
      //加收藏夹
      addFavouriteKmDoc(record) {
        let httpurl = ''
        let method = ''
        httpurl += this.url.addFavouriteKmDoc
        method = 'post'
        httpPostAction(httpurl, {docId: record.id}, method).then((res) => {
          if (res.success) {
            this.$message.success("收藏成功!")
            //this.loadData()
            record.favourite = 1
          } else {
            this.$message.warning("收藏失败!")
          }
        }).finally(() => {
        })
      },
      //取消收藏夹
      delFavouriteKmDoc(record) {
        let httpurl = ''
        let method = ''
        httpurl += this.url.delFavouriteKmDoc
        method = 'delete'
        httpPostAction(httpurl, {docId: record.id}, method).then((res) => {
          if (res.success) {
            this.$message.success("取消收藏成功!")
            // this.loadData()
            record.favourite = 0
          } else {
            this.$message.warning("取消收藏失败!")
          }
        }).finally(() => {
        })
      },
      // 显示预览窗口，初始化
      previewKmDoc(record) {
        let kmTitle = record.title.replace(/<[^>]+>/g, '')
        this.PDFurl =  this.url.previewKmDoc + "?docId=" + record.id
        this.title ='预览 - ' + kmTitle
        this.visible = true
        this.pdfLoading = true
        this.pdfShow = true
      },
      //关闭预览窗口
      handleCancel() {
        this.visible = false
      },
      //下载文件
      downloadKmDoc(record) {
        this.$notification.success({
          message: '文件开始下载...',
          duration: 1,
        })
        downloadFileName(this.url.downloadKmDoc, {docId: record.id})
      },

      advancedSearch() {
        let params = {
          content: this.content,
          phraseMatchSearchFlag: this.phraseMatchSearchFlag
        }
        this.$router.push({name:'advancedSearch',params:params})
      },
      // 返回
      backHomepage() {
        this.$router.push(this.indexUrl)
      },
      // 跳转到收藏夹
      jumpKmDocFavouritePage(){
        this.$router.push('/km/filemanagement/KmDocFavouriteList')
      },
      // 跳转到个人草稿文件夹
      jumpDraftsPage(){
        this.$router.push('/km/filemanagement/DraftsList')
      },
      historyBack(){
        history.back()
      },
      // 点击个人登录，跳转页面
      login() {
        this.$router.push('/dashboard/analysis')
      },
      refreshDocDetail(){
        this.$refs.docDetailRef.refreshDocDetail()
      }
    }
  }
</script>

<style  type="text/css">
@import '~@assets/less/common.less';

.result_title {
  font-weight: bold;
}
</style>