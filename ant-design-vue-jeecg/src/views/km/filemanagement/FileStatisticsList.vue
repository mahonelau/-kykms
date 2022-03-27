<template>
  <a-card>
    <j-form-container>
      <a-form-model ref="form" slot="detail" style="text-align: left">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-model-item label="分类" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" placeholder="请选择分类"
                                 v-model="statisticsType" dictCode="dict_statisticsType"></j-dict-select-tag>
            </a-form-model-item>
          </a-col>
          <a-col :md="6" :sm="8">
          <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
             <a-button @click="loadData" type="primary" icon="snippets" style="margin-top: 4px">统计</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form-model>
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
        class="j-table-force-nowrap"
        @change="handleTableChange">
      </a-table>
    </j-form-container>

  </a-card>

</template>

<script>

  import {JeecgListMixin} from '@/mixins/JeecgListMixin'
  import {getAction} from '@/api/manage'

  export default {
    name: "FileStatisticsList",
    mixins: [JeecgListMixin],

    data() {
      return {
        statisticsType: 1,
        labelCol: {
          xs: {span: 24},
          sm: {span: 5},
        },
        wrapperCol: {
          xs: {span: 24},
          sm: {span: 16},
        },
        columns: [
          {
            title: '#',
            dataIndex: '',
            key: 'rowIndex',
            width: 40,
            align: "center",
            customRender: function (t, r, index) {
              return parseInt(index) + 1;
            }
          },
          {
            title: '统计维度',
            align: "center",
            dataIndex: 'itemText'
          },
          {
            title: '文件数量',
            align: "center",
            dataIndex: 'amount'
          },
          {
            title: '文件大小',
            align: "center",
            dataIndex: 'fileSize'
          }

        ],
        url: {
          list: '/KM/kmDoc/queryKmDocStatistics'
        }
      }
    },
    methods: {
      // 初始化加载列表数据
      loadData() {
        this.loading = true;
        var params = {};
        if (this.statisticsType < 1)
          this.statisticsType = 1

        params['statisticsType'] = this.statisticsType;
        getAction(this.url.list, params).then((res) => {
          if (res.success) {
            this.dataSource = res.result.records || res.result;
            if (res.result.total) {
              this.ipagination.total = res.result.total;
            } else {
              this.ipagination.total = 0;
            }
          }
          if (res.code === 510) {
            this.$message.warning(res.message)
          }
          this.loading = false;
        })
      }
    }

  }
</script>

<style scoped>
  @import '~@assets/less/common.less'
</style>