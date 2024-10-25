
import BJModal from './BJModal'
import PDFModal from './PDFModal'
export default {
  install(Vue) {
    Vue.component('BJModal',BJModal)
    Vue.component('PDFModal',PDFModal)
  }
}