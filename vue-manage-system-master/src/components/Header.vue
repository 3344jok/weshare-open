<template>
    <div class="header">
        <!-- 折叠按钮 -->
        <div class="collapse-btn" @click="collapseChage">
            <i v-if="!sidebar.collapse" class="el-icon-s-fold"></i>
            <i v-else class="el-icon-s-unfold"></i>
        </div>
        <div class="logo">WESHARE</div>
        <div class="header-right">
            <div class="header-user-con">
                <!-- 消息中心 -->
                <div class="btn-bell">
                    <el-tooltip effect="dark" :content="messages.value?`有${len}条未读消息`:`消息中心`" placement="bottom">
                        <router-link to="/tabs">
                            <i class="el-icon-bell"></i>
                        </router-link>
                    </el-tooltip>
                    <span class="btn-bell-badge" v-if="messages.value"></span>
                </div>
                <!-- 用户头像 -->
                <div class="user-avator">
                    <img :src="pfp" id="pfp"/>
                </div>
                <!-- 用户名下拉菜单 -->
                <el-dropdown class="user-name" trigger="click" @command="handleCommand">
                    <span class="el-dropdown-link">
                        {{username}}
                        <i class="el-icon-caret-bottom"></i>
                    </span>
                    <template #dropdown>
                        <el-dropdown-menu>
                            <!-- <a href="https://github.com/lin-xin/vue-manage-system" target="_blank"> -->
                            <a href="" target="_blank">
                                <el-dropdown-item>项目仓库</el-dropdown-item>
                            </a>
                            <el-dropdown-item command="user">个人中心</el-dropdown-item>
                            <el-dropdown-item divided command="loginout">退出登录</el-dropdown-item>
                        </el-dropdown-menu>
                    </template>
                </el-dropdown>
            </div>
        </div>
    </div>
</template>
<script>
import { computed, onMounted } from "vue";
import { useSidebarStore } from '../store/sidebar'
import { useRouter } from "vue-router";
import axios from 'axios';
import { ElMessage } from "element-plus";
import { ref, reactive } from "vue";
import qs from  'qs'  ;
import { getCurrentInstance } from "vue";

var socket;
const messages= ref([]);;
function openSocket() {
    if(typeof(WebSocket) == "undefined") {
        console.log("您的浏览器不支持WebSocket");
    }else{
        console.log("您的浏览器支持WebSocket"); 
        var socketUrl="ws://47.113.194.136:8080/imserver/"+sessionStorage.getItem("ms_username");
        console.log(socketUrl);
        if(socket!=null){
            socket.close();
            socket=null;
        }
        socket = new WebSocket(socketUrl);
        //打开事件
        socket.onopen = function() {
            console.log("websocket已打开");
            //socket.send("这是来自客户端的消息" + location.href + new Date());
        };
        //获得消息事件
        socket.onmessage = function(msg) {
            // console.log(msg.data);
            axios.get('/api/header/getmessages' ,{
                params : { //请求参数
                    username : sessionStorage.getItem("ms_username")
                }
            })
                .then(response => {
                if (response.data.statusCode == 1000 || response.data.statusCode == 1001) {
                    messages.value=response.data.data;
                    // console.log(messages.value);
                    
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
            //发现消息进入    开始处理前端触发逻辑
        };
        //关闭事件
        socket.onclose = function() {
            console.log("websocket已关闭");
        };
        //发生了错误事件
        socket.onerror = function() {
            console.log("websocket发生了错误");
        }
    }
}
function sendMessage() {
    if(typeof(WebSocket) == "undefined") {
        console.log("您的浏览器不支持WebSocket");
    }else {
        console.log("您的浏览器支持WebSocket"); 
        console.log('{"toUserId":"'+$("#toUserId").val()+'","contentText":"'+$("#contentText").val()+'"}');
        socket.send('{"toUserId":"'+$("#toUserId").val()+'","contentText":"'+$("#contentText").val()+'"}');
    }
}
export default {
    messages,
    setup() {
        openSocket();
        const username = sessionStorage.getItem("ms_username");
        
        let pfp=null;
        let bg=null;
        const sidebar = useSidebarStore();
        // 侧边栏折叠
        const collapseChage = () => {
            sidebar.handleCollapse();
        };
        const userInfo = ref([]);
        const getUserInfo=()=>{
            axios.get('/api/header/getuserinfo' ,{
                params : { //请求参数
                    username : username
                }
            })
                .then(response => {
                if (response.data.statusCode == 1000 || response.data.statusCode == 1001) {
                    sessionStorage.setItem("emailAcount", response.data.data.emailAcount);
                    sessionStorage.setItem("userLoginId", response.data.data.userLoginId);
                    sessionStorage.setItem("id", response.data.data.id);
                    pfp='data:'+response.data.data.pfpType+';base64,'+response.data.data.profilePhoto;
                    bg='data:'+response.data.data.bgType+';base64,'+response.data.data.background;
                    sessionStorage.setItem("profilePhoto", pfp);
                    sessionStorage.setItem("background", bg);
                    document.getElementById("pfp").src=pfp;
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
        
        onMounted(() => {
            if (document.body.clientWidth < 1500) {
                collapseChage();
            }
        });

        // 用户名下拉菜单选择事件
        const router = useRouter();
        const handleCommand = (command) => {
            if (command == "loginout") {
                sessionStorage.clear();   
                axios.post('/api/logout')
                    .then(response => {
                    if (response.data.statusCode == 1000 || response.data.statusCode == 1001) {
                        ElMessage.success("注销成功");
                        sessionStorage.removeItem("ms_username");
                        router.push("/login");
                    } else if(response.data.statusCode == 4007) {
                        router.push("/login");
                        ElMessage.error(response.data.statusMsg);
                    } else {
                        ElMessage.error(response.data.statusMsg);
                    }
                })
                    .catch(function (error) {
                    ElMessage.error("未知错误");
                    console.log(error);
                    return false;
                });
            } else if (command == "user") {
                router.push("/user");
            }
        };

        
        getUserInfo();

        // const message = messages.value.unreadLen;
        const message=1;
        return {
            sidebar,
            username,
            message,
            pfp,
            bg,
            messages,
            collapseChage,
            handleCommand,
            getUserInfo,
        };
    },
};
</script>
<style scoped>
.header {
    position: relative;
    box-sizing: border-box;
    width: 100%;
    height: 70px;
    font-size: 22px;
    color: #fff;
}
.collapse-btn {
    float: left;
    padding: 0 21px;
    cursor: pointer;
    line-height: 70px;
}
.header .logo {
    float: left;
    width: 250px;
    line-height: 70px;
}
.header-right {
    float: right;
    padding-right: 50px;
}
.header-user-con {
    display: flex;
    height: 70px;
    align-items: center;
}
.btn-fullscreen {
    transform: rotate(45deg);
    margin-right: 5px;
    font-size: 24px;
}
.btn-bell,
.btn-fullscreen {
    position: relative;
    width: 30px;
    height: 30px;
    text-align: center;
    border-radius: 15px;
    cursor: pointer;
}
.btn-bell-badge {
    position: absolute;
    right: 0;
    top: -2px;
    width: 8px;
    height: 8px;
    border-radius: 4px;
    background: #f56c6c;
    color: #fff;
}
.btn-bell .el-icon-bell {
    color: #fff;
}
.user-name {
    margin-left: 10px;
}
.user-avator {
    margin-left: 20px;
}
.user-avator img {
    display: block;
    width: 40px;
    height: 40px;
    border-radius: 50%;
}
.el-dropdown-link {
    color: #fff;
    cursor: pointer;
}
.el-dropdown-menu__item {
    text-align: center;
}
</style>
