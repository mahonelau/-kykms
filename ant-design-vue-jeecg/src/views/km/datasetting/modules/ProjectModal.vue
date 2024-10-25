<template>
  <a-card :bordered="false" v-show="visible">
      <div class="table-operator">
        <a-button @click="handleAddModal()" type="primary" icon="plus">收录新文件</a-button>
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
        @change="handleTableChange">

        <span slot="action" slot-scope="text, record">
                <a-popconfirm title="确定移除吗?" @confirm="() => removeTopic(record.id)">
                  <a>移除</a>
                </a-popconfirm>
        </span>

      </a-table>
    <project-list-form ref="modalForm" @ok="modalFormOk" />
  </a-card>
</template>

<script>
  import {JeecgListMixin} from '@/mixins/JeecgListMixin'
  import ProjectListForm from "./ProjectListForm";
  import {getAction,httpPostAction} from '@/api/manage'
  export default {
    name: 'DepartAuthModal',
    mixins: [JeecgListMixin],
    components:{
      ProjectListForm
    },
    data(){
      return {
        visible:false,
        columns: [
          {
            title: '#',
            dataIndex: '',
            key:'rowIndex',
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
          {
            title:'标题',
            align:"left",
            customCell: () => {
              return {
                style: {
                  'max-width': '30em',
                },
              };
            },
            dataIndex: 'title'
          },
          {
            title:'关键字',
            align:"left",
            dataIndex: 'keywords'
          },
          {
            title:'编号',
            align:"center",
            dataIndex: 'serialNumber'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            fixed:"right",
            width:"4em",
            scopedSlots: { customRender: 'action' }
          }
        ],
        url: {
          list: "/KM/kmDoc/listTopicPageList",
          removeDocFromTopic:"KM/kmDoc/removeDocFromTopic",
        },
        dictOptions:{},
        superFieldList:[],
        topicId:'',
      }
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      },
    },
    methods: {
      initDictConfig(){
      },
      created() {
        this.getSuperFieldList();
      },
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'string',value:'serialNumber',text:'编号',dictCode:''})
        fieldList.push({type:'string',value:'title',text:'文件名',dictCode:''})
        fieldList.push({type:'string',value:'keywords',text:'关键字',dictCode:''})
        this.superFieldList = fieldList
      },
      getOrderMain(topicId) {
        this.topicId=topicId;
        if(this.topicId!==''){
          this.visible=true;
        }else{
          this.visible=false;
        }
        this.loadData(1);
      },
      loadData(arg) {
        if (arg === 1) {
          this.ipagination.current = 1;
        }

        var params = this.getQueryParams();
        if(this.topicId!=null) {
          this.loading=true;
          getAction(this.url.list, {
            topicId: this.topicId, pageNo: this.ipagination.current,
            pageSize: this.ipagination.pageSize
          }).then((res) => {
            if (res.success) {
              this.dataSource = res.result.records;
              console.log(this.dataSource);
              this.ipagination.total = res.result.total;
            } else {
              this.dataSource = null;
            }
            this.loading=false;
          })

        }
      },
      handleAddModal(){
        this.$refs.modalForm.init(this.topicId);
      },
      removeTopic(docId){
        let httpurl = '';
        let method = '';
        httpurl += this.url.removeDocFromTopic;
        method = 'put';
        httpPostAction(httpurl, {topicId:this.topicId,docId:docId}, method).then((res) => {
          if (res.success) {
            this.$message.success("取消收录成功!");
            this.loadData(1);
          } else {
            this.$message.warning("取消收录失败!");
          }
        }).finally(() => {})
      }
    },
  }
</script>

<style scoped>
  @import '~@assets/less/common.less';
</style>