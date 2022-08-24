<template>
    <div  id="bg" class="login-wrap" >
        <div style="padding-left: 2%; padding-top:2%">
            <el-button type="primary"  icon="el-icon-lx-attention" @click="changeStatus1()">评论该相片</el-button>
            <el-button type="text" icon="el-icon-delete" style="color: red;" v-if="photoCreatorId==userId"
                            @click="handleDelete()">删除</el-button>
            <div>
                <img
                style="width: 96%;padding: 1%;" 
                :src="imgUrl">
            </div>
            
        </div>
        <div>
            <div style="padding: 2% ; padding-top:1%;opacity: 0.8; " >
                <el-container class="background">
                    <el-header 
                    height="20px"
                    style="text-align: left; font-size: 20px">
                    <span>评论区</span>
                    </el-header>
                    
                    <el-main>
                        <el-row type="flex" class="row-bg">
                            <el-col  v-for="comment in comments" v-bind:key="comment.id" style="border: 2px;" :span="24"><div class="grid-content bg-purple">
                                <div v-if="comment.receiverId==null"> 
                                    {{comment.senderName}}：{{comment.content}} 
                                    
                                    <el-button type="text" icon="el-icon-delete" style="color: red;float: right;" v-if="comment.senderId==userId" @click="commentDelete(comment.id)">删除</el-button>
                                    <el-button type="text" style="float: right;"  icon="el-icon-lx-comment" @click="changeStatus2(comment.senderName,comment.senderId)">回复</el-button>
                                </div > 
                                <div v-else> 
                                    {{comment.senderName}} 回复 {{comment.recevierName}}：{{comment.content}} 
                                
                                    <el-button type="text" icon="el-icon-delete" style="color: red;float: right;" v-if="comment.senderId==userId" @click="commentDelete(comment.id)">删除</el-button>
                                    <el-button type="text" style="float: right;"  icon="el-icon-lx-comment" @click="changeStatus2(comment.senderName,comment.senderId)">回复</el-button>
                                </div>
                                
                            </div> </el-col>
                            
                        </el-row>
                        <div title="评论"   v-if="operation1">
                        <el-divider content-position="left" style="color: #ffffff;">评论图片</el-divider>
                            <el-input
                                type="textarea"
                                :rows="2"
                                placeholder="评论该相片"
                                autosize="true"
                                v-model="textarea1">
                            </el-input>
                            <el-button type="primary" class="commit_button" style="float: right;" icon="el-icon-lx-top" @click="commit(1)">提交评论</el-button>
                        </div>
                        <div title="回复"   v-if="operation2">
                        <el-divider content-position="left">回复{{toAnswer}}</el-divider>
                            <el-input id="a"
                                type="textarea"
                                :rows="2"
                                placeholder="回复好友"
                                autosize="true"
                                v-model="textarea2">
                            </el-input>
                            <el-button type="primary" class="commit_button" style="float: right;" icon="el-icon-lx-top" @click="commit(2)">提交评论</el-button>
                        </div>
                    </el-main>
                </el-container>

            </div>
        </div>
        

    </div>
</template>

<script>

import { ref, reactive } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { fetchData } from "../api/index";
import router from "../router";
import axios from 'axios';
import { getCurrentInstance } from "vue";
import { useTagsStore } from '../store/tags'
import { onBeforeRouteUpdate, useRoute, useRouter } from "vue-router";

export default{
    name: '#photo',
    data () {
      return {
        note: {
          backgroundImage: "url(" + sessionStorage.getItem("filePath") + ") ",
          width:1
        },
        imgUrl:sessionStorage.getItem("filePath"),
        userId:sessionStorage.getItem("id"),
        photoCreatorId:sessionStorage.getItem("photoCreatorId"),
        operation1 : false,
        operation2 : false,
        textarea1: '',
        textarea2: '',
        userId :sessionStorage.getItem("id"),
        receiverId:0,
        toAnswer:'',
      };
    },
    setup (){
        const tags = useTagsStore();
        const me = getCurrentInstance();
        const route = useRoute();
        const router = useRouter();
        let comments = ref([]);
        const getComments=()=>{
            axios.get('/api/comment/getcomments' ,{
                params : { //请求参数
                    photoId : sessionStorage.getItem("photoId")
                }
            })
                .then(response => {
                if (response.data.statusCode == 1000 || response.data.statusCode == 1001) {
                    comments.value=response.data.data; 
                } else {
                    ElMessage.error(response.data.statusMsg);
                    return false;
                }
            })
                .catch(function (error) {
                ElMessage.error("未知错误");
                console.log(error);
                return false;
            });
        }
        getComments();
        const handleDelete=()=>{
            ElMessageBox.confirm("确定要删除吗？", "提示", {
                type: "warning",
            })
            .then(() => {
                axios.delete('/api/photo' ,{
                    params : { //请求参数
                        photoId : sessionStorage.getItem("photoId"),
                    }
                })
                    .then(response => {
                    if (response.data.statusCode == 1000 || response.data.statusCode == 1001) {
                        ElMessage.success("删除成功");
                        // 关闭当前页面的标签页
                        tags.closeCurrentTag({
                            $router: router,
                            $route: route
                        });
                        router.push("/album");
                    } else {
                        ElMessage.error(response.data.statusMsg);
                        return false;
                    }
                })
                    .catch(function (error) {
                    ElMessage.error("未知错误");
                    console.log(error);
                    return false;
                });
                
            })
            .catch(() => {});
            
        }

        const commit=(a)=>{
            let params={
                senderId:sessionStorage.getItem("id"),
                receiverId:-1,
                photoId:sessionStorage.getItem("photoId"),
                content:'',
            }
            if(a==1){
                params.content=me.data.textarea1;
                me.data.operation1 = !me.data.operation1;
            }
            else{
                params.receiverId=me.data.receiverId;
                params.content=me.data.textarea2;
                me.data.operation2 = !me.data.operation2;
            }
            axios.get('/api/comment/add' ,{
                params
            })
                .then(response => {
                if (response.data.statusCode == 1000 || response.data.statusCode == 1001) {
                    ElMessage.success(response.data.statusMsg);
                    getComments();
                } else {
                    ElMessage.error(response.data.statusMsg);
                    return false;
                }
            })
                .catch(function (error) {
                ElMessage.error("未知错误");
                console.log(error);
                return false;
            });
            
        }
        return {
            comments,
            commit,
            getComments,
            handleDelete,
        }
    },
    methods: {
        commentDelete(commentId){
            ElMessageBox.confirm("确定要删除吗？", "提示", {
                type: "warning",
            })
            .then(() => {
                axios.delete('/api/comment' ,{
                    params : { //请求参数
                        commentId : commentId,
                    }
                })
                    .then(response => {
                    if (response.data.statusCode == 1000 || response.data.statusCode == 1001) {
                        ElMessage.success("删除成功");
                        const that=this;
                        that.getComments();
                    } else {
                        ElMessage.error(response.data.statusMsg);
                        return false;
                    }
                })
                    .catch(function (error) {
                    ElMessage.error("未知错误");
                    console.log(error);
                    return false;
                });
                
            })
            .catch(() => {});
            
        },
        changeStatus1(){
            this.operation1 = !this.operation1;
            this.operation2 = false;
            this.textarea1='';
            this.textarea2='';
        },
        changeStatus2(name,id){
            this.operation2 = !this.operation2;
            this.operation1 = false;
            this.toAnswer=name;
            this.textarea1='';
            this.textarea2='';
            this.receiverId=id;
        }
    }
};
</script>

<style scoped>
.commit_button{
    position: relative;
    top:10px;
}
.login-wrap {
    position: relative;
    /* width: 100%;
    height: 100%;
    background-size: 100%; */
}
.el-row {
    margin-bottom: 20px;
}

/* .grid-content {
    display: flex;
    align-items: center;
    height: 100px;
} */

.grid-cont-right {
    flex: 1;
    text-align: center;
    font-size: 14px;
    color: #999;
}

.grid-num {
    font-size: 30px;
    font-weight: bold;
}

.grid-con-icon {
    font-size: 50px;
    width: 100px;
    height: 100px;
    text-align: center;
    line-height: 100px;
    color: #fff;
}

.grid-con-1 .grid-con-icon {
    background: rgb(45, 140, 240);
}

.grid-con-1 .grid-num {
    color: rgb(45, 140, 240);
}

.grid-con-2 .grid-con-icon {
    background: rgb(100, 213, 114);
}

.grid-con-2 .grid-num {
    color: rgb(45, 140, 240);
}

.grid-con-3 .grid-con-icon {
    background: rgb(242, 94, 67);
}

.grid-con-3 .grid-num {
    color: rgb(242, 94, 67);
}

.user-info {
    display: flex;
    align-items: center;
    padding-bottom: 20px;
    border-bottom: 2px solid #ccc;
    margin-bottom: 20px;
}

.user-avator {
    width: 120px;
    height: 120px;
    border-radius: 50%;
}

.user-info-cont {
    padding-left: 50px;
    flex: 1;
    font-size: 14px;
    color: #999;
}

.user-info-cont div:first-child {
    font-size: 30px;
    color: #222;
}

.user-info-list {
    font-size: 14px;
    color: #999;
    line-height: 25px;
}

.user-info-list span {
    margin-left: 70px;
}

.background{
    padding: 5%;
    background-color: aliceblue;
    border-radius: 15px;
}

.mgb20 {
    margin-bottom: 20px;
}

.todo-item {
    font-size: 14px;
}

.todo-item-del {
    text-decoration: line-through;
    color: #999;
}

.schart {
    width: 100%;
    height: 300px;
}

  /* .el-col {
    border-radius: 4px;
  } */
  .bg-purple-dark {
    background: #99a9bf;
  }
  .bg-purple {
    background: #d3dce6;
  }
  .bg-purple-light {
    background: #e5e9f2;
  }
  .grid-content {
    padding: 2%;
    border-radius: 15px;
    min-height: 30px;
    border-style:outset;
    border-width:5px;
  }
  /* .row-bg {
    padding: 10px 0;
    background-color: #f9fafc;
  } */
</style>
