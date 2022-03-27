/**
 * 新增修改完成调用 modalFormOk方法 编辑弹框组件ref定义为modalForm
 * 高级查询按钮调用 superQuery方法  高级查询组件ref定义为superQueryModal
 * data中url定义 list为查询列表  delete为删除单条记录  deleteBatch为批量删除
 */
import { filterObj } from '@/utils/util';
import { deleteAction, getAction,downFile,getFileAccessHttpUrl } from '@/api/manage'
import Vue from 'vue'
import { ACCESS_TOKEN, TENANT_ID } from "@/store/mutation-types"
import store from '@/store'
import {Modal} from 'ant-design-vue'
import {httpAction, httpPostAction} from "../api/manage";

export const RencentlyJeecgListMixin = {
  data(){
    return {
      /* 数据源 */
      rencentlyDataSource:[],
      /* 分页参数 */
      rencentlyIpagination:{
        current: 1,
        pageSize: 10,
        pageSizeOptions: ['10'],
        showTotal: (total, range) => {
          return range[0] + "-" + range[1] + " 共" + total + "条"
        },
        showQuickJumper: true,
        showSizeChanger: true,
        total: 0
      },
      /* 排序参数 */
      rencentlyIsorter:{
        column: 'createTime',
        order: 'desc',
      },
      /* table加载状态 */
      rencentlyLoading:false,
    }
  },
  created() {
      if(!this.disableMixinCreated){
        console.log(' -- 6666 -- ')
        this.rencentlyLoadData();
        //初始化字典配置 在自己页面定义
        this.initDictConfig();
      }
  },
  computed: {
    //token header
    tokenHeader(){
      let head = {'X-Access-Token': Vue.ls.get(ACCESS_TOKEN)}
      let tenantid = Vue.ls.get(TENANT_ID)
      if(tenantid){
        head['tenant-id'] = tenantid
      }
      return head;
    }
  },
  methods:{

    rencentlyLoadData(arg) {
      console.log("执行rencentlyLoadData方法")
      if(!this.url.recentlyList){
        this.$message.error("请设置url.recentlyList的属性!")
        return
      }
      //加载数据 若传入参数1则加载第一页的内容
      if (arg === 1) {
        this.rencentlyIpagination.current = 1;
      }
      var params = this.getQueryParams();//查询条件
      this.rencentlyLoading = true;
      getAction(this.url.recentlyList, params).then((res) => {
        if (res.success) {
          //update-begin---author:zhangyafei    Date:20201118  for：适配不分页的数据列表------------
          this.rencentlyDataSource = res.result.records||res.result;
          if(res.result.total)
          {
            this.rencentlyIpagination.total = res.result.total;
          }else{
            this.rencentlyIpagination.total = 0;
          }
          //update-end---author:zhangyafei    Date:20201118  for：适配不分页的数据列表------------
        }
        if(res.code===510){
          this.$message.warning(res.message)
        }
        this.rencentlyLoading = false;
      })
    },
    getQueryField() {
      //TODO 字段权限控制
      var str = "id,";
      this.columns.forEach(function (value) {
        str += "," + value.dataIndex;
      });
      return str;
    },

    onSelectChange(selectedRowKeys, selectionRows) {
      this.selectedRowKeys = selectedRowKeys;
      this.selectionRows = selectionRows;
    },
    onClearSelected() {
      this.selectedRowKeys = [];
      this.selectionRows = [];
    },
    searchQuery() {
      this.rencentlyLoadData(1);
    },
    superQuery() {
      this.$refs.superQueryModal.show();
    },
    searchReset() {
      this.queryParam = {}
      this.rencentlyLoadData(1);
    },

    reCalculatePage(count){
      //总数量-count
      let total=this.rencentlyIpagination.total-count;
      //获取删除后的分页数
      let currentIndex=Math.ceil(total/this.rencentlyIpagination.pageSize);
      //删除后的分页数<所在当前页
      if(currentIndex<this.rencentlyIpagination.current){
        this.rencentlyIpagination.current=currentIndex;
      }
      console.log('currentIndex',currentIndex)
    },

    recentlyHandleTableChange(pagination, filters, sorter) {
      //分页、排序、筛选变化时触发
      //TODO 筛选
      console.log(pagination)
      if (Object.keys(sorter).length > 0) {
        this.rencentlyIsorter.column = sorter.field;
        this.rencentlyIsorter.order = "ascend" == sorter.order ? "asc" : "desc"
      }
      this.rencentlyIpagination = pagination;
      this.rencentlyLoadData();
    },
    handleToggleSearch(){
      this.toggleSearchStatus = !this.toggleSearchStatus;
    },
    // 给popup查询使用(查询区域不支持回填多个字段，限制只返回一个字段)
    getPopupField(fields){
      return fields.split(',')[0]
    },
  }
}