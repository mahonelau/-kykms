<template>
  <a-layout class="layout">

    <a-layout-header class="searchHeader header-shadow"
                     :style="{background: kmConfig.HeaderBackgroundColor,width: '100%', height: '64px',position: 'fixed',top:'0',zIndex:'999'}">
      <SearchHeader  :title='pageTitle'/>
    </a-layout-header>

  <a-layout-content style=" background:#fff; minHeight: 680px;margin-top: 64px">


  <div :bordered="false" style="background-color: #f2f2f2;height: 100%">
    <div style="background-color: white">


      <!-- 查询区域 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline">
          <a-row>
            <a-col :span="4"
                   style="text-align: left;background-color: #fafafa;height: 370px;overflow:auto;min-width: 200px">
              <div>
                <h3 style="margin-left: 20px;margin-top: 10px"><b>知识专题</b></h3>

              </div>
              <a-tree
                checkStrictly
                checkable
                v-model="topicCodesTree"
                :tree-data="treeData"
                @check="onCheck"
                :selectable="boolSelect"
              />
            </a-col>

            <a-col :span="18" style="margin-left: 80px;margin-top: 30px">
              <a-form layout="inline">
                <a-row :gutter="24">
                  <a-col :xl="14" :lg="14" :md="16" :sm="24">
                    <a-form-item label="标题" style="margin-left: 28px;margin-top: -10px">
                      <a-input placeholder="请输入标题" v-model="advance.title"></a-input>
                    </a-form-item>
                  </a-col>
                  <a-col :xl="14" :lg="14" :md="16" :sm="24">
                    <a-form-item label="关键字" style="margin-left: 14px;margin-top: -10px">
                      <a-input placeholder="请输入关键字" v-model="advance.keywords"></a-input>
                    </a-form-item>
                  </a-col>
                  <a-col :xl="14" :lg="14" :md="16" :sm="24">
                    <a-form-item label="全文" style="margin-left: 28px;margin-top: -10px">
                      <a-input placeholder="请输入内容" v-model="advance.content"></a-input>
                    </a-form-item>
                  </a-col>


                  <a-col :xl="14" :lg="14" :md="16" :sm="24">
                    <a-form-model-item label="标签" style="margin-left: 28px; margin-top: -10px">
                      <j-multi-select-tag type="list_multi" v-model="advance.businessTypes" :trigger-change="true"
                                          placeholder="请选择标签" dictCode="km_dict_business" ></j-multi-select-tag>
                    </a-form-model-item>
                  </a-col>

                  <a-col :xl="14" :lg="14" :md="16" :sm="24">
                    <a-form-item label="上传时间">
                      <j-date :show-time="true" date-format="YYYY-MM-DD" placeholder="请选择开始时间"
                              class="query-group-cust" v-model="advance.createTimeStart"></j-date>
                      <span class="query-group-split-cust"></span>
                      <j-date :show-time="true" date-format="YYYY-MM-DD" placeholder="请选择结束时间"
                              class="query-group-cust" v-model="advance.createTimeEnd"></j-date>
                    </a-form-item>
                  </a-col>
                  <a-col :xl="14" :lg="14" :md="16" :sm="24" style="margin-top: -10px;margin-bottom: 10px">
                    <div style="width: 100%;margin-left: 80px">
                      <a-checkbox-group v-model="advance.category" :options="dicCategoryOptions" @change="advanceOnChange"/>
                    </div>
                  </a-col>

                  <a-col :xl="14" :lg="14" :md="16" :sm="48">
                  <span style="float: left;overflow: hidden;align: center;margin-left: 80px" class="table-page-search-submitButtons">
                    <span style="margin-left: 20px">
                   <span>精确匹配</span>
                      <a-checkbox  class="checkbox" v-model="advance.phraseMatchSearchFlag" style="margin-left: 8px"  />
                  </span>
                    <a-button type="primary" @click="advanceSearch(false)" style="width: 100px;margin-left: 30px">检索</a-button>
                    <a-button @click="advanceSearch(true)" style="margin-left: 30px">结果中检索</a-button>
                    <a-button @click="defaultSearch" style="margin-left: 50px">返回</a-button>
                   <!--<a-button @click="advanceReset" style="margin-left: 30px">重置条件</a-button>-->
                 </span>
                  </a-col>
                </a-row>
              </a-form>
            </a-col>
          </a-row>
        </a-form>
      </div>
      <!-- 查询区域-END -->
    </div>

    <div class="paramPathDiv">

      <div class="paramPathTitle"><span v-if="itemList.length >= 0">检索历史：</span></div>
      <div v-for="(item,index) in itemList" :key="index" class="paramPathContainer">
        <!--            <a-space size="small">-->

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

    <div style="background-color:white;margin: 15px;padding: 15px">
      <!-- 操作按钮区域 -->
      <div v-has="'searchList:batchDownload'" class="table-operator">
        <a-dropdown v-if="selectedRowKeys.length > 0">
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
                  :style="{ fontSize:'16px',color:  '#108ee9',minWidth:'35px'}"/>

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

          <!--           <a-icon v-if="record.downloadFlag==1" type="download" title="下载" @click="downloadKmDoc(record)"-->
<!--                   :style="{ fontSize: '16px', color: '#1890FF'}"/>-->
<!--            <a-icon v-else type="download" title="禁止下载" :style="{ fontSize: '16px', color: '#909399'}"/>-->
<!--            <a-divider type="vertical"/>-->
<!--          <a-icon v-if="record.favourite==0" type="star" title="收藏" @click="addFavouriteKmDoc(record)"-->
<!--                  :style="{ fontSize: '18px', color: '#1890FF', }"/>-->
<!--          <a-icon v-else type="star" theme="filled" title="取消收藏" @click="delFavouriteKmDoc(record)"-->
<!--                  :style="{ fontSize: '18px', color: '#1890FF', }"/>-->

          <span slot="docTitle" slot-scope="text,record">
<!--           <span  @click="previewKmDoc(record,true)" ><a style="color: #303133">-->
             <a @click="showDrawer(record)"  :title ="[ record.fileType + '文件 - 大小:' + record.fileSize +'B, By ' + record.createBy + '/' + record.orgCode_dictText + ' 下载  ' + record.downloads + ' 次' ] ">
              <span style="color: #303133" v-html="concatTitleContent(record)"> </span>
             </a>
                           <!--              <span style="color: #303133" v-html=" record.title "> </span>-->

<!--             <span v-html=" record.title "   :title ="[ record.fileType + '文件 - 大小:' + record.fileSize +'B, By ' + record.createBy + '/' + record.orgCode_dictText + ' 下载  ' + record.downloads + ' 次' ] "></span></a></span>-->
        </span>

        </a-table>
      </div>

      <b-j-modal :title="title"
                 :width="width"
                 :visible="visible"
                 @cancel="handleCancel"
                 cancelText="关闭"
                 :okButtonProps="{ class:{'jee-hidden': true} }">
        <div>
          <div>
            <p-d-f-modal :p-d-furl="PDFurl" :iframeWidth="width"/>
          </div>
        </div>
      </b-j-modal>

    </div>

  </a-layout-content>
  </a-layout>
</template>

<script>
import {ACCESS_TOKEN} from "@/store/mutation-types"
import {ajaxGetDictItems, getDictItemsFromCache} from '@/api/api'
import {downloadFileName, getAction, httpPostAction, postAction} from "@api/manage";
import {AJeecgListMixin} from '@/mixins/AJeecgListMixin'
import Vue from "vue";
import SearchHeader from '../Common/SearchHeader'
import DocComments from '@views/km/Common/Comments.vue'
import DocDetail from '@views/km/Common/DocDetail.vue'

export default {
    name: "AdvancedSearch",
    mixins: [AJeecgListMixin],
    components: {
      SearchHeader,
      DocDetail,
      DocComments
    },
    watch: {
      loadedRatio: {
        handler(newVal, oldVal) {
          console.log(newVal)
          if (newVal === 1) {
            this.pdfLoading = false;
          }
        }
      }
    },
    data() {
      return {
        phraseMatchSearchFlag: false,
        pageTitle:"高级检索",
        checkedArray: [],
        isSearchResult: false,
        // a-tree 属性
        boolSelect: false,
        category: [],
        loadedRatio: 0,
        dictCode: 'km_dict_category',
        dicCategoryOptions: [],
        rangePickerArr: null,
        topicCodesTree: {
          checked: [],
          halfChecked: [],
        },
        title: "预览",
        width: '900',
        treeData: [],
        replaceFields: {
          key: 'id',
          parentId: "pid",
          title: "name",
        },
        //结果中检索的历史条件记录
        filterParamArray:[],
        //检索路径
        filterParamPaths:[],
        filterOptions:[],
        //要filter的字段
        filterDictCode: 'km_dict_source',
        advance: {},
        itemList: [],
        PDFurl: '',
        visible: false,
        pdfLoading: false,
        pdfShow: true,
        isorter: {
          column: '_score',
          order: 'desc',
        },
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
            width: 80,
            scopedSlots: {
              filterDropdown: 'filterDropdown',
              filterIcon: 'filterIcon',
              customRender: 'action'
            }
          },
        ],
        siteInfo: {},
        kmConfig: {},
        url: {
          list: '/KM/kmDoc/searchDoc',
          previewKmDoc: '/KM/kmDoc/previewKmDoc',
          downloadKmDoc: "/KM/kmDoc/downloadKmDoc",
          rootList: "/sys/category/loadTreeRoot",
          childList: "/sys/category/childList",
          advanceSearchDoc: '/KM/kmDoc/searchDoc',
          addFavouriteKmDoc: '/KM/kmDocFavourite/add',
          delFavouriteKmDoc: '/KM/kmDocFavourite/delete',
        },
        indexUrl:'/DefaultDocSearch',
        showDocDetail:{},
        docTitleOriginal:'',
        drawerVisible: false,
      }
    },
    created() {
      this.siteInfo = this.$store.getters.siteInfo
      this.kmConfig = this.$store.getters.kmConfig
      console.log('siteInfo:',this.siteInfo)
      if(this.kmConfig !== undefined && this.kmConfig.DefaultPageUseTopicList === '1')
        this.indexUrl = '/RecommendTopicList'
      //设置全局token
      Vue.prototype.token = Vue.ls.get(ACCESS_TOKEN);
      window._CONFIG['token'] = Vue.prototype.token;
      this.initDict(this.dictCode);
      this.loadTree();
      // 调用初始化自定义table列表函数
      this.initColumns();
      this.changeTitle(this.pageTitle);
      let params = this.$route.params
      this.advance = {}
      this.advance.content =params.content
      this.phraseMatchSearchFlag = params.phraseMatchSearchFlag
    },
    methods: {
      concatTitleContent(record){
        return '<h4>' +record.title  +'</h4>' + '<span style="font-size: xx-small">'  + record.content + '</span>'
      },
      onDrawerClose() {
        this.drawerVisible = false
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
      // 加载filter字段选项
      initFilterDict() {
        //优先从缓存中读取字典配置
        // if (getDictItemsFromCache(this.dictCode)) {
        let options = getDictItemsFromCache(this.filterDictCode)
        if(options){
          options.forEach((item, index) => {
            let filterOption = {};
            filterOption.text = item.title;
            filterOption.value = item.value;
            this.filterOptions.push(filterOption);
          });
          console.log(this.filterOptions);
          this.defColumns[2].filters =this.filterOptions;
          return
        }

        //根据字典Code, 初始化字典数组
        ajaxGetDictItems(this.filterDictCode, null).then((res) => {
          if (res.success) {
            let options = res.result
            console.log(options);
            options.forEach((item, index) => {
              let filterOption = {};
              filterOption.text = item.title;
              filterOption.value = item.value;
              this.filterOptions.push(filterOption);
            });
            this.defColumns[2].filters =this.filterOptions;
          }
        })
      },

      // 更改页面标题
      changeTitle(title) {
        let projectTitle = this.siteInfo.siteTitle
        // 特殊处理
        document.title = title + ' · ' + projectTitle

      },

      // 自定义列表  列设置更改事件
      onColSettingsChange(checkedValues) {
        var key = this.$route.name + ":colsettings";
        console.log("colsettings", key);
        Vue.ls.set(key, checkedValues, 30 * 7 * 24 * 60 * 60 * 1000)
        this.settingColumns = checkedValues;
        const cols = this.defColumns.filter(item => {
          if (item.key == 'rowIndex' || item.dataIndex == 'action') {
            return true
          }
          if (this.settingColumns.includes(item.dataIndex)) {
            return true
          }
          return false
        })
        this.columns = cols;
      },
      // 自定义列表  初始化
      initColumns() {
        var key = this.$route.name + ":colsettings";
        console.log("colsettings", key);
        let colSettings = Vue.ls.get(key);
        if (colSettings == null || colSettings == undefined) {
          let allSettingColumns = [];
          this.defColumns.forEach(function (item, i, array) {
            allSettingColumns.push(item.dataIndex);
          })
          this.settingColumns = allSettingColumns;
          this.columns = this.defColumns;
        } else {
          this.settingColumns = colSettings;
          const cols = this.defColumns.filter(item => {
            if (item.key == 'rowIndex' || item.dataIndex == 'action') {
              return true;
            }
            if (colSettings.includes(item.dataIndex)) {
              return true;
            }
            return false;
          })
          this.columns = cols;
        }
      },

      loadData() {
        this.advanceSearch(false);
      },

      // 加载树节点，获取树数据
      loadTree() {
        let params = {
          async: false,
          pcode: ""
        };
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

      //批量下载
      downloadKmDocBatch() {
        console.log("批量下载")
        if (!this.url.downloadKmDoc) {
          this.$message.error("请设置url.downloadKmDoc属性!")
          return
        }
        if (this.selectionRows.length <= 0) {
          this.$message.warning('请选择一条记录！');
          return;
        } else {
          var that = this;
          this.$confirm({
            title: "确认下载",
            content: "是否下载选中数据?",
            onOk: function () {
              for (var i = 0; i < that.selectionRows.length; i++) {
                let fileName = i;
                downloadFileName(that.url.downloadKmDoc, {docId: that.selectionRows[i].id})
              }
              //that.loadData();
              that.onClearSelected();
            }
          });
        }
      },

      // 显示预览窗口，初始化
      previewKmDoc(record, boolFormDisabled) {
        this.PDFurl =   this.url.previewKmDoc + "?docId=" + record.id;
        this.visible = true;
        this.pdfLoading = true;
        this.pdfShow = true;
      },
      //关闭预览窗口
      handleCancel() {
        this.visible = false;
      },

      // 查找一个节点的所有父节点
      familyTree(treeData, id) {
        var arrTree = [];
        var forFn = function (arr, key) {
          for (var i = 0; i < arr.length; i++) {
            var item = arr[i]
            if (item.key === key) {
              if (item.parentId === "0") {
                break;
              } else {
                console.log("父节点", item.parentId);
                arrTree.push(item.parentId);
                forFn(treeData, item.parentId);
              }
              break
            } else {
              if (item.children != null) {
                forFn(item.children, key);
              }
            }
          }
        }
        forFn(treeData, id);
        return arrTree
      },

      //下载文件
      downloadKmDoc(record) {
        this.$notification.success({
          message: '文件开始下载...',
          duration: 1,
        });
        downloadFileName(this.url.downloadKmDoc, {docId: record.id})
      },
      // 树节点选择触发
      onCheck(checkedKeys, checkedNodes) {
        let temp = new Array();
        let tempArray = new Array();
        this.checkedArray = checkedKeys.checked;
        for (let i = 0; i < this.checkedArray.length; i++) {
          let arrTemp = this.familyTree(this.treeData, this.checkedArray[i]);
          temp = temp.concat(arrTemp);
        }
        // 数组去重
        tempArray = [...new Set(temp)];
        console.log("tempArray", tempArray);
        checkedKeys.halfChecked = tempArray;
        let checkedTitle = checkedNodes.checkedNodes;
        this.advance.topicCodes = "";
        for (let i = 0; i < checkedTitle.length; i++) {
          if (this.advance.topicCodes === "") {
            this.advance.topicCodes = checkedTitle[i].data.props.code;
          } else {
            if (checkedTitle[i].data.props.code != null) {
              this.advance.topicCodes = this.advance.topicCodes + "," + checkedTitle[i].data.props.code;
            }
          }
        }
      },
      initDict(dictCode) {
        this.dicCategoryOptions = this.getDictOptions(dictCode);
      },

      getDictOptions(dictCode){
        let dictOptions = [];
        //优先从缓存中读取字典配置
        if (getDictItemsFromCache(dictCode)) {
          let options = getDictItemsFromCache(dictCode)
          options.forEach((item, index) => {
            let person = {};
            person.label = item.title;
            person.value = item.value;
            dictOptions.push(person);
          });
          return dictOptions
        }

        //根据字典Code, 初始化字典数组
        ajaxGetDictItems(dictCode, null).then((res) => {
          if (res.success) {
            let options = res.result
            options.forEach((item, index) => {
              let person = {};
              person.label = item.title;
              person.value = item.value;
              dictOptions.push(person);
            });
          }
        })
        return dictOptions
      },

      // 选中多选框触发
      advanceOnChange(checkedValues) {
        // this.category = checkedValues;
        // this.advance.category = this.category.toString();
      },
      // 高级查询条件重置
      // advanceReset() {
      //   this.category = [];
      //   this.advance = {};
      //   this.topicCodesTree = [];
      //   this.rangePickerArr = null;
      //   this.itemList = [];
      // },

      defaultSearch() {
        let params={
          content: this.advance.content,
          phraseMatchSearchFlag: this.advance.phraseMatchSearchFlag
        }
        this.$router.push({name: 'docSearch', params:params})
      },
      // 高级查询
      advanceSearch(withinSearchFlag) {
        // this.isSearchResult = withinSearchFlag
        if(withinSearchFlag) {
          //结果中搜索嵌套限制
          if(this.filterParamArray.length>=50){
            alert("查询条件过多！请勿过度使用结果中检索")
            this.loading = false;
            return
          }
        }

        let searchParam =  Object.assign({},this.advance)
        console.log("this.advance",this.advance)
        // if (this.advance.topicCodes !== null && this.advance.topicCodes !== undefined) {
        if (this.advance.topicCodes ) {
          searchParam.topicCodes = this.advance.topicCodes.split(',');
        }
        if (this.advance.businessTypes ) {
          searchParam.businessTypes = this.advance.businessTypes.split(',');
        }
        if (searchParam.keywords) {
          searchParam.keywords = this.advance.keywords.split(',');
          searchParam.keywords = this.advance.keywords.split(' ');
        }
        console.log("searchParam",searchParam)

        if (this.empty(searchParam)) {
          if (withinSearchFlag) {
            // this.advance.withinSearchFlag = true
            searchParam.withinSearchFlag = true
            searchParam.filterParams = this.filterParamArray
            console.log("searchParam this.filterParamArray:",this.filterParamArray)
          }
          else{
            searchParam.withinSearchFlag = false
          }
          searchParam.advSearchFlag = true
          searchParam.field = this.getQueryField();
          searchParam.pageNo = this.ipagination.current;
          searchParam.pageSize = this.ipagination.pageSize;
          this.loading = true;

          this.searchDoSend(searchParam)
        } else {
            this.$message.info("请输入搜索条件");
        }
      },

      //加收藏夹
      addFavouriteKmDoc(record) {
        let httpurl = '';
        let method = '';
        httpurl += this.url.addFavouriteKmDoc;
        method = 'post';
        httpPostAction(httpurl, {docId: record.id}, method).then((res) => {
          if (res.success) {
            this.$message.success("收藏成功!");
            // this.loadData();
            record.favourite = 1;
          } else {
            this.$message.warning("收藏失败!");
          }
        }).finally(() => {
        })
      },
      //取消收藏夹
      delFavouriteKmDoc(record) {
        let httpurl = '';
        let method = '';
        httpurl += this.url.delFavouriteKmDoc;
        method = 'delete';
        httpPostAction(httpurl, {docId: record.id}, method).then((res) => {
          if (res.success) {
            this.$message.success("取消收藏成功!");
            // this.loadData();
            record.favourite = 0;
          } else {
            this.$message.warning("取消收藏失败!");
          }
        }).finally(() => {
        })
      },

      //  判断对象是否为空
      empty(obj) {
        console.log(obj)
        for (let key in obj) {
          return true;
        }
        return false;
      },

      // 返回
      backHomepage() {
        this.$router.push(indexUrl);
      },
      advancedSearch() {
        this.$router.push('/advancedSearch');
      },
      // 点击个人登录，跳转页面
      login() {
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
      historyBack(){
        history.back()
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
      buildParamPath(params){
        let paramPath = ""
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

      //处理检索条件历史
      handleParamHistory(param){
        // debugger
        if(param.withinSearchFlag === undefined || !param.withinSearchFlag ) {
          console.log("首次检索，清空历史路径",param.withinSearchFlag )
          //首次检索，清空历史路径
          this.filterParamPaths = []
          this.filterParamArray = []
        }
        //结果中检索
        delete param.filterParams
        let hisParam = {...param}

        this.filterParamArray.push(hisParam)
        let paramPath = this.buildParamPath(hisParam)
        this.filterParamPaths.push(paramPath)
        console.log("filterParamArray",this.filterParamArray)
        console.log("filterParamPaths",this.filterParamPaths)
      },
      //建立历史条件，为结果中查询服务
      buildHisFilterParams(params){
        let hisParam = {}
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
      //历史条件搜索
      historySearch(paramIndex){
        let hisParams = this.filterParamArray
        if(hisParams.length === 0 ||paramIndex<0 || paramIndex>=hisParams.length)
          return
        let newParam = hisParams[paramIndex]
        //console.log("newParam",newParam)
        this.filterParamArray.splice(paramIndex)
        this.filterParamPaths.splice(paramIndex)
        // console.log("newFilterParam",newFilterParam)
        newParam.filterParams = this.filterParamArray
        this.content = newParam.content
        this.category = newParam.category
        this.topicCodes = newParam.topicCodes
        newParam.withinSearchFlag = true
        newParam.advSearchFlag = true
        newParam.pageNo = this.ipagination.current;
        newParam.pageSize = this.ipagination.pageSize;
        this.loading = true;

        this.searchDoSend(newParam)
      },
      searchDoSend(params) {
        //console.log("params before send",params)
        postAction(this.url.list, params).then((res) => {
          if (res.success) {
            this.handleParamHistory(params)
            this.dataSource = res.result.kmSearchResultVOPage.records;
            this.itemList = this.filterParamPaths;
            console.log("this.filterParamPaths",this.filterParamPaths)
            // this.itemList = res.result.paramPath;
            if (res.result.kmSearchResultVOPage.total) {
              this.ipagination.total = res.result.kmSearchResultVOPage.total;
            } else {
              this.ipagination.total = 0;
            }
          } else {
            this.$message.error(res.message);
          }
          this.loading = false
        })
      },

      refreshDocDetail(){
        this.$refs.docDetailRef.refreshDocDetail()
      },
    }
  }
</script>

<style scoped>
  @import '~@assets/less/common.less';

</style>