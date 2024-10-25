<template>
  <a-card :bordered="false">

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button type="primary" icon="upload" @click="newBackup">创建新备份</a-button>
    </div>
    <!-- table区域-begin -->
    <div>
      <a-spin size="large" :spinning=spinning >
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

                <span slot="action" slot-scope="text, record">
                  <a-space>
                    <a-icon  type="download" title="下载" @click="download(record)"
                             :style="{ fontSize: '16px', color: '#1890FF'}"/>
                <a-divider type="vertical"/>
                    <a-icon type="delete" title="删除"  @click="deleteBackup(record)"
                            :style="{ fontSize: '16px', color: '#1890FF' }"/>
                  </a-space>
               </span>

      </a-table>
      </a-spin>
    </div>

  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import { downloadFileName, httpPostAction } from '@api/manage'

  export default {
    name: 'KmSysDbBackupList',
    mixins:[JeecgListMixin, mixinDevice],
    data () {
      return {
        spinning: false,
        description: '数据库备份管理页面',
        // 表头
        columns: [
          {
            title:'文件名',
            align:"left",
            dataIndex: 'fileName'
          },
          {
            title:'大小',
            align:"center",
            dataIndex: 'size'
          },
          {
            title:'备份时间',
            align:"left",
            dataIndex: 'createTime'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            fixed:"right",
            width:80,
            scopedSlots: { customRender: 'action' }
          },
        ],
        url: {
          list: "/KM/kmSysDbBackup/list",
          add: "/KM/kmSysDbBackup/add",
          delete: "/KM/kmSysDbBackup/delete",
          downloadById: "/KM/kmSysDbBackup/downloadById",
        },
      }
    },
    created() {
     },
    methods: {
      //下载文件
      download(record) {
        this.$notification.success({
          message: '文件开始下载...',
          duration: 1,
        });
        downloadFileName(this.url.downloadById, {id: record.id})
      },
      //创建备份
      newBackup(record) {
        let httpurl = '';
        let method = '';
        httpurl += this.url.add;
        method = 'post';
        this.spinning = true
        httpPostAction(httpurl, {}, method).then((res) => {
          if (res.success) {
            this.$message.success("创建备份成功!");
            this.loadData();
          } else {
            this.$message.warning("创建备份失败!");
          }
        }).finally(() => {
          this.spinning = false
        })
      },
      //删除
      deleteBackup(record) {
        let httpurl = '';
        let method = '';
        httpurl += this.url.delete;
        method = 'delete';
        this.spinning = true
        httpPostAction(httpurl, {id: record.id}, method).then((res) => {
          if (res.success) {
            this.$message.success("删除成功!");
            this.loadData();
            record.favourite = 0;
          } else {
            this.$message.warning("删除失败!");
          }
        }).finally(() => {
          this.spinning = false
        })
      },
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>