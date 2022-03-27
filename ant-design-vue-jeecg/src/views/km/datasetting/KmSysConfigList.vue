<template>
  <a-card :bordered="false">
    <!-- table区域-begin -->
    <div>
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
        class="j-table-force-nowrap"
        @change="handleTableChange">

        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无图片</span>
          <img v-else :src="getImgView(text)" height="25px" alt="" style="max-width:80px;font-size: 12px;font-style: italic;"/>
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
          <a @click="handleEdit(record)">编辑</a>
        </span>

      </a-table>
    </div>

    <km-sys-config-modal ref="modalForm" @ok="modalFormOk"></km-sys-config-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import KmSysConfigModal from './modules/KmSysConfigModal'

  export default {
    name: 'KmSysConfigList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      KmSysConfigModal
    },
    data () {
      return {
        description: '系统参数管理页面',
        // 表头
        columns: [
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            fixed:"left",
            width:80,
            scopedSlots: { customRender: 'action' }
          },
          {
            title:'参数编码',
            align:"left",
            dataIndex: 'itemCode'
          },
          {
            title:'参数值',
            align:"center",
            dataIndex: 'itemValue'
          },
          {
            title:'参数说明',
            align:"left",
            dataIndex: 'itemName'
          },

        ],
        url: {
          list: "/KM/kmSysConfig/list",
          delete: "/KM/kmSysConfig/delete",
          deleteBatch: "/KM/kmSysConfig/deleteBatch",
          exportXlsUrl: "/KM/kmSysConfig/exportXls",
          importExcelUrl: "KM/kmSysConfig/importExcel",

        },
        dictOptions:{},
        superFieldList:[],
      }
    },
    created() {
    this.getSuperFieldList();
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      },
    },
    methods: {
      initDictConfig(){
      },
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'string',value:'itemCode',text:'itemCode'})
        fieldList.push({type:'string',value:'itemValue',text:'itemValue'})
        fieldList.push({type:'string',value:'itemName',text:'itemName'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>