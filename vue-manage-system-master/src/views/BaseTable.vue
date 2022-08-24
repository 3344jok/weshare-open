<template>
    <div style="padding: 3%;">
        <div class="crumbs" >
            <el-breadcrumb separator="/">
                <el-breadcrumb-item>
                    <i class="el-icon-lx-cascades"></i> 我的相册集
                </el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container" style="padding: 3%;">
            
            <div class="handle-box">
                <el-radio-group v-model="radio" @change="clickToChoose()">
                    <el-radio  label="1" >只看我的</el-radio>
                    <el-radio  label="2" >查看所有</el-radio>
                </el-radio-group>
                <el-input v-model="query.pattern" placeholder="相册集名称" class="handle-input mr10"></el-input>
                <el-button type="primary" icon="el-icon-search" @click="clickGetData">搜索</el-button>
                <el-button type="text" icon="el-icon-lx-add" style="float: right;" @click="handleAdd()">创建</el-button>
                <el-button type="text" icon="el-icon-lx-friendadd" style="float: right;" @click="handleJoin()">加入</el-button>
                
            </div>
            <el-table :data="tableData" border class="table" ref="multipleTable" header-cell-class-name="table-header">
                <!-- <el-table-column prop="id"  label="ID" width="55" align="center"></el-table-column> -->
                <el-table-column prop="groupName" label="相册集名称"></el-table-column>
                <!-- <el-table-column label="创建者">
                    <template #default="scope">￥{{ scope.row.money }}</template>
                </el-table-column> -->
                <el-table-column label="封面" align="center">
                    <template #default="scope">
                        <el-image class="table-td-thumb" :src="'data:'+scope.row.ctype+';base64,'+scope.row.cover" :preview-src-list="[scope.row.thumb]">
                        </el-image>
                    </template>
                </el-table-column>
                <el-table-column prop="groupUuid" label="共享ID"></el-table-column>
                <el-table-column prop="invitationCode" label="邀请码"></el-table-column>

                <el-table-column prop="date" label="创建时间"></el-table-column>
                <el-table-column label="操作" width="180" align="center">
                    <template #default="scope">
                        <el-button type="text" icon="el-icon-lx-link" @click="handleEdit(scope.$index, scope.row)">查看
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
                <el-form-item label="相册集名">
                    <el-input v-model="form.groupName"></el-input>
                </el-form-item>
                <el-form-item label="邀请码">
                    <el-input v-model="form.invitationCode"></el-input>
                </el-form-item>
                <el-form-item label="备注">
                    <el-input v-model="form.remark" type="text"></el-input>
                </el-form-item>
                <el-form-item label="封面">
                    <input id="coverUpload" @change="uploadFileCover($event)" type="file"> 
                </el-form-item>
            </el-form>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="editVisible = false">取 消</el-button>
                    <el-button type="primary" @click="saveAdd">确 定</el-button>
                </span>
            </template>
        </el-dialog>

        <!-- 编辑弹出框 -->
        <el-dialog title="加入" v-model="joinVisible" width="30%">
            <el-form label-width="70px">
                <el-form-item label="共享ID">
                    <el-input v-model="form.targetUuid"></el-input>
                </el-form-item>
                <el-form-item label="邀请码">
                    <el-input v-model="form.targetInvitationCode"></el-input>
                </el-form-item>
            </el-form>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="joinVisible = false">取 消</el-button>
                    <el-button type="primary" @click="saveJoin">确 定</el-button>
                </span>
            </template>
        </el-dialog>
    </div>
</template>

<script>
import { ref, reactive } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { fetchData } from "../api/index";
import { onMounted, getCurrentInstance } from 'vue';
import axios from 'axios';
import qs from 'qs'  ;
import router from "../router";

export default {
    name: "basetable",
    data () {
      return {
        userId :sessionStorage.getItem("id"),
        radio: "2",
      };
    },
    setup() {
        const formatDate=(date)=>{
            let Y = date.getFullYear() + '-';
            let M = (date.getMonth()+1 < 10 ? (date.getMonth()+1) : date.getMonth()+1) + '-';
            let D = date.getDate() + ' ';
            let h = date.getHours() + ':';
            let m = date.getMinutes() + ':';
            let s = date.getSeconds(); 
            return Y+M+D; 
        }
        let all=true;
        let tableData = ref([]);
        let pageTotal = ref(0);
        let query= reactive({
                all: true,
                id: sessionStorage.getItem("id"),
                pattern:"",
                pageIndex: 1,
                pageSize: 10,
            });
        let formData=new FormData();
        let form = reactive({
            userId: sessionStorage.getItem("id"),
            groupName:"",
            cover:"",
            invitationCode:"",
            ctype:"",
            targetUuid:"",
            targetInvitationCode:"",
            remark:"",
        });
        // 获取表格数据
        const getData = () => {
            query.id=sessionStorage.getItem("id");
            fetchData(query).then((res) => {
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
                axios.delete('/api/group' ,{
                    params : { //请求参数
                        groupId : row.id
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

        // 表格编辑时弹窗和保存
        const editVisible = ref(false);
        const joinVisible = ref(false);
        
        let idx = -1;
        const handleAdd = () => {
            editVisible.value = true;
        };
        const handleJoin = () => {
            joinVisible.value = true;
        };
        const saveAdd = () => {
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
            formData.append("userId",sessionStorage.getItem("id"));
            formData.append("groupName",form.groupName);
            formData.append("invitationCode",form.invitationCode);
            formData.append("remark",form.remark);
            axios.post('/api/group/add' , formData,{
                headers: {
                    "Content-Type": "application/json"
                },
            })
                    .then(response => {
                    if (response.data.statusCode == 1000 || response.data.statusCode == 1001) {
                        ElMessage.success("添加成功");
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
            editVisible.value = false;
            clickGetData();
            // form.groupName='';
            // form.cover='';
            // form.invitationCode='';
            // form.ctype='';
            formData=null;
            formData=new FormData();
        };
        const saveJoin = () => {
            formData.append("userId",form.userId);
            formData.append("targetUuid",form.targetUuid);
            formData.append("targetInvitationCode",form.targetInvitationCode);
            axios.post('/api/group/join' , formData,{
                headers: {
                    "Content-Type": "application/json"
                },
            })
                    .then(response => {
                    if (response.data.statusCode == 1000 || response.data.statusCode == 1001) {
                        ElMessage.success("添加成功");
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
            clickGetData();
            joinVisible.value = false;
            form.targetUuid='';
            form.targetInvitationCode='';
            formData=null;
            formData=new FormData();
        };
        // const router = useRouter();
        //查看
        const handleEdit = (index, row) => {
            // axios.get('/api/group/getgroupdetails' ,{
            //     params : { //请求参数
            //         groupId : row.id
            //     }
            // })
            //     .then(response => {
            //     if (response.data.statusCode == 1000 || response.data.statusCode == 1001) {
            //         sessionStorage.setItem("groupName",response.data.data.groupFormData);
            //         sessionStorage.setItem("cover",response.data.data.cover);
            //         sessionStorage.setItem("invitationCode",response.data.data.invitationCode);
            //         sessionStorage.setItem("remark",response.data.data.remark);
            //         sessionStorage.setItem("ctype",response.data.data.ctype);
            //         // groupName=sessionStorage.getItem("groupName");
            //         // cover=sessionStorage.getItem("cover");
            //         // invitationCode=sessionStorage.getItem("invitationCode");
            //         // remark=sessionStorage.getItem("remark");
            //         // ctype=sessionStorage.getItem("ctype");
            //     } else {
            //         ElMessage.error(response.data.statusMsg);
            //         return false;
            //     }
            // })
            //     .catch(function (error) {
            //     ElMessage.error("getgeoupdetail错误");
            //     console.log(error);
            //     return false;
            // });
            axios.get('/api/group/getCreatorName' ,{
                    params : { //请求参数
                        creatorId : row.creatorId
                    }
                })
                    .then(response => {
                    if (response.data.statusCode == 1000 || response.data.statusCode == 1001) {
                        sessionStorage.setItem("creatorName",response.data.data);
                        // console.log(response.data.data);
                        return response.data.data;
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
            idx = index;
            sessionStorage.setItem("groupId",row.id);
            sessionStorage.setItem("groupName",row.groupName);
            sessionStorage.setItem("creatorId",row.creatorId);
            sessionStorage.setItem("cover",row.cover);
            sessionStorage.setItem("invitationCode",row.invitationCode);
            sessionStorage.setItem("date",row.date);
            sessionStorage.setItem("remark",row.remark);
            sessionStorage.setItem("groupUuid",row.groupUuid);
            sessionStorage.setItem("ctype",row.ctype);
            
            router.push("/group");
        };
        const onReset = () => {
            formRef.value.resetFields();
        };
        return {
            query,
            tableData,
            pageTotal,
            editVisible,
            joinVisible,
            form,
            formData,
            handleSearch,
            handlePageChange,
            handleDelete,
            handleEdit,
            handleAdd,
            handleJoin,
            saveAdd,
            saveJoin,
            getData,
            clickGetData,
            clickToChoose,
            onReset,
            formatDate,
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
                    that.formData.append("cover",el.target.files[0]);
                    that.formData.append("ctype",el.target.files[0].type);
                };
                
            }
        },
    },
};
</script>

<style scoped>
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
.table {
    width: 100%;
    font-size: 14px;
}
.red {
    color: #ff0000;
}
.mr10 {
    margin-right: 10px;
}
.table-td-thumb {
    display: block;
    margin: auto;
    width: 40px;
    height: 40px;
}
</style>
