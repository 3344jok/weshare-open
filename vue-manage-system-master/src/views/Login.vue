<template>
    <div class="login-wrap">
        <div class="ms-login">
            <div class="ms-title">WESHARE</div>
            <el-form :model="param" :rules="rules" ref="login" label-width="0px" class="ms-content">
                <el-form-item prop="username">
                    <el-input v-model="param.username" placeholder="username">
                        <template #prepend>
                            <el-button icon="el-icon-user"></el-button>
                        </template>
                    </el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input type="password" placeholder="password" v-model="param.password"
                        @keyup.enter="submitForm()">
                        <template #prepend>
                            <el-button icon="el-icon-lock"></el-button>
                        </template>
                    </el-input>
                </el-form-item>
                <div class="login-btn">
                    <el-button type="primary" @click="submitForm()">登录</el-button>
                </div>
                <div>
                    <p class="login-tips">
                        <router-link to="/register" class="login-tips">注册新的账户</router-link>
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
import qs from  'qs'  ;

export default {
    setup() {
        const router = useRouter();
        const param = reactive({
            username: "",
            password: "",
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
        };
        const login = ref(null);
        
        const submitForm = () => {
            if (param.username.length < 1) {
                ElMessage.warning("用户名长度至少 1 位");
                return;
            }
            if (param.password.length < 3) {
                ElMessage.warning("密码长度至少 3 位");
                return;
            }

            //直接向后端请求，后端传入参数为 null，需要通过 qs 进行对象解析传递
            let loginData = {
                username: param.username,
                password: param.password
            }
            
            axios.post('/api/login', qs.stringify(loginData))
                .then(response => {
                if (response.data.statusCode == 1000 || response.data.statusCode == 1001) {
                    ElMessage.success("登录成功");
                    sessionStorage.setItem("ms_username", param.username);
                    router.push("/");
                } else {
                    ElMessage.error(response.data.statusMsg);
                }
            })
                .catch(function (error) {
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
        };
    },
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
    top: 50%;
    width: 350px;
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
    width: 100%;
    height: 36px;
    margin-bottom: 10px;
}
.login-tips {
    font-size: 12px;
    line-height: 30px;
    color: #fff;
    text-align: center;
    
}
</style>