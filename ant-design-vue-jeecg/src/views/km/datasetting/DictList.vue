<template>
  <a-card :bordered="false">
    <!-- 左侧面板 -->
    <div class="table-page-search-wrapper">

      <div class="table-operator" style="border-top: 5px">

        <a-button type="primary" icon="sync" @click="refleshCache()">刷新缓存</a-button>

      </div>

      <a-table
        ref="table"
        rowKey="id"
        size="middle"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        @change="handleTableChange">
        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">
            <a-icon type="edit"/>
            编辑
          </a>
          <a-divider type="vertical"/>
          <a @click="editDictItem(record)"><a-icon type="setting"/> 属性配置</a>

        </span>
      </a-table>

    </div>
    <dict-modal ref="modalForm" @ok="modalFormOk"></dict-modal>  <!-- 属性类型 -->
    <dict-item-list ref="dictItemList"></dict-item-list>
    <!--<dict-delete-list ref="dictDeleteList" @refresh="() =>loadData()"></dict-delete-list>-->
  </a-card>
</template>

<script>
  import { filterObj } from '@/utils/util';
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import DictModal from './modules/DictModal'
  import DictItemList from './DictItemList'
  import { getAction } from '@/api/manage'
  import { UI_CACHE_DB_DICT_DATA } from "@/store/mutation-types"
  import Vue from 'vue'

  export default {
    name: "DictList",
    mixins:[JeecgListMixin],
    components: {DictModal, DictItemList},
    data() {
      return {
        description: '这是文档属性页面',
        visible: false,
        // 查询条件
        queryParam: {
          dictCode: "",
          dictName: "",
        },
        // 表头
        columns: [
          {
            title: '操作',
            dataIndex: 'action',
            align: "center",
            scopedSlots: {customRender: 'action'},
          },
          {
            title: '属性名称',
            align: "left",
            dataIndex: 'dictName',
          },
          {
            title: '属性编号',
            align: "left",
            dataIndex: 'dictCode',
          },
          {
            title: '描述',
            align: "left",
            dataIndex: 'description',
          },

        ],
        dict: "",
        labelCol: {
          xs: {span: 8},
          sm: {span: 5},
        },
        wrapperCol: {
          xs: {span: 16},
          sm: {span: 19},
        },
        url: {
          list: "/sys/dict/listKM",
          delete: "/sys/dict/delete",
          exportXlsUrl: "sys/dict/exportXls",
          importExcelUrl: "sys/dict/importExcel",
          refleshCache: "sys/dict/refleshCache",
          queryAllDictItems: "sys/dict/queryAllDictItems",
        },
      }
    },
    computed: {
      importExcelUrl: function () {
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }
    },
    methods: {
      getQueryParams() {
        var param = Object.assign({}, this.queryParam, this.isorter);
        param.field = this.getQueryField();
        param.pageNo = this.ipagination.current;
        param.pageSize = this.ipagination.pageSize;
        if (this.superQueryParams) {
          param['superQueryParams'] = encodeURI(this.superQueryParams)
          param['superQueryMatchType'] = this.superQueryMatchType
        }
        return filterObj(param);
      },
      //取消选择
      cancelDict() {
        this.dict = "";
        this.visible = false;
        this.loadData();
      },
      //编辑属性数据
      editDictItem(record) {
        this.$refs.dictItemList.edit(record);
      },
      // 重置属性类型搜索框的内容
      searchReset() {
        var that = this;
        that.queryParam.dictName = "";
        that.queryParam.dictCode = "";
        that.loadData(this.ipagination.current);
      },
      openDeleteList(){
        this.$refs.dictDeleteList.show()
      },
      refleshCache(){
        getAction(this.url.refleshCache).then((res) => {
          if (res.success) {
            //重新加载缓存
            getAction(this.url.queryAllDictItems).then((res) => {
              if (res.success) {
                Vue.ls.remove(UI_CACHE_DB_DICT_DATA)
                Vue.ls.set(UI_CACHE_DB_DICT_DATA, res.result, 7 * 24 * 60 * 60 * 1000)
              }
            })
            this.$message.success("刷新缓存完成！");
          }
        }).catch(e=>{
          this.$message.warn("刷新缓存失败！");
          console.log("刷新失败",e)
        })
      }
    },
    watch: {
      openKeys(val) {
        console.log('openKeys', val)
      },
    },
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>