<template>
    <div>
        <el-row :gutter="20">
            <el-col :span="8">
                <el-card shadow="hover" class="mgb20" style="height:252px;">
                    <div class="user-info">
                        <img :src="dpfp" id="dpfp" class="user-avator" alt />
                        <div class="user-info-cont">
                            <div class="user-info-name">{{ name }}</div>
                            <div>{{ role }}</div>
                        </div>
                    </div>
                    <div class="user-info-list">
                        我的邮箱
                        <span>{{ email}}</span>
                    </div>
                    <div class="user-info-list">
                        上次登录地点：
                        <span>理塘</span>
                    </div>
                </el-card>
                <el-card shadow="hover" style="height:252px;">
                    <template #header>
                        <div class="clearfix">
                            <i class="el-icon-lx-settings" > </i>
                            <span style="padding-left: 12px;">修改个人信息</span>
                            <el-button style="float: right;" type="text">更改</el-button>
                        </div>
                    </template>
                    <div class="clearfix">
                            <i class="el-icon-lx-setting" > </i>
                            <span style="padding-left: 12px;">您可以重新设置您的</span>
                    </div>
                    <div class="clearfix" style="padding-top: 8px;">
                            <i class="el-icon-lx-people" style="padding-left: 40px;"> </i>
                            <span style="padding-left: 12px;">用户名</span>
                    </div>
                    <div class="clearfix" style="padding-top: 5px;">
                            <i class="el-icon-lx-mail" style="padding-left: 40px;"> </i>
                            <span style="padding-left: 12px;">邮箱</span>
                    </div>
                    <div class="clearfix" style="padding-top: 5px;">
                            <i class="el-icon-lx-profile" style="padding-left: 40px;"> </i>
                            <span style="padding-left: 12px;">头像</span>
                    </div>
                    <div class="clearfix" style="padding-top: 5px;">
                            <i class="el-icon-lx-like" style="padding-left: 40px;"> </i>
                            <span style="padding-left: 12px;">最中意的图片</span>
                    </div>
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
                                    <div>已经建立的相册集</div>
                                </div>
                            </div>
                        </el-card>
                    </el-col>
                    <el-col :span="8">
                        <el-card shadow="hover" :body-style="{ padding: '0px' }">
                            <div class="grid-content grid-con-2">
                                <i class="el-icon-message-solid grid-con-icon"></i>
                                <div class="grid-cont-right">
                                    <div class="grid-num"></div>
                                    <div></div>
                                </div>
                            </div>
                        </el-card>
                    </el-col>
                    <el-col :span="8">
                        <el-card shadow="hover" :body-style="{ padding: '0px' }">
                            <div class="grid-content grid-con-3">
                                <i class="el-icon-s-goods grid-con-icon"></i>
                                <div class="grid-cont-right">
                                    <div class="grid-num"></div>
                                    <div></div>
                                </div>
                            </div>
                        </el-card>
                    </el-col>
                </el-row> -->
                <el-card shadow="hover" style="height:525px;">
                    <template #header>
                        <div class="clearfix">
                            <i class="el-icon-lx-likefill" style="color:#dd553a" > </i>
                            <span style="padding-left: 12px;">最中意的图片</span>
                            
                        </div>
                    </template>
                    <img :src="dbg" id="dbg" class="user-background" alt />
                </el-card>
            </el-col>
        </el-row>
        <!-- <el-row :gutter="20">
            <el-col :span="12">
                <el-card shadow="hover">
                    <schart ref="bar" class="schart" canvasId="bar" :options="options"></schart>
                </el-card>
            </el-col>
            <el-col :span="12">
                <el-card shadow="hover">
                    <schart ref="line" class="schart" canvasId="line" :options="options2"></schart>
                </el-card>
            </el-col>
        </el-row> -->
    </div>
</template>

<script>
import Schart from "vue-schart";
import { reactive } from "vue";
import axios from 'axios';
import { ElMessage } from "element-plus";
import { p } from "../../dist/assets/vendor.f3d64f44";
import router from "../router";
export default {
    name: "dashboard",
    components: { Schart },
    setup() {
        const name = sessionStorage.getItem("ms_username");
        const role = name === "admin" ? "超级管理员" : "普通用户";
        let dpfp=null;
        let dbg=null;
        let email=null;
        const getUserInfo=()=>{
            axios.get('/api/header/getuserinfo' ,{
                params : { //请求参数
                    username : sessionStorage.getItem("ms_username")
                }
            })
                .then(response => {
                if (response.data.statusCode == 1000 || response.data.statusCode == 1001) {
                    dpfp='data:'+response.data.data.dpfpType+';base64,'+response.data.data.profilePhoto;
                    dbg='data:'+response.data.data.bgType+';base64,'+response.data.data.background;
                    sessionStorage.setItem("emailAcount", response.data.data.emailAcount);
                    sessionStorage.setItem("profilePhoto", dpfp);
                    sessionStorage.setItem("background", dbg);
                    sessionStorage.setItem("userLoginId", response.data.data.userLoginId);
                    sessionStorage.setItem("id", response.data.data.id);
                    email=sessionStorage.getItem("emailAcount");
                    document.getElementById("dpfp").src=dpfp;
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

        
        if(sessionStorage.getItem("id")==null){
            getUserInfo();
            dpfp=sessionStorage.getItem("profilePhoto");
            dbg=sessionStorage.getItem("background");
            email=sessionStorage.getItem("emailAcount");
        }
        else{
            dpfp=sessionStorage.getItem("profilePhoto");
            dbg=sessionStorage.getItem("background");
            email=sessionStorage.getItem("emailAcount");
        }
        
        const data = reactive([
            {
                name: "2018/09/04",
                value: 1083,
            },
            {
                name: "2018/09/05",
                value: 941,
            },
            {
                name: "2018/09/06",
                value: 1139,
            },
            {
                name: "2018/09/07",
                value: 816,
            },
            {
                name: "2018/09/08",
                value: 327,
            },
            {
                name: "2018/09/09",
                value: 228,
            },
            {
                name: "2018/09/10",
                value: 1065,
            },
        ]);
        const options = {
            type: "bar",
            title: {
                text: "最近一周各品类销售图",
            },
            xRorate: 25,
            labels: ["周一", "周二", "周三", "周四", "周五"],
            datasets: [
                {
                    label: "家电",
                    data: [234, 278, 270, 190, 230],
                },
                {
                    label: "百货",
                    data: [164, 178, 190, 135, 160],
                },
                {
                    label: "食品",
                    data: [144, 198, 150, 235, 120],
                },
            ],
        };
        const options2 = {
            type: "line",
            title: {
                text: "最近几个月各品类销售趋势图",
            },
            labels: ["6月", "7月", "8月", "9月", "10月"],
            datasets: [
                {
                    label: "家电",
                    data: [234, 278, 270, 190, 230],
                },
                {
                    label: "百货",
                    data: [164, 178, 150, 135, 160],
                },
                {
                    label: "食品",
                    data: [74, 118, 200, 235, 90],
                },
            ],
        };
        const todoList = reactive([
            {
                title: "今天要修复100个bug",
                status: false,
            },
            {
                title: "今天要修复100个bug",
                status: false,
            },
            {
                title: "今天要写100行代码加几个bug吧",
                status: false,
            },
            {
                title: "今天要修复100个bug",
                status: false,
            },
            {
                title: "今天要修复100个bug",
                status: true,
            },
            {
                title: "今天要写100行代码加几个bug吧",
                status: true,
            },
        ]);

        return {
            name,
            data,
            options,
            options2,
            todoList,
            role,
            dpfp,
            dbg,
            email,
            getUserInfo,
        };
    },
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
.user-background{
    margin: 0 auto;
    display: table;
    /* width: 400px;
    height: 300px; */
    object-fit:cover
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
</style>
