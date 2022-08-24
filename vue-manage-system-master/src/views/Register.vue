<template>
    <div class="login-wrap">
        <div class="ms-login">
            <div class="ms-title">WESHARE注册</div>
                <!-- <p class="login-btn"> -->
                    <!-- <img class="profile-photo" src="../assets/img/img.jpg" />    -->
                    <!-- <img class="profile-photo" src=imgSrc /> -->
                <!-- </p> -->
            
            <el-form :model="param" :rules="rules" ref="login" label-width="0px" class="ms-content">
                <el-form-item prop="username">
                    <el-input v-model="param.username" @blur="checkName()" placeholder="username"  >
                        <template #prepend>
                            <el-button icon="el-icon-user"></el-button>
                        </template>
                    </el-input>
                </el-form-item>
                <el-form-item prop="email">
                    <el-input v-model="param.email" placeholder="email" @blur="checkEmail()">
                        <template #prepend>
                            <el-button icon="el-icon-lx-mail"></el-button>
                        </template>
                    </el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input type="password" placeholder="password" v-model="param.password" >
                        <template #prepend>
                            <el-button icon="el-icon-lock"></el-button>
                        </template>
                    </el-input>
                </el-form-item>
                <el-form-item prop="retypePassword">
                    <el-input type="password" placeholder="Re-type password" @blur="checkReType()" v-model="param.retypePassword"
                        @keyup.enter="submitForm()">
                        <template #prepend>
                            <el-button icon="el-icon-lock"></el-button>
                        </template>
                    </el-input>
                </el-form-item>
                <div class="photo_tips">
                    <input  @change="uploadFilepfp($event)" type="file"> 
                </div>
                <div>
                    <p style="text-align: center;">
                        <img class="profile-photo" src="../assets/img/empty.png" id="pfp"/>
                        <p  class="_tips" >
                            头像
                        </p>
                    </p>
                </div>
                <div class="bg_tips">
                    <input  @change="uploadFilebg($event)" type="file"> 
                </div>
                <div>
                    <p style="text-align: center;">
                        <img class="bg-photo" src="../assets/img/empty.png" id="bg"/>
                        <p  class="_tips2" >
                            最中意的图片
                        </p>
                    </p>
                </div>

                <div class="login-btn">
                    <el-button type="primary" @click="submitForm()">注册</el-button>
                </div>
                <div>
                    <p class="login-tips">
                        <router-link to="/login" class="login-tips">返回登录界面</router-link>
                    </p>
                </div>
            </el-form>
        </div>
    </div>
</template>

<script>
import { ref, reactive } from "vue";
import { useTagsStore } from '../store/tags'
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import axios from 'axios';
import qs from 'qs'  ;

export default {
    name: "register",
    setup() {
        const router = useRouter();
        const param = reactive({
            username: "",
            password: "",
            retypePassword: "",
            email: "",
            profilePhoto: "",
            background: "",
            formData:new FormData()
        });

        const rules = {
            username: [
                {
                    required: true,
                    message: "请输入用户名",
                    trigger: "blur",
                },
            ],
            password: [
                { required: true, message: "请输入密码", trigger: "blur" },
            ],
            retypePassword: [
                {
                    required: true,
                    message: "请输入确认密码",
                    trigger: "blur",
                },
            ],
            email: [
                { required: true, message: "请输入邮箱账号", trigger: "blur" },
            ],
        };
        const login = ref(null);
        
        const checkReType = () =>{
            if (param.password!=param.retypePassword) {
                param.retypePassword="";
                ElMessage.warning("确认密码错误");
                return;
            }
        }

        const checkName = () => {
            if (param.username.length < 1) {
                ElMessage.warning("用户名不能为空");
                return;
            }
            let checkNameloginData = {
                username: param.username
            }
            axios.post('/api/register/checkname', qs.stringify(checkNameloginData))
                .then(response => {
                if (response.data.statusCode == 1000 || response.data.statusCode == 1001) {
                    
                } else {
                    ElMessage.error("该用户名已被占用");
                    param.username="";
                }
            })
                .catch(function (error) {
                ElMessage.error("未知错误");
                console.log(error);
                param.username="";
                return false;
            });
        };

        const checkEmail = () => {
            if (param.email.length < 1) {
                ElMessage.warning("邮箱不能为空");
                return;
            }
            let checkEmailloginData = {
                email: param.email
            }
            axios.post('/api/register/checkemail', qs.stringify(checkEmailloginData))
                .then(response => {
                if (response.data.statusCode == 1000 || response.data.statusCode == 1001) {
                    
                } else {
                    ElMessage.error("该邮箱已被占用");
                    param.email="";
                }
            })
                .catch(function (error) {
                ElMessage.error("未知错误");
                console.log(error);
                param.email="";
                return false;
            });
        };

        const submitForm = () => {
            // 直接向后端请求，后端传入参数为 null，需要通过 qs 进行对象解析传递
            if (param.username.length < 1) {
                ElMessage.warning("用户名不能为空");
                return;
            }if (param.email.length < 1) {
                ElMessage.warning("邮箱不能为空");
                return;
            }if (param.password.length < 3) {
                ElMessage.warning("密码长度至少3位");
                return;
            }if (param.password!=param.retypePassword) {
                ElMessage.warning("确认密码错误");
                return;
            }
            param.formData.append("userName",param.username);
            param.formData.append("password",param.password);
            param.formData.append("emailAcount",param.email);
            axios.post('/api/register', param.formData,{
                headers: {
                    "Content-Type": "application/json"
                },
            })
                .then(response => {
                if (response.data.statusCode == 1000 || response.data.statusCode == 1001) {
                    ElMessage.success("注册成功");
                    router.push("/login");
                } else {
                    param.formData=null;
                    param.formData=new formData();
                    ElMessage.error(response.data.statusMsg);
                    return false;
                }
            })
                .catch(function (error) {
                param.formData=null;
                param.formData=new formData();
                ElMessage.error("未知错误");
                console.log(error);
                return false;
            });
        };
        
        
        const tags = useTagsStore();
        tags.clearTags();

        return {
            param,
            rules,
            login,
            submitForm,
            checkName,
            checkEmail,
            checkReType,
        };
    },
    data() {
      return {
        pfpUrl: '',
        bgUrl:  '' 
      };
    },
    

    methods: {  
      uploadFilepfp (el) {
            if (!el.target.files[0].size) return; // 如果文件大小为0，则返回
            if (el.target.files[0].type.indexOf('image') === -1) { //如果不是图片格式
                // this.$dialog.toast({ mes: '请选择图片文件' });
                ElMessage.warning('请选择图片文件');
            } 
            else if(el.target.files[0].size>524288){
                ElMessage.warning("图片不能大于0.5M！");
            }else {
                const that = this;
                const reader = new FileReader(); // 创建读取文件对象
                reader.readAsDataURL(el.target.files[0]); // 发起异步请求，读取文件
                reader.onload = function () {  // 文件读取完成后
                // 读取完成后，将结果赋值给img的src
                that.valueUrl = reader.result;
                document.getElementById("pfp").src=that.valueUrl;
                };
                this.param.formData.append('profilePhoto', el.target.files[0]); 
                this.param.formData.append('pfpType', el.target.files[0].type); 
            }
        },
    uploadFilebg (el) {
            if (!el.target.files[0].size) return; // 如果文件大小为0，则返回
            if (el.target.files[0].type.indexOf('image') === -1) { //如果不是图片格式
                // this.$dialog.toast({ mes: '请选择图片文件' });
                ElMessage.warning('请选择图片文件');
            } 
            else if(el.target.files[0].size>1048576){
                ElMessage.warning("图片不能大于1M！");
            }else {
                const that = this;
                const reader = new FileReader(); // 创建读取文件对象
                reader.readAsDataURL(el.target.files[0]); // 发起异步请求，读取文件
                reader.onload = function () {  // 文件读取完成后
                // 读取完成后，将结果赋值给img的src
                that.valueUrl = reader.result;
                document.getElementById("bg").src=that.valueUrl;
                };
                this.param.formData.append('background', el.target.files[0]); 
                this.param.formData.append('bgType', el.target.files[0].type); 
            }
        },
    }
};
</script>

<style scoped>
.login-wrap {
    position: relative;
    width: 100%;
    height: 100%;
    background-image: url(../assets/img/login-bg.jpg);
    background-size: 100%;
}
.ms-title {
    width: 100%;
    line-height: 50px;
    text-align: center;
    font-size: 20px;
    color: #fff;
    border-bottom: 1px solid #ddd;
}
.ms-login {
    position: absolute;
    left: 50%;
    top: 200px;
    width: 420px;
    margin: -190px 0 0 -175px;
    border-radius: 5px;
    background: rgba(255, 255, 255, 0.3);
    overflow: hidden;
}
.ms-content {
    padding: 30px 30px;
}
.login-btn {
    text-align: center;
}
.login-btn button {
    position: relative;
    top:-250px;
    width: 100%;
    height: 30px;
    margin-bottom: 10px;
}
.login-tips {
    position: relative;
    top: -130px;
    font-size: 12px;
    line-height: 30px;
    color: #fff;
    text-align: center;
}
.photo_tips {
    font-size: 20px;
    line-height: 20px;
    color: rgba(48, 48, 48, 0.3);
}
.bg_tips {
    position: relative;
    top: -130px;
    font-size: 20px;
    line-height: 20px;
    color: rgba(48, 48, 48, 0.3);
}
._tips {
    position: relative;
    top: -70px;
    font-size: 10;
    line-height: 10;
    color: rgba(48, 48, 48, 0.3);
}
._tips2 {
    position: relative;
    top: -190px;
    font-size: 10;
    line-height: 10;
    color: rgba(48, 48, 48, 0.3);
}
.profile-photo{
    
    width:80px; 
    height:80px; 
    border-radius:60%; 
    text-align: center;
}
.bg-photo{
    position: relative;
    top: -120px;
    width:35%; 
    text-align: center;
}

.up-profile-photo{
    position: relative;
    top:0px;
    /* left: 30px; */

    /* display: flex; 新弹性盒布局 */
    /* align-items: center;水平居中 */

    width:200px; 
    /* height:10px;  */
}
  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }
  .avatar-uploader-icon {
    font-size: 12px;
    color: #8c939d;
    width: 178px;
    height: 100px;
    line-height: 178px;
    text-align: center;
  }
  .avatar {
    width: 178px;
    height: 178px;
    display: block;
  }
</style>