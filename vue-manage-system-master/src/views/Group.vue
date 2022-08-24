<template>
    <div>
    <el-dialog title="修改"  v-model="editGroupVisible"  width="30%" style="opacity:1;">
        <el-form label-width="70px" ref="formRef" :model="form">
        <el-form-item label="tips">
                        修改前请先刷新页面
                    </el-form-item>
            <el-form-item label="相册名">
                <el-input v-model="form.groupName"></el-input>
            </el-form-item>
            <el-form-item label="邀请码">
                <el-input v-model="form.invitationCode"></el-input>
            </el-form-item>
            <el-form-item label="备注">
                <el-input v-model="form.remark" type="text"></el-input>
            </el-form-item>
            <el-form-item label="封面">
                <input v-on="form.photo" @change="uploadFileCover($event)" type="file"> 
            </el-form-item>
        </el-form>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="onReset();">取 消</el-button>
                <el-button type="primary" @click="saveGroupEdit()">确 定</el-button>
            </span>
        </template>
    </el-dialog>
    <!-- :style="{backgroundImage: 'url(data:'+ctype+';base64,'+cover+')'}" -->
    <div class="login-wrap" >
        <div style="padding: 3% ;opacity: 0.9; " >
            <el-row :gutter="20">
                <el-col :span="8">
                    <el-card shadow="hover" class="mgb20" style="height:270px;">
                        <div class="user-info">
                            <img src="../assets/img/group.jfif" class="user-avator" alt />
                            <div class="user-info-cont">
                                <div class="user-info-name">{{ groupName }}</div>
                                <div>by</div>
                                <div> {{creatorName}}</div>
                                <div>when</div>
                                <div>{{ date }}</div>
                            </div>
                        </div>
                        <div class="user-info-list">
                            共享ID：{{groupUuid}}
                        </div>
                        <div class="user-info-list">
                            邀请码：
                            {{invitationCode}}
                        </div>
                    </el-card>
                    <el-card shadow="hover" style="height:234px;">
                        <template #header>
                            <div class="clearfix">
                                <i class="el-icon-lx-text"></i>
                                <span style="padding-left: 12px;">备注</span>
                            </div>
                        </template>
                        <p >{{remark}}</p>
                       
                    </el-card>
                </el-col>
                <el-col :span="16">
                    <!-- <el-row :gutter="20" class="mgb20">
                        <el-col :span="8">
                            <el-card shadow="hover" :body-style="{ padding: '0px' }">
                                <div class="grid-content grid-con-1">
                                    <i class="el-icon-user-solid grid-con-icon"></i>
                                    <div class="grid-cont-right">
                                        <div class="grid-num">1234</div>
                                        <div>用户访问量</div>
                                    </div>
                                </div>
                            </el-card>
                        </el-col>
                        <el-col :span="8">
                            <el-card shadow="hover" :body-style="{ padding: '0px' }">
                                <div class="grid-content grid-con-2">
                                    <i class="el-icon-message-solid grid-con-icon"></i>
                                    <div class="grid-cont-right">
                                        <div class="grid-num">321</div>
                                        <div>系统消息</div>
                                    </div>
                                </div>
                            </el-card>
                        </el-col>
                        <el-col :span="8">
                            <el-card shadow="hover" :body-style="{ padding: '0px' }">
                                <div class="grid-content grid-con-3">
                                    <i class="el-icon-s-goods grid-con-icon"></i>
                                    <div class="grid-cont-right">
                                        <div class="grid-num">5000</div>
                                        <div>数量</div>
                                    </div>
                                </div>
                            </el-card>
                        </el-col>
                    </el-row> -->
                    <el-card shadow="hover" style="height:525px;">
                        <template #header>
                            <div class="clearfix">
                                <i class="el-icon-lx-likefill" style="color:#dd553a" > </i>
                                <span style="padding-left: 12px;">封面</span>
                                <el-button type="text" icon="el-icon-lx-link" style="float: right;" v-if="creatorId==userId" @click="handleGroupEdit()">修改
                        </el-button>
                            </div>
                        </template>
                    <img :src="'data:'+ctype+';base64,'+cover" id="dbg" class="user-background" alt />
                    
                    </el-card>
                </el-col>
            </el-row>
        </div>
        <!-- 表  -->
        <div >
            <div class="crumbs" >
                <el-breadcrumb separator="/">
                    <el-breadcrumb-item>
                        <!-- <i class="el-icon-lx-cascades"></i> 基础表格 -->
                    </el-breadcrumb-item>
                </el-breadcrumb>
            </div>
            <div class="container" >
                
                <div class="handle-box">
                <el-radio-group v-model="radio" @change="clickToChoose()">
                    <el-radio  label="1" >只看我的</el-radio>
                    <el-radio  label="2" >查看所有</el-radio>
                </el-radio-group>
                <el-input v-model="query.pattern" placeholder="相册名称" class="handle-input mr10"></el-input>
                <el-button type="primary" icon="el-icon-search" @click="clickGetData">搜索</el-button>
                <el-button type="text" icon="el-icon-lx-add" style="float: right;" @click="handleEdit()">创建</el-button>
                
            </div>
                <el-table :data="tableData" border class="table" ref="multipleTable" header-cell-class-name="table-header">
                    <!-- <el-table-column prop="creatorId" label="ID" width="55" align="center"></el-table-column> -->
                    <el-table-column prop="albumName" label="相册名称"></el-table-column>
                    <el-table-column label="封面" align="center">
                        <template #default="scope">
                        <el-image class="table-td-thumb" :src="'data:'+scope.row.ctype+';base64,'+scope.row.cover" :preview-src-list="[scope.row.thumb]">
                        </el-image>
                    </template>
                    </el-table-column>

                    <el-table-column prop="date" label="创建时间"></el-table-column>
                    <el-table-column label="操作" width="180" align="center">
                        <template #default="scope">
                            <el-button type="text" icon="el-icon-lx-link" @click="handlePush(scope.$index, scope.row)">查看
                        </el-button>
                        <p></p>
                        <el-button type="text" icon="el-icon-delete" class="red" v-if="scope.row.creatorId==form.userId"
                            @click="handleDelete(scope.$index,  scope.row)">删除</el-button>
                        </template>
                    </el-table-column>
                </el-table>
                <div class="pagination">
                    <el-pagination background layout="total, prev, pager, next" :current-page="query.pageIndex"
                        :page-size="query.pageSize" :total="pageTotal" @current-change="handlePageChange"></el-pagination>
                </div>
            </div>

            <!-- 编辑弹出框 -->
            <el-dialog title="创建" v-model="editVisible" width="30%">
                <el-form label-width="70px">
                    <el-form-item label="tips">
                        创建前请先刷新页面
                    </el-form-item>
                    <el-form-item label="相册名">
                        <el-input v-model="albumForm.albumName"></el-input>
                    </el-form-item>
                    <el-form-item label="备注">
                        <el-input v-model="albumForm.remark"></el-input>
                    </el-form-item>
                    <el-form-item label="封面">
                        <input id="albumCover" v-on="albumForm.photo" @change="uploadFileAlbumCover($event)" type="file"> 
                    </el-form-item>
                </el-form>
                <template #footer>
                    <span class="dialog-footer">
                        <el-button @click="editVisible = false">取 消</el-button>
                        <el-button type="primary" @click="saveEdit">确 定</el-button>
                    </span>
                </template>
            </el-dialog>
        </div>
    </div>
    </div>
</template>

<script>
import { ref, reactive } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { fetchData ,fetchAlbumsData} from "../api/index";
import axios from 'axios';
import router from "../router";

export default {
    name: "basetable",
    data () {
      return {
        userId :sessionStorage.getItem("id"),
        creatorId:sessionStorage.getItem("creatorId"),
        radio: '2',
        creatorName:sessionStorage.getItem("creatorName"),
      };
    },
    setup() {
        const formRef = ref(null);
        const formatDate=(date)=>{
            let Y = date.getFullYear() + '-';
            let M = (date.getMonth()+1 < 10 ? (date.getMonth()+1) : date.getMonth()+1) + '-';
            let D = date.getDate() + ' ';
            let h = date.getHours() + ':';
            let m = date.getMinutes() + ':';
            let s = date.getSeconds(); 
            return Y+M+D; 
        }
        let groupId= sessionStorage.getItem("groupId");
        let creatorName=sessionStorage.getItem("creatorName");
        let groupName=sessionStorage.getItem("groupName");
        let creatorId=sessionStorage.getItem("creatorId");
        let cover=sessionStorage.getItem("cover");
        let invitationCode=sessionStorage.getItem("invitationCode");
        let date=formatDate(new Date(sessionStorage.getItem("date")));
        let remark=sessionStorage.getItem("remark");
        let groupUuid=sessionStorage.getItem("groupUuid");
        let ctype=sessionStorage.getItem("ctype");
        
        let all=true;
        let tableData = ref([]);
        let pageTotal = ref(0);
        let query= reactive({
                all: true,
                groupId: sessionStorage.getItem("groupId"),
                pattern:"",
                userId:sessionStorage.getItem("id"),
                pageIndex: 1,
                pageSize: 10,
            });
        // 获取表格数据
        const getData = () => {
            query.groupId=sessionStorage.getItem("groupId");
            fetchAlbumsData(query).then((res) => {
                tableData.value = res.data.list;
                pageTotal.value = res.data.pageTotal;
            });
        };
        getData();

        const clickGetData = () => {
            query.all=all;
            getData();
        };

        const clickToChoose = () => {
            all=!all;
            query.all=all;
            getData();
        };
        // 查询操作
        const handleSearch = () => {
            query.pageIndex = 1;
            getData();
        };
        // 分页导航
        const handlePageChange = (val) => {
            query.pageIndex = val;
            getData();
        };
        
        // 删除操作
        const handleDelete = (index,row) => {
            // 二次确认删除
            ElMessageBox.confirm("确定要删除吗？", "提示", {
                type: "warning",
            })
            .then(() => {
                axios.delete('/api/album' ,{
                    params : { //请求参数
                        albumId : row.id
                    }
                })
                    .then(response => {
                    if (response.data.statusCode == 1000 || response.data.statusCode == 1001) {
                        ElMessage.success("删除成功");
                        clickGetData();
                        tableData.value.splice(index, 1);
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
        };
        const editVisible = ref(false);
        // 表格编辑时弹窗和保存
        const editGroupVisible = ref(false);
        let form = reactive({
            userId: sessionStorage.getItem("id"),
            groupName:"",
            cover:"",
            invitationCode:"",
            ctype:"",
            targetUuid:"",
            targetInvitationCode:"",
            remark:"",
            photo:null,
        });
        let albumForm = reactive({
            albumName:"",
            cover:"",
            ctype:"",
            remark:"",
            photo:null,
        });
        let idx = -1;
        let albumFormData=new FormData();
        const handleEdit = (index, row) => {
            editVisible.value = true;
        };
        const saveEdit = () => {
            editVisible.value = false;
            //向后端发送创建新相册的请求
            
            if (albumForm.albumName.length < 1) {
                ElMessage.warning("相册名称不能为空");
                return;
            }if (albumForm.albumName.length >20) {
                ElMessage.warning("相册名称长度不能大于20");
                return;
            }
            albumFormData.append("userId",sessionStorage.getItem("id"));
            albumFormData.append("groupId",sessionStorage.getItem("groupId"));
            albumFormData.append("albumName",albumForm.albumName);
            albumFormData.append("albumRemark",albumForm.remark);
            axios.post('/api/album/add' , albumFormData,{
                headers: {
                    "Content-Type": "application/json"
                },
            })
                    .then(response => {
                    if (response.data.statusCode == 1000 || response.data.statusCode == 1001) {
                        ElMessage.success("添加成功");
                        getData();
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
            
            albumFormData=null;
            albumFormData=new FormData();
        };
        let groupFormData=new FormData();
        
        const handleGroupEdit = () => {
            form.userId=sessionStorage.getItem("id");
            form.groupName=sessionStorage.getItem("groupName");
            form.cover=sessionStorage.getItem("cover");
            form.invitationCode=sessionStorage.getItem("invitationCode");
            form.ctype=sessionStorage.getItem("ctype");
            form.targetUuid=sessionStorage.getItem("targetUuid");
            form.targetInvitationCode=sessionStorage.getItem("targetInvitationCode");
            form.remark=sessionStorage.getItem("remark");
            editGroupVisible.value = true;
        };
        const saveGroupEdit = () => {
            if (form.groupName.length < 1) {
                ElMessage.warning("相册集名称不能为空");
                return;
            }if (form.groupName.length >20) {
                ElMessage.warning("相册集名称长度不能大于20");
                return;
            }if (form.invitationCode.length < 3) {
                ElMessage.warning("邀请码长度至少3位");
                return;
            }
            if (form.invitationCode.length > 10) {
                ElMessage.warning("邀请码长度不能大于10");
                return;
            }
            groupFormData.append("groupId",sessionStorage.getItem("groupId"));
            groupFormData.append("groupName",form.groupName);
            groupFormData.append("invitationCode",form.invitationCode);
            groupFormData.append("remark",form.remark);
            axios.post('/api/group/update' , groupFormData,{
                headers: {
                    "Content-Type": "application/json"
                },
            })
                    .then(response => {
                    if (response.data.statusCode == 1000 || response.data.statusCode == 1001) {
                        ElMessage.success("添加成功");
                        sessionStorage.setItem("groupName",response.data.data.groupName);
                        sessionStorage.setItem("cover",response.data.data.cover);
                        sessionStorage.setItem("invitationCode",response.data.data.invitationCode);
                        sessionStorage.setItem("remark",response.data.data.remark);
                        sessionStorage.setItem("ctype",response.data.data.ctype);
                        groupName=sessionStorage.getItem("groupName");
                        cover=sessionStorage.getItem("cover");
                        invitationCode=sessionStorage.getItem("invitationCode");
                        remark=sessionStorage.getItem("remark");
                        ctype=sessionStorage.getItem("ctype");
                        getData();
                    } else {
                        ElMessage.error(response.data.statusMsg);
                        return false;
                    }
                    groupFormData=null;
                    groupFormData=new FormData();
                })
                    .catch(function (error) {
                    ElMessage.error("未知错误");
                    console.log(error);
                    groupFormData=null;
                    groupFormData=new FormData();
                    return false;
                });
            onReset();
            
        };
        const getGroupDetail=()=>{
            
        };
        getGroupDetail();
        const onReset = () => {
            editGroupVisible.value = false;
        };

        const handlePush = (index, row) => {
            sessionStorage.setItem("albumId",row.id);
            sessionStorage.setItem("albumName",row.albumName);
            sessionStorage.setItem("albumCreatorId",row.creatorId);
            sessionStorage.setItem("albumCover",row.cover);
            sessionStorage.setItem("albumDate",row.date);
            sessionStorage.setItem("albumRemark",row.remark);
            sessionStorage.setItem("albumCtype",row.ctype);
            
            router.push("/album");
        };
        return {
            creatorName,
            query,
            tableData,
            pageTotal,
            editVisible,
            editGroupVisible,
            form,
            cover,
            ctype,
            groupId,
            groupName,
            creatorId,
            cover,
            invitationCode,
            date,
            remark,
            groupUuid,
            groupFormData,
            formRef,
            albumForm,
            albumFormData,
            handleSearch,
            handlePageChange,
            handleDelete,
            handleEdit,
            saveEdit,
            handleGroupEdit,
            saveGroupEdit,
            onReset,
            getGroupDetail,
            clickGetData,
            clickToChoose,
            handlePush,
        };
    },
    methods: {  
      uploadFileCover (el) {
            if (!el.target.files[0].size) return; // 如果文件大小为0，则返回
            if (el.target.files[0].type.indexOf('image') === -1) { //如果不是图片格式
                // this.$dialog.toast({ mes: '请选择图片文件' });
                ElMessage.warning('请选择图片文件');
            } 
            else if(el.target.files[0].size>2097152){
                ElMessage.warning("图片不能大于2M！");
            }else {
                const that = this;
                const reader = new FileReader(); // 创建读取文件对象
                reader.readAsDataURL(el.target.files[0]); // 发起异步请求，读取文件
                reader.onload = function () {  // 文件读取完成后
                // 读取完成后，将结果赋值给img的src
                    that.groupFormData.append("cover",el.target.files[0]);
                    that.groupFormData.append("ctype",el.target.files[0].type);
                };
                
            }
        },
        uploadFileAlbumCover (el) {
            if (!el.target.files[0].size) return; // 如果文件大小为0，则返回
            if (el.target.files[0].type.indexOf('image') === -1) { //如果不是图片格式
                // this.$dialog.toast({ mes: '请选择图片文件' });
                ElMessage.warning('请选择图片文件');
            } 
            else if(el.target.files[0].size>2097152){
                ElMessage.warning("图片不能大于2M！");
            }else {
                const that = this;
                const reader = new FileReader(); // 创建读取文件对象
                reader.readAsDataURL(el.target.files[0]); // 发起异步请求，读取文件
                reader.onload = function () {  // 文件读取完成后
                // 读取完成后，将结果赋值给img的src
                    that.albumFormData.append("cover",el.target.files[0]);
                    that.albumFormData.append("ctype",el.target.files[0].type);
                };
                
            }
        },
    },
};
</script>


<style scoped>
.login-wrap {
    position: relative;
    width: 100%;
    height: 100%;
    /* background-image: url(../assets/img/login-bg.jpg); */
    background-size: 100%;
}
.el-row {
    margin-bottom: 20px;
}

.grid-content {
    display: flex;
    align-items: center;
    height: 100px;
}

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
    font-size: 10px;
    color: #999;
    line-height: 25px;
}

.user-info-list span {
    margin-left: 70px;
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
.user-background{
    margin: 0 auto;
    display: table;
    /* width: 100%;
    height: 100%; */
    object-fit:cover
}
.handle-box {
    margin-bottom: 20px;
}

.handle-select {
    width: 120px;
}

.handle-input {
    width: 300px;
    display: inline-block;
}

.red {
    color: #ff0000;
}
</style>
