<template>
    <a-layout class="layout">

      <a-layout-header class="searchHeader header-shadow"
                       :style="{background: kmConfig.HeaderBackgroundColor,width: '100%', height: '64px',position: 'fixed',top:'0',zIndex:'999'}">
        <SearchHeader  :title='pageTitle'/>
      </a-layout-header>

      <a-layout style="margin-top: 64px" >
        <a-layout-sider width="250" style="background: #f2f2f2">

        <div style="padding: 5px;overflow: auto" >
          <!-- 树-->
          <a-tree
            checkStrictly
            checkable
            v-model="topicCodesTree"
            @check="onCheck"
            :selectable="boolSelect"
            :tree-data="treeData"
          />
        </div>

      </a-layout-sider>

      <a-layout :style="{ marginLeft: '0px' }">

        <!--<a-layout-content>-->
        <a-layout-content :style="{ background: '#fff', padding: '2px', margin: 0, minHeight: '280px' }">
          <div :style="{ padding: '5px', background: '#fff' }">

            <!-- 详情区域-BEGIN -->
            <a-drawer
              :title="showDocDetail.title"
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
            <div style="background-color:white" >
              <!-- 操作按钮区域 -->
              <div class="table-operator">
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
              <div style="height: 100%;background-color: white" >

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
              <span style="color: #303133" v-html=" record.title "> </span>
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
            </div>
          </div>
        </a-layout-content>

      </a-layout>
      </a-layout>
    </a-layout>


</template>

<script>
  import {ajaxGetDictItems, getDictItemsFromCache} from '@/api/api'
  import {ACCESS_TOKEN} from "@/store/mutation-types"
  import {httpPostAction, getAction, downloadFileName, getActionPDF, postAction} from "../../../api/manage";
  import {AJeecgListMixin} from '@/mixins/AJeecgListMixin'
  import Vue from "vue";
  import IframePageContent from "../../../components/layouts/IframeFReportView";
  import ACol from "ant-design-vue/es/grid/Col";
  import SearchHeader from '../Common/SearchHeader'
  import DocDetail from '@views/km/Common/DocDetail.vue'
  import DocComments from '@views/km/Common/Comments.vue'

  export default {
    name: "RecommendTopicList",
    mixins: [AJeecgListMixin],
    components: { DocComments, DocDetail, ACol, IframePageContent,SearchHeader},
    data() {
      return {
        // boolCheckChange: true,
        // knowledgeTitle: "知识专题",
        pageTitle:"知识专题",
        checkedArray: [],
        topicCodesTree: {
          checked: [],
          halfChecked: [],
        },
        topicCodes:"",
        // businessTypes:"",
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
        width: '900',
        // hotTopicReportList: [],
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
        siteInfo: {},
        kmConfig: {},
        url: {
          rootList: "/sys/category/loadTreeRootRecommend",
          rootListAll: "/sys/category/loadTreeRoot",
          list: '/KM/kmDoc/searchDoc',
          // hotTopicReport: '/KM/kmSearchRecord/hotTopicReport',
          previewKmDoc: '/KM/kmDoc/previewKmDoc',
          downloadKmDoc: "/KM/kmDoc/downloadKmDoc",
          addFavouriteKmDoc: '/KM/kmDocFavourite/add',
          delFavouriteKmDoc: '/KM/kmDocFavourite/delete',
        },
        recommendFlag: false,
        showDocDetail:{},
        drawerVisible: false
      }
    },
    created() {
      this.siteInfo = this.$store.getters.siteInfo
      this.kmConfig = this.$store.getters.kmConfig
      // this.loadTree();
      // 调用初始化自定义table列表函数
      this.initColumns();
      this.initFilterDict();

      //设置全局token
      Vue.prototype.token = Vue.ls.get(ACCESS_TOKEN);
      window._CONFIG['token'] = Vue.prototype.token;
      this.initDict();
      let query = this.$route.query;
      if(this.empty(query))
        query = this.$route.params
      console.log("query", query);
      this.recommendFlag = query.recommendFlag
      this.topicCodes = query.topicCodes;
      this.loadTree();
      if(this.topicCodes!=null){
        this.topicCodesTree=query.topicCodesTree;
        this.knowledgeTitle=query.knowledgeTitle;
        this.searchDocFun(0);
      }
      this.changeTitle(this.pageTitle)
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
      //打开文档详情
      showDrawer(record) {
        this.showDocDetail = record
        this.drawerVisible = true
      },
      onDrawerClose() {
        this.drawerVisible = false
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

      // 加载树节点，获取树数据
      loadTree() {
        let params = {
          async: false,
          pcode: ""
        };
        let topicListUrl = this.url.rootListAll
        if(this.recommendFlag)
          topicListUrl = this.url.rootList

        getAction(topicListUrl, params).then(res => {
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
          console.log("checkedKeys", checkedKeys);
          console.log("checkedNodes", checkedNodes);
          let checkKeys = checkedKeys.checked[1];
          console.log("checkKeys",checkKeys)
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
        // console.log("tempArray", tempArray);
        checkedKeys.halfChecked = tempArray;
        let checkedTitle = checkedNodes.checkedNodes;

        console.log("checkedTitle", checkedTitle);
        if (!checkedTitle[0]) {
          // this.knowledgeTitle = "知识专题"
          //this.$message.warning("错误的专题参数!");
          this.dataSource=null;
          this.ipagination.total = 0;
          this.topicCodes = null;
        }
        else {
          let param = this.getQueryParams();//查询条件
          this.topicCodes = checkedTitle[0].data.props.code;
          param["topicCodes"] = this.topicCodes.split(',');
          this.searchDocFun(param);
        }

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
          //do nothing
        }
        params["advSearchFlag"] = 0;
        if( this.topicCodes!=null){
          params["topicCodes"]=this.topicCodes.split(',');
        }
        // 判断参数是否为空
        if (!this.empty(params)) {

          params.withinSearchFlag = '0';
          params.field = this.getQueryField();
          params.pageNo = this.ipagination.current;
          params.pageSize = this.ipagination.pageSize;
          this.loading = true;
          postAction(this.url.list, params).then((res) => {
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
          this.$message.info("请选择专题");
        }
      },

      //  判断对象是否为空
      empty(obj) {
        for (let key in obj) {
          return false;
        }
        return true;
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
        this.PDFurl =   this.url.previewKmDoc + "?docId=" + record.id;
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
      refreshDocDetail(){
        this.$refs.docDetailRef.refreshDocDetail()
      },
    }
  }
</script>

<style scoped>
  @import '~@assets/less/common.less';
</style>