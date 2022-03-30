<template>
  <a-spin :spinning="confirmLoading">
    <a-upload
      name="file"
      :multiple="true"
      :action="url"
      :headers="headers"
      @change="handleChange"
      :file-list="fileList"
      :showUploadList="{showPreviewIcon:true,showRemoveIcon:false}"
    >
      <a-button type="primary">
        <a-icon type="upload"/>
        批量上传文件
      </a-button>
    </a-upload>

  </a-spin>

</template>

<script>
  import { ACCESS_TOKEN } from "@/store/mutation-types"
  import JMultiSelectTag from "@/components/dict/JMultiSelectTag"
  import Vue from "vue";


  export default {
    name: 'DraftsForm',
    components: {
      JMultiSelectTag,
    },

    data() {
      return {
        fileList:[],
        url: window._CONFIG['domianURL']+"/KM/kmDoc/uploadDoc",
        confirmLoading: false,
        headers: {
          authorization: 'authorization-text',
          'X-Access-Token': Vue.ls.get(ACCESS_TOKEN)
        },
      }
    },
    computed: {
      formDisabled() {
        return this.disabled
      },
    },
    methods: {

      edit(record) {
        this.model = Object.assign({}, record);
        this.visible = true;
      },
      submitForm() {
        this.$emit('ok');
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