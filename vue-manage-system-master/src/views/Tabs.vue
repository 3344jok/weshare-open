<template>
    <div class="">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-lx-copy"></i> tab选项卡</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <el-tabs v-model="message">
                <el-tab-pane :label="`未读消息(${global.messages.value.unreadLen})`" name="first">
                    <el-table :data="global.messages.value.unread" :show-header="false" style="width: 100%">
                        <el-table-column>
                            <template #default="scope">
                                <span class="message-title">{{scope.row.content}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column prop="createDate" width="180"></el-table-column>
                        <el-table-column width="120">
                            <template #default="scope">
                                <el-button size="small" @click="handleRead(scope.row.id)">标为已读</el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                    <div class="handle-row">
                        <el-button type="primary" @click="handleAllRead()">全部标为已读</el-button>
                    </div>
                </el-tab-pane>
                <el-tab-pane :label="`已读消息`" name="second">
                    <template v-if="message === 'second'">
                        <el-table :data="global.messages.value.read" :show-header="false" style="width: 100%">
                            <el-table-column>
                                <template #default="scope">
                                    <span class="message-title">{{scope.row.content}}</span>
                                </template>
                            </el-table-column>
                            <el-table-column prop="createDate" width="150"></el-table-column>
                            <el-table-column width="120">
                                <template #default="scope">
                                    <!-- <el-button type="danger" @click="handleDel(scope.$index)">删除</el-button> -->
                                </template>
                            </el-table-column>
                        </el-table>
                        <!-- <div class="handle-row">
                            <el-button type="danger">删除全部</el-button>
                        </div> -->
                    </template>
                </el-tab-pane>
                <!-- <el-tab-pane :label="`回收站(${state.recycle.length})`" name="third">
                    <template v-if="message === 'third'">
                        <el-table :data="state.recycle" :show-header="false" style="width: 100%">
                            <el-table-column>
                                <template #default="scope">
                                    <span class="message-title">{{scope.row.title}}</span>
                                </template>
                            </el-table-column>
                            <el-table-column prop="date" width="150"></el-table-column>
                            <el-table-column width="120">
                                <template #default="scope">
                                    <el-button @click="handleRestore(scope.$index)">还原</el-button>
                                </template>
                            </el-table-column>
                        </el-table>
                        <div class="handle-row">
                            <el-button type="danger">清空回收站</el-button>
                        </div>
                    </template>
                </el-tab-pane> -->
            </el-tabs>
        </div>
    </div>
</template>

<script>
import { ref, reactive } from "vue";
import global from '../components/Header.vue'
import axios from 'axios';
import { ElMessage } from "element-plus";
export default {
    name: "tabs",
    setup() {
        // console.log("mess");
        // console.log(global.messages.value);

        const message = ref("first");
        

        const handleRead = (messageId) => {
            axios.get('/api/tabs/readone' ,{
                params : { //请求参数
                    messageId : messageId,
                    userName : sessionStorage.getItem("ms_username")
                }
            })
                .then(response => {
                if (response.data.statusCode == 1000 || response.data.statusCode == 1001) {
                    
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
        const handleAllRead = () => {
            axios.get('/api/tabs/readall' ,{
                params : { //请求参数
                    userName : sessionStorage.getItem("ms_username")
                }
            })
                .then(response => {
                if (response.data.statusCode == 1000 || response.data.statusCode == 1001) {
                    
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
        const handleDel = (index) => {
            const item = state.read.splice(index, 1);
            state.recycle = item.concat(state.recycle);
        };
        const handleRestore = (index) => {
            const item = state.recycle.splice(index, 1);
            state.read = item.concat(state.read);
        };

        return {
            message,
            global,
            handleRead,
            handleDel,
            handleAllRead,
            handleRestore,
        };
    },
};
</script>

<style>
.message-title {
    cursor: pointer;
}
.handle-row {
    margin-top: 30px;
}
</style>

