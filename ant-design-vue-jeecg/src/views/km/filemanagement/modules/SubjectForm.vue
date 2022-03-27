<template>
  <a-tree
    checkStrictly
    checkable
    v-model="treeTopicCodes"
    :tree-data="treeData"
    :selectable="boolSelect"
    @check="onCheck"
  />
</template>

<script>

  import {getAction} from '@/api/manage'
  import Vue from "vue";
  import PropTypes from "ant-design-vue/lib/_util/vue-types";

  export default {
    name: 'SubjectForm',
    components: {},
    props: {
      //表单禁用
      disabled: {
        type: Boolean,
        default: false,
        required: false
      }
    },
    data() {
      return {
        // a-tree 属性
        boolSelect:false,
        checkedTitle:[],
        topicIds: '',
        treeTopicCodes:{
          checked:[],
          halfChecked:[],
        },
        topicIdsArr: [],
        topicCodesTree:[],
        checkedArray:[],
        treeData: [],
        selectedKeys: [],
        model: {},
        type: 0,
        labelCol: {
          xs: {span: 24},
          sm: {span: 5},
        },
        wrapperCol: {
          xs: {span: 24},
          sm: {span: 16},
        },
        confirmLoading: false,
        validatorRules: {},
        replaceFields: {
          key: 'id',
          parentId:"pid",
          title:"name",
        },
        url: {
          rootList: "/sys/category/loadTreeRoot",
          list: "/sys/category/rootListNoPage",
          childList: "/sys/category/childList",
          getChildListBatch: "/sys/category/getChildListBatch",
          editRelease: '/KM/kmDoc/editRelease',
          editDraft: '/KM/kmDoc/editDraft'
        },
        // 表头
        columns: [
          {
            title: '专题名称',
            align: "left",
            dataIndex: 'name'
          },
        ],
      }
    },
    computed: {
      formDisabled() {
        return this.disabled
      },
    },
    created() {
        this.loadTree();
    },
    methods: {



      // 查找一个节点的所有父节点
      familyTree(treeData,id){
        var arrTree=[];
        var forFn = function (arr, key) {
          for (var i = 0; i < arr.length; i++) {
            var item = arr[i]
            if (item.key === key) {
               if(item.parentId==="0"){
                 break;
               }else{
                 console.log("父节点",item.parentId);
                 arrTree.push(item.parentId);
                 forFn(treeData, item.parentId);
               }
              break
            } else {
              if (item.children!=null) {
                forFn(item.children, key);
              }
            }
          }
        }
        forFn(treeData,id);
        return arrTree
      },


      // 加载树节点
      loadTree() {
        let params = {
          async: false,
          pcode: ""
        };
        getAction(this.url.rootList, params).then(res => {
          if (res.success) {
            if (res.result && res.result.length > 0) {
              this.treeData = res.result
              this.treeTopicCodes.checked=this.topicCodesTree;
              let temp=[];
              let tempArray=[];
              for(let i=0;i<this.topicCodesTree.length;i++){
                let arrTemp=  this.familyTree(this.treeData,this.topicCodesTree[i]);
                temp=temp.concat(arrTemp);
              }
              // 数组去重
              tempArray=[...new Set(temp)];
              this.treeTopicCodes.halfChecked=tempArray;
            } else {
              this.treeData = []
            }
          } else {
            this.$message.warning(res.message)
          }
        }).finally(() => {
        })
      },

      getDataByResult(result) {
        if (result && result.length > 0) {
          return result.map(item => {
            //判断是否标记了带有子节点
            if (item.hasChild === '1') {
              item["isLeaf"]=false;
            }else{
              item["isLeaf"]=true;
            }
            return item
          })
        }
      },


      onLoadData(treeNode){
        return new Promise(resolve => {
          let params ={};
          params["pid"] = treeNode.dataRef.id
          console.log("treeNode", treeNode.dataRef.id);
          getAction(this.url.childList, params).then((res) => {
            if (res.success) {
              if (res.result && res.result.length > 0) {
                treeNode.dataRef.children = this.getDataByResult(res.result)
                this.treeData = [...this.treeData];
                resolve();
              }
            } else {
              this.$message.warning(res.message)
            }
          })

        })
      },

      // 树节点选择触发
      onCheck(checkedKeys, checkedNodes) {
         let temp=new Array();
        let tempArray=new Array();
        this.checkedArray =checkedKeys.checked;
        for(let i=0;i<this.checkedArray.length;i++){
          let arrTemp=  this.familyTree(this.treeData,this.checkedArray[i]);
          temp=temp.concat(arrTemp);
        }
        // 数组去重
        tempArray=[...new Set(temp)];
        console.log("tempArray",tempArray);
        checkedKeys.halfChecked=tempArray;
        this.checkedTitle=checkedNodes.checkedNodes;
      },


      edit(model, type) {
        this.type = type;
        this.model = Object.assign({}, model);
        this.model = model;
        this.visible = true;
        if (model.topicIds != null) {
          this.topicCodesTree = model.topicIds.split(',');

          this.topicIds = model.topicIds;
          this.topicIdsArr = model.topicIds.split(',');
        } else {
          this.topicCodesTree = [];
        }
       console.log(this.checkedKeys);
      },
      // 点击确定触发事件
      submitForm() {

        if (this.checkedArray.length > 0) {
          this.model.topicIds = this.checkedArray.toString();
        } else {
          this.model.topicIds = '',
            this.model.topicIds_dictText = ''
        }

        if (this.topicIds === this.checkedArray.toString()) {
          console.log("不操作")
        } else if (this.topicIds === '') {
          this.model.addTopicIdList = this.checkedArray;
        } else if (this.checkedArray.length === 0) {

          this.model.removeTopicIdList = this.topicIds.split(',');
        } else {
          var topicIdsArr = this.topicIdsArr;
          var topicCodesTreeArr = this.checkedArray;
          this.model.addTopicIdList = [];
          this.model.removeTopicIdList = [];
          for (let i = 0; i < this.topicIdsArr.length; i++) {
            for (let j = 0; j < this.checkedArray.length; j++) {
              if (topicIdsArr[i] === topicCodesTreeArr[j]) {
                delete topicIdsArr[i];
                delete topicCodesTreeArr[j];
              }
            }
          }
          for (let i = 0; i < topicIdsArr.length; i++) {
            if (typeof (topicIdsArr[i]) != "undefined") {
              this.model.removeTopicIdList.push(topicIdsArr[i]);
            }
          }
          for (let i = 0; i < topicCodesTreeArr.length; i++) {
            if (typeof (topicCodesTreeArr[i]) != "undefined") {
              this.model.addTopicIdList.push(topicCodesTreeArr[i]);
            }
          }
        }
        this.model.topicIds_dictText="";
        for(let i=0;i<this.checkedTitle.length;i++){
          if(this.model.topicIds_dictText===""){
            this.model.topicIds_dictText=this.checkedTitle[i].data.props.title;
          }else{
            this.model.topicIds_dictText= this.model.topicIds_dictText+","+this.checkedTitle[i].data.props.title;
          }
        }
        this.$emit('ok');
      },

    }
  }
</script>

<style scoped>
  @import '~@assets/less/common.less';
</style>