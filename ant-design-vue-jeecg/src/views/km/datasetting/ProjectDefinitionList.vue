<template>
  <a-row :gutter="10">
    <a-col :md="12" :sm="24">
      <a-card :bordered="false">
        <!-- 按钮操作区域 -->
        <div>
          <!-- 树-->
          <a-col :md="24" :sm="48">
            <div class="table-operator">
              <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
            </div>
            <a-table
              ref="table"
              size="middle"
              rowKey="id"
              :columns="columns"
              :dataSource="dataSource"
              :pagination="ipagination"
              :loading="loading"
              :expandedRowKeys="expandedRowKeys"
              :customRow="clickThenCheck"
              :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange,type:type}"
              @change="handleTableChange"
              @expand="handleExpand"
              v-bind="tableProps">
                <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>
          <a-divider type="vertical"/>
          <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record)">
            <a>删除</a>
          </a-popconfirm>
          <a-divider type="vertical"/>
          <a @click="handleAddSub(record)">添加下级</a>
        </span>
            </a-table>
          </a-col>
        </div>
        <sysCategory-modal ref="modalForm" @ok="modalFormOk"></sysCategory-modal>
      </a-card>

    </a-col>
    <a-col :md="12" :sm="24">
          <project-modal ref="projectModal"/>
    </a-col>

  </a-row>

</template>
<script>

  import SysCategoryModal from "./modules/SysCategoryModal";
  import {getAction, deleteAction} from '@/api/manage'
  import {JeecgListMixin} from '@/mixins/JeecgListMixin'
  import ProjectModal from './modules/ProjectModal'
  import Vue from "vue";

  export default {
    name: 'DepartList',
    mixins: [JeecgListMixin],
    components: {
      ProjectModal,
      SysCategoryModal
    },
    data() {
      return {
        description: '分类字典管理页面',
        // 表头
        columns: [
          {
            title: '专题名称',
            align: "left",
            dataIndex: 'name'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align: "center",
            scopedSlots: {customRender: 'action'},
          }
        ],
        // 选择参数
        type: "radio",
        isorter: false,
        url: {
          list: "/sys/category/rootList",
          childList: "/sys/category/childList",
          getChildListBatch: "/sys/category/getChildListBatch",
          delete: "/sys/category/delete"
        },
        expandedRowKeys: [],
        hasChildrenField: "hasChild",
        pidField: "pid",
        dictOptions: {},
        subExpandedKeys: [],
      }
    },
    computed: {
      tableProps() {
        let _this = this
        return {
          // 列表项是否可选择
          rowSelection: {
            selectedRowKeys: _this.selectedRowKeys,
            onChange: (selectedRowKeys) => _this.selectedRowKeys = selectedRowKeys
          }
        }
      }
    },
    methods: {
      // 初始化加载专题数据
      loadData(arg) {
        if (arg == 1) {
          this.ipagination.current = 1
        }
        this.loading = true
        let params = this.getQueryParams()
        return new Promise((resolve) => {
          getAction(this.url.list, params).then(res => {
            if (res.success) {
              let result = res.result
              if (Number(result.total) > 0) {
                this.ipagination.total = Number(result.total)
                this.dataSource = this.getDataByResult(res.result.records)
                return this.loadDataByExpandedRows(this.dataSource)
                //update--end--autor:lvdandan-----date:20201204------for：JT-31 删除成功后默认展开已展开信息
              } else {
                this.ipagination.total = 0
                this.dataSource = []
              }
            } else {
              this.$message.warning(res.message)
            }
          }).finally(() => {
            this.loading = false
          })
        })
      },
      getDataByResult(result) {
        if (result && result.length > 0) {
          return result.map(item => {
            //判断是否标记了带有子节点
            if (item[this.hasChildrenField] == '1') {
              let loadChild = {id: item.id + '_loadChild', name: 'loading...', isLoading: true}
              item.children = [loadChild]
            }
            return item
          })
        }
      },
      handleExpand(expanded, record) {
        // 判断是否是展开状态
        if (expanded) {
          this.expandedRowKeys.push(record.id)
          if (record.children.length > 0 && record.children[0].isLoading === true) {
            let params = this.getQueryParams();//查询条件
            params[this.pidField] = record.id
            getAction(this.url.childList, params).then((res) => {
              if (res.success) {

                if (res.result && res.result.length > 0) {
                  record.children = this.getDataByResult(res.result)
                  this.dataSource = [...this.dataSource]
                } else {
                  record.children = ''
                  record.hasChildrenField = '0'
                }
              } else {
                this.$message.warning(res.message)
              }
            })
          }
        } else {
          let keyIndex = this.expandedRowKeys.indexOf(record.id)
          if (keyIndex >= 0) {
            this.expandedRowKeys.splice(keyIndex, 1);
          }
        }
      },
      initDictConfig() {
      },
      clickThenCheck(record) {
        return {
          on: {
            click: () => {
              this.onSelectChange(record.id.split(","), [record]);
            }
          }
        };
      },
      onSelectChange(selectedRowKeys, selectionRows) {
        this.selectedRowKeys = selectedRowKeys;
        this.selectionRows = selectionRows;
        this.$refs.projectModal.getOrderMain(this.selectedRowKeys[0]);
      },
      editOk(formData, arr) {
        if (arr && arr.length > 0) {
          for (let i = 0; i < arr.length; i++) {
            if (arr[i].id == formData.id) {
              arr[i] = formData
              break
            } else {
              this.editOk(formData, arr[i].children)
            }
          }
        }
      },
      async addOk(formData, arr) {
        if (!formData[this.pidField]) {
          this.loadData()
        } else {
          this.expandedRowKeys = []
          console.log("22222", arr)
          for (let i of arr) {
            await this.expandTreeNode(i)
          }
        }
      },
      expandTreeNode(nodeId) {
        return new Promise((resolve, reject) => {
          this.getFormDataById(nodeId, this.dataSource)
          let row = this.parentFormData
          this.expandedRowKeys.push(nodeId)
          let params = this.getQueryParams();//查询条件
          params[this.pidField] = nodeId
          getAction(this.url.childList, params).then((res) => {
            console.log("11111", res)
            if (res.success) {
              if (res.result && res.result.length > 0) {
                row.children = this.getDataByResult(res.result)
                this.dataSource = [...this.dataSource]
                resolve()
              } else {
                row.children = ''
                row.hasChildrenField = '0'
                reject()
              }
            } else {
              reject()
            }
          })
        })
      },
      getFormDataById(id, arr) {
        if (arr && arr.length > 0) {
          for (let i = 0; i < arr.length; i++) {
            if (arr[i].id == id) {
              this.parentFormData = arr[i]
            } else {
              this.getFormDataById(id, arr[i].children)
            }
          }
        }
      },
      handleAddSub(record) {
        this.subExpandedKeys = [];
        this.getExpandKeysByPid(record.id, this.dataSource, this.dataSource)
        this.$refs.modalForm.subExpandedKeys = this.subExpandedKeys;
        this.$refs.modalForm.title = "添加子分类";
        this.$refs.modalForm.edit({'pid': record.id});
        this.$refs.modalForm.disableSubmit = false;

      },


      handleDelete: function (record) {
        let that = this;
        deleteAction(that.url.delete, {id: record.id}).then((res) => {
          if (res.success) {
            that.loadData();
            this.$refs.projectModal.getOrderMain('');
          } else {
            that.$message.warning(res.message);
          }
        });
      },
      // 添加子分类时获取所有父级id
      getExpandKeysByPid(pid, arr, all) {
        if (pid && arr && arr.length > 0) {
          for (let i = 0; i < arr.length; i++) {
            if (arr[i].id == pid) {
              this.subExpandedKeys.push(arr[i].id)
              this.getExpandKeysByPid(arr[i]['pid'], all, all)
            } else {
              this.getExpandKeysByPid(pid, arr[i].children, all)
            }
          }
        }
      },
      // 根据已展开的行查询数据（用于保存后刷新时异步加载子级的数据）
      loadDataByExpandedRows(dataList) {
        if (this.expandedRowKeys.length > 0) {
          return getAction(this.url.getChildListBatch, {parentIds: this.expandedRowKeys.join(',')}).then(res => {
            if (res.success && res.result.records.length > 0) {
              //已展开的数据批量子节点
              let records = res.result.records
              const listMap = new Map();
              for (let item of records) {
                let pid = item[this.pidField];
                if (this.expandedRowKeys.join(',').includes(pid)) {
                  let mapList = listMap.get(pid);
                  if (mapList == null) {
                    mapList = [];
                  }
                  mapList.push(item);
                  listMap.set(pid, mapList);
                }
              }
              let childrenMap = listMap;
              let fn = (list) => {
                if (list) {
                  list.forEach(data => {
                    if (this.expandedRowKeys.includes(data.id)) {
                      data.children = this.getDataByResult(childrenMap.get(data.id))
                      fn(data.children)
                    }
                  })
                }
              }
              fn(dataList)
            }
          })
        } else {
          return Promise.resolve()
        }
      },


    }

  }
</script>
<style scoped>

  @import '~@assets/less/common.less';

  .ant-card-body .table-operator {
    margin: 5px;
  }

  .anty-form-btn {
    width: 100%;
    text-align: center;
  }

  .anty-form-btn button {
    margin: 0 5px;
  }

  .anty-node-layout .ant-layout-header {
    padding-right: 0
  }

  .header {
    padding: 0 8px;
  }

  .header button {
    margin: 0 3px
  }

  .ant-modal-cust-warp {
    height: 100%
  }

  .ant-modal-cust-warp .ant-modal-body {
    height: calc(100% - 110px) !important;
    overflow-y: auto
  }

  .ant-modal-cust-warp .ant-modal-content {
    height: 90% !important;
    overflow-y: hidden
  }

  #app .desktop {
    height: auto !important;
  }

  /** Button按钮间距 */
  .ant-btn {
    margin-left: 3px
  }

  .drawer-bootom-button {
    /*position: absolute;*/
    bottom: 0;
    width: 100%;
    border-top: 1px solid #e8e8e8;
    padding: 10px 16px;
    text-align: left;
    left: 0;
    background: #fff;
    border-radius: 0 0 2px 2px;
  }
</style>