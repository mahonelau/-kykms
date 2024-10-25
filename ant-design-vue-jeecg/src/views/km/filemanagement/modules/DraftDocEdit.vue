<template>
  <div>
      <h3 style="color: #303133;margin-left: 10px;padding:5px">
        <div v-show="editOrCompareFlag>0" @click="navBack" style="float:left;color: #303133; ;width: 10%;text-align: left">
          <a style="color:#303133;">
            <a-space>
            <a-icon type="arrow-left"/>
            <span>返回</span>
            </a-space>
          </a>
        </div>

        <div style="color: #303133;float:right;width: 90%;text-align: center">
          <b>{{ model.title+'.'+model.fileType}}</b>
        </div>
    </h3>
    <a-divider style="margin:0px"></a-divider>
      <a-spin :spinning="confirmLoading" style="margin-top: 10px;float: left;width: 690px">
        <j-form-container v-if="editOrCompareFlag === 0">
          <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
            <a-row>
              <a-col v-if="!dupCheck" :span="24">
                <a-form-model-item label="标题"   :labelCol="labelCol2" :wrapperCol="wrapperCol2"
                                   prop="title">
                  <a-textarea v-model="model.title" placeholder="请输入标题"
                              :auto-size="{ minRows: 1, maxRows: 3 }"></a-textarea>
                </a-form-model-item>
              </a-col>
              <a-col v-if="dupCheck" :span="21">
                <a-form-model-item label="标题" style="margin-left: -15px" :labelCol="labelCol3" :wrapperCol="wrapperCol3"
                                   prop="title">
                  <a-textarea v-model="model.title" placeholder="请输入标题"
                              :auto-size="{ minRows: 1, maxRows: 3 }"></a-textarea>
                </a-form-model-item>
              </a-col>
              <a-col v-if="dupCheck" :span="3" style="text-align: center;margin-top: 4px">
                <a-button type="primary" @click="searchTitleDupDoc">查重</a-button>
              </a-col>
              <a-col :span="12">
                <a-form-model-item label="分类" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="category">
                  <j-dict-select-tag type="list" placeholder="请选择分类" v-model="model.category"
                                     dictCode="km_dict_category"></j-dict-select-tag>
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
                <a-form-model-item label="标签" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="businessTypes">
                  <j-multi-select-tag type="list_multi" v-model="model.businessTypes" :trigger-change="true"
                                      placeholder="请选择标签" dictCode="km_dict_business"></j-multi-select-tag>
                </a-form-model-item>
              </a-col>
              <a-col :span="12">
                <a-form-model-item label="归属部门" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="depId">
                  <j-select-depart v-model="model.depId" :multi="false"></j-select-depart>
                </a-form-model-item>
              </a-col>
              <a-col :span="12">
                <a-form-model-item label="公开范围" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="publicRemark">
                  <j-dict-select-tag type="list" placeholder="请选择公开范围" v-model="model.publicRemark"
                                     dictCode="dict_public_remark"></j-dict-select-tag>
                </a-form-model-item>
              </a-col>
              <a-col :span="24">
                <a-form-model-item label="备注" :labelCol="labelCol2" :wrapperCol="wrapperCol2" prop="remark">
                  <a-textarea v-model="model.remark" placeholder="请输入备注"
                              :auto-size="{ minRows: 1, maxRows: 3 }"></a-textarea>
                </a-form-model-item>
              </a-col>
              <a-col :span="6">
                <a-form-model-item label="允许下载" :labelCol="labelCol1" :wrapperCol="wrapperCol1"
                                   prop="switchDownloadFlag">
                  <a-switch checked-children="是" un-checked-children="否" v-model="switchDownloadFlag"/>
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
                      :action="changePreviewFileUrl+model.id"
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
                <a-space>
                <a-button type="primary" @click="editCommit(model,false)">确认修改</a-button>


                <a-button v-if="releaseButton" style="margin-left: 40px" @click="editCommit(model,true)">修改并发布</a-button>


                <a-button style="margin-left: 40px" @click="handleCancel">取消修改</a-button>
                <a-button v-if="dupCheck" style="margin-left: 40px;" @click="searchContentDupDoc">内容查重</a-button>
                </a-space>
              </a-col>
            </a-row>
          </a-form-model>
          <subject-modal ref="modalForm"></subject-modal>
        </j-form-container>
        <div v-if="editOrCompareFlag === 1">
          <a-form layout="inline" style="min-height: 10px">
            <a-row>
              <a-col v-for="(item,index) in dupDocList" :xl="24" :lg="24" :md="24" :sm="24" :key="index">
                <p style="text-align: left;margin-left: 20px">
                  <span class="titleIndex"><b>{{index+1+'、'}}</b></span>
                  <span @click="compareDupDocPreview(item.id,item.title)"><a style="color: #303133" v-html="item.title"></a></span>
                </p>
              </a-col>
            </a-row>
          </a-form>
        </div>
      </a-spin>

      <div style="float: left;width: 690px" v-if="editOrCompareFlag === 2" >
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
  </div>
</template>

<script>
  import {ACCESS_TOKEN} from "@/store/mutation-types"
  import '@/assets/less/TableExpand.less'
  import SubjectModal from '../modules/SubjectModal'
  import { httpPostAction, httpAction, getAction, downloadFileName } from '@/api/manage'
  import Vue from 'vue'

  export default {
    name: 'DraftDocEdit',
    props:{
      releaseButton: Boolean,
      dupCheck: Boolean,
      model:{
        type: Object
      }
    },
    components: {
      SubjectModal,
    },
    data() {
      return {
        //编辑或比较文件模式 0：编辑文件元数据 1：显示相似文档列表 2：预览相似文档并比较
        editOrCompareFlag: 0,
        // 重复标题列表
        dupDocList: [],
        dataUpdated: false,
        switchDownloadFlag: true,
        PDFurl: '',
        TitlePDFurl: '',
        changePreviewFileUrl: window._CONFIG['domianURL'] + "/KM/kmDoc/changePreviewFile?docId=",
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
        pdfLoading: false,
        pdfShow: true,
        resetPreviewShow: false,
        validatorRules: {
          depId: [{required: true, message: '请选择部门!'}],
        },
        businessTypes: '',
        title: "编辑",
        width: "100%",
        url: {
          docDPCheck: '/KM/kmDoc/docDPCheck',
          downloadKmDoc: '/KM/kmDoc/downloadKmDoc',
          previewKmDoc: '/KM/kmDoc/previewKmDoc',
          edit: '/KM/kmDoc/editDraft',
          resetPreviewFile: '/KM/kmDoc/resetPreviewFile',
          editAndRelease: '/KM/kmDoc/editAndRelease',
          editRelease: '/KM/kmDoc/editRelease',

          list: '/KM/kmDoc/listDraft',
          delete: '/KM/kmDoc/delete',
          deleteBatch: '/KM/kmDoc/deleteBatch',
          editBatch: '/KM/kmDoc/submitAuditBatch',
        },
      }
    },
    created() {
      this.init()
    },
    methods: {
      navBack(){
        if(this.editOrCompareFlag > 0)
          this.editOrCompareFlag -= 1
      },
      // 点击查重触发方法
      searchTitleDupDoc() {
        // this.title = '查找相似标题：' + this.model.title
        this.dupDocList = []
        let params = {}
        params['docId'] = this.model.id;
        params['checkType'] = "1";
        params['pageSize']="20";
        getAction(this.url.docDPCheck, params).then((res) => {
          if (res.success) {
            this.dupDocList = res.result.kmSearchResultVOPage.records
          } else {
            this.$message.error('检索失败')
          }
          this.loading = false
        })
        this.editOrCompareFlag = 1
      },
      // 内容查重触发方法
      searchContentDupDoc() {
        this.title = '查找相似内容：' + this.model.title
        this.dupDocList = []
        let params = {}
        params['docId'] = this.model.id;
        params['checkType'] = "2";
        params['pageSize']="20";
        getAction(this.url.docDPCheck, params).then((res) => {
          if (res.success) {
            this.dupDocList = res.result.kmSearchResultVOPage.records
          } else {
            this.$message.error('检索失败')
          }
          this.loading = false
        })
        // this.boolCheckTitle = false
        this.editOrCompareFlag = 1
      },
      // 显示查重对比文件的预览窗口
      compareDupDocPreview(id,title) {
        // this.showTitlePDF=true;
        this.editOrCompareFlag = 2
        this.title=" ";
        this.TitlePDFurl =   this.url.previewKmDoc + '?docId=' + id
      },
       initDictConfig() {
      },
      // 下载文件
      downloadKmDoc(record) {
        this.$notification.success({
          message: '文件开始下载...',
          duration: 1,
        });
        downloadFileName(this.url.downloadKmDoc, {docId: record.id})
      },
      //显示预览窗口，并初始化配置
      init() {
        this.editOrCompareFlag = 0
        if (this.model.depId == null || this.model.depId === "") {
          var depId = Vue.ls.get("Dep_ID");
          this.model.depId = depId
        }
        this.businessTypes = this.model.businessTypes;
        this.PDFurl =  this.url.previewKmDoc + "?docId=" + this.model.id;

        this.pdfLoading = true;
        this.pdfShow = true;
        if (this.model.previewFileId !== this.model.originalPreviewFileId) {
          this.resetPreviewShow = true;
        }
        //  将是否允许下载状态从 1 和 0 转为 switch的true和false
        if (this.model.downloadFlag === 1) {
          this.switchDownloadFlag = true;
        } else {
          this.switchDownloadFlag = false;
        }
        console.log("init end.....this.editOrCompareFlag：",this.editOrCompareFlag)
      },
      //关闭取消
      handleCancel() {
        console.log("handleCancel, dataUpdated:", this.dataUpdated)
        if(this.dataUpdated)
          this.handleOK()
        else
          this.$emit("cancel")
      },
      //确认修改并关闭
      handleOK() {
        this.$emit("ok")
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
            this.dataUpdated = true
            this.PDFurl =this.url.previewKmDoc + "?docId=" + this.model.id +"&nounce=" + Math.random();
            // this.loadData();
          } else {
            this.$message.warning("恢复失败!");
            this.pdfShow = true;
            this.pdfLoading = false;
          }
        }).finally(() => {
          this.pdfShow = true;
        })
      },

      //提交修改
      editCommit(model,releaseFlag) {
        // 触发表单验证
        this.$refs.form.validate(valid => {
          if (valid) {
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
            let actionUrl = this.url.edit
            if(this.model.status === 2)
              actionUrl = this.url.editRelease
            if(releaseFlag)
              actionUrl = this.url.editAndRelease
            httpAction(actionUrl, model, 'put').then((res) => {
              if (res.success) {
                this.$message.success(res.message);
                this.handleOK();
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
        this.pdfLoading = true
        this.pdfShow = false
        if (info.file.status === 'done') {
          if (info.file.response.code === 200) {
            this.$message.success(`${info.file.name} 文件上传成功`)
            this.pdfShow = true;
            this.resetPreviewShow = true
            this.dataUpdated = true
            this.PDFurl =this.url.previewKmDoc + "?docId=" + this.model.id +"&nounce=" + Math.random();
            // this.loadData();
          } else {
            this.$message.error(`${info.file.name} 文件上传失败${info.file.response.message}`)
            this.pdfShow = true
            this.pdfLoading = false
          }
        } else if (info.file.status === 'error') {
          this.$message.error(`${info.file.name} 文件上传失败`)
          this.pdfShow = true
          this.pdfLoading = false
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
</style>