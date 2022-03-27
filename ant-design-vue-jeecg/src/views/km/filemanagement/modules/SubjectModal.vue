<template>
  <c-j-modal
    :title="title"
    :width="width"
    :visible="visible"
    @ok="handleOk"
    fullscreen
    :okButtonProps="{ class:{'jee-hidden': disableSubmit} }"
    @cancel="handleCancel"
    cancelText="取消">
    <subject-form ref="realForm" @ok="submitCallback" :disabled="disableSubmit"></subject-form>
  </c-j-modal>
</template>

<script>

 import SubjectForm from "./SubjectForm";
  export default {
    name: 'SubjectModal',
    components: {
      SubjectForm
    },
    data () {
      return {
        title:'',
        width:400,
        visible: false,
        disableSubmit: false
      }
    },
    methods: {
      edit (model,type) {
        this.visible=true;
        // 这个方法可以等待dom更新后，执行
        this.$nextTick(()=>{
          this.$refs.realForm.edit(model,type);
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
      handleCancel () {
        this.close()
      }
    }
  }
</script>