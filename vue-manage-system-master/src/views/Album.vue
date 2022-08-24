<template >
    <div>
        <div class="login-wrap" >
        <div style="padding: 3% ;opacity: 0.9; " >
        <el-row :gutter="20">
            <el-col :span="8">
                <el-card shadow="hover" class="mgb20" style="height:252px;">
                    <div class="user-info">
                            <img src="../assets/img/group.jfif" class="user-avator" alt />
                            <div class="user-info-cont">
                                <div class="user-info-name">{{ albumName }}</div>
                                <div>when</div>
                                <div>{{ albumDate }}</div>
                            </div>
                        </div>
                    <!-- <div class="user-info-list">
                        上次登录时间：
                        <span>2019-11-01</span>
                    </div>
                    <div class="user-info-list">
                        上次登录地点：
                        <span>东莞</span>
                    </div> -->
                </el-card>
                <el-card shadow="hover" style="height:252px;">
                        <template #header>
                            <div class="clearfix">
                                <i class="el-icon-lx-text"></i>
                                <span style="padding-left: 12px;">备注</span>
                            </div>
                        </template>
                        <p >{{albumRemark}}</p>
                       
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
                                <el-button type="text" icon="el-icon-lx-link" style="float: right;" v-if="albumCreatorId==userId" @click="handleAlbumEdit()">修改
                        </el-button>
                            </div>
                        </template>
                    <img :src="'data:'+albumCtype+';base64,'+albumCover" id="dbg" class="user-background" alt />
                    
                    </el-card>
                    <!-- 编辑弹出框 -->
            <el-dialog title="修改" v-model="editAlbumVisible" width="30%">
                <el-form label-width="70px">
                    <el-form-item label="tips">
                        修改前请先刷新页面
                    </el-form-item>
                    <el-form-item label="相册名">
                        <el-input v-model="form.albumName"></el-input>
                    </el-form-item>
                    <el-form-item label="备注">
                        <el-input v-model="form.albumRemark"></el-input>
                    </el-form-item>
                    <el-form-item label="封面">
                        <input id="albumCover" v-on="form.photo" @change="uploadFileAlbumCover($event)" type="file"> 
                    </el-form-item>
                </el-form>
                <template #footer>
                    <span class="dialog-footer">
                        <el-button @click="editAlbumVisible = false">取 消</el-button>
                        <el-button type="primary" @click="saveAlbumEdit">确 定</el-button>
                    </span>
                </template>
            </el-dialog>
            </el-col>
        </el-row>
    </div>
    </div>
    <div>
    <el-divider content-position="left"></el-divider>
    <el-radio-group v-model="radio" @change="clickToChoose()" >
                    <el-radio  label="1" >只看我的</el-radio>
                    <el-radio  label="2" >查看所有</el-radio>
                </el-radio-group>
    <!-- <el-divider content-position="left">
        本次浏览添加
    </el-divider> -->
    <div style="padding: 3% ;" >
    <div>
    <el-row type="flex" class="row-bg" justify="space-around">
        <el-col :span="5" v-for="photo in photos" v-bind:key="photo.id"  :offset="index > 0 ? 0 : 0">
            <el-card :body-style="{ padding: '3px' }">
            <img :src=photo.filePath class="image">
            <div style="padding: 6px;">
                <div class="bottom clearfix" style="float: right;">
                <el-button type="text" icon="el-icon-lx-link" @click="handleDetail(photo)">查看
                        </el-button>
                        <p></p>
                        <el-button type="text" icon="el-icon-delete" style="color: red;" v-if="photo.creatorId==userId"
                            @click="handleDelete(photo.id)">删除</el-button>
                </div>
            </div>
            </el-card>
        </el-col>
        </el-row>
    </div>
        <el-upload
            action="/api/photo/upload"
            :show-file-list="false"
            :headers="myHeaders"
            :with-credentials="true"    
            list-type="picture-card"
            :on-success="handleSuccess"
            :on-remove="handleRemove">
            <i class="el-icon-plus"></i>
        </el-upload>
        <el-dialog :visible.sync="dialogVisible" >
            <img width="100%" :src="dialogImageUrl" alt="">
        </el-dialog>
    </div>
    </div>
    </div>
</template>

<script>
import { ref, reactive } from "vue";
import Schart from "vue-schart";
import axios from 'axios';
import { ElMessage, ElMessageBox } from "element-plus";
import router from "../router";

export default {
    name: "dashboard",
    components: { Schart },
    data() {
      return {
        userId :sessionStorage.getItem("id"),
        dialogImageUrl: '',
        radio: '2',
        dialogVisible: false,
        myHeaders: {'X-XSRF-TOKEN': this.getCookie('XSRF-TOKEN')}
      };
    },
    setup() {
        let photos = ref([]);
        let all=true;
        let albumFormData=new FormData();
        const editAlbumVisible = ref(false);
        let albumId=sessionStorage.getItem("albumId");
        let albumName=sessionStorage.getItem("albumName");
        let albumCreatorId=sessionStorage.getItem("albumCreatorId");
        let albumCover=sessionStorage.getItem("albumCover");
        let albumCtype=sessionStorage.getItem("albumCtype");
        let albumRemark=sessionStorage.getItem("albumRemark");
        let albumDate=sessionStorage.getItem("albumDate");
        const name = localStorage.getItem("ms_username");
        const role = name === "admin" ? "超级管理员" : "普通用户";

        
        
        
        let form = reactive({
            userId: sessionStorage.getItem("id"),
            albumName:"",
            albumCover:"",
            albumCtype:"",
            albumRemark:"",
        });
        const handleAlbumEdit = () => {
            form.userId=sessionStorage.getItem("id");
            form.albumName=sessionStorage.getItem("albumName");
            form.albumCover=sessionStorage.getItem("albumCover");
            form.albumCtype=sessionStorage.getItem("albumCtype");
            form.albumRemark=sessionStorage.getItem("albumRemark");
            editAlbumVisible.value = true;
        };
        const getAlbum = () => {
            albumFormData.append("albumId",sessionStorage.getItem("albumId"));
            axios.post('/api/album/update' , albumFormData,{
                headers: {
                    "Content-Type": "application/json"
                },
            })
                    .then(response => {
                    if (response.data.statusCode == 1000 || response.data.statusCode == 1001) {
                       
                        sessionStorage.setItem("albumName",response.data.data.albumName);
                        sessionStorage.setItem("albumCover",response.data.data.cover);
                        sessionStorage.setItem("albumRemark",response.data.data.remark);
                        sessionStorage.setItem("albumCtype",response.data.data.ctype);
                        albumName=sessionStorage.getItem("albumName");
                        albumCover=sessionStorage.getItem("albumCover");
                        albumCtype=sessionStorage.getItem("albumCtype");
                        albumRemark=sessionStorage.getItem("albumRemark");
                        // console.log(sessionStorage.getItem("albumName"));
                        
                    } else {
                        return false;
                    }
                })
                    .catch(function (error) {
                    return false;
                });
        };
        getAlbum();
        const getPhotos=()=>{
            axios.get('/api/photo/getphotos' ,{
                params : { //请求参数
                    userId : sessionStorage.getItem("id"),
                    albumId: sessionStorage.getItem("albumId"),
                    all:all
                }
            })
                .then(response => {
                if (response.data.statusCode == 1000 || response.data.statusCode == 1001) {
                    photos.value=response.data.data; 
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
        };
        getPhotos();
        const clickToChoose=()=>{
            all=!all;
            getPhotos();
        }
        const saveAlbumEdit = () => {
            if (form.albumName.length < 1) {
                ElMessage.warning("相册名称不能为空");
                return;
            }if (form.albumName.length >20) {
                ElMessage.warning("相册名称长度不能大于20");
                return;
            }
            albumFormData.delete("albumId");
            albumFormData.delete("albumName");
            albumFormData.delete("remark");
            albumFormData.append("albumId",sessionStorage.getItem("albumId"));
            albumFormData.append("albumName",form.albumName);
            albumFormData.append("remark",form.albumRemark);
            // console.log("cover");
            // console.log(albumFormData.get("cover"));
            axios.post('/api/album/update' , albumFormData,{
                headers: {
                    "Content-Type": "application/json"
                },
            })
                    .then(response => {
                    if (response.data.statusCode == 1000 || response.data.statusCode == 1001) {
                        ElMessage.success("添加成功");
                        sessionStorage.setItem("albumName",response.data.data.albumName);
                        sessionStorage.setItem("albumCover",response.data.data.cover);
                        sessionStorage.setItem("albumRemark",response.data.data.remark);
                        sessionStorage.setItem("albumCtype",response.data.data.ctype);
                        albumName=sessionStorage.getItem("albumName");
                        albumCover=sessionStorage.getItem("albumCover");
                        albumCtype=sessionStorage.getItem("albumCtype");
                        albumRemark=sessionStorage.getItem("albumRemark");
                        // console.log(sessionStorage.getItem("albumName"));
                        albumFormData.delete("albumId");
                        albumFormData.delete("albumName");
                        albumFormData.delete("remark");
                        
                    } else {
                        ElMessage.error(response.data.statusMsg);
                        return false;
                    }
                    albumFormData=null;
                    albumFormData=new FormData();
                })
                    .catch(function (error) {
                    ElMessage.error("未知错误");
                    console.log(error);
                    albumFormData=null;
                    albumFormData=new FormData();
                    return false;
                });
            editAlbumVisible.value =false;
        };
        const handleDetail=(photo)=>{
            sessionStorage.setItem("photoId",photo.id);
            sessionStorage.setItem("photoDate",photo.date);
            sessionStorage.setItem("photoCreatorId",photo.creatorId);
            sessionStorage.setItem("filePath",photo.filePath);
            router.push("/photo");
        }
        const handleDelete=(photoId)=>{
            ElMessageBox.confirm("确定要删除吗？", "提示", {
                type: "warning",
            })
            .then(() => {
                axios.delete('/api/photo' ,{
                    params : { //请求参数
                        photoId : photoId
                    }
                })
                    .then(response => {
                    if (response.data.statusCode == 1000 || response.data.statusCode == 1001) {
                        ElMessage.success("删除成功");
                        getPhotos();
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
        return {
            name,
            role,
            albumCover,
            albumCreatorId,
            albumCtype,
            albumDate,
            albumId,
            albumName,
            albumRemark,
            form,
            photos,
            editAlbumVisible,
            albumFormData,
            handleAlbumEdit,
            saveAlbumEdit,
            getPhotos,
            clickToChoose,
            handleDelete,
            handleDetail,
        };
    },
    methods: {
       
        getCookie(cookieName) {
            const strCookie = document.cookie
            const cookieList = strCookie.split(';')
            
            for(let i = 0; i < cookieList.length; i++) {
                const arr = cookieList[i].split('=')
                if (cookieName === arr[0]) {
                return arr[1]
                }
            }
            
            return ''
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
                    // console.log("上传");
                };
                
            }
        },
      handleRemove(file, fileList) {
        console.log();
      },
      handlePictureCardPreview(file) {
        this.dialogImageUrl = file.url;
        this.dialogVisible = true;
        console.log(file.url);
      },
      handleSuccess(response, file, fileList){
        console.log(file.url);
        var filePath=response.data;
        axios.get('/api/photo/add' ,{
                params : { //请求参数
                    creatorId : sessionStorage.getItem("id"),
                    albumId: sessionStorage.getItem("albumId"),
                    filePath: response.data
                }
            })
                .then(response => {
                if (response.data.statusCode == 1000 || response.data.statusCode == 1001) {
                    ElMessage.success(response.data.statusMsg);
                    const that=this;
                    that.getPhotos();
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
    }
  
};
</script>

<style scoped>
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
    font-size: 14px;
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

.login-wrap {
    position: relative;
    width: 100%;
    height: 100%;
    /* background-image: url(../assets/img/login-bg.jpg); */
    background-size: 100%;
}
.time {
    font-size: 13px;
    color: #999;
  }
  
  .bottom {
    margin-top: 13px;
    line-height: 12px;
  }

  .button {
    padding: 0;
    float: right;
  }

  .image {
    width: 100%;
    display: block;
  }

  .clearfix:before,
  .clearfix:after {
      display: table;
      content: "";
  }
  
  .clearfix:after {
      clear: both
  }
.el-row {
    margin-bottom: 20px;
  }
  .el-col {
    border-radius: 4px;
  }
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
    border-radius: 4px;
    min-height: 36px;
  }
  .row-bg {
    padding: 10px 0;
    background-color: #f9fafc;
  }
</style>
