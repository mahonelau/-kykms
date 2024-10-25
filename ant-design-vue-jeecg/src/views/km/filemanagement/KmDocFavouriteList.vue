<template>
  <a-card :bordered="false">


    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="标题">
              <a-input placeholder="请输入标题" v-model="queryParam.title"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="分类">
              <j-dict-select-tag type="list" placeholder="请选择分类" v-model="queryParam.category"
                                 dictCode="km_dict_category"></j-dict-select-tag>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
<!--            <a-col :xl="6" :lg="7" :md="8" :sm="24">-->
<!--              <a-form-item label="编号">-->
<!--                <a-input placeholder="请输入编号" v-model="queryParam.serialNumber"></a-input>-->
<!--              </a-form-item>-->
<!--            </a-col>-->
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-model-item label="标签">
                <j-multi-select-tag type="list_multi" v-model="queryParam.businessTypeList" :trigger-change="true"
                                    placeholder="请选择标签" dictCode="km_dict_business"></j-multi-select-tag>
              </a-form-model-item>
            </a-col>

            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="关键字">
                <a-input placeholder="请输入关键字" v-model="queryParam.keywords"></a-input>
              </a-form-item>
            </a-col>

            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="文件类型">
                <a-input placeholder="请输入文件类型" v-model="queryParam.fileType"></a-input>
              </a-form-item>
            </a-col>

            <a-col :xl="12" :lg="14" :md="16" :sm="48">
              <a-form-item label="上传时间">
                <j-date :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择开始时间"
                        class="query-group-cust" v-model="queryParam.createTimeStart"></j-date>
                <span class="query-group-split-cust"></span>
                <j-date :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择结束时间"
                        class="query-group-cust" v-model="queryParam.createTimeEnd"></j-date>
              </a-form-item>
            </a-col>

          </template>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
              </a>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END ant-alert ant-alert-info -->
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
    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <!--      <a-button type="primary" icon="download" @click="handleExportXls('草稿文件夹')">导出</a-button>-->
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="3" @click="downloadKmDocBatch">
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
      <div v-if="selectedRowKeys.length > 0" class="ant-alert ant-alert-info" style="margin-bottom: 8px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{
        selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        :scroll="{x:true}"
        bordered
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
                :style="{ fontSize:'16px',color:  '#108ee9',minWidth:'25px'}"/>


        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无图片</span>
          <img v-else :src="getImgView(text)" height="25px" alt=""
               style="max-width:80px;font-size: 12px;font-style: italic;"/>
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无文件</span>
          <a-button
            v-else
            :ghost="true"
            type="primary"
            icon="download"
            size="small"
            @click="downloadFile(text)">
            下载
          </a-button>
        </template>
        <span slot="action" slot-scope="text, record">
          <a-space>
          <a-icon type="read" title="预览" @click="previewKmDoc(record)"
                  :style="{ fontSize: '18px', color: '#1890FF', }" style="margin-left: 5px"/>
          <a-popconfirm title="确定取消收藏吗?" @confirm="() => delFavouriteKmDoc(record)">
          <a-icon type="delete" title="取消收藏" :style="{ fontSize: '18px', color: '#1890FF', }" style="margin-left: 5px"/>
          </a-popconfirm>
          <a-icon type="download" title="下载" @click="downloadKmDoc(record)"
                  :style="{ fontSize: '18px', color: '#1890FF'}" style="margin-left: 5px"/>
          </a-space>
        </span>
        <span slot="docTitle" slot-scope="text,record">
          <span @click="showDrawer(record,true)"  :title ="[ record.fileType + '文件-大小:'+ record.fileSize +' B'] "><a style="color: #303133">{{ record.title}}</a></span>
        </span>
      </a-table>
    </div>

    <b-j-modal :title="previewTitle"
               :width="width"
               :visible="previewVisible"
               @cancel="handleCancel"
               cancelText="关闭"
               :okButtonProps="{ class:{'jee-hidden': true} }">
      <div>
        <div>
          <p-d-f-modal :p-d-furl="PDFurl" :iframeWidth="width"/>
        </div>
      </div>
    </b-j-modal>
  </a-card>
</template>

<script>
  import {ajaxGetDictItems, getDictItemsFromCache} from '@/api/api'
  import {ACCESS_TOKEN, USER_INFO} from "@/store/mutation-types"
  import '@/assets/less/TableExpand.less'
  import {mixinDevice} from '@/utils/mixin'
  import {JeecgListMixin} from '@/mixins/JeecgListMixin'
  import {httpPostAction, httpAction, getAction, downloadFileName} from '@/api/manage'
  import Vue from "vue";
  import DocComments from '@/views/km/Common/Comments'
  import DocDetail from '@/views/km/Common/DocDetail'
  import store from "@comp/tools/HeaderNotice";


  export default {
    name: 'FavouriteList',
    mixins: [JeecgListMixin, mixinDevice],
    components:{
      DocComments,
      DocDetail
    },
    data() {
      return {
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
            title: '操作',
            dataIndex: 'action',
            align: "center",
            fixed: "left",
            width: 80,
            scopedSlots: {
              filterDropdown: 'filterDropdown',
              filterIcon: 'filterIcon',
              customRender: 'action'
            }
          },
          {
            title: '标题',
            align: "left",
            scopedSlots: {customRender: 'docTitle'},
            customCell: () => {
              return {
                style: {
                  'max-width': '30em',
                },
              };
            },
            dataIndex: 'title'
          },
          {
            title: '分类',
            align: "center",
            sorter: true,
            customCell: () => {
              return {
                style: {
                  'max-width': '8em',
                },
              };
            },
            dataIndex: 'category_dictText'
          },

          {
            title: '标签',
            align: "center",
            dataIndex: 'businessTypes_dictText',
            customCell: () => {
              return {
                style: {
                  'max-width': '10em',
                  overflow: 'hidden',
                  whiteSpace: 'nowrap',
                  textOverflow:'ellipsis'
                },
              };
            },
          },
          {
            title: '关键字',
            align: "center",
            dataIndex: 'keywords',
            customCell: () => {
              return {
                style: {
                  'max-width': '10em',
                  overflow: 'hidden',
                  whiteSpace: 'nowrap',
                  textOverflow:'ellipsis'
                },
              };
            }
          }
        ],
        PDFurl: '',
        headers: {
          authorization: 'authorization-text',
          'X-Access-Token': Vue.ls.get(ACCESS_TOKEN)
        },
        labelCol: {
          xs: {span: 24},
          sm: {span: 5},
        },
        labelCol1: {
          xs: {span: 10},
          sm: {span: 10},
        },
        wrapperCol: {
          xs: {span: 24},
          sm: {span: 16},
        },
        wrapperCol1: {
          xs: {span: 12},
          sm: {span: 12},
        },
        width: "900",
        url: {
          list: "/KM/kmDocFavourite/list",
          delFavouriteKmDoc: '/KM/kmDocFavourite/delete',
          deleteBatch: "/KM/kmDocFavourite/deleteBatch",
          downloadKmDoc: "/KM/kmDoc/downloadKmDoc",
          previewKmDoc: '/KM/kmDoc/previewKmDoc',
        },
        dictOptions: {},
        superFieldList: [],

        uid: '',
        userInfo: {},
        showDocDetail: {},
        drawerVisible: false,
        previewVisible: false,
        previewTitle: "预览 - ",      }
    },
    created() {
      //设置全局token
      Vue.prototype.token = Vue.ls.get(ACCESS_TOKEN);
      window._CONFIG['token'] = Vue.prototype.token;
      // 调用初始化自定义table列表函数
      this.initColumns();
      this.initFilterDict();
      // let userInfo = Vue.ls.get(USER_INFO)
      let userInfo = this.$store.getters.userInfo
      this.uid = userInfo.id
      this.userInfo = userInfo
    },
    methods: {

      // 加载filter字段选项
      initFilterDict() {
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
            this.defColumns[3].filters =this.filterOptions;
          }
        })
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
      // 下载文件
      downloadKmDoc(record) {
        this.$notification.success({
          message: '文件开始下载...',
          duration: 1,
        });
        downloadFileName(this.url.downloadKmDoc, {docId: record.id})
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
              that.loadData();
              that.onClearSelected();
            }
          });
        }
      },
      showDrawer(record) {
        this.showDocDetail = record
        this.drawerVisible = true
      },
      onDrawerClose() {
        this.drawerVisible = false
      },
      //编辑源文件
      editKmDoc(record){
        let editorUrl = window._CONFIG['domianURL'] + '/onlyoffice/editor?'
        editorUrl = editorUrl + 'docId=' + record.id
        editorUrl = editorUrl + '&uid=' + this.uid
        editorUrl = editorUrl + '&fileName=' + record.title + '.'+ record.fileType
        window.open(editorUrl,"_blank")
      },
      //显示预览窗口，并初始化配置
      previewKmDoc(record) {
        this.PDFurl =  this.url.previewKmDoc + "?docId=" + record.id;
        this.pdfLoading = true;
        this.previewVisible = true;
        this.previewTitle = "预览 - " + record.title
      },
      //关闭窗口
      handleCancel() {
        console.log("cancel ")
        this.configVisible = false
        this.previewVisible = false;
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
            this.loadData();
          } else {
            this.$message.warning("取消收藏失败!");
          }
        }).finally(() => {
        })
      },
      refreshDocDetail(){
        console.log("emit new from comments and call docDetail refreshDocDetail....")
        this.$refs.docDetailRef.refreshDocDetail()
      }

    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>