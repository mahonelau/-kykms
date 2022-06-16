<template>
  <a-layout class="layout">

  <a-layout-header  class="searchHeader" style=" background-color: #1a53ba;width: 100%; height: 40px" >

    <SearchHeader :title="pageTitle"/>

  </a-layout-header>
  <a-layout-content :style="{ background: '#fff',  minHeight: '680px' }">


  <div :bordered="false" style="background-color: #f2f2f2;height: 100%">
    <div style="background-color: white">

      <div style="background-color: #1a53ba;padding: 20px;">

        <!--<SearchHeader/>-->
      </div>

      <!-- 查询区域 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline">
          <a-row>

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
                    <a-form-item label="来源" style="margin-left: 28px;margin-top: -10px">
                      <j-multi-select-tag type="list_multi" placeholder="请选择来源" v-model="advance.source"
                                          dictCode="km_dict_source"></j-multi-select-tag>
                    </a-form-item>
                  </a-col>
                  <a-col :xl="14" :lg="14" :md="16" :sm="24">
                    <a-form-item label="文号" style="margin-left: 28px;margin-top: -10px">
                      <a-input placeholder="请输入文号" v-model="advance.fileNo"></a-input>
                    </a-form-item>
                  </a-col>
                  <a-col :xl="14" :lg="14" :md="16" :sm="24">
                    <a-form-item label="发文时间" style="margin-top: -10px">
                      <j-dict-select-tag type="list" placeholder="请选择开始时间" v-model="advance.pubTimeStart"
                                         style="width:48%"
                                         dictCode="km_dict_year"></j-dict-select-tag>
                      <span>~</span>
                      <j-dict-select-tag type="list" placeholder="请选择结束时间" v-model="advance.pubTimeEnd"
                                         style="width: 49%"
                                         dictCode="km_dict_year"></j-dict-select-tag>
                    </a-form-item>
                  </a-col>
                  <a-col :xl="14" :lg="14" :md="16" :sm="24">
                    <a-form-model-item label="版本状态" style="margin-top: -10px">
                      <j-multi-select-tag type="list_multi" v-model="advance.versions" :trigger-change="true"
                                          placeholder="请选择版本状态" dictCode="km_dict_versions"></j-multi-select-tag>
                    </a-form-model-item>
                  </a-col>
                  <a-col :xl="14" :lg="14" :md="16" :sm="24">
                    <a-form-model-item label="涉及业务" style="margin-top: -10px">
                      <j-multi-select-tag type="list_multi" v-model="advance.businessTypes" :trigger-change="true"
                                          placeholder="请选择涉及业务" dictCode="km_dict_business"></j-multi-select-tag>
                    </a-form-model-item>
                  </a-col>
                  <a-col :xl="14" :lg="14" :md="16" :sm="24" style="margin-top: -10px;margin-bottom: 10px">
                    <div style="width: 600px;margin-left: 80px">
                      <a-checkbox-group v-model="category" :options="options" @change="advanceOnChange"/>
                    </div>
                  </a-col>

                  <a-col :xl="14" :lg="14" :md="16" :sm="48">
                  <span style="float: left;overflow: hidden;align: center;margin-left: 80px" class="table-page-search-submitButtons">
                   <a-button type="primary" @click="advanceSearch('0')" style="width: 100px;margin-left: 80px">检索</a-button>
                    <a-button @click="advanceSearch('1')" style="margin-left: 60px">结果中检索</a-button>
                   <!--<a-button @click="advanceReset" style="margin-left: 30px">重置条件</a-button>-->
                    <!--<a-button @click="defaultSearch" style="margin-left: 30px">返回</a-button>-->
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

      <!-- table区域-begin -->
      <div>
        <div v-if="itemList.length>0&&itemList!=''" class="ant-alert ant-alert-info" style="margin-bottom: 15px;">
          <span v-for="(item,index) in itemList" :key="index">
            <span><b v-if="index==0">检索范围: </b><span v-if="index==itemList.length-1">{{item}}</span> <span v-else>{{item}} <span
              style="font-weight: bold;color: red">-></span> </span></span>
          </span>
        </div>
        <div v-else class="ant-alert ant-alert-info" style="margin-bottom: 15px;">
          <span><b>搜索：</b></span>
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

           <!--<a-icon type="read" title="预览" @click="previewKmDoc(record,true)"-->
                   <!--:style="{ fontSize: '18px', color: '#1890FF', }"/>-->

           <!--<a-divider type="vertical"/>-->

           <a-icon v-if="record.downloadFlag==1" type="download" title="下载" @click="downloadKmDoc(record)"
                   :style="{ fontSize: '16px', color: '#1890FF'}"/>
            <a-icon v-else type="download" title="禁止下载" :style="{ fontSize: '16px', color: '#909399'}"/>

            <a-divider type="vertical"/>

          <a-icon v-if="record.favourite==0" type="star" title="收藏" @click="addFavouriteKmDoc(record)"
                  :style="{ fontSize: '18px', color: '#1890FF', }"/>
          <a-icon v-else type="star" theme="filled" title="取消收藏" @click="delFavouriteKmDoc(record)"
                  :style="{ fontSize: '18px', color: '#1890FF', }"/>
        </span>

          <span slot="docTitle" slot-scope="text,record">
           <span  @click="previewKmDoc(record,true)"><a style="color: #303133">
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

        <div>
          <div>
            <p-d-f-modal :p-d-furl="PDFurl" :iframeWidth="width"/>
          </div>
        </div>
      </b-j-modal>

    </div>
  </div>
  </a-layout-content>
  </a-layout>
</template>

<script>
  import {ACCESS_TOKEN} from "@/store/mutation-types"
  import {ajaxGetDictItems, getDictItemsFromCache} from '@/api/api'
  import {httpPostAction, getAction, downloadFileName} from "../../../api/manage";
  import {AJeecgListMixin} from '@/mixins/AJeecgListMixin'
  import Vue from "vue";
  import SearchHeader from '../Common/SearchHeader'

  export default {
    name: "AdvancedSearch",
    mixins: [AJeecgListMixin],
    components: {SearchHeader},
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
        pageTitle:"高级检索",
        checkedArray: [],
        isSearchResult: false,
        // a-tree 属性
        boolSelect: false,
        category: [],
        loadedRatio: 0,
        dictCode: 'km_dict_category',
        options: [],
        rangePickerArr: null,
        topicCodesTree: {
          checked: [],
          halfChecked: [],
        },
        title: "预览",
        width: 900,
        treeData: [],
        replaceFields: {
          key: 'id',
          parentId: "pid",
          title: "name",
        },

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
        url: {
          list: '/KM/kmDoc/searchDoc',
          previewKmDoc: '/KM/kmDoc/previewKmDoc',
          downloadKmDoc: "/KM/kmDoc/downloadKmDoc",
          rootList: "/sys/category/loadTreeRoot",
          childList: "/sys/category/childList",
          advanceSearchDoc: '/KM/kmDoc/searchDoc',
          addFavouriteKmDoc: '/KM/kmDocFavourite/add',
          delFavouriteKmDoc: '/KM/kmDocFavourite/delete',
        }
      }
    },
    created() {
      //设置全局token
      Vue.prototype.token = Vue.ls.get(ACCESS_TOKEN);
      window._CONFIG['token'] = Vue.prototype.token;
      this.initDict();
      this.loadTree();
      // 调用初始化自定义table列表函数
      this.initColumns();

      this.changeTitle(this.pageTitle);
      // this.initFilterDict();
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

      // 更改页面标题
      changeTitle(title) {
        let projectTitle = "科亿知识库"
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

      loadData() {
        this.advanceSearch('0');
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
        this.PDFurl = window._CONFIG['domianURL'] + this.url.previewKmDoc + "?docId=" + record.id;
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
      // 时间联动组件方法
      onDateChange: function (value, dateString) {
        this.advance.pubTimeStart = dateString[0];
        this.advance.pubTimeEnd = dateString[1];
        this.rangePickerArr = [this.advance.pubTimeStart, this.advance.pubTimeEnd];
      },
      // 加载多选框数据
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

      // 选中多选框触发
      advanceOnChange(checkedValues) {
        this.category = checkedValues;
        this.advance.category = this.category.toString();
      },
      // 返回上普通搜索页面
      defaultSearch() {
        this.$router.push('/defaultDocSearch');
      },
      // 高级查询条件重置
      advanceReset() {
        this.category = [];
        this.advance = {};
        this.topicCodesTree = [];
        this.rangePickerArr = null;
        this.itemList = [];
      },

      // 高级查询
      advanceSearch(type) {

        if (type === '0') {
          this.isSearchResult = false;
          //this.ipagination.current = 1;
        } else if (type === '1') {
          this.isSearchResult = true;
          //this.ipagination.current = 1;
        }
        for (let key in this.advance) {
          if (this.advance[key] === '' || this.advance[key] === null) {
            delete this.advance[key];
          }
          if (key === 'withinSearchFlag') {
            delete this.advance[key];
          }
          if (this.advance.keywords != null) {
            this.advance.keywords = this.advance.keywords.replace(/，/g, ',');

          }
        }

        let getParams = this.getQueryParams();
        console.log(getParams)
        let params = Object.assign(this.advance, getParams);

        if (this.empty(params)) {
          if (this.isSearchResult) {
            this.advance.withinSearchFlag = '1';
            //禁用过滤
            // console.log("禁用过滤");
            // this.defColumns[2].filters = [];
          }
          else {
            //启用过滤
            // console.log("启用过滤");
            // this.defColumns[2].filters = this.filterOptions;
            // console.log(this.filterOptions);

          }
          params.field = this.getQueryField();

          params["advSearchFlag"] = 1;

          params.pageNo = this.ipagination.current;
          params.pageSize = this.ipagination.pageSize;
          this.loading = true;

          getAction(this.url.advanceSearchDoc, params).then(res => {
            if (res.success) {
              this.dataSource = res.result.kmSearchResultVOPage.records;
              this.itemList = res.result.paramPath;
              console.log(res);
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
        this.$router.push('/defaultDocSearch');
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
    }
  }
</script>

<style scoped>
  @import '~@assets/less/common.less'

</style>