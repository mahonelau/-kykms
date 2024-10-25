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
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-model-item label="归属部门"   prop="depId">
              <j-select-depart v-model="queryParam.depId" :multi="true"></j-select-depart>
            </a-form-model-item>
          </a-col>
          <template v-if="toggleSearchStatus">
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
    <!-- config区域-BEGIN -->
    <a-j-modal :width="width"
               :visible="configVisible"
               :show-return="true"
               @cancel="handleCancel"
               @close="handleCancel"
               :destroyOnClose="true"
               cancelText="关闭"
               :okButtonProps="{ class:{'jee-hidden': true} }">
      <draft-doc-edit :model="configDetail"
                      :release-button="true"
                      :dup-check=true
                      @cancel="handleCancel"
                      @ok="editOk" >
      </draft-doc-edit>
    </a-j-modal>
    <!-- config区域-END -->
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

      <doc-comments ref="comments"
                    @new="refreshDocDetail"
                    :doc-id="showDocDetail.id">
      </doc-comments>

    </a-drawer>
    <!-- 详情区域-END -->
    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchAuditPass">
            <a-icon type="check"/>
            批量通过
          </a-menu-item>
          <a-menu-item key="2" @click="batchAuditReject">
            <a-icon type="close"/>
            批量拒绝
          </a-menu-item>
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
                :style="{ fontSize:'16px',color:  '#108ee9',minWidth:'50px'}"/>

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
             <a-popover content="通过审核">
                <a-popconfirm title="确定通过吗?" @confirm="() => handleAuditPass(record.id)">
                 <a-icon type="check-circle" title="通过" :style="{ fontSize: '18px', color: 'green', }"/>
               </a-popconfirm>
             </a-popover>
             <a-popover content="驳回">
                 <a-popconfirm title="确定驳回吗?" @confirm="() => handleAuditReject(record.id)">
                 <a-icon type="close-circle" title="驳回" :style="{ fontSize: '18px', color: 'red', }"/>
               </a-popconfirm>
             </a-popover>
             <a-popover content="预览">
               <a-icon type="read" title="预览" @click="previewKmDoc(record)"
                       :style="{ fontSize: '18px', color: '#1890FF', }"/>
             </a-popover>
             <a-popover content="下载">
            <a-icon type="download" title="下载" @click="downloadKmDoc(record)"
                    :style="{ fontSize: '18px', color: '#1890FF'}" />
             </a-popover>
           <a-divider type="vertical"/>
            <a-dropdown>
              <a class="ant-dropdown-link"><a-icon :style="{ fontSize: '18px', color: '#1890FF'}" type="down-circle"/></a>
              <a-menu slot="overlay">
<!--                 <a-menu-item>-->
<!--                  <a v-show="docEditable(record)" @click="editKmDoc(record)">编辑源文件</a>-->
<!--                </a-menu-item>-->
                 <a-menu-item>
                  <a  @click="configDoc(record)">配置</a>
                </a-menu-item>
                 <a-menu-item>
                  <a  @click="downloadKmDoc(record)">下载</a>
                </a-menu-item>
                <a-menu-item>
                  <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                    <a>删除</a>
                  </a-popconfirm>
                </a-menu-item>
              </a-menu>
            </a-dropdown>
           </a-space>
        </span>

        <span slot="docTitle" slot-scope="text,record">
          <span v-if="record.convertFlag === -1" v-bind:title=record.processMsg><a style="color: #d71345">{{record.title}}</a></span>
          <span v-else-if="record.convertFlag === 0" title="等待转换"><a style="color: #fa8c16">{{record.title}}</a></span>
          <span v-else @click="showDrawer(record)"  :title ="[ record.fileType + '文件-大小:'+ record.fileSize +' B'] "><a style="color: #303133">{{ record.title}}</a></span>
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
          <p-d-f-modal :p-d-furl="PDFurl" :iframeWidth="width" :pdf-loading="pdfLoading" />
        </div>
      </div>
    </b-j-modal>
  </a-card>
</template>

<script>
  import {ajaxGetDictItems, getDictItemsFromCache} from '@/api/api'
  import {ACCESS_TOKEN, USER_INFO} from "@/store/mutation-types"
  import {downloadFileName, httpPostAction, httpAction, getAction} from '@/api/manage'
  import '@/assets/less/TableExpand.less'
  import {mixinDevice} from '@/utils/mixin'
  import {JeecgListMixin} from '@/mixins/JeecgListMixin'
  import SubjectModal from "./modules/SubjectModal"
  import Vue from "vue"
  import DocComments from '@/views/km/Common/Comments'
  import DocDetail from '@/views/km/Common/DocDetail'
  import DraftDocEdit from '@/views/km/filemanagement/modules/DraftDocEdit'
  import store from "@comp/tools/HeaderNotice";

  export default {
    name: 'PendingList',
    mixins: [JeecgListMixin, mixinDevice],
    components: {
      SubjectModal,
      DocComments,
      DocDetail,
      DraftDocEdit
    },
    data() {
      return {
        //表头
        columns: [],
        //列设置
        settingColumns: [],
        //  过滤字段数据
        filterOptions: [],
        //列定义
        defColumns: [
          {
            title: '操作',
            dataIndex: 'action',
            align: "center",
            fixed: "left",
            width: 180,
            scopedSlots: {
              filterDropdown: 'filterDropdown',
              filterIcon: 'filterIcon',
              customRender: 'action'
            }
          },
          {
            title: '标题',
            align: "left",
            dataIndex: 'title',
            scopedSlots: {customRender: 'docTitle'},
            customCell: () => {
              return {
                style: {
                  'max-width': '30em',
                },
              };
            },
          },
          {
            title: '分类',
            align: "center",
            dataIndex: 'category_dictText',
            sorter: true,
            customCell: () => {
              return {
                style: {
                  'max-width': '6em',
                },
              };
            },
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
            },
          },
          {
            title: '上传人',
            align: "center",
            dataIndex: 'createBy',
            sorter: true,
            customCell: () => {
              return {
                style: {
                  'max-width': '3em',
                },
              };
            },
          },
          {
            title: '归属部门',
            align: "center",
            dataIndex: 'orgCode_dictText',
            sorter: true,
            customCell: () => {
              return {
                style: {
                  'max-width': '5em',
                },
              };
            },
          },
        ],
        PDFurl: '',
        model: {},
        headers: {
          authorization: 'authorization-text',
          'X-Access-Token': Vue.ls.get(ACCESS_TOKEN)
        },
        confirmLoading: false,
        pdfLoading: false,
        title: "编辑",
        width: "900",
        url: {
          list: "/KM/kmDoc/listWaitAudit",
          auditPass: '/KM/kmDoc/auditPass',
          auditReject: '/KM/kmDoc/auditReject',
          batchAuditPass: '/KM/kmDoc/auditPassBatch',
          batchAuditReject: 'KM/kmDoc/auditRejectBatch',
          previewKmDoc: '/KM/kmDoc/previewKmDoc',
          downloadKmDoc: "/KM/kmDoc/downloadKmDoc",
        },
        // superFieldList: [],
        uid: '',
        userInfo: {},
        showDocDetail: {},
        configDetail: {},
        drawerVisible: false,
        configVisible: false,
        previewVisible: false,
        previewTitle: "预览 - ",
      }
    },
    created() {
      // this.getSuperFieldList();
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
      //打开文档详情
      showDrawer(record) {
        this.showDocDetail = record
        this.drawerVisible = true
      },
      onDrawerClose() {
        this.drawerVisible = false
      },
      docEditable: function (record) {
        let editAbleFileType = ['doc', 'docm', 'docx', 'dot', 'dotm', 'dotx', 'epub', 'fodt', 'htm', 'html', 'mht', 'odt', 'ott', 'pdf', 'rtf', 'txt', 'djvu', 'xps','csv', 'fods', 'ods', 'ots', 'xls', 'xlsm', 'xlsx', 'xlt', 'xltm', 'xltx','fodp', 'odp', 'otp', 'pot', 'potm', 'potx', 'pps', 'ppsm', 'ppsx', 'ppt', 'pptm', 'pptx' ]
        // console.log(record.fileType+ ":"+editAbleFileType.indexOf(record.fileType))
        return editAbleFileType.indexOf(record.fileType) >=0
      },
      //编辑源文件
      editKmDoc(record){
        let editorUrl = window._CONFIG['domianURL'] + '/onlyoffice/editor?'
        editorUrl = editorUrl + 'docId=' + record.id
        editorUrl = editorUrl + '&uid=' + this.uid
        editorUrl = editorUrl + '&fileName=' + record.title + '.'+ record.fileType
        // console.log("editorUrl:",editorUrl)
        window.open(editorUrl,"_blank")
        // let option ={
        //   url: '',
        //     isEdit: '',
        //     fileType: '',
        //     title: '',
        //     lang: '',
        //     isPrint: '',
        //     user: { id:null,name:''}
        // }
        // this.$router.push({path: '/Km/filemanagement/VabOnlyOfficeEditor', query: {selected: "2"}})
      },
      configDoc(record){
        this.configVisible = true
        this.configDetail = record
      },
      //显示预览窗口，并初始化配置
      previewKmDoc(record) {
        this.PDFurl =  this.url.previewKmDoc + "?docId=" + record.id;
        this.pdfLoading = true;
        this.previewVisible = true;
        this.previewTitle = "预览 - " + record.title
      },
      //关闭预览窗口
      handleCancel() {
        console.log("cancel ")
        this.configVisible = false
        this.previewVisible = false;
        this.model = {};
      },
      editOk(){
        this.configVisible=false
        this.loadData()
      },

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
            this.defColumns[2].filters =this.filterOptions;
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
      //下载文件
      downloadKmDoc(record) {
        this.$notification.success({
          message: '文件开始下载...',
          duration: 1,
        });
        downloadFileName(this.url.downloadKmDoc, {docId: record.id})
      },
      // 同意通过
      handleAuditPass: function (id) {
        if (!this.url.auditPass) {
          this.$message.error("请设置url.auditPass属性!")
          return
        }
        var that = this;
        httpPostAction(that.url.auditPass, {id: id}, 'put').then((res) => {
          if (res.success) {
            //重新计算分页问题
            that.reCalculatePage(1)
            that.$message.success(res.message);
            that.loadData();
          } else {
            that.$message.warning(res.message);
          }
        });
      },
      //拒绝通过
      handleAuditReject: function (id) {
        if (!this.url.auditReject) {
          this.$message.error("请设置url.auditReject!")
          return
        }
        var that = this;
        httpPostAction(that.url.auditReject, {id: id}, 'put').then((res) => {
          if (res.success) {
            //重新计算分页问题
            that.reCalculatePage(1)
            that.$message.success(res.message);
            that.loadData();
          } else {
            that.$message.warning(res.message);
          }
        });
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