<template>
  <j-modal
    :title="title"
    :width="width"
    :visible="visible"
    @ok="handleOk"
    :okButtonProps="{ class:{'jee-hidden': disableSubmit} }"
    @cancel="handleCancel"
    cancelText="关闭">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="编号">
              <a-input placeholder="请输入编号" v-model="queryParam.serialNumber"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="分类">
              <j-dict-select-tag type="list" placeholder="请选择分类" v-model="queryParam.category"
                                 dictCode="km_dict_category"></j-dict-select-tag>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="标题">
                <a-input placeholder="请输入标题" v-model="queryParam.title"></a-input>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-model-item label="标签">
                <j-multi-select-tag type="list_multi" v-model="queryParam.businessTypeList" :trigger-change="true"
                                    placeholder="请选择标签" dictCode="km_dict_business"></j-multi-select-tag>
              </a-form-model-item>
            </a-col>

            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="关键字">
                <a-input placeholder="请输入关键字" v-model="queryParam.keywords"></a-input>
              </a-form-item>
            </a-col>

            <a-col :xl="14" :lg="16" :md="18" :sm="47">
              <a-form-item label="上传时间">
                <j-date :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择开始时间"
                        class="query-group-cust" v-model="queryParam.createTimeStart"></j-date>
                <span class="query-group-split-cust"></span>
                <j-date :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择结束时间"
                        class="query-group-cust" v-model="queryParam.createTimeEnd"></j-date>
              </a-form-item>
            </a-col>

          </template>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
              </a>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

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

    </a-table>

  </j-modal>
</template>

<script>
  import {JeecgListMixin} from '@/mixins/JeecgListMixin'
  import {httpPostAction} from '@/api/manage'
  export default {
    name: "ProjectListForm",
    mixins:[JeecgListMixin],
    data() {
      return {
        title: '收录文件到专题',

        width: 1200,
        visible: false,
        disableSubmit: false,
        topicId:'',
        // 表头
        columns: [
          {
            title: '#',
            dataIndex: '',
            key: 'rowIndex',
            width: 60,
            align: "center",
            customRender: function (t, r, index) {
              return parseInt(index) + 1;
            }
          },
          {
            title: '标题',
            align: "left",
            dataIndex: 'title',
            scopedSlots: {customRender: 'docTitle'},
            customCell: () => {
              return {
                style: {
                  'max-width': '30em',
                },
              };
            },
          },
          {
            title: '分类',
            align: "center",
            dataIndex: 'category_dictText',
            sorter: true,
            customCell: () => {
              return {
                style: {
                  'max-width': '6em',
                },
              };
            },
          },

          {
            title: '标签',
            align: "center",
            dataIndex: 'businessTypes_dictText',
            customCell: () => {
              return {
                style: {
                  'max-width': '10em',
                  overflow: 'hidden',
                  whiteSpace: 'nowrap',
                  textOverflow:'ellipsis'
                },
              };
            },
          },
          {
            title: '关键字',
            align: "center",
            dataIndex: 'keywords',
            customCell: () => {
              return {
                style: {
                  'max-width': '10em',
                  overflow: 'hidden',
                  whiteSpace: 'nowrap',
                  textOverflow:'ellipsis'
                },
              };
            },
          },
          {
            title: '上传人',
            align: "center",
            dataIndex: 'createBy',
            sorter: true,
            customCell: () => {
              return {
                style: {
                  'max-width': '3em',
                },
              };
            },
          },
          {
            title: '归属部门',
            align: "center",
            dataIndex: 'orgCode_dictText',
            sorter: true,
            customCell: () => {
              return {
                style: {
                  'max-width': '5em',
                },
              };
            },
          },
        ],
        url:{
          list:'/KM/kmDoc/listPassed',
          addDocToTopic:'/KM/kmDoc/addDocToTopic'
        }
      }
    },
    methods:{
      init(topicId){
        this.topicId=topicId;
        this.visible=true;
      },
      handleOk(){
          console.log("docIdsArr"+this.selectedRowKeys.toString());
          console.log('topicId:'+this.topicId);
          let httpurl = '';
          let method = '';
          let docIdsArr=this.selectedRowKeys.toString();
          httpurl += this.url.addDocToTopic;
          method = 'put';
          httpPostAction(httpurl, {topicId:this.topicId,docIds:docIdsArr}, method).then((res) => {
            console.log(res);
            if (res.success) {
              if(res.result==null || res.result == ""){
                this.$message.success("收录成功!");
              }else{

                this.$message.warning(res.result+"文件收录失败");
              }
              this.$emit('ok');
              this.visible = false;
            } else {
              this.$message.warning(res.message);
            }
          }).finally(() => {
          })
      },
      handleCancel () {
        this.close()
      },
      close () {
        this.visible = false;
      },
    }
  }
</script>

<style scoped>
  @import '~@assets/less/common.less';
</style>