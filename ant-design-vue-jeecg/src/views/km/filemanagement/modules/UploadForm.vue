<template>
<div>
  <!--<a-spin :spinning="confirmLoading">-->
<div>

<a-form-model>
    <a-row :gutter="24">
      <a-col :span="12">
        <a-form-item label="分类" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" placeholder="请选择分类" v-model="model.category"
                             dictCode="km_dict_category"></j-dict-select-tag>
        </a-form-item>
      </a-col>
<!--      <a-col :xl="10" :lg="7" :md="8" :sm="24">-->
      <a-col :span="12">
        <a-form-model-item label="标签" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-multi-select-tag type="list_multi" v-model="model.businessTypes" :trigger-change="true"
                              placeholder="请选择标签" dictCode="km_dict_business"></j-multi-select-tag>
        </a-form-model-item>
      </a-col>
      <a-col :span="12">
          <a-form-model-item label="专题分类" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-textarea @click="showModel(model)" v-model="model.topicIds_dictText"
                        placeholder="请点击选择专题" readOnly :auto-size="{ minRows: 1, maxRows: 3 }">
            </a-textarea>
          </a-form-model-item>
        </a-col>
      <a-col :span="12">
          <a-form-item label="关键字" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input placeholder="请输入关键字" v-model="model.keywords"></a-input>
          </a-form-item>
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

  </a-row>

  </a-form-model>

     <div style="float:right;">
      <subject-modal ref="modalForm" @ok='handleOk' ></subject-modal>
    </div>
    <div style="float:right;">
      <a-button @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
    </div>
</div>

    <a-upload
      name="file"
      :multiple="true"
      :action="url"
      :headers="headers"
      @change="handleChange"
      :data= this.model
      :file-list="fileList"
      :showUploadList="{showPreviewIcon:true,showRemoveIcon:false}"
    >
      <a-button type="primary">
        <a-icon type="upload"/>
        批量上传文件
      </a-button>
    </a-upload>

  <!--</a-spin>-->
</div>
</template>

<script>
  import { ACCESS_TOKEN } from "@/store/mutation-types"
  import JMultiSelectTag from "@/components/dict/JMultiSelectTag"
  import SubjectModal from './SubjectModal'
  import Vue from "vue";
  import ACol from "ant-design-vue/es/grid/Col";

  export default {
    name: 'DraftsForm',
    components: {
      ACol,
      SubjectModal,
      JMultiSelectTag,
    },

    data() {
      return {
        model: {},
        fileData:{category:'123'},
        fileList:[],
        url: window._CONFIG['domianURL']+"/KM/kmDoc/uploadDoc",
        confirmLoading: false,
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
      }
    },
    computed: {
      formDisabled() {
        return this.disabled
      },
    },
    methods: {
      handleOk(){
        this.$forceUpdate()

      },
      //  显示专题
      showModel(model) {
        this.$refs.modalForm.edit(model, 0);
        this.$refs.modalForm.title = "选择专题"
      },

      edit(record) {
        this.model = Object.assign({}, record);
        this.visible = true;
        },

      submitForm() {
        this.$emit('ok');
      },

      searchReset(){
        this.model = {};
        this.$forceUpdate();
      },

      // 用来控制上传控件列表的显示
      handleChange(info) {

        var boolFilelistAdd=true;
        this.fileList.forEach((item,index)=>{
             if(item.uid==info.file.uid){
               boolFilelistAdd=false;
             }
        });
        if(boolFilelistAdd){
          this.fileList.push(info.file)
        }
        if(info.file.status !== 'uploading'){
          if (info.file.status === 'done') {
            if(info.file.response.code===200){
              this.$message.success(`${info.file.name} 文件上传成功`);
              // this.$emit('nd');
            }else{
              this.fileList.forEach((item,index)=>{
                if(item.uid===info.file.uid){
                   this.fileList[index].status='error';
                  this.fileList[index].response=info.file.response.message;
                }
              });
              this.$message.error(`${info.file.name} 文件上传失败${info.file.response.message}`);

            }
          } else if (info.file.status === 'error') {

            this.fileList.forEach((item,index)=>{
              if(item.uid===info.file.uid){
                this.fileList[index].status='error';
              }
            });
            this.$message.error(`${info.file.name} 文件上传失败`);
          }
        }
      },
    }
  }
</script>