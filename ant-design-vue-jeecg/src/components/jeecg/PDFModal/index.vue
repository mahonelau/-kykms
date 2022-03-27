<template>
  <div >
    <div class="adiv" :style="styleVar" v-if="pdfLoading">
    <a-spin class="aspin"  :spinning="pdfLoading" tip="预览内容正在加载中，请稍等...">
      <p v-if="pdfLoading"></p>
    </a-spin>
    </div>

    <div style="color: #606266">
      <iframe  :v-show="showPDFModal" :id="iframeID" :width="iframeWidth" :height="iframeHeight">
      </iframe>
    </div>
  </div>
</template>

<script>
  import {getActionPDF} from "../../../api/manage";



  export default {
    name: "PDFModal",
    props: {
      showPDFModal: {
        type: Boolean,
        default: true
      },
      iframeID: {
        type: String,
        default: 'iframeID'
      },
      iframeWidth: {
        type: String,
        default: '700'
      },
      iframeHeight: {
        type: String,
        default: '95%'
      },
      PDFurl: {
        type: String,
        default: ''
      },


    },
    computed: {
      styleVar() {
        return {
          '--width': this.iframeWidth+"px",
        }
      }
    },
    data() {
      return{
        pdfLoading: true
      }
    },

    mounted() {
      this.loadPDF();
    },
    methods: {

      loadPDF() {

        let iframeID = document.querySelector("#" + this.iframeID);
        getActionPDF(this.PDFurl).then((res) => {
          this.pdfLoading = false;
          // 重点
          let blobPDF = new Blob([res], {
            type: `application/pdf;charset-UTF-8` // word文档为msword,pdf文档为pdf
          });
          iframeID.src = "/pdfjs/web/viewer.html?file="+window.URL.createObjectURL(blobPDF);
        })

      }
    }

  }
</script>

<style scoped>
 iframe{
   position:absolute;
   z-index:1
 }
  .adiv{
    text-align: center;
    position:absolute;
    z-index:10;
    width:var(--width);
    height: 100%;
  }
  .aspin{
    height: 100%;
    line-height:50%;
  }
</style>