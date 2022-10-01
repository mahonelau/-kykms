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
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="编号">
                <a-input placeholder="请输入编号" v-model="queryParam.serialNumber"></a-input>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="来源">
                <j-multi-select-tag type="list_multi" placeholder="请选择来源" v-model="queryParam.sourceList"
                                    dictCode="km_dict_source"></j-multi-select-tag>

              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-model-item label="版本状态">
                <j-multi-select-tag type="list_multi" v-model="queryParam.versionList" :trigger-change="true"
                                    placeholder="请选择版本状态" dictCode="km_dict_versions"></j-multi-select-tag>
              </a-form-model-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-model-item label="涉及业务">
                <j-multi-select-tag type="list_multi" v-model="queryParam.businessTypeList" :trigger-change="true"
                                    placeholder="请选择涉及业务" dictCode="km_dict_business"></j-multi-select-tag>
              </a-form-model-item>
            </a-col>

            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="关键字">
                <a-input placeholder="请输入关键字" v-model="queryParam.keywords"></a-input>
              </a-form-item>
            </a-col>

            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="文号">
                <a-input placeholder="请输入文号" v-model="queryParam.fileNo" @change="transitionQueryFileNo"></a-input>
              </a-form-item>
            </a-col>


            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="文件类型">
                <a-input placeholder="请输入文件类型" v-model="queryParam.fileType"></a-input>
              </a-form-item>
            </a-col>
            <a-col :xl="10" :lg="12" :md="14" :sm="48">
              <a-form-item label="发文时间">
                <j-dict-select-tag type="list" placeholder="请选择开始时间" v-model="queryParam.pubTimeStart" style="width:40%"
                                   dictCode="km_dict_year"></j-dict-select-tag>
                <span class="query-group-split-cust"></span>
                <j-dict-select-tag type="list" placeholder="请选择结束时间" v-model="queryParam.pubTimeEnd" style="width: 40%"
                                   dictCode="km_dict_year"></j-dict-select-tag>
              </a-form-item>
            </a-col>
            <a-col :xl="10" :lg="12" :md="14" :sm="48">
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
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button type="primary" icon="upload" @click="uploadModules">上传</a-button>
      <!--      <a-button type="primary" icon="download" @click="handleExportXls('草稿文件夹')">导出</a-button>-->
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel">
            <a-icon type="delete"/>
            批量删除
          </a-menu-item>
          <!--<a-menu-item key="2" @click="batchEdit">-->
            <!--<a-icon type="audit"/>-->
            <!--批量提审-->
          <!--</a-menu-item>-->
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
          <a-icon type="edit" title="编辑" @click="previewKmDoc(record,false)"
                  :style="{ fontSize: '18px', color: '#1890FF', }"/>

          <!--<a-popconfirm-->
            <!--title="你是否将文件提交审核?"-->
            <!--ok-text="确定"-->
            <!--cancel-text="取消"-->

            <!--@confirm="confirm(record)">-->
            <!--<a-icon type="audit" title="提审" href="#" :style="{ fontSize: '18px', color: '#1890FF', }"-->
                    <!--style="margin-left: 5px"/>-->
          <!--</a-popconfirm>-->

          <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
          <a-icon type="delete" title="删除" :style="{ fontSize: '18px', color: '#1890FF', }" style="margin-left: 5px"/>
          </a-popconfirm>

          <a-icon type="download" title="下载" @click="downloadKmDoc(record)"
                  :style="{ fontSize: '18px', color: '#1890FF'}" style="margin-left: 5px"/>
        </span>

        <span slot="docTitle" slot-scope="text,record">
          <span v-if="record.convertFlag === -1" :title=record.processMsg><a style="color: #d71345">{{record.title}}</a></span>
          <span v-else-if="record.convertFlag === 0" title="等待转换"><a style="color: #fa8c16">{{record.title}}</a></span>
          <span v-else @click="previewKmDoc(record,true)"><a style="color: #303133">{{ record.title}}</a></span>
        </span>

        <span slot="status" slot-scope="text,record">
          <a v-if="record.status===0" style="color: #000c17">未提审</a>
          <a v-if="record.status===3" style="color: #d71345">已拒绝</a>
        </span>

      </a-table>
    </div>
    <upload-modal ref="uploadModules" @ok="modalFormOk"></upload-modal>

    <a-j-modal :title="title"
               :width="width"
               :visible="visible"
               @cancel="handleCancel"
               @changeShowTitle="changeShowTitle"
               :show-return="showTitlePDF"
               cancelText="关闭"
               :okButtonProps="{ class:{'jee-hidden': true} }">
      <a-spin :spinning="confirmLoading" style="margin-top: 10px;float: left;width: 690px">
        <j-form-container v-if="!formDisabled&&boolCheckTitle&&!showTitlePDF">
          <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
            <a-row>
              <a-col :span="21">
                <a-form-model-item label="标题" style="margin-left: -15px" :labelCol="labelCol3" :wrapperCol="wrapperCol3"
                                   prop="title">
                  <a-textarea v-model="model.title" placeholder="请输入标题"
                              :auto-size="{ minRows: 1, maxRows: 3 }"></a-textarea>
                </a-form-model-item>
              </a-col>
              <a-col :span="3" style="text-align: center;margin-top: 4px">
                <a-button type="primary" @click="checkTitleFun">查重</a-button>
              </a-col>
              <a-col :span="12">
                <a-form-model-item label="分类" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="category">
                  <j-dict-select-tag type="list" placeholder="请选择分类" v-model="model.category"
                                     dictCode="km_dict_category"></j-dict-select-tag>
                </a-form-model-item>
              </a-col>
              <a-col :span="12">
                <a-form-model-item label="文号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="fileNo">
                  <a-input v-model="model.fileNo" placeholder="输入文号" @change="transitionFileNo"></a-input>
                </a-form-model-item>
              </a-col>
              <a-col :span="12">
                <a-form-model-item label="来源" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="source">
                  <j-dict-select-tag type="list" placeholder="请选择来源" v-model="model.source"
                                     dictCode="km_dict_source"></j-dict-select-tag>
                </a-form-model-item>
              </a-col>
              <a-col :span="12">
                <a-form-model-item label="发文时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pubTimeTxt">
                  <a-input v-model="model.pubTimeTxt" placeholder="输入格式: 2021-01-01"></a-input>
                </a-form-model-item>
              </a-col>
              <a-col :span="12">
                <a-form-model-item label="关键字" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="keywords">
                  <a-textarea v-model="model.keywords" placeholder="请输入关键字"
                              :auto-size="{ minRows: 1, maxRows: 3 }"></a-textarea>
                </a-form-model-item>
              </a-col>

              <a-col :span="12">
                <!--专题分类-->
                <a-form-model-item label="专题分类" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-textarea @click="showModel(model)" v-model="model.topicIds_dictText"
                              placeholder="请点击选择专题" readOnly :auto-size="{ minRows: 1, maxRows: 3 }">
                  </a-textarea>
                </a-form-model-item>
              </a-col>

              <a-col :span="12">
                <a-form-model-item label="涉及业务" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="businessTypes">
                  <j-multi-select-tag type="list_multi" v-model="model.businessTypes" :trigger-change="true"
                                      placeholder="请选择涉及业务" dictCode="km_dict_business"></j-multi-select-tag>
                </a-form-model-item>
              </a-col>

              <a-col :span="12">
                <a-form-model-item label="版本状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="versions">
                  <j-multi-select-tag type="list_multi" v-model="model.versions" :trigger-change="true"
                                      placeholder="请选择版本状态" dictCode="km_dict_versions"></j-multi-select-tag>
                </a-form-model-item>
              </a-col>


              <a-col :span="12">
                <a-form-model-item label="归属部门" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="depId">
                  <j-select-depart v-model="model.depId" :multi="false"></j-select-depart>
                </a-form-model-item>
              </a-col>

              <a-col :span="12">
                <a-form-model-item label="公开方式" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="publicRemark">
                  <j-dict-select-tag type="list" placeholder="请选择公开方式" v-model="model.publicRemark"
                                     dictCode="km_dict_public_remark"></j-dict-select-tag>
                </a-form-model-item>
              </a-col>

              <a-col :span="24">
                <a-form-model-item label="备注" :labelCol="labelCol2" :wrapperCol="wrapperCol2" prop="remark">
                  <a-textarea v-model="model.remark" placeholder="请输入备注"
                              :auto-size="{ minRows: 1, maxRows: 3 }"></a-textarea>
                </a-form-model-item>
              </a-col>


              <a-col :span="12">
                <a-form-model-item label="预览文件" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="keywords">
                    <span>
                        <a-popconfirm v-if="resetPreviewShow"
                                      title="你是否将文件恢复吗?"
                                      ok-text="确定"
                                      cancel-text="取消"
                                      @confirm="resetPreviewFile(model)">
                        <a href="#" style="margin-left: 20px;color: #1890FF">恢复</a>
                          </a-popconfirm>
                    <a-upload
                      name="file"
                      :multiple="true"
                      :action="urlAction+model.id"
                      :headers="headers"
                      @change="handleChange"
                      :showUploadList="false"
                    >
                    <a-button type="link" style="color: #1890FF">更改</a-button>
                    </a-upload>
                    </span>
                </a-form-model-item>
              </a-col>

              <a-col :span="24" style="text-align: center;margin-top: -10px">
                <a-button type="primary" @click="arrayFun(model)">确认修改</a-button>
                <a-button style="margin-left: 40px" @click="handleCancel">取消修改</a-button>
              </a-col>
            </a-row>
          </a-form-model>
          <subject-modal ref="modalForm" @ok="modalFormOk"></subject-modal>
        </j-form-container>
        <div v-if="!formDisabled&&!boolCheckTitle&&!showTitlePDF">
          <a-form layout="inline" style="min-height: 10px">
            <a-row>
              <a-col style="background-color:#ededf0">
                <h3 style="color: #303133;margin-left: 10px;padding:5px">
                  <b> </b>
                  <b style="margin-left: 10px" @click="changeBoolCheckTitleFun">
                    <a style="color:#303133;">
                      <a-icon type="arrow-left"/>
                      <span>&nbsp;&nbsp;返回</span>
                    </a>
                  </b>
                </h3>
              </a-col>
              <a-col v-for="(item,index) in checkTitleList" :xl="24" :lg="24" :md="24" :sm="24" :key="index">
                <p style="text-align: left;margin-left: 20px">
                  <span class="titleIndex"><b>{{index+1+'、'}}</b></span>
                  <span @click="showPreviewKmDoc(item.id,item.title)"><a style="color: #303133" v-html="item.title"></a></span>
                </p>
              </a-col>
            </a-row>
          </a-form>
        </div>
      </a-spin>

      <div style="float: left;width: 690px" v-if="!formDisabled&&!boolCheckTitle&&showTitlePDF" >
        <div>
          <div>
            <p-d-f-modal :p-d-furl="TitlePDFurl" />
          </div>
        </div>
      </div>
      <div style="float: right;width: 700px" >
        <div>
          <div>
            <p-d-f-modal :p-d-furl="PDFurl"  />
          </div>
        </div>
      </div>

    </a-j-modal>

    <b-j-modal :title="title"
               :width="width"
               :visible="visibleB"
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
  import {ACCESS_TOKEN} from "@/store/mutation-types"
  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import SubjectModal from './modules/SubjectModal'
  import UploadModal from './modules/UploadModal'
  import { httpPostAction, httpAction, getAction, downloadFileName } from '@/api/manage'
  import Vue from 'vue'

  export default {
    name: 'DraftsList',
    mixins: [JeecgListMixin, mixinDevice],
    components: {
      UploadModal,
      SubjectModal,
    },
    data() {
      return {
        // 重复标题列表
        checkTitleList: [],
        showTitlePDF: false,
        boolCheckTitle: true,
        // switch 是否公开按钮的的状态
        // switchPublicFlag: true,
        // switch 是否允许下载按钮的的状态
        switchDownloadFlag: true,
        description: '草稿文件夹管理页面',
        //表头
        columns: [],
        //列设置
        settingColumns: [],
        //要filter的字典
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
            width: 105,
            scopedSlots: {
              filterDropdown: 'filterDropdown',
              filterIcon: 'filterIcon',
              customRender: 'action'
            }
          },
          {
            title: '编号',
            align: "center",
            dataIndex: 'serialNumber',
            sorter: true,
            customCell: () => {
              return {
                style: {
                  'max-width': '7em',
                },
              }
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
                  'max-width': '8em',
                },
              };
            },
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
            title: '来源',
            align: "center",
            dataIndex: 'source_dictText',
            filters: [],
            customCell: () => {
              return {
                style: {
                  'max-width': '7em',
                },
              };
            },
          },
          {
            title: '发文时间',
            align: "center",
            sorter: true,
            dataIndex: 'pubTimeTxt',
            customCell: () => {
              return {
                style: {
                  'max-width': '11em',
                },
              };
            },
          },
          {
            title: '版本状态',
            align: "center",
            dataIndex: 'versions_dictText',
            customCell: () => {
              return {
                style: {
                  'max-width': '12em',
                },
              };
            },
          },
          {
            title: '涉及业务',
            align: "center",
            dataIndex: 'businessTypes_dictText',
            customCell: () => {
              return {
                style: {
                  'max-width': '12em',
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
                  'max-width': '12em',
                },
              };
            },
          },
          {
            title: '文号',
            align: "center",
            dataIndex: 'fileNo',
            customCell: () => {
              return {
                style: {
                  'max-width': '12em',
                },
              };
            },
          },
          // {
          //   title: '状态',
          //   align: "center",
          //   dataIndex: 'status',
          //   scopedSlots: {customRender: 'status'},
          //   customCell: () => {
          //     return {
          //       style: {
          //         'max-width': '5em',
          //       },
          //     };
          //   },
          // },

        ],
        PDFurl: '',
        TitlePDFurl: '',
        visible: false,
        visibleB: false,
        model: {},
        urlAction: window._CONFIG['domianURL'] + "/KM/kmDoc/changePreviewFile?docId=",
        headers: {
          authorization: 'authorization-text',
          'X-Access-Token': Vue.ls.get(ACCESS_TOKEN)
        },


        labelCol: {
          xs: {span: 24},
          sm: {span: 6},
        },
        labelCol1: {
          xs: {span: 12},
          sm: {span: 12},
        },
        labelCol2: {
          xs: {span: 3},
          sm: {span: 3},
        },
        labelCol3: {
          xs: {span: 4},
          sm: {span: 4},
        },
        wrapperCol: {
          xs: {span: 24},
          sm: {span: 18},
        },
        wrapperCol1: {
          xs: {span: 10},
          sm: {span: 10},
        },
        wrapperCol2: {
          xs: {span: 21},
          sm: {span: 21},
        },
        wrapperCol3: {
          xs: {span: 20},
          sm: {span: 20},
        },

        confirmLoading: false,
        // 用来判断文件是否加载完成
        loadedRatio: 0,
        pdfLoading: false,
        pdfShow: true,
        resetPreviewShow: false,
        validatorRules: {
          depId: [{required: true, message: '请选择部门!'}],
          pubTimeTxt: [{
            required: false,
            pattern: /^[0-9]{4}$|^[0-9]{4}-(?:0[1-9]|1[0-2])$|^(?:[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)$/,
            message: '日期格式应为:2021-01-01'
          }],
        },
        formDisabled: false,
        versions: '',
        businessTypes: '',
        title: "编辑",
        width: "900",
        url: {
          docDPCheck: '/KM/kmDoc/docDPCheck',
          list: '/KM/kmDoc/listDraft',
          delete: '/KM/kmDoc/delete',
          deleteBatch: '/KM/kmDoc/deleteBatch',
          editBatch: '/KM/kmDoc/submitAuditBatch',
          downloadKmDoc: '/KM/kmDoc/downloadKmDoc',
          previewKmDoc: '/KM/kmDoc/previewKmDoc',
          edit: '/KM/kmDoc/editDraft',
          resetPreviewFile: '/KM/kmDoc/resetPreviewFile',
          exportXlsUrl: '/KM/kmDoc/exportXls',
          submitAuditUrl: '/KM/kmDoc/submitAudit',
        },
        dictOptions: {},
        superFieldList: [],
      }
    },
    created() {
      this.getSuperFieldList();
      //设置全局token
      Vue.prototype.token = Vue.ls.get(ACCESS_TOKEN);
      window._CONFIG['token'] = Vue.prototype.token;

      // 调用初始化自定义table列表函数
      this.initColumns();
      this.initFilterDict();

    },
    computed: {
      importExcelUrl: function () {
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      },
    },
    watch: {
      loadedRatio: {
        handler(newVal, oldVal) {
          if (newVal === 1) {
            this.pdfLoading = false;
          }
        }
      }
    },
    methods: {

      changeShowTitle(){
        this.title= '查找相似标题：' + this.model.title;
        this.showTitlePDF=false;
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
            this.defColumns[4].filters =this.filterOptions;
          }
        })

      },
      // 点击查重触发方法
      checkTitleFun() {
        this.title = '查找相似标题：' + this.model.title
        this.checkTitleList = []
        let params = {}
        // replaceTitle = replaceTitle.replace(/\[/g, '%5B');
        // replaceTitle = replaceTitle.replace(/\]/g, '%5D');

        params['docId'] = this.model.id;
        params['checkType'] = "1";
        params['pageSize']="20";
        getAction(this.url.docDPCheck, params).then((res) => {
          if (res.success) {
            this.checkTitleList = res.result.kmSearchResultVOPage.records
          } else {
            this.$message.error('检索失败')
          }
          this.loading = false
        })
        this.boolCheckTitle = false
      },

      // 返回编辑界面
      changeBoolCheckTitleFun() {
        this.boolCheckTitle = true
        this.title="编辑";
      },

      // 显示预览窗口，初始化
      showPreviewKmDoc(id,title) {
        this.showTitlePDF=true;
        this.title=" ";
        //this.TitlePDFurl = window._CONFIG['domianURL'] + this.url.previewKmDoc + '?docId=' + id
        this.PDFurl =  this.url.previewKmDoc + "?docId=" + record.id;

      },

      // 部门控件触发方法
      backDepartInfo(info) {
        this.model.departIds = this.model.selecteddeparts;
        console.log(this.model.departIds);
        console.log("info", info);
        this.nextDepartOptions = info.map((item, index, arr) => {
          let c = {label: item.text, value: item.value + ""}
          console.log(c);
          return c;
        })
      },

      transitionFileNo() {
        // 将输入的中括号 转换为 六角括号
        if (typeof (this.model.fileNo) !== "undefined") {
          this.model.fileNo = this.model.fileNo.replace(/\[/g, '〔');
          this.model.fileNo = this.model.fileNo.replace(/\]/g, '〕');
          console.log(this.model.fileNo);
        }
      },


      transitionQueryFileNo() {
        // 将输入的中括号 转换为 六角括号
        if (typeof (this.queryParam.fileNo) !== "undefined") {
          this.queryParam.fileNo = this.queryParam.fileNo.replace(/\[/g, '〔');
          this.queryParam.fileNo = this.queryParam.fileNo.replace(/\]/g, '〕');
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

      initDictConfig() {
      },
      getSuperFieldList() {
        let fieldList = [];
        fieldList.push({type: 'string', value: 'serialNumber', text: '编号', dictCode: ''})
        fieldList.push({type: 'string', value: 'category', text: '分类', dictCode: ''})
        fieldList.push({type: 'string', value: 'title', text: '标题', dictCode: ''})
        fieldList.push({type: 'string', value: 'source', text: '来源', dictCode: ''})
        fieldList.push({type: 'datetime', value: 'pubTime', text: '发文时间'})
        fieldList.push({type: 'string', value: 'versions', text: '版本状态', dictCode: ''})
        fieldList.push({type: 'string', value: 'businessTypes', text: '涉及业务', dictCode: ''})
        fieldList.push({type: 'string', value: 'keywords', text: '关键字', dictCode: ''})
        fieldList.push({type: 'datetime', value: 'createTime', text: '上传时间'})
        fieldList.push({type: 'string', value: 'fileType', text: '文件类型', dictCode: ''})
        this.superFieldList = fieldList
      },
      //上传文件
      uploadModules() {
        this.$refs.uploadModules.edit();
        this.$refs.uploadModules.title = "上传";
        this.$refs.uploadModules.disableSubmit = false;
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
              //that.loadData();
              that.onClearSelected();
            }
          });
        }
      },
      //显示预览窗口，并初始化配置
      previewKmDoc(record, boolFormDisabled) {
        if (boolFormDisabled) {
          this.title = '预览 - ' + record.title;
          this.visibleB = true;
        } else {
          this.title = '编辑 - ' + record.title;
          this.visible = true;
        }
        this.model = {};
        this.model = Object.assign({}, record);

        console.log("model", this.model);

        this.boolCheckTitle=true;
        this.showTitlePDF=false;
        if (this.model.depId == null || this.model.depId === "") {
          var depId = Vue.ls.get("Dep_ID");
          console.log("部门数据为空,从本地中获取数据", depId);
          this.model.depId = depId
        }
        this.versions = this.model.versions;
        this.formDisabled = boolFormDisabled;
        this.businessTypes = this.model.businessTypes;
        // this.PDFurl = window._CONFIG['domianURL'] + this.url.previewKmDoc + "?docId=" + record.id;
        this.PDFurl =  this.url.previewKmDoc + "?docId=" + record.id;

        this.pdfLoading = true;
        this.pdfShow = true;
        if (this.model.previewFileId != this.model.originalPreviewFileId) {
          this.resetPreviewShow = true;
        }

        //  将是否允许下载状态从 1 和 0 转为 switch的true和false
        if (this.model.downloadFlag === 1) {
          this.switchDownloadFlag = true;
        } else {
          this.switchDownloadFlag = false;
        }


      },
      //关闭预览窗口
      handleCancel() {
        this.visible = false;
        this.visibleB = false;
        this.model = {};
      },

      // 提审
      confirm(record) {
        let httpurl = '';
        let method = '';
        let person = {id: record.id};
        httpurl += this.url.submitAuditUrl;
        method = 'put';
        httpPostAction(httpurl, person, method).then((res) => {
          if (res.success) {
            this.$message.success("提审成功!");
            this.loadData();
          } else {
            this.$message.warning("提审失败!");
          }
        }).finally(() => {
        })
      },


      //恢复预览文件
      resetPreviewFile(model) {
        let httpurl = '';
        let method = '';
        httpurl += this.url.resetPreviewFile;
        method = 'post';
        this.pdfLoading = true;
        this.pdfShow = false;
        httpPostAction(httpurl, {docId: model.id}, method).then((res) => {
          if (res.success) {
            this.$message.success("恢复成功!");
            this.pdfShow = true;
            this.resetPreviewShow = false;
            this.loadData();
          } else {
            this.$message.warning("恢复失败!");
            this.pdfShow = true;
            this.pdfLoading = false;
          }
        }).finally(() => {
          this.pdfShow = true;
        })
      },


      //  比较数组并提交修改
      arrayFun(model) {

        // 触发表单验证
        this.$refs.form.validate(valid => {
          if (valid) {
            if (model.versions === this.versions) {
              console.log("不操作versions")
            } else if (model.versions === "") {
              model.removeVersionList = this.versions.split(',');
            } else if (this.versions == null) {
              model.addVersionList = model.versions.split(',');
            } else {
              var versionsArr = model.versions.split(',');
              var versionsArrConst = this.versions.split(',');
              model.addVersionList = [];
              model.removeVersionList = [];
              for (let i = 0; i < versionsArr.length; i++) {
                for (let j = 0; j < versionsArrConst.length; j++) {
                  if (versionsArr[i] === versionsArrConst[j]) {
                    delete versionsArr[i];
                    delete versionsArrConst[j];
                  }
                }
              }

              for (let i = 0; i < versionsArr.length; i++) {
                if (typeof (versionsArr[i]) != "undefined") {
                  model.addVersionList.push(versionsArr[i]);
                }
              }
              for (let i = 0; i < versionsArrConst.length; i++) {
                if (typeof (versionsArrConst[i]) != "undefined") {
                  model.removeVersionList.push(versionsArrConst[i]);
                }
              }
            }
            if (model.businessTypes === this.businessTypes) {
              console.log("不操作businessTypes");
            } else if (model.businessTypes === "") {
              model.removeBusinessTypeList = this.businessTypes.split(',');
            } else if (this.businessTypes == null) {
              model.addBusinessTypeList = model.businessTypes.split(',');
            } else {
              var businessTypesArr = model.businessTypes.split(',');
              var businessTypesArrConst = this.businessTypes.split(',');
              model.addBusinessTypeList = [];
              model.removeBusinessTypeList = [];
              for (let i = 0; i < businessTypesArr.length; i++) {
                for (let j = 0; j < businessTypesArrConst.length; j++) {
                  if (businessTypesArr[i] === businessTypesArrConst[j]) {
                    delete businessTypesArr[i];
                    delete businessTypesArrConst[j];
                  }
                }
              }
              for (let i = 0; i < businessTypesArr.length; i++) {
                if (typeof (businessTypesArr[i]) != "undefined") {
                  model.addBusinessTypeList.push(businessTypesArr[i]);
                }
              }
              for (let i = 0; i < businessTypesArrConst.length; i++) {
                if (typeof (businessTypesArrConst[i]) != "undefined") {
                  model.removeBusinessTypeList.push(businessTypesArrConst[i]);
                }
              }
            }
            // 之前将状态 1或者0 改为 true和false 现在将 true和false改为 1和0
            if (this.switchDownloadFlag === false) {
              this.model.downloadFlag = 0;
            } else {
              this.model.downloadFlag = 1;
            }
            Vue.ls.set("Dep_ID", this.model.depId, 7 * 24 * 60 * 60 * 1000)
            console.log(this.model);
            httpAction(this.url.edit, model, 'put').then((res) => {
              if (res.success) {
                this.$message.success(res.message);
                this.handleCancel();
                this.loadData();
              } else {
                this.$message.warning(res.message);
              }
            }).finally(() => {
            })
          } else {
            return false;
          }
        })
      },
      // 更改预览文件方法
      handleChange(info) {
        this.pdfLoading = true;
        this.pdfShow = false;
        if (info.file.status === 'done') {
          if (info.file.response.code === 200) {
            this.$message.success(`${info.file.name} 文件上传成功`);
            this.pdfShow = true;
            this.resetPreviewShow = true;
            this.loadData();
          } else {
            this.$message.error(`${info.file.name} 文件上传失败${info.file.response.message}`)
            this.pdfShow = true;
            this.pdfLoading = false;
          }
        } else if (info.file.status === 'error') {
          this.$message.error(`${info.file.name} 文件上传失败`)
          this.pdfShow = true;
          this.pdfLoading = false;
        }

      },

      //  显示专题
      showModel(model) {
        this.$refs.modalForm.edit(model, 0);
        this.$refs.modalForm.title = "选择专题"
      },
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';

  .titleIndex {

  }
</style>