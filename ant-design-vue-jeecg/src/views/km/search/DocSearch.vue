<template>
  <a-layout class="layout">

    <a-layout-header  class="searchHeader" style=" background-color: #1a53ba;width: 100%; height: 60px" >

      <SearchHeader :title='pageTitle'/>

    </a-layout-header>
    <a-layout-content :style="{ background: '#fff',  minHeight: '680px' }">

      <div :bordered="false" :style="{backgroundColor: '#f2f2f2',height: '100%'}">
        <!--<SearchHeader/>-->
        <div  :style="{backgroundColor: '#1a53ba',padding: '10px',minWidth: '900px',height: '150px'}">
      <!-- 查询区域 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline">
          <a-row>
            <a-col :span="24" style="text-align: center;">
              <div style="display:inline-block">
                <div style="float: left">
                  <a-input style="width: 800px;" size="large" placeholder="标题、关键字、全文" v-model="content"
                           @pressEnter="pressEnterFun">

                    <a-icon slot="suffix" @click="searchDocFun('0')" type="search" style="color:#1890FF;fontSize:22px"/>
                  </a-input>
                  <div class="checkbox" style="width: 750px;text-align: center;margin-top: 10px;margin-left: 20px">
                    <a-checkbox-group :options="options" v-model="checkboxVuale" @change="onChange"/>
                  </div>
                </div>
                <div style="float: left;width: 100px;text-align: left;margin-top: -2px">
                  <span style="color: white;margin-left: 20px">
                   <span>全文检索</span>
                   <a-checkbox class="checkbox" v-model="boolCheckChange" style="margin-left: 8px"
                               @change="onCheckChange"/>
                  </span>
                </div>
              </div>
            </a-col>
          </a-row>
        </a-form>
      </div>
      <!-- 查询区域-END -->
    </div>
    <!-- 表格区域 -->
    <div style="background-color:white;margin: 15px;padding: 15px">
      <!-- 操作按钮区域 -->
      <div class="table-operator">
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
        <div  class="ant-alert ant-alert-info" style="margin-bottom: 15px;color: #303133;">
          <span v-for="(item,index) in itemList" :key="index">
            <span><b v-if="index==0">检索范围: </b><span v-if="index==itemList.length-1">{{item}}</span> <span v-else>{{item}}<span
              style="font-weight: bold;color: red">-></span>  </span></span>
          </span>
        </div>

        <!--<div v-else class="ant-alert ant-alert-info" style="margin-bottom: 15px;color: #303133;">-->
          <!--<span v-for="(item,index) in defaultBusinessTypeList" :xl="4" :lg="4" :md="4" :sm="4" :key="index">-->
            <!--<span style="text-align: left;margin-left: 10px">-->
              <!--<span @click="searchBusinessType(item.value)">-->
                <!--<a v-if="item.value === businessTypes" style="color: #303133;font-weight: bold">{{item.text}}</a>-->
                <!--<a v-else style="color: #303133">{{item.text}}</a></span>-->
            <!--</span>-->
          <!--</span>-->
        <!--</div>-->

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
                  :style="{ fontSize:'18px',color:  '#108ee9',minWidth:'35px'}"/>

          <span slot="indexNum" slot-scope="text, record,index">
                  <p>{{index+1+(ipagination.current-1)*ipagination.pageSize}}</p>
               </span>
          <span slot="action" slot-scope="text, record">

           <!--<a-icon type="read" title="预览" @click="previewKmDoc(record)"-->
            <!--:style="{ fontSize: '18px', color: '#1890FF', }"/>-->


          <a-icon v-if="record.downloadFlag==1" type="download" title="下载" @click="downloadKmDoc(record)"
                  :style="{ fontSize: '18px', color: '#1890FF'}"/>
          <a-icon v-else type="download" title="禁止下载" :style="{ fontSize: '18px', color: '#909399'}"/>

          <a-divider type="vertical"/>

          <a-icon v-if="record.favourite==0" type="star" title="收藏" @click="addFavouriteKmDoc(record)"
                  :style="{ fontSize: '18px', color: '#1890FF', }"/>
          <a-icon v-else type="star" theme="filled" title="取消收藏" @click="delFavouriteKmDoc(record)"
                  :style="{ fontSize: '18px', color: '#1890FF', }"/>

        </span>

          <span slot="docTitle" slot-scope="text,record">
           <span @click="previewKmDoc(record,true)"><a style="color: #303133">
             <span v-html=" record.title "></span></a></span>
        </span>
          <!--<span slot="test" slot-scope="text, record">-->
          <!--<span v-html="record.title"></span>-->
          <!--</span>-->

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

    </div>
  </div>
    </a-layout-content>
  </a-layout>
</template>

<script>
  import {ajaxGetDictItems, getDictItemsFromCache} from '@/api/api'
  import {ACCESS_TOKEN} from "@/store/mutation-types"
  import {httpPostAction, getAction, downloadFileName, getActionPDF} from "../../../api/manage";
  import {AJeecgListMixin} from '@/mixins/AJeecgListMixin'
  import Vue from "vue";
  import IframePageContent from "../../../components/layouts/IframeFReportView";
  import SearchHeader from '../Common/SearchHeader'

  export default {
    name: "docSearch",
    mixins: [AJeecgListMixin],
    components: {IframePageContent,SearchHeader},
    data() {
      return {
        pageTitle:"检索结果",
        boolCheckChange: true,
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
        checkboxVuale: [],
        title: "预览",
        width: 900,
        // hotTopicReportList: [],
        defaultBusinessTypeList: [],
        docDataSource: [],
        loadedRatio: 0,
        isSearchResult: false,
        filterOptions:[],
        //要filter的字段
        filterDictCode: 'km_dict_source',
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
        options: [],
        content: '',
        checkedValues: '',
        itemList: [],
        // 要加载多选的字典
        dictCode: 'km_dict_category',

        url: {
          rootList: "/sys/category/loadTreeRoot",
          list: '/KM/kmDoc/searchDoc',
          hotTopicReport: '/KM/kmSearchRecord/hotTopicReport',
          defaultBusinessTypeList: '/KM/HomePage/listBusinessType',
          previewKmDoc: '/KM/kmDoc/previewKmDoc',
          downloadKmDoc: "/KM/kmDoc/downloadKmDoc",
          addFavouriteKmDoc: '/KM/kmDocFavourite/add',
          delFavouriteKmDoc: '/KM/kmDocFavourite/delete',
        }
      }
    },
    created() {
      this.loadTree();
      // 调用初始化自定义table列表函数
      this.initColumns();
      this.initFilterDict();

      //设置全局token
      Vue.prototype.token = Vue.ls.get(ACCESS_TOKEN);
      window._CONFIG['token'] = Vue.prototype.token;
      // this.hotTopicReportFun();
      this.initDict();
      let params = this.$route.params;
      console.log("params：", params);
      this.topicCodes=params.topicCodes;
      this.businessTypes=params.businessTypes;

      // if(this.businessTypes)
      //   this.loadDefaultBusinessTypeFunc()

      if(this.topicCodes==null){
        if (Object.keys(params).length > 0) {
          this.boolCheckChange = params.boolCheckChange;
          this.content = params.content;
          let replaceTitle=this.content;
          if(replaceTitle)
            replaceTitle = replaceTitle.replace(/，/g, ',');
          // replaceTitle = replaceTitle.replace(/\[/g, '%5B');
          // replaceTitle = replaceTitle.replace(/\]/g, '%5D');
          if(this.boolCheckChange){
            params["content"] = replaceTitle;
          }else{
            delete params["content"];
            delete params["boolCheckChange"];
          }
          params["title"] = replaceTitle;
          params["keywords"] = replaceTitle;
          params["advSearchFlag"] = 0;

          if (params.category !== "" && params.category != null) {
            this.checkboxVuale = params.category.split(",");
            this.checkedValues = params.category
          }
          this.searchFun(params);
        }
      }else{
        this.topicCodesTree=params.topicCodesTree;
        this.knowledgeTitle=params.knowledgeTitle;
        let param={};
        param["topicCodes"]=this.topicCodes;
        this.searchFun(param);
      }
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
    methods: {
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

      // 是否全文检索
      onCheckChange(e) {
        this.boolCheckChange = e.target.checked
        console.log(`checked = ${e.target.checked}`);
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

      // 树节点选择触发
      onCheck(checkedKeys, checkedNodes) {
        if (checkedKeys.checked.length > 1) {
          let checkKeys = checkedKeys.checked[1];
          checkedKeys.checked = [];
          checkedKeys.checked.push(checkKeys);
          let checkNodesTitle = checkedNodes.checkedNodes[1];
          checkedNodes.checkedNodes = [];
          checkedNodes.checkedNodes.push(checkNodesTitle);
        }
        console.log("checkedKeys", checkedKeys);
        console.log("checkedNodes", checkedNodes);
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
        this.knowledgeTitle = "";
        for (let i = 0; i < checkedTitle.length; i++) {
          if (this.knowledgeTitle === "") {
            this.knowledgeTitle = checkedTitle[i].data.props.title;
          } else {
            if (checkedTitle[i].data.props.data != null) {
              this.knowledgeTitle = this.knowledgeTitle + "," + checkedTitle[i].data.props.title;
            }
          }
        }
        if (this.knowledgeTitle === "") {
          this.knowledgeTitle = "知识专题"
          this.topicCodes = null;
        }
        let param = this.getQueryParams();//查询条件互相影响
        // let param={};
        this.topicCodes=checkedTitle[0].data.props.code;
        param["topicCodes"]=this.topicCodes;
        this.searchFun(param);
        // this.businessTypes=null;

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
        //权限过滤（列权限控制时打开，修改第二个参数为授权码前缀）
        //this.defColumns = colAuthFilter(this.defColumns,'testdemo:');

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

      searchFun(params) {
        getAction(this.url.list, params).then((res) => {
          if (res.success) {
            this.dataSource = res.result.kmSearchResultVOPage.records;
            this.itemList = res.result.paramPath;
            if (res.result.kmSearchResultVOPage.total) {
              this.ipagination.total = res.result.kmSearchResultVOPage.total;
            } else {
              this.ipagination.total = 0;
            }
          } else {
            this.$message.error("检索失败");
          }
        })
      },

      loadData() {
        this.searchDocFun('2');
      },

      // 加载 分类数据
      initDict() {
        this.options = [];

        //优先从缓存中读取字典配置
        if (getDictItemsFromCache(this.dictCode)) {
          let options = getDictItemsFromCache(this.dictCode)
          options.forEach((item, index) => {
            let person = {};
            person.label = item.title;
            person.value = item.value;
            this.options.push(person);
          });
          return
        }

        //根据字典Code, 初始化字典数组
        ajaxGetDictItems(this.dictCode, null).then((res) => {
          if (res.success) {
            let options = res.result
            options.forEach((item, index) => {
              let person = {};
              person.label = item.title;
              person.value = item.value;
              this.options.push(person);
            });
          }
        })


      },

      // 多选框选择触发
      onChange(checkedValues) {
        this.checkedValues = checkedValues.toString();
        console.log('checked = ', checkedValues);
      },
      // 按回车键触发方法
      pressEnterFun(e) {
        this.$nextTick(() => {
          this.searchDocFun('0');
        })
      },
      // 检索
      searchDocFun(type) {
        // this.businessTypes=null;

        let params = {};
        params = this.getQueryParams();//查询条件
        if (type === '0') {
          this.isSearchResult = false;
          this.ipagination.current = 1;
          //启用用过滤
          this.defColumns[2].filters =this.filterOptions;
        } else if (type === '1') {
          this.isSearchResult = true;
          this.ipagination.current = 1;
          //禁用过滤
          this.defColumns[2].filters = [];
        }

        this.loading = true;

        if (this.content !== "" && this.content != null) {
          let replaceTitle=this.content
          if(replaceTitle)
            replaceTitle = replaceTitle.replace(/，/g, ',');
          // replaceTitle = replaceTitle.replace(/\[/g, '%5B');
          // replaceTitle = replaceTitle.replace(/\]/g, '%5D');
          if(this.boolCheckChange){
            params["content"] = replaceTitle;
          }else{
            delete params["content"];
          }
          params["title"] = replaceTitle;
          params["keywords"] = replaceTitle;
        }
        params["advSearchFlag"] = 0;
        if (this.checkedValues !== "" && this.checkedValues != null) {
          params['category'] = this.checkedValues;
        }
        if( this.topicCodes!=null){
          params['topicCodes']=this.topicCodes;
        }
        if(this.businessTypes!=null){
          params['businessTypes']=this.businessTypes;
        }

        // 判断参数是否为空
        if (this.empty(params)) {
          if (this.isSearchResult) {
            params.withinSearchFlag = '1';
          }
          params.field = this.getQueryField();
          params.pageNo = this.ipagination.current;
          params.pageSize = this.ipagination.pageSize;
          this.loading = true;
          getAction(this.url.list, params).then((res) => {
            if (res.success) {
              this.dataSource = res.result.kmSearchResultVOPage.records;
              this.itemList = res.result.paramPath;
              if (res.result.kmSearchResultVOPage.total) {
                this.ipagination.total = res.result.kmSearchResultVOPage.total;
              } else {
                this.ipagination.total = 0;
              }
            } else {
              this.$message.error("检索失败");
            }
            this.loading = false;
          })
        } else {
          this.loading = false;
          this.$message.info("请输入搜索条件");
        }
      },

      loadDefaultBusinessTypeFunc(){
        getAction(this.url.defaultBusinessTypeList).then(res => {
          if (res.success) {
            this.defaultBusinessTypeList = res.result;
          } else {
            this.$message.error("业务加载失败");
          }
        })
      },

      //  判断对象是否为空
      empty(obj) {
        for (let key in obj) {
          return true;
        }
        return false;
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
              that.onClearSelected();
            }
          });
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
            //this.loadData();
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

      // 显示预览窗口，初始化
      previewKmDoc(record) {
        // this.PDFurl = window._CONFIG['domianURL'] + this.url.previewKmDoc + "?docId=" + record.id;
        this.PDFurl =  this.url.previewKmDoc + "?docId=" + record.id;
        this.visible = true;
        this.pdfLoading = true;
        this.pdfShow = true;
      },
      //关闭预览窗口
      handleCancel() {
        this.visible = false;
      },

      //下载文件
      downloadKmDoc(record) {
        this.$notification.success({
          message: '文件开始下载...',
          duration: 1,
        });
        downloadFileName(this.url.downloadKmDoc, {docId: record.id})
      },

      // 返回
      backHomepage() {
        this.$router.push('/defaultDocSearch');
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
      // 点击个人登录，跳转页面
      login() {
        this.$router.push('/dashboard/analysis');
      },

    }
  }
</script>

<style scoped>
  @import '~@assets/less/common.less';
</style>