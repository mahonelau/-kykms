<template>
  <j-modal
    :title="title"
    :width="width"
    :visible="visible"

    @ok="handleOk"
    :okButtonProps="{ class:{'jee-hidden': true} }"
    @cancel="handleCancel"
    cancelText="关闭"
     :cancelButtonProps="{ class:{'jee-hidden': false} }">
    <upload-form ref="realForm" @ok="submitCallback"  @nd="Newdate" :disabled="disableSubmit"></upload-form>
    <span style="float: right">   文件大小限制:<2M</span>

  </j-modal>
</template>

<script>

 import UploadForm from "./UploadForm";
  export default {
    name: 'DraftsModal',
    components: {
      UploadForm
    },
    data () {
      return {
        title:'',
        width:800,
        visible: false,
        disableSubmit: false
      }
    },
    methods: {
      edit (record) {
        this.visible=true
        this.$nextTick(()=>{
          this.$refs.realForm.edit(record);
        })
      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      handleOk () {
        this.$refs.realForm.submitForm();
      },
      submitCallback(){
        this.$emit('ok');
        this.visible = false;
      },
      Newdate(){
        this.$emit('ok');
      },
      handleCancel () {
        this.$refs.realForm.submitForm();
        // this.close()
      }
    }
  }
</script>