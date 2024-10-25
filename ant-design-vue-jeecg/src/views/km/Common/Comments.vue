<template>
  <div>
    <a-comment>
      <a-avatar
        slot="avatar"
        :src="getAvatarView(userInfo.avatar)"
        alt="Han Solo"
      />
      <div slot="content">
        <a-form-item style="width:85%;float: left">
          <a-textarea :rows="2" :value="value" @change="handleChange" />
        </a-form-item>
        <a-form-item style="float: right">
          <a-button html-type="submit" :loading="submitting" type="primary" @click="handleSubmit">
            评论
          </a-button>
        </a-form-item>
      </div>
    </a-comment>

    <a-list item-layout="vertical" size="small" :split="false" :bordered="false" :loading="loading" :pagination="pagination" :data-source="comments">

        <a-list-item slot="renderItem" key="item.id" slot-scope="item, index" class="ky-list-comment">
          <a-comment :author="item.createBy" :avatar="getAvatarView(item.avatar)">
            <p slot="content">
              {{ item.comment }}
            </p>
            <a-tooltip slot="datetime" :title="item.createTime">
              <span>{{ moment(item.createTime).fromNow() }}</span>
            </a-tooltip>
          </a-comment>
        </a-list-item>
      <a-spin v-if="loading" class="demo-loading" />
    </a-list>

    </div>
</template>
<script>
import moment from 'moment';
import { getFileAccessHttpUrl, httpAction, getAction } from '@/api/manage'

const getCommentsUrl = '/KM/KmDocComments/list';
const commitCommentsUrl = '/KM/KmDocComments/add';

export default {
  name: 'DocComments',
  props: {
    docId: ''
  },
  created() {
    this.init()
  },
  data() {
    return {
      comments: [],
      userInfo:{},
      pagination: {
        onChange: page => {
          console.log(page)
          this.pagination.current = page
          this.fetchData()
        },
        hideOnSinglePage: true,
        pageSize: 10,
        pageSizeOptions: ['10', '20', '30'],
        showTotal: (total, range) => {
          return range[0] + "-" + range[1] + " 共" + total + "条"
        },
        showQuickJumper: true,
        showSizeChanger: true,
      },
      submitting: false,
      value: '',
      loading: false,
      moment,
    };
  },
  watch:{
    docId(newVal,oldVal){
      console.log("id changed:",newVal)
      this.init()
    }
  },
  methods: {
    init(){
      this.userInfo = this.$store.getters.userInfo
      this.pagination.current =1
      this.fetchData()
    },
    getAvatarView: function (avatar) {
      return getFileAccessHttpUrl(avatar)
    },
    fetchData() {
      let params = {
        docId: this.docId,
        pageNo: this.pagination.current,
        pageSize: this.pagination.pageSize
      }
      getAction(getCommentsUrl, params).then((res) => {
        console.log("comments res:",res)
        if(res.success) {
          this.comments = res.result.records
          this.loading = false;
          if(res.result.total)
          {
            this.pagination.total = res.result.total;
          }else{
            this.pagination.total = 0;
          }
        }else{
          this.$message.warning("获取评论失败!" );
        }
      }).finally(() => {
      })
    },

    handleSubmit() {
      if (!this.value) {
        return;
      }
      this.submitting = true;

      let params = {
        docId: this.docId,
        comment: this.value
      }
      let passParam = Object.assign({}, params);
      // console.log("pass param",passParam)
      let method = 'post';
      httpAction(commitCommentsUrl,  passParam ,method).then((res) => {
        console.log("comments res:",res)
        if(res.success){
          this.$message.success("评论成功!");
          this.fetchData();
          this.submitting = false;
          this.value = ''
          console.log("emit new  ....res:",res)
          this.$emit('new','')
        }else {
          this.$message.warning("评论失败!" );
        }
      }).finally(() => {
      })
    },
    handleChange(e) {
      this.value = e.target.value;
    },
  },
};
</script>
<style>
.demo-loading {
  position: absolute;
  bottom: 40px;
  width: 100%;
  text-align: center;
}
.ky-list-comment{
  padding-top: 0px;
  padding-bottom: 0px;
}
.ant-comment-inner{
  padding-top: 0px;
  padding-bottom: 0px;
  margin-top: 0px;
  margin-bottom: 0px;
}
.ant-comment-content-detail{
  padding-top: 0px;
  padding-bottom: 0px;
  margin-top: 0px;
  margin-bottom: 0px;
}
.ant-comment-content{
  padding-top: 0px;
  padding-bottom: 0px;
  margin-top: 0px;
  margin-bottom: 0px;
}
.ant-comment{
  padding-top: 0px;
  padding-bottom: 0px;
  margin-top: 0px;
  margin-bottom: 0px;
}
</style>