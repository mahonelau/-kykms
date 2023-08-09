<template>
  <a-layout class="layout">

    <a-layout-header  class="searchHeader" style=" background-color: #1a53ba;width: 100%; height: 60px" >
      <SearchHeader  :title='pageTitle'/>
    </a-layout-header>

    <a-layout-content :style="{ background: '#fff',  minHeight: '680px' }">

      <div :bordered="false" :style="{backgroundColor: '#f2f2f2',height: '100%'}">
        <!--<SearchHeader/>-->
        <div  :style="{backgroundColor: '#1a53ba',padding: '10px',minWidth: '900px',height: '150px'}">
      <!-- 查询区域 -->
      <div class="table-page-search-wrapper" >
        <!--<p></p>-->
        <a-form layout="inline">
          <a-row>
            <a-col :span="24" style="text-align: center;">
              <div style="display:inline-block">
                <div style="float: left">
                  <a-input style="width: 800px;" size="large" placeholder="标题、关键字、全文" v-model="content"
                           @pressEnter="pressEnterFun">

                    <a-icon slot="suffix" @click="searchDocFun" type="search" style="color:#1890FF;fontSize:22px"/>
                  </a-input>
                  <div class="checkbox" style="width: 750px;text-align: center;margin-top: 10px;margin-left: 20px">
                    <a-checkbox-group :options="options" v-model="checkboxVuale" @change="onChange"/>
                  </div>
                </div>
                <div style="float: left;width: 100px;text-align: left;margin-top: -2px">
                  <span style="color: white;margin-left: 20px">
                   <span>全文检索</span>
                      <a-checkbox  class="checkbox" v-model="boolCheckChange" style="margin-left: 8px" @change="onCheckChange" />
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
    <div style="background-color:#f2f2f2">
      <a-form layout="inline">
        <a-row>
          <a-col :xl="17" :lg="15" :md="14" :sm="24" style="background-color: #f2f2f2;padding: 15px">
            <div style="padding: 0; box-shadow:1px 2px 3px 2px #ccc">
              <!--<a-col style="background-color: #108ee9">-->
              <!--<h3 style="color: white;margin-left: 20px;padding:5px"><b>新入库文档</b></h3>-->
              <!--</a-col>-->
              <a-table
                style="background-color: white"
                ref="docTable"
                size="middle"
                rowKey="id"
                :columns="columns"
                :dataSource="dataSource"
                :pagination="ipagination"
                :loading="loading"
                @change="handleTableChange">

            <span slot="docTitle" slot-scope="text,record">
              <span @click="previewKmDoc(record)"><a style="color: #303133">{{ record.title}}</a></span>
           </span>


                // 添加自定义列表插槽
                <div slot="filterDropdown">
                  <a-card style="width: 350px">
                    <a-checkbox-group @change="onColSettingsChange" v-model="settingColumns"
                                      :defaultValue="settingColumns">
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
                   <a-icon v-if="record.downloadFlag==1" type="download" title="下载" @click="downloadKmDoc(record)"
                           :style="{ fontSize: '16px', color: '#1890FF'}"/>
                   <a-icon v-else type="download" title="禁止下载"
                           :style="{ fontSize: '16px', color: '#909399'}"/>
                   <a-divider type="vertical"/>
                   <a-icon v-if="record.favourite==0" type="star" title="收藏" @click="addFavouriteKmDoc(record)"
                           :style="{ fontSize: '18px', color: '#1890FF', }"/>
                   <a-icon v-else type="star" theme="filled" title="取消收藏" @click="delFavouriteKmDoc(record)"
                           :style="{ fontSize: '18px', color: '#1890FF', }"/>
               </span>
              </a-table>
            </div>
          </a-col>
          <a-col :xl="7" :lg="9" :md="10" :sm="24" style="background-color: #f2f2f2;padding: 15px;min-width: 350px">
            <div style="background-color: white;padding: 0; box-shadow:1px 2px 3px 2px #ccc">
              <a-form layout="inline" style="min-height: 10px">
                <a-row>
                  <a-col style="background-color: #108ee9">
                    <h3 style="color: white;margin-left: 20px;padding:5px">
                      <b>业务</b>
                    </h3>
                  </a-col>
                  <a-col v-for="(item,index) in defaultBusinessTypeList" :xl="12" :lg="12" :md="12" :sm="24" :key="index">
                    <h4 style="text-align: left;margin-left: 20px">
                      <span @click="searchBusinessType(item.value)"><a style="color: #303133">{{item.text}}</a></span>
                    </h4>
                  </a-col>
                </a-row>
              </a-form>
            </div>

          </a-col>

        </a-row>
      </a-form>

      <b-j-modal :title="title"
                 :width="width"
                 :visible="visible"
                 @cancel="handleCancel"
                 cancelText="关闭"
                 :okButtonProps="{ class:{'jee-hidden': true} }">
        <div>
          <div>
            <p-d-f-modal iframe-i-d="pdfIDde" :p-d-furl="PDFurl" :iframeWidth="width"/>
          </div>
        </div>
      </b-j-modal>

      <a-layout-footer style="padding: 0">
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
  import {httpPostAction, getAction, downloadFileName, getActionPDF} from "../../../api/manage";
  import {JeecgListMixin} from '@/mixins/JeecgListMixin'
  import Vue from "vue";
  import IframePageContent from "../../../components/layouts/IframeFReportView";
  import GlobalFooter from '@/components/page/GlobalFooter'
  import ARow from "ant-design-vue/es/grid/Row";
  import SearchHeader from '../Common/SearchHeader'

  export default {
    name: "defaultDocSearch",
    mixins: [JeecgListMixin],
    components: {ARow, IframePageContent,GlobalFooter,SearchHeader},
    data() {
      return {
        boolCheckChange:true,
        checkedArray:[],
        topicCodesTree: {
          checked: [],
          halfChecked: [],
        },
        boolSelect:false,
        treeData:[],
        knowledgeTitle:"知识专题",
        confirmLoading: false,
        PDFurl: '',
        visible: false,
        pdfLoading: false,
        pdfShow: true,
        labelCol: {
          xs: {span: 24},
          sm: {span: 5},
        },
        wrapperCol: {
          xs: {span: 24},
          sm: {span: 16},
        },
        title: "预览",
        width: '900',
        defaultBusinessTypeList: [],
        hotTopicReportList: [],
        docDataSource: [],
        loadedRatio: 0,

        topMenuStyle: {
          headerIndexLeft: {},
          topNavHeader: {},
          headerIndexRight: {},
          topSmenuStyle: {}
        },
        pageTitle: "",
        //表头
        columns: [],
        //列设置
        settingColumns: [],
        //要filter的字段
        filterDictCode: 'km_dict_source',
        //  过滤字段数据
        filterOptions: [],
        //列定义
        defColumns: [
          {
            title: '#',
            dataIndex: '',
            key: 'rowIndex',
            width: 40,
            align: "center",
            scopedSlots: {customRender: 'indexNum'},
          },
          {
            title: '标题',
            align: "left",
            dataIndex: 'title',
            scopedSlots: {customRender: 'docTitle'},
          },
          {
            title: '文档来源',
            align: "center",
            dataIndex: 'source_dictText',
            width: 150,
            filters: [],
            // scopedSlots: {
            //   filterDropdown: 'filterSource',
            //   filterIcon: 'filterIcon'
            // },
          },
          {
            title: '发文时间',
            align: "center",
            sorter: true,
            dataIndex: 'pubTimeTxt',
            width: 95,
          },
          {
            title: '操作',
            dataIndex: 'action',
            align: "center",
            width: 70,
            scopedSlots: {
              filterDropdown: 'filterDropdown',
              filterIcon: 'filterIcon',
              customRender: 'action'
            }
          },


        ],
        //  分类数据
        options: [],
        // 输入框输入的内容
        content: '',
        // 多选组件使用的变量
        checkboxVuale: [],
        checkedValues: '',

        itemList: [],
        // 要加载多选的字典
        dictCode: 'km_dict_category',
        // 请求的url地址
        url: {
          rootList: "/sys/category/loadTreeRoot",
          recentlyList: '/KM/kmDocVisitRecord/recentlyVisitDocs',
          defaultBusinessTypeList: '/KM/HomePage/listBusinessType',
          hotTopicReport: '/KM/HomePage/listRecommendTopic',
          list: '/KM/kmDoc/listRecently',
          previewKmDoc: '/KM/kmDoc/previewKmDoc',
          downloadKmDoc: "/KM/kmDoc/downloadKmDoc",
          addFavouriteKmDoc: '/KM/kmDocFavourite/add',
          delFavouriteKmDoc: '/KM/kmDocFavourite/delete',
        }
      }
    },
    created() {
      this.loadTree();
      //设置全局token
      Vue.prototype.token = Vue.ls.get(ACCESS_TOKEN);
      window._CONFIG['token'] = Vue.prototype.token;
      this.hotTopicReportFun();
      this.loadDefaultBusinessTypeFunc();
      this.initDict();
      this.initFilterDict();

      // 调用初始化自定义table列表函数
      this.initColumns();

      this.changeTitle("普通检索");

    },
    methods: {
      // 加载filter字段选项
      initFilterDict() {
        this.filterOptions = [];

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
      onCheckChange(e){
        this.boolCheckChange=e.target.checked
        console.log(`checked = ${e.target.checked}`);
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
        if(checkedKeys.checked.length>1){
         let checkKeys=checkedKeys.checked[1];
          checkedKeys.checked=[];
          checkedKeys.checked.push(checkKeys);
          let checkNodesTitle= checkedNodes.checkedNodes[1];
          checkedNodes.checkedNodes=[];
          checkedNodes.checkedNodes.push(checkNodesTitle);
        }
        console.log("checkedKeys",checkedKeys);
        console.log("checkedNodes",checkedNodes);
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
            if (checkedTitle[i].data.props.title != null) {
              this.knowledgeTitle = this.knowledgeTitle + "," + checkedTitle[i].data.props.title;
            }
          }
        }
        if(this.knowledgeTitle ===""){
          this.knowledgeTitle="知识专题"
        }
        let topicCodes=checkedTitle[0].data.props.code;
        this.knowledgeTitleFun(topicCodes,this.topicCodesTree,this.knowledgeTitle);
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
        this.checkboxVuale = checkedValues;
        console.log('checked = ', checkedValues);
      },
      // 按回车键触发方法
      pressEnterFun(e) {
        this.$nextTick(() => {
          this.searchDocFun();
        })
      },

      //专题检索
      knowledgeTitleFun(topicCodes,topicCodesTree,knowledgeTitle){
        let params={};
        params["topicCodes"]=topicCodes;
        params["topicCodesTree"]=topicCodesTree;
        params["knowledgeTitle"]=knowledgeTitle;
        // this.$router.push({name: 'RecommendTopicList',params:params});
        this.$router.push({name: 'docSearch',params:params});
      },

      // 检索
      searchDocFun() {
        let params = {};
        if (this.content !== "" && this.content != null) {
          params['content'] = this.content;
        }
        if (this.checkedValues !== "" && this.checkedValues != null) {
          params['category'] = this.checkedValues;
        }
        // 判断参数是否为空
        if (this.empty(params)) {
          params['column']="_score";
          params['order']="desc";
          params['boolCheckChange']=this.boolCheckChange;
          if (params['content'] != null) {
            params['content'] = params['content'].replace(/，/g, ',');
          }
          this.$router.push({name: 'docSearch', params: params});
        } else {
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
      // 热门专题
      hotTopicReportFun() {
        getAction(this.url.hotTopicReport).then(res => {
          if (res.success) {
            this.hotTopicReportList = res.result;
          } else {
            //this.$message.error("热门专题加载失败");
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
              // that.loadData();
              that.onClearSelected();
            }
          });
        }
      },

      //访问业务类型
      searchBusinessType(value){
        let params = {};
        params['businessTypes'] = value;
        this.$router.push({name: 'docSearch', params: params});
      },

      // 显示预览窗口，初始化
      previewKmDoc(record) {
        //this.PDFurl = window._CONFIG['domianURL'] + this.url.previewKmDoc + "?docId=" + record.id;
        this.PDFurl =  this.url.previewKmDoc + "?docId=" + record.id;
        this.title ='预览 - ' + record.title;
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



    }
  }
</script>

<style>
  @import "~@assets/less/common.less";

</style>

